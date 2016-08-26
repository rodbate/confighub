package com.iworker.bigdata.common;


import java.util.List;

/**
 *
 * 常量类
 *
 */
public class Constants {

    //目标费用表
    public static final String TABLE_TARGET_DATA = "bs_target_data";

    //用户表
    public static final String TABLE_USERS = "bs_users";

    //组织表
    public static final String TABLE_ORGANIZATIONS = "bs_orgs";


    //hbase 目标销售额表 t_sale_target
    public static final String HBASE_SALE_TARGET = "t_sale_target";


    //hbase 表rowKey分隔符 #
    public static final String HBASE_ROWKEY_SEPARATOR = "#";

    //时间格式 yyyyMMdd
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";


    /**
     * 报表id 类型
     */
    public enum ReportIdType {

        //公司ID
        COMPANY(1),

        //部门ID
        ORG(2),

        //门店ID
        STORE(3),

        //人员ID
        USER(4),

        //商品类别ID
        PRODUCT_CATEGORY(5),

        //商品ID
        PRODUCT(6);


        private int type;

        ReportIdType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static ReportIdType build(int type){
            switch (type) {
                case 1 : return COMPANY;
                case 2 : return ORG;
                case 3 : return STORE;
                case 4 : return USER;
                case 5 : return PRODUCT_CATEGORY;
                case 6 : return PRODUCT;
                default: throw new RuntimeException("没有该类型");
            }
        }
    }

    /**
     *
     * 报表时间类型
     *
     */

    public enum ReportDateType {

        //本周
        THIS_WEEK(1),

        //上周
        LAST_WEEK(2),

        //本月
        THIS_MONTH(3),

        //上月
        LAST_MONTH(4),

        //本年
        THIS_YEAR(5),

        //去年
        LAST_YEAR(6),

        //上上周
        BEFORE_LAST_WEEK(7),

        //上上月
        BEFORE_LAST_MONTH(8);


        private int type;

        ReportDateType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static ReportDateType build(int type){

            switch (type) {
                case 1 : return THIS_WEEK;
                case 2 : return LAST_WEEK;
                case 3 : return THIS_MONTH;
                case 4 : return LAST_MONTH;
                case 5 : return THIS_YEAR;
                case 6 : return LAST_YEAR;
                case 7 : return BEFORE_LAST_WEEK;
                case 8 : return BEFORE_LAST_MONTH;
                default: throw new RuntimeException("没有该类型");
            }
        }
    }


    /**
     *
     * 报表数据类型
     */
    public enum ReportDataType{

        //完成率
        COMPLETION_RATE(1),

        //销售额
        SALES_AMOUNT(2),

        //成交量
        TRADING(3),

        //成交单数
        TURNOVER(4);



        private int type;

        ReportDataType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static ReportDataType build(int type){

            switch (type) {
                case 1 : return COMPLETION_RATE;
                case 2 : return SALES_AMOUNT;
                case 3 : return TRADING;
                case 4 : return TURNOVER;
                default: throw new RuntimeException("没有该类型");
            }
        }
    }



    public static class OrgIncludeOrgStore {

        private String currentOrgId;

        private List<String> includedOrgIds;

        private List<String> includedStoreIds;

        public OrgIncludeOrgStore() {
        }

        public OrgIncludeOrgStore(String currentOrgId, List<String> includedOrgIds, List<String> includedStoreIds) {
            this.currentOrgId = currentOrgId;
            this.includedOrgIds = includedOrgIds;
            this.includedStoreIds = includedStoreIds;
        }

        public String getCurrentOrgId() {
            return currentOrgId;
        }

        public void setCurrentOrgId(String currentOrgId) {
            this.currentOrgId = currentOrgId;
        }

        public List<String> getIncludedOrgIds() {
            return includedOrgIds;
        }

        public void setIncludedOrgIds(List<String> includedOrgIds) {
            this.includedOrgIds = includedOrgIds;
        }

        public List<String> getIncludedStoreIds() {
            return includedStoreIds;
        }

        public void setIncludedStoreIds(List<String> includedStoreIds) {
            this.includedStoreIds = includedStoreIds;
        }
    }
}
