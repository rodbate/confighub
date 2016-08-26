package com.iworker.bigdata.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Calendar.*;
import static com.iworker.bigdata.common.Constants.*;

/**
 *
 * 常用工具类
 *
 */
public class CommonUtil {


    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);


    /**
     * 为PreparedStatement 设置参数
     */
    public static void setParamsForPS(PreparedStatement ps, Object[] params) throws SQLException {

        for (int i = 0; i < params.length; i++) {
            ps.setObject(i+1, params[i]);
        }

    }


    /**
     * 获取某周的星期一和星期日
     * @param date 指定的时间
     * @param formatIn 输入格式
     * @param formatOut 输出格式
     * @param week -1 表示上周  0 表示本周  1 表示下周  以此类推
     * @return map
     */
    public static Map<String, String> getMonAndSun(String date, String formatIn, String formatOut, int week){


        Map<String, String> map = new HashMap<>();

        SimpleDateFormat sdfIn = new SimpleDateFormat(formatIn);
        SimpleDateFormat sdfOut = new SimpleDateFormat(formatOut);

        Calendar calendar = getInstance();

        try {
            calendar.setTime(sdfIn.parse(date));

            calendar.add(WEEK_OF_YEAR, week);

            int day = calendar.get(DAY_OF_WEEK);
            if (day == 1) {
                //时间为星期天
                calendar.add(WEEK_OF_YEAR, -1);
            }

            calendar.set(DAY_OF_WEEK, MONDAY);

            map.put("first", sdfOut.format(calendar.getTime()));

            calendar.add(WEEK_OF_YEAR, 1);
            calendar.set(DAY_OF_WEEK, SUNDAY);
            map.put("last", sdfOut.format(calendar.getTime()));

        } catch (ParseException e) {
            LOGGER.info(e.getMessage(), e);
        }

        return map;
    }

    /**
     *
     * 获取某月或某年的第一天和最后一天
     * @param date 指定的时间
     * @param formatIn 输入格式
     * @param formatOut 输出格式
     * @param month -1 表示上月(年)  0 表示本月(年)  1 表示下月(年)  以此类推
     * @param unitType   Calendar.MONTH | Calendar.YEAR
     * @param dayType   Calendar.DAY_OF_MONTH | Calendar.DAY_OF_YEAR
     * @return map
     */
    private static Map<String, String> getFirstAndLastDay(String date, String formatIn, String formatOut, int month, int unitType, int dayType){

        Map<String, String> map = new HashMap<>();

        SimpleDateFormat sdfIn = new SimpleDateFormat(formatIn);
        SimpleDateFormat sdfOut = new SimpleDateFormat(formatOut);

        Calendar calendar = getInstance();

        try {

            calendar.setTime(sdfIn.parse(date));

            calendar.add(unitType, month);

            int firstDay = calendar.getActualMinimum(dayType);
            int lastDay = calendar.getActualMaximum(dayType);

            calendar.set(dayType, firstDay);
            map.put("first", sdfOut.format(calendar.getTime()));

            calendar.set(dayType, lastDay);
            map.put("last", sdfOut.format(calendar.getTime()));

        } catch (ParseException e) {
            LOGGER.info(e.getMessage(), e);
        }

        return map;
    }


    /**
     * 获取某年的第一天和最后一天
     * @param date 指定的时间
     * @param formatIn 输入格式
     * @param formatOut 输出格式
     * @param month -1 表示上年  0 表示本年 1 表示下年  以此类推
     * @return map
     */
    public static Map<String, String> getFirstAndLastDayByYear(String date, String formatIn, String formatOut, int month){

        return getFirstAndLastDay(date, formatIn, formatOut, month, YEAR, DAY_OF_YEAR);

    }

    /**
     * 获取某月的第一天和最后一天
     * @param date 指定的时间
     * @param formatIn 输入格式
     * @param formatOut 输出格式
     * @param month -1 表示上月  0 表示本月  1 表示下月  以此类推
     * @return map
     */
    public static Map<String, String> getFirstAndLastDayByMonth(String date, String formatIn, String formatOut, int month){
        return getFirstAndLastDay(date, formatIn, formatOut, month, MONTH, DAY_OF_MONTH);
    }


    /**
     *
     * 根据输入的当前时间获取本周的星期一，和星期日
     * @param date 输入的时间
     * @param formatIn 输入的时间格式
     * @param formatOut 输出的时间格式
     * @return map
     */
    public static Map<String, String> getMonAndSunByThisWeek(String date, String formatIn, String formatOut){
        return getMonAndSun(date, formatIn, formatOut, 0);
    }

    public static Map<String, String> getMonAndSunByThisWeek(Date date, String formatIn, String formatOut){

        return getMonAndSunByThisWeek(new SimpleDateFormat(formatIn).format(date), formatIn, formatOut);
    }



    public static Map<String, String> getMonAndSunByThisWeek(Date date){

        return getMonAndSunByThisWeek(new SimpleDateFormat(DATE_FORMAT_YYYYMMDD).format(date), DATE_FORMAT_YYYYMMDD, DATE_FORMAT_YYYYMMDD);
    }

    public static Map<String, String> getMonAndSunByThisWeek(String date){

        return getMonAndSunByThisWeek(date, DATE_FORMAT_YYYYMMDD, DATE_FORMAT_YYYYMMDD);
    }

    public static Map<String, String> getMonAndSunByLastWeek(String date, String formatIn, String formatOut){
        return getMonAndSun(date, formatIn, formatOut, -1);
    }

    public static Map<String, String> getMonAndSunByLastWeek(Date date, String formatIn, String formatOut){
        return getMonAndSun(new SimpleDateFormat(formatIn).format(date), formatIn, formatOut, -1);
    }

    public static Map<String, String> getMonAndSunByLastWeek(String date){
        return getMonAndSun(date, DATE_FORMAT_YYYYMMDD, DATE_FORMAT_YYYYMMDD, -1);
    }

    public static Map<String, String> getMonAndSunByLastWeek(Date date){
        return getMonAndSun(new SimpleDateFormat(DATE_FORMAT_YYYYMMDD).format(date), DATE_FORMAT_YYYYMMDD, DATE_FORMAT_YYYYMMDD, -1);
    }

    public static Map<String, String> getFirstAndLastDayByThisMonth(Date date){
        return getFirstAndLastDayByMonth(new SimpleDateFormat(DATE_FORMAT_YYYYMMDD).format(date), DATE_FORMAT_YYYYMMDD, DATE_FORMAT_YYYYMMDD, 0);
    }

    public static Map<String, String> getFirstAndLastDayByThisMonth(Date date, String formatIn, String formatOut){
        return getFirstAndLastDayByMonth(new SimpleDateFormat(formatIn).format(date), formatIn, formatOut, 0);
    }

    public static Map<String, String> getFirstAndLastDayByThisMonth(String date){
        return getFirstAndLastDayByMonth(date, DATE_FORMAT_YYYYMMDD, DATE_FORMAT_YYYYMMDD, 0);
    }

    public static Map<String, String> getFirstAndLastDayByThisMonth(String date, String formatIn, String formatOut){
        return getFirstAndLastDayByMonth(date, formatIn, formatOut, 0);
    }

    public static Map<String, String> getFirstAndLastDayByLastMonth(String date, String formatIn, String formatOut){
        return getFirstAndLastDayByMonth(date, formatIn, formatOut, -1);
    }

    public static Map<String, String> getFirstAndLastDayByLastMonth(String date){
        return getFirstAndLastDayByMonth(date, DATE_FORMAT_YYYYMMDD, DATE_FORMAT_YYYYMMDD, -1);
    }

    public static Map<String, String> getFirstAndLastDayByLastMonth(Date date){
        return getFirstAndLastDayByMonth(new SimpleDateFormat(DATE_FORMAT_YYYYMMDD).format(date), DATE_FORMAT_YYYYMMDD, DATE_FORMAT_YYYYMMDD, -1);
    }

    public static Map<String, String> getFirstAndLastDayByThisYear(String date, String formatIn, String formatOut){
        return getFirstAndLastDayByYear(date, formatIn, formatOut, 0);
    }

    public static Map<String, String> getFirstAndLastDayByLastYear(String date, String formatIn, String formatOut){
        return getFirstAndLastDayByYear(date, formatIn, formatOut, -1);
    }

    public static Map<String, String> getFirstAndLastDayByThisYear(Date date, String formatIn, String formatOut){
        return getFirstAndLastDayByYear(new SimpleDateFormat(formatIn).format(date), formatIn, formatOut, 0);
    }

    public static Map<String, String> getFirstAndLastDayByLastYear(Date date, String formatIn, String formatOut){
        return getFirstAndLastDayByYear(new SimpleDateFormat(formatIn).format(date), formatIn, formatOut, -1);
    }

    public static Map<String, String> getFirstAndLastDayByLastYear(String date){
        return getFirstAndLastDayByYear(date, DATE_FORMAT_YYYYMMDD, DATE_FORMAT_YYYYMMDD, -1);
    }

    public static Map<String, String> getFirstAndLastDayByThisYear(String date){
        return getFirstAndLastDayByYear(date, DATE_FORMAT_YYYYMMDD, DATE_FORMAT_YYYYMMDD, 0);
    }

    public static Map<String, String> getFirstAndLastDayByLastYear(Date date){
        return getFirstAndLastDayByYear(new SimpleDateFormat(DATE_FORMAT_YYYYMMDD).format(date), DATE_FORMAT_YYYYMMDD, DATE_FORMAT_YYYYMMDD, -1);
    }

    public static Map<String, String> getFirstAndLastDayByThisYear(Date date){
        return getFirstAndLastDayByYear(new SimpleDateFormat(DATE_FORMAT_YYYYMMDD).format(date), DATE_FORMAT_YYYYMMDD, DATE_FORMAT_YYYYMMDD, 0);
    }


    public static boolean isNotNull(String src){
        return (src != null && src.trim().length() != 0);
    }

    /**
     * 构建时间格式正则表达式   20160901
     * @param first 第一天日期
     * @param last 最后一天日期
     * @param type 维度类型
     * @return regex String
     */
    public static String buildDateRegex(String first, String last, DateRegexType type){

        if (!isNotNull(first) || !isNotNull(last) || first.length() != 8 || last.length() != 8)
            throw new RuntimeException("输入日期格式有误!!!");

        String regex = "";

        if (type == DateRegexType.YEAR) {

            String year = first.substring(0, 4);
            //"2016.*"
            regex = String.format("%s.*", year);
        }

        if (type == DateRegexType.MONTH) {

            String yearAndMonth = first.substring(0, 6);
            //"201608.*"
            regex = String.format("%s.*", yearAndMonth);
        }

        if (type == DateRegexType.WEEK) {

            //跨年 例如：20151228(MON) -- 20160103(SUN)
            //regex  "(201512((2[8-9])|(3[0-1])))|(2016010[1-3])"
            int yearPre = Integer.valueOf(first.substring(0, 4));
            int yearPost = Integer.valueOf(last.substring(0, 4));
            if (yearPost != yearPre) {
                //201512
                String yMFirst = first.substring(0, 6);

                //28
                String dayFirst = first.substring(6);


                //取出改年的最后一天日期  如 20151231
                Map<String, String> mapFirst = getFirstAndLastDayByThisYear(first);
                String lastFirst = mapFirst.get("last");

                //取出第二年的前7位 如: 2016010
                //Map<String, String> mapLast = getFirstAndLastDayByThisYear(first);
                String lastYearSeven = last.substring(0, 7);

                //1
                String lastFirstSec = lastFirst.substring(7);

                //3
                String lastYearPost = last.substring(7);

                if (Integer.valueOf(dayFirst) < 30) {

                    //8
                    String dayFirstSec = dayFirst.substring(1);

                    regex = String.format("(%s((2[%s-9])|(3[0-%s])))|(%s[1-%s])", yMFirst, dayFirstSec, lastFirstSec, lastYearSeven, lastYearPost);
                } else {
                    //2015123
                    String lastIndexSeven = first.substring(0, 7);
                    regex = String.format("(%s[0-%s])|(%s[1-%s])", lastIndexSeven, lastFirstSec, lastYearSeven, lastYearPost);
                }


            }

            //yearPost == yearPre 不跨年
            else {

                int monthPre = Integer.valueOf(first.substring(4, 6));
                int monthPost = Integer.valueOf(last.substring(4, 6));

                //跨月  20160627 --- 20160703
                if (monthPre != monthPost) {

                    //例如   2016(06((2[7-9])|(30)))|(070[1-3])

                    //如果该月的最后一天小于 30 比如 0228|0229
                    //取出该月最后一天日期 28|29
                    String last1 = getFirstAndLastDayByThisMonth(first).get("last");
                    if (Integer.valueOf(last1.substring(6)) < 30) {
                        //2016(06(2[7-9]))|(070[1-3])
                        regex = String.format("%s(%s[%s-%s])|(%s[1-%s])", String.valueOf(yearPre), first.substring(4, 7), first.substring(7),
                                last1.substring(7), last.substring(4, 7), last.substring(7));
                    } else {

                        //取出第一天的日期 如： 27
                        int firstDay = Integer.valueOf(first.substring(6));
                        if (firstDay < 30) {
                            regex = String.format("%s(%s((2[%s-9])|(3[0-%s])))|(%s[1-%s])", String.valueOf(yearPre), first.substring(4, 6),
                                    first.substring(7), last1.substring(7), last.substring(4, 7), last.substring(7));
                        } else {
                            regex = String.format("%s(%s[0-%s])|(%s[1-%s])", String.valueOf(yearPre), first.substring(4, 7), last1.substring(7),
                                    last.substring(4, 7), last.substring(7));
                        }
                    }

                }

                //月内  20160808 --- 20160814
                else {

                    int dayF = Integer.valueOf(first.substring(6, 7));
                    int dayL = Integer.valueOf(last.substring(6, 7));

                    // 01 -- 07 2016080[1-7]
                    if (dayF == dayL) {
                        regex = String.format("%s[%s-%s]", first.substring(0,7), first.substring(7), last.substring(7));
                    } else {
                        regex = String.format("%s(%s[%s-9])|(%s[0-%s])", first.substring(0, 6), first.substring(6, 7), first.substring(7),
                                last.substring(6, 7), last.substring(7));
                    }

                }

            }


        }

        return regex;
    }




    public enum DateRegexType{

        YEAR,  //年维度
        MONTH, //月维度
        WEEK   //周维度
    }

}
