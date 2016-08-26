package com.iworker.bigdata.common;


import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;

import static org.apache.hadoop.hbase.util.Bytes.toBytes;

import static com.iworker.bigdata.common.CommonUtil.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HBaseClientTest {


    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseClientTest.class);

    @Autowired
    HBaseClient client;

    @BeforeClass
    public static void init(){
        System.setProperty("hadoop.home.dir", "D:\\hadoop");
    }

    @Test
    public void testScan(){


        Connection connection = client.getConnection();

        try {
            Table table = connection.getTable(TableName.valueOf("erp_dashboard_report"));

            //Get get = new Get(toBytes("1#2#3#5#20160825"));

            Scan scan = new Scan();
            //1#2#3#4#20160825

            Map<String, String> map = getMonAndSunByThisWeek("20160101");

            String timeRegex = buildDateRegex("20160815", "20160820", DateRegexType.WEEK);
            String regex = "^1#.*#.*#.*#" + timeRegex + "$";

            System.out.println(regex + "   ====   ");

            scan.setFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(regex)));



            ResultScanner scanner = table.getScanner(scan);

            Iterator<Result> iterator = scanner.iterator();

            while (iterator.hasNext()) {
                Result rs = iterator.next();

                Cell[] cells = rs.rawCells();

                for (int i = 0; i < cells.length; i++) {

                    String rowStr = Bytes.toString(cells[i].getRowArray());
                    int rowOffset = cells[i].getRowOffset();
                    short rowLength = cells[i].getRowLength();
                    int familyOffset = cells[i].getFamilyOffset();
                    byte familyLength = cells[i].getFamilyLength();
                    int qualifierOffset = cells[i].getQualifierOffset();
                    int qualifierLength = cells[i].getQualifierLength();
                    int valueOffset = cells[i].getValueOffset();
                    int valueLength = cells[i].getValueLength();

                    System.out.println(" row key : " + rowStr.substring(rowOffset, rowLength + rowOffset));
                    System.out.println(" family : " + rowStr.substring(familyOffset, familyLength + familyOffset));
                    System.out.println(" qualifier : " + rowStr.substring(qualifierOffset, qualifierLength + qualifierOffset));
                    //System.out.println(" value : " + rowStr.substring(valueOffset, valueLength + valueOffset));



                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            client.closeConnection(connection);
        }

    }



    @Test
    public void testSave(){



        Put put = new Put(toBytes("11#2#201#2016"));
        put.addColumn(toBytes("data"), toBytes("1"), toBytes("1000000"));

        client.save("t_sale_target", put);
    }
}
