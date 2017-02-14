package com.zinglabs.servlet.modules;
import com.zinglabs.db.DAOTools;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.servlet.ZKMDocServlet;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.StringUtils;
import com.zinglabs.util.DataRelationComponent.DataRelationComponent;
import com.zinglabs.util.flowUtil.DisponseBeans.DisponseBase;
import com.zinglabs.util.flowUtil.FlowDisponseImp.ZKMFlowDisponse;

public class ZkmCommons extends DisponseBase{
	public static  Logger logger = LoggerFactory.getLogger("ZKM"); 
	public static String dbid=ZKMDocServlet.zkmDBId;
	public static String sql_insertSecurityUser="INSERT INTO `Z_OrgZKMDocMap` (`subType`,`attrIndex`,`name`,`dataId`,`winName`,`generateTime`) values ('',?,?,?,?,now());";
	public static String sql_deleteSecurityUser="delete from Z_OrgZKMDocMap where dataId=?";
	public static String sql_loadDBConf="select peizhiItem,peizhiItemValue,productionId from DataItemAllocation a where (productionId like '%ZDesk%' or productionId like '%ZKM%' or productionId like '%ZQC%' ) and (peizhiItem='dbip' or peizhiItem='route') or peizhiItem='TTIP' or (productionId='z3000' and peizhiItem='dbip' ) or (productionId='openfire' and peizhiItem='dbip' ) or peizhiItem='ZDeskDOMAIN' or peizhiItem='ZKM_SERVER_IP'  or peizhiItem='ZKM_CLUSTER_OPEN'";
	public static final String ZKM_FB_RLEATION_KEY="zkmDisponsInfoRelation";
	public static Map<String,HashMap> rolePageAuthMap;
	
	
	public static void zkmDocSecuritySet(String userIds,String userNames,String docids) throws Exception{
		String[] uids=userIds.split(",");
		String[] unames=userNames.split(",");
		String[] dids=docids.split(",");
		zkmDocSecuritySet(uids,unames,dids);
	}
	
	public static List getSysDBConfs() throws Exception{
		return DAOTools.queryMap(sql_loadDBConf, dbid);
	}
	
	public static void zkmDocSecuritySet(String[] userIds,String[] userNames,String[] docids) throws Exception{
		if(userIds.length == userNames.length){
			if(docids.length>0){
				for(String id:docids){
					zkmDocSecuritySingDel(id);
					for(int i=0;i<userIds.length;i++){
						zkmDocSecuritySingSet(userIds[i],userNames[i],id);
					}
					try{
						String struid=StringUtils.arrayToCommaDelimitedString(userIds);
						String struname=StringUtils.arrayToCommaDelimitedString(userNames);
						zkmDocSecurityUpdateSrcDoc(struid,struname,id);
					}catch(Exception x){
						logger.error("---- zkmDocSecuritySet up srcdoc error : " + x.getMessage() + "  id : " + id);
					}
					
				}
			}
		}
	}
	
	public static void zkmDocSecuritySingSet(String userid,String username,String docid) throws Exception{
		String[] p={userid,username,docid,""};
		DAOTools.updateForPrepared(sql_insertSecurityUser, p, dbid);
	}
	
	public static void zkmDocSecuritySingDel(String docid) throws Exception {
		DAOTools.updateForPrepared(sql_deleteSecurityUser,new String[]{docid},dbid);
	}
	
	public static void zkmDocSecurityUpdateSrcDoc(String userIds,String userNames,String docid) throws Exception{
		String sql="update zkmInfoBaseDisponse set securityUserId=? , securityUser=? where id=?";
		List list=DataRelationComponent.getRelationForDistId(docid, ZKM_FB_RLEATION_KEY);
		if(list!=null && list.size()>0){
			Map map=(Map)list.get(0);
			if(map!=null){
				String sid=map.get("srcId")==null?"":(String)map.get("srcId");
				if(sid.length()>0){
					DAOTools.updateForPrepared(sql, new String[]{userIds,userNames,sid},dbid);
				}
			}
		}
	}
	
	public static List<String> getZkmFolderFiles(String fid) throws Exception{
		ArrayList<String> rlist=new ArrayList<String>();
		String sql="select id from zkmInfoBase where id='" + fid + "' and recordType='d'";
		List<HashMap> list=DAOTools.queryMap(sql, dbid);
		if(list!=null && list.size()>0){
			for(HashMap data:list){
				String id=(String)data.get("id");
				rlist.add(id);
			}
		}
		return rlist;
	}
	
	public static List<String> getZkmFolderFiles(String[] fids) throws Exception{
		ArrayList<String> alist=new ArrayList();
		for(String fid:fids){
			List tlist=getZkmFolderFiles(fid);
			if(tlist!=null && tlist.size()>0){
				alist.addAll(tlist);
			}
		}
		return alist;
	}
	
	public static boolean vaildateTitleForCF(String title,String applyType,String companyId) throws Exception{
		String newFlag=ZKMFlowDisponse.applyTypeDDefined[0];
		String modiFlag=ZKMFlowDisponse.applyTypeDDefined[1];
		String fbFlag=ZKMFlowDisponse.zkmDocStateDefined[1];
		List list=new ArrayList();
		String sql="select * from zkmInfoBaseDisponse where title=? and zkmDocState <> '停用' and zkmDocState <> '' and flowType <> '' and applyType <> ''";
		if(companyId.length()>0){
			sql+=" and companyId=? ";
			list=DAOTools.queryMap(sql, new String[]{title,companyId}, zkmDBId);
		}else{
			list=DAOTools.queryMap(sql, new String[]{title}, zkmDBId);
		}
		if(newFlag.equals(applyType)){
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Map map=(Map)list.get(i);
					String zkmDocState=map.get("zkmDocState")==null?"":(String)map.get("zkmDocState");
					if(fbFlag.equals(zkmDocState)){
						return true;
					}
				}
			}
		}
		if(modiFlag.equals(applyType)){
			if(list!=null && list.size()>1){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args){
		String a="abcd";
		String[] b=a.split(",");
		System.out.println(b.length);
	}
}
