package com.zinglabs.remote.server.implDef;
import java.util.*;

/**
 * 远程调用接口定义
 * @author QCF
 *
 */
public interface HessianServerImpDef {
	public Map execRemote (RemoteServerCall remote,Map params);
}
