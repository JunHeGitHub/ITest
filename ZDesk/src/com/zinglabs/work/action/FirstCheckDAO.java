package com.zinglabs.work.action;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.log.LogUtil;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.*;
import com.zinglabs.work.GradeData;

import com.zinglabs.work.WorkPublic;
import com.zinglabs.work.WorkPublicDAO;



public class FirstCheckDAO {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
		
	public static GradeData queryFirstTaskInfoNew( Connection conn, String gradeIndex )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		//sql = "select use_grade_index from T_GRADE_DETAIL where grade_id="+gradeId;	
		sql = "select grade_name,id,grade_yipiaofoujue,grade_important from SA_QC_GRADE_DICTINFO where id='"+gradeIndex+"'";
		LogUtil.debug(sql, SKIP_Logger);
		GradeData data = new GradeData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {				
				/*data.taskEndTime = rs.getString("task_end_dt");	*/			 
				//data.useGradeIndex = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
				data.useGradeIndex = rs.getInt("id" )+"";
				//data.useGradeIndexName = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
				data.useGradeIndexName=rs.getString("grade_name" );
				data.grade_yipiaofoujue=rs.getString("grade_yipiaofoujue");
				data.grade_important = rs.getString("grade_important");
			}
		} catch (SQLException e) {
			LogUtil.debug("Invide queryFirstTaskInfoNew Statement:\n" , SKIP_Logger);
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
	public static String queryScoreState( Connection conn, String filename )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";	
//		String isMp3 = "";
		sql = "select score_state from SU_QC_SOURCE_RECORD_DATA where file_name='"+filename+"'";
		LogUtil.debug(sql, SKIP_Logger);
		FirstCheckForm form = new FirstCheckForm();
		String scoreState = "";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {					
				scoreState=rs.getString("score_state" );
			}
		} catch (SQLException e) {
			LogUtil.debug("Invide queryIsMp3:\n" , SKIP_Logger);
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
		return scoreState;
	}
	
	public static String queryIsMp3( Connection conn, String filename )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";	
//		String isMp3 = "";
		sql = "select is_mp3 from SU_QC_SOURCE_RECORD_DATA where file_name='"+filename+"'";
		LogUtil.debug(sql, SKIP_Logger);
		GradeData data = new GradeData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {					
				data.isMp3=rs.getString("is_mp3" );
			}
		} catch (SQLException e) {
			LogUtil.debug("Invide queryIsMp3:\n" , SKIP_Logger);
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
		return data.isMp3;
	}
	
	public static String queryPhoneNumber( Connection conn, String filename )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";	
//		String isMp3 = "";
		sql = "select phone_number from SU_QC_SOURCE_RECORD_DATA where file_name='"+filename+"'";
		LogUtil.debug(sql, SKIP_Logger);
		GradeData data = new GradeData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {					
				data.phone_number=rs.getString("phone_number" );
			}
		} catch (SQLException e) {
			LogUtil.debug("Invide queryIsMp3:\n" , SKIP_Logger);
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
		return data.phone_number;
	}
	
	public static String queryIsMp3Id( Connection conn, String filename,String s )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";	
//		String isMp3 = "";
		sql = "select is_mp3 from SU_QC_SOURCE_RECORD_DATA where file_name='"+filename+s+"'";
		LogUtil.debug(sql, SKIP_Logger);
		GradeData data = new GradeData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {					
				data.isMp3=rs.getString("is_mp3" );
			}
		} catch (SQLException e) {
			LogUtil.debug("Invide queryIsMp3:\n" , SKIP_Logger);
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
		return data.isMp3;
	}
	 
    public static boolean saveGradeScoreNew(Connection conn, FirstCheckForm form ,String isRecord) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql = "replace into T_GRADE_DETAIL_NEW set ";
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String curTime = format.format(Calendar.getInstance().getTime());
    	
		try {
			if(isRecord.equals("txt")){
				String serialNumber = form.getSerialNumber();
	            String fileName = form.getFileName();
	            String gradeId = fileName.substring(0,fileName.length()-5);
	            String gradeRemark = form.getGradeRemark();
	            int oneticket = form.getOneticket();

		            String sOut = "";
		            Map formvalues = form.getValuesMap();
		            Set set = formvalues.entrySet();
		            Iterator it = set.iterator();	
		            while( it.hasNext() ){
		                Map.Entry me = (Map.Entry)it.next();
		                String keyStr = (String)me.getKey();
		      
		                String key = keyStr.split("_")[1];
		                String col_name = "pfx_";
		                if(key.length() == 1){
		                    col_name += "00"+key;
		                }else{
		                    col_name += "0"+key;
		                }
		                
		                sOut += col_name + "='" + (String)me.getValue()+"',";
		            } 
		            sql += sOut;	            
		            double total = WorkPublic.getCountTotal( conn, form.getUseGradeIndex(), formvalues, 0 );
		            DecimalFormat df = new DecimalFormat("#.00"); 
	            	String strTotal = df.format(total);	    
		            if(total <= 0){
		            	 sql += " pfx_total='0.00',";	 
		            	 strTotal = "0.00";
		            }else{          
			            sql += " pfx_total='"+strTotal+"',";	        
		            }	            

	            sql += " grade_remark='"+gradeRemark+"',";
	            sql += " file_name='"+form.getFileName()+"',";
	            sql += " file_location='"+form.getFileLocation()+"',";
	            sql += " use_grade_index='"+form.getUseGradeIndex()+"',";
	            sql += " grade_id='"+gradeId+"',";
	            sql += " grade_yipiaofoujue = '"+form.getGradeYipiaofoujue()+"'";
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				form.setPfxTotal(strTotal);
				LogUtil.debug("insert txt chujian sql"+sql,SKIP_Logger);
			}else{
				String serialNumber = form.getSerialNumber();
	            String fileName = form.getFileName();
	            String gradeId = fileName.substring(0,fileName.length()-4);
	            String gradeRemark = form.getGradeRemark();
	            int oneticket = form.getOneticket();
	            	String NoUse="";
		            String sOut = "";
		            Map formvalues = form.getValuesMap();
		            Set set = formvalues.entrySet();
		            Iterator it = set.iterator();	
		            while( it.hasNext() ){
		                Map.Entry me = (Map.Entry)it.next();
		                String keyStr = (String)me.getKey();
		                
		                String key = keyStr.split("_")[1];
		                String col_name = "pfx_";
		                if(key.length() == 1){
		                    col_name += "00"+key;
		                }else{
		                    col_name += "0"+key;
		                }
		                if((String)me.getValue()=="未使用" || ((String)me.getValue()).equals("未使用")){
		                	NoUse ="0";
		                }else{
		                	NoUse =(String)me.getValue();
		                }
		                
		                sOut += col_name + "='" + NoUse+"',";
		            } 
		            sql += sOut;	            
		            double total = WorkPublic.getCountTotal( conn, form.getUseGradeIndex(), formvalues, 0 );
		            DecimalFormat df = new DecimalFormat("0.00"); 
	            	String strTotal = df.format(total);	    
		            if(total<0){
		            	 sql += " pfx_total='0.00',";	 
		            	 strTotal = "0.00";
		            }else{          
			            sql += " pfx_total='"+strTotal+"',";	        
		            }
					    

	            sql += " grade_remark='"+gradeRemark+"',";
	            sql += " file_name='"+form.getFileName()+"',";
	            sql += " file_location='"+form.getFileLocation()+"',";
	            sql += " use_grade_index='"+form.getUseGradeIndex()+"',";
	            sql += " grade_id='"+gradeId+"',";
	            sql += " grade_yipiaofoujue = '"+form.getGradeYipiaofoujue()+"'";
	            
	            LogUtil.debug("saveGradeScoreNew sql:"+sql, SKIP_Logger);
	            
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				form.setPfxTotal(strTotal);
			}
            
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    }    
    
    public static boolean savePeixunGradeScoreNew(Connection conn, FirstCheckForm form ,String isRecord) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql = "replace into T_GRADE_DETAIL_PEIXUN_COMPLETE set ";
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String curTime = format.format(Calendar.getInstance().getTime());
    	
		try {
			if(isRecord.equals("txt")){
				String serialNumber = form.getSerialNumber();
	            String fileName = form.getFileName();
	            String gradeId = fileName.substring(0,fileName.length()-5);
	            String gradeRemark = form.getGradeRemark();
	            int oneticket = form.getOneticket();

		            String sOut = "";
		            Map formvalues = form.getValuesMap();
		            Set set = formvalues.entrySet();
		            Iterator it = set.iterator();	
		            while( it.hasNext() ){
		                Map.Entry me = (Map.Entry)it.next();
		                String keyStr = (String)me.getKey();
		                
		                String key = keyStr.split("_")[1];
		                String col_name = "pfx_";
		                if(key.length() == 1){
		                    col_name += "00"+key;
		                }else{
		                    col_name += "0"+key;
		                }
		                
		                sOut += col_name + "='" + (String)me.getValue()+"',";
		            } 
		            sql += sOut;	            
		            double total = WorkPublic.getCountTotal( conn, form.getUseGradeIndex(), formvalues, 0 );
					DecimalFormat df = new DecimalFormat("#.00"); 
					String strTotal = df.format(total);	           
		            sql += " pfx_total='"+strTotal+"',";	            

	            sql += " grade_remark='"+gradeRemark+"',";
	            sql += " file_name='"+form.getFileName()+"',";
	            sql += " file_location='"+form.getFileLocation()+"',";
	            sql += " use_grade_index='"+form.getUseGradeIndex()+"',";
	            sql += " grade_id='"+gradeId+"'";
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				form.setPfxTotal(strTotal);
			}else{
				String serialNumber = form.getSerialNumber();
	            String fileName = form.getFileName();
	            String gradeId = fileName.substring(0,fileName.length()-4);
	            String gradeRemark = form.getGradeRemark();
	            int oneticket = form.getOneticket();

		            String sOut = "";
		            Map formvalues = form.getValuesMap();
		            Set set = formvalues.entrySet();
		            Iterator it = set.iterator();	
		            while( it.hasNext() ){
		                Map.Entry me = (Map.Entry)it.next();
		                String keyStr = (String)me.getKey();
		                
		                String key = keyStr.split("_")[1];
		                String col_name = "pfx_";
		                if(key.length() == 1){
		                    col_name += "00"+key;
		                }else{
		                    col_name += "0"+key;
		                }
		                
		                sOut += col_name + "='" + (String)me.getValue()+"',";
		            } 
		            sql += sOut;	            
		            double total = WorkPublic.getCountTotal( conn, form.getUseGradeIndex(), formvalues, 0 );
					DecimalFormat df = new DecimalFormat("#.00"); 
					String strTotal = df.format(total);	           
		            sql += " pfx_total='"+strTotal+"',";	            

	            sql += " grade_remark='"+gradeRemark+"',";
	            sql += " file_name='"+form.getFileName()+"',";
	            sql += " file_location='"+form.getFileLocation()+"',";
	            sql += " use_grade_index='"+form.getUseGradeIndex()+"',";
	            sql += " grade_id='"+gradeId+"'";
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				form.setPfxTotal(strTotal);
			}
            
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    }    
    
    public static boolean saveGradeScoreNewFJ(Connection conn, FirstCheckForm form ,String isRecord) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql = "replace into T_GRADE_DETAIL_FUJIAN_NEW set ";
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String curTime = format.format(Calendar.getInstance().getTime());
    	
		try {
			if(isRecord.equals("txt")){
				String reowner = form.getReowner();
				String serialNumber = form.getSerialNumber();
	            String fileName = form.getFileName();
	            String gradeId = fileName.substring(0,fileName.length()-5);
	            String gradeRemark = form.getGradeRemark();
	            String fujianRemark = form.getFujianRemark();
	            int oneticket = form.getOneticket();

		            String sOut = "";
		            Map formvalues = form.getValuesMap();
		            Set set = formvalues.entrySet();
		            Iterator it = set.iterator();	
		            while( it.hasNext() ){
		                Map.Entry me = (Map.Entry)it.next();
		                String keyStr = (String)me.getKey();
		                
		                String key = keyStr.split("_")[1];
		                String col_name = "pfx_";
		                if(key.length() == 1){
		                    col_name += "00"+key;
		                }else{
		                    col_name += "0"+key;
		                }
		                
		                sOut += col_name + "='" + (String)me.getValue()+"',";
		            } 
		            sql += sOut;	            
		            double total = WorkPublic.getCountTotal( conn, form.getUseGradeIndex(), formvalues, 0 );
					DecimalFormat df = new DecimalFormat("#.00"); 
					String strTotal = df.format(total);	           
		            sql += " pfx_total='"+strTotal+"',";	            

	            sql += " grade_remark='"+gradeRemark+"',";
	            sql += " grade_remark_fj='"+fujianRemark+"',";
	            sql += " file_name='"+form.getFileName()+"',";
	            sql += " file_location='"+form.getFileLocation()+"',";
	            sql += " use_grade_index='"+form.getUseGradeIndex()+"',";
	            sql += " grade_id='"+gradeId+"',";
	            sql += " reowner='"+reowner+"',";
	            sql += " grade_yipiaofoujue = '"+form.getGradeYipiaofoujue()+"'";
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				form.setPfxTotal(strTotal);
			}else{
				String reowner = form.getReowner();
				String serialNumber = form.getSerialNumber();
	            String fileName = form.getFileName();
	            String gradeId = fileName.substring(0,fileName.length()-4);
	            String gradeRemark = form.getGradeRemark();
	            String fujianRemark = form.getFujianRemark();
	            int oneticket = form.getOneticket();

		            String sOut = "";
		            Map formvalues = form.getValuesMap();
		            Set set = formvalues.entrySet();
		            Iterator it = set.iterator();	
		            while( it.hasNext() ){
		                Map.Entry me = (Map.Entry)it.next();
		                String keyStr = (String)me.getKey();
		                
		                String key = keyStr.split("_")[1];
		                String col_name = "pfx_";
		                if(key.length() == 1){
		                    col_name += "00"+key;
		                }else{
		                    col_name += "0"+key;
		                }
		                
		                sOut += col_name + "='" + (String)me.getValue()+"',";
		            } 
		            sql += sOut;	            
		            double total = WorkPublic.getCountTotal( conn, form.getUseGradeIndex(), formvalues, 0 );
					DecimalFormat df = new DecimalFormat("#.00"); 
					String strTotal = df.format(total);	           
		            sql += " pfx_total='"+strTotal+"',";	            

	            sql += " grade_remark='"+gradeRemark+"',";
	            sql += " grade_remark_fj='"+fujianRemark+"',";
	            sql += " file_name='"+form.getFileName()+"',";
	            sql += " file_location='"+form.getFileLocation()+"',";
	            sql += " use_grade_index='"+form.getUseGradeIndex()+"',";
	            sql += " grade_id='"+gradeId+"',";
	            sql += " reowner='"+reowner+"',";
	            sql += " grade_yipiaofoujue = '"+form.getGradeYipiaofoujue()+"'";
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				form.setPfxTotal(strTotal);
			}
            
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    }    
    public static boolean saveGradeScoreNewFS(Connection conn, FirstCheckForm form ,String isRecord) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql = "replace into T_GRADE_DETAIL_FUSHEN_NEW set ";
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String curTime = format.format(Calendar.getInstance().getTime());
    	String reowner = form.getReowner();
		try {
			if(isRecord.equals("txt")){
				String serialNumber = form.getSerialNumber();
	            String fileName = form.getFileName();
	            String gradeId = fileName.substring(0,fileName.length()-5);
	            String gradeRemark = form.getGradeRemark();
	            String fujianRemark = form.getFujianRemark();
	            int oneticket = form.getOneticket();

		            String sOut = "";
		            Map formvalues = form.getValuesMap();
		            Set set = formvalues.entrySet();
		            Iterator it = set.iterator();	
		            while( it.hasNext() ){
		                Map.Entry me = (Map.Entry)it.next();
		                String keyStr = (String)me.getKey();
		                
		                String key = keyStr.split("_")[1];
		                String col_name = "pfx_";
		                if(key.length() == 1){
		                    col_name += "00"+key;
		                }else{
		                    col_name += "0"+key;
		                }
		                
		                sOut += col_name + "='" + (String)me.getValue()+"',";
		            } 
		            sql += sOut;	            
		            double total = WorkPublic.getCountTotalNew( conn, form.getUseGradeIndex(), formvalues, 0 );
		            DecimalFormat df = new DecimalFormat("#.00"); 
	            	String strTotal = df.format(total);	    
		            if(total<0){
		            	 sql += " pfx_total='0.00',";	 
		            	 strTotal = "0.00";
		            }else{          
			            sql += " pfx_total='"+strTotal+"',";	        
		            }	                        

	            sql += " grade_remark='"+gradeRemark+"',";
	            sql += " grade_remark_fj='"+fujianRemark+"',";
	            sql += " file_name='"+form.getFileName()+"',";
	            sql += " file_location='"+form.getFileLocation()+"',";
	            sql += " use_grade_index='"+form.getUseGradeIndex()+"',";
	            sql += " grade_id='"+gradeId+"',";
	            sql += " reowner='"+reowner+"',";
	            sql += " grade_important = '"+form.getGradeImportant()+"'";
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				form.setPfxTotal(strTotal);
			}else{
				String serialNumber = form.getSerialNumber();
	            String fileName = form.getFileName();
	            String gradeId = fileName.substring(0,fileName.length()-4);
	            String gradeRemark = form.getGradeRemark();
	            String fujianRemark = form.getFujianRemark();
	            int oneticket = form.getOneticket();

		            String sOut = "";
		            Map formvalues = form.getValuesMap();
		            Set set = formvalues.entrySet();
		            Iterator it = set.iterator();	
		            while( it.hasNext() ){
		                Map.Entry me = (Map.Entry)it.next();
		                String keyStr = (String)me.getKey();
		                
		                String key = keyStr.split("_")[1];
		                String col_name = "pfx_";
		                if(key.length() == 1){
		                    col_name += "00"+key;
		                }else{
		                    col_name += "0"+key;
		                }
		                
		                sOut += col_name + "='" + (String)me.getValue()+"',";
		            } 
		            sql += sOut;	            
		            double total = WorkPublic.getCountTotal( conn, form.getUseGradeIndex(), formvalues, 0 );
					DecimalFormat df = new DecimalFormat("#.00"); 
					String strTotal = df.format(total);	           
		            sql += " pfx_total='"+strTotal+"',";	            

	            sql += " grade_remark='"+gradeRemark+"',";
	            sql += " grade_remark_fj='"+fujianRemark+"',";
	            sql += " file_name='"+form.getFileName()+"',";
	            sql += " file_location='"+form.getFileLocation()+"',";
	            sql += " use_grade_index='"+form.getUseGradeIndex()+"',";
	            sql += " grade_id='"+gradeId+"',";
	            sql += " reowner='"+reowner+"',";
	            sql += " grade_yipiaofoujue = '"+form.getGradeYipiaofoujue()+"'";
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				form.setPfxTotal(strTotal);
			}
            
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    }    
    public static boolean saveGradeScoreOld(Connection conn, FirstCheckForm form ,String isRecord) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql = "replace into T_GRADE_DETAIL_NEW set ";
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String curTime = format.format(Calendar.getInstance().getTime());
    	
		try {
			if(isRecord.equals("txt")){
				String serialNumber = form.getSerialNumber();
	            String fileName = form.getFileName();
	            String gradeId = fileName.substring(0,fileName.length()-5);
	            String gradeRemark = form.getGradeRemark();
	            int oneticket = form.getOneticket();

		            String sOut = "";
		            Map formvalues = form.getValuesMap();
		            Set set = formvalues.entrySet();
		            Iterator it = set.iterator();	
		            while( it.hasNext() ){
		                Map.Entry me = (Map.Entry)it.next();
		                String keyStr = (String)me.getKey();
		                
		                String key = keyStr.split("_")[1];
		                String col_name = "pfx_";
		                if(key.length() == 1){
		                    col_name += "00"+key;
		                }else{
		                    col_name += "0"+key;
		                }
		                
		                sOut += col_name + "='0.00',";
		            } 
		            sql += sOut;	            
		            double total = WorkPublic.getCountTotal( conn, form.getUseGradeIndex(), formvalues, 0 );
					DecimalFormat df = new DecimalFormat("#.00"); 
					String strTotal = df.format(total);	           
		            sql += " pfx_total='0.00',";	            

	            sql += " grade_remark='"+gradeRemark+"',";
	            sql += " file_name='"+form.getFileName()+"',";
	            sql += " file_location='"+form.getFileLocation()+"',";
	            sql += " use_grade_index='"+form.getUseGradeIndex()+"',";
	            sql += " grade_id='"+gradeId+"',";
	            sql += " grade_important = '"+form.getGradeImportant()+"'";
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				form.setPfxTotal(strTotal);
			}else{
				String serialNumber = form.getSerialNumber();
	            String fileName = form.getFileName();
	            String gradeId = fileName.substring(0,fileName.length()-4);
	            String gradeRemark = form.getGradeRemark();
	            int oneticket = form.getOneticket();

		            String sOut = "";
		            Map formvalues = form.getValuesMap();
		            Set set = formvalues.entrySet();
		            Iterator it = set.iterator();	
		            while( it.hasNext() ){
		                Map.Entry me = (Map.Entry)it.next();
		                String keyStr = (String)me.getKey();
		                
		                String key = keyStr.split("_")[1];
		                String col_name = "pfx_";
		                if(key.length() == 1){
		                    col_name += "00"+key;
		                }else{
		                    col_name += "0"+key;
		                }
		                
		                sOut += col_name + "='0.00',";
		            } 
		            sql += sOut;	            
		            double total = WorkPublic.getCountTotal( conn, form.getUseGradeIndex(), formvalues, 0 );
					DecimalFormat df = new DecimalFormat("#.00"); 
					String strTotal = df.format(total);	           
		            sql += " pfx_total='0.00',";	            

	            sql += " grade_remark='"+gradeRemark+"',";
	            sql += " file_name='"+form.getFileName()+"',";
	            sql += " file_location='"+form.getFileLocation()+"',";
	            sql += " use_grade_index='"+form.getUseGradeIndex()+"',";
	            sql += " grade_id='"+gradeId+"',";
	            sql += " grade_yipiaofoujue = '"+form.getGradeYipiaofoujue()+"'";
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				form.setPfxTotal(strTotal);
			}
            
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    }    
    
    public static boolean updateGradeDetailNew(Connection conn, FirstCheckForm form) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		Map map = form.getValuesMap();
		Set<String> key = map.keySet();
		String mapSql = "";
        for (Iterator it = key.iterator(); it.hasNext();) {
            String s = (String) it.next();
            if(s.endsWith("_0")){
            	mapSql +=",pfx_000="+map.get(s);
            }else if (s.endsWith("_1")){
            	mapSql +=",pfx_001="+map.get(s);
            }else if (s.endsWith("_2")){
            	mapSql +=",pfx_002="+map.get(s);
            }else if (s.endsWith("_3")){
            	mapSql +=",pfx_003="+map.get(s);
            }else if (s.endsWith("_4")){
            	mapSql +=",pfx_004="+map.get(s);
            }else if (s.endsWith("_5")){
            	mapSql +=",pfx_005="+map.get(s);
            }else if (s.endsWith("_6")){
            	mapSql +=",pfx_006="+map.get(s);
            }else if (s.endsWith("_7")){
            	mapSql +=",pfx_007="+map.get(s);
            }else if (s.endsWith("_8")){
            	mapSql +=",pfx_008="+map.get(s);
            }else if (s.endsWith("_9")){
            	mapSql +=",pfx_009="+map.get(s);
            }else if (s.endsWith("_10")){
            	mapSql +=",pfx_010="+map.get(s);
            }else if (s.endsWith("_11")){
            	mapSql +=",pfx_011="+map.get(s);
            }else if (s.endsWith("_12")){
            	mapSql +=",pfx_012="+map.get(s);
            }else if (s.endsWith("_13")){
            	mapSql +=",pfx_013="+map.get(s);
            }else if (s.endsWith("_14")){
            	mapSql +=",pfx_014="+map.get(s);
            }else if (s.endsWith("_15")){
            	mapSql +=",pfx_015="+map.get(s);
            }
            
        }
   
		
        String sql= "update T_GRADE_DETAIL_NEW set "+mapSql.substring(1)+"  where file_name='"+form.getFileName()+"'";
//        String sql1="update SU_QC_SOURCE_RECORD_DATA set "+mapSql.substring(1)+"  where file_name='"+form.getFileName()+"'";
//    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	String curTime = format.format(Calendar.getInstance().getTime());
    	
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
//			stmt.executeUpdate(sql1);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateGradeDetailNewFs(Connection conn, FirstCheckForm form) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		Map map = form.getValuesMap();
		Set<String> key = map.keySet();
		String mapSql = "";
        for (Iterator it = key.iterator(); it.hasNext();) {
            String s = (String) it.next();
            if(s.endsWith("_0")){
            	mapSql +=",pfx_000="+map.get(s);
            }else if (s.endsWith("_1")){
            	mapSql +=",pfx_001="+map.get(s);
            }else if (s.endsWith("_2")){
            	mapSql +=",pfx_002="+map.get(s);
            }else if (s.endsWith("_3")){
            	mapSql +=",pfx_003="+map.get(s);
            }else if (s.endsWith("_4")){
            	mapSql +=",pfx_004="+map.get(s);
            }else if (s.endsWith("_5")){
            	mapSql +=",pfx_005="+map.get(s);
            }else if (s.endsWith("_6")){
            	mapSql +=",pfx_006="+map.get(s);
            }else if (s.endsWith("_7")){
            	mapSql +=",pfx_007="+map.get(s);
            }else if (s.endsWith("_8")){
            	mapSql +=",pfx_008="+map.get(s);
            }else if (s.endsWith("_9")){
            	mapSql +=",pfx_009="+map.get(s);
            }else if (s.endsWith("_10")){
            	mapSql +=",pfx_010="+map.get(s);
            }else if (s.endsWith("_11")){
            	mapSql +=",pfx_011="+map.get(s);
            }else if (s.endsWith("_12")){
            	mapSql +=",pfx_012="+map.get(s);
            }else if (s.endsWith("_13")){
            	mapSql +=",pfx_013="+map.get(s);
            }else if (s.endsWith("_14")){
            	mapSql +=",pfx_014="+map.get(s);
            }else if (s.endsWith("_15")){
            	mapSql +=",pfx_015="+map.get(s);
            }
            
        }
   
		
        String sql= "update T_GRADE_DETAIL_FUSHEN_NEW set "+mapSql.substring(1)+"  where file_name='"+form.getFileName()+"'";
//        String sql1="update SU_QC_SOURCE_RECORD_DATA set "+mapSql.substring(1)+"  where file_name='"+form.getFileName()+"'";
//    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	String curTime = format.format(Calendar.getInstance().getTime());
    	
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
//			stmt.executeUpdate(sql1);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateAgentArgumentNew(Connection conn, FirstCheckForm form ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date =(String) format.format(new java.util.Date());
        String sql= "update SA_AGENT_ARGUMENT set work_state='1', admin_remark='"+form.getAdminAgentArguRemark()+"' , update_time = '"+date+"'  where grade_id='"+form.getGradeId()+"'";//update work_state='1'
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateAgentSaAgentArgumentRecord(Connection conn, FirstCheckForm form ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String date =(String) format.format(new java.util.Date());
        String sql= "update SA_AGENT_ARGUMENT set grade_remark='"+form.getGradeRemark()+"'  where grade_id='"+form.getGradeId()+"'";
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    public static boolean updateAgentSaAgentArgumentRecordFJ(Connection conn, FirstCheckForm form ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String date =(String) format.format(new java.util.Date());
        String sql= "update SA_AGENT_ARGUMENT set grade_remark_fj='"+form.getGradeRemark()+"'  where grade_id='"+form.getGradeId()+"'";
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateAgentSaAgentArgumentText(Connection conn, FirstCheckForm form ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String date =(String) format.format(new java.util.Date());
        String sql= "update SA_AGENT_ARGUMENT set grade_remark='"+form.getGradeRemark()+"'   where grade_id='"+form.getGradeId()+"'";
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    public static boolean updateAgentSaAgentArgumentTextFJ(Connection conn, FirstCheckForm form ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String date =(String) format.format(new java.util.Date());
        String sql= "update SA_AGENT_ARGUMENT set grade_remark_fj='"+form.getGradeRemark()+"'   where grade_id='"+form.getGradeId()+"'";
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    public static boolean updateScoreState(Connection conn, FirstCheckForm form,boolean changer  ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		//form.getPfxTotal()
		String sql = "";
		if(changer){
			sql= "update SU_QC_SOURCE_RECORD_DATA set score_state='上诉成功'  where file_name='"+form.getGradeId()+".mp3'";
		}else{
			sql= "update SU_QC_SOURCE_RECORD_DATA set score_state='上诉失败'   where file_name='"+form.getGradeId()+".mp3'";
		}
        
        
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    public static boolean updateScoreStateText(Connection conn, FirstCheckForm form,boolean changer  ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		//form.getPfxTotal()
		String sql = "";
		if(changer){
			sql= "update SU_QC_SOURCE_RECORD_DATA set score_state='上诉成功'  where file_name='"+form.getGradeId()+".html'";
		}else{
			sql= "update SU_QC_SOURCE_RECORD_DATA set score_state='上诉失败'   where file_name='"+form.getGradeId()+".html'";
		}
        
        
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateScoreStateArgument(Connection conn, FirstCheckForm form,boolean changer  ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		//form.getPfxTotal()
		String sql = "";
		if(changer){
			sql= "update SA_AGENT_ARGUMENT set score_state='上诉成功'  where grade_id='"+form.getGradeId()+"'";
		}else{
			sql= "update SA_AGENT_ARGUMENT set score_state='上诉失败'   where grade_id='"+form.getGradeId()+"'";
		}
        
        
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateScoreStateArgumentText(Connection conn, FirstCheckForm form,boolean changer  ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		//form.getPfxTotal()
		String sql = "";
		if(changer){
			sql= "update SA_AGENT_ARGUMENT set score_state='上诉成功'  where grade_id='"+form.getGradeId()+"'";
		}else{
			sql= "update SA_AGENT_ARGUMENT set score_state='上诉失败'   where grade_id='"+form.getGradeId()+"'";
		}
        
        
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateScoreStateWorkState(Connection conn, FirstCheckForm form,String fabu ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		String sql="";
		if(fabu.equals("0"))
			sql= "update SU_QC_SOURCE_RECORD_DATA set  work_state=\"发布成功\"   where file_name='"+form.getGradeId()+".mp3'";//score_state=\"上诉成功\" , 
		else if(fabu.equals("1"))
			sql= "update SU_QC_SOURCE_RECORD_DATA set  work_state=\"发布失败\"   where file_name='"+form.getGradeId()+".mp3'";//score_state=\"上诉失败\" ,
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    }
    
    public static boolean updateWorkState(Connection conn, FirstCheckForm form,String fabu ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		String sql="";
		if(fabu.equals("0"))
			sql= "update SA_AGENT_ARGUMENT  set  work_state=\"发布成功\"   where grade_id='"+form.getGradeId()+"'";//score_state=\"上诉成功\" , 
		else if(fabu.equals("1"))
			sql= "update SA_AGENT_ARGUMENT set  work_state=\"发布失败\"   where grade_id='"+form.getGradeId()+"'";//score_state=\"上诉失败\" ,
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    }
    
    public static boolean updateScoreStateFalse(Connection conn, FirstCheckForm form ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql= "update SU_QC_SOURCE_RECORD_DATA set score_state='上诉失败'  where file_name='"+form.getGradeId()+".mp3'";
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateMauvais(Connection conn, FirstCheckForm form, int x , int y ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql= "update T_GRADE_DETAIL_NEW set mauvais_item='"+x+"' , mauvais_number='"+y+"'  where file_name='"+form.getFileName()+"'";  	
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    public static boolean updateScoreState(Connection conn, String fileName ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql= "update SU_QC_SOURCE_RECORD_DATA set score_state='初检已发布' where file_name='"+fileName+"'";  	
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateScoreStateFushen(Connection conn, String fileName ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql= "update SA_AGENT_ARGUMENT set score_state='复审已发布'  where file_name='"+fileName+"'";  	
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
//			李金与客户交流结果 su_qc_record_data也要更新
			sql= "update SU_QC_SOURCE_RECORD_DATA set score_state='复审已发布'  where file_name='"+fileName+"'";
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateGradeScoreNew(Connection conn, FirstCheckForm form ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql= "update SU_QC_SOURCE_RECORD_DATA set score_state='初检已评分' where file_name='"+form.getFileName()+"'";
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String curTime = format.format(Calendar.getInstance().getTime());
    	
    	
    	LogUtil.debug(conn.getMetaData().getURL()+"updateGradeScoreNew sql:"+sql, SKIP_Logger);
    	
		try {
		    
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			LogUtil.debug("updateGradeScoreNew ", SKIP_Logger);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateFJGradeScoreNew(Connection conn, FirstCheckForm form ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql= "update SU_QC_SOURCE_RECORD_DATA set score_state='复检已评分' where file_name='"+form.getFileName()+"'";
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String curTime = format.format(Calendar.getInstance().getTime());
    	
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updatePfxTotalNew(Connection conn, FirstCheckForm form ,int pfxSum) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql= "update SU_QC_SOURCE_RECORD_DATA set pfx_total='"+pfxSum+"' where file_name='"+form.getFileName()+"'";
		String sql1= "update SU_QC_SOURCE_RECORD_DATA set pfx_total='"+form.getPfxTotal()+"' where file_name='"+form.getFileName()+"'";
//    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	String curTime = format.format(Calendar.getInstance().getTime());
    	
		try {
			stmt = conn.createStatement();
			if(pfxSum ==0 ){
			    LogUtil.debug(conn.getMetaData().getURL()+"updatePfxTotalNew sql1:"+sql1, SKIP_Logger);
				stmt.executeUpdate(sql1);
			}else{
			    LogUtil.debug(conn.getMetaData().getURL()+"updatePfxTotalNew sql:"+sql, SKIP_Logger);
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			LogUtil.debug("sql", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateTGradeDetailNew(Connection conn, FirstCheckForm form ,int pfxSum) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql= "update T_GRADE_DETAIL_NEW set pfx_total='"+pfxSum+"' where file_name='"+form.getFileName()+"'";
		String sql1= "update T_GRADE_DETAIL_NEW set pfx_total='"+form.getPfxTotal()+"' where file_name='"+form.getFileName()+"'";
//    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	String curTime = format.format(Calendar.getInstance().getTime());
    	
		try {
			stmt = conn.createStatement();
			if(pfxSum ==0 ){
			    LogUtil.debug(conn.getMetaData().getURL()+"updateT_GRADE_DETAIL_NEW sql1:"+sql1, SKIP_Logger);
				stmt.executeUpdate(sql1);
			}else{
			    LogUtil.debug(conn.getMetaData().getURL()+"updateT_GRADE_DETAIL_NEW sql:"+sql, SKIP_Logger);
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			LogUtil.debug("sql", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static int CountFileNameNumberRS(Connection conn, FirstCheckForm form,String isRecord ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		int countNumber  =0;
//        String sql= "update SU_QC_SOURCE_RECORD_DATA set avg_pfx_total='"+form.getPfxTotal()+"' where file_name='"+form.getFileName()+"'";
//    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	String curTime = format.format(Calendar.getInstance().getTime());
//    	String sql ="select sum(pfx_total) from T_GRADE_DETAIL_FUJIAN_NEW where file_name='"+form.getFileName()+"'";
    	String sql ="select count(file_name) from T_GRADE_DETAIL_FUJIAN_NEW where file_name='"+form.getFileName()+"'";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				countNumber =rs.getInt("count(file_name)" );
			}
		} catch (SQLException e) {
			LogUtil.debug("sql", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return countNumber;
    } 
    
    public static int sumFileNameNumberRS(Connection conn, FirstCheckForm form,String isRecord ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		int sumNumber  = 0;
//        String sql= "update SU_QC_SOURCE_RECORD_DATA set avg_pfx_total='"+form.getPfxTotal()+"' where file_name='"+form.getFileName()+"'";
//    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	String curTime = format.format(Calendar.getInstance().getTime());
    	String sql ="select sum(pfx_total) from T_GRADE_DETAIL_FUJIAN_NEW where file_name='"+form.getFileName()+"'";
//    	String sql1 ="select count(file_name) from T_GRADE_DETAIL_FUJIAN_NEW where file_name='"+form.getFileName()+"'";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				sumNumber = rs.getInt("sum(pfx_total)" );
			}
		} catch (SQLException e) {
			LogUtil.debug("sql", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return sumNumber;
    } 
    
    public static boolean calAvgPfxScoreRS(Connection conn, FirstCheckForm form,String isRecord,int countNumber,int sumNumber) throws SQLException{
	
    	Statement stmt = null;
		ResultSet rs = null;

		float avg_pfx =sumNumber/countNumber ;
		
        String sql= "update T_GRADE_DETAIL_FUJIAN_NEW set avg_pfx_total='"+avg_pfx+"' where file_name='"+form.getFileName()+"'";
//    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	String curTime = format.format(Calendar.getInstance().getTime());
//    	String sql ="select count(file_name) from T_GRADE_DETAIL_FUJIAN_NEW where file_name='"+form.getFileName()+"'";
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.debug("sql", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateAgentTeachQuery(Connection conn, FirstCheckForm form ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String curTime = format.format(Calendar.getInstance().getTime());
        String sql= "update AGENT_TEACH_QUERY set generate_time='"+curTime+"'   where file_name='"+form.getFileName()+"'";
        String sql1= "update AGENT_TEACH_QUERY set dejafinir='"+"已完成"+"'   where file_name='"+form.getFileName()+"'";
//    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	String curTime = format.format(Calendar.getInstance().getTime());
    	
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql1);
		} catch (SQLException e) {
			LogUtil.debug("sql", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updatePfxTotalOld(Connection conn, FirstCheckForm form ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql= "update SU_QC_SOURCE_RECORD_DATA set pfx_total='0.00'  where file_name='"+form.getFileName()+"'";
//    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	String curTime = format.format(Calendar.getInstance().getTime());
    	
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.debug("sql", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updatePfxTotal(Connection conn, FirstCheckForm form ) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql= "update SU_QC_SOURCE_RECORD_DATA set  pfx_total='"+form.getPfxTotal()+"' where file_name='"+form.getFileName()+"'";
//    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    	String curTime = format.format(Calendar.getInstance().getTime());
    	
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.debug("sql", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
     public static GradeData queryTeachRecordInfo( Connection conn, String gradeId )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		//sql = "select use_grade_index from T_GRADE_DETAIL where grade_id="+gradeId;	
		sql = "select use_grade_index,file_name,file_location from T_GRADE_DETAIL_NEW where grade_id='"+gradeId+"'";
		
		GradeData data = new GradeData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {				
				/*data.taskEndTime = rs.getString("task_end_dt");	*/			 
				//data.useGradeIndex = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
				data.useGradeIndex = rs.getInt("use_grade_index" )+"";
				//data.useGradeIndexName = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
				data.file_name=rs.getString("file_name" );
				data.file_location=rs.getString("file_location" );
			}
		} catch (SQLException e) {
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
		return data;
	}
     
     public static FirstCheckForm queryFormInfo( Connection conn, String gradeId )
 	throws SQLException 	
 	{
 		Statement stmt = null;
 		ResultSet rs = null;
 		String sql = "";
 		//sql = "select use_grade_index from T_GRADE_DETAIL where grade_id="+gradeId;	
 		sql = "select use_grade_index,file_name,file_location,pfx_000,pfx_001,pfx_002,pfx_003,pfx_004,pfx_005,pfx_006,pfx_007,pfx_008,pfx_009,pfx_010,pfx_011,pfx_012,pfx_013,pfx_014,pfx_015,pfx_016,pfx_017,pfx_018,pfx_019,pfx_020 from T_GRADE_DETAIL_NEW where grade_id='"+gradeId+"' ORDER by grade_id";
 		
 		FirstCheckForm data = new FirstCheckForm();
 		try {
 			stmt = conn.createStatement();
 			rs = stmt.executeQuery(sql);
 			while (rs.next()) {				
 				data.setUseGradeIndex(rs.getInt("use_grade_index" )+"");
 				data.setFileName(rs.getString("file_name" ));
 				data.setFileLocation(rs.getString("file_location" ));
 				Map map = new HashMap();
 				map.put("pfx_000", rs.getString("pfx_000" ));
 				map.put("pfx_001", rs.getString("pfx_001" ));
 				map.put("pfx_002", rs.getString("pfx_002" ));
 				map.put("pfx_003", rs.getString("pfx_003" ));
 				map.put("pfx_004", rs.getString("pfx_004" ));
 				map.put("pfx_005", rs.getString("pfx_005" ));
 				map.put("pfx_006", rs.getString("pfx_006" ));
 				map.put("pfx_007", rs.getString("pfx_007" ));
 				map.put("pfx_008", rs.getString("pfx_008" ));
 				map.put("pfx_009", rs.getString("pfx_009" ));
 				map.put("pfx_010", rs.getString("pfx_010" ));
 				map.put("pfx_011", rs.getString("pfx_011" ));
 				map.put("pfx_012", rs.getString("pfx_012" ));
 				map.put("pfx_013", rs.getString("pfx_013" ));
 				map.put("pfx_014", rs.getString("pfx_014" ));
 				map.put("pfx_015", rs.getString("pfx_015" ));
 				map.put("pfx_016", rs.getString("pfx_016" ));
 				map.put("pfx_017", rs.getString("pfx_017" ));
 				map.put("pfx_018", rs.getString("pfx_018" ));
 				map.put("pfx_019", rs.getString("pfx_019" ));
 				map.put("pfx_020", rs.getString("pfx_020" ));
 				data.setValuesMap(map);
 			}
 		} catch (SQLException e) {
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
 		return data;
 	}
     
     public static GradeData queryAgentRecordInfo( Connection conn, String gradeId )
 	throws SQLException 	
 	{
 		Statement stmt = null;
 		ResultSet rs = null;
 		String sql = "";
 		//sql = "select use_grade_index from T_GRADE_DETAIL where grade_id="+gradeId;	
 		sql = "select use_grade_index,file_name,file_location from T_GRADE_DETAIL_NEW where grade_id='"+gradeId+"'";
 		
 		GradeData data = new GradeData();
 		try {
 			stmt = conn.createStatement();
 			rs = stmt.executeQuery(sql);
 			while (rs.next()) {				
 				/*data.taskEndTime = rs.getString("task_end_dt");	*/			 
 				//data.useGradeIndex = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
 				data.useGradeIndex = rs.getInt("use_grade_index" )+"";
 				//data.useGradeIndexName = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
 				data.file_name=rs.getString("file_name" );
 				data.file_location=rs.getString("file_location" );
 			}
 		} catch (SQLException e) {
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
 		return data;
 	}
    
    public static String queryGradeName( Connection conn, String useGradeIndex )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String gradeName = "";
		String sql = "";
		//sql = "select use_grade_index from T_GRADE_DETAIL where grade_id="+gradeId;	
		sql = "select grade_name from SA_QC_GRADE_DICTINFO where id='"+useGradeIndex+"'";
		
		GradeData data = new GradeData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {				
				/*data.taskEndTime = rs.getString("task_end_dt");	*/			 
				//data.useGradeIndex = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
//				data.useGradeIndex = rs.getInt("use_grade_index" )+"";
				//data.useGradeIndexName = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
				/*data.grade_name=rs.getString("grade_name" );
				data.setGrade_name(rs.getString("grade_name" ));*/
				gradeName = rs.getString("grade_name" );
//				data.file_location=rs.getString("file_location" );
			}
		} catch (SQLException e) {
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
		return gradeName;
	}
    
    public static String queryAgentGradeId( Connection conn, String gradeName )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String gradeId = "";
		String sql = "";
		//sql = "select use_grade_index from T_GRADE_DETAIL where grade_id="+gradeId;	
		sql = "select id from SA_QC_GRADE_DICTINFO where grade_name='"+gradeName+"'";
		
		GradeData data = new GradeData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {				
				gradeId = rs.getString("id" );
			}
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			throw e;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return gradeId;
	}
    
    public static String queryAgentGradeName( Connection conn, String useGradeIndex )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String gradeName = "";
		String sql = "";
		//sql = "select use_grade_index from T_GRADE_DETAIL where grade_id="+gradeId;	
		sql = "select grade_name from SA_QC_GRADE_DICTINFO where id='"+useGradeIndex+"'";
		
		GradeData data = new GradeData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {				
				/*data.taskEndTime = rs.getString("task_end_dt");	*/			 
				//data.useGradeIndex = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
//				data.useGradeIndex = rs.getInt("use_grade_index" )+"";
				//data.useGradeIndexName = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
				/*data.grade_name=rs.getString("grade_name" );
				data.setGrade_name(rs.getString("grade_name" ));*/
				gradeName = rs.getString("grade_name" );
//				data.file_location=rs.getString("file_location" );
			}
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			throw e;
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return gradeName;
	}
    public static void saveArgu( Connection conn, FirstCheckForm form,String agentArgue,String gradeRemark,String gradeRemarkFJ,String isRecord)
	throws SQLException 
	{		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String curTime = format.format(Calendar.getInstance().getTime());
		String gradeId = null;
		PreparedStatement pstmt = null;		
		if(isRecord.equals("txt")){
			gradeId = form.getFileName().substring(0, form.getFileName().length()-5);
		}else{
			gradeId = form.getFileName().substring(0, form.getFileName().length()-4);
		}
		
		
	    String sql = "replace into SA_AGENT_ARGUMENT(grade_id,owner," +
	    		"agent_user,agent_user_dt,agent_user_remark,grade_remark,grade_remark_fj,file_name) values(?,?,?,?,?,?,?,?)";      
		
		try {
			pstmt = conn.prepareStatement( sql );
			pstmt.setString(1, gradeId );
			pstmt.setString(2, form.getOwner() );
			pstmt.setString(3, form.getPhoneNumber());
			pstmt.setString(4, curTime );
			pstmt.setString(5, agentArgue);
			pstmt.setString(6, gradeRemark);
			pstmt.setString(7, gradeRemarkFJ);
			pstmt.setString(8, form.getFileName());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			throw e;
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
		}		
	}    	
    public static FirstCheckForm getAgentArguementWork( Connection conn,  String gradeId,String isRecord )
	throws SQLException 	
{
    	FirstCheckForm data = new FirstCheckForm();	
		Statement stmt = null;
		ResultSet rs = null;	
		
		String sql = "select * from SU_QC_SOURCE_RECORD_DATA"+
			" where file_name='"+gradeId+".mp3'";  
		
		String sql1 ="select * from SU_QC_SOURCE_RECORD_DATA"+
		" where file_name='"+gradeId+".html'";  
	
		try {
			if(isRecord.equals("txt")){
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql1);			
				while(rs.next()){
					data.setFileName(rs.getString("file_name"));
					data.setOwner( rs.getString("owner") );
					data.setPhoneNumber(rs.getString("phone_number"));	
					data.setGradeName(rs.getString("grade_name"));
				}
			}else{
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);			
				while(rs.next()){
					data.setFileName(rs.getString("file_name"));
					data.setOwner( rs.getString("owner") );
					data.setPhoneNumber(rs.getString("phone_number"));	
					data.setGradeName(rs.getString("grade_name"));
				}
			}
		} catch (SQLException e) {
			LogUtil.error("Invide SQL Statement:\n", SKIP_Logger);
			throw e;
		} 
		finally{
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return data;
	}
    
    public static GradeData getAgentArguementWorkGradeData( Connection conn,  String gradeId,String isRecord )
	throws SQLException 	
{
    	GradeData data = new GradeData();	
		Statement stmt = null;
		ResultSet rs = null;	
		
		String sql = "select * from SU_QC_SOURCE_RECORD_DATA"+
			" where file_name='"+gradeId+".mp3'";  
		
		String sql1 ="select * from SU_QC_SOURCE_RECORD_DATA"+
		" where file_name='"+gradeId+".html'";  
	
		try {
			if(isRecord.equals("txt")){
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql1);			
				while(rs.next()){
					data.setFile_name(rs.getString("file_name"));
					data.setPhone_number(rs.getString("phone_number"));	
					data.setGrade_name(rs.getString("grade_name"));
					data.setFile_location(rs.getString("file_location"));
				}
			}else{
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);			
				while(rs.next()){
					data.setFile_name(rs.getString("file_name"));
					data.setPhone_number(rs.getString("phone_number"));	
					data.setGrade_name(rs.getString("grade_name"));
					data.setFile_location(rs.getString("file_location"));
				}
			}
		} catch (SQLException e) {
			LogUtil.error("Invide SQL Statement:\n", SKIP_Logger);
			throw e;
		} finally{
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return data;
	}
    
    public static String getScoreState( Connection conn,  String gradeId,String isRecord )
	throws SQLException 	
{
    	GradeData data = new GradeData();	
		Statement stmt = null;
		ResultSet rs = null;	
		String score_state ="";
		
		String sql = "select * from SU_QC_SOURCE_RECORD_DATA"+
			" where file_name='"+gradeId+".mp3'";  
		
		String sql1 ="select * from SU_QC_SOURCE_RECORD_DATA"+
		" where file_name='"+gradeId+".html'";  
	
		try {
			if(isRecord.equals("txt")){
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql1);			
				while(rs.next()){
					score_state = rs.getString("score_state");
				}
			}else{
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);			
				while(rs.next()){
					score_state = rs.getString("score_state");
				}
			}
		} catch (SQLException e) {
			LogUtil.error("Invide SQL Statement:\n", SKIP_Logger);
			throw e;
		} finally{
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return score_state;
	}
    
    public static GradeData queryAdminAgentRecordInfo( Connection conn, String gradeId )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		//sql = "select use_grade_index from T_GRADE_DETAIL where grade_id="+gradeId;	
		sql = "select use_grade_index,file_name,file_location from T_GRADE_DETAIL_NEW where grade_id='"+gradeId+"'";
		
		GradeData data = new GradeData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {				
				/*data.taskEndTime = rs.getString("task_end_dt");	*/			 
				//data.useGradeIndex = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
				data.useGradeIndex = rs.getInt("use_grade_index" )+"";
				//data.useGradeIndexName = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
				data.file_name=rs.getString("file_name" );
				data.file_location=rs.getString("file_location" );
			}
		} catch (SQLException e) {
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
		return data;
	}
    
    public static String queryUserArgs( String userName )
	throws SQLException 	
	{
    	Connection con=null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		String userArgs = "";
		//sql = "select use_grade_index from T_GRADE_DETAIL where grade_id="+gradeId;	
		sql = "select name from suSecurityUser where loginName='"+userName+"'";
		
		try {
			String securityDBId=ConfInfo.confMapDBConf.get("securityDBId");
			con=DAOTools.getConnectionOutS(securityDBId);
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {				
				userArgs = rs.getString("name" );
			}
		} catch (SQLException e) {
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
		return userArgs;
	}
    
    public static String queryAdminAgentName( Connection conn, String useGradeIndex )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String gradeName = "";
		String sql = "";
		//sql = "select use_grade_index from T_GRADE_DETAIL where grade_id="+gradeId;	
		sql = "select grade_name from SA_QC_GRADE_DICTINFO where id='"+useGradeIndex+"'";
		
		GradeData data = new GradeData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {				
				/*data.taskEndTime = rs.getString("task_end_dt");	*/			 
				//data.useGradeIndex = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
//				data.useGradeIndex = rs.getInt("use_grade_index" )+"";
				//data.useGradeIndexName = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
				/*data.grade_name=rs.getString("grade_name" );
				data.setGrade_name(rs.getString("grade_name" ));*/
				gradeName = rs.getString("grade_name" );
//				data.file_location=rs.getString("file_location" );
			}
		} catch (SQLException e) {
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
		return gradeName;
	}
    
    public static String queryRecordGradeRemark( Connection conn, String gradeId )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String gradeRemark= "";
		String sql = "";
		//sql = "select use_grade_index from T_GRADE_DETAIL where grade_id="+gradeId;	
		sql = "select grade_remark from T_GRADE_DETAIL_NEW where grade_id='"+gradeId+"' and grade_remark !='null'";
		
		GradeData data = new GradeData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {				
				/*data.taskEndTime = rs.getString("task_end_dt");	*/			 
				//data.useGradeIndex = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
//				data.useGradeIndex = rs.getInt("use_grade_index" )+"";
				//data.useGradeIndexName = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
				/*data.grade_name=rs.getString("grade_name" );
				data.setGrade_name(rs.getString("grade_name" ));*/
				gradeRemark = rs.getString("grade_remark" );
//				data.file_location=rs.getString("file_location" );
			}
		} catch (SQLException e) {
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
		return gradeRemark;
	}
    public static String queryRecordGradeRemarkFJ( Connection conn, String gradeId )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String gradeRemarkfj= "";
		String sql = "";
		//sql = "select use_grade_index from T_GRADE_DETAIL where grade_id="+gradeId;	
		sql = "select grade_remark_fj from T_GRADE_DETAIL_NEW where grade_id='"+gradeId+"' ";
		
		GradeData data = new GradeData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {				
				/*data.taskEndTime = rs.getString("task_end_dt");	*/			 
				//data.useGradeIndex = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
//				data.useGradeIndex = rs.getInt("use_grade_index" )+"";
				//data.useGradeIndexName = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
				/*data.grade_name=rs.getString("grade_name" );
				data.setGrade_name(rs.getString("grade_name" ));*/
				gradeRemarkfj = rs.getString("grade_remark_fj" );
//				data.file_location=rs.getString("file_location" );
			}
		} catch (SQLException e) {
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
		return gradeRemarkfj;
	}
    
    public static String queryAgentRecordGradeRemark( Connection conn, String gradeId )
	throws SQLException 	
	{
		Statement stmt = null;
		ResultSet rs = null;
		String agentRemark= "";
		String sql = "";
		//sql = "select use_grade_index from T_GRADE_DETAIL where grade_id="+gradeId;	
		sql = "select agent_user_remark from T_GRADE_DETAIL_NEW where grade_id='"+gradeId+"'";
		
		GradeData data = new GradeData();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {				
				/*data.taskEndTime = rs.getString("task_end_dt");	*/			 
				//data.useGradeIndex = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
//				data.useGradeIndex = rs.getInt("use_grade_index" )+"";
				//data.useGradeIndexName = WorkPublicDAO.getGradeIndexNameById( conn, logger,  rs.getInt("use_grade_index") );
				/*data.grade_name=rs.getString("grade_name" );
				data.setGrade_name(rs.getString("grade_name" ));*/
				agentRemark = rs.getString("agent_user_remark" );
//				data.file_location=rs.getString("file_location" );
			}
		} catch (SQLException e) {
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
		return agentRemark;
	}
    
    public static boolean updateAgentArgumentScoreState(Connection conn, String gradeId ,String isRecord) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql= "update SU_QC_SOURCE_RECORD_DATA set score_state=\""+"已上诉"+"\"  where file_name='"+gradeId+".mp3'";
        String sql1= "update SU_QC_SOURCE_RECORD_DATA set score_state=\""+"已上诉"+"\"  where file_name='"+gradeId+"html'";
		try {
			if(isRecord.equals("txt")){
				stmt = conn.createStatement();
				stmt.executeUpdate(sql1);
			}else{
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    public static boolean updateAgentArgumentScoreState2(Connection conn, String gradeId) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql= "update sa_agent_argument set score_state=\"上诉成功\"  where grade_id='"+gradeId+"'";
        //String sql1= "update sa_agent_argument set score_state=\""+"已上诉"+"\"  where file_name='"+gradeId+"html'";
		try {
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
		} catch (SQLException e) {
			LogUtil.error("Invalid sql ", SKIP_Logger);
			throw e;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (rs != null) {
					rs.close();
				}						
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true;
    } 
    
    
    public static int getItemSum( FirstCheckForm form )
	throws SQLException 	
	{
    	int sumkeyInt =0 ;
    	Map map = form.getValuesMap();
         Set set = map.entrySet();
           Iterator it = set.iterator();	
           int x = 0;
           int y = 0;
           while( it.hasNext() ){
               Map.Entry me = (Map.Entry)it.next();
               String keyStr = (String)me.getKey();
               String[] a = keyStr.split("_");
               String num = a[0];
               String parentId= WorkPublicDAO.getParentid(num);
               String key = (String) me.getValue();
               if(key =="未使用" ||key.equals("未使用")){
               	key ="0.0";
	            }
               double keyInt = Double.parseDouble(key); 
               y++;
               if(WorkPublicDAO.getDescription(parentId) && keyInt>=0.0){
               	continue;
               }else{
	                if(keyInt<=0.0){
	                	x++;
	                }
               }
               sumkeyInt =(int) (sumkeyInt+ keyInt);
           }
           return sumkeyInt;
	} 
}
