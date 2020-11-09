package com.example.controller;

import cn.hutool.json.JSONUtil;
import com.example.entities.Message;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * @Description:
 * @Author: chenjun
 * @Date: 2020/11/9 15:05
 */
@RestController
public class KafkaProducer {
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/kafka/normal/{msg}")
    public void sendMessage(@PathVariable("msg") String msg) {
        Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        message.setSendTime(new Date());
        message.setMessage(msg);
        kafkaTemplate.send("test", JSONUtil.toJsonStr(message));
    }
}
