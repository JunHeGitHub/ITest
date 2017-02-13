package com.testing.tools.TestAmoebaTask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.zinglabs.util.commonTask.imp.CommonTaskImp;

public class RTask implements CommonTaskImp{
	public Connection rcon;
	public int count=0;
	String rsql="select count(1) from mytest";
	
	public void doWork() {
		try{
//			count++;
//			PreparedStatement ps =rcon.prepareStatement(rsql);
//			ResultSet rs=ps.executeQuery();
//			if(rs.next()){
//				System.out.println(rs.getString(1));
//			}
			System.out.println("你好");
		}catch(Exception x){
			x.printStackTrace();
			System.out.println(x.getMessage());
		}
	}

	public String getTaskMgs() {
		return "read task ";
	}
	
}