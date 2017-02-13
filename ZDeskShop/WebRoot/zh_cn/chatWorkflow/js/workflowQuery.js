
var row = 0;//每页显示到第几条数据
var offset = 0;//每页从第几条显示数据
var isData = 0;//是否搜索到数据
var page = 0;//当前第几页变量
$(document).ready(function () {

	//当页面滑动或滚动 高度到200 隐藏筛选项
	$(window).scroll(function () {
		var h = $(document).scrollTop();
		if (h > 200) {
			$("#searchItem").slideUp(300);
			$("#hideSearchItem").slideUp(300);
		}
	});
	searchButton();
});



//工作流表Workflow_Table
function doSearch(rows, offset) {
	var searchItem = zkm_getHtmlFormJsons("searchDataForm");
	var params = {};
	var pdata = {};
	params.isPagination = "true";
	params.rows = rows;
	params.offset = offset;
	var columnValues = {};
	params.columnValues = columnValues;
	params.columnValues.workflow_name = "%" + searchItem.workflow_name + "%";
	params.columnValues.workflow_sort = "%" + searchItem.workflow_sort + "%";
	params.columnValues.workflow_state = "%" + searchItem.workflow_state + "%";
	params.like = "workflow_name,workflow_sort,workflow_state";
	if (searchItem.user_name != null && searchItem.user_name != "" && searchItem.user_name != undefined) {
		var idList = getWorkflow_id(searchItem.user_name);//获取用户名对应的Id
		pdata = getIdData(idList, rows, offset);						//获取id对应的数据
	} else {
		params.tableName = "Workflow_Table";
		commonSelect(params, false, function (data) {
			if (data && data.data) {
				pdata = data;
				if (pdata.total > 10) {
					var b = $("#loading");
					b.remove();
					$("#house-loadmore").append('<span id="loading">点击加载更多</span>');
				}
			}
		});
	}
	return pdata;
}

//根据登录名获取对应的工作流编号
function getWorkflow_id(name) {
	var id = "(";
	var data = doSearch_Workflow_Relation_Table(name);//根据用户名查询工作流关联表
	for (var i = 0; i < data.length; i++) {
		id += data[i].workflow_id + ",";
	}
	id += ")";
	var idlist = id.substring(0, id.lastIndexOf(",")) + ")";
	return idlist;
}

//根据id获得对应的数据
function getIdData(idlist, rows, offset) {
	var searchItem = zkm_getHtmlFormJsons("searchDataForm");
	var rdata = [];
	var pat = {};
	pat.isPagination = "true";
	pat.rows = rows;
	pat.offset = offset;
	pat.beanId = "commonFreeFilter";
	pat.nameSpace = "com.zinglabs.apps.chatWorkflow.defXml.anYongHuMingChaXun";
	pat.workflow_name =searchItem.workflow_name;
	pat.workflow_sort =searchItem.workflow_sort;
	pat.workflow_state =searchItem.workflow_state;
	pat.idlist = idlist;
	commonSelect(pat, false, function (data) {
		if (data && data.data) {
			rdata = data;
		}
	});
	return rdata;
} 


//根据用户名查询工作流关联表
function doSearch_Workflow_Relation_Table(name) {
	var params = {};
	var pdata = {};
	var columnValues = {};
	params.isPagination = "false";
	params.columnValues = columnValues;
	if (name != null && name != "" && name != undefined) {
		params.columnValues.user_name = "%" + name + "%";
		params.like = "user_name";
	}
	params.tableName = "Workflow_Relation_Table";
	commonSelect(params, false, function (data) {
		if (data && data.data) {
			pdata = data.data;
		}
	});
	return pdata;
}
function loadTable(data) {
	for (var i = 0; i < data.length; i++) {
		var workflow_name = "";
		var woow_chat_id = data[i].woow_chat_id;
		var path = "/talk.htm?woowId=" + woow_chat_id + "&v=9999.9999";
		if (data[i].workflow_name.length > 10) {
			workflow_name = data[i].workflow_name.substring(0, 10) + "...";
		} else {
			workflow_name = data[i].workflow_name;
		}
		$("#info").append("<a href=" + path + " style='color:#484848;text-decoration:none;'>" + "<div>" + "<span>" + workflow_name + "</span>" + "<ul style='font-size:12px;margin:5px'>" + "<li>分类：" + data[i].workflow_sort + "<span style='float: right;'>" + data[i].workflow_state + "</span></li>" + "<li>创建时间：" + data[i].create_time + "</li>" + "</ul>" + "<hr/>" + "</div>" + "</a>");
	}
}

//分页显示 当前页  总页数。。。
function flitGIlarts(data, row, offset) {
	var totalpage = 0;
	page = page + 1;
	var totalrow = data.total;
	if (totalrow == "" || totalrow == null || totalrow == undefined) {
		if (data.data[0] == "" || data.data[0] == null || data.data[0] == undefined) {
			totalrow = 0;
		} else {
			totalrow = data.data[0].total;
		}
	}
	if (totalrow % (row - offset) > 0) {
		totalpage = parseInt((totalrow / (row - offset)) + 1);
	} else {
		totalpage = totalrow / (row - offset);
	}
	$("#total").html(totalrow);
	if (row >= totalrow) {
		$("#row").html(totalrow);
		var b = $("#loading");
		b.remove();
		$("#house-loadmore").append('<span id="loading">没有更多加载项</span>');
	} else {
		$("#row").html(row);
	}
}

function searchButton() {
	page = 0;//初始第一页
	$("#info").html("");
	var data = doSearch(10, 0);
	if (data.data.length > 0) {
		isData = 1;
	}
	loadTable(data.data);
	row = 10;//每页显示到第几条数据
	offset = 0;//每页从第几条显示数据
	flitGIlarts(data, row, offset);
	$("#order-select").mousemove(function () {
		$("#order-select").css("opacity", "0.7");
	});
	$("#order-select").mouseleave(function () {
		$("#order-select").css("opacity", "1");
	});
	$("#hideSearchItem").mousemove(function () {
		$("#hideSearchItem").css("opacity", "0.7");
	});
	$("#hideSearchItem").mouseleave(function () {
		$("#hideSearchItem").css("opacity", "1");
	});
	$("#hideSearchItem").click(function () {
		$("#searchItem").slideToggle(300);
	});
	$("#order-select").click(function () {
		useLayMaskModels();
		$("#searchItem").slideToggle(300);
		$("#hideSearchItem").show(300);
	});
	$("#hideSearchItem").click(function () {
		page = 0;//初始第一页
		$("#info").html("");
		var data = doSearch(10, 0);
		if (data.data.length > 0) {
			isData = 1;
		}
		loadTable(data.data);
		row = 10;//每页显示到第几条数据
		offset = 0;//每页从第几条显示数据
		flitGIlarts(data, row, offset);
	});
	$("#house-loadmore").click(function () {
		if (isData != 0) {
			row = row + 10;
			offset = offset + 10;
			var data = doSearch(row, offset);
			var length = data.length;
			if (length != 0) {
				loadTable(data.data);
				flitGIlarts(data, row, offset);
			}
		}
	});
}

//点击遮挡层时，摧毁遮挡层
function clickUseLayMaskModels(obj_Mask) {
	var obj_Mask = $("#divMask");
	obj_Mask.remove();
	$("#hideSearchItem").slideUp(1);
	$("#searchItem").slideUp(300);
}

//遮挡层
function useLayMaskModels() {
	var obj_Mask = $("#divMask");
	if (obj_Mask.length != 0) {
		var state = obj_Mask.css("display");
		obj_Mask.show();
	} else {
		$(document.body).append("<div id='divMask' style='opacity:0;left:0px;top:340px;width:100%;height:100%;background-color:pink;z-index=-1;position:absolute;' onclick='clickUseLayMaskModels()'></div>");
	}
}

