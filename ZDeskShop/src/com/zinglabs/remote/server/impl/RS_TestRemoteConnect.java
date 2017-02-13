package com.zinglabs.remote.server.impl;

import java.io.Serializable;
import java.util.Map;

import com.zinglabs.remote.server.implDef.RemoteServerCall;

/**
 * 测试远程调用
 * @author QCF
 *
 */
public class RS_TestRemoteConnect implements RemoteServerCall,Serializable{
	private static final long serialVersionUID = -743918455329041805L;

	public Map afterExec(Map map) {
		return map;
	}
	public Map beforeExec(Map map) {
		return map;
	}

	public Map exec(Map map) {
		map.put("success", true);
		return map;
	}

}
