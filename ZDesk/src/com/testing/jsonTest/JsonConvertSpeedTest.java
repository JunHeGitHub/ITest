package com.testing.jsonTest;
import java.util.*;
import java.sql.*;

import org.springframework.context.ApplicationContext;

import net.sf.json.JSONSerializer;
import atg.taglib.json.util.*;

import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.base.core.utils.AppSpringUtils;
import com.zinglabs.db.DAOTools;
import com.zinglabs.tools.zkmDataTools.ToolsBase;

public class JsonConvertSpeedTest {
	
	String[] conStrs={"com.mysql.jdbc.Driver","jdbc:mysql://128.128.128.128:3306/securityDB?user=zinglabs&password=12&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=round&useOldAliasMetadataBehavior=true","zinglabs","12"};
	
	ApplicationContext context;
	
	List datalist;
	
	public JsonConvertSpeedTest() throws Exception{
		
		context=getSpringContext();
		//datalist=getTestData();
		//datalist.addAll(getTestData());
		//datalist.addAll(getTestData());
		//datalist.addAll(getTestData());
		//datalist.addAll(getTestData());
		//getTestData_ibatis();
		//System.out.println("\n data size : " + datalist.size());
	}
	
	public ApplicationContext getSpringContext(){
		return AppSpringUtils.getApplicationContext();
	}
	
	public List getTestData() throws Exception{
		long d1=System.currentTimeMillis();
		
		String sql="select * from zkmInfoBaseDisponseFB";
		Connection conn=ToolsBase.getConnection(conStrs);
		List list=ToolsBase.queryMap(sql, null, conn);
		
		long d2=System.currentTimeMillis();
		
		//System.out.println("get Data Size: " + list.size() + " use time: " + (d2-d1));
		return list;
	}
	
	public List getTestData_ibatis() throws Exception{
		AppSqlSession ass=(AppSqlSession)context.getBean("apptest_SqlSession");
		long d1=System.currentTimeMillis();
		
		List list=ass.selectList("json_test_getTestData");
		
		long d2=System.currentTimeMillis();
		
		//System.out.println("get Data Size: " + list.size() + " use time: " + (d2-d1));
		return list;
	}
	
	public void jsonLibTest() throws Exception{
		List list=getTestData();
		//List list=getTestData_ibatis();
		
		long d1=System.currentTimeMillis();
		String jsonStr=JSONSerializer.toJSON(list).toString();
		long d2=System.currentTimeMillis();
		
		System.out.println("jsonLib convert use Time : " + (d2-d1));
	}
	
	public int f19=0;
	public int f24=0;
	
	public void jacksonTest()throws Exception{
		//List list=getTestData();
		//List list=getTestData_ibatis();
		List list=datalist;
		
		
		
		long d3=System.currentTimeMillis();
		org.codehaus.jackson.map.ObjectMapper jsonMapper19 = new org.codehaus.jackson.map.ObjectMapper();
		String jsonStr19=jsonMapper19.writeValueAsString(list);
		long d4=System.currentTimeMillis();
		
		System.out.println("jackson 1.9 convert use Time : " + (d4-d3));
		
		Thread.sleep(100);
		
		long d1=System.currentTimeMillis();
		com.fasterxml.jackson.databind.ObjectMapper jsonMapper24 = new com.fasterxml.jackson.databind.ObjectMapper();
		String jsonStr24=jsonMapper24.writeValueAsString(list);
		long d2=System.currentTimeMillis();
		
		System.out.println("jackson 2.4 convert use Time : " + (d2-d1));
		
		if((d4-d3) <= (d2-d1)){
			f19++;
		}else{
			f24++;
		}
	}
	
	public void daoToolsTest() throws Exception{
		String sql="select * from zkmInfoBaseDisponseFB";
		long b1=System.currentTimeMillis();
		String str=DAOTools.easyQueryJSON(sql,"ZDesk","zkmInfoBaseDisponseFB",true,null);
		long b2=System.currentTimeMillis();
		System.out.println("daotools easyQueryJSON: " + (b2-b1));
	}
	
	public void daoToolsTest1() throws Exception{
		String sql="select * from zkmInfoBaseDisponseFB";
		long b1=System.currentTimeMillis();
		List list=DAOTools.queryMap(sql, "ZDesk");
		org.codehaus.jackson.map.ObjectMapper jsonMapper= new org.codehaus.jackson.map.ObjectMapper();
		String str=jsonMapper.writeValueAsString(list);
		long b2=System.currentTimeMillis();
		System.out.println("daotools queryMap: " + (b2-b1));
	}
	
	public void daoToolsTest2() throws Exception{
		long b1=System.currentTimeMillis();
		List list=getTestData_ibatis();
		org.codehaus.jackson.map.ObjectMapper jsonMapper= new org.codehaus.jackson.map.ObjectMapper();
		String str=jsonMapper.writeValueAsString(list);
		long b2=System.currentTimeMillis();
		System.out.println("myibatis query: " + (b2-b1));
	}
	
	public static void main(String[] args) throws Exception{
		DAOTools.initAllStatic();
		DAOTools.hasInitStatic=true;
		JsonConvertSpeedTest jcst=new JsonConvertSpeedTest();
		
		for(int i=0;i<100;i++){
			
			System.out.println("\n");
//			
//			jcst.daoToolsTest();
//			jcst.daoToolsTest1();
//			jcst.daoToolsTest2();
			
//			System.out.println("\n");
//			
			jcst.daoToolsTest2();
			jcst.daoToolsTest1();
			jcst.daoToolsTest();
			
		}
//		for(int i=0;i<100;i++){
//			System.out.println("\n");
//			
////			long d1=System.currentTimeMillis();
////			jcst.jsonLibTest();
////			long d2=System.currentTimeMillis();
////			System.out.println("jsonLibTest use Time : " + (d2-d1));
////			
////			System.out.println("\n");
//			
//			//d1=System.currentTimeMillis();
//			jcst.jacksonTest();
//			//d2=System.currentTimeMillis();
//			//System.out.println("jacksonTest use Time : " + (d2-d1));
//			
//			}
//		
//		System.out.println("\n " + jcst.f19 + "  " + jcst.f24);
	}
}
