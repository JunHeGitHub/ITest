package com.zinglabs.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexWriter;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.zinglabs.db.ConfInfo;
import com.zinglabs.db.DAOTools;
import com.zinglabs.db.UserInfo2;
import com.zinglabs.luceneSearch.LuceneBase;
import com.zinglabs.luceneSearch.LuceneSearcher;
import com.zinglabs.luceneSearch.LunceIndexer;
import com.zinglabs.task.LuceneWriterTask;
import com.zinglabs.util.DateUtil;
import com.zinglabs.util.FileUtils;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.RandomGUID;
import com.zinglabs.util.WebFormUtil;
import com.zinglabs.util.ZKMAPPUtils;
import com.zinglabs.util.ZKMConfs;
import com.zinglabs.util.excelUtils.ExcelHelper;
import com.zinglabs.util.excelUtils.model.CreateExcelModel;
import com.zinglabs.util.solrj.SolrjUtil;

public class LuceneSearchHandle {

	public static  Logger logger = LoggerFactory.getLogger("ZKM"); 

	public static final String RETURN_DATA_TYPE_JSON = "JSON";
	public static final String RETURN_DATA_TYPE_XML = "XML";
	public static final String SEARCH_TYPE_PRECISE = "PRECISE";
	public static final String SEARCH_TYPE_ANALYZER = "ANALYZER";

	public static final String WRITER_TYPE_DEL = "DEL";
	public static final String WRITER_TYPE_DEL_FILE = "DEL_FILE";
	public static final String WRITER_TYPE_DEL_FOLDER = "DEL_FOLDER";
	public static final String WRITER_TYPE_INDEX = "INDEX";
	public static final String WRITER_TYPE_INDEX_INFILE = "FILE_INDEX";
	public static final String WRITER_TYPE_INDEX_BASE_ALL = "BASE_ALL";

	public static final String WRITER_INDEX_TYPE_DATABASE = "dataBase";
	public static final String WRITER_INDEX_TYPE_FILE = "file";
	public static final String SOLR_SERVER_KEY_SET = "SOLR_SERVER_KEY_SET";

	public static final String[] quickSearchField = { "title", "lables", "keywords", "summary", "bbdate", "bedate", "ebdate", "eedate", "delbDate", "deleDate", "area", "lastMbdate", "lastMedate"};

	public static final String[] exprotFields = { "id", "title", "bDate", "eDate", "keywords", "keywords1", "keywords2", "keywords3", "keywords4", "keywords5", "summary" };
	public static final String[] exprotFieldNames = { "主键", "标题", "开始日期", "结束日期", "关键字1", "关键字2", "关键字3", "关键字4", "关键字5", "关键字6", "备注" };

	public static final int DEL_INDEXDOC_MAX_TRY=5;
	
	public static void doResponse(String str, HttpServletResponse response) throws Exception {
		PrintWriter wi = response.getWriter();
		if (str != null && str.length() > 0) {
			wi.write(str);
		}
		wi.flush();
		wi.close();
	}

	public static void spellkeyWordsSql(String keyWord, StringBuffer wb, List p) {
		String sql = " and (`keywords` like ? or `keywords1` like ? or `keywords2` like ? or `keywords3` like ? or `keywords4` like ? or `keywords5` like ? ) ";
		if (keyWord.length() > 0) {
			wb.append(sql);
			p.add(keyWord);
			p.add(keyWord);
			p.add(keyWord);
			p.add(keyWord);
			p.add(keyWord);
			p.add(keyWord);
		}
	}

	public static void spellDateSql(String bdate, String edate, String field, StringBuffer wb, List p) {
		String sql = "";
		if (bdate.length() > 0 && edate.length() > 0) {
			sql = " and (" + field + " >= DATE_FORMAT(?,'%Y-%m-%d') and " + field + " <= DATE_FORMAT(?,'%Y-%m-%d')) ";
			p.add(bdate);
			p.add(edate);
		} else if (bdate.length() > 0 && edate.length() <= 0) {
			sql = " and " + field + " >= DATE_FORMAT(?,'%Y-%m-%d') ";
			p.add(bdate);
		} else if (bdate.length() <= 0 && edate.length() > 0) {
			sql = " and " + field + " <= DATE_FORMAT(?,'%Y-%m-%d') ";
			p.add(edate);
		}
		if (sql.length() > 0) {
			wb.append(sql);
		}
	}

	public static void spellTitleSql(String title, StringBuffer wb, List p) {
		if (title != null && title.length() > 0) {
			String[] ts = title.split(",");
			int index = 0;
			boolean has = false;
			for (String t : ts) {
				if (t.length() > 0) {
					if (index == 0) {
						has = true;
						wb.append(" and( ");
						wb.append(" hotTag like ? ");
					} else {
						wb.append(" or hotTag like ? ");
					}
					p.add("%" + t + "%");
					index++;
				}
			}
			if (has) {
				wb.append(" ) ");
			}
		
		}
	}

	public static void companySqlSpell( HttpServletRequest request,StringBuffer wb, List plist){
		WebFormUtil wfu=new WebFormUtil(request);
		String companyId=wfu.get("companyId");
		String companyName=wfu.get("companyName");
		String departmentId=wfu.get("departmentId");
		String departmentName=wfu.get("departmentName");
		String departmentValiDate=wfu.get("departmentValiDate");
		String companyValiDate=wfu.get("companyValiDate");
		if(companyValiDate.length()<=0){
			companyValiDate="Y";
		}else{
			companyValiDate=companyValiDate.toUpperCase();
		}
		if(departmentValiDate.length()<=0){
			departmentValiDate="N";
		}else{
			departmentValiDate=departmentValiDate.toUpperCase();
		}
		if("Y".equals(companyValiDate)){
			if(companyId.length()>0){
				wb.append(" and companyId=? ");
				plist.add(companyId);
			}
			if(companyName.length()>0){
				wb.append(" and companyName=? ");
				plist.add(companyName);
			}
		}
		if("Y".equals(departmentValiDate)){
			if(departmentId.length()>0){
				wb.append(" and departmentId=? ");
				plist.add(departmentId);
			}
			if(departmentName.length()>0){
				wb.append(" and departmentName=? ");
				plist.add(departmentName);
			}
		}
	}
	
	public static void quickSearch(HttpServletRequest request, HttpServletResponse response) {
		long btime = System.currentTimeMillis();
		String searchType = request.getParameter("searchType") == null ? "" : request.getParameter("searchType");
		String returnType = request.getParameter("returnType") == null ? "JSON" : request.getParameter("returnType");
		String exportIs = request.getParameter("exportIs") == null ? "" : request.getParameter("exportIs");
		//String sql = "select * from zkmInfoBaseDisponseFB where 1=1 and now() >= bDate and ( `eDate` >= now()  or `eDate` is null or eDate='0000-00-00 00:00:00' or eDate='0001-01-01 00:00:00.0' or eDate='0001-01-01 00:00:00') ";
		String searchFields="id,title,summary,lastModiTime,keywords,keywords1,keywords2,keywords3,keywords4,keywords5,bDate,eDate";
		
		String sql = "select "+ searchFields +" from zkmInfoBaseDisponseFB where 1=1 and now() >= bDate and ( `eDate` >= now()  or `eDate` is null or eDate='0000-00-00 00:00:00' or eDate='0001-01-01 00:00:00.0' or eDate='0001-01-01 00:00:00') ";
		StringBuffer wb = new StringBuffer();
		Map infoMap = new HashMap();
		List<String> plist = new ArrayList();
		List rlist = null;
		for (int i = 0; i < quickSearchField.length; i++) {
			String f = quickSearchField[i];
			String value = request.getParameter(f) == null ? "" : request.getParameter(f);

			if (value.length() > 0) {
				value = value.replaceAll("　", " ");
				value = value.replaceAll("＊", "*");
				value = value.replaceAll("\\*", "%");
				value = value.trim();
			}
			infoMap.put(f, value);
			if ("bbdate".equals(f)) {
				String ev = request.getParameter("bedate") == null ? "" : request.getParameter("bedate");
				spellDateSql(value, ev, "bDate", wb, plist);
			} else if ("ebdate".equals(f)) {
				String ev = request.getParameter("eedate") == null ? "" : request.getParameter("eedate");
				spellDateSql(value, ev, "eDate", wb, plist);
			} else if ("delbDate".equals(f)) {
				String ev = request.getParameter("deleDate") == null ? "" : request.getParameter("deleDate");
				spellDateSql(value, ev, "lastModiTime", wb, plist);
			} else if ("lastMbdate".equals(f)) {
				String ev = request.getParameter("lastMedate") == null ? "" : request.getParameter("lastMedate");
				spellDateSql(value, ev, "lastModiTime", wb, plist);
			} else if ("keywords".equals(f)) {
				String[] kws = value.split(" ");
				for (String keyWord : kws) {
					spellkeyWordsSql(keyWord, wb, plist);
				}
			} else if ("lables".equals(f)) {
				spellTitleSql(value, wb, plist);
			} else {
				if (!"bedate".equals(f) && !"eedate".equals(f) && !"deleDate".equals(f) && !"lastMedate".equals(f) && value.length() > 0) {
					wb.append(" and " + f + " like ? ");
					plist.add(value);
				}
			}
		}
		companySqlSpell(request,wb,plist);
		spellSecuritySql(wb, plist);
		
		sql =sql+ wb.toString();
		
		logger.info(" query search sql : " + sql);
		String restr = "";
		try {
			if (plist.size() > 0) {
				rlist = DAOTools.queryMap(sql, plist, ZKMDocServlet.zkmDBId);
			} else {
				rlist = DAOTools.queryMap(sql, ZKMDocServlet.zkmDBId);
			}
			// 过滤权限数据
			rlist = getSecurityFilterList(request, rlist);
			if (exportIs.length() > 0 && "TRUE".equals(exportIs.toUpperCase())) {
				exportZkmDocs("", rlist, request, response);
			} else {
				long etime = System.currentTimeMillis();
				infoMap.put("useTime", etime - btime);
				logger.debug("zkm getData num : " + rlist.size() + " useTime :" + (etime - btime));
				System.out.println("zkm getData num : " + rlist.size() + " useTime :" + (etime - btime));
				restr = getFormatReturnStr(returnType, rlist, infoMap,response);
				//doResponse(restr, response);
			}
		} catch (Exception x) {
			logger.error("quick search error .");
			logger.error(x.getMessage());
			rlist = new ArrayList();
		}
	}

	public static void groupSearch(HttpServletRequest request, HttpServletResponse response) {

	}

	public static void spellSecuritySql(StringBuffer where, List plist) {

	}

	public static List getSecurityFilterList(HttpServletRequest request, List list) throws Exception {
		UserInfo2 user = UserInfo2.getUser(request);
		list = ZKMDocServlet.getDocListFromFilterMap(user, list);
		user.releaseAll();
		return list;
	}

	public static void getDocContent_bak(String docId, HttpServletResponse res) throws Exception {
		HashMap remap = new HashMap();
		if (docId != null && !"".equals(docId)) {
			int id = -1;
			Map map = null;
			String cb = "";
			if (docId.length() != 36) {
				id = Integer.parseInt(docId);
				map = LuceneSearcher.getSearch(id);
				if (map != null) {
					String indexType = map.get("indexType") == null ? "" : (String) map.get("indexType");
					if (WRITER_INDEX_TYPE_FILE.equals(indexType)) {
						String filePath = map.get("filePath") == null ? "" : map.get("filePath").toString();
						String fileName = map.get("fileName") == null ? "" : map.get("fileName").toString();
						File file = new File(filePath + "/" + fileName);
						if (filePath.length() > 0 && fileName.length() > 0) {
							cb = getContentByFile(filePath + "/" + fileName);
						}
					} else if (WRITER_INDEX_TYPE_DATABASE.equals(indexType)) {
						String infoId = map.get("infoId") == null ? "" : (String) map.get("infoId");
						if (!"".equals(infoId)) {
							List list = getContentByDataBase(infoId);
							if (list != null && list.get(0) != null) {
								map = (Map) list.get(0);
								String path = map.get("filePath") == null ? "" : (String) map.get("filePath");
								path = ZKMDocServlet.getContentFileName(path);
								cb = ZKMDocServlet.getFileContentForFile(path);
								List flist = getFuJianForInfoId(infoId);
								if (flist != null) {
									remap.put("fujian", flist);
								}
							}
						}
					}
				} else {
					// 没有建立索引，则去查找流程库
					List list = getContentByDisponseInfo(docId);
					if (list != null && list.get(0) != null) {
						map = (Map) list.get(0);
						String path = map.get("contentPath") == null ? "" : (String) map.get("contentPath");
						String filePath = path;
						String ruler = ZKMConfs.confs.getProperty("ZKM_DOC_PATH_REPLACE_RULER", "^/usr/local/tomcat/webapps/ZDesk/ZKM/zkmDocs");
						Pattern rule = Pattern.compile(ruler);
						Matcher m = rule.matcher(path);
						if (m.find()) {
							filePath = m.replaceAll("");
						}
						ruler = "/index.html$";
						rule = Pattern.compile(ruler);
						m = rule.matcher(filePath);
						if (m.find()) {
							filePath = m.replaceAll("");
						}
						map.put("filePath", filePath);
						// path = ZKMDocServlet.getContentFileName(path);
						cb = FileUtils.getFileRealContent(path);
						List flist = getFuJianForInfoId(docId);
						if (flist != null) {
							remap.put("fujian", flist);
						}
					}
				}
			} else {
				List list = getContentByDataBase(docId);
				if (list != null && list.get(0) != null) {
					map = (Map) list.get(0);
					String path = map.get("filePath") == null ? "" : (String) map.get("filePath");
					path = ZKMDocServlet.getContentFileName(path);
					cb = ZKMDocServlet.getFileContentForFile(path);
					List flist = getFuJianForInfoId(docId);
					if (flist != null) {
						remap.put("fujian", flist);
					}
				} else {
					// 发布库中没有，则去查找流程库
					list = getContentByDisponseInfo(docId);
					if (list != null && list.get(0) != null) {
						map = (Map) list.get(0);
						String path = map.get("contentPath") == null ? "" : (String) map.get("contentPath");
						String filePath = ZKMAPPUtils.getFixZKMDisponseContentPath(path);
						map.put("filePath", filePath);
						// path = ZKMDocServlet.getContentFileName(path);
						cb = FileUtils.getFileRealContent(path);
						List flist = getFuJianForInfoId(docId);
						if (flist != null) {
							remap.put("fujian", flist);
						}
					}
				}
			}
			if (map != null) {
				remap.put("doc", map);
				if (cb != null) {
					// String cs=Utility.convertToHtml(cb.toString());
					remap.put("content", cb.toString());
				}
			}
		}
//		PrintWriter wi = res.getWriter();
//		if (remap != null && remap.size() > 0) {
//			String json = JSONSerializer.toJSON(remap).toString();
//			wi.write(json);
//		} else {
//			wi.write("");
//		}
//		wi.flush();
//		wi.close();
		if (remap != null && remap.size() > 0) {
			JsonUtils.genUpdateDataReturnJsonStr(remap, res);
		}else{
			JsonUtils.genUpdateDataReturnJsonStr("", res);
		}
	}

	public static void getDocContent(String docId, HttpServletResponse res) throws Exception {
		HashMap remap = new HashMap();
		if (docId != null && !"".equals(docId)) {
			Map map = null;
			String cb = "";
				//查询文档库
			List list = getContentByDataBase(docId);
			if (list != null && list.get(0) != null) {
				map = (Map) list.get(0);
				String path = map.get("contentPath") == null ? "" : (String) map.get("contentPath");
				String filePath = ZKMAPPUtils.getFixZKMDisponseContentPath(path);
				map.put("filePath", filePath);
				cb = FileUtils.getFileRealContent(path);
			}else{
				//查询流程库
				list = getContentByDisponseInfo(docId);
				if(list!=null && list.size()>0){
					map = (Map) list.get(0);
					String path = map.get("contentPath") == null ? "" : (String) map.get("contentPath");
					String filePath = ZKMAPPUtils.getFixZKMDisponseContentPath(path);
					map.put("filePath", filePath);
					cb = FileUtils.getFileRealContent(path);
				}
			}
			List flist = getFuJianForInfoId(docId);
			if (flist != null) {
				remap.put("fujian", flist);
			}
			if (map != null) {
				remap.put("doc", map);
				if (cb != null) {
					remap.put("content", cb);
				}
			}
		}
//		PrintWriter wi = res.getWriter();
//		if (remap != null && remap.size() > 0) {
//			String json = JSONSerializer.toJSON(remap).toString();
//			wi.write(json);
//		} else {
//			wi.write("");
//		}
//		wi.flush();
//		wi.close();
		if (remap != null && remap.size() > 0) {
			JsonUtils.genUpdateDataReturnJsonStr(remap, res);
		}else{
			JsonUtils.genUpdateDataReturnJsonStr("", res);
		}
	}

	public static List getContentByDataBase(String id) throws Exception {
		if (id != null && !"".equals(id)) {
			String sql = "select * from zkmInfoBaseDisponseFB where id=?";
			String[] param = { id };
			List list = DAOTools.queryMap(sql, param, ConfInfo.confMapDBConf.get("securityDBId"));
			if (list != null && list.size() > 0) {
				return list;
			}
		}
		return null;
	}

	public static List getContentByDisponseInfo(String id) throws Exception {
		if (id != null && !"".equals(id)) {
			String sql = "select * from zkmInfoBaseDisponse where id=?";
			String[] param = { id };
			List list = DAOTools.queryMap(sql, param, ConfInfo.confMapDBConf.get("securityDBId"));
			if (list != null && list.size() > 0) {
				return list;
			}
		}
		return null;
	}

	public static List getFuJianForInfoId(String infoId) throws Exception {
		List relist = null;
		if (infoId != null && !"".equals(infoId)) {
			String sql = "select * from zkmInfoFile where relationId=? and fileUse=? and (isdel='' or isdel ='0') ";
			String[] param = { infoId, "zkm_fujian" };
			relist = DAOTools.queryMap(sql, param, ConfInfo.confMapDBConf.get("securityDBId"));
		}
		return relist;
	}

	public static String getContentByFile(String filePath) throws Exception {
		String str = null;
		if (filePath != null && !"".equals(filePath)) {
			File file = new File(filePath);
			if (file.exists() && file.isFile()) {
				str = FileUtils.getThreadSingFileContent(file);
			}
		}
		return str;
	}

	public static String getFormatReturnStr(String type, List data, Map map,HttpServletResponse res) throws Exception {
		String restr = "";
		if (type != null && !"".equals(type)) {
			type = type.toUpperCase();
			if (RETURN_DATA_TYPE_JSON.equals(type)) {
				getFormat2JsonStr(data, map,res);
			} else if (RETURN_DATA_TYPE_XML.equals(type)) {
				getFormat2XmlStr(data, map,res);
			}
		}
		return restr;
	}

	public static void getFormat2JsonStr(List data, Map map,HttpServletResponse res) throws Exception {
		if (data != null && data.size() > 0) {
			Map jsonMap=new HashMap();
			jsonMap.put("data", data);
			jsonMap.put("info", map);
			JsonUtils.genUpdateDataReturnJsonStr(jsonMap, res);
		}
	}

	public static void getFormat2XmlStr(List data, Map map,HttpServletResponse res) {

	}

	/*
	 * public static List getSearch(String conditions,String searchType,int
	 * b,int n) throws Exception{ if(searchType==null || "".equals(searchType)){
	 * searchType=SEARCH_TYPE_PRECISE; } searchType=searchType.toUpperCase();
	 * if(conditions!=null && !"".equals(conditions))
	 * if(SEARCH_TYPE_ANALYZER.equals(searchType)){ return
	 * LuceneSearcher.getSearch(conditions,b,n,true); }else if
	 * (SEARCH_TYPE_PRECISE.equals(searchType)){ String[] cds=conditions.split("
	 * "); return LuceneSearcher.getSearch(cds,b,n,false); } return null; }
	 */

	public static List getSearch(String conditions, String searchType, int b, int n, String stf, String ads) throws Exception {
		if (searchType == null || "".equals(searchType)) {
			searchType = SEARCH_TYPE_PRECISE;
		}
		searchType = searchType.toUpperCase();
		if (conditions != null && !"".equals(conditions)) {
			/*
			 * if(SEARCH_TYPE_ANALYZER.equals(searchType)){ return
			 * LuceneSearcher.getSearch(conditions,b,n,true); }else if
			 * (SEARCH_TYPE_PRECISE.equals(searchType)){ if(stf==null ||
			 * stf.length()<=0){ stf="uploadDate"; } if(ads==null ||
			 * ads.length()<=0){ ads="desc"; } return
			 * SolrjUtil.queryEasy(conditions, b, n, stf,
			 * SolrjUtil.getOrderType(ads)); }
			 */
			if (stf == null || stf.length() <= 0) {
				stf = "uploadDate";
			}
			if (ads == null || ads.length() <= 0) {
				ads = "desc";
			}
			return SolrjUtil.queryEasy(conditions, b, n, stf, SolrjUtil.getOrderType(ads));
		}
		return null;
	}

	public static List getSearch(String conditions, String[] sorts, String searchType, int b, int n, String stf, String ads) throws Exception {
		if (searchType == null || "".equals(searchType)) {
			searchType = SEARCH_TYPE_PRECISE;
		}
		searchType = searchType.toUpperCase();
		if (conditions != null && !"".equals(conditions)) {
			/*
			 * if(SEARCH_TYPE_ANALYZER.equals(searchType)){ return
			 * LuceneSearcher.getSearch(conditions,b,n,true); }else if
			 * (SEARCH_TYPE_PRECISE.equals(searchType)){ if(stf==null ||
			 * stf.length()<=0){ stf="uploadDate"; } if(ads==null ||
			 * ads.length()<=0){ ads="desc"; } return
			 * SolrjUtil.queryEasy(conditions, b, n, stf,
			 * SolrjUtil.getOrderType(ads)); }
			 */
			if (stf == null || stf.length() <= 0) {
				stf = "uploadDate";
			}
			if (ads == null || ads.length() <= 0) {
				ads = "desc";
			}
			return SolrjUtil.queryForSort(conditions, sorts, b, n, stf, SolrjUtil.getOrderType(ads));
		}
		return null;
	}

	public static void getSearch(String condition, String searchType, String returnType, int b, int n, HttpServletRequest request, HttpServletResponse res) throws Exception {
		long btime = System.currentTimeMillis();
		String exportIs = request.getParameter("exportIs") == null ? "" : request.getParameter("exportIs");
		String companyId=request.getParameter("companyId")==null?"": request.getParameter("companyId");
		
		String nowDate=DateUtil.getLocalDefineDate("yyyy-MM-dd HH:mm:ss");
		nowDate=DateUtil.getFormatDateSolrUTCStr(nowDate);
		String beDateCondition="bDate:[* TO "+ nowDate +"] AND (eDate:["+ nowDate +" TO *] OR eDate:\"0001-01-01T00:00:00Z\")";
		
		if(companyId.length()>0){
			condition=condition+" AND companyId:" + companyId + " AND " +beDateCondition;
		}else{
			//公司标识为空时，清空查询条件，会跳过查询。
			condition=beDateCondition;
		}
		List list = LuceneSearchHandle.getSearch(condition, searchType, b, n, "", "");
		// 过滤权限数据
		list = getSecurityFilterList(request, list);
		if (exportIs.length() > 0 && "TRUE".equals(exportIs.toUpperCase())) {
			exportZkmDocs("", list, request, res);
		} else {
			long etime = System.currentTimeMillis();
			HashMap map = new HashMap();
			map.put("useTime", etime - btime);
			if (list != null && list.size() > 0) {
				getFormatReturnStr(returnType, list, map,res);
			}
		}
	}

	public static void getSearch(String condition, String[] sorts, String searchType, String returnType, int b, int n, HttpServletResponse res) throws Exception {
		long btime = System.currentTimeMillis();
		List list = LuceneSearchHandle.getSearch(condition, sorts, searchType, b, n, "", "");
		long etime = System.currentTimeMillis();
		HashMap map = new HashMap();
		map.put("useTime", etime - btime);
		if (list != null && list.size() > 0) {
			getFormatReturnStr(returnType, list, map,res);
		}
	}

	/*
	 * private static synchronized void delIndexDocumentForDataBase(String
	 * infoId) throws Exception{ IndexWriter writer=LuceneBase.getIndexWriter();
	 * Query queryTemp=new TermQuery(new Term("infoId",infoId));
	 * writer.deleteDocuments(queryTemp); writer.optimize(); writer.commit();
	 * writer.close(); logger.info("---------------del : " + infoId); }
	 */

	private static synchronized void delIndexDocumentForDataBase(String infoId,int tryNum){
		try{
			SolrjUtil.delIndexForAllWserver(infoId, SolrjUtil.SOLR_DEL_TYPE_BYID);
		}catch(Exception x){
			try {
				Thread.sleep(100);
				if(tryNum<DEL_INDEXDOC_MAX_TRY){
					delIndexDocumentForDataBase(infoId,tryNum++);
				}else{
					logger.info("----solrserver-----------del error: " + infoId);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//logger.info("---------------del : " + infoId);
	}

	public static synchronized void indexFileForDataBase(IndexWriter writer, HashMap map) throws Exception {
		// logger.debug("Indexing " + f.getCanonicalPath());
		String infoId = map.get("infoId") == null ? "" : map.get("infoId").toString();
		if (infoId.length() > 0) {
			delIndexDocumentForDataBase(infoId,0);
		}
		writer = LuceneBase.getIndexWriter();
		Document doc = new Document();
		doc.add(new Field("filePath", map.get("filePath") == null ? "" : map.get("filePath").toString(), Field.Store.YES, Field.Index.NO, TermVector.NO));
		doc.add(new Field("fileName", map.get("fileName") == null ? "" : map.get("fileName").toString(), Field.Store.YES, Field.Index.ANALYZED, TermVector.YES));

		Object content = map.get("fileContent");
		Reader rd = null;
		if (content != null) {
			if (content instanceof Reader) {
				rd = (Reader) content;
				doc.add(new Field("fileContent", rd, TermVector.YES));
			}
			if (content instanceof String) {
				String str = (String) content;
				doc.add(new Field("fileContent", str, Field.Store.NO, Field.Index.ANALYZED, TermVector.YES));
			}
		} else {
			doc.add(new Field("fileContent", "", Field.Store.NO, Field.Index.ANALYZED, TermVector.YES));
		}

		doc.add(new Field("title", map.get("title") == null ? "" : map.get("title").toString(), Field.Store.YES, Field.Index.ANALYZED, TermVector.YES));
		doc.add(new Field("keywords", (String) map.get("keywords") == null ? "" : map.get("keywords").toString(), Field.Store.YES, Field.Index.ANALYZED, TermVector.YES));
		doc.add(new Field("keywords1", (String) map.get("keywords1") == null ? "" : map.get("keywords1").toString(), Field.Store.YES, Field.Index.ANALYZED, TermVector.YES));
		doc.add(new Field("keywords2", (String) map.get("keywords2") == null ? "" : map.get("keywords2").toString(), Field.Store.YES, Field.Index.ANALYZED, TermVector.YES));
		doc.add(new Field("keywords3", (String) map.get("keywords3") == null ? "" : map.get("keywords3").toString(), Field.Store.YES, Field.Index.ANALYZED, TermVector.YES));
		doc.add(new Field("keywords4", (String) map.get("keywords4") == null ? "" : map.get("keywords4").toString(), Field.Store.YES, Field.Index.ANALYZED, TermVector.YES));
		doc.add(new Field("keywords5", (String) map.get("keywords5") == null ? "" : map.get("keywords5").toString(), Field.Store.YES, Field.Index.ANALYZED, TermVector.YES));
		doc.add(new Field("summary", (String) map.get("summary") == null ? "" : map.get("summary").toString(), Field.Store.YES, Field.Index.ANALYZED, TermVector.YES));
		doc.add(new Field("infoId", map.get("infoId") == null ? "" : map.get("infoId").toString(), Field.Store.YES, Field.Index.NOT_ANALYZED, TermVector.NO));
		doc.add(new Field("fileId", map.get("fileId") == null ? "" : map.get("fileId").toString(), Field.Store.YES, Field.Index.NOT_ANALYZED, TermVector.NO));
		doc.add(new Field("indexType", map.get("indexType") == null ? "" : map.get("indexType").toString(), Field.Store.YES, Field.Index.NO, TermVector.NO));

		doc.add(new Field("text", map.get("text") == null ? "" : map.get("text").toString(), Field.Store.YES, Field.Index.NO));
		doc.add(new Field("bDate", map.get("bDate") == null ? "" : map.get("bDate").toString(), Field.Store.YES, Field.Index.NO));
		doc.add(new Field("eDate", map.get("eDate") == null ? "" : map.get("eDate").toString(), Field.Store.YES, Field.Index.NO));
		doc.add(new Field("uploadDate", map.get("uploadDate") == null ? "" : map.get("uploadDate").toString(), Field.Store.YES, Field.Index.NO));
		doc.add(new Field(LuceneBase.WRITER_INDEX_MAP_KEY, LuceneBase.workManager.getIndexMapKey(), Field.Store.YES, Field.Index.NO));
		doc.add(new Field(LuceneBase.WRITER_INDEX_FOLDER_PATH, LuceneBase.workManager.getIndexFolder(), Field.Store.YES, Field.Index.NO));

		writer.addDocument(doc);
		logger.info("---------------add : " + map.get("infoId") == null ? "" : map.get("infoId").toString());
		writer.optimize();
		logger.info("---------------optimize : " + map.get("infoId") == null ? "" : map.get("infoId").toString());
		writer.commit();
		logger.info("---------------commit : " + map.get("infoId") == null ? "" : map.get("infoId").toString());
		writer.close();
		logger.info("---------------close : " + map.get("infoId") == null ? "" : map.get("infoId").toString());
		/*
		 * if(rd!=null){ rd.close(); rd=null; }
		 */
		/*
		 * File tmFile=map.get("ZKM_DOC_TEMP_FILE")==null?null:(File)map.get(
		 * "ZKM_DOC_TEMP_FILE"); if(tmFile!=null){ tmFile.delete(); }
		 */
	}

	public static synchronized HashMap indexFileForDataBase(HashMap map, String serverKey) {
		String infoId = map.get("infoId") == null ? "" : map.get("infoId").toString();
		if (infoId.length() > 0) {
			map.put("id", infoId);
		}
		StringBuffer sb = new StringBuffer();
		File file = null;
		InputStream in = null;
		BufferedReader reader = null;
		try {
			file = (File) map.get("ZKM_DOC_TEMP_FILE");
			String charset = FileUtils.getFileCharset(file);
			in = new FileInputStream(file);
			logger.info("index file : " + file.getPath());
			logger.info("index file charset is : " + charset);
			reader = new BufferedReader(new InputStreamReader(in, charset));
			String line = reader.readLine();
			while (line != null) {
				sb.append(line);
				line = reader.readLine();
			}
			map.put("fileContent", sb.toString());
		} catch (Exception x) {
			logger.info("index file get file error : " + file.getPath());
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (in != null)
					in.close();
				if (file != null)
					file.deleteOnExit();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			SolrjUtil.updateIndexDoc(map, serverKey);
			map.put(LunceIndexer.COMMIT_TF, true);
		} catch (Exception x) {
			map.put(LunceIndexer.COMMIT_TF, false);
			int cnum = 0;
			if (map.get(LunceIndexer.COMMIT_NUM) != null) {
				cnum = ((Integer) map.get(LunceIndexer.COMMIT_NUM)).intValue();
				cnum++;
			}
			map.put(LunceIndexer.COMMIT_NUM, cnum);
			logger.debug(x.getMessage());
			logger.debug("solr index error : " + map.get("id") + " : " + cnum);
			x.printStackTrace();
		}
		return map;
	}

	public static synchronized void doWriterManager(String wdo, HashMap map) throws Exception {
		String zkmRootPath = LuceneBase.confs.getProperty("zkmDocSaveDir", "/mnt/zkmDoc");
		if (WRITER_TYPE_DEL.equals(wdo)) {
			String id = (String) map.get("infoId");
			if (id != null && !"".equals(id)) {
				delIndexDocumentForDataBase(id,0);
			}
		} else if (WRITER_TYPE_INDEX.equals(wdo)) {
			if (map != null) {
				map.put("indexType", WRITER_INDEX_TYPE_DATABASE);
				indexFileForDataBase(LuceneBase.getIndexWriter(), map);
			}
		} else if (WRITER_TYPE_INDEX_INFILE.equals(wdo)) {
			if (map != null) {
				File f = (File) map.get("file");
				Map pm = FileUtils.parserFile(f);
				if (pm != null && pm.get(FileUtils.FILE_METADATA) != null) {
					// 获取文件元信息
					Map fm = (HashMap) pm.get(FileUtils.FILE_METADATA);
					if (isCanIndexFile(fm.get(FileUtils.FILE_MIMETYPE_CONTENT_TYPE).toString())) {
						// 获取文件内容
						String s = pm.get(FileUtils.FILE_CONTENT) == null ? "" : pm.get(FileUtils.FILE_CONTENT).toString();
						// 替换掉文档中的空格、换行符等
						s.replaceAll("\\s", "");
						String keywords = map.get("keywords") == null ? (String) map.get("relName") : (String) map.get("keywords");
						String summary = map.get("summary") == null ? (String) map.get("relName") : (String) map.get("summary");
						String filePath = FileUtils.getThreadSingFileContent(f);
						map.put("keywords", keywords);
						map.put("summary", summary);
						map.put("fileContent", s);
						map.put("indexType", WRITER_INDEX_TYPE_FILE);
						indexFileForDataBase(LuceneBase.getIndexWriter(), map);
					}
				}
			}
		} else if (WRITER_TYPE_INDEX_BASE_ALL.equals(wdo)) {
			Thread.sleep(200);
			String infoId = (String) map.get("infoId");
			if (infoId != null && !"".equals(infoId)) {
				Map infoMap = new HashMap();
				// 获取ZKMDOC信息
				String sql = "select * from zkmInfoBaseDisponseFB where id=?";
				String[] params = { infoId };
				List list = DAOTools.queryMap(sql, params, ConfInfo.confMapDBConf.get("securityDBId"));
				if (list != null && list.size() > 0) {
					infoMap = (Map) list.get(0);
				}

				map.put("title", infoMap.get("title") == null ? "" : (String) infoMap.get("title"));
				map.put("keywords", infoMap.get("keywords") == null ? "" : (String) infoMap.get("keywords"));
				map.put("keywords1", infoMap.get("keywords1") == null ? "" : (String) infoMap.get("keywords1"));
				map.put("keywords2", infoMap.get("keywords2") == null ? "" : (String) infoMap.get("keywords2"));
				map.put("keywords3", infoMap.get("keywords3") == null ? "" : (String) infoMap.get("keywords3"));
				map.put("keywords4", infoMap.get("keywords4") == null ? "" : (String) infoMap.get("keywords4"));
				map.put("keywords5", infoMap.get("keywords5") == null ? "" : (String) infoMap.get("keywords5"));
				map.put("summary", infoMap.get("summary") == null ? "" : (String) infoMap.get("summary"));
				String bd=infoMap.get("bDate") == null ? "" : infoMap.get("bDate").toString();
				String ed=infoMap.get("eDate") == null ? "" : infoMap.get("eDate").toString();
				bd=DateUtil.getFormatDateSolrUTCStr(bd);
				ed=DateUtil.getFormatDateSolrUTCStr(ed);
				map.put("bDate", bd);
				map.put("eDate", ed);
				map.put("lastModiTime", infoMap.get("lastModiTime") == null ? "" : infoMap.get("lastModiTime").toString());
				map.put("uploadDate", infoMap.get("createTime") == null ? "" : infoMap.get("createTime").toString());
				map.put("indexType", WRITER_INDEX_TYPE_DATABASE);
				// 获取附件信息
				List fjlist = LuceneSearchHandle.getFuJianForInfoId(infoId);
				// 获取临时文件目录
				String tempDir = LuceneBase.confs.getProperty("mergeTempDir", "/mnt/mergeTempDir");
				RandomGUID rg = new RandomGUID();
				String fileName = rg.toString();
				// 临时文件
				File file = FileUtils.createFolderUseSystemDate(tempDir);
				file = new File(file.getPath() + "/" + fileName);
				if (file.exists() && file.isFile()) {
					file.delete();
				}
				// 合并内容
				// 写入正文
				String infoContent = "";
				String cp = infoMap.get("contentPath") == null ? "" : infoMap.get("contentPath").toString();
				if (cp.length() > 0) {
					//cp = ZKMDocServlet.getContentFileName(cp);
					infoContent = FileUtils.getFileContent(cp);
					// logger.info("---infoContent: " + infoContent);
					infoContent = infoContent.replaceAll("\\s", "");
				}

				FileUtils.appendToFile(infoContent, file, false);
				infoContent = null;
				// 写附件内容
				for (int i = 0; i < fjlist.size(); i++) {
					Map fm = (Map) fjlist.get(i);
					String path = fm.get("filePath") == null ? "" : fm.get("filePath").toString();
					String name = fm.get("fileName") == null ? "" : fm.get("fileName").toString();
					String relName = fm.get("relName") == null ? "" : fm.get("relName").toString();
					if (path.length() > 0 && name.length() > 0) {
						File ft = new File(path + "/" + name);
						// 判断文件是否存在
						if (ft.exists() && ft.isFile() && ft.canRead()) {
							// 获取文件头信息及无格式txt内容
							try {
								Map pm = FileUtils.parserFile(ft);
								// 验证是否为可建索引的信息
								if (pm != null && pm.get(FileUtils.FILE_METADATA) != null) {
									Map fmet = (HashMap) pm.get(FileUtils.FILE_METADATA);
									if (isCanIndexFile(fmet.get(FileUtils.FILE_MIMETYPE_CONTENT_TYPE).toString())) {
										// 获取文件内容
										String s = pm.get(FileUtils.FILE_CONTENT) == null ? "" : pm.get(FileUtils.FILE_CONTENT).toString();
										// 替换掉文档中的空格、换行符等
										s.replaceAll("\\s", "");
										// 追加到file
										FileUtils.appendToFile(s, file, true);
										// 将文件名写入
										FileUtils.appendToFile(relName, file, true);
									}
								}
							} catch (Exception x) {
								logger.error("index fj error:" + ft.getPath());
								logger.error("index fj error:" + x.getMessage());
							}
						}
					}
				}
				ZKMAPPUtils.fcdirChangeUserAndGroup(file);
				String keywords = map.get("keywords") == null ? (String) map.get("relName") : (String) map.get("keywords");
				String summary = map.get("summary") == null ? (String) map.get("relName") : (String) map.get("summary");
				map.put("ZKM_DOC_TEMP_FILE", file);
				/*
				 * String charset=FileUtils.getFileCharset(file); InputStream
				 * in=new FileInputStream(file); logger.info("index file charset
				 * is : " + charset); BufferedReader reader=new
				 * BufferedReader(new InputStreamReader(in,charset));
				 * map.put("fileContent",reader);
				 */
				LuceneBase.lunceIndexer.doList(map, LunceIndexer.WADD);
				// indexFileForDataBase(LuceneBase.getIndexWriter(),map);
				// reader.close();
				// file.delete();
			}
		}
	}

	public static boolean isCanIndexFile(String type) {
		if (type != null && type.length() > 0) {
			type = type.trim();
			if (type.indexOf(FileUtils.FILE_MIMETYPE_HTML) >= 0 || FileUtils.FILE_MIMETYPE_OFFICE_DOC.equals(type) || FileUtils.FILE_MIMETYPE_OFFICE_DOCX.equals(type) || FileUtils.FILE_MIMETYPE_OFFICE_XLS.equals(type) || FileUtils.FILE_MIMETYPE_OFFICE_XLSX.equals(type)
					|| FileUtils.FILE_MIMETYPE_TEXT.equals(type)) {
				return true;
			}
		}
		return false;
	}
    //TODO 去掉 synchronized   2014年6月5日改  
	public static  void doTaskWriter(String wdo, HashMap map) throws Exception {
		LuceneWriterTask lwt = new LuceneWriterTask();
		lwt.writeMap = map;
		lwt.doWhat = wdo;
		Thread trd = new Thread(lwt);
		trd.start();
	}

	public static void exportZkmDocs(String fileName, List datalist, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (datalist != null && datalist.size() > 0) {
			String fname = "";
			if (fileName != null && fileName.length() > 0) {
				fname = fileName;
			} else {
				fname = "searchExprot.xls";
			}
			response.setHeader("Content-disposition", "attachment;filename=" + new String(fname.getBytes("gb2312"), "iso8859-1"));
			List edl = new ArrayList();
			for (int i = 0; i < datalist.size(); i++) {
				Object obj = datalist.get(i);
				Map dm = null;
				if (obj instanceof SolrDocument) {
					dm = ((SolrDocument) obj).getFieldValueMap();
				} else {
					dm = (HashMap) obj;
				}
				if (dm != null) {
					String[] data = new String[exprotFields.length];
					for (int k = 0; k < exprotFields.length; k++) {
						String field = exprotFields[k];
						String vl = dm.get(field) == null ? "" : (String) dm.get(field);
						data[k] = vl;
					}
					edl.add(data);
				}
			}
			if (edl.size() > 0) {
				CreateExcelModel cem = new CreateExcelModel();
				cem.setOs(response.getOutputStream());
				cem.setFristRow(exprotFieldNames);
				cem.setRowDataList(edl);
				cem.setSheetName("exportData");
				ExcelHelper.makeExcel(cem);
			}
		}
	}

	public static void main(String[] args) {
		String a = "*＊ad*　asdf　";
		a = a.replaceAll("　", " ");
		a = a.replaceAll("＊", "*");
		a = a.replaceAll("\\*", "%");
		System.out.println(a);
	}
}