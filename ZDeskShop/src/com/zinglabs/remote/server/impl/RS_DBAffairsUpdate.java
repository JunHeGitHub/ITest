package com.zinglabs.remote.server.impl;

import java.io.Serializable;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.remote.server.implDef.DBAffairsImpDef;
import com.zinglabs.remote.server.implDef.RemoteServerCall;

/**
 * 数据库业务性事务更新，远程调整实现类
 * @author QCF
 *
 */
public class RS_DBAffairsUpdate implements RemoteServerCall,Serializable{
	private static final long serialVersionUID = 8360404529275363378L;
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	
	public Map afterExec(Map map) {
		return map;
	}

	public Map beforeExec(Map map) {
		return map;
	}
	
	public Map exec(Map map) {
		Map rmap=null;
		if(map!=null){
			String classz=map.get(DBAffairsImpDef.CLASS_KEY)==null?"":(String)map.get(DBAffairsImpDef.CLASS_KEY);
			if(classz.length()>0){
				DBAffairsImpDef did=RS_DBAffairsUpdateHelper.initializeClass(classz);
				rmap=RS_DBAffairsUpdateHelper.execute(did,map);
			}else{
				logger.debug("debug RS_DBAffairsUpdate.exec : " + DBAffairsImpDef.CLASS_KEY + " paramr is null.");
			}
		}else{
			logger.debug("debug RS_DBAffairsUpdate.exec : paramer map is null.");
		}
		return rmap;
	}
}
