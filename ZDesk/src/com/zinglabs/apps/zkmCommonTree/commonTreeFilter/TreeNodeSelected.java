package com.zinglabs.apps.zkmCommonTree.commonTreeFilter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zinglabs.apps.suPermission.service.PermissonHelper;

public class TreeNodeSelected implements ZkmCommonTreeFilter {

	
	            
	public List<HashMap<String, String>> commonTreeFilter(Map<String, String> map,List<HashMap<String, String>> targetNodeList) {
		
		//String type=map.get("type");
		String role=map.get("roleName");
		String roles []=role.split(",");
	    List<Map<String,String>> pList=PermissonHelper.getPermisstionByRole(roles, "menu");
	     Set<String>set =new HashSet<String>();
	    for(Map<String,String> permisson :pList){
	    	String code=permisson.get("permissionCode");
	    	set.add(code);
	    }
	    for(int i=0;i<targetNodeList.size();i++){
	 		Map target=targetNodeList.get(i);
	 		if(set.contains(target.get("id"))){
	 			//targetNodeList.remove(i);
	 			target.put("checked", "true");
	 		}
	 	}
	    
	  
		return targetNodeList;
	}

}
