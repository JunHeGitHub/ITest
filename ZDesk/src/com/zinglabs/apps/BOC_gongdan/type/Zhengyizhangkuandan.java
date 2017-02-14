package com.zinglabs.apps.BOC_gongdan.type;
/**
 * 争议账款单接口字段
 * */
public class Zhengyizhangkuandan {
	////代表未确认字段  //代表可推送字段
	////处理分行或者总行
	String Branchid_2_Brid ;
	////处理部门
	String Departid_2_Departid ;
	
	//工单级别
	String gongdanLevel_2_UrgencyType  ;
	
	
	////受理时间
	String AcceptTime_2_AcceptTime ;
	////受理人
	String AcceptName_2_AcceptName ;
	////签发时间
	String AcceptCheckTime_2_AcceptCheckTime ;
	////复核人
	String AcceptChecker_2_AcceptChecker ;
	////转发时间
	String TransmitTime_2_TransmitTime ;
	////转发人
	String Transmitter_2_Transmitter ;
	////转签时间
	String TransmitCheckTime_2_TransmitCheckTime ;
	////复核人
	String TransmitChecker_2_TransmitChecker ;
	
	//客户姓名
	String customerName_2_CustName  ;
	//客户性别
	String customerSex_2_CustSex  ;
	//证件类型
	String paperType_2_CertType  ;
	//证件号码
	String paperNum_2_CertNo  ;
	
	
	
	
	////家庭电话
	String HomePhone_2_HomePhone  ;
	////办公电话
	String UnitPhone_2_UnitPhone  ;
	////手机
	String MobilePhone_2_MobilePhone  ;
	
	//来电号码
	String customerCallTime_2_PhoneCode  ;
	
	////问题类型
	String QuestionType_2_QuestionType ;
	
	
	//所属分行
	String belongBank_2_InvolvedBranch ;
	//账户类型
	String accountType_2_AccountType ;
	//账户号码
	String accountNumber_2_AccountNo ;
	
	////交易地点
	String TransactionSite_2_TransactionSite ;
	////交易时间-->
	String TransactionTime_2_TransactionTime ;
	////交易币种-->
	String CurrencyType_2_CurrencyType ;
	////交易金额-->	
	String TransactionMoney_2_TransactionMoney ;
	////交易类型-->
	String TransactionType_2_TransactionType ;
	////记账时间-->
	String KeepAccountsTime_2_KeepAccountsTime;
	
	
	//渠道名称
	String channelName_2_ChannelValue ;
	////渠道标示
	String ChannelType_2_ChannelType ;
	
	
	//涉及部门
	String referDept_2_InvolvedDept ;
	//处理时限
	String manageTimeLimit_2_LimitTime ;
	//争议描述
	String matter_2_DisputedContent ;
	
	////境内外表示 默认为1
	String BranchType_2_BranchType ;
	
	//关联工单
	String guanlianId_2_Linkid ;
}
