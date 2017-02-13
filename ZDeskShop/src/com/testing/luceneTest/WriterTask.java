package com.testing.luceneTest;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.log.LogUtil;



public class WriterTask implements Runnable{
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	
	public String fileDir=null;
	String trName=null;
	public void run() {
		while(true){
			try{
		        long start = new Date().getTime();
		        int numIndexed = LuceneIndexerForTest.index(LuceneIndexerForTest.getIndexWriter(),new File(fileDir));//调用index方法  
		        long end = new Date().getTime();
		        LogUtil.debug("Indexing " + numIndexed + " files took " + (end - start) + " milliseconds", SKIP_Logger);
		        Thread.sleep(4000);
			}catch(Exception x){
				LogUtil.error(x, SKIP_Logger);
				LogUtil.error("trName:"+trName, SKIP_Logger);
			}
		}
	}

}
