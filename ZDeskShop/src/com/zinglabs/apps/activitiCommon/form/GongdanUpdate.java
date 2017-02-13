package com.zinglabs.apps.activitiCommon.form;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.zinglabs.apps.activitiCommon.service.ActivitiFormCurdService;
import com.zinglabs.apps.activitiManager.service.ActivitiManagerService;
import com.zinglabs.apps.mq.service.MQService;
import com.zinglabs.apps.BOC_gongdan.IdGenerator;
import com.zinglabs.base.Interface.ActivitiFromInterface;
import com.zinglabs.base.core.activitiSupport.ActivitiUtils;
import com.zinglabs.base.core.utils.AppSpringUtils;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.RandomGUID;

public class GongdanUpdate implements ActivitiFromInterface{
	
	/**
	 * 主键 id 
	 * gongdanid 作为业务数据
	 * */
	@Override
	public Map<String, Map> execute(Map<String, Map> map) {
		// TODO Auto-generated method stub
		Logger A_logger = LoggerFactory.getLogger("Activiti");
		ActivitiUtils acticitiutil= new ActivitiUtils() ;
		Map workOrdermap = null ;
		String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss") ;
        Map formmap =(Map)map.get("formmap") ;
        Map actmmap =(Map)map.get("actmap") ;
        Map activitiMap = ActivitiManagerService.activtiParamMap.get(String.valueOf(actmmap.get("A_PROCESSDEFINITIONID")));
		try{
			Task task = acticitiutil.getTaskByProcessInstanceId(actmmap.get("A_PROCESSINSTANCEID").toString()) ;
			/**
			 * 工单提交
			 * disponseETime 更新
			 * endDealTime 更新
			 * disponseUser 
			 * */
			Map<String,String> insertmap = new HashMap() ;
			insertmap.put("flowId",String.valueOf(actmmap.get("A_PROCESSINSTANCEID"))) ;
			insertmap.putAll(formmap) ;
			insertmap.put("disponseETime", date) ;
			String name = "" ;
			if(task==null||"".equals(task.getName())){
				//task=null　流程结束。
				insertmap.put("flowNode", "") ;
				insertmap.put("flowState", "1") ;
				insertmap.put("wanchengrenId", String.valueOf(actmmap.get("user"))) ;
				insertmap.put("wanchengTime", date) ;
			}else{
				name = task.getName().toString();
				insertmap.put("flowNode",name ) ;
			}
			if(formmap.get("fuherenId")!=null&&!"".equals(formmap.get("fuherenId").toString())){
				insertmap.put("fuherenId", formmap.get("fuherenId").toString()) ;
				insertmap.put("fuheTime", date) ;
			}
			insertmap.put("disponseUser", actmmap.get("A_ASSIGNEE").toString()!=null&&actmmap.get("A_ASSIGNEE").toString()!=""?actmmap.get("A_ASSIGNEE").toString():"") ;
			if(actmmap.get("A_ASSIGNEE").toString().length()>0){
				insertmap.put("disponseBTime", date) ;
				insertmap.put("disponseETime", "0000-00-00 00:00:00") ;
			}
			insertmap.put("endDealTime", date) ;
			A_logger.debug("insertmap :"+insertmap.toString()) ;
			int i = getService().gongdanupdate(insertmap);
			Map rmapHis =new HashMap() ;
			rmapHis.putAll(formmap) ;
			rmapHis.put("endDealTime", date) ;
			rmapHis.put("disponseUser", actmmap.get("user")) ;
			rmapHis.put("id", new RandomGUID().toString());
			getService().insertgongdanHis(rmapHis);
			if(task!=null&&"gongDanXiTong".equals(task.getName())){
				if(formmap.get("gongdanType")!=null&&formmap.get("gongdanType").toString().length()>1){
					MQService mq = new MQService() ;
					insertmap.put("BranchType", "1") ;
					mq.sendMsg(formmap.get("gongdanType").toString(), formmap.get("gongdanId").toString(), "", insertmap) ;
				}
			}
			return map ;
		}catch(Exception e){
			e.printStackTrace() ;
			A_logger.error("updategongdan error :"+map.toString(),e) ;
			return null ;
		}
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public ActivitiFormCurdService getService(){
		return (ActivitiFormCurdService) getBean("ActivitiFormCurdService");
	}
	public  Object getBean(String id){
		if(id!=null && id.length()>0){
			ApplicationContext context= AppSpringUtils.getApplicationContext();
			if(context!=null){
				return context.getBean(id);
			}else{
//				LogUtil.error("BaseAction-getBean  Spring  ApplicationContext is null ", SKIP_Logger);
			}
		}
	return null;
	}

}
