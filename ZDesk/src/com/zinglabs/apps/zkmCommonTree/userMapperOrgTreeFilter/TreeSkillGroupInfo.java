package com.zinglabs.apps.zkmCommonTree.userMapperOrgTreeFilter;

import com.zinglabs.apps.skillGroupInfo.service.IM_SkillGroupInfoService;
import com.zinglabs.base.core.frame.BaseService;
import com.zinglabs.base.core.springSupport.impl.AppSqlSessionDbid;
import com.zinglabs.db.MHBO;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.CookieUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/31 0031.
 */
public class TreeSkillGroupInfo extends BaseService implements UserMapperOrgTreeFilter {
    IM_SkillGroupInfoService skillService = new IM_SkillGroupInfoService();

    @Override
    public AppSqlSessionDbid getServiceSqlSession() {
        return null;
    }

    /**
     * @param users       所用用户信息
     *                    otherParams 一些用户信息
     *                    如otherParams.get("loginName"); 可以取到用户信息
     * @param otherParams
     */
    @Override
    public void userFilter(List<Map> users, Map<String, Object> otherParams) {

    }

    /**
     * @param rootOrgs
     * @param childOrgs
     * @param otherParams
     */
    @Override
    public void orgFilter(List<Map> rootOrgs, List<Map> childOrgs, Map<String, Object> otherParams) {

    }

    @Override
    public List<Map> getUserByFilter(List<Map> users, Map<String, Object> otherParams) {
        List<Map> rusers = new ArrayList<Map>();
        LogUtil.debug("----TreeSkillGroupInfo.getUserByFilter：根据参数：" + otherParams + ",开始过滤组织机构树（用户只能是绑定用户）。所有人员:" + users.toString());
        String companyId = "";
        try {
            CookieUtils.pauseMyMemDBId();
            String cardUrl = MHBO.getMHCardUrlByZDeskUser(otherParams.get("loginName").toString());
            companyId = MHBO.loadUserCompany(cardUrl, otherParams.get("loginName").toString());
            LogUtil.debug("this companyId"+companyId) ;
        } catch (Exception e) {
            LogUtil.error("----TreeSkillGroupInfo.getUserByFilter：查询companyId异常");
            LogUtil.error(e);
        } finally {
            CookieUtils.startMyMemDBId();
        }
        Map m = new HashMap();
        for (int i = 0; users.size() > i; i++) {
            try {
            	CookieUtils.pauseMyMemDBId();
                Map slmap = new HashMap();
                slmap.put("zdeskUserLoginName", users.get(i).get("id"));
                slmap.put("companyId", companyId);
                m = skillService.getServiceSqlSession().db_selectOne("Selectbangding", slmap,"WXSDK");
                LogUtil.debug("查询该用户是否绑定" + m);
            } catch (Exception e) {
                LogUtil.error("----TreeSkillGroupInfo.getUserByFilter：查询companyId下用户异常：" + companyId + "");
                LogUtil.error(e);
            } finally {
                CookieUtils.startMyMemDBId();
            }
            try {
                if (m != null && users.get(i).get("id") != null && m.get("zdeskUserLoginName") != null && String.valueOf(m.get("zdeskUserLoginName")).equals(String.valueOf(users.get(i).get("id")))) {
                    users.get(i).put("cardUrl",m.get("cardUrl")) ;
                    users.get(i).put("text",m.get("RealName")==""||m.get("RealName")==null?"":m.get("RealName")) ;
                    rusers.add(users.get(i));
                    LogUtil.debug("----TreeSkillGroupInfo.getUserByFilter：共:" + users.size() + "个人---第" + i + "个人---" + "该用户已绑定:" + users.get(i));
                } else {
                    LogUtil.debug("----TreeSkillGroupInfo.getUserByFilter：共:" + users.size() + "个人---第" + i + "个人---" + "该用户未绑定:" + users.get(i));

                }
            } catch (Exception e) {
                LogUtil.error("----TreeSkillGroupInfo.getUserByFilter：" + "TreeSkillGroupInfo,查询绑定用户数据异常");
                LogUtil.error(e);
            }
        }
        System.out.println("过滤后的，已绑定的用户：" + rusers);
        if (otherParams.get("skId") == null || String.valueOf(otherParams.get("skId")).length() == 0) {
            return rusers;
        }
        LogUtil.debug("----TreeSkillGroupInfo.getUserByFilter:查询技能组skId:" + otherParams.get("skId") + "，将已存在的用户设置选中属性。");
        try {
            String skId = String.valueOf(otherParams.get("skId"));
            Map skMap = new HashMap();
            skMap.put("skId", skId);
            List<Map> l = skillService.getServiceSqlSession().db_selectList("SelectCardUrlFromIM_AgentSkillGroup", skMap);
            LogUtil.debug("----TreeSkillGroupInfo.getUserByFilter：" + "根据当前技能组标识skId:" + skId + "。查找该技能组下人员：" + l);
            for (int ll = 0; l.size() > ll; ll++) {
                Map lmap = new HashMap();
                lmap.put("cardUrl", l.get(ll).get("cardUrl"));
          //      lmap.put("text", l.get(ll).get("RealName")==""||l.get(ll).get("RealName")==null?"":l.get(ll).get("RealName"));
          //      LogUtil.debug("test:"+l.get(ll).get("RealName")) ;
          //      LogUtil.debug("map:"+lmap) ;
                try {
                    CookieUtils.pauseMyMemDBId();
                    Map sillmap = skillService.getServiceSqlSession().db_selectOne("SelectLoginNameFromChatWorkflow_binding", lmap,"WXSDK");
                    l.get(ll).put("zdeskUserLoginName", sillmap.get("zdeskUserLoginName"));
                } catch (Exception e) {
                    LogUtil.error("----TreeSkillGroupInfo.getUserByFilter：" + "zdeskUserLoginName,查询绑定用户数据异常");
                    LogUtil.error(e);
                } finally {
                    CookieUtils.startMyMemDBId();
                }
            }

            for (int j = 0; j < l.size(); j++) {
                for (int k = 0; k < users.size(); k++) {
                    try {
                        if (String.valueOf(users.get(k).get("id")).equals(String.valueOf(l.get(j).get("zdeskUserLoginName")))) {
                            users.get(k).put("checked", "true");
                            LogUtil.debug("设置选中属性:" + users.get(k));
                        }
                    } catch (Exception e) {
                        LogUtil.error("设置选中属性异常:" + users.get(k));
                        LogUtil.error(e);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.error("----TreeSkillGroupInfo.getUserByFilter：" + "获取当前技能组skid异常，或者查询技能组下人员异常");
            LogUtil.error(e);
        }
        return rusers;
    }
}
