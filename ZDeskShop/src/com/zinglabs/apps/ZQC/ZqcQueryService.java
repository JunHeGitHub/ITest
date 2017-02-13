package com.zinglabs.apps.ZQC;

import java.util.List;
import java.util.Map;

import com.zinglabs.apps.ZQC.action.ZqcQueryAction;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class ZqcQueryService extends BaseService{

	//根据条件查询
	public List<Map<String,String>> searchWenBenFenPeiInfo(Map<String, String> map){
		List list = getServiceSqlSession().db_selectList("queryZQCInfo",map);
		return list;
	}
	//根据选中数据查询对应的数据
	public List<Map<String,String>> searchExcelData(Map<String, String> map){
		List list = getServiceSqlSession().db_selectList("propEXCEL",map);
		return list;
	}
	//根据选grade_index获取reference2_name
	public List<Map<String,String>> searchDictinfo(Map<String, String> map){
		List list = getServiceSqlSession().db_selectList("queryDictinfo",map);
		return list;
	}
	
	public List<Map<String,String>> searchgetTotalRow(Map<String, String> map){
		List list = getServiceSqlSession().db_selectList("getTotalRow",map);
		return list;
	}

	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) getBean("zqcQuerySqlsession");
	}	
}
