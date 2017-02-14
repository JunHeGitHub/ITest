package com.zinglabs.util;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONSerializer;


import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;
import com.zinglabs.servlet.ZKMDocServlet;

public class UITreeHelper {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	public static String dbid=ZKMDocServlet.zkmDBId;
	//public static String dbid="ZDesk";
	public static HashMap<String,List> zkmTreeMap;
	public static String [] zkmKeys={"d","Uproblem"};
	public static final String ROOTKEY="root";
	//一个用户属于一个组
	public static final String USERORGKEY="zdesk_userOrgTree";
	//一个用户属于多个组
	public static final String USERORGKEY_UMT="zdesk_userOrgTree_UMT";
	
	static{
		init();
	}
	
	public static void init(){
		zkmTreeMap=new HashMap();
		for(String key:zkmKeys){
			zkmTreeInit(key);
		}
		userOrganizationInit();
	}
	
	public static void zkmTreeInit(String key){
		if(zkmTreeMap==null){
			zkmTreeMap=new HashMap();
		}
		List tree=getZkmTreeList(key);
		zkmTreeMap.put(key, tree);
	}
	
	public static List getZkmTreeList(String key){
		String sql="select id,`text` as `name`,recordType,parentId,companyId,companyName,departmentId,departmentName from zkmInfoBase where recordType=? and isdel<>'1' order by sortField asc";
		try{
			List<Map> list=DAOTools.queryMap(sql,new String[]{key},dbid);
			if(list!=null){
				List tree=zkmTreeFormat(list);
				return tree;
			}
		}catch(Exception x){
			LogUtil.error("zkm tree key: " + key + " error: " + x.getMessage(),SKIP_Logger);
			x.printStackTrace();
		}
		return null;
	}
	
	public static List zkmTreeFormat(List list){
		HashMap<String,List<HashMap>> phash=new HashMap<String,List<HashMap>>();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				HashMap map=(HashMap)list.get(i);
				String key=ROOTKEY;
				if(map!=null && map.get("parentId")!=null){
					String pid=(String)map.get("parentId");
					if(pid.length()>0){
						String [] pids=pid.split(",");
						for(int j=0;j<pids.length;j++){
							String pidt=pids[j];
							if(pidt.length()>0){
								key=pidt;
								List ml=phash.get(key)==null?new ArrayList():(List)phash.get(key);
								//TODO 改了节点的parentId 不知道对其他功能有没有影响 
								Map map2 =(Map) map.clone();
								map2.put("parentId", key);
								ml.add(map2);
								phash.put(key,ml);
							}
						}
					}else{
						List ml=phash.get(key)==null?new ArrayList():(List)phash.get(key);
						ml.add(map);
						phash.put(key,ml);
					}
				}
			}
			List<HashMap> plist=new ArrayList<HashMap>();
			plist=phash.get(ROOTKEY);
			if(plist!=null && plist.size()>0){
				for(Map dm:plist){
					if(dm!=null && dm.get("id")!=null){
						String id=(String)dm.get("id");
						if(id.length()>0){
							List clist=zkmTreeFormatChild(id,phash);
							dm.put("children", clist);
						}
					}
				}
				return plist;
			}else{
				return null;
			}
		}else{
			return null;
		}	
	}
	
	public static List zkmTreeFormatChild(String id,HashMap<String,List<HashMap>> phash){
		if(id!=null && id.length()>0){
			List<HashMap> clist=phash.get(id);
			if(clist!=null && clist.size()>0){
				for(Map dm:clist){
					if(dm!=null && dm.get("id")!=null){
						String cid=(String)dm.get("id");
						if(cid.length()>0){
							List cclist=zkmTreeFormatChild(cid,phash);
							dm.put("children", cclist);
						}
					}
				}
			}
			return clist;
		}else{
			return null;
		}
	}
	
	/**
	 * 获取树数据的入口，适配不同类型的数据 
	 * @param key 树数据的类型，与缓存KEY对应
	 * @param reload 是否重载
	 * @return
	 */
	public static List getTreeData(String key,boolean reload){
		List list=new ArrayList();
		if(key.equals(USERORGKEY)){
			list=getUserOrganization(reload);
		}else if(key.equals(USERORGKEY_UMT)){
			list=getUserOrganization_UMT(reload);
		}else{
			list=getZkmTree(key,reload);
		}
		return list;
	}
	
	/**
	 * 获取知识库树，常见问题、知识库目录
	 * @param key 标识是哪种类型的目录
	 * @param reload 是否重新载入
	 * @return
	 */
	public static List getZkmTree(String key,boolean reload){
		if(reload){
			return getZkmTreeList(key);
		}else{
			return zkmTreeMap.get(key);
		}
	}
	
	/**
	 * 获取用户树数据
	 * @param reload 是否重新载入
	 * @return
	 */
	public static List getUserOrganization(boolean reload){
		if(reload){
			return getUserOranizationList();
		}else{
			return zkmTreeMap.get(USERORGKEY);
		}
	}
	
	/**
	 * 获取用户树数据
	 * @param reload 是否重新载入
	 * @return
	 */
	public static List getUserOrganization_UMT(boolean reload){
		if(reload){
			return getUserOranizationList_UMT();
		}else{
			return zkmTreeMap.get(USERORGKEY_UMT);
		}
	}
	
	/**
	 * 组织结构与用户初始化，用于选人。
	 */
	public static void userOrganizationInit(){
		List tree=getUserOranizationList();
		zkmTreeMap.put(USERORGKEY, tree);
	}
	
	/**
	 * 组织结构与用户初始化，用于选人。
	 */
	public static void userOrganizationInit_UMT(){
		List tree=getUserOranizationList_UMT();
		zkmTreeMap.put(USERORGKEY_UMT, tree);
	}
	
	public static List getUserOranizationList(){
		String sql="select code as id,name,typeName,parentIndex as parentId,'org' as dataType from suSecurityPermission where typeName='organization'";
		try{
			List<Map> list=DAOTools.queryMap(sql,dbid);
			if(list!=null){
				sql="select id,name,loginName,phone_number,organizationCode as parentId , 'ren' as dataType from suSecurityUser where name <> 'openfire' and organizationCode<>''";
				List ulist=DAOTools.queryMap(sql, dbid);
				list.addAll(ulist);
				List tree=zkmTreeFormat(list);
				return tree;
			}
		}catch(Exception x){
			LogUtil.error("zkm tree key: " + USERORGKEY + " error: " + x.getMessage(),SKIP_Logger);
			x.printStackTrace();
		}
		return null;
	}
	
	public static List getUserOranizationList_UMT(){
		String sql="select code as id,name,typeName,parentIndex as parentId,'org' as dataType from suSecurityPermission where typeName='organization'";
		try{
			List<Map> list=DAOTools.queryMap(sql,dbid);
			if(list!=null){
				sql="SELECT id,name,loginName,phone_number,'ren' as dataType,(SELECT group_concat(`suSecurityUserOrg`.`orgCode` SEPARATOR ',') FROM `suSecurityUserOrg` WHERE `suSecurityUserOrg`.`loginName` = `suSecurityUser`.`loginName` GROUP BY `suSecurityUserOrg`.`loginName` ) AS `parentId` FROM suSecurityUser where name <> 'openfire'";
				List ulist=DAOTools.queryMap(sql, dbid);
				list.addAll(ulist);
				List tree=zkmTreeFormat(list);
				return tree;
			}
		}catch(Exception x){
			LogUtil.error("zkm tree key: " + USERORGKEY + " error: " + x.getMessage(),SKIP_Logger);
			x.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){
		List list=UITreeHelper.getTreeData(USERORGKEY_UMT,true);
		String json=JSONSerializer.toJSON(list).toString();
		System.out.println(json);
	}
}
