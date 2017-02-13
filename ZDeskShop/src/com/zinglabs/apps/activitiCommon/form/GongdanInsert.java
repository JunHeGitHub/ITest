package com.zinglabs.apps.activitiCommon.form;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.zinglabs.apps.activitiCommon.service.ActivitiFormCurdService;
import com.zinglabs.apps.activitiManager.service.ActivitiManagerService;
import com.zinglabs.apps.BOC_gongdan.IdGenerator;
import com.zinglabs.base.Interface.ActivitiFromInterface;
import com.zinglabs.base.core.utils.AppSpringUtils;
import com.zinglabs.util.DateUtil;
@SuppressWarnings("unchecked")
public class GongdanInsert implements ActivitiFromInterface{
	
	public static Logger A_logger = LoggerFactory.getLogger("Activiti");
	
	@Override
	public Map<String, Map> execute(Map<String, Map> map) {
		// TODO Auto-generated method stub
		Map workOrdermap = null ;
		String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss") ;
        Map frommap =(Map)map.get("formmap") ;
        Map actmap =(Map)map.get("actmap") ;
		try{
			/**
			 * 新建工单
			 * 当前处理人,处理开始时间,最后处理时间,流程标识,创建时间,创建人id
			 * */
			Map<String,String> insertmap = new HashMap() ;
			insertmap.putAll(frommap) ;
			insertmap.put("disponseUser", actmap.get("user").toString()) ;
			insertmap.put("disponseBTime", date) ;
			insertmap.put("endDealTime", date) ;
			insertmap.put("flowId",actmap.get("A_PROCESSINSTANCEID").toString() );
			insertmap.put("chuangjianTime",date) ;
			insertmap.put("chuangjianrenId", actmap.get("user").toString()) ;
			workOrdermap = IdGenerator.getInstance().makeWorkOrder(insertmap) ;
			
		}catch(Exception e){
			e.printStackTrace() ;
			A_logger.error("insertForm eror :",e) ;
			return null ;
		}
		return workOrdermap ;
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
