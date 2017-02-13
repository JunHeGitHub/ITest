package com.zinglabs.db;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



import com.zinglabs.base.Interface.BaseInterface;
import com.zinglabs.base.absImpl.BaseAbs;
import com.zinglabs.log.LogUtil;
import com.zinglabs.task.SendTask;
import com.zinglabs.tools.DOMTool;
import com.zinglabs.tools.Utility;

/**
 * 内存共享组件对象接口类
 * @author Liduo.wang
 *
 */
public class ShareComManager extends BaseAbs {
	
	public ShareComManager(){
		/**
		 * TODO 初始化日志对象
		 */
		init(SKIP_Logger);
		this.dataRefreshTime=DEFULT_DATA_REFRESH_TIME;
		this.connectSucess=false;
//		this.pageMap=new HashMap<String, String>();
//		this.pageIdMap=new HashMap<String, String>();
		this.pageDoc=new ConcurrentHashMap<String, Document>();
//		this.xmlDoc=new HashMap<String ,Document>();
		this.agentList=new ArrayList<AgentInfo>();
		this.agentMap=new ArrayList<AgentInfo>();
		
	}
	
	public ShareComManager(HttpServletResponse response,String clientId){
		/**
		 * TODO 初始化日志对象
		 */
		init(SKIP_Logger);
		this.dataRefreshTime=DEFULT_DATA_REFRESH_TIME;
		this.connectSucess=false;
//		this.pageMap=new HashMap<String, String>();
//		this.pageIdMap=new HashMap<String, String>();
		this.pageDoc=new ConcurrentHashMap<String, Document>();
//		this.xmlDoc=new HashMap<String ,Document>();
		this.agentList=new ArrayList<AgentInfo>();
		this.agentMap=new ArrayList<AgentInfo>();
		this.response=response;
		this.clientId=clientId;
		
	}
	/**
	 * 内存共享对象缓存
	 */
	
	/**
	 * TODO 通过唯一id标识的 服务线程，页面信息等数据
	 * ConcurrentHashMap 线程安全
	 */
	public static ConcurrentHashMap<String,ShareComManager> shareMap;
	
	public static ConcurrentHashMap<String,Document> allPageInfo;
	
	static{
		shareMap=new ConcurrentHashMap<String, ShareComManager>();
		allPageInfo=new ConcurrentHashMap<String, Document>();
	}
	
	public HttpServletResponse response;
	
	public SendTask task;
	
	public Thread T;
	
	public volatile boolean connectSucess;
	
	public String clientId;
	
	/*****参数 页面控制相关 begin 打开页面 刷新率等参数*****/

	public volatile int dataRefreshTime;
	
	/**
	 * 打开页面描述、查询条件、页面参数等xml描述
	 */
	public ConcurrentHashMap<String,Document> pageDoc;
	/**
	 * 缓存与拼音ID对应的服务器xml描述
	 */
//	public HashMap<String,Document> xmlDoc;
	
	
//	public HashMap<String, String> pageMap;
//	
//	public HashMap<String,String> pageIdMap;
	
	/*****参数 页面控制相关 end*****/
	
	
	/*****数据相关 begin *****/
	
	ArrayList<AgentInfo> agentList;
	ArrayList<AgentInfo> agentMap;
	
	
	//TODO:测试用sql limit
	public static int startNum=0;
	
	public static int returnNum=100;
	
	public int testnum=0;
	public int datanum=0;
	
	
	
	/**
	 * 更改页面条件XML描述,并缓存页面相关服务器描述
	 * @param id 页面ID （目前：汉语拼音）
	 * @param doc 上发的doc对象
	 */
	public void changePageDoc(String id,Document doc,String relPath) throws Exception{
		if(id!=null && !"".equals(id)){
			this.pageDoc.put(id, doc);
			ConfXmlObj xmlObj=null;
//			debug("changePageDoc pageDoc:"+this.pageDoc);
//			debug("file path :"+relPath + id + "\\" + id + ".xml");
			if(ConfXmlObj.XMLObjMap.containsKey(id)){
//				pageDoc=XMLCacheServ.XMLObjMap.get(id).pageDoc;
				xmlObj=ConfXmlObj.XMLObjMap.get(id);
			}else{
//				File file = new File(absPath+"/"+id+"/"+id+".xml");
//				pageDoc = DOMTool.loadDocumentFromFile(file);
				xmlObj=new ConfXmlObj(relPath+"/"+id+"/"+id+".xml");
//				pageDoc = xmlObj.pageDoc;
				ConfXmlObj.XMLObjMap.put(id, xmlObj);
			}
			
			
			
//			if(!this.xmlDoc.containsKey(id)){
//				Document serverDoc=null;
//				//TODO:服务器 XML 描述文件存放位置 规则 id\id.xml
//				debug("file path :"+relPath + id + "/" + id + ".xml");
//				File file = new File(relPath + id + "/" + id + ".xml" );
//				if(file.exists()){
//					serverDoc = DOMTool.loadDocumentFromFile(file);
//				}
//				this.xmlDoc.put(id, serverDoc);
//				//debug("--------------get doc ：" + this.xmlDoc.get(id)==null?"nulllll":"not nullll");
//			}
		}
	}
	
	public String toXml(PrintWriter writer){
		String ret=null;
		
		
		return ret;
	}
	
	
	public String genAndWrite(HttpServletResponse response) throws Exception{
		StringBuffer ret=new StringBuffer();
//		debug(System.currentTimeMillis()+"pageDoc:"+pageDoc);
		if(pageDoc!=null && pageDoc.size()>0){
			String msg=null;
			ConfXmlObj xmlObj=null;
			Document doc=null;
//			StringBuffer mgs=new StringBuffer();
//			mgs.append(COMET_XML_MGS_ENCODING);
//			mgs.append("<" + COMET_XML_MGS_DATASET + ">");
//			
			msg ="<?xml version=\"1.0\" encoding=\"GBK\"?>";
			msg+="<datasets>";
			
			Iterator it=pageDoc.keySet().iterator();
			while(it.hasNext()){
				String key=it.next().toString();
				//TODO:这里需要做一下处理，复制
				doc=((Document)pageDoc.get(key));
				//TODO:这里得做为空时间处理
				xmlObj=ConfXmlObj.XMLObjMap.get(key);
//				Document fdoc=((Document)xmlDoc.get(key));
				//获取数据串
				
				//debug("key:"+key+"doc "+doc+"fdoc"+fdoc);
				msg += ConfXmlObj.loadXmlData(doc, xmlObj,null,false,null);
//				mgs=parseAndReturnXml(doc,fdoc,mgs);
				
			}
			msg+="</datasets>";
//			mgs.append("</" + COMET_XML_MGS_DATASET + ">");
//			debug("mgs : ------- : " + mgs.toString());
//			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			mgs.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
//			mgs.append("<datasets>");
//			mgs.append("<table>");
//			mgs.append("<rows groupID=\"测试"+ (++datanum) +"\" workDate=\""+ f.format(new Timestamp(System.currentTimeMillis())) +"\" callSkExtCount=\"103\" callSkDistributeCount=\"104\" callSkExtConnectCount=\"105\"  transCount=\"106\" meetingCount=\"107\" colineGiveupCount=\"108\" inlineNoAnsCount=\"109\" callSkExtTalkPeriod=\"110\" onlineAmt=\"111\"/>");
//			for(int i=0;i<500;i++){
//				mgs.append("<rows groupID=\"测试"+ (++datanum) +"\" workDate=\""+ f.format(new Timestamp(System.currentTimeMillis())) +"\" callSkExtCount=\"103\" callSkDistributeCount=\"104\" callSkExtConnectCount=\"105\"  transCount=\"106\" meetingCount=\"107\" colineGiveupCount=\"108\" inlineNoAnsCount=\"109\" callSkExtTalkPeriod=\"110\" onlineAmt=\"111\"/>");
//			}
//			
//			mgs.append("</table>");
//			mgs.append("</datasets>");
			
//			if(task==null || task.clientId==null) return msg;
			
			cometoClient(msg,response);
			
			//debug(Thread.currentThread().getName()+"--"+mgs.length()+"-----!!!------testnum:"+testnum);
			//debug("----------- Old RET___: " + ret.toString());
			
			if(ret!=null){
//				String tm=ret.toString();
				return ret.toString();
				
//				return Utility.getCodeStr(ret.toString(),"utf-8","gb2312");
//				return Utility.getCodeStr(ret.toString(),"ISO-8859-1","gb2312");
			}
		}else{
			cometoClient(null,response);
//			ret=getReturnStr(null);
		}
		return ret.toString();
	}
	
	/**
	 * 筛选数据下发
	 * @return
	 * @throws Exception
	 */
	public String toXml() throws Exception{
		StringBuffer ret=new StringBuffer();
//		debug(System.currentTimeMillis()+"pageDoc:"+pageDoc);
		if(pageDoc!=null && pageDoc.size()>0){
			String msg=null;
			ConfXmlObj xmlObj=null;
			Document doc=null;
//			StringBuffer mgs=new StringBuffer();
//			mgs.append(COMET_XML_MGS_ENCODING);
//			mgs.append("<" + COMET_XML_MGS_DATASET + ">");
//			
			msg ="<?xml version=\"1.0\" encoding=\"GBK\"?>";
			msg+="<datasets>";
			
			Iterator it=pageDoc.keySet().iterator();
			while(it.hasNext()){
				String key=it.next().toString();
				//TODO:这里需要做一下处理，复制
				doc=((Document)pageDoc.get(key));
				//TODO:这里得做为空时间处理
				xmlObj=ConfXmlObj.XMLObjMap.get(key);
//				Document fdoc=((Document)xmlDoc.get(key));
				//获取数据串
				
				//debug("key:"+key+"doc "+doc+"fdoc"+fdoc);
				msg += ConfXmlObj.loadXmlData(doc, xmlObj,null,false,null);
//				mgs=parseAndReturnXml(doc,fdoc,mgs);
				
			}
			msg+="</datasets>";
//			mgs.append("</" + COMET_XML_MGS_DATASET + ">");
//			debug("mgs : ------- : " + mgs.toString());
//			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			mgs.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
//			mgs.append("<datasets>");
//			mgs.append("<table>");
//			mgs.append("<rows groupID=\"测试"+ (++datanum) +"\" workDate=\""+ f.format(new Timestamp(System.currentTimeMillis())) +"\" callSkExtCount=\"103\" callSkDistributeCount=\"104\" callSkExtConnectCount=\"105\"  transCount=\"106\" meetingCount=\"107\" colineGiveupCount=\"108\" inlineNoAnsCount=\"109\" callSkExtTalkPeriod=\"110\" onlineAmt=\"111\"/>");
//			for(int i=0;i<500;i++){
//				mgs.append("<rows groupID=\"测试"+ (++datanum) +"\" workDate=\""+ f.format(new Timestamp(System.currentTimeMillis())) +"\" callSkExtCount=\"103\" callSkDistributeCount=\"104\" callSkExtConnectCount=\"105\"  transCount=\"106\" meetingCount=\"107\" colineGiveupCount=\"108\" inlineNoAnsCount=\"109\" callSkExtTalkPeriod=\"110\" onlineAmt=\"111\"/>");
//			}
//			
//			mgs.append("</table>");
//			mgs.append("</datasets>");
			
			if(task==null || task.clientId==null) return msg;
			
			ret=getReturnStr2(msg);
			
			//debug(Thread.currentThread().getName()+"--"+mgs.length()+"-----!!!------testnum:"+testnum);
			//debug("----------- Old RET___: " + ret.toString());
			
			if(ret!=null){
//				String tm=ret.toString();
				return ret.toString();
				
//				return Utility.getCodeStr(ret.toString(),"utf-8","gb2312");
//				return Utility.getCodeStr(ret.toString(),"ISO-8859-1","gb2312");
			}
		}else{
			ret=getReturnStr2(null);
		}
		return ret.toString();
	}
	
	
	

	public StringBuffer parseAndReturnXml(Document doc,Document fdoc,StringBuffer mgs) throws Exception{
		//TODO：下面的 item client 是否应该提取成为配置文件。
		NodeList nl = DOMTool.getMultiNodes(doc, COMET_XML_MGS_DATASET_TABLE_ROW);
		Node clientRoot = DOMTool.getSingleNode(doc, COMET_XML_MGS_CLIENT);
		String id=DOMTool.getAttributeValue((Element)clientRoot, "id");
		StringBuffer sql=new StringBuffer(),where=new StringBuffer(" where 1=1 "),sqlFrom=new StringBuffer(" FROM ");
		String table=null,nodeName=null,nodeValue=null,dbType=null,javaType=null,extend=null,dbID=null,timeEndTmp=null;
		int keyType=Integer.parseInt("0", 2),count=0;
		String[] extendTmp=null;
//		Attribute[] attrs = new Attribute[nl.getLength()];
		HashMap<String, String> attrMap=null;
		
		Node pageDWNode=DOMTool.getSingleNode(fdoc, "DataWindow");
		dbID=DOMTool.getAttributeValue((Element)pageDWNode,"dbID");
		Element attrNode=null;
		XPathFactory factoryXpah = XPathFactory.newInstance();
		XPath xpath = factoryXpah.newXPath();
		if(nl.getLength()>0){
			error("search with multi-condition");
			error(DOMTool.toXmlString(doc, false));
		}
		
		for (int i = 0; i < nl.getLength(); i++) {
			attrMap = DOMTool.getAttributeValues((Element) ((Element) nl.item(i)));
			String operation=DOMTool.getAttributeValue((Element)nl.item(i), "operation");
			count=0;
			for (Iterator ia = attrMap.entrySet().iterator(); ia.hasNext();count++) {
				//LogUtil.debug("----- sql : " + sql.toString(), SKIP_Logger);
				timeEndTmp=null;
				java.util.Map.Entry ea = (java.util.Map.Entry) ia.next();
				nodeValue = (String) ea.getValue();
				nodeName=(String)ea.getKey();
				if(nodeName.equals("operation"))continue;
				
				if(nodeName.indexOf(BASE_TIME_END)>0){
					timeEndTmp=nodeName.substring(0,nodeName.indexOf(BASE_TIME_END));
				}
				XPathExpression expr = null;
				if(timeEndTmp==null){
					expr = xpath.compile("/DataWindow/attributes/attribute[@attrName='"+nodeName+"']");
				}else{
					expr = xpath.compile("/DataWindow/attributes/attribute[@attrName='"+timeEndTmp+"']");
				}
				
				attrNode = (Element)(expr.evaluate(fdoc, XPathConstants.NODE));
				table=DOMTool.getAttributeValue(attrNode, "fullName");
				dbType=DOMTool.getAttributeValue(attrNode, "dbType");
				extend=DOMTool.getAttributeValue(attrNode, "extend");
				keyType=Integer.parseInt("0", 2);
				if(extend!=null && extend.length()>0){
					extendTmp=extend.split("#");
					if(!extendTmp[0].equals(BaseInterface.CONF_INFO_SKIP)){
						keyType=Integer.parseInt(extendTmp[0], 2);
					}
				}
				
				javaType=Utility.getTypeStr(dbType);
				if (operation.equals(OPERATION_SEARCH)) {
					if (sql.length() == 0) {
						sql.append("select  ");
						sqlFrom.append(table);
					}
					if(timeEndTmp==null){
						sql.append(table);
						sql.append(".");
						sql.append(nodeName);
						sql.append(",");
					}
					//LogUtil.debug(nodeValue+"__"+keyType+"___"+nodeName+"____"+(keyType & BaseData.DB_KEY_TYPE_SELECT), SKIP_Logger);
					if (nodeValue != null && nodeValue.length() > 0 && (keyType & DB_KEY_TYPE_SELECT)>0 ) {
						where.append(" and ");
						where.append(table);
						where.append(".");
						
						if(timeEndTmp==null){
							where.append(nodeName);
						}else{
							where.append(timeEndTmp);
						}
						
						if(javaType.equals("java.sql.Timestamp") && timeEndTmp==null){
							where.append(" >= ");
						}else if(javaType.equals("java.sql.Timestamp")){
							where.append(" <= ");
						}else{
							where.append("=");
						}
						
						if(javaType.equals("java.lang.String") || javaType.equals("java.sql.Timestamp")){
							where.append("'");	
							where.append(nodeValue);
							where.append("'");
						}else{
							where.append(nodeValue);
						}
					}
				}
			}
			/*
			 * TODO:在上发条件 XML 没有条件时，会出现SQL为空的现像。
			 * 		在上发条件 XML 中缺少下发数据的数据项时，组装下发数据时会出现异常。
			 * 所以：在拼装上发条件 XML 时，要将返回的数据不论有没有条件都写入 item 中
			 */
			
			if(sql==null || "".equals(sql)){
				return new StringBuffer().append("");
			}
			
			sql=new StringBuffer( sql.substring(0,sql.length()-1).toString()+sqlFrom.toString() + where.toString());
			
			//debug("sql:"+sql.toString());
			
			mgs.append(queryXml(sql.toString(), dbID,fdoc,id));
			
			if(nl.getLength()>1){
				error("loadXmlData nl >1 xml:"+DOMTool.toXmlString(doc, true));
				break;
			}
		}
		return mgs;
	}
	
	
	
	/**
	 * 返回查询结果集 xml
	 * @param sql 查询语句
	 * @param type 数据库标识 即数据配置 id (该在 服务端描述xml中 标签DataWindow 的属性 dbId)
	 * @param doc 服务器端 xml 描述
	 * @param id pageDoc 的ID
	 * @return
	 * @throws Exception
	 */
	public StringBuffer queryXml(String sql,String type,Document doc,String id) throws Exception{
		
		//TODO:测试用sql
//		sql=sql + " LIMIT " + startNum +"," + returnNum;
		debug("---------- : " + sql);
//		this.startNum=startNum+returnNum>500?0:startNum+returnNum;
		
		
		StringBuffer ret=new StringBuffer();
		Connection conn=null;
		PreparedStatement stmt=null;
		ResultSet res = null;
		
		ret.append("<" + COMET_XML_MGS_DATASET_TABLE +" id=\"" + id + "\">");
		String table=null,colName=null,fTmp=null;
		Attribute[] attrs=initAttrs(doc);
		Object obj=null;
		Element attrNode=null;
		XPathFactory factoryXpah = XPathFactory.newInstance();
		XPath xpath = factoryXpah.newXPath();
		try {
			//这里获取的是一个被保持的数据库长连接
//			info("-----------: " + type);
			conn=DAOTools.getConnectionOutS(type);
			stmt=conn.prepareStatement(sql.toString());
			res=stmt.executeQuery();
			while(res.next()){
				ret.append("<" + COMET_XML_MGS_DATASET_TABLE_ROW + " ");
				for(int i=0;i<attrs.length;i++){
					table=attrs[i].getAttrValue("fullName");
					colName=attrs[i].getAttrValue("attrName");
					obj=res.getObject(table+"."+colName);
					
					XPathExpression expr = xpath.compile("/DataWindow/attributes/attribute[@attrName='"+colName+"']");
					Object result = expr.evaluate(doc, XPathConstants.NODE);
					attrNode = (Element) result;
					fTmp=DOMTool.getAttributeValue(attrNode, "timeFormat");
					if(fTmp!=null && fTmp.length()>0){
						SimpleDateFormat format = new SimpleDateFormat(extFormatToJava(fTmp));
						obj=format.format(obj);
					}
					
					ret.append(colName);
					ret.append("=");
					ret.append("\"");
					//TODO:obj获取的值为空时如何处理
					ret.append(obj);
					ret.append("\" ");
				}
				ret.append(" />");
			}
		} catch (Exception e) {
			error("error in execSelect sql:"+sql.toString());
			error(e);
		}finally{
			try {
				if(stmt!=null){
					stmt.close();
					stmt=null;
				}
				DAOTools.releaseConnectionOutS(type, conn);
			} catch (Exception e) {
				error(e);
			}
			
		}
		ret.append("</" + COMET_XML_MGS_DATASET_TABLE + ">");
		return ret;
	}
	
	public Attribute[] initAttrs(Node node) {
		NodeList nl = DOMTool.getMultiNodes(node, "attribute");
		Attribute[] attrs = new Attribute[nl.getLength()];
		for (int i = 0; i < nl.getLength(); i++) {
			attrs[i] = new Attribute(nl.item(i));
		}
		return attrs;
	}
	
	public String extFormatToJava(String format){
		if(format.equals("Y-m-d H:i:s")){
			return "yyyy-MM-dd HH:mm:ss";
		}else if(format.equals("H:i:s")){
			return "HH:mm:ss";
		}else if(format.equals("Y-m-d")){
			return "yyyy-MM-dd";
		}
		return null;
	}
	
	
	/**
	 * 推数据的页面开始执行函数。
	 * @param msg
	 * @return
	 * 
	 */
	public StringBuffer getReturnStr2(String msg){
		if(msg==null)msg="";
		StringBuffer rb=new StringBuffer();
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp tt=new Timestamp(System.currentTimeMillis());
		String ttStr=format.format(tt);
//		log("Send 1 message:  to everyone."+ttStr);
		
//		rb.append("<script type=\"text/javascript\" msg='<?xml version=\"1.0\" encoding=\"GBK\"?><datasets><table id=\"zuoxizhuangtaimingxijiankong\"><item AgentId=\"6014\" StatuId=\"3\" StatuName=\"就绪\" ContinuedTime=\"93\" TelephoneCode=\"\" DealTime=\"00:01:33\" TelCount=\"21\" phyName=\"张天宠组\" agentName=\"毕涛\"  /><item AgentId=\"5555\" StatuId=\"6\" StatuName=\"话务整理\" ContinuedTime=\"14779\" TelephoneCode=\"\" DealTime=\"04:06:19\" TelCount=\"0\" phyName=\"YY_CC\" agentName=\"测试\"  /><item AgentId=\"6049\" StatuId=\"6\" StatuName=\"话务整理\" ContinuedTime=\"624\" TelephoneCode=\"\" DealTime=\"00:10:24\" TelCount=\"0\" phyName=\"张天宠组\" agentName=\"张天宠\"  /><item AgentId=\"6050\" StatuId=\"4\" StatuName=\"双方通话\" ContinuedTime=\"183\" TelephoneCode=\"13034002333\" DealTime=\"00:03:03\" TelCount=\"5\" phyName=\">张天宠组\" agentName=\"张健061465\"  /><item AgentId=\"8026\" StatuId=\"3\" StatuName=\"就绪\" ContinuedTime=\"1491\" TelephoneCode=\"\" DealTime=\"00:24:51\" TelCount=\"3\" phyName=\"VIP-郭鹏组\" agentName=\"纪萌萌\"  /></table></datasets>' >");
//		rb.append("window.parent.comet.newMessage('"+ttStr+"cccccccccccccccccccccccc就绪就绪就绪就绪就绪就绪就绪就绪','"+System.currentTimeMillis()+"');");		
//		rb.append("</script>");
		
		
////		.print("<script type=\"text/javascript\">");
		
		if(task==null){  //临时
			return rb;
		}
		
		rb.append("<html>  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=gbk\" /> ");
		rb.append("<script id='msgStr' ");
		rb.append("clientId='"+task.clientId+"' ");
		rb.append("threadName='"+Thread.currentThread().getName());
		rb.append("' testnum='"+(++testnum)+"' ");
		rb.append(" tttime='"+System.currentTimeMillis()+"' ");
		rb.append(" msg='" );
		rb.append(msg);
		rb.append("' type='text/javascript'>");
		rb.append("try{");
		rb.append(" window.parent.comet.parseData_(); ");
		rb.append("}catch(e){}");
		rb.append(" </script>");
		//rb.append("</html>");
		return rb;
	}
	
	
	/**
	 * 推数据的页面开始执行函数。
	 * @param msg
	 * @return
	 * 
	 */
	public StringBuffer getReturnStr(String msg){
		if(msg==null)msg="";
		StringBuffer rb=new StringBuffer();
		
//		.print("<script type=\"text/javascript\">");
		rb.append("<html>  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=gbk\" /> ");
		rb.append("<script id='msgStr' ");
		rb.append("clientId='"+task.clientId+"' ");
		rb.append("threadName='"+Thread.currentThread().getName());
		rb.append("' testnum='"+(++testnum)+"' ");
		rb.append(" tttime='"+System.currentTimeMillis()+"' ");
		rb.append(" msg='" );
		rb.append(msg);
		rb.append("' type='text/javascript'>");
		rb.append(" window.parent.comet.parseData_(); ");
		rb.append(" </script>");
		//rb.append("</html>");
		return rb;
	}
	
	public StringBuffer cometoClient(String msg,HttpServletResponse response) throws Exception{
		if(msg==null)msg="";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp tt=new Timestamp(System.currentTimeMillis());
		String ttStr=format.format(tt);
		StringBuffer rb=new StringBuffer();
		PrintWriter writer = response.getWriter();
//		writer.println("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ");
//		response.flushBuffer();
		debug(response.isCommitted()+" "+response.getBufferSize()+" "+this.clientId);
//		response.setBufferSize(1);
//		response.reset();
//		response.resetBuffer();
//		.print("<script type=\"text/javascript\">");
//		rb.append("<html>  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=gbk\" /> ");
//		rb.append("<script id='msgStr' ");
////		rb.append("clientId='"+task!=null && task.clientId!=null?task.clientId:this.clientId+"' ");
//		rb.append("clientId='"+this.clientId+"' ");
//		rb.append("threadName='"+Thread.currentThread().getName());
//		rb.append("' testnum='"+(++testnum)+"' ");
//		rb.append(" tttime='"+System.currentTimeMillis()+"' ");
//		rb.append(" tttimeStr='"+ttStr+"' ");
//		rb.append(" msg='" );
//		rb.append(msg);
//		rb.append("' type='text/javascript'>");
		
		writer.write("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ");
		
		rb.append("<html>  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=gbk\" /> ");
		rb.append("<script id='msgStr' ");
		rb.append("clientId='"+this.clientId+"' ");
		rb.append("threadName='"+Thread.currentThread().getName());
		rb.append("' testnum='"+(++testnum)+"' ");
		rb.append(" tttime='"+System.currentTimeMillis()+"' ");
		rb.append(" msg='" );
		rb.append(msg);
		rb.append("' type='text/javascript'>");
		rb.append("window.parent.comet.newMessage('"+ttStr+"cccccccccccccccccccccccc就绪就绪就绪就绪就绪就绪就绪就绪','"+System.currentTimeMillis()+"');");
//		rb.append(" window.parent.comet.parseData_(); ");
		rb.append(" </script>");
//		rb.append(" type='text/javascript'>");
		
		
		writer.print(rb.toString());
		debug(rb.toString());
//		rb=new StringBuffer();
//		writer.println(" window.parent.comet.parseData_('"+System.currentTimeMillis()+"'); ");
//		writer.println("comet.parseData_('"+System.currentTimeMillis()+"'); ");
//		writer.println("comet.parseData_('"+System.currentTimeMillis()+"'); ");
//		writer.println("comet.newMessage('"+ttStr+"cccccccccccccccccccccccc就绪就绪就绪就绪就绪就绪就绪就绪','"+System.currentTimeMillis()+"');");
//		writer.print("</script>");
//		writer.println("                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                ");
		
		
		writer.flush();
		
//		response.flushBuffer();
//		
//		rb.append(" window.parent.comet.parseData_(); ");
//		rb.append(" </script>");
		//rb.append("</html>");
		return rb;
	}
	
	public String toXml(String userie) throws Exception{
		
		String ret=null;
		
		StringBuffer mgs=new StringBuffer();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mgs.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		mgs.append("<datasets>");
		mgs.append("<table id=\"zuoxileijixinxi\">");
		for(int i=0;i<1000;i++){
			mgs.append("<item id=\""+ (++datanum) +"\"  onlineCount=\""+Utility.getRandomInt(i)+"\" temporaryCount=\""+Utility.getRandomInt(i)+"\" noReadyCount=\""+Utility.getRandomInt(i)+"\"  readyCount=\""+Utility.getRandomInt(i)+"\" ACWCount=\""+Utility.getRandomInt(i)+"\" inboundAccCount=\""+Utility.getRandomInt(i)+"\" outboundCount=\""+Utility.getRandomInt(i)+"\" ringCount=\""+Utility.getRandomInt(i)+"\"/>");
		}
		
		mgs.append("</table>");
		
		
		mgs.append("<table id=\"xiangmuzuhuawuliangfenxi\">");
		for(int i=0;i<1000;i++){
			mgs.append("<item id=\"测试"+ (++datanum) +"\" workDate=\""+ f.format(new Timestamp(System.currentTimeMillis())) +"\" callSkExtCount=\""+i+"\" callSkDistributeCount=\""+i+"\" callSkExtConnectCount=\"105\"  transCount=\"106\" meetingCount=\"107\" colineGiveupCount=\"108\" inlineNoAnsCount=\"109\" callSkExtTalkPeriod=\"110\" onlineAmt=\"111\"/>");
		}
		
		mgs.append("</table>");
		
		mgs.append("</datasets>");
		
		//StringBuffer mgs=new StringBuffer("qwertyuiophygtfredwswdefghyjklkjhtgfedqwswdefrghjklkjhgfdewswdefrghjkll87k6jgfredwsqd35t4y6u7i8");
//		StringBuffer mgs=new StringBuffer("qwertyuiophygtfredwswdefghyjklkjhtgfedqwswdefrghjklkjhgfdewswdefrghjkll87k6jgfredwsqd35t4y6u7i8");
//		for(int i=0;i<15;i++){
//			mgs.append(mgs.toString());
//		}
		
		
		
//		ret="<div id='divStr' threadName='"+Thread.currentThread().getName()+"' testnum='"+(++testnum)+"' vTmp='"+mgs.toString()+"'></div>";
		
//		ret+="<script id='tStr'  type=\"text/javascript\">";
//		ret+="window.parent.comet.tmpStr='"+mgs.toString()+"';";
		ret=null;
		if(task==null || task.clientId==null) return ret;
		//ret+="<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">";
//		writer.println("<html><head><script type=\"text/javascript\">var comet = window.parent.comet;</script></head><body>");
		
		
		ret="<html>  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=gbk\" /> <head></head><body></body><script id='msgStr' clientId='"+task.clientId+"' threadName='"+Thread.currentThread().getName()+"' testnum='"+(++testnum)+"' msg='"+mgs.toString()+"' type=\"text/javascript\">";
	//	ret+="if (USER_IE==USER_IE_TYPE_CHROME) {";
		ret+="var comet = window.parent.comet;";
		ret+="comet.parseData_();";
		
//		ret+="window.parent.comet.parseData_();";
//		ret+="window.comet.parseData_();";
//			for (int i = 0; i < 100; i++) {
//				writer.print("<input type=hidden name=none value=none>");
//			}
//			writer.flush();
		//ret+="}else{" ;
	//	ret+="window.parent.comet.parseData_();";
	//	ret+="}";
		
		
		
		
		//ret+="window.parent.comet.parseData_();";
		ret+="</script></html>";
		
		debug(Thread.currentThread().getName()+"--"+mgs.length()+"-----!!!------"+testnum);
//		debug(ret);
		
		if(ret!=null){
			return Utility.getCodeStr(ret,"UTF-8","GBK");
		}
		return null;
	}
	
	
	/*****数据相关 end *****/
	
	
	public void clearAll(){
		this.T=null;
		this.pageDoc=null;
//		this.pageMap.clear();
//		this.pageIdMap.clear();
		this.agentList.clear();
		this.agentMap.clear();
		this.pageDoc=null;
//		this.xmlDoc=null;
	}
	
//	/**
//	 * 推送数据线程类缓存
//	 */
//	public static Map<String,SendTask> sendMap=new HashMap<String, SendTask>();
	

//	
//	/**
//	 * 初始化、建立推送连接对象
//	 * @param request
//	 * @param response
//	 */
//	public static synchronized ShareCom createShareCom(HttpServletRequest request,HttpServletResponse response){
//		ShareCom sc=null;
//		//客户端标识
//		String clientId=request.getParameter("clientId")==null?"":request.getParameter("clientId");
//		//参数描述串
//		String commXmlStr=request.getParameter("commXmlStr")==null?"":request.getParameter("commXmlStr");
//		//验证对象是否已经存在，若存在则不再生成。
//		sc=ShareComManager.getSharCom(clientId);
//		if(sc!=null){
//			return null;
//		}
//		//若客户端标识为空，则生成一个
//		if("".equals(clientId)){
//			RandomGUID rg=new RandomGUID();
//			clientId=rg.toString();
//			//request.setAttribute("clientId", clientId);
//		}
//		try{
//			System.out.println("----------: clientId " + clientId);
//			//初始化推送参数
//			CometCommandParse ccp=new CometCommandParse(commXmlStr);
//			CometCommand ccd=new CometCommand();
//			ccd.setXmlStr(commXmlStr);
//			ccd.setXmlDoc(ccp.getXmldoc());
//			ccd.setClientId(clientId);
//			ccd.setCommandMap(ccp.getCommandMap());
//			
//			//初始化缓存对象
//			sc=new ShareCom(request,response,ccd,new CometSend());
//			//缓存 
//			synchronized(shareMap){
//				shareMap.put(clientId, sc);
//			}
//		}catch(Exception x){
//			sc=null;
//			x.printStackTrace();
//		}
//		return sc;
//	}
	/**
	 * 移除推送连接对象
	 * @param request
	 * @param response
	 */
	public static void destroyShareCom(HttpServletRequest request,HttpServletResponse response){
		//客户端标识
//		String clientId=request.getParameter("clientId")==null?"":request.getParameter("clientId");
		String clientId=request.getAttribute("clientId")==null?"":request.getAttribute("clientId").toString();
		LogUtil.info("--------------------: destroy :" + clientId,SKIP_Logger);
		ShareComManager tmp=shareMap.remove(clientId);
		if(tmp!=null && tmp.task!=null){
			tmp.task.cleanAll();
			tmp.clearAll();
			tmp.T=null;
			tmp.task=null;
		}else{
			LogUtil.error("ShareComManager is null" + clientId,SKIP_Logger);
		}
		LogUtil.info("------------close is success." + clientId,SKIP_Logger);
	}
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZDesk"); 
}
