package com.zinglabs.util.DataRelationComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOBatchUpdate;
import com.zinglabs.db.DAOTools;
import com.zinglabs.servlet.ZKMDocServlet;
import com.zinglabs.util.RandomGUID;



public class DataRelationComponent {
	
	public static String dbid=ZKMDocServlet.zkmDBId;
	
	public static void setRelation(String srcid,String distid,String type,boolean isClear) throws Exception{
		if(srcid!=null && srcid.length()>0 && type!=null && type.length()>0 && distid!=null && distid.length()>0){
			//在执行插入前，进行删除操作，确保数据不会重复
			if(isClear){
				delRelationSrcId(srcid,type);
			}
			//插入
			String sql="insert into DataRelationComponent (`srcId`,`distId`,`rType`) values (?,?,?)";
			String [] param={srcid,distid,type};
			DAOTools.updateForPrepared(sql, param, dbid);
		}
	}
	
	public static void setRelation(String id,String[] ids,String type,boolean isClear) throws Exception{
		if(id!=null && id.length()>0 && type!=null && type.length()>0 && ids!=null && ids.length>0){
			//在执行插入前，进行删除操作，确保数据不会重复
			if(isClear){
				delRelationSrcId(id,type);
			}
			//插入
			String sql="insert into DataRelationComponent (`srcId`,`distId`,`rType`) values (?,?,?)";
			DAOBatchUpdate dbu=new DAOBatchUpdate(dbid);
			List<String[]> params=new ArrayList<String[]>();
			for(String distId:ids){
				String [] param={id,distId,type};
				params.add(param);
			}
			dbu.batchUpdate(sql, params, true, true);
		}
	}
	
	public static void setRelation(String id,List<String> list,String type,boolean isClear)throws Exception{
		if(list!=null && list.size()>0){
			String [] ids =(String[])list.toArray();
			setRelation(id,ids,type,isClear);
		}
	}
	
	public static List<Map> getRelation(String id,String type) throws Exception{
		List<Map> relist=null;
		if(id!=null && id.length()>0 && type!=null && type.length()>0){
			String sql="select * from DataRelationComponent where `srcId`=? and `rType`=?";
			relist=DAOTools.queryMap(sql, new String[]{id,type}, dbid);
		}
		return relist;
	}
	
	public static List<Map> getRelationForDistId(String did,String type) throws Exception{
		List<Map> relist=null;
		if(did!=null && did.length()>0 && type!=null && type.length()>0){
			String sql="select * from DataRelationComponent where `distId`=? and `rType`=?";
			relist=DAOTools.queryMap(sql, new String[]{did,type}, dbid);
		}
		return relist;
	}
	
	public static boolean delRelation(String id,List<String> list,String type)throws Exception{
		if(list!=null && list.size()>0){
			String [] ids =(String[])list.toArray();
			delRelation(id,ids,type);
			return true;
		}
		return false;
	}
	
	public static boolean delRelation(String id,String[] ids,String type) throws Exception{
		if(id!=null && id.length()>0 && type!=null && type.length()>0 && ids!=null && ids.length>0){
			String sql="delete from DataRelationComponent where srcId=? and distId=? and rType=?";
			DAOBatchUpdate dbu=new DAOBatchUpdate(dbid);
			List<String[]> params=new ArrayList<String[]>();
			for(String distId:ids){
				if(distId!=null && distId.length()>0){
					String [] param={id,distId,type};
					params.add(param);
				}
			}
			dbu.batchUpdate(sql, params, true, true);
			return true;
		}
		return false;
	}
	
	public static boolean delRelationSrcId(String id,String type)throws Exception{
		if(id!=null && id.length()>0 && type!=null && type.length()>0){
			String sql="delete from DataRelationComponent where srcId=? and rType=?";
			DAOTools.updateForPrepared(sql, new String[]{id,type},dbid);
			return true;
		}
		return false;
	}
	
	public static boolean delRelationDistId(String id,String type)throws Exception{
		if(id!=null && id.length()>0 && type!=null && type.length()>0){
			String sql="delete from DataRelationComponent where distId=? and rType=?";
			DAOTools.updateForPrepared(sql, new String[]{id,type},dbid);
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) throws Exception{
		DAOTools dts=new DAOTools();
		String dbid=ConfInfo.confMapDBConf.get("securityDBId");
		Thread.sleep(2000);
		System.out.println(dbid);
		DataRelationComponent.dbid=dbid;
		String id=(new RandomGUID()).toString();
		String type="test";
		String [] str=new String[100];
		for(int i=0;i<str.length;i++){
			str[i]=(new RandomGUID()).toString();
		}
		//DataRelationComponent.setRelation(id, str, type);
		id="4B687A4D-F904-8006-1B96-738F503F5C07";
		List list=DataRelationComponent.getRelation(id, type);
		System.out.println(list.size());
	}
}
