package com.zinglabs.db;

public class BaseData {
	
	public static final int LOG_AGENT_STATE_LOGIN = 1;
	public static final int LOG_AGENT_STATE_READY = 2;
	public static final int LOG_AGENT_STATE_REST = 3;
	public static final int LOG_AGENT_STATE_DELAY = 4;
	public static final int LOG_AGENT_STATE_LOGOUT = 5;
	public static final int LOG_AGENT_STATE_TEMPORARY = 6;

	public static final int LOG_CALL_CONNECT_TYPE_NOANSWER = 1;
	public static final int LOG_CALL_CONNECT_TYPE_NONUMBER = 2;
	public static final int LOG_CALL_CONNECT_TYPE_FAX = 3;
	public static final int LOG_CALL_CONNECT_TYPE_NORMAL = 4;
	
	public static final int  LOG_CALL_END_TYPE_SELFDISCONNECT   =1;
	public static final int  LOG_CALL_END_TYPE_AGENTDISCONNECT  =2;
	public static final int  LOG_CALL_END_TYPE_IVRFLOWEND       =3;
	public static final int  LOG_CALL_END_TYPE_CTICOMMAND       =4;
	public static final int  LOG_CALL_END_TYPE_TRANSFER         =5;	
	public static final int  LOG_CALL_END_TYPE_TRANSFERIVR      =6;	
	
	public static final int LOG_SKILLGROUP_REASON_BY_AGENT = 1;
	public static final int LOG_SKILLGROUP_REASON_BY_SELF = 2;
	public static final int LOG_SKILLGROUP_REASON_BY_FLOW = 3;

	public static final int VT_SGSERVICE_QUIT_BY_ONHOOK = 1;
	public static final int VT_SGSERVICE_QUIT_BY_IVR = 2;
	public static final int VT_SGSERVICE_QUIT_BY_AGENT = 3;

	public static final int CHANNEL_TYPE_INLINE = 0;
	public static final int CHANNEL_TYPE_COLINE = 1;
	
	public static final int PEER_TARGET_TYPE_IVR = 1;
	public static final int PEER_TARGET_TYPE_SKILLGROUP = 2;
	public static final int PEER_TARGET_TYPE_EXT = 3;
	public static final int PEER_TARGET_TYPE_TRUNK = 4;
	public static final int PEER_TARGET_TYPE_FAX = 5;
	public static final int PEER_TARGET_TYPE_LOST_MULTILINE = 6;
	
	public static final int SELF_TYPE_INLINE_OUTGOING = 1;
	public static final int SELF_TYPE_COLINE_INBOUND = 2;
	public static final int SELF_TYPE_COLINE_OUTBOUND = 3;
	public static final int SELF_TYPE_NO_SELF = 3;

	public static final int TYPE_STRING = 1;
	public static final int TYPE_INT = 2;
	public static final int TYPE_DATE = 3;
	
	public static final int GRANULARITY_YEAR = 1;
	// public static final int GRANULARITY_SEASON = 2;
	public static final int GRANULARITY_MONTH = 3;
	// public static final int GRANULARITY_WEEK = 4;
	public static final int GRANULARITY_DATE = 5;
	public static final int GRANULARITY_HOUR = 6;
	public static final int GRANULARITY_HALF_HOUR = 7;
	public static final int GRANULARITY_QUARTER = 8;
	public static final int GRANULARITY_MINUTES = 9;
	public static final int GRANULARITY_SECOND = 10;

	public static final int CALCULATE_ADD = 1;
	public static final int CALCULATE_SUB = 2;

	public static String ID = "__";

	public static final int PHONECOUNTFLAG = 3; //save count in array pos

	public static final int ACWTIMEPOS = 0;

	public static final int ACWCOUNTPOS = 1;

	public static final int CONTACTTIMEPOS = 2;

	public static final int CONTACTCOUNTPOS = 3;

	public static final int HOLDTIMEPOS = 4;

	public static final int HOLDTIMECOUNTPOS = 5;

	public static final int HOLDCUSTTIMEPOS = 6;
	
	public static final int RINGPERIODPOS = 7;
	
	public static final int EXCEPTMEETING  = 0;
	
	public static final int INCLUDEMEETING  = 1;
	
	public static final double PERRINGPERIOD  = 4.8;
	
	public static final String SSC_SERI_DIR="/hd2/REPORT/SERI/";
	
	public static final String SSC_REPORT_DIR="/hd2/REPORT/";
//	
	public static final String IVR_DATE_DIR="/hd2/nftp/";
	
//	public static final String SSC_SERI_DIR="c:/hd2/REPORT/SERI/";
//	
//	public static final String SSC_REPORT_DIR="c:/hd2/REPORT/";
	
	public static final String ASPECT_AP_TITAL="Agent Productivity Report";
	
	public static final String ASPECT_AP_DATE="Date: ";
	public static final String ASPECT_AP_NAME="ID ACD SIT SOT NCH ATT AWT PIP NOC AOTT AOWT";
	public static final String ASPECT_AP_GROUP="Group";
	public static final String ASPECT_AP_END="$END OF ASPECT";
	
	public static final int CRITICAL_TYPE_TEN_MIN=1;
	
	public static final String QUEU_SKILL_GROUP_NAME="11";
	
	public static final long DEFULT_SLEEP_INTERVAL=2000L;
	public static final long DEFULT_LONG_SLEEP_INTERVAL=20000L;
	public static final long DEFULT_FORWARD_INTERVALx=1200000L;
	public static final long DEFULT_LOG_WARN_INTERVAL=180000L;
	public static final long DEFULT_1000_INTERVAL=60000L;
	public static final int DEFULT_THREAD_TASK_SIZE=1000;
	
	public static final int DEFULT_SQL_BATCH_SIZE=2000;
	
//	public static final String CONFIG_FILE_PATH="/usr/local/tomcat/server/webapps/ReportView/xml/Conf.xml";
//	public static final String CONFIG_FILE_PATH="c:/ConfZShifts.xml";
//	public static final String CONFIG_FILE_PATH="c:/Conf�������.xml";
//	public static final String CONFIG_FILE_PATH="c:/Conf�ɶ�.xml";
//	public static final String CONFIG_FILE_PATH="c:/ConfBase.xml";
//	public static final String CONFIG_FILE_PATH="/usr/local/tomcat/webapps/ZWM/xml/ConfZShifts.xml";
	
	
	public static final int GRANULARITY_SQL_INIT=0;
	public static final int GRANULARITY_SQL_INSERT=2;
	public static final int GRANULARITY_SQL_DELETE=1;
	public static final int GRANULARITY_SQL_UPDATE=3;
	
	
	public static final int QUEUE_TYPE_BIG_GROUP=1;
	public static final int QUEUE_TYPE_STRICT=2;
	public static final int QUEUE_TYPE_CIRCLE=3;
	
	public static final int BALANCE_Add_TYPE_TALK=1;
	public static final int BALANCE_Add_TYPE_ACW=2;
	public static final int BALANCE_Add_TYPE_CONSULT=3;
	public static final int BALANCE_Add_TYPE_BECONSULT=4;
	public static final int BALANCE_Add_TYPE_BETRANS=5;
	public static final int BALANCE_Add_TYPE_THREE_TALK=6;
	public static final int BALANCE_Add_TYPE_BETHREE_TALK=7;
	public static final int BALANCE_Add_TYPE_CONSULT_TRANSFER=8;
	public static final int BALANCE_Add_TYPE_EXT_DIRECT=9;
	public static final int BALANCE_Add_TYPE_AUTO_OUT_DIAL=10;
	
	public static final String CONF_INFO_SKIP="SKIP_";
	public static final String CONF_INFO_SUM="$SUM";
	
	public static final String CONF_INFO_TABLE_TYPE_BASE_DATA="baseData";
	
	public static final int DB_KEY_TYPE_INSERT= Integer.parseInt("1000", 2);
	public static final int DB_KEY_TYPE_UPDATE= Integer.parseInt("0100", 2);
	public static final int DB_KEY_TYPE_DELETE= Integer.parseInt("0010", 2);
	public static final int DB_KEY_TYPE_SELECT= Integer.parseInt("0001", 2);
	
	public static final String REAL_TIME_TYPE_UNANS="realTime$UnAns$";
	public static final String REAL_TIME_TYPE_STATE="realTime$State: Exp $";
	public static final String REAL_TIME_TYPE_TEMPORARY="realTime$Temporary$";
	public static final String REAL_TIME_TYPE_ABAND="realTime$Aband$";
	public static final String REAL_TIME_TYPE_FIRST_DISTRIBUTE="realTime$FirstDistribute$";
	public static final String REAL_TIME_TYPE_DETAIL_CALL_OUT_AUTO="realTime$DetailCallOutAuto$";
	public static final String REAL_TIME_TYPE_DETAIL_CALL_OUT="realTime$DetailCallOut$";
	public static final String REAL_TIME_TYPE_DETAIL_CALL_IN="realTime$DetailCallIn$";
	
	public static final String CITY_TEL = "�л�";
	public static final String COUNTRY_TEL = "���ڳ�;";
	public static final String INTERNATIONAL_TEL = "��ʳ�;";
	public static final String HONGKONG_TEL = "�۰�̨";
	public static final String IP_TEL = "IP�绰";
	public static final String HONGKONG = "852";
	public static final String MACAO = "853";
	public static final String TAIWAN = "886";
	public static final String AUTO_OUT_DIAL = "OUTDIAL";
	public static final String STATIC_VALUE_NOW="STATIC:NOW#"; 
	public static final String STATIC_VALUE_SCOPE="STATIC:SCOPE#";
	
	public static final String OPERATION_ADD="add";
	public static final String OPERATION_DELETE="delete";
	public static final String OPERATION_UPDATE="update";
	public static final String OPERATION_SEARCH="search";
	
	public static final String COMBO_TYPE_SKILL="skill";
	public static final String COMBO_TYPE_SKILL_AGENT="skillAgent";
	public static final String COMBO_TYPE_ALL_COMBO="allCombo";
	public static final String COMBO_TYPE_ALL_GROUP="allGroup";
	public static final String COMBO_TYPE_SELECT_COMBO_VALUE="selectComboValue";
	public static final String COMBO_QC_BUSINESS_TYPE="QCBusinessType";
	public static final String COMBO_QC_AGENT_LEVEL_ID="QCAgentLevelID";
	public static final String COMBO_QC_RECORD_AGENT_LEVEL="QCRecordAgentLevel";
	public static final String COMBO_QC_BUSINESS_TYPE_AGENT="QCBusinessTypeAgent";
	public static final String COMBO_QC_AGENT_LEVEL_NUMBER="QCRecordAgentLevelNumber";
	public static final String COMBO_QC_AGENT_LEVEL_PHONE_NUMBER="QCAgentLevelPhoneNumber";
	public static final String COMBO_QC_DICT="QCDict";
	public static final String QC_RECORD_ZJY="QCRecordZhiJianYuan";
	public static final String COMBO_QC_TEACH_TYPE ="QCTeachType";
	public static final String COMBO_QC_DATA_STATE ="QCDataState";
	public static final String COMBO_QC_SCORE_STATE ="QCScoreState";
	public static final String COMBO_FJ_SCORE_STATE ="FJScoreState";
	public static final String COMBO_QC_TEXT_BUSINESS_TYPE="QCTextBusinessType";
	public static final String COMBO_QC_TEXT_BUSINESS_TYPE_AGENT="QCTextBusinessTypeAgent";
	public static final String COMBO_QC_TEXT_BUSINESS_TYPE_AGENT_NUMBER="QCTextBusinessTypeAgentNumber";
	public static final String COMBO_QC_TEXT_WORK_NUMBER_TYPE="QCTextWorkNumberType";
	public static final String COMBO_QC_TEXT_WORK_NUMBER="QCTextWorkNumber";
	public static final String COMBO_QC_PHYSIQUE_GROUP="QCPhysiqueGroup";
	public static final String COMBO_QC_QUALITY_GROUP="QCQualityGroup";
	
	//for WeChat
	public static final String COMBO_WE_CHAT_MENU_SIGN="WeChatMenuSign";
	
/*
 *  add texte
 */
	public static final String COMBO_QC_TEXTE_LONGUEUR ="QCTextLength";
	public static final String COMBO_QC_CLIENT_ATTENDRE_LENGTH="QCTextClientWaitLength";
	public static final String COMBO_QC_CLIENT_NAME="QCTextClientName";
	public static final String COMBO_QC_TEXTE_QUARTIER ="QCTextQuartier";
	public static final String COMBO_QC_TEXTE_CLIENT_SATISFY ="QCTextClientSatify";
	public static final String COMBO_QC_TEXTE_IP_ADRESSE ="QCTextIPAdresse";
	public static final String COMBO_QC_TEXTE_REPETER_NUMERO ="QCTextRepeter";
	
	public static final String COMBO_QC_ZHI_BIAO_STATE ="QCZhiBiaoState";
	
	public static final String BASE_COMPONENT_WINDOW="window";
	public static final String BASE_COMPONENT_FORM="form";
	
	public static final String BASE_DATA_TYPE_JSON="json";
	public static final String BASE_DATA_TYPE_XML="xml";
	
	public static final String FIELD_TYPE_AUTO_INCREMENT="1";
	
	public static final String BASE_TIME_END="timeend";
	
	
	public static final String CHART_TYPE_LINE="linechart";
	public static final String CHART_TYPE_COLUMN="columnchart";
	public static final String CHART_TYPE_STACKED_BAR_CHART="stackedbarchart";
}


