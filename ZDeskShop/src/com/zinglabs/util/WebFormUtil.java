package com.zinglabs.util;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class WebFormUtil {
	private HttpServletRequest request;
	public WebFormUtil(HttpServletRequest request){
		this.request=request;
	}
	public String get(String key){
		String v=request.getParameter(key)==null?"":request.getParameter(key);
		if(v.length()<=0){
			v=request.getAttribute(key)==null?"":(String)request.getAttribute(key);
		}
		return v;
	}
	
	public void set(String key,String value){
		request.setAttribute(key, value);
	}
	
	/**
	 * 从request中获得参数Map，并返回可读的Map
	 *
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map getParameterMap(HttpServletRequest request) {
	    // 参数Map
	    Map properties = request.getParameterMap();
	    // 返回值Map
	    Map returnMap = new HashMap();
	    Iterator entries = properties.entrySet().iterator();
	    Map.Entry entry;
	    String name = "";
	    String value = "";
	    while (entries.hasNext()) {
	        entry = (Map.Entry) entries.next();
	        name = (String) entry.getKey();
	        Object valueObj = entry.getValue();
	        if(null == valueObj){
	            value = "";
	        }else if(valueObj instanceof String[]){
	            String[] values = (String[])valueObj;
	            for(int i=0;i<values.length;i++){
	                value = values[i] + ",";
	            }
	            value = value.substring(0, value.length()-1);
	        }else{
	            value = valueObj.toString();
	        }
	        returnMap.put(name, value);
	    }
	    return returnMap;
	}
}
