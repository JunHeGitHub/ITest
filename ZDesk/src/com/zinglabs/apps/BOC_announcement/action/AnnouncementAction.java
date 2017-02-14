package com.zinglabs.apps.BOC_announcement.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sun.tools.javac.code.Attribute.Array;
import com.zinglabs.apps.BOC_announcement.AnnouncementService;
import com.zinglabs.apps.zshdSearch.QuestionService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;
@Controller
@RequestMapping(value="/*/announce")
public class AnnouncementAction extends BaseAction{
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/insert")
	public void insert(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		String array[];
		String ids=map.get("ids").toString();
		array=ids.split(",");
		for(int i=0;i<array.length;i++){
			map.put("uid",new RandomGUID().toString()) ;
			map.put("ids",array[i]);
			getService().insert(map);
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",map),response) ;
		
		
	}

	public AnnouncementService getService(){
		return (AnnouncementService)getBean("AnnouncementService");
	}	

}
