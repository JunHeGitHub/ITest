package com.zinglabs.apps.chatWorkflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.zinglabs.log.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.io.MapSerializer;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import com.sun.org.apache.bcel.internal.generic.NEWARRAY;
import com.sybase.jdbc3.tds.a;
import com.zinglabs.apps.suPermission.service.UserInfoService;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.db.DAOTools;
import com.zinglabs.servlet.WXServlet;
import com.zinglabs.util.CookieUtils;

public class WorkflowService extends BaseService {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");

    public static final String MHDBID="MHZDesk";

    public static final String DBIDRMHTmp="ZDesk";
	
	/**
	 * 添加微信门户用户与ZDesk用户绑定信息
	 */
	public boolean save(Map<String,String> map){
		int i=getServiceSqlSession().db_insert("insert",map);		
		if(i>0) return true;
		
		logger.error("微信门户与ZDesk用户绑定：后台保存方法出现错误！map:"+map);
		return false;
	}

    public void delBindUser(String zdeskUserLoginName){
        HashMap mapTmp=new  HashMap();
        mapTmp.put("zdeskUserLoginName", zdeskUserLoginName);
        getServiceSqlSession().db_delete("delBindUser",mapTmp);
    }

	/**
	 * 根据登录名判断该用户是否存在
	 */
	public boolean checkLoginName(String loginName){
		UserInfoService uis =new UserInfoService();
		Map<String,String> map=uis.getUserInfoByLoginName(loginName);
		if(map!=null && map.size()!=0){
			return true;			
		}
		return false;
	}
	/**
	 * 根据登录名获取用户信息
	 */
	public Map<String,String> getUserInfoByLoginName(String loginName) {
		UserInfoService uis =new UserInfoService();
		return uis.getUserInfoByLoginName(loginName);
		
	}
	/**
	 * 根据用户账号判断是否已绑定  返回true 或false
	 */
	public boolean checkBindingByLoginName(String loginName){
		Map params=new HashMap();
		params.put("loginName", loginName);
		
		Map<String, String> map = getServiceSqlSession().db_selectOne("selectByLoginName", params);
		if(map!=null && map.size()!=0){			
			return true;
		}
		return false;
	}
	/**
	 * 根据用户账号判断是否已绑定
	 */
	public List getBindingByCardUrlForReList(String cardUrl){
		Map params=new HashMap();
		params.put("cardUrl", cardUrl);
		
		List ls= getServiceSqlSession().db_selectList("selectByCardUrl", params);
		
		return ls;
	}
	/**
	 * 删除历史的绑定信息
	 * @param map
	 * @return
	 */
	public int deleteBindingById(Map map){
		return getServiceSqlSession().db_delete("deleteBindingById",map);
	}
	
	/**
	 * 获取所有绑定的用户数据
	 */
	public List getBindingData(){
		List list = getServiceSqlSession().db_selectList("getBindingData");
		return list;
	}
	/**
	 * 根据cardUrl获取与ZDesk用户登录账号
	 */
	public List getZDeskLoginNameBycardUrl(String cardUrl){
		Map params=new HashMap();
		params.put("cardUrl", cardUrl);
		List list = getServiceSqlSession().db_selectList("selectZDeskLoginNameBycardUrl",params);
		return list;
	}
	/**
	 * 根据ZDesk用户登录账号获取所属组织id
	 */
	public List getOrgByLoginName(String loginName){
		Map params=new HashMap();
		params.put("loginName", loginName);
		List list = getServiceSqlSession().db_selectList("selectgetOrgByLoginName",params);
		return list;
	}
	
	/**
	 * 获取所用组织机构数据
	 * @return
	 */
	public List getAllOrg(){
		Map map=new HashMap();
		map.put("recordType", "org");
		List orglist = getServiceSqlSession().db_selectList("getAllOrg", map);
		return orglist;
	}
	//获取组织对应用户的信息
	public Map getAllOrg2UserInfo(List users){
		//users=userFilter(users);
		Map <String,List> orgTreeTemp=new HashMap<String, List>();
		List<Map<String, String>> list=getBindingData();
		for(int i=0;i<users.size();i++){
			Map user =(Map) users.get(i);
			int tmp=0;
			String cardUrl="";
			String gender="";
            String RealName="";
			for (int u = 0; u < list.size(); u++) {
				if(user.get("id").toString().equals(list.get(u).get("zdeskUserLoginName").toString())){
					cardUrl=list.get(u).get("cardUrl").toString();
					gender=list.get(u).get("gender").toString();
                    RealName=list.get(u).get("RealName").toString();
					tmp+=1;					
				}
			}
			if(tmp==0){
				continue;
			}
			user.put("cardUrl", cardUrl);
			user.put("gender", gender);
            user.put("RealName", RealName);
			String userStr=(String)user.get("org");
		    if(userStr!=null&&userStr.length()>0){
		    	String usersArr[]=userStr.split(",");    	
		    	for(int j=0;j<usersArr.length;j++){
		    	    String orgId=usersArr[j];
		    	    List org2userList=  orgTreeTemp.get(orgId)==null?new ArrayList():orgTreeTemp.get(orgId);
		    		Map newMap=new HashMap();
			    	newMap.putAll(user);
			    	newMap.put("org", orgId);
			    	org2userList.add(newMap);
		    		orgTreeTemp.put(orgId, org2userList);
		    	}
		    }
		}
		// 返回数据格式 HASHmap  {组织id:[{用户数据}，用户2，用户3]}
		return orgTreeTemp;
	}
	/**
	 * 获取门户组织机构 用户数据  
	 */
	public Map getMhZuZhiJiGouData(){
		Map map=new HashMap();
		map.put("recordType", "org");
		List orgs= getAllOrg();       

		
		List users=getServiceSqlSession().db_selectList("getAllUser");
		
		Map org2user=getAllOrg2UserInfo(users);
		
		Map resultMap=new HashMap();
		for(int i=0;i<orgs.size();i++){
			Map org=(Map) orgs.get(i);
			String id=String.valueOf(org.get("id"));
			String name=String.valueOf(org.get("text"));
			Map orginfo=new HashMap();
			orginfo.put("name", name);
			List user=(List) org2user.get(id);
			if(user!=null){
				orginfo.put("users", user);
			}
			resultMap.put(id, orginfo);
		}
		return resultMap;
		
		
	}
	/**-----------------------------------------------筛选组织机构数据 BEGIN-------------------------------------------------*/
	//获取组织对应用户的信息 门户工作流中所用

    /***
     *
     * @param users
     * @param id
     * @param type
     * @return
     * 20150628调整： 参与者不需要限定范围，只要是非客户访客身份即可。
     * type为template id为null时不做范围过滤
     *
     * TODO 组织机构 以spring结构来处理，不灵活。目前以nodeid 过滤数据，如果后续增加其它过滤参数，每一次参数变化
     * TODO 都需要额外sql 写到xml配置中。后续增加公司等属性，容易混乱。即使不混乱维护也会变复杂。逻辑不够简单。
     *
     */
	public Map getAllOrg2UserInfoForWorkflow(List users,String id,String type){
		//users=userFilter(users);
		Map <String,List> orgTreeTemp=new HashMap<String, List>();
		List<Map<String, String>> list=getBindingData();
		List<Map<String, String>> list2 = new ArrayList<Map<String,String>>();
		if(type.equals("node")){
			list2 = getZuZhiByNodeId(id);
		}
		if(type.equals("template") && id!=null && id.length()>0){ //20150628调整
			list2 = getZuZhiFanWeiByTemplateId(id);
		}
		for(int i=0;i<users.size();i++){
			Map user =(Map) users.get(i);			
			int tmp=0;
			int tmp2=0;
			String cardUrl="";
			String gender="";
            String RealName="";
			for (int u = 0; u < list.size(); u++) {
				if(user.get("id").toString().equals(list.get(u).get("zdeskUserLoginName").toString())){
					cardUrl=list.get(u).get("cardUrl").toString();
					gender=list.get(u).get("gender").toString();
                    RealName=list.get(u).get("RealName").toString();
					tmp+=1;					
				}
			}
			if(tmp==0){
				continue;
			}
			user.put("cardUrl", cardUrl);
			user.put("gender", gender);
            user.put("RealName", RealName);
			String userStr=(String)user.get("org");
		    if(userStr!=null&&userStr.length()>0){
		    	String usersArr[]=userStr.split(",");    	
		    	for(int j=0;j<usersArr.length;j++){
		    		String orgId=usersArr[j];
		    		//只加载指定的组织机构范围内的数据
		    		for (int k = 0; list2!=null && k < list2.size(); k++) {
		    			String orgTemp = list2.get(k).get("stepScope");
		    			if(orgId.equals(orgTemp)){	
		    				System.err.println("dd");
		    				tmp2++;
						}
					}
					if(tmp2==0 && (!type.equals("template") || (id!=null && id.length()>0))){ //20150628调整
						continue;
					}
		    	    List org2userList=  orgTreeTemp.get(orgId)==null?new ArrayList():orgTreeTemp.get(orgId);
		    		Map newMap=new HashMap();
			    	newMap.putAll(user);
			    	newMap.put("org", orgId);
			    	org2userList.add(newMap);
		    		orgTreeTemp.put(orgId, org2userList);
		    	}
		    }
		}
		// 返回数据格式 HASHmap  {组织id:[{用户数据}，用户2，用户3]}
		return orgTreeTemp;
	}
	
	
	/**-----------------------------------------------筛选组织机构数据 END---------------------------------------------------*/
	
	/**-----------------------------------------------创建工作流 BEGIN------------------------------------------------------*/
	/**
	 * 根据节点id获取该节点所设置的组织机构的范围数据
	 */
	public List getZuZhiByNodeId(String nodeid){
		Map params=new HashMap();
		params.put("nodeid", nodeid);
		params.put("scopeType", "zzjg");
		List list = getServiceSqlSession().db_selectList("getUserDataByNodeId",params);
		
		return list;
	}
	/**
	 * 根据模板id 获取该模板下所有节点设置的组织机构范围数据
	 */
	public List getZuZhiFanWeiByTemplateId(String templateid){
		Map params=new HashMap();
		params.put("templateid", templateid);
		params.put("scopeType", "zzjg");
		List list = getServiceSqlSession().db_selectList("getAllFanWeiByTemplateId",params);
		
		return list;
	}
	/**
	 * 根据节点id获取所设置的范围组织机构用户数据
     *
     * 20150628调整： 参与者不需要限定范围，只要是非客户访客身份即可。
     * type为template id为null时不做范围过滤
     *
     * TODO 组织机构 以spring结构来处理，不灵活。目前以nodeid 过滤数据，如果后续增加其它过滤参数，每一次参数变化
     * TODO 都需要额外sql 写到xml配置中。后续增加公司等属性，容易混乱。即使不混乱维护也会变复杂。逻辑不够简单。
     *
	 */
	public Map getZuZhiUserDataByNodeIdOrTemplateId(String nodeid,boolean isAll,String templateid){		
		Map map=new HashMap();
		map.put("recordType", "org");
		List orgs= getAllOrg();       

		
		List users=getServiceSqlSession().db_selectList("getAllUser");
		
		Map org2user=new HashMap<String, String>();
		List<Map<String, String>> ls=new ArrayList<Map<String,String>>();
		if(isAll){
			org2user=getAllOrg2UserInfoForWorkflow(users,templateid,"template");
            if(templateid!=null && templateid.length()>0){
                ls =getZuZhiFanWeiByTemplateId(templateid);
            }
		}else{
			org2user=getAllOrg2UserInfoForWorkflow(users,nodeid,"node");
			ls =getZuZhiByNodeId(nodeid);
		}
		Map resultMap=new HashMap();
		for(int i=0;i<orgs.size();i++){
			int temp=0;
			Map org=(Map) orgs.get(i);
			String id=String.valueOf(org.get("id"));
			String name=String.valueOf(org.get("text"));
			if(ls!=null && ls.size()!=0){
				for (int j = 0; j < ls.size(); j++) {
					
					if(id.equals(ls.get(j).get("stepScope"))){
						temp++;						
					}
				}				
			}
			if(temp==0 && (!isAll ||(templateid!=null && templateid.length()>0))){
				continue;
			}
			Map orginfo=new HashMap();
			orginfo.put("name", name);
			List user=(List) org2user.get(id);
			if(user!=null){
				orginfo.put("users", user);
			}
			resultMap.put(id, orginfo);
		}
		return resultMap;
	}
	
	/**
	 * 根据节点id获取该节点所设置的角色的范围数据
	 */
	public List getJueSeByNodeId(String nodeid){
		Map params=new HashMap();
		params.put("nodeid", nodeid);
		params.put("scopeType", "js");
		List list = getServiceSqlSession().db_selectList("getUserDataByNodeId",params);
		
		return list;
	}
	/**
	 * 根据模板id 获取该模板下所有节点设置的角色范围数据
	 */
	public List getJueSeFanWeiByTemplateId(String templateid){
		Map params=new HashMap();
		params.put("templateid", templateid);
		params.put("scopeType", "js");
		List list = getServiceSqlSession().db_selectList("getAllFanWeiByTemplateId",params);
		
		return list;
	}	
	
	/**
	 * 根据节点id获取所设置的角色范用户数据
     *
     * 20150628调整： 参与者不需要限定范围，只要是非客户访客身份即可。
     * templateid过滤，templateid为null时 不做节点范围过滤
     * 不要必需绑定用户
     *
	 */
	public List getJueSeUserDataByNodeIdOrTemplateId(String nodeid,boolean isAll,String templateid){
		//获取门户所有角色
		List<Map<String, String>> rd=getMHRoleData();
		//获取绑定的数据
		List<Map<String, String>> bd=getBindingData();
		//获取门户用户的数据
		List<Map<String, String>> md=getMHCardUserData();
		//获取所设定的角色范围数据
		List<Map<String, String>> srd =new ArrayList<Map<String,String>>();
		if(isAll){
            if(templateid!=null && templateid.length()>0){ //20150628调整
                srd =getJueSeFanWeiByTemplateId(templateid);
            }
		}else{
			srd =getJueSeByNodeId(nodeid);
		}
		//筛选交集数据 以绑定数据为基准
		List<Map<String, String>> retList=new ArrayList<Map<String,String>>();
		if(bd!=null && md!=null){
			//外层循环 当前的角色数
			for (int k = 0; k < rd.size(); k++) {
				String roleType=rd.get(k).get("roleCode").toString();
				int t=0;
				for (int u = 0; u < srd.size(); u++) {
					if(roleType.equals(srd.get(u).get("stepScope"))){
						t++;
					}
				}
				if(t==0 && (!isAll || (templateid!=null && templateid.length()>0))){
					continue;
				}
				//过滤角色 保留 q客服座席 q客服专家 雇员
				List<Map<String, String>> m1=new ArrayList<Map<String,String>>();

				if(roleType.equals("agent") || roleType.equals("expert") || roleType.equals("employee")){
//					for (int i = 0; i < bd.size(); i++) {
						for (int j = 0; j < md.size(); j++) {
                            //20150628调整
//							String bdCardUrl=bd.get(i).get("cardUrl");
                            String mdCardUrl=md.get(j).get("cardUrl");
//							if(bdCardUrl.equals(mdCardUrl)){

                            if(roleType.equals(md.get(j).get("roleCode"))){
                                Map<String, String> temp =new HashMap<String, String>();
                                temp.put("id", mdCardUrl);
                                temp.put("text", md.get(j).get("RealName"));
                                m1.add(temp);
                            }

//							}
                        }
//					}
				}
				Map m2=new HashMap<String, String>();
				//q客服座席
				if(roleType.equals("agent")){
					m2.put("name", "q客服座席");
					m2.put("users", m1);
					retList.add(m2);
				}
				//q客服专家
				if(roleType.equals("expert")){
					m2.put("name", "q客服专家");
					m2.put("users", m1);
					retList.add(m2);
				}
				//雇员 非客户 非座席 非专家 （未来可能包括班长 管理员 之类的）
				if(roleType.equals("employee")){
					m2.put("name", "雇员");
					m2.put("users", m1);
					retList.add(m2);
				}
			}
		}
		
		return retList;
	}
	
	/**-----------------------------------------------创建工作流 END------------------------------------------------------*/
	
	/**-----------------------------------------------通过获取配置的数据源获取门户数据库中的数据----------------------------------------*/
	/**
	 * 获取门户系统的用户信息
	 * @return
	 */
	public List getMHCardUserData(){
		String sql="select id,cardUrl,RealName,roleCode from cardUser order by roleCode";
		List<Object[]> ls = DAOTools.execSelectS(sql, MHDBID);
		List<Map<String, String>> retList=new ArrayList<Map<String,String>>();
		if(ls!=null){
			for(Object[] objs:ls){
				Map<String, String> map=new HashMap<String, String>();
				map.put("id", objs[0].toString());
				map.put("cardUrl", objs[1].toString());
				map.put("RealName", objs[2].toString());
				map.put("roleCode", objs[3].toString());
				retList.add(map);
			}
		}
		return retList;
	}

    public String getRealNameByCardUrl(String cardUrl){
        String ret=null;
        String sql="select RealName from cardUser where cardUrl='"+cardUrl+"'";
        List<Object[]> al =DAOTools.execSelectS(sql, MHDBID);
        Object[] resTmp = null;
        for (int i = 0; i < al.size(); i++)
        {
            resTmp = al.get(i);
            if (resTmp[0] != null)
            {
                ret = String.valueOf(resTmp[0]);

            }
        }

        return ret;
    }

	/**
	 * 获取门户系统所有的角色
	 * @return
	 */
	public List getMHRoleData(){
		String sql="select roleCode from cardUser group by roleCode order by roleCode";
		List<Object[]> ls = DAOTools.execSelectS(sql, MHDBID);
		List<Map<String, String>> retList=new ArrayList<Map<String,String>>();
		if(ls!=null){
			for(Object[] objs:ls){
				Map<String, String> map=new HashMap<String, String>();
				map.put("roleCode", objs[0].toString());
				retList.add(map);
			}
		}
		return retList;
	}
	/**
	 * 获取门户系统中的所有技能组数据
	 * @return
	 */
	public List getMHAllJNZData(){
		String sql="select id,skName,skId,skType from IM_SkillGroupInfo group by skId";
		List<Object[]> ls = DAOTools.execSelectS(sql, MHDBID);
		List<Map<String, String>> retList=new ArrayList<Map<String,String>>();
		if(ls!=null){
			for(Object[] objs:ls){
				Map<String, String> map=new HashMap<String, String>();
				map.put("id", objs[0].toString());
				map.put("skName", objs[1].toString());
				map.put("skId", objs[2].toString());
				map.put("skType", objs[3].toString());
				retList.add(map);
			}
		}
		return retList;
	}
	/**
	 * 获取门户系统中的所有技能组类型数据
	 * @return
	 */
	public List getMHJNZTypeData(){
		List<Map> ls = getServiceSqlSession().db_selectList("getMHJNZType");
		return ls;
	}
	/**
	 * 通过技能组类型获取门户系统中该技能组类型下的技能组
	 * @return
	 */
	public List getMHJNZDataByType(String skType){
		String sql="select id,skName,skId,skType from IM_SkillGroupInfo where skType='"+skType+"'";
		List<Object[]> ls = DAOTools.execSelectS(sql, MHDBID);
		List<Map<String, String>> retList=new ArrayList<Map<String,String>>();
		if(ls!=null){
			for(Object[] objs:ls){
				Map<String, String> map=new HashMap<String, String>();
				map.put("id", objs[0].toString());
				map.put("skName", objs[1].toString());
				map.put("skId", objs[2].toString());
				map.put("skType", objs[3].toString());
				retList.add(map);
			}
		}
		return retList;
	}
	/**
	 * 获取门户系统中所有技能组的人员数据
	 * @return
	 */
	public List getMHAllJNZUserData(){
		String sql="select `id`,`cardUrl`,`name`,`roleCode`,`skId`,`status` from IM_AgentSkillGroup where skId in (select skId from IM_SkillGroupInfo)";
		List<Object[]> ls = DAOTools.execSelectS(sql, MHDBID);
		List<Map<String, String>> retList=new ArrayList<Map<String,String>>();
		if(ls!=null){
			for(Object[] objs:ls){
				Map<String, String> map=new HashMap<String, String>();
				map.put("id", objs[0].toString());
				map.put("cardUrl", objs[1].toString());
				map.put("name", objs[2].toString());
				map.put("roleCode", objs[3].toString());
				map.put("skId", objs[4].toString());
				map.put("status", objs[4].toString());
				retList.add(map);
			}
		}		
		return retList;
	}
	/**
	 * 获取门户系统中所有技能组的人员数据
	 * @return
	 */
	public List getMHJNZUserDataBySkId(String skId){
		//获取门户所有技能组人员数据
		List<Map<String, String>> ls = getMHAllJNZUserData();
		List<Map<String, String>> reData =new ArrayList<Map<String,String>>();
		//根据skId进行筛选数据
		if(ls!=null){
			if(ls.size()>0){
				for (int i = 0; i < ls.size(); i++) {
					if(ls.get(i).get("skId").toString().equals(skId)){
						reData.add(ls.get(i));
					}
				}
			}
			
		}
		return reData;
	}
	/**
	 * 获取门户系统中所有技能组的人员数据
	 * @return
	 */
	public Map getMHJNZUserInfoBySkIdForSuiJi(String skId){
		List<Map<String, String>> ls=getMHJNZUserDataBySkId(skId);
		Map<String, String> map =new HashMap<String, String>();
		if(ls!=null){
			if(ls.size()>0){
				//进行随机分配人员
				int num = new Random().nextInt(ls.size());
				System.err.println(num);
				map.putAll(ls.get(num));
			}
		}
		return map;
	}
	/**
	 * 重构门户系统中所有技能组的人员数据格式
	 * @return
	 */
	public List getMHAllUserForJNZ(){
		//获取所有技能组下的用户数据
		List<Map<String, String>> jnzUser = getMHAllJNZUserData();
		//获取所有技能组数据
		List<Map<String, String>> jnz = getMHAllJNZData();
		//重构返回的数据格式
		List retList=new ArrayList();
		if(jnz!=null){
			for (int i = 0; i < jnz.size(); i++) {
				if(jnzUser!=null){
					String skId = jnz.get(i).get("skId");
					Map temp=new HashMap();
					temp.put("id", jnz.get(i).get("id"));
					temp.put("skName", jnz.get(i).get("skName"));
					temp.put("skId", jnz.get(i).get("skId"));
					temp.put("skType", jnz.get(i).get("skType"));
					Map<String, String> tUser = new HashMap<String, String>();
					for (int j = 0; j < jnzUser.size(); j++) {
						if(jnzUser.get(j).get("skId").equals(skId)){
							tUser.put("id", jnzUser.get(j).get("id"));
							tUser.put("cardUrl", jnzUser.get(j).get("cardUrl"));
							tUser.put("name", jnzUser.get(j).get("name"));
							tUser.put("roleCode", jnzUser.get(j).get("roleCode"));
							tUser.put("skId", jnzUser.get(j).get("skId"));
							tUser.put("status", jnzUser.get(j).get("status"));
						}						
					}
					temp.put("users", tUser);
					retList.add(temp);
				}else{
					logger.debug("获取所有技能组用户数据异常，出现null值："+jnzUser);
				}				
			}
		}else{
			logger.debug("获取所有技能组数据异常，出现null值："+jnz);
		}
		return retList;
	}
	/**
	 * 根据技能组标识重构门户系统中技能组的人员数据格式
	 * @return
	 */
	public List getMHAllUserForJNZBySkId(List ls){
		//获取所有技能组下的用户数据
		List<Map<String, String>> jnzUser = getMHAllJNZUserData();
		//获取所有技能组数据
		List<Map<String, String>> jnz = getMHAllJNZData();
		//重构返回的数据格式
		List retList=new ArrayList();
		if(jnz!=null){
			for (int i = 0; i < jnz.size(); i++) {
				if(jnzUser!=null){
					String skId = jnz.get(i).get("skId");
					int c=0;//判断是否否和条件
					if(ls!=null){
						for (int u = 0; u < ls.size(); u++) {
							if(ls.get(u).equals(skId)){
								c++;
							}
						}
					}else{
						break;
					}
					if(c==0){
						continue;
					}
					Map temp=new HashMap();
					temp.put("id", jnz.get(i).get("id"));
					temp.put("skName", jnz.get(i).get("skName"));
					temp.put("skId", jnz.get(i).get("skId"));
					temp.put("skType", jnz.get(i).get("skType"));
					List<Map<String, String>> tUser = new ArrayList<Map<String,String>>();
					for (int j = 0; j < jnzUser.size(); j++) {
						if(jnzUser.get(j).get("skId").equals(skId)){
							Map t =new HashMap();
							t.put("id", jnzUser.get(j).get("id"));
							t.put("cardUrl", jnzUser.get(j).get("cardUrl"));
							t.put("name", jnzUser.get(j).get("name"));
							t.put("roleCode", jnzUser.get(j).get("roleCode"));
							t.put("skId", jnzUser.get(j).get("skId"));
							t.put("status", jnzUser.get(j).get("status"));
							tUser.add(t);
						}
					}
					temp.put("users", tUser);
					retList.add(temp);
				}else{
					logger.debug("获取所有技能组用户数据异常，出现null值："+jnzUser);
				}				
			}
		}else{
			logger.debug("获取所有技能组数据异常，出现null值："+jnz);
		}
		return retList;
	}


    /*类似这样的功能，用静态的更干净，调用更灵活。只要小心全局变量就可以*/
    public static String getMHRealNameByCardUrl(String cardUrl){
        String RealName = "";
        String sql="select RealName FROM cardUser where cardUrl='"+cardUrl+"'";
        ArrayList<Object[]> al = DAOTools.execSelectS(sql, MHDBID);
        Object[] resTmp = null;
        for (int ii = 0; ii < al.size(); ii++)
        {
            resTmp = al.get(ii);
            if (resTmp[0] != null)
            {
                RealName = String.valueOf(resTmp[0]);
            }
        }
        return RealName;
    }
    /**
	 * 通过loginName获取公司数据
	 * @return
	 */
	public static List getCompanDataByLoginName(String loginName){
		String companId = "";
		String sql="select companyId from MH_ZDeskUserCompany where loginName='"+loginName+"' limit 1";
		LogUtil.debug("fn:getCompanDataByLoginName sql"+sql);
		ArrayList<Object[]> al = DAOTools.execSelectS(sql, MHDBID);
		if(al!=null && al.size()>0){
			Object[] resTmp = null;
	        for (int ii = 0; ii < al.size(); ii++)
	        {
	            resTmp = al.get(ii);
	            if (resTmp[0] != null)
	            {
	            	companId = String.valueOf(resTmp[0]);
	            }
	        }
		}
		List<Map<String, String>> retList=new ArrayList<Map<String,String>>();
		if(!"".equals(companId)){
			String sql2="select id,userName,companyName,companyDesc,headimgurl from DB_DockerPools where id = '"+companId+"' limit 1";	
			LogUtil.debug("fn:getCompanDataByLoginName sql2"+sql2);
			List<Object[]> ls = DAOTools.execSelectS(sql2, MHDBID);
			if(ls!=null){
				for(Object[] objs:ls){
					Map<String, String> map=new HashMap<String, String>();
					map.put("id", objs[0].toString());
					map.put("userName", objs[1].toString());
					map.put("companyName", objs[2].toString());
					map.put("companyDesc", objs[3].toString());
					map.put("headimgurl", objs[4].toString());
					retList.add(map);
				}
			}
		}
		
		
		return retList;
	}

    /***
     *
     * @param cardUrl 用户标识
     * @param type 验证类型
     *             用户是否在流程中：isUserInWorkFlow
     *             是否节点执行者：isNodeExecUser
     *             是否执行者：isExecUser
     * @return
     * desc 门户相关的权限验证，如用户是否在工作流中、用户是否是工作流参与者、集中方便管理
     *
     */
    public static boolean MHWorkFlowVerify(String cardUrl,String type,String workFlowId,String stepId){

        if(cardUrl!=null && cardUrl.length()>0){
            if(type.equals("isUserInWorkFlow")){
                String sql="select cardUrl from Workflow_Relation_Table where workflow_id='"+workFlowId+"' and cardUrl='"+cardUrl+"'";
                ArrayList<Object[]> al =DAOTools.execSelectS(sql,DBIDRMHTmp);
                if(al.size()>0){
                    return true;
                }
                return false;
            }else if(type.equals("isNodeExecUser")){
                String sql="select cardUrl from Workflow_Relation_Table where workflow_id='"+workFlowId+"' and stepId='"+stepId+"' and cardUrl='"+cardUrl+"' and role='2'";
                ArrayList<Object[]> al =DAOTools.execSelectS(sql,DBIDRMHTmp);
                if(al.size()>0){
                    return true;
                }
                return false;
            }else if(type.equals("isExecUser")){
                String sql="select cardUrl from Workflow_Relation_Table where workflow_id='"+workFlowId+"' and cardUrl='"+cardUrl+"' and role='2'";
                ArrayList<Object[]> al =DAOTools.execSelectS(sql,DBIDRMHTmp);
                if(al.size()>0){
                    return true;
                }
                return false;
            }
        }

        return false;
    }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WorkflowService ws=new WorkflowService();
//		List<Map<String, String>> list=ws.getZDeskLoginNameBycardUrl("vYBJjuJFnmauaeeA3eEzErA3");
//		if(list!=null && list.size()!=0){
//			for (int i = 0; i < list.size(); i++) {
//				System.out.println(list.get(i).get("zdeskUserLoginName").toString());
//			}
//			
//		}
		//System.err.println(ws.getZuZhiUserDataByNodeIdOrTemplateId("",true,"100"));
		//System.err.println(ws.getJueSeUserDataByNodeIdOrTemplateId("",true,"100"));
		//List l=new ArrayList();
		//l.add("djnzcs");
		//l.add("skcs");
		//System.err.println(ws.getMHAllUserForJNZBySkId(l));
		System.err.println(ws.getMHJNZUserInfoBySkIdForSuiJi("skts"));
		
//		Map<String, String> tt=new HashMap<String, String>();
//		
//		tt.put("zdeskUserId", "qqq");
//		tt.put("zdeskUserLoginName", "qqq");
//		tt.put("zdeskUserName", "qqq");
//		tt.put("cardUrl", "qqq");
//		tt.put("currentState", "qqq");
//		tt.put("bindingTime", "2015-05-01 00:00:00");
//		tt.put("unbundlingTime", "2015-05-01 00:00:00");
//		
//		// TODO Auto-generated method stub
//		ws.save(tt);
	}
	@Override
	public AppSqlSessionDbid getServiceSqlSession() {
		// TODO Auto-generated method stub
		 return (AppSqlSessionDbid)this.getBean("requestservice");
	}

}
