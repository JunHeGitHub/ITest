
/*******工单存放相关函数*******/

//全局变量
var nowOrderId = "";	//当前工单编号


/***级联效果方法***/
function jlShow(nowsel) {
	//获取当前选中下拉值
	var nowselval = $(nowsel).val();
	//数据库查询：根据父级编码查询与它绑定的子项
	var pat = {};
	var rdata = [];
	pat.isPagination = false;
	pat.tableName = "DicData";
	pat.equal = "parentIndexCode";
	pat.columnValues = {"parentIndexCode":nowselval};
	commonSelect(pat, false, function (data) {
		if (data && data.data) {
			rdata = data.data;
		}
	});
			    //清空子项内容
	$("#BessActivitySon").empty();
				//循环增加下拉项  ps:为了显示，调用通过code查name方法进行转换
	for (var i in rdata) {
		$("#BessActivitySon").append("<option value='" + rdata[i].code + "'>" + getDictValueForCode(rdata[i].code) + "</option>");
	}
}


/***表单验证方法***/
var selects;  //下拉集合:必选项
var checkRes = false; //验证标识

function checkForm(nowsel) {
//判断是提交还是普通失去焦点事件
	if (nowsel == "\u63d0\u4ea4") {
		var inputs = $("span").parent().next().find("input:visible"); //获取显示必选项
//循环判断是否为初始值，给予提示
		$(selects).each(function () {
			if ($(this).val() == "-----") {
				showTooltip($(this).attr("id"), "\u8bf7\u9009\u62e9\u5bf9\u5e94\u4e0b\u62c9\u5185\u5bb9");   //悬浮图表化--方法下边
				checkRes = false;
			} else {
				hideTooltip($(this).attr("id"));
				checkRes = true;
			}
		});

//循环判断是否为空，给予提示
		$(inputs).each(function () {
			if ($(this).val() == "" || $(this).val() == null || $(this).val() == undefined) {
//截取对应输入文字,方便指定提示内容
				var alertText_All = $(this).parent().prev().text().trim();
				alertText_All = alertText_All.substring(1, alertText_All.length - 1) + "\u4e0d\u53ef\u4e3a\u7a7a";
				showTooltip($(this).attr("id"), alertText_All);
				checkRes = false;
			} else {
				hideTooltip($(this).attr("id"));
				checkRes = true;
			}
		});
	} else {
		if ($(nowsel).val() == "" || $(nowsel).val() == null || $(nowsel).val() == undefined) {
			var alertText = $(nowsel).parent().prev().text().trim();
			alertText = alertText.substring(1, alertText.length - 1) + "\u4e0d\u53ef\u4e3a\u7a7a";
			showTooltip($(nowsel).attr("id"), alertText);
			checkRes = false;
		} else {
			hideTooltip($(nowsel).attr("id"));
			checkRes = true;
		}
	}
}

//表单验证 提示样式
function showTooltip(id, title) {
	$("#" + id).attr("title", title);
	$("#" + id).tooltip({placement:"top"});
	$("#" + id).tooltip("show");
	$("#" + id).addClass("error");
}

//还原样式
function hideTooltip(id) {
	$("#" + id).tooltip("destroy");
	$("#" + id).removeClass("error");
}


//工单类型-显示隐藏
function openSelect(nowsel) {
	$("#tscld").hide();
	$("#ywxcd").hide();
	$("#wtzxd").hide();
	$("#jybyd").hide();
	$("#ggywd").hide();
	$("#zyzkd").hide();
	$("#fwyyd").hide();
	var selval = $(nowsel).val();
	$("#" + selval).show();
}

 //数据初始化,时间制定格式拼装并存放工单
function valOnload() {
	selects = $("span").parent().next().find("select");  //必选项下拉菜单集合赋对应集合
	$(".datetimepicker").datetimepicker({format:"yyyy-mm-dd hh:ii", minuteStep:1}); //绑定时间控件
	$("select").each(function () {
		var data = getDictListfoIndexData($(this).attr("name"));
		$("#" + $(this).attr("name")).empty();
		var lgh = $("select[name=" + $(this).attr("name") + "]").find("option").length;
		for (var j = 0; j < selects.length; j++) {
			if ($(selects).eq(j).attr("name") == $(this).attr("name")) {
				if ($(this).attr("name") != "orderType" && $(this).attr("name") != "belongBankType") {
					$("#" + $(this).attr("id")).append("<option value='-----'>-----</option>");
				}
			}
		}
		for (var i in data) {
			if (data.length >= lgh) {
				if ($(this).attr("name") != "BessActivitySon") {
					$("select[name=" + $(this).attr("name") + "]").append("<option value='" + data[i].code + "'>" + data[i].name + "</option>");
				}
			}
		}
	});
		
//四位随机数生成
	var sjvalue = "";
	for (i = 1; i <= 5; i++) {
		sjvalue += parseInt(10 * Math.random());
	}
	
//当前时间对应
	var now = new Date();
	var callInNum = sjvalue + sjvalue;
	var year = now.getFullYear();
	var month = now.getMonth() + 1;
	var date = now.getDate();
	var hour = now.getHours();
	var minutes = now.getMinutes(); 
	
	
	//初始化数据
	var orderId = year + "" + month + "" + date + "" + sjvalue;
	var callTimeFm = year + "-" + month + "-" + date + " " + hour + ":" + minutes;
	$("#callInNum").val(callInNum);
	$("#workOrderId").val(orderId);
	$("#workOrderState").val("\u65b0\u5efa\u5de5\u5355");
	$("#customerCallTime").val(callTimeFm);
	$("#creator").val(getUserInfo().name);
	$("#createTime").val(callTimeFm);
	//当前工单ID赋值
	nowOrderId = orderId;
	
	    
	 //存放到数据库
	var param = zkm_getHtmlFormJsons("orderForm");
	   	//alert(param.managerRole=getUserInfo().pwd);
	delete param.undefined;
	var pat = {};
	pat.tableName = "BOC_workOrderMem";
	pat.primarykeyType = "uuid";
	pat.columnValues = param;
	commonInsertOrUpdate(pat, false, function (data) {
		if (data.success == false) {
			zinglabs.alert("system_saveFailed");
			return;
		}
	});
}



  //保存工单
function saveOrder() {
	checkForm("\u63d0\u4ea4");
	if (checkRes) {
		var loadi = layer.load("\u4fdd\u5b58\u4e2d...");
		var param = zkm_getHtmlFormJsons("orderForm");
	    //用于存放工单类型及对应子项内容
		var gdtypeval = $("#gdtype").val();
		var orderType = zkm_getHtmlFormJsons("Form" + gdtypeval);
		for (var key in orderType) {
			param[key] = orderType[key];
		}
		delete param.undefined;
		var pat = {};
		pat.tableName = "BOC_workOrderMem";
		pat.primarykey = "workOrderId";
		pat.columnValues = param;
		commonUpdate(pat, false, function (data) {
			if (data.success) {
				layer.close(loadi);
				zinglabs.i18n.alert("system_saveSuccess");
				return;
			} else {
				layer.close(loadi);
				zinglabs.i18n.alert("system_saveFailed");
				return;
			}
		});
		layer.close(loadi);
	}
}



//提交工单
function submitAndAudit() {
	checkForm("\u63d0\u4ea4");
	if (checkRes) {
		var loadi = layer.load("\u63d0\u4ea4\u4e2d...");
		var date = new Date();
		var param = zkm_getHtmlFormJsons("orderForm");
	   //用于存放工单类型及对应子项内容
		var gdtypeval = $("#orderType").val();
		var orderType = zkm_getHtmlFormJsons("Form" + gdtypeval);
		for (var key in orderType) {
			param[key] = orderType[key];
		}
		param.workOrderState = "\u590d\u6838\u4e2d";
		delete param.undefined;
		var pat = {};
		pat.tableName = "BOC_workOrderMem";
		pat.primarykey = "workOrderId";
		pat.columnValues = param;
		commonUpdate(pat, false, function (data) {
			if (data.success) {
				var nowid = parent.$("iframe").parents("div").attr("id");
				var index = nowid.indexOf("_");
				nowid = nowid.substring(0, index);
				parent.com_bootStarpTab_removeTab({"id":nowid, "mode":"remove"});
				parent.com_bootStarpTab_removeTab_def(nowid, "remove");
				layer.close(loadi);
				return;
			} else {
				layer.close(loadi);
				zinglabs.i18n.alert("system_replyFailed");
				return;
			}
		});
		layer.close(loadi);
	}
}
/******相关服务请求关联数据函数******/


/***查询当前工单关联工单数据***/
var ReOrderList = [];	//关联工单对象集合
function RelationOrderList() {
	var OrderId = nowOrderId;
	var ReOrderIdList = [];  //关联工单ID集合
	ReOrderList = [];  //清空
	pat = {};
	pat.tableName = "BOC_workOrderRelationMem";
	pat.equal = "workOrderId";
	pat.columnValues = {"workOrderId":OrderId};
	pat.columns = "RT_workOrderId";
	commonSelect(pat, false, function (data) {
		if (data.success == false) {
			alert("error");
		} else {
			ReOrderIdList = data.data;
		}
	});
	//根据ID查询对应内容
	for (var i in ReOrderIdList) {
		ReOrderList.push(doSearchByOrderId(ReOrderIdList[i].RT_workOrderId));
	}
	//数据转换:根据cord查询value(文本值)。
	for (var i = 0; i < ReOrderList.length; i++) {
		for (var key in ReOrderList[i]) {
			var NowVal = getDictValueForCode(ReOrderList[i][key]);
			if (NowVal.length == 0) {
				NowVal = null;
			}
			if (NowVal != null) {
				ReOrderList[i][key] = NowVal;
			}
		}
	}
	return ReOrderList;
}


/***新增关联工单***/
function AddRelation() {
	var OrderId = nowOrderId;
	var ReOrderId = $("#dataRelation").val();
	var RelationState=true;    //关联状态
	
	//根据对应ID查询工单是否存在
	var ReOrder =doSearchByOrderId(ReOrderId);
	if(ReOrder==null){
		zinglabs.i18n.alert("OrderRelation_OrderNotFind");
	RelationState=false;
	}
	 //根据对应ID查询工单是否关联
	var pat = {};
	pat.tableName = "BOC_workOrderRelationMem";
	pat.equal = "workOrderId,RT_workOrderId";
	pat.columnValues = {"workOrderId":OrderId, "RT_workOrderId":ReOrderId};
	pat.columns = "id";
	commonSelect(pat, false, function (data) {
			 if(data.data.length!=0){
			RelationState=false;
			zinglabs.i18n.alert("OrderRelation_AlreadyRelation");
			}
	});
    if(RelationState){
	var pati = {};
	pati.tableName = "BOC_workOrderRelationMem";
	pati.primarykeyType = "uuid";
	pati.columnValues = {"workOrderId":OrderId, "RT_workOrderId":ReOrderId};
	commonInsertOrUpdate(pati, true, function (data) {
		if (!data.success) {
			alert("error");
		} else {
			refreshReList();
		}
	});
  }
}


/***关联列表刷新***/
function refreshReList() {
	var table = $("#orderF").DataTable();
	table.clear().draw();
	table.rows.add(RelationOrderList()).draw(true);
}


/***根据工单编号查询数据***/
function doSearchByOrderId(OrderId) {
	var oneOrder = {};
	var pat = {};
	var rdata = [];
	pat.isPagination = false;
	pat.tableName = "BOC_workOrderMem";
	pat.equal = "workOrderId";
	pat.columnValues = {"workOrderId":OrderId};
	commonSelect(pat, false, function (data) {
		if (data && data.data) {
			oneOrder = data.data[0];
		}else{
		alert("error");
		oneOrder=null;
		}
	});
	return oneOrder;
}


/***删除关联工单记录***/
function DelReOrder(ReOrderId) {
    //根据对应ID查询工单关联唯一ID标识
	var OrderId = nowOrderId;
	var SelOrderid = {};
	var pat = {};
	pat.isPagination = false;
	pat.tableName = "BOC_workOrderRelationMem";
	pat.equal = "workOrderId,RT_workOrderId";
	pat.columnValues = {"workOrderId":OrderId, "RT_workOrderId":ReOrderId};
	pat.columns = "id";
	commonSelect(pat, false, function (data) {
		if (data && data.data) {
			SelOrderid = data.data[0];
		}
	});
	
	
//根据唯一标识删除对应关联
	var patd = {};
	patd.tableName = "BOC_workOrderRelationMem";
	patd.primarykey = "id";
	patd.columnValues = SelOrderid.id;
	commonDelete(patd, false, function (data) {
		if (data.success) {
			refreshReList();
		} else {
			alert("error");
		}
	});
}


/****数据初始化加载****/
window.onload = function () {
	valOnload();
	var inputs = $("span").parent().next().find("input");
	$(inputs).blur(function () {
		checkForm(this);
	});
};

