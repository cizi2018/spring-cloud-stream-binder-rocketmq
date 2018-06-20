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