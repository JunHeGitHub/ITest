package com.zinglabs.apps.i18nPrompt;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class ReadProperties {
	
	public static List<Map<String, String>> readProperties(String filePath) {
		I18nPromptService is =new I18nPromptService();
		Properties props = new Properties();		
		List<Map<String, String>> ls=new ArrayList<Map<String, String>>();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			props.load(in);
			in.close();
			Enumeration en = props.propertyNames();
			while (en.hasMoreElements()) {
				Map<String, String> map=new HashMap<String, String>();
				String key = (String) en.nextElement();
				String Property = props.getProperty(key);
				map.put("promptKey", key);
				map.put("promptValue", Property);
				ls.add(map);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ls;
	}

	public static void main(String[] args) {
		//writeProperties("E:/test.properties", "age", "21");
		readProperties("E:/test.properties");
		//System.out.println("OK");
	}
}
