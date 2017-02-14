package com.zinglabs.apps.messageProcess;

import java.util.List;
import java.util.Map;

import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.base.core.frame.BaseService;

public class messageProcessService extends BaseService {
	/**
	 * 获取一条随机数据
	 * @param map
	 * @return
	 */
	public Map randomAccessToData(Map map) {
		return getServiceSqlSession().db_selectOne("selectMessData", map);
	}
	public int UpdateMess(Map map){
		return ((Integer)getServiceSqlSession().db_update("updateMessData", map)).intValue();
	}
	
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) this.getBean("messageProcessSqlSession");
	}


}
