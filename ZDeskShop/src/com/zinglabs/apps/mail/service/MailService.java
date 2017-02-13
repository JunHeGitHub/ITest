package com.zinglabs.apps.mail.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class MailService extends BaseService {
		
	private static Map cfgMap = null;
	
	private static boolean init = false;
	
	public static Logger logger = LoggerFactory.getLogger("ZDesk");

	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) getBean("mailSqlSession");
	}

	public boolean sengMail(String content, String title, String skillType,
			String cusMail, String test) {
		if(cfgMap == null){
			cfgMap = getConfigMsg(skillType);
		}
		SimpleMailSender simpleMailSender = new SimpleMailSender();
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost((String)cfgMap.get("hostSmtp"));
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(Boolean.parseBoolean(test));
		String name = (String)cfgMap.get("txNo");
		mailInfo.setUserName(name == null ? "" : name.trim() );
		String password = (String)cfgMap.get("txPass");
		mailInfo.setPassword(password == null ? "" : password.trim());// 您的邮箱密码
		mailInfo.setFromAddress((String)cfgMap.get("txNo"));
		mailInfo.setToAddress(cusMail);
		mailInfo.setSubject(title);
		mailInfo.setInit(init);
		mailInfo.setContent(content);
		logger.debug("邮件转码钱发送内容："+content);
		mailInfo.setEnconde("GBK");
		try {
			simpleMailSender.sendMail(mailInfo);
		} catch (UnsupportedEncodingException e) {
			logger.error("编码错误", e);
			return false;
		}catch (MessagingException e) {
			logger.error("邮件发送异常", e);
			return false;
		}
		init = false;
		return true;
	}
	
	public void initMailCfg(String skillType){
		cfgMap = getConfigMsg(skillType);
	}

	public Map getConfigMsg(String skillType) {
		Map map = new HashMap();
		map.put("skillType", skillType);
		init = true;
		return getServiceSqlSession().db_selectOne("selectConfigMsg", map);
	}

}
