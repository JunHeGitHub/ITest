package com.zinglabs.luceneSearch;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
public class IndexManager {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM"); 
	
	private String indexMapKey;
	
	private String indexFolder;
	
	private File workRootFolder;
	
	private String state;
	
	private File workFolder;
	
	private FSDirectory directory;
	
	public FSDirectory getDirectory() throws Exception{
		if(directory==null && this.getWorkFolder()!=null){
			String direct=LuceneBase.confs.getProperty("DirectoryClass","MMapDirectory");
			if("MMapDirectory".equals(direct)){
				directory=org.apache.lucene.store.MMapDirectory.open(this.getWorkFolder());
			}else if("SimpleFSDirectory".equals(direct)){
				directory=org.apache.lucene.store.SimpleFSDirectory.open(this.getWorkFolder());
			}else if("NIOFSDirectory".equals(direct)){
				directory=org.apache.lucene.store.NIOFSDirectory.open(this.getWorkFolder());
			}
		}
		return directory;
	}

	public void setDirectory(FSDirectory directory) {
		this.directory = directory;
	}

	public File getWorkRootFolder() {
		return workRootFolder;
	}

	public void setWorkRootFolder(File workRootFolder) {
		this.workRootFolder = workRootFolder;
	}

	public File getWorkFolder() {
		return workFolder;
	}

	public void setWorkFolder(File workFolder) {
		this.workFolder = workFolder;
	}


	public IndexReader getReader(boolean readOnly) throws Exception{
		IndexReader reader=null;
		if(this.getDirectory()!=null && LuceneBase.hasIndexFiles(this)){
			reader=IndexReader.open(this.getDirectory(), readOnly);
		}
		return reader;
	}

	public IndexSearcher getSearcher() throws Exception{
		IndexSearcher iser=null;
		if(this.getDirectory()!=null && LuceneBase.hasIndexFiles(this)){
			iser=new IndexSearcher(this.getDirectory(),true);
		}
		return iser;
	}


	public String getIndexMapKey() {
		return indexMapKey;
	}

	public void setIndexMapKey(String indexMapKey) {
		this.indexMapKey = indexMapKey;
	}

	public String getIndexFolder() {
		return indexFolder;
	}

	public void setIndexFolder(String indexFolder) {
		this.indexFolder = indexFolder;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
