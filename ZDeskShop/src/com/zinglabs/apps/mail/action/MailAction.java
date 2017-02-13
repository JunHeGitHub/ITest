package com.zinglabs.apps.mail.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.mail.service.MailService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping("/*/mailAction")
public class MailAction extends BaseAction {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	@Resource
	public MailService mailService;

	@RequestMapping("/sendMail")
	public void sendMail(@RequestParam Map map, HttpServletRequest request,
			HttpServletResponse response) {
		
		String content = (String) map.get("content");
		String title = (String) map.get("titlecc");
		String skillType = (String) map.get("skillType");
		String cusMail = (String) map.get("cusMail");
		String test = (String) map.get("test");
		boolean flag = mailService.sengMail(content, title, skillType, cusMail, test);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(flag,""),response);
		
	}
	@RequestMapping("/initMailCfg")
	public void initMailCfg(@RequestParam Map map, HttpServletRequest request,
			HttpServletResponse response){
		mailService.initMailCfg((String)map.get("skillType"));
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,""),response);
	}

}
