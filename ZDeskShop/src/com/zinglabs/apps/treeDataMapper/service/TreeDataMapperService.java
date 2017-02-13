package com.zinglabs.apps.treeDataMapper.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class TreeDataMapperService extends BaseService{

	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) getBean("treeDataMapperSqlSession");
	}

	/*
	 * 根据nodeId查询数据
	 */
	public Map getDataByNodeId(Map map) {
		
		List list=getServiceSqlSession().db_selectList("selectByNodeId",map);
		Map rmap = new HashMap();
		rmap.put(map.get("nodeId"),list);
		return rmap;
	}

	/*
	 * 根据dataId查询数据
	 */
	public Map getDataByDataId(Map map) {
		List list=getServiceSqlSession().db_selectList("selectByDataId",map);
		Map rmap = new HashMap();
		rmap.put(map.get("dataId"),list);
		return rmap;
	}
	
	/*
	 * 获取分类树
	 */
	public Map getTemplateTree() {
		List list = getServiceSqlSession().db_selectList("getTemplateTypeTree");
		Map map =new HashMap();
		map.put("data",list);
		return map;
	}
	
	/*
	 * 获取节点名称
	 */
	public Map getTreeNodeText(Map map) {
		List list = getServiceSqlSession().db_selectList("getTreeNodeText",map);
		Map rmap =new HashMap();
		rmap.put("data",list);
		return rmap;
	}
	
}
