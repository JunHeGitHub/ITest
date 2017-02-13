package com.zinglabs.apps.Category;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import atg.taglib.json.util.JSONArray;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class CategoryServices extends BaseService {

	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	
	
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) getBean("CategorySqlSession");
	}
	
	/**
	 * 分类和添加商品左侧树，flag是判断标识，是一次查出还是异步加载，
	 * flag != null 一次加载。
	 * @param map
	 * @return
	 */
	public List<HashMap<String, String>> getTreeRoot(HashMap<String, String> map) {
		List treeRoot =  new ArrayList();	
		treeRoot=getServiceSqlSession().db_selectList("getTreeRoot", map);
		
		//判断第二次节点
		if(map.get("flag") != null && map.get("flag") != ""){
			if(treeRoot.size() > 0 ){
				for(int i = 0 ; i < treeRoot.size(); i++){
					Map map2 = (Map)treeRoot.get(i);
					getChildNode(map2);
				}
			}
		}
		
		return treeRoot;
	} 
	
	/**
	 * 递归查出所有子节点；
	 * @param map2
	 */
	public void getChildNode(Map map2){
		if(map2.get("id") == null || map2.get("id") == ""){
			return;
		}else{
			Map map = new HashMap();
			map.put("id", map2.get("id")); 
			List treeRoot=getServiceSqlSession().db_selectList("getTreeRoot", map);
			map2.put("children", treeRoot);
			
			if(treeRoot.size() > 0){
				for(int i = 0 ; i < treeRoot.size(); i++){
					Map map3 = (Map)treeRoot.get(i);
					getChildNode(map3);
				}
			}else{
				return;
			}
		}
	}
	
	
	public List<HashMap<String, String>> getPropNode(HashMap<String, String> map) {
		List treeRoot =  new ArrayList();	
		treeRoot = getServiceSqlSession().db_selectList("getPropNode", map);
		return treeRoot;
	} 
	
	
	/**
	 * dataTable 数据
	 * @param map
	 * @param request
	 * @return
	 */
	
	public  Map getGridList(Map map,HttpServletRequest request){
		Map rMap =new HashMap();
		
		List listData = getServiceSqlSession().db_selectList("getGridList", map);
		rMap = getServiceSqlSession().db_selectOne("getTotalData", map);
		rMap.put("data",listData);
		return rMap;
	} 
	
	/**
	 * 添加商品获取该分类下的所有属性（包含上层分类的属性和属性值）
	 * @param map
	 * @param request
	 * @return
	 */
	public  Map getAllProp(Map map,HttpServletRequest request){
		Map rMap =new HashMap();
		
		List listData = getServiceSqlSession().db_selectList("getGridList", map);
		
		if(map.get("flag") != null && map.get("flag") != ""){
			if(listData.size() > 0 ){
				for(int i = 0 ; i < listData.size(); i++){
					Map map2 = (Map)listData.get(i);
					getChildPropValue(map2);
				}
			}
		}
		rMap.put("data",listData);
		
		return rMap;
		
		
//		
//		String str ="";
//		for(int i = 0 ; i <100;i++){
//			if(start != null && end != null ){
//				str = "/""01/":"签到，签退"; 
//			}
//			
//		}
		
		
	} 
	
	
	
	
	/**
	 * 递归查出所有属性值；
	 * @param map2
	 */
	public void getChildPropValue(Map map2){
		if(map2.get("id") == null || map2.get("id") == ""){
			return;
		}else{
			Map map = new HashMap();
			String str = map2.get("id").toString();
			map.put("categoryId", "'"+str+"'"); 
			map.put("tableName", "tb_Prop");
			List listData = getServiceSqlSession().db_selectList("getGridList", map);
			map2.put("children", listData);
			
			if(listData.size() > 0){
				for(int i = 0 ; i < listData.size(); i++){
					Map map3 = (Map)listData.get(i);
					getChildPropValue(map3);
				}
			}else{
				return;
			}
		}
	}
	
	
	
	
	
	public int updateCateNode(HashMap<String, String> map){
		return  getServiceSqlSession().db_update("updateCateNode", map);
		
	}
	
	public int updatePropNode(HashMap<String, String> map){
		return  getServiceSqlSession().db_update("updatePropNode", map);
	}
	
	
	//分页禁用数据（未使用）
	public Map getAllDisableData(Map map,HttpServletRequest request){
		
		Map rMap =new HashMap();
		
		List list1 = getServiceSqlSession().db_selectList("getDisableProp", map);
		List list2 = getServiceSqlSession().db_selectList("getDisableCate", map);
		List newList = new ArrayList();
		for(int i = 0; i < list1.size();i++){
			newList.add(list1.get(i));
		}
		
		for(int i = 0; i < list2.size();i++){
			newList.add(list2.get(i));
		}
		
		rMap.put("total", newList.size());
		rMap.put("data",newList);
		
		return rMap;
		
	}
	
	
	//禁用通用操作
	public int disableCateNode(HashMap<String, String> map){
		int a =	updateCateNode(map);
		int b = updatePropNode(map);
		
		if(a!= 0 && b!=0){
			return 1;
		}else{
			return 0;
		}
	}
	
	/**
	 * 验证分类同级不同名
	 */
	public Map validateCate(Map map){
		Map rMap = new HashMap();
		
		rMap =	this.getServiceSqlSession().db_selectOne("validateCate",map);
		
		return rMap;
	}
	
	/**
	 * 验证属性同级不同名
	 */
	public Map validateProp(Map map){
		Map rMap = new HashMap();
		
		rMap =	this.getServiceSqlSession().db_selectOne("validateProp",map);
		
		return rMap;
	}
	
	
	
	
	/**
	 * 新增收货地址
	 */
	
	public int addAddress(Map map){
		
		//添加时如果选择设置为默认地址，则其他数据清空；
		if(map.get("isDefaultAddress").toString().equals("1")){
			Map rMap = new HashMap();
			rMap.put("isDefaultAddress", "0");
			int count = this.getServiceSqlSession().db_update("updateAddress", rMap);
		}
		
		int result =  this.getServiceSqlSession().db_insert("addAddress", map);
	
		return result;
	}
	
	
	/**
	 * 修改收货地址；
	 */
	public int updateAddress(Map map){
		
		//修改时如果选择设置为默认地址，则其他数据清空；
		if(map.get("isDefaultAddress").toString().equals("1")){
			Map rMap = new HashMap();
			rMap.put("isDefaultAddress", "0");
			int count = this.getServiceSqlSession().db_update("updateAddress", rMap);
		}
		
		int result = this.getServiceSqlSession().db_update("updateAddress", map);
		
		return result;
		
	}
	
	/**
	 * 删除地址
	 */
	
	public int delAddress(Map map){
			
		int result = this.getServiceSqlSession().db_delete("delAddress",map);
		
		return result;
	}
	
	/**
	 * 查询地址集合（条件为用户id）
	 */
	public Map selectAddress(Map map){
		Map rMap = new HashMap();
		List list =	this.getServiceSqlSession().db_selectList("selectAddress", map);
		rMap =	this.getServiceSqlSession().db_selectOne("selectAddressCount",map);
		rMap.put("data", list);
		return rMap;
	}
	
	
	/**
	 * 新增商品评价
	 */
	
	public int addPingJia(Map map){
		int result = this.getServiceSqlSession().db_insert("addPingJia",map);
		
		return result;
		
	}
	
	/**
	 * 查看评价
	 */
	public Map selectAllPingJia(Map map){
		Map rMap = new HashMap();
		List list =	this.getServiceSqlSession().db_selectList("selectAllPingJia", map);
		rMap =	this.getServiceSqlSession().db_selectOne("selectAllPingJiaCount",map);
		rMap.put("data", list);
		return rMap;
	}
	
}
