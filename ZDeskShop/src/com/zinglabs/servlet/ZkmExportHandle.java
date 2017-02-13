package com.zinglabs.servlet;

import org.htmlparser.*;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.tags.*;
import org.htmlparser.util.DefaultParserFeedback;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;
import com.zinglabs.servlet.ZKMDocServlet;
import com.zinglabs.servlet.HtmlTagExtends.EmbedTag;
import com.zinglabs.servlet.HtmlTagExtends.IframeTag;
import com.zinglabs.servlet.modules.ZkmCommons;
import com.zinglabs.servlet.zkmDocTemplate.ZKMDocHelper;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.FileUtils;
import com.zinglabs.util.RandomGUID;
import com.zinglabs.util.ZKMAPPUtils;
import com.zinglabs.util.ZKMConfs;


public class ZkmExportHandle {
	public static  Logger logger = LoggerFactory.getLogger("ZKM"); 
	public static String zkmFlag=ZKMConfs.confs.getProperty("ZKM_EXPORT_DOC_FLAG","/zkmDocs");
	public static String fileFlag=ZKMConfs.confs.getProperty("ZKM_EXPORT_FILE_FLAG","/uploads");
	public static String fixFlag=ZKMConfs.confs.getProperty("ZKM_EXPORT_FIX_FLAG","/ZDesk");
	public static String folderRule="^[A-Z0-9]+-([A-Z0-9]{4}-){3}[A-Z0-9]+";
	
	public static void exportDoc(String id,File dfile,String type) throws Exception{
		if(id!=null && id.length()>0){
			String field=" `id`,`title`,`contentPath` ";
			String sql="";
			List plist=new ArrayList();
			if("F".equals(type)){
				sql="select "+ field +" from zkmInfoBaseDisponseFB where id=? ";
				plist.add(id);
			}else if("D".equals(type)){
				sql="select zibdfb.* from `DataRelationComponent` drc,`zkmInfoBaseDisponseFB` zibdfb where drc.`srcId`=zibdfb.`id` and drc.`distId`=? and drc.`rType`=? ";
				plist.add(id);
				plist.add(ZkmCommons.ZKM_FB_RLEATION_KEY);
			}
			if(sql.length()>0){
				List list=DAOTools.queryMap(sql, plist, ConfInfo.confMapDBConf.get("securityDBId"));
				if("F".equals(type)){
					if(list!=null && list.size()>0){
						Map<String,String> qm=(Map<String,String>)list.get(0);
						exportDocF(qm,dfile);
					}
				}else if("D".equals(type)){
					if(list!=null && list.size()>0){
						exportDocD(list,dfile);
					}
				}
			}
		}
	}
	
	public static void exportDocD(List list,File dfile) throws Exception{
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String,String> qmf=(Map<String,String>)list.get(i);
				exportDocF(qmf,dfile);
			}
		}
	}
	
	public static void exportDocF(Map<String,String> qm,File dfile) throws Exception{
		String id=qm.get("id");
		String title=qm.get("title")==null?"":qm.get("title");
		String fpath=qm.get("contentPath")==null?"":qm.get("contentPath");
		if(fpath!=null && fpath.length()>0){
			File sfile=new File(fpath);
			sfile=sfile.getParentFile();
			if(sfile.exists() && sfile.isDirectory()){
				File dist=new File(dfile + "/" + sfile.getName());
				File rtf=new File(dfile.getPath() +"/" + title + "_" +dist.getName());
				org.apache.commons.io.FileUtils.copyDirectory(sfile, rtf);
				//TODO:正文不需要处理
				//spellExpFile(id,rtf);
			}
		}
	}
	
	
	public static HashMap exportDoc(String[] ids,String type) throws Exception{
		HashMap reMap=new HashMap();
		if(ids!=null && ids.length>0){
			RandomGUID rg=new RandomGUID();
			String folder=rg.toString();
			String tempDir=ZKMConfs.confs.getProperty("mergeTempDir","/mnt/mergeTempDir");
			File expRoot=FileUtils.createFolderUseSystemDate(tempDir);
			File dfile=new File(expRoot.getPath() + "/" + folder);
			if(dfile.exists() && dfile.isDirectory()){
				dfile.delete();
			}
			dfile.mkdirs();
			for(String id:ids){
				exportDoc(id,dfile,type);
			}
			String name="export_";
			String zipTrueName=new RandomGUID().toString() + ".zip";
			String zipFilePath=expRoot.getPath() + "/" + zipTrueName;
			File reFile=new File(zipFilePath);
			reFile=FileUtils.zip(reFile, dfile);
			ZKMAPPUtils.fcdirChangeUserAndGroup(reFile);
			try{
				org.apache.commons.io.FileUtils.deleteDirectory(dfile);
				if(dfile.exists()){
					ZKMAPPUtils.dirChangeUserAndGroup(dfile, true);
				}
			}catch(Exception x){
				LogUtil.error(x, logger);
			}
			String date=DateUtil.getLocalDefineDate("yyyy-MM-dd");
			String zipFileName=name + date + ".zip";
			reMap.put("expName", zipFileName);
			reMap.put("expFile", reFile.getPath());
			LogUtil.debug(" -------------- exp Zip file : " + reFile.getPath(), logger);
			//System.out.println(reFile.getPath());
		}
		return reMap;
	}
	
	public static void spellChilds(File file){
		if(file!=null && file.exists() && file.isDirectory()){
			String id=file.getName();
			try{
				spellExpFile(id,file);
			}catch(Exception x){
				LogUtil.error(x, logger);
				logger.debug("export ZKM FOLDER : " + id + " -- final.");
			}
			File[] files=file.listFiles();
			if(files!=null && files.length>0){
				Pattern p = Pattern.compile(folderRule);
				Matcher matcher = p.matcher(id);
				if(matcher.find()){
					for(File cf:files){
						if(cf.isDirectory()){
							spellChilds(cf);
						}
					}
				}
			}
		}
	}
	
	public static void spellExpFile(String id,File file) throws Exception{
		File indexFile=null;
		for(String name:ZKMDocServlet.docFileName){
			indexFile=new File(file.getPath() + "/" + name);
			if(indexFile.exists() && indexFile.isFile()){
				break;
			}
		}
		if(indexFile!=null && indexFile.exists() && indexFile.isFile()){
			File resourceFolder=getResourceFolder(file.getPath());
			String html=FileUtils.getFileRealContent(indexFile);
			
			html=fixHtml(id,html,resourceFolder);
			if(ZKMDocHelper.zkmDocGen!=null){
				html=ZKMDocHelper.zkmDocGen.getZkmContent(html);
			}
			FileUtils.appendToFile(html, indexFile, false);
		}
	}
	
	public static String copyResourceFile(String id,String url,File resourceFolder)throws Exception{
		String rePath="";
		String resourceFolderName=ZKMConfs.confs.getProperty("ZKM_EXPORT_RESOURCE_FOLDER","resource");
		String upFilePath=ZKMConfs.confs.getProperty("ZKM_EXPORT_UP_FILE_USE_PATH","");
		String zkmFilePath=ZKMConfs.confs.getProperty("ZKM_EXPORT_ZKM_DOC_USE_PATH","");
		String ruleUrl="^[hH][tT]{2}[pP]://([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]+:{0,1}[0-9]{0,4}";
		Pattern p = Pattern.compile(ruleUrl);
		Matcher matcher = p.matcher(url);
		if(matcher.find()){
			url=url.replaceAll(ruleUrl, "");
		}
		if(url.indexOf(fileFlag)>0 && upFilePath.length()>0){
			String file=upFilePath + url;
			File sfile=new File(file);
			if(sfile.exists() && sfile.isFile()){
				String df=resourceFolder.getPath() + "/" + sfile.getName();
				File dfile=new File(df);
				org.apache.commons.io.FileUtils.copyFile(sfile, dfile);
				if(dfile.exists() && dfile.isFile()){
					rePath=resourceFolderName + "/" +dfile.getName();
				}
			}
		}else if(url.indexOf(zkmFlag)>0 && zkmFilePath.length()>0){
			String file=zkmFilePath + url;
			File sfile=new File(file);
			if(sfile.exists() && sfile.isFile()){
				String df=resourceFolder.getPath() + "/" + sfile.getName();
				File dfile=new File(df);
				org.apache.commons.io.FileUtils.copyFile(sfile, dfile);
				if(dfile.exists() && dfile.isFile()){
					rePath=resourceFolderName + "/" +dfile.getName();
				}
			}
		}else {
			String idRuler=".+[A-Z0-9]+-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]+.+";
			p = Pattern.compile(ruleUrl);
			matcher = p.matcher(url);
			if(matcher.find()){
				url=url.substring(url.indexOf(id) + 37,url.length());
			}
			rePath=url;
		}
		return rePath;
	}
	
	
	public static String fixHtml(String id,String html,File resourceFolder) throws Exception{
		//html="<html><body>" + html;
		PrototypicalNodeFactory pnf=new PrototypicalNodeFactory ();
		pnf.registerTag(new EmbedTag());
		pnf.registerTag(new IframeTag());
		Parser parser =createParser(html);
		parser.setNodeFactory(pnf);
		parser.setEncoding("UTF-8");
		
		HtmlPage page=new HtmlPage(parser); 
		
		parser.visitAllNodesWith(page);
		
		NodeList list=page.getBody();
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				processTags(tag,id,resourceFolder);
			}
			return list.toHtml();
		}else{
			return "";
		}
		/*
		NodeFilter[] nf={new NodeClassFilter(EmbedTag.class),new NodeClassFilter(LinkTag.class),new NodeClassFilter(ImageTag.class)};
		OrFilter linkFilter = new OrFilter();
		linkFilter.setPredicates(nf);
		NodeList list = parser.extractAllNodesThatMatch(linkFilter);
		for (int i = 0; i < list.size(); i++) {
			Node tag = list.elementAt(i);
			disposeTags(tag,id,resourceFolder);
		}
		*/
		
		
	}
	
	
	private static void processTags(Node tag,String id,File resourceFolder) throws Exception{
		disposeTags(tag,id,resourceFolder);
		NodeList list=tag.getChildren();
		if(list!=null && list.size()>0){
			for(int i = 0; i < list.size(); i++){
				Node ct = list.elementAt(i);
				processTags(ct,id,resourceFolder);
			}
		}
	}
	
	public static void disposeTags(Node tag,String id,File resourceFolder) throws Exception{
		//logger.info("dispose tag : " + tag.getText());
		if (tag instanceof LinkTag)
		{
			LinkTag link = (LinkTag) tag;
			String linkUrl = link.getLink();
			if(linkUrl!=null && linkUrl.length()>0 && linkUrl.indexOf(fixFlag)>=0){
				String path=copyResourceFile(id,linkUrl,resourceFolder);
				link.setLink(path);
			}
		}
		else if (tag instanceof ImageTag)
		{
			ImageTag image = (ImageTag) tag;
			String src=image.getImageURL();
			if(src!=null && src.length()>0 && src.indexOf(fixFlag)>=0){
				String path=copyResourceFile(id,src,resourceFolder);
				image.setImageURL(path);
				Object ob=image.getParent();
			}
		}else if(tag instanceof EmbedTag){
			EmbedTag et=(EmbedTag) tag;
			String src=et.getSrc();
			if(src!=null && src.length()>0 && src.indexOf(fixFlag)>=0){
				//flv文件处理
				String rule="(.+flvplayer.swf)\\?vcastr_file=(.+\\.flv)$";
				Pattern pattern=Pattern.compile(rule);
				Matcher mat=pattern.matcher(src);
				if(mat.find(0)){
					//copy flvplayer to
					String zkmFilePath=ZKMConfs.confs.getProperty("ZKM_EXPORT_ZKM_DOC_USE_PATH","");
					String resourceFolderName=ZKMConfs.confs.getProperty("ZKM_EXPORT_RESOURCE_FOLDER","resource");
					String playerFile=zkmFilePath + "/ZDesk/js/flvPlayer/flvplayer.swf";
					String copTo=resourceFolder.getParent() + "/flvplayer.swf";
					org.apache.commons.io.FileUtils.copyFile(new File(playerFile), new File(copTo));
					String file1=mat.group(1);
					String file2=mat.group(2);
					file1=file1.substring(file1.lastIndexOf("/") +1);
					file2=copyResourceFile(id,file2,resourceFolder);
					String flvPath= file1 + "?vcastr_file=" + file2;
					logger.info("----- replace flv path : " + flvPath);
					et.setSrc(flvPath);
				}else{
					String path=copyResourceFile(id,src,resourceFolder);
					et.setSrc(path);
				}
			}
		}
	}
	
	public static File getResourceFolder(String filePath){
		String name=ZKMConfs.confs.getProperty("ZKM_EXPORT_RESOURCE_FOLDER","resource");
		File file=new File(filePath + "/" + name);
		if(file.exists() && file.isDirectory()){
			file.delete();
		}
		file.mkdirs();
		return file;
	}
	
	public static File getAttachmentFolder(String filePath){
		String name=ZKMConfs.confs.getProperty("ZKM_EXPORT_ATTACHMENT_FOLDER","attachmentFile");
		File file=new File(filePath + "/" + name);
		if(file.exists() && file.isDirectory()){
			file.delete();
		}
		file.mkdirs();
		return file;
	}
	
	public static Parser createParser(String inputHTML) {
        Lexer mLexer = new Lexer(new Page(inputHTML));
        return new Parser(mLexer,new DefaultParserFeedback(DefaultParserFeedback.QUIET));
    }
	
	public static void main(String[] args) throws Exception{
		/*PrototypicalNodeFactory pnf=new PrototypicalNodeFactory ();
		pnf.registerTag(new EmbedTag());
		pnf.registerTag(new IframeTag());
		String html="我是一个<a href=\"http://127.0.0.1:8888/ZDesk/zkmDocs/3AF0BEB7-B2C7-0620-A7B9-4DF8BE0BF5D0/8C033D88-CD49-4F9E-00A4-3AEDF0C3DE0A/EC82910F-7B59-EAB5-DB05-9D95BD336735/index_2.html\">中国人</a><br /><img height=\"100\" alt=\"\" src=\"http://127.0.0.1:8888/ZDesk/zkmDocs/3AF0BEB7-B2C7-0620-A7B9-4DF8BE0BF5D0/8C033D88-CD49-4F9E-00A4-3AEDF0C3DE0A/EC82910F-7B59-EAB5-DB05-9D95BD336735/a.jpg\" width=\"100\" /> <embed src=\"http://127.0.0.1:8888/ZDesk/zkmDocs/3AF0BEB7-B2C7-0620-A7B9-4DF8BE0BF5D0/8C033D88-CD49-4F9E-00A4-3AEDF0C3DE0A/EC82910F-7B59-EAB5-DB05-9D95BD336735/n1319187455326.swf\" width=\"480\" height=\"400\" type=\"application/x-shockwave-flash\" allowfullscreen=\"true\" loop=\"true\" play=\"true\" menu=\"false\" quality=\"high\" wmode=\"opaque\" classid=\"clsid:d27cdb6e-ae6d-11cf-96b8-4445535400000\" /><img height=\"170\" src=\"/ZDesk/uploads/resourses/2011/12/06/CB4A4CA6-D029-BD57-4E05-36E99AA2F917.jpg\" width=\"256\" alt=\"\" />";

		Parser parser = createParser(html);
		parser.setNodeFactory(pnf);
		
		parser.setEncoding("UTF-8");
		
		HtmlPage page=new HtmlPage(parser);
		
		parser.visitAllNodesWith(page);
		
		NodeList list= page.getBody();
		
		logger.info("list size : " + list.size());
		
		//NodeFilter[] nf={new NodeClassFilter(IframeTag.class),new NodeClassFilter(EmbedTag.class),new NodeClassFilter(ObjectTag.class),new NodeClassFilter(LinkTag.class),new NodeClassFilter(ImageTag.class)};
		
		//OrFilter linkFilter = new OrFilter();
		
		//linkFilter.setPredicates(nf);
		
		
		//NodeList list = parser.extractAllNodesThatMatch(linkFilter);
		System.out.println(list.toHtml());
		for (int i = 0; i < list.size(); i++) {
			Node tag = list.elementAt(i);
			if (tag instanceof LinkTag)//<a> 标签 
			{
				LinkTag link = (LinkTag) tag;
				String linkUrl = link.getLink();//url
				String text = link.getLinkText();//链接文字
				link.setLink("yyyyyyyyyyyy");
			}
			else if (tag instanceof ImageTag)//<img> 标签
			{
				ImageTag image = (ImageTag) list.elementAt(i);
				image.setImageURL("hhhhhhhhhhhhh");
			}
			else if (tag instanceof ObjectTag)//<object> 标签
			{
				ObjectTag ob = (ObjectTag) list.elementAt(i);
			}else if(tag instanceof EmbedTag){
				EmbedTag et=(EmbedTag) list.elementAt(i);
				et.setSrc("xxxxxxxx");
			}else if(tag instanceof IframeTag){
				IframeTag et=(IframeTag) list.elementAt(i);
				et.setSrc("vvvvvvvvv");
			}
		}
		System.out.println(list.toHtml());*/
		//String a="abc";
		//System.out.println(a.indexOf("b"));
		
		ConfInfo cf=new ConfInfo();
		DAOTools dts=new DAOTools();
		ZKMConfs lb=new ZKMConfs();
		String[] id={"529B80EA-909C-CA38-3058-40C8ECE84B70"};
		ZkmExportHandle.exportDoc(id,"D");
		
		/*String str="http://129.eee.129.129:8888/sfewf/345";
		String rule="^[hH][tT]{2}[pP]://([a-zA-Z0-9]+\.)+[a-zA-Z0-9]+:{0,1}[0-9]{0,4}/";
		System.out.println(str.replaceAll(rule, ""));*/
		
		/*String str="/ZDesk/flvplayer.swf?vcastr_file=movie.flvxxx";
		String rule="(.+flvplayer.swf)\\?vcastr_file=(.+\\.flv)$";
		Pattern pattern=Pattern.compile(rule);
		Matcher mat=pattern.matcher(str);
		if(mat.find(0)){
			System.out.println(mat.group(1));
			System.out.println(mat.group(2));
		}else{
			System.out.println("no matched.");
		}*/
	}
}
