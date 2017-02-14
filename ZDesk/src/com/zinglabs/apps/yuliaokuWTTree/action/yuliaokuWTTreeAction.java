package com.zinglabs.apps.yuliaokuWTTree.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.messageProcess.messageProcessService;
import com.zinglabs.apps.yuliaokuWTTree.yuliaokuWTTreeService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping(value = "/*/yuliaokuWTTreeProcess")
public class yuliaokuWTTreeAction extends BaseAction {
	
	
	/**
	 * 获取未绑定到树的对应数据 
	 * @param map
	 * @param request
	 * @param response
	 */
	
	@RequestMapping(value = "/getDataListNotToTree")
	public void getDataList(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			List<Map> DataList = getService().getDataList(map);
			Map countMap= getService().getDataListCount(map);
			int count =Integer.parseInt(countMap.get("total").toString());
			Map resultdata =new HashMap();
			resultdata.put("AllData", DataList);
			resultdata.put("total", count);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "查询成功",
					resultdata), response);
		} catch (Exception e) {
			postStrToClient(
					JsonUtils.genUpdateDataReturnJsonStr(false, "查询异常"),
					response);
		}
	}
	
	/**
	 * 获取绑定到树的对应数据 
	 * @param map
	 * @param request
	 * @param response
	 */
	
	@RequestMapping(value = "/getDataListToTree")
	public void getDataListToTree(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			List<Map> DataList = getService().getDataListToTree(map);
			Map countMap= getService().getDataListCountToTree(map);
			int count =Integer.parseInt(countMap.get("total").toString());
			Map resultdata =new HashMap();
			resultdata.put("AllData", DataList);
			resultdata.put("total", count);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "查询成功",
					resultdata), response);
		} catch (Exception e) {
			postStrToClient(
					JsonUtils.genUpdateDataReturnJsonStr(false, "查询异常"),
					response);
		}
	}
	
	/**
	 * 批量改绑数据
	 * @param map
	 * @param request
	 * @param response
	 */
	
	@RequestMapping(value = "/updDataToTree")
	public void updDataToTree(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			String DataIdsStr=String.valueOf(map.get("DataIdsStr"));
			String[] DataIds = DataIdsStr.toString().split(";");
			map.remove("DataIdsStr");
			map.put("DataIds", DataIds);
			int Updcount = getService().updDataSome(map);
			if(Updcount!=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "改绑成功",
					Updcount), response);
			}else{
				postStrToClient(
						JsonUtils.genUpdateDataReturnJsonStr(false, "改绑失败"),
						response);
				
			}
		} catch (Exception e) {
			postStrToClient(
					JsonUtils.genUpdateDataReturnJsonStr(false, "改绑异常"),
					response);
		}
	}
	
	/**
	 * 批量解绑数据
	 * @param map
	 * @param request
	 * @param response
	 */
	
	@RequestMapping(value = "/delDataToTree")
	public void delDataToTree(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			String DataIdsStr=String.valueOf(map.get("DataIdsStr"));
			String[] DataIds = DataIdsStr.toString().split(";");
			map.remove("DataIdsStr");
			map.put("DataIds", DataIds);
			int Delcount = getService().delDataSome(map);
			if(Delcount!=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "解绑成功",
					Delcount), response);
			}else{
				postStrToClient(
						JsonUtils.genUpdateDataReturnJsonStr(false, "解绑失败"),
						response);
				
			}
		} catch (Exception e) {
			postStrToClient(
					JsonUtils.genUpdateDataReturnJsonStr(false, "解绑异常"),
					response);
		}
	}
	
	/**
	 * 批量解绑数据(删除节点时)
	 * @param map
	 * @param request
	 * @param response
	 */
	
	@RequestMapping(value = "/delDataToTreeOfDelNode")
	public void delDataToTreeOfDelNode(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {
			String NodeIdsStr=String.valueOf(map.get("NodeIdsStr"));
			String[] NodeIds = NodeIdsStr.toString().split(";");
			map.remove("NodeIdsStr");
			map.put("nodeIds", NodeIds);
			int Delcount = getService().delDataSomeOfDelNode(map);
			if(Delcount!=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "解绑成功",
					Delcount), response);
			}else{
				postStrToClient(
						JsonUtils.genUpdateDataReturnJsonStr(false, "解绑失败"),
						response);
				
			}
		} catch (Exception e) {
			postStrToClient(
					JsonUtils.genUpdateDataReturnJsonStr(false, "解绑异常"),
					response);
		}
	}
	
	
	public yuliaokuWTTreeService getService() {
		return (yuliaokuWTTreeService) getBean("yuliaokuWTTreeService");
	}
}
