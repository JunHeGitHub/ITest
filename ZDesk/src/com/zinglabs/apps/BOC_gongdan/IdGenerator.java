package com.zinglabs.apps.BOC_gongdan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.util.RandomGUID;

public class IdGenerator {
	public static Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk");

	private volatile static IdGenerator instance = null;
	private static BOCgongdanService idservice = null;

	public static BOCgongdanService getIdservice(){
		if (idservice == null) {
			idservice = new BOCgongdanService();
		}
		return idservice;
	}

	public static IdGenerator getInstance() {
		if (instance == null) {
			synchronized (BOCgongdanService.class) {
				if (instance == null) {
					instance = new IdGenerator();
				}
			}
		}
		return instance;

	}
	//有锁
	public static synchronized Map<String, String>  makeWorkOrder(Map<String, String> map){
		List<Map<String, String>> list = null ;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date() ;
        String dateStr = df.format(date) ;
        Map<String, String> interDate = new HashMap<String, String>() ;
        interDate.put("interDate", dfs.format(date)) ;
		list = getIdservice().selectMaxId(interDate) ;
		String gongdanId = "";
		if(list.size()!=0&&(String) list.get(0).get("gongdanId")!=null){
			long Id = Long.parseLong((String)list.get(0).get("gongdanId")) ;
			Id++ ;
			gongdanId = String.valueOf(Id) ;
		}else{
	        gongdanId= String.valueOf(dateStr+"0001") ;
		}
		if(map.get("customerCallTime")==null||!(isValidDate(map.get("customerCallTime")))){
			map.put("customerCallTime",map.get("chuangjianTime").toString()) ;
		}
		if(map.get("country")==null||map.get("country").toString().length()==0){
			map.put("country", "中国") ;
		}
		String uuid = "" ;
		uuid = new RandomGUID().toString() ;
		map.put("id",uuid ) ;
		map.put("gongdanId", gongdanId) ;
		SKIP_Logger.debug("IdGenerator_getId_create workOrderId &&& uuid &&&  time :"+gongdanId+"&&&"+ uuid) ;
		getIdservice().act_insert(map) ;
		return  map;
	}
	

	static int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	/**
	 * @param date yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static boolean isValidDate(String date) {
		try {
			int year = Integer.parseInt(date.substring(0, 4));
			if (year <= 0)
				return false;
			int month = Integer.parseInt(date.substring(5, 7));
			if (month <= 0 || month > 12)
				return false;
			int day = Integer.parseInt(date.substring(8, 10));
			if (day <= 0 || day > DAYS[month])
				return false;
			if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {
				return false;
			}
			int hour = Integer.parseInt(date.substring(11, 13));
			if (hour < 0 || hour > 23)
				return false;
			int minute = Integer.parseInt(date.substring(14, 16));
			if (minute < 0 || minute > 59)
				return false;
			int second = Integer.parseInt(date.substring(17, 19));
			if (second < 0 || second > 59)
				return false;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static final boolean isGregorianLeapYear(int year) {
		return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
	}
	public static void main(String[] args) {
//		System.out.println(isValidDate("2100-02-27 23:00:01"));
		System.out.println((new Date()).getTime() ) ;
	}

}
