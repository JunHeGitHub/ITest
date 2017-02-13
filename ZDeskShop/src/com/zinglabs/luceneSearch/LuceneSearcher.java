package com.zinglabs.luceneSearch;

import java.io.File;
import java.io.StringReader;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultipleTermPositions;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.zinglabs.log.LogUtil;


import java.util.*;

public class LuceneSearcher extends LuceneBase{
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM"); 
	
	public static ScoreDoc[] getSearch(Searcher searcher,Query query,int beginNum,int recordNum) throws Exception{
		TopScoreDocCollector collector = TopScoreDocCollector.create(beginNum+recordNum,false);
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs(beginNum,recordNum).scoreDocs;
		return hits;
	}
	
	public static ScoreDoc[] getManyTermSearch(Searcher searcher,String[][] conditions,int beginNum,int recordNum,boolean useAnalyzerTrem)throws Exception {
		if(conditions!=null && conditions.length>0 && searcher!=null){
			long start = new Date().getTime();
			BooleanQuery query = new BooleanQuery();
			QueryParser parser =null;
			Query queryTemp =null;
			for(int i=0;i<conditions.length;i++){
				String []term=conditions[i];
				Occur occur=null;
				if(term[0]!=null && !"".equals(term[0]) && term[1]!=null && !"".equals(term[1])){
					if(term.length<3){
						occur=BooleanClause.Occur.SHOULD;
					}else if("MUST".equals(term[2])){
						occur=BooleanClause.Occur.MUST;
					}else if("MUST_NOT".equals(term[2])){
						occur=BooleanClause.Occur.MUST_NOT;
					}else if("SHOULD".equals(term[2])){
						occur=BooleanClause.Occur.SHOULD;
					}else{
						occur=BooleanClause.Occur.MUST_NOT;
					}
					if(useAnalyzerTrem){
						parser = new QueryParser(Version.LUCENE_33, term[0],analyzer);
						queryTemp=parser.parse(term[1]);
					}else{
						queryTemp=new TermQuery(new Term(term[0],term[1]));
					}
					query.add(queryTemp,occur);
					LogUtil.debug("add term :" + term[0] + " : " + term[1] + " : " + occur,SKIP_Logger);
				}
			}
			ScoreDoc[] hits = getSearch(searcher,query,beginNum,recordNum);
			long end = new Date().getTime();
			LogUtil.debug("term search Found " + hits.length+ 
					" document(s) (in " + (end - start)+ " milliseconds) that matched query trem num :" + conditions.length + ":",SKIP_Logger);
			return hits;
		}else{
			return null;
		}
	}
	
	public static ScoreDoc[] getManyConditionSearch(Searcher searcher,String field,String[] condition,int beginNum,int recordNum,boolean useAnalyzerTrem) throws Exception {
		if(condition!=null && condition.length>0 && field!=null && !"".equals(field) && searcher!=null){
			String [][] tfields=new String[condition.length][3];
			for(int i=0;i<condition.length;i++){
				tfields[i][0]=field;
				tfields[i][1]=condition[i];
				tfields[i][2]=TREM_OCCUR_SHOULD;
			}
			return getManyTermSearch(searcher,tfields,beginNum,recordNum,useAnalyzerTrem);
		}else{
			return null;
		}
	}
	
	public static ScoreDoc[] getManyFieldSearch(Searcher searcher,String[] fields,String condition,int beginNum,int recordNum,boolean useAnalyzerTrem) throws Exception{
		if(fields!=null && fields.length>0 && condition!=null && !"".equals(condition) && searcher!=null){
			String [][] tfields=new String[fields.length][3];
			for(int i=0;i<fields.length;i++){
				tfields[i][0]=fields[i];
				tfields[i][1]=condition;
				tfields[i][2]=TREM_OCCUR_SHOULD;
			}
			return getManyTermSearch(searcher,tfields,beginNum,recordNum,useAnalyzerTrem);
		}else {
			return null;
		}
	}
	
	public static ScoreDoc[] getManyFieldConditionSearch(Searcher searcher,String[] fields,String[] condition,int beginNum,int recordNum,boolean useAnalyzerTrem) throws Exception{
		if(fields!=null && fields.length>0 && condition!=null && !"".equals(condition) && searcher!=null){
			List list=new ArrayList();
			for(int j=0;j<fields.length;j++){
				for(int k=0;k<condition.length;k++){
					String[] ts=new String[3];
					ts[0]=fields[j];
					ts[1]=condition[k];
					ts[2]=TREM_OCCUR_SHOULD;
					list.add(ts);
				}
			}
			if(list!=null && list.size()>0){
				String [][] tfields=new String[list.size()][3];
				for(int i=0;i<list.size();i++){
					tfields[i]=(String[])list.get(i);
				}
				return getManyTermSearch(searcher,tfields,beginNum,recordNum,useAnalyzerTrem);
			}else{
				return null;
			}
		}else {
			return null;
		}
	}
	
	public static HashMap getDocFreqForQuery(IndexReader indexReader,String[][] conditions,boolean useAnalyzerTrem)throws Exception{
		if(conditions!=null && conditions.length>0 && indexReader!=null){
			List termList=new ArrayList();
			Term [] terms=null;
			if(!useAnalyzerTrem){
				terms=new Term[conditions.length];
			}
			for(int i=0;i<conditions.length;i++){
				String [] cd=conditions[i];
				if(useAnalyzerTrem){
					List cdlist=getConditionAnalyzer(cd[1]);
					if(cdlist!=null && cdlist.size()>0){
						for(int j=0;j<cdlist.size();j++){
							String cd1=(String)cdlist.get(j);
							if(cd1!=null && !"".equals(cd1)){
								boolean toadd=true;
								//去除重复的条件
								for(int k=0;k<termList.size();k++){
									Term tm=(Term)termList.get(k);
									if(cd1.equals(tm.text())){
										toadd=false;
										break;
									}
								}
								if(toadd){
									termList.add(new Term(cd[0],cd1));
								}
							}
						}
					}
				}else{
					terms[i]=new Term(cd[0],cd[1]);
				}
			}
			MultipleTermPositions mtp=null;
			if(useAnalyzerTrem){
				if(termList!=null && termList.size()>0){
					terms=new Term[termList.size()];
					for(int i=0;i<termList.size();i++){
						terms[i]=(Term)termList.get(i);
					}
				}
				mtp=new MultipleTermPositions(indexReader,terms);
			}else{
				mtp=new MultipleTermPositions(indexReader,terms);
			}
			HashMap map=new HashMap();
			while(mtp.next()){
				map.put(mtp.doc(), mtp.freq());
			}
			return map;
		}else{
			return null;
		}
	}
	
	public static HashMap getManyTermDocFreq(IndexReader reader,String[][] conditions,boolean useAnalyzerTrem) throws Exception{
		if(conditions!=null && conditions.length>0 && reader!=null){
			return getDocFreqForQuery(reader,conditions,useAnalyzerTrem);
		}else{
			return null;
		}
	}
	
	public static HashMap getManyFieldConditionDocFreq(IndexReader reader,String[] fields,String[] condition,boolean useAnalyzerTrem) throws Exception{
		if(fields!=null && fields.length>0 && condition!=null && !"".equals(condition) && reader!=null){
			List list=new ArrayList();
			for(int j=0;j<fields.length;j++){
				for(int k=0;k<condition.length;k++){
					String[] ts=new String[3];
					ts[0]=fields[j];
					ts[1]=condition[k];
					ts[2]=TREM_OCCUR_SHOULD;
					list.add(ts);
				}
			}
			if(list!=null && list.size()>0){
				String [][] tfields=new String[list.size()][3];
				for(int i=0;i<list.size();i++){
					tfields[i]=(String[])list.get(i);
				}
				return getDocFreqForQuery(reader,tfields,useAnalyzerTrem);
			}else{
				return null;
			}
		}else {
			return null;
		}
	}
	
	public static HashMap getManyFieldDocFreq(IndexReader reader,String[] fields,String condition,boolean useAnalyzerTrem) throws Exception{
		if(fields!=null && fields.length>0 && condition!=null && !"".equals(condition) && reader!=null){
			String [][] tfields=new String[fields.length][3];
			for(int i=0;i<fields.length;i++){
				tfields[i][0]=fields[i];
				tfields[i][1]=condition;
				tfields[i][2]=TREM_OCCUR_SHOULD;
			}
			return getDocFreqForQuery(reader,tfields,useAnalyzerTrem);
		}else {
			return null;
		}
	}
	
	public static HashMap getManyConditionDocFreq(IndexReader reader,String field,String[] condition,boolean useAnalyzerTrem) throws Exception{
		if(condition!=null && condition.length>0 && field!=null && !"".equals(field) && reader!=null){
			String [][] tfields=new String[condition.length][3];
			for(int i=0;i<condition.length;i++){
				tfields[i][0]=field;
				tfields[i][1]=condition[i];
				tfields[i][2]=TREM_OCCUR_SHOULD;
			}
			return getDocFreqForQuery(reader,tfields,useAnalyzerTrem);
		}else{
			return null;
		}
	}
	
	public static List getScoreDoc2List(ScoreDoc[] hits,Searcher searcher) throws Exception {
		List relist=null;
		if(hits!=null && hits.length>0){
			relist=new ArrayList();
			for(int i=0;i<hits.length;i++){
				ScoreDoc hit=hits[i];
				HashMap hm=getDocument2HashMap(hit.doc,searcher);
				hm.put(SEARCH_RETURN_DOC_ID, hit.doc);
				hm.put(SEARCH_RETURN_HIT_LEVEL,hit.score);
				relist.add(hm);
			}
		}
		return relist;
	}
	
	public static HashMap getDocument2HashMap(int docId,Searcher searcher) throws Exception{
		Document doc=searcher.doc(docId);
		return getDocument2HashMap(doc);
	}
	
	public static HashMap getDocument2HashMap(Document doc){
		HashMap remap=null;
		if(doc!=null){
			remap= new HashMap();
			List fs=doc.getFields();
			for(int i=0;i<fs.size();i++){
				Field fi=(Field)fs.get(i);
				remap.put(fi.name(), fi.stringValue());
			}
		}
		return remap;
	}
	
	public static List getConditionAnalyzer(String condition) throws Exception{
		if(condition!=null && !"".equals(condition)){
			long bd=System.currentTimeMillis();
			List list=new ArrayList();
			IKSegmenter tokenizer = new IKSegmenter(new StringReader(condition), false);
			while(true){
				Lexeme le=tokenizer.next();
				if(le!=null){
					list.add(le.getLexemeText());
				}else{
					break;
				}
			}
			long ed=System.currentTimeMillis();
			//logger.info(condition + " analyzer time : " + (ed-bd));
			return list;
		}else{
			return null;
		}
	}
	
	public static List getSearch(String condition,int b,int n,boolean analyzer) throws Exception{
		Searcher searcher=getAllSearcher();
		ScoreDoc[] hits=getManyFieldSearch(searcher,allSearchField,condition,b,n,analyzer);
		List searchList=getScoreDoc2List(hits,searcher);
		IndexReader reader=getAllReaders(true);
		HashMap freqMap=getManyFieldDocFreq(reader,allSearchField,condition,analyzer);
		searcher.close();
		reader.close();
		return getSearchListHandle(searchList,freqMap);
	}
	
	public static List getSearch(String[] conditions,int b,int n,boolean analyzer)throws Exception{
		Searcher searcher=getAllSearcher();
		ScoreDoc[] hits=getManyFieldConditionSearch(searcher,allSearchField,conditions,b,n,analyzer);
		List searchList=getScoreDoc2List(hits,searcher);
		IndexReader reader=getAllReaders(true);
		HashMap freqMap=getManyFieldConditionDocFreq(reader,allSearchField,conditions,analyzer);
		searcher.close();
		reader.close();
		return getSearchListHandle(searchList,freqMap);
	}
	
	public static HashMap getSearch(int docId)throws Exception{
		Searcher searcher=getAllSearcher();
		HashMap map=getDocument2HashMap(docId,searcher);
		searcher.close();
		return map;
	}
	
	
	public static List getSearchListHandle(List searchList,HashMap freqMap){
		if(searchList!=null && freqMap!=null){
			List relist=new ArrayList();
			for(int i=0;i<searchList.size();i++){
				HashMap map=(HashMap)searchList.get(i);
				map.put(SEARCH_RETURN_FREQ_NUM, freqMap.get(map.get(SEARCH_RETURN_DOC_ID)));
				relist.add(map);
			}
			return relist;
		}
		return null;
	}
	
	public static void main(String[] args){
		LuceneBase lb=new LuceneBase();
		try{
			
			
			int beginNum=0;
			int recordNum=100;
			
			 /*String s="";
			 //s="蹂躏";
			 //s="啃文书库";
			 s="趾高气昂";
			 //s="年龄";
			 //s="保护费啊";
			//String[] fields={"filename","contents","title","id"};
			String[] fields={"contents","title"};
			ScoreDoc[] hits=getManyFieldSearch(fields,s,beginNum,recordNum,false);
			List fieldlist=getScoreDoc2List(hits);
			HashMap frqe=getManyFieldDocFreq(fields,s,false);
			if(fieldlist!=null && fieldlist.size()>0){
				for(int i=0;i<fieldlist.size();i++){
					HashMap map=(HashMap)fieldlist.get(i);
					logger.info(map.get("filename"));
					logger.debug(map.get("title"));
					logger.debug("索引文档ID：" + map.get(SEARCH_RETURN_DOC_ID));
					logger.debug("权值：" + map.get(SEARCH_RETURN_HIT_LEVEL));
					logger.debug("出现次数：" + frqe.get(map.get(SEARCH_RETURN_DOC_ID)));
				}
			}*/
			
			
			/*String [][] qstr={
					{"contents","状态",TREM_OCCUR_MUST}
			};
			Searcher searcher=getAllSearcher();
			IndexReader reader=getAllReaders(true);
			ScoreDoc[] hits=getManyTermSearch(searcher,qstr,beginNum,recordNum,false);
			HashMap frqe=getManyTermDocFreq(reader,qstr,true);
			List tremlist=getScoreDoc2List(hits,searcher);
			if(tremlist!=null && tremlist.size()>0){
				for(int i=0;i<tremlist.size();i++){
					HashMap map=(HashMap)tremlist.get(i);
					logger.debug(map.get("filename"));
					logger.debug(map.get("title"));
					logger.debug("索引文档ID：" + map.get(SEARCH_RETURN_DOC_ID));
					logger.debug("权值：" + map.get(SEARCH_RETURN_HIT_LEVEL));
					logger.debug("出现次数：" + frqe.get(map.get(SEARCH_RETURN_DOC_ID)));
					
					String folder=(String)map.get(WRITER_INDEX_FOLDER_PATH);
					File file=new File(folder);
					
					if(file.exists() && file.isDirectory()){
						File pf=file.getParentFile();
						logger.info(pf.getName());
					}
				}
			}*/
			
			//IndexReader read=getAllReaders(false);
			//System.out.println(read.numDocs());
			
		}catch(Exception x){
			x.printStackTrace();
		}
	}
}
