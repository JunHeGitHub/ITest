package com.testing.mysqlMemory;

import com.zinglabs.base.Interface.CallBackInterface;
import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.db.CallBackParam;
import com.zinglabs.db.DAOTools;
import com.zinglabs.tools.RandomGUID;
import com.zinglabs.tools.Utility;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestReadTask extends BaseAbs implements Runnable{

	public DAOTools dt;
	
	public String pid;
	
	public Connection con;
	
	public String dbType;
	
	public static volatile int counter=0;
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
	public void loadRow(CallBackParam param){
		
	}
	
	public void run(){
		init(SKIP_Logger);
		
		ResultSet rs=null;
		
		CallBackDB callBack =new CallBackDB();
		Class fieldC=null;
		Method m=null;
		String scopeTime=null,hourTime=null;
		String[] keyTmp=null;
		ArrayList<Object[]> al=null;
		Object[] resTmp=null;
		try {
			fieldC = Class.forName("com.testing.mysqlMemory.TestReadTask");
			m=fieldC.getMethod("loadRow", CallBackParam.class);
//			m=fieldC.getMethod("loadTest", CallBackParam.class);
		} catch (Exception e1) {
			error("loadDataDB getMethod");
			error(e1);
		}

		callBack.setM(m, this);
		
		try {
			while(ShareConf.allRun){
				if(ShareConf.readFlag){
					long startTime=System.currentTimeMillis();
					//RandomGUID rg=new RandomGUID();
					String sql="select * from LOG_AGNET_DETAIL_MEMORY  limit 1000";
					//System.out.println(sql);
//					rs=dt.execSelectR(sql, ShareConf.confDBName);

					
					ArrayList list=null;
					try {
//						list=dt.execSelect(sql, con);
						dt.execSelectCallBack(sql, ShareConf.confDBName,callBack,CALL_BACK_SQL_TYPE_STREAM,con);
//						dt.execSelectCallBack(sql, ShareConf.confDBName,callBack,CALL_BACK_SQL_TYPE_STREAM);
					} catch (Exception e) {
						debug("---- error : " + e.getMessage());
                        DAOTools.releaseConnectionOutS(dbType,con);
						break;
					}
					
					long endTime=System.currentTimeMillis();
					long x=endTime-startTime;
					if(ShareConf.readMax<x){
						ShareConf.readMax=x;
					}
//					debug(" ---------- RPID : " + pid +  " runTime : " + (x) + " DataSize: " + list.size() + " readMax: " + ShareConf.readMax);
					debug(" ---------- RPID : " + pid +  " runTime : " + (x) +  " readMax: " + ShareConf.readMax +  
							" queryuMax: " + ShareConf.queryuMax +
							" borowMax: " + ShareConf.borowMax);
					
//					if(counter++>1000){
//						try {
//							Utility.gcTimes(3, 3000L);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						counter=0;
//					}
				}
				
				Thread.sleep(ShareConf.readSleep);
			}
		} catch (InterruptedException e) {
			System.out.println("read : " + pid + "--error :  break;");
			e.printStackTrace();
		}finally{
            DAOTools.releaseConnectionOutS(dbType,con);
		}
	}
	
	class CallBackDB implements CallBackInterface{
		public void execute(CallBackParam param){
//			debug("aaaaaaaaaaaaaa");
//			Object[] oA=new Object[1];
//			oA[0]=param;
			switch (param.type) {
			case CALL_BACK_TYPE_DB_RES:
				try {
						m.invoke(o,param);
					} catch (Exception e) {
						error("CallBackDB execute invoke");
						error(e);
					}
//				loadRow(param);
				break;

			default:
				break;
			}
		}
		
		public void setM(Method m1,Object o1){
			m=m1;
			o=o1;
		}
		Object o;
		Method m;
		public void executeEnd(CallBackParam param) {
			// TODO Auto-generated method stub
			
		}

		public void setCriteria(String name, Object value) {
			// TODO Auto-generated method stub
			
		}
	}

	
	public static void main(String [] args) throws Exception{
		TestReadTask trt=new TestReadTask();
		trt.pid="rrr";
		trt.dbType=ShareConf.confDBName;
		trt.dt=new DAOTools();
		trt.con=DAOTools.getConnectionOutS(trt.dbType);
		Thread trd=new Thread(trt);
		trd.start();
		ShareConf.readFlag=true;
	}
	
	
}
