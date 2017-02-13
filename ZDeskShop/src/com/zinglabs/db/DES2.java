package com.zinglabs.db;
 

import java.security.Key;   
import java.security.SecureRandom;   
  
import javax.crypto.Cipher;   
import javax.crypto.KeyGenerator;   
  
import sun.misc.BASE64Decoder;   
import sun.misc.BASE64Encoder;   
  
public class DES2 {   
  Key key;   
  public DES2(String str) {   
    setKey(str);//生成密匙   
  }   
  public DES2() {   
    setKey("siyue_qi");   
  }   
  /**  
   * 根据参数生成KEY  
   */  
  public void setKey(String strKey) {   
      try {   
        KeyGenerator _generator = KeyGenerator.getInstance("DES");   
        _generator.init(new SecureRandom(strKey.getBytes("UTF-8")));   
        this.key = _generator.generateKey();   
        _generator = null;   
      } catch (Exception e) {   
        throw new RuntimeException(   
           "Error initializing SqlMap class. Cause:" + e);   
      }   
  }   
  
  /**  
   * 加密String明文输入,String密文输出  
   */  
  public String getEncString(String strMing) {   
      byte[] byteMi = null;   
      byte[] byteMing = null;   
      String strMi ="";   
      BASE64Encoder base64en = new BASE64Encoder();   
      try {   
        byteMing = strMing.getBytes("UTF8");   
        byteMi = this.getEncCode(byteMing);   
        strMi = base64en.encode(byteMi);   
      } catch (Exception e) {   
        throw new RuntimeException(   
           "Error initializing SqlMap class. Cause:" + e);   
      } finally {   
        base64en = null;   
        byteMing = null;   
        byteMi = null;   
      }   
      return strMi;   
  }   
  
  /**  
   * 解密 以String密文输入,String明文输出  
   * @param strMi  
   * @return  
   */  
  public String getDesString(String strMi) {   
      BASE64Decoder base64De = new BASE64Decoder();   
      byte[] byteMing = null;   
      byte[] byteMi = null;   
      String strMing ="";   
      try {   
        byteMi = base64De.decodeBuffer(strMi);   
        byteMing = this.getDesCode(byteMi);   
        strMing = new String(byteMing,"UTF8");   
      } catch (Exception e) {   
        throw new RuntimeException(   
          "Error initializing SqlMap class. Cause:" + e);   
      } finally {   
        base64De = null;   
        byteMing = null;   
        byteMi = null;   
      }   
      return strMing;   
  }   
  
  /**  
   * 加密以byte[]明文输入,byte[]密文输出  
   * @param byteS  
   * @return  
   */  
  public byte[] getEncCode(byte[] byteS) {   
      byte[] byteFina = null;   
      Cipher cipher;   
      try {   
        cipher = Cipher.getInstance("DES");   
        cipher.init(Cipher.ENCRYPT_MODE, key);   
        byteFina = cipher.doFinal(byteS);   
      } catch (Exception e) {   
        throw new RuntimeException(   
          "Error initializing SqlMap class. Cause:" + e);   
      } finally {   
        cipher = null;   
      }   
      return byteFina;   
  }   
  
  /**  
   * 解密以byte[]密文输入,以byte[]明文输出  
   * @param byteD  
   * @return  
   */  
  public byte[] getDesCode(byte[] byteD) {   
      Cipher cipher;   
      byte[] byteFina = null;   
      try {   
        cipher = Cipher.getInstance("DES");   
        cipher.init(Cipher.DECRYPT_MODE, key);   
        byteFina = cipher.doFinal(byteD);   
      } catch (Exception e) {   
        throw new RuntimeException(   
          "Error initializing SqlMap class. Cause:" + e);   
      } finally {   
        cipher = null;   
      }   
      return byteFina;   
  }   
  
  public static void main(String args[]) {   
      DES2 des = new DES2();   
      // 设置密钥     
      String str1 ="12";   
      //DES加密   
      String str2 = des.getEncString(str1);   
      String deStr = des.getDesString(str2);   
      System.out.println("加密后:"+str2);   
      System.out.println("解密后:"+deStr);
      
      //DES解密   
//      System.out.println("解密后:"+des.getDesCode("[B@1fc3828".getBytes()));   
  }  
}