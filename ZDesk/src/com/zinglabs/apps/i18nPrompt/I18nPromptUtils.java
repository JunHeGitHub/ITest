package com.zinglabs.apps.i18nPrompt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 国际化提示的工具类 包含方法：
 * 		1.通过key来获取对应的提示信息 
 * 		2.在添加、修改和删除国际化提示后重新载入缓存里数据 
 */
public class I18nPromptUtils {
	
	//缓存数据的容器，定义成Map是方便访问，直接根据Key就可以获取Value了 key选用String是为了简单
	public static Map<String, String> i18nPromptData = new HashMap<String,String>();
	private static Map<String, String> mapTemp = new HashMap<String, String>(); //第一次访问时存入的map  map中存放的是程序语言代码和程序代码
	public static Map<String, String> langugeCodeAndProjectCode = new HashMap<String, String>(); //程序语言代码和程序代码
	private static String conversionStringTemp ="";//存放需要转换的key
	
	/** 
     * 本地字符 Ascii码字符串 前缀 声明变量
     */  
    private static String PREFIX = "\\u";
    
	/**
	 * 加载提示数据
	 */
    static{
    	i18nPromptData=new I18nPromptUtils().getI18nPromptMap();
    	Map<String, String> tm =new HashMap<String, String>();
    	tm.put("langugeCode", "zh_CN");
    	tm.put("projectCode", "YLX");
    	mapTemp=tm;
    	langugeCodeAndProjectCode=tm;
    }
    
	/**
	 * 通过key来获取对应的提示信息
	 * @param key 传入想要提取哪些值的key
	 * @return
	 */
	public static String getText(String key){
		//I18nPromptUtils c =new I18nPromptUtils();
		//Map<String, String> map=c.getI18nPromptMap();
		Map<String, String> map = i18nPromptData;
		if (map!=null) {
			if(map.get(key)!=""){
				return map.get(key);  //取到值返回对应的提示内容				
			}			
		}
		return null;  //未取到值返回null
	}
	/**
	 * 通过key来获取对应的提示信息
	 * @param key 传入想要提取哪些值的key
	 * @param paramsMap 提示信息中需要插入显示的数据集
	 * @return
	 */
	public static String getText(String promptkey,Map<String, String> paramsMap){
		Map<String, String> map = i18nPromptData;
		if (map!=null) {
			if(map.get(promptkey)!=null && map.get(promptkey)!=""){
				String promptMsg=map.get(promptkey);
				if(paramsMap!=null){
					for (String key : paramsMap.keySet()) {
						promptMsg=promptMsg.replace(key, paramsMap.get(key));
					}
				}
				return promptMsg;  //取到值返回对应的提示内容				
			}			
		}
		return null;  //未取到值返回null
	}
	/**
	 * 获取国际化提示信息的map集合
	 * @param map map中存放的是程序语言代码和程序代码
	 * @param conversionString 存放需要转换的key 把key里的值转换成对应国家的语言
	 * @return 返回HashMap集合
	 */
	public static HashMap<String, String> getI18nPromptData(HashMap<String, String> map,String conversionString) {
		I18nPromptUtils c =new I18nPromptUtils();
		mapTemp=map;
		conversionStringTemp=conversionString;
		return c.getI18nPromptMap();

	}
	/**
	 * 获取国际化提示数据的实际操作方法
	 * @return
	 */
	private HashMap<String, String> getI18nPromptMap() {
		I18nPromptService i18nPromptService=new I18nPromptService();
		HashMap<String, String> promptData = (HashMap<String, String>)i18nPromptService.getI18nPromptData(mapTemp,conversionStringTemp);
		
		// 返回数据
		return promptData;

	}
	/**
	 * 在添加、修改和删除国际化提示后重新载入缓存里面的数据
	 */
	public static void reloadI18nPromptCache(){
		I18nPromptService i18nPromptService=new I18nPromptService();
		//读取数据并把读取到的数据重新存入缓存里面
		i18nPromptData = (HashMap<String, String>)i18nPromptService.getI18nPromptData(mapTemp,conversionStringTemp);
	}
	
	
	 //实现同java native2Ascii.exe的进行多国语言互转的功能
	 //例： 你好！> \u4f60\u597d\uff01
	/** 
     * 本地字符 转 Ascii码字符串 例：你好！ > \u4f60\u597d\uff01 由于个别国家语言测试出现问题，目前未用到，中文正常
     * @param str 
     * @return ascii string 
     */  
    public static String native2Ascii(String str) {
        char[] chars = str.toCharArray();  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < chars.length; i++) {  
            sb.append(char2Ascii(chars[i]));  
        }  
        return sb.toString();  
    }  
  
    /** 
     *  
     * @param c 
     * @return ascii string 
     */  
    private static String char2Ascii(char c) {  
        if (c > 255) {  
            StringBuilder sb = new StringBuilder();  
            sb.append(PREFIX);  
            int code = (c >> 8);  
            String tmp = Integer.toHexString(code);  
            if (tmp.length() == 1) {  
                sb.append("0");  
            }  
            sb.append(tmp);  
            code = (c & 0xFF);  
            tmp = Integer.toHexString(code);  
            if (tmp.length() == 1) {  
                sb.append("0");  
            }  
            sb.append(tmp);  
            return sb.toString();  
        } else {  
            return Character.toString(c);  
        }  
    }  
  
    /** 
     * Ascii字符码 转 本地字符 例：\u4f60\u597d\uff01 > 你好！
     * @param str 
     * @return native string 
     */  
    public static String ascii2Native(String str) {  
        StringBuilder sb = new StringBuilder();  
        int begin = 0;  
        int index = str.indexOf(PREFIX);  
        while (index != -1) {  
            sb.append(str.substring(begin, index));  
            sb.append(ascii2Char(str.substring(index, index + 6)));  
            begin = index + 6;  
            index = str.indexOf(PREFIX, begin);  
        }
        sb.append(str.substring(begin));  
        return sb.toString();  
    }  
  
    /** 
     * @param str 
     * @return native character 
     */  
    private static char ascii2Char(String str) {  
        if (str.length() != 6) {
            throw new IllegalArgumentException("一个本地字符的ASCII字符串必须是6个字符。");  
        }  
        if (!PREFIX.equals(str.substring(0, 2))) {  
            throw new IllegalArgumentException("一个本地字符的ASCII字符串的格式应为\"\\u\".");  
        }  
        String tmp = str.substring(2, 4);  
        int code = Integer.parseInt(tmp, 16) << 8;  
        tmp = str.substring(4, 6);  
        code += Integer.parseInt(tmp, 16);  
        return (char) code;  
    }  
    /**
     * 将Ascii字符码转换成对应的语言
     * @param ls 传入list集合
     * @param conversionString 传入需要转换的map中key
     * @return 返回转换后的list集合
     */
    public static List<Map<String, String>> ascii2Native(List<Map<String, String>> ls,String conversionString) {
    	Map<String, String> mapTemp=new HashMap<String, String>();
		String conversionValue="";//转换后的字符串变量
		List<Map<String, String>> conversionList=new ArrayList<Map<String,String>>();
    	if(ls.size()!=0 && ls!=null){
    		for (int i = 0; i < ls.size(); i++) {
    			mapTemp=ls.get(i);//获取map数据
    			//遍历map中的数据进行重组
    			for (String key : mapTemp.keySet()) {
    				//进行指定key值的转换
    				if(key.toString().equals(conversionString)){
    					conversionValue=I18nPromptUtils.ascii2Native(mapTemp.get(key).toString()).toString();
    					mapTemp.put(key,conversionValue);
    				}
    			}
    			conversionList.add(mapTemp);
    		}
    		return conversionList;
    	}
    	return ls;//如果传入的数据出现问题则把传入的数据返还回去
    	
    }
    /**
     * 将Ascii字符码转换成对应的语言
     * @param ls 传入Map集合
     * @param conversionString 传入需要转换的map中key
     * @return 返回转换后的Map集合
     */
    public static Map<String, String> ascii2Native(Map<String, String> map,String conversionString) {
    	Map<String, String> mapTemp=new HashMap<String, String>();
		String conversionValue="";//转换后的字符串变量
    	if(map.size()!=0 && map!=null){
    		//遍历map中的数据进行重组
    		for (String key : map.keySet()) {
    			//进行指定key值的转换
    			if(key.toString().equals(conversionString)){
    				conversionValue=I18nPromptUtils.ascii2Native(map.get(key).toString()).toString();
    				mapTemp.put(key,conversionValue);
    			}
    		}
    		return mapTemp;
    	}
    	return map;//如果传入的数据出现问题则把传入的数据返还回去
    	
    }
    /**
     * 将本地字符语言转换成对应ascii字符码  个别国家语言暂时有问题 暂未使用 中文没有问题
     * @param map
     * @param conversionString
     * @return
     */
    public static HashMap<String,String> native2Ascii(HashMap<String,String> map,String conversionString) {
    	
    	String conversionValue="";//转换后的字符串变量
		if(map.size()!=0 && map!=null){
			for (String key : map.keySet()) {
				//进行指定key值的转换
				if(key.toString().equals(conversionString.toString())){
					conversionValue=I18nPromptUtils.native2Ascii(map.get(key).toString()).toString();
					map.put(key,conversionValue);
				}
			}
			return map;
		}
		return map;//如果传入的数据出现问题则把传入的数据返还回去
    }
    
    public static void main(String[] args) {  
    	//System.out.println(native2Ascii("¡Buenos das!"));
        //System.out.println(ascii2Native("\u0e2a\u0e27\u0e31\u0e2a\u0e14\u0e35"));
    	//reloadI18nPromptCache();
    	Map<String, String> map=new HashMap<String, String>();
    	map.put("${0}", "qqq");
    	map.put("${1}", "www");
    	System.out.println(getText("SystemGeneration_yuyanchuangjianchenggong", map));
    }

}
