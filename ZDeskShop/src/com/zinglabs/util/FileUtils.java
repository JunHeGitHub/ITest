package com.zinglabs.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONSerializer;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.WriteOutContentHandler;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.zinglabs.db.ConfInfo;
import com.zinglabs.log.LogUtil;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

public class FileUtils {
	/**
	 * 文件数据内容
	 */
	public static final String FILE_CONTENT = "FILE_CONTENT";
	/**
	 * 文件数据元
	 */
	public static final String FILE_METADATA = "FILE_METADATA";
	/**
	 * 文件数据元，文件类型
	 */
	public static final String FILE_MIMETYPE_CONTENT_TYPE = Metadata.CONTENT_TYPE;

	public static final String FILE_MIMETYPE_TEXT = "text/plain";

	public static final String FILE_MIMETYPE_OFFICE_DOC = "application/msword";

	public static final String FILE_MIMETYPE_OFFICE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

	public static final String FILE_MIMETYPE_OFFICE_XLS = "application/vnd.ms-excel";

	public static final String FILE_MIMETYPE_OFFICE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public static final String FILE_MIMETYPE_IMAGE_JPG = "image/jpeg";

	public static final String FILE_MIMETYPE_IMAGE_PNG = "image/png";

	public static final String FILE_MIMETYPE_IMAGE_GIF = "image/gif";

	public static final String FILE_MIMETYPE_VIDO_AVI = "video/x-msvideo";

	public static final String FILE_MIMETYPE_HTML = "text/html";
	
	public static final String FILE_MIMETYPE_RAR="application/x-rar-compressed";
	
	public static final String FILE_MIMETYPE_ZIP="application/zip";
	
	public static int FILE_DOCUMENT_MAX_STRING_LENGTH=1000000;

	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");

	/**
	 * 取得文件夹或文件大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public static String getFileExtendName(File f) {
		if (f.exists() && f.isFile()) {
			String name = f.getName();
			String extName = "";
			if (name.lastIndexOf(".") >= 0) {
				extName = name.substring(name.lastIndexOf("."));
			}
			return extName;
		} else {
			return "";
		}
	}
	
	public static  String getThreadSingFileContent(File file) throws Exception {
		return getFileContent(file);
	}

	public static  String getFileContent(String file) throws Exception {
		if (null != file && file.length() > 0) {
			File f = new File(file);
			return getFileContent(f);
		}
		return "";
	}

	public static  String getFileContent(File file) throws Exception {
		String content = "";
		if (file != null && file.exists() && file.isFile() && file.canRead()) {
			content = parseToString(file);
		}
		return content;
	}
	
	public static String getFileRealContent(String file) throws Exception{
		if (null != file && file.length() > 0) {
			File f = new File(file);
			return getFileRealContent(f);
		}
		return "";
	}

	public static String getFileRealContent(File file) throws Exception{
		if(file!=null && file.exists() && file.isFile() && file.canRead()){
			String charset=getFileCharset(file);
			return getFileRealContent(file,charset);
		}
		return "";
	}
	
	public static String getFileRealContent(File file,String charset) throws Exception{
		if(file!=null && file.exists() && file.isFile() && file.canRead()){
			return org.apache.commons.io.FileUtils.readFileToString(file,charset);
		}
		return "";
	}
	
	public static String getDateFolderStr() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		if (month.length() < 2) {
			month = "0" + month;
		}
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		if (day.length() < 2) {
			day = "0" + day;
		}
		return year + "/" + month + "/" + day;
	}

	public static File createFolderUseSystemDate(String rootPath) {
		File fp = null;
		if (rootPath != null && !"".equals(rootPath)) {
			Calendar c = Calendar.getInstance();
			String year = String.valueOf(c.get(Calendar.YEAR));
			String month = String.valueOf(c.get(Calendar.MONTH) + 1);
			if (month.length() < 2) {
				month = "0" + month;
			}
			String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
			if (day.length() < 2) {
				day = "0" + day;
			}
			/*if (rootPath.lastIndexOf("/") > -1) {
				rootPath = rootPath.substring(0, rootPath.lastIndexOf("/"));
			} else if (rootPath.lastIndexOf("\\") > -1) {
				rootPath = rootPath.substring(0, rootPath.lastIndexOf("\\"));
			}*/
			fp = new File(rootPath + "/" + year + "/" + month + "/" + day);
			if (!fp.exists()) {
				fp.mkdirs();
			}
		}
		return fp;
	}

	public static synchronized File createFolder(String path,boolean reCreate) throws Exception{
		File f=new File(path);
		if(f.exists() && f.isDirectory() && reCreate){
			f.delete();
			f.mkdirs();
		}else if(!f.exists() || !f.isDirectory()){
			f.mkdirs();
		}
		return f;
	}
	
	/**
	 * 获取数据元与数据的内容
	 * 
	 * @param file
	 * @return
	 */
	public static Map parserFile(String file) throws Exception {
		if (file != null && file.length() > 0) {
			File f = new File(file);
			return parserFile(f);
		}
		return null;
	}

	/**
	 * 获取数据元与数据的内容
	 * 
	 * @param file
	 * @return
	 */
	public static Map parserFile(File file) throws Exception {
		return parserFile(file,FILE_DOCUMENT_MAX_STRING_LENGTH);
	}

	
	public static Map parserFile(File file,int maxStringLength) throws Exception {
		Map rm = null;
		if (file != null && file.exists() && file.isFile() && file.canRead()) {
			rm = new HashMap();
			WriteOutContentHandler handler = new WriteOutContentHandler(maxStringLength);
			ContentHandler contenthandler = new BodyContentHandler(handler);
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
			Parser parser = new AutoDetectParser();
			InputStream is = new FileInputStream(file);
			parser.parse(is, contenthandler, metadata);
			Map tm = null;
			if (metadata != null) {
				tm = new HashMap();
				String[] ns = metadata.names();
				for (int i = 0; i < ns.length; i++) {
					String n = ns[i];
					LogUtil.info("-- : " + n + " : " + metadata.get(n),SKIP_Logger);
					tm.put(n, metadata.get(n));
				}
				rm.put(FILE_METADATA, tm);
			}
			if (contenthandler != null) {
				// TODO：这里提取的内容是无格式的纯文本内容。
				String s = contenthandler.toString();
				//LogUtil.info("--- parse : " +  s);
				LogUtil.info("---- content parse : " + s.length(),SKIP_Logger);
				rm.put(FILE_CONTENT, s);
			}
			is.close();
		}
		return rm;
	}
	
	public static String parseToString(File file) throws IOException, TikaException {
		return parseToString(file,FILE_DOCUMENT_MAX_STRING_LENGTH);
	}
	
	public static String parseToString(File file,int maxStringLength) throws IOException, TikaException {
		Metadata metadata = new Metadata();
		InputStream is=new FileInputStream(file);
		String str=parseToString(is,metadata,maxStringLength);
		return str;
	}
	
	public static String parseToString(InputStream stream, Metadata metadata, int maxStringLength) throws IOException, TikaException {
		WriteOutContentHandler handler = new WriteOutContentHandler(maxStringLength);
		try {
		    ParseContext context = new ParseContext();
		    Parser parser = new AutoDetectParser();
		    context.set(Parser.class, parser);
		    parser.parse( stream, new BodyContentHandler(handler), metadata, context);
		} catch (SAXException e) {
		    if (!handler.isWriteLimitReached(e)) {
		        // This should never happen with BodyContentHandler...
		        throw new TikaException("Unexpected SAX processing failure", e);
		    }
		} finally {
		    stream.close();
		}
		return handler.toString();
		}
	
	/**
	 * 转换HTML格式字符串，提取其中正文内容
	 * 
	 * @param htmlStr
	 *            HTML格式文本
	 * @return
	 * @throws Exception
	 */
	public static String getHtmlStr2Text(String htmlStr) throws Exception {
		return getHtmlStr2Text(htmlStr,FILE_DOCUMENT_MAX_STRING_LENGTH);
	}
	
	public static String getHtmlStr2Text(String htmlStr,int maxStringLength) throws Exception {
		String restr = "";
		if (htmlStr != null && htmlStr.length() > 0) {
			WriteOutContentHandler handler = new WriteOutContentHandler(maxStringLength);
			ContentHandler contenthandler = new BodyContentHandler(handler);
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, "strKey");
			metadata.set(Metadata.CONTENT_TYPE, "text/html; charset=utf-8");
			InputStream in = new java.io.ByteArrayInputStream(htmlStr.getBytes());
			Parser parser = new AutoDetectParser();
			parser.parse(in, contenthandler, metadata);
			restr = contenthandler.toString();
		}
		return restr;
	}
	
	public static void appendToFile(File source, File dist,boolean isAppend) throws Exception{
		if (source != null && source.exists() && source.isFile() && source.canRead() && dist!=null) {
			try{
				String cont=getFileRealContent(source);
				appendToFile(cont,dist,isAppend);
			}catch(Exception x){
				LogUtil.error(x,SKIP_Logger);
			}
			
		}
	}

	public static void appendToFile(String str,File dist,String chart,boolean isAppend) throws Exception {
		if(str==null){
			str="";
		}
		FileOutputStream fos=new FileOutputStream(dist,isAppend);
		OutputStreamWriter osw=new OutputStreamWriter(fos,chart);
		BufferedWriter writer=new BufferedWriter(osw);
		writer.write(str);
		writer.flush();
		osw.close();
		writer.close();
		fos.close();
	}
	
	public static void appendToFile(String str,File dist,boolean isAppend) throws Exception {
		appendToFile(str,dist,"UTF-8",isAppend);
	}
	
	public static void appendToFile(String source, String dist,boolean isAppend) throws Exception{
		if(source!=null && !"".equals(source) && dist!=null && !"".equals(dist)){
			File sf=new File(source);
			File df=new File(dist);
			appendToFile(sf,df,isAppend);
		}
	}

	
	/** 
     * 解压zip格式压缩包 
     * 对应的是ant.jar 
     */  
    public static void unzip(String sourceZip,String destDir) throws Exception{  
    	//默认编码gbk  
    	unzip(sourceZip,destDir,"gbk");
    }
    
    public static void unzip(File sourceZip,File destDir) throws Exception{  
    	//默认编码gbk  
    	unzip(sourceZip,destDir,"gbk");
    }
	/**
	 * 解压ZIP
	 * @param sourceZip
	 * @param destDir
	 * @param coder 编码格式
	 * @throws Exception
	 */
    public static void unzip(String sourceZip,String destDir,String coder) throws Exception{ 
    	File sf=new File(sourceZip);
    	File df=new File(destDir);
    	if(coder==null || coder.length()<=0){
    		coder="gbk";
    	}
    	unzip(sf,df,coder);
    }
    /**
     * 解压ZIP文件
     * @param sourceZip
     * @param destDir
     * @param coder
     * @throws Exception
     */
    public static void unzip(File sourceZip,File destDir,String coder) throws Exception{ 
   	 try{
            Project p = new Project();  
            Expand e = new Expand();  
            e.setProject(p);  
            e.setSrc(sourceZip);  
            e.setOverwrite(false);  
            e.setDest(destDir);
            e.setEncoding(coder);
            e.execute();
        }catch(Exception e){
        	LogUtil.error("unzip error ... " + e.getMessage(),SKIP_Logger);
            throw e;  
        }
   }
    
    /**  
     *   
     * @param inputFileName 输入一个文件夹  
     * @param zipFileName   输出一个压缩文件夹，打包后文件名字  
     * @throws Exception 
     */  
    public static void zip(String inputFileName, String zipFileName) throws Exception {   
        LogUtil.info("begin zip :" + zipFileName + "......",SKIP_Logger);
        zip(zipFileName, new File(inputFileName));
    }
  
    public static void zip(String zipFileName, File inputFile) throws Exception {
    	FileOutputStream fout=new FileOutputStream(zipFileName);
        ZipOutputStream out = new ZipOutputStream(fout);
        zip(out, inputFile, "");
        LogUtil.info(zipFileName + " zip done.",SKIP_Logger);
        //fout.close();
        out.close();
    }
  
    public static File zip(File zipFile, File inputFile) throws Exception {
    	FileOutputStream fout=new FileOutputStream(zipFile);
        ZipOutputStream out = new ZipOutputStream(fout);
        zip(out, inputFile, "");
        LogUtil.info(zipFile.getName()+ " zip done.",SKIP_Logger);
        //fout.close();
        out.close();
        return zipFile;
    }
    
    public static void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {//判断是否为目录
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {//压缩目录中的所有文件
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int b;
            LogUtil.debug("add zip file : " + base,SKIP_Logger);
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            in.close();
        }
    }
    
    
    /** 
     * 解压rar格式压缩包
     */  
    public static void unrar(String sourceRar,String destDir) throws Exception{
    	if(sourceRar!=null && destDir!=null && sourceRar.length()>0 && destDir.length()>0){
    		File srf=new File(sourceRar);
    		File dsf=new File(destDir);
    		if(srf.exists() && srf.isFile() && dsf.exists() && dsf.isDirectory()){
    			unrar(srf,dsf);
    		}
    	}
    } 
    
    
    public static void unrar(File sourceRar,File distDir) throws Exception{
    	if(sourceRar!=null && distDir!=null && sourceRar.exists()){
	    	Archive a = null;  
	        FileOutputStream fos = null;  
	        try{  
	             a = new Archive(sourceRar);
	             FileHeader fh = a.nextFileHeader();
	             while(fh!=null){  
	                 if(!fh.isDirectory()){
	                     String compressFileName = fh.getFileNameString().trim();
	                     String destDir="";
	                     if(compressFileName.lastIndexOf("\\")>=0){
	                    	 compressFileName = compressFileName.replaceAll("\\\\", "/");
	                     }
	                     if(compressFileName.lastIndexOf("/")>=0){
	                    	 destDir=compressFileName.substring(0,compressFileName.lastIndexOf("/"));
	                     }
	                     File dir=new File(distDir.getPath() + "/" + destDir);
	                     if(!dir.exists() && !dir.isDirectory()){
	                    	 dir.mkdirs();
	                     }
	                     String destFileName = distDir.getPath() +"/"+ compressFileName.replaceAll("\\\\", "/");
	                     LogUtil.info("-----file name : " + destFileName,SKIP_Logger);
	                     fos = new FileOutputStream(new File(destFileName));  
	                     a.extractFile(fh, fos);  
	                     fos.close();  
	                     fos = null;  
	                 }
	                 fh = a.nextFileHeader();
	             }  
	             a.close();  
	             a = null;  
	         }catch(Exception e){
	             throw e;  
	         }finally{  
	             if(fos!=null){  
	                 try{fos.close();fos=null;}catch(Exception e){e.printStackTrace();}  
	             }  
	             if(a!=null){  
	                 try{a.close();a=null;}catch(Exception e){e.printStackTrace();}  
	             }
	         }
    	}
    }
    
    public static String getFileCharset(File f){
    	String fc="";
    	if(f.exists()&& f.isFile()){
			try{
				FileCharsetDetector fcd=new FileCharsetDetector();
				fc=fcd.guestFileEncoding(f);
			}catch(Exception x){
				LogUtil.error(x,SKIP_Logger);
			}
		}
    	return fc;
    }
    
    public static String getFileCharset(String filePath){
    	String fc="";
    	if(filePath!=null && filePath.length()>0){
    		File f=new File(filePath);
    		fc=getFileCharset(f);
    	}
    	return fc;
    }
    
    public static List<File> getFilesForFolder(String folder,String ... types) throws Exception{
    	File file=new File(folder);
    	return getFilesForFolder(file,types);
    }
    
    public static List<File> getFilesForFolder(File folder,String ... types) throws Exception{
    	List<File> list=new ArrayList<File>();
    	if(folder.exists() && folder.isDirectory()){
    		File[] fs=folder.listFiles();
    		for(File f:fs){
    			String name=f.getName();
    			if(name.lastIndexOf(".")>0){
    				String extName=name.substring(name.lastIndexOf("."));
    				extName=extName.trim().toUpperCase();
    				for(String type:types){
    					type=type.toUpperCase();
        				if(extName.equals(type)){
            				list.add(f);
            			}
        			}
    			}
    		}
    		return list;
    	}else{
    		return null;
    	}
    }
    
	public static String getFilesJson(String path,String ... types) throws Exception{
		if(path!=null && path.length()>0){
			File file=new File(path);
			if(file.exists() && file.isDirectory()){
				return getFilesJson(file,types);
			}
		}
		return "";
	}
	
	public static String getFilesJson(File path,String ... types) throws Exception{
		List files=getFilesForFolder(path,types);
		if(files!=null){
			List fl=genServerFileMapList(files);
			return JSONSerializer.toJSON(fl).toString();
		}
		return "";
	}
	
	public static List genServerFileMapList(List<File> files){
		List flist=null;
		if(files!=null && files.size()>0){
			Map fm=null;
			flist=new ArrayList();
			for(int i=0;i<files.size();i++){
				File f=files.get(i);
				String name=f.getName();
				String path=f.getPath();
				fm=new HashMap();
				fm.put("fname", name);
				fm.put("fpath", path);
				flist.add(fm);
			}
		}
		return flist;
	}
    
	public static String deleteFile(String path){
	       String messge="";
			File file=new File(path);
			if(file.exists()){
				if(file.isFile()){
					file.delete();
					messge="删除成功";
				}else{

					messge="出错了！";
				}	
			}else{
				messge="文件不存在了！";
			}
			return messge;
		}
    
	public static void main(String[] args) throws Exception {
		// createFolderUseSystemDate("E:\\file\\workDir\\webServer\\myEclipse\\ZShifts\\apache-tomcat-6.0.29\\webapps\\ZWM\\uploads\\resourses\\");
// String s=getFileContent("c:/bb/bb.html");

		// System.out.println(s.replaceAll("\\s", ""));

		//String s = "<script>var ddddd='我是中国人';</script><p>123</p>";

		//s = getHtmlStr2Text(s);
		
		String file="C:/Users/王立铎/Desktop/2个文档/总中心_电话银行座席系统操作手册（2012年8月版）120828/总中心_电话银行座席系统操作手册（2012年8月版）120828.htm";
		
		String ecode=FileUtils.getFileCharset(file);
		
		System.out.println(ecode);
		
		//String s=FileUtils.getFileContent(file);
		
		//System.out.println(s);
		//Map map=parserFile(new File(file));
		//Map mm=(Map)map.get(FILE_METADATA);
		//System.out.println(mm.get("charset"));
		//String s=map.get(FILE_CONTENT).toString();
		
		//System.out.println("--- : " + s);
		//unrar("E:/dddd/dddd.rar","E:/file/workDir/webServer/myEclipse/ZDeskRelease/apache-tomcat-6.0.29/webapps/ZDesk/zkmDocs/AB3EAD84-2704-9CC4-6A91-89C796F12623/025FE06D-1E68-D1AB-FD65-E0F60B72AB23");
		//File f=new File("e:/b.txt");
		
		
		//Reader r=new FileReader(f);
		
		//LineNumberReader lr=new LineNumberReader(r);
		//String ls=lr.readLine();
		//while(ls!=null){
		//	System.out.println(ls);
		//	ls=lr.readLine();
		//}
		
		
		/*String s1="e:/test.txt";
		File sf=new File(s1);
		File file=new File("C:/bb");
		File[] f=file.listFiles();
		for(int i=0;i<f.length;i++){
			File ft=f[i];
			appendToFile(ft,sf);
		}*/
		//String file="E:/file/workDir/workBase/5/ZDesk/update_1123/BC139262-E114-DEBB-5EBE-9C52EC325A14";
		//String s=getFileRealContent(file);
		//System.out.println(s);
		
		//InputStream in=new FileInputStream(new File(file));
		//BufferedReader reader=new BufferedReader(new InputStreamReader(in,"UTF-8"));
		//String line=reader.readLine();
		//while(line!=null){
		//	System.out.println(line);
		//	line=reader.readLine();
		//}
		
		/*String abc="我是一个中国人~!!@#!@#$%^&*)>?/";
		String eco=java.net.URLEncoder.encode(abc,"UTF-8");
		System.out.println(eco);
		System.out.println(java.net.URLDecoder.decode(eco,"UTF-8"));*/
		
		/*File f=new File("E:/TDDOWNLOAD/dddd");
		File [] fs=f.listFiles();
		for(File fi:fs){
			String name=fi.getName();
			String ex=name.substring(name.lastIndexOf(".") +1,name.length());
			if("html".equals(ex)){
				name=name.substring(0,name.lastIndexOf("."));
				fi.renameTo(new File(f.getPath() + "/" + name));
			}
		}*/
		/*String p="/D6B82E31-403A-58A9-62B5-422F3E0A062F/74AE69A1-C612-B2EB-3571-B3AB3F260973/9ECCA527-0F3C-5AE4-E0AF-D92C7EE8F263";
		String s="9ECCA527-0F3C-5AE4-E0AF-D92C7EE8F263";
		p=p.substring(0,p.lastIndexOf("/"));
		System.out.println(p);
		String [] ss=p.split("/");
		System.out.println("ssss : " + ss[0] + "  " + ss[1] + " " + ss.length);*/
		//String src="E:\\lunceTest\\ZW_temp\\2011\\12\\07\\F6703483-62DB-6C5B-0984-17EF04D32F84";
		//String to="E:\\lunceTest\\ZW_temp\\2011\\12\\07\\F6703483-62DB-6C5B-0984-17EF04D32F84\\test.zip";
		//File fs=new File(src);
		//File ft=new File(to);
		//zip(ft,fs);
		//String a="abcde";
		//String c="b";
		//System.out.println(a.indexOf(c));
		/*String file="E:/file/workDir/webServer/myEclipse/ZDesk_205/apache-tomcat-6.0.29/webapps/ZDesk/ZKM/zkmDocs/flowDisponseDocs/2013/01/16/C55AD51B-31D0-313D-0215-F9BD188A7347/index.html";
		Metadata metadata = new Metadata();
		String str=FileUtils.parseToString(new FileInputStream(new File(file)),metadata,1000000);
		System.out.println(str.length());*/
		//Map map =parserFile(new File(file));
	}


}
