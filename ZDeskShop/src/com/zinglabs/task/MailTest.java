package com.zinglabs.task;



import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class MailTest
{
String d_email = "abc@boc.com",
d_password = "abc",
d_host = "22.127.27.199",
d_port = "25",
m_to = "abc@boc.com",
m_subject = "Testing",
m_text = "Hey, this is the testing email.";

public MailTest()
{
Properties props = new Properties();
props.put("mail.smtp.user", d_email);
props.put("mail.smtp.host", d_host);
props.put("mail.smtp.port", d_port);
props.put("mail.smtp.starttls.enable","true");
props.put("mail.smtp.auth", "true");
//props.put("mail.smtp.debug", "true");
props.put("mail.smtp.socketFactory.port", d_port);
//props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//props.put("mail.smtp.socketFactory.fallback", "false");

//SecurityManager security = System.getSecurityManager();

try
{
Authenticator auth = new SMTPAuthenticator();
Session session = Session.getInstance(props, auth);
//session.setDebug(true);

MimeMessage msg = new MimeMessage(session);
msg.setText(m_text);
msg.setSubject(m_subject);
msg.setFrom(new InternetAddress(d_email));
msg.addRecipient(Message.RecipientType.TO, new InternetAddress(m_to));
Transport.send(msg);
}
catch (Exception mex)
{
mex.printStackTrace();
} 
}

public static void main(String[] args)
{
	MailTest blah = new MailTest();
}

private class SMTPAuthenticator extends javax.mail.Authenticator
{
public PasswordAuthentication getPasswordAuthentication()
{
return new PasswordAuthentication(d_email, d_password);
}
}
}



//package com.zinglabs.task;
//
//import java.sql.Date;
//import java.util.Properties;
//
//import javax.mail.Address;
//import javax.mail.Authenticator;
//import javax.mail.BodyPart;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Multipart;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//
//import com.zinglabs.db.MailSenderInfo;
//import com.zinglabs.db.MyAuthenticator;
//import com.zinglabs.log.LogUtil;
//
//public class MailTest {
//
//	public boolean sendTextMail(MailSenderInfo mailInfo) {    
//	      // 判断是否需要身份认证    
//	      MyAuthenticator authenticator = null;    
//	      Properties pro = mailInfo.getProperties();   
//	      if (mailInfo.isValidate()) {    
//	      // 如果需要身份认证，则创建一个密码验证器    
//	        authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());    
//	      }   
//	      // 根据邮件会话属性和密码验证器构造一个发送邮件的session    
//	      Session sendMailSession = Session.getDefaultInstance(pro,authenticator);    
//	      try {    
//	      // 根据session创建一个邮件消息    
//	      Message mailMessage = new MimeMessage(sendMailSession);    
//	      // 创建邮件发送者地址    
//	      Address from = new InternetAddress(mailInfo.getFromAddress());    
//	      // 设置邮件消息的发送者    
//	      mailMessage.setFrom(from);    
//	      // 创建邮件的接收者地址，并设置到邮件消息中    
//	      Address to = new InternetAddress(mailInfo.getToAddress());    
//	      mailMessage.setRecipient(Message.RecipientType.TO,to);    
//	      // 设置邮件消息的主题    
//	      mailMessage.setSubject(mailInfo.getSubject());    
//	      // 设置邮件消息发送的时间    
//	      mailMessage.setSentDate(new Date(System.currentTimeMillis()));    
//	      // 设置邮件消息的主要内容    
//	      String mailContent = mailInfo.getContent();    
//	      mailMessage.setText(mailContent);    
//	      // 发送邮件    
//	      Transport.send(mailMessage);   
//	      return true;    
//	      } catch (MessagingException ex) {    
//	          ex.printStackTrace();    
//	      }    
//	      return false;    
//	    }    
//	       
//	    /** *//**   
//	      * 以HTML格式发送邮件   
//	      * @param mailInfo 待发送的邮件信息   
//	      */    
//	    public static boolean sendHtmlMail(MailSenderInfo mailInfo){    
//	      // 判断是否需要身份认证    
//	      MyAuthenticator authenticator = null;   
//	      Properties pro = mailInfo.getProperties();   
//	      //如果需要身份认证，则创建一个密码验证器     
//	      if (mailInfo.isValidate()) {    
//	        authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());   
//	      }    
//	      // 根据邮件会话属性和密码验证器构造一个发送邮件的session    
//	      Session sendMailSession = Session.getDefaultInstance(pro,authenticator);    
//	      try {    
//	      // 根据session创建一个邮件消息    
//	      Message mailMessage = new MimeMessage(sendMailSession);    
//	      // 创建邮件发送者地址    
//	      Address from = new InternetAddress(mailInfo.getFromAddress());    
//	      // 设置邮件消息的发送者    
//	      mailMessage.setFrom(from);    
//	      // 创建邮件的接收者地址，并设置到邮件消息中    
//	      Address to = new InternetAddress(mailInfo.getToAddress());    
//	      // Message.RecipientType.TO属性表示接收者的类型为TO    
//	      mailMessage.setRecipient(Message.RecipientType.TO,to);    
//	      // 设置邮件消息的主题    
//	      mailMessage.setSubject(mailInfo.getSubject());    
//	      // 设置邮件消息发送的时间    
//	      mailMessage.setSentDate(new Date(System.currentTimeMillis()));    
//	      // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象    
//	      Multipart mainPart = new MimeMultipart();    
//	      // 创建一个包含HTML内容的MimeBodyPart    
//	      BodyPart html = new MimeBodyPart();    
//	      // 设置HTML内容    
//	      html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");    
//	      mainPart.addBodyPart(html);    
//	      // 将MiniMultipart对象设置为邮件内容    
//	      mailMessage.setContent(mainPart);    
//	      // 发送邮件    
//	      Transport.send(mailMessage);    
//	      return true;    
//	      } catch (MessagingException ex) {    
//	          ex.printStackTrace();    
//	      }    
//	      return false;    
//	    }  
//	
//	
//	/**
//	 * @author 	
//	 * @function 	
//	 * @description   	
//	 * @example  	
//	 * @requires 
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		try {
////			resp.setContentType(CONTENT_TYPE);
//			MailSenderInfo mailInfo = new MailSenderInfo();    
//		      mailInfo.setMailServerHost("22.127.27.199");    
//		      mailInfo.setMailServerPort("25");    
//		      mailInfo.setValidate(true);    
//		      mailInfo.setUserName("abc@boc.com");    
//		      mailInfo.setPassword("abc");//您的邮箱密码    
//		      mailInfo.setFromAddress("abc@boc.com");    
//		      mailInfo.setToAddress("abc@boc.com");    
//		      mailInfo.setSubject("xxxxxxx");    
//		      mailInfo.setContent("xxxxxxxxxx111111");   
//		         //这个类主要来发送邮件   
//		      MailTest sms = new MailTest();   
//		          sms.sendTextMail(mailInfo);//发送文体格式    
//		          sms.sendHtmlMail(mailInfo);//发送html格式   
//			
//			
//			
//			
////			
////			
////			
////			String host = "22.127.27.199";
////			String to = "abc@boc.com";
////			String from = "abc@boc.com";
////			String subject = "xxxxxxxxxxx";
////			String messageContent = "xxxxxxxxxxxxxxxxxxxx";
////
//////			resp.setContentType(CONTENT_TYPE);
////			// PrintWriter out = resp.getWriter();
////			//
////			// out.println(" <html> ");
////			// out.println(" <head> <title> MailServlet </title> </head> ");
////
////			 MyAuthenticator authenticator = null;   
////			 
////			 authenticator = new MyAuthenticator("abc@boc.com", "abc");
////			 
//////		      //如果需要身份认证，则创建一个密码验证器     
//////		      if (mailInfo.isValidate()) {    
//////		           
//////		      }  
////			
////			
////			
//////			Authenticator authenticator = new Authenticator() {
//////				protected PasswordAuthentication getPasswordAuthentication() {
//////					return new PasswordAuthentication("abc@boc.com", "abc");
//////				}
//////			};   
////			Properties prop = new Properties();
////			prop.put("mail.smtp.port", "25");   
////			
////			
////			prop.setProperty("mail.smtp.host", "22.127.27.199");   
////			//发送邮件的协议   
////			prop.setProperty("mail.transport.protocol", "smtp");   
//////			prop.setProperty("smtp.163.com",mailServer);
////
////			//在连接服务器的时候是否需要验证，发邮件是需要验证的   
////			
////			prop.setProperty("mail.smtp.auth", "true");   
////			
////			Session mySession = Session.getInstance(prop, authenticator);
////			Message msg = new MimeMessage(mySession);
////			msg.setFrom(new InternetAddress(from));
////			InternetAddress[] address = { new InternetAddress(to) };
////			msg.setRecipients(Message.RecipientType.TO, address);
////			msg.setSubject(subject);
////			msg.setSentDate(new Date(System.currentTimeMillis()));
////			msg.setText(messageContent);
////			Transport.send(msg);
//
//			// out.println(" <body> ");
//			// out.println("Mail was send to " + to);
//			// out.println("from " + from);
//			// out.println("using host " + host + ". ");
//			// out.println(" </body> </html> ");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//}
