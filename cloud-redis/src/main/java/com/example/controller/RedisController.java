package com.example.controller;

import com.example.utils.RedisUtil;
import entities.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description:
 * @Author: chenjun
 * @Date: 2020/11/5 16:30
 */
@RestController
@RequestMapping("/redis")
public class RedisController {
    private static final int ExpireTime = 60;
    @Resource
    private RedisUtil redisUtil;

    @RequestMapping("/set")
    public Boolean redisSet(String key, String value) {
        User user = new User();
        user.setId(1L);
        user.setGuid(value);
        user.setName("Geo");
        user.setAge("20");
        user.setCreateTime(new Date());
        return redisUtil.set(key, user);
    }

    @RequestMapping("/get")
    public Object redisGet(String key) {
        return redisUtil.get(key);
    }

    @RequestMapping("/expire")
    public boolean expire(String key) {
        return redisUtil.expire(key, ExpireTime);
    }
}

