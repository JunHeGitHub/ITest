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



/**
 */
public class MailUtil3 {
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
	public static void main(String[] args) throws Exception {
//		TXInfo tx=new TXInfo(TXTask.TASK_MODE_EMAIL);
//		tx.txNo="MailConsult@163.com";
//		tx.password="zinglabs";
//		tx.hostPop3="pop3.163.com";
//		tx.hostSmtp="smtp.163.com";
//		tx.defaultSK="wsyh";
//		tx.pop3Port="110";
//		receive(tx);
//		receive();
		
	}

	

	public static void estmpMain(String content,String subject,String to,String hostPop3,String password,String txNo) {
	  Socket smtpclient=null;
	  DataOutputStream os=null;
	  BufferedReader is=null;
	    String answer=null;
	    try{        
	         smtpclient=new Socket(hostPop3,25);
	         is=new BufferedReader(new InputStreamReader(smtpclient.getInputStream()));
	         os=new DataOutputStream(smtpclient.getOutputStream());         
	        }
	        catch(UnknownHostException ue){
	         System.out.println("UnknownHostException");
	         System.out.println(ue);
	        }
	        catch(IOException io){
	        	System.out.println("I/O Exception");
	        	System.out.println(io);
	        }
	        try{
	        	System.out.println("now login ");
	        	os.writeBytes("EHLO localhost\r\n");  
	            while ((answer=is.readLine())!=null){
	            	System.out.println("Server:"+answer);
	             if (answer.indexOf("220")!=-1){
	              break;
	             }
	            }   
	            System.out.println("now check serv....");
	            os.writeBytes("AUTH LOGIN\r\n");  
	            while ((answer=is.readLine())!=null){
	             if (answer.indexOf("250")==-1){
	              break;
	             }
	             System.out.println("Server:"+answer);
	            }
	         System.out.println("Server:"+answer);
	         if(password!=null && password.length()>0){
	        	 os.writeBytes(txNo+"\r\n");
		         os.writeBytes(password+"\r\n");
	         }
	         
	            while ((answer=is.readLine())!=null){
	            System.out.println("Server:"+answer);
	             if (answer.indexOf("235")!=-1){
	            	 System.out.println("check sucess");
	                 break;
	             }
	             else if (answer.indexOf("334")==-1) {
	            	 System.out.println("check filed");
	                    os.close();
	                    is.close();
	                    smtpclient.close();  
//	                    System.exit(0);
	             }
	            }         
	            System.out.println("now send mail....");
	         os.writeBytes("MAIL From: < "+txNo+" >\r\n");   
	         os.writeBytes("RCPT To: < "+to+" >\r\n");
//	         os.writeBytes("RCPT To: < nhsoft@126.com >\r\n");         
	     os.writeBytes("DATA\r\n");
	            while ((answer=is.readLine())!=null){
	             if (answer.indexOf("354")!=-1){
	              break;
	             }             
	             System.out.println("Server:"+answer);
	            }
	            System.out.println("Server:"+answer);
	            System.out.println("now send mail content....");
	         os.writeBytes("From: "+txNo+"\r\n");
	            os.writeBytes("To: "+to+"\r\n");
	            os.writeBytes("Subject: "+subject+"\r\n");
	            os.writeBytes("Content-Type: text/html\r\n");         
	            os.writeBytes(content+"\r\n\r\n");
	         os.writeBytes("\r\n.\r\n");
	            while ((answer=is.readLine())!=null){
	            	System.out.println("Server:"+answer);
//	             System.out.println("Server:"+answer);
	             if (answer.indexOf("250")!=-1){
	              break;
	             }             
	            } 
	            os.writeBytes("QUIT\r\n");
	            while ((answer=is.readLine())!=null){
	            	System.out.println("Server:"+answer);
//	             System.out.println("Server:"+answer);
	             if (answer.indexOf("221")!=-1){
	            	 System.out.println("sucess quit !");
	              break;
	             }             
	            }
	           
	            os.close();
	            is.close();
	            smtpclient.close();           
	        }
	        catch(UnknownHostException ue){
	        	ue.printStackTrace();
	        	 System.out.println("UnknownHostException");
	        }
	        catch(IOException io){
	        	io.printStackTrace();
	        	 System.out.println("I/O  2");
	        }
	 
	 }
	
}
