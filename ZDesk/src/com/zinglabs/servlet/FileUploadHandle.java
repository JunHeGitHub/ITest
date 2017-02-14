package com.zinglabs.servlet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.db.UserInfo2;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.FileUtils;
import com.zinglabs.util.RandomGUID;

public class FileUploadHandle {
	public static  Logger logger = LoggerFactory.getLogger("ZKM"); 
	public static String[] fieldStr={"id","fileName","relName","fileType","filePath","createTime","createUser","isdel","relationId","fileUse","relationSort"};
	public static String dbTable="zkmInfoFile";
	
	public static void HandleEntrance(String type,HashMap map) throws Exception{
		if(type!=null && !"".equals(type)){
			type=type.toUpperCase();
			if("DB".equals(type)){
				toDb(map);
			}else if("LUCENEINDEX".equals(type)){
				toFileLuceneIndex(map);
			}else if("DB_LUCENEINDEX".equals(type)){
				toDbAndLuceneIndex(map);
			}else if("FILETODBLUCENEINDEX".equals(type)){
				fileToDBToLuceneIndex(map);
			}
		}
	}
	
	public static void toFileLuceneIndex(HashMap map){
		try{
			String filePath=(String)map.get("filePath");
			String fileName=(String)map.get("fileName");
			File file=new File(filePath + "/" + fileName);
			logger.info("index file --- : " + filePath + "/" + fileName);
			if(file!=null && file.exists() && file.isFile()){
				LuceneSearchHandle.doTaskWriter(LuceneSearchHandle.WRITER_TYPE_INDEX_BASE_ALL, map);
			}
		}catch(Exception x){
			LogUtil.error(x,logger);
			LogUtil.error("-----: index writer error..",logger);
		}
	}
	
	public static String fileToDb(HashMap map) throws Exception{
		String id=null;
		//TODO　批量导入　filePath　存储　文本文件正文
		UserInfo2 user=null;
		File f=map.get("file")==null?null:(File)map.get("file");
		if(f!=null && f.exists() && f.isFile()){
			String sql="insert into zkmInfoBase (id,text,recordType,parentId,title,createUser,createTime,filePath,keywords,summary,createRoleName,createGroup,bDate,eDate) values(?,?,?,?,?,?,?,?,?,?,?,?,now(),DATE_ADD(now(),INTERVAL 1 year))";
			try{
				id=new RandomGUID().toString();
				String parendId= map.get("infoId")==null?"":(String)map.get("infoId");
				String infoName=map.get("infoName")==null?"":map.get("infoName").toString();
				String dbpath=ZKMDocServlet.createZKMFolder(id, parendId);
				
				String title=map.get("relName")==null?"":(String)map.get("relName");
				String date=DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
				user=(UserInfo2)map.get("userInfo");
				//文件导入　以文件名为 title;keywords;summary
				
				String [] param={id,infoName,"f",parendId,title,user.loginName2,date,dbpath,title,title,user.userGetFieldValue("userRole"),user.userGetFieldValue("organizationCode")};
				DAOTools.updateForPrepared(sql, param, ConfInfo.confMapDBConf.get("securityDBId"));
				map.put("zkmDocPath", dbpath);
				unRarTOfileContent(map);
				
			}catch(Exception x){
				id=null;
				logger.error("upload file insert data to zkmInfoBase error.");
				LogUtil.error(x,logger);
			}finally{
				if(user!=null){
					user.releaseAll();
				}
			}
		}
		return id;
	}
	
	public static void unRarTOfileContent(Map map) throws Exception{
		File f=map.get("file")==null?null:(File)map.get("file");
		if(f!=null && f.exists() && f.isFile()){
			String zkmDocPath=map.get("zkmDocPath")==null?"":map.get("zkmDocPath").toString();
			String path=ZKMDocServlet.zkmRootPath + zkmDocPath;
			
			File distFile=new File(path);
			if(distFile.exists() && distFile.isDirectory()){
				Map fm=FileUtils.parserFile(f);
				if(fm!=null && fm.get(FileUtils.FILE_METADATA)!=null){
					Map fmt=(HashMap)fm.get(FileUtils.FILE_METADATA);
					String ftype=fmt.get(FileUtils.FILE_MIMETYPE_CONTENT_TYPE)==null?"":fmt.get(FileUtils.FILE_MIMETYPE_CONTENT_TYPE).toString();
					if(ftype.length()>0 && FileUtils.FILE_MIMETYPE_RAR.equals(ftype) && f.canRead()){
						FileUtils.unrar(f, distFile);
					}
				}
			}
		}
	}
	
	public static String toDb(HashMap map) throws Exception{
		int ret=-1;
		String id=null;
		UserInfo2 user=null;
		try{
			user=map.get("userInfo")==null?new UserInfo2():(UserInfo2)map.get("userInfo");
			String infoId=map.get("infoId")==null?"":map.get("infoId").toString();
			String fileUse=map.get("fileUse")==null?"":map.get("fileUse").toString();
			String sortId=map.get("sortId")==null?"":map.get("sortId").toString();
			String fileName=map.get("fileName")==null?"":map.get("fileName").toString();
			String relName=map.get("relName")==null?"":map.get("relName").toString();
			String fileType=map.get("fileType")==null?"":map.get("fileType").toString();
			String filePath=map.get("filePath")==null?"":map.get("filePath").toString();
			
			id=map.get("fileId")==null?(new RandomGUID()).toString():map.get("fileId").toString();
			
			String fielStr="`id`,`fileName`,`relName`,`fileType`,`filePath`,`createTime`,`createUser`,`isdel`,`relationId`,`fileUse`,`relationSort`";
			String vstr="?,?,?,?,?,now(),?,?,?,?,?";
			String[] param={id,fileName,relName,fileType,filePath,user.loginName2,"0",infoId,fileUse,sortId};
			String sql="insert into `" + dbTable + "` (" + fielStr + ") values ( "+ vstr +")";
			DAOTools.updateForPrepared(sql, param, ConfInfo.confMapDBConf.get("securityDBId"));
			logger.info("-------------- up file to db: " + ret);
		}catch(Exception x){
			id=null;
			logger.info("file to db error :");
			logger.error(x.getMessage());
		}finally{
			user.releaseAll();
		}
		return id;
	}
	
	public static void toDbAndLuceneIndex(HashMap map) throws Exception{
		String fileId=toDb(map);
		map.put("fileId", fileId);
		/*if(fileId!=null){
			map.put("fileId", fileId);
			toFileLuceneIndex(map);
		}*/
	}
	
	public static void fileToDBToLuceneIndex(HashMap map) throws Exception{
		String id=fileToDb(map);
		if(id!=null){
			map.put("infoId", id);
			String fileId=toDb(map);
			map.put("fileId", fileId);
			/*if(fileId!=null){
				map.put("fileId", fileId);
				toFileLuceneIndex(map);
			}*/
		}
	}
	
}
