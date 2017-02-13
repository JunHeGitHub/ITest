package com.zinglabs.util;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;

public class SSCUtil
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub

    }
    
    public static void initCompany(String company) {
        
        /**
         * 级别目前都插入为9 可根据实际情况调整
         */
        
        addSkill(company,"9");
        LogUtil.debug("addCompanyIVRInfo begin", SKIP_Logger);
        addCompanyIVRInfo(company);
//        addSkillGroup(company);
        
        
        execCmd(" RELOAD ALL");
    }
    
    public static void addCompanyIVRInfo(String company) {
        ArrayList<String> al=new ArrayList<String>();
//        一、插入每个客户不同的IM名称 以下三项中的IVR名称同一个客户均需一致
        String sql="INSERT INTO SA_IVR_FLOW (flow_name, flow_desc) VALUES ('im_"+company+"','"+company+"')";
        al.add(sql);
        
        
//        二、客户端显示每个客户的业务类型，和定义转人工前的skilltype变量内容。

//        【本条为入口节点，最后值为1，数据插入解释：im_客户名称为IVR流程的名称，EXP: 1 GOTO 产品咨询 为客户所拥有的业务类型，显示在客户端，如果多个则增加 EXP:1 GOTO XXXX 、EXP:3 GOTO XXXX 依次类推】

        sql="INSERT INTO SA_IVR_TREE (flow_name, node_name, action_name, action_param, action_result, action_timeout, flag_clear_timeout, flag_clear_input, flag_ignore_key, flag_clear_error, flag_node_log, flag_continue_play, display_x, display_y, flag_portal) VALUES ('im_"+company+"','欢迎','Casemodule','[input] EXP: 1 GOTO 产品咨询 DEFAULT: GOTO IVRExit_0','欢迎您使用英云在线客服系统--人工服务！请选择业务分类：',0,0,0,1,0,0,0,40,40,1)";  
        al.add(sql);
        
  
//        【插入数据解释：im_客户名称为IVR流程的名称；'产品咨询' 为模块节点名称，与第一条语句中的GOTO关联；cpzx$您的问题属于以下业务分类吗：中的cpzx为skilltype变量，需根据客户的不同而不变更，第二个IVR流程的medtxt中会根据此变量判断指定哪儿个技能组；如果有多个客户则需多增加此模块节点,与第一条语句中的GOTO关联，并且每次增加，要么x轴数值在第一次数值上翻3倍，要么y轴在第一次数值上翻3倍】        
        sql="INSERT INTO SA_IVR_TREE (flow_name, node_name, action_name, action_param, action_result, action_timeout, flag_clear_timeout, flag_clear_input, flag_ignore_key, flag_clear_error, flag_node_log, flag_continue_play, display_x, display_y, flag_portal) VALUES ('im_"+company+"','产品咨询','Casemodule','[input] DEFAULT: GOTO IVRExit_0','"+company+"_cpzx$您的问题属于以下业务分类吗：',0,0,0,1,0,0,0,160,100,0)";
        al.add(sql);
        
//        【插入数据解释：im_客户名称为IVR流程的名称，其他为默认，此模块为退出模块】
        sql="INSERT INTO SA_IVR_TREE (flow_name, node_name, action_name, action_param, action_result, action_timeout, flag_clear_timeout, flag_clear_input, flag_ignore_key, flag_clear_error, flag_node_log, flag_continue_play, display_x, display_y, flag_portal) VALUES ('im_"+company+"','IVRExit_0','IVRExit','','',0,0,0,0,0,0,0,545,65,0)";
        al.add(sql);
        
//        【插入数据解释：im_客户名称为IVR流程的名称，产品咨询 为IVR中模块节点，后面为IVR指向为默认，此模块为退出模块】
        sql="INSERT INTO SA_IVR_EVENT (flow_name, node_name, event_name, next_flow, next_node) VALUES ('im_"+company+"','产品咨询','ON_RUN_FAILED','im','IVRExit_0')";
        al.add(sql);
        
        
//        【插入数据解释：im_客户名称为IVR流程的名称，欢迎 为IVR中模块节点，后面为IVR指向为默认，此模块为退出模块】

        sql="INSERT INTO SA_IVR_EVENT (flow_name, node_name, event_name, next_flow, next_node) VALUES ('im_"+company+"','欢迎','ON_RUN_FAILED','im','IVRExit_0')";
        al.add(sql);

        
        
//        
//        三、客户端进行转人工操作medtxt 流程整理内容：
//        进入此流程第一通过skilltype判断客户的走向。所以skilltype对应的是每个用户的技能类型
//        而每增加一个客户，则需在此表中增加对应的IVR模块信息。如skilltype模块和技能组模块等。并且需要修改selsg模块中skilltype判断技能组的走向，需要在增加相关判断。
        
        String actionParamNow=null;
        
        
        sql="select action_param from SA_IVR_TREE where flow_name ='medtxt' and node_name ='selsg'";
        Object[] resTmp = null;
        ArrayList<Object[]> alTmp=DAOTools.execSelectS(sql, "z3000");
        for(int t=0;t<alTmp.size();t++){
            resTmp=alTmp.get(t);
            if(resTmp!=null && resTmp[0]!=null) {
                actionParamNow=resTmp[0].toString();
                actionParamNow=actionParamNow.replace("DEFAULT: GOTO IVRExit_0", "EXP: "+company+"_cpzx GOTO "+company+"_cpzx DEFAULT: GOTO IVRExit_0");
            }
            
            break;
        }
        
        sql="update SA_IVR_TREE set action_param ='"+actionParamNow+"' where flow_name ='medtxt' and node_name ='selsg'";
        LogUtil.debug("sql:"+sql, SKIP_Logger);
        
        al.add(sql);
        
        
//        【插入数据解释：定义一个技能组类型模块变量为skilltype，medtxt为IVR流程名称，产品咨询为节点名称，第二个'产品咨询'为skilltype变量内容，监控需要，此项需配置与技能组名称一致，其他默认，如果需要增加多个技能组，则此项需进行多增加一条，并且x轴数值不变，y轴在第一次或之前数值上翻3倍】
        sql="INSERT INTO SA_IVR_TREE (flow_name, node_name, action_name, action_param, action_result, action_timeout, flag_clear_timeout, flag_clear_input, flag_ignore_key, flag_clear_error, flag_node_log, flag_continue_play, display_x, display_y, flag_portal) VALUES ('medtxt','"+company+"_cpzx','AssignValue','"+company+"_cpzx','skilltype',0,0,0,1,0,0,0,200,50,0)";
        al.add(sql);

//        【插入数据解释：定义一个技能组模块，medtxt 为IVR流程名称，产品咨询_sg为节点名称，产品咨询为技能组名称，其他默认。如果需要增加多个技能组，则此项需进行多增加一条，并且x轴数值不变，y轴在第一次或之前数值上翻3倍】
        sql="INSERT INTO SA_IVR_TREE (flow_name, node_name, action_name, action_param, action_result, action_timeout, flag_clear_timeout, flag_clear_input, flag_ignore_key, flag_clear_error, flag_node_log, flag_continue_play, display_x, display_y, flag_portal) VALUES ('medtxt','"+company+"_cpzx_sg','SendToQueue','"+company+"_cpzx_sg','',0,0,0,1,0,0,0,350,50,0)";
        al.add(sql);

        
//        【插入数据解释：插入数据解释：定义一个技能组模块，medtxt 为IVR流程名称，产品咨询_music为节点名称，其他默认。如果需要增加多个技能组，则此项需进行多增加一条，并且x轴数值不变，y轴在第一次或之前数值上翻3倍】
        sql="INSERT INTO SA_IVR_TREE (flow_name, node_name, action_name, action_param, action_result, action_timeout, flag_clear_timeout, flag_clear_input, flag_ignore_key, flag_clear_error, flag_node_log, flag_continue_play, display_x, display_y, flag_portal) VALUES ('medtxt','"+company+"_cpzx_music','PlayMsg','/srn/resource/ivrsound/music.wav','',0,0,0,1,0,0,0,500,50,0)";
        al.add(sql);

//        【插入数据解释：插入数据解释：定义一个技能组模块，medtxt 为IVR流程名称，产品咨询_ExitQue为节点名称，其他默认。如果需要增加多个技能组，则此项需进行多增加一条，并且x轴数值不变，y轴在第一次或之前数值上翻3倍】
        sql="INSERT INTO SA_IVR_TREE (flow_name, node_name, action_name, action_param, action_result, action_timeout, flag_clear_timeout, flag_clear_input, flag_ignore_key, flag_clear_error, flag_node_log, flag_continue_play, display_x, display_y, flag_portal) VALUES ('medtxt','"+company+"_cpzx_ExitQue','ExitQue','','',0,0,0,1,0,0,0,650,50,0)";
        al.add(sql);
        
//        
////        【插入数据解释：插入数据解释：定义一个技能组模块，medtxt 为IVR流程名称，其他默认，因为前面的模块会指向最后退出节点，所以此项出流程名称，其他都不要修改】
//        sql="INSERT INTO SA_IVR_TREE (flow_name, node_name, action_name, action_param, action_result, action_timeout, flag_clear_timeout, flag_clear_input, flag_ignore_key, flag_clear_error, flag_node_log, flag_continue_play, display_x, display_y, flag_portal) VALUES ('medtxt','IVRExit_0','IVRExit','','',0,0,0,0,0,0,0,900,80,0)";
//        al.add(sql);
//        
        
//        sql="INSERT INTO SA_IVR_EVENT (flow_name, node_name, event_name, next_flow, next_node) VALUES ('medtxt','selsg','ON_RUN_FAILED','medtxt','IVRExit_0')";
//        
        
        sql="INSERT INTO SA_IVR_EVENT (flow_name, node_name, event_name, next_flow, next_node) VALUES ('medtxt','"+company+"_cpzx','ON_RUN_FAILED','medtxt','"+company+"_cpzx_sg')";
        al.add(sql);
        
        
        sql="INSERT INTO SA_IVR_EVENT (flow_name, node_name, event_name, next_flow, next_node) VALUES ('medtxt','"+company+"_cpzx','ON_RUN_SUCCESS','medtxt','"+company+"_cpzx_sg')";
        al.add(sql);
        
//        【插入数据解释：节点 产品咨询_music 进行指向，'medtxt',为 IVR流程名称； '产品咨询_music'为节点名称；并指向 medtxt流程名称，产品咨询_sg为技能组模块的名称】
        sql="INSERT INTO SA_IVR_EVENT (flow_name, node_name, event_name, next_flow, next_node) VALUES ('medtxt','"+company+"_cpzx_music','ON_DISCONNECT','medtxt','"+company+"_cpzx_sg')";
        al.add(sql);
        
        sql="INSERT INTO SA_IVR_EVENT (flow_name, node_name, event_name, next_flow, next_node) VALUES ('medtxt','"+company+"_cpzx_music','ON_RUN_TIMEOUT','medtxt','"+company+"_cpzx_sg')";
        al.add(sql);
        
        
        
//        【插入数据解释：节点 产品咨询_music 进行指向，'medtxt',为 IVR流程名称； '产品咨询_music'为节点名称；并指向 medtxt流程名称，产品咨询_ExitQue'为技能组退出模块的名称】
        sql="INSERT INTO SA_IVR_EVENT (flow_name, node_name, event_name, next_flow, next_node) VALUES ('medtxt','"+company+"_cpzx_music','ON_HOOK','medtxt','"+company+"_cpzx_ExitQue')";
        al.add(sql);
        
        sql="INSERT INTO SA_IVR_EVENT (flow_name, node_name, event_name, next_flow, next_node) VALUES ('medtxt','"+company+"_cpzx_music','ON_RUN_FAILED','medtxt','"+company+"_cpzx_ExitQue')";
        al.add(sql);
        
        
//        【插入数据解释：节点 产品咨询_music 进行指向，'medtxt',为 IVR流程名称； '产品咨询_music'为节点名称；并指向 medtxt流程名称，产品咨询_music'为模块自己的名称】
        sql="INSERT INTO SA_IVR_EVENT (flow_name, node_name, event_name, next_flow, next_node) VALUES ('medtxt','"+company+"_cpzx_music','ON_PLAYREC_END','medtxt','"+company+"_cpzx_music')";
        al.add(sql);
        
        
        sql="INSERT INTO SA_IVR_EVENT (flow_name, node_name, event_name, next_flow, next_node) VALUES ('medtxt','"+company+"_cpzx_music','ON_PLAYREC_ERROR','medtxt','"+company+"_cpzx_music')";
        al.add(sql);
        
//        【插入数据解释：节点 产品咨询_music 进行指向，'medtxt',为 IVR流程名称； '产品咨询_sg'为技能组节点名称；并指向 medtxt流程名称，产品咨询_music为语音播放模块的名称】
        sql="INSERT INTO SA_IVR_EVENT (flow_name, node_name, event_name, next_flow, next_node) VALUES ('medtxt','"+company+"_cpzx_sg','ON_RUN_SUCCESS','medtxt','"+company+"_cpzx_music')";
        al.add(sql);
        
        
//        【插入数据解释：节点 产品咨询_music 进行指向，'medtxt',为 IVR流程名称； '产品咨询_ExitQue'为节点名称；并指向 medtxt流程名称，'IVRExit_0'为退出系统模块的名称】
        sql="INSERT INTO SA_IVR_EVENT (flow_name, node_name, event_name, next_flow, next_node) VALUES ('medtxt','"+company+"_cpzx_ExitQue','ON_RUN_SUCCESS','medtxt','IVRExit_0')";
        al.add(sql);
        
        
        DAOTools.execBatchS(al, "z3000",false);
    }
    
    
    public static boolean addSSCAgent(String company,String phoneNum,String skLevel) {
        ArrayList<String> al=new ArrayList<String>();
        
        String sql="insert into SU_CALLCENTER_PHONE_USER(phone_number,person_name,phone_passwd,email_addr," +
        		"phone_perm,phone_duty,can_recv_email,can_recv_vmail,dont_disturb,permit_id,authcode_mode,if_sip,phygrpcode,max_serve_txt) "
            + "values('"+phoneNum+"','"+phoneNum+"','"+phoneNum+"','',0,0,0,0,0,'P00',0,0,'',0)";
        al.add(sql);
        
        sql="insert into SU_CALLCENTER_USER_SKILL(phone_number,skill_name,skill_level) values ('"+phoneNum+"','"+company+"',"+skLevel+")";
        al.add(sql);
        
        
        sql="insert into DA_SGINFO(phone_number,group_name) values ('"+phoneNum+"','"+company+"')";
        al.add(sql);
        
        DAOTools.execBatchS(al, "z3000",false);
        
        return true;
    }

    public static String getNewAutoPhoneNum(String loginName) {
        String phoneNum=null;
        String sql="select max(phoneNum) from Z_PhoneGen ";
        
        Object[] resTmp = null;
        ArrayList<Object[]> al=DAOTools.execSelectS(sql, "ZDesk");
        for(int t=0;t<al.size();t++){
            resTmp=al.get(t);
            if(resTmp!=null && resTmp[0]!=null) {
                phoneNum=resTmp[0].toString();
                break;
            }
        }
        
        
        if(phoneNum==null || phoneNum.length()>0) {
            sql="insert into Z_PhoneGen(phoneNum) values (31000)";
            phoneNum="31000";
            DAOTools.execUpdateS(sql, "ZDesk");
        }else {
            try
            {
                int phoneNumInt=Integer.parseInt(phoneNum);
                
                phoneNumInt+=1;
                sql="update Z_PhoneGen set phone_number="+phoneNumInt;
                DAOTools.execUpdateS(sql, "ZDesk"); 
                phoneNum=String.valueOf(phoneNumInt);
            }
            catch (Exception e)
            {
               LogUtil.error(e, SKIP_Logger);
               phoneNum=null;
//                sql="delete from Z_PhoneGen";
//                al.add(sql);
//                
//                sql="insert into Z_PhoneGen(phone_number) values (31000)";
//                phoneNum="31000";
//                al.add(sql);
            }
        }
        
        return phoneNum;
    }
    
    public static void addSkill(String skName,String skLevel) {
       ArrayList<String> al=new ArrayList<String>();
       String sql="insert into SU_CALLCENTER_SKILL(skill_name,skill_desc,max_level) values ('"+skName+"','"+skName+"',"+skLevel+")";
       al.add(sql);
//       DAOTools.execUpdateS(sql, "z3000");
       
//       sql="insert into SU_CALLCENTER_SKILL (skill_name,skill_desc,max_level) values ('"+skName+"','"+skName+"',9)";
//       al.add(sql);
       
       sql="insert into SU_INIT_SKILLGROUP(group_name,group_desc,skill_condition,acd_algorithm,skill_searchCondition,remark) values ('"+skName+"','"+skName+"','"+skName+">=1',1,'(skill_name= \\'"+skName+"\\' and skill_level >= 1 )','')";
       al.add(sql);
       
       DAOTools.execBatchS(al, "z3000",false);
       
    }
    
    public static void addSkillGroup(String skName) {
        String sql="insert into SU_INIT_SKILLGROUP(group_name,group_desc,skill_condition,acd_algorithm,skill_searchCondition,remark) values ('"+skName+"','"+skName+"','"+skName+">=1',1,'(skill_name= \\'"+skName+"\\' and skill_level >= 1 )','')";
        DAOTools.execUpdateS(sql, "z3000");
        
    }
    
//    insert into SU_INIT_SKILLGROUP(group_name,group_desc,skill_condition,acd_algorithm,skill_searchCondition,remark) values (?,?,?,?,?,?) 
    
    
    public static void addSkillUser(String skName,String phoneNum,String skLevel) {
        String sql="insert into SU_CALLCENTER_USER_SKILL(phone_number,skill_name,skill_level) values ('"+phoneNum+"','"+skName+"',"+skLevel+")";
        DAOTools.execUpdateS(sql, "z3000");
        
        
    }
    
    public static boolean execCmd(String cmd) {
        boolean ret = false;
        Socket socket = null;
//        String ip = Utility.getZingServerIP();
        if(sscCmdPort==null || sscCmdPort.length()==0) {
            if(!loadSSCInfo()) {return false;}  
        }
        
        String ip=ConfInfo.proerties.getProperty("dbip");
        
        if(ip==null || ip.length()==0) {
            return false;
        }
        
        try {
            socket = new Socket(ip, Integer.parseInt(sscCmdPort));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.print(cmd.trim());
            out.flush();
            out.close();
            ret = true;
            LogUtil.debug("ssc cmd SUCCESS:" + cmd,SKIP_Logger);
        } catch (Exception e) {
            LogUtil.error("ssc cmd Exception 1: "+cmd, SKIP_Logger);
            LogUtil.error(e, SKIP_Logger);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    LogUtil.error("ssc cmd Exception: "+cmd, SKIP_Logger);
                    LogUtil.error(e, SKIP_Logger);
                }
            }
        }
        return ret;
    }
    
    
    public static boolean loadSSCInfo() {
        boolean ret=false;
        Object[] resTmp = null;
        String sql="SELECT service_port FROM SU_2004_SERVICE_PORT WHERE service_name='SSC_COMMAND_PORT'";
        ArrayList<Object[]> al=DAOTools.execSelectS(sql, "z3000");
        for(int t=0;t<al.size();t++){
            resTmp=al.get(t);
            sscCmdPort=resTmp[0].toString();
            ret=true;
            break;
        }
        return ret;
    }
    
    public static String sscCmdPort;
    
    public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
}
