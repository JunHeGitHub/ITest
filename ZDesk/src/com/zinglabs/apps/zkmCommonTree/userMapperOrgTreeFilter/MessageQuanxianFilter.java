package com.zinglabs.apps.zkmCommonTree.userMapperOrgTreeFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class MessageQuanxianFilter extends BaseService implements UserMapperOrgTreeFilter{

	@Override
	public void orgFilter(List<Map> rootOrgs, List<Map> childOrgs,
			Map<String, Object> otherParams) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void userFilter(List<Map> users, Map<String, Object> otherParams) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public AppSqlSessionDbid  getServiceSqlSession() {
		return (AppSqlSessionDbid )this.getBean("zkmCommonTreeSqlSession");
	}

	@Override
	public List<Map> getUserByFilter(List<Map> users,
			Map<String, Object> otherParams) {
		// TODO Auto-generated method stub
		Map map = new HashMap() ;
		map.put("permissionName", otherParams.get("permissionName")) ;
		List<Map> roleIds=getServiceSqlSession().db_selectList("roleIdByPermissionName",map); 
		List<Map> rUser = new ArrayList<Map>() ;
		if(roleIds.size()>0){
			for(int j=0 ;j<users.size();j++){
				int ct = 0 ;
				for(int i=0 ;i<roleIds.size();i++){
					if(ct==1){
						break ;
					}
					if(users.get(j).get("role")!=null){
						String role = users.get(j).get("role").toString() ;
						String s[] = role.split(",") ;
						for (String str : s) {
							if (str.equals(roleIds.get(i).get("id"))) {
								rUser.add(users.get(j)) ;
								ct = 1 ;
								break ;
							}
						}
					}
				}
			}
		}
		return rUser;
	}
}
