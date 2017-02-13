package com.zinglabs.apps.orderManage.action;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.orderManage.service.OrderService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

/**
 * Test class
 * 
 * @author YZ
 * 
 */
@Controller
@RequestMapping("/*/OD")
public class OrderManageAction extends BaseAction {
	/**
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getOrderList")
	public void getOrderListByCondition(@RequestParam HashMap map,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = getService().getOrderListData(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", data),
				response);
	}

	/**
	 * 添加订单
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addOrder")
	public void addOrder(@RequestParam HashMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = getService().addOrder(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(Boolean
				.parseBoolean(data.get("?x8800?x").toString()), data.get("msg")
				.toString(), map), response);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getOrderDetails")
	public void getOrderDetailsByCondition(@RequestParam HashMap map,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = getService().getOrderDetailsData(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", data),
				response);
	}

	/**
	 * 不显示 商品详细数据
	 * */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getOrderDetailsInPhone")
	public void getOrderDetailsInPhone(@RequestParam HashMap map,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = getService().getOrderDetailsDataInPhone(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", data),
				response);
	}

	/**
	 * 展示商品详细数据
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getOrderInPhones")
	public void getOrderDetailsInPhones(@RequestParam HashMap map,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = getService()
				.getOrderDetailsDataInPhones(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", data),
				response);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getOrderStateSelect")
	public void getOrderStateSelect(@RequestParam HashMap map,
			HttpServletRequest request, HttpServletResponse response) {
		List list = getService().getStetaSelect(map, request);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", list),
				response);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateOrderState")
	public void updateOrderState(@RequestParam HashMap map,
			HttpServletRequest request, HttpServletResponse response) {
		// 拼接数据
		Map<String, Object> data = getService().updateOrderState(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", data),
				response);
	}

	@RequestMapping(value = "/delOrder")
	public void delOrder(@RequestParam HashMap map, HttpServletRequest request,
			HttpServletResponse response) {
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, getService()
				.delOrder(map, request), map), response);
	}

	@RequestMapping(value = "/upOrderGoodsPrice")
	public void upOrderGoodsPrice(@RequestParam HashMap map,
			HttpServletRequest request, HttpServletResponse response) {
		String msg = getService().upOrderGoodsPrice(map);
		postStrToClient(
				JsonUtils.genUpdateDataReturnJsonStr(
						msg.equalsIgnoreCase("操作成功") ? true : false, msg, map),
				response);
	}

	@RequestMapping(value = "/insertGoodsRecord")
	public void insertGoodsRecord(@RequestParam HashMap map,
			HttpServletRequest request, HttpServletResponse response) {
		int result = getService().insertGoodsRecord(map);
		if (result != 0) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "添加成功"),
					response);
		} else {
			postStrToClient(
					JsonUtils.genUpdateDataReturnJsonStr(false, "添加失败"),
					response);
		}

	}

	@RequestMapping(value = "/toPayment")
	public void toPayment(@RequestParam HashMap map,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = getService().toPayment(map);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(Boolean
					.parseBoolean(data.get("bool").toString()), data.get("msg")
					.toString(), data), response);
	}

	/*	*//**
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @RequestMapping(value = "/updateStudent") public void
	 * updateStudentDataByCondition(@RequestParam HashMap map,
	 * HttpServletRequest request, HttpServletResponse response) { int
	 * i=getService().updateStudent(map);
	 * postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", i),
	 * response); }
	 *//**
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @RequestMapping(value = "/addStudent") public void
	 * addStudentDataByCondition(@RequestParam HashMap map, HttpServletRequest
	 * request, HttpServletResponse response) { int
	 * i=getService().addStudent(map);
	 * postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", i),
	 * response); }
	 *//**
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @RequestMapping(value = "/delStudent") public void
	 * delStudentDataByCondition(@RequestParam HashMap map, HttpServletRequest
	 * request, HttpServletResponse response) { int
	 * i=getService().deleteStudent(map);
	 * postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", i),
	 * response); }
	 */
	public OrderService getService() {
		return (OrderService) getBean("OrderService");
	}

}
