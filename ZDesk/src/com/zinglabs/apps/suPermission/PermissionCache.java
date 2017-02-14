package com.zinglabs.apps.suPermission;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PermissionCache {
    private static Map <String,Object>cache =new  ConcurrentHashMap <String,Object>();
     
    public static void put(String key,Object value){
    	cache.put(key, value);
    }  
    public static Object get(String key){
    	return cache.get(key);
    }
    public static Object getAllPermissions(){
    	return cache;
    }
}
