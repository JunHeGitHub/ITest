package com.zinglabs.db;
import java.sql.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DAOBatchUpdate {
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	private Connection con;
	
	public DAOBatchUpdate(String dbid) throws Exception{
		con=DAOTools.getConnectionOutS(dbid);
		con.setAutoCommit(false);
	}
	
	DAOBatchUpdate(Connection con) throws Exception{
		this.con=con;
		this.con.setAutoCommit(false);
	}
	
	public void singUpdate(String sql) throws Exception{
		PreparedStatement ps=con.prepareStatement(sql);
		ps.executeUpdate();
	}
	
	public void singUpdate(String sql,boolean commit) throws Exception{
		try{
			singUpdate(sql);
		}catch(Exception x){
			rollBack();
			throw x;
		}
		if(commit){
			commit();
		}
	}

	public void singUpdate(String sql,boolean commit,boolean isClose) throws Exception{
		try{
			singUpdate(sql,commit);
		}catch(Exception x){
			throw x;
		}finally{
			if(isClose){
				close();
			}
		}
	}
	
	public void singUpdate(String sql,String [] param) throws Exception{
		PreparedStatement ps=con.prepareStatement(sql);
		ps=spellPreparedStatement(ps,param);
		ps.executeUpdate();
	}
	
	public void singUpdate(String sql, String [] param,boolean commit) throws Exception{
		try{
			singUpdate(sql,param);
		}catch(Exception x){
			rollBack();
			throw x;
		}
		if(commit){
			commit();
		}
	}

	public void singUpdate(String sql,String [] param,boolean commit,boolean isClose) throws Exception{
		try{
			singUpdate(sql,param,commit);
		}catch(Exception x){
			throw x;
		}finally{
			if(isClose){
				close();
			}
		}
	}
	
	public PreparedStatement spellPreparedStatement(PreparedStatement ps,String [] parms) throws Exception{
		if(ps!=null && parms!=null && parms.length>0){
			for(int i=0;i<parms.length;i++){
				ps.setString(i+1, parms[i]);
			}
		}
		return ps;
	}
	
	public void batchUpdate(String sql,String[][] parms) throws Exception{
		if(sql!=null && sql.length()>0){
			if(parms!=null && parms.length>0){
				for(String [] pm:parms){
					singUpdate(sql,pm);
				}
			}else{
				singUpdate(sql);
			}
		}
	}
	
	public void batchUpdate(String sql,String[][] parms,boolean commit) throws Exception{
		try{
			batchUpdate(sql,parms);
		}catch(Exception x){
			rollBack();
			throw x;
		}
		if(commit){
			commit();
		}
	}
	
	public void batchUpdate(String sql,String[][] parms,boolean commit,boolean isClose) throws Exception{
		try{
			batchUpdate(sql,parms,commit);
		}catch(Exception x){
			throw x;
		}finally{
			if(isClose){
				close();
			}
		}
	}
	
	public void batchUpdate(String sql,List<String[]> parms) throws Exception{
		if(sql!=null && sql.length()>0){
			if(parms!=null && parms.size()>0){
				for(String [] pm:parms){
					singUpdate(sql,pm);
				}
			}else{
				singUpdate(sql);
			}
		}
	}
	
	public void batchUpdate(String sql,List<String[]> parms,boolean commit) throws Exception{
		try{
			batchUpdate(sql,parms);
		}catch(Exception x){
			rollBack();
			throw x;
		}
		if(commit){
			commit();
		}
	}
	
	public void batchUpdate(String sql,List<String[]> parms,boolean commit,boolean isClose) throws Exception{
		try{
			batchUpdate(sql,parms,commit);
		}catch(Exception x){
			throw x;
		}finally{
			if(isClose){
				close();
			}
		}
	}
	
	public void commit()throws Exception{
		this.con.commit();
	}
	
	public void commit(boolean isClose)throws Exception{
		this.con.commit();
		if(isClose){
			this.close();
		}
	}
	
	public void close() throws Exception{
		this.con.setAutoCommit(true);
		this.con.close();
	}
	
	public void rollBack()throws Exception{
		this.con.rollback();
	}
	
	public static void main(String[] args) throws Exception{
//		String sql="insert into `zkmInfoSecurity` (`infoId`) values ('1111111')";
//		String sql1="insert into `zkmInfoSecurity` (`infoId`) values ('222222')";
//		DAOTools dts=new DAOTools();
//		String dbid=ConfInfo.securityDBId;
//		Thread.sleep(2000);
//		DAOBatchUpdate dbu=new DAOBatchUpdate(dbid);
//		dbu.singUpdate(sql);
//		dbu.singUpdate(sql1);
//		dbu.rollBack();
//		dbu.close();
	}
}
