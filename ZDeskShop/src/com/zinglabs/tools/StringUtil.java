package com.zinglabs.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zinglabs.base.Interface.BaseInterface;



public class StringUtil {
	



	

	public String replaceAllFirst(String target, String oldStr, String newStr) {
		if (target != null && target.length() > 0) {
			while (target.startsWith(oldStr)) {
				target = target.replaceFirst(oldStr, newStr);
			}
		}
		return target;
	}

	public String changeCharset(String str, String oldCharset, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			//用旧的字符编码解码字符串。解码可能会出现异常。
			byte[] bs = str.getBytes(oldCharset);
			//用新的字符编码生成字符串
			return new String(bs, newCharset);
		}
		return null;
	}

	public String changeCharset(String str, String newCharset)
			throws UnsupportedEncodingException {
		if (str != null) {
			//用默认字符编码解码字符串。
			byte[] bs = str.getBytes();
			//用新的字符编码生成字符串
			return new String(bs, newCharset);
		}
		return null;
	}

	/**
	 * 
	 * @param str seed to gen string
	 * @param length 
	 * @return string with str*length otherwish StringBuffer with nothing
	 */
	public String getLengthStr(String str, int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(str);
		}
		return sb.toString();
	}
	


}
