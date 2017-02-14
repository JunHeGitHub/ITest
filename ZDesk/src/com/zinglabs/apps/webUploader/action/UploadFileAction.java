package com.zinglabs.apps.webUploader.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONSerializer;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zinglabs.apps.i18nPrompt.I18nPromptUtils;
import com.zinglabs.apps.webUploader.service.UploadFileService;
import com.zinglabs.base.core.frame.BaseAction;

@RequestMapping(value = "/*/UploadFile")
@Controller
public class UploadFileAction extends BaseAction {
	
	public static Logger logger = LoggerFactory.getLogger("ZDesk");
	/**
	 * upload 上传文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upload")
	public void upload(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String dataId=request.getParameter("dataId");//数据id
		String maxSize = request.getParameter("maxSize");//上传大小限制
		String relativeKey = request.getParameter("relativeKey");//相对位置key值
		String relativePath = "";//相对路径
		//查询配置表中配置的相对路径
		relativePath = getService().searchRelativeValue(relativeKey);
		if ("".equals(relativePath)|| relativePath==null) {
			relativePath="files";
		}
		String path = request.getSession().getServletContext().getRealPath(File.separator)+relativePath;
		UploadHandler handler = UploadHandler.getUploadHandler();// 上传工具类
		DiskFileItemFactory factory = new DiskFileItemFactory();// 文件处理工厂
		File tempFile = new File(path + "/temp/");// 缓存文件
		if (!tempFile.exists()) {
			tempFile.mkdirs();
		}
		factory.setSizeThreshold(1024 * 1024 * 4);// 设置缓存大小
		factory.setRepository(tempFile);// 设置缓存文件
		ServletFileUpload upload = new ServletFileUpload(factory);// request的解析器
		upload.setFileSizeMax(Long.parseLong(maxSize));//设置上传文件大小限制
		List items = null;// 上传文件队列集合
		String oldFileName = null;
		String newFileName = null;
		String fileType = null;
		long fileSize = 0;
		Map file = new HashMap();
		try {
			items = upload.parseRequest(request);
			for (Iterator it = items.iterator(); it.hasNext();) {
				FileItem item = (FileItem) it.next();
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = item.getString("UTF-8");
				} else {
					path = handler.getTimePath(path+"/");// 使用时间生成新路径
					// String filedName=item.getFieldName();
					String fileName = item.getName();// 文件路径
					oldFileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1,fileName.length());// 文件名
					newFileName = handler.getUuidName(fileName);// 使用UUID生成的新文件名
					fileType = handler.getFileType(oldFileName);
					fileSize = item.getSize();// 文件大小
					FileOutputStream fos = new FileOutputStream(path+newFileName);
					if (item.isInMemory()) {
						fos.write(item.get());
						fos.close();
					} else {
						InputStream is = item.getInputStream();
						byte[] buffer = new byte[1024];
						int len;
						while ((len = is.read(buffer)) > 0) {
							fos.write(buffer, 0, len);
						}
						is.close();
						fos.close();
					}
				}
			}
			path = path.replace(File.separatorChar, '/') + newFileName;
			file.put("id",UUID.randomUUID()+"");
			file.put("fileName",oldFileName);
			file.put("newFileName",newFileName);
			file.put("fileType",fileType);
			file.put("savePath",path);
			file.put("fileSize",fileSize);
			file.put("relativeSavePath",path.subSequence(path.indexOf(relativePath)-1,path.length()));
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			file.put("uploadTime",sdf.format(new Date()));
			
			//保存文件信息到数据库
			getService().addUploadFile(file);

			//保存数据关联信息到数据库
			Map map = new HashMap();
			map.put("fileId",file.get("id"));
			
			if(!dataId.equals(null)&&!dataId.equals("")&&dataId!=null){
				String[] list = dataId.split(",");
				for (String str : list) {
					map.put("dataId",str);
					getService().addFileDataMapper(map);
				}
			}
			//为文件名称转码
			file.put("fileName",I18nPromptUtils.native2Ascii(oldFileName));
			file.put("newFileName",I18nPromptUtils.native2Ascii(newFileName));
			//向前端返回数据
			response.setHeader("Content-Type", "text/plain");
			PrintWriter out = response.getWriter();
			out.println(JSONSerializer.toJSON(file).toString());
			out.flush();
			out.close();
			
			//清空缓存目录中的文件
			File[] files = tempFile.listFiles();
			for (File fi : files) {
				if(fi.exists()){
					fi.delete();
				}
			}
		} catch (FileUploadBase.FileSizeLimitExceededException e) {
			logger.debug("文件上传失败,超出上传大小限制");
			logger.error("UploadFileAction  upload"+e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.debug("发生异常");
			logger.error("UploadFileAction  upload"+e.getMessage());
			e.printStackTrace();
		}
	}
	private UploadFileService getService() {
		return (UploadFileService) getBean("uploadFileService");
	}
}
