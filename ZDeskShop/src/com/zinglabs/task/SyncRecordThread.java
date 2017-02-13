package com.zinglabs.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import com.zinglabs.db.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.sun.org.apache.xml.internal.serialize.OutputFormat.DTD;
import com.zinglabs.base.absImpl.BaseAbs;
//import com.zinglabs.util.Ag100Info;


public class SyncRecordThread extends BaseAbs implements Runnable {

	public SyncRecordThread() {
		init(SKIP_Logger);
//		this.ip=proerties.getProperty("db_ip");
//		this.port=Integer.parseInt(proerties.getProperty("RTAPort"));
//		z3000IP=proerties.getProperty("db_ip");
//		extIP=proerties.getProperty("db_ext_ip");
//		this.start=new Timestamp(start.getTime());
//		this.end=new Timestamp(end.getTime());
		this.scopeRecordCount=0;
//		this.pId=1;
		lock = new BusyFlag();
	}
	
	public void run() {
		// TODO Auto-generated method stub
		myThread=Thread.currentThread();
		long nowTime=System.currentTimeMillis();
		long sleepTime=0L;
		while (flag) {
			nowTime=System.currentTimeMillis();
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				error("exception in SyncRecordThread run");
				error(e);
			}
			info("SyncRecordThread begin");
			try {
				if(!isRelease)
					syncDate();
				else
					cleanAndStop();
			} catch (Exception e) {
				// TODO: handle exception
				error("Exception during sync date");
				error(e);
			}
			info("SyncRecordThread end");
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
	
	private void syncDate(){
		Connection con2=null;
		PreparedStatement pstmt2 = null;
		boolean isClean=false;
		long timeTmp=0L,intervalActure=0L,syncBetweenSec=0L;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ArrayList<String> phonePlanList=new ArrayList<String>();
		int state=-1;
		String sql="";
		sql="SELECT sync_end_time FROM SU_QC_PLAN_TIME ";
		String sql2=null;
		ArrayList<Object[]> al=null;
		Object[] resTmp=null;
		Timestamp syncBegin=null,syncEnd=null,syncEndNow=null,syncBeginNow=null,genTime=null;
		al=DAOTools.execSelectS(sql,"ZQC");
		for(int ii=0;ii<al.size();ii++){
			resTmp=al.get(ii);
			syncEndNow=(Timestamp)resTmp[0];
		}
		if(al.size()==0)return;
		
//		if(syncBeginTime==null && !this.flag) return;
//		else if(syncBeginTime==null){
//			LogUtil.warn("exception syncDate SU_QC_PLAN_TIME null plan id is:"+pId);
//			return;
//		}
		
		intervalActure=System.currentTimeMillis();
		
//		if(this.startTmp==null || this.endTmp.getTime()==this.end.getTime() || this.start.getTime()==this.startTmp.getTime()){}
		debug(startTmp+"-------------"+intervalActure+"------------"+syncEndNow);
//		this.lock.getBusyFlag();
		
		if(intervalActure<=syncEndNow.getTime() || intervalActure-syncEndNow.getTime()<600000L){
			debug(" plan now sync time  <600000L");
			try {
				Thread.currentThread().sleep(intervalActure-syncEndNow.getTime());
			} catch (InterruptedException e) {
				error(e);
			}
			return;
		}
		
		syncBetweenSec=intervalActure-syncEndNow.getTime()>QC_DEFULT_SYNC_INTERVAL?QC_DEFULT_SYNC_INTERVAL:intervalActure-syncEndNow.getTime();
		syncBegin=new Timestamp(syncEndNow.getTime());
		syncEnd=new Timestamp(syncEndNow.getTime()+syncBetweenSec);

//		this.lock.freeBusyFlag();
		
		ArrayList<OrsInfo> alOrs=null;
		ArrayList<Ag100Info> alAg100=null;
		String typeName=null,localIp=null;
		ResultSet res=null;
		ResultSet res1=null;
//		Connection con=null;
		RecordData rd = null;
		String fileName=null;
		String sqlTmp=null;
		ArrayList<String> sqlExec=new ArrayList<String>();
		if (!isClean) {
			try {
				alOrs = SSCDBTools.getOrsIPs();
			} catch (Exception e) {
				error("Exception in syncDate ");
				error(e);
			} 
			
			try {
				alAg100 = SSCDBTools.getAg100IPs();
			} catch (Exception e) {
				error("Exception in syncDate ");
				error(e);
			} 
			
			try {
				sqlExec.clear();
				con2 = DAOTools.getConnectionOutS("ZQC");
				sql2 = "select id,generate_time,file_name from SU_QC_SOURCE_RECORD_DATA  where  generate_time >='" + f.format(syncBegin)
						+ "' and generate_time <'" + f.format(syncEnd) + "'";
				debug(sql2);
				al=DAOTools.execSelectS(sql2,"ZQC");
				for(int ii=0;ii<al.size();ii++){
					resTmp=al.get(ii);
					fileName=String.valueOf(resTmp[2]);
					debug("Exc sync record has data bef id is :"
							+ String.valueOf(resTmp[0]) + " generate_time is :"+ (Timestamp)resTmp[1]+
							" file_name:"+String.valueOf(resTmp[2]));
					sqlTmp="delete from T_GRADE_DETAIL_NEW where file_name='"+fileName+"'";
					sqlExec.add(sqlTmp);
				}
				
				sql2 = "delete from SU_QC_SOURCE_RECORD_DATA where  generate_time >='" + f.format(syncBegin)
						+ "' and generate_time <'" + f.format(syncEnd) + "' and is_mp3='"+QC_FILE_TYPE_MP3+"' ";
				DAOTools.execUpdateS(sql2, "ZQC");
				DAOTools.execBatchS(sqlExec, "ZQC",false);
				
				sql2 = "insert into SU_QC_SOURCE_RECORD_DATA(plan_id,srn_name,channel,"
						+ "begin_time,end_time,call_number,caller_number,phone_number,"
						+ "channel_state,file_exist,file_size,record_length,associated_sg,"
						+ "file_name,file_location,generate_time,business_type,alias1,alias2,alias3,is_mp3,call_type,ddh,ywlx,jqbdh,shybdh,jqtbdh,shytbdh,cbh,dhgsd)"
						+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?)";
				pstmt2 = con2.prepareStatement(sql2);

				if (alOrs.size() > 0 && ORS_TYPE_COMMON.equals(ConfInfo.confMapDBConf.get("procMode"))) { // use ors -- record will be remove from
																							// ag100 by ors so sync ors only
					for (int i = 0; i < alOrs.size(); i++) {
						debug("--------ors---ip:"+ DAOTools.getDyConName(alOrs.get(i).ip));
						// recordFromAg100OrOrs(String ip,String type,Timestamp
						// syncBegin,Timestamp syncEnd)
						state=recordFromAg100OrOrs(alOrs.get(i).ip, "ors",syncBegin, syncEnd, pstmt2,alOrs.get(i).ip);
						if (state==QC_STATE_BREAK) {
							debug("time <600000L in recordFromAg100OrOrs ors ip is "+ alOrs.get(i).ip+ " beginTIme is "+ syncBegin+ "endTime is " + syncEnd);
							continue;
						}else if(state==QC_STATE_OK){
							debug("query ok in recordFromAg100OrOrs ors ip is "+ alOrs.get(i).ip+ " beginTIme is "+ syncBegin+ "endTime is " + syncEnd);
//							break;
						}else if(state==QC_STATE_FAILED){
							debug("query failed ors in recordFromAg100OrOrs ors ip is "+ alOrs.get(i).ip+ " beginTIme is "+ syncBegin+ "endTime is " + syncEnd);
						}
					}
				} else if(alAg100.size()>0) {
					for (int i = 0; i < alAg100.size(); i++) {
						debug("-------ag100----ip:"+ DAOTools.getDyConName(alAg100.get(i).ip));
						if (ORS_TYPE_COMMON.equals(ConfInfo.confMapDBConf.get("procMode")) ) {
							state=recordFromAg100OrOrs(alAg100.get(i).ip, "z3000",syncBegin, syncEnd, pstmt2,alAg100.get(i).ip);
							if (state==QC_STATE_BREAK) {
								debug("time <600000L in recordFromAg100OrOrs ag100 ip is "+ alAg100.get(i).ip+ " beginTIme is "+ syncBegin+ "endTime is " + syncEnd);
								continue;
							}else if(state==QC_STATE_OK){
								debug("query ok in recordFromAg100OrOrs ag100 ip is "+ alAg100.get(i).ip+ " beginTIme is "+ syncBegin+ "endTime is " + syncEnd);
//								break;
							}else if(state==QC_STATE_FAILED){
								debug("query failed ag100 in recordFromAg100OrOrs ag100 ip is "+ alAg100.get(i).ip+ " beginTIme is "+ syncBegin+ "endTime is " + syncEnd);
							}
//							debug("Exception in recordFromAg100OrOrs ag100 ip is "+ alAg100.get(i).ip+ " beginTIme is "+ syncBegin+ "endTime is " + syncEnd);
						}else{
							Iterator<OrsInfo> ii =alOrs.iterator();
							while(ii.hasNext()){
								localIp=ii.next().ip;
								state=recordFromAg100OrOrs(alAg100.get(i).ip, "z3000",syncBegin, syncEnd, pstmt2,localIp);
								if (state==QC_STATE_BREAK) {
									debug("time <600000L in recordFromAg100OrOrs ag100(abc) ip is "+ alAg100.get(i).ip+ " beginTIme is "+ syncBegin+ "endTime is " + syncEnd);
									continue;
								}else if(state==QC_STATE_OK){
									debug("query ok in recordFromAg100OrOrs ag100(abc) ip is "+ alAg100.get(i).ip+ " beginTIme is "+ syncBegin+ "endTime is " + syncEnd);
									break;
								}else if(state==QC_STATE_FAILED){
									debug("query failed ors in recordFromAg100OrOrs ag100(abc) ip is "+ alAg100.get(i).ip+ " beginTIme is "+ syncBegin+ "endTime is " + syncEnd);
								}
//								if(){
//									break;
//								}else{
//									debug("Exception in recordFromAg100OrOrs ag100(abc) ip is "+ alAg100.get(i).ip+ " beginTIme is "+ syncBegin+ "endTime is " + syncEnd +" local ip is "+localIp);
//								}
							}
						}
					}
				}
			} catch (Exception e1) {
				error("Exception in syncDate ");
				error(e1);
			} finally {
				DAOTools.releaseConnectionOutS("ZQC", con2);
			}
		} else {
			//11111111111111
			clearReocrd(syncBegin, syncEnd);
		}
		
		StringBuffer sqlNotIn=new StringBuffer();
		String phoneNum=null;
		try {
//			debug(sql);
			
//			sql="delete from SU_QC_SOURCE_RECORD_DATA where generate_time <'"+f.format(syncBeginNow)+"' and generate_time >= '"+f.format(syncEndNow)+"'";
//			DAOTools.execUpdateS(sql,"ZQC");
			
//			String sql = "insert into T_PHONES_BY_RECORD(phone_number,plan_id) select distinct(phone_number),"+planid+" from SU_QC_SOURCE_RECORD_DATA where plan_id="+planid;
			
//			sql="REPLACE into SU_QC_PLAN_TIME (sync_begin_time,sync_end_time,generate_time) values(NOW(),'"+f.format(syncEndNow)+"',NOW()) ";
//			sql="update SU_QC_PLAN_TIME set sync_begin_time=NOW(),sync_end_time='"+f.format(syncEnd)+"',generate_time=NOW() ";
			sql="update SU_QC_PLAN_TIME set generate_time=NOW(),sync_end_time='"+f.format(syncEnd)+"',sync_begin_time=NOW()";
			DAOTools.execUpdateS(sql,"ZQC");

		}catch (Exception e) {
			error("Exception in update SU_QC_PLAN_TIME");
			error(e);
		}
	}
	
	
	public boolean clearReocrd(Timestamp delBegin, Timestamp delEnd) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "delete from SU_QC_SOURCE_RECORD_DATA where  generate_time >='" + f.format(delBegin)
				+ "' and generate_time <'" + f.format(delEnd) + "' and is_mp3='"+QC_FILE_TYPE_MP3+"'";
		try {
			DAOTools.execUpdateS(sql, "ZQC");
		} catch (Exception e1) {
			error("Exception in data clean ");
			error(e1);
		} 
		return true;
	}

	
	public int recordFromAg100OrOrs(String ip,String type,Timestamp syncBegin,Timestamp syncEnd,PreparedStatement pstmt2,String localIp){
		Connection con=null;
		ResultSet res=null;
		String sql=null;
		String sql2=null,typeName=null;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp genTime = null;
		RecordData rd = null;
		PreparedStatement pstmt=null;
		try {
			
			typeName = DAOTools.getDyConName(ip);
			debug(type+"-----"+typeName+"------ip:" + DAOTools.getDyConName(ip));
			DAOTools.insertDYInfo("zinglabs", "12", ip, type, "3306", "mysql",typeName);
			con = DAOTools.getConnectionOutS(typeName);
			
//			sql = "select generate_time from SA_SRN_RECORD order by generate_time desc limit 1";
//			res = dao.execSelect(sql, con);
//			if (res.next()) {
//				genTime = res.getTimestamp("generate_time");
//				if (genTime.getTime()-syncEnd.getTime() < 600000L){
//					debug(syncEnd+"----------exc time < 600000L-------------"+genTime);
//					return QC_STATE_BREAK;
//				}
//			} else {
//				debug("----------exc no data-------------");
//				return QC_STATE_FAILED;
//			}

			sql = "select *,(unix_timestamp(end_time) -unix_timestamp(begin_time)) as length "
					+ "from SA_SRN_RECORD where generate_time >= '"
					+ f.format(syncBegin)
					+ "' and generate_time < '"
					+ f.format(syncEnd) + "'";
			pstmt=con.prepareStatement(sql);
			res = pstmt.executeQuery();
			while (res.next()) {
				rd = new RecordData();
				rd.plan_id = 1;
				rd.srn_name = res.getString("srn_name");
				rd.channel = res.getInt("channel");
				rd.begin_time = res.getString("begin_time");
				rd.end_time = res.getString("end_time");
				rd.call_number = res.getString("call_number");
				rd.caller_number = res.getString("caller_number");
				rd.phone_number = res.getString("phone_number");
				rd.channel_state = res.getInt("channel_state");
				rd.file_exist = res.getInt("file_exist");
				rd.file_size = res.getInt("file_size");
				rd.record_length = res.getInt("length");
				rd.associated_sg = "";
				rd.file_name = res.getString("filename");
				rd.file_location = localIp;
				rd.generate_time = res.getString("generate_time");
				rd.business_type = "";
				rd.isMp3=QC_FILE_TYPE_MP3;
				rd.call_type = res.getString("call_type");
//				rd.ywlx = res.getString("ywlx");
				rd.ddh = res.getString("ddh");

				rd.ywlx = res.getString("ywlx");
				rd.jqbdh = res.getString("jqbdh");
				rd.shybdh = res.getString("shybdh");
				rd.jqtbdh = res.getString("jqtbdh");
				rd.shytbdh = res.getString("shytbdh");
				rd.cbh = res.getString("cbh");
				rd.dhgsd = res.getString("dhgsd");
				
//				rd.jqbdh = res.getString("jqbdh");
//				rd.shybdh = res.getString("shybdh");
//				rd.jqtbdh = res.getString("jqtbdh");
//				rd.shytbdh = res.getString("shytbdh");
//				rd.cbh = res.getString("cbh");
//				rd.dhgsd = res.getString("dhgsd");
				debug(rd.phone_number+" load rd caller_number:"+rd.caller_number);
				SSCDBTools.loadRecordSgInfo(rd);
				DAOTools.setRecordData(pstmt2, rd);
				// if(pstmt2==null){
				// pstmt2=con2.prepareStatement(sql2);
				// }
			}
		} catch (Exception e) {
			error("Exception in recordFromAg100OrOrs "+ip);
			error(e);
		} finally {
			DAOTools.releaseConnectionOutS(typeName, con);
		}

		return QC_STATE_OK;
	}
	
	public void releaseAll(){
		isRelease=true;
	}
	
	public void cleanAndStop(){
//		Connection con=null;
//		ResultSet res=null;
//		String sql="SELECT count(*) FROM `SU_QC_SOURCE_RECORD_DATA` where plan_id=";
//		try {
//			con=DAOTools.getConnectionOutS("z3000_qc");
//			res =dao.execSelect(sql,con);
//			while(res.next()){
//				
//			}
//		}catch (Exception e) {
//			debug("Exception in syncDate ");
//			debug(e);
//		}finally{
//			DAOTools.releaseConnectionOutS("z3000_qc",con);
//		}
		
		this.myThread=null;
		this.flag=false;
	}
	
	public volatile boolean flag = true;
	
	public volatile boolean isRelease=false;

//	private final LogUtil LogUtil = LogUtil.getLogUtil(this.getClass().getName());
	
//	public static String z3000IP = "192.168.0.167";
//	public static String extIP = "192.168.0.167";
//	
	
//	public Timestamp start=null;
//	public Timestamp end=null;
//	
	public volatile Timestamp startTmp=null;
	public volatile Timestamp endTmp=null;
	
	public int scopeRecordCount;

	public Thread myThread=null;
//	private Integer pId=null;
	
	public  BusyFlag lock;
	
	private static final int QC_STATE_OK=0;
	private static final int QC_STATE_BREAK=1;
	private static final int QC_STATE_FAILED=0;
	private static final String QC_FILE_TYPE_MP3="mp3";
	private static final String QC_FILE_TYPE_TXT="txt";
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
}
