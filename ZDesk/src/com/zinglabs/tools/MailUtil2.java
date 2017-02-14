package com.zinglabs.tools;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


import java.io.*;
import java.net.*;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;


import javax.mail.Transport;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

import com.zinglabs.tools.TXInfo2;
import com.zinglabs.tools.TXMsg2;
import com.zinglabs.log.LogUtil;
//import com.zinglabs.task.TXTask;

//import com.sun.mail.util.LineInputStream;

/**
 * 使用POP3协议接收邮件
 */
public class MailUtil2 {
////	pop3.163.com
//	public String hostPop3;
////	smtp.163.com
//	public String hostSmtp;
////	110
//	public String pop3Port;
//	
////	25
////	public String smtpPort;
//////	zdeskmail@163.com
////	public String uName;
//////	zinglabs
////	public String pword;
////	默认分配的技能组
	public static void main(String[] args) throws Exception {
		TXInfo2 tx=new TXInfo2(0);
//		tx.txNo="MailConsult@163.com";
//		tx.password="zinglabs";
//		tx.hostPop3="pop3.163.com";
//		tx.hostSmtp="smtp.163.com";
//		tx.defaultSK="wsyh";
//		tx.pop3Port="110";
		
		

//		tx.txNo="abc@mail.notes.bank-of-china.com";
		tx.txNo="def@mail.notes.bank-of-china.com";
		tx.password="def";
		tx.hostPop3="22.188.58.14";
		tx.hostSmtp="22.188.58.14";
		tx.defaultSK="wsyh";
		tx.pop3Port="110";
//		sendMail2("测试测试","邮件测试","def@mail.notes.bank-of-china.com",tx,"false","GBK","true");
//		sendMail2("测试测试","邮件测试","zaixiankefur@boc.com",tx,"false","GBK","true");
		sendMail2("测试测试","邮件测试","chuan.li@zinglabs.com",tx,"false","GBK","false");
//		22.188.58.14
//		
//		tx.txNo="MailConsult@163.com";
//		tx.password="zinglabs";
//		tx.hostPop3="pop3.163.com";
//		tx.hostSmtp="smtp.163.com";
//		tx.defaultSK="wsyh";
//		tx.pop3Port="110";
//		sendMail2("金老师您好这几天麻烦你了，不好意思","邮件测试","chuan.li@zinglabs.com",tx,"false","UTF-8","true");
//		sendMail2("金老师您好这几天麻烦你了，不好意思","邮件测试","chuan.li@zinglabs.com",tx,"false","GBK","true");
//		sendMail2("金老师您好这几天麻烦你了，不好意思","邮件测试","chuan.li@zinglabs.com",tx,"false","GB2312","true");
////		
//		
//		
//		tx.txNo="chuan.li@zinglabs.com";
//		tx.password="20000919";
//		tx.hostPop3="mail.zinglabs.com";
//		tx.hostSmtp="mail.zinglabs.com";
//		tx.defaultSK="wsyh";
//		tx.pop3Port="110";
//		sendMail2("金老师您好这几天麻烦你了，不好意思","邮件测试","chuan.li@zinglabs.com",tx,"false","UTF-8","true");
//		sendMail2("金老师您好这几天麻烦你了，不好意思","邮件测试","chuan.li@zinglabs.com",tx,"false","GBK","true");
//		sendMail2("金老师您好这几天麻烦你了，不好意思","邮件测试","chuan.li@zinglabs.com",tx,"false","GB2312","true");
//		sendMail2("金老师您好这几天麻烦你了，不好意思","邮件测试","chuan.li@zinglabs.com",tx,"false","UTF-8","true");
		
//		sendMail("测试邮件","测试邮件之自己发自己","MailConsult@163.com",tx);
//		receive(tx);
//		receive();
		
//		TXInfo2 tx=new TXInfo2(0);
//		tx.txNo="95566_boc@bank-of-china.com";
//		tx.password="";
////		tx.password="password";
//		tx.hostPop3="21.120.4.145";
//		tx.hostSmtp="21.120.4.145";
//		tx.defaultSK="wsyh";
//		tx.pop3Port="110";
//		sendMail("测试邮件","测试邮件之自己发自己","15210105667@139.com",tx);
		
		
//		TXInfo2 tx=new TXInfo2(0);
//		tx.txNo="95566_boc@bank-of-china.com";
//		tx.password="";
////		tx.password="password";
//		tx.hostPop3="21.120.4.145";
//		tx.hostSmtp="21.120.4.145";
////		tx.hostSmtp="mail.zinglabs.com";
//		tx.defaultSK="wsyh";
//		tx.pop3Port="110";
		
		
		
//		MailUtil2.estmpMain("测试邮件测试邮件UTF-8","试试","chuan.li@zinglabs.com",tx,"false","UTF-8");
//		MailUtil2.estmpMain("测试邮件测试邮件GBK","试试","chuan.li@zinglabs.com",tx,"false","GBK");
//		MailUtil2.estmpMain("测试邮件测试邮件GB2312","试试","chuan.li@zinglabs.com",tx,"false","GB2312");
//		
	}

	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
	public static void init(){
		
	}
	
	public static void sendMail(String content,String subject,String to,TXInfo2 tx){
		try {
// resp.setContentType(CONTENT_TYPE);
//			String host = hostSmtp;
//			String to = uName;
//			String from = req.getParameter("from");
//			String subject = "人寿财在线客服邮件__"+System.currentTimeMillis();
//			String messageContent = "人寿财在线客服邮件__"+System.currentTimeMillis();
//			estmpMain(content,subject,to,tx);
			
			LogUtil.debug("hostName:"+tx.hostSmtp+" from:"+tx.txNo+" subject:"+subject+" to:"+to+" content:"+content, SKIP_Logger);
			HtmlEmail email = new HtmlEmail(); 
			email.setHostName(tx.hostSmtp);
			email.setSmtpPort(25);
//			email.setAuthenticator(new DefaultAuthenticator(tx.txNo,tx.password));
//			email.set
			email.setDebug(true);
			email.setSSL(false);
			email.setTLS(false);
			email.setCharset("GBK");
//			email.setSSLOnConnect(false);
			email.setFrom(tx.txNo);
			email.setSubject(subject);
			email.setHtmlMsg(content);  // set the alternative message  
//	    	email.setTextMsg("抱歉！您的邮件客户端不支持html格式");  // send the email
			email.addTo(to);
			email.send();
			
			
//			HtmlEmail email2 = new HtmlEmail(); 
//			email2.setHostName(tx.hostSmtp);
//			email2.setSmtpPort(25);
////			email.setAuthenticator(new DefaultAuthenticator(tx.txNo,tx.password));
////			email.set
//			email2.setDebug(true);
//			email2.setSSL(false);
//			email2.setTLS(false);
//			email2.setCharset("GBK");
////			email.setSSLOnConnect(false);
//			email2.setFrom(tx.txNo);
//			email2.setSubject(subject);
//			email2.setHtmlMsg(content);  // set the alternative message  
////	    	email.setTextMsg("抱歉！您的邮件客户端不支持html格式");  // send the email
//			email2.addTo(to);
//			email2.send();
			
//			resp.setContentType(CONTENT_TYPE);
//			// PrintWriter out = resp.getWriter();
//			//
//			// out.println(" <html> ");
//			// out.println(" <head> <title> MailServlet </title> </head> ");
//
//			Authenticator authenticator = new Authenticator() {
//				protected PasswordAuthentication getPasswordAuthentication() {
//					return new PasswordAuthentication("abc@boc.com", "abc");
//				}
//			};   
//			Properties prop = new Properties();
//			prop.setProperty("mail.smtp.host", "22.127.27.199");   
//			//发送邮件的协议   
//			prop.setProperty("mail.transport.protocol", "smtp");   
//			//在连接服务器的时候是否需要验证，发邮件是需要验证的   
//			prop.setProperty("mail.smtp.auth", "true");   
//			
//			Session mySession = Session.getInstance(prop, authenticator);
//			Message msg = new MimeMessage(mySession);
//			msg.setFrom(new InternetAddress(from));
//			InternetAddress[] address = { new InternetAddress(to) };
//			msg.setRecipients(Message.RecipientType.TO, address);
//			msg.setSubject(subject);
//			msg.setSentDate(new Date(System.currentTimeMillis()));
//			msg.setText(messageContent);
//			Transport.send(msg);

			// out.println(" <body> ");
			// out.println("Mail was send to " + to);
			// out.println("from " + from);
			// out.println("using host " + host + ". ");
			// out.println(" </body> </html> ");

		} catch (Exception e) {
			LogUtil.error(to+"sendMail ACTION_SEND_MAIL" +content, SKIP_Logger);
			LogUtil.error(e, SKIP_Logger);
		}
	}
	
	/**
	 * 接收邮件
	 */
	public static TXMsg2 receive(TXInfo2 tx) throws Exception {
		// 准备连接服务器的会话信息
		TXMsg2 ret=null;
		Properties props = new Properties();
		
		props.put("mail.smtp.auth","true");
		
		props.setProperty("mail.store.protocol", "pop3");		// 协议
		props.setProperty("mail.pop3.port", tx.pop3Port);				// 端口
		props.setProperty("mail.pop3.host", tx.hostPop3);	// pop3服务器

		// 创建Session实例对象
//		Authenticator auth=new PopupAuthenticator(tx.txNo, tx.password);
//
////		PasswordAuthentication pop = popAuthenticator.performCheck(tx.txNo, tx.password); 
////		Session session=Session.getInstance(props,popAuthenticator);
//		
//		
////		Authenticator auth = new SMTPAuthenticator();
//		Session session = Session.getInstance(props, auth);
//		mailsession加的popAuthenticator

		
		
		Session session = Session.getInstance(props);
		Store store = session.getStore("pop3");
		store.connect(tx.txNo, tx.password);

		// 获得收件箱
		Folder folder = store.getFolder("INBOX");
		/* Folder.READ_ONLY：只读权限
		 * Folder.READ_WRITE：可读可写（可以修改邮件的状态）
		 */
		folder.open(Folder.READ_WRITE);	//打开收件箱

		// 由于POP3协议无法获知邮件的状态,所以getUnreadMessageCount得到的是收件箱的邮件总数
//		System.out.println("未读邮件数: " + folder.getUnreadMessageCount());
//
//		// 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
//		System.out.println("删除邮件数: " + folder.getDeletedMessageCount());
//		System.out.println("新邮件: " + folder.getNewMessageCount());
//
//		// 获得收件箱中的邮件总数
//		System.out.println("邮件总数: " + folder.getMessageCount());

		
		// 得到收件箱中的所有邮件,并解析
		Message[] messages = folder.getMessages();
//		folder.getMessage(0);
//		parseMessage(messages);
		
		// 得到并删除收件箱中的第一封邮件,并解析
		System.out.println("receive "+messages+" "+messages.length+" "+folder.getMessageCount()+" "+folder.getUnreadMessageCount()+" "+folder.getNewMessageCount());
		if (messages == null || messages.length < 1 || folder.getMessageCount()-folder.getDeletedMessageCount()<1){
			LogUtil.debug("no mail in", SKIP_Logger);
			return null;
		}
		Message msgRev=(folder.getMessage(1));
		

		if(msgRev!=null){
			MimeMessage msg = (MimeMessage)(msgRev);
			StringBuffer content = new StringBuffer(30);
			getMailTextContent(msg, content);
			ret=new TXMsg2(content.toString());
			ret.subject=getSubject(msg);
			ret.emailAddress=getFrom(msg);
			msgRev.setFlag(Flags.Flag.DELETED, true);
		}
		
		//释放资源
		folder.close(true);
		store.close();
		
		return ret;
	}

	
	
	public static void parseAndDelMessage(MimeMessage msg) throws Exception{
//		System.out.println("------------------解析第" + msg.getMessageNumber() + "封邮件-------------------- ");
//		System.out.println("主题: " + getSubject(msg));
//		System.out.println("发件人: " + getFrom(msg));
//		System.out.println("收件人：" + getReceiveAddress(msg, null));
//		System.out.println("发送时间：" + getSentDate(msg, null));
//		System.out.println("是否已读：" + isSeen(msg));
//		System.out.println("邮件优先级：" + getPriority(msg));
//		System.out.println("是否需要回执：" + isReplySign(msg));
//		System.out.println("邮件大小：" + msg.getSize() * 1024 + "kb");
//		boolean isContainerAttachment = isContainAttachment(msg);
//		System.out.println("是否包含附件：" + isContainerAttachment);
//		if (isContainerAttachment) {
//			saveAttachment(msg, "c:\\mailtmp\\"+msg.getSubject() + "_"); //保存附件
//		}
		StringBuffer content = new StringBuffer(30);
		getMailTextContent(msg, content);
		System.out.println("邮件正文：" + (content.length() > 100 ? content.substring(0,100) + "..." : content));
		System.out.println("------------------第" + msg.getMessageNumber() + "封邮件解析结束-------------------- ");
		System.out.println();
	}
	/**
	 * 解析邮件
	 * @param messages 要解析的邮件列表
	 */
	public static void parseMessage(Message ...messages) throws MessagingException, IOException {
		if (messages == null || messages.length < 1)
			throw new MessagingException("未找到要解析的邮件!");

		// 解析所有邮件
		for (int i = 0, count = messages.length; i < count; i++) {
			MimeMessage msg = (MimeMessage) messages[i];
			System.out.println("------------------解析第" + msg.getMessageNumber() + "封邮件-------------------- ");
			System.out.println("主题: " + getSubject(msg));
			System.out.println("发件人: " + getFrom(msg));
			System.out.println("收件人：" + getReceiveAddress(msg, null));
			System.out.println("发送时间：" + getSentDate(msg, null));
			System.out.println("是否已读：" + isSeen(msg));
			System.out.println("邮件优先级：" + getPriority(msg));
			System.out.println("是否需要回执：" + isReplySign(msg));
			System.out.println("邮件大小：" + msg.getSize() * 1024 + "kb");
			boolean isContainerAttachment = isContainAttachment(msg);
			System.out.println("是否包含附件：" + isContainerAttachment);
//			if (isContainerAttachment) {
//				saveAttachment(msg, "c:\\mailtmp\\"+msg.getSubject() + "_"); //保存附件
//			}
			StringBuffer content = new StringBuffer(30);
			getMailTextContent(msg, content);
			System.out.println("邮件正文：" + (content.length() > 100 ? content.substring(0,100) + "..." : content));
			System.out.println("------------------第" + msg.getMessageNumber() + "封邮件解析结束-------------------- ");
			System.out.println();
		}
	}

	/**
	 * 获得邮件主题
	 * @param msg 邮件内容
	 * @return 解码后的邮件主题
	 */
	public static String getSubject(MimeMessage msg) throws UnsupportedEncodingException, MessagingException {
		return MimeUtility.decodeText(msg.getSubject());
	}

	/**
	 * 获得邮件发件人
	 * @param msg 邮件内容
	 * @return 姓名 <Email地址>
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	public static String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
		String from = "";
		Address[] froms = msg.getFrom();
		if (froms.length < 1)
			throw new MessagingException("没有发件人!");

		InternetAddress address = (InternetAddress) froms[0];
		String person = address.getPersonal();
		if (person != null) {
			person = MimeUtility.decodeText(person) + " ";
		} else {
			person = "";
		}
		from = person + "<" + address.getAddress() + ">";

		return from;
	}

	/**
	 * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
	 * <p>Message.RecipientType.TO  收件人</p>
	 * <p>Message.RecipientType.CC  抄送</p>
	 * <p>Message.RecipientType.BCC 密送</p>
	 * @param msg 邮件内容
	 * @param type 收件人类型
	 * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
	 * @throws MessagingException
	 */
	public static String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {
		StringBuffer receiveAddress = new StringBuffer();
		Address[] addresss = null;
		if (type == null) {
			addresss = msg.getAllRecipients();
		} else {
			addresss = msg.getRecipients(type);
		}

		if (addresss == null || addresss.length < 1)
			throw new MessagingException("没有收件人!");
		for (Address address : addresss) {
			InternetAddress internetAddress = (InternetAddress)address;
			receiveAddress.append(internetAddress.toUnicodeString()).append(",");
		}

		receiveAddress.deleteCharAt(receiveAddress.length()-1);	//删除最后一个逗号

		return receiveAddress.toString();
	}

	/**
	 * 获得邮件发送时间
	 * @param msg 邮件内容
	 * @return yyyy年mm月dd日 星期X HH:mm
	 * @throws MessagingException
	 */
	public static String getSentDate(MimeMessage msg, String pattern) throws MessagingException {
		Date receivedDate = msg.getSentDate();
		if (receivedDate == null)
			return "";

		if (pattern == null || "".equals(pattern))
			pattern = "yyyy年MM月dd日 E HH:mm ";

		return new SimpleDateFormat(pattern).format(receivedDate);
	}

	/**
	 * 判断邮件中是否包含附件
	 * @param msg 邮件内容
	 * @return 邮件中存在附件返回true，不存在返回false
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static boolean isContainAttachment(Part part) throws MessagingException, IOException {
		boolean flag = false;
		if (part.isMimeType("multipart/*")) {
			MimeMultipart multipart = (MimeMultipart) part.getContent();
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				String disp = bodyPart.getDisposition();
				if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
					flag = true;
				} else if (bodyPart.isMimeType("multipart/*")) {
					flag = isContainAttachment(bodyPart);
				} else {
					String contentType = bodyPart.getContentType();
					if (contentType.indexOf("application") != -1) {
						flag = true;
					}  

					if (contentType.indexOf("name") != -1) {
						flag = true;
					}
				}

				if (flag) break;
			}
		} else if (part.isMimeType("message/rfc822")) {
			flag = isContainAttachment((Part)part.getContent());
		}
		return flag;
	}

	/**
	 * 判断邮件是否已读
	 * @param msg 邮件内容
	 * @return 如果邮件已读返回true,否则返回false
	 * @throws MessagingException
	 */
	public static boolean isSeen(MimeMessage msg) throws MessagingException {
		return msg.getFlags().contains(Flags.Flag.SEEN);
	}

	/**
	 * 判断邮件是否需要阅读回执
	 * @param msg 邮件内容
	 * @return 需要回执返回true,否则返回false
	 * @throws MessagingException
	 */
	public static boolean isReplySign(MimeMessage msg) throws MessagingException {
		boolean replySign = false;
		String[] headers = msg.getHeader("Disposition-Notification-To");
		if (headers != null)
			replySign = true;
		return replySign;
	}

	/**
	 * 获得邮件的优先级
	 * @param msg 邮件内容
	 * @return 1(High):紧急  3:普通(Normal)  5:低(Low)
	 * @throws MessagingException
	 */
	public static String getPriority(MimeMessage msg) throws MessagingException {
		String priority = "普通";
		String[] headers = msg.getHeader("X-Priority");
		if (headers != null) {
			String headerPriority = headers[0];
			if (headerPriority.indexOf("1") != -1 || headerPriority.indexOf("High") != -1)
				priority = "紧急";
			else if (headerPriority.indexOf("5") != -1 || headerPriority.indexOf("Low") != -1)
				priority = "低";
			else
				priority = "普通";
		}
		return priority;
	} 

	/**
	 * 获得邮件文本内容
	 * @param part 邮件体
	 * @param content 存储邮件文本内容的字符串
	 * @throws MessagingException
	 * @throws IOException
	 */
	public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
		//如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
		boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
		if (part.isMimeType("text/*") && !isContainTextAttach) {
			content.append(part.getContent().toString());
		} else if (part.isMimeType("message/rfc822")) {
			getMailTextContent((Part)part.getContent(),content);
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				getMailTextContent(bodyPart,content);
			}
		}
	}

	/**
	 * 保存附件
	 * @param part 邮件中多个组合体中的其中一个组合体
	 * @param destDir  附件保存目录
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void saveAttachment(Part part, String destDir) throws UnsupportedEncodingException, MessagingException,
			FileNotFoundException, IOException {
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();	//复杂体邮件
			//复杂体邮件包含多个邮件体
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				//获得复杂体邮件中其中一个邮件体
				BodyPart bodyPart = multipart.getBodyPart(i);
				//某一个邮件体也有可能是由多个邮件体组成的复杂体
				String disp = bodyPart.getDisposition();
				if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
					InputStream is = bodyPart.getInputStream();
					saveFile(is, destDir, decodeText(bodyPart.getFileName()));
				} else if (bodyPart.isMimeType("multipart/*")) {
					saveAttachment(bodyPart,destDir);
				} else {
					String contentType = bodyPart.getContentType();
					if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
						saveFile(bodyPart.getInputStream(), destDir, decodeText(bodyPart.getFileName()));
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachment((Part) part.getContent(),destDir);
		}
	}

	/**
	 * 读取输入流中的数据保存至指定目录
	 * @param is 输入流
	 * @param fileName 文件名
	 * @param destDir 文件存储目录
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void saveFile(InputStream is, String destDir, String fileName)
			throws FileNotFoundException, IOException {
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(new File(destDir + fileName)));
		int len = -1;
		while ((len = bis.read()) != -1) {
			bos.write(len);
			bos.flush();
		}
		bos.close();
		bis.close();
	}

	/**
	 * 文本解码
	 * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
	 * @return 解码后的文本
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeText(String encodeText) throws UnsupportedEncodingException {
		if (encodeText == null || "".equals(encodeText)) {
			return "";
		} else {
			return MimeUtility.decodeText(encodeText);
		}
	}
	
//	
//	   public static void sendMail2(String smtpHost, String from, String to,
//	            String subject, String messageText) throws MessagingException,
//	            java.io.UnsupportedEncodingException {

		   public static void sendMail2(String content,String subject,String to,TXInfo2 tx,
				   String test,String encodeing,String needAuth) throws MessagingException,
		            java.io.UnsupportedEncodingException {
		   
//		       sendMail3();
		       
		       
	        // Step 1: Configure the mail session
	        LogUtil.debug("Configuring mail session for: " + tx.hostSmtp,SKIP_Logger);
	        
	        String contentGB2 = new String(content.getBytes(encodeing));
	        
	        java.util.Properties props = new java.util.Properties();
	        
	        if(tx.password!=null && tx.password.length()>0) {
	            props.put("mail.smtp.auth", "true");// 指定是否需要SMTP验证  user stmp for outgoing mail this request kerio
	            LogUtil.debug("need auth", SKIP_Logger);
	        }else {
	            props.put("mail.smtp.auth", "false");
	        }
	        
	        if(needAuth.equals("true")) {
	            props.put("mail.smtp.starttls.enable", "true");
	        }
	        
//	        props.put("mail.smtp.starttls.enable", "false");

	        props.put("mail.smtp.host", tx.hostSmtp);// 指定SMTP服务器
	        props.put("mail.transport.protocol", "smtp");
	        
//	        MyAuthenticator myauth = new MyAuthenticator(tx.txNo, tx.password);
//	        Session mailSession = Session.getDefaultInstance(props, myauth);
	        
	        
	        Session mailSession = Session.getDefaultInstance(props);
	        mailSession.setDebug(true);// 是否在控制台显示debug信息

	        // Step 2: Construct the message
	        LogUtil.debug("Constructing message -  from=" + tx.txNo + "  to="+ to,SKIP_Logger);
	        
	        InternetAddress fromAddress = new InternetAddress(tx.txNo);
	        InternetAddress toAddress = new InternetAddress(to);

	        MimeMessage testMessage = new MimeMessage(mailSession);
	        testMessage.setFrom(fromAddress);
	        testMessage.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
	        testMessage.setSentDate(new java.util.Date());
	        testMessage.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
//	        testMessage.setSubject(MimeUtility.encodeText(subject, encodeing, "B"));
	        
	        testMessage.setContent(contentGB2, "text/html;charset="+encodeing);
	        LogUtil.debug(testMessage.getAllRecipients()+" Message constructed"+content,SKIP_Logger);

	        // Step 3: Now send the message
	        
	        Transport transport = mailSession.getTransport("smtp");
	        
	        if(tx.password!=null && tx.password.length()>0) {
	            transport.connect(tx.hostSmtp, tx.txNo, tx.password);
	            LogUtil.debug("password > 0", SKIP_Logger);
	        }else if(!needAuth.equals("false")){
	            transport.connect(tx.hostSmtp, tx.txNo);  
	        }else if(needAuth.equals("false")){
	            transport.connect();
	        }
	        
//	        if(test.equals("true") &&(tx.password==null || tx.password.length()==0)){
//	        	transport.connect(tx.hostSmtp, tx.txNo);
//	        }else if(test.equals("true")){
//	        	transport.connect(tx.hostSmtp, tx.txNo, tx.password);
//	        }else if(test.equals("false")){
//	        	transport.connect();
//	        }
	        LogUtil.debug("transport.isConnected "+transport.isConnected(),SKIP_Logger);
//	        transport.se
//	        transport.send(testMessage)
	        transport.sendMessage(testMessage, testMessage.getAllRecipients());
	        transport.close();

	        LogUtil.debug("Message sent! "+transport.isConnected(),SKIP_Logger);
	        
	        
	    }

	
		   
		   
		public static void sendMail3() {
		    try
            {
		        LogUtil.debug("sendMail3 ",SKIP_Logger);
		        java.util.Properties props = new java.util.Properties();
	            props.put("mail.smtp.auth", "true");
	                
	                    props.put("mail.smtp.host","22.1.116.86");
	            props.put("mail.transport.protocol", "smtp");
	            
	            Session mailSession = Session.getDefaultInstance(props);
	            mailSession.setDebug(true);

	            // Step 2: Construct the message
	            
	            InternetAddress fromAddress = new InternetAddress("95566_boc@bank-of-china.com");
	            InternetAddress toAddress = new InternetAddress("qp_xxxx_db@163.com");

	            MimeMessage testMessage = new MimeMessage(mailSession);
	            testMessage.setFrom(fromAddress);
	            testMessage.addRecipient(javax.mail.Message.RecipientType.TO, toAddress);
	            testMessage.setSentDate(new java.util.Date());
	            testMessage.setSubject(MimeUtility.encodeText("1111111", "utf-8", "B"));
//	          testMessage.setSubject(MimeUtility.encodeText("测试", encodeing, "B"));
	            
	            testMessage.setContent("oooooooooooooo", "text/html;charset=GBK");

	            // Step 3: Now send the message
	            
	            Transport transport = mailSession.getTransport("smtp");
	            transport.connect("22.1.116.86", "95566_boc@bank-of-china.com","password");
	                    transport.sendMessage(testMessage, testMessage.getAllRecipients());
	            transport.close();
            }
            catch (Exception e)
            {
                LogUtil.error(e, SKIP_Logger);
            }
		}
		   


	public static void estmpMain(String content,String subject,String to,TXInfo2 tx,String test,String encodeing) {
	  Socket smtpclient=null;
	  DataOutputStream os=null;
	  BufferedReader is=null;
	    String answer=null;
	    try{        
	    	
	    	LogUtil.debug("estmpMain begin", SKIP_Logger);
	         smtpclient=new Socket(tx.hostPop3,25);
	         is=new BufferedReader(new InputStreamReader(smtpclient.getInputStream()));
	         os=new DataOutputStream(smtpclient.getOutputStream());         
	        }
	        catch(UnknownHostException ue){
	         LogUtil.error("未知主机", SKIP_Logger);
	         LogUtil.error(ue, SKIP_Logger);
	        }
	        catch(IOException io){
	        	LogUtil.error("I/O错误", SKIP_Logger);
	        	LogUtil.error(io, SKIP_Logger);
	        }
	        try{
	        	LogUtil.debug("正在登录邮箱服务器....", SKIP_Logger);
	        	os.writeBytes("EHLO localhost\r\n");  
	            while ((answer=is.readLine())!=null){
	            	LogUtil.debug("Server1:"+answer, SKIP_Logger);
	             if (answer.indexOf("220")!=-1){
	              break;
	             }
	            }   
	            LogUtil.debug("正在检测邮箱支持的服务....", SKIP_Logger);
//	            os.writeBytes("AUTH LOGIN\r\n");  
//	            while ((answer=is.readLine())!=null){
//	             if (answer.indexOf("250")==-1){
//	              break;
//	             }
//	             LogUtil.debug("Server:"+answer, SKIP_Logger);
//	            }
//	         LogUtil.debug("Server:"+answer, SKIP_Logger);
//	         if(tx.password!=null && tx.password.length()>0){
//	        	 os.writeBytes(tx.txNo+"\r\n");//用户名的BASE64值
//		         os.writeBytes(tx.password+"\r\n");//密码的BASE64值，这里用*代替
//	         }
	         
//	            while ((answer=is.readLine())!=null){
//	            LogUtil.debug("Server:"+answer, SKIP_Logger);
//	             if (answer.indexOf("235")!=-1){
//	            	 LogUtil.debug("验证成功", SKIP_Logger);
//	                 break;
//	             }
//	             else if (answer.indexOf("334")==-1) {
//	            	 LogUtil.debug("验证失败", SKIP_Logger);
//	                    os.close();
//	                    is.close();
//	                    smtpclient.close();  
////	                    System.exit(0);
//	             }
//	            }         
	         LogUtil.debug("开始发送邮件....", SKIP_Logger);
	         os.writeBytes("MAIL From: < "+tx.txNo+" >\r\n");   
	         os.writeBytes("RCPT To: < "+to+" >\r\n");
	         os.writeBytes("DATA\r\n");
	            while ((answer=is.readLine())!=null){
	             if (answer.indexOf("354")!=-1){
	              break;
	             }             
	             LogUtil.debug("Server2:"+answer, SKIP_Logger);
	            }
	            LogUtil.debug("Server3:"+answer, SKIP_Logger);
	            LogUtil.debug("正在发送邮件内容....", SKIP_Logger);
	            os.writeBytes("From: "+tx.txNo+"\r\n");
	            os.writeBytes("To: "+to+"\r\n");
	            os.writeBytes("Subject: "+subject+"\r\n");
	            os.writeBytes("Content-Type: text/html;charset="+encodeing+"\r\n");       
	            
//	            
//				String comboParam2 = new String(content.getBytes("gbk"));
//				String comboParam3 = new String(content.getBytes("UTF-8"));
//				String comboParam4 = new String(content.getBytes("gbk"),"UTF-8");
//				String comboParam5 = new String(content.getBytes("UTF-8"),"gbk");
//				String comboParam6=new String(content.getBytes("ISO-8859-1"),"UTF-8");
				
				
				String contentGBx=new String(content.getBytes("ISO-8859-1"),"gbk");
				String contentGBx1=new String(content.getBytes("ISO-8859-1"),"utf-8");
				String contentGB2 = new String(content.getBytes("gbk"));
				String contentGB3 = new String(content.getBytes("UTF-8"));
				String contentGB4 = new String(content.getBytes("gbk"),"UTF-8");
				String contentGB5 = new String(content.getBytes("UTF-8"),"gbk");
				String contentGB6 = new String(content.getBytes("ISO-8859-1"));		
				
				LogUtil.debug("contentGBx:"+contentGBx+"contentGBx1:"+contentGBx1+
				" contentGB2:"+contentGB2+" contentGB3:"+contentGB3+" contentGB4:"+contentGB4+
				"contentGB5:"+contentGB5+" contentGB6:"+contentGB6, SKIP_Logger);
	            
//				BASE64Encoder base64=new BASE64Encoder();
//                base64.encode(args[0].getBytes("utf-8"), System.out);
				
				if(test!=null && test.equals("true")){
					String tmp1="CCC2"+contentGBx+"CCC3"+contentGBx1+"CCC4"+contentGB2+
					"CCC5"+contentGB3+"CCC6"+contentGB4+"CCC7"+contentGB5+"CCC8"+contentGB6;
//					os.writeBytes(base64.encode(tmp1.getBytes())+"\r\n\r\n");
					os.writeBytes(tmp1.getBytes()+"\r\n\r\n");
				}else{
					if(encodeing.equals("\"GBK\"")){
						encodeing="GBK";
					}else if(encodeing.equals("\"GBK2312\"")){
						encodeing="GBK2312";
					}else if(encodeing.equals("\"UTF-8\"")){
						encodeing="UTF-8";
					}
					String contentTmp = new String(content.getBytes(encodeing));
					os.writeBytes(contentTmp.getBytes()+"\r\n\r\n");
				}
//	            os.writeBytes(content+"\r\n\r\n");
	            os.writeBytes("\r\n.\r\n");
	            while ((answer=is.readLine())!=null){
	            	LogUtil.debug("Server4:"+answer, SKIP_Logger);
//	             System.out.println("Server:"+answer);
	             if (answer.indexOf("250")!=-1){
	              break;
	             }             
	            } 
	            os.writeBytes("QUIT\r\n");
	            while ((answer=is.readLine())!=null){
	            	LogUtil.debug("Server5:"+answer, SKIP_Logger);
//	             System.out.println("Server:"+answer);
	             if (answer.indexOf("221")!=-1){
	            	 LogUtil.debug("邮件发送成功,退出邮箱！", SKIP_Logger);
//	                 System.out.println("邮件发送成功,退出邮箱！");               
	              break;
	             }             
	            }
	            os.close();
	            is.close();
	            smtpclient.close();           
	        }
	        catch(UnknownHostException ue){
	        	 LogUtil.error(ue, SKIP_Logger);
	        	 LogUtil.error("无法连接主机", SKIP_Logger);
	        }
	        catch(IOException io){
	        	 LogUtil.error(io, SKIP_Logger);
	        	 LogUtil.error("发送I/O错误", SKIP_Logger);
	        }
	        LogUtil.debug("estmpMain end", SKIP_Logger);
	 }
	
	
	
	public static byte[] base64encode(byte[] inbuf) {
        int size = inbuf.length;
        byte[] outbuf = new byte[((inbuf.length + 2) / 3) * 4];
        int inpos, outpos;
        int val;
        // 情况1：大于等于3
        for (inpos = 0, outpos = 0; size >= 3; size -= 3, outpos += 4) {
            val = inbuf[inpos++] & 0xff;
            val <<= 8;
            val |= inbuf[inpos++] & 0xff;
            val <<= 8;
            val |= inbuf[inpos++] & 0xff;
            // 到此val中存储了3*8=24个二进制位，然后分4次，每次6个二进制位输出
            outbuf[outpos + 3] = (byte) pem_array[val & 0x3f];
            val >>= 6;
            outbuf[outpos + 2] = (byte) pem_array[val & 0x3f];
            val >>= 6;
            outbuf[outpos + 1] = (byte) pem_array[val & 0x3f];
            val >>= 6;
            outbuf[outpos + 0] = (byte) pem_array[val & 0x3f];
        }
        // 情况2：等于1或者等于2
        if (size == 1) {
            val = inbuf[inpos++] & 0xff;
            val <<= 4;
            // 到此val中实际有效二进制位8+4=12个，并且后4个都为0
            outbuf[outpos + 3] = (byte) '='; // pad character;
            outbuf[outpos + 2] = (byte) '='; // pad character;
            outbuf[outpos + 1] = (byte) pem_array[val & 0x3f];
            val >>= 6;
            outbuf[outpos + 0] = (byte) pem_array[val & 0x3f];
        } else if (size == 2) {
            val = inbuf[inpos++] & 0xff;
            val <<= 8;
            val |= inbuf[inpos++] & 0xff;
            val <<= 2;
            // 得到此val中实际有效二进制位为8+8+2=18个，并且后2个为0
            outbuf[outpos + 3] = (byte) '='; // pad character;
            outbuf[outpos + 2] = (byte) pem_array[val & 0x3f];
            val >>= 6;
            outbuf[outpos + 1] = (byte) pem_array[val & 0x3f];
            val >>= 6;
            outbuf[outpos + 0] = (byte) pem_array[val & 0x3f];
        }
        return outbuf;
    }

    private final static char pem_array[] = { 
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', // 0
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', // 1
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', // 2
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', // 3
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', // 4
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', // 5
            'w', 'x', 'y', 'z', '0', '1', '2', '3', // 6
            '4', '5', '6', '7', '8', '9', '+', '/' // 7
    };
	
	
	private final static char[] BASE64_ENCODING_TABLE;  
	   private final static byte[] BASE64_DECODING_TABLE;  
	 
	   static   
	   {  
	       BASE64_ENCODING_TABLE="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();  
	       BASE64_DECODING_TABLE=new byte[]  
	       {  
	           -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  
	           -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,62,-1,-1,-1,63,52,53,54,55,56,57,58,59,60,61,-1,-1,-1,-1,-1,-1,  
	           -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,-1,-1,-1,-1,-1,  
	           -1,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,-1,-1,-1,-1,-1  
	       };  
	   }  
	     
	 
	   /** 
	    * 将数据进行Base64编码。 
	    * @param data 数据 
	    * @param offset 数据中的初始偏移量 
	    * @param length 写入的字节数 
	    * @return 编码后的字符串 
	    */  
	   public final static String encode(byte[] data,int offset,int length)  
	   {  
	       if(data==null)  
	       {  
	           return null;  
	       }  
	         
	       StringBuffer buffer=new StringBuffer();  
	       int[] temp=new int[3];  
	       int end=offset+length;  
	         
	       while(offset<end)  
	       {              
	           temp[0]=data[offset++]&255;  
	             
	           if(offset==data.length)  
	           {  
	               buffer.append(BASE64_ENCODING_TABLE[(temp[0]>>>2)&63]);  
	               buffer.append(BASE64_ENCODING_TABLE[(temp[0]<<4)&63]);  
	               buffer.append('=');  
	               buffer.append('=');  
	                 
	               break;  
	           }  
	             
	           temp[1]=data[offset++]&255;  
	             
	           if(offset==data.length)  
	           {  
	               buffer.append(BASE64_ENCODING_TABLE[(temp[0]>>>2)&63]);  
	               buffer.append(BASE64_ENCODING_TABLE[((temp[0]<<4)|(temp[1]>>>4))&63]);  
	               buffer.append(BASE64_ENCODING_TABLE[(temp[1]<<2)&63]);  
	               buffer.append('=');  
	                 
	               break;  
	           }  
	             
	           temp[2]=data[offset++]&255;  
	             
	           buffer.append(BASE64_ENCODING_TABLE[(temp[0]>>>2)&63]);  
	           buffer.append(BASE64_ENCODING_TABLE[((temp[0]<<4)|(temp[1]>>>4))&63]);  
	           buffer.append(BASE64_ENCODING_TABLE[((temp[1]<<2)|(temp[2]>>>6))&63]);  
	           buffer.append(BASE64_ENCODING_TABLE[temp[2]&63]);  
	       }  
	         
	       return buffer.toString();  
	   }  
	     
	   /** 
	    * 将数据进行Base64编码。 
	    * @param data 数据 
	    * @return 编码后的字符串 
	    */  
	   public final static String encode(byte[] data)  
	   {  
	       return encode(data,0,data.length);  
	   }  
	 
	   /** 
	    * 将字符串进行Base64编码。 
	    * @param str 字符串 
	    * @return 编码后的字符串 
	    */  
	    public final static String encode(String str)  
	    {  
	        return encode(str.getBytes());  
	    }  
	      
	    /** 
	     * 对使用Base64编码的字符串进行解码。 
	     * @param str 经过编码的字符串 
	     * @return 解码后的数据 
	     */  
	    public final static byte[] decode(String str)  
	    {  
	        if(str==null)  
	        {  
	            return null;  
	        }  
	  
	        ByteArrayOutputStream buffer=new ByteArrayOutputStream();  
	        byte[] data=str.getBytes();  
	        int[] temp=new int[4];  
	        int index=0;  
	          
	        while(index<data.length)  
	        {  
	            do  
	            {  
	                temp[0]=BASE64_DECODING_TABLE[data[index++]];  
	            }while(index<data.length&&temp[0]==-1);  
	              
	            if(temp[0]==-1)  
	            {  
	                break;  
	            }  
	  
	            do  
	            {  
	                temp[1]=BASE64_DECODING_TABLE[data[index++]];  
	            }while(index<data.length&&temp[1]==-1);  
	              
	            if(temp[1]==-1)  
	            {  
	                break;  
	            }  
	              
	            buffer.write(((temp[0]<<2)&255)|((temp[1]>>>4)&255));  
	  
	            do  
	            {  
	                temp[2]=data[index++];  
	                  
	                if(temp[2]==61)  
	                {  
	                    return buffer.toByteArray();  
	                }  
	                  
	                temp[2]=BASE64_DECODING_TABLE[temp[2]];  
	            }while(index<data.length&&temp[2]==-1);  
	              
	            if(temp[2]==-1)  
	            {  
	                break;  
	            }  
	              
	            buffer.write(((temp[1]<<4)&255)|((temp[2]>>>2)&255));  
	  
	            do  
	            {  
	                temp[3]=data[index++];  
	                  
	                if(temp[3]==61)  
	                {  
	                    return buffer.toByteArray();  
	                }  
	                  
	                temp[3]=BASE64_DECODING_TABLE[temp[3]];  
	            }while(index<data.length&&temp[3]==-1);  
	              
	            if(temp[3]==-1)  
	            {  
	                break;  
	            }  
	              
	            buffer.write(((temp[2]<<6)&255)|temp[3]);  
	        }  
	          
	        return buffer.toByteArray();  
	    }  

	
}

class PopupAuthenticator extends Authenticator{
	private String username;
	private String password;
	public PopupAuthenticator(String username,String pwd){
	this.username=username;
	this.password=pwd;
	}
	public PasswordAuthentication getPasswordAuthentication(){
	return new PasswordAuthentication(this.username,this.password);
	}
	




 


} 

