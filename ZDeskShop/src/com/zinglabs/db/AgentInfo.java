package com.zinglabs.db;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.absImpl.BaseAbs;

public class AgentInfo extends BaseAbs {
	
	public AgentInfo() throws Exception{
		init(SKIP_Logger);
		if(ConfInfo.minGranularity==0){
			ConfInfo.minGranularity=GRANULARITY_HALF_HOUR;
		}
//		this.agentGranularity=new ConcurrentHashMap<Integer, AgentGranularity>();
//		this.skillGroup=new CopyOnWriteArraySet<String>();
//		this.agentSkill=new CopyOnWriteArraySet<SkillInfo>();
//		this.timeBegin=new Timestamp(begTime.getTime());
//		clear(begTime);
        SSCDBTools.defultValue(this);
	}
	
	public void init(Timestamp begTime) throws Exception{
		
		this.timeBegin=new Timestamp(begTime.getTime());
		AgentGranularity granularityTmp=null;
		String aggregaeTmp=null;
		if(ConfInfo.minGranularity==GRANULARITY_HALF_HOUR){
			for(int i=0;i<48;i++){
				granularityTmp=new AgentGranularity();
				granularityTmp.agent$granularityBeginTime=new Timestamp(this.timeBegin.getTime()+i*1800000L);
				granularityTmp.agent$granularityEndTime=new Timestamp(this.timeBegin.getTime()+(i+1)*1800000L);
				granularityTmp.agent$Num=this.AgentInfo$Num;
				granularityTmp.upDateTime=new Timestamp(System.currentTimeMillis());
				granularityTmp.upDBTime=new Timestamp(System.currentTimeMillis());
				this.agentGranularity.put(i, granularityTmp);
			}
		}else if(ConfInfo.minGranularity==GRANULARITY_QUARTER){
			for(int i=0;i<96;i++){
				this.agentGranularity.put(i, new AgentGranularity());
			}
		}
		
		if(this.aggregaeMap!=null && this.aggregaeMap.size()>0){
			for(Iterator ia=this.aggregaeMap.entrySet().iterator();ia.hasNext();){
    			java.util.Map.Entry eAggregae=(java.util.Map.Entry)ia.next();
    			granularityTmp=(AgentGranularity)eAggregae.getValue();
    			aggregaeTmp=(String)eAggregae.getKey();
                SSCDBTools.defultValue(granularityTmp);
//    			granularityTmp.granularityBeginTime=new Timestamp(this.timeBegin.getTime()+i*1800000L);
//				granularityTmp.granularityEndTime=new Timestamp(this.timeBegin.getTime()+(i+1)*1800000L);
//    			time can set by data granularity
    			if(aggregaeTmp.startsWith("day$agent$")){
					granularityTmp.agent$granularityBeginTime=new Timestamp(begTime.getTime());
					granularityTmp.agent$granularityEndTime=new Timestamp(begTime.getTime()+86400000l);
					granularityTmp.upDateTime=new Timestamp(System.currentTimeMillis());
					granularityTmp.upDBTime=new Timestamp(System.currentTimeMillis());
				}
				granularityTmp.agent$Num=this.AgentInfo$Num;
			}
		}else{
			this.aggregaeMap=new HashMap<String, AgentGranularity>();
			for(Iterator<String> ia=AgentInfo.SKIP_granularitySet.iterator();ia.hasNext();){
				aggregaeTmp=ia.next();
				if(aggregaeTmp.startsWith("day$agent$")){
					granularityTmp=new AgentGranularity();
					granularityTmp.agent$granularityBeginTime=new Timestamp(begTime.getTime());
					granularityTmp.agent$granularityEndTime=new Timestamp(begTime.getTime()+86400000l);
					granularityTmp.upDateTime=new Timestamp(System.currentTimeMillis());
					granularityTmp.upDBTime=new Timestamp(System.currentTimeMillis());
				}
				granularityTmp.agent$Num=this.AgentInfo$Num;
				if(granularityTmp!=null)
					this.aggregaeMap.put(aggregaeTmp, granularityTmp);
			}
		}
	}
	
	public static void init(){
		
		AgentInfo.SKIP_allPhone=new CopyOnWriteArraySet<String>();
		AgentInfo.SKIP_agentMap=new ConcurrentHashMap<String, AgentInfo>();
	}
	
	public  CopyOnWriteArraySet<SkillInfo> agentSkill;
	
	public CopyOnWriteArraySet<String> AgentInfo$SkillGroup;
	
	/**
	 * alias is use for customer sys info like PhysicGroup departCode
	 */
	public String AgentInfo$Alias1;
	
	public String AgentInfo$Alias2;
	
	public String AgentInfo$Alias3;
	
	public String AgentInfo$Alias4;
	
	public String AgentInfo$Alias5;
	
	public String AgentInfo$Alias6;
	
	public String AgentInfo$Alias7;
	
	public String AgentInfo$Alias8;
	
	public String AgentInfo$Alias9;
	
	public String AgentInfo$Alias10;
	
	public String AgentInfo$SkillName;
	
	public String AgentInfo$Name;
	
	public String AgentInfo$Num;
	
	public String AgentInfo$AgentLevel;
	
	public String AgentInfo$roleNameAbc;
	
	public String AgentInfo$organizationCodeAbc;
	
	public String agentSumId;
	
	public Timestamp AgentInfo$minLoginTime;
	
	public Timestamp AgentInfo$maxLogoutTime;
	
	public ConcurrentHashMap<Integer,AgentGranularity> agentGranularity;
	
	public HashMap<String, AgentGranularity> aggregaeMap;
	
//	public AgentGranularity agentAggregae;
	
	public int phoneDuty;
	
	public int channel;
	
	public String srnIp;
	
	public Timestamp timeBegin;
	
	public static HashSet<String> SKIP_granularitySet;  //day$agent$  day$skill$ week$skill month$skill etc base on minGranularity 
	
	public static CopyOnWriteArraySet<String> SKIP_allPhone;
	
	public static ConcurrentHashMap<String ,AgentInfo> SKIP_agentMap;
	
//	public static int minGranularity=0;
	
//	for agent state
	
	public int agentState;              //now agent state  if agent logout bef day it is 5 if agent did not login it is 0
	
	public int duration;               // now agent state duration
	
	public Timestamp stateBeginTime;
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
	public boolean hasLogin;
}
