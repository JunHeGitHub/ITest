package com.zinglabs.index.tbl.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.zinglabs.index.tbl.exception.NeedLoginException;
import com.zinglabs.index.tbl.sys.*;


public class SystemUtil {
	
	public String SESSION_PATHS_TEMP_TOKEN = "TBL_TEMPPATHS";
	

	
	public static final String  pageNoForShowdeFaultKey 	= "page.sys.pageNoForShowdefault";
	public static final String  rowsNoForShowdeFaultKey 	= "page.sys.rowsNoForShowdefault";
	public static final String  SysConfFileNm 		 		= "sysconfig.properties";
	public static final String  DelaultUploadConfFileNm 	= "upload.properties";
	public static final String  SystemConfigProperties   	= "sysprop.properties";
	public static final String  AuthorityCodeFileMn		   	= "authorityCode.properties";
	
	
//	private static String webPath = "";
	
	
	public static final String BACK_IMAGE_PATH		= SystemConfig.getProperty("sys.validate.backimage",SysConfFileNm);
	
	public static final String SHRINK_IMAGE_SUFFIX  = SystemConfig.getProperty("upload.image.shrinkImageSuffix",DelaultUploadConfFileNm);
	
	public static final String MEMBER_DEFAULT_IMAGE = SystemConfig.getProperty("sys.member.defaultimage",SysConfFileNm);
	
	
	
	public static final String VALCODE_TOKEN 		= "_VALCODE";
	
	public static final String VALCODE_TEMP_TOKEN 	= "_VALCODE_TEMP";
	
	public static final String FILE_PATH_TOKEN 		= "=*=";
	
	
	public static final String FILE_NAME_TOKEN 		= "@~@";
	
	
	
	public static String getBigImageName(String shrinkImageNm){
		return shrinkImageNm.replaceAll(SHRINK_IMAGE_SUFFIX, "");
	}

	
	
	public static String getMinImageName(String filename){
		int pos ;
		String ext = "";
		if ((pos = filename.lastIndexOf('.')) != (-1)) {
			ext = filename.substring(pos).trim();
		}
		return filename.replaceAll(ext, SHRINK_IMAGE_SUFFIX + ext);
	}
	

	public static boolean userHasdLogined(HttpServletRequest request){
		UserSession us = getUserSessionObj(request);
		return us!=null?true:false;
	}
	
	
	
	public static UserSession getUserSessionObj(HttpServletRequest request){
		UserSession us = (UserSession)request.getSession().getAttribute(UserSession.SESSION_TOKEN);
		return us;
	}
		
	
	public static void logout(HttpServletRequest request){
		request.getSession().setAttribute(UserSession.SESSION_TOKEN,null);
	}
	
	
	public static String transTBLDateToStr(Date date){ 
		if(date==null)return "";
		return DateUtil.transDateToString(DateUtil.MASK_DATE, date);
	}
	
	public static String getFileName(String fileName){
		String temp[] = fileName.split(FILE_NAME_TOKEN);
		return temp.length>1?temp[1]:"";
	}

}
