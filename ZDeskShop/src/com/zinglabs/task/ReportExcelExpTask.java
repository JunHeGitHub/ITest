package com.zinglabs.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;
import com.zinglabs.tools.ExcelTools;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.SysUtils;
import com.zinglabs.util.ZKMAPPUtils;
import com.zinglabs.util.commonTask.CommonTask;
import com.zinglabs.util.commonTask.TaskDutyUtils;
import com.zinglabs.util.commonTask.imp.CommonTaskImp;
import com.zinglabs.util.excelUtils.ExcelHelper;
import com.zinglabs.util.excelUtils.model.CreateExcelModel;

public class ReportExcelExpTask implements CommonTaskImp{
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk");
	//数据标识
	public String taskDbid="ZDesk";
	
	public String task_mgs="执行excel生成任务";
	//每天1点执行
	public static String runTime="01:00:00";
	//任务类型基本常量
	public static final String DUTY_BASE_NAME="ZDESK_EXCEL_TASK";
	//任务类型
	public static String defaultDutyType="";
	/**
	 * 是否执行
	 * @return
	 */
	public static synchronized boolean isDoExcelExportDuty(String dutyType){
		String rt=ConfInfo.confMapDBConf.get("ZDESK_EXCEL_EXP_TASK_TIME");
		if(rt!=null && rt.length()>0 && !"null".equals(rt.toLowerCase())){
			runTime=rt;
		}
		DateUtil du=new DateUtil();
		String nowTime=du.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
		String[] dt=nowTime.split(" ");
		String t1=dt[0] + runTime;
		long lnow=TaskDutyUtils.convertDateTimeStr2Long(nowTime);
		long lrun=TaskDutyUtils.convertDateTimeStr2Long(t1);
		if(lrun<=lnow){
			//未被运行过则返回true
			if(!TaskDutyUtils.dutyIsRunDay(dutyType,dt[0],null)){
				//先设置执行日志，否则在执行过程中还可能被执行。
       		 	TaskDutyUtils.setDutyRuned(dutyType, "");
       		 	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void doWork() {
		boolean hasdt=false;
		if(defaultDutyType.length()<=0){
			hasdt=genDutyType();
		}else{
			hasdt=true;
		}
		if(hasdt){
			LogUtil.debug("excel生成任务......" + defaultDutyType, SKIP_Logger);
			if(isDoExcelExportDuty(defaultDutyType)){
				List dlist=getExcelExportList();
				if(dlist!=null && dlist.size()>0){
					for(int i=0;i<dlist.size();i++){
						Map map=(Map)dlist.get(i);
						try{
							doExcelExportWork(map);
							//导出成功后，重置最后执行时间
							String id=(String)map.get("id");
							String sql="update Z_excelTaskDefined set lastRunTime=now() where id='" + id +"'";
							DAOTools.updateForPrepared(sql, taskDbid);
							LogUtil.debug("excel生成任务......" + defaultDutyType + " " + id + " excel数据导出成功。", SKIP_Logger);
						}catch(Exception x){
							LogUtil.error(x, SKIP_Logger);
							LogUtil.error("excel生成任务......" + defaultDutyType + " 异常 ： " + map.get("id"), SKIP_Logger);
						}
					}
				}
				LogUtil.debug("excel生成任务......" + defaultDutyType + " 执行完毕。", SKIP_Logger);
			}else{
				LogUtil.debug("excel生成任务......" + defaultDutyType + " 未满足执行条件。", SKIP_Logger);
			}
		}
	}
	
	@Override
	public String getTaskMgs() {
		if(defaultDutyType.length()<=0){
			genDutyType();
		}
		return task_mgs;
	}
	
	public boolean doExcelExportWork(Map<String,String> map) throws Exception{
		boolean r=true;
		String sql=map.get("sql")==null?"":map.get("sql");
		String sqlParam=map.get("sqlParam")==null?"":map.get("sqlParam");
		String excelTitles=map.get("excelTitles")==null?"":map.get("excelTitles");
		//xml方式exportFields 中的定义无效，需要将exportFields写到sql中
		String exportFields=map.get("exportFields")==null?"":map.get("exportFields");
		String dbId=map.get("dbId")==null?"":map.get("dbId");
		String type=map.get("type")==null?"":map.get("type");
		String excelSaveRootPath=map.get("excelSaveRootPath")==null?"":map.get("excelSaveRootPath");
		if(sql.length()<=0 || excelTitles.length()<=0 || dbId.length()<=0 || type.length()<=0 || excelSaveRootPath.length()<=0 || exportFields.length()<=0){
			LogUtil.error("excel生成任务......" + defaultDutyType + " 配置参数不完整中止执行 " + map.get("id"), SKIP_Logger);
			return false;
		}
		String[] fields=exportFields.split(",");
		String[] cells=excelTitles.split(",");
		if(fields.length != cells.length){
			LogUtil.error("excel生成任务......" + defaultDutyType + " 定义参数列与列头数量不一致，中止执行 " + map.get("id"), SKIP_Logger);
			return false;
		}
		//-----------------------------jxl方式
		//List list=null;
		//if(sqlParam.length()>0){
		//	list=DAOTools.queryMap(sql, sqlParam.split(","), dbId);
		//}else{
		//	list=DAOTools.queryMap(sql, dbId);
		//}
		//LogUtil.debug("execSql -------------- :" + sql, SKIP_Logger);
		//LogUtil.debug("execParam -------------- :" + sqlParam, SKIP_Logger);
		//----------------------------jxl方式 END
		
		//----------------------------xml方式
		
		LogUtil.debug("execSql -------------- :" + sql, SKIP_Logger);
		LogUtil.debug("execParam -------------- :" + sqlParam, SKIP_Logger);
		
		sql=genExcelSql(sql,sqlParam);
		
		LogUtil.debug("execGenSql -------------- :" + sql, SKIP_Logger);
		//---------------------------xml方式 END
		
		File bfile=new File(excelSaveRootPath);
		if(!bfile.exists() || !bfile.isDirectory()){
			bfile.mkdirs();
			ZKMAPPUtils.dirChangeUserAndGroup(bfile,false);
		}
		String fd=DateUtil.getLocalDefineDate("yyyy-MM-dd");
		fd=DateUtil.subDay(fd, -1);
		String fn=DateUtil.getFormatDateStrToNumerStr(fd);
		bfile=genExcelName(bfile,fn,0);
		
		//-------------------------jxl方式
		//if(list==null || list.size()<=0){
		//	LogUtil.error("excel生成任务......" + defaultDutyType + " 获取报表数据为 0 " + map.get("id"), SKIP_Logger);
		//	bfile.createNewFile();
		//}else{
		//	//生成excel
		//	exportHandle(bfile,"export",list,fields,cells);
		//	ZKMAPPUtils.fileChangeUserAndGroup(bfile);
		//}
		//--------------------------jxl方式 END
		
		//----------------------------xml方式
		if(excelTitles.length()>0){
			excelTitles.replaceAll(",", "@");
		}else if(exportFields.length()>0){
			exportFields.replaceAll(",", "@");
		}else{
			LogUtil.error("execType -------------- :" + type + " attrNames defined error.", SKIP_Logger);
			return false;
		}
		HashMap<String, String> paramap=new HashMap<String, String>();
		paramap.put("fileNameAndPath",bfile.getPath());
		//paramap.put("fileName",fileName);
		paramap.put("fTitle","export");
		paramap.put("attrNames",excelTitles);
		paramap.put("sql",sql);
		paramap.put("dbID",dbId);	
		paramap.put("statistical","");
		int len=ExcelTools.sql2ExcelFile(paramap);
		
		ZKMAPPUtils.fileChangeUserAndGroup(bfile);
		//---------------------------xml方式 END
		
		return r;
	}
	
	/**
	 * 拼装sql语句
	 * @param sql
	 * @param sqlParam
	 * @return
	 */
	public String genExcelSql(String sql,String sqlParam){
		if(sql!=null && sql.length()>0){
			String[] p=sqlParam.split(",");
			for(String tp:p){
				if(p.length>0){
					tp.replaceAll("'", "''");
					if(sql.indexOf('?')>0){
						sql=sql.replaceFirst("\\?", "'" + tp + "'");
					}else{
						LogUtil.error("execParam -------------- sql :" + sql, SKIP_Logger);
						LogUtil.error("execParam -------------- sqlParam :" + sqlParam, SKIP_Logger);
						LogUtil.error("execParam -------------- 参数定义错误。", SKIP_Logger);
						return "";
					}
				}
			}
			return sql;
		}
		return "";
	}
	
	/**
	 * 导出excel
	 * @param file
	 * @param sheetName
	 * @param datalist
	 * @param fields
	 * @param fieldNames
	 * @throws Exception
	 */
	public static void exportHandle(File file, String sheetName, List datalist, String[] fields, String[] fieldNames) throws Exception {
		if (datalist != null && datalist.size() > 0 && fields != null && fields.length > 0) {
			List edl = new ArrayList();
			for (int i = 0; i < datalist.size(); i++) {
				Map dm = (HashMap) datalist.get(i);
				if (dm != null) {
					String[] data = new String[fields.length];
					for (int k = 0; k < fields.length; k++) {
						String field = fields[k];
						Object vo=dm.get(field);
						String vl ="";
						if(vo!=null){
							if(vo instanceof String){
								vl = (String)vo;
							}else{
								vl=String.valueOf(vo);
							}
						}
						data[k] = vl;
					}
					edl.add(data);
				}
			}
			if (edl.size() > 0) {
				CreateExcelModel cem = new CreateExcelModel();
				OutputStream os=new FileOutputStream(file);
				cem.setOs(os);
				if(fieldNames!=null && fieldNames.length>0){
					cem.setFristRow(fieldNames);
				}
				cem.setRowDataList(edl);
				cem.setSheetName("exportData");
				ExcelHelper.makeExcel(cem);
			}
		}
	}
	/**
	 * 生成excel文件名称
	 * @param dirName
	 * @param fileName
	 * @param i
	 * @return
	 */
	public File genExcelName(File dirName,String fileName,int i){
		String path=dirName.getPath() + "/" + fileName + ".xls";
		File file=new File(path);
		//if(file.exists() && file.isFile()){
		//	file=genExcelName(dirName,fileName,i++);
		//}
		if(file.exists() && file.isFile()){
			file.delete();
		}
		return file;
	}
	
	/**
	 * 获取要执行excel导出工作的列表
	 * @return
	 */
	public List getExcelExportList(){
		LogUtil.debug("获取excel生成任务，执行列表获取......", SKIP_Logger);
		String sql="select * from Z_excelTaskDefined where (select (TO_DAYS(now())-TO_DAYS(`lastRunTime`))) >=runCycle";
		List rlist=null;
		try {
			rlist=DAOTools.queryMap(sql, taskDbid);
			if(rlist!=null && rlist.size()>0){
				for(int i=rlist.size()-1;i>=0;i--){
					Map rm=(Map)rlist.get(i);
					String mip=rm.get("runnerIp")==null?"":(String)rm.get("runnerIp");
					if(mip.length()>0){
						if(!SysUtils.ipMatched(mip)){
							rlist.remove(i);
						}
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error(e, SKIP_Logger);
			LogUtil.error("获取excel生成任务，执行列表获取......失败，任务中止。", SKIP_Logger);
		}
		if(rlist!=null){
			LogUtil.debug("获取excel生成任务，执行列表获取......" + rlist.size(), SKIP_Logger);
		}
		return rlist;
	}
	
	/**
	 * 生成任务标识
	 * @return
	 */
	public synchronized boolean genDutyType(){
		LogUtil.debug("获取excel生成任务，任务标识......", SKIP_Logger);
		try {
			List ips=SysUtils.getLocalInetAddresses();
			if(ips!=null && ips.size()>0){
				String sql="select * from DataItemAllocation where peizhiItemValue=? and bItem='database'";
				for(int i=0;i<ips.size();i++){
					String ip=(String)ips.get(i);
					if(ip.length()>0){
						List rlist=DAOTools.queryMap(sql, new String[]{ip}, taskDbid);
						if(rlist!=null && rlist.size()>0){
							Map rmap=(Map)rlist.get(0);
							String p1=(String)rmap.get("peizhiItemValue");
							defaultDutyType=DUTY_BASE_NAME + "_" +ip;
							task_mgs +=":" + ip;
							LogUtil.debug("获取excel生成任务，任务标识......" + defaultDutyType, SKIP_Logger);
							LogUtil.debug("获取excel生成任务，任务标识......" + task_mgs, SKIP_Logger);
							break;
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			LogUtil.error(e, SKIP_Logger);
			LogUtil.error("获取excel生成任务，任务标识......失败，任务中止。", SKIP_Logger);
		}
		return false;
	}
	
	public static void main(String [] args) throws Exception{
		//DAOTools.initAllStatic();
		//DAOTools.hasInitStatic=true;
		ReportExcelExpTask reet=new ReportExcelExpTask();
		//CommonTask ct=new CommonTask(reet,1000,0);
		//Thread t=new Thread(ct);
		//t.start();
		//reet.doWork();
		
		String sql="select * from abc where a=? and b=? and c=?";
		String sqlp="111,222,333";
		System.out.println(reet.genExcelSql(sql,sqlp));
	}
}
