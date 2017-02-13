package com.zinglabs.apps.activitiCommon.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.apps.activitiCommon.util.FieldUtil;
import com.zinglabs.base.core.activitiSupport.ActivitiUtils;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;

/**
 * 简易通用增改删
 * 仅用于流程外接表
 * 
 * */
@SuppressWarnings("unchecked")
public class ActivitiFormCurdService extends BaseService {

	public static Logger logger = LoggerFactory.getLogger("Activiti");
	
	public void act_insert(Map map){
	    Map confMap = FieldUtil.activitiMoveCommonInfo(map) ;
		map = FieldUtil.spellMap(map) ;
		map.putAll(confMap) ;
		try{
			getServiceSqlSession().db_insert("insert" , map) ;
		}catch(Exception e){
			e.printStackTrace() ;
			logger.error("ActivitiCurdService__act_insert:"+e.getMessage()) ;
		}
	}
	
	public int act_update(Map map){
		Map confMap = FieldUtil.activitiMoveCommonInfo(map) ;
		map = FieldUtil.spellUpdateMap(map) ;
		map.putAll(confMap);
		if(map.get("primaryKey")==null||String.valueOf(map.get("primaryKey"))==""){
			map.put("primaryKey", "id");
		}
		if(map.get("primaryKeyValue")==null||String.valueOf(map.get("primaryKeyValue"))==""){
			map.put("primaryKeyValue",String.valueOf(map.get("id")));
		}
		try{
			getServiceSqlSession().db_update("update" , map) ;
			return 1 ;
		}catch(Exception e){
			e.printStackTrace() ;
			Map m = new HashMap() ;
			m.putAll(map) ;
			logger.error("ActivitiCurdService__act_update:"+m.toString(),e) ;
			return 0 ;
		}
	}
	public int insertgongdanHis(Map map){
		try{
			getServiceSqlSession().db_insert("insertgongdanHis" , map) ;
			return 1 ;
		}catch(Exception e){
			return 0 ;
		}
	}
	public int gongdanupdate(Map map){
		try{
			getServiceSqlSession().db_update("updategongdan" , map) ;
			return 1 ;
		}catch(Exception e){
			e.printStackTrace() ;
			Map m = new HashMap() ;
			m.putAll(map) ;
			logger.error("ActivitiCurdService__act_update:"+m.toString(),e) ;
			return 0 ;
		}
	}
	public int gongdanupdateByProcessinstanceId(Map map){
		try{
			getServiceSqlSession().db_update("updategongdanByProcessinstanceId" , map) ;
			return 1 ;
		}catch(Exception e){
			e.printStackTrace() ;
			Map m = new HashMap() ;
			m.putAll(map) ;
			logger.error("ActivitiCurdService__act_update:"+m.toString(),e) ;
			return 0 ;
		}
	}
	public int qs_updategongdanByflowId(Map map){
		try{
			getServiceSqlSession().db_update("qs_updategongdanByflowId" , map) ;
			return 1 ;
		}catch(Exception e){
			e.printStackTrace() ;
			Map m = new HashMap() ;
			m.putAll(map) ;
			logger.error("ActivitiCurdService__act_update:"+m.toString(),e) ;
			return 0 ;
		}
	}
	public void GL_updategongdanByflowId_ZhuanBan(Map map){
		try{
			getServiceSqlSession().db_update("GL_updategongdanByflowId_ZhuanBan" , map) ;
		}catch(Exception e){
			e.printStackTrace() ;
			Map m = new HashMap() ;
			m.putAll(map) ;
			logger.error("ActivitiCurdService__act_update:"+m.toString(),e) ;
		}
	}
	public Map selectGongdanById(String gongdanId){
		Map rmap = new HashMap() ;
		Map gmap = new HashMap() ;
		gmap.put("gongdanId", gongdanId) ;
		List rlist = getServiceSqlSession().db_selectList("selectGongdan",gmap);
		if(rlist.size()>0){
			rmap = (Map)rlist.get(0) ;
		}
		return rmap ;
	}
	public void act_delete(){
		
		
		
	}
	
	//重置功能:查询留言中的流程id
	public List selectMessageFlowId(Map map){
		List rlist = getServiceSqlSession().db_selectList("selectMessageFlowID",map);
		return rlist ;
	}
	//重置功能:重置后，插入一条历史
	public void insertMessagePorcessHis(Map map){
		getServiceSqlSession().db_insert("insertMessagePorcessHis",map);
	}
	//重置功能:重置后，更改流程表processinstanceId
	public void updateMessagePorcessProcessinstanceId(Map map){
		getServiceSqlSession().db_insert("updateMessagePorcessProcessinstanceId",map);
	}
	
	/**
	 * whereField:flowId  (根据流程id到业务表中查数据)
	 * */
	public Map act_select(String table,String whereField,Map<String ,Object> map){
		Map rmap = new HashMap() ;
		if(map.get("taskData")==null||map.get("total")==null||Integer.valueOf(map.get("total").toString())==0){
			rmap.put("resultformdata", new ArrayList()) ;
			return map;
		}
		try{
			List rlist = new ArrayList () ;
			Map smap = new HashMap() ;
			List<Map> obj  = (List)map.get("taskData") ;
//			if(map.get("total")){
//				
//			}
			List<String> in_fields =  new ArrayList() ;
			for(int i=0 ;i<obj.size() ; i++){
				in_fields.add(String.valueOf(obj.get(i).get("a_processInstanceId"))) ;
			}
			smap.put("in_fields",in_fields) ;
			smap.put("tableName", table);
			smap.put("flowId", whereField) ;
			rlist = getServiceSqlSession().db_selectList("select",smap);
			rmap.put("resultformdata", rlist) ;
			return rmap ;
		}catch(Exception e){
			e.printStackTrace() ;
			logger.error("act_select :"+e.getMessage()) ;
			return null ;
		}
	}
	public static void main(String []args){
		Map map = new HashMap()  ;
		String processInstanceId = "" ;
		String processKey = "";
		ActivitiFormCurdService Service = new ActivitiFormCurdService() ;
		ActivitiUtils activitiUtils = new ActivitiUtils() ;
		Map<String,Map> all = new HashMap() ;
		String dataId = String.valueOf(map.get("id")) ;
		processKey= String.valueOf(map.get("processKey")) ;
		Map rmap =new HashMap() ;
		try{
		//	rmap = Service.selectMessageFlowId(map) ;
		}catch(Exception e){
			e.printStackTrace() ;
			logger.error("未找到查询留言流程，id:"+dataId,e) ;
		}
		if(rmap.get("flowId").toString()!=""&&processKey!=""){
			try{
				processInstanceId = rmap.get("flowId").toString() ;
				activitiUtils.doClearTask(processInstanceId,"重置") ;
			}catch(Exception e){}
			
			try{
				String newprocessInstanceId = activitiUtils.doStartProcess("processKey");
				String taskid = activitiUtils.getTaskIdByProcessInstanceId(newprocessInstanceId) ;
				//map主要存flow_type
				map.put("flow_type", "2") ;
				activitiUtils.doCompletTask(taskid,map) ;
			}catch(Exception e){
				return ;
			
			}
		}
	}
	//公告删除 
     public void announceDelete(Map map){
 		try{
 			getServiceSqlSession().db_update("announceDelete" , map) ;		
 		}catch(Exception e){
 			e.printStackTrace() ;
 			Map m = new HashMap() ;
 			m.putAll(map) ;
 			logger.error("ActivitiCurdService__announceDelete:"+m.toString(),e) ;
 		}
		
		
	}
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) this.getBean("ActivitiCurdSqlSession");
	}
	
}
