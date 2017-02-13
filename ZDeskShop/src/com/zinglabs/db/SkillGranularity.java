package com.zinglabs.db;

import java.sql.Timestamp;

/**
 * 
 * @author chuan.li
 * will process by AddObjData  if there any map value use String key only by now 
 *
 */
public class SkillGranularity {
	
	public SkillGranularity() throws Exception{
        SSCDBTools.defultValue(this);
	}

	public Timestamp upDateTime;  // one self update once
	
	public Timestamp upDBTime;
	
	public Timestamp skill$granularityBeginTime;
	
	public Timestamp skill$granularityEndTime;
	
	public BusyFlag lock;	 // when this lock occur performance problem  can be use thread copy
	
	public String skill$Name;
	
	public long skill$inboundCount;								//	来电数                          
	public long skill$inboundAccCount;             //  接听客户电话次数                
	public long skill$inboundAccTime;              //  接听客户电话时间 不包括会议通话时间   
	public long skill$shortAbandCount;             //  短时放弃量     
	public long skill$queueShortAbandCount;             // 排队并且短时放弃的电话量
	public long skill$abandCount;                  //  客户呼入放弃量                  
	public long skill$abandTime;                   //  放弃时长                        
	public long skill$accTelCount15;               //  15秒内接起的电话总量            
	public long skill$accTelCount20;               //  20秒内接起的电话总量            
	public long skill$queueCount;                  //  排队等待数                      
	public long skill$queuTime;                    //  排队时长                        
	public long skill$queueAbandCount;             //  客户排队放弃量                  
	public long skill$accQueueTime;                //  被接起电话的累计排队时间        
	public long skill$transAccCount;               //  转接数                          
	public long skill$threeTalkCount;              //  三方通话量
	public long skill$threeTalkTime;              //  三方通话时间_发起       
	public long skill$holdCount;                   //  持线次数                        
	public long skill$holdTime;                    //  持线时间                        
	public long skill$beThreeTalkCount;            //  被三方通话次数                  
	public long skill$beThreeTalkTime;             //  被三方通话时间                  
	public long skill$beTransAccCount;             //  被转接应答次数                  
	public long skill$beTransAccTime;              //  被转接应答时间                  
	public long skill$threeTalkACWCount;           //  三方通话事后处理次数            
	public long skill$transACWCount;               //  转接通话事后处理次数            
	public long skill$threeTalkACWTime;            //  三方通话事后处理时间            
	public long skill$transACWTime;                //  转接通话事后处理时间    
	public long skill$accACWTime;             //  客户呼入事后处理时间     不包括会议通话时间
	public long skill$accACWCount;             //  客户呼入事后处理次数		不包括会议通话次数
	
	public long skill$ACWCountA;                    //  接听 转接 会议事后处理次数                    
	public long skill$ACWTimeA;                     //  接听 转接 会议事后整理状态时间   
	
	public long skill$ACWCount;                    //  事后处理次数                    
	public long skill$ACWTime;                     //  事后整理状态时间                
	public long skill$readyNoAccTime;              //  非话务等待时间(就绪未通话时间)  1:n    need no skill add  
	public long skill$readyTime;                   //  就绪状态时间                    
	public long skill$noReadyTime;                 //  未就绪状态时间                  
	public long skill$temporaryTime;               //  临时签退状态时间                
	public long skill$longNoAnswCount;             //  座席久未应答电话量              
	public long skill$ringTime;                    //  振铃时间             
	public long skill$beAccRingTime;                  //  被接起电话振铃时间
	public long skill$outboundCount;               //  外拨数  1:n     need no skill add                         
	public long skill$outboundSuccCount;           //  外拨接通数  1:n     need no skill add                      
	public long skill$outboundTime;                //  外拨时间                        
	public long skill$innerCount;                  //  内呼数  1:n     need no skill add                       
	public long skill$innerCallTime;               //  内呼时间   1:n    need no skill add                        
	public long skill$loginAgentCount;             //  登录座席数       排除临时签退座席
	public long skill$maxLoginAgentCount;             //  最大登录座席数  1:n     need no skill add
	public long skill$avgLoginAgentCount;             //  平均登录座席数   1:n    need no skill add
	public long skill$onlineAgentCount;            //  在线坐席数 在线座席
	
//	public long skill$offlineAgentCount;            //  离线坐席数 在线座席 临时签退
	public long skill$maxOnlineAgentCount;            //  最大在线坐席数  1:n     need no skill add    
	public long skill$avgOnlineAgentCount;            //  平均在线坐席数
	public long skill$oneceSolvCount;              //  一次问题解决量      
	public long skill$consultCount;				//咨询其它座席电话量
	public long skill$consultTime;				//咨询其它座席电话时间	
	public long skill$beConsultCount;			//被其它座席咨询的电话量
	public long skill$beConsultTime;			//被其它座席咨询的电话时间
	public long skill$transIVRCount;			//转IVR数 座席挂断
	
	public long skill$transCenterIVRCount;			//IVR转其它中心数 座席挂断
	public long skill$maxQueuTime;			// 最长排队等待时间	
	public long skill$oneAcc;				//一声振铃接听量
	public long skill$twoAcc;				//二声振铃接听量
	public long skill$threeAcc;				//三声振铃接听量
	public long skill$fourAcc;				//四声振铃接听量
	public long skill$outFourAcc;				//四声外振铃接听量
	
	public long skill$totalContactTime;    //累计通话时间（接听客户电话时间+三方通话时间＋被三方通话时间＋被转接时间）
	public long skill$totalContactCount;   //	累计通话次数（接听客户电话次数+三方通话次数＋被三方通话次数＋被转接次数） 
	
	public long skill$directContactTime;    //直接客户通话时间（接听客户电话时间+三方通话时间）
	public long skill$directContactCount;   //	直接客户通话次数（接听客户电话次数+三方通话次数）  
	
	public String skill$oneceSolvRate;            //  一次问题解决率      
	public long skill$noContactReadyTime; 	//非话务等待时间(就绪未通话时间)
	/** abc end */
	
	
	public long skill$extInCount;							//	直拨分机来电数
	public long skill$extAccCount;							//	直拨分机接听数
	public long skill$extAccPeriod;							//	直拨分机通话时长
	public long skill$skDistributeCount;					//	技能组分配座席电话数
	public long skill$skInCount;							//	技能组呼入来电数
	public long skill$skAccCount;							//	技能组分配接听数
	public long skill$skAccPeriod;							//	技能组分配接听时长
	public long skill$onlinePeriod;							//	座席总在线时长
	public long skill$longNoAnswPeriod;							//	座席总在线时长
	public long skill$skFirstDistributeSucceCount;			//首次分配成功数
	
	public long skill$autoOutboundCount ;			//自动外呼成功量
	public long skill$autoOutboundConnectCount ;			//自动外呼成功量
	public long skill$totalAutoOutboundPeriod ;				//自动外呼通话时长
	
	public long skill$phoneDensity;
	
}
