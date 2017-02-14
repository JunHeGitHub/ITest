package com.zinglabs.tools.zkmDataTools;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.log.LogUtil;
import com.zinglabs.util.DateUtil;


public class ToolsBase {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM"); 
	/**
	 * 获取数据库连接
	 * @param className JDBC实现类
	 * @param url URL
	 * @param user 用户名
	 * @param pwd 密码
	 * @return Connection
	 * @throws Exception
	 */
	public static Connection getConnection(String className,String url,String user,String pwd) throws Exception{
		Connection con=null;
		Class.forName(className);
		con=DriverManager.getConnection(url, user, pwd);
		return con;
	}
	
	/**
	 * 
	 * @param conStr {className,url,user,pwd}
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection(String[] conStr) throws Exception{
		Connection con=null;
		Class.forName(conStr[0]);
		con=DriverManager.getConnection(conStr[1], conStr[2], conStr[3]);
		return con;
	}
	
	public static void closeConnection(Connection con) throws Exception{
		if(con!=null){
			con.close();
		}
	}
	
	public static List<Map> queryMap(String sql,String [] params,Connection conn) throws Exception {
		List<Map> relist=null;
		PreparedStatement stmt=null;
		ResultSet res = null;
		if(sql!=null && !"".equals(sql) && conn!=null){
			try{
				relist=new ArrayList();
				stmt=conn.prepareStatement(sql.toString());
				String pp="";
				if(params!=null && params.length>0){
					for(int i=0;i<params.length;i++){
						stmt.setString(i+1, params[i]);
						pp += params[i]+ ",";
					}
				}
				
				LogUtil.debug("queryMap sql:"+sql.toString() + " param : " + pp,SKIP_Logger);
				
				res=stmt.executeQuery();
				while(res.next()){
					HashMap map=new HashMap();
					int colNum=res.getMetaData().getColumnCount();
					for(int i=0;i<colNum;i++){
						String name=res.getMetaData().getColumnName(i+1);
						String ctype=res.getMetaData().getColumnTypeName(i+1);
						String colvalue="";
						if("TIMESTAMP".equals(ctype)){
							try{
								colvalue=DateUtil.convertTimeSampToString(res.getTimestamp(i+1));
							}catch(Exception x){
								colvalue=res.getString(i+1);
							}
						}else{
							colvalue=res.getString(i+1);
						}
						map.put(name, colvalue);
					}
					relist.add(map);
				}
			}catch(Exception x){
				LogUtil.error(x, SKIP_Logger);
			}finally{
				if(res!=null){
					res.close();
				}
				if(stmt!=null){
					stmt.close();
				}
				if(conn!=null){
					conn.close();
				}
			}
		}
		return relist;
	}
	
	public static void updateForPrepared(String sql,String[] param,Connection conn) throws Exception{
		PreparedStatement stmt=null;
		if(sql!=null && !"".equals(sql) && conn!=null ){
			try{
				stmt=conn.prepareStatement(sql.toString());
				String pp="";
				if(param!=null && param.length>0){
					for(int i=0;i<param.length;i++){
						stmt.setString(i+1, param[i]);
						pp += param[i]+ ",";
					}
				}
				LogUtil.debug("queryMap sql:"+sql.toString() + " param : " + pp,SKIP_Logger);
				stmt.executeUpdate();
			}catch(Exception x){
				LogUtil.error(x, SKIP_Logger);
			}finally{
				if(stmt!=null){
					stmt.close();
				}
			}
		}
	}
	
	public static String getResultSetStringValue(ResultSet rs,String key){
		String str="";
		if(rs!=null){
			try{
				str=rs.getString(key)==null?"":rs.getString(key);
			}catch(Exception x){
				x.printStackTrace();
			}
		}
		return str;
	}
	
	public static void main(String [] args) throws Exception{
		String[] constrs={"com.mysql.jdbc.Driver","jdbc:mysql://128.128.128.128:3306/openfire?user=zinglabs&password=12&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=round&useOldAliasMetadataBehavior=true","zinglabs","12"};
		Connection con=ToolsBase.getConnection(constrs);
		con.close();
	}
}
