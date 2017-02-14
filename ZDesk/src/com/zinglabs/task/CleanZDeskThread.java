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

public class CleanZDeskThread extends BaseAbs implements Runnable {

	public CleanZDeskThread(){
		init(SKIP_Logger);
//		lock = new BusyFlag();
//		this.dao=new DAOTools();
	}
		
	public void run() {
		myThread=Thread.currentThread();
		long nowTime=System.currentTimeMillis();
		long sleepTime=0L;
		while (flag) {
			nowTime=System.currentTimeMillis();
			try {
				Thread.sleep(3000L);
			} catch (InterruptedException e) {
				error("CleanZDeskThread run");
				error(e);
			}
			info("CleanZDeskThread date check begin");
			try {
			    Timestamp timeNow=new Timestamp(System.currentTimeMillis());
			    
//			    2015年起6月份前3天进行数据清理
			    if(timeNow.getYear()> 2015 &&timeNow.getMonth()==6 && (timeNow.getDate() ==1||timeNow.getDate() ==2||timeNow.getDate() ==3)) {
			        
			    }
			} catch (Exception e) {
				error("in CleanZDeskThread");
				error(e);
			}
			info("CleanZDeskThread end");
			
//			try {
//				sleepTime=BaseData.QC_RECORD_INTERVAL - (System.currentTimeMillis() - nowTime);
//				LogUtil.warn("Thread SyncRecordThread sleep : "+sleepTime);
//				Thread.sleep(sleepTime>0?sleepTime:2000);
//			} catch (InterruptedException e) {
//				LogUtil.warn("exception in SyncRecordThread run");
//				LogUtil.warn(e.toString());
//			}
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
