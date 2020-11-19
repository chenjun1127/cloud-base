package com.example.controller;

import com.example.service.SnowFlakeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Description:
 * @Author: chenjun
 * @Date: 2020/11/5 11:18
 */
@RestController
public class SnowflakeController {
    @Resource
    private SnowFlakeService snowFlakeService;

    @GetMapping("/snowflake")
    public Map<String, Object> getSnowflake() throws ExecutionException, InterruptedException {
        return snowFlakeService.getIdBySnowFlake();
    }
}
