package com.zinglabs.db;

import java.util.HashMap;

public class DBDesc {
	
	public DBDesc() throws Exception{
        SSCDBTools.defultValue(this);
	}

	public String dbName;
//	public HashMap<String, TableDesc> tableMap;
	public HashMap<String, HashMap<String, TableDesc>> tableMap; // table with granularity first increase performance
	
	public HashMap<String, TableDesc> realTimeTableMap;
}
