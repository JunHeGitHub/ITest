package com.zinglabs.apps.Dictmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.apps.i18nPrompt.I18nPromptUtils;

public class DictmanagerUtils {
	public static Map<String, List> DictMap = new HashMap();
	public static List DictIndex = new ArrayList();
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	/**
	 * 根据索引查询数据表数据
	 * 
	 * @return
	 */
	static DictmanagerService dictmanager = new DictmanagerService();

	public static Map<String, List> searchDicData() {
		Map<String,List> queryMap = new HashMap<String,List> ();
		Map map = new HashMap();
		Map<String, List> indexmap = new HashMap<String, List>();
		List<Map> indexList = new ArrayList();
		indexList = dictmanager.getServiceSqlSession().db_selectList("selectDicIndex", map);
		if ((indexList.size() > 0)) {
			for (int i = 0; i < indexList.size(); i++) {
				List<Map> dataComboIndexList = new ArrayList<Map>();
				String indexCode = "";
				indexCode = (String) indexList.get(i).get("indexCode");
				dataComboIndexList = (List<Map>)dictmanager.getServiceSqlSession().db_selectList("groupBYindexCode",indexList.get(i));
				if(dataComboIndexList.size()>0){
//					dataComboIndexList = I18nPromptUtils.ascii2Native(dataComboIndexList,"name");
//					dataComboIndexList = I18nPromptUtils.ascii2Native(dataComboIndexList,"indexName");
					queryMap.put(indexCode, dataComboIndexList);
				}else{
					
				}
			}
		}
		if (!(queryMap.size() > 0)) {
			logger.error("根据字典索引未查询出字典项数据");
		} 
		return queryMap;
	}

	/**
	 * 获取数据表数据,缓存不为空,取缓存。为空重新查询。
	 * 
	 * @return
	 */
	public static Map<String, List> getDicData() {
		Map<String, List> data = new HashMap<String, List>();
		if (DictMap.size() == 0) {
			data = searchDicData();
			DictMap = data;
		} else {
			data = DictMap;
		}
		return data;
	}

	/**
	 * 字典项数据重加载到缓存
	 */
	public static void reloadDicData() {
		DictMap = searchDicData();
	}

	/**
	 * 字典项索引重新加载到缓存
	 * 
	 * @return
	 */
	public static void reloadDicIndex() {
		Map map = new HashMap();
		List list = dictmanager.selectIndexData(map);
		DictIndex = list;
	}

	/**
	 * 获取字典索引
	 * 
	 * @return
	 */
	public static List getDicIndex() {
		List data = new ArrayList();
		if (DictMap.size() == 0) {
			reloadDicIndex();
		}
		data = DictIndex;
		return data;
	}

}
