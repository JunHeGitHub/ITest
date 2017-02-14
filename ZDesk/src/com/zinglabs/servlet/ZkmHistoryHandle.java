package com.zinglabs.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.db.DAOTools;
import com.zinglabs.db.UserInfo2;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.FileUtils;
import com.zinglabs.util.RandomGUID;
import com.zinglabs.util.ZKMAPPUtils;
import com.zinglabs.util.ZKMConfs;

public class ZkmHistoryHandle {
	
	public static  Logger logger = LoggerFactory.getLogger("ZKM"); 
	
	public static String dbid=ZKMDocServlet.zkmDBId;
	
	//public static String dbid="ZDesk";
	
	public static final String SaveZkmBaseSql="insert into `zkmHistoryInfoBase` (`uuid`,`text`,`iconCls`,`cls`,`leaf`,`recordType`,`parentId`,`title`,`bDate`,`eDate`,`keywords`,`sortPath`,`summary`,`filePath`,`versions`,`createUser`,`createTime`,`lastModiTime`,`lastModiUser`,`lookNum`,`isdel`,`delUser`,`isExpired`,`createRoleName`,`lastModiRole`,`createGroup`,`lastModiGroup`,`keywords1`,`keywords2`,`keywords3`,`keywords4`,`keywords5`,`hotTag`,`sortField`,`versionDate`,`relID`,`vindicator`,`isEmailSend`) " +
											"SELECT ?, `text`, `iconCls`, `cls`, `leaf`, `recordType`, `parentId`, `title`, `bDate`, `eDate`, `keywords`, `sortPath`, `summary`, `filePath`, `versions`, `createUser`, `createTime`, `lastModiTime`, `lastModiUser`, `lookNum`, `isdel`, `delUser`, `isExpired`, `createRoleName`, `lastModiRole`, `createGroup`, `lastModiGroup`, `keywords1`, `keywords2`, `keywords3`, `keywords4`, `keywords5`, `hotTag`, `sortField` ,now(), ?,`vindicator`,`isEmailSend` FROM `zkmInfoBase` where id=?";
	
	public static final String GetFilePathSql="select `contentPath` FROM `zkmInfoBaseDisponseFB` where id=?";
	
	public static final String GetFilePathSql_use="select `contentPath` FROM `zkmInfoBaseDisponse` where id=?";
	
	public static final String SaveZkmFileSql="INSERT INTO `zkmHistoryInfoFile` (`id`,`fileName`,`relName`,`fileType`,`filePath`,`createTime`,`createUser`,`isdel`,`relationId`,`delTime`,`delUser`,`fileUse`,`relationSort`,`relHistoryId`) " +
												"SELECT `id`,`fileName`,`relName`,`fileType`,`filePath`,`createTime`,`createUser`,`isdel`,`relationId`,`delTime`,`delUser`,`fileUse`,`relationSort`,? FROM `zkmInfoFile` where `relationId`=?";
	
	public static final String RecoverZkmBaseSql="INSERT INTO `zkmInfoBase` (`id`,`text`,`iconCls`,`cls`,`leaf`,`recordType`,`parentId`,`title`,`bDate`,`eDate`,`keywords`,`sortPath`,`summary`,`filePath`,`versions`,`createUser`,`createTime`,`lastModiTime`,`lastModiUser`,`lookNum`,`isdel`,`delUser`,`isExpired`,`createRoleName`,`lastModiRole`,`createGroup`,`lastModiGroup`,`keywords1`,`keywords2`,`keywords3`,`keywords4`,`keywords5`,`hotTag`,`sortField`,`vindicator`,`isEmailSend`) " +
													"SELECT ?,`text`,`iconCls`,`cls`,`leaf`,`recordType`,`parentId`,`title`,`bDate`,`eDate`,`keywords`,`sortPath`,`summary`,`filePath`,`versions`,`createUser`,`createTime`,now(),?,`lookNum`,`isdel`,`delUser`,`isExpired`,`createRoleName`,`lastModiRole`,`createGroup`,`lastModiGroup`,`keywords1`,`keywords2`,`keywords3`,`keywords4`,`keywords5`,`hotTag`,`sortField`,`vindicator`,`isEmailSend` FROM `zkmHistoryInfoBase` where `uuid`=?";
	public static final String RecoverZkmFileSql="INSERT INTO `zkmInfoFile` (`id`,`fileName`,`relName`,`fileType`,`filePath`,`createTime`,`createUser`,`isdel`,`relationId`,`delTime`,`delUser`,`fileUse`,`relationSort`) " +
													"SELECT `id`,`fileName`,`relName`,`fileType`,`filePath`,`createTime`,`createUser`,`isdel`,`relationId`,`delTime`,`delUser`,`fileUse`,`relationSort` FROM `zkmHistoryInfoFile` where `relHistoryId` =?";
	
	public static final String d_baseSql="INSERT INTO `zkmInfoBaseDisponseHistory` (`id`,`title`,`bDate`,`eDate`,`createUser`,`createTime`,`area`,`versions`,`hotTag`,`keywords`,`keywords1`,`keywords2`,`keywords3`,`keywords4`,`keywords5`,`summary`,`ToSMS`,`ToEmail`,`notTrue`,`notTimely`,`notNormally`,`basilicType`,`relID`,historyType,companyId,companyName,departmentId,departmentName,lastModiTime,lastModiUser)" + 
											"select ?,`title`,`bDate`,`eDate`,`createUser`,`createTime`,`area`,`versions`,`hotTag`,`keywords`,`keywords1`,`keywords2`,`keywords3`,`keywords4`,`keywords5`,`summary`,`ToSMS`,`ToEmail`,`notTrue`,`notTimely`,`notNormally`,`basilicType`,?,?,companyId,companyName,departmentId,departmentName,lastModiTime,lastModiUser from zkmInfoBaseDisponseFB where id=?";
	
	
	public static void doSaveToHistory(String id)throws Exception{
		if(id!=null && id.length()>0){
			String [] pp={id};
			List<Map> list=DAOTools.queryMap(GetFilePathSql, pp, dbid);
			if(list!=null && list.size()>0){
				Map<String,String> qmap=list.get(0);
				String filePath=qmap.get("filePath");
				if(filePath!=null && filePath.length()>0){
					String fileName=ZKMDocServlet.getContentFileName(filePath);
					File file=new File(fileName);
					String content="";
					if(file.exists() && file.isFile() && file.canRead()){
						content=FileUtils.getFileRealContent(file);
					}
					String newId=new RandomGUID().toString();
					String[] sp={newId,id,id};
					DAOTools.updateForPrepared(SaveZkmBaseSql, sp,dbid);
					String[] fp={newId,id};
					DAOTools.updateForPrepared(SaveZkmFileSql, fp, dbid);
					String sql="update `zkmHistoryInfoBase` set `content`=? where uuid=?";
					String [] p={content,newId};
					DAOTools.updateForPrepared(sql, p, dbid);
				}
			}
		}
	}
	
	/**
	 * 废弃的方法
	 * @param srcId
	 * @param docid
	 * @param historyType
	 * @throws Exception
	 */
	public static void doSaveToHistoryDisponse(String srcId,String docid,String historyType)throws Exception{
		//保存数据至历史
		String newId=new RandomGUID().toString();
		String[] sp={newId,srcId,historyType,docid};
		DAOTools.updateForPrepared(d_baseSql, sp,dbid);
		//保存相关附件至历史
		String[] fp={newId,docid};
		DAOTools.updateForPrepared(SaveZkmFileSql, fp, dbid);
		//保存正文至历史
		String [] pp={docid};
		List<Map> list=DAOTools.queryMap(GetFilePathSql, pp, dbid);
		if(list!=null && list.size()>0){
			Map<String,String> qmap=list.get(0);
			String filePath=qmap.get("contentPath");
			if(filePath!=null && filePath.length()>0){
				File file=new File(filePath);
				if(file.exists() && file.isFile() && file.canRead()){
					file=file.getParentFile();
					if(file.exists() && file.isDirectory()){
						File historyFile=FileUtils.createFolderUseSystemDate(ZKMConfs.confs.getProperty("ZKM_DOC_HISTORY_SAVE_PATH","/usr/local/tomcat/webapps/ZDesk/ZKM/zkmDocs/docHistory"));
						RandomGUID hname=new RandomGUID();
						historyFile=new File(historyFile.getPath() + "/" + hname);
						if(historyFile.exists() && historyFile.isDirectory()){
							org.apache.commons.io.FileUtils.deleteDirectory(historyFile);
							//historyFile.delete();
						}
						historyFile.mkdirs();
						ZKMAPPUtils.fcdirChangeUserAndGroup(historyFile);
						ZKMAPPUtils.dirChangeUserAndGroup(historyFile, true);
						org.apache.commons.io.FileUtils.copyDirectory(file, historyFile);
						String spath=historyFile.getPath();
						String sql="update `zkmInfoBaseDisponseHistory` set `fileContent`=?,contentPath=? where id=?";
						String [] p={spath,filePath,newId};
						DAOTools.updateForPrepared(sql, p, dbid);
					}else{
						createNullFile(file);
					}
				}else{
					createNullFile(file);
				}
			}
		}
	}
	
	public static void doSaveToHistoryDisponse(String id,String historyType)throws Exception{
		//保存数据至历史
		String newId=new RandomGUID().toString();
		String[] sp={newId,id,historyType,id};
		DAOTools.updateForPrepared(d_baseSql, sp,dbid);
		//保存相关附件至历史
		String[] fp={newId,id};
		DAOTools.updateForPrepared(SaveZkmFileSql, fp, dbid);
		//保存正文至历史
		String [] pp={id};
		List<Map> list=DAOTools.queryMap(GetFilePathSql, pp, dbid);
		if(list!=null && list.size()>0){
			Map<String,String> qmap=list.get(0);
			String filePath=qmap.get("contentPath");
			if(filePath!=null && filePath.length()>0){
				File file=new File(filePath);
				if(file.exists() && file.isFile() && file.canRead()){
					file=file.getParentFile();
					if(file.exists() && file.isDirectory()){
						file=ZKMAPPUtils.copyToTempFolder(file);
						File historyFile=FileUtils.createFolderUseSystemDate(ZKMConfs.confs.getProperty("ZKM_DOC_HISTORY_SAVE_PATH","/usr/local/tomcat/webapps/ZDesk/ZKM/zkmDocs/docHistory"));
						historyFile=new File(historyFile.getPath());
						historyFile.mkdirs();
						ZKMAPPUtils.doCommonShellMove(file.getPath(), historyFile.getPath());
						//org.apache.commons.io.FileUtils.copyDirectory(file, historyFile);
						historyFile=new File(historyFile.getPath() + "/" + file.getName());
						ZKMAPPUtils.fcdirChangeUserAndGroup(historyFile);
						ZKMAPPUtils.dirChangeUserAndGroup(historyFile, true);
						String spath=historyFile.getPath();
						spath=spath+"/index.html";
						String sql="update `zkmInfoBaseDisponseHistory` set `fileContent`=?,contentPath=? where id=?";
						String [] p={spath,filePath,newId};
						DAOTools.updateForPrepared(sql, p, dbid);
					}else{
						createNullFile(file);
					}
				}else{
					createNullFile(file);
				}
			}
		}
	}
	
	public static void createNullFile(File file){
		
	}
	
	public static void doRecoverVersion(String docId,String historyId,UserInfo2 user) throws Exception{
		if(docId!=null && docId.length()>0 && historyId!=null && historyId.length()>0){
			String sql="";
			String [] pp={docId};
			List<Map> list=DAOTools.queryMap(GetFilePathSql, pp, dbid);
			if(list!=null && list.size()>0){
				Map<String,String> qmap=list.get(0);
				String filePath=qmap.get("filePath");
				if(filePath!=null && filePath.length()>0){
					String fileName=ZKMDocServlet.getContentFileName(filePath);
					File file=new File(fileName);
					
					sql="delete from `zkmInfoBase` where id=?";
					String[] dz={docId};
					DAOTools.updateForPrepared(sql, dz, dbid);
					sql="delete from `zkmInfoFile` where `relationId`=?";
					DAOTools.updateForPrepared(sql, dz, dbid);
					
					String [] rp={docId,user.loginName2,historyId};
					DAOTools.updateForPrepared(RecoverZkmBaseSql, rp, dbid);
					String[] rf={historyId};
					DAOTools.updateForPrepared(RecoverZkmFileSql, rf, dbid);
					Connection con=null;
					PreparedStatement ps=null;
					ResultSet rs = null;
					Writer writer=null;
					Reader reader =null;
					try{
						sql="select `content` from `zkmHistoryInfoBase` where uuid=?";
						con=DAOTools.getConnectionOutS(dbid);
						ps=con.prepareStatement(sql);
						ps.setString(1, historyId);
						rs=ps.executeQuery();
						if(rs.next()){
							reader = rs.getCharacterStream(1);
							if(reader!=null){
								writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
								char [] buff = new char[1024];//设立缓冲区
								for(int i = 0; (i = reader.read(buff))>0;){
									writer.write(buff, 0, i);
					            }
							}
						}
					}catch(Exception x){
						LogUtil.error(x, logger);
						throw(x);
					}finally{
						if(writer!=null)
							writer.close();
						if(reader!=null)
							reader.close();
						if(rs!=null)
							rs.close();
						if(ps!=null)
							ps.close();
						if(con!=null)
							con.close();
					}
				}
			}
		}
	}
	
	public static Map<String,String> getHistoryVersion(String id) throws Exception{
		String sql="SELECT id,title,bDate,eDate,createUser,createTime,area,versions,hotTag,keywords,keywords1,keywords2,keywords3,keywords4,keywords5,summary,ToSMS,ToEmail,notTrue,notTimely,notNormally,changReason,disponseOpinion,checkOneOpinion,checkTowOpinion,basilicType,zkmDocState,applyType,approveState,appointedCheckUser,disponseUser,disponseBTime,disponseETime,flowNode,flowType,securityUserId,securityUser,securityFolderID,securityFolderName,SMSSendPhone,contentPath,relID,flowID,historyType,fileContent,appointedCheckUserID FROM zkmInfoBaseDisponseHistory " +
				" where id=?";
		Map reMap=null;
		if(id!=null && id.length()>0){
			String [] p={id};
			List<Map> list=DAOTools.queryMap(sql, p, dbid);
			if(list!=null && list.size()>0){
				reMap=list.get(0);
			}
		}
		return reMap;
	}
	
	public static void main(String[] args) throws Exception{
//		ZkmHistoryHandle.doSaveToHistory("CDA92FE3-BE37-08B3-E1AD-D684B6E4D96E");
//		UserInfo user=new UserInfo();
//		user.loginName="ttttt";
//		String doId="CDA92FE3-BE37-08B3-E1AD-D684B6E4D96E";
//		String hid="34A9FCC5-4641-387C-7249-2091E11AA921";
//		ZkmHistoryHandle.doRecoverVersion(doId,hid,user);
//		ZkmHistoryHandle.getHistoryVersion("FE08CB01-7E31-697A-E60E-7B087F8E35C9");
/*		String [] oldfield={"id","title","bDate","eDate","keywords","sortPath","summary","filePath","versions","createUser","createTime","lastModiTime","lastModiUser","lookNum","isdel","delUser","isExpired","createRoleName","lastModiRole","createGroup","lastModiGroup","keywords1","keywords2","keywords3","keywords4","keywords5","hotTag","sortField","vindicator","isEmailSend","changReason","trueFlag","area","sendIsNotTimely"};
		String [] newField={"id","title","bDate","eDate","createUser","createTime","area","versions","hotTag","keywords","keywords1","keywords2","keywords3","keywords4","keywords5","summary","ToSMS","ToEmail","notTrue","notTimely","notNormally","basilicType"};
		StringBuffer nvHas=new StringBuffer();
		StringBuffer olhas=new StringBuffer();
		for(String old:oldfield){
			boolean has=false;
			for(String nv:newField){
				if(nv.equals(old)){
					has=true;
					break;
				}
			}
			if(!has){
				olhas.append(old + ";");
			}
		}
		for(String nv:newField){
			boolean has=false;
			for(String old:oldfield){
				if(old.equals(nv)){
					has=true;
					break;
				}
			}
			if(!has){
				nvHas.append(nv + ";");
			}
		}
		System.out.println("oldHas : " + olhas.toString());
		System.out.println("nvHas : " + nvHas.toString());*/
		
	}
}
