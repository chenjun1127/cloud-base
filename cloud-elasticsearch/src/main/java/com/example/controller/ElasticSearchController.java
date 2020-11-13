package com.example.controller;

import com.example.entities.Content;
import com.example.entities.Page;
import com.example.service.ElasticSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chenjun
 * @Date: 2020/11/11 17:34
 */
@RestController
@RequestMapping("es")
public class ElasticSearchController {
    @Resource
    private ElasticSearchService elasticSearchService;

    /*
     * 批量插入JSON数据
     */
    @GetMapping("/insertBatch")
    public String insetBatchJson(String keyword) throws IOException {
        return elasticSearchService.insetBatchJson(keyword);
    }

    /*
     * 通过ID查询数据
     */
    @GetMapping("/getById")
    public Map<String, Object> getById(String id) throws IOException {
        return elasticSearchService.getById(id);
    }

    /*
     * 删除数据根据ID和索引
     */
    @GetMapping("/deleteDocument")
    public String delDocument(String index, String id) throws IOException {
        return elasticSearchService.delDocument(index, id);
    }

    /*
     * 删除索引，库里的数据也会被删除
     */
    @GetMapping("/deleteIndex")
    public String deleteIndex(String index) throws IOException {
        return elasticSearchService.delIndex(index);
    }

    /*
     * 搜索分页
     */
    @GetMapping("/search")
    public List<Content> search(@RequestParam(defaultValue = "1", value = "pageNo") Integer pageNo, @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize, String keyword) throws IOException {
        return elasticSearchService.search(pageNo, pageSize, keyword, Content.class);
    }

    @GetMapping("/searchAll")
    public Page<Content> searchAll(@RequestParam(defaultValue = "1", value = "pageNo") Integer pageNo, @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize) throws IOException {
        return elasticSearchService.searchAll(pageNo, pageSize, Content.class);
    }
}
