package com.zinglabs.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.zinglabs.db.UserInfo;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.FileUtils;


public class LuceneSearchServlet extends HttpServlet {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM"); 
	private static final long serialVersionUID = 2628329819758883023L;
	private String charSet="";
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doSearch(req,resp);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doSearch(request,response);
	}
	
	public void init(ServletConfig config) throws ServletException {
		charSet=config.getInitParameter("encoding");
		if(charSet==null){
			charSet="UTF-8";
		}
		super.init(config);
	}

	public void doSearch(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		response.setCharacterEncoding(charSet);
		request.setCharacterEncoding(charSet);
		
		String returnType=request.getParameter("returnType")==null?"":request.getParameter("returnType");
		if("".equals(returnType)){
			returnType=request.getAttribute("returnType")==null?"JSON":request.getAttribute("returnType").toString();
		}
		
		String bt=request.getParameter("beginNum")==null?"":request.getParameter("beginNum");
		int b=0;
		if("".equals(bt)){
			bt=request.getAttribute("beginNum")==null?"0":request.getAttribute("beginNum").toString();
		}
		b=Integer.parseInt(bt);
		
		String num=request.getParameter("getNum")==null?"":request.getParameter("getNum");
		int n=0;
		if("".equals(num)){
			num=request.getAttribute("getNum")==null?"0":request.getAttribute("getNum").toString();
		}
		n=Integer.parseInt(num);
		
		String condition=request.getParameter("condition")==null?"":request.getParameter("condition");
		if("".equals(condition)){
			condition=request.getAttribute("condition")==null?"":request.getAttribute("condition").toString();
		}
		condition=condition.trim();
		//查询条件星号处理
		if(condition.length()>0 && condition.indexOf("*")>=0){
			condition=condition.replaceAll("\\*", " ");
			condition=condition.trim();
			condition=condition.replaceAll(" ", " and ");
		}
		
		String searchType=request.getParameter("searchType")==null?"":request.getParameter("searchType");
		if("".equals(searchType)){
			searchType=request.getAttribute("searchType")==null?"PRECISE":request.getAttribute("searchType").toString();
		}
		
		String action=request.getParameter("action")==null?"":request.getParameter("action");
		if("".equals(action)){
			action=request.getAttribute("action")==null?"":request.getAttribute("action").toString();
		}
		
		try{
			//condition=conditionFix(condition);
			if("getSearch".equals(action)){
				LuceneSearchHandle.getSearch(condition,searchType,returnType,b,n,request,response);
			}else if("quickSearch".equals(action)){
				LuceneSearchHandle.quickSearch(request,response);
			}else if("groupSearch".equals(action)){
				LuceneSearchHandle.groupSearch(request,response);
			}else if("getContent".equals(action)){
				String docId=request.getParameter("docId")==null?"":request.getParameter("docId");
				if(docId.length()<=0){
					docId=request.getAttribute("docId")==null?"":request.getAttribute("docId").toString();
				}
				LuceneSearchHandle.getDocContent(docId,response);
			}
		}catch(Exception x){
			LogUtil.error(x,SKIP_Logger);
		}
	}
	
	
	public static String conditionFix(String condition){
		if(condition!=null && condition.length()>0){
			//第一个字符不可以为*
			if(condition.indexOf("*")==0 && condition.length()>1){
				condition=condition.substring(1,condition.length());
			}
		}
		return condition;
	}
}
