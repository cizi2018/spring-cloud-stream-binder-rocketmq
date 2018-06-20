### springcloud stream binder for rocketmq

##### <font color="#dd0000">**注意如果broker设置了autoCreateTopicEnable=false，那么请先创建好对应的topic**</font>

* 下载源码并maven install


* 应用添加maven依赖

```
<dependency>
    <groupId>com.runssnail.springcloud</groupId>
    <artifactId>spring-cloud-stream-binder-rocketmq</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

or

```
<dependency>
    <groupId>com.runssnail.springcloud</groupId>
    <artifactId>spring-cloud-starter-stream-rocketmq</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

```

* binder配置项

    配置项 | 说明 |
    :---: | :---: |
    spring.cloud.stream.rocketmq.binder.namesrvAddr | namesrv地址，例如：localhost:9876 |
    spring.cloud.stream.rocketmq.bindings.[channelName].producer.producerGroup | 生产者组名，接入应用必须设置，例如：spring.cloud.stream.rocketmq.bindings.sleuth.producer.producerGroup=zipkin |
    spring.cloud.stream.rocketmq.bindings.[channelName].conmsumer.consumerGroup | 消费者组名，zipkin-server必须设置，例如：spring.cloud.stream.rocketmq.bindings.sleuth.consumer.consumerGroup=zipkin |
     
 