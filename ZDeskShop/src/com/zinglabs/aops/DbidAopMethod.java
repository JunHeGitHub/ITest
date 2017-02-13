package com.zinglabs.aops;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zinglabs.log.LogUtil;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.util.CookieUtils;

public class DbidAopMethod {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
 
	public void setThredDbidToMem(ProceedingJoinPoint pjp) throws Throwable {
		//开关
	 	CookieUtils.isUseMemCookieDBId = false;
		logger.debug("CookieUtils.isUseMemCookieDBId=" + CookieUtils.isUseMemCookieDBId);
		// 设置dbid
		if (CookieUtils.isUseMemCookieDBId) {

			logger.debug("当前线程id ---" + Thread.currentThread().getId());
			Object[] params = pjp.getArgs();
			HttpServletRequest request = null;
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					Object obj = params[i];
					if (obj instanceof HttpServletRequest) {
						request = (HttpServletRequest) obj;
						break;
					}
				}
			}

            logger.debug("spring action:"+request.getServletPath()+" aop ------------ 设置 dbid");
			// 如果请求参数有idbid idbid 优先
			String importDBId = request.getParameter("idbid");
			if (!StringUtils.isEmpty(importDBId)) {
				logger.debug("idbid优先---dbid="+importDBId );
				CookieUtils.cookieDBId2Mem(null, importDBId);
			} else {
				logger.debug("cookie dbid 优先");
				try {
					//CookieUtils.cookieDBId2Mem(null, "ZDesk");
					CookieUtils.cookieDBId2Mem(request, null);
				} catch (Exception e) {
					logger.debug("错误 未从cookie 中取到 dbid");
				}

			}
             
			pjp.proceed();

			logger.debug("aop ------------- 释放dbid "+Thread.currentThread().getId());
			// 设置dbid
			CookieUtils.releaseMyDBId();
		} else {
			pjp.proceed();
		}

	}

}
