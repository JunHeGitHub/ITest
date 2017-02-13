package com.zinglabs.work.action;

import java.util.HashMap;
import java.util.Map;

import com.zinglabs.base.BaseForm;

public class FirstCheckForm extends BaseForm {

    private String gradeId;
    
    private String qcUser;
    
    private String taskId;
    
	private String serialNumber;

    private String useGradeIndex;

    private String gradeStatus;
    
    private String gradeStatusText;
    
    private String phoneNumber;

    private String recordLength;
    
    private String callerNumber;

    private String recordBeginTime;

    private String fileName;

    private String fileLocation;

    private String gradeRemark;

    private String teachType;
    
    private String teachRemark;

    private String dirId;
    
    private String teachOperation;
    
    private int oneticket;
    
    private String gradeScore;
    
    private String pfxTotal;
    
    private Map map = new HashMap();

    private String[] selectedItems;

	private String gradeUser;
	
	private int gradeStep;	
	
	private String gradeStepText;
	
    private int isHasRetryCheck;
    
    private String retryCheckText;
    
    private String agentArguRemark;
    
    private String adminAgentArguRemark;
    
    private String fujianRemark;
    
    private String owner;
    
    private String scoreState;
    
   private String workState;
   
   private String gradeYipiaofoujue;
   
   private String gradeImportant;
   
   private String gradeName;
   
   private String reowner;
   
   public String getReowner() {
	return reowner;
}

public void setReowner(String reowner) {
	this.reowner = reowner;
}

public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
   
   public String getGradeImportant() {
	return gradeImportant;
}

public void setGradeImportant(String gradeImportant) {
	this.gradeImportant = gradeImportant;
}

public String getGradeYipiaofoujue() {
		return gradeYipiaofoujue;
	}

	public void setGradeYipiaofoujue(String gradeYipiaofoujue) {
		this.gradeYipiaofoujue = gradeYipiaofoujue;
	}
   
   public String getWorkState() {
		return workState;
	}

	public void setWorkState(String workState) {
		this.workState = workState;
	}


    public String getScoreState() {
		return scoreState;
	}

	public void setScoreState(String scoreState) {
		this.scoreState = scoreState;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Object getValue(String key){
        return map.get(key);
    }

    public void setValue(String key, Object value){
        map.put(key, value);
    }

    public void setValuesMap(Map p_FormvalueMap) {
        this.map = p_FormvalueMap;
    }

    public Map getValuesMap() {
        return this.map;
    }

	public String getQcUser() {
		return qcUser;
	}

	public void setQcUser(String qcUser) {
		this.qcUser = qcUser;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getUseGradeIndex() {
		return useGradeIndex;
	}

	public void setUseGradeIndex(String useGradeIndex) {
		this.useGradeIndex = useGradeIndex;
	}

	public String getGradeStatus() {
		return gradeStatus;
	}

	public void setGradeStatus(String gradeStatus) {
		this.gradeStatus = gradeStatus;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRecordLength() {
		return recordLength;
	}

	public void setRecordLength(String recordLength) {
		this.recordLength = recordLength;
	}

	public String getCallerNumber() {
		return callerNumber;
	}

	public void setCallerNumber(String callerNumber) {
		this.callerNumber = callerNumber;
	}

	public String getRecordBeginTime() {
		return recordBeginTime;
	}

	public void setRecordBeginTime(String recordBeginTime) {
		this.recordBeginTime = recordBeginTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getGradeRemark() {
		return gradeRemark;
	}

	public void setGradeRemark(String gradeRemark) {
		this.gradeRemark = gradeRemark;
	}

	public String[] getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(String[] selectedItems) {
		this.selectedItems = selectedItems;
	}

	public String getGradeStatusText() {
		return gradeStatusText;
	}

	public void setGradeStatusText(String gradeStatusText) {
		this.gradeStatusText = gradeStatusText;
	}

	public String getTeachRemark() {
		return teachRemark;
	}

	public void setTeachRemark(String teachRemark) {
		this.teachRemark = teachRemark;
	}

	public String getTeachType() {
		return teachType;
	}

	public void setTeachType(String teachType) {
		this.teachType = teachType;
	}

	public String getDirId() {
		return dirId;
	}

	public void setDirId(String dirId) {
		this.dirId = dirId;
	}

	public String getTeachOperation() {
		return teachOperation;
	}

	public void setTeachOperation(String teachOperation) {
		this.teachOperation = teachOperation;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getOneticket() {
		return oneticket;
	}

	public void setOneticket(int oneticket) {
		this.oneticket = oneticket;
	}

	public String getGradeScore() {
		return gradeScore;
	}

	public void setGradeScore(String gradeScore) {
		this.gradeScore = gradeScore;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getPfxTotal() {
		return pfxTotal;
	}

	public void setPfxTotal(String pfxTotal) {
		this.pfxTotal = pfxTotal;
	}

	public String getAgentArguRemark() {
		return agentArguRemark;
	}

	public void setAgentArguRemark(String agentArguRemark) {
		this.agentArguRemark = agentArguRemark;
	}

	public String getAdminAgentArguRemark() {
		return adminAgentArguRemark;
	}

	public void setAdminAgentArguRemark(String adminAgentArguRemark) {
		this.adminAgentArguRemark = adminAgentArguRemark;
	}

	public String getFujianRemark() {
		return fujianRemark;
	}

	public void setFujianRemark(String fujianRemark) {
		this.fujianRemark = fujianRemark;
	}
    
}

