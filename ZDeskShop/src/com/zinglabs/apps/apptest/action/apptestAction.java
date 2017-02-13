package com.zinglabs.apps.apptest.action;


import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.appCommonCurd.AppCommonsCurdService;
import com.zinglabs.apps.apptest.TestService;
import com.zinglabs.base.core.frame.AppLogImpl;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.util.JsonUtils;


@Controller
@RequestMapping("/*/ZKMSpringTest")
public class apptestAction extends BaseAction{
	
	/**
	 * 测试最简单的访问
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/test")
	public String sayHello(HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("hello", "annot say hello.");
		return "sayHello.jsp";
	}
	
	/**
	 * 测试参数类入HashMap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/test2")
	public String sayHello(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("HashMap", map);
		return "HashMap.jsp";
	}
	
	/**
	 * 测试参数类 获取个别参数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/test3")
	public String sayHello(@RequestParam String say,HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("hello", say);
		return "sayHello.jsp";
	}
	
	/**
	 * 测试参数类 获取个别参数
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/test4")
	public String seeMapperId(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		
		return "sayHello.jsp";
	}
	
	/**
	 * 增/改
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/save")
	public void testappAdd(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		String mgs="";
		if(map!=null){
			String id="";
			id=(String)map.get("id");
			if(id!=null && id.length()>0){
				getService().testAdd(map);
				mgs="修改成功";
			}else{
				getService().testUpdate(map);
				mgs="添加成功";
			}
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, mgs),response);
	}
	
	/**
	 * 删
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delete")
	public void testappDelete(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		getService().testDelete(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功"),response);
	}
	
	/**
	 * 查
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/search")
	public void testappSearch(HttpServletRequest request,HttpServletResponse response){
		List list =getService().testSelectAll();
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", list),response);
	}
	
	public TestService getService() {
		return (TestService) getBean("testService");
	}

}
