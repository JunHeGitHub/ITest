package com.zinglabs.db;

import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author chuan.li
 * @desc  will process by AddObjData  if there any map value use String key only by now
 */
public class SumGranularity {
	
	public SumGranularity() throws Exception{
        SSCDBTools.defultValue(this);
	}

	public Timestamp upDateTime;  // one self update once
	
	public Timestamp upDBTime;
	
	public Timestamp sum$granularityBeginTime;
	
	public Timestamp sum$granularityEndTime;
	
	public BusyFlag lock;	 // when this lock occur performance problem  can be use thread copy
	
	public long sum$inboundCount;								//	来电数                          
	public long sum$inboundAccCount;             //  接听客户电话次数                
	public long sum$shortAbandCount;             //  短时放弃量      
	public long sum$outboundCount;               //  外拨数  1:n     need no skill add                         
	public long sum$outboundSuccCount;           //  外拨接通数  1:n     need no skill add         
	
	public long sum$transCenterIVRCount;                 //IVR转其它中心数                    
	public long sum$transCenterIVRAccCount;           //  IVR转其它中心接听数
	
	public String sumId;
//	public String sumTmp;
	
	public ConcurrentHashMap<String ,SkillGranularity> sum$map$skillGranularity;
	
	public ConcurrentHashMap<String ,SkillGranularity> sum$map$skillAggregate;
	
//	public ConcurrentHashMap<String ,AgentGranularity> agentGranularity;
	
}
