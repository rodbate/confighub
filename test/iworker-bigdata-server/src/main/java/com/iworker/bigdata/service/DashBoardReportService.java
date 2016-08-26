package com.iworker.bigdata.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.iworker.bigdata.common.DateUtil;
import com.iworker.bigdata.common.HbaseUtil;
import com.iworker.bigdata.common.IworkerUtils;
import com.iworker.bigdata.domain.DashBoard;

/**
 * 公司报表 仪表盘，区域销售情况，销售曲线，需要关注service
 * @作者 张震文
 * 2016年8月24日-上午9:37:55
 */
@Service
public class DashBoardReportService {
	
	@Autowired
	HbaseUtil hbaseUtil;
	
	private final Logger logger = Logger.getLogger(getClass());
	public static final String reportTableName = "erp_dashboard_report";
	public static final String targetSaleTableName = "t_sale_target";
	public static final String reportTableFamily = "info";
	public static final String targetTableFamily = "data";
	/**
	 * 公司仪表盘
	 * 报表数据组织
	 * @param map
	 */
	public String CompanyDashBoardThisWeek(Map map) {
		HTable reportTable = null;
		HTable targetSaleTable = null;
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
			//最后返回结果封装到Report里面
			DashBoard report = new DashBoard();
			//销售总额，即实际销售额
			Long real_sales = 0L;
			
			for(Result result : ress){
				//当天的销售额
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						//累加到实际销售额上
						real_sales += Long.parseLong(salesVolumeStr);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
			}
			
			//实际销售额
			report.setReal_sales(real_sales);
			
			//目标销售额
			targetSaleTable = hbaseUtil.getTable(targetSaleTableName);
			Scan targetScan = new Scan();
			String prefix = DateUtil.getYear() + "#" + companyId + "#";
			Filter targetFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryPrefixComparator(Bytes.toBytes(prefix)));
			targetScan.setFilter(targetFilter);
			ResultScanner targetScanner = targetSaleTable.getScanner(targetScan);
			
			//全公司按周查询的目标销售额
			Long targetSales = 0L;
			for(Result result :targetScanner) {
				String month = DateUtil.getWeekInMonth(0);//获取周一所在的月为目标月
				//获取本月所有店员的目标销售额，将店员目标销售额相加除以4，得到公司一周的目标销售额
				Cell targetVolumeCell = result.getColumnLatestCell(Bytes.toBytes(targetTableFamily), Bytes.toBytes(month));
				String targetVolumeStr = "";
				if(targetVolumeCell != null) {
					targetVolumeStr = new String(CellUtil.cloneValue(targetVolumeCell));
				}
				if(StringUtils.isNotBlank(targetVolumeStr)) {
					try {
						targetSales += Long.parseLong(targetVolumeStr);
					} catch (Exception e) {
						logger.error("统计目标销售额数据出错  targetVolumeStr " + targetVolumeStr);
					}
				}
			}
			
			if(targetSales.longValue() != 0) {
				//设置目标销售额
				report.setTarget_sales(targetSales/4);
				//设置完成率
				report.setCompletion_rate(Double.valueOf(report.getReal_sales()/report.getTarget_sales()));
			}
			
			//计算环比，本周比上周的实际销售额
			Long firstDayOfLastWeek = DateUtil.getFirstDayOfLastWeek();//上周周一
			Long lastDayOfLastWeek2 = DateUtil.getSameWeekDay(-1);//上周同期
			String lastStart = companyId + "_" + firstDayOfLastWeek;
			String lastEnd = companyId + "_" + lastDayOfLastWeek2;
			//设置过滤条件
			Scan lastScanReport = new Scan(Bytes.toBytes(lastStart), Bytes.toBytes(lastEnd));
			ResultScanner lastRess = reportTable.getScanner(lastScanReport);
			
			//销售总额，即实际销售额
			Long last_real_sales = 0L;
			for(Result result : lastRess){
				//当天的销售额
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = "";
				if(salesVolumeCell != null) {
					salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				}
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						//累加到上周实际销售额上
						last_real_sales += Long.parseLong(salesVolumeStr);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
			}
			if(last_real_sales.longValue() != 0) {
				report.setMom(Double.valueOf(real_sales/last_real_sales));
			}
			
			return JSON.toJSONString(report);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		} finally {
			hbaseUtil.closeTable(reportTable);
			hbaseUtil.closeTable(targetSaleTable);
		}
	}
	
	/**
	 * 公司仪表盘上周数据
	 * @param map
	 * @return
	 */
	public String CompanyDashBoardLastWeek(Map map) {
		String companyId = IworkerUtils.get(map, "companyId");
		HTable reportTable = null;
		HTable targetSaleTable = null;
		try {
			//如果选中的是上周
			//上周周一
			Long firstDayOfLastWeek = DateUtil.getFirstDayOfLastWeek();
			//上周周末
			Long lastDayOfLastWeek = DateUtil.getLastDayOfLastWeek();
			
			String start = companyId + "_" + firstDayOfLastWeek;
			String end = companyId + "_" + lastDayOfLastWeek;
			//设置过滤条件
			Scan scanReport = new Scan(Bytes.toBytes(start), Bytes.toBytes(end));
			scanReport.setCaching(1000);
			//查询
			reportTable = hbaseUtil.getTable(reportTableName);
			ResultScanner ress = reportTable.getScanner(scanReport);
			//最后返回结果封装到Report里面
			DashBoard report = new DashBoard();
			//销售总额，即实际销售额
			Long real_sales = 0L;
			
			for(Result result : ress){
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						//累加到实际销售额上
						real_sales += Long.parseLong(salesVolumeStr);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
			}
			
			//实际销售额
			report.setReal_sales(real_sales);
			
			//目标销售额
			targetSaleTable = hbaseUtil.getTable(targetSaleTableName);
			Scan targetScan = new Scan();
			String prefix = DateUtil.getYear() + "#" + companyId + "#";
			Filter targetFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryPrefixComparator(Bytes.toBytes(prefix)));
			targetScan.setFilter(targetFilter);
			
			ResultScanner targetScanner = targetSaleTable.getScanner(targetScan);
			
			//全公司按周查询的目标销售额
			Long targetSales = 0L;
			for(Result result :targetScanner) {
				String month = DateUtil.getWeekInMonth(0);//获取周一所在的月为目标月
				//获取本月所有店员的目标销售额，将店员目标销售额相加除以4，得到公司一周的目标销售额
				Cell targetVolumeCell = result.getColumnLatestCell(Bytes.toBytes(targetTableFamily), Bytes.toBytes(month));
				String targetVolumeStr = "";
				if(targetVolumeCell != null) {
					targetVolumeStr = new String(CellUtil.cloneValue(targetVolumeCell));
				}
				if(StringUtils.isNotBlank(targetVolumeStr)) {
					try {
						targetSales += Long.parseLong(targetVolumeStr);
					} catch (Exception e) {
						logger.error("统计目标销售额数据出错  targetVolumeStr " + targetVolumeStr);
					}
				}
			}
			
			if(targetSales.longValue() != 0) {
				//设置目标销售额
				report.setTarget_sales(targetSales/4);
				//设置完成率
				report.setCompletion_rate(Double.valueOf(report.getReal_sales()/report.getTarget_sales()));
			}
			//计算环比，上周比上上周的实际销售额
			Long firstDayOfLastLastWeek = DateUtil.getDynamicFirstDayOfLastWeek(-2);//上上周周一
			Long lastDayOfLastLastWeek = DateUtil.getDynamicLastDayOfWeek(-1);//上上周周末
			String lastStart = companyId + "_" + firstDayOfLastLastWeek;
			String lastEnd = companyId + "_" + lastDayOfLastLastWeek;
			//设置过滤条件
			Scan lastScanReport = new Scan(Bytes.toBytes(lastStart), Bytes.toBytes(lastEnd));
			ResultScanner lastRess = reportTable.getScanner(lastScanReport);
			
			//销售总额，即实际销售额
			Long last_real_sales = 0L;
			for(Result result : lastRess){
				//当天的销售额
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = "";
				if(salesVolumeCell != null) {
					salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				}
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						//累加到上周实际销售额上
						last_real_sales += Long.parseLong(salesVolumeStr);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
			}
			if(last_real_sales.longValue() != 0) {
				report.setMom(Double.valueOf(real_sales/last_real_sales));
			}
			return JSON.toJSONString(report);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		} finally {
			hbaseUtil.closeTable(reportTable);
			hbaseUtil.closeTable(targetSaleTable);
		}
	}

	/**
	 * 公司仪表盘本月报表
	 * @param map
	 * @return
	 */
	public String CompanyDashBoardThisMonth(Map map) {
		String companyId = IworkerUtils.get(map, "companyId");
		HTable reportTable = null;
		HTable targetSaleTable = null;
		try {
			Long firstDayOfMonth = DateUtil.getFirstDayOfMonth();//本月月初
			Long lastDayOfMonth = DateUtil.getLastDayOfMonth();//本月月末
			String start = companyId + "_" + firstDayOfMonth;
			String end = companyId + "_" + lastDayOfMonth;
			
			//设置过滤条件
			Scan scanReport = new Scan(Bytes.toBytes(start), Bytes.toBytes(end));
			scanReport.setCaching(1000);
			//查询
			reportTable = hbaseUtil.getTable(reportTableName);
			ResultScanner ress = reportTable.getScanner(scanReport);
			//最后返回结果封装到Report里面
			DashBoard report = new DashBoard();
			//销售总额，即实际销售额
			Long real_sales = 0L;
			
			for(Result result : ress){
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						//累加到实际销售额上
						real_sales += Long.parseLong(salesVolumeStr);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
			}
			
			//实际销售额
			report.setReal_sales(real_sales);
			
			//目标销售额
			targetSaleTable = hbaseUtil.getTable(targetSaleTableName);
			Scan targetScan = new Scan();
			String prefix = DateUtil.getYear() + "#" + companyId + "#";
			Filter targetFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryPrefixComparator(Bytes.toBytes(prefix)));
			targetScan.setFilter(targetFilter);
			
			ResultScanner targetScanner = targetSaleTable.getScanner(targetScan);
			//全公司按月查询的目标销售额
			Long targetSales = 0L;
			for(Result result :targetScanner) {
				String month = DateUtil.getMonth();//获取当前月
				//获取本月所有店员的目标销售额
				Cell targetVolumeCell = result.getColumnLatestCell(Bytes.toBytes(targetTableFamily), Bytes.toBytes(month));
				String targetVolumeStr = "";
				if(targetVolumeCell != null) {
					targetVolumeStr = new String(CellUtil.cloneValue(targetVolumeCell));
				}
				if(StringUtils.isNotBlank(targetVolumeStr)) {
					try {
						targetSales += Long.parseLong(targetVolumeStr);
					} catch (Exception e) {
						logger.error("统计目标销售额数据出错  targetVolumeStr " + targetVolumeStr);
					}
				}
			}
			
			if(targetSales.longValue() != 0) {
				//设置目标销售额
				report.setTarget_sales(targetSales);
				//设置完成率
				report.setCompletion_rate(Double.valueOf(report.getReal_sales()/report.getTarget_sales()));
			}
			
			//计算环比，本月比上月的实际销售额
			Long firstDayOfLastMonth = DateUtil.getFirstDayOfLastMonth();//上月月初
			Long lastDayOfLastMonth = DateUtil.getSameMonthDay(-1);//上月同期
			
			String lastStart = companyId + "_" + firstDayOfLastMonth;
			String lastEnd = companyId + "_" + lastDayOfLastMonth;
			//设置过滤条件
			Scan lastScanReport = new Scan(Bytes.toBytes(lastStart), Bytes.toBytes(lastEnd));
			ResultScanner lastRess = reportTable.getScanner(lastScanReport);
			
			//销售总额，即实际销售额
			Long last_real_sales = 0L;
			for(Result result : lastRess){
				//当天的销售额
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = "";
				if(salesVolumeCell != null) {
					salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				}
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						//累加到上周实际销售额上
						last_real_sales += Long.parseLong(salesVolumeStr);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
			}
			if(last_real_sales.longValue() != 0) {
				report.setMom(Double.valueOf(real_sales/last_real_sales));
			}
			return JSON.toJSONString(report);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		} finally {
			hbaseUtil.closeTable(reportTable);
			hbaseUtil.closeTable(targetSaleTable);
		}
	}

	/**
	 * 公司仪表盘上月报表
	 * @param map
	 * @return
	 */
	public String CompanyDashBoardLastMonth(Map map) {
		String companyId = IworkerUtils.get(map, "companyId");
		HTable reportTable = null;
		HTable targetSaleTable = null;
		try {
			Long firstDayOfMonth = DateUtil.getFirstDayOfLastMonth();//上月月初
			Long lastDayOfMonth = DateUtil.getLastDayOfLastMonth();//上月月末
			String start = companyId + "_" + firstDayOfMonth;
			String end = companyId + "_" + lastDayOfMonth;
			
			//设置过滤条件
			Scan scanReport = new Scan(Bytes.toBytes(start), Bytes.toBytes(end));
			scanReport.setCaching(1000);
			//查询
			reportTable = hbaseUtil.getTable(reportTableName);
			ResultScanner ress = reportTable.getScanner(scanReport);
			//最后返回结果封装到Report里面
			DashBoard report = new DashBoard();
			//销售总额，即实际销售额
			Long real_sales = 0L;
			
			for(Result result : ress){
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						//累加到实际销售额上
						real_sales += Long.parseLong(salesVolumeStr);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
			}
			
			//实际销售额
			report.setReal_sales(real_sales);
			
			//目标销售额
			targetSaleTable = hbaseUtil.getTable(targetSaleTableName);
			Scan targetScan = new Scan();
			String prefix = DateUtil.getYear() + "#" + companyId + "#";
			Filter targetFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryPrefixComparator(Bytes.toBytes(prefix)));
			targetScan.setFilter(targetFilter);
			
			ResultScanner targetScanner = targetSaleTable.getScanner(targetScan);
			//全公司按月查询的目标销售额
			Long targetSales = 0L;
			for(Result result :targetScanner) {
				String month = DateUtil.getMonth(-2);//获取上上月
				//获取本月所有店员的目标销售额
				Cell targetVolumeCell = result.getColumnLatestCell(Bytes.toBytes(targetTableFamily), Bytes.toBytes(month));
				String targetVolumeStr = "";
				if(targetVolumeCell != null) {
					targetVolumeStr = new String(CellUtil.cloneValue(targetVolumeCell));
				}
				if(StringUtils.isNotBlank(targetVolumeStr)) {
					try {
						targetSales += Long.parseLong(targetVolumeStr);
					} catch (Exception e) {
						logger.error("统计目标销售额数据出错  targetVolumeStr " + targetVolumeStr);
					}
				}
			}
			
			if(targetSales.longValue() != 0) {
				//设置目标销售额
				report.setTarget_sales(targetSales);
				//设置完成率
				report.setCompletion_rate(Double.valueOf(report.getReal_sales()/report.getTarget_sales()));
			}
			
			//计算环比，上月比上上月的实际销售额
			Long firstDayOfLastMonth = DateUtil.getDynamicFirstDayOfMonth(-2);//上上月月初
			Long lastDayOfLastMonth = DateUtil.getDynamicLastDayOfMonth(-2);//上上月月末
			
			String lastStart = companyId + "_" + firstDayOfLastMonth;
			String lastEnd = companyId + "_" + lastDayOfLastMonth;
			//设置过滤条件
			Scan lastScanReport = new Scan(Bytes.toBytes(lastStart), Bytes.toBytes(lastEnd));
			ResultScanner lastRess = reportTable.getScanner(lastScanReport);
			
			//销售总额，即实际销售额
			Long last_real_sales = 0L;
			for(Result result : lastRess){
				//当天的销售额
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = "";
				if(salesVolumeCell != null) {
					salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				}
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						//累加到上周实际销售额上
						last_real_sales += Long.parseLong(salesVolumeStr);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
			}
			if(last_real_sales.longValue() != 0) {
				report.setMom(Double.valueOf(real_sales/last_real_sales));
			}
			return JSON.toJSONString(report);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		} finally {
			hbaseUtil.closeTable(reportTable);
			hbaseUtil.closeTable(targetSaleTable);
		}
	}

	/**
	 * 公司仪表盘本年报表
	 * @param map
	 * @return
	 */
	public String CompanyDashBoardThisYear(Map map) {
		String companyId = IworkerUtils.get(map, "companyId");
		HTable reportTable = null;
		HTable targetSaleTable = null;
		try {
			Long startOfYear = DateUtil.getStartOfYear(0);//今年年初
			Long endOfYear = DateUtil.getEndOfYear(0);//今年年末
			String start = companyId + "_" + startOfYear;
			String end = companyId + "_" + endOfYear;
			
			//设置过滤条件
			Scan scanReport = new Scan(Bytes.toBytes(start), Bytes.toBytes(end));
			scanReport.setCaching(1000);
			//查询
			reportTable = hbaseUtil.getTable(reportTableName);
			ResultScanner ress = reportTable.getScanner(scanReport);
			//最后返回结果封装到Report里面
			DashBoard report = new DashBoard();
			//销售总额，即实际销售额
			Long real_sales = 0L;
			
			for(Result result : ress){
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						//累加到实际销售额上
						real_sales += Long.parseLong(salesVolumeStr);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
			}
			
			//实际销售额
			report.setReal_sales(real_sales);
			
			//目标销售额
			targetSaleTable = hbaseUtil.getTable(targetSaleTableName);
			Scan targetScan = new Scan();
			String prefix = DateUtil.getYear() + "#" + companyId + "#";
			Filter targetFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryPrefixComparator(Bytes.toBytes(prefix)));
			targetScan.setFilter(targetFilter);
			
			ResultScanner targetScanner = targetSaleTable.getScanner(targetScan);
			//全公司按月查询的目标销售额
			Long targetSales = 0L;
			for(Result result :targetScanner) {
				//String month = DateUtil.getMonth(-2);//获取上上月
				int currentMonth = Integer.parseInt(DateUtil.getMonth());
				
				for(int month=1; month<=currentMonth; month++) {
					//获取本月所有店员的目标销售额
					Cell targetVolumeCell = result.getColumnLatestCell(Bytes.toBytes(targetTableFamily), Bytes.toBytes(month));
					String targetVolumeStr = "";
					if(targetVolumeCell != null) {
						targetVolumeStr = new String(CellUtil.cloneValue(targetVolumeCell));
					}
					if(StringUtils.isNotBlank(targetVolumeStr)) {
						try {
							targetSales += Long.parseLong(targetVolumeStr);
						} catch (Exception e) {
							logger.error("统计目标销售额数据出错  targetVolumeStr " + targetVolumeStr);
						}
					}
				}
			}
			
			if(targetSales.longValue() != 0) {
				//设置目标销售额
				report.setTarget_sales(targetSales);
				//设置完成率
				report.setCompletion_rate(Double.valueOf(report.getReal_sales()/report.getTarget_sales()));
			}
			
			//计算环比，几年比去年的实际销售额
			Long firstDayOfLastMonth = DateUtil.getStartOfYear(-1);//去年年初
			Long lastDayOfLastMonth = DateUtil.getSameYearDay(-1);//去年同期
			
			String lastStart = companyId + "_" + firstDayOfLastMonth;
			String lastEnd = companyId + "_" + lastDayOfLastMonth;
			//设置过滤条件
			Scan lastScanReport = new Scan(Bytes.toBytes(lastStart), Bytes.toBytes(lastEnd));
			ResultScanner lastRess = reportTable.getScanner(lastScanReport);
			
			//销售总额，即实际销售额
			Long last_real_sales = 0L;
			for(Result result : lastRess){
				//当天的销售额
				Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("sales_volume"));
				String salesVolumeStr = "";
				if(salesVolumeCell != null) {
					salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
				}
				if(StringUtils.isNotBlank(salesVolumeStr)) {
					try {
						//累加到上周实际销售额上
						last_real_sales += Long.parseLong(salesVolumeStr);
					} catch (Exception e) {
						logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
					}
				}
			}
			if(last_real_sales.longValue() != 0) {
				report.setMom(Double.valueOf(real_sales/last_real_sales));
			}
			return JSON.toJSONString(report);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		} finally {
			hbaseUtil.closeTable(reportTable);
			hbaseUtil.closeTable(targetSaleTable);
		}
	}
}
