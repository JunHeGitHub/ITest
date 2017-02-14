package com.zinglabs.apps.suPermission.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.util.JsonUtils;

public class MH_ZDeskUserCompany extends BaseService{
public static Logger logger = LoggerFactory.getLogger("ZDesk");
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) getBean("suPermissSqlsession");
	}
	/**
	 *根据用户名获取MH_ZDeskUserCompany表中用户对象
	 *@param loginName 输入用于名 
	 *@return 
	 */
	public List<Map> findMHUserByLoginName(HashMap map){
		
		return getServiceSqlSession().db_selectList("FindMHUserByLoginNameSel",map);
	}
	/**
	 *新增用户到MH_ZDeskUserCompany表
	 *@param loginName 登录名 companyId 公司编号 
	 *@return 
	 */
	public int addUserOfMHSer(Map map){
	
		return((Integer)getServiceSqlSession().db_insert("AddUserInsOfMHIns", map)).intValue();
	}
	/**
	 * 根据用户用获取chatWorkflow_binding表中已绑定的用户
	 */
	public List<Map> findMHuserByLoginNameIsBinding(HashMap map){
		return getServiceSqlSession().db_selectList("findMHuserByLoginNameIsBinding",map);
	}
	
	/**
	 * 删除未绑定的MH用户
	 */
	public int deleteMH_UserOfMHSer(Map map){
		return((Integer)getServiceSqlSession().db_delete("deleteMH", map)).intValue();
	}
}
