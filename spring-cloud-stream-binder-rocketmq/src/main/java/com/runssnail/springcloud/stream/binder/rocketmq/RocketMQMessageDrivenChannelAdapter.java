package com.runssnail.springcloud.stream.binder.rocketmq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQBinderConfigurationProperties;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQConsumerProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author zhengwei
 */
public class RocketMQMessageDrivenChannelAdapter extends MessageProducerSupport {


    private DefaultMQPushConsumer consumer;

    private ConsumerDestination consumerDestination;

    private String consumerGroup;

    private ExtendedConsumerProperties<RocketMQConsumerProperties> consumerProperties;

    private RocketMQBinderConfigurationProperties configurationProperties;

    public RocketMQMessageDrivenChannelAdapter(ConsumerDestination destination, String consumerGroup, ExtendedConsumerProperties<RocketMQConsumerProperties> consumerProperties, RocketMQBinderConfigurationProperties configurationProperties) {
        this.consumerDestination = destination;
        this.consumerGroup = consumerGroup;
        this.consumerProperties = consumerProperties;

        this.configurationProperties = configurationProperties;

        this.consumerGroup = consumerGroup;

    }


    @Override
    protected void doStart() {

        String consumerGroup = this.consumerProperties.getExtension().getConsumerGroup();

        if (StringUtils.isEmpty(consumerGroup)) {
            consumerGroup = this.consumerGroup;
        }

        Assert.notNull(consumerGroup, "The 'consumerGroup' is required, you can use 'spring.cloud.stream.bindings.[channelName].group' or 'spring.cloud.stream.rocketmq.binder.consumerGroup' to setting");

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(configurationProperties.getNamesrvAddr());
        consumer.registerMessageListener(new RocketMQMessageListener());
        this.consumer = consumer;


        try {
            this.consumer.subscribe(this.consumerDestination.getName(), "*");
            this.consumer.start();
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }

    }


    private class RocketMQMessageListener implements MessageListenerConcurrently {

        private Logger logger = LoggerFactory.getLogger(RocketMQMessageListener.class);

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

            logger.info("receive messages[size={}]: {}", msgs.size(), msgs);

            for (MessageExt msg : msgs) {
                byte[] payload = msg.getBody();

                Message<?> internalMsgObject = getMessageBuilderFactory().withPayload(payload).build();
                sendMessage(internalMsgObject);

            }


            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }


    @Override
    protected void doStop() {
        this.consumer.shutdown();
    }

}
