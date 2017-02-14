package com.zinglabs.tools.zkmDataTools;

import java.util.*;
import java.sql.*;
public class SyncZKMTree extends ToolsBase{

	public String[] source={"com.mysql.jdbc.Driver","jdbc:mysql://128.128.128.128:3306/securityDB?user=zinglabs&password=12&useUnicode=true&characterEncoding=utf8","zinglabs","12"};
	public String[] dist={"com.mysql.jdbc.Driver","jdbc:mysql://22.8.81.196:3306/securityDB?user=zinglabs&password=12&useUnicode=true&characterEncoding=utf8","zinglabs","12"};
	
	public String[] insertField={"id","text","iconCls","cls","leaf","recordType","parentId","sortPath","filePath","createUser","createTime"};
	
	public List getTreeData(String recordType) throws Exception{
		Connection con=getConnection(source[0], source[1], source[2], source[3]);
		String sql="select * from zkmInfoBase where recordType='" +  recordType +"' and isdel <> 1";
		List list=queryMap(sql,null,con);
		return list;
	}
	
	public void putTreeData(List list) throws Exception{
		Connection con=getConnection(dist[0], dist[1], dist[2], dist[3]);
		String w="?,?,?,?,?,?,?,?,?,?,?";
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				String[] v=new String[insertField.length];
				String f="";
				int j=0;
				for(String fd:insertField){
					String tv=(String)map.get(fd);
					if(tv==null || tv.length()<=0){
						tv="";
					}
					v[j]=tv;
					if(j+1 < insertField.length){
						f +=fd + ",";
					}else{
						f+=fd;
					}
					j++;
				}
				String sql="insert into zkmInfoBase (" + f  +") values ("+ w +")";
				updateForPrepared(sql,v,con);
			}
		}
		if(con!=null){
			con.close();
		}
	}
	
	public void syncDType() throws Exception{
		List list=getTreeData("d");
		putTreeData(list);
	}
	
	public void syncDVType() throws Exception{
		List list=getTreeData("dv");
		putTreeData(list);
	}
	
	public void syncDFAQType() throws Exception{
		List list=getTreeData("dfaq");
		putTreeData(list);
	}
	
	public static void main(String[] args) throws Exception{
		SyncZKMTree szt=new SyncZKMTree();
		szt.syncDType();
		//szt.syncDVType();
		//szt.syncDFAQType();
	}

}
