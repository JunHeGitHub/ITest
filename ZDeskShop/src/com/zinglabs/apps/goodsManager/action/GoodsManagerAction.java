package com.zinglabs.apps.goodsManager.action;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.goodsManager.GoodsManagerServices;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;


@Controller
@RequestMapping("/*/goodsManager")
public class GoodsManagerAction extends BaseAction {

	public GoodsManagerServices getService() {
		return (GoodsManagerServices) getBean("GoodsManagerServices");
	}
	
	@RequestMapping(value="/insertGoods") 
	public void insertGoods(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		
		
			SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String date = sdf.format(new Date());
			
			map.put("createDate",date);
			map.put("dataDate", date);
			map.put("addDate", date);
		
		//int b =0;
        int b=getService().insertGoods(map);
        
        if(b!=0)
		   postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "添加成功"), response);
        else
           postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "添加失败"), response);
	}

	
	
	@RequestMapping(value="/insertGoodsRecord")
	public void insertGoodsRecord(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		
		int result = getService().insertGoodsRecord(map);
		 if(result!=0){
			   postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "添加成功"), response);
		 } else{
	           postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "添加失败"), response);
		 }
		
	}
	
	
	@RequestMapping(value="/selectMainGoods")
	public void selectMainGoods(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		
		//如分类格式为"XXX,XXX" 转成数组，然后mybatis循环查询
		/*if(map.get("goodsCateGory").toString() != null || map.get("goodsCateGory").toString() != "" ){
			
			map.put("goodsCateGory", map.get("goodsCateGory").toString().split(","));
		}*/
		
		// System.out.println(map.get("goodsCateGory").toString());
		
		Map rMap =	getService().selectMainGoods(map);
		
		rMap.put("mgs", "成功");
		rMap.put("success", true);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap), response);
		
	}
	
	
	
	@RequestMapping(value="/validateCount")
	public void validateCount(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		
		//如分类格式为"XXX,XXX" 转成数组，然后mybatis循环查询
		/*if(map.get("goodsCateGory").toString() != null || map.get("goodsCateGory").toString() != "" ){
			
			map.put("goodsCateGory", map.get("goodsCateGory").toString().split(","));
		}*/
		
		// System.out.println(map.get("goodsCateGory").toString());
		
		Map rMap =	getService().validateGoodsCount(map);
		
		rMap.put("mgs", "成功");
		rMap.put("success", true);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap), response);
		
	}
	
	
	
	
	
	//修改或者下架某个商品
	@RequestMapping(value="/updateOrDelGoodsInfo")
	public void updateOrDelGoodsInfo(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
	
		Map rMap = getService().updateOrDel(map);
		rMap.put("mgs", "成功");
		rMap.put("success", true);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap), response);
			
	}
	
	

	
	//通过商品id 查询属性
	@RequestMapping(value ="/selectPropByGoodsId")
	public void selectPropByGoodsId(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {		
	 	List<HashMap<String, String>>  list = getService().selectPropByGoodsId(map);  
		if(list.size() > 0){
			
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "请求成功", list), response);				
			
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "请求失败", list), response);
			
		}

	}
	
	

	@RequestMapping(value="/testSelect")
	public void testSelect(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		
		
		 String str = "38B2CDAB-22A4-5CD6-369C-79EF3A579F76,AB1110A4-8290-38E9-A2D5-D28E19396404";
		 String[] s = str.split(",");
		//map.put("ids", "38B2CDAB-22A4-5CD6-369C-79EF3A579F76,AB1110A4-8290-38E9-A2D5-D28E19396404");
//		List li = new ArrayList();
//		li.add("38B2CDAB-22A4-5CD6-369C-79EF3A579F76");
//		li.add("AB1110A4-8290-38E9-A2D5-D28E19396404");
		
		map.put("ids", s);
		List<HashMap<String, String>>  list = getService().testSelect(map);  
		if(list.size() > 0){
			
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "请求成功", list), response);				
			
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "请求失败", list), response);
			
		}
		
	}
	
	
	
	
}
