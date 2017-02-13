var testCurdStatus = "false";
var fullName = "";
var datas = {};
var price = "";
var rowSelTr = false;
var companyID = '';// 部门ID
var selectData = {};
var url = '/' + PRJ_PATH + '/' + ZDesk_ROU + '/OD/';
$(document).ready(function() {
			$.blockUI(maskContent);
			// 初始化 状态下拉列表
			var s = {};
			s[0] = 'orderState';
			s[1] = 'orderState1';
			initialize('tb_dic_item', 'keyValue', 'keyCode', s);
			$('#testGrid').DataTable({
						// 滚动条
						// scrollY: 500,
						// scrollX: false,
						// 延迟加载
						deferRender : true,
						processing : false,
						// 开启服务端分页
						serverSide : true,
						// 服务器端分页ajax请求 可抽离出一个方法
						ajax : doSearch,
						pagingType : "full_numbers",
						// pageLength : 10,
						bPaginate : true, // 翻页功能
						bLengthChange : true, // 改变每页显示数据数量
						bFilter : true, // 过滤功能
						bSort : true, // 排序功能
						bInfo : true,// 页脚信息
						bAutoWidth : false, // 自动宽度
						sPaginationType : "bootstrap",// 分页样式
						dom : 'TRlrtip',
						columns : [{
									title : "订单编号",
									data : "orderNumber",
									defaultContent : '',
									width : '10%',
									orderable : true
								}, {
									title : "订单状态",
									data : "keyValue",
									width : '6%',
									visible : true
								}, {
									title : "订单总金额",
									data : "price",
									width : '6%',
									visible : true
								}, {
									title : "下单人姓名",
									data : "buyersName",
									width : '6%',
									visible : true
								}, {
									title : "下单人电话",
									data : "buyersPhone",
									width : '6%',
									visible : true
								}, {
									title : "下单人留言",
									data : "buyersMessage",
									width : '6%',
									visible : true
								}, /*
									 * { title : "订单总金额", data : "price", width :
									 * '6%', visible : true }, { title : "商品数量",
									 * data : "goodsNumber", width : '6%',
									 * visible : true }, { title : "商品ID", data :
									 * "goodsId", width : '6%', visible : true }, {
									 * title : "商品名称", data : "goodsName", width :
									 * '6%', visible : true }, { title : "商品标题",
									 * data : "goodsTitle", width : '6%',
									 * visible : true },
									 */{
									title : "公司ID",
									data : "companyID",
									width : '6%',
									visible : false
								}, {
									title : "下单时间",
									data : "orderTime",
									width : '6%',
									visible : true
								}],
						columnDefs : [{
									"searchable" : false,
									"orderable" : false,
									"targets" : 0
								}],
						oLanguage : {
							infoFiltered : function() {

							},
							"sLengthMenu" : "每页显示 _MENU_条",
							"sZeroRecords" : "没有找到符合条件的数据",
							"sProcessing" : "&lt;imgsrc=’./loading.gif’ /&gt;",
							"sInfo" : "当前第 _START_ - _END_ 条　共计 _TOTAL_ 条",
							"sInfoEmpty" : "没有记录",
							"sInfoFiltered" : "(从 _MAX_ 条记录中过滤)",
							"sSearch" : "搜索：",
							"oPaginate" : {
								"sFirst" : "首页",
								"sPrevious" : "前一页",
								"sNext" : "后一页",
								"sLast" : "尾页"
							}
						},
						tableTools : {
							fnRowSelected : function(nodes) {
								doCleanAddForm();
								testCurdStatus = "false";
								var index = nodes[0]._DT_RowIndex;
								rowSelTr = index;
								datas = table.row(rowSelTr).data();
								// checkSelect('testGrid');
								$("#testEdit").hide();
								// openEdit(data);

							},
							sRowSelect : 'single',
							aButtons : []
						}
					});

			// 自定义列插件绑定
			// 绑定事件 on
			var table = $('#testGrid').DataTable();

			/*
			 * table.on( 'order.dt search.dt', function () { table.column(0,
			 * {search:'applied', order:'applied'}).nodes().each( function
			 * (cell, i) { cell.innerHTML = i+1; } ); } ).draw(true);
			 */
			initValidate("testAddForm");
			initValidate("test");
			$.unblockUI();
		});
/**
 * 加载数据字典 表名,test字段名,value字段名,selectId
 */
function initialize(tableName, textName, valueName, selectId) {
	var params = {};
	params.tableName = tableName;
	params.columns = textName + "," + valueName;
	params.orderBy = valueName;
	ajaxFunction(url+"getOrderStateSelect.action",params, true, function(data) {
				selectData = data.data;
				for (var key in selectId) {
					comTypes(selectId[key], data.data, textName, valueName, '0');
				}
			});
}
/**
 * 准备下拉列表
 */
function comTypes(selectid, data, textName, valueName, progress) {
	// var data=getDictListfoIndexData(selectid);
	// $("#" + "" + selectid + "").empty();
	for (var i in data) {
		var str = "<option value='" + data[i][valueName] + "' ";
		if (progress != null && data[i][valueName] < progress) {
			str += " disabled='' ";
		}
		$("#" + "" + selectid + "").append(str + ">" + data[i][textName]
				+ "</option>");
	}
}
/**
 * 查询 select
 */
function doSearch(data, callback, settings) {
	var params = spellSelectParams("searchDataForm");
	// params.tableName = fullName;
	// params.columnValues.msgId="@cookie_zhi";
	var datas = params.columnValues;
	datas.rows = settings._iDisplayLength;
	datas.offset = settings._iDisplayStart;
	$.blockUI(maskContent);
	try {
		ajaxFunction(url + 'getOrderList.action', datas, true, function(data) {
					var pdata = {};
					//
					pdata.data = data.data.data;
					// 总条数
					pdata.recordsTotal = data.data.lengths || 0;
					// 过滤没有使用到总条数
					pdata.recordsFiltered = data.data.lengths || 0;
					// 代表第几次画页面 如pdata.draw 不能删 如删掉不会翻页
					pdata.draw = settings.iDraw;
					callback(pdata);
				});

	} catch (ex) {
		logErr("查询失败 failed " + ex.name + ": " + ex.message);
	}
	$.unblockUI();
	/*
	 * commonSelect(params, true, function(data) { try { if (data && data.data) {
	 * var pdata = {}; var size = data.data.length; for (var i = 0; i < size;
	 * i++) { data.data[i].DT_RowNumber = i + 1; } // 数据 pdata.data = data.data; //
	 * 总条数 pdata.recordsTotal = data.total || 0; // 过滤没有使用到总条数
	 * pdata.recordsFiltered = data.total || 0; // 代表第几次画页面 如pdata.draw 不能删
	 * 如删掉不会翻页 pdata.draw = settings.iDraw; callback(pdata); } } catch (ex) {
	 * logErr("查询失败 failed " + ex.name + ": " + ex.message); } $.unblockUI();
	 * });
	 */
};

/**
 * 退单
 */
function delOrder() {

	if (checkSelect("testGrid")) {
		if (datas['orderState'] == '06') {
			showError("订单已关闭");
			return;
		}
		$.blockUI(maskContent);
		var params = {};
		var data = {
			'orderNumber' : datas['orderNumber'],
			'tableName' : 'tb_goodsRecord',
			'orderState' : datas['orderState'],
			'orderId' : datas['id'],
			'primarKey' : 'id',
			'dbid' : 'ZKM',
			'nameSpace' : DeleteSetting.nameSpace
		};
		// params.tableName = fullName;
		// params.columnValues.msgId="@cookie_zhi";
		/*
		 * params.orderNumber = datas["orderNumber"]; data.data=params;
		 * data.tableName="tb_goodsRecord"; data.primarKey="id"; data.dbid="ZKM"
		 */
		try {
			ajaxFunction(url + 'delOrder.action', data, true, function(d) {
						showSuccess(d.mgs);
						if (d.mgs == '操作成功') {
							datas.orderState = d.data.orderState;
							datas.keyValue = '交易关闭';
							var table = $('#testGrid').dataTable();
							table.fnUpdate(datas, rowSelTr, null, false);
						}
						$.unblockUI();
					});

		} catch (ex) {
			$.unblockUI();
			logErr("查询失败 failed " + ex.name + ": " + ex.message);
		}

	} else {
		showSuccess("请选中数据");
	}

	/*
	 * commonSelect(params, true, function(data) { try { if (data && data.data) {
	 * var pdata = {}; var size = data.data.length; for (var i = 0; i < size;
	 * i++) { data.data[i].DT_RowNumber = i + 1; } // 数据 pdata.data = data.data; //
	 * 总条数 pdata.recordsTotal = data.total || 0; // 过滤没有使用到总条数
	 * pdata.recordsFiltered = data.total || 0; // 代表第几次画页面 如pdata.draw 不能删
	 * 如删掉不会翻页 pdata.draw = settings.iDraw; callback(pdata); } } catch (ex) {
	 * logErr("查询失败 failed " + ex.name + ": " + ex.message); } $.unblockUI();
	 * });
	 */
};

/*******************************************************************************
 * @TODO 通用，调试完成后分文件，不要加Velocity代码
 */

function doEdit() {

	var bool = $("#test").validate().form();
	if (bool) {
		$.blockUI(maskContent);
		// var params = {};
		var cValues = zkm_getHtmlFormJsons("test");
		// 主键的值
		// params.columnValues = cValues;
		// cValues.msgReceiver="@cookie_zhi";
		// params.tableName = fullName;
		// 如更改不是id 需定义 params.primarykey
		var tableTmp = $('#testGrid').DataTable();
		// The selected data DT_RowNumber
		var selectedData = tableTmp.row(rowSelTr).data();
		if (checkDate(selectedData, cValues)) {
			try {
				var data = "";
				ajaxFunction(url + 'updateOrderState.action', cValues, true,
						function fn(datas) {
							data = datas;
							if (data != null && data != 0) {
								var table = $('#testGrid').dataTable();
								table.fnUpdate(cValues, rowSelTr, null, false);
								showSuccess("修改成功!");
							} else {
								showError("修改失败");
							}
						});
			} catch (ex) {
				showError("异常！" + ex.name);
				logErr("doEdit 2222 Excep " + ex.name + ": " + ex.message);
			}
			/*
			 * commonUpdate(params, true, function(data) { try { if
			 * (data.success) {
			 * 
			 * var tableTmp = $('#testGrid').dataTable();
			 * tableTmp.fnUpdate(params.caolumnValues, rowSelTr);
			 * showSuccess("修改成功!"); } catch (e) { showError("异常！");
			 * logErr("Exception need see log " + e.name + ": " + e.message); } }
			 * else { logErr("Exception edit failed " + data + " " + ("Items" in
			 * data)); showError("修改失败"); } } catch (ex) { showError("异常！");
			 * logErr("doEdit 2222 Excep " + ex.name + ": " + ex.message); }
			 * 
			 * });
			 */
		} else {
			showError("修改失败 数据无变化 或未选中");
		}
		$.unblockUI();
		return false;
	}

	return "edit";

};
//给子页面调用
function upOrderPrice(price) {
	var table = $('#testGrid').dataTable();
	datas.price-=-price;
	table.fnUpdate(datas, rowSelTr, null, false);
}
function doSearchs() {
	var tableTmp = $("#testGrid").DataTable();
	tableTmp.draw(false);
}

// 每页显示条数
function setPageNum(obj) {
	table.page.len(obj.value).draw();
}
// 刷新
function refresh() {
	// cleanSearchForm();
	doSearchs();
}

function PT() {
	return window;
};
// 初始化验证组件
// fromid 表单ID
function initValidate(fromid) {
	$("#" + fromid).validate({
				// 错误信息处理方法
				errorPlacement : function(error, element) {
					// if(element.)
					element.addClass('error');
					var errorInfo = error.html();
					element.tooltip({
								trigger : "manual",
								title : errorInfo,
								placement : "left"
							});
					element.tooltip("show");
				},
				// 成功信息处理方法
				success : function($e, dom) {
					var element = $(dom);
					element.tooltip('hide');
					element.removeClass('error');

				}
			});
}

// 表单
function openAdd() {
	$("#testEdit").hide();
	doCleanAddForm();

}

// 打开提交表单
function openEdit() {
	if (checkSelect("testGrid")) {
		$("#orderState1").empty();
		comTypes('orderState1', selectData, 'keyValue', 'keyCode',
				datas['orderState']);
		$("#testEdit").show();
		// $("#testAdd").hide();
		testCurdStatus = "edit";
		var pvars = {
			"id" : "testEdit",
			"data" : datas
		};
		setPanelVal(pvars);

	} else {
		showSuccess("请选中数据");
	}
}

// 提交方法
function doSubmit() {
	try {

		if (testCurdStatus == "add") {
			doAdd();
		} else if (testCurdStatus == "edit") {
			doEdit();
		}

	} catch (e) {
		// 编辑、增加页面状态清理。一定要确保页面数据已经不再使用。
		zinglabs.alert("出现异常");
		testCurdStatus = "false";
		logErr("Exception BtnAddSubmit need see log " + e.name + ": "
				+ e.message);

	}

}

// 清空添加表单
function doCleanAddForm() {
	testCurdStatus = "add";
	var pvars = {
		"id" : "testAdd",
		"isClear" : "true"
	};
	setPanelVal(pvars);
}

function cleanSearchForm() {
	$("#searchDataForm")[0].reset();
}

// 详细信息
function manageCategory() {
	var tableTmp = $('#testGrid').DataTable();
	orderNumber = getPitchOnId(tableTmp, "orderNumber");
	if (orderNumber.length < 1) {
		showSuccess("至少选中一条数据");
	} else {
		var url = "orderDetails.html";
		$.blockUI(maskContent);
		closeDialog = orgDialog(url, "订单详情");
		closeBlock();
	}
}
function closeBlock() {
	$(".ui-dialog-close").click(function() {
				$.unblockUI();
			});
	// esc 键触发
	$(document).keyup(function(event) {
				switch (event.keyCode) {
					case 27 :
						$.unblockUI();
				}
			});
}

function orgDialog(url, table) {
	var orgDig = dialog({
		title : table,
		content : '<iframe src='
				+ url
				+ ' style="border: 0; width:900px; height: 500px"  frameborder="0"/>',
		width : '900px',
		height : '500px'
	});
	orgDig.show();
	return orgDig;
}

// 关闭dialog
function closeDig() {
	window.closeDialog.close().remove();
}
/**
 * 当定当状态变更时 更改keyValue的value值
 */
function updateKeyValue() {
	var keyValue = $('#orderState1').find('option:selected').text();
	$('#keyValue').val(keyValue);
}