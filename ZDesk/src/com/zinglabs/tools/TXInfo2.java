package com.zinglabs.tools;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

//import net.solosky.litefetion.LiteFetion;
//import net.solosky.litefetion.bean.Buddy;

//import org.jivesoftware.smack.Chat;
//import org.jivesoftware.smack.ChatManager;
//import org.jivesoftware.smack.Connection;
//import org.jivesoftware.smack.ConnectionConfiguration;
//import org.jivesoftware.smack.MessageListener;
//import org.jivesoftware.smack.PacketListener;
//import org.jivesoftware.smack.XMPPConnection;
//import org.jivesoftware.smack.XMPPException;
//import org.jivesoftware.smack.filter.PacketFilter;
//import org.jivesoftware.smack.packet.Message;
//import org.jivesoftware.smack.packet.Packet;

//import com.umpay.bank.SMSClient;
import com.zinglabs.base.absImpl.BaseAbs;
//import com.zinglabs.sms.TestRecDemoHandler;
//import com.zinglabs.sms.TestSendDemoHandler;
//import com.zinglabs.task.SmSTask;
//import com.zinglabs.task.TXTask;
//import com.zinglabs.tools.MailUtil;
//import com.zinglabs.tools.TXTools;
import com.zinglabs.tools.Utility;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * 同时应用于外线和代理
 *
 */
public class TXInfo2 extends BaseAbs implements Serializable   {
	
	public TXInfo2(String uin, String nick, int face, long flag,int type) {
		super();
		init(SKIP_Logger);
		this.clientid = genClientId();
		this.uin = uin;
		this.nick = nick;
		this.face = face;
		this.flag = flag;
		this.type=type;
		this.sscNum=null;
		this.peerSmsNum=null;
		
//		if(isSmS){
//			
//		}
		
	}
	
	public TXInfo2(int type){
		super();
		init(SKIP_Logger);
		this.clientid = genClientId();
		this.type=type;
		
//		if(this.type==TXTask.TASK_MODE_FETION){
//			client = new LiteFetion();
//		}
		
	}
	public static int txSeed=0;
	public synchronized static String genClientId(){
//		String id=null;
//		String(k.random(0, 99)) + String((new Date()).getTime() % 1000000)
		
		return new Random(txSeed++).nextInt(100) +""+System.currentTimeMillis()%1000000L;
	}
	
	
	
	private static final long serialVersionUID = 2662675048684326025L;
	public ConcurrentHashMap<String, TXInfo2> firends = new ConcurrentHashMap<String, TXInfo2>();
	public ConcurrentHashMap<String, TXInfo2> firends2 = new ConcurrentHashMap<String, TXInfo2>();

	public String uin;
	
	public String peerUin;

	public String txNo;

	public String nick;

	public int face;

	public long flag;
	
	public String cookie = "";

	public String password;
	
	public String ptwebqq;
	
	public String vfwebqq;
	
	public String skey;
	
	public String clientid;
	
	public String psessionid = "";   
	
	public String peerTxNo = "";
	
	public TXInfo2 peerTx;
	
	public int type;
	
	
	public String txVerifyCode;

	public ConcurrentLinkedQueue<TXMsg2> TXMessages=new ConcurrentLinkedQueue<TXMsg2>();
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
	
	public long befTimestamp;
	
	public String sscNum;
	
	public String peerSmsNum;
	
//	pop3.163.com
	public String hostPop3;
//	smtp.163.com
	public String hostSmtp;
//	110
	public String pop3Port;
	
//	25
//	public String smtpPort;
////	zdeskmail@163.com
//	public String uName;
////	zinglabs
//	public String pword;
//	默认分配的技能组
	public String defaultSK;
	
	public String keyWords;
	
	public String firstContent;
	
//	public String toString() {
//		String user = this.uin + "\t\t" + this.txNo + "\t\t" + this.nick
//				+ "\t\t" + this.flag;
//		return user;
//	}
	
	public static void main(String[] args)
	{ 
		System.out.println("main ");
	}
	
	public boolean hasLogin=false;


	
	
//	jid
	public String selfJid=null;
	public String peerJid=null;
	

}
