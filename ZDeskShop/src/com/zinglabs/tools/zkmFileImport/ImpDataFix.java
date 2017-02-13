package com.zinglabs.tools.zkmFileImport;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.log.LogUtil;


public class ImpDataFix {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM"); 
	
	private Map<String,String> fixMap=null;
	//public String [] keys={"总中心_个人外汇结算账户","总中心_个人人民币存款业务","总中心_个人客户预约转账服务","总中心_个人存款证明","总中心_个人存款挂失","总中心_代缴储蓄存款利息所得征收个人所得税","总中心_储蓄国债（电子式）"};
	//public String [] values={"个人外汇结算账户服务","储蓄_/_个人业务","其他业务","存款证明","个人存款挂失业务","代缴储蓄存款利息所得税","储蓄国债(电子式)"};
	public String [] keys={"对私（含）中银智富权益投资信托理财","对私（含）委托性理财--中银货币市场理财计划","对私（含）委托性理财--中银货币理财计划之日积月累","对私（含）委托性理财——中银代客境外理财"};
	public String [] values={"资产管理_/_中银智富权益投资信托理财（对公）","资产管理_/_中银货币市场理财计划（对公）","资产管理_/_中银货币理财计划之日积月累（对公）","资产管理_/_代客境外理财（对公）"};
	ImpDataFix(){
		fixMap=new HashMap<String,String>();
		
		if(keys.length == values.length){
			for(int i=0;i<keys.length;i++){
				fixMap.put(keys[i], values[i]);
			}
		}else{
			LogUtil.error("ImpDataFix ---- keys " + keys.length + "  values " + values.length + " 数量不等.",SKIP_Logger);
		}
	}
	
	public String getFixValuse(String key){
		return fixMap.get(key);
	}
	
	public static void main(String[] args){
		ImpDataFix idf=new ImpDataFix();
		System.out.println(idf.keys.length + "  " + idf.values.length);
	}
}
