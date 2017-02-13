/* 操作收货地址 */
// 收货地址数据集
var addRessData = [];
// 被选中的收货地址 id
var xzId = "";
// 最大id 新增搜收货址的临时id
var maxId = 0;
function showASD() {
	popShade(1);
	if (addRessData.length == 0) {
		selectASD()
	}
	$(".upAddress").fadeIn();
}

function closeASD() {
	switch ($(".upAddressO").text().trim()) {
		case "保存" :
			$(".upAddressO").text("确定");
			$("#ADSEidt").fadeOut();
			$("#ADSList").fadeIn();
			$("#ADSEtTitl").html("");
			break;
		case "确定" :
			popShade(-1);
			$("#ADSEtTitl").html("");
			$(".upAddress").fadeOut();
			break;
	}
	$(".tjImg").fadeIn();
}

function selectASD() {
	try {
		popShade(1);
		var data = {
			'userId' : userAccount
		}
		ajaxFunction(url1 + "selectAddress.action?", data, true,
				function(list) {
					addRessData = list.data;
					insertASData(addRessData);
					popShade(-1);
				});
	} catch (e) {
		popShade(-1);
	}
}
// 写入数据 直接操作 addRessData
function insertASData(data) {
	var i = 0;
	for (; data.length > i; i++) {
		var str = '';
		str += '<div class="site topAndBottomLine  divFlotLeft">'
				+ '<div onclick="on(&apos;' + data[i].id
				+ '&apos;)" class="siteImg divFlotLeft">';
		str += '	<img id="ADSimg-' + data[i].id
				+ '"; class="ADSimg" alt="" src="../images/didian.png">';
		str += '</div>'
				+ '	<div class="siteTit divFlotLeft">'
				+ '	<strong class="siteTitText divFlotLeft overflowHidden"> <span'
				+ '		id="buyersName-' + data[i].id + '">'
				+ data[i].consigneeName + '<span></strong> <strong'
				+ '		class="siteTitText1 divFlotLeft" id="buyersPhone-'
				+ data[i].id + '">' + data[i].consigneePhone + '</strong>'
				+ '	</div>' + '	<div class="siteText divFlotLeft">'
				+ '	<div class="stText divFlotLeft" id="buyersAddress-'//<img alt="" src="../images/sc.png">
				+ data[i].id + '">' + data[i].area + data[i].address + '</div>'+'<div class="imgRjt divFlotLeft" onclick="delASD()"></div>'
				+ '</div>' + '</div>';
		$("#ADSList").append(str);
		if (maxId <= (data[i].id - 0)) {
			maxId = (data[i].id - 0) - (-1);
		}
	}
	if ((i - 0) == 0) {
		$("#ADSMsg").text("去点击 红色十字按钮 添加收货地址吧");
	} else {
		on(addRessData[0].id);
		$("#ADSMsg").text("");
	}
}
// 点击选中（）{
function on(id) {
	xzId = id;
	$(".ADSimg").attr("src", "../images/didian.png");
	$("#ADSimg-" + id).attr("src", "../images/didianxz.png");
}
// 确认选择收货地址
function okADS() {
	switch ($(".upAddressO").text().trim()) {
		case "保存" :
			sumbitADSData();
			break;
		case "确定" :
			$("#buyersName").text($("#buyersName-" + xzId).text());
			$("#buyersPhone").text($("#buyersPhone-" + xzId).text());
			$("#buyersAddress").text($("#buyersAddress-" + xzId).text());
			closeASD();
			break;
	}
}
function delASD(div){
	
}

function sumbitADSData() {
	if (BIsNullVal($("#1").text())) {
		popoAlert("请选择省份");
		return;
	}
	if (BIsNullVal($("#2").text())) {
		popoAlert("请选择城市");
		return;
	}
	/*
	 * if (BIsNullVal($("#3").text())) { popoAlert("请选择街道"); return; }
	 */
	if (BIsNullVal($("#ebuyersName").val())) {
		return;
	}
	if (BIsNullVal($("#ebuyersPhone").val())) {
		return;
	}
	if (!checkTel($("#ebuyersPhone").val())) {
		$("#ebuyersPhone").val("");
		popoAlert("手机号格式错误");
		return;
	}
	if (BIsNullVal($("#ebuyersAddress").val())) {
		return;
	}
	var params = {};
	params.userId = userAccount; // 用户id
	params.consigneeName = $("#ebuyersName").val(); // 联系人
	params.consigneePhone = $("#ebuyersPhone").val(); // 电话
	params.area = $("#1").text() + "_" + $("#2").text() + "_" + $("#3").text(); // 地区
	params.postCode = ""; // 邮政编码
	params.address = $("#ebuyersAddress").val(); // 详细地址
	params.isDefaultAddress = 0; // 是否默认收货地址
	params.street = 0; // 街道

	// alert(url);
	// console.log(params);

	// return;
	try {
		ajaxFunction(url1 + "addAddress.action", params, true, function(data) {
					if (!data.success) {
						popoAlert("收货地址保存失败（但不会影响本次下单）");
					}
				});
	} catch (e) {

	}
	/*
	 * $("#buyersName").text(params.consigneeName);
	 * $("#buyersPhone").text(params.consigneePhone);
	 * $("#buyersAddress").text(params.area+params.address);
	 */
	var datas = [{
				id : maxId,
				consigneeName : params.consigneeName,
				consigneePhone : params.consigneePhone,
				area : params.area,
				address : params.address
			}];
	addRessData.push(datas);
	insertASData(datas);
	$(".upAddressO").text("确定");
	$(".tjImg").fadeIn();
	$("#ADSEidt").fadeOut();
	$("#ADSList").fadeIn();
	$("#ADSEtTitl").html("");
}
// 验证手机号
function checkTel(tel) {
	var reg = /^1[3|4|5|8][0-9]\d{4,8}$/;
	if (!reg.test($.trim(tel))) {
		return false;
	}
	return true;
}

// =======非空判断；
function BIsNullVal(value) {
	return typeof(value) == "undefined"
			|| value == null
			|| (typeof(value) == 'string' && (value == "undefined"
					|| value == '' || value == 'null'))
			|| (typeof(value) == 'boolean' && value == false);
};
// 添加收货地址
var dsyData;
function addADS() {
	$(".tjImg").fadeOut();
	if (dsyData == undefined) {
		// 调用添加方法时引入areaData js （相对较大 不要每次加载页面都引入）
		jQuery.getScript("../js/area.js", function(data, status, jqxhr) {
					/*
					 * 做一些加载完成后需要执行的事情
					 */
					dsyData = dsy;
					initADSPage();
				});
	} else {
		initADSPage();
	}
}
// 打开添加收货地址页面 时触发
function initADSPage() {
	$("#ADSEidt").fadeIn();
	$("#ADSList").fadeOut();
	$(".upAddressO").text("保存");
	init("0");
}
// 真正向页面写入 城市信息 id= dsy的key键 详细结构请看源码
function init(id) {
	closeADSEtTitl();
	var s = id.split("_");
	var ii = s.length;
	switch (ii) {
		case 1 :
			$("#ADSEtTitlT").text("请选择省份");
			break;
		case 2 :
			$("#ADSEtTitlT").text("选择城市");
			break;
		case 3 :
			$("#ADSEtTitlT").text("选择街道(区镇)");
			break;
	}
	$("#ADSEarea").text("");
	var data = dsyData.Items[id];
	var i = 0
	try {
		for (; data.length > i; i++) {
			var str = '<div onclick="goADS(&apos;'
					+ id
					+ '_'
					+ i
					+ '&apos;,&apos;'
					+ data[i]
					+ '&apos;)" class="filterss color929292 areaN divFlotLeft   ASDText"> '
					+ data[i] + '</div>';
			$("#ADSEarea").append(str);
		}
	} catch (e) {
		if (ii - 4 < 0) {
			$("#" + ii).remove();
		}
		openADSEtTitl();
		// 城市选择结束 填写详细信息
	}
}

function goADSDetails() {

}

// 点击城市触发
function goADS(id, text) {
	var s = id.split("_");
	var i = s.length;
	var divId = "";
	// 父级城市id
	var areaid = "";
	var str = "";
	switch (i) {
		// 省
		case 2 :
			areaid = s[0];
			divId = "1";
			break;
		// 城
		case 3 :
			areaid = s[0] + "_" + s[1];
			divId = "2";
			break;
		// 去
		case 4 :
			areaid = s[0] + "_" + s[1] + "_" + s[2];
			divId = "3";
			break;
	}
	var div = $("#" + divId);
	if (div.length == 0) {
		str = '<div onclick="init(&apos;'
				+ areaid
				+ '&apos;)" id="'
				+ divId
				+ '" data="&apos;'
				+ id
				+ '&apos;" class=" filterss colorF929292 ADSEtTitlT divFlotLeft ASDText">'
				+ text + '</div> ';
		$("#ADSEtTitl").append(str);
	} else {
		div.attr("onclick", "init('" + areaid + "')");
		div.text(text);
	}
	init(id);
}
/* 打开详细页面 */
function openADSEtTitl() {
	$("#ADSEtTitlT").text("请添加 详细地址-收件人-收件人电话");
	$("#ADSEtTitlDetails").fadeIn();
	$("#ebuyersAddress").attr("value", "");
	$("#ebuyersName").attr("value", "");
	$("#ebuyersPhone").attr("value", "");
}
function closeADSEtTitl() {
	$("#ADSEtTitlDetails").hide();
}