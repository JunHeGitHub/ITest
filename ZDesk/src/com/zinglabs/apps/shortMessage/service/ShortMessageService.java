package com.zinglabs.apps.shortMessage.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class ShortMessageService extends BaseService {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");

	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid) getBean("shortMessageService");
	}

	public void sendShortMessage(String smsNumber,String smsContent, String rootPath) throws Exception {
		
		
		String smsContentGBK = new String(smsContent.getBytes("gbk"));
		System.out.println(smsContentGBK);
		// localZSmsSend.initZSmsSend(this.sRootPath, this.logger);
		ShortMessageSender localZSmsSend = new ShortMessageSender(rootPath);
		localZSmsSend.sendSms(smsNumber, smsContentGBK);

		this.logger.info("ZSmsSvr: sendSms end, number=\"" + smsNumber
				+ "\", content=\"" + smsContent + "\".");

	}

	public void initCfg(String rootPath) {
		ShortMessageSender localZSmsSend = new ShortMessageSender(rootPath);
		localZSmsSend.loadConf();
	}

}
