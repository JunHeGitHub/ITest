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

public class AnnounceUpdate implements ActivitiFromInterface {
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
			formmap.put("tableName", String.valueOf(activitiMap.get("tableName")));
			String fields = (String) activitiMap.get("field");
			String strArray[] = fields.trim().split(",");			
			formmap.put("appointedCheckTime", date);//复核最后处理时间
			formmap.put("endDealTime", date);//最后处理时间
			formmap.put("disponseETime", date);//处理结束时间			
			String loginName=formmap.get("loginName").toString();			
			formmap.remove("loginName") ;
			
			if(formmap.get("approveState").equals("待审批")){	
				//formmap.put("flowNode", "fuhe");
				formmap.remove("ids") ;
				formmap.remove("A_ASSIGNEE") ;
				getService().act_update(formmap);//更新流程主表的数据
			}
			if(formmap.get("approveState").equals("通过")){
				//2015-6-10 公告接收者调用接口,不需存入自定义接受者表 WDT修改
				String ids=formmap.get("ids").toString();		
				String aid=formmap.get("aid").toString();
				formmap.remove("aid") ;
				formmap.put("messageState","发布");
				formmap.put("approveState","通过");
				formmap.put("flowNode","end");
				formmap.put("published",date);
				actmmap.put("flow_type","1");
				formmap.remove("ids") ;
				formmap.remove("isDisable") ;
				formmap.remove("flow_type") ;	
//				formmap.remove("zkm_Name") ;
				getService().act_update(formmap);//更新流程主表的数据
				//将数据插入到接收者表中--调整 WDT修改
				/*Map userMap= new HashMap();					
				String []array=ids.split(",");
				for(int i=0;i<array.length;i++){	
					userMap.put("tableName", "zkmAnnouncementSecurityUser");
					userMap.put("id",new RandomGUID().toString()) ;
					userMap.put("loginName",array[i]);
					userMap.put("aid",aid);
					getService().act_insert(userMap);	
				}*/
				//将数据插入发布表中
				formmap.put("tableName", "zkmAnnouncementPublished");	
				formmap.put("isDisable","否");
				getService().act_insert(formmap);
				}
				//需求变更：先不需要修订流程形式功能,用编辑替换
					//判断是否已修订,判别修改或新增发布信息
				/*
				try {
					if(formmap.get("isUpdate").equals("已修订")){
						getService().act_updateByGongGao(map);
					}else{
					getService().act_insert(formmap);
					}
				} catch (Exception e) {
					getService().act_insert(formmap);
					}
				}*/
			if(formmap.get("approveState").equals("拒绝")){
				//String aid=formmap.get("aid").toString();
				formmap.remove("aid") ;
				formmap.remove("ids") ;
				actmmap.put("flow_type","2");
//				formmap.put("approveState","待审批");
				formmap.put("flowNode","jingban");				
				formmap.remove("flow_type") ;
				formmap.remove("isDisable") ;
				formmap.put("disponseUser",formmap.get("createUser"));
				//formmap.put("A_ASSIGNEE",formmap.get("createUser"));
				getService().act_update(formmap);
			}
			if(formmap.get("approveState").equals("删除")){	
				formmap.put("flowNode", "end");
				formmap.remove("ids") ;
				actmmap.put("flow_type","1");
				getService().act_update(formmap);//更新流程主表的数据
			}
			rmap.putAll(formmap);
			rmapHis.putAll(rmap);
			rmapHis.put("publishArea", formmap.get("publishArea"));
			rmapHis.put("tableName", String.valueOf(activitiMap.get("tableName"))+"His");
			rmapHis.put("id", new RandomGUID().toString());
			rmapHis.remove("isDisable");
			rmap.remove("isDisable");
			//getService().act_update(rmap);
			rmapHis.remove("A_ASSIGNEE") ;
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
