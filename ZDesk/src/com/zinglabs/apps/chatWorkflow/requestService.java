package com.zinglabs.apps.chatWorkflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.validator.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class requestService extends BaseService {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	public List reSelect(Map<String, String> map){
		return getServiceSqlSession().db_selectList("requestSelect",map);
	}
	//删除选中角色已有的分类权限
	public int deCategory(Map<String, String> map){
		if(map.get("roleCode")==null || map.get("roleCode").length()<=0
				||map.get("roleCode").equals("null")){
			return 0;
		}else{
			return getServiceSqlSession().db_delete("deleteCategory", map);
		}
	}
	//验证已选中模块下的描述是否有权限
	public List checkDesc(Map<String, String> map){
		return getServiceSqlSession().db_selectList("checkCategoryDesc",map);
	}
	//选择人员时获取人员信息
	public List searchUser(Map<String, String> map){
		return getServiceSqlSession().db_selectList("searchUser",map);
	}
	//选择人员时获取人员信息
	public Map getNodeDataByTemplateId(String templateid){
		Map<String, String> map=new HashMap<String, String>();
		map.put("templateid", templateid);
		List ls=getServiceSqlSession().db_selectList("getNodeDataByTemplateId",map);
		logger.debug("查看是否获取到节点数据（未进行筛选前）：ls:"+ls);
		List<Map<String, String>> ll=ls;
		Map<String, String> m =new HashMap<String, String>(); 
		if(ll!=null){
			if(ll.size()>0){
				for (int i = 0; i < ll.size(); i++) {
					String id = ll.get(i).get("id");
					logger.debug("查看节点id="+id);
					String tt="".equals(id) || id==null?"":id.substring(0,3);
					logger.debug("查看节点id前缀是否为jnz。tt="+tt);
					if(tt.equals("jnz")){
						m.put("id", ll.get(i).get("id"));
						m.put("templateId", ll.get(i).get("templateId"));
						m.put("stepName", ll.get(i).get("stepName"));
						m.put("executor_name", ll.get(i).get("executor_name"));
						m.put("executor", ll.get(i).get("executor"));
						m.put("description", ll.get(i).get("description"));
						m.put("createDate", ll.get(i).get("createDate"));
						m.put("serial", ll.get(i).get("serial"));
						m.put("companyId", ll.get(i).get("companyId"));
					}
				}
			}
		}
		return m;
	}

	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		// TODO Auto-generated method stub
		return (AppSqlSessionDbid)this.getBean("requestservice");
	}
	public static void main(String[] args) {
		requestService rs =new requestService();
		System.err.println(rs.getNodeDataByTemplateId("1081"));
	}
}
