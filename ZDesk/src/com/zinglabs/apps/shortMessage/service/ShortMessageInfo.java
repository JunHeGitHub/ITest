package com.zinglabs.apps.shortMessage.service;

import java.util.Properties;

public class ShortMessageInfo {
	private String smsServIP = "192.168.0.127";
	private int smsServPort = 7891;
	private String smsServUser = "401588";
	private String smsServPwd = "401588";
	private String smsServProtocol = "cmpp2.0";
	private int sendtime = 1000000;
	private String m_number;
	private String m_content;
	public ShortMessageInfo(Properties properties){
		this.smsServIP = properties.getProperty("smsServIP");
		this.smsServPort = Integer.parseInt(properties.getProperty("smsServPort"));
		this.smsServUser = properties.getProperty("smsServUser");
		this.smsServPwd = properties.getProperty("smsServPwd");
	}
	public ShortMessageInfo(){
		
	}
	public String getSmsServIP() {
		return smsServIP;
	}
	public void setSmsServIP(String smsServIP) {
		this.smsServIP = smsServIP;
	}
	public int getSmsServPort() {
		return smsServPort;
	}
	public void setSmsServPort(int smsServPort) {
		this.smsServPort = smsServPort;
	}
	public String getSmsServUser() {
		return smsServUser;
	}
	public void setSmsServUser(String smsServUser) {
		this.smsServUser = smsServUser;
	}
	public String getSmsServPwd() {
		return smsServPwd;
	}
	public void setSmsServPwd(String smsServPwd) {
		this.smsServPwd = smsServPwd;
	}
	public String getSmsServProtocol() {
		return smsServProtocol;
	}
	public void setSmsServProtocol(String smsServProtocol) {
		this.smsServProtocol = smsServProtocol;
	}
	public int getSendtime() {
		return sendtime;
	}
	public void setSendtime(int sendtime) {
		this.sendtime = sendtime;
	}
	public String getM_number() {
		return m_number;
	}
	public void setM_number(String m_number) {
		this.m_number = m_number;
	}
	public String getM_content() {
		return m_content;
	}
	public void setM_content(String m_content) {
		this.m_content = m_content;
	}
	

}
