package com.zinglabs.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.zinglabs.base.Interface.BaseInterface;
import com.zinglabs.db.ShareComManager;
import com.zinglabs.log.LogUtil;
import com.zinglabs.tools.DOMTool;
import com.zinglabs.tools.Utility;
/**
 * comet 命令接收服务
 * @author Liduo.wang
 *
 */
public class CommandService extends HttpServlet{

	private static final long serialVersionUID = -6613276920757182603L;

	//public static TaskMonitor tm=new TaskMonitor();
	
	public void destroy() {
		super.destroy();
	}

	
	public void init() throws ServletException {
		//Thread td=new Thread(tm);
		//td.start();
		super.init();
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doChangeCommands(req, resp);
		super.doPost(req, resp);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doChangeCommands(req, resp);
		super.doGet(req, resp);
	}
	
	/**
	 * 
	 * @param req
	 * @param resp
	 * Description 
	 * doc for test like this 
	 * <client id=''></client>
	 * TODO 得到客户端请求，并改变相应配置
	 */
	protected void doChangeCommands(HttpServletRequest req, HttpServletResponse resp){
		//tm.tlist.add(Thread.currentThread());
		String action=req.getParameter("action");
		String contentGB=null;
		if(req.getParameter("content")!=null){
//			try {
//				contentGB=Utility.getCodeStr(req.getParameter("content"),"ISO-8859-1","gb2312");
//			} catch (Exception e) {
//				LogUtil.error("Utility.getCodeStr", SKIP_Logger);
//				LogUtil.error(e, SKIP_Logger);
//			}
			
			String content = "<?xml version=\"1.0\" encoding=\"gbk\"?>"+ req.getParameter("content");
			try {
				contentGB = new String(content.getBytes("gbk"));
			} catch (UnsupportedEncodingException e) {
				LogUtil.error("Utility.getCodeStr", SKIP_Logger);
				LogUtil.error(e, SKIP_Logger);
			}
			contentGB = contentGB.replaceAll("<\\?xml version=\"1.0\"\\?>", "");
		}
		
		LogUtil.debug("CommandService action:"+action+" content:"+contentGB, SKIP_Logger);
		
		if(action != null){
			if (action.equals(BaseInterface.ACTION_PAGE_OPEN)) {
				Document doc=null;
				try {
					doc = DOMTool.loadDocumentFromStr(contentGB,"gbk");
				} catch (Exception e1) {
					LogUtil.error(e1, SKIP_Logger);
				}
				Node clientRoot = DOMTool.getSingleNode(doc, BaseInterface.COMET_XML_MGS_CLIENT);
				String id=DOMTool.getAttributeValue((Element)clientRoot, "id");
				String clientId=DOMTool.getAttributeValue((Element)clientRoot, "clientId");
				LogUtil.debug("containsKey clientId:"+ShareComManager.shareMap.containsKey(clientId)+"  id:"+id, SKIP_Logger);
				
				if(ShareComManager.shareMap.containsKey(clientId)){
					ShareComManager scTmp=ShareComManager.shareMap.get(clientId);
					try{
						LogUtil.debug("ShareComManager doc:"+doc+"  id:"+id, SKIP_Logger);
						scTmp.changePageDoc(id, doc,req.getSession().getServletContext().getRealPath("/"));
						scTmp.connectSucess=true;
					}catch(Exception e){
						LogUtil.error(e, SKIP_Logger);
						scTmp.connectSucess=false;
					}
				}
			}else if(action.equals(BaseInterface.ACTION_PAGE_CLOSE)){
				String id=req.getParameter("id");
				
				String clientId=req.getParameter("clientId");
				LogUtil.debug("containsKey clientId:"+ShareComManager.shareMap.containsKey(clientId)+"  id:"+id, SKIP_Logger);
				
				if(ShareComManager.shareMap.containsKey(clientId)){
					ShareComManager scTmp=ShareComManager.shareMap.get(clientId);
					try{
						LogUtil.debug("ACTION_PAGE_CLOSE ShareComManager id:"+id, SKIP_Logger);
						scTmp.pageDoc.remove(id);
//						scTmp.changePageDoc(id, doc,req.getSession().getServletContext().getRealPath("/"));
//						scTmp.connectSucess=true;
					}catch(Exception e){
						LogUtil.error(e, SKIP_Logger);
//						scTmp.connectSucess=false;
					}
				}
			}
		}
	}
	
	
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
}
