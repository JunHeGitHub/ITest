
package com.zinglabs.index.tbl.util;

import java.io.Serializable;
import java.util.HashMap;

import com.zinglabs.index.tbl.sys.*;




public class UserSession implements Serializable {

	private static final long serialVersionUID = -5793634449692869514L;
	
	
	public static final String SESSION_TOKEN = "TBL_USERSESSION";

	private User user;
	
	//private String userRole;


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}