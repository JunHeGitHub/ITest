package com.zinglabs.tools.zkmFileImport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zinglabs.db.DAOTools;

public class ZkmDataVaildate {
	
	public static HashMap<String,String> nodes;
	public static HashMap<String,String> files;
	public static List<HashMap<String,String>> flist;
	public static String dbid="ZDesk_old";
	public static HashMap<String,String> matchDBTitle=new HashMap<String,String>();
	public static List<String> matchDBList=new ArrayList<String>();
	
	
	public static List datas=new ArrayList();
	
	static{
		String sql="select * from zkmInfoBase where recordType='d'";
		try{
			List list=DAOTools.queryMap(sql, dbid);
			sql="select * from zkmInfoBase where recordType='f'";
			flist=DAOTools.queryMap(sql, dbid);
			
		}catch(Exception x){
			
		}
	}
	
	
	public static void main(String [] args) throws Exception{
		/*ZkmFileImport zfi=new ZkmFileImport();
		zfi.getChildFolderFiles(new File("E:/testImp/GHexcel/"));
		
		if(zfi.xlslist!=null ){
			for(File f:zfi.xlslist){
				List<List> list=zfi.xlsToList(f);
				for(List<String> xl:list){
					String title=xl.get(0);
					//System.out.println(title);
					boolean has=true;
					for(HashMap<String,String> dm:flist){
						String dbt=dm.get("title");
						if(dbt.equals(title)){
							has=false;
							break;
						}
					}
					if(has){
						matchDBList.add(title);
					}
				}
			}
		}
		System.out.println(matchDBList.size());
		for(String tt:matchDBList){
			
		}*/
		String dbid="ZDesk";
		String sql="select * from zkmSmsSet where content like '%ZING%'";
		List<HashMap<String,String>> list=DAOTools.queryMap(sql, dbid);
		System.out.println("------------ : " + list.size());
		if(list!=null && list.size()>0){
			int x=0;
			int y=0;
			for(HashMap<String,String> d:list){
				System.out.println(x++);
				String cont=d.get("content");
				System.out.println(cont);
				cont=cont.replaceAll("ZING_nnn;","");
				cont=cont.replaceAll("ZING_quot;", "");
				String id=d.get("id");
				System.out.println(cont);
				sql="update zkmSmsSet set content=? where id=?";
				if(id!=null && !"".equals(id)){
					String [] p={cont,id};
					DAOTools.updateForPrepared(sql, p, "ZDesk");
					y++;
				}
			}
			System.out.println(x + " " + y);
		}
		
	}
	
}
