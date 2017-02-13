package com.zinglabs.work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.util.MessageResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.log.LogUtil;
import com.zinglabs.util.Constants;
import com.zinglabs.util.Utility;



public class WorkPublic {
    
	public static String getScoreChangeString( Connection conn, String useGradeIndex, Map scoreMap, ArrayList scoreList, boolean isOneTicketChange )
	throws SQLException
	{
		String res = "";
		MyTable helloWorld = new MyTable();
		ArrayList idlist = helloWorld.MyTestLeafs(useGradeIndex);	
		if( idlist.size() != scoreList.size() ) return res;
		
		int id = 0;
		if( isOneTicketChange )
			res = "1;";
		else
			res = "0;";
		
		for(int i=0;i < idlist.size(); i++ )
		{
			id = ((Integer)idlist.get(i)).intValue();
			if( scoreMap != null )
			{
                String key_name = id+"_"+i;	 
				String value = (String)scoreMap.get(key_name);
				if( value == null ) 
				{						
					return "";
				}
				else
				{
					String oldValue = (String)scoreList.get(i);
					Double dValue = new Double( value );
					Double dOldVal = new Double( oldValue );
					if( !dValue.equals( dOldVal ) )		
					{
						res += id+":"+ dOldVal +";";				
					}					
				}
			}							
		}	
		return res;
	}
	
	public static double getCountTotal( Connection conn, String useGradeIndex, Map scoreMap, int countType )
	throws SQLException
	{
		MyTable helloWorld = new MyTable();
		ArrayList idlist = helloWorld.MyTestLeafs(useGradeIndex);				
		int id = 0;
		double countResult = 0;
		int queryRS =0;
		int RS=0;
		Double dbvalue = null;
		for(int i=0;i < idlist.size(); i++ )
		{
			id = ((Integer)idlist.get(i)).intValue();
			if( scoreMap != null )
			{
                String key_name = id+"_"+i;	 
				String value = (String)scoreMap.get(key_name);
				if( value == null ) 
				{						
					return -1000;
				}else if(value =="0.0" || value.equals("0.0")||value =="未使用" ||value.equals("未使用")){
					if(value=="未使用" || value.equals("未使用")){
						value="0.0";
					}
					queryRS = WorkPublicDAO.QueryRSMaxValue(id);
					RS += queryRS;
					dbvalue = 0.0;
				}
				else
					dbvalue = new Double( value );
			}				
			countResult += WorkPublicDAO.getGradeIndexPoint( conn, id, dbvalue, countType );
		}	
		if(RS > 0){
			countResult = countResult*100/(100-RS);
		}
		if(RS ==100){
			countResult = 0.00;
		}
		//countResult = WorkPublicDAO.getGradeIndexPoint( conn, id, dbvalue, countType );
		return countResult;
	}
	
	public static double getCountTotalRS( Connection conn, String useGradeIndex, Map scoreMap, int countType )
	throws SQLException
	{
		MyTable helloWorld = new MyTable();
		ArrayList idlist = helloWorld.MyTestLeafs(useGradeIndex);				
		int id = 0;
		double countResult = 0;
		double queryRS =0;
		double RS=0;
		double dbvalue = 0;
		double atemp =0;
		for(int i=0;i < idlist.size(); i++ )
		{
			id = ((Integer)idlist.get(i)).intValue();
			if( scoreMap != null )
			{
                String key_name = id+"_"+i;	 
				String value = (String)scoreMap.get(key_name);
				int minValue=getMinPercentRS(conn,id);
				if( value == null ) 
				{						
					return -1000;
				}else if(value =="0" || value.equals("0")){
					if(value =="0" || value.equals("0")){
						value="0.0";
						dbvalue=0;
					}
//					queryRS = WorkPublicDAO.QueryRSMaxValue(id);
//					RS += queryRS*dbvalue;
				}else if( value =="" || value.equals("") ){
					dbvalue=0;
				}
				else{
						dbvalue = new Double( value );
				}
				queryRS = WorkPublicDAO.QueryRSMaxValue(id);
				if(dbvalue>queryRS){
					dbvalue=queryRS;
				}
				if(dbvalue<minValue){
					dbvalue=minValue;
				}
				if(value ==""|| value.equals("")){
					RS =RS+(queryRS/100)*atemp;
				}else
					RS =RS+(queryRS/100)+atemp;
			}				
			countResult += WorkPublicDAO.getGradeIndexPoint( conn, id, dbvalue, countType );
		}	
		if(RS > 0){
			countResult = countResult/(RS);
		}
		if(RS ==100){
			countResult = 0.00;
		}
		//countResult = WorkPublicDAO.getGradeIndexPoint( conn, id, dbvalue, countType );
		return countResult;
	}
	
	public static int getMinPercentRS( Connection conn, int id )
	throws SQLException
	{
		Statement stmt = null;
		ResultSet rs = null;
        int minValue = 0;
        
//		String sql = "select grade_index from SA_QC_USER_GRADE_TABLE where qc_user='"+qcUser+"'"; 
		String sql ="SELECT min_value FROM dictinfo WHERE id ='"+id+"'";
        
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				minValue = rs.getInt("min_value");        
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
		return minValue;
	}
	public static double getCountTotalNew( Connection conn, String useGradeIndex, Map scoreMap, int countType )
	throws SQLException
	{
		MyTableNew helloWorld = new MyTableNew();
		ArrayList idlist = helloWorld.MyTestLeafs(useGradeIndex);				
		int id = 0;
		double countResult = 0;
		System.out.println("aa");
		Double dbvalue = null;
		int queryRS=0;
		int RS=0;
		int y =0;
		for(int i=0;i < idlist.size(); i++ )
		{
			id = ((Integer)idlist.get(i)).intValue();
			if( scoreMap != null )
			{
                String key_name = id+"_"+i;	 
				String value = (String)scoreMap.get(key_name);
				if( value == null ) 
				{						
					return -1000;
				}
				else if(/*value =="0.0" || value.equals("0.0")||*/(value=="未使用" || value.equals("未使用"))){
					if(value=="未使用" || value.equals("未使用")){
						value="0.0";
					}
					queryRS = WorkPublicDAO.QueryRSMaxValue(id);
					RS += queryRS;
					dbvalue = 0.0;
				}
				else
					dbvalue = new Double( value );
			}
			countResult +=dbvalue;
			LogUtil.debug("countResult content equals :"+countResult, SKIP_Logger);
			//countResult += WorkPublicDAO.getGradeIndexPoint( conn, id, dbvalue, countType );
			/*if((countResult+"").equals("NaN") ){
				countResult =0.0;
			}*/
		}	
		if(RS >0||RS<100){
			countResult = countResult*100/(100-RS);
		}
		if(RS==100){
			countResult = 0.00;
		}
		if(countResult<0){
			countResult = 0.00;
		}
		//countResult = WorkPublicDAO.getGradeIndexPoint( conn, id, dbvalue, countType );
		return countResult;
	}
	
	public static double getCountSum( Connection conn, String useGradeIndex, int countType )
	throws SQLException
	{
		MyTable helloWorld = new MyTable();
		ArrayList idlist = helloWorld.MyTestLeafs(useGradeIndex);				
		int id = 0;
		for(int i=0;i < idlist.size(); i++ )
		{
			id = ((Integer)idlist.get(i)).intValue();
		}
		double countResult = 0;
		countResult = WorkPublicDAO.getGradeIndexPointSum( conn, id, countType );
		return countResult;
	}
	
    public static boolean validGradeData( HttpServletRequest request, Connection conn, Map formvalues, ArrayList errMsgList ){
        	
        Set set = formvalues.entrySet();
        Iterator it = set.iterator();
        while( it.hasNext() ){
            Map.Entry me = (Map.Entry)it.next();
            String keyStr = (String)me.getKey();
            String value = (String)me.getValue();

            String id = keyStr.split("_")[0];

            GradeData data = new GradeData();
            if( value == null || value.equalsIgnoreCase("") )
            {
            	value = "0";
            	WorkPublicDAO.isValidValue( conn, id, value, data );
            	String parentDescription = WorkPublicDAO.getPfxParentDescription(conn, data.getPfxParentId());
                request.setAttribute("Info", parentDescription+"->"+data.getPfxDescription() +" "+ (String)errMsgList.get(0));            	
            	return false;
            }     
            
            if(value =="未使用" ||value.equals("未使用")){
            	value = "0";
            	return true;
            }
            
            if(!WorkPublicDAO.isValidValue(conn, id, value,data)){
                String parentDescription = WorkPublicDAO.getPfxParentDescription(conn, data.getPfxParentId());

               request.setAttribute("Info", parentDescription+"->"+data.getPfxDescription() +" "+ (String)errMsgList.get(2) +"["+data.getPfxMinVal()+"--"+data.getPfxMaxVal()+"]");
               return false;
            }
        } 
        return true;
    }
    
    public static void recordErrorValues( HttpServletRequest request, Map formvalues )
    {
		   Map newMap = new HashMap();		    
		   Set set = formvalues.entrySet();
		   Iterator it = set.iterator();
		   while( it.hasNext() ){
		        Map.Entry me = (Map.Entry)it.next();
		        String keyStr = (String)me.getKey();
		        String value = (String)me.getValue();
		        if(value =="0.0" || value.equals("0.0")||value=="未使用" || value.equals("未使用")){
		        	value="0";
		        }
		        String key = keyStr.split("_")[1];
			    String col_name = "pfx_";
			    if(key.length() == 1){
			        col_name += "00"+key;
			    }else{
			    	col_name += "0"+key;
		        }
		        newMap.put( col_name, value);
		   }            	
		   request.setAttribute("errorValueMap", newMap );
    }  	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
}
