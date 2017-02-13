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

public class ZkmSimpleCommonOrgTreeFilter extends BaseService implements ZkmCommonTreeFilter {

	public List<HashMap<String, String>> commonTreeFilter(Map<String, String> map, List<HashMap<String, String>> targetNodeList) {
		if(targetNodeList.size()<=0){
			return targetNodeList;
		}else{
			Map<String, Object> myMap = new HashMap<String, Object>();
			myMap.put("loginName", map.get("user")); 
			List suSecurityUserOrg = getServiceSqlSession().db_selectList("getUserOrgeList", myMap);
			for (HashMap<String, String> targetNode : targetNodeList) {
				targetNode.put("name", targetNode.get("text"));
				for (HashMap securityUserOrg : (List<HashMap>)suSecurityUserOrg) {
					if (targetNode.get("id").equals(securityUserOrg.get("orgCode"))) {
						targetNode.put("checked", "true");
					}
				}
			}
			return targetNodeList;
		}
	}

	@Override
	public AppSqlSessionDbid  getServiceSqlSession() {
		return (AppSqlSessionDbid )this.getBean("zkmCommonTreeSqlSession");
	}


}
