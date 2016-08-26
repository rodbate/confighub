package com.iworker.bigdata.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 销售目标或费用值对象
 *
 *
 * {
        "companyId":1,      公司id
        "year":2016,        年
        "dataList":[
            {
                "month":1,
                "amount":100
            },
            {
                "month":2,
                "amount":100
            }
        ],
        "users":{
            "user":"1,2,3",   用户id
            "org":"1,2" ,     组织id
            "store":"1,2"     组织id
        }

 }
 *
 *
 *
 */


public class TargetFeeVO {


    private int companyId;

    private int year;

    private List<MonthToAmount> dataList = new ArrayList<>();

    private Users users;

    public TargetFeeVO() {
    }

    public TargetFeeVO(int companyId, int year, List<MonthToAmount> dataList, Users users) {
        this.companyId = companyId;
        this.year = year;
        this.dataList = dataList;
        this.users = users;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<MonthToAmount> getDataList() {
        return dataList;
    }

    public void setDataList(List<MonthToAmount> dataList) {
        this.dataList = dataList;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

   public static class MonthToAmount {

        private int month;

        private double amount;

       public MonthToAmount() {
       }

       public MonthToAmount(int month, double amount) {
           this.month = month;
           this.amount = amount;
       }

       public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

    public static class Users {

        private String user;

        private String org;

        private String store;

        public Users() {
        }

        public Users(String user, String org, String store) {
            this.user = user;
            this.org = org;
            this.store = store;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getOrg() {
            return org;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }
    }



}
