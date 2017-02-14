package com.zinglabs.db;

import com.zinglabs.tools.RandomGUID;


/**
 * 
 * 
 * 
 * 通过数据库 标识方式 实现分布式锁  
 * 
 * spring jta等 过于复杂。
 * 
 * 
 *
 */
public class DBSyncLock {
	
	public String lockId;
	
	public String selfId;
	
	public DBSyncLock(String lockId){
		this.lockId=lockId;
		this.selfId=new RandomGUID().toString();
	}
	
	public boolean check(){
		String sql="select selfId from lockInfo where lockId='"+this.lockId+"'";
		
		return false;
	}
	
	
	public void lock(){
		String sql="update lockInfo set selfId='"+selfId+"' where lockId='"+this.lockId+"' and selfId=''  ";
		
	}
	
	public void free(){
		String sql="update lockInfo set selfId='"+selfId+"' where lockId='"+this.lockId+"' and selfId=''  ";
		
	}

}
