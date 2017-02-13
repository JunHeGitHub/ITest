package com.zinglabs.apps.skillGroupInfo.action;

import com.zinglabs.apps.skillGroupInfo.service.IM_SkillGroupInfoService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.db.MHBO;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.AppCommonUtils;
import com.zinglabs.util.CookieUtils;
import com.zinglabs.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/29 0029.
 */
@Controller
@RequestMapping(value = "/*/SkillGroupInfo")
public class IM_SkillGroupInfoAction extends BaseAction {

    @RequestMapping(value = "/getGroup")
    public void getGroup(@RequestParam
                         HashMap map, HttpServletRequest request, HttpServletResponse response) {
        try {
            List list = this.getService().selectSkillGroupInfo(map);
            postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "查询成功", list), response);
        } catch (Exception e) {
            LogUtil.error(e);
            LogUtil.error("select MH_ZDeskUserCompany Exception:");
            postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "异常"), response);
        }
    }

    @RequestMapping(value = "/addGroup")
    public void addGroup(@RequestParam
                         HashMap map, HttpServletRequest request, HttpServletResponse response) {
        String loginName = AppCommonUtils.getLoginName(request);
//        /**
//         * 先判断是否为docker模式
//         * */
//        try {
//            CookieUtils.releaseMyDBId();
//            String cardUrl = MHBO.getMHCardUrlByZDeskUser(loginName);
//            String companyId = MHBO.loadUserCompany(cardUrl, loginName);
//            if (StringUtils.isEmpty(companyId)) {
//                LogUtil.error("非docker模式");
//                postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "非docker模式"), response);
//                return;
//            }
//        } catch (Exception e) {
//            LogUtil.error("判断是否为docker模式异常");
//            postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "判断是否为docker模式异常"), response);
//            return;
//        }

//        /**
//         * 是docker模式在继续处理
//         * */
        try {
            List rl = this.getService().selectSkillGroupInfo(map) ;
            if(rl!=null&&rl.size()!=0){
                postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "技能组标识重复"), response);
                return;
            }
        }catch (Exception e){

        }

        try {
            Map rm = this.getService().addGroupInfo(map);
            postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "添加成功",rm), response);
        } catch (Exception e) {
            LogUtil.error(e);
            LogUtil.error("select MH_ZDeskUserCompany Exception:");
            postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "异常"), response);
        }
    }


    @RequestMapping(value = "/updateGroup")
    public void updateGroup(@RequestParam
                            HashMap map, HttpServletRequest request, HttpServletResponse response) {
        String loginName = AppCommonUtils.getLoginName(request);
        try {
            Map rm = this.getService().updateGroupInfo(map);
            postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "编辑成功", rm), response);
        } catch (Exception e) {
            LogUtil.error(e);
            LogUtil.error("select MH_ZDeskUserCompany Exception:");
            postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "异常"), response);
        }
    }
    @RequestMapping(value="/deleteGroup")
    public void deleteGroup(@RequestParam HashMap map,HttpServletRequest request,HttpServletResponse response){
    		
    	try {
			this.getService().deleteSK_GroupByskId(map);
			 postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功"), response);
		} catch (Exception e) {
			LogUtil.error(e);
            LogUtil.error("delete IM_SkillGroupInfo Exception:");
            postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "异常"), response);
		}
    }

    public IM_SkillGroupInfoService getService() {
        return (IM_SkillGroupInfoService) getBean("skillGroupInfoService");
    }
}
