package com.runssnail.springcloud.stream.binder.rocketmq.properties;

public class RocketMQConsumerProperties {

    private String consumerGroup;

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }
}
