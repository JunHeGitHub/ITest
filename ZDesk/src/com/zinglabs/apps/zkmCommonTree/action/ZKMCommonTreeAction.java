package com.zinglabs.apps.zkmCommonTree.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.i18nPrompt.I18nPromptUtils;
import com.zinglabs.apps.suPermission.service.UserInfoService;
import com.zinglabs.apps.zkmCommonTree.ZkmTreeCommonService;
import com.zinglabs.apps.zkmCommonTree.commonTreeFilter.ZkmCommonTreeFilter;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.db.UserInfo2;
import com.zinglabs.util.CookieUtils;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;

@Controller
@RequestMapping("/*/ZKMCommonTree")
public class ZKMCommonTreeAction extends BaseAction {
    public static  Logger logger = LoggerFactory.getLogger("ZDesk");
    
	@RequestMapping(value = "/getTreeNode")
	public void getTreeNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		String roleName = map.get("roleName")==null?"":(String) map.get("roleName");
		roleName=I18nPromptUtils.ascii2Native(roleName);
		map.put("roleName", roleName);
		String beanName =map.get("beanName")==null?"":(String) map.get("beanName");
	
		//排序字段
		String treeSort=map.get("sortField")==null?"sortField":(String)map.get("sortField");
		if(treeSort.equals("")){
			treeSort="sortField";			
		}
		map.put("sortField", treeSort);
		List<HashMap<String, String>> zkmInfoBasePojoList = getService().getTreeNode(map);
     	if (beanName == null || beanName.trim().length() <= 0) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmInfoBasePojoList), response);
			
		} else {
		if (roleName == null || roleName.trim().length() <= 0) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数"), response);
		} else {
				ZkmCommonTreeFilter zkmCommonTreeFilter = (ZkmCommonTreeFilter) getFiterService(request, beanName);
				//获取过滤后的节点
				List<HashMap<String, String>> zkmCommonNodeList = zkmCommonTreeFilter.commonTreeFilter(map, zkmInfoBasePojoList);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmCommonNodeList), response);
		}
		}
	}
	//质检用
	@RequestMapping(value = "/getZhijianTreeNode")
	public void getZhijianTreeNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {		
		List<HashMap<String, String>> zkmInfoBasePojoList = getService().getZhijianTreeNode(map);    			
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmInfoBasePojoList), response);				
	}
	//菜单管理
	@RequestMapping(value = "/getCaidanTreeNode")
	public void getCaidanTreeNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {		
		List<HashMap<String, String>> zkmInfoBasePojoList = getService().getCaidanTreeNode(map);    			
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmInfoBasePojoList), response);				
	}
	@RequestMapping(value = "/getCaidanTreeNode_ById")
	public void getCaidanTreeNode_ById(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {		
		List<HashMap<String, String>> zkmInfoBasePojoList = getService().getCaidanTreeNode_ById(map);    			
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmInfoBasePojoList), response);				
	}
	@RequestMapping(value = "/getCaidanTreeNode_isStart")
	public void getCaidanTreeNode_isStart(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {		
		List<HashMap<String, String>> zkmInfoBasePojoList = getService().getCaidanTreeNode_isStart(map);    			
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmInfoBasePojoList), response);				
	}
	@RequestMapping(value = "/getCaidanTreeNode_isStart1")
	public void getCaidanTreeNode_isStart1(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {		
		List<HashMap<String, String>> zkmInfoBasePojoList = getService().getCaidanTreeNode_isStart_child(map); 
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmInfoBasePojoList), response);				
	}
	@RequestMapping(value = "/getCaidanTreeNode_geshu")
	public void getCaidanTreeNode_geshu(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {		
		//根菜单集合
		List<HashMap<String, String>> zkmInfoBasePojoList = getService().getCaidanTreeNode_geshu(map);
		//子菜单集合
		List<HashMap<String, String>> zkmInfoBasePojoList2 = getService().getCaidanTreeNode_isStart_child(map);
		//转json字符串
		String menuData = JsonUtils.genUpdateDataReturnJsonStr(zkmInfoBasePojoList);
		String subMenuData = JsonUtils.genUpdateDataReturnJsonStr(zkmInfoBasePojoList2);
		
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", "{menuData:"+menuData+",subMenuData:"+subMenuData+"}"), response);				
	}
	@RequestMapping(value = "/getCaidanTreeNode_geshu_isStart")
	public void getCaidanTreeNode_geshu_isStart(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {		
		List<HashMap<String, String>> zkmInfoBasePojoList = getService().getCaidanTreeNode_geshu_isStart(map);    			
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmInfoBasePojoList), response);				
	}
	@RequestMapping(value = "/getCaidanTreeNode_geshu_child")
	public void getCaidanTreeNode_geshu_child(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {		
		List<HashMap<String, String>> zkmInfoBasePojoList = getService().getCaidanTreeNode_geshu_child(map);    			
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmInfoBasePojoList), response);				
	}
	//质检用
	@RequestMapping(value = "/getZhijianAllNode")
	public void getZhijianAllNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {		
		List<HashMap<String, String>> zkmInfoBasePojoList = getService().getZhijianTreeNode(map);  
		for(int i=0;i<zkmInfoBasePojoList.size();i++){
			if(zkmInfoBasePojoList.get(i).get("value_remark").indexOf("\r\n")!=-1){
				zkmInfoBasePojoList.get(i).put("value_remark", zkmInfoBasePojoList.get(i).get("value_remark").replaceAll("\r\n", "\\u000d\\u000a"));
				
			}
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmInfoBasePojoList), response);				
	}
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getSuSecurityPermissionList.action")
	public void getSuSecurityPermission(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		List<HashMap<String, String>> zkmSuSecurityList = getService().getSecurityPermissionTreeNode(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmSuSecurityList), response);
	}
	//获取配置表的列表
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getPeiZhiCommonTreeList.action")
	public void getPeiZhiCommonTreeList(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		List<HashMap<String, String>> zkmSuSecurityList = getService().getPeiZhiCommonTreeList(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmSuSecurityList), response);
	}
	@RequestMapping(value = "/getCommonTreeParam")
	public void getCommonTreeParam(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
	/**	UserInfo2 user = getTTSessionUser(request);
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}**/
	
		HashMap<String, String> zkmCommonTreeParam = getService().getCommonTreeParam(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmCommonTreeParam), response);
	}
	@RequestMapping(value = "/getCommonTreeParamList")
	public void getCommonTreeParamList(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		/**UserInfo2 user = getTTSessionUser(request);
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}**/
		List<HashMap<String, String>> zkmCommonTreeParamList = getService().getCommonTreeParamList(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmCommonTreeParamList), response);
	}
	@RequestMapping(value = "/addTreeNode")
	public void addTreeNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		/**UserInfo2 user = getTTSessionUser(request);
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}**/
		//String pid=request.getParameter("parentId");
		//map.put("createUser", user.loginName2);
		String uuid=new RandomGUID().toString();
		map.put("id",uuid);
		//if(pid==null)
			//pid="";
		//map.put("parentId", pid);
        int b=getService().addTreeNode(map);
        if(b!=0)
		   postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "添加成功",uuid), response);
        else
         postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "添加失败"), response);
	}
	@RequestMapping(value = "/addNode_zhijian")
	public void addNode_zhijian(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
        int b=getService().addTreeNode_zhijian(map);
        if(b!=0)
		   postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "添加成功",b), response);
        else
         postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "添加失败"), response);
	}
	/**
	 * 组织机构修改节点信息
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateTreeNode")
	public void updateTreeNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		/**UserInfo2 user = getTTSessionUser(request);
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}**/
	
		 int b= getService().updateTreeNode(map);
		 if(b!=0)
		  postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "更改成功"), response);
	}
	/**
	 * 组织机构修改节点名称信息
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/updateTreeNodeName")
	public void updateTreeNodeName(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		/**UserInfo2 user = getTTSessionUser(request);
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}**/
	
		 int b= getService().updateTreeNodeName(map);
		 if(b!=0)
		  postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "更改成功"), response);
	}
	/**
	 * 组织机构修改节点(通过id首先找到修改的节点，为了带到model里面)
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/selectTreeNodeById")
	public void selectTreeNodeById(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		/**UserInfo2 user = getTTSessionUser(request);
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}**/
		List<HashMap<String, String>> zkmCommonTreeParamList = getService().selectTreeNodeById(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmCommonTreeParamList), response);
		 
	}
	@RequestMapping(value = "/deleteTreeNode")
	public void deleteTreeNode(HttpServletRequest request, HttpServletResponse response) {
		/**UserInfo2 user = getTTSessionUser(request);
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}**/
		String ids=request.getParameter("ids");
		String idsArr[]=ids.split(",");
		Map <String, String>map=new HashMap<String, String>();
		for(int i=0;i<idsArr.length;i++){
			map.put("id", idsArr[i]);
			getService().deleteTreeNode(map);
			map.clear();
		}
		//int b= getService(request).deleteTreeNode();
//		if(b!=0)
   	   postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功"), response);
	}
	@RequestMapping(value = "/deleteTreeNode_ZJ")
	public void deleteTreeNode_ZJ(HttpServletRequest request, HttpServletResponse response) {
		/**UserInfo2 user = getTTSessionUser(request);
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}**/
		String ids=request.getParameter("ids");
		String idsArr[]=ids.split(",");
		Map <String, String>map=new HashMap<String, String>();
		for(int i=0;i<idsArr.length;i++){
			map.put("id", idsArr[i]);
			getService().deleteTreeNode_ZJ(map);
			map.clear();
		}
		//int b= getService(request).deleteTreeNode();
//		if(b!=0)
   	   postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功"), response);
	}
	@RequestMapping(value = "/deleteTreeNode_caishu")
	public void deleteTreeNode_caishu(HttpServletRequest request, HttpServletResponse response) {
		/**UserInfo2 user = getTTSessionUser(request);
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}**/
		String ids=request.getParameter("ids");
		String idsArr[]=ids.split(",");
		Map <String, String>map=new HashMap<String, String>();
		for(int i=0;i<idsArr.length;i++){
			map.put("id", idsArr[i]);
			getService().deleteTreeNode_caidan(map);
			map.clear();
		}
		//int b= getService(request).deleteTreeNode();
//		if(b!=0)
   	   postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功"), response);
	}
	@RequestMapping(value = "/addzuzhijigouTreeNode")
	public void addzuzhijigouTreeNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		//UserInfo2 user = getTTSessionUser(request);
//		if (user == null || "_guest".equals(user.loginName2)) {
//			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
//			return;
//		}
		Object pidObj = map.get("parentId");
		String pid ="";
			pid = pidObj.toString();
		map.put("parentIndex", pid);
		map.put("code", RandomGUID.getUinAA());
        int b=getService().addzuzhijigouTreeNode(map);
        if(b!=0)
		   postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "添加成功",map.get("code").toString()), response);
        else
         postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "添加失败"), response);
	}
	@RequestMapping(value = "/updatezuzhijigouTreeNode")
	public void updatezuzhijigouTreeNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
//		UserInfo2 user = getTTSessionUser(request);
//		if (user == null || "_guest".equals(user.loginName2)) {
//			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
//			return;
//		}
		 int b= getService().updatezuzhijigouTreeNode(map);
		 if(b!=0)
		  postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "更改成功"), response);
	}
	/**
	 * 删除节点(递归删除)
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/deletezuzhijigouTreeNode")
	public void deletezuzhijigouTreeNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
	/**	UserInfo2 user = getTTSessionUser(request);
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String ids=(String)map.get("id");		
		String[] idsArr=ids.split(",");
		List <String> list =new ArrayList<String>();
		for(int i=0;i<idsArr.length;i++){
			list.add(idsArr[i]);
		}**/
		List<HashMap<String, String>> zkmInfoBasePojoList = getService().getTreeNodeByPidIsId(map);//通过pid=id
		getService().deletezuzhijigouTreeChildNode(map);
	    for(int i=0;i<zkmInfoBasePojoList.size();i++){
	    	HashMap m=new HashMap();
	    	m.put("id",zkmInfoBasePojoList.get(i).get("id"));
	    	deletezuzhijigouTreeNode(m,request,response);
	    	
	    }
			//postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", zkmInfoBasePojoList), response);
		  
		//int b= getService(request).deleteTreeNode();
//		if(b!=0)
   	   postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功！"), response);
	}
	 @RequestMapping(value = "/deleteZJTreeNode")
		public void deleteZJTreeNode(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
			List<HashMap<String, String>> zkmInfoBasePojoList = getService().getTreeNodeByPidIsId_zj(map);//通过pid=id
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getService().deleteZjChildNode(map);
		    for(int i=0;i<zkmInfoBasePojoList.size();i++){
		    	HashMap m=new HashMap();
		    	m.put("id",zkmInfoBasePojoList.get(i).get("id"));
		    	deleteZJTreeNode(m,request,response);
		    }
	   	   postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功！"), response);
		}
	 
	/**
	 * 拖拽节点
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/treeNodeClassification")
	public void treeNodeClassification(HttpServletRequest request, HttpServletResponse response) {
		/**UserInfo2 user = getTTSessionUser(request);
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}**/
		String treeNode = request.getParameter("treeNode");
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(treeNode);
		List<HashMap> treeNodeList = (List<HashMap>) jsonArray.toList(jsonArray, HashMap.class);
		String ids = request.getParameter("ids");
		if (treeNode == null || treeNodeList.size() != 1 || ids==null || ids.trim().length()<1) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "参数错误！"), response);
		} else {
			getService().treeNodeClassification(treeNodeList.get(0),ids);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, ""), response);
		}
	}
	/**
	 * 其他节点拖拽到根节点的特殊位置时，做排序操作
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/treeNodeClassificationRoot")
	public void treeNodeClassificationRoot(HttpServletRequest request, HttpServletResponse response) {
		    String ids = request.getParameter("ids");
			getService().treeNodeClassification(ids);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, ""), response);
	}
	
	/**
	 * 组织机构删除树
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/commonTreeDelete")
	public void commonTreeDelete(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		getService().commonTreeDelete(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功！"), response);
	}
	@RequestMapping(value = "/commonTreeDeleteByRecordType")
	public void commonTreeDeleteByRecordType(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		getService().commonTreeDeleteByRecordType(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功！"), response);
	}
	
	/**
	 * 其他节点拖拽到根节点的位置将数据库的pid置为''
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/treeNodeClassificationUpdatePidIsNull")
	public void treeNodeClassificationUpdatePidIsNull(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		getService().treeNodeClassificationUpdatePidIsNull(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "修改成功！"), response);
	}
	@RequestMapping(value = "/commonTreeSave")
	public void commonTreeSave(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
		boolean save = getService().commonTreeSave(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(save, null), response);
	}
	
	public ZkmTreeCommonService getService() {
		return (ZkmTreeCommonService) getBean("zkmCommonTreeService");
	}
	public ZkmCommonTreeFilter getFiterService(HttpServletRequest request, String beanName) {
		return (ZkmCommonTreeFilter) getBean(beanName);
	}
	
	/**
	 * 用户组织树
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getUserMappingOrgTree")
	public void getUserMappingOrgTree(@RequestParam HashMap map,HttpServletRequest request, HttpServletResponse response) {
      
		try {
		String loginName=CookieUtils.getCookieVal(request);
		if(loginName!=null&&!"".equals(loginName)){
			//map.put("loginName", loginName);
			Map userInfo=UserInfoService.getUserInfoByLoginName(loginName);
			String companyId=String.valueOf(userInfo.get("companyId"));
			String departmentId=String.valueOf(userInfo.get("departmentId"));
			//map.putAll(userInfo);
			map.put("companyId", companyId);
			map.put("departmentId", departmentId);
			map.put("loginName", loginName);
			
			
			List list=getService().getUserMapOrgTree(map);
		   	postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"",list), response);
			
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"请重新登录系统"), response);
		}
		
		logger.debug("getUserMappingOrgTree getLoginNameBycookie:"+loginName);
		} catch (Exception e) {
			logger.error("ZKMCommonTreeAction getUserMappingOrgTree:"+e.getMessage());
			e.printStackTrace();
		}
		
	
	}
	public static void main(String[] args) {
		
	}
}
