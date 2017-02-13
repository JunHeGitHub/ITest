package com.zinglabs.apps.shoppingCart.service;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.search.SimilarityDelegator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class ShoppingCartService extends BaseService {

	public List searchGoodsProp(@RequestParam HashMap<String, String> map) {
		List<Map> list = getServiceSqlSession().db_selectList(
				"st_searchGoodsProp", map);
		/*
		 * for (int i = 0; i < list.size(); i++) { Map map2 = list.get(i);
		 * String name = map2.get("goodsName").toString(); String prop =
		 * map2.get("goodsProp").toString(); if (name.length() >= 20) { name =
		 * name.substring(0, 20) + "..."; map2.put("goodsName", name); } if
		 * (prop.length() >= 20) { prop = prop.substring(0, 20) + "...";
		 * map2.put("goodsProp", prop); } list.set(i, map2);
		 * 
		 * }
		 */
		// System.out.println(list);
		return list;
	}

	/**
	 * 购物车查询方法
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List query(HashMap map) {
		List<Map> list;
		Map data = new HashMap();
		data.putAll(map);
		if (map.get("id") == null || map.get("id").toString().length() == 0
				|| map.get("id").toString().split(",").length == 0) {
			data.put("id", null);
		} else {
			data.put("id", map.get("id").toString().split(","));
		}
		list = getServiceSqlSession().db_selectList("st_select", data);
		/*
		 * for (int i = 0; i < list.size(); i++) { Map map2 = list.get(i);
		 * String name = map2.get("goodsName").toString(); String prop =
		 * map2.get("goodsProp").toString(); if (name.length() >= 20) { name =
		 * name.substring(0, 20) + "..."; map2.put("goodsName", name); } if
		 * (prop.length() >= 20) { prop = prop.substring(0, 20) + "...";
		 * map2.put("goodsProp", prop); } list.set(i, map2);
		 * 
		 * }
		 */
		// System.out.println(list);
		return list;
	}

	public List search(HashMap<String, String> map) {
		List list = getServiceSqlSession().db_selectList("st_search", map);
		return list;
	}

	/***************************************************************************************
	 * 购物车添加方法
	 * 
	 * @param map
	 *            :{ goodsSubId -商品id 对应商品表主键id goodsName -商品名称 goodsTitle -商品标题
	 *            goodsCount -数量 userAccount -用户账号 userName -用户姓名 }
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> insert(Map map) {
		// 需校验字段
		String checkF[] = new String[] { "goodsSubId", "goodsName",
				"goodsTitle", "goodsCount", "userAccount", "userName" };
		try {

			Map<String, Object> check = checkF(checkF, map, null);
			if (!Boolean.parseBoolean(check.get("bool").toString())) {
				return sonReturn("参数异常", false);
			}
			List<Map> list = getServiceSqlSession().db_selectList("st_search2",
					map);

			if (list.size() > 0) {
				//商品已经存在时，数量+1；
				int result = getServiceSqlSession().db_update("st_update3", map);
				
				if(result > 0 ){
					return sonReturn("加入购物车成功",true);
				}else{
					return sonReturn("数据异常",false);
				}
				//System.out.println("aaaaaaaaaaaaaaaaa");
			//	return sonReturn("购物车已有该商品无需重复添加", false);
			}
			List<Map> goodsList = getServiceSqlSession().db_selectList(
					"goods_search", map);
			if (goodsList.size() == 0) {
				return sonReturn("商品已下架", false);
			}
			/*
			 * int count = Integer.valueOf(goodsList.get(0).get("goodsCount")
			 * .toString()); if ((count <
			 * Integer.valueOf(map.get("goodsCount").toString())) || (count ==
			 * 0)) { return sonReturn("商品库存不足", false); }
			 */
			map.put("id", UUID.randomUUID());
			map.put("addTime", new Timestamp(new java.util.Date().getTime()));
			if (null == map.get("goodsCount")
					|| map.get("goodsCount").equals("")) {
				map.put("goodsCount", "1");
			}
			map.put("goodsName", goodsList.get(0).get("goodsName"));
			map.put("goodsTitle", goodsList.get(0).get("goodsTitle"));
			int i = getServiceSqlSession().db_insert("st_insert", map);
			if (i > 0)
				return sonReturn("添加成功", true);
			/*
			 * return sonReturn(count > 10 ? "添加成功" : "添加成功(商品库存所剩无几--剩余[" +
			 * count + "].再不下单就要卖没啦)", true);
			 */
			else
				return sonReturn("添加异常", false);
		} catch (Exception e) {
			return sonReturn("添加异常", false);
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> mapToData(Map map) {
		map.put("addTime",
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
						.toString());
		return map;
	}

	public Map<String, Object> sonReturn(String msg, boolean is) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("msg", msg);
		data.put("bool", is);
		return data;
	}

	/**
	 * map1==null?: 验证map中某些key是否为空 else : 比较map与map1中 指定key值是否相等
	 * 
	 * @return map:{ msg:"", boole:true/false }
	 */
	public Map<String, Object> checkF(String key[], Map map, Map map1) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			if (map1 == null) {
				for (int i = 0; i < key.length; i++) {
					if (checkF(map.get(key[i]))) {
						data.put("msg",
								"参数异常 :" + key[i] + ":" + map.get(key[i]));
						data.put("bool", false);
						return data;
					}
				}
			} else {
				for (int i = 0; i < key.length; i++) {
					if ((map.get(map.get(key[i])) != map1.get(map.get(key[i])))
							|| checkF(map1.get(map.get(key[i])))) {
						data.put("msg", "参数不匹配");
						data.put("bool", false);
						return data;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			data.put("msg", "验证过程异常");
			data.put("bool", false);
		}
		data.put("msg", "");
		data.put("bool", true);
		return data;
	}

	public boolean checkF(Object o) {
		if (o == null || o.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/********************************* -------------------- ***********************************/
	/**
	 * 购物车删除方法
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> delete(Map map) {
		String checkF[] = new String[] { "id" };
		try {
			Map<String, Object> check = checkF(checkF, map, null);
			if (!Boolean.parseBoolean(check.get("bool").toString())) {
				return sonReturn("参数异常", false);
			}
			check.put("id", map.get("id").toString().split(","));
			int i = getServiceSqlSession().db_delete("st_delete", check);
			if (i > 0) {
				return sonReturn("更改成功", true);
			}
		} catch (Exception e) {

		}
		return sonReturn("更改失败数据异常 请与客服联系", false);
	}

	/**
	 * 购物车修改方法
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> update(Map map) {
		String checkF[] = new String[] { "goodsSubId", "id", "goodsCount",
				"userAccount" };
		try {
			Map<String, Object> check = checkF(checkF, map, null);
			if (!Boolean.parseBoolean(check.get("bool").toString())) {
				return sonReturn("参数异常", false);
			}
			List<Map> list = getServiceSqlSession().db_selectList("st_search2",
					map);
			if (list.size() > 0) {
				return sonReturn("购物车已有该商品无需重复添加", false);
			}
			int i = getServiceSqlSession().db_update("st_update", map);
			if (i > 0)
				return sonReturn("更改成功", true);
		} catch (Exception e) {

		}

		return sonReturn("更改失败数据异常 请与客服联系", false);
	}

	public int update2(Map map) {

		int i = getServiceSqlSession().db_update("st_update2", map);
		return i;
	}

	public int getCount(HashMap<String, String> map) {
		int i = getServiceSqlSession().db_update("st_getCount", map);
		return i;
	}

	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) this.getBean("shoppingCart_SqlSession");
	}

}
