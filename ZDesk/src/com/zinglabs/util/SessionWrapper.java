package com.zinglabs.util;

import java.io.Serializable;
import java.util.List;

public class SessionWrapper implements Serializable {
	// user login id
	String userLoginId;

	// user menus
	List menuList = null;

	public SessionWrapper() {
	}

	public String getUserLoginId() {
		return userLoginId;
	}

	public void setUserLoginId(String loginId) {
		this.userLoginId = loginId;
	}

	public List getMenuList() {
		return menuList;
	}

	public void setMenuList(List aMenuList) {
		this.menuList = aMenuList;
	}
}