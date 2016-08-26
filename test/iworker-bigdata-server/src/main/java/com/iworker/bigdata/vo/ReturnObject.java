package com.iworker.bigdata.vo;




public class ReturnObject {

    //为时间戳
    private long id;

    //返回码
    private int code;

    //返回信息
    private String message;

    //返回内容
    private Object content;


    public ReturnObject() {
    }

    public ReturnObject(long id, int code, String message, Object content) {
        this.id = id;
        this.code = code;
        this.message = message;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
