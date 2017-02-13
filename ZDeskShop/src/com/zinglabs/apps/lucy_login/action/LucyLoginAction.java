package com.zinglabs.apps.lucy_login.action;


import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.db.DAOTools;
import com.zinglabs.util.DESUtil;
import com.zinglabs.util.JsonUtils;


@Controller
@RequestMapping(value = "/ZKM/Lucy")
public class LucyLoginAction extends BaseAction {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
    @RequestMapping(value="/login")
	public void login(HttpServletRequest request,HttpServletResponse response){
    	String user = request.getParameter("index_username");
		String sql = "SELECT * FROM DataItemAllocation WHERE peizhiItem ='ZKM_LUCY_LoginName'";
		String s[] = null;
		String s1 = "";
		HttpSession session=request.getSession();
		try {
			List<Map> list = DAOTools.queryMap(sql, s, "ZDesk");
			if(!list.isEmpty()){
				s1 = list.get(0).get("peizhiItemValue").toString();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(s1.equals(user)) {
			String cookieToken = user + "_ZSP_" + System.currentTimeMillis()
			+ "_ZSP_login";
	        try {
				cookieToken = DESUtil.encodeUTF2(cookieToken);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Cookie cookie =new Cookie("token",cookieToken);
			cookie.setPath("/");
			cookie.setMaxAge(60*3);
			response.addCookie(cookie);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "登录成功"), response);
		}else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "登录失败"), response);
		}
         
    	return ;
	}
}
