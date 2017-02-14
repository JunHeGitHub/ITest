package com.zinglabs.apps.zkmCommonTree.userMapperOrgTreeFilter;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;


public interface UserMapperOrgTreeFilter {
	/**
	 * 
	 * @param users  所用用户信息 
	 *  otherParams 一些用户信息 
	 *  如otherParams.get("loginName"); 可以取到用户信息
	 */
    public void userFilter(List <Map> users,Map<String,Object>otherParams);
    
    /**
     * 
     * @param rootOrgs
     * @param childOrgs
     */
    public void orgFilter(List <Map> rootOrgs,List <Map> childOrgs, Map<String,Object>otherParams);
    
    public List <Map> getUserByFilter(List <Map> users,Map<String,Object>otherParams);
    
}
