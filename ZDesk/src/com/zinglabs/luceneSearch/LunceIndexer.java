package com.zinglabs.luceneSearch;
import java.util.HashMap;
import org.apache.solr.client.solrj.SolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;
import com.zinglabs.servlet.LuceneSearchHandle;
import com.zinglabs.servlet.ZKMDocServlet;
import com.zinglabs.util.ObjectUtil;
import com.zinglabs.util.RandomGUID;
import com.zinglabs.util.solrj.SolrjUtil;

import java.util.*;

public class LunceIndexer implements Runnable{
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM"); 
	private static ArrayList writeList=new ArrayList();
	public static final String WADD="ADD";
	public static final String WGET="GET";
	public static boolean runFlag=true;
	public static String dbid=ZKMDocServlet.zkmDBId;
	//提交成功失败标识
	public static final String COMMIT_TF="commitTF";
	//提交次数
	public static final String COMMIT_NUM="commitNUM";
	//提交次数
	public static final String COMMIT_SERVER_KEY="commitSERVERKEY";
	//提交建立索引最大失败次数
	public static int maxCommitLost=4;
	
	//TODO:重新建立索引
    public static void IndexReWriterToFolder(){
    	
    }
	
    public synchronized ArrayList doList(HashMap map,String dow){
    	if(WADD.equals(dow)){
    		if(map!=null){
    			writeList.add(map);
    		}
    	}else if(WGET.equals(dow)){
    		ArrayList reli=new ArrayList();
    		reli.addAll(writeList);
    		writeList.clear();
    		return reli;
    	}
    	return null;
    }
    
	public void run() {
		while(true){
			try{
				Thread.sleep(5000);
				ArrayList wl=doList(null,WGET);
				for(int i=wl.size()-1;i>=0;i--){
					Thread.sleep(1000);
					HashMap map=wl.get(i)==null?null:(HashMap)wl.get(i);
					LogUtil.info("-----infoId : " + map.get("infoId"),SKIP_Logger);
					//LuceneSearchHandle.indexFileForDataBase(LuceneBase.getIndexWriter(),map);
					//solr方式
					if(SolrjUtil.wserver!=null){
						Iterator<Map.Entry<String,SolrServer>> it=SolrjUtil.wserver.entrySet().iterator();
						while(it.hasNext()){
							Map.Entry<String, SolrServer> e=it.next();
							HashMap remap=LuceneSearchHandle.indexFileForDataBase(map,e.getKey());
							if(remap.get("commitTF")!=null){
								 boolean tf=((Boolean)remap.get("commitTF")).booleanValue();
								 if(!tf){
									 int fnum=((Integer)(remap.get(COMMIT_NUM)==null?0:remap.get(COMMIT_NUM))).intValue();
									 if(fnum<=maxCommitLost){
										 doList(remap,WADD);
										 LogUtil.debug("index lost try id : " + remap.get("id") + " : " + fnum + " : " + maxCommitLost,SKIP_Logger);
									 }else{
										 saveIndexerLostData(remap);
										 LogUtil.debug("index lost stop id : " + remap.get("id") + " : " + fnum + " : stop",SKIP_Logger);
									 }
								 }
							}else{
								LogUtil.info("Bad error - index id :" + map.get("id"),SKIP_Logger);
							}
						}
					}else{
						LogUtil.info("wserver error - index id :" + map.get("id"),SKIP_Logger);
					}
				}
				if(!runFlag){
					break;
				}
			}catch(Exception x){
				LogUtil.error("indexWriter is error ",SKIP_Logger);
				LogUtil.error(x,SKIP_Logger);
			}
		}
	}
	
	public static void saveIndexerLostData(HashMap<String,String> data){
		if(data!=null){
			try{
				String sql="insert into zkmIndexLostStore (id,hashMapObject,creatTime,type) values (?,?,now(),?)";
				String id=new RandomGUID().toString();
				ObjectUtil ou=new ObjectUtil();
				String mapdata=ou.objectToString(data);
				String [] p={id,mapdata,"zkmIndexerLoster"};
				DAOTools.updateForPrepared(sql, p, dbid);
			}catch(Exception x){
				LogUtil.error("solr lost indexer save is error : ",SKIP_Logger);
				LogUtil.error(x,SKIP_Logger);
				x.printStackTrace();
			}
		}
	}
}
