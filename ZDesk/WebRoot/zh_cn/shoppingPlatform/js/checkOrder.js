var url = "/" + PRJ_PATH + "/" + ZDesk_ROU + "/shoppingCartAction/";
var url1 = "/" + PRJ_PATH + "/" + ZDesk_ROU + "/Category/";
var urls = "/" + PRJ_PATH + "/" + ZDesk_ROU;
var goodsId = "";
var goodsNumber = "";
var spcdId = "";// 购物车主键id
var count = 0;
var price = 0.00;
var userAccount = "1";
var goodsCotegory = "";
var check = 1;
// 全为虚拟类 ==true
var goodsType = false;
$(document).ready(function() {
			// 获取数据
			spcdId = getUrlParam("spcdId") || "";
			goodsId = getUrlParam("goodsId") || "";
			if (spcdId.length == 0 && goodsId.length == 0) {
				popoAlert("数据异常");
				return;
			}
			selectASD1();
			// 遮罩载体
			$(document.body).append(zzStr);
			select();
			// popoAlert("ss");
			loaded();
		});
/** ********ajax()*********** */
function selectASD1() {
	try {
		popShade(1);
		var data = {
			'userId' : userAccount,
			'offset' : 0,
			'rows' : 1
		}
		ajaxFunction(url1 + "selectAddress.action?", data, true,
				function(list) {
					try {
						$("#buyersName").text(list.data[0].consigneeName || "");
						$("#buyersPhone").text(list.data[0].consigneePhone);
						$("#buyersAddress").text(list.data[0].area
								+ list.data[0].address);
					} catch (e) {

					}
					popShade(-1);
				});
	} catch (e) {
		popShade(-1);
	}
}
// http://192.168.0.80:8888/ZDesk/ZKM/Category/getAllProp.action?flag=flag&saleOrBuy=0&tableName=tb_Prop&categoryId=%
function initInputData(gId, ategoryId) {
	try {
		var data = {
			flag : "flag",
			saleOrBuy : "0",
			tableName : "tb_Prop",
			// "categoryId" : ""
			"categoryId" : ategoryId
		}
		ajaxFunction(url1 + "getAllProp.action", data, true, function(list) {
					for (var i = 0; i < list.data.length || 0; i++) {
						// alert(list[i]);
						initGoodsInput(gId, list.data[i], list.data[i].id);
					}
					check--;
				});
	} catch (e) {
		popoAlert('加载异常请重试');
	}
}

// init data
function select() {
	try {
		popShade(1);
		var data = {
			"userAccount" : userAccount,
			"follow" : "0",
			"line" : 0,
			"id" : spcdId || "",
			'goodsId' : goodsId
		}
		ajaxFunction(url + "query.action", data, true, function(list) {
					var bool = true;
					if (goodsId.length > 0)
						bool = false;
					var l = list.length;
					for (var i = 0; i < list.length; i++) {
						// alert(list[i]);
						list[i].goodsCount = list[i].goodsCount
								|| getUrlParam("goodsNumber");
						if (list[i].goodsType == 1) {
							l--;
						}
						initGoods(list[i]);
						initInputData(list[i].id, list[i].goodsCateGory);
						if (bool)
							goodsId += list[i].id + ",";
						goodsNumber += list[i].goodsCount + ",";
						count -= -(list[i].goodsCount || getUrlParam("goodsNumber"));
						price -= -(list[i].goodsSalePrice);
					}
					if (l == 0 && list.length != 0) {
						// 隐藏收货地址
						goodsType = true;
						$(".site").hide();
					}
					upMyScroll();
					check = list.length;
					popShade(-1);
					upPageData();
				});
	} catch (e) {
		popShade(-1);
	}
}
function upPageData() {
	$("#sumPrice").text(price + " ");
	$("#count").text("X" + count + " 件");
}
// 写入商品文本
function initGoodsInput(gid, data, id) {
	var values = "";
	for (var i = 0; i < data.children.length || 0; i++) {
		values += data.children[i].propDesc || " " + ",";
	}
	var idSon = gid + "-" + id;
	values = values.substring(0, (values.length - 1));
	// 默认值
	var val = "";
	try {
		val = data.children[0]['propDesc'];
	} catch (e) {
		val = data['propDesc'];
	}
	var str = '<div class="goodsDTSon divFlotLeft goodsDTSon-' + gid + '">'
			+ '<div class="inputDTKey divFlotLeft" ifNull="' + data.requiredOrNot
			+ '" valueId="inputDTValue-' + idSon + '" valueType="'
			+ data.showType + '" id="inputDTKey-' + idSon + '">'
			+ data.propName + '</div>';
	if (data.showType == 'text') {
		str += '<input class="goodsNputText divFlotLeft" id="inputDTValue-' + idSon
				+ '"  name="tt" />';
	} else {
		str += '<div class="inputDTValue divFlotLeft" id="inputDTValue-' + idSon
				+ '" onclick="inputZZ(&apos;' + data.showType + '&apos;,&apos;'
				+ values + '&apos;,&apos;' + data.propName + '&apos;,&apos;'
				+ idSon + '&apos;)">' + val + '</div>';
	}
	str += '<div class="inputDTImg divFlotLeft" onclick="inputZZ(&apos;' + data.showType
			+ '&apos;,&apos;' + values + '&apos;,&apos;' + data.propName
			+ '&apos;,&apos;' + idSon + '&apos;)">'
			+ '<img alt="" src="../images/yjt.png">' + '</div>' + '</div>';

	$("#goodsDT-" + gid).append(str);
}
// 写入数据
function initGoods(data) {
	$("#container").append('<div class="goodsMap topAndBottomLine divFlotLeft" id="goodsDiv-'
			+ data.id + '"></div>');
	initGoodsText(data);
	// 未加清除事件
	/*
	 * $("#goodsDiv-" + data.sid).append('<div class="xx-' + state + '">商品已下架
	 * -点我清除所有下架商品</div>');
	 */
}
function initGoodsText(data) {
	var str = '<strong class="goodsTitle overflowHidden divFlotLeft text_overflow1">' + data.goodsTitle
			+ '</strong>' + '	<div class="goodsText colorHSBJ divFlotLeft">'
			+ '<div class="goodsImg divFlotLeft">' + '	<img alt="图片暂无"' + '		src="'
			+ data.goodsImgs.split(",")[0] + '">' + '</div>'
			+ '<div class="goodsNamePrice divFlotLeft">'
			+ '	<div class="goodsName divFlotLeft overflowHidden">' + data.goodsName
			+ '</div>' + '	<div class="goodsPrice divFlotLeft colorFFD5702">'
			+ '		￥<span id="goodsPrice">' + data.goodsSalePrice + '</span>'
			+ '	</div>' + '</div>' + '<div class="goodsPropNumber divFlotLeft">'
			+ '	<div id="goodsProp-' + data.id
			+ '" class="goodsProp corlorFAAA9AC  text_overflow4 text_overflow divFlotLeft">' + data.goodsProp + '</div>'
			+ '	<div class="goodsNumber divFlotLeft">' + '		x<span id="goodsNumber-'
			+ data.id + '">' + data.goodsCount + '</span>' + '	</div>'
			+ '	</div>' + '	</div>' + '	<div id="goodsDT-' + data.id
			+ '" class="goodsDT divFlotLeft">';
	/**
	 * + ' <div class="goodsDTSon">' + '<div class="inputDTKey"
	 * id="inputDTKey-' + data.id + '">类型：</div>' + '<div class="inputDTValue"
	 * id="inputDTValue-' + data.id + '"' + '
	 * onclick="inputZZ(&apos;select&apos;,&apos;aaxx,xx,XL,aa,aa,a,a&apos;,&apos;选择啊啊&apos;,&apos;1&apos;)">啊xx</div>' + '<div
	 * class="inputDTImg"
	 * onclick="inputZZ(&apos;select&apos;,&apos;aaxx,xx,XL&apos;,&apos;选择啊啊&apos;,&apos;1&apos;)">' + '
	 * <img alt="" src="../images/yjt.png">' + '</div>' + '</div>'
	 */
	str += '</div>';
	$("#goodsDiv-" + data.id).html(str);
}
// 去提交
function goSumbitOrder() {
	if (check < "0") {
		return;
	}
	var goodsIds = goodsId.split(",");
	for (var i = 0; i < goodsIds.length; i++) {
		var goodsCotegorySon = "";
		var data = $(".goodsDTSon-" + goodsIds[i] + " div[valueid]");
		var bool = true;
		var msg = "";
		for (var x = 0; x < data.length; x++) {
			var key = data.eq(x).text();
			var valueid = data.eq(x).attr("valueid");
			var value = $("#" + valueid).text() || $("#" + valueid).val();
			if (value.length == 0) {
				value = $("#" + valueid).val();
			}
			var ifNull = data.eq(x).attr("ifnull");
			if (ifNull == 1 && (value == undefined || value == "")) {
				bool = false;
				msg += "请完善" + key + "</br>";
			}
			goodsCotegorySon += key + ":" + value + "-";
		}
		// 必填字段 添加提示
		if (!bool) {
			popoAlert(msg);
			goodsCotegory = "";
			return;
		}
		var value = "";
		goodsCotegory += goodsCotegorySon + " , ";
	}
	sumbitOrder();
}
// 提交订单
function sumbitOrder() {
	popShade(1);
	try {
		// 参数带放入
		var buyersAddress = $("#buyersAddress").text();
		var buyersPhone=$("#buyersPhone").text();
		var buyersName=$("#buyersName").text();
		if (!goodsType) {
			if ($("#buyersAddress").text() == "") {
				popoAlert("请选择收货地址");
				popShade(-1);
				return;
			}
		}else{
			buyersAddress = "虚拟物品";
			buyersPhone="虚拟物品";
			buyersName="虚拟物品";
		}
		var data = {
			ids : goodsId,// 商品id
			goodsId : spcdId || 0,// 购物车主键
			goodsNumber : goodsNumber,
			buyersUser : userAccount,// 必填
			buyersName : buyersName,// 必填
			'buyersPhone' : buyersPhone,// 必填
			'buyersAddress' : buyersAddress,// 必填
			buyersMessage : $("#buyersMessage").val(),// 必填
			sellersUser : "1",// 必填
			sellersName : "1",// 必填
			sellersDepartmentId : "1",// 必填
			sellersDepartmentName : "1",// 必填
			companyID : "1",
			goodsCateGory1 : goodsCotegory,
			goodsCateGory : "1"
		};
		var url1 = '/' + PRJ_PATH + '/' + ZDesk_ROU + '/OD/'
				+ 'addOrder.action?';
		ajaxFunction(url1 + 'insert.action', data, true, function(data) {
					popoAlert(data.mgs + "(加入跳转地址)");
					if (data.success) {
						window.location.href= "userCenter.html";
						/*
						window.location.href = urls + "/OD"
								+ "/toPayment.action?orderNumber="
								+ data.data.orderNumber + "&buyersUser="
								+ userAccount;*/
					}
					popShade(-1);
				});
	} catch (e) {
		popoAlert("提交失败请刷新页面重新提交");
		popShade(-1);
		// TODO: handle exception
	}
}
/** ******--ajax--********** */
/** ********通用方法()*********** */
// 获取url中的参数
/** ******--通用方法--********** */

/** **************input******************** */
function inputZZ(type, data, title, id) {
	if (type == 'text') {
		return;
	}
	var str = "<div class='zz-input'>";
	str += "<div class='zz-Title'>" + title + "</div>";
	str += selectZZ(type, data, id);
	str += "</div>";
	popoAlert(str, function() {
				var value = "";
				// $('input[name="check_item"]:checked ').val();
				$('input[name="check_item"]:checked').each(function() {
							value += $(this).val() + ",";
						});
				var inpuValue = $("#inputDTValue-" + id);
				inpuValue.text(value.substring(0, (value.length - 1)) || "");
			});
	var value = $("#inputDTValue-" + id).text();
	$("#c" + value).attr("checked", "checked");
	oo("m" + value, true);
}
function selectZZ(type, data, id) {
	var type1 = "";
	if (type == "checkbox") {
		type1 = "checkbox";
	} else {
		type1 = "radio";
	}
	var st = data.split(",");
	var str = "<div class='zz-Text'>"
	var mrText = $("#inputDTValue-" + id).text();
	for (var i = 0; i < st.length; i++) {
		str += "<div class='zz-TextSon'><div "
				/*
				 * + "' onclick='oo(&apos;" + st[i] + "" + i + "&apos;,true)'"
				 */
				+ " class='zzTextSon-value zzBc' id='m" + st[i]
				+ "' ><label onclick='oo(&apos;m" + st[i]
				+ "&apos;,true)'  for='c" + st[i] + "'>" + st[i]
				+ "</label></div>" + "<div class='zzRadio'>" + "<input type='"
				+ type1 + "' ";
		str += " class='checkbox1' value='" + st[i]
				+ "'  name='check_item' id='c" + st[i] + "'>" + "</div>"
				+ "</div>";
	}
	str += "</div>";
	return str;
}
function oo(id, bool) {
	if (bool) {
		$(".zzTextSon-value").removeClass("zzOk");
		$(".zzTextSon-value").addClass("zzBc");
		$("#c" + id).removeClass("zzBc");
		$("#c" + id).addClass("zzOk");
	}
}
/** **************input******************** */
