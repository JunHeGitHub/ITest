package com.zinglabs.work.action;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.BaseAction;
import com.zinglabs.base.BaseException;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.io.PrintWriter;
import java.sql.*;


import com.zinglabs.log.LogUtil;
import com.zinglabs.servlet.CommandService;
import com.zinglabs.util.*;
import com.zinglabs.work.GradeData;
import com.zinglabs.work.MyTable;
import com.zinglabs.work.MyTableNew;
import com.zinglabs.work.MyTableRecord;
import com.zinglabs.work.WorkPublic;
import com.zinglabs.work.WorkPublicDAO;

public final class FirstCheckAction extends BaseAction {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	protected HttpSession cSession = null;
	
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws BaseException,Exception {
		
		ActionForward myforward = null;
		String myaction = mapping.getParameter();

		if ("WORK_INDEX".equalsIgnoreCase(myaction)) {
			myforward = mapping.findForward("index");
		} else if ("WORK_VIEW_NEW".equalsIgnoreCase(myaction)) {
			myforward = viewWorkNew(mapping, form, request, response);
		} else if ("WORK_VIEW_NEW_RS".equalsIgnoreCase(myaction)) {
			myforward = viewWorkNewRS(mapping, form, request, response);
		} else if ("WORK_VIEW_MX".equalsIgnoreCase(myaction)) {
			myforward = viewWorkMingXi(mapping, form, request, response);
		}else if ("WORK_SAVE_NEW".equalsIgnoreCase(myaction)) {
			myforward = saveWorkNew(mapping, form, request, response);
		}else if ("WORK_SAVE_NEW_RS".equalsIgnoreCase(myaction)) {
			myforward = saveWorkNewRS(mapping, form, request, response);
		} else if ("WORK_FABU_NEW_CHUJIAN".equalsIgnoreCase(myaction)) {
			myforward = saveFabuNewChuanjian(mapping, form, request, response);
		}else if ("WORK_FABU_NEW_FUSHEN".equalsIgnoreCase(myaction)) {
			myforward = saveFabuNewFushen(mapping, form, request, response);
		} else if ("WORK_CACULATE_NEW".equalsIgnoreCase(myaction)) {
			myforward = caculateTotalNew(mapping, form, request, response);			
		} else if ("WORK_CACULATE_NEW_RS".equalsIgnoreCase(myaction)) {
			myforward = caculateTotalNewRS(mapping, form, request, response);			
		} else if ("LISTEN_TEACH_RECORD".equalsIgnoreCase(myaction)) {
			myforward = listenTeachRecord(mapping, form, request, response);			
		} else if ("LIST_AGENT_RECORD".equalsIgnoreCase(myaction)) {
			myforward = listAgentRecord(mapping, form, request, response);			
		} else if ("LIST_ZJY_RECORD".equalsIgnoreCase(myaction)) {
			myforward = listZhijianyuanRecord(mapping, form, request, response);			
		} else if ("LIST_ZJY_FJ_RECORD".equalsIgnoreCase(myaction)) {
			myforward = listZhijianyuanFJRecord(mapping, form, request, response);			
		} else if ("AGENT_WORK_VIEW".equalsIgnoreCase(myaction)) {
			myforward = viewAgentWorkRecord(mapping, form, request, response);			
		} else if ("AGENT_WORK_VIEW_NEW".equalsIgnoreCase(myaction)) {
			myforward = viewAgentWorkRecordNew(mapping, form, request, response);			
		} else if ("ARGU_SAVE".equalsIgnoreCase(myaction)) {
			myforward = saveArgu(mapping, form, request, response);			
		} else if ("ADMIN_AGENT_WORK".equalsIgnoreCase(myaction)) {
			myforward = viewAdminAgentWorkRecord(mapping, form, request, response);			
		} else if ("LIST_AGENT_PEIXUN_TEXT".equalsIgnoreCase(myaction)) {
			myforward = viewAgentWorkPeiXunText(mapping, form, request, response);			
		} else if ("ADMIN_AGENT_WORK_SAVE".equalsIgnoreCase(myaction)) {
			myforward = saveAdminAgentWorkRecord(mapping, form, request, response);			
		} else if ("WORK_VIEW_FJ".equalsIgnoreCase(myaction)) {
			myforward = viewWorkFJ(mapping, form, request, response);			
		} else if ("WORK_VIEW_FJ_WENBEN".equalsIgnoreCase(myaction)) {
			myforward = viewWorkFJWENBEN(mapping, form, request, response);			
		} else if ("WORK_SAVE_FJ".equalsIgnoreCase(myaction)) {
			myforward = saveWorkFJ(mapping, form, request, response);			
		} else if ("WORK_SAVE_FJ_WENBEN".equalsIgnoreCase(myaction)) {
			myforward = saveWorkFJWENBEN(mapping, form, request, response);			
		} else if ("WORK_CACULATE_FJ".equalsIgnoreCase(myaction)) {
			myforward = caculateTotalFJ(mapping, form, request, response);			
		} else if ("ADMIN_AGENT_WORK_CALCULER".equalsIgnoreCase(myaction)) {
			myforward = caculateTotalZJYFS(mapping, form, request, response);			
		}else if ("AGENT_PEIXUN_COMPLETE_SAVE_WORK".equalsIgnoreCase(myaction)) {
			myforward = peixunSaveWork(mapping, form, request, response);			
		} else {
			throw new BaseException("Parameter Error");
		}
		return myforward;
	}
	
	private ActionForward viewWorkNew(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		String isRecord = null;
		String gradeId = request.getParameter("gradeId");
		String gradeIndex =request.getParameter("gradeIndex");
		if(gradeIndex==null){
			gradeIndex =(String)request.getAttribute("gradeIndex");
		}
		String filename =request.getParameter("filename");
		if(filename==null)
		{
			filename =(String)request.getAttribute("filename");
		}
		String filelocation= request.getParameter("filelocation");
		if(filelocation==null)
		{
			filelocation =(String)request.getAttribute("filelocation");
		}	
		LogUtil.debug("gradeId="+gradeId+"gradeIndex="+gradeIndex+"filename="+filename+"viewWorkNew", SKIP_Logger);
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		//String userArgs = (String)request.getParameter("userArgs");
		String userName = request.getParameter("userName");
		// if(userArgs.equals("质检员") || userArgs.equals("质检组长") ){
	    //    	request.setAttribute("userArgs", "质检员");
	       // }else{
	       // 	request.setAttribute("userArgs", "系统管理员");
	       // }	
			try {
				
				String userArgsName = (String)request.getAttribute("userArgs");
				//System.out.println(userArgsName.equals(""));
				//System.out.println(userArgsName==null);
				//System.out.println(userArgsName.equals("null"));
				conn = DAOTools.getConnectionOutS("ZQC");
				String userArgs = "";
				if(!(userName==null)){
					userArgs = FirstCheckDAO.queryUserArgs( userName );
					request.setAttribute("userArgs", userArgs);
				}else {
					if(!(userArgsName==null)){
						userArgs = userArgsName;
						 request.setAttribute("userArgs", userArgs);
					}
				}
			
			GradeData tdata = FirstCheckDAO.queryFirstTaskInfoNew( conn, gradeIndex );
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
			form.setScoreState(FirstCheckDAO.queryScoreState( conn, filename ));
//			System.out.println("isMp3=========="+isRecord);
	            if( tdata != null )
	            {
	            	if(isRecord.equals("mp3")){
		            	request.setAttribute( "TaskGradeIndex", tdata.useGradeIndexName);
		            	form.setUseGradeIndex(tdata.useGradeIndex);
		            	if( tdata.grade_yipiaofoujue ==null){
		            		form.setGradeYipiaofoujue("可用");
		            	}else{
			            	form.setGradeYipiaofoujue(tdata.grade_yipiaofoujue);
		            	}
	            	}else{
	            		request.setAttribute( "TaskGradeIndex", tdata.useGradeIndexName);
		            	form.setUseGradeIndex(tdata.useGradeIndex);
		            	if(tdata.grade_important ==null){
		            		form.setGradeImportant("重要");
		            	}else{
			            	form.setGradeImportant(tdata.grade_important);
		            	}
	            	}
	            }
	        String useGradeIndex = form.getUseGradeIndex();
	        form.setFileName(filename);
			form.setFileLocation(filelocation);
	        if( filename == null ||  useGradeIndex == null || filelocation == null)
	        	return mapping.findForward("failure");
	        
			FirstCheckForm data = null;
			
            MyTable helloWorld = new MyTable();
            String sTable = "";
			Map newMap = (Map)request.getAttribute( "errorValueMap" );
			StringBuffer sbOneTicket = new StringBuffer(0); 		

					
			if( newMap != null )
			{
				HashMap map = (HashMap)form.getValuesMap();
				sTable = helloWorld.MyTestFuncByHashMap(tdata.getUseGradeIndex(), newMap, map );
			}
			else
			{
				if(isRecord.equals("txt")){
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-5));
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}else{
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}
				 
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getGradeRemarkNew(conn, gradeId);
			}
			form.setGradeRemark(gradeRemark);
			
            // get grade table
           
            request.setAttribute("sTable", sTable);
//          request.setAttribute("userArgs", userArgs);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            request.setAttribute("scoreState", form.getScoreState());
            
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            	request.setAttribute("gradeImportant", form.getGradeImportant());
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            	request.setAttribute("gradeYipiaofoujue", form.getGradeYipiaofoujue());
            }
    
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(tdata.getUseGradeIndex());
            
		} catch (Exception e) {
			LogUtil.error("Fail to viewwork", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
//			try {
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				logger.warn(e);
//			}
		}
//		if(isRecord==null||isRecord.isEmpty()||isRecord.equals("txt")){
		if(isRecord==null||isRecord.length()==0||isRecord.equals("txt")){
			return mapping.findForward("successtxt");
		}
		else{
//			System.out.println("this is the mp3");
			return mapping.findForward("success");
		}
	}	
	
	private ActionForward viewWorkNewRS(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		String isRecord = null;
		String gradeId = request.getParameter("gradeId");
		String gradeIndex =request.getParameter("gradeIndex");
		if(gradeIndex==null){
			gradeIndex =(String)request.getAttribute("gradeIndex");
		}
		String filename =request.getParameter("filename");
		if(filename==null)
		{
			filename =(String)request.getAttribute("filename");
		}
		String filelocation= request.getParameter("filelocation");
		if(filelocation==null)
		{
			filelocation =(String)request.getAttribute("filelocation");
		}	
		LogUtil.debug("gradeId="+gradeId+"gradeIndex="+gradeIndex+"filename="+filename+"viewWorkNew", SKIP_Logger);
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		//String userArgs = (String)request.getParameter("userArgs");
		String userName = request.getParameter("userName");
		// if(userArgs.equals("质检员") || userArgs.equals("质检组长") ){
	    //    	request.setAttribute("userArgs", "质检员");
	       // }else{
	       // 	request.setAttribute("userArgs", "系统管理员");
	       // }	
			try {
				
				String userArgsName = (String)request.getAttribute("userArgs");
				//System.out.println(userArgsName.equals(""));
				//System.out.println(userArgsName==null);
				//System.out.println(userArgsName.equals("null"));
				conn = DAOTools.getConnectionOutS("ZQC");
				String userArgs = "";
				if(!(userName==null)){
					userArgs = FirstCheckDAO.queryUserArgs( userName );
					request.setAttribute("userArgs", userArgs);
				}else {
					if(!(userArgsName==null)){
						userArgs = userArgsName;
						 request.setAttribute("userArgs", userArgs);
					}
				}
			
			GradeData tdata = FirstCheckDAO.queryFirstTaskInfoNew( conn, gradeIndex );
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
			form.setScoreState(FirstCheckDAO.queryScoreState( conn, filename ));
//			System.out.println("isMp3=========="+isRecord);
	            if( tdata != null )
	            {
	            	if(isRecord.equals("mp3")){
		            	request.setAttribute( "TaskGradeIndex", tdata.useGradeIndexName);
		            	form.setUseGradeIndex(tdata.useGradeIndex);
		            	if( tdata.grade_yipiaofoujue ==null){
		            		form.setGradeYipiaofoujue("可用");
		            	}else{
			            	form.setGradeYipiaofoujue(tdata.grade_yipiaofoujue);
		            	}
	            	}else{
	            		request.setAttribute( "TaskGradeIndex", tdata.useGradeIndexName);
		            	form.setUseGradeIndex(tdata.useGradeIndex);
		            	if(tdata.grade_important ==null){
		            		form.setGradeImportant("重要");
		            	}else{
			            	form.setGradeImportant(tdata.grade_important);
		            	}
	            	}
	            }
	        String useGradeIndex = form.getUseGradeIndex();
	        form.setFileName(filename);
			form.setFileLocation(filelocation);
	        if( filename == null ||  useGradeIndex == null || filelocation == null)
	        	return mapping.findForward("failure");
	        
			FirstCheckForm data = null;
			
			MyTableRecord helloWorld = new MyTableRecord();
            String sTable = "";
			Map newMap = (Map)request.getAttribute( "errorValueMap" );
			StringBuffer sbOneTicket = new StringBuffer(0); 		
			String grade_Index=tdata.getUseGradeIndex();
					
			if( newMap != null )
			{
				HashMap map = (HashMap)form.getValuesMap();
				sTable = helloWorld.RSMyTestFuncByMap(tdata.getUseGradeIndex(), newMap );
			}
			else
			{
				if(isRecord.equals("txt")){
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-5));
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}else{
					ArrayList scoreList = null;
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}
				 
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getGradeRemarkNew(conn, gradeId);
			}
			form.setGradeRemark(gradeRemark);
			
            // get grade table
           
            request.setAttribute("sTable", sTable);
//          request.setAttribute("userArgs", userArgs);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            request.setAttribute("scoreState", form.getScoreState());
            
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            	request.setAttribute("gradeImportant", form.getGradeImportant());
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            	request.setAttribute("gradeYipiaofoujue", form.getGradeYipiaofoujue());
            }
    
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(tdata.getUseGradeIndex());
            
		} catch (Exception e) {
			LogUtil.error("Fail to viewworkRS", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
//			try {
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				logger.warn(e);
//			}
		}
//		if(isRecord==null||isRecord.isEmpty()||isRecord.equals("txt")){
		if(isRecord==null||isRecord.length()==0||isRecord.equals("txt")){
			return mapping.findForward("successtxt");
		}
		else{
//			System.out.println("this is the mp3");
			return mapping.findForward("success");
		}
	}
	
	private ActionForward viewWorkMingXi(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		String isRecord = null;
		String gradeId = request.getParameter("gradeId");
		String gradeIndex =request.getParameter("gradeIndex");
		if(gradeIndex==null){
			gradeIndex =(String)request.getAttribute("gradeIndex");
		}
		String filename =request.getParameter("filename");
		if(filename==null)
		{
			filename =(String)request.getAttribute("filename");
		}
		String filelocation= request.getParameter("filelocation");
		if(filelocation==null)
		{
			filelocation =(String)request.getAttribute("filelocation");
		}
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		
		LogUtil.debug("gradeId="+gradeId+"gradeIndex="+gradeIndex+"filename="+filename+"viewWorkMingXi", SKIP_Logger);
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			GradeData tdata = FirstCheckDAO.queryFirstTaskInfoNew( conn, gradeIndex );
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
			form.setScoreState(FirstCheckDAO.queryScoreState( conn, filename ));
//			System.out.println("isMp3=========="+isRecord);
	            if( tdata != null )
	            {
	            	if(isRecord.equals("mp3")){
		            	request.setAttribute( "TaskGradeIndex", tdata.useGradeIndexName);
		            	form.setUseGradeIndex(tdata.useGradeIndex);
		            	if( tdata.grade_yipiaofoujue ==null){
		            		form.setGradeYipiaofoujue("可用");
		            	}else{
			            	form.setGradeYipiaofoujue(tdata.grade_yipiaofoujue);
		            	}
	            	}else{
	            		request.setAttribute( "TaskGradeIndex", tdata.useGradeIndexName);
		            	form.setUseGradeIndex(tdata.useGradeIndex);
		            	if(tdata.grade_important ==null){
		            		form.setGradeImportant("重要");
		            	}else{
			            	form.setGradeImportant(tdata.grade_important);
		            	}
	            	}
	            }
	        String useGradeIndex = form.getUseGradeIndex();
	        form.setFileName(filename);
			form.setFileLocation(filelocation);
	        if( filename == null ||  useGradeIndex == null || filelocation == null)
	        	return mapping.findForward("failure");
	        
			FirstCheckForm data = null;
			
            MyTable helloWorld = new MyTable();
            String sTable = "";
			Map newMap = (Map)request.getAttribute( "errorValueMap" );
			StringBuffer sbOneTicket = new StringBuffer(0); 		

					
			if( newMap != null )
			{
				HashMap map = (HashMap)form.getValuesMap();
				sTable = helloWorld.MyTestFuncByHashMap(tdata.getUseGradeIndex(), newMap, map );
			}
			else
			{
				if(isRecord.equals("txt")){
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-5));
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}else{
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}
				 
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getGradeRemarkNew(conn, gradeId);
			}
			form.setGradeRemark(gradeRemark);
			
            // get grade table
           
            request.setAttribute("sTable", sTable);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            request.setAttribute("scoreState", form.getScoreState());
            
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            	request.setAttribute("gradeImportant", form.getGradeImportant());
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            	request.setAttribute("gradeYipiaofoujue", form.getGradeYipiaofoujue());
            }
    
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(tdata.getUseGradeIndex());
            
		} catch (Exception e) {
			LogUtil.error("Fail to viewwork", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
//			try {
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				logger.warn(e);
//			}
		}
//		if(isRecord==null||isRecord.isEmpty()||isRecord.equals("txt")){
		if(isRecord==null||isRecord.length()==0||isRecord.equals("txt")){
			return mapping.findForward("successtxt");
		}
		else{
//			System.out.println("this is the mp3");
			return mapping.findForward("success");
		}
	}	
	
	private ActionForward saveFabuNewChuanjian(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		
        FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		String isRecord = null;
		String filename = form.getFileName();
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			FirstCheckDAO.updateScoreState( conn, filename);
			String userArgs = (String)request.getParameter("userArgs");
			if(!(userArgs ==null)){
			 if(userArgs.equals("false")){
		        	request.setAttribute("userArgs", "质检员");
		        }else{
		        	request.setAttribute("userArgs", "质检组长");
		        }
			}
			ArrayList errMsglist = new ArrayList();
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Empty") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Number") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.NoValidValid") );
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );

            request.setAttribute("filename", form.getFileName());
            request.setAttribute("filelocation", form.getFileLocation());
            request.setAttribute("gradeRemark", form.getGradeRemark());
           request.setAttribute("gradeIndex",form.getUseGradeIndex());
	       } catch (SQLException e) {
			LogUtil.error("Fail to savework", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
				if (conn != null) {
					DAOTools.releaseConnectionOutS("ZQC", conn);
				}
		}
		return mapping.findForward("success");
	}
	
	private ActionForward saveFabuNewFushen(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		
        FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		String isRecord = null;
		String filename = form.getFileName();
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			FirstCheckDAO.updateScoreStateFushen( conn, filename);
			ArrayList errMsglist = new ArrayList();
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Empty") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Number") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.NoValidValid") );
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
			String phoneNumber = FirstCheckDAO.queryPhoneNumber( conn, filename );
			String userArgs = (String)request.getParameter("userArgs");
			if(!(userArgs ==null)){
			 if(userArgs.equals("false")){
		        	request.setAttribute("userArgs", "质检员");
		        }else{
		        	request.setAttribute("userArgs", "质检组长");
		        }
			}
            request.setAttribute("filename", form.getFileName());
            request.setAttribute("filelocation", form.getFileLocation());
            request.setAttribute("gradeRemark", form.getGradeRemark());
           request.setAttribute("gradeIndex",form.getUseGradeIndex());
	       } catch (SQLException e) {
			LogUtil.error("Fail to savework", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
				if (conn != null) {
					DAOTools.releaseConnectionOutS("ZQC", conn);
				}
		}
		return mapping.findForward("success");
	}
	
	
	private ActionForward saveWorkNew(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		
        FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		String isRecord = null;
		int sumkeyInt =0 ;
		String filename = form.getFileName();
		String userArgs = (String)request.getParameter("userArgs");
		try {
			if(!(userArgs ==null)){
				 if(userArgs.equals("false")){
			        	request.setAttribute("userArgs", "质检员");
			        }else{
			        	request.setAttribute("userArgs", "质检组长");
			        }
			}
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
//			sumkeyInt = FirstCheckDAO.getItemSum(form);
	     	
			conn = DAOTools.getConnectionOutS("ZQC");
			
			
			LogUtil.debug(ConfInfo.proerties.getProperty("ZQC_port")+" saveWorkNew ", SKIP_Logger);
			
			ArrayList errMsglist = new ArrayList();
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Empty") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Number") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.NoValidValid") );
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
	            if(!WorkPublic.validGradeData( request, conn, form.getValuesMap(), errMsglist ) )
	            {            	           
	            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
	     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
	                return mapping.findForward("failure");
	            }
	            if(isRecord.equals("mp3")){
	            	if(form.getGradeYipiaofoujue() == null ){
	            		if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
	            			 FirstCheckDAO.updateGradeScoreNew( conn, form);
					         FirstCheckDAO.updatePfxTotalNew( conn, form,0);
	            		}
	            	}else if(form.getGradeYipiaofoujue().equals("可用")){
	            		if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
	            			FirstCheckDAO.updateGradeScoreNew( conn, form);
	            			FirstCheckDAO.updatePfxTotalNew( conn, form,0);
	            			//FirstCheckDAO.updateAgentSaAgentArgumentRecord( conn, form);
				          }
					}else{
						if(FirstCheckDAO.saveGradeScoreOld( conn, form,isRecord)){
					        FirstCheckDAO.updateGradeScoreNew( conn, form);
					        FirstCheckDAO.updatePfxTotalOld( conn, form);
					        //	FirstCheckDAO.updateAgentSaAgentArgumentText( conn, form);
						}
					}
	            }else{
	            	if(form.getGradeImportant().equals("非常重要")){
						if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
			           	   FirstCheckDAO.updateGradeScoreNew( conn, form);
			           	   FirstCheckDAO.updatePfxTotalNew( conn, form,sumkeyInt);
			           	   FirstCheckDAO.updateTGradeDetailNew( conn, form,sumkeyInt);
			           }
					}else if(form.getGradeImportant().equals("重要")){
						if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
				           FirstCheckDAO.updateGradeScoreNew( conn, form);
				           FirstCheckDAO.updatePfxTotalNew( conn, form,sumkeyInt);
				           FirstCheckDAO.updateTGradeDetailNew( conn, form,sumkeyInt);
						}
					}else{
						if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
					       FirstCheckDAO.updateGradeScoreNew( conn, form);
					       FirstCheckDAO.updatePfxTotalNew( conn, form,sumkeyInt);
					       FirstCheckDAO.updateTGradeDetailNew( conn, form,sumkeyInt);
							}
						/*if(FirstCheckDAO.saveGradeScoreOld( conn, form,isRecord)){
				           FirstCheckDAO.updateGradeScoreNew( conn, form);
				           FirstCheckDAO.updatePfxTotalNew( conn, form);
						}*/
					}	
				}
            request.setAttribute("filename", form.getFileName());
            request.setAttribute("filelocation", form.getFileLocation());
            request.setAttribute("gradeRemark", form.getGradeRemark());
           request.setAttribute("gradeIndex",form.getUseGradeIndex());
           setErrorMSG2( request, "errors.Msg.ScoreTotal", " : "+sumkeyInt+".00" );
           FirstCheckDAO.updateMauvais( conn, form, x, y);
	       } catch (SQLException e) {
			LogUtil.error("Fail to savework", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
				if (conn != null) {
					DAOTools.releaseConnectionOutS("ZQC", conn);
				}
		}
		return mapping.findForward("success");
	}	  	
	
	private ActionForward saveWorkNewRS(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		
        FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		String isRecord = null;
		int sumkeyInt =0 ;
		String filename = form.getFileName();
		String userArgs = (String)request.getParameter("userArgs");
		try {
			if(!(userArgs ==null)){
				 if(userArgs.equals("false")){
			        	request.setAttribute("userArgs", "质检员");
			        }else{
			        	request.setAttribute("userArgs", "质检组长");
			        }
			}
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
//			sumkeyInt = FirstCheckDAO.getItemSum(form);
	     	
			conn = DAOTools.getConnectionOutS("ZQC");
			
			
			LogUtil.debug(ConfInfo.proerties.getProperty("ZQC_port")+" saveWorkNew ", SKIP_Logger);
			
			ArrayList errMsglist = new ArrayList();
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Empty") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Number") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.NoValidValid") );
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
	            if(!WorkPublic.validGradeData( request, conn, form.getValuesMap(), errMsglist ) )
	            {            	           
	            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
	     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
	                return mapping.findForward("failure");
	            }
	            if(isRecord.equals("mp3")){
	            	if(form.getGradeYipiaofoujue() == null ){
	            		if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
	            			 FirstCheckDAO.updateGradeScoreNew( conn, form);
					         FirstCheckDAO.updatePfxTotalNew( conn, form,0);
	            		}
	            	}else if(form.getGradeYipiaofoujue().equals("可用")){
	            		if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
	            			FirstCheckDAO.updateGradeScoreNew( conn, form);
	            			FirstCheckDAO.updatePfxTotalNew( conn, form,0);
	            			//FirstCheckDAO.updateAgentSaAgentArgumentRecord( conn, form);
				          }
					}else{
						if(FirstCheckDAO.saveGradeScoreOld( conn, form,isRecord)){
					        FirstCheckDAO.updateGradeScoreNew( conn, form);
					        FirstCheckDAO.updatePfxTotalOld( conn, form);
					        //	FirstCheckDAO.updateAgentSaAgentArgumentText( conn, form);
						}
					}
	            }else{
	            	if(form.getGradeImportant().equals("非常重要")){
						if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
			           	   FirstCheckDAO.updateGradeScoreNew( conn, form);
			           	   FirstCheckDAO.updatePfxTotalNew( conn, form,sumkeyInt);
			           	   FirstCheckDAO.updateTGradeDetailNew( conn, form,sumkeyInt);
			           }
					}else if(form.getGradeImportant().equals("重要")){
						if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
				           FirstCheckDAO.updateGradeScoreNew( conn, form);
				           FirstCheckDAO.updatePfxTotalNew( conn, form,sumkeyInt);
				           FirstCheckDAO.updateTGradeDetailNew( conn, form,sumkeyInt);
						}
					}else{
						if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
					       FirstCheckDAO.updateGradeScoreNew( conn, form);
					       FirstCheckDAO.updatePfxTotalNew( conn, form,sumkeyInt);
					       FirstCheckDAO.updateTGradeDetailNew( conn, form,sumkeyInt);
							}
						/*if(FirstCheckDAO.saveGradeScoreOld( conn, form,isRecord)){
				           FirstCheckDAO.updateGradeScoreNew( conn, form);
				           FirstCheckDAO.updatePfxTotalNew( conn, form);
						}*/
					}	
				}
            request.setAttribute("filename", form.getFileName());
            request.setAttribute("filelocation", form.getFileLocation());
            request.setAttribute("gradeRemark", form.getGradeRemark());
           request.setAttribute("gradeIndex",form.getUseGradeIndex());
           setErrorMSG2( request, "errors.Msg.ScoreTotal", " : "+sumkeyInt+".00" );
           FirstCheckDAO.updateMauvais( conn, form, x, y);
	       } catch (SQLException e) {
			LogUtil.error("Fail to savework", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
				if (conn != null) {
					DAOTools.releaseConnectionOutS("ZQC", conn);
				}
		}
		return mapping.findForward("success");
	}
	
	private ActionForward caculateTotalNew(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		String gradeId = request.getParameter("gradeId");
		String isRecord = null;
		String filelocation= request.getParameter("filelocation");
		if(filelocation==null)
		{
			filelocation =(String)request.getAttribute("filelocation");
		}
		form.setFileLocation(filelocation);
		form.setGradeId(gradeId);
		String gradeIndex =request.getParameter("gradeIndex");
		gradeIndex = form.getUseGradeIndex();
		Connection conn = null;
		String userArgs = (String)request.getParameter("userArgs");
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			ArrayList errMsglist = new ArrayList();
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Empty") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Number") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.NoValidValid") );
			MyTableNew helloWorld = new MyTableNew();
			String sTable = "";
			if(!(userArgs ==null)){
				 if(userArgs.equals("false")){
			        	request.setAttribute("userArgs", "质检员");
			        }else{
			        	request.setAttribute("userArgs", "质检组长");
			     }
			}
			String filename = form.getFileName();
	        String useGradeIndex = form.getUseGradeIndex();
	        isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
			GradeData tdata = FirstCheckDAO.queryFirstTaskInfoNew( conn, gradeIndex );
			tdata.setUseGradeIndex(gradeIndex);
            if( tdata != null )
            {
            	request.setAttribute( "TaskGradeIndex", tdata.useGradeIndexName);
            	form.setUseGradeIndex(tdata.useGradeIndex);
            }
	        if( filename == null || filelocation == null || useGradeIndex == null )
	        	return mapping.findForward("failure");
				double total = 0;
	            if(!WorkPublic.validGradeData( request, conn, form.getValuesMap(), errMsglist ) )
	            {            	           
	            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
	     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
	                return mapping.findForward("failure");
	            }	         
	            if(isRecord.equals("txt")){
	            	total = WorkPublic.getCountTotalNew( conn, tdata.useGradeIndex, form.getValuesMap(), 0 );
	            }else{
	            	total = WorkPublic.getCountTotal( conn, tdata.useGradeIndex, form.getValuesMap(), 0 );
	            }
            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeIndex",form.getUseGradeIndex());
            request.setAttribute("gradeId", gradeId);
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            }
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(useGradeIndex);
			DecimalFormat df = new DecimalFormat("#.00"); 
			if(total == 0){
				setErrorMSG2( request, "errors.Msg.ScoreTotal", " : "+"0.00" );
				form.setPfxTotal("0");
			}else{
				String strTotal = df.format(total);
				setErrorMSG2( request, "errors.Msg.ScoreTotal", " : "+strTotal );
				form.setPfxTotal(strTotal);
			}
			
			
		} catch (SQLException e) {
			LogUtil.error("Fail to caculateTotalNew", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
				if (conn != null) {
					DAOTools.releaseConnectionOutS("ZQC", conn);
				}
		}
		//TODO
		return mapping.findForward("success");
	}	  	
	
	
	private ActionForward caculateTotalNewRS(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		String gradeId = request.getParameter("gradeId");
		String isRecord = null;
		String filelocation= request.getParameter("filelocation");
		if(filelocation==null)
		{
			filelocation =(String)request.getAttribute("filelocation");
		}
		form.setFileLocation(filelocation);
		form.setGradeId(gradeId);
		String gradeIndex =request.getParameter("gradeIndex");
		gradeIndex = form.getUseGradeIndex();
		Connection conn = null;
		String userArgs = (String)request.getParameter("userArgs");
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			ArrayList errMsglist = new ArrayList();
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Empty") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Number") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.NoValidValid") );
			MyTableNew helloWorld = new MyTableNew();
			String sTable = "";
			if(!(userArgs ==null)){
				 if(userArgs.equals("false")){
			        	request.setAttribute("userArgs", "质检员");
			        }else{
			        	request.setAttribute("userArgs", "质检组长");
			     }
			}
			String filename = form.getFileName();
	        String useGradeIndex = form.getUseGradeIndex();
	        isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
			GradeData tdata = FirstCheckDAO.queryFirstTaskInfoNew( conn, gradeIndex );
			tdata.setUseGradeIndex(gradeIndex);
            if( tdata != null )
            {
            	request.setAttribute( "TaskGradeIndex", tdata.useGradeIndexName);
            	form.setUseGradeIndex(tdata.useGradeIndex);
            }
	        if( filename == null || filelocation == null || useGradeIndex == null )
	        	return mapping.findForward("failure");
				double total = 0;
	        /*    if(!WorkPublic.validGradeData( request, conn, form.getValuesMap(), errMsglist ) )
	            {            	           
	            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
	     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
	                return mapping.findForward("failure");
	            }	  */       
	            if(isRecord.equals("txt")){
	            	total = WorkPublic.getCountTotalNew( conn, tdata.useGradeIndex, form.getValuesMap(), 0 );
	            }else{
	            	total = WorkPublic.getCountTotalRS( conn, tdata.useGradeIndex, form.getValuesMap(), 0 );
	            }
            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeIndex",form.getUseGradeIndex());
            request.setAttribute("gradeId", gradeId);
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            }
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(useGradeIndex);
			DecimalFormat df = new DecimalFormat("#.00"); 
			if(total == 0){
				setErrorMSG2( request, "errors.Msg.ScoreTotal", " : "+"0.00" );
				form.setPfxTotal("0");
			}else{
				String strTotal = df.format(total);
				setErrorMSG2( request, "errors.Msg.ScoreTotal", " : "+strTotal );
				form.setPfxTotal(strTotal);
			}
			
			
		} catch (SQLException e) {
			LogUtil.error("Fail to caculateTotalNew", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
				if (conn != null) {
					DAOTools.releaseConnectionOutS("ZQC", conn);
				}
		}
		//TODO
		return mapping.findForward("success");
	}
	
	private ActionForward caculateTotalFJ(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		String gradeId = request.getParameter("gradeId");
		String filelocation= request.getParameter("filelocation");
		String isRecord = null;
		if(filelocation==null)
		{
			filelocation =(String)request.getAttribute("filelocation");
		}
		form.setFileLocation(filelocation);
		form.setGradeId(gradeId);
		String gradeIndex =request.getParameter("gradeIndex");
		gradeIndex = form.getUseGradeIndex();
		Connection conn = null;
		
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			ArrayList errMsglist = new ArrayList();
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Empty") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Number") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.NoValidValid") );
			MyTableNew helloWorld = new MyTableNew();
			String sTable = "";
			
            
			String filename = form.getFileName();
	        String useGradeIndex = form.getUseGradeIndex();
	        isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
			GradeData tdata = FirstCheckDAO.queryFirstTaskInfoNew( conn, gradeIndex );
			tdata.setUseGradeIndex(gradeIndex);
            if( tdata != null )
            {
            	request.setAttribute( "TaskGradeIndex", tdata.useGradeIndexName);
            	form.setUseGradeIndex(tdata.useGradeIndex);
            }
	        if( filename == null || filelocation == null || useGradeIndex == null )
	        	return mapping.findForward("failure");
			double total = 0;
	            if(!WorkPublic.validGradeData( request, conn, form.getValuesMap(), errMsglist ) )
	            {            	           
	            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
	     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
	                return mapping.findForward("failure");
	            }	         
	            if(isRecord.equals("txt")){
	            	total = WorkPublic.getCountTotalNew( conn, tdata.useGradeIndex, form.getValuesMap(), 0 );
	            }else{
	            	total = WorkPublic.getCountTotal( conn, tdata.useGradeIndex, form.getValuesMap(), 0 );
	            }
	//            total = WorkPublic.getCountTotalNew( conn, tdata.useGradeIndex, form.getValuesMap(), 0 );
            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeIndex",form.getUseGradeIndex());
            request.setAttribute("gradeId", gradeId);
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            }
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(useGradeIndex);
			DecimalFormat df = new DecimalFormat("#.00"); 
			String strTotal = df.format(total);
			setErrorMSG2( request, "errors.Msg.ScoreTotal", " : "+strTotal );
			form.setPfxTotal(strTotal);
		} catch (SQLException e) {
			LogUtil.error("Fail to caculateTotalNew", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
				if (conn != null) {
					DAOTools.releaseConnectionOutS("ZQC", conn);
				}
		}
		//TODO
		return mapping.findForward("success");
	}	  
	
	private ActionForward caculateTotalZJYFS(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		String gradeId = request.getParameter("gradeId");
		String filelocation= request.getParameter("filelocation");
		String reowner=request.getParameter("reowner");
		String isRecord = null;
		
		if(filelocation==null)
		{
			filelocation =(String)request.getAttribute("filelocation");
		}
		 if(!(reowner ==null)){
			 
		        if(reowner.contains("ZJ0")){
		        	request.setAttribute("userArgs", "质检组长");
		        }else{
		        	request.setAttribute("userArgs", "质检组长");
		        }
	        }
		form.setFileLocation(filelocation);
		form.setGradeId(gradeId);
		String gradeIndex =request.getParameter("gradeIndex");
		gradeIndex = form.getUseGradeIndex();
		Connection conn = null;
		
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			ArrayList errMsglist = new ArrayList();
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Empty") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Number") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.NoValidValid") );
			MyTableNew helloWorld = new MyTableNew();
			String sTable = "";
			
            
			String filename = form.getFileName();
	        String useGradeIndex = form.getUseGradeIndex();
	        isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
			GradeData tdata = FirstCheckDAO.queryFirstTaskInfoNew( conn, gradeIndex );
			tdata.setUseGradeIndex(gradeIndex);
            if( tdata != null )
            {
            	request.setAttribute( "TaskGradeIndex", tdata.useGradeIndexName);
            	form.setUseGradeIndex(tdata.useGradeIndex);
            }
	        if( filename == null || filelocation == null || useGradeIndex == null )
	        	return mapping.findForward("failure");
			double total = 0;
	            if(!WorkPublic.validGradeData( request, conn, form.getValuesMap(), errMsglist ) )
	            {            	           
	            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
	     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
	                return mapping.findForward("failure");
	            }	         
	            if(isRecord.equals("txt")){
	            	total = WorkPublic.getCountTotalNew( conn, tdata.useGradeIndex, form.getValuesMap(), 0 );
	            }else{
	            	total = WorkPublic.getCountTotal( conn, tdata.useGradeIndex, form.getValuesMap(), 0 );
	            }
	//            total = WorkPublic.getCountTotalNew( conn, tdata.useGradeIndex, form.getValuesMap(), 0 );
            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeIndex",form.getUseGradeIndex());
            request.setAttribute("gradeId", gradeId);
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            }
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(useGradeIndex);
			DecimalFormat df = new DecimalFormat("#.00"); 
			String strTotal = df.format(total);
			setErrorMSG2( request, "errors.Msg.ScoreTotal", " : "+strTotal );
			form.setPfxTotal(strTotal);
		} catch (SQLException e) {
			LogUtil.error("Fail to caculateTotalNew", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
				if (conn != null) {
					DAOTools.releaseConnectionOutS("ZQC", conn);
				}
		}
		//TODO
		return mapping.findForward("success");
	}
	
	private ActionForward listenTeachRecord(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		String gradeId = request.getParameter("gradeId");
		String isRecord = null; 
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			GradeData tdata = FirstCheckDAO.queryTeachRecordInfo( conn, gradeId );
					
			String gradeName = (String)FirstCheckDAO.queryGradeName( conn, tdata.getUseGradeIndex() );
			//String gradeName = tdata.getGrade_name();
	            if( tdata != null )
	            {
	            	request.setAttribute( "TaskGradeIndex", gradeName);
	            	form.setUseGradeIndex(tdata.useGradeIndex);
	            }
//	        tdata= tdata.setGrade_name(FirstCheckDAO.queryGradeName( conn, tdata.getUseGradeIndex() ));
	        String useGradeIndex = form.getUseGradeIndex();
	        String filename = tdata.getFile_name();
	        String  filelocation= tdata.getFile_location();
	        isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
	     /*   form.setFileName(filename);
			form.setFileLocation(filelocation);*/
	        if( filename == null ||  useGradeIndex == null || filelocation == null)
	        	return mapping.findForward("failure");
	        
			FirstCheckForm data = null;
			
            MyTableRecord helloWorld = new MyTableRecord();
            String sTable = "";
			Map newMap = (Map)request.getAttribute( "errorValueMap" );
			StringBuffer sbOneTicket = new StringBuffer(0); 		
            
            if( newMap != null )
			{
				HashMap map = (HashMap)form.getValuesMap();
				sTable = helloWorld.MyTestFuncByMap(useGradeIndex, newMap );
			}
			else
			{
				if(isRecord.equals("txt")){
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-5));
					 sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
				}else{
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
					 sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
				}
				 
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getTeachGradeRemarkNew(conn, gradeId);
			}
			String gradeRemarkFJ = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemarkFJ == null )
			{
				gradeRemarkFJ = WorkPublicDAO.getTeachGradeRemarkNewFJ(conn, gradeId);
			}
			form.setGradeRemark(gradeRemark);
			form.setFujianRemark(gradeRemarkFJ);
            // get grade table
           
            request.setAttribute("sTable", sTable);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            }
            
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(tdata.getUseGradeIndex());
            
		} catch (Exception e) {
			LogUtil.error("Fail to viewTeachRecord", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
		}
		//TODO
		if(isRecord.equals("txt")){
			return mapping.findForward("successtxt");
		}else{
			return mapping.findForward("success");
		}
		
	}	
	
	private ActionForward listZhijianyuanRecord(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		Connection conn = null;
		
		String isRecord = null;
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		
		/*if(gradeId.substring(gradeId.length()-1, gradeId.length()).equals(".")){
			gradeId=gradeId.substring(0, gradeId.length()-1);
		}*/
		
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			
			String gradeId = request.getParameter("gradeId");
			isRecord = FirstCheckDAO.queryIsMp3( conn, gradeId );	
			if(isRecord.equals("txt")){
				gradeId = gradeId.substring(0,gradeId.length()-5);
			}else{
				gradeId = gradeId.substring(0,gradeId.length()-4);
			}
			
			GradeData tdata = FirstCheckDAO.queryTeachRecordInfo( conn, gradeId );
			
			String gradeName = (String)FirstCheckDAO.queryGradeName( conn, tdata.getUseGradeIndex() );
			//String gradeName = tdata.getGrade_name();
	            if( tdata != null )
	            {
	            	request.setAttribute( "TaskGradeIndex", gradeName);
	            	form.setUseGradeIndex(tdata.useGradeIndex);
	            }
//	        tdata= tdata.setGrade_name(FirstCheckDAO.queryGradeName( conn, tdata.getUseGradeIndex() ));
	        String useGradeIndex = form.getUseGradeIndex();
	        String filename = tdata.getFile_name();
	        String  filelocation= tdata.getFile_location();
	        	
	     /*   form.setFileName(filename);
			form.setFileLocation(filelocation);*/
	        if( filename == null ||  useGradeIndex == null || filelocation == null)
	        	return mapping.findForward("failure");
	        
			FirstCheckForm data = null;
			
            MyTableRecord helloWorld = new MyTableRecord();
            String sTable = "";
			Map newMap = (Map)request.getAttribute( "errorValueMap" );
			StringBuffer sbOneTicket = new StringBuffer(0); 		
            
            if( newMap != null )
			{
				HashMap map = (HashMap)form.getValuesMap();
				sTable = helloWorld.MyTestFuncByMap(useGradeIndex, newMap );
			}
			else
			{
				if(isRecord.equals("txt")){
					int i =0;
					 ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-5));
					 ArrayList scoreListFujian = WorkPublicDAO.queryGradeScoreNewFujian(conn, filename.substring(0,filename.length()-5));
					 ArrayList scoreListFushen = WorkPublicDAO.queryGradeScoreNewFushen(conn, filename.substring(0,filename.length()-5));
//					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
					 if(scoreListFushen.size()>0){
							sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFushen);
					 }else if(scoreListFujian.size()>0){
						 sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFujian);
					 }else{
							sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
					 }
				}else{
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
					ArrayList scoreListFujian = WorkPublicDAO.queryGradeScoreNewFujian(conn, filename.substring(0,filename.length()-4));
					ArrayList scoreListFushen = WorkPublicDAO.queryGradeScoreNewFushen(conn, filename.substring(0,filename.length()-4));
					if(scoreListFushen.size()>0){
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFushen);
					}else if(scoreListFujian.size()>0){
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFujian);
					}else{
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
					}
				 }
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getTeachGradeRemarkNew(conn, gradeId);
			}
			
			String gradeRemarkFJ = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemarkFJ == null )
			{
				gradeRemarkFJ = WorkPublicDAO.getTeachGradeRemarkNewFJ(conn, gradeId);
			}
			form.setGradeRemark(gradeRemark);
			form.setFujianRemark(gradeRemarkFJ);
            // get grade table
           
            request.setAttribute("sTable", sTable);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
    		}else{
    			form.setSerialNumber(filename.substring(0,filename.length()-4));
    		}
           // form.setSerialNumber(filename.substring(0,filename.length()-4));
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(tdata.getUseGradeIndex());
            
		} catch (Exception e) {
			LogUtil.error("Fail to viewTeachRecord", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
		}
		if(isRecord==null||isRecord.length()==0||isRecord.equals("txt")){
			//System.out.println("this is the txt");
			return mapping.findForward("successtxt");
		}
		else{
//			System.out.println("this is the mp3");
			return mapping.findForward("success");
		}
	}	
	private ActionForward listZhijianyuanFJRecord(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		Connection conn = null;
		
		String isRecord = null;
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		
		/*if(gradeId.substring(gradeId.length()-1, gradeId.length()).equals(".")){
			gradeId=gradeId.substring(0, gradeId.length()-1);
		}*/
		
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			
			String gradeId = request.getParameter("gradeId");
			isRecord = FirstCheckDAO.queryIsMp3( conn, gradeId );	
			if(isRecord.equals("txt")){
				gradeId = gradeId.substring(0,gradeId.length()-5);
			}else{
				gradeId = gradeId.substring(0,gradeId.length()-4);
			}
			
			GradeData tdata = FirstCheckDAO.queryTeachRecordInfo( conn, gradeId );
			
			String gradeName = (String)FirstCheckDAO.queryGradeName( conn, tdata.getUseGradeIndex() );
			//String gradeName = tdata.getGrade_name();
	            if( tdata != null )
	            {
	            	request.setAttribute( "TaskGradeIndex", gradeName);
	            	form.setUseGradeIndex(tdata.useGradeIndex);
	            }
//	        tdata= tdata.setGrade_name(FirstCheckDAO.queryGradeName( conn, tdata.getUseGradeIndex() ));
	        String useGradeIndex = form.getUseGradeIndex();
	        String filename = tdata.getFile_name();
	        String  filelocation= tdata.getFile_location();
	        	
	     /*   form.setFileName(filename);
			form.setFileLocation(filelocation);*/
	        if( filename == null ||  useGradeIndex == null || filelocation == null)
	        	return mapping.findForward("failure");
	        
			FirstCheckForm data = null;
			
            MyTableRecord helloWorld = new MyTableRecord();
            String sTable = "";
			Map newMap = (Map)request.getAttribute( "errorValueMap" );
			StringBuffer sbOneTicket = new StringBuffer(0); 		
            
            if( newMap != null )
			{
				HashMap map = (HashMap)form.getValuesMap();
				sTable = helloWorld.MyTestFuncByMap(useGradeIndex, newMap );
			}
			else
			{
				if(isRecord.equals("txt")){
					int i =0;
					 ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-5));
					 ArrayList scoreListFujian = WorkPublicDAO.queryGradeScoreNewFujian(conn, filename.substring(0,filename.length()-5));
					 ArrayList scoreListFushen = WorkPublicDAO.queryGradeScoreNewFushen(conn, filename.substring(0,filename.length()-5));
//					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
					 if(scoreListFushen.size()>0){
							sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFushen);
					 }else if(scoreListFujian.size()>0){
						 sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFujian);
					 }else{
							sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
					 }
				}else{
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
					ArrayList scoreListFujian = WorkPublicDAO.queryGradeScoreNewFujian(conn, filename.substring(0,filename.length()-4));
					ArrayList scoreListFushen = WorkPublicDAO.queryGradeScoreNewFushen(conn, filename.substring(0,filename.length()-4));
					if(scoreListFushen.size()>0){
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFushen);
					}else if(scoreListFujian.size()>0){
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFujian);
					}else{
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
					}
				 }
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getTeachGradeRemarkNew(conn, gradeId);
			}
			
			String gradeRemarkFJ = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemarkFJ == null )
			{
				gradeRemarkFJ = WorkPublicDAO.getTeachGradeRemarkNewFJ(conn, gradeId);
			}
			form.setGradeRemark(gradeRemark);
			form.setFujianRemark(gradeRemarkFJ);
            // get grade table
           
            request.setAttribute("sTable", sTable);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
    		}else{
    			form.setSerialNumber(filename.substring(0,filename.length()-4));
    		}
           // form.setSerialNumber(filename.substring(0,filename.length()-4));
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(tdata.getUseGradeIndex());
            
		} catch (Exception e) {
			LogUtil.error("Fail to viewTeachRecord", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
		}
		if(isRecord==null||isRecord.length()==0||isRecord.equals("txt")){
			//System.out.println("this is the txt");
			return mapping.findForward("successtxt");
		}
		else{
//			System.out.println("this is the mp3");
			return mapping.findForward("success");
		}
	}	
	
	private ActionForward listAgentRecord(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		Connection conn = null;
		
		String isRecord = null;
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		
		/*if(gradeId.substring(gradeId.length()-1, gradeId.length()).equals(".")){
			gradeId=gradeId.substring(0, gradeId.length()-1);
		}*/
		
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			
			String gradeId = request.getParameter("gradeId");
			isRecord = FirstCheckDAO.queryIsMp3( conn, gradeId );	

			if(isRecord.equals("txt")){
				gradeId = gradeId.substring(0,gradeId.length()-5);
			}else{
				gradeId = gradeId.substring(0,gradeId.length()-4);
			}
			
			GradeData tdata = FirstCheckDAO.queryTeachRecordInfo( conn, gradeId );
			String score_state = FirstCheckDAO.getScoreState(conn, gradeId, isRecord);
			String gradeName = (String)FirstCheckDAO.queryGradeName( conn, tdata.getUseGradeIndex() );
			//String gradeName = tdata.getGrade_name();
	            if( tdata != null )
	            {
	            	request.setAttribute( "TaskGradeIndex", gradeName);
	            	form.setUseGradeIndex(tdata.useGradeIndex);
	            }
//	        tdata= tdata.setGrade_name(FirstCheckDAO.queryGradeName( conn, tdata.getUseGradeIndex() ));
	        String useGradeIndex = form.getUseGradeIndex();
	        String filename = tdata.getFile_name();
	        String  filelocation= tdata.getFile_location();
	        	
	     /*   form.setFileName(filename);
			form.setFileLocation(filelocation);*/
	        if( filename == null ||  useGradeIndex == null || filelocation == null)
	        	return mapping.findForward("failure");
	        
			FirstCheckForm data = null;
			
            MyTableRecord helloWorld = new MyTableRecord();
            String sTable = "";
			Map newMap = (Map)request.getAttribute( "errorValueMap" );
			StringBuffer sbOneTicket = new StringBuffer(0); 		
            
            if( newMap != null )
			{
				HashMap map = (HashMap)form.getValuesMap();
				sTable = helloWorld.MyTestFuncByMap(useGradeIndex, newMap );
			}
			else
			{
				if(isRecord.equals("txt")){
					int i =0;
					ArrayList allList = new ArrayList();
					 ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-5));
					 ArrayList scoreListFujian = WorkPublicDAO.queryGradeScoreNewFujian(conn, filename.substring(0,filename.length()-5));
					 ArrayList scoreListFushen = WorkPublicDAO.queryGradeScoreNewFushen(conn, filename.substring(0,filename.length()-5));
//					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
					 if(score_state.equals("上诉成功")){
							if(scoreListFushen.size()>0){
								
								for(int x=0;x<scoreListFushen.size();x++){
									if(scoreList.size()>0){
										String fushen= (String) scoreListFushen.get(x);
										String chujian = (String) scoreList.get(x);
										allList.add(chujian+"--"+fushen);
									}
								}
								sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),allList);
							}else if(scoreListFujian.size()>0){
								sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFujian);
							}else{
								sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
							}
					 }else{
						 if(scoreListFushen.size()>0){
								sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFushen);
						 }else if(scoreListFujian.size()>0){
							 sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFujian);
						 }else{
								sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
						 }
					 }
					
				}else{
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
					ArrayList scoreListFujian = WorkPublicDAO.queryGradeScoreNewFujian(conn, filename.substring(0,filename.length()-4));
					ArrayList scoreListFushen = WorkPublicDAO.queryGradeScoreNewFushen(conn, filename.substring(0,filename.length()-4));
					if(scoreListFushen.size()>0){
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFushen);
					}else if(scoreListFujian.size()>0){
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFujian);
					}else{
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
					}
				 }
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getTeachGradeRemarkNew(conn, gradeId);
			}
			
			String gradeRemarkFJ = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemarkFJ == null )
			{
				gradeRemarkFJ = WorkPublicDAO.getTeachGradeRemarkNewFJ(conn, gradeId);
			}
			form.setGradeRemark(gradeRemark);
			form.setFujianRemark(gradeRemarkFJ);
            // get grade table
           
            request.setAttribute("sTable", sTable);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
    		}else{
    			form.setSerialNumber(filename.substring(0,filename.length()-4));
    		}
           // form.setSerialNumber(filename.substring(0,filename.length()-4));
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(tdata.getUseGradeIndex());
            
		} catch (Exception e) {
			LogUtil.error("Fail to viewTeachRecord", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
		}
		if(isRecord==null||isRecord.length()==0||isRecord.equals("txt")){
			//System.out.println("this is the txt");
			return mapping.findForward("successtxt");
		}
		else{
//			System.out.println("this is the mp3");
			return mapping.findForward("success");
		}
	}	
	private ActionForward viewAgentWorkRecord(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException {
        
		
	/*	if(gradeId.substring(gradeId.length()-1, gradeId.length()).equals(".")){
			gradeId=gradeId.substring(0, gradeId.length()-1);
		}*/
	
		String isRecord = null;
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		String fileName=request.getParameter("filename");
		String id=request.getParameter("gradeId");
		if(fileName !=null && !fileName.equals("")){
			if(!fileName.equals(form.getFileName())){
				form.setFileName(fileName);
			}
		}
		Connection conn = null;
		if(form.getFileName()== null ){
			form.setFileName(form.getGradeId());
		}
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			
			String gradeId = request.getParameter("gradeId");
			isRecord = FirstCheckDAO.queryIsMp3( conn, form.getFileName());
			if(isRecord == null){
				isRecord = FirstCheckDAO.queryIsMp3Id( conn, form.getFileName(),".html");
				if(isRecord == null){
					isRecord = FirstCheckDAO.queryIsMp3Id( conn, form.getFileName(),".mp3");
				}
			}
			if(form.getFileName().equals(form.getGradeId())){
				if(isRecord.equals("txt")){
					gradeId = gradeId.substring(0,gradeId.length()-5);
				}else{
					gradeId = gradeId.substring(0,gradeId.length()-4);
				}
			}
			GradeData tdata = FirstCheckDAO.queryTeachRecordInfo( conn, gradeId );
			String score_state = FirstCheckDAO.getScoreState(conn, gradeId, isRecord);
			String gradeName = (String)FirstCheckDAO.queryGradeName( conn, tdata.getUseGradeIndex() );
			//String gradeName = tdata.getGrade_name();
	            if( tdata != null )
	            {
	            	request.setAttribute( "TaskGradeIndex", gradeName);
	            	form.setUseGradeIndex(tdata.useGradeIndex);
	            }
//	        tdata= tdata.setGrade_name(FirstCheckDAO.queryGradeName( conn, tdata.getUseGradeIndex() ));
	        String useGradeIndex = form.getUseGradeIndex();
	        String filename = tdata.getFile_name();
	        String  filelocation= tdata.getFile_location();
	        
	     /*   form.setFileName(filename);
			form.setFileLocation(filelocation);*/
	        if( filename == null ||  useGradeIndex == null || filelocation == null)
	        	return mapping.findForward("failure");
	        
			FirstCheckForm data = null;
			
            MyTableRecord helloWorld = new MyTableRecord();
            String sTable = "";
			Map newMap = (Map)request.getAttribute( "errorValueMap" );
			StringBuffer sbOneTicket = new StringBuffer(0); 		
            
            if( newMap != null )
			{
            	//ArrayList onetkList = WorkPublic.getOneTicketList( conn, request, useGradeIndex, sbOneTicket.toString(), 
				//WorkPublic.getResString( getResources(request), "DZ.Property" ),
				//WorkPublic.getResString( getResources(request), "DZ.OneTicketDisable" ) );		
            	
				HashMap map = (HashMap)form.getValuesMap();
				sTable = helloWorld.MyTestFuncByMap(useGradeIndex, newMap );
			}
			else
			{
				if(isRecord.equals("txt")){
					ArrayList allList = new ArrayList();
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-5));
					ArrayList scoreListFujian = WorkPublicDAO.queryGradeScoreNewFujian(conn, filename.substring(0,filename.length()-5));
					ArrayList scoreListFushen = WorkPublicDAO.queryGradeScoreNewFushen(conn, filename.substring(0,filename.length()-5));
//					sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
					/*if(score_state.equals("上诉成功")){
						if(scoreListFushen.size()>0){
							
							for(int i=0;i<scoreListFushen.size();i++){
								if(scoreList.size()>0){
									String fushen= (String) scoreListFushen.get(i);
									String chujian = (String) scoreList.get(i);
									allList.add(chujian+"--"+fushen);
								}
							}
							sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),allList);
						}else if(scoreListFujian.size()>0){
							sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFujian);
						}else{
							sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
						}
					}else{*/
						if(scoreListFushen.size()>0){
							sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFushen);
						}else if(scoreListFujian.size()>0){
							sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFujian);
						}else{
							sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
						}
					//}
					
				}else{
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
					ArrayList scoreListFujian = WorkPublicDAO.queryGradeScoreNewFujian(conn, filename.substring(0,filename.length()-4));
					ArrayList scoreListFushen = WorkPublicDAO.queryGradeScoreNewFushen(conn, filename.substring(0,filename.length()-4));
					if(scoreListFushen.size()>0){
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFushen);
					}else if(scoreListFujian.size()>0){
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFujian);
					}else{
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
					}
				}
				 
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getTeachGradeRemarkNew(conn, gradeId);
			}
			String gradeRemarkFJ = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemarkFJ == null )
			{
				gradeRemarkFJ = WorkPublicDAO.getTeachGradeRemarkNewFJ(conn, gradeId);
			}
			String agentArguRemark=WorkPublicDAO.getAgentArguRemark(conn, gradeId);
			form.setGradeRemark(gradeRemark);
			form.setFujianRemark(gradeRemarkFJ);
			form.setAgentArguRemark(agentArguRemark);
			
			String adminRemark=WorkPublicDAO.getAdminRemark(conn, gradeId);
			form.setAdminAgentArguRemark(adminRemark);
			
            // get grade table
           
            request.setAttribute("sTable", sTable);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            }
            
            
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(tdata.getUseGradeIndex());
            
		} catch (Exception e) {
			LogUtil.error("Fail to viewAgentWorkRecord", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
		}
		if(isRecord==null||isRecord.length()==0||isRecord.equals("txt")){
//			System.out.println("this is the txt");
			return mapping.findForward("successtxt");
		}
		else{
//			System.out.println("this is the mp3");
			return mapping.findForward("success");
		}
	}
	
	private ActionForward viewAgentWorkRecordNew(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException {
		String isRecord = null;
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		String fileName=request.getParameter("filename");
		if(fileName !=null ||fileName.equals("")){
			if(!fileName.equals(form.getFileName())){
				form.setFileName(fileName);
			}
		}
		Connection conn = null;
		Connection conn1 = null;
		if(form.getFileName()== null ){
			form.setFileName(form.getGradeId());
		}
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			conn1 = DAOTools.getConnectionOutS("ZQC");
			String gradeId = request.getParameter("gradeId");
			isRecord = FirstCheckDAO.queryIsMp3( conn, form.getFileName());
			if(form.getFileName().equals(form.getGradeId())){
				if(isRecord.equals("txt")){
					gradeId = gradeId.substring(0,gradeId.length()-5);
				}else{
					gradeId = gradeId.substring(0,gradeId.length()-4);
				}
			}
			GradeData tdata = FirstCheckDAO.getAgentArguementWorkGradeData( conn, gradeId,isRecord );
			tdata.setUseGradeIndex(FirstCheckDAO.queryAgentGradeId( conn, tdata.getGrade_name()));
			//GradeData tdata = FirstCheckDAO.queryTeachRecordInfo( conn, gradeId );
			String gradeName =  tdata.getGrade_name();
	        if( tdata != null )
	        {
	            request.setAttribute( "TaskGradeIndex", gradeName);
	            form.setUseGradeIndex(tdata.useGradeIndex);
	        }
	        String useGradeIndex = form.getUseGradeIndex();
	        String filename = tdata.getFile_name();
	        String  filelocation= tdata.getFile_location();

	        if( filename == null ||  useGradeIndex == null || filelocation == null)
	        	return mapping.findForward("failure");
            MyTableRecord helloWorld = new MyTableRecord();
            String sTable = "";
			Map newMap = (Map)request.getAttribute( "errorValueMap" );
			StringBuffer sbOneTicket = new StringBuffer(0); 		
            
            if( newMap != null )
			{
				HashMap map = (HashMap)form.getValuesMap();
				sTable = helloWorld.MyTestFuncByMap(useGradeIndex, newMap );
			}
			else
			{
				if(isRecord.equals("txt")){
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-5));
					ArrayList scoreListFujian = WorkPublicDAO.queryGradeScoreNewFujian(conn, filename.substring(0,filename.length()-5));
					ArrayList scoreListFushen = WorkPublicDAO.queryGradeScoreNewFushen(conn, filename.substring(0,filename.length()-5));
					if(scoreListFushen.size()>0){
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFushen);
					}else if(scoreListFujian.size()>0){
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFujian);
					}else{
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
					}
				}else{
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
					ArrayList scoreListFujian = WorkPublicDAO.queryGradeScoreNewFujian(conn, filename.substring(0,filename.length()-4));
					ArrayList scoreListFushen = WorkPublicDAO.queryGradeScoreNewFushen(conn, filename.substring(0,filename.length()-4));
					if(scoreListFushen.size()>0){
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFushen);
					}else if(scoreListFujian.size()>0){
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreListFujian);
					}else{
						sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
					}
				}
			}
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getTeachGradeRemarkNew(conn1, gradeId);
			}
			String gradeRemarkFJ = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemarkFJ == null )
			{
				gradeRemarkFJ = WorkPublicDAO.getTeachGradeRemarkNewFJ(conn1,  gradeId);
			}
			String agentArguRemark=WorkPublicDAO.getAgentArguRemark(conn, gradeId);
			form.setGradeRemark(gradeRemark);
			form.setFujianRemark(gradeRemarkFJ);
			form.setAgentArguRemark(agentArguRemark);
			
			String adminRemark=WorkPublicDAO.getAdminRemark(conn, gradeId);
			form.setAdminAgentArguRemark(adminRemark);
            request.setAttribute("sTable", sTable);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            }
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(tdata.getUseGradeIndex());
		} catch (Exception e) {
			LogUtil.error("Fail to viewAgentWorkRecord", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
		}
		if(isRecord==null||isRecord.length()==0||isRecord.equals("txt")){
			return mapping.findForward("successtxt");
		}
		else{
			return mapping.findForward("success");
		}
	}
	
	private ActionForward saveArgu(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException {
		
		FirstCheckForm form = (FirstCheckForm)actionForm;        
		Connection conn = null;
		String isRecord = null;
//		UserInfo userInfo_=null;
		String gradeId = form.getGradeId();
		
		String agentArguement = form.getAgentArguRemark();
		String filename = form.getFileName();
		try {
	          
			conn = DAOTools.getConnectionOutS("ZQC");        
			String gradeRemark = FirstCheckDAO.queryRecordGradeRemark(conn,gradeId);
			String gradeRemarkFJ = FirstCheckDAO.queryRecordGradeRemarkFJ(conn,gradeId);
//			userInfo_=new UserInfo();
//			String a=userInfo_.userRole;
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
			FirstCheckForm data = FirstCheckDAO.getAgentArguementWork( conn,   gradeId ,isRecord);  
			String gradeName = data.getGradeName();
			String id = FirstCheckDAO.queryAgentGradeId(conn,gradeName);
			//String gradeName = tdata.getGrade_name();
	            if( data != null )
	            {
	            	request.setAttribute( "TaskGradeIndex", gradeName);
	            	form.setUseGradeIndex(id);
	            }
            FirstCheckDAO.saveArgu( conn, data,agentArguement,gradeRemark,gradeRemarkFJ,isRecord);
        	FirstCheckDAO.updateAgentArgumentScoreState(conn,gradeId,isRecord);
        	FirstCheckDAO.saveGradeScoreNewFS( conn, form,isRecord);
        	//FirstCheckDAO.updateAgentArgumentScoreState2(conn, gradeId);
        	
          
		} catch (SQLException e) {
			LogUtil.error("Fail to save arguement", SKIP_Logger);
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
		}
		return mapping.findForward("success");
	}	 
	private ActionForward viewAdminAgentWorkRecord(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException {
        
		String gradeId = request.getParameter("gradeId");
		String isRecord = null;
		String userName = request.getParameter("userName");
		String reowner = request.getParameter("reowner");
		//String userArgs = request.getParameter("userArgs");
		//String userArgs = FirstCheckDAO.queryUserArgs( userName );
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
//		System.out.println(userName);
		String userArgsName = (String)request.getAttribute("userArgs");
		
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			String userArgs = "";
			if(!(userName==null)){
				userArgs = FirstCheckDAO.queryUserArgs( userName );
			}else {
				userArgs = userArgsName;
			}
			
			GradeData tdata = FirstCheckDAO.queryAdminAgentRecordInfo( conn, gradeId );
			
			String gradeName = (String)FirstCheckDAO.queryAdminAgentName( conn, tdata.getUseGradeIndex() );
			//String gradeName = tdata.getGrade_name();
	            if( tdata != null )
	            {
	            	request.setAttribute( "TaskGradeIndex", gradeName);
	            	form.setUseGradeIndex(tdata.useGradeIndex);
	            }
//	        tdata= tdata.setGrade_name(FirstCheckDAO.queryGradeName( conn, tdata.getUseGradeIndex() ));
	        String useGradeIndex = form.getUseGradeIndex();
	        String filename = tdata.getFile_name();
	        String  filelocation= tdata.getFile_location();
	        isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
//	        System.out.println("isRecord==="+isRecord);
	     /*   form.setFileName(filename);
			form.setFileLocation(filelocation);*/
	        
	        form.setScoreState(FirstCheckDAO.getScoreState( conn, gradeId,isRecord));
			request.setAttribute("scoreState", form.getScoreState());
	        if( filename == null ||  useGradeIndex == null || filelocation == null)
	        	return mapping.findForward("failure");
	        
			FirstCheckForm data = null;
			
            MyTableRecord helloWorld = new MyTableRecord();
            String sTable = "";
			Map newMap = (Map)request.getAttribute( "errorValueMap" );
			StringBuffer sbOneTicket = new StringBuffer(0); 		
            
            if( newMap != null )
			{
				HashMap map = (HashMap)form.getValuesMap();
				sTable = helloWorld.AdminAgentMyTestFuncByMap(useGradeIndex, newMap );
			}
			else
			{	if(isRecord.equals("txt")){
				 ArrayList scoreList = WorkPublicDAO.queryAdminAgentInfo(conn, filename.substring(0,filename.length()-5));
				 ArrayList scoreListFujian = WorkPublicDAO.queryGradeScoreNewFujian(conn, filename.substring(0,filename.length()-5));
				 ArrayList scoreListFushen = WorkPublicDAO.queryGradeScoreNewFushen(conn, filename.substring(0,filename.length()-5));
//				 sTable = helloWorld.AdminAgentMyTestFunc(tdata.getUseGradeIndex(),scoreList);
				 if(scoreListFushen.size()>0){
						sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreListFushen);
				 }else if(scoreListFujian.size()>0){
						sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreListFujian);
				 }else{
						sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				 }
			}else{
				ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
				ArrayList scoreListFujian = WorkPublicDAO.queryGradeScoreNewFujian(conn, filename.substring(0,filename.length()-4));
				if(scoreListFujian.size()>0){
					sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreListFujian);
				}else{
					sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}
			}
				
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getTeachGradeRemarkNew(conn, gradeId);
			}
			String gradeRemarkFJ = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemarkFJ == null )
			{
				gradeRemarkFJ = WorkPublicDAO.getTeachGradeRemarkNewFJ(conn, gradeId);
			}
			String agentArguRemark=WorkPublicDAO.getAgentArguRemark(conn, gradeId);
//			String fjRemark = WorkPublicDAO.getFuJianRemark(conn, gradeId);
			String adminAgentArguRemark=WorkPublicDAO.getAdminAgentGradeRemarkNew(conn, gradeId);
			form.setGradeRemark(gradeRemark);
			form.setAgentArguRemark(agentArguRemark);
			form.setAdminAgentArguRemark(adminAgentArguRemark);
			form.setFujianRemark(gradeRemarkFJ);
			
			
            // get grade table
           
            request.setAttribute("sTable", sTable);
            request.setAttribute("userArgs", userArgs);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            }
            
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(tdata.getUseGradeIndex());
            
		} catch (Exception e) {
			LogUtil.error("Fail to viewAdminAgentWorkRecord", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
		}
		if(isRecord==null||isRecord.length()==0||isRecord.equals("txt")){
//			System.out.println("this is the txt");
			return mapping.findForward("successtxt");
		}
		else{
//			System.out.println("this is the mp3");
			return mapping.findForward("success");
	}
}
	private ActionForward viewAgentWorkPeiXunText(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException {
		String isRecord = null;
		Connection conn = null;
		
		String gradeId = request.getParameter("gradeId");
	
		FirstCheckForm form = (FirstCheckForm)actionForm; 
	
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			isRecord = FirstCheckDAO.queryIsMp3( conn, gradeId );
			if(isRecord == null){
				isRecord = FirstCheckDAO.queryIsMp3( conn,  form.getFileName());
			}else{
				if(isRecord.equals("txt")){
					gradeId = gradeId.substring(0,gradeId.length()-5);
				}else{
					gradeId = gradeId.substring(0,gradeId.length()-4);
				}
			}
			
			GradeData tdata = FirstCheckDAO.queryAdminAgentRecordInfo( conn, gradeId );
					
			String gradeName = (String)FirstCheckDAO.queryAdminAgentName( conn, tdata.getUseGradeIndex() );
			//String gradeName = tdata.getGrade_name();
	            if( tdata != null )
	            {
	            	request.setAttribute( "TaskGradeIndex", gradeName);
	            	form.setUseGradeIndex(tdata.useGradeIndex);
	            }
//	        tdata= tdata.setGrade_name(FirstCheckDAO.queryGradeName( conn, tdata.getUseGradeIndex() ));
	        String useGradeIndex = form.getUseGradeIndex();
	        String filename = tdata.getFile_name();
	        String  filelocation= tdata.getFile_location();
	        isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
	        System.out.println("isRecord==="+isRecord);
	     /*   form.setFileName(filename);
			form.setFileLocation(filelocation);*/
	        if( filename == null ||  useGradeIndex == null || filelocation == null)
	        	return mapping.findForward("failure");
	        
			FirstCheckForm data = null;
			
            MyTableRecord helloWorld = new MyTableRecord();
            String sTable = "";
			Map newMap = (Map)request.getAttribute( "errorValueMap" );
			StringBuffer sbOneTicket = new StringBuffer(0); 		
            
            if( newMap != null )
			{
				HashMap map = (HashMap)form.getValuesMap();
				sTable = helloWorld.AdminAgentMyTestFuncByMap(useGradeIndex, newMap );
			}
			else
			{	if(isRecord.equals("txt")){
				 ArrayList scoreList = WorkPublicDAO.queryAdminAgentInfo(conn, filename.substring(0,filename.length()-5));
				 //sTable = helloWorld.AdminAgentMyTestFunc(tdata.getUseGradeIndex(),scoreList);
				 sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
			}else{
				 ArrayList scoreList = WorkPublicDAO.queryAdminAgentInfo(conn, filename.substring(0,filename.length()-4));
				 //sTable = helloWorld.AdminAgentMyTestFunc(tdata.getUseGradeIndex(),scoreList);
				 sTable = helloWorld.MyTestFuncView(tdata.getUseGradeIndex(),scoreList);
			}
				
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getTeachGradeRemarkNew(conn, gradeId);
			}
//			String agentArguRemark=WorkPublicDAO.getAgentArguRemark(conn, gradeId);
//			String fjRemark = WorkPublicDAO.getFuJianRemark(conn, gradeId);
//			String adminAgentArguRemark=WorkPublicDAO.getAdminAgentGradeRemarkNew(conn, gradeId);
			form.setGradeRemark(gradeRemark);
//			form.setAgentArguRemark(agentArguRemark);
//			form.setAdminAgentArguRemark(adminAgentArguRemark);
//			form.setFujianRemark(fjRemark);
			
			
            // get grade table
           
            request.setAttribute("sTable", sTable);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            }
            
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(tdata.getUseGradeIndex());
            int i =0;
		} catch (Exception e) {
			LogUtil.error("Fail to viewAdminAgentWorkRecord", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
		}
		if(isRecord==null||isRecord.length()==0||isRecord.equals("txt")){
//			System.out.println("this is the txt");
			return mapping.findForward("successtxt");
		}
		else{
//			System.out.println("this is the mp3");
			return mapping.findForward("success");
	}
}
	private ActionForward saveAdminAgentWorkRecord(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		
        FirstCheckForm form = (FirstCheckForm)actionForm;
		Connection conn = null;
		String isRecord=null;
		String filename=form.getFileName();
		String userArgs = (String)request.getParameter("userArgs");
		String reowner =request.getParameter("reowner");
		try {
	     
			conn = DAOTools.getConnectionOutS("ZQC");
			ArrayList errMsglist = new ArrayList();
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Empty") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Number") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.NoValidValid") );

	            if(!WorkPublic.validGradeData( request, conn, form.getValuesMap(), errMsglist ) )
	            {            	           
	            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
	     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
	                return mapping.findForward("failure");
	            }
	        boolean updateScoreState = false;
	        FirstCheckForm formSelect = new FirstCheckForm();
	        formSelect = (FirstCheckForm)FirstCheckDAO.queryFormInfo(conn, form.getGradeId());
	        Map mapSelect = formSelect.getValuesMap();
	        Map map = form.getValuesMap();
	        boolean changer = false;
	        boolean change1r ;
	        Iterator it = map.entrySet().iterator();
	        while (it.hasNext()){
	        	Map.Entry pairs = (Map.Entry)it.next();
	        	String key = pairs.getKey()+"";
	        	float value = Float.parseFloat(pairs.getValue()+"");
	        	
	        	//int n = key.charAt(key.length()-1);
	        	
	        	Iterator itSelect = mapSelect.entrySet().iterator();
	        	while (itSelect.hasNext()){
		        	Map.Entry pairsSelect = (Map.Entry)itSelect.next();
		        	String keySelect = pairsSelect.getKey()+"";
		        	if((pairsSelect.getValue()+"").equals("null") || pairsSelect.getValue()+"" == null){
		        		continue;
		        	}
		        	float valueSelect = Float.parseFloat(pairsSelect.getValue()+"");	        	
		        	//int nSelect = keySelect.charAt(keySelect.length()-1);
		        	if(key.charAt(key.length()-1)==keySelect.charAt(keySelect.length()-1)){
		        		if(value != valueSelect){
		        			changer = true;
		        			break;
		        		}
		        	}
	        	}
	        	if(changer){
	        		break;
	        	}
	        }


	        //updateScoreState    = FirstCheckDAO.updateGradeDetailNew(conn, form);
	        updateScoreState    = FirstCheckDAO.updateGradeDetailNewFs(conn, form);
	        if(updateScoreState==true){
	        	if(isRecord.equals("txt")){
	        		FirstCheckDAO.updateScoreStateText(conn,form, changer );
		        	FirstCheckDAO.updateScoreStateArgumentText(conn,form, changer );
		        	FirstCheckDAO.saveGradeScoreNewFS( conn, form,isRecord);
	        	}else{
	        		FirstCheckDAO.updateScoreState(conn,form, changer );
		        	FirstCheckDAO.updateScoreStateArgument(conn,form, changer );
		        	FirstCheckDAO.saveGradeScoreNewFS( conn, form,isRecord);
	        	}
	        	
	        } 
	        double total = 0;
	        if(isRecord.equals("mp3")){
	        	total = WorkPublic.getCountTotal( conn, form.getUseGradeIndex(), form.getValuesMap(), 0 );
	        }else{
	        	total = WorkPublic.getCountTotalNew( conn, form.getUseGradeIndex(), form.getValuesMap(), 0 );
	        }
	        DecimalFormat df = new DecimalFormat("#.00"); 
	        String strTotal = "";
	         
            if(total<=0){
            	 strTotal = "0.00";
            }else{          
            	strTotal = df.format(total);     
            }	        
            
			setErrorMSG2( request, "errors.Msg.ScoreTotal", " : "+strTotal );
			form.setPfxTotal(strTotal);
			
	        FirstCheckDAO.updateAgentArgumentNew(conn, form);
	        FirstCheckDAO.updatePfxTotal(conn,form);
	        //FirstCheckDAO.updateAgentArgumentScoreState2(conn, form.getGradeId());
	        if(!(userArgs ==null)){
		        if(userArgs.equals("false")){
		        	request.setAttribute("userArgs", "质检员");
		        }else{
		        	request.setAttribute("userArgs", "质检组长");
		        }
	        }
            request.setAttribute("filename", form.getFileName());
            request.setAttribute("filelocation", form.getFileLocation());
            
           request.setAttribute("gradeIndex",form.getUseGradeIndex());
           
		} catch (SQLException e) {
			LogUtil.error("Fail to saveadminwork", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
				if (conn != null) {
					DAOTools.releaseConnectionOutS("ZQC", conn);
				}
		}
		return mapping.findForward("success");
	}	
	
	private ActionForward viewWorkFJ(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		String gradeId = request.getParameter("gradeId");
		String gradeIndex =request.getParameter("gradeIndex");
		String isRecord = null;
		if(gradeIndex==null){
			gradeIndex =(String)request.getAttribute("gradeIndex");
		}
		String filename =request.getParameter("filename");
		if(filename==null)
		{
			filename =(String)request.getAttribute("filename");
		}
		String filelocation= request.getParameter("filelocation");
		if(filelocation==null)
		{
			filelocation =(String)request.getAttribute("filelocation");
		}
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		
		
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
			GradeData tdata = FirstCheckDAO.queryFirstTaskInfoNew( conn, gradeIndex );
	            if( tdata != null )
	            {
	            	request.setAttribute( "TaskGradeIndex", tdata.useGradeIndexName);
	            	form.setUseGradeIndex(tdata.useGradeIndex);
	            }
	        String useGradeIndex = form.getUseGradeIndex();
	        form.setFileName(filename);
			form.setFileLocation(filelocation);
	        if( filename == null ||  useGradeIndex == null || filelocation == null)
	        	return mapping.findForward("failure");
	        
			FirstCheckForm data = null;
			
            MyTable helloWorld = new MyTable();
            String sTable = "";
			Map newMap = (Map)request.getAttribute( "errorValueMap" );
			StringBuffer sbOneTicket = new StringBuffer(0); 		

					
			if( newMap != null )
			{
				HashMap map = (HashMap)form.getValuesMap();
				sTable = helloWorld.MyTestFuncByHashMap(tdata.getUseGradeIndex(), newMap, map );
			}
			else
			{
				if(isRecord.equals("txt")){
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-5));
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}else{
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}
				 
			}
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getGradeRemarkNew(conn, gradeId);
			}
		/*	String gradeRemarkFJ = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemarkFJ == null )
			{
				gradeRemarkFJ = WorkPublicDAO.getGradeRemarkNewFJ(conn, gradeId);
			}*/
			form.setGradeRemark(gradeRemark);
//			form.setFujianRemark(gradeRemarkFJ);
			
            // get grade table
           
            request.setAttribute("sTable", sTable);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            }
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(tdata.getUseGradeIndex());
            
		} catch (Exception e) {
			LogUtil.error("Fail to viewwork", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
//			try {
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				logger.warn(e);
//			}
		}
		if(isRecord==null||isRecord.length()==0||isRecord.equals("txt")){
//			System.out.println("this is the txt");
			return mapping.findForward("successtxt");
		}
		else{
//			System.out.println("this is the mp3");
			return mapping.findForward("success");
		}
	}	
	private ActionForward viewWorkFJWENBEN(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		String gradeId = request.getParameter("gradeId");
		String gradeIndex =request.getParameter("gradeIndex");
		String isRecord = null;
		if(gradeIndex==null){
			gradeIndex =(String)request.getAttribute("gradeIndex");
		}
		String filename =request.getParameter("filename");
		if(filename==null)
		{
			filename =(String)request.getAttribute("filename");
		}
		String filelocation= request.getParameter("filelocation");
		if(filelocation==null)
		{
			filelocation =(String)request.getAttribute("filelocation");
		}
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		
		
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
			GradeData tdata = FirstCheckDAO.queryFirstTaskInfoNew( conn, gradeIndex );
	            if( tdata != null )
	            {
	            	request.setAttribute( "TaskGradeIndex", tdata.useGradeIndexName);
	            	form.setUseGradeIndex(tdata.useGradeIndex);
	            }
	        String useGradeIndex = form.getUseGradeIndex();
	        form.setFileName(filename);
			form.setFileLocation(filelocation);
	        if( filename == null ||  useGradeIndex == null || filelocation == null)
	        	return mapping.findForward("failure");
	        
			FirstCheckForm data = null;
			
            MyTable helloWorld = new MyTable();
            String sTable = "";
			Map newMap = (Map)request.getAttribute( "errorValueMap" );
			StringBuffer sbOneTicket = new StringBuffer(0); 		

					
			if( newMap != null )
			{
				HashMap map = (HashMap)form.getValuesMap();
				sTable = helloWorld.MyTestFuncByHashMap(tdata.getUseGradeIndex(), newMap, map );
			}
			else
			{
				if(isRecord.equals("txt")){
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-5));
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}else{
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}
				 
			}
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getGradeRemarkNew(conn, gradeId);
			}
			String gradeRemarkFJ = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemarkFJ == null )
			{
				gradeRemarkFJ = WorkPublicDAO.getGradeRemarkNewFJ(conn, gradeId);
			}
			form.setGradeRemark(gradeRemark);
			form.setFujianRemark(gradeRemarkFJ);
			
            // get grade table
           
            request.setAttribute("sTable", sTable);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            
            form.setGradeId(gradeId);
            if(isRecord.equals("txt")){
            	form.setSerialNumber(filename.substring(0,filename.length()-5));
            }else{
            	form.setSerialNumber(filename.substring(0,filename.length()-4));
            }
            form.setFileName(filename);
            form.setFileLocation(filelocation);
            form.setUseGradeIndex(tdata.getUseGradeIndex());
            
		} catch (Exception e) {
			LogUtil.error("Fail to viewwork", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
			if (conn != null) {
				DAOTools.releaseConnectionOutS("ZQC", conn);
			}
//			try {
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				logger.warn(e);
//			}
		}
		if(isRecord==null||isRecord.length()==0||isRecord.equals("txt")){
//			System.out.println("this is the txt");
			return mapping.findForward("successtxt");
		}
		else{
//			System.out.println("this is the mp3");
			return mapping.findForward("success");
		}
	}
	private ActionForward saveWorkFJ(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		
        FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		String isRecord =null;
		int sumkeyInt =0;
		String filename = form.getFileName();
		try {
	          
			conn = DAOTools.getConnectionOutS("ZQC");
			ArrayList errMsglist = new ArrayList();
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Empty") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Number") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.NoValidValid") );
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
	            if(!WorkPublic.validGradeData( request, conn, form.getValuesMap(), errMsglist ) )
	            {            	           
	            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
	     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
	     		   request.setAttribute("errorValueRemarkFJ", form.getFujianRemark());
	                return mapping.findForward("failure");
	            }
	            
	           /* Map map = form.getValuesMap();
		          Set set = map.entrySet();
		            Iterator it = set.iterator();	
		           
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
		     
		                sumkeyInt =(int) (sumkeyInt+ keyInt);
		            }*/
	             sumkeyInt = FirstCheckDAO.getItemSum(form);
            if(FirstCheckDAO.saveGradeScoreNewFJ( conn, form,isRecord)){
            	 if(isRecord.equals("mp3")){
            		 int countNumber = FirstCheckDAO.CountFileNameNumberRS(conn, form,isRecord);
            		 int sumNumber = FirstCheckDAO.sumFileNameNumberRS(conn, form,isRecord); 
	            	 FirstCheckDAO.calAvgPfxScoreRS( conn, form,isRecord,countNumber,sumNumber);
	            	 FirstCheckDAO.updatePfxTotalNew( conn, form,0);
            	 }else{
            		 FirstCheckDAO.updatePfxTotalNew( conn, form,sumkeyInt);
            	 }
            	 	 FirstCheckDAO.updateFJGradeScoreNew( conn, form);
            	 
            	 
            }
            request.setAttribute("filename", form.getFileName());
            request.setAttribute("filelocation", form.getFileLocation());
            
           request.setAttribute("gradeIndex",form.getUseGradeIndex());
		} catch (SQLException e) {
			LogUtil.error("Fail to savefujianwork", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
				if (conn != null) {
					DAOTools.releaseConnectionOutS("ZQC", conn);
				}
		}
		return mapping.findForward("success");
	}
	private ActionForward saveWorkFJWENBEN(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		
        FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		String isRecord =null;
		String filename = form.getFileName();
		String reowner = request.getParameter("reowner");
		int sumkeyInt=0;
		try {
	          
			conn = DAOTools.getConnectionOutS("ZQC");
			ArrayList errMsglist = new ArrayList();
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Empty") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Number") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.NoValidValid") );
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
	            if(!WorkPublic.validGradeData( request, conn, form.getValuesMap(), errMsglist ) )
	            {            	           
	            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
	     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
	     		   request.setAttribute("errorValueRemarkFJ", form.getFujianRemark());
	                return mapping.findForward("failure");
	            }
	            
	           /* Map map = form.getValuesMap();
		          Set set = map.entrySet();
		            Iterator it = set.iterator();	
		           
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
		     
		                sumkeyInt =(int) (sumkeyInt+ keyInt);
		            }*/
	            sumkeyInt = FirstCheckDAO.getItemSum(form);
	            
            if(FirstCheckDAO.saveGradeScoreNewFJ( conn, form,isRecord)){
            	 FirstCheckDAO.updateFJGradeScoreNew( conn, form);
            	 if(isRecord.equals("mp3")){
            		 FirstCheckDAO.updatePfxTotalNew( conn, form,0);
            	 }else{
            		 FirstCheckDAO.updatePfxTotalNew( conn, form,sumkeyInt);
            	 }
            	 
            }
            request.setAttribute("filename", form.getFileName());
            request.setAttribute("filelocation", form.getFileLocation());
            
           request.setAttribute("gradeIndex",form.getUseGradeIndex());
		} catch (SQLException e) {
			LogUtil.error("Fail to savefujianwork", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
				if (conn != null) {
					DAOTools.releaseConnectionOutS("ZQC", conn);
				}
		}
		return mapping.findForward("success");
	}	 
	private ActionForward peixunSaveWork(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		String isRecord = null;
		String filename = form.getFileName();
		try {
	          
			conn = DAOTools.getConnectionOutS("ZQC");
			ArrayList errMsglist = new ArrayList();
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Empty") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Number") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.NoValidValid") );
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
	            if(!WorkPublic.validGradeData( request, conn, form.getValuesMap(), errMsglist ) )
	            {            	           
	            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
	     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
	                return mapping.findForward("failure");
	            }
	            if(isRecord.equals("mp3")){
	            	if(form.getGradeYipiaofoujue() == null ){
	            		if(FirstCheckDAO.savePeixunGradeScoreNew( conn, form,isRecord)){
	            			FirstCheckDAO.updateAgentTeachQuery(conn, form);
	            			 //FirstCheckDAO.updateGradeScoreNew( conn, form);
					         //FirstCheckDAO.updatePfxTotalNew( conn, form);
	            		}
	            	}
	            	}else{
		            	if(form.getGradeYipiaofoujue() == null ){
		            		if(FirstCheckDAO.savePeixunGradeScoreNew( conn, form,isRecord)){
		            			FirstCheckDAO.updateAgentTeachQuery(conn, form);
		            			 //FirstCheckDAO.updateGradeScoreNew( conn, form);
						         //FirstCheckDAO.updatePfxTotalNew( conn, form);
		            		}
		            	}
	            	/*else{
						if(FirstCheckDAO.saveGradeScoreOld( conn, form,isRecord)){
					        FirstCheckDAO.updateGradeScoreNew( conn, form);
					        FirstCheckDAO.updatePfxTotalOld( conn, form);
					        //	FirstCheckDAO.updateAgentSaAgentArgumentText( conn, form);
						}
					}
	            }else{
	            	if(form.getGradeImportant().equals("非常重要")){
						if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
			           	   FirstCheckDAO.updateGradeScoreNew( conn, form);
			           	   FirstCheckDAO.updatePfxTotalNew( conn, form);
			           }
					}else if(form.getGradeImportant().equals("重要")){
						if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
				           FirstCheckDAO.updateGradeScoreNew( conn, form);
				           FirstCheckDAO.updatePfxTotalNew( conn, form);
						}
					}else{
						if(FirstCheckDAO.savePeixunGradeScoreNew( conn, form,isRecord)){
					       FirstCheckDAO.updateGradeScoreNew( conn, form);
					       FirstCheckDAO.updatePfxTotalNew( conn, form);
							}
					}	*/
				}
            request.setAttribute("filename", form.getFileName());
            request.setAttribute("filelocation", form.getFileLocation());
            request.setAttribute("gradeRemark", form.getGradeRemark());
           request.setAttribute("gradeIndex",form.getUseGradeIndex());
	       } catch (SQLException e) {
			LogUtil.error("Fail to savework", SKIP_Logger);
            setErrorMSG(request, "errors.DBError");	
			return mapping.findForward("failure");
		} finally {
				if (conn != null) {
					DAOTools.releaseConnectionOutS("ZQC", conn);
				}
		}
		return mapping.findForward("success");
	}
}
