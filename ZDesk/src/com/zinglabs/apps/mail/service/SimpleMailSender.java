package com.zinglabs.apps.mail.service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单邮件（不带附件的邮件）发送器
 */
public class SimpleMailSender {
	
	public static Logger logger = LoggerFactory.getLogger("ZDesk");

	public static void main(String[] args) {
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.zinglabs.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("zhenhui.zhu@zinglabs.com");
		mailInfo.setPassword("zing1234");// 您的邮箱密码
		mailInfo.setFromAddress("zhenhui.zhu@zinglabs.com");
		// mailInfo.setToAddress("zhenhui.zhu@zinglabs.com");
		mailInfo.setToAddress("1587904185@qq.com");
		mailInfo.setSubject("测试邮件");
		mailInfo.setContent("这是一个测试邮件");
		mailInfo.setEnconde("UTF-8");
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		try {
			sms.sendMail(mailInfo);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * @param mailInfo
	 *            待发送的邮件信息
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	public static void sendMail(MailSenderInfo mailInfo)
			throws UnsupportedEncodingException, MessagingException {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = null;
		if (mailInfo.isInit()) {
			sendMailSession = Session.getInstance(pro, authenticator);
		} else {
			sendMailSession = Session.getDefaultInstance(pro, authenticator);
		}

		// 根据session创建一个邮件消息
		Message mailMessage = new MimeMessage(sendMailSession);
		// 创建邮件发送者地址
		Address from = new InternetAddress(mailInfo.getFromAddress());
		// 设置邮件消息的发送者
		mailMessage.setFrom(from);
		// 创建邮件的接收者地址，并设置到邮件消息中
		Address to = new InternetAddress(mailInfo.getToAddress());
		// Message.RecipientType.TO属性表示接收者的类型为TO
		mailMessage.setRecipient(Message.RecipientType.TO, to);
		String encode = mailInfo.getEnconde();
		// 设置邮件消息的主题
		mailMessage.setSubject(MimeUtility.encodeText(mailInfo.getSubject(),
				encode, "B"));
		// 设置邮件消息发送的时间
		mailMessage.setSentDate(new Date());
		// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
		Multipart mainPart = new MimeMultipart();
		// 创建一个包含HTML内容的MimeBodyPart
		String content = "";
		
		if (mailInfo.getContent() != null) {
			content = new String(mailInfo.getContent().getBytes(encode),encode);
		}
		logger.debug("邮件转码后："+content);
		// 将MiniMultipart对象设置为邮件内容
		mailMessage.setContent(content, "text/html; charset=" + encode);
		// 发送邮件
		Transport.send(mailMessage);
	}
}
