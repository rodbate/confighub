package com.iworker.bigdata;

import com.iworker.bigdata.dao.TestDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IworkerBigdataServerApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	TestDao dao;

	@Test
	public void testJdbc(){

		dao.testSelect();

	}

	@Test
	public void testDynamicSource(){

		dao.testDynamicDataSource();
	}

}
