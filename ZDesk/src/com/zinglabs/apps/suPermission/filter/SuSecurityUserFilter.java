package com.zinglabs.apps.suPermission.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.apps.appCommonCurd.filter.interfaces.ParamsFilterInterface;

@SuppressWarnings("unchecked")
public class SuSecurityUserFilter implements ParamsFilterInterface {
	public static  Logger logger = LoggerFactory.getLogger("ZDesk");
	
	public Map freeFilter(Map params, HttpServletRequest request)
			throws Exception {
		Map map =new HashMap();
		String loginName =(String)params.get("loginName");
		if(loginName!=null){
			if(loginName.length()>0){
				map.put("loginName",loginName);
			}
		}
		String name =(String)params.get("name");
		if(name!=null){
			if(name.length()>0){
				map.put("name",name);
			}
		}
		String orgIds =(String)params.get("orgIds");
		if(orgIds!=null){
			if(orgIds.length()>0){
				params.put("orgIds",processingStr(orgIds));
			}else{
				params.remove("orgIds");
			}
		}
		
		String orgs =(String)params.get("orgs");
		if(orgs!=null){
			if(orgs.length()>0){
				orgs = processingStr(orgs);
				params.put("orgs",orgs);
				map.put("orgs",orgs);
			}else{
				params.remove("orgs");
			}
		}
		String roleIds =(String)params.get("roleIds");
		if(roleIds!=null){
			if(roleIds.length()>0){
				params.put("roleIds",processingStr(roleIds));
			}else{
				params.remove("roleIds");
			}
		}
		String roles =(String)params.get("roles");
		if(roles!=null){
			if(roles.length()>0){
				roles = processingStr(roles);
				params.put("roles",roles);
				map.put("roles",roles);
			}else{
				params.remove("roles");
			}
		}
		
		String tableName =(String)params.get("tableName");
		if(tableName!=null){
			if(tableName.length()>0){
				map.put("tableName",tableName);
			}
		}
		String loginNameList =(String)params.get("loginNameList");
		if(loginNameList!=null){
			if(loginNameList.length()>0){
				map.put("loginNameList",loginNameList);
			}
		}
		params.put("columnValues",map);
		return params;
	}
	//处理以逗号分隔的字符串，为字符串加单引号
	public String processingStr(String str){
		String result = "";
		String[] strArr = str.split(",");
		for(String s:strArr){
			result += "'"+s+"',";
		}
		result = result.substring(0,result.length()-1);
		return result;
	}
}
