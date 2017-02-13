package com.testing.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.testing.tools.TestAmoebaTask.RTask;
import com.testing.tools.TestAmoebaTask.WTask;
import com.zinglabs.db.DAOTools;
import com.zinglabs.util.commonTask.CommonTask;

import java.sql.*;

public class TestAmoeba {

	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	
	public static void main(String[] args) throws Exception{
		String dbType="myTest";
		if(!DAOTools.hasInitStatic){
			DAOTools.initAllStatic();
			DAOTools.hasInitStatic=true;
		}
		
		
		Connection conn=DAOTools.getConnectionOutS(dbType);
		RTask rTask=new RTask();
		rTask.rcon=conn;
		CommonTask ctr=new CommonTask(rTask,100,0);
		Thread th=new Thread(ctr);
		th.start();
		
		WTask wTask=new WTask();
		Connection conn1=DAOTools.getConnectionOutS(dbType);
		wTask.wcon=conn1;
		CommonTask ctw=new CommonTask(wTask,100,0);
		Thread th1=new Thread(ctw);
		th1.start();
	}

}