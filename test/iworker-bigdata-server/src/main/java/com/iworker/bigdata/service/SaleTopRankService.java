package com.iworker.bigdata.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iworker.bigdata.common.Constants;
import com.iworker.bigdata.vo.TopRankVO;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.iworker.bigdata.common.Constants.*;
import static com.iworker.bigdata.common.CommonUtil.*;

/**
 *
 * 销售排行榜业务处理类
 *
 */

@Service
public class SaleTopRankService {


    /**
     * 处理报表数据
     * @param data 数据
     * @return json 数据
     * @throws Exception
     */
    public String handleReport(String data) throws Exception{


        TopRankVO topRankVO = buildVO(data);

        int companyId = topRankVO.getCompanyId();

        ReportIdType idType = ReportIdType.build(topRankVO.getIdType());

        ReportDateType dateType = ReportDateType.build(topRankVO.getDateType());

        ReportDataType dataType = ReportDataType.build(topRankVO.getDataType());

        //1 首先判断 idType
        //当id type 为 company
        if (idType == ReportIdType.COMPANY) {

            //
            List<OrgIncludeOrgStore> orgIncludeOrgStores = new ArrayList<>();

            //计算需要处理的部门
            TopRankVO.OrgStoreShowList showList = (TopRankVO.OrgStoreShowList)topRankVO.getShowList();

            TopRankVO.OrgStoreDataList dataList = (TopRankVO.OrgStoreDataList) topRankVO.getDataList();

            List<String> orgLongIds = showList.getOrgLongId();
            List<String> storeIds = showList.getStoreId();

            List<String> orgLongIdDatas = dataList.getOrgLongIds();
            List<TopRankVO.Store> stores = dataList.getStores();

            for (String orgLongId : orgLongIds) {

                OrgIncludeOrgStore orgIncludeOrgStore = new OrgIncludeOrgStore();

                String orgId = getOwnId(orgLongId);

                orgIncludeOrgStore.setCurrentOrgId(orgId);

                List<String> includedOrgIds = new ArrayList<>();

                List<String> includedStoreIds = new ArrayList<>();

                //处理部门
                for (String orgLongIdData : orgLongIdDatas) {

                    if (orgLongIdData.startsWith(orgLongId)) {
                        includedOrgIds.add(getOwnId(orgLongIdData));
                    }
                }

                //处理门店
                for (TopRankVO.Store store : stores) {

                    String orgLongId1 = store.getOrgLongId();

                    if (orgLongId1.startsWith(orgLongId)) {
                        includedStoreIds.addAll(Arrays.asList(store.getStoreIds().split(",")));
                    }
                }

                orgIncludeOrgStore.setIncludedOrgIds(includedOrgIds);
                orgIncludeOrgStore.setIncludedStoreIds(includedStoreIds);

                orgIncludeOrgStores.add(orgIncludeOrgStore);
            }


            // TODO: 2016/8/26 0026 需要处理的门店


        }

        if (idType == ReportIdType.ORG) {



        }

        if (idType == ReportIdType.STORE) {



        }


        return null;
    }

    //longId !222!2222!
    public String getOwnId(String longId){
        String substring = longId.substring(1, longId.length() - 1);
        String[] split = substring.split("!");
        return split[split.length - 1];
    }

    public static void main(String[] args) {

        //System.out.println(getOwnId("!221!332!55!"));

    }


    /**
     * 构建vo值对象
     * @param data json 数据
     * @return TopRankVO
     */
    public TopRankVO buildVO(String data){

        TopRankVO vo = new TopRankVO();

        JSONObject jsonObject = JSON.parseObject(data);

        String content = jsonObject.getString("data");

        JSONObject contentObj = JSON.parseObject(content);

        int companyId = contentObj.getInteger("company_id");

        int id = contentObj.getInteger("id");

        int idType = contentObj.getInteger("id_type");

        int dateType = contentObj.getInteger("date_type");

        int dataType = contentObj.getInteger("data_type");

        int pageNo = contentObj.getInteger("page");

        int pageSize = contentObj.getInteger("count");

        String dataList = contentObj.getString("data_list");

        String showList = contentObj.getString("show_list");

        vo.setCompanyId(companyId);
        vo.setDataType(dataType);
        vo.setDateType(dateType);
        vo.setId(id);
        vo.setIdType(idType);
        vo.setPageNo(pageNo);
        vo.setPageSize(pageSize);

        //判断idType 是 成员还是其他
        if (Constants.ReportIdType.build(idType) == Constants.ReportIdType.USER) {

            TopRankVO.UserDataList userDataList = new TopRankVO.UserDataList();

            TopRankVO.UserShowList userShowList = new TopRankVO.UserShowList();

            JSONObject dataListObj = JSON.parseObject(dataList);

            JSONArray member_id = dataListObj.getJSONArray("member_id");

            Object[] array = member_id.toArray();

            List<String> list = new ArrayList<>();


            for (int i = 0; i < array.length; i++) {
                list.add(String.valueOf(array[i]));
            }

            userDataList.setUserIds(list);
            userShowList.setUserIds(list);

            vo.setDataList(userDataList);
            vo.setShowList(userShowList);

        } else {

            TopRankVO.OrgStoreDataList orgStoreDataList = new TopRankVO.OrgStoreDataList();

            JSONObject orgDataObj = JSON.parseObject(dataList);

            JSONArray orgLongIdsArray = orgDataObj.getJSONArray("org_long_ids");

            Object[] arrayData = orgLongIdsArray.toArray();

            List<String> orgLongIds = new ArrayList<>();

            for (Object o : arrayData) {
                orgLongIds.add(String.valueOf(o));
            }


            JSONArray storeArray = orgDataObj.getJSONArray("store");

            Object[] objects = storeArray.toArray();

            List<TopRankVO.Store> stores = new ArrayList<>();

            for (Object o : objects) {

                TopRankVO.Store store = new TopRankVO.Store();

                JSONObject jsonObject1 = JSON.parseObject(String.valueOf(o));

                store.setOrgId(jsonObject1.getString("org_id"));
                store.setOrgLongId(jsonObject1.getString("org_long_id"));
                store.setStoreIds(jsonObject1.getString("store_ids"));

                stores.add(store);

            }

            orgStoreDataList.setOrgLongIds(orgLongIds);
            orgStoreDataList.setStores(stores);

            TopRankVO.OrgStoreShowList orgStoreShowList = new TopRankVO.OrgStoreShowList();

            JSONObject orgShowList = JSON.parseObject(showList);

            JSONArray orgId = orgShowList.getJSONArray("org_id");
            JSONArray storeId = orgShowList.getJSONArray("store_id");

            List<String> orgLongIdList = new ArrayList<>();
            List<String> storeIdList = new ArrayList<>();

            for (Object o : orgId) {
                orgLongIdList.add(String.valueOf(o));
            }

            for (Object o : storeId) {
                storeIdList.add(String.valueOf(o));
            }

            orgStoreShowList.setOrgLongId(orgLongIdList);
            orgStoreShowList.setStoreId(storeIdList);

            vo.setDataList(orgStoreDataList);
            vo.setShowList(orgStoreShowList);
        }

        return vo;
    }

}
