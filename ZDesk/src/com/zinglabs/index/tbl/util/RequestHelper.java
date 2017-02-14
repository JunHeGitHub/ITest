package com.zinglabs.index.tbl.util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * 
 */
public class RequestHelper {
	public static final String KEY_REFRESH_URL = "refreshUrl";

	public static void setRequestObject(HttpServletRequest request, String key,
			Object value) {
		request.setAttribute(key, value);
	}

	public static Object getRequestObject(HttpServletRequest request, String key) {
		return request.getAttribute(key);
	}

	/**
	 * @param request
	 * @param key
	 * @param value
	 * @param isSetAttrubute
	 */
	public static void setSessionObject(HttpServletRequest request, String key,
			Object value) {
		request.getSession().setAttribute(key, value);
	}

	public static void removeSessionObject(HttpServletRequest request,
			String key) {
		request.getSession().removeAttribute(key);
	}

	
	public static Object getSessionObject(HttpServletRequest request, String key) {
		return request.getSession().getAttribute(key);
	}
	
	
	public static int getIntParam(HttpServletRequest reqeust, String key,
			int defaultValue) {
		String value = reqeust.getParameter(key);
		if (value == null) {
			return defaultValue;
		} else {
			return Integer.parseInt(value);
		}
	}
	
	
	public static String getStringParam(HttpServletRequest request, String key) {
		return request.getParameter(key);
	}

	
	public static String getStringParamNotNull(HttpServletRequest request, String key) {
		return request.getParameter(key)==null?"":request.getParameter(key);
	}
	
	public static String[] getStrsParam(HttpServletRequest request, String keys[]){
		
		int str_len = keys.length;
		String result[] = new String[str_len];
		for(int i = 0 ; i < str_len ; i ++ ){
			result[i] = getStringParamNotNull(request, keys[i]);
		}
		
		return result;
		
	}
	
	
	
	public static List<Map> getParamsList(HttpServletRequest request,int count ,String keys[]){
		
		if(count==0){
			return null;
		}
		ArrayList<Map> result = new ArrayList<Map>();
		int keys_len = keys.length;
		String temp[] = new String[keys_len];
		for(int i = 1 ; i <= count ; i ++ ){
			
			for(int j = 0 ; j < keys_len ; j ++ ){
				temp[j] = keys[j] + i;
			}
			result.add(getParameterFromPage(request,keys,temp));
		}
		
		return result;
	}
	
	
	

	public static List<String[]> getParameterValues(HttpServletRequest request,String keys[]){
	
		if(keys.length == 0){
			return null;
		}
		ArrayList<String[]> result = new ArrayList<String[]>();
		int keys_len = keys.length;
		
		for(int i = 0 ; i < keys_len ; i ++ ){
			result.add(request.getParameterValues(keys[i]));
		}
		
		return result;
	}
	
	
	
	public static java.util.Date getParameterAsDate(HttpServletRequest request,
			String key, java.util.Date dft) {

		String s = request.getParameter(key);
		if (s == null || s.equals("")){
			return dft;
		}

		s = s.trim();
		if (s.equals("")){
			return dft;
		}

		DateFormat df = DateFormat.getDateInstance();
		try {
			return df.parse(s);
		} catch (Exception ex) {
			return dft;
		}
	}
	
	
	public static int getParameterAsInt(HttpServletRequest request, String key,
			int dft) {
		String s = request.getParameter(key);
		if (s == null || s.equals("")){
			return dft;
		}

		s = s.trim();
		if (s.equals("")){
			return dft;
		}

		try {
			return Integer.parseInt(s);
		} catch (Exception ex) {
			return dft;
		}
	}

	
	public static Integer getParameterAsInteger(HttpServletRequest request,
			String key, int dft) {
		String s = request.getParameter(key);
		Integer integer = new Integer(dft);
		if (s == null || s.equals("")){
			return integer;
		}

		s = s.trim();
		if (s.equals("")){
			return integer;
		}

		try {
			return new Integer(s);
		} catch (Exception ex) {
			return integer;
		}
	}

	
	public static Double getParameterAsDouble(HttpServletRequest request,
			String key, Double dft) {
		String s = request.getParameter(key);
		if (s == null || s.equals("")){
			return dft;
		}

		s = s.trim();
		if (s.equals("")){
			return dft;
		}

		try {
			return new Double(s);
		} catch (Exception ex) {
			return dft;
		}
	}

	
	public static Map<String,String> getParameterFromPage(HttpServletRequest request, String[] keys){
		if(keys == null || keys.length < 1){
			return null;
		} else {
			Map<String,String> param = new HashMap<String,String>();
			for (int i = 0; i < keys.length; i++) {
				String key = keys[i];
				if(key != null && !"".equals(key)){
					String value = request.getParameter(key);
 					//System.out.println(value+" ȡ��ҳ���ı����е�ֵ");//---
					if(null == value || "".equals(value)){
						value = null;
					}
					param.put(key, value);
				}
			}
			return param;
		}
	}
	
	
	
	public static Map<String,String> getParameters(HttpServletRequest request, String[] keys,String replaceStr){
		if(keys == null || keys.length < 1){
			return null;
		} else {
			Map<String,String> param = new HashMap<String,String>();
			for (int i = 0; i < keys.length; i++) {
				String key = keys[i];
				if(key != null && !"".equals(key)){
					String value = request.getParameter(key);
					if(null == value || "".equals(value)){
						value = replaceStr;
					}
					param.put(key, value);
				}
			}
			return param;
		}
	}
	
	
	public static Map<String,String> getParameterFromPage(HttpServletRequest request, String[] keys, String[] keysTemp){
		if(keys == null || keys.length < 1){
			return null;
		} else {
			Map<String,String> param = new HashMap<String,String>();
			for (int i = 0; i < keys.length; i++) {
				String keyTemp = keysTemp[i];
				if(keyTemp != null && !"".equals(keyTemp)){
					String value = request.getParameter(keyTemp);
					if(null == value || "".equals(value)){
						value = null;
					}
					param.put(keys[i], value);
				}
			}
			return param;
		}
	}
	
	
	public static String getQueryStringFromMap(Map paramMap){
		StringBuffer buffer = new StringBuffer();
		
		Set keySet = paramMap.keySet();
		for(Iterator it = keySet.iterator();it.hasNext();){
			String keyStr = String.valueOf(it.next());
			if((!"null".equals(paramMap.get(keyStr))) &&(!"".equals(paramMap.get(keyStr))) && paramMap.get(keyStr)!= null){
				buffer.append(keyStr).append("=").append(paramMap.get(keyStr)).append("&");
			}
		}
		if(buffer.length() > 0){
			buffer.setLength(buffer.length() - 1);
		}		
		return buffer.toString();
	}
	
	public static void main(String s[]){
		//StringBuffer buffer = new StringBuffer();
		//System.out.println("111" + buffer.toString() + "222");
	}
}
