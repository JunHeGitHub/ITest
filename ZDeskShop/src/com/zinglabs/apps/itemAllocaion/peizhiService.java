package com.zinglabs.apps.itemAllocaion;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.db.DAOTools;
public class peizhiService  extends BaseService {
public static Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	

	
	/**增改example
	 * 对应的配置，如下例子
	 * 
	 *   
	     <insert id="app_insert" parameterType="java.util.Map">
             insert into `app_test` (`id`,`name`,`money`,`dateTime`,`createTime`) values(#{id},#{name},#{money},#{dateTime},now())
         </insert>
	 * @throws Exception 
	 */
		
	public void Add(Map map) throws Exception{
		String idStr = DAOTools.getTableId("DataItemAllocation","ZKM",null,null,null);
		map.put("id", Integer.parseInt(idStr));
		getServiceSqlSession().db_insert("peizhi_insert",map);
	}
	/**修改example
	 *  对应的配置，如下例子
	 *  <update id="app_update" parameterType="java.util.Map">
           update `app_test` set `name`=#{name},`money`=#{money}  where `id`=#{id}
        </update>
	 * 
	 * 
	 * @param map
	 */

	public void Update(Map map){
		getServiceSqlSession().db_update("peizhi_update",map);
	}				
	/**
	 * 
	 * 实现Baseservice 里的抽象方法
	 */
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid)this.getBean("peizhi_SqlSession");
	}

}
