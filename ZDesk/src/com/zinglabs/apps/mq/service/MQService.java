package com.zinglabs.apps.mq.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ibm.mq.MQException;
import com.zinglabs.log.LogUtil;
import com.zinglabs.mq.MQSend;
import com.zinglabs.tools.Utility;

public class MQService {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");

	private static Map<String, Map<String, String>> conf = new HashMap<String, Map<String, String>>();
	
	static {
		Properties properties = new Properties();
		try {
			properties.load(MQService.class.getClassLoader().getResourceAsStream("mqConf.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Entry<Object, Object> pro : properties.entrySet()) {
			String key = (String) pro.getKey();
			if (key != null) {
				String[] val = key.split("_");
				String keyStart = val[0];
				String keyEnd = val[1];
				Map<String, String> map = conf.get(keyStart);
				if (map == null) {
					map = new HashMap<String, String>();
				}
				map.put(keyEnd, (String) pro.getValue());
				conf.put(keyStart, map);
			}
		}
	}
	/**
	 * 
	 * @param gdType 用于区分不同的工单类型，在mqConf.properties中需要定义:xxx_class(javabean名称)、 xxx_type（工单类型,参考报文编码表
	 * @param gdId 工单id
	 * @param path 上传文件路径
	 * @param map 工单表单数据
	 * @throws MQException
	 * @throws ClassNotFoundException
	 */
	public void sendMsg(String gdType, String gdId, String path, Map map)
			throws MQException, ClassNotFoundException {
		String contentUTF = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		contentUTF += "<sr id=\"" + gdId + "\" type=\"" + conf.get(gdType).get("type") + "\" serialNo=\""+ gdId +"\">";
		
		Class<?> clas = Class.forName(conf.get(gdType).get("class"));
		Field[] fields = clas.getDeclaredFields();
		for (Field field : fields) {
			String nameStr = field.getName();
			String names[] = nameStr.split("_2_");
			String type = field.getType().getSimpleName();
			String value = (String) map.get(names[0]);
			contentUTF += "<field type=\"" + type + "\" name=\"" + names[1] + "\" value=\"" + (value == null ? "" : value) + "\"/>";
		}
		if (path != null && path.trim().length() > 0) {
			File file = new File(path);
			if (file.exists()) {
				String fileNameReal = path;
				if (path.indexOf("/") != -1) {
					fileNameReal = path.substring(path.lastIndexOf("/") + 1,path.length());
				}
				LogUtil.debug("fileNameReal 1 :" + path, logger);

				String fileCode = Utility.fileToEncodeStr2(path);
				if (fileCode != null) {
					contentUTF += "<attachement name=\"" + fileNameReal + "\" value=\"" + fileCode + "\" /></sr>";
				} else {
					contentUTF += "<attachement name=\"\" value=\"\" /></sr>";
				}
			}
		} else {
			contentUTF += "<attachement name=\"\" value=\"\" /></sr>";
		}

		logger.debug("SaveOrderMQ xml:" + contentUTF);
		MQSend mqsend = new MQSend();
		mqsend.send2(contentUTF);
		
	}
	
	public static void main(String[] args) {
		Map map = new HashMap();
		map.put("gongdanId","ssss");
		map.put("callInNum","ssss");
		map.put("gongdanSource","eeeee");
		map.put("organize","xxxx");
		map.put("gongdanLevel","adfasd");
		map.put("belongBank","xxxx");
		map.put("channelName","wwww");
		
		MQService mqService = new MQService();
		try {
			mqService.sendMsg("Fuwuyuyuedan", "wwwww", "d:/excel地址.txt", map);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (MQException e) {
			e.printStackTrace();
		} 
	}
}
