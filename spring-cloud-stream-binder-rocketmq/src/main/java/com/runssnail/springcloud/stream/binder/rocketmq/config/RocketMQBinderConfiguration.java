package com.runssnail.springcloud.stream.binder.rocketmq.config;


import com.runssnail.springcloud.stream.binder.rocketmq.RocketMQMessageChannelBinder;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQBinderConfigurationProperties;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQExtendedBindingProperties;
import com.runssnail.springcloud.stream.binder.rocketmq.provisioning.RocketMQTopicProvisioner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.cloud.stream.config.codec.kryo.KryoCodecAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.codec.Codec;

@Configuration
@ConditionalOnMissingBean(Binder.class)
@Import({KryoCodecAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class})
@EnableConfigurationProperties({RocketMQExtendedBindingProperties.class})
public class RocketMQBinderConfiguration {

    @Autowired
    private Codec codec;

    @Autowired
    private RocketMQExtendedBindingProperties extendedBindingProperties;

//    @Autowired
//    private MQProducer producer;


//    /**
//     * 可以和应用共享producer，所以这里先判断有没有bean存在
//     *
//     * @param configurationProperties
//     * @return
//     */
//    @Bean
//    @ConditionalOnMissingBean(MQProducer.class)
//    MQProducer createMQProducer(RocketMQBinderConfigurationProperties configurationProperties) {
//
//        String producerGroup = configurationProperties.getProducerGroup();
//        if (StringUtils.isEmpty(producerGroup)) {
//            producerGroup = ProducerConstants.DEFAULT_PRODUCER_GROUP;
//        }
//        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
//        producer.setNamesrvAddr(configurationProperties.getNamesrvAddr());
//        try {
//            producer.start();
//        } catch (MQClientException e) {
//            throw new RuntimeException(e);
//        }
//        return producer;
//    }

    @Bean
    RocketMQBinderConfigurationProperties configurationProperties() {
        return new RocketMQBinderConfigurationProperties();
    }

    @Bean
    RocketMQTopicProvisioner provisioningProvider(RocketMQBinderConfigurationProperties configurationProperties) {
        return new RocketMQTopicProvisioner(configurationProperties);
    }

    @Bean
    RocketMQMessageChannelBinder rocketMQMessageChannelBinder(RocketMQBinderConfigurationProperties configurationProperties,
                                                           RocketMQTopicProvisioner provisioningProvider) {

        RocketMQMessageChannelBinder messageChannelBinder = new RocketMQMessageChannelBinder(
                configurationProperties, provisioningProvider);
        messageChannelBinder.setExtendedBindingProperties(this.extendedBindingProperties);
//        messageChannelBinder.setProducer(this.producer);

        messageChannelBinder.setCodec(this.codec);


        return messageChannelBinder;
    }

}
