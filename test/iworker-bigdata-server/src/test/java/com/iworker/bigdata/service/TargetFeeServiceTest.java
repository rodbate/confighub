package com.iworker.bigdata.service;


import com.iworker.bigdata.vo.TargetFeeVO;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TargetFeeServiceTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(TargetFeeServiceTest.class);

    @Autowired
    TargetFeeService service;

    @BeforeClass
    public static void init(){

        System.setProperty("hadoop.home.dir", "D:\\hadoop");

    }

    @Test
    public void testSave() throws Exception {

        TargetFeeVO vo = new TargetFeeVO();

        TargetFeeVO.Users users = new TargetFeeVO.Users("2222,3333", "111,11", "777,999");

        List<TargetFeeVO.MonthToAmount> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            TargetFeeVO.MonthToAmount ma = new TargetFeeVO.MonthToAmount();
            ma.setMonth(i);
            ma.setAmount(i * 100.0);
            list.add(ma);
        }

        vo.setCompanyId(8888);
        vo.setYear(2016);
        vo.setUsers(users);
        vo.setDataList(list);
        service.add(vo);

    }

    @Test
    public void testUpdate() throws Exception {

        TargetFeeVO vo = new TargetFeeVO();

        TargetFeeVO.Users users = new TargetFeeVO.Users("2222,3333", "111,11", "777,999");

        List<TargetFeeVO.MonthToAmount> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            TargetFeeVO.MonthToAmount ma = new TargetFeeVO.MonthToAmount();
            ma.setMonth(i);
            ma.setAmount(123456);
            list.add(ma);
        }

        vo.setCompanyId(8888);
        vo.setYear(2016);
        vo.setUsers(users);
        vo.setDataList(list);
        service.modify(vo);
    }
}
