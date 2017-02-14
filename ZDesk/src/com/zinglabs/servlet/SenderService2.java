package com.zinglabs.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.CometEvent;
import org.apache.catalina.CometProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.zinglabs.base.Interface.BaseInterface;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.ConfXmlObj;
import com.zinglabs.db.ShareComManager;
import com.zinglabs.log.LogUtil;
import com.zinglabs.task.SendTask;
import com.zinglabs.tools.Utility;

import demo.ChatServlet.MessageSender;
/**
 * comet 数据推送服务
 * @author Liduo.wang
 *
 */
public class SenderService2 extends HttpServlet implements CometProcessor{

	private static final long serialVersionUID = 1L;
	

	// <用户,长连接>
	protected static Map<String, ShareComManager> connections = new HashMap<String, ShareComManager>();

	// 消息推送线程
	protected static MessageSender2 messageSender = null;

	public void init() throws ServletException {
		// 启动消息推送线程
		messageSender = new MessageSender2();
		Thread messageSenderThread = new Thread(messageSender, "MessageSender["
				+ getServletContext().getContextPath() + "]");
		messageSenderThread.setDaemon(true);
		messageSenderThread.start();
	}

	public void destroy() {
		connections.clear();
		messageSender.stop();
		messageSender = null;
	}
	
	public void event(CometEvent event) throws IOException, ServletException {
		HttpServletRequest request = event.getHttpServletRequest();
		HttpServletResponse response = event.getHttpServletResponse();
//		// 昵称
//		String name = request.getParameter("name");
//		if (name == null) {
//			return;
//		}

		if (event.getEventType() == CometEvent.EventType.BEGIN) {
			
			
			LogUtil.debug("Begin of comet ",SKIP_Logger);
			

			// 欢迎信息
//			writer.print("<script type=\"text/javascript\">");
//			writer.println("comet.newMessage('Hello " + name + ", Welcome!');");
//			writer.print("</script>");
//			writer.flush();

//			// 通知其他用户有新用户登陆
//			if (!connections.containsKey(name)) {
//				messageSender.login(name);
//			}

			// 推送已经登陆的用户信息
//			for (String user : connections.keySet()) {
//				if (!user.equals(name)) {
//					writer.print("<script type=\"text/javascript\">");
//					writer.println("comet.newUser('" + user + "');");
//					writer.print("</script>");
//				}
//			}
//			writer.flush();
			response.setCharacterEncoding("gb2312");
		    response.setHeader( "Pragma", "no-cache" );
		    response.addHeader( "Cache-Control", "must-revalidate" );
		    response.addHeader( "Cache-Control", "no-cache" );
		    response.addHeader( "Cache-Control", "no-store" );
		    response.setDateHeader("Expires", 0);

		    
		    
		 // Http连接空闲超时
			event.setTimeout(Integer.MAX_VALUE);
			// 创建Comet Iframe
			PrintWriter writer = response.getWriter();
			writer.println("<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">");
			writer.println("<html><head><script type=\"text/javascript\">var comet = window.parent.comet;</script></head><body>");
			writer.println("<script type=\"text/javascript\">");
			writer.println("var comet = window.parent.comet;");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Timestamp tt=new Timestamp(System.currentTimeMillis());
			String ttStr=format.format(tt);
//			writer.println("var timestatmServ = "+System.currentTimeMillis()+";");
//			writer.println("var timestatmLocal = (new Date()).getTime();");
			writer.println("</script>");
			writer.flush();

			// for chrome
			if (request.getHeader("User-Agent").contains("KHTML")) {
				for (int i = 0; i < 100; i++) {
					writer.print("<input type=hidden name=none value=none>");
				}
				writer.flush();
			}
		    
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
			
//			if(contentGB.indexOf(BaseInterface.COMET_MESSAGE_CLIENT_BEGIN_CONECT)>=0&& clientId!=null && clientId.length()>0){
				ip=Utility.getIpAddr(request);//无效ip情况
//				SendTask st=new SendTask(request,response,ip,clientId);
//				Thread tst=new Thread(st);
//				tst.start();
				synchronized (connections) {
					
					ShareComManager sc=new ShareComManager(response,clientId);
					ShareComManager.shareMap.put(clientId, sc);
					request.setAttribute("clientId", clientId);
//					ShareComManager.shareMap.put(clientId, sc);
					connections.put(clientId, sc);
				}
//			}
			
			LogUtil.info("ip:"+ip+" action:"+action+" content:"+contentGB+" clientId:"+clientId, SKIP_Logger);
			
			
		} else if (event.getEventType() == CometEvent.EventType.ERROR) {
			LogUtil.error("Error for session: " + request.getSession(true).getId(),SKIP_Logger);
			ShareComManager.destroyShareCom(request, response);
			String clientId=request.getAttribute("clientId")==null?"":request.getAttribute("clientId").toString();
			connections.remove(clientId);
			event.close();
		} else if (event.getEventType() == CometEvent.EventType.END) {
			LogUtil.debug("End for session: " ,SKIP_Logger);
			ShareComManager.destroyShareCom(request, response);
			String clientId=request.getAttribute("clientId")==null?"":request.getAttribute("clientId").toString();
			connections.remove(clientId);
			PrintWriter writer = response.getWriter();
			writer.println("</body></html>");
			event.close();
		} else if (event.getEventType() == CometEvent.EventType.READ) {
			InputStream is = request.getInputStream();
			byte[] buf = new byte[512];
			do {
				int n = is.read(buf); // can throw an IOException
				if (n > 0) {
					LogUtil.debug("Read " + n + " bytes: " + new String(buf, 0, n)
							+ " for session: "
							+ request.getSession(true).getId(),SKIP_Logger);
				} else if (n < 0) {
					return;
				}
			} while (is.available() > 0);
		}
	}

	

	public class MessageSender2 implements Runnable {

		protected boolean running = true;
		protected Map<String, String> messages = new HashMap<String, String>();

		public MessageSender2() {

		}

		public void stop() {
			running = false;
		}

		
		public void run() {
			while (running) {
//				if (messages.size() == 0) {
//					try {
//						synchronized (messages) {
//							messages.wait();
//						}
//					} catch (InterruptedException e) {
//						// Ignore
//					}
//				}
				
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e) {
					LogUtil.error( e,SKIP_Logger);
				}
//				
				synchronized (connections) {
					
					for (ShareComManager sc : connections.values()) {
						try {
							if(sc==null || sc.response==null){
								connections.remove(sc);
								continue;
							}
							sc.genAndWrite(sc.response);
							
//							PrintWriter writer = response.getWriter();
//							writer.print("<script type=\"text/javascript\" msg='<?xml version=\"1.0\" encoding=\"GBK\"?><datasets><table id=\"zuoxizhuangtaimingxijiankong\"><item AgentId=\"6014\" StatuId=\"3\" StatuName=\"就绪\" ContinuedTime=\"93\" TelephoneCode=\"\" DealTime=\"00:01:33\" TelCount=\"21\" phyName=\"张天宠组\" agentName=\"毕涛\"  /><item AgentId=\"5555\" StatuId=\"6\" StatuName=\"话务整理\" ContinuedTime=\"14779\" TelephoneCode=\"\" DealTime=\"04:06:19\" TelCount=\"0\" phyName=\"YY_CC\" agentName=\"测试\"  /><item AgentId=\"6049\" StatuId=\"6\" StatuName=\"话务整理\" ContinuedTime=\"624\" TelephoneCode=\"\" DealTime=\"00:10:24\" TelCount=\"0\" phyName=\"张天宠组\" agentName=\"张天宠\"  /><item AgentId=\"6050\" StatuId=\"4\" StatuName=\"双方通话\" ContinuedTime=\"183\" TelephoneCode=\"13034002333\" DealTime=\"00:03:03\" TelCount=\"5\" phyName=\">张天宠组\" agentName=\"张健061465\"  /><item AgentId=\"8026\" StatuId=\"3\" StatuName=\"就绪\" ContinuedTime=\"1491\" TelephoneCode=\"\" DealTime=\"00:24:51\" TelCount=\"3\" phyName=\"VIP-郭鹏组\" agentName=\"纪萌萌\"  /></table></datasets>' >");
//							writer.println("comet.newMessage('"+ttStr+" <datasets><table id=\"zuoxizhuangtaimingxijiankong\"><item AgentId=\"6014\" StatuId=\"3\" StatuName=\"就绪\" ContinuedTime=\"93\" TelephoneCode=\"\" DealTime=\"00:01:33\" TelCount=\"21\" phyName=\"张天宠组\" agentName=\"毕涛\"  /><item AgentId=\"5555\" StatuId=\"6\" StatuName=\"话务整理\" ContinuedTime=\"14779\" TelephoneCode=\"\" DealTime=\"04:06:19\" TelCount=\"0\" phyName=\"YY_CC\" agentName=\"测试\"  /><item AgentId=\"6049\" StatuId=\"6\" StatuName=\"话务整理\" ContinuedTime=\"624\" TelephoneCode=\"\" DealTime=\"00:10:24\" TelCount=\"0\" phyName=\"张天宠组\" agentName=\"张天宠\"  /><item AgentId=\"6050\" StatuId=\"4\" StatuName=\"双方通话\" ContinuedTime=\"183\" TelephoneCode=\"13034002333\" DealTime=\"00:03:03\" TelCount=\"5\" phyName=\">张天宠组\" agentName=\"张健061465\"  /><item AgentId=\"8026\" StatuId=\"3\" StatuName=\"就绪\" ContinuedTime=\"1491\" TelephoneCode=\"\" DealTime=\"00:24:51\" TelCount=\"3\" phyName=\"VIP-郭鹏组\" agentName=\"纪萌萌\"  /></table></datasets>cccccccccccccccccccccccc就绪就绪就绪就绪就绪就绪就绪就绪');");
//							writer.print("</script>");
//							writer.flush();
						} catch (Exception e) {
							LogUtil.error( e,SKIP_Logger);
						}
					}
				}
			}
		}
	}
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
}
