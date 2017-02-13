package com.zinglabs.util;

import java.util.Properties;
import java.io.*;
import com.zinglabs.util.Utility;

public class I18NConstants {
	public static Properties proerties = null;

	private static String propertiesFilePath = "";

	public static Properties proertiesFile = null;

	public static String getKeyValue(String key) {
		String value;
		try {
			if (proerties == null) {
				proerties = new Properties();
				String fileName = Utility.getContexPath()
						+ "/WEB-INF/report.properties";
				File msgFile = new File(fileName);
				FileInputStream in = new FileInputStream(msgFile);
				proerties.load(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		value = (String) proerties.getProperty(key);
		if (value == null) {
			value = key;
		}
		return value;
	}

	public static void setPropertiesFilePath(String fileName) {
		propertiesFilePath = fileName;
	}

	public static String getValue(String key) {
		String value = "";
		try {
			if (proertiesFile == null) {
				proertiesFile = new Properties();
				String fileName = Utility.getContexPath() + propertiesFilePath;
				File msgFile = new File(fileName);
				FileInputStream in = new FileInputStream(msgFile);
				proertiesFile.load(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		value = (String) proertiesFile.getProperty(key);
		if (value == null) {
			value = key;
		}
		return value;
	}

public static void putValue(String key,String proerties){
		try{
			if(proertiesFile==null){
				proertiesFile=new Properties();
				String fileName = Utility.getContexPath()
				+ propertiesFilePath;
				File msgFile = new File(fileName);
				FileInputStream in = new FileInputStream(msgFile);
				proertiesFile.load(in);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		proertiesFile.put(key,proerties);
		
	}
}
