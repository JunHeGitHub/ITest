package com.zinglabs.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.zinglabs.base.absImpl.BaseAbs;



public class CTIRequest extends BaseAbs {
	
	public CTIRequest(String ip){
		init(SKIP_Logger);
		this.ip=ip;
	}
	
	public int getChannelStatus(String phone,String srn, String channel) {
		int ret=-1;
		reqXml=new StringBuffer();
		setActionName("get_channel_status");
		setParam("number", phone);
		setParam("password", SSC_DEFULT_AGENT_PASS);
		setParam("srn", srn);
		setParam("channel", channel);
		endReq();
		String stateStr=doAction();
		int pos;
		if(stateStr!=null&&(pos=stateStr.indexOf(SSC_STATUS_STR))>0){
			ret=Integer.parseInt(stateStr.substring(pos+SSC_STATUS_STR.length(), 
					pos+SSC_STATUS_STR.length()+2));
		}
		if(ret<0&&(pos=stateStr.indexOf(SSC_STATUS_ERROR_STR))>0){
			ret=Integer.parseInt(stateStr.substring(pos+SSC_STATUS_ERROR_STR.length(), 
					pos+SSC_STATUS_ERROR_STR.length()+2));
			if(ret>0){
				ret=-ret;
			}
		}
		return ret;
	}
	
	public  String getOneChannelInfo(String phone,String srn, String channel){
		String ret=null;
		reqXml=new StringBuffer();
		setActionName("get_one_channel_info1");
		setParam("number", phone);
		setParam("password", SSC_DEFULT_AGENT_PASS);
		setParam("srn", srn);
		setParam("channel", channel);
		endReq();
		ret=doAction();
//		ret=stateStr.split("\n");
//		System.out.print(stateStr);
		
//		System.out.print(ret.length+"==================");
		return ret;
	}
	
	public  String[] getAgentInfo(String phone){
		String[] ret=null;
		reqXml=new StringBuffer();
		setActionName("get_agent_info");
		setParam("number", phone);
		setParam("password", SSC_DEFULT_AGENT_PASS);
		endReq();
		String stateStr=doAction();
		int pos;
		if(stateStr!=null&&(pos=stateStr.indexOf(SSC_STATUS_AGENT_VALUE_STR))>0){
			int end = stateStr.indexOf(SSC_STATUS_AGENT_VALUE_END_STR);
			stateStr=stateStr.substring(pos+SSC_STATUS_AGENT_VALUE_STR.length(),end);
		}
		ret=stateStr.split(":");
		return ret;
	}
	
	public  String[] getAllAgentStatus(String phone){
		String[] ret=null;
		reqXml=new StringBuffer();
		setActionName("get_all_agent_status");
		setParam("number", phone);
		setParam("password", SSC_DEFULT_AGENT_PASS);
		endReq();
		String stateStr=doAction();
		int pos;
		if(stateStr!=null&&(pos=stateStr.indexOf(SSC_STATUS_AGENT_VALUE_STR))>0){
			int end = stateStr.indexOf(SSC_STATUS_AGENT_VALUE_END_STR);
			stateStr=stateStr.substring(pos+SSC_STATUS_AGENT_VALUE_STR.length(),end);
		}
		ret=stateStr.split("\n");
		return ret;
	}
	
	
	public  String[] getAllSrnStatus(String phone){
		String[] ret=null;
		reqXml=new StringBuffer();
		setActionName("get_all_srn_status");
		setParam("number", phone);
		setParam("password", SSC_DEFULT_AGENT_PASS);
		endReq();
		String stateStr=doAction();
		int pos;
		if(stateStr!=null&&(pos=stateStr.indexOf(SSC_STATUS_AGENT_VALUE_STR))>0){
			int end = stateStr.indexOf(SSC_STATUS_AGENT_VALUE_END_STR);
			stateStr=stateStr.substring(pos+SSC_STATUS_AGENT_VALUE_STR.length(),end);
		}
		ret=stateStr.split("\n");
		return ret;
	}
	
//	 * <ZTAPI> 
//	 *	<REQ name="login">
//	 *	<PARAMETER name="number" value="501" />
//	 *	<PARAMETER name="password" value="heeee" />
//	 *	<PARAMETER name="role" value="agent/monitor" /> 
//	 *	</REQ>
//	 * </ZTAPI>
	
	public  String[] login(){
		String[] ret=null;
		reqXml=new StringBuffer();
		setActionName("login");
		setParam("number", "3694");
		setParam("password", "3694");
		setParam("role", "monitor");
		endReq();
		String stateStr=doAction();
		int pos;
		if(stateStr!=null&&(pos=stateStr.indexOf(SSC_STATUS_AGENT_VALUE_STR))>0){
			int end = stateStr.indexOf(SSC_STATUS_AGENT_VALUE_END_STR);
			stateStr=stateStr.substring(pos+SSC_STATUS_AGENT_VALUE_STR.length(),end);
		}
		ret=stateStr.split("\n");
		return ret;
	}
	
	public  String getAllSkillGroupStatus(String phone){
		reqXml=new StringBuffer();
		setActionName("get_all_skillgroup_status");
		setParam("number", phone);
		setParam("password", SSC_DEFULT_AGENT_PASS);
		endReq();
		String stateStr=doAction();
		int pos;
		if(stateStr!=null&&(pos=stateStr.indexOf(SSC_STATUS_AGENT_VALUE_STR))>0){
			int end = stateStr.indexOf(SSC_STATUS_AGENT_VALUE_END_STR);
			stateStr=stateStr.substring(pos+SSC_STATUS_AGENT_VALUE_STR.length(),end);
		}
		return stateStr;
	}
	
	
	/**
	 * req
	 * <ZTAPI> 
	 *	<REQ name="get_skillgroup_queue_status">
	 *	<PARAMETER name="number" value="501" />
	 *	<PARAMETER name="password" value="501" />
	 *	<PARAMETER name="sgname" value="sg1/null" />
	 *	<PARAMETER name="period" value="10" />
	 *	<PARAMETER name="num" value="6" />
	 *	</REQ>
	 * </ZTAPI>
	 *
	 * res 
	 * <ZTAPI> 
	 *	<RES errcode="0">
	 *	<PARAMETER name="wait1" value="20" />
	 *	<PARAMETER name="wait2" value="2" />
	 *	<PARAMETER name="wait3" value="0" />
	 *	<PARAMETER name="wait4" value="33" />
	 *	<PARAMETER name="wait5" value="1" />
	 *	<PARAMETER name="wait6" value="6" />
	 *	</RES>
	 * </ZTAPI>
	 */
	
	public  String getSkillGroupQueueStatus(String phone,String sgName,String period,String num){
		reqXml=new StringBuffer();
		setActionName("get_skillgroup_queue_status");
		setParam("number", phone);
//		setParam("password", SSC_DEFULT_AGENT_PASS);
//		setParam("number", "3190");
//		setParam("password", "3190");
		setParam("password", SSC_DEFULT_AGENT_PASS);
		setParam("sgname", sgName);
		setParam("period", period);
		setParam("num", num);
		endReq();
		String stateStr=doAction();
		int pos;
		if(stateStr!=null&&(pos=stateStr.indexOf(SSC_STATUS_AGENT_VALUE_STR))>0){
			int end = stateStr.indexOf(SSC_STATUS_AGENT_VALUE_END_STR);
			stateStr=stateStr.substring(pos+SSC_STATUS_AGENT_VALUE_STR.length(),end);
		}
		return stateStr;
	}
	
	public  String[] getAllAgentInfoToDB(String phone){
		String[] ret=null;
		reqXml=new StringBuffer();
		setActionName("get_all_agent_info_todb");
		setParam("number", phone);
		setParam("password", SSC_DEFULT_AGENT_PASS);
		endReq();
		String stateStr=doAction();
		int pos;
		if(stateStr!=null&&(pos=stateStr.indexOf(SSC_STATUS_AGENT_VALUE_STR))>0){
			int end = stateStr.indexOf(SSC_STATUS_AGENT_VALUE_END_STR);
			stateStr=stateStr.substring(pos+SSC_STATUS_AGENT_VALUE_STR.length(),end);
		}
//		ret=stateStr.split("\n");
		return ret;
	}
	
	
	public  String[] getAllMonitorInfoToDB(String phone){
		String[] ret=null;
		reqXml=new StringBuffer();
		setActionName("get_all_monitor_info_todb");
		setParam("number", phone);
		setParam("password", SSC_DEFULT_AGENT_PASS);
		endReq();
		String stateStr=doAction();
		int pos;
		if(stateStr!=null&&(pos=stateStr.indexOf(SSC_STATUS_AGENT_VALUE_STR))>0){
			int end = stateStr.indexOf(SSC_STATUS_AGENT_VALUE_END_STR);
			stateStr=stateStr.substring(pos+SSC_STATUS_AGENT_VALUE_STR.length(),end);
		}
//		ret=stateStr.split("\n");
		return ret;
	}
	
	public  String[] getAllColineInfoToDB(String phone){
		String[] ret=null;
		reqXml=new StringBuffer();
		setActionName("get_monitor_co_todb");
		setParam("number", phone);
		setParam("password", SSC_DEFULT_AGENT_PASS);
		endReq();
		String stateStr=doAction();
		int pos;
		if(stateStr!=null&&(pos=stateStr.indexOf(SSC_STATUS_AGENT_VALUE_STR))>0){
			int end = stateStr.indexOf(SSC_STATUS_AGENT_VALUE_END_STR);
			stateStr=stateStr.substring(pos+SSC_STATUS_AGENT_VALUE_STR.length(),end);
		}
//		ret=stateStr.split("\n");
		return ret;
	}
	
	
	public  String getCallInfo(String phone){
		reqXml=new StringBuffer();
		setActionName("call_info");
//		setParam("number", "3689");
//		setParam("password", "3689");
		setParam("number", phone);
		setParam("password", SSC_DEFULT_AGENT_PASS);
		endReq();
		String stateStr=doAction();
		int pos;
		if(stateStr!=null&&(pos=stateStr.indexOf(SSC_STATUS_AGENT_VALUE_STR))>0){
			int end = stateStr.indexOf(SSC_STATUS_AGENT_VALUE_END_STR);
			stateStr=stateStr.substring(pos+SSC_STATUS_AGENT_VALUE_STR.length(),end);
		}
//		ret=stateStr.split("\n");
		return stateStr;
	}
	
	public  String[] getAllChannelStatus(String phone){
		String[] ret=null;
		reqXml=new StringBuffer();
		setActionName("get_all_channel_status");
		setParam("number", phone);
		setParam("password", SSC_DEFULT_AGENT_PASS);
		endReq();
		String stateStr=doAction();
		int pos;
		if(stateStr!=null&&(pos=stateStr.indexOf(SSC_STATUS_AGENT_VALUE_STR))>0){
			int end = stateStr.indexOf(SSC_STATUS_AGENT_VALUE_END_STR);
			stateStr=stateStr.substring(pos+SSC_STATUS_AGENT_VALUE_STR.length(),end);
		}
		ret=stateStr.split(":");
		return ret;
	}
	
	public  void setActionName(String name){
		reqXml.append("<?xml version=\"1.0\" encoding='gb2312'?>");
		reqXml.append("<ZTAPI>\n");
		reqXml.append("<REQ name=\"");
		reqXml.append(name);
		reqXml.append( "\">\n");
	}
	
	public  void setParam(String name,String value){
		reqXml.append("<PARAMETER name=\"");
		reqXml.append(name);
		reqXml.append("\"");
		reqXml.append(" value=\"");
		reqXml.append(value);
		reqXml.append("\" />\n");
	}

	public  void endReq(){
		reqXml.append("</REQ>\n");
		reqXml.append("</ZTAPI>\n");
		int le=reqXml.toString().getBytes().length;
		reqXml.insert(0, "\r\n\r\n");
		reqXml.insert(0, le);
		reqXml.insert(0, "HTTP/1.1 200 OK\r\nContent-Length:");
	}
	
	
	public  String doAction()
	{
		String ret=null;
		Socket socket = null;
		PrintWriter out=null;
		InputStream in =null;
			try {
//				debug(ip+"----------"+SSC_CTI_COMMAND_PORT);
				socket = new Socket(ip, SSC_CTI_COMMAND_PORT);
				socket.setSoTimeout(SSC_SOCKET_TIME_OUT);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.print(reqXml.toString());
				out.flush();
				in=socket.getInputStream();
				byte[] buf = new byte[MAX_CTI_COM_REQ_LEN];
				int len=0;
				int readTimes=0;
				while (true) {
					len = in.read(buf);
					if (len > 0) {
						ret = new String(buf, 0, len);
					} else if (len == 0) {
						warn("Zero info Read");
						continue;
					} else if (len < 0) { // -1 means read the end of stream
						throw new IOException(reqXml.toString()+"\n Socket closed in doAction");
					}
					if((ret=GetXMLBody(ret))!=null&&ret.length()>0){
						break;
					}
					if(++readTimes>3){
						break;
					}
					warn(ret);
				}
			} catch (UnknownHostException e) {
				error("during doAction1");
				error(e);
			} catch (IOException e) {
				error("during doAction");
				error(e);
			}finally{
				if(socket!=null){
					try {
						socket.close();
					} catch (IOException e) {
						error("socket.getInputStream close in doAction");
						error(e);
					}
					socket=null;
				}
			}
		return ret;
	}
	
	public  String GetXMLBody(String strReq) {
		String strRet = null;
		try {
			if (null == strReq)
				return null;
			int pos = strReq.indexOf("HTTP/1.1 200 OK");
			if (pos < 0) {
				return null;
			}
			strReq = strReq.substring(pos);
			int pos1 = strReq.indexOf("Content-Length:");
			int pos2 = strReq.indexOf("\r\n\r\n", pos1);
			if (pos1 < 0 || pos2 < 0 || pos2 < pos1)
				return null;
			String str = strReq.substring(pos1 + "Content-Length:".length(),
					pos2);
			if (null == str)
				return null;
			int len = Integer.parseInt(str.trim());
			if (len > 0	&& strReq.getBytes().length - (pos2 + "\r\n\r\n".length()) >= len) {
				byte[] bTemp = strReq.substring(pos2 + "\r\n\r\n".length())
						.getBytes();
				strRet = new String(bTemp, 0, len, "GBK");
			}
		} catch (Exception e) {
			error("during GetXMLBody()\n" + strRet);
			error(e);
			return null;
		}
		return strRet;
	}
	
	public static void main(String[] args){
//		getOneChannelInfo("5001","10.169.232.215","64");
//		String[] ret=getAgentInfo("10");
//		String[] ret=getAllSrnStatus("10");
		Document doc=null;
		NodeList nl = null;
		String name=null ,value=null;
		int attrLen=0;
//		
//		String[] ret=getAllAgentStatus("10");
//		String[] ret=getAllAgentInfoToDB("10");
		CTIRequest cti=new CTIRequest("192.168.0.30");
		String ret=cti.getCallInfo("10");
//		String ret=cti.getSkillGroupQueueStatus("3190","g1","5","6");
//		String[] ret=cti.getAllAgentInfoToDB("10");
//		StringBuffer sb=new StringBuffer();
//		sb.append("<?xml version=\"1.0\" encoding='gb2312'?>");
//		sb.append("<ZTAPI>");
//		sb.append("<RES errcode=\"0\">");
//		sb.append("<PARAMETER name=\"coline_inbound_ivr\" value=\"0\" />");
//		sb.append("<PARAMETER name=\"coline_inbound_connected\" value=\"0\" />");
//		sb.append("<PARAMETER name=\"coline_inbound\" value=\"0\" />");
//		sb.append("<PARAMETER name=\"coline_outbound\" value=\"0\" />");
//		sb.append("<PARAMETER name=\"agent_count\" value=\"0\" />");
//		sb.append("<PARAMETER name=\"agent_connected\" value=\"0\" />");
//		sb.append("</RES>");
//		sb.append("</ZTAPI>");
//		String[] ret=cti.getAgentInfo("10");
//		String[] ret=cti.login();
//		String ret=cti.getOneChannelInfo("10","192.168.0.26","111");
		ret=ret.substring(ret.indexOf("<ZTAPI>"),ret.indexOf("</ZTAPI>")+8);
		System.out.print(ret);
		doc = DOMTool.loadDocumentFromStr(ret);
		
		System.out.print(doc.getXmlVersion());
		nl = DOMTool.getMultiNodes(doc, "PARAMETER");
//		LogUtil.warn(nl.getLength()+"-------------------------");
		if(attrLen==0){
			attrLen=nl.getLength();
		}
		
		

		System.out.print("3544".indexOf("3613"));
//		for(int i=0;i<ttt.length;i++){
//			System.out.print(ttt[i]+"\n");
//		}
		
	}
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
	private StringBuffer reqXml;
	
	public  String ip="192.168.0.30";
	
//	public  String ip="";
}
