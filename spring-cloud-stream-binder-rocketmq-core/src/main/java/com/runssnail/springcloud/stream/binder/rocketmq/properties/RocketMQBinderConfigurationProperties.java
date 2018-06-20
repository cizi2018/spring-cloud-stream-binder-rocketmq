package com.runssnail.springcloud.stream.binder.rocketmq.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.cloud.stream.rocketmq.binder")
public class RocketMQBinderConfigurationProperties {

    private String[] headers = new String[] {};

    /**
     * 生产者组名称
     */
    private String producerGroup;

    private String namesrvAddr;

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }
}
