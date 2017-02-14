package com.zinglabs.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {
	public static Properties getProperties(String file) {
		Properties pro = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			pro = new Properties();
			pro.load(in);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pro;
	}

}
