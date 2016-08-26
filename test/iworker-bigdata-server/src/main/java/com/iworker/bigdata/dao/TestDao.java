package com.iworker.bigdata.dao;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iworker.bigdata.conf.TargetDataSource;
import com.iworker.bigdata.entity.TargetFee;
import com.iworker.bigdata.vo.TargetFeeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedNames;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class TestDao {

    private final static Logger LOG = LoggerFactory.getLogger(TestDao.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void testSelect(){

        Integer count = jdbcTemplate.queryForObject("select count(1) from bs_target_data", Integer.class);

        LOG.info("table bs_target_data count is {}", count);
    }


    @TargetDataSource(name = "global")
    public void testDynamicDataSource(){

        Integer count = jdbcTemplate.queryForObject("select count(1) from bind_keys", Integer.class);

        LOG.info(" =============  table bind_keys count is {}   ==========", count);
    }


    public static void main(String[] args) {


        //System.out.println(convert("testDynamicDataSource"));

        RelaxedNames names = new RelaxedNames("dataSource");

        for (String name : names) {

            System.out.println(name);
        }


    }

    public static String convert(String src){

        final Pattern pattern = Pattern.compile("([^A-Z])([A-Z])");

        StringBuffer sb = new StringBuffer();

        Matcher matcher = pattern.matcher(src);
        System.out.println("=========== begin ===========");
        while (matcher.find()) {

            matcher.appendReplacement(sb, matcher.group(1) + "_" + matcher.group(2).toLowerCase());
            System.out.println("========= " + sb.toString());
        }

        matcher.appendTail(sb);

        return sb.toString();
    }
}
