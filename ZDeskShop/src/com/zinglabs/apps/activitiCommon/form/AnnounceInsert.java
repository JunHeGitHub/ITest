package com.zinglabs.apps.activitiCommon.form;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.zinglabs.apps.activitiCommon.service.ActivitiFormCurdService;
import com.zinglabs.apps.activitiManager.service.ActivitiManagerService;
import com.zinglabs.base.Interface.ActivitiFromInterface;
import com.zinglabs.base.core.utils.AppSpringUtils;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.RandomGUID;

public class AnnounceInsert implements ActivitiFromInterface {
	public static Logger A_logger = LoggerFactory.getLogger("Activiti");
	@Override
	public Map<String, Map> execute(Map<String, Map> map) {
		// TODO Auto-generated method stub
		try{
	        String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss") ;
	        Map frommap =(Map)map.get("formmap") ;  //业务数据
	        Map actmap =(Map)map.get("actmap") ;  //流程数据
			Map rmap = new HashMap() ;
			rmap.put("flowId", String.valueOf(actmap.get("A_PROCESSINSTANCEID"))) ;
			Map activitiMap = ActivitiManagerService.activtiParamMap.get(String.valueOf(actmap.get("A_PROCESSDEFINITIONID"))); ;
			rmap.put("tableName", String.valueOf(activitiMap.get("tableName")));
			rmap.put("createTime",date);//创建时间
			rmap.put("disponseBTime",date);//处理开始时间
			rmap.put("applyTime",date);//申请时间
			String fields = (String) activitiMap.get("field");
			String strArray[] = fields.trim().split(",");
			// 获取需要存入本地数据表的map数据
			for (String str : strArray) {
				if (frommap.get(str) != null && frommap.get(str) != "") {
					rmap.put(str, String.valueOf(frommap.get(str)));
				}
			}
			rmap.put("id", new RandomGUID().toString());
			rmap.put("publishArea", frommap.get("publishArea"));
			getService().act_insert(rmap) ;
			//需求变更：先不需要修订流程形式功能,用编辑替换
			/*if(frommap.get("isUpdate").equals("已修订")){
				Map publishMap= new HashMap();	
				publishMap.putAll(frommap);
				publishMap.put("tableName", "zkmAnnouncementPublished");
				publishMap.remove("pageSize");
				publishMap.remove("A_ASSIGNEE");
				publishMap.remove("A_PROCESSINSTANCEID");
				publishMap.remove("A_UUID");
				publishMap.remove("A_TASKID");
				publishMap.put("flowId",String.valueOf(rmap.get("flowId")));
				getService().act_update(publishMap);
				}*/
			}catch(Exception e){
				e.printStackTrace() ;
				A_logger.error("insertForm eror :"+e.getMessage()) ;
				return null ;
			}
			Map m = new HashMap() ;
			return map ;
	}
	/**
	 * 直接返回true
	 * 返回值默认为false,记得要改成true
	 * */
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
