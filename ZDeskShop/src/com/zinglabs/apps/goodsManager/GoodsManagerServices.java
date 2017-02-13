package com.zinglabs.apps.goodsManager;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.apps.appCommonCurd.filter.Params;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class GoodsManagerServices extends BaseService {

	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) getBean("GoodsManagerSqlSession");
	}

	
	/**
	 * 新增商品
	 * @param map
	 * @return
	 */
	
	public int insertGoods(HashMap<String, String> map){
		
		int result = 0;
		if(map.get("paramStatus").toString().equals("add")){
			result = getServiceSqlSession().db_insert("insertGoods", map);//添加商品信息表
		}else{
			result = getServiceSqlSession().db_update("updateGoods",map); //修改商品信息表
		}
		
		
		//添加成功后，给商品属性表添加该商品属性
		if(result != 0){
			
			String id = map.get("id").toString(); // 统一id
			
			//propChange == change 时 先删除，后插入
			if(map.get("paramStatus").toString().equals("edit") && map.get("propChange").equals("change")){
				map.put("goodsId", id);
				int count =	delGoodsProp(map);
			}
			
			//添加或者属性变更时重新插入
			if(map.get("paramStatus").toString().equals("add") || map.get("propChange").equals("change")){
				
				String prop = map.get("goodsProp").toString();
				String[] arg = 	prop.split("-");
				for(int i = 0; i < arg.length;i++){
					map.put("goodsId", id);  //属性表中的商品id为商品表中的表id
					map.put("id",  UUID.randomUUID().toString());
					map.put("propKey", arg[i].split(":")[0]);
					map.put("propValue", arg[i].split(":")[1]);
					result = getServiceSqlSession().db_insert("insertGoodsProp", map); //商品属性表
				}
				
			}
			
			//添加和商品变更时都对流水表做一次数据添加
			if(map.get("paramStatus").toString().equals("add") || map.get("state").toString().equals("商品变更")){
				map.put("goodsId", id);
				map.put("id", UUID.randomUUID().toString());
				// map.put("addDate", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
				//int re = insertGoodsRecord(map); //流水记录表
				 result = getServiceSqlSession().db_insert("com.zinglabs.apps.goodsManager.insertGoodsRecord", map);
			}
			
		}
		
		return result;
		
	}
	
	public int delGoodsProp(HashMap<String,String> map){
		
		int result = getServiceSqlSession().db_delete("delGoodsProp",map);
		
		return result;
	}
	
	
	//商品流水表
	public int insertGoodsRecord(HashMap<String, String> map){
	
		int result = getServiceSqlSession().db_insert("com.zinglabs.apps.goodsManager.insertGoodsRecord.insertGoodsRecord", map); //流水记录表
			
		return result;
		
	}
	
	
	//删除时查看该商品是否产生订单，如产生订单则修改，否则删除
	
	public Map updateOrDel(HashMap<String, String> map) {
		
		Map rmap = new HashMap();
		List treeRoot = getServiceSqlSession().db_selectList("selectOrderByGoodsId", map);
		String id = map.get("id").toString();
		//存在订单，修改状态
		if(treeRoot.size() > 0){
			map.put("dataStatus", "0");
			int result = getServiceSqlSession().db_update("updateGoods",map); //修改商品信息表
			//*map.put("goodsId", id);*/
		}else{
			int result = getServiceSqlSession().db_delete("delGoodsInfo",map); //删除商品信息
			
		}
		
		//如果删除的是主商品，则修改添加的第二件商品为主商品
		if(map.get("flag").equals("main")){
			Map rMap =	getServiceSqlSession().db_selectOne("selectTopGoodsByGroupId", map);
			//如果该主商品存在子商品
			if(rMap != null){
				map.put("id", rMap.get("id").toString());
				int result = getServiceSqlSession().db_update("updateGoods",map); 
			}
			
		}
		
		
		map.put("goodsId", id);
		int count =	delGoodsProp(map);
		rmap.put("msg", "删除成功");
		
		return rmap;
	}
	
	public List<HashMap<String, String>> selectPropByGoodsId(HashMap<String, String> map) {
		List treeRoot =  new ArrayList();	
		treeRoot = getServiceSqlSession().db_selectList("selectGoodsInfo", map);
		return treeRoot;
	} 
	
	//订单验证
	public List<HashMap<String, Object>> selectSomeGoods(HashMap<String, Object> map){
		List treeRoot =  new ArrayList();	
		treeRoot = getServiceSqlSession().db_selectList("selectSomeGoods",map);
		return treeRoot;
	}
	

	public Map selectMainGoods(HashMap<String, String> map) {
		Map rmap = new HashMap();
		List treeRoot =  new ArrayList();	
		treeRoot = getServiceSqlSession().db_selectList("selectMainGoods", map);
		rmap = getServiceSqlSession().db_selectOne("selectMainGoodsTotal", map);
		rmap.put("data", treeRoot);
		return rmap;
	} 
	
	
	public Map validateGoodsCount(HashMap<String,String> map){
		
		Map rmap = 	getServiceSqlSession().db_selectOne("validateGoodsCount",map);
		
		return rmap;
	}
	
	
	
	
	//测试方法
	public List<HashMap<String, String>> testSelect(HashMap<String, String> map) {
		List treeRoot =  new ArrayList();	
		treeRoot = getServiceSqlSession().db_selectList("testSelect", map);
		return treeRoot;
	} 
	
	
	
}
