package com.zinglabs.apps.Category.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.Category.CategoryServices;
import com.zinglabs.apps.zkmCommonTree.ZkmTreeCommonService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping("/*/Category")
public class CategoryAction extends BaseAction {

	public CategoryServices getService() {
		return (CategoryServices) getBean("CategoryServices");
	}
	
	
	@RequestMapping(value = "/getTreeRoot")
	public void getCaidanTreeNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {		
		List<HashMap<String, String>> treeRoot = getService().getTreeRoot(map);  
		/*List<HashMap<String, String>> treeRoot2 = getService().getPropNode(map);  
		
		List<HashMap<String, String>> newList = new ArrayList<HashMap<String,String>>();
		
		for(int i = 0; i < treeRoot.size();i++){
			newList.add(treeRoot.get(i));
		}
		
		for(int i = 0; i < treeRoot2.size();i++){
			newList.add(treeRoot2.get(i));
		}*/
		
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", treeRoot), response);				
	}
	
	
	
	
	@RequestMapping(value = "/getGridList")
	public void getGridList(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {
		
		Map rMap = getService().getGridList(map,request);
		
		rMap.put("mgs", "成功");
		rMap.put("success", true);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap), response);
			
	}
	
	
	@RequestMapping(value = "/getAllProp")
	public void getAllProp(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {
		
		Map rMap = getService().getAllProp(map,request);
		rMap.put("mgs", "成功");
		rMap.put("success", true);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap), response);
			
	}
	
	
	
	
	@RequestMapping(value = "/getAllDisableData")
	public void getAllDisableData(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {		
		Map rMap = getService().getAllDisableData(map,request);  
		rMap.put("mgs", "成功");
		rMap.put("success", true);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap), response);				
	}
	
	
	
	@RequestMapping(value="/validate")
	public void validate(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {

		Map rMap = new HashMap();
		if(map.get("tableName").equals("tb_Prop")){
			
			rMap = getService().validateProp(map);
			
		}else{
			rMap = getService().validateCate(map);
		}
		System.out.println(rMap.keySet());
		if(Integer.parseInt((rMap.get("total").toString())) > 0){
			
			rMap.put("success",true);
			rMap.put("mgs","该值已存在");
		}else{
			rMap.put("success",false);
			rMap.put("mgs","该值可使用");
		}
		
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap), response);	
		
	}
	
	
	
	
	@RequestMapping(value = "/disableTreeNode")
	public void disableTreeNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		 int b = getService().disableCateNode(map);
		 if(b!=0)
		  postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "禁用成功"), response);
	}
	
	
	@RequestMapping(value = "/updateCateNode")
	public void updateCateNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		 int b = getService().updateCateNode(map);
		 if(b!=0)
		  postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "拖拽成功"), response);
	}
	
	@RequestMapping(value = "/updatePropNode")
	public void updatePropNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		 int b = getService().updatePropNode(map);
		 if(b!=0)
		  postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "拖拽成功"), response);
	}
	
	
	@RequestMapping(value="/addAddress")
	public void addAddress(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		int result = this.getService().addAddress(map);
		Map rMap = new HashMap();
		if(result > 0){
			rMap.put("mgs", "新增地址成功");
			rMap.put("success", true);
		}else{
			rMap.put("mgs", "新增地址失败");
			rMap.put("success", false);
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap), response);
	}
	
	@RequestMapping(value="/updateAddress")
	public void updateAddress(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		
		int result = this.getService().updateAddress(map);
		Map rMap = new HashMap();
		if(result > 0){
			rMap.put("mgs", "修改地址成功");
			rMap.put("success", true);
		}else{
			rMap.put("mgs", "修改地址失败");
			rMap.put("success", false);
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap), response);
		
	}
	
	@RequestMapping(value="/delAddress")
	public void delAddress(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		
		int result = this.getService().delAddress(map);
		Map rMap = new HashMap();
		if(result > 0){
			rMap.put("mgs", "删除地址成功");
			rMap.put("success", true);
		}else{
			rMap.put("mgs", "删除地址失败");
			rMap.put("success", false);
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap), response);
		
	}
	
	
	
	
	@RequestMapping(value="/selectAddress")
	public void selectAddress(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		
		Map rMap = this.getService().selectAddress(map);
		rMap.put("success", true);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap), response);
		
	}
	
	
	
	/**
	 * 商品评价管理
	 * @throws Exception 
	 */
	@RequestMapping(value="/addPingJia")
	public void addPingJia(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) throws Exception {
		String str = map.toString();
		
		str = str.substring(1, str.length()-2);
		//System.out.println(str);
		List list =	(List) JsonUtils.stringToJSON(str,List.class);
		map.put("map",list);
		int result = this.getService().addPingJia(map);
		Map rMap = new HashMap();
		if(result > 0){
			rMap.put("mgs", "评价成功");
			rMap.put("success", true);
		}else{
			rMap.put("mgs", "评价失败");
			rMap.put("success", false);
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap), response);
	}
	
	@RequestMapping(value="/selectAllPingJia")
	public void selectAllPingJia(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		
		Map rMap = this.getService().selectAllPingJia(map);
		rMap.put("success", true);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap), response);
		
	}
	
	
	
	/**
	 * 
	 * 测试远程加载数据；
	 * 
	 */
	
	@RequestMapping(value="/getCookieValue")
	public void getCookieValue(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		
		
		map.put("id","44806");
		map.put("username","测试用户1111");
		map.put("phone","150000000000");
		map.put("address","北京市海淀区上地7街");
		
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(map), response);	
		
	}

}
