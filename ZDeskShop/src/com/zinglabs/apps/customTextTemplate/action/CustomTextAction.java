package com.zinglabs.apps.customTextTemplate.action;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.customTextTemplate.service.CustomTextService;
import com.zinglabs.base.core.frame.BaseAction;
@Controller
@RequestMapping("/*/CustomText")
public class CustomTextAction extends BaseAction{
	public static Logger logger = LoggerFactory.getLogger("ZKM");
	
	//=======================模板开始============================
	//查询模板
	@RequestMapping("/getTemplate")
	public void getTemplate(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		Map rmap = this.getService().getTemplate();
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}
	//根据id查询模板
	@RequestMapping("/getTemplateById")
	public void getTemplateById(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		Map rmap = this.getService().getTemplateById(map);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}
	//添加模板
	@RequestMapping("/addTemplate")
	public void addTemplate(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		map.put("id",UUID.randomUUID().toString());
		boolean flag = this.getService().addTemplate(map);
		map.put("success",flag);
		postStrToClient(JSONSerializer.toJSON(map).toString(), response);
	}
	//修改模板
	@RequestMapping("/updateTemplate")
	public void updateTemplate(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		boolean flag = this.getService().updateTemplate(map);
		postStrToClient(flag, response);
	}
	//删除模板
	@RequestMapping("/deleteTemplate")
	public void deleteTemplate(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		boolean flag = this.getService().deleteTemplate(map);
		postStrToClient(flag, response);
	}
	//=======================模板结束============================
	
	//=======================模板数据开始============================
	//查询模板数据
	@RequestMapping("/getTemplateDataByTemplateId")
	public void getTemplateDataByTemplateId(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		Map rmap = this.getService().getTemplateDataByTemplateId(map);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}
	//添加模板数据
	@RequestMapping("/addTemplateData")
	public void addTemplateData(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		map.put("id",UUID.randomUUID().toString());
		boolean flag = this.getService().addTemplateData(map);
		postStrToClient(flag, response);
	}
	//删除模板数据
	@RequestMapping("/deleteTemplateDataByTemplateId")
	public void deleteTemplateDataByTemplateId(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		boolean flag = this.getService().deleteTemplateDataByTemplateId(map);
		postStrToClient(flag, response);
	}
	
	//修改模板数据
	@RequestMapping("/updateTemplateDataByTemplateId")
	public void updateTemplateDataByTemplateId(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		boolean flag = this.getService().updateTemplateDataByTemplateId(map);
		postStrToClient(flag, response);
	}
	
	//=======================模板数据结束============================
	public CustomTextService getService() {
		return (CustomTextService) getBean("customTextService");
	}
}
