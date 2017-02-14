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
public class HQDBUpdate implements ActivitiFromInterface {
	public static Logger A_logger = LoggerFactory.getLogger("Activiti");

	@Override
	public Map execute(Map<String, Map> map) {
		// TODO Auto-generated method stub
		try {
			String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
			Map formmap = (Map) map.get("formmap");
			Map actmmap = (Map) map.get("actmap");
			Map rmap = new HashMap();
			Map activitiMap = ActivitiManagerService.activtiParamMap.get(String.valueOf(actmmap.get("A_PROCESSDEFINITIONID")));
			String doClaimIDS=String.valueOf(formmap.get("doClaimIDS"));
			String fields = (String) activitiMap.get("field");
			String strArray[] = fields.trim().split(",");
				// 获取需要存入本地数据表的map数据
				for (String str : strArray) {
					if (formmap.get(str) != null && formmap.get(str) != "") {
						rmap.put(str, String.valueOf(formmap.get(str)));
					}
				}
			//判断是否为获取待办认领进行循环修改数据
			if(!doClaimIDS.equals("")&&!doClaimIDS.equals("null")){
				//相关时间设置value
				rmap.put("disponseBTime",date);  //处理人变化时记录时间
				rmap.put("endDealTime",date);  //最后一次操作时间(包括提交和签收)
				String[] ids = doClaimIDS.toString().split(";");
				for(String id : ids){
					rmap.put("id",id);
					rmap.put("tableName",String.valueOf(activitiMap.get("tableName")));
					getService().act_update(rmap);
				}	
			}else{
				return null;
			}
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
