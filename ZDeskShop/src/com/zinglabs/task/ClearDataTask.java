package com.zinglabs.task;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.base.Interface.BaseInterface;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;

public class ClearDataTask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    
		Thread t=null,t2=null;
		CleanZDeskThread clZDesk=null;
		CleanOnceThread clOnce=null;
		
		try {
			if(!DAOTools.hasInitStatic){
				DAOTools.initAllStatic();
				DAOTools.hasInitStatic=true;
			}
			
			clOnce =new CleanOnceThread();
            t2=new Thread(clOnce, "clean once thread "+(++taskStartTimes));
            t2.start();
			
//			ConfInfo.parseConfDB();
			while(true){
				LogUtil.info("TAG:11-22 syncTaskStartTimes:"+syncTaskStartTimes+" taskStartTimes:"+taskStartTimes,SKIP_Logger);
				if(t!=null&&t.isAlive()){
					
				}else if(ConfInfo.confMapDBConf.get("isCleanZDesk").equals("true")){
					if(clZDesk!=null){
//						check.releaseRES();
					    clZDesk=null;
					}else{
					    clZDesk=new CleanZDeskThread();
					}
					t=new Thread(clZDesk, "clean ZDesk thread "+(++taskStartTimes));
					t.start();
					int count=0;
					while(!t.isAlive()){
						try {
							Thread.sleep(BaseInterface.DEFULT_SLEEP_INTERVAL);
						} catch (InterruptedException e) {
							LogUtil.error(e,SKIP_Logger);
						}
						if(count++>20){
							LogUtil.error("waiting for clean ZDesk task thread start 20 times",SKIP_Logger);
							break;
						}
					}
					LogUtil.info(" clean ZDesk task thread starting ....",SKIP_Logger);
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
