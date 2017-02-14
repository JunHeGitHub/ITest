package com.zinglabs.apps.BOC_gongdan.type;
/**
 * 问题咨询单接口字段
 * */
public class Wentizixundan {
	
    ////处理分行或者总行
	String Brid_2_Brid ;
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
	//账户账号
	String accountNumber_2_AccountNo ;
	//问题类型
	String problemType_2_QuestionType ;
	//涉及部门
	String referDept_2_InvolvedDept ;
	//所在分行
	String belongBank_2_InvolvedBranch ;
	//咨询内容
	String matter_2_ConsultContent ;
	//处理时限
	String manageTimeLimit_2_LimitTime ;
	//关联工单
	String guanlianId_2_Linkid ;
	
	//渠道标示
	String ChannelType_2_ChannelType ;
	//渠道名称
	String channelName_2_ChannelValue ;
	//境内外标识
	String BranchType_2_BranchType ;
}
