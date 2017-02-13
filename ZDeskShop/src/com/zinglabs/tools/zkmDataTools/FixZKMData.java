package com.zinglabs.tools.zkmDataTools;
import java.util.regex.*;
import java.util.*;
import java.sql.*;
import java.io.*;

import com.zinglabs.db.SSCDBTools;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.HtmlPage;

import com.zinglabs.db.DAOTools;
import com.zinglabs.servlet.ZKMDocServlet;
import com.zinglabs.servlet.zkmDocTemplate.templateImp.ZkmDocTemplateDefault;
import com.zinglabs.util.FileUtils;
import com.zinglabs.util.html.HtmlHelper;
public class FixZKMData extends ToolsBase{
	
	public String[] distCon={"com.mysql.jdbc.Driver","jdbc:mysql://22.8.81.196:3306/securityDB?user=zinglabs&password=12&useUnicode=true&characterEncoding=utf8","zinglabs","12"};
	
	public List loglist=new ArrayList();
	
	public void fixDataTitle() throws Exception{
		Connection con=getConnection(distCon);
		String sql="select * from zkmInfoBase where recordType='f'";
		List list=queryMap(sql, null, con);
		Pattern rule=Pattern.compile("\\d+$");
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				String title=map.get("title")==null?"":(String)map.get("title");
				String id=map.get("id")==null?"":(String)map.get("id");
				Matcher m=rule.matcher(title);
				if(m.find()){
					String tt=title;
					title=m.replaceAll("");
					try{
						updateFixDataTitle(id,title,"zkmInfoBase");
						loglist.add(putLogMap(tt,title));
					}catch(Exception x){
						x.printStackTrace();
					}
				}
			}
		}
		putLogListToHtml("zkmInfoBase");
		
		sql="select * from zkmInfoBaseDisponse";
		con=getConnection(distCon);
		list=queryMap(sql, null, con);
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				String title=map.get("title")==null?"":(String)map.get("title");
				String id=map.get("id")==null?"":(String)map.get("id");
				Matcher m=rule.matcher(title);
				if(m.find()){
					String tt=title;
					title=m.replaceAll("");
					try{
						updateFixDataTitle(id,title,"zkmInfoBaseDisponse");
						loglist.add(putLogMap(tt,title));
					}catch(Exception x){
						x.printStackTrace();
					}
				}
			}
		}
		putLogListToHtml("zkmInfoBaseDisponse");
	}
	
	public void updateFixDataTitle(String id,String title,String table) throws Exception{
		Connection con=getConnection(distCon);
		String sql="update " + table + " set title = ? where id=?";
		updateForPrepared(sql, new String[]{title,id}, con);
		closeConnection(con);
	}
	
	public Map putLogMap(String log1,String log2){
		Map map=new HashMap();
		map.put("old", log1);
		map.put("new", log2);
		return map;
	}
	
	public void putLogListToHtml(String title){
		File file=new File("e:/"+title+"_fixData.html");
		StringBuffer sb=new StringBuffer();
		sb.append(title);
		sb.append("<br>");
		sb.append("<table>");
		sb.append("<tr><td>序</td><td>原</td><td>新</td></tr>");
		if(loglist!=null && loglist.size()>0){
			for(int i=0;i<loglist.size();i++){
				Map map=(Map)loglist.get(i);
				String l1=map.get("old")==null?"":(String)map.get("old");
				String l2=map.get("new")==null?"":(String)map.get("new");
				sb.append("<tr>");
				sb.append("<td>" + (i+1) + "</td>");
				sb.append("<td>" + l1 + "</td>");
				sb.append("<td>" + l2 + "</td>");
				sb.append("</tr>");
			}
		}
		sb.append("</table>");
		ZkmDocTemplateDefault zdt=new ZkmDocTemplateDefault();
		String html=zdt.getZkmContent(sb.toString());
		try{
			FileUtils.appendToFile(html, file, false);
		}catch(Exception x){
			x.printStackTrace();
		}
		
	}
	
	public void reIndexDocs() throws Exception{
		Connection con=getConnection(distCon);
		String sql="select * from zkmInfoBase where recordType='f'";
		List list=queryMap(sql, null, con);
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map map=(Map)list.get(i);
				String id=map.get("id")==null?"":(String)map.get("id");
				if(id.length()>0){
					ZKMDocServlet.indexDocs(id,false);
				}
			}
		}
	}
	
	public void fixHtml(){
		File fileSource=new File("E:/testImp/zkmDocs");
		File[] fs=fileSource.listFiles();
		for(File f:fs){
			fixHtmlFile(f);
		}
	}
	
	public void fixHtmlFile(File fd){
		if(fd.exists() && fd.isDirectory()){
			File [] fs =fd.listFiles();
			if(fs.length>0){
				for(File f:fs){
					fixHtmlFile(f);
				}
			}
		}else if(fd.exists() && fd.isFile()){
			String name=fd.getName();
			if(name.indexOf("html")>=0){
				try{
					String html=FileUtils.getFileRealContent(fd);
					html=fixHtmlContent(html);
				}catch(Exception x){
					System.out.println("----error:" + fd.getPath());
					System.out.println("----error:" + x.getMessage());
				}
				
			}
		}
	}
	
	public String  fixHtmlContent(String html) throws Exception {
		if(html.length()>0){
			Parser parser=HtmlHelper.createLexerParser(html);
			HtmlPage page=HtmlHelper.createHtmlPage(html, parser);
			NodeList list=page.getBody();
			if(list!=null && list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					Node tag = list.elementAt(i);
					processTags(tag);
				}
				html=list.toHtml();
			}
		}
		return html;
	}
	
	public void processTags(Node tag){
		try{
			disposeTags(tag);
		}catch(Exception x){
			x.printStackTrace();
		}
		NodeList list=tag.getChildren();
		
		if(list!=null && list.size()>0){
			for(int i = 0; i < list.size(); i++){
				Node ct = list.elementAt(i);
				processTags(ct);
			}
		}
	}
	
	public String fixUrlZkmDocsRuler="^/ZDesk/zkmDocs+";
	
	public String uploadsURLFix(String url){
		String ruler="^/ZDesk/uploads+";
		String fix="/ZDesk/ZKM/uploads";
		return fixForRuler(ruler,fix,url);
	}
	
	public String zkmDocsURLFix(String url){
		String ruler="^/ZDesk/zkmDocs+";
		String fix="/ZDesk/ZKM/zkmDocs";
		return fixForRuler(ruler,fix,url);
	}
	
	public String fixForRuler(String ruler,String fix,String str){
		Pattern rule=Pattern.compile(ruler);
		Matcher m=rule.matcher(str);
		if(m.find()){
			str=m.replaceAll(fix);
		}
		return str;
	}
	
	public  void disposeTags(Node tag) throws Exception{
		if (tag instanceof LinkTag){
			LinkTag link = (LinkTag) tag;
			String linkUrl = link.getLink();
			if(linkUrl!=null && linkUrl.length()>0){
				if(linkUrl.indexOf("uploads")>0){
					link.setLink(uploadsURLFix(linkUrl));
				}
				if(linkUrl.indexOf("zkmDocs")>0){
					link.setLink(zkmDocsURLFix(linkUrl));
				}
				
			}
		}else if (tag instanceof ImageTag){
			ImageTag image = (ImageTag) tag;
			String src=image.getImageURL();
			if(src!=null && src.length()>0){
				if(src.indexOf("uploads")>0){
					image.setImageURL(uploadsURLFix(src));
				}
				if(src.indexOf("zkmDocs")>0){
					image.setImageURL(zkmDocsURLFix(src));
				}
			}
		}/*else if(tag instanceof EmbedTag){
			EmbedTag et=(EmbedTag) tag;
			String src=et.getSrc();
			if(src!=null && src.length()>0){
				//flv文件处理
				String rule="(.+flvplayer.swf)\\?vcastr_file=(.+\\.flv)$";
				Pattern pattern=Pattern.compile(rule);
				Matcher mat=pattern.matcher(src);
				if(mat.find(0)){
					String playerFile="/ZDesk/js/flvPlayer/flvplayer.swf";
					String file1=mat.group(1);
					String file2=mat.group(2);
					file1=file1.substring(file1.lastIndexOf("/") +1);
					String flvPath= file1 + "?vcastr_file=" + file2;
					et.setSrc(flvPath);
				}
			}
		}*/
	}
	
	public void fixUploadsFilePath() throws Exception{
		String ruler="^/usr/local/nginx/html/ZDesk/uploads+";
		String fix="/usr/local/tomcat/webapps/ZDesk/ZKM/uploads";
		Connection con=getConnection(distCon);
		String sql="select * from zkmInfoFile";
		List list=queryMap(sql, null, con);
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map data=(Map)list.get(i);
				String fp=data.get("filePath")==null?"":(String)data.get("filePath");
				String id=(String)data.get("id");
				if(fp.length()>0){
					fp=fixForRuler(ruler,fix,fp);
				}
				sql="update zkmInfoFile set filePath=? where id=?";
				con=getConnection(distCon);
				updateForPrepared(sql, new String[]{fp,id}, con);
				closeConnection(con);
			}
		}
	}
	
	public static void main(String[] args) throws Exception{
		//FixZKMData fzd=new FixZKMData();
		//String x=fzd.fixForRuler("/index.html$", "", "/usr/local/tomcat/webapps/ZDesk/ZKM/zkmDocs/flowDisponseDocs/index.html/2013/01/06/006AA643-38FF-5ACB-E113-E8B9AD3D2F18/index.html");
		//System.out.println(x);
		//String str=fzd.uploadsURLFix("/ZDesk/uploads/resource/12f");
		//System.out.println(str);
		//fzd.fixUploadsFilePath();
		//fzd.fixDataTitle();
		/*String str="/ZDesk/uploads/resource/12f";
		Pattern rule=Pattern.compile("^/ZDesk/uploads+");
		Matcher m=rule.matcher(str);
		if(m.find()){
			System.out.println(m.group(0));
			str=m.replaceAll("/ZDesk/ZKM/uploads");
			System.out.println(str);
		}*/
		
		DAOTools.initAllStatic();
		List list= SSCDBTools.loadHasSecurityUserList("textSymbol", "ZKMDocDisponseJB");
		System.out.println("--------------------------" + list.size());
	}
}
