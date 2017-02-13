package com.zinglabs.apps.activitiManager.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.zinglabs.base.core.activitiSupport.ActivitiUtils;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.base.core.utils.AppSpringUtils;

public class ActivitiManagerService extends BaseService{
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	public static Map<String, Map<String, String>> activtiParamMap = new HashMap<String, Map<String,String>>();

	public void initActivitiParam(){
//		List<Map> mapList=null;
//		try{
//			mapList= getServiceSqlSession().db_selectList("getList");
//		}catch(Exception x){
//			logger.error("......activiti initActivitiParam error." + x.getMessage());
//			return;
//		}
//		String curretProcessDefination = "";
//		Map<String, String> curretMap = new HashMap<String, String>();
//		for (Map map : mapList) {
//			String processDefination =(String) map.get("processDefination");
//			if(curretProcessDefination.length() != processDefination.length() || curretProcessDefination.indexOf(processDefination)!=0){
//				if(curretProcessDefination.length()>0){
//					activtiParamMap.put(curretProcessDefination, curretMap);
//				}
//				curretProcessDefination = processDefination;
//				curretMap = new HashMap<String, String>();
//			}
//			curretMap.put((String)map.get("key"),(String) map.get("value"));
//		}
//		activtiParamMap.put(curretProcessDefination, curretMap);
//		try{
//			ActivitiUtils ActivitiUtils= new ActivitiUtils();
//			ActivitiUtils.initActivitiConf() ;
//		}catch(Exception e){
//			logger.error("......activiti initActivitiParam ActivitiUtils.initActivitiConf error." + e.getMessage());
//			return;
//		}
	}

	public AppSqlSessionDbid getServiceSqlSession(){
		return (AppSqlSessionDbid)this.getBean("activitiManagerSession");
	}
	
	public static void main(String[] args) throws Exception{
		ApplicationContext context=AppSpringUtils.getContextAsClassPath(new String[]{"appConf/applicationContext.xml","classpath:com/zinglabs/apps/**/xml/*-beans.xml"});
		ActivitiManagerService su=(ActivitiManagerService)context.getBean("activitiManagerService");
	}
}
