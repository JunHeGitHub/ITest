package com.zinglabs.remote.server.implDef;

import java.util.Map;
/**
 * 数据库更新多步操作事务保障接口
 * @author QCF
 *
 */
public interface DBAffairsImpDef {
	/**
	 * 实现类参数，在execute方法参数MAP中必须有该参数
	 * 
	 * 程序会根据该参数获取接口实现类
	 */
	public static final String CLASS_KEY="ACHIEVE_CLASS_";
	
	public Map execute(Map params);
}
