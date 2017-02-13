package com.zinglabs.apps.webUploader.action;

import java.io.File;
import java.util.Calendar;
import java.util.UUID;

public class UploadHandler {
	
	private UploadHandler(){};
	
	//用单例模式创建UploadHandler对象
	public static UploadHandler getUploadHandler(){
		UploadHandler handler =null;
		if(handler==null||handler.equals(null)){
			handler = new UploadHandler();
		}
		return handler;
	}
	//获取文件类型
	public String getFileType(String name){
		String fileType=name.substring(name.lastIndexOf(".")+1,name.length());
		return fileType.isEmpty()?null:fileType;
	}
	/*
	 * 使用时间生成的文件路径
	 */
	public String getTimePath(String path) {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		String newPath="";
		if(path.lastIndexOf("/")>-1){
			newPath = path + year +"_"+ month+"_"+day + "/";
		}else{
			newPath = path + "/" + year +"_"+ month+"_"+ day + "/";
		}
		
		File file = new File(newPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return newPath;
	}
	
	/*
	 * 生成具有唯一性的UUID文件名称
	 */
	public String getUuidName(String fileName) {
		UUID uuid = UUID.randomUUID();
		return uuid.toString() + fileName.substring(fileName.lastIndexOf('.'));
	}
}
