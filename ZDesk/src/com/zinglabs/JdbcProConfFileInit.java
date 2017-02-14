package com.zinglabs;

import java.util.Properties;
import com.zinglabs.util.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;

public class JdbcProConfFileInit {
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
	public static void init(){
		//对数据库配置文件指向重新赋值
		if(AppConfs.APP_JDBC_PRO_FILE!=null && AppConfs.APP_JDBC_PRO_FILE.length()>0){
			LogUtil.debug("change CONFIG_FILE_PATH_PROPERTIES to : " + AppConfs.APP_JDBC_PRO_FILE, SKIP_Logger);
			BaseAbs.CONFIG_FILE_PATH_PROPERTIES=AppConfs.APP_JDBC_PRO_FILE;
		}
		
		if(AppConfs.APP_JDBC_PRO_FILE_DEFAULT!=null && AppConfs.APP_JDBC_PRO_FILE_DEFAULT.length()>0){
			LogUtil.debug("change CONFIG_FILE_PATH_PROPERTIES2 to : " + AppConfs.APP_JDBC_PRO_FILE_DEFAULT, SKIP_Logger);
			BaseAbs.CONFIG_FILE_PATH_PROPERTIES2=AppConfs.APP_JDBC_PRO_FILE_DEFAULT;
		}
		SKIP_Logger.debug("generate DAOTools.appId ... ...");
		try{
	    	//主键生成策略配置
			Properties pconf =PropertiesUtils.getProperties(AppConfs.APP_JDBC_PRO_FILE);
			DAOTools.appId=Long.valueOf(String.valueOf(pconf.get("zdeskr"))) ;
		}catch (Exception e) {
			SKIP_Logger.error("AppConfs.APP_JDBC_PRO_FILE " + AppConfs.APP_JDBC_PRO_FILE + " error ... ... ",e);
			try{
				Properties pconf =PropertiesUtils.getProperties(AppConfs.APP_JDBC_PRO_FILE_DEFAULT);
				DAOTools.appId=Long.valueOf(String.valueOf(pconf.get("zdeskr"))) ;
			}catch (Exception x) {
				SKIP_Logger.error("AppConfs.APP_JDBC_PRO_FILE_DEFAULT " + AppConfs.APP_JDBC_PRO_FILE_DEFAULT + " error ... ... ",e);
			}
		}
		SKIP_Logger.debug("generate DAOTools.appId ... ... value is " + DAOTools.appId);
		
		DAOTools.initAllStatic();
	}
	
}
