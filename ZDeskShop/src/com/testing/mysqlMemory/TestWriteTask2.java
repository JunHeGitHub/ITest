package com.testing.mysqlMemory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.db.DAOTools;
import com.zinglabs.tools.RandomGUID;
import com.zinglabs.tools.Utility;

public class TestWriteTask2 extends BaseAbs implements Runnable{
	
	public TestWriteTask2(){
		
		init(SKIP_Logger);
		alSkill.add("JISHU");
		alSkill.add("XIAOSHOU");
		alSkill.add("KEFU");
		
		alState.add("在线");
		alState.add("休息");
		alState.add("事后整理");
		alState.add("离线");
		alState.add("双方通话");
		alState.add("会议");
		
		defultInteger=new HashMap<String, ArrayList<Integer>>();
		
		dt=new DAOTools();
		try {
			con=DAOTools.getConnectionOutS(ShareConf.confDBName);
		} catch (SQLException e) {
			error(e);
		}
		
	}
	
	public DAOTools dt;
	
	public String pid;
	
	public Connection con;
	
	public String dbType;
	
	public static Integer num=0;;
	
	public static volatile int counter=0;
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
	public static synchronized int getNum_(){
		int t0=num;
		num++;
		return t0;
	}
	
	public int count=0;
	
	public void run() {
		init(SKIP_Logger);
		try {
			while(ShareConf.allRun){
				if(ShareConf.writeFlag){
					long startTime=System.currentTimeMillis();
					int tn=getNum_();
//					String sql="delete from SU_QC_SOURCE_RECORD_DATA";
//					dt.execUpdate(sql, ShareConf.confDBName);
					String sql="insert into SU_QC_SOURCE_RECORD_DATA " +
					"(" +
					"begin_time," +
					"end_time," +
					"caller_number," +
					"call_number" +
					")" +
					"values " +
					"('2011-01-01 00:00:00'," +
					"'2011-01-01 00:10:00'," +
					"'" + Utility.getRandomInt(++count) + "'," +
					"'" + Utility.getRandomInt(++count) + "'" +
					")";
					dt.execUpdate(sql, ShareConf.confDBName);
					count=count>1000?0:count;
					
					
					System.out.println(sql);
//					try {
//						dt.execUpdate(sql, con);
//					} catch (Exception e) {
//						debug("------error : "  + e.getMessage());
//						DAOTools.releaseConnectionOutS(ShareConf.confDBName,con);
//						break;
//					}
//					
					
					long endTime=System.currentTimeMillis();
					long x=endTime-startTime;
//					if(ShareConf.writeMax<x){
//						ShareConf.writeMax=x;
//					}
//					debug(" ---------- WPID : " + pid +  " runTime : " + x + " writeMax: " + ShareConf.writeMax);
					
					
//					if(counter++>1000){
//						try {
//							Utility.gcTimes(5, 100L);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						counter=0;
//					}
				}
				Thread.sleep(ShareConf.writeSleep);
			}
		}catch (Exception e1){
			error(e1);
		}
		finally{
            DAOTools.releaseConnectionOutS(ShareConf.confDBName,con);
		}
	}
	
	public static void main(String [] args) throws Exception{
		TestWriteTask2 twt=new TestWriteTask2();
		twt.pid="1111";
		twt.num=1;
		twt.dbType=ShareConf.confDBName;
		twt.dt=new DAOTools();
		twt.con=DAOTools.getConnectionOutS(twt.dbType);
		Thread trd=new Thread(twt);
		trd.start();
		ShareConf.writeFlag=true;
	}
	
	
	
	public int rand(int b, int e) {
		return (int) (Math.random() * (e - b + 1) + b);
	}
	
	public HashMap<String, ArrayList<String>> defultStr;
	public HashMap<String, ArrayList<Integer>> defultInteger;
	
	
	ArrayList<String> alSkill=new ArrayList<String>();
	
	ArrayList<String> alState=new ArrayList<String>();

}

