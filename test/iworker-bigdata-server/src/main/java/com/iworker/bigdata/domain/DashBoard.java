package com.iworker.bigdata.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 公司仪表盘数据
 * @作者 张震文
 * 2016年8月22日-下午3:31:09
 */
public class DashBoard implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 完成率=实际销售额/目标销售额
	 */
	private Double completion_rate;
	
	/**
	 * 实际销售额，是每天销售额的总和
	 */
	private Long real_sales;

	/**
	 * 目标销售额
	 */
	private Long target_sales;
	
	/**
	 * 环比 month-on-month
	 */
	private Double mom;
	
	public Long getReal_sales() {
		return real_sales;
	}

	public void setReal_sales(Long real_sales) {
		this.real_sales = real_sales;
	}

	public Long getTarget_sales() {
		return target_sales;
	}

	public void setTarget_sales(Long target_sales) {
		this.target_sales = target_sales;
	}

	public Double getMom() {
		return mom;
	}

	public void setMom(Double mom) {
		this.mom = mom;
	}

	public Double getCompletion_rate() {
		return completion_rate;
	}

	public void setCompletion_rate(Double completion_rate) {
		this.completion_rate = completion_rate;
	}
}
