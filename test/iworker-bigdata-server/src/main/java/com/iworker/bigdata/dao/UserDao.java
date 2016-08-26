package com.iworker.bigdata.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.iworker.bigdata.common.Constants.*;
import static com.iworker.bigdata.common.CommonUtil.*;

/**
 *
 *  用户 dao 对象
 */

@Repository
public class UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Integer> getUserIdByOrg(int orgId) throws Exception{

        List<Integer> list = new ArrayList<>();

        String sql = "select id from " + TABLE_USERS + " where org_id = ?";

        return jdbcTemplate.queryForList(sql, Integer.class, orgId);
    }

    public int getOrgIdById(int id) throws Exception{

        String sql = "select org_id from " + TABLE_USERS + " where id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
    }
}
