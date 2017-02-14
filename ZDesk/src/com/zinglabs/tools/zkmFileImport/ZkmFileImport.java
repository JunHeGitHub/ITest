package com.zinglabs.tools.zkmFileImport;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.db.DAOTools;
import com.zinglabs.db.UserInfo2;
import com.zinglabs.log.LogUtil;
import com.zinglabs.servlet.ZKMDocServlet;
import com.zinglabs.servlet.zkmDocTemplate.ZKMDocHelper;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.FileUtils;
import com.zinglabs.util.RandomGUID;
import com.zinglabs.util.WebFormUtil;
import com.zinglabs.util.ZKMAPPUtils;
import com.zinglabs.util.ZKMConfs;
import com.zinglabs.util.excelUtils.ExcelHelper;
import com.zinglabs.util.excelUtils.model.ReadExcelModel;
import com.zinglabs.util.flowUtil.FlowDisponse;
import com.zinglabs.util.flowUtil.FlowHelper;
import com.zinglabs.util.flowUtil.FlowDisponseImp.ZKMFlowDisponse;
import com.zinglabs.util.officeConvert.OfficeConvert;

public class ZkmFileImport {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM"); 
	public int total=0;
	public int success=0;
	public int error=0;
	public int xlsnum=0;
	public int docnum=0;
	public String dbid=ZKMDocServlet.zkmDBId;
	public boolean stopFlag=false;
	public List<File> xlslist=new ArrayList();
	public List doclist=new ArrayList();
	public Map docMap=new HashMap();
	public List elist=new ArrayList();
	public List sameList=new ArrayList();
	public Map sameDoc=new HashMap();
	public ImpDataFix idf=new ImpDataFix();
	public int tnum=0;
	public String ttitle="";
	public File tfile=null;
	
	public void imp2Database(UserInfo2 user,HttpServletRequest request){
		
		WebFormUtil wfu=new WebFormUtil(request);
		String importType=wfu.get("importType");
		String filePath=wfu.get("filePath");
		String fileName=wfu.get("fileName");
		File unzipf=null;
		boolean isZip=false;
		boolean unZip=true;
		if(filePath.length()>0 && fileName.length()>0){
			File upf=new File(filePath);
			if(upf.exists()&& upf.isFile() && upf.canRead()){
				String en=FileUtils.getFileExtendName(upf);
				if(en.length()>0 && en.toUpperCase().equals(".ZIP")){
					isZip=true;
				}
				if(isZip){
					String tmpDir=ZKMConfs.confs.getProperty("mergeTempDir","/mnt/zkm_temp_F");
					unzipf=FileUtils.createFolderUseSystemDate(tmpDir);
					RandomGUID rg=new RandomGUID();
					unzipf=new File(unzipf.getPath() + "/" + rg.toString());
					ZKMAPPUtils.fcdirChangeUserAndGroup(unzipf);
					try{
						FileUtils.unzip(upf, unzipf);
						ZKMAPPUtils.dirChangeUserAndGroup(unzipf, true);
						unZip=true;
					}catch(Exception x){
						LogUtil.error("解压文件－异常失败。",SKIP_Logger);
						LogUtil.error(x, SKIP_Logger);
						unZip=false;
					}
				}
			}
			if(unZip){
				boolean flowInfo=true;
				String flowId=new RandomGUID().toString();
				if(importType.equals("noFlow")){
					wfu.set("flowType", "zkmUsuallyFlow");
					wfu.set("zkmDocState", "已发布");
					wfu.set("applyType", "新建");
					wfu.set("versions", "0.01");
					wfu.set("flowID", flowId);
				}else if(importType.equals("zkmUsuallyFlow")){
					wfu.set("flowType", "zkmUsuallyFlow");
					wfu.set("flowNode", "zkmJB");
					wfu.set("zkmDocState", "起草");
					wfu.set("applyType", "新建");
					wfu.set("flowID", flowId);
				}else if(importType.equals("zkmGraveFlow")){
					wfu.set("flowType", "zkmUsuallyFlow");
					wfu.set("flowNode", "zkmJB");
					wfu.set("zkmDocState", "起草");
					wfu.set("applyType", "新建");
					wfu.set("flowID", flowId);
				}else{
					flowInfo=false;
				}
				if(flowInfo){
					File[] fs=unzipf.listFiles();
					for(File f:fs){
						String title="";
						if(f.exists() && f.isDirectory() && f.canRead()){
							title=f.getName();
							wfu.set("title", title);
							String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
							wfu.set("bDate", date);
						}
						//建立数据
						HashMap redata=ZKMDocServlet.zkmDocDisponseAdd(wfu, user);
						String dataid="";
						//建立正文
						if(((Boolean)redata.get("success")).booleanValue()){
							Map zkmDoc=(HashMap)redata.get("data");
							dataid=(String)zkmDoc.get("id");
							if(zkmDoc!=null){
								File savePath=null;
								String rootpath=ZKMConfs.confs.getProperty("zkmFlowDocSaveDir","/usr/local/nginx/html/ZDesk/zkmDocs/flowDisponseDocs/");
								String dataFolder=FileUtils.getDateFolderStr();
								savePath=new File(rootpath + "/" +  dataFolder +"/" +dataid);
								if(!savePath.exists()){
									savePath.mkdirs();
									ZKMAPPUtils.fcdirChangeUserAndGroup(savePath);
								}
								boolean copySuccess=true;
								try{
									org.apache.commons.io.FileUtils.copyDirectory(f, savePath);
									ZKMAPPUtils.dirChangeUserAndGroup(savePath, true);
								}catch(Exception x){
									LogUtil.error(x, SKIP_Logger);
									LogUtil.error("导入复制异常。", SKIP_Logger);
									copySuccess=false;
								}
								if(copySuccess){
									File [] cfs=savePath.listFiles();
									File contentFile=null;
									for(File cf:cfs){
										if(cf.isFile()&& cf.canRead()){
											String fn=cf.getName();
											if(fn.toUpperCase().indexOf("INDEX")>=0){
												contentFile=cf;
											}else if(fn.indexOf(title)>=0){
												contentFile=cf;
											}
											if(contentFile!=null){
												String enn=FileUtils.getFileExtendName(cf);
												if(enn.toUpperCase().equals(".HTML")){
													contentFile=cf;
													break;
												}else{
													contentFile=null;
												}
											}
										}
									}
									if(contentFile!=null){
										boolean contentSet=true;
										try{
											String content=FileUtils.getFileRealContent(contentFile);
											wfu.set("id", dataid);
											wfu.set("data", content);
											wfu.set("contentPath", contentFile.getPath());
											wfu.set("path", savePath.getPath());
											HashMap docData=ZKMDocServlet.setZkmDisponseContent(wfu);
											if(((Boolean)docData.get("success")).booleanValue()){
												contentSet=true;
											}else{
												contentSet=false;
											}
										}catch(Exception x){
											LogUtil.error(x, SKIP_Logger);
											LogUtil.error("正文导入异常。", SKIP_Logger);
											contentSet=false;
										}
										if(!contentSet){
											//正文导入异常的处理
										}
									}else{
										//没有正文的处理
									}
								}else {
									//文件复制失败的处理。
								}
							}else{
								//获取数据（ID）失败的处理
							}
							//直接发布
							if(importType.equals("noFlow")){
								String flowType=wfu.get("flowType");
								FlowDisponse flowd=FlowHelper.getFlowDisponse();
								try{
									Map end=FlowHelper.getEnd(flowType);
									Map rdata=ZKMDocServlet.zkmDocDisponseSelectOne(dataid);
									flowd.disponseEnd(end, rdata, null);
								}catch(Exception x){
									LogUtil.error(x, SKIP_Logger);
									LogUtil.error("直接发布异常。", SKIP_Logger);
								}
							}
						}else{
							//添加失败的处理
						}
					}
				}else{
					//流程类别异常提示
				}
			}else{
				//解压失败提示
			}
		}else{
			//没有文件提示
		}
	}
	
	public void imp2Database(){
		/*initImpFiles();
		xlsnum=xlslist.size();
		docnum=doclist.size();
		docnum+=sameList.size();
		if(stopFlag){
			return;
		}*/
		initImpDocs();
		impDocToDB();
	}
	
	public void initImpDocs(){
		String dfolder=ZKMConfs.confs.getProperty("docFolder");
		File file=new File(dfolder);
		if(file.exists() && file.isDirectory()){
			File [] fs=file.listFiles();
			total=fs.length;
			for(int i=0;i<fs.length;i++){
				File f=fs[i];
				doclist.add(f);
			}
		}
	}
	
	public void impDocToDB(){
		for(int i=0;i<doclist.size();i++){
			boolean flag=true;
			File fs=(File)doclist.get(i);
			String title=fs.getName();
			title=title.substring(0,title.lastIndexOf("."));
			String id=(new RandomGUID()).toString();
			String dbsortName="";
			String name=title.trim();
			name=name.replaceAll("[\r|\n]", "");
			
			tnum=i+2;
			ttitle=name;
			
			String docDBPath="";
			File serverFile=null;
			if(name==null || name.length()<=0){
				elist.add(genMgsMap("数据导入失败","“标题”数据为空。"));
				error++;
				continue;
			}
			
			dbsortName=getDBSort(name);
			
			//获取分类
			Map pmap=getSortFolder(dbsortName);
			if(pmap!=null){
				try{
					docDBPath=ZKMDocServlet.createZKMFolder(id, (String)pmap.get("id"));
				}catch(Exception x){
					elist.add(genMgsMap("数据导入失败","建立知识库分类路径失败，跳过。"));
					LogUtil.error(x, SKIP_Logger);
					error++;
					continue;
				}
			}else{
				continue;
			}
			//doc文档入附件
			File obj=fs;
			if(obj!=null){
				File f=(File)obj;
				serverFile=fileToDb(id,f);
				if(serverFile==null){
					continue;
				}
			}else{
				obj=(File)sameDoc.get(name);
				if(obj!=null){
					elist.add(genMgsMap("数据导入失败","数据所对应文档名称重复，跳过。"));
				}else{
					elist.add(genMgsMap("数据导入失败","未找到对应的文档，跳过。"));
				}
				error++;
				continue;
			}
			//将doc文档转换成html,即知识库正文。
			File hfile=null;
			if(serverFile!=null && serverFile.isFile() && serverFile.canRead() && docDBPath.length()>0){
				hfile=doc2Html(serverFile,docDBPath);
				//hfile=new File("e:/111.x");
				if(hfile==null){
					continue;
				}
			}else{
				elist.add(genMgsMap("数据导入失败","对应的服务器文档或分类路径异常，跳过。"));
				error++;
				continue;
			}
			//生成数据库知识库记录
			flag=dataToDb(id,name,pmap,docDBPath);
			if(!flag){
				continue;
			}
			doIndex(id);
			success++;
		}
	}
	
	public void initImpFiles(){
		String efolder=ZKMConfs.confs.getProperty("excelFolder");
		String dfolder=ZKMConfs.confs.getProperty("docFolder");
		File efile=new File(efolder);
		if(efile.exists() && efile.isDirectory()){
			getChildFolderFiles(efile);
		}else{
			stopFlag=true;
			elist.add(genMgsMap("读取错误",null,"数据目录“"+efolder+"”不存在或不是目录，程序中止。"));
		}
		File dfile=new File(dfolder);
		if(dfile.exists() && dfile.isDirectory()){
			getChildFolderFiles(dfile);
		}else{
			stopFlag=true;
			elist.add(genMgsMap("读取错误",null,"正文目录“"+dfolder+"”不存在或不是目录，程序中止。"));
		}
	}
	
	public void getChildFolderFiles(File folder){
		if(folder.exists() && folder.isDirectory()){
			File [] cfile=folder.listFiles();
			for(int i=0;i<cfile.length;i++){
				File f=cfile[i];
				if(f.isFile() && f.exists()){
					String name=f.getName();
					String ename="";
					if(name.lastIndexOf(".")>0){
						ename=name.substring(name.lastIndexOf("."),name.length()).toLowerCase();
					}else{
						continue;
					}
					if(".xls".equals(ename)){
						xlslist.add(f);
					}
					if(".doc".equals(ename) || ".docx".equals(ename)){
						if(!hasSame(f)){
							doclist.add(f);
							String key=name.trim().substring(0,name.lastIndexOf("."));
							docMap.put(key, f);
						}
					}
				}
				if(f.isDirectory() && f.exists()){
					getChildFolderFiles(f);
				}
			}
		}
	}
	
	public boolean hasSame(File file){
		boolean same=false;
		if(file.exists() && file.isFile()){
			String name=file.getName();
			String key=name.trim().substring(0,name.lastIndexOf("."));
			Object obj=docMap.get(key);
			if(obj!=null && obj instanceof File){
				File f=(File)obj;
				docMap.remove(key);
				sameList.add(genMgsMap("重名文件",f,"doc文档文件件名重复，无法导入。"));
				sameList.add(genMgsMap("重名文件",file,"doc文档文件件名重复，无法导入。"));
				sameDoc.put(key, f);
				return true;
			}
			obj=sameDoc.get(key);
			if(obj!=null && obj instanceof File){
				File f=(File)obj;
				sameList.add(genMgsMap("重名文件",f,"doc文档文件件名重复，无法导入。"));
				return true;
			}
		}
		return same;
	}
	
	public Map genMgsMap(String type,String mgs){
		Map rmap=new HashMap();
		rmap.put("type", type);
		if(tfile!=null && tfile.exists() && tfile.isFile()){
			rmap.put("file", tfile.getPath());
		}else{
			rmap.put("file", "");
		}
		rmap.put("num", tnum);
		rmap.put("title", ttitle);
		rmap.put("mgs", mgs);
		return rmap;
	}
	
	public Map genMgsMap(String type,File file,String mgs){
		tfile=file;
		tnum=0;
		ttitle="";
		return genMgsMap(type,mgs);
	}
	
	public void impToDB(){
		if(xlslist.size()>0){
			for(int i=0;i<xlslist.size();i++){
				File file=(File)xlslist.get(i);
				List list=xlsToList(file);
				if(list!=null){
					total+=list.size();
					ListToDb(list,file);
				}
			}
		}
	}
	
	public List xlsToList(File file){
		List list=null;
		if(file.exists() && file.isFile()){
			InputStream in=null;
			try{
				in=new FileInputStream(file);
				ReadExcelModel rem=new ReadExcelModel();
				rem.setStartRowIndex(1);
				rem.setSheetIndex(0);
				rem.setIn(in);
				ExcelHelper eh=new ExcelHelper();
				list=eh.readExcel(rem);
			}catch(Exception x){
				elist.add(genMgsMap("文件读取错误",file,"中止文件“"+file.getPath()+"”数据入库。"));
				LogUtil.error(x, SKIP_Logger);
			}
		}
		return list;
	}
	
	public String getDBSort(String name){
		if(idf.getFixValuse(name)!=null){
			return idf.getFixValuse(name);
		}else{
			String rule="[_|＿].+\\d?";
			Pattern p = Pattern.compile(rule);
			Matcher m = p.matcher(name); 
			if(m.find()){
				String str=m.group();
				str=str.replaceAll("\\d", "");
				str=str.replaceAll("[_|＿]", "");
				str=str.replaceAll("对公", "");
				name=str;
			}
			if(idf.getFixValuse(name)!=null){
				return idf.getFixValuse(name);
			}
		}
		return name;
	}
	
	public void ListToDb(List list,File file){
		if(list!=null && list.size()>0){
			tfile=file;
			for(int i=0;i<list.size();i++){
				List clist=(List)list.get(i);
				boolean flag=true;
				if(clist!=null){
					if(clist.size()<8){
						for(int j=clist.size();j<=8;j++){
							clist.add("");
						}
					}
					String id=(new RandomGUID()).toString();
					String dbsortName="";
					String name=((String)clist.get(0)).trim();
					name=name.replaceAll("[\r|\n]", "");
					
					tnum=i+2;
					ttitle=name;
					
					String docDBPath="";
					File serverFile=null;
					if(name==null || name.length()<=0){
						elist.add(genMgsMap("数据导入失败","“标题”数据为空。"));
						error++;
						continue;
					}
					
					dbsortName=getDBSort(name);
					
					//获取分类
					Map pmap=getSortFolder(dbsortName);
					if(pmap!=null){
						try{
							docDBPath=ZKMDocServlet.createZKMFolder(id, (String)pmap.get("id"));
						}catch(Exception x){
							elist.add(genMgsMap("数据导入失败","建立知识库分类路径失败，跳过。"));
							LogUtil.error(x, SKIP_Logger);
							error++;
							continue;
						}
					}else{
						continue;
					}
					//doc文档入附件
					File obj=getFileDoc(name);
					if(obj!=null){
						File f=(File)obj;
						serverFile=fileToDb(id,f);
						if(serverFile==null){
							continue;
						}
					}else{
						obj=(File)sameDoc.get(name);
						if(obj!=null){
							elist.add(genMgsMap("数据导入失败","数据所对应文档名称重复，跳过。"));
						}else{
							elist.add(genMgsMap("数据导入失败","未找到对应的文档，跳过。"));
						}
						error++;
						continue;
					}
					//将doc文档转换成html,即知识库正文。
					File hfile=null;
					if(serverFile!=null && serverFile.isFile() && serverFile.canRead() && docDBPath.length()>0){
						hfile=doc2Html(serverFile,docDBPath);
						if(hfile==null){
							continue;
						}
					}else{
						elist.add(genMgsMap("数据导入失败","对应的服务器文档或分类路径异常，跳过。"));
						error++;
						continue;
					}
					//生成数据库知识库记录
					flag=dataToDb(id,clist,pmap,docDBPath);
					if(!flag){
						continue;
					}
					doIndex(id);
					success++;
				}
			}
		}
	}
	
	public Map getSortFolder(String sortName){
		Map map=null;
		if(sortName!=null && sortName.length()>0){
			String [] sname=sortName.split("_/_");
			String sql="select * from `zkmInfoBase` ";
			String sw="where `recordType`='d' and text='" + sname[0] + "'";
			List list=null;
			try{
				list=DAOTools.queryMap(sql + sw, dbid);
				if(list!=null && list.size()>0){
					if(list.size()>1){
						sql="select * from  `zkmInfoBase` where 1=1 ";
						sw=" and ( ";
						if(sname.length>1 && sname[1].length()>0){
							for(int i=0;i<list.size();i++){
								Map<String,String> dm=(Map)list.get(i);
								if(dm!=null && dm.get("parentId") !=null && dm.get("parentId").length()>0){
									if(i+1<list.size()){
										sw+=" `id` = '"+ dm.get("parentId") +"' or ";
									}else{
										sw+=" `id` = '" + dm.get("parentId") + "'";
									}
								}
							}
							sw+=" )";
							list=DAOTools.queryMap(sql + sw, dbid);
							if(list!=null && list.size()>0){
								for(int i=0;i<list.size();i++){
									Map<String,String> dm=(Map)list.get(i);
									if(dm!=null && dm.get("text")!=null && dm.get("text").length()>0){
										if(sname[1].trim().equals(dm.get("text"))){
											map=dm;
											break;
										}
									}
								}
								if(map==null){
									error++;
									elist.add(genMgsMap("数据导入失败","数据对应分类非唯一(无父分类对应数据)，跳过。"));
								}
							}else{
								error++;
								elist.add(genMgsMap("数据导入失败","数据对应分类非唯一(未找到其对应父分类)，跳过。"));
							}
						}else{
							error++;
							elist.add(genMgsMap("数据导入失败",sortName + " 数据对应分类非唯一，跳过。"));
						}
					}else{
						map=(Map)list.get(0);
					}
				}else{
					elist.add(genMgsMap("数据导入失败","获取知识库分类“ "+ sname[0] +" ”失败，跳过。"));
					error++;
				}
			}catch(Exception x){
				error++;
				elist.add(genMgsMap("数据导入失败","数据数据库访问失败，跳过。"));
				LogUtil.error(x, SKIP_Logger);
			}
		}else{
			error++;
			elist.add(genMgsMap("数据导入失败","数据无法获得对应知识库的分类名称，跳过。"));
		}
		return map;
	}
	
	public boolean dataToDb(String id,String title,Map<String,String> pmap,String docDBPath){
		
		String fields="`id`,`text`,`recordType`,`parentId`,`title`,`codeNum`,`bDate`,`summary`,`keywords`,`keywords1`,`keywords2`,`keywords3`,`keywords4`,`keywords5`,`filePath`,`sortPath`,`versions`,`createUser`,`createTime`";
		String values="?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,now()";
		String [] p={id,pmap.get("text"),"f",pmap.get("id"),title,"","now()","","","","","","",docDBPath,"","0.01","importer"};
		String sql="insert into `zkmInfoBase` ("+ fields +") values ("+ values +")";
		try{
			DAOTools.updateForPrepared(sql, p, dbid);
		}catch(Exception x){
			error++;
			elist.add(genMgsMap("数据导入失败","数据入库操作异常，跳过。"));
			LogUtil.error(x, SKIP_Logger);
			return false;
		}
		return true;
	}
	
	public boolean dataToDb(String id,List<String> clist,Map<String,String> pmap,String docDBPath){
		
		String fields="`id`,`text`,`recordType`,`parentId`,`title`,`codeNum`,`bDate`,`summary`,`keywords`,`keywords1`,`keywords2`,`keywords3`,`keywords4`,`keywords5`,`filePath`,`sortPath`,`versions`,`createUser`,`createTime`";
		String values="?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?,now()";
		String [] p={id,pmap.get("text"),"f",pmap.get("id"),clist.get(0),"",clist.get(1),clist.get(2),clist.get(3),clist.get(4),clist.get(5),clist.get(6),clist.get(7),docDBPath,"","0.01","importer"};
		String sql="insert into `zkmInfoBase` ("+ fields +") values ("+ values +")";
		try{
			DAOTools.updateForPrepared(sql, p, dbid);
		}catch(Exception x){
			error++;
			elist.add(genMgsMap("数据导入失败","数据入库操作异常，跳过。"));
			LogUtil.error(x, SKIP_Logger);
			return false;
		}
		return true;
	}
	
	public boolean doIndex(String id){
		try{
			ZKMDocServlet.indexDocs(id, false);
		}catch(Exception x){
			error++;
			elist.add(genMgsMap("数据导入失败","建立全文检索异常。"));
			LogUtil.error(x, SKIP_Logger);
			return false;
		}
		return true;
	}
	
	public File fileToDb(String infoId,File docfile){

		String rootpath=ZKMConfs.confs.getProperty("zkmUploadFileSaveDir","/usr/local/nginx/html/ZDesk/uploads");
		String fileServerName="";
		rootpath = rootpath + "/resourses/";
		String dataFolder=FileUtils.getDateFolderStr();
		File savePath=new File(rootpath + "/" +  dataFolder);
		if(!savePath.exists()){
			savePath.mkdirs();
			ZKMAPPUtils.fcdirChangeUserAndGroup(savePath);
		}
		String name = docfile.getName();
		String extName="";
		File sf=new File(savePath.getPath() + "/" + name);
		try{
			org.apache.commons.io.FileUtils.copyFile(docfile, sf);
			ZKMAPPUtils.fileChangeUserAndGroup(sf);
		}catch(Exception x){
			error++;
			elist.add(genMgsMap("数据导入失败","所对应的文档入库操作失败，跳过。"));
			LogUtil.error(x, SKIP_Logger);
			return null;
		}
		if (name == null || name.trim().equals("")) {
			error++;
			elist.add(genMgsMap("数据导入失败","所对应的文档文件名获取失败，跳过。"));
			return null;
		}
		
		if(name.lastIndexOf("\\")>-1){
			name=name.substring(name.lastIndexOf("\\")+1,name.length());
		}
		if(name.lastIndexOf("/")>-1){
			name=name.substring(name.lastIndexOf("/")+1,name.length());
		}
		if (name.lastIndexOf(".") >= 0) {
			extName = name.substring(name.lastIndexOf("."));
		}
		name=name.substring(0, name.lastIndexOf("."));
		String fileId=(new RandomGUID()).toString();
		String saveName=fileId;
		File saveFile = new File(savePath.getPath() +"/" +  saveName + extName);
		fileServerName=saveName + extName;
		sf.renameTo(saveFile);
		
		String user="importer";
		String fileUse="zkm_fujian";
		String sortId="";
		String fileName=fileServerName;
		String relName=name + extName;
		String fileType=extName;
		String filePath=savePath.getPath();
		
		String fielStr="`id`,`fileName`,`relName`,`fileType`,`filePath`,`createTime`,`createUser`,`isdel`,`relationId`,`fileUse`,`relationSort`";
		String vstr="?,?,?,?,?,now(),?,?,?,?,?";
		String[] param={fileId,fileName,relName,fileType,filePath,user,"0",infoId,fileUse,sortId};
		String sql="insert into `zkmInfoFile` (" + fielStr + ") values ( "+ vstr +")";
		try{
			DAOTools.updateForPrepared(sql, param,dbid);
		}catch(Exception x){
			error++;
			elist.add(genMgsMap("数据导入失败","所对应的文档操作数据库失败，跳过。"));
			LogUtil.error(x, SKIP_Logger);
			return null;
		}
		return saveFile;
	}
	
	public File doc2Html(File docfile,String dbpath){
		File hfile=null;
		String rootPath=ZKMConfs.confs.getProperty("zkmDocSaveDir", "/mnt/zkmDoc");
		File dfile=new File(rootPath + dbpath);
		if(dfile.exists() && dfile.isDirectory()){
			try{
				hfile=OfficeConvert.convert(docfile, dfile.getPath());
			}catch(Exception x){
				error++;
				elist.add(genMgsMap("数据导入失败","所对应的文档html转换异常，跳过。"));
				LogUtil.error(x, SKIP_Logger);
				return null;
			}
		}else{
			error++;
			elist.add(genMgsMap("数据导入失败", "所对应的文档服务器路径异常，跳过。"));
			return null;
		}
		return hfile;
	}
	
	public File getFileDoc(String name){
		File file=null;
		file=(File)docMap.get(name);
		if(file==null){
			for(int i=0;i<doclist.size();i++){
				File f=(File)doclist.get(i);
				if(f!=null && f.exists() && f.isFile() && f.canRead()){
					String fn=f.getName();
					if(fn.indexOf(name)>=0){
						file=f;
						break;
					}else if(name.indexOf("_")>=0){
						name=name.replaceAll("_", "＿");
						if(fn.indexOf(name)>=0){
							file=f;
							break;
						}
					}else if(name.indexOf("＿")>=0){
						name=name.replaceAll("＿", "_");
						if(fn.indexOf(name)>=0){
							file=f;
							break;
						}
					}
				}
			}
		}
		return file;
	}
	
	public static void delFolders(File file){
		if(file!=null && file.exists() && file.isDirectory()){
			File[] fs=file.listFiles();
			if(fs!=null && fs.length>0){
				for(File f:fs){
					if(f!=null && f.exists() && f.isDirectory()){
						System.out.println("--- disponse :" + f.getPath());
						if(".svn".equals(f.getName())){
							try{
								org.apache.commons.io.FileUtils.deleteDirectory(f);
							}catch(Exception x){
								System.out.println(f.getPath());
								x.printStackTrace();
							}
							LogUtil.info("11111111111111", SKIP_Logger);
							System.out.println("------: delete .");
						}else{
							delFolders(f);
						}
					}
				}
			}
		}
	}
	
	public static void fixHtmlFile(File file)throws Exception{
		if(file.exists() && file.isFile()){
			
			String data=FileUtils.getFileRealContent(file);
			data=ZKMDocHelper.clearWordFormat(data);
			data=ZKMDocHelper.ZkmDocFix(data,null);
			FileUtils.appendToFile(data, file, false);
		}
	}
	
	public static void fixHtmlFileForFolders(String folder)throws Exception{
		File file=new File(folder);
		if(file.exists() && file.isDirectory()){
			checkChildFolder(file);
		}
	}
	
	public static void checkChildFolder(File file) throws Exception{
		File[] fs=file.listFiles();
		for(File f:fs){
			if(f.isDirectory()){
				checkChildFolder(f);
			}else if(f.isFile() && f.getName().equals("index.html")){
				fixHtmlFile(f);
			}
		}
	}
	
	public static void testGetFile(){
		String title="总中心_礼仪存单折";
		File savePath=new File("E:/testImp/impDoc/总中心_礼仪存单折");
		File [] cfs=savePath.listFiles();
		File contentFile=null;
		for(File cf:cfs){
			if(cf.isFile()&& cf.canRead()){
				String fn=cf.getName();
				if(fn.toUpperCase().indexOf("INDEX")>=0){
					contentFile=cf;
				}else if(fn.indexOf(title)>=0){
					contentFile=cf;
				}
				if(contentFile!=null){
					String enn=FileUtils.getFileExtendName(cf);
					if(enn.toUpperCase().equals(".HTML")){
						contentFile=cf;
						break;
					}else{
						contentFile=null;
					}
				}
			}
		}
	}
	
	public static void main(String [] args) throws Exception {
		//testGetFile();
		/*ZkmFileImport zfi=new ZkmFileImport();
		zfi.imp2Database();
		List list=zfi.elist;
		System.out.println(list.size());
		*/
		/*String name="总中心_对公长城“万事顺”国际电子借记卡20120521";
		String rule="[_|＿].+\\d?";
		Pattern p = Pattern.compile(rule);
		Matcher m = p.matcher(name); 
		if(m.find()){
			String str=m.group();
			str=str.replaceAll("\\d", "");
			str=str.replaceAll("[[_|＿]]", "");
			str=str.replaceAll("对公", "");
			System.out.println(str);
		}*/
		/*String name="业务类公告_/_公告";
		String[] k=name.split("_/_");
		System.out.print(k[0] + "  " + k[1]);*/
		//ZkmFileImport zfi=new ZkmFileImport();
		/*String dfolder=ZKMConfs.confs.getProperty("docFolder");
		zfi.getChildFolderFiles(new File(dfolder));
		String name="“中国银行.益农贷”个人涉农贷款业务管理办法（2011年版）";
		File file=zfi.getFileDoc(name);
		System.out.println(file.getPath());*/
		
		/*ZkmFileImport zfi=new ZkmFileImport();
		zfi.imp2Database();
		Map rmap=new HashMap();
		rmap.put("total", zfi.total);
		rmap.put("success", zfi.success);
		rmap.put("error", zfi.error);
		rmap.put("xlsnum", zfi.xlsnum);
		rmap.put("docnum", zfi.docnum);
		rmap.put("elist", zfi.elist);
		rmap.put("slist", zfi.sameList);
		System.out.println(JSONSerializer.toJSON(rmap).toString());*/
		//File file=new File("E:/desktopFolder/tmpNow/matchZDesk/2");
		//File file=new File("E:/desktopFolder/tmpNow/matchZDesk/2");
		File file=new File("E:/desktopFolder/tmpNow/source/ZDesk");
		
		delFolders(file);
		
		//file=new File("E:/desktopFolder/tmpNow/matchZDesk/2");
		//delFolders(file);
		/*float vf=Float.parseFloat("0.01");
		for(int i=0;i<10;i++){
			vf=vf+Float.parseFloat("0.01");
			java.text.DecimalFormat df = new java.text.DecimalFormat("#0.##");
			System.out.println(df.format(vf));
		}*/
		/*ZkmFileImport zfi=new ZkmFileImport();
		zfi.imp2Database();
		File file=new File("e:/aaa.log");
		OutputStream out=new FileOutputStream(file);
		OutputStreamWriter fw=new OutputStreamWriter(out,Charset.forName("utf-8"));
		for(int i=0;i<zfi.elist.size();i++){
			Map map=(Map)zfi.elist.get(i);
			fw.write(map.get("file") + "  " + map.get("mgs") + "\n");
			fw.flush();
		}
		fw.write(zfi.total + "  " + zfi.success + " " + zfi.error);
		fw.flush();
		fw.close();*/
		
		/*String uri="E:/file/workDir/workBase/81.222/zkmDocs/DCC986CC-E56F-4E11-AE97-2CCC7B6BBAEF/B2BDC934-F3ED-870F-3387-7AB8829F6572/435FA117-5152-A181-F6B6-02197600D379/C4708AB9-81B8-DB89-AA14-E5B64E5388F9/index.html";
		
		File file=new File(uri);
		
		ZkmFileImport.fixHtmlFile(file);
		*/
		
		//fixHtmlFileForFolders("E:/file/workDir/workBase/81.222/zkmDocs");
	}
}
