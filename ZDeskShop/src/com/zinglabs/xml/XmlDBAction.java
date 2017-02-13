package com.zinglabs.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zinglabs.db.*;
import net.sf.json.JSONSerializer;
import atg.taglib.json.util.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



//import atg.taglib.json.util.JSONObject;

import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.log.LogUtil;
import com.zinglabs.mq.MQSend;
import com.zinglabs.tools.DOMTool;
import com.zinglabs.tools.ECBTools;
import com.zinglabs.tools.ExcelTools;
import com.zinglabs.tools.GenConfAll;
import com.zinglabs.tools.MailUtil2;
import com.zinglabs.tools.RandomGUID;
import com.zinglabs.tools.TXInfo2;
import com.zinglabs.tools.Utility;
import com.zinglabs.tools.ZQCUtility;

public class XmlDBAction extends BaseAbs  {
	public void init(){
		init(SKIP_Logger);
		hasInit=true;
	}
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		if(!hasInit)
			init();
		response.setContentType("text/xml");
//        response.setHeader("Cache-Control", "no-cache");
        response.setHeader( "Pragma", "no-cache" );
        response.addHeader( "Cache-Control", "must-revalidate" );
        response.addHeader( "Cache-Control", "no-cache" );
        response.addHeader( "Cache-Control", "no-store" );
        response.setDateHeader("Expires", 0);
        response.setCharacterEncoding("gbk");
//        try {
//			request.setCharacterEncoding("utf-8");
//		} catch (UnsupportedEncodingException e1) {
//			error(e1);
//		}
        String action=request.getParameter("action");
        String callback=request.getParameter("callback");
        String retStr=null;
       // LogUtil.warn(action, XmlGenAction.class);
		info("XmlDBAction action:"+action);
		try {
			if (action != null && action.equals("userLoginCheck")) {
				request.getSession().setMaxInactiveInterval(3 * 60 * 60);
				String userName = request.getParameter("userName");
				String password = request.getParameter("password");
				String ip=request.getRemoteAddr();
				String clientId=Utility.genClientId();
				String restr="";
				if (userName != null && password!=null && !"".equals(userName) && !"".equals(password)) {
					restr=SSCDBTools.getLoginCheck(userName, password, ip, clientId);
//					if(restr.indexOf("true")>-1){
//						request.getSession().setAttribute("userName", userName);
//					}
				} else {
					restr="{success:'false','mgs':'用户名或密码不能为空。'}";
				}
				debug(restr);
				PrintWriter out = response.getWriter();
				out.write(JSONSerializer.toJSON(restr).toString());
				if (out.checkError()) {
					warn("---------exception in userRole-----------");
				}
				out.flush();
				out.close();
				
			}else if(action != null && action.equals("logout")){
				String uname=request.getParameter("userName");
				debug("logout userName:"+uname);
//				String uname=request.getSession().getAttribute("userName")==null?"":request.getSession().getAttribute("userName").toString();
				UserInfo2 uTmp=UserInfo2.getUser(uname);
				if(uTmp!=null){
					
					try {
//						if(UserInfo.allUserInfo.get(uname)!=null){
//						UserInfo uTmp=UserInfo.allUserInfo.get(uname);
						String ttUserName=uTmp.userGetFieldValue("userName");
						String ttUserIP=uTmp.userGetFieldValue("userIP");
						
						ttUserName=ttUserName!=null?ttUserName:uname;
						
						if(ttUserName!=null && ttUserName.length()>0){
							String sqlLog="insert into Z_DBModifyLog (optName,optTable,optTableName,optUser,ip)VALUES('登出','suSecurityUser','登入登出记录','"+ttUserName+"','"+ttUserIP+"')";
							ArrayList<String> batch=new ArrayList<String>();
							batch.add(sqlLog);
							DAOTools.execBatchS(batch, "ZDesk",false);
						}
						
//						String clientId=uTmp.userGetFieldValue("clientId");
//						if(UserInfo.allClientIdUserInfo.containsKey(clientId)){
//							UserInfo.allClientIdUserInfo.remove(clientId);
//						}
						
//						UserInfo.allUserInfo.remove(uname);
					} catch (Exception e) {
						error(e);
					} finally {
						uTmp.clearAll();
					}
				}
				PrintWriter out = response.getWriter();
				out.write("true");
				out.flush();
				out.close();
			}else if (action != null && action.equals("pageValidate")) {
				String userName = "LoginFail";
				if (request.getSession().getAttribute("userName") != null)
					userName = request.getSession().getAttribute("userName").toString();
//				warn("_________" + userName);
				PrintWriter out = response.getWriter();
				out.write(userName);
				if (out.checkError()) {
					warn("---------exception in pageValidate-----------");
				}
				out.flush();
				out.close();

			} else if (action != null && action.equals("LoadWinData")) {
				String userName =request.getParameter("userName");
//				if(request.getSession().getAttribute("userName")!=null){
//					userName =request.getSession().getAttribute("userName").toString();
//				}
				
				
				UserInfo2 user=UserInfo2.getUser(userName);
//				if(userName!=null && UserInfo.allUserInfo.containsKey(userName)){
//					user=UserInfo.allUserInfo.get(userName);
//				}
				try {
					String content = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+ request.getParameter("content");
//					String contentUTF = new String(content.getBytes("ISO-8859-1"),"UTF-8");
					String contentUTF = new String(content.getBytes("gbk"));
					contentUTF = contentUTF.replaceAll("<\\?xml version=\"1.0\"\\?>", "");
					if (contentUTF.length() > 0) {
						if(isDebug){
							debug("Client 1 xml:"+contentUTF);
						}
						PrintWriter out = response.getWriter();
						Document doc = DOMTool.loadDocumentFromStr(contentUTF,"gbk");
						String outTmp = ConfXmlObj.loadXmlData(doc, request.getSession().getServletContext().getRealPath("/"),user,out);
//						outTmp=Utility.encodeStr(outTmp);
//						out.write(outTmp);
						
						if(isDebug){
							debug(outTmp);
						}
						// String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
						if (out.checkError()) {
							error("ERROR: client LoadWinData  xml ");
							error(outTmp);
						}
						out.flush();
						out.close();
					}
				} catch (Exception e) {
					error(" LoadWinData ");
					error(e);
				} finally {
					if(user!=null){
						user.releaseAll();
					}
				}
				
				debug("LoadWinData end");
			}
            else if (action != null && action.equals("EasyQueryXmlGBK"))
            {
                // long easyBegin=System.currentTimeMillis();
                String sql = request.getParameter("sql");

                LogUtil.debug("EasyQueryXmlGBK "+sql, SKIP_Logger);
                sql=Utility.decodeUTFN(sql);
                
                String contentGB2 = new String(sql.getBytes("gbk"));
                String contentGB3 = new String(sql.getBytes("UTF-8"));
                String contentGB4 = new String(sql.getBytes("gbk"), "UTF-8");
                String contentGB5 = new String(sql.getBytes("UTF-8"), "gbk");
                String contentGBx = new String(sql.getBytes("ISO-8859-1"),"gbk");
                String contentGBx1 = new String(sql.getBytes("ISO-8859-1"),"utf-8");
                // String contentGB4 = new
                // String(msgCon.getBytes("ISO-8859-1"));
                LogUtil.debug(sql+" contentGBx:" + contentGBx + "contentGBx1:"
                        + contentGBx1 + " contentGB2:" + contentGB2
                        + " contentGB3:" + contentGB3 + " contentGB4:"
                        + contentGB4 + " contentGB5:" + contentGB5, SKIP_Logger);

                String encodeOrder = request.getParameter("encodeOrder");
                if (encodeOrder != null && encodeOrder.equals("true"))
                {
                    debug("encodeStrOrder sql:" + sql);
                    sql = Utility.encodeStrOrder(sql);
                }
                
                String dbId=request.getParameter("dbId");
                String tableName=request.getParameter("tableName");
                debug("EasyQueryXml sql:"+sql+" dbId:"+dbId+" tableName:"+tableName);
                String ret=null;
                if(sql!=null && dbId!=null && tableName!=null){
                    ret=DAOTools.easyQueryXml(sql,dbId,tableName,true,null);
                }
                if(ret!=null && ret.length()>0){
                    
                    ret=Utility.encodeStr(ret);
//                  response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write(String.valueOf(ret));
                    
//                  debug("ret:"+ret);
                    // String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
                    if (out.checkError()) {
                        error("Exception: EasyQueryXml ");
                        error("ret:"+ret);
                    }
                    out.flush();
                    out.close();    
                }
                //debug(ret+" EasyQueryXml end"+(System.currentTimeMillis()-easyBegin));
			}else if(action != null && action.equals("EasyQueryXml")){
                //long easyBegin=System.currentTimeMillis();
                String sql=request.getParameter("sql");
                String ret=null;
                if(sql.indexOf("from ZBotQCount")!=-1){
 
                }else{
                    String encodeOrder=request.getParameter("encodeOrder");
                    if(encodeOrder!=null && encodeOrder.equals("true")){
                        debug("encodeStrOrder sql:"+sql);
                        sql=Utility.encodeStrOrder(sql);
                    }
 
                    String dbId=request.getParameter("dbId");
                    String tableName=request.getParameter("tableName");
                    debug("EasyQueryXml sql:"+sql+" dbId:"+dbId+" tableName:"+tableName);
//                    String ret=null;
                    if(sql!=null && dbId!=null && tableName!=null){
                        ret=DAOTools.easyQueryXml(sql,dbId,tableName,true,null);
                    }
                }
 
                if(ret!=null && ret.length()>0){
 
                    ret=Utility.encodeStr(ret);
//      response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write(String.valueOf(ret));
 
//      debug("ret:"+ret);
                    // String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
                    if (out.checkError()) {
                        error("Exception: EasyQueryXml ");
                        error("ret:"+ret);
                    }
                    out.flush();
                    out.close();
                }
                //debug(ret+" EasyQueryXml end"+(System.currentTimeMillis()-easyBegin));
            }else if(action != null && action.equals("EasyQueryJson")){
			    long easyBegin=System.currentTimeMillis();
                String sql=request.getParameter("sql");
                String encodeOrder=request.getParameter("encodeOrder");
                if(encodeOrder!=null && encodeOrder.equals("true")){
                    debug("encodeStrOrder sql:"+sql);
                    sql=Utility.encodeStrOrder(sql);
                }
                
                String isStream=request.getParameter("isStream");
                
                String dbId=request.getParameter("dbId");
                String tableName=request.getParameter("tableName");
                debug("EasyQueryJson sql:"+sql+" dbId:"+dbId+" tableName:"+tableName);
                String ret=null;
                if(sql!=null && dbId!=null && tableName!=null){
                    if(isStream!=null && isStream.equals("true")) {
                        PrintWriter out = response.getWriter();
                        DAOTools.queryJsonStr(sql,dbId,tableName,out);
                        out.flush();
                        out.close();  
                    }else {
                        ret=DAOTools.queryJsonStr(sql,dbId,tableName,null);
                    }
                }
                if(ret!=null && ret.length()>0){
                    
//                    ret=Utility.encodeStr(ret);
//                  response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write(String.valueOf(ret));
                    
//                  debug("ret:"+ret);
                    // String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
                    if (out.checkError()) {
                        error("Exception: EasyQueryJson ");
                        error("ret:"+ret);
                    }
                    out.flush();
                    out.close();    
                }
                debug(ret+" EasyQueryJson end"+(System.currentTimeMillis()-easyBegin)); 
			}else if(action != null && action.equals("EasyQueryJson2")) {
			    long easyBegin=System.currentTimeMillis();
                String sql=request.getParameter("sql");
                String encodeOrder=request.getParameter("encodeOrder");
                if(encodeOrder!=null && encodeOrder.equals("true")){
                    debug("encodeStrOrder sql:"+sql);
                    sql=Utility.encodeStrOrder(sql);
                }
                
                String dbId=request.getParameter("dbId");
                String tableName=request.getParameter("tableName");
                debug("EasyQueryJson sql:"+sql+" dbId:"+dbId+" tableName:"+tableName);
                String ret=null;
                if(sql!=null && dbId!=null && tableName!=null){
                    ret=DAOTools.queryJsonStr2(sql,dbId,tableName);
                }
                if(ret!=null && ret.length()>0){
                    
//                    ret=Utility.encodeStr(ret);
//                  response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write(String.valueOf(ret));
                    
//                  debug("ret:"+ret);
                    // String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
                    if (out.checkError()) {
                        error("Exception: EasyQueryJson ");
                        error("ret:"+ret);
                    }
                    out.flush();
                    out.close();    
                }
                debug(ret+" EasyQueryJson2 end"+(System.currentTimeMillis()-easyBegin)); 
			} if(action != null && action.equals("EasyUpdate")) {
			    long easyBegin=System.currentTimeMillis();
                String sql=request.getParameter("sql");
                String encodeOrder=request.getParameter("encodeOrder");
                if(encodeOrder!=null && encodeOrder.equals("true")){
                    debug("encodeStrOrder sql:"+sql);
                    sql=Utility.encodeStrOrder(sql);
                }
                
                String dbId=request.getParameter("dbId");
                String tableName=request.getParameter("tableName");
                debug("EasyUpdate sql:"+sql+" dbId:"+dbId+" tableName:"+tableName);
                int retInt=0;
                if(sql!=null && dbId!=null && tableName!=null){
                    retInt=DAOTools.execUpdateS(sql,dbId);
                }
                debug(retInt+" EasyUpdate end"+(System.currentTimeMillis()-easyBegin));
			} /*if(action != null && action.equals("EasyUpdataScoreState")){
				String sql=request.getParameter("sql");
				String dbId=request.getParameter("dbId");
				String tableName=request.getParameter("tableName");
				debug("EasyQueryXml sql:"+sql+" dbId:"+dbId+" tableName:"+tableName);
				String ret=null;
				if(sql!=null && dbId!=null && tableName!=null){
					ret=DAOTools.easyUpdataScoreState(sql,dbId,tableName,true,null);
				}
				if(ret!=null && ret.length()>0){
					PrintWriter out = response.getWriter();
					out.write(String.valueOf(ret));
					
					debug("ret:"+ret);
					// String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
					if (out.checkError()) {
						error("Exception: EasyQueryXml ");
						error("ret:"+ret);
					}
					out.flush();
					out.close();	
				}
			}else if(action != null && action.equals("UpdataScoreStateAndSaAgentArgument")){
				String sql=request.getParameter("sql");
				String dbId=request.getParameter("dbId");
				String tableName=request.getParameter("tableName");
				String dataId = request.getParameter("dataId");
				debug("EasyQueryXml sql:"+sql+" dbId:"+dbId+" tableName:"+tableName);
				String ret=null;
				if(sql!=null && dbId!=null && tableName!=null){
					ret=DAOTools.easyUpdataScoreState(sql,dbId,tableName,true,null);
					DAOTools.easyUpdataSaAgentArgument(dataId);
				}
				if(ret!=null && ret.length()>0){
					PrintWriter out = response.getWriter();
					out.write(String.valueOf(ret));
					
					debug("ret:"+ret);
					// String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
					if (out.checkError()) {
						error("Exception: EasyQueryXml ");
						error("ret:"+ret);
					}
					out.flush();
					out.close();	
				}
			}*/else if(action != null && action.equals("EasyQueryXml2")){
				String sql=request.getParameter("sql");
				String encodeOrder=request.getParameter("encodeOrder");
				String codeType=request.getParameter("codeType");
				
				
				if(encodeOrder!=null && encodeOrder.equals("true")){
					sql=Utility.encodeStrOrder(sql);
				}
				String dbId=request.getParameter("dbId");
				String tableName=request.getParameter("tableName");
				debug("EasyQueryXml2 sql:"+sql+" dbId:"+dbId+" tableName:"+tableName);
				String ret=null;
				if(sql!=null && dbId!=null && tableName!=null){
					ret=DAOTools.easyQueryXml2(sql,dbId,tableName,true,null,codeType);
				}
				if(ret!=null && ret.length()>0){
					ret=Utility.encodeStr(ret);
//					response.setCharacterEncoding("UTF-8");
					PrintWriter out = response.getWriter();
					out.write(String.valueOf(ret));
					
					//debug("ret:"+ret);
					// String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
					if (out.checkError()) {
						error("Exception: EasyQueryXml ");
						error("ret:"+ret);
					}
					out.flush();
					out.close();	
				}
			}else if(action != null && action.equals("EasyToDBXml")){
//				String sql=request.getParameter("sql");
				String dbId=request.getParameter("dbId");
				String content =  request.getParameter("content");
//				content=Utility.decodeStr(content);
				content = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+ content;
//				String contentUTF = new String(content.getBytes("ISO-8859-1"), "UTF-8");
				String contentUTF = new String(content.getBytes("gbk"));
				debug("EasyToDBXml begin dbId:"+dbId+" client xml "+contentUTF);
//				if(isDebug){
//					debug("Client xml:"+contentUTF);
////					debug(new String(content.getBytes("ISO-8859-1"), "gb2312"));
////					debug(new String(content.getBytes("gb2312")));
//				}
				contentUTF = contentUTF.replaceAll("<\\?xml version=\"1.0\"\\?>", "");
				if(contentUTF.length()>0){
					Document doc = DOMTool.loadDocumentFromStr(contentUTF,"GBK");
					String ret=null;
					if(content!=null && dbId!=null){
						ret=ConfXmlObj.easyToDBXml(dbId, doc,true);
					}
					if(ret!=null && ret.length()>0){
						PrintWriter out = response.getWriter();
						out.write(String.valueOf(ret));
						
						debug("ret:"+ret);
						// String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
						if (out.checkError()) {
							error("Exception: EasyToDBXml out");
							error("ret:"+ret);
						}
						out.flush();
						out.close();	
					}
				}
			} else if(action != null && action.equals("SaveOrderMQ")){
//				sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
//				sun.misc.BASE64Encoder encoder=new BASE64Encoder();
				String contentUTF = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+ request.getParameter("content");
				String contentUTF1 = new String(contentUTF.getBytes("gbk"),"UTF-8");
				String contentUTF2 = new String(contentUTF.getBytes("gbk"));
				
//				debug();
//				contentUTF = new String(contentUTF.getBytes("ISO-8859-1"), "UTF-8");
//				debug("SaveOrderMQ xml:"+contentUTF);
//				contentUTF=encoder.encode(contentUTF.getBytes());
//				debug("SaveOrderMQ xml:"+new String(decoder.decodeBuffer(contentUTF)));
				
				
				if(contentUTF2.indexOf("attachement=\"")!=-1) {
				    String fileName=contentUTF2.substring(contentUTF2.indexOf("attachement=\"")+13);
	                fileName=fileName.substring(0,fileName.indexOf("\""));
	                LogUtil.debug("fileName 1 :"+fileName, SKIP_Logger);
	                if(fileName!=null && fileName.trim().length()>0) {
	                    fileName=fileName.replace("@*", "/");
	                    LogUtil.debug("fileName 2 :"+fileName, SKIP_Logger);
	                    File file=new File(fileName);
	                    if(file.exists()) {
	                        String fileNameReal=fileName;
	                        if(fileName.indexOf("/")!=-1) {
	                            fileNameReal = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
	                        }
	                        LogUtil.debug("fileNameReal 1 :"+fileName, SKIP_Logger);
	                        
	                        String fileCode=Utility.fileToEncodeStr2(fileName);
	                        if(fileCode!=null) {
	                            contentUTF2=contentUTF2.replace("</sr>", "<attachement name=\""+fileNameReal+"\" value=\""+fileCode+"\" /></sr>");
	                        }else {
	                            contentUTF2=contentUTF2.replace("</sr>", "<attachement name=\"\" value=\"\" /></sr>");   
	                        }
	                    }else {
	                        contentUTF2=contentUTF2.replace("</sr>", "<attachement name=\"\" value=\"\" /></sr>");
	                    }
	                }
	                
	                contentUTF2=contentUTF2.substring(0,contentUTF2.indexOf("</sr>")+5);
	                
				}else {
				    contentUTF2=contentUTF2.replace("</sr>", "<attachement name=\"\" value=\"\" /></sr>"); 
				}
				
//				contentUTF2=contentUTF2.replaceAll("<attachement name=\"\" value=\"\" />", "<attachement name=\"\" value=\"\" />");
				
				
				MQSend mqsend = new MQSend();
				mqsend.send2(contentUTF2);
				
				if(contentUTF2.indexOf("bboocc")!=-1) {
				    debug("SaveOrderMQ xml:"+contentUTF2);
				}
				
//              debug("SaveOrderMQ xml:"+contentUTF1);
				
			}else if (action != null && action.equals("Client")) {
				String contentUTF = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+ request.getParameter("content");
//				String contentUTF = new String(content.getBytes("ISO-8859-1"), "UTF-8");
//				String contentUTF = new String(content.getBytes("gbk"));
//				String contentUTF = Utility.decodeStr(content);
//				new String(content.getBytes("gbk"));
				int ret=-1;
				if(isDebug){
					debug("Client xml:"+contentUTF);
//					debug(new String(content.getBytes("ISO-8859-1"), "gb2312"));
//					debug(new String(content.getBytes("gb2312")));
				}
				
				contentUTF = contentUTF.replaceAll("<\\?xml version=\"1.0\"\\?>", "");
				
				if(contentUTF.length()>0){
					Document doc = DOMTool.loadDocumentFromStr(contentUTF,"GBK");
					ThreadInfoObj threadInfo=new ThreadInfoObj();
					ConfXmlObj.allThreadInfo.put(Thread.currentThread(), threadInfo);
					
					ret=ConfXmlObj.xmlToDB(doc,request.getSession().getServletContext().getRealPath("/"),true);
					
					if(threadInfo.threadErrorList.size()>0){
					// TODO gen and send error xml to customer may be a json window
						threadInfo=ConfXmlObj.allThreadInfo.remove(Thread.currentThread());
						threadInfo.clear();
					}
					
					PrintWriter out = response.getWriter();
					out.write(String.valueOf(ret));
					
					if(isDebug){
						debug("ret:"+ret);
					}
					// String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
					if (out.checkError()) {
						error("ERROR: Client xmlToDB ");
						error("ret:"+ret);
					}
					out.flush();
					out.close();
				}
			} else if(action != null && action.equals("Combo")){
				String comboType =  request.getParameter("comboType");
				String comboParam =  request.getParameter("comboParam");
				if(comboParam!=null){
//					comboParam=new String(comboParam.getBytes("ISO-8859-1"),"gbk");
//					String comboParam2 = new String(comboParam.getBytes("gbk"));
//					String comboParam3 = new String(comboParam.getBytes("UTF-8"));
//					String comboParam4 = new String(comboParam.getBytes("gbk"),"UTF-8");
//					String comboParam5 = new String(comboParam.getBytes("UTF-8"),"gbk");
//					String comboParam6=new String(comboParam.getBytes("ISO-8859-1"),"UTF-8");
//					debug("combo str "+comboParam+" "+comboParam1+" "+comboParam2+" "+comboParam3+" "+comboParam4+" "+comboParam5+" "+comboParam6);
//					comboParam=comboParam6;
				}
				
//				String dbID =  request.getParameter("dbID");
				
//				String outTmp = new String(Utility.loadComboXml(comboType,comboParam,dbID).getBytes("GBK"),"UTF-8");
				String outTmp = ZQCUtility.loadComboXml(comboType,comboParam);
				PrintWriter out = response.getWriter();
				out.write(outTmp);
				
				if(isDebug){
					debug(outTmp);
				}
				// String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
				if (out.checkError()) {
					error(" load Combo  comboType:"+comboType+" comboParam:"+comboParam+" xml ");
					error(outTmp);
				}
				out.flush();
				out.close();
				
			} else if(action!=null && action.equals("splitorStr")) {
			       long easyBegin=System.currentTimeMillis();
			       String sql=request.getParameter("sql");
			                String sqlTmp=sql.toLowerCase();
			                String ret=null;
			                if(sqlTmp.indexOf("select")!=-1 || sql.indexOf("BocBusinessAgent1")!=-1 ){
			 
			                }else{
			                    String encodeOrder=request.getParameter("encodeOrder");
			                    if(encodeOrder!=null && encodeOrder.equals("true")){
			                        debug("encodeStrOrder sql:"+sql);
			                        sql=Utility.encodeStrOrder(sql);
			                    }
			                    String dbId=request.getParameter("dbId");
			                    String splitor=request.getParameter("splitor");
			 
			                    if(sql!=null && dbId!=null){
			                        ret=DAOTools.querySplitorStr(sql,dbId,splitor);
			                    }
			                }
			 
			                if(ret!=null && ret.length()>0){
			                    ret=Utility.encodeStr(ret);
//			                  response.setCharacterEncoding("UTF-8");
			                    PrintWriter out = response.getWriter();
			                    out.write(String.valueOf(ret));
			                    
//			                  debug("ret:"+ret);
			                    // String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
			                    if (out.checkError()) {
			                        error("Exception: splitorStr ");
			                        error("ret:"+ret);
			                    }
			                    out.flush();
			                    out.close();    
			                }
			                debug(ret+" EasyQueryXml end"+(System.currentTimeMillis()-easyBegin));
			                
			   } else if(action != null && action.equals("loadTabMenuXml")){
				String userName =request.getParameter("userName");
//				if(request.getSession().getAttribute("userName")!=null){
//					userName =request.getSession().getAttribute("userName").toString();
//				}
				if(userName!=null){
					UserInfo2 user=UserInfo2.getUser(userName);
					
					try{
						PrintWriter out = null;
						String outTmp =null;
//						String securityDBId=request.getSession().getAttribute("securityDBId").toString();
						String securityDBId=request.getParameter("securityDBId").toString();
						String parentIndex=request.getParameter("parentIndex").toString();
						if(user!=null){
//							user=UserInfo.allUserInfo.get(userName);
							outTmp = ConfXmlObj.loadMenuXml(request.getSession().getServletContext().getRealPath("/"),user,securityDBId,parentIndex);
							out = response.getWriter();
							out.write(outTmp);
							if(isDebug){
								debug(outTmp);
							}
							if (out.checkError()) {
								error(" load Combo  loadMenuXml");
								error(outTmp);
							}
							out.flush();
							out.close();
						}
					} catch (Exception e) {
						error(e);
					} finally{
						if(user!=null){
							user.releaseAll();
						}
						
					}
				}
//				debug("loadMenuXml userName:"+userName);
				
					//user=UserInfo.allUserInfo.get(userName);
					
				// String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
			}else if (action != null && action.equals("loadMenuXml")) {
				String userName =request.getParameter("userName");
//				if(request.getSession().getAttribute("userName")!=null){
//					userName =request.getSession().getAttribute("userName").toString();
//				}
				
				UserInfo2 user=UserInfo2.getUser(userName);
				try{
					PrintWriter out = null;
					String outTmp =null;
//					String securityDBId=request.getSession().getAttribute("securityDBId").toString();
					String securityDBId=request.getParameter("securityDBId").toString();
					String tabCode=request.getParameter("tabCode").toString();
					if(userName!=null){
//						user=UserInfo.allUserInfo.get(userName);
						outTmp = ConfXmlObj.loadMenuXml(request.getSession().getServletContext().getRealPath("/"),user,securityDBId,tabCode);
						out = response.getWriter();
						out.write(outTmp);
						if(isDebug){
							debug(outTmp);
						}
						if (out.checkError()) {
							error(" load Combo  loadMenuXml");
							error(outTmp);
						}
						out.flush();
						out.close();
					}
				} catch (Exception e) {
					error(e);
				} finally{
					if(user!=null){
						user.releaseAll();
					}
//					user.releaseAll();
				}
				
//				debug("loadMenuXml userName:"+userName);
				
					//user=UserInfo.allUserInfo.get(userName);
					
				// String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
			} else if(action != null && action.equals("loadAgentXml")){
				String userName =request.getParameter("userName");
//				if(request.getSession().getAttribute("userName")!=null){
//					userName =request.getSession().getAttribute("userName").toString();
//				}
				
				UserInfo2 user=UserInfo2.getUser(userName);
				PrintWriter out = null;
				String outTmp =null;
//				String securityDBId=request.getSession().getAttribute("securityDBId").toString();
				String securityDBId=request.getParameter("securityDBId").toString();
				debug(user+"loadAgentXml userName:"+userName);
				if(user!=null){
					try {
//						user=UserInfo.allUserInfo.get(userName);
						ArrayList<String> al=new ArrayList<String>();
						al.add(AGENT_TAB_TYPE_SYMBOL);
						al.add(AGENT_TYPE_SYMBOL);
						al.add(MEETING_SYMBOL);
						al.add(INNER_MSG_SYMBOL);
						al.add(CHAT_SYMBOL);
						al.add(ZKM_MODIFY_SYMBOL);
						al.add(ZKM_FLOW_SYMBOL);
						al.add(ORDER_SYMBOL);
						outTmp = ConfXmlObj.loadAgentXml(request.getSession().getServletContext().getRealPath("/"),user,securityDBId,al);
						out = response.getWriter();
						out.write(outTmp);
						if(isDebug){
							debug(outTmp);
						}
						if (out.checkError()) {
							error(" load Combo  loadMenuXml");
							error(outTmp);
						}
						out.flush();
						out.close();	
					}  catch (Exception e) {
						error(e);
					} finally {
						if(user!=null){
							user.releaseAll();
						}
//						user.releaseAll();
					}

				}
			}else if(action != null && action.equals("saveWorkFlow")){
				String content = request.getParameter("content");
				String flowName = request.getParameter("flowName");
//				debug(flowName+" saveWorkFlow "+content);
				if (flowName != null && content != null && flowName.length() > 0 && content.length() > 0) {
//					String contentUTF = new String(content.getBytes("UTF-8"));
//					String fN1=new String(flowName.getBytes("UTF-8"));
//					try {
//						Utility._getSpell(fN1, null);
//					} catch (Exception e) {
//						error(e);
//					}
//					
//					String fN2=new String(flowName.getBytes("gbk"));
//					try {
//						Utility._getSpell(fN1, null);
//					} catch (Exception e) {
//						error(e);
//					}
					
					flowName = Utility._getSpell(flowName, null);
//					String path = request.getSession().getServletContext().getRealPath("/")+ "flowDesinger/flowDetail/" + flowName + ".jsflow";
//					OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(new File(path)), "UTF-8");
//					BufferedWriter writer = new BufferedWriter(write);
//					writer.write(contentUTF);
//					writer.close();
					
//					path = request.getSession().getServletContext().getRealPath("/")+ "flowDesinger/flowDetail/" + flowName + "1.jsflow";
//					write = new OutputStreamWriter(new FileOutputStream(new File(path)));
//					writer = new BufferedWriter(write);
//					writer.write(content);
//					writer.close();
					
					String path = request.getSession().getServletContext().getRealPath("/")+ "flowDesinger/flowDetail/" + flowName + ".jsflow";
					OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(new File(path)), "UTF-8");
					BufferedWriter writer = new BufferedWriter(write);
					writer.write(content);
					writer.flush();
					writer.close();
					
					
//					path = request.getSession().getServletContext().getRealPath("/")+ "flowDesinger/flowDetail/" + flowName + "3.jsflow";
//					write = new OutputStreamWriter(new FileOutputStream(new File(path)));
//					writer = new BufferedWriter(write);
//					writer.write(contentUTF);
//					writer.close();
					
				} else {
					error("saveWorkFlow flowName : " + flowName+ " or content is null");
				}
				
			}else if(action != null && action.equals("assignRecord")){
				String content = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+ request.getParameter("content");
				String userName=request.getParameter("userName")==null?"":request.getParameter("userName");
//				String contentUTF = new String(content.getBytes("ISO-8859-1"), "UTF-8");
				String contentUTF = new String(content.getBytes("gbk"));
				int ret=-1;
				if(isDebug){
					debug("Client xml:"+contentUTF);
					debug(new String(content.getBytes("ISO-8859-1"), "gb2312"));
					debug("gb2312"+new String(content.getBytes("gb2312")));
					debug("gbk"+new String(content.getBytes("gbk")));
					debug(new String(content.getBytes("UTF-8")));
					debug(new String(content.getBytes("UTF-8"),"gbk"));
				}
				
				contentUTF = contentUTF.replaceAll("<\\?xml version=\"1.0\"\\?>", "");
				if(contentUTF.length()>0){
					Document doc = DOMTool.loadDocumentFromStr(contentUTF,"GBK");
					ret=ZQCUtility.assignRecord(doc,request.getSession().getServletContext().getRealPath("/"),userName);
//					PrintWriter out = response.getWriter();
//					out.write(String.valueOf(ret));
//					if(isDebug){
//						debug("ret:"+ret);
//					}
//					// String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
//					if (out.checkError()) {
//						error("ERROR: Client xmlToDB ");
//						error("ret:"+ret);
//					}
//					out.close();
				}
				
			}else if(action != null && action.equals("fjassignRecord")){
				String content = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+ request.getParameter("content");
				String userName=request.getParameter("userName")==null?"":request.getParameter("userName");
//				String contentUTF = new String(content.getBytes("ISO-8859-1"), "UTF-8");
				String contentUTF = new String(content.getBytes("gbk"));
				int ret=-1;
				if(isDebug){
					debug("Client xml:"+contentUTF);
					debug(new String(content.getBytes("ISO-8859-1"), "gb2312"));
					debug("gb2312"+new String(content.getBytes("gb2312")));
					debug("gbk"+new String(content.getBytes("gbk")));
					debug(new String(content.getBytes("UTF-8")));
					debug(new String(content.getBytes("UTF-8"),"gbk"));
				}
				
				contentUTF = contentUTF.replaceAll("<\\?xml version=\"1.0\"\\?>", "");
				if(contentUTF.length()>0){
					Document doc = DOMTool.loadDocumentFromStr(contentUTF,"GBK");
					ret=ZQCUtility.fjassignRecord(doc,request.getSession().getServletContext().getRealPath("/"),userName);
//					PrintWriter out = response.getWriter();
//					out.write(String.valueOf(ret));
//					if(isDebug){
//						debug("ret:"+ret);
//					}
//					// String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
//					if (out.checkError()) {
//						error("ERROR: Client xmlToDB ");
//						error("ret:"+ret);
//					}
//					out.close();
				}
				
			}else if(action != null && action.equals("assignRecordTxt")){
				String content = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+ request.getParameter("content");
//				String contentUTF = new String(content.getBytes("ISO-8859-1"), "UTF-8");
				String userName=request.getParameter("userName")==null?"":request.getParameter("userName");
				String contentUTF = new String(content.getBytes("gbk"));
				String assignRole =  request.getParameter("assignRole");
				int ret=-1;
				if(isDebug){
					debug("Client xml:"+contentUTF);
					debug(new String(content.getBytes("ISO-8859-1"), "gb2312"));
					debug("gb2312"+new String(content.getBytes("gb2312")));
					debug("gbk"+new String(content.getBytes("gbk")));
					debug(new String(content.getBytes("UTF-8")));
					debug(new String(content.getBytes("UTF-8"),"gbk"));
				}
				
				contentUTF = contentUTF.replaceAll("<\\?xml version=\"1.0\"\\?>", "");
				if(contentUTF.length()>0){
					Document doc = DOMTool.loadDocumentFromStr(contentUTF,"GBK");
					ret=ZQCUtility.assignRecordTxt(doc,request.getSession().getServletContext().getRealPath("/"),assignRole,userName);
//					PrintWriter out = response.getWriter();
//					out.write(String.valueOf(ret));
//					if(isDebug){
//						debug("ret:"+ret);
//					}
//					// String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
//					if (out.checkError()) {
//						error("ERROR: Client xmlToDB ");
//						error("ret:"+ret);
//					}
//					out.close();
				}
				
			}else if(action != null && action.equals("fjassignRecordTxt")){
				String content = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+ request.getParameter("content");
				String userName=request.getParameter("userName")==null?"":request.getParameter("userName");
//				String contentUTF = new String(content.getBytes("ISO-8859-1"), "UTF-8");
				String contentUTF = new String(content.getBytes("gbk"));
				int ret=-1;
				if(isDebug){
					debug("Client xml:"+contentUTF);
					debug(new String(content.getBytes("ISO-8859-1"), "gb2312"));
					debug("gb2312"+new String(content.getBytes("gb2312")));
					debug("gbk"+new String(content.getBytes("gbk")));
					debug(new String(content.getBytes("UTF-8")));
					debug(new String(content.getBytes("UTF-8"),"gbk"));
				}
				
				contentUTF = contentUTF.replaceAll("<\\?xml version=\"1.0\"\\?>", "");
				if(contentUTF.length()>0){
					Document doc = DOMTool.loadDocumentFromStr(contentUTF,"GBK");
					ret=ZQCUtility.fjassignRecordTxt(doc,request.getSession().getServletContext().getRealPath("/"),userName);
//					PrintWriter out = response.getWriter();
//					out.write(String.valueOf(ret));
//					if(isDebug){
//						debug("ret:"+ret);
//					}
//					// String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
//					if (out.checkError()) {
//						error("ERROR: Client xmlToDB ");
//						error("ret:"+ret);
//					}
//					out.close();
				}
				
			}else if(action != null && action.equals("LoadOrganizations")) {
				String orgJson=ZSecurityManager.getOrganizationJson();
				//debug("----------organizations : " +orgJson);
				PrintWriter out = response.getWriter();
				if (out.checkError()) {
					warn("---------exception in userRole-----------");
				}
				out.write(orgJson);
				out.flush();
				out.close();
			}else if(action != null && action.equals("LoadRosterXml")) {
//			    String openfire_dbipStr=String.valueOf(ConfInfo.proerties.get("openfire_dbip"));
//			    String formatTmp="<item jid=\"_ZSP_@"+openfire_dbipStr+"\" name=\"_ZSP_\" subscription=\"both\"><group>XIAOSHOU</group></item>";
			    String formatTmp="<item jid=\"_ZSP_\" name=\"_ZSP_\" subscription=\"both\"><group>XIAOSHOU</group></item>";
			    String sql="SELECT phone_number,phone_number AS phone FROM suSecurityUser";
                String rosterXml=DAOTools.queryFormatStr(sql,"ZQC",formatTmp);
                debug(" LoadRosterXml : " +rosterXml);
                if(rosterXml!=null && rosterXml.length()>0) {
                    rosterXml="<iq xmlns=\"jabber:client\" type=\"result\" id=\"roster_1\" ><query xmlns=\"jabber:iq:roster\">"+rosterXml+"</query></iq>"; 
                }
                PrintWriter out = response.getWriter();
                if (out.checkError()) {
                    warn("---------exception in userRole-----------");
                }
                out.write(rosterXml);
                out.flush();
                out.close();
            }else if(action !=null && action.equals("loadUserInfo")){
				String userName=request.getParameter("userName")==null?"":request.getParameter("userName");
				//debug("------------userInfo userName : " + userName);
				String userJson=ZSecurityManager.getUserInfoJson(userName);
				//debug("----------userInfo : " +userJson);
				PrintWriter out = response.getWriter();
				if (out.checkError()) {
					warn("---------exception in userInfo-----------");
				}
				out.write(userJson);
				out.flush();
				out.close();
			}else if(action != null && action.equals("saveFileToPath")){
				
				String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+request.getParameter("content");
				content= content.replaceAll("<\\?xml version=\"1.0\"\\?>", "");
//				debug(new String(content.getBytes("ISO-8859-1"), "gb2312"));
//				debug("gb2312"+new String(content.getBytes("gb2312")));
//				debug("gbk"+new String(content.getBytes("gbk")));
//				debug(new String(content.getBytes("UTF-8")));
//				debug(new String(content.getBytes("UTF-8"),"gbk"));
//				debug(new String(content.getBytes("gbk"),"UTF-8"));
//				debug(new String(content.getBytes("ISO-8859-1"), "UTF-8"));
//				String contentGB=new String(content.getBytes("gb2312"));
//				debug(content);
				
				String fileName = request.getParameter("fileName");
				File fileSave = new File(fileName);
				if(fileSave.exists()){
					fileSave.delete();
				}
//				if(!fileSave.exists())dateDir.mkdir();
//				debug(new String(content.getBytes("utf-8")));
//				debug(""+new String(content.getBytes(),"gbk").getBytes("UTF-8"));
//				
//				Utility.writeToFileEnd2(fileName , content.getBytes(), 0);
//				Utility.writeToFileEnd2(fileName+"1" , new String(content.getBytes("ISO-8859-1"), "gb2312").getBytes(), 0);
//				Utility.writeToFileEnd2(fileName+"2" , content.getBytes("gb2312"), 0);
//				Utility.writeToFileEnd2(fileName+"3" , content.getBytes("gbk"), 0);
//				Utility.writeToFileEnd2(fileName+"4" , content.getBytes(), 0);
				Utility.writeToFileEnd2(fileName , content.getBytes("UTF-8"), 0);
//				Utility.writeToFileEnd2(fileName+"6" , new String(content.getBytes("UTF-8"),"gbk").getBytes(), 0);
//				Utility.writeToFileEnd2(fileName+"7" ,new String(content.getBytes("gbk"),"UTF-8").getBytes(), 0);
//				Utility.writeToFileEnd2(fileName+"8" , new String(content.getBytes("ISO-8859-1"), "UTF-8").getBytes(), 0);
//				Utility.writeToFileEnd2(fileName+"9" , new String(content.getBytes("ISO-8859-1"), "gbk").getBytes(), 0);
				
			}else if (action != null && action.equals("saveZimRecord")) {
				// data:{'endTime':Z_ConEndTime,'action':'saveZimRecord','beginTime':Z_ConBeginTime,
				// 'recordText':chartRecHTML,'businessType':Z_NowSgName,
				// 'callNumber':peerNick,'callerNumber':sscName},
//				HashMap<String, String> sgIdName=ConfInfo.dynamicFields.get("sgNameMap");
				SimpleDateFormat formatx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat formatx2 = new SimpleDateFormat("yyyyMMddHHmmss");
				SimpleDateFormat formatD = new SimpleDateFormat("yyyyMMdd");	
				String endTime = request.getParameter("endTime");
				String beginTime = request.getParameter("beginTime");
				String recordText = "<html> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=gbk\"></meta>      <script type=\"text/javascript\"    src=\"../../js/soundmanager2-jsmin.js?v=2.575\"></script>  <script language=\"javascript\"> function Z_PreLoadImgxx(a,b,c) {opener.Z_PreLoadImgxx(a,b,c);} </script>    </head>"+request.getParameter("recordText")+"</html>";
				String businessType = request.getParameter("businessType")==null?"":request.getParameter("businessType");
				
//				String sgNameZHCN=businessType;
//				if(sgIdName.containsKey(businessType)){
//					sgNameZHCN=sgIdName.get(businessType);
//				}
				
				String callNumber = request.getParameter("callNumber")==null?"":request.getParameter("callNumber");
				String callerNumber = request.getParameter("callerNumber")==null?"":request.getParameter("callerNumber");
				String colineIP= request.getParameter("colineIP")==null?"":request.getParameter("colineIP");
				
				
				int onceSlavCount=DAOTools.loadOnceSlavCount(colineIP);
				
				
				String cusName= request.getParameter("cusName")==null?"":request.getParameter("cusName");
				String email= request.getParameter("email")==null?"":request.getParameter("email");
				String area= request.getParameter("area")==null?"":request.getParameter("area");
				String MYD= request.getParameter("MYD")==null?"":request.getParameter("MYD");
				String phyName= request.getParameter("phyName")==null?"":request.getParameter("phyName");
				String alias3= request.getParameter("alias3")==null?"":request.getParameter("alias3");
				String talkPeriodStr= request.getParameter("talkPeriod")==null?"":request.getParameter("talkPeriod");
				String queuePeriodStr= request.getParameter("queuePeriod")==null?"":request.getParameter("queuePeriod");
				String medType= request.getParameter("medType")==null?"":request.getParameter("medType");				
				
				HashMap<String, String> agentMap= SSCDBTools.loadAgentInfo(callNumber);
				
				phyName=agentMap.get("phyName");
				String alias1=agentMap.get("loginName");
				String agentNameLoad=agentMap.get("name");
				
//				String isS1=request.getParameter("isS1")==null?"":request.getParameter("isS1");
				int talkPeriod=0;
				int queuePeriod=0;
				
				try {
					if(talkPeriodStr.length()>0){
						talkPeriod=Integer.parseInt(Utility.round(talkPeriodStr, 0).toString());
					}
				} catch (Exception e) {
					error(e);
					talkPeriod=0;
				}
				
				try {
					if(queuePeriodStr.length()>0){
						queuePeriod=Integer.parseInt(Utility.round(queuePeriodStr, 0).toString());
					}
				} catch (Exception e) {
					error(e);
					queuePeriod=0;
				}
				
				String beginTimeStr=null;
				String beginTimeStr2=null;
				String beginStrDay=null;
				String fileName=null;
//				String servIP= request.getParameter("servIP");
				String servIP=request.getLocalAddr();
				debug(endTime+" saveZimRecord beginTime:"+beginTime+" businessType:"+businessType+" callNumber:"+callNumber+" callerNumber:"+callerNumber+" queuePeriod:"+queuePeriodStr+" talkPeriod:"+talkPeriodStr+" alias3:"+alias3+"cusName:"+cusName+" email:"+email+" area:"+area+" MYD:"+MYD+" phyName:"+phyName);
				
//				debug("recordText:"+recordText);
//				debug(request.getLocalAddr()+" local ip "+servIP);
//				servIP=request.getLocalAddr();
				
				if(beginTime!=null && beginTime.length()>0){
					beginTimeStr=formatx.format(new Timestamp(Long.parseLong(beginTime)));
					beginStrDay=formatD.format(new Timestamp(Long.parseLong(beginTime)));
					beginTimeStr2=formatx2.format(new Timestamp(Long.parseLong(beginTime)));
					endTime=formatx.format(new Timestamp(Long.parseLong(endTime)));
					String sql2 = "insert into SU_QC_SOURCE_RECORD_DATA(plan_id,srn_name,channel,"
						+ "begin_time,end_time,call_number,caller_number,phone_number,"
						+ "channel_state,file_exist,file_size,record_length,associated_sg,"
						+ "file_name,file_location,generate_time,business_type,alias1,alias2,alias3,is_mp3,client_name,email_adresse,quartier,client_satisfy,texte_longueur,client_attendre_length,repeter_numero,ip_adresse,alias6)"
						+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?, ?, ?, ?, ?, ?,?,?,?)";
				
					fileName=colineIP+"_"+callNumber +"_"+beginTime+"_"+ callerNumber+"_"+beginTimeStr2+".html";
					File dateDir = new File(SSC_ZIM_RECORD_DIR+beginStrDay);
					if(!dateDir.exists())dateDir.mkdir();
					File fileRecordFile=new File(SSC_ZIM_RECORD_DIR +beginStrDay+"/"+ fileName);
					
					PreparedStatement pstmt2 = null;
					Connection con2 = null;
					ResultSet rs = null;
					Statement stmt = null;
					try {
						con2 = DAOTools.getConnectionOutS("ZQC");
						
						if(fileRecordFile.exists()){ //
							String sqlCheck="select id,begin_time,file_name from SU_QC_SOURCE_RECORD_DATA where begin_time ='"+
							beginTimeStr+"' and file_name='"+fileName+"'";
							
							stmt = con2.createStatement();
							rs = stmt.executeQuery(sqlCheck);
							if (rs.next()) {
								int idTT=rs.getInt("id");
								String beginTimeeee=rs.getString("begin_time");
								String fileNameeee=rs.getString("file_name");
								debug("check SU_QC_SOURCE_RECORD_DATA has record id:"+idTT+" beginTime:"+beginTimeeee+" fileName:"+fileNameeee);
								
								sql2 = "update SU_QC_SOURCE_RECORD_DATA set client_name='"+cusName+"', email_adresse='"+email+"', "
									+ " quartier='"+area+"',client_satisfy='"+MYD+"' where id="+idTT ;
								debug("sql:"+sql2);
								
								stmt.executeUpdate(sql2);
							}
							
							try {
								stmt.close();
								rs.close();
							} catch (Exception e) {
								// TODO: handle exception
							}finally{
								stmt=null;
								rs=null;
							}
							
						}else{
							Utility.writeToFileEnd2(SSC_ZIM_RECORD_DIR +beginStrDay+"/"+ fileName , recordText.getBytes(), 0);
							pstmt2 = con2.prepareStatement(sql2);
							
							RecordData rd = null;
							rd = new RecordData();
							rd.plan_id = 1;
							rd.srn_name = colineIP;
							rd.ipAdresse= colineIP;
							rd.channel = 0;
							rd.begin_time = beginTimeStr;
							rd.end_time = endTime;
							rd.call_number = callNumber;
							rd.caller_number = callerNumber;
							rd.phone_number = callNumber;
							rd.channel_state = 0;
							rd.file_exist = 2;
							rd.file_size = recordText.length();
							rd.record_length = recordText.length();
							rd.associated_sg = businessType;
							rd.file_name = fileName;
							rd.file_location = servIP;
							rd.generate_time = formatx.format(new Timestamp(System.currentTimeMillis()));
							rd.business_type = businessType;
							rd.isMp3="txt";
							rd.cusName=cusName;
							rd.email=email;
							rd.area=area;
							rd.MYD=MYD;
							rd.alias1=alias1;
							rd.alias2=phyName;
							rd.alias3=alias3;
							rd.talkPeriod=talkPeriod;
							rd.queuePeriod=queuePeriod;
							rd.onceSlavCount=onceSlavCount;
							rd.ddh="";
							rd.call_type="";
							rd.alias6=agentNameLoad;
							DAOTools.setRecordData(pstmt2, rd);
							
							
							try {
								pstmt2.close();
							} catch (Exception e) {
								// TODO: handle exception
							}finally{
								pstmt2=null;
							}
						}
						
					
					} catch (Exception e) {
						error(e);
					}finally {
						DAOTools.releaseConnectionOutS("ZQC", con2);
					}
				}else{
					error("vvxc saveZimRecord  date");
				}
				
			}else if(action!=null && action.equals("toExcel")){
				HashMap<String, String> paramap=new HashMap<String, String>();
				String userName=request.getParameter("userName")==null?"":request.getParameter("userName");
				String sql=request.getParameter("sql");
				String dbId=request.getParameter("dbID");
				String fileName=request.getParameter("fileName");
				String filePath="/excel/";
				String fTitle=request.getParameter("fTitle");
				String attrNames=request.getParameter("attrNames");
				String statistical = request.getParameter("Statistical")==null?"":request.getParameter("Statistical");
				
				statistical=statistical.length()>0?Utility.decodeUTF2(statistical):"";
				
				debug(" toExcel param "+fTitle+" fileName "+fileName+" attrNames "+attrNames+" sql "+sql);
				paramap.put("fileName",fileName);
				paramap.put("fTitle",fTitle);
				paramap.put("attrNames",attrNames);
				paramap.put("sql",sql);
				paramap.put("dbID",dbId);	
				paramap.put("statistical",statistical);
				paramap.put("userName", userName);
				int len=ExcelTools.sql2ExcelFile(paramap);

//				ServletContext context  = request..getServletContext();
				// String mimetype=context.getMimeType(fileName);
				// String filename =
				// request.getParameter("filename");//"1a.txt";
				// String filepath = request.getParameter("filepath");//"d:\\";
//				PrintWriter out = null;
//				out = response.getWriter();
//				int i = 0;
//				response.setContentType("application/octet-stream");
//				response.setHeader("Content-Disposition","attachment;filename = " + fileName);
//				java.io.FileInputStream fileInputStream = new java.io.FileInputStream(fileName);
//				String outTmp="";
//				while ((i = fileInputStream.read()) != -1) {
//					outTmp+=i;
//					out.write(i);
//				}                                                                                        
//				if (out.checkError()) {
//					error(" toExcel Exception");
//				}
//				debug(outTmp);
//				out.close();
// response.setHeader("Content-Disposition", "attachment;");
//				header("Content-Type: application/force-download");         #表明是一个下载请求
//				header('Accept-Range : byte ');   #文件单位
//				header('Accept-Length: 3428');    #文件长度
//				header('Content-Disposition: attachment; filename=file.txt');#文件名
//				header("X-Accel-Redirect: /down/file.txt");
				response.setHeader("Content-Type", "application/force-download");
				response.setHeader("X-Accel-Redirect", filePath+fileName);
				response.setHeader("Content-Disposition", "attachment; filename="+fileName);
				response.setHeader("Accept-Range","byte");
				response.setHeader("Accept-Length",""+len);
				debug("toExcel end");
			}else if(action!=null && action.equals("loadZimRecordByIp")){
				String colineIP= request.getParameter("colineIP");
				String sql="select * from SU_QC_SOURCE_RECORD_DATA where srn_name='"+colineIP+"'";
				String ret=null;
				PrintWriter out = null;
				ret=DAOTools.easyQueryXml(sql, "ZQC", "SU_QC_SOURCE_RECORD_DATA", true,null);
				debug("colineIP:"+colineIP+" ret:"+ret);
				out = response.getWriter();
				
				out.write(ret);
//				if(isDebug){
//					debug(outTmp);
//				}
				if (out.checkError()) {
					error(" load Combo  loadMenuXml");
					error(ret);
				}
				out.flush();
				out.close();
				
			}else if (action != null && action.equals("querySIPNumber")) {
				Object[] resTmp = null;
				String phoneNumber=null;
				String sql="Select phone_number from SGNET_DEVICE where chtype=2 and zim_used=0";
				synchronized (this) {
					ArrayList<Object[]> al=DAOTools.execSelectS(sql, "z3000");
					for (int ii = 0; ii < al.size(); ii++) {
						resTmp = al.get(ii);
						if (resTmp[0] != null) {
							phoneNumber = String.valueOf(resTmp[0]);
							if (phoneNumber != null && phoneNumber.length() > 0) {
								String sql2="update SGNET_DEVICE set zim_used=1 where phone_number='"+phoneNumber+"'";
								DAOTools.execUpdateS(sql2, "z3000");
								break;
							}
						}
					}
				}
				
				if (phoneNumber == null && phoneNumber.length() == 0) {
					phoneNumber="#NOSIPPhone";
				}
				PrintWriter out = response.getWriter();
				if (out.checkError()) {
					warn("---------exception in userRole-----------");
				}
				out.write(phoneNumber);
				out.flush();
				out.close();
				
			}else if (action != null && action.equals("hangupSIPNumber")) {
				Object[] resTmp = null;
				String phoneNumber=null;
				String sql="Select phone_number from SGNET_DEVICE where chtype=2 and zim_used=1";
				synchronized (this) {
					ArrayList<Object[]> al=DAOTools.execSelectS(sql, "z3000");
					for (int ii = 0; ii < al.size(); ii++) {
						resTmp = al.get(ii);
						if (resTmp[0] != null) {
							phoneNumber = String.valueOf(resTmp[0]);
							System.out.println("phoneNumber=="+phoneNumber);
							if (phoneNumber != null && phoneNumber.length() > 0) {
								String sql2="update SGNET_DEVICE set zim_used=0 where phone_number='"+phoneNumber+"'";
								DAOTools.execUpdateS(sql2, "z3000");
								break;
							}
						}
					}
				}
			}else if(action != null && action.equals("sendMail")){
//				String txNo = request.getParameter("txNo");
				String content = request.getParameter("content");
				String title = request.getParameter("titlecc");
				String skillType = request.getParameter("skillType");
				String cusMail = request.getParameter("cusMail");
				String test = request.getParameter("test");
				
//				String comboParam2 = new String(content.getBytes("gbk"));
//				String comboParam3 = new String(content.getBytes("UTF-8"));
//				String comboParam4 = new String(content.getBytes("gbk"),"UTF-8");
//				String comboParam5 = new String(content.getBytes("UTF-8"),"gbk");
//				String comboParam6=new String(content.getBytes("ISO-8859-1"),"UTF-8");
				
//				debug("send mail comboParam2:"+comboParam2+" comboParam3:"+comboParam3+" comboParam4:"+comboParam4+" comboParam5:"+comboParam5+" comboParam6:"+comboParam6);
				
				
				ArrayList<Object[]> al = null;
				Object[] resTmp = null;
				String sql="select txNo,txPass,state,hostPop3,hostSmtp,skillType,pop3Port,needAuth from `Z_TX_INFO` a where a.`skillType`='"+skillType+"'";
				String txPass = null;
				String txState= null;
				String hostPop3 = null;
				String hostSmtp= null;
				String txNo =null;
//				String skillType= null;
				String pop3Port= null;
				String needAuth="true";
				al=DAOTools.execSelectS(sql, "ZDesk");
				for (int ii = 0; ii < al.size(); ii++) {
					resTmp = al.get(ii);
					if (resTmp[0] != null) {
						txNo = String.valueOf(resTmp[0]);
						txPass = String.valueOf(resTmp[1]);
						txState= String.valueOf(resTmp[2]);
						hostPop3 = String.valueOf(resTmp[3]);
						hostSmtp= String.valueOf(resTmp[4]);
						skillType= String.valueOf(resTmp[5]);
						pop3Port= String.valueOf(resTmp[6]);
						needAuth= String.valueOf(resTmp[7]);
						break;
					}
				}
				
//				if(txPass!=null && txPass.length()>0){
					TXInfo2 tx=new TXInfo2(0);
					tx.txNo=txNo;
					tx.password=txPass;
					tx.hostPop3=hostPop3;
					tx.hostSmtp=hostSmtp;
					tx.defaultSK="bocmail";
					tx.pop3Port="110";
					debug("sendMail begin title:"+title+" mailId:"+txNo+" hostPop3:"+hostPop3);
//					MailUtil2.estmpMain(content,title,cusMail,tx,test,"GBK");
//					MailUtil2.estmpMain(content,title,cusMail,tx,test,"GBK2312");
//					MailUtil2.estmpMain(content,title,cusMail,tx,test,"UTF-8");
					
//					if(txPass==null || txPass.length()==0){
//						needAuth="false";
//					}
					MailUtil2.sendMail2(content,title,cusMail,tx,test,"GBK",needAuth);
//					MailUtil2.sendMail2(content+"GBK",title,cusMail,tx,test,"GBK",needAuth);
//					MailUtil2.sendMail2(content+"GB2312",title,cusMail,tx,test,"GB2312",needAuth);
//					MailUtil2.sendMail2(content+"UTF-8",title,cusMail,tx,test,"UTF-8",needAuth);
					debug("mailSend action end");
//				}
				
			}else if(action != null && action.equals("zimGenConf")){
//				GenConfAll.gen();
			}else if(action != null && action.equals("ATKeepAlive")) {
			    retStr="alive";
			}else if(action !=null && action.equals("DESECB")) {
//			    
//			 22.188.136.158-http://22.188.159.17/ZDesk/ZDesk/XmlDBAction.do {encryctData=(hwO4kWbOc85dfkeNnXT2uyFD
//			        NwCxbd1fTfurI4sLw6z0Kev5Gx4f51U6mrtEoJtuP/Y7CwiimVKxXfUAgPwN2EXXywpaHqPKbFN27sP18JMZaBX+LgXKiA,)action=(DESECB,)type=(decode,)}
//			  12688 2013-09-29 20:14:03,616`_`catalina-exec-26`_`INFO `_`ZDesk`_`DESECB
//			  12689 2013-09-29 20:14:03,616`_`catalina-exec-26`_`DEBUG`_`ZDesk`_`null DESECB null
//			  ~                                                                                                      
			    String encryctDataStr=request.getParameter("encryptData");
                String type=request.getParameter("type");
			    
			    LogUtil.debug("DESkey key:"+new String(ECBTools.DESkey)+" DESECB begin "+ConfInfo.pBase.getProperty("bockey"), SKIP_Logger);
			    
			   
			    if(encryctDataStr!=null && encryctDataStr.length()>0) {
			        ECBTools tools = new ECBTools();
			        if(type!=null && type.equals("decode")) {
			            retStr=tools.decode(encryctDataStr);
			        }else if(type!=null && type.equals("encode")) {
			            retStr=tools.encode(encryctDataStr);
			        }
			    }
			    LogUtil.debug("DESkey key:"+ECBTools.DESkey+" "+encryctDataStr+" DESECB "+retStr, SKIP_Logger);
			    
			}else if(action != null && "JSONeditorSaverDataGet".equals(action)){
                String sql="select * from JSONeditorSaver";
                List jsonList=DAOTools.queryMap(sql, "ZDesk");
                String reJson=JSONSerializer.toJSON(jsonList).toString();
                postStrToClient(reJson, response);
            }else if(action !=null && "JSONeditorSaverDataSave".equals(action)){
                String id=request.getParameter("id")==null?"":request.getParameter("id");
                String name=request.getParameter("name")==null?"":request.getParameter("name");
                String type=request.getParameter("type")==null?"":request.getParameter("type");
                String miaoShu=request.getParameter("miaoShu")==null?"":request.getParameter("miaoShu");
                String jsons=request.getParameter("jsons")==null?"":request.getParameter("jsons");
                String sql="";
                List plist=new ArrayList();
                if(id.length()<=0){
                    sql="insert into JSONeditorSaver (`id`,`name`,`type`,`miaoShu`,`jsons`) values (?,?,?,?,?)";
                    plist.add(new RandomGUID().toString());
                    plist.add(name);
                    plist.add(type);
                    plist.add(miaoShu);
                    plist.add(jsons);
                }else{
                    sql="update JSONeditorSaver set `name`=?,`type`=?,`miaoShu`=?,`jsons`=? where `id`=?";
                    plist.add(name);
                    plist.add(type);
                    plist.add(miaoShu);
                    plist.add(jsons);
                    plist.add(id);
                }
                HashMap rmap=new HashMap();
                try{
                    DAOTools.updateForPrepared(sql, plist, "ZDesk");
                    rmap.put("success", true);
                }catch(Exception x){
                    rmap.put("success", false);
                    error("save JSONeditorSaverData error : " + x.getMessage());
                }
                postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
            }else if(action !=null && "JSONeditorSaverDataDel".equals(action)){
                String id=request.getParameter("id")==null?"":request.getParameter("id");
                boolean delOk=true;
                if(id.length()>0){
                    String sql="delete from JSONeditorSaver where id=?";
                    try{
                        DAOTools.updateForPrepared(sql, new String[]{id}, "ZDesk");
                    }catch(Exception x){
                        delOk=false;
                        error("save JSONeditorSaverData error : " + x.getMessage());
                    }
                }
                postStrToClient(String.valueOf(delOk), response);
            }
            else if (action != null && action.equals("saveWXForm"))
            {
//                insert into VT_ChatSession (inboundTime,endTime,MYD,MYD2,cusType,area,businessType1,businessType2,sgName,businessAgent1,businessAgent2,IMRBeginTime,BotBeginTime,BotTalkCount,CSRBeginTime,sessionId,ringBeginTime,chatBeginTime,hangUpTime,ip,cookie,agentId,agentName,colineNo,fileName) values('"+inboundTimeStr+"','"+endTimeStr+"','"+MYD+"','"+MYD2+"','"+cusType+"','"+area+"','"+businessType1+"','"+businessType2+"','"+sgName+"','"+businessAgent1+"','"+businessAgent2+"','"+IMRBeginTimeStr+"','"+BotBeginTimeStr+"','"+BotTalkCount+"','"+CSRBeginTimeStr+"','"+sessionId+"','"+ringBeginTimeStr+"','"+chatBeginTimeStr+"','"+hangUpTimeStr+"','"+ip+"','"+cookie+"','"+agentId+"','"+agentName+"','"+colineNo+"','"+fileName+"')";
                ArrayList<String> sqlExec=new ArrayList<String>();
//                JSONObject jsObj1 = null;
//                jsObj1.getString(key)
                String content = request.getParameter("content");
                LogUtil.debug("saveWXForm:" + content, SKIP_Logger);
                
                if (content != null && content.length() > 0)
                {
                    JSONObject jsObj = new JSONObject(content);
                    
                    if (jsObj.has("maxIndex"))
                    {
                        int maxIndex = jsObj.getInt("maxIndex");
                        String tableName=jsObj.getString("tableName");
                        String tableNameEn=Utility.converterToSpell(tableName);
                        Document docRet = DOMTool.loadDocumentFromStr("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><DataWindow BaseComponent=\"window\" ComponentType=\"PopEdit\" canEdit=\"true\" dataType=\"xml\" dbID=\"\" height=\"360\" isComet=\"false\" root=\"item\" title=\""+tableName+"\" type=\"\"  url=\"\" width=\"915\"></DataWindow>","UTF-8");
                        
                        Element headers = docRet.createElement("headers");
                        DOMTool.setAttribute(headers, "maxLevel", "1");
                        Element attributes = docRet.createElement("attributes");
                        Element header=null;
                        Element attribute=null;
                        for (int i = 0; i < maxIndex; i++)
                        {
                            
                            JSONObject jObjTmp = jsObj.getJSONObject("p" + i);
                            String Text=jObjTmp.getString("Text");
                            String Name=jObjTmp.getString("Name");
                            String Type=jObjTmp.getString("Type");
                            String Values=jObjTmp.getString("Values");
                            String DefaultValue=jObjTmp.getString("DefaultValue");
                            String Placeholder=jObjTmp.getString("Placeholder");
                            
                            String sql=" insert into VT_WXPage (`tableName`,`tableNameEn`,`Text`,`Name`,`Type`,`Values`,`DefaultValue`,`Placeholder`) values('"+tableName+"','"+tableNameEn+"','"+Text+"','"+Name+"','"+Type+"','"+Values+"','"+DefaultValue+"','"+Placeholder+"')";
                            sqlExec.add(sql);
                            
                            String index=""+(i+1);
                            header = docRet.createElement("header");

                            // <header attrIndex="1" hasSub="no" level="1"/>
                            DOMTool.setAttribute(header, "hasSub", "no");
                            DOMTool.setAttribute(header, "level", "1");
                            DOMTool.setAttribute(header, "attrIndex", index);
                              
                            attribute= docRet.createElement("attribute");
                            
                            DOMTool.setAttribute(attribute, "extend", "0001");
                            DOMTool.setAttribute(attribute, "dbType", "varchar");
                            DOMTool.setAttribute(attribute, "comboType", "");
                            DOMTool.setAttribute(attribute, "comboParam", "");
                            DOMTool.setAttribute(attribute, "fieldType", "3");
                            DOMTool.setAttribute(attribute, "index", index);
                            DOMTool.setAttribute(attribute, "isPk", "false");
                            DOMTool.setAttribute(attribute, "isSeach", "true");
                            DOMTool.setAttribute(attribute, "timeFormat", "");
                            DOMTool.setAttribute(attribute, "title", Text);
                            DOMTool.setAttribute(attribute, "xtype", "textfield");
                            DOMTool.setAttribute(attribute, "attrName", Name);
                            DOMTool.setAttribute(attribute, "fullName", tableNameEn);
                            DOMTool.setAttribute(attribute, "fieldLen", "90");
//                        XPathExpression expr = xpath.compile("/DataWindow/attributes/attribute[@index='"+index+"']");
//                        Object result = expr.evaluate(doc, XPathConstants.NODE);
//                        tmp = (Node) result;
                            DOMTool.appendChild(headers, header);
                            DOMTool.appendChild(attributes, attribute);
                            // LogUtil.debug("Name:"+jObjTmp.get("Name")+"
                            // Text:"+jObjTmp.get("Text"), SKIP_Logger);
                            // JSONObject jsObjP=

                        }
                        DAOTools.execBatchS(sqlExec, "ZDesk",false);
                    }
                }

                // if(content!=null && content.length()>0) {
                // JSONObject jsObj=new JSONObject(content);
                // // JSONObject jsonObject = new JSONObject(content);
                // }

            }else if(action!=null && action.equals("QJSON")) {
                String sql = request.getParameter("sql");

                LogUtil.debug("QJSON "+sql, SKIP_Logger);
//                sql=Utility.decodeUTFN(sql);
                
//                String contentGB2 = new String(sql.getBytes("gbk"));
//                String contentGB3 = new String(sql.getBytes("UTF-8"));
//                String contentGB4 = new String(sql.getBytes("gbk"), "UTF-8");
//                String contentGB5 = new String(sql.getBytes("UTF-8"), "gbk");
//                String contentGBx = new String(sql.getBytes("ISO-8859-1"),"gbk");
//                String contentGBx1 = new String(sql.getBytes("ISO-8859-1"),"utf-8");
//                // String contentGB4 = new
//                // String(msgCon.getBytes("ISO-8859-1"));
//                LogUtil.debug(sql+" contentGBx:" + contentGBx + "contentGBx1:"
//                        + contentGBx1 + " contentGB2:" + contentGB2
//                        + " contentGB3:" + contentGB3 + " contentGB4:"
//                        + contentGB4 + " contentGB5:" + contentGB5, SKIP_Logger);

                String encodeOrder = request.getParameter("encodeOrder");
                if (encodeOrder != null && encodeOrder.equals("true"))
                {
                    debug("encodeStrOrder sql:" + sql);
                    sql = Utility.encodeStrOrder(sql);
                }
                
                String dbId=request.getParameter("dbId");
                String tableName=request.getParameter("tableName");
                debug("QJSON sql:"+sql+" dbId:"+dbId+" tableName:"+tableName);
                String ret=null;
                if(sql!=null && dbId!=null && tableName!=null){
                    ret=DAOTools.easyQueryJSON(sql,dbId,tableName,true,null);
                }
                if(ret!=null && ret.length()>0){
                    
//                    ret=Utility.encodeStr(ret);
//                  response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    out.write(String.valueOf(ret));
                    
                  debug("ret:"+ret);
                    // String(data.DataToXml(DataSet.TYPE_XML_PRINT).getBytes(),"gb2312"));
                    if (out.checkError()) {
                        error("Exception: QJSON ");
                        error("ret:"+ret);
                    }
                    out.flush();
                    out.close();    
                }
            }
            else if (action!=null && action.equals("zimServerLogs"))
            {
                String StrEncode = "UTF-8";
                String logInfo = request.getParameter("content") == null ? ""
                        : new String(request.getParameter("content").getBytes(
                                "ISO-8859-1"), StrEncode);
                LogUtil.debug("ZIM_SERVER_LOG:" + logInfo, SKIP_Logger);
            }else if(action!=null && action.equals("WMonitorXml")) {
               
                
                String sql="select IF(sum(QueuingCount)>0,sum(QueuingCount),0) queueCount,IF(ROUND(sum(TelCountans)/(sum(TelCountans)+sum(GiveUpCount)),4)>0,ROUND(sum(TelCountans)/(sum(TelCountans)+sum(GiveUpCount)),4),0) as accRate ,alias4 from Tbl_TJCallcenterMss where alias1=\"语音\" and alias4 !=\"\" group by alias4 ";
//                String sql="select IF(sum(QueuingCount)>0,sum(QueuingCount),0) queueCount,IF(ROUND(sum(TelCountans)/(sum(TelCountans)+sum(GiveUpCount)),4)>0,ROUND(sum(TelCountans)/(sum(TelCountans)+sum(GiveUpCount)),4),0) as accRate ,alias4 from Tbl_TJCallcenterMss where alias1=\"语音\" and alias4=\"VIP\" or alias4=\"CC\"  group by alias4 ";
                retStr="<?xml version=\"1.0\" encoding=\"GBK\"?>";
                retStr+="<datasets>";
                retStr+=DAOTools.easyQueryXml2(sql,"ZMonitor","Tbl_TJCallcenterMss",false,null,null);
                
                sql="SELECT SUBSTRING(phyName,1,3) as businessType,IF(sum(case when StatuName=\"就绪\" then 1 else 0 end)>0,sum(case when StatuName=\"就绪\" then 1 else 0 end),0) as ReadyCount,IF(sum(case when StatuName=\"小休\" then 1 else 0 end)>0,sum(case when StatuName=\"小休\" then 1 else 0 end),0) as RestCount FROM Tbl_TJAgentDetail  WHERE ( phyName LIKE '普线%' OR phyName LIKE 'VIP%') GROUP BY SUBSTRING(phyName,1,3) ";
                retStr+=DAOTools.easyQueryXml2(sql,"ZMonitor","Tbl_TJAgentDetail",false,null,null);
                retStr+="</datasets>";
                
                LogUtil.debug("WMonitorXml ret "+retStr, SKIP_Logger);
            }
			
			if(callback!=null  && retStr!=null && retStr.length()>0){
			    retStr=callback+"('"+retStr+"')";
	        }
			
			if(retStr!=null){
				PrintWriter out = response.getWriter();
				out.write(String.valueOf(retStr));
			}
			
		} catch (Exception e) {
			error(" XmlDBAction service ");
			error(e);
		}
		return null;
	}
	
	
	   public void postStrToClient(String json, HttpServletResponse response) {
	        try {
	            response.setContentType("text/html;charset=UTF-8");
	            PrintWriter pw = response.getWriter();
	            pw.write(json);
	            pw.flush();
	            pw.close();
	        } catch (Exception x) {
	            LogUtil.error(x, SKIP_Logger);
	        }
	    }
	
	public static boolean hasInit=false;
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
}
