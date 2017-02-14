package com.zinglabs.apps.chatWorkflow.action;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.zinglabs.apps.appCommonCurd.AppCommonsCurdService;
import com.zinglabs.apps.chatWorkflow.WorkflowService;
import com.zinglabs.apps.chatWorkflow.stepService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.CookieUtils;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;


@Controller
@RequestMapping("/*/Step")
public class stepAction extends BaseAction{
	
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	//修改或添加节点
	@RequestMapping(value = "/insertStep")
	public void searchStep(@RequestParam HashMap<String,String> map, HttpServletRequest request, HttpServletResponse response){
		logger.debug("\n------------"+map+" zhcn="+request.getParameter("zhcn"));
		//如果id不为空那么进行修改节点的操作
		if(map.get("id")!=""){
			String msg="";
			msg=getService().stepUpdate(map, request);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(msg.equalsIgnoreCase("添加成功")?true:false, msg),response);
		}
		//否则进行添加节点的操作
		else{
		String msg="";
		msg=getService().stepInsert1(map, request);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(msg.equalsIgnoreCase("添加成功")?true:false, msg),response);
		}
	}
	//删除节点
	@RequestMapping(value="/updateStep")
	public void updateStep(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response){
		AppCommonsCurdService as=new AppCommonsCurdService();
		map.put("beanId", "commonDeleteFilter");
		map.put("nameSpace", "com.zinglabs.apps.appCommonCurd.pojo.common_delete");
		map.put("dbid", "ZKM");
		map.put("primarykey", "id");
		int num=as.common_delete(map, request);
		if(num>0){
			map.put("primarykey", "stepId");
			map.put("tableName", "stepManage");
			int i=as.common_delete(map, request);
			if(i>0){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功",i),response);
			}
		}
	}
	
	//查询stepManage表中stepScope对应的组织机构名
	@RequestMapping(value = "/selectStepManage")
	public void selectStepManage(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		List list=getService().stepManageSelect(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",list),response);
	}
	//当模板选择技能组时添加一条节点
	@RequestMapping(value = "/skillStep")
	public  void skillStep(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		map.put("createDate", df.format(new Date()));
		String id=new RandomGUID().toString();
		map.put("id","jnz_"+id );
		map.put("serial", 0);
		if(getService().stepSkill(map, request)){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "添加成功！"),response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "添加失败！"),response);
		}
	}
	//总览  查询该模板下的所有节点 及节点的执行人状态
	@RequestMapping(value = "/searchStep")
	public void searchStep(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		map.put("role", "2");
		List list =getService().searchSelectSkill(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", list),response);
	}
	//现况  显示当前用户相关的节点 及执行状态
	@RequestMapping(value = "/searchNowStep")
	public void searchNowStep(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		try {
			String cardUrl= CookieUtils.getMhCardUrlByCookie(request, null);
			map.put("cardUrl",cardUrl);
			List list =getService().selectNowStep(map);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", list),response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 查询门户下的角色
	 * @return
	 */
	@RequestMapping(value = "/selectMHRole")
	public void selectMHRole(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		try {
			CookieUtils.pauseMyMemDBId();
			WorkflowService ws = new WorkflowService();
			List roleCode = ws.getMHRoleData();
			if(roleCode.size()>0){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", roleCode),response);
			}else {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "查询失败", roleCode),response);
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
	    	CookieUtils.startMyMemDBId();
	    }
	}
	
	public stepService getService() {
		return (stepService) getBean("stepService");
	}

}
