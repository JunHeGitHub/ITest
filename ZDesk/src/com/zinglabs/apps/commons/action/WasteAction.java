package com.zinglabs.apps.commons.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zinglabs.util.CookieUtils;

/**
 * 为什么会有这样的代码我也不知道
 * 
 * 
 * 
 * 
 */
@Controller
@RequestMapping(value = "/*/WasteDelCookie")
public class WasteAction {
	@RequestMapping(value="delUserCookie")
	public String delUserFromCookie(HttpServletRequest request, HttpServletResponse response) {

		try {
			CookieUtils.delUserCookie(response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//请求验证码 --！
		return "redirect:/ZKM/validateCode/randomCode.action?"+System.currentTimeMillis(); 

	}

}
