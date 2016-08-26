package com.iworker.bigdata.vo;


import java.util.List;

/**
 * 销售排行榜值对象
 *
 *
 */
public class TopRankVO {

    //id 取决于 idType 类型
    private int id;

    //id 类型
    private int idType;

    //公司id
    private int companyId;

    //数据类型
    private int dataType;

    //时间范围类型
    private int dateType;

    //当前页
    private int pageNo;

    //每页显示数据的条数
    private int pageSize;


    private DataList dataList;

    private ShowList showList;

    public TopRankVO() {
    }

    public TopRankVO(int id, int idType, int companyId, int dataType, int dateType, int pageNo, int pageSize,
                     DataList dataList, ShowList showList) {
        this.id = id;
        this.idType = idType;
        this.companyId = companyId;
        this.dataType = dataType;
        this.dateType = dateType;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.dataList = dataList;
        this.showList = showList;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getDateType() {
        return dateType;
    }

    public void setDateType(int dateType) {
        this.dateType = dateType;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    public ShowList getShowList() {
        return showList;
    }

    public void setShowList(ShowList showList) {
        this.showList = showList;
    }


    public static class ShowList {

    }

    /**
     * 需要处理的部门和门店对象
     *
     */
    public static class OrgStoreShowList extends ShowList{

        List<String> orgLongId;

        List<String> storeId;

        public OrgStoreShowList() {
        }

        public OrgStoreShowList(List<String> orgLongId, List<String> storeId) {
            this.orgLongId = orgLongId;
            this.storeId = storeId;
        }

        public List<String> getOrgLongId() {
            return orgLongId;
        }

        public void setOrgLongId(List<String> orgLongId) {
            this.orgLongId = orgLongId;
        }

        public List<String> getStoreId() {
            return storeId;
        }

        public void setStoreId(List<String> storeId) {
            this.storeId = storeId;
        }
    }

    /**
     * 需要处理的成员对象
     *
     */
    public static class UserShowList extends ShowList {

        private List<String> userIds;

        public UserShowList() {
        }

        public UserShowList(List<String> userIds) {
            this.userIds = userIds;
        }

        public List<String> getUserIds() {
            return userIds;
        }

        public void setUserIds(List<String> userIds) {
            this.userIds = userIds;
        }
    }

    public static class DataList {


    }

    /**
     *
     *
     * 部门和门店数据处理的逻辑对象
     */
    public static class OrgStoreDataList extends DataList{

        private List<String> orgLongIds;

        private List<Store> stores;

        public OrgStoreDataList() {
        }

        public OrgStoreDataList(List<String> orgLongIds, List<Store> stores) {
            this.orgLongIds = orgLongIds;
            this.stores = stores;
        }

        public List<String> getOrgLongIds() {
            return orgLongIds;
        }

        public void setOrgLongIds(List<String> orgLongIds) {
            this.orgLongIds = orgLongIds;
        }

        public List<Store> getStores() {
            return stores;
        }

        public void setStores(List<Store> stores) {
            this.stores = stores;
        }
    }


    /**
     *
     *
     * 成员数据处理逻辑对象
     */
    public static class UserDataList extends DataList {

        private List<String> userIds;

        public UserDataList() {
        }

        public UserDataList(List<String> userIds) {
            this.userIds = userIds;
        }

        public List<String> getUserIds() {
            return userIds;
        }

        public void setUserIds(List<String> userIds) {
            this.userIds = userIds;
        }
    }


    /**
     *
     * 数据处理store对象
     */
    public static class Store {

        private String orgId;

        private String storeIds;

        private String orgLongId;

        public Store() {
        }

        public Store(String orgId, String storeIds, String orgLongId) {
            this.orgId = orgId;
            this.storeIds = storeIds;
            this.orgLongId = orgLongId;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public String getStoreIds() {
            return storeIds;
        }

        public void setStoreIds(String storeIds) {
            this.storeIds = storeIds;
        }

        public String getOrgLongId() {
            return orgLongId;
        }

        public void setOrgLongId(String orgLongId) {
            this.orgLongId = orgLongId;
        }
    }



}
