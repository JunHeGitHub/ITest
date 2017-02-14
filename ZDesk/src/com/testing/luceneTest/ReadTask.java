package com.testing.luceneTest;

import java.util.HashMap;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.log.LogUtil;
import com.zinglabs.luceneSearch.LuceneSearcher;


public class ReadTask implements Runnable{
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	
	String [][]qstr=null;
	String trName=null;
	public void run() {
		while(true){
			try{
				Thread.sleep(2000);
				Searcher searcher=LuceneSearcher.getAllSearcher();
				
				IndexReader reader=LuceneSearcher.getAllReaders(true);
				ScoreDoc[] hits=LuceneSearcher.getManyTermSearch(searcher,qstr,0,100,false);
				HashMap frqe=LuceneSearcher.getManyTermDocFreq(reader,qstr,false);
				List tremlist=LuceneSearcher.getScoreDoc2List(hits,searcher);
				/*if(tremlist!=null && tremlist.size()>0){
					for(int i=0;i<tremlist.size();i++){
						HashMap map=(HashMap)tremlist.get(i);
						logger.debug(map.get("filename"));
						logger.debug(map.get("title"));
						logger.debug("索引文档ID：" + map.get(LuceneSearcher.SEARCH_RETURN_DOC_ID));
						logger.debug("权值：" + map.get(LuceneSearcher.SEARCH_RETURN_HIT_LEVEL));
						logger.debug("出现次数：" + frqe.get(map.get(LuceneSearcher.SEARCH_RETURN_DOC_ID)));
					}
				}*/
				searcher.close();
				reader.close();
			}catch(Exception x){
				LogUtil.error(x, SKIP_Logger);
				LogUtil.error("trName:"+trName, SKIP_Logger);
			}
		}
	}

}
