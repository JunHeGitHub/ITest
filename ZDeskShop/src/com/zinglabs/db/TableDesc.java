package com.zinglabs.db;

import java.util.ArrayList;
import java.util.HashMap;

public class TableDesc {
	
	public TableDesc() throws Exception{
        SSCDBTools.defultValue(this);
	}
	
	public String dbo;
	public String dbid;
	public String tableName;
	public boolean needUpdate;
	public ArrayList<FieldDesc> fields;
	public int timeGranularity;
	public String type;
	public String style;
	public HashMap<String,String> filters;
	public String nameRule;
}
