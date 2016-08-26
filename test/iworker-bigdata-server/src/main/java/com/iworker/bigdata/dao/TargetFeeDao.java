package com.iworker.bigdata.dao;


import com.iworker.bigdata.entity.TargetFee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.iworker.bigdata.common.Constants.*;
import static com.iworker.bigdata.common.CommonUtil.*;

/**
 *
 * 销售目标或费用  DAO对象
 *
 */


@Repository
public class TargetFeeDao {

    private static final Logger LOG = LoggerFactory.getLogger(TargetFeeDao.class);

    @Autowired
    JdbcTemplate jdbcTemplate;


    public long save(TargetFee targetFee) throws Exception{

        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "insert into " + TABLE_TARGET_DATA + " (company_id,type_id,date_range_type,year,month,user_id,amount,category_id," +
                "org_id,fee_type_id,amount_used, store_id) values (?,?,?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                setParamsForPS(ps, new Object[]{targetFee.getCompanyId(), targetFee.getTypeId(), targetFee.getDateRangeType(),
                        targetFee.getYear(), targetFee.getMonth(), targetFee.getUserId(), targetFee.getAmount(),
                        targetFee.getCategoryId(), targetFee.getOrgId(), targetFee.getFeeTypeId(), targetFee.getAmountUsed(), targetFee.getStoreId()});
                return ps;
            }
        }, keyHolder);


        return keyHolder.getKey().longValue();
    }

    public boolean batchSave(List<TargetFee> list) throws Exception{

        String sql = "insert into " + TABLE_TARGET_DATA + " (company_id,type_id,date_range_type,year,month,user_id,amount,category_id," +
                "org_id,fee_type_id,amount_used,store_id) values (?,?,?,?,?,?,?,?,?,?,?,?)";

        int[] rets = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                TargetFee targetFee = list.get(i);
                setParamsForPS(ps, new Object[]{targetFee.getCompanyId(), targetFee.getTypeId(), targetFee.getDateRangeType(),
                        targetFee.getYear(), targetFee.getMonth(), targetFee.getUserId(), targetFee.getAmount(),
                        targetFee.getCategoryId(), targetFee.getOrgId(), targetFee.getFeeTypeId(), targetFee.getAmountUsed(),targetFee.getStoreId()});
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });

        return rets.length == list.size();

    }

    public int update(TargetFee fee) throws Exception{

        String sql;

        if (fee.getOrgId() != 0) {
            sql = "update " + TABLE_TARGET_DATA + " set amount = ? where year = ? and company_id = ? and org_id = ? and month = ?";
            return jdbcTemplate.update(sql, fee.getAmount(), fee.getYear(), fee.getCompanyId(), fee.getOrgId(), fee.getMonth());
        }

        if (fee.getStoreId() != 0) {
            sql = "update " + TABLE_TARGET_DATA + " set amount = ? where year = ? and company_id = ? and store_id = ? and month = ?";
            return jdbcTemplate.update(sql, fee.getAmount(), fee.getYear(), fee.getCompanyId(), fee.getStoreId(), fee.getMonth());
        }

        if (fee.getUserId() != 0) {
            sql = "update " + TABLE_TARGET_DATA + " set amount = ? where year = ? and company_id = ? and user_id = ? and month = ?";
            return jdbcTemplate.update(sql, fee.getAmount(), fee.getYear(), fee.getCompanyId(), fee.getUserId(), fee.getMonth());
        }
        return 0;
    }

    /**
     * 目标值批量更新操作
     * @param list

     * @return
     * @throws Exception
     */
    public boolean batchUpdate(List<TargetFee> list) throws Exception{

        int retVal = 0;
        for (TargetFee fee : list) {
            retVal += update(fee);
        }

        return retVal == list.size();
    }


    /*public TargetFee select(TargetFee targetFee) throws Exception{

        String sql = "select id,company_id,type_id,date_range_type,year,month,user_id,amount,category_id, org_id,fee_type_id,amount_used" +
                " from " + TABLE_TARGET_DATA + " where company_id = ? and type_id = ? " +
                " and date_range_type = ? and year = ? and month = ? and user_id = ? limit 1";

        Object[] obj = new Object[] {targetFee.getCompanyId(), targetFee.getTypeId(), targetFee.getDateRangeType(),
                targetFee.getYear(), targetFee.getMonth(), targetFee.getUserId()};

        TargetFee fee = new TargetFee();

        jdbcTemplate.query(sql, obj, (rs)-> {

                fee.setId(rs.getLong(1));
                fee.setCompanyId(rs.getLong(2));
                fee.setTypeId(rs.getLong(3));
                fee.setDateRangeType(rs.getInt(4));
                fee.setYear(rs.getInt(5));
                fee.setMonth(rs.getInt(6));
                fee.setUserId(rs.getLong(7));
                fee.setAmount(rs.getDouble(8));
                fee.setCategoryId(rs.getInt(9));
                fee.setOrgId(rs.getLong(10));
                fee.setFeeTypeId(rs.getInt(11));
                fee.setAmountUsed(rs.getDouble(12));
            }
        );


        return fee;
    }*/

}
