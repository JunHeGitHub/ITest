package com.zinglabs.apps.BOC_gongdan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.apps.BOC_gongdan.util.FieldUtil;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

public class BOCgongdanService extends BaseService{
	public static Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk");
	
	public List selectMaxId(Map map){
		return getServiceSqlSession().db_selectList("selectMaxId",map);
	}
	public void act_insert(Map map){
		getServiceSqlSession().db_insert("BOCinsertgongdan" , map) ;
	}
	public void insertHis(Map map){
		getServiceSqlSession().db_insert("BOCinsertgongdanHis" , map) ;
	}
	public Map getflowid(Map map){
		List<Map> list = getServiceSqlSession().db_selectList("selectgongdan" , map) ;
		try{
			if(list.size()>0){
				return list.get(0) ;
			}else{
				return null ;
			}
		}
		catch(Exception e){
			SKIP_Logger.error("BOC_gongdanService-getflowid:",e) ;
			e.printStackTrace() ;
			return null ;
		}
	}
	
	public void updateForMQ(Map map){
		getServiceSqlSession().db_update("updateForMQ",map) ;
	}
	
	public boolean guanlian(Map map){
		if(map.get("gongdanId")!=null&&!"".equals(map.get("gongdanId").toString())&&map.get("guanlianId")!=null&&!"".equals(map.get("guanlianId").toString())){
			getServiceSqlSession().db_update("deleteguanlian",map) ; 
			List list = getServiceSqlSession().db_selectList("selectgongdanid", map) ;
			if(list.size()!=0){
				getServiceSqlSession().db_update("updateguanlianId", map) ;
				Map update= new HashMap() ;
				update.put("gongdanId", map.get("guanlianId")) ;
				update.put("guanlianId", map.get("gongdanId")) ;
				getServiceSqlSession().db_update("updateguanlianId", update) ;
				return true ;
			}else{
				return false ;
			}
		}
		return false ;
	}
	
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		return (AppSqlSessionDbid)this.getBean("BOCgongdan_SqlSession");
	}
}
