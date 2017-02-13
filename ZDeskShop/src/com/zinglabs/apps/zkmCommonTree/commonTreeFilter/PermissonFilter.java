package com.zinglabs.apps.zkmCommonTree.commonTreeFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zinglabs.apps.suPermission.service.PermissonHelper;
public class PermissonFilter implements ZkmCommonTreeFilter{

	public List<HashMap<String, String>> commonTreeFilter(Map<String, String> map,
			List<HashMap<String, String>> targetNodeList) {
		String roleNames=map.get("roleName");
		String roleName[]=roleNames.split(",");
		
	 	List filterNodes=PermissonHelper.getTreeByRole(roleName);
	 	List <HashMap<String, String>> list =new ArrayList<HashMap<String,String>>();
	 	List rList=new ArrayList();
	 	for(int i=0;i<targetNodeList.size();i++){
	 		Map target=targetNodeList.get(i);
	 		if(filterNodes.contains(target.get("id"))){
	 			rList.add(target);
	 		}
	 	}
	 	
		return rList;
	}

}
