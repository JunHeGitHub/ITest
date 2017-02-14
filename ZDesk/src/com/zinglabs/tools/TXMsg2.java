package com.zinglabs.tools;

import java.sql.Timestamp;

public class TXMsg2 {

	public TXMsg2(String msg){
		this.msg=msg;
		this.msgTime=new Timestamp(System.currentTimeMillis());
	}
	public String msg;
	public Timestamp msgTime;
	public String smsNum;
	public String subject;
	public String emailAddress;
	
	public String weiBoTitle;
	public String weiBoUrl;
	public String weiBoDateS;
	
}
