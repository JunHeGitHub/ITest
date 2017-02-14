package com.zinglabs.luceneSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;
import java.util.List;
import java.io.File;
import java.io.InputStream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.zinglabs.log.LogUtil;
import com.zinglabs.util.FileUtils;
import com.zinglabs.util.RandomGUID;

public class LuceneBase implements LuceneBaseInterface{
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
	
	public static final String LUCENE_CONF_NAME="luceneConf.properties";
	
	public static Properties confs=null;
	
	public static HashMap<String,IndexManager> workManagerMap;
	
	public static HashMap<String,IndexSearcher> searcherMap;
	
	public static HashMap<String,IndexReader> readerMap;
	
	public static IndexManager workManager;
	
	public static List<IndexManager> indexManagerList;
	
	public static Analyzer analyzer;
	
	public static Thread WriterThread=null;
	
	public static LunceIndexer lunceIndexer=null;
	
	private static IndexWriter indexWriter;
	
	//public static final String[] allSearchField={"contents","title","filename","extendName"};
	
	public static final String[] allSearchField={"title","keywords","keywords1","keywords2","keywords3","keywords4","keywords5","summary","fileContent"};
	
	public static IndexManager initIndexManager(File root,File file){
		IndexManager im=null;
		if(file!=null  && file.exists() && file.isDirectory()){
			im=new IndexManager();
			String indexMapKey=root.getName() + "_" +file.getName();
			im.setWorkFolder(file);
			im.setIndexFolder(file.getPath());
			im.setWorkRootFolder(root);
			im.setIndexMapKey(indexMapKey);
		}
		return im;
	}
	
	public static IndexManager createFolderForIndexManager(File root){
		IndexManager im=null;
		if(root!=null && root.exists() && root.isDirectory()){
			String folderName=(new RandomGUID()).toString();
			String fpath=root.getPath() + "/" + folderName;
			File nf=new File(fpath);
			if(!nf.exists()){
				nf.mkdirs();
			}
			im=initIndexManager(root,nf);
		}
		return im;
	}
	
	public static boolean hasIndexFiles(IndexManager im){
		boolean rebl=false;
		if(im!=null && im.getWorkFolder()!=null && im.getWorkFolder().exists() && im.getWorkFolder().isDirectory()){
			File[] fi=im.getWorkFolder().listFiles();
			if(fi!=null && fi.length>0){
				for(int i=0;i<fi.length;i++){
					File f=fi[i];
					String na=f.getName();
					if(na.indexOf("segments")>=0){
						rebl=true;
					}
				}
			}
		}
		return rebl;
	}
	
	static{
		try{
			indexManagerList=new ArrayList<IndexManager>();
			analyzer=new IKAnalyzer();
			workManagerMap=new HashMap<String,IndexManager>();
			confs=new Properties();
			InputStream is = LuceneBase.class.getClassLoader().getResourceAsStream(LUCENE_CONF_NAME);
			confs.load(is);
			is.close();
			String indexRootDir=confs.getProperty("indexRootDirs");
			if(indexRootDir!=null && !"".equals(indexRootDir)){
				long rootDirMaxSize=getConfsRootDirMaxSize();
				String [] roots=indexRootDir.split(";");
				for(int i=0;i<roots.length;i++){
					String root=roots[i];
					File file=new File(root);
					if((!file.exists()) || (!file.isDirectory())){
						file.mkdirs();
					}
					File[] files=file.listFiles();
					if(files!=null && files.length>0){
						for(int j=0;j<files.length;j++){
							File fi=files[j];
							if(fi.isDirectory()){
								IndexManager im=initIndexManager(file,fi);
								workManagerMap.put(im.getIndexMapKey(), im);
								if(rootDirMaxSize!=0){
									 if(FileUtils.getFileSize(fi)<rootDirMaxSize){
										 workManager=im;
									 }
								}else{
									workManager=im;
								}
								indexManagerList.add(im);
							}
						}
					}
					if((i+1 ==roots.length) && workManager==null){
						IndexManager im=createFolderForIndexManager(file);
						workManagerMap.put(im.getIndexMapKey(),im);
						workManager=im;
						indexManagerList.add(im);
					}
				}
				LogUtil.info("Load index folder : " + indexManagerList.size() + ";  now use workManager is : " + workManager.getIndexFolder(),SKIP_Logger);
			}else{
				LogUtil.error("初始化全文检索失败：索引目录参数“indexRootDir”未设置或为空",SKIP_Logger);
			}
		}catch(Exception x){
			LogUtil.error(x, SKIP_Logger);
		}
	}
	
	public static boolean vildateFolderMaxSizeFull() throws Exception{
		File fwork=workManager.getWorkFolder();
		long maxSize=getConfsRootDirMaxSize();
		LogUtil.info("maxSize : " + maxSize + "; folder size: " + FileUtils.getFileSize(fwork),SKIP_Logger);
		if(maxSize!=0 && FileUtils.getFileSize(fwork)>maxSize){
			return true;
		}else{
			return false;
		}
	}
	
	
	public static synchronized void closeWorkWriter()throws Exception{
		//indexWriter.commit();
		indexWriter.close();
		//indexWriter=null;
	}
	
	public static IndexSearcher getSingSearcher(String indexName) throws Exception{
		IndexSearcher search=null;
		if(workManagerMap.get(indexName)!=null){
			search=workManagerMap.get(indexName).getSearcher();
		}
		return search;
	}
	
	public static MultiSearcher getAllSearcher() throws Exception{
		MultiSearcher search=null;
		List<IndexManager> slist=new ArrayList<IndexManager>();
		for(int i=0;i<indexManagerList.size();i++){
			IndexManager im=indexManagerList.get(i);
			if(im!=null && im.getSearcher()!=null){
				slist.add(im);
			}
		}
		
		if(slist.size()>0){
			IndexSearcher [] searchers=new IndexSearcher[slist.size()];
			for(int i=0;i<slist.size();i++){
				searchers[i]=slist.get(i).getSearcher();
			}
			search=new MultiSearcher(searchers);
		}
		return search;
	}
	
	public static IndexReader getSingReader(String indexName,boolean readOnly) throws Exception{
		IndexReader reader=null;
		if(workManagerMap.get(indexName)!=null){
			reader=workManagerMap.get(indexName).getReader(readOnly);
		}
		return reader;
	}
	
	public static MultiReader getAllReaders(boolean readOnly) throws Exception{
		MultiReader mreader=null;
		List<IndexManager> slist=new ArrayList<IndexManager>();
		for(int i=0;i<indexManagerList.size();i++){
			IndexManager im=indexManagerList.get(i);
			if(im.getReader(readOnly)!=null){
				slist.add(im);
			}
		}
		if(slist.size()>0){
			IndexReader [] readers=new IndexReader[slist.size()];
			for(int i=0;i<slist.size();i++){
				readers[i]=slist.get(i).getReader(readOnly);
			}
			mreader=new MultiReader(readers);
		}
		return mreader;
	}
	
	public static long getConfsRootDirMaxSize(){
		String maxSize=confs.getProperty("rootDirMaxSize");
		long rootDirMaxSize=0;
		if(confs.get("rootDirMaxSize")!=null && !"".equals(maxSize)){
			rootDirMaxSize =Long.parseLong(maxSize);
			rootDirMaxSize=rootDirMaxSize*1024 * 1024;
		}
		return rootDirMaxSize;
	}
	
	public static synchronized IndexWriter getIndexWriter() throws Exception{
		if(vildateFolderMaxSizeFull()){
			IndexManager im=createFolderForIndexManager(workManager.getWorkRootFolder());
			workManagerMap.put(im.getIndexMapKey(), im);
			indexManagerList.add(im);
			workManager=im;
			indexWriter.close();
			indexWriter=null;
			LogUtil.info("changer work folder : " + workManager.getIndexFolder(),SKIP_Logger);
		}
		if(indexWriter!=null && workManager.getDirectory()!=null && indexWriter.isLocked(workManager.getDirectory())){
			return indexWriter;
		}
		if(workManager.getDirectory()!=null && LuceneBase.analyzer!=null){
				indexWriter=new IndexWriter(workManager.getDirectory(),LuceneBase.analyzer,IndexWriter.MaxFieldLength.UNLIMITED);
				String MaxBufferedDocs=LuceneBase.confs.getProperty("MaxBufferedDocs");
				if(MaxBufferedDocs!=null && !"".equals(MaxBufferedDocs)){
					indexWriter.setMaxBufferedDocs(Integer.parseInt(MaxBufferedDocs));
				}
				String RAMBufferSizeMB=LuceneBase.confs.getProperty("RAMBufferSizeMB");
				if(RAMBufferSizeMB!=null && !"".equals(RAMBufferSizeMB)){
					indexWriter.setRAMBufferSizeMB(Integer.parseInt(RAMBufferSizeMB));
				}
				String MergeFactor=LuceneBase.confs.getProperty("MergeFactor");
				if(MergeFactor!=null && !"".equals(MergeFactor)){
					indexWriter.setMergeFactor(Integer.parseInt(MergeFactor));
				}
				String maxFieldLength =confs.getProperty("MaxFieldLength");
				if(maxFieldLength!=null && !"".equals(maxFieldLength)){
					indexWriter.setMaxFieldLength(Integer.parseInt(maxFieldLength));
				}
			}
		return indexWriter;
	}
	
	public static void main(String[] args) throws Exception{
		String f="E:/lunceTest/index/f7a517b9-e259-4722-8214-f89197c6b826";
		LogUtil.info(""+Integer.MAX_VALUE,SKIP_Logger);
		//logger.info(FileUtils.getFileSize(new File(f)));
	}
}
