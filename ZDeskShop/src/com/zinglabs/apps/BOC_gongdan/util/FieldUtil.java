package com.zinglabs.apps.BOC_gongdan.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FieldUtil {

	/**
	 * 过滤掉spellMyIbatis方法不需要的参数,把这些参数整理到另一个HashMap中
	 * 
	 * @param map
	 * @return
	 */
	public static Map activitiMoveCommonInfo(Map map) {
		Map dmap = new HashMap();
		if (map.get("tableName") != null) {
			dmap.put("tableName", map.get("tableName"));
			map.remove("tableName");
		}
		if (map.get("nameSpace") != null) {
			dmap.put("nameSpace", map.get("nameSpace"));
			map.remove("nameSpace");
		}
		if (map.get("nameSpaceId") != null) {
			dmap.put("nameSpaceId", map.get("nameSpaceId"));
			map.remove("nameSpaceId");
		}
		if (map.get("orderBy") != null) {
			dmap.put("orderBy", map.get("orderBy"));
			map.remove("orderBy");
		}
		if (map.get("limit") != null) {
			dmap.put("limit", map.get("limit"));
			map.remove("limit");
		}
		if (map.get("primaryKey") != null) {
			dmap.put("primaryKey", map.get("primaryKey"));
			map.remove("primaryKey");
		}
		if (map.get("primaryKeyValue") != null) {
			dmap.put("primaryKeyValue", map.get("primaryKeyValue"));
			map.remove("primaryKeyValue");
		}
		if (map.get("uuid") != null) {
			map.remove("uuid");
		}
		return dmap;
	}
	
	public static Map spellMap(Map map){
		if(map!=null){
			Map<String,Object> dMap=new HashMap<String,Object>();
			List<String> fields=new ArrayList<String>();
			List<String> fieldsValue=new ArrayList<String>();
			Iterator<Map.Entry<String, String>> it=map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String,String> e=it.next();
				if(e.getKey()==null || e.getKey().length()<=0 || e.getKey().equals("zkm_userName") || e.getKey().equals("r")){
					continue;
				}
				String key=e.getKey();
				String value=e.getValue()==null?"":e.getValue();
				fields.add(key);
				fieldsValue.add(value);
			}
			dMap.put("fields", fields);
			dMap.put("fieldsValue", fieldsValue);
			return dMap;
		}
		return map;
	}
	
	public static Map<String,Object> spellUpdateMap(Map<String,Object> map){
		if(map!=null){
			Map<String,Object> dMap=new HashMap<String,Object>();
			List<String> fields=new ArrayList<String>();
			Iterator<Map.Entry<String, Object>> it=map.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String,Object> e=it.next();
				if(e.getKey()==null || e.getKey().length()<=0 || e.getKey().equals("zkm_userName") || e.getKey().equals("r")){
					continue;
				}
				String key=e.getKey();
				fields.add(key);
			}
			dMap.put("update_fields", fields);
			dMap.putAll(map) ;
			return dMap;
		}
		return map;
	}
}
