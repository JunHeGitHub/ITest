package com.zinglabs.apps.treeDataMapper.treeDataFilter;

import java.util.Map;


public interface TreeDataFilter {
	//绑定数据
	boolean doDataBinding(Map map);
	//解绑数据
	boolean doDataUnbinding(Map map);
	//查询数据
	Map getDataList(Map map);
	
}
