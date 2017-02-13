package com.zinglabs.index.tbl.sys;

public class Acl extends BaseDto {
	private static final long serialVersionUID = 3168754031342218169L;

	private String roleCode;
	private String resourceCode;
	private String privilegeCode;
	
	public Acl(){}
	public Acl(String roleCode,String resourceCode,String privilegeCode){
		this.roleCode = roleCode;
		this.resourceCode = resourceCode;
		this.privilegeCode = privilegeCode;
	}
	/**
	 * @return Returns the privilegeCode.
	 */
	public String getPrivilegeCode() {
		return privilegeCode;
	}

	/**
	 * @param privilegeCode
	 *            The privilegeCode to set.
	 */
	public void setPrivilegeCode(String privilegeCode) {
		this.privilegeCode = privilegeCode;
	}

	/**
	 * @return Returns the resourceCode.
	 */
	public String getResourceCode() {
		return resourceCode;
	}

	/**
	 * @param resourceCode
	 *            The resourceCode to set.
	 */
	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	/**
	 * @return Returns the roleCode.
	 */
	public String getRoleCode() {
		return roleCode;
	}

	/**
	 * @param roleCode
	 *            The roleCode to set.
	 */
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Acl)) {
			return false;
		}
		if (roleCode == null || resourceCode == null || privilegeCode == null) {
			return false;
		}

		Acl tmp = (Acl) obj;

		return (roleCode.equals(tmp.roleCode)
				&& resourceCode.equals(tmp.resourceCode) && privilegeCode
				.equals(tmp.privilegeCode));
	}
}
