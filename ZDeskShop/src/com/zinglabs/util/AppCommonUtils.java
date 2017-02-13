package com.zinglabs.util;

import java.util.HashMap;
import java.util.Map;


import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.aops.AppsActionMethodAop;


public class AppCommonUtils {
	private static Map<String,Logger> loggerMap = new ConcurrentHashMap<String,Logger>();
	/**
	 * 获取日志操作类
	 * @param key 可为ZDesk ZKM等
	 * @return
	 */
	public static Logger getLogger(String key){
		Logger logger = loggerMap.get(key);
		if(logger == null){
			logger = LoggerFactory.getLogger("ZDesk");
			loggerMap.put(key,logger);
		}
		return logger;
	}
	/**
	 * 获取日志操作类（向ZDesk.log中添加日志）
	 * @return
	 */
	public static Logger getLogger(){
		return getLogger("ZDesk");
	}
	/**
	 * 获取cookie工具类，此工具类提供对cookie的操作以及从cookie中获取一些通用信息，如：dbid、用户名等。
	 * @return
	 */
	public static CookieUtils getCookieUtils(){
		return new CookieUtils();
	}
	
	/**
	 * 业务函数  获取用户登录名称
	 * @param request
	 * @return
	 */
	public static String  getLoginName(HttpServletRequest request){
		String loginName=(String)request.getAttribute(AppsActionMethodAop.TT_SESSION_USER_KEY_);
		return loginName;
	}

}
