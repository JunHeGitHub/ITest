package com.zinglabs.util.flowUtil;
import java.util.*;
import java.io.*;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.log.LogUtil;
import com.zinglabs.util.ZKMConfs;
import com.zinglabs.util.flowUtil.DisponseBeans.DisponseBase;

public class FlowHelper extends DisponseBase{
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	public static String definedFile=ZKMConfs.confs.getProperty("commonflowDefineFile","/usr/local/tomcat/ZDesk/WebRoot/js/DWConf.js");
	public static String flowDisponseClass=ZKMConfs.confs.getProperty("zkmFlowDisponseClass","com.zinglabs.util.flowUtil.FlowDisponseImp.ZKMFlowDisponse");
	public static String flowDefineStr;
	public static Map commonFlowDefine;
	
	static{
		flowDefineStr="";
		commonFlowDefine=new HashMap();
		initFlows();
	}
	
	public static void initFlows(){
		Reader in =null;
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		try{
			in = new InputStreamReader(new FileInputStream(definedFile));
			engine.eval(in);
			Invocable invocable = (Invocable) engine;
			flowDefineStr=(String)invocable.invokeFunction("F_getCommonFlowDefineStr");
			JSONObject obj = JSONObject.fromObject(flowDefineStr);
			commonFlowDefine=(Map)JSONObject.toBean(obj,HashMap.class);
		}catch(Exception x){
			LogUtil.error("------ flow init error :" ,SKIP_Logger);
			LogUtil.error(x,SKIP_Logger);
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static boolean isStart(String type,String node) throws Exception{
		MorphDynaBean mb=(MorphDynaBean)commonFlowDefine.get(type);
		String start=(String)mb.get("start");
		if(start.equals(node)){
			return true;
		}
		return false;
	}
	
	public static Map getFlow(String type){
		if(type!=null && type.length()>0){
			try{
				MorphDynaBean mb=(MorphDynaBean)commonFlowDefine.get(type);
				return convert2Map(mb);
			}catch(Exception x){
				LogUtil.error(x,SKIP_Logger);
			}
		}
		return null;
	}
	
	public static boolean isEnd(String type,String node) throws Exception {
		MorphDynaBean mb=(MorphDynaBean)commonFlowDefine.get(type);
		String end=(String)mb.get("end");
		if(end.equals(node)){
			return true;
		}
		return false;
	}
	
	public static Map getStart(String type) throws Exception{
		MorphDynaBean mb=(MorphDynaBean)commonFlowDefine.get(type);
		if(mb!=null){
			String start=(String)mb.get("start");
			if(start!=null && start.length()>0){
				MorphDynaBean nodes=(MorphDynaBean)mb.get("nodes");
				if(nodes!=null){
					MorphDynaBean map=(MorphDynaBean)nodes.get(start);
					return convert2Map(map);
				}
			}
		}
		return null;
		
	}
	
	public static Map getEnd(String type) throws Exception{
		MorphDynaBean mb=(MorphDynaBean)commonFlowDefine.get(type);
		if(mb!=null){
			String end=(String)mb.get("end");
			if(end!=null && end.length()>0){
				MorphDynaBean nodes=(MorphDynaBean)mb.get("nodes");
				if(nodes!=null){
					MorphDynaBean map=(MorphDynaBean)nodes.get(end);
					return convert2Map(map);
				}
			}
		}
		return null;
		
	}
	
	public static Map getNext(String type,String node) throws Exception{
		MorphDynaBean mb=(MorphDynaBean)commonFlowDefine.get(type);
		if(mb!=null){
			MorphDynaBean nodes=(MorphDynaBean)mb.get("nodes");
			if(nodes!=null){
				MorphDynaBean nodeNow=(MorphDynaBean)nodes.get(node);
				String next=nodeNow.get("next")==null?"":(String)nodeNow.get("next");
				if(next!=null && next.length()>0){
					MorphDynaBean map=(MorphDynaBean)nodes.get(next);
					return convert2Map(map);
				}
			}
		}
		return null;
	}
	
	public static Map getNode(String type,String node){
		Map rm=null;
		try{
			MorphDynaBean mb=getNode_MorphDynaBean(type,node);
			if(mb!=null){
				rm=convert2Map(mb);
			}
		}catch(Exception x){
			LogUtil.error(x,SKIP_Logger);
		}
		return rm;
	}
	
	public static MorphDynaBean getNode_MorphDynaBean(String type,String node) throws Exception{
		MorphDynaBean mb=(MorphDynaBean)commonFlowDefine.get(type);
		if(mb!=null){
			MorphDynaBean nodes=(MorphDynaBean)mb.get("nodes");
			if(nodes!=null){
				MorphDynaBean nodeNow=(MorphDynaBean)nodes.get(node);
				return nodeNow;
			}
		}
		return null;
	}
	
	public static Map getPrevious(String type,String node) throws Exception{
		MorphDynaBean mb=(MorphDynaBean)commonFlowDefine.get(type);
		if(mb!=null){
			MorphDynaBean nodes=(MorphDynaBean)mb.get("nodes");
			if(nodes!=null){
				MorphDynaBean nodeNow=(MorphDynaBean)nodes.get(node);
				Object previous=nodeNow.get("previous")==null?"":nodeNow.get("previous");
				if(nodeNow.get("previous")!=null){
					MorphDynaBean map=(MorphDynaBean)nodes.get((String)previous);
					return convert2Map(map);
				}
			}
		}
		return null;
	}
	
	public static Map convert2Map(MorphDynaBean map) throws Exception{
		Map remap=null;
		if(map!=null){
			String str=JSONSerializer.toJSON(map).toString();
			JSONObject obj=JSONObject.fromObject(str);
			if(str!=null && str.length()>0){
				remap=(Map)JSONObject.toBean(obj,HashMap.class);
			}
		}
		return remap;
	}
	
	public static FlowDisponse getFlowDisponse(){
		FlowDisponse fd=null;
		try{
			Object obj=Class.forName(flowDisponseClass).newInstance();
			if(obj!=null && obj instanceof FlowDisponse){
				fd=(FlowDisponse)obj;
			}
		}catch(Exception x){
			LogUtil.error(x,SKIP_Logger);
		}
		return fd;
	}
	
	public static FlowDisponse getFlowDisponse(String key){
		FlowDisponse fd=null;
		try{
			String classa=ZKMConfs.confs.getProperty(key);
			if(classa!=null && classa.length()>0){
				Object obj=Class.forName(classa).newInstance();
				if(obj!=null && obj instanceof FlowDisponse){
					fd=(FlowDisponse)obj;
				}
			}
		}catch(Exception x){
			LogUtil.error(x,SKIP_Logger);
		}
		return fd;
	}
	
	public static List<Map> getSecurityAllNode(String stype){
		List<Map> rlist=new ArrayList();
		if(stype!=null && stype.length()>0){
			Iterator it=commonFlowDefine.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry e = (Map.Entry)it.next();
				String key=(String)e.getKey();
				rlist=getSecurityFlowType(key,stype);
			}
		}
		return rlist;
	}
	
	public static List<Map> getSecurityFlowType(String flowType,String stype){
		List<Map> rlist=new ArrayList();
		if(flowType.length()>0 && stype.length()>0){
			Map nodes=getFlowNodes(flowType);
			Iterator it=nodes.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry e = (Map.Entry)it.next();
				String key=(String)e.getKey();
				if(chekcSecurityFlowNode(flowType,key,stype)){
					rlist.add(getNode(flowType, key));
				}
			}
		}
		return rlist;
	}
	
	public static boolean chekcSecurityFlowNode(String flowType,String node,String stype){
		if(flowType!=null&& flowType.length()>0 && node!=null && node.length()>0 && stype!=null && stype.length()>0){
			Map n=getNode(flowType, node);
			if(n!=null){
				String st=n.get("securityType")==null?"":(String)n.get("securityType");
				if(stype.equals(st)){
					return true;
				}
			}
		}
		return false;
	}
	
	public static Map getFlowNodes(String flowType){
		Map rm=null;
		if(flowType.length()>0){
			MorphDynaBean flow=(MorphDynaBean)commonFlowDefine.get(flowType);
			MorphDynaBean nodes=(MorphDynaBean)flow.get("nodes");
			try{
				rm=convert2Map(nodes);
			}catch(Exception x){
				LogUtil.error(x,SKIP_Logger);
			}
		}
		return rm;
	}
	
	public static void main(String[] args){
		//FlowHelper fh=new FlowHelper();
		/*try{
			boolean map=FlowHelper.isStart("zkmUsuallyFlow","zkmJB");
		}catch(Exception x){
			x.printStackTrace();
		}
		System.out.println("abc");*/
		Map map=FlowHelper.getFlow("zkmUsuallyFlow");
		System.out.println(map.get("name"));
	}
}
