package com.zinglabs.apps.activitiCommon.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.zinglabs.apps.activitiCommon.service.ActivitiFormCurdService;
import com.zinglabs.apps.activitiManager.service.ActivitiManagerService;
import com.zinglabs.base.Interface.ActivitiFromInterface;
import com.zinglabs.base.core.utils.AppSpringUtils;
import com.zinglabs.util.DateUtil;

@SuppressWarnings("unchecked")
public class updateForm implements ActivitiFromInterface{

	@Override
	public Map execute(Map<String,Map> map) {
		// TODO Auto-generated method stub
		
		String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss") ;
		Map frommap =(Map)map.get("formmap") ;
        Map actmmap =(Map)map.get("actmap") ;
		Map activitiMap = ActivitiManagerService.activtiParamMap.get(String.valueOf(actmmap.get("A_PROCESSDEFINITIONID"))); ;
//		rmap.put("tableName", String.valueOf(activitiMap.get("tableName")));
//		getService().act_insert(rmap) ;
		
		return null;
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
