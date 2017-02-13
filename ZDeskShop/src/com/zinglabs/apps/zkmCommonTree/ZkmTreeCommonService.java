package com.zinglabs.apps.zkmCommonTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.zinglabs.apps.zkmCommonTree.userMapperOrgTreeFilter.UserMapperOrgTreeFilter;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.base.core.utils.AppSpringUtils;
import com.zinglabs.util.RandomGUID;

public class ZkmTreeCommonService extends BaseService {

	public List<HashMap<String, String>> getTreeNode(HashMap<String, String> map) {
		List zkmInfoBasePojoList = null;
		if("1".equals(map.get("isSynchronous"))){
			zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getTreeNode", map);
		}else{
			zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getAllTreeNode", map);
		}

        
		return zkmInfoBasePojoList;
	}
	public List<HashMap<String, String>> getZhijianTreeNode(HashMap<String, String> map) {
		List zkmInfoBasePojoList = null;		
		zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getAllZhijianTreeNode", map);
		/*for(int i=0;i<zkmInfoBasePojoList.size();i++){
			HashMap<String, String>m=zkmInfoBasePojoList.get(i);
			//String fenshu=zkmInfoBasePojoList.get(i).get("percent").toString();
			//String mm=m.get("percent");
			System.out.println(m);
			zkmInfoBasePojoList.get(i).put("text", zkmInfoBasePojoList.get(i).get("text")+"("+1+")");
		}*/
		return zkmInfoBasePojoList;
	}
	public List<HashMap<String, String>> getCaidanTreeNode(HashMap<String, String> map) {
		List zkmInfoBasePojoList = null;		
		zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getCaidanTreeNode", map);
		return zkmInfoBasePojoList;
	}
	public List<HashMap<String, String>> getCaidanTreeNode_isStart(HashMap<String, String> map) {
		List zkmInfoBasePojoList = null;		
		zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getCaidanTreeNode_isStart", map);
		return zkmInfoBasePojoList;
	}
	public List<HashMap<String, String>> getCaidanTreeNode_isStart_child(HashMap<String, String> map) {
		List zkmInfoBasePojoList = null;		
		zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getCaidanTreeNode_isStart_child", map);
		return zkmInfoBasePojoList;
	}
	public List<HashMap<String, String>> getCaidanTreeNode_geshu(HashMap<String, String> map) {
		List zkmInfoBasePojoList = null;		
		zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getCaidanTreeNode_geshu", map);
		return zkmInfoBasePojoList;
	}
	public List<HashMap<String, String>> getCaidanTreeNode_geshu_isStart(HashMap<String, String> map) {
		List zkmInfoBasePojoList = null;		
		zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getCaidanTreeNode_geshu_isStart", map);
		return zkmInfoBasePojoList;
	}
	public List<HashMap<String, String>> getCaidanTreeNode_ById(HashMap<String, String> map) {
		List zkmInfoBasePojoList = null;		
		zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getCaidanTreeNode_ById", map);
		return zkmInfoBasePojoList;
	}
	public List<HashMap<String, String>> getCaidanTreeNode_geshu_child(HashMap<String, String> map) {
		List zkmInfoBasePojoList = null;		
		zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getCaidanTreeNode_geshu_child", map);
		return zkmInfoBasePojoList;
	}
	public void treeNodeClassification(HashMap<String, String> treeNode, String ids) {
		getServiceSqlSession().db_update("updateCurrentNode", treeNode);
		String[] id = ids.split(";");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(int i = 0;i<id.length;i++){
			Map<String, String> map = new HashMap<String, String>();
			map.put("id",id[i]);
			map.put("sortField", i+"");
			list.add(map);
		}
		getServiceSqlSession().db_update("updateZkmInfoBaseSortField", list);
	}
	public void treeNodeClassification(String ids) {
		
		String[] id = ids.split(";");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(int i = 0;i<id.length;i++){
			Map<String, String> map = new HashMap<String, String>();
			map.put("id",id[i]);
			map.put("sortField", i+"");
			list.add(map);
		}
		getServiceSqlSession().db_update("updateZkmInfoBaseSortField", list);
	}
	//找到pid=id的孩子节点
	public List<HashMap<String, String>> getTreeNodeByPidIsId(HashMap<String, String> map) {
		    List zkmInfoBasePojoList = null;
			zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getTreeNodeByPidIsId", map);
		    return zkmInfoBasePojoList;
	}
	public List<HashMap<String, String>> getTreeNodeByPidIsId_zj(HashMap<String, String> map) {
	    List zkmInfoBasePojoList = null;
		zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getTreeNodeByPidIsId_zj", map);
	    return zkmInfoBasePojoList;
}
	public List<HashMap<String, String>> getSecurityPermissionTreeNode(HashMap<String, String> map) {
		List zkmInfoBasePojoList = null;
		//判断是否进行权限关联查询
		//Jurisdiction为Map参数
		if("Big".equals(map.get("Jurisdiction"))){
			zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getAllSecurityPermissionTreeNode", map);
		}if("1".equals(map.get("isSynchronous"))){
			zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getSecurityPermissionTreeNode", map);
		}else{
			zkmInfoBasePojoList = getServiceSqlSession().db_selectList("getSecurityPermissionTreeNode", map);
		}
		List<HashMap<String, String>> zkmInfoBaseTransfromPojoList = new ArrayList<HashMap<String, String>>();
		for (HashMap zkmInfoBaseMap : (List<HashMap>)zkmInfoBasePojoList) {
			zkmInfoBaseMap.put("id", zkmInfoBaseMap.get("id"));
			zkmInfoBaseMap.put("pId", zkmInfoBaseMap.get("parentId"));
			
			zkmInfoBaseMap.put("isParent", "true");
			
			zkmInfoBaseTransfromPojoList.add(zkmInfoBaseMap);
		}
		return zkmInfoBasePojoList;
	}
	public HashMap<String, String> getCommonTreeParam(HashMap<String, String> map) {
		List zkmCommonTreeParamList = getServiceSqlSession().db_selectList("getCommonTreeParam", map);
		if (zkmCommonTreeParamList != null && zkmCommonTreeParamList.size() >= 1) {
			return (HashMap)zkmCommonTreeParamList.get(0);
		}
		return null;
	}
	public List<HashMap<String, String>> getCommonTreeParamList(HashMap<String, String> map) {
		List zkmCommonTreeParamList = getServiceSqlSession().db_selectList("getCommonTreeParam", map);
		return zkmCommonTreeParamList;
	}
	public List<HashMap<String, String>> selectTreeNodeById(HashMap<String, String> map) {
		List zkmCommonTreeParamList = getServiceSqlSession().db_selectList("selectTreeNodeById", map);
		return zkmCommonTreeParamList;
	}
	public int addTreeNode(HashMap<String, String> map){
		return getServiceSqlSession().db_insert("addTreeNode", map);
	}
	public int addTreeNode_zhijian(HashMap<String, String> map){
		return getServiceSqlSession().db_insert("addTreeNode_zhijian", map);
	}
	public int updateTreeNode(HashMap<String, String> map){
		return getServiceSqlSession().db_update("updateTreeNode", map);
	}
	public int updateTreeNodeName(HashMap<String, String> map){
		return getServiceSqlSession().db_update("updateTreeNodeName", map);
	}
	
	public int  deleteTreeNode(Map<String, String> map){
		return getServiceSqlSession().db_delete("deleteTreeNode", map);
	}
	public int  deleteTreeNode_ZJ(Map<String, String> map){
		return getServiceSqlSession().db_delete("deleteTreeNode_ZJ", map);
	}
	public int  deleteTreeNode_caidan(Map<String, String> map){
		return getServiceSqlSession().db_delete("deleteTreeNode_caidan", map);
	}
	public int  commonTreeDeleteByRecordType(Map<String, String> map){
		return getServiceSqlSession().db_delete("deleteTreeNode", map);
	}
	public int addzuzhijigouTreeNode(HashMap map) {
		return getServiceSqlSession().db_insert("addzuzhijigouTreeNode", map);
	}
	public int updatezuzhijigouTreeNode(HashMap map) {
		return getServiceSqlSession().db_update("updatezuzhijigouTreeNode", map);
	}
	public int deletezuzhijigouTreeNode(List<String> list) {
		return getServiceSqlSession().db_delete("deletezuzhijigouTreeNode", list);
	}
	//删除该节点下面的第一层孩子节点
	public int deletezuzhijigouTreeChildNode(HashMap map) {
		return getServiceSqlSession().db_delete("deleteTreeNode", map);
	}
	public int deleteZjChildNode(HashMap map) {
		return getServiceSqlSession().db_delete("deleteZjChildNode", map);
	}
	public void commonTreeDelete(HashMap map) {
		getServiceSqlSession().db_delete("commonTreeDelete", map);
	}
	public void commonTreeUpdate(HashMap map) {
		getServiceSqlSession().db_update("commonTreeUpdate", map);
	}
	public void treeNodeClassificationUpdatePidIsNull(HashMap map) {
		getServiceSqlSession().db_update("updateCurrentNode", map);
	}
	public boolean commonTreeSave(HashMap map) {
		boolean save = false;
		int num = Integer.parseInt(getServiceSqlSession().db_selectOne("checkCommonTreeSaveData", map).toString());
		if(num ==0){
			map.put("nodeId",new RandomGUID().toString());
			getServiceSqlSession().db_insert("commonTreeSave", map);
			save = true;
		}
		return save;
	}
	public List commonTreeSelect(HashMap map) {
		return getServiceSqlSession().db_selectList("getTreeNodeByPidIsId", map);
	}
	@Override
	public com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid  getServiceSqlSession() {
		return (AppSqlSessionDbid) getBean("zkmCommonTreeSqlSession");
	}
	
	//获取配置表的列表
	public List<HashMap<String, String>> getPeiZhiCommonTreeList(HashMap<String, String> map){
		List zkmCommonTreeParamList = getServiceSqlSession().db_selectList("getPeiZhiCommonTreeList", map);
		return zkmCommonTreeParamList;
	}
	
	private List getUserMapOrg(String companyId,String departmentId){
		Map map=new HashMap();
		map.put("recordType", "org");
		map.put("companyId", companyId);
		map.put("departmentId", departmentId);
		
		List <Map> roots= getServiceSqlSession().db_selectList("getRootNodes",map);
		return roots;
	}
	/**
	 * 获取所用组织机构数据
	 * @return
	 */
	public List getAllOrg(){
		Map map=new HashMap();
		map.put("recordType", "org");
		List orglist = getServiceSqlSession().db_selectList("getAllOrg", map);
		return orglist;
	}
	
	/**
	 * 用户组织树拼接 
	 * @param childs
	 * @param parentNode
	 * @return
	 */
	public List formatChild(List<Map> childs,List<Map> parentNode){
		for(int i=0;i<parentNode.size();i++){ 
			  Map pNode=parentNode.get(i);
			  String rootId =pNode.get("id").toString();
			  List newChilds=new ArrayList();
			  List temp =new ArrayList(); 
			  newChilds.addAll(childs);
			  List c=new ArrayList();
			  for(int j=0;j<childs.size();j++){			 
				  Map child=childs.get(j);
				  String childId=child.get("parentId").toString();
				  //用于区分组织还是个人
				  child.put("isParent", "true");
				  if(childId.equals(rootId)){ 
					  c.add(child);
					  temp.add(child);
					  newChilds.remove(child);
			      } 		
			  }
			  pNode.put("children", c);
			  formatChild(newChilds,temp);
		}
		return parentNode;	
	}
	public void setUserToOrg(Map org,Map org2users){
		if(org!=null){
				String id=(String)org.get("id");
				if(org2users.containsKey(id)){
					List user=(List) org.get("children")==null?new ArrayList():(List)org.get("children");
					user.addAll((List)org2users.get(id));
					org.put("child",user);
					org2users.remove(id)	;
				}   
			
		}
		
	}
	//拼接用户组织树  遍历组织机构节点 能对应上用户进行组装
	public void formatUser(Map org,Map org2users){
		List orgList=(List) org.get("children");
		setUserToOrg(org,org2users);
		if(orgList!=null){
			for(int i=0;i<orgList.size();i++){
				Map child=(Map) orgList.get(i);
				setUserToOrg(org,org2users);
				formatUser(child,org2users);
			}
		}	
	}
	
	//获取用户组织树
	public List getUserMapOrgTree(Map params){
        String companyId=(String)params.get("companyId");
        String departmentId=(String)params.get("departmentId");
        String loginName=(String)params.get("loginName");
        //先加公司id 部分id区分
		Map map=new HashMap();
		map.put("companyId", companyId);
		map.put("departmentId", departmentId);
		
		map.put("recordType", "org");
		List orgChilds= getServiceSqlSession().db_selectList("getChilds",map);       
		List<Map> orgRoots=  getUserMapOrg(companyId,departmentId);
		
		List users=getServiceSqlSession().db_selectList("getAllUser",params);
		
		 //过滤器部分
		String beanId=(String)params.get("beanId");
       
		if(beanId!=null&&!"".equals(beanId)){
			UserMapperOrgTreeFilter	 treeFilter=(UserMapperOrgTreeFilter) this.getBean(beanId);
			treeFilter.orgFilter(orgRoots,orgChilds,params);
			treeFilter.userFilter(users,params);
		}
		String getUserByFilter=(String)params.get("FilterBeanId");
		
		if(getUserByFilter!=null&&getUserByFilter.length()>0){
			UserMapperOrgTreeFilter	 treeFilter=(UserMapperOrgTreeFilter) this.getBean(getUserByFilter);
			users = treeFilter.getUserByFilter(users,params) ;
		}
		
		List orgs=formatChild(orgChilds,orgRoots);
		Map org2user=getAllOrg2UserInfo(users);
		for(int i=0;i<orgs.size();i++){
			Map org=(Map) orgs.get(i);
			formatUser(org,org2user);
		}
		return orgs;
	}
	//获取组织对应用户的信息
	public Map getAllOrg2UserInfo(List users){
		//users=userFilter(users);
		Map <String,List> orgTreeTemp=new HashMap<String, List>();
		for(int i=0;i<users.size();i++){
			Map user =(Map) users.get(i);
			String userStr=(String)user.get("org");
		    if(userStr!=null&&userStr.length()>0){
		    	String usersArr[]=userStr.split(","); 
		    	for(int j=0;j<usersArr.length;j++){
		    	    String orgId=usersArr[j];
		    	    List org2userList=  orgTreeTemp.get(orgId)==null?new ArrayList():orgTreeTemp.get(orgId);
		    		Map newMap=new HashMap();
			    	newMap.putAll(user);
			    	newMap.put("org", orgId);
			    	org2userList.add(newMap);
		    		orgTreeTemp.put(orgId, org2userList);
		    	}
		    }
		}
		// 返回数据格式 HASHmap  {组织id:[{用户数据}，用户2，用户3]}
		return orgTreeTemp;
	}
	
	
	/**组织——用户列表接口  
	**/
	public Map getOrgAndUserList(Map params){
		Map map=new HashMap();
		map.put("recordType", "org");
		List orgs= getAllOrg();       

		
		List users=getServiceSqlSession().db_selectList("getAllUser");
		
		Map org2user=getAllOrg2UserInfo(users);
		
		Map resultMap=new HashMap();
		for(int i=0;i<orgs.size();i++){
			Map org=(Map) orgs.get(i);
			String id=String.valueOf(org.get("id"));
			String name=String.valueOf(org.get("text"));
			Map orginfo=new HashMap();
			orginfo.put("name", name);
			List user=(List) org2user.get(id);
			if(user!=null){
				orginfo.put("users", user);
			}
			resultMap.put(id, orginfo);
		}
		return resultMap;
		
		
	}
	public static void main(String[] args) {
		ZkmTreeCommonService zx=new ZkmTreeCommonService();
		Map m=zx.getOrgAndUserList(new HashMap());
		System.out.println(m);
	}
    
	
	//用户节点  修改或过滤
//    private List userFilter(List users){
//		
//        for(int i=0;i<users.size();i++){
//             Map map=(Map)users.get(i);
//             map.put("icon", "/ZDesk/js/zTree/zTreeStyle/img/diy/icon_unavailable.gif");
//        }
//		return users;
//    }



}
