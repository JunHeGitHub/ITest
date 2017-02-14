package com.zinglabs.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.CometEvent;
import org.apache.catalina.CometProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.Interface.BaseInterface;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.db.ShareComManager;
import com.zinglabs.log.LogUtil;
import com.zinglabs.task.SendTask;
import com.zinglabs.tools.Utility;
/**
 * comet 数据推送服务
 * @author Liduo.wang
 *
 */
public class SenderService extends HttpServlet implements CometProcessor{

	private static final long serialVersionUID = 1L;

	
	public void destroy() {
		super.destroy();
	}
	
	/**
	 * TODO 增加初始化日志和配置文件解析 
	 * 日志可以使用 LogUtil.error(e, SKIP_Logger); 或 通过 继承 BaseAbs error(e)方式调用
	 */
	public void init() throws ServletException {
		
		super.init();
//		LogUtil.init();
		try {
			ConfInfo.parseConfDB();
			LogUtil.debug(DAOTools.hasInitStatic+" init SenderService begin "+ConfInfo.hasParse, SKIP_Logger);
		} catch (Exception e) {
			LogUtil.error("init SenderService", SKIP_Logger);
			LogUtil.error(e, SKIP_Logger);
		}
	}
	

	/**
	 * TODO 握手及数据发送逻辑： 客户端请求连接（SendService.java），服务器给客户端生成id
	 * 并发有结构无内容的空xml到客户端
	 * 
	 * 
	 * 客户端根据客户打开的页面生成描述xml，并向参数调整服务(CommandService.java)发送请求（所有请求都需带服
	 * 务器分配的id 如客户登录后无打开页面操作，则发空描述xml---带xml结构，无内容的数据服
	 * 务器端）。服务器端无论有无页面内容，只要收到id一致，都标志握手成功。
	 * 
	 * 握手后，服务器根据当前用户的刷新时间，持续向客户端推数据，如无数据则推有结构但无内
	 * 容的xml。
	 * 
	 */
	public void event(CometEvent event) throws IOException, ServletException {
		try {
			//System.out.println("------------------------- thread start Id" +Thread.currentThread().getId());
			HttpServletRequest request = event.getHttpServletRequest();  
			HttpServletResponse response = event.getHttpServletResponse();
			if (event.getEventType() == CometEvent.EventType.BEGIN) {
				event.setTimeout(Integer.MAX_VALUE); // add
				
//				PrintWriter writer = response.getWriter();
//			//	writer.println("<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">");
//				writer.println("<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gbk\" /><head><script type=\"text/javascript\"></script></head><body>");
//				writer.println("<script type=\"text/javascript\">");
////				writer.println("var comet = window.parent.comet;");
//				writer.println("</script>");
//				writer.flush();
				
				
				// for chrome
//				if (request.getHeader("User-Agent").contains("KHTML")) {
//					for (int i = 0; i < 100; i++) {
//						writer.print("<input type=hidden name=none value=none>");
//					}
//					writer.flush();
//				}

				
				//response.setContentType("text/xml");
		        //response.setHeader("Cache-Control", "no-cache");
				response.setCharacterEncoding("gb2312");
			    response.setHeader( "Pragma", "no-cache" );
			    response.addHeader( "Cache-Control", "must-revalidate" );
			    response.addHeader( "Cache-Control", "no-cache" );
			    response.addHeader( "Cache-Control", "no-store" );
			    response.setDateHeader("Expires", 0);
//			    response.addHeader("cc", "                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ");
//				response.setBufferSize(1);
				//初始化数据推送线程
				String action=request.getParameter("action");
				String clientId=request.getParameter("clientId");
				
				String contentGB=null;
				String ip=null;
				if(request.getParameter("content")!=null){
					try {
						contentGB=Utility.getCodeStr(request.getParameter("content"),"ISO-8859-1","gb2312");
					} catch (Exception e) {
						LogUtil.error("Utility.getCodeStr", SKIP_Logger);
						LogUtil.error(e, SKIP_Logger);
					}
				}
				
				if(contentGB.indexOf(BaseInterface.COMET_MESSAGE_CLIENT_BEGIN_CONECT)>=0&& clientId!=null && clientId.length()>0){
					ip=Utility.getIpAddr(request);//无效ip情况
					SendTask st=new SendTask(request,response,ip,clientId);
					Thread tst=new Thread(st);
					tst.start();
				}
				
				LogUtil.info("ip:"+ip+" action:"+action+" content:"+contentGB+" clientId:"+clientId, SKIP_Logger);
			}else if (event.getEventType() == CometEvent.EventType.END) {
				//结束 移除对response 的保持 
				LogUtil.info("------E N D.....", SKIP_Logger);
				ShareComManager.destroyShareCom(request, response);
				PrintWriter writer = response.getWriter();
				writer.println("</body></html>");
				event.close();
			}else if (event.getEventType() == CometEvent.EventType.READ) {
				LogUtil.info("server is read.", SKIP_Logger);
				//在请求为POST时，会用到这里。
				InputStream is = request.getInputStream();
				byte[] buf = new byte[512];
				do {
					int n = is.read(buf); // can throw an IOException
					if (n > 0) {
						LogUtil.info("Read " + n + " bytes: " + new String(buf, 0, n)
								+ " for session: "
								+ request.getSession(true).getId(), SKIP_Logger);
					} else if (n < 0) {
						return;
					}
				} while (is.available() > 0);
				
			}else if (event.getEventType() == CometEvent.EventType.ERROR) {
				//报错 移除对 response 的保持 
				LogUtil.info("------E R R O R....."+event.getEventType()+" error subType"+event.getEventSubType(), SKIP_Logger);
				ShareComManager.destroyShareCom(request, response);
				event.close();
			}
		} catch (Exception e) {
			LogUtil.error(e, SKIP_Logger);
		}
	}
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
}
