package com.runssnail.springcloud.stream.binder.rocketmq.properties;

public class RocketMQProducerProperties {

    private String producerGroup;

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }
}
