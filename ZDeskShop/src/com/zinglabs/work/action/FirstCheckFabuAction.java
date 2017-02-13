package com.zinglabs.work.action;

import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.BaseAction;
import com.zinglabs.base.BaseException;
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

public final class FirstCheckFabuAction extends BaseAction {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws BaseException,Exception {
		
		ActionForward myforward = null;
		String myaction = mapping.getParameter();

		if ("WORK_INDEX".equalsIgnoreCase(myaction)) {
			myforward = mapping.findForward("index");
		} else if ("WORK_VIEW_NEW".equalsIgnoreCase(myaction)) {
			myforward = viewWorkNew(mapping, form, request, response);
		} else if ("WORK_SAVE_NEW".equalsIgnoreCase(myaction)) {
			myforward = saveWorkNew(mapping, form, request, response);
		} else if ("WORK_CACULATE_NEW".equalsIgnoreCase(myaction)) {
			myforward = caculateTotalNew(mapping, form, request, response);			
		} else if ("LISTEN_TEACH_RECORD".equalsIgnoreCase(myaction)) {
			myforward = listenTeachRecord(mapping, form, request, response);			
		} else if ("LIST_AGENT_RECORD".equalsIgnoreCase(myaction)) {
			myforward = listAgentRecord(mapping, form, request, response);			
		} else if ("AGENT_WORK_VIEW".equalsIgnoreCase(myaction)) {
			myforward = viewAgentWorkRecord(mapping, form, request, response);			
		} else if ("ARGU_SAVE".equalsIgnoreCase(myaction)) {
			myforward = saveArgu(mapping, form, request, response);			
		} else if ("ADMIN_AGENT_WORK".equalsIgnoreCase(myaction)) {
			myforward = viewAdminAgentWorkRecord(mapping, form, request, response);			
		} else if ("ADMIN_AGENT_WORK_SAVE".equalsIgnoreCase(myaction)) {
			myforward = saveAdminAgentWorkRecord(mapping, form, request, response);			
		} else if ("WORK_VIEW_FJ".equalsIgnoreCase(myaction)) {
			myforward = viewWorkFJ(mapping, form, request, response);			
		} else if ("WORK_SAVE_FJ".equalsIgnoreCase(myaction)) {
			myforward = saveWorkFJ(mapping, form, request, response);			
		} else if ("WORK_CACULATE_FJ".equalsIgnoreCase(myaction)) {
			myforward = caculateTotalFJ(mapping, form, request, response);			
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
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		
		
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			GradeData tdata = FirstCheckDAO.queryFirstTaskInfoNew( conn, gradeIndex );
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
//			System.out.println("isMp3=========="+isRecord);
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
				gradeRemark = WorkPublicDAO.getGradeRemarkNew(conn,  gradeId);
			}
			form.setGradeRemark(gradeRemark);
			
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
			DAOTools.releaseConnectionOutS("ZQC", conn);
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
//			System.out.println("this is the txt");
			return mapping.findForward("successtxt");
		}
		else{
//			System.out.println("this is the mp3");
			return mapping.findForward("success");
		}
	}	
	
	
	private ActionForward saveWorkNew(ActionMapping mapping,
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
		
            if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
            	 FirstCheckDAO.updateGradeScoreNew( conn, form);
            	 FirstCheckDAO.updatePfxTotalNew( conn, form,0);
            }
            request.setAttribute("filename", form.getFileName());
            request.setAttribute("filelocation", form.getFileLocation());
            
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
	            total = WorkPublic.getCountTotalNew( conn, tdata.useGradeIndex, form.getValuesMap(), 0 );
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
	            total = WorkPublic.getCountTotalNew( conn, tdata.useGradeIndex, form.getValuesMap(), 0 );
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
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}else{
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}
				 
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getTeachGradeRemarkNew(conn, gradeId);
			}
			form.setGradeRemark(gradeRemark);
			
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
			DAOTools.releaseConnectionOutS("ZQC", conn);
		}
		//TODO
		return mapping.findForward("success");
	}	
	
	private ActionForward listAgentRecord(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException,Exception {
		Connection conn = null;
		String gradeId = request.getParameter("gradeId");
		String isRecord = null;
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		
		/*if(gradeId.substring(gradeId.length()-1, gradeId.length()).equals(".")){
			gradeId=gradeId.substring(0, gradeId.length()-1);
		}*/
		
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
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}else{
					 ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
					 sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				 }
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getTeachGradeRemarkNew(conn, gradeId);
			}
			form.setGradeRemark(gradeRemark);
			
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
			DAOTools.releaseConnectionOutS("ZQC", conn);
		}
		if(isRecord==null||isRecord.length()==0||isRecord.equals("txt")){
			System.out.println("this is the txt");
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
        
		String gradeId = request.getParameter("gradeId");
	/*	if(gradeId.substring(gradeId.length()-1, gradeId.length()).equals(".")){
			gradeId=gradeId.substring(0, gradeId.length()-1);
		}*/
	
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
					sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}else{
					ArrayList scoreList = WorkPublicDAO.queryGradeScoreNew(conn, filename.substring(0,filename.length()-4));
					sTable = helloWorld.MyTestFunc(tdata.getUseGradeIndex(),scoreList);
				}
				 
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getTeachGradeRemarkNew(conn, gradeId);
			}
			String agentArguRemark=WorkPublicDAO.getAgentArguRemark(conn, gradeId);
			form.setGradeRemark(gradeRemark);
			form.setAgentArguRemark(agentArguRemark);
			
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
			DAOTools.releaseConnectionOutS("ZQC", conn);
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
	private ActionForward saveArgu(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException {
		
		FirstCheckForm form = (FirstCheckForm)actionForm;        
		Connection conn = null;
		String isRecord = null;
		String gradeId = form.getGradeId();
		
		String agentArguement = form.getAgentArguRemark();
		String filename = form.getFileName();
		String gradeRemarkFJ = null;
		try {
	          
			conn = DAOTools.getConnectionOutS("ZQC");        
			String gradeRemark = FirstCheckDAO.queryRecordGradeRemark(conn,gradeId);
			isRecord = FirstCheckDAO.queryIsMp3( conn, filename );
			FirstCheckForm data = FirstCheckDAO.getAgentArguementWork( conn,   gradeId ,isRecord);          
            FirstCheckDAO.saveArgu( conn, data,agentArguement,gradeRemark,gradeRemarkFJ,isRecord);
        	FirstCheckDAO.updateAgentArgumentScoreState(conn,gradeId,isRecord);
          
		} catch (SQLException e) {
			LogUtil.error("Fail to save arguement", SKIP_Logger);
			return mapping.findForward("failure");
		} finally {
			DAOTools.releaseConnectionOutS("ZQC", conn);
		}
		return mapping.findForward("success");
	}	 
	private ActionForward viewAdminAgentWorkRecord(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws BaseException {
        
		String gradeId = request.getParameter("gradeId");
		String isRecord = null;
		
		FirstCheckForm form = (FirstCheckForm)actionForm; 
		Connection conn = null;
		try {
			conn = DAOTools.getConnectionOutS("ZQC");
			
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
				 sTable = helloWorld.AdminAgentMyTestFunc(tdata.getUseGradeIndex(),scoreList);
			}else{
				 ArrayList scoreList = WorkPublicDAO.queryAdminAgentInfo(conn, filename.substring(0,filename.length()-4));
				 sTable = helloWorld.AdminAgentMyTestFunc(tdata.getUseGradeIndex(),scoreList);
			}
				
			}
			
			
			String gradeRemark = (String)request.getAttribute( "errorValueRemark" );
			if( gradeRemark == null )
			{
				gradeRemark = WorkPublicDAO.getTeachGradeRemarkNew(conn, gradeId);
			}
			String agentArguRemark=WorkPublicDAO.getAgentArguRemark(conn,  gradeId);
			String fjRemark = WorkPublicDAO.getFuJianRemark(conn,  gradeId);
			String adminAgentArguRemark=WorkPublicDAO.getAdminAgentGradeRemarkNew(conn,  gradeId);
			form.setGradeRemark(gradeRemark);
			form.setAgentArguRemark(agentArguRemark);
			form.setAdminAgentArguRemark(adminAgentArguRemark);
			form.setFujianRemark(fjRemark);
			
            // get grade table
           
            request.setAttribute("sTable", sTable);
            request.setAttribute("filename", filename);
            request.setAttribute("filelocation", filelocation);
            request.setAttribute("gradeId", gradeId);
            
            form.setGradeId(gradeId);
            int i =0;
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
			DAOTools.releaseConnectionOutS("ZQC", conn);
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
//		String adminArgu=form.getAdminAgentArguRemark();
		try {
	        String fabu = form.getWorkState();
			conn = DAOTools.getConnectionOutS("ZQC");
			ArrayList errMsglist = new ArrayList();
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Empty") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.Number") );
			errMsglist.add( getMSGText("errors.Score.GradeRecord.NoValidValid") );

	       /*     if(!WorkPublic.validGradeData( request, conn, form.getValuesMap(), errMsglist ) )
	            {            	           
	            	WorkPublic.recordErrorValues( request, form.getValuesMap() );
	     		    request.setAttribute("errorValueRemark", form.getGradeRemark());
	                return mapping.findForward("failure");
	            }*/
	        /*boolean updateScoreState = false;
	        updateScoreState    = FirstCheckDAO.updateGradeDetailNew(conn, form);*/
	       // if(updateScoreState==true){
	        	FirstCheckDAO.updateScoreStateWorkState(conn,form,fabu);
	        	FirstCheckDAO.updateWorkState(conn, form, fabu);
	        //} 
	        /*double total = 0;
	        total = WorkPublic.getCountTotalNew( conn, form.getUseGradeIndex(), form.getValuesMap(), 0 );
	        DecimalFormat df = new DecimalFormat("#.00"); 
	        String strTotal = df.format(total);
			setErrorMSG2( request, "errors.Msg.ScoreTotal", " : "+strTotal );
			form.setPfxTotal(strTotal);
			
	        FirstCheckDAO.updateAgentArgumentNew(conn, form);
	        FirstCheckDAO.updatePfxTotal(conn,form);
	        */
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
			form.setGradeRemark(gradeRemark);
			
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
			DAOTools.releaseConnectionOutS("ZQC", conn);
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
		
            if(FirstCheckDAO.saveGradeScoreNew( conn, form,isRecord)){
            	 FirstCheckDAO.updateFJGradeScoreNew( conn, form);
            	 FirstCheckDAO.updatePfxTotalNew( conn, form,0);
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
}
