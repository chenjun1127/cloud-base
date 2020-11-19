package com.example.service;

import com.example.utils.IdGeneratorSnowflake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
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

    /**
     * 线程池处理20个请求；
     */

    public Map<String, Object> getIdBySnowFlake() throws ExecutionException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        // 线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
        ExecutorService executorService = new ThreadPoolExecutor(5, 20, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

        List<Long> list = new ArrayList<>();
        List<Future<Long>> futureList = new ArrayList<Future<Long>>();

        // 高速提交20个任务，每个任务返回一个Future入list
        for (int i = 0; i < 20; i++) {
            futureList.add(executorService.submit(new Callable<Long>() {
                @Override
                public Long call() throws Exception {
                    logger.info("当前线程：" + Thread.currentThread().getName());
                    return idGeneratorSnowflake.snowflakeId();
                }
            }));
        }
        // 结果归集，用迭代器遍历futureList,高速轮询（模拟实现了并发），任务完成就移除
        while (futureList.size() > 0) {
            Iterator<Future<Long>> iterator = futureList.iterator();
            while (iterator.hasNext()) {
                Future<Long> future = iterator.next();
                // 如果任务完成取结果，否则判断下一个任务是否完成
                if (future.isDone() && !future.isCancelled()) {
                    Long id = future.get();
                    logger.info("任务id=" + id + "获取完成，移出任务队列！");
                    list.add(id);
                    iterator.remove();
                }
            }
        }
        executorService.shutdown();
        map.put("size", list.size());
        map.put("list", list);
        return map;
    }
}
