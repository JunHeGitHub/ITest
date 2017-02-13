package com.zinglabs.util.excelUtils.model;

import java.io.OutputStream;
import java.util.List;

/**
 * Excel文件生成配置类
 * @author 王立铎
 *
 */
public class CreateExcelModel {
	private String[] fristRow;//EXCEL第一行（列头）
	private List rowDataList;//Excel数据列表
	private OutputStream os;//写入流
	private String sheetName;//sheet名字
	private String fileName;//excel文件名
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String[] getFristRow() {
		return fristRow;
	}
	public void setFristRow(String[] fristRow) {
		this.fristRow = fristRow;
	}
	public OutputStream getOs() {
		return os;
	}
	public void setOs(OutputStream os) {
		this.os = os;
	}
	public List getRowDataList() {
		return rowDataList;
	}
	public void setRowDataList(List rowDataList) {
		this.rowDataList = rowDataList;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	
}
