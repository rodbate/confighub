package com.iworker.bigdata.dao;


import com.iworker.bigdata.common.HBaseClient;
import com.iworker.bigdata.entity.TargetFee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TargetDaoTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(TargetDaoTest.class);
    @Autowired
    TargetFeeDao dao;



    @Test
    //@Rollback
    //@Transactional
    public void testSave() throws Exception {


        long retId = dao.save(new TargetFee(0.0, 0, 8888, 2, 2, 2016, 10, 0, 100.0, 0, 0, 976, 0));

        LOGGER.info("==========  save ret id is  {}  ==========", retId);
    }

    @Test
    public void testSelect() throws Exception {

        //TargetFee fee = dao.select(new TargetFee(0, 8888, 2, 2, 2016, 8, 2345, 123.0, 0, 2456, 0, 0.0));


        //LOGGER.info(" id is  {} ", fee.getId());

    }

    @Test
    @Rollback
    @Transactional
    public void testBatchSave() throws Exception {

        List<TargetFee> list = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            //list.add(new TargetFee(0, 8888 - i, 2, 2, 2016, 7, 2345, 123.0, 0, 2456, 0, 0.0));
        }

        boolean a = dao.batchSave(list);

        LOGGER.info(" return value is  {} ", a);
    }


    @Test
    public void testUpdate() throws Exception {
        //dao.update(new TargetFee(17377, 8888, 2, 2, 2016, 8, 2345, 12300.0, 0, 2456, 0, 0.0));
    }

    @Test
    public void testBatchUpdate() throws Exception {
        List<TargetFee> list = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            //list.add(new TargetFee(17377 + i, 8888, 2, 2, 2016, 7, 2345, 10000 + i, 0, 2456, 0, 0.0));
        }
       // dao.batchUpdate(list);
    }
}
