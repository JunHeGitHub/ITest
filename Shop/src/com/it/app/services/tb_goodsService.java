package com.it.app.services;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.it.util.JsonUtils;

@Service("tb_goodsService")
public class tb_goodsService extends SqlSessionDaoSupport {
	
	//注入dao
   // @Resource(name="sqlSession")
   // private SqlSession sqlSession;
	
	
	
    private String nameSpace = "tb_goods";
    
    /**
     *  商品添加
     * @param params
     * @param companyId
     */
    
    public boolean insertGood(Map<String,String> params){
    	boolean result = false;
    	System.out.println("Params"+params);
    	try {
    		//插入数据并返回 主键Id
    		int id = this.getSqlSession().insert(nameSpace+".addGoods",params);
    		
    		List<Map<String,String>> listJson = (List<Map<String,String>>) JsonUtils.stringToJSON(params.get("data"),List.class);  
 			for(int i = 0; i < listJson.size();i++){
				Map<String,String> itemMap = listJson.get(i);
				
				itemMap.put("goodId",String.valueOf(id));
				itemMap.put("reCount",itemMap.get("goodsCount"));
				
				this.getSqlSession().insert(nameSpace+".addProp",itemMap);
 			}
    		
 			result = true;
		} catch (Exception e) {
			System.out.println(e);
			System.out.println(e.getMessage());
		}
    	return result;
    }
    
    
    /*
    public void addGood(Map params,String companyId) throws Exception{
    	ArrayList<String> alExec=new ArrayList<String>();
    	
    	// 商品属�?�表插入数据
    	String[] goodsProp = String.valueOf(params.get("goodInfo")).split(",");
    	
    	for(int i = 0; i<goodsProp.length; i++){
    		
    		String	sql = "insert into `tb_goodProp` (`goodId`,`goodsProp`,	`goodsPrice`,`goodsCount`,`reCount`,`createTime`)" +
    			  " values("+params.get("dataId") +
    			  		   ", '" +goodsProp[i].split("-")[0]+
    			  		   "','" +goodsProp[i].split("-")[1]+
    			  		   "','"+goodsProp[i].split("-")[2]+"'," +
    			  		   " '"+goodsProp[i].split("-")[2]+"',NOW())";
    		alExec.add(sql);
    	}
    	
    	DAOTools.execBatchS(alExec, DBidUtils.getCompanyDbid(companyId));
    	
    }
    
    
    
    
    
    
    /**
     *  商品添加
     * @param params
     * @param companyId
     */
    
//    public void updateGood(Map params,String companyId){
//		ArrayList<String> alExec=new ArrayList<String>();
//    	
//		
//		//先清空原信息，然后添加�??
//		
//		String delSql =" delete from `tb_goodProp` where goodId = "+params.get("dataId");
//		alExec.add(delSql);
//		DAOTools.execBatchS(alExec, DBidUtils.getCompanyDbid(companyId));
//		alExec.clear();
//		
//    	// 商品属�?�表插入数据
//    	String[] goodsProp = String.valueOf(params.get("goodInfo")).split(",");
//    	
//    	for(int i = 0; i<goodsProp.length; i++){
//    		
//    		String	sql = "insert into `tb_goodProp` (`goodId`,`goodsProp`,	`goodsPrice`,`goodsCount`,`reCount`,`createTime`)" +
//    			  " values("+params.get("dataId") +
//    			  		   ", '" +goodsProp[i].split("-")[0]+
//    			  		   "','" +goodsProp[i].split("-")[1]+
//    			  		   "','"+goodsProp[i].split("-")[2]+"'," +
//    			  		   " '"+goodsProp[i].split("-")[2]+"',NOW())";
//    		alExec.add(sql);
//    	}
//    	
//    	DAOTools.execBatchS(alExec, DBidUtils.getCompanyDbid(companyId));
//    	
//    	//this.baseDao.db_update(nameSpace+".updateGood", params, DBidUtils.getCompanyDbid(companyId));
//    	
//    }
    
    
    /**
     *  删除 common_order 中的数据�? tb_goodsProp 中的数据�?
     * @param dataId
     * @param companyId
     */
//    public void delGood(Map params,String companyId){
//    	ArrayList<String> alExec=new ArrayList<String>();
//		
//    	// 真实删除数据
//		String delSql =" delete from `common_order` where dataId = "+params.get("dataId");
//		alExec.add(delSql);
//		delSql =" delete from `tb_goodProp` where goodId = "+params.get("dataId");
//		alExec.add(delSql);
//		DAOTools.execBatchS(alExec, DBidUtils.getCompanyDbid(companyId));
//    	
//    	/*this.baseDao.db_delete(nameSpace+".delGood", params, DBidUtils.getCompanyDbid(companyId));*/
//    	
//    }
    
    
    /**
     * 
     * 获取�?选商品属�?
     * 
     */
//    public List getAllGoodProp(Map params,String companyId){
//    	
//    	List<Map> list = this.baseDao.db_selectList(nameSpace+".getAllGoodProp", params,DBidUtils.getCompanyDbid(companyId));
//    	
//    	return list;
//    	
//    }
    
    
    
    
    /**
     * 平台默认给出的分�?
     * @param companyId
     * @return
     */
//    public List getAllGoodType(String companyId){
//    	
//    	List list = this.baseDao.db_selectList(nameSpace+".getAllGoodType", null, DBidUtils.getCompanyDbid(companyId));
//    	
//    	return list;
//    }
    
    
    /**
     *  已�?�商品属性集�?
     * @param companyId
     * @return
     */
//    public List getAllGoodPropByIdStr(List list,String type,String companyId){
//    	
//    	List<Map> propList = this.baseDao.sqlIdInQuery(null, list, type, "tb_goods.getAllGoodPropByIdStr", DBidUtils.getCompanyDbid(companyId));
//    	
//    	return propList;
//    }
    
    
    
    
    
    /**
     * 验证数量，加入购物车时验证， 提交订单时验�?
     * 
     */
//    public boolean validateGoodsCount(Map params ,String companyId){
//    	
//    	List<Map> list = this.baseDao.db_selectList(nameSpace+".validateCount", params, DBidUtils.getCompanyDbid(companyId));
//    	
//    	logger.debug("-----------+list"+list);
//    	
//    	//避免没有数据报错�?
//    	if(list.size() > 0){
//	    	//如果购买数量大于 库存数量，返回true
//    		logger.debug("------buy count------"+Integer.parseInt((String)params.get("goodCount")));
//    		logger.debug("------exit count------"+Integer.parseInt((String)list.get(0).get("reCount")));
//    		//如果购买数量 小于等于 库存 ，返�? true
//	    	if(Integer.parseInt((String)params.get("goodCount")) <=	Integer.parseInt((String)list.get(0).get("reCount"))){
//	    		
//	    		return true;
//	    	}	
//    	}
//    	
//    	return false;
//    	
//    }
    
    
    
    /**
     * 库存数量增减
     */
//    public void updatePropCount(Map map,String companyId){
//    	
//    	this.baseDao.db_update(nameSpace+".updatePropCount", map, DBidUtils.getCompanyDbid(companyId));
//    	
//    }
    
    
    
    
    
    
    
    /**
     * 查询方法---用于电商系统
     */
    
    public List<Map> getGoodData(Map map,String companyId){
    	
    	
    	return null;
    }


}
