/**
 * sumbitOrder()提交订单方法 参数等待放入 手机购物车 js :20160601-01
 */
var sumPriceId = "#sumPrice";
var priceID = "#price-";
var numberId = "#number-";
// 用户账号
var userAccount = "1";
var goodsDivId = "#container";
var goodsTypeDivId = "#agtText";
// 翻页
var follow = 0;
var line = 5;
var numberOld = 0;
var url = "/" + PRJ_PATH + "/" + ZDesk_ROU + "/shoppingCartAction/";
var goodsProp = "";
// 删除单个商品调用
var goodsStatic = "";
// 页面商品数量
var pageGoodsCount = 0;
var typeBool = false;
// 遮罩属性
var ckTure = true;
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
	message : '<img src="../images/wait.gif" border="0" />'
};
$(document).ready(function() {
			$(".alertD").toggle();
			select();
			$("#alertzz").fadeOut();
			// 绑定事件
			c();
			// 遮罩载体
			$(document.body).append(zzStr);
			loaded();
		});
/** ************ 数据加载取 ************* */
function select(id) {
	try {
		var data = {
			"userAccount" : userAccount,
			"follow" : follow,
			"line" : line,
			"id" : id || null
		}
		var ids = "";
		popShade(1);
		ajaxFunction(url + "query.action", data, true, function(list) {
			for (var i = 0; i < list.length; i++) {
				// alert(list[i]);
				if (id == undefined || id.length <= 0) {
					initGoods(list[i]);
				} else {
					initGoodsText(list[i]);
				}
				ids += list[i].sid + ",";
			}
			// 没有更多数据
			if (id == undefined && list.length < (line - 0)) {
				$("#line").html(" ");
				if (list.length == 0 && (1 + follow) <= 1) {
					$("#line")
							.html('<div onclick="location=&apos;index.html&apos;">购物车空空荡荡 快去添加商品吧。。。</div>');
				}
				follow = -1;
			}
			if (id == undefined) {
				pageGoodsCount += list.length;
			}
			// 隐藏编辑框

			var id1 = ids.split(",") || [0];
			for (var i = 0; i < id1.length; i++) {
				$("#editText-" + id1[i]).toggle();
				$("#editDel-" + id1[i]).toggle();
			}
			popShade(-1);
			upMyScroll();
		});
	} catch (e) {
		popShade(-1);
	}
}
// 绑定单选事件-隐藏编辑表单
function c() {
	try {
		/*
		 * $("input[name='check_item']").click(function() { var index =
		 * $("input[name='check_item']").index(this);
		 * $("input[name='check_item']").eq(index) .toggleClass("checked");
		 * upSumPrice(); }); $("#check_all,#box_all").click(function() {
		 * 
		 * $("input[name='check_item']").attr("checked",
		 * $(this).attr("checked"));
		 * 
		 * $("input[name='check_item']").each(function() { this.checked =
		 * !this.checked; }); $("input[name='check_item'],#check_all,#box_all")
		 * .toggleClass("checked"); upSumPrice(); }); $("input").blur(function() {
		 * if (!verifyInteger($(this).val())) { $(this).val(numberOld); } else {
		 * numberOld = $(this).val(); } });
		 */
		$(document.body).delegate("input[name='check_item']", "click",
				function() {
					var index = $("input[name='check_item']").index(this);
					$("input[name='check_item']").eq(index)
							.toggleClass("checked");
					/*
					 * if(!$("input[name='check_item']").eq(index).hasClass("checked")){
					 * this.checked = true; console.log(this);
					 * $("input[name='check_item']").eq(index)
					 * .toggleClass("checked"); }else{ this.checked = false;
					 * $("input[name='check_item']").eq(index)
					 * .removeClass("checked") }
					 */

					upSumPrice();
				});
		$(document.body).delegate("#check_all,#box_all", "click", function() {

			/*
			 * //点击全选时判断是否已选中 如果全选选中，则取消，如果全选未选中，则全选
			 * if(!$("#check_all,#box_all").hasClass("checked")){
			 * $("input[name='check_item'],#check_all,#box_all")
			 * .toggleClass("checked"); }else{
			 * $("input[name='check_item'],#check_all,#box_all")
			 * .removeClass("checked"); }
			 */

			/*
			 * $("input[name='check_item']").each(function() { this.checked =
			 * bool; });
			 */
			if (ckTure) {
				$("input[name='check_item'],#check_all,#box_all")
						.addClass("checked");
			} else {
				$("input[name='check_item'],#check_all,#box_all")
						.removeClass("checked");
			}
			$("input[name='check_item']").each(function() {
						this.checked = ckTure;
					});
			ckTure = !ckTure;
			upSumPrice();
		});
		$(document.body).delegate(".editN", "blur", function() {
					if (!verifyInteger($(this).val())) {
						$(this).val(numberOld);
					} else {
						numberOld = $(this).val();
					}
				});
	} catch (e) {

	}
}
// 写入数据
function initGoods(data) {
	var state = data.goodsStatus * data.dataStatus * data.goodsCount1;
	// 清除div
	$("#goodsDiv-" + data.sid).remove();
	$(goodsDivId).append('<div id="goodsDiv-' + data.sid
			+ '" class="goodsDiv topAndBottomLine overflowHidden "></div>');
	initGoodsText(data, state);
	// 未加清除事件
	/*
	 * $("#goodsDiv-" + data.sid).append('<div class="xx-' + state + '">商品已下架
	 * -点我清除所有下架商品</div>');
	 */
}
function initGoodsText(data, state) {
	var str = '<div class="goodsTitleC overflowHidden "> 			<div id="tit-'
			+ data.sid + '" class="titD  text_overflow1 text_overflow text_overflow">' + data.goodsTitle
			+ '			</div>'
			+ '			<div class="Sline overflowHidden"></div><div onclick="';
	if (state != 0) {
		str += 'openEdit(&apos;' + data.sid + '&apos;,&apos;' + data.id
				+ '&apos;);'
	}
	str += '" id="' + data.sid + '" class="delD overflowHidden">';

	if (state != 0) {
		str += '编辑';
	}

	// 加一些缓存数据 // 当前商品 id:data-goods 页面up前商品 id： goodsCountOld
	str += '		</div></div>' + '		<div id="goodsText-' + data.sid
			+ '"  data-goodsCount="' + data.goodsCount1
			+ '"  data-goodsIdOld="' + data.id + '" data-goodsCountOld="'
			+ data.goodsCount + '" data-goods="' + data.id
			+ '" class="goodsTextC overflowHidden ">'
			+ '			<div class="checkbosDiv">';
	if (state != 0) {
		str += '<input type="checkbox" class="checkbox" name="check_item" id="c-'
				+ data.sid + '"' + '					value="' + data.sid + '">';
	} else {
		str += '<span class="goodsOver">已失效</span>';
	}
	str += '</div>'
			+ '			<div class="goodsImgC overflowHidden">'
			+ '				<img id="goodsImg-'
			+ data.sid
			+ '" alt="暂无图片" style="width: 100%;height: 100%;"'
			+ 'src="'
			+ data.goodsImgs.split(",")[0]
			+ '">'
			+ '</div>'
			+ '	<div class="goodsDescribe overflowHidden  divFlotLeft">'
			+ '				<!-- 展示部分 -->'
			+ '				<div class="describe-'
			+ data.sid
			+ ' describe divFlotLeft divGCentre text_overflow1 text_overflow">'
			+ '					<p id="tit1-'
			+ data.sid
			+ '">'
			+ data.goodsName
			+ '</p>'
			+ '				</div>'
			+ '				<div id="text-'
			+ data.sid
			+ '"'
			+ '					class="describe-'
			+ data.sid
			+ ' describe1  divFlotLeft corlorFAAA9AC text_overflow3 text_overflow">'
			+ data.goodsProp
			+ '				</div>'
			+ '				<div class="describe-'
			+ data.sid
			+ ' describe divFlotLeft">'
			+ '					<div id="price-'
			+ data.sid
			+ '"'
			+ '						class="gSP divFlotLeft divGCentre overflowHidden">￥'
			+ data.goodsSalePrice
			+ '</div>'
			+ '					<div id="sPrice-'
			+ data.sid
			+ '" class="gP divFlotLeft overflowHidden">￥'
			+ data.goodsShowPrice
			+ '</div>'
			+ '					<div id="number-'
			+ data.sid
			+ '"'
			+ '						class="gNmb divFlotLeft divGCentre overflowHidden">x'
			+ data.goodsCount
			+ '</div>'
			+ '				</div>'
			+ '				<!-- 编辑部分编辑时显示 -->'
			+ '				<div id="editText-'
			+ data.sid
			+ '" class="editText overflowHidden divFlotLeft"> <div class="editSon1 divFlotLeft">';

	if (state != 0) {
		str += '<div onclick="up(&apos;n-' + data.sid
				+ '&apos;,true)" class="editA divFlotLeft">'
				+ '							<img alt="" src="' + '								../images/jia.png"'
				+ '								style="width: 100%;height: 100%" />'
				+ '						</div>' + '						<div class="editDiv divFlotLeft">'
				+ '							<input id="n-' + data.sid
				+ '" class="editN divFlotLeft" type="text" value="'
				+ data.goodsCount + '"  />' + '						</div>'
				+ '						<div onclick="up(&apos;n-' + data.sid
				+ '&apos;,false)" class="editD divFlotLeft">'
				+ '							<img alt="" src="' + '								../images/jian.png"'
				+ '								style="width: 100%;height: 100%" />'
				+ '						</div>' + '					';
	} else {
		str += '<div' + ' class="editA divFlotLeft">'
				+ '							<img alt="" src="' + '								../images/jia.png"'
				+ '								style="width: 100%;height: 100%" />'
				+ '						</div>' + '						<div class="editDiv divFlotLeft">'
				+ '							<input disabled=disabled id="n-' + data.sid
				+ '" class="editN divFlotLeft" type="text" value="'
				+ data.goodsCount + '" />' + '						</div>' + '						<div '
				+ ' class="editD divFlotLeft">' + '							<img alt="" src="'
				+ '								../images/jian.png"'
				+ '								style="width: 100%;height: 100%" />'
				+ '						</div>' + '					';
	}

	str += '				</div><div id="gCls-' + data.sid
			+ '" class="editSon gCls corlorFAAA9AC text_overflow text_overflow5 divFlotLeft">'
			+ data.goodsProp + '								</div>' + '					<div ';
	if (state != 0)
		str += 'onclick="ok(&apos;' + data.sid + '&apos;,&apos;';

	str += data.goodsId + '&apos;,&apos;' + data.goodsCount + '&apos;,&apos;'
			+ data.goodsShowPrice
			+ '&apos;);" class="editImg divFlotLeft"></div>' + '				</div>'
			+ '				<div onclick="del(&apos;' + data.sid
			+ '&apos;)" id="editDel-' + data.sid
			+ '" class="editDel divFlotLeft">删除</div>' + '			</div></div>';
	$("#goodsDiv-" + data.sid).html(str);
	if (state == 0) {
		openEdit(data.sid, data.id);
	}

}
/** ********** --数据加载 子页面-- *********** */

/**
 * 选择属性 商品主键id 商品id 库存 售价 *
 */
function ok(sId, goodsId, conut, price) {
	// 清除缓存
	$(goodsTypeDivId).text("");
	popShade(1);
	initGoodsProp(sId, goodsId, conut, price);
	$(".alertD").fadeToggle();
}
function initGoodsProp(sId, goodsId, conut, price) {
	try {
		var data = {
			"goodsId" : goodsId
		}
		popShade(1);
		ajaxFunction(url + "searchGoodsProp.action", data, true,
				function(list) {
					for (var i = 0; i < list["data"].length; i++) {
						// alert(list[i]);
						initGoodsType(list["data"][i], sId);
					}
					// 点击选择属性
					$(".agtType").click(function() {
								$(".agtOK").addClass("agtDefault");
								$(".agtOK").removeClass("agtOK");
								$(this).removeClass("agtDefault");
								$(this).addClass("agtOK");
								var str = $(this).attr('data');
								// 记录选中数据
								goodsProp = str;
								initToGoodsTypePage(str);
							});
					popShade(-1);
					typeBool = true;
				});
	} catch (e) {
		typeBool = true;
		popShade(-1);
	}
}
// 插入商品类别
function initGoodsType(data, sId) {
	var goodsId = $("#goodsText-" + sId).attr("data-goods");
	var str = data.id + ',' + data.goodsCount + ',' + data.goodsProp + ','
			+ data.goodsImgs.split(",")[0] + ',' + data.goodsName + ','
			+ data.goodsSalePrice + ',' + sId + ',' + data.goodsShowPrice + ','
			+ data.goodsTitle;
	if (data.id == goodsId) {
		$(goodsTypeDivId).append('<div class="agtType breakAll agtOK" data="'
				+ str.toString() + '">' + data.goodsProp + '</div>');
		goodsProp = str;
		initToGoodsTypePage(str);
	} else {
		$(goodsTypeDivId).append('<div class="agtType breakAll agtDefault" data="' + str
				+ '">' + data.goodsProp + '</div>');
	}

}
// 向更 GoodsTypePage 插入数据
function initToGoodsTypePage(str) {
	var data = str.split(",");
	$("#aGoodsImg").attr("src", data[3]);
	$("#aGoodsCount").text(data[1]);
	$("#aGoodsPrice").text(data[5]);
	$("#aGoodsName").text(data[4]);
	$("#aGoodsProp").text(data[2]);
}
function closeBlockUI() {
	if (!typeBool)
		return
	var data = goodsProp.split(",");
	var number = $("#number-" + data[6]).text().replace("x", "") || 0;
	if ((data[1] - 0) < (number - 0)) {
		popoAlert("库存不足 请选择其他商品");
		return null;
	}
	typeBool = false;
	$(".alertD").toggle();
	upGoodData();
	popShade(-1);
}
function upGoodData() {
	var data = goodsProp.split(",");
	$("#price-" + data[6]).text(data[5]);
	$("#tit-" + data[6]).text(data[8]);
	$("#tit1-" + data[6]).text(data[4]);
	$("#sPrice-" + data[6]).text(data[7]);
	$("#text-" + data[6]).text(data[2]);
	$("#gCls-" + data[6]).text(data[2]);
	var number = $("#number-" + data[6]).text().replace("x", "") || 0;
	if ((data[1] - 0) < (number - 0)) {
		$("#n-" + data[6]).val(data[1]);
		popoAlert("库存不足 商品数量自动变更为 最大库存");
	}
	$("#goodsImg-" + data[6]).attr("src", data[3]);
	// goodsId 主键 值
	$("#goodsText-" + data[6]).attr("data-goods", data[0]);
	$("#goodsText-" + data[6]).attr("data-goodsCount", data[1]);
}
/** --选择属性-- * */

/** ******复选框操作区******* */

// 更新总金额
function upSumPrice() {
	var sumPrice = 0;
	var count = 0;
	console.log($('input[name="check_item"]:checked'));
	// $('input[name="check_item"]:checked').each(function() {
	$('input[name="check_item"]:checked').each(function() {
				// alert(1111);
				count++;
				var id = $(this).val();
				var price = $(priceID + id).text().replace("￥", "") || 0;
				var number = $(numberId + id).text().replace("x", "") || 0;
				sumPrice -= -(price * number);
			});
	if (count > 0) {
		$("#delG").css("background", "#EC5E38");
		$("#delG").attr("disabled", false);
		$("#submitG").css("background", "#FD5702");
		$("#submitG").attr("disabled", false);
	} else {
		$("#delG").css("background", "#DDDDDD");
		$("#delG").attr("disabled", true);
		$("#submitG").css("background", "#DDDDDD");
		$("#submitG").attr("disabled", true);
	}

	$(".gt").html(count);
	$(sumPriceId).text(sumPrice);
	// alert(chk_value.length == 0 ? '你还没有选择任何内容！' : chk_value);
}
function verifyInteger(i) {
	var re = /^[0-9]*[1-9][0-9]*$/;
	// 验证
	if (re.test(i)) {
		return true;
	}
	return false;
}
/** ****--复选框操作区--***** */

/** ************编辑区************* */
function openEdit(id, goodsId) {
	var state = $("#" + id).text().trim();
	goEdit(id);
	if (state == "编辑") {
		numberOld = $(numberId + id).text().replace("x", "");
		$("#" + id).text("完成");
	} else {
		upData(id);
		upSumPrice();
		upShoppingCart(id);
		$("#" + id).text("编辑");
	}
}

// 分页
function appendGoods() {
	if ((follow - 0) < 0)
		return;
	follow += line;
	select();
}
// 提交订单
function goSumbitOrder() {
	var str = "是否提交订单";
	var i = 0;
	var id = "";
	$('input[name="check_item"]:checked').each(function() {
				id += $(this).val() + ",";
				i++;
			});
	if (i == 0) {
		popShade(1);
		popoErr();
		return false;
	}
	popoAlert(str, function() {
				window.location.href = "checkOrder.html?spcdId="
						+ id.substring(0, (id.length - 1));
			});

}

function sumbitOrder() {
	popShade(1);
	var id = "";
	var numbers = "";
	var ids = "";
	try {
		$('input[name="check_item"]:checked').each(function() {
			id += $(this).val() + ",";
			ids += $("#goodsText-" + $(this).val()).attr("data-goods") + ",";
			numbers += $(numberId + $(this).val()).text().replace("x", "")
					+ ",";
		});
		// 参数带放入
		var data = {
			ids : ids,// 商品id
			goodsId : id,// 购物车主键
			goodsNumber : numbers,
			buyersUser : "1",// 必填
			buyersName : "1",// 必填
			buyersPhone : "1",// 必填
			buyersAddress : "1",// 必填
			buyersMessage : "1",// 必填
			sellersUser : "1",// 必填
			sellersName : "1",// 必填
			sellersDepartmentId : "1",// 必填
			sellersDepartmentName : "1",// 必填
			companyID : "1",
			goodsCateGory : "1"
		};
		var url1 = '/' + PRJ_PATH + '/' + ZDesk_ROU + '/OD/'
				+ 'addOrder.action?';
		ajaxFunction(url1 + 'insert.action', data, true, function(data) {
					popoAlert(data.mgs);
					if (data.success) {
						var ids = data.data.goodsId.split(",");
						hiedGoodsDiv(ids);
					}
					popShade(-1);
				});
	} catch (e) {
		popoAlert("更新失败");
		popShade(-1);
		// TODO: handle exception
	}
}
// 更新数据
function upData(id) {
	var num = $("#n-" + id).val().trim();
	var gCls = $("#gCls-" + id).text().trim();
	var xx = $("#number-" + id);
	xx.html("x" + num);
	$("#text--" + id).html(gCls);
}
// 无选中数据时的操作
function popoErr() {
	popoAlert("未选中数据");
	popShade(-1);
}
function del(ids) {
	goodsStatic = ids;
	popoAlert("继续即将删除选中数据 是否继续？", function(a) {
				var id = goodsStatic;
				if (id == undefined) {
					id = "";
					$('input[name="check_item"]:checked').each(function() {
								id += $(this).val() + ",";
							});
					id = id.substring(0, (id.length - 1));
				}
				if (id.length <= 0) {
					popoErr();
					return false;
				}
				delShoppingCart(id);
			});
}
// 提交把控 将验证数据是否有变动 如果没有变动不将返回false
function bool(id) {
	var oldCount = $("#goodsText-" + id).attr("data-goodscountold");
	var count = $("#n-" + id).val();
	var goodsId = $("#goodsText-" + id).attr("data-goods");
	var oldGoodsId = $("#goodsText-" + id).attr("data-goodsidold");
	if (oldCount != count || goodsId != oldGoodsId) {
		return true;
	}
	return false;
}

// 更新 订单商品 ajax
function upShoppingCart(id) {
	if (!bool(id))
		return;
	popShade(1);
	var num = $("#n-" + id).val().trim();
	var goodsId = $("#goodsText-" + id).attr("data-goods");
	var data = {
		goodsSubId : goodsId,// 商品主键id
		id : id,// 购物车主键
		goodsCount : num,// 商品数量
		userAccount : userAccount
	};
	try {
		ajaxFunction(url + 'update.action', data, true, function(data) {
					popoAlert(data.mgs);
					if (!data.success) {
						// 更改失败刷新 局部div
						select(data.data.id);
					} else {
						$("#goodsText-" + data.data.id).attr(
								"data-goodscountold", data.data.goodsCount);
						$("#goodsText-" + data.data.id).attr("data-goodsidold",
								data.data.goodsSubId);
					}
					popShade(-1);
				});
	} catch (e) {
		// 更改失败刷新 局部div
		popShade(-1);
		select(id);
		popoAlert("数据异常 请联系客服:" + e);
		// TODO: handle exception
	}
}
// 删除订单 逗号分隔
function delShoppingCart(ids) {
	popShade(1);
	var data = {
		id : ids
		// 购物车主键
	};
	try {
		ajaxFunction(url + 'delete.action', data, true, function(data) {
					if (data.success) {
						var ids = data.data.id.split(",");
						hiedGoodsDiv(ids);
						pageGoodsCount -= (ids || "").length;
						if (pageGoodsCount == 0)// 页面无数据刷新页面
							select();
					}
					popoAlert(data.mgs);
					popShade(-1);
				});
	} catch (e) {
		popShade(-1);
		// TODO: handle exception
	}
}
// 隐藏删除商品
function hiedGoodsDiv(ids) {
	for (var i = 0; i < ids.length; i++) {
		$("#goodsDiv-" + ids[i]).slideUp();
		$("#goodsDiv-" + ids[i]).text("");
	}
	upMyScroll();
	upSumPrice();
}
// 显示/隐藏编辑
function goEdit(id) {
	$("#editText-" + id).toggle();
	$("#editDel-" + id).toggle();
	$(".describe-" + id).toggle();
}
/** **********--编辑区--*********** */

/** ***********up按钮区************* */
function up(id, bool) {
	var num = $("#" + id).val();
	if (bool) {
		num -= -1;
	} else {
		num -= 1;
	}
	if (verifyInteger(num))
		numberOld = num;
	var goodsCount = $("#goodsText-" + id.replace("n-", ""))
			.attr("data-goodscount");
	if ((goodsCount - 0) < (num - 0)) {
		numberOld = goodsCount;
		popoAlert("库存不足商品数量已改为最大库存");
	}
	$("#" + id).val(numberOld);
}
/** *********--up按钮区--*********** */

