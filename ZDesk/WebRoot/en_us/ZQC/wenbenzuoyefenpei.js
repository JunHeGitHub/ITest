
var ZQC_GOLBAL_PARAM = {};
ZQC_GOLBAL_PARAM["zhiJianYuanData"] = {};
//处理显示与隐藏
function showLingQu() {
	$("#updateFormDiv").slideUp(300);
	$("#fenPeiDiv").slideUp(300);
	$("#lingQuDiv").slideDown(300);
	$("#btn_tijiaofenpei").hide();
	$("#btn_fenpei").show();
	$("#btn_lingqu").hide();
	$("#btn_tijiaolingqu").show();
	scroll2data("lingQuDiv");
}
function showFenPei() {
	$("#updateFormDiv").slideUp(300);
	$("#lingQuDiv").slideUp(300);
	$("#fenPeiDiv").slideDown(300);
	$("#btn_tijiaolingqu").hide();
	$("#btn_lingqu").show();
	$("#btn_fenpei").hide();
	$("#btn_tijiaofenpei").show();
	scroll2data("fenPeiDiv");
}
function hideAll() {
	$("#lingQuDiv").slideUp(300);
	$("#fenPeiDiv").slideUp(300);
	$("#btn_tijiaolingqu").hide();
	$("#btn_lingqu").show();
	$("#btn_tijiaofenpei").hide();
	$("#btn_fenpei").show();
}

//加载数据
function initData() {
	/*
	var rdata;
	var pat = {};
	pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
	pat.where = {"is_mp3":"txt"};
	commonSelect(pat, false, function (data) {
		if (data && data.rows != null) {
			rdata = data.rows;
		}
	});
	return rdata;
	*/
	var rdata = [];
	var pat = {};
	pat.nameSpace = "com.zinglabs.apps.mybaits_xml_def.ZQC";
	pat.nameSpaceId = "wenBenZuoYeFenPei_initData";
	pat.data_state_parames = "'" + zinglabs.zqc.getState("zero") + "','" + zinglabs.zqc.getState("chujianweifenpei") + "','" + zinglabs.zqc.getState("chujianyifenpei") + "'";
	pat.score_state_parames = "'" + zinglabs.zqc.getState("zero") + "','" + zinglabs.zqc.getState("chujianweipingfen") + "'";
	commonSelectForComplex(pat, false, function (data) {
		if (data) {
			rdata = data.rows;
		}
	});
	return rdata;
}
//刷新
function refresh() {
	var table = $("#tableForData").DataTable();
     
     //清空筛选
	table.search("").draw();
	$("#shaixuan").val("");
     //清空数据
	table.clear().draw();
     //重新加载数据
	var data = searchData();
	if(data!=undefined){
		table.rows.add(data).draw(true);
	}
	
}
//加载指标数据			
function initZhiBiao() {
	var data = getzhibiaoTree();
	var zhibiao = "";
	$.each(data, function (n, d) {
		zhibiao += "<option value=\"" + d.grade_name + "\" >" + d.grade_name + "</option>";
	});
	$("#pingfenzhibiao").append(zhibiao);
	$("#pingfenzhibiao2").append(zhibiao);
}
//领取
function doLingQu() {
	var table = $("#tableForData").DataTable();
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	if (selectData.length == 0 || selectData == null || selectData == undefined) {
		//alert("请选中要领取的数据!");
		zinglabs.i18n.alert("zqc_share_qxzylqdsj");
		return;
	}
	//判断领取时间是否为空
	if (timeCheck("lingQuDiv #begin_time_devoir_DFTB", "lingQuDiv #end_time_devoir_DFTE")) { 
				//判断是否选择了评分指标
		var zhibiao = $("#pingfenzhibiao").val();
		if (zhibiao == undefined || zhibiao == 0) {
					//alert("请选择评分指标！");
			zinglabs.i18n.alert("zqc_share_qxzpfzb");
			return;
		}
				//判断所选择数据是否未经过分配
		if (selectData && selectData != null && selectData != undefined) {
			var si = "id in (";
			$.each(selectData, function (n, d) {
				si += "'" + d.id + "',";
			});
			si = si.substring(0, si.length - 1) + ") and data_state ='" + zinglabs.zqc.getState("chujianyifenpei") + "'";
			var pat = {};
			pat.nameSpace = "com.zinglabs.apps.mybaits_xml_def.ZQC";
			pat.nameSpaceId = "zqc_common_checkwenbendata";
			pat.searchItems = si;
			commonSelectForComplex(pat, false, function (data) {
				if (data && data.rows.length != 0) {
							//alert("所要领取的数据中存在已分配的数据！");
					zinglabs.i18n.alert("zqc_share_czyfpdsj");
					return;
				} else {
							//执行数据状态更新操作
					zinglabs.confirm(zinglabs.i18n.getText("zqc_share_qdlqsxzsj"), function () {
						var b = 0;
						var c = 0;
						$.each(selectData, function (n, d) {
							var pat = d;
							pat.data_state = "" + zinglabs.zqc.getState("chujianyifenpei") + "";
							pat.score_state = "" + zinglabs.zqc.getState("chujianweipingfen") + "";
							pat.owner = getUserInfo().loginName;
							pat.assign_role = getUserInfo().loginName;
							pat.grade_name = zhibiao;
							pat.begin_time_devoir = $("#lingQuDiv #begin_time_devoir_DFTB").val();
							pat.end_time_devoir = $("#lingQuDiv #end_time_devoir_DFTE").val();
							pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
							pat.primaryKey = "id";
							pat.primaryKeyValue = d.id;
							commonUpdate(pat, function (data) {
								var json = eval("(" + data + ")");
								c += 1;
								if (!json.success) {
									b += 1;
								}
								if (b == 0 && c == selectData.length) {
							    			//alert("领取成功！");
									zinglabs.i18n.alert("system_caozuochenggong");
									refresh("tableForData", initData());
								} else {
									if (b != 0 && c != selectData.length) {
							    			//alert("领取异常！");
										zinglabs.i18n.alert("system_caozuoshibai");
									}
								}
							});
						});
					});
				}
			});
		}
	}
}
//取消领取
function quXiaoLingQu() {
	var table = $("#tableForData").DataTable();
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	if (selectData.length == 0 || selectData == null || selectData == undefined) {
		//alert("请选中要取消领取的数据!");
		zinglabs.i18n.alert("zqc_share_qxzyqxlqdsj");
		return;
	}
	//判断所选择数据是否为初检已分配
	if (selectData && selectData != null && selectData != undefined) {
		var c = 0;
		$.each(selectData, function (n, d) {
			if (d.data_state == "" + zinglabs.zqc.getState("zero") + "" || d.data_state == "" + zinglabs.zqc.getState("chujianweifenpei") + "") {
				c += 1;
			}
		});
		if (c != 0) {
			//alert("所要取消领取的数据中存在文本状态初检未分配和0的数据！");
			zinglabs.i18n.alert("zqc_share_qxlqdsjbhf");
			return;
		}
	}
	//判断是否有操作权限
	if (selectData && selectData != null && selectData != undefined) {
		var c = 0;
		$.each(selectData, function (n, d) {
			if (d.owner != getUserInfo().loginName && d.assign_role != getUserInfo().loginName) {
				c += 1;
			}
		});
		if (c != 0) {
			//alert("所要取消领取的数据中存在无权操作的数据，只能取消质检员为当前登录人的数据！");
			zinglabs.i18n.alert("zqc_share_czwqczdsj_zjy");
			return;
		}
	}
	
	//执行数据状态更新操作
	zinglabs.confirm(zinglabs.i18n.getText("zqc_share_qdqxlqsxzsj"), function () {
		var b = 0;
		var c = 0;
		$.each(selectData, function (n, d) {
			var pat = d;
			pat.data_state = "" + zinglabs.zqc.getState("chujianweifenpei") + "";
			pat.score_state = "" + zinglabs.zqc.getState("chujianweipingfen") + "";
			pat.owner = " ";
			pat.grade_name = " ";
			pat.assign_role = " ";
			pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
			pat.primaryKey = "id";
			pat.primaryKeyValue = d.id;
			commonUpdate(pat, function (data) {
				var json = eval("(" + data + ")");
				c += 1;
				if (!json.success) {
					b += 1;
				}
				if (b == 0 && c == selectData.length) {
			    	//alert("取消领取成功！");
					zinglabs.i18n.alert("system_caozuochenggong");
					refresh("tableForData", initData());
				} else {
					if (b != 0 && c != selectData.length) {
			    	//alert("取消领取异常！");
						zinglabs.i18n.alert("system_caozuoshibai");
					}
				}
			});
		});
	});
}
//分配 查询并显示质检员信息列表
function doFenPei() {
	
	//进行批量分配的操作
	var table = $("#tableForData").DataTable();
	//文本作业数据
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	var uselectData = ZQC_GOLBAL_PARAM["zhiJianYuanData"];
	if (selectData == undefined || selectData == null || selectData.length == 0) {
		//zinglabs.confirm("未选中列表中的数据！<br/>是否需要程序自动按平均分配规则分配列表中所有文本数据给选中的质检员？",function(){
			
			
		//});
		//alert("请选中要分配的数据!");
		zinglabs.i18n.alert("zqc_share_qxzyfpdsj");
		return;
	}
	//判断分配时间是否为空
	if (timeCheck("fenPeiDiv #begin_time_devoir_fenpei_DFTB", "fenPeiDiv #end_time_devoir_fenpei_DFTE")) { 
	//判断是否选择了评分指标
	var zhibiao = $("#pingfenzhibiao2").val();
	if (zhibiao == undefined || zhibiao == 0) {
		//alert("请选择评分指标！");
		zinglabs.i18n.alert("zqc_share_qxzpfzb");
		return;
	}
	
	//判断是否选择了质检员
		if (uselectData.length == undefined || uselectData.length == 0) {
		//alert("请选中要分配文本数据给哪些质检员!");
			zinglabs.i18n.alert("zqc_share_fpsjgzjy");
			return;
		}
	//判断所选择数据是否未经过分配
		if (selectData && selectData != null && selectData != undefined) {
			var si = "id in (";
			$.each(selectData, function (n, d) {
				si += "'" + d.id + "',";
			});
			si = si.substring(0, si.length - 1) + ") and data_state ='" + zinglabs.zqc.getState("chujianyifenpei") + "'";
			var pat = {};
			pat.nameSpace = "com.zinglabs.apps.mybaits_xml_def.ZQC";
			pat.nameSpaceId = "zqc_common_checkwenbendata";
			pat.searchItems = si;
			commonSelectForComplex(pat, false, function (data) {
				if (data && data.rows.length != 0) {
				//alert("所要分配的数据中存在已分配的数据！");
					zinglabs.i18n.alert("zqc_share_czyfpdsj_plfp");
					return;
				} else {
				//执行数据状态更新操作
					zinglabs.confirm(zinglabs.i18n.getText("zqc_share_qdfpsxzsj"), function () {
						var dcon = selectData.length;
						var ucon = uselectData.length;
						var updateData = {};
						var tc = 0;
						var dataRow=0;
					//处理选中的数据			
						for (var i = 0; i < dcon; i++) {
							var a=Math.round(dcon/ucon) ;
							if(a==0){
								a=1;
							}
							for (var j = dataRow; j < a; j++) {
								if (selectData[tc] == undefined) {
									break;
								}
								updateData[tc] = selectData[tc];
								updateData[tc].data_state = "" + zinglabs.zqc.getState("chujianyifenpei") + "";
								updateData[tc].score_state = "" + zinglabs.zqc.getState("chujianweipingfen") + "";
								if(i==ucon){
									updateData[tc].owner = uselectData[0].loginName;
								}else{
									updateData[tc].owner = uselectData[i].loginName;
								}
								updateData[tc].assign_role = getUserInfo().loginName;
								updateData[tc].begin_time_devoir = $("#fenPeiDiv #begin_time_devoir_fenpei_DFTB").val();
								updateData[tc].end_time_devoir = $("#fenPeiDiv #end_time_devoir_fenpei_DFTE").val();
								updateData[tc].grade_name = zhibiao;
								tc += 1;
							}
						}
						var b = 0;
						var c = 0;
						$.each(selectData, function (n, d) {
							var pat = d;
							pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
							pat.primaryKey = "id";
							pat.primaryKeyValue = d.id;
							commonUpdate(pat, function (data) {
								var json = eval("(" + data + ")");
								c += 1;
								if (!json.success) {
									b += 1;
								}
								if (b == 0 && c == selectData.length) {
						    	//alert("分配成功！");
									zinglabs.i18n.alert("system_caozuochenggong");
									refresh("tableForData", initData());
								} else {
									if (b != 0 && c != selectData.length) {
						    	//alert("分配异常！");
										zinglabs.i18n.alert("system_caozuoshibai");
									}
								}
							});
						});
					});
				}
			});
		}
	}
}
//取消分配
function quXiaoFenPei() {
	var table = $("#tableForData").DataTable();
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	if (selectData.length == 0 || selectData == null || selectData == undefined) {
		//alert("请选中要取消分配的数据!");
		zinglabs.i18n.alert("zqc_share_qxzyqxfpdsj");
		return;
	}
	//判断是否有操作权限
	if (selectData && selectData != null && selectData != undefined) {
		var c = 0;
		$.each(selectData, function (n, d) {
			if (d.assign_role != getUserInfo().loginName) {
				c += 1;
			}
		});
		if (c != 0) {
			//alert("所要取消分配的数据中存在无权操作的数据，只能取消分配人标识为当前登录人的数据！");
			zinglabs.i18n.alert("zqc_share_czwqczdsj_fprbs");
			return;
		}
	}
	//判断所选择数据是否为初检已分配
	if (selectData && selectData != null && selectData != undefined) {
		var c = 0;
		$.each(selectData, function (n, d) {
			if (d.data_state == "" + zinglabs.zqc.getState("zero") + "" || d.data_state == "" + zinglabs.zqc.getState("chujianweifenpei") + "") {
				c += 1;
			}
		});
		if (c != 0) {
			//alert("所要取消领取的数据中存在文本状态初检未分配和0的数据！");
			zinglabs.i18n.alert("zqc_share_qxfpdsjbhf_fp");
			return;
		}
	}
	//执行数据状态更新操作
	zinglabs.confirm(zinglabs.i18n.getText("zqc_share_qdqxfpsxzsj"), function () {
		var b = 0;
		var c = 0;
		$.each(selectData, function (n, d) {
			var pat = d;
			pat.data_state = "" + zinglabs.zqc.getState("chujianweifenpei") + "";
			pat.score_state = "" + zinglabs.zqc.getState("chujianweipingfen") + "";
			pat.owner = " ";
			pat.grade_name = " ";
			pat.assign_role = " ";
			pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
			pat.primaryKey = "id";
			pat.primaryKeyValue = d.id;
			commonUpdate(pat, function (data) {
				var json = eval("(" + data + ")");
				c += 1;
				if (!json.success) {
					b += 1;
				}
				if (b == 0 && c == selectData.length) {
			    	//alert("取消分配成功！");
					zinglabs.i18n.alert("system_caozuochenggong");
					refresh("tableForData", initData());
				} else {
					if (b != 0 && c != selectData.length) {
			    	//alert("取消分配异常！");
						zinglabs.i18n.alert("system_caozuoshibai");
					}
				}
			});
		});
	});
}
//查询文本作业数据
function searchData() {
	$("#updateFormDiv").slideUp(300);
	var rdata = [];
	var pat = {};
	pat.nameSpace = "com.zinglabs.apps.mybaits_xml_def.ZQC";
	pat.nameSpaceId = "wenBenZuoYeFenPei_search";
	pat.searchItems = searchItem2sql("searchDataForm");
	pat.data_state_parames = "'" + zinglabs.zqc.getState("zero") + "','" + zinglabs.zqc.getState("chujianweifenpei") + "','" + zinglabs.zqc.getState("chujianyifenpei") + "'";
	pat.score_state_parames = "'" + zinglabs.zqc.getState("zero") + "','" + zinglabs.zqc.getState("chujianweipingfen") + "'";
	commonSelectForComplex(pat, false, function (data) {
		if (data) {
			var table = $("#tableForData").DataTable();	
			//清空筛选
			table.search("").draw();
		    //清空数据
			table.clear().draw();
		    //重新加载数据
			var rdata = data.rows;
			table.rows.add(rdata).draw(true);
		}
	});
}
//选择质检员
function choseZhiJianYuan() {
	var params = {title:"\u8d28\u68c0\u5458\u5217\u8868",content: '<iframe src="/ZDesk/zh_cn/ZQC/zhijianyuanliebiao.html" style="border: 0; width: 100%; height: 100%"  frameborder="0"/>', width:"900", height:"570", ok:function () {
		var data = ZQC_GOLBAL_PARAM["zhiJianYuanData"];
		if (data.length == undefined || data.length == 0) {
				//alert("请选中需要分配的质检员信息!");
			zinglabs.i18n.alert("zqc_share_qxzxyfpdzjyxx");
			return false;
		}
		var us = "";
		for (var i = 0; i < data.length; i++) {
			us += data[i].loginName + ",";
		}
		us = us.substring(0, us.length - 1);
		$("#zhiJianYuanList").val(us);
	}, okValue:" \u786e \u5b9a ", cancel:function () {
	}, cancelValue:" \u5173 \u95ed "};
	zinglabs.dialog(params);
}

//开始时间和结束时间不能大于30天。
function timeCheck(bid, eid) {
	var bt = $("#" + bid).val();
	var et = $("#" + eid).val();
	bt = new Date(Date.parse(bt.replace(/-/g, "/")));
	et = new Date(Date.parse(et.replace(/-/g, "/")));
	if ($("#" + bid).val() == "" || $("#" + eid).val() == "") {
		zinglabs.i18n.alert("ZQC_renwukaishishijianbunengweikong");
		return false;
	}
	if (et.getTime() < bt.getTime()) {
		zinglabs.i18n.alert("ZQC_jieshushijianbunengdayukaishihijian");
		return false;
	}
	if ((U_DateDiffer(bt, et) / 24) > 30) {
		zinglabs.i18n.alert("ZQC_renwujieshushijianbunengdayuqishishijian");
		return false;
	} else {
		return true;
	}
}

