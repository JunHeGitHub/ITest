package com.zinglabs.apps.suPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.util.RandomGUID;


public class SuSecurityPermissionService extends BaseService {
	//根据条件查询工单
	public List<Map<String,String>> searchSuSecurityPermission(Map<String, String> map){
		List list = getServiceSqlSession().db_selectList("searchSuSecurityPermission",map);
		return list;
	}
	//根据权限类型获取对应的信息
	public List<Map<String,String>> searchSuSecurityPermission(String type){
		Map map=new HashMap<String, String>();
		map.put("typeName", type);
		List list=getServiceSqlSession().db_selectList("searchSuSecurityPermission",map);
		return list;
	}
	
	
	//验证权限码code
	public List<Map<String,String>> checkSuSecurityPermission(Map<String, String> map){
		
		List list =getServiceSqlSession().db_selectList("checkSuSecurityPermission",map);
		return list;
	}
	
	//删除操作
	public int deleteSuSecurityPermission(Map<String,List<String>> map){
		return getServiceSqlSession().db_delete("deleteSuSecurityPermission",map);
	}
	
	//修改操作
	public int updateSuSecurityPermission(Map<String, String> map){
		return getServiceSqlSession().db_delete("SuSecurityPermission_update",map);
	}
	//保存操作
	public int SavesuSuSecurityPermission(Map<String, String> map){
		return this.getServiceSqlSession().db_insert("SuSecurityPermission_insert", map);
	}

   	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) getBean("suPermissSqlsession");
	}
}
