package com.runssnail.springcloud.stream.binder.rocketmq;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.MQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQBinderConfigurationProperties;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQProducerProperties;

import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

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

        Assert.notNull(this.producer, "MQProducer is required");
//        if (this.producer == null) {
//            DefaultMQProducer  producer = new DefaultMQProducer("springcloud-binder-rocketmq");
//            producer.setNamesrvAddr(this.configurationProperties.getNamesrvAddr());
//            try {
//                producer.start();
//            } catch (MQClientException e) {
//                throw new RuntimeException(e);
//            }
//
//            this.producer = producer;
//        }


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
