package com.example.config;

import com.example.util.YamlPropertySourceFactory;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Description:
 * @Author: chenjun
 * @Date: 2020/11/13 16:39
 */
@Configuration
@PropertySource(value = "classpath:application-rocketmq.yml", factory = YamlPropertySourceFactory.class)
public class RocketmqProducerConfig {
    public static final Logger logger = LoggerFactory.getLogger(RocketmqProducerConfig.class);
    /**
     * 发送同一类消息的设置为同一个group，保证唯一,默认不需要设置，rocketmq会使用ip@pid(pid代表jvm名字)作为唯一标示
     */
    @Value("${rocketmq.producer.groupName}")
    private String groupName;
    @Value("${rocketmq.producer.namesrvAddr}")
    private String namesrvAddr;
    /**
     * 消息最大大小，默认4M
     */
    @Value("${rocketmq.producer.maxMessageSize}")
    private Integer maxMessageSize;
    /**
     * 消息发送超时时间，默认3秒
     */
    @Value("${rocketmq.producer.sendMsgTimeout}")
    private Integer sendMsgTimeout;
    /**
     * 消息发送失败重试次数，默认2次
     */
    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private Integer retryTimesWhenSendFailed;

    @Bean
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        logger.info("defaultProducer 正在创建---------------------------------------");
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(groupName);
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        defaultMQProducer.setVipChannelEnabled(false);
        defaultMQProducer.setMaxMessageSize(maxMessageSize);
        defaultMQProducer.setSendMsgTimeout(sendMsgTimeout);
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(retryTimesWhenSendFailed);
        defaultMQProducer.start();
        logger.info("rocketmq producer server 开启成功----------------------------------");
        return defaultMQProducer;
    }
}