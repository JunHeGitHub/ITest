package com.zinglabs.apps.customTextTemplate.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.DbidSqlsession;

public class CustomTextService extends BaseService {
	public static Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	
	public DbidSqlsession getServiceSqlSession() {
		return (DbidSqlsession) this.getBean("customTextTemplateSqlsession");
	}
	//=======================模板开始============================
	// 查询模板
	public Map<String, String> getTemplate() {
		List list = getServiceSqlSession().db_selectList("getAllTemplate");
		Map result = new HashMap();
		result.put("rows", list);
		return result;
	}
	//根据id查询模板
	public Map getTemplateById(Map map) {
		List list = getServiceSqlSession().db_selectList("getTemplateById",map);
		Map result = new HashMap();
		result.put("rows", list);
		return result;
	}
	//根据分类名称查询模板
	public Map<String,String> getTemplateByTemplateTypeName(Map map){
		List list = getServiceSqlSession().db_selectList("getTemplateByTemplateTypeName",map);
		Map result = new HashMap();
		result.put("rows", list);
		return result;
	}
	
	// 添加模板
	public boolean addTemplate(Map map) {
		boolean flag = false;
		int result=getServiceSqlSession().db_insert("addTemplate", map);
		if(result>0){
			flag=true;
		}
		return flag;
		
	}

	// 修改模板
	public boolean updateTemplate(Map map) {
		boolean flag = false;
		int result=getServiceSqlSession().db_update("updateTemplateById", map);
		if(result>0){
			flag=true;
		}
		return flag;
	}

	// 删除模板
	public boolean deleteTemplate(Map map) {
		boolean flag = false;
		int result=getServiceSqlSession().db_delete("deleteTemplateById",map);
		if(result>0){
			flag=true;
		}
		return flag;
	}
	//=======================模板结束============================
	
	//=======================模板数据开始============================
	//查询模板数据
	public Map getTemplateDataByTemplateId(Map map) {
		List list = getServiceSqlSession().db_selectList("getTemplateDataByTemplateId",map);
		Map result = new HashMap();
		result.put("rows", list);
		return result;
	}
	// 添加模板数据
	public boolean addTemplateData(Map map) {
		boolean flag = false;
		int result=getServiceSqlSession().db_insert("addTemplateData", map);
		if(result>0){
			flag=true;
		}
		return flag;
	}
	//删除模板数据
	public boolean deleteTemplateDataByTemplateId(Map map) {
		boolean flag = false;
		int result=getServiceSqlSession().db_delete("deleteTemplateDataByTemplateId",map);
		if(result>0){
			flag=true;
		}
		return flag;
	}
	//修改模板数据
	public boolean updateTemplateDataByTemplateId(Map map) {
		boolean flag = false;
		int result=getServiceSqlSession().db_update("updateTemplateDataByTemplateId", map);
		if(result>0){
			flag=true;
		}
		return flag;
	}
	//=======================模板数据结束============================
}
