package com.example.entities;

import java.util.Date;

/**
 * @Description:
 * @Author: chenjun
 * @Date: 2020/11/9 15:13
 */
public class Message {
    private String id;
    private String message;
    private Date sendTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
