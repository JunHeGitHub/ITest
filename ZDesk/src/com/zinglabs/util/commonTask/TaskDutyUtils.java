package com.zinglabs.util.commonTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zinglabs.db.DAOTools;
import com.zinglabs.util.RandomGUID;
import java.util.*;

public class TaskDutyUtils {
	public static  Logger logger = LoggerFactory.getLogger("ZDesk"); 
	public static String dbid="ZDesk";
	/**
	 * 在数据库中验证任务是否被执行过。
	 * 
	 * @param type	任务类型 
	 * @param date	执行日期 注意：日期格式必须是 yyyy-mm-dd 
	 * @param time	执行时间 注意：时间格式必须是 hh:mi:ss
	 * @return	执行过返回true，未执行过返回false
	 */
	public static boolean dutyIsRun(String type,String date,String dateFomat,String time){
		String sql="select id from Z_Task_Duty_log where type='"+ type +"' and DATE_FORMAT(runTime,'"+ dateFomat +"')= '" + date +"'";
//		if(time!=null && time.length()>0){
//			sql+=" and DATE_FORMAT(runTime,'%H:%I:%S') <= '" +  time +"'";
//		}
		logger.debug("duty sql : " + sql);
		try{
			List list=DAOTools.queryMap(sql, dbid);
			if(list.size()>0){
				return true;
			}else{
				return false;
			}
		}catch(Exception x){
			logger.error("error : 验证任务是否执行异常 ：" + x.getMessage());
			return true;
		}
	}
	
	public static boolean dutyIsRunYear(String type,String date,String time){
		return dutyIsRun(type,date,"%Y",time);
	}
	
	public static boolean dutyIsRunMonth(String type,String date,String time){
		return dutyIsRun(type,date,"%Y-%m",time);
	}
	
	public static boolean dutyIsRunDay(String type,String date,String time){
		return dutyIsRun(type,date,"%Y-%m-%d",time);
	}
	/**
	 * 设置任务执行数据
	 * @param type	任务类型
	 * @param mgs	任务执行消息
	 */
	public static void setDutyRuned(String type,String mgs){
		String sql="insert into `Z_Task_Duty_log` values (?,?,now(),?)";
		String id= new RandomGUID().toString();
		String[] p={id,type,mgs};
		logger.debug("duty sql : " + sql + " : " + id + " : " + type + " : " + mgs);
		try{
			DAOTools.updateForPrepared(sql, p, dbid);
		}catch(Exception x){
			logger.error("error : 设置任务执行状态异常 ：" + x.getMessage());
		}
	}
	
	/**
	 * 转换日期字符为长整型
	 * @param dateStr
	 * @return
	 */
	public static long convertDateTimeStr2Long(String dateStr){
		dateStr=dateStr.replaceAll("[-\\s:]","");
		return Long.valueOf(dateStr);
	}
}
