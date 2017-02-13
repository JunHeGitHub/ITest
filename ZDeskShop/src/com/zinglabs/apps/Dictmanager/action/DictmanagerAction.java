package com.zinglabs.apps.Dictmanager.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.Dictmanager.DictmanagerService;
import com.zinglabs.apps.Dictmanager.DictmanagerUtils ;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;
@Controller
@RequestMapping(value="/*/Dicmaintenance")
public class DictmanagerAction extends BaseAction{
	
	@RequestMapping(value="/add")
	public void addForDicData(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		Map appmap = new HashMap() ;
		appmap.put("code", map.get("code")) ;
		List listquery = getService().searchDicData(appmap) ;
		if(listquery.size()>0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"Data duplication"),response) ;
			return ;
		}
		if(map.size()>0){
			getService().insertDicData(map) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,""),response) ;
			
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,""),response) ;
		}
	}
	@RequestMapping(value="/delete")
	public void delete(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){		
		String ids = (String)map.get("id") ;
		String[] idsArr=ids.split(",");
		List <String> list =new ArrayList<String>();
		for(int i=0;i<idsArr.length;i++){
			list.add(idsArr[i]);
		}
		getService().deleteDicData(list) ;
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"删除成功"),response) ;
	}
	@RequestMapping(value="/update")
	public void updataDic(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		Map appmap = new HashMap() ;
		appmap.put("code", map.get("code")) ;
		appmap.put("id", map.get("id")) ;
		List listquery = getService().selectDataforUpdate(appmap) ;
		if(listquery.size()>0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"Data duplication"),response) ;
			return ;
		}
		getService().updateDicData(map) ;
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,""),response) ;
	}
	@RequestMapping(value="/select")
	public void select(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		List list = getService().searchDicData(map) ;
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"查询成功",list),response) ;
	}
	@RequestMapping(value="/reloadDicData")
	public void reloadDicData(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		DictmanagerUtils.reloadDicData();
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "加载成功",DictmanagerUtils.DictMap),response) ;	
	}
	@RequestMapping(value="/onloadDicData")
	public void getDicData(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		Map<String,List> data = DictmanagerUtils.getDicData() ;
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "加载成功",data),response) ;	
	}
	
	@RequestMapping(value="/addDicIndex")
	public void addDicIndex(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		Map appmap = new HashMap() ;
		appmap.put("indexCode", map.get("indexCode")) ;
		List listquery = getService().selectIndexData(appmap) ;
		if(listquery.size()>0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"该字典码已存在"),response) ;
			return ;
		}
		if(map.size()>0){
			getService().insertDicIndex(map) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"ok"),response) ;
			
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"error"),response) ;
		}
	}
	@RequestMapping(value="/deleteDicIndex")
	public void deleteDicIndex(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		String ids = (String)map.get("id") ;
		String[] idsArr=ids.split(",");
		List <String> list =new ArrayList<String>();
		for(int i=0;i<idsArr.length;i++){
			list.add(idsArr[i]);
		}
		if(list.size()>0){
			getService().deleteDicIndex(list) ;
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"删除成功"),response) ;
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"缺少数据"),response) ;
		}
	}
	@RequestMapping(value="/updateDicIndex")
	public void updateDicIndex(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		Map appmap = new HashMap() ;
		appmap.put("indexCode", map.get("indexCode")) ;
		appmap.put("id", map.get("id")) ;
		List listquery = getService().selectIndexforUpdate(appmap) ;
		if(listquery.size()>0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"该字典码已存在"),response) ;
			return ;
		}
		getService().updateDicIndex(map) ;
		getService().updateDicDataByIndex(map) ;
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"ok"),response) ;
	}
	@RequestMapping(value="/selectDicIndex")
	public void selectDicIndex(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		List list = getService().selectIndexData(map) ;
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"查询成功",list),response) ;
	}
	@RequestMapping(value="/onloadDicIndex")
	public void onloadDicIndex(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		List list = DictmanagerUtils.getDicIndex() ;
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"查询成功",list),response) ;
	}
	@RequestMapping(value="/reloadDicIndex")
	public void reloadDicIndex(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
		DictmanagerUtils.reloadDicIndex() ;
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"查询成功",DictmanagerUtils.DictIndex),response) ;
	}
	public DictmanagerService getService() {
		return (DictmanagerService ) getBean("DictmanagerService");
	}
}
