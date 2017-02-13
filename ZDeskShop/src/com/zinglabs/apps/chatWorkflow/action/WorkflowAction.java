package com.zinglabs.apps.chatWorkflow.action;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zinglabs.log.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.chatWorkflow.WorkflowService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.CookieUtils;
import com.zinglabs.util.JsonUtils;
@Controller
@RequestMapping("/*/MHWorkflow")
public class WorkflowAction extends BaseAction {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	
	/**
	 * 保存绑定 微信门户用户与ZDesk用户进行绑定
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/saveBinding")
	public void saveBinding(@RequestParam Map<String,String> map,HttpServletRequest request,HttpServletResponse response){

		//1.判断用户名是否存在
		if (!getService().checkLoginName(map.get("loginName"))) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "用户账号不存在！"),response);
			return;
		}
		//2.判断用户名所对应的密码是否填写正确
		Map<String, String> m =getService().getUserInfoByLoginName(map.get("loginName"));
		if(!m.get("pwd").toString().equals(map.get("loginPwd"))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "用户账号所对应的密码不正确！"),response);
			return;
		}
		//3.获取当前微信用户对应的cardUrl
		String cardUrl="";
		try {
			cardUrl= CookieUtils.getMhCardUrlByCookie(request,null);
			if(cardUrl==null || cardUrl.equals("")){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "当前登录信息已失效，请重新登录门户！"),response);
				return;
			}
		} catch (Exception e) {
			logger.error("获取cookie信息出现异常，cardUrl:"+cardUrl);
			e.printStackTrace();
		}
		//4.判断该用户账号是否已绑定过  如绑定过则删除历史绑定记录  重新进行绑定
		List<Map<String, String>> ll = getService().getBindingByCardUrlForReList(cardUrl);
		if(ll!=null){
			if(ll.size()!=0){
				//进行删除历史绑定数据操作
				getService().deleteBindingById(ll.get(0));
			}			
		}
		//4.判断ZDesk用户账号是否已绑定过 有则进行删除
		if(getService().checkBindingByLoginName(map.get("loginName"))){
//			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "该用户账号已绑定！"),response);
//			return;
            /*有需要重新绑定的场景，只要用户密码正确就更新绑定信息*/
            getService().delBindUser(m.get("loginName"));
		}
		
        String RealName=getService().getRealNameByCardUrl(cardUrl);

		//5.保存绑定数据
		Map<String, String> userMap=new HashMap<String, String>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		userMap.put("zdeskUserId", m.get("id"));
		userMap.put("zdeskUserLoginName", m.get("loginName"));
		userMap.put("zdeskUserName", m.get("name"));
		userMap.put("gender", map.get("gender"));
		userMap.put("cardUrl", cardUrl);
		userMap.put("currentState", "1");
		userMap.put("bindingTime", df.format(new Date()));
		userMap.put("unbundlingTime", df.format(new Date()));
        userMap.put("RealName", RealName);
		
		if(getService().save(userMap)){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "绑定成功！"),response);			
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "绑定异常！"),response);
		}
		
	}
	/**
	 * 获取门户组织机构的数据
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getMhZuZhiJiGouData")
	public void getMhZuZhiJiGouData(@RequestParam Map<String,String> map,HttpServletRequest request,HttpServletResponse response){
		Map<String, String> reMap=getService().getMhZuZhiJiGouData();
		logger.debug("获取门户组织机构：reMap:"+reMap);
		if(reMap!=null && reMap.size()!=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",reMap),response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "获取组织机构数据异常！"),response);
		}
	}
	/**
	 * 根据节点id获取当前节点所设置的组织人员范围数据
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getMhZuZhiUserData")
	public void getMhZuZhiUserData(@RequestParam Map<String,String> map,HttpServletRequest request,HttpServletResponse response){
		Map<String, String> reMap=getService().getZuZhiUserDataByNodeIdOrTemplateId(map.get("stepId").toString(),false,"");
		if(reMap!=null && reMap.size()!=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",reMap),response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取数据异常！"),response);
		}
	}
	/**
	 * 根据模板id获取所有节点所设置的组织人员范围数据
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getMhZuZhiUserDataByTemplateId")
	public void getMhZuZhiUserDataByTemplateId(@RequestParam Map<String,String> map,HttpServletRequest request,HttpServletResponse response){
        Map<String, String> reMap=null;

//        要加try catch 否则日志会记录到catalina.out 和 localhost.log中
        try {
            reMap=getService().getZuZhiUserDataByNodeIdOrTemplateId("",true,map.get("templateid"));
        }catch (Exception e){
            LogUtil.error(e,logger);
            reMap=null;
        }

		if(reMap!=null && reMap.size()!=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",reMap),response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取数据异常！"),response);
		}

	}
	/**
	 * 根据节点id获取当前节点所设置的角色人员范围数据
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getMhJueSeUserData")
	public void getMhJueSeUserData(@RequestParam Map<String,String> map,HttpServletRequest request,HttpServletResponse response){
		List<Map<String, String>> reMap=getService().getJueSeUserDataByNodeIdOrTemplateId(map.get("stepId").toString(),false,"");
		if(reMap!=null && reMap.size()!=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",reMap),response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取数据异常！"),response);
		}
	}
	/**
	 * 根据模板id获取所有节点所设置的角色人员范围数据
	 * @param map
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getMhJueSeUserDataByTemplateId")
	public void getMhJueSeUserDataByTemplateId(@RequestParam Map<String,String> map,HttpServletRequest request,HttpServletResponse response){
        List<Map<String, String>> reMap=null;
        try {
            reMap=getService().getJueSeUserDataByNodeIdOrTemplateId("",true,map.get("templateid"));
        }catch (Exception e){
            LogUtil.error(e,logger);
            reMap=null;
        }
		if(reMap!=null && reMap.size()!=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",reMap),response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取数据异常！"),response);
		}
	}
	/**
	 * 获取门户系统中的所有技能组类型数据
	 * @return
	 */
	@RequestMapping(value="/getSkill")
	public void getSkill(@RequestParam Map<String,String> map,HttpServletRequest request,HttpServletResponse response){
		List  list=getService().getMHJNZTypeData();
		if(list!=null && list.size()!=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",list),response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "查询技能组类型失败",list),response);
		}
	}
	/**
	 * 通过技能组类型获取技能组数据
	 * @return
	 */
	@RequestMapping(value="/getJNZDataByskType")
	public void getJNZDataByskType(@RequestParam Map<String,String> map,HttpServletRequest request,HttpServletResponse response){
		List list=getService().getMHJNZDataByType(map.get("skType"));
		if(list!=null && list.size()!=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",list),response);
		}
	}
	/**
	 * 获取当前登录所在的公司数据 功能点：下载公司二维码 需要获取二维码的存放url 
	 * 
	 */
	@RequestMapping(value="/getCompanyData")
	public void getCompanyData(@RequestParam Map<String,String> map,HttpServletRequest request,HttpServletResponse response){
		try {
			String cardUrl= CookieUtils.getMhCardUrlByCookie(request,null);
			CookieUtils.pauseMyMemDBId();
			List<Map<String, String>> ls = getService().getZDeskLoginNameBycardUrl(cardUrl);
			if(ls!=null && ls.size()>0){
				List list=getService().getCompanDataByLoginName(ls.get(0).get("zdeskUserLoginName"));
				if(list!=null && list.size()!=0){
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",list),response);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, ""),response);
		}finally{
			CookieUtils.startMyMemDBId();
		}
		
	}
	public WorkflowService getService(){
		return (WorkflowService)getBean("WorkflowService");
	}
}
