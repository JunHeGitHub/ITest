package com.zinglabs.apps.suPermission.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.suPermission.service.MH_ZDeskUserCompany;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.db.MHBO;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.CookieUtils;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping(value = "/*/MH_ZDeskUserCompany")
public class MH_ZDeskUserCompanyAction extends BaseAction {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");

	/**
	 * 验证用户是否存在MH_ZDeskUserCompany表中,判别是否可进行新增
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/CheckUserIsHaveMH")
	public void checkUserIsHaveMH(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			//CookieUtils.pauseMyMemDBId();
			String loginName=request.getParameter("loginName");
//	        String companyId=request.getParameter("companyId");
			Map searchMap = new HashMap() ;
			searchMap.put("loginName", loginName) ;
			List<Map> SelDataLoginName = this.getService().findMHUserByLoginName(map);
			//CookieUtils.startMyMemDBId() ;
			LogUtil.debug("select MH_ZDeskUserCompany "+SelDataLoginName);
			if (SelDataLoginName.size()!=0) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,""), response);
			} else {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"", SelDataLoginName), response);
			}
		} catch (Exception e) {
			LogUtil.error(e) ;
			LogUtil.error("select MH_ZDeskUserCompany Exception:");
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,	"异常"), response);
		}
	}

	/**
	 * 新增用户到MH_ZDeskUserCompany表中
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/AddUserOfMH")
	public void addUserOfMHAct(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			//CookieUtils.pauseMyMemDBId();
			String userName=request.getParameter("loginName");
	        String companyId=request.getParameter("companyId");
	        Map insertmap = new HashMap() ;
	        insertmap.put("loginName", userName) ;
	        insertmap.put("companyId", companyId) ;
	        LogUtil.debug(" MH_ZDeskUserCompanyAction : addUser  MH_ZDeskUserCompany "+userName+" "+companyId+""+insertmap,logger);
			int inscount = this.getService().addUserOfMHSer(insertmap);
			
			if (inscount != 0) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"添加成功", inscount), response);
			} else {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"添加失败"), response);
			}
		} catch (Exception e) {
			LogUtil.error(e) ;
			LogUtil.error("insert MH_ZDeskUserCompany Exception:");
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"添加失败"), response);
		}finally{
			//CookieUtils.startMyMemDBId();
		}
	}
	
	/**
	 * 验证用户是否已绑定微信
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/CheckUserIsIsBinded")
	public void checkUserIsBinded(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			//CookieUtils.pauseMyMemDBId();
			List<Map> SelDataLoginName = this.getService().findMHuserByLoginNameIsBinding(map);
			CookieUtils.startMyMemDBId() ;
			LogUtil.debug("select chatWorkflow_binding "+SelDataLoginName);
			if (SelDataLoginName.size()==0) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,""), response);
			} else {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"", SelDataLoginName), response);
			}
		} catch (Exception e) {
			LogUtil.error(e) ;
			LogUtil.error("select chatWorkflow_binding Exception:");
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,	"异常"), response);
		}
	}
	
	/**
	 * 删除用户MH_ZDeskUserCompany表中的数据
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/deleteUserOfMH")
	public void deleteUserOfMHAct(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			//CookieUtils.pauseMyMemDBId();
			String userName=request.getParameter("loginName");
	        String companyId=request.getParameter("companyId");
	       // Map deletemap = new HashMap() ;
	        //deletemap.put("loginName", userName) ;
	        //deletemap.put("companyId", companyId) ;
	        LogUtil.debug(" MH_ZDeskUserCompanyAction : deleteUser  MH_ZDeskUserCompany "+userName+" "+companyId+"",logger);
			int inscount = this.getService().deleteMH_UserOfMHSer(map);

			if (inscount != 0) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"删除成功", inscount), response);
			} else {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"删除失败"), response);
			}
		} catch (Exception e) {
			LogUtil.error(e) ;
			LogUtil.error("delete MH_ZDeskUserCompany Exception:");
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"删除失败"), response);
		}finally {
			//CookieUtils.startMyMemDBId();
		}
	}

	/**
	 * 判断是否为docker模式,并获取当前用户companyId
	 * 
	 * */
	@RequestMapping(value = "/isDocker")
	public void isDocker(@RequestParam	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		String userName=request.getParameter("loginName");
		try {
			String cardUrl=MHBO.getMHCardUrlByZDeskUser(userName);
			String companyId=MHBO.loadUserCompany(cardUrl,userName);
			LogUtil.debug(" MH_ZDeskUserCompanyAction : isDocker "+cardUrl+" "+companyId+" "+userName,logger);
			Map rmap = new HashMap () ;
			if(!StringUtils.isEmpty(companyId)){
				rmap.put("companyId", companyId) ;
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"", rmap), response);
				return ;
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"", rmap), response);
				return ;
			}
		} catch (Exception e) {
			LogUtil.error(e) ;
			LogUtil.error("select MH_ZDeskUserCompany Exception:");
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"异常,不可新增"), response);
		}finally {
		}
	}
	
	/**
	 * 删除MH_ZDeskUserCompany用户
	 */
	public MH_ZDeskUserCompany getService() {
		return (MH_ZDeskUserCompany) getBean("MH_ZDeskUserCompanyService");
	}
}
