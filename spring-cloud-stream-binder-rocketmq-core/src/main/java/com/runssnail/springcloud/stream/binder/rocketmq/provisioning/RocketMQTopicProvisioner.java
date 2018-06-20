package com.runssnail.springcloud.stream.binder.rocketmq.provisioning;

import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQBinderConfigurationProperties;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQConsumerProperties;
import com.runssnail.springcloud.stream.binder.rocketmq.properties.RocketMQProducerProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.cloud.stream.provisioning.ProvisioningException;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;

public class RocketMQTopicProvisioner implements ProvisioningProvider<ExtendedConsumerProperties<RocketMQConsumerProperties>, ExtendedProducerProperties<RocketMQProducerProperties>>, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private RocketMQBinderConfigurationProperties configurationProperties;

    public RocketMQTopicProvisioner(RocketMQBinderConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public ProducerDestination provisionProducerDestination(String name, ExtendedProducerProperties<RocketMQProducerProperties> properties) throws ProvisioningException {

        if (this.logger.isInfoEnabled()) {
            this.logger.info("Using RocketMQ topic for outbound: {}", name);
        }

        int partitions = properties.getPartitionCount();

        RocketMQProducerDestination producerDestination = new RocketMQProducerDestination(name, partitions);

        return producerDestination;
    }

    @Override
    public ConsumerDestination provisionConsumerDestination(String name, String group, ExtendedConsumerProperties<RocketMQConsumerProperties> properties) throws ProvisioningException {

        if (this.logger.isInfoEnabled()) {
            this.logger.info("Using RocketMQ topic for inbound: {}", name);
        }

        if (properties.getInstanceCount() == 0) {
            throw new IllegalArgumentException("Instance count cannot be zero");
        }

        int partitionCount = properties.getInstanceCount() * properties.getConcurrency();

        ConsumerDestination consumerDestination = new RocketMQConsumerDestination(name);

        return consumerDestination;
    }


    private static final class RocketMQProducerDestination implements ProducerDestination {

        private final String producerDestinationName;

        private final int partitions;

        RocketMQProducerDestination(String destinationName, int partitions) {
            this.producerDestinationName = destinationName;
            this.partitions = partitions;
        }

        @Override
        public String getName() {
            return producerDestinationName;
        }

        @Override
        public String getNameForPartition(int partition) {
            return producerDestinationName;
        }

        @Override
        public String toString() {
            return "RocketMQProducerDestination{" +
                    "producerDestinationName='" + producerDestinationName + '\'' +
                    ", partitions=" + partitions +
                    '}';
        }
    }


    private static final class RocketMQConsumerDestination implements ConsumerDestination {

        private final String consumerDestinationName;

        private final int partitions;

        private final String dlqName;

        RocketMQConsumerDestination(String consumerDestinationName) {
            this(consumerDestinationName, 0, null);
        }

        RocketMQConsumerDestination(String consumerDestinationName, int partitions) {
            this(consumerDestinationName, partitions, null);
        }

        RocketMQConsumerDestination(String consumerDestinationName, Integer partitions, String dlqName) {
            this.consumerDestinationName = consumerDestinationName;
            this.partitions = partitions;
            this.dlqName = dlqName;
        }

        @Override
        public String getName() {
            return this.consumerDestinationName;
        }

        @Override
        public String toString() {
            return "RocketMQConsumerDestination{" +
                    "consumerDestinationName='" + consumerDestinationName + '\'' +
                    ", partitions=" + partitions +
                    ", dlqName='" + dlqName + '\'' +
                    '}';
        }
    }
}
