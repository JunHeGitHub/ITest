package com.zinglabs.apps.i18nPrompt;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class I18nLayoutService extends BaseService {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");

	/**
	 * 查询出对应的页面文字及按钮文字数据
	 * @param map map中包含langugeCdoe（语言代码） tableName（对应数据表名） dataType（该条数据的类型）
	 * @return 返回数据集
	 */
	public List<Map<String, String>> SelectDateForLayout(Map<String, String> map){
		List list=getServiceSqlSession().db_selectList("SelectDateForLayout",map);
		return list;
	}
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		// TODO Auto-generated method stub
		return (AppSqlSessionDbid)this.getBean("i18nPromptSqlSession");
	}
	
	
}
