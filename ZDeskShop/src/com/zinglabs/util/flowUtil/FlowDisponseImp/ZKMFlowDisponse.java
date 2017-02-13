package com.zinglabs.util.flowUtil.FlowDisponseImp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.db.ZZkmRecordClickUtils;
import com.zinglabs.servlet.ZKMDocServlet;
import com.zinglabs.servlet.ZkmHistoryHandle;
import com.zinglabs.servlet.modules.ZkmCommons;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.FileUtils;
import com.zinglabs.util.RandomGUID;
import com.zinglabs.util.SysUtils;
import com.zinglabs.util.ZKMAPPUtils;
import com.zinglabs.util.ZKMConfs;
import com.zinglabs.util.DataRelationComponent.DataRelationComponent;
import com.zinglabs.util.flowUtil.FlowDisponse;

public class ZKMFlowDisponse implements FlowDisponse{
	public static  Logger logger = LoggerFactory.getLogger("ZKM");
	public static final String[] zkmDocStateDefined={"起草","已发布","停用"};
	public static final String[] approveStateDefined={"待审批","通过","拒绝","发布","暂存","停用"};
	public static final String[] applyTypeDDefined={"新建","修订","版本恢复"};
	public static final String[] historyTypeDefined={"flowHistory","zkmInfoHistory"};
	public String dbid=ZKMDocServlet.zkmDBId;
	public String[] disponse_update_fields={"appointedCheckUserID","zkmDocState","disponseUser","disponseBTime","appointedCheckUser","approveState","SMSSendPhone","flowNode","disponseETime","flowID"};
	public String[] history_insert_fields={"id","title","bDate","eDate","createUser","createTime","area","versions","hotTag","keywords","keywords1","keywords2","keywords3","keywords4","keywords5","summary","ToSMS","ToEmail","notTrue","notTimely","notNormally","changReason","disponseOpinion","checkOneOpinion","checkTowOpinion","basilicType","zkmDocState","applyType","approveState","appointedCheckUser","disponseUser","disponseBTime","disponseETime","flowNode","flowType","securityUserId","securityUser","securityFolderID","securityFolderName","SMSSendPhone","contentPath","relID","flowID","historyType","companyId","companyName","departmentId","departmentName","lastModiTime","lastModiUser","notTrueBZ","notTimelyBZ","notNormallyBZ"};
	public String sql_history_del="delete from zkmInfoBaseDisponseHistory where id=?";
	public String sql_disponse_update="update zkmInfoBaseDisponse set appointedCheckUserID=?, zkmDocState=?, disponseUser=?,disponseBTime =?,appointedCheckUser=?,approveState=?,SMSSendPhone=?,flowNode=?,disponseETime=?,flowID=? where id=?";
	
	public String sql_clearData_delInfo="delete from zkmInfoBaseDisponseFB where id=?";
	public String sql_clearData_delFile="delete from zkmInfoFile where relationId=?";
	public String sql_clearData_delSecurityUser="delete from Z_OrgZKMDocMap where dataId=?";
	
	public String sql_putOut_insertBaseFB="insert into zkmInfoBaseDisponseFB select * from zkmInfoBaseDisponse where id=?";
	public String sql_putOut_insertBaseFB_contentPath="update zkmInfoBaseDisponseFB set contentPath=? where id=?";
	public String sql_putOut_upModiTime="update zkmInfoBaseDisponse set lastModiTime=now() where id=?";
	
	public String sql_putOut_insertFile="INSERT INTO `zkmInfoFile` (id,`fileName`,`relName`,`fileType`,`filePath`,`createTime`,`createUser`,`isdel`,`delTime`,`delUser`,`fileUse`,`relationId`) " +
										" select UUID(),`fileName`,`relName`,`fileType`,`filePath`,`createTime`,`createUser`,`isdel`,`delTime`,`delUser`,`fileUse`,? from zkmInfoFile where relationId=?";
	
	public <T> Map disponseFirst(Map node, Map data, T params) {
		if(data!=null){
			String flowId= new RandomGUID().toString();
			data.put("flowID",flowId);
			fixPostData(data);
			Map hmap=(Map)((HashMap)data).clone();
			//处理数据
			nextDataDisponse(data,hmap,node);
			//更新数据库
			return disponseDataToDB(data,hmap);
		}else{
			return genReturnMap(false,"数据为空。",null);
		}
	}

	public <T> Map disponseEnd(Map node, Map data, T params) {
		//更新对应目录关系，更新附件，更新正文，更新数据项，保存历史
		fixPostData(data);
		String ndid=data.get("id")==null?"":(String)data.get("id");
		String folderIds=data.get("securityFolderID")==null?"":(String)data.get("securityFolderID");
		String userIds=data.get("securityUserId")==null?"":(String)data.get("securityUserId");
		String applyType=data.get("applyType")==null?"":(String)data.get("applyType");
		String companyId=data.get("companyId")==null?"":(String)data.get("companyId");
		String departmentId=data.get("departmentId")==null?"":(String)data.get("departmentId");
		if(folderIds.length()<=0){
			return genReturnMap(false,"未设置发布目录",null);
		}
		if(userIds.length()<=0){
			return genReturnMap(false,"未设置发布授权",null);
		}
		Map dataBak=(Map)((HashMap)data).clone();
		if(ndid.length()>0){
			Map hmap=(Map)((HashMap)data).clone();
			nextDataDisponse(data,hmap,node);
			hmap.put("approveState",approveStateDefined[3]);
			data.put("flowNode", "");
			data.put("zkmDocState",zkmDocStateDefined[1]);
			data.put("disponseUser","");
			data.put("disponseBTime", "0000-00-00 00:00:00");
			data.put("disponseETime", "0000-00-00 00:00:00");
			Map rm=disponseDataToDB(data,hmap);
			//获取已发布数据列表
			if(((Boolean)rm.get("success")).booleanValue()){
				List list=new ArrayList();
				try{
					list=DataRelationComponent.getRelation(ndid, ZkmCommons.ZKM_FB_RLEATION_KEY);
				}catch(Exception x){
					logger.error(x.getMessage());
					try{
						rollBackHistory(hmap);
						updateDisponseData(data);
					}catch(Exception xx){
						logger.error(" flow rollback error :------" + x.getMessage());
					}
					return genReturnMap(false,"发布失败，获取发布关系异常失败",null);
				}
				//TODO:这里不再做数据库异常时的处理，还是用数据库事务比较好。
				if(list!=null && list.size()>0){
					//保存历史版本
					saveHistory(ndid);
					//清理数据关联
					try {
						DataRelationComponent.delRelationSrcId(ndid, ZkmCommons.ZKM_FB_RLEATION_KEY);
						clearData(ndid);
					} catch (Exception e) {
						logger.error("---- error flow clear relations " + " : " + e.getMessage());
						e.printStackTrace();
					}
				}
				
				Map mm=putOutDoc(userIds,folderIds,data);
				if(!((Boolean)mm.get("success")).booleanValue()){
					try{
						dataBak.put("disponseETime", "0000-00-00 00:00:00");
						updateDisponseData(dataBak);
						String delhistory="delete from zkmInfoBaseDisponseHistory where id=?";
						DAOTools.updateForPrepared(delhistory, new String[]{(String)hmap.get("id")}, dbid);
					}catch(Exception x){
						logger.error("---- error flow roll bak put out doc" + "  :" +x.getMessage());
					}
				}else{
					String loginName=data.get("createUser")==null?"":(String)data.get("createUser");
					//记录文档新建/修订统计数据
					if(applyType.equals(applyTypeDDefined[0])){
						//新建
						ZZkmRecordClickUtils.zkmDoAddRecord(ndid,folderIds,loginName,companyId,departmentId);
					}else if(applyType.equals(applyTypeDDefined[1])){
						//修订
						ZZkmRecordClickUtils.zkmDoModiRecord(ndid,folderIds,loginName,companyId,departmentId);
					}
					ZZkmRecordClickUtils.zkmJBCreateDocRecord(ndid,loginName,loginName,companyId,departmentId);
				}
				return mm;
			}else{
				return rm;
			}
		}else{
			return genReturnMap(false,"未找到有效数据。",null);
		}
	}

	public boolean disponseEnd_toReFB(Map data) {
		//更新对应目录关系，更新附件，更新正文，更新数据项，保存历史
		fixPostData(data);
		String ndid=data.get("id")==null?"":(String)data.get("id");
		String folderIds=data.get("securityFolderID")==null?"":(String)data.get("securityFolderID");
		String userIds=data.get("securityUserId")==null?"":(String)data.get("securityUserId");
		if(folderIds.length()<=0){
			return false;
		}
		if(userIds.length()<=0){
			return false;
		}
		if(ndid.length()>0){
			//清理数据关联
			try {
				DataRelationComponent.delRelationSrcId(ndid, ZkmCommons.ZKM_FB_RLEATION_KEY);
				clearData(ndid);
			} catch (Exception e) {
				return false;
			}
			Map mm=putOutDoc(userIds,folderIds,data);
			if(((Boolean)mm.get("success")).booleanValue()){
				return true;
			}
			return false;
		}else{
			return false;
		}
	}
	
	public Map putOutDoc(String users,String folders,Map data){
		//发数据、发附件
		String srcid=data.get("id")==null?"":(String)data.get("id");
		List mgsList=new ArrayList();
		int successint=0;
		int total=0;
		if(folders!=null && folders.length()>0){
			List idlist=new ArrayList();
			String folderNames=data.get("securityFolderName")==null?"":(String)data.get("securityFolderName");
			String[] fnames=folderNames.split(",");
			String [] pids=folders.split(",");
			total=pids.length;
			boolean herror=false;
			for(int k=0;k<pids.length;k++){
				String pid=pids[k];
				Map pmap=getZKMInfoData(pid);
				if(pmap==null){
					logger.debug("error ------ continue parent Data is null.");
					mgsList.add(genReturnMap(false,"未找到发布知识目录,“"+ fnames[k] +"”",null));
					herror=true;
					break;
				}
				//设置关联
				try{
					DataRelationComponent.setRelation(srcid,pid,ZkmCommons.ZKM_FB_RLEATION_KEY,false);
				}catch(Exception x){
					logger.error(" folw out put doc setRelation error :------" + pid + "  : " + x.getMessage());
					mgsList.add(genReturnMap(false,"知识发布数据关联设置异常。“"+ fnames[k] +"”" ,null));
					herror=true;
					break;
				}
			}
			//数据
			if(!herror){
				try{
					String[] p={srcid};
					DAOTools.updateForPrepared(sql_putOut_upModiTime, p, dbid);
					DAOTools.updateForPrepared(sql_putOut_insertBaseFB, p, dbid);
				}catch(Exception x){
					logger.error(" folw out put doc infoBase error :------" + srcid + " :" +x.getMessage());
					mgsList.add(genReturnMap(false,"知识发布数据项发布异常失败。" ,null));
					herror=true;
				}
			}
			if(!herror){
				//正文文件
				String cpath=data.get("contentPath")==null?"":(String)data.get("contentPath");
				File file=FileUtils.createFolderUseSystemDate(ZKMConfs.confs.getProperty("zkmDocSaveDir","/usr/local/tomcat/webapps/ZDesk/ZKM/zkmDocs"));
				if(!file.exists() && file.isDirectory() && file.canWrite()){
					file.mkdirs();
				}
				ZKMAPPUtils.fcdirChangeUserAndGroup(file);
				boolean isCreate=false;
				if(cpath.length()>0){
					File cf=new File(cpath);
					if(cf.exists() && cf.isFile() && cf.canRead()){
						cf=new File(cf.getParent());
						File tfile=ZKMAPPUtils.copyToTempFolder(cf);
						if(tfile.exists()&& tfile.isDirectory() && tfile.canRead()){
							File rf=new File(file.getPath() +"/" +tfile.getName());
							try{
								ZKMAPPUtils.doCommonShellMove(tfile.getPath(), file.getPath());
								//org.apache.commons.io.FileUtils.copyDirectory(cf, rf);
								//-------------变更上传目录的文件linux所属用户/组
								ZKMAPPUtils.dirChangeUserAndGroup(rf, true);
								String [] p={rf.getPath() + "/index.html",srcid};
								DAOTools.updateForPrepared(sql_putOut_insertBaseFB_contentPath, p, dbid);
							}catch(Exception x){
								logger.error("---- flow copy content file error ---:" + x.getMessage());
								isCreate=false;
								mgsList.add(genReturnMap(false,"知识发布正文发布异常失败。" ,null));
								herror=true;
							}
							isCreate=true;
						}
					}
					if(!isCreate){
						file=new File(file.getPath() + "/index.html");
						try{
							FileUtils.appendToFile("", file, false);
						}catch(Exception x){
							logger.error("putOutDoc -- error: appendToFile error "  + x.getMessage());
						}
					}
				}
				//授权
				if(!herror){
					if(users!=null && users.length()>0){
						String userNames=data.get("securityUser")==null?"":(String)data.get("securityUser");
						String[] names=userNames.split(",");
						String[] user=users.split(",");
						for(int i=0;i<user.length;i++){
							String uid=user[i];
							String uname=names[i];
							if(uid!=null&& uid.length()>0){
								try{
									ZkmCommons.zkmDocSecuritySingSet(uid, uname, srcid);
								}catch(Exception x){
									logger.error(" folw out put doc security set error :------userid：" + uid + " userName：" + uname + " infoData：" + srcid  + "  : "  + x.getMessage());
									mgsList.add(genReturnMap(false,"知识发布数据授权异常失败。" ,null));
									herror=true;
									break;
								}
							}
						}
					}
				}
				//建索引
				try{
					ZKMDocServlet.indexDocs(srcid,false);
				}catch(Exception x){
					logger.error(" folw out put doc index doc error :------" + x.getMessage());
					mgsList.add(genReturnMap(false,"知识发布数据全文索引异常失败。" ,null));
					herror=true;
				}
				mgsList.add(genReturnMap(false,"数据发布成功。－－" ,null));
			}
			if(herror){
				try{
					//清理数据关联
					DataRelationComponent.delRelationSrcId(srcid, ZkmCommons.ZKM_FB_RLEATION_KEY);
					clearData(srcid);
					mgsList.add(genReturnMap(false,"取消数据发布。" ,null));
				}catch(Exception x){
					logger.error(" folw out put doc rollbck doc error ." + x.getMessage());
					mgsList.add(genReturnMap(false,"取消数据发布异常失败。" ,null));
				}
				return genReturnMap(false,"流程提交失败",mgsList);
			}
		}
		return genReturnMap(true,"流程提交成功",mgsList);
	}
	
	
	public void saveHistory(String srcid){
		try{
			ZkmHistoryHandle.doSaveToHistoryDisponse(srcid,historyTypeDefined[1]);
		}catch(Exception x){
			logger.error("---- save history data error --- :" + x.getMessage());
		}
	}
	
	public void clearData(String did) throws Exception {
		//清理主数据,zkmInfoBase中不再存放数据
		DAOTools.updateForPrepared(sql_clearData_delInfo, new String[]{did},dbid);
		//清理附件数据
		//DAOTools.updateForPrepared(sql_clearData_delFile, new String[]{did},dbid);
		//清理权限数据
		DAOTools.updateForPrepared(sql_clearData_delSecurityUser, new String[]{did},dbid);
		//清理索引
		ZKMDocServlet.delIndexDocs(did);
	}
	
	public <T> Map disponseNext(Map node, Map data, T params) {
		if(data!=null){
			fixPostData(data);
			Map hmap=(Map)((HashMap)data).clone();
			//处理数据
			nextDataDisponse(data,hmap,node);
			//更新数据库
			return disponseDataToDB(data,hmap);
		}else{
			return genReturnMap(false,"数据为空。",null);
		}
	}
	
	public <T> Map disponsePrevious(Map node, Map data, T params) {
		
		return null;
	}

	
	public <T> Map disponseToFirst(Map node, Map data, T params) {
		//发布，知识库首页，修订，停用，历史
		//将当前处理用户设置为建立用户；节点为first节点
		if(data!=null){
			String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
			fixPostData(data);
			Map hmap=(Map)((HashMap)data).clone();
			//处理数据，node为首节点
			nextDataDisponse(data,hmap,node);
			//将当前处理人设定为建立者
			data.put("disponseUser",data.get("createUser"));
			data.put("disponseBTime",date);
			//更新数据库
			return disponseDataToDB(data,hmap);
		}else{
			return genReturnMap(false,"数据为空。",null);
		}
	}

	public <T> Map disponseToEnd(Map node, Map data, T params) {
		return null;
	}
	
	public <T> Map disponseToNode(Map node, Map data, T params) {
		return null;
	}
	
	public <T> Map genReturnMap(boolean success,String mgs,T obj){
		Map rm=new HashMap();
		rm.put("success", success);
		rm.put("mgs", mgs);
		rm.put("data", obj);
		return rm;
	}
	
	public Map disponseDataToDB(Map data,Map hdata){
		boolean ist=true;
		String mgs="提交成功";
		try{
			insertDisponseHistory(hdata);
		}catch(Exception x){
			ist=false;
			mgs="建立流程历史数据异常。";
			logger.error(x.getMessage());
		}
		if(ist){
			try{
				updateDisponseData(data);
			}catch(Exception x){
				rollBackHistory(hdata);
				ist=false;
				mgs="更新流程数据异常。";
				logger.error(x.getMessage());
			}
		}
		return genReturnMap(ist, mgs, null);
	}
	
	public void nextDataDisponse(Map data,Map hdata,Map node){
		String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
		String appointedCheckUserID=data.get("appointedCheckUserID")==null?"":((String)data.get("appointedCheckUserID")).trim();
		//指定复核人时的处理
		if(appointedCheckUserID.length()>0){
			//设置当前处理人
			data.put("disponseUser",appointedCheckUserID);
			//当前处理人接收时间
			data.put("disponseBTime", date);
		}else{
			data.put("disponseUser","");
			data.put("disponseBTime", "0000-00-00 00:00:00");
		}
		//初始化指定复核人
		data.put("appointedCheckUser", "");
		data.put("appointedCheckUserID", "");
		//初始化审核状态
		data.put("approveState", "");
		//初始化短信提醒
		data.put("SMSSendPhone", "");
		//设置当前数据节点
		data.put("flowNode", node.get("type"));
		data.put("disponseETime", "0000-00-00 00:00:00");
		//设置历史数据
		hdata.put("disponseETime", date);
		//设置历史数据ID
		String hid= new RandomGUID().toString();
		hdata.put("id",hid);
		hdata.put("historyType", historyTypeDefined[0]);
		//设置历史数据与当前数据的关联
		hdata.put("relID", data.get("id"));
	}
	
	public void fixPostData(Map data){
		String v="";
		v=data.get("eDate")==null?"":(String)data.get("eDate");
		if(v.length()<=0){
			v="0000-00-00 00:00:00";
			data.put("eDate", v);
		}
		v=data.get("bDate")==null?"":(String)data.get("bDate");
		if(v.length()<=0){
			v="0000-00-00 00:00:00";
			data.put("bDate", v);
		}
		v=data.get("disponseBTime")==null?"":(String)data.get("disponseBTime");
		if(v.length()<=0){
			v="0000-00-00 00:00:00";
			data.put("disponseBTime", v);
		}
		v=data.get("disponseETime")==null?"":(String)data.get("disponseETime");
		if(v.length()<=0){
			v="0000-00-00 00:00:00";
			data.put("disponseETime", v);
		}
		v=data.get("lastModiTime")==null?"":(String)data.get("lastModiTime");
		if(v.length()<=0){
			v="0000-00-00 00:00:00";
			data.put("lastModiTime", v);
		}
	}
	
	public void updateDisponseData(Map data) throws Exception{
		if(data!=null){
			String id=data.get("id")==null?"":(String)data.get("id");
			if(id.length()>0){
				List<String> plist=new ArrayList<String>();
				for(String f:disponse_update_fields){
					String v=data.get(f)==null?"":(String)data.get(f);
					plist.add(v);
				}
				plist.add(id);
				DAOTools.updateForPrepared(sql_disponse_update, plist, dbid);
			}else{
				logger.error("error updateDisponseData not find id");
			}
		}
	}
	
	public void insertDisponseHistory(Map data) throws Exception {
		if(data!=null){
			String fields="";
			String vask="";
			List<String> plist=new ArrayList<String>();
			for(int i=0;i<history_insert_fields.length;i++){
				String f=history_insert_fields[i];
				String v=data.get(f)==null?"":(String)data.get(f);
				if(("bDate".equals(f) || "eDate".equals(f) || "disponseETime".equals(f) 
						|| "createTime".equals(f) || "disponseBTime".equals(f) || "lastModiTime".equals(f)) && v.length()<=0){
					v="0000-00-00 00:00:00";
				}
				plist.add(v);
				if(i+1>=history_insert_fields.length){
					fields=fields + f;
					vask=vask + "?";
				}else{
					fields=fields + f + ",";
					vask=vask + "?,";
				}
			}
			String sql="insert into zkmInfoBaseDisponseHistory (" + fields +") values (" + vask + ")";
			logger.debug("history sql : " + sql );
			DAOTools.updateForPrepared(sql, plist, dbid);
		}
	}
	
	public void rollBackHistory(Map hdata){
		String id="";
		if(hdata!=null){
			id=hdata.get("id")==null?"":(String)hdata.get("id");
		}
		if(id!=null && id.length()>0){
			try{
				DAOTools.updateForPrepared(sql_history_del, new String[]{id},dbid);
			}catch(Exception x){
				logger.error(x.getMessage());
			}
		}
	}
	
	
	public void createZKMFolder(String id, Map pdata,Map<String,String> rmap) {
		String zkmRootPath = ZKMConfs.confs.getProperty("zkmDocSaveDir", "/mnt/zkmDoc");
		Map pmap = null;
		String path = "";
		String dbPath = "";
		if (pdata != null) {
			dbPath = pdata.get("filePath") == null ? "" : pdata.get("filePath").toString();
		}
		if (dbPath.length() > 0) {
			path = zkmRootPath + dbPath;
		} else {
			path = zkmRootPath;
		}
		path = path + "/" + id;
		dbPath = dbPath + "/" + id;
		try{
			File ff=FileUtils.createFolder(path, false);
			ZKMAPPUtils.fcdirChangeUserAndGroup(ff);
		}catch(Exception x){
			logger.error("-----flow createZKMFolder error --- :" + x.getMessage());
			path="";
			dbPath="";
		}
		rmap.put("dbpath", dbPath);
		rmap.put("relPath", path);
	}
	
	public Map<String,String> getZKMInfoData(String id){
		String sql="select `id`,`text`,`iconCls`,`cls`,`leaf`,`recordType`,`parentId`,`sortPath`,`filePath` from zkmInfoBase where id=?";
		Map rm=null;
		try{
			List list=DAOTools.queryMap(sql,new String[]{id},dbid);
			if(list!=null && list.size()>0){
				return (Map)list.get(0);
			}
		}catch(Exception x){
			logger.error("-----flow getZKMInfoData error --- :" +x.getMessage());
		}
		return rm;
	}

	public <T> Map disponseStop(Map node, Map data, T params) {
		String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
		//List list=DataRelationComponent.getRelation(sid, FlowDisponse.ZKM_FB_RLEATION_KEY);
		
		//sql="update zkmInfoBaseDisponse set zkmDocState=?,approveState=?,disponseUser=?,disponseBTime=? where id=?";
		//String[] p={"停用","停用",user.loginName,date,sid};
		//DAOTools.updateForPrepared(sql, p,zkmDBId);
		return null;
	}
}
