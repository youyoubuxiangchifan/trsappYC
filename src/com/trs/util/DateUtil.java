package com.trs.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * 
 * @Description:时间操作工具类<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jin.yu
 * @ClassName: DateUtil
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-6 下午04:37:49
 * @version 1.0
 */
public class DateUtil {

	public static Date date = null;

	public static DateFormat dateFormat = null;

	public static Calendar calendar = null;

	/**
	 * 
	* @Description: TODO(格式化日期) <BR>   
	* @author jinyu
	* @date 2014-3-6 下午04:42:09
	* @param dateStr String 字符型日期
	* @param format	String 格式
	* @return Date 日期
	* @version 1.0
	 */
	public static Date parseDate(String dateStr, String format) {
		try {
			dateFormat = new SimpleDateFormat(format);
			//String dt = dateStr.replaceAll("-", "/");
//			if ((!dt.equals("")) && (dt.length() < format.length())) {
//				dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]",
//						"0");
//			}
//			System.out.println(dt);
			date = (Date) dateFormat.parse(dateStr);
		} catch (Exception e) {
			
		}
		return date;
	}

	/**
	 * 功能描述：格式化日期
	 * 
	 * @param dateStr
	 *            String 字符型日期：YYYY-MM-DD 格式
	 * @return Date
	 */
	public static Date parseDate(String dateStr) {
		return parseDate(dateStr, "yyyy-MM-dd");
	}

	/**
	 * 功能描述：格式化输出日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return 返回字符型日期
	 */
	public static String format(Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				dateFormat = new SimpleDateFormat(format);
				result = dateFormat.format(date);
			}
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 功能描述：
	 * 
	 * @param date
	 *            Date 日期
	 * @return
	 */
	public static String format(Date date) {
		return format(date, "yyyy/MM/dd");
	}

	/**
	 * 功能描述：返回年份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回年份
	 */
	public static int getYear(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 功能描述：返回月份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回月份
	 */
	public static int getMonth(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 功能描述：返回日份
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回日份
	 */
	public static int getDay(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 功能描述：返回小时
	 * 
	 * @param date
	 *            日期
	 * @return 返回小时
	 */
	public static int getHour(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 功能描述：返回分钟
	 * 
	 * @param date
	 *            日期
	 * @return 返回分钟
	 */
	public static int getMinute(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * 返回秒钟
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回秒钟
	 */
	public static int getSecond(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 功能描述：返回毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	/**
	 * 功能描述：返回字符型日期
	 * 
	 * @param date
	 *            日期
	 * @return 返回字符型日期 yyyy/MM/dd 格式
	 */
	public static String getDate(Date date) {
		return format(date, "yyyy/MM/dd");
	}

	/**
	 * 功能描述：返回字符型时间
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回字符型时间 HH:mm:ss 格式
	 */
	public static String getTime(Date date) {
		return format(date, "HH:mm:ss");
	}

	/**
	 * 功能描述：返回字符型日期时间
	 * 
	 * @param date
	 *            Date 日期
	 * @return 返回字符型日期时间 yyyy/MM/dd HH:mm:ss 格式
	 */
	public static String getDateTime(Date date) {
		return format(date, "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * 功能描述：日期相加
	 * 
	 * @param date
	 *            Date 日期
	 * @param day
	 *            int 天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int day) {
		calendar = Calendar.getInstance();
		long millis = getMillis(date) + ((long) day) * 24 * 3600 * 1000;
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	/**
	 * 功能描述：日期相减
	 * 
	 * @param date
	 *            Date 日期
	 * @param date1
	 *            Date 日期
	 * @return 返回相减后的日期
	 */
	public static int diffDate(Date date, Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}

	/**
	 * 功能描述：取得指定月份的第一天
	 * 
	 * @param strdate
	 *            String 字符型日期
	 * @return String yyyy-MM-dd 格式
	 */
	public static String getMonthBegin(String strdate) {
		date = parseDate(strdate);
		return format(date, "yyyy-MM") + "-01";
	}

	/**
	 * 功能描述：取得指定月份的最后一天
	 * 
	 * @param strdate
	 *            String 字符型日期
	 * @return String 日期字符串 yyyy-MM-dd格式
	 */
	public static String getMonthEnd(String strdate) {
		date = parseDate(getMonthBegin(strdate));
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return formatDate(calendar.getTime());
	}

	/**
	 * 功能描述：常用的格式化日期
	 * 
	 * @param date
	 *            Date 日期
	 * @return String 日期字符串 yyyy-MM-dd格式
	 */
	public static String formatDate(Date date) {
		return formatDateByFormat(date, "yyyy-MM-dd");
	}

	/**
	 * 功能描述：以指定的格式来格式化日期
	 * 
	 * @param date
	 *            Date 日期
	 * @param format
	 *            String 格式
	 * @return String 日期字符串
	 */
	public static String formatDateByFormat(Date date, String format) {
		String result = "";
		if (date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 
	* Description: 返回系统当前时间 <BR>   
	* @author jin.yu
	* @date 2014-4-4 上午11:19:23
	* @return Date 日期对象
	* @version 1.0
	 */
	public static Date now(){
		Date date = new java.util.Date(System.currentTimeMillis());
		return date;
	}
	
	/**
	 * 
	* Description: 以字符串返回系统当前时间，返回格式为：yyyy-MM-dd HH:mm:ss <BR>   
	* @author jin.yu
	* @date 2014-4-4 上午11:19:18
	* @return String系统当前时间，格式：yyyy-MM-dd HH:mm:ss
	* @version 1.0
	 */
	public static String nowFormat(){
		Date date = new java.util.Date(System.currentTimeMillis());
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}
	public static Timestamp nowSqlDate() throws ParseException{
		
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        java.util.Date du = sp.parse(nowFormat());  
            java.sql.Timestamp st = new java.sql.Timestamp(du.getTime());
            return st;
	}
	public static Timestamp nowSqlDate(String date) throws ParseException{
			SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	        java.util.Date du = sp.parse(date);  
	            java.sql.Timestamp st = new java.sql.Timestamp(du.getTime());
	            return st;
		
	}
	public static java.sql.Date nowSqlToDate(String date) throws ParseException{
	
		 java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
	        Date date1 = format.parse(date);
	        return new java.sql.Date((date1.getTime()));
	      
	}
	/**
	 * 
	* Description: 取得两个日期之间的日期，返回日期的集合 <BR>   
	* @author jin.yu
	* @date 2014-4-11 下午03:37:02
	* @param dBegin 开始时间
	* @param dEnd 结束时间
	* @return List 日期的集合 日期格式为：yyyy-MM-dd
	* @version 1.0
	 */
	public static List<String> findDates(Date dBegin, Date dEnd)
	 {
	  List<String> lDate = new ArrayList<String>();
	  lDate.add(formatDate(dBegin));
	  Calendar calBegin = Calendar.getInstance();
	  // 使用给定的 Date 设置此 Calendar 的时间
	  calBegin.setTime(dBegin);
	  Calendar calEnd = Calendar.getInstance();
	  // 使用给定的 Date 设置此 Calendar 的时间
	  calEnd.setTime(dEnd);
	  // 测试此日期是否在指定日期之后
	  while (dEnd.after(calBegin.getTime()))
	  {
	   // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
	   calBegin.add(Calendar.DAY_OF_MONTH, 1);
	   lDate.add(formatDate(calBegin.getTime()));
	  }
	  return lDate;
	 }
	 /**
	  * 
	 * Description:和当前日期进行比较 <BR>   
	 * @author zhangzun
	 * @date 2014-5-4 下午03:14:07
	 * @param sDate
	 * @return 比较结果
	 * @version 1.0
	  */
	 public static int compareNow(String sDate){
		 Date _date = parseDate(sDate);
		 Date now =  now();
		 return _date.compareTo(now);
	 }
	public static void main(String[] args) {
		Date d = new Date();
		List<String> date = findDates(d,DateUtil.addDate(d, 10));
		for (String str : date) {
			System.out.println(str);
		}
//		Date d = new Date();
//		System.out.println(d.toString());
//		System.out.println(formatDate(d).toString());
		// System.out.println(getMonthBegin(formatDate(d).toString()));
		// System.out.println(getMonthBegin("2008/07/19"));
		// System.out.println(getMonthEnd("2008/07/19"));
//		System.out.println(format(d,"yyyy-MM-dd"));
//		System.out.println(getDay(d));
//		System.out.println(getMonth(d));
//		System.out.println(getYear(d));
//		System.out.println(format(parseDate("2014-03-19"), "yyyy/MM/dd"));
		System.out.println(System.currentTimeMillis()-getMillis(parseDate("20140419","yyyyMMdd")));
	}

}
