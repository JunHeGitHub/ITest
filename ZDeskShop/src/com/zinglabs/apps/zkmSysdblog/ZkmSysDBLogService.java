package com.zinglabs.apps.zkmSysdblog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.db.UserInfo2;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.RandomGUID;
import com.zinglabs.util.WebFormUtil;

public class ZkmSysDBLogService extends BaseService{
	public static Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	// 查询所有日志
	public List<Map> sysDBLogSelect(HashMap sdb) {
		List<Map> sysDBLogPojoList = null;
		if (sdb == null) {
			sysDBLogPojoList = getServiceSqlSession().db_selectList("selectAllSysDBLogPojo");
		} else
			sysDBLogPojoList = getServiceSqlSession().db_selectList("selectAllSysDBLogPojo", sdb);
		return sysDBLogPojoList;
	}

	// 插入一条日志
	public void sysDBLogInsert(HashMap sdb,String longinName) {
		sdb.put("id", new RandomGUID().toString());
		sdb.put("user",longinName);
		try{
			 getServiceSqlSession().db_insert("insertSysDBLogPojo", sdb);
		}
		catch(Exception x){
			LogUtil.error(x, SKIP_Logger);
		}
	}

	/**
	 * 保存操作日志
	 * @param relId　关联数据的ID
	 * @param user　用户
	 * @param content　日志内容
	 * @param disponseType	处理类型
	 */
	public void sysZkmOperationLogSave(String relId,String  loginName,String operateType,String content,String disponseType,HttpServletRequest request){
		HashMap sdb=new HashMap();
		sdb.put("relId",relId);
		sdb.put("logType","zkmOperationLog");
		sdb.put("operateType",operateType);
		sdb.put("content",content);
		sdb.put("disponseType",disponseType);
		WebFormUtil wfu=new WebFormUtil(request);
		sdb.put("companyId", wfu.get("companyId"));
		sdb.put("departmentId", wfu.get("departmentId"));
		sysDBLogInsert(sdb,loginName);
	}

	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		// TODO Auto-generated method stub
		return (AppSqlSessionDbid)this.getBean("zkmSysdblogSqlSession");
	}
}
