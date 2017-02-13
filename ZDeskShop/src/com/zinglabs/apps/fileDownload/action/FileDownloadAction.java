package com.zinglabs.apps.fileDownload.action;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.customTextTemplate.service.CustomTextService;
import com.zinglabs.apps.fileDownload.service.fileDownloadService;
import com.zinglabs.apps.i18nPrompt.I18nPromptUtils;
import com.zinglabs.base.core.frame.BaseAction;

@Controller
@RequestMapping(value = "/*/Commons")
public class FileDownloadAction extends BaseAction{
	@RequestMapping(value="fileDownload.action")
    public void fileDownload(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	request.setCharacterEncoding("utf-8");
    	//文件名
    	String name=request.getParameter("name")==null?"downloadFile":request.getParameter("name");
        //文件路径
    	String file=request.getParameter("file")==null?"":request.getParameter("file");
    	String mimetype=request.getParameter("mimetype");
    	file=I18nPromptUtils.ascii2Native(file);
    	name=I18nPromptUtils.ascii2Native(name);  	
    	if(file.length()>0){
    		//TODO  去过滤
    		//if(file.indexOf("zkmDocs") > 0 || file.indexOf("uploads") > 0 || file.indexOf("zkm_temp_F")>0){
    			File df=new File(file);
    			if(df.exists() && df.isFile() && df.canRead()){
    				response.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
    				response.setContentLength((int)df.length());
    				response.setHeader("Content-disposition", "attachment;filename="+ new String(name.getBytes("gb2312"),"iso8859-1"));
    				ServletOutputStream op= response.getOutputStream();;
    				byte[] bbuf = new byte[512];
    				DataInputStream in = new DataInputStream(new FileInputStream(df));
    				int length=0;
    				while ((in != null) && ((length = in.read(bbuf)) != -1)){
    					  op.write(bbuf,0,length);
    				}
    				in.close();
    				op.flush();
    				op.close();
    			}else{
    				response.setContentType("text/html;charset=UTF-8");
    				PrintWriter pw = response.getWriter();
     				pw.write("<script>window.top.fileExists();</script>");
    				pw.flush();
    				pw.close();
    				//String body="";
    				// "<script type=\"text/javascript\">alert(\"文件不存在\");<scirpt>";
    			}
    	}
    //	return null;
		
    }
	/*
	 * 根据数据id查询已上传文件信息
	 */
	@RequestMapping(value="searchFileToDownload.action")
    public void searchFileToDownload(@RequestParam Map map,HttpServletRequest request, HttpServletResponse response){
		Map rmap = this.getService().searchFileToDownload(map);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}
	public fileDownloadService getService() {
		return (fileDownloadService) getBean("fileDownloadService");
	}
	public static void main(String[] args) {
		File df=new File("c://xxx.xml");
		
	}
}
