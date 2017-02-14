package com.zinglabs.apps.suPermission.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;



import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.base.core.utils.AppSpringUtils;


/**
 * 权限帮助类 
 * @author Guozhiyuan
 *
 */
@SuppressWarnings("unchecked")
public class PermissonHelper extends BaseService{
	
	private static AppSqlSessionDbid appSqlSession;
    
    public void init(){
    	appSqlSession=(AppSqlSessionDbid) this.getBean("suPermissSqlsession");	
    }
    
    static{
    	
    	
    }
   /**
    * 权限授权
    * @param permisstionCodes 权限码   String[]数组
    * @param roleId           角色id   String
    * @param type             权限类型  String
    *
    * @return  1   成功  -1 失败
    */
      
    public static int roleMapPermisstion(String permisstionCodes[], String roleId,String type){
		 if(permisstionCodes!=null&&permisstionCodes.length>0&&roleId!=null&&!"".equals(roleId)&&type!=null&&!"".equals(type)){
			     List<Map<String,String>>itemList=new ArrayList<Map<String,String>>();
			     Map pMap=new HashMap();
			     for(int i=0;i<permisstionCodes.length;i++){ 
			    	 Map<String,String> map=new HashMap<String,String>();
			    	 if("".equals(permisstionCodes[i])){
			    		 return -1; 
			    	 }
			    	 map.put("permissionCode", permisstionCodes[i]);
			    	 map.put("roleCode", roleId);
			    	 map.put("type", type);
			    	 itemList.add(map);
			     }
			    pMap.put("itemList", itemList);
			    //授权
			    appSqlSession.db_insert("givePermission",pMap); 
				return 1;
		 }
		 return -1;
	 }
    /**
     * 授权操作     
     * @param permisstionCode    权限码    String
     * @param roleId             角色code  String
     * @param type               权限类型  String
     * @return  1   成功  -1 失败
     */
    
	 public static int roleMapPermisstion(String permisstionCode, String roleId,String type){
         String permisstionCodes[]=new String[]{permisstionCode};
		 return roleMapPermisstion(permisstionCodes,roleId,type);
	 }

	 
	 /***  
	  * 根据角色码集合获取权限集合
	  * @param roles  String[]
	  * @return  List<Map<权限码字段,权限码>>
	  */
	 
	 public static List<Map<String,String>> getPermisstionByRoles(String roles[],String type){
		 //查询参数
		 Map searchParam=new HashMap();
		 searchParam.put("roleList", roles);
		 searchParam.put("type", type);
		 List rList= appSqlSession.db_selectList("getPermission",searchParam);
		 //获取权限集合
		 return rList;
	 }
	 /**
	  * 根据角色和type获取权限
	  * @param roles
	  * @param type
	  * @return List<权限码(String)>
	  */
	 public static List<String> getPermisstionCodeByRole(String role,String type){
		 ;
		 //查询参数
		 Map searchParam=new HashMap();
		 searchParam.put("roleCode", role);
		 searchParam.put("type", type);
		 List permisstionList= appSqlSession.db_selectList("getPermissionId",searchParam);
		// Set <String> set=permisstionMap.keySet(); 
		 List<String> list =new ArrayList();
		 for(int i=0;i<permisstionList.size();i++){
			 Map map=(Map) permisstionList.get(i);
			 if(!map.isEmpty()){
				 String permisstionCode=(String)map.get("permissionCode");
				 list.add(permisstionCode);
			 }
		 }
		 
		 return list;
	 }
	 
	 /**
	  * 根据roleName 获取权限结合
	  * 
	  * @param role
	  * @return Map <权限类型，权限集合>
	  */
	 public static Map<String,List> getPermisstionAllByRole(List <String> roles){
		 //查询参数
		 Map searchParam=new HashMap();
		 searchParam.put("roleList", roles);
		 List<Map>permisstionList= appSqlSession.db_selectList("getAllPermission",searchParam);
		 int size=permisstionList.size();
		 Map <String,List>rMap=new HashMap();
		  for(int i=0;i<size;i++){
			 Map listMap=permisstionList.get(i);
			 String type=(String)listMap.get("typeName");
			 if(!rMap.containsKey(type)){
				 List list=new ArrayList();
				 list.add(listMap);
				 rMap.put(type, list);
				 
			 }else{
				 List list=rMap.get(type);
				 list.add(listMap);
			 }		 
		 }
		 return rMap;
	 }
	 
	 /**
	  * 根据登陆账号获取权限集合
	  * @param loginName
	  * @return  Map<权限id，权限集合List<Map>>
	  */
	 
	 public static Map<String,List> getPermisstionByLoginName(String loginName){
		 HashMap map=new HashMap();
		 map.put("loginName", loginName);
		 
		 //获取给用户角色
		 List<String> roles= UserInfoService.getRoleListByLoginName(loginName);
		 
		 if(roles!=null&&roles.size()>0){
			 Map<String,List> permisstions=getPermisstionAllByRole(roles);
			 //System.out.println(permisstions.get("menu"));
			 //TODO List<权限码>
			 List list= getPermisstionByRole(roles,"menu"); 
		
			 permisstions.put("menu", list);
			  return  permisstions;
		 }
		 return new HashMap<String, List>();
		
	 }
	 /**
	  * 根据角色和type获取权限
	  * @param roles
	  * @param type
	  * @return List<Map> 此用户某类型的所有权限码
	  */
	 public static List<Map<String,String>> getPermisstionByRole(List roles,String type){
		 //查询参数
		 Map searchParam=new HashMap();
		 searchParam.put("roleList", roles);
		 searchParam.put("type", type);
		 List rList= appSqlSession.db_selectList("getPermission",searchParam);
		 return rList;
	 }
	 /**
	  * 根据角色和type获取权限
	  * @param roles  String[]
	  * @param type
	  * @return List<Map> 此用户某类型的所有权限码
	  */
	 public static List<Map<String,String>> getPermisstionByRole(String roles[],String type){
	      List roleList =new ArrayList();
	      for(int i=0;i<roles.length;i++){
	    	  roleList.add(roles[i]); 
	      }
		 return getPermisstionByRole(roleList,type);
	 }
	 /**
	  * 根据角色删除权限授权
	  * @param roleCode 角色id
	  * @param type 权限类型
	  */
	 public  static int deauthorizeByRoleCode(String roleCode,String type){
		 Map <String,String>map=new HashMap<String,String>();
		 map.put("roleCode", roleCode);
		 map.put("type", type);
		 return appSqlSession.db_delete("deauthorize",map);
	 }
	 /**
	  * 根据角色删除权限授权
	  * @param roleCode 角色id
	  * @param type 权限类型
	  * @param pcodes 权限id
	  */
	 
	 public  static int deauthorize(String roleCode,String type,String[]pcodes){
		 Map <String,Object>map=new HashMap<String,Object>();
		 map.put("roleCode", roleCode);
		 map.put("type", type);
		 map.put("permissionCodes", pcodes);
		 return appSqlSession.db_delete("deauthorize",map);
	 }
     
	 
	 /**
	  * 验证此用户是否有此权限
	  * @return true 有此权限
	  *         false  没有此操作权限
	  */
	 public static  boolean isHavePermission(String loginName,String permissionCode){
		 //获取角色
		 List<String> roles= UserInfoService.getRoleListByLoginName(loginName);
		 //全部权限
		 List<String> allPermisstionCode=new ArrayList<String>();
		 for(int i=0;i<roles.size();i++){
			 List<String> list= getPermisstionCodeByRole(roles.get(i),"");
			 allPermisstionCode.addAll(list);
		 }
		 if(allPermisstionCode.contains(permissionCode))
			   return true;
		 return false;
	 }  
     
	 /**
	  * 根据角色获取菜单
	  * @param roles
	  * @param type
	  * @return
	  */
	 public static  List  getTreeByRole(List roles){
		  int size=roles.size();
		  String roleArr[]=new String[size];
	      for(int i=0;i<size;i++){
	    	  roleArr[i]=(String) roles.get(i);  
	      }
		 return getTreeByRole(roleArr);
	 }
	 /**
	  * 根据角色获取菜单
	  * @param roles
	  * @param type
	  * @return  List<Map> 权限码
	  */
	 public static  List  getTreeByRole(String roles[]){
		 List <Map<String,String>>list=getPermisstionByRole(roles,"menu");
		 List rlist =new ArrayList();
		 for(Map map:list){
			 rlist.add(map.get("permissionCode"));
		 }
		 return rlist;
		 
	 }
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
			return (AppSqlSessionDbid)getBean("suPermissSqlsession");
	}
	 public static void main(String[] args) {
	    	 ApplicationContext context=AppSpringUtils.getContextAsClassPath(new String[]{"appConf/applicationContext.xml","classpath:com/zinglabs/apps/suPermission/defXml/*-beans.xml"});
	    	// PermissonHelper ph=(PermissonHelper)context.getBean("permissonHelper");
	    	// Map rList= appSqlSession.selectMap("test","id");
	    	// System.out.println(rList);
//	    	 String s[]=new String[]{"1","2","3","4","5"};
//	    	 //int i= ph.treeAuthorization(s, "test","tt");
//	    	 int i =PermissonHelper.roleMapPermissin(s, "YYZX", "tree");
//	    	 PermissonHelper.roleMapPermissin(s, "ZQC", "Basis");
//	    	 PermissonHelper.roleMapPermissin(s, "admin", "Basis");
//	    	 System.out.println(i);
	    	 Map map=new HashMap();
	    	 String []s=new String[]{
	    			 "测试账号",
	    			 "测试角色" 			 
	    	 };
	    	 
	    System.out.println(getPermisstionByLoginName("admin"));	;
	    	 //getPermisstionAllByRole(s);
	    	 //getPermisstionByRole("公告管理员","base");
	    
	    	// getPermisstionAllByRole();
	    	 	
	    	 //List roleList=new ArrayList();
			// roleList.add("admin");
			// roleList.add("WBZX");
//	    	 String s[]=new String[]{"admin","WBZX"};
//			 map.put("roleList", s);
//			 map.put("type", "menu");
//			 List rList= appSqlSession.selectList("getPermission",map);
//			 System.out.println(rList);
		}
}
