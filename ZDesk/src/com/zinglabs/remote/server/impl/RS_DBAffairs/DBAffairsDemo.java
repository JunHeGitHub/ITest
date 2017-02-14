package com.zinglabs.remote.server.impl.RS_DBAffairs;

import java.io.Serializable;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zinglabs.remote.server.implDef.DBAffairsImpDef;
/**
 * 数据业务数据事务原子操作实现示例
 * @author QCF
 *
 */
public class DBAffairsDemo implements DBAffairsImpDef,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	
	public Map execute(Map params) {
		String str1=executeType1(params);
		String str2=executeType2(params);
		Map map=new HashMap();
		map.put("str1", str1);
		map.put("str2", str2);
		return map;
	}
	
	public String executeType1(Map p){
		System.out.println("executeType1");
		return "type1";
	}
	
	public String executeType2(Map p){
		System.out.println("executeType2");
		return "type2";
	}
}
