//package com.zinglabs.db;
//import java.util.HashMap;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import javax.servlet.http.HttpServletRequest;
//
//
//public class UserInfo {
//	private static Logger logger = Logger.getLogger(UserInfo.class);
//	public String userName;
//	public String loginName;
//	public String userPassword;
//	public String userRole;
//	public String clientId;
//	public String userIP;
//	public String phoneNumber;
//	public String organizationCode;
//	public String userAlias1;
//	public String userAlias2;
//	public String userAlias3;
//	public String userAlias4;
//	public String userAlias5;
//	public long userId;
//	
//	public Long loginTime;
//	public Long laseActionTime;
//	
//	public String phyName;
//	
//	public HashMap<String, String> userResMap;
//	
//	public HashMap<String, DataFilter> tableFilterMap;
//	
//	public static AtomicInteger loginNum=new AtomicInteger(0);
//	
//	public static ConcurrentHashMap<String, UserInfo> allUserInfo=new ConcurrentHashMap<String, UserInfo>();
//	
//	public static ConcurrentHashMap<String, UserInfo> allClientIdUserInfo=new ConcurrentHashMap<String, UserInfo>();
//	
//	public static UserInfo getUser(HttpServletRequest request){
//		String userName=request.getParameter("zkm_userName")==null?"":request.getParameter("zkm_userName");
//		if(userName.length()<=0){
//			userName=request.getAttribute("zkm_userName")==null?"":request.getParameter("zkm_userName");
//		}
//		if(request.getSession().getAttribute("userName")!=null){
//			userName =request.getSession().getAttribute("userName").toString();
//		}
//		if(userName==null || "".equals(userName)){
//			request.getRequestDispatcher(request.getContextPath() + "/login.html");
//		}
//		return getUser(userName,request);
//	}
//	
//	public static UserInfo getUser(String loginName,HttpServletRequest request){
//		UserInfo user=null;
//		try{
//			if(loginName!=null && UserInfo.allUserInfo.containsKey(loginName)){
//				user=UserInfo.allUserInfo.get(loginName);
//			}
//		}catch(Exception x){
//			logger.error("获取用户信息错误....");
//			user=null;
//		}
//		//TODO:没有用户登录信息时的处理
//		if(user==null){
//			user=new UserInfo();
//			user.loginName="_guest";
//			user.userName="访客";
//			user.userIP=request.getRemoteAddr();
//		}
//		return user;
//	}
//	
//}
