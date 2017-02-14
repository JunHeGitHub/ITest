package com.zinglabs.apps.activitiCommon.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.zinglabs.base.Interface.ActivitiFromInterface;
import com.zinglabs.base.core.utils.AppSpringUtils;

@SuppressWarnings("unchecked")
public class FormHandle{
	
	public static Logger A_logger = LoggerFactory.getLogger("Activiti");
	public static String InsertFormClass = "InsertFormClass" ;
	public static String UpdateFormClass = "UpdateFormClass" ;
	public static String SelectFormClass = "SelectFormClass" ;
	
	public  Map formdata(Map<String, Map> all){
		ActivitiFromInterface obj = (ActivitiFromInterface)getBean(String.valueOf(((Map)all.get("actmap")).get("A_FORMCLASS"))) ;
		try{
	        return obj.execute(all) ;
		}catch(Exception e){
			A_logger.error("formData:"+all.toString()) ;
			A_logger.error("FormHandle error",e) ;
			e.printStackTrace() ;
			return null ;
		}
	}
	public boolean validateClass(String ClassName){
		try{
			ActivitiFromInterface m = (ActivitiFromInterface)getBean(ClassName);
	        return  m.validate() ; 
		}catch(Exception e){
			A_logger.error("validateClass_ClassName:"+ClassName.toString(),e) ;
			e.printStackTrace() ;
			return false ;
		}
	}
	public Map validateStart(Map map){
		Map actmap = new HashMap() ;
		Map rmap = new HashMap() ;
		Map msg = new HashMap() ;
		msg.put("success", "true") ;
		if(map.get("A_PROCESSDEFINITIONID")!=null &&!"".equals(String.valueOf(map.get("A_PROCESSDEFINITIONID").toString()))){
			actmap.put("A_PROCESSDEFINITIONID", map.get("A_PROCESSDEFINITIONID").toString()) ;
			map.remove("A_PROCESSDEFINITIONID") ;
		}else{
			msg.put("success", "false") ;
			msg.put("mgs", "A_PROCESSDEFINITIONID  is  null!") ;
			rmap.put("mgsmap", msg) ;
		}
		if(map.get("A_FORMCLASS")!=null &&!"".equals(String.valueOf(map.get("A_FORMCLASS").toString()))){
			actmap.put("A_FORMCLASS", map.get("A_FORMCLASS").toString()) ;
			map.remove("A_FORMCLASS") ;
		}else{
			actmap.put("A_FORMCLASS", InsertFormClass) ;
			map.remove("A_FORMCLASS") ;
		}
		if(map.get("A_ASSIGNEE")!=null &&"".equals(map.get("A_ASSIGNEE"))){
			actmap.put("A_ASSIGNEE", map.get("A_ASSIGNEE").toString()) ;
			map.remove("A_ASSIGNEE") ;
		}else{
			actmap.put("A_ASSIGNEE", "") ;
		}
		if(map.get("A_PROCESSINSTANCEID")!=null&&"".equals(map.get("A_PROCESSINSTANCEID"))){
			actmap.put("A_PROCESSINSTANCEID", map.get("A_PROCESSINSTANCEID").toString()) ;
			map.remove("A_PROCESSINSTANCEID") ;
		}
		if("A_TASKID".equals(map.get("A_TASKID"))){
			actmap.put("A_TASKID", map.get("A_TASKID").toString()) ;
			map.remove("A_TASKID") ;
		}
		if("A_TASKIDS".equals(map.get("A_TASKIDS"))){
			actmap.put("A_TASKIDS", map.get("A_TASKIDS").toString()) ;
			map.remove("A_TASKIDS") ;
		}
		if("A_TASKNAME".equals(map.get("A_TASKNAME"))){
			actmap.put("A_TASKNAME", map.get("A_TASKNAME").toString()) ;
			map.remove("A_TASKNAME") ;
		}
		rmap.put("formmap", map) ;
		rmap.put("actmap", actmap) ;
		rmap.put("mgsmap", msg) ;
		return rmap;
	}

	public Map validateComplete(Map map){
		Map actmap = new HashMap() ;
		Map rmap = new HashMap() ;
		Map msg = new HashMap() ;
		rmap.put("mgsmap", msg.put("success", "true")) ;
		if(map.get("A_PROCESSDEFINITIONID")!=null &&!"".equals(String.valueOf(map.get("A_PROCESSDEFINITIONID").toString()))){
			actmap.put("A_PROCESSDEFINITIONID", map.get("A_PROCESSDEFINITIONID").toString()) ;
			map.remove("A_PROCESSDEFINITIONID") ;
		}else{
			msg.put("success", "false") ;
			msg.put("mgs", "A_PROCESSDEFINITIONID  is  null!") ;
			rmap.put("mgsmap", msg) ;
		}
		if(map.get("A_PROCESSINSTANCEID")!=null &&!"".equals(String.valueOf(map.get("A_PROCESSINSTANCEID").toString()))){
			actmap.put("A_PROCESSINSTANCEID", map.get("A_PROCESSINSTANCEID").toString()) ;
			map.remove("A_PROCESSINSTANCEID") ;
		}else{
			msg.put("success", "false") ;
			msg.put("mgs", "A_PROCESSINSTANCEID  is  null!") ;
			rmap.put("mgsmap", msg) ;
		}
		if(map.get("A_FORMCLASS")!=null &&!"".equals(String.valueOf(map.get("A_FORMCLASS").toString()))){
			actmap.put("A_FORMCLASS", map.get("A_FORMCLASS").toString()) ;
			map.remove("A_FORMCLASS") ;
		}else{
			actmap.put("A_FORMCLASS", UpdateFormClass) ;
			map.remove("A_FORMCLASS") ;
		}
		if(map.get("A_ASSIGNEE")!=null &&!"".equals(String.valueOf(map.get("A_ASSIGNEE").toString()))){
			actmap.put("A_ASSIGNEE", map.get("A_ASSIGNEE").toString()) ;
			map.remove("A_ASSIGNEE") ;
		}else{
			actmap.put("A_ASSIGNEE", "") ;
		}
		
		if(map.get("A_TASKID")!=null &&!"".equals(String.valueOf(map.get("A_TASKID").toString()))){
			actmap.put("A_TASKID", map.get("A_TASKID").toString()) ;
			map.remove("A_TASKID") ;
		}
		if("A_TASKNAME".equals(map.get("A_TASKNAME"))){
			actmap.put("A_TASKNAME", map.get("A_TASKNAME").toString()) ;
			map.remove("A_TASKNAME") ;
		}
		rmap.put("formmap", map) ;
		rmap.put("actmap", actmap) ;
		rmap.put("mgsmap", msg) ;
		return rmap;
	}
	
	public Map validateGetUnClaimTask(Map map){
		Map actmap = new HashMap() ;
		Map rmap = new HashMap() ;
		Map msg = new HashMap() ;
		rmap.put("mgsmap", msg.put("success", "true")) ;
		if(map.get("A_PROCESSDEFINITIONID")!=null &&!"".equals(String.valueOf(map.get("A_PROCESSDEFINITIONID").toString()))){
			actmap.put("A_PROCESSDEFINITIONID", map.get("A_PROCESSDEFINITIONID").toString()) ;
			map.remove("A_PROCESSDEFINITIONID") ;
		}else{
			msg.put("success", "false") ;
			msg.put("mgs", "A_PROCESSDEFINITIONID  is  null!") ;
			rmap.put("mgsmap", msg) ;
		}
		if(map.get("A_FORMCLASS")!=null &&!"".equals(String.valueOf(map.get("A_FORMCLASS").toString()))){
			actmap.put("A_FORMCLASS", map.get("A_FORMCLASS").toString()) ;
			map.remove("A_FORMCLASS") ;
		}else{
			actmap.put("A_FORMCLASS", SelectFormClass) ;
			map.remove("A_FORMCLASS") ;
		}
		if(map.get("A_ASSIGNEE")!=null &&!"".equals(String.valueOf(map.get("A_ASSIGNEE").toString()))){
			actmap.put("A_ASSIGNEE", map.get("A_ASSIGNEE").toString()) ;
			map.remove("A_ASSIGNEE") ;
		}
		if(map.get("A_PROCESSINSTANCEID")!=null &&!"".equals(String.valueOf(map.get("A_PROCESSINSTANCEID").toString()))){
			actmap.put("A_PROCESSINSTANCEID", map.get("A_PROCESSINSTANCEID").toString()) ;
			map.remove("A_PROCESSINSTANCEID") ;
		}
		if(map.get("A_TASKID")!=null &&!"".equals(String.valueOf(map.get("A_TASKID").toString()))){
			actmap.put("A_TASKID", map.get("A_TASKID").toString()) ;
			map.remove("A_TASKID") ;
		}
		if("A_TASKIDS".equals(map.get("A_TASKIDS"))){
			actmap.put("A_TASKIDS", map.get("A_TASKIDS").toString()) ;
			map.remove("A_TASKIDS") ;
		}
		
		if(map.get("A_TASKNAME")!=null &&!"".equals(String.valueOf(map.get("A_TASKNAME").toString()))){
			actmap.put("A_TASKNAME", map.get("A_TASKNAME").toString()) ;
			map.remove("A_TASKNAME") ;
		}else{
			msg.put("success", "false") ;
			msg.put("mgs", "A_TASKNAME  is  null!") ;
			rmap.put("mgsmap", msg) ;
		}
		rmap.put("formmap", map) ;
		rmap.put("actmap", actmap) ;
		rmap.put("mgsmap", msg) ;
		return rmap;
	}
	
	
	public Map validateUserTask(Map map){
		Map actmap = new HashMap() ;
		Map rmap = new HashMap() ;
		Map msg = new HashMap() ;
		rmap.put("mgsmap", msg.put("success", "true")) ;
		if(map.get("A_PROCESSDEFINITIONID")!=null &&!"".equals(String.valueOf(map.get("A_PROCESSDEFINITIONID").toString()))){
			actmap.put("A_PROCESSDEFINITIONID", map.get("A_PROCESSDEFINITIONID").toString()) ;
			map.remove("A_PROCESSDEFINITIONID") ;
		}else{
			msg.put("success", "false") ;
			msg.put("mgs", "A_PROCESSDEFINITIONID  is  null!") ;
			rmap.put("mgsmap", msg) ;
		}
		if(map.get("A_FORMCLASS")!=null &&!"".equals(String.valueOf(map.get("A_FORMCLASS").toString()))){
			actmap.put("A_FORMCLASS", map.get("A_FORMCLASS").toString()) ;
			map.remove("A_FORMCLASS") ;
		}else{
			actmap.put("A_FORMCLASS", SelectFormClass) ;
			map.remove("A_FORMCLASS") ;
		}
		if(map.get("A_ASSIGNEE")!=null &&!"".equals(String.valueOf(map.get("A_ASSIGNEE").toString()))){
			actmap.put("A_ASSIGNEE", map.get("A_ASSIGNEE").toString()) ;
			map.remove("A_ASSIGNEE") ;
		}
		if(map.get("A_PROCESSINSTANCEID")!=null &&!"".equals(String.valueOf(map.get("A_PROCESSINSTANCEID").toString()))){
			actmap.put("A_PROCESSINSTANCEID", map.get("A_PROCESSINSTANCEID").toString()) ;
			map.remove("A_PROCESSINSTANCEID") ;
		}
		if(map.get("A_TASKID")!=null &&!"".equals(String.valueOf(map.get("A_TASKID").toString()))){
			actmap.put("A_TASKID", map.get("A_TASKID").toString()) ;
			map.remove("A_TASKID") ;
		}
		if("A_TASKIDS".equals(map.get("A_TASKIDS"))){
			actmap.put("A_TASKIDS", map.get("A_TASKIDS").toString()) ;
			map.remove("A_TASKIDS") ;
		}
		if("A_TASKNAME".equals(map.get("A_TASKNAME"))){
			actmap.put("A_TASKNAME", map.get("A_TASKNAME").toString()) ;
			map.remove("A_TASKNAME") ;
		}
		rmap.put("formmap", map) ;
		rmap.put("actmap", actmap) ;
		rmap.put("mgsmap", msg) ;
		return rmap;
	}
	
	
	public Map validatedoClaimByTaskId(Map map){
		Map actmap = new HashMap() ;
		Map rmap = new HashMap() ;
		Map msg = new HashMap() ;
		msg.put("success", "true") ;
		if(map.get("A_PROCESSDEFINITIONID")!=null &&!"".equals(String.valueOf(map.get("A_PROCESSDEFINITIONID").toString()))){
			actmap.put("A_PROCESSDEFINITIONID", map.get("A_PROCESSDEFINITIONID").toString()) ;
			map.remove("A_PROCESSDEFINITIONID") ;
		}else{
			msg.put("success", "false") ;
			msg.put("mgs", "A_PROCESSDEFINITIONID  is  null!") ;
			rmap.put("mgsmap", msg); 
		}
		if(map.get("A_FORMCLASS")!=null &&!"".equals(String.valueOf(map.get("A_FORMCLASS").toString()))){
			actmap.put("A_FORMCLASS", map.get("A_FORMCLASS").toString()) ;
			map.remove("A_FORMCLASS") ;
		}else{
			actmap.put("A_FORMCLASS", InsertFormClass) ;
			map.remove("A_FORMCLASS") ;
		}
		if(map.get("A_ASSIGNEE")!=null &&!"".equals(String.valueOf(map.get("A_ASSIGNEE").toString()))){
			actmap.put("A_ASSIGNEE", map.get("A_ASSIGNEE").toString()) ;
			map.remove("A_ASSIGNEE") ;
		}else{
			msg.put("success", "false") ;
			msg.put("mgs", "A_ASSIGNEE  is  null!") ;
			rmap.put("mgsmap", msg); 
		}
		if("A_PROCESSINSTANCEID".equals(map.get("A_PROCESSINSTANCEID"))){
			actmap.put("A_PROCESSINSTANCEID", map.get("A_PROCESSINSTANCEID").toString()) ;
			map.remove("A_PROCESSINSTANCEID") ;
		}
		if("A_TASKID".equals(map.get("A_TASKID"))){
			actmap.put("A_TASKID", map.get("A_TASKID").toString()) ;
			map.remove("A_TASKID") ;
		}
		if(map.get("A_TASKIDS")!=null &&!"".equals(String.valueOf(map.get("A_TASKIDS").toString()))){
			actmap.put("A_TASKIDS", map.get("A_TASKIDS").toString()) ;
			map.remove("A_TASKIDS") ;
		}else{
			msg.put("success", "false") ;
			msg.put("mgs", "A_TASKIDS  is  null!") ;
			rmap.put("mgsmap", msg); 
		}
		if("A_TASKNAME".equals(map.get("A_TASKNAME"))){
			actmap.put("A_TASKNAME", map.get("A_TASKNAME").toString()) ;
			map.remove("A_TASKNAME") ;
		}
		rmap.put("formmap", map) ;
		rmap.put("actmap", actmap) ;
		rmap.put("mgsmap", msg) ;
		return rmap;
	}
	
	public List getUser(Map map ){
		List list = new ArrayList() ;
		Map rmap = new HashMap() ;
		if(map.get("zkmUserName")!=null&&map.get("zkmUserName")!=""){
			
			rmap.put("userName",map.get("zkmUserName").toString() ) ;
			map.remove("zkmUserName") ;
		}
		if(map.get("zkmLoginName")!=null&&map.get("zkmLoginName")!=""){
			
			rmap.put("loginName",map.get("zkmLoginName").toString() ) ;
			map.remove("zkmLoginName") ;
		}
		list.add(map) ;
		list.add(rmap) ;
		return list ;
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
