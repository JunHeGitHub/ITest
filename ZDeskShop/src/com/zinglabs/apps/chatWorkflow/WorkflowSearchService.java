package com.zinglabs.apps.chatWorkflow;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.zinglabs.apps.appCommonCurd.AppCommonsCurdService;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class WorkflowSearchService extends BaseService {
	
	public Map MyWorkSearch(Map map,HttpServletRequest request){
		AppCommonsCurdService as=new AppCommonsCurdService();
		//为查询关联表做必要的参数准备
		String columnValues="{\"cardUrl\":\""+map.get("columnValues")+"\"}";
		String equal="{\"cardUrl\":\""+map.get("equal")+"\"}";
		map.put("columnValues", columnValues);
		map.put("equal",equal);
		map.put("columns", "workflow_id");
		Map m =new HashMap();
		//根据用户名查询工作流关联表中我的工作流的工作流编号
		m=as.commonSelect(map, request);
		String idlist=m.get("data").toString().replace("workflow_id=", "").replace("[", "(").replace("{", "").replace("}", "").replace("]", ")");
		//为查询我的工作流做必要的参数准备
		map.remove("columnValues");
		map.remove("equal");
		map.remove("columns");
		map.put("beanId", "commonFreeFilter");
		map.put("nameSpace", "com.zinglabs.apps.chatWorkflow.defXml.anYongHuMingChaXun");
		map.put("idlist", idlist);
		map.put("isPagination", "true");
		Map rMap=new HashMap();
		//查询我的工作流表
		rMap=as.commonSelect(map, request);
		return rMap;
	}

	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		// TODO Auto-generated method stub
		return (AppSqlSessionDbid)getBean("appCommonsSqlSession");
	}

}
