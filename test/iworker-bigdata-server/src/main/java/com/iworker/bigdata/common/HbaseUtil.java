package com.iworker.bigdata.common;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 封装一些hbase常用操作
 * @作者 张震文
 * 2016年8月22日-下午5:57:15
 */
@Component
public class HbaseUtil {
	private final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private Util util;
	
	public HTable getTable(String tableName) throws Exception {
		return (HTable)util.getConnection().getTable(TableName.valueOf(Bytes.toBytes(tableName)));
	}
	
	public void closeTable(HTable table) {
		if(table != null) {
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
	}
}
