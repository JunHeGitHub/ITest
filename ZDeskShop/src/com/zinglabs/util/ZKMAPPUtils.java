package com.zinglabs.util;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.UserInfo2;
import com.zinglabs.log.LogUtil;

public class ZKMAPPUtils {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM"); 
	
	public static String getFixZKMDisponseContentPath(String path){
		String filePath=path;
		String ruler=ZKMConfs.confs.getProperty("ZKM_DOC_PATH_REPLACE_RULER_FRIST","^/usr/local/tomcat/webapps/ZDesk/ZKM/zkmDocs");
		Pattern rule=Pattern.compile(ruler);
		Matcher m=rule.matcher(path);
		if(m.find()){
			filePath=m.replaceAll("");
		}
		ruler=ZKMConfs.confs.getProperty("ZKM_DOC_PATH_REPLACE_RULER_END","index.html$");
		String rt="\\\\" + ruler;
		rule=Pattern.compile(rt);
		m=rule.matcher(filePath);
		if(m.find()){
			filePath=m.replaceAll("");
		}
		rt="/" + ruler;
		rule=Pattern.compile(rt);
		m=rule.matcher(filePath);
		if(m.find()){
			filePath=m.replaceAll("");
		}
		return filePath;
	}
	
	/**
	 * 使用FileUtils工具建立日期目录后对期进行linux所属用户/组的变更
	 * @param file
	 */
	public static void fcdirChangeUserAndGroup(File file){
		try{
			String ischwon=getConfisChangeUserGroupDefined();
			String sysUser=getConfLinuxSysUserDefined();
			String sysGroup=getConfLinuxSysGroupDefined();
			if("Y".equals(ischwon.toUpperCase())){
				String tu=SysUtils.linuxGetFileUser(file);
				if(!sysUser.equals(tu)){
					SysUtils.linuxChangeFileUserGroup(sysUser,sysGroup,file.getPath(),false);
				}
				File tf=file.getParentFile();
				String fn=tf.getName();
				//TODO:这里是针对linux写的
				if("/".equals(fn) || fn.equals("mnt")){
					return;
				}
				//TODO:这里应该参数判断，现在写死，所以知识库的上传文件及正文，目前只对针对这两个目录。
				if("uploads".equals(fn) || "zkmDocs".equals(fn) || "zkm_temp_F".equals(fn) || "upfileVaildate".equals(fn) || "luceneIndexFolder_ZDesk".equals(fn)){
					tu=SysUtils.linuxGetFileUser(tf);
					if(!sysUser.equals(tu)){
						SysUtils.linuxChangeFileUserGroup(sysUser,sysGroup,tf.getPath(),false);
					}
				}else{
					fcdirChangeUserAndGroup(tf);
				}
			}
		}catch(Exception x){
			SKIP_Logger.error("fcdirChangeUserAndGroup error : " + x.getMessage());
		}
		
	}
	
	/**
	 * 使用mkdirs建立目录后对期进行linux所属用户/组的变更
	 * @param file　当前文件
	 * @param parentNum　向上变更几层父级目录
	 */
	public static void mkdirsChangeUserAndGroup(File file,int parentNum){
		String ischwon=getConfisChangeUserGroupDefined();
		String sysUser=getConfLinuxSysUserDefined();
		String sysGroup=getConfLinuxSysGroupDefined();
		if("Y".equals(ischwon.toUpperCase())){
			String tu=SysUtils.linuxGetFileUser(file);
			if(!sysUser.equals(tu)){
				SysUtils.linuxChangeFileUserGroup(sysUser,sysGroup,file.getPath(),false);
			}
			File tf=file;
			for(int i=0;i<parentNum;i++){
				tf=tf.getParentFile();
				if(tf==null){
					break;
				}
				tu=SysUtils.linuxGetFileUser(tf);
				if(!sysUser.equals(tu)){
					SysUtils.linuxChangeFileUserGroup(sysUser,sysGroup,tf.getPath(),false);
				}
			}
		}
	}
	
	/**
	 * 对文件进行linux所属用户/组的变更
	 * @param file
	 */
	public static void fileChangeUserAndGroup(File file){
		String ischwon=getConfisChangeUserGroupDefined();
		String sysUser=getConfLinuxSysUserDefined();
		String sysGroup=getConfLinuxSysGroupDefined();
		if("Y".equals(ischwon.toUpperCase())){
			String tu=SysUtils.linuxGetFileUser(file);
			if(!sysUser.equals(tu)){
				SysUtils.linuxChangeFileUserGroup(sysUser,sysGroup,file.getPath(),false);
			}
		}
	}
	
	/**
	 * 对目录进行linux所属用户/组的变更
	 * @param file
	 */
	public static void dirChangeUserAndGroup(File file,boolean recursive){
		String ischwon=getConfisChangeUserGroupDefined();
		String sysUser=getConfLinuxSysUserDefined();
		String sysGroup=getConfLinuxSysGroupDefined();
		if("Y".equals(ischwon.toUpperCase())){
			SysUtils.linuxChangeFileUserGroup(sysUser,sysGroup,file.getPath(),recursive);
		}
	}
	
	/**
	 * 获取参数（DataItemAllocation表中定义的）ZDesk_uploadFiles_isChangeUserGroup的值
	 * @return
	 */
	public static String getConfisChangeUserGroupDefined(){
		String ischwon=ConfInfo.confMapDBConf.get("ZDesk_uploadFiles_isChangeUserGroup");
		if(ischwon==null || ischwon.length()<=0){
			ischwon="N";
		}
		return ischwon;
	}
	
	/**
	 * 获取参数（DataItemAllocation表中定义的）ZDesk_uploadFiles_group的值
	 * @return
	 */
	public static String getConfLinuxSysGroupDefined(){
		String sysGroup=ConfInfo.confMapDBConf.get("ZDesk_uploadFiles_group");
		if(sysGroup==null || sysGroup.length()<=0){
			sysGroup="olcs";
		}
		return sysGroup;
	}
	
	/**
	 * 获取参数（DataItemAllocation表中定义的）ZDesk_uploadFiles_user的值
	 * @return
	 */
	public static String getConfLinuxSysUserDefined(){
		String sysUser=ConfInfo.confMapDBConf.get("ZDesk_uploadFiles_user");
		if(sysUser==null || sysUser.length()<=0){
			sysUser="olcs";
		}
		return sysUser;
	}
	
//	public static synchronized Map addCommonProcessMess(HashMap<String, String> processMes,ZkmCommonProcessService zcp,HttpServletRequest request){
//		String mgs = "提交成功";
//		boolean ist = true;
//		Map<String, String> data = null;
//		try {
//			// 检测流程是否已经加入复核
//			boolean isSave = false;
//			isSave = zcp.checkDataId(processMes);
//			if(isSave){
//				mgs = "数据已经加入复核流程，请重新选择!";
//				ist = false;
//			}else{
//				mgs = "数据保存成功";
//				ist = true;
//				data = zcp.commonProcessAdd(processMes);
//			}
//		} catch (Exception e) {
//			LogUtil.error(e,SKIP_Logger);
//			mgs = "新增数据异常失败。";
//			ist = false;
//		}finally{
//			Map rm = new HashMap();
//			rm.put("success", ist);
//			rm.put("mgs", mgs);
//			rm.put("data", data);
//			return rm;
//		}
//	}
	
//	public static synchronized String randomAccessToData(ZkmCommonProcessService zcp,Map map,UserInfo2 user){ 
//		Date startDate = new Date();
//		Map dataMap = zcp.randomAccessToData(map);
//		if(dataMap==null){
//			return JsonUtils.genUpdateDataReturnJsonStr(false, "没有需要复核的数据。");
//		}else{
//			String print="";
//			for(Iterator iClass = dataMap.entrySet().iterator(); iClass.hasNext();) {
//		        java.util.Map.Entry eClass = (java.util.Map.Entry)iClass.next();
//		        print+=(String)eClass.getValue()+"__";
//			}
//			dataMap.put("dataId", dataMap.get("id"));
//			dataMap.put("createUser", user.loginName2);
//			dataMap.put("disponseUser", user.loginName2);
//			LogFactory.getLog("com.zinglabs.apps.zkmCommonProcess.randomData").debug(print);
//			dataMap.putAll(map);
//			zcp.commonProcessAdd(dataMap);
//			Date endDate = new Date();
//			Long mi = endDate.getTime()-startDate.getTime();
//			LogFactory.getLog("com.zinglabs.apps.zkmCommonProcess.randomData").debug("数据查询时间："+mi+"毫秒");
//		}
//		return JsonUtils.genUpdateDataReturnJsonStr(true, "获取数据成功。", dataMap);
//	}
	
	/**
	 * 移动目录或文件
	 * @param sFile 源文件
	 * @param dFile 目标文件
	 * @return
	 */
	public static boolean doCommonShellMove(String sFile,String dFile){
		String osName=SysUtils.getSystemOSName();
		if(osName.indexOf(SysUtils.OS_WINDOWS_FLAG)>=0){
			return SysUtils.commonShellMove(sFile, dFile, SysUtils.OS_WINDOWS_FLAG);
		}else if(osName.indexOf(SysUtils.OS_LINUX_FLAG)>=0){
			return SysUtils.commonShellMove(sFile, dFile, SysUtils.OS_LINUX_FLAG);
		}
		SKIP_Logger.error("没有配置的操作系统标识。");
		return false;
	}
	
	/**
	 * 复制文件或文件夹至临时目录
	 * @param sfile
	 * @return
	 */
	public static File copyToTempFolder(File sfile){
		String tmpDir=ZKMConfs.confs.getProperty("mergeTempDir","/mnt/zkm_temp_F");
		String dataFolder=FileUtils.getDateFolderStr();
		String tfid=new RandomGUID().toString();
		File dfile=new File(tmpDir + "/" + dataFolder + "/" + tfid);
		try{
			if(!dfile.exists()){
				dfile.mkdirs();
			}else{
				org.apache.commons.io.FileUtils.deleteDirectory(dfile);
				dfile.mkdirs();
			}
			ZKMAPPUtils.dirChangeUserAndGroup(dfile,true);
			org.apache.commons.io.FileUtils.copyDirectory(sfile, dfile);
		}catch(Exception x){
			SKIP_Logger.error("copyToTempFolder error : " + x.getMessage());
			return null;
		}
		return dfile;
	}
	
	public static void main(String[] args){
		System.out.println("Windows 7".indexOf("Windows"));
	}
}
