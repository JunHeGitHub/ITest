package com.zinglabs.apps.zkmCommonTree.commonTreeFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ZkmCommonTreeFilter {
	// map页面参数，需要被过滤的树节点
	List<HashMap<String, String>> commonTreeFilter(Map<String,String> map,List<HashMap<String, String>> targetNodeList);
}
