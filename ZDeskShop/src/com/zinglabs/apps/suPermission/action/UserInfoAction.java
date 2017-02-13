package com.zinglabs.apps.suPermission.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zinglabs.db.MHBO;
import com.zinglabs.util.CookieUtils;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



import com.zinglabs.apps.suPermission.filter.SuSecurityUserFilter;
import com.zinglabs.apps.suPermission.service.UserInfoService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.db.CryptoTools2;
import com.zinglabs.util.DESUtil;
import com.zinglabs.util.JsonUtils;

import com.qq.weixin.sdk.util.ATUtil;
import com.zinglabs.log.LogUtil;
import com.qq.weixin.sdk.util.CardBO;

@RequestMapping(value = "/*/UserInfo")
@Controller
public class UserInfoAction extends BaseAction {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");


	/*
	 * // 过滤掉没用的参数 private static void filterDataTablePat(Map map) { if
	 * (map.get("_time_stamp_") != null) { map.remove("_time_stamp_"); }
	 * handlerPagingParameters(map); } // 处理分页参数 private static void
	 * handlerPagingParameters(Map map) { if (map.get("start") != null) { String
	 * limit = (String) map.get("start") + ',' + (String) map.get("limit");
	 * map.put("limit", limit); map.remove("start"); } }
	 */
	/**
	 * 通用查询用户信息（用户、角色、组织）
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/Find")
	public void find(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		// filterDataTablePat(map);
		try {
			Map rmap = this.getService().getListForUser(map);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);

		} catch (Exception e) {
			logger.error("common find fail");
			e.printStackTrace();
		}
		// AppSqlSession
		// ass=(AppSqlSession)this.getBean("zkmYongHuXinXiSqlSession", request);
		// List list=ass.selectList("getYongHuList",map);
		// postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,
		// "",list),response);
	}
	/**
	 * 根据 loginName 获取用户信息，用户、组织、角色
	 */
	@RequestMapping(value="/getUserByLoginName")
	public void getUserByLoginName(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
        String loginName=(String)map.get("loginName");
		Map rmap=UserInfoService.getUserInfoByLoginName(loginName);
		 postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rmap),response);
	}
	/**
	 * 根据roleId获取loginName集合
	 * @param request
	 * return
	 */
	@RequestMapping(value="/getLoginNameList")
	public void getLoginNameListFromRoleId(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		try {
			Map rmap = this.getService().getLoginNameListFromRoleId(map);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		} catch (Exception e) {
			logger.error("common find fail");
			e.printStackTrace();
		}
	}
	/**
	 * 保存用户角色关联
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/saveUserRole")
	public void saveUserRole(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		try {
			Map rmap = this.getService().saveUserRole(map);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		} catch (Exception e) {
			logger.error("common find fail");
			e.printStackTrace();
		}
	}
	/**
	 * 登陆
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/login")
    public void login(HttpServletRequest request,HttpServletResponse response){
        String userName=request.getParameter("loginName");
        String password=request.getParameter("password");

        try {

//           登陆比较特殊 需要清除cookie dbid 防止登出后登陆其它docker用户造成影响
            CookieUtils.releaseMyDBId();
            String cardUrl=MHBO.getMHCardUrlByZDeskUser(userName);
			if(cardUrl==null||"nocardurl".equals(cardUrl)){
				LogUtil.error("", logger);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "你输入的用户名或密码错误"), response);
				return;
			}

//            companyId就是dbid
            String companyId=MHBO.loadUserCompany(cardUrl,userName);

            LogUtil.debug(" UserInfoAction : login "+cardUrl+" "+companyId+" "+userName,logger);

            if(!StringUtils.isEmpty(companyId)){
//                因为登陆时，cookie没有设置，所以需要手动设置，校验用户要在docker中进行
//                后续查询都使用companyId对应的dbid
                CookieUtils.cookieDBId2Mem(null,companyId);
            }

//查询用户信息

//        TODO check用户名密码，getUserInfoByLoginName，组织机构角色信息都获取没必要，浪费资源
			Map map=UserInfoService.getUserInfoByLoginName(userName);



            if(map!=null&&!map.isEmpty()){
                String pwd=(String) map.get("pwd");
                if(pwd.equals(password)){
                    putLoginName2Cookie(userName,response);
//                  TODO getMHCardUrlByZDeskUser应该已调整到ZDeskLib中，但ZDeskR没引用ZDeskLib.jar

//                    cookie格式 mhdtoken=cardUrl_ZSP_companyId_ZSP_时间戳。
//                    String cookieTmp= ATUtil.setUserCookie(cardUrl,response);

//                    需要切换dbid
                    CookieUtils.releaseMyDBId();
                    String cookieTmp=CookieUtils.setUserCookie(cardUrl,userName,response);
                            LogUtil.debug("zdesk login cookieTmp:"+cookieTmp,logger);
                    CookieUtils.cookieDBId2Mem(null,companyId);


//                  TODO 这里需要根据companyId替换掉所有涉及的dbid,以便docker中使用

                    postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "登陆成功",map),response);
                }else{
                    postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户名或密码错误"),response);
                }
            }else{
                postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户名不存在"),response);
            }

            if(!StringUtils.isEmpty(companyId)){
                CookieUtils.releaseMyDBId();
            }
        }catch (Exception e){
            LogUtil.error(e,logger);
            postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "登陆失败"),response);
        }
    }
	/**
	 * 根据用roleId中得到loginName数组筛选用户信息
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getUserByScreen")
	public void getUserByRoleId(@RequestParam HashMap map, HttpServletRequest request,HttpServletResponse response){
		Map loginNameMap=this.getService().getLoginNameListFromRoleId(map);
		Map reMap = this.getService().getUserByScreen(loginNameMap);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "查找成功",reMap),response);
	}
	/*
	 * 根据用户ID删除用户
	 */
	@RequestMapping(value="/deleteUserById")
	public void deleteUserById(@RequestParam HashMap map, HttpServletRequest request,HttpServletResponse response){
		getService().deleteUserById(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功"),response);
	}
	/*
	 * 根据loginName删除角色关联
	 */
	@RequestMapping(value="/deleteUserRoleByLoginName")
	public void deleteUserRoleByLoginName(@RequestParam HashMap map, HttpServletRequest request,HttpServletResponse response){
		getService().deleteUserRoleByLoginName(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功"),response);
	}
	/*
	 * 根据loginName删除用户关联
	 */
	@RequestMapping(value="/deleteUserOrgByLoginName")
	public void deleteUserOrgByLoginName(@RequestParam HashMap map, HttpServletRequest request,HttpServletResponse response){
		getService().deleteUserOrgByLoginName(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功"),response);
	}
	/*
	 * 条件查询用户信息
	 */
	@RequestMapping(value="/searchUser")
	public void searchUser(@RequestParam HashMap map, HttpServletRequest request,HttpServletResponse response) throws Exception{
		SuSecurityUserFilter filter = new SuSecurityUserFilter();
		map = (HashMap) filter.freeFilter(map, request);
		Map rmap = getService().searchUser(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rmap),response);
	}
	/**
	 * 测试
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/test")
	public void Test(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		List<String> list=UserInfoService.getRoleListByLoginName("1");
		for(String o:list){
			System.out.println(o);
		}
	}
	
	
    /**
     * 
     * cookie 存放  统一管理
     * 先用时间时间戳当秘钥 
     * 进行加密存放 防止cookie盗用
     *  
     */
    public static void putLoginName2Cookie(String str,HttpServletResponse response){
    	    //秘钥
            long utoken = System.currentTimeMillis();
            
            try {
            	str=str+"_ZSP_"+utoken;
            	//
                CryptoTools2 ctools = new CryptoTools2();
    			str = ctools.encode(str);
    			//logger.debug("setCookie  tempKey："+str);
    			
				String eStr=DESUtil.encodeUTF2(str);
				
				logger.debug("setCookie  加密后字符串："+eStr);
				
			    Cookie loginName=new Cookie("utoken",eStr);
			    loginName.setPath("/");
			    loginName.setMaxAge(3652460*60);
			    response.addCookie(loginName);
			   // Cookie loginName=new Cookie("loginName",String.valueOf(utoken));
			    
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("CookieUtils setCookie :"+e.getMessage());
			}
    	
    }
	
	public UserInfoService getService() {
		return (UserInfoService) getBean("userInfoService");
	}
}
