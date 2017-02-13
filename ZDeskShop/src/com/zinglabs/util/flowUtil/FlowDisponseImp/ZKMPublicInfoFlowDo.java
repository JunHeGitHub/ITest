package com.zinglabs.util.flowUtil.FlowDisponseImp;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.db.DAOTools;
import com.zinglabs.db.ZZkmRecordClickUtils;
import com.zinglabs.log.LogUtil;
import com.zinglabs.servlet.ZKMDocServlet;
import com.zinglabs.servlet.ZkmHistoryHandle;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.FileUtils;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;
import com.zinglabs.util.SysUtils;
import com.zinglabs.util.ZKMAPPUtils;
import com.zinglabs.util.ZKMConfs;
import com.zinglabs.util.DataRelationComponent.DataRelationComponent;
import com.zinglabs.util.excelUtils.ExcelPOIHelper;
import com.zinglabs.util.excelUtils.ExcelVaildateHelper;
import com.zinglabs.util.flowUtil.FlowDisponse;

public class ZKMPublicInfoFlowDo implements FlowDisponse{
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	public static final String[] approveStateDefined={"待审批","通过","拒绝","发布","暂存","停用"};
	public static final String[] applyTypeDDefined={"新建","修订","版本恢复"};
	public final String dtable="zkmPublicInfoDisponse";
	public final String htable="zkmPublicInfoDisponseHistory";
	public String dbid=ZKMDocServlet.zkmDBId;
	public String[] disponse_update_fields={"approveState","appointedCheckUserID","disponseUser","disponseBTime","appointedCheckUser","approveState","SMSSendPhone","flowNode","disponseETime","flowID"};
	public String[] history_insert_fields={"id","infoType","approveState","appointedCheckUser","title","area","createUser","createTime","FBTime","summary","disponseOpinion","checkOneOpinion","changeOpinion","flowType","flowNode","disponseUser","disponseBTime","disponseETime","flowID","relId","SMSSendPhone","filePath","fileName","notTrueBZ","notTimelyBZ","notNormallyBZ","companyId","companyName","departmentId","departmentName"};
	public String sql_history_del="delete from " + htable  +  " where id=?";
	public String sql_disponse_update="update "+ dtable +" set approveState=?,appointedCheckUserID=?, disponseUser=?,disponseBTime =?,appointedCheckUser=?,approveState=?,SMSSendPhone=?,flowNode=?,disponseETime=?,flowID=? where id=?";
	
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
		//根据infoType对数据进行处理，将excel中的数据入库，将压缩包中的文件与数据建立附件关系,生成入库日志。
		String id=(String)data.get("id");
		String infoType=(String)data.get("infoType");
		String filePath=(String)data.get("filePath");
		Map rmap=null;
		if(filePath.length()<=0){
			return genReturnMap(false,"数据不完整，缺少数据文件。",null);
		}
		if("分支机构调查表".equals(infoType)){
			rmap= disponseFZJG(data);
		}else if("理财产品一览表".equals(infoType)){
			rmap= disponseLCCP(data);
		}else if("营销活动汇总".equals(infoType)){
			rmap= disponseYXHD(data);
		}else if("自助设备信息调查表".equals(infoType)){
			rmap= disponseZZSB(data);
		}else if("常见问题".equals(infoType)){
			rmap= disponseCJWT(data);
		}else{
			rmap= genReturnMap(false,"未找到类型：“"+ infoType +"” 流程处理定义。",null);
		}
		if(((Boolean)rmap.get("success")).booleanValue()){
			Map hmap=(Map)((HashMap)data).clone();
			nextDataDisponse(data,hmap,node);
			hmap.put("approveState",approveStateDefined[3]);
			data.put("approveState",approveStateDefined[3]);
			data.put("flowNode", "");
			data.put("disponseUser","");
			data.put("disponseBTime", "0000-00-00 00:00:00");
			data.put("disponseETime", "0000-00-00 00:00:00");
			Map rm=disponseDataToDB(data,hmap);
			String loginName=data.get("createUser")==null?"":(String)data.get("createUser");
			String companyId=data.get("companyId")==null?"":(String)data.get("companyId");
			String departmentId=data.get("departmentId")==null?"":(String)data.get("departmentId");
			//维护报表统计记录,新增，公共信息只有新增
			ZZkmRecordClickUtils.insertClickRecord(ZZkmRecordClickUtils.INFO_TYPE_PUBLIC_INFO,id,"whatToDo","add","zkmOperater",loginName,companyId,departmentId);
		}
		return rmap;
	}
	
	public <T> Map disponseLCCP(Map data){
		List mgsList=new ArrayList();
		ExcelVaildateHelper evh=new ExcelVaildateHelper();
		//flowDataId
		String fid=(String)data.get("id");
		//数据文件
		String filePath=(String)data.get("filePath");
		Map fmap=unZipToTempFolder(filePath);
		File xlsFile=null;
		Map<String,File> tfmap=new HashMap<String,File>();
		if(!((Boolean)fmap.get("success")).booleanValue()){
			return genReturnMap(false,(String)fmap.get("mgs"),null);
		}else{
			File zf=(File)fmap.get("data");
			try{
				List<File> xfs=FileUtils.getFilesForFolder(zf,".xls",".xlsx");
				if(xfs.size()>0 && xfs.size()<2){
					xlsFile=xfs.get(0);
				}else{
					return genReturnMap(false,"ZIP压缩包中不包含excel文件或包含excel文件大于1.",null);
				}
				File[] tfs=zf.listFiles();
				for(File f:tfs){
					String name=f.getName();
	    			if(name.lastIndexOf(".")>0){
	    				String extName=name.substring(name.lastIndexOf("."));
	    				name=name.substring(0,name.lastIndexOf("."));
	    				extName=extName.trim().toUpperCase();
	    				if(!extName.equals(".XLS") && !extName.equals(".XLSX")){
	    					name=name.trim();
	    					tfmap.put(name, f);
	    				}	
	    			}
				}
			}catch(Exception x){
				LogUtil.error("对解压后目录进行文件识别发生异常：" ,SKIP_Logger);
				LogUtil.error(x, SKIP_Logger);
				return genReturnMap(false,"对解压后目录进行文件识别发生异常。",null);
			}
		}
		//类型
		String infoType=(String)data.get("infoType");
		int startRow=evh.definedExcel.get(infoType).size();
		ExcelPOIHelper eph=new ExcelPOIHelper(xlsFile);
		List<String[]> list=eph.getAllData(0);
		//文件存放路径
		String rootPath=ZKMConfs.confs.getProperty("zkmUploadFileSaveDir","/usr/local/tomcat/webapps/ZDesk/ZKM/uploads");
		File toFileBase=FileUtils.createFolderUseSystemDate(rootPath);
		ZKMAPPUtils.fcdirChangeUserAndGroup(toFileBase);
		String sql="INSERT INTO zkmPublicInfoLCCP (id,chanPinBianHao,chanPinMingCheng,biZhong,tianShu,yuQiNianHuaShouYi,qiShouJinE,xiaoShouQuDao,fengXian,qiShouRi,tingShouRi,qiXiEr,daoQiEr,xiaoShouDiQu,fileName,filePath,relFlowId,isDel,flowArae,isStop) values " +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con=null;
		try{
			con=DAOTools.getConnectionOutS(dbid);
		}catch(Exception x){
			LogUtil.error("自助设备发布异常：数据库异常：" ,SKIP_Logger);
			LogUtil.error(x, SKIP_Logger);
			return genReturnMap(false,"发布异常，数据库异常。",null);
		}
		if(con!=null){
			for(int i=startRow;i<list.size();i++){
				String id= new RandomGUID().toString();
				List p=new ArrayList();
				p.add(id);
				String[] cells=list.get(i);
				for(String str:cells){
					p.add(str.trim());
				}
				//获取产品名称,xls第二列
				String mc=cells[0];
				String fname="";
				String fpath="";
				if(mc.length()>0){
					mc=mc.trim();
					File file=tfmap.get(mc);
					if(file!=null && file.exists() && file.isFile() && file.canRead()){
						fname=file.getName();
						String gfn= new RandomGUID().toString();
						fpath=toFileBase.getPath() + "/" + gfn;
						try{
							File toFile=new File(fpath);
							org.apache.commons.io.FileUtils.copyFile(file, toFile);
							ZKMAPPUtils.fileChangeUserAndGroup(toFile);
						}catch(Exception x){
							fname="";
							fpath="";
							mgsList.add(evh.genMgsMaper(false, infoType, filePath, i, 0,"产品 " + mc + " 获取并设置相关文件异常：" +x.getMessage()));
							LogUtil.error("理财产品发布异常：行：" + i + " 获取并设置相关文件异常：" ,SKIP_Logger);
							LogUtil.error(x, SKIP_Logger);
						}
					}else{
						mgsList.add(evh.genMgsMaper(false, infoType, filePath, i, 0, "产品 " + mc + " 没有找到相关附加文件"));
					}
				}
				p.add(fname);
				p.add(fpath);
				p.add(fid);
				p.add("0");
				p.add("");
				p.add("0");
				try{
					DAOTools.updateForPrepared(sql, p, dbid,con,true,false);
				}catch(Exception x){
					mgsList.add(evh.genMgsMaper(false, infoType, filePath, i, 0, "产品 " + mc + " 数据库插入异常：" +x.getMessage()));
					LogUtil.error("理财产品发布异常：行：" + i + " 跳过" ,SKIP_Logger);
					LogUtil.error(x, SKIP_Logger);
				}
			}
			DAOTools.releaseConnectionOutS(dbid,con);
		}
		return genReturnMap(true,"发布完成。",mgsList);
	}
	
	public <T> Map disponseYXHD(Map data){
		List mgsList=new ArrayList();
		ExcelVaildateHelper evh=new ExcelVaildateHelper();
		//flowDataId
		String fid=(String)data.get("id");
		//数据文件
		String filePath=(String)data.get("filePath");
		Map fmap=unZipToTempFolder(filePath);
		File xlsFile=null;
		Map<String,File> tfmap=new HashMap<String,File>();
		if(!((Boolean)fmap.get("success")).booleanValue()){
			return genReturnMap(false,(String)fmap.get("mgs"),null);
		}else{
			File zf=(File)fmap.get("data");
			try{
				List<File> xfs=FileUtils.getFilesForFolder(zf,".xls",".xlsx");
				if(xfs.size()>0 && xfs.size()<2){
					xlsFile=xfs.get(0);
				}else{
					return genReturnMap(false,"ZIP压缩包中不包含excel文件或包包含excel文件大于1.",null);
				}
				File[] tfs=zf.listFiles();
				for(File f:tfs){
					String name=f.getName();
	    			if(name.lastIndexOf(".")>0){
	    				String extName=name.substring(name.lastIndexOf("."));
	    				name=name.substring(0,name.lastIndexOf("."));
	    				extName=extName.trim().toUpperCase();
	    				if(!extName.equals(".XLS") && !extName.equals(".XLSX")){
	    					name=name.trim();
	    					tfmap.put(name, f);
	    				}	
	    			}
				}
			}catch(Exception x){
				LogUtil.error("对解压后目录进行文件识别发生异常：" ,SKIP_Logger);
				LogUtil.error(x, SKIP_Logger);
				return genReturnMap(false,"对解压后目录进行文件识别发生异常。",null);
			}
		}
		//类型
		String infoType=(String)data.get("infoType");
		int startRow=evh.definedExcel.get(infoType).size();
		ExcelPOIHelper eph=new ExcelPOIHelper(xlsFile);
		List<String[]> list=eph.getAllData(0);
		//文件存放路径
		String rootPath=ZKMConfs.confs.getProperty("zkmUploadFileSaveDir","/usr/local/tomcat/webapps/ZDesk/ZKM/uploads");
		File toFileBase=FileUtils.createFolderUseSystemDate(rootPath);
		ZKMAPPUtils.fcdirChangeUserAndGroup(toFileBase);
		String sql="INSERT INTO zkmPublicInfoYXHD(id,huoDongMingCheng,kaShiShiJian,jieShuShiJian,houDongDuanXin,duanXinFaShongShiJian,yingXiaoLeiBie,huoDongDuiXiang,huoDongNeiRong,houDongDiDian,fileName,filePath,relFlowId,isDel,flowArae,isStop) values " +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con=null;
		try{
			con=DAOTools.getConnectionOutS(dbid);
		}catch(Exception x){
			LogUtil.error("自助设备发布异常：数据库异常：" ,SKIP_Logger);
			LogUtil.error(x, SKIP_Logger);
			return genReturnMap(false,"发布异常，数据库异常。",null);
		}
		if(con!=null){
			for(int i=startRow;i<list.size();i++){
				String id= new RandomGUID().toString();
				List p=new ArrayList();
				p.add(id);
				String[] cells=list.get(i);
				for(String str:cells){
					p.add(str.trim());
				}
				//获取活动名称,xls第一列
				String mc=cells[0];
				String fname="";
				String fpath="";
				if(mc.length()>0){
					mc=mc.trim();
					File file=tfmap.get(mc);
					if(file!=null && file.exists() && file.isFile() && file.canRead()){
						fname=file.getName();
						String gfn= new RandomGUID().toString();
						fpath=toFileBase.getPath() + "/" + gfn;
						try{
							File toFile=new File(fpath);
							org.apache.commons.io.FileUtils.copyFile(file, toFile);
							ZKMAPPUtils.fileChangeUserAndGroup(toFile);
						}catch(Exception x){
							fname="";
							fpath="";
							mgsList.add(evh.genMgsMaper(false, infoType, filePath, i, 0,"营销活动  " + mc + " 获取并设置相关文件异常：" +x.getMessage()));
							LogUtil.error("营销活动发布异常：行：" + i + " 获取并设置相关文件异常：" ,SKIP_Logger);
							LogUtil.error(x, SKIP_Logger);
						}
					}else{
						mgsList.add(evh.genMgsMaper(false, infoType, filePath, i, 0, "营销活动  " + mc + " 没有找到相关附加文件"));
					}
				}
				p.add(fname);
				p.add(fpath);
				p.add(fid);
				p.add("0");
				p.add("");
				p.add("0");
				try{
					DAOTools.updateForPrepared(sql, p, dbid,con,true,false);
				}catch(Exception x){
					mgsList.add(evh.genMgsMaper(false, infoType, filePath, i, 0, "营销活动 " + mc + " 数据库插入异常：" +x.getMessage()));
					LogUtil.error("营销活动发布异常：行：" + i + " 路过" ,SKIP_Logger);
					LogUtil.error(x, SKIP_Logger);
				}
			}
			DAOTools.releaseConnectionOutS(dbid,con);
		}
		return genReturnMap(true,"发布完成。",mgsList);
	}
	
	public <T> Map disponseZZSB(Map data){
		String area=(String)data.get("area");
		if(area.length()<=0){
			return genReturnMap(false,"缺少必要的地区数据。",null);
		}
		//清理数据
		String sql="delete from zkmPublicInfoZZSB where flowArae=?";
		try{
			DAOTools.updateForPrepared(sql, new String[]{area}, dbid);
		}catch(Exception x){
			LogUtil.error("自助设备发布异常：数据库异常：" ,SKIP_Logger);
			LogUtil.error(x, SKIP_Logger);
			return genReturnMap(false,"发布异常，数据库异常。",null);
		}
		List mgsList=new ArrayList();
		ExcelVaildateHelper evh=new ExcelVaildateHelper();
		//flowDataId
		String fid=(String)data.get("id");
		//数据文件
		String filePath=(String)data.get("filePath");
		//类型
		String infoType=(String)data.get("infoType");
		int startRow=evh.definedExcel.get(infoType).size();
		ExcelPOIHelper eph=new ExcelPOIHelper(filePath);
		List<String[]> list=eph.getAllData(0);
		sql="INSERT INTO zkmPublicInfoZZSB (id,jiQiHao,zhuangTai,anZhuangDiDian,znZhuangDiZhi,sheBeiGongNeng,shiYongShiJian,yingYeShiJianDianHua,feiYingYeDianHua,zhuanGuanYuan,zhuanGuanDianHua,guanJiHangMingCheng,quKaWangDian,beiZhu,sheng,shi,quYu,relFlowId,isDel,flowArae) values " +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con=null;
		try{
			con=DAOTools.getConnectionOutS(dbid);
		}catch(Exception x){
			LogUtil.error("自助设备发布异常：数据库异常：" ,SKIP_Logger);
			LogUtil.error(x, SKIP_Logger);
			return genReturnMap(false,"发布异常，数据库异常。",null);
		}
		if(con!=null){
			for(int i=startRow;i<list.size();i++){
				String id= new RandomGUID().toString();
				List p=new ArrayList();
				p.add(id);
				String[] cells=list.get(i);
				for(String str:cells){
					p.add(str);
				}
				p.add(fid);
				p.add("0");
				p.add(area);
				try{
					DAOTools.updateForPrepared(sql, p, dbid,con,true,false);
				}catch(Exception x){
					mgsList.add(evh.genMgsMaper(false, infoType, filePath, i, 0, "数据库插入异常：" +x.getMessage()));
					LogUtil.error("自助设备发布异常：行：" + i + " 路过" ,SKIP_Logger);
					LogUtil.error(x, SKIP_Logger);
				}
			}
			DAOTools.releaseConnectionOutS(dbid,con);
		}
		return genReturnMap(true,"发布完成。",mgsList);
	}
	
	public <T> Map disponseCJWT(Map data){
		List mgsList=new ArrayList();
		ExcelVaildateHelper evh=new ExcelVaildateHelper();
		//flowDataId
		String fid=(String)data.get("id");
		//数据文件
		String filePath=(String)data.get("filePath");
		//发布分类ID
		String fids=(String)data.get("CJWTTreeSelectIds");
		//类型
		String infoType=(String)data.get("infoType");
		int startRow=evh.definedExcel.get(infoType).size();
		ExcelPOIHelper eph=new ExcelPOIHelper(filePath);
		List<String[]> list=eph.getAllData(0);
		String sql="INSERT INTO zkmPublicInfoCJWT(id,problem,answer,createTime,folder,cNum,relFlowId,isDel)  values " +
				"(?,?,?,now(),?,?,?,?)";
		Connection con=null;
		try{
			con=DAOTools.getConnectionOutS(dbid);
		}catch(Exception x){
			LogUtil.error("自助设备发布异常：数据库异常：" ,SKIP_Logger);
			LogUtil.error(x, SKIP_Logger);
			return genReturnMap(false,"发布异常，数据库异常。",null);
		}
		if(con!=null){
			for(int i=startRow;i<list.size();i++){
				String id= new RandomGUID().toString();
				List p=new ArrayList();
				p.add(id);
				String[] cells=list.get(i);
				for(String str:cells){
					p.add(str);
				}
				p.add(fids);
				p.add("0");
				p.add(fid);
				p.add("0");
				try{
					DAOTools.updateForPrepared(sql, p, dbid,con,true,false);
				}catch(Exception x){
					mgsList.add(evh.genMgsMaper(false, infoType, filePath, i, 0, "数据库插入异常：" +x.getMessage()));
					LogUtil.error("常见问题异常：行：" + i + " 路过" ,SKIP_Logger);
					LogUtil.error(x, SKIP_Logger);
				}
			}
			DAOTools.releaseConnectionOutS(dbid,con);
		}
		return genReturnMap(true,"发布完成。",mgsList);
	}
	
	public <T> Map disponseFZJG(Map data){
		String area=(String)data.get("area");
		if(area.length()<=0){
			return genReturnMap(false,"缺少必要的地区数据。",null);
		}
		//清理数据
		String sql="delete from zkmPublicInfoFZJG where flowArae=?";
		try{
			DAOTools.updateForPrepared(sql, new String[]{area}, dbid);
		}catch(Exception x){
			LogUtil.error("分支机构发布异常：数据库异常：" ,SKIP_Logger);
			LogUtil.error(x, SKIP_Logger);
			return genReturnMap(false,"发布异常，数据库异常。",null);
		}
		List mgsList=new ArrayList();
		ExcelVaildateHelper evh=new ExcelVaildateHelper();
		//flowDataId
		String fid=(String)data.get("id");
		//数据文件
		String filePath=(String)data.get("filePath");
		//类型
		String infoType=(String)data.get("infoType");
		int startRow=evh.definedExcel.get(infoType).size();
		ExcelPOIHelper eph=new ExcelPOIHelper(filePath);
		List<String[]> list=eph.getAllData(0);
		sql="INSERT INTO zkmPublicInfoFZJG (id,wangDianMingCheng,wangDianChengYongMing,jiaoHuanHao,zhuangTai,diZhiJiYouBian,yengYeShiJian,zhouMoJiaErYingYeShiJian,ziXunDianHua,POS_WeiHu,yeWuShouLiFanWei,jiGouMiaoShu,sheng,shi,quyu,jiGou,jiGouBianHao,jiGouShuXing,beiZhu,relFlowId,isDel,flowArae) values " +
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con=null;
		try{
			con=DAOTools.getConnectionOutS(dbid);
		}catch(Exception x){
			LogUtil.error("自助设备发布异常：数据库异常：" ,SKIP_Logger);
			LogUtil.error(x, SKIP_Logger);
			return genReturnMap(false,"发布异常，数据库异常。",null);
		}
		if(con!=null){
			for(int i=startRow;i<list.size();i++){
				String id= new RandomGUID().toString();
				List p=new ArrayList();
				p.add(id);
				String[] cells=list.get(i);
				for(String str:cells){
					p.add(str);
				}
				p.add(fid);
				p.add("0");
				p.add(area);
				try{
					DAOTools.updateForPrepared(sql, p, dbid,con,true,false);
				}catch(Exception x){
					mgsList.add(evh.genMgsMaper(false, infoType, filePath, i, 0, "数据库插入异常：" +x.getMessage()));
					LogUtil.error("分支机构发布异常：行：" + i + " 路过" ,SKIP_Logger);
					LogUtil.error(x, SKIP_Logger);
				}
			}
			DAOTools.releaseConnectionOutS(dbid,con);
		}
		return genReturnMap(true,"发布完成。",mgsList);
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
			LogUtil.error(x,SKIP_Logger);
			LogUtil.error(x, SKIP_Logger);
		}
		if(ist){
			try{
				updateDisponseData(data);
			}catch(Exception x){
				rollBackHistory(hdata);
				ist=false;
				mgs="更新流程数据异常。";
				LogUtil.error(x,SKIP_Logger);
				LogUtil.error(x, SKIP_Logger);
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
		//data.put("approveState", "");
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
				LogUtil.error("error updateDisponseData not find id",SKIP_Logger);
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
				if(("bDate".equals(f) || "eDate".equals(f) || "disponseETime".equals(f) || "FBTime".equals(f)) && v.length()<=0){
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
			String sql="insert into " + htable +" (" + fields +") values (" + vask + ")";
			LogUtil.debug("history sql : " + sql,SKIP_Logger);
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
				LogUtil.error(x,SKIP_Logger);
				LogUtil.error(x, SKIP_Logger);
			}
		}
	}

	public <T> Map disponseStop(Map node, Map data, T params) {
		String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
		return null;
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
					LogUtil.error("解压文件－文件识别失败：" + zf.getPath() + " ： ",SKIP_Logger);
					LogUtil.error(x, SKIP_Logger);
				}*/
				String en=FileUtils.getFileExtendName(zf);
				if(en.length()>0 && en.toUpperCase().equals(".ZIP")){
					isZip=true;
				}
				if(isZip){
					String tmpDir=ZKMConfs.confs.getProperty("mergeTempDir","/mnt/zkm_temp_F");
					unFolder=FileUtils.createFolderUseSystemDate(tmpDir);
					ZKMAPPUtils.fcdirChangeUserAndGroup(unFolder);
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
	
	public static void main(String[] args){
		String rootPath=ZKMConfs.confs.getProperty("zkmUploadFileSaveDir","/usr/local/tomcat/webapps/ZDesk/ZKM/uploads");
		File toFileBase=FileUtils.createFolderUseSystemDate(rootPath);
		System.out.print(toFileBase.getPath());
	}
}
