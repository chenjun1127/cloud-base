package com.example.config;

import com.example.util.MQConsumeMsgListenerProcessor;
import com.example.util.YamlPropertySourceFactory;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: chenjun
 * @Date: 2020/11/13 17:03
 */
@Configuration
@PropertySource(value = "classpath:application-rocketmq.yml", factory = YamlPropertySourceFactory.class)
public class RocketmqConsumerConfig {
    public static final Logger logger = LoggerFactory.getLogger(RocketmqConsumerConfig.class);
    @Value("${rocketmq.consumer.namesrvAddr}")
    private String namesrvAddr;
    @Value("${rocketmq.consumer.groupName}")
    private String groupName;
    @Value("${rocketmq.consumer.consumeThreadMin}")
    private int consumeThreadMin;
    @Value("${rocketmq.consumer.consumeThreadMax}")
    private int consumeThreadMax;
    @Value("${rocketmq.consumer.topics}")
    private String topics;
    @Value("${rocketmq.consumer.consumeMessageBatchMaxSize}")
    private int consumeMessageBatchMaxSize;
    @Resource
    private MQConsumeMsgListenerProcessor mqConsumeMsgListenerProcessor;

    @Bean
    public DefaultMQPushConsumer defaultMQPushConsumer() throws MQClientException {
        logger.info("defaultConsumer 正在创建---------------------------------------");
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(groupName);
        defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
        defaultMQPushConsumer.setConsumeThreadMin(consumeThreadMin);
        defaultMQPushConsumer.setConsumeThreadMax(consumeThreadMax);
        defaultMQPushConsumer.setConsumeMessageBatchMaxSize(consumeMessageBatchMaxSize);
        // 设置监听
        defaultMQPushConsumer.registerMessageListener(mqConsumeMsgListenerProcessor);
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        try {
            // 设置该消费者订阅的主题和tag，如果订阅该主题下的所有tag，则使用*,
            String[] topicArr = topics.split(";");
            for (String tag : topicArr) {
                String[] tagArr = tag.split("~");
                defaultMQPushConsumer.subscribe(tagArr[0], tagArr[1]);
            }
            defaultMQPushConsumer.start();
            logger.info("consumer 创建成功 groupName={}, topics={}, namesrvAddr={}", groupName, topics, namesrvAddr);
        } catch (MQClientException e) {
            logger.error("consumer 创建失败!");
        }
        return defaultMQPushConsumer;
    }
}
