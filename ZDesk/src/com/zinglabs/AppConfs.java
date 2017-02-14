package com.zinglabs;

/**
 * 项目上用的一些静态变量或配置用的类
 * @author QCF
 *
 */
public class AppConfs {
	/**
	 * 与baselib BaseInterface 中的CONFIG_FILE_PATH_PROPERTIES 对应
	 * 若设置为空或空串则使用baselib中默认的值
	 * 否则在加载时servlet 类com.zinglabs.servlet.ConfCommandServlet会将这里设定的值赋值给 CONFIG_FILE_PATH_PROPERTIES
	 * 
	 * 其主要作用是允许项目工程配置自已的数据库指向文件的名称与位置
	 * 
	 */
	public static final String APP_JDBC_PRO_FILE="/mnt/zdeskr.properties";
	/**
	 * 与baselib BaseInterface 中的CONFIG_FILE_PATH_PROPERTIES2 对应
	 * 若设置为空或空串则使用baselib中默认的值
	 * 否则在加载时servlet 类com.zinglabs.servlet.ConfCommandServlet会将这里设定的值赋值给 CONFIG_FILE_PATH_PROPERTIES2
	 * 
	 *  其主要作用是允许项目工程配置自已的数据库指向文件的名称与位置
	 * 
	 */
	public static final String APP_JDBC_PRO_FILE_DEFAULT="/mnt/zdesk.properties";
	
	
}
