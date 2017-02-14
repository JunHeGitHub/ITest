package com.zinglabs.util.flowUtil.FlowDisponseImp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import net.sf.json.JSONSerializer;

import com.zinglabs.apps.zkmSysdblog.ZkmSysDBLogService;
import com.zinglabs.db.DAOTools;
import com.zinglabs.db.UserInfo2;
import com.zinglabs.db.ZZkmRecordClickUtils;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.FileUtils;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;
import com.zinglabs.util.WebFormUtil;
import com.zinglabs.util.ZKMAPPUtils;
import com.zinglabs.util.ZKMConfs;
import com.zinglabs.util.excelUtils.ExcelVaildateHelper;
import com.zinglabs.util.flowUtil.FlowDisponse;
import com.zinglabs.util.flowUtil.FlowHelper;
import com.zinglabs.util.flowUtil.DisponseBeans.DisponseBase;

public class ZkmPublicInfoFlowDisponse extends DisponseBase{
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	
	public final String [] DISPONSE_SEARCH_TERMS={"infoType:=","title:like"};
	public final String DISPONSE_SEARCH_FIELDS="id,infoType,approveState,appointedCheckUser,title,area,createUser,createTime,FBTime,summary,disponseOpinion,checkOneOpinion,changeOpinion,flowType,flowNode,disponseUser,disponseBTime,disponseETime,flowID,SMSSendPhone,filePath,fileName,appointedCheckUserID,CJWTTreeSelect,CJWTTreeSelectIds,notTrue,notTimely,notNormally,notTrueBZ,notTimelyBZ,notNormallyBZ,companyId,companyName,departmentId,departmentName";
	public final String[] DISPONSE_UPDATE_FIELDS={"infoType","approveState","appointedCheckUser","title","area","createUser","createTime","FBTime","summary","disponseOpinion","checkOneOpinion","changeOpinion","flowType","flowNode","disponseUser","disponseBTime","disponseETime","flowID","SMSSendPhone","filePath","fileName","appointedCheckUserID","CJWTTreeSelect","CJWTTreeSelectIds","notTrue","notTimely","notNormally","notTrueBZ","notTimelyBZ","notNormallyBZ","companyId","companyName","departmentId","departmentName"};
	public final String dtable="zkmPublicInfoDisponse";
	public final String htable="zkmPublicInfoDisponseHistory";
	
	public void disponseSearch(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		List plist=new ArrayList();
		String sql="select " + DISPONSE_SEARCH_FIELDS + " from " +dtable + " where 1=1 ";
		String sqlWhere=" and disponseUser = ? ";
		plist.add(user.loginName2);
		for(String field:DISPONSE_SEARCH_TERMS){
			String []f=field.split(":");
			String f0=f[0];
			String f1=f[1];
			String v=wfu.get(f0);
			if(v.length()>0){
				if("=".equals(f1)){
					sqlWhere+=" and " + f0 + "=?";
					plist.add(v);
				}else if("like".equals(f1)){
					v=v.trim().replaceAll("\\*", "%");
					plist.add(v);
					sqlWhere+=" and " + f0 + " like ?";
				}
			}
		}
		
		sql+=sqlWhere;
		if(wfu.get("orderField").length()>0 && wfu.get("orderType").length()>0){
			sql+=" order by " + wfu.get("orderField") + " " + wfu.get("orderType");
		}else{
			sql+=" order by disponseBTime desc";
		}
		try{
			List list=DAOTools.queryMap(sql, plist, zkmDBId);
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", list);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		}catch(Exception x){
			LogUtil.error("zkmDocDisponseSearch ---- error :" , SKIP_Logger);
			LogUtil.error(x, SKIP_Logger);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "获取待处理数据异常。"), response);
		}
	}
	
	public final String [] DISPONSE_SEARCH_TERMS_ALL={"infoType:=","title:like","approveState:=","createUser:like",};
	public void disponseSearch_All(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		List plist=new ArrayList();
		String sql="select " + DISPONSE_SEARCH_FIELDS + " from " +dtable + " where 1=1 ";
		String sqlWhere=" ";
		for(String field:DISPONSE_SEARCH_TERMS_ALL){
			String []f=field.split(":");
			String f0=f[0];
			String f1=f[1];
			String v=wfu.get(f0);
			if(v.length()>0){
				if("=".equals(f1)){
					sqlWhere+=" and " + f0 + "=?";
					plist.add(v);
				}else if("like".equals(f1)){
					v=v.trim().replaceAll("\\*", "%");
					plist.add(v);
					sqlWhere+=" and " + f0 + " like ?";
				}
			}else{
				if(f0.equals("approveState")){
					sqlWhere+=" and " + f0 + "=?";
					plist.add(v);
				}
			}
		}
		sql+=sqlWhere;
		String bdate=wfu.get("bDate");
		String edate=wfu.get("eDate");
		String format="'%Y-%m-%d %H:%i:%S'";
		if(bdate.length()>0 && edate.length()>0){
			sql+=" and (createTime >= DATE_FORMAT(?,"+ format +") and createTime <= DATE_FORMAT(?,"+ format +")) ";
			plist.add(bdate);
			plist.add(edate);
		}else if(bdate.length()>0 && edate.length()<=0){
			sql+=" and createTime >= DATE_FORMAT(?,"+ format +") ";
			plist.add(bdate);
		}else if(bdate.length()<=0 && edate.length()>0){
			sql+=" and createTime <= DATE_FORMAT(?,"+ format +") ";
			plist.add(edate);
		}
		if(wfu.get("orderField").length()>0 && wfu.get("orderType").length()>0){
			sql+=" order by " + wfu.get("orderField") + " " + wfu.get("orderType");
		}else{
			sql+=" order by createTime desc";
		}
		try{
			List list=DAOTools.queryMap(sql, plist, zkmDBId);
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", list);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		}catch(Exception x){
			LogUtil.error("zkmDocDisponseSearch ---- error :" , SKIP_Logger);
			LogUtil.error(x, SKIP_Logger);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "获取待处理数据异常。"), response);
		}
	}
	
	public  void disponseActive(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		if(wfu.get("id").length()>0){
			postStrToClient(JSONSerializer.toJSON(disponseEdit(wfu,user)).toString(), response);
		}else{
			postStrToClient(JSONSerializer.toJSON(disponseAdd(wfu,user)).toString(), response);
		}
	}
	
	public  HashMap disponseAdd(WebFormUtil wfu, UserInfo2 user){
		String id=new RandomGUID().toString();
		String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
		String sql="insert into " +dtable + " ";
		List plist=new ArrayList();
		String fields="(id,";
		String values="(?,";
		plist.add(id);
		for(int i=0;i<DISPONSE_UPDATE_FIELDS.length;i++){
			String f=DISPONSE_UPDATE_FIELDS[i];
			String v=wfu.get(f);
			if(("bDate".equals(f) || "eDate".equals(f) || "disponseETime".equals(f) || "FBTime".equals(f)) && v.length()<=0){
				v="0000-00-00 00:00:00";
			}else if("createTime".equals(f) || "disponseBTime".equals(f)){
				v=date;
			}else if("createUser".equals(f) || "disponseUser".equals(f)){
				v=user.loginName2;
			}
			if(i+1<DISPONSE_UPDATE_FIELDS.length){
				fields+=f+",";
				values+="?,";
			}else{
				fields+=f+") ";
				values+="?) ";
			}
			plist.add(v);
		}
		sql=sql + fields + " values " + values;
		try{
			DAOTools.updateForPrepared(sql,plist, zkmDBId);
			//防止数据复制慢，导致本地数据问题。
			Thread.sleep(200);
			Map idata=disponseSelectOne(id);
			if(idata!=null){
				return JsonUtils.genUpdateDataReturnMap(true,"数据保存成功。",idata);
			}else{
				return JsonUtils.genUpdateDataReturnMap(false, "新增数据异常失败。",null);
			}
		}catch(Exception x){
			LogUtil.error(x, SKIP_Logger);
			return JsonUtils.genUpdateDataReturnMap(false, "新增数据异常失败。",null);
		}
	}
	
	public  HashMap disponseEdit(WebFormUtil wfu, UserInfo2 user){
		String id=wfu.get("id");
		if(id.length()>0){
			String sql="update " +dtable + " set ";
			List plist =new ArrayList();
			String sets="";
			for(int i=0;i<DISPONSE_UPDATE_FIELDS.length;i++){
				String f=DISPONSE_UPDATE_FIELDS[i];
				String v=wfu.get(f);
				if(("bDate".equals(f) || "eDate".equals(f) || "disponseETime".equals(f) 
						|| "createTime".equals(f) || "disponseBTime".equals(f) || "FBTime".equals(f)) && v.length()<=0){
					v="0000-00-00 00:00:00";
				}
				plist.add(v);
				if(i+1 <DISPONSE_UPDATE_FIELDS.length){
					sets+=f + "=?,";
				}else{
					sets+=f + "=? ";
				}
			}
			plist.add(id);
			sql+=sets + " where id=?";
			try{
				DAOTools.updateForPrepared(sql,plist, zkmDBId);
				//防止数据复制慢，导致本地数据问题。
				Thread.sleep(200);
				Map idata=disponseSelectOne(id);
				if(idata!=null){
					return JsonUtils.genUpdateDataReturnMap(true, "数据保存成功。",idata);
				}else{
					return JsonUtils.genUpdateDataReturnMap(false, "更新数据异常失败。",null);
				}
			}catch(Exception x){
				LogUtil.error(x, SKIP_Logger);
				return JsonUtils.genUpdateDataReturnMap(false, "更新数据异常失败。",null);
			}
		}else{
			return JsonUtils.genUpdateDataReturnMap(false, "更新数据失败，缺少必要参数。",null);
		}
	}
	
	public  Map disponseSelectOne(String id) throws Exception{
		String sql="select " + DISPONSE_SEARCH_FIELDS + " from " +dtable + " where id='"+ id +"' ";
		List<Map> list=DAOTools.queryMap(sql, zkmDBId);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return  null;
		}
	}
	
	public void submitFlow(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String id=wfu.get("id");
		String sb=wfu.get("approveState");
		String flowType=wfu.get("flowType");
		String flowNode=wfu.get("flowNode");
		String flowID=wfu.get("flowID");
		boolean isStart=false;
		boolean isEnd=false;
		String sql="";
		Map dataMap=null;
		//验证ID存在
		if(id.length()<=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败，数据未保存。"), response);
			return;
		}
		//保存数据并获取数据内容
		try{
			Map saver=null;
			if(wfu.get("id").length()>0){
				saver=disponseEdit(wfu,user);
			}else{
				//saver=zkmDocDisponseAdd(wfu,user);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败，数据未保存。"), response);
				return;
			}
			if(((Boolean)saver.get("success")).booleanValue()){
				dataMap=(Map)saver.get("data");
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败，数据未保存或数据异常。"), response);
				return;
			}
		}catch(Exception x){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转由于异常失败－数据库异常"), response);
			LogUtil.error(x, SKIP_Logger);
			return;
		}
		if(sb.length()<=0 || flowType.length()<=0 || flowNode.length()<=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败，缺少必要参数。"), response);
		}else{
			try {
				isStart=FlowHelper.isStart(flowType, flowNode);
				isEnd=FlowHelper.isEnd(flowType, flowNode);
			} catch (Exception x) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转由于异常失败－无法获取必要条件"), response);
				LogUtil.error(x, SKIP_Logger);
				return;
			}
			FlowDisponse flowd=FlowHelper.getFlowDisponse("zkmPublicInfoFlowDisponseClass");
			if(flowd==null){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转由于异常失败。"), response);
				LogUtil.error("获取流程处理类失败 key ：‘zkmPublicInfoFlowDisponseClass’",SKIP_Logger);
				return;
			}
			//记录操作日志的对象
			ZkmSysDBLogService log2db=new ZkmSysDBLogService();
			if("待审批".equals(sb)){
				if(isStart){
					Map next=null;
					try{
						next=FlowHelper.getNext(flowType, flowNode);
						if(next!=null){
							Map reMap=flowd.disponseFirst(next, dataMap, null);
							String mgs=reMap.get("mgs")==null?"":(String)reMap.get("mgs");
							String dataId=dataMap.get("id")==null?"":(String)dataMap.get("id");
							String infoType=dataMap.get("infoType")==null?"":(String)dataMap.get("infoType");
							String title=dataMap.get("title")==null?"":(String)dataMap.get("title");
							String area=dataMap.get("area")==null?"":(String)dataMap.get("area");
							String userName=user.loginName2;
							
							if(((Boolean)reMap.get("success")).booleanValue()){
								//记录操作日志
								String content="经办待审批公共信息，名称："+ title +"；类型：" +infoType + "；地区：" + area + "；";
								log2db.sysZkmOperationLogSave(dataId,userName, flowType, content, "经办",request);
								//记录经办统计数据，公共信息只有新增
								ZZkmRecordClickUtils.zkmJBAdd(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMPUBLICINFO,request);
								
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, mgs,reMap), response);
							}else{
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, mgs,reMap), response);
							}
						}else{
							postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-下一节点为空。"), response);
						}
					}catch(Exception x){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转由于异常失败。"), response);
						LogUtil.error(x, SKIP_Logger);
					}
				}else{
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-非起始节点，请选择“通过或拒绝”"), response);
				}
			}else if("通过".equals(sb)){
				if(isStart){
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-起始节点，请选择“待审批”"), response);
				}else if(isEnd){
					if(flowID.length()<=0){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-缺少必要参数。"), response);
						return;
					}
					try{
						Map map=FlowHelper.getNode(flowType, flowNode);
						if(map!=null){
							Map reMap=flowd.disponseEnd(map, dataMap, null);
							String dataId=dataMap.get("id")==null?"":(String)dataMap.get("id");
							String infoType=dataMap.get("infoType")==null?"":(String)dataMap.get("infoType");
							String title=dataMap.get("title")==null?"":(String)dataMap.get("title");
							String area=dataMap.get("area")==null?"":(String)dataMap.get("area");
							String userName=user.loginName2;
							
							String mgs=reMap.get("mgs")==null?"":(String)reMap.get("mgs");
							if(((Boolean)reMap.get("success")).booleanValue()){
								//记录操作日志
								String content="复核发布公共信息，名称："+ title +"；类型：" +infoType + "；地区：" + area + "；";
								log2db.sysZkmOperationLogSave(dataId, userName, flowType, content, "发布",request);
								//记录复核统计数据，公共信息只有新增
								ZZkmRecordClickUtils.zkmFHAdd(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMPUBLICINFO,request);
								
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, mgs,reMap), response);
							}else{
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,mgs,reMap), response);
							}
						}else{
							postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-当前结点定义为空。"), response);
						}
						//防止数据复制慢，导致本地数据问题。
						Thread.sleep(200);
					}catch(Exception x){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转由于异常失败。"), response);
						LogUtil.error(x, SKIP_Logger);
					}
				}else{
					if(flowID.length()<=0){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-缺少必要参数。"), response);
						return;
					}
					Map next=null;
					try{
						next=FlowHelper.getNext(flowType, flowNode);
						if(next!=null){
							Map reMap=flowd.disponseNext(next, dataMap, null);
							String mgs=reMap.get("mgs")==null?"":(String)reMap.get("mgs");
							
							String dataId=dataMap.get("id")==null?"":(String)dataMap.get("id");
							String infoType=dataMap.get("infoType")==null?"":(String)dataMap.get("infoType");
							String title=dataMap.get("title")==null?"":(String)dataMap.get("title");
							String area=dataMap.get("area")==null?"":(String)dataMap.get("area");
							String userName=user.loginName2;
							
							if(((Boolean)reMap.get("success")).booleanValue()){
								//记录操作日志
								String content="复核通过公共信息，名称："+ title +"；类型：" +infoType + "；地区：" + area + "；";
								log2db.sysZkmOperationLogSave(dataId, userName, flowType, content, "复核",request);
								//记录复核统计数据，公共信息只有新增
								ZZkmRecordClickUtils.zkmFHAdd(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMPUBLICINFO,request);
								
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, mgs,reMap), response);
							}else{
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, mgs,reMap), response);
							}
						}else{
							postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-下一节点为空。"), response);
						}
					}catch(Exception x){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转由于异常失败-无法获取下步节点。"), response);
						LogUtil.error(x, SKIP_Logger);
					}
				}
			}else if("拒绝".equals(sb)){
				if(isStart){
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-当前节点为开始节点。"), response);
				}else{
					if(flowID.length()<=0){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-缺少必要参数。"), response);
					}
					Map first=null;
					try{
						first=FlowHelper.getStart(flowType);
						//TODO:保存现有数据，找到起始流程数据。
						if(first!=null){
							Map reMap=flowd.disponseToFirst(first, dataMap, null);
							
							String dataId=dataMap.get("id")==null?"":(String)dataMap.get("id");
							String infoType=dataMap.get("infoType")==null?"":(String)dataMap.get("infoType");
							String title=dataMap.get("title")==null?"":(String)dataMap.get("title");
							String area=dataMap.get("area")==null?"":(String)dataMap.get("area");
							String userName=user.loginName2;
							String cuser=dataMap.get("createUser")==null?"":(String)dataMap.get("createUser");
							
							if(((Boolean)reMap.get("success")).booleanValue()){
								//记录操作日志
								String content="复核退回公共信息，名称："+ title +"；类型：" +infoType + "；地区：" + area + "；";
								log2db.sysZkmOperationLogSave(dataId, userName, flowType, content, "退回",request);
								//记录复核统计数据，公共信息只有新增
									ZZkmRecordClickUtils.zkmFHAdd(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMPUBLICINFO,request);
								
								//记录复核退回统计数据
								ZZkmRecordClickUtils.zkmFHToBack(dataId, userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMPUBLICINFO,request);
								//记录经办被退回统计数据
								ZZkmRecordClickUtils.zkmJBToBack(dataId, cuser,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMPUBLICINFO,request);
								
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "流程提交成功。",reMap), response);
							}else{
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "",reMap), response);
							}
						}else{
							postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-没有找到第一节点设置。"), response);
						}
					}catch(Exception x){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-获取起始节点异常。"), response);
						LogUtil.error(x, SKIP_Logger);
					}
				}
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败，选择了非法审批状态。"), response);
			}
		}
	}
	
	public void decompressAndVaildate(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		WebFormUtil wfu=new WebFormUtil(request);
		String vkey=wfu.get("vkey");
		String file=wfu.get("file");
		if(vkey.length()>0 && file.length()>0){
			if("分支机构调查表".equals(vkey)){
				Map map=vaildateData(vkey,file);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(map),response);
			}else if("理财产品一览表".equals(vkey)){
				Map map=unZipAndVaildateExcel(vkey,file);
				boolean success=map.get("success")==null?false:((Boolean)map.get("success")).booleanValue();
				if(success){
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr((Map)map.get("data")),response);
				}else{
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(map),response);
				}
			}else if("营销活动汇总".equals(vkey)){
				Map map=unZipAndVaildateExcel(vkey,file);
				boolean success=map.get("success")==null?false:((Boolean)map.get("success")).booleanValue();
				if(success){
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr((Map)map.get("data")),response);
				}else{
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(map),response);
				}
			}else if("自助设备信息调查表".equals(vkey)){
				Map map=vaildateData(vkey,file);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(map),response);
			}else if("常见问题".equals(vkey)){
				Map map=vaildateData(vkey,file);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(map),response);
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "没有找到相关" + vkey +" excel的验证定义。"), response);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数，excel验证失败。"), response);
		}
	}
	
	public Map unZipAndVaildateExcel(String vkey,String file){
		Map map=unZipToTempFolder(file);
		if(((Boolean)map.get("success")).booleanValue()){
			File zf=(File)map.get("data");
			try{
				List<File> xfs=FileUtils.getFilesForFolder(zf,".xls",".xlsx");
				if(xfs.size()>0 && xfs.size()<2){
					Map vmap=vaildateData(vkey,xfs.get(0).getPath());
					return JsonUtils.genUpdateDataReturnMap(true,"",vmap);
				}else{
					return JsonUtils.genUpdateDataReturnMap(false,"ZIP压缩包中不包含excel文件或包包含excel文件大于1.",null);
				}
			}catch(Exception x){
				LogUtil.error("对解压后目录进行文件识别发生异常：" , SKIP_Logger);
				LogUtil.error(x, SKIP_Logger);
				return JsonUtils.genUpdateDataReturnMap(false,"对解压后目录进行文件识别发生异常。",null);
			}
		}else{
			return JsonUtils.genUpdateDataReturnMap(false,(String)map.get("mgs"),null);
		}
	}
	
	public Map vaildateData(String key,String file){
		if(file!=null && file.length()>0){
			File f=new File(file);
			if(f.exists() && f.isFile()){
				boolean isExcel=false;
				/*try{
					Map map=(Map)FileUtils.parserFile(file);
					Map mdt=(Map)map.get(FileUtils.FILE_METADATA);
					String mt=(String)mdt.get("Content-Type");
					if(FileUtils.FILE_MIMETYPE_OFFICE_XLS.equals(mt) || FileUtils.FILE_MIMETYPE_OFFICE_XLSX.equals(mt)){
						isExcel=true;
					}
				}catch(Exception x){
					LogUtil.error("文件识别失败：" + f.getPath() + " ： ", SKIP_Logger);
					LogUtil.error(x, SKIP_Logger);
					return JsonUtils.genUpdateDataReturnMap(false,"异常：文件识别失败，文件异常或不是Excel文件。",null);
				}*/
				String en=FileUtils.getFileExtendName(f);
				if(en.length()>0 && (en.toUpperCase().equals(".XLS") || en.toUpperCase().equals(".XLSX"))){
					isExcel=true;
				}
				if(isExcel){
					ExcelVaildateHelper evh=new ExcelVaildateHelper();
					return JsonUtils.genUpdateDataReturnMap(true,"",evh.vaildateExcelFile(key, file));
				}else{
					return JsonUtils.genUpdateDataReturnMap(false,"异常：非Excel文件。",null);
				}
			}else{
				return JsonUtils.genUpdateDataReturnMap(false,"异常：文件不存在。",null);
			}
		}else{
			return JsonUtils.genUpdateDataReturnMap(false,"参数不正确，传入文件路径为空。",null);
		}
	}
	
	public Map unZipToTempFolder(String file){
		File unFolder=null;
		String mgs="";
		boolean success=false;
		if(file!=null && file.length()>0){
			boolean isZip=false;
			File zf=new File(file);
			if(zf.exists() && zf.isFile()){
				/*try{
					Map map=(Map)FileUtils.parserFile(file);
					Map mdt=(Map)map.get(FileUtils.FILE_METADATA);
					String mt=(String)mdt.get("Content-Type");
					if(FileUtils.FILE_MIMETYPE_ZIP.equals(mt)){
						isZip=true;
					}
				}catch(Exception x){
					mgs="异常：文件识别失败，文件异常或不是ZIP文件。";
					LogUtil.error("解压文件－文件识别失败：" + zf.getPath() + " ： ", SKIP_Logger);
					LogUtil.error(x, SKIP_Logger);
				}*/
				String en=FileUtils.getFileExtendName(zf);
				if(en.length()>0 && en.toUpperCase().equals(".ZIP")){
					isZip=true;
				}
				if(isZip){
					String tmpDir=ZKMConfs.confs.getProperty("mergeTempDir","/mnt/zkm_temp_F");
					unFolder=FileUtils.createFolderUseSystemDate(tmpDir);
					RandomGUID rg=new RandomGUID();
					unFolder=new File(unFolder.getPath() + "/" + rg.toString());
					ZKMAPPUtils.fcdirChangeUserAndGroup(unFolder);
					try{
						FileUtils.unzip(zf, unFolder);
						ZKMAPPUtils.dirChangeUserAndGroup(unFolder, true);
						success=true;
					}catch(Exception x){
						LogUtil.error("解压文件－异常失败。",SKIP_Logger);
						mgs="解压文件－异常失败。";
						LogUtil.error(x, SKIP_Logger);
					}
				}else{
					mgs="解压文件失败-文件非ZIP文件。";
					LogUtil.error("解压文件-文件非ZIP文件。",SKIP_Logger);
				}
			}else{
				mgs="解压文件失败-文件不存在。";
				LogUtil.error("解压文件－文件不存在。",SKIP_Logger);
			}
		}else{
			mgs="参数不正确，传入文件路径为空。";
			LogUtil.error("参数不正确，传入文件路径为空。",SKIP_Logger);
		}
		return JsonUtils.genUpdateDataReturnMap(success,mgs,unFolder);
	}
	
	public void getWaitFlowInfo(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String flowType=wfu.get("flowType");
		if(flowType.length()>0){
			String sql="select " +DISPONSE_SEARCH_FIELDS + " from " + dtable +" where 1=1 ";
			List plist=new ArrayList();
			String sw=getWaitInfoSqlWhere(wfu,user,plist);
			sql+=sw;
			try{
				List list=DAOTools.queryMap(sql, plist, zkmDBId);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取未分配数据成功。",list), response);
			}catch(Exception x){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "获取未分配数据异常。"), response);
				LogUtil.error(x, SKIP_Logger);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数。"), response);
		}
	}
	
	public void setWaitFlowInfoToUser(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String ids_=wfu.get("ids");
		if(ids_.length()>0){
			String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
			String sql="update "+ dtable +" set disponseUser=?,disponseBTime=? where id=? and disponseUser =?";
			String[] ids=ids_.split(",");
			boolean allUp=true;
			for(String id:ids){
				if(id!=null && id.length()>0){
					String[] p={user.loginName2,date,id,""};
					try{
						DAOTools.updateForPrepared(sql, p, zkmDBId);
					}catch(Exception x){
						allUp=false;
						LogUtil.error("------ error get wait doc : " + id,SKIP_Logger);
						LogUtil.error(x, SKIP_Logger);
					}
				}
			}
			if(allUp){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取待处理数据获取成功。"), response);
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取待处理数据获取，部分异常失败。"), response);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "未选择任何分配数据或缺少必要参数。"), response);
		}
	}
	
	public  void getWaitFlowInfoNum(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		if(!(user.checkUserTextSymbolSecurity("ZKMDocDisponseFHOne")) && !(user.checkUserTextSymbolSecurity("ZKMDocDisponseFHTwo"))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户无权限查看未分配数据。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String flowType=wfu.get("flowType");
		if(flowType.length()>0){
			String sql="select count(1) num from "+ dtable +" where 1=1 ";
			List plist=new ArrayList();
			String sw=getWaitInfoSqlWhere(wfu,user,plist);
			sql+=sw;
			try{
				List list=DAOTools.queryMap(sql, plist, zkmDBId);
				String num="0";
				if(list!=null && list.size()>0){
					Map map=(Map)list.get(0);
					num=map.get("num")==null?"0":(String)map.get("num");
				}
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取未分配数据成功。",num), response);
			}catch(Exception x){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "获取未分配数据异常。"), response);
				LogUtil.error(x, SKIP_Logger);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数。"), response);
		}
	}
	
	public String getWaitInfoSqlWhere(WebFormUtil wfu,UserInfo2 user,List plist) throws Exception{
		StringBuffer sw=new StringBuffer();
		if(plist==null){
			plist=new ArrayList();
		}
		String flowType=wfu.get("flowType");
		List slist=new ArrayList();
		if(flowType.length()>0){
			sw.append(" and flowType=?");
			plist.add(flowType);
		}
		List list=null;
		if(user.checkUserTextSymbolSecurity("ZKMDocDisponseFHTwo")){
			if( flowType.length()>0){
				list=FlowHelper.getSecurityFlowType(flowType, "ZKMDocDisponseFHTwo");
			}else{
				list=FlowHelper.getSecurityAllNode("ZKMDocDisponseFHTwo");
			}
			if(list!=null && list.size()>0){
				slist.addAll(list);
				list.clear();
			}
		}
		if(user.checkUserTextSymbolSecurity("ZKMDocDisponseFHOne")){
			if(flowType.length()>0){
				list=FlowHelper.getSecurityFlowType(flowType, "ZKMDocDisponseFHOne");
			}else{
				list=FlowHelper.getSecurityAllNode("ZKMDocDisponseFHOne");
			}
			if(list!=null && list.size()>0){
				slist.addAll(list);
				list.clear();
			}
		}
		if(slist!=null && slist.size()>0){
			sw.append(" and ( ");
			for(int i=0;i<slist.size();i++){
				Map map=(Map)slist.get(i);
				String nodeType=map.get("type")==null?"":(String)map.get("type");
				if(nodeType.length()>0){
					if(i+1>=slist.size()){
						sw.append(" flowNode=?)");
						plist.add(nodeType);
					}else{
						sw.append(" flowNode=? or ");
						plist.add(nodeType);
					}
				}
			}	
		}else{
			sw.append(" and 1=2 ");
		}
		sw.append(" and disponseUser = ?");
		plist.add("");
		return sw.toString();
	}
	
	public final String [] DISPONSE_SEARCH_TERMS_FZJG={"wangDianMingCheng:like","diZhiJiYouBian:like","sheng:like","shi:like","quyu:like","flowArae:=","ziXunDianHua:like","yeWuShouLiFanWei:like","jiGouMiaoShu:like","quyu:like"};
	public final String DISPONSE_SEARCH_FIELDS_FZJG="id,wangDianMingCheng,wangDianChengYongMing,jiaoHuanHao,zhuangTai,diZhiJiYouBian,yengYeShiJian,zhouMoJiaErYingYeShiJian,ziXunDianHua,POS_WeiHu,yeWuShouLiFanWei,jiGouMiaoShu,sheng,shi,quyu,jiGou,jiGouBianHao,jiGouShuXing,beiZhu,relFlowId,isDel,flowArae,stopDate,stopUser";
	public final String table_FZJG="zkmPublicInfoFZJG";
	
	public final String [] DISPONSE_SEARCH_TERMS_LCCP={"chanPinBianHao:like","chanPinMingCheng:like","biZhong:like","xiaoShouErQi:like","xiaoShouQuDao:like"};
	public final String DISPONSE_SEARCH_FIELDS_LCCP="id,chanPinBianHao,chanPinMingCheng,biZhong,tianShu,yuQiNianHuaShouYi,qiShouJinE,xiaoShouQuDao,fengXian,xiaoShouErQi,qiShouRi,tingShouRi,qiXiEr,daoQiEr,xiaoShouDiQu,fileName,filePath,relFlowId,isDel,flowArae,isStop,stopDate,stopUser";
	public final String table_LCCP="zkmPublicInfoLCCP";
	public final String LCCP_TS="";
	public final String LCCP_GQ="";
	
	public final String [] DISPONSE_SEARCH_TERMS_YXHD={"huoDongMingCheng:like","kaShiShiJian:like","jieShuShiJian:like","yingXiaoLeiBie:like","huoDongNeiRong:like"};
	public final String DISPONSE_SEARCH_FIELDS_YXHD="id,huoDongMingCheng,kaShiShiJian,jieShuShiJian,houDongDuanXin,duanXinFaShongShiJian,yingXiaoLeiBie,huoDongDuiXiang,huoDongNeiRong,houDongDiDian,fileName,filePath,relFlowId,isDel,flowArae,isStop,stopDate,stopUser";
	public final String table_YXHD="zkmPublicInfoYXHD";
	
	public final String [] DISPONSE_SEARCH_TERMS_ZZSB={"jiQiHao:like","zhuangTai:like","sheBeiGongNeng:like","sheng:like","shi:like","quYu:like","znZhuangDiZhi:like","flowArae:="};
	public final String DISPONSE_SEARCH_FIELDS_ZZSB="id,jiQiHao,zhuangTai,anZhuangDiDian,znZhuangDiZhi,sheBeiGongNeng,shiYongShiJian,yingYeShiJianDianHua,feiYingYeDianHua,zhuanGuanYuan,zhuanGuanDianHua,guanJiHangMingCheng,quKaWangDian,beiZhu,sheng,shi,quYu,relFlowId,isDel,flowArae,stopDate,stopUser";
	public final String table_ZZSB="zkmPublicInfoZZSB";
	
	public final String [] DISPONSE_SEARCH_TERMS_CJWT={"problem:like","answer:like"};
	public final String DISPONSE_SEARCH_FIELDS_CJWT="id,problem,answer,createTime,folder,cNum,relFlowId,isDel,flowArae,isStop,stopDate,stopUser";
	public final String table_CJWT="zkmPublicInfoCJWT";
	
	public void dataTableDoSearch(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		long d1=new Date().getTime();
		WebFormUtil wfu=new WebFormUtil(request);
		String sType=wfu.get("sType");
		String sflag=wfu.get("sflag");
		if(sType.length()<=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "查询失败，缺少必要参数。"), response);
			return;
		}
		String where="";
		if(sflag.length()<=0){
			where=" and isStop=0 ";
		}else if(sflag.equals("stop")){
			where=" and isStop=1 ";
		}
		if("FZJG".equals(sType)){
			postStrToClient( JSONSerializer.toJSON(commonSearch(DISPONSE_SEARCH_TERMS_FZJG, DISPONSE_SEARCH_FIELDS_FZJG, table_FZJG,where, request)).toString(), response);
		}else if("LCCP".equals(sType)){
			String lccpType=wfu.get("lccpType");
			String now=DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
			if(lccpType.equals("ZS")){
				where = where + " and '"+ now +"'  >= concat(qiShouRi,' 00:00:00') and '" + now + "' <= concat(tingShouRi,' 23:59:59')";
			}else if(lccpType.equals("TS")){
				where = where + " and '"+ now +"'  >= concat(date_add(tingShouRi, interval 1 day),' 00:00:00') and '" + now + "' <= concat(daoQiEr,' 23:59:59')";
			}else if(lccpType.equals("GQ")){
				where = where + " and '"+ now +"'  >= concat(date_add(daoQiEr,  interval 3 month),' 23:59:59')";
			}
			postStrToClient( JSONSerializer.toJSON(commonSearch(DISPONSE_SEARCH_TERMS_LCCP, DISPONSE_SEARCH_FIELDS_LCCP, table_LCCP,where, request)).toString(), response);
		}else if("YXHD".equals(sType)){
			postStrToClient( JSONSerializer.toJSON(commonSearch(DISPONSE_SEARCH_TERMS_YXHD, DISPONSE_SEARCH_FIELDS_YXHD, table_YXHD,where, request)).toString(), response);
		}else if("ZZSB".equals(sType)){
			long ds1=new Date().getTime();
			Map map=commonSearch(DISPONSE_SEARCH_TERMS_ZZSB, DISPONSE_SEARCH_FIELDS_ZZSB, table_ZZSB,where, request);
			long ds2=new Date().getTime();
			LogUtil.info("------ database time : --------------" + (ds2-ds1),SKIP_Logger);
			long dj1=new Date().getTime();
			String json=JSONSerializer.toJSON(map).toString();
			System.out.println("----------- json len : --------------" + json.length());
			long dj2=new Date().getTime();
			LogUtil.info("------ json convert time : -------------- " + (dj2-dj1),SKIP_Logger);
			long dp1=new Date().getTime();
			postStrToClient( json, response);
			long dp2=new Date().getTime();
			LogUtil.info("------ post json time : ------------------ " + (dp2-dp1),SKIP_Logger);
		}else if("CJWT".equals(sType)){
			postStrToClient( JSONSerializer.toJSON(commonSearch(DISPONSE_SEARCH_TERMS_CJWT, DISPONSE_SEARCH_FIELDS_CJWT, table_CJWT,where, request)).toString(), response);
		}else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "数据查询类型：" + sType + " 未定义。"), response);
			return;
		}
		long d2=new Date().getTime();
		LogUtil.info("---------------run time ------------- : " + (d2-d1),SKIP_Logger);
	}
	
	public final String[] FZJG_MH_FIELD={"wangDianMingCheng","wangDianChengYongMing","diZhiJiYouBian","ziXunDianHua","yeWuShouLiFanWei","jiGouMiaoShu","sheng","shi","quyu","jiGou","jiGouBianHao","beiZhu"};
	
	public final String[] ZZSB_MH_FIELD={"jiQiHao","zhuangTai","anZhuangDiDian","znZhuangDiZhi","sheBeiGongNeng","shiYongShiJian","yingYeShiJianDianHua","feiYingYeDianHua","zhuanGuanYuan","zhuanGuanDianHua","guanJiHangMingCheng","quKaWangDian","beiZhu","sheng","shi","quYu"};
	
	public void dataTableDoSearchMh(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		long d1=new Date().getTime();
		WebFormUtil wfu=new WebFormUtil(request);
		String condition=wfu.get("condition");
		String sType=wfu.get("sType");
		String sflag=wfu.get("sflag");
		if(sType.length()<=0 || condition.length()<=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "查询失败，缺少必要参数。"), response);
			return;
		}
		String where="";
		if(sflag.length()<=0){
			where=" where isStop=0 ";
		}else if(sflag.equals("stop")){
			where=" where isStop=1 ";
		}
		List plist=new ArrayList();
		List dlist=new ArrayList();
		String sql="";
		if("FZJG".equals(sType)){
			plist=new ArrayList();
			where =spellSearchMhWhere(condition,FZJG_MH_FIELD,where,plist);
			sql="select " + DISPONSE_SEARCH_FIELDS_FZJG + " from " + table_FZJG + where ;
			dlist=DAOTools.queryMap(sql, plist, zkmDBId);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",dlist), response);
		}else if("ZZSB".equals(sType)){
			plist=new ArrayList();
			where =spellSearchMhWhere(condition,ZZSB_MH_FIELD,where,plist);
			sql="select " + DISPONSE_SEARCH_FIELDS_ZZSB + " from " + table_ZZSB + where ;
			dlist=DAOTools.queryMap(sql, plist, zkmDBId);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",dlist), response);
		}else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "数据查询类型：" + sType + " 未定义。"), response);
			return;
		}
		long d2=new Date().getTime();
		LogUtil.info("---------------run time ------------- : " + (d2-d1),SKIP_Logger);
	}
	
	public String spellSearchMhWhere(String condition,String[] fields,String where,List plist){
		if(condition.length()>0){
			String v=condition.trim().replaceAll("\\*", "%");
			StringBuffer sb=new StringBuffer();
			sb.append(" and (");
			for(int i=0;i<fields.length;i++){
				String f=fields[i];
				if(i==0){
					sb.append(f + " like ?");
				}else{
					sb.append(" or " + f + " like ?");
				}
				plist.add(v);
			}
			sb.append(" )");
			return where + sb.toString();
		}else{
			return "";
		}
	}
	
	public void zkmPublicInfoDisponseDelete(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String id=wfu.get("id");
		if(id.length()>0){
			try{
				String sql="delete from zkmPublicInfoDisponse where id=?";
				DAOTools.updateForPrepared(sql, new String[]{id}, zkmDBId);
				//记录操作日志
				ZkmSysDBLogService log2db=new ZkmSysDBLogService();
				log2db.sysZkmOperationLogSave(id, user.loginName2, "zkmPublicFlowDel", "删除流程中的公共信息数据", "删除",request);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作已成功。"), response);
			}catch(Exception x){
				LogUtil.error("-- public info delete error : " , SKIP_Logger);
				LogUtil.error(x, SKIP_Logger);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "删除操作由于异常失败。"), response);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数。"), response);
		}
	}
	
	public void publicInfoStop(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String stype=wfu.get("stype");
		String tableType=wfu.get("tableType");
		String id=wfu.get("id");
		String sql="";
		//记录操作日志的对象
		ZkmSysDBLogService log2db=new ZkmSysDBLogService();
		String content="";
		String operateType="";
		String disponseType="";
		if(stype.length()>0 && id.length()>0 && tableType.length()>0){
			if(tableType.equals("FZJG")){
				tableType="zkmPublicInfoFZJG";
				content="分支机构";
				operateType="fzjgStop";
			}else if(tableType.equals("LCCP")){
				tableType="zkmPublicInfoLCCP";
				content="理财产品";
				operateType="lccpStop";
			}else if(tableType.equals("YXHD")){
				tableType="zkmPublicInfoYXHD";
				content="营销活动";
				operateType="yxhdStop";
			}else if(tableType.equals("ZZSB")){
				tableType="zkmPublicInfoZZSB";
				content="自助设备";
				operateType="zzsbStop";
			}else if(tableType.equals("CJWT")){
				tableType="zkmPublicInfoCJWT";
				content="常见问题";
				operateType="cjwtStop";
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "非法参数。"), response);
				return;
			}
			sql="update " + tableType + " set isStop=1,stopDate=now(),stopUser=? where ";
			if(stype.equals("batch")){
				sql+=" relFlowId = ?";
				content="批量停用," + content;
				disponseType="批量停用";
			}else if(stype.equals("sing")){
				sql +=" id=?";
				content="停用," + content;
				disponseType="停用";
			}else if(stype.equals("lccpStopSing")){
				content="停用," + content;
				sql +=" id=?";
				String[] ids=id.split(",");
				for(String id_:ids){
					try{
						DAOTools.updateForPrepared(sql,new String[]{user.loginName2,id_},zkmDBId);
						//记录操作日志
						log2db.sysZkmOperationLogSave(id_, user.loginName2, "lccpStopSing", content, "停用",request);
					}catch(Exception x){
						LogUtil.error("--- zkm public info  lccpStopSing stop error: " , SKIP_Logger);
						LogUtil.error(x, SKIP_Logger);
					}
				}
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "停用操作已完成。"), response);
				return;
			}else if(stype.equals("lccpStopAll")){
				content="停用过期数据," + content;
				String now=DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
				sql="update zkmPublicInfoLCCP set isStop=1,stopDate=now(),stopUser=? where " +
						"'"+ now +"'  >= concat(date_add(daoQiEr,  interval 3 month),' 23:59:59') and isStop = 0";
				try{
					DAOTools.updateForPrepared(sql,new String[]{user.loginName2},zkmDBId);
					//记录操作日志
					log2db.sysZkmOperationLogSave("", user.loginName2, "lccpStopAll", content, "停用过期数据",request);
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "停用操作成功。"), response);
				}catch(Exception x){
					LogUtil.error("--- zkm public info stop error: " , SKIP_Logger);
					LogUtil.error(x, SKIP_Logger);
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "停用由于异常失败。"), response);
				}
				return;
			}
			try{
				DAOTools.updateForPrepared(sql,new String[]{user.loginName2,id},zkmDBId);
				//记录操作日志
				log2db.sysZkmOperationLogSave(id, user.loginName2, operateType, content, disponseType,request);
				if(stype.equals("batch")){
					//复核报表停用
					ZZkmRecordClickUtils.zkmFHStop(id, user.loginName2,user.loginName2,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMPUBLICINFO,request);
					//维护报表停用
					ZZkmRecordClickUtils.insertClickRecord(ZZkmRecordClickUtils.INFO_TYPE_PUBLIC_INFO,id,"whatToDo","stop","zkmOperater",user.loginName2,request);
				}
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "停用操作成功。"), response);
			}catch(Exception x){
				LogUtil.error("--- zkm public info stop error: " , SKIP_Logger);
				LogUtil.error(x, SKIP_Logger);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "停用由于异常失败。"), response);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数。"), response);
		}
	}
	
	public void publicInfoUNStop(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String tableType=wfu.get("tableType");
		String ids=wfu.get("ids");
		String sql="";
		//记录操作日志的对象
		ZkmSysDBLogService log2db=new ZkmSysDBLogService();
		String content="";
		String operateType="";
		String disponseType="";
		if(ids.length()>0 && tableType.length()>0){
			if(tableType.equals("FZJG")){
				tableType="zkmPublicInfoFZJG";
				content="分支机构，恢复停用";
				operateType="fzjgUNStop";
			}else if(tableType.equals("LCCP")){
				tableType="zkmPublicInfoLCCP";
				content="理财产品，恢复停用";
				operateType="lccpUNStop";
			}else if(tableType.equals("YXHD")){
				tableType="zkmPublicInfoYXHD";
				content="营销活动，恢复停用";
				operateType="yxhdUNStop";
			}else if(tableType.equals("ZZSB")){
				tableType="zkmPublicInfoZZSB";
				content="自助设备，恢复停用";
				operateType="zzsbUNStop";
			}else if(tableType.equals("CJWT")){
				tableType="zkmPublicInfoCJWT";
				content="常见问题，恢复停用";
				operateType="cjwtUNStop";
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "非法参数。"), response);
				return;
			}
			sql="update " + tableType + " set isStop=0,stopDate=now(),stopUser=? where id=?";
			String[] ids_=ids.split(",");
			for(String id:ids_){
				try{
					DAOTools.updateForPrepared(sql,new String[]{user.loginName2,id},zkmDBId);
					//记录操作日志
					log2db.sysZkmOperationLogSave(id, user.loginName2, operateType, content, "恢复停用",request);
				}catch(Exception x){
					LogUtil.error("--- zkm public info unstop error: " , SKIP_Logger);
					LogUtil.error(x, SKIP_Logger);
				}
			}
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "恢复操作已完成。"), response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数。"), response);
		}
	}
	
	public void getPublicInfoCJWTListForFolder(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String pid=wfu.get("pid");
		String sql="SELECT id,problem,answer,createTime,folder,cNum,relFlowId,isDel,flowArae,isStop,stopDate,stopUser FROM zkmPublicInfoCJWT " +
				"where folder like ?";
		if(pid.length()>0){
			pid="%" + pid + "%";
			try{
				List list=DAOTools.queryMap(sql,new String []{pid},zkmDBId);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",list), response);
			}catch(Exception x){
				LogUtil.error("-- public info CJWTListForFolder error : " , SKIP_Logger);
				LogUtil.error(x, SKIP_Logger);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "获取数据由于异常失败。"), response);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "查询失败，缺少必要参数。"), response);
		}
	}
	
	public void setPublicInfoCJWTCNum(HttpServletRequest request, HttpServletResponse response) throws Exception{
		WebFormUtil wfu=new WebFormUtil(request);
		String id=wfu.get("id");
		if(id.length()>0){
			String sql="update zkmPublicInfoCJWT set cNum=(cNum+1) where id=?";
			try{
				DAOTools.updateForPrepared(sql, new String[]{id}, zkmDBId);
			}catch(Exception x){
				LogUtil.error("-- public info CJWTListForFolder update CNum error : " , SKIP_Logger);
				LogUtil.error(x, SKIP_Logger);
			}
		}
	}
	
	public void getCJWTFolderNames(HttpServletRequest request, HttpServletResponse response) throws Exception{
		WebFormUtil wfu=new WebFormUtil(request);
		String ids_=wfu.get("ids");
		List names=new ArrayList();
		if(ids_.length()>0){
			String sql="select text from zkmInfoBase where id=?";
			String[] ids=ids_.split(",");
			for(String id:ids){
				try{
					List list=DAOTools.queryMap(sql,  new String[]{id}, zkmDBId);
					if(list.size()>0){
						Map map=(Map)list.get(0);
						String name=(String)map.get("text");
						if(name.length()>0){
							names.add(name);
						}
					}
				}catch(Exception x){
					LogUtil.error("-- public info CJWTListForFolder getCJWTFolderNames error : " ,SKIP_Logger);
					LogUtil.error(x, SKIP_Logger);
					break;
				}
			}
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",names), response);
		}
	}
	
	public static void main(String[] args) throws Exception{
		/*File file=new File("E:/testImp/[中华针灸学].项平.王玲玲.扫描版.zip");
		Map map=(Map)FileUtils.parserFile(file);
		Map mdt=(Map)map.get(FileUtils.FILE_METADATA);
		String mt=(String)mdt.get("Content-Type");
		System.out.println("-----：" + mt);*/
		
		ZkmPublicInfoFlowDisponse zpif=new ZkmPublicInfoFlowDisponse();
		zpif.unZipToTempFolder("E:/testImp/[中华针灸学].项平.王玲玲.扫描版.zip");
	}
}
