package com.it.app.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.it.app.services.tb_goodsService;
import com.it.util.BaseAction;


@Controller
@RequestMapping("/*/Shopping/goodManager")
public class goodsManager extends BaseAction {

	//public static  Logger logger = LoggerFactory.getLogger("ZDesk"); 
	
	
	@Autowired
	private tb_goodsService goodsService;
	
	
	/**
	 * 添加商品
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/insertGood")
	public void insertGood(@RequestParam HashMap<String, String> map, HttpServletRequest request,HttpServletResponse response) {
		
		String ret="{\"retcode\":\"-1\"}";
		try{
			
			boolean result =  this.goodsService.insertGood(map);
			if(result){
				ret="{\"retcode\":\"0\"}";
			}	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		postStrToClient(ret,response);
	}	
	
	/**
	 * 
	 * @param map
	 * @param request
	 * @param response
	 */
	/*
	@RequestMapping(value = "/addGood")
	public void addGood(@RequestParam HashMap<String, String> map, HttpServletRequest request,HttpServletResponse response) {
		
		String ret = Constants.RET_STATUS_FAILED;
		try{
			String cardUrl = CookieUtils.getMhCardUrlByCookie(request, null);
			String companyId = MHUserT.getCompanyByCookieAndCardUrl(cardUrl,request);
			map.put("dataId", DAOTools.getDockerTableId());
	    	map.put("type", "GOOD");
	    	map.put("reserved7", companyId); //存放当前使用人所在的companyId
	    	//通用接口 ---
	    	this.com_orderService.saveCommonOrderData(map, companyId);
	    	
	    	this.goodsService.addGood(map, companyId);
			
			ret = Constants.RET_STATUS_SUCESS;
		} catch (Exception e) {
			LogUtil.error(e, logger);
		}
		postStrToClient(ret,response);
	}	
	*/
	
	
	/**
	 * 修改商品
	 * @param map
	 * @param request
	 * @param response
	 */
//	@RequestMapping(value = "/updateGood")
//	public void updateGood(@RequestParam HashMap<String, String> map, HttpServletRequest request,HttpServletResponse response) {
//		
//		String ret = Constants.RET_STATUS_FAILED;
//		try{
//			
//			String cardUrl = CookieUtils.getMhCardUrlByCookie(request, null);
//			String companyId = MHUserT.getCompanyByCookieAndCardUrl(cardUrl,request);
//			map.put("type", "GOOD");
//			this.com_orderService.updateDataByDataId(map, false, companyId);
//			
//			this.goodsService.updateGood(map, companyId);
//			ret = Constants.RET_STATUS_SUCESS;
//			
//		} catch (Exception e) {
//			LogUtil.error(e, logger);
//		}
//		postStrToClient(ret,response);
//		
//	}
//	
	
	
	/**
	 * 删除商品
	 * @param map
	 * @param request
	 * @param response
	 */
//	@RequestMapping(value = "/delGood")
//	public void delGood(@RequestParam HashMap<String, String> map, HttpServletRequest request,HttpServletResponse response) {
//		
//		String ret = Constants.RET_STATUS_FAILED;
//		try{
//			
//			String cardUrl = CookieUtils.getMhCardUrlByCookie(request, null);
//			String companyId = MHUserT.getCompanyByCookieAndCardUrl(cardUrl,request);
//			/*this.com_orderService.deleteByIds(ids, companyId);*/
//			
//			/**
//			 *  该删除只是把 reserved6 变成 商家和未上架   1 是上架 0 是未上架
//			 */
//			map.put("type", "GOOD");
//			/*this.com_orderService.updateDataByDataId(map, false, companyId);*/
//			this.goodsService.delGood(map, companyId);
//			ret = Constants.RET_STATUS_SUCESS;
//			
//		} catch (Exception e) {
//			LogUtil.error(e, logger);
//		}
//		postStrToClient(ret,response);
//		
//	}
	
	
	/**
	 * 删除商品
	 * @param map
	 * @param request
	 * @param response
	 */
//	@RequestMapping(value = "/checkGoodStatus")
//	public void checkGoodStatus(@RequestParam HashMap<String, String> map, HttpServletRequest request,HttpServletResponse response) {
//		
//		String ret = Constants.RET_STATUS_FAILED;
//		try{
//			
//			String cardUrl = CookieUtils.getMhCardUrlByCookie(request, null);
//			String companyId = MHUserT.getCompanyByCookieAndCardUrl(cardUrl,request);
//			/*this.com_orderService.deleteByIds(ids, companyId);*/
//			
//			/**
//			 *  该删除只是把 reserved6 变成 商家和未上架   1 是上架 0 是未上架
//			 */
//			map.put("type",Constants.SHOPPING_TYPE);
//			this.com_orderService.updateDataByDataId(map, false, companyId);
//			
//			
//			/*this.goodsService.delGood(map, companyId);*/
//			ret = Constants.RET_STATUS_SUCESS;
//			
//		} catch (Exception e) {
//			LogUtil.error(e, logger);
//		}
//		postStrToClient(ret,response);
//		
//	}
	
	
	
	
	
	/**
	 *  获取所有商品集合
	 */
//	@RequestMapping(value = "/getAllGoodData")
//	public void getAllGoodData(@RequestParam HashMap<String, String> map, HttpServletRequest request,HttpServletResponse response) {
//		
//		try{
//			
//			String cardUrl = CookieUtils.getMhCardUrlByCookie(request, null);
//			String companyId = MHUserT.getCompanyByCookieAndCardUrl(cardUrl,request);
//			
//			map.put("type",Constants.SHOPPING_TYPE);
//			
//			List list = this.com_orderService.getCommonOrderData(map, companyId);
//			Map reMap = new HashMap();
//			
//			// 如果dataId 为null  不加载下面数据
//			if(String.valueOf(map.get("dataId")) != null){
//				List li = this.goodsService.getAllGoodProp(map, companyId);
//				reMap.put("propList", li);
//				reMap.put("propTotal", li.size());
//			}
//			
//			reMap.put("data", list);
//			reMap.put("total", list.size());
//			
//			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"",reMap), response);
//			
//		} catch (Exception e) {
//			LogUtil.error(e, logger);
//		}
//	}
	
	/**
	 * 
	 * 查询商品，返回带商品属性的list集合
	 *
	 */
//	@RequestMapping(value = "/getAllGoodDataWithProp")
//	public void getAllGoodDataWithProp(@RequestParam HashMap<String, String> map, HttpServletRequest request,HttpServletResponse response) {
//		
//		try{
//			
//			String cardUrl = CookieUtils.getMhCardUrlByCookie(request, null);
//			String companyId = MHUserT.getCompanyByCookieAndCardUrl(cardUrl,request);
//			
//			map.put("type",Constants.SHOPPING_TYPE);
//			
//			List<Map> list = this.com_orderService.getAllCommonOrderData(map, companyId);
//			Map reMap = new HashMap();
//			
//			
//			if(list.size() > 0){
//				Map propMap = new HashMap();
//				List<Map> propList = this.goodsService.getAllGoodPropByIdStr(list,"dataId",companyId);
//				
//				for(int i = 0; i < propList.size(); i++){
//					
//					if(propMap.get(propList.get(i).get("goodId")) == null){
//						List<Map> li = new ArrayList<Map>();
//						li.add(propList.get(i));
//						propMap.put(propList.get(i).get("goodId"),li);
//					}else{
//						((List<Map>) propMap.get(propList.get(i).get("goodId"))).add(propList.get(i));
//					}
//					
//				}
//				
//				for(int j = 0; j< list.size();j++){
//					list.get(j).put("propData",propMap.get(list.get(j).get("dataId")));
//				}
//				
//			}
//			
//			reMap.put("data", list);
//			reMap.put("total", list.size());
//			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"",reMap), response);
//			
//		} catch (Exception e) {
//			LogUtil.error(e, logger);
//		}
//	}
	
	
	/**
	 * 查询商品，返回不带商品属性的list集合
	 */
//	@RequestMapping(value = "/getAllGoodDataWithOutProp")
//	public void getAllGoodDataWithOutProp(@RequestParam HashMap<String, String> map, HttpServletRequest request,HttpServletResponse response) {
//		
//		try{
//			
//			String cardUrl = CookieUtils.getMhCardUrlByCookie(request, null);
//			String companyId = MHUserT.getCompanyByCookieAndCardUrl(cardUrl,request);
//			
//			map.put("type",Constants.SHOPPING_TYPE);
//			
//			List list = this.com_orderService.getAllCommonOrderData(map, companyId);
//			Map reMap = new HashMap();
//			
//			reMap.put("data", list);
//			reMap.put("total", list.size());
//			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"",reMap), response);
//			
//		} catch (Exception e) {
//			LogUtil.error(e, logger);
//		}
//	}
	
	
	/**
	 * 获取所有商品类型
	 * @param map
	 * @param request
	 * @param response
	 */
//	@RequestMapping(value = "/getAllGoodType")
//	public void getAllGoodType(@RequestParam HashMap<String, String> map, HttpServletRequest request,HttpServletResponse response) {
//		
//		try{
//			
//			String cardUrl = CookieUtils.getMhCardUrlByCookie(request, null);
//			String companyId = MHUserT.getCompanyByCookieAndCardUrl(cardUrl,request);
//
//			List list = this.goodsService.getAllGoodType(companyId);
//			
//			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"",list), response);
//			
//		} catch (Exception e) {
//			LogUtil.error(e, logger);
//		}
//	}
		
}
	