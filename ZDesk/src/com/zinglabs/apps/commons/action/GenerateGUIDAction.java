package com.zinglabs.apps.commons.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;

@Controller
@RequestMapping(value="/*/CommonGenerateGUID")
public class GenerateGUIDAction extends BaseAction {
	@RequestMapping(value="generateGUID")
    public void generateGUIDforNum(HttpServletRequest request, HttpServletResponse response){
    	List list =new ArrayList();
    	int num=0;
    	try{
    		 num =request.getParameter("generateNumber")==null?1:Integer.valueOf((String)request.getParameter("generateNumber"));
    	}catch (Exception e) {
    		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "生成数量转换异常"), response);
    		return ;
		}
    	
    	
        for(int i=0;i<num;i++){
           	list.add(new RandomGUID().toString());
        }	
    	postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",list), response);
    }
}
