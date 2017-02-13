package com.zinglabs.apps.commons;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.jdbc.SqlBuilder;
import org.apache.ibatis.jdbc.SelectBuilder;
import org.apache.ibatis.jdbc.SqlRunner;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

@SuppressWarnings("unchecked")
public class ZKMCommonsCurdService extends BaseService {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");

	/**
	 * 通用分页查询
	 * 
	 * @param map
	 *            参数集
	 * @return
	 * @throws RuntimeException
	 */
	public Map getListForPaging(Map map) throws Exception {
		Map result = new HashMap();

		Map commonInfo = this.reMoveCommonInfo(map);

		HashMap dmap = (HashMap) spellMyIbatisSelectMap(map);

		int total = spelllCommonSelectInfoMap(commonInfo, dmap);
        
		String namespance=commonInfo.get("nameSpace")+"."+commonInfo.get("nameSpaceId");
		List list = getServiceSqlSession().db_selectList(namespance, dmap);

		result.put("total", total);

		result.put("rows", list);

		return result;
	}

	/**
	 * 
	 * @param commonInfo
	 *            除去spellMyIbatisSelectMap函数需要用的参数 剩余的参数
	 * @param dmap
	 *            经过spellMyIbatisSelectMap整理后的map
	 * @return
	 */
	private int spelllCommonSelectInfoMap(Map commonInfo, HashMap dmap) {
		SpellCommonInfoInsertMap(commonInfo, dmap);
		String orderBy = (String) commonInfo.get("orderBy");
		if (!StringUtils.isEmpty(orderBy)) {
			dmap.put("orderBy", orderBy);
		}
		String namespace="count";
		int total = ((Integer) getServiceSqlSession().db_selectOne(namespace, dmap)).intValue();
		String limit = (String) commonInfo.get("limit");
		if (!StringUtils.isEmpty(limit)) {
			dmap.put("limit", limit);
		}
		return total;
	}

	/**
	 * 自定义mybatis配置文件的分页查询
	 * 
	 * @param map
	 * @return
	 * @throws RuntimeException
	 */
	public Map getListPagingForComplex(Map map) throws Exception {
		Map result = new HashMap();
		Map dmap = new HashMap();
		spellCommonSelectForComPlexMap(map, dmap);
		String baseNamespace=(String)dmap.get("nameSpace");
		String baseNamespaceId=(String) dmap.get("nameSpaceId");
		if (dmap.get("countId") != null) {
			String namespace=baseNamespace+"."+dmap.get("countId");
			int total = ((Integer)getServiceSqlSession().db_selectOne(namespace, map)).intValue();
			result.put("total", total);
		}
		List list = getServiceSqlSession().db_selectList(baseNamespace+"."+baseNamespaceId, map);
		result.put("rows", list);
		return result;
	}
	/**
	 * 自定义mybatis增改
	 * 
	 * @param map
	 * @return
	 * @throws RuntimeException
	 */
	public Map commonupdate(Map map) throws Exception {
		Map result = new HashMap();
		Map dmap = new HashMap();
		spellCommonSelectForComPlexMap(map, dmap);
		String baseNamespace=(String)dmap.get("nameSpace");
		String baseNamespaceId=(String) dmap.get("nameSpaceId");
		if (dmap.get("countId") != null) {
			String namespace=baseNamespace+"."+dmap.get("countId");
			int total = ((Integer)getServiceSqlSession().db_insert(namespace, map)).intValue();
			result.put("total", total);
		}
		int list = getServiceSqlSession().db_insert(baseNamespace+"."+baseNamespaceId, map);
		result.put("rows", list);
		return result;
	}

	/**
	 * 
	 * @param map
	 * @param dmap
	 */
	private void spellCommonSelectForComPlexMap(Map map, Map dmap) {
		if (map.get("nameSpace") != null) {
			dmap.put("nameSpace", map.get("nameSpace"));
			map.remove("nameSpace");
		}
		if (map.get("nameSpaceId") != null) {
			dmap.put("nameSpaceId", map.get("nameSpaceId"));
			map.remove("nameSpaceId");

		}
		if (map.get("countId") != null) {
			dmap.put("countId", map.get("countId"));
			map.remove("countId");

		}
	}

	/**
	 * 新建
	 * 
	 * @param map
	 */
	public void insert(Map map) throws Exception {
	
	        String uuid=(String)map.get("uuid");
	        if("true".equals(uuid)){
				map.put(map.get("primaryKey"), new RandomGUID().toString());
			}else{
				map.remove(map.get("primaryKey"));
			}
			map.remove("uuid");

		Map commonInfo = this.reMoveCommonInfo(map);
		HashMap dmap = (HashMap) spellMyIbatisInsertMap(map, true);
		String namespace=commonInfo.get("nameSpace")+"."+commonInfo.get("nameSpaceId");
		SpellCommonInfoInsertMap(commonInfo, dmap);
		
		getServiceSqlSession().db_insert(namespace, dmap);
	}

	private void SpellCommonInfoInsertMap(Map commonInfo, HashMap dmap) {
		String tableName = (String) commonInfo.get("tableName");
		if (!StringUtils.isEmpty(tableName)) {
			dmap.put("table", tableName);
		}
	}

	/**
	 * 修改
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void update(Map map) throws Exception {
		Map commonInfo = this.reMoveCommonInfo(map);
		Map dmap = spellMyIbatisUpdateMap(map);
		spellCommonInfoUpdateMap(commonInfo, dmap);
		String namespace=commonInfo.get("nameSpace")+"."+commonInfo.get("nameSpaceId");
		getServiceSqlSession().db_update(namespace, dmap);
	}
	/**
	 * 通用修改的后端借口
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void updateback(Map map) throws Exception {
		//map.put("nameSpaceId", "update") ;
		//map.put("nameSpace", "com.zinglabs.apps.commons.pojo") ;
		Map commonInfo = this.reMoveCommonInfo(map);
		Map dmap = spellMyIbatisUpdateMap(map);
		spellCommonInfoUpdateMap(commonInfo, dmap);
		try{
			String namespace=commonInfo.get("nameSpace")+"."+commonInfo.get("nameSpaceId");
			getServiceSqlSession().db_update(namespace, dmap);
		}catch(Exception e){e.printStackTrace();}
	}
	
	private void spellCommonInfoUpdateMap(Map commonInfo, Map dmap) {
		String tableName = (String) commonInfo.get("tableName");
		String primaryKey = (String) commonInfo.get("primaryKey");
		String primaryKeyValue = (String) commonInfo.get("primaryKeyValue");
		if (!StringUtils.isEmpty(tableName)) {
			dmap.put("table", tableName);
		}
		if (!StringUtils.isEmpty(primaryKey)) {
			dmap.put("primaryKey", primaryKey);
		}
		if (!StringUtils.isEmpty(primaryKeyValue)) {
			dmap.put("primaryKeyValue", primaryKeyValue);
		}
	}

	/**
	 * 删除
	 * 
	 * @param map
	 */
	public void delete(HashMap map) throws Exception {
		Map commonInfo = this.reMoveCommonInfo(map);
		Map dmap = new HashMap();
		spellCommonInfoUpdateMap(commonInfo, dmap);
		String namespace=commonInfo.get("nameSpace")+"."+commonInfo.get("nameSpaceId");
		getServiceSqlSession().db_delete(namespace, dmap);

	}


	/**
	 * 过滤掉spellMyIbatis方法不需要的参数,把这些参数整理到另一个HashMap中
	 * 
	 * @param map
	 * @return
	 */
	private Map reMoveCommonInfo(Map map) {
		Map dmap = new HashMap();

		if (map.get("tableName") != null) {
			dmap.put("tableName", map.get("tableName"));
			map.remove("tableName");
		}
		if (map.get("nameSpace") != null) {
			dmap.put("nameSpace", map.get("nameSpace"));
			map.remove("nameSpace");
		}
		if (map.get("nameSpaceId") != null) {
			dmap.put("nameSpaceId", map.get("nameSpaceId"));
			map.remove("nameSpaceId");
		}
		if (map.get("orderBy") != null) {
			dmap.put("orderBy", map.get("orderBy"));
			map.remove("orderBy");
		}
		if (map.get("limit") != null) {
			dmap.put("limit", map.get("limit"));
			map.remove("limit");
		}
		if (map.get("primaryKey") != null) {
			dmap.put("primaryKey", map.get("primaryKey"));
			map.remove("primaryKey");
		}
		if (map.get("primaryKeyValue") != null) {
			dmap.put("primaryKeyValue", map.get("primaryKeyValue"));
			map.remove("primaryKeyValue");
		}
		if (map.get("uuid") != null) {
			map.remove("uuid");
		}
		return dmap;
	}

	public List getList(Map map) throws Exception {
		Map commonInfo = this.reMoveCommonInfo(map);
		HashMap dmap = (HashMap) spellMyIbatisSelectMap(map);
		SpellCommonInfoInsertMap(commonInfo, dmap);
		String orderBy = (String) commonInfo.get("orderBy");
		if (!StringUtils.isEmpty(orderBy)) {
			dmap.put("orderBy", orderBy);
		}
		String limit = (String) commonInfo.get("limit");
		if (!StringUtils.isEmpty(limit)) {
			dmap.put("limit", limit);
		}
		String namespace=commonInfo.get("nameSpace")+"."+commonInfo.get("nameSpaceId");
		return getServiceSqlSession().db_selectList(namespace, dmap);
	}

	
	/**通用插入方法
	 * @author Guozhiyuan
	 * 
	 * */
	public String commonInsert(HashMap map){
		
		String uuid=(String)map.get("uuid");
		String primaryKey = (String) map.get("primaryKey");
		//主键的值
		String primaryValue="";
	    //是否自增长
		boolean useGeneratedKeySupport=false;
		if("true".equals(uuid)){
			primaryValue=new RandomGUID().toString();
			map.put(primaryKey,primaryValue);
		}else{
				map.remove(primaryKey);
				useGeneratedKeySupport=true;
		}
		//要执行的插入sql
		String insertSql="";
		//拼装开始
		SqlBuilder.BEGIN();
		//表名
		SqlBuilder.INSERT_INTO((String)map.get("tableName"));
		//过滤特殊值 非表字段
		reMoveCommonInfo(map);
		//拼装插入语句
		
		Iterator it = map.keySet().iterator();
		while(it.hasNext()){
     		String key=(String)it.next();
     		if(key==null||key.equals("null")||key.equals("zkm_userName")||key.equals("r")){
     			continue;
     		}
     		StringBuffer values=new StringBuffer("'");
     		values=values.append(map.get(key));
     		values.append("'");
     		SqlBuilder.VALUES(key, values.toString());	
		} 
		
		try {
			//获取Connection 对象
			//Connection 修改
			Connection conn = DAOTools.getConnectionOutS("ZDesk");
			//初始化mybait SqlRunner对象
			SqlRunner sqlRunner =new SqlRunner(conn);
			
			//设置主键为自增长类型
			if(useGeneratedKeySupport){
				sqlRunner.setUseGeneratedKeySupport(true);
			}
			insertSql=SqlBuilder.SQL();
            //System.out.println(insertSql);
			//TextSqlNode node =new TextSqlNode(insertSql);     
			//DynamicSqlSource s=new DynamicSqlSource(getServiceSqlSession().getConfiguration(), node);    
			//此外对于静态SQL，ibatis还提供了StaticSqlSource  
			// BoundSql sql2 = s.getBoundSql(map);  
			 List list =new ArrayList();
			 int id=sqlRunner.insert(insertSql, list.toArray(new Object[] {}));
			 primaryValue=""+id;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			primaryValue="";
		}  
	
	
		return primaryValue;
	}
	/**
	 * 通用 拼装sql
	 * @author Guozhiyuan
	 * @param <T>
	 * @param map 参数  key 
	 */
	public   String  commonSearchSpell(Map map){
		SelectBuilder.BEGIN();
		//TODO 可以添加条件 	SelectBuilder.SELECT_DISTINCT(columns);
		if(map.get("select")!=null&&!"".equals(map.get("select"))){
			SelectBuilder.SELECT((String)map.get("select"));
		}else{
			SelectBuilder.SELECT("*");
		}
		//表名
		String tableName=map.get("tableName").toString();
		//TODO 防止注入   
		//tableName.replace("'", "");
		if(tableName!=null&&!"".equals(tableName)){
			SelectBuilder.FROM(tableName);
		}else{
			return "";
		}
        //TODO 如有特殊的进行特殊处理  columns 一般  ct 特殊
		String columnsStr=(String)map.get("columns");
		Map<String, String> columns=null;
		try {
			//字符串转json
			if(columnsStr!=null&&!"null".equals(columnsStr)&&columnsStr.length()>0){
				columns = (Map<String,String>) JsonUtils.stringToJSON(columnsStr, Map.class);
			}
			
			if(columns!=null&&columns.size()>0){
				Iterator<String> it=columns.keySet().iterator();
				while(it.hasNext()){
					String key=it.next();
				    String value=columns.get(key);
				    StringBuffer str =new StringBuffer();
				    str.append(key).append("=").append("'").append(value).append("'");
				    SelectBuilder.WHERE(str.toString());
				}
			}
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//特殊条件  格式 key 条件 
		String specialColumns=(String)map.get("specialColumns");
		if(specialColumns!=null&&specialColumns.length()>0){
			String special[] =specialColumns.split(",");
			for(int i=0;i<special.length;i++){
				//占位符
				if(!"".equals(special[i]))
				SelectBuilder.WHERE(special[i]+" ?");
			}
		}
		//左链接
		String leftjoin=(String)map.get("leftjoin");
		if(leftjoin!=null&&leftjoin.length()>0){
			SelectBuilder.LEFT_OUTER_JOIN(leftjoin);
		}
		//右链接
		String rightjoin=(String)map.get("rightjoin");
		if(rightjoin!=null&&rightjoin.length()>0){
			SelectBuilder.RIGHT_OUTER_JOIN(rightjoin);
		}
		//交叉连接
		String outjoin=(String)map.get("outjoin");
		if(outjoin!=null&&outjoin.length()>0){
			SelectBuilder.JOIN(outjoin);
		}
		String innerjoin=(String)map.get("innerjoin");
		if(innerjoin!=null&&innerjoin.length()>0){
			SelectBuilder.INNER_JOIN(innerjoin);
		}
		//添加排序
		if(map.get("orderBy")!=null && !"".equals(map.get("orderBy"))){
			SelectBuilder.ORDER_BY(map.get("orderBy").toString());
		}
		return SelectBuilder.SQL();

	}
	/**
	 * 通用查询 
	 * @param map
	 */
	public  List<Map>commonSearch(Map map){
		String sql=commonSearchSpell(map);
		logger.debug("通用查询sql:"+sql);
		System.out.println("通用查询sql:"+sql);
		String params[] = null;
		String p=(String)map.get("teshucanshu");
		if(p!=null&&p.length()>0){
			params=p.split(",");
		}
		try {
			List<Map> dlist=DAOTools.queryMap(sql,params,"ZDesk");
			return dlist;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("通用查询"+e.getMessage());
			
		}
		/*Connection conn = getServiceSqlSession().getConnection();
		SqlRunner sqlRunner =new SqlRunner(conn);
		List<Map<String,Object>> list;
		try {
			String s1=new String();
			list=sqlRunner.selectAll(sql,s1,s1);
			//sqlRunner.
			//conn.commit(); 
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}*/
		return new ArrayList<Map>(); 	
	} 
	@Override
	public AppSqlSessionDbid getServiceSqlSession(){
		return(AppSqlSessionDbid)this.getBean("zkmCommonsSqlSession");
	}

	

}
