package com.zinglabs.remote.server.impl;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.remote.server.implDef.DBAffairsImpDef;
public class RS_DBAffairsUpdateHelper {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	
	public static DBAffairsImpDef initializeClass(String classz){
		DBAffairsImpDef did=null;
		try{
			Object obj=Class.forName(classz).newInstance();
			if(obj instanceof DBAffairsImpDef){
				did=(DBAffairsImpDef)obj;
			}else{
				logger.error("error RS_DBAffairsUpdateHelper.initializeClass : " + classz + "  is not DBAffairsImpDef.");
			}
		}catch(Exception x){
			x.printStackTrace();
			logger.error("error RS_DBAffairsUpdateHelper.initializeClass : " + classz + " \n "+ x.getMessage());
		}
		return did;
	}
	/**
	 * 加锁的执行
	 * @param did
	 * @param map
	 * @return
	 */
	public static synchronized Map execute(DBAffairsImpDef did,Map map){
		return did.execute(map);
	}
}
