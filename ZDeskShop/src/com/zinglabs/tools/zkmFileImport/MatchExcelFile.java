package com.zinglabs.tools.zkmFileImport;
import java.util.*;
import java.io.*;
import java.nio.charset.Charset;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;

import com.zinglabs.util.excelUtils.ExcelHelper;
import com.zinglabs.util.excelUtils.model.ReadExcelModel;

public class MatchExcelFile {

	//public static final String[] EF={"机器号","状态","安装地址"};
	public static final String[] EF={"网点名称（必填、唯一标识）","网点曾用名","交换号","状态","地址及邮编","营业时间（周一至周五）","周末营业时间","咨询电话","POS维护团队联系人及电话","业务受理范围","机构描述","所属市","所属区域","所属机构","机构编号","机构属性"};
	
	public static List slist=new ArrayList();
	public static List mlist=new ArrayList();
	public static Integer[] shnum=new Integer[EF.length];
	public static Integer[] mhnum=new Integer[EF.length];
	public static List errorlist=new ArrayList();
	public static boolean flag=true;
	public static OutputStreamWriter osw=null;
	public static OutputStreamWriter oswx=null;
	public static OutputStreamWriter oswxm=null;
	public static String sfile="";
	public static String mfile="";
	public static int lostNum=0;
	public static int sameLostNum=0;
	
	public static void initData(File sfile,int sr,int si,File mfile,int mr,int mi){
		List sl=readExcel(sfile,sr,si,"s");
		List ml=readExcel(mfile,mr,mi,"m");
		System.out.println(sl.size() + "  " + ml.size());
	}
	
	public static List readExcel(File file,int startRow,int sheetNum,String type){
		List list=null;
		if(file.exists() && file.isFile()){
			InputStream in=null;
			try{
				in=new FileInputStream(file);
				POIFSFileSystem poiFileSystem = new POIFSFileSystem(in);
				HSSFWorkbook workbook = new HSSFWorkbook(poiFileSystem);
				HSSFSheet sheet = workbook.getSheetAt(sheetNum);
				int rowCount =sheet.getPhysicalNumberOfRows();
				//获取字义列位置
				HSSFRow row=sheet.getRow(startRow);
				for(int j=0;j<EF.length;j++){
					String efv=EF[j];
					boolean has=false;
					int cellCount = row.getPhysicalNumberOfCells();
					for(int i=0;i<cellCount;i++){
						HSSFCell cell= row.getCell(i);
						String value=getCell(cell);
						value=value.trim();
						if(value.length()>0 && efv.equals(value)){
							if("s".equals(type)){
								shnum[j]=i;
							}else{
								mhnum[j]=i;
							}
							has=true;
						}
					}
					if(!has){
						flag=false;
						System.out.println(file.getName() + " 缺少定义列：" + efv);
						break;
					}
				}
				//获取数据
				Integer [] tf=null;
				if("s".equals(type)){
					tf=shnum;
				}else{
					tf=mhnum;
				}
				for(int i=startRow+1;i<rowCount;i++){
					HSSFRow trow=sheet.getRow(i);
					/*int tc=trow.getPhysicalNumberOfCells();
					if(tc<EF.length){
						osw.write("文件：" + file.getName() + " 行 " + (i-1) + " 不附合列定义，"+ tc +" 跳过。\n");
						MatchExcelFile.osw.flush();
						continue;
					}*/
					Map dmap=new HashMap();
					dmap.put("row", String.valueOf(i-1));
					for(int j=0;j<tf.length;j++){
						HSSFCell cell=null;
						try{
							cell= trow.getCell(tf[j]);
						}catch(Exception x){
							//System.out.println("cell is null " + tf[j] + " " + i);
						}
						String value="";
						if(cell!=null){
							value=getCell(cell);
							value=value.trim();
							value=valueFix(value);
						}
						dmap.put(EF[j], value);
					}
					if("s".equals(type)){
						slist.add(dmap);
					}else{
						mlist.add(dmap);
					}
				}
			}catch(Exception x){
				x.printStackTrace();
			}
		}
		return list;
	}
	
	public static String getCell(HSSFCell cell) {
	    if (cell == null)
	      return "";
	    switch (cell.getCellType()) {
	      case HSSFCell.CELL_TYPE_NUMERIC:
	        return cell.getNumericCellValue() + "";
	      case HSSFCell.CELL_TYPE_STRING:
	        return cell.getStringCellValue();
	      case HSSFCell.CELL_TYPE_FORMULA:
	        return cell.getCellFormula();
	      case HSSFCell.CELL_TYPE_BLANK:
	        return "";
	      case HSSFCell.CELL_TYPE_BOOLEAN:
	        return cell.getBooleanCellValue() + "";
	      case HSSFCell.CELL_TYPE_ERROR:
	        return cell.getErrorCellValue() + "";
	    }
	    return "";
	  }
	
	public static void matchList() throws Exception{
		oswx.write("-----------------------------" +  sfile + "(" + slist.size() +") siebel-----------------------------------------\n");
		oswx.write("-----------------------------比对-----------------------------------------\n");
		oswx.write("-----------------------------" +  mfile + "(" + mlist.size() +")-----------------------------------------\n");
		oswxm.write("<tr>\n");
		oswxm.write("<td colspan='" + EF.length+ "'>"+sfile+"(" + slist.size() +") - siebel　比对　" +  mfile +"(" + mlist.size() +") </td>\n");
		oswxm.write("</tr>\n");
		matchFJ(slist,mlist,"s");
		oswx.write("-----------------------------统计-----------------------------------------\n");
		oswx.write("-----------------------------" + lostNum + "-----------------------------------------\n");
		oswx.write("----------------------------------------------------------------------\n");
		oswxm.write("<tr>\n");
		oswxm.write("<td colspan='" + EF.length+ "'>统计："+ sameLostNum + "</td>\n");
		oswxm.write("</tr>\n");
		
		oswx.write("-----------------------------" +  mfile + "(" + mlist.size() +")-----------------------------------------\n");
		oswx.write("-----------------------------比对-----------------------------------------\n");
		oswx.write("-----------------------------" +  sfile + "(" + slist.size() +") siebel-----------------------------------------\n");
		oswxm.write("<tr>\n");
		oswxm.write("<td colspan='" + EF.length+ "'>"+mfile+"(" + mlist.size() +")　比对　" +  sfile +"(" + slist.size() +") siebel </td>\n");
		oswxm.write("</tr>\n");
		matchFJ(mlist,slist,"m");
		oswx.write("-----------------------------统计-----------------------------------------\n");
		oswx.write("-----------------------------" + lostNum + "-----------------------------------------\n");
		oswx.write("----------------------------------------------------------------------\n");
		oswxm.write("<tr>\n");
		oswxm.write("<td colspan='" + EF.length+ "'>统计："+ sameLostNum + "</td>\n");
		oswxm.write("</tr>\n");
	}
	
	public static void matchFJ(List list1,List list2,String type) throws Exception{
		int hh=0;
		sameLostNum=0;
		lostNum=0;
		String st1="style='background:#D5D5D5'";
		String st2="style='background:#FFFFFF'";
		String file="";
		if("s".equals(type)){
			file=sfile;
		}else{
			file=mfile;
		}
		for(int i=0;i<list1.size();i++){
			Map map1=(Map)list1.get(i);
			Map map2=null;
			for(int j=0;j<list2.size();j++){
				Map tm=(Map)list2.get(j);
				boolean mat1=true;
				if(map1.get(EF[0]).equals(tm.get(EF[0]))){
					map2=tm;
					break;
				}
			}
			if(map2!=null){
				boolean same=true;
				for(int j=0;j<EF.length;j++){
					if(!map1.get(EF[j]).equals(map2.get(EF[j]))){
						same=false;
						break;
					}
				}
				if(!same){
					sameLostNum++;
					String line1="";
					String line2="";
					line1+="<td>" + map1.get("row") + "</td>\n";
					line2+="<td>" +map2.get("row") + "</td>\n";
					for(int j=0;j<EF.length;j++){
						if(!map1.get(EF[j]).equals(map2.get(EF[j]))){
							line1+="<td style='color:red'>" + map1.get(EF[j]) + "</td>\n";
							line2+="<td style='color:red'>" +map2.get(EF[j]) + "</td>\n";
						}else{
							line1+="<td>" + map1.get(EF[j]) + "</td>\n";
							line2+="<td>" +map2.get(EF[j]) + "</td>\n";
						}
					}
					String std="";
					if(hh==0){
						std=st1;
						hh=1;
					}else{
						std=st2;
						hh=0;
					}
					oswxm.write("<tr " + std +">\n");
					oswxm.write(line1);
					oswxm.write("</tr>\n");
					oswxm.write("<tr  " + std +">\n");
					oswxm.write(line2);
					oswxm.write("</tr>\n");
					oswx.flush();
				}
			}else{
				lostNum++;
				oswx.write(map1.get("row") + "," + map1.get(EF[0]) + ",未找到命中项。\n");
				oswx.flush();
			}
		}
	}
	
	public static void fixList(List flist) throws Exception{
		for(int i=flist.size()-1;i>=0;i--){
			Map map=(Map)flist.get(i);
			String str=(String)map.get(EF[0]);
			if(str.length()<=0){
				flist.remove(i);
			}
		}
	}
	
	public static String valueFix(String value){
		value=value.replaceAll("　", "");
		value=value.replaceAll(" ", "");
		value=value.replaceAll("中国银行股份有限公司", "");
		value=value.replaceAll("中国银行", "");
		value=value.replaceAll("安徽省", "");
		value=value.replaceAll("安徽", "");
		return value;
	}
	
	public static void main(String[] args) throws Exception {
		File file=new File("e:/bbb.txt");
		File filex=new File("E:/testImp/macthes/match_r/安徽_分支机构_log.txt");
		File filem=new File("E:/testImp/macthes/match_r/安徽_分支机构_Match.html");
		MatchExcelFile.osw=new OutputStreamWriter(new FileOutputStream(file),Charset.forName("gb2312"));
		MatchExcelFile.oswx=new OutputStreamWriter(new FileOutputStream(filex),Charset.forName("gb2312"));
		MatchExcelFile.oswxm=new OutputStreamWriter(new FileOutputStream(filem),Charset.forName("gb2312"));
		MatchExcelFile.oswxm.write("<html>");
		MatchExcelFile.oswxm.write("<body>");
		MatchExcelFile.oswxm.write("<table border='1'>");
		//MatchExcelFile.sfile="E:/testImp/macthes/1.xls";
		//MatchExcelFile.mfile="E:/testImp/macthes/2.xls";
		MatchExcelFile.sfile="安徽_分支机构及自助设备信息调查表120918.xls";
		MatchExcelFile.mfile="安徽分行-附件1：网点机构服务信息调查表.xls";
		
		String f1="E:/testImp/macthes/new/siebel知识库下载/安徽_分支机构及自助设备信息调查表120918.xls";
		String f2="E:/testImp/macthes/new/个金提供分支机构及自助设备/分支机构/安徽分行-附件1：网点机构服务信息调查表.xls";
		File sfile=new File(f1);
		File mfile=new File(f2);
		//MatchExcelFile.initData(sfile, 3, 1, mfile, 3,0);
		
		MatchExcelFile.readExcel(sfile,1,0,"s");
		MatchExcelFile.readExcel(mfile,2,0,"m");
		
		MatchExcelFile.fixList(MatchExcelFile.slist);
		MatchExcelFile.fixList(MatchExcelFile.mlist);
		
		osw.write(MatchExcelFile.sfile +"," + MatchExcelFile.slist.size() + "\n");
		osw.write(MatchExcelFile.mfile +"," + MatchExcelFile.mlist.size() + "\n");
		
		MatchExcelFile.matchList();
		MatchExcelFile.oswxm.write("</table>");
		MatchExcelFile.oswxm.write("</body>");
		MatchExcelFile.oswxm.write("</html>");
		MatchExcelFile.osw.flush();
		MatchExcelFile.oswx.flush();
		MatchExcelFile.oswxm.flush();
		MatchExcelFile.osw.close();
		MatchExcelFile.oswx.close();
		MatchExcelFile.oswxm.close();
	}
}
