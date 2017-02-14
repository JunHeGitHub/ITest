package com.zinglabs.db;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.zinglabs.base.Interface.BaseInterface;
import com.zinglabs.tools.CryptoTools2;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 *
 * @author Administrator
 */

public class CryptoTools3 {
   // DES加密的私钥，必须是8位长的字符串
    private static final byte[] DESkey = "11111111".getBytes();// 设置密钥

 private static final byte[] DESIV = "12345678".getBytes();// 设置向量

 static AlgorithmParameterSpec iv = null;// 加密算法的参数接口，IvParameterSpec是它的一个实现
 private static Key key = null;

 public CryptoTools3() throws Exception {
  DESKeySpec keySpec = new DESKeySpec(DESkey);// 设置密钥参数
  iv = new IvParameterSpec(DESIV);// 设置向量
  SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");// 获得密钥工厂
  key = keyFactory.generateSecret(keySpec);// 得到密钥对象

 }

 public String encode(String data) throws Exception {
  Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");// 得到加密对象Cipher
  enCipher.init(Cipher.ENCRYPT_MODE, key, iv);// 设置工作模式为加密模式，给出密钥和向量
  byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
  BASE64Encoder base64Encoder = new BASE64Encoder();
  return base64Encoder.encode(pasByte);
 }

 public String decode(String data) throws Exception {
  Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
  deCipher.init(Cipher.DECRYPT_MODE, key, iv);
  BASE64Decoder base64Decoder = new BASE64Decoder();
  byte[] pasByte = deCipher.doFinal(base64Decoder.decodeBuffer(data));
  return new String(pasByte, "UTF-8");
 }

 

//测试
 public static void main(String[] args) throws Exception {
     try {
         if(args==null || args.length==0){
           
         }else{
             CryptoTools3 tools = new CryptoTools3();
             if(args[0]!=null){
                 System.out.println("密文:"+tools.decode(args[0]));
             }
         }
//       runTmp.exec("chpasswd < /hd2/mysqlSpe").waitFor();
//       runTmp.exec("passwd mysqlProc <<EOF zinglabs zinglabs EOF").waitFor();
     } catch (Exception e) {
         e.printStackTrace();
     }
     
//  CryptoTools3 tools = new CryptoTools3();
//  System.out.println("加密:" + tools.encode("12"));
//  System.out.println("解密:" + tools.decode("ILZp+y5YhOrvg34ifrqm2g=="));
 }

}

