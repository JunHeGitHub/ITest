package com.testing.tools.TestAmoebaTask;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.zinglabs.util.RandomGUID;
import com.zinglabs.util.commonTask.imp.CommonTaskImp;

public class WTask implements CommonTaskImp{
	public Connection wcon;
	public int count=0;
	String wsql="insert mytest values(?,?,?)";
	
	public void doWork() {
		count++;
		RandomGUID rg=new RandomGUID();
		String age=String.valueOf(count);
		String name="ab" + count;
		try{
			PreparedStatement ps =wcon.prepareStatement(wsql);
			ps.setString(1, rg.toString());
			ps.setString(2, name);
			ps.setString(3, age);
			ps.executeUpdate();
		}catch(Exception x){
			x.printStackTrace();
			System.out.println(x.getMessage());
		}
	}

	public String getTaskMgs() {
		return "write task ";
	}
}