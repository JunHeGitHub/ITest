package com.zinglabs.apps.webUploader.service;

import java.util.HashMap;
import java.util.Map;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class UploadFileService extends BaseService{

	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) getBean("uploadFileSqlSession");
	}
	//添加文件到数据库
	public void addUploadFile(Map map){
		getServiceSqlSession().db_insert("addUploadFile",map);
	}
	//添加关联信息到数据库
	public void addFileDataMapper(Map map){
		getServiceSqlSession().db_insert("addFileDataMapper",map);
	}
	//查询配置表中配置的相对路径
	public String searchRelativeValue(String key){
		Map map =  new HashMap();
		map.put("relativeKey",key);
		String value = getServiceSqlSession().db_selectOne("searchRelativeValue",map);
		return value;
	}
}
