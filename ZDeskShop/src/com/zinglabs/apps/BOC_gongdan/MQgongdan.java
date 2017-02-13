package com.zinglabs.apps.BOC_gongdan;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.zinglabs.apps.activitiManager.service.ActivitiManagerService;
import com.zinglabs.base.core.activitiSupport.ActivitiUtils;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.base.core.utils.AppSpringUtils;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.RandomGUID;

public class MQgongdan extends BaseService{
	public static Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk");
	/**
	 * 
	 * @return    返回map 
	 * 
	 *    map.state =0 成功    =1 失败
     *    map.mgs : 提交结果。
	 * 
	 * */
	public Map docomplete(Map MQmap){
		ActivitiUtils activi = new ActivitiUtils() ;
		String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss") ;
		Map map = new HashMap() ;
		map.put("state", "0") ;
		map.put("mgs", "提交成功") ;
		SKIP_Logger.debug("id:",MQmap.get("id")) ;
		SKIP_Logger.debug("MQmap",MQmap) ;
		if(MQmap.get("idcode")==null||!(MQmap.get("idcode").toString().length()>0)){
			map.put("state", "1") ;
			map.put("mgs", "工单id 未获取到.") ;
			return map;
		}
		Map gongdanmap ;
		BOCgongdanService service = (BOCgongdanService)getBean("BOCgongdanService") ;
		try{
		gongdanmap = service.getflowid(MQmap) ;
		if(gongdanmap.get("flowId")==null||!(gongdanmap.get("flowId").toString().length()>0)){
			map.put("state", "1") ;
			map.put("mgs", "工单id:"+MQmap.get("idcode").toString()+"。根据`工单id`查询`流程id`失败") ;
			return map;
		}
		}catch(Exception e){
			String ess= e.getMessage();
			map.put("state", "1") ;
			map.put("mgs", "查询工单失败"+ess) ;
			return map;
			}
		Task t;
		try{
			t = activi.getTaskByProcessInstanceId(gongdanmap.get("flowId").toString()) ;
			gongdanmap.put("flowNode", t.getName()) ;
		}catch(Exception e){
			e.printStackTrace() ;
			map.put("state", "1") ;
			map.put("mgs", "提交失败,工单id:"+MQmap.get("id").toString()+"。工单已办理或流程中无此数据。") ;
			return map;
		}
		if(t.getName()==null&&t.getName().toString()!="gongDanXiTong"){
			map.put("state", "1") ;
			map.put("mgs", "提交失败,流程节点错误。工单id:"+MQmap.get("id").toString()+"。当前数据在流程节点:"+t.getName().toString()) ;
			return map;
		}
		Map update = new HashMap() ;
		if("gongDanXiTong".equals(t.getName().toString())){
//			int i = new Random().nextInt(4) ;
//			MQmap.put("Status", i);
//			SKIP_Logger.debug("随机生成流向flow_type："+i+".") ;
			try{
				if(MQmap.get("Status")!=null&&"1".equals(MQmap.get("Status").toString())){
					//1,工单退回到创建人待处理工单中
					map.put("flow_type", "3") ;
					activi.doCompletTask(t.getId(), map) ;
					String taskid = activi.getTaskIdByProcessInstanceId(gongdanmap.get("flowId").toString()) ;
					activi.doClaim(taskid, gongdanmap.get("chuangjianrenId").toString()) ;
					update.put("gongdanState", "退回") ;
					update.put("disponseUser", gongdanmap.get("chuangjianrenId").toString()) ;
					update.put("wancehngrenId", (MQmap.get("Handler")!=null)?MQmap.get("Handler").toString():"") ;
					update.put("endDealTime", date) ;
					update.put("disponseBTime", date) ;
					update.put("disponseETime", "0000-00-00 00:00:00") ;
				}else if(MQmap.get("Status")!=null&&"0".equals(MQmap.get("Status").toString())){
					//0 工单处理完成。
					map.put("flow_type", "2") ;
					update.put("gongdanState", "已完成") ;//flowNode endDealTime
					update.put("endDealTime", date) ;
					update.put("disponseBTime", "0000-00-00 00:00:00") ;
					update.put("disponseETime", "0000-00-00 00:00:00") ;
					update.put("flowState", "1") ;
					update.put("wancehngrenId", (MQmap.get("Handler")!=null)?MQmap.get("Handler").toString():"") ;
					update.put("wanchengTime", date) ;
					activi.doCompletTask(t.getId(), map) ;
				}else if(MQmap.get("Status")!=null&&"2".equals(MQmap.get("Status").toString())){
					//2,提交至回复坐席
					map.put("flow_type", "1") ;
					update.put("gongdanState", "待回复") ;
					update.put("endDealTime", date) ;
					update.put("disponseBTime", "0000-00-00 00:00:00") ;
					update.put("disponseETime", "0000-00-00 00:00:00") ;
					activi.doCompletTask(t.getId(), map) ;
				}else if(MQmap.get("Status")!=null&&"3".equals(MQmap.get("Status").toString())){
					//阶段回复，保存操作
					update.put("gongdanId", (MQmap.get("idcode")!=null)?MQmap.get("idcode").toString():"") ;
					update.put("chuliMgs", (MQmap.get("manageResult")!=null)?MQmap.get("manageResult").toString():"") ;
					update.put("flowNode", t.getName().toString()) ;
					service.updateForMQ(update) ;
					map.put("state", "0") ;
					map.put("mgs", "阶段回复，保存数据成功。") ;
					return map;
				}else{
					//报文中状态不为1,则提交至回复坐席  测试使用
					
//					SKIP_Logger.debug("随机生成流向flow_type"+i) ;
					map.put("flow_type","1") ;
					update.put("gongdanState", "待回复") ;
					update.put("endDealTime", date) ;
					update.put("disponseBTime", "0000-00-00 00:00:00") ;
					update.put("disponseETime", "0000-00-00 00:00:00") ;
					activi.doCompletTask(t.getId(), map) ;
				}
				t = activi.getTaskByProcessInstanceId(gongdanmap.get("flowId").toString()) ;
				if(t==null){
					update.put("flowNode", "") ;
				}else{
					update.put("flowNode", t.getName().toString()) ;
				}
			}catch(Exception e){
				e.printStackTrace() ;
				SKIP_Logger.error("提交失败,工单id:"+MQmap.get("idcode").toString()+"。流程提交失败。",e) ;
				map.put("state", "1") ;
				map.put("mgs", "提交失败,工单id:"+MQmap.get("idcode").toString()+"。"+MQmap) ;
				return map;
			}
		}else{
			if(MQmap.get("Status")!=null&&"3".equals(MQmap.get("Status").toString())){
				//阶段回复，保存操作
				update.put("gongdanId", MQmap.get("idcode").toString()) ;
				update.put("chuliMgs", (MQmap.get("manageResult")!=null)?MQmap.get("manageResult").toString():"") ;
				service.updateForMQ(update) ;
				map.put("state", "0") ;
				map.put("mgs", "阶段回复，保存数据成功。") ;
				return map;
			}else{
				map.put("state", "1") ;
				map.put("mgs", "提交失败,工单流程节点不再工单系统:"+MQmap.get("idcode").toString()+"。"+MQmap) ;
				return map ;
			}
		}
		try{
			update.put("gongdanId", MQmap.get("idcode").toString()) ;
		//更新工单
			if(MQmap.get("manageResult")!=null&&!(MQmap.get("manageResult").toString().length()>0)){
				update.put("chuliMgs",MQmap.get("manageResult").toString()) ;
			}else{
				update.put("chuliMgs","") ;
			}
			SKIP_Logger.debug("提交参数"+update+"。流程提交失败。") ;
			service.updateForMQ(update) ;
			gongdanmap.putAll(update) ;
			gongdanmap.put("id", new RandomGUID().toString()) ;
			if(map.get("Handler")!=null&&map.get("Handler").toString().length()>0){
				gongdanmap.put("disponseUser",map.get("Handler").toString()) ;
			}
		//保存历史
			service.insertHis(gongdanmap) ;
		}catch(Exception e){
			String es = e.getMessage() ;
			SKIP_Logger.error("表单存储异常",e) ;
			map.put("state", "1") ;
			map.put("mgs",es) ;
		}
		return map;
	}

	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		// TODO Auto-generated method stub
		return null;
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
	public static void main(String args[]){
//从数据库中找到一条工单系统节点的task任务，然后模拟工单系统返回数据
		MQgongdan mq = new MQgongdan() ;
		ActivitiManagerService se = new ActivitiManagerService() ;
		se.initActivitiParam() ;
		
		Map MQmap = new HashMap() ;
		MQmap.put("id", "201505060004") ;
		//需要一条在gongdanxitong节点的流程数据
		mq.docomplete(MQmap) ;
		
	}
}