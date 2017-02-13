package com.zinglabs.apps.activitiCommon;

import java.util.HashMap;
import java.util.Map;

import com.zinglabs.apps.BOC_gongdan.MQgongdan;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;

@SuppressWarnings("unchecked")
public class ActivitiCommonService extends BaseService{
	
	/**
	 * 
	 * @return    返回map 
	 * 
	 *    map.state =0 成功    =1 失败
     *    map.mgs : 提交结果。
	 * 
	 * */
	public Map docomplete(Map MQmap){		
		MQgongdan gongdan = new MQgongdan() ;
		Map map = gongdan.docomplete(MQmap) ;
		return map;
	}

	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		// TODO Auto-generated method stub
		return null;
	}
}
