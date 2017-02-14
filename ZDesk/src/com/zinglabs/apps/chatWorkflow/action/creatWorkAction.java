package com.zinglabs.apps.chatWorkflow.action;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qq.weixin.sdk.util.ATUtil;
import com.zinglabs.db.DAOTools;
import com.zinglabs.apps.appCommonCurd.filter.Params;
import com.zinglabs.apps.appCommonCurd.util.PrimaryKeyBuilder;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.appCommonCurd.AppCommonsCurdService;
import com.zinglabs.apps.chatWorkflow.WorkflowService;
import com.zinglabs.apps.chatWorkflow.requestService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

import com.qq.weixin.sdk.util.CardBO;


@Controller
@RequestMapping("/*/MENHUWX/creatWork")
public class creatWorkAction extends BaseAction{
	
	public static Logger logger = LoggerFactory.getLogger("ZDesk");


    /*静态返回值，最终确定后统一管理*/
    public static final String RET_SUCESS_LEFT="{\"success\":\"true\",\"mgs\":\"";

    public static final String RET_SUCESS_RIGHT="\"}";

    public static final String RET_FAILED_LEFT="{\"success\":\"false\",\"mgs\":\"";

    public static final String RET_FAILED_RIGHT="\"}";

    public final static String RET_STATUS_SUCESS="{\"retcode\":0}";

    public final static String RET_STATUS_FAILED="{\"retcode\":-1}";

    /*执行者退出工作流，提示先委托工作  1000~4000已经使用 后续从 +-5000开始返回  与页面procErr函数对应*/
    public final static String RET_WOOW_LEVEL_IS_EXEC="{\"error\":-5001}";

	@RequestMapping(value="/InsertWorkTable")
	public void insertWorkTable(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {
		try {

            String cardUrl= CookieUtils.getMhCardUrlByCookie(request, null);

            if(cardUrl==null || cardUrl.length()==0){
//                这里返回值要与 门户其它模块的返回值对接  NcardServlet.RET_STATUS_NO_LOGIN   页面会引导客户重新调用微信接口认证
                postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作超时，请重试"),response);
                return;
            }
          /*//获取当前登录人与ZDesk用户账号的登录名
    		String loginName = "";
    		WorkflowService ws =new WorkflowService();
    		List<Map<String, String>> list=ws.getZDeskLoginNameBycardUrl(cardUrl);
    		if(list!=null && list.size()!=0){
    			for (int i = 0; i < list.size(); i++) {
    				loginName = list.get(i).get("zdeskUserLoginName").toString();
    			}
    		}*/
            
//            WorkflowService ws =new WorkflowService();
//            String RealName=ws.getRealNameByCardUrl(cardUrl);

            /*类似的工具类方法，完全没必要对象化。最好都直接使用静态方法*/
            String RealName=WorkflowService.getMHRealNameByCardUrl(cardUrl);


            HashMap<String,String> retMap=null;
            List<Map> columnValuesMapList=null;
            /*需要分配技能组的流程，参数包括skcode 分配成功 返回被分配用户CardUrl。会赋给skUserCardUrl*/
            String skUserCardUrl=null;
            /*新建立工作流的id*/
            String workflow_id=null;

            String skId=null;

            if(map.containsKey(Params.COLUMN_VALUES)){
                String pkVal= PrimaryKeyBuilder.getPrimaryValue("integer", map, String.valueOf(map.get(Params.DBID)));
                String columnValuesTmp=String.valueOf(map.get(Params.COLUMN_VALUES));
                columnValuesTmp=columnValuesTmp.replace("\"woow_chat_id\":\"WOOW\"","\"id\":\""+pkVal+"\",\"woow_chat_id\":\"WOOW"+pkVal+"\"");
                logger.debug("insertWorkTable "+Params.COLUMN_VALUES+":"+columnValuesTmp);
                map.put(Params.COLUMN_VALUES,columnValuesTmp);
                columnValuesMapList=(List<Map>) JsonUtils.stringToJSON(columnValuesTmp, List.class);

//                初始化通信信息
//                参数：
//                param.get("chatId") 通信标识 工作流WOOW开头
//                param.get("cardUrl") 发起人
//                param.get("skName") 传技能组触发分配

//                返回： ret hashmap
//                ret.get("cardUrl") 分配的座席
//                ret.get("retcode") 返回值  3001 成功 -3001 分配或初始化通信信息失败

                HashMap<String,String> params=new HashMap<String, String>();
                params.put("chatId","WOOW"+pkVal);
                params.put("cardUrl",cardUrl);


                if(columnValuesMapList.get(0)!=null && columnValuesMapList.get(0).containsKey("skcode")){
                    skId=String.valueOf(columnValuesMapList.get(0).get("skcode"));
                    params.put("skName",skId);
                }

                retMap=CardBO.startEasyChat(params);

                if(retMap.containsKey("cardUrl")){
                    skUserCardUrl=retMap.get("cardUrl");
                }

                LogUtil.debug("create Workflow skId:"+skId+" "+skUserCardUrl,logger);

            }

            if(retMap.containsKey("retcode") && retMap.get("retcode").equals("3001")){
                //添加一条工作流
            	List<Map> columnValues=(List<Map>) JsonUtils.stringToJSON(map.get("columnValues").toString(), List.class);
                columnValues.get(0).put("workflow_creater",cardUrl);//cardUrl暂时写死
                map.put("columnValues", JsonUtils.genUpdateDataReturnJsonStr(columnValues));
                Map rMap=this.getService().common_insertOrUpdate(map,request);

                if(rMap.containsKey("idValues")){
                    List alTmp=(List)rMap.get("idValues");
                    workflow_id=String.valueOf(alTmp.get(0));
                }

                if(rMap.isEmpty()||Integer.parseInt(String.valueOf(rMap.get("total")))==0 || workflow_id==null || workflow_id.length()==0){
                    rMap.put("success", false);
                    rMap.put("mgs", "创建工作流异常！");
                    logger.error("向工作流表Workflow_Table插入数据产生异常！map:"+map+"--执行返回结果rMap："+rMap);
                    postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap),response);
                    return;
                }
//			//为修改聊天窗口标识修改必要参数
//			map.remove("columnValues");
//			map.remove("beanId");
//			map.remove("nameSpace");
//			String columnValues="{\"woow_chat_id\":\"WOOW"+String.valueOf(rMap.get("idValues")).replace("[","").replace("]","")+"\",\"id\":\""+String.valueOf(rMap.get("idValues")).replace("[","").replace("]","")+"\"}";
//			map.put("columnValues", columnValues);
//			map.put("beanId", "commonUpdateFilter");
//			map.put("nameSpace", "com.zinglabs.apps.appCommonCurd.pojo.common_update");
//			//如果工作流添加成功 更改聊天窗口标识（WOOW+聊天工作流id）
//			int num=this.getService().common_update(map,request);
//			if(num==0){
//				String UColumnValues=rMap.get("idValues").toString().replace("[", "").replace("]", "");
//				map.put("columnValues", UColumnValues);
//				map.put("beanId", "commonDeleteFilter");
//				map.put("nameSpace", "com.zinglabs.apps.appCommonCurd.pojo.common_delete");
//				int deleteNum=this.getService().common_delete(map,request);
//				rMap.put("success", false);
//				rMap.put("mgs", "创建工作流异常！");
//				logger.error("更新工作流表Workflow_Table中字段woow_chat_id产生异常！columnValues:"+UColumnValues);
//				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap),response);
//			    return;
//			}

                //为添加人员关联表修改必要参数

                /*几乎全局都要使用，提前初始化好放一起*/
                String glDataStr=map.get("glData").toString();
                String templateId=map.get("templateId").toString();
                logger.debug("节点数据 ,glDataStr:"+glDataStr);

                Map skAssignUserMap=null;
                List<Map> skUserList=null;
                requestService rs=new requestService();
                /*需要分配技能组的工作流*/
                if(skUserCardUrl!=null && skUserCardUrl.length()>0){

                    /*目前只能处理单节点，此函数应该调整为静态函数。没有抽象成对象的意义，只需要归纳整理，方便了解维护*/
                    Map stepmap=rs.getNodeDataByTemplateId(templateId);

                    String skUserRealName=WorkflowService.getMHRealNameByCardUrl(skUserCardUrl);

                    skAssignUserMap=new HashMap<String, String>();
                    skAssignUserMap.put("workflow_id", workflow_id);
                    skAssignUserMap.put("stepLogo", "node");
                    skAssignUserMap.put("stepId",stepmap.get("id"));
                    skAssignUserMap.put("stepName", stepmap.get("stepName"));
                    skAssignUserMap.put("description", stepmap.get("description"));
                    skAssignUserMap.put("cardUrl", skUserCardUrl);
                    skAssignUserMap.put("user_name", skUserRealName);
                    skAssignUserMap.put("role", "2");
                    skAssignUserMap.put("staff_state", "待处理");

                    skUserList=new ArrayList<Map>();
                    skUserList.add(skAssignUserMap);

                    LogUtil.debug("assign sk user workflow "+stepmap.get("id")+" "+skUserCardUrl+" "+workflow_id,logger);

                }else{

                }

                boolean isOk=glData2Work(workflow_id,glDataStr,cardUrl,RealName,request,skUserList);
                if(!isOk){
                    String UColumnValues=workflow_id;
                    map.put("columnValues", UColumnValues);
                    map.put("beanId", "commonDeleteFilter");
                    map.put("nameSpace", "com.zinglabs.apps.appCommonCurd.pojo.common_delete");
                    int deleteNum=this.getService().common_delete(map,request);


                    rMap.put("success", false);
                    rMap.put("mgs", "创建工作流异常！");
                    logger.error("向工作流与人员关联表Workflow_Relation_Table插入数据产生异常！columnValues:"+UColumnValues);
                    postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap),response);
                    return;
                }

                //可添加对聊天窗口动作的操作

                rMap.put("success", true);
                rMap.put("mgs", "创建工作流成功");
                postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(rMap),response);

            }else{
                postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "创建工作流异常！"),response);
            }
        } catch (Exception e) {
            LogUtil.error(e,logger);
//			e.printStackTrace();
//			logger.error("创建门户工作流"+e.getMessage());
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "创建工作流异常！"),response);
			return ;
		}
	}
    @RequestMapping(value="/inviteWoow")
    public void inviteWoow(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {
        try {
            String cardUrl= CookieUtils.getMhCardUrlByCookie(request, null);
            if(cardUrl==null || cardUrl.length()==0){
//                这里返回值要与 门户其它模块的返回值对接  NcardServlet.RET_STATUS_NO_LOGIN   页面会引导客户重新调用微信接口认证
                postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作超时，请重试"),response);
                return;
            }
            String loginName = "";
            WorkflowService ws =new WorkflowService();
            List<Map<String, String>> list=ws.getZDeskLoginNameBycardUrl(cardUrl);
            if(list!=null && list.size()!=0){
                for (int i = 0; i < list.size(); i++) {
                    loginName = list.get(i).get("zdeskUserLoginName").toString();
                    break;
                }
            }

            String glDataStr=map.get("glData").toString();
            boolean isOk=glData2Work(null,glDataStr,cardUrl,null,request,null);
//            临时这样后面扩展成函数，Map太占资源了
            if(isOk){
                postStrToClient(RET_SUCESS_LEFT+"邀请成功"+RET_SUCESS_RIGHT,response);
            }else{
                postStrToClient(RET_FAILED_LEFT+"邀请失败"+RET_FAILED_RIGHT,response);
            }

        }catch (Exception e) {
            LogUtil.error(e,logger);
            postStrToClient(RET_FAILED_LEFT+"邀请失败"+RET_FAILED_RIGHT,response);
            return ;
        }
    }

    @RequestMapping(value="/tranWoow")
    public void tranWoow(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {
        try {
            String workflow_id=map.get("workflow_id").toString();
            String nodeid=map.get("nodeid").toString();
            String cardUrlPeer=map.get("cardUrlPeer").toString();

            String cardUrl= CookieUtils.getMhCardUrlByCookie(request, null);
            if(cardUrl==null || cardUrl.length()==0){
//                这里返回值要与 门户其它模块的返回值对接  NcardServlet.RET_STATUS_NO_LOGIN   页面会引导客户重新调用微信接口认证
                postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作超时，请重试"),response);
                return;
            }
            if(WorkflowService.MHWorkFlowVerify(cardUrl,"isNodeExecUser",workflow_id,nodeid)){
                String peerRealName=WorkflowService.getMHRealNameByCardUrl(cardUrlPeer);
                ArrayList<String> alExec=new ArrayList<String>();

//          已经是参与者的，委托前要先删除
                String sql="delete from Workflow_Relation_Table where workflow_id='"+workflow_id+"' and cardUrl='"+cardUrlPeer+"' and role='1' ";
                LogUtil.debug(sql,logger);
                alExec.add(sql);
                sql="update Workflow_Relation_Table set cardUrl='"+cardUrlPeer+"', user_name='"+peerRealName+"' where cardUrl='"+cardUrl+"' and workflow_id='"+workflow_id+"' and  stepId='"+nodeid+"' and role='2'";
                LogUtil.debug(sql,logger);
//            有可能会被要求重置，当前执行结果
                alExec.add(sql);
                DAOTools.execBatchS(alExec,WorkflowService.DBIDRMHTmp);

                if(!WorkflowService.MHWorkFlowVerify(cardUrl,"isUserInWorkFlow",workflow_id,null)){
//                委托者，如果不是其它节点的执行者，需要被加为参与者
                    String RealName=WorkflowService.getMHRealNameByCardUrl(cardUrl);
                    sql="insert into Workflow_Relation_Table (workflow_id,cardUrl,user_name,role)values("+workflow_id+",'"+cardUrl+"','"+RealName+"','1')";
                    LogUtil.debug(sql, logger);
                    DAOTools.execUpdateS(sql, WorkflowService.DBIDRMHTmp);
                }
                postStrToClient(RET_SUCESS_LEFT+"委托成功"+RET_SUCESS_RIGHT,response);
            }else{
                postStrToClient(RET_FAILED_LEFT+"委托失败，您不是工作执行人"+RET_FAILED_RIGHT,response);
            }
        }catch (Exception e) {
            LogUtil.error(e,logger);
            postStrToClient(RET_FAILED_LEFT+"委托失败"+RET_FAILED_RIGHT,response);
            return ;
        }
    }

    @RequestMapping(value="/execWoow")
    public void execWoow(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {
        try {
            String workflow_id=map.get("workflow_id").toString();
            String nodeid=map.get("nodeid").toString();
            String execMsg=map.get("execMsg").toString();

            String cardUrl= CookieUtils.getMhCardUrlByCookie(request, null);
            if(cardUrl==null || cardUrl.length()==0){
//                这里返回值要与 门户其它模块的返回值对接  NcardServlet.RET_STATUS_NO_LOGIN   页面会引导客户重新调用微信接口认证
                postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作超时，请重试"),response);
                return;
            }

            if(WorkflowService.MHWorkFlowVerify(cardUrl,"isNodeExecUser",workflow_id,nodeid)){

                HashMap<String,String> params=new HashMap<String, String>();
                params.put("chatId","WOOW"+workflow_id);
                params.put("nodeid",nodeid);
                params.put("cardUrl",cardUrl);
                params.put("execMsg",execMsg);
                boolean isOk=CardBO.execWoow(params);
                if(isOk){

                    String sql="update Workflow_Relation_Table set staff_state_describe='"+execMsg+"' where  cardUrl='"+cardUrl+"' and workflow_id='"+workflow_id+"' and  stepId='"+nodeid+"' and role='2'";
                    DAOTools.execUpdateS(sql,WorkflowService.DBIDRMHTmp);

                    postStrToClient(RET_SUCESS_LEFT+"执行成功"+RET_SUCESS_RIGHT,response);
                }else{
                    postStrToClient(RET_FAILED_LEFT+"执行失败"+RET_FAILED_RIGHT,response);
                }
            }else{
                postStrToClient(RET_FAILED_LEFT+"执行失败，您不是工作执行人"+RET_FAILED_RIGHT,response);
            }
        }catch (Exception e) {
            LogUtil.error(e,logger);
            postStrToClient(RET_FAILED_LEFT+"执行失败"+RET_FAILED_RIGHT,response);
            return ;
        }
    }


    @RequestMapping(value="/leaveWoow")
    public void leaveWoow(@RequestParam HashMap map, HttpServletRequest request, HttpServletResponse response) {
        try {
            String woowId=map.get("woowId").toString();
            if(woowId.startsWith("WOOW")){
                woowId=woowId.substring(4);
            }
            String cardUrl= CookieUtils.getMhCardUrlByCookie(request, null);
            if(cardUrl==null || cardUrl.length()==0){
//                这里返回值要与 门户其它模块的返回值对接  NcardServlet.RET_STATUS_NO_LOGIN   页面会引导客户重新调用微信接口认证
                postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作超时，请重试"),response);
                return;
            }

            if(!WorkflowService.MHWorkFlowVerify(cardUrl,"isExecUser",woowId,null)){
                String sql="delete from  Workflow_Relation_Table where cardUrl='"+cardUrl+"' and workflow_id='"+woowId+"' ";
                LogUtil.debug(sql,logger);
                DAOTools.execUpdateS(sql,WorkflowService.DBIDRMHTmp);
                postStrToClient(RET_STATUS_SUCESS,response);
            }else{
                postStrToClient(RET_WOOW_LEVEL_IS_EXEC,response);
            }
        }catch (Exception e) {
            LogUtil.error(e,logger);
            postStrToClient(RET_STATUS_FAILED,response);
            return ;
        }
    }

    /***
     *
     * @param workflow_id !null workflow_id 为空需要替换
     * @param glDataStr 参与者执行者信息
     * @param cardUrl 操作人标识，用于验证权限
     * @param loginName !null 加自己为参与者（后续可以扩类型）
     * @param glDataList 其它需要添加的执行、参与 用户列表 如技能组分配等
     *
     * desc 向workflow中加参与者与执行者
     */
    public boolean glData2Work(String workflow_id,String glDataStr,String cardUrl,String loginName,HttpServletRequest request,List<Map> glDataList)throws Exception {

//        workflow_id不为空，需要赋值workflow_id
        if (workflow_id != null && workflow_id.length() > 0) {
            glDataStr = glDataStr.replaceAll("\"workflow_id\":\"", "\"workflow_id\":\"" + workflow_id);
        }

        List<Map> wflist = (List<Map>) JsonUtils.stringToJSON(glDataStr, List.class);
        if (wflist == null) {
            if(glDataList==null || glDataList.size()==0){
                LogUtil.error("Exception glData2Work no data", logger);
                return false;
            }
            wflist=new ArrayList<Map>();
        }

        /*合并其他参与、执行者列表*/
        if(glDataList!=null && glDataList.size()>0){
            wflist.addAll(glDataList);
        }

        if(workflow_id==null || workflow_id.length()==0){
            workflow_id= String.valueOf(wflist.get(0).get("workflow_id"));
        }

        if (loginName != null && loginName.length() > 0) {
            boolean needAdd=true;
            Map glUserTmp=null;
            for(int i=0;i<wflist.size();i++){
                glUserTmp=wflist.get(i);

                LogUtil.debug(glUserTmp.get("role")+" glUserTmp cardUrl:"+glUserTmp.get("cardUrl"),SKIP_Logger);


                if(glUserTmp.containsKey("cardUrl") && glUserTmp.get("cardUrl").equals(cardUrl)){
                    needAdd=false;
                    break;
                }
            }

            if(needAdd){
                Map wfmap = new HashMap();
                wfmap.put("workflow_id", workflow_id);
                wfmap.put("cardUrl", cardUrl);
                wfmap.put("user_name", loginName);
                wfmap.put("role", "1");
                wflist.add(wfmap);
            }
        }

//        String workflowIdTmp = String.valueOf(wflist.get(0).get("workflow_id"));
        if (workflow_id != null && workflow_id.length() > 0) {

            HashSet<String> nowFlowUser = new HashSet<String>();
            String sql = "select cardUrl from Workflow_Relation_Table where workflow_id=" + workflow_id;
            ArrayList<Object[]> al = DAOTools.execSelectS(sql, WorkflowService.DBIDRMHTmp);
            Object[] resTmp = null;

            String cardUrlTmp = null;
            for (int i = 0; i < al.size(); i++) {
                resTmp = al.get(i);
                if (resTmp[0] != null) {
                    cardUrlTmp = String.valueOf(resTmp[0]);
                    nowFlowUser.add(cardUrlTmp);
                }
            }

            LogUtil.debug("glData2Work sql:" + sql, logger);
            Map inviteUserMap = null;

            Iterator<Map> ii = wflist.iterator();
            while (ii.hasNext()) {
                inviteUserMap = ii.next();
                cardUrlTmp = String.valueOf(inviteUserMap.get("cardUrl"));
                if (nowFlowUser.contains(cardUrlTmp)) { //已经在工作流中的用户，跳过
                    ii.remove();
                }
            }

            if(wflist.size()>0){
                Map map = new HashMap();
                map.put("beanId", "commonInsertFilter");
                map.put("columnValues", JsonUtils.genUpdateDataReturnJsonStr(wflist));
                map.put("tableName", "Workflow_Relation_Table");
                map.put("nameSpace", "com.zinglabs.apps.appCommonCurd.pojo.common_insert");
                Map RelationMap = this.getService().common_insertOrUpdate(map, request);
                if (RelationMap.isEmpty() || Integer.parseInt(String.valueOf(RelationMap.get("total"))) == 0) {
                    return false;
                }
            }else{
                LogUtil.debug(" loop end "+wflist.size(),logger);
                return false;
            }
        }
        return true;
    }
	
	public AppCommonsCurdService getService(){
		return (AppCommonsCurdService)getBean("appCommonsCurdService");
	}

}
