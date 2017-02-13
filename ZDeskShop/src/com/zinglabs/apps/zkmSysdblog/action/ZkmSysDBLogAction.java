package com.zinglabs.apps.zkmSysdblog.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.aops.AppsActionMethodAop;
import com.zinglabs.apps.zkmSysdblog.ZkmSysDBLogService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.db.UserInfo2;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping("/*/ZKMSysDBLog")
public class ZkmSysDBLogAction extends BaseAction {
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addSysDBLog")
	public void sysDBLogAdd(@RequestParam HashMap sdb,HttpServletRequest request,HttpServletResponse response){
//		UserInfo2 user = getTTSessionUser(request);
//		if (user == null || "_guest".equals(user.loginName2)) {
//			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
//			return;
//		}
		String loginName=(String) request.getAttribute(AppsActionMethodAop.TT_SESSION_USER_KEY_);
		getService().sysDBLogInsert(sdb,loginName);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, ""), response);
	}
	
	@RequestMapping(value = "/showAllSysDBLog")
	public void showAllSysDBLog(@RequestParam HashMap sdb,HttpServletRequest request,HttpServletResponse response){
//		UserInfo2 user = getTTSessionUser(request);
//		if (user == null || "_guest".equals(user.loginName2)) {
//			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
//			return;
//		}
		List<Map> sysDBLogpojoList = getService().sysDBLogSelect(sdb);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", sysDBLogpojoList),response);
	}
	public ZkmSysDBLogService getService(){
		return (ZkmSysDBLogService)getBean("zkmSysDBLogService");
	}


}
