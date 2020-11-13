package com.example.entities;

import java.util.Date;

/**
 * @Description:
 * @Author: chenjun
 * @Date: 2020/11/11 17:18
 */
public class Content {
    private static final long serialVersionUID = 1L;
    private String id;
    private String img;
    private String title;
    private String price;
    private Date createTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
