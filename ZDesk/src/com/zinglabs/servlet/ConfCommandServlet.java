package com.zinglabs.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.AppConfs;
import com.zinglabs.base.Interface.BaseInterface;
import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.ConfXmlObj;
import com.zinglabs.db.DAOTools;

import com.zinglabs.log.LogUtil;
import com.zinglabs.task.ReportExcelExpTask;
import com.zinglabs.util.SSCUtil;
import com.zinglabs.util.commonTask.CommonTask;

/**
 * 
 * 此应用中最先加载的servlet 主要用于各种组件的初始化
 * 
 * 依赖baselib
 *
 */
public class ConfCommandServlet  extends HttpServlet{

	private static final String CONTENT_TYPE = "text/html; charset=GBK";   
	private static final long serialVersionUID = -1113276910757142653L;
	public void destroy() {
		super.destroy();
	}

	/**
	 * 系统启动时一些参数的初始化
	 * 
	 * 依赖baselib
	 * 
	 */
	public void init() throws ServletException {
		
		DAOTools.initAllStatic();
		//EXCEL 生成线程启动
		String runCycle=ConfInfo.confMapDBConf.get("ZDESK_EXCEL_EXP_TASK_RUNCYCLE");
		long runCyclel=0;
		try{
			if(runCycle==null || runCycle.length()<=0){
				runCyclel=5 * 60 * 1000;
			}else{
				runCyclel=Long.parseLong(runCycle);
			}
		}catch(Exception x){
			runCyclel=5 * 60 * 1000;
			LogUtil.error("ZDESK_EXCEL_EXP_TASK_RUNCYCLE 参数异常，无法转换成为Long类型数据。", SKIP_Logger);
			LogUtil.error(x, SKIP_Logger);
		}
		ReportExcelExpTask reet=new ReportExcelExpTask();
		CommonTask ct=new CommonTask(reet,Long.parseLong(runCycle),0);
		Thread t=new Thread(ct);
		t.start();
		super.init();
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processCommands(req, resp);
		super.doPost(req, resp);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processCommands(req, resp);
		super.doGet(req, resp);
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * DESC 动态命令接收处理如xml配置文件变化 重新载入
	 * 动态调整日志级别 
	 * 不重启服务情况下启动关闭某些功能等
	 * 
	 */
	
	protected void processCommands(HttpServletRequest req, HttpServletResponse resp){
		String action=req.getParameter("action");
		String contentGB=null;
		String ret=null;
		
		try
        {

	        if(req.getParameter("content")!=null){
//	          try {
//	              contentGB=Utility.getCodeStr(req.getParameter("content"),"ISO-8859-1","gb2312");
//	          } catch (Exception e) {
//	              LogUtil.error("Utility.getCodeStr", SKIP_Logger);
//	              LogUtil.error(e, SKIP_Logger);
//	          }
	            
	            String content = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+ req.getParameter("content");
	            try {
	                contentGB = new String(content.getBytes("gbk"));
	            } catch (UnsupportedEncodingException e) {
	                LogUtil.error("Utility.getCodeStr", SKIP_Logger);
	                LogUtil.error(e, SKIP_Logger);
	            }
	            contentGB = contentGB.replaceAll("<\\?xml version=\"1.0\"\\?>", "");
	        }
	        
	        LogUtil.debug("CommandService action:"+action+" content:"+contentGB, SKIP_Logger);
	        if (action != null && action.equals(BaseInterface.ACTION_RELOAD_PAGE_XML_ALL)) {
	            LogUtil.debug("ACTION_RELOAD_PAGE_XML_ALL clear all", SKIP_Logger);
	            ConfXmlObj.XMLObjMap.clear();
	        }else if(action != null && action.equals("zimGenConf")){
//	            GenConfAll.gen();
	        }else if(action != null && action.equals(BaseInterface.ACTION_INIT_COMPANY)){
	            String companyName=req.getParameter("companyName");
	            String companyId=req.getParameter("companyId");
	            if(companyName!=null && companyName.length()>0 && companyId!=null && companyId.length()>0) {
	                
//	              String skNameTmp=ATUtility.converterToSpell(companyName);
	                
	                SSCUtil.initCompany(companyId);
	            }
	        }else if(action != null && action.equals(BaseInterface.ACTION_ADD_COMPANY_AGENT)) {
	            String loginName=req.getParameter("loginName");
	            String companyName=req.getParameter("companyName");
	            String companyId=req.getParameter("companyId");
	            if(loginName!=null && loginName.length()>0 && companyId!=null && companyId.length()>0) {
	                try
	                {
	                    String phoneNum=SSCUtil.getNewAutoPhoneNum(loginName);
	                    if(phoneNum!=null && phoneNum.length()>0) {
	                        SSCUtil.addSSCAgent(companyId,phoneNum,"1");
	                        ret=phoneNum;
	                    }
	                }
	                catch (Exception e)
	                {
	                    LogUtil.error(e, SKIP_Logger);
	                    ret="Exception";
	                }
	            }
	        } if(action != null && action.equals(BaseInterface.ACTION_SEND_MAIL)){
	            try {
//	              resp.setContentType(CONTENT_TYPE);
	                String host = "22.127.27.199";
	                String to = req.getParameter("to");
//	              String from = req.getParameter("from");
	                String subject = req.getParameter("subject")==null?"中行在线客服邮件":req.getParameter("subject");
	                String messageContent = req.getParameter("content");
	                
	                HtmlEmail email = new HtmlEmail(); 
	                email.setHostName(host);
	                email.setSmtpPort(25);
	                email.setAuthenticator(new DefaultAuthenticator("abc@boc.com","abc"));
//	              email.setDebug(true);
	                email.setSSL(false);
	                email.setTLS(false);
//	              email.setSSLOnConnect(false);
	                email.setFrom("abc@boc.com");
	                email.setSubject(subject);
	                email.setHtmlMsg(messageContent);  // set the alternative message  
	                email.setTextMsg("抱歉！您的邮件客户端不支持html格式");  // send the email
	                email.addTo(to);
	                email.send();
	                
//	              resp.setContentType(CONTENT_TYPE);
//	              // PrintWriter out = resp.getWriter();
//	              //
//	              // out.println(" <html> ");
//	              // out.println(" <head> <title> MailServlet </title> </head> ");
	//
//	              Authenticator authenticator = new Authenticator() {
//	                  protected PasswordAuthentication getPasswordAuthentication() {
//	                      return new PasswordAuthentication("abc@boc.com", "abc");
//	                  }
//	              };   
//	              Properties prop = new Properties();
//	              prop.setProperty("mail.smtp.host", "22.127.27.199");   
//	              //发送邮件的协议   
//	              prop.setProperty("mail.transport.protocol", "smtp");   
//	              //在连接服务器的时候是否需要验证，发邮件是需要验证的   
//	              prop.setProperty("mail.smtp.auth", "true");   
//	              
//	              Session mySession = Session.getInstance(prop, authenticator);
//	              Message msg = new MimeMessage(mySession);
//	              msg.setFrom(new InternetAddress(from));
//	              InternetAddress[] address = { new InternetAddress(to) };
//	              msg.setRecipients(Message.RecipientType.TO, address);
//	              msg.setSubject(subject);
//	              msg.setSentDate(new Date(System.currentTimeMillis()));
//	              msg.setText(messageContent);
//	              Transport.send(msg);

	                // out.println(" <body> ");
	                // out.println("Mail was send to " + to);
	                // out.println("from " + from);
	                // out.println("using host " + host + ". ");
	                // out.println(" </body> </html> ");

	            } catch (Exception e) {
	                LogUtil.error("processCommands ACTION_SEND_MAIL", SKIP_Logger);
	                LogUtil.error(e, SKIP_Logger);
	            }
	        }
	        
	        
	        if(ret!=null) {
//	          if(callback!=null  && retStr!=null && retStr.length()>0){
//	                retStr=callback+"('"+retStr+"')";
//	            }
	            
	            PrintWriter out = resp.getWriter();
	            out.write(String.valueOf(ret));
	        }
        }
        catch (Exception e)
        {
            LogUtil.error(e, SKIP_Logger);
        }
	}
	
	
	/**
	 * be careful with no method state
	 */
	static {
		if(!DAOTools.hasInitStatic){
			DAOTools.hasInitStatic=true;
			DAOTools.initAllStatic();
		}
	}
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
}
