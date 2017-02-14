package com.zinglabs.apps.fileDownload.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.DbidSqlsession;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class fileDownloadService extends BaseService {

	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		return (DbidSqlsession) this.getBean("fileDownloadSqlsession");
	}

	/*
	 * 根据数据id查询已上传文件信息
	 */
	public Map searchFileToDownload(Map map) {
		Map resultMap = new HashMap();
		List reulst = getServiceSqlSession().db_selectList(
				"searchFileToDownload",map);
		resultMap.put("data", reulst);
		return resultMap;

	}
}
