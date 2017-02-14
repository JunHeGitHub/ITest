package com.zinglabs.apps.zshdSearch.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.zshdSearch.QuestionService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;
@Controller
@RequestMapping("/*/LH")
public class QuestionAction extends BaseAction{
	public static Logger logger=LoggerFactory.getLogger("ZDesk");
	//根据条件查询  高级查询
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/searchQuestion")
	public void searchQuestion(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		List mapList = getService().search(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response) ;
	}
	
	//根据条件查询  积分流水查询
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/searchIntegral")
	public void searchIntegral(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		List mapList = getService().searchIntegral(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response) ;
	}
	//根据时间段查询我的问题
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/searchMyQuestion")
	public void searchMyQuestion(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		List mapList =getService().seletMyQuestion(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"",mapList), response);
	}
	//我关注的问题查询
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/attentionQuestion")
	public void searchAttention(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		List mapList = getService().searchAttentionQuestion(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response) ;
	}
	//根据登录名查积分数   我的问题
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/doSeatchByUserName")
	public void doSearchByUserName(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		int mapList = getService().sum(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response) ;
	}
	//互动问题  查询表单
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/doSeatch")
	public void search(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		List mapList = getService().searchQuestion(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response) ;
	}
	//积分排行
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/doIntegraNum")
	public void doIntegraNumByUser(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		List mapList = getService().IntegraNum(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response) ;
	}
	//根据选中的questionId查询关注表里 的数据
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/guanzhu")
	public void guanzhu(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		int mapList = getService().guanzhuQuestionId(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList),response) ;
	}
	//查询问题表你所有的数据   互动问题页下的  最新问题
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/doSeatchAll")
	public void seatchAll(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		List mapList = getService().seatchAll(map);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mapList), response);
	}
	//查询问题表你所有的数据   互动问题页下的  最热问题	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/doSearchNum")
	public void searchNum(@RequestParam Map map,HttpServletRequest request,HttpServletResponse response){
		List mapList = getService().seatChAllNum(map); 
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"",mapList), response);
	}
	
	public QuestionService getService(){
		return (QuestionService)getBean("QuestionService");
	}	

}
