package com.runssnail.springcloud.stream.binder.rocketmq;

import com.alibaba.rocketmq.client.producer.MQProducer;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQBinderConfigurationProperties;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQConsumerProperties;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQExtendedBindingProperties;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQProducerProperties;
import com.runssnail.springcloud.stream.binder.rocketmq.provisioning.RocketMQTopicProvisioner;

import org.springframework.cloud.stream.binder.AbstractMessageChannelBinder;
import org.springframework.cloud.stream.binder.BinderHeaders;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.binder.ExtendedPropertiesBinder;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.integration.core.MessageProducer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

public class RocketMQMessageChannelBinder extends AbstractMessageChannelBinder<ExtendedConsumerProperties<RocketMQConsumerProperties>,
        ExtendedProducerProperties<RocketMQProducerProperties>, RocketMQTopicProvisioner>
        implements ExtendedPropertiesBinder<MessageChannel, RocketMQConsumerProperties, RocketMQProducerProperties> {


    private MQProducer producer;

    private RocketMQBinderConfigurationProperties configurationProperties;

    private RocketMQExtendedBindingProperties extendedBindingProperties;

    public RocketMQMessageChannelBinder(RocketMQBinderConfigurationProperties configurationProperties, RocketMQTopicProvisioner provisioningProvider) {
        super(false, headersToMap(configurationProperties), provisioningProvider);
        this.configurationProperties = configurationProperties;

    }

    private static String[] headersToMap(RocketMQBinderConfigurationProperties configurationProperties) {
        String[] headersToMap;
        if (ObjectUtils.isEmpty(configurationProperties.getHeaders())) {
            headersToMap = BinderHeaders.STANDARD_HEADERS;
        }
        else {
            String[] combinedHeadersToMap = Arrays.copyOfRange(BinderHeaders.STANDARD_HEADERS, 0,
                    BinderHeaders.STANDARD_HEADERS.length + configurationProperties.getHeaders().length);
            System.arraycopy(configurationProperties.getHeaders(), 0, combinedHeadersToMap,
                    BinderHeaders.STANDARD_HEADERS.length,
                    configurationProperties.getHeaders().length);
            headersToMap = combinedHeadersToMap;
        }
        return headersToMap;
    }


    @Override
    protected MessageHandler createProducerMessageHandler(ProducerDestination destination, ExtendedProducerProperties<RocketMQProducerProperties> producerProperties, MessageChannel errorChannel) throws Exception {


        RocketMQProducerMessageHandler messageHandler = new RocketMQProducerMessageHandler(destination.getName(), configurationProperties, producerProperties);
        messageHandler.setBeanFactory(this.getBeanFactory());
        messageHandler.setProducer(this.producer);
        return messageHandler;
    }

    @Override
    protected MessageProducer createConsumerEndpoint(ConsumerDestination destination, String group, ExtendedConsumerProperties<RocketMQConsumerProperties> properties) throws Exception {

        RocketMQMessageDrivenChannelAdapter messageDrivenChannelAdapter = new RocketMQMessageDrivenChannelAdapter(destination, group, properties, this.configurationProperties);

        return messageDrivenChannelAdapter;
    }

    @Override
    public RocketMQConsumerProperties getExtendedConsumerProperties(String channelName) {
        return this.extendedBindingProperties.getExtendedConsumerProperties(channelName);
    }

    @Override
    public RocketMQProducerProperties getExtendedProducerProperties(String channelName) {
        return extendedBindingProperties.getExtendedProducerProperties(channelName);
    }

    public RocketMQBinderConfigurationProperties getConfigurationProperties() {
        return configurationProperties;
    }

    public void setConfigurationProperties(RocketMQBinderConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public RocketMQExtendedBindingProperties getExtendedBindingProperties() {
        return extendedBindingProperties;
    }

    public void setExtendedBindingProperties(RocketMQExtendedBindingProperties extendedBindingProperties) {
        this.extendedBindingProperties = extendedBindingProperties;
    }

    public MQProducer getProducer() {
        return producer;
    }

    public void setProducer(MQProducer producer) {
        this.producer = producer;
    }
}
