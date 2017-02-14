package com.zinglabs.apps.skillGroupInfo.service;

import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.util.CookieUtils;
import com.zinglabs.log.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2015/7/29 0029.
 */
public class IM_SkillGroupInfoService extends BaseService {
    @Override
    public AppSqlSessionDbid getServiceSqlSession() {
        return (AppSqlSessionDbid) getBean("skillGroupInfoSqlSession");
    }

    /**
     * 查询docker下所有技能组
     */
    public List selectSkillGroupInfo(Map map) throws Exception {
        List list = this.getServiceSqlSession().db_selectList("selectSkillGroupInfo",map);
        return list;
    }

    public Map addGroupInfo(Map map) {
        //设置返回值 rm,用来定位问题
        Map rm = new HashMap();
        rm.put("b", "true");

        List<Map> dockerWaiUserList = new ArrayList();

        /**
         * 新增技能组先插入数据到当前docker库中
         * */
        try {
            this.getServiceSqlSession().db_insert("insertSkillGroupInfo", map);
        } catch (Exception e) {
            LogUtil.error(e);
            LogUtil.error("--------IM_SkillGroupInfoService.addGroupInfo:新增技能组插入当前docker表异常。");
            rm.put("mgs", "新增技能组插入当前docker表异常");
            rm.put("b", "false");
            return rm;
        }

        /**
         * 需要给技能组加用户时。
         * 先查询docker信息:
         *      1.docker外，所有已选择用户的信息（cardUrl）
         *      2.根据cardUrl查询用户信息
         * 在保存:
         *      3.存入本地docker库gentSkillGroup和AgentSkillGroupMem表中
         * */
        if (map.get("skId") == null || map.get("skId") == "") {
            rm.put("mgs", "缺少skId");
            rm.put("b", "false");
            return rm;
        }

        if (map.get("checkUsersAdd") != null && "clear".equals(map.get("checkUsersAdd") != null)) {
            this.getServiceSqlSession().db_delete("deleteAllAgentSkillGroup", map);
            this.getServiceSqlSession().db_delete("deleteAllAgentSkillGroupMem", map);
        }
        if (map.get("checkUsersAdd") != null && String.valueOf(map.get("checkUsersAdd")).length() > 0) {
            try {
                CookieUtils.pauseMyMemDBId();
                String strArray[] = String.valueOf(map.get("checkUsersAdd")).trim().split(",");
                List sl = new ArrayList();
                for (String str : strArray) {
                    sl.add(str);
                }
                System.out.println("strArray:" + sl);
                map.put("zdeskUserLoginName", sl);
                dockerWaiUserList = this.getServiceSqlSession().db_selectList("SelectUserFrom_chatWorkflow_bindingAnd_cardUser", map);
            } catch (Exception e) {
                LogUtil.error(e);
                LogUtil.error("--------IM_SkillGroupInfoService.addGroupInfo:查询绑定用户数据异常。");
                rm.put("mgs", "查询绑定用户数据异常");
                rm.put("b", "false");
                return rm;
            } finally {
                CookieUtils.startMyMemDBId();
            }
            System.out.println("dockerWaiUserList:" + dockerWaiUserList.size());

            //先全量清除所有人，再添加。
            this.getServiceSqlSession().db_delete("deleteAllAgentSkillGroup", map);
            this.getServiceSqlSession().db_delete("deleteAllAgentSkillGroupMem", map);
            for (int i = 0; i < dockerWaiUserList.size(); i++) {
                try {
                    dockerWaiUserList.get(i).put("skId", map.get("skId"));
                    this.getServiceSqlSession().db_insert("insertAgentSkillGroup", dockerWaiUserList.get(i));
                    this.getServiceSqlSession().db_insert("insertAgentSkillGroupMem", dockerWaiUserList.get(i));
                } catch (Exception e) {
                    rm.put("第" + i + "次插入用户信息异常", dockerWaiUserList.get(i));
                }
            }
        }

        return rm;
    }

    public Map updateGroupInfo(Map map) {
        //设置返回值 rm,用来定位问题
        Map rm = new HashMap();
        rm.put("b", "true");
        List<Map> dockerWaiUserList = new ArrayList();

        /**
         * 先更改当前docker下,技能组
         * */
        try {
            this.getServiceSqlSession().db_update("updateSkillGroupInfo", map);
        } catch (Exception e) {
            LogUtil.error(e);
            LogUtil.error("--------IM_SkillGroupInfoService.addGroupInfo:新增技能组插入当前docker表异常。");
            rm.put("mgs", "新增技能组插入当前docker表异常");
            rm.put("b", "false");
            return rm;
        }

        /**
         * 需要给技能组更改用户时。
         * 先查询docker信息:
         *      1.docker外，所有已选择用户的信息（cardUrl）
         *      2.根据cardUrl查询用户信息
         * 在保存:
         *      3.存入本地docker库gentSkillGroup和AgentSkillGroupMem表中
         * */
        if (map.get("skId") == null || map.get("skId") == "") {
            rm.put("mgs", "缺少skId");
            rm.put("b", "false");
            return rm;
        }

        if (map.get("checkUsersUpdate") != null && "clear".equals(map.get("checkUsersUpdate") != null)) {
            this.getServiceSqlSession().db_delete("deleteAllAgentSkillGroup", map);
            this.getServiceSqlSession().db_delete("deleteAllAgentSkillGroupMem", map);
        }
        if (map.get("checkUsersUpdate") != null && String.valueOf(map.get("checkUsersUpdate")).length() > 0) {
            try {
                CookieUtils.pauseMyMemDBId();
                String strArray[] = String.valueOf(map.get("checkUsersUpdate")).trim().split(",");
                List sl = new ArrayList();
                for (String str : strArray) {
                    sl.add(str);
                }
                System.out.println("strArray:" + sl);
                map.put("zdeskUserLoginName", sl);

                dockerWaiUserList = this.getServiceSqlSession().db_selectList("SelectUserFrom_chatWorkflow_bindingAnd_cardUser", map);
                System.out.println("dockerWaiUserList:" + dockerWaiUserList.size());
                CookieUtils.startMyMemDBId();
                //先全量清除所有人，再添加。
                this.getServiceSqlSession().db_delete("deleteAllAgentSkillGroup", map);
                this.getServiceSqlSession().db_delete("deleteAllAgentSkillGroupMem", map);
                for (int i = 0; i < dockerWaiUserList.size(); i++) {
                    try {
                        dockerWaiUserList.get(i).put("skId", map.get("skId"));
                        this.getServiceSqlSession().db_insert("insertAgentSkillGroup", dockerWaiUserList.get(i));
                        this.getServiceSqlSession().db_insert("insertAgentSkillGroupMem", dockerWaiUserList.get(i));
                    } catch (Exception e) {
                        rm.put("第" + i + "次插入用户信息异常", dockerWaiUserList.get(i));
                    }
                }
            } catch (Exception e) {
                LogUtil.error(e);
                LogUtil.error("--------IM_SkillGroupInfoService.addGroupInfo:查询绑定用户数据异常。");
                rm.put("mgs", "查询绑定用户数据异常");
                rm.put("b", "false");
                return rm;
            } finally {
                CookieUtils.startMyMemDBId();
            }
        }

        return rm;
    }
	/*
	 * 根据skId删除技能组
	 */
	public boolean deleteSK_GroupByskId(Map map){
		int result=getServiceSqlSession().db_delete("deleteSK_GroupByskId",map);
		if(result>0){
			return true;
		}
		return false;
	}
    public static void main(String args[]) {
        IM_SkillGroupInfoService i = new IM_SkillGroupInfoService();
        List l = new ArrayList();
        try {
            //        i.getServiceSqlSession().db_delete("deleteAllAgentSkillGroup",Map map);
        } catch (Exception e) {

        }
        System.out.println(l);
    }
}
