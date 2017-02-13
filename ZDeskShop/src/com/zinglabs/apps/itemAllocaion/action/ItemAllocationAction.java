package com.zinglabs.apps.itemAllocaion.action;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.itemAllocaion.peizhiService;
import com.zinglabs.base.core.frame.SysPropertiesUtil;
import com.zinglabs.util.JsonUtils;

import com.zinglabs.apps.appCommonCurd.AppCommonsCurdService;
import com.zinglabs.apps.itemAllocaion.peizhiService;
import com.zinglabs.base.core.frame.AppLogImpl;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;

@Controller
@RequestMapping("/itemAllocation/itemAllocationAction")
public class ItemAllocationAction extends BaseAction {
	
	SysPropertiesUtil sysProperties=new SysPropertiesUtil();

	@RequestMapping(value = "/query")
	public void query(@RequestParam HashMap<String,String> map,HttpServletRequest request,HttpServletResponse response){
		List list=null;
		if(null != map){
			if((null == map.get("peizhiItem") || "".equals(map.get("peizhiItem"))) && (null == map.get("bItem") || "".equals(map.get("bItem")))  && (null == map.get("productionId") || "".equals(map.get("productionId")))){
				list = sysProperties.propertList;
			}else{
				list= sysProperties.queryBItemAndProductionIdAndPeizhiItem(map.get("bItem"),map.get("productionId"),map.get("peizhiItem"));
			}
		}
		write(list,response);
	}
	
	@RequestMapping(value = "/reload")
	public void reload(HttpServletRequest request,HttpServletResponse response){
		sysProperties=new SysPropertiesUtil();
		sysProperties.propertList=new ArrayList<HashMap<String,String>>();
		sysProperties.init();
		List list=sysProperties.propertList;
		write(list,response);
	}
	
	public void write(List list,HttpServletResponse response){
		response.setCharacterEncoding("utf-8");
		String json=JSONArray.fromObject(list).toString();
		try {
			Writer write=response.getWriter();
			write.write(json);
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 增/改
	 * @param request
	 * @param response
	 * @return
	 */	
	@RequestMapping(value = "/add")
	public void appAdd(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){		
		try {
			getService().Add(map);
		} catch (Exception e) {
			e.printStackTrace();
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "增加失败"),response);
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "增加成功"),response);
	}
	
	/**
	 * 修改
	 * @param request
	 * @param response
	 * @return
	 */	
	@RequestMapping(value = "/update")
	public void appEdit(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		getService().Update(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "修改成功"),response);
	}

	public peizhiService getService() {
		return (peizhiService) getBean("peizhiService");
	}


}
