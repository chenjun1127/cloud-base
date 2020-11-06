package com.example.service;

import com.example.utils.IdGeneratorSnowflake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Description:
 * @Author: chenjun
 * @Date: 2020/11/5 11:11
 */
@Service
public class SnowFlakeService {
    private final Logger logger = LoggerFactory.getLogger(SnowFlakeService.class);
    @Resource
    private IdGeneratorSnowflake idGeneratorSnowflake;

    /*
     * @Description: 5个线程程处理20个请求；
     * @Param: []
     * @Return: java.util.List<java.lang.Long>
     */
    public List<Long> getIdBySnowFlake() {
        // 线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
        // ExecutorService executorService = Executors.newFixedThreadPool(5);
        ExecutorService executorService = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            executorService.execute(() -> {
                logger.info(Thread.currentThread().getName());
                list.add(idGeneratorSnowflake.snowflakeId());
            });
        }
        executorService.shutdown();
        return list;
    }
}
