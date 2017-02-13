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


public class IndexBrowseDAO {
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	//queryIndexIdsByUserId
	//queryIndexIdsByUserId(conn,qcUserId);
	//queryIndexById( conn, index_id );
	
	public static List queryIndexs(Connection conn
	) throws SQLException {
			Statement stmt = null;
			ResultSet rs = null;
			String sql = "SELECT grade_index,grade_name, gradeindex_state, grade_index_desp, grade_index_creator, grade_index_time "
			+ " FROM SA_QC_GRADE_DICTINFO ";
	
		List list = new ArrayList();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
		
		
		while(rs.next()){
			IndexBrowseForm data = new IndexBrowseForm();
			data.setGrade_index(rs.getString("grade_index"));
			data.setGrade_name(rs.getString("grade_name"));
			data.setGradeindex_state(rs.getString("gradeindex_state"));
			if("1".equals(rs.getString("gradeindex_state"))){
				data.setAvailable("����");
			}else{
				data.setAvailable("������");
			}
			data.setGrade_index_desp(rs.getString("grade_index_desp"));
			data.setGrade_index_creator(rs.getString("grade_index_creator"));
			data.setGrade_index_time(rs.getString("grade_index_time"));
			
			list.add(data);
		}
		} catch (SQLException e) {
			LogUtil.debug("Invide SQL Statement:\n" ,SKIP_Logger);
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
	
	public static IndexBrowseForm queryIndexById(Connection conn, String index_id ) throws SQLException {

		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT grade_index,grade_name, gradeindex_state, grade_index_desp, grade_index_creator, grade_index_time "
		+ " FROM SA_QC_GRADE_DICTINFO where grade_index="+index_id;
	
		IndexBrowseForm data = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			if(rs.next()){
				data = new IndexBrowseForm();
				data.setGrade_index(rs.getString("grade_index"));
				data.setGrade_name(rs.getString("grade_name"));
				data.setGradeindex_state(rs.getString("gradeindex_state"));
				if("1".equals(rs.getString("gradeindex_state"))){
					data.setAvailable("����");
				}else{
					data.setAvailable("������");
				}
				data.setGrade_index_desp(rs.getString("grade_index_desp"));
				data.setGrade_index_creator(rs.getString("grade_index_creator"));
				data.setGrade_index_time(rs.getString("grade_index_time"));				
			}
		} catch (SQLException e) {
			LogUtil.debug("Invide SQL Statement:\n" ,SKIP_Logger);
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
		return data;
	}
	
	public static ArrayList queryIndexIdsByUserId(Connection conn, String qcUserId )
	throws SQLException {
			Statement stmt = null;
			ResultSet rs = null;
			String sql = "SELECT grade_index "
			+ " FROM SA_QC_USER_GRADE_TABLE where qc_user='"+qcUserId+"' and grade_index <> -1";
	
			ArrayList list = new ArrayList();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
				
			while(rs.next()){
				String index = rs.getString("grade_index");
				list.add(index);
			}
		} catch (SQLException e) {
			LogUtil.debug("Invalid sql ", SKIP_Logger);
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
	
	public static IndexBrowseForm getGradeDetail( Connection conn, String gradeIndex  )
    throws SQLException {

    if(gradeIndex == null || gradeIndex.trim().length() ==0)
        return null;

    Statement stmt = null;
    ResultSet rs = null;
    IndexBrowseForm form = null;

    try{
        String sql = "SELECT id,grade_name, gradeindex_state, grade_index_desp, grade_index_creator, grade_index_time "
    		+ " FROM SA_QC_GRADE_DICTINFO where id = '"+gradeIndex+"'";
    	
        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);
        if(rs.next()){
            form = new IndexBrowseForm();
			form.setGrade_index( rs.getString("id") );
			form.setGrade_name( rs.getString( "grade_name" ));				
			form.setGrade_index_creator( rs.getString("grade_index_creator") );
			form.setGrade_index_time( rs.getString("grade_index_time")); 
			form.setGrade_index_desp( rs.getString("grade_index_desp"));
			form.setGradeindex_state(rs.getString("gradeindex_state"));
        }
    }catch(SQLException e){
    	LogUtil.debug("Invalid sql statement", SKIP_Logger);
		throw e;
    }finally{
		try {
			if (rs != null) {
				rs.close();
                rs = null;
			}
			if (stmt != null) {
				stmt.close();
                stmt = null;
			}
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			throw e;
		}
    }
    return form;
}
	
	public static ArrayList queryGradeScore(Connection conn, String gradeIndex) 
    throws SQLException {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    ArrayList list = null;
	
	    try{
	        String sql = "select * from SA_QC_GRADE_DICTINFO where grade_index = ?" ;
	
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, gradeIndex);
	        list = new ArrayList();
	        rs = pstmt.executeQuery();
	        if(rs.next()){
	            for(int i = 0; i < 100; i ++){
	                String pfx_id = "";
	                if(i < 10)
	                    pfx_id = "pfx_00"+i;
	                else
	                    pfx_id = "pfx_0" +i;
	                String pfx = rs.getString(pfx_id);
	                if( pfx == null )
	                    break;
	                list.add(pfx);
	            }
	        }
	
	    }catch(SQLException e){	
	    	LogUtil.error(e, SKIP_Logger);
			throw e;
	
	    }finally{
			try {
				if (rs != null) {
					rs.close();
	                rs = null;
				}
				if (pstmt != null) {
					pstmt.close();
	                pstmt = null;
				}
			} catch (SQLException e) {		
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
	    }	
	    return list;
	}

}
