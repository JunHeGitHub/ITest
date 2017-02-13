package com.zinglabs.util.commonTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.util.commonTask.imp.CommonTaskImp;

/**
 * 数据库数据导出线程
 * @author QCF
 *
 */
public class CommonTask implements Runnable{
	
	//运行标志
	public boolean runFlag=true;
	//运行实例
	public CommonTaskImp cti;
	//休眠时间
	public long sleepNum=60000;
	//运行次数,为0时一直运行
	public int runCount=0;
	//已执行次数
	public long runingCount=0;
	public static  Logger logger = LoggerFactory.getLogger("ZDesk"); 
	
	/**
	 * @param cti 接口实例
	 * @param sleepNum 挂起时间
	 * @param runCount 执行次数，为0时为一直运行。
	 */
	public CommonTask(CommonTaskImp cti,long sleepNum,int runCount){
		this.cti=cti;
		this.sleepNum=sleepNum;
		this.runCount=runCount;
	}
	
	public void run() {
		if(cti!=null){
			if(runCount==0){
				logger.debug(cti.getTaskMgs() + "..... START");
				while(runFlag){
					try{
						Thread.sleep(sleepNum);
						//logger.debug(cti.getTaskMgs() + "..... ");
						cti.doWork();
						///logger.debug(cti.getTaskMgs() + "..... end");
						runingCount++;
					}catch(Exception x){
						logger.error("commonTask : " + x.getMessage());
						setRunFlag(false);
					}
				}
			}else{
				for(int i=0;i<runCount;i++){
					try{
						Thread.sleep(sleepNum);
						cti.doWork();
						runingCount++;
					}catch(Exception x){
						logger.error("commonTask : " + x.getMessage());
						setRunFlag(false);
					}
				}
			}
		}else{
			logger.error("commonTask : 线程实例未设置。");
		}
		logger.debug(cti.getTaskMgs() + "..... STOP");
		setRunFlag(false);
	}
	/**
	 * 设置运行状态
	 * @param rs
	 */
	public void setRunFlag(boolean rs){
		this.runFlag=rs;
	}
}
