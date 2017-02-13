package com.zinglabs.task;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.base.Interface.BaseInterface;
import com.zinglabs.db.AgentInfo;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.db.SkillInfo;
import com.zinglabs.db.SumInfo;
import com.zinglabs.log.LogUtil;

public class MQSync {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Thread t=null;
		MQGetThread mqget=null;
		try {
			if(!DAOTools.hasInitStatic){
				DAOTools.initAllStatic();
				DAOTools.hasInitStatic=true;
			}
			while(true){
				LogUtil.info("TAG:MQSync ",SKIP_Logger);
				if(t!=null&&t.isAlive()){
					
				}else{
					if(mqget!=null){
					    mqget=null;
					}else{
					    mqget=new MQGetThread();
						t=new Thread(mqget, "MQSync task thread "+(++syncTaskStartTimes));
						t.start();
						int count=0;
						while(!t.isAlive()){
							try {
								Thread.sleep(180000L);
							} catch (InterruptedException e) {
								LogUtil.error(e,SKIP_Logger);
							}
							if(count++>20){
								LogUtil.error("waiting for MQSync task thread start 20 times",SKIP_Logger);
								break;
							}
						}	
						
						LogUtil.info(" MQSync task thread starting ....",SKIP_Logger);
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
