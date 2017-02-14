package com.zinglabs.work.action;


import org.apache.struts.action.ActionForm;



public class ZKMPeiXunForm extends ActionForm{
	
	private String recordType;
	private String title;
	private String bdate;
	private String edate;
	private String keywords;
	private String versions;
	private String createUser;
	private String createTime;
	private String lastModiTime;
	private String lastModiUser;
	private String lookNum;
	private String isExpired;
	
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getBdate() {
		return bdate;
	}
	public void setBdate(String bdate) {
		this.bdate = bdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getVersions() {
		return versions;
	}
	public void setVersions(String versions) {
		this.versions = versions;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLastModiTime() {
		return lastModiTime;
	}
	public void setLastModiTime(String lastModiTime) {
		this.lastModiTime = lastModiTime;
	}
	public String getLastModiUser() {
		return lastModiUser;
	}
	public void setLastModiUser(String lastModiUser) {
		this.lastModiUser = lastModiUser;
	}
	public String getLookNum() {
		return lookNum;
	}
	public void setLookNum(String lookNum) {
		this.lookNum = lookNum;
	}
	public String getIsExpired() {
		return isExpired;
	}
	public void setIsExpired(String isExpired) {
		this.isExpired = isExpired;
	}
}
