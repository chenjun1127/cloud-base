package com.example.controller;

import com.example.util.YamlPropertySourceFactory;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: chenjun
 * @Date: 2020/11/13 17:16
 */
@RestController
@RequestMapping("/mqProducer")
@PropertySource(value = "classpath:application-rocketmq.yml", factory = YamlPropertySourceFactory.class)
public class RocketmqController {
    public static final Logger logger = LoggerFactory.getLogger(RocketmqController.class);
    @Value("${rocket.topic}")
    public String rocketTopic;
    @Value("${rocket.tag}")
    public String rocketTag;
    @Resource
    private DefaultMQProducer defaultMQProducer;

    @GetMapping("/send")
    public Object send(String msg) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        logger.info("发送MQ消息内容：" + msg);
        Message sendMsg = new Message(rocketTopic, rocketTag, "test_key", msg.getBytes());
        // 默认3秒超时
        SendResult sendResult = defaultMQProducer.send(sendMsg);
        logger.info("消息发送响应：" + sendResult.toString());
        return sendResult;
    }
}
