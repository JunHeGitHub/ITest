package com.zinglabs.base;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.servlet.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.io.*;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.struts.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;

import com.zinglabs.log.LogUtil;
import com.zinglabs.util.Utility;
import com.zinglabs.util.I18NConstants;

public class BaseServlet extends ActionServlet {
	private static final long serialVersionUID = 1L;
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk");
	private static String Algorithm = "DES";
	// ���� �����㷨,���� DES,DESede,Blowfish
	static boolean debug = false;
	/**
	 * we override init() to load some initial process here
	 *
	 * @throws ServletException
	 */
	public void init() throws ServletException {
		try {
			// Make sure to always call the super's init() first
			super.init();
			//getMenus();
			loadMSGFile();
			//������Ȩ��Ϣ
/*			loadLicenceInfo();
			getUnixMACAddress();*/
//			loadHostAddress();
			getWebPath();
			Utility.setContexPath(getServletContext().getRealPath("/"));
			Utility.setZingServerIP(getInitParameter("ZingServerIP"));
			Utility.setDebugSwitchFlag(this.getInitParameter("DebugSwitchFlag"));
			I18NConstants.setPropertiesFilePath(this.getInitParameter("LogoProperties"));
		} catch (Exception ex) {

		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doPost(request, response);
	}

	private void setCharSet(HttpServletRequest request,
			  HttpServletResponse response) throws IOException {
	//System.out.println(request.getCharacterEncoding()); 
	//request.setCharacterEncoding("utf-8");
	//response.setCharacterEncoding("GB2312");
	//request.setCharacterEncoding("UTF-8");

	}
	private static HashMap getParameterMap(HttpServletRequest req) {
		HashMap map = new HashMap();
		Enumeration em = req.getParameterNames();
		while (em.hasMoreElements()) {
			String name = (String) em.nextElement();
			String value[] = req.getParameterValues(name);
			map.put(name, value);
		}
		HttpSession session = req.getSession(true);
		em = session.getAttributeNames();
		while (em.hasMoreElements()) {
			String name = (String) em.nextElement();
			Object value = session.getAttribute(name);
			map.put(name, value);
		}
		return map;
	}

	private void logAccess(HttpServletRequest req) {
		HashMap map = getParameterMap(req);
		Iterator keys = map.keySet().iterator();
		String log = req.getRemoteAddr() + "-";
		log += req.getRequestURL().toString();
		log += " {";
		while (keys.hasNext()) {
			String name = (String) keys.next();
			Object value = map.get(name);
			if (value instanceof String[]) {
				String v[] = (String[]) value;
				log += name + "=(";
				for (int i = 0; i < v.length; i++) {
					log += v[i] + ",";
				}
				log += ")";
			} else {
				log += name + "=" + value.toString() + "; ";
			}
		}
		log += "}";
		LogUtil.debug(log, SKIP_Logger);
	}

	/**
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cashe-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		logAccess(request);
		process(request, response);
	}

	private void getWebPath() {
		String path = "";
		path = getServletContext().getRealPath("/");
		getServletContext().setAttribute("WEBPATH", path);
	}



	// ��ȡ��Ϣ�ļ��������䱣�浽Session�С�
	private void loadMSGFile() {
		String msgFileName = this.getServletContext().getRealPath(
				getInitParameter("MSGProperties"));
		try {
			Properties msgProerties = new Properties();
			File msgFile = new File(msgFileName);
			FileInputStream msgFIS = new FileInputStream(msgFile);
			msgProerties.load(msgFIS);
			getServletContext().setAttribute("MSGProerties", msgProerties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String readFile(){
		String ss = "" ;
		try {
			FileReader fr = new FileReader("/usr/local/tomcat/server/webapps/qc/WEB-INF/zqc.dat");
			BufferedReader br = new BufferedReader(fr);
			String s = "" ;
			while((s = br.readLine()) != null){
				ss =  s ;
				System.out.println(s);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ss ;
	}
	
	private void loadLicenceInfo(){
		String s = readFile();
		String[] aa = s.split(" ");

		try {
			BASE64Decoder base64decoder = new BASE64Decoder(); 
			byte[] dkey = base64decoder.decodeBuffer(aa[0]); 
			byte[] dmac = base64decoder.decodeBuffer(aa[1]); 
			byte[] dlicencenum = base64decoder.decodeBuffer(aa[2]); 
			
			byte[] remac = decryptData(dmac,dkey);
			byte[] renum = decryptData(dlicencenum,dkey);
			Utility.setHostMac2(new String(remac));
			Utility.setAgent_limit(new String(renum));
			System.out.println(new String(remac) + ":" + new String(renum));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static byte[] decryptData(byte[] input, byte[] key) throws Exception {
		SecretKey deskey = new javax.crypto.spec.SecretKeySpec(key, Algorithm);
		if (debug)
			System.out.println("����ǰ����Ϣ:" + byte2hex(input));
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.DECRYPT_MODE, deskey);
		byte[] clearByte = c1.doFinal(input);
		if (debug) {
			System.out.println("���ܺ�Ķ���:" + byte2hex(clearByte));
			System.out.println("���ܺ���ַ�:" + (new String(clearByte)));

		}
		return clearByte;

	}
	
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";

		}
		return hs.toUpperCase();
	}
/*	private void loadLicenceInfo(){
		String filepath = this.getServletContext().getRealPath("/WEB-INF/zinglabs.properties");
		try {
			Properties msgProerties = new Properties();
			File msgFile = new File(filepath);
			FileInputStream msgFIS = new FileInputStream(msgFile);
			msgProerties.load(msgFIS);
			String licence_username = msgProerties.getProperty("LICENCE_USERNAME");
			String agent_limit = msgProerties.getProperty("AGENT_LIMIT");
//			logger.debug("username = " + licence_username + ", number = " + licence_number);
//			Utility.setLicence_username(licence_username);
			
			Utility.setAgent_limit(agent_limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
/*	private void loadHostAddress(){
		String ip = "";
		try {
			Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) e1.nextElement();
				if (!ni.getName().equals("eth0")) {
					continue;
				} else {
					Enumeration<?> e2 = ni.getInetAddresses();
					while (e2.hasMoreElements()) {
						InetAddress ia = (InetAddress) e2.nextElement();
						if (ia instanceof Inet6Address)
							continue;
						ip = ia.getHostAddress();
					}
					break;
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		Utility.setLicence_serverIp(ip);
	}*/
	
    /**  
     * ��ȡunix���mac��ַ.  
     * ��windows��ϵͳĬ�ϵ��ñ�������ȡ.���������ϵͳ�����)���µ�ȡmac��ַ����.  
     * @return mac��ַ  
     */  
    public static String getUnixMACAddress() {   
        String mac = null;   
        BufferedReader bufferedReader = null;   
        Process process = null;   
        try {   
            process = Runtime.getRuntime().exec("ifconfig eth0");// linux�µ����һ��ȡeth0��Ϊ�������� ��ʾ��Ϣ�а���mac��ַ��Ϣ   
            bufferedReader = new BufferedReader(new InputStreamReader(process   
                    .getInputStream()));   
            String line = null;   
            int index = -1;   
            while ((line = bufferedReader.readLine()) != null) {   
                index = line.toLowerCase().indexOf("hwaddr");// Ѱ�ұ�ʾ�ַ�[hwaddr]   
                if (index >= 0) {// �ҵ���   
                    mac = line.substring(index +"hwaddr".length()+ 1).trim();//  ȡ��mac��ַ��ȥ��2�߿ո�   
                    break;   
                }   
            }   
        } catch (IOException e) {   
            e.printStackTrace();   
        } finally {   
            try {   
                if (bufferedReader != null) {   
                    bufferedReader.close();   
                }   
            } catch (IOException e1) {   
                e1.printStackTrace();   
            }   
            bufferedReader = null;   
            process = null;   
        }   
        Utility.setHostMac(mac);
        return mac;   
    }   
  
}
