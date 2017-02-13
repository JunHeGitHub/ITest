/**
 * 发起流程
 * @param {} param{A_PROCESSDEFINITIONID  :"流程定义标识",flowType:"流向"} 必须包含参数
 * @param {} fn 回调函数
 */
function doStartProcess(param,fn){
	var url = "/ZDesk/*/activityCommon/doStartByProcessDefinitionId.action";
	putLoginNameToParam(param);
	ajaxFunction(url,param,false,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}

function doStartAlsoSubmitByProcessDefinitionId(param,fn){
	var url = "/ZDesk/*/activityCommon/doStartAlsoSubmitByProcessDefinitionId.action";
	putLoginNameToParam(param);
	ajaxFunction(url,param,false,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}

/**
 * 提交
 * 参数
 * param{A_TASKID:"任务标识（必填参数）"，A_PROCESSINSTANCEID：“”,A_TOTASKID:"需要跳转的任务标识（此参数可不填）"}
 * @param {} fn
 * 
 */
function doCompletTask(param,fn){
	var url = "/ZDesk/*/activityCommon/doCompletTask.action";
	putLoginNameToParam(param);
	ajaxFunction(url,param,false,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}
/**
 * 转办
 * @param {} param{A_TASKID:"任务标识",A_TOUSERID:"转办用户"} 必须包含参数
 * @param {} fn 回调函数
 */
function transferAssignee(param,fn){
	var url = "/ZDesk/activitiManager/activityCommon/transferAssignee.action";
	param = putLoginNameToParam(param);
	ajaxFunction(url,param,true,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}
/**
 * 转办
 * @param {} param{A_PROCESSINSTANCEID:"任务标识",A_TOUDERID:"转办用户"} 必须包含参数
 * @param {} fn 回调函数
 */
function transferAssigneeByProcessInstanceId(param,fn){
	var url = "/ZDesk/activitiManager/activityCommon/transferAssigneeByProcessInstanceId.action";
	param = putLoginNameToParam(param);
	ajaxFunction(url,param,true,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}
/**
 * 获取历史
 * @param {} param{A_PROCESSINSTANCEID:"流程实例ID","A_PROCESSDEFINITIONID":"(流程ID)"} 必须包含参数   param{startTimeAsc:"true"}  按创建时间升序
 * @param {} fn 回调函数
 */
function getHistoryTasks(param,fn){
	var url = "/ZDesk/activitiManager/activityCommon/getHiTasks.action";
	param = putLoginNameToParam(param);
	ajaxFunction(url,param,true,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}

/**
 * 流程实例更新/保存
 *参数: A_TASKID 任务标识
 * */
function doUpdateVariables(param,fn){
	var url = "/ZDesk/activitiManager/activityCommon/doUpdateVariables.action";
	param = putLoginNameToParam(param);
	ajaxFunction(url,param,true,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}

/**
 * 签收/领取  
 * 参数(多条) A_TASKIDS：   taskIds1，taskIds2，taskIds3，taskIds4
 * user默认取当前人，不用传参数了。
 * */
function doClaimByTaskId(param,fn){
	var url = "/ZDesk/*/activityCommon/doClaimByTaskId.action";
	param = putLoginNameToParam(param);
	ajaxFunction(url,param,true,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}

/**
 * 获取未签收任务
 *  注意:当查询不到a_id等流程数据时,请将业务表中 A_PROCESSINSTANCEID  存储字段定义为 flowId
 * 参数：A_PROCESSDEFINITIONID  流程标识
 *            A_TASKNAME  节点名称
 * */
function getTaskUnassigned(param,fn){
	var url = "/ZDesk/*/activityCommon/getTaskUnassigned.action";
	param = putLoginNameToParam(param);
	ajaxFunction(url,param,false,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}
/**
 * 删除流程
 * 必填参数
 * A_FORMCLASS
 * A_PROCESSINSTANCEID
 * */
function doClearTask(param,fn){
	var url = "/ZDesk/*/activityCommon/clearTask.action";
	putLoginNameToParam(param);
	ajaxFunction(url,param,true,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}
function putLoginNameToParam(param){
	var loginName = window.top.getUserInfo().loginName;
	if(loginName!=null&&loginName!=''&&loginName!=undefined){
		param['zkm_userName'] = loginName;
//		param['zkm_Name'] = window.top.getUserInfo().name;
		return param;
	}else{
		alert('获取用户登录帐号失败！');
		return false;
	}
}
