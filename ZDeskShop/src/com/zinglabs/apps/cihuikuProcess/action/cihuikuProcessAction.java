package com.zinglabs.apps.cihuikuProcess.action;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.validator.Var;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.appCommonCurd.action.AppCommonsCurdAction;
import com.zinglabs.apps.appCommonCurd.AppCommonsCurdService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;

@Controller
@RequestMapping(value = "/*/cihuikuProcess")
public class cihuikuProcessAction extends BaseAction {

	/*
	 * 验证上传Excel是否有效--xls格式
	 * @参数说明:
	 * 1、TitleList为，验证Excel表格标题的标准集合
	 * 2、FilePath为，Excel存放绝对地址
	 * 3、MaxLength为，验证数据的最大长度
	 */
	@RequestMapping(value = "/readXlsCheck")
	public void readXlsCheck(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		//验证标识
		boolean CheckLast =true;
		//验证错误信息
		String mgs="";
		// 验证标题集合字符串分割用于判断
		String TitleListStr = map.get("TitleList").toString();
		String[] TitleList = TitleListStr.split(",");
		InputStream is = new FileInputStream(map.get("FilePath").toString());
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);

		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			if (!CheckLast) {
				break;
			}

			// 循环行Row
			for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				if (!CheckLast) {
					break;

				}

				// 循环列Cell
				for (int cellNum = 0; cellNum <= TitleList.length; cellNum++) {
					HSSFCell hssfCell = hssfRow.getCell(cellNum);
					if (hssfCell == null) {
						continue;
					}
					String nowVal = getValueForHSSF(hssfCell);
					if (rowNum == 0) {
						//验证标题是否与提供值匹配
						if (nowVal.equals(TitleList[cellNum].toString()) == false) {
							CheckLast = false;
							mgs="标题内容与标准不一致,请重新选择";
							break;
						}
					} else {
						if (CheckLast) {
							//验证数据是否超出最大值和是否为空
							if (nowVal.length() >= Integer.parseInt(map.get("MaxLength").toString())) {
								CheckLast = false;
								mgs="单元格数据太长,服务器压力好大";
								break;
							}
							if (nowVal.length() <=0) {
								CheckLast = false;
								mgs="已存在单元格数据为空,请核查";
								break;
							}
						}
					}
				}
			}
		}
		postStrToClient(JsonUtils.genUpdateDataReturnJsonStr(CheckLast,mgs), response);
	}

	/*
	 * 读取xls文件数据并存放对应表中
	 *  @参数说明:
	 * 1、batchMark为，存放发布表的批次标识
	 * 2、FilePath为，Excel存放绝对地址
	 * 3、TableCells为，数据列存放字段集合，用于赋值
	 */
	@RequestMapping(value = "/readXlsAndInsert")
	public void readXlsAndInsert(@RequestParam
	HashMap map, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		boolean ValIsNull = false; // 获取数据是否为空
		String batchMark = map.get("batchMark").toString();
		InputStream is = new FileInputStream(map.get("FilePath").toString());
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		List list = new ArrayList();
		String TableCellsStr = map.get("TableCells").toString();
		String[] TableCells = TableCellsStr.split(",");
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}

			// 循环行Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				HashMap nowMap = new HashMap();
				// 循环列Cell
				for (int cellNum = 0; cellNum < TableCells.length; cellNum++) {
					HSSFCell hssfCell = hssfRow.getCell(cellNum);
					if (hssfCell == null) {
						continue;
					}
					String nowVal = getValueForHSSF(hssfCell);
					if (nowVal.length() != 0) {
						nowMap.put(TableCells[cellNum], nowVal);
					} else {
						ValIsNull = true; // 标识为空,用于判断是否添加对象
					}
				}
				if (!ValIsNull) {
					//发布时间
					nowMap.put("publishTime", new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(Calendar.getInstance().getTime()));
					nowMap.put("batchMark", batchMark);
					list.add(nowMap);
				}
				ValIsNull = false; // 恢复原状态，方便下次判别操作
			}
		}

		postStrToClient(JsonUtils
				.genUpdateDataReturnJsonStr(true, "查询成功", list), response);
	}

	/*
	 * xls文件读取方法
	 */
	@SuppressWarnings("static-access")
	private String getValueForHSSF(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	// 对应访问Service
	public AppCommonsCurdService getService() {
		return (AppCommonsCurdService) getBean("appCommonsCurdService");
	}
}