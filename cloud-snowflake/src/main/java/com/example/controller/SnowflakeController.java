package com.example.controller;

import com.example.service.SnowFlakeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description:
 * @Author: chenjun
 * @Date: 2020/11/5 11:08
 */
@RestController
public class SnowflakeController {
    @Resource
    private SnowFlakeService snowFlakeService;
    @GetMapping("/snowflake")
    public List<Long> index(){
        return snowFlakeService.getIdBySnowFlake();
    }
}
