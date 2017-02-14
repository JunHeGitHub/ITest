package com.zinglabs.apps.shoppingCart.action;



import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.shoppingCart.service.ShoppingCartService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping("/*/shoppingCartAction")
public class ShoppingCartAction extends BaseAction {

	ShoppingCartService stService = getService();

	/**
	 * 购物车查询方法
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = "/query")
	public void query(@RequestParam HashMap<String, String> map,
			HttpServletRequest request, HttpServletResponse response) {
		List list = stService.query(map);
		write(list, response);
	}

	@RequestMapping(value = "/search")
	public void search(@RequestParam HashMap<String, String> map,
			HttpServletRequest request, HttpServletResponse response) {
		List list = stService.search(map);
		write(list, response);
	}

	@RequestMapping(value = "/searchGoodsProp")
	public void searchGoodsProp(@RequestParam HashMap<String, String> map,
			HttpServletRequest request, HttpServletResponse response) {
		List list = stService.searchGoodsProp(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", list),
				response);
	}

	/**
	 * 购物车添加方法
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/insert")
	public void insert(@RequestParam HashMap<String, String> map,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = stService.insert(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(
				Boolean.parseBoolean(data.get("bool").toString()),
				data.get("msg").toString()), response);
	}

	/**
	 * 购物车删除方法
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delete")
	public void delete(@RequestParam HashMap<String, String> map,
			HttpServletRequest request, HttpServletResponse response) {
		Map map1 = stService.delete(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(
				Boolean.parseBoolean(map1.get("bool").toString()),
				map1.get("msg").toString(), map), response);
	}

	/**
	 * 购物车修改方法
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/update")
	public void update(@RequestParam HashMap<String, String> map,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map1 = stService.update(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(
				Boolean.parseBoolean(map1.get("bool").toString()),
				map1.get("msg").toString(), map), response);
	}

	@RequestMapping(value = "/update2")
	public void update2(@RequestParam HashMap<String, String> map,
			HttpServletRequest request, HttpServletResponse response) {
		stService.update2(map);

	}

	@RequestMapping(value = "/getCount")
	public int getCount(@RequestParam HashMap<String, String> map,
			HttpServletRequest request, HttpServletResponse response) {
		int i = stService.getCount(map);
		return i;
	}

	public void write(List list, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			String s = JSONArray.fromObject(list).toString();
			Writer write = response.getWriter();
			write.write(s);
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ShoppingCartService getService() {
		return (ShoppingCartService) getBean("testService");
	}
}
