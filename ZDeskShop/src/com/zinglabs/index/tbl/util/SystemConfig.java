

package com.zinglabs.index.tbl.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




public class SystemConfig {

	
	public static final String permittedList="permittedList";
	public static final String modulesList="modulesList";
	public static final String noPermitted="nopermitted";
	
	private static HashMap hmp = new HashMap();

	private static Log log = LogFactory.getLog(SystemConfig.class);
	
	private SystemConfig(){
	}
	
	
	public static String getProperty(String key,String configFileName){
		Properties properties = (Properties)hmp.get(configFileName);
		if(properties == null){
			properties = loadConfigProperties(configFileName);
		}
		String value = properties.getProperty(key);
		return StrgUtil.convertEncode(value,"ISO-8859-1","UTF-8");
	}
	
	
	public static String getProperty(String key,String defaultValue,String configFileName){
		Properties properties = (Properties)hmp.get(configFileName);
		if(properties == null){
			properties = loadConfigProperties(configFileName);
		}
		String value = properties.getProperty(key,defaultValue);
		
		return StrgUtil.convertEncode(value,"ISO-8859-1","UTF-8");
	}
	
	
	public static String getProperty(String key,String[] args,String configFileName){
		String value = getProperty(key,configFileName);				
		value = getArguementString(value,args);
		
		return value;
	}
	
	
	public static String getArguementString(String value,String[] args){
		
		for(int i = 0;i < args.length;i++){			
			value = value.replace("{" + String.valueOf(i) + "}",args[i]);
		}
		
		return value;
	}
	
	public static Properties getProperty(String configFileName){
		Properties properties = (Properties)hmp.get(configFileName);
		if(properties == null){
			properties = loadConfigProperties(configFileName);
		}
		return properties;
		
	}
	
	
	
	
	private static synchronized Properties loadConfigProperties(String configFileName){
		Properties properties = (Properties)hmp.get(configFileName);
		if(properties == null){
			try{						
				ClassLoader cl = Thread.currentThread().getContextClassLoader();
				InputStream is = cl.getResourceAsStream(configFileName);
				properties = new Properties();
				properties.load(is);
				hmp.put(configFileName, properties);
				is.close();
			}catch(Exception e){				
				log.error("读取系统配置文件(" + configFileName + ")的信息时出现错误");
			}
		}
		return properties;
	}
	
	public static void removeObject(String key){
		if(hmp!=null)hmp.remove(key);
	}
}
