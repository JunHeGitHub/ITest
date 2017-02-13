package com.zinglabs.apps.activitiCommon.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.zinglabs.apps.activitiCommon.service.ActivitiFormCurdService;
import com.zinglabs.apps.activitiManager.service.ActivitiManagerService;
import com.zinglabs.base.Interface.ActivitiFromInterface;
import com.zinglabs.base.core.utils.AppSpringUtils;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.RandomGUID;

@SuppressWarnings("unchecked")
public class WorksBaseUpdate implements ActivitiFromInterface {
	public static Logger A_logger = LoggerFactory.getLogger("Activiti");

	@Override
	public Map execute(Map<String, Map> map) {
		// TODO Auto-generated method stub
		try {
			String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
			Map formmap = (Map) map.get("formmap");
			Map actmmap = (Map) map.get("actmap");
			Map rmap = new HashMap();
			Map rmapHis = new HashMap();
			Map activitiMap = ActivitiManagerService.activtiParamMap.get(String.valueOf(actmmap.get("A_PROCESSDEFINITIONID")));
			rmap.put("tableName", String.valueOf(activitiMap.get("tableName")));
			String fields = (String) activitiMap.get("field");
			String strArray[] = fields.trim().split(",");
				// 获取需要存入本地数据表的map数据
				for (String str : strArray) {
					if (formmap.get(str) != null && formmap.get(str) != "") {
						rmap.put(str, String.valueOf(formmap.get(str)));
					}
				}
				rmap.put("disponseUser",String.valueOf(formmap.get("disponseUser")));  //处理人(为空情况下)
				//复核人(为空情况下)
				rmap.put("appointedCheckUser",String.valueOf(formmap.get("appointedCheckUser"))); 
				rmap.put("appointedCheckUserID",String.valueOf(formmap.get("appointedCheckUserID")));  
				//相关时间设置value
				rmap.put("endDealTime", date);   //最后操作时间
				rmap.put("disponseETime",date);  //最后提交时间
				//判断是否为结束流程,发布时间赋值
				if (formmap.get("flowNode").equals("Over")) {
						rmap.put("published", date);
					}
				//复核所在节点发生变化时,认为复核处理结束
				if (formmap.get("flow_type").equals("1")||formmap.get("flow_type").equals("-2")) {
					rmap.put("appointedCheckTime", date);  //复核处理结束时间
				}
				//当前处理人发生变化的时候进行时间改变
				if(formmap.get("disponseUser").equals("")){
					rmap.put("disponseBTime","0001-01-01 00:00:00");
					}else{
					rmap.put("disponseBTime",date);  
					}
				rmapHis.putAll(rmap);
				rmapHis.put("tableName", String.valueOf(activitiMap.get("tableName"))+"His");
				rmapHis.put("id", new RandomGUID().toString());  
				getService().act_update(rmap);  
				getService().act_insert(rmapHis);
				 
		} catch (Exception e) {
			e.printStackTrace();
			A_logger.error("insertForm eror :" + e.getMessage());
			return null;
		}
		return map;
	}

	/**
	 * 直接返回true 返回值默认为false,记得要改成true
	 */
	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

	public ActivitiFormCurdService getService() {
		return (ActivitiFormCurdService) getBean("ActivitiFormCurdService");
	}

	public Object getBean(String id) {
		if (id != null && id.length() > 0) {
			ApplicationContext context = AppSpringUtils.getApplicationContext();
			if (context != null) {
				return context.getBean(id);
			} else {
				// LogUtil.error("BaseAction-getBean Spring ApplicationContext
				// is null ", SKIP_Logger);
			}
		}
		return null;
	}
}
