package com.zinglabs.util;

public class Constants {
	public static final int FLAN_RUN_NUM_MAX = 4;
	
	
	

	

	
	public static final String ARGU_CK_STATUS_UNMODIFY = "0";
	public static final String ARGU_CK_STATUS_UNCHECK = "1";
	public static final String ARGU_CK_STATUS_FINISH = "2";
	
	
	public static final int GRADE_STEP_FIRSTCHECK = 0;
	public static final int GRADE_STEP_FIRSTRCHECK = 1;
	public static final int GRADE_STEP_SECONDCHECK = 2;
	public static final int GRADE_STEP_SECONDRCHECK = 3;
	public static final int GRADE_STEP_THIRDCHECK = 4;
	public static final int GRADE_STEP_RETRYCHECK = 5;
	public static final int GRADE_STEP_QARETRYCHECK = 6;
	
	public static final String GRADE_STATUS_TYPE = "grade_status";
	public static final int GRADE_STAUTS_NO = 0;
	public static final int GRADE_STAUTS_YES = 1;
	public static final int GRADE_STAUTS_ALL = 2;
	
	public static final int GRADE_FLAG_NOUSE = 0;
	public static final int GRADE_FLAG_USE = 1;
	
	//0 ���Ḵ�� 1 ǰ̨��ܸ��󲵻� 2 ǰ̨��ܸ���ͨ�� 3 ǰ̨����Ḵ�� 
	public static final int RCHECK_SUBMIT = 0;
	public static final int RCHECK_RCUSER_BACK = 1;
	public static final int RCHECK_RCUSER_OK = 2;
	public static final int RCHECK_RCUSER_SUBMIT = 3;
	//0 Ʒ����ܴ��� 1 Ʒ����ܲ��� 2 Ʒ����ܸ���ͨ��
	public static final int RCHECK_QAUSER_WAIT = 0;
	public static final int RCHECK_QAUSER_BACK = 1;
	public static final int RCHECK_QAUSER_OK = 2;
	
	public static final int RCHECK_GRADECHG_NO = 0;
	public static final int RCHECK_GRADECHG_YES = 1;	
	
	public static final int TASKFLAG_FIRSTCHECK = 1;
	public static final int TASKFLAG_SECONDCHECK = 2;
	public static final int TASKFLAG_ASSIGN = 1;
	public static final int TASKFLAG_REASSIGN = 2;
	
	public static final int DEPARTMENT_AGENT = 0;
	public static final int DEPARTMENT_QCUSER = 1;	
	
	public static final String QCDBNAME = "z3000_qc";
	public static final String QCDBUSER = "zinglabs";
	public static final String QCDBPSWD = "12";	
	
	public static final int BACKUP_TYPE_BAK = 0;
	public static final int BACKUP_TYPE_DEL = 1;
}
