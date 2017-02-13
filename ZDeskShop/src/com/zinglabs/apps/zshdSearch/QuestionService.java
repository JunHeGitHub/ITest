package com.zinglabs.apps.zshdSearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class QuestionService extends BaseService{

	
	//高级查询条件查询
	public List search(Map<String, String> map){
		return getServiceSqlSession().db_selectList("searchQuestion",map);
	}

	//积分流水查询
	public List searchIntegral(Map map){
		return getServiceSqlSession().db_selectList("integralQuery",map);
	}
	//我的问题查询
	public List seletMyQuestion(Map map){
		return getServiceSqlSession().db_selectList("MyQuestion",map);
	}
	   //我关注的问题查询
	public List searchAttentionQuestion(Map map){
		return getServiceSqlSession().db_selectList("attentionQuestion",map);
	}
	//积分加一
	public int sum(Map map){
        Integer ret=getServiceSqlSession().db_selectOne("doSeatchByUserName", map);
        return ret;
		
	}
	//互动问题  查询表单
	public List searchQuestion(Map map){
		return getServiceSqlSession().db_selectList("search",map);
	}
	
	//积分排行
	public List IntegraNum(Map map){
		return getServiceSqlSession().db_selectList("doIntegraNum", map);
	}
	//根据选中的questionId查询关注表里 的数据
	public int guanzhuQuestionId(Map map){  //编译出错临时调整
		Integer count =  getServiceSqlSession().db_selectOne("guanzhu",map);
		return count;
	}
	//查询问题表你所有的数据   互动问题页下的  最新问题
	public List seatchAll(Map map){
		return getServiceSqlSession().db_selectList("doSeatchAll",map);
	}
	//查询问题表你所有的数据   互动问题页下的  最热问题
	public List seatChAllNum(Map map){
		return getServiceSqlSession().db_selectList("doSearchNum",map);
	}
	
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		// TODO Auto-generated method stub
		return (AppSqlSessionDbid)this.getBean("questionSqlSession");
	}

	
	
	
}
