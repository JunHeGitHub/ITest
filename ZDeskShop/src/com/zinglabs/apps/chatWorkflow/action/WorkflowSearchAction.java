package com.zinglabs.apps.chatWorkflow.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.chatWorkflow.WorkflowSearchService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;



@Controller
@RequestMapping("/*/WorkflowSearch")
public class WorkflowSearchAction extends BaseAction {
	
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	
	@RequestMapping(value="/MyWorkflowSearch")
	public void MyWorkflowSearch(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response){
		WorkflowSearchService ws=new WorkflowSearchService();
		
		Map m=ws.MyWorkSearch(map, request);
		
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",m), response);
		
	}

}
