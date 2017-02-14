package com.zinglabs.apps.suPermission.action;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.i18nPrompt.I18nPromptUtils;
import com.zinglabs.apps.suPermission.service.PermissonHelper;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.CookieUtils;
import com.zinglabs.util.JsonUtils;
/**
 * 权限模块
 * @author Guozhiyuan
 *
 */
@Controller
@RequestMapping(value="/*/permissonMappingRole")
public class PermissonAction  extends BaseAction{
    /**
     * 授权
     * 
     */
	@RequestMapping(value="/addPermisson")
	public void addPermisson(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		String roleId=map.get("roleCode")==null?"":map.get("roleCode").toString();
			   //转码
			   roleId = I18nPromptUtils.ascii2Native(roleId);
		String type=map.get("type")==null?"":map.get("type").toString();
		String permisstionCode=map.get("permisstionCode")==null?"":map.get("permisstionCode").toString();
		String permisstionCodes[]=permisstionCode.split(",");
		
		if("".equals(roleId)||"".equals(type)||permisstionCodes.length<=0){
			 postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少参数").toString(), response);
			 return;
		}
		//先删
		PermissonHelper.deauthorizeByRoleCode(roleId, type);
		//后加
		PermissonHelper.roleMapPermisstion(permisstionCodes, roleId, type);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "添加成功").toString(), response);

	}
	
	
    /**
     * 功能菜单授权
     * 
     */
	@RequestMapping(value="/addMenuPermisson")
	public void addmenuPermisson(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		String roleId=map.get("roleCode")==null?"":map.get("roleCode").toString();
		String delCodes=map.get("delCodes")==null?"":map.get("delCodes").toString();
		String type=map.get("type")==null?"":map.get("type").toString();
		String permisstionCode=map.get("permisstionCode")==null?"":map.get("permisstionCode").toString();
		String permisstionCodes[]=permisstionCode.split(",");
		String delCode[]=delCodes.split(",");
		
		if("".equals(roleId)||"".equals(type)||permisstionCodes.length<=0){
			 postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少参数").toString(), response);
			 return;
		}
		if(delCode.length>0){
			//先删
			//PermissonHelper.deauthorizeByRoleCode(roleId, type);
			PermissonHelper.deauthorize(roleId, type, delCode);
		}
		
		//后加
		PermissonHelper.roleMapPermisstion(permisstionCodes, roleId, type);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "添加成功").toString(), response);

	}
	/**
	 * 获取权限根据 角色id和type
	 * @param map
	 * @param request
	 * @param response
	 * 用于授权时,获取该角色已授权数据
	 */
	@RequestMapping(value="/getPermisson")
	public void getPermisson(HttpServletRequest request,HttpServletResponse response){
		String roleId=request.getParameter("roleCode")==null?"":request.getParameter("roleCode").toString();
		String type=request.getParameter("type")==null?"":request.getParameter("type").toString();
	
		List<String> list=PermissonHelper.getPermisstionCodeByRole(roleId,type);
		
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",list).toString(), response);
		
	}
	/**
	 * 获取权限根据 角色id和type
	 * @param map
	 * @param request
	 * @param response
	 * 用于授权时,获取该角色已授权数据
	 */
	@RequestMapping(value="/getPermissonCode")
	public void getPermissonCode(HttpServletRequest request,HttpServletResponse response){
		String roleId=request.getParameter("roleCode")==null?"":request.getParameter("roleCode").toString();
		String type=request.getParameter("type")==null?"":request.getParameter("type").toString();
		List<String> list=PermissonHelper.getPermisstionCodeByRole(roleId,type);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",list).toString(), response);
		
	}
	/**
	 * 获取用户全部权限 
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getAllPermisson")
	public void getAllPermisson(HttpServletRequest request,HttpServletResponse response){
		String loginName;
		try {
			loginName = CookieUtils.getCookieVal(request);
			if(loginName!=null&&!"".equals(loginName)){
				 Map<String,List> map=PermissonHelper.getPermisstionByLoginName(loginName);
				 //TODO 获取页面权限
			     postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",map).toString(), response);	
			}else{
				 postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户没有权限").toString(), response);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
}
