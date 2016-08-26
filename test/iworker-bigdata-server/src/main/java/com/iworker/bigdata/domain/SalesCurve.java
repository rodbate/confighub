package com.iworker.bigdata.domain;

/**
 * 销售曲线实体类
 * @作者 张震文
 * 2016年8月24日-下午2:16:46
 */
public class SalesCurve {

	/**
	 * 代表横坐标的值，周就是1-7，月就是1-30/31
	 */
	private Integer x;

	/**
	 * 当天，或者当月销售额
	 */
	private Long salesVolume;
	
	/**
	 * 成交单数，即订单数
	 */
	private Long orderNumber;
	
	/**
	 * 客单价=销售额/成交单数
	 */
	private Double guestUnitPrice;

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Long getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(Long salesVolume) {
		this.salesVolume = salesVolume;
	}

	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Double getGuestUnitPrice() {
		return guestUnitPrice;
	}

	public void setGuestUnitPrice(Double guestUnitPrice) {
		this.guestUnitPrice = guestUnitPrice;
	}
}
