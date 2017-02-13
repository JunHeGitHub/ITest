/**
 * 
 * @param {} param{processDefinitionId:"流程定义标识",flowType:"流向"} 必须包含参数
 * @param {} fn 回调函数
 */
function doStartProcess(param,fn){
	var url = "/ZDesk/activitiManager/activityCommon/doStartWorkOrderProcess.action";
	putLoginNameToParam(param);
	ajaxFunction(url,param,false,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}
/**
 * 
 * @param {} param{taskId:"任务标识（必填参数）",toTaskId:"需要跳转的任务标识（此参数可不填）"}
 * @param {} fn
 */
function doCompletTask(param,fn){
	var url = "/ZDesk/activitiManager/activityCommon/doCompletTask.action";
	putLoginNameToParam(param);
	ajaxFunction(url,param,false,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}
/**
 * 
 * @param {} param{taskId:"任务标识",to_userId:"转办用户"} 必须包含参数
 * @param {} fn 回调函数
 */
function transferAssignee(param,fn){
	var url = "/ZDesk/activitiManager/activityCommon/transferAssignee.action";
	param = putLoginNameToParam(param);
	ajaxFunction(url,param,false,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}
/**
 * 
 * @param {} param{processInstanceId:"流程实例ID","processDefination":"(流程ID)"} 必须包含参数   param{startTimeAsc:"true"}  按创建时间升序
 * @param {} fn 回调函数
 */
function getHistoryTasks(param,fn){
	var url = "/ZDesk/activitiManager/activityCommon/getHiTasks.action";
	param = putLoginNameToParam(param);
	ajaxFunction(url,param,false,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}

/**
 * 流程实例更新
 * */
function doUpdateVariables(param,fn){
	var url = "/ZDesk/activitiManager/activityCommon/doUpdateVariables.action";
	param = putLoginNameToParam(param);
	ajaxFunction(url,param,false,function(data){
		if(fn!=undefined && fn!=null && fn!='' && typeof(fn)=='function'){
			fn(data);
		}
	});
}

function putLoginNameToParam(param){
	var loginName = getUserInfo().loginName;
	if(loginName!=null&&loginName!=''&&loginName!=undefined){
		param['zkm_userName'] = loginName;
		return param;
	}else{
		alert('获取用户登录帐号失败！');
		return false;
	}
}
