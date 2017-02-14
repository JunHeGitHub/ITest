package com.zinglabs.base.core.myIbatisSupport.dataSource;

import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.datasource.DataSourceFactory;

import com.zinglabs.base.core.frame.DBConfigLoader;

//TODO DBCP未实现
public class DBCPDataSourceFactory implements DataSourceFactory {
	private BasicDataSource datasource = null;

	public DBCPDataSourceFactory() {
		this.datasource = new BasicDataSource();
	}

	public DataSource getDataSource() {
		return datasource;
	}

	public void setProperties(Properties ps) {
		
		datasource.setDriverClassName(ps.getProperty("driverclassname"));
		datasource.setUsername(ps.getProperty("username"));
		datasource.setUrl(ps.getProperty("url"));
		datasource.setPassword(ps.getProperty("password"));
		datasource.setDefaultAutoCommit(true);
		datasource.setInitialSize(20);
		datasource.setMaxActive(100);
		datasource.setMaxIdle(20);
		datasource.setMinIdle(20);
		datasource.setMaxWait(3000);
		datasource.setTimeBetweenEvictionRunsMillis( 60 * 60 * 1000);
		datasource.setMinEvictableIdleTimeMillis( 5 * 60 * 1000);
		datasource.setLogAbandoned(true);
		datasource.setRemoveAbandonedTimeout(180);
		datasource.setValidationQuery("select 1");
		datasource.setTestOnBorrow(true);
		datasource.setTestWhileIdle(true);
		datasource.setTestOnReturn(true);
		datasource.setNumTestsPerEvictionRun(5);
	}

	public static Properties propertiesConvert(Map<String,String> map,DBConfigLoader loader){
		Properties ps=new Properties();
		String user=map.get("userName")==null?"":(String)map.get("userName");
		String pwd=map.get("password")==null?"":(String)map.get("password");
		String ip=map.get("dbip")==null?"":(String)map.get("dbip");
		String port=map.get("port")==null?"":(String)map.get("port");
		String database=map.get("name")==null?"":(String)map.get("name");
		String dbtype=map.get("type")==null?"":(String)map.get("type");
		String autoCommit=map.get("autoCommit")==null?"true":(String)map.get("autoCommit");
		
		if(autoCommit.toUpperCase().equals("TRUE")){
			ps.setProperty("defaultautocommit", "1");
		}else{
			ps.setProperty("defaultautocommit", "0");
		}
		
		String logAbandoned=map.get("logAbandoned")==null?"true":(String)map.get("logAbandoned");
		if(logAbandoned.toUpperCase().equals("TRUE")){
			ps.setProperty("logAbandoned", "1");
		}else{
			ps.setProperty("logAbandoned", "0");
		}
		
		String removeAbandoned=map.get("removeAbandoned")==null?"true":(String)map.get("removeAbandoned");
		if(removeAbandoned.toUpperCase().equals("TRUE")){
			ps.setProperty("removeAbandoned", "1");
		}else{
			ps.setProperty("removeAbandoned", "0");
		}
		
		String dbdriver= loader.getDbDriver();
		if(user.length()>0 && pwd.length()>0 && ip.length()>0 && port.length()>0 && database.length()>0 && dbtype.length()>0 && dbdriver.length()>0){
			String url=loader.genUrl(dbtype,ip,port,database,user,pwd);
			ps.setProperty("driverclassname",dbdriver);
			ps.setProperty("url", url);
			ps.setProperty("username", user);
			ps.setProperty("password", pwd);
		}
		
		return ps;
	}
}
