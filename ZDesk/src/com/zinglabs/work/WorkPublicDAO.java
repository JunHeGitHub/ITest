

package com.zinglabs.work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.Constants;
import com.zinglabs.util.Utility;
import com.zinglabs.work.action.FirstCheckForm;


public class WorkPublicDAO {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
	
    
    
    public static String getGradeRemarkNew( Connection conn, String gradeId
    		 )
    	throws SQLException 	
    {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String remark = null;
       

        try{
   String sql = "select grade_remark from T_GRADE_DETAIL_NEW where grade_id=? ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gradeId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                remark = rs.getString("grade_remark");
            }

        }catch(SQLException e){
        	LogUtil.error(e, SKIP_Logger);
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
			}
        }

        return remark;
    }
    
    public static String getGradeRemarkNewFJ( Connection conn,String gradeId
    		 )
    	throws SQLException 	
    {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String remark = null;

        try{
            String sql = "select grade_remark_fj from T_GRADE_DETAIL_FUJIAN_NEW where grade_id=? ";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, gradeId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                remark = rs.getString("grade_remark_fj");
            }

        }catch(SQLException e){
        	LogUtil.error(e, SKIP_Logger);
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
			}
        }

        return remark;
    }
    
    
    
	public static String getGradeIndexNameById( Connection conn,int useGradeIndex ) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		
		String res = "";
		String sql = "select grade_name from SA_QC_GRADE_DICTINFO where grade_index="+useGradeIndex;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				res = rs.getString("grade_name");
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
			}
		}
		return res;
	}	
		
    public static int getUseGradeTableIndex(Connection conn,String qcUser) throws SQLException{
        if(qcUser == null || qcUser.length() == 0)
            return 1;
        
		Statement stmt = null;
		ResultSet rs = null;
        int gradeIndex = 1;
        
		String sql = "select grade_index from SA_QC_USER_GRADE_TABLE where qc_user='"+qcUser+"'"; 
        
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				gradeIndex = rs.getInt("grade_index");        
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
		return gradeIndex;
    }	
    
    public static ArrayList queryGradeScoreNewFujian(Connection conn, String gradeId) 
    throws SQLException {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    ArrayList list = null;
	
	    try{
	        String sql = "select * from T_GRADE_DETAIL_FUJIAN_NEW where grade_id = '"+gradeId+"'";
	
	        pstmt = conn.prepareStatement(sql);
	       
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
	                LogUtil.debug("queryGradeScoreNewFujian "+i+" "+pfx, SKIP_Logger);
	                list.add(pfx);
	            }
	            //sbOneTicket.append(rs.getInt("pfx_oneticket"));
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
    
    public static ArrayList queryGradeScoreNewFushen(Connection conn, String gradeId) 
    throws SQLException {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    ArrayList list = null;
	
	    try{
	        String sql = "select * from T_GRADE_DETAIL_FUSHEN_NEW where grade_id = '"+gradeId+"'";
	
	        pstmt = conn.prepareStatement(sql);
	       
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
	            //sbOneTicket.append(rs.getInt("pfx_oneticket"));
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
    
    
    public static ArrayList queryGradeScoreNew(Connection conn, String gradeId) 
    throws SQLException {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    ArrayList list = null;
	
	    try{
	        String sql = "select * from T_GRADE_DETAIL_NEW where grade_id = '"+gradeId+"'";
	
	        pstmt = conn.prepareStatement(sql);
	       
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
	            //sbOneTicket.append(rs.getInt("pfx_oneticket"));
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
    
    public static boolean isValidValue(Connection conn, String key, String value, GradeData data){
        if(key == null || key.length() == 0)
            return false;

        float input_val = Float.parseFloat(value);
        
		Statement stmt = null;
		ResultSet rs = null;
        int count = 0;
        
		String sql = "select min_value, max_value ,DESCRIPTION,PARENTID from dictinfo where ID="+key;
        
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
                data.setPfxDescription(rs.getString("DESCRIPTION"));
                data.setPfxParentId(rs.getString("PARENTID"));
                float min_val = rs.getFloat("min_value");
                float max_val = rs.getFloat("max_value");
                data.setPfxMinVal(min_val);
                data.setPfxMaxVal(max_val);

                if( input_val < min_val || input_val > max_val){
                    return false;
                }else
                    return true;
			}
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			return false;
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
				return false;
			}
		}

		return false; 
    }

    public static String getPfxMinMaxValue( Connection conn, int key, StringBuffer valueRemark )
    {
        
        PreparedStatement pstmt = null;
               
		ResultSet rs = null;
        String res = "";
		String sql = "select min_value, max_value, value_remark from dictinfo where ID=?";
        
		try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, key+"");
            rs = pstmt.executeQuery();

			if(rs.next()){
                res = rs.getFloat("min_value")+"--"+rs.getFloat("max_value");
                String remark = rs.getString("value_remark");
                if( remark != null && !remark.equals("") )
                {
                	 valueRemark.append(remark);
                }                
			}
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			return "";
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				return "";
			}
		}
		return res; 
    } 

    public static int QueryRSMaxValue(Integer id){
    	Statement stmt = null;
		ResultSet rs = null;
        
		String sql = "select max_value from dictinfo where ID="+id;
		Connection conn = null;
        int maxValue = 0;
        try {
			conn = DAOTools.getConnectionOutS("ZQC");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				maxValue = rs.getInt("max_value");
			}
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
			}
		}
		return maxValue; 
    }
    public static ArrayList getPfx1Pfx2MinMaxValue( Connection conn, int key, StringBuffer valueRemark )
    {
        PreparedStatement pstmt = null;
        
        ArrayList resList=new ArrayList();
               
		ResultSet rs = null;
        String res = "";
		String sql = "select description,min_value, max_value,reference_value,reference2_value, PARENTID from dictinfo where ID=?";
		try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, key+"");
            rs = pstmt.executeQuery();
			while(rs.next()){
				String a =rs.getString("description").trim();
				String parentId = rs.getString("PARENTID").trim();
				boolean description = WorkPublicDAO.getDescription(parentId);
				
				/*if(a.equals("开头/结束语") ){
					if(!(rs.getString("max_value").equals("")) || rs.getString("max_value") != null)
						resList.add(rs.getFloat("max_value"));
					if(!(rs.getString("min_value").equals("")) || rs.getString("min_value") != null)
						resList.add(rs.getFloat("min_value"));
				}else if(a.equals("倾听理解") || a.equals("电话控制能力")){
					if(!(rs.getString("reference_value").equals("")) || rs.getString("reference_value") != null)
						resList.add(rs.getFloat("reference_value"));
					if(!(rs.getString("min_value").equals("")) || rs.getString("min_value") != null)
						resList.add(rs.getFloat("min_value"));
					if(!(rs.getString("max_value").equals("")) || rs.getString("max_value") != null)
						resList.add(rs.getFloat("max_value"));
				}else if(a.equals("引导能力")){
					if(!(rs.getString("reference_value").equals("")) || rs.getString("reference_value") != null)
						resList.add(rs.getFloat("reference_value"));
					if(!(rs.getString("min_value").equals("")) || rs.getString("min_value") != null)
						resList.add(rs.getFloat("min_value"));
					if(!(rs.getString("max_value").equals("")) || rs.getString("max_value") != null)
						resList.add(rs.getFloat("max_value"));
				}else if(a.equals("亲切感")){
					if(!(rs.getString("reference_value").equals("")) || rs.getString("reference_value") != null)
						resList.add(rs.getFloat("reference_value"));
					if(!(rs.getString("reference2_value").equals("")) || rs.getString("reference2_value") != null)
						resList.add(rs.getFloat("reference2_value"));
					if(!(rs.getString("min_value").equals("")) || rs.getString("min_value") != null)
						resList.add(rs.getFloat("min_value"));
					if(!(rs.getString("max_value").equals("")) || rs.getString("max_value") != null)
						resList.add(rs.getFloat("max_value"));
				}else if(a.equals("表达能力")){
					if(!(rs.getString("reference2_value").equals("")) || rs.getString("reference2_value") != null)
						resList.add(rs.getFloat("reference2_value"));
					if(!(rs.getString("reference_value").equals("")) || rs.getString("reference_value") != null)
						resList.add(rs.getFloat("reference_value"));
					if(!(rs.getString("min_value").equals("")) || rs.getString("min_value") != null)
						resList.add(rs.getFloat("min_value"));
					if(!(rs.getString("max_value").equals("")) || rs.getString("max_value") != null)
						resList.add(rs.getFloat("max_value"));
				}else if(a.equals("业务知识") || a.equals("解决能力")){
					if(!(rs.getString("reference_value").equals("")) || rs.getString("reference_value") != null)
						resList.add(rs.getFloat("reference_value"));
					if(!(rs.getString("reference2_value").equals("")) || rs.getString("reference2_value") != null)
						resList.add(rs.getFloat("reference2_value"));
					if(!(rs.getString("min_value").equals("")) || rs.getString("min_value") != null)
						resList.add(rs.getFloat("min_value"));
					if(!(rs.getString("max_value").equals("")) || rs.getString("max_value") != null)
						resList.add(rs.getFloat("max_value"));
				}else if(a.trim().equals("服务营销整体意识感觉")){
					if(!(rs.getString("reference_value").equals("")) || rs.getString("reference_value") != null)
						resList.add(rs.getFloat("reference_value"));
					if(!(rs.getString("min_value").equals("")) || rs.getString("min_value") != null)
						resList.add(rs.getFloat("min_value"));
					if(!(rs.getString("max_value").equals("")) || rs.getString("max_value") != null)
						resList.add(rs.getFloat("max_value"));
					System.out.println("start=============");
					for(int i=0;i<resList.size();i++){
						logger.debug("resList========"+resList.get(i));
						System.out.println("resList========"+resList.get(i));
					}
					System.out.println("end=============");
					logger.debug("end=============");
				}else if(a.trim().equals("开场白/结束语") || a.equals("匹配") || a.equals("应变解决") || a.equals("引导力")){
					if(!(rs.getString("reference_value").equals("")) || rs.getString("reference_value") != null)
						resList.add(rs.getFloat("reference_value"));
					if(!(rs.getString("reference2_value").equals("")) || rs.getString("reference2_value") != null)
						resList.add(rs.getFloat("reference2_value"));
					if(!(rs.getString("min_value").equals("")) || rs.getString("min_value") != null)
						resList.add(rs.getFloat("min_value"));
					if(!(rs.getString("max_value").equals("")) || rs.getString("max_value") != null)
						resList.add(rs.getFloat("max_value"));
				}else if(a.trim().equals("控制力") || a.equals("营销能力") || a.equals("营销态度") || a.equals("营销结果")){
					if(!(rs.getString("reference_value").equals("")) || rs.getString("reference_value") != null)
						resList.add(rs.getFloat("reference_value"));
					if(!(rs.getString("min_value").equals("")) || rs.getString("min_value") != null)
						resList.add(rs.getFloat("min_value"));
					if(!(rs.getString("max_value").equals("")) || rs.getString("max_value") != null)
						resList.add(rs.getFloat("max_value"));
				}else{*/
				String max_value = rs.getString("max_value").trim();
				String min_value = rs.getString("min_value").trim();
				String reference_value = rs.getString("reference_value").trim();
				String reference2_value = rs.getString("reference2_value").trim();
				if(description){
					if((!(max_value.equals("")) || max_value != null) && max_value.equals("0")){
						resList.add(rs.getFloat("max_value"));
						if(!max_value.equals(reference_value) ){
							if(!(reference_value.equals("")) || reference_value != null)
								resList.add(rs.getFloat("reference_value"));
						}
						if(!reference_value.equals(reference2_value) && !reference2_value.equals(max_value)){
							if(!(reference2_value.equals("null")) )
								resList.add(rs.getFloat("reference2_value"));
						}
						if(!min_value.equals(reference2_value) && !min_value.equals(reference_value) &&  !min_value.equals(max_value)){
							if(!(min_value.equals("")) || min_value != null)
								resList.add(rs.getFloat("min_value"));
						}
					}else if(reference_value.equals("0") && (!(reference_value.equals("")) || reference_value != null)){
						resList.add(rs.getFloat("reference_value"));
						if(!max_value.equals(reference_value) ){
							if(!(max_value.equals("")) || max_value != null)
								resList.add(rs.getFloat("max_value"));
						}
						if(!reference_value.equals(reference2_value) && !reference2_value.equals(max_value)){
							if(!(reference2_value.equals("null")) )
								resList.add(rs.getFloat("reference2_value"));
						}
						if(!min_value.equals(reference2_value) && !min_value.equals(reference_value) &&  !min_value.equals(max_value)){
							if(!(min_value.equals("")) || min_value != null)
								resList.add(rs.getFloat("min_value"));
						}
					}else if(reference2_value.equals("0") && (!(reference2_value.equals("null")) )){
						resList.add(rs.getFloat("reference2_value"));
						if(!reference2_value.equals(max_value)){
							if(!(max_value.equals("")) || max_value != null)
								resList.add(rs.getFloat("max_value"));
						}
						if(!max_value.equals(reference_value) && !reference_value.equals(reference2_value)  ){
							if(!(reference_value.equals("")) || reference_value != null)
								resList.add(rs.getFloat("reference_value"));
						}
						if(!min_value.equals(reference2_value) && !min_value.equals(reference_value) &&  !min_value.equals(max_value)){
							if(!(min_value.equals("")) || min_value != null)
								resList.add(rs.getFloat("min_value"));
						}
					}else if(min_value.equals("0")){
						resList.add(rs.getFloat("min_value"));
						if( !min_value.equals(max_value)){
							if(!(max_value.equals("")) || max_value != null)
								resList.add(rs.getFloat("max_value"));
						}
						if(!max_value.equals(reference_value) && min_value.equals(reference_value)){
							if(!(reference_value.equals("")) || reference_value != null)
								resList.add(rs.getFloat("reference_value"));
						}
						if(!reference_value.equals(reference2_value) && !reference2_value.equals(max_value) && min_value.equals(reference2_value)){
							if(!(reference2_value.equals("null")) )
								resList.add(rs.getFloat("reference2_value"));
						}
					}else{
						if(!(rs.getString("max_value").equals("")) || rs.getString("max_value") != null)
							resList.add(rs.getFloat("max_value"));
						if(!rs.getString("max_value").equals(rs.getString("reference_value")) ){
							if(!(rs.getString("reference_value").equals("")) || rs.getString("reference_value") != null)
								resList.add(rs.getFloat("reference_value"));
						}
						if(!rs.getString("reference_value").equals(rs.getString("reference2_value")) && !rs.getString("reference2_value") .equals(rs.getString("max_value"))){
							if(!(rs.getString("reference2_value").equals("null")) )
								resList.add(rs.getFloat("reference2_value"));
						}
						if(!rs.getString("min_value").equals( rs.getString("reference2_value")) && !rs.getString("min_value").equals(rs.getString("reference_value")) &&  !rs.getString("min_value").equals( rs.getString("max_value"))){
							if(!(rs.getString("min_value").equals("")) || rs.getString("min_value") != null)
								resList.add(rs.getFloat("min_value"));
						}
					}
				}else{
					if(a.equals("开头/结束语") ){
						if(!(rs.getString("max_value").equals("")) || rs.getString("max_value") != null)
							resList.add(rs.getFloat("max_value"));
						if(!(rs.getString("min_value").equals("")) || rs.getString("min_value") != null)
							resList.add(rs.getFloat("min_value"));
					}else{
						if(!(rs.getString("max_value").equals("")) || rs.getString("max_value") != null)
							resList.add(rs.getFloat("max_value"));
						if(!rs.getString("max_value").equals(rs.getString("reference_value")) ){
							if(!(rs.getString("reference_value").equals("")) || rs.getString("reference_value") != null)
								resList.add(rs.getFloat("reference_value"));
						}
						if(!rs.getString("reference_value").equals(rs.getString("reference2_value")) && !rs.getString("reference2_value") .equals(rs.getString("max_value"))){
							if(!(rs.getString("reference2_value").equals("null")) )
								resList.add(rs.getFloat("reference2_value"));
						}
						if(!rs.getString("min_value").equals( rs.getString("reference2_value")) && !rs.getString("min_value").equals(rs.getString("reference_value")) &&  !rs.getString("min_value").equals( rs.getString("max_value"))){
							if(!(rs.getString("min_value").equals("")) || rs.getString("min_value") != null)
								resList.add(rs.getFloat("min_value"));
						}
					}
				}
				
//				resList.add("未使用");
				/*	HashSet set = new HashSet(resList);       
					resList.clear();       
					resList.addAll(set); */
				}
			//}
//			logger.debug("getPfx1Pfx2MinMaxValue end");
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
			}
		}
//		logger.debug("11111111111getPfx1Pfx2MinMaxValue end");
		return resList; 
    }   
    
    public static String getParentid(String id )
    {
        PreparedStatement pstmt = null;
        String parentId="";
		ResultSet rs = null;
        String res = "";
		String sql = "select PARENTID from dictinfo where ID=?";
		Connection conn = null;
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id+"");
            rs = pstmt.executeQuery();
            
			while(rs.next()){
				parentId = rs.getString("PARENTID").trim();
				}
			//}
//			logger.debug("getPfx1Pfx2MinMaxValue end");
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
			}
		}
//		logger.debug("11111111111getPfx1Pfx2MinMaxValue end");
		return parentId; 
    }   
    
    public static Boolean getDescription( String key )
    {
        if(key == null || key.length() == 0)
            return false;

		Statement stmt = null;
		ResultSet rs = null;
        
		String sql = "select DESCRIPTION from dictinfo where ID="+key;
		Connection conn = null;
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
                String description = rs.getString("DESCRIPTION");
                String[] x = description.split("附加");
                System.out.println("x.length = "+x.length);
                if(x.length>1||description.equals("附加")){
                	return true;
                }else{
                	return false;
                }
                
                /*if(description.equals("附加项") || description.equals("附加") || description.equals("附加1")){
                	return true;
                }else{
                	return false;
                }*/
			}
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
			}
		}

		return false; 
    }      
    
    
    public static String getPfxParentDescription( Connection conn, String key )
    {
        if(key == null || key.length() == 0)
            return "";

		Statement stmt = null;
		ResultSet rs = null;
        
		String sql = "select DESCRIPTION from dictinfo where ID="+key;
        
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
                return rs.getString("DESCRIPTION");
			}
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
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
			}
		}

		return ""; 
    }         
    
	
	
	public static String getGradeIndexNameById( String gradeId )
	{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String sql = "select grade_name from SA_QC_GRADE_DICTINFO where grade_index="+gradeId;
		
		String title = "";
		try {
			try {
				conn = DAOTools.getConnectionOutS("ZQC");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogUtil.error(e, SKIP_Logger);
			}
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);			
			if(rs.next())
			{
				title = rs.getString("grade_name");			
			}
		} catch (SQLException e) {			
			LogUtil.error(e, SKIP_Logger);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}				
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null)
					DAOTools.releaseConnectionOutS("ZQC", conn);
			} catch (SQLException e) {				
				LogUtil.error(e, SKIP_Logger);
			}
		}
		return title;
	}  	
	
	
	
    public static String getPfxPercentValue( Connection conn, int key )
    {
        
        PreparedStatement pstmt = null;
        
		ResultSet rs = null;
        String res = "";
		String sql = "select percent from dictinfo where ID=?";
        
		try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, key);
            rs = pstmt.executeQuery();

			if(rs.next()){
                res = rs.getFloat("percent")+"";
			}
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			return "";
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
				return "";
			}
		}
		return res; 
    }   
   
	public static boolean isGradeIndexLeaf( Connection conn, String useGradeIndex, int id )
	throws SQLException
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select distinct(PARENTID) from dictinfo where grade_index = '" + useGradeIndex + "'";
		try{
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
			int m = 0;
			while(rs.next()){
				m = rs.getInt("PARENTID");
				if(id == m){					
					return false ;
				}
			}
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			throw e;			
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {		
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true; 
	}	
	
	public static boolean isGradeIndexLeaf1( Connection conn, String useGradeIndex, int id )
	throws SQLException
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select distinct(TEACH_DIR_PARENTID) from T_TEACH_LIB_DIR where teach_id = '" + useGradeIndex + "'";
		try{
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
			int m = 0;
			while(rs.next()){
				m = rs.getInt("TEACH_DIR_PARENTID");
				if(id == m){					
					return false ;
				}
			}
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			throw e;			
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {		
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return true; 
	}	
	
	public static double getGradeIndexCurpercent( Connection conn, int id )
	throws SQLException{
		double ret = 1;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select percent from dictinfo where id = "+ id;
		try{
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while( rs.next() )
            {
				int cPer = rs.getInt("percent");
				if( cPer > 0 )
					ret = cPer;
					//ret = (cPer * 0.01);	
            }
		} catch (SQLException e) {
			LogUtil.error(e, SKIP_Logger);
			throw e;			
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {		
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return ret; 
	}
	
	public static double getGradeIndexPoint( Connection conn, int id, Double dbScore, int caculateType )
	throws SQLException
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select IDPATH, max_value, min_value, reference_value,reference2_value, percent from dictinfo where id = " + id;
		
		double pointResult = 0;
		try{
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

			String idpath = "" ;
			double curPerCount = 1;
			double parPerCount = 1;
			
			String [] idpathArr = null;
			while(rs.next())
			{
				/*parPerCount = Double.valueOf(rs.getString("max_value"));
				double per = Double.valueOf(rs.getString("percent"));
				double count = parPerCount * per;
				if(count == -0.00){
					parPerCount = 0.00;
					per = 0.00;
				}
				if(dbScore==0){
					break;
				}*/
				pointResult = dbScore;
				//if(parPerCount * per !=0)
				//pointResult +=dbScore /  parPerCount * per;
			//	else
				//	pointResult =-1000;
			}
		}catch(SQLException e){
			LogUtil.error(e, SKIP_Logger);
			throw e;
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {	
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return pointResult; 
	}	
	
	public static double getGradeIndexPointSum( Connection conn, int id,  int caculateType )
	throws SQLException
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select sum(percent) percentSum from dictinfo where grade_index in (select grade_index from dictinfo where id="+id+")and parentid=0 ";
		
		double pointResult = 0;
		try{
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

			String idpath = "" ;
			double curPerCount = 1;
			double parPerCount = 1;
			
			String [] idpathArr = null;
			while(rs.next())
			{
				pointResult = Double.valueOf(rs.getString("percentSum"));
			}
		}catch(SQLException e){
			LogUtil.error(e, SKIP_Logger);
			throw e;
		}finally{
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {		
				LogUtil.error(e, SKIP_Logger);
				throw e;
			}
		}
		return pointResult; 
	}	
	public static void setGradeFlagNoUseNew( Connection conn,
    		String grade_id) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
        String sql = "update T_GRADE_DETAIL_NEW set ";
    	
		try {
            sql += " grade_flag="+Constants.GRADE_FLAG_NOUSE;   
            sql += " where grade_id='"+grade_id+"'";
            //sql += " where grade_id="+grade_id;
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
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
    }  
	 public static String queryFileNameNew(Connection conn, String gradeId) 
	    throws SQLException {
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    ArrayList list = null;
		    String file_name = "";
		    try{
		        String sql = "select file_name from T_GRADE_DETAIL where grade_id ='"+gradeId+"'";
		        //sql = "select file_name from T_GRADE_DETAIL where grade_id ="+gradeId;
		        pstmt = conn.prepareStatement(sql);
		        list = new ArrayList();
		        rs = pstmt.executeQuery();
		        if(rs.next()){
		        	file_name = rs.getString("file_name");
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
		    return file_name;
		}
	 public static String queryFileLocationNew(Connection conn, String gradeId) 
	    throws SQLException {
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    ArrayList list = null;
		    String file_location = "";
		    try{
		        String sql = "select file_location from T_GRADE_DETAIL where grade_id ='"+gradeId+"'";
		        //sql = "select file_location from T_GRADE_DETAIL where grade_id ="+gradeId
		        pstmt = conn.prepareStatement(sql);
		        list = new ArrayList();
		        rs = pstmt.executeQuery();
		        if(rs.next()){
		        	file_location = rs.getString("file_location");
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
		    return file_location;
		}
	 public static FirstCheckForm queryGradeFormNew(Connection conn, String gradeId) 
	    throws SQLException {
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    ArrayList list = null;
		    FirstCheckForm form = new FirstCheckForm();
		    try{
		        String sql = "select * from T_GRADE_DETAIL where grade_id ='"+gradeId+"'";
		        //sql = "select * from T_GRADE_DETAIL where grade_id ="+gradeId;
		        pstmt = conn.prepareStatement(sql);
		        list = new ArrayList();
		        rs = pstmt.executeQuery();
		        if(rs.next()){
		        	form.setGradeId(rs.getString("grade_id"));
		        	form.setCallerNumber(rs.getString("caller_number"));
		        	form.setFileName(rs.getString("file_name"));
		        	form.setFileLocation(rs.getString("file_location"));
		        	form.setPhoneNumber(rs.getString("phone_number"));
		        	form.setGradeRemark(rs.getString("grade_remark"));
		        	form.setUseGradeIndex(rs.getString("use_grade_index"));
		        	form.setPfxTotal(rs.getString("pfx_total"));
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
		    return form;
		}
	 public static String getTeachGradeRemarkNew( Connection conn,String gradeId
	 ) throws SQLException 	
	{
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String remark = null;
	
	try{
	    String sql = "select grade_remark from T_GRADE_DETAIL_NEW where grade_id='"+gradeId+"' and grade_remark!='null'";
	
	    pstmt = conn.prepareStatement(sql);
//	    pstmt.setString(1, gradeId);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
	        remark = rs.getString("grade_remark");
	    }
	
	}catch(SQLException e){
		LogUtil.error(e, SKIP_Logger);
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
		}
	}
	
	return remark;
	}
	 
	 public static String getTeachGradeRemarkNewFJ( Connection conn,String gradeId
	 ) throws SQLException 	
	{
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String remark_fj = null;
	
	try{
	    String sql = "select grade_remark_fj from T_GRADE_DETAIL_FUJIAN_NEW where grade_id='"+gradeId+"'";
	
	    pstmt = conn.prepareStatement(sql);
//	    pstmt.setString(1, gradeId);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
	    	remark_fj = rs.getString("grade_remark_fj");
	    }
	
	}catch(SQLException e){
		LogUtil.error(e, SKIP_Logger);
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
		}
	}
	
	return remark_fj;
	}
	 
	 public static ArrayList queryAdminAgentInfo(Connection conn, String gradeId) 
	    throws SQLException {
		    PreparedStatement pstmt = null;
		    ResultSet rs = null;
		    ArrayList list = null;
		
		    try{
		        String sql = "select * from T_GRADE_DETAIL_NEW where grade_id = '"+gradeId+"'";
		
		        pstmt = conn.prepareStatement(sql);
		       
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
		            //sbOneTicket.append(rs.getInt("pfx_oneticket"));
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
	 
	 public static String getAgentArguRemark( Connection conn,String gradeId
	 ) throws SQLException 	
	{
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String agentRemark = null;
	
	try{
	    String sql = "select agent_user_remark from SA_AGENT_ARGUMENT where grade_id='"+gradeId+"'";
	
	    pstmt = conn.prepareStatement(sql);
//	    pstmt.setString(1, gradeId);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
	    	agentRemark = rs.getString("agent_user_remark");
	    }
	
	}catch(SQLException e){
		LogUtil.error(e, SKIP_Logger);
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
		}
	}
	return agentRemark;
	}
	 
	 public static String getAdminRemark( Connection conn,String gradeId
	 ) throws SQLException 	
	{
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String adminRemark = null;
	
	try{
	    String sql = "select admin_remark from SA_AGENT_ARGUMENT where grade_id='"+gradeId+"'";
	
	    pstmt = conn.prepareStatement(sql);
//	    pstmt.setString(1, gradeId);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
	    	adminRemark = rs.getString("admin_remark");
	    }
	
	}catch(SQLException e){
		LogUtil.error(e, SKIP_Logger);
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
		}
	}
	return adminRemark;
	}
	 
	 public static String getFuJianRemark( Connection conn,String gradeId
	 ) throws SQLException 	
	{
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String fujianRemark = null;
	
	try{
	    String sql = "select grade_remark from SA_AGENT_ARGUMENT where grade_id='"+gradeId+"'";
	
	    pstmt = conn.prepareStatement(sql);
//	    pstmt.setString(1, gradeId);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
	    	fujianRemark = rs.getString("grade_remark");
	    }
	
	}catch(SQLException e){
		LogUtil.error(e, SKIP_Logger);
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
		}
	}
	
	return fujianRemark;
	}
	 
	 public static String getAdminAgentArguRemark( Connection conn,String gradeId
	 ) throws SQLException 	
	{
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String remark = null;
	
	try{
	    String sql = "select grade_remark from T_GRADE_DETAIL_NEW where grade_id=?";
	
	    pstmt = conn.prepareStatement(sql);
	    pstmt.setString(1, gradeId);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
	        remark = rs.getString("grade_remark");
	    }
	
	}catch(SQLException e){
		LogUtil.error(e, SKIP_Logger);
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
		}
	}
	
	return remark;
	}
	 
	 public static String getAdminAgentGradeRemarkNew( Connection conn,String gradeId
	 ) throws SQLException 	
	{
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String adminRemark = null;
	
	try{
	    String sql = "select admin_remark from SA_AGENT_ARGUMENT where grade_id='"+gradeId+"'";
	
	    pstmt = conn.prepareStatement(sql);
//	    pstmt.setString(1, gradeId);
	    rs = pstmt.executeQuery();
	    if(rs.next()){
	    	adminRemark = rs.getString("admin_remark");
	    }
	
	}catch(SQLException e){
		LogUtil.error(e, SKIP_Logger);
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
		}
	}
	return adminRemark;
	}
}
