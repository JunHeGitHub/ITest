package com.zinglabs.apps.shortMessage.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umpay.bank.SMSClient;
import com.umpay.bank.test.TestRecDemoHandler;
import com.umpay.bank.test.TestSendDemoHandler;

public class ShortMessageSender {

	private static Logger logger = LoggerFactory.getLogger("ZDesk");
	private static Properties localProperties = null;
	private ShortMessageInfo shortMessageInfo;
	private String sRootPath;

	public ShortMessageSender(String rootPath) {
		if (localProperties == null) {
			sRootPath = rootPath;
			loadConf();
		}
		shortMessageInfo = new ShortMessageInfo(localProperties);
	}

	public void loadConf() {
		FileInputStream localFileInputStream = null;
		localProperties = new Properties();
		try {
			String str1 = sRootPath  + "WEB-INF"
					+ File.separator + "config" + File.separator
					+ "config.properties";
			System.out.println(str1);
			localFileInputStream = new FileInputStream(str1);
		} catch (Exception localException) {
			System.out.println("can't open conf");
			localFileInputStream = null;
		}

		if ((localProperties != null) && (localFileInputStream != null))
			try {
				localProperties.load(localFileInputStream);
			} catch (IOException localIOException) {
				localProperties = null;
			}
		else {
			localProperties = null;
		}

	}

	public void sendSms(String paramString1, String paramString2)
			throws Exception {
		logger.info("sendSms: enter.");

		SMSClient client = SMSClient.getInstance(
				shortMessageInfo.getSmsServIP(),
				shortMessageInfo.getSmsServPort(),
				shortMessageInfo.getSmsServUser(),
				shortMessageInfo.getSmsServPwd(),
				shortMessageInfo.getSmsServProtocol(), 1,
				new TestRecDemoHandler());

		client.setServiceId("BANK");
		client.setMsgSrc(shortMessageInfo.getSmsServUser());
		client.setBasicSrcId(shortMessageInfo.getSmsServUser());
		client.setExtendedSrcId(shortMessageInfo.getSmsServUser());

		client.setContentLength(paramString2.length());

		client.setSplitMode("Split");
		client.setSendBankHandler(new TestSendDemoHandler());
		logger.info("sendSms: before send");

		boolean bool = client.send(paramString1, paramString2);

		if (client.isFirstRun) {
			// Thread.sleep(8000L);
			logger.info("sendSms: first run SMSClient");
		} else {
			logger.info("sendSms: not first run SMSClient");
		}

		logger.info("sendSms: after send ret=" + bool);

		logger.info("sendSms: leave sendSms, ip=\""
				+ shortMessageInfo.getSmsServIP() + "\",port="
				+ shortMessageInfo.getSmsServPort() + ",user=\""
				+ shortMessageInfo.getSmsServUser() + "\",pwd=\""
				+ shortMessageInfo.getSmsServPwd() + "\"");

	}

}