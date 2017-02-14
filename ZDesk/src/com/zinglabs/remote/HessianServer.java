package com.zinglabs.remote;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.remote.server.implDef.HessianServerImpDef;
import com.zinglabs.remote.server.implDef.RemoteServerCall;


/**
 * 远程调用服务端
 * @author QCF
 *
 */
public class HessianServer implements HessianServerImpDef{
	public Map execRemote(RemoteServerCall remote, Map params) {
		Map map=null;
		map=remote.beforeExec(params);
		if(map==null){
			map=remote.exec(params);
		}else{
			map=remote.exec(map);
		}
		map=remote.afterExec(map);
		return map;
	}
	
}
