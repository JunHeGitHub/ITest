package com.zinglabs.apps.suPermission.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.suPermission.SuSecurityPermissionService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;
@Controller
@RequestMapping(value="/*/suSecurityPermission")
public class SuSecurityPermissionAction extends BaseAction{
	/**
	 * 根据条件查询工单
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/searchSuSecurityPermission")
	public void searchSuSecurityPermission(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		List<Map<String,String>> mapList = getService().searchSuSecurityPermission(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response) ;
	}
	

	//删除操作
	@RequestMapping(value="/deleteSuSecurityPermission")
	public void deleteSuSecurityPermission(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		String sid=map.toString();
		String id=sid.substring(1,sid.lastIndexOf("="));
		String[]test =id.split(",");
		List<String> myList=new ArrayList<String>();
		for(int i=0;i<test.length;i++){
			myList.add(test[i]);
		}
		Map<String,List<String>> maplist=new HashMap<String,List<String>> ();
		maplist.put("map", myList);
		int num = getService().deleteSuSecurityPermission(maplist);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",num),response) ;
	}
	
	//增加操作
	@RequestMapping(value="/SaveSuSecurityPermission")
	public void SaveSuSecurityPermission(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		RandomGUID myGUID = new RandomGUID();
		map.put("id", myGUID.toString());
		int mapList = getService().SavesuSuSecurityPermission(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response) ;			
	
	}
	//修改操作
	@RequestMapping(value="/updateSuSecurityPermission")
	public void updateSuSecurityUser(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		int mapList = getService().updateSuSecurityPermission(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response) ;
	}
	
	public SuSecurityPermissionService getService(){
		return (SuSecurityPermissionService)getBean("suSecurityPermission");
	}		
	
	
}
