package com.testing.luceneTest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.log.LogUtil;
import com.zinglabs.luceneSearch.LuceneBase;
import com.zinglabs.util.FileUtils;

public class LuceneIndexerForTest extends LuceneBase{
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM"); 
	
	public static synchronized int index(IndexWriter writer,File dataDir) throws Exception {  
        if (!dataDir.exists() || !dataDir.isDirectory()) {
          throw new IOException(dataDir + " does not exist or is not a directory");  
        }
        indexDirectory(writer, dataDir);
        int numIndexed = writer.numDocs();
        
        return numIndexed;  
    }
	
	
    public static synchronized void indexDirectory(IndexWriter writer, File dir) throws Exception {  
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {  
                indexDirectory(writer, f);  // recurse  
            }else if (f.getName().endsWith(".txt")) {
                indexFile(writer, f);
            }else if(f.getName().endsWith(".htm")){
            	indexFile(writer, f);
            }
            writer=getIndexWriter();
       }
       writer.optimize();
       writer.commit();
    }
    
    
    
    public static synchronized void indexFile(IndexWriter writer, File f)throws Exception {  
        if (f.isHidden() || !f.exists() || !f.canRead()) {  
          return;
        }
        if(f.length()>=Integer.parseInt(confs.getProperty("MaxFieldLength"))){
        	LogUtil.error("index file error : file size " + f.length() + " then " + confs.getProperty("MaxFieldLength"), SKIP_Logger);
        	return;
        }
        LogUtil.debug("Indexing " + f.getCanonicalPath(), SKIP_Logger);
        Document doc = new Document();
        doc.add(new Field("contents",FileUtils.getThreadSingFileContent(f),Field.Store.NO, Field.Index.ANALYZED,TermVector.YES));
        doc.add(new Field("title",f.getName(),Field.Store.YES, Field.Index.ANALYZED,TermVector.YES));
        doc.add(new Field("filename",f.getCanonicalPath(),Field.Store.YES, Field.Index.ANALYZED,TermVector.YES));
        doc.add(new Field("extendName",f.getCanonicalPath(),Field.Store.YES, Field.Index.ANALYZED,TermVector.YES));
        
        doc.add(new Field(WRITER_INDEX_MAP_KEY,workManager.getIndexMapKey(),Field.Store.YES, Field.Index.NO));
        doc.add(new Field(WRITER_INDEX_FOLDER_PATH,workManager.getIndexFolder(),Field.Store.YES, Field.Index.NO));
        
        writer.addDocument(doc);
    }
    
    public static synchronized void indexFile(IndexWriter writer, HashMap map)throws Exception {  
    	File f=(File)map.get("file");
        if (f.isHidden() || !f.exists() || !f.canRead()) {  
          return;
        }
        //logger.debug("Indexing " + f.getCanonicalPath());
        Document doc = new Document();
        doc.add(new Field("contents",FileUtils.getThreadSingFileContent(f),Field.Store.NO, Field.Index.ANALYZED,TermVector.YES));
        doc.add(new Field("title",(String)map.get("name"),Field.Store.YES, Field.Index.ANALYZED,TermVector.YES));
        doc.add(new Field("filename",f.getCanonicalPath(),Field.Store.YES, Field.Index.ANALYZED,TermVector.YES));
        doc.add(new Field("extendName",(String)map.get("extendName"),Field.Store.YES, Field.Index.ANALYZED,TermVector.YES));
        
        doc.add(new Field("uploadDate",(String)map.get("uploadDate"),Field.Store.YES, Field.Index.ANALYZED,TermVector.YES));
        doc.add(new Field(WRITER_INDEX_MAP_KEY,workManager.getIndexMapKey(),Field.Store.YES, Field.Index.NO));
        doc.add(new Field(WRITER_INDEX_FOLDER_PATH,workManager.getIndexFolder(),Field.Store.YES, Field.Index.NO));
        
        writer.addDocument(doc);
        writer.optimize();
        writer.commit();
    }
    
    public static synchronized void indexFileNotCommit(IndexWriter writer, HashMap map)throws Exception {  
    	File f=(File)map.get("file");
        if (f.isHidden() || !f.exists() || !f.canRead()) {  
          return;
        }
        //logger.debug("Indexing " + f.getCanonicalPath());
        Document doc = new Document();
        doc.add(new Field("contents",FileUtils.getThreadSingFileContent(f),Field.Store.NO, Field.Index.ANALYZED,TermVector.YES));
        doc.add(new Field("title",(String)map.get("name"),Field.Store.YES, Field.Index.ANALYZED,TermVector.YES));
        doc.add(new Field("filename",f.getCanonicalPath(),Field.Store.YES, Field.Index.ANALYZED,TermVector.YES));
        doc.add(new Field("extendName",(String)map.get("extendName"),Field.Store.YES, Field.Index.ANALYZED,TermVector.YES));
        
        doc.add(new Field("uploadDate",(String)map.get("uploadDate"),Field.Store.YES, Field.Index.ANALYZED,TermVector.YES));
        doc.add(new Field(WRITER_INDEX_MAP_KEY,workManager.getIndexMapKey(),Field.Store.YES, Field.Index.NO));
        doc.add(new Field(WRITER_INDEX_FOLDER_PATH,workManager.getIndexFolder(),Field.Store.YES, Field.Index.NO));
        
        writer.addDocument(doc);
    }
    
    public static void IndexWriterCommit(IndexWriter iw) throws Exception{
    	iw.optimize();
    	iw.commit();
    	getIndexWriter();
    }
    
    public static void main(String[] args) {
    	try{
	        /*String fileDir = "E://lunceTest//datas";//文件存放的目录 
	        long start = new Date().getTime();
	        int numIndexed = index(getIndexWriter(),new File(fileDir));//调用index方法  
	        long end = new Date().getTime();
	        logger.debug("Indexing " + numIndexed + " files took " + (end - start) + " milliseconds");
	        
	        index(getIndexWriter(),new File(fileDir));
	        
	        index(getIndexWriter(),new File(fileDir));
	        
	        index(getIndexWriter(),new File(fileDir));*/
	        
	    	/* mmseg4j
	        String str="啃文书库";
	        MaxWord stt=new MaxWord();
	        String words=stt.segWords(str,"|");
	        logger.debug(words);
	        */
	    	/*IK*/
	    	//String t = toFieldFileReader(new File("E:\\lunceTest\\新建 文本文档 (2).txt"));
    		/*String t = "";
			IKTokenizer tokenizer = new IKTokenizer(new StringReader(t) , false);
			while(tokenizer.incrementToken()){
				TermAttribute termAtt = tokenizer.getAttribute(TermAttribute.class);
				logger.debug(termAtt);
			}*/
    	}catch(Exception x){
			x.printStackTrace();
		}
    }
}
