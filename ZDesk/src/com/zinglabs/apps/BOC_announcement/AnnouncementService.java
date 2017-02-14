package com.zinglabs.apps.BOC_announcement;

import java.util.List;
import java.util.Map;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class AnnouncementService extends BaseService{

	
	//向接收者表中插入数据
	public void insert(Map<String, String> map){
		getServiceSqlSession().db_insert("BOC_insert", map);
	}	
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		// TODO Auto-generated method stub
		return (AppSqlSessionDbid)this.getBean("announceSqlSession");
	}

}
