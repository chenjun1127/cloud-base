package com.example.utils;

import cn.hutool.json.JSONUtil;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Description:
 * @Author: chenjun
 * @Date: 2020/11/11 16:45
 */
@Component
public class EsTools {
    @Resource
    private RestHighLevelClient restHighLevelClient;
    public static final String INDEX = "es-test";

    /*
     * 通过id获取数据
     */
    public GetResponse get(String id) throws IOException {
        GetRequest getRequest = new GetRequest(INDEX, id);
        return restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
    }

    /*
     * 删除document
     */
    public DeleteResponse delete(String index, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index);
        deleteRequest.id(id);
        return restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    /*
     * 创建索引，新版ES插入数据时自动创建
     */
    public CreateIndexResponse createIndex(String index) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        return restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    }

    /*
     * 删除索引
     */
    public AcknowledgedResponse deleteIndex(String index) throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        return restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
    }

    /*
     * 插入json数据
     */
    public IndexResponse insertJson(String jsonStr) throws IOException {
        IndexRequest indexRequest = new IndexRequest(INDEX);
        indexRequest.source(jsonStr, XContentType.JSON);
        return restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    /*
     * 批量插入json数据
     */
    public BulkResponse insertBatchJson(List<?> contentList) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        IndexRequest indexRequest;
        for (Object item : contentList) {
            indexRequest = new IndexRequest(INDEX);
            indexRequest.source(JSONUtil.toJsonStr(item), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        return restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    /*
     * 根据关键字模糊搜索
     */
    public SearchResponse search(Integer pageNo, Integer pageSize, QueryBuilder queryBuilder) throws IOException {
        return getSearchResponse(pageNo, pageSize, queryBuilder);
    }

    /*
     * 搜索全部
     */
    public SearchResponse searchAll(Integer pageNo, Integer pageSize, QueryBuilder queryBuilder) throws IOException {
        return getSearchResponse(pageNo, pageSize, queryBuilder);
    }

    /*
     * 搜索加分页
     */
    private SearchResponse getSearchResponse(Integer pageNo, Integer pageSize, QueryBuilder queryBuilder) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).from((pageNo - 1) * pageSize).size(pageSize);
        searchRequest.source(searchSourceBuilder);
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

}
