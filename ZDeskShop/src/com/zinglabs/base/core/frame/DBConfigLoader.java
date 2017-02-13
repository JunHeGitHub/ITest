package com.zinglabs.base.core.frame;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.db.CryptoTools2;
import com.zinglabs.log.LogUtil;

public class DBConfigLoader {
	
	public String confPath;
	public Properties dbconf;
	public List<HashMap<String,String>> dbList;
	public HashMap<String,HashMap<String,String>> dbMap;
	
	private String userKey;
	private String pwdKey;
	private String ipKey;
	private String portKey;
	private String dbtypeKey;
	private String databaseKey;
	private String dbDriver;
	/**
	 * 在任意时间存在的活动（也就是正在使用）连接的数量。默认值：10 
	 */
	private String poolMaximumActiveConnections;
	/**
	 * 任意时间存在的空闲连接数
	 */
	private String poolMaximumIdleConnections;
	/**
	 * 在被强制返回之前，池中连接被检查的时间。默认值：20000 毫秒（也就是 20 秒） 
	 */
	private String poolMaximumCheckoutTime;
	/**
	 * 这是给连接池一个打印日志状态机会的低层次设置，还有重新尝试获得连接，
	 * 这些情况下往往需要很长时间（为了避免连接池没有配置时静默失败）。
	 * 默认值：20000 毫秒（也就是 20 秒）
	 */
	private String poolTimeToWait;
	/**
	 * 发送到数据的侦测查询，用来验证连接是否正常工作，并且准备接受请求。
	 * 默认是“NO  PING QUERY  SET”，这会引起许多数据库驱动连接由一个错误信息而导致失败。
	 */
	private String poolPingQuery;
	/**
	 * 这是开启或禁用侦测查询。如果开启，你必须用一个合法的SQL语句（最好是很快速的）设置 poolPingQuery 属性。
	 * 默认值：false
	 */
	private String poolPingEnabled;
	/**
	 * 这是用来配置 poolPingQuery 多次时间被用一次。
	 * 这可以被设置匹配标准的数据库连接超时时间，来避免不必要的侦测。
	 * 默认值：0（也就是所有连接每一时刻都被侦测-但仅仅当 poolPingEnabled 为 true 时适用）
	 */
	private String poolPingConnectionsNotUsedFor;
	
	public String getPoolMaximumActiveConnections() {
		return poolMaximumActiveConnections;
	}

	public void setPoolMaximumActiveConnections(String poolMaximumActiveConnections) {
		this.poolMaximumActiveConnections = poolMaximumActiveConnections;
	}

	public String getPoolMaximumIdleConnections() {
		return poolMaximumIdleConnections;
	}

	public void setPoolMaximumIdleConnections(String poolMaximumIdleConnections) {
		this.poolMaximumIdleConnections = poolMaximumIdleConnections;
	}

	public String getPoolMaximumCheckoutTime() {
		return poolMaximumCheckoutTime;
	}

	public void setPoolMaximumCheckoutTime(String poolMaximumCheckoutTime) {
		this.poolMaximumCheckoutTime = poolMaximumCheckoutTime;
	}

	public String getPoolTimeToWait() {
		return poolTimeToWait;
	}

	public void setPoolTimeToWait(String poolTimeToWait) {
		this.poolTimeToWait = poolTimeToWait;
	}

	public String getPoolPingQuery() {
		return poolPingQuery;
	}

	public void setPoolPingQuery(String poolPingQuery) {
		this.poolPingQuery = poolPingQuery;
	}

	public String getPoolPingEnabled() {
		return poolPingEnabled;
	}

	public void setPoolPingEnabled(String poolPingEnabled) {
		this.poolPingEnabled = poolPingEnabled;
	}

	public String getPoolPingConnectionsNotUsedFor() {
		return poolPingConnectionsNotUsedFor;
	}

	public void setPoolPingConnectionsNotUsedFor(String poolPingConnectionsNotUsedFor) {
		this.poolPingConnectionsNotUsedFor = poolPingConnectionsNotUsedFor;
	}

	public String getConfPath() {
		return confPath;
	}
	
	public void setConfPath(String confPath) {
		this.confPath = confPath;
	}
	
	
	public Properties getDbconf() {
		return dbconf;
	}

	public void setDbconf(Properties dbconf) {
		this.dbconf = dbconf;
	}

	public List getDbList() {
		return dbList;
	}

	public void setDbList(List dbList) {
		this.dbList = dbList;
	}

	public HashMap getDbMap() {
		return dbMap;
	}

	public void setDbMap(HashMap dbMap) {
		this.dbMap = dbMap;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getPwdKey() {
		return pwdKey;
	}

	public void setPwdKey(String pwdKey) {
		this.pwdKey = pwdKey;
	}

	public String getIpKey() {
		return ipKey;
	}

	public void setIpKey(String ipKey) {
		this.ipKey = ipKey;
	}

	public String getPortKey() {
		return portKey;
	}

	public void setPortKey(String portKey) {
		this.portKey = portKey;
	}

	public String getDbtypeKey() {
		return dbtypeKey;
	}

	public void setDbtypeKey(String dbtypeKey) {
		this.dbtypeKey = dbtypeKey;
	}

	public String getDatabaseKey() {
		return databaseKey;
	}

	public void setDatabaseKey(String databaseKey) {
		this.databaseKey = databaseKey;
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}
	
	public void init() throws Exception {
		//System.out.println("------------------ loader init.....");
		if(confPath.length()>0){
			dbconf=new Properties();
			File file=new File(confPath);
			if(file.exists() && file.isFile() && file.canRead()){
				try{
					InputStream in=new FileInputStream(file);
					dbconf.load(in);
					in.close();
				}catch(Exception x){
				    LogUtil.error(x, SKIP_Logger);
					x.printStackTrace();
				}
			}
			if(dbconf!=null){
				dbList=new ArrayList<HashMap<String,String>>();
				dbMap=new HashMap<String,HashMap<String,String>>();
				String user=dbconf.getProperty(getUserKey(),"");
				
				String pwd=dbconf.getProperty(getPwdKey(),"");
				
				String isPasswordCncrypt=dbconf.getProperty("isPasswordCncrypt");
				if(isPasswordCncrypt!=null && isPasswordCncrypt.equals("true")){
					CryptoTools2 tools = new CryptoTools2();
					pwd=tools.decode(pwd);
				}
				
				
				String ip=dbconf.getProperty(getIpKey(),"");
				String port=dbconf.getProperty(getPortKey(),"");
				String database=dbconf.getProperty(getDatabaseKey(),"");
				String dbtype=dbconf.getProperty(getDbtypeKey(),"");
				String dbdriver=getDbDriver();
				if(user.length()>0 && pwd.length()>0 && ip.length()>0 && port.length()>0 && database.length()>0 && dbtype.length()>0 && dbdriver.length()>0){
					String url=genUrl(dbtype,ip,port,database,user,pwd);
					Connection con=null;
					PreparedStatement ps =null;
					ResultSet rs=null;
					try{
						con=getConnection(dbdriver,url,user,pwd);
						String sql="select productionId from DataItemAllocation where bItem='database' group by productionId";
						ps=con.prepareStatement(sql);
						rs=ps.executeQuery();
						List<String> dbids=new ArrayList<String>();
						while(rs.next()){
							String id=rs.getString("productionId");
							dbids.add(id);
						}
						sql="select peizhiItem,peizhiItemValue from DataItemAllocation where bItem='database' and productionId=?";
						for(String dbid:dbids){
							HashMap map=new HashMap();
							map.put("dbid", dbid);
							ps=con.prepareStatement(sql);
							ps.setString(1, dbid);
							rs=ps.executeQuery();
							while(rs.next()){
								map.put(rs.getString("peizhiItem"), rs.getString("peizhiItemValue"));
							}
							dbMap.put(dbid, map);
							dbList.add(map);
						}
					}catch(Exception x){
						LogUtil.error(x, SKIP_Logger);
					}finally{
						try {
							rs.close();
							ps.close();
							con.close();
						} catch (SQLException e) {
							e.printStackTrace();
							LogUtil.error(e, SKIP_Logger);
						}
					}
				}
			}
		}
	}
	
	public String genUrl(String dbType,String ip,String port,String database,String user,String pwd){
		if("mysql".equals(dbType)){
			return "jdbc:mysql://"+ip+":"+port+"/"+ database+ "?autoReconnect=true&user="+user+"&password="+pwd+"&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=round&useOldAliasMetadataBehavior=true";
		}
		return "";
	}
	
	public Connection getConnection(String className,String url,String user,String pwd) throws Exception{
		Connection con=null;
		Class.forName(className);
		con=DriverManager.getConnection(url, user, pwd);
		return con;
	}
	
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
}
