package com.example.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 配置类(ES可以作为一个独立的单个搜索服务器,不过为了处理大型数据集,实现容错和高可用性,ES可以运行在许多互相合作的服务器上集群,本案例是单个服务器
 * @Author: chenjun
 * @Date: 2020/11/11 16:33
 */
@Configuration
public class ElasticSearchConfig {
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")
        ));
    }
}
