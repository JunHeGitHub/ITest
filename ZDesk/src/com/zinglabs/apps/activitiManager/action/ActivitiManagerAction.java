package com.zinglabs.apps.activitiManager.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.activitiManager.service.ActivitiManagerService;
import com.zinglabs.base.core.activitiSupport.ActivitiUtils;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping("/*/activitiAdmin")
public class ActivitiManagerAction extends BaseAction {
	public static  Logger logger = LoggerFactory.getLogger("ZDesk");
	ActivitiUtils ActivitiUtils = new ActivitiUtils() ;
	@RequestMapping(value = "/getProcesses")
	public void getProcess(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		String key=map.get("key")==null?"":map.get("key").toString();
		List list=new ArrayList();
		try{
			list=ActivitiUtils.getAllProcessDefinitions2Map(key,true);
		}catch(Exception x){
			logger.error("获取流程列表失败 ： " + x.getMessage());
			LogUtil.error(x, logger);
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"", list),response);
	}
	
	@RequestMapping(value = "/delProcesses")
	public void delProcesses(@RequestParam HashMap<String,String> map,HttpServletRequest request,HttpServletResponse response){
		String key=map.get("key")==null?"":map.get("key").toString();
		List list=new ArrayList();
		try{
			list=ActivitiUtils.getAllProcessDefinitions2Map(key,true);
		}catch(Exception x){
			logger.error("获取流程列表失败 ： " + x.getMessage());
			LogUtil.error(x, logger);
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"", list),response);
	}
	
	@RequestMapping(value = "/startProcesses")
	public void startProcesses(@RequestParam HashMap<String,String> map,HttpServletRequest request,HttpServletResponse response){
		String key=map.get("key")==null?"":map.get("key").toString();
		List list=new ArrayList();
		try{
			list=ActivitiUtils.getAllProcessDefinitions2Map(key,true);
		}catch(Exception x){
			logger.error("获取流程列表失败 ： " + x.getMessage());
			LogUtil.error(x, logger);
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"", list),response);
	}
	
//	@RequestMapping(value = "/getProcessesImage")
//	public void getProcessesImage(@RequestParam HashMap<String,String> map,HttpServletRequest request,HttpServletResponse response){
//		String id=map.get("id")==null?"":map.get("id").toString();
//		try{
//			ActivitiUtils.getProcessDefinitionImageStream(id, response);
//		}catch(Exception x){
//			logger.error("获取流程定义图片失败 ： " + x.getMessage());
//			LogUtil.error(x, logger);
//		}
//		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"" ),response);
//	}
	
	@RequestMapping(value = "/getProcesseModels")
	public void getProcesseModels(@RequestParam HashMap<String,String> map,HttpServletRequest request,HttpServletResponse response){
		String mkey=map.get("key");
		List list=new ArrayList();
		try{
			list=ActivitiUtils.getProcessDefinitionModles2Map(mkey);
		}catch(Exception x){
			x.printStackTrace() ;
			logger.error("获取流程模型列表失败 ： " + x.getMessage());
			LogUtil.error(x, logger);
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"", list),response);
	}
	
	@RequestMapping(value = "/doCreateModel")
	public void doCreateModel(@RequestParam HashMap<String,String> map,HttpServletRequest request,HttpServletResponse response){
		String key=map.get("key")==null?"":map.get("key");
		String name=map.get("name")==null?"":map.get("name");
		String description=map.get("description")==null?"":map.get("description");
		if(key.length()> 0 && name.length()>0){
			try{
				String mid=ActivitiUtils.createProcessDefinitionModle(name, key, description);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"模型建立成功。", mid),response);
			}catch(Exception x){
				logger.error("获取流程模型列表失败 ： " + x.getMessage());
				LogUtil.error(x, logger);
			}
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"缺少建立模型属性，建立失败。"),response);
	}
	
	@RequestMapping(value = "/doDeleteModel")
	public void doDeleteModel(@RequestParam HashMap<String,String> map,HttpServletRequest request,HttpServletResponse response){
		String mid=map.get("id");
		if(mid!=null && mid.length()>0){
			try{
				ActivitiUtils.delProcessDefinitionModle(mid);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"删除成功", mid),response);
			}catch(Exception x){
				logger.error("删除流程模型失败 ： " + x.getMessage());
				LogUtil.error(x, logger);
			}
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"缺少关键参数，删除失败。"),response);
	}
	
	@RequestMapping(value = "/deployProcess")
	public void deployProcess(@RequestParam HashMap<String,String> map,HttpServletRequest request,HttpServletResponse response){
		String mid=map.get("id");
		if(mid!=null && mid.length()>0){
			try{
				ActivitiUtils.deployProcessDefinitionModle(mid);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"发布成功", mid),response);
			}catch(Exception x){
				logger.error("发布流程模型失败 ： " + x.getMessage());
				LogUtil.error(x, logger);
			}
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"缺少关键参数，发布失败。"),response);
	}
	
	@RequestMapping(value = "/deployProcessWithStr")
	public void deployProcessWithStr(@RequestParam HashMap<String,String> map,HttpServletRequest request,HttpServletResponse response){
		String mid=map.get("id");
		String xmlStr = map.get("activitiXmlStr");
		if(mid!=null && mid.length()>0){
			try{
				ActivitiUtils.deployProcessDefinitionModle(mid,xmlStr.trim());
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"发布成功", mid),response);
			}catch(Exception x){
				logger.error("发布流程模型失败 ： " + x.getMessage());
				LogUtil.error(x, logger);
			}
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"缺少关键参数，发布失败。"),response);
	}
	
	@RequestMapping(value = "/doExprotModel")
	public void doExprotModel(@RequestParam HashMap<String,String> map,HttpServletRequest request,HttpServletResponse response){
		String mid=map.get("id");
		if(mid!=null && mid.length()>0){
			try{
				ActivitiUtils.exportProcessDefinitionModle(mid, response);
			}catch(Exception x){
				logger.error("导出流程模型失败 ： " + x.getMessage());
				LogUtil.error(x, logger);
			}
		}
	}
	
//	@RequestMapping(value = "/getTaskImage")
//	public void getTaskImage(@RequestParam HashMap<String,String> map,HttpServletRequest request,HttpServletResponse response){
//		String id=map.get("id")==null?"":map.get("id").toString();
//		try{
//			ActivitiUtils.getTaskImageStream(id, response);
//		}catch(Exception x){
//			logger.error("获取流程跟踪图片失败 ： " + x.getMessage());
//			LogUtil.error(x, logger);
//		}
//		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"" ),response);
//	}
	
	@RequestMapping(value = "/reloadActivitiParam")
	public void reloadActivitiParam(@RequestParam HashMap<String,String> map,HttpServletRequest request,HttpServletResponse response){
		getService(request).initActivitiParam();
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,""),response);
	}
	
	public ActivitiManagerService getService(HttpServletRequest request){
		return (ActivitiManagerService)getBean("activitiManagerService");
	}
	
}
