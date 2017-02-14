package com.zinglabs.db;

import java.sql.Timestamp;

/**
 * 
 * @author chuan.li
 * @desc 
 *  rule of fieldName : time granularity(like halfHour,hour,day,week ...)$(separator)calc obj(like agent,skill,realtime ...)$(separator)dataType(like baseCti,AgentInfo,SkillInfo ....)$(separator) fieldName
 * will process by AddObjData  if there any map value use String key only by now
 */
public class AgentGranularity {
	
	public AgentGranularity() throws Exception{
		SSCDBTools.defultValue(this);
	}
	
	public Timestamp upDateTime;   //use for detect this record need update mem data up time
	
	public Timestamp upDBTime;	//use for detect this record need update  db data up time
	
	public Timestamp agent$granularityBeginTime;
	
	public Timestamp agent$granularityEndTime;
	
	public String agent$Num;
	
	public BusyFlag lock;	 // when this lock occur performance problem  can be use thread copy
	public long agent$inboundCount;							//	来电数                             
	public long agent$inboundAccCount;			//接听客户电话次数                       
	public long agent$inboundAccTime;            //  接听客户电话时间 不包括会议通话时间  
	public long agent$accTelCount15;             //  15秒内接起的电话总量            
	public long agent$accTelCount20;             //  20秒内接起的电话总量            
	public long agent$transAccCount;             //  转接数
	public long agent$threeTalkCount;            //  三方通话量
	public long agent$threeTalkTime;            //  三方通话时间_发起      
	public long agent$holdCount;                 //  持线次数                        
	public long agent$holdTime;                  //  持线时间                        
	public long agent$beThreeTalkCount;          //  被三方通话次数                  
	public long agent$beThreeTalkTime;           //  被三方通话时间                  
	public long agent$innerCallTime;             //  内呼时间                        
	public long agent$innerCount;                //  内呼数                          
	public long agent$beTransAccTime;            //  被转接应答时间                  
	public long agent$beTransAccCount;           //  被转接应答次数                  
	public long agent$threeTalkACWCount;         //  三方通话事后处理次数
	public long agent$threeTalkACWTime;          //  三方通话事后处理时间   
	public long agent$transACWCount;             //  转接通话事后处理次数            
	public long agent$accACWTime;             //  客户呼入事后处理时间     不包括会议通话时间
	public long agent$accACWCount;             //  客户呼入事后处理次数		不包括会议通话次数
	public long agent$transACWTime;              //  转接通话事后处理时间       
	public long agent$ACWCountA;                  //  接听 转接 会议事后处理次数                    
	public long agent$ACWTimeA;                   //  接听 转接 会议事后整理状态时间
	public long agent$ACWCount;                  //  事后处理次数                    
	public long agent$ACWTime;                   //  事后整理状态时间                
	public long agent$ringTime;                  //  振铃时间
	public long agent$beAccRingTime;                  //  被接起电话振铃时间        
	public long agent$readyTime;                 //  就绪状态时间                    
	public long agent$noReadyTime;               //  未就绪状态时间                  
	public long agent$temporaryTime;             //  临时签退状态时间                
	public long agent$readyNoAccTime;            //  非话务等待时间(就绪未通话时间)  
	public long agent$longNoAnswCount;           //  座席久未应答电话量              
	public long agent$outboundCount;             //  外拨数                          
	public long agent$outboundTime;              //  外拨时间                        
	public long agent$outboundSuccCount;         //  外拨接通数                      
	public long agent$customerAbandTime;         //  客户放弃时长  若振铃时客户放弃，累计放弃时长＝排队时间＋振铃时间；若在排队时客户放弃(排队放弃只对技能组统计有效)，累计放弃时长＝排队时长。以秒计算。                      
	public long agent$customerAbandCount;        //  客户呼入放弃量                  
	public long agent$transIVRCount;             //  转IVR数 座席挂断                         
	public long agent$oneceSolvCount;            //  一次问题解决数       
	public long agent$consultCount;				//咨询其它座席电话量
	public long agent$consultTime;				//咨询其它座席电话时间	
	public long agent$beConsultCount;			//被其它座席咨询的电话量
	public long agent$beConsultTime;			//被其它座席咨询的电话量时间

	public long agent$transCenterIVRCount;			//IVR转其它中心数 座席挂断
	public long agent$oneAcc;				//一声振铃接听量
	public long agent$twoAcc;				//二声振铃接听量
	public long agent$threeAcc;				//三声振铃接听量
	public long agent$fourAcc;				//四声振铃接听量
	public long agent$outFourAcc;				//四声外振铃接听量
	
	public long agent$totalContactTime;    //累计通话时间（接听客户电话时间+三方通话时间＋被三方通话时间＋被转接时间）
	public long agent$totalContactCount;   //	累计通话次数（接听客户电话次数+三方通话次数＋被三方通话次数＋被转接次数）    
	
	public long agent$directContactTime;    //直接客户通话时间（接听客户电话时间+三方通话时间）
	public long agent$directContactCount;   //	直接客户通话次数（接听客户电话次数+三方通话次数）
	
	public String agent$oneceSolvRate;            //  一次问题解决率      
	
	public long agent$noContactReadyTime; 	//非话务等待时间(就绪未通话时间)
	
	public long agent$accQueueTime; 	//被接起电话排队时长（到达座席，振铃或通话）
	public long agent$totalQueueTime; 	//排队时间（到达座席，振铃或通话）
	public long agent$totalQueueCount; 	//排队数量（到达座席，振铃或通话）
	/** abc end */
	
	public long agent$extInCount;							//	直拨分机来电数
	public long agent$extAccCount;							//	直拨分机接听数
	public long agent$extAccPeriod;							//	直拨分机通话时长
	public long agent$skDistributeCount;					//	技能组分配座席电话数
	public long agent$skInCount;							//	技能组呼入来电数
	public long agent$skAccCount;							//	技能组分配接听数
	public long agent$skAccPeriod;							//	技能组分配接听时长
	public long agent$onlinePeriod;							//	座席总在线时长
	
	
	public long agent$cityTelCount;
	public long agent$countryTelCount;
	public long agent$internationalTelCount;
	public long agent$hongkongTelCount;
	public long agent$ipTelCount;
	public long agent$cityTelPeriod;
	public long agent$countryTelPeriod;
	public long agent$internationalTelPeriod;
	public long agent$hongkongTelPeriod;
	public long agent$ipTelPeriod;
	public String agent$cityTelFee;
	public String agent$countryTelFee;
	public String agent$internationalTelFee;
	public String agent$hongkongTelFee;
	public String agent$ipTelFee;

	public long agent$cityTelCountAuto;
	public long agent$countryTelCountAuto;
	public long agent$internationalTelCountAuto;
	public long agent$hongkongTelCountAuto;
	public long agent$ipTelCountAuto;
	public long agent$cityTelPeriodAuto;
	public long agent$countryTelPeriodAuto;
	public long agent$internationalTelPeriodAuto;
	public long agent$hongkongTelPeriodAuto;
	public long agent$ipTelPeriodAuto;
	public String agent$cityTelFeeAuto;
	public String agent$countryTelFeeAuto;
	public String agent$internationalTelFeeAuto;
	public String agent$hongkongTelFeeAuto;
	public String agent$ipTelFeeAuto;
	
}
