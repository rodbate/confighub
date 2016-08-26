/**
 * 
 */
package com.iworker.bigdata.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.hadoop.hive.HiveTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

@RestController 
@RequestMapping(value="/report/test") 
public class ReportController {
	private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private DiscoveryClient client;
//	@Autowired
//	private HiveTemplate hiveTemplate;
	
	private static String driverName = 
            "org.apache.hive.jdbc.HiveDriver";
	
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public String getReport(@RequestBody String data) {
		ServiceInstance instance = client.getLocalServiceInstance();
		logger.info("/add, host:" + instance.getHost() + ", service_id:" + instance.getServiceId() + ", result:" + data);
		
//		hiveTemplate
		
		try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
		
		try {
			
			Connection con = DriverManager.getConnection(
					"jdbc:hive2://192.168.1.208:10000/default", "root", "123.com");
			Statement stmt = con.createStatement();
			String tableName = "wyphao";
			stmt.execute("drop table if exists " + tableName);
			stmt.execute("create table " + tableName + 
					" (key int, value string)");
			System.out.println("Create table success!");
			// show tables
			String sql = "show tables '" + tableName + "'";
			System.out.println("Running: " + sql);
			ResultSet res = stmt.executeQuery(sql);
			if (res.next()) {
				System.out.println(res.getString(1));
			}
			
			// describe table
			sql = "describe " + tableName;
			System.out.println("Running: " + sql);
			res = stmt.executeQuery(sql);
			while (res.next()) {
				System.out.println(res.getString(1) + "\t" + res.getString(2));
			}
			
			
			sql = "select * from " + tableName;
			res = stmt.executeQuery(sql);
			while (res.next()) {
				System.out.println(String.valueOf(res.getInt(1)) + "\t"
						+ res.getString(2));
			}
			
			sql = "select count(1) from " + tableName;
			System.out.println("Running: " + sql);
			res = stmt.executeQuery(sql);
			while (res.next()) {
				System.out.println(res.getString(1));
			}
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
        
		
		return JSON.toJSONString(data);
	}

}
