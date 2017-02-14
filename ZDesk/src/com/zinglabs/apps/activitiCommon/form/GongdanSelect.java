package com.zinglabs.apps.activitiCommon.form;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.zinglabs.apps.activitiCommon.service.ActivitiFormCurdService;
import com.zinglabs.apps.activitiManager.service.ActivitiManagerService;
import com.zinglabs.base.Interface.ActivitiFromInterface;
import com.zinglabs.base.core.utils.AppSpringUtils;

public class GongdanSelect implements ActivitiFromInterface{

	@Override
	public Map<String, Map> execute(Map<String, Map> map) {
		// TODO Auto-generated method stub
		try{
			//查询主键:flowId
			Map parammap = ActivitiManagerService.activtiParamMap.get(String.valueOf(((Map)map.get("actmap")).get("A_PROCESSDEFINITIONID"))) ;
			map.putAll(parammap) ;
			Map formmap =  getService().act_select(String.valueOf(parammap.get("tableName")),"flowId",(Map)map.get("activitiSelectMap")) ;
			map.putAll(formmap) ;
			return map ;
		}catch(Exception e){
			e.printStackTrace() ;
			return null;
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
