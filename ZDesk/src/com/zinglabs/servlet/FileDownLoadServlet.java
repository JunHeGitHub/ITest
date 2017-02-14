package com.zinglabs.servlet;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONSerializer;


import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.log.LogUtil;

public class FileDownLoadServlet extends HttpServlet{
	private static final long serialVersionUID = 1338451589356828699L;
	public static  Logger logger = LoggerFactory.getLogger("ZKM"); 
	private String charSet="";
	public void init(ServletConfig config) throws ServletException {
		charSet=config.getInitParameter("encoding");
		if(charSet==null){
			charSet="UTF-8";
		}
		super.init(config);
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doDownload(req,resp);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doDownload(request,response);
	}
	
	public void doDownload(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		response.setCharacterEncoding(charSet);
		request.setCharacterEncoding(charSet);
		String id=request.getParameter("id")==null?"":request.getParameter("id");
		HashMap reMap=new HashMap();
		String json="";
		if(!"".equals(id)){
			try{
				String sql="select * from zkmInfoFile where id=?";
				String[]param={id};
				List<Map> list=DAOTools.queryMap(sql, param, ConfInfo.confMapDBConf.get("securityDBId"));
				if(list!=null && list.size()>0){
					Map dm=list.get(0);
					String fileName=dm.get("fileName")==null?"":(String)dm.get("fileName");
					String filePath=dm.get("filePath")==null?"":(String)dm.get("filePath");
					String tName=dm.get("relName")==null?fileName:(String)dm.get("relName");
					String ft=dm.get("fileType")==null?"":(String)dm.get("fileType");
					String relName=tName+ft;
					File f=new File(filePath + "/" + fileName);
					if(f.exists()){
						ServletContext context  = getServletConfig().getServletContext();
						String mimetype=context.getMimeType(fileName);
						response.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
						response.setContentLength((int)f.length());
						response.setHeader("Content-disposition", "attachment;filename="+ new String(relName.getBytes("gb2312"),"iso8859-1"));
						ServletOutputStream op = response.getOutputStream();
						byte[] bbuf = new byte[512];
				        DataInputStream in = new DataInputStream(new FileInputStream(f));
				        int length=0;
				        while ((in != null) && ((length = in.read(bbuf)) != -1)){
				            op.write(bbuf,0,length);
				        }
				        in.close();
				        op.flush();
				        op.close();
				        return;
					}else{
						reMap.put("success", false);
						reMap.put("data", dm);
						reMap.put("mgs", "文件不存在或已被删除的数据。");
					}
				}else{
					reMap.put("success", false);
					reMap.put("data", "");
					reMap.put("mgs", "文件不存在或已被删除的数据。");
				}
			}catch(Exception x){
				reMap.put("success", false);
				reMap.put("data", "");
				reMap.put("mgs", "异常下载失败。");
				LogUtil.error(x, logger);
			}
		}else{
			reMap.put("success", false);
			reMap.put("data", "");
			reMap.put("mgs", "缺少下载的必要参数");
		}
		json=JSONSerializer.toJSON(reMap).toString();
		PrintWriter out=response.getWriter();
		out.write(json);
		out.flush();
		out.close();
	}
}
