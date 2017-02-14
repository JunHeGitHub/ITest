package com.zinglabs.apps.Dictmanager;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.apps.i18nPrompt.I18nPromptUtils;

public class DictmanagerService extends BaseService {
	public void deleteDicData (List<String> list){
		getServiceSqlSession().db_delete("deleteDicData",list) ;
		DictmanagerUtils.reloadDicData() ;
	}
	public List searchDicData (Map<String,String> map){
		List list =getServiceSqlSession().db_selectList("selectDicData",map) ;
		return  list;
	}
	public List selectDataforUpdate(Map<String,String> map){
		return getServiceSqlSession().db_selectList("selectDataforUpdate",map);
	}
	public void insertDicData (Map map){
		getServiceSqlSession().db_insert("insertDicData",map) ;
		DictmanagerUtils.reloadDicData() ;
	}
	public void updateDicData(Map map){
		getServiceSqlSession().db_update("update",map) ;
		DictmanagerUtils.reloadDicData() ;
	}
	public List selectIndexData(Map<String,String> map){
		return getServiceSqlSession().db_selectList("selectDicIndex",map) ;
	}
	public void insertDicIndex (Map map){
		getServiceSqlSession().db_insert("insertDicIndex",map) ;
	}
	public void deleteDicIndex (List<String> list){
		getServiceSqlSession().db_delete("deleteDicIndex",list) ;
	}
	public void updateDicIndex(Map map){
		getServiceSqlSession().db_update("updateIndex",map) ;
	}
	public void updateDicDataByIndex(Map map){
		getServiceSqlSession().db_update("updateDicDataByIndex",map) ;
	}
	public List selectIndexforUpdate(Map<String,String> map){
		return getServiceSqlSession().db_selectList("selectIndexforUpdate",map) ;
	}
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) this.getBean("DictmanagerSqlSession");
	}
	public static void main(String[] args){
		
	}
}
