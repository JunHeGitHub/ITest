package com.zinglabs.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.base.Interface.BaseInterface;
import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.db.AgentInfo;

import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.db.TTQueue;
import com.zinglabs.log.LogUtil;

public class GenConfAll extends BaseAbs{
	
	public GenConfAll(){
		init(SKIP_Logger);
//		this.doc=doc;
//		this.dao=new DAOTools();
	}
	
//	public static void gen(){
////		boolean isCheck=false;
//		LogUtil.info("GenConfAll 配置开始。。。。。", SKIP_Logger);
//		try {
//			serverName=null;
//			if (ATUtility.isSelfIpMatch(String.valueOf(ConfInfo.proerties.get("ZDesk_dbip")))) {
//				serverName=String.valueOf(ConfInfo.proerties.get("ZDesk_dbip"));
//				localVarMap.put("_MY", "ZDesk");
//				localVarMap.put("_peer_dbip", "ZDesk_route");
//			}else if(ATUtility.isSelfIpMatch(String.valueOf(ConfInfo.proerties.get("ZDesk_route")))){
//				serverName=String.valueOf(ConfInfo.proerties.get("ZDesk_route"));
//				localVarMap.put("_peer_dbip", "ZDesk_dbip");
//				localVarMap.put("_MY", "ZDesk");
//				isRoute=true;
//			}else if(ATUtility.isSelfIpMatch(String.valueOf(ConfInfo.proerties.get("ZQC_dbip")))){
//				serverName=String.valueOf(ConfInfo.proerties.get("ZQC_dbip"));
//				localVarMap.put("_peer_dbip", "ZQC_route");
//				localVarMap.put("_MY", "ZQC");
//			}else if(ATUtility.isSelfIpMatch(String.valueOf(ConfInfo.proerties.get("ZQC_route")))){
//				serverName=String.valueOf(ConfInfo.proerties.get("ZQC_route"));
//				localVarMap.put("_peer_dbip", "ZQC_dbip");
//				localVarMap.put("_MY", "ZQC");
//				isRoute=true;
//			}else if(ATUtility.isSelfIpMatch(String.valueOf(ConfInfo.proerties.get("ZKM_dbip")))){
//				serverName=String.valueOf(ConfInfo.proerties.get("ZKM_dbip"));
//				localVarMap.put("_peer_dbip", "ZKM_route");
//				localVarMap.put("_MY", "ZKM");
//			}else if(ATUtility.isSelfIpMatch(String.valueOf(ConfInfo.proerties.get("ZKM_route")))){
//				serverName=String.valueOf(ConfInfo.proerties.get("ZKM_route"));
//				localVarMap.put("_peer_dbip", "ZKM_dbip");
//				localVarMap.put("_MY", "ZKM");
//				isRoute=true;
//			}else if(ATUtility.isSelfIpMatch(String.valueOf(ConfInfo.proerties.get("ZWM_dbip")))){
//				serverName=String.valueOf(ConfInfo.proerties.get("ZWM_dbip"));
//				localVarMap.put("_peer_dbip", "ZWM_route");
//				localVarMap.put("_MY", "ZWM");
//			}else if(ATUtility.isSelfIpMatch(String.valueOf(ConfInfo.proerties.get("ZWM_route")))){
//				serverName=String.valueOf(ConfInfo.proerties.get("ZWM_route"));
//				localVarMap.put("_peer_dbip", "ZWM_dbip");
//				localVarMap.put("_MY", "ZWM");
//				isRoute=true;
//			}else if(ATUtility.isSelfIpMatch(String.valueOf(ConfInfo.proerties.get("Report_dbip")))){
//				serverName=String.valueOf(ConfInfo.proerties.get("Report_dbip"));
//				localVarMap.put("_peer_dbip", "Report_route");
//				localVarMap.put("_MY", "Report");
//			}else if(ATUtility.isSelfIpMatch(String.valueOf(ConfInfo.proerties.get("Report_route")))){
//				serverName=String.valueOf(ConfInfo.proerties.get("Report_route"));
//				localVarMap.put("_peer_dbip", "Report_dbip");
//				localVarMap.put("_MY", "Report");
//				isRoute=true;
//			}else{
//				LogUtil.info("ip地址匹配失败 ", SKIP_Logger);
//				ATUtility.printSelfIpIpInfo();
//				
//			}
//			
//			if(serverName!=null){
//				LogUtil.info("dbip 验证成功 serverName:"+serverName+" isRoute:"+isRoute, SKIP_Logger);
//				String[] ipArr=serverName.split("\\.");
//				mysqlId=ipArr[0]+ipArr[1]+ipArr[2]+ipArr[3];
//				
//				if(isRoute){
//					localVarMap.put("Master_ZDesk_ip", "ZDesk_route");
//					
//				}else{
//					localVarMap.put("Master_ZDesk_ip", "ZDesk_dbip");
//				}
//				
//				
//				genMysqlConf();
//				genAmoebaConf(serverName);
//				genNginxConf();
//				genLinkAndCP();
//			}
//		} catch (Exception e) {
//			LogUtil.error(e, SKIP_Logger);
//		}
//		LogUtil.info("GenConfAll 配置结束", SKIP_Logger);
//	}
//	
//	
//	public static String getVarValue(String var){
////		if(){}
//		
//		for(Iterator iVar = localVarMap.entrySet().iterator(); iVar.hasNext();) {
//	        java.util.Map.Entry eVar = (java.util.Map.Entry)iVar.next();
//	        String varName =(String)eVar.getKey();
//	        String varValue =(String)eVar.getValue();
//	        if(var.indexOf(varName)!=-1){
//	        	var =var.replaceAll(varName, varValue);
//	        }
//	    }
//		
//		return var;
//	}
//	
//	public static String getDBConfValue(String var){
//		if(ConfInfo.proerties.containsKey(var)){
//			return String.valueOf(ConfInfo.proerties.get(var));
//		}else if(ConfInfo.confMapDBConf.containsKey(var)){
//			return String.valueOf(ConfInfo.confMapDBConf.get(var));
//		}else{
//			LogUtil.error("getDBConfValue no value match value:"+var, SKIP_Logger);
//		}
//		
//		return var;
//	}
//	
//	public static void genVarConfFile(String fileName,String tarFileName) throws Exception{
//		File input = new File(fileName);
//		StringBuffer sb = new StringBuffer();   
////        InputStreamReader is = new InputStreamReader(new FileInputStream(input));   
//        InputStreamReader is = new InputStreamReader(new FileInputStream(input)/*, "utf-8"*/);   
//        BufferedReader br = new BufferedReader(is);   
//        String line = br.readLine();
//        String lineVarTT=null,lineVarTTNew=null;
////        String  
//        while (line != null) {
//        	if(isRoute){
//        		if(line.indexOf("[[[_MY_dbip]]]")!=-1){
//        			line=line.replaceAll("\\[\\[\\[_MY_dbip\\]\\]\\]", "[[[_MY_route]]]");
//        		}
//        	}
//        	
//    		Pattern  p = Pattern.compile("\\[\\[\\[(\\w+)\\]\\]\\]");
//    		Matcher   m = p.matcher(line);  
////    		System.out.print("find"+m.find());
//    		while(m.find()){
//    			lineVarTT=m.group(1);
//    			if(lineVarTT!=null){
//    				LogUtil.debug("var:"+lineVarTT, SKIP_Logger);
//    				lineVarTTNew=getVarValue(lineVarTT);
//    				LogUtil.debug("varValue:"+lineVarTTNew, SKIP_Logger);
//    				lineVarTTNew=getDBConfValue(lineVarTTNew);
//    				LogUtil.debug("DB varValue:"+lineVarTTNew, SKIP_Logger);
//    				
//        			line=line.replaceAll("\\[\\[\\["+lineVarTT+"\\]\\]\\]", lineVarTTNew);
//    			}else{
//    				LogUtil.error("null m group "+line, SKIP_Logger);
//    			}
//    		}
//            sb.append(line);
//            sb.append("\n");
//            line = br.readLine();
//        }
//        br.close();   
//        int fileEndPosT2=0;
//        File fileNew=new File(tarFileName);
//        if(fileNew.exists()){
//        	fileNew.delete();
//        }
//        
//        fileEndPosT2=ATUtility.writeToFileEnd2(tarFileName, sb.toString().getBytes(), fileEndPosT2);
//        
//        
//	}
//	
//	
//	public static void genAmoebaConf(String serverName) throws Exception{
//		String amoebaXml=TTQueue.baseConfPath+"amoeba/amoeba.xml";
//		String dbServerXml=TTQueue.baseConfPath+"amoeba/dbServers.xml";
//		
//		
//		String tarAmoebaFile="/usr/local/amoeba/conf/amoeba.xml";
//		String tardbServerFile="/usr/local/amoeba/conf/dbServers.xml";
//		genVarConfFile(amoebaXml,tarAmoebaFile);
//		genVarConfFile(dbServerXml,tardbServerFile);
//		
//		RunCmd.execCmd2("sh /usr/local/tomcat/webapps/manager/amoeba/restart_amoeba_tomcat6.sh &");
//		
////		
////		File input = new File(amoebaXml);
////		
////		StringBuffer sb = new StringBuffer();   
//////        InputStreamReader is = new InputStreamReader(new FileInputStream(input));   
////        InputStreamReader is = new InputStreamReader(new FileInputStream(input)/*, "utf-8"*/);   
////        BufferedReader br = new BufferedReader(is);   
////        String line = br.readLine();
////        String lineVarTT=null,lineVarTTNew=null;
//////        String  
////        while (line != null) {
////        	
////    		Pattern  p = Pattern.compile("\\[\\[\\[(\\w+)\\]\\]\\]");
////    		Matcher   m = p.matcher(line);  
//////    		System.out.print("find"+m.find());
////    		while(m.find()){
////    			lineVarTT=m.group(1);
////    			if(lineVarTT!=null){
////    				LogUtil.debug("var:"+lineVarTT, SKIP_Logger);
////    				lineVarTTNew=getVarValue(lineVarTT);
////    				LogUtil.debug("varValue:"+lineVarTTNew, SKIP_Logger);
////    				lineVarTTNew=getDBConfValue(lineVarTTNew);
////    				LogUtil.debug("DB varValue:"+lineVarTTNew, SKIP_Logger);
////    				
////        			line=line.replaceAll("\\[\\[\\["+lineVarTT+"\\]\\]\\]", lineVarTTNew);
////    			}else{
////    				LogUtil.error("null m group "+line, SKIP_Logger);
////    			}
////    		}
////            sb.append(line);
////            sb.append("\n");
////            line = br.readLine();
////        }
////        br.close();   
////        int fileEndPosT2=0;
////        fileEndPosT2=ATUtility.writeToFileEnd2(amoebaXml+".NEW", sb.toString().getBytes(), fileEndPosT2);
//        
//        
//	}
//
//	public static boolean genMysqlConf() throws Exception{
//		LogUtil.info("genMysqlConf开始", SKIP_Logger);
//		
//		
////		File input = new File(TTQueue.baseConfPath+"mysql/my.cnf");
////		
////		StringBuffer sb = new StringBuffer();   
//////        InputStreamReader is = new InputStreamReader(new FileInputStream(input));   
////        InputStreamReader is = new InputStreamReader(new FileInputStream(input)/*, "utf-8"*/);   
////        BufferedReader br = new BufferedReader(is);   
////        String line = br.readLine();   
////        ArrayList<String> alLine=new ArrayList<String>();
////        while (line != null) {
////            line = br.readLine();
////            
////            
////            sb.append(line);
////        }
////        br.close();   
////		
////		
////		
////		int fileEndPosT2=0;
////		
////		
////		fileEndPosT2=ATUtility.writeToFileEnd2("d:\\dictprofes.csv", "".getBytes(), fileEndPosT2);
////		
//		
//		LogUtil.info("genMysqlConf结束", SKIP_Logger);
//		return true;
//	}
//	
//	public static void genNginxConf(){
//		
//	}
//	
//	public static void genLinkAndCP(){
//		
//	}
//	
//	/**
//	 * 
//	 * @author 	
//	 * @function 	
//	 * @description 通过  <database dbip="192.168.0.210" dbo=""  route="192.168.0.211" id="openfire" name="openfire" password="12" port="3306" type="mysql" userName="zinglabs"/>
//	 * 支持集群同步好友   	各openfire服务器此配置保持一致。
//	 * @example  	
//	 * @requires
//	 */
//	public static void sysnAgent(){
//		
//		String sql="select DISTINCT  phone_number,group_name   from DA_SGINFO ";
//		
//		int id=100;
//		ArrayList<Object[]> al=null,al2=null;
//		Object[] resTmp=null,resTmp2=null,resTmp3=null;
////		al=dao.execSelect(sql, "z3000IM");
//		ArrayList<String> sqlExec=new ArrayList<String>();
//		String sgName=null;		
////		sql="select phone_number from SU_CALLCENTER_PHONE_USER";
//		sql="select `phone_number`  from `suSecurityUser` ";
//		String sqlTmp=null;
//		System.out.println("CONFIG_FILE_PATH:"+BaseInterface.CONFIG_FILE_PATH);
//		String openFireIp=ConfInfo.proerties.getProperty("openfire_dbip");
//		String routeIp=ConfInfo.proerties.getProperty("openfire_route");
//		al2=DAOTools.execSelectS(sql, "ZDesk");
//		boolean hasLoad=false;
//		for(int i=0 ;i<al2.size();i++){
//			hasLoad=false;
//			resTmp=al2.get(i);
//			if(resTmp!=null && resTmp[0]!=null && String.valueOf(resTmp[0]).length()>0){
//				
////				sql="select DISTINCT  phone_number,group_name   from DA_SGINFO where phone_number='"+resTmp[0]+"'";
////				al=DAOTools.execSelectS(sql, "z3000IM");
////				
////				if(al.size()>0){
////					resTmp2=al.get(0);
////					sgName=String.valueOf(resTmp2[1]);
////				}else{
////					sgName="XIAOSHOU";
////				}
//				
//				sgName="XIAOSHOU";
//				for(int k=0;k<al2.size();k++){
//					resTmp3=al2.get(k);
//					
//					if(resTmp3[0]==null || String.valueOf(resTmp3[0]).length()==0){
//						continue;
//					}
//					
//					sqlTmp="INSERT INTO `ofRoster` (`rosterID`, `username`, `jid`, `sub`, `ask`, `recv`, `nick`) VALUES ("+id+", '"+resTmp[0]+"', '"+resTmp3[0]+"@"+openFireIp+"', 3, -1, -1, '"+resTmp3[0]+"')";
//					sqlExec.add(sqlTmp);
//					sqlTmp="INSERT INTO `ofRosterGroups` (`rosterID`, `rank`, `groupName`) VALUES ("+id+", 0, '"+sgName+"')";
//					sqlExec.add(sqlTmp);
//					id++;
//					
////					sqlTmp="INSERT INTO `ofRoster` (`rosterID`, `username`, `jid`, `sub`, `ask`, `recv`, `nick`) VALUES ("+id+", '"+resTmp3[0]+"', '"+resTmp2[0]+"@192.168.0.174', 3, -1, -1, '"+resTmp2[0]+"')";
////					sqlExec.add(sqlTmp);
////					sqlTmp="INSERT INTO `ofRosterGroups` (`rosterID`, `rank`, `groupName`) VALUES ("+id+", 0, '"+sgName+"')";
////					sqlExec.add(sqlTmp);
////					id++;
//				}
//			}
//		}
//		
//		
//		sql="delete from ofRoster";
//		DAOTools.execUpdateS(sql, "openfire");
//		sql="delete from ofRosterGroups";
//		DAOTools.execUpdateS(sql, "openfire");
//		try {
//			DAOTools.execBatchS(sqlExec, "openfire",false);
//		} catch (Exception e) {
//			LogUtil.error(e, SKIP_Logger);
////			e.printStackTrace();
//			// TODO: handle exception
//		}
//		
//		LogUtil.error("openFireIp:"+openFireIp+" routeIp:"+routeIp, SKIP_Logger);
//		
//		if(openFireIp!=null && routeIp!=null && routeIp.length()>0){
////			ConfInfo.proerties.getProperty("openfire_type");
//			
//			String typeName="DY_openfireRoute";
//			
////			ConfInfo.proertiesDy.put(type+"_userName", pBase.getProperty("userName"));
////			ConfInfo.proertiesDy.put(type+"_password",passwordTT);
////			ConfInfo.proertiesDy.put(type+"_dbip",pBase.getProperty("dbip"));
////			ConfInfo.proertiesDy.put(type+"_name",pBase.getProperty("name"));
////			ConfInfo.proertiesDy.put(type+"_port",pBase.getProperty("port"));
////			ConfInfo.proertiesDy.put(type+"_type",pBase.getProperty("type"));
//			
//			DAOTools.insertDYInfo(ConfInfo.proerties.getProperty("openfire_userName"), ConfInfo.proerties.getProperty("openfire_password"), routeIp, ConfInfo.proerties.getProperty("openfire_name"), ConfInfo.proerties.getProperty("openfire_port"), ConfInfo.proerties.getProperty("openfire_type"),typeName);
////			(String user,String password,String ip,String dbName,String port,String dbType,String type)
//			
//			sql="delete from ofRoster";
//			DAOTools.execUpdateS(sql, typeName);
//			sql="delete from ofRosterGroups";
//			DAOTools.execUpdateS(sql, typeName);
//			try {
//				DAOTools.execBatchS(sqlExec, typeName,false);
//			} catch (Exception e) {
//				LogUtil.error(e, SKIP_Logger);
////				e.printStackTrace();
//				// TODO: handle exception
//			}
////			ip = proertiesDy.getProperty(type+"_ip");
////			dbName = proertiesDy.getProperty(type+"_name");
////			port = proertiesDy.getProperty(type+"_port");
////			dbType=proertiesDy.getProperty(type+"_type");
////			if(proertiesDy.containsKey(type+"_charset")){
////				charset=proertiesDy.getProperty(type+"_charset");
////			}else{
////				charset=conCode;
//		}
//		
////		for(int i=0;i<sqlExec.size();i++){
////			debug(i+" sql:"+sqlExec.get(i));
////		}
//	}
//	
//	public static void main(String[] args) {
////		try {
////			ConfInfo.parseConf();
////		} catch (Exception e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		GenConfAll db=new GenConfAll();
////		db.sysnAgent();
//		
////		String ip="22.0.1.123";
////		String[] ipArr=ip.split("\\.");
//////		System.out.println(ip.substring(ip.lastIndexOf(".")+1));
////		System.out.println(ipArr[2]+ipArr[3]);
////		String jsonTmp="<property name=\"port\">[[_MY_port1]]</property><property name=\"port\">[[#MY_port2]]</property><property name=\"port\">[[_MY_port3]]</property>";
////		Pattern  p = Pattern.compile("\\[\\[(\\w+)\\]\\]");
////		Matcher   m = p.matcher(jsonTmp);  
//////		System.out.print("find"+m.find());
////		while(m.find()){
////			System.out.println(m.group(1));
////		}
//		
////		 String tmpStr="<property name=\"port\">aaa=$MY_port1</property>cookie:lang=zh_CN; Path=/mm_lang=zh_CN; Domain=.qq.com; Path=/wxloadtime=1366641148; Domain=.qq.com; Path=/wxsid=/25hBU9i89Z0N82A; Domain=.qq.com; Path=/wxuin=930404083; Domain=.qq.com; Path=/ cookie:lang=zh_CN; Path=/mm_lang=zh_CN; Domain=.qq.com; Path=/wxloadtime=1366641148; Domain=.qq.com; Path=/wxsid=/25hBU9i89Z0N82A; Domain=.qq.com; Path=/wxuin=930404083; Domain=.qq.com; Path=/";
////		 Pattern  p = Pattern.compile("aaa=(\\w+);");   
////		 Matcher   m = p.matcher(tmpStr);   
////	        if(m.find())   
////	        {   
////	            System.out.println( m.group(1));   
////	        }   
//		
//		localVarMap.put("_MY", "ZDesk");
//		
////		String amoebaXml=TTQueue.baseConfPath+"amoeba/amoeba.xml";
//		String amoebaXml="c://amoeba/amoeba.xml";
////		String dbServerXml=TTQueue.baseConfPath+"amoeba/dbServers.xml";
//		File input = new File(amoebaXml);
//		
//		StringBuffer sb = new StringBuffer();   
////        InputStreamReader is = new InputStreamReader(new FileInputStream(input));   
//        InputStreamReader is;
//		try {
//			is = new InputStreamReader(new FileInputStream(input)/*, "utf-8"*/);
//			 BufferedReader br = new BufferedReader(is);   
//		        String line = br.readLine();
//		        String lineVarTT=null,lineVarTTNew=null;
////		        String  
//		        while (line != null) {
//		        	if(line.indexOf("[[[")!=-1){
//		        		System.out.println("");
//		        	}
//		    		Pattern  p = Pattern.compile("\\[\\[\\[(\\w+)\\]\\]\\]");
//		    		Matcher   m = p.matcher(line);  
////		    		System.out.print("find"+m.find());
//		    		while(m.find()){
//		    			lineVarTT=m.group(1);
//		    			if(lineVarTT!=null){
//		    				lineVarTTNew=getVarValue(lineVarTT);
//		        			line=line.replaceAll("\\[\\[\\["+lineVarTT+"\\]\\]\\]", lineVarTTNew);
//		    			}else{
//		    				LogUtil.error("null m group "+line, SKIP_Logger);
//		    			}
//		    		}
//		            sb.append(line);
//		            sb.append("\n");
//		            line = br.readLine();
//		        }
//		        br.close();   
//		        int fileEndPosT2=0;
//		        fileEndPosT2=ATUtility.writeToFileEnd2(amoebaXml+".NEW", sb.toString().getBytes(), fileEndPosT2);
//		        
//		} catch (Exception e) {
//			LogUtil.error(e, SKIP_Logger);
//		}   
//       
//        
//		
		
//	}
	
	
	public static HashMap<String, String> localVarMap=new HashMap<String, String>();
	
	public static String serverName;
	
	public static String mysqlId;
	
	public Timestamp initDay;
	
	public static boolean isRoute=false;
	
//	public DAOTools dao;
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("AT");
	
}

