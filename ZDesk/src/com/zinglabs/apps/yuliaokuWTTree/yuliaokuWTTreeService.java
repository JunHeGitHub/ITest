package com.zinglabs.apps.yuliaokuWTTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.base.core.frame.BaseService;

public class yuliaokuWTTreeService extends BaseService {
	
	/**
	 * 获取对应类别数据
	 * @param map
	 * @return
	 */
	public List<Map> getDataList(Map map) {
		return (List<Map>)getServiceSqlSession().db_selectList("selectDataNotToTree", map);
	}
	
	/**
	 * 获取对应类别数据条数
	 * @param map
	 * @return
	 */
	public Map getDataListCount(Map map) {
		return getServiceSqlSession().db_selectOne("selectDataNotToTreeCount", map);
	}
	
	/**
	 * 获取绑定的对应类别数据
	 * @param map
	 * @return
	 */
	public List<Map> getDataListToTree(Map map) {
		return (List<Map>)getServiceSqlSession().db_selectList("selectDataToTree", map);
	}
	
	/**
	 * 获取绑定的对应类别数据条数
	 * @param map
	 * @return
	 */
	public Map getDataListCountToTree(Map map) {
		return getServiceSqlSession().db_selectOne("selectDataToTreeCount", map);
	}
	
	/**
	 * 批量修改对应数据
	 * @param map
	 * @return
	 */
	public int updDataSome(Map map){
		return ((Integer)getServiceSqlSession().db_update("UpdDataToTree", map)).intValue();
		
	}
	/**
	 * 批量删除数据
	 * @param map
	 * @return
	 */
	public int delDataSome(Map map){
		return ((Integer)getServiceSqlSession().db_update("DelDataToTree", map)).intValue();
		
	}
	
	/**
	 * 批量删除数据(删除节点时)
	 * @param map
	 * @return
	 */
	public int delDataSomeOfDelNode(Map map){
		return ((Integer)getServiceSqlSession().db_update("DelDataToTreeOfDelNode", map)).intValue();
		
	}
	
	
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) this.getBean("yuliaokuWTTreeSqlSession");
	}
}
