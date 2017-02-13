package com.zinglabs.base;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.struts.action.ActionForm;

public class BaseForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private int searchRecordStart;

	private int searchRecordAmount;

	private int searchRecordEnd;

	private int searchTotalAmount;

	private int searchHasNext;

	private int searchHasPrev;

	private int searchAction; // 0: init ; 1:prev 2:next

	private String searchOrderbyStr;

	private String searchOldOrderbyStr;

	private int searchOrder; // pingpong flag 0:desc 1:asc

	private int searchFlag;

	private String sysCurrentTime;

	private String sysCurrentPlusTime;

	private String deptName;
	private String deptAdmin; 

	public BaseForm() {
		searchRecordStart = 0;
		searchRecordAmount = 200;
		searchRecordEnd = 0;

		searchTotalAmount = 0;
		searchHasNext = 0;
		searchHasPrev = 0;
		searchAction = 0;

		searchOrder = 0;
		searchFlag = 0;

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar now = Calendar.getInstance();
		sysCurrentTime = format.format(now.getTime());
		now.add(Calendar.MINUTE, 30);
		sysCurrentPlusTime = format.format(now.getTime());
	}

	public int getSearchRecordStart() {
		return this.searchRecordStart;
	}

	public void setSearchRecordStart(int searchRecordStart) {
		this.searchRecordStart = searchRecordStart;
	}

	public int getSearchRecordAmount() {
		return this.searchRecordAmount;
	}

	public void setSearchRecordAmount(int searchRecordAmount) {
		this.searchRecordAmount = searchRecordAmount;
	}

	public int getSearchRecordEnd() {
		int ret;
		ret = this.searchRecordEnd - 1;
		if (ret < 0) {
			ret = 0;
		}
		return ret;
	}

	public void setSearchRecordEnd(int searchRecordEnd) {
		this.searchRecordEnd = searchRecordEnd;
	}

	public int getSearchTotalAmount() {
		return this.searchTotalAmount;
	}

	public void setSearchTotalAmount(int searchTotalAmount) {
		this.searchTotalAmount = searchTotalAmount;
		if (searchRecordStart > 0) {
			searchHasPrev = 1;
		} else {
			searchHasPrev = 0;
		}

		if ((searchRecordStart + searchRecordAmount) < searchTotalAmount) {
			searchHasNext = 1;
			searchRecordEnd = searchRecordStart + searchRecordAmount;
		} else {
			searchHasNext = 0;
			searchRecordEnd = searchTotalAmount;
		}
	}

	public int getSearchHasNext() {
		return this.searchHasNext;
	}

	public void setSearchHasNext(int searchHasNext) {
		this.searchHasNext = searchHasNext;
	}

	public int getSearchHasPrev() {
		return this.searchHasPrev;
	}

	public void setSearchHasPrev(int searchHasPrev) {
		this.searchHasPrev = searchHasPrev;
	}

	public int getSearchAction() {
		return this.searchAction;
	}

	public void setSearchAction(int searchAction) {
		this.searchAction = searchAction;
	}

	public int getSearchOrder() {
		return this.searchOrder;
	}

	public void setSearchOrder(int searchOrder) {
		this.searchOrder = searchOrder;
	}

	public String getSearchOrderbyStr() {
		return this.searchOrderbyStr;
	}

	public void setSearchOrderbyStr(String searchOrderbyStr) {
		this.searchOrderbyStr = searchOrderbyStr;
	}

	public String getSearchOldOrderbyStr() {
		return this.searchOldOrderbyStr;
	}

	public void setSearchOldOrderbyStr(String searchOldOrderbyStr) {
		this.searchOldOrderbyStr = searchOldOrderbyStr;
	}

	public int calutlateSearchOrder() {
		if (searchOldOrderbyStr != null && searchOrderbyStr != null) {
			if (searchOrderbyStr.equals(searchOldOrderbyStr)) {
				this.searchOrder = (this.searchOrder == 0) ? 1 : 0;
			} else {
				this.searchOrder = 0;
			}
		}
		this.searchOldOrderbyStr = this.searchOrderbyStr;
		return this.searchOrder;
	}

	public int calculateSearchRecordStart() {
		int start = 0;
		switch (this.searchAction) {
		case 0:
			start = searchRecordStart;
			break;
		case 1:
			start = searchRecordStart - searchRecordAmount;
			if (start < 0) {
				start = 0;
			}
			break;
		case 2:
			start = searchRecordStart + searchRecordAmount;
			break;
		}
		searchRecordStart = start;
		return start;
	}

	public String getSysCurrentTime() {
		return this.sysCurrentTime;
	}

	public void setSysCurrentTime(String sysCurrentTime) {
		this.sysCurrentTime = sysCurrentTime;
	}

	public String getSysCurrentPlusTime() {
		return this.sysCurrentPlusTime;
	}

	public void setSysCurrentPlusTime(String sysCurrentPlusTime) {
		this.sysCurrentPlusTime = sysCurrentPlusTime;
	}

	public int getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(int searchFlag) {
		this.searchFlag = searchFlag;
	}
	public String getDeptName(){
		return this.deptName;
	}
	public void setDeptName(String deptName){
		this.deptName = deptName;
	}
	public String getDeptAdmin(){
		return this.deptAdmin;
	}
	public void setDeptAdmin(String deptAdmin){
		this.deptAdmin = deptAdmin;
	}
}
