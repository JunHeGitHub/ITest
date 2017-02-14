package com.zinglabs.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zinglabs.db.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import net.sf.json.JSONSerializer;


import com.zinglabs.apps.zkmSysdblog.ZkmSysDBLogService;
import com.zinglabs.log.LogUtil;
import com.zinglabs.luceneSearch.LuceneBase;
import com.zinglabs.servlet.modules.ZkmCommons;
import com.zinglabs.servlet.zkmDocTemplate.ZKMDocHelper;
import com.zinglabs.tools.zkmFileImport.ZkmFileImport;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.FileUtils;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;
import com.zinglabs.util.UITreeHelper;
import com.zinglabs.util.WebFormUtil;
import com.zinglabs.util.ZKMAPPUtils;
import com.zinglabs.util.ZKMConfs;
import com.zinglabs.util.DataRelationComponent.DataRelationComponent;
import com.zinglabs.util.excelUtils.ExcelHelper;
import com.zinglabs.util.excelUtils.model.CreateExcelModel;
import com.zinglabs.util.flowUtil.FlowDisponse;
import com.zinglabs.util.flowUtil.FlowHelper;
import com.zinglabs.util.flowUtil.FlowDisponseImp.ZKMFlowDisponse;
import com.zinglabs.util.flowUtil.FlowDisponseImp.ZKMPublicInfoFlowDo;
import com.zinglabs.util.flowUtil.FlowDisponseImp.ZkmPublicInfoFlowDisponse;

public class ZKMDocServlet extends HttpServlet {
	public static  Logger logger = LoggerFactory.getLogger("ZKM"); 
	private static final long serialVersionUID = 2628329819758883023L;
	private String charSet = "";
	public static final String zkmBaseFieldDefined="id,text,recordType,parentId,title,bDate,eDate,keywords,sortPath,summary,filePath,versions,keywords1,keywords2,keywords3,keywords4,keywords5,hotTag,vindicator,isEmailSend,changReason,trueFlag,area,sendIsNotTimely";
	public static String zkmRootPath = ZKMConfs.confs.getProperty("zkmDocSaveDir", "/mnt/zkmDoc");
	public static String zkmBaseField=ZKMConfs.confs.getProperty("zkmBaseFieldDefined", zkmBaseFieldDefined);
	public static final String[] docFileName = { "index.html" };

	public static final String ZKM_BASE_TABLE = "zkmInfoBase";

	//public static String zkmDBId ="ZDesk";//ConfInfo.securityDBId;
	
	public static String zkmDBId ="ZKM";

	public static final String w_noDel = " (isdel='' or isdel ='0') ";

	public static final String w_isDel = " `isdel`='1' ";

	public static final String w_LessEndTime = " `eDate` < now() ";

	public static final String w_notLessEndTime = " ( `eDate` >= now()  or `eDate` is null or eDate='0000-00-00 00:00:00')";

	public static final String w_isFolder = " `recordType`='d' ";

	public static final String w_isSubjectFolder = " `recordType`='dv' ";

	public static final String w_isFaqFolder = " `recordType`='dfaq' ";

	public static final String w_isUproblemFolder = " `recordType`='Uproblem' ";
	
	public static final String w_isUserFavoritesFolder = " `recordType`='userFavorites' ";
	
	public static final String w_isFile = " `recordType`='f' ";

	public static final String ZKM_RECORD_TYPE_FOLDER = "d";

	public static final String ZKM_RECORD_TYPE_SUBJECT_FOLDER = "dv";

	public static final String ZKM_RECORD_TYPE_FILE = "f";

	public static final String superRole = "系统管理员";
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			doActive(req, resp);
		} catch (Exception e) {
			LogUtil.error(e, logger);
			// logger.error(e);
			// e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			doActive(request, response);
		} catch (Exception e) {
			LogUtil.error(e, logger);
		}
	}

	public void init(ServletConfig config) throws ServletException {
		charSet = config.getInitParameter("encoding");
		if (charSet == null) {
			charSet = "UTF-8";
		}
		super.init(config);
	}

	public void doActive(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, Exception {

		response.setCharacterEncoding("GBK");
		request.setCharacterEncoding(charSet);
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		// response.setCharacterEncoding("gbk");
		String operate = request.getParameter("operate") == null ? "" : request.getParameter("operate");
		String docid = "zkmDocXmlDefind";
		String absPath = request.getSession().getServletContext().getRealPath("/");

		if ("".equals(operate)) {
			operate = request.getAttribute("operate") == null ? "" : request.getAttribute("operate").toString();
		}
		// TODO:获取用户集中处理，应该有一个超类来直接管理，这里暂时选这样写吧。
		UserInfo2 user = UserInfo2.getUser(request);
		// System.out.println("____________________------------ operate : " +
		// operate);
		
		try {
			if ("getChild".equals(operate)) {
				getChild(user, docid, absPath, request, response);
			}

			String content = "<?xml version=\"1.0\" encoding=\"gbk\"?>" + request.getParameter("content");
			String contentUTF = new String(content.getBytes("gbk"));
			contentUTF = contentUTF.replaceAll("<\\?xml version=\"1.0\"\\?>", "");
			int ret = -1;
			// 添加一个分类，recordType=d
			if ("addChild".equals(operate)) {
				addChild(user, contentUTF, request, response);
			}
			// 修改一个分类的名称，recordType=d
			// TODO:当分类名称修改时，分类下的记录recordType=f也应该做出相应修改。
			if ("modiChild".equals(operate)) {
				modiChild(user, contentUTF, request, response);
			}
			// 删除一个分类记录，recordType=d
			if ("delChild".equals(operate)) {
				delChild(user, contentUTF, request, response);
			}
			
			if("zkmDocDisponseSearch".equals(operate)){
				zkmDocDisponseSearch(user,request, response);
			}
			
			if("zkmDocDisponseActive".equals(operate)){
				zkmDocDisponseActive(user,request, response);
			}
			
			if("getHotTags".equals(operate)){
				getHotTags(user,request, response);
			}
			
			if("getUsers".equals(operate)){
				getUsers(user,request, response);
			}
			
			if("getFolders".equals(operate)){
				getFolders(user,request, response);
			}
			
			if("setZkmDisponseContent".equals(operate)){
				setZkmDisponseContent(request, response);
			}
			
			if("getZkmDisponseContent".equals(operate)){
				getZkmDisponseContent(request, response);
			}
			
			if("getZKMDocFJFiles".equals(operate)){
				getZKMDocFJFiles(user,request, response);
			}
			
			if("getZKMDocFJFilesHistory".equals(operate)){
				getZKMDocFJFilesHistory(user,request, response);
			}
			
			if("zkmDocSubmitFlow".equals(operate)){
				zkmDocSubmitFlow(user,request, response);
			}
			
			if("getZKMDocWaitDocuments".equals(operate)){
				getZKMDocWaitDocuments(user,request, response);
			}
			
			if("getZKMDocWaitDocNum".equals(operate)){
				getZKMDocWaitDocNum(user,request, response);
			}
			
			if("setZKMDocWaitDocumentsToUser".equals(operate)){
				setZKMDocWaitDocumentsToUser(user,request, response);
			}
			
			if("modiDocsToDisponse".equals(operate)){
				modiDocsToDisponse(user,request, response);
			}
			
			if("getZKMDocRelationId".equals(operate)){
				getZKMDocRelationId(user,request, response);
			}
			
			if("stopZkmDocs".equals(operate)){
				stopZkmDocs(user,request, response);
			}
			
			if("zkmDocDel".equals(operate)){
				zkmDocDel(user,request, response);
			}
			
			if("getVaildateFolders".equals(operate)){
				getVaildateFolders(user,request, response);
			}
			
			if("getZkmFlowHistory".equals(operate)){
				getZkmFlowHistory(user,request, response);
			}
			
			if("zkmPublicInfoDisponseSearch".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.disponseSearch(user, request, response);
			}
			
			if("zkmPublicInfoDisponseActive".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.disponseActive(user, request, response);
			}
			
			if("zkmPublicInfoVaildateExcel".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.decompressAndVaildate(user, request, response);
			}
			
			if("zkmPublicInfoSubmitFlow".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.submitFlow(user, request, response);
			}
			
			if("getPublicInfoWaitInfo".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.getWaitFlowInfo(user, request, response);
			}
			
			if("setWaitPublicInfoToUser".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.setWaitFlowInfoToUser(user, request, response);
			}
			
			if("getPublicWaitInfoNum".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.getWaitFlowInfoNum(user, request, response);
			}
			
			if("zkmPublicInfoDataSearch".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.dataTableDoSearch(user, request, response);
			}
			
			if("zkmPublicInfoDataSearchMh".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.dataTableDoSearchMh(user, request, response);
			}
			
			if("getIdForViewContent".equals(operate)){
				String id=request.getParameter("id")==null?"":request.getParameter("id");
				List dlist=DataRelationComponent.getRelation(id,ZkmCommons.ZKM_FB_RLEATION_KEY);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "", dlist), response);
			}
			
			if("zkmPublicInfoDisponseDelete".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.zkmPublicInfoDisponseDelete(user, request, response);
			}
			
			if("disponseSearch_All".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.disponseSearch_All(user, request, response);
			}
			
			if("publicInfoStop".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.publicInfoStop(user, request, response);
			}
			
			if("publicInfoUNStop".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.publicInfoUNStop(user, request, response);
			}
			
			if("getPublicInfoCJWTListForFolder".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.getPublicInfoCJWTListForFolder(user, request, response);
			}
			
			if("setPublicInfoCJWTCNum".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.setPublicInfoCJWTCNum(request, response);
			}
			
			if("getCJWTFolderNames".equals(operate)){
				ZkmPublicInfoFlowDisponse pif=new ZkmPublicInfoFlowDisponse();
				pif.getCJWTFolderNames(request, response);
			}
			
			if("getZkmDocUserFavoritesList".equals(operate)){
				getZkmDocUserFavoritesList(user, request, response);
			}
			
			if("setUserFavoritesRecord".equals(operate)){
				setUserFavoritesRecord( request, response);
			}
			
			if("delUserFavoritesRecord".equals(operate)){
				delUserFavoritesRecord( request, response);
			}
			
			if("getCommonZkmTree".equals(operate)){
				getCommonZkmTree(user,request, response);
			}
			
			if("zkmUnZipContent".equals(operate)){
				zkmUnZipContent(user,request, response);
			}
			
			if("zkmContentImpSet".equals(operate)){
				zkmContentImpSet(user,request, response);
			}
			
			if("getZKMDisponseContentPath".equals(operate)){
				getZKMDisponseContentPath(user,request, response);
			}
			
			if("setZkmDocSecurity".equals(operate)){
				setZkmDocSecurity(user,request, response);
			}
			
			if("getRolePageAuth".equals(operate)){
				getRolePageAuth(user,request, response);
			}
			
			if("getSysDBConfs".equals(operate)){
				getSysDBConfs(request, response);
			}
			
/*			// 查询知识记录，ID为分类ID,recordType=f
			if ("zkmDocSearch".equals(operate)) {
				zkmDocSearch(request, response, user);
			}
*/
			if ("getSearchDvRecord".equals(operate)) {
				getSearchDvRecord(request, response, user);
			}

			// 添加一条记录
			if ("zkmDocAdd".equals(operate)) {
				zkmDocAdd(user, contentUTF, request, response);

			}

			if ("setDvRecord".equals(operate)) {
				setDvRecord(request, response);
			}

			// 修改记录，逻辑删除也在这里
			// TODO:删除与修改放在了一起，应该分开放，删除只是置一个标识位，没必要重建索引
			if ("zkmDocModi".equals(operate)) {
				zkmDocModi(user, contentUTF, request, response);
			}
			// 彻底删除文档
			// TODO:还要删除相关文件数据。
			if ("zkmDocDel".equals(operate)) {
				zkmDocDel(user, contentUTF, request, response);

			}

			if ("getZkmDoc".equals(operate)) {
				getZkmDoc(request, response);
			}

			if ("delDvRecord".equals(operate)) {
				delDvRecord(request, response);
			}

			// 清空回收站功能
			if ("zkmDocIsDelClear".equals(operate)) {
				zkmDocIsDelClear(user, response);
			}

			if ("isDelRecords".equals(operate)) {
				isDelRecords(user, response);
			}

			if ("upDateDelRecord".equals(operate)) {
				upDateDelRecord(request, response);
			}

			if ("isEndTimeRecords".equals(operate)) {
				isEndTimeRecords(user, request, response);
			}

			if ("setZkmContent".equals(operate)) {
				setZkmContent(request, response);
			}

			if ("getZkmContent".equals(operate)) {
				getZkmContent(request, response);
			}

			if ("securityZKMDataRelation".equals(operate)) {
				securityZKMDataRelation(request, response);
			}

			if ("getZKMDataRelation".equals(operate)) {
				getZKMDataRelation(request, response);
			}

			if ("ZKMDataExport".equals(operate)) {
				ZKMDataExport(request, response);
			}

			if ("delfujianModi".equals(operate)) {
				delfujianModi(user, request, response);
			}

			if ("updateViewNum".equals(operate)) {
				updateViewNum(request,user);
			}

			if ("getUserAttention".equals(operate)) {
				getUserAttention(user, request, response);
			}

			if ("getAttentionSortUsers".equals(operate)) {
				getAttentionSortUsers(user, request, response);
			}

			if ("setUserAttention".equals(operate)) {
				setUserAttention(user, request, response);
			}

			if ("setTreeNodeAdjust".equals(operate)) {
				setTreeNodeAdjust(user, request, response);
			}

			if ("getDocPath".equals(operate)) {
				getDocPath(user, request, response);
			}

			if ("getDocHistoryVersion".equals(operate)) {
				getDocHistoryVersion(user, request, response);
			}

			if ("getRecoverHistoryVersion".equals(operate)) {
				getRecoverHistoryVersion(user, request, response);
			}

			if ("getDocHistoryVersionMatcher".equals(operate)) {
				getDocHistoryVersionMatcher(user, request, response);
			}

			if ("searchTermsSave".equals(operate)) {
				searchTermsSave(request, response);
			}

			if ("getUserAddDocStatic".equals(operate)) {
				getUserAddDocStatic(user, request, response);
			}

			if ("getAddDocStatic".equals(operate)) {
				getAddDocStatic(user, request, response);
			}

			if ("getModiDocStatic".equals(operate)) {
				getModiDocStatic(user, request, response);
			}

			if ("getTreamStatic".equals(operate)) {
				getTreamStatic(user, request, response);
			}

			if ("getStaticDocClickView".equals(operate)) {
				getStaticDocClickView(user, request, response);
			}

			if ("getDocAreaStatic".equals(operate)) {
				getDocAreaStatic(user, request, response);
			}

			if ("getDocSortStatic".equals(operate)) {
				getDocSortStatic(user, request, response);
			}

			if ("getZkmFaqUserAskAnserStatic".equals(operate)) {
				getZkmFaqUserAskAnserStatic(user, request, response);
			}

			if ("zkmDocMaintainStatic".equals(operate)) {
				zkmDocMaintainStatic(user, request, response);
			}
			
			if("zkmSearchRoundStatic".equals(operate)){
				zkmSearchRoundStatic(user, request, response);
			}
			
			if ("zkmDocFolderManagerStatic".equals(operate)) {
				zkmDocFolderManagerStatic(user, request, response);
			}

			if("getAreaSendDocStatic".equals(operate)){
				getAreaSendDocStatic(user, request, response);
			}
			
			if("getZkmSearchModleUseStatic".equals(operate)){
				getZkmSearchModleUseStatic(user, request, response);
			}
			
			if("getZkmFaqHotClickStatic".equals(operate)){
				getZkmFaqHotClickStatic(user, request, response);
			}
			
			if("getZkmFaqAreaStatic".equals(operate)){
				getZkmFaqAreaStatic(user, request, response);
			}
			
			if("getZkmFaqSortStatic".equals(operate)){
				getZkmFaqSortStatic(user, request, response);
			}
			
			if("getZkmLastModiDocSearch".equals(operate)){
				getZkmLastModiDocSearch(user, request, response);
			}
			
			if("getZkmFaqSearch".equals(operate)){
				getZkmFaqSearch(user, request, response);
			}
			
			if("getExportDocSort".equals(operate)){
				getExportDocSort(user, request, response);
			}
			
			if ("saveClickRecord".equals(operate)) {
				saveClickRecord(user, request, response);
			}

			if ("getAGuid".equals(operate)) {
				getAGuid(request, response);
			}

			if ("doFaqSaveFaq".equals(operate)) {
				doFaqSaveFaq(user, request, response);
			}

			if ("doFaqGetFaq".equals(operate)) {
				doFaqGetFaq(user, request, response);
			}

			if ("doFaqDeleteFaq".equals(operate)) {
				doFaqDeleteFaq(user, request, response);
			}

			if ("doFaqChangeState".equals(operate)) {
				doFaqChangeState(user, request, response);
			}

			if("zkmDocFlowJBStatic".equals(operate)){
				zkmDocFlowJBStatic(user, request, response);
			}
			
			if("zkmDocFlowFHStatic".equals(operate)){
				zkmDocFlowFHStatic(user, request, response);
			}
			
			if ("getUserRoleSecurity".equals(operate)) {
				getUserRoleSecurity(user, request, response);
			}

			if ("getDictList".equals(operate)) {
				getDictList(request, response);
			}

			if ("doServerImp".equals(operate)) {
				doServerImp(user,request, response);
			}
			
			if ("doServerImp2".equals(operate)) {
				doServerImp2(user,request, response);
			}
			
			if("getHasSecurityUsers".equals(operate)){
				getHasSecurityUsers(user,request, response);
			}
			
			if ("lookNumDesc".equals(operate)) {
				String order = " order by `lookNum` desc LIMIT 0,1000";
				String sql = "select * from `zkmInfoBase` where 1=1 ";
				sql = sqlSpell(sql, ZKM_RECORD_TYPE_FILE, false, false) + order;

				List list = DAOTools.queryMap(sql, zkmDBId, null);
				String json = JSONSerializer.toJSON(list).toString();
				postStrToClient(json, response);
			}
			if ("viewUpdateGeren".equals(operate)) {
				String json = "";
				if (user != null && user.loginName2 != null && user.loginName2.length() > 0) {
					String order = " order by `lastModiTime` desc LIMIT 0,1000";
					String sql = "select * from `zkmInfoBase` where `createUser`=? ";
					sql = sqlSpell(sql, ZKM_RECORD_TYPE_FILE, false, false) + order;
					String[] param = { user.loginName2 };
					List list = DAOTools.queryMap(sql, param, zkmDBId);
					json = JSONSerializer.toJSON(list).toString();
				}
				postStrToClient(json, response);
			}
			if ("viewUpdateXiaozu".equals(operate)) {
				String json = "";
				/*String organizationCodeStr=user.userGetFieldValue("organizationCode");
				if (user != null && organizationCodeStr != null && organizationCodeStr.length() > 0) {
					String order = " order by `lastModiTime` desc LIMIT 0,1000";
					String sql = "select * from `zkmInfoBase` where `createGroup`=? ";
					sql = sqlSpell(sql, ZKM_RECORD_TYPE_FILE, false, false) + order;
					String[] param = { organizationCodeStr };
					List list = DAOTools.queryMap(sql, param, zkmDBId);
					json = JSONSerializer.toJSON(list).toString();
				}*/
				postStrToClient(json, response);
			}
			if ("viewUpdateXitong".equals(operate)) {
				String order = " order by `lastModiTime` desc LIMIT 0,1000";
				String sql = "select * from `zkmInfoBase` where 1=1 ";
				sql = sqlSpell(sql, ZKM_RECORD_TYPE_FILE, false, false) + order;
				List list = DAOTools.queryMap(sql, zkmDBId, null);
				String json = JSONSerializer.toJSON(list).toString();
				postStrToClient(json, response);
			}
			// 短信发送后，保存发送历史
			if ("smsSendHistorySave".equals(operate)) {
				String data = request.getParameter("datas");
				String rejson = "";
				List dlist;
				try {
					
					dlist = (List) JsonUtils.stringToJSON(data, List.class);
				} catch (Exception x) {
					logger.error(x.getMessage());
					rejson = JsonUtils.genUpdateDataReturnJsonStr(false, "保存失败。");
					postStrToClient(rejson, response);
					return;
				}
				int error = 0;
				if (dlist != null && dlist.size() > 0) {
					for (int i = 0; i < dlist.size(); i++) {
						HashMap mapd = (HashMap) dlist.get(i);
						Iterator it = mapd.entrySet().iterator();
						String sql = "";
						String sqlf = "";
						String sqlv = "";
						String[] params;
						List<String> vlist = new ArrayList();
						while (it.hasNext()) {
							Map.Entry<String, String> e = (Map.Entry) it.next();
							String name = e.getKey();
							String value = e.getValue();
							sqlf += "`" + name + "`,";
							sqlv += "?,";
							if (value == null) {
								value = "";
							}
							vlist.add(value);
						}
						if (sqlf.length() > 0) {
							String ulname = "";
							String utname = "";
							if (user != null) {
								ulname = user.loginName2;
								utname = user.userGetFieldValue("userName");
							}
							sqlf += "`sendRelTime`,`sendUserLoginName`,`sendUserName`";
							sqlv += "now(),'" + ulname + "','" + utname + "'";
							params = new String[vlist.size()];
							for (int k = 0; k < vlist.size(); k++) {
								params[k] = vlist.get(k);
							}
							sql = "insert into suSmsSendInfo ( " + sqlf + " ) values ( " + sqlv + " )";
							try {
								DAOTools.updateForPrepared(sql, params, zkmDBId);
							} catch (Exception x) {
								error++;
								logger.error(x.getMessage());
							}
						}
					}
				}
				rejson = JsonUtils.genUpdateDataReturnJsonStr(true, String.valueOf(error));
				postStrToClient(rejson, response);
				return;
			}
		} finally {
			if(user!=null){
				user.releaseAll();
			}
		}
	}

	public static List getChildList(String id) throws Exception {
		/*String sql = "select * from `zkmInfoBase` where `sortPath` like ? and `recordType`='d'";
		String[] params = { "%" + id + "%" };
		return DAOTools.queryMap(sql, params, zkmDBId);*/
		String sql="select * from `zkmInfoBase` where parentId='" + id + "' and `recordType`='d' and `isdel` <> '1' ";
		return DAOTools.queryMap(sql, "ZDesk");
	}

	public static List getFatherList(String sortPath) throws Exception {
		if (sortPath != null && sortPath.length() > 0) {
			String[] ps = sortPath.split("/");
			String sql = "select * from `zkmInfoBase` where ";
			String sqlWhere = " 1=1 and (";
			for (int i = 0; i < ps.length; i++) {
				if (i + 1 >= ps.length) {
					sqlWhere += " `id`=? )";
				} else {
					sqlWhere += " `id`=? or ";
				}
			}
			sql = sql + sqlWhere;
			logger.info("get father sql : " + sql);
			return DAOTools.queryMap(sql, ps, zkmDBId);
		}
		return null;
	}

	public static void updateZkmDocForMap(String tableName, HashMap<String, String> map, String DBId) throws Exception {
		String sql = "update " + tableName + " set ";
		Iterator it = map.entrySet().iterator();
		ArrayList<String> list = new ArrayList<String>();
		String id = "";
		while (it.hasNext()) {
			Entry<String, String> e = (Entry) it.next();
			String name = e.getKey();
			String value = e.getValue();
			if ("ID".equals(name.toUpperCase())) {
				id = value;
			} else {
				sql += " `" + name + "`=?,";
				list.add(value);
			}
		}

		if (!"".equals(id)) {
			sql = sql.substring(0, sql.length() - 1);
			list.add(id);
			String[] param = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				param[i] = (String) list.get(i);
			}
			sql += " where `id`=?";
			DAOTools.updateForPrepared(sql, param, DBId);
		}
	}

	public static void getZkmDoc(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		String sql = "select * from zkmInfoBase where id='" + id + "'";
		if (id.length() > 0) {
			List list = DAOTools.queryMap(sql.toString(), zkmDBId, null);
			if (list.size() > 0 && list.size() < 2) {
				HashMap map = (HashMap) list.get(0);
				if (map != null) {
					Map rmap = new HashMap();
					rmap.put("data", map);
					rmap.put("success", true);
					postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
				} else {
					JsonUtils.genUpdateDataReturnJsonStr(false, "没有找到相关数据，数据已经被删除。");
				}
			} else {
				JsonUtils.genUpdateDataReturnJsonStr(false, "没有找到相关数据，数据已经被删除。");
			}
		} else {
			JsonUtils.genUpdateDataReturnJsonStr(false, "没有找到相关数据，数据已经被删除。");
		}
	}

	public static HashMap getZkmBaseRecord(String id) throws Exception {
		String sql = "select * from  `zkmInfoBaseDisponseFB` where id=?";
		String[] params = { id };
		List list = DAOTools.queryMap(sql, params, zkmDBId);
		if (list != null && list.size() > 0) {
			return (HashMap) list.get(0);
		}
		return null;
	}

	public static String createZKMFolder(String id, String pid) throws Exception {
		Map pmap = null;
		String path = "";
		String dbPath = "";
		if (pid != null && pid.length() > 0) {
			pmap = getZkmBaseRecord(pid);
		}
		if (pmap != null) {
			dbPath = pmap.get("filePath") == null ? "" : pmap.get("filePath").toString();
		}
		if (dbPath.length() > 0) {
			path = zkmRootPath + dbPath;
		} else {
			path = zkmRootPath;
		}

		path = path + "/" + id;
		dbPath = dbPath + "/" + id;

		File fpath=FileUtils.createFolder(path, false);
		ZKMAPPUtils.fcdirChangeUserAndGroup(fpath);
		logger.info("-------creat zkm folder : " + path);
		logger.info("----save zkm folder path:" + dbPath);

		return dbPath;
	}

	public static void writeContentToFile(String path, String content) throws Exception {
		if (content != null && path != null) {
			String fdir = zkmRootPath + path;
			String fi = "index.html";
			File f = new File(fdir);
			if (!f.exists() || !f.isDirectory()) {
				f.mkdirs();
			}
			ZKMAPPUtils.fcdirChangeUserAndGroup(f);
			f = new File(fdir + "/" + fi);

			if (ZKMDocHelper.zkmDocGen != null) {
				content = ZKMDocHelper.zkmDocGen.getZkmContent(content);
			}
			FileUtils.appendToFile(content, f, false);
			ZKMAPPUtils.dirChangeUserAndGroup(f, true);
		}
	}

	public static String getFileContentForFile(String path) throws Exception {
		if (path != null) {
			return FileUtils.getFileRealContent(path);
		}
		return "";
	}

	public static void indexDocs(String id, boolean isdel) throws Exception {
		if (id != null && id.length() > 0) {
			Thread.sleep(200);
			HashMap map = (HashMap) getZkmBaseRecord(id);
			if (map != null) {
				map.put("infoId", id);
				indexDocs(map, isdel);
			}
		}
	}

	public static void indexDocs(HashMap map, boolean isdel) throws Exception {
		if (map != null) {
			// System.out.println("______________------------- : modiAdd");
			String id = map.get("id") == null ? "" : map.get("id").toString();
			String infoId = map.get("infoId") == null ? "" : map.get("infoId").toString();
			if (id.length() > 0) {
				if (infoId.length() <= 0) {
					map.put("infoId", id);
				}
				// delIndexDocs(id);
				if (!isdel) {
					LuceneSearchHandle.doTaskWriter(LuceneSearchHandle.WRITER_TYPE_INDEX_BASE_ALL, map);
				}
			}
		}
	}

	public static void delIndexDocs(String id) throws Exception {
		if (id != null && id.length() > 0) {
			HashMap map = new HashMap();
			map.put("infoId", id);
			LuceneSearchHandle.doTaskWriter(LuceneSearchHandle.WRITER_TYPE_DEL, map);
		}
	}

	public static String getContentFileName(String path) {
		String reStr = "";
		if (path != null && path.length() > 0) {
			for (String str : docFileName) {
				String filePath = ZKMDocServlet.zkmRootPath + path + "/" + str;
				File file = new File(filePath);
				if (file.exists() && file.isFile()) {
					reStr = ZKMDocServlet.zkmRootPath + path + "/" + str;
					break;
				}
			}
		}
		System.out.print("-------------- : index file : " + reStr);
		logger.info("-------------- : index file : " + reStr);
		return reStr;
	}

	public static void postStrToClient(String json, HttpServletResponse response) {
		try {
			PrintWriter pw = response.getWriter();
			pw.write(json);
			pw.flush();
			pw.close();
		} catch (Exception x) {
			LogUtil.error(x, logger);
		}
	}

	/**
	 * 
	 * @param sql
	 * @param type
	 *            记录类型（recordType）d 目录(d)，f 文件(f)
	 * @param isDel
	 *            是否删除 true 删除数据，false 未删除数据。
	 * @param isLessEndTime
	 *            是否过期 true 过期数据，false未过期数据。
	 * @return
	 */
	public static String sqlSpell(String sql, String type, boolean isDel, boolean isLessEndTime) {
		if (isDel) {
			sql += " and " + w_isDel;
		} else {
			sql += " and " + w_noDel;
		}
		if ("d".equals(type)) {
			sql += " and " + w_isFolder;
		} else if ("dv".equals(type)) {
			sql += " and " + w_isSubjectFolder;
		} else if ("f".equals(type)) {
			sql += " and " + w_isFile;
		} else if ("dfaq".equals(type)) {
			sql += " and " + w_isFaqFolder;
		}else if ("Uproblem".equals(type)) {
			sql += " and " + w_isUproblemFolder;
		}else if("userFavorites".equals(type)){
			sql += " and " + w_isUserFavoritesFolder;
		}

		return sql;
	}

	public static String spellPagingSql(String sql, HttpServletRequest request) {
		String start = request.getParameter("start") == null ? "0" : request.getParameter("start");
		String limit = request.getParameter("limit") == null ? "10" : request.getParameter("limit");
		if (start.length() > 0 && limit.length() > 0) {
			sql = sql + " LIMIT " + start + "," + limit;
		}
		return sql;
	}

	/**
	 * 删除数据相关的文档。
	 * 
	 * @param id
	 */
	public static void ZKMDocDel_delFiles(String id) {

	}

	/**
	 * 拼装权限sql
	 * 
	 * @param user
	 * @param sql
	 * @return
	 * @throws Exception 
	 */
	public static String getSecuritySql(UserInfo2 user, String sql) throws Exception {
		String zmkAdm=user.getUserResMapValue("ZKMAdminSymbol");
		if(zmkAdm!=null){
			//TODO:这里在实际投产要开启
//		if (!user.userResMap.containsKey("ZKMAdminSymbol")) {
			//sql += " and EXISTS (select id from `Z_OrgZKMDocMap`  where `Z_OrgZKMDocMap`.`attrIndex` = '" + user.userGetFieldValue("userId") + "' and `Z_OrgZKMDocMap`.`dataId` = `zkmInfoBase`.`id`)";
		} else {
			return sql;
		}
		return sql;
	}

	public static void getChild(UserInfo2 user, String docid, String absPath, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		String companyId = request.getParameter("companyId") == null ? "" : request.getParameter("companyId");
		String departmentId = request.getParameter("departmentId") == null ? "" : request.getParameter("departmentId");
		String folderType = request.getParameter("folderType") == null ? ZKM_RECORD_TYPE_FOLDER : request.getParameter("folderType");
		String sql = "";
		if ("".equals(id)) {
			id = request.getAttribute("id") == null ? "" : request.getAttribute("id").toString();
		}

		String roleName = user.userGetFieldValue("userRole");
		sql="select * from zkmInfoBase where 1=1 ";
		
		if ("".equals(id) || "ZKM_FOLDER_TREE".equals(id)) {
			sql += " and parentId='' ";
		} else {
			sql += " and parentId='" + id + "' ";
		}
		//公司的处理　
		if(companyId.length()>0){
			sql += " and companyId='" + companyId + "' ";
		}
		//部门的处理
		if(departmentId.length()>0){
			sql += " and departmentId='" + departmentId + "' ";
		}
		//收藏夹的处理
		if(folderType.equals("userFavorites")){
			//收藏夹与权限无关
			sql +=" and createUser='" + user.loginName2 +"' ";
		}else{
			//权限验证
			if (roleName != null && roleName.length() > 0 && roleName.equals("系统管理员")) {
				
			} else {
				//非管理员
				if ("".equals(id) || "ZKM_FOLDER_TREE".equals(id)) {
					sql += " and EXISTS(select `id` from `zkmInfoSecurity` where infoId=zkmInfoBase.id and `roleName`='" + roleName + "') ";
				} else {
					sql += " and  EXISTS(select `id` from `zkmInfoSecurity` where infoId=zkmInfoBase.id and `roleName`='" + roleName + "') ";
				}
			}
		}
		
		// 用户
		sql = sqlSpell(sql, folderType, false, false);
		
		ConfXmlObj xmlObj = null;
		if (ConfXmlObj.XMLObjMap.containsKey(docid)) {
			xmlObj = ConfXmlObj.XMLObjMap.get(docid);
		} else {
			xmlObj = new ConfXmlObj(absPath + "/" + docid + "/" + docid + ".xml");
			ConfXmlObj.XMLObjMap.put(id, xmlObj);
		}
		logger.debug(sql);
		String xml = DAOTools.queryXml(sql.toString(), zkmDBId, xmlObj, docid,true,"");
		logger.info(xml);
		PrintWriter pw = response.getWriter();
		pw.write(xml);
		pw.flush();
		pw.close();
	}

	public static void addChild(UserInfo2 user, String contentUTF, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int ret = -1;
		String pid = "";
		String sortPath = "";
		String recordType = "";
		String id = new RandomGUID().toString();
		WebFormUtil wfu=new WebFormUtil(request);
		pid=wfu.get("parentId");
		sortPath=wfu.get("sortPath");
		if(pid.length()>0){
			if (sortPath.length() > 0) {
				sortPath=sortPath + "/" + id;
			} else {
				sortPath=pid + "/" + id;
			}
		}
		String dbPath = createZKMFolder(id, pid);
		String sql="insert into zkmInfoBase (`id`,`text`,`leaf`,`createUser`,`createTime`,`parentId`,`sortPath`,`recordType`,filePath,companyId,companyName,departmentId,departmentName) values (?,?,?,?,now(),?,?,?,?,?,?,?,?)";
		String [] p={id,wfu.get("text"),wfu.get("leaf"),user.loginName2,pid,sortPath,wfu.get("recordType"),dbPath,wfu.get("companyId"),wfu.get("companyName"),wfu.get("departmentId"),wfu.get("departmentName")};
		String restr = "";
		try{
			DAOTools.updateForPrepared(sql, p, zkmDBId);
			ret=0;
		}catch(Exception x){
			ret=-1;
			logger.error("--- error - addChild : " + x.getMessage());
		}
		
		if (ret == -1) {
			restr = "false";
		} else {
			// 对添加者所属角色进行授权
			if (recordType != null && recordType.length() > 0) {
				ZSecurityManager.setSecurityDataRoleRelationSingle(id, user.userGetFieldValue("userRole"), recordType);
			}
			restr = "true";
		}
		postStrToClient(restr, response);
	}

	public static void modiChild(UserInfo2 user, String contentUTF, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
		}
		WebFormUtil wfu=new WebFormUtil(request);
		if(wfu.get("name").length()>0 && wfu.get("id").length()>0){
			String sql="update `zkmInfoBase` set `text`=?,`lastModiUser`=?,lastModiTime=now() where `id` = ?";
			String[] p={wfu.get("name"),user.loginName2,wfu.get("id")};
			try{
				DAOTools.updateForPrepared(sql, p, zkmDBId);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "分类编辑成功。"), response);
			}catch(Exception x){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "分类编辑由于异常失败。"), response);
				logger.error("分类更新错误------: " + x.getMessage());
				x.printStackTrace();
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "分类编辑失败，缺少参数。"), response);
		}
	}

	public static void delChild(UserInfo2 user, String contentUTF, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		if(wfu.get("id").length()>0){
			String sql="update  `zkmInfoBase` set `isdel`=1,`delUser`=? where id=?";
			String [] p={user.loginName2,wfu.get("id")};
			try{
				DAOTools.updateForPrepared(sql, p, zkmDBId);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "分类删除成功。"), response);
			}catch(Exception x){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "分类删除失败，缺少参数。"), response);
				logger.error("分类删除错误------: " + x.getMessage());
				x.printStackTrace();
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "分类删除由于异常失败。"), response);
		}
	}
/*
	public static void zkmDocSearch(HttpServletRequest request, HttpServletResponse response, UserInfo2 user) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		String sql = "";
		String countSql = "";
		if ("".equals(id)) {
			id = request.getAttribute("id") == null ? "" : request.getAttribute("id").toString();
		}
		if ("".equals(id) || "ZKM_FOLDER_TREE".equals(id)) {
			sql = "select * from zkmInfoBase where parentId='' ";
			countSql = "select count(*) allDataSize from zkmInfoBase where parentId='' ";
		} else {
			sql="select zibfb.* from `DataRelationComponent` drc left join `zkmInfoBaseDisponseFB` zibfb on zibfb.id=drc.`srcId` where drc.`distId`='"+ id+"'";
			countSql = "select count(1) allDataSize from `DataRelationComponent` drc left join `zkmInfoBaseDisponseFB` zibfb on zibfb.id=drc.`srcId` where drc.`distId`='"+ id+"'";
			//sql = "select * from zkmInfoBase where parentId='" + id + "' ";
			//countSql = "select count(*) allDataSize from zkmInfoBase where parentId='" + id + "' ";
		}
		sql = getSecuritySql(user, sql);
		countSql = getSecuritySql(user, countSql);
		//sql = sqlSpell(sql, ZKM_RECORD_TYPE_FILE, false, false);
		//countSql = sqlSpell(countSql, ZKM_RECORD_TYPE_FILE, false, false);
		
		String json = getPageingSearch(countSql, sql, request, user);

		postStrToClient(json, response);
	}
*/
	public static void zkmDocAdd(UserInfo2 user, String contentUTF, HttpServletRequest request, HttpServletResponse response) throws Exception {
		WebFormUtil wfu=new WebFormUtil(request);
		String sqlfields=zkmBaseField + ",createUser,createTime,createRoleName,createGroup";
		List<String> sqlList=new ArrayList<String>();
		String sql="insert into zkmInfoBase (" + sqlfields + ") values (";
		String id = new RandomGUID().toString();
		String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
		String parentId = wfu.get("parentId");
		String dbPath = createZKMFolder(id, parentId);
		String htmlContent = wfu.get("htmlContent");
		if(parentId.length()>0 && dbPath.length()>0){
			String[] fileds=sqlfields.split(",");
			for(int i=0;i<fileds.length;i++){
				String field=fileds[i];
				if(field.length()>0){
					String value=wfu.get(field);
					if("recordType".equals(field)){
						value="f";
					}else if("id".equals(field)){
						value=id;
					}else if("createUser".equals(field)){
						value=user.loginName2;
					}else if("createTime".equals(field)){
						value=date;
					}else if("filePath".equals(field)){
						value=dbPath;
					}else if("createRoleName".equals(field)){
						//value=user.userRole == null ? "" : user.userRole;
					}else if("createGroup".equals(field)){
						//=user.organizationCode == null ? "" : user.organizationCode;
					}
					sqlList.add(value);
					if(i+1>fileds.length){
						sql+="?)";
					}else{
						sql+="?,";
					}
				}
			}
			try{
				DAOTools.updateForPrepared(sql, sqlList, zkmDBId);
				if (dbPath.length() > 0) {
					writeContentToFile(dbPath, htmlContent);
				}
				indexDocs(id, false);
				Map rem = new HashMap();
				rem.put("success", true);
				Map record = getZkmBaseRecord(id);
				if (record != null) {
					rem.put("data", record);
				} else {
					rem.put("success", false);
				}
				postStrToClient(JSONSerializer.toJSON(rem).toString(), response);
			}catch(Exception x){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "文档建立异常失败。"), response);
				logger.error("---- create doc error : " + x.getMessage());
				x.printStackTrace();
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "文档建立失败，缺少参数。"), response);
		}
	}

	public static void zkmDocModi(UserInfo2 user, String contentUTF, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		WebFormUtil wfu=new WebFormUtil(request);
		String sqlfields=zkmBaseField + ",lastModiUser,lastModiTime,lastModiRole,lastModiGroup";
		List<String> sqlList=new ArrayList<String>();
		String sql="update zkmInfoBase ";
		String id = wfu.get("id");
		String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
		String parentId = wfu.get("parentId");
		String dbPath = wfu.get("filePath");
		String htmlContent = wfu.get("htmlContent");
		String isdel = wfu.get("isdel");
		String lookNum = wfu.get("lookNum");
		
	}
	
	public static void zkmDocSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
	}

	public static void zkmDocDel(UserInfo2 user, String contentUTF, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int ret = -1;
		String id = "";
		String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
		String isdel = "";
		String parentId = "";
		id = request.getParameter("id") == null ? "" : request.getParameter("id");
		String reJson = "";
		if (id.length() > 0) {
			try {
				// 必须先删除索引，再删除记录。
				// 删除索引
				delIndexDocs(id);
				// 删除数据
				ZKMDocDel_delFiles(id);// TODO:未实现。
				// 删除记录
				String sql = "delete from `zkmInfoBase` where id=?";
				String[] param = { id };
				DAOTools.updateForPrepared(sql, param, zkmDBId);
				reJson = JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功");
			} catch (Exception x) {
				LogUtil.error(x, logger);
				reJson = JsonUtils.genUpdateDataReturnJsonStr(false, "由于异常，删除失败");
			}
		} else {
			JsonUtils.genUpdateDataReturnJsonStr(false, "没有找到相关数据，数据已经被删除。");
		}
		postStrToClient(reJson, response);
	}

	public static void zkmDocIsDelClear(UserInfo2 user, HttpServletResponse response) throws Exception {
		String rejson = "";
		String sql = "select * from `zkmInfoBase` where " + w_isDel;
		sql = getSecuritySql(user, sql);
		if (sql.length() == 0) {
			rejson = JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限进行操作。");
			postStrToClient(rejson, response);
			return;
		}
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			list = DAOTools.queryMap(sql, zkmDBId, null);
		} catch (Exception x) {
			rejson = JsonUtils.genUpdateDataReturnJsonStr(false, "异常，清除数据失败。");
			postStrToClient(rejson, response);
			return;
		}
		sql = "delete from `zkmInfoBase` where " + w_isDel;
		sql = getSecuritySql(user, sql);
		if (list != null && list.size() > 0) {
			for (HashMap<String, String> map : list) {
				String d_id = map.get("id") == null ? "" : map.get("id").toString();
				if (d_id.length() > 0) {
					// 删除索引
					delIndexDocs(d_id);
					// 删除数据：TODO:未实现
					ZKMDocDel_delFiles(d_id);
				}
			}
			try {
				DAOTools.updateForPrepared(sql, zkmDBId);
				rejson = JsonUtils.genUpdateDataReturnJsonStr(true, "回收站清除成功。");
			} catch (Exception x) {
				rejson = JsonUtils.genUpdateDataReturnJsonStr(false, "异常，清除数据失败。");
			}
		} else {
			rejson = JsonUtils.genUpdateDataReturnJsonStr(true, "回收站清除成功。");
		}
		postStrToClient(rejson, response);
	}

	public static void isDelRecords(UserInfo2 user, HttpServletResponse response) throws Exception {
		String sql = "select * from `zkmInfoBase` where 1=1 and " + w_isFile + " and " + w_isDel;
		sql = getSecuritySql(user, sql);
		String json = "";
		if (sql.length() > 0) {
			List list = DAOTools.queryMap(sql, zkmDBId, null);
			json = JSONSerializer.toJSON(list).toString();
		} else {
			json = JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限进行操作。");
		}
		postStrToClient(json, response);
	}

	public static void upDateDelRecord(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		String sql = "update `zkmInfoBase` set `isdel`='0' where id=?";
		String[] param = { id };
		String rejson = "";
		try {
			DAOTools.updateForPrepared(sql, param, zkmDBId);
			rejson = JsonUtils.genUpdateDataReturnJsonStr(true, "数据恢复成功。");
		} catch (Exception x) {
			rejson = JsonUtils.genUpdateDataReturnJsonStr(false, "异常，数据恢复失败。");
		}
		postStrToClient(rejson, response);
	}

	public static void isEndTimeRecords(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sql = "select * from `zkmInfoBase` where 1=1 ";
		String countSql = "select count(*) from `zkmInfoBase` where 1=1 ";
		sql = sqlSpell(sql, ZKM_RECORD_TYPE_FILE, false, true);
		sql = getSecuritySql(user, sql);
		countSql = sqlSpell(countSql, ZKM_RECORD_TYPE_FILE, false, true);
		countSql = getSecuritySql(user, countSql);
		String json = getPageingSearch(countSql, sql, request, user);
		postStrToClient(json, response);
	}
	
	public static void setZkmContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String docId = request.getParameter("id") == null ? "" : request.getParameter("id");
		String data = request.getParameter("data") == null ? "" : request.getParameter("data");
		HashMap zmap = null;
		if (docId.length() > 0) {
			zmap = getZkmBaseRecord(docId);
		}
		if (zmap != null) {
			String path = zmap.get("filePath") == null ? "" : zmap.get("filePath").toString();
			if (path.length() > 0) {
				writeContentToFile(path, data);
			}
			indexDocs(zmap, false);
		}
		// ZkmHistoryHandle.doSaveToHistory(docId);
		String restr = "{'sucess':'" + true + "','id':'" + docId + "'}";
		String json = JSONSerializer.toJSON(restr).toString();
		postStrToClient(json, response);
	}

	public static void getZkmContent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String docId = request.getParameter("id") == null ? "" : request.getParameter("id");
		String cnt = "";
		HashMap zmap = null;
		if (docId.length() > 0) {
			zmap = getZkmBaseRecord(docId);
		}
		if (zmap != null) {
			String path = zmap.get("filePath") == null ? "" : zmap.get("filePath").toString();
			if (path.length() > 0) {
				path = getContentFileName(path);
				cnt = getFileContentForFile(path);
			}
		}
		HashMap remap = new HashMap();
		remap.put("data", cnt);
		remap.put("id", docId);
		String json = JSONSerializer.toJSON(remap).toString();
		postStrToClient(json, response);
	}

	public static void securityZKMDataRelation(String ids, String roleName, String type) throws Exception {
		if (ids != null && ids.length() > 0 && roleName != null && type != null && roleName.length() > 0 && type.length() > 0) {
			String[] ids_ = ids.split(";");
			for (String id : ids_) {
				ZSecurityManager.setSecurityDataRoleRelationSingle(id, roleName, type);
			}
		}
	}

	public static void securityZKMDataRelation(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String roleList = request.getParameter("roleList") == null ? "" : request.getParameter("roleList");
		String sortPath = request.getParameter("sortPath") == null ? "" : request.getParameter("sortPath");
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		if (roleList.length() > 0 && id.length() > 0) {
			List<String> rlist = null;
			List uplist = new ArrayList();
			String delWhere = "";
			if (roleList.length() > 0) {
				//rlist = JsonUtils.converJsonArrayString(roleList);
				List list=(List) JsonUtils.stringToJSON(roleList, List.class);
			}
			if (rlist != null && rlist.size() > 0) {
				String type = ZSecurityManager.DATA_RELATION_TYPE_ZKM;
				delWhere = " and ( `infoId`='" + id + "' ";
				for (String rname : rlist) {
					HashMap upm = new HashMap();
					upm.put("infoId", id);
					upm.put("roleName", rname);
					upm.put("dataType", type);
					uplist.add(upm);
				}
				List<HashMap<String, String>> clist = getChildList(id);
				if (clist != null && clist.size() > 0) {
					for (HashMap map : clist) {
						String cid = (String) map.get("id");
						if (id.equals(cid)) {
							continue;
						}
						delWhere += " or `infoId`='" + cid + "' ";
						for (String rname : rlist) {
							HashMap upm = new HashMap();
							upm.put("infoId", cid);
							upm.put("roleName", rname);
							upm.put("dataType", type);
							uplist.add(upm);
						}
					}
				}
				delWhere += " ) ";
				List<HashMap> flist = getFatherList(sortPath);
				if (flist != null && flist.size() > 0) {
					for (HashMap map : flist) {
						String fid = (String) map.get("id");
						if (id.equals(fid)) {
							continue;
						}
						for (String rname : rlist) {
							HashMap upm = new HashMap();
							upm.put("infoId", fid);
							upm.put("roleName", rname);
							upm.put("dataType", type);
							uplist.add(upm);
						}
					}
				}
				String reJson = JsonUtils.genUpdateDataReturnJsonStr(true, "知识分类授权成功。");
				try {
					ZSecurityManager.setZKMSecurityDataRoleRelation(id, uplist, delWhere);
				} catch (Exception x) {
					reJson = JsonUtils.genUpdateDataReturnJsonStr(false, "异常，授权失败。");
					LogUtil.error(x, logger);
				}
				postStrToClient(reJson, response);
			}
		}
	}

	public static void getZKMDataRelation(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		String jsonStr = "";
		if (id.length() > 0) {
			List<Map> list = ZSecurityManager.getSecurityDataRoleRelation(id, ZSecurityManager.DATA_RELATION_TYPE_ZKM);
			if (list != null && list.size() > 0) {
				jsonStr = JSONSerializer.toJSON(list).toString();
			} else {
				jsonStr = "false";
			}
		}
		postStrToClient(jsonStr, response);
	}

	public static void ZKMDataExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String idStr = request.getParameter("id") == null ? "" : request.getParameter("id");
		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		type=type.toUpperCase();
		if (idStr.length() > 0 && type.length()>0) {
			String[] ids = idStr.split(";");
			HashMap map = ZkmExportHandle.exportDoc(ids,type);
			LogUtil.debug("----------------exprot zip map done.", logger);
			String name = map.get("expName") == null ? "export.zip" : map.get("expName").toString();
			String fp = map.get("expFile") == null ? "" : (String) map.get("expFile");
			File file=null;
			if(fp.length()>0){
				file=new File(fp);
			}
			String reJson = "";
			if (file != null && file.exists()) {
				String server_id=ZKMConfs.confs.getProperty("ZKM_CLUSTER_ID","185");
				HashMap remap = new HashMap();
				remap.put("success", true);
				remap.put("infoId", idStr);
				remap.put("name", name);
				remap.put("file", file.getPath());
				remap.put("mimetype", "application/zip");
				remap.put("server_id", server_id);
				reJson = JSONSerializer.toJSON(remap).toString();
			} else {
				reJson = JsonUtils.genUpdateDataReturnJsonStr(false, "没有找到知识文档或知识文档已经被删除，导出中止。");
			}
			LogUtil.debug("----------------exprot zip reJson done. ：" + reJson, logger);
			postStrToClient(reJson, response);
		}
	}

	public static void updateViewNum(HttpServletRequest request,UserInfo2 user) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		String area = request.getParameter("area") == null ? "" : request.getParameter("area");
		String parentId = request.getParameter("parentId") == null ? "" : request.getParameter("parentId");
		if(id.length()>0){
			String usern=user.loginName2==null?"":user.loginName2;
			ZZkmRecordClickUtils.insertClickRecord(ZZkmRecordClickUtils.INFO_TYPE_ZKM, id, "", "", "f",usern,request);
			if (area.trim().length() > 0) {
				ZZkmRecordClickUtils.insertClickRecord(ZZkmRecordClickUtils.INFO_TYPE_ZKM, id, "area", area, "f",usern,request);
			}
			if (parentId.length() > 0) {
				ZZkmRecordClickUtils.zkmDocFolderClickSave(id,parentId,usern,request);
			}else{
				List dlist=DataRelationComponent.getRelation(id, ZkmCommons.ZKM_FB_RLEATION_KEY);
				if(dlist!=null && dlist.size()>0){
					for(int i=0;i<dlist.size();i++){
						Map map=(HashMap)dlist.get(i);
						String did=map.get("distId")==null?"":(String)map.get("distId");
						if(did.length()>0){
							ZZkmRecordClickUtils.zkmDocFolderClickSave(id,did,usern,request);
						}
					}
				}
			}
		}
	}

	public static void getSearchDvRecord(HttpServletRequest request, HttpServletResponse response, UserInfo2 user) throws Exception {
		if(user==null){
			postStrToClient("[]", response);
			return;
		}
		String id = request.getParameter("id") == null ? "-1" : request.getParameter("id");
		if ("".equals(id)) {
			id = request.getAttribute("id") == null ? "-1" : request.getAttribute("id").toString();
		}

		// TODO:这里需要补全权限sql,优化left join语句
		String sql = "";
		String countsql = "";
		String zmkAdm=user.getUserResMapValue("ZKMAdminSymbol");
		if(zmkAdm!=null){
//		if (!user.userResMap.containsKey("ZKMAdminSymbol")) {
			sql = "select zib.* from `DataRelationComponent` drc left join `zkmInfoBase` zib on drc.`distId` = zib.`id` where drc.srcid='" + id + "'";
			countsql = "select count(1) allDataSize from `DataRelationComponent` drc left join `zkmInfoBase` zib on drc.`distId` = zib.`id` where drc.srcid='" + id + "'";
		} else {
			sql = "select zib.* from `DataRelationComponent` drc left join `zkmInfoBase` zib on drc.`distId` = zib.`id` where drc.srcid='" + id + "'";
			countsql = "select count(1) allDataSize from `DataRelationComponent` drc left join `zkmInfoBase` zib on drc.`distId` = zib.`id` where drc.srcid='" + id + "'";
		}

		String json = getPageingSearch(countsql, sql, request, user);
		postStrToClient(json, response);
	}

	public static void delDvRecord(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
		String reJson = JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功。");
		if (id.length() > 0 && ids.length() > 0) {
			String[] pIds = ids.split(",");
			try {
				DataRelationComponent.delRelation(id, pIds, "zkm_vd");
			} catch (Exception x) {
				reJson = JsonUtils.genUpdateDataReturnJsonStr(false, "删除由于异常失败。");
			}
		}
		postStrToClient(reJson, response);
	}

	public static void setDvRecord(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
		String reJson = JsonUtils.genUpdateDataReturnJsonStr(true, "添加成功。");
		if (id.length() > 0 && ids.length() > 0) {
			String[] pIds = ids.split(",");
			try {
				DataRelationComponent.setRelation(id, pIds, "zkm_vd",false);
			} catch (Exception x) {
				reJson = JsonUtils.genUpdateDataReturnJsonStr(false, "添加由于异常失败。");
			}
		}
		postStrToClient(reJson, response);
	}

	public static void setUserFavoritesRecord(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
		String reJson = JsonUtils.genUpdateDataReturnJsonStr(true, "添加成功。");
		if (id.length() > 0 && ids.length() > 0) {
			String[] pIds = ids.split(",");
			try {
				DataRelationComponent.setRelation(id, pIds, "userFavorites",false);
			} catch (Exception x) {
				reJson = JsonUtils.genUpdateDataReturnJsonStr(false, "添加由于异常失败。");
			}
		}
		postStrToClient(reJson, response);
	}
	
	public static void delUserFavoritesRecord(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
		String reJson = JsonUtils.genUpdateDataReturnJsonStr(true, "删除成功。");
		if (id.length() > 0 && ids.length() > 0) {
			String[] pIds = ids.split(",");
			try {
				DataRelationComponent.delRelation(id, pIds, "userFavorites");
			} catch (Exception x) {
				reJson = JsonUtils.genUpdateDataReturnJsonStr(false, "删除由于异常失败。");
			}
		}
		postStrToClient(reJson, response);
	}
	
	public static String getPageingJson(String total, List data) {
		Map map = new HashMap();
		map.put("allDataSize", total);
		map.put("rows", data);
		return JSONSerializer.toJSON(map).toString();
	}

	//
	public static String getPageingSearch(String countSql, String sql, HttpServletRequest request, UserInfo2 user) throws Exception {
		String count = "0";
		List dataList = new ArrayList();

		/*
		 * HashMap<String, DataFilter> filtersMap=new HashMap<String,
		 * DataFilter>(); if(!user.userResMap.containsKey("ZKMAdminSymbol")){
		 * DataFilter filterOrg=null;
		 * if(user.tableFilterMap.containsKey("Z_OrgZKMDocMap")){
		 * filterOrg=user.tableFilterMap.get("Z_OrgZKMDocMap"); }else{ // String
		 * tableName,String sql,String dbID,String idName filterOrg=new
		 * DataFilter("Z_OrgZKMDocMap","select dataId from Z_OrgZKMDocMap where
		 * winName ='zhishikufenleiguanli' and
		 * attrIndex="+user.userId+"",zkmDBId,"dataId"); }
		 * filterOrg.initFilter(); filterOrg.srcIdName="id";
		 * filterOrg.filterName="dataId"; filtersMap.put("Z_OrgZKMDocMap",
		 * filterOrg); } List list = DAOTools.queryMap(countSql.toString(),
		 * zkmDBId,filtersMap);
		 */
		List list = DAOTools.queryMap(countSql.toString(), zkmDBId);
		if (list != null && list.size() > 0) {
			Map map = (Map) list.get(0);
			count = (String) map.get("allDataSize");
			if (count == null || count.length() <= 0) {
				count = (String) map.get("count(*)");
			}
			if (count != null && count.length() > 0) {
				sql = spellPagingSql(sql, request);
				// dataList = DAOTools.queryMap(sql, zkmDBId,filtersMap);
				dataList = DAOTools.queryMap(sql, zkmDBId);
			} else {
				count = "0";
			}
		}
		return getPageingJson(count, dataList);
	}

	public static void getUserAttention(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if ("_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "未登录，获取失败"), response);
		} else {
			String lu = user.loginName2;
			if (lu != null && lu.length() > 0) {
				String sql = "select * from `zkmUserAttentionSet` where `loginName`=?";
				List qlist = DAOTools.queryMap(sql, new String[] { lu }, zkmDBId);
				String restr = JSONSerializer.toJSON(qlist).toString();
				postStrToClient(restr, response);
			} else {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "非法访问。"), response);
			}
		}
	}

	public static void getAttentionSortUsers(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if ("_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "未登录，获取失败"), response);
		} else {
			String id = request.getParameter("id") == null ? "" : request.getParameter("id");
			if (id.length() > 0) {
				String sql = "select * from `zkmUserAttentionSet` where `infoID`=?";
				List qlist = DAOTools.queryMap(sql, new String[] { id }, zkmDBId);
				Map rmap = new HashMap();
				rmap.put("success", true);
				rmap.put("data", qlist);
				postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
			} else {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "非法访问。"), response);
			}
		}
	}

	public static void setUserAttention(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if ("_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "未登录，设置失败"), response);
		} else {
			String delsql = "delete from `zkmUserAttentionSet` where `loginName`=?";

			String sql = "insert into `zkmUserAttentionSet` (`loginName`,`infoID`) values (?,?)";
			String lu = user.loginName2;
			if (lu != null && lu.length() > 0) {
				String[] dp = { lu };
				List dlist = new ArrayList();
				dlist.add(dp);
				String infoIds = request.getParameter("infoIds") == null ? "" : request.getParameter("infoIds");
				List list = new ArrayList();
				if (infoIds.length() > 0) {
					String[] ids = infoIds.split(";");
					for (String id : ids) {
						if (id.length() > 0) {
							String[] p = { lu, id };
							list.add(p);
						}
					}
					try {
						DAOBatchUpdate dbu = new DAOBatchUpdate(zkmDBId);
						dbu.batchUpdate(delsql, dlist, false, false);
						dbu.batchUpdate(sql, list, true, true);
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "设置成功。"), response);
					} catch (Exception x) {
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "由于异常设置失败。"), response);
					}
				}
			} else {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "非法访问。"), response);
			}
		}
	}

	public static void setTreeNodeAdjust(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String index = request.getParameter("index") == null ? "" : request.getParameter("index");
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		String pid = request.getParameter("pid") == null ? "" : request.getParameter("pid");
		String sortPath = request.getParameter("sortPath") == null ? "" : request.getParameter("sortPath");
		String ids = request.getParameter("ids") == null ? "" : request.getParameter("ids");
		String indexs = request.getParameter("indexs") == null ? "" : request.getParameter("indexs");
		String[] ids_ = null;
		String[] indexs_ = null;
		if (ids.length() > 0) {
			ids_ = ids.split(";");
		}
		if (indexs.length() > 0) {
			indexs_ = indexs.split(";");
		}
		if ("_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "未登录，无权进行设置"), response);
		}
		if (index.length() > 0 && id.length() > 0) {
			String sql = "update `zkmInfoBase` set `parentId`=?,`sortField`=?,`lastModiTime`=now(),`sortPath`=?," + "`lastModiUser`=? where `id`=?";
			String[] pm = { pid, index, sortPath, user.loginName2, id };
			try {
				DAOTools.updateForPrepared(sql, pm, zkmDBId);
				if (ids_ != null && indexs_ != null && ids_.length == indexs_.length) {
					sql = "update `zkmInfoBase` set `sortField`=? where `id`=?";
					for (int i = 0; i < ids_.length; i++) {
						String[] pm1 = { indexs_[i], ids_[i] };
						DAOTools.updateForPrepared(sql, pm1, zkmDBId);
					}
				}
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "设置成功。"), response);

				// 更新点击记录表。
				/*
				 * sql="update `zkmRecordClickInfo` set dataValue=? where
				 * relId=? and dataType='f' and dataField='parentId'"; String[]
				 * pmc={pid,id}; DAOTools.updateForPrepared(sql, pm, zkmDBId);
				 */

			} catch (Exception x) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "由于异常设置失败。"), response);
			}
		}
	}

	public static void getDocPath(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		List list=DataRelationComponent.getRelation(id, ZkmCommons.ZKM_FB_RLEATION_KEY);
		StringBuffer path = new StringBuffer();
		if(list!=null && list.size()>0){
			for(int j=0;j<list.size();j++){
				Map map=(Map)list.get(j);
				String did=map.get("distId")==null?"":(String)map.get("distId");
				if (did.length() > 0) {
					String sql = "select `id`,`text`,`parentId`,`recordType` from `zkmInfoBase` where `id`=?";
					List plist = new ArrayList();
					getDocPath(did, sql, plist);
					if (plist != null && plist.size() > 0) {
						for (int i = plist.size() - 1; i >= 0; i--) {
							Map mapp = (Map) plist.get(i);
							String rt=mapp.get("recordType")==null?"":(String)mapp.get("recordType");
							if (rt.length() > 0 && "f".equals(rt)) {
								continue;
							}
							path.append(mapp.get("text") + "/");
						}
					} else {
						path.append("根目录");
					}
					path.append(";");
				}
			}
		} else {
			path.append("没有找到该文档对应的有效文档分类");
		}
		Map rmap = new HashMap();
		rmap.put("path", path.toString());
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}

	public static void getDocPath(String id, String sql, List plist) throws Exception {
		if (id != null && id.length() > 0 && sql != null && sql.length() > 0) {
			String[] pm = { id };
			// List list=DAOTools.queryMap(sql, pm, "ZDesk");
			List list = DAOTools.queryMap(sql, pm, zkmDBId);
			if (list != null && list.size() > 0) {
				Map map = (Map) list.get(0);
				if (map != null) {
					String pid = (String) map.get("parentId");
					plist.add(map);
					if (pid != null && pid.length() > 0) {
						getDocPath(pid, sql, plist);
					}
				}
			}
		}
	}

	public static void getDocHistoryVersion(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		Map rmap = new HashMap();
		if (id.length() > 0) {
			rmap.put("msg", "");
			Map map = ZkmHistoryHandle.getHistoryVersion(id);
			rmap.put("data", map);
		} else {
			rmap.put("msg", "缺少参数，无法获取数据。");
		}
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}

	public static void getRecoverHistoryVersion(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		String hid = request.getParameter("hid") == null ? "" : request.getParameter("hid");
		if (id.length() > 0 && hid.length() > 0) {
			try {
				ZkmHistoryHandle.doRecoverVersion(id, hid, user);
				// 重建索引
				indexDocs(id, false);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "恢复成功"), response);
			} catch (Exception x) {
				LogUtil.error(x, logger);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "恢复失败，异常。"), response);
			}
		} else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "恢复失败，缺少必要参数或没有权限。"), response);
		}
	}

	public static HashMap getSecurityDocMap(UserInfo2 user) throws Exception {
		HashMap<String, DataFilter> filtersMap = new HashMap<String, DataFilter>();
		if (user != null && !"_guest".equals(user.loginName2)) {
			String zmkAdm=user.getUserResMapValue("ZKMAdminSymbol");
			if(zmkAdm!=null){
//			if (!user.userResMap.containsKey("ZKMAdminSymbol")) {
				DataFilter filterOrg = null;
				if (user.tableFilterMap.containsKey("Z_OrgZKMDocMap")) {
					filterOrg = user.tableFilterMap.get("Z_OrgZKMDocMap");
				} else {
					// String tableName,String sql,String dbID,String idName
					filterOrg = new DataFilter("Z_OrgZKMDocMap", "select dataId from Z_OrgZKMDocMap where   winName ='zhishikufenleiguanli' and attrIndex=" +  user.userGetFieldValue("userId") + "", zkmDBId, "dataId");
				}
				filterOrg.initFilter();
				filterOrg.srcIdName = "id";
				filterOrg.filterName = "dataId";
				filtersMap.put("Z_OrgZKMDocMap", filterOrg);
			}
		}
		return filtersMap;
	}

	public static List getDocListFromFilterMap(UserInfo2 user, List<Map> dataList) throws Exception {
		List rlist = new ArrayList();
		if (user != null && dataList != null && dataList.size() > 0) {
			String sql = "select  DISTINCT dataId from `Z_OrgZKMDocMap`  where `Z_OrgZKMDocMap`.`attrIndex` = '" + user.userGetFieldValue("userId") + "'";
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			String zmkAdm=user.getUserResMapValue("ZKMAdminSymbol");
			logger.debug("do getSecurityFilterList ---------------------------------------------" + zmkAdm);
			if(zmkAdm==null || zmkAdm.length()<=0){
				logger.debug("do getSecurityFilterList --------------------------------------------- filter");
//			if (!user.userResMap.containsKey("ZKMAdminSymbol")) {
				try {
					con = DAOTools.getConnectionOutS(zkmDBId);
					ps = con.prepareStatement(sql);
					rs = ps.executeQuery();
					while (rs.next()) {
						String did = rs.getString(1);
						for (Map<String, String> map : dataList) {
							String id = map.get("id");
							String ids=map.get("lucene_saveIds")==null?"":map.get("lucene_saveIds");
							if(ids.length()>0){
								if(ids.indexOf(did)>=0){
									rlist.add(map);
									break;
								}
							}else{
								if (did.equals(id)) {
									rlist.add(map);
									break;
								}
							}
						}
					}
				} catch (Exception x) {
					LogUtil.error(x, logger);
					logger.debug("ZKM DOC sucerity filter error : " + x.getMessage());
				} finally {
					if (rs != null) {
						rs.close();
					}
					if (ps != null) {
						ps.close();
					}
					if (con != null) {
						con.close();
					}
				}
			} else {
				logger.debug("do getSecurityFilterList --------------------------------------------- return all.");
				rlist = dataList;
			}
		}
		return rlist;
		//return dataList;
	}

	public static void getAGuid(HttpServletRequest request, HttpServletResponse response) {
		Map rmap = new HashMap();
		String gid = new RandomGUID().toString();
		rmap.put("gid", gid);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}

	public static void doFaqSaveFaq(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String uuid = request.getParameter("uuid") == null ? "" : request.getParameter("uuid");
		String title = request.getParameter("title") == null ? "" : request.getParameter("title");
		String content = request.getParameter("content") == null ? "" : request.getParameter("content");
		String recordType = request.getParameter("recordType") == null ? "" : request.getParameter("recordType");
		String parentId = request.getParameter("parentId") == null ? "" : request.getParameter("parentId");
		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		String isHead = request.getParameter("isHead") == null ? "" : request.getParameter("isHead");
		String faqState = request.getParameter("faqState") == null ? "" : request.getParameter("faqState");
		String area = request.getParameter("area") == null ? "" : request.getParameter("area");
		String sql = "";
		String[] param = null;
		if ("new".equals(type)) {
			String[] tp = { uuid, title, content, parentId, recordType, user.loginName2 ,area};
			param = tp;
			sql = "INSERT INTO `zkmFaq` (`uuid`,`title`,`content`,`parentId`,`recordType`,`createDate`,`createUser`,area) values (?,?,?,?,?,now(),?,?)";
			// 保存提问或回答记录
			if (uuid.length() > 0 && recordType.length() > 0 && user != null && user.loginName2 != null && user.loginName2.length() > 0) {
				ZZkmRecordClickUtils.insertClickRecord(ZZkmRecordClickUtils.INFO_TYPE_ZKMFAQ, uuid, "createUser", user.loginName2, recordType,user.loginName2,request);
			}
		} else if ("update".equals(type)) {
			if ("update".equals(type) && uuid.length() <= 0) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作失败，缺少必要参数。"), response);
			}
			String[] tp = { title, content, isHead, user.loginName2, faqState,area, uuid };
			param = tp;
			sql = "UPDATE `zkmFaq` SET `title` = ?,`content` = ?,`isHead`=?,`modiDate` = now(),`modiUser` = ?,faqState=?,area=? where `uuid`=?";
		}
		if (!"".equals(sql)) {
			try {
				DAOTools.updateForPrepared(sql, param, zkmDBId);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "操作成功。"), response);
			} catch (Exception x) {
				LogUtil.error(x, logger);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作失败，异常。"), response);
			}
		} else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作失败，缺少必要参数。"), response);
		}
	}

	public static void doFaqGetFaq(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map rmap = new HashMap();
		String fields = " `ID`,`uuid`,`title`,`content`,`parentId`,`recordType`,`isHead`,`createDate`,`createUser`,`modiDate`,`modiUser`,`isDel`,`delUser`,`faqState`,`area` ";
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		if (id.length() > 0) {
			String sql = "select " + fields + " from `zkmFaq` where `uuid`=? and isDel='0'";
			String[] p = { id };
			List<Map> list = DAOTools.queryMap(sql, p, zkmDBId);
			if (list != null && list.size() > 0) {
				Map map = list.get(0);
				rmap.put("master", map);
				sql = "select " + fields + " from `zkmFaq` where `parentId`=? and isDel='0' order by `zkmFaq`.`isHead` desc, `zkmFaq`.`createDate` desc";
				String[] p1 = { id };
				List<Map> listc = DAOTools.queryMap(sql, p, zkmDBId);
				if (listc != null) {
					rmap.put("child", listc);
				}
			}
		} else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作失败，缺少必要参数。"), response);
		}
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}

	public static void doFaqDeleteFaq(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ids_ = request.getParameter("ids") == null ? "" : request.getParameter("ids");
		if (ids_.length() > 0) {
			String[] ids = ids_.split(";");
			String sql = "update `zkmFaq` SET `isDel`='1' where uuid=?";
			for (int i = 0; i < ids.length; i++) {
				String[] p = { ids[i] };
				DAOTools.updateForPrepared(sql, p, zkmDBId);
			}
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "操作已成功。"), response);
		} else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作失败，缺少必要参数。"), response);
		}
	}

	public static void doFaqChangeState(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("ids") == null ? "" : request.getParameter("ids");
		String state = request.getParameter("state") == null ? "" : request.getParameter("state");
		if (id.length() > 0 && state.length() > 0) {
			String sql = "update `zkmFaq` set  `faqState`=? where uuid=?";
			String[] p = { state, id };
			try {
				DAOTools.updateForPrepared(sql, p, zkmDBId);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "操作成功"), response);
			} catch (Exception x) {
				LogUtil.error(x, logger);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作失败，异常"), response);
			}
		} else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "操作失败，缺少必要参数。"), response);
		}
	}

	public static void getUserRoleSecurity(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user != null && !"_guest".equals(user.loginName2)) {
			try {
				List<String> list = SSCDBTools.loadUserResList(user);
				Map rm = new HashMap();
				rm.put("success", true);
				rm.put("data", list);
				postStrToClient(JSONSerializer.toJSON(rm).toString(), response);
			} catch (Exception x) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "读取失败，异常"), response);
			}
		} else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "非法访问－用户未登录。"), response);
		}
	}

	public static void getDocHistoryVersionMatcher(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id") == null ? "" : request.getParameter("id");
		Map rmap = new HashMap();
		if (id.length() > 0) {
			String fid = "";
			String[] ids = id.split(";");
			List clist = new ArrayList();
			for (String aid : ids) {
				if (aid != null && aid.length() > 0) {
					Map<String, String> cmap = ZkmHistoryHandle.getHistoryVersion(aid);
					if (cmap != null) {
						String fileContent=cmap.get("fileContent")==null?"":(String)cmap.get("fileContent");
						if(fileContent.length()>0){
							cmap.put("fileContent", ZKMAPPUtils.getFixZKMDisponseContentPath(fileContent));
						}
						clist.add(cmap);
					}
				}
			}
			rmap.put("historyInfo", clist);
			rmap.put("success", true);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		} else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数，查询失败。"), response);
		}
	}

	public static void searchTermsSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String terms = request.getParameter("terms") == null ? "" : request.getParameter("terms");
		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		String departmentId = request.getParameter("departmentId") == null ? "" : request.getParameter("departmentId");
		String companyId = request.getParameter("companyId") == null ? "" : request.getParameter("companyId");
		if (terms.length() > 0 && type.length() > 0) {
			String sql = "insert into `zkmSearchStatic` (`searchType`,`fieldName`,`searchContent`,`searchDate`,uuid,fieldNameView,departmentId,companyId) values (?,?,?,now(),?,?,?,?) ";
			List list = null;
			try {
				list = (List) JsonUtils.stringToJSON(terms, List.class);
			} catch (Exception x) {
				logger.error("-- save query terms error: ");
				logger.error("-- save query terms error: " + x.getMessage());
				LogUtil.error(x, logger);
			}
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map e = (HashMap) list.get(i);
					String fd = (String) (e.get("fieldName") == null ? "" : e.get("fieldName"));
					String fv = (String) (e.get("fieldNameView") == null ? "" : e.get("fieldNameView"));
					String sc = (String) (e.get("searchContent") == null ? "" : e.get("searchContent"));
					if (fd.length() > 0 && sc.length() > 0) {
						String uuid = new RandomGUID().toString();
						String[] p = { type, fd, sc, uuid, fv, departmentId,companyId};
						DAOTools.updateForPrepared(sql, p, zkmDBId);
					}
				}
			}
		}
	}

	public static void getTreamStatic(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String companyId = request.getParameter("companyId") == null ? "" : request.getParameter("companyId");
		String start = "0";
		String num = "100";
		String sql = "select searchType,fieldNameView,searchContent,count(1) num from zkmSearchStatic";
		if(companyId.length()>0){
			sql+=" where companyId='" + companyId +"'";
		}
		sql+=" group by searchContent,fieldName order by num desc  LIMIT " + start + "," + num;
		logger.debug("getTreamStatic sql : " + sql);
		List list = DAOTools.queryMap(sql, zkmDBId);
		Map rmap = new HashMap();
		rmap.put("success", true);
		rmap.put("data", list);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}

	public static void getUserAddDocStatic(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		String loginName=request.getParameter("loginName") == null ? "" : request.getParameter("loginName");
		String companyId=request.getParameter("companyId") == null ? "" : request.getParameter("companyId");
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		String dateSql = "";
		if (bdate.length() > 0 && edate.length() > 0) {
			dateSql = " and `md` >= TIMESTAMP('" + bdate + "') and `md`<=TIMESTAMP('" + edate + "') ";
		} else if (bdate.length() > 0 && edate.length() <= 0) {
			dateSql = " and `md` >= TIMESTAMP('" + bdate + "') ";
		} else if (bdate.length() <= 0 && edate.length() > 0) {
			dateSql = " and `md`<=TIMESTAMP('" + edate + "') ";
		}
		if(companyId.length()>0){
			dateSql +=" and companyId='" + companyId +"' ";
		}
		String sql = "";
		if(loginName.length()>0){
			sql = "select `suSecurityUser`.`loginName`,`suSecurityUser`.`name`,(select sum(num) from `zkmUserAddDocStatic` where 1=1 " + dateSql + " and `suSecurityUser`.`loginName` = `zkmUserAddDocStatic`.loginName) num from `suSecurityUser` where loginName='"+ loginName +"' order by num desc";
		}else{
			sql = "select `suSecurityUser`.`loginName`,`suSecurityUser`.`name`,(select sum(num) from `zkmUserAddDocStatic` where 1=1 " + dateSql + " and `suSecurityUser`.`loginName` = `zkmUserAddDocStatic`.loginName) num from `suSecurityUser` order by num desc";
		}

		List list = DAOTools.queryMap(sql, zkmDBId);
		Map rmap = new HashMap();
		rmap.put("success", true);
		rmap.put("data", list);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}

	/**
	 * 作废，已迁移至　com.zinglabs.apps.zkmStatic.action.ZkmStatic
	 * @param user
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void getAddDocStatic(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		String dateSql = "";
		if (bdate.length() > 0 && edate.length() > 0) {
			dateSql = " and zif2.createTime >= TIMESTAMP('" + bdate + "') and zif2.createTime <= TIMESTAMP('" + edate + "') ";
		} else if (bdate.length() > 0 && edate.length() <= 0) {
			dateSql = " and zif2.createTime >= TIMESTAMP('" + bdate + "') ";
		} else if (bdate.length() <= 0 && edate.length() > 0) {
			dateSql = " and zif2.createTime <= TIMESTAMP('" + edate + "') ";
		}
		String sql = "select zif1.`id`,zif1.`text`,zif1.`parentId` _parentId,(select count(1) from  `zkmInfoBase` zif2 where zif2.parentId=zif1.id and zif2.recordType='f' " + dateSql + ") num from `zkmInfoBase` zif1 where zif1.`recordType`='d' and zif1.`isdel` <> '1' order by num desc";
		List list = DAOTools.queryMap(sql, zkmDBId);
		logger.debug("---getAddDocStatic static nums: " + list.size());
		Map rmap = new HashMap();
		rmap.put("success", true);
		rmap.put("data", list);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}

	/**
	 * 作废，已迁移至　com.zinglabs.apps.zkmStatic.action.ZkmStatic
	 * @param user
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void getModiDocStatic(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		String dateSql = "";
		if (bdate.length() > 0 && edate.length() > 0) {
			dateSql = " and zif2.lastModiTime >= TIMESTAMP('" + bdate + "') and zif2.lastModiTime <= TIMESTAMP('" + edate + "') ";
		} else if (bdate.length() > 0 && edate.length() <= 0) {
			dateSql = " and zif2.lastModiTime >= TIMESTAMP('" + bdate + "') ";
		} else if (bdate.length() <= 0 && edate.length() > 0) {
			dateSql = " and zif2.lastModiTime <= TIMESTAMP('" + edate + "') ";
		}
		String sql = "select zif1.`id`,zif1.`text`,zif1.`parentId` _parentId,(select count(1) from  `zkmInfoBase` zif2 where zif2.parentId=zif1.id and zif2.recordType='f' " + dateSql + ") num from `zkmInfoBase` zif1 where zif1.`recordType`='d' and zif1.`isdel` <> '1' order by num desc";
		List list = DAOTools.queryMap(sql, zkmDBId);
		Map rmap = new HashMap();
		rmap.put("success", true);
		rmap.put("data", list);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}

	public static void getDictList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String dictType = request.getParameter("dictType") == null ? "" : request.getParameter("dictType");
		if (dictType.length() > 0) {
			List list = getDictList(dictType);
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", list);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		} else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数。"), response);
		}
	}

	public static List getDictList(String dictType) throws Exception {
		String sql = "select id,name,value from ComboDic where comboName=?";
		String[] p = { dictType };
		Map emptyMap = new HashMap();
		emptyMap.put("id", "-10001");
		emptyMap.put("name", "");
		emptyMap.put("value", "");
		List list = new ArrayList();
		List qlist = DAOTools.queryMap(sql, p, zkmDBId);
		list.add(emptyMap);
		list.addAll(qlist);
		return list;
	}

	public static void getStaticDocClickView(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String expis=request.getParameter("exportIs")==null?"":request.getParameter("exportIs");
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		String companyId = request.getParameter("companyId") == null ? "" : request.getParameter("companyId");
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		List list = ZZkmRecordClickUtils.getStaticSearch("zkm", "", "", bdate, edate, "relId", "0", "100",companyId);
		if("TRUE".equals(expis.toUpperCase())){
			String[] fields={"title","num"};
			String[] fieldNames={"标题","访问次数"};
			exportHandle("staticExport.xls","export",list,fields,fieldNames,response);
		}else{
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", list);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		}
	}
	
	/**
	 * 作废，已迁移至　com.zinglabs.apps.zkmStatic.action.ZkmStatic
	 * @param user
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void getDocAreaStatic(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String sql = "select `ComboDic`.`value`,(select num from `zkmAreaRecordClickStaticView` where `dataValue`=`ComboDic`.`value`) clickNum,(select num from `zkmInfoBaseAreaStaticView` where `area`=`ComboDic`.`value`) dataNum from `ComboDic` where `ComboDic`.`comboName`='zkmInfoArea' order by dataNum desc";
		List list = DAOTools.queryMap(sql, zkmDBId);
		Map rmap = new HashMap();
		rmap.put("success", true);
		rmap.put("data", list);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}
	/**
	 * 作废，已迁移至　com.zinglabs.apps.zkmStatic.action.ZkmStatic
	 * @param user
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void getDocSortStatic(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String sql = "select id,text,(select num from `zkmSortDocTotalStatic` where `zkmSortDocTotalStatic`.id=zif.id) dataNum,(select num from `zkmSortDocTotalClickStatic` where `zkmSortDocTotalClickStatic`.`dataValue`=zif.id) clickNum from `zkmInfoBase` zif where zif.`recordType`='d' and zif.`isdel`<>'1'";
		List list = DAOTools.queryMap(sql, zkmDBId);
		Map rmap = new HashMap();
		rmap.put("success", true);
		rmap.put("data", list);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}

	public static void getZkmFaqUserAskAnserStatic(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String sql = "select * from `zkmFaqAskAnswerStaticView` ";
		List list = DAOTools.queryMap(sql, zkmDBId);
		Map rmap = new HashMap();
		rmap.put("success", true);
		rmap.put("data", list);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}

	public static void saveClickRecord(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
		}*/
		String infoType = request.getParameter("infoType") == null ? "" : request.getParameter("infoType");
		String dataid = request.getParameter("dataid") == null ? "" : request.getParameter("dataid");
		String df = request.getParameter("df") == null ? "" : request.getParameter("df");
		String dv = request.getParameter("dv") == null ? "" : request.getParameter("dv");
		String dt = request.getParameter("dt") == null ? "" : request.getParameter("dt");
		if (infoType.length() > 0 && dataid.length() > 0 && dt.length() > 0) {
			if(user!=null){
				ZZkmRecordClickUtils.insertClickRecord(infoType, dataid, df, dv, dt,user.loginName2,request);
			}else{
				ZZkmRecordClickUtils.insertClickRecord(infoType, dataid, df, dv, dt,"_guest",request);
			}
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "成功"), response);
		} else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数"), response);
		}
	}

	public static void zkmDocMaintainStatic(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String expis=request.getParameter("exportIs")==null?"":request.getParameter("exportIs");
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		String loginName = request.getParameter("loginName") == null ? "" : request.getParameter("loginName");
		String companyId = request.getParameter("companyId") == null ? "" : request.getParameter("companyId");
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (bdate.length() < 0 || edate.length() < 0) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "开始时间与结束时间均不可以为空。"), response);
			return;
		}
		List rlist = new ArrayList();
		List dayList = DateUtil.allDateBetween2Date(bdate, edate);
		String sql="";
		if(loginName.length()>0){
			sql = "select ? mdate," 
				+ "(select sum(num) from `zkmDocAddforDateStatic` where `zkmDocAddforDateStatic`.`md`=DATE_FORMAT(?,'%Y-%m-%d') and user=? and companyId=?) addNum," 
				+ "(select sum(num) from `zkmDocModiforDateStatic` where `zkmDocModiforDateStatic`.`md`=DATE_FORMAT(?,'%Y-%m-%d') and user=? and companyId=?) modiNum,"
				+ "(select sum(num) from `zkmDocStopforDateStatic` where `zkmDocStopforDateStatic`.`md`=DATE_FORMAT(?,'%Y-%m-%d') and user=? and companyId=?) stopNum ";
		}else{
			sql = "select ? mdate," 
				+ "(select sum(num) from `zkmDocAddforDateStatic` where `zkmDocAddforDateStatic`.`md`=DATE_FORMAT(?,'%Y-%m-%d') and companyId=?) addNum," 
				+ "(select sum(num) from `zkmDocModiforDateStatic` where `zkmDocModiforDateStatic`.`md`=DATE_FORMAT(?,'%Y-%m-%d') and companyId=?) modiNum,"
				+ "(select sum(num) from `zkmDocStopforDateStatic` where `zkmDocStopforDateStatic`.`md`=DATE_FORMAT(?,'%Y-%m-%d') and companyId=?) stopNum ";
		}
		if (dayList != null && dayList.size() > 0) {
			for (int i = 0; i < dayList.size(); i++) {
				String d = (String) dayList.get(i);
				if (d != null && d.length() > 0 && DateUtil.vaildateDateIs(d)) {
					List dl = new ArrayList();
					if(loginName.length()>0){
						String[] p = { d, d,loginName,companyId, d,loginName, companyId,d,loginName ,companyId};
						dl=DAOTools.queryMap(sql, p, zkmDBId);
					}else{
						String[] p = { d, d,companyId, d,companyId, d ,companyId};
						dl=DAOTools.queryMap(sql, p, zkmDBId);
					}
					
					if (dl != null && dl.size() > 0) {
						rlist.add(dl.get(0));
					}
				}
			}
			ArrayList nlist=new ArrayList();
			for(int i=0;i<rlist.size();i++){
				Map map=(HashMap)rlist.get(i);
				String addNum=map.get("addNum")==null?"":(String)map.get("addNum");
				if(addNum.length()<=0 || "NULL".equals(addNum.toUpperCase())){
					addNum="0";
				}
				map.put("addNum", addNum);
				String modiNum=map.get("modiNum")==null?"":(String)map.get("modiNum");
				if(modiNum.length()<=0 || "NULL".equals(modiNum.toUpperCase())){
					modiNum="0";
				}
				map.put("modiNum", modiNum);
				String stopNum=map.get("stopNum")==null?"":(String)map.get("stopNum");
				if(stopNum.length()<=0 || "NULL".equals(stopNum.toUpperCase())){
					stopNum="0";
				}
				map.put("stopNum", stopNum);
				int staticAll=Integer.parseInt(addNum) + Integer.parseInt(modiNum)+ Integer.parseInt(stopNum);
				map.put("staticAll", String.valueOf(staticAll));
				nlist.add(map);
			}
			if("TRUE".equals(expis.toUpperCase())){
				String[] fields={"mdate","addNum","modiNum","stopNum","staticAll"};
				String[] fieldNames={"日期","新增","更新","停用","合计"};
				exportHandle("staticExport.xls","export",rlist,fields,fieldNames,response);
			}else{
				Map rmap = new HashMap();
				rmap.put("success", true);
				rmap.put("data", nlist);
				postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
			}
		} else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
		}
	}

	public static void zkmDocFolderManagerStatic(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		String companyId = request.getParameter("companyId") == null ? "" : request.getParameter("companyId");
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		String dateSql = "";
		String mdSql = "";
		if (bdate.length() > 0 && edate.length() > 0) {
			//dateSql = " and `zkmInfoBase`.`createTime` <= TIMESTAMP('" + edate + "') ";
			mdSql = " and `md` >= TIMESTAMP('" + bdate + "') and `md` <= TIMESTAMP('" + edate + "') ";
		} else if (bdate.length() > 0 && edate.length() <= 0) {
			//dateSql = " and `zkmInfoBase`.`createTime` >= TIMESTAMP('" + bdate + "') ";
			mdSql = " and `md` >= TIMESTAMP('" + bdate + "') ";
		} else if (bdate.length() <= 0 && edate.length() > 0) {
			//dateSql = " and `zkmInfoBase`.`createTime` <= TIMESTAMP('" + edate + "') ";
			mdSql = " and `md` <= TIMESTAMP('" + edate + "') ";
		}
		if(companyId.length()>0){
			dateSql+=" and companyId='" + companyId + "'";
			mdSql+=" and companyId='" + companyId + "'";
		}
		String sql = "select " + "(select count(1) from `zkmInfoBase` where `zkmInfoBase`.`recordType`='d' and `zkmInfoBase`.`isdel`<>1 " + dateSql + " ) folderNum," + "(select SUM(num) from `zkmDocAddforDateStatic` where 1=1 " + mdSql + " ) addNum,"
				+ "(select SUM(num) from `zkmDocModiforDateStatic` where 1=1 " + mdSql + " ) modiNum," + "(select SUM(num) from `zkmDocStopforDateStatic` where 1=1 " + mdSql + " ) stopNum;";
		List list = DAOTools.queryMap(sql, zkmDBId);
		Map rmap = new HashMap();
		rmap.put("success", true);
		rmap.put("data", list);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}

	public static void zkmSearchRoundStatic (UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String expis=request.getParameter("exportIs")==null?"":request.getParameter("exportIs");
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		String departmentId = request.getParameter("departmentId") == null ? "" : request.getParameter("departmentId");
		String companyId = request.getParameter("companyId") == null ? "" : request.getParameter("companyId");
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		String dateSql = "";
		String mdSql = "";
		if (bdate.length() > 0 && edate.length() > 0) {
			mdSql = " and `makeDate` >= TIMESTAMP('" + bdate + "') and `makeDate` <= TIMESTAMP('" + edate + "') ";
		} else if (bdate.length() > 0 && edate.length() <= 0) {
			mdSql = " and `makeDate` >= TIMESTAMP('" + bdate + "') ";
		} else if (bdate.length() <= 0 && edate.length() > 0) {
			mdSql = " and `makeDate` <= TIMESTAMP('" + edate + "') ";
		}

		if(companyId.length()>0){
			mdSql+=" and companyId='" + companyId +"'";
		}
		String sql = "select dataValue type,count(1) num from ZKMSearchRoundStatic where 1=1 " + mdSql + " group by type";
		logger.debug("zkmSearchRoundStatic sql : " + sql);
		List list = DAOTools.queryMap(sql, zkmDBId);
		if("TRUE".equals(expis.toUpperCase())){
			String[] fields={"type","num"};
			String[] fieldNames={"功能","访问次数"};
			exportHandle("staticExport.xls","export",list,fields,fieldNames,response);
		}else{
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", list);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		}
	}
	
	public static void getExportDocSort(UserInfo2 user, HttpServletRequest request, HttpServletResponse response)throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String id=request.getParameter("id")==null?"":request.getParameter("id");
		List clist=getChildList(id);
		List exportList=new ArrayList();
		getAllChildList(clist,exportList,0);
		if(clist!=null && clist.size()>0){
			response.setHeader("Content-disposition", "attachment;filename=" + new String("exportFolders.xls".getBytes("gb2312"), "iso8859-1"));
			CreateExcelModel cem = new CreateExcelModel();
			cem.setOs(response.getOutputStream());
			cem.setRowDataList(exportList);
			cem.setSheetName("exportData");
			ExcelHelper.makeExcel(cem);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "没有任何要导出的数据。"), response);
		}
	}
	
	public static void getAllChildList(List list,List expList,int ft){
		if(list!=null && list.size()>0){
			int num=ft;
			for(int i=0;i<list.size();i++){
				List clist=new ArrayList();
				Map map=(HashMap)list.get(i);
				String id="";
				if(map!=null){
					id=map.get("id")==null?"":(String)map.get("id");
					String [] values=new String[num +1];
					for(int j=0;j<num;j++){
						values[j]="";
					}
					values[values.length-1]=map.get("text")==null?"":(String)map.get("text");
					expList.add(values);
				}
				if(id.length()>0){
					try{
						clist=getChildList(id);
					}catch(Exception x){
						
					}
				}
				if(clist!=null && clist.size()>0){
					map.put("CHILD_DATAS_", clist);
					getAllChildList(clist,expList,num+1);
				}
			}
		}
	}
	
	public static void exportHandle(String fileName, String sheetName, List datalist, String[] fields, String[] fieldNames, HttpServletResponse response) throws Exception {
		if (datalist != null && datalist.size() > 0 && fields != null && fields.length > 0) {
			String fname = "";
			if (fileName != null && fileName.length() > 0) {
				fname = fileName;
			} else {
				fname = "exprot.xls";
			}
			response.setHeader("Content-disposition", "attachment;filename=" + new String(fname.getBytes("gb2312"), "iso8859-1"));
			List edl = new ArrayList();
			for (int i = 0; i < datalist.size(); i++) {
				Map dm = (HashMap) datalist.get(i);
				if (dm != null) {
					String[] data = new String[fields.length];
					for (int k = 0; k < fields.length; k++) {
						String field = fields[k];
						Object vo=dm.get(field);
						String vl ="";
						if(vo!=null){
							if(vo instanceof String){
								vl = (String)vo;
							}else{
								vl=String.valueOf(vo);
							}
						}
						data[k] = vl;
					}
					edl.add(data);
				}
			}
			if (edl.size() > 0) {
				CreateExcelModel cem = new CreateExcelModel();
				cem.setOs(response.getOutputStream());
				if(fieldNames!=null && fieldNames.length>0){
					cem.setFristRow(fieldNames);
				}
				cem.setRowDataList(edl);
				cem.setSheetName("exportData");
				ExcelHelper.makeExcel(cem);
			}
		}
	}
	
	public static void getAreaSendDocStatic (UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String expis=request.getParameter("exportIs")==null?"":request.getParameter("exportIs");
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		String companyId = request.getParameter("companyId") == null ? "" : request.getParameter("companyId");
		//是否计算公共信息部分
		String countPublicFN=request.getParameter("countPublicFN") == null ? "" : request.getParameter("countPublicFN");
		String fbstate=ZKMFlowDisponse.zkmDocStateDefined[1];
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		//获取文档的统计
		String dateSql = "";
		if (bdate.length() > 0 && edate.length() > 0) {
			dateSql = " and `zkmInfoBaseDisponse`.createTime >= TIMESTAMP('" + bdate + "') and `zkmInfoBaseDisponse`.createTime <= TIMESTAMP('" + edate + "') ";
		} else if (bdate.length() > 0 && edate.length() <= 0) {
			dateSql = " and `zkmInfoBaseDisponse`.createTime >= TIMESTAMP('" + bdate + "') ";
		} else if (bdate.length() <= 0 && edate.length() > 0) {
			dateSql = " and `zkmInfoBaseDisponse`.createTime <= TIMESTAMP('" + edate + "') ";
		}
		if(companyId.length()>0){
			dateSql+=" and companyId='" + companyId + "'";
		}
		String sql="select value,"
					+"(select count(1) from `zkmInfoBaseDisponse` where `zkmInfoBaseDisponse`.`area`=`ComboDic`.`value` and `zkmInfoBaseDisponse`.`zkmDocState`='"+ fbstate +"'" + dateSql +" ) allNum,"
					+"(select count(1) from `zkmInfoBaseDisponse` where `zkmInfoBaseDisponse`.`area`=`ComboDic`.`value` and `zkmInfoBaseDisponse`.`notTrue`<>'Y'  and `zkmInfoBaseDisponse`.`zkmDocState`='"+ fbstate +"'"+ dateSql +" ) trueNum,"
					+"(select count(1) from `zkmInfoBaseDisponse` where `zkmInfoBaseDisponse`.`area`=`ComboDic`.`value` and `zkmInfoBaseDisponse`.`notTimely`<>'Y'  and `zkmInfoBaseDisponse`.`zkmDocState`='"+ fbstate +"'" + dateSql +" ) TimelyNum,"
					+"(select count(1) from `zkmInfoBaseDisponse` where `zkmInfoBaseDisponse`.`area`=`ComboDic`.`value` and `zkmInfoBaseDisponse`.`notNormally`<>'Y'  and `zkmInfoBaseDisponse`.`zkmDocState`='"+ fbstate +"'" + dateSql +" ) normallyNum "
					+"from `ComboDic` where `ComboDic`.`comboName`='zkmInfoArea'";
		List listDoc = DAOTools.queryMap(sql, zkmDBId);
		
		List sumList=new ArrayList();
		//需要合计公共信息时
		if(countPublicFN.length()>0){
			//获取公共信息的统计
			dateSql = "";
			if (bdate.length() > 0 && edate.length() > 0) {
				dateSql = " and `zkmPublicInfoDisponse`.createTime >= TIMESTAMP('" + bdate + "') and `zkmPublicInfoDisponse`.createTime <= TIMESTAMP('" + edate + "') ";
			} else if (bdate.length() > 0 && edate.length() <= 0) {
				dateSql = " and `zkmPublicInfoDisponse`.createTime >= TIMESTAMP('" + bdate + "') ";
			} else if (bdate.length() <= 0 && edate.length() > 0) {
				dateSql = " and `zkmPublicInfoDisponse`.createTime <= TIMESTAMP('" + edate + "') ";
			}
			
			fbstate=ZKMPublicInfoFlowDo.approveStateDefined[3];
			sql="select value,"
					+"(select count(1) from `zkmPublicInfoDisponse` where `zkmPublicInfoDisponse`.`area`=`ComboDic`.`value` and `zkmPublicInfoDisponse`.`approveState`='"+ fbstate +"'" + dateSql +" ) allNum,"
					+"(select count(1) from `zkmPublicInfoDisponse` where `zkmPublicInfoDisponse`.`area`=`ComboDic`.`value` and `zkmPublicInfoDisponse`.`notTrue`<>'Y'  and `zkmPublicInfoDisponse`.`approveState`='"+ fbstate +"'"+ dateSql +" ) trueNum,"
					+"(select count(1) from `zkmPublicInfoDisponse` where `zkmPublicInfoDisponse`.`area`=`ComboDic`.`value` and `zkmPublicInfoDisponse`.`notTimely`<>'Y'  and `zkmPublicInfoDisponse`.`approveState`='"+ fbstate +"'" + dateSql +" ) TimelyNum,"
					+"(select count(1) from `zkmPublicInfoDisponse` where `zkmPublicInfoDisponse`.`area`=`ComboDic`.`value` and `zkmPublicInfoDisponse`.`notNormally`<>'Y'  and `zkmPublicInfoDisponse`.`approveState`='"+ fbstate +"'" + dateSql +" ) normallyNum "
					+"from `ComboDic` where `ComboDic`.`comboName`='zkmInfoArea'";
			List listPub = DAOTools.queryMap(sql, zkmDBId);
			
			//合并结果集
			
			if(listDoc!=null && listDoc.size()>0 && listPub!=null && listPub.size()>0){
				for(int i=0;i<listDoc.size();i++){
					Map sumMap=new HashMap();
					try{
						Map mdoc=(Map)listDoc.get(i);
						String area=mdoc.get("value")==null?"":(String)mdoc.get("value");
						sumMap.put("value", area);
						int allNum=mdoc.get("allNum")==null?0:Integer.parseInt((String)mdoc.get("allNum"));
						int trueNum=mdoc.get("trueNum")==null?0:Integer.parseInt((String)mdoc.get("trueNum"));
						int TimelyNum=mdoc.get("TimelyNum")==null?0:Integer.parseInt((String)mdoc.get("TimelyNum"));
						int normallyNum=mdoc.get("normallyNum")==null?0:Integer.parseInt((String)mdoc.get("normallyNum"));
						for(int j=0;j<listPub.size();j++){
							Map pdoc=(Map)listPub.get(i);
							String area1=pdoc.get("value")==null?"":(String)pdoc.get("value");
							if(area.equals(area1)){
								int allNum1=pdoc.get("allNum")==null?0:Integer.parseInt((String)pdoc.get("allNum"));
								int trueNum1=pdoc.get("trueNum")==null?0:Integer.parseInt((String)pdoc.get("trueNum"));
								int TimelyNum1=pdoc.get("TimelyNum")==null?0:Integer.parseInt((String)pdoc.get("TimelyNum"));
								int normallyNum1=pdoc.get("normallyNum")==null?0:Integer.parseInt((String)pdoc.get("normallyNum"));
								allNum=allNum + allNum1;
								trueNum=trueNum + trueNum1;
								TimelyNum=TimelyNum + TimelyNum1;
								normallyNum=normallyNum + normallyNum1;
								break;
							}
						}
						sumMap.put("allNum", allNum);
						sumMap.put("trueNum", trueNum);
						sumMap.put("TimelyNum", TimelyNum);
						sumMap.put("normallyNum", normallyNum);
						sumList.add(sumMap);
					}catch(Exception x){
						continue;
					}
				}
			}
		}else{
			sumList=listDoc;
		}
		
		if("TRUE".equals(expis.toUpperCase())){
			String[] fields={"value","allNum","TimelyNum","trueNum"};
			String[] fieldNames={"地区","总量","及时率","正确率"};
			exportHandle("staticExport.xls","export",sumList,fields,fieldNames,response);
		}else{
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", sumList);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		}
	}
	
	public static void getZkmSearchModleUseStatic (UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String expis=request.getParameter("exportIs")==null?"":request.getParameter("exportIs");
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		String companyId = request.getParameter("companyId") == null ? "" : request.getParameter("companyId");
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		String dateSql = "";
		if (bdate.length() > 0 && edate.length() > 0) {
			dateSql = " and `zkmRecordClickInfo`.makeDate >= TIMESTAMP('" + bdate + "') and `zkmRecordClickInfo`.makeDate <= TIMESTAMP('" + edate + "') ";
		} else if (bdate.length() > 0 && edate.length() <= 0) {
			dateSql = " and `zkmRecordClickInfo`.makeDate >= TIMESTAMP('" + bdate + "') ";
		} else if (bdate.length() <= 0 && edate.length() > 0) {
			dateSql = " and `zkmRecordClickInfo`.makeDate <= TIMESTAMP('" + edate + "') ";
		}
		if(companyId.length()>0){
			dateSql +=" and companyId ='" + companyId + "'";
		}
		String sql="select `dataValue`,count(1) num from `zkmRecordClickInfo` where `zkmRecordClickInfo`.`infoType`='ZKMSearch' "+ dateSql +" group by `zkmRecordClickInfo`.`dataType`";
		List list = DAOTools.queryMap(sql, zkmDBId);
		if("TRUE".equals(expis.toUpperCase())){
			String[] fields={"dataValue","num"};
			String[] fieldNames={"功能","统计"};
			exportHandle("staticExport.xls","export",list,fields,fieldNames,response);
		}else{
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", list);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		}
	}
	
	public static void getZkmFaqHotClickStatic (UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String expis=request.getParameter("exportIs")==null?"":request.getParameter("exportIs");
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		String dateSql = "";
		if (bdate.length() > 0 && edate.length() > 0) {
			dateSql = " and makeDate >= TIMESTAMP('" + bdate + "') and makeDate <= TIMESTAMP('" + edate + "') ";
		} else if (bdate.length() > 0 && edate.length() <= 0) {
			dateSql = " and makeDate >= TIMESTAMP('" + bdate + "') ";
		} else if (bdate.length() <= 0 && edate.length() > 0) {
			dateSql = " and makeDate <= TIMESTAMP('" + edate + "') ";
		}
		String sql="select relId,title,createDate,createUser,makeDate,count(1) AS `num` from `zkmFaqHotClickStatic` where 1=1 " + dateSql +" group by relId LIMIT 0,100";
		List list = DAOTools.queryMap(sql, zkmDBId);
		if("TRUE".equals(expis.toUpperCase())){
			String[] fields={"title","createDate","createUser","num"};
			String[] fieldNames={"标题","建立时间","建立用户","查看次数"};
			exportHandle("staticExport.xls","export",list,fields,fieldNames,response);
		}else{
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", list);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		}
	}
	
	public static void getZkmFaqAreaStatic (UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String expis=request.getParameter("exportIs")==null?"":request.getParameter("exportIs");
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		String dateSql = "";
		if (bdate.length() > 0 && edate.length() > 0) {
			dateSql = " and `zkmFaq`.createDate >= TIMESTAMP('" + bdate + "') and `zkmFaq`.createDate <= TIMESTAMP('" + edate + "') ";
		} else if (bdate.length() > 0 && edate.length() <= 0) {
			dateSql = " and `zkmFaq`.createDate >= TIMESTAMP('" + bdate + "') ";
		} else if (bdate.length() <= 0 && edate.length() > 0) {
			dateSql = " and `zkmFaq`.createDate <= TIMESTAMP('" + edate + "') ";
		}
		String sql="select value,"
					+"(select count(1) from `zkmFaq` where `zkmFaq`.`area`=`ComboDic`.`value` and `zkmFaq`.`recordType`='M' and isDel<>1 "+ dateSql +" ) allNum,"
					+"(select count(1) from `zkmFaq` where `zkmFaq`.`area`=`ComboDic`.`value` and `zkmFaq`.`recordType`='M' and isDel<>1 and `zkmFaq`.`faqState`='内部处理中'  "+ dateSql +" ) waitNum,"
					+"(select count(1) from `zkmFaq` where `zkmFaq`.`area`=`ComboDic`.`value` and `zkmFaq`.`recordType`='M' and isDel<>1 and `zkmFaq`.`faqState`='已完成'  "+ dateSql +" ) completNum "
					+"from `ComboDic` where `ComboDic`.`comboName`='zkmInfoArea'";
		List list = DAOTools.queryMap(sql, zkmDBId);
		if("TRUE".equals(expis.toUpperCase())){
			String[] fields={"value","allNum","waitNum","completNum"};
			String[] fieldNames={"地区","总量","内部处理中","已完成"};
			exportHandle("staticExport.xls","export",list,fields,fieldNames,response);
		}else{
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", list);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		}
	}
	
	public static void getZkmFaqSortStatic (UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String expis=request.getParameter("exportIs")==null?"":request.getParameter("exportIs");
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateTimeIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		String dateSql = "";
		if (bdate.length() > 0 && edate.length() > 0) {
			dateSql = " and `zkmFaq`.createDate >= TIMESTAMP('" + bdate + "') and `zkmFaq`.createDate <= TIMESTAMP('" + edate + "') ";
		} else if (bdate.length() > 0 && edate.length() <= 0) {
			dateSql = " and `zkmFaq`.createDate >= TIMESTAMP('" + bdate + "') ";
		} else if (bdate.length() <= 0 && edate.length() > 0) {
			dateSql = " and `zkmFaq`.createDate <= TIMESTAMP('" + edate + "') ";
		}
		String sql="select `id`,`text`,"
					+"(select count(1) from `zkmFaq` where `zkmFaq`.`parentId`=`zkmInfoBase`.`id` and `zkmFaq`.`recordType`='M' and isDel<>1 "+ dateSql +" ) allNum,"
					+"(select count(1) from `zkmFaq` where `zkmFaq`.`parentId`=`zkmInfoBase`.`id` and `zkmFaq`.`recordType`='M' and isDel<>1 and `zkmFaq`.`faqState`='内部处理中'  "+ dateSql +" ) waitNum,"
					+"(select count(1) from `zkmFaq` where `zkmFaq`.`parentId`=`zkmInfoBase`.`id` and `zkmFaq`.`recordType`='M' and isDel<>1 and `zkmFaq`.`faqState`='已完成'  "+ dateSql +" ) completNum "
					+"from `zkmInfoBase` where `zkmInfoBase`.`recordType`='dfaq' and  `zkmInfoBase`.`isdel`<>1";
		List list = DAOTools.queryMap(sql, zkmDBId);
		if("TRUE".equals(expis.toUpperCase())){
			String[] fields={"text","allNum","waitNum","completNum"};
			String[] fieldNames={"分类（目录）","总量","内部处理中","已完成"};
			exportHandle("staticExport.xls","export",list,fields,fieldNames,response);
		}else{
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", list);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		}
	}
	
	public static void getZkmLastModiDocSearch (UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String expis=request.getParameter("exportIs")==null?"":request.getParameter("exportIs");
		String day = request.getParameter("day") == null ? "" : request.getParameter("day");
		String ruler ="\\d+";
		Pattern p = Pattern.compile(ruler);
		if(!p.matcher(day).matches()){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "非法月数或缺少参数，请重新输入。"), response);
			return;
		}
		String now=DateUtil.getLocalDefineDate("yyyy-MM-dd");
		String lm=DateUtil.subMonth(now, Integer.parseInt("-" + day));
		long mday=DateUtil.getCalculateDayOfDay(now, lm);
		String sql="select * from `zkmInfoBaseDisponseFB`  where " +
				"TO_DAYS(now())-TO_DAYS(`lastModiTime`) >= " + mday ;
		List list = DAOTools.queryMap(sql, zkmDBId);
		if("TRUE".equals(expis.toUpperCase())){
			LuceneSearchHandle.exportZkmDocs("", list, request, response);
		}else{
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", list);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		}
	}
	
	public static void getZkmFaqSearch(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String expis=request.getParameter("exportIs")==null?"":request.getParameter("exportIs");
		String area=request.getParameter("area")==null?"":request.getParameter("area");
		String faqState=request.getParameter("faqState")==null?"":request.getParameter("faqState");
		String title=request.getParameter("title")==null?"":request.getParameter("title");
		String bdate=request.getParameter("bdate")==null?"":request.getParameter("bdate");
		String edate=request.getParameter("edate")==null?"":request.getParameter("edate");
		String userm=request.getParameter("userm")==null?"":request.getParameter("userm");
		
		String sql="select * from `zkmFaq` where recordType='M' and isDel <> 1 ";
		List params=new ArrayList();
		if(area.length()>0){
			sql+=" and area=? ";
			params.add(area);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要查询条件。"), response);
			return;
		}
		if(faqState.length()>0){
			sql+=" and faqState=? ";
			params.add(faqState);
		}
		if(title.length()>0){
			sql+=" and title like ? ";
			params.add(title);
		}
		if(userm.length()>0){
			sql+=" and createUser = ? ";
			params.add(userm);
		}
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (bdate.length() > 0 && edate.length() > 0) {
			sql += " and `zkmFaq`.createDate >= TIMESTAMP('" + bdate + "') and `zkmFaq`.createDate <= TIMESTAMP('" + edate + "') ";
		} else if (bdate.length() > 0 && edate.length() <= 0) {
			sql += " and `zkmFaq`.createDate >= TIMESTAMP('" + bdate + "') ";
		} else if (bdate.length() <= 0 && edate.length() > 0) {
			sql += " and `zkmFaq`.createDate <= TIMESTAMP('" + edate + "') ";
		}
		List list = DAOTools.queryMap(sql, params,zkmDBId);
		if("TRUE".equals(expis.toUpperCase())){
			String[] fields={"faqState","title","area","createDate","createUser"};
			String[] fieldNames={"状态","标题","分行","征询时间","征询用户"};
			exportHandle("staticExport.xls","export",list,fields,fieldNames,response);
		}else{
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", list);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		}
	}
	
	public static void zkmDocFlowJBStatic(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String expis=request.getParameter("exportIs")==null?"":request.getParameter("exportIs");
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		String loginName = request.getParameter("loginName") == null ? "" : request.getParameter("loginName");
		String companyId = request.getParameter("companyId") == null ? "" : request.getParameter("companyId");
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (bdate.length() < 0 || edate.length() < 0) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "开始时间与结束时间均不可以为空。"), response);
			return;
		}
		List rlist = new ArrayList();
		List dayList = DateUtil.allDateBetween2Date(bdate, edate);
		
		String jbAdd="select date_format(makeDate,'%Y-%m-%d') as `md`,count(1) num,dataValue as loginName from zkmRecordClickInfo where infoType = 'ZKM' and dataType='zkmStatic_UserJB_Add' and date_format(makeDate,'%Y-%m-%d')=date_format(?,'%Y-%m-%d') and dataValue=? and companyId=? group by date_format(makeDate,'%Y-%m-%d'),dataValue";
		String jbmodi="select date_format(makeDate,'%Y-%m-%d') as `md`,count(1) num,dataValue as loginName from zkmRecordClickInfo where infoType = 'ZKM' and dataType='zkmStatic_UserJB_Modi' and date_format(makeDate,'%Y-%m-%d')=date_format(?,'%Y-%m-%d') and dataValue=? and companyId=? group by date_format(makeDate,'%Y-%m-%d'),dataValue";
		String jbback="select date_format(makeDate,'%Y-%m-%d') as `md`,count(1) num,dataValue as loginName from zkmRecordClickInfo where infoType = 'ZKM' and dataType='zkmStatic_UserJB_Back' and date_format(makeDate,'%Y-%m-%d')=date_format(?,'%Y-%m-%d') and dataValue=? and companyId=? group by date_format(makeDate,'%Y-%m-%d'),dataValue";

		if(dayList!=null && dayList.size()>0){
			for(int i=0;i<dayList.size();i++){
				int addNum=0;
				int modiNum=0;
				int backNum=0;
				
				String [] p={(String)dayList.get(i),loginName,companyId};
				List dlist=DAOTools.queryMap(jbAdd,p, zkmDBId);
				if(dlist!=null && dlist.size()>0){
					Map map=(Map)dlist.get(0);
					String num=map.get("num")==null?"":(String)map.get("num");
					if(num.length()>0){
						addNum=Integer.parseInt(num);
					}
				}
				
				dlist=DAOTools.queryMap(jbmodi, p,zkmDBId);
				if(dlist!=null && dlist.size()>0){
					Map map=(Map)dlist.get(0);
					String num=map.get("num")==null?"":(String)map.get("num");
					if(num.length()>0){
						modiNum=Integer.parseInt(num);
					}
				}
				
				dlist=DAOTools.queryMap(jbback, p,zkmDBId);
				if(dlist!=null && dlist.size()>0){
					Map map=(Map)dlist.get(0);
					String num=map.get("num")==null?"":(String)map.get("num");
					if(num.length()>0){
						backNum=Integer.parseInt(num);
					}
				}
				
				int staticAll=addNum + modiNum;
				float justOne=100;
				if(staticAll!=0){
					Integer ints=new Integer(staticAll);
					Integer intb=new Integer(backNum);
					justOne=(1-intb.floatValue()/ints.floatValue())*100;
				}
				Map dmap=new HashMap();
				dmap.put("mdate", (String)dayList.get(i));
				dmap.put("czh",loginName);
				dmap.put("addNum",String.valueOf(addNum));
				dmap.put("modiNum",String.valueOf(modiNum));
				dmap.put("backNum",String.valueOf(backNum));
				dmap.put("staticAll",String.valueOf(staticAll));
				dmap.put("justOne",String.valueOf(justOne) + "%");
				rlist.add(dmap);
			}
			if("TRUE".equals(expis.toUpperCase())){
				String[] fields={"mdate","czh","addNum","modiNum","staticAll","justOne"};
				String[] fieldNames={"日期","操作号","新增","更新","合计","一次通过率"};
				exportHandle("staticExport.xls","export",rlist,fields,fieldNames,response);
			}else{
				Map rmap = new HashMap();
				rmap.put("success", true);
				rmap.put("data", rlist);
				postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
			}
		}
	}
	
	public static void zkmDocFlowFHStatic(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		String expis=request.getParameter("exportIs")==null?"":request.getParameter("exportIs");
		String bdate = request.getParameter("bdate") == null ? "" : request.getParameter("bdate");
		String edate = request.getParameter("edate") == null ? "" : request.getParameter("edate");
		String loginName = request.getParameter("loginName") == null ? "" : request.getParameter("loginName");
		String companyId = request.getParameter("companyId") == null ? "" : request.getParameter("companyId");
		boolean dv = true;
		if (bdate.length() > 0) {
			dv = DateUtil.vaildateDateIs(bdate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (edate.length() > 0) {
			dv = DateUtil.vaildateDateIs(edate);
			if (!dv) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "日期格式错误。"), response);
				return;
			}
		}
		if (bdate.length() < 0 || edate.length() < 0) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "开始时间与结束时间均不可以为空。"), response);
			return;
		}
		List rlist = new ArrayList();
		List dayList = DateUtil.allDateBetween2Date(bdate, edate);
		
		String fhAdd="select date_format(makeDate,'%Y-%m-%d') as `md`,count(1) num,dataValue as loginName from zkmRecordClickInfo where infoType = 'ZKM' and dataType='zkmStatic_UserFH_Add' and date_format(makeDate,'%Y-%m-%d')=date_format(?,'%Y-%m-%d') and dataValue=?  and companyId=? group by date_format(makeDate,'%Y-%m-%d'),dataValue";
		String fhmodi="select date_format(makeDate,'%Y-%m-%d') as `md`,count(1) num,dataValue as loginName from zkmRecordClickInfo where infoType = 'ZKM' and dataType='zkmStatic_UserFH_Modi' and date_format(makeDate,'%Y-%m-%d')=date_format(?,'%Y-%m-%d') and dataValue=?  and companyId=? group by date_format(makeDate,'%Y-%m-%d'),dataValue";
		String fhstop="select date_format(makeDate,'%Y-%m-%d') as `md`,count(1) num,dataValue as loginName from zkmRecordClickInfo where infoType = 'ZKM' and dataType='zkmStatic_UserFH_Stop' and date_format(makeDate,'%Y-%m-%d')=date_format(?,'%Y-%m-%d') and dataValue=?  and companyId=? group by date_format(makeDate,'%Y-%m-%d'),dataValue";
		String fhback="select date_format(makeDate,'%Y-%m-%d') as `md`,count(1) num,dataValue as loginName from zkmRecordClickInfo where infoType = 'ZKM' and dataType='zkmStatic_UserFH_Back' and date_format(makeDate,'%Y-%m-%d')=date_format(?,'%Y-%m-%d') and dataValue=?  and companyId=? group by date_format(makeDate,'%Y-%m-%d'),dataValue";
		
		if(dayList!=null && dayList.size()>0){
			for(int i=0;i<dayList.size();i++){
				int addNum=0;
				int modiNum=0;
				int backNum=0;
				int stopNum=0;
				
				String [] p={(String)dayList.get(i),loginName,companyId};
				List dlist=DAOTools.queryMap(fhAdd,p, zkmDBId);
				if(dlist!=null && dlist.size()>0){
					Map map=(Map)dlist.get(0);
					String num=map.get("num")==null?"":(String)map.get("num");
					if(num.length()>0){
						addNum=Integer.parseInt(num);
					}
				}
				
				dlist=DAOTools.queryMap(fhmodi, p,zkmDBId);
				if(dlist!=null && dlist.size()>0){
					Map map=(Map)dlist.get(0);
					String num=map.get("num")==null?"":(String)map.get("num");
					if(num.length()>0){
						modiNum=Integer.parseInt(num);
					}
				}
				
				dlist=DAOTools.queryMap(fhback, p,zkmDBId);
				if(dlist!=null && dlist.size()>0){
					Map map=(Map)dlist.get(0);
					String num=map.get("num")==null?"":(String)map.get("num");
					if(num.length()>0){
						backNum=Integer.parseInt(num);
					}
				}
				
				dlist=DAOTools.queryMap(fhstop, p,zkmDBId);
				if(dlist!=null && dlist.size()>0){
					Map map=(Map)dlist.get(0);
					String num=map.get("num")==null?"":(String)map.get("num");
					if(num.length()>0){
						stopNum=Integer.parseInt(num);
					}
				}
				
				int staticAll=addNum + modiNum +stopNum ;
				Map dmap=new HashMap();
				dmap.put("mdate", (String)dayList.get(i));
				dmap.put("czh",loginName);
				dmap.put("addNum",addNum);
				dmap.put("modiNum",modiNum);
				dmap.put("stopNum",stopNum);
				dmap.put("backNum",backNum);
				dmap.put("staticAll",staticAll);
				rlist.add(dmap);
			}
			if("TRUE".equals(expis.toUpperCase())){
				String[] fields={"mdate","czh","addNum","modiNum","stopNum","backNum","staticAll"};
				String[] fieldNames={"日期","操作号","新增","更新","停用","退回","合计"};
				exportHandle("staticExport.xls","export",rlist,fields,fieldNames,response);
			}else{
				Map rmap = new HashMap();
				rmap.put("success", true);
				rmap.put("data", rlist);
				postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
			}
		}
	}
	
	public static void doServerImp(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		ZkmFileImport zfi=new ZkmFileImport();
		zfi.imp2Database();
		Map rmap=new HashMap();
		rmap.put("total", zfi.total);
		rmap.put("success", zfi.success);
		rmap.put("error", zfi.error);
		rmap.put("xlsnum", zfi.xlsnum);
		rmap.put("docnum", zfi.docnum);
		rmap.put("elist", zfi.elist);
		rmap.put("slist", zfi.sameList);
		postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
	}
	
	public static void doServerImp2(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		ZkmFileImport zfi=new ZkmFileImport();
		zfi.imp2Database(user,request);
		Map rmap=new HashMap();
		//rmap.put("total", zfi.total);
		//rmap.put("success", zfi.success);
		//rmap.put("error", zfi.error);
		//rmap.put("xlsnum", zfi.xlsnum);
		//rmap.put("docnum", zfi.docnum);
		//rmap.put("elist", zfi.elist);
		//rmap.put("slist", zfi.sameList);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",rmap), response);
	}
	
	public static final String [] DISPONSE_SEARCH_TERMS={"flowType:=","basilicType:=","applyType:=","area:=","title:like","companyId:=","companyName:=","departmentId:=","departmentName:="};
	public static final String DISPONSE_SEARCH_FIELDS="`id`,`title`,`bDate`,`eDate`,`createUser`,`createTime`,`area`,`versions`,`hotTag`,`keywords`,`keywords1`,`keywords2`,`keywords3`,`keywords4`,`keywords5`,`summary`,`ToSMS`,`ToEmail`,`notTrue`,`notTimely`,`notNormally`,`changReason`,`disponseOpinion`,`checkOneOpinion`,`checkTowOpinion`,`basilicType`,`zkmDocState`,`applyType`,`approveState`,`appointedCheckUser`,`disponseUser`,`disponseBTime`,`disponseETime`,`flowNode`,`flowType`,`securityUserId`,`securityUser`,`securityFolderID`,`securityFolderName`,`SMSSendPhone`,contentPath,flowID,appointedCheckUserID,companyId,companyName,departmentId,departmentName,lastModiUser,lastModiTime,notTrueBZ,notTimelyBZ,notNormallyBZ";
	public static final String[] DISPONSE_UPDATE_FIELDS={"title","bDate","eDate","createUser","createTime","area","versions","hotTag","keywords","keywords1","keywords2","keywords3","keywords4","keywords5","summary","ToSMS","ToEmail","notTrue","notTimely","notNormally","changReason","disponseOpinion","checkOneOpinion","checkTowOpinion","basilicType","zkmDocState","applyType","approveState","appointedCheckUser","disponseUser","disponseBTime","disponseETime","flowNode","flowType","securityUserId","securityUser","securityFolderID","securityFolderName","SMSSendPhone","contentPath","flowID","appointedCheckUserID","companyId","companyName","departmentId","departmentName","lastModiUser","lastModiTime","notTrueBZ","notTimelyBZ","notNormallyBZ"};
	
	public static void zkmDocDisponseSearch(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		List plist=new ArrayList();
		String sql="select " + DISPONSE_SEARCH_FIELDS + " from zkmInfoBaseDisponse where 1=1 ";
		String sqlWhere=" and disponseUser = ? ";
		plist.add(user.loginName2);
		for(String field:DISPONSE_SEARCH_TERMS){
			String []f=field.split(":");
			String f0=f[0];
			String f1=f[1];
			String v=wfu.get(f0);
			if(v.length()>0){
				if("=".equals(f1)){
					sqlWhere+=" and " + f0 + "=?";
					plist.add(v);
				}else if("like".equals(f1)){
					v=v.trim().replaceAll("\\*", "%");
					plist.add(v);
					sqlWhere+=" and " + f0 + " like ?";
				}
			}
		}
		sql+=sqlWhere;
		if(wfu.get("orderField").length()>0 && wfu.get("orderType").length()>0){
			sql+=" order by " + wfu.get("orderField") + " " + wfu.get("orderType");
		}else{
			sql+=" order by disponseBTime desc";
		}
		try{
			List list=DAOTools.queryMap(sql, plist, zkmDBId);
			Map rmap = new HashMap();
			rmap.put("success", true);
			rmap.put("data", list);
			postStrToClient(JSONSerializer.toJSON(rmap).toString(), response);
		}catch(Exception x){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "获取待处理数据异常。"), response);
			logger.error("zkmDocDisponseSearch ---- error :" + x.getMessage());
			x.printStackTrace();
		}
	}
	
	public static void zkmDocDisponseActive(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		if(wfu.get("id").length()>0){
			postStrToClient(JSONSerializer.toJSON(zkmDocDisponseEdit(wfu,user)).toString(), response);
		}else{
			postStrToClient(JSONSerializer.toJSON(zkmDocDisponseAdd(wfu,user)).toString(), response);
		}
	}
	
	public static HashMap zkmDocDisponseAdd(WebFormUtil wfu, UserInfo2 user){
		String title=wfu.get("title");
		String applyType=wfu.get("applyType");
		String companyId=wfu.get("companyId");
		if(title.length()>0){
			title=title.trim();
			try{
				boolean has=ZkmCommons.vaildateTitleForCF(title,applyType,companyId);
				if(has){
					return JsonUtils.genUpdateDataReturnMap(false, "标题：“"+ title +"”已经存在，新增数据失败。",null);
				}
			}catch(Exception x){
				logger.error(x.getMessage());
				x.printStackTrace();
				return JsonUtils.genUpdateDataReturnMap(false, "新增数据异常失败。",null);
			}
		}else{
			return JsonUtils.genUpdateDataReturnMap(false, "新增数据失败，缺少标题。",null);
		}
		String id=new RandomGUID().toString();
		String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
		String sql="insert into zkmInfoBaseDisponse ";
		List plist=new ArrayList();
		String fields="(id,";
		String values="(?,";
		plist.add(id);
		for(int i=0;i<DISPONSE_UPDATE_FIELDS.length;i++){
			String f=DISPONSE_UPDATE_FIELDS[i];
			String v=wfu.get(f);
			if(("bDate".equals(f) || "eDate".equals(f) || "disponseETime".equals(f) || "lastModiTime".equals(f)) && v.length()<=0){
				v="0000-00-00 00:00:00";
			}else if("createTime".equals(f) || "disponseBTime".equals(f)){
				v=date;
			}else if("createUser".equals(f) || "disponseUser".equals(f)){
				v=user.loginName2;
			}
			if(i+1<DISPONSE_UPDATE_FIELDS.length){
				fields+=f+",";
				values+="?,";
			}else{
				fields+=f+") ";
				values+="?) ";
			}
			plist.add(v);
		}
		sql=sql + fields + " values " + values;
		try{
			DAOTools.updateForPrepared(sql,plist, zkmDBId);
			//防止数据复制慢，导致本地数据问题。
			Thread.sleep(200);
			Map idata=zkmDocDisponseSelectOne(id);
			if(idata!=null){
				return JsonUtils.genUpdateDataReturnMap(true,"数据保存成功。",idata);
			}else{
				return JsonUtils.genUpdateDataReturnMap(false, "新增数据异常失败。",null);
			}
		}catch(Exception x){
			logger.error(x.getMessage());
			x.printStackTrace();
			return JsonUtils.genUpdateDataReturnMap(false, "新增数据异常失败。",null);
		}
	}
	
	public static HashMap zkmDocDisponseEdit(WebFormUtil wfu, UserInfo2 user){
		String id=wfu.get("id");
		String title=wfu.get("title");
		String applyType=wfu.get("applyType");
		String companyId=wfu.get("companyId");
		if(title.length()>0){
			title=title.trim();
			try{
				boolean has=ZkmCommons.vaildateTitleForCF(title,applyType,companyId);
				if(has){
					return JsonUtils.genUpdateDataReturnMap(false, "标题：“"+ title +"”已经存在，修订数据失败。",null);
				}
			}catch(Exception x){
				logger.error(x.getMessage());
				x.printStackTrace();
				return JsonUtils.genUpdateDataReturnMap(false, "修订数据异常失败。",null);
			}
		}else{
			return JsonUtils.genUpdateDataReturnMap(false, "修订数据失败，缺少标题。",null);
		}
		if(id.length()>0){
			String sql="update zkmInfoBaseDisponse set ";
			List plist =new ArrayList();
			String sets="";
			for(int i=0;i<DISPONSE_UPDATE_FIELDS.length;i++){
				String f=DISPONSE_UPDATE_FIELDS[i];
				String v=wfu.get(f);
				if(("bDate".equals(f) || "eDate".equals(f) || "disponseETime".equals(f) 
						|| "createTime".equals(f) || "disponseBTime".equals(f) || "lastModiTime".equals(f)) && v.length()<=0){
					v="0000-00-00 00:00:00";
				}
				plist.add(v);
				if(i+1 <DISPONSE_UPDATE_FIELDS.length){
					sets+=f + "=?,";
				}else{
					sets+=f + "=? ";
				}
			}
			plist.add(id);
			sql+=sets + " where id=?";
			try{
				DAOTools.updateForPrepared(sql,plist, zkmDBId);
				//防止数据复制慢，导致本地数据问题。
				Thread.sleep(200);
				Map idata=zkmDocDisponseSelectOne(id);
				if(idata!=null){
					return JsonUtils.genUpdateDataReturnMap(true, "数据保存成功。",idata);
				}else{
					return JsonUtils.genUpdateDataReturnMap(false, "更新数据异常失败。",null);
				}
			}catch(Exception x){
				logger.error(x.getMessage());
				x.printStackTrace();
				return JsonUtils.genUpdateDataReturnMap(false, "更新数据异常失败。",null);
			}
		}else{
			return JsonUtils.genUpdateDataReturnMap(false, "更新数据失败，缺少必要参数。",null);
		}
	}
	
	public static Map zkmDocDisponseSelectOne(String id) throws Exception{
		String sql="select " + DISPONSE_SEARCH_FIELDS + " from zkmInfoBaseDisponse where id='"+ id +"' ";
		List<Map> list=DAOTools.queryMap(sql, zkmDBId);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return  null;
		}
	}
	
	public static void zkmDocSubmitFlow(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String id=wfu.get("id");
		String sb=wfu.get("approveState");
		String flowType=wfu.get("flowType");
		String flowNode=wfu.get("flowNode");
		String flowID=wfu.get("flowID");
		boolean isStart=false;
		boolean isEnd=false;
		String sql="";
		Map dataMap=null;
		//验证ID存在
		if(id.length()<=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败，数据未保存。"), response);
			return;
		}
		//保存更新前数据，用于保存历史版本
		Map hisMap=null;
		//保存数据并获取数据内容
		try{
			Map saver=null;
			if(wfu.get("id").length()>0){
				//保持数据
				sql="select * from zkmInfoBaseDisponse where id='" + wfu.get("id") + "'";
				List hisList=DAOTools.queryMap(sql, zkmDBId);
				if(hisList!=null && hisList.size()>0){
					hisMap=(Map)hisList.get(0);
				}
				//更新并获取内容
				saver=zkmDocDisponseEdit(wfu,user);
			}else{
				//saver=zkmDocDisponseAdd(wfu,user);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败，数据未保存。"), response);
				return;
			}
			if(((Boolean)saver.get("success")).booleanValue()){
				dataMap=(Map)saver.get("data");
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败，数据未保存或数据异常。"), response);
				return;
			}
		}catch(Exception x){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转由于异常失败－数据库异常"), response);
			logger.error(x.getMessage());
			x.printStackTrace();
			return;
		}
		if(sb.length()<=0 || flowType.length()<=0 || flowNode.length()<=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败，缺少必要参数。"), response);
		}else{
			try {
				isStart=FlowHelper.isStart(flowType, flowNode);
				isEnd=FlowHelper.isEnd(flowType, flowNode);
			} catch (Exception x) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转由于异常失败－无法获取必要条件"), response);
				logger.error(x.getMessage());
				x.printStackTrace();
				return;
			}
			FlowDisponse flowd=FlowHelper.getFlowDisponse();
			//记录操作日志的对象
			ZkmSysDBLogService log2db=new ZkmSysDBLogService();
			if("待审批".equals(sb)){
				if(isStart){
					Map next=null;
					try{
						next=FlowHelper.getNext(flowType, flowNode);
						if(next!=null){
							Map reMap=flowd.disponseFirst(next, dataMap, null);
							String mgs=reMap.get("mgs")==null?"":(String)reMap.get("mgs");
							String dataId=dataMap.get("id")==null?"":(String)dataMap.get("id");
							String title=dataMap.get("title")==null?"":(String)dataMap.get("title");
							String userName=user.loginName2;
							String applyType=dataMap.get("applyType")==null?"":(String)dataMap.get("applyType");
							
							if(((Boolean)reMap.get("success")).booleanValue()){
								//记录经办统计数据
								if(applyType.equals(ZKMFlowDisponse.applyTypeDDefined[0])){
									//新建
									//记录操作日志
									String content="经办待审批文档：“" + title + "”（新建）";
									log2db.sysZkmOperationLogSave(dataId, userName, flowType,content, "经办",request);
									//记录统计数据
									ZZkmRecordClickUtils.zkmJBAdd(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
								}else if(applyType.equals(ZKMFlowDisponse.applyTypeDDefined[1])){
									//修订
									//记录操作日志
									String content="经办待审批文档：“" + title + "”（修订）";
									log2db.sysZkmOperationLogSave(dataId, userName, flowType,content, "经办",request);
									//记录统计数据
									ZZkmRecordClickUtils.zkmJBModi(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
								}
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, mgs,reMap), response);
							}else{
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, mgs,reMap), response);
							}
						}else{
							postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-下一节点为空。"), response);
						}
					}catch(Exception x){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转由于异常失败。"), response);
						logger.error(x.getMessage());
						x.printStackTrace();
					}
				}else{
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-非起始节点，请选择“通过或拒绝”"), response);
				}
			}else if("通过".equals(sb)){
				if(isStart && isEnd){
					String flowId= new RandomGUID().toString();
					dataMap.put("flowID",flowId);
					sql="update zkmInfoBaseDisponse set flowID=? where id=?";
					try{
						DAOTools.updateForPrepared(sql, new String[]{flowId,wfu.get("id")}, zkmDBId);
						Map map=FlowHelper.getNode(flowType, flowNode);
						if(map!=null){
							String dataId=dataMap.get("id")==null?"":(String)dataMap.get("id");
							String userName=user.loginName2;
							String applyType=dataMap.get("applyType")==null?"":(String)dataMap.get("applyType");
							String title=dataMap.get("title")==null?"":(String)dataMap.get("title");
							Map reMap=flowd.disponseEnd(map, dataMap, null);
							String mgs=reMap.get("mgs")==null?"":(String)reMap.get("mgs");
							if(((Boolean)reMap.get("success")).booleanValue()){
								//记录复核统计数据
								if(applyType.equals(ZKMFlowDisponse.applyTypeDDefined[0])){
									//新建
									//记录操作日志
									String content="复核发布文档：“" + title + "”（新建）";
									log2db.sysZkmOperationLogSave(dataId, userName,flowType, content,"发布",request);
									//记录统计数据
									ZZkmRecordClickUtils.zkmFHAdd(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
								}else if(applyType.equals(ZKMFlowDisponse.applyTypeDDefined[1])){
									//修订
									//记录操作日志
									String content="复核发布文档：“" + title + "”（修订）";
									log2db.sysZkmOperationLogSave(dataId, userName, flowType,content, "发布",request);
									//记录统计数据
									ZZkmRecordClickUtils.zkmFHModi(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
								}
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, mgs,reMap), response);
							}else{
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,mgs,reMap), response);
							}
							//防止数据复制慢，导致本地数据问题。
							Thread.sleep(200);
						}
					}catch(Exception x){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转由于异常失败。"), response);
						logger.error(x.getMessage());
						x.printStackTrace();
					}
				}else if(isStart && !isEnd){
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-起始节点，请选择“待审批”"), response);
				}else if(isEnd && !isStart){
					if(flowID.length()<=0){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-缺少必要参数。"), response);
						return;
					}
					try{
						Map map=FlowHelper.getNode(flowType, flowNode);
						if(map!=null){
							String dataId=dataMap.get("id")==null?"":(String)dataMap.get("id");
							String userName=user.loginName2;
							String applyType=dataMap.get("applyType")==null?"":(String)dataMap.get("applyType");
							String title=dataMap.get("title")==null?"":(String)dataMap.get("title");
							Map reMap=flowd.disponseEnd(map, dataMap, null);
							String mgs=reMap.get("mgs")==null?"":(String)reMap.get("mgs");
							if(((Boolean)reMap.get("success")).booleanValue()){
								//记录复核统计数据
								if(applyType.equals(ZKMFlowDisponse.applyTypeDDefined[0])){
									//新建
									//记录操作日志
									String content="复核发布文档：“" + title + "”（新建）";
									log2db.sysZkmOperationLogSave(dataId, userName,flowType, content,"发布",request);
									//记录统计数据
									ZZkmRecordClickUtils.zkmFHAdd(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
								}else if(applyType.equals(ZKMFlowDisponse.applyTypeDDefined[1])){
									//修订
									//记录操作日志
									String content="复核发布文档：“" + title + "”（修订）";
									log2db.sysZkmOperationLogSave(dataId, userName, flowType,content, "发布",request);
									//记录统计数据
									ZZkmRecordClickUtils.zkmFHModi(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
								}
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, mgs,reMap), response);
							}else{
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,mgs,reMap), response);
							}
							//防止数据复制慢，导致本地数据问题。
							Thread.sleep(200);
						}else{
							postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-当前结点定义为空。"), response);
						}
					}catch(Exception x){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转由于异常失败。"), response);
						logger.error(x.getMessage());
						x.printStackTrace();
					}
				}else{
					if(flowID.length()<=0){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-缺少必要参数。"), response);
						return;
					}
					Map next=null;
					try{
						next=FlowHelper.getNext(flowType, flowNode);
						if(next!=null){
							
							String dataId=dataMap.get("id")==null?"":(String)dataMap.get("id");
							String userName=user.loginName2;
							String applyType=dataMap.get("applyType")==null?"":(String)dataMap.get("applyType");
							String title=dataMap.get("title")==null?"":(String)dataMap.get("title");
							
							Map reMap=flowd.disponseNext(next, dataMap, null);
							String mgs=reMap.get("mgs")==null?"":(String)reMap.get("mgs");
							if(((Boolean)reMap.get("success")).booleanValue()){
								//记录复核统计数据
								if(applyType.equals(ZKMFlowDisponse.applyTypeDDefined[0])){
									//新建
									//记录操作日志
									String content="复核通过文档：“" + title + "”（新建）";
									log2db.sysZkmOperationLogSave(dataId, userName, flowType,content,"复核",request);
									//记录统计数据
									ZZkmRecordClickUtils.zkmFHAdd(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
								}else if(applyType.equals(ZKMFlowDisponse.applyTypeDDefined[1])){
									//修订
									//记录操作日志
									String content="复核通过文档：“" + title + "”（修订）";
									log2db.sysZkmOperationLogSave(dataId, userName, flowType,content, "复核",request);
									//记录统计数据
									ZZkmRecordClickUtils.zkmFHModi(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
								}
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, mgs,reMap), response);
							}else{
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, mgs,reMap), response);
							}
						}else{
							postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-下一节点为空。"), response);
						}
					}catch(Exception x){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转由于异常失败-无法获取下步节点。"), response);
						logger.error(x.getMessage());
						x.printStackTrace();
					}
				}
			}else if("拒绝".equals(sb)){
				if(isStart){
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-当前节点为开始节点。"), response);
				}else{
					if(flowID.length()<=0){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-缺少必要参数。"), response);
					}
					Map first=null;
					try{
						first=FlowHelper.getStart(flowType);
						//TODO:保存现有数据，找到起始流程数据。
						if(first!=null){
							
							String dataId=dataMap.get("id")==null?"":(String)dataMap.get("id");
							String userName=user.loginName2;
							String cuser=dataMap.get("createUser")==null?"":(String)dataMap.get("createUser");
							String applyType=dataMap.get("applyType")==null?"":(String)dataMap.get("applyType");
							String title=dataMap.get("title")==null?"":(String)dataMap.get("title");
							
							Map reMap=flowd.disponseToFirst(first, dataMap, null);
							if(((Boolean)reMap.get("success")).booleanValue()){
								//记录复核统计数据
								if(applyType.equals(ZKMFlowDisponse.applyTypeDDefined[0])){
									//新建
									//记录操作日志
									String content="复核退回文档：“" + title + "”（新建）";
									log2db.sysZkmOperationLogSave(dataId, userName,flowType, content, "退回",request);
									//记录统计数据
									ZZkmRecordClickUtils.zkmFHAdd(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
								}else if(applyType.equals(ZKMFlowDisponse.applyTypeDDefined[1])){
									//修订
									//记录操作日志
									String content="复核退回文档：“" + title + "”（修订）";
									log2db.sysZkmOperationLogSave(dataId, userName, flowType,content, "退回",request);
									//记录统计数据
									ZZkmRecordClickUtils.zkmFHModi(dataId,userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
								}
								//记录复核退回统计数据
								ZZkmRecordClickUtils.zkmFHToBack(dataId, userName,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
								//记录经办被退回统计数据
								ZZkmRecordClickUtils.zkmJBToBack(dataId, cuser,userName,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "流程提交成功。",reMap), response);
							}else{
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "",reMap), response);
							}
						}else{
							postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-没有找到第一节点设置。"), response);
						}
					}catch(Exception x){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败-获取起始节点异常。"), response);
						logger.error(x.getMessage());
						x.printStackTrace();
					}
				}
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "流程流转失败，选择了非法审批状态。"), response);
			}
		}
	}
	
	public static void getHotTags(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String tagType=wfu.get("tagType");
		if(tagType.length()<=0){
			tagType="知识库标签";
		}
		String sql="select * from zkmHotTag where tagType=?";
		try{
			List list=DAOTools.queryMap(sql,new String[]{tagType},zkmDBId);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"标签加载成功。",list),response);
		}catch(Exception x){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "标签列表加载异常失败。"), response);
			logger.error(x.getMessage());
			x.printStackTrace();
		}
	}
	
	public static void getUsers(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String sql="select * from suSecurityUser";
		try{
			List list=DAOTools.queryMap(sql,zkmDBId);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"用户加载成功。",list),response);
		}catch(Exception x){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户列表加载异常失败。"), response);
			logger.error(x.getMessage());
			x.printStackTrace();
		}
	}
	
	public static void getFolders(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String recordType=wfu.get("recordType");
		if(recordType.length()<=0){
			recordType="d";
		}
		String sql="select id,text,iconCls,cls,leaf,recordType,parentId from zkmInfoBase where recordType=? and isdel<>1";
		try{
			List list=DAOTools.queryMap(sql,new String[]{recordType},zkmDBId);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"目录列表加载成功。",list),response);
		}catch(Exception x){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "目录列表加载异常失败。"), response);
			logger.error(x.getMessage());
			x.printStackTrace();
		}
	}
	
	public static void setZkmDisponseContent(HttpServletRequest request, HttpServletResponse response) {
		WebFormUtil wfu=new WebFormUtil(request);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(setZkmDisponseContent(wfu)), response);
	}
	
	public static HashMap setZkmDisponseContent(WebFormUtil wfu){
		String docId = wfu.get("id");
		String data = wfu.get("data");
		String contentPath = wfu.get("contentPath");
		//System.out.println(data);
		String dataPath= wfu.get("path");
		if(contentPath.length()<=0){
			return JsonUtils.genUpdateDataReturnMap(false, "正文保存失败，缺少必要参数。",null);
		}
		if(docId.length()>0){
			Map map=null;
			try{
				map=zkmDocDisponseSelectOne(docId);
			}catch(Exception x){
				logger.error(x.getMessage());
				x.printStackTrace();
				return JsonUtils.genUpdateDataReturnMap(false, "正文保存失败，获取数据记录异常。",null);
			}
			if(map!=null && map.get("id")!=null && !"".equals(map.get("id"))){
				File savePath=null;
				boolean isbl=false;
				if(dataPath.length()>0){
					savePath=new File(dataPath);
					if(savePath.exists()&& savePath.isFile() && savePath.canWrite()){
						isbl=true;
					}else{
						isbl=false;
					}
				}
				String htmlFixPath="";
				if(!isbl){
					String rootpath=ZKMConfs.confs.getProperty("zkmFlowDocSaveDir","/usr/local/nginx/html/ZDesk/ZKM/zkmDocs/flowDisponseDocs/");
					savePath=new File(dataPath);
					htmlFixPath=dataPath;
					if(!savePath.exists()){
						savePath.mkdirs();
						ZKMAPPUtils.fcdirChangeUserAndGroup(savePath);
					}
				}
				try{
					String filepath=savePath.getPath() + "/index.html";
					File wf=new File(filepath);
					//data=ZKMDocHelper.clearWordFormat(data);
					//修正文档所需的参数
					Map genMap=new HashMap();
					//文档所在的目录
					genMap.put("disponseFilePath", htmlFixPath);
					
					//data=ZKMDocHelper.zkmDocFixAndGen(data,genMap);
					//FileUtils.appendToFile(data, wf, false);
					//word 另存格式使用GB2312编码
					//FileUtils.appendToFile(data, wf, "GB2312",false);
					File sf=new File(contentPath);
					if(sf.exists() && sf.isFile()){
						org.apache.commons.io.FileUtils.copyFile(sf, wf);
						ZKMAPPUtils.fileChangeUserAndGroup(wf);
						String sql="update zkmInfoBaseDisponse set contentPath=? where id=?";
						DAOTools.updateForPrepared(sql, new String[]{wf.getPath(),docId}, zkmDBId);
						Map rm=new HashMap();
						rm.put("id", docId);
						rm.put("contentPath", wf.getPath());
						return JsonUtils.genUpdateDataReturnMap(true, "正文保存成功。",rm);
					}else{
						return JsonUtils.genUpdateDataReturnMap(false, "正文保存失败，未发现数据文件。",null);
					}
				}catch(Exception x){
					logger.error(x.getMessage());
					x.printStackTrace();
					return JsonUtils.genUpdateDataReturnMap(false, "正文保存失败，文件写入异常或数据库保存异常。",null);
				}
			}else{
				return JsonUtils.genUpdateDataReturnMap(false, "正文保存失败，未找到对应文档。",null);
			}
		}else{
			return JsonUtils.genUpdateDataReturnMap(false, "正文保存失败，缺少必要参数。",null);
		}
	}
	
	public static void getZkmDisponseContent(HttpServletRequest request, HttpServletResponse response) {
		WebFormUtil wfu=new WebFormUtil(request);
		String id = wfu.get("id");
		String path = wfu.get("path");
		if(id.length()>0){
			if(path.length()<=0){
				Map map=null;
				try{
					map=zkmDocDisponseSelectOne(id);
				}catch(Exception x){
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "数据读取异常失败。"), response);
					logger.error(x.getMessage());
					x.printStackTrace();
				}
				if(map!=null && map.get("id")!=null && !"".equals(map.get("id"))){
					path=(String)map.get("contentPath");
				}else{
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "正文读取失败，未找到相关数据。"), response);
				}
			}
			if(path!=null && path.length()>0){
				File file=new File(path);
				if(file.exists() && file.isFile() && file.canRead()){
					String content="";
					try{
						content=getFileContentForFile(file.getPath());
						//System.out.println(content);
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "读取成功",content), response);
					}catch(Exception x){
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "正文读取失败，读取文件异常。"), response);
						logger.error(x.getMessage());
						x.printStackTrace();
					}
				}else{
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "正文读取失败，正文文件丢失。"), response);
				}
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "正文读取失败，未找到正文路径数据。"), response);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "正文读取失败，缺少必要参数。"), response);
		}
	}
	
	public static void delfujianModi(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!"_guest".equals(user.loginName2)) {
			String sql = "update `zkmInfoFile` set `isdel`=1,`delTime`=now(),`delUser`=? where id=?";
			String id = request.getParameter("id") == null ? "" : request.getParameter("id");
			if (id.length() > 0) {
				String[] ids = id.split(";");
				for (int i = 0; i < ids.length; i++) {
					if (ids[i] != null && ids[i].length() > 0) {
						DAOTools.updateForPrepared(sql, new String[] { user.loginName2, ids[i] }, zkmDBId);
					}
				}
			}
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "操作已完成。"), response);
		} else {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "非法访问－用户未登录。"), response);
		}
	}
	
	public static void getZKMDocFJFiles(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		WebFormUtil wfu=new WebFormUtil(request);
		String id = wfu.get("id");
		String ftype = wfu.get("ftype");
		if(id.length()>0){
			String sql="select * from zkmInfoFile where relationId = ? and `isdel` <> 1";
			if(ftype.length()>0){
				sql+=" and fileUse=?";
			}
			try{
				List list=null;
				if(ftype.length()>0){
					list=DAOTools.queryMap(sql,new String[]{id,ftype},zkmDBId);
				}else{
					list=DAOTools.queryMap(sql,new String[]{id},zkmDBId);
				}
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取附件成功。",list), response);
			}catch(Exception x){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "附件获取异常。"), response);
				logger.error(x.getMessage());
				x.printStackTrace();
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或超时。"), response);
		}
	}
	
	
	public static void getZKMDocFJFilesHistory(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		WebFormUtil wfu=new WebFormUtil(request);
		String id = wfu.get("id");
		String ftype = wfu.get("ftype");
		if(id.length()>0){
			String sql="select * from zkmHistoryInfoFile where relHistoryId = ? and `isdel` <> 1";
			if(ftype.length()>0){
				sql+=" and fileUse=?";
			}
			try{
				List list=null;
				if(ftype.length()>0){
					list=DAOTools.queryMap(sql,new String[]{id,ftype},zkmDBId);
				}else{
					list=DAOTools.queryMap(sql,new String[]{id},zkmDBId);
				}
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取附件成功。",list), response);
			}catch(Exception x){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "附件获取异常。"), response);
				logger.error(x.getMessage());
				x.printStackTrace();
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或超时。"), response);
		}
	}
	
	public static void getZKMDocWaitDocuments(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String flowType=wfu.get("flowType");
		if(flowType.length()>0){
			String sql="select " +DISPONSE_SEARCH_FIELDS + " from zkmInfoBaseDisponse where 1=1 ";
			List plist=new ArrayList();
			String sw=getZKMDocWaitSqlWhere(wfu,user,plist);
			sql+=sw;
			try{
				List list=DAOTools.queryMap(sql, plist, zkmDBId);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取未分配数据成功。",list), response);
			}catch(Exception x){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "获取未分配数据异常。"), response);
				logger.error(x.getMessage());
				x.printStackTrace();
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数。"), response);
		}
		
	}
	/**
	 * 待办池数据。
	 * @param user
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void getZKMDocWaitDocNum(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		if(!(user.checkUserTextSymbolSecurity("ZKMDocDisponseFHOne")) && !(user.checkUserTextSymbolSecurity("ZKMDocDisponseFHTwo"))){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户无权限查看未分配数据。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String flowType=wfu.get("flowType");
		String companyId=wfu.get("companyId");
		if(companyId.length()<=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数。"), response);
			logger.error("知识库获取待办，缺少必要参数。companyId");
			return;
		}
		if(flowType.length()>0){
			String sql="select count(1) num from zkmInfoBaseDisponse where 1=1 ";
			List plist=new ArrayList();
			String sw=getZKMDocWaitSqlWhere(wfu,user,plist);
			sql+=sw;
			try{
				List list=DAOTools.queryMap(sql, plist, zkmDBId);
				String num="0";
				if(list!=null && list.size()>0){
					Map map=(Map)list.get(0);
					num=map.get("num")==null?"0":(String)map.get("num");
				}
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取未分配数据成功。",num), response);
			}catch(Exception x){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "获取未分配数据异常。"), response);
				logger.error(x.getMessage());
				x.printStackTrace();
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数。"), response);
		}
	}
	
	public static String getZKMDocWaitSqlWhere(WebFormUtil wfu,UserInfo2 user,List plist) throws Exception{
		StringBuffer sw=new StringBuffer();
		if(plist==null){
			plist=new ArrayList();
		}
		String flowType=wfu.get("flowType");
		List slist=new ArrayList();
		if(flowType.length()>0){
			sw.append(" and flowType=?");
			plist.add(flowType);
		}
		List list=null;
		if(user.checkUserTextSymbolSecurity("ZKMDocDisponseFHTwo")){
			if( flowType.length()>0){
				list=FlowHelper.getSecurityFlowType(flowType, "ZKMDocDisponseFHTwo");
			}else{
				list=FlowHelper.getSecurityAllNode("ZKMDocDisponseFHTwo");
			}
			if(list!=null && list.size()>0){
				slist.addAll(list);
				list.clear();
			}
		}
		if(user.checkUserTextSymbolSecurity("ZKMDocDisponseFHOne")){
			if(flowType.length()>0){
				list=FlowHelper.getSecurityFlowType(flowType, "ZKMDocDisponseFHOne");
			}else{
				list=FlowHelper.getSecurityAllNode("ZKMDocDisponseFHOne");
			}
			if(list!=null && list.size()>0){
				slist.addAll(list);
				list.clear();
			}
		}
		if(slist!=null && slist.size()>0){
			sw.append(" and ( ");
			for(int i=0;i<slist.size();i++){
				Map map=(Map)slist.get(i);
				String nodeType=map.get("type")==null?"":(String)map.get("type");
				if(nodeType.length()>0){
					if(i+1>=slist.size()){
						sw.append(" flowNode=?)");
						plist.add(nodeType);
					}else{
						sw.append(" flowNode=? or ");
						plist.add(nodeType);
					}
				}
			}	
		}else{
			sw.append(" and 1=2 ");
		}
		sw.append(" and disponseUser = ?");
		plist.add("");
		sw.append(" and companyId=?");
		plist.add(wfu.get("companyId"));
		return sw.toString();
	}
	
	public static void setZKMDocWaitDocumentsToUser(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String ids_=wfu.get("ids");
		if(ids_.length()>0){
			String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
			String sql="update zkmInfoBaseDisponse set disponseUser=?,disponseBTime=? where id=? and disponseUser =?";
			String[] ids=ids_.split(",");
			boolean allUp=true;
			for(String id:ids){
				if(id!=null && id.length()>0){
					String[] p={user.loginName2,date,id,""};
					try{
						DAOTools.updateForPrepared(sql, p, zkmDBId);
					}catch(Exception x){
						allUp=false;
						logger.error("------ error get wait doc : " + id);
						logger.error(x.getMessage());
						x.printStackTrace();
					}
				}
			}
			if(allUp){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取待处理数据获取成功。"), response);
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取待处理数据获取，部分异常失败。"), response);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "未选择任何分配数据或缺少必要参数。"), response);
		}
	}
	
	public  static void modiDocsToDisponse(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		List mgsList=new ArrayList();
		if (user == null || "_guest".equals(user.loginName2)) {
			mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "用户未登录或没有权限。",null));
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "",mgsList), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String ids_=wfu.get("ids");
		String titles_=wfu.get("titles");
		if(ids_.length()>0){
			String[] ids=ids_.split(",");
			String[] titles=titles_.split("_y22y_");
			String date = DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
			String snode="";
			String fname="";
			for(int i=0;i<ids.length;i++){
				String id=ids[i];
				String title=titles[i];
				try{
					String sql="select id,versions,zkmDocState,flowType from zkmInfoBaseDisponse where id=?";
					List list=DAOTools.queryMap(sql, new String[]{id},zkmDBId);
					String version="0.01";
					if(list!=null && list.size()>0){
						Map smap=(Map)list.get(0);
						version=smap.get("versions")==null?"0.01":(String)smap.get("versions");
						if(version.length()<=0){
							version="0.01";
						}
						String zse=smap.get("zkmDocState")==null?"":(String)smap.get("zkmDocState");
						String flowType=smap.get("flowType")==null?"":(String)smap.get("flowType");
						if(flowType.length()>0){
							Map map=FlowHelper.getStart(flowType);
							Map flow=FlowHelper.getFlow(flowType);
							if(flow==null){
								mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "获取流程定义失败，中止修订。",null));
								continue;
							}
							if(map==null){
								mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "获取流程定义失败，中止修订。",null));
								continue;
							}
							fname=flow.get("name")==null?"":(String)flow.get("name");
							snode=map.get("type")==null?"":(String)map.get("type");
							if(snode.length()<=0){
								mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "文档数据损坏，无法进行修订。标题:" + title,null));
								continue;
							}
						}else{
							mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "文档数据缺失，无法进行修订。标题:" + title,null));
							continue;
						}
						if("已发布".equals(zse)){
							float vff=Float.parseFloat(version);
							vff=vff+Float.parseFloat("0.01");
							java.text.DecimalFormat df = new java.text.DecimalFormat("#0.##");
							String vf=df.format(vff);
							sql="update zkmInfoBaseDisponse set zkmDocState=?,applyType=?,approveState=?,disponseUser=?,disponseBTime=?,flowNode=?,versions=?,createUser=?,notTrue=?,notTimely=?,notNormally=?,notTrueBZ='',notTimelyBZ='',notNormally='' where id=?";
							String[] p={"起草","修订","待审批",user.loginName2,date,snode,vf,user.loginName2,"N","N","N",id};
							DAOTools.updateForPrepared(sql, p,zkmDBId);
							//记录操作日志
							ZkmSysDBLogService log2db=new ZkmSysDBLogService();
							String content="修订文档，文档：“" + title +"”";
							log2db.sysZkmOperationLogSave(id, user.loginName2, "zkmDocModi", content, "修订",request);
							mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "文档：“"+ title +"”启动修订流程，请查看您的“"+fname+"”待办列表。",null));
						}else{
							mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "文档状态为：" +zse + " 无法进行修订。标题:" + title,null));
						}
					}else{
						mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "所选文档：“" + title +"”，主文档被删除，无法修订。",null));
					}
				}catch(Exception x){
					logger.error("------ error get relation : " + id);
					logger.error(x.getMessage());
					x.printStackTrace();
					mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "获取主文档异常，中止修订启动修订流程。标题:" + title,null));
				}
			}
		}else{
			mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "所选文档被删除或被重新发布，无法进行修订。",null));
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mgsList),response);
	}
	
	public static void getZKMDocRelationId(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		WebFormUtil wfu=new WebFormUtil(request);
		String id=wfu.get("id");
		if(id.length()>0){
			String rid="";
			try{
				List dlist=DataRelationComponent.getRelationForDistId(id,ZkmCommons.ZKM_FB_RLEATION_KEY);
				if(dlist!=null && dlist.size()>0){
					Map data=(Map)dlist.get(0);
					rid=data.get("srcId")==null?"":(String)data.get("srcId");
				}
			}catch(Exception x){
				logger.error("------ error get relation : " + id);
				logger.error(x.getMessage());
				x.printStackTrace();
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "读取异常失败。"), response);
				return;
			}
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "成功",rid), response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "读取错误，缺少必要参数。"), response);
		}
	}
	
	public static void stopZkmDocs(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		List mgsList=new ArrayList();
		if (user == null || "_guest".equals(user.loginName2)) {
			mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "用户未登录或没有权限。",null));
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "",mgsList), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String ids_=wfu.get("ids");
		String titles_=wfu.get("titles");
		if(ids_.length()>0){
			String[] ids=ids_.split(",");
			String[] titles=titles_.split("_y22y_");
			String snode="";
			String fname="";
			for(int i=0;i<ids.length;i++){
				String id=ids[i];
				String title=titles[i];
				try{
					//验证是否已被停用
					String sql="select id,zkmDocState,flowType from zkmInfoBaseDisponse where id=?";
					List list=DAOTools.queryMap(sql, new String[]{id},zkmDBId);
					if(list!=null && list.size()>0){
						Map smap=(Map)list.get(0);
						String zse=smap.get("zkmDocState")==null?"":(String)smap.get("zkmDocState");
						ZKMFlowDisponse fds=new ZKMFlowDisponse();
						String stopStauts=fds.zkmDocStateDefined[2];
						boolean doStop=false;
						//验证
						if(stopStauts.equals(zse)){
							mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "文档：“"+ title +"”，停用成功。",null));
							ZZkmRecordClickUtils.zkmDoStopRecord(id,user.loginName2,request);
							ZZkmRecordClickUtils.zkmFHStop(id, user.loginName2,user.loginName2,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
							continue;
						}
						//进行停用
						try{
							ZkmSysDBLogService log2db=new ZkmSysDBLogService();
							//清理关联数据
							DataRelationComponent.delRelationSrcId(id, ZkmCommons.ZKM_FB_RLEATION_KEY);
							//清理数据
							fds.clearData(id);
							sql="update zkmInfoBaseDisponse set zkmDocState=?,lastModiTime=now(),eDate=now() where id=?";
							String[] p={stopStauts,id};
							DAOTools.updateForPrepared(sql, p, zkmDBId);
							mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "文档：“"+ title +"”，停用成功。",null));
							//记录操作日志
							String content="停用文档，标题：“"+ title +"”";
							log2db.sysZkmOperationLogSave(id, user.loginName2, "zkmDocStop", content, "停用",request);
							//记录统计数据
							ZZkmRecordClickUtils.zkmDoStopRecord(id,user.loginName2,request);
							ZZkmRecordClickUtils.zkmFHStop(id, user.loginName2,user.loginName2,ZZkmRecordClickUtils.ZKM_DF_TYPE_ZKMDOC,request);
						}catch(Exception x){
							logger.error("------ error get relation : " + id);
							logger.error(x.getMessage());
							x.printStackTrace();
							mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "文档：“"+ title +"”，由于异常，停用失败。",null));
						}
					}else{
						mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "所选文档：“" + title +"”，主文档被删除，无法停用。",null));
					}
				}catch(Exception x){
					logger.error("------ error get relation : " + id);
					logger.error(x.getMessage());
					x.printStackTrace();
					mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "获取主文档异常，中止停用。标题:" + title,null));
				}
			}
		}else{
			mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "所选文档被删除或被重新发布，无法进行停用。",null));
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",mgsList),response);
	}
	
	public static void zkmDocDel(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		List mgsList=new ArrayList();
		if (user == null || "_guest".equals(user.loginName2)) {
			mgsList.add(JsonUtils.genUpdateDataReturnMap(false, "用户未登录或没有权限。",null));
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "",mgsList), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String id=wfu.get("id");
		
		if(id.length()>0){
			String sql="select * from DataRelationComponent where srcId=?";
			try{
				List list=DAOTools.queryMap(sql, new String[] {id},zkmDBId);
				if(list.size()>0){
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "被删除数据为“已发布数据”，不可以进行删除。",null),response);
				}else{
					sql="delete from zkmInfoBaseDisponse where id=?";
					DAOTools.updateForPrepared(sql, new String[]{id}, zkmDBId);
					//记录操作日志
					ZkmSysDBLogService log2db=new ZkmSysDBLogService();
					log2db.sysZkmOperationLogSave(id, user.loginName2, "zkmDocDel", "删除流程中的知识文档数据", "删除",request);
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "数据删除成功。",null),response);
				}
			}catch(Exception x){
				logger.error("------ error get relation : " + id);
				logger.error(x.getMessage());
				x.printStackTrace();
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "数据删除失败，异常。",null),response);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "数据删除失败，缺少关键参数。",null),response);
		}
	}	
	public static void getVaildateFolders(UserInfo2 user, HttpServletRequest request, HttpServletResponse response){
		WebFormUtil wfu=new WebFormUtil(request);
		String type=wfu.get("recordType");
		String pid=wfu.get("parentId");
		String name=wfu.get("text");
		String companyId=wfu.get("companyId");
		name=name.trim();
		if(type.length()>0 && name.length()>0){
			String sql="select count(1) num from zkmInfoBase where recordType=? and parentId=? and text=? and isdel<>'1' and companyId=?";
			String [] p={type,pid,name,companyId};
			try{
				List rlist=DAOTools.queryMap(sql, p, zkmDBId);
				Map map=(HashMap)rlist.get(0);
				String num=(String)map.get("num");
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",num),response);
			}catch(Exception x){
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "无法进行有效性验证，异常。",null),response);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "无法进行有效性验证，缺少关键参数。",null),response);
		}
	}
	
	public static void getZkmFlowHistory(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String id=wfu.get("id");
		String type=wfu.get("type");
		if(type.length()<=0){
			type="flowHistory";
		}
		if(id.length()>0){
			String sql="select " + DISPONSE_SEARCH_FIELDS + " from zkmInfoBaseDisponseHistory where historyType=? and relId=? order by  disponseBTime desc";
			String[] p={type,id};
			List list=DAOTools.queryMap(sql, p, zkmDBId);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",list),response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "无法获取生命周期，缺少必要参数。",null),response);
		}
	}
	
	public static void getZkmDocUserFavoritesList(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String pid=wfu.get("pid");
		String type=wfu.get("type");
		String sql="select zib.* from DataRelationComponent drc,zkmInfoBase zib where drc.`distId`=zib.id and drc.`srcId`='" + pid +"' and rType='"+ type +"'";
		if(pid.length()>0 && type.length() > 0){
			try{
				List list=DAOTools.queryMap(sql, zkmDBId);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",list),response);
			}catch(Exception x){
				logger.error("getZkmDocUserFavoritesList -- error : " + x.getMessage());
				x.printStackTrace();
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "获取列表由于异常失败。",null),response);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "获取列表异常，缺少必要参数。",null),response);
		}
	}
	
	public static void getCommonZkmTree(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		WebFormUtil wfu=new WebFormUtil(request);
		String treeKey=wfu.get("treeKey");
		String reLoad=wfu.get("reLoad");
		String companyId=wfu.get("companyId");
		boolean rel=false;
		if(reLoad.length()>0){
			rel=(Boolean.valueOf(reLoad)).booleanValue();
		}
		if(treeKey.length()>0){
			List list=UITreeHelper.getTreeData(treeKey, rel);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",list),response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少关键参数。"),response);
		}
	}
	
	public static void zkmUnZipContent(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		WebFormUtil wfu=new WebFormUtil(request);
		String id=wfu.get("id");
		String filePath=wfu.get("filePath");
		Map data=zkmDocDisponseSelectOne(id);
		if(id.length()<=0 || filePath.length()<=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少关键参数。"),response);
			return;
		}
		if(data!=null){
			File zfile=new File(filePath);
			String title=data.get("title")==null?"":(String)data.get("title");
			if(zfile.exists() && zfile.isFile() && zfile.canRead()){
				boolean isZip=false;
				boolean unZip=false;
				String en=FileUtils.getFileExtendName(zfile);
				if(en.length()>0 && en.toUpperCase().equals(".ZIP")){
					isZip=true;
				}
				if(isZip){
					String tmpDir=ZKMConfs.confs.getProperty("mergeTempDir","/mnt/zkm_temp_F");
					File unzipf=FileUtils.createFolderUseSystemDate(tmpDir);
					String fName=new RandomGUID().toString();
					unzipf=new File(unzipf.getPath() + "/" + fName);
					ZKMAPPUtils.fcdirChangeUserAndGroup(unzipf);
					try{
						FileUtils.unzip(zfile, unzipf);
						ZKMAPPUtils.dirChangeUserAndGroup(unzipf,true);
						unZip=true;
					}catch(Exception x){
						logger.error("解压文件－异常失败。");
						x.printStackTrace();
						unZip=false;
					}
					if(unZip){
						File savePath=null;
						//String lastDir=new RandomGUID().toString();
						String rootpath=ZKMConfs.confs.getProperty("zkmFlowDocSaveDir","/usr/local/nginx/html/ZDesk/zkmDocs/flowDisponseDocs/");
						String dataFolder=FileUtils.getDateFolderStr();
						String distFolder=rootpath + "/" +  dataFolder + "/";
						savePath=new File( distFolder + fName);
						boolean copySuccess=true;
						try{
							if(savePath.exists()){
								org.apache.commons.io.FileUtils.deleteDirectory(savePath);
							}
							File df=new File(distFolder);
							if(!df.exists()){
								df.mkdirs();
							}
							ZKMAPPUtils.doCommonShellMove(unzipf.getPath(), distFolder);
							ZKMAPPUtils.fcdirChangeUserAndGroup(savePath);
							//org.apache.commons.io.FileUtils.copyDirectory(unzipf, savePath);
							ZKMAPPUtils.dirChangeUserAndGroup(savePath, true);
						}catch(Exception x){
							logger.error("导入复制异常。" + x.getMessage());
							x.printStackTrace();
							copySuccess=false;
						}
						if(copySuccess){
							File [] cfs=savePath.listFiles();
							File contentFile=null;
							List<File> htmllist=new ArrayList<File>();
							List<Map> relist=new ArrayList<Map>();
							for(File cf:cfs){
								if(cf.isFile()&& cf.canRead()){
									String fn=cf.getName();
									String enn=FileUtils.getFileExtendName(cf);
									boolean ishtml=false;
									if(enn.toUpperCase().equals(".HTML")){
										htmllist.add(cf);
										ishtml=true;
									}else if(enn.toUpperCase().equals(".HTM")){
										htmllist.add(cf);
										ishtml=true;
									}
									if(ishtml){
										Map map=new HashMap();
										map.put("fileName", cf.getName());
										map.put("filePath", cf.getPath());
										map.put("id", id);
										map.put("savePath", savePath.getPath());
										relist.add(map);
									}
								}
							}
							if(htmllist.size()<=0){
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "数据包未包含任何正文文件（*.html;*.htm）。"),response);
								return;
							}
							if(htmllist.size()==1){
								contentFile=htmllist.get(0);
							}else{
								Map manyMap=new HashMap();
								manyMap.put("flist", relist);
								manyMap.put("id", id);
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取成功",manyMap),response);
								return;
							}
							if(contentFile==null){
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "正文建立失败，未找到ZIP包中的正文文件。"),response);
							}else{
								wfu.set("id", id);
								wfu.set("savePath", savePath.getPath());
								wfu.set("contentFile", contentFile.getPath());
								postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(zkmContentImpSet(wfu)),response);
							}
						}else{
							postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "正文建立失败，异常。"),response);
						}
					}else{
						postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "ZIP包解压失败，异常。"),response);
					}
				}else{
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "请上传ZIP格式的压缩包。"),response);
				}
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "未找到正文压缩包。"),response);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "未找到关键数据或数据缺失。"),response);
		}
	}
	
	public static void zkmContentImpSet(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		WebFormUtil wfu=new WebFormUtil(request);
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(zkmContentImpSet(wfu)),response);
	}
	
	public static HashMap zkmContentImpSet(WebFormUtil wfu) throws Exception{
		String id=wfu.get("id");
		String sp=wfu.get("savePath");
		String cf=wfu.get("contentFile");
		if(sp.length()==0){
			return JsonUtils.genUpdateDataReturnMap(false, "缺少必要参数1。",null);
		}
		if(cf.length()==0){
			return JsonUtils.genUpdateDataReturnMap(false, "缺少必要参数2。",null);
		}
		File savePath=new File(sp);
		if(savePath.exists() && savePath.isDirectory()){
			
		}else{
			return JsonUtils.genUpdateDataReturnMap(false, "缺少必要参数3。",null);
		}
		File contentFile=new File(cf);
		if(contentFile.exists() && contentFile.isFile()){
			
		}else{
			return JsonUtils.genUpdateDataReturnMap(false, "缺少必要参数4。",null);
		}
		boolean contentSet=true;
		HashMap docData=null;
		try{
			String content=FileUtils.getFileRealContent(contentFile,"GB2312");
			wfu.set("id", id);
			wfu.set("data", content);
			wfu.set("contentPath", contentFile.getPath());
			wfu.set("path", savePath.getPath());
			docData=ZKMDocServlet.setZkmDisponseContent(wfu);
			if(((Boolean)docData.get("success")).booleanValue()){
				contentSet=true;
			}else{
				contentSet=false;
			}
		}catch(Exception x){
			logger.error("正文建立异常：" + x.getMessage());
			x.printStackTrace();
			contentSet=false;
		}
		if(contentSet){
			return JsonUtils.genUpdateDataReturnMap(true, "正文建立成功。",docData.get("data"));
		}else{
			return JsonUtils.genUpdateDataReturnMap(false, "正文建立失败，正文写入失败。",null);
		}
	}
	
	public static void getZKMDisponseContentPath(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		WebFormUtil wfu=new WebFormUtil(request);
		String id=wfu.get("id");
		if(id.length()>0){
			Map data=zkmDocDisponseSelectOne(id);
			if(data!=null){
				String path=data.get("contentPath")==null?"":(String)data.get("contentPath");
				if(path.length()>0){
					String filePath=ZKMAPPUtils.getFixZKMDisponseContentPath(path);
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true,"OK",filePath),response);
				}else{
					postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"数据异常,缺少正文。"),response);
				}
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"数据异常。"),response);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false,"缺少必要参数。"),response);
		}
	}
	
	public static void setZkmDocSecurity(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String ids=wfu.get("dids");
		String type=wfu.get("type");
		String userIds=wfu.get("userIds");
		String userNames=wfu.get("userNames");
		if(ids.length()<=0 || type.length()<=0 || userIds.length()<=0 || userNames.length()<=0){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数，授权失败。"), response);
			return;
		}
		String[] uids=userIds.split(",");
		String[] uns=userNames.split(",");
		if(uids.length!=uns.length){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数验证异常，请重新选择授权。"), response);
			return;
		}
		try{
			if("d".equals(type)){
				String[] ids_=ids.split(",");
				List<String> af=ZkmCommons.getZkmFolderFiles(ids_);
				if(af!=null && af.size()>0){
					String[] dids=new String[af.size()];
					af.toArray(dids);
					ZkmCommons.zkmDocSecuritySet(uids, uns, dids);
				}
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "授权成功。"), response);
			}else if("f".equals(type)){
				ZkmCommons.zkmDocSecuritySet(userIds, userNames, ids);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "授权成功。"), response);
			}else{
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "未知授权类型，授权失败。"), response);
			}
		}catch(Exception x){
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "数据操作生发异常，授权失败。"), response);
		}
	}
	
	
	public static void getRolePageAuth(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String userRole=wfu.get("userRole");
		//TOD0: 从服务器端取userRole,有问题。
		if(userRole.length()<=0){
			userRole=user.getUserResMapValue("userRole")==null?"":user.getUserResMapValue("userRole");
		}
		if(userRole.length()>0){
			Map authMap=ZSecurityManager.getRoleAllPageAuth(userRole);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",authMap), response);
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "错误：无法获取登录用户的角色信息。"), response);
			return;
		}
	}
	
	public static void getHasSecurityUsers(UserInfo2 user, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (user == null || "_guest".equals(user.loginName2)) {
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "用户未登录或没有权限。"), response);
			return;
		}
		WebFormUtil wfu=new WebFormUtil(request);
		String type=wfu.get("type");
		String securityCode=wfu.get("code");
		if(type.length()>0 && securityCode.length()>0){
			try {
				List<Map> list = SSCDBTools.loadHasSecurityUserList(type,securityCode);
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "获取成功。",list), response);
			} catch (Exception x) {
				postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "读取失败，异常"), response);
			}
		}else{
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "缺少必要参数，获取数据失败"), response);
		}
		
	}
	public static void getSysDBConfs(HttpServletRequest request, HttpServletResponse response){
		List list=new ArrayList();
		try{
			list=ZkmCommons.getSysDBConfs();
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(true, "",list), response);
		}catch(Exception x){
			logger.error("获取数据库初始配置异常",x);
			postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(false, "获取数据异常失败。",list), response);
		}
	}
	public static void main(String [] args){
		/*String str="*abc*";"
		String [] strs=str.split(":");
		System.out.println(str.replaceAll("\\*", "%"));*/
		//String a="a_y22y_b";
		//String [] b=a.split("_y22y_");
		//System.out.println(b[0] + "  " +b[1]);
		/*List list1=new ArrayList();
		list1.add("c");
		list1.add("b");
		List list=new ArrayList();
		list.addAll(list1);
		list1.clear();
		System.out.println(list.size() +" " + list1.size());*/
		
		String a="0.03";
		float b=Float.parseFloat(a);
		b=b+Float.parseFloat("0.01");
		System.out.println(String.valueOf(b));
	}
	
}
