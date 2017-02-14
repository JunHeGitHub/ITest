package com.zinglabs.tools.zkmDataTools;
import java.util.*;
import java.sql.*;
import java.io.*;

public class ZKMDocTools extends ToolsBase{
	
	public static List getFindNoContentRows(Connection con) throws Exception{
		List list=new ArrayList();
		String sql="select * from zkmInfoBaseDisponse where zkmDocState<>'停用' order by zkmDocState";
		if(con!=null){
			List rlist=queryMap(sql, null, con);
			if(rlist!=null && rlist.size()>0){
				for(int i=0;i<rlist.size();i++){
					Map map=(Map)rlist.get(i);
					String contentPath=(String)map.get("contentPath");
					File file=new File(contentPath);
					if(!file.exists()){
						list.add(map);
					}
				}
			}
		}
		return list;
	}
}
