package com.zinglabs.db;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.zinglabs.tools.Utility;



public class Maintest22 {
	  public String getMD5Str(String str) {  //方法1
	         MessageDigest messageDigest = null; 
	         try { 
	             messageDigest = MessageDigest.getInstance("MD5"); 
	             messageDigest.reset(); 
	             messageDigest.update(str.getBytes("UTF-8")); 
	         } catch (NoSuchAlgorithmException e) { 
	             System.out.println("NoSuchAlgorithmException caught!"); 
	             System.exit(-1); 
	         } catch (UnsupportedEncodingException e) { 
	             e.printStackTrace(); 
	         } 
	         byte[] byteArray = messageDigest.digest(); 
	         StringBuffer md5StrBuff = new StringBuffer(); 
	         for (int i = 0; i < byteArray.length; i++) {             
	             if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  //如果一个byte小于等于15，   

	                                                        //Integer.toHexString转换成后前面不会添0，所以这里要判断
	                 md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i])); 
	             else 
	                 md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i])); 
	         } 
	         return md5StrBuff.toString(); 
	     }
	  public static String getMD5(byte[] source) {//方法2
	    String s = null;
	    char hexDigits[] = {       // 用来将字节转换成 16 进制表示的字符
	       '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'};
	     try
	     {
	      java.security.MessageDigest md = java.security.MessageDigest.getInstance( "MD5" );
	      md.update( source );
	      byte tmp[] = md.digest();          // MD5 的计算结果是一个 128 位的长整数，
	                                                  // 用字节表示就是 16 个字节
	      char str[] = new char[16 * 2];   // 每个字节用 16 进制表示的话，使用两个字符，
	                                                   // 所以表示成 16 进制需要 32 个字符
	      int k = 0;                                // 表示转换结果中对应的字符位置
	      for (int i = 0; i < 16; i++) {          // 从第一个字节开始，对 MD5 的每一个字节
	                                                   // 转换成 16 进制字符的转换
	       byte byte0 = tmp[i];                 // 取第 i 个字节
	       str[k++] = hexDigits[byte0 >>> 4 & 0xf];  // 取字节中高 4 位的数字转换,
	                                                               // >>> 为逻辑右移，将符号位一起右移
	       str[k++] = hexDigits[byte0 & 0xf];            // 取字节中低 4 位的数字转换
	      }
	      s = new String(str);                                 // 换后的结果转换为字符串

	     }catch( Exception e )
	     {
	      e.printStackTrace();
	     }
	     return s;
	   }
	  public static String mygetMd5(String source){ //方法3
	    try {
	     java.security.MessageDigest md = java.security.MessageDigest.getInstance( "MD5" );
	     md.update(source.getBytes());
	     byte tmp[] = md.digest();          // MD5 的计算结果是一个 128 位的长整数，
	    
	     StringBuffer sb = new StringBuffer();
	     for (byte b : tmp) {
	     sb.append(String.format("%02x", b));
	     }
	     return sb.toString();
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  return null;
	  }
	 public static void main(String[] args) {
		 String ccc="eseltc2%*02%f0or%m02useSucirytePmrsiisno2%w0eher2%t0pyNema%eD32%o7grnazitaoi%n722%0";
	    	try {
				System.out.println(Utility.encodeStrOrder(ccc));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 
		 
		 
		 String mainSS="eseltc2%f0nuaNem2%fCnudI2%F0OR%M02_ZaPeguFAntuohirez2%w0eher2%m0doeldI3%%D72u%E4D2u%56782%%702na%d02orelaNem3%%D72xx%x72";
		 String r="";
		 for(int i=0;i<mainSS.length();i++,i++)
			  
		 {
		  
		  r=r+mainSS.substring(i+1,i+2)+mainSS.substring(i,i+1);
		  
		 }
		 try {
			r=Utility.decodeStr(r);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		 System.out.println(r);
		 
		 
	  System.out.println(new Maintest22().getMD5Str("9c4f574f3c4ed22e1c5309970dc176d3"));
	  System.out.println(Maintest22.getMD5("9c4f574f3c4ed22e1c5309970dc176d3".getBytes()));
	  System.out.println(Maintest22.mygetMd5("9c4f574f3c4ed22e1c5309970dc176d3"));
	 }

	}


