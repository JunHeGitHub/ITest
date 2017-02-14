package com.zinglabs.index.tbl.sys;

public class User extends BaseDto {
	private static final long serialVersionUID = -1123195119991406996L;
	public static final int ENABLED = 1;
	public static final int DISABLED = 0;
	
	public static final String CTX_USER = "user";
	public static final String CTX_USER_LIST = "userList";
	
	private String userPin;
	private String password;
	private String name;
	private int deptId;
	private String phone;
	private String email;
	private int grade;
	private int listIndex;
	private int enabled;
	
	public User(){		
	}
	public User(String userPin){
		this.userPin = userPin;
	}
	
	/**
	 * @return Returns the deptId.
	 */
	public int getDeptId() {
		return deptId;
	}
	/**
	 * @param deptId The deptId to set.
	 */
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the enabled.
	 */
	public int getEnabled() {
		return enabled;
	}
	/**
	 * @param enabled The enabled to set.
	 */
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	/**
	 * @return Returns the grade.
	 */
	public int getGrade() {
		return grade;
	}
	/**
	 * @param grade The grade to set.
	 */
	public void setGrade(int grade) {
		this.grade = grade;
	}
	/**
	 * @return Returns the listIndex.
	 */
	public int getListIndex() {
		return listIndex;
	}
	/**
	 * @param listIndex The listIndex to set.
	 */
	public void setListIndex(int listIndex) {
		this.listIndex = listIndex;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the phone.
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone The phone to set.
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return Returns the userPin.
	 */
	public String getUserPin() {
		return userPin;
	}
	/**
	 * @param userPin The userPin to set.
	 */
	public void setUserPin(String userPin) {
		this.userPin = userPin;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	//@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof User)){
			return false;
		}
		
		User tmp = (User)obj;
		
		return (this.userPin.equals(tmp.userPin));
	}	
	
	
	
}
