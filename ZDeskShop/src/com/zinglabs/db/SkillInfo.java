package com.zinglabs.db;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


import com.zinglabs.base.absImpl.BaseAbs;

public class SkillInfo extends BaseAbs{
	
	public SkillInfo() throws Exception{
		if(ConfInfo.minGranularity==0){
			ConfInfo.minGranularity=GRANULARITY_HALF_HOUR;
		}
//		this.skillGranularity=new ConcurrentHashMap<Integer, SkillGranularity>();
//		this.skillAgent=new ConcurrentHashMap<String, AgentInfo>();
//		this.timeBegin=new Timestamp(begTime.getTime());
//		clear(begTime);
        SSCDBTools.defultValue(this);
		if("true".equals(ConfInfo.confMapDBConf.get("isSecArr"))){
			SKIP_secArr=new int[86401];
			for(int i=0;i<SKIP_secArr.length;i++){
				SKIP_secArr[i]=0;
			}
		}
	}

	public void init(Timestamp begTime) throws Exception{
//		DAOTools.defultValue(this);
		this.timeBegin=new Timestamp(begTime.getTime());
		SkillGranularity granularityTmp=null;
		String aggregaeTmp=null;
		if(ConfInfo.minGranularity==GRANULARITY_HALF_HOUR){
			for(int i=0;i<48;i++){
				granularityTmp=new SkillGranularity();
				granularityTmp.skill$granularityBeginTime=new Timestamp(this.timeBegin.getTime()+i*1800000L);
				granularityTmp.skill$granularityEndTime=new Timestamp(this.timeBegin.getTime()+(i+1)*1800000L);
				granularityTmp.skill$Name=this.SkillInfo$Name;
				granularityTmp.upDateTime=new Timestamp(System.currentTimeMillis());
				granularityTmp.upDBTime=new Timestamp(System.currentTimeMillis());
				this.skillGranularity.put(i, granularityTmp);
			}
		}else if(ConfInfo.minGranularity==GRANULARITY_QUARTER){
			for(int i=0;i<96;i++){
				this.skillGranularity.put(i, new SkillGranularity());
			}
		}
		
		// here use for reset
		
		if(this.aggregaeMap!=null && this.aggregaeMap.size()>0){
			for(Iterator ia=this.aggregaeMap.entrySet().iterator();ia.hasNext();){
    			java.util.Map.Entry eAggregae=(java.util.Map.Entry)ia.next();
    			granularityTmp=(SkillGranularity)eAggregae.getValue();
    			aggregaeTmp=(String)eAggregae.getKey();
                SSCDBTools.defultValue(granularityTmp);
//    			granularityTmp.granularityBeginTime=new Timestamp(this.timeBegin.getTime()+i*1800000L);
//				granularityTmp.granularityEndTime=new Timestamp(this.timeBegin.getTime()+(i+1)*1800000L);
//    			time can set by data granularity
    			if(aggregaeTmp.startsWith("day$skill$")){
					granularityTmp.skill$granularityBeginTime=new Timestamp(begTime.getTime());
					granularityTmp.skill$granularityEndTime=new Timestamp(begTime.getTime()+86400000l);
					granularityTmp.upDateTime=new Timestamp(System.currentTimeMillis());
					granularityTmp.upDBTime=new Timestamp(System.currentTimeMillis());
				}
    			
				granularityTmp.skill$Name=this.SkillInfo$Name;
			}
		}else{
			this.aggregaeMap=new HashMap<String, SkillGranularity>();
			for(Iterator<String> ia=SkillInfo.SKIP_granularitySet.iterator();ia.hasNext();){
				aggregaeTmp=ia.next();
				
				if(aggregaeTmp.startsWith("day$skill$")){
					granularityTmp=new SkillGranularity();
					granularityTmp.skill$granularityBeginTime=new Timestamp(begTime.getTime());
					granularityTmp.skill$granularityEndTime=new Timestamp(begTime.getTime()+86400000l);
					granularityTmp.upDateTime=new Timestamp(System.currentTimeMillis());
					granularityTmp.upDBTime=new Timestamp(System.currentTimeMillis());
				}
				granularityTmp.skill$Name=this.SkillInfo$Name;
				
				
				if(granularityTmp!=null)
					this.aggregaeMap.put(aggregaeTmp, granularityTmp);
				
//				System.out.println(this.aggregaeMap.get(aggregaeTmp).skill$granularityBeginTime+"-----------!!!!!!!!!!!!!---------"+aggregaeTmp);
			}
		}
		
	}
	
	public static void init(){
		SkillInfo.SKIP_allSkillName=new CopyOnWriteArraySet<String>();
		SkillInfo.SKIP_skillMap=new ConcurrentHashMap<String, SkillInfo>();
	}
	
	public String SkillInfo$Alias1;
	
	public String SkillInfo$Alias2;
	
	public String SkillInfo$Alias3;
	
	public String SkillInfo$Alias4;
	
	public String SkillInfo$Alias5;
	
	public String SkillInfo$Alias6;
	
	public String SkillInfo$Alias7;
	
	public String SkillInfo$Alias8;
	
	public String SkillInfo$Alias9;
	
	public String SkillInfo$Alias10;
	
	public String SkillInfo$Name;
	
	public String skillSumId;
	
	public int SkillInfo$onlineAgentCount;
	
	public int SkillInfo$dayLoginAgentCount;            //  不重复的 当日最大座席登录数据 
	
	public Timestamp timeBegin;
	
	public ConcurrentHashMap<Integer,SkillGranularity> skillGranularity;
	
	public HashMap<String, SkillGranularity> aggregaeMap;
	
	public ConcurrentHashMap<String ,AgentInfo> skillAgent;
	
	public static HashSet<String> SKIP_granularitySet;
	
	public static ConcurrentHashMap<String ,SkillInfo> SKIP_skillMap;
	
	public static CopyOnWriteArraySet<String> SKIP_allSkillName;
	
	public int[] SKIP_secArr;
	
	public BusyFlag lock;	
//	public static Logger SKIP_Logger=Logger.getLogger(SkillInfo.class);
}
