package com.zinglabs.apps.suPermission.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.suPermission.SuRoleCurdService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

@RequestMapping(value = "/*/RoleCurd")
@Controller
public class SuRoleCurdAction extends BaseAction {

public static Logger logger = LoggerFactory.getLogger("ZDesk");
	/**
	 * 特殊查询
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/FindTeShu")
	public void findTeShu(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {		
		try {
			Map rmap = this.getService().getListForTeShu(map);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);

		} catch (Exception e) {
			logger.error("common find fail");
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/getRoleByLoginName")
	public void getRoleByLoginName(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {		
		try {
			String loginName=request.getParameter("loginName");
			//map.put("loginName", "admin");//测试
			//map.put("loginName", loginName);
			Map rmap = this.getService().getListForTeShu_Role(map);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);

		} catch (Exception e) {
			logger.error("common find fail");
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/getRoleNotInLoginName")
	public void getRoleNotInLoginName(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {		
		try {
			String loginName=request.getParameter("loginName");
			Map rmap = this.getService().getRoleNotInLoginName(map);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);

		} catch (Exception e) {
			logger.error("common find fail");
			e.printStackTrace();
		}
	}
	@RequestMapping(value = "/getRoleByRoleName")
	public void getRoleByRoleName(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {		
		try {
			Map rmap = this.getService().getListForRoleName(map);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);

		} catch (Exception e) {
			logger.error("common find fail");
			e.printStackTrace();
		}
	}
	public SuRoleCurdService getService() {
		
		return (SuRoleCurdService) getBean("suRoleCurdService");
	}
    
	/**
	 * 为用户授予角色
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * loginName为测试数据，对接时需要修改
	 */
	@RequestMapping(value = "/GiveRoleForUser", method = RequestMethod.POST)
	public void giveRoleForUser(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			Map rmap=new HashMap();
			String loginName=request.getParameter("loginName");
			if(loginName==null||loginName.equals("")){
				rmap.put("false", false);
				postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
				return;
			}
			String idStr=(String)map.get("primaryKeyValue");
			if(idStr.equals("")||idStr==null){
				rmap.put("no", "no");
				postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
				return;
			}
			//String loginName="admin";
			//System.out.println(idStr);
			if (idStr != null) {
				String[] id = idStr.split(",");
				int[] ids=new int[id.length];
				for (int i = 0; i < id.length; i++) {		
					ids[i]=Integer.parseInt(id[i]);
				}
				
				rmap=getService().addRoleForUser(loginName, ids); 
			}
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);		
			} catch (Exception e) {
			logger.error("GiveRoleForUser fail"+e.getMessage());
		
			return ;
		}
	}
	 /*
	  * 删除角色
	  */
	@RequestMapping(value="deleteRole")
	public void deleteRole(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		 getService().deleteRole(map);
		 postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功").toString(), response);
	}
	/*
	 * 删除角色用户关联
	 */
	@RequestMapping(value="deleteRoleUser")
	public void deleteRoleUser(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		getService().deleteRoleUser(map);
		 postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功").toString(), response);
	}
	/*
	 * 删除角色权限关联
	 */
	@RequestMapping(value="deleteRolePermission")
	public void deleteRolePermission(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		getService().deleteRolePermission(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功").toString(), response);
	}
}
