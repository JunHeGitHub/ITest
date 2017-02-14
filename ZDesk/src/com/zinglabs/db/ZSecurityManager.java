package com.zinglabs.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.log.LogUtil;
import com.zinglabs.servlet.ZKMDocServlet;

import net.sf.json.JSONSerializer;

public class ZSecurityManager {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	public static final String DATA_RELATION_TYPE_ZKM="zkmData";
	public static String dbid;
	static{
		init();
	}
	
	public static void init(){
		dbid=ConfInfo.confMapDBConf.get("securityDBId");
		//dbid="ZDesk";
	}
	/**
	 * 初始化页面功能权限，tab,btn
	 * @throws Exception
	 */
	public static Map getRolesPageAuth() throws Exception{
		Map rolesPageAuth=new HashMap();
		String sql="select * from Z_PageFunAuthorize";
		List list=DAOTools.queryMap(sql, dbid);
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map map=(HashMap)list.get(i);
				String roleName=map.get("roleName")==null?"":(String)map.get("roleName");
				String modleId=map.get("modleId")==null?"":(String)map.get("modleId");
				if(roleName.length()>0 && modleId.length()>0){
					Map rpa=rolesPageAuth.get(roleName)==null?new HashMap():(HashMap)rolesPageAuth.get(roleName);
					List funList=rpa.get(modleId)==null?new ArrayList():(ArrayList)rpa.get(modleId);
					funList.add(map);
					rpa.put(modleId, funList);
					rolesPageAuth.put(roleName, rpa);
				}
			}
		}
		return rolesPageAuth;
	}
	
	public static Map getRoleAllPageAuth(String roleName) throws Exception{
		Map rolePageAuth=new HashMap();
		String sql="select * from Z_PageFunAuthorize where roleName=?";
		List list=DAOTools.queryMap(sql,new String[]{roleName}, dbid);
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map map=(HashMap)list.get(i);
				String modleId=map.get("modleId")==null?"":(String)map.get("modleId");
				if(roleName.length()>0 && modleId.length()>0){
					List funList=rolePageAuth.get(modleId)==null?new ArrayList():(ArrayList)rolePageAuth.get(modleId);
					funList.add(map);
					rolePageAuth.put(modleId, funList);
				}
			}
		}
		return rolePageAuth;
	}
	
	public static List getModlePageAuthList(String roleName,String modleId) throws Exception{
		Map rolesPageAuth=getRoleAllPageAuth(roleName);
		return rolesPageAuth.get(modleId)==null?new ArrayList():(ArrayList)rolesPageAuth.get(modleId);
	}
	
	/**
	 * 获取所有的组织机构
	 * @return
	 */
	public static List getOrganizations() throws Exception{
		String sql="select * from suSecurityPermission where typeName='organization'";
		List list=DAOTools.queryMap(sql, dbid,null);
		return list;
	}
	/**
	 * 获取组织的子组织
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	public static List getOrganizationChilds(String orgCode) throws Exception{
		String sql="select * from suSecurityPermission where typeName='organization' and parentIndex=?";
		String [] param={orgCode};
		List list=DAOTools.queryMap(sql,param, dbid);
		return list;
	}
	
	/**
	 * 获取所有用户
	 * @return
	 */
	public static List getAllUsers() throws Exception{
		String sql="select * from suSecurityUser";
		List list=DAOTools.queryMap(sql, dbid,null);
		return list;
	}
	/**
	 * 获取组织的用户
	 * @param orgCode
	 * @return
	 * @throws Exception
	 */
	public static List getUserForOrganization(String orgCode)throws Exception {
		String sql="select * from suSecurityUser where organizationCode=? and (startus='0' or startus='' or startus=null)";
		String [] param={orgCode};
		List list=DAOTools.queryMap(sql,param, dbid);
		return list;
	}
	
	/**
	 * 获取根组织
	 * @return
	 * @throws Exception
	 */
	public static List getRootOraganizations() throws Exception{
		String sql="select * from suSecurityPermission where typeName='organization' and (parentIndex='' or parentIndex=null)";
		List list=DAOTools.queryMap(sql, dbid,null);
		return list;
	}
	
	/**
	 * 获取组织映射 map(orgCode,childsList)
	 * @return
	 * @throws Exception
	 */
	public static Map getOrganizationsMap() throws Exception{
//		List list=getOrganizations();
//		Map reMap=new HashMap();
//		for(int i=0;i<list.size();i++){
//			Map map=(Map)list.get(i);
//			if(map!=null){
//				String code=map.get("code")==null?"":map.get("code").toString();
//				if(code.length()>0){
//					List clist=getOrganizationChilds(code);
//					reMap.put(code,clist);
//				}
//			}
//		}
//		return reMap;
		
		String sql="SELECT b.code p_code_,a.* FROM suSecurityPermission a LEFT JOIN suSecurityPermission b ON a.parentIndex=b.code  " +
				"WHERE  a.typeName='organization' AND  b.typeName='organization' ORDER BY p_code_,a.name";
		List list=DAOTools.queryMap(sql, dbid);
		Map reMap=new HashMap();
		if(list!=null){
			String tcode="";
			List clist=new ArrayList();
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				if(map!=null){
					String p_code_=map.get("p_code_")==null?"":(String)map.get("p_code_");
					if(p_code_.length()>0){
						if(p_code_.equals(tcode)){
							clist.add(map);
						}else{
							if(tcode.length()>0){
								reMap.put(tcode, clist);
							}
							tcode=p_code_;
							clist=new ArrayList();
							clist.add(map);
						}
					}
				}
			}
			if(!"".equals(tcode)){
				reMap.put(tcode, clist);
			}
		}
		return getOrganizationsFix(reMap);
	}
	
	/**
	 * 获取组织用户映射　map(orgCode,userList);
	 * @return
	 * @throws Exception
	 */
	public static Map getOrganizationsUserMap() throws Exception{
//		List list=getOrganizations();
//		Map reMap=new HashMap();
//		for(int i=0;i<list.size();i++){
//			Map map=(Map)list.get(i);
//			if(map!=null){
//				String code=map.get("code")==null?"":map.get("code").toString();
//				if(code.length()>0){
//					List mulist=getUserForOrganization(code);
//					reMap.put(code, mulist);
//				}
//			}
//		}
//		return reMap;
		String sql="SELECT b.code p_code_,b.name,a.* FROM suSecurityUser a,suSecurityPermission b " +
			"WHERE a.organizationCode=b.code AND (startus='0' OR startus='' OR startus=NULL) order by p_code_,a.loginName";
		Map reMap=new HashMap();
		List list=DAOTools.queryMap(sql, dbid);
		if(list!=null){
			List clist=new ArrayList();
			String tcode="";
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				if(map!=null){
					String p_code_=map.get("p_code_")==null?"":(String)map.get("p_code_");
					if(p_code_.length()>0){
						if(p_code_.equals(tcode)){
							clist.add(map);
						}else{
							if(tcode.length()>0){
								reMap.put(tcode, clist);
							}
							clist=new ArrayList();
							tcode=p_code_;
							clist.add(map);
						}
					}
				}
			}
			if(!"".equals(tcode)){
				reMap.put(tcode, clist);
			}
		}
		return getOrganizationsFix(reMap);
	}
	
	/**
	 * 用于获取组织，获取组织人员后，补齐返回map中缺少的组织
	 * @param reMap
	 * @return
	 * @throws Exception
	 */
	public static Map getOrganizationsFix(Map reMap) throws Exception{
		List orglist=getOrganizations();
		for(int i=0;i<orglist.size();i++){
			Map orgmap=(Map)orglist.get(i);
			if(orgmap!=null){
				String code =orgmap.get("code")==null?"":(String)orgmap.get("code");
				if(!reMap.containsKey(code)){
					reMap.put(code, new ArrayList());
				}
			}
		}
		return reMap;
	}
	/**
	 * 获取组织的JSON对象
	 * @return
	 * @throws Exception
	 */
	public static String getOrganizationJson() throws Exception{
		HashMap map=new HashMap();
		//long bt=System.currentTimeMillis();
		List roots=getRootOraganizations();
		//LogUtil.error("getOrganizationJson getRootOraganizations 1 " + (System.currentTimeMillis() -bt), SKIP_Logger);
		if(roots!=null){
			map.put("roots", roots);
		}else{
			map.put("roots", "");
		}
		//bt=System.currentTimeMillis();
		Map omap=getOrganizationsMap();
		//LogUtil.error("getOrganizationJson getOrganizationsMap  " + (System.currentTimeMillis() -bt), SKIP_Logger);
		if(omap!=null){
			map.put("OrgsMap", omap);
		}else{
			map.put("OrgsMap", "");
		}
		//bt=System.currentTimeMillis();
		Map umap=getOrganizationsUserMap();
		//LogUtil.error("getOrganizationJson getOrganizationsUserMap " + (System.currentTimeMillis() -bt), SKIP_Logger);
		if(umap!=null){
			map.put("OrgUsers", umap);
		}else{
			map.put("OrgUsers", "");
		}
		//bt=System.currentTimeMillis();
		String rejson=JSONSerializer.toJSON(map).toString();
		//LogUtil.error("getOrganizationJson rejson  " + (System.currentTimeMillis() -bt), SKIP_Logger);
		return rejson;
	}
	
	public static String getUserInfoJson(String userName) throws Exception{
		if(userName!=null && userName.length()>0){
			String sql="select * from `suSecurityUser` where `loginName`=?";
			String[] params={userName};
			List list=DAOTools.queryMap(sql, params, dbid);
			if(list!=null && list.size()==1){
				Map map=list.get(0)==null?null:(Map)list.get(0);
				if(map!=null){
					return JSONSerializer.toJSON(map).toString();
				}
			}
		}
		return "";
	}
	
	public static void setZKMSecurityDataRoleRelation(String id,List<HashMap<String,String>> uplist,String delSqlWhere) throws Exception{
		DAOBatchUpdate dbu=new DAOBatchUpdate(dbid);
		String sql="insert into `zkmInfoSecurity` (`infoId`,`roleName`,`dataType`) value (?,?,?)";
		//删除与授权记录重复的授权记录
		String delRepSql="delete from `zkmInfoSecurity` where `infoId`=? and `roleName`=? and `dataType`=?";
		//删除本级与子级已存在的授权
		String delsql="delete from `zkmInfoSecurity` where `infoId`=? and `dataType`=?";
		List<String[]> list=new ArrayList<String[]>();
		List<String[]> dellist=new ArrayList<String[]>();
		List<String[]> delReplist=new ArrayList<String[]>();
		for(int i=0;i<uplist.size();i++){
			HashMap<String,String> pm=uplist.get(i);
			String [] up={pm.get("infoId"),pm.get("roleName"),pm.get("dataType")};
			list.add(up);
			delReplist.add(up);
		}
		//查询本级与子级已存在的授权，以便进行授权清理。
		String nowSql="select * from `zkmInfoSecurity` where 1=1 " + delSqlWhere + " and `dataType` = '" + DATA_RELATION_TYPE_ZKM +"'";
		List<Map> hlist=DAOTools.queryMap(nowSql, dbid,null);
		if(hlist!=null && hlist.size()>0){
			for(Map<String,String> hm:hlist){
				String[] delup={hm.get("infoId"),hm.get("dataType")};
				dellist.add(delup);
			}
		}
		try{
			dbu.batchUpdate(delRepSql, delReplist);
			if(dellist.size()>0){
				dbu.batchUpdate(delsql,dellist);
			}
			dbu.batchUpdate(sql,list);
			dbu.commit();
		}catch(Exception x){
			dbu.rollBack();
			throw x;
		}finally{
			dbu.close();
		}
	}
	
	public static List<Map> getSecurityDataRoleRelation(String infoId,String type) throws Exception{
		String sql="select * from `zkmInfoSecurity` where `infoId`=? and `dataType`=?";
		String [] parms={infoId,type};
		return DAOTools.queryMap(sql, parms, dbid);
	}
	
	public static void setSecurityDataRoleRelationSingle(String id,String roleName,String type) throws Exception{
		String delRepSql="delete from `zkmInfoSecurity` where `infoId`=? and `roleName`=? and `dataType`=?";
		String sql="insert into `zkmInfoSecurity` (`infoId`,`roleName`,`dataType`) value (?,?,?)";
		String[] param={id,roleName,type};
		DAOTools.updateForPrepared(delRepSql, param, ZKMDocServlet.zkmDBId);
		DAOTools.updateForPrepared(sql, param, ZKMDocServlet.zkmDBId);
	}
	
	public static void main(String[] args) throws Exception{
		//logger.info(getOrganizationJson());
		//DAOTools.initAllStatic();
		//DAOTools.hasInitStatic=true;
		ZSecurityManager zs=new ZSecurityManager();
		//Map map =zs.getOrganizationsUserMap();
	}
}
