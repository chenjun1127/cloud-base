package com.example.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.example.entities.Content;
import com.example.entities.Page;
import com.example.utils.EsTools;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: （查询京东数据，存储到ES，并显示页面）
 * @Author: chenjun
 * @Date: 2020/11/11 17:12
 */
@Service
public class ElasticSearchService {
    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private EsTools esTools;

    public static List<Content> contentList(String keyword) throws IOException {
        String url = "https://search.jd.com/Search?keyword=" + keyword;
        Document document = Jsoup.parse(new URL(url), 30000);
        Element element = document.getElementById("J_goodsList");
        Elements li = element.getElementsByTag("li");
        List<Content> list = new ArrayList<>();
        for (Element elements : li) {
            String img = elements.getElementsByTag("img").eq(0).attr("source-data-lazy-img");
            String price = elements.getElementsByClass("p-price").eq(0).text();
            String title = elements.getElementsByClass("p-name").eq(0).text();
            Content content = new Content();
            content.setImg(img);
            content.setPrice(price);
            content.setTitle(title);
            content.setCreateTime(new Date());
            content.setId(IdUtil.simpleUUID());
            list.add(content);
        }
        return list;
    }

    public String insetBatchJson(String keyword) throws IOException {
        List<Content> contents = contentList(keyword);
        BulkResponse bulkResponse = esTools.insertBatchJson(contents);
        return String.valueOf(bulkResponse.status());
    }

    public Map<String, Object> getById(String id) throws IOException {
        return esTools.get(id).getSourceAsMap();
    }

    public String delDocument(String index, String id) throws IOException {
        DeleteResponse deleteResponse = esTools.delete(index, id);
        if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
            return "delete fail: index:" + index + ",id:";
        } else {
            return "delete success!";
        }
    }

    public String delIndex(String index) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        boolean indicesExistsResponse = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        if (!indicesExistsResponse) {
            return "index 不存在";
        }
        AcknowledgedResponse acknowledgedResponse = esTools.deleteIndex(index);
        if (acknowledgedResponse.isAcknowledged()) {
            return "delete index success!";
        } else {
            return "delete index fail!";
        }
    }

    /*
     * 模糊查询加分页，未返回总页码数及当前页、数量
     */
    public <T> List<T> search(Integer pageNo, Integer pageSize, String keyword, Class<T> c) throws IOException {
        QueryBuilder queryBuilder = QueryBuilders.fuzzyQuery("title", keyword);
        SearchResponse searchResponse = esTools.search(pageNo, pageSize, queryBuilder);
        SearchHits searchHits = searchResponse.getHits();
        List<T> list = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            list.add(JSONUtil.toBean(searchHit.getSourceAsString(), c));
        }
        return list;
    }
    /*
     * 查询所有加分页，返回总页码数及当前页、数量
     */
    public <T> Page<T> searchAll(Integer pageNo, Integer pageSize, Class<T> c) throws IOException {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        SearchResponse searchResponse = esTools.searchAll(pageNo, pageSize, queryBuilder);
        SearchHits searchHits = searchResponse.getHits();
        TotalHits totalHits = searchHits.getTotalHits();
        int total = (int) totalHits.value;
        Page<T> page = new Page<>();
        page.setTotal(total);
        List<T> list = new ArrayList<>();
        for (SearchHit searchHit : searchHits) {
            list.add(JSONUtil.toBean(searchHit.getSourceAsString(), c));
        }
        page.setList(list);
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        return page;
    }
}
