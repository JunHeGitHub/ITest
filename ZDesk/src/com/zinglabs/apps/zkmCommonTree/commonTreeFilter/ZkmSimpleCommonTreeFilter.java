package com.zinglabs.apps.zkmCommonTree.commonTreeFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.util.UITreeHelper;

public class ZkmSimpleCommonTreeFilter extends BaseService implements ZkmCommonTreeFilter {

	public List<HashMap<String, String>> commonTreeFilter(Map<String, String> map, List<HashMap<String, String>> targetNodeList) {
		if(targetNodeList.size()<=0){
			return targetNodeList;
		}else{
			Map<String, Object> myMap = new HashMap<String, Object>();
			String recordType=map.get("recordType");
			if("d".equals(recordType)){
				recordType="zkmData";
			}
			myMap.put("dataType", recordType);
			myMap.put("roleName", map.get("roleName")); 
			myMap.put("myList", targetNodeList);
			List zkmInfoSecurityList = getServiceSqlSession().db_selectList("getCommonTreeFilter", myMap);
			for (HashMap<String, String> targetNode : targetNodeList) {
				targetNode.put("name", targetNode.get("text"));
				for (HashMap zkmInfoSecurity : (List<HashMap>)zkmInfoSecurityList) {
					if (targetNode.get("id").equals(zkmInfoSecurity.get("infoId"))) {
						targetNode.put("checked", "true");
					}
				}
			}
			targetNodeList=UITreeHelper.zkmTreeFormat(targetNodeList);
			return targetNodeList;
		}
	}

	@Override
	public AppSqlSessionDbid  getServiceSqlSession() {
		return (AppSqlSessionDbid )this.getBean("zkmCommonTreeSqlSession");
	}


}
