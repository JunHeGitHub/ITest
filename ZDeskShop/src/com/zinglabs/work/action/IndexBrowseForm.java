package com.zinglabs.work.action;


import org.apache.struts.action.ActionForm;



public class IndexBrowseForm extends ActionForm{
	private String  qc_user;
    private String  grade_index;   
    private String  grade_name;
    private String[] selectedItems;
    private String available ;
    
    private String grade_index_desp ;
    private String grade_index_creator;
    private String grade_index_time ;
    private String gradeindex_state;
    
	public String getGrade_index_desp() {
		return grade_index_desp;
	}
	public void setGrade_index_desp(String grade_index_desp) {
		this.grade_index_desp = grade_index_desp;
	}
	public String getGrade_index_creator() {
		return grade_index_creator;
	}
	public void setGrade_index_creator(String grade_index_creator) {
		this.grade_index_creator = grade_index_creator;
	}
	public String getGrade_index_time() {
		return grade_index_time;
	}
	public void setGrade_index_time(String grade_index_time) {
		this.grade_index_time = grade_index_time;
	}
	public String[] getSelectedItems() {
		return selectedItems;
	}
	public void setSelectedItems(String[] selectedItems) {
		this.selectedItems = selectedItems;
	}
	public String getGrade_index() {
		return grade_index;
	}
	public void setGrade_index(String grade_index) {
		this.grade_index = grade_index;
	}
	
	public String getGrade_name() {
		return grade_name;
	}
	public void setGrade_name(String grade_name) {
		this.grade_name = grade_name;
	}
	public String getQc_user() {
		return qc_user;
	}
	public void setQc_user(String qc_user) {
		this.qc_user = qc_user;
	}
	public String getAvailable() {
		return available;
	}
	public void setAvailable(String available) {
		this.available = available;
	}
	public String getGradeindex_state() {
		return gradeindex_state;
	}
	public void setGradeindex_state(String gradeindex_state) {
		this.gradeindex_state = gradeindex_state;
	}
}
