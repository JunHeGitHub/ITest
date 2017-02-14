package com.zinglabs.apps.apptest;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class TestService extends BaseService{
	public static Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	
	/**
	 *  创建表
	 *  测试表结构
	 * 	CREATE TABLE IF NOT EXISTS `app_test` (
		  `id` int(11) NOT NULL AUTO_INCREMENT,
		  `name` varchar(255) DEFAULT NULL,
		  `money` double(255,0) DEFAULT NULL,
		  `dateTime` datetime DEFAULT NULL,
		  `createTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
		  PRIMARY KEY (`id`)
		) ENGINE=InnoDB  DEFAULT CHARSET=gbk;
	 *  
	 * 
	 * 
	 * 
	 */

	public void createTable(){
		getServiceSqlSession().db_insert("createTalbe");
	}
	/**增改example
	 * 对应的配置
	 * 
	 *   
	     <insert id="app_insert" parameterType="java.util.Map">
             insert into `app_test` (`id`,`name`,`money`,`dateTime`,`createTime`) values(#{id},#{name},#{money},#{dateTime},now())
         </insert>
	 */
	public void testAdd(Map map){
		getServiceSqlSession().db_insert("app_insert",map);
	}
	/**删出example  可以配置String  
	 *  
	 *  <delete id="app_delete" parameterType="java.util.Map">
          delete from `app_test` where id=#{id}
        </delete>
	 * @param map
	 */
   
	public void testDelete(Map map){
		getServiceSqlSession().db_delete("app_delete",map);
	}
	/**
	 *  对应的配置
	 *  <update id="app_update" parameterType="java.util.Map">
           update `app_test` set `name`=#{name},`money`=#{money}  where `id`=#{id}
        </update>
	 * 
	 * 
	 * @param map
	 */
	public void testUpdate(Map map){
		getServiceSqlSession().db_update("app_update",map);
	}
	/**
	 * 查询 返回List
	 *  <select id="app_select" parameterType="java.util.Map"  resultType="java.util.Map">
          select * from `app_test`
        </select>
	 * 
	 * 
	 * 
	 * @param map
	 * @return
	 */
	public List testSelect(Map map){
		List list=getServiceSqlSession().db_selectList("app_select",map);
		return list;
	}
	
	public int testSelectOne(Map map){	
		return ((Integer)getServiceSqlSession().db_selectOne("app_selectOne", map)).intValue();
	}
	public List testSelectAll(){
		List list=getServiceSqlSession().db_selectList("app_select");
		return list;
	}
	/**
	 * 事物操作 需要数据库支持
	 * 目前只支持单库  
	 * 
	 */

	public void  transaction(Map map){
		getServiceSqlSession().setAutoCommit(false);
		try{
			testDelete(map);
			
			throw new Exception("制造异常");

		}
		catch (Exception e) {
			System.out.println("回滚");
			getServiceSqlSession().rollback();
		}finally{
			
			getServiceSqlSession().commit();
			
		}
	
		
	}
	
	/**
	 * 批量操作 增/改/删 都可以使用此方法
	 * 
	 * params 参数
	 * 
	 */
	
	public void testBatchExecute(List<Object>params){
		try {
			//说明: 批量是 一个SQL  提交多组参数 
			// app_insert：  mybait 对应的sql
			// params ：     参数集合
			// dbid  ： 取到相应的数据连接池
			// dbid 配置 securityDB.DataItemAllocation中
			
		 /*	下面是dbid等于ZKM的配置示例
		  * id  peizhiItem peizhiItemValue
		  * 184	dbip	   128.128.128.128	securityDB数据库IP	    database	 ZKM数据库配置	ZKM	2014-06-16 16:31:19
			185	dbo		                    securityDB数据库dbo	    database	 ZKM数据库配置	ZKM	2013-03-15 08:25:41
			186	id	       ZKM             	securityDB数据库调用标识   database     ZKM数据库配置	ZKM	2013-03-15 08:25:43
			187	name	   securityDB   	securityDB数据库名称	    database	 ZKM数据库配置	ZKM	2015-01-04 06:27:17
			188	password   12	            securityDB数据库密码	    database	 ZKM数据库配置	ZKM	2013-03-15 08:25:46
			189	port	   3306	            securityDB数据库端口   	database	 ZKM数据库配置	ZKM	2013-03-15 08:25:48
			190	type	   mysql	        securityDB数据库类型	    database	 ZKM数据库配置	ZKM	2013-03-15 08:25:49
			191	userName   zinglabs	        securityDB数据库用户名	database	 ZKM数据库配置	ZKM	2013-03-15 08:25:56
			192	route	   128.128.128.128	securityDB备份数据库       database	 ZKM数据库配置	ZKM	2014-06-16 16:31:19
			193	charset	   UTF-8	        securityDB数据库编码	    database	 ZKM数据库配置	ZKM	2014-03-05 17:43:16
			*/
			//调用dbid 等于ZDesk的连接
			int results[]=getServiceSqlSession().batchExecute("app_insert", params, "ZKM");
			for(int result:results){
				System.out.println(result);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * 实现Baseservice 里的抽象方法
	 */
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid)this.getBean("apptest_SqlSession");
	}
}
