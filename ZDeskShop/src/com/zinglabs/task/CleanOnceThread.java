package com.zinglabs.task;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.base.absImpl.BaseAbs;

import com.zinglabs.log.LogUtil;

public class CleanOnceThread extends BaseAbs implements Runnable {

	public CleanOnceThread(){
		init(SKIP_Logger);
//		lock = new BusyFlag();
//		this.dao=new DAOTools();
	}
		
	public void run() {
		myThread=Thread.currentThread();
		long nowTime=System.currentTimeMillis();
		long sleepTime=0L;
		
		
		try
        {
		    info("CleanOnceThread date check begin");
		    
        }
        catch (Exception e)
        {
            LogUtil.error(e, SKIP_Logger);
        }catch (Error e) {
            LogUtil.error(e.getMessage(), SKIP_Logger);
        }
		
	}
	
	
	public void cleanAndStop(){
		this.myThread=null;
		this.flag=false;
	}
	
	public static void main(String[] args) {
		long time=System.currentTimeMillis();
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(time);
		System.out.println(cal.getActualMaximum(Calendar.DAY_OF_WEEK)+"------------"+cal.get(Calendar.DAY_OF_WEEK));
		System.out.println(cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"------------"+cal.get(Calendar.DAY_OF_MONTH));
//	return cal.getActualMaximum(Calendar.DAY_OF_MONTH)==cal.get(Calendar.DAY_OF_MONTH);
	}
	public volatile boolean isRelease=false;
	public volatile boolean flag = true;
	public volatile boolean hasWeekCal = false;
	public volatile boolean hasMonthCal = false;
//	public  BusyFlag lockx;
	public Thread myThread=null;
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
//	public DAOTools dao;
}
