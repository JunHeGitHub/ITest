package com.zinglabs.apps.ZhongHangYeMian.action;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.appCommonCurd.AppCommonsCurdService;
import com.zinglabs.apps.ZhongHangYeMian.TestService;
import com.zinglabs.base.core.frame.AppLogImpl;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.util.JsonUtils;


@Controller
@RequestMapping("/*/ZhongHangYeMian")
public class ZhongHangAction extends BaseAction {
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
		try {
			getService().testAdd(map);
		} catch (Exception e) {
			e.printStackTrace();
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "增加失败"),response);
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "增加成功"),response);
	}
	
	@RequestMapping(value = "/add")
	public void appAdd(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){		
		try {
			getService().Add(map);
		} catch (Exception e) {
			e.printStackTrace();
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "增加失败"),response);
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "增加成功"),response);
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
	 * 修改
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public void testappEdit(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		getService().testUpdate(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "修改成功"),response);
	}
	@RequestMapping(value = "/update")
	public void appEdit(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		getService().Update(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "修改成功"),response);
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
		return (TestService) getBean("zhService");
	}


}
