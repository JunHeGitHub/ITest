package com.zinglabs.base.Interface;

import java.util.Map;

@SuppressWarnings("unchecked")
public interface ActivitiFromInterface {
	public boolean validate() ;
	
	//存储表单接口  str 流程定义  map表单值
	public Map<String,Map>  execute(Map<String,Map> map) ;
	
}
