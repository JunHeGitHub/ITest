package com.zinglabs.apps.i18nPrompt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSession;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
/**
 * 
 * @author zhao
 * 2014-08-26
 */
public class I18nPromptService extends BaseService {
	
	public static Logger logger = LoggerFactory.getLogger("ZDesk");

	/**
	 * 添加提示信息数据
	 * @param map
	 * @return
	 */
	public boolean save(List<Map<String,String>> map){
		int i=getServiceSqlSession().db_insert("insert",map);		
		if(i>0) return true;
		
		logger.error("提示信息管理：后台保存方法出现错误！map:"+map);
		return false;
	}
	/**
	 * 添加提示语言
	 * @param map
	 * @return
	 */
	public boolean saveLanguge(List<Map<String,String>> ls){
		int i=getServiceSqlSession().db_insert("insert",ls);		
		if(i>0) return true;
		
		logger.error("提示信息管理：后台保存方法出现错误！map:"+ls);
		return false;
	}
	/**
	 * 修改提示信息数据
	 * @param map
	 * @return
	 */
	public boolean update(Map<String, String> map){
		int i=getServiceSqlSession().db_update("update",map);		
		if(i>0) return true;
		
		logger.error("提示信息管理：后台修改方法出现错误！map:"+map);
		return false;
	}
	/**
	 * 删除提示信息数据
	 * @param map
	 * @return
	 */
	public boolean delete(List<String> list){
		int i=getServiceSqlSession().db_delete("delete",list);
		if(i>0) return true;
		
		logger.error("提示信息管理：后台删除方法出现错误！map:"+list);
		return false;
		
	}
	/**
	 * 根据前台筛选项promptKey 和 promptValue进行模糊查询数据
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> selectByPromptKeyAndPromptValue(Map<String, String> map){
		List list=getServiceSqlSession().db_selectList("selectByPromptKeyAndPromptValue",map);
		return list;
	}
	
	/**
	 * 查询提示信息数据
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> selectPromptKeyById(Map<String, String> map){
		List list=getServiceSqlSession().db_selectList("selectById",map);
		return list;
	}
	/**
	 * 根据promptKey查询出ID
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> selectByPromptKey(Map<String, String> map){
		List list=getServiceSqlSession().db_selectList("selectByPromptKey",map);
		return list;
	}
	/**
	 * 根据配置表中的语言代码及项目名称来获取对应的提示信息集合
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> promptList(Map<String, String> map){
		List list=getServiceSqlSession().db_selectList("selectByLangugeCOdeAndProjectType",map);		
		return list;
	}
	
	/**
	 * 根据配置表中的语言代码及项目名称来获取只有提示key值和提示内容的map集合
	 * @param map
	 * @return
	 */
	public Map<String, String> getI18nPromptData(Map<String, String> map,String conversionString){
		List ls=getServiceSqlSession().db_selectList("selectPromptMap",map);
		ls=I18nPromptUtils.ascii2Native(ls,conversionString);
		Map<String, String> prommptMap=new HashMap<String, String>();
		if(ls.size()!=0 && ls!=null){
			for (int i = 0; i < ls.size(); i++) {
				Map m=(Map) ls.get(i);
				prommptMap.put((String)m.get("promptKey"),(String) m.get("promptValue"));
			}			
		}
		return prommptMap;
	}
	
	/**
	 * 查询出提示表中所有的key
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> selectPromptKey(){
		List list=getServiceSqlSession().db_selectList("selectPromptKey");		
		return list;
	}
	/**
	 * 查询出提示表中所有的key
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> selectDefaultValue(Map<String, String> map){
		List list=getServiceSqlSession().db_selectList("selectDefaultValue",map);		
		return list;
	}
	
	/**
	 * 判断promptKey是否存在
	 * @param map
	 * @return
	 */
	public boolean selectExistForPromptKey(Map<String, String> map){
		List list=getServiceSqlSession().db_selectList("selectExistForPromptKey",map);	
		if(list.size()!=0){
			return true;
		}
		return false;
	}
	
	/**
	 * 查询数据表中的语言代码
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> selectLangugeCode(Map<String, String> map){
		List list=getServiceSqlSession().db_selectList("selectLangugeCode",map);	
		return list;
	}
	/**
	 * 查询数据表中的语言代码
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> selectPromptForLangugeCode_zh_CN(Map<String, String> map){
		List list=getServiceSqlSession().db_selectList("selectPromptForLangugeCode_zh_CN",map);	
		return list;
	}
	/**
	 * 判断langugeCode是否存在
	 * @param map
	 * @return
	 */
	public boolean selectExistForLangugeCode(Map<String, String> map){
		List list=getServiceSqlSession().db_selectList("selectExistForLangugeCode",map);	
		if(list.size()!=0){
			return true;
		}
		return false;
	}
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		// TODO Auto-generated method stub
		 return (AppSqlSessionDbid)this.getBean("i18nPromptSqlSession");
	}
	
}
