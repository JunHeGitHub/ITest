package com.zinglabs.servlet.zkmDocTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.util.FileUtils;
import com.zinglabs.util.ZKMConfs;
import com.zinglabs.util.html.HtmlHelper;
import java.util.*;

public class ZKMDocHelper {
	
	public static  Logger logger = LoggerFactory.getLogger("ZKM"); 
	
	public static String templateClass;
	
	public static ZkmDocGenerator zkmDocGen;
	
	static{
		templateClass=ZKMConfs.confs.getProperty("templateClass","com.zinglabs.servlet.zkmDocTemplate.templateImp.ZkmDocTemplateDefault");
		zkmDocGen=getZkmDocTemplate(templateClass);
	}
	
	public static ZkmDocGenerator getZkmDocTemplate(String className){
		ZkmDocGenerator adg=null;
		try{
			Object obj=Class.forName(className).newInstance();
			if(obj instanceof ZkmDocGenerator){
				adg=(ZkmDocGenerator)obj;
			}
		}catch(Exception x){
			logger.error("ZkmDocGenerator error : " + className);
		}
		return adg;
	}
	
	public static String zkmDocFixAndGen(String docStr) throws Exception{
		//TODO:不对正文进行处理
		//docStr=zkmDocGen.getZkmContent(docStr);
		//docStr=ZkmDocFix(docStr,null);
		//docStr=zkmDocGen.getZkmContent(docStr);
		return docStr;
	}
	
	public static String zkmDocFixAndGen(String docStr,Map param) throws Exception{
		//TODO:不对正文进行处理
		//docStr=zkmDocGen.getZkmContent(docStr);
		//docStr=ZkmDocFix(docStr,param);
		//docStr=zkmDocGen.getZkmContent(docStr);
		return docStr;
	}
	
	public static String ZkmDocFix(String docStr,Map param) throws Exception{
		String restr="";
		if(docStr!=null && docStr.length()>0){
			Parser parser=HtmlHelper.createLexerParser(docStr);
			HtmlPage page=HtmlHelper.createHtmlPage(docStr, parser);
			NodeList list=page.getBody();
			if(list!=null && list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					Node tag = list.elementAt(i);
					processTags(tag,param);
				}
				restr=list.toHtml();
			}
		}
		return restr;
	}
	
	public static void processTags(Node tag,Map param){
		try{
			disposeTags(tag,param);
		}catch(Exception x){
			x.printStackTrace();
		}
		NodeList list=tag.getChildren();
		
		if(list!=null && list.size()>0){
			for(int i = 0; i < list.size(); i++){
				Node ct = list.elementAt(i);
				processTags(ct,param);
			}
		}
	}
	
	public static void disposeTags(Node tag,Map param) throws Exception{
		if (tag instanceof LinkTag){
			LinkTag link = (LinkTag) tag;
			String linkUrl = link.getLink();
			if(linkUrl!=null && linkUrl.length()>0){
				if(linkUrl.indexOf("#")>=0){
					linkUrl=linkUrl.substring(linkUrl.indexOf("#"),linkUrl.length());
					link.setLink(linkUrl);
				}
				//只有文件名，没有URI的处理
				
			}
			String name=link.getAttribute("name");
			if(name!=null && name.length()>0){
				NodeList list=link.getChildren();
				if(list!=null && list.size()>0){
					Node pn=link.getParent();
					NodeList plist=pn.getChildren();
					if(pn!=null){
						if(plist==null){
							plist=new NodeList();
						}
						for(int i=list.size()-1;i>=0;i--){
							Node e=list.elementAt(i);
							if(e!=null){
								plist.add(e);
								link.removeChild(i);
							}
						}
						pn.setChildren(plist);
					}
				}
			}
		}else if (tag instanceof ImageTag){
			ImageTag image = (ImageTag) tag;
			String src=image.getImageURL();
			if(src!=null && src.length()>0){
				if(param!=null){
					String path=param.get("disponseFilePath")==null?"":(String)param.get("disponseFilePath");
					if(path.length()>0){
						if(src.indexOf("/ZDesk")<0 && src.indexOf("/")<0){
							image.setImageURL(path + "/" + src);
						}
					}
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
	
    public static String clearWordFormat(String content) {  
        //把<P></P>转换成</div></div>保留样式  
        //content = content.replaceAll("(<P)([^>]*>.*?)(<\\/P>)", "<div$2</div>");  
        //把<P></P>转换成</div></div>并删除样式  
        content = content.replaceAll("(<P)([^>]*)(>.*?)(<\\/P>)", "<p$3</p>");  
        //删除不需要的标签  
        content = content.replaceAll("<[/]?(font|FONT|span|SPAN|xml|XML|del|DEL|ins|INS|meta|META|[ovwxpOVWXP]:\\w+)[^>]*?>", "");  
        //删除不需要的属性  
        content = content.replaceAll("<([^>]*)(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|[ovwxpOVWXP]:\\w+)=(?:'[^']*'|\"\"[^\"\"]*\"\"|[^>]+)([^>]*)>", "<$1$2>");  
        //删除<STYLE TYPE="text/css"></STYLE>及之间的内容
        //int styleBegin = content.indexOf("<STYLE");
        //int styleEnd = content.indexOf("</STYLE>") + 8;
        //String style = content.substring(styleBegin, styleEnd);  
        //content = content.replace(style, "");  
        return content;  
    }
	
    public static void disponseFolders(File file) throws Exception{
    	if(file!=null && file.exists() && file.isDirectory()){
    		File[] fs=file.listFiles();
    		if(fs!=null && fs.length>0){
    			for(File f:fs){
    				if(f.exists() && f.isDirectory()){
    					disponseFolders(f);
    				}else if(f.exists() && f.isFile()){
    					String name=f.getName();
    					System.out.println("------ path : " + f.getPath());
    					System.out.println("-------name ：" + f.getName());
    					if(name.indexOf(".html")>0){
    						String content=FileUtils.getFileRealContent(f);
    						if(content.length()>0){
	    						content=ZkmDocFix(content,null);
	    						content=clearWordFormat(content);
	    						content=zkmDocGen.getZkmContent(content);
	    						FileUtils.appendToFile(content, f, false);
    						}
    					}
    				}
    			}
    		}
    	}
    }
    
	public static void main(String [] args) throws Exception{
		File file=new File("C:/aaaaa/总中心_中国银行“百年行庆”/");
		System.out.println(file.getPath());
		/*StringBuffer str= new StringBuffer();
		InputStreamReader fr=new InputStreamReader(new FileInputStream(file));
		int in=1024;
		char[] inc=new char[1024];
		while(fr.read(inc)>0){
			str.append(inc);
		}
		Map map=new HashMap();
		map.put("disponseFilePath", "/ZDesk/uploads");
		System.out.println(zkmDocFixAndGen(str.toString()));*/
		//File file=new File("C:/aaaaa/xx");
		//disponseFolders(file);
	}
}
