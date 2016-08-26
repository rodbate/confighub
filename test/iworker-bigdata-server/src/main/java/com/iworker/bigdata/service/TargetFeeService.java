package com.iworker.bigdata.service;


import com.iworker.bigdata.common.HBaseClient;
import com.iworker.bigdata.dao.TargetFeeDao;
import com.iworker.bigdata.entity.TargetFee;
import com.iworker.bigdata.vo.TargetFeeVO;
import org.apache.hadoop.hbase.client.Put;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.apache.hadoop.hbase.util.Bytes.*;
import static com.iworker.bigdata.common.Constants.*;

/**
 *
 *
 * 销售目标或费用  Service对象
 */


@Service
public class TargetFeeService {


    @Autowired
    TargetFeeDao targetDao;

    @Autowired
    HBaseClient client;


    @Transactional
    public boolean add(TargetFeeVO vo) throws Exception {

        List<TargetFee> feeList = parseData(vo);

        client.save(HBASE_SALE_TARGET, buildPuts(feeList));

        return targetDao.batchSave(feeList);

    }

    @Transactional
    public boolean modify(TargetFeeVO vo) throws Exception {

        List<TargetFee> list = parseData(vo);

        client.save(HBASE_SALE_TARGET, buildPuts(list));

        return targetDao.batchUpdate(list);

    }


    public List<Put> buildPuts(List<TargetFee> fees) {
        List<Put> list = new ArrayList<>();

        for (TargetFee fee : fees){

            String rowKey = String.valueOf(fee.getYear()) + HBASE_ROWKEY_SEPARATOR + String.valueOf(fee.getCompanyId()) +
                    HBASE_ROWKEY_SEPARATOR + String.valueOf(fee.getOrgId()) + HBASE_ROWKEY_SEPARATOR + String.valueOf(fee.getStoreId()) +
                    HBASE_ROWKEY_SEPARATOR + String.valueOf(fee.getUserId());

            Put put = new Put(toBytes(rowKey));

            for (int i = 1; i <= 12 ; i++) {

                if (fee.getMonth() == i){
                    put.addColumn(toBytes("data"), toBytes(String.valueOf(i)), toBytes(String.valueOf(fee.getAmount())));
                } else {
                    put.addColumn(toBytes("data"), toBytes(String.valueOf(i)), toBytes("0"));
                }
            }

            list.add(put);
        }

        return list;
    }



    public List<TargetFee> parseData(TargetFeeVO vo) throws Exception {

        //1 解析user id

        Map<Integer, Integer> userOrg = new HashMap<>();

        TargetFeeVO.Users users = vo.getUsers();

        String[] userIds = users.getUser().split(",");

        String[] orgIds = users.getOrg().split(",");

        String[] storeIds = users.getStore().split(",");


        //2 构造实体对象
        List<TargetFeeVO.MonthToAmount> dataList = vo.getDataList();

        List<TargetFee> feeList = new ArrayList<>();

        for (int i = 0; i < userIds.length; i++) {

            for (int j = 0; j < dataList.size(); j++) {
                TargetFeeVO.MonthToAmount monthToAmount = dataList.get(i);

                TargetFee fee = new TargetFee();

                fee.setCompanyId(vo.getCompanyId());
                fee.setOrgId(0);
                fee.setAmountUsed(0.0);
                fee.setAmount(monthToAmount.getAmount());
                fee.setFeeTypeId(0);
                fee.setCategoryId(0);
                fee.setStoreId(0);
                fee.setDateRangeType(2);
                fee.setMonth(monthToAmount.getMonth());
                fee.setTypeId(3);
                fee.setYear(vo.getYear());
                fee.setUserId(Integer.valueOf(userIds[i]));

                feeList.add(fee);
            }
        }

        for (int i = 0; i < orgIds.length; i++) {

            for (int j = 0; j < dataList.size(); j++) {
                TargetFeeVO.MonthToAmount monthToAmount = dataList.get(i);

                TargetFee fee = new TargetFee();

                fee.setCompanyId(vo.getCompanyId());
                fee.setOrgId(Integer.valueOf(orgIds[i]));
                fee.setAmountUsed(0.0);
                fee.setAmount(monthToAmount.getAmount());
                fee.setFeeTypeId(0);
                fee.setCategoryId(0);
                fee.setStoreId(0);
                fee.setDateRangeType(2);
                fee.setMonth(monthToAmount.getMonth());
                fee.setTypeId(3);
                fee.setYear(vo.getYear());
                fee.setUserId(0);

                feeList.add(fee);
            }
        }

        for (int i = 0; i < storeIds.length; i++) {

            for (int j = 0; j < dataList.size(); j++) {
                TargetFeeVO.MonthToAmount monthToAmount = dataList.get(i);

                TargetFee fee = new TargetFee();

                fee.setCompanyId(vo.getCompanyId());
                fee.setOrgId(0);
                fee.setAmountUsed(0.0);
                fee.setAmount(monthToAmount.getAmount());
                fee.setFeeTypeId(0);
                fee.setCategoryId(0);
                fee.setStoreId(Integer.valueOf(storeIds[i]));
                fee.setDateRangeType(2);
                fee.setMonth(monthToAmount.getMonth());
                fee.setTypeId(3);
                fee.setYear(vo.getYear());
                fee.setUserId(0);

                feeList.add(fee);
            }
        }


        return feeList;

    }
}
