package com.iworker.bigdata.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.iworker.bigdata.common.DateUtil;
import com.iworker.bigdata.common.HbaseUtil;
import com.iworker.bigdata.common.IworkerUtils;
import com.iworker.bigdata.domain.SalesCurve;

/**
 * 公司销售曲线服务类
 * @作者 张震文
 * 2016年8月24日-上午9:37:55
 */
@Service
public class SalesCurveReportService {
	
	@Autowired
	HbaseUtil hbaseUtil;
	
	private final Logger logger = Logger.getLogger(getClass());
	public static final String reportTableName = "erp_dashboard_report";
	public static final String targetSaleTableName = "t_sale_target";
	public static final String reportTableFamily = "info";
	public static final String targetTableFamily = "data";
	/**
	 * 本周销售曲线
	 * @param map
	 */
	public String SalesCurveThisWeek(Map map) {
		HTable reportTable = null;
		try {
			String companyId = IworkerUtils.get(map, "companyId");
			//选中的是本周
			//本周周一
			Long firstDayOfWeek = DateUtil.getFirstDayOfWeek();
			//本周周末
			Long lastDayOfWeek = DateUtil.getLastDayOfWeek();
			String start = companyId + "_" + firstDayOfWeek;
			String end = companyId + "_" + lastDayOfWeek;
			//设置过滤条件
			Scan scanReport = new Scan(Bytes.toBytes(start), Bytes.toBytes(end));
			scanReport.setCaching(1000);
			//查询
			reportTable = hbaseUtil.getTable(reportTableName);
			ResultScanner ress = reportTable.getScanner(scanReport);
			
			int x = 1;
			List<SalesCurve> curves = new ArrayList<SalesCurve>();
			for(Result result : ress){
				SalesCurve salesCurve = new SalesCurve();
				//当天的销售额
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				long salesVolume = 0L;
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						salesVolume = Long.parseLong(salesVolumeStr);//获取到当天的销售额
						salesCurve.setSalesVolume(salesVolume);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
				
				//订单数，即成交单数
				Cell orderNumberCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("order_number"));
				String orderNumberStr = new String(CellUtil.cloneValue(orderNumberCell));
				long orderNumber = 0L;
				if(StringUtils.isNotBlank(orderNumberStr)) {
					try {
						orderNumber = Long.parseLong(orderNumberStr);
						salesCurve.setOrderNumber(orderNumber);
					} catch (Exception e) {
						logger.error("转换成交单数时数据出错  orderNumberStr " + orderNumberStr);
					}
				}
				
				//计算客单价=销售额/成交单数
				if(orderNumber != 0) {
					salesCurve.setGuestUnitPrice(Double.valueOf(salesVolume/orderNumber));
				}
				
				curves.add(salesCurve);
			}
			
			return JSON.toJSONString(curves);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		} finally {
			hbaseUtil.closeTable(reportTable);
		}
	}
	
	/**
	 * 上周销售曲线
	 * @param map
	 */
	public String SalesCurveLastWeek(Map map) {
		HTable reportTable = null;
		try {
			String companyId = IworkerUtils.get(map, "companyId");
			//选中的是上周
			//上周周一
			Long firstDayOfWeek = DateUtil.getFirstDayOfLastWeek();
			//上周周末
			Long lastDayOfWeek = DateUtil.getLastDayOfLastWeek();
			String start = companyId + "_" + firstDayOfWeek;
			String end = companyId + "_" + lastDayOfWeek;
			//设置过滤条件
			Scan scanReport = new Scan(Bytes.toBytes(start), Bytes.toBytes(end));
			scanReport.setCaching(1000);
			//查询
			reportTable = hbaseUtil.getTable(reportTableName);
			ResultScanner ress = reportTable.getScanner(scanReport);
			
			int x = 1;
			List<SalesCurve> curves = new ArrayList<SalesCurve>();
			for(Result result : ress){
				SalesCurve salesCurve = new SalesCurve();
				//当天的销售额
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				long salesVolume = 0L;
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						salesVolume = Long.parseLong(salesVolumeStr);//获取到当天的销售额
						salesCurve.setSalesVolume(salesVolume);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
				
				//订单数，即成交单数
				Cell orderNumberCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("order_number"));
				String orderNumberStr = new String(CellUtil.cloneValue(orderNumberCell));
				long orderNumber = 0L;
				if(StringUtils.isNotBlank(orderNumberStr)) {
					try {
						orderNumber = Long.parseLong(orderNumberStr);
						salesCurve.setOrderNumber(orderNumber);
					} catch (Exception e) {
						logger.error("转换成交单数时数据出错  orderNumberStr " + orderNumberStr);
					}
				}
				
				//计算客单价=销售额/成交单数
				if(orderNumber != 0) {
					salesCurve.setGuestUnitPrice(Double.valueOf(salesVolume/orderNumber));
				}
				curves.add(salesCurve);
			}
			
			return JSON.toJSONString(curves);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		} finally {
			hbaseUtil.closeTable(reportTable);
		}
	}
	
	/**
	 * 本月销售曲线
	 * @param map
	 * @return
	 */
	public String SalesCurveThisMonth(Map map) {
		HTable reportTable = null;
		try {
			String companyId = IworkerUtils.get(map, "companyId");
			//选中的是本月
			//月初
			Long firstDayOfMonth = DateUtil.getFirstDayOfMonth();
			//月末
			Long lastDayOfMonth = DateUtil.getLastDayOfMonth();
			String start = companyId + "_" + firstDayOfMonth;
			String end = companyId + "_" + lastDayOfMonth;
			//设置过滤条件
			Scan scanReport = new Scan(Bytes.toBytes(start), Bytes.toBytes(end));
			scanReport.setCaching(1000);
			//查询
			reportTable = hbaseUtil.getTable(reportTableName);
			ResultScanner ress = reportTable.getScanner(scanReport);
			
			int x = 1;
			List<SalesCurve> curves = new ArrayList<SalesCurve>();
			for(Result result : ress){
				SalesCurve salesCurve = new SalesCurve();
				//当天的销售额
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				long salesVolume = 0L;
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						salesVolume = Long.parseLong(salesVolumeStr);//获取到当天的销售额
						salesCurve.setSalesVolume(salesVolume);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
				
				//订单数，即成交单数
				Cell orderNumberCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("order_number"));
				String orderNumberStr = new String(CellUtil.cloneValue(orderNumberCell));
				long orderNumber = 0L;
				if(StringUtils.isNotBlank(orderNumberStr)) {
					try {
						orderNumber = Long.parseLong(orderNumberStr);
						salesCurve.setOrderNumber(orderNumber);
					} catch (Exception e) {
						logger.error("转换成交单数时数据出错  orderNumberStr " + orderNumberStr);
					}
				}
				
				//计算客单价=销售额/成交单数
				if(orderNumber != 0) {
					salesCurve.setGuestUnitPrice(Double.valueOf(salesVolume/orderNumber));
				}
				curves.add(salesCurve);
			}
			
			return JSON.toJSONString(curves);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		} finally {
			hbaseUtil.closeTable(reportTable);
		}
	}
	
	/**
	 * 上月销售曲线
	 * @param map
	 * @return
	 */
	public String SalesCurveLastMonth(Map map) {
		HTable reportTable = null;
		try {
			String companyId = IworkerUtils.get(map, "companyId");
			//选中的是上月
			//上月月初
			Long firstDayOfMonth = DateUtil.getFirstDayOfLastMonth();
			//上月月末
			Long lastDayOfMonth = DateUtil.getLastDayOfLastMonth();
			String start = companyId + "_" + firstDayOfMonth;
			String end = companyId + "_" + lastDayOfMonth;
			//设置过滤条件
			Scan scanReport = new Scan(Bytes.toBytes(start), Bytes.toBytes(end));
			scanReport.setCaching(1000);
			//查询
			reportTable = hbaseUtil.getTable(reportTableName);
			ResultScanner ress = reportTable.getScanner(scanReport);
			
			int x = 1;
			List<SalesCurve> curves = new ArrayList<SalesCurve>();
			for(Result result : ress){
				SalesCurve salesCurve = new SalesCurve();
				//当天的销售额
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				long salesVolume = 0L;
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						salesVolume = Long.parseLong(salesVolumeStr);//获取到当天的销售额
						salesCurve.setSalesVolume(salesVolume);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
				
				//订单数，即成交单数
				Cell orderNumberCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("order_number"));
				String orderNumberStr = new String(CellUtil.cloneValue(orderNumberCell));
				long orderNumber = 0L;
				if(StringUtils.isNotBlank(orderNumberStr)) {
					try {
						orderNumber = Long.parseLong(orderNumberStr);
						salesCurve.setOrderNumber(orderNumber);
					} catch (Exception e) {
						logger.error("转换成交单数时数据出错  orderNumberStr " + orderNumberStr);
					}
				}
				
				//计算客单价=销售额/成交单数
				if(orderNumber != 0) {
					salesCurve.setGuestUnitPrice(Double.valueOf(salesVolume/orderNumber));
				}
				curves.add(salesCurve);
			}
			
			return JSON.toJSONString(curves);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		} finally {
			hbaseUtil.closeTable(reportTable);
		}
	}
	
	/**
	 * 本年销售曲线
	 * @param map
	 * @return
	 */
	public String SalesCurveThisYear(Map map) {
		HTable reportTable = null;
		try {
			String companyId = IworkerUtils.get(map, "companyId");
			reportTable = hbaseUtil.getTable(reportTableName);
			//分别统计年初到本月
			int currentMonth = Integer.parseInt(DateUtil.getMonth());
			List<SalesCurve> curves = new ArrayList<SalesCurve>();
			for(int month=1; month<=currentMonth; month++) {
				//对应月月初
				Long monthStart = DateUtil.getDynamicFirstDayWithMonth(month);
				//对应月月末
				Long monthEnd = DateUtil.getDynamicLastDayWithMonth(month);
				String start = companyId + "_" + monthStart;
				String end = companyId + "_" + monthEnd;
				//设置过滤条件
				Scan scanReport = new Scan(Bytes.toBytes(start), Bytes.toBytes(end));
				scanReport.setCaching(1000);
				//查询
				ResultScanner ress = reportTable.getScanner(scanReport);
				//将一个月的数据相加起来整合成一条数据
				SalesCurve salesCurve = new SalesCurve();
				salesCurve.setX(month);//设置月份
				long monthSalesVolume = 0L;
				long monthOrderNumber = 0L;
				for(Result result : ress){
					//当天的销售额
					Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("sales_volume"));
					String salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
					if(StringUtils.isNotBlank(salesVolumeStr)) {
						try {
							monthSalesVolume += Long.parseLong(salesVolumeStr);//获取到当天的销售额
						} catch (Exception e) {
							logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
						}
					}
					
					//订单数，即成交单数
					Cell orderNumberCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("order_number"));
					String orderNumberStr = new String(CellUtil.cloneValue(orderNumberCell));
					if(StringUtils.isNotBlank(orderNumberStr)) {
						try {
							monthOrderNumber += Long.parseLong(orderNumberStr);
						} catch (Exception e) {
							logger.error("转换成交单数时数据出错  orderNumberStr " + orderNumberStr);
						}
					}
					
				}
				
				//设置月销售额
				salesCurve.setSalesVolume(monthSalesVolume);
				//设置月订单数
				salesCurve.setOrderNumber(monthOrderNumber);
				//计算客单价=销售额/成交单数
				if(monthOrderNumber != 0) {
					salesCurve.setGuestUnitPrice(Double.valueOf(monthSalesVolume/monthOrderNumber));
				}
				curves.add(salesCurve);
			}
			
			return JSON.toJSONString(curves);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		} finally {
			hbaseUtil.closeTable(reportTable);
		}
	}
	
}
