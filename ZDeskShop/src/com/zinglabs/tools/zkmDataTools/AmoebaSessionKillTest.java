package com.zinglabs.tools.zkmDataTools;

import java.sql.Connection;
import java.util.*;
public class AmoebaSessionKillTest extends ToolsBase{
	
	public static void main(String[] args) throws Exception{
		String[] source={"com.mysql.jdbc.Driver","jdbc:mysql://22.188.136.178:8066/securityDB?user=zinglabs&password=12&useUnicode=true&characterEncoding=utf8","zinglabs","12"};
		Connection con=getConnection(source[0], source[1], source[2], source[3]);
		String sql="select zif1.`id`, "+
			       "zif1.`text`, "+
			       "zif1.`parentId` _parentId, "+
			       "(select count(1) from `zkmInfoBase` zif2 where zif2.parentId = zif1.id "+
			        "and zif2.recordType = 'f' and zif2.createTime >= TIMESTAMP ( "+
			        "'2013-06-01 14:41:32') and zif2.createTime <= TIMESTAMP ( "+
			        "'2013-06-30 14:41:37')) num "+
					"from `zkmInfoBase` zif1 "+
					"where zif1.`recordType` = 'd' and "+
					      "zif1.`isdel` <> '1' "+
					"order by num desc";
		List list=queryMap(sql,null,con);
		System.out.println(list.size());
	}
}
