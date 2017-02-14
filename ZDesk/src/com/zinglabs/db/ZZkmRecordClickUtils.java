package com.zinglabs.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.log.LogUtil;
import com.zinglabs.servlet.ZKMDocServlet;
import com.zinglabs.util.WebFormUtil;

public class ZZkmRecordClickUtils {
	public static  Logger logger = LoggerFactory.getLogger("ZKM");
	//知识库
	public static final String INFO_TYPE_ZKM="ZKM";
	//公共信息
	public static final String INFO_TYPE_PUBLIC_INFO="PUBLIC_INFO";
	
	public static final String INFO_TYPE_ZKMFAQ="zkmFaq";
	
	public static final String ZKM_DF_TYPE_ZKMDOC="zkmDoc";
	
	public static final String ZKM_DF_TYPE_ZKMPUBLICINFO="publicInfo";
	
	public static void insertClickRecord(String infoType,String relId,String df,String dv,String dt,String user, HttpServletRequest request){
		String sql="insert into `zkmRecordClickInfo` (`infoType`,`relId`,`makeDate`,`dataField`,`dataValue`,`dataType`,`user`,companyId,departmentId) values (?,?,now(),?,?,?,?,?,?)";
		WebFormUtil wfu=new WebFormUtil(request);
		String companyId=wfu.get("companyId");
		String departmentId=wfu.get("departmentId");
		try{
			String [] param={infoType,relId,df,dv,dt,user,companyId,departmentId};
			DAOTools.updateForPrepared(sql, param, ZKMDocServlet.zkmDBId);
		}catch(Exception x){
			logger.error("insertClickRecord" + x.getMessage());
		}
	}
	
	public static void insertClickRecord(String infoType,String relId,String df,String dv,String dt,String user, String companyId,String departmentId){
		String sql="insert into `zkmRecordClickInfo` (`infoType`,`relId`,`makeDate`,`dataField`,`dataValue`,`dataType`,`user`,companyId,departmentId) values (?,?,now(),?,?,?,?,?,?)";
		try{
			String [] param={infoType,relId,df,dv,dt,user,companyId,departmentId};
			DAOTools.updateForPrepared(sql, param, ZKMDocServlet.zkmDBId);
		}catch(Exception x){
			logger.error("insertClickRecord" + x.getMessage());
		}
	}
	
	/**
	 * 知识库停用一条记录时，统计记录表+1
	 * infoType zkm
	 * dataType zkmOperater
	 * dataField whatToDo
	 * dataValue stop
	 */
	public static void zkmDoStopRecord(String dataId,String user, HttpServletRequest request){
		insertClickRecord(INFO_TYPE_ZKM,dataId,"whatToDo","stop","zkmOperater",user,request);
	}
	/**
	 * 知识库增加一条记录时，统计记录表+1
	 * infoType zkm
	 * dataType zkmOperater
	 * dataField whatToDo
	 * dataValue add
	 */
	public static void zkmDoAddRecord(String dataId,String folderIds,String user, HttpServletRequest request){
		insertClickRecord(INFO_TYPE_ZKM,dataId,"whatToDo","add","zkmOperater",user,request);
		//知识库增长统计数据
		if(folderIds!=null && folderIds.length()>0){
			String [] fids=folderIds.split(",");
			for(int i=0;i<fids.length;i++){
				insertClickRecord(INFO_TYPE_ZKM,dataId,"folder",fids[i],"zkmZhengZhangStaticData",user,request);
			}
		}
	}
	
	public static void zkmDoAddRecord(String dataId,String folderIds,String user, String companyId,String departmentId){
		insertClickRecord(INFO_TYPE_ZKM,dataId,"whatToDo","add","zkmOperater",user,companyId,departmentId);
		//知识库增长统计数据
		if(folderIds!=null && folderIds.length()>0){
			String [] fids=folderIds.split(",");
			for(int i=0;i<fids.length;i++){
				insertClickRecord(INFO_TYPE_ZKM,dataId,"folder",fids[i],"zkmZhengZhangStaticData",user,companyId,departmentId);
			}
		}
	}
	/**
	 * 知识库修改一条记录时，统计记录表+1
	 * infoType zkm
	 * dataType zkmOperater
	 * dataField whatToDo
	 * dataValue modi
	 */
	public static void zkmDoModiRecord(String dataId,String folderIds,String user, String companyId,String departmentId){
		insertClickRecord(INFO_TYPE_ZKM,dataId,"whatToDo","modi","zkmOperater",user,companyId,departmentId);
		//知识库更新统计数据
		if(folderIds!=null && folderIds.length()>0){
			String [] fids=folderIds.split(",");
			for(int i=0;i<fids.length;i++){
				insertClickRecord(INFO_TYPE_ZKM,dataId,"folder",fids[i],"zkmGengXinStaticData",user,companyId,departmentId);
			}
		}
	}
	
	public static void zkmDoModiRecord(String dataId,String folderIds,String user, HttpServletRequest request){
		insertClickRecord(INFO_TYPE_ZKM,dataId,"whatToDo","modi","zkmOperater",user,request);
		//知识库更新统计数据
		if(folderIds!=null && folderIds.length()>0){
			String [] fids=folderIds.split(",");
			for(int i=0;i<fids.length;i++){
				insertClickRecord(INFO_TYPE_ZKM,dataId,"folder",fids[i],"zkmGengXinStaticData",user,request);
			}
		}
	}
	
	/**
	 * 用于用户发布知识统计
	 * 记录经办员建立或修改的数据被发布的数量
	 * infoType zkm
	 * dataType zkmStatic_UserCreateModi
	 * dataField userName
	 * dataValue %userName%
	 * 
	 * @param dataId
	 */
	public static void zkmJBCreateDocRecord(String dataId,String userName,String user, HttpServletRequest request){
		insertClickRecord(INFO_TYPE_ZKM,dataId,"userName",userName,"zkmStatic_UserCreateModi",user,request);
	}
	
	public static void zkmJBCreateDocRecord(String dataId,String userName,String user, String companyId,String departmentId){
		insertClickRecord(INFO_TYPE_ZKM,dataId,"userName",userName,"zkmStatic_UserCreateModi",user,companyId,departmentId);
	}
	
	/**
	 * 经办统计数据 经办新增
	 * @param dataId
	 * @param userName
	 * 
	 * infoType zkm
	 * dataType zkmStatic_UserJB_Add
	 * dataValue %userName%
	 */
	public static void zkmJBAdd(String dataId,String userName,String user,String dataType, HttpServletRequest request){
		insertClickRecord(INFO_TYPE_ZKM,dataId,dataType,userName,"zkmStatic_UserJB_Add",user,request);
	}
	/**
	 * 经办统计数据 经办修订
	 * @param dataId
	 * @param userName
	 * 
	 * infoType zkm
	 * dataType zkmStatic_UserJB_Modi
	 * dataValue %userName%
	 */
	public static void zkmJBModi(String dataId,String userName,String user,String dataType, HttpServletRequest request){
		insertClickRecord(INFO_TYPE_ZKM,dataId,dataType,userName,"zkmStatic_UserJB_Modi",user,request);
	}
	
	/**
	 * 经办退回数据
	 * @param dataId
	 * @param userName
	 * 
	 * infoType zkm
	 * dataType zkmStatic_UserJB_Back
	 * dataValue %userName%
	 */
	public static void zkmJBToBack(String dataId,String userName,String user,String dataType, HttpServletRequest request){
		insertClickRecord(INFO_TYPE_ZKM,dataId,dataType,userName,"zkmStatic_UserJB_Back",user,request);
	}
	
	/**
	 * 复核统计数据 复核新建
	 * @param dataId
	 * @param userName
	 * 
	 * infoType zkm
	 * dataType zkmStatic_UserFH_Add
	 * dataValue %userName%
	 */
	public static void zkmFHAdd(String dataId,String userName,String user,String dataType, HttpServletRequest request){
		insertClickRecord(INFO_TYPE_ZKM,dataId,dataType,userName,"zkmStatic_UserFH_Add",user,request);
	}
	/**
	 * 复核统计数据 复核修订
	 * @param dataId
	 * @param userName
	 * 
	 * infoType zkm
	 * dataType zkmStatic_UserFH_Modi
	 * dataValue %userName%
	 */
	public static void zkmFHModi(String dataId,String userName,String user,String dataType, HttpServletRequest request){
		insertClickRecord(INFO_TYPE_ZKM,dataId,dataType,userName,"zkmStatic_UserFH_Modi",user,request);
	}
	/**
	 * 停用统计数据
	 * @param dataId
	 * @param userName
	 * 
	 * infoType zkm
	 * dataType zkmStatic_UserFH_Stop
	 * dataValue %userName%
	 */
	public static void zkmFHStop(String dataId,String userName,String user,String dataType, HttpServletRequest request){
		insertClickRecord(INFO_TYPE_ZKM,dataId,dataType,userName,"zkmStatic_UserFH_Stop",user,request);
	}
	/**
	 * 复核退回数据
	 * @param dataId
	 * @param userName
	 * 
	 * infoType zkm
	 * dataType zkmStatic_UserFH_Back
	 * dataValue %userName%
	 */
	public static void zkmFHToBack(String dataId,String userName,String user,String dataType, HttpServletRequest request){
		insertClickRecord(INFO_TYPE_ZKM,dataId,dataType,userName,"zkmStatic_UserFH_Back",user,request);
	}
	
	public static List getStaticSearch(String infoType,String df,String dv,String bdt,String edt,String groupField,String start,String number,String companyId) throws Exception{
		String sql="select `relId`,(select `title` from `zkmInfoBaseDisponse` where `zkmInfoBaseDisponse`.`id`=`zkmRecordClickInfo`.`relId`) title,count(1) num from `zkmRecordClickInfo` ";
		String gp="";
		String sw=" where 1=1 ";
		if(infoType!=null && infoType.length()>0 && groupField!=null && groupField.length()>0){
			 ArrayList<String> plist=new ArrayList();
			 gp="group by " + groupField + " order by num desc";
			 sw +=" and infoType=?";
			 plist.add(infoType);
			 if(df!=null){
				 sw +=" and dataField=?";
				 plist.add(df);
			 }
			 if(dv!=null){
				 sw +=" and dataValue=?";
				 plist.add(dv);
			 }
			 if(bdt!=null && bdt.length()>0 && edt!=null && edt.length()>0){
				 sw +=" and makeDate >= TIMESTAMP(?) and makeDate<= TIMESTAMP(?) ";
				 plist.add(bdt);
				 plist.add(edt);
			 }else if(bdt!=null && bdt.length()>0 && (edt==null || edt.length()<=0)){
				 sw +=" and makeDate >= TIMESTAMP(?) ";
				 plist.add(bdt);
			 }else if((bdt==null || bdt.length()<=0) && edt!=null && edt.length()>0){
				 sw +=" and makeDate<= TIMESTAMP(?) ";
				 plist.add(edt);
			 }
			 if(companyId!=null && companyId.length()>0){
				 sw +=" and companyId=? ";
				 plist.add(companyId);
			 }
			 if(start!=null && start.length()>0 && number!=null && number.length()>0){
				 sql=sql + sw +gp + " LIMIT " + start + "," + number;
			 }else{
				 sql=sql + sw +gp ;
			 }
			 List list=new ArrayList();
			 try{
				 list=DAOTools.queryMap(sql, plist, ZKMDocServlet.zkmDBId);
			 }catch (Exception x){
				 logger.error("getStaticSearch " + x.getMessage());
			 }
			 return list;
		}else{
			return new ArrayList();
		}
	}
	
	public static void zkmDocFolderClickSave(String docId,String parentId,String user, HttpServletRequest request){
		if(parentId.length()>0 && docId.length()>0 && user.length()>0){
			insertClickRecord(INFO_TYPE_ZKM, docId, "parentId", parentId, "f",user,request);
			/*String sql="select * from zkmInfoBase where isDel<> '1' and id='" + parentId  + "'";
			try{
				List list=DAOTools.queryMap(sql, ZKMDocServlet.zkmDBId);
				if(list!=null && list.size()>0){
					HashMap map=(HashMap)list.get(0);
					String pid=map.get("parentId")==null?"":(String)map.get("parentId");
					insertClickRecord(INFO_TYPE_ZKM, docId, "parentId", pid, "f",user);
				}
			}catch(Exception x){
				logger.error("zkmDocFolderClickSave " + x.getMessage());
			}
			*/
		}
	}
}
