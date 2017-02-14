package com.testing.officeTest;

import java.io.File;
import java.io.InputStream;

import org.apache.poi.POITextExtractor;
import org.apache.poi.extractor.ExtractorFactory;

import java.io.FileInputStream;  
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.metadata.Metadata;  
import org.apache.tika.parser.AutoDetectParser;  
import org.apache.tika.parser.Parser;  
import org.apache.tika.sax.BodyContentHandler;  
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;  

import com.zinglabs.log.LogUtil;

public class OfficeTest {
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("AT");
	
	public static void a(StringBuffer list){
		list.append("bbb");
	}
	
	public static void b(StringBuffer list){
		list.append("ccc");
	}
	
	public static void getDocText(File file) throws Exception{
		POITextExtractor e=ExtractorFactory.createExtractor(file);
		System.out.println(e.getText());
	}
	
	public static void getDocMimeType(File file) throws Exception{
		ContentHandler contenthandler = new BodyContentHandler();  
	    Metadata metadata = new Metadata();  
	    metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
	    Parser parser = new AutoDetectParser();
	    
	    InputStream is=new FileInputStream(file);
	    
	    parser.parse(is, contenthandler, metadata);
	    LogUtil.info("Mime: " + metadata.get(Metadata.CONTENT_TYPE)+" Title:"+metadata.get(Metadata.TITLE)+" Author:"+metadata.get(Metadata.AUTHOR), SKIP_Logger);
	    //logger.info("content: " + contenthandler.toString());
	}
	
	public static void main(String [] args) throws Exception{
//		File[] files={
//				new File("C://bb//bb.txt"),
//				new File("C://bb//bb.doc"),
//				new File("C://bb//bb.docx"),
//				new File("C://bb//bb.xls"),
//				new File("C://bb//bb.xlsx"),
//				new File("C://bb//bb.swf"),
//				new File("C://bb//bb.jpg"),
//				new File("C://bb//bb.html"),
//				new File("C://bb//bb.png"),
//				new File("C://bb//bb.gif")
//		};
//		
//		/*File file=new File("C://bb//bb.txt");
//		OfficeTest.getDocText(file);*/
//		
//		for(int i=0;i<files.length;i++){
//			File f=files[i];
//			logger.info("");
//			logger.info("----------- : " + f.getName());
//			OfficeTest.getDocMimeType(f);
//			logger.info("");
//		}
		
		StringBuffer list=new StringBuffer();
		
		list.append("aaa");
		System.out.println(list.toString());
		OfficeTest.a(list);
		OfficeTest.b(list);
		
		System.out.println(list.toString());
		
	}
}
