package com.zinglabs.apps.BOC_gongdan.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.BOC_gongdan.BOCgongdanService;
import com.zinglabs.apps.BOC_gongdan.IdGenerator;
import com.zinglabs.base.core.activitiSupport.ActivitiUtils;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping(value="/*/Bocgongdan")
public class IdGeneratorAction extends BaseAction{
	public static Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk");
	@RequestMapping(value="/makeWorkOrder")
	public void makeWorkOrder(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		Map workOrdermap = null ;
		String user = map.get("zkm_userName").toString() ;
		map.remove("zkm_userName") ;
		try{
			if(map.get("precessDefinedId")==null){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"error",workOrdermap),response) ;
			}
			ActivitiUtils ActivitiUtils = new ActivitiUtils() ;
			String processInstanceId =  ActivitiUtils.doStartProcess(map.get("precessDefinedId").toString()) ;
			String nextTaskId = ActivitiUtils.getTaskIdByProcessInstanceId(processInstanceId) ;
			ActivitiUtils.doClaim(nextTaskId, user) ;	
			map.put("flowNode", "jingBan") ;
			map.put("flowState", "新建") ;
			map.put("flowId", processInstanceId) ;
			map.put("disponseUser", user) ;
			workOrdermap = IdGenerator.getInstance().makeWorkOrder(map) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"ok",workOrdermap),response) ;
		}catch(Exception e){
			e.printStackTrace() ;
			SKIP_Logger.error("IdGeneratorAction.date8AndNumber4.action"+e.getMessage());
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"error",workOrdermap),response) ;
		}
	}
	@RequestMapping(value="/guanlian")
	public void guanlian(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		try{
			boolean a = getService().guanlian(map) ;
			if(a){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"ok"),response) ;
				return ;
			}
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"error"),response) ;
		}catch(Exception e){
			e.printStackTrace() ;
			SKIP_Logger.error("IdGeneratorAction.date8AndNumber4.action"+e.getMessage());
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"error"),response) ;
		}
	}
	public BOCgongdanService getService(){
		return (BOCgongdanService)getBean("BOCgongdanService");
	}	
}