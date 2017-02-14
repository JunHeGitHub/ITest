package com.zinglabs.apps.chatWorkflow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;

public class stepService extends BaseService {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	
	public boolean stepInsert(Map map,HttpServletRequest request){
		logger.debug("\nstepRecording 所需值" + map);
		int i=getServiceSqlSession().db_insert("stepInsert",map);
		if(i>0) return true;
		logger.error("插入节点出现错误！"+map);
		return false;
		
	}
	public String stepInsert1(Map map,HttpServletRequest request){
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			map.put("createDate", df.format(new Date()));
			map.put("id", new RandomGUID().toString());
			if(stepInsert(map, request)){
				String stepId=map.get("id").toString();
				//循环向strpManage表里插入scopeType为"js"的数据
				if(map.get("js")!=null&&map.get("js").toString().length()>0){
					for(int i=0;i<map.get("js").toString().split(",").length;i++){
						String []str=map.get("js").toString().split(",");
						Map<String, String> smMap=new HashMap<String, String>();
						smMap.put("stepId", stepId);
						smMap.put("stepScope", str[i]);
						smMap.put("scopeType", "js");
						if(!stepManageInsert(smMap)){
							return "添加失败";
						}
					}
				}
				//如果插入成功就继续向strpManage表里插入scopeType为"zzjg"的数据
				if(map.get("zzjg")!=null&&map.get("zzjg").toString().length()>0){
					for(int j=0;j<map.get("zzjg").toString().split(",").length;j++){
						String []zStr=map.get("zzjg").toString().split(",");
						Map<String, String> zzjgMap=new HashMap<String, String>();
						zzjgMap.put("stepId", stepId);
						zzjgMap.put("stepScope", zStr[j]);
						zzjgMap.put("scopeType", "zzjg");
						if(!stepManageInsert(zzjgMap)){
							return"添加失败";
						}
					}
				}
				return"添加成功";
			}else{
				return"添加失败";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("common find fail"+e.getMessage());
			return "异常";
		}
		
	}
	//向stepManage表中添加数据
	public boolean stepManageInsert(Map<String,String> map){
		int i=getServiceSqlSession().db_insert("InsertStepManage",map);
		if(i>0) return true;
		logger.error("插入节点出现错误！"+map);
		return false;
	}
	//修改节点的方法
	public String stepUpdate(Map map,HttpServletRequest request){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		map.put("createDate", df.format(new Date()));
		//1、修改stepRecording表中的数据
		int i=getServiceSqlSession().db_update("updateStep", map);
		//2、如果修改成功对stepManage表进行操作 先删除
		if(i>0){
			String stepId=map.get("id").toString();
			Map<String, String> newMap=new HashMap<String, String>();
			newMap.put("stepId", stepId);
			if(!stepManageUpdate(newMap)){
				return"修改失败";
			}
			//如果删除成功就继续向strpManage表里修改scopeType为"js"的数据
			if(map.get("js")!=null&&map.get("js").toString().length()>0){
				for(int js=0;js<map.get("js").toString().split(",").length;js++){
					String []str=map.get("js").toString().split(",");
					Map<String, String> jsMap=new HashMap<String, String>();
					jsMap.put("stepId", stepId);
					jsMap.put("stepScope", str[js]);
					jsMap.put("scopeType", "js");
					if(!stepManageInsert(jsMap)){
						return"修改失败";
					}
				}
			}
			//如果修改成功就继续向strpManage表里修改scopeType为"zzjg"的数据
			if(map.get("zzjg")!=null&&map.get("zzjg").toString().length()>0){
				for(int j=0;j<map.get("zzjg").toString().split(",").length;j++){
					String []zStr=map.get("zzjg").toString().split(",");
					Map<String, String> zzjgMap=new HashMap<String, String>();
					zzjgMap.put("stepId", stepId);
					zzjgMap.put("stepScope", zStr[j]);
					zzjgMap.put("scopeType", "zzjg");
					if(!stepManageInsert(zzjgMap)){
						return"修改失败";
					}
				}
			}
			return "修改成功";
		}
		logger.error("插入节点出现错误！"+map);
		return "修改失败";
	}
	//删除要修改的节点
	public boolean stepManageUpdate(Map<String,String> map){
		int i=getServiceSqlSession().db_update("UpdateStepManage",map);
		if(i>0)
			return true;
		logger.error("修改节点出现错误！"+map);
		return false;
		
	}
	//查询stepManage表中stepScope对应的组织机构名
	public List stepManageSelect(Map<String, String> map){
		return getServiceSqlSession().db_selectList("selectStepManage",map);
		
	}
	
	public boolean stepSkill(Map map,HttpServletRequest request){
		int i=getServiceSqlSession().db_insert("stepSkillInsert",map);
		if(i>0) return true;
		logger.error("插入节点出现错误！"+map);
		return false;
		
	}
	/**
	 * 条件查询 返回List
	 * 总览  查询该模板下的所有节点 及节点的执行人状态
	 * @param map
	 * @return
	 */
	public List searchSelectSkill(Map map){
		List list=getServiceSqlSession().db_selectList("searchStep",map);
		return list;
	}
	/**
	 * 条件查询 返回List
	 * 现况  查询当前用户的节点 及节点的执行人状态
	 * @param map
	 * @return
	 */
	public List selectNowStep(Map map){
		List list=getServiceSqlSession().db_selectList("searchNowStep",map);
		return list;
	}
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		// TODO Auto-generated method stub
		return (AppSqlSessionDbid)this.getBean("requestservice");
	}

}
