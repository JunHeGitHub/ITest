package com.zinglabs.apps.zkmCommonTree.userMapperOrgTreeFilter;

import java.util.List;
import java.util.Map;

public class IconFileterImpl implements UserMapperOrgTreeFilter{



	@Override
	public void orgFilter(List<Map> rootOrgs, List<Map> childOrgs,Map<String,Object>otherParams) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void userFilter(List<Map> users,Map<String,Object>otherParams) {
		// TODO Auto-generated method stub
		  for(int i=0;i<users.size();i++){
            Map map=(Map)users.get(i);
            map.put("icon", "/ZDesk/js/zTree/zTreeStyle/img/diy/icon_unavailable.gif");
       }
	}

	@Override
	public List<Map> getUserByFilter(List<Map> users,
			Map<String, Object> otherParams) {
		// TODO Auto-generated method stub
		return null;
	}

}
