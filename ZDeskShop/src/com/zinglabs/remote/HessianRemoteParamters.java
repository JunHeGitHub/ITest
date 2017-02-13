package com.zinglabs.remote;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.core.frame.SysPropertiesUtil;
import com.zinglabs.db.DAOTools;

public class HessianRemoteParamters {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	public static List<Map<String,String>> errorServer;
	public static final String REMOTE_CONF_BITEM="remoteServer";
	
	static{
		errorServer=new ArrayList<Map<String,String>>();
	}
	/**
	 * 获取远程服务的服务参数
	 * @param serverKey 服务器标识
	 * @return 返回远程服务器参数
	 */
	public synchronized static Map getRemoteServer(String serverKey){
		return getRemoteServer(serverKey,0);
	}
	
	/**
	 * 获取远程服务的服务参数
	 * 逻辑：
	 * 	获取远程服务参数，测试是否是errorServer,是则再取；
	 * 					 测试远程服务是否可用，否则再取；
	 * 	直到取完所有配置的服务器，若都不可用，则返回错误；
	 * @param severKey 服务器标识
	 * @param serverNum	优先级代码
	 * @return 返回远程服务器参数
	 */
	public synchronized static Map getRemoteServer(String serverKey,int serverNum){
		logger.debug("debug HessianRemoteParamters.getRemoteServer serverkey : " + serverKey + " : serverNum : " + serverNum);
		Map rmap=null;
		List list=SysPropertiesUtil.getConfWithBItemAndProductionIdAndPeizhiItem(REMOTE_CONF_BITEM,serverKey,String.valueOf(serverNum));
		//若配置的serverNum有多个0或1这种情况属配置有问题，这种情况下，list.get(0)只取其中一条。
		if(list!=null && list.size()>0){
			rmap=(Map)list.get(0);
		}
		if(rmap!=null){
			//测试取回的服务器参数是否是errorServer中
			if(errorServerWork(rmap,"getOne")){
				logger.debug("debug HessianRemoteParamters.getRemoteServer is errorServer  errorServerWork : " + serverKey + " : serverNum : " + serverNum );
				rmap=getRemoteServer(serverKey,++serverNum);
			}else{
				//测试远程服务是否可用
				boolean tester=HessianClient.testRemoteServer(rmap);
				if(!tester){
					logger.debug("debug HessianRemoteParamters.getRemoteServer is errorServer tester: " + serverKey + " : serverNum : " + serverNum );
					errorServerWork(rmap,"add");
					rmap=getRemoteServer(serverKey,++serverNum);
				}
			}
		}else{
			logger.error("error HessianRemoteParamters.getRemoteServer rmap is null : " + serverKey + " : " + serverNum);
		}
		return rmap;
	}
	
	/**
	 * 操作errorServer线程安全的方法
	 * @param map
	 * @param doSome
	 * @return
	 */
	public static synchronized boolean errorServerWork(Map map,String doSome){
		boolean rebl=false;
		if(map==null){
			logger.error("-- HessianRemoteParamters.errorServerWork paramter map is null....");
			return rebl;
		}
		logger.debug("-- HessianRemoteParamters.errorServerWork " + doSome);
		if("add".equals(doSome)){
			errorServer.add(map);
			logger.debug("-- HessianRemoteParamters.errorServerWork add ");
		}else if("remove".equals(doSome)){
			String id=map.get("id")==null?"":(String)map.get("id");
			logger.debug("-- HessianRemoteParamters.errorServerWork remove ... " + id);
			for(int i=errorServer.size()-1;i>0;i--){
				Map tm=(Map)errorServer.get(i);
				String tid=tm.get("id")==null?"":(String)tm.get("id");
				if(tid.equals(id)){
					errorServer.remove(i);
					break;
				}
			}
			logger.debug("-- HessianRemoteParamters.errorServerWork remove " + id + " end");
		}else if("getOne".equals(doSome)){
			String productionId=map.get("productionId")==null?"":(String)map.get("productionId");
			String peizhiItem=map.get("peizhiItem")==null?"":(String)map.get("peizhiItem");
			logger.debug("-- HessianRemoteParamters.errorServerWork getOne ... " + productionId + " : " + peizhiItem);
			String xpath="bItem='remoteServer' and productionId='"+ productionId +"' and peizhiItem='"+ peizhiItem +"'";
			if(productionId.length()>0 && peizhiItem.length()>0){
				List list=SysPropertiesUtil.listSearch(errorServer, xpath);
				if(list!=null && list.size()>0){
					return true;
				}
			}
			logger.debug("-- HessianRemoteParamters.errorServerWork getOne " + productionId + " : " + peizhiItem + " end");
		}
		return rebl;
	}
}
