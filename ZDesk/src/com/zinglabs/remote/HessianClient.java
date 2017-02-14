package com.zinglabs.remote;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.client.HessianProxyFactory;
import com.zinglabs.remote.server.impl.RS_DBAffairsUpdate;
import com.zinglabs.remote.server.impl.RS_IdGen;
import com.zinglabs.remote.server.impl.RS_TestRemoteConnect;
import com.zinglabs.remote.server.implDef.DBAffairsImpDef;
import com.zinglabs.remote.server.implDef.HessianServerImpDef;
import com.zinglabs.remote.server.implDef.RemoteServerCall;
/**
 * 远程调用客户端 
 * @author QCF
 */
public class HessianClient {
	
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	
	public static Map doRemoteServer(String serverKey,Map p,RemoteServerCall cla){
		Map rmap=null;
		Map smap=HessianRemoteParamters.getRemoteServer(serverKey);
		String url=smap.get("peizhiItemValue")==null?"":(String)smap.get("peizhiItemValue");
		try{
			HessianProxyFactory factory = new HessianProxyFactory();
			HessianServerImpDef server = (HessianServerImpDef)factory.create(HessianServerImpDef.class, url);
			rmap=server.execRemote(cla, p);
		}catch(Exception x){
			logger.error("errexecRemoteor -- HessianClient.doRemoteServer :" + url + " : " + smap.get("peizhiItem") + " : " + serverKey);
			Iterator it=p.keySet().iterator();
			while(it.hasNext()){
				String mk=(String)it.next();
				logger.error("error -- HessianClient.doRemoteServer param :" + mk + " : " + p.get(mk));
			}
		}
		return rmap;
	}
	
	/**
	 * 事务性更新数据远程调用
	 * @param key 服务标识如：ZKM;ZQC
	 * @param p
	 * @return
	 */
	public static Map doDBAffairsUpdateRemoteServer(String serverKey,Map p){
		RemoteServerCall rsc=new RS_DBAffairsUpdate();
		Map map=doRemoteServer(serverKey,p,rsc);
		return map;
	}
	
	/**
	 * ID生成远程服务调用
	 * @param key
	 * @param p
	 * @return
	 */
	public static Map doIdGenServerRemoteServer(String serverKey,Map p){
		RemoteServerCall rsc=new RS_IdGen();
		Map map=doRemoteServer(serverKey,p,rsc);
		return map;
	}
	
	/**
	 * 测试远程服务是否可用
	 * @param map
	 * @return
	 */
	public static synchronized boolean testRemoteServer(Map p){
		boolean rebl=false;
		Map rmap=null;
		RemoteServerCall rsc=new RS_TestRemoteConnect();
		String url=p.get("peizhiItemValue")==null?"":(String)p.get("peizhiItemValue");
		try{
			HessianProxyFactory factory = new HessianProxyFactory();
			HessianServerImpDef server = (HessianServerImpDef)factory.create(HessianServerImpDef.class, url);
			rmap=server.execRemote(rsc, p);
			if(rmap!=null){
				rebl=rmap.get("success")==null?false:((Boolean)rmap.get("success")).booleanValue();
			}
		}catch(Exception x){
			logger.error("error -- HessianClient.doRemoteServer :" + url + " : " + p.get("peizhiItem") + " : " + p.get("productionId"));
			Iterator it=p.keySet().iterator();
			while(it.hasNext()){
				String mk=(String)it.next();
				logger.error("error -- HessianClient.doRemoteServer param :" + mk + " : " + p.get(mk));
			}
			logger.error("error -- HessianClient.doRemoteServer :"  + x.getMessage());
			x.printStackTrace();
		}
		return rebl;
	}
	
	/**
	 * 供外部调用的测试服务
	 * @param serverKey 服务器标识
	 * @param p 参数
	 * @return
	 */
	public static boolean testRemoteServer(String serverKey,Map p){
		boolean rebl=false;
		Map smap=HessianRemoteParamters.getRemoteServer(serverKey);
		rebl=testRemoteServer(smap);
		return rebl;
	}
	
	public static void main(String[] args) throws Exception{
		/*HessianProxyFactory factory = new HessianProxyFactory();
		String url = ("http://127.0.0.1:8888/ZDesk/remoteCall");
		HessianServerImpDef server = (HessianServerImpDef)factory.create(HessianServerImpDef.class, url);
		Map p=new HashMap();
		p.put("mgs", "111111");
		RemoteServerCall dbau=new TestRemoteConnect();
		p=server.execRemote(dbau, p);
		System.out.println(p.get("success"));
		*/
		/*测试 测试类
		boolean rebl=HessianClient.testRemoteServer("ZKM",null);
		System.out.println(rebl);
		*/
		//业务数据事务操作
		Map p=new HashMap();
		//远程调用接口
		p.put(DBAffairsImpDef.CLASS_KEY, "com.zinglabs.remote.server.impl.RS_DBAffairs.DBAffairsDemo");
		Map map=doDBAffairsUpdateRemoteServer("ZKM",p);
		Iterator it=map.keySet().iterator();
		while(it.hasNext()){
			String key=(String)it.next();
			System.out.println(key + " : " + map.get(key));
		}
	}
}
