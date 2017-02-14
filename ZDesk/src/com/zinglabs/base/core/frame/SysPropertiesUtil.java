package com.zinglabs.base.core.frame;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.jxpath.JXPathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.JdbcProConfFileInit;
import com.zinglabs.base.Interface.BaseInterface;
import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.db.CryptoTools2;
/**
 * 系统配置表工具类
 * 依赖baseLib 中的BaseAbs
 * @author QCF
 */
public class SysPropertiesUtil {
	
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	
	public static List propertList;
	
	/**
	 * 类被第一次初始化时调用
	 */
	static{
		propertList=new ArrayList<HashMap<String,String>>();
		JdbcProConfFileInit.init();
		init();
	}
	
	/**
	 * 初始化配置表中的数据并缓存至propertList
	 */
	public static void init(){
		InputStream in =null;
		Connection con=null;
		PreparedStatement ps =null;
		ResultSet rs=null;
		try{
			Properties pro=new Properties();
			logger.debug("SysPropertiesUtil init confPath is : " + BaseAbs.CONFIG_FILE_PATH_PROPERTIES + " : " + BaseAbs.CONFIG_FILE_PATH_PROPERTIES2);
			try{
			   in = new BufferedInputStream(new FileInputStream(BaseAbs.CONFIG_FILE_PATH_PROPERTIES));
	        }
	        catch (Exception e){
	           in = new BufferedInputStream(new FileInputStream(BaseAbs.CONFIG_FILE_PATH_PROPERTIES2));
	        }
	        pro.load(in);
	        String user=pro.getProperty("userName","");
			String pwd=pro.getProperty("password","");
			String isPasswordCncrypt=pro.getProperty("isPasswordCncrypt");
			if(isPasswordCncrypt!=null && isPasswordCncrypt.equals("true")){
				CryptoTools2 tools = new CryptoTools2();
				pwd=tools.decode(pwd);
			}
			String ip=pro.getProperty("dbip","");
			String port=pro.getProperty("port","");
			String database=pro.getProperty("name","");
			String dbtype=pro.getProperty("type","");
			in.close();
			
			String sql="select * from DataItemAllocation";
			con=getConnection(getDBDriver(dbtype),genMySqlUrl(dbtype,ip,port,database,user,pwd),user,pwd);
			ps=con.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				HashMap data=new HashMap();
				String id=rs.getString("id")==null?"":rs.getString("id");
				String peizhiItem=rs.getString("peizhiItem")==null?"":rs.getString("peizhiItem");
				String peizhiItemValue=rs.getString("peizhiItemValue")==null?"":rs.getString("peizhiItemValue");
				String desc=rs.getString("desc")==null?"":rs.getString("desc");
				String bItem=rs.getString("bItem")==null?"":rs.getString("bItem");
				String sItem=rs.getString("sItem")==null?"":rs.getString("sItem");
				String productionId=rs.getString("productionId")==null?"":rs.getString("productionId");
				String generateTime=rs.getString("generateTime")==null?"":rs.getString("generateTime");
				data.put("id", id);
				data.put("peizhiItem", peizhiItem);
				data.put("peizhiItemValue", peizhiItemValue);
				data.put("desc", desc);
				data.put("bItem", bItem);
				data.put("sItem", sItem);
				data.put("productionId", productionId);
				data.put("generateTime", generateTime);
				propertList.add(data);
			}
			logger.info("load all database conf : " + propertList.size());
		}catch(Exception x){
			logger.error("load db set error.",x);
			logger.error(x.getMessage());
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					logger.error("error ." ,e);
				}
			}
		}finally{
			try{
				if(rs!=null){
					rs.close();
					rs=null;
				}
				if(ps!=null){
					ps.close();
					ps=null;
				}
				if(con!=null){
					con.close();
					con=null;
				}
			}catch(Exception e){
				
			}
		}
	}
	/**
	 * 拼装mysql连接串
	 * @param dbType
	 * @param ip
	 * @param port
	 * @param database
	 * @param user
	 * @param pwd
	 * @return
	 */
	public static String genMySqlUrl(String dbType,String ip,String port,String database,String user,String pwd){
		if("mysql".equals(dbType)){
			return "jdbc:mysql://"+ip+":"+port+"/"+ database+ "?autoReconnect=true&user="+user+"&password="+pwd+"&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=round&useOldAliasMetadataBehavior=true";
		}
		return "";
	}
	/**
	 * 获取数据库连接
	 * @param className
	 * @param url
	 * @param user
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection(String className,String url,String user,String pwd) throws Exception{
		Connection con=null;
		Class.forName(className);
		con=DriverManager.getConnection(url, user, pwd);
		return con;
	}
	/**
	 * 获取数据库连接驱动
	 * @param dbType
	 * @return
	 */
	public static String getDBDriver(String dbType){
		String dstr="";
		if(dbType==null || dbType.length()<=0){
			return dstr;
		}
		dbType=dbType.toUpperCase();
		if("MYSQL".equals(dbType)){
			dstr="com.mysql.jdbc.Driver";
		}else if("ORACLE".equals(dbType)){
			dstr="oracle.jdbc.driver.OracleDriver";
		}
		return dstr;
	}
	
	/**
	 * 在list中查询满足xpath条件xpathStr的数据
	 * 例：
	 * 		要取到数组中bitem=conf 与 bitem=database 数据
	 * 		propertList[bItem='conf' or bItem='database']
	 * 		注：propertList内置名称是所有数据库配置的集合
	 * 			bItem 列名
	 *   		区分大小写
	 * @param list 数据数组
	 * @param xpathStr xpath表达式
	 * @return
	 */
	public static List listSearch(List list,String xpathStr){
		List rlist=null;
		if(list!=null && list.size()>0){
			rlist=new ArrayList();
			HashMap m=new HashMap();
			m.put("propertList", list);
			JXPathContext context= JXPathContext.newContext(m);
			Iterator it=context.iterate(xpathStr);
			while(it.hasNext()){
				rlist.add(it.next());
			}
		}
		return rlist;
	}
	/**
	 * 获取xpath条件满足的map对象集合
	 * 例：
	 * 		要取到所有配置中bitem=conf 与 bitem=database 数据
	 * 		propertList[bItem='conf' or bItem='database']
	 * 		注：propertList内置名称是所有数据库配置的集合
	 * 			bItem 列名
	 *   		区分大小写
	 * @param xpathStr xpath表达式
	 * @return
	 */
	public static List getProperties(String xpathStr){
		return listSearch(propertList,xpathStr);
	}
	/**
	 * 获取缓存数据中 bitem等于参数值的数据
	 * @param bitem
	 * @return
	 */
	public static List getConfWidthBItem(String bitem){
		String xpath="propertList[1=1 and bItem='"+ bitem +"']";
		System.out.println(xpath);
		return getProperties(xpath);
	}
	
	/**
	 * 获取缓存数据中 peizhiItem等于参数值的数据
	 * @param peizhiItem
	 * @return
	 */
	public static List getConfWidthPeizhiItem(String peizhiItem){
		String xpath="propertList[peizhiItem='"+ peizhiItem +"']";
		return getProperties(xpath);
	}
	
	/**
	 * 获取缓存数据中 productionId等于参数值的数据
	 * @param productionId
	 * @return
	 */
	public static List getConfWidthProductionId(String productionId){
		String xpath="propertList[productionId='"+ productionId +"']";
		return getProperties(xpath);
	}
	
	/**
	 * 获取缓存数据中 bitem productionId 等于参数值的数据
	 * @param bitem
	 * @param productionId
	 * @return
	 */
	public static List getConfWithBItemAndProductionId(String bitem,String productionId){
		String xpath="propertList[bItem='"+ bitem +"' and productionId='"+ productionId +"']";
		return getProperties(xpath);
	}
	
	/**
	 * 获取缓存数据中 bitem peizhiItem 等于参数值的数据
	 * @param bitem
	 * @param peizhiItem
	 * @return
	 */
	public static List getConfWithBItemAndPeizhiItem(String bitem,String peizhiItem){
		String xpath="propertList[bItem='"+ bitem +"' and peizhiItem='"+ peizhiItem +"']";
		return getProperties(xpath);
	}
	
	/**
	 * 获取缓存数据中 productionId peizhiItem 等于参数值的数据
	 * @param productionId
	 * @param peizhiItem
	 * @return
	 */
	public static List getConfWithProductionIdAndPeizhiItem(String productionId,String peizhiItem){
		String xpath="propertList[productionId='"+ productionId +"' and peizhiItem='"+ peizhiItem +"']";
		return getProperties(xpath);
	}
	
	/**
	 * 获取缓存数据中 bitem productionId peizhiItem 等于参数值的数据
	 * @param bitem
	 * @param productionId
	 * @param peizhiItem
	 * @return
	 */
	public static List getConfWithBItemAndProductionIdAndPeizhiItem(String bitem,String productionId,String peizhiItem){
		String xpath="propertList[bItem='"+bitem+"' and productionId='"+ productionId +"' and peizhiItem='"+ peizhiItem +"']";
		return getProperties(xpath);
	}
	
	public static List queryBItemAndProductionIdAndPeizhiItem(String bitem,String productionId,String peizhiItem){
		StringBuffer sb=new StringBuffer("propertList[1=1");
		if(null !=bitem && bitem.length()>0){
			sb.append(" and bItem = '"+bitem+"'");
		}
		if(null !=productionId && productionId.length()>0){
			sb.append(" and productionId = '"+productionId+"'");
		}
		if(null !=peizhiItem && peizhiItem.length()>0){
			sb.append(" and peizhiItem = '"+peizhiItem+"'");
		}
		sb.append("]");
		String xpath=sb.toString();
		return getProperties(xpath);
	}
	
	public static void main(String [] args){
		List list=SysPropertiesUtil.getConfWidthBItem("remoteServer");
		System.out.println(list.size());
		
		list=SysPropertiesUtil.getConfWithBItemAndProductionId("remoteServer","ZKM");
		System.out.println(list.size());
		
		list=SysPropertiesUtil.getConfWithBItemAndProductionIdAndPeizhiItem("remoteServer","ZKM","0");
		System.out.println(list.size());
	}
	
	public List getDataItemAllocationList(HashMap<String,String> map){
		if(null != map){
			if(null != map.get("peizhiItem") && ""!= map.get("peizhiItem") && null != map.get("bItem") && ""!= map.get("bItem")  && null != map.get("productionId") && ""!= map.get("productionId")){
				return getConfWithBItemAndProductionIdAndPeizhiItem(map.get("bItem"),map.get("productionId"),map.get("peizhiItem"));
			}
		}
		return this.propertList;
	}
	
}
