package com.zinglabs.task;

import com.zinglabs.base.absImpl.BaseAbs;

public class AgentTask extends BaseAbs implements Runnable{

	public void run() {
		long time=0L,sleepValue=0L,toXmlTime=0L;
		
		while (flag) {
			time=System.currentTimeMillis();
			try {
				getData();
				debug((System.currentTimeMillis()-time)+"getData end ");
			} catch (Exception e) {
				error(" AgentTask Exc ");
				error(e);
			}
			
			sleepValue=DEFULT_SLEEP_INTERVAL-(System.currentTimeMillis()-time);
			debug("sleepValue:"+sleepValue);
			try {
				Thread.sleep(sleepValue>0L?sleepValue:1000L);
			} catch (InterruptedException e) {
				error(e);
			}
		}
	}
	
	
	public void getData(){
		
	}
	
	public volatile boolean flag;
}
