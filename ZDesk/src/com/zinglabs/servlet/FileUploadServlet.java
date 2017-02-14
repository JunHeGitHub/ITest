package com.zinglabs.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.db.UserInfo2;
import com.zinglabs.log.LogUtil;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.FileUtils;
import com.zinglabs.util.RandomGUID;
import com.zinglabs.util.ZKMAPPUtils;
import com.zinglabs.util.ZKMConfs;


public class FileUploadServlet extends HttpServlet {
	public static  Logger logger = LoggerFactory.getLogger("ZKM"); 
	private static final long serialVersionUID = 2628329819758883023L;
	private String charSet="";
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doUpload(req,resp);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doUpload(request,response);
	}
	
	public void init(ServletConfig config) throws ServletException {
		charSet=config.getInitParameter("encoding");
		if(charSet==null){
			charSet="UTF-8";
		}
		super.init(config);
	}
	
	public void doUpload(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		response.setCharacterEncoding(charSet);
		request.setCharacterEncoding(charSet);
		
		//String rootpath = this.getServletConfig().getServletContext().getRealPath("/");
		String fileUse="";
		//String root=request.getContextPath();
		
		String rootpath=ZKMConfs.confs.getProperty("zkmUploadFileSaveDir","/usr/local/nginx/html/ZDesk/uploads");
		String tempDir=ZKMConfs.confs.getProperty("mergeTempDir","/mnt/zkm_temp_F");
		
		String fileServerName="";
		rootpath = rootpath + "/resourses/";
		String dataFolder=FileUtils.getDateFolderStr();
		File savePath=new File(rootpath + "/" +  dataFolder);
		if(!savePath.exists()){
			savePath.mkdirs();
			ZKMAPPUtils.fcdirChangeUserAndGroup(savePath);
		}
		DiskFileItemFactory fac = new DiskFileItemFactory();
		fac.setSizeThreshold(5 * 1024 * 1024);
		fac.setRepository(new File(tempDir));
		
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding(charSet);
		upload.setSizeMax(50 * 1024 * 1024);
		
		List fileList = null;
		try {
			fileList = upload.parseRequest(request);
		} catch (FileUploadException ex) {
			ex.printStackTrace();
			logger.error("上传文件异常。");
			logger.error(ex.getMessage());
		}
		
		LogUtil.debug("upload file size : " + (fileList==null?"null":fileList.size()), logger);
		
		if(fileList!=null && fileList.size()>0){
			
			Iterator<FileItem> it = fileList.iterator();
			
			
			HashMap pmap=new HashMap();
			
			while (it.hasNext()) {
				FileItem item = it.next();
				if (item.isFormField()) {
					pmap.put(item.getFieldName(),item.getString(charSet));
				}
			}
			String name="";
			String extName="";
			String saveName="";
			String fileId="";
			String saveType=pmap.get("saveType")==null?"":pmap.get("saveType").toString();
			String xmlDoc=pmap.get("xmlDoc")==null?"":pmap.get("xmlDoc").toString();
			
			String loginUser=pmap.get("zkm_userName")==null?"":pmap.get("zkm_userName").toString();
			
			String infoId=pmap.get("infoId")==null?"":pmap.get("infoId").toString();
			String infoName=pmap.get("infoName")==null?"":pmap.get("infoName").toString();
			fileUse=pmap.get("fileUse")==null?"":pmap.get("fileUse").toString();
			
			if(saveType.length()<=0){
				saveType=request.getParameter("saveType")==null?"":request.getParameter("saveType");
			}
			
			if(fileUse.length()<=0){
				fileUse=request.getParameter("fileUse")==null?"":request.getParameter("fileUse");
			}
			
			if(xmlDoc.length()<=0){
				
			}
			
			it=fileList.iterator();
			HashMap imap=null;
			while(it.hasNext()){
				FileItem item = it.next();
				if (!item.isFormField() && saveType.length()>0) {
					HashMap savemap=new HashMap();
					name = item.getName();
					if (name == null || name.trim().equals("")) {
						continue;
					}
					if(name.lastIndexOf("\\")>-1){
						name=name.substring(name.lastIndexOf("\\")+1,name.length());
					}
					if(name.lastIndexOf("/")>-1){
						name=name.substring(name.lastIndexOf("/")+1,name.length());
					}
					if (name.lastIndexOf(".") >= 0) {
						extName = name.substring(name.lastIndexOf("."));
					}
					name=name.substring(0, name.lastIndexOf("."));
					
					UserInfo2 user=null;
					try {
						fileId=(new RandomGUID()).toString();
						saveName=fileId;
						File saveFile = new File(savePath.getPath() +"/" +  saveName + extName);
						fileServerName=saveName + extName;
						logger.info("upload file -- : " + saveFile.getPath() + " : " + saveFile.getName());
						item.write(saveFile);
						
						user=UserInfo2.getUser(loginUser);
						if(user==null){
							user=UserInfo2.getUser(request);
						}
						
						
						savemap.put("userInfo", user);
						savemap.put("fileName", saveName + extName);
						savemap.put("filePath", savePath.getPath());
						savemap.put("relName", name);
						savemap.put("fileType", extName);
						savemap.put("file", saveFile);
						savemap.put("name", name);
						savemap.put("extendName", extName);
						savemap.put("uploadDate", DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss"));
						savemap.put("infoId", infoId);
						savemap.put("infoName", infoName);
						savemap.put("xmlDoc",xmlDoc);
						savemap.put("fileUse", fileUse);
						savemap.put("relationSort", pmap.get("relationSort"));
						savemap.put("relPath", request.getSession().getServletContext().getRealPath("/"));
						savemap.put("fileId", fileId);
						FileUploadHandle.HandleEntrance(saveType, savemap);
						imap=savemap;
						
					} catch (Exception e) {
						LogUtil.error(e, logger);
						LogUtil.error("上传文件失败",logger);
					}finally{
						if(user!=null){
							try {
								user.releaseAll();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			if(imap!=null && "DB_LUCENEINDEX".equals(saveType)){
				try{
					logger.info("----- upload file is :" + imap.get("infoId"));
					//LuceneSearchHandle.doTaskWriter(LuceneSearchHandle.WRITER_TYPE_DEL, imap);
					LuceneSearchHandle.doTaskWriter(LuceneSearchHandle.WRITER_TYPE_INDEX_BASE_ALL, imap);
					
				}catch(Exception ee){
					LogUtil.error(ee, logger);
					LogUtil.error("上传文件更新索引失败...",logger);
				}
			}
		}
		//response.getWriter().print(name + " 上传成功");
		String fileURL=request.getContextPath() + "/ZKM/" + "uploads/resourses/" + dataFolder +"/" + fileServerName;
		
		if("zkm_flash".equals(fileUse)){
			String exName=fileServerName.substring(fileServerName.lastIndexOf("."));
			exName=exName.toUpperCase();
			if(".FLV".equals(exName.trim())){
				fileURL="/ZDesk/js/flvPlayer/flvplayer.swf?vcastr_file=" + fileURL;
			}
		}
		
		HashMap rem=new HashMap();
		rem.put("err", "");
		rem.put("msg", fileURL);
		PrintWriter out=response.getWriter();
		out.write(JSONSerializer.toJSON(rem).toString());
		out.flush();
		out.close();
	}
}
