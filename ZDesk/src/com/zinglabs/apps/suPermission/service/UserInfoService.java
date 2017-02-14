package com.zinglabs.apps.suPermission.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.zinglabs.apps.suPermission.filter.SuSecurityUserFilter;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.base.core.utils.AppSpringUtils;

public class UserInfoService extends BaseService {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	private static AppSqlSessionDbid appSqlSession;
	public void init(){
		appSqlSession=(AppSqlSessionDbid)getBean("suPermissSqlsession");
	}
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) getBean("suPermissSqlsession");
	}

	/**
	 * 查询用户信息
	 * 
	 * @param 带有loginName='登录名'的map集合
	 * @return
	 */
	public Map getListForUser(HashMap map) {
		Map result = new HashMap();
		List userList = getServiceSqlSession().db_selectList("getUserList", map);

		List roleList=getServiceSqlSession().db_selectList("getRoleMapperList");
		List orgList=getServiceSqlSession().db_selectList("getOrgMapperList");
		result.put("users", userList);
		result.put("roles", roleList);
		result.put("orgs", orgList);

		return result;
	}

	/**
	 * 根据roleId获取loginName集合
	 * @param List
	 * @return
	 */
	public Map getLoginNameListFromRoleId(HashMap map) {
		Map result = new HashMap();
		List loginNameList = getServiceSqlSession().db_selectList("getLoginNameListFromRoleId", map);
		result.put("loginNameList", loginNameList);
		return result;
	}
	public Map saveUserRole(HashMap map) {
		Map rmap = new HashMap();
		String[] loginNameArray=((String)map.get("loginNameArray")).split(",");
		for(String s:loginNameArray){
			map.put("loginName",s);
			int result=getServiceSqlSession().db_insert("insertUserRole",map);
			if(result==1){
				rmap.put("success",true);
			}else{
				rmap.put("error",false);
			}
		}
		return rmap;
	}
	/*
	 * 条件查询用户信息
	 */
	public Map searchUser(HashMap map){
		Map rmap = new HashMap();
		List totals = getServiceSqlSession().db_selectList("searchUserTotal", map);
		rmap.put("total",((Map)totals.get(0)).get("total"));
		List userList = getServiceSqlSession().db_selectList("searchUser", map);
		rmap.put("data", userList);
		return rmap;
	}
	
	
	/*
	 * 根据用户id删除用户
	 */
	public boolean deleteUserById(Map map){
		int result=getServiceSqlSession().db_delete("deleteUserById",map);
		if(result>0){
			return true;
		}
		return false;
	}
	/*
	 * 根据loginName删除用户角色关联
	 */
	public boolean deleteUserRoleByLoginName(Map map){
		int result = getServiceSqlSession().db_delete("deleteUserRoleByLoginName",map);
		if(result>0){
			return true;
		}
		return false;
	}
	/*
	 * 根据loginName删除用户角色关联
	 */
	public boolean deleteUserOrgByLoginName(Map map){
		int result = getServiceSqlSession().db_delete("deleteUserOrgByLoginName",map);
		if(result>0){
			return true;
		}
		return false;
	}
	//-------------------接口------------------------
	/**
	 * 根据登录名获取用户信息
	 * @param loginName
	 * @return
	 */
	public static Map getUserInfoByLoginName(String loginName) {
		Map params=new HashMap();
		params.put("loginName", loginName);
		
		Map map = appSqlSession.db_selectOne("getUser", params);
		if(map==null){
			return new HashMap();
		}
		List roles= getRoleListByLoginName(loginName);
		List orgs= getOrgListByLoginName(loginName);
		map.put("roles", roles);
		map.put("orgs", orgs);
		return map;
	}
	
	public Map getUserByScreen(Map map){
		Map reMap = new HashMap();
		List list=(List) map.get("loginNameList");
		String str = "";
		for(Object obj:list){
			Map m = (Map) obj;
			str="'"+m.get("loginName")+"',"+str;
		}
		String loginNameList="";
		if(str.length()>1){
			loginNameList = str.substring(0,str.length()-1);
		}else{
			loginNameList =str;
		}
		
		Map pat = new HashMap();
		pat.put("loginNameList",loginNameList);
		List l = getServiceSqlSession().db_selectList("getUserByLoginNameScreen",pat);
		reMap.put("rows",l);
		return reMap;
	}
	
	/**
	 * 根据登录名查找用户拥有角色
	 * 
	 * @param loginName String类型
	 * @return List<String> 角色value集合
	 */
	public static List<String> getRoleListByLoginName(String loginName) {
		Map map = new HashMap();
		map.put("loginName",loginName);
		List list = appSqlSession.db_selectList("getRoleByloginNameToList",map);
		List<String> roleList = new ArrayList<String>();
		for(Object o:list){
			Map m =(Map)o;
			for(Object k:m.keySet()){
				String key = (String)k;
				roleList.add((String) m.get(key));
			}
		}
		return roleList;
	}
	/**
	 * 根据登录名查找用户所属组织
	 * 
	 * @param loginName String类型
	 * @return List<String> 组织value集合
	 */
	public static List<String> getOrgListByLoginName(String loginName) {
		Map map = new HashMap();
		map.put("loginName",loginName);
		List list = appSqlSession.db_selectList(
				"getOrgByloginNameToList", map);
		List<String> orgList = new ArrayList<String>();
		for(Object o:list){
			Map m =(Map)o;
			for(Object k:m.keySet()){
				String key = (String)k;
				orgList.add((String) m.get(key));
			}
		}
		return orgList;
	}
	
	/**
	 * 修改用户状态
	 * 0正常 1锁定 2删除
	 */
	public static boolean updateUserStartus(int userId,int startuNum){
		Map map = new HashMap();
		map.put("id", userId);
		map.put("startus", startuNum);
		int result= appSqlSession.db_update("updateUserStartus",map);
		if(result>1){
			System.out.println("修改用户状态成功");
			return true;
		}else{
			System.out.println("修改用户状态失败");
			return false;
		}
	}
	/**
	 * 修改用户最后登录时间
	 */
	public static boolean updateUserLastLoginDate(int userId){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date=sdf.format(new Date());
		Map map = new HashMap();
		map.put("id", userId);
		map.put("lastLoginDate",date);
		int result= appSqlSession.db_update("updateUserLastLoginDate",map);
		if(result>1){
			System.out.println("修改用户最后后登录时间成功");
			return true;
		}else{
			System.out.println("修改用户最后后登录时间失败");
			return false;
		}
	}
	/**
	 * 修改用户最后修改密码时间
	 */
	public static boolean updateUserLastModifyPassword(int userId){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date=sdf.format(new Date());
		Map map = new HashMap();
		map.put("id", userId);
		map.put("lastModifyPassword", date);
		int result= appSqlSession.db_update("updateUserLastModifyPassword",map);
		if(result>1){
			System.out.println("修改用户最后修改密码时间成功");
			return true;
		}else{
			System.out.println("修改用户最后修改密码时间失败");
			return false;
		}
	}
	//获取所有角色对应用户
	public static Map getRoleMapUser(){
		List<Map> roles= getAllRole();
		 Map  roleMapUser= getAllUserInfo();
         Map result=new HashMap();
         for(int i=0;i<roles.size();i++){
        	Map role=roles.get(i);
        	String id=(String)role.get("id");
        	String name=(String)role.get("name");
        	Map orginfo=new HashMap();
			orginfo.put("name", name);
        	if(name!=null&&!"".equals(name)){
    			orginfo.put("users", roleMapUser.get(id));
    			
        		result.put(id, orginfo);
        	}
         }
		return result;
	}
	
	//获取所有角色
	public static List<Map> getAllRole() {
		return appSqlSession.db_selectList("getAllRole");
	}
	//获取所有用户信息
	public static Map getAllUserInfo() {
		List alluser =appSqlSession.db_selectList("getAllUserInfo");
		Map<String,List> roles =new HashMap();
		for(int i=0;i<alluser.size();i++){
			Map user=(Map)alluser.get(i);
			//System.out.println("users"+user);
			String role=user.get("role")==null?"":(String)user.get("role");
			String roleArr[]=role.split(",");
			for(int j=0;j<roleArr.length;j++){
				String roleId=roleArr[j];
				List users=roles.get(roleId)==null?new ArrayList():roles.get(roleId);
				users.add(user);
				roles.put(roleId, users);
			}
			
			
		}
		return roles;
	}
	public static void main(String[] args) {
		ApplicationContext context=AppSpringUtils.getApplicationContext();
		System.out.println(getRoleMapUser());
	}
}
