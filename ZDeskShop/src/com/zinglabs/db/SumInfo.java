package com.zinglabs.db;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.zinglabs.base.absImpl.BaseAbs;

public class SumInfo extends BaseAbs {
	
	public SumInfo() throws Exception{
		if(ConfInfo.minGranularity==0){
			ConfInfo.minGranularity=GRANULARITY_HALF_HOUR;
		}
		DAOTools.defultValue(this);
		SumInfo$Departcode="0150000000";
	}
	
	public void init(Timestamp begTime) throws Exception{
//		DAOTools.defultValue(this);
		this.timeBegin=new Timestamp(begTime.getTime());
		SumGranularity granularityTmp=null;
		String aggregaeTmp=null;
		if(ConfInfo.minGranularity==GRANULARITY_HALF_HOUR){
			for(int i=0;i<48;i++){
				granularityTmp=new SumGranularity();
				granularityTmp.sum$granularityBeginTime=new Timestamp(this.timeBegin.getTime()+i*1800000L);
				granularityTmp.sum$granularityEndTime=new Timestamp(this.timeBegin.getTime()+(i+1)*1800000L);
				granularityTmp.upDateTime=new Timestamp(System.currentTimeMillis());
				granularityTmp.upDBTime=new Timestamp(System.currentTimeMillis());
				this.sumGranularity.put(i, granularityTmp);
			}
		}else if(ConfInfo.minGranularity==GRANULARITY_QUARTER){
			for(int i=0;i<96;i++){
				this.sumGranularity.put(i, new SumGranularity());
			}
		}
		
		// here use for reset
		
		if(this.aggregaeMap!=null && this.aggregaeMap.size()>0){
			for(Iterator ia=this.aggregaeMap.entrySet().iterator();ia.hasNext();){
    			java.util.Map.Entry eAggregae=(java.util.Map.Entry)ia.next();
    			granularityTmp=(SumGranularity)eAggregae.getValue();
    			aggregaeTmp=(String)eAggregae.getKey();
    			DAOTools.defultValue(granularityTmp);
//    			granularityTmp.granularityBeginTime=new Timestamp(this.timeBegin.getTime()+i*1800000L);
//				granularityTmp.granularityEndTime=new Timestamp(this.timeBegin.getTime()+(i+1)*1800000L);
//    			time can set by data granularity
    			if(aggregaeTmp.startsWith("day$sum$")){
					granularityTmp.sum$granularityBeginTime=new Timestamp(begTime.getTime());
					granularityTmp.sum$granularityEndTime=new Timestamp(begTime.getTime()+86400000l);
					granularityTmp.upDateTime=new Timestamp(System.currentTimeMillis());
					granularityTmp.upDBTime=new Timestamp(System.currentTimeMillis());
				}
			}
		}else{
			this.aggregaeMap=new HashMap<String, SumGranularity>();
			for(Iterator<String> ia=SumInfo.SKIP_granularitySet.iterator();ia.hasNext();){
				aggregaeTmp=ia.next();
				if(aggregaeTmp.startsWith("day$sum$")){
					granularityTmp=new SumGranularity();
					granularityTmp.sum$granularityBeginTime=new Timestamp(begTime.getTime());
					granularityTmp.sum$granularityEndTime=new Timestamp(begTime.getTime()+86400000l);
					granularityTmp.upDateTime=new Timestamp(System.currentTimeMillis());
					granularityTmp.upDBTime=new Timestamp(System.currentTimeMillis());
					
				}
				if(granularityTmp!=null)
					this.aggregaeMap.put(aggregaeTmp, granularityTmp);
				
//				System.out.println(this.aggregaeMap.get(aggregaeTmp).skill$granularityBeginTime+"-----------!!!!!!!!!!!!!---------"+aggregaeTmp);
			}
		}
		
	}
	
	public static void init(){
		SumInfo.SKIP_allSumName=new CopyOnWriteArraySet<String>();
		SumInfo.SKIP_sumMap=new ConcurrentHashMap<String, SumInfo>();
	}
	
	public String SumInfo$Name;

	public String SumInfo$Alias1;
	
	public String SumInfo$Alias2;
	
	public String SumInfo$Alias3;
	
	public String SumInfo$Alias4;
	
	public String SumInfo$Alias5;
	
	public String SumInfo$Alias6;
	
	public String SumInfo$Alias7;
	
	public String SumInfo$Alias8;
	
	public String SumInfo$Alias9;
	
	public String SumInfo$Alias10;
	
	public String SumInfo$Departcode;
	
	public Timestamp timeBegin;
	
	public ConcurrentHashMap<Integer,SumGranularity> sumGranularity;
	
	public static HashSet<String> SKIP_granularitySet;
	
	public HashMap<String, SumGranularity> aggregaeMap;
	
	public static ConcurrentHashMap<String ,SumInfo> SKIP_sumMap;
	
	public static CopyOnWriteArraySet<String> SKIP_allSumName;
	
}
