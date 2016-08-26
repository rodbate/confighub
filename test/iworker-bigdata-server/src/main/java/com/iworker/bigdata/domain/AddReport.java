package com.iworker.bigdata.domain;

/**
 * 添加报表的数据
 * @作者 张震文
 * 2016年8月23日-下午3:05:27
 */
public class AddReport {

	/**
	 * 公司id
	 */
	private String company_id;
	
	/**
	 * 部门id
	 */
	private String org_id;
	
	/**
	 * 店员id
	 */
	private String user_id;
	
	/**
	 * 店员每天的销售额
	 */
	private String sales_volume;
	
	/**
	 * 成交单数
	 */
	private String order_number;
	
	/**
	 * 店员姓名
	 */
	private String user_name;
	
	/**
	 * 部门名
	 */
	private String org_name;
	

	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getSales_volume() {
		return sales_volume;
	}

	public void setSales_volume(String sales_volume) {
		this.sales_volume = sales_volume;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
}
