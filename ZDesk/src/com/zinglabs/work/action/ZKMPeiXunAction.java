package com.zinglabs.work.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.zinglabs.base.BaseAction;
import com.zinglabs.base.BaseException;
import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.*;
import com.zinglabs.work.MyTable;
import com.zinglabs.work.WorkPublicDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public final class ZKMPeiXunAction extends BaseAction{
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws BaseException,Exception{ 
		ActionForward myforward = null;
		String myaction = mapping.getParameter();
		
		if (isCancelled(request)) {
			return mapping.findForward("cancel");
		} else if ("ZKM_PEIXUN_LIST".equalsIgnoreCase(myaction)) {
			myforward = viewIndexRecord(mapping, form, request, response);
		}else if ("LIST".equalsIgnoreCase(myaction)) {
			myforward = listZkmPeiXun(mapping, form, request, response);
		}else{
			throw new BaseException("Parameter Error");
		} 
		return myforward;
	}

	private ActionForward listZkmPeiXun(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Connection conn = null;
		try {
			conn = Utility.getConnection();
			String ids = request.getParameter("ids");
			System.out.println("ids==="+ids);
			String a[] = ids.split(",");
			System.out.println(a[0]);
			ArrayList list = ZKMPeiXunDAO.queryZKMList(conn,a[0]);
            for(int i=0;i<list.size();i++){
            	ZKMPeiXunForm data = (ZKMPeiXunForm)list.get(i);
          /*      logger.debug("data:"+data.getAccount()+" "+
                                     data.getCallerNumber()+" "+
                                     data.getCountry()+" "+
                                     data.getCityTeleRate()+ " "+
                                     data.getRemoteTeleRate()+" "+
                                     data.getCallManagement()+" "+data.getCallManagementText()+" "+
                                     data.getRemarks());*/
            }
			setStoreList(request, "ZKMPeiXunList", list);

		} catch (SQLException e) {
			LogUtil.error("Fail to get accountcaller list:\n" + e.toString(),SKIP_Logger);
			LogUtil.error(e, SKIP_Logger);
			return mapping.findForward("failure");
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
			}
		}
		return mapping.findForward("success");
	}
	
	
	
	private ActionForward viewIndexRecord(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
        
		IndexBrowseForm form = (IndexBrowseForm)actionForm;
		
		
		
//		String gradeId = form.getGrade_index();
//        String gradeUser = form.getGrade_index_creator();
//        String gradeStep = form.getGradeindex_state();
		
		String gradeId = request.getParameter("gradeId");
//		String gradeUser = request.getParameter("gradeUser");
//		String gradeStep = request.getParameter("gradeStep");
  
       
//        String gradeIndex = form.getGrade_index();
       
        
		Connection conn = null;
		IndexBrowseForm data = null;        
		try {            
			conn = DAOTools.getConnectionOutS("ZQC");	 			
			data = IndexBrowseDAO.getGradeDetail(conn, gradeId);
			
			if( data != null )
			{					
	            MyTable helloWorld = new MyTable();
	            String sTable = "";
	                 	            
				sTable = helloWorld.MyTestDictView( data.getGrade_index(),data.getGrade_index_creator(),data.getGradeindex_state());					 	            	
									
	            request.setAttribute("sTable", sTable);
	            
				
				form.setGrade_index( data.getGrade_index() );
				form.setGrade_name( data.getGrade_name() );				
				form.setGrade_index_creator( data.getGrade_index_creator() );
				form.setGrade_index_time( data.getGrade_index_time() );
				form.setGrade_index_desp(data.getGrade_index_desp());
				form.setGradeindex_state(data.getGradeindex_state());
										
			}
		} catch (SQLException e) {
			LogUtil.debug("Fail to get score record list:\n" ,SKIP_Logger);
			return mapping.findForward("failure");
		} finally {
			try {
				
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LogUtil.error(e, SKIP_Logger);
			}
			finally{
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
		}
		return mapping.findForward("success");
	}	
	
}
