package com.runssnail.springcloud.stream.binder.rocketmq;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.runssnail.springcloud.stream.binder.rocketmq.constant.ProducerConstants;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQBinderConfigurationProperties;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQProducerProperties;

import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class RocketMQProducerMessageHandler extends AbstractReplyProducingMessageHandler {

    private String topic;

    private RocketMQBinderConfigurationProperties configurationProperties;

    private ExtendedProducerProperties<RocketMQProducerProperties> producerProperties;

    private MQProducer producer;


    public RocketMQProducerMessageHandler(String topic, RocketMQBinderConfigurationProperties configurationProperties, ExtendedProducerProperties<RocketMQProducerProperties> producerProperties) {
        this.topic = topic;
        this.configurationProperties = configurationProperties;
        this.producerProperties = producerProperties;
    }


    @Override
    protected void doInit() {
        super.doInit();

        if (this.producer == null) {

            String producerGroup = producerProperties.getExtension().getProducerGroup();

            if (StringUtils.isEmpty(producerGroup)) {
                producerGroup = ProducerConstants.DEFAULT_PRODUCER_GROUP;
            }
            Assert.notNull(producerGroup, "The producerGroup is required, you can use 'spring.cloud.stream.rocketmq.bindings.[channelName].producer.producerGroup' to setting");

            DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
            producer.setNamesrvAddr(this.configurationProperties.getNamesrvAddr());
            try {
                producer.start();
            } catch (MQClientException e) {
                throw new RuntimeException(e);
            }
        }

        Assert.notNull(this.producer, "The MQProducer is required");
    }

    @Override
    protected Object handleRequestMessage(Message<?> requestMessage) {

        SendResult sendResult = null;
        try {
            sendResult = producer.send(new com.alibaba.rocketmq.common.message.Message(this.topic, (byte[]) requestMessage.getPayload()));

        } catch (MQClientException e) {
            throw new RuntimeException(e);
        } catch (RemotingException e) {
            throw new RuntimeException(e);
        } catch (MQBrokerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return null;
    }

    public MQProducer getProducer() {
        return producer;
    }

    public void setProducer(MQProducer producer) {
        this.producer = producer;
    }
}
