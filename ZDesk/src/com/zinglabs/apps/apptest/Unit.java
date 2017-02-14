package com.zinglabs.apps.apptest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zinglabs.db.DAOTools;

/**
 * TestService单元测试
 * 
 * 
 * @author Guozhiyuan
 *
 */
@SuppressWarnings("unchecked")
public class Unit {
	public static void main(String[] args) {
		
//      单元测试通过初始化spring配置文件
//		ApplicationContext context=AppSpringUtils.getContextAsClassPath(new String[]{"appConf/applicationContext.xml","classpath:com/zinglabs/apps/**/defXml/*-beans.xml"});
//		TestService ts2 = (TestService) context.getBean("testService");
//		ts2.createTable();	
		
//		单元测试通过实例化对象
		TestService ts=new TestService();
		//创建表
		ts.createTable();
		
		Map <String,String>insertParams=new HashMap<String,String>();
	

		//增
		String id="";
		try {
			//分布式id自增长解决方案
			id = DAOTools.getTableId("app_test", "ZKM",null,null,null);
			insertParams.put("id", id);
			insertParams.put("name", "zhiyuan.guo");
			insertParams.put("money", "1000000");
			insertParams.put("dateTime", "2015-01-19 17:09:28");
			ts.testAdd(insertParams);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//查询结果
		showAllData(ts.testSelectAll());
		
		
		
		//改
		Map <String,String>updateParam=new HashMap<String, String>();
		updateParam.put("id",id);
		updateParam.put("name","郭志远");
		updateParam.put("money","11111");
		ts.testUpdate(updateParam);
		//查询结果
		showAllData(ts.testSelectAll());
		
		
		
		
		
		//删
		Map <String,String>deleteParam=new HashMap<String, String>();
		deleteParam.put("id",id);		
		ts.testDelete(deleteParam);
		//查询结果
		showAllData(ts.testSelectAll());
		
		
        //批量操作 插入
	
		try {
			
			 List<Object> batchList=new ArrayList<Object>();
			 Map batchInsert1=new HashMap();
			 String id1 = DAOTools.getTableId("app_test", "ZKM",null,null,null);
			 batchInsert1.put("id", id1);
			 batchInsert1.put("name", "zhiyuan.guo");
			 batchInsert1.put("money", "1000000");
			 batchInsert1.put("dateTime", "2015-01-19 17:09:28");
			 
			 Map batchInsert2=new HashMap();
			 String id2 = DAOTools.getTableId("app_test", "ZKM",null,null,null);
			 batchInsert2.put("id", id2);
			 batchInsert2.put("name", "xiaoming");
			 batchInsert2.put("money", "1000");
			 batchInsert2.put("dateTime", "2015-01-19 17:09:28");
			 
			 batchList.add(batchInsert1);
			 batchList.add(batchInsert2);
			 ts.testBatchExecute(batchList);
			 //展示数据库记录
			 showAllData(ts.testSelectAll()); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		 //selectOne
		 Map selectOneParam=new HashMap();
		 selectOneParam.put("name", "xiaoming");
		 int tid=ts.testSelectOne(selectOneParam);
		 System.out.println(tid);
		 
		 
		 
		 //事物性操作
		 Map tmap=new HashMap();
		 tmap.put("id", tid);
		 ts.transaction(tmap);


	
	}
	
	public static void showAllData(List list){
	  for(int i=0;i<list.size();i++)
		  System.out.println("数据"+i+"条:"+list.get(i));
	}
}
