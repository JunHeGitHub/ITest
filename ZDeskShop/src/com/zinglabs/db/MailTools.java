package com.zinglabs.db;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;

import org.apache.commons.mail.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;   

import javax.mail.FetchProfile;   
import javax.mail.Folder;   
import javax.mail.Message;   
import javax.mail.Multipart;
import javax.mail.Session;   
import javax.mail.Store;   
import javax.mail.internet.InternetAddress;   
import javax.mail.Part; 

import java.io.BufferedInputStream; 
import java.io.BufferedOutputStream; 
import java.io.BufferedReader;
import java.io.File; import java.io.FileOutputStream; 
import java.io.InputStream; import java.io.InputStreamReader;
import java.text.SimpleDateFormat; 
import java.util.Properties;   
import javax.mail.BodyPart; 
import javax.mail.Folder; 
import javax.mail.Message; 
import javax.mail.MessagingException; 
import javax.mail.Multipart; 
import javax.mail.Part; 
import javax.mail.Session; 
import javax.mail.Store; 
import javax.mail.URLName; 
import javax.mail.internet.InternetAddress; 
import javax.mail.internet.MimeMessage; 
import javax.mail.internet.MimeUtility;   
import com.sun.mail.pop3.POP3Message; 


import com.zinglabs.log.LogUtil;
import com.zinglabs.task.StartTask;

public class MailTools {

	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	public static void revMail(){
//		POP3Client client = new POP3Client( );
//		client.connect("www.discursive.com");
//		client.login("tobrien@discursive.com", "secretpassword");
//		POP3MessageInfo[] messages = client.listMessages( );
//		for (int i = 0; i < messages.length; i++) {
//		int messageNum = messages[i].number;
//		System.out.println( "************* Message number: " + messageNum );
//		Reader reader = client.retrieveMessage( messageNum );
//		System.out.println( "Message:\n" + IOUtils.toString( reader ) );
//		IOUtils.closeQuietly( reader );
//		}
//		client.logout( );
//		client.disconnect( );
		
		
		// Get directory
				
		
	}
	
	
	 public static void receive()   
	    {   
	        try    
	        {  
	        	
	            Properties props = new Properties();      
	            Session s = Session.getInstance(props);      
	            Store store = s.getStore("pop3");      
	               
	            //对应改成自己的用户名和密码   
	            store.connect("pop.163.com", "zdeskmail@163.com", "zinglabs");      
	      

	          
	            Folder folder = store.getFolder("Inbox");      
	            folder.open(Folder.READ_WRITE);      
	     
	            FetchProfile profile = new FetchProfile();      
	            profile.add(FetchProfile.Item.ENVELOPE);      
	            Message arraymessage[] = folder.getMessages();      
	            folder.fetch(arraymessage, profile);      
	     
	            System.out.println("收件箱的邮件数：" + arraymessage.length);      
	            for (int i = 0; i < arraymessage.length; i++)    
	            {      
	                //邮件发送者      
	                String from = arraymessage[i].getFrom()[0].toString();      
	                InternetAddress ia = new InternetAddress(from);      
	                   
	                System.out.println("******" + i + "******");   
	                   
	                System.out.println("From:" + ia.getPersonal() + '(' + ia.getAddress() + ')');      
	                
	                //邮件标题      
	                System.out.println("Title:" + arraymessage[i].getSubject());      
	                   
	                //邮件大小      
	                System.out.println("Size:" + arraymessage[i].getSize());      
	                   
	                //邮件发送时间      
	                System.out.println("Date:" + arraymessage[i].getSentDate());     
	                
	                String content = getMailContent((Part)arraymessage[i]);//获取内容 
	                
//	                String content2 = arraymessage[i].getContent().toString();
//	                System.out.println(content2+" content:" +content);
//	                arraymessage[i].
	            }      
	            
	            
	            
	            folder.open(Folder.READ_ONLY);

//	         // Get directory
//	         Message message[] = folder.getMessages();
//
//	         for (int i=0, n=message.length; i<n; i++) {
//	         System.out.println(i + ": " + message.getFrom()[0]
//	             + "\t" + message[i].getSubject());
//	         }
	            
//	            Message message[] = folder.getMessages();
//				for (int i=0, n=message.length; i<n; i++) {
////				   System.out.println(i + ": " + message.getFrom()[0]
////				+ "\t" + message.getSubject());
//
//					BufferedReader reader = new BufferedReader (
//							   new InputStreamReader(System.in));
//					message[1]
//				   System.out.println("Do you want to read message? " +
//				"[YES to read/QUIT to end]");
//				   String line = reader.readLine();
//				   	
//				   		if ("YES".equals(line)) {
//					   message.writeTo(System.out);
//				
//				   } else if ("QUIT".equals(line)) {
//				break;
//				   }
//				}
	     
	            folder.close(false);      
	               
	            store.close();      
	               
	        } catch (Exception ee) {      
	            ee.printStackTrace();      
	        }      
	        
	        
	        
	    }  
	 
	  private static String getMailContent(Part part) throws Exception {
		StringBuffer bodytext = new StringBuffer();/* 存放邮件内容 //判断邮件类型,不同类型操作不同 */
		if (part.isMimeType("text/plain")) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("text/html")) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMailContent(multipart.getBodyPart(i));
			}
		} else if (part.isMimeType("message/rfc822")) {
			getMailContent((Part) part.getContent());
		} else {
		}
		return bodytext.toString();
	}       
	
	public static void sendMail(){
		try {
// resp.setContentType(CONTENT_TYPE);
			String host = "smtp.163.com";
			String to = "zdeskmail@163.com";
//			String from = req.getParameter("from");
			String subject = "人寿财在线客服邮件__"+System.currentTimeMillis();
			String messageContent = "人寿财在线客服邮件__"+System.currentTimeMillis();
//			esmtm smtm  pop3
			HtmlEmail email = new HtmlEmail(); 
			email.setHostName(host);
			
			email.setSmtpPort(25);
			email.setAuthenticator(new DefaultAuthenticator("zdeskmail@163.com","zinglabs"));
//			email.setDebug(true);
			email.setSSL(false);
			email.setTLS(false);
			email.setCharset("GBK");
//			email.setSSLOnConnect(false);
			email.setFrom("zdeskmail@163.com");
			email.setSubject(subject);
			email.setHtmlMsg(messageContent);  // set the alternative message  
	    	email.setTextMsg("抱歉！您的邮件客户端不支持html格式");  // send the email
			email.addTo(to);
			email.send();
			
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
			LogUtil.error("processCommands ACTION_SEND_MAIL", SKIP_Logger);
			LogUtil.error(e, SKIP_Logger);
		}
		
		
	}
	/**
	 * @author 	
	 * @function 	
	 * @description   	
	 * @example  	
	 * @requires 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		sendMail();
		receive();
	}

	
	
}
