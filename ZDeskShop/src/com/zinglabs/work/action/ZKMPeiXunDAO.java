package com.zinglabs.work.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.log.LogUtil;


public class ZKMPeiXunDAO {
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk");
	
	public static ArrayList queryZKMList(Connection conn,String ids)
	throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT recordType,title,bDate,eDate,keywords,versions,createUser,createTime,lastModiTime,lastModiUser,lookNum FROM zkmInfoBase where id ='"+ids+"'";
		LogUtil.debug("sql = "+sql,SKIP_Logger);
		ArrayList list = new ArrayList();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				ZKMPeiXunForm form = new ZKMPeiXunForm();
				form.setRecordType(rs.getString("recordType"));
				form.setTitle(rs.getString("title"));
		        form.setBdate(rs.getString("bDate"));
				form.setEdate(rs.getString("eDate"));
				form.setKeywords(rs.getString("keywords"));
				form.setVersions(rs.getString("versions"));			
				form.setCreateUser(rs.getString("createUser"));
				form.setCreateTime(rs.getString("createTime"));
				form.setCreateTime(rs.getString("lastModiTime"));
				form.setLastModiTime(rs.getString("lastModiUser"));
				form.setLookNum(rs.getString("lookNum"));
//				form.setIsExpired(rs.getString("isExpired"));
				list.add(form);
			}
		} catch (SQLException e) {
			LogUtil.error("Invide  queryZKMList SQL Statement:\n" + e.toString(),SKIP_Logger);
			LogUtil.error(e, SKIP_Logger);
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return list;
	}

}
