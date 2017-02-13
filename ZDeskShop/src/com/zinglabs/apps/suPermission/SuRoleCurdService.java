package com.zinglabs.apps.suPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zinglabs.apps.i18nPrompt.I18nPromptUtils;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;


public class SuRoleCurdService extends BaseService {
	
	public Map getListForTeShu(Map map) throws Exception {
		Map result = new HashMap();
		List list = getServiceSqlSession(map).db_selectList(

		(String) map.get("nameSpaceId"), map);
     
		if(list.size()>0){
			result.put("success", true);
			
			return result;
		}else{
			result.put("fail", false);
			return result;
		}

		
	}
	public Map getListForTeShu_Role(Map map) throws Exception {
		Map result = new HashMap();
		List list = getServiceSqlSession(map).db_selectList(

		(String) map.get("nameSpaceId"), map);
		result.put("total", list.size());

		result.put("rows", list);
		if(list.size()>0){
			
			
			return result;
		}else{
			
			return null;
		}

		
	}
	public Map getRoleNotInLoginName(Map map) throws Exception {
		Map result = new HashMap();
		List list = getServiceSqlSession(map).db_selectList(

		(String) map.get("nameSpaceId"), map);
		result.put("total", list.size());

		result.put("rows", list);
		if(list.size()>0){
			
			
			return result;
		}else{
			
			return null;
		}

		
	}
	
	public Map getListForRoleName(Map map) throws Exception {
		Map result = new HashMap();
		List list = getServiceSqlSession(map).db_selectList(

		(String) map.get("nameSpaceId"), map);
		result.put("total", list.size());

		result.put("rows", list);
		if(list.size()>0){
			
			
			return result;
		}else{
			
			return null;
		}

		
	}

	public AppSqlSessionDbid getServiceSqlSession(Map map) {
		AppSqlSessionDbid appSqlSession = (AppSqlSessionDbid) getBean("suPermissSqlsession");
		String namespace = (String) map.get("nameSpace");
		appSqlSession.setNamespance(namespace);
		return appSqlSession;
	}

	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) getBean("suPermissSqlsession");
	}
	 public  Map addRoleForUser( String loginName,int RoleId[]){
		 Map map=new HashMap();
		 if(RoleId!=null&&RoleId.length>0&&loginName!=null&&!"".equals(loginName)){
			     List<Map<String,Object>>itemList=new ArrayList<Map<String,Object>>();
			     Map pMap=new HashMap();
			     for(int i=0;i<RoleId.length;i++){ 
			    	 Map<String,Object> map1=new HashMap<String,Object>();		
			    	 map1.put("roleId", RoleId[i]);
			    	 map1.put("loginName", loginName);			  
			    	 itemList.add(map1);
			     }
			    pMap.put("itemList", itemList);
			    //授权
			    getServiceSqlSession().db_insert("giveRoleForUser",pMap);
			    map.put("true", true);
				return map;
		 }
		 map.put("false", false);
		 return map;
	 }
	 /*
	  * 删除角色
	  */
	public boolean deleteRole(Map map){
		int result = getServiceSqlSession().db_delete("deleteRole",map);
		if(result>0){
			return true;
		}
		return false;
	}
	/*
	 * 删除角色用户关联
	 */
	public boolean deleteRoleUser(Map map){
		int result = getServiceSqlSession().db_delete("deleteRoleUser",map);
		if(result>0){
			return true;
		}
		return false;
	}
	/*
	 * 删除角色权限关联
	 */
	public boolean deleteRolePermission(Map map){
		map.put("roleCodeList",I18nPromptUtils.ascii2Native((String)map.get("roleCodeList")));
		int result = getServiceSqlSession().db_delete("deleteRolePermission",map);
		if(result>0){
			return true;
		}
		return false;
	}
}
