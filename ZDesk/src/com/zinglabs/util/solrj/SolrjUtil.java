package com.zinglabs.util.solrj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.CoreContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.log.LogUtil;
import com.zinglabs.util.ZKMConfs;


public class SolrjUtil {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	public static Properties confs=null;
	
	public static final String MODE_HTTP="HTTP";
	public static final String MODE_EMBED="Embedded";
	
	//写服务配置的配置名称开头标识
	public static final String SOLR_SERVER_HEAD="server_";
	public static final String SOLR_SERVER_W_HEAD="server_w";
	public static final String SOLR_SERVER_R_HEAD="server_r";
	
	public static final String SOLR_DEL_TYPE_BYID="byId";
	public static final String SOLR_DEL_TYPE_BYQUERY="byQuery";
	
	public static String solrMode;
	public static String serverWriteDefault;
	public static String serverReaderDefault;
	public static String indexFields;
	
	//写服务Map
	public static Map<String,SolrServer> wserver;
	public static Map<String,SolrServer> rserver;
	
	public static List<String> wlist;
	
	//读服务列表
	public static Map <String,CommonsHttpSolrServer> rhttpServerList;
	public static Map <String,EmbeddedSolrServer> rembedServerList;
	
	//写服务列表
	public static Map <String,CommonsHttpSolrServer> whttpServerList;
	public static Map <String,EmbeddedSolrServer> wembedServerList;
	
	//排序常量
	public static final SolrQuery.ORDER orderDesc=SolrQuery.ORDER.desc;
	public static final SolrQuery.ORDER orderAsc=SolrQuery.ORDER.asc;
	
	public static int maxBooleanClauses=0;
	
	static{
		init();
	}
	
	public static void init(){
		try{
			wlist=new ArrayList<String>();
			confs=ZKMConfs.confs;
			solrMode=confs.getProperty("solrMode");
			maxBooleanClauses=Integer.parseInt(confs.getProperty("maxBooleanClauses","3000"));
			
			indexFields=confs.getProperty("indexFields");
			serverWriteDefault=confs.getProperty("server_w_default","server_w_core0");
			serverReaderDefault=confs.getProperty("server_r_default","server_w_core0");
			
			if(wserver==null){
				wserver=new HashMap<String,SolrServer>();
			}
			
			if(rserver==null){
				rserver=new HashMap<String,SolrServer>();
			}
			
			if(whttpServerList==null){
				whttpServerList=new HashMap<String,CommonsHttpSolrServer>();
			}
			
			if(rhttpServerList==null){
				rhttpServerList=new HashMap<String,CommonsHttpSolrServer>();
			}
			
			if(rembedServerList==null){
				rembedServerList=new HashMap<String,EmbeddedSolrServer>();
			}
			
			if(wembedServerList==null){
				wembedServerList=new HashMap<String,EmbeddedSolrServer>();
			}
			
			if(solrMode!=null && solrMode.length()>0){
				loadServer();
			}else{
				LogUtil.error("获取solr初始化参数失败。",SKIP_Logger);
			}
		}catch(Exception x){
			LogUtil.error("初始化 solrServer失败。",SKIP_Logger);
		}
		
	}
	
	public static SolrServer loadWriteServer(String serverKey){
		System.out.println("1111111111111");
		if(confs!=null && serverKey!=null && serverKey.length()>0){
			SolrServer ws=wserver.get(serverKey);
			if(ws==null){
				String url=confs.getProperty(serverKey,"");
				//TODO:这里没有做服务类型判断
				if(url!=null && url.length()>0 && serverKey.indexOf(SOLR_SERVER_W_HEAD)>=0){
					try{
						CommonsHttpSolrServer chss=httpServerInit(url);
						whttpServerList.put(serverKey,chss);
						wserver.put(serverKey, chss);
						ws=chss;
					}catch(Exception x){
						LogUtil.error("--- init wserver error:",SKIP_Logger);
						LogUtil.error(x,SKIP_Logger);
						x.printStackTrace();
					}
				}
			}
			return ws;
		}else{
			return null;
		}
	}
	
	public static SolrServer loadReaderServer(String serverKey){
		if(confs!=null && serverKey!=null && serverKey.length()>0){
			SolrServer ws=wserver.get(serverKey);
			if(ws==null){
				String url=confs.getProperty(serverKey,"");
				//TODO:这里没有做服务类型判断
				if(url!=null && url.length()>0 && serverKey.indexOf(SOLR_SERVER_R_HEAD)>=0){
					try{
						CommonsHttpSolrServer chss=httpServerInit(url);
						whttpServerList.put(serverKey,chss);
						wserver.put(serverKey, chss);
						ws=chss;
					}catch(Exception x){
						LogUtil.error("--- init wserver error:",SKIP_Logger);
						LogUtil.error(x,SKIP_Logger);
						x.printStackTrace();
					}
				}
			}
			return ws;
		}else{
			return null;
		}
	}
	
	public static void loadServer(){
		if(confs!=null){
			Iterator it=confs.entrySet().iterator();
			while(it.hasNext()){
				Entry et=(Entry)it.next();
				String key=(String)et.getKey();
				String value=(String)et.getValue();
				if(key!=null && key.length()>0 && value!=null && value.length()>0){
					if(key.indexOf(SOLR_SERVER_HEAD)>=0){
						if(wserver.get(key)!=null || rserver.get(key)!=null ||
								key.equals("server_w_default") ||
								key.equals("server_r_default")){
							continue;
						}
						if(solrMode!=null && solrMode.equals(MODE_HTTP)){
							if(key.indexOf(SOLR_SERVER_W_HEAD)>=0){
								CommonsHttpSolrServer chss=httpServerInit(value);
								whttpServerList.put(key,chss);
								wserver.put(key, chss);
							}
							if(key.indexOf(SOLR_SERVER_R_HEAD)>=0){
								CommonsHttpSolrServer chss=httpServerInit(value);
								rhttpServerList.put(key,chss);
								rserver.put(key, chss);
							}
						}else if(solrMode!=null && solrMode.equals(MODE_EMBED)){
							if(key.indexOf(SOLR_SERVER_W_HEAD)>=0){
								EmbeddedSolrServer emss=embeddedServerInit(value);
								wembedServerList.put(key,emss);
								wserver.put(key, emss);
							}
							if(key.indexOf(SOLR_SERVER_R_HEAD)>=0 && !serverReaderDefault.equals(key)){
								EmbeddedSolrServer emss=embeddedServerInit(value);
								rembedServerList.put(key,emss);
								rserver.put(key, emss);
							}
						}
					}
				}
			}
		}
	}
	
	public static CommonsHttpSolrServer httpServerInit(String serverUrl){
		LogUtil.debug("init solrHttpServer "+ serverUrl + " ...... ",SKIP_Logger);
		CommonsHttpSolrServer server=null;
		if(serverUrl!=null && serverUrl.length()>0){
			try{
				server= new CommonsHttpSolrServer(serverUrl);
			}catch(Exception x){
				LogUtil.error("init solrHttpServer error......",SKIP_Logger);
				LogUtil.error(x,SKIP_Logger);
				x.printStackTrace();
			}
			if(server!=null){
				server.setSoTimeout(1000); // socket read timeout   
				server.setConnectionTimeout(1000);   
				server.setDefaultMaxConnectionsPerHost(1000);   
				server.setMaxTotalConnections(3000);
				server.setFollowRedirects(false); // defaults to false   
				// allowCompression defaults to false.   
				// Server side must support gzip or deflate for this to have any effect.   
				server.setAllowCompression(true);
				server.setMaxRetries(1); // defaults to 0.  > 1 not recommended.
				//sorlr J 目前使用二进制的格式作为默认的格式。对于solr1.2的用户通过显示的设置才能使用XML格式。  
				server.setParser(new XMLResponseParser());
			}
			LogUtil.debug("init solrHttpServer...... done.",SKIP_Logger);
		}
		return server;
	}
	
	public static EmbeddedSolrServer embeddedServerInit(String serverUrl){
		LogUtil.debug("init embeddedServer " + serverUrl +" ......",SKIP_Logger);
		EmbeddedSolrServer ws=null;
		if(serverUrl!=null && serverUrl.length()>0){
			try{
				System.setProperty("solr.solr.home", serverUrl);
				CoreContainer.Initializer initializer = new CoreContainer.Initializer();
				CoreContainer coreContainer = initializer.initialize();
				ws = new EmbeddedSolrServer(coreContainer, "");
			}catch(Exception x){
				LogUtil.error("init embeddedServer error......",SKIP_Logger);
			}
			LogUtil.debug("init embeddedServer...... done.",SKIP_Logger);
		}
		return ws;
	}
	
	/**
	 * 增加修改索引文档
	 * @param docMap
	 * @return
	 * @throws Exception
	 */
	public static boolean updateIndexDoc(HashMap<String,String> docMap,String serverKey) throws Exception{
		if(docMap!=null && serverKey!=null && serverKey.length()>0){
			SolrInputDocument doc = new SolrInputDocument();
			Iterator<Entry<String,String>> it=docMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<String,String> e=it.next();
				String key=e.getKey();
				String value="";
				if(e.getValue()!=null && e.getValue() instanceof String){
					value=e.getValue();
				}
				if(indexFields.indexOf(key + ",")>=0){
					doc.addField(key,value);
				}
			}
			SolrServer wsr=wserver.get(serverKey);
			if(wsr==null){
				wsr=loadWriteServer(serverKey);
			}
			if(wsr!=null){
				try{
					wsr.add(doc);
					wsr.commit();
					return true;
				}catch(Exception ex){
					LogUtil.error("------ add or update doc error : ",SKIP_Logger);
					LogUtil.error(ex,SKIP_Logger);
					throw ex;
				}
			}
			
			/*SolrServer ss=getWriterSolrServer(serverKey);
			if(ss!=null){
				SolrInputDocument doc = new SolrInputDocument();
				Iterator<Entry<String,String>> it=docMap.entrySet().iterator();
				while(it.hasNext()){
					Entry<String,String> e=it.next();
					String key="";
					String value="";
					if(e.getKey() instanceof String)
						key=e.getKey();
					if(e.getValue()!=null && e.getValue() instanceof String){
						value=e.getValue();
					}
					if(indexFields.indexOf(key + ",")>=0){
						doc.addField(key,value);
					}
				}
				ss.add(doc);
				ss.commit();
			}else{
				return false;
			}*/
		}
		return false;
	}
	
	public static boolean updateIndexDoc(HashMap<String,String> docMap) throws Exception{
		return updateIndexDoc(docMap,serverWriteDefault);
	}
	/**
	 * 以线程方式增加、修改索引文档
	 * @param docMap
	 * @throws Exception
	 */
	public static void updateIndexDocForThread(HashMap<String,String> docMap,String serverKey) throws Exception{
		PutdocThread pt=new PutdocThread();
		pt.doc=docMap;
		pt.serverKey=serverKey;
		Thread th=new Thread(pt);
		th.start();
	}
	/**
	 * 简单查询
	 * @param value 查询的值
	 * @param start 开始记录
	 * @param num 取记录数量
	 * @param sortField 排序字段
	 * @param order 排序方式
	 * @return
	 * @throws Exception
	 */
	public static SolrDocumentList queryEasy(String value,int start,int num,String sortField,SolrQuery.ORDER order,String serverKey) throws Exception{
		SolrQuery solrQuery = new SolrQuery().
		setQuery(value).
		setFacet(false);
		//设置起始记录位置、取得记录数、排序列，排序方式
		solrQuery=queryParamDisponse(solrQuery,start ,num,sortField,order);
		return doQuery(solrQuery);
	}
	
	public static SolrDocumentList queryEasy(String value,int start,int num,String sortField,SolrQuery.ORDER order) throws Exception{
		return queryEasy(value,start,num,sortField,order,serverReaderDefault);
	}
	/**
	 * 一值对多列查询
	 * @param value 值
	 * @param Fields 查询列数组
	 * @param start 开始记录
	 * @param num 取记录数
	 * @param sortField 排序字段
	 * @param order 排序方式
	 * @return
	 * @throws Exception
	 */
	public static SolrDocumentList querySingValue(String value,String[] fields,int start ,int num,String sortField,SolrQuery.ORDER order,String serverKey) throws Exception{
		if(fields!=null && fields.length>0){
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<fields.length;i++){
				if(i+1==fields.length){
					sb.append(fields[i] + ":" + value);
				}else{
					sb.append(fields[i] + ":" + value + " AND ");
				}
			}
			value=sb.toString();
		}
		SolrQuery solrQuery = new SolrQuery().
		setQuery(value).
		setFacet(false);
		//设置起始记录位置、取得记录数、排序列，排序方式
		solrQuery=queryParamDisponse(solrQuery,start ,num,sortField,order);
		return doQuery(solrQuery);
	}
	
	public static SolrDocumentList querySingValue(String value,String[] fields,int start ,int num,String sortField,SolrQuery.ORDER order) throws Exception{
		return querySingValue(value,fields,start,num,sortField,order,serverReaderDefault);
	}
	
	public static SolrQuery queryForSortBase(String value,String [] sorts,int start ,int num,String sortField,SolrQuery.ORDER order,String serverKey)throws Exception{
		SolrQuery solrQuery = new SolrQuery().
		setQuery(value).
		setFacet(false);
		if(sorts!=null && sorts.length>0){
			if(sorts.length<maxBooleanClauses){
				StringBuffer sb=new StringBuffer();
				sb.append(confs.getProperty("suceritySortField","parentId"));
				sb.append(":(");
				for(int i=0;i<sorts.length;i++){
					String s=sorts[i];
					if(s!=null && s.length()>0){
						if(i+1==sorts.length){
							sb.append(s);
							sb.append(")");
						}else{
							sb.append(s);
							sb.append(" OR ");
						}
					}
				}
				solrQuery.addFilterQuery(sb.toString());
			}else{
				LogUtil.error("the sorts MAX number too big! maxBooleanClauses : " + maxBooleanClauses + " sorts : " + sorts.length,SKIP_Logger);
			}
		}
		solrQuery=queryParamDisponse(solrQuery,start,num,sortField,order);
		return solrQuery;
	}
	
	public static SolrDocumentList queryForSort(String value,String [] sorts,int start ,int num,String sortField,SolrQuery.ORDER order,String serverKey) throws Exception{
		return doQuery(queryForSortBase(value,sorts,start,num,sortField,order,serverKey));
	}
	
	public static SolrDocumentList queryForSort(String value,String [] sorts,int start ,int num,String sortField,SolrQuery.ORDER order) throws Exception{
		return queryForSort(value,sorts,start,num,sortField,order,serverReaderDefault);
	}
	
	/**
	 * 查询参数加配
	 * @param solrQuery solr查询实例
	 * @param start 开始记录
	 * @param num 取记录数量
	 * @param sortField	排序字段
	 * @param order 排序方式
	 * @return
	 */
	public static SolrQuery queryParamDisponse(SolrQuery solrQuery,int start ,int num,String sortField,SolrQuery.ORDER order){
		if(solrQuery!=null){
			//设置起始记录位置、取得记录数、排序列，排序方式
			solrQuery.setStart(start).setRows(num).addSortField(sortField, order);
		}
		return solrQuery;
	}
	
	/**
	 * solr串查询
	 * @param q solr查询串
	 * @return
	 * @throws Exception
	 */
	public static SolrDocumentList queryString(String q,String serverKey) throws Exception{
		SolrQuery solrQuery = new SolrQuery().setQuery(q);
		return doQuery(solrQuery);
	}
	
	public static SolrDocumentList queryString(String q) throws Exception{
		return queryString(q,serverReaderDefault);
	}
	/**
	 * 查询执行，返回SolrDocumentList对象
	 * @param solrQuery solr查询实例
	 * @return 
	 * @throws Exception
	 */
	public static SolrDocumentList doQueryList(SolrQuery solrQuery,String serverKey) throws Exception{
		SolrDocumentList list=null;
		if(solrQuery!=null && serverKey!=null && serverKey.length()>0){
			QueryResponse rsp=doQueryResponse(solrQuery);
			list=rsp.getResults();
		}
		return list;
	}
	
	public static SolrDocumentList doQuery(SolrQuery solrQuery) throws Exception{
		return doQueryList(solrQuery,serverReaderDefault);
	}
	
	/**
	 * 查询，返回QueryResponse对象
	 * @param solrQuery
	 * @param serverKey
	 * @return
	 * @throws Exception
	 */
	public static QueryResponse doQueryResponse(SolrQuery solrQuery,String serverKey) throws Exception{
		QueryResponse rsp=null;
		if(solrQuery!=null && serverKey!=null && serverKey.length()>0){
			SolrServer rs=getReaderSolrServer(serverKey);
			rsp = rs.query(solrQuery,SolrRequest.METHOD.POST);
		}
		return rsp;
	}
	
	public static QueryResponse doQueryResponse(SolrQuery solrQuery){
		QueryResponse rsp=null;
		try{
			rsp=doQueryResponse(solrQuery,serverReaderDefault);
		}catch(Exception x){
			LogUtil.error("solr reader is error : ............ " + serverReaderDefault,SKIP_Logger);
			LogUtil.error(x,SKIP_Logger);
			x.printStackTrace();
		}
		if(rsp==null && rserver!=null){
			Iterator<Map.Entry<String, SolrServer>> it=rserver.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, SolrServer> e=it.next();
				String key=e.getKey();
				SolrServer reader=e.getValue();
				if(reader==null){
					loadReaderServer(key);
				}
				try{
					rsp=doQueryResponse(solrQuery,key);
				}catch(Exception x){
					LogUtil.error("solr reader is error : ............ " + key,SKIP_Logger);
					LogUtil.error(x,SKIP_Logger);
					x.printStackTrace();
				}
				if(rsp!=null){
					break;
				}
			}
		}
		return rsp;
	}
	
	/**
	 * 删除索引
	 * @param id 删除索引的ID
	 * @throws Exception
	 */
	public static void delIndexDoc(String id,String serverKey) throws Exception{
		if(id!=null && id.length()>0 && serverKey!=null && serverKey.length()>0){
			SolrServer ss=getWriterSolrServer(serverKey);
			if(ss!=null){
				ss.deleteById(id);
				ss.commit();
			}
		}
	}
	
	public static void delIndexDoc(String id) throws Exception{
		delIndexDoc(id,serverWriteDefault);
	}
	
	public static void delIndexByQuery(String query,String serverKey) throws Exception{
		if(query!=null && query.length()>0 && serverKey!=null && serverKey.length()>0){
			SolrServer ss=getWriterSolrServer(serverKey);
			if(ss!=null){
				ss.deleteByQuery(query);
				ss.commit();
			}
		}
	}
	
	public static void delIndexByQuery(String query) throws Exception{
		delIndexDoc(query,serverWriteDefault);
	}
	
	/**
	 * 在所有写服务器上进行删除操作
	 * @param str 关键字
	 * @param type 删除类型
	 * @throws Exception
	 */
	public static void delIndexForAllWserver(String str,String type) throws Exception {
		if(wserver!=null){
			Iterator<Map.Entry<String,SolrServer>> it=wserver.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, SolrServer> e=it.next();
				if(type.equals(SOLR_DEL_TYPE_BYID)){
					delIndexDoc(str, e.getKey());
				}else if(type.equals(SOLR_DEL_TYPE_BYQUERY)){
					delIndexByQuery(str,e.getKey());
				}
			}
		}
	}
	
	public static SolrQuery.ORDER getOrderType(String orderStr){
		orderStr=orderStr.toUpperCase();
		if("ASC".equals(orderStr)){
			return orderAsc;
		}else if("DESC".equals(orderStr)){
			return orderDesc;
		}else{
			//没有则返回desc
			return orderDesc;
		}
	}
	
	public static SolrServer getWriterSolrServer(String serverKey){
		if(serverKey!=null && serverKey.length()>0){
			SolrServer ss=wserver.get(serverKey);
			return ss;
		}
		return null;
	}
	
	public static SolrServer getReaderSolrServer(String serverKey){
		if(serverKey!=null && serverKey.length()>0){
			SolrServer ss=rserver.get(serverKey);
			return ss;
		}
		return null;
	}
	
	public static void simpleDeleteByUrl(String url,String id) throws Exception {
		CommonsHttpSolrServer hs= httpServerInit(url);
		hs.deleteById(id);
		hs.commit();
		hs.optimize();
	}
	
	public static void main(String [] args) throws Exception{
		//SolrServer ws=wserver.get("server_w_core0".toUpperCase());
		//SolrServer rs=rserver.get("server_r_core0".toUpperCase());
		
		//queryString("*:*","server_r_core0");
		/*SolrQuery solrQuery = new SolrQuery();
		String value="allField:大地 AND parentId:(";
		StringBuffer sb=new StringBuffer();
		String [] sort=new String[4000];
		for(int i=0;i<sort.length;i++){
			if(i+1==sort.length){
				sb.append("5ACB5125-C63D-24F6-BABF-E4C71D024546)");
			}else{
				sb.append("5ACB5125-C63D-24F6-BABF-E4C71D024546 ");
			}
		}
		value+=sb.toString();
		//System.out.println(value);
		solrQuery.setQuery(value);
		QueryResponse qr=doQueryResponse(solrQuery);
		System.out.println(qr.getResults().size());
		*/
		//List list=queryEasy("*:*",0,100,"keywords",orderDesc,serverReaderDefault);
		//System.out.println(list.size());
		/*Iterator it=SolrjUtil.wserver.entrySet().iterator();
		Iterator<Map.Entry<String, SolrServer>> itw=wserver.entrySet().iterator();
		while(itw.hasNext()){
			Map.Entry<String, SolrServer> e=itw.next();
			SolrServer wsr=e.getValue();
			if(wsr==null){
				System.out.println(e.getKey() + "  is null");
			}else{
				System.out.println(e.getKey() + "  is not null");
			}
		}
		SolrjUtil.loadServer();*/
		//simpleDeleteByUrl("http://22.8.81.222:9902/solr/core0","B1783FCF-C95C-95A1-3CF2-3DBF692395C3");
		//simpleDeleteByUrl("http://22.8.81.222:9902/solr/core1","B1783FCF-C95C-95A1-3CF2-3DBF692395C3");
		SolrjUtil.delIndexForAllWserver("*:*",SolrjUtil.SOLR_DEL_TYPE_BYQUERY);
		
	}
}
