package com.zinglabs.task;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.ShareComManager;

import com.zinglabs.tools.RandomGUID;
import com.zinglabs.tools.Utility;

//import zinglabs.comet.frame.serviceServlet.ShareComManager;
//import zinglabs.comet.frame.serviceServlet.dataAdapter.DataPoll;
//import zinglabs.comet.frame.serviceServlet.shareComponent.ShareCom;
//import zinglabs.comet.frame.serviceServlet.shareComponent.Component.CometSend;

/**
 * 数据推送线程
 * 
 * @author Liduo.wang
 * 
 */
public class SendTask extends BaseAbs implements Runnable {

	/**
	 * TODO 线程安全变量flag，有可能外部访问。直接开放变量访问
	 */
	public volatile boolean flag;
	
	public int debugCount=0;
	
	/**
	 * TODO 变量不用加get set方法直接开放 访问 
	 */
	public String clientId;

	public Timestamp time;
	
	public PrintWriter writer;
	
	public HttpServletRequest request;
	
	public HttpServletResponse response;
	
	public String ieType;
	
	public String ip;
	
	private RandomGUID rg;
	
	public ShareComManager sc;
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
	public SendTask(HttpServletRequest request,HttpServletResponse response,String ip,String clientId) {
		init(SKIP_Logger);
		this.request=request;
		this.response=response;
		this.ip=ip;
		this.flag=true;
//		this.rg=new RandomGUID();
		
//		this.clientId=rg.toString();
		
//		this.clientId=Utility.genClientId();
		try {
			this.writer=response.getWriter();
//			this.writer = new PrintWriter(response.getOutputStream(), true);
		} catch (IOException e) {
			error("SendTask PrintWriter ");
			error(e);
		}
		this.sc=new ShareComManager();
		sc.task=this;
		this.clientId=clientId;
		sc.T=Thread.currentThread();
		ShareComManager.shareMap.put(clientId, sc);
		request.setAttribute("clientId", this.clientId);
		this.ieType=null;
	}

	public void stop() {
		this.flag = false;
	}

	public void run() {
		long time=0L,sleepValue=0L,toXmlTime=0L;
		
		while (flag) {
			time=System.currentTimeMillis();
			
			sleepValue=(long)this.sc.dataRefreshTime-(System.currentTimeMillis()-time);
//			debug("sleepValue:"+sleepValue);
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				error(e);
			}
			
			
			if(this.clientId==null)continue;
			
//			if(ieType==null){
//				if (request.getHeader("User-Agent").contains(USER_IE_TYPE_CHROME)) {
//					this.ieType=USER_IE_TYPE_CHROME;
//					
//					//for (int i = 0; i < 2; i++) {
//					writer.write("<input type=hidden name=none value=none>");
//						//writer.print("<input type=hidden name=none value=none>");
//				//	}
//					//writer.flush();
//				}else{
//					this.ieType=USER_IE_TYPE_IE;
//				}
//			}
			

//			writer.println("<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">");
//			writer.println("<html><head><script type=\"text/javascript\">var comet = window.parent.comet;</script></head><body>");
//		//	writer.println("<script type=\"text/javascript\">");
//		//	writer.println("var comet = window.parent.comet;");
//		//	writer.println("</script>");
////			writer.println("<script type=\"text/javascript\">var comet = window.parent.comet;</script>");
//			writer.flush();

			// for chrome
		/*	if (request.getHeader("User-Agent").contains("KHTML")) {
				for (int i = 0; i < 100; i++) {
					writer.print("<input type=hidden name=none value=none>");
				}
				writer.flush();
			}
			*/
		/*	writer.print("<script type=\"text/javascript\">");
			writer.println("comet.parseData_();");
			writer.print("</script>");
			writer.flush();
			debug("for chrome end");*/
			try {
//				String ret=sc.toXml(this.ieType);
				toXmlTime=System.currentTimeMillis();
				//debug("toXml begin "+toXmlTime);
				String ret=sc.toXml();
//				sc.genAndWrite(response);
//				debug("SendTask send xml:"+ret);
				//debug((System.currentTimeMillis()-toXmlTime)+"toXml end "+System.currentTimeMillis());
				writeAll(ret);
				if(++debugCount>100000){
					debug("SendTask send xml:"+ret);
					debug(sleepValue+" ret xml send is ok! ");
					debugCount=0;
				}
				
			} catch (Exception e) {
				error(" SendTask wirte Exc ");
				error(e);
				this.flag=false;
			}
		}
//		if(this.writer!=null){
//			this.writer.close();
//			this.writer=null;
//		}
		
//		cleanAll();
	}
	
	
	public boolean writeAll(String content){
		if(content==null)return true;
		try{
			if(flag&&writer!=null && !writer.checkError()){
				writer.write(content);
				writer.flush();
//				out.flush();
			}else{
				error("Exc writer error");
				this.flag=false;
			}
		}catch(Exception x){
			error(x);
			this.flag=false;
//			debug(x.getMessage());
		}
		return this.flag;
	}
	
	public void cleanAll(){
		this.flag=false;
	}
	

	/**
	 * 过程方式获取数据
	 */
//	private void doItForProcess() {
//		ShareCom sc = ShareComManager.getSharCom(clientId);
//		if (sc == null) {
//			System.out.println("-----------: ShareCom is null ");
//			return;
//		}
//		CometSend cs = sc.getCometSend();
//		if (cs == null) {
//			System.out.println("-----------: CometSend is null ");
//			return;
//		}
//		DataPoll dp = new DataPoll(this.clientId);
//		while (flag) {
//			// 获取要推的数据
//			try {
//				String sendStr = dp.getPollData();
//				if (sendStr == null || "".equals(sendStr)) {
//					Thread.sleep(2000);
//					continue;
//				}
//				//System.out.println("--------------  sendstr ");
//				if (writer == null) {
//					HttpServletResponse res = sc.getResponse();
//					if (res == null) {
//						System.out.println("-----------: Response is null ");
//						this.flag = false;
//						break;
//					}
//					writer = res.getWriter();
//				}
//				
//				writer.write(sendStr);
//				// writer.flush();
//				// 出错控制
//				if (writer.checkError()) {
//					System.out.println("------------ writer : E R R O R ......");
//					this.flag = false;
//					break;
//				}
//				Thread.sleep(2000);
//				// 置空下发的数据
//				// sc.getCometSend().setSendXmlStr("");
//			} catch (Exception x) {
//				System.out.println("---------------- Exception in sender: ");
//				this.flag = false;
//				x.printStackTrace();
//			}
//		}
//		System.out.println("------------ send task is end");
//	}

	/**
	 * 线程方式获取数据。
	 * 
	 */
//	private void doItForThread() {
//
//		ShareCom sc = ShareComManager.getSharCom(clientId);
//		if (sc == null) {
//			System.out.println("-----------: ShareCom is null ");
//			return;
//		}
//		CometSend cs = sc.getCometSend();
//		if (cs == null) {
//			System.out.println("-----------: CometSend is null ");
//			return;
//		}
//		DataPollTask dpt = new DataPollTask(this.clientId);
//		Thread tdpt = new Thread(dpt);
//		tdpt.start();
//		while (flag) {
//			// 获取要推的数据
//			try {
//				System.out.println("--------------------  datapool: " + tdpt.getState());
//				if (tdpt.getState() == State.WAITING) {
//					String sendStr = sc.getCometSend().getSendXmlStr();
//					if (sendStr == null || "".equals(sendStr)) {
//						Thread.sleep(2000);
//						continue;
//					}
//					System.out.println("--------------  sendstr ");
//					HttpServletResponse res = sc.getResponse();
//					if (res == null) {
//						System.out.println("-----------: Response is null ");
//						this.flag = false;
//						break;
//					}
//					res.setCharacterEncoding("GBK");
//					PrintWriter writer = res.getWriter();
//					
//					writer.write(sendStr);
//					// writer.flush();
//					// 出错控制
//					if (writer.checkError()) {
//						System.out
//								.println("------------ writer : E R R O R ......");
//						this.flag = false;
//						break;
//					}
//					// 置空下发的数据
//					sc.getCometSend().setSendXmlStr("");
//					synchronized (dpt) {
//						dpt.notify();
//					}
//				} else {
//					Thread.sleep(2000);
//				}
//			} catch (Exception x) {
//				System.out.println("---------------- Exception in sender: ");
//				this.flag = false;
//				x.printStackTrace();
//			}
//		}
//	}
	/**
	 * 客户端第一次连接初始化
	 * @param request
	 * @param response
	 * @return
	 */
//	private boolean senderInit(HttpServletRequest request,HttpServletResponse response){
//		ShareCom sc=ShareComManager.createShareCom(request, response);
//		if(sc==null){
//			return false;
//		}else{
//			try{
//				this.clientId=sc.getCometCommand().getClientId();
//				ShareComManager.addSendMap(clientId, this);
//				this.writer=sc.getResponse().getWriter();
//			}catch(Exception x){
//				System.out.println("---------- sender task init E R R O R......_  ");
//				x.printStackTrace();
//				return false;
//			}
//		}
//		return true;
//	}
}
