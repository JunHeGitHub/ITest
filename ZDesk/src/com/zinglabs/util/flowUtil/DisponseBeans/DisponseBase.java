package com.zinglabs.util.flowUtil.DisponseBeans;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;
import com.zinglabs.servlet.ZKMDocServlet;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.WebFormUtil;

public class DisponseBase {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	
	public static String zkmDBId=ZKMDocServlet.zkmDBId;
	public static void postStrToClient(String json, HttpServletResponse response) {
		try {
			PrintWriter pw = response.getWriter();
			pw.write(json);
			pw.flush();
			pw.close();
		} catch (Exception x) {
			LogUtil.error(x, SKIP_Logger);
		}
	}
	
	public static Map commonSearch(String[] terms,String fields,String table,String otherWhere,HttpServletRequest request){
		WebFormUtil wfu=new WebFormUtil(request);
		List plist=new ArrayList();
		String sql="select " + fields + " from " +table + " where 1=1 ";
		String sqlWhere=" ";
		for(String field:terms){
			String []f=field.split(":");
			String f0=f[0];
			String f1=f[1];
			String v=wfu.get(f0);
			if(v.length()>0){
				if("=".equals(f1)){
					sqlWhere+=" and " + f0 + "=?";
					plist.add(v);
				}else if("like".equals(f1)){
					v=v.trim().replaceAll("\\*", "%");
					plist.add(v);
					sqlWhere+=" and " + f0 + " like ?";
				}
			}
		}
		sql+=sqlWhere + otherWhere;
		if(wfu.get("orderField").length()>0 && wfu.get("orderType").length()>0){
			sql+=" order by " + wfu.get("orderField") + " " + wfu.get("orderType");
		}
		try{
			List list=new ArrayList();
			if(plist.size()>0){
				list=DAOTools.queryMap(sql, plist, zkmDBId);
			}else{
				list=DAOTools.queryMap(sql, zkmDBId);
			}
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", list);
			return rmap;
		}catch(Exception x){
			LogUtil.error("commonSearch ---- error " ,SKIP_Logger);
			LogUtil.error(x, SKIP_Logger);
			return JsonUtils.genUpdateDataReturnMap(false, "获取待处理数据异常。",null);
		}
	}
}
