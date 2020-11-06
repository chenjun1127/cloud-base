package com.example.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Description: hutool工具包生成snowflake
 * @Author: chenjun
 * @Date: 2020/11/5 11:14
 */
@Component
public class IdGeneratorSnowflake {
    private final Logger logger = LoggerFactory.getLogger(IdGeneratorSnowflake.class);
    private long workId = 1;
    private final long dataCenterId = 1;
    private final Snowflake snowflake = IdUtil.createSnowflake(workId, dataCenterId);

    /*
     * @Description: 加载一些初始化的工作；
     * @Param: []
     * @Return: void
     */
    @PostConstruct
    public void init() {
        try {
            workId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            logger.info("当前机器的workId: " + workId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("当前机器的workId获取失败", e);
        }
    }

    public synchronized long snowflakeId() {
        return snowflake.nextId();
    }

    public synchronized long snowflakeId(long workId, long dataCenterId) {
        Snowflake snowflake = IdUtil.createSnowflake(workId, dataCenterId);
        return snowflake.nextId();
    }
}
