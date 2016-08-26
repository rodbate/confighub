package com.iworker.bigdata.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.iworker.bigdata.conf.TargetDataSource;

public class TestDbService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//默认数据源
	public List<?> getList(){
		return jdbcTemplate.queryForList("");
	}
	
	//其他数据源
	@TargetDataSource(name="global")
	public List<?> getOtherList(){
		return jdbcTemplate.queryForList("");
	}
} 
