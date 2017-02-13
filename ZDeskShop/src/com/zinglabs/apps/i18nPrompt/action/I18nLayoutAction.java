package com.zinglabs.apps.i18nPrompt.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.i18nPrompt.I18nLayoutService;
import com.zinglabs.apps.i18nPrompt.I18nPromptUtils;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping("/*/i18nLayout")
public class I18nLayoutAction  extends BaseAction {
	public static  Logger logger = LoggerFactory.getLogger("ZDesk"); 
	/**
	 * 数据查询  页面文字集合
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/getLayoutDate")
	public void getLayoutDate(@RequestParam HashMap<String,String> map,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		List<Map<String, String>> lm = (List<Map<String, String>>) JsonUtils.stringToJSON(map.get("data"), List.class);
		Map<String, String> mp=new HashMap<String, String>();
		if (lm!=null && lm.size()==1) {
			for (int i = 0; i < lm.size(); i++) {
				mp.put("dataType",lm.get(i).get("dataType"));				
				mp.put("langugeCode",lm.get(i).get("langugeCode"));
				mp.put("tableName",lm.get(i).get("tableName"));
			}
		}
		//获取数据
		List<Map<String, String>> ls=getService().SelectDateForLayout(mp);
		//ascii码转换为对应的语言文字
		ls=I18nPromptUtils.ascii2Native(ls,"layoutValue");
		
		Map<String, String> m =new HashMap<String, String>();
		if(ls!=null && ls.size()!=0){
			for (int i = 0; i < ls.size(); i++) {
				m.put(ls.get(i).get("layoutKey"), ls.get(i).get("layoutValue"));
			}			
		}
		
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",m), response);
		
	}	
	
	public I18nLayoutService getService(){
		return (I18nLayoutService)getBean("i18nLayoutBaseService");
	}
}
