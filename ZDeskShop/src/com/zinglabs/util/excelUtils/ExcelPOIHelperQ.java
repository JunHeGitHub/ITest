package com.zinglabs.util.excelUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zinglabs.log.LogUtil;

import java.util.*; 
import java.io.*; 

/**
 * Excel POI 封装
 * @author 王立铎
 *
 */
public class ExcelPOIHelperQ {
	public static  Logger SKIP_Logger = LoggerFactory.getLogger("ZKM");
	Workbook wb = null;
	List<String[]> dataList = new ArrayList<String[]>(100);
	
	public ExcelPOIHelperQ(String path) {
		init(new File(path));
	}

	public ExcelPOIHelperQ(File file) {
		init(file);
	}
	
	public void init(File file){
		try {
			InputStream inp = new FileInputStream(file);
			wb = WorkbookFactory.create(inp);
			inp.close();
		} catch (FileNotFoundException e) {
			LogUtil.error(e, SKIP_Logger);
			LogUtil.error("POI 读取Excel错误，没有找到文件。",SKIP_Logger);
		} catch (InvalidFormatException e) {
			LogUtil.error("POI 读取Excel错误，文件格式错误。",SKIP_Logger);
			LogUtil.error(e, SKIP_Logger);
		} catch (IOException e) {
			LogUtil.error("POI 读取Excel错误，发生异常。",SKIP_Logger);
			LogUtil.error(e, SKIP_Logger);
		}
	}
	
	/**
	 * 取Excel所有数据，包含header
	 * 
	 * @return List<String[]>
	 */
	public List<String[]> getAllData(int sheetIndex) {
		int columnNum = 0;
		Sheet sheet = wb.getSheetAt(sheetIndex);
		if (sheet.getRow(0) != null) {
			columnNum = sheet.getRow(0).getLastCellNum()
					- sheet.getRow(0).getFirstCellNum();
		}
		if (columnNum > 0) {
			for (Row row : sheet) {
				String[] singleRow = new String[columnNum];
				int n = 0;
				for (int i = 0; i < columnNum; i++) {
					Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BLANK:
						singleRow[n] = "";
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						singleRow[n] = Boolean.toString(cell
								.getBooleanCellValue());
						break;
					// 数值
					case Cell.CELL_TYPE_NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							singleRow[n] = String.valueOf(cell
									.getDateCellValue());
						} else {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							String temp = cell.getStringCellValue();
							// 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串
							if (temp.indexOf(".") > -1) {
								singleRow[n] = String.valueOf(new Double(temp))
										.trim();
							} else {
								singleRow[n] = temp.trim();
							}
						}
						break;
					case Cell.CELL_TYPE_STRING:
						singleRow[n] = cell.getStringCellValue().trim();
						break;
					case Cell.CELL_TYPE_ERROR:
						singleRow[n] = "";
						break;
					case Cell.CELL_TYPE_FORMULA:
						cell.setCellType(Cell.CELL_TYPE_STRING);
						singleRow[n] = cell.getStringCellValue();
						if (singleRow[n] != null) {
							singleRow[n] = singleRow[n].replaceAll("#N/A", "")
									.trim();
						}
						break;
					default:
						singleRow[n] = "";
						break;
					}
					n++;
				}
				if ("".equals(singleRow[0])) {
					continue;
				}// 如果第一行为空，跳过
				dataList.add(singleRow);
			}
		}
		return dataList;
	}

	/**
	 * 返回Excel最大行index值，实际行数要加1
	 * 
	 * @return
	 */
	public int getRowNum(int sheetIndex) {
		Sheet sheet = wb.getSheetAt(sheetIndex);
		return sheet.getLastRowNum();
	}

	/**
	 * 返回数据的列数
	 * 
	 * @return
	 */
	public int getColumnNum(int sheetIndex) {
		Sheet sheet = wb.getSheetAt(sheetIndex);
		Row row = sheet.getRow(0);
		if (row != null && row.getLastCellNum() > 0) {
			return row.getLastCellNum();
		}
		return 0;
	}

	/**
	 * 获取某一行数据
	 * 
	 * @param rowIndex
	 *            计数从0开始，rowIndex为0代表header行
	 * @return
	 */
	public String[] getRowData(int sheetIndex, int rowIndex) {
		String[] dataArray = null;
		if (rowIndex > this.getColumnNum(sheetIndex)) {
			return dataArray;
		} else {
			dataArray = new String[this.getColumnNum(sheetIndex)];
			return this.dataList.get(rowIndex);
		}

	}

	/**
	 * 获取某一列数据
	 * 
	 * @param colIndex
	 * @return
	 */
	public String[] getColumnData(int sheetIndex, int colIndex) {
		String[] dataArray = null;
		if (colIndex > this.getColumnNum(sheetIndex)) {
			return dataArray;
		} else {
			if (this.dataList != null && this.dataList.size() > 0) {
				dataArray = new String[this.getRowNum(sheetIndex) + 1];
				int index = 0;
				for (String[] rowData : dataList) {
					if (rowData != null) {
						dataArray[index] = rowData[colIndex];
						index++;
					}
				}
			}
		}
		return dataArray;
	}
	
	public static void main(String [] args){
		ExcelPOIHelper eph=new ExcelPOIHelper("D:/自然语言脚本-excel-130111/因果.xls");
		List<String []> list=eph.getAllData(0);
		try {
		BufferedWriter w = new BufferedWriter(new FileWriter("D:/自然语言脚本-excel-130111/yinguo.aiml"));
        Scanner in = new Scanner("\t");
        for(int i=1;i<list.size();i++){ 
			 System.out.print(i+"\t"); 
			 in = new Scanner(i+"\t");
			 String[] x = list.get(i);
				
				w.write("<category>\r\n");
				w.write("<pattern>");
				w.write(x[0]+"</pattern>\r\n");
				String s = x[1];
				String[] n = s.split("；");
				w.write("<template>\r\n");
				w.write("<random>\r\n");
				for(int j=3;j<x.length;j++){
					if(!x[j].trim().equals("")){
					w.write("<li>"+x[j]+"</li>\r\n");
					}
				}
				w.write("</random>\r\n");
				w.write("</template>\r\n");
				w.write("</category>\r\n\r\n");

				
				if(!x[1].trim().equals("")){
				for(int m=0;m<n.length;m++){
					w.write("<category>\r\n");
					w.write("<pattern>"+n[m]+"</pattern>\r\n");
					w.write("<template>\r\n");
					w.write("<srai>"+x[0]+"</srai>\r\n");
					w.write("</template>\r\n");
					w.write("</category>\r\n\r\n");
					}
				}
				
				if(!x[2].trim().equals("")){
					w.write("<category>\r\n");
					w.write("<pattern>");
					w.write(x[2]+"</pattern>\r\n");
					w.write("<template>\r\n");
					w.write("<random>\r\n");
					for(int j=3;j<x.length;j++){
						if(!x[j].trim().equals("")){
						w.write("<li>"+x[j]+"</li>\r\n");
						}
					}
					w.write("</random>\r\n");
					w.write("</template>\r\n");
					w.write("</category>\r\n\r\n");
				}
			}

        String s = in.nextLine();
        int c;
        w.flush();
        w.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
