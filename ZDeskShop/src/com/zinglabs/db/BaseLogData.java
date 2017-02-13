package com.zinglabs.db;

import java.sql.*;
import java.util.*;

public abstract class BaseLogData {
	public String SKIP_selectSql;
	
	public String SKIP_deleteSql;
	
	public ArrayList SKIP_retValue;
	
	public String SKIP_dbName; // map to dbid  use for db connection
	
	public String SKIP_tableName;
	
	
	public String getDBName(){
		return this.SKIP_dbName;
	}
	public void setDBName(String SKIP_dbName){
		this.SKIP_dbName=SKIP_dbName;
	}
	
	public String getSelectSql(){
		return this.SKIP_selectSql;
	}
	public void setSuperSelectSql(String sql){
		this.SKIP_selectSql=sql;
	}
	
	public String getDeleteSql(){
		return this.SKIP_deleteSql;
	}
	
	public void setDeleteSql(String sql){
		this.SKIP_deleteSql=sql;
	}
	
	public void  addRetValue(Object o){
		SKIP_retValue.add(o);
	}
	
	public Object getNewObj() throws Exception{
		return this.getClass().newInstance();
	}
	
	
//	public int loopRes(){return -1;}
}
