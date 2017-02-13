package com.zinglabs.apps.chatWorkflow.action;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.chatWorkflow.WorkflowService;
import com.zinglabs.apps.chatWorkflow.requestService;
import com.zinglabs.apps.suPermission.service.UserInfoService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.CookieUtils;
import com.zinglabs.util.JsonUtils;
@Controller
@RequestMapping("/*/RA")
public class requestAction extends BaseAction {
	
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/requestSelect")
	public void requestSelect(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		List mapList=getService().reSelect(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response);
	}
	//删除选中角色已有的分类权限
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/deleteCategory")
	public void deleteCategory(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		int mapList =getService().deCategory(map);
		if(mapList==0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "授权失败",mapList),response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response);
		}
	}
	
	//验证已选中模块下的描述是否有权限
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/checkCategoryDesc")
	public void checkCategoryDesc(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		//System.out.println(map);
		//获取当前登录人的cardUrl
		String cardUrl="";
		try {
			cardUrl= CookieUtils.getMhCardUrlByCookie(request,null);
			if(cardUrl==null || cardUrl.equals("")){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "当前登录信息已失效，请重新登录门户！"),response);
				return;
			}
		} catch (Exception e) {
			logger.error("获取cookie信息出现异常，cardUrl:"+cardUrl);
			e.printStackTrace();
		}
		
		//获取当前登录人与ZDesk用户账号的登录名
		String loginName = "";
		WorkflowService ws =new WorkflowService();
		List<Map<String, String>> list=ws.getZDeskLoginNameBycardUrl(cardUrl);
		if(list!=null && list.size()!=0){
			for (int i = 0; i < list.size(); i++) {
				loginName = list.get(i).get("zdeskUserLoginName").toString();
			}
		}
		//获取当前的登录人所属组织机构的id
		String roleCode ="";
		List<Map<String, String>> list2 = ws.getOrgByLoginName(loginName);
		if (list2!=null && list2.size()!=0) {
			roleCode="";
			for (int j = 0; j < list2.size(); j++) {
				roleCode+=list2.get(j).get("id").toLowerCase()+",";
			}
			if(!"".equals(roleCode)){
				roleCode=roleCode.substring(0,roleCode.length()-1);
			}
		}
		//修改前台传进map中的roleCode值
		map.remove("roleCode");
		map.put("roleCode",roleCode);
		List mapList =getService().checkDesc(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response);
	}
	//获取角色信息

	@RequestMapping(value = "/getRoleUser")
	public void getRoleMapUser(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		List mapList=getService().searchUser(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response);
	}
	
	public requestService getService(){
		return (requestService)getBean("requestService");
	}
}
