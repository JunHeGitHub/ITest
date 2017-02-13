package com.zinglabs.util;

import java.io.*;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 持久化工具类
 * 支持:
 * 对象到文件，对象到流，对象到字节数组以及逆向
 * @author 王立铎
 * 2008-03-25
 */
public class ObjectUtil {
	protected InputStream in;
	protected OutputStream out;
	/**
	 * 关闭输入流和输出流
	 * @throws Exception
	 */
	public void closeInOrOut() throws Exception{
		if(in!=null)
			in.close();
		if(out!=null)
			out.close();
	}
	
	/**
	 * 将Object对象持久化到一个String对象中
	 * @param object 传入的对象
	 * @return 返回一个字符串
	 */
	public String objectToString(Object object) throws Exception {
		byte[] ob=objectToBytes(object);
		return bytesToString(ob);
	}
	/**
	 * 将一个Object对象持久化到一个字节数组中
	 * @param object 传入的对象
	 * @return 返回一个字节数组
	 */
	public byte[] objectToBytes(Object object) throws Exception{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		byte[] reBytes=baos.toByteArray();
		oos.close();
		baos.close();
		return reBytes;
	}
	/**
	 * 将一个字节数组转换为String对象
	 * @param ob 传入一个Object对象
	 * @return 返回一个String
	 */
	public String bytesToString(byte[] ob) throws Exception{
		BASE64Encoder enc=new BASE64Encoder();
		return enc.encode(ob);
	}
	/**
	 * 将一个传入的String对象逆转到Object对象
	 * @param str 由BASE64转换后的String对象
	 * @return 返回一个Object
	 */
	public Object stringToObject(String str) throws Exception{
		byte[] bytes=stringToBytes(str);
		return bytesToObject(bytes);
	}
	/**
	 * 将一个传入的String对象逆转为字节数组
	 * @param str 由BASE64转换后的String对象
	 * @return 返回byte[]
	 */
	public byte[] stringToBytes(String str) throws Exception{
		BASE64Decoder dec=new BASE64Decoder();
		return dec.decodeBuffer(str);
	}
	/**
	 * 将传入的字节数据逆转回到Object对象
	 * @param bytes 由对象获得到的byte数组
	 * @return 返回Object
	 */
	public Object bytesToObject(byte[] bytes) throws Exception{
		InputStream in=new ByteArrayInputStream(bytes);
		ObjectInputStream ino = new ObjectInputStream(in);
		Object object=(Object)ino.readObject();
		ino.close();
		in.close();
		return object;
	}
	/**
	 * 将一个对像持久化到一个本地文件中。
	 * @param object 持久化对像
	 * @param file 本地文件对像
	 * @return
	 * @throws Exception
	 */
	public void objectToFile(Object object,File file) throws Exception{
		if(file.exists())
			return;
		OutputStream os=new FileOutputStream(file);
		ObjectOutputStream ino = new ObjectOutputStream(os);
		ino.writeObject(object);
		ino.close();
	}
	
	/**
	 * 将一个本地持久化文件反持久化成对象。
	 * @param file 需要反持久化的文件
	 * @return
	 * @throws Exception
	 */
	public Object fileToObject(File file) throws Exception{
		if(file.exists())
			return null;
		InputStream os=new FileInputStream(file);
		ObjectInputStream ino = new ObjectInputStream(os);
		Object object=(Object)ino.readObject();
		ino.close();
		return object;
	}
	/**
	 * 将一个文件写入一个byte数组
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public byte[] fileToBytes(File file) throws Exception{
		if(!file.exists())
			return null;
		byte[] data = null;
		try 
        {
            in = new FileInputStream(file);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return data;
	}
	/**
	 * 将一个文件持久化成为一个字符串
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public String fileToString(File file) throws Exception{
        byte[] data = fileToBytes(file);
        String restr=bytesToString(data);
        return restr;
	}
	/**
	 * 将一个byte数据写入到一个文件中
	 * @param data
	 * @param file
	 * @throws Exception
	 */
	public void bytesToFile(byte[] data,File file) throws Exception{
        for(int i=0;i<data.length;++i){
            if(data[i]<0) {
                data[i]+=256;
            }
        }
        out = new FileOutputStream(file);    
        out.write(data);
        out.flush();
        out.close();
	}
	
	/**
	 * 将一个持久化字符串反转回文件
	 * @param path
	 * @param fileName
	 * @throws Exception
	 */
	public void stringToFile(String str,File file) throws Exception{
		byte[] bytes=stringToBytes(str);
		bytesToFile(bytes,file);
	}
	
	/**
	 * test
	 * @param args
	 */
	public static void main(String [] args){
		ObjectUtil ou=new ObjectUtil();
		File file=new File("d:\\0309.JPG");
		String str="";
		try {
			str=ou.fileToString(file);
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file1=new File("d:\\test.jpg");
		try {
			ou.stringToFile(str, file1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
