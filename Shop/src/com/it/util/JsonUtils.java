package com.it.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.lang3.StringEscapeUtils;


/**
 *   如遇以下问题
 *	 The type java.util.Map$Entry cannot be resolved. It is indirectly referenced from 
	 required .class files 
 *   
 *   jdk 版本问题。本地1.7 及以下没问题。1.8有问题
 *
 */

public class JsonUtils {
	
	/**
	 * 构造json字符串
	 * @param success 是否成功
	 * @param mgs 返回提示内容
	 * @return json字符串
	 */
	public static String genUpdateDataReturnJsonStr(boolean success,String mgs){
		HashMap remap=genUpdateDataReturnMap(success,mgs,null);
		//ObjectMapper jsonMapper=new ObjectMapper();
		String rejson="";
		try{
			rejson=oMapper.writeValueAsString(remap);
		}catch(Exception x){
			System.out.println("genUpdateDataReturnJsonStr 1 json 转换失败。\n" + x);
		}
		return rejson;
	}

    /**
     * 构造json字符串
     * @param retMap 返回数据
     * @return json字符串
     */
    public static String getRetJsonStr(Map retMap){
        //ObjectMapper jsonMapper=new ObjectMapper();
        String rejson="";
        try{
            rejson=oMapper.writeValueAsString(retMap);
        }catch(Exception x){
			System.out.println("retMap："+retMap);
			System.out.println("genUpdateDataReturnJsonStr 1 json 转换失败。\n" + x);

        }
        return rejson;
    }
	
	/**
	 * 构造带数据的json字符串
	 * @param <T>
	 * @param success 是否成功
	 * @param mgs 提示内容
	 * @param data 数据
	 * @return json字符串
	 */
	public static <T> String genUpdateDataReturnJsonStr(boolean success,String mgs,T data){
		//ObjectMapper jsonMapper=new ObjectMapper();
		HashMap remap=genUpdateDataReturnMap(success,mgs,data);
		String rejson="";
		try{
			rejson=oMapper.writeValueAsString(remap);
		}catch(Exception x){
			System.out.println("retMap："+data+"----success:"+success+"------mgs:"+mgs);
			System.out.println("genUpdateDataReturnJsonStr 7 json 转换失败。\n" + x);
		}
		return rejson;
	}


	
	/**
	 * 构造带数据的json字符串，并且写入流中
	 * @param <T>
	 * @param success 是否成功
	 * @param mgs 提示内容
	 * @param data 数据
	 * @param os 流对象
	 */
	public static <T> void genUpdateDataReturnJsonStr(boolean success,String mgs,T data,OutputStream os){
		//ObjectMapper jsonMapper=new ObjectMapper();
		HashMap remap=genUpdateDataReturnMap(success,mgs,data);
		try{
			genUpdateDataReturnJsonStr(remap,os);
		}catch(Exception x){
			System.out.println("retMap："+data+"----success:"+success+"------mgs:"+mgs);
			System.out.println("genUpdateDataReturnJsonStr 7 json 转换失败。\n" + x);
		}
	}
	
	/**
	 * 构造带有数据的json字符串，并将JSON写入至response中
	 * @param <T>
	 * @param success 是否成功
	 * @param mgs 提示内容
	 * @param data 数据
	 * @param response HttpServletResponse对象
	 */
	public static <T> void genUpdateDataReturnJsonStr(boolean success,String mgs,T data,HttpServletResponse response){
		//ObjectMapper jsonMapper=new ObjectMapper();
		HashMap remap=genUpdateDataReturnMap(success,mgs,data);
		try{
			genUpdateDataReturnJsonStr(remap,response);
		}catch(Exception x){
			System.out.println("retMap："+data+"----success:"+success+"------mgs:"+mgs);
			System.out.println("genUpdateDataReturnJsonStr 6 json 转换失败。\n" + x);
		}
	}
	
	/**
	 * 构造数据json字符串
	 * @param <T>
	 * @param data 数据
	 * @return 返回json字符串
	 */
	public static <T> String genUpdateDataReturnJsonStr(T data){
		//ObjectMapper jsonMapper=new ObjectMapper();
		String rejson="";
		try{
			rejson=oMapper.writeValueAsString(data);
		}catch(Exception x){
			System.out.println("retMap："+data);
			System.out.println("genUpdateDataReturnJsonStr 3 json 转换失败。\n" + x);
		}
		//System.out.println(rejson);
		return rejson;
	}
	
	/**
	 * 构造数据json字符串，并将数据写入至response
	 * @param <T>
	 * @param data 数据
	 * @param response HttpServletResponse对象
	 */
	public static <T> void genUpdateDataReturnJsonStr(T data,HttpServletResponse response){
		response.setContentType("text/html;charset=UTF-8");
		try{
			genUpdateDataReturnJsonStr(data,response.getOutputStream());
		}catch(Exception x){
			System.out.println("retMap："+data);
			System.out.println("genUpdateDataReturnJsonStr 4 json 转换失败。\n" + x);
		}
	}
	/**
	 * 构造数据json字符串，并将数据写入至流中
	 * @param <T>
	 * @param data 数据
	 * @param os 流对象
	 */
	public static <T> void genUpdateDataReturnJsonStr(T data,OutputStream os){
		//ObjectMapper jsonMapper=new ObjectMapper();
		try{
//			long d1=System.currentTimeMillis();
			oMapper.writeValue(os, data);
//			long d2=System.currentTimeMillis();
			os.flush();
			os.close();
			/*类似日志，在业务功能调用函数位置增加，不要加这里影响整体日志*/
//			logger.debug("json to outputStream time : " + (d2-d1));
//			System.out.println("json to outputStream time : " + (d2-d1));
		}catch(Exception x){
			System.out.println("retMap："+data);
			System.out.println("genUpdateDataReturnJsonStr 5 json 转换失败。\n" + x);
		}
	}
	
	/**
	 * 构造常用返回值的map对象
	 * @param <T>
	 * @param success 是否成功
	 * @param mgs 提示内容
	 * @param data 数据
	 * @return HashMap
	 */
	public static <T> HashMap genUpdateDataReturnMap(boolean success,String mgs,T data){
		HashMap remap=new HashMap();
		remap.put("success", success);
		remap.put("mgs", mgs);
		remap.put("data", data);
		return remap;
	}
	
	
//	private static org.codehaus.jackson.map.ObjectMapper oMapper = null;
	private static ObjectMapper oMapper = null;
	static {
//		oMapper = new org.codehaus.jackson.map.ObjectMapper();
		oMapper = new ObjectMapper();
		// 支持'单引号
		
		oMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		oMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
		oMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

		// 可接受反\字符
		oMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER,true);
		//
		oMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);

//       客户输入特殊字符的情况。chuan.li
        oMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);


	}
	
	/**
	 * String 转 json  
	 * 
	 * @param <T>   
	 * @param str   需要转的字符串
	 * @param clazz  
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> Object stringToJSON(String str, Class<?> clazz)
		throws JsonParseException, JsonMappingException, IOException {
	// 特殊字符串过滤
	   return oMapper.readValue(str, clazz);
	}

	/**
	 * String to json
	 * 直接反回ObjectMapper
	 * @return
	 */
	public static ObjectMapper getObjectMapper(){
		return oMapper;
	}

//	public static org.codehaus.jackson.map.ObjectMapper getObjectMapper(){
//		return oMapper;
//	}


    public static String quote(String string) {
        StringWriter sw = new StringWriter();
            try {
                return quote(string, sw).toString();
            } catch (IOException ignored) {
                // will never happen - we are writing to a string writer
                return "";
            }
    }

    /**
     *
     * @param string
     * @param w
     * @return
     * @throws IOException
     * @desc  这个函数从JSON-java [package org.json] JSONObject.quote获取
     * 转义json字符串中的非法字符,并增加""号
     */
    public static Writer quote(String string, Writer w) throws IOException {
        if (string == null || string.length() == 0) {
            w.write("\"\"");
            return w;
        }

        char b;
        char c = 0;
        String hhhh;
        int i;
        int len = string.length();

        w.write('"');
        for (i = 0; i < len; i += 1) {
            b = c;
            c = string.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    w.write('\\');
                    w.write(c);
                    break;
                case '/':
                    if (b == '<') {
                        w.write('\\');
                    }
                    w.write(c);
                    break;
                case '\b':
                    w.write("\\b");
                    break;
                case '\t':
                    w.write("\\t");
                    break;
                case '\n':
                    w.write("\\n");
                    break;
                case '\f':
                    w.write("\\f");
                    break;
                case '\r':
                    w.write("\\r");
                    break;
                default:
                    if (c < ' ' || (c >= '\u0080' && c < '\u00a0')
                            || (c >= '\u2000' && c < '\u2100')) {
                        w.write("\\u");
                        hhhh = Integer.toHexString(c);
                        w.write("0000", 0, 4 - hhhh.length());
                        w.write(hhhh);
                    } else {
                        w.write(c);
                    }
            }
        }
        w.write('"');
        return w;
    }


	/**
	 *
	 * @param x
	 * @return
	 * 页面使用了JSON.stringify导致'处理不正常
	 * 临时做转义处理，后续统一处理
	 */
	public static String escapeHtml(String x) {

		int stringLength = x.length();
		String parameterAsString = x;

		StringBuilder buf = new StringBuilder((int) (x.length() * 1.1));

		//
		// Note: buf.append(char) is _faster_ than appending in blocks, because the block append requires a System.arraycopy().... go figure...
		//

		for (int i = 0; i < stringLength; ++i) {
			char c = x.charAt(i);
			switch (c) {

				case '\'':
					buf.append("&#x27;");
					break;

//				case '"':
//					buf.append("&quot;");
//					break;
//
//				case '\\':
//					buf.append("\\\\");
//
//					break;

				default:
					buf.append(c);
			}
		}

		parameterAsString = buf.toString();
		return parameterAsString;
	}

}
