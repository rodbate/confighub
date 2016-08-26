package com.iworker.bigdata.web;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.iworker.bigdata.common.DateUtil;
import com.iworker.bigdata.common.HbaseUtil;
import com.iworker.bigdata.common.IworkerUtils;
import com.iworker.bigdata.domain.AddReport;
import com.iworker.bigdata.service.DashBoardReportService;
import com.iworker.bigdata.service.SalesCurveReportService;

@RestController 
@RequestMapping(value="/report") 
public class DashBoardReportController {

	@Autowired
	private HbaseUtil hbaseUtil;
	@Autowired
	private DashBoardReportService dashBoardReportService;
	@Autowired
	private SalesCurveReportService salesCurveReportService;
 	
	private final Logger logger = Logger.getLogger(getClass());
	private static final String thisWeek = "1";//本周
	private static final String lastWeek = "2";//上周
	private static final String thisMonth = "3";//本月
	private static final String lastMonth = "4";//上月
	private static final String thisYear = "5";//本年
	
	@RequestMapping(value = "/addReport", method = RequestMethod.POST)
	public String addReport(@RequestBody String json) {
		
		HTable reportTable = null;
		try {
			logger.info("QueryController:addReport接受到请求" + json);
			System.out.println("QueryController:addReport接受到请求" + json);
			AddReport addReport = JSON.parseObject(json, AddReport.class);
			String company_id = addReport.getCompany_id();
			if(StringUtils.isBlank(company_id)) {
				return "";
			}
			String org_id = addReport.getOrg_id();
			String user_id = addReport.getUser_id();
			String sales_volume = addReport.getSales_volume();
			String order_number = addReport.getOrder_number();
			String user_name = addReport.getUser_name();
			String org_name = addReport.getOrg_name();
			reportTable = hbaseUtil.getTable(DashBoardReportService.reportTableName);
			//组织rowkey
			String rowkey = company_id + "_" + DateUtil.getStartOfToday();
			Put put = new Put(Bytes.toBytes(rowkey));
			//冗余公司id
			put.addColumn(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("company_id"), Bytes.toBytes(company_id));
			
			if(StringUtils.isNotBlank(org_id)) {
				put.addColumn(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("org_id"), Bytes.toBytes(org_id));
			}
			if(StringUtils.isNotBlank(user_id)) {
				put.addColumn(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("user_id"), Bytes.toBytes(user_id));
			}
			if(StringUtils.isNotBlank(sales_volume)) {
				put.addColumn(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("sales_volume"), Bytes.toBytes(sales_volume));
			}
			if(StringUtils.isNotBlank(order_number)) {
				put.addColumn(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("order_number"), Bytes.toBytes(order_number));
			}
			if(StringUtils.isNotBlank(user_name)) {
				put.addColumn(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("user_name"), Bytes.toBytes(user_name));
			}
			if(StringUtils.isNotBlank(org_name)) {
				put.addColumn(Bytes.toBytes(DashBoardReportService.reportTableFamily), Bytes.toBytes("org_name"), Bytes.toBytes(org_name));
			}
			reportTable.put(put);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		} finally {
			hbaseUtil.closeTable(reportTable);
		}
		
		return "";
	}
	
	/**
	 * 查询全公司仪表盘
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/company/dashboard", method = RequestMethod.POST)
	public String queryCompanyDashBoard(@RequestBody String json) {
	
		try {
			logger.info("QueryController:queryCompanyDashBoard接受到请求" + json);
			System.out.println("QueryController:queryCompanyDashBoard接受到请求" + json);
			Map map = JSON.parseObject(json, Map.class);
			String type = IworkerUtils.get(map, "type");
			String companyId = IworkerUtils.get(map, "companyId");
			if(StringUtils.isBlank(companyId)) {
				return "";
			}
			
			if(thisWeek.equals(type)) {
				//本周
				return dashBoardReportService.CompanyDashBoardThisWeek(map);
			} else if(lastWeek.equals(type)) {
				//上周
				return dashBoardReportService.CompanyDashBoardLastWeek(map);
			} else if(thisMonth.equals(type)) {
				//本月
				return dashBoardReportService.CompanyDashBoardThisMonth(map);
			} else if(lastMonth.equals(type)) {
				//上月
				return dashBoardReportService.CompanyDashBoardLastMonth(map);
			} else if(thisYear.equals(type)) {
				//本年
				return dashBoardReportService.CompanyDashBoardThisYear(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		}
		return "";
	}
	
	/**
	 * 公司销售曲线
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/company/salescurve", method = RequestMethod.POST)
	public String queryCompanySalesCurve (@RequestBody String json) {
		try {
			logger.info("QueryController:queryCompanySalesCurve接受到请求" + json);
			System.out.println("QueryController:queryCompanySalesCurve接受到请求" + json);
			Map map = JSON.parseObject(json, Map.class);
			String type = IworkerUtils.get(map, "type");
			String company_id = IworkerUtils.get(map, "company_id");
			if(StringUtils.isBlank(company_id)) {
				return "";
			}
			
			if(thisWeek.equals(type)) {
				//本周
				return salesCurveReportService.SalesCurveThisWeek(map);
			} else if(lastWeek.equals(type)) {
				//上周
				return salesCurveReportService.SalesCurveLastWeek(map);
			} else if(thisMonth.equals(type)) {
				//本月
				return salesCurveReportService.SalesCurveThisMonth(map);
			} else if(lastMonth.equals(type)) {
				//上月
				return salesCurveReportService.SalesCurveLastMonth(map);
			} else if(thisYear.equals(type)) {
				//本年
				return salesCurveReportService.SalesCurveThisYear(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		}
		return "";
	}
	
	/*@RequestMapping(value = "/company", method = RequestMethod.POST)
	public String queryCompanyReport(@RequestBody String json) {
		HTable reportTable = null;
		HTable targetSaleTable = null;
		try {
			logger.info("QueryController:queryProduct接受到请求" + json);
			System.out.println("QueryController:queryProduct接受到请求" + json);
			Map map = JSON.parseObject(json, Map.class);
			String type = IworkerUtils.get(map, "type");
			String company_id = IworkerUtils.get(map, "company_id");
			if(StringUtils.isBlank(company_id)) {
				return "";
			}
			
			if(thisWeek.equals(type)) {
				//选中的是本周
				//本周周一
				Long firstDayOfWeek = DateUtil.getFirstDayOfWeek();
				//本周周末
				Long lastDayOfWeek = DateUtil.getLastDayOfWeek();
				String start = company_id + "_" + firstDayOfWeek;
				String end = company_id + "_" + lastDayOfWeek;
				
				//设置过滤条件
				Scan scanReport = new Scan(Bytes.toBytes(start), Bytes.toBytes(end));
				//查询
				reportTable = hbaseUtil.getTable(reportTableName);
				ResultScanner ress = reportTable.getScanner(scanReport);
				//最后返回结果封装到Report里面
				Report report = new Report();
				//销售总额，即实际销售额
				Long real_sales = 0L;
				
				int x = 1;
				for(Result result : ress){
					//有7条数据
					ReportDetail detail = new ReportDetail();
					detail.setX(x);//1-7
					x++;
					Long salesVolume = 0L; //当天销售额
					Long orderNumber = 0L; //订单数
					//当天的销售额
					Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("sales_volume"));
					String salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
					if(StringUtils.isNotBlank(salesVolumeStr)) {
						try {
							salesVolume = Long.parseLong(salesVolumeStr);
							detail.setSales_volume(salesVolume);
							//累加到实际销售额上
							real_sales += salesVolume;
						} catch (Exception e) {
							logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
						}
					}
					
					//订单数，即成交单数
					Cell orderNumberCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("order_number"));
					String orderNumberStr = new String(CellUtil.cloneValue(orderNumberCell));
					if(StringUtils.isNotBlank(orderNumberStr)) {
						try {
							orderNumber = Long.parseLong(orderNumberStr);
							detail.setOrder_number(orderNumber);
						} catch (Exception e) {
							logger.error("转换成交单数时数据出错  orderNumberStr " + orderNumberStr);
						}
					}
					//计算客单价=销售额/成交单数
					if(orderNumber.longValue() != 0) {
						detail.setGuest_unit_price(Double.valueOf(salesVolume/orderNumber));
					}
					
					report.addDetail(detail);
				}
				
				//实际销售额
				report.setReal_sales(real_sales);
				
				//目标销售额
				targetSaleTable = hbaseUtil.getTable(targetSaleTableName);
				Scan targetScan = new Scan();
				String prefix = DateUtil.getYear() + "#" + company_id + "#";
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
				Long lastDayOfLastWeek2 = DateUtil.getLastDayOfLastWeek();//上周周末
				String lastStart = company_id + "_" + firstDayOfLastWeek;
				String lastEnd = company_id + "_" + lastDayOfLastWeek2;
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
				
			} else if(lastWeek.equals(type)) {
				//如果选中的是上周
				//上周周一
				Long firstDayOfLastWeek = DateUtil.getFirstDayOfLastWeek();
				//上周周末
				Long lastDayOfLastWeek = DateUtil.getLastDayOfLastWeek();
				
				String start = company_id + "_" + firstDayOfLastWeek;
				String end = company_id + "_" + lastDayOfLastWeek;
				
				//设置过滤条件
				Scan scanReport = new Scan(Bytes.toBytes(start), Bytes.toBytes(end));
				//查询
				reportTable = hbaseUtil.getTable(reportTableName);
				ResultScanner ress = reportTable.getScanner(scanReport);
				//最后返回结果封装到Report里面
				Report report = new Report();
				//销售总额，即实际销售额
				Long real_sales = 0L;
				
				int x = 1;
				for(Result result : ress){
					//有7条数据
					ReportDetail detail = new ReportDetail();
					detail.setX(x);//1-7
					x++;
					Long salesVolume = 0L; //当天销售额
					Long orderNumber = 0L; //订单数
					//当天的销售额
					Cell salesVolumeCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("sales_volume"));
					String salesVolumeStr = new String(CellUtil.cloneValue(salesVolumeCell));
					if(StringUtils.isNotBlank(salesVolumeStr)) {
						try {
							salesVolume = Long.parseLong(salesVolumeStr);
							detail.setSales_volume(salesVolume);
							//累加到实际销售额上
							real_sales += salesVolume;
						} catch (Exception e) {
							logger.error("统计销售额时数据出错  salesVolumeStr " + salesVolumeStr);
						}
					}
					
					//订单数，即成交单数
					Cell orderNumberCell = result.getColumnLatestCell(Bytes.toBytes(reportTableFamily), Bytes.toBytes("order_number"));
					String orderNumberStr = new String(CellUtil.cloneValue(orderNumberCell));
					if(StringUtils.isNotBlank(orderNumberStr)) {
						try {
							orderNumber = Long.parseLong(orderNumberStr);
							detail.setOrder_number(orderNumber);
						} catch (Exception e) {
							logger.error("转换成交单数时数据出错  orderNumberStr " + orderNumberStr);
						}
					}
					//计算客单价=销售额/成交单数
					if(orderNumber.longValue() != 0) {
						detail.setGuest_unit_price(Double.valueOf(salesVolume/orderNumber));
					}
					
					report.addDetail(detail);
				}
				
				//实际销售额
				report.setReal_sales(real_sales);
				
				//目标销售额
				targetSaleTable = hbaseUtil.getTable(targetSaleTableName);
				Scan targetScan = new Scan();
				String prefix = DateUtil.getYear() + "#" + company_id + "#";
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
				String lastStart = company_id + "_" + firstDayOfLastLastWeek;
				String lastEnd = company_id + "_" + lastDayOfLastLastWeek;
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
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			return "";
		} finally {
			hbaseUtil.closeTable(reportTable);
			hbaseUtil.closeTable(targetSaleTable);
		}
		return "";
	}*/
}
