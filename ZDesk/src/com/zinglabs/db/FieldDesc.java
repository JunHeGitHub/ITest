package com.zinglabs.db;

import java.sql.Types;
import java.text.SimpleDateFormat;

/**
 * 
 * @author chuan.li
 * @desc every record in db must has time
 */
public class FieldDesc {
	public String type;  // db type char etc
	public String javaType;  // java type java.unit.String etc
	public SimpleDateFormat format;
	public String formatStr;
	public String dbid;
	public String dbName;
	public String tableName;
	public String fieldName;
	public boolean isPK;  // use for update & replace 
//	public boolean isDeleteKey; // time field map to baseData like agentHalfHour  skillHalfHour
	public int dbKeyType;  // use 4bit Hex value 1111 to decide if field is insert del update select key
	
	public String value;
	public String maxLength;
	public int  scale;
	public String baseCol;
	public TableDesc myTable;
	public String directName;
   
//	public void fieldToString(){
//		System.out.println("[[ type:"+type +"\n"+
//				" javaType:"+javaType +"\n"+
//				" format:"+format+"\n"+
//				" dbid:"+dbid+"\n"+
//				" dbName:"+dbName+"\n"+
//				" tableName:"+tableName+"\n"+
//				" fieldName:"+fieldName+"\n"+
//				" isPK:"+isPK +"\n"+
//				" dbKeyType:"+dbKeyType +"\n"+
//				" value:"+value+"\n"+
//				" maxLength:"+maxLength+"\n"+
//				" scale:"+scale+"\n"+
//				" baseCol:"+baseCol+"\n"+
//				" myTable:"+myTable+"\n"+
//				" directName:"+directName+"]]");
//	}
}
