package com.zinglabs.apps.chatWorkflow.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.suPermission.service.UserInfoService;
import com.zinglabs.apps.zkmCommonTree.ZkmTreeCommonService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping("/*/chatWorkflow")
public class ChatWorkflowAction  extends BaseAction {
	public static  Logger logger = LoggerFactory.getLogger("ZDesk"); 
	/**
	 * 获取组织机构下的所有用户信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/getOrgAndUserList")
	public void getOrgAndUserList(@RequestParam HashMap<String,String> map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		ZkmTreeCommonService zx=new ZkmTreeCommonService();
		Map m=zx.getOrgAndUserList(new HashMap());
		if (m!=null) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",m), response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "数据获取失败!"), response);
		}
	}	
	/**
	 * 获取组织机构下的所有用户信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/getRoleMapUser")
	public void getRoleMapUser(@RequestParam HashMap<String,String> map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		UserInfoService uis=new UserInfoService();
		Map m = uis.getRoleMapUser();
		
		if (m!=null) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",m), response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "数据获取失败!"), response);
		}
		
	}
}

