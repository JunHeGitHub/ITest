package com.zinglabs.remote.server.implDef;

import java.util.Map;

/**
 * 远程调用接口定义
 * @author QCF
 */
public interface RemoteServerCall {
	public Map beforeExec(Map param);
	public Map exec(Map param);
	public Map afterExec(Map param);
}
