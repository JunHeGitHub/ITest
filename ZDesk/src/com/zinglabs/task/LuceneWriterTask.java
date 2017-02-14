package com.zinglabs.task;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.log.LogUtil;
import com.zinglabs.luceneSearch.LuceneBase;
import com.zinglabs.luceneSearch.LunceIndexer;
import com.zinglabs.servlet.LuceneSearchHandle;

public class LuceneWriterTask implements Runnable{

	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM"); 
	public HashMap writeMap;
	public String doWhat;
	
	public void run() {
		try{
			if(writeMap!=null && doWhat!=null){
				//启动轮询写入线程
				 if(LuceneBase.WriterThread==null){
			        	LuceneBase.lunceIndexer =new LunceIndexer();
			        	LuceneBase.WriterThread=new Thread(LuceneBase.lunceIndexer);
			        	LuceneBase.WriterThread.start();
			        	Thread.sleep(1000);
			       }
				LuceneSearchHandle.doWriterManager(doWhat,writeMap);
			}
			LogUtil.info("task lucene index "+writeMap.get("relName")+" ; serverFile: " + writeMap.get("filePath")+"/"
					+ writeMap.get("fileName")+ "  ...... success.",SKIP_Logger);
		}catch(Exception x){
			LogUtil.error("Lucene index writ error ....... " +writeMap.get("relName")+" ; serverFile: " 
															+ writeMap.get("filePath")+"/"
															+ writeMap.get("fileName"),SKIP_Logger);
			LogUtil.error(x,SKIP_Logger);
		}
	}

}
