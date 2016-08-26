package com.iworker.bigdata.common;

import java.util.Calendar;

/**
 * 获取日期的常用方法
 * @作者 张震文
 * 2016年8月23日-上午9:52:56
 */
public class DateUtil {

	/**
	 * 获取月初第一天的时间戳 
	 * @return
	 */
	public static Long getFirstDayOfMonth() {
		Calendar calendar = Calendar.getInstance();// 获取当前日期  
	    calendar.add(Calendar.MONTH, 0);  //1462032000   682
	    calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天  
	    calendar.set(Calendar.HOUR_OF_DAY, 0);  
	    calendar.set(Calendar.MINUTE, 0);  
	    calendar.set(Calendar.SECOND, 0);  
	    return calendar.getTimeInMillis();
	}
	
	/**
	 * 本月最后一天时间戳
	 * @return
	 */
	 public static Long getLastDayOfMonth() {  
        Calendar calendar = Calendar.getInstance();  
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);  
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
        calendar.set(Calendar.HOUR_OF_DAY, 23);  
        return calendar.getTimeInMillis();  
	 } 
	 
	/**
	 * 上月最后一天时间戳
	 * @return
	 */
	 public static Long getLastDayOfLastMonth() {  
        Calendar calendar = Calendar.getInstance();  
        calendar.add(Calendar.MONTH, -1);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);  
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
        calendar.set(Calendar.HOUR_OF_DAY, 23);  
        return calendar.getTimeInMillis();  
	 }
	 
	 /**
	  * 动态获取某月月月末最后一天
	  * @param amount 0本月
	  * @return
	  */
	 public static Long getDynamicLastDayOfMonth(int amount) {  
		Calendar calendar = Calendar.getInstance();  
        calendar.add(Calendar.MONTH, amount);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);  
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
        calendar.set(Calendar.HOUR_OF_DAY, 23);  
        return calendar.getTimeInMillis();  
	 }
	 
	 /**
	  * 获取指定月份月末时间戳
	  * @param amount 0本月
	  * @return
	  */
	 public static Long getDynamicLastDayWithMonth(int month) {  
		Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);  
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
        calendar.set(Calendar.HOUR_OF_DAY, 23);  
        return calendar.getTimeInMillis();  
	 }
	 
    /**
	 * 获取上月月初第一天的时间戳 
	 * @return
	 */
	public static Long getFirstDayOfLastMonth() {
		Calendar calendar = Calendar.getInstance();// 获取当前日期  
	    calendar.add(Calendar.MONTH, -1);  //1462032000   682
	    calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天  
	    calendar.set(Calendar.HOUR_OF_DAY, 0);  
	    calendar.set(Calendar.MINUTE, 0);  
	    calendar.set(Calendar.SECOND, 0);  
	    return calendar.getTimeInMillis();
	}
	
	/**
	 * 动态获取某月月初
	 * @param amount  0本月，-1上月， 1下月
	 * @return
	 */
	public static Long getDynamicFirstDayOfMonth(int amount) {
		Calendar calendar = Calendar.getInstance();// 获取当前日期  
	    calendar.add(Calendar.MONTH, amount);  //1462032000   682
	    calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天  
	    calendar.set(Calendar.HOUR_OF_DAY, 0);  
	    calendar.set(Calendar.MINUTE, 0);  
	    calendar.set(Calendar.SECOND, 0);  
	    return calendar.getTimeInMillis();
	}
	
	/**
	 * 获取指定月份的月初时间戳
	 * @param month
	 * @return
	 */
	public static Long getDynamicFirstDayWithMonth(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month-1);  //1462032000   682
	    calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天  
	    calendar.set(Calendar.HOUR_OF_DAY, 0);  
	    calendar.set(Calendar.MINUTE, 0);  
	    calendar.set(Calendar.SECOND, 0); 
		return calendar.getTimeInMillis();
	}
	
	/**
	 * 获取本周星期一的时间戳 0h.0m.0s
	 * @return
	 */
	public static Long getFirstDayOfWeek() {
		Calendar calendar = Calendar.getInstance(); //1463932800754
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0); 
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
	    return calendar.getTimeInMillis();
	}
	
	/**
	 * 获取上周星期一的时间戳 0h.0m.0s
	 * @return
	 */
	public static Long getFirstDayOfLastWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0); 
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
		calendar.add(Calendar.WEEK_OF_YEAR, -1);
		return calendar.getTimeInMillis();
	}
	
	/**
	 * 动态获取周一
	 * @param amount 0表示本周，1表示下周，-1表示上周
	 * @return
	 */
	public static Long getDynamicFirstDayOfLastWeek(int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0); 
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); 
		calendar.add(Calendar.WEEK_OF_YEAR, amount);
		return calendar.getTimeInMillis();
	}
	
	/**
	 * 获取本周周末时间戳
	 * 根据本项目需求，应该获取的是星期天0.0.0秒的时间戳
	 * @return
	 */
	public static Long getLastDayOfWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0); 
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); 
		calendar.add(Calendar.WEEK_OF_YEAR, 1);
		return calendar.getTimeInMillis();
	}
	
	/**
	 * 获取上周周末时间戳
	 * 根据本项目需求，应该获取的是星期天0.0.0秒的时间戳
	 * @return
	 */
	public static Long getLastDayOfLastWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0); 
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); 
		return calendar.getTimeInMillis();
	}
	
	/**
	 * 动态获取周末时间戳
	 * 根据本项目需求，应该获取的是星期天0.0.0秒的时间戳
	 * amount 0表示上周，1表示本周，2表示下周
	 * @return
	 */
	public static Long getDynamicLastDayOfWeek(int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0); 
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); 
		calendar.add(Calendar.WEEK_OF_YEAR, amount);
		return calendar.getTimeInMillis();
	}
	
	/**
	 * 获取当天0点0分0秒时间戳
	 * @return
	 */
	public static Long getStartOfToday() {
		Calendar cal = Calendar.getInstance();  
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
	    return cal.getTimeInMillis();
	}
	
	/**
	 * 获取当前月
	 * @return
	 */
	public static String getMonth() {
		return getMonth(0);
	}
	
	/**
	 * 动态获取月
	 * 1表示下月，0表示本月，-1表示上月
	 * @return
	 */
	public static String getMonth(int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, amount);
		return calendar.get(Calendar.MONTH) + 1 + "";
	}
	
	/**
	 * 动态获取周一所在的月份
	 * amount 0表示本周，1表示下周，-1表示上周
	 */
	public static String getWeekInMonth(int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.WEEK_OF_MONTH, amount);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return calendar.get(Calendar.MONTH) + 1 + "";
	}
	
	/**
	 * 获取当前年
	 * @return
	 */
	public static String getYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR) + "";
	}
	
	/**
	 * 获取一年开始的时间戳
	 * amount 0本年，-1去年，1明年
	 * @return
	 */
	public static Long getStartOfYear(int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.add(Calendar.YEAR, amount);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		return calendar.getTimeInMillis();
	}
	
	/**
	 * 获取年底的时间戳
	 * amount 0本年，-1去年，1明年
	 */
	public static Long getEndOfYear(int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, 11);
		calendar.add(Calendar.YEAR, amount);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);  
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        return calendar.getTimeInMillis();
	}
	
	/**
	 * 获取某周同期时间戳，0是本周，-1是上周对应天，1是下周对应天
	 */
	public static Long getSameWeekDay(int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.add(Calendar.WEEK_OF_MONTH, amount);
        return calendar.getTimeInMillis();
	}
	
	/**
	 * 获取某月同期时间戳，0是本月，-1是上月对应天，1是下月对应天
	 */
	public static Long getSameMonthDay(int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.add(Calendar.MONDAY, amount);
        return calendar.getTimeInMillis();
	}
	
	/**
	 * 获取某年同期时间戳，0是本年，-1是去年对应天，1是明年对应天
	 */
	public static Long getSameYearDay(int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		calendar.add(Calendar.YEAR, amount);
        return calendar.getTimeInMillis();
	}
}
