package com.zinglabs.util.excelUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.zinglabs.log.LogUtil;
import com.zinglabs.util.ZKMConfs;
import com.zinglabs.util.excelUtils.model.CreateExcelModel;
import com.zinglabs.util.excelUtils.model.ReadExcelModel;

/**
 * Excel读写帮助类
 * @author 王立铎
 *
 */
public class ExcelHelper {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM"); 
	
	/**
	 * 新建一个EXCEL
	 * @param ec Excel配置类
	 * @return int 0 成功，-1失败
	 */
public static int makeExcel(CreateExcelModel ec) throws Exception{
		OutputStream os=ec.getOs();
		try{
			WritableWorkbook wb=Workbook.createWorkbook(os);
			WritableSheet ws=wb.createSheet(ec.getSheetName(), 0);
			int rowNum=0;
			String [] head=ec.getFristRow();
			if(head!=null && head.length>0){
				for(int i=0;i<head.length;i++){
					Label l=new Label(i,0,head[i]);
					ws.addCell(l);
				}
				rowNum++;
			}
			List list=ec.getRowDataList();
			for(int j=0;j<list.size();j++){
				String [] s=(String[])list.get(j);
				for(int k=0;k<s.length;k++){
					Label l=new Label(k,rowNum,s[k]);
					ws.addCell(l);
				}
				rowNum++;
			}
			wb.write();
			os.flush();
			wb.close();
		}catch(Exception x){
			System.out.println(x.getMessage());
			return -1;
		}finally{
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
/**
 * 读取Excel返回内容数组
 * 返回值，行List，列List
 * @param reml
 * @return
 */
	public static List readExcel(ReadExcelModel reml){
		List relist=new ArrayList();
		Workbook wb=null;
		Cell tcell=null;
		try{
			wb=Workbook.getWorkbook(reml.getIn());
		}catch(Exception x){
			x.printStackTrace();
		}
		Sheet sheet=wb.getSheet(reml.getSheetIndex());
		for(int i=reml.getStartRowIndex();i<sheet.getRows();i++){
			List list=new ArrayList();
			for(int j=0;j<sheet.getColumns();j++){
				tcell=sheet.getCell(j,i);
				list.add(tcell.getContents());
			}
			relist.add(list);
		}
		wb.close();
		try {
			reml.getIn().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//清除空行
		cleanNullRow(relist);
		//清除多余的列
		cleanOtioseCell(relist);
		return relist;
	}
	/**
	 * 清除空行
	 * @param list 满足规则rowlist包含celllist的List数组
	 * @return
	 */
	public static List cleanNullRow(List list){
		//实现原理：判断一行中的列，有一个不为空或""，
		//就断定这一行不是空行
		if(list!=null){
			for(int i=list.size()-1;i>=0;i--){
				List clist=(List)list.get(i);
				boolean isClean=true;
				for(int j=0;j<clist.size();j++){
					String str=(String)clist.get(j);
					if(str!=null && !"".equals(str)){
						isClean=false;
						break;
					}
				}
				if(isClean){
					list.remove(i);
				}
			}
		}
		return list;
	}
	/**
	 * 清除多余的列
	 * @param list 满足规则rowlist包含celllist的List数组
	 * @return
	 */
	public static List cleanOtioseCell(List list){
		//实现原理：取第一行，得到第一行的有效列数（有效列：不为空的列）
		//用第一行的有效列数与其它行的有效列数比，若其它行的有效列数大于第一行的有效列数
		//就从后取并且去掉多出的那部分。
		if(list!=null && list.size()>0){
			List flist=(List)list.get(0);
			for(int i=flist.size()-1;i>=0;i--){
				String str=(String)flist.get(i);
				if(str==null || "".equals(str)){
					flist.remove(i);
				}
			}
			int cnum=flist.size();
			for(int i=1;i<list.size();i++){
				List tlist=(List)list.get(i);
				int tnum=tlist.size();
				if(tnum>cnum){
					int xnum=tnum-cnum;
					for(int j=0;j<xnum;j++){
						tlist.remove(tlist.size()-1);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 导出结果
	 * @param fileName
	 * @param sheetName
	 * @param datalist
	 * @param fields
	 * @param fieldNames
	 * @param response
	 * @throws Exception
	 */
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
						String vl = dm.get(field) == null ? "" : (String) dm.get(field);
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
				if(sheetName!=null && sheetName.length()>0){
					cem.setSheetName(sheetName);
				}else{
					cem.setSheetName("exportData");
				}
				makeExcel(cem);
			}
		}
	}
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		//String filePath=ExcelHelper.class.getResource("C:/aaaaa/11111.xls").toString();
		//filePath=filePath.substring(filePath.indexOf("/"),filePath.length());
		//System.out.println(filePath);
		InputStream in=new FileInputStream(new File("E:/testImp/macthes/new/siebel知识库下载/安徽_分支机构及自助设备信息调查表120918.xls"));
		ReadExcelModel rem=new ReadExcelModel();
		rem.setStartRowIndex(0);
		rem.setSheetIndex(0);
		rem.setIn(in);
		ExcelHelper eh=new ExcelHelper();
		List list=eh.readExcel(rem);
		System.out.println(list.size());
		for(int i=0;i<list.size();i++){
			List tlist=(List)list.get(i);
			for(int j=0;j<tlist.size();j++){
				System.out.print(tlist.get(j) + "  ");
			}
			System.out.println("");
		}
		
		//ExcelHelper.init();
	}
}
