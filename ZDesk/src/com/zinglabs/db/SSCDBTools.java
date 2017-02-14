package com.zinglabs.db;


import com.qp.tools.impl.KeyedObPoolFactory;
import com.qp.tools.impl.KeyedObjPool;
import com.zinglabs.base.Interface.CallBackInterface;
import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.log.LogUtil;
import com.zinglabs.tools.StringUtil;
import com.zinglabs.tools.Utility;
import com.zinglabs.util.DateUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;





/*原DAOTools中一些业务相关的功能，为方便通用包划分。拆分到这里*/
public class SSCDBTools extends BaseAbs  {
	public static  Logger logger = LoggerFactory.getLogger("ZDesk");

    public SSCDBTools(){
//      init(SKIP_Logger,false);
//      st=new StringUtil();
//      ConfInfo conf=new ConfInfo();
//
////        ConPool poolUtil=new ConPool();
//      KeyedObPoolFactory fac=new KeyedObPoolFactory(conf);
//      pool=fac.createPool();
    }


    public static ArrayList<Ag100Info> getAg100IPs() {
        Connection conn=null;
        ArrayList ag100 = null;
        Statement stmt = null;
        ResultSet rs = null;
        String ip = null;
        String sql = "select a.srn_ip from SU_INIT_SRN a,DA_SRN_STATUS b,DA_SYSINFO c where a.srn_name =b.srn_name " +
                "and a.srn_ip=c.ipaddr and c.hd>2048 and b.srn_status='online' order by a.srn_name";
        try {
            conn=DAOTools.getConnectionOutS("z3000");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ag100 = new ArrayList<Ag100Info>();
            while (rs.next()) {
                Ag100Info aTmp=new Ag100Info();
                aTmp.ip=rs.getString("srn_ip");
                ag100.add(aTmp);
            }

        } catch (SQLException e) {
            LogUtil.error(e,SKIP_Logger);
        }finally{

            try
            {
                if(rs!=null) {
                    rs.close();
                }
            }
            catch (Exception e2)
            {
            }

            try
            {
                if(stmt!=null) {
                    stmt.close();
                }
            }
            catch (Exception e2)
            {

            }

            rs=null;
            stmt=null;

            DAOTools.releaseConnectionOutS("z3000", conn);
        }
        return ag100;
    }


    public static HashMap<String,String> loadAgentInfo(String phoneNum){
        HashMap<String, String> ret=new HashMap<String, String>();
        String sql=" select loginName,alias5 as phyName,name  from suSecurityUser  where phone_number='"+phoneNum+"' ";
        Connection conn=null;
        Statement stmt = null;
        ResultSet rs = null;
        String loginName="";
        String phyName="";
        String name="";
        try {

            conn=DAOTools.getConnectionOutS("ZDesk");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                loginName=rs.getString("loginName");
                phyName=rs.getString("phyName");
                name=rs.getString("name");
                ret.put("loginName", loginName);
                ret.put("phyName", phyName);
                ret.put("name", name);
            }

        } catch (SQLException e) {
            LogUtil.error(e,SKIP_Logger);
        }finally{

            try
            {
                if(rs!=null) {
                    rs.close();
                }
            }
            catch (Exception e2)
            {

            }

            try
            {
                if(stmt!=null) {
                    stmt.close();
                }
            }
            catch (Exception e2)
            {
            }

            rs=null;
            stmt=null;
            DAOTools.releaseConnectionOutS("ZQC", conn);
        }

        return ret;
    }

    public static ArrayList<OrsInfo> getOrsIPs(){
        Connection conn=null;
        ArrayList<OrsInfo> ors = null;
        Statement stmt = null;
        ResultSet rs = null;
//      String sql = "select distinct b.ors_ip from ORS_STATUS a join ORS_AG100_MAP b on a.ors_ip = b.ors_ip where a.uptime >= (sysdate()-INTERVAL 120 SECOND)";
        String sql ="select a.ors_ip,uptime,dealtime from ORS_AG100_MAP a left join ORS_STATUS b on a.ors_ip=b.ors_ip group by a.ors_ip";
        try {
            conn=DAOTools.getConnectionOutS("z3000");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            Timestamp upTime=null ,dealTime=null;
            ors = new ArrayList<OrsInfo>();
            String ip =null;
            while (rs.next()) {
                OrsInfo oTmp=new OrsInfo();
                oTmp.ip=rs.getString("ors_ip");
                oTmp.upTime=rs.getTimestamp("uptime");
                oTmp.dealTime=rs.getTimestamp("dealtime");
                ors.add(oTmp);
            }
        } catch (SQLException e) {
            LogUtil.error(e, SKIP_Logger);
        }finally{
            try
            {
                if(rs!=null) {
                    rs.close();
                }
            }
            catch (Exception e2)
            {

            }

            try
            {
                if(stmt!=null) {
                    stmt.close();
                }
            }
            catch (Exception e2)
            {
            }

            rs=null;
            stmt=null;
            DAOTools.releaseConnectionOutS("z3000", conn);
        }
        return ors;
    }


    public static void loadRecordSgInfo(RecordData rd) {
        HashMap<String, String> businessGroup = ConfInfo.dynamicFields.get("businessGroup");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp nowEnd = null, nowBegin = null, nowTime = null;
        boolean hasRecord = false;
        String sessionId = null;
        String sgName = null, sgNameTmp = null;
        String agentId = null, agentPhy = null, agentName = null,agentLevel=null;
        ArrayList<Object[]> al = null;
        Object[] resTmp = null;
        AgentInfo agent = null;
        String sql = null;
        try {
            nowEnd = new Timestamp(format.parse(rd.end_time).getTime() + 900000L);
            nowBegin = new Timestamp(format.parse(rd.begin_time).getTime() - 900000L);
        } catch (ParseException e1) {
            LogUtil.error(e1, SKIP_Logger);
        }
    /*  String sql="select session_id,begin_time,end_time from LOG_SESSION_ID USE INDEX(begin_time)  where begin_time<='"+rd.end_time+"' and " +
                " begin_time>='"+format.format(nowBegin)+"' and caller_number like '%"+rd.caller_number+"%'  " +
                        "order by begin_time desc ";
        LogUtil.debug(sql,SKIP_Logger);*/
        try {
            if (/*rd.call_number.equals(rd.phone_number) || */(rd.caller_number==null || rd.caller_number.length()==0)
                    &&rd.call_number!=null && rd.call_number.length()>0) {
                // sql="select * from LOG_COLINE_OUTBOUND_CALL_TRAVEL where
                // out_called_number like '%"+rd.caller_number+"%' ";
                sql = "select session_id,begin_time,end_time from LOG_SESSION_ID USE INDEX(begin_time)  where begin_time<='"
                        + format.format(nowEnd)
                        + "' and "
                        + " begin_time>='"
                        + format.format(nowBegin)
                        + "' and caller_number = '"
                        + rd.phone_number
                        + "'   and  called_number like '%"+rd.call_number+"%' " + "order by begin_time desc ";
                al = DAOTools.execSelectS(sql, "z3000_ext");
                for (int ii = 0; ii < al.size(); ii++) {
                    resTmp = al.get(ii);
                    if (resTmp[0] != null) {
                        sessionId = String.valueOf(resTmp[0]);
                        if (sessionId != null && sessionId.length() > 0) {
                            hasRecord = true;
                            break;
                        }
                    }
                }

                LogUtil.debug("size:" + al.size() + " inline sql:" + sql, SKIP_Logger);

                if (hasRecord && sessionId != null) {
                    sql = "select * from LOG_COLINE_OUTBOUND_CALL_TRAVEL  where  peer_session_id ='"
                            + sessionId + "' ";
                    al = DAOTools.execSelectS(sql, "z3000_ext");
                    if (al.size() > 0) {
                        sgNameTmp = "外呼";
                    }else{
                        LogUtil.debug("vvxc hasRecord and sessionId al.size=0 " + sessionId, SKIP_Logger);
                    }
                } else {
                    sgNameTmp = "内线";
                }
            } else {
                sql = "select session_id,begin_time,end_time from LOG_SESSION_ID USE INDEX(begin_time)  where begin_time<='"
                        + format.format(nowEnd)
                        + "' and "
                        + " begin_time>='"
                        + format.format(nowBegin)
                        + "' and caller_number like '%"
                        + rd.caller_number
                        + "%'  " + "order by begin_time desc ";

                al = DAOTools.execSelectS(sql, "z3000_ext");
                for (int ii = 0; ii < al.size(); ii++) {
                    resTmp = al.get(ii);
                    if (resTmp[0] != null) {
                        sessionId = String.valueOf(resTmp[0]);
                        if (sessionId != null && sessionId.length() > 0) {
                            hasRecord = true;
                            break;
                        }
                    }
                }
                LogUtil.debug("size:" + al.size() + " sql:" + sql, SKIP_Logger);

                al.clear();
                if (hasRecord && sessionId != null) {
                    sql = "select  sg_name,enter_time, quit_time,"
                            + " quit_reason from LOG_SKILLGROUP_WORK where request_sg_session_id ='"
                            + sessionId + "' "
                            + "order by enter_time,quit_time ,generate_time ";
                    al = DAOTools.execSelectS(sql, "z3000_ext");
                    LogUtil.debug("skill size:" + al.size() + " sql:" + sql,SKIP_Logger);
                    for (int ii = 0; ii < al.size(); ii++) {
                        resTmp = al.get(ii);
                        if (resTmp[0] != null) {
                            sgName = String.valueOf(resTmp[0]);
                            if (!businessGroup.containsKey(sgName)) {
                                LogUtil.error("exception key :" + sgName,SKIP_Logger);
                                sgName = null;
                                continue;
                            }

                            sgNameTmp = businessGroup.get(sgName);

                            // if(sgNameTmp==null){
                            // sgNameTmp=Utility.SHSGNameMap.get(sgName);
                            // }
                            break;

                        } else {
                            LogUtil.error("exception null sgName sessionId:"+ sessionId, SKIP_Logger);
                        }
                    }
                }

                al.clear();

                if (sgNameTmp == null && sessionId != null) {
                    LogUtil.error("vvxc sessionId is not null whit null sgNameTmp :"+ rd.caller_number + " sessionid is null :"+ sessionId, SKIP_Logger);
                    // // sql="select * from LOG_COLINE_OUTBOUND_CALL_TRAVEL
                    // where out_called_number like '%"+rd.caller_number+"%' ";
                    // sql="select * from LOG_COLINE_OUTBOUND_CALL_TRAVEL where
                    // self_session_id ='"+sessionId+"' ";
                    // al=execSelectS(sql, "z3000_ext");
                    // if(al.size()>0){
                    // sgNameTmp="外呼";
                    // }
                } else if (rd.caller_number != null) {
                    LogUtil.error("vvxc is null caller is :"+ rd.caller_number + " sessionid is :"+ sessionId, SKIP_Logger);
                } else if (sgNameTmp == null) {
                    sgNameTmp = "内线";
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, SKIP_Logger);
            LogUtil.error("excption in loadRecordSgInfo", SKIP_Logger);
        }


        try {
            if (rd.phone_number != null && AgentInfo.SKIP_agentMap.containsKey(rd.phone_number)) {
                agent = AgentInfo.SKIP_agentMap.get(rd.phone_number);
            } else if (rd.call_number != null && AgentInfo.SKIP_agentMap.containsKey(rd.call_number)) {
                agent = AgentInfo.SKIP_agentMap.get(rd.call_number);
            }

            if (agent != null) {
                agentId = agent.AgentInfo$Alias1;
                agentPhy = agent.AgentInfo$Alias3;
                agentName = agent.AgentInfo$Name;
                agentLevel=agent.AgentInfo$AgentLevel;

            }
            LogUtil.debug("rd.phone_number:"+rd.phone_number+" agentId:"+agentId+" agentPhy:"+agentPhy+" agentName:"+agentName+" agentLevel:"+agentLevel ,SKIP_Logger);
            rd.associated_sg = sgNameTmp;
            rd.business_type = sgNameTmp;
            rd.alias1 = agentId;
            rd.alias2 = agentPhy;
            rd.alias3 = agentName;
            rd.agentLevel=agentLevel;
        } catch (Exception e) {
            LogUtil.error(e, SKIP_Logger);
        }

    }


    /**
     * 验证用户登录
     * @param user　用户名
     * @param pwd   密码
     * @param ip　IP地址
     * @param clientId　ext clientId
     * @return
     *
     * 先放这里，后续调整登陆与cookie对接时，统一处理
     *
     */
    public static String getLoginCheck(String user,String pwd,String ip,String clientId){
//      String sql="select * from `suSecurityUser` where `loginName`=? and `pwd`=?";
        UserInfo2 user_=null;
        String sql="select a.*,b.name as orgCode from `suSecurityUser` a left join `suSecurityPermission` b on  a.organizationCode=b.code where `loginName`=? and `pwd`=? and `typeName`='organization'";
        boolean rebl=true;
        String remgs="登录成功。";
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        String userName=null,userRole=null;
        String phyName="";
        String phoneNum="";
        String phoneNumDES="";
        String companyId="";
        String companyName="";
        String departmentId="";
        String departmentName="";
        String showMsg = "false";
        user_=UserInfo2.getUser(user);
        if(user_!=null){
           user_.releaseAll();
           return "{'success':false,'mgs':'登录失败，用户已登录。','code':'hasLogin'}";
        }
        try{

            con= DAOTools.getConnectionOutS(ConfInfo.confMapDBConf.get("securityDBId"));
            ps=con.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pwd);
            rs=ps.executeQuery();
            boolean hasUser=false;
            if(rs.next()){
            	hasUser=true;
            }else{
            	sql="select * from `suSecurityUserAsManyOrgView`  where `loginName`=? and `pwd`=?";
            	ps=con.prepareStatement(sql);
                ps.setString(1, user);
                ps.setString(2, pwd);
                rs=ps.executeQuery();
                if(rs.next()){
	                hasUser=true;
                }
            }
            if(hasUser){
            	 try
                 {
                     user_=new UserInfo2();
                     userName=rs.getString("name")==null?"":rs.getString("name");
                     user_.loginName2=user;
                     user_.userSetFieldValue("loginName2", user);
                     user_.userSetFieldValue("userName", userName);
                     userRole=rs.getString("roleName")==null?"":rs.getString("roleName");
                     user_.userSetFieldValue("userRole", userRole);
                     user_.userSetFieldValue("userPassword", pwd);
                     user_.userSetFieldValue("loginTime", String.valueOf(System.currentTimeMillis()));
                     user_.userSetFieldValue("clientId", clientId);
                     user_.userSetFieldValue("userIP", ip);
                     user_.userSetFieldValue("organizationCode", rs.getString("organizationCode")==null?"":rs.getString("organizationCode"));
                     phoneNum=rs.getString("phone_number");
                     user_.userSetFieldValue("phoneNumber", phoneNum);

                     companyId=rs.getString("companyId")==null?"":rs.getString("companyId");
                     user_.userSetFieldValue("companyId", companyId);

                     companyName=rs.getString("companyName")==null?"":rs.getString("companyName");
                     user_.userSetFieldValue("companyName", companyName);

//                   departmentId=rs.getString("departmentId")==null?"":rs.getString("departmentId");
                     user_.userSetFieldValue("departmentId", departmentId);

//                   departmentName=rs.getString("departmentName")==null?"":rs.getString("departmentId");
                     user_.userSetFieldValue("departmentName", departmentId);


//                 String phoneNumTX=new String(phoneNum.getBytes("gbk"));
//                 DES desTT=new DES();
                     CryptoTools desTT= new CryptoTools();
                     phoneNumDES=desTT.encode(phoneNum);
//                 LogUtil.debug(desTT.getEncCode(phoneNum.getBytes())+" "+desTT.getEncString(phoneNumTX)+" "+phoneNumDES+"getDesString : " + desTT.getDesString(phoneNumDES),SKIP_Logger );
                     user_.userSetFieldValue("userAlias1", rs.getString("alias1")==null?"":rs.getString("alias1"));
                     user_.userSetFieldValue("userAlias2", rs.getString("alias2")==null?"":rs.getString("alias2"));
                     user_.userSetFieldValue("userAlias3", rs.getString("alias3")==null?"":rs.getString("alias3"));
                     user_.userSetFieldValue("userAlias4", rs.getString("alias4")==null?"":rs.getString("alias4"));
                     user_.userSetFieldValue("userAlias5", rs.getString("alias5")==null?"":rs.getString("alias5"));
                     user_.userSetFieldValue("userId", ""+rs.getLong("id"));

//                 phyName=rs.getString("phyName")==null?"":rs.getString("phyName");

                     phyName=rs.getString("alias5")==null?"":rs.getString("alias5");
                     user_.userSetFieldValue("phyName",phyName);

                     user_.tableFilterMap=new HashMap<String, DataFilter>();

                     user_.userResMapx=loadUserResMap(user_.loginName2);
//                 user_.userResMapx=new HashMap<String, String>();
//                 if(ZKMSymbol!=null){
                     if ("true".equals(ConfInfo.confMapDBConf.get("isCluster"))) {
                         String keyTT=null,valueTT=null;
                          for(Iterator iM = user_.userResMapx.entrySet().iterator(); iM.hasNext();) {
                                 Map.Entry eM = (Map.Entry)iM.next();
                                 keyTT=(String)eM.getKey();
                                 valueTT=(String)eM.getValue();
//                             ClassInfo info =(ClassInfo)eClass.getValue();
                                 user_.userSetFieldValue(keyTT, valueTT);
                          }
                         
//                     user_.userSetFieldValue("ZKMAdminSymbol", ZKMSymbol);
                         // user_.userResMap=loadUserResMap2(user_.loginName2);
                     } else {
//                     user_.userResMapx.put("ZKMAdminSymbol", ZKMSymbol);
                     }
//                 }
                     String check = ConfInfo.confMapDBConf.get("is_check_expiry_date");
                     if(check!=null && "true".indexOf(check) == 0){
                         sql = "select * from `suZCloudUserMessage` where id=?";
                         ps=con.prepareStatement(sql);
                         ps.setString(1, companyId);
                         rs=ps.executeQuery();
                         int i = 0;
                         // 判断用户是否过期
                         while(rs.next()){
                             i++;
                             Date date = rs.getDate("endDate");
                             if(date != null){
                                 Calendar calendar = Calendar.getInstance();
                                 calendar.setTime(date);
                                 
                                 Date dateNow = new Date();
                                 Calendar calendar2 = Calendar.getInstance();
                                 calendar2.setTime(dateNow);
                                 int compareValue = calendar2.compareTo(calendar);
                                 if(compareValue > 0){
                                     showMsg = "true";
                                     remgs="用户已到期，请续费。";
                                     rebl=false;
                                 }
                             }else{
                                 showMsg = "true";
                                 remgs="到期时间异常。";
                                 rebl=false;
                             }
                             
                         }
                         if(i<1){
                             remgs="用户公司id不存在，登录失败。";
                             LogUtil.error("login check : " + remgs,SKIP_Logger );
                             rebl=false;
                         }
                     }
                     if(rebl){
                         if( "".equals(userRole)){
                             remgs="获取用户角色为空，登录失败。";
                             LogUtil.error("login check : " + remgs,SKIP_Logger );
                             rebl=false;
                         }else{
                             
                             if(!"true".equals(ConfInfo.confMapDBConf.get("isCluster"))){
                                 UserInfo2.allUserInfo.put(user, user_);
                                 UserInfo2.allClientIdUserInfo.put(clientId, user_);
                             }
                             
                             String sqlLog="insert into Z_DBModifyLog (optName,optTable,optTableName,optUser,ip)VALUES('登录','suSecurityUser','登入登出记录','"+userName+"','"+ip+"')";
                             ArrayList<String> batch=new ArrayList<String>();
                             batch.add(sqlLog);
                             DAOTools.execBatchS(batch, "ZDesk",false);
                         }
                     }
//                 user_.textSymbolSecurityList=loadUserResList(user_);
                 }
                 finally
                 {
                     if(user_!=null){
                         try {
                             user_.releaseAll();
                         } catch (Exception e) {
                             LogUtil.error(e, SKIP_Logger);
                         }
                     }
                     user_=null;
                 }
            }else{
                remgs="用户名或密码错误，登录失败。";
                LogUtil.error("login check : " + remgs,SKIP_Logger );
                rebl=false;
            }
        }catch(Exception x){
            LogUtil.error(x, SKIP_Logger);
            x.printStackTrace();
            remgs="数据库访问失败。";
            rebl=false;
        }finally{
            try {
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
                if(user_!=null){
                    try {
                        user_.releaseAll();
                    } catch (Exception e) {
                        LogUtil.error(e, SKIP_Logger);
                    }
                }
                if(con!=null){
                    DAOTools.releaseConnectionOutS(ConfInfo.confMapDBConf.get("securityDBId"), con);
//                    con.close();
                }
                
            } catch (SQLException e) {
                LogUtil.error(e, SKIP_Logger);
                rs=null;
                ps=null;
                con=null;
            }
            
        }
        String returnMsg = "{'success':" + rebl + ",'mgs':'"+ remgs +"','userName':'"+ userName +"','phyName':'"+ phyName +"','phoneNum':'"+ phoneNum +"','phoneNumDES':'"+ phoneNumDES +"','clientId':'"+ clientId +"','userRole':'"+userRole+"','timestamp':'"+System.currentTimeMillis()+"'" + ",'showMsg':'"+ showMsg +"'}";
        return returnMsg;
    }

    
    public static HashMap<String, String> loadUserResMap(String loginName) {
        HashMap<String, String> resMapTmp=new HashMap<String, String>();
        String ret=null;
//      HashMap<String, String> ret=new HashMap<String, String>();
        
//      select `modleId` from `suSecurityRoleMapPermission`  where `typeName`='textSymbol' and `roleName`=?
        String sql="select modleId from suSecurityRoleMapPermission a, suSecurityUser b where a.roleName=b.roleName  and `typeName`='textSymbol' and b.`loginName`='"+loginName+"'";
//      String sql="select modleId from suSecurityRoleMapPermission a, suSecurityUser b where a.roleName=b.roleName and a.modleId='ZKMAdminSymbol' and b.`loginName`='"+loginName+"'";
        Connection con=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        String zkmAdmin=null;
        try {
            con= DAOTools.getConnectionOutS(ConfInfo.confMapDBConf.get("securityDBId"));
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){
                zkmAdmin=rs.getString("modleId");
                resMapTmp.put(zkmAdmin, zkmAdmin);
//              if(zkmAdmin.equals("ZKMAdminSymbol")){
//                  ret=zkmAdmin;
////                    ret.put("ZKMAdminSymbol", "ZKMAdminSymbol");
//              }
            }
        } catch (Exception e) {
            LogUtil.error(e, SKIP_Logger);
        }finally{
            try {
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
                
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                LogUtil.error(e, SKIP_Logger);
            }
            
            if(con!=null){
                DAOTools.releaseConnectionOutS(ConfInfo.confMapDBConf.get("securityDBId"), con);
//                con.close();
            }
        }
        return resMapTmp;
    }
    
    public static List<String> loadUserResList(UserInfo2 user) throws Exception{
        List<String> list=new ArrayList();
        String loginName=user.loginName2;
        String userRole=user.userGetFieldValue("userRole");
        if(user!=null && !"_guest".equals(loginName) && userRole!=null && userRole.length()>0){
            String roleName=userRole;
            String sql="select `modleId` from `suSecurityRoleMapPermission`  where `typeName`='textSymbol' and `roleName`=?";
            String[] params={roleName};
            List<Map> dlist= DAOTools.queryMap(sql, params, ConfInfo.confMapDBConf.get("securityDBId"));
            if(dlist!=null && dlist.size()>0){
                for(int i=0;i<dlist.size();i++){
                    Map<String,String> map=dlist.get(i);
                    String mid=map.get("modleId");
                    if(mid!=null && mid.length()>0){
                        list.add(mid);
                    }
                }
            }
        }
        return list;
    }
    
    public static List<Map> loadHasSecurityUserList(String typeName,String securityCode) throws Exception{
        List<Map> list=new ArrayList();
        if(typeName!=null && typeName.length()>0 && securityCode!=null && securityCode.length()>0){
            String[] params={typeName,securityCode};
            String sql="select roleName,typeName,`modleId` from `suSecurityRoleMapPermission`  where `typeName`=? and `modleId`=?";
            //System.out.println(sql);
            List<Map> rlist= DAOTools.queryMap(sql, params, ConfInfo.confMapDBConf.get("securityDBId"));
            if(rlist!=null && rlist.size()>0){
                List plist=new ArrayList();
                String where="where 1=1 ";
                for(int i=0;i<rlist.size();i++){
                    Map dmap=rlist.get(i);
                    String rname=dmap.get("roleName")==null?"":(String)dmap.get("roleName");
                    plist.add(rname);
                    if(i==0){
                        where+=" and (roleName=?";
                    }else if(i+1 >=rlist.size()){
                        where+=" or roleName=?)";
                    }else{
                        where+=" or roleName=? ";
                    }
                }
                sql="select id,loginName,name,roleName from suSecurityUser " + where ;
                //System.out.println(sql);
                list= DAOTools.queryMap(sql, plist, ConfInfo.confMapDBConf.get("securityDBId"));
            }
        }
        return list;
    }

    public static void defultValue(Object o)throws Exception{
//      ArrayList<ClassInfo> al = classInfoMap.get(o.getClass().getName());
        HashMap<String,ClassInfo> classMap=classInfoMap.get(o.getClass().getName());
        if(classMap==null)return;
        for(Iterator iClass = classMap.entrySet().iterator(); iClass.hasNext();) {
            java.util.Map.Entry eClass = (java.util.Map.Entry)iClass.next();
            ClassInfo info =(ClassInfo)eClass.getValue();

//          sDebug("-----------sDebug------------"+info.fieldName, SKIP_Logger);
//      for (int i = 0; i < al.size(); i++) {
//          ClassInfo info = al.get(i);
            if (info.valueType.equals("java.lang.String")){
                String tmp=null;
                info.setValue(o, tmp);
            } else if (info.valueType.equals("int")) {
                info.setValue(o, 0);
            } else if (info.valueType.equals("long")) {
                info.setValue(o, 0L);
            } else if (info.valueType.equals("java.sql.Timestamp")) {
                Timestamp tmp=null;
                info.setValue(o, tmp);
            } else if (info.valueType.equals("double")) {
                info.setValue(o, 0.0);
            } else if(info.valueType.equals("boolean")){
                info.setValue(o, false);
            } else if(info.valueType.equals("java.math.BigDecimal")){
                info.setValue(o, new BigDecimal(0));
            }else {
                Object tmp=Class.forName( info.valueType).newInstance();
                info.setValue(o, tmp);
            }
        }
    }

    /**
     *
     * @param target
     * @param self
     * @return
     * @throws Exception
     * @desc
     *  careful use recursion can  infinitude loop
     *  if any map type String key only
     * self and target must be init so do not process null pointer Exception
     *
     */

    public Object AddObjData(Object target,Object self) throws Exception{
        if(target==null||self==null||!self.getClass().getName().equals(target.getClass().getName()))
            return null;
        String tmp=null;
//      ArrayList<ClassInfo> al=classInfoMap.get(target.getClass().getName());
        HashMap<String,ClassInfo> classMap=classInfoMap.get(target.getClass().getName());
        for(Iterator iClass = classMap.entrySet().iterator(); iClass.hasNext();) {
            java.util.Map.Entry eClass = (java.util.Map.Entry)iClass.next();
            ClassInfo info =(ClassInfo)eClass.getValue();
//      for(int i=0;i<al.size();i++){
//          ClassInfo info=al.get(i);
            if(info.valueType.equals("java.lang.String")){
                tmp=String.valueOf(info.getValue(target));
                info.setValue(self, tmp);
            }else if(info.valueType.equals("int")){
                int selfValue;
                selfValue = Integer.parseInt(String.valueOf(info.getValue(self)));
                int tarValue = Integer.parseInt(String.valueOf(info.getValue(target)));
                info.setValue(self, selfValue + tarValue);
            }else if(info.valueType.equals("long")){
                long selfValue;
                selfValue = Long.parseLong(String.valueOf(info.getValue(self)));
                long tarValue = Long.parseLong(String.valueOf(info.getValue(target)));
                info.setValue(self, selfValue + tarValue);
            }else if(info.valueType.equals("java.sql.Timestamp")){
                Timestamp tw=(Timestamp)info.getValue(self);
                Timestamp twTar=(Timestamp)info.getValue(target);
//              if(tw==null || twTar==null || tw.getTime()!=twTar.getTime())
//              throw new Exception("AddObjData TimeWrap  obj not equal target");
            }else if(info.valueType.equals("double")){
//              java.sql.Timestamp timeValue=res.getTimestamp(dbName);
//              info.setMe.invoke(newOne, timeValue);
            }else if(info.valueType.equals("java.util.concurrent.ConcurrentHashMap")){
                ConcurrentHashMap targetMap=(ConcurrentHashMap)info.getValue(target);
                ConcurrentHashMap selfMap=(ConcurrentHashMap)info.getValue(self);
                Object oSelf=null;
                Object obj =null;
                for(Iterator iM = targetMap.entrySet().iterator(); iM.hasNext();) {
                    java.util.Map.Entry eM = (java.util.Map.Entry)iM.next();
                    obj =eM.getValue();
                    String key=(String)eM.getKey();
                    if(selfMap.containsKey(key)){
                        oSelf=selfMap.get(key);
                        AddObjData(obj,oSelf);
                    }else{
//                          Class.forName( info.valueType).newInstance();
                        oSelf=obj.getClass().newInstance();
                        AddObjData(obj,oSelf);
                        selfMap.put(key, oSelf);
                    }
                }
            }else{
//              info.setMe.invoke(newOne, res.getObject(dbName));
            }
        }
        return self;
    }

    /**
     * @return a zero length array for none method
     */
    public static HashMap<String,ClassInfo> getClassInfo(Class cl){
        HashMap<String,ClassInfo> classMap=new HashMap<String, ClassInfo>();
//      ArrayList <ClassInfo> al=new ArrayList <ClassInfo>(0);
        Field[] fie=cl.getDeclaredFields();
//      StringUtil sTools=new StringUtil();
        for(int i=0;i<fie.length;i++){
//          String fieldName=sTools.upFirst(fie[i].getName());

            String fieldName=fie[i].getName();
            if(fieldName.startsWith("SKIP_")||fieldName.startsWith("This")||fieldName.startsWith("SerialVersionUID") ){
                continue;
            }
//          sDebug(fieldName+"----------"+cl.getName(), SKIP_Logger);
            ClassInfo info=new ClassInfo();
            try {
//              info.setMe=cl.getMethod("set"+fieldName, new Class[] {fie[i].getType()});
//              info.getMe=cl.getMethod("get"+fieldName, (Class[])null);
//              sDebug(fieldName+"---"+fie[i].getType().getName()+"-------"+cl.getName(), SKIP_Logger);
                info.fd=fie[i];
                info.valueType=fie[i].getType().getName();
                info.fieldName=fie[i].getName();
            } catch (Exception e) {
                LogUtil.error("error in getClassInfo",SKIP_Logger);
                LogUtil.error(e,SKIP_Logger);
            }
            classMap.put(info.fieldName, info);


//          al.add(info);
        }
        return classMap;
    }

    public static boolean hasInit=false;


    //  private static Map<String, ArrayList<ClassInfo>> classInfoMap=new HashMap<String, ArrayList<ClassInfo>>();
    public static Map<String, HashMap<String,ClassInfo>> classInfoMap=new HashMap<String, HashMap<String,ClassInfo>>();


    public synchronized static void initSSCDBInfo(){
        if(hasInit){
            return;
        }
        hasInit=true;
//      LogUtil.init();
//      ArrayList<ClassInfo> logClass = null;
        HashMap<String, ClassInfo> logClass=null;

        Class cls = DBDesc.class;
        logClass = getClassInfo(cls);
        classInfoMap.put(cls.getName(), logClass);

        cls = TableDesc.class;
        logClass = getClassInfo(cls);
        classInfoMap.put(cls.getName(), logClass);

        cls = FieldDesc.class;
        logClass = getClassInfo(cls);
        classInfoMap.put(cls.getName(), logClass);

        cls = AgentGranularity.class;
        logClass = getClassInfo(cls);
        classInfoMap.put(cls.getName(), logClass);

        cls = SkillGranularity.class;
        logClass = getClassInfo(cls);
        classInfoMap.put(cls.getName(), logClass);

        cls = SumGranularity.class;
        logClass = getClassInfo(cls);
        classInfoMap.put(cls.getName(), logClass);

        cls = AgentInfo.class;
        logClass = getClassInfo(cls);
        classInfoMap.put(cls.getName(), logClass);

        cls = SkillInfo.class;
        logClass = getClassInfo(cls);
        classInfoMap.put(cls.getName(), logClass);

    }
    


    public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk");
}