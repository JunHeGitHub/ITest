package com.zinglabs.apps.commons.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.commons.ZKMCommonsCurdService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.db.UserInfo2;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;
import com.zinglabs.util.WebFormUtil;

@RequestMapping(value = "/*/CommonsCurd")
@Controller
public class ZKMCommonsCurdAction extends BaseAction {

	public static Logger logger = LoggerFactory.getLogger("ZDesk");

	/**
	 * 通用增加方法
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/Insert", method = RequestMethod.POST)
	public void insert(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			this.getService().insert(map);
		} catch (Exception e) {
			logger.error("FJSH map.size:"+map.size());
			logger.error("FJSH Insert Action ERROR common inser fail ERRORMESSAGE:"+e.getMessage());
			e.printStackTrace();
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "异常"),response);
			return ;
		}

		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "seccuss"),response);

	}

	/**
	 * 通用分野查询
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/Find")
	public void find(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		filterDataTablePat(map);
		try {
			Map rmap = this.getService().getListForPaging(map);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);

		} catch (Exception e) {
			logger.error("common find fail");
			e.printStackTrace();
		}
	}

	/**
	 * 通用分页查询
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/FindForComplex")
	public void FindForForComplex(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		filterDataTablePat(map);

		try {
			Map rmap = this.getService().getListPagingForComplex(map);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/FindForComplex_update")
	public void freeupdate(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		filterDataTablePat(map);

		try {
			Map rmap = this.getService().commonupdate(map);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 通用修改
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/Update", method = RequestMethod.POST)
	public void update(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			this.getService().update(map);
		} catch (Exception e) {
			logger.error("common update fail");
			e.printStackTrace();
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "seccuss"),
				response);
	}

	/**
	 * 通用删除
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/Delete", method = RequestMethod.POST)
	public void delete(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			this.getService().delete(map);
		} catch (Exception e) {
			logger.error("common delete fail");
			e.printStackTrace();
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "seccuss"),
				response);
	}
	
	// 过滤掉没用的参数
	private static void filterDataTablePat(Map map) {
		if (map.get("_time_stamp_") != null) {
			map.remove("_time_stamp_");
		}
		handlerPagingParameters(map);

	}

	// 处理分页参数
	private static void handlerPagingParameters(Map map) {
		if (map.get("start") != null) {
			String limit = (String) map.get("start") + ','
					+ (String) map.get("limit");
			map.put("limit", limit);
			map.remove("start");
		}
	}
    

	/**
	 * 
	 * 通用commonInsert方法
	 * 
	 * @param map
	 * @param request
	 * @param response
	 * @return  data.success ture or false
	 * @return  data.mgs 提示消息
	 * @return  data.data 主键的值
	 */
	@RequestMapping(value = "/commonInsert", method = RequestMethod.POST)
	public void repalce(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
       		String id=this.getService().commonInsert(map);
       		if("".equals(id)||id==null){
       			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作失败",id),response);
       			return;
       		}
       		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "操作成功",id),response);
		} catch (Exception e) {
			logger.error("common inser fail"+e.getMessage());
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "出现异常"),response);
			return ;
		}
	}
	/**
	 * 
	 * @param request
	 * @return
	 */
	
	@RequestMapping(value = "/commonSearch", method = RequestMethod.POST)
	public void commonSearch(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
       		List<Map>list=this.getService().commonSearch(map);
       		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "查询成功",list),response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("common Search fail"+e.getMessage());
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "出现异常"),response);
			return ;
		}
	}
	public ZKMCommonsCurdService getService() {
		return (ZKMCommonsCurdService) getBean("zkmCommonsCurdService");
	}
    
}
