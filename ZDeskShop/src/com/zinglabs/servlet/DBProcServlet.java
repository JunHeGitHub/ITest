package com.zinglabs.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;
import com.zinglabs.tools.DOMTool;
import com.zinglabs.tools.Utility;
import com.zinglabs.util.CookieUtils;

import com.zinglabs.util.DESUtil;
import  com.zinglabs.util.JsonUtils;

/***
 *
 * 目标：覆盖数据常见操作。
 * 鲁棒、简单、安全、可扩展、维护成本低、上手容易、问题容易排查
 *
 */
public class DBProcServlet extends HttpServlet {

//    @TODO 与名片微信系统统一，版本号统一管理。
//    版本号规则，后面是修改时间，前面顺序递增。同一天修改多次时递增前面部分。

    public static final String version="v=1.13140911";

	private static final long serialVersionUID = 1L;
	
//	public final SimpleDateFormat formatx = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	public final SimpleDateFormat formatd = new SimpleDateFormat("yyyy-MM-dd");

	public final static String RET_STATUS_COMMON_ERR="{\"error\":-1004}";
	public final static String RET_STATUS_GET_OID_ERR="{\"error\":-1003}";
	public final static String RET_STATUS_NO_GROUP_ID="{\"error\":-1002}";
	public final static String RET_STATUS_NO_LOGIN="{\"error\":-1001}";
	public final static String RET_STATUS_NO_AUTH="{\"error\":-1000}";
	public final static String RET_STATUS_SUCCESS="{\"retcode\":0}";
    public final static String RET_STATUS_SUCCESS_PART="{\"retcode\":0";
	public final static String RET_STATUS_FAILED="{\"retcode\":-1}";
    public final static String RET_STATUS_NO_DATA="{\"retcode\":-3}";

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try
        {

            
        }
        catch (Exception e)
        {
            LogUtil.error(e);
        }

//       版本号及修改内容简单描述。
        LogUtil.info("DB Process version "+version );
	}

    public boolean writeAll(PrintWriter writer,String content){
        if(content==null)return true;
        if(writer!=null && !writer.checkError()){
            writer.write(content);
//              debug("writeAll :"+content);
//              out.flush();
        }else{
//          error("Exc writer error");
//          this.flag=false;
            return false;
        }
        return true;
    }
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    try {
            process(request, response);
        } catch (Exception e) {
            LogUtil.error(e);
        } 
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    try {
            process(request, response);
        } catch (Exception e) {
            LogUtil.error(e);
        } 
    }
	
	public void process(HttpServletRequest request,HttpServletResponse response) throws Exception{
	    String action = request.getParameter("action");
	    String callBack=request.getParameter("callback");
	    
	    LogUtil.debug(" DBProcServlet actino:"+action);
        request.getSession().setMaxInactiveInterval(3 * 60 * 60);
//        特殊返回变量，需要返回给客户端的数据
        String ret=null;

//      登录名使用des加密后存放在httpcookie中
        String loginName=CookieUtils.getCookieVal(request);
//          用户cookie校验  根据其他功能统一调整，处理
//        if(!checkUser(loginName)){
//            LogUtil.debug("checkUser false "+loginName);
//            return;
//        }

        if(action==null) {
            LogUtil.debug("sendRedirect ac null");
//            response.sendRedirect("/");
            return;
        }

        if (action.equals("LoadWinDataJson")) {
//            通用查询json
            long easyBegin=System.currentTimeMillis();
            String sql=request.getParameter("sql");

            String encode=request.getParameter("encode");
            if(encode!=null && encode.equals("true")){
                LogUtil.debug("encodeStrOrder sql:"+sql);
                sql= DESUtil.decodeUTF2(sql);
            }

            String isStream=request.getParameter("isStream");

            String dbId=request.getParameter("dbId");
            String tableName=request.getParameter("tableName");
            LogUtil.debug("EasyQueryJson sql:"+sql+" dbId:"+dbId+" tableName:"+tableName);
            if(sql!=null && dbId!=null && tableName!=null){
                if(isStream!=null && isStream.equals("true")) {
                    PrintWriter out = response.getWriter();
                    DAOTools.queryJsonStr(sql, dbId, tableName, out);
                    out.flush();
                    out.close();
                }else {
                    ret=DAOTools.queryJsonStr2(sql,dbId,tableName);
                }

                LogUtil.debug(ret);
            }
            LogUtil.debug(ret.length()+" EasyQueryJson end"+(System.currentTimeMillis()-easyBegin));
        } else if(action.equals("EasyDBJson")) {
//            通用增改删json
            long easyBegin=System.currentTimeMillis();
//            需要执行的sql
            String sql=request.getParameter("sql");
//            sql执行成功后，因更新页面，返回数据等原因，返回的查询sql。
//            注意：插入时无法提供完整sql，因id是服务器端生成的。
            String selSql=request.getParameter("selSql");
            String encode=request.getParameter("encode");

            if(encode!=null && encode.equals("true")){
                LogUtil.debug("encode sql:"+sql);
                sql= DESUtil.decodeUTF2(sql);
                if(selSql!=null){
                    selSql= DESUtil.decodeUTF2(selSql);
                    LogUtil.debug("selSql:"+selSql);
                }
            }

            String dbId=request.getParameter("dbId");
            String tableName=request.getParameter("tableName");
            LogUtil.debug("EasyToDB sql:"+sql+" dbId:"+dbId+" tableName:"+tableName);
            String retId=null;
            if(sql!=null && dbId!=null && tableName!=null){
                retId=DAOTools.execUpdateJsonS(sql,dbId,tableName);

                if(retId!=null && retId.length()>0){
//                    添加请求，返回插入数据库的id 更新页面列表

                    if(selSql==null || selSql.length()==0){
                        ret=RET_STATUS_SUCCESS_PART+",\"id\":"+"\""+retId+"\"}";
                    }else{
                        if(retId.equals("-1")){
//                            编辑，删除等需要返回查询的情况，selSql可以提供完整sql.

                        }else{
                            selSql=selSql+retId;
                        }
                        ret=DAOTools.queryJsonStr2(selSql,dbId,tableName);
                    }
                }
            }
            LogUtil.debug(retId+" EasyUpdate end"+(System.currentTimeMillis()-easyBegin));

        } else if(action.equals("Combo")){


        } else {
//            ret="authFail";
            LogUtil.debug("Exception else relogin ");
//            response.sendRedirect("/");
        }
        
        if(ret!=null) {
            String ans = null;
            if (callBack != null)
            {
                ans = callBack + "('" + ret + "')";
            }
            else
            {
                ans = ret;
            }
            if(ret==null || ret.length()<1000) {
                LogUtil.debug(ans);
            }
            // LogUtil.debug(ans);
            response.setCharacterEncoding("utf-8");
            PrintWriter writer = response.getWriter();
            writeAll(writer, ans);
            writer.flush();
            writer.close(); 
        }
	}
	

	@Override
	public void destroy() {
		super.destroy();
	}


    public volatile static boolean isDebug=false;

}
