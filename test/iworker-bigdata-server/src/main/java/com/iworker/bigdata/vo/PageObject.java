package com.iworker.bigdata.vo;


import java.util.List;

public class PageObject {

    //当前页数
    private int pageNum;

    //总页数
    private int pageTotal;

    //内容
    private List<Data> data;

    public PageObject() {
    }

    public PageObject(int pageNum, int pageTotal, List<Data> data) {
        this.pageNum = pageNum;
        this.pageTotal = pageTotal;
        this.data = data;
    }

    public static class Data {

        private String data;

        private String id;

        private String name;

        public Data() {
        }

        public Data(String data, String id, String name) {
            this.data = data;
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
