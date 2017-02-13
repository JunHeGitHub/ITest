package com.zinglabs.apps.activitiCommon.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.activitiCommon.service.ActivitiFormCurdService;
import com.zinglabs.apps.activitiCommon.util.FormHandle;
import com.zinglabs.apps.activitiManager.service.ActivitiManagerService;
import com.zinglabs.apps.mq.service.MQService;
import com.zinglabs.base.Interface.ActivitiFromInterface;
import com.zinglabs.base.core.activitiSupport.ActivitiUtils;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;


/**
 *  注:
 *  		1.以下为请求activiti相关action时的配置项参数。
 *  		2.表单中的字段不能与下列字段重复。
 * -----------------------------------------------------------------------
 * 
 * params.ACTIVITI的对流程相关配置:
 * A_PROCESSDEFINITIONID  流程定义标识
 * A_ASSIGNEE :"userid"   指定 处理人/(签收人) 参数。
 * A_PROCESSINSTANCEID    流程实例标识
 * A_TASKID               任务实例标识
 * A_TASKIDS              任务实例标识（签收/批量签收）A_TASKIDS={ID1,ID2,ID3}  
 * A_TASKNAME             节点的name  （多个节点用 ; 来区分）
 * 
 * 
 * A_FORMCLASS    form表单处理类
 * 
 * 可在流程图中自定义flow_type 来控制流向
 * */

@Controller
@RequestMapping("/*/activityCommon")
@SuppressWarnings("unchecked")
public class ActivitiAction extends BaseAction {
	public static Logger A_logger = LoggerFactory.getLogger("Activiti");
	
	
	/**
	 * 创建工单流程   
	 * @param map	发起流程
	 * @param request   
	 * @param response   retMap{"a_processInstanceId":"(流程实例ID)"}
	 */
	@RequestMapping(value="doStartByProcessDefinitionId")
	public void doStartByProcessDefinitionId(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
//		UserInfo2 user = cheackUser(request,response);
		ActivitiUtils ActivitiUtils = new ActivitiUtils() ;
		String user = map.get("zkm_userName").toString() ;
		map.remove("zkm_userName") ;
		String processInstanceId = "" ;
		
		FormHandle formHandle= new FormHandle() ;
		Map<String,Map> all = new HashMap() ;
		//参数验证
		all = formHandle.validateStart(map) ;
		if(!Boolean.valueOf(String.valueOf(all.get("mgsmap").get("success")))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,String.valueOf(all.get("mgsmap").get("mgs"))), response);
			return ;
		}
		//表单类验证
		if(!Boolean.valueOf(formHandle.validateClass(String.valueOf(all.get("actmap").get("A_FORMCLASS"))))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"FORMCLASS not found"), response);
			return ;
		}
		
		A_logger.debug("doStartWorkOrderProcess.action:开始:") ;
		long dateStr1= System.currentTimeMillis();
//		if(user != null&&user !=""){
			try {
				processInstanceId = ActivitiUtils.doStartProcess(all.get("actmap").get("A_PROCESSDEFINITIONID").toString());
				map.put("A_PROCESSINSTANCEID", processInstanceId) ;
				
				//设置处理人:assignee。
				if(map.get("A_ASSIGNEE")!=""&&map.get("A_ASSIGNEE")!=null){	
					String nextTaskId = ActivitiUtils.getTaskIdByProcessInstanceId(processInstanceId) ;
					ActivitiUtils.doClaim(nextTaskId, String.valueOf(map.get("A_ASSIGNEE"))) ;	
				}
				long dateStr11= System.currentTimeMillis();
				//业务数据处理过程。
				FormHandle fh = new FormHandle() ;
				all.get("actmap").put("A_PROCESSINSTANCEID", processInstanceId) ;
				all.get("actmap").put("user", user) ;
//				all.get("actmap").put("username", map.get("zkm_Name").toString()) ;
				
				Map rmap = (Map)fh.formdata(all) ;
				rmap.put("a_processInstanceId",processInstanceId) ;
				rmap.put("a_id",ActivitiUtils.getTaskIdByProcessInstanceId(processInstanceId)) ;
				long dateStr2= System.currentTimeMillis();
				A_logger.debug("doStartWorkOrderProcess.action-formClass:" +(dateStr2-dateStr11)) ;
				A_logger.debug("doStartWorkOrderProcess.action-end:"+(dateStr2-dateStr1)) ;
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"流程发起成功！",rmap), response);
				return ;
			} catch (Exception e) {
				e.printStackTrace();
				long dateStr2= System.currentTimeMillis();
				A_logger.error("doStartWorkOrderProcess.action  异常:",e) ;
				A_logger.error("doStartWorkOrderProcess.action-end:"+(dateStr2-dateStr1)) ;
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"流程发起失败！"), response);
				return ;
			}
//		}
	}

	/**
	 * 创建工单流程并提交给下一个人   
	 * @param map	发起流程
	 * @param request   
	 * @param response   retMap{"a_processInstanceId":"(流程实例ID)"}
	 */
	@RequestMapping(value="doStartAlsoSubmitByProcessDefinitionId")
	public void doStartAlsoSubmitByProcessDefinitionId(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
//		UserInfo2 user = cheackUser(request,response);
		String user = map.get("zkm_userName").toString() ;
		ActivitiUtils ActivitiUtils = new ActivitiUtils() ;
		map.remove("zkm_userName") ;
		Map<String,Map> all = new HashMap() ;
		FormHandle formHandle= new FormHandle() ;
		
		//参数验证
		all = formHandle.validateStart(map) ;
		if(!Boolean.valueOf(String.valueOf(all.get("mgsmap").get("success")))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,String.valueOf(all.get("mgsmap").get("mgs"))), response);
			return ;
		}
		//表单类验证
		if(!Boolean.valueOf(formHandle.validateClass(String.valueOf(all.get("actmap").get("A_FORMCLASS"))))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"FORMCLASS not found"), response);
			return ;
		}
		
		String processInstanceId = "" ;
		A_logger.debug("doStartWorkOrderProcess.action:开始:") ;
		long dateStr1= System.currentTimeMillis();
		if(user != null&&user !=""){
			Object processKeyObj = all.get("actmap").get("A_PROCESSDEFINITIONID");
			if(processKeyObj == null){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"请选择数据！"), response);
				return;
			}
			try {
				processInstanceId = ActivitiUtils.doStartProcess(String.valueOf(all.get("actmap").get("A_PROCESSDEFINITIONID")));
				map.put("A_PROCESSINSTANCEID", processInstanceId) ;
				String taskid = ActivitiUtils.getTaskIdByProcessInstanceId(processInstanceId) ;
				//map主要存flow_type
				ActivitiUtils.doCompletTask(taskid,all.get("actmap")) ;
				//设置处理人:assignee。
				if(map.get("A_ASSIGNEE")!=""&&map.get("A_ASSIGNEE")!=null){	
					taskid = ActivitiUtils.getTaskIdByProcessInstanceId(processInstanceId) ;
					ActivitiUtils.doClaim(taskid, String.valueOf(map.get("A_ASSIGNEE"))) ;	
				}
				//业务数据处理过程。
				
				all.get("actmap").put("A_PROCESSINSTANCEID", processInstanceId) ;
				all.get("actmap").put("user", user) ;
				Map rmap = (Map)formHandle.formdata(all) ;
				if(rmap==null){
					//业务存数据失败，删流程。
					A_logger.debug("doClearTask and processInstanceId is :"+processInstanceId+". Formdata:"+map.toString()) ;
					ActivitiUtils.doClearTask(processInstanceId,"") ;
				}
				long dateStr2= System.currentTimeMillis();
				A_logger.debug("doStartAlsoSubmitByProcessDefinitionId.action:结束  action总耗时:"+(dateStr2-dateStr1)) ;
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"流程发起成功！",map), response);
				return ;
			} catch (Exception e) {
				e.printStackTrace();
				long dateStr2= System.currentTimeMillis();
				A_logger.error("doStartAlsoSubmitByProcessDefinitionId.action 提交失败:" , e);
				A_logger.error("doStartAlsoSubmitByProcessDefinitionId.action:结束  action总耗时:"+(dateStr2-dateStr1)) ;
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"流程发起失败！"), response);
				return ;
			}
		}
	}
	
	/**
	 * 任务提交 
	 * @param map 
	 * 		必须参数:
	 * 			1.A_TASKID :任务id
	 * 			2.A_PROCESSINSTANCEID:流程实例id。
	 * 			3.flow_type=="2"  控制流向
	 *      非必要参数:
	 * 			1.A_ASSIGNEE :处理人.
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="doCompletTask")
	public void doCompletTask(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
//		UserInfo2 user = cheackUser(request,response);
		String user = map.get("zkm_userName").toString() ;
		ActivitiUtils ActivitiUtils = new ActivitiUtils() ;
		map.remove("zkm_userName") ;
		FormHandle formHandle = new FormHandle() ;
		Map<String,Map> all = new HashMap() ;
		
		//参数验证
		all = formHandle.validateComplete(map) ;
		if(!Boolean.valueOf(String.valueOf(all.get("mgsmap").get("success")))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,String.valueOf(all.get("mgsmap").get("mgs"))), response);
			return ;
		}
		//表单类验证
		if(!Boolean.valueOf(formHandle.validateClass(String.valueOf(all.get("actmap").get("A_FORMCLASS"))))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"FORMCLASS not found"), response);
			return ;
		}
		
		A_logger.debug("doCompletTask.action:开始") ;
		long dateStr1= System.currentTimeMillis();
		Object objTaskIdObj = all.get("actmap").get("A_TASKID");
		if(objTaskIdObj != null){
			String taskId = objTaskIdObj.toString();
			try {
				ActivitiUtils.doCompletTask(taskId,map);
				if(all.get("actmap").get("A_ASSIGNEE")!=""&&all.get("actmap").get("A_ASSIGNEE")!=null){	
					if(all.get("actmap").get("A_PROCESSINSTANCEID")!=null&&all.get("actmap").get("A_PROCESSINSTANCEID")!=""){
						String nextTaskId = ActivitiUtils.getTaskIdByProcessInstanceId(String.valueOf(all.get("actmap").get("A_PROCESSINSTANCEID"))) ;
						ActivitiUtils.doClaim(nextTaskId, String.valueOf(all.get("actmap").get("A_ASSIGNEE"))) ;	
					}else{
						A_logger.debug("指定处理人失败:页面传值缺少A_PROCESSINSTANCEID。"+all.toString()) ;
					}
				}
				long dateStr11= System.currentTimeMillis();
				//业务数据处理过程。
				all.get("actmap").put("user", user) ;
//				all.get("actmap").put("username", map.get("zkm_Name").toString()) ;
				Map rmap = (Map)formHandle.formdata(all) ;
				if(rmap==null){
					//业务存数据失败，还原流程。
					
				}
				long dateStr2= System.currentTimeMillis();
				A_logger.debug("doCompletTask.action-formClass:" +(dateStr2-dateStr11)) ;
				A_logger.debug("doCompletTask.action-end:"+(dateStr2-dateStr1)) ;
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"提交成功！"), response);
				return ;
			} catch (Exception e) {
				long dateStr2= System.currentTimeMillis();
				A_logger.error("doCompletTask.action:提交结束,流程已提交  action总耗时:"+(dateStr2-dateStr1)) ;
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"提交失败！"), response);
				e.printStackTrace();
				return ;
			}
		}else{
			A_logger.error("提交失败--缺少任务标识:"+map.toString()) ;
			long dateStr2= System.currentTimeMillis();
			A_logger.debug("doCompletTask.action:提交结束  action总耗时:"+(dateStr2-dateStr1)) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"缺少任务标识！"), response);
			return ;
		}
	}
	
	/**
	 * 获取某一个流程某一个节点未签收任务
	 * 必须参数:
	 * 			1.A_PROCESSDEFINITIONID :流程标识
	 * 			2.A_TASKNAME:节点名称
	 * 	可选参数(分页查询需要):
	 * 			====mysql limit规则
	 *         1、A_BeginNum：开始位置(int类型);
	 *         2、A_MaxNum：每页展示长度(int类型);
	 *        ====默认查询从第一条开始的前十条数据-----即默认为beginNum=0,maxNum=10;
	 * */
	@RequestMapping(value="getTaskUnassigned")
	public void getTaskUnassigned(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		String user = map.get("zkm_userName").toString() ;
		ActivitiUtils ActivitiUtils = new ActivitiUtils() ;
		map.remove("zkm_userName") ;
		String processInstanceId = "" ;
		Map<String,Map> all = new HashMap() ;
		FormHandle formHandle= new FormHandle() ;

		//参数验证
		all = formHandle.validateGetUnClaimTask(map) ;
		if(!Boolean.valueOf(String.valueOf(all.get("mgsmap").get("success")))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,String.valueOf(all.get("mgsmap").get("mgs"))), response);
			return ;
		}
		//表单类验证
		if(!Boolean.valueOf(formHandle.validateClass(String.valueOf(all.get("actmap").get("A_FORMCLASS"))))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"FORMCLASS not found"), response);
			return ;
		}
		
		//分页参数设置
		int beginNum=0; //开始位置默认0
		int maxNum=10; //结束位置默认10
		String strbeginNum=String.valueOf(all.get("formmap").get("A_BeginNum"));
		String strmaxNum=String.valueOf(all.get("formmap").get("A_MaxNum"));
		if(!strbeginNum.equals("")&&!strbeginNum.equals("null")&&!strmaxNum.equals("")&&!strmaxNum.equals("null")){
			beginNum=Integer.parseInt(strbeginNum);
			maxNum=Integer.parseInt(strmaxNum);	
		}
		
		A_logger.debug("getTaskUnassigned.action:开始") ;
		long dateStr1= System.currentTimeMillis();
		try {
			Map parammap = ActivitiManagerService.activtiParamMap.get(String.valueOf(((Map)all.get("actmap")).get("A_PROCESSDEFINITIONID"))) ;

			Map unMap =ActivitiUtils.getTaskUnassigned(String.valueOf(all.get("actmap").get("A_TASKNAME")),String.valueOf(all.get("actmap").get("A_PROCESSDEFINITIONID")),beginNum,maxNum );
			//业务数据处理过程。
			if("0".equals(unMap.get("total").toString())){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"没有流程数据",parammap), response);
				return ;
			}
			long dateStr11= System.currentTimeMillis();
			all.get("actmap").put("user", user) ;
			all.put("activitiSelectMap", unMap) ;
			Map rmap = (Map)formHandle.formdata(all) ;
			rmap.putAll(parammap) ;
			if(!(((List)rmap.get("resultformdata")).size()>0)){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"没有流程数据",rmap), response);
				return ;
			}
			
			List ulist = (List)unMap.get("taskData") ;
			List rlist = (List)rmap.get("resultformdata") ;
			for(int i=0 ;i<ulist.size(); i++){
				for(int j=0 ;j<rlist.size();j++){
					if((String.valueOf(((Map)ulist.get(i)).get("a_processInstanceId"))).equals((String.valueOf(((Map)rlist.get(j)).get("flowId"))))){
						((Map)ulist.get(i)).putAll(((Map)rlist.get(j))) ;
						continue ;
					}
				}
			}
			rmap.put("resultformdata", ulist) ;
			rmap.put("total", unMap.get("total").toString()) ;
			long dateStr2= System.currentTimeMillis();
			A_logger.debug("getTaskUnassigned.action-formClass:" +(dateStr2-dateStr11)) ;
			A_logger.debug("getTaskUnassigned.action-end:"+(dateStr2-dateStr1)) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"获取任务成功！",rmap), response);
			return ;
		} catch (Exception e) {
			A_logger.error("getTaskUnassigned.action 异常:" , e);
			long dateStr2= System.currentTimeMillis();
			A_logger.error("getTaskUnassigned.action-end:"+(dateStr2-dateStr1)) ;
			e.printStackTrace();
			return ;
		}
	}


	/**
	 * 根据任务id签收任务
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="doClaimByTaskId")
	public void doClaimByTaskId(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
//		UserInfo2 user = cheackUser(request,response);
		A_logger.debug("doClaimByTaskId.action:开始:") ;
		ActivitiUtils ActivitiUtils = new ActivitiUtils() ;
		long dateStr1= System.currentTimeMillis();
		String user = map.get("zkm_userName").toString() ;
		map.remove("zkm_userName") ;
		Map<String,Map> all = new HashMap() ;
		FormHandle formHandle= new FormHandle() ;

		//参数验证
		all = formHandle.validatedoClaimByTaskId(map) ;
		if(!Boolean.valueOf(String.valueOf(all.get("mgsmap").get("success")))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,String.valueOf(all.get("mgsmap").get("mgs"))), response);
			return ;
		}
		//表单类验证
		if(!Boolean.valueOf(formHandle.validateClass(String.valueOf(all.get("actmap").get("A_FORMCLASS"))))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"FORMCLASS not found"), response);
			return ;
		}
		all.get("actmap").put("user", user) ;
//		all.get("actmap").put("username", map.get("zkm_Name").toString()) ;
		List<Map> claimlist = new ArrayList() ;
		if(user != null&&user !=""){
			Object taskIdsObj = all.get("actmap").get("A_TASKIDS");
			if(taskIdsObj == null){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"请选择数据！"), response);
				return ;
			}
			String[] ids = taskIdsObj.toString().split(";");
				int count =0;
				for(String id : ids){
					try {
						Map taskmap = new HashMap() ;
						Task task = ActivitiUtils.findTaskById(id) ;
						taskmap.put("processinstanceId", task.getProcessInstanceId()) ;
						taskmap.put("taskName", task.getName()) ;
						claimlist.add(taskmap) ;
						ActivitiUtils.doClaim(id,user);
						count++;
//						A_logger.debug("doClaimByTaskId.action:签收id:"+id) ;
					} catch (Exception e) {
						A_logger.error("流程已签收!") ;
						A_logger.error("doClaimByTaskId.action:结束:") ;
					}
				}
				//业务数据处理过程。--修改数据标准为业务数据主表ID
				Map cmap = new HashMap() ;
				cmap.put("claimlist", claimlist) ;
				all.put("claimlist",cmap) ;
				Map rmap = (Map)formHandle.formdata(all) ;
			long dateStr2= System.currentTimeMillis();
			A_logger.debug("doClaimByTaskId.action:结束  action总耗时:"+(dateStr2-dateStr1));
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"签收成功！",count), response);
		}
	}
	
	/**
	 * 获取用户某一个流程的任务
	 *必须参数:
	 * 			1.A_PROCESSDEFINITIONID :流程标识
	 * 			2.A_ASSIGNEE:当前处理人
	 * 	可选参数(分页查询需要):
	 * 			====mysql limit规则
	 *        1、A_BeginNum：开始位置(int类型);
	 *        2、A_MaxNum：每页展示长度(int类型);
	 *        ====默认查询从第一条开始的前十条数据-----即默认为beginNum=0,maxNum=10; 
	 * */
	@RequestMapping(value="getUserTask")
	public void getTaskUserTask(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		String user = map.get("zkm_userName").toString() ;
		map.remove("zkm_userName") ;
		ActivitiUtils ActivitiUtils = new ActivitiUtils() ;
		Map<String,Map> all = new HashMap() ;
		FormHandle formHandle= new FormHandle() ;

		//参数验证
		all = formHandle.validateUserTask(map) ;
		if(!Boolean.valueOf(String.valueOf(all.get("mgsmap").get("success")))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,String.valueOf(all.get("mgsmap").get("mgs"))), response);
			return ;
		}
		//表单类验证
		if(!Boolean.valueOf(formHandle.validateClass(String.valueOf(all.get("actmap").get("A_FORMCLASS"))))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"FORMCLASS not found"), response);
			return ;
		}
		//分页参数设置
		int beginNum=0; //开始位置默认0
		int maxNum=10; //结束位置默认10
		String strbeginNum=String.valueOf(all.get("formmap").get("A_BeginNum"));
		String strmaxNum=String.valueOf(all.get("formmap").get("A_MaxNum"));
		if(!strbeginNum.equals("")&&!strbeginNum.equals("null")&&!strmaxNum.equals("")&&!strmaxNum.equals("null")){
			beginNum=Integer.parseInt(strbeginNum);
			maxNum=Integer.parseInt(strmaxNum);	
		}
		
		A_logger.debug("getUserTask.action:开始") ;
		long dateStr1= System.currentTimeMillis();
		try {
			Map parammap = ActivitiManagerService.activtiParamMap.get(String.valueOf(((Map)all.get("actmap")).get("A_PROCESSDEFINITIONID"))) ;
			Map unMap = ActivitiUtils.getWaitTaskForUserAndKey(user,String.valueOf(all.get("actmap").get("A_PROCESSDEFINITIONID")),beginNum,maxNum) ;
			//业务数据处理过程。
			if("0".equals(unMap.get("total").toString())){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"没有流程数据",parammap), response);
				return ;
			}
			all.put("activitiSelectMap", unMap) ;
			all.get("actmap").put("user", user) ;
			long dateStr11= System.currentTimeMillis();
			Map rmap = (Map)formHandle.formdata(all) ;
			if(rmap==null||rmap.get("resultformdata")==null||!(((List)rmap.get("resultformdata")).size()>0)){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"没有流程数据",parammap), response);
				return ;
			}
			rmap.putAll(parammap) ;
			List ulist = (List)unMap.get("taskData") ;
			List rlist = (List)rmap.get("resultformdata") ;
			rmap.put("total", unMap.get("total")) ;
			for(int i=0 ;i<ulist.size(); i++){
				for(int j=0 ;j<rlist.size();j++){
					if((String.valueOf(((Map)ulist.get(i)).get("a_processInstanceId"))).equals((String.valueOf(((Map)rlist.get(j)).get("flowId"))))){
						((Map)ulist.get(i)).putAll(((Map)rlist.get(j))) ;
						continue ;
					}
				}
			}
			rmap.put("resultformdata", ulist) ;
			if(rmap==null){
				//业务存数据失败，还原流程。
			}
			long dateStr2= System.currentTimeMillis();
			A_logger.debug("getUserTask.action-formClass:"+(dateStr2-dateStr11)) ;
			A_logger.debug("getUserTask.action-end:"+(dateStr2-dateStr1)) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"获取任务成功！",rmap), response);
			return ;
			
		} catch (Exception e) {
			A_logger.error("getUserTask.action 异常:" , e);
			long dateStr2= System.currentTimeMillis();
			A_logger.error("getUserTask.action: 总耗时:"+(dateStr2-dateStr1)) ;
			e.printStackTrace();
		}
	}
	
	/**
	 * 留言重置
	 * */
	@RequestMapping(value="resettingMessage")
	public void resettingMessage(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){ 
		String processInstanceId = "" ;
		String processKey = "";
		ActivitiUtils activitiUtils = new ActivitiUtils() ;
		Map<String,Map> all = new HashMap() ;
		String dataId = String.valueOf(map.get("id")) ;
		String loginName = String.valueOf(map.get("loginName")) ;
		processKey= String.valueOf(map.get("processKey")) ;
		List<Map> r = new ArrayList<Map>()  ;
		Map rmap = new HashMap() ;
		try{
			r = getService().selectMessageFlowId(map) ;
			if(r==null||r.isEmpty()){
				A_logger.error("未找到查询留言流程,，id:"+dataId)  ;
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"未创建流程！",r), response);
				return ;
			}
			rmap = r.get(0) ;
		}catch(Exception e){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"未创建流程！",r), response);
			A_logger.error("未找到查询留言流程，id:"+dataId,e) ;
			return ;
		}
		if(r.size()>0&&r.get(0).get("flowId").toString()!=""){
			try{
				processInstanceId = r.get(0).get("flowId").toString() ;
				activitiUtils.doClearTask(processInstanceId,"重置") ;
			}catch(Exception e){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"未创建流程！",r), response);
				return ;
			}
			
			try{
				String processkey = r.get(0).get("flowType").toString() ;
				String newprocessInstanceId = activitiUtils.doStartProcess(processkey);
				String taskid = activitiUtils.getTaskIdByProcessInstanceId(newprocessInstanceId) ;
				//map主要存flow_type
				map.put("flow_type", "2") ;
				activitiUtils.doCompletTask(taskid,map) ;
				Map pmap = new HashMap() ;
				pmap.put("dataId",dataId) ;
				pmap.put("flowId", newprocessInstanceId) ;
				String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss") ;
				pmap.put("endDealTime", date) ;
				pmap.put("nodeName", "留言复核") ;
				pmap.put("flowNode", "liuYanFH") ;
				getService().updateMessagePorcessProcessinstanceId(pmap) ;
				String uuid= new RandomGUID().toString() ;
				rmap.put("id", uuid) ;
				rmap.put("disponseUser", loginName) ;
				rmap.put("endDealTime", date) ;
				getService().insertMessagePorcessHis(rmap)  ;
				A_logger.debug("activitiUtils.doStartProcess:dataId"+dataId+".流程标识:"+processKey) ;
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"重置成功！"), response);
				return ;
			}catch(Exception e){
				A_logger.debug("activitiUtils.doStartProcess:dataId"+dataId+".流程标识:"+processKey) ;
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"重置流程失败！",r), response);
				return ;
			}
			
		}else{
			A_logger.debug("can not resettingMessage:dataId"+dataId+".流程标识:"+processKey) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"未创建流程！",r), response);
			return ;
		}
	}
	/**
	 * 必填参数
	 * A_FORMCLASS
	 * A_PROCESSINSTANCEID
	 * */
	@RequestMapping(value="clearTask")
	public void clearTask(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){ 
		ActivitiUtils activitiUtils = new ActivitiUtils() ;
		Map<String,Map> all = new HashMap() ;
		String dataId = String.valueOf(map.get("id")) ;
		String loginName = String.valueOf(map.get("loginName")) ;
		String processInstanceId = "" ;
		FormHandle formHandle = new FormHandle() ;
		if(map.get("A_FORMCLASS")==null &&!(String.valueOf(map.get("A_FORMCLASS").toString()).length()>0)){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"删除失败！A_FORMCLASS为空"), response);
		}
		if(map.get("A_PROCESSINSTANCEID")==null &&!(String.valueOf(map.get("A_PROCESSINSTANCEID").toString()).length()>0)){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"删除失败！A_PROCESSINSTANCEID为空"), response);
		}
		try{
			processInstanceId = String.valueOf(map.get("A_PROCESSINSTANCEID")) ;
			activitiUtils.doClearTask(processInstanceId, "删除公告") ;
			ActivitiFromInterface obj = (ActivitiFromInterface)getBean(String.valueOf(map.get("A_FORMCLASS"))) ;
			obj.execute(map) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"删除成功！"), response);
			return ;
		}catch(Exception e){
			A_logger.debug("activitiUtils.doStartProcess:dataId"+dataId,e) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"删除成功！"), response);
			e.printStackTrace() ;
			return ;
		}
	}
	
	/**
	 * 工单转办
	 * gongdanState
	 * A_FORMCLASS
	 * A_PROCESSINSTANCEID
	 * */
	@RequestMapping(value="transferAssignee")
	public void transferAssignee(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){ 
		ActivitiUtils activitiUtils = new ActivitiUtils() ;
		Map<String,Map> all = new HashMap() ;
		String dataId = String.valueOf(map.get("id")) ;
		String loginName = String.valueOf(map.get("loginName")) ;
		String disponseUser = String.valueOf(map.get("disponseUser")!=null?map.get("disponseUser"):"") ;
		String gongdanState = String.valueOf(map.get("gongdanState")!=null?map.get("gongdanState"):"");
		String flowId = String.valueOf(map.get("flowId")!=null?map.get("flowId"):"") ;
		if("".equals(disponseUser)||"".equals(gongdanState)||"".equals(flowId)){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"缺少参数"), response);
			return ;
		}
//		FormHandle formHandle = new FormHandle() ;
//		if(map.get("A_FORMCLASS")==null &&!(String.valueOf(map.get("A_FORMCLASS").toString()).length()>0)){
//			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"删除失败！A_FORMCLASS为空"), response);
//		}
//		if(map.get("A_PROCESSINSTANCEID")==null &&!(String.valueOf(map.get("A_PROCESSINSTANCEID").toString()).length()>0)){
//			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"删除失败！A_PROCESSINSTANCEID为空"), response);
//		}
		try{
			Task task = activitiUtils.getTaskByProcessInstanceId(flowId) ;
			activitiUtils.transferAssignee(task.getId(), disponseUser) ;
			task = activitiUtils.getTaskByProcessInstanceId(flowId) ;
			//处理时间
			String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss") ;
			map.put("disponseBTime", date) ;
			map.put("endDealTime", date) ;
			map.put("disponseETime", "0000-00-00 00:00:00") ;
			if("复核中".equals(map.get("gongdanState"))){
				map.put("fuherenId", disponseUser) ;
				map.put("fuherenTime", date) ;
			}
			getService().GL_updategongdanByflowId_ZhuanBan(map) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"删除成功！"), response);
			return ;
		}catch(Exception e){
			A_logger.debug("activitiUtils.doStartProcess:dataId"+dataId,e) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"删除成功！"), response);
			e.printStackTrace() ;
			return ;
		}
	}
	/**
	 * 工单处理
	 * gongdanState
	 * A_FORMCLASS
	 * A_PROCESSINSTANCEID
	 * */
	@RequestMapping(value="gongdanChuli")
	public void gongdanChuli(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){ 
		ActivitiUtils activitiUtils = new ActivitiUtils() ;
		Map<String,Map> all = new HashMap() ;
		String dataId = String.valueOf(map.get("id")) ;
		String loginName = String.valueOf(map.get("loginName")) ;
		String A_ASSIGNEE = String.valueOf(map.get("A_ASSIGNEE")!=null?map.get("A_ASSIGNEE"):"") ;
		String gongdanState = String.valueOf(map.get("gongdanState")!=null?map.get("gongdanState"):"");
		String flowId = String.valueOf(map.get("flowId")!=null?map.get("flowId"):"") ;
		if("".equals(A_ASSIGNEE)||"".equals(gongdanState)||"".equals(flowId)){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"缺少参数"), response);
			return ;
		}
		try{
			Task task = activitiUtils.getTaskByProcessInstanceId(flowId) ;
			try{
				activitiUtils.doClaim(task.getId(), A_ASSIGNEE) ;
			}catch(Exception e){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"已被其他人签收"), response);
				return ;
			}
			
			task = activitiUtils.getTaskByProcessInstanceId(flowId) ;
			//处理人
			map.put("disponseUser", A_ASSIGNEE) ;
			//处理时间
			String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss") ;
			map.put("disponseBTime", date) ;
			map.put("endDealTime", date) ;
			map.put("disponseETime", "0000-00-00 00:00:00") ;
			if(map.get("gongdanState")!=null&&"待复核".equals(map.get("gongdanState"))){
				map.put("fuheTime", date) ;
				map.put("fuherenId", A_ASSIGNEE) ;
				map.put("gongdanState", "复核中") ;
			}
			if(map.get("gongdanState")!=null&&"待回复".equals(map.get("gongdanState"))){
				map.put("disponseTime", date) ;
				map.put("gongdanState", "联系中") ;
			}
			getService().gongdanupdateByProcessinstanceId(map) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"删除成功！"), response);
			return ;
		}catch(Exception e){
			A_logger.debug("activitiUtils.doStartProcess:dataId"+dataId,e) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"删除成功！"), response);
			e.printStackTrace() ;
			return ;
		}
	}
	
	@RequestMapping(value="doUnclaim")
	public void doUnclaim(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){ 
		ActivitiUtils activitiUtils = new ActivitiUtils() ;
		Map<String,Map> all = new HashMap() ;
		String dataId = String.valueOf(map.get("id")) ;
		String loginName = String.valueOf(map.get("loginName")) ;
		String A_ASSIGNEE = String.valueOf(map.get("A_ASSIGNEE")!=null?map.get("A_ASSIGNEE"):"") ;
		String gongdanState = String.valueOf(map.get("gongdanState")!=null?map.get("gongdanState"):"");
		String flowId = String.valueOf(map.get("flowId")!=null?map.get("flowId"):"") ;
		String type = String.valueOf(map.get("type")!=null?map.get("type"):"") ;
		if("".equals(gongdanState)||"".equals(flowId)){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"缺少参数"), response);
			return ;
		}
		try{
			Task task = activitiUtils.getTaskByProcessInstanceId(flowId) ;
			try{
				activitiUtils.douUnClaim(task.getId()) ;
			}catch(Exception e){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"已被其他人处理"), response);
				return ;
			}
			if("fuHe".equals(type)){
				//处理人
				map.put("disponseUser", "") ;
				map.put("fuHeTime", "0000-00-00 00:00:00") ;
				map.put("fuherenId", "") ;
			}
			if("huiFuZuoXi".equals(type)){
				//处理人
				map.put("disponseUser", "") ;
			}
			//处理时间
			String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss") ;
			map.put("disponseBTime", "0000-00-00 00:00:00") ;
			map.put("endDealTime", date) ;
			map.put("disponseETime", "0000-00-00 00:00:00") ;
			getService().gongdanupdateByProcessinstanceId(map) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"删除成功！"), response);
			return ;
		}catch(Exception e){
			A_logger.debug("activitiUtils.doStartProcess:dataId"+dataId,e) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"删除成功！"), response);
			e.printStackTrace() ;
			return ;
		}
	}
	
	@RequestMapping("/resendMQ")
	public void sendMail(@RequestParam Map map, HttpServletRequest request,
			HttpServletResponse response) {
		String gongdanId = map.get("gongdanId").toString() ;
		Map rmap = getService().selectGongdanById(gongdanId) ;
		MQService mq = new MQService() ;
		try{
			//需要附件
			A_logger.debug("工单:"+gongdanId+",sendMQ again:") ;
			mq.sendMsg(rmap.get("gongdanType").toString(), rmap.get("gongdanId").toString(), "", rmap) ;
		}catch(Exception e){
			A_logger.debug("工单:"+gongdanId+",sendMQ again error:",e) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"发送失败！"), response);
			return ;
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"已发送！"), response);
	}
	public ActivitiFormCurdService getService() {
		return (ActivitiFormCurdService) getBean("ActivitiFormCurdService");
	}
	
	@RequestMapping(value="ceshi")
	public void ceshi(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		ActivitiUtils ActivitiUtils = new ActivitiUtils() ;
		String user = new RandomGUID().toString() ;
		String A_PROCESSDEFINITIONID="BOC_gongdan";
		String processInstanceId = "" ;
		long dateStr1= System.currentTimeMillis();
		Map<String,Map> startMap = new HashMap() ;
		startMap.put("actmap", new HashMap()) ;
		startMap.put("formmap", new HashMap()) ;
		startMap.get("actmap").put("A_PROCESSDEFINITIONID", "BOC_gongdan") ;
		startMap.get("actmap").put("A_FORMCLASS", "GongdanInsert") ;
		startMap.get("formmap").put("chuangjianrenId", "admin") ;
		startMap.get("formmap").put("chuangjianrenName=1", "管理员") ;
		startMap.get("formmap").put("gongdanState", "新建") ;
		startMap.get("formmap").put("companyId", "01") ;
		try{
			processInstanceId = ActivitiUtils.doStartProcess(A_PROCESSDEFINITIONID);
			long dateStr2= System.currentTimeMillis();
			A_logger.debug("user:"+user+"-新建:"+"doStartWorkOrderProcess.action-ActivitiUtils.doStartProcess:" +(dateStr2-dateStr1)) ;
			map.put("A_PROCESSINSTANCEID", processInstanceId) ;
			
			//设置处理人:assignee。
			String nextTaskId = ActivitiUtils.getTaskIdByProcessInstanceId(processInstanceId) ;
			ActivitiUtils.doClaim(nextTaskId, user) ;	
			long dateStr3= System.currentTimeMillis();
			A_logger.debug("user:"+user+"-（指定自己）新建:"+"doStartWorkOrderProcess.action-ActivitiUtils.doClaim:" +(dateStr3-dateStr2)) ;
			//业务数据处理过程。
			FormHandle fh = new FormHandle() ;
			startMap.get("actmap").put("A_PROCESSINSTANCEID", processInstanceId) ;
			Map rmap = (Map)fh.formdata(startMap) ;
			long dateStr4= System.currentTimeMillis();
			A_logger.debug("user:"+user+"-新建:"+"doStartWorkOrderProcess.action-formClass:" +(dateStr4-dateStr3)) ;
//			A_logger.debug("user:"+user+"-新建:"+"doStartWorkOrderProcess.action-end:"+(dateStr4-dateStr1)) ;
		}catch(Exception e){
			A_logger.error("新建流程失败",e) ;
			e.printStackTrace() ;
			return ;
		}
		//////////////////////////////////////////////////
		Map<String,Map> getUserMap = new HashMap() ;
		getUserMap.put("actmap", new HashMap()) ;
		getUserMap.get("actmap").put("A_PROCESSDEFINITIONID", "BOC_gongdan") ;
		getUserMap.get("actmap").put("A_FORMCLASS", "GongdanSelect") ;
		getUserMap.put("formmap", new HashMap()) ;
		Map userMap = new HashMap() ;
		try {
			long dateStr5= System.currentTimeMillis();
			Map parammap = ActivitiManagerService.activtiParamMap.get(String.valueOf((getUserMap.get("actmap")).get("A_PROCESSDEFINITIONID"))) ;
			Map unMap = ActivitiUtils.getWaitTaskForUserAndKey(String.valueOf(getUserMap.get("actmap").get("A_PROCESSDEFINITIONID")),user) ;
			long dateStr55= System.currentTimeMillis();
			A_logger.debug("user:"+user+"-查自己的待办:"+"getUserTask.action-ActivitiUtils.getWaitTaskForUserAndKey:"+(dateStr55-dateStr5)) ;
			//业务数据处理过程。
			if("0".equals(unMap.get("total").toString())){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"没有流程数据",parammap), response);
				return ;
			}
			getUserMap.put("activitiSelectMap", unMap) ;
			long dateStr6= System.currentTimeMillis();
			FormHandle formHandle = new FormHandle() ;
			Map rmap = (Map)formHandle.formdata(getUserMap) ;
			rmap.putAll(parammap) ;
			if(!(((List)rmap.get("resultformdata")).size()>0)){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"没有流程数据",rmap), response);
				return ;
			}
			List ulist = (List)unMap.get("taskData") ;
			List rlist = (List)rmap.get("resultformdata") ;
			for(int i=0 ;i<rlist.size(); i++){
				for(int j=0 ;j<ulist.size();j++){
					if((String.valueOf(((Map)ulist.get(j)).get("a_processInstanceId"))).equals((String.valueOf(((Map)rlist.get(i)).get("flowId"))))){
						((Map)rlist.get(i)).putAll(((Map)ulist.get(j))) ;
						continue ;
					}
				}
			}
			if(rmap==null){
				//业务存数据失败，还原流程。
			}
			userMap = rmap ;
			long dateStr7= System.currentTimeMillis();
			A_logger.debug("user:"+user+"-查自己的待办:"+"getUserTask.action-formClass:"+(dateStr7-dateStr6)) ;
			
			
		} catch (Exception e) {
			A_logger.error("user:"+user+"-查自己的待办:"+"getUserTask.action异常:" , e);
			e.printStackTrace();
			return ;
		}
		
		////////////////////////////////
		List userlist = (List)userMap.get("resultformdata") ;
		Map umap = (Map)userlist.get(0) ;
		
		Map<String,Map> completeMap = new HashMap() ;
		completeMap.put("actmap", new HashMap()) ;
		completeMap.put("formmap", new HashMap()) ;
		
		completeMap.get("actmap").put("A_PROCESSDEFINITIONID", "BOC_gongdan") ;
		completeMap.get("actmap").put("A_TASKID", umap.get("a_id")) ;
		completeMap.get("actmap").put("A_FORMCLASS",String.valueOf("GongdanUpdate")) ;
		completeMap.get("formmap").put("gongdanId", umap.get("gongdanId")) ;
		completeMap.get("formmap").put("departmentId", "0101") ;
		completeMap.get("formmap").put("departmentName=1", "0101") ;
		completeMap.get("formmap").put("gongdanState", "待复核") ;
		completeMap.get("formmap").put("companyId", "0101") ;
		
		try {
			long dateStr11= System.currentTimeMillis();
			ActivitiUtils.doCompletTask(completeMap.get("actmap").get("A_TASKID").toString(),map);		
			long dateStr22= System.currentTimeMillis();
			A_logger.debug("user:"+user+"-提交:"+"doCompletTask.action-ActivitiUtils.doCompletTask:" +(dateStr22-dateStr11)) ;
			//业务数据处理过程。
			FormHandle formHandle = new FormHandle() ;
//			Map rmap = (Map)formHandle.formdata(completeMap) ;
			long dateStr33= System.currentTimeMillis();
			A_logger.debug("user:"+user+"-提交:"+"doCompletTask.action-formClass:" +(dateStr33-dateStr22)) ;
		} catch (Exception e) {
			A_logger.error("doCompletTask.action 提交失败:" , e);
			e.printStackTrace();
			return ;
		}
//////////////////////////////////////////////////		
		Map<String,Map> getUnMap = new HashMap() ;
		getUnMap.put("actmap", new HashMap()) ;
		getUnMap.put("formmap", new HashMap()) ;
		getUnMap.get("actmap").put("A_PROCESSDEFINITIONID", "BOC_gongdan") ;
		getUnMap.get("actmap").put("A_TASKNAME", "fuHe") ;
		getUnMap.get("actmap").put("A_FORMCLASS", "SelectFormClass") ;
		Map unClaim = new HashMap() ;
		try {
			long dateStr11= System.currentTimeMillis();
			Map parammap = ActivitiManagerService.activtiParamMap.get(String.valueOf(((Map)getUnMap.get("actmap")).get("A_PROCESSDEFINITIONID"))) ;

			Map unMap =ActivitiUtils.getTaskUnassigned(String.valueOf(getUnMap.get("actmap").get("A_TASKNAME")),String.valueOf(getUnMap.get("actmap").get("A_PROCESSDEFINITIONID")) );
			long dateStr22= System.currentTimeMillis();
			A_logger.debug("user:"+user+"-查询未签收流程:"+"getTaskUnassigned.action-ActivitiUtils.getTaskUnassigned:" +(dateStr22-dateStr11)) ;
			//业务数据处理过程。
			if("0".equals(unMap.get("total").toString())){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"没有流程数据",parammap), response);
				return ;
			}
			getUnMap.put("activitiSelectMap", unMap) ;
			FormHandle formHandle = new FormHandle() ;
			Map rmap = (Map)formHandle.formdata(getUnMap) ;
			rmap.putAll(parammap) ;
			if(!(((List)rmap.get("resultformdata")).size()>0)){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"没有流程数据",rmap), response);
				return ;
			}
			List ulist = (List)unMap.get("taskData") ;
			List rlist = (List)rmap.get("resultformdata") ;
			for(int i=0 ;i<rlist.size(); i++){
				for(int j=0 ;j<ulist.size();j++){
					if((String.valueOf(((Map)ulist.get(j)).get("a_processInstanceId"))).equals((String.valueOf(((Map)rlist.get(i)).get("flowId"))))){
						((Map)rlist.get(i)).putAll(((Map)ulist.get(j))) ;
						continue ;
					}
				}
			}
			unClaim = rmap ;
			long dateStr33= System.currentTimeMillis();
			A_logger.debug("user:"+user+"-查询未签收流程:"+"getTaskUnassigned.action-formClass:" +(dateStr33-dateStr22)) ;
		} catch (Exception e) {
			A_logger.error("getTaskUnassigned.action 异常:" , e);
			e.printStackTrace();
			return ;
		}
////////////////////////////////////////////////////////////////////
		List clist = (List)unClaim.get("resultformdata") ;
		Map forclaim = (Map)clist.get(0) ;
		Map<String,Map> claimMap = new HashMap() ;
		claimMap.put("actmap", new HashMap()) ;
		claimMap.put("formmap", new HashMap()) ;
		claimMap.get("actmap").put("A_PROCESSDEFINITIONID", "BOC_gongdan") ;
		claimMap.get("actmap").put("A_TASKID", forclaim.get("a_id") );
		try {
			long dateStr22= System.currentTimeMillis();
			String nextTaskId = ActivitiUtils.getTaskIdByProcessInstanceId(processInstanceId) ;
			ActivitiUtils.doClaim(nextTaskId, user) ;	
//			ActivitiUtils.doClaim(forclaim.get("a_id").toString(),user);
			long dateStr33= System.currentTimeMillis();
			A_logger.debug("user"+user +"-签收:"+"doClaimByTaskId.action-ActivitiUtils.doClaim:签收id:"+forclaim.get("a_id").toString()+"时间:"+(dateStr33-dateStr22)) ;
		} catch (Exception e) {
			A_logger.error("签收异常，taskid不存在",e) ;
			A_logger.error("doClaimByTaskId.action:结束:") ;
			e.printStackTrace();
		}
////////////////////////////////////////////////////////////////////
		
		getUserMap = new HashMap() ;
		getUserMap.put("actmap", new HashMap()) ;
		getUserMap.get("actmap").put("A_PROCESSDEFINITIONID", "BOC_gongdan") ;
		getUserMap.get("actmap").put("A_FORMCLASS", "GongdanSelect") ;
		getUserMap.put("formmap", new HashMap()) ;
		userMap = new HashMap() ;
		try {
			long dateStr5= System.currentTimeMillis();
			Map parammap = ActivitiManagerService.activtiParamMap.get(String.valueOf((getUserMap.get("actmap")).get("A_PROCESSDEFINITIONID"))) ;
			Map unMap = ActivitiUtils.getWaitTaskForUserAndKey(String.valueOf(getUserMap.get("actmap").get("A_PROCESSDEFINITIONID")),user) ;
			long dateStr55= System.currentTimeMillis();
			A_logger.debug("user:"+user+"-查自己的待办:"+"getUserTask.action-ActivitiUtils.getWaitTaskForUserAndKey:"+(dateStr55-dateStr5)) ;
			//业务数据处理过程。
			if("0".equals(unMap.get("total").toString())){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"没有流程数据",parammap), response);
				return ;
			}
			getUserMap.put("activitiSelectMap", unMap) ;
			long dateStr6= System.currentTimeMillis();
			FormHandle formHandle = new FormHandle() ;
			Map rmap = (Map)formHandle.formdata(getUserMap) ;
			rmap.putAll(parammap) ;
			if(!(((List)rmap.get("resultformdata")).size()>0)){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"没有流程数据",rmap), response);
				return ;
			}
			List ulist = (List)unMap.get("taskData") ;
			List rlist = (List)rmap.get("resultformdata") ;
			for(int i=0 ;i<rlist.size(); i++){
				for(int j=0 ;j<ulist.size();j++){
					if((String.valueOf(((Map)ulist.get(j)).get("a_processInstanceId"))).equals((String.valueOf(((Map)rlist.get(i)).get("flowId"))))){
						((Map)rlist.get(i)).putAll(((Map)ulist.get(j))) ;
						continue ;
					}
				}
			}
			if(rmap==null){
				//业务存数据失败，还原流程。
			}
			userMap = rmap ;
			long dateStr7= System.currentTimeMillis();
			A_logger.debug("user:"+user+"-查自己的待办:"+"getUserTask.action-formClass:"+(dateStr7-dateStr6)) ;
			
			
		} catch (Exception e) {
			A_logger.error("user:"+user+"-查自己的待办:"+"getUserTask.action异常:" , e);
			e.printStackTrace();
			return ;
		}
		
		////////////////////////////////
		userlist = (List)userMap.get("resultformdata") ;
		umap = (Map)userlist.get(0) ;
		
		completeMap = new HashMap() ;
		completeMap.put("actmap", new HashMap()) ;
		completeMap.put("formmap", new HashMap()) ;
		
		completeMap.get("actmap").put("A_PROCESSDEFINITIONID", "BOC_gongdan") ;
		completeMap.get("actmap").put("A_TASKID", umap.get("a_id")) ;
		completeMap.get("actmap").put("A_FORMCLASS", "GongdanUpdate") ;
		completeMap.get("formmap").put("gongdanId", umap.get("gongdanId")) ;
		completeMap.get("formmap").put("gongdanId", umap.get("gongdanId")) ;
		completeMap.get("formmap").put("departmentId", "0101") ;
		completeMap.get("formmap").put("departmentName=1", "0101") ;
		completeMap.get("formmap").put("gongdanState", "完成") ;
		completeMap.get("formmap").put("companyId", "0101") ;
		
		try {
			long dateStr11= System.currentTimeMillis();
			ActivitiUtils.doCompletTask(completeMap.get("actmap").get("A_TASKID").toString(),map);		
			long dateStr22= System.currentTimeMillis();
			A_logger.debug("user:"+user+"-提交:"+"doCompletTask.action-ActivitiUtils.doCompletTask:" +(dateStr22-dateStr11)) ;
			//业务数据处理过程。
			FormHandle formHandle = new FormHandle() ;
//			Map rmap = (Map)formHandle.formdata(completeMap) ;
			long dateStr33= System.currentTimeMillis();
			A_logger.debug("user:"+user+"-提交:"+"doCompletTask.action-formClass:" +(dateStr33-dateStr22)) ;
		} catch (Exception e) {
			A_logger.error("doCompletTask.action 提交失败:" , e);
			e.printStackTrace();
			return ;
		}
	}
	
	@RequestMapping(value="ceshi2")
	public void ceshi2(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		ActivitiUtils ActivitiUtils = new ActivitiUtils() ;
		String user = new RandomGUID().toString() ;
		String A_PROCESSDEFINITIONID="BOC_gongdan";
		String processInstanceId = "" ;
		long dateStr1= System.currentTimeMillis();
		try{
			processInstanceId = ActivitiUtils.doStartProcess(A_PROCESSDEFINITIONID);
		}catch(Exception e){
			A_logger.error("doStartWorkOrderProcess.action-ActivitiUtils.doStartProcess",e) ;
			return ;
		}
		long dateStr2= System.currentTimeMillis();
		A_logger.debug("user:"+user+"-新建:"+"doStartWorkOrderProcess.action-ActivitiUtils.doStartProcess:" +(dateStr2-dateStr1)) ;
		try{
			String nextTaskId = ActivitiUtils.getTaskIdByProcessInstanceId(processInstanceId) ;
			ActivitiUtils.doClaim(nextTaskId, user) ;	
		}catch(Exception e){
			A_logger.error("doStartWorkOrderProcess.action-ActivitiUtils.doClaim",e) ;return ;
		}
		long dateStr3= System.currentTimeMillis();
		A_logger.debug("user:"+user+"-（签收）新建:"+"doStartWorkOrderProcess.action-ActivitiUtils.doClaim:" +(dateStr3-dateStr2)) ;
		try{
			ActivitiUtils.getWaitTaskForUserAndKey("BOC_gongdan",user) ;
		}catch(Exception e){
			A_logger.error("getUserTask.action-ActivitiUtils.getWaitTaskForUserAndKey",e) ;
			return ;
		}
		long dateStr4= System.currentTimeMillis();
		A_logger.debug("user:"+user+"-查自己的待办:"+"getUserTask.action-ActivitiUtils.getWaitTaskForUserAndKey:"+(dateStr4-dateStr3)) ;
		try{
			String nextTaskId = ActivitiUtils.getTaskIdByProcessInstanceId(processInstanceId) ;
			ActivitiUtils.doCompletTask(nextTaskId,map);		
		}catch(Exception e){
			A_logger.error("doCompletTask.action-ActivitiUtils.doCompletTask",e) ;
			return ;
		}
		long dateStr5= System.currentTimeMillis();
		A_logger.debug("user:"+user+"-提交:"+"doCompletTask.action-ActivitiUtils.doCompletTask:" +(dateStr5-dateStr4)) ;
		try{
			Map unMap =ActivitiUtils.getTaskUnassigned("fuHe", "BOC_gongdan");
		}catch(Exception e){
			A_logger.error("getTaskUnassigned.action-ActivitiUtils.getTaskUnassigned:",e);
		}
		long dateStr6= System.currentTimeMillis();
		A_logger.debug("user:"+user+"-提交:"+"getTaskUnassigned.action-ActivitiUtils.getTaskUnassigned:" +(dateStr6-dateStr5)) ;
		try{
			String nextTaskId = ActivitiUtils.getTaskIdByProcessInstanceId(processInstanceId) ;
			ActivitiUtils.doClaim(nextTaskId, user) ;	
		}catch(Exception e){
			A_logger.error("doClaim.action-ActivitiUtils.doClaim",e) ;return ;
		}
		long dateStr7= System.currentTimeMillis();
		A_logger.debug("user:"+user+"-签收:"+"doClaim.action-ActivitiUtils.doClaim:" +(dateStr7-dateStr6)) ;
		try{
			String nextTaskId = ActivitiUtils.getTaskIdByProcessInstanceId(processInstanceId) ;
			ActivitiUtils.doCompletTask(nextTaskId,map);		
		}catch(Exception e){
			A_logger.error("doCompletTask.action-ActivitiUtils.doCompletTask",e) ;
			return ;
		}
		long dateStr8= System.currentTimeMillis();
		A_logger.debug("user:"+user+"-提交(完成):"+"doCompletTask.action-ActivitiUtils.doCompletTask:" +(dateStr8-dateStr7)) ;
	}
}
