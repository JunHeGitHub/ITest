package com.zinglabs.apps.i18nPrompt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.i18nPrompt.I18nPromptService;
import com.zinglabs.apps.i18nPrompt.I18nPromptUtils;
import com.zinglabs.apps.i18nPrompt.ReadProperties;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;

@Controller
@RequestMapping("/*/i18nPrompt")
public class I18nPromptAction extends BaseAction{
	public static  Logger logger = LoggerFactory.getLogger("ZDesk"); 
	/** 
	 * 数据添加
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/save")
	public void save(@RequestParam HashMap<String,String> map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		map.put("id", new RandomGUID().toString());
		//获取已存在语言代码
		List<Map<String, String>> ls=getService().selectLangugeCode(map);
		//根据语言代码制造同步key的数据
		List<Map<String, String>> lsTemp=new ArrayList<Map<String,String>>();	
		if(ls!=null && ls.size()!=0){
			for (int i = 0; i < ls.size(); i++) {
				if (!ls.get(i).get("langugeCode").equals(I18nPromptUtils.langugeCodeAndProjectCode.get("langugeCode"))) {
					Map<String,String> mapTemp=new HashMap<String, String>();
					mapTemp.put("id", new RandomGUID().toString());
					mapTemp.put("promptKey", map.get("promptKey"));
					mapTemp.put("promptValue", "");
					mapTemp.put("languge", ls.get(i).get("languge").toString());
					mapTemp.put("langugeCode", ls.get(i).get("langugeCode").toString());
					mapTemp.put("projectName", ls.get(i).get("projectName").toString());
					mapTemp.put("projectCode", ls.get(i).get("projectCode").toString());				
					lsTemp.add(mapTemp);
				}
			}
			lsTemp.add(map);
			
			if(!getService().selectExistForPromptKey(map)){
				if(getService().save(lsTemp)){
					//I18nPromptUtils.reloadI18nPromptCache();
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, ""), response);			
				}else{
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, ""), response);
				}			
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, I18nPromptUtils.getText("i18nPrompt_keyyicunzai")), response);
			}
		}else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, ""), response);
		}
		
	}
	/** 
	 * 添加新语言
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/saveLanguge")
	public void saveLanguge(@RequestParam HashMap<String,String> map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		//查询出所有的key
		List<Map<String, String>> ls=new ArrayList<Map<String,String>>();
		ls=getService().selectPromptKey();
		//制造同步key的数据
		List<Map<String, String>> promptKeyList=new ArrayList<Map<String,String>>();		
		if(ls!=null && ls.size()!=0){
			for (int i = 0; i < ls.size(); i++) {
				Map<String,String> mapTemp=new HashMap<String, String>();
				mapTemp.put("id", new RandomGUID().toString());
				mapTemp.put("promptKey", ls.get(i).get("promptKey").toString());
				mapTemp.put("promptValue", "");
				mapTemp.put("languge", map.get("languge"));
				mapTemp.put("langugeCode", map.get("langugeCode"));
				mapTemp.put("projectName", map.get("projectName"));
				mapTemp.put("projectCode", map.get("projectCode"));				
				promptKeyList.add(mapTemp);
			}
		}else{
			Map<String, String> mt=new HashMap<String, String>();
			mt.put("id", new RandomGUID().toString());
			mt.put("promptKey", "SystemGeneration_yuyanchuangjianchenggong");
			mt.put("promptValue", I18nPromptUtils.native2Ascii("语言创建成功！"));
			mt.put("languge", map.get("languge"));
			mt.put("langugeCode", map.get("langugeCode"));
			mt.put("projectName", map.get("projectName"));
			mt.put("projectCode", map.get("projectCode"));				
			promptKeyList.add(mt);
		}
		if(!getService().selectExistForLangugeCode(map)){
			//执行插入数据的操作
			if(getService().saveLanguge(promptKeyList)){
				//I18nPromptUtils.reloadI18nPromptCache();
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, I18nPromptUtils.getText("SystemGeneration_yuyanchuangjianchenggong")), response);			
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, ""), response);
			}			
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, I18nPromptUtils.getText("i18nPrompt_yuyandaimayicunzai")), response);
		}
		
		
	}
	/**
	 * 数据修改
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/update")
	public void update(@RequestParam HashMap<String,String> map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		if(getService().update(map)){
			//I18nPromptUtils.reloadI18nPromptCache();
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,""), response);			
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,""), response);
		}
		
	}
	/**
	 * 数据删除
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	public void delete(@RequestParam HashMap<String,String> map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<String> ids =new ArrayList<String>();
		List<Map<String, String>> ls = (List<Map<String, String>>) JsonUtils.stringToJSON(map.get("data"), List.class);
		if(ls.size()!=0 && ls!=null){
			Map<String, String> m=new HashMap<String, String>();
			List<Map<String, String>> lm =new ArrayList<Map<String,String>>();
			for (int i = 0; i < ls.size(); i++) {
				m.put("promptKey", ls.get(i).get("promptKey").toString());
				lm.addAll(getService().selectByPromptKey(m));
			}
			if(lm.size()!=0 && lm!=null){
				for (int i = 0; i < lm.size(); i++) {
					ids.add(lm.get(i).get("id").toString());
				}
				
				if(getService().delete(ids)){
					//I18nPromptUtils.reloadI18nPromptCache();
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,""), response);			
				}else{
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,""), response);
				}
			}
			
		}
	}
	/**
	 * 数据查询  所有
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/select")
	public void select(@RequestParam HashMap<String,String> map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		map.putAll(I18nPromptUtils.langugeCodeAndProjectCode);
		List<Map<String, String>> ls=getService().promptList(map);
				
		ls=I18nPromptUtils.ascii2Native(ls,"promptValue");
		
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", ls), response);
		
	}/**
	 * 根据前台筛选项promptKey 和 promptValue进行模糊查询数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectByPromptKeyAndPromptValue")
	public void selectByPromptKeyAndPromptValue(@RequestParam HashMap<String,String> map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		map.putAll(I18nPromptUtils.langugeCodeAndProjectCode);
		map.put("promptValue",map.get("promptValue").replaceAll("\\\\", "\\\\\\\\"));
		List<Map<String, String>> ls=getService().selectByPromptKeyAndPromptValue(map);
		
		ls=I18nPromptUtils.ascii2Native(ls,"promptValue");
		
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", ls), response);
		
	}
	/**
	 * 查询默认值
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectDefaultValue")
	public void selectDefaultValue(@RequestParam HashMap<String,String> map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		map.putAll(I18nPromptUtils.langugeCodeAndProjectCode);
		List<Map<String, String>> ls=getService().selectDefaultValue(map);
		Map<String, String> rMap =new HashMap<String, String>();
		if(ls !=null && ls.size()!=0){
			for (int i = 0; i < ls.size(); i++) {
				rMap.put("languge", ls.get(i).get("languge").toString());
				rMap.put("langugeCode", ls.get(i).get("langugeCode"));
				rMap.put("projectName", ls.get(i).get("projectName"));
				rMap.put("projectCode", ls.get(i).get("projectCode"));
			}
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",rMap), response);
		}else{
			//rMap.put("languge", "中文");
			//rMap.put("langugeCode", "zh_CN");
			//rMap.put("projectName", "英立讯");
			//rMap.put("projectCode", "YLX");
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, ""), response);
		}
		
	}		
	/**
	 * 根据配置表中的语言代码及项目名称来获取只有提示key值和提示内容的map集合
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/getI18nPromptData")
	public void getI18nPromptData(@RequestParam HashMap<String,String> map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		map.putAll(I18nPromptUtils.langugeCodeAndProjectCode);
		Map<String, String> i18nPromptHashMap=I18nPromptUtils.getI18nPromptData(map,"promptValue");		
		
		if(i18nPromptHashMap!=null){
			//读取数据并把读取到的数据重新存入缓存里面
			I18nPromptUtils.i18nPromptData = i18nPromptHashMap; 
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", I18nPromptUtils.i18nPromptData), response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, ""), response);
		}
		
		
	}
	/**
	 * 导入国际化提示信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/i18nPromptImport")
	public void i18nPromptImport(@RequestParam HashMap<String,String> map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String filePath = map.get("fileUrl");
		map.remove("fileUrl");
		//判断语言代码是否已经存在
		if(!getService().selectExistForLangugeCode(map)){
			//读取properties文件中的数据
			List<Map<String, String>> ls = ReadProperties.readProperties(filePath);
			//获取数据库中已存在的key
			List<Map<String, String>> localLs=getService().selectPromptKey();
			//变量  记录properties文件中key与数据库中key未对应的值
			List<Map<String, String>> ckls=new ArrayList<Map<String, String>>();
			//变量  记录properties文件中数据  为保存数据使用
			List<Map<String, String>> data=new ArrayList<Map<String,String>>();
			int t=0;
			if(ls.size()!=0 && ls!=null){
				if (localLs.size()!=0 && localLs!=null) {
					//判断导入文件key数与本地数据库key数对比后做相应的操作
					if(ls.size()<= localLs.size()){
						//判断ls中key是否与数据库中数据的key完全对应
						for (int i = 0; i < localLs.size(); i++) {
							String localKey = localLs.get(i).get("promptKey").toString();
							for (int j = 0; j < ls.size(); j++) {
								String importKey = ls.get(j).get("promptKey").toString();
								//如果存在key则进行储存properties文件中的数据
								if(localKey.equals(importKey)){
									t=1;
									Map<String, String> eMap=new HashMap<String, String>();
									eMap.put("id", new RandomGUID().toString());
									eMap.put("promptKey", ls.get(j).get("promptKey").toString());
									eMap.put("promptValue", ls.get(j).get("promptValue").toString());
									eMap.put("languge", map.get("languge").toString());
									eMap.put("langugeCode", map.get("langugeCode").toString());
									eMap.put("projectName", map.get("projectName").toString());
									eMap.put("projectCode", map.get("projectCode").toString());	
									data.add(eMap);
								}
							}
							//如果t等于1则说明出现相等的key  反之则没有  然后记录key
							if(t==0){
								Map<String, String> importNotExist =new HashMap<String, String>();
								importNotExist.put("id", new RandomGUID().toString());
								importNotExist.put("promptKey", localKey);
								importNotExist.put("promptValue", "");
								importNotExist.put("languge", map.get("languge").toString());
								importNotExist.put("langugeCode", map.get("langugeCode").toString());
								importNotExist.put("projectName", map.get("projectName").toString());
								importNotExist.put("projectCode", map.get("projectCode").toString());	
								data.add(importNotExist);
							}
							t=0;						
						}
						int t2=0;
						//处理不存在的数据  
						if(data.size()!=0 && data!=null){
							for (int i = 0; i < ls.size(); i++) {
								String importKey=ls.get(i).get("promptKey");
								for (int j = 0; j < data.size(); j++) {
									String localKey=data.get(j).get("promptKey");
									if(importKey.equals(localKey)){
										t2=1;
									}
								}
								if(t2==0){
									//获取已存在语言代码
									List<Map<String, String>> langugeCodeLs=getService().selectLangugeCode(map);
									if(langugeCodeLs!=null && langugeCodeLs.size()!=0){
										for (int j = 0; j < langugeCodeLs.size(); j++) {
											if (!ls.get(j).get("langugeCode").equals(I18nPromptUtils.langugeCodeAndProjectCode.get("langugeCode"))) {
												Map<String, String> langugeMap=new HashMap<String, String>();
												langugeMap.put("id", new RandomGUID().toString());
												langugeMap.put("promptKey", ls.get(i).get("promptKey").toString());
												langugeMap.put("promptValue", "");
												langugeMap.put("languge", langugeCodeLs.get(j).get("languge").toString());
												langugeMap.put("langugeCode", langugeCodeLs.get(j).get("langugeCode").toString());
												langugeMap.put("projectName", langugeCodeLs.get(j).get("projectName").toString());
												langugeMap.put("projectCode", langugeCodeLs.get(j).get("projectCode").toString());
												data.add(langugeMap);
											}
										}
										Map<String, String> importExist =new HashMap<String, String>();
										importExist.put("id", new RandomGUID().toString());
										importExist.put("promptKey", ls.get(i).get("promptKey").toString());
										importExist.put("promptValue", ls.get(i).get("promptValue").toString());
										importExist.put("languge", map.get("languge").toString());
										importExist.put("langugeCode", map.get("langugeCode").toString());
										importExist.put("projectName", map.get("projectName").toString());
										importExist.put("projectCode", map.get("projectCode").toString());	
										data.add(importExist);
									}else{
										Map<String, String> importExist =new HashMap<String, String>();
										importExist.put("id", new RandomGUID().toString());
										importExist.put("promptKey", ls.get(i).get("promptKey").toString());
										importExist.put("promptValue", ls.get(i).get("promptValue").toString());
										importExist.put("languge", map.get("languge").toString());
										importExist.put("langugeCode", map.get("langugeCode").toString());
										importExist.put("projectName", map.get("projectName").toString());
										importExist.put("projectCode", map.get("projectCode").toString());	
										data.add(importExist);
									}
									
								}
							}
						}
					}else{
						//判断ls中key是否与数据库中数据的key完全对应
						for (int i = 0; i < ls.size(); i++) {
							String importKey = ls.get(i).get("promptKey").toString();
							for (int j = 0; j < localLs.size(); j++) {
								String localKey = localLs.get(j).get("promptKey").toString();
								//如果存在key则进行储存properties文件中的数据
								if(importKey.equals(localKey)){
									t=1;
									Map<String, String> eMap=new HashMap<String, String>();
									eMap.put("id", new RandomGUID().toString());
									eMap.put("promptKey", ls.get(j).get("promptKey").toString());
									eMap.put("promptValue", ls.get(j).get("promptValue").toString());
									eMap.put("languge", map.get("languge").toString());
									eMap.put("langugeCode", map.get("langugeCode").toString());
									eMap.put("projectName", map.get("projectName").toString());
									eMap.put("projectCode", map.get("projectCode").toString());	
									data.add(eMap);
								}
							}
							//如果t等于1则说明出现相等的key  反之则没有  然后记录key
							if(t==0){
								//获取已存在语言代码
								List<Map<String, String>> langugeCodeLs=getService().selectLangugeCode(map);
								if(langugeCodeLs.size()!=0 && langugeCodeLs!=null){
									for (int j = 0; j < langugeCodeLs.size(); j++) {
										if (!ls.get(j).get("langugeCode").equals(I18nPromptUtils.langugeCodeAndProjectCode.get("langugeCode"))) {
											Map<String, String> langugeMap=new HashMap<String, String>();
											langugeMap.put("id", new RandomGUID().toString());
											langugeMap.put("promptKey", ls.get(i).get("promptKey").toString());
											langugeMap.put("promptValue", "");
											langugeMap.put("languge", langugeCodeLs.get(j).get("languge").toString());
											langugeMap.put("langugeCode", langugeCodeLs.get(j).get("langugeCode").toString());
											langugeMap.put("projectName", langugeCodeLs.get(j).get("projectName").toString());
											langugeMap.put("projectCode", langugeCodeLs.get(j).get("projectCode").toString());
											data.add(langugeMap);
										}
									}
									Map<String, String> localNotExist =new HashMap<String, String>();
									localNotExist.put("id", new RandomGUID().toString());
									localNotExist.put("promptKey", ls.get(i).get("promptKey").toString());
									localNotExist.put("promptValue", ls.get(i).get("promptValue").toString());
									localNotExist.put("languge", map.get("languge").toString());
									localNotExist.put("langugeCode", map.get("langugeCode").toString());
									localNotExist.put("projectName", map.get("projectName").toString());
									localNotExist.put("projectCode", map.get("projectCode").toString());	
									data.add(localNotExist);
								}else{
									Map<String, String> localNotExist =new HashMap<String, String>();
									localNotExist.put("id", new RandomGUID().toString());
									localNotExist.put("promptKey", ls.get(i).get("promptKey").toString());
									localNotExist.put("promptValue", ls.get(i).get("promptValue").toString());
									localNotExist.put("languge", map.get("languge").toString());
									localNotExist.put("langugeCode", map.get("langugeCode").toString());
									localNotExist.put("projectName", map.get("projectName").toString());
									localNotExist.put("projectCode", map.get("projectCode").toString());	
									data.add(localNotExist);
								}
								
							}
							t=0;
						}
					}
				}else if(localLs.size()==0 && ls.size()!=0){
					//获取已存在语言代码
					List<Map<String, String>> langugeCodeLs=getService().selectLangugeCode(map);
					for (int j = 0; j < ls.size(); j++) {
						if(langugeCodeLs.size()!=0){
							for (int j2 = 0; j2 < langugeCodeLs.size(); j2++) {
								if (!ls.get(j2).get("langugeCode").equals(I18nPromptUtils.langugeCodeAndProjectCode.get("langugeCode"))) {
									Map<String,String> im =new HashMap<String, String>();
									im.put("id", new RandomGUID().toString());
									im.put("promptKey", ls.get(j).get("promptKey").toString());
									im.put("promptValue", ls.get(j).get("promptValue").toString());
									im.put("languge", map.get("languge").toString());
									im.put("langugeCode", map.get("langugeCode").toString());
									im.put("projectName", map.get("projectName").toString());
									im.put("projectCode", map.get("projectCode").toString());	
									data.add(im);
								}
							}
							Map<String,String> im =new HashMap<String, String>();
							im.put("id", new RandomGUID().toString());
							im.put("promptKey", ls.get(j).get("promptKey").toString());
							im.put("promptValue", ls.get(j).get("promptValue").toString());
							im.put("languge", map.get("languge").toString());
							im.put("langugeCode", map.get("langugeCode").toString());
							im.put("projectName", map.get("projectName").toString());
							im.put("projectCode", map.get("projectCode").toString());	
							data.add(im);
						}else{
							Map<String,String> im =new HashMap<String, String>();
							im.put("id", new RandomGUID().toString());
							im.put("promptKey", ls.get(j).get("promptKey").toString());
							im.put("promptValue", ls.get(j).get("promptValue").toString());
							im.put("languge", map.get("languge").toString());
							im.put("langugeCode", map.get("langugeCode").toString());
							im.put("projectName", map.get("projectName").toString());
							im.put("projectCode", map.get("projectCode").toString());	
							data.add(im);
						}
						
					}
				}
				
				
				//执行保存到数据库的操作
				if(getService().save(data)){
					I18nPromptUtils.reloadI18nPromptCache();
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, ""), response);			
				}else{
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, ""), response);
				}
				
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "出现异常，请联系管理员！"), response);
			}
			
			
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "语言代码已经存在！"), response);
		}
		
		
	}
	
	public I18nPromptService getService(){
		return (I18nPromptService)getBean("i18nPromptBaseService");
	}
}

