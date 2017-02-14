package com.zinglabs.apps.shortMessage.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.shortMessage.service.ShortMessageService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping("/*/shortMessageAction")
public class ShortMessageAction extends BaseAction {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	@Resource
	public ShortMessageService shortMessageService;
	
	@RequestMapping("/sendShortMessage")
	public void sendShortMessage(@RequestParam Map map, HttpServletRequest request,
			HttpServletResponse response) {
		String smsNumber = (String) map.get("smsNumber");
		String smsContent = (String) map.get("smsContent");
		String rootPath = request.getSession().getServletContext().getRealPath("/");

		try {
			long startTime = System.currentTimeMillis();
			shortMessageService.sendShortMessage(smsNumber,smsContent,rootPath);
			long endTime = System.currentTimeMillis();
			logger.debug("短信发送时间："+(endTime-startTime)+"毫秒");
		} catch (Exception e) {
			logger.error("发送短信失败", e);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,""),response);
			return;
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,""),response);
	}
	
	@RequestMapping("/initCfg")
	public void initCfg(HttpServletRequest request,
			HttpServletResponse response){
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		shortMessageService.initCfg(rootPath);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,""),response);
	}

}
