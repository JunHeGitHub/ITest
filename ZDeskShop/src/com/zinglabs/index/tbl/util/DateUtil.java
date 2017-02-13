

package com.zinglabs.index.tbl.util;

import java.util.*;
import java.text.*;


public class DateUtil {

	
	public static final String MASK_DATE 			= "yyyyMMdd";
	public static final String MASK_DATE_WITH_LINE 	= "yyyy-MM-dd";
	public static final String MASK_DATE_WITH_DOT 	= "yyyy.MM.dd";
	public static final String MASK_TIME_IN_24 		= "HH:mm:ss";
	public static final String MASK_DATE_TIME 		= "yyyyMMddHHmmss";
	public static final String MASK_YEAR 			= "yyyy";
	public static final String MASK_MONTH 			= "MM";
	public static final String MASK_DAY 			= "dd";
	public static final String MASK_HOUR 			= "HH";
	public static final String MASK_MINUTE 			= "mm";
	public static final String MASK_SECOND 			= "ss";
	
	public static final String MASK_DATE_CHINESE_ALL ="yyyy骞碝M鏈坉d鏃? HH:MM";
	public static final String MASK_DATE_CHINESE_YEAR_MONTH_DAY = "yyyy骞碝M鏈坉d鏃?";


	public static Calendar transStringToCalendar(String mask, String str_date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(transStringToDate(mask, str_date));
		return cal;
	}

	
	public static Date transStringToDate(String mask, String str_date) {
		SimpleDateFormat sdf = new SimpleDateFormat(mask);
		Date date = null;
		try {
			date = sdf.parse(str_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	
	public static String transDateToString(String mask, Date date) {
		if (date == null)
			return "";
		return new SimpleDateFormat(mask).format(date);
	}

	
	private static Calendar addDateOrMonthOrYear(int type, int amount, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(type, amount);
		return cal;
	}

	
	public static Calendar addDate(Date date, int amount) {
		return addDateOrMonthOrYear(Calendar.DATE, amount, date);
	}

	
	public static Calendar addDate(String mask, String str_date, int amount) {
		Date date = transStringToDate(mask, str_date);
		return addDate(date, amount);
	}

	
	public static Calendar addMonth(Date date, int amount) {
		return addDateOrMonthOrYear(Calendar.MONTH, amount, date);
	}

	
	public static Calendar addMonth(String mask, String str_date, int amount) {
		Date date = transStringToDate(mask, str_date);
		return addMonth(date, amount);
	}

	
	public static Calendar addYear(Date date, int amount) {
		return addDateOrMonthOrYear(Calendar.YEAR, amount, date);
	}

	
	public static Calendar addYear(String mask, String str_date, int amount) {
		Date date = transStringToDate(mask, str_date);
		return addMonth(date, amount);
	}

	
	public static String getToday(String mask) {
		return new SimpleDateFormat(mask).format(getToday().getTime());
	}

	
	public static Calendar getToday() {
		return Calendar.getInstance();
	}

	
	public static int getTodayYear() {
		return getToday().get(Calendar.YEAR);
	}

	
	public static int getTodayMonth() {
		return getToday().get(Calendar.MONTH);
	}

	public static int getTodayDay() {
		return getToday().get(Calendar.DAY_OF_MONTH);
	}

	
	public static int getTodayHour() {
		return getToday().get(Calendar.HOUR_OF_DAY);
	}

	
	public static int getTodayMinute() {
		return getToday().get(Calendar.MINUTE);
	}

	public static int getTodaySecond() {
		return getToday().get(Calendar.SECOND);
	}

	
	public static int getTodayWeek() {
		return getToday().get(Calendar.DAY_OF_WEEK);
	}

	
	public static int getDateElement(String mask, String str_date,
			int elementName) {

		Calendar cal = transStringToCalendar(mask, str_date);
		return cal.get(elementName);
	}

	
	public static int getDateElement(Date date, int elementName) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(elementName);
	}

	
	public static int compareDate(String str_date1, String str_date2,
			String mask) {
		Date date1 = transStringToDate(mask, str_date1);
		Date date2 = transStringToDate(mask, str_date2);
		return compareDate(date1, date2);
	}

	
	public static int compareDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		return cal1.compareTo(cal2);
	}

	
	public static int getInterval(String str_date1, String str_date2,
			String mask, int elementName) {
		Date date1 = transStringToDate(mask, str_date1);
		Date date2 = transStringToDate(mask, str_date2);
		return getInterval(date1, date2, elementName);
	}

	
	public static int getInterval(Date date1, Date date2, int elementName) {
		Calendar firstDate = Calendar.getInstance();
		firstDate.setTime(date1);
		Calendar secondDate = Calendar.getInstance();
		secondDate.setTime(date2);
		return firstDate.get(elementName) - secondDate.get(elementName);
	}
	
	
	public static void main(String s[]) {
	/*	Date today = new Date();
		long dd = System.currentTimeMillis();
		Calendar cal1 =transStringToCalendar(DateUtil.MASK_DATE, "01000102");
		
		Calendar cal =transStringToCalendar(DateUtil.MASK_DATE, "01000101");
		
		Calendar cal2 =transStringToCalendar(DateUtil.MASK_DATE, "10000102");
		//, Calendar.MILLISECOND) ;
	System.out.println((dd-cal.getTimeInMillis())/(1000*60*60*24));
	System.out.println((dd-cal1.getTimeInMillis())/(1000*60*60*24));
	System.out.println((dd-cal2.getTimeInMillis())/(1000*60*60*24));
	*/
	}
}
