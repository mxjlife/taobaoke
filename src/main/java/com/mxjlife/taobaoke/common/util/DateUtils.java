package com.mxjlife.taobaoke.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;


/**
 * 类说明:
 * 
 * @author mxj
 * @email xj.meng@sinowaycredit.com
 * @version 创建时间：2017年9月10日 上午11:07:08
 */
public class DateUtils {
    
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 获取指定格式的当前时间的时间字符串
     * @param pattern
     * @return
     */
    public static String getTimeFormat(String pattern) {
        if(StringUtils.isBlank(pattern)){
            pattern = DEFAULT_PATTERN;
        }
        DateFormat sdf = null;
        try {
            sdf = new SimpleDateFormat(pattern);
        } catch (Exception e) {
            sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        }
        String result = sdf.format(new Date());
        return result;
    }
    
    /**
     * 将输入的毫秒数转化为yyyyMMdd HH:mm:ss格式的时间
     * @param milliSecond
     * @return
     */
    public static String convertTime(Long milliSecond, String pattern){
        Date date = new Date(milliSecond);
        DateFormat sdf = null;
        try {
            sdf = new SimpleDateFormat(pattern);
        } catch (Exception e) {
            sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        }
        return sdf.format(date);
    }
    
    /**
     * 解析时间字符串为Date
     * @param dateString 待解析的日期字符串
     * @param pattern  传入的日期字符串的格式 默认yyyyMMdd
     * @return
     */
    public static Date getDate(String dateString, String pattern){
        if(dateString == null){
            return new Date();
        }
        if(pattern == null){
            pattern = DEFAULT_PATTERN;
        }
        DateFormat sdf = null;
        try {
            sdf = new SimpleDateFormat(pattern);
            Date date = sdf.parse(dateString);
            return date;
        } catch (Exception e) {
            System.out.println("获取Date对象失败");
            return null;
        }
    }
    
    /**
     * 获取yyyyMMdd格式的当前系统日期
     * @return
     */
    public static String getCurrentDate() {
        return getTimeFormat("yyyyMMdd");
    }

    
    /**
     * 获取当前日期日历对象
     * 
     * @return 当前日期对应的日历对象
     */
    public static Calendar getNowCalendar() {
        
        // 以数据库服务器时间为准
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    /**
     * 得到当前年份
     * 
     * @return 当前年份
     */
    public static int getCurrentYear() {
        return getNowCalendar().get(Calendar.YEAR);
    }

    /**
     * 得到当前月份
     * 
     * @return 当前月份
     */
    public static int getCurrentMonth() {
        
        // 用get得到的月份数比实际的小1，需要加上
        return getNowCalendar().get(Calendar.MONTH) + 1;
    }

    /**
     * 得到当前日
     * 
     * @return 当前日
     */
    public static int getCurrentDay() {
        return getNowCalendar().get(Calendar.DATE);
    }


    /**
     * 内部方法。为指定日期增加相应的天数或月数
     * 
     * @param date 基准日期
     * @param amount 增加的数量
     * @param field 增加的单位，年，月或者日
     * @return 增加以后的日期
     */
    public static Date add(Date date, int amount, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);

        return calendar.getTime();
    }
    
    private static String getDateString(Date date, String pattern){
        DateFormat sdf = null;
        String result = null;
        try {
            sdf = new SimpleDateFormat(pattern);
            result = sdf.format(date);
        } catch (Exception e) {
            
        }
        return result;
    }
    
    /**
     * 取得系统日期加天数
     * 如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
     * 
     * @param days 增加的日期数
     * @return 增加以后的日期
     */
    public static Date addDays(int days) {
        return add(getDate(null, null), days, Calendar.DATE);
    }
    
    /**
     * 指定日加减相应天数返回指定格式日期
     * 
     * @param dateString 待增加日期
     * @param pattern 输入的日期的格式
     * @param days 增加的日期数
     * @return 增加以后的日期字符串
     */
    public static String addDays(String dateString, String pattern, int days){
        Date date = add(getDate(dateString, pattern), days, Calendar.DATE);
        return getDateString(date, pattern);
    }


    /**
     * 计算两个日期相差天数。 用第一个日期减去第二个。<br>
     * 如果前一个日期小于后一个日期，则返回负数
     * 
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差天数
     */
    public static long diffDays(Date one, Date two) {
        return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
    }

    /**
     * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
     * 
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差月份数
     */
    public static int diffMonths(Date one, Date two) {

        Calendar calendar = Calendar.getInstance();

        //得到第一个日期的年分和月份数
        calendar.setTime(one);
        int yearOne = calendar.get(Calendar.YEAR);
        int monthOne = calendar.get(Calendar.MONDAY);

        //得到第二个日期的年份和月份
        calendar.setTime(two);
        int yearTwo = calendar.get(Calendar.YEAR);
        int monthTwo = calendar.get(Calendar.MONDAY);

        return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
    }
    
    /**
     * 计算两个日期相差年数 如果前一个日期小于后一个日期，则返回负数
     * 
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差年数
     */
    public static int diffYear(Date one, Date two) {

        Calendar calendar = Calendar.getInstance();

        //得到第一个日期的年分和月份数
        calendar.setTime(one);
        int yearOne = calendar.get(Calendar.YEAR);

        //得到第二个日期的年份和月份
        calendar.setTime(two);
        int yearTwo = calendar.get(Calendar.YEAR);

        return (yearOne - yearTwo);
    }

    /**
     * 将一个字符串用给定的格式转换为日期类型。
     * 注意：如果返回null，则表示解析失败
     * 
     * @param datestr 需要解析的日期字符串
     * @return 解析后的日期
     */
    public static Date parse(String datestr){
        if (null == datestr) return null;
        return parse(datestr, null);
    }
    
    /**
     * 将一个字符串用给定的格式转换为日期类型。 <br>
     * 注意：如果返回null，则表示解析失败
     * 
     * @param datestr 需要解析的日期字符串
     * @param pattern 日期字符串的格式，默认为“yyyyMMdd”的形式
     * @return 解析后的日期
     */
    public static Date parse(String datestr, String pattern){
        if (null == datestr) return null;
        Date date = null;
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_PATTERN;
        }
        DateFormat dateFormat = null;
        try {
            dateFormat = new SimpleDateFormat(pattern);
            date = dateFormat.parse(datestr);
        } catch (Exception e) {
            System.out.println("无法将[" + datestr + "]转为[" + pattern + "]格式的日期");
        }
        return date;
    }


    /**
     * 判断是否在有效期
     * @param startDateStr
     * @param endDateStr
     * @return 当在有效期时返回True,否则False
     */
    public boolean isValidDate (String startDateStr, String endDateStr) {
        boolean bResult = false;
        String strSysDate = getCurrentDate();
        
        // 开始或结束日期为空表示无此限制
        if(null == startDateStr || "".equals(startDateStr)) 
            startDateStr = "19700101";
        if(null == endDateStr || "".equals(endDateStr))
            endDateStr = "99999999";
        
        // 判断日期是否在有效期
        bResult = (startDateStr.compareTo(strSysDate) 
                          <= 0 && endDateStr.compareTo(strSysDate) >= 0);
        // 返回值
        return bResult;
    }
    
    /**
     * 判断输入值是否为指定格式的合法日期
     * @param strDate 8位日期字符串
     * @return 合法则返回true,否则返回false
     */
    public static Boolean checkDate(String strDate) {

        try {
            // 如果输入日期不是8位的,判定为false.
            if (null == strDate || "".equals(strDate)
                                    || !strDate.matches("[0-9]{8}")) {
                return false;
            }
            int year = Integer.parseInt(strDate.substring(0, 4));
            int month = Integer.parseInt(strDate.substring(4, 6)) - 1;
            int day = Integer.parseInt(strDate.substring(6));
            Calendar calendar = GregorianCalendar.getInstance();

            // 当 Calendar 处于 non-lenient 模式时，
            // 如果其日历字段中存在任何不一致性，它都会抛出一个异常。
            calendar.setLenient(false);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DATE, day);
            
            // 如果日期错误,执行该语句,必定抛出异常.
            calendar.get(Calendar.YEAR);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    /** 
     * 两个时间相差距离多少秒 
     * @param str1 时间参数 1
     * @param str2 时间参数 2
     * @return long 
     */  
    public static Long compareTimes(String str1, String str2, String pattern) {  
        DateFormat df = new SimpleDateFormat(pattern);
        Date one;  
        Date two;  
        Long sec = null;  
        try {  
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            sec = time1 - time2;
        } catch (ParseException e) {  
            System.out.println(e.getMessage());
        }  
        return sec/1000;  
    }  
    
    /** 
     * 输入时间是当前时间的多少秒之前
     * @param datestr 时间参数 1
     * @param pattern 时间参数 2
     * @return long 
     */  
    public static Long compareTimes(String datestr, String pattern) {  
        DateFormat df = new SimpleDateFormat(pattern);
        Date one;  
        Date two = new Date();  
        Long sec = null;  
        try {  
            one = df.parse(datestr);
            long time1 = one.getTime();
            long time2 = two.getTime();
            sec = (time2 - time1)/1000;
        } catch (ParseException e) {  
            System.out.println(e.getMessage());
        }  
        return sec;  
    }

    /**
     * 判断年份是否是闰年
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year){
        boolean flag = false;
        if(year%100 == 0){
            if(year % 400 == 0){
                flag = true;
            }
        } else {
            if(year % 4 == 0){
                flag = true;
            }
        }
        return flag;
    }

    /**
     * c生成随机的某一天的日期字符串
     * @param startYear
     * @param endYear
     * @return
     */
    public static String generatRandomDataString(int startYear, int endYear){
        if (startYear > endYear){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        Random random = new Random();
        //年
        int randomYear = random.nextInt(endYear-startYear)+startYear;
        calendar.set(Calendar.YEAR, randomYear);
//        //月
//        int randomMonth = random.nextInt(12);
//        calendar.set(Calendar.MONTH, randomMonth);
        //日
        int randomDay = random.nextInt(365)+1;
        calendar.set(Calendar.DAY_OF_YEAR, randomDay);
        sdf.format(calendar.getTime());
        return sdf.format(calendar.getTime());
    }



}
