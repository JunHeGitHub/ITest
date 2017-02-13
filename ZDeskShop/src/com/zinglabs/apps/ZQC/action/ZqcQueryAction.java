package com.zinglabs.apps.ZQC.action;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zinglabs.apps.ZQC.ZqcQueryService;
import com.zinglabs.base.core.frame.BaseAction;
import com.zinglabs.util.JsonUtils;
import com.zinglabs.util.excelUtils.ExcelHelper;
import com.zinglabs.util.excelUtils.model.CreateExcelModel;

@Controller
@RequestMapping("/*/ZQC")
public class ZqcQueryAction extends BaseAction {
	public static Logger logger = LoggerFactory.getLogger("ZDesk");

	@RequestMapping(value = "/queryWenBenFenPei")
	public void queryWenBenFenPei(@RequestParam
	HashMap<String, String> map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Map<String, String>> mapList = getService()
				.searchWenBenFenPeiInfo(map);
		postStrToClient(
				JsonUtils.genUpdateDataReturnJsonStr(true, "", mapList),
				response);
	}

	@RequestMapping(value = "/getTotalRow")
	public void getTotalRow(@RequestParam
	HashMap<String, String> map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Map<String, String>> mapList = getService().searchgetTotalRow(map);
		postStrToClient(
				JsonUtils.genUpdateDataReturnJsonStr(true, "", mapList),
				response);
	}

	// excel导出
	@RequestMapping(value = "/getExcelData")
	public void getExcelData(@RequestParam
	HashMap<String, String> map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String fname = "质检评分数据.xls";
		CreateExcelModel cem = new CreateExcelModel();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename="
				+ new String(fname.getBytes("gb2312"), "iso8859-1"));
		List eList = new ArrayList();
		List dictinfoData = new ArrayList();
		String gradename = "";
		String renwushijian="";
		if(map.get("pingFenTableName").equals("T_GRADE_DETAIL_NEW")){
			renwushijian="begin_time_devoir";
		}
		if(map.get("pingFenTableName").equals("T_GRADE_DETAIL_FUJIAN_NEW")){
			renwushijian="begin_time_retest";
		}
		if(map.get("shangSuTableName")!=null){
			if(map.get("shangSuTableName").equals("SA_AGENT_ARGUMENT")){
				renwushijian="agent_user_dt";
			}
		}
		List list = getService().searchExcelData(map);
		// first 第一行标题
		Map tMap = (Map) list.get(0);
		String[] title1 = { "服务开始时间", "任务开始时间", "评分流水号", "质检员", "座席工号", "总分" };
		String[] title2 = tMap.get("aaa").toString().split(",");
		String titleStr = "";
		for (int i = 0; i < title2.length; i++) {
			titleStr += "评分项" + (i + 1) + "," + "评分值" + (i + 1) + "," + "评语"
					+ (i + 1) + ",";

		}
		String[] arr = titleStr.split(",");
		String[] array = new String[(arr.length + title1.length) + 1]; // 定义数组
		// 作为excel表头
		for (int i = 0; i < (arr.length + title1.length); i++) {
			if (i < title1.length) {
				array[i] = title1[i];
			} else {
				array[i] = arr[i - title1.length];
			}
		}
		array[array.length - 1] = "总评语";
		// 表头组合结束

		// 开始拼装数据
		for (int i = 0; i < list.size(); i++) {
			Map eMap = (Map) list.get(i);
			String strList = "";
			String strList2 = "";
			String[] DESCRIPTION = eMap.get("aaa").toString().split(",");
			String[] grade_score = eMap.get("bbb").toString().split(",");
			String[] remark_describe = eMap.get("ccc").toString().split(",");
			String[] grade_index_id1 = eMap.get("ddd").toString().split(",");
			String grade_name = eMap.get("grade_name").toString();
			Map grade_index_id2 = new HashMap<String, String>();
			grade_index_id2.put("grade_name", grade_name);
			if (grade_index_id2.size() > 0) {
				if (!gradename.equals(grade_name)) {//如果第一条和第二条评分指标相同 就不会再从新获取
					if (gradename == "") {			
						gradename = grade_name;
					}
					dictinfoData = getService().searchDictinfo(grade_index_id2);//获取评分值名称
				}

			}

			for (int k = 0; k < DESCRIPTION.length; k++) {

				Map dictInfoMap = (Map) dictinfoData.get(k);
				for (int j = 0; j < grade_index_id1.length; j++) {
					String str1 = grade_index_id1[j];
					String str2 = (String) dictInfoMap.get("ID");
					if (str1.equals(str2)) {
						String[] dicScoreVal = dictInfoMap.get(
								"reference2_value").toString().split(",");
						String[] dicScoreNam = dictInfoMap.get(
								"reference2_name").toString().split(",");
						String reference2_name = "";
						for (int l = 0; l < dicScoreVal.length; l++) {
							int dicnum = Integer.parseInt(dicScoreVal[l]);
							int chujiannum = Integer.parseInt(grade_score[k]
									.substring(0, grade_score[k].indexOf(".")));
							if (dicnum == chujiannum) {
								reference2_name = dicScoreNam[l];

							}
						}

						strList += DESCRIPTION[k] + "," + reference2_name + ","
								+ remark_describe[k] + ",";
					}
				}
			}
			strList2 = (String) eMap.get("begin_time") + ","
					+ (String) eMap.get(renwushijian) + ","
					+ (String) eMap.get("grade_id") + ","
					+ (String) eMap.get("owner") + ","
					+ (String) eMap.get("alias1") + ","
					+ (String) eMap.get("pfx_total") + "," + strList
					+ (String) eMap.get("grade_remark");
			eList.add(strList2.split(","));
			// eList.add(i,eMap.values());
		}// 拼装数据结束
		
		
		//开始下载excel
		cem.setOs(response.getOutputStream());
		cem.setFristRow(array);
		cem.setRowDataList(eList);
		cem.setSheetName("exportData");
		ExcelHelper.makeExcel(cem);

		// postStrToClient(
		// JsonUtils.genUpdateDataReturnJsonStr(true, "", mapList),
		// response);
	}

	public ZqcQueryService getService() {
		return (ZqcQueryService) getBean("zqcQuery");
	}
}
