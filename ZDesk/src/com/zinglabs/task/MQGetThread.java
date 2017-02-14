package com.zinglabs.task;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zinglabs.apps.activitiCommon.ActivitiCommonService;
import com.zinglabs.apps.mq.service.MQService;
import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.base.core.frame.SysPropertiesUtil;
import com.zinglabs.base.core.springSupport.DbidSqlsession;
import com.zinglabs.base.core.utils.AppSpringUtils;
import com.zinglabs.log.LogUtil;
import com.zinglabs.mq.MQGet;
import com.zinglabs.tools.DOMTool;
import com.zinglabs.tools.ExcelTools;
import com.zinglabs.tools.Utility;

public class MQGetThread extends BaseAbs implements Runnable {

	public static Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk");
	
	private static String savePath;
	
	private static DbidSqlsession sqlsession = null;
	
	static {
		ApplicationContext applicationContext =AppSpringUtils.getApplicationContext();
		sqlsession = (DbidSqlsession)applicationContext.getBean("mqSqlSession");
		List list = SysPropertiesUtil.getConfWithBItemAndProductionIdAndPeizhiItem("conf", "mqsync", "mq");
		savePath = (String)((Map)list.get(0)).get("peizhiItemValue");
	}

	public MQGetThread() {
		init(SKIP_Logger);
	}

	public void run() {
		try {
			Thread.sleep(20000L);
		} catch (InterruptedException e) {
			error("MQGetThread run");
			error(e);
		}
		try {
			Map map = pullData();
			insertData(map);
			
		} catch (Exception e) {
			error(e);
		}
		info("get order end ");
	}
	
	
	private Map pullData() throws Exception{
		MQGet mqGet = new MQGet();
		String retTmp = mqGet.getMqMsg();

		if (retTmp == null || retTmp.length() == 0) {
			return null;
		}
		String content = retTmp;
		String contentGBx = new String(content.getBytes("ISO-8859-1"),"gbk");
		String contentGBx1 = new String(content.getBytes("ISO-8859-1"),"utf-8");
		String contentGB2 = new String(content.getBytes("gbk"));
		String contentGB3 = new String(content.getBytes("UTF-8"));
		String contentGB4 = new String(content.getBytes("ISO-8859-1"));
		LogUtil.debug("contentGBx:" + contentGBx + "contentGBx1:" + contentGBx1
						+ " contentGB2:" + contentGB2 + " contentGB3:"
						+ contentGB3 + " contentGB4:" + contentGB4,SKIP_Logger);

		Map map = Xml2MapPaser(contentGBx1);
		return map;
	}
	
	private void insertData(Map map){
		if(map != null){
			ActivitiCommonService activitiCommonService = new ActivitiCommonService();
			Map retMap = activitiCommonService.docomplete(map);
			map.putAll(retMap);
			sqlsession.db_insert("insertMq",map);
		}
	}
	/**
	 * 将xml转换成map
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private Map Xml2MapPaser(String context) throws Exception{
		Map map = new HashMap();
		Document doc = DOMTool.loadDocumentFromStr(context, "utf-8");
		
		Node dWNode = DOMTool.getSingleNode(doc, "sr");
		String type = DOMTool.getAttributeValue((Element) dWNode, "type");
		if (type != null && type.length() > 0) {
			map.put("type", type);
		}

		String serialNo = DOMTool.getAttributeValue((Element) dWNode,"serialNo");
		if (serialNo != null && serialNo.length() > 0) {
			map.put("serialNo", serialNo);
		}
		
		String idcodeTmp = DOMTool.getAttributeValue((Element) dWNode, "id");
		if (idcodeTmp != null && idcodeTmp.length() > 0) {
			map.put("idcode", idcodeTmp);
		}
		
		Element e1 = null;                        
		NodeList nl = DOMTool.getMultiNodes(doc, "field");
		for (int i = 0; i < nl.getLength(); i++) {
			e1 = (Element) nl.item(i);
			String name = DOMTool.getAttributeValue(e1, "name");
			String value = DOMTool.getAttributeValue(e1, "value");
			if(value == null || "null".equals(value)){
				value = "";
			}
			map.put(name, value);
		}
		
		nl = DOMTool.getMultiNodes(doc, "attachement");
		for (int i = 0; i < nl.getLength(); i++) {
			e1 = (Element) nl.item(i);
			String name = DOMTool.getAttributeValue(e1, "name");
			String value = DOMTool.getAttributeValue(e1, "value");

			if (name != null && name.length() > 0 && value != null && value.length() > 0) {
				String path = savePath + File.separator + name;
				Utility.encodeStrToFile(value, path);
				map.put("attachement", path);
				break;
			}
		}
		return map;
	}
	
	public static void main(String[] args) {
		List list = SysPropertiesUtil.getConfWithBItemAndProductionIdAndPeizhiItem("conf", "mqsync", "mq");
		System.out.println(((Map)list.get(0)).get("peizhiItemValue"));
		
	}
}
