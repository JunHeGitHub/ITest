package com.zinglabs.apps.treeDataMapper.treeDataFilter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zinglabs.apps.treeDataMapper.treeDataFilter.TreeDataFilter;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class TreeDataFilterImpl extends BaseService implements TreeDataFilter {

	/*
	 * 绑定数据
	 */
	public boolean doDataBinding(Map map) {
		String nodeIdStr = (String) map.get("nodeIdList");
		String dataIdStr = (String) map.get("dataIdList");
		
		String nodeIdList[] = nodeIdStr.split(",");
		String dataIdList[] = dataIdStr.split(",");
		
		for(String nodeId:nodeIdList){
			Map pat = new HashMap();
			pat.put("nodeId",nodeId);
			for(String dataId:dataIdList){
				pat.put("dataId",dataId);
				getServiceSqlSession().db_insert("addData",pat);
			}
		}
		return true;
	}

	/*
	 * 解绑数据
	 */
	public boolean doDataUnbinding(Map map) {
		getServiceSqlSession().db_delete("deleteData",map);
		return true;
	}

	/*
	 * 查询数据
	 */
	public Map getDataList(Map map) {
		Map newMap = filterMap(map);
		List list=getServiceSqlSession().db_selectList("getDataList",newMap);
		Map rmap = new HashMap();
		rmap.put("data",list);
		return rmap;
	}
	/*
	 * 过滤Map
	 */
	public Map filterMap(Map map){
		Map m = new HashMap();
		List list = new ArrayList();
		for(Object o : map.keySet()){
			String key = (String) o;
			if(key.equals("beanName")){
				continue;
			}
			if(key.equals("tableName")){
				continue;
			}
			if(key.equals("columnName")){
				continue;
			}
			list.add((String)o);
		}
		map.put("keyList",list);
		return map;
	}

	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid)this.getBean("treeDataMapperSqlSession");
	}
}
