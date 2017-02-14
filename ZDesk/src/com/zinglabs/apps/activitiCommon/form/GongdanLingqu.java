package com.zinglabs.apps.activitiCommon.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.apache.commons.validator.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.zinglabs.apps.BOC_gongdan.IdGenerator;
import com.zinglabs.apps.activitiCommon.service.ActivitiFormCurdService;
import com.zinglabs.apps.activitiManager.service.ActivitiManagerService;
import com.zinglabs.base.Interface.ActivitiFromInterface;
import com.zinglabs.base.core.activitiSupport.ActivitiUtils;
import com.zinglabs.base.core.utils.AppSpringUtils;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.RandomGUID;

@SuppressWarnings("unchecked")
public class GongdanLingqu implements ActivitiFromInterface {

	public static Logger A_logger = LoggerFactory.getLogger("Activiti");

	@Override
	public Map<String, Map> execute(Map<String, Map> map) {
		// TODO Auto-generated method stub
		Map workOrdermap = null;
		ActivitiUtils acticitiutil = new ActivitiUtils() ;
		String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
		Map frommap = (Map) map.get("formmap");
		Map actmap = (Map) map.get("actmap");
		List<Map> clist = (List) map.get("claimlist").get("claimlist") ;
		try {
			/**
			 * 领取操作,改处理人,处理时间
			 */
			Map<String, String> insertmap;
			for(int i=0 ;i<clist.size();i++){
				insertmap = new HashMap();
				if(clist.get(i).get("taskName")!=null&&"fuHe".equals(clist.get(i).get("taskName").toString())){
					insertmap.put("gongdanState", "复核中") ;
					insertmap.put("fuherenId", actmap.get("user").toString());
					insertmap.put("fuheTime", date) ;
				}
				if(clist.get(i).get("taskName")!=null&&"huiFuZuoXi".equals(clist.get(i).get("taskName").toString())){
					insertmap.put("gongdanState", "联系中") ;
				}
				insertmap.putAll(frommap);
				insertmap.put("disponseUser", actmap.get("user").toString());
				insertmap.put("disponseBTime", date);
				insertmap.put("disponseETime", "0000-00-00 00:00:00");
				insertmap.put("endDealTime", date);
				insertmap.put("flowId", clist.get(i).get("processinstanceId").toString());
				getService().qs_updategongdanByflowId(insertmap) ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			A_logger.error("insertForm eror :", e);
			return workOrdermap;
		}
		return workOrdermap;
	}

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
