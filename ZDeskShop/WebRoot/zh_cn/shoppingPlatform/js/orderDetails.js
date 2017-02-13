var testCurdStatus = "false";
var fullName = "AStudent";
var rowSelTr = false;
var orderStateMax = "02";// 可更最高改状态值
var datas = window.parent.datas || 0;
var goodsData = {};
var privilegePriceOld = 0;// 记录优惠金额
var url = '/' + PRJ_PATH + '/' + ZDesk_ROU + '/OD/';
$(document).ready(function() {
			// alert(orderNumber);
			if (datas == null || datas.length == 0) {
				return;
			}
			for (var key in datas) {
				try {
					info(key, datas[key]);
				} catch (e) {
					// TODO: handle exception
				}
			}
			$.blockUI(maskContent);
			$('#testGrid1').DataTable({
						// 滚动条
						// scrollY: 500,
						// scrollX: false,
						// 延迟加载
						deferRender : true,
						processing : false,
						// 开启服务端分页
						serverSide : true,
						// 服务器端分页ajax请求 可抽离出一个方法
						ajax : doSearchNew,
						pagingType : "full_numbers",
						// pageLength : 10,
						bPaginate : false, // 翻页功能
						bLengthChange : false, // 改变每页显示数据数量
						bFilter : true, // 过滤功能
						bSort : true, // 排序功能
						bInfo : false,// 页脚信息
						bAutoWidth : false, // 自动宽度
						sPaginationType : "bootstrap",// 分页样式
						dom : 'TRlrtip',
						columns : [{
									title : "商品编号",
									data : "goodsNumber",
									width : '38%',
									visible : true
								}, {
									title : "买家所选信息",
									data : "goodsCotegory",
									width : '38%',
									visible : true
								}, {
									title : "商品名称",
									data : "goodsName",
									width : '4%',
									visible : true
								}, {
									title : "商品成本",
									data : "goodsCostPrice",
									width : '4%',
									visible : true
								}, {
									title : "商品单价",
									data : "unitPrice",
									width : '4%',
									visible : true
								}, {
									title : "商品数量",
									data : "numbers",
									width : '4%',
									visible : true
								}, {
									title : "商品总售价",
									data : "price",
									width : '4%',
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
								var index = nodes[0]._DT_RowIndex;
								rowSelTr = index;
								goodsData = table.row(index).data();
								if (orderStateMax > datas["orderState"]) {
									$("#privilegePrice").val("0");// 优惠价格
									upPrice();
									$("#edit").show();
								}
							},
							sRowSelect : 'single',
							aButtons : []
						}
					});

			// 自定义列插件绑定
			// 绑定事件 on
			var table = $('#testGrid1').DataTable();

			/*
			 * table.on( 'order.dt search.dt', function () { table.column(0,
			 * {search:'applied', order:'applied'}).nodes().each( function
			 * (cell, i) { cell.innerHTML = i+1; } ); } ).draw(true);
			 */
			$("#testAdd").show();
			$.unblockUI();

		});
// 打开编辑
/*
 * function openEdit(data) { $("#priceNew").text(data["price"]); privilegePrice =
 * (data["unitPrice"] * data["numbers"]) - data["price"]
 * $("#privilegePriceYet").text(privilegePrice); }
 */
// 更改金额
function upPrice() {
	// 已优惠价格
	var privilegePriceYet = (goodsData["unitPrice"] * goodsData["numbers"])
			- goodsData["price"];
	// 新订单商品价格
	var newPrice = goodsData["price"];
	// 新优惠金额
	var privilegePriceNew = $("#privilegePrice").val() || "0";
	$("#privilegePriceYet").text(privilegePriceYet);

	if ((privilegePriceNew.indexOf(".") > 0)
			&& (privilegePriceNew.indexOf(".") == (privilegePriceNew.length - 1)))
		return;
	// var boolean = isInteger(privilegePriceNew);
	if (isDouble(privilegePriceNew)) {
		newPrice -= privilegePriceNew;
		datas.privilegePrice = -privilegePriceNew;// 优惠价格
		datas.newPrice = newPrice;// 新订单商品价格
		/*
		 * $("#privilegePrice").val(privilegePriceNew == "00" ? "" :
		 * privilegePriceNew); $("#priceNew").text(newPrice);
		 */
	} else {
		// showError("请输入数字整数或负数");
		privilegePriceNew = privilegePriceOld;
	}
	$("#privilegePrice")
			.val(privilegePriceNew == "00" ? "" : privilegePriceNew);
	$("#priceNew").text(newPrice);
	privilegePriceOld = $("#privilegePrice").val() || "0";
}
function doEdit() {
	var privilegePriceNew = $("#privilegePrice").val()
	var bool = (isDouble(privilegePriceNew) && (privilegePriceNew * 1) != 0 && ((($("#priceNew")
			.text()) - 0) > 0));
	if (bool && checkSelect("testGrid1")) {
		$.blockUI(maskContent);
		// var params = {};
		var cValues = dataToData();
		// 主键的值
		// params.columnValues = cValues;
		// cValues.msgReceiver="@cookie_zhi";
		// params.tableName = fullName;
		// 如更改不是id 需定义 params.primarykey
		var tableTmp = $('#testGrid1').DataTable();
		// The selected data DT_RowNumber
		var selectedData = tableTmp.row(rowSelTr).data();
		try {
			var data = "";
			ajaxFunction(url + 'upOrderGoodsPrice.action', cValues, true,
					function fn(dat) {
						var msg = dat.mgs;
						if (dat.success) {
							var table = $('#testGrid1').dataTable();
							goodsData.price = datas["newPrice"];
							upPrice();
							table.fnUpdate(goodsData, rowSelTr, null, false);
							window.parent
									.upOrderPrice(cValues["privilegePrice"]);
							var price = $("#price").text();
							price -= -cValues["privilegePrice"];
							$("#price").text(price);
						}
						showSuccess(msg);
						$.unblockUI();
					});
		} catch (ex) {
			showError("异常！" + ex.name);
			logErr("doEdit 2222 Excep " + ex.name + ": " + ex.message);
			$.unblockUI();
		}
		/*
		 * commonUpdate(params, true, function(data) { try { if (data.success) {
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
		showSuccess("请输入正确优惠价格(商品总金额不可低于0)");
	}
};
function dataToData() {
	var cValues = {
		"privilegePrice" : datas["privilegePrice"],
		"orderStateMax" : orderStateMax,
		"orderId" : datas["id"],
		"price" : datas["newPrice"],
		"orderGoodsId" : goodsData["id"],
		"orderNumber" : datas["orderNumber"],
		"orderGroupNumber" : datas["orderGroupNumber"],
		"goodsId" : goodsData["goodsId"],
		"goodsNumber" : goodsData["goodsNumber"],
		"goodsName" : goodsData["goodsName"],
		"privilegePrice" : datas["privilegePrice"],
		"sellersName" : datas["sellersName"],
		"type" : "变更价格"
	};
	return cValues;
}
/**
 * 查询 select
 */
function doSearchNew(data, callback, settings) {
	var params = {};
	// params.tableName = fullName;
	// params.columnValues.msgId="@cookie_zhi";
	params.orderId = datas["id"];
	params.rows = settings._iDisplayLength;
	params.offset = settings._iDisplayStart;
	$.blockUI(maskContent);
	try {
		ajaxFunction(url + 'getOrderDetails.action', params, true, function(
						data) {
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

function doSearchs() {
	var tableTmp = $("#testGrid").DataTable();
	tableTmp.draw(false);
}

// 刷新
function refresh() {
	// cleanSearchForm();
	doSearchs();
}

/*******************************************************************************
 * 必须处理的严重错误，测试阶段可以alert 生产环境特殊标识写入日志 发现就要分析原因
 * 
 * @param err
 */
function logErr(errMsg) {
	log.debug(errMsg);
	// alert(errMsg);
	// PT().log.error(errMsg);
};

/*******************************************************************************
 * 嵌入ifram可以返回top parent 非嵌入 可以返回 window ...
 * 
 * @returns {Window}
 * @constructor
 */
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

// 清空添加表单
function doCleanAddForm() {
	testCurdStatus = "add";
	var pvars = {
		"id" : "testAdd",
		"isClear" : "true"
	};
	setPanelVal(pvars);
}