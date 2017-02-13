// 遮罩属性
var maskContent = {
	css : {
		border : 'none',
		padding : '15px',
		backgroundColor : '#000',
		'-webkit-border-radius' : '10px',
		'-moz-border-radius' : '10px',
		opacity : .5,
		color : '#fff',
		font : '14px'
	},
	message : '<img src="../../../../img/wait.gif" border="0" />  正在加载中，请稍候...'
};
/*******************************************************************************
 * 验证整数
 ******************************************************************************/

function isDouble(a) {
	
	a-=0;
	// 声明正则表达式
	var re = /^-?\d+\.?\d{0,2}$/;
	// var reg=/^(-|+)?d+(.d+)?$/
	//alert(re.test(a))
	return re.test(a);
}

/**
 * 验证 两串json数据是否相等 否 return ture
 */
function checkDate(data1, data2) {
	var i = false;
	try {
		if (data1 == null && data2 == null) {
			return i;
		}
		if (data1 != null && data1.length == data2.length) {
			for (var key in data2) {
				if (data1[key] != data2[key]) {
					i = true;
				}
			}
		}
		return i;
	} catch (e) {
		return i;
	}
}
/*******************************************************************************
 * 
 * @desc 指定panel赋值 和清空
 * @todo 只测试了主要类型 , checkbox、radio没测试
 * 
 * pVars.id panel id pVars.data 赋值数据 pVars.isClear 字符串 "true" 清空此panel
 * 
 * 
 * @TODO 通用，调试完成后分文件，不要加Velocity代码
 * 
 */
function setPanelVal(pVars) {
	$('#' + pVars.id + ' :input').each(function(index, obj) {
		var type = obj.type;
		var tag = obj.tagName.toLowerCase();
		if (("isClear" in pVars) && pVars.isClear == 'true') {
			if (type == 'text' || type == 'password' || tag == 'textarea'
					|| type == 'hidden')
				$(obj).val('');
			else if (type == 'checkbox' || type == 'radio')
				$(obj).attr('checked', false);
			else if (tag == 'select')
				$(obj).attr('selectedIndex', -1);
		} else {
			var vTmp = pVars.data[this.name];
			if (BIsNullVal(vTmp)) {
				vTmp = '';
			}
			if (type == 'text' || type == 'password' || type == 'hidden'
					|| tag == 'textarea' || tag == 'select')
				$(obj).val(vTmp);
			else if (type == 'checkbox' || type == 'radio') {
				$(obj).attr('checked', vTmp == '' ? false : vTmp);
			} else if (tag == 'select') {
				$(obj).attr('selectedIndex', vTmp == '' ? -1 : vTmp);
			}
		}
	});
}
// 验证是否有选中

/*
 * gain dataTable Pitch On ID return value,value,value,value
 */
function getPitchOnId(table, key) {
	// 注意DataTable大写 或者 $('#promptManager').dataTable().api();
	// 获取选中行 通过dataTable查找
	// var cell=table.rows(".active").data();
	// 通过table TableTools插件 查找
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	var ids = "";
	$.each(selectData, function(index, content) {
				ids += content[key] + ",";
			});
	// alert();
	return ids.substring(0, ids.length - 1);
}

function BIsNullVal(value) {
	return typeof(value) == "undefined"
			|| value == null
			|| (typeof(value) == 'string' && (value == "undefined"
					|| value == '' || value == 'null'))
			|| (typeof(value) == 'boolean' && value == false);
};
/**
 * ajax data= General select data return data json
 */
function doAjax(url, data, fn) {
	// 通用查询action路径
	var xhr = new XMLHttpRequest();
	xhr.open("POST", "*", true);
	xhr.withCredentials = true;
	xhr.send();
	$.ajax({
				async : true,
				type : "post",
				url : url,
				dataType : "json",
				data : data,
				success : function(data) {
					rdata = data;
					if (fn && typeof(fn) == 'function') {
						fn(data);
					}
				},
				error : function(r) {
					zinglabs.alert("error");
				}
			});
	return '';
}

// 显示错误信息
function showError(mgs) {
	// $("#errorDiv").show();
	// $("#successDiv").hide();
	// $("#errorDiv").html(mgs);
	// setTimeout(function(){
	// $("#errorDiv").hide();
	// },3000);
	zinglabs.alert(mgs);

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
// 显示成功信息
function showSuccess(mgs) {
	// $("#errorDiv").hide();
	// $("#successDiv").show();
	// $("#successDiv").html(mgs);
	// setTimeout(function(){
	// $("#successDiv").hide();
	// },3000);
	zinglabs.alert(mgs);
}

// 验证datatable 是否选中数据
function checkSelect(tableId) {
	var tableTmp = $('#' + tableId).DataTable();
	var tt = new $.fn.dataTable.TableTools(tableTmp);
	var selectData = tt.fnGetSelectedData();
	if (selectData.length == 0) {
		return false;
	}
	return true;
}
/** *********************************************************************************** */
function info(id, text, xx) {
	$("#" + id).text(text);
}



