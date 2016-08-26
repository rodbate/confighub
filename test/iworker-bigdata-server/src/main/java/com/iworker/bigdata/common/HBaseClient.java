package com.iworker.bigdata.common;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.hadoop.hbase.util.Bytes.*;

@Component
public class HBaseClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseClient.class);

    @Value("${hbase.zookeeper.quorum}")
    private String hbaseQuorum;

    @Value("${hbase.zookeeper.property.clientPort}")
    private String hbaseZookeeperClientPort;

    private Configuration conf;


    @PostConstruct
    public void init(){
        conf = HBaseConfiguration.create();

        conf.set("hbase.zookeeper.quorum", hbaseQuorum);

        conf.set("hbase.zookeeper.property.clientPort", hbaseZookeeperClientPort);
    }

    public Connection getConnection(){

        Connection conn = null;

        try {
            conn = ConnectionFactory.createConnection(conf);

        } catch (IOException e) {
            LOGGER.info(e.getMessage(), e);
        }

        return conn;
    }

    public void closeConnection(Connection conn){

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (IOException e) {
            LOGGER.info(e.getMessage(), e);
        }
    }



    public void save(String tableName, List<Put> puts){

        Connection conn = getConnection();

        Table table = null;
        try {
            table = conn.getTable(TableName.valueOf(tableName));
            table.put(puts);
        } catch (IOException e) {
            LOGGER.info(e.getMessage(), e);
        } finally {
            if (table != null){
                try {
                    table.close();
                } catch (IOException e) {
                    LOGGER.info(e.getMessage(), e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    LOGGER.info(e.getMessage(), e);
                }
            }
        }

    }

    public void save(String tableName, Put put){
        List<Put> list = new ArrayList<>();
        list.add(put);
        save(tableName, list);
    }


    public void getOneRowByTableNameAndRowKey(String tableName, String rowKey){

        Connection connection = getConnection();

        try {
            Table table = connection.getTable(TableName.valueOf(tableName));
            Get get = new Get(toBytes(rowKey));
            Result result = table.get(get);
            result.getRow();
        } catch (IOException e) {
            LOGGER.info(e.getMessage(), e);
        } finally {
            if (connection != null) {
                closeConnection(connection);
            }
        }

    }


}
