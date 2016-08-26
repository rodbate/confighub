package com.iworker.bigdata.common;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import static com.iworker.bigdata.common.CommonUtil.*;

public class HbaseTest {



    private static final String HBASE_QUORUM = "master.hadoop,slave01.hadoop,slave02.hadoop";

    private static final String HBASE_CLIENT_PORT = "2181";



    public static void main(String[] args) throws IOException {


        /*Configuration conf = HBaseConfiguration.create();

        conf.set("hbase.zookeeper.quorum", HBASE_QUORUM);
        conf.set("hbase.zookeeper.property.clientPort", HBASE_CLIENT_PORT);

        Connection connection = ConnectionFactory.createConnection(conf);


        Table t_sale_target = connection.getTable(TableName.valueOf("erp_dashboard_report"));









        Scan scan = new Scan();

        scan.setFilter(new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("^1#.*#.*#.*#2016082[6-7]")));


        ResultScanner scanner = t_sale_target.getScanner(scan);

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

                System.out.print(" row key : " + rowStr.substring(rowOffset, rowLength + rowOffset) + "     ");
                System.out.print(" family : " + rowStr.substring(familyOffset, familyLength + familyOffset) + "     ");
                System.out.print(" qualifier : " + rowStr.substring(qualifierOffset, qualifierLength + qualifierOffset) + "     ");
                System.out.print(" value : " + rowStr.substring(valueOffset, valueLength + valueOffset) + "     ");

                System.out.println();


            }
        }*/

        buildRegex();

    }


    public static String buildRegex(){


        Map<String, String> map= getMonAndSunByLastWeek(new Date());

        String first = map.get("first");
        String last = map.get("last");

        int y1 = Integer.valueOf(first.substring(0, 4));
        int m1 = Integer.valueOf(first.substring(4, 6));
        int d1 = Integer.valueOf(first.substring(6));

        int y2 = Integer.valueOf(last.substring(0, 4));
        int m2 = Integer.valueOf(last.substring(4, 6));
        int d2 = Integer.valueOf(last.substring(6));


        System.out.println(y1 + " " + m1 + " " + d1);
        System.out.println(y2 + " " + m2 + " " + d2);
        return null;
    }
}
