package com.iworker.bigdata.common;

import javax.annotation.PostConstruct;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
/**
 * 获取hbase和solr的连接
 * @作者 张震文
 * 2016年8月11日-下午4:06:03
 */
public class Util {
	
	private Connection connection;
	@Value("${hbase.zookeeper.quorum}")
	private String zkHosts;
	
	public Util() {
		
	}
	
	@PostConstruct
	public void init() {
		try {
			Configuration config = HBaseConfiguration.create(); 
			config.set("hbase.zookeeper.quorum", zkHosts);
			System.out.println("zkHosts" + zkHosts);
			connection = ConnectionFactory.createConnection(config);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
