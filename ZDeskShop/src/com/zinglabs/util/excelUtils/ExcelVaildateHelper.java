package com.zinglabs.util.excelUtils;

import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.log.LogUtil;
import com.zinglabs.util.FileUtils;
import com.zinglabs.util.ZKMConfs;



public class ExcelVaildateHelper {
	
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	public static String definedFolder;
	public static Map<String,List<String[]>> definedExcel;
	
	static{
		init();
	}
	
	public static void init(){
		definedFolder=ZKMConfs.confs.getProperty("excelDefined","");
		definedExcel=new HashMap<String,List<String[]>>();
		if(definedFolder!=null && definedFolder.length()>0){
			File file=new File(definedFolder);
			if(file.exists() && file.isDirectory()){
				File [] fs=file.listFiles();
				for(File f:fs){
					//只初始化文件
					if(f.exists() && f.isFile()){
						boolean isExcel=false;
						String en=FileUtils.getFileExtendName(f);
						if(en.length()>0 && (en.toUpperCase().equals(".XLS") || en.toUpperCase().equals(".XLSX"))){
							isExcel=true;
						}
						try{
							/*Map map=FileUtils.parserFile(f);
							Map mdt=(Map)map.get(FileUtils.FILE_METADATA);
							String mt=(String)mdt.get("Content-Type");
							if(mt.equals(FileUtils.FILE_MIMETYPE_OFFICE_XLS) || mt.equals(FileUtils.FILE_MIMETYPE_OFFICE_XLSX)){
							*/
							if(isExcel){
								ExcelPOIHelper eph=new ExcelPOIHelper(f);
								if(eph.wb!=null){
									List<String[]> list=eph.getAllData(0);
									if(list!=null && list.size()>0){
										String key=f.getName();
										key=key.substring(0,key.lastIndexOf("."));
										definedExcel.put(key, list);
									}
								}else{
									LogUtil.error(" excel定义文件："  + f.getPath() + "初始化失败，跳过",SKIP_Logger);
								}
							}else{
								LogUtil.error(" excel定义文件："  + f.getPath() + " 非excel格式文件，跳过",SKIP_Logger);
							}
						}catch(Exception x){
							LogUtil.error(" excel定义文件："  + f.getPath() + " 初始化异常，跳过",SKIP_Logger);
							LogUtil.error(x, SKIP_Logger);
						}
					}
				}
			}else{
				LogUtil.error(" excel定义目录："  + definedFolder + " 不是目录或目录不存在。",SKIP_Logger);
			}
		}else{
			LogUtil.error("获取 excel 定义目录参数失败。",SKIP_Logger);
		}
	}
	
	public Map vaildateExcelFile(String key,String file){
		return vaildateExcelFile(key,new File(file));
	}
	
	public Map vaildateExcelFile(String key,File file){
		Map rem=new HashMap();
		List<Map> mgsList=new ArrayList<Map>();
		boolean flag=false;
		if(file.exists() && file.isFile()){
			if(definedExcel.containsKey(key)){
				List<String[]> dlist=definedExcel.get(key);
				ExcelPOIHelper eph=new ExcelPOIHelper(file);
				if(eph.wb!=null){
					List<String[]> list=eph.getAllData(0);
					if(list.size()<dlist.size()){
						flag=false;
						mgsList.add(genMgsMaper(false,key,file.getName(),0,0,"文件内容rows验证失败，被比对文件行数据少于验证文件。"));
					}else{
						flag=true;
						for(int i=0;i<dlist.size();i++){
							String[] celld=dlist.get(i);
							String[] cellf=list.get(i);
							//System.out.println(celld.length + " " + cellf.length);
							if(cellf.length != celld.length){
								mgsList.add(genMgsMaper(false,key,file.getName(),i,0,"文件内容cells验证失败，被比对文件列数据不等于验证文件。"));
								flag=false;
								break;
							}else{
								for(int j=0;j<celld.length;j++){
									String dc=celld[j]==null?"":celld[j];
									String fc=cellf[j]==null?"":cellf[j];
									dc=dc.trim();
									fc=fc.trim();
									if(!dc.equals(fc)){
										flag=false;
										mgsList.add(genMgsMaper(false,key,file.getName(),i,j,"文件内容cells验证失败，被比对文件列与验证文件列不符，" + fc + " : " + dc + "。"));
									}
								}
							}
						}
					}
				}else{
					mgsList.add(genMgsMaper(false,key,file.getName(),0,0,"未找到须验证的excel文件或excel文件不存在。"));
					flag=false;
				}
			}else{
				mgsList.add(genMgsMaper(false,key,file.getName(),0,0,"未找到excel验证定义数据。"));
				flag=false;
			}
		}else{
			mgsList.add(genMgsMaper(false,key,file.getName(),0,0,"未找到excel文件。"));
			flag=false;
		}
		rem.put("success", flag);
		rem.put("mgsList", mgsList);
		return rem;
	}
	
	public boolean getTF(Map reMap){
		return ((Boolean)reMap.get("success")).booleanValue();
	}
	
	public List<Map> getMgsLists(Map reMap){
		return (List<Map>)reMap.get("mgsList");
	}
	
	public Map genMgsMaper(boolean success,String key,String file,int rowNum,int cellNum,String mgs){
		Map map=new HashMap();
		map.put("success", success);
		map.put("key", key);
		map.put("file", file);
		map.put("rowNum", rowNum);
		map.put("cellNum", cellNum);
		map.put("mgs", mgs);
		return map;
	}
	
	public static void main(String [] args){
		
		ExcelVaildateHelper evh=new ExcelVaildateHelper();
		Map map=evh.vaildateExcelFile("分支机构调查表", "E:/testImp/macthes/new/测试.xlsx");
		boolean tf=evh.getTF(map);
		List list=evh.getMgsLists(map);
		System.out.println(tf);
		System.out.println(list.size());
	}
}
