package com.zinglabs.task;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.base.absImpl.BaseAbs;

import com.zinglabs.db.DAOTools;
//import com.zinglabs.util.LogUtil;
import com.zinglabs.util.TimeUtil;
import com.zinglabs.tools.Utility;

public class CalculateThread extends BaseAbs implements Runnable {

	public CalculateThread(){
		init(SKIP_Logger);
//		lock = new BusyFlag();
//		this.dao=new DAOTools();
	}
		
	public void run() {
		myThread=Thread.currentThread();
		long nowTime=System.currentTimeMillis();
		long sleepTime=0L;
		while (flag) {
			nowTime=System.currentTimeMillis();
			try {
				Thread.sleep(3000L);
			} catch (InterruptedException e) {
				error("CalculateThread run");
				error(e);
			}
			info("date check begin");
			try {
				if(!isRelease)
					dateCheck();
				else
					cleanAndStop();
			} catch (Exception e) {
				error("during date check");
				error(e);
			}
			info("date check end");
			
//			try {
//				sleepTime=BaseData.QC_RECORD_INTERVAL - (System.currentTimeMillis() - nowTime);
//				LogUtil.warn("Thread SyncRecordThread sleep : "+sleepTime);
//				Thread.sleep(sleepTime>0?sleepTime:2000);
//			} catch (InterruptedException e) {
//				LogUtil.warn("exception in SyncRecordThread run");
//				LogUtil.warn(e.toString());
//			}
		}
	}
	
	
	public void dateCheck(){
//		Timestamp timeNow=new Timestamp(System.currentTimeMillis());
		Timestamp weekTime=null,monthTime=null,nTime=null,weekEndTime=null,weekBeginTime=null,monthBegin=null,monthEnd=null,nowCalTime=null,dayEndTime=null,dayBeginTime=null;
		int type;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatH =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String sql="select max(end_time) as endT,type,now() as nTime FROM `VT_REPORT_END_TIME` group by dbKey , type  ";
		String sql="select max(end_time) as endT,type,now() as nTime ,dbKey FROM `VT_ZQC_REPORT_END_TIME` group by dbKey , type  ";
		ArrayList<String> sqlList=new ArrayList<String>();
		StringBuffer sqlWeek=new StringBuffer();
		StringBuffer sqlMonth=new StringBuffer();
		ArrayList<Object[]> al = null;
		Object[] resTmp = null;
		String dbKey=null;
		try {
			debug(sql);
//			al =dao.execSelect(sql,"endTimeDb");
			al=DAOTools.execSelectS(sql, "ZQC");
			for(int t=0;t<al.size();t++){
				resTmp=al.get(t);
				type=Integer.parseInt(resTmp[1].toString());
				debug(type+"-----"+getTimestamp(resTmp[0], format));
				if(type==GRANULARITY_WEEK){
					weekTime=getTimestamp(resTmp[0], format);
					nTime=getTimestamp(resTmp[2], formatH);
					dbKey=String.valueOf(resTmp[3]);
					if(nTime.getTime()-weekTime.getTime()>86400000l*7l ){
//						weekBeginTime=new Timestamp(weekTime.getTime()+86400000l*7l);
						weekBeginTime=new Timestamp(weekTime.getTime());
						weekEndTime=TimeUtil.calculateTime(weekBeginTime, GRANULARITY_WEEK, CALCULATE_ADD, 1L);
//						if(nowCalTime==null || weekEndTime.getTime()>nowCalTime.getTime()+86400000l)continue;
//						weekEndTime=new Timestamp(weekBeginTime.getTime()+86400000l*7l);
						debug(weekTime+"--------------"+weekBeginTime);
						if(genZQCReport(weekBeginTime, weekEndTime, GRANULARITY_WEEK,dbKey)){
//						if(dao.updateSum(weekBeginTime, weekEndTime, type)){
							sqlWeek=new StringBuffer();
							sqlWeek.append("INSERT INTO VT_ZQC_REPORT_END_TIME");
							sqlWeek.append("(end_time,type,generate_time,dbKey)");
							sqlWeek.append("VALUES");
							sqlWeek.append("(");
							sqlWeek.append("'");
							sqlWeek.append(format.format(weekEndTime));
							sqlWeek.append("'");
							sqlWeek.append(",");
							sqlWeek.append(GRANULARITY_WEEK);
							sqlWeek.append(",");
//							sqlWeek.append("'");
							sqlWeek.append("now()");
							sqlWeek.append(",");
							sqlWeek.append("'");
							sqlWeek.append(dbKey);
							sqlWeek.append("'");
							sqlWeek.append(")");
							debug(sqlWeek.toString());
							sqlList.add(sqlWeek.toString());
						}
						
					}
//					if(weekTime.get){}
				}else if(type==GRANULARITY_MONTH){
					
//					monthTime=getTimestamp(resTmp[0], format);
//					nTime=getTimestamp(resTmp[2], formatH);
//					debug(nTime+"--------Month----"+monthTime+"---------"+nowCalTime);
//					if(nTime.getTime()>monthTime.getTime() && nTime.getMonth()!=monthTime.getMonth() && nowCalTime.getTime()>monthTime.getTime() && monthTime.getMonth()!=nowCalTime.getMonth()){
////						weekBeginTime=new Timestamp(weekTime.getTime()+86400000l*7l);
//						monthBegin=new Timestamp(monthTime.getTime());
//						monthEnd=TimeUtil.calculateTime(monthBegin, GRANULARITY_MONTH, CALCULATE_ADD, 1L);
//						if(nowCalTime==null || monthEnd.getTime()>nowCalTime.getTime()+86400000l)continue;
//						debug(monthBegin+"--------------"+monthEnd);
//						if(dao.updateSum(monthBegin, monthEnd, type)){
//							sqlWeek=new StringBuffer();
//							sqlWeek.append("INSERT INTO VT_REPORT_END_TIME");
//							sqlWeek.append("(end_time,type,generate_time)");
//							sqlWeek.append("VALUES");
//							sqlWeek.append("(");
//							sqlWeek.append("'");
//							sqlWeek.append(format.format(monthEnd));
//							sqlWeek.append("'");
//							sqlWeek.append(",");
//							sqlWeek.append(GRANULARITY_MONTH);
//							sqlWeek.append(",");
////							sqlWeek.append("'");
//							sqlWeek.append("now()");
////							sqlWeek.append("'");
//							sqlWeek.append(")");
//							debug(sqlWeek.toString());
//							sqlList.add(sqlWeek.toString());
//						}
//					}
					
					
				}else if(type==GRANULARITY_DATE){
					nowCalTime=getTimestamp(resTmp[0], format);
					nTime=getTimestamp(resTmp[2], formatH);
					dbKey=String.valueOf(resTmp[3]);
					if(nTime.getTime()-nowCalTime.getTime()>86400000l ){
						dayBeginTime=new Timestamp(nowCalTime.getTime());
						dayEndTime=TimeUtil.calculateTime(dayBeginTime, GRANULARITY_DATE, CALCULATE_ADD, 1L);
//						if(nowCalTime==null || weekEndTime.getTime()>nowCalTime.getTime()+86400000l)continue;
//						weekEndTime=new Timestamp(weekBeginTime.getTime()+86400000l*7l);
						debug(dayBeginTime+"--------------"+dayEndTime);
						if(genZQCReport(dayBeginTime, dayEndTime, GRANULARITY_DATE,dbKey)){
//						if(dao.updateSum(weekBeginTime, weekEndTime, type)){
							sqlWeek=new StringBuffer();
							sqlWeek.append("INSERT INTO VT_ZQC_REPORT_END_TIME");
							sqlWeek.append("(end_time,type,generate_time,dbKey)");
							sqlWeek.append("VALUES");
							sqlWeek.append("(");
							sqlWeek.append("'");
							sqlWeek.append(format.format(dayEndTime));
							sqlWeek.append("'");
							sqlWeek.append(",");
							sqlWeek.append(GRANULARITY_DATE);
							sqlWeek.append(",");
//							sqlWeek.append("'");
							sqlWeek.append("now()");
							sqlWeek.append(",");
							sqlWeek.append("'");
							sqlWeek.append(dbKey);
							sqlWeek.append("'");
							sqlWeek.append(")");
							debug(sqlWeek.toString());
							sqlList.add(sqlWeek.toString());
						}
						
					}
				}
			}
			
		}catch (Exception e) {
			error("during dateCheck");
			error(e);
		}
		
		if(sqlList.size()>0){
			DAOTools.execBatchS(sqlList, "ZQC",false);
			sqlList.clear();
		}
			
//		if(timeNow.getDate()==1 && !hasMonthCal){ //month
//			
//			hasMonthCal=true;
//		}else if(timeNow.getDate()!=1){
//			hasMonthCal=false;
//		}
//		
//		if(timeNow.getDay()==1 && !hasWeekCal){ //week
//			
//			
//			
//			hasWeekCal=true;
//		}else if(timeNow.getDay()!=1){
//			hasWeekCal=false;
//		}
	}
	
	
//	public void dateCheck(){
////		Timestamp timeNow=new Timestamp(System.currentTimeMillis());
//		Connection con=null;
//		ResultSet res=null;
//		Timestamp weekTime=null,MonthTime=null,nTime=null,weekEndTime=null,weekBeginTime=null;
//		int type;
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		String sql="select max(end_time) as endT,type,now() as nTime ,dbKey FROM `VT_REPORT_END_TIME` group by dbKey , type  ";
//		ArrayList<String> sqlList=new ArrayList<String>();
//		StringBuffer sqlWeek=new StringBuffer();
//		StringBuffer sqlMonth=new StringBuffer();
//		String dbKey=null;
//		try {
//			con=DAOTools.getConnectionOutS("ZQC");
//			res =DAOTools.getSelectRes(sql,con);
//			while(res.next()){
//				type=res.getInt("type");
//				if(type==GRANULARITY_WEEK){
//					weekTime=res.getTimestamp("endT");
//					nTime=res.getTimestamp("nTime");
//					dbKey=res.getString("dbKey");
//					if(nTime.getTime()-weekTime.getTime()>86400000l*7l){
//						weekBeginTime=new Timestamp(weekTime.getTime()+86400000l*7l);
//						weekEndTime=new Timestamp(weekBeginTime.getTime()+86400000l*7l);
//						debug(weekTime+"--------------"+weekBeginTime);
//						if(genZQCReport(weekBeginTime, weekEndTime, GRANULARITY_WEEK,dbKey)){
//							sqlWeek.append("INSERT INTO VT_REPORT_END_TIME");
//							sqlWeek.append("(end_time,type,generate_time,dbKey)");
//							sqlWeek.append("VALUES");
//							sqlWeek.append("(");
//							sqlWeek.append("'");
//							sqlWeek.append(format.format(weekBeginTime));
//							sqlWeek.append("'");
//							sqlWeek.append(",");
//							sqlWeek.append(GRANULARITY_WEEK);
//							sqlWeek.append(",");
////							sqlWeek.append("'");
//							sqlWeek.append("now()");
//							sqlWeek.append(",");
//							sqlWeek.append("'");
//							sqlWeek.append(dbKey);
//							sqlWeek.append("'");
////							sqlWeek.append("'");
//							sqlWeek.append(")");
//							debug(sqlWeek.toString());
//							sqlList.add(sqlWeek.toString());
//						}
//					}
////					if(weekTime.get){}
//				}else if(type==GRANULARITY_MONTH){
//					MonthTime=res.getTimestamp("endT");
//				}
//			}
//		}catch (Exception e) {
//			error("during dateCheck");
//			error(e);
//		}finally{
//			DAOTools.releaseConnectionOutS("ZQC",con);
//		}
//		
//		if(sqlList.size()>0){
//			try {
//				DAOTools.execBatchS(sqlList, "ZQC");
//			} catch (Exception e) {
//				error("update QC report end time");
//				error(e);
//			}finally{
////				DAOTools.releaseConnectionOutS("z3000_qc", con);
//			}
//		}
//			
////		if(timeNow.getDate()==1 && !hasMonthCal){ //month
////			
////			hasMonthCal=true;
////		}else if(timeNow.getDate()!=1){
////			hasMonthCal=false;
////		}
////		
////		if(timeNow.getDay()==1 && !hasWeekCal){ //week
////			
////			
////			
////			hasWeekCal=true;
////		}else if(timeNow.getDay()!=1){
////			hasWeekCal=false;
////		}
//	}
	
	public boolean genZQCReport(Timestamp begin,Timestamp end ,int type,String dbKey){
		boolean ret=true;
		if(end==null || begin==null) {
			warn("null begin or endTime genReport");
			return false;
		}
		Timestamp beginBef=null,endBef=null;
		switch (type) {
		case GRANULARITY_WEEK:
			beginBef=new Timestamp(begin.getTime()-86400000l*7l);
			endBef=new Timestamp(begin.getTime());
			break;
		case GRANULARITY_MONTH:
			
			
			break;
		default:
			break;
		}
		
		Connection con=null;
		ResultSet res=null;
		ArrayList<String> sqlExec=new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
		String extNumber=null,csrId=null,extName=null,skillName=null,physicsName=null,levelName=null,
		physicsNameBef=null,agentLevel=null;
		
		int totalPfx=0,totalCount=0,phyRank=0,totalCountSum=0;
		HashMap<String, Integer> physicsRank=new HashMap<String, Integer>();
		HashMap<String, Integer> skillRank=new HashMap<String, Integer>();
		HashMap<String, Integer> allRank=new HashMap<String, Integer>();
		HashMap<String, BigDecimal> allScore=new HashMap<String, BigDecimal>();
		HashMap<String, BigDecimal> levelScore=new HashMap<String, BigDecimal>();
		
		HashMap<String, Integer> allRankBef=new HashMap<String, Integer>();
		HashMap<String, BigDecimal> allScoreBef=new HashMap<String, BigDecimal>();
		BigDecimal avgScore=null,avgAllAgentScore=null,levelScoreTmp=null,levelScoreSum=null;
		ArrayList<String> allLevel=new ArrayList<String>();
		HashMap<String, String> levelTmp=new HashMap<String, String>();
//		String sql="SELECT sum(pfx_total),count(*),phone_number FROM `T_GRADE_DETAIL`" +
//				" where record_begin_time BETWEEN '"+format.format(begin)+"' and '"+format.format(end)+"' and pfx_total<>0" +
//						" group by phone_number";
		
//		String sql="SELECT sum(pfx_total) as totalPfx,count(*) as totalCount,sum(pfx_total)/count(*) as avgScore ,a.phone_number,csr_id,agent_user,skill_name,physics_name " +
//				"FROM `T_GRADE_DETAIL` a,SA_QC_AGENT_PHONE_MAP b" +
//				" where record_begin_time BETWEEN '"+format.format(begin)+"' and '"+format.format(end)+"' " +
//				"and pfx_total<>0 and a.phone_number=b.phone_number group by a.phone_number order by avgScore";
		
		
		
//		String sql="SELECT sum(pfx_total* role_percent/100) as totalPfx,count(*) as totalCount," +
//		"sum(pfx_total* role_percent/100)/count(*) as avgScore,physics_name FROM `T_GRADE_DETAIL` a," +
//		"SA_QC_AGENT_PHONE_MAP b,SYS_USER c,SU_ROLE_LIST d  where " +
//		"record_begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'   " +
//		"and pfx_total<>0 and role_percent<>'0' and a.phone_number=b.phone_number and c.role_name=d.role_name " +
//		"and user_id=grade_user group by  physics_name order by avgScore desc";
		
		sqlExec.add("delete from VT_LevelScore where WorkDate='"+formatDay.format(begin)+"'");
		sqlExec.add("delete from VT_SkillScore where WorkDate='"+formatDay.format(begin)+"'");
		sqlExec.add("delete from VT_AgentScore where WorkDate='"+formatDay.format(begin)+"'");
		sqlExec.add("delete from VT_UserLevelScore where WorkDate='"+formatDay.format(begin)+"'");
		
		
		sqlExec.add("delete from ZQCAgentAvgScore where begin_time='"+formatDay.format(begin)+"'");
		sqlExec.add("delete from ZQCAgentGroupAvgScore where begin_time='"+formatDay.format(begin)+"'");
		sqlExec.add("delete from ZQCProAndQualityCompareReport where begin_time='"+formatDay.format(begin)+"'");
		sqlExec.add("delete from ZQCZhiJianYanAvgScore where begin_time='"+formatDay.format(begin)+"'");
		sqlExec.add("delete from ZQCFuJianTongJiReport where begin_time='"+formatDay.format(begin)+"'");
		sqlExec.add("delete from ZQCFuShenTongJiReport where begin_time='"+formatDay.format(begin)+"'");
		
//		String sql="select sum(pfx_total* d.scoreWeight/100) as totalPfx,count(*) as totalCount, " +
//				"sum(pfx_total* d.scoreWeight/100)/count(*) as avgScore,f.`alias2` FROM suSecurityUser b ," +
//				"suSecurityRole d ,SU_QC_SOURCE_RECORD_DATA e ,`suSecurityUser` f where begin_time " +
//				"BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'" +
//						" and pfx_total<>0 and d.scoreWeight<>'0' " +
//				"and b.`roleName`=d.name and b.`alias1`=e.`owner` and e.`phone_number`=f.`phone_number` " +
//				"group by f.`alias2` order by avgScore desc ";
//		debug(sql);
		try {
			
			String sql="INSERT INTO ZQCAgentAvgScore SELECT  NULL AS id ,  '"+formatDay.format(begin)+" 00:00:00"+"' AS begin_time " +
					" ,alias1,alias6,alias2,business_type,SUM(texte_longueur) AS texte_longueur" +
					",quartier," +
					" grade_name,SUM(`pfx_total`)/COUNT(*) AS 'avg_score',COUNT(*) AS 'acc_number' FROM ZQCPingFenLiuShuiReport   WHERE  " +
					"  begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'  " +
							"   GROUP BY alias1,alias6,quartier,`business_type`,grade_name";
			debug(sql);
			sqlExec.add(sql);
			
			
			sql="INSERT INTO  ZQCAgentGroupAvgScore SELECT  NULL AS id , '"+formatDay.format(begin)+" 00:00:00"+"' AS begin_time" +
					" ,`alias1`,alias2,business_type , " +
					"SUM(texte_longueur) AS texte_longueur ,quartier ,grade_name" +
					"   FROM ZQCPingFenLiuShuiReport    WHERE" +
					"  begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'  " +
					"  GROUP BY alias2,quartier,`business_type`,grade_name ";
			
			debug(sql);
			sqlExec.add(sql);
			
			
			sql="INSERT INTO  ZQCProAndQualityCompareReport   SELECT NULL AS id ," +
				" '"+formatDay.format(begin)+" 00:00:00"+"' AS begin_time,`alias1`,`alias6`,`alias2`,   " +
				" COUNT(IF(`texte_longueur`>0,TRUE,NULL)) AS service_number  ," +
				"SUM(texte_longueur)/COUNT(IF(`texte_longueur`>0,TRUE,NULL)) AS avg_time," +
				"SUM(pfx_total)/COUNT(IF(`score_state`='初检已发布' OR `score_state`='复审已发布' ,TRUE,NULL)) AS avg_score  ,  " +
				" `grade_name`,  COUNT(IF(`score_state`='初检已发布' OR `score_state`='复审已发布' ,TRUE,NULL)) AS acc_number  ," +
				"quartier " +
				"FROM `SU_QC_SOURCE_RECORD_DATA` WHERE  " +
				" `begin_time` BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' AND  " +
				"  `score_state`!=''  AND `alias1` IS NOT NULL   AND `alias1`!='null' AND `alias1`!=''  " +
				" GROUP BY alias1,alias6";
			
			debug(sql);
			sqlExec.add(sql);
			
			sql="INSERT INTO `ZQCZhiJianYanAvgScore` SELECT NULL AS id , '"+formatDay.format(begin)+" 00:00:00"+"' AS begin_time " +
					" ,business_type ,`owner`,alias2,`alias4`,`alias5`,grade_name , " +
					"SUM(texte_longueur)  AS texte_longueur,quartier  ,`alias6` ,SUM(`pfx_total`)/COUNT(*) AS score," +
					"COUNT(*) AS `acc_number`,  '' AS `fujian_number`   FROM ZQCPingFenLiuShuiReport   WHERE " +
					" begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
					" GROUP BY `owner`,quartier,`business_type`,grade_name";
			
			debug(sql);
			sqlExec.add(sql);
			
			
			sql="INSERT INTO `ZQCFuJianTongJiReport` SELECT NULL AS id , '"+formatDay.format(begin)+" 00:00:00"+"' AS begin_time " +
			" ,`owner`,`ZhiJianYuanName`,`ZhiJianZu`,SUM(FuJianFenShu)  AS FuJianFenShu,COUNT(*) AS `fujian_number`,grade_name,`FuJianRenYuan`,FuJianPingGuTime" +
			"   FROM ZQCFuJianLiuShuiReport   WHERE " +
			" begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
			" GROUP BY `owner`,ZhiJianYuanName,ZhiJianZu,FuJianRenYuan,grade_name";
	
			debug(sql);
			sqlExec.add(sql);
			
			sql="INSERT INTO `ZQCFuShenTongJiReport` SELECT NULL AS id , '"+formatDay.format(begin)+" 00:00:00"+"' AS begin_time " +
			" ,`owner`,`zhijianyuanname`,`zhijianzu`,FuShenFaBuTime,fushenrenyuan, COUNT(*) AS `fushen_number`, SUM(FuShenFen)/COUNT(*)  AS fushen_score, SUM(ChuJianFen)/COUNT(*)  AS chujian_score, grade_name " +
			"   FROM ZQCFuShenLiuShuiReport   WHERE " +
			" begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
			" GROUP BY `owner`,zhijianyuanname,zhijianzu,fushenrenyuan,grade_name";
	
			debug(sql);
			sqlExec.add(sql);
			
			
			 sql="select sum(pfx_total* d.scoreWeight/100) as totalPfx,count(*) as totalCount, " +
			"sum(pfx_total* d.scoreWeight/100)/count(*) as avgScore,f.`alias2` FROM suSecurityUser b ," +
			"suSecurityRole d ,SU_QC_SOURCE_RECORD_DATA e ,`suSecurityUser` f where begin_time " +
			"BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'" +
					" and pfx_total<>0 and d.scoreWeight<>'0' " +
			"and b.`roleName`=d.name and b.`alias1`=e.`owner` and e.`phone_number`=f.`phone_number` " +
			"group by f.`alias2` order by avgScore desc ";
			 debug(sql);
			
			con=DAOTools.getConnectionOutS("ZDesk");
			res =DAOTools.getSelectRes(sql,con);
			while(res.next()){
				physicsName=res.getString("alias2");
				physicsRank.put(physicsName, new Integer(++phyRank));
			}
			
			sql=" select DISTINCT agent_level from suSecurityUser   ";
			res =DAOTools.getSelectRes(sql,con);
			while(res.next()){
				allLevel.add(res.getString("agent_level"));
			}
		
//			sql="SELECT sum(pfx_total* role_percent/100) as totalPfx,count(*) as totalCount," +
//				"sum(pfx_total* role_percent/100)/count(*) as avgScore,agent_level FROM " +
//					"`T_GRADE_DETAIL` a, SA_QC_AGENT_PHONE_MAP b,SYS_USER c,SU_ROLE_LIST d  " +
//					"where record_begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'  " +
//					"and pfx_total<>0 and role_percent<>'0' and a.phone_number=b.phone_number and " +
//					"c.role_name=d.role_name and user_id=grade_user group by  agent_level order by avgScore ";
//			
//			
			sql="SELECT sum(pfx_total* d.scoreWeight/100) as totalPfx,count(*) as totalCount, " +
					"sum(pfx_total* d.scoreWeight/100)/count(*) as avgScore,f.agent_level FROM " +
					"`SU_QC_SOURCE_RECORD_DATA` a, suSecurityUser b,suSecurityRole d ," +
					"suSecurityUser f where begin_time" +
					" BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
					"and pfx_total<>0 and d.scoreWeight<>'0' " +
					"and b.`alias1`=a.`owner` and b.`roleName`=d.name and a.`phone_number`=f.`phone_number` " +
					"group by f.agent_level order by avgScore";
//			sql="SELECT sum(pfx_total* scoreWeight/100) as totalPfx,count(*) as totalCount, " +
//					" sum(pfx_total* scoreWeight/100)/count(*) as avgScore,agent_level FROM " +
//					" `SU_QC_SOURCE_RECORD_DATA` a, suSecurityUser b,suSecurityRole d " +
//					" where begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
//							"and pfx_total<>0 and scoreWeight<>'0' and b.`alias1`=a.`owner` and " +
//							"b.`roleName`=d.name  group by  agent_level order by avgScore ";
		
			debug(sql);
			res =DAOTools.getSelectRes(sql,con);
			levelScoreSum=new BigDecimal(0);
			totalCountSum=0;
			while(res.next()){
				levelName=res.getString("agent_level");
				debug("VT_LevelScore agentLevel:"+agentLevel);
				if(res.getBigDecimal("avgScore")!=null){
					levelScore.put(levelName, Utility.round(res.getString("avgScore"), 2));
					
					levelScoreSum=levelScoreSum.add(res.getBigDecimal("totalPfx"));
					//debug(res.getBigDecimal("totalPfx")+"levelScoreSum:"+levelScoreSum.doubleValue());
					totalCountSum+=res.getInt("totalCount");
					
					StringBuffer sqlTmp=new StringBuffer();
			        sqlTmp.append("replace into VT_LevelScore ");
			        sqlTmp.append("(LevelName,Score,Count,AvgScore,WorkDate)");
			        sqlTmp.append("values (");
			        sqlTmp.append("'");
					sqlTmp.append(levelName);
					sqlTmp.append("'");
					sqlTmp.append(",");
					sqlTmp.append(res.getBigDecimal("totalPfx"));
					sqlTmp.append(",");
					sqlTmp.append(res.getInt("totalCount"));
					sqlTmp.append(",");
					if(levelScore.containsKey(levelName)){
						sqlTmp.append(levelScore.get(levelName));
					}else{
					sqlTmp.append("0");
					}
				
					sqlTmp.append(",");
					sqlTmp.append("'");
					sqlTmp.append(formatDay.format(begin));
					sqlTmp.append("'");
					sqlTmp.append(")");
					sqlExec.add(sqlTmp.toString());
				}
			}
//			if(totalCountSum>0){
			StringBuffer sqlTmp=new StringBuffer();
		        sqlTmp.append("replace into VT_LevelScore ");
		        sqlTmp.append("(LevelName,Score,Count,AvgScore,WorkDate)");
		        sqlTmp.append("values (");
		        sqlTmp.append("'");
		        sqlTmp.append("汇总");
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append(levelScoreSum.doubleValue());
				sqlTmp.append(",");
				sqlTmp.append(totalCountSum);
				sqlTmp.append(",");
				if(totalCountSum>0){
					sqlTmp.append(levelScoreSum.divide(new BigDecimal(totalCountSum), 2, BigDecimal.ROUND_HALF_UP));
				}else{
					sqlTmp.append(0);
				}
				//sqlTmp.append(levelScore.get(levelName));
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(formatDay.format(begin));
				sqlTmp.append("'");
				sqlTmp.append(")");
				sqlExec.add(sqlTmp.toString());
//			}
//			sql="SELECT sum(pfx_total* role_percent/100) as totalPfx,count(*) as totalCount," +
//					"sum(pfx_total* role_percent/100)/count(*) as avgScore ,business_type FROM `T_GRADE_DETAIL` a, " +
//					"SYS_USER c,SU_ROLE_LIST d  where record_begin_time " +
//					"BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' and pfx_total<>0 and " +
//					"role_percent<>'0' and c.role_name=d.role_name and user_id=grade_user group by " +
//					" business_type order by avgScore desc ";
			
			sql="SELECT sum(pfx_total* scoreWeight/100) as totalPfx,count(*) as totalCount, " +
					"sum(pfx_total* scoreWeight/100)/count(*) as avgScore ,business_type FROM " +
					"`SU_QC_SOURCE_RECORD_DATA` a, suSecurityUser b,suSecurityRole d  where " +
					"begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
					"and pfx_total<>0 and scoreWeight<>'0' and b.`roleName`=d.name  and b.`alias1`=a.`owner` " +
					"group by business_type order by avgScore desc ";
			
			res =DAOTools.getSelectRes(sql,con);
			while(res.next()){
				skillName=res.getString("business_type");
				if(res.getBigDecimal("avgScore")!=null){
				
					sqlTmp=new StringBuffer();
			        sqlTmp.append("replace into VT_SkillScore ");
			        sqlTmp.append("(Score,Count,AvgScore,skillGroupID,WorkDate)");
			        sqlTmp.append("values (");
			        sqlTmp.append(res.getBigDecimal("totalPfx"));
					sqlTmp.append(",");
			    	sqlTmp.append(res.getInt("totalCount"));
					sqlTmp.append(",");
					sqlTmp.append(res.getBigDecimal("avgScore"));
					sqlTmp.append(",");
			        sqlTmp.append("'");
					sqlTmp.append(skillName);
					sqlTmp.append("'");
					sqlTmp.append(",");
					sqlTmp.append("'");
					sqlTmp.append(formatDay.format(begin));
					sqlTmp.append("'");
					sqlTmp.append(")");
					sqlExec.add(sqlTmp.toString());
				}
			}
			
//			sql="SELECT sum(pfx_total* role_percent/100) as totalPfx,count(*) as totalCount, " +
//					"sum(pfx_total* role_percent/100)/count(*) as avgScore FROM `T_GRADE_DETAIL` a, " +
//					"SYS_USER c,SU_ROLE_LIST d  where  record_begin_time" +
//					" BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
//					" and pfx_total<>0 and role_percent<>'0' and c.role_name=d.role_name " +
//					"and user_id=grade_user ";
			sql="SELECT sum(pfx_total* scoreWeight/100) as totalPfx,count(*) as totalCount," +
					"sum(pfx_total* scoreWeight/100)/count(*) as avgScore FROM `SU_QC_SOURCE_RECORD_DATA` a," +
					" suSecurityUser b,suSecurityRole d  where  begin_time " +
					" BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
					"and pfx_total<>0 and scoreWeight<>'0' and b.`roleName`=d.name  and b.`alias1`=a.`owner` ";
			res =DAOTools.getSelectRes(sql,con);
			while(res.next()){
				avgAllAgentScore=res.getBigDecimal("avgScore");
				if(avgAllAgentScore!=null){
					avgAllAgentScore=Utility.round(avgAllAgentScore.toString(), 2);
				}
			}
			
			
//			sql="SELECT sum(pfx_total* role_percent/100) as totalPfx,count(*) as totalCount," +
//			"sum(pfx_total* role_percent/100)/count(*) as avgScore,skill_name FROM `T_GRADE_DETAIL` a," +
//			"SA_QC_AGENT_PHONE_MAP b,SYS_USER c,SU_ROLE_LIST d  where " +
//			"record_begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'   " +
//			"and pfx_total<>0 and role_percent<>'0' and a.phone_number=b.phone_number and c.role_name=d.role_name " +
//			"and user_id=grade_user group by  skill_name order by avgScore desc";
			
			sql=" SELECT sum(pfx_total* d.scoreWeight/100) as totalPfx,count(*) as totalCount, " +
					"sum(pfx_total* d.scoreWeight/100)/count(*) as avgScore,f.`alias3` " +
					"FROM `SU_QC_SOURCE_RECORD_DATA` a, suSecurityUser b,suSecurityRole d ," +
					"suSecurityUser f where begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'  " +
					"and pfx_total<>0 and d.scoreWeight<>'0' and b.`roleName`=d.name and b.`alias1`=a.`owner` " +
					"and a.`phone_number`=f.`phone_number` group by f.`alias3` order by avgScore desc ";
			
//			sql="  SELECT sum(pfx_total* scoreWeight/100) as totalPfx,count(*) as totalCount, " +
//					"sum(pfx_total* scoreWeight/100)/count(*) as avgScore,b.`alias3` FROM `SU_QC_SOURCE_RECORD_DATA` a," +
//					" suSecurityUser b,suSecurityRole d  where begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
//					" and pfx_total<>0 and scoreWeight<>'0' and b.`roleName`=d.name  and b.`alias1`=a.`owner`" +
//					" group by  b.`alias3` order by avgScore desc ";
		
			debug(sql);
			res =DAOTools.getSelectRes(sql,con);
			phyRank=0;
			while(res.next()){
				skillName=res.getString("alias3");
				skillRank.put(skillName, new Integer(++phyRank));
			}
			
//			sql=" SELECT phone_number, sum(pfx_total* role_percent/100)/count(*) as avgScore" +
//			" FROM `T_GRADE_DETAIL` ,SYS_USER c,SU_ROLE_LIST d  where record_begin_time " +
//			" BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' and pfx_total<>0  " +
//			" and role_percent<>'0' and user_id=grade_user  group by  phone_number order by  avgScore desc";
			sql="  SELECT a.phone_number, sum(pfx_total* scoreWeight/100)/count(*) as avgScore " +
					"FROM `SU_QC_SOURCE_RECORD_DATA` a ,suSecurityUser b,suSecurityRole d  " +
					" where begin_time BETWEEN  '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
					"and pfx_total<>0 and scoreWeight<>'0' and b.`roleName`=d.name  and b.`alias1`=a.`owner` " +
					"group by a.phone_number order by  avgScore desc ";
			debug(sql);
			res =DAOTools.getSelectRes(sql,con);
			phyRank=0;
			while(res.next()){
				extNumber=res.getString("phone_number");
				allRank.put(extNumber, new Integer(++phyRank));
				
				if(res.getBigDecimal("avgScore")!=null){
					allScore.put(extNumber, Utility.round(res.getString("avgScore"), 2));
				}
			}
			
//			sql=" SELECT phone_number, sum(pfx_total* role_percent/100)/count(*) as avgScore" +
//			" FROM `T_GRADE_DETAIL` ,SYS_USER c,SU_ROLE_LIST d  where record_begin_time " +
//			" BETWEEN '"+formatDay.format(beginBef)+" 00:00:00"+"' and '"+formatDay.format(endBef)+" 23:59:59"+"' and pfx_total<>0  " +
//			" and role_percent<>'0' and user_id=grade_user group by  phone_number order by  avgScore desc";
			sql="  SELECT a.phone_number, sum(pfx_total* scoreWeight/100)/count(*) as avgScore " +
			"FROM `SU_QC_SOURCE_RECORD_DATA` a ,suSecurityUser b,suSecurityRole d  " +
			" where begin_time BETWEEN  '"+formatDay.format(beginBef)+" 00:00:00"+"' and '"+formatDay.format(endBef)+" 23:59:59"+"' " +
			"and pfx_total<>0 and scoreWeight<>'0' and b.`roleName`=d.name  and b.`alias1`=a.`owner` " +
			"group by a.phone_number order by  avgScore desc ";
			
			debug(sql);
			res =DAOTools.getSelectRes(sql,con);
			phyRank=0;
			while(res.next()){
				extNumber=res.getString("phone_number");
				allRankBef.put(extNumber, new Integer(++phyRank));
				if(res.getBigDecimal("avgScore")!=null){
					allScoreBef.put(extNumber, Utility.round(res.getString("avgScore"), 2));
				}
			}
			
////			sql=" SELECT sum(pfx_total * role_percent/100) as totalPfx,count(*) as totalCount," +
////			"sum(pfx_total* role_percent/100)/count(*) as avgScore ,a.phone_number,csr_id,agent_user," +
////			"skill_name,physics_name,agent_level FROM `T_GRADE_DETAIL` a,SA_QC_AGENT_PHONE_MAP b," +
////			"SYS_USER c,SU_ROLE_LIST d  where record_begin_time " +
////			" BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' and pfx_total<>0 and " +
////			" a.phone_number=b.phone_number and role_percent<>'0' and c.role_name=d.role_name and user_id=grade_user " +
////			" group by  a.phone_number order by physics_name, avgScore desc";
//			
////			sql=" SELECT sum(pfx_total * scoreWeight/100) as totalPfx,count(*) as totalCount, " +
////					"sum(pfx_total* scoreWeight/100)/count(*) as avgScore ,a.phone_number,b.`alias1`,b.`name`, " +
////					"b.`alias3`,b.`alias2`,agent_level FROM `SU_QC_SOURCE_RECORD_DATA` a,suSecurityUser " +
////					"b,suSecurityRole d where begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
////					"and pfx_total<>0 and scoreWeight<>'0' and b.`roleName`=d.name and b.`alias1`=a.`owner` " +
////					"group by a.phone_number order by b.`alias3`, avgScore desc";
		
			sql="SELECT sum(pfx_total * d.scoreWeight/100) as totalPfx,count(*) as totalCount, " +
					"sum(pfx_total* d.scoreWeight/100)/count(*) as avgScore ,a.phone_number,f.`alias1`,f.`name`, " +
					"f.`alias3`,f.`alias2`,f.agent_level FROM `SU_QC_SOURCE_RECORD_DATA` a,suSecurityUser b," +
					"suSecurityRole d ,suSecurityUser f where begin_time " +
					"BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
					"and pfx_total<>0 and d.scoreWeight<>'0' and b.`roleName`=d.name and b.`alias1`=a.`owner`" +
					" and a.`phone_number`=f.`phone_number` group by a.phone_number order by f.`alias2`, " +
					"avgScore desc ";
			debug(sql);
			res =DAOTools.getSelectRes(sql,con);
			phyRank=0;
			while(res.next()){
				extNumber=res.getString("phone_number");
				csrId=res.getString("alias1"); //Alias1
				extName=res.getString("name");
				skillName=res.getString("alias3");
				physicsName=res.getString("alias2");
				totalPfx=res.getInt("totalPfx");
				totalCount=res.getInt("totalCount");
				agentLevel=res.getString("agent_level");
				avgScore=res.getBigDecimal("avgScore");
				if(avgScore!=null){
					avgScore=Utility.round(avgScore.toString(), 2);
				}
//				allRank.get(extNumber);
//				allScore.get(extNumber);
//				allRankBef.get(extNumber);
//				allScoreBef.get(extNumber);
				if(physicsNameBef==null){
					++phyRank;
				}else if(physicsNameBef.equals(physicsName)){
					++phyRank;
				}else if(!physicsNameBef.equals(physicsName)){
					phyRank=1;
				}
				
				sqlTmp=new StringBuffer();
				sqlTmp.append("replace into VT_AgentScore ");
				sqlTmp.append("(Name,Level,ExtNumber,Score,Count,AvgScore,SkillGroupID,PhyGroupID," +
						"SkillGroupRank,InsideSkillGroupRank,PhyGroupRank,InsidePhyGroupRank," +
						"AllAgentRank,WorkDate,RankFloating,AvgScoreFloating,Alias1,AvgSameLevelScore,AvgAllAgentScore) ");
				sqlTmp.append("values (");
				sqlTmp.append("'");
				sqlTmp.append(extName);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(agentLevel);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(extNumber);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append(totalPfx);
				sqlTmp.append(",");
				sqlTmp.append(totalCount);
				sqlTmp.append(",");
				if(avgScore!=null){
					sqlTmp.append(avgScore.doubleValue());
				}else{
					sqlTmp.append(0);
				}
		
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(skillName);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(physicsName);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append(skillRank.get(extNumber));
				sqlTmp.append(",");
				sqlTmp.append(skillRank.get(extNumber));
				sqlTmp.append(",");
				sqlTmp.append(physicsRank.get(physicsName));
				sqlTmp.append(",");
				sqlTmp.append(phyRank);
				sqlTmp.append(",");
				sqlTmp.append(allRank.get(extNumber));
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(formatDay.format(begin));
				sqlTmp.append("'");
				sqlTmp.append(",");
				
				if(allRank.get(extNumber)!=null && allRankBef.get(extNumber)!=null){
					sqlTmp.append(allRank.get(extNumber)-allRankBef.get(extNumber));
				}else{
					sqlTmp.append("0");
				}
				sqlTmp.append(",");
			
				if(allScore.get(extNumber)!=null && allScoreBef.get(extNumber)!=null){
//				debug(allScore.get(extNumber)+"_______sub_______"+allScoreBef.get(extNumber)+"______"+allScore.get(extNumber).subtract(allScoreBef.get(extNumber)).doubleValue());
					sqlTmp.append(allScore.get(extNumber).subtract(allScoreBef.get(extNumber)).doubleValue());
				}else{
					sqlTmp.append("0.0");
				}
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(csrId);
				sqlTmp.append("'");
				sqlTmp.append(",");
				if(agentLevel!=null && levelScore.containsKey(agentLevel)){
					sqlTmp.append(levelScore.get(agentLevel).doubleValue());
				}else{
					sqlTmp.append(0);
				}
				sqlTmp.append(",");
				if(avgAllAgentScore!=null){
					sqlTmp.append(avgAllAgentScore.doubleValue());
				}else{
					sqlTmp.append("0");	
				}
				sqlTmp.append(")");
				debug(sqlTmp.toString());
				sqlExec.add(sqlTmp.toString());
			
				physicsNameBef=physicsName;
			}
////			sql="SELECT sum(pfx_total* role_percent/100) as totalPfx,count(*) as totalCount," +
////					"sum(pfx_total* role_percent/100)/count(*) as avgScore,b.agent_level,grade_user,user_name,e.csr_id " +
////					"FROM  `T_GRADE_DETAIL` a, SA_QC_AGENT_PHONE_MAP b,SYS_USER c,SU_ROLE_LIST d ,SA_QC_AGENT_PHONE_MAP e " +
////					"  where record_begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'   " +
////					" and pfx_total<>0 and role_percent<>'0' and a.phone_number=b.phone_number and " +
////					" c.role_name=d.role_name and user_id=grade_user and user_id=e.phone_number group by grade_user, agent_level ";
//			
//			
////			sql="SELECT sum(pfx_total * d.scoreWeight/100) as totalPfx,count(*) as totalCount, " +
////			"sum(pfx_total* d.scoreWeight/100)/count(*) as avgScore ,a.phone_number,f.`alias1`,f.`name`, " +
////			"f.`alias3`,f.`alias2`,f.agent_level FROM `SU_QC_SOURCE_RECORD_DATA` a,suSecurityUser b," +
////			"suSecurityRole d ,suSecurityUser f where begin_time " +
////			"BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
////			"and pfx_total<>0 and d.scoreWeight<>'0' and b.`roleName`=d.name and b.`alias1`=a.`owner`" +
////			" and a.`phone_number`=f.`phone_number` group by a.phone_number order by f.`alias2`, " +
////			"avgScore desc ";
		
			sql="SELECT sum(pfx_total* scoreWeight/100) as totalPfx,count(*) as totalCount,  " +
					"sum(pfx_total* scoreWeight/100)/count(*) as avgScore,f.agent_level,a.`owner`,b.`alias1`," +
					"b.`name` FROM `SU_QC_SOURCE_RECORD_DATA` a,suSecurityUser b,suSecurityRole d  ,suSecurityUser f  " +
					" where begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'  " +
					"and pfx_total<>0 and scoreWeight<>'0' and b.`roleName`=d.name and b.`alias1`=a.`owner`  " +
					" and a.`phone_number`=f.`phone_number`  group by a.`owner`, f.agent_level ";
			debug(sql);
			res =DAOTools.getSelectRes(sql,con);
			String extBef=null,befCsrId=null,befName=null;
			while(res.next()){
				extNumber=res.getString("owner");
				extName=res.getString("name");
				totalPfx=res.getInt("totalPfx");
				totalCount=res.getInt("totalCount");
				agentLevel=res.getString("agent_level");
				debug(extBef+" VT_UserLevelScore agentLevel:"+agentLevel+" owner: "+extNumber);
			
				avgScore=res.getBigDecimal("avgScore");
				csrId=res.getString("alias1");
//				levelTmp.put(agentLevel, agentLevel);
				if(extBef==null || extBef.equals(extNumber)){
					levelTmp.put(agentLevel, agentLevel);
////					xxxxxxxxxx
				}else{
					for(int i=0;i<allLevel.size();i++){
						if(!levelTmp.containsKey(allLevel.get(i))){
							sqlTmp=new StringBuffer();
							sqlTmp.append("replace into VT_UserLevelScore ");
							sqlTmp.append("(UserName,UserId,LevelName,Score,Count,AvgScore,WorkDate) ");
							sqlTmp.append("values (");
							sqlTmp.append("'");
							sqlTmp.append(befName);
							sqlTmp.append("'");
							sqlTmp.append(",");
							sqlTmp.append("'");
							sqlTmp.append(befCsrId);
							sqlTmp.append("'");
							sqlTmp.append(",");
							sqlTmp.append("'");
							sqlTmp.append(allLevel.get(i));
							sqlTmp.append("'");
							sqlTmp.append(",");
							sqlTmp.append(0);
							sqlTmp.append(",");
							sqlTmp.append(0);
							sqlTmp.append(",");
							sqlTmp.append(0);
							sqlTmp.append(",");
							sqlTmp.append("'");
							sqlTmp.append(formatDay.format(begin));
							sqlTmp.append("'");
							sqlTmp.append(")");
							
							debug(sqlTmp.toString());
						
							sqlExec.add(sqlTmp.toString());
						}
					}
					levelTmp.clear();
					levelTmp.put(agentLevel, agentLevel);
				}
				
				
				sqlTmp=new StringBuffer();
				sqlTmp.append("replace into VT_UserLevelScore ");
				sqlTmp.append("(UserName,UserId,LevelName,Score,Count,AvgScore,WorkDate) ");
				sqlTmp.append("values (");
				sqlTmp.append("'");
				sqlTmp.append(extName);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(csrId);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(agentLevel);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append(totalPfx);
				sqlTmp.append(",");
				sqlTmp.append(totalCount);
				sqlTmp.append(",");
				if(avgScore!=null){
					sqlTmp.append(avgScore.doubleValue());
				}else{
					sqlTmp.append(0);
				}
			
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(formatDay.format(begin));
				sqlTmp.append("'");
				sqlTmp.append(")");
				
				debug(sqlTmp.toString());
				
				sqlExec.add(sqlTmp.toString());
				extBef=extNumber;
				befCsrId=csrId;
				befName=extName;
			}
			
			for(int i=0;i<allLevel.size();i++){
				if(!levelTmp.containsKey(allLevel.get(i))){
					sqlTmp=new StringBuffer();
					sqlTmp.append("replace into VT_UserLevelScore ");
					sqlTmp.append("(UserName,UserId,LevelName,Score,Count,AvgScore,WorkDate) ");
					sqlTmp.append("values (");
					sqlTmp.append("'");
					sqlTmp.append(befName);
					sqlTmp.append("'");
					sqlTmp.append(",");
					sqlTmp.append("'");
					sqlTmp.append(befCsrId);
					sqlTmp.append("'");
					sqlTmp.append(",");
					sqlTmp.append("'");
					sqlTmp.append(allLevel.get(i));
					sqlTmp.append("'");
					sqlTmp.append(",");
					sqlTmp.append(0);
					sqlTmp.append(",");
					sqlTmp.append(0);
					sqlTmp.append(",");
					sqlTmp.append(0);
					sqlTmp.append(",");
					sqlTmp.append("'");
					sqlTmp.append(formatDay.format(begin));
					sqlTmp.append("'");
					sqlTmp.append(")");
					
					debug(sqlTmp.toString());
					
					sqlExec.add(sqlTmp.toString());
				}
			}
			
//			for(Iterator iL = levelScore.entrySet().iterator(); iL.hasNext();) {
//		        java.util.Map.Entry eL = (java.util.Map.Entry)iL.next();
//		        levelScoreTmp =(BigDecimal)eL.getValue();
//		        levelName =(String)eL.getKey();
//				
//				
//			}
			
			DAOTools.execBatchS(sqlExec, "ZQC",false);
			
		}catch (Exception e) {
			error("during genReport");
			error(e);
		}finally{
//			DAOTools.releaseConnectionOutS("ZQC",con);
		}
		
		
		switch (type) {
		case GRANULARITY_WEEK:
			
			
			break;
		case GRANULARITY_MONTH:
			
			
			break;	
		default:
			break;
		}
		
		return ret;
	}
	
	/*public boolean genZQCReportRecord(Timestamp begin,Timestamp end ,int type,String dbKey){
		boolean ret=true;
		if(end==null || begin==null) {
			warn("null begin or endTime genReportRecord");
			return false;
		}
		Timestamp beginBef=null,endBef=null;
		switch (type) {
		case GRANULARITY_WEEK:
			beginBef=new Timestamp(begin.getTime()-86400000l*7l);
			endBef=new Timestamp(begin.getTime());
			break;
		case GRANULARITY_MONTH:
			
			
			break;
		default:
			break;
		}
		
		Connection con=null;
		ResultSet res=null;
		ArrayList<String> sqlExec=new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
		String extNumber=null,csrId=null,extName=null,skillName=null,physicsName=null,levelName=null,
		physicsNameBef=null,agentLevel=null;
		
		int totalPfx=0,totalCount=0,phyRank=0,totalCountSum=0;
		HashMap<String, Integer> physicsRank=new HashMap<String, Integer>();
		HashMap<String, Integer> skillRank=new HashMap<String, Integer>();
		HashMap<String, Integer> allRank=new HashMap<String, Integer>();
		HashMap<String, BigDecimal> allScore=new HashMap<String, BigDecimal>();
		HashMap<String, BigDecimal> levelScore=new HashMap<String, BigDecimal>();
		
		HashMap<String, Integer> allRankBef=new HashMap<String, Integer>();
		HashMap<String, BigDecimal> allScoreBef=new HashMap<String, BigDecimal>();
		BigDecimal avgScore=null,avgAllAgentScore=null,levelScoreTmp=null,levelScoreSum=null;
		ArrayList<String> allLevel=new ArrayList<String>();
		HashMap<String, String> levelTmp=new HashMap<String, String>();

		
		sqlExec.add("delete from VT_LevelScore where WorkDate='"+formatDay.format(begin)+"'");
		sqlExec.add("delete from VT_SkillScore where WorkDate='"+formatDay.format(begin)+"'");
		sqlExec.add("delete from VT_AgentScore where WorkDate='"+formatDay.format(begin)+"'");
		sqlExec.add("delete from VT_UserLevelScore where WorkDate='"+formatDay.format(begin)+"'");
		
		
		

		try {
			
			String sql="select sum(pfx_total) as totalPfx,count(*) as totalCount, " +
			"sum(pfx_total/100)/count(*) as avgScore,f.`alias2` FROM suSecurityUser b ," +
			"suSecurityRole d ,SU_QC_SOURCE_RECORD_DATA e ,`suSecurityUser` f where begin_time " +
			"BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'" +
				" and pfx_total<>0  " +
			"and b.`roleName`=d.name and b.`alias1`=e.`owner` and e.`phone_number`=f.`phone_number` " +
			"group by f.`alias2` order by avgScore desc ";
//	         debug(sql);
			
			
			con=DAOTools.getConnectionOutS(dbKey);
			res =DAOTools.getSelectRes(sql,con);
			while(res.next()){
				physicsName=res.getString("alias2");
				physicsRank.put(physicsName, new Integer(++phyRank));
			}
			
			sql=" select DISTINCT agent_level from suSecurityUser   ";
			res =DAOTools.getSelectRes(sql,con);
			while(res.next()){
				allLevel.add(res.getString("agent_level"));
			}
//			sql="SELECT sum(pfx_total* role_percent/100) as totalPfx,count(*) as totalCount," +
//					"sum(pfx_total* role_percent/100)/count(*) as avgScore,agent_level FROM " +
//					"`T_GRADE_DETAIL` a, SA_QC_AGENT_PHONE_MAP b,SYS_USER c,SU_ROLE_LIST d  " +
//					"where record_begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'  " +
//					"and pfx_total<>0 and role_percent<>'0' and a.phone_number=b.phone_number and " +
//					"c.role_name=d.role_name and user_id=grade_user group by  agent_level order by avgScore ";
			
			
			sql="SELECT sum(pfx_total) as totalPfx,count(*) as totalCount, " +
					"sum(pfx_total/100)/count(*) as avgScore,f.agent_level FROM " +
					"`SU_QC_SOURCE_RECORD_DATA` a, suSecurityUser b,suSecurityRole d ," +
					"suSecurityUser f where begin_time" +
					" BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
					"and pfx_total<>0  " +
					"and b.`alias1`=a.`owner` and b.`roleName`=d.name and a.`phone_number`=f.`phone_number` " +
					"group by f.agent_level order by avgScore";
//			sql="SELECT sum(pfx_total) as totalPfx,count(*) as totalCount, " +
//					" sum(pfx_total/100)/count(*) as avgScore,b.agent_level FROM " +
//					" `SU_QC_SOURCE_RECORD_DATA` a, suSecurityUser b,suSecurityRole d " +
//					" where begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
//							"and pfx_total<>0  and b.`alias1`=a.`owner` and " +
//							"b.`roleName`=d.name  group by  b.agent_level order by avgScore ";
			
			debug(sql);
			res =DAOTools.getSelectRes(sql,con);
			levelScoreSum=new BigDecimal(0);
			totalCountSum=0;
			while(res.next()){
				levelName=res.getString("agent_level");
				debug("VT_LevelScore agentLevel:"+agentLevel);
				if(res.getBigDecimal("avgScore")!=null){
					levelScore.put(levelName, Utility.round(res.getString("avgScore"), 2));
					
					levelScoreSum=levelScoreSum.add(res.getBigDecimal("totalPfx"));
					//debug(res.getBigDecimal("totalPfx")+"levelScoreSum:"+levelScoreSum.doubleValue());
					totalCountSum+=res.getInt("totalCount");
					
					
				
					
					StringBuffer sqlTmp=new StringBuffer();
			        sqlTmp.append("replace into VT_LevelScore ");
			        sqlTmp.append("(LevelName,Score,Count,AvgScore,WorkDate)");
			        sqlTmp.append("values (");
			        sqlTmp.append("'");
					sqlTmp.append(levelName);
					sqlTmp.append("'");
					sqlTmp.append(",");
					sqlTmp.append(res.getBigDecimal("totalPfx"));
					sqlTmp.append(",");
					sqlTmp.append(res.getInt("totalCount"));
					sqlTmp.append(",");
					if(levelScore.containsKey(levelName)){
						sqlTmp.append(levelScore.get(levelName));
					}else{
						sqlTmp.append("0");
					}
					
					sqlTmp.append(",");
					sqlTmp.append("'");
					sqlTmp.append(formatDay.format(begin));
					sqlTmp.append("'");
					sqlTmp.append(")");
					sqlExec.add(sqlTmp.toString());
				}
			}
//			if(totalCountSum>0){
				StringBuffer sqlTmp=new StringBuffer();
		        sqlTmp.append("replace into VT_LevelScore ");
		        sqlTmp.append("(LevelName,Score,Count,AvgScore,WorkDate)");
		        sqlTmp.append("values (");
		        sqlTmp.append("'");
		        sqlTmp.append("汇总");
//				sqlTmp.append("����");
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append(levelScoreSum.doubleValue());
				sqlTmp.append(",");
				sqlTmp.append(totalCountSum);
				sqlTmp.append(",");
				if(totalCountSum>0){
					sqlTmp.append(levelScoreSum.divide(new BigDecimal(totalCountSum), 2, BigDecimal.ROUND_HALF_UP));
				}else{
					sqlTmp.append(0);
				}
				//sqlTmp.append(levelScore.get(levelName));
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(formatDay.format(begin));
				sqlTmp.append("'");
				sqlTmp.append(")");
				sqlExec.add(sqlTmp.toString());
//			}
//			sql="SELECT sum(pfx_total* role_percent/100) as totalPfx,count(*) as totalCount," +
//					"sum(pfx_total* role_percent/100)/count(*) as avgScore ,business_type FROM `T_GRADE_DETAIL` a, " +
//					"SYS_USER c,SU_ROLE_LIST d  where record_begin_time " +
//					"BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' and pfx_total<>0 and " +
//					"role_percent<>'0' and c.role_name=d.role_name and user_id=grade_user group by " +
//					" business_type order by avgScore desc ";
				
			sql="SELECT sum(pfx_total) as totalPfx,count(*) as totalCount, " +
					"sum(pfx_total/100)/count(*) as avgScore ,business_type FROM " +
					"`SU_QC_SOURCE_RECORD_DATA` a, suSecurityUser b,suSecurityRole d  where " +
					"begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
					"and pfx_total<>0  and b.`roleName`=d.name  and b.`alias1`=a.`owner` " +
					"group by business_type order by avgScore desc ";
			
	
			res =DAOTools.getSelectRes(sql,con);
			while(res.next()){
				skillName=res.getString("business_type");
				if(res.getBigDecimal("avgScore")!=null){
					
					sqlTmp=new StringBuffer();
			        sqlTmp.append("replace into VT_SkillScore ");
			        sqlTmp.append("(Score,Count,AvgScore,skillGroupID,WorkDate)");
			        sqlTmp.append("values (");
			        sqlTmp.append(res.getBigDecimal("totalPfx"));
					sqlTmp.append(",");
			    	sqlTmp.append(res.getInt("totalCount"));
					sqlTmp.append(",");
					sqlTmp.append(res.getBigDecimal("avgScore"));
					sqlTmp.append(",");
			        sqlTmp.append("'");
					sqlTmp.append(skillName);
					sqlTmp.append("'");
					sqlTmp.append(",");
					sqlTmp.append("'");
					sqlTmp.append(formatDay.format(begin));
					sqlTmp.append("'");
					sqlTmp.append(")");
					sqlExec.add(sqlTmp.toString());
				}
			}
			
//			sql="SELECT sum(pfx_total* role_percent/100) as totalPfx,count(*) as totalCount, " +
//					"sum(pfx_total* role_percent/100)/count(*) as avgScore FROM `T_GRADE_DETAIL` a, " +
//					"SYS_USER c,SU_ROLE_LIST d  where  record_begin_time" +
//					" BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
//					" and pfx_total<>0 and role_percent<>'0' and c.role_name=d.role_name " +
//					"and user_id=grade_user ";
			sql="SELECT sum(pfx_total) as totalPfx,count(*) as totalCount," +
					"sum(pfx_total/100)/count(*) as avgScore FROM `SU_QC_SOURCE_RECORD_DATA` a," +
					" suSecurityUser b,suSecurityRole d  where  begin_time " +
					" BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
					"and pfx_total<>0  and b.`roleName`=d.name  and b.`alias1`=a.`owner` ";
			res =DAOTools.getSelectRes(sql,con);
			while(res.next()){
				avgAllAgentScore=res.getBigDecimal("avgScore");
				if(avgAllAgentScore!=null){
					avgAllAgentScore=Utility.round(avgAllAgentScore.toString(), 2);
				}
			}
			
			
//			sql="SELECT sum(pfx_total* role_percent/100) as totalPfx,count(*) as totalCount," +
//			"sum(pfx_total* role_percent/100)/count(*) as avgScore,skill_name FROM `T_GRADE_DETAIL` a," +
//			"SA_QC_AGENT_PHONE_MAP b,SYS_USER c,SU_ROLE_LIST d  where " +
//			"record_begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'   " +
//			"and pfx_total<>0 and role_percent<>'0' and a.phone_number=b.phone_number and c.role_name=d.role_name " +
//			"and user_id=grade_user group by  skill_name order by avgScore desc";
			
			sql=" SELECT sum(pfx_total) as totalPfx,count(*) as totalCount, " +
					"sum(pfx_total/100)/count(*) as avgScore,f.`alias3` " +
				    "FROM `SU_QC_SOURCE_RECORD_DATA` a, suSecurityUser b,suSecurityRole d ," +
					"suSecurityUser f where begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'  " +
					"and pfx_total<>0  and b.`roleName`=d.name and b.`alias1`=a.`owner` " +
					"and a.`phone_number`=f.`phone_number` group by f.`alias3` order by avgScore desc ";
			
//			sql="  SELECT sum(pfx_total* scoreWeight/100) as totalPfx,count(*) as totalCount, " +
//					"sum(pfx_total* scoreWeight/100)/count(*) as avgScore,b.`alias3` FROM `SU_QC_SOURCE_RECORD_DATA` a," +
//					" suSecurityUser b,suSecurityRole d  where begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
//					" and pfx_total<>0 and scoreWeight<>'0' and b.`roleName`=d.name  and b.`alias1`=a.`owner`" +
//					" group by  b.`alias3` order by avgScore desc ";
			
			debug(sql);
			res =DAOTools.getSelectRes(sql,con);
			phyRank=0;
			while(res.next()){
				skillName=res.getString("alias3");
				skillRank.put(skillName, new Integer(++phyRank));
			}
			
//			sql=" SELECT phone_number, sum(pfx_total* role_percent/100)/count(*) as avgScore" +
//			" FROM `T_GRADE_DETAIL` ,SYS_USER c,SU_ROLE_LIST d  where record_begin_time " +
//			" BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' and pfx_total<>0  " +
//			" and role_percent<>'0' and user_id=grade_user  group by  phone_number order by  avgScore desc";
			sql="  SELECT a.phone_number, sum(pfx_total)/count(*) as avgScore " +
					"FROM `SU_QC_SOURCE_RECORD_DATA` a ,suSecurityUser b,suSecurityRole d  " +
					" where begin_time BETWEEN  '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
					"and pfx_total<>0  and b.`roleName`=d.name  and b.`alias1`=a.`owner` " +
					"group by a.phone_number order by  avgScore desc ";
			debug(sql);
			res =DAOTools.getSelectRes(sql,con);
			phyRank=0;
			while(res.next()){
				extNumber=res.getString("phone_number");
				allRank.put(extNumber, new Integer(++phyRank));
				
				if(res.getBigDecimal("avgScore")!=null){
					allScore.put(extNumber, Utility.round(res.getString("avgScore"), 2));
				}
			}
			
			
//			sql=" SELECT phone_number, sum(pfx_total* role_percent/100)/count(*) as avgScore" +
//			" FROM `T_GRADE_DETAIL` ,SYS_USER c,SU_ROLE_LIST d  where record_begin_time " +
//			" BETWEEN '"+formatDay.format(beginBef)+" 00:00:00"+"' and '"+formatDay.format(endBef)+" 23:59:59"+"' and pfx_total<>0  " +
//			" and role_percent<>'0' and user_id=grade_user group by  phone_number order by  avgScore desc";
			sql="  SELECT a.phone_number, sum(pfx_total)/count(*) as avgScore " +
			"FROM `SU_QC_SOURCE_RECORD_DATA` a ,suSecurityUser b,suSecurityRole d  " +
			" where begin_time BETWEEN  '"+formatDay.format(beginBef)+" 00:00:00"+"' and '"+formatDay.format(endBef)+" 23:59:59"+"' " +
			"and pfx_total<>0  and b.`roleName`=d.name  and b.`alias1`=a.`owner` " +
			"group by a.phone_number order by  avgScore desc ";
			
			debug(sql);
			res =DAOTools.getSelectRes(sql,con);
			phyRank=0;
			while(res.next()){
				extNumber=res.getString("phone_number");
				allRankBef.put(extNumber, new Integer(++phyRank));
				if(res.getBigDecimal("avgScore")!=null){
					allScoreBef.put(extNumber, Utility.round(res.getString("avgScore"), 2));
				}
			}
			
//			sql=" SELECT sum(pfx_total * role_percent/100) as totalPfx,count(*) as totalCount," +
//			"sum(pfx_total* role_percent/100)/count(*) as avgScore ,a.phone_number,csr_id,agent_user," +
//			"skill_name,physics_name,agent_level FROM `T_GRADE_DETAIL` a,SA_QC_AGENT_PHONE_MAP b," +
//			"SYS_USER c,SU_ROLE_LIST d  where record_begin_time " +
//			" BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' and pfx_total<>0 and " +
//			" a.phone_number=b.phone_number and role_percent<>'0' and c.role_name=d.role_name and user_id=grade_user " +
//			" group by  a.phone_number order by physics_name, avgScore desc";
			
//			sql=" SELECT sum(pfx_total * scoreWeight/100) as totalPfx,count(*) as totalCount, " +
//					"sum(pfx_total* scoreWeight/100)/count(*) as avgScore ,a.phone_number,b.`alias1`,b.`name`, " +
//					"b.`alias3`,b.`alias2`,agent_level FROM `SU_QC_SOURCE_RECORD_DATA` a,suSecurityUser " +
//					"b,suSecurityRole d where begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
//					"and pfx_total<>0 and scoreWeight<>'0' and b.`roleName`=d.name and b.`alias1`=a.`owner` " +
//					"group by a.phone_number order by b.`alias3`, avgScore desc";
			
			sql="SELECT sum(pfx_total ) as totalPfx,count(*) as totalCount, " +
					"sum(pfx_total/100)/count(*) as avgScore ,a.phone_number,f.`alias1`,f.`name`, " +
					"f.`alias3`,f.`alias2`,f.agent_level FROM `SU_QC_SOURCE_RECORD_DATA` a,suSecurityUser b," +
					"suSecurityRole d ,suSecurityUser f where begin_time " +
					"BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
					"and pfx_total<>0  and b.`roleName`=d.name and b.`alias1`=a.`owner`" +
					" and a.`phone_number`=f.`phone_number` group by a.phone_number order by f.`alias2`, " +
					"avgScore desc ";
			debug(sql);
			res =DAOTools.getSelectRes(sql,con);
			phyRank=0;
			while(res.next()){
				extNumber=res.getString("phone_number");
				csrId=res.getString("alias1"); //Alias1
				extName=res.getString("name");
				skillName=res.getString("alias3");
				physicsName=res.getString("alias2");
				totalPfx=res.getInt("totalPfx");
				totalCount=res.getInt("totalCount");
				agentLevel=res.getString("agent_level");
				avgScore=res.getBigDecimal("avgScore");
				if(avgScore!=null){
					avgScore=Utility.round(avgScore.toString(), 2);
				}
//				allRank.get(extNumber);
//				allScore.get(extNumber);
//				allRankBef.get(extNumber);
//				allScoreBef.get(extNumber);
				if(physicsNameBef==null){
					++phyRank;
				}else if(physicsNameBef.equals(physicsName)){
					++phyRank;
				}else if(!physicsNameBef.equals(physicsName)){
					phyRank=1;
				}
				
				sqlTmp=new StringBuffer();
				sqlTmp.append("replace into VT_AgentScore ");
				sqlTmp.append("(Name,Level,ExtNumber,Score,Count,AvgScore,SkillGroupID,PhyGroupID," +
						"SkillGroupRank,InsideSkillGroupRank,PhyGroupRank,InsidePhyGroupRank," +
						"AllAgentRank,WorkDate,RankFloating,AvgScoreFloating,Alias1,AvgSameLevelScore,AvgAllAgentScore) ");
				sqlTmp.append("values (");
				sqlTmp.append("'");
				sqlTmp.append(extName);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(agentLevel);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(extNumber);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append(totalPfx);
				sqlTmp.append(",");
				sqlTmp.append(totalCount);
				sqlTmp.append(",");
				if(avgScore!=null){
					sqlTmp.append(avgScore.doubleValue());
				}else{
					sqlTmp.append(0);
				}
				
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(skillName);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(physicsName);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append(skillRank.get(extNumber));
				sqlTmp.append(",");
				sqlTmp.append(skillRank.get(extNumber));
				sqlTmp.append(",");
				sqlTmp.append(physicsRank.get(physicsName));
				sqlTmp.append(",");
				sqlTmp.append(phyRank);
				sqlTmp.append(",");
				sqlTmp.append(allRank.get(extNumber));
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(formatDay.format(begin));
				sqlTmp.append("'");
				sqlTmp.append(",");
				
				if(allRank.get(extNumber)!=null && allRankBef.get(extNumber)!=null){
					sqlTmp.append(allRank.get(extNumber)-allRankBef.get(extNumber));
				}else{
					sqlTmp.append("0");
				}
				sqlTmp.append(",");
				
				if(allScore.get(extNumber)!=null && allScoreBef.get(extNumber)!=null){
//					debug(allScore.get(extNumber)+"_______sub_______"+allScoreBef.get(extNumber)+"______"+allScore.get(extNumber).subtract(allScoreBef.get(extNumber)).doubleValue());
					sqlTmp.append(allScore.get(extNumber).subtract(allScoreBef.get(extNumber)).doubleValue());
				}else{
					sqlTmp.append("0.0");
				}
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(csrId);
				sqlTmp.append("'");
				sqlTmp.append(",");
				if(agentLevel!=null && levelScore.containsKey(agentLevel)){
					sqlTmp.append(levelScore.get(agentLevel).doubleValue());
				}else{
					sqlTmp.append(0);
				}
				sqlTmp.append(",");
				if(avgAllAgentScore!=null){
					sqlTmp.append(avgAllAgentScore.doubleValue());
				}else{
					sqlTmp.append("0");	
				}
				sqlTmp.append(")");
				debug(sqlTmp.toString());
				sqlExec.add(sqlTmp.toString());
				
				physicsNameBef=physicsName;
			}
//			sql="SELECT sum(pfx_total* role_percent/100) as totalPfx,count(*) as totalCount," +
//					"sum(pfx_total* role_percent/100)/count(*) as avgScore,b.agent_level,grade_user,user_name,e.csr_id " +
//					"FROM  `T_GRADE_DETAIL` a, SA_QC_AGENT_PHONE_MAP b,SYS_USER c,SU_ROLE_LIST d ,SA_QC_AGENT_PHONE_MAP e " +
//					"  where record_begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'   " +
//					" and pfx_total<>0 and role_percent<>'0' and a.phone_number=b.phone_number and " +
//					" c.role_name=d.role_name and user_id=grade_user and user_id=e.phone_number group by grade_user, agent_level ";
			
			
//			sql="SELECT sum(pfx_total * d.scoreWeight/100) as totalPfx,count(*) as totalCount, " +
//			"sum(pfx_total* d.scoreWeight/100)/count(*) as avgScore ,a.phone_number,f.`alias1`,f.`name`, " +
//			"f.`alias3`,f.`alias2`,f.agent_level FROM `SU_QC_SOURCE_RECORD_DATA` a,suSecurityUser b," +
//			"suSecurityRole d ,suSecurityUser f where begin_time " +
//			"BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"' " +
//			"and pfx_total<>0 and d.scoreWeight<>'0' and b.`roleName`=d.name and b.`alias1`=a.`owner`" +
//			" and a.`phone_number`=f.`phone_number` group by a.phone_number order by f.`alias2`, " +
//			"avgScore desc ";
			
			sql="SELECT sum(pfx_total) as totalPfx,count(*) as totalCount,  " +
					"sum(pfx_total/100)/count(*) as avgScore,f.agent_level,a.`owner`,b.`alias1`," +
					"b.`name` FROM `SU_QC_SOURCE_RECORD_DATA` a,suSecurityUser b,suSecurityRole d  ,suSecurityUser f  " +
					" where begin_time BETWEEN '"+formatDay.format(begin)+" 00:00:00"+"' and '"+formatDay.format(end)+" 23:59:59"+"'  " +
					"and pfx_total<>0  and b.`roleName`=d.name and b.`alias1`=a.`owner`  " +
					" and a.`phone_number`=f.`phone_number`  group by a.`owner`, f.agent_level ";
			debug(sql);
			res =DAOTools.getSelectRes(sql,con);
			String extBef=null,befCsrId=null,befName=null;
			while(res.next()){
				extNumber=res.getString("owner");
				extName=res.getString("name");
				totalPfx=res.getInt("totalPfx");
				totalCount=res.getInt("totalCount");
				agentLevel=res.getString("agent_level");
				debug(extBef+" VT_UserLevelScore agentLevel:"+agentLevel+" owner: "+extNumber);
				
				avgScore=res.getBigDecimal("avgScore");
				csrId=res.getString("alias1");
//				levelTmp.put(agentLevel, agentLevel);
				if(extBef==null || extBef.equals(extNumber)){
					levelTmp.put(agentLevel, agentLevel);
//					xxxxxxxxxx
				}else{
					for(int i=0;i<allLevel.size();i++){
						if(!levelTmp.containsKey(allLevel.get(i))){
							sqlTmp=new StringBuffer();
							sqlTmp.append("replace into VT_UserLevelScore ");
							sqlTmp.append("(UserName,UserId,LevelName,Score,Count,AvgScore,WorkDate) ");
							sqlTmp.append("values (");
							sqlTmp.append("'");
							sqlTmp.append(befName);
							sqlTmp.append("'");
							sqlTmp.append(",");
							sqlTmp.append("'");
							sqlTmp.append(befCsrId);
							sqlTmp.append("'");
							sqlTmp.append(",");
							sqlTmp.append("'");
							sqlTmp.append(allLevel.get(i));
							sqlTmp.append("'");
							sqlTmp.append(",");
							sqlTmp.append(0);
							sqlTmp.append(",");
							sqlTmp.append(0);
							sqlTmp.append(",");
							sqlTmp.append(0);
							sqlTmp.append(",");
							sqlTmp.append("'");
							sqlTmp.append(formatDay.format(begin));
							sqlTmp.append("'");
							sqlTmp.append(")");
							
							debug(sqlTmp.toString());
							
							sqlExec.add(sqlTmp.toString());
						}
					}
					levelTmp.clear();
					levelTmp.put(agentLevel, agentLevel);
				}
				
				
				sqlTmp=new StringBuffer();
				sqlTmp.append("replace into VT_UserLevelScore ");
				sqlTmp.append("(UserName,UserId,LevelName,Score,Count,AvgScore,WorkDate) ");
				sqlTmp.append("values (");
				sqlTmp.append("'");
				sqlTmp.append(extName);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(csrId);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(agentLevel);
				sqlTmp.append("'");
				sqlTmp.append(",");
				sqlTmp.append(totalPfx);
				sqlTmp.append(",");
				sqlTmp.append(totalCount);
				sqlTmp.append(",");
				if(avgScore!=null){
					sqlTmp.append(avgScore.doubleValue());
				}else{
					sqlTmp.append(0);
				}
				
				sqlTmp.append(",");
				sqlTmp.append("'");
				sqlTmp.append(formatDay.format(begin));
				sqlTmp.append("'");
				sqlTmp.append(")");
				
				debug(sqlTmp.toString());
				
				sqlExec.add(sqlTmp.toString());
				extBef=extNumber;
				befCsrId=csrId;
				befName=extName;
			}
			
			for(int i=0;i<allLevel.size();i++){
				if(!levelTmp.containsKey(allLevel.get(i))){
					sqlTmp=new StringBuffer();
					sqlTmp.append("replace into VT_UserLevelScore ");
					sqlTmp.append("(UserName,UserId,LevelName,Score,Count,AvgScore,WorkDate) ");
					sqlTmp.append("values (");
					sqlTmp.append("'");
					sqlTmp.append(befName);
					sqlTmp.append("'");
					sqlTmp.append(",");
					sqlTmp.append("'");
					sqlTmp.append(befCsrId);
					sqlTmp.append("'");
					sqlTmp.append(",");
					sqlTmp.append("'");
					sqlTmp.append(allLevel.get(i));
					sqlTmp.append("'");
					sqlTmp.append(",");
					sqlTmp.append(0);
					sqlTmp.append(",");
					sqlTmp.append(0);
					sqlTmp.append(",");
					sqlTmp.append(0);
					sqlTmp.append(",");
					sqlTmp.append("'");
					sqlTmp.append(formatDay.format(begin));
					sqlTmp.append("'");
					sqlTmp.append(")");
					
					debug(sqlTmp.toString());
					
					sqlExec.add(sqlTmp.toString());
				}
			}
			
//			for(Iterator iL = levelScore.entrySet().iterator(); iL.hasNext();) {
//		        java.util.Map.Entry eL = (java.util.Map.Entry)iL.next();
//		        levelScoreTmp =(BigDecimal)eL.getValue();
//		        levelName =(String)eL.getKey();
//				
//				
//			}
			
			DAOTools.execBatchS(sqlExec, "ZQC");
		}catch (Exception e) {
			error("during genReport");
			error(e);
		}finally{
			DAOTools.releaseConnectionOutS("ZQC",con);
		}
		
		
		switch (type) {
		case GRANULARITY_WEEK:
			
			
			break;
		case GRANULARITY_MONTH:
			
			
			break;	
		default:
			break;
		}
		
		return ret;
	}*/
	
	public void cleanAndStop(){
		this.myThread=null;
		this.flag=false;
	}
	
	public static void main(String[] args) {
		long time=System.currentTimeMillis();
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(time);
		System.out.println(cal.getActualMaximum(Calendar.DAY_OF_WEEK)+"------------"+cal.get(Calendar.DAY_OF_WEEK));
		System.out.println(cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"------------"+cal.get(Calendar.DAY_OF_MONTH));
//	return cal.getActualMaximum(Calendar.DAY_OF_MONTH)==cal.get(Calendar.DAY_OF_MONTH);
	}
	public volatile boolean isRelease=false;
	public volatile boolean flag = true;
	public volatile boolean hasWeekCal = false;
	public volatile boolean hasMonthCal = false;
//	public  BusyFlag lockx;
	public Thread myThread=null;
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
//	public DAOTools dao;
}
