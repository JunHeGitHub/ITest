package com.zinglabs.apps.itemAllocaion.action;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.appCommonCurd.AppCommonsCurdService;
import com.zinglabs.apps.itemAllocaion.peizhiService;
import com.zinglabs.base.core.frame.AppLogImpl;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.util.JsonUtils;


@Controller
@RequestMapping("/*/itemAllocaion")
public class peizhiAction extends BaseAction {	
	/**
	 * 增/改
	 * @param request
	 * @param response
	 * @return
	 */	
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
	 * 修改
	 * @param request
	 * @param response
	 * @return
	 */	
	@RequestMapping(value = "/update")
	public void appEdit(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		getService().Update(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "修改成功"),response);
	}

	public peizhiService getService() {
		return (peizhiService) getBean("peizhiService");
	}


}
