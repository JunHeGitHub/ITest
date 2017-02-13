package com.zinglabs.aops;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.util.CookieUtils;
import com.zinglabs.util.JsonUtils;

public class AppsActionMethodAop{
	public static final String TT_SESSION_USER_KEY_="ttSessionUser_";
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
    public Object appsActionBeforeMethod(ProceedingJoinPoint pjp) throws Throwable{
        Object [] params=pjp.getArgs();
        //UserInfo2 user=null;
        HttpServletRequest request=null;
        HttpServletResponse response=null;
        if(params!=null){
        	for(int i=0;i<params.length;i++){
        		Object obj=params[i];
        		if(obj instanceof HttpServletRequest){
        			request=(HttpServletRequest)obj;
        		}
        		if(obj instanceof HttpServletResponse){
        			response=(HttpServletResponse)obj;
        		}
        		
        	}
        }
    	
    	String loginName= CookieUtils.getCookieVal(request);
    	logger.debug("aop -----登陆用户名 loginName====="+loginName);
    	if("".equals(loginName)||loginName==null){
    		response.setContentType("text/html;charset=UTF-8");
    	    PrintWriter out=response.getWriter();
    	    //out.print("{\"name:'test'\"}");
    	    out.write(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限",""));
    	    out.flush();
    	    out.close();
    	    
    		return null;
    	}
    	request.setAttribute(TT_SESSION_USER_KEY_, loginName);
        Object object = pjp.proceed();
        return object;  
    }
}
