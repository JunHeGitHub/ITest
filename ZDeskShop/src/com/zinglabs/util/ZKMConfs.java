package com.zinglabs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.log.LogUtil;
import com.zinglabs.util.solrj.SolrjUtil;

public class ZKMConfs {
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	
	public static Properties rootConfs;
	
	public static Properties confs;
	
	public static final String LUCENE_CONF_NAME="luceneConf.properties";
	
	static{
		init();
	}
	
	public static void init(){
		LogUtil.debug("init rootConfs......",SKIP_Logger);
		InputStream is = ZKMConfs.class.getClassLoader().getResourceAsStream(LUCENE_CONF_NAME);
		try{
			rootConfs=new Properties();
			confs=new Properties();
			rootConfs.load(is);
			is.close();
			LogUtil.debug("init rootConfs......end",SKIP_Logger);
			if(rootConfs!=null){
				LogUtil.debug("init confs......",SKIP_Logger);
				String cpath=rootConfs.getProperty("propertiesPath","/mnt/luceneConfs.properties");
				if(cpath!=null && cpath.length()>0){
					File cfile=new File(cpath);
					if(cfile.exists() && cfile.isFile() && cfile.canRead()){
						InputStream cin=new FileInputStream(cfile);
						confs.load(cin);
						cin.close();
						LogUtil.debug("init confs......end",SKIP_Logger);
					}else{
						LogUtil.error("---- 知识库主配置文件不存在或无法读取" + cpath,SKIP_Logger);
					}
				}else{
					LogUtil.error("---- 未找到知识库主配置文件配置项" + cpath,SKIP_Logger);
				}
			}
		}catch(Exception x){
			LogUtil.error("初始化知识库参数失败。",SKIP_Logger);
			LogUtil.error(x,SKIP_Logger);
		}
	}
	
	public static void main(String [] args){
		System.out.println(ZKMConfs.confs.get("mergeTempDir"));
	}
}
