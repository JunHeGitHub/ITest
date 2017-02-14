package com.zinglabs.task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.tools.CTIRequest;

public class AgentStatusThread extends BaseAbs implements Runnable {

	public AgentStatusThread(/*WriteDBThread dbThread*/) {
		init(SKIP_Logger);
//		try {
//			RTA_BEGIN = new String("__BEGIN__\n".getBytes(),BaseData.US_ASCII);
//			RTA_END = new String("__END__\n".getBytes(), BaseData.US_ASCII);
//			RTA_POSITION = new String("      ".getBytes(), BaseData.US_ASCII);
//			RTA_SEPARATOR = new String(" ".getBytes(), BaseData.US_ASCII);
//			RTA_SPLIT_GATE_OR_QUEUE = new String("      ".getBytes(),BaseData.US_ASCII);
//			RTA_REASON_CODE = new String("      ".getBytes(), BaseData.US_ASCII);
//			RTA_SPARE_SPACES = new String("          \n".getBytes(),BaseData.US_ASCII);
//			RTA_TIME_IN_STATE = new String("   10".getBytes(),BaseData.US_ASCII);
//		} catch (UnsupportedEncodingException e1) {
//			LogUtil.warn("exception in AgentStatusThread init");
//			LogUtil.warn(e1);
//		}
		
		
		z3000IP=ConfInfo.proerties.getProperty("z3000");
		extIP=ConfInfo.proerties.getProperty("z3000_ext");
		ctiReq=new CTIRequest(z3000IP);
		ctiReq.ip=z3000IP;
		this.dao=new DAOTools();
	}
	
	
	public void getDate(){
		String sql="select * from CTI_MONITOR_INFO";
		ResultSet res=null;
		
		
//		res = dao.execSelect(sql,"z3000");
		
		
		
		
		
	}
	
	public void run() {
		// TODO Auto-generated method stub
		long sleepTime=0L;
		ctiCommondTime=new Timestamp(System.currentTimeMillis());
		while (flag) {
			ctiCommondTime.setTime(System.currentTimeMillis());
//			nowTime=System.currentTimeMillis();
			this.ctiReq.getAllMonitorInfoToDB("10");
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				error("AgentStatusThread run");
				error(e);
			}
			info("AgentStatusThread begin");
			getDate();
			info("AgentStatusThread end");
			try {
				sleepTime=DEFULT_SLEEP_INTERVAL - (System.currentTimeMillis() - ctiCommondTime.getTime());
				info("Thread AgentStatusThread sleep : "+sleepTime);
				Thread.sleep(sleepTime>0L?sleepTime:1000L);
			} catch (InterruptedException e) {
				error("sleep death AgentStatusThread run");
				error(e);
			}
		}
	}
	
	private CTIRequest ctiReq; 
	
	public static String z3000IP = "192.168.0.167";
	public static String extIP = "192.168.0.167";
	
	private Timestamp ctiCommondTime =null;
	
	public DAOTools dao;
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	public volatile boolean flag = true;

}
