package com.zinglabs.task;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.base.Interface.BaseInterface;
import com.zinglabs.db.AgentInfo;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.db.SkillInfo;
import com.zinglabs.log.LogUtil;

public class MainThread {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		LogUtil.init();
		Thread t=null,t2=null,t3=null,t4=null;
		AgentCheckThread check=null;
		SyncRecordThread sync=null;
		CalculateThread cal=null;
		try {
			if(!DAOTools.hasInitStatic){
				DAOTools.initAllStatic();
				DAOTools.hasInitStatic=true;
				
			}
			AgentInfo.init();
			SkillInfo.init();
//			ConfInfo.parseConfDB();
			while(true){
				LogUtil.info("TAG:renshou 04-30 11:21 call_type  boc ZQC report check thread  syncTaskStartTimes:"+syncTaskStartTimes+" taskStartTimes:"+taskStartTimes,SKIP_Logger);
				if(t!=null&&t.isAlive()){
					
				}else if(ConfInfo.confMapDBConf.get("isCheckZQCAgent").equals("true")){
					if(check!=null){
//						check.releaseRES();
						check=null;
					}else{
						
						check=new AgentCheckThread(new Timestamp(System.currentTimeMillis()));
						check.syncAvailableAgentData();
					}
					t=new Thread(check, "check thread "+(++taskStartTimes));
					t.start();
					int count=0;
					while(!t.isAlive()){
						try {
							Thread.sleep(BaseInterface.DEFULT_SLEEP_INTERVAL);
						} catch (InterruptedException e) {
							LogUtil.error(e,SKIP_Logger);
						}
						if(count++>20){
							LogUtil.error("waiting for task thread start 20 times",SKIP_Logger);
							break;
						}
					}	
					
					LogUtil.info(" task thread starting ....",SKIP_Logger);
				}
				
				
				if(t3!=null&&t3.isAlive()){
					
				}else{
					if(sync!=null){
						sync=null;
					}else if(ConfInfo.confMapDBConf.get("isSyncZQCRecord").equals("true")){
						sync=new SyncRecordThread();
						t3=new Thread(sync, "sync task thread "+(++syncTaskStartTimes));
						t3.start();
						int count=0;
						while(!t3.isAlive()){
							try {
								Thread.sleep(BaseInterface.DEFULT_SLEEP_INTERVAL);
							} catch (InterruptedException e) {
								LogUtil.error(e,SKIP_Logger);
							}
							if(count++>20){
								LogUtil.error("waiting for sync task thread start 20 times",SKIP_Logger);
								break;
							}
						}	
						
						LogUtil.info(" sync task thread starting ....",SKIP_Logger);
					}
				}
				
				
				if(t4!=null&&t4.isAlive()){
					
				}else if(ConfInfo.confMapDBConf.get("isCalZQCReport").equals("true")){
					if(cal!=null){
						cal=null;
					}else{
						cal=new CalculateThread();
						t4=new Thread(cal, "cal task thread "+(++calTaskStartTimes));
						t4.start();
						int count=0;
						while(!t4.isAlive()){
							try {
								Thread.sleep(BaseInterface.DEFULT_SLEEP_INTERVAL);
							} catch (InterruptedException e) {
								LogUtil.error(e,SKIP_Logger);
							}
							if(count++>20){
								LogUtil.error("waiting for cal task thread start 20 times",SKIP_Logger);
								break;
							}
						}
						
						LogUtil.info(" cal task thread starting ....",SKIP_Logger);
					}
				}
				
				try {
					Thread.sleep(20000L);
				} catch (InterruptedException e) {
					LogUtil.error(e,SKIP_Logger);
				}
				
				
			}
		} catch (Exception e) {
			LogUtil.error(e, SKIP_Logger);
		}
		
	}
	public static int calTaskStartTimes=0;
	public static int syncTaskStartTimes=0;
	public static int taskStartTimes=0;
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
}
