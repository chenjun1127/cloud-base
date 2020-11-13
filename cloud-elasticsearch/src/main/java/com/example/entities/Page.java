package com.example.entities;

import java.util.List;

/**
 * @Description: 分页实体类
 * @Author: chenjun
 * @Date: 2020/11/13 9:27
 */
public class Page<T> {
    private int pageSize;
    private int pageNo;
    private List<T> list;
    private int total;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
