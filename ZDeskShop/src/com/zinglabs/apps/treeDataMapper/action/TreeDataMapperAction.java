package com.zinglabs.apps.treeDataMapper.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.treeDataMapper.service.TreeDataMapperService;
import com.zinglabs.apps.treeDataMapper.treeDataFilter.TreeDataFilter;
import com.zinglabs.apps.zkmCommonTree.commonTreeFilter.ZkmCommonTreeFilter;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping("/*/TreeDataMapper")
public class TreeDataMapperAction extends BaseAction{
	
	public TreeDataMapperService getService() {
		return (TreeDataMapperService) getBean("treeDataMapperService");
	}
	
	/*
	 *绑定数据 
	 */
	@RequestMapping(value="/doDataBinding")
	public void doDataBinding(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		boolean flag=checkMap(map,response);
		if(flag){
			TreeDataFilter treeDataFilter = getFiterService((String) map.get("beanName"));
			boolean f=treeDataFilter.doDataBinding(map);
			if(f){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"绑定数据成功"),response);
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"绑定数据失败"),response);
			}
		}else{
			return;
		}
	}
	/*
	 * 解绑数据
	 */
	@RequestMapping(value="/doDataUnbinding")
	public void doDataUnbinding(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		boolean flag=checkMap(map,response);
		if(flag){
			TreeDataFilter treeDataFilter = getFiterService((String) map.get("beanName"));
			boolean f=treeDataFilter.doDataUnbinding(map);
			if(f){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"解绑数据成功"),response);
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"解绑数据失败"),response);
			}
		}else{
			return;
		}
	}
	
	/*
	 * 根据节点id(nodeId）查询关联表数据
	 */
	@RequestMapping(value="/getDataByNodeId")
	public void getDataByNodeId(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		Map rmap=getService().getDataByNodeId(map);
		postStrToClient(rmap, response);
	}
	
	/*
	 * 根据数据id(dataId）查询关联表数据
	 */
	@RequestMapping(value="/getDataByDataId")
	public void getDataByDataId(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		Map rmap= getService().getDataByDataId(map);
		postStrToClient(JSONSerializer.toJSON(rmap),response);
	}
	
	/*
	 * 获取分类树
	 */
	@RequestMapping("/getTemplateTree")
	public void getTemplateTree(HttpServletRequest request,HttpServletResponse response){
		Map map = this.getService().getTemplateTree();
		postStrToClient(JSONSerializer.toJSON(map).toString(), response);
	}
	/*
	 * 获取节点名称
	 */
	@RequestMapping("/getTreeNodeText")
	public void getTreeNodeText(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		Map rmap = this.getService().getTreeNodeText(map);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}
	/*
	 * 查询数据
	 */
	@RequestMapping("/getDataList")
	public void getDataList(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response) {
		if(map.get("beanName")==null||map.get("beanName").equals(null)){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数:beanName"), response);
			return;
		}
		if(map.get("tableName")==null||map.get("tableName").equals(null)||map.get("tableName").equals("")){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数:tableName"), response);
			return;
		}
		TreeDataFilter treeDataFilter = getFiterService((String) map.get("beanName"));
		Map rmap=treeDataFilter.getDataList(map);
		postStrToClient(rmap,response);
	}
	
	/*
	 * 验证数据
	 */
	public boolean checkMap(Map map,HttpServletResponse response){
		if(map.get("beanName")==null||map.get("beanName").equals(null)){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数:beanName"), response);
			return false;
		}
		if(map.get("nodeIdList")==null||map.get("nodeIdList").equals(null)){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数:nodeIdList"), response);
			return false;
		}
		if(map.get("dataIdList")==null||map.get("dataIdList").equals(null)){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数:dataIdList"), response);
			return false;
		}
		return true;
	}
	
	public TreeDataFilter getFiterService(String beanName) {
		return (TreeDataFilter) getBean(beanName);
	}
	
}
