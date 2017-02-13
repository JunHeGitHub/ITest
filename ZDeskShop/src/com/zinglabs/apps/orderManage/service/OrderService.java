package com.zinglabs.apps.orderManage.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.apps.goodsManager.GoodsManagerServices;
import com.zinglabs.apps.shoppingCart.service.ShoppingCartService;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;


public class OrderService extends BaseService {
	// 订单关闭状态代码
	private String orderStateCloseCode = "06";
	private boolean ifSplit = true;// 是否分组
	String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.format(new Date()).toString();
	public static Logger logger = LoggerFactory.getLogger("ZDesk");

	/**
	 * 更新订单
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> updateOrderState(Map map) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("length",
				getServiceSqlSession().db_update("updateOrderState", map));
		return data;
	}

	/**
	 * 获取订单列表
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getOrderList(Map map) {
		return getServiceSqlSession().db_selectList("getOrderList", map);
	}

	/**
	 * 获取订单列表
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getOrderListInPhone(Map map) {
		return getServiceSqlSession().db_selectList("getOrderListBy", map);
	}

	/**
	 * 获取订单列表数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getOrderListData(Map map) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("data", getOrderList(map));
		data.put("lengths", getOrderListLength(map));
		return data;
	}

	/**
	 * 获取订单列表长度
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getOrderListLength(Map map) { // 编译出错临时调整
		Integer i = getServiceSqlSession().db_selectOne("getOrderListLength",
				map);
		// int i
		return i;
	}

	/**
	 * 获取订单详情列表
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map> getOrderDetails(Map map) {
		return getServiceSqlSession().db_selectList("getOrderDetails", map);
	}

	/**
	 * 获取订单详情列表数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrderDetailsData(Map map) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("data", getOrderDetails(map));
		data.put("lengths", getOrderDetailsLength(map));
		return data;
	}

	/**
	 * 获取订单详情列表数据(phone)
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrderDetailsDataInPhone(Map map) {
		if (map.get("sellersUser") == null
				|| map.get("sellersUser").toString().trim().equals("")) {
			logger.debug("orderService getOrderDetailsDataInPhones 缺少参数 sellersUser==null");
			return null;
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("data", getOrderListInPhone(map));
		data.put("lengths", getOrderDetailsLengthInPhone(map));
		return data;
	}

	/**
	 * 获取订单详情列表数据(phones)
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getOrderDetailsDataInPhones(Map map) {
		Map<String, Object> data = new HashMap<String, Object>();
		// 下单人账号
		if (map.get("buyersUser") == null
				|| map.get("buyersUser").toString().trim().equals("")) {
			logger.debug("orderService getOrderDetailsDataInPhones 缺少参数 buyersUser==null");
			return null;
		}
		List<Map> list = getOrderList(map);
		for (int i = 0; list.size() > i; i++) {
			map.put("orderId", list.get(i).get("orderGroupNumber"));
			List<Map> list1 = getOrderDetails(map);
			list.get(i).put("subset", list1);
		}
		data.put("data", list);
		data.put("lengths", getOrderDetailsLengthInPhone(map));
		return data;
	}

	/**
	 * 获取订单详情列表长度
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getOrderDetailsLength(Map map) { // 编译出错 临时调整
		Integer i = getServiceSqlSession().db_selectOne(
				"getOrderDetailsLength", map);
		// int i
		return i;
	}

	/**
	 * 获取订单详情列表长度
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getOrderDetailsLengthInPhone(Map map) { // 编译出错 临时调整
		Integer i = getServiceSqlSession().db_selectOne("getOrderListByLength",
				map);
		// int i
		return i;
	}

	/**
	 * 插入商品流水
	 * 
	 * @param map
	 * @return
	 */
	public int insertGoodsRecord(Map<String, String> map) {
		int result = getServiceSqlSession().db_insert("insertGoodsRecord", map); // 流水记录表
		return result;

	}

	/************************************** 去付款 *******************************************/
	public Map<String, Object> toPayment(Map<String, String> map) {
		Map<String, Object> data = getServiceSqlSession().db_selectOne("payment", map); // 流水记录表
		if (data == null) {
			logger.error("orderService toPayment select error :data==null");
			return sonReturn("查询异常", false);
		}
		if (!data.get("COUNT").equals("0")) {
			return sonReturn(data.get("count") + "组商品库存不足 购买其他商品", false);
		}
		if (!data.get("dataStatus").equals("0")) {
			return sonReturn(data.get("dataStatus") + "组商品已失效 请重新下单 购买其他商品", false);
		}
		if (!data.get("buyersUser").equals("0")) {
			return sonReturn("身份认证失败", false);
		}
		if (!data.get("orderState").equals("0")) {
			return sonReturn("请勿重复 付款（订单已关闭）", false);
		}
		data.putAll(sonReturn("ok", true));
		return data;
	}

	/**
	 * 返回 信息
	 * 
	 * */
	public Map<String, Object> sonReturn(String msg, boolean is) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("msg", msg);
		data.put("bool", is);
		return data;
	}

	/************************************** 去付款 *******************************************/
	/************************************** 添加订单 *******************************************/
	/**
	 * 订单 编号生成
	 */
	public String getOrderNumber(){
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+String.valueOf(new Random().nextInt(10000));
	}
	/**
	 * 添加订单入口
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map addOrder(Map<String, Object> map) {
		// 订单主id
		String orderNumber =getOrderNumber() ;
		map = disposeOrderData(map);// 校验解析数据
		if (!Boolean.parseBoolean(map.get("?x8800?x").toString()))
			return map;
		Map data = getMap();
		if (orderBePutInStorage(map, orderNumber)) {
			data.put("msg", map.get("msg") == null ? "订单添加成功" : map.get("msg"));
			data.put("orderNumber", orderNumber);
			data.put("?x8800?x", true);
		} else {
			data.put("msg", map.get("msg") == null ? "订单添加失败" : map.get("msg"));
			data.put("?x8800?x", false);
		}
		// int result = getServiceSqlSession().db_insert("insertGoodsRecord",
		// map); // 流水记录表
		return data;
	}

	/**
	 * 订单入库
	 * 
	 * @param map
	 * @param orderNumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean orderBePutInStorage(Map map, String orderNumber) {
		Map orderData = (Map) map.get("orderGoodsData");
		List<Map> goodsRollBackData = new ArrayList<Map>();
		Iterator iter = orderData.entrySet().iterator();
		map.put("orderNumber", orderNumber);
		try {
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				double sum = 0;
				// 取出订单组
				List<Map> orderList = (List<Map>) entry.getValue();
				// 生成组编号
				String orderGroupNumber = getOrderNumber();
				for (int i = 0; orderList.size() > i; i++) {
					Map orderGoods = orderList.get(i);
					orderGoods.put("orderNumber", orderNumber);
					orderGoods.put("orderGroupNumber", orderGroupNumber);
					Map data = getOrderGoodsData(orderGoods);
					sum += (Double.valueOf(data.get("price").toString()));
					// 插入订单商品表
					int z = getServiceSqlSession().db_insert(
							"insertOrderGoods", data);
					// 插入商品流水表
					int x = getServiceSqlSession().db_insert(
							"insertGoodsRecord", data);
					goodsRollBackData.add(rollBackGoods(orderGoods));
					int j = getService().getServiceSqlSession().db_update(
							"updateGoods", getGoodsData(orderGoods));
					if (z == 0 || x == 0 || j == 0) {
						// 失败写回滚
						roolBack(goodsRollBackData, orderNumber);
						return false;
					}
				}
				map.put("price", sum);
				map.put("orderGroupNumber", orderGroupNumber);
				map.put("orderFor", ifSplit ? entry.getKey() : "");
				Map data = getOrderTableData(map);
				int z = getServiceSqlSession().db_insert("insertOrderAccount",
						data);
				int x = getServiceSqlSession().db_insert("insertOrderTable",
						data);
				if (z == 0 || x == 0) {
					// 失败写回滚
					roolBack(goodsRollBackData, orderNumber);
					return false;
				}
				// 删除购物车 数据
				Map map1 = getMap();
				map1.put("id", map.get("goodsId"));
				ShoppingCartService ShoppingCartService = (ShoppingCartService) this
						.getBean("testService");
				ShoppingCartService.delete(map1);
			}
			return true;
		} catch (Exception e) {
			logger.error("error" + e.toString());
			roolBack(goodsRollBackData, orderNumber);
			return false;
			// TODO: handle exception
		}
	}

	/**
	 * 回滚
	 * 
	 * @param list
	 * @param orderNumber
	 */
	public void roolBack(List<Map> list, String orderNumber) {
		for (int i = 0; i < list.size(); i++) {
			getService().getServiceSqlSession().db_update("updateGoods",
					list.get(i));
		}
		Map map = getMap();
		map.put("orderNumber", orderNumber);
		getServiceSqlSession().db_delete("roolBackOrder", map);
	}

	public boolean checkF(Object o) {
		if (o == null || o.equals("")) {
			return true;
		} else {
			return false;
		}
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

	/**
	 * 插入订单表
	 */
	/**
	 * 插入订单商品表
	 */
	/**
	 * 插入订单流水表
	 */
	/**
	 * 插入库存流水表
	 */
	/**
	 * 解析数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> disposeOrderData(Map<String, Object> map) {
		try {
			String checkF[] = new String[] { "ids", "goodsId", "goodsNumber",
					"buyersUser", "buyersName", "buyersPhone", "buyersAddress",
					"sellersUser", "sellersName", "sellersDepartmentId",
					"sellersDepartmentName", "goodsCateGory1" };
			Map<String, Object> check = checkF(checkF, map, null);
			if (!Boolean.parseBoolean(check.get("bool").toString())) {
				map.put("?x8800?x", false);
				map.put("msg", "参数异常");
				return map;
			}
			String[] goodsIds = map.get("ids").toString().split(",");
			String[] goodsNumbers = map.get("goodsNumber").toString()
					.split(",");
			String[] goodsCateGory = map.get("goodsCateGory1").toString()
					.split(",");
			int i = 0;
			for (String id : goodsIds) {
				map.put(id, goodsNumbers[i]);
				if (goodsCateGory.length != 0) {
					map.put("goryCate" + id, goodsCateGory[i]);
				} else {
					map.put("goryCate" + id, "");
				}
				i++;
			}
			// 验证库存 获取订单数据
			Map<String, Object> checkoutRepertory = getOrderGoods(map
					.get("ids").toString(), map);
			if (!Boolean.parseBoolean(checkoutRepertory.get("is").toString())) {
				map.put("?x8800?x", false);
				map.put("msg", checkoutRepertory.get("msg"));
				return map;
			}
			map.put("?x8800?x", true);
			// 处理后订单商品数据
			List<HashMap<String, Object>> orderGoodsData = (List<HashMap<String, Object>>) checkoutRepertory
					.get("data");
			// 订单分组
			Map data = groupByCategory(orderGoodsData);
			if (data == null) {
				map.put("?x8800?x", false);
				map.put("msg", "数据解析异常");
				return map;
			}
			map.put("orderGoodsData", data);// 加入订单分组数据
			map.put("orderGoodsList", orderGoodsData);// 加入订单商品数据
			/*
			 * for (HashMap<String, Object> map1 : orderList) { // 计算总金额
			 * sumPrice += Double.valueOf(map1.get("goodsSalePrice")
			 * .toString()); }
			 */
			/*
			 * String[] goodsIds=map.get("ids").split(","); HashMap<String,
			 * Object> map1=new HashMap<String, Object>(); map1.put("id",
			 * goodsIds); getService().selectSomeGoods(map1);
			 */
			return map;
		} catch (Exception e) {
			// TODO: handle exception
			map.put("?x8800?x", false);
			map.put("msg", "数据解析异常");
			logger.error("数据解析异常" + e.toString());
			return map;
		}
	}

	/**
	 * 验证库存
	 * 
	 * @param list
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> checkoutRepertory(
			List<HashMap<String, Object>> list, Map map) {
		Map<String, Object> msg = getMap();
		StringBuilder msgStr = new StringBuilder("");
		boolean is = true;
		for (int i = 0; list.size() > i; i++) {
			int re = Integer.valueOf(map.get(list.get(i).get("id")).toString());// 购物车商品数量
			int nu = Integer.valueOf(list.get(i).get("goodsCount").toString());// 剩余数量
			if (re > nu) {
				msgStr.append("商品:" + list.get(i).get("goodsName") + "数量不足\n");
				is = false;
			}
		}
		msg.put("msg", msgStr);
		msg.put("is", is);
		return msg;
	}

	// value 变key 装 value
	public Map valueToKey(Map map, String keyValue, String valueValue) {

		return map;
	}

	/**
	 * 通过种类将数据分组
	 * 
	 * @param list
	 * @param isNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map groupByCategory(List<HashMap<String, Object>> list) {
		try {
			Map<String, List> map = getMap();
			if (!ifSplit) {
				map.put("list1", list);
				return map;
			}
			// diyi次数据分离
			for (Map data : list) {
				if (map.get(data.get("groy").toString()) == null)// 加入Ke
					map.put(data.get("groy").toString(), new ArrayList<Map>());
				map.get(data.get("groy").toString()).add(data);
			}
			/*
			 * // 一层数据分组 for (Map data : list) {
			 * map.get(data.get("groy").toString()).add(data); }
			 */
			// 第二次数据分离
			Map<String, List> map1 = getMap();
			Iterator iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String sectionId = getGoodsSection(entry.getKey().toString());
				if (map1.get(sectionId) == null)
					map1.put(sectionId, new ArrayList<Map>());
				map1.get(sectionId).addAll((List<Map>) entry.getValue());
			}
			return map1;
		} catch (Exception e) {
			logger.error("order 数据分组析异常");
			return null;
			// TODO: handle exception
		}

	}

	/**
	 * 获取分类部门id
	 * 
	 * @param classify
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getGoodsSection(String classify) {
		Map map = getMap();
		map.put("id", classify);
		List<Map> list = getServiceSqlSession().db_selectList(
				"getGoodsClassify", map);
		return list.get(0).get("departmentId").toString();
	}

	/**
	 * 获取商品信息
	 * 
	 * @param ids
	 * @param ifSplit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getOrderGoods(String ids, Map map) {
		Map map1 = new HashMap();
		boolean is = true;
		StringBuilder msgStr = new StringBuilder("");
		/*
		 * Map map = new HashMap(); map.put("tableName", tableName);
		 * map.put("delete_ids", ids.split(","));
		 */
		map1.put("id", ids.split(","));
		List<HashMap<String, Object>> list = getService().selectSomeGoods(
				(HashMap<String, Object>) map1);

		for (int i = 0; list.size() > i; i++) {// 追加剥离分类id
			if (ifSplit) {
				// 处理分类id 用于后期拆分订单
				String[] goodsCateGory = list.get(i).get("goodsCateGory")
						.toString().replace("'", "").split(",");
				list.get(i).put("groy",
						goodsCateGory[(goodsCateGory.length - 1)]);
			}
			int re = Integer.valueOf(map.get(list.get(i).get("id")).toString());// 购物车商品数量
			int nu = Integer.valueOf(list.get(i).get("goodsCount").toString());// 剩余数量
			list.get(i).put("number", re);
			if (re > nu) {
				msgStr.append("商品:" + list.get(i).get("goodsName") + "数量不足\n");
				is = false;
			}
			list.get(i).put("goodsCotegoryText",
					map.get("goryCate" + list.get(i).get("id")));
		}
		map1.put("msg", msgStr);
		map1.put("is", is);
		map1.put("data", list);
		return map1;
	}

	/**
	 * 拼装订单商品数据
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getOrderGoodsData(Map map) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orderGroupNumber", map.get("orderGroupNumber"));//
		data.put("orderId",
				map.get("orderId") == null ? "0" : map.get("orderId"));
		data.put("groupId", map.get("groupId"));
		data.put("goodsNumber", map.get("goodsCode"));
		data.put("goodsTitle", map.get("goodsTitle"));
		data.put("goodsId", map.get("id"));
		data.put("goodsName", map.get("goodsName"));
		data.put("goodsCostPrice", map.get("goodsCostPrice"));
		data.put("goodsSalePrice", map.get("goodsSalePrice"));
		data.put("saleCount", map.get("number"));
		data.put("updateCount", "-" + map.get("number"));
		data.put("state", "创建订单");
		data.put("addDate", time);
		data.put("orderNumber", map.get("orderNumber"));
		data.put("GoodsAttributes", map.get("goodsProp"));
		data.put("goodsCotegory", map.get("goodsCotegoryText"));
		data.put("numbers", map.get("number"));
		data.put("unitPrice", map.get("goodsSalePrice"));
		data.put(
				"price",
				Double.valueOf(map.get("number").toString())
						* Double.valueOf(map.get("goodsSalePrice").toString()));
		data.put("createDate", time);
		data.put("companyID",
				map.get("companyID") == null ? "0" : map.get("companyID"));
		return data;
	}

	/**
	 * 拼装商品数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getGoodsData(Map map) {
		Map data = getMap();
		data.put("id", map.get("id"));
		data.put("goodsId", map.get("id"));
		data.put("reduceGoodsCount", map.get("number"));
		return data;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> rollBackGoods(Map map) {
		Map data = getMap();
		data.put("id", map.get("id"));
		data.put("goodsId", map.get("goodsId"));
		data.put("plusGoodsCount", map.get("number"));
		return data;
	}

	/**
	 * 拼装订单数据
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getOrderTableData(Map map) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orderNumber", map.get("orderNumber"));// 待处理
		data.put("orderGroupNumber", map.get("orderGroupNumber"));//
		data.put("orderTime", time);
		data.put("orderState", "01");
		data.put("orderFor",
				map.get("orderFor") == null ? "" : map.get("orderFor"));//
		data.put("buyersUser", map.get("buyersUser"));
		data.put("buyersName", map.get("buyersName"));
		data.put("buyersPhone", map.get("buyersPhone"));
		data.put("goodsId", "0");
		data.put("sellersUser", map.get("sellersUser"));
		data.put("buyersMessage", map.get("buyersMessage"));
		data.put("buyersAddress", map.get("buyersAddress"));
		data.put("sellersName", map.get("sellersName"));
		data.put("sellersDepartmentId", map.get("sellersDepartmentId"));
		data.put("sellersDepartmentName", map.get("sellersDepartmentId"));
		data.put("orderProcessingTime", time);
		data.put("companyID", map.get("companyID"));
		data.put("type", "创建订单");
		data.put("privilegePrice", "0");
		data.put("processingTime", time);
		data.put("price", map.get("price"));
		data.put("orderPrice", map.get("price"));
		return data;
	}

	/**
	 * 提供注入 商品service接口
	 * 
	 * @return
	 */
	public GoodsManagerServices getService() {
		return (GoodsManagerServices) getBean("GoodsManagerServices");
	}

	/************************************** ------- *******************************************/

	/************************************** 更改价格 *******************************************/
	@SuppressWarnings("unchecked")
	public String upOrderGoodsPrice(Map<String, Object> map) {
		Map orderData = toOrderData(map);
		Map<String, Object> orderGoodData = toOrderGoodData(map);
		// 验证是否可以更改
		int length = getOrderListLength(orderData);
		if (length > 0) {
			int id = getServiceSqlSession()
					.db_insert("insertOrderAccount", map);
			if (id > 0) {
				updateOrderPrice(orderData);
				updateOrderGoodsPrice(orderGoodData);
				return "操作成功";
			}
		}
		return "操作失败";
	}

	@SuppressWarnings("unchecked")
	public Map toOrderData(Map map) {
		Map orderData = new HashMap();
		orderData.put("privilegePrice", map.get("privilegePrice"));
		orderData.put("orderStateMax", map.get("orderStateMax"));
		orderData.put("orderId", map.get("orderId"));
		return orderData;
	}

	@SuppressWarnings("unchecked")
	public Map toOrderGoodData(Map map) {
		Map orderGoodData = new HashMap();
		orderGoodData.put("price", map.get("price"));
		orderGoodData.put("GId", map.get("orderGoodsId"));
		return orderGoodData;
	}

	// 插入订单流水
	@SuppressWarnings("unchecked")
	public int insertOrderAccount(Map<String, String> map) {
		return getServiceSqlSession().db_insert("insertOrderAccount", map);
	}

	// 更改订单金额
	@SuppressWarnings("unchecked")
	public boolean updateOrderPrice(Map<String, String> map) {
		int i = getServiceSqlSession().db_insert("updateOrderPrice", map);
		return i <= 0 ? false : true;
	}

	// 更改订单商品金额
	@SuppressWarnings("unchecked")
	public boolean updateOrderGoodsPrice(Map<String, Object> map) {
		int i = getServiceSqlSession().db_update("updateOrderGoodsPrice", map);
		return i <= 0 ? false : true;
	}

	/*************************************** ------ *******************************************/

	/************************************** 退单操作 *******************************************/
	/**
	 * 退单
	 * 
	 * @param map
	 * @return
	 */

	public List getStetaSelect(Map<String, Object> map,
			HttpServletRequest request) {
		return (List) getServiceSqlSession().db_selectList(
				"getOrderSatteSelect", map);
	}

	@SuppressWarnings("unchecked")
	public String delOrder(Map<String, Object> map, HttpServletRequest request) {
		// 记录原始数据值集合用于回滚操作
		String ids = "";

		if ((!updateOrderState(map.get("orderId").toString(),
				orderStateCloseCode))
				&& (map.get("orderState").equals(orderStateCloseCode))) {
			return "退单失败";
		}

		Map<String, Object> datas = (Map<String, Object>) getOrderDetailsData((Map<String, Object>) map);
		List<Map<String, String>> list = (List<Map<String, String>>) datas
				.get("data");
		// 流水表主键id字段名 从页面上传过来
		String primarKey = map.get("primarykey") == null ? "id" : map.get(
				"primarykey").toString();
		try {
			for (Map<String, String> data : list) {
				data = orderGoodsToRecord(data);
				// data.put(primarKey, new RandomGUID().toString());
				// data.put(primarKey, "3");
				int id = insertGoodsRecord(data);
				// 插入失败回滚操作
				if (id == 0)
					return rollBack(map, ids, primarKey, request);
				ids += "," + id;
			}
		} catch (Exception e) {
			return rollBack(map, ids, primarKey, request);
		}
		map.put("orderState", orderStateCloseCode.toString());
		return "操作成功";

	}

	/**
	 * 回滚
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String rollBack(Map<String, Object> map, String ids,
			String primarKey, HttpServletRequest request) {
		updateOrderState(map.get("orderId").toString(), map.get("orderState")
				.toString());
		if (!ids.isEmpty())
			delGoodsRecord(ids.substring(1), primarKey, map.get("tableName")
					.toString(), map.get("dbid").toString(),
					map.get("nameSpace").toString(), request);
		return "订单状态更改失败";
	}

	@SuppressWarnings("unchecked")
	public boolean updateOrderState(String orderId, String orderState) {
		// 更改订单状态为关闭
		Map upMap = new HashMap();
		upMap.put("orderState", orderState);
		upMap.put("id", orderId);
		// upMap.put("orderStateCloseCode", orderStateCloseCode);
		int i = Integer.valueOf(updateOrderState(upMap).get("length")
				.toString());
		if (i == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 数据转换
	 */
	@SuppressWarnings("unchecked")
	public Map orderGoodsToRecord(Map map) {
		Map map1 = new HashMap();
		map1.put("goodsId",
				map.get("goodsId") == null ? "0" : map.get("goodsId"));
		map1.put("groupId",
				map.get("groupId") == null ? "0" : map.get("groupId"));
		map1.put("orderId", map.get("id") == null ? "0" : map.get("id"));
		map1.put("goodsCostPrice", map.get("goodsCostPrice") == null ? "0"
				: map.get("goodsCostPrice"));
		map1.put("goodsSalePrice",
				map.get("unitPrice") == null ? "0" : map.get("unitPrice"));
		map1.put("saleCount",
				map.get("numbers") == null ? "0" : "-" + map.get("numbers"));
		map1.put("updateCount",
				map.get("numbers") == null ? "0" : map.get("numbers"));
		map1.put("addTime",
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
						.toString());
		map1.put("goodsName",
				map.get("goodsName") == null ? "0" : map.get("goodsName"));
		map1.put("state", "取消订单");
		return map1;
	}

	/**
	 * 删除流水
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int delGoodsRecord(String ids, String primarKey, String tableName,
			String dbid, String nameSpace, HttpServletRequest request) {
		Map map = new HashMap();
		map.put("tableName", tableName);
		map.put("delete_ids", ids.split(","));
		map.put("primarykey", primarKey);
		try {
			return getServiceSqlSessions().db_delete(nameSpace, map, dbid);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	/*********************** ------------------------------------ **************************/

	public AppSqlSessionDbid getServiceSqlSessions() {
		return (AppSqlSessionDbid) getBean("appCommonsSqlSession");
	}

	public Map getMap() {
		return new HashMap();
	}

	/**
	 * get sqlsession
	 */
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		// TODO Auto-generated method stub
		return (AppSqlSessionDbid) this.getBean("SqlSessionss");
	}
}
