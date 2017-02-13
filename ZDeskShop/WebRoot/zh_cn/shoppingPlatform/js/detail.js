//加载评价内容
function loadPingJia() {
	var params = {};
	params.rows = 2;
	params.offset = Math.abs(parseInt((count - 1) * params.rows));
	params.groupId = goodsData.groupId;
			 // params.goodsId = "00B811CF-AE66-4F6E-89EF-9F7DF7BD3785"; //测试groupId
	ajaxFunction("/" + PRJ_PATH + "/ZKM/Category/selectAllPingJia.action", params, true, function (data) {
		if (data.success) {
			for (var i = 0; i < data.data.length; i++) {
				$("#pingjia").append("<li>" + "<p class='grey'>\u8bc4\u5206\uff1a<span class='star star" + data.data[i].goodLevel + "'></span></p>" + "<p class='mt'>" + data.data[i].content + "</p>" + "<p class='grey mt'>" + data.data[i].userName + "&nbsp;&nbsp;&nbsp;" + data.data[i].addDate.split(" ")[0] + "</p>" + "</li>");
			}
					
					//控制显示和隐藏加载更多
			if ((parseInt(params.offset) + parseInt(params.rows)) >= data.total) {
				$("#loadMore").css("display", "none");
			} else {
				$("#loadMore").css("display", "block");
			}
					
						
			// 这个count 参数为分页参数，count = 0时 为加载第一页。下面内容只需加载一次
			if (count == 1) {
				var goodPer = parseInt((parseInt(data.good) / parseInt(data.total)) * 100);
				var good = parseInt((parseInt(data.good) / parseInt(data.total)) * 50);
				var center = parseInt((parseInt(data.center) / parseInt(data.total)) * 50);
				var bad = parseInt((parseInt(data.bad) / parseInt(data.total)) * 50);
							//没有评论时，设为0%；
				if (isNaN(goodPer)) {
					$(".commtSum").html(0 + "%<br/>全部");
				} else {
					$(".commtSum").html(goodPer + "%<br/>全部");
				}
				$(".goodCommtScore").html("<em></em><i style='height:" + good + "px;'></i>");
				$(".centerCommtScore").html("<em></em><i style='height:" + center + "px;'></i>");
				$(".badCommtScore").html("<em></em><i style='height:" + bad + "px;'></i>");
				$(".allPJ").html("全部<br />(" + data.total + ")");
				$(".goodPJ").html("好评<br />(" + data.good + ")");
				$(".centerPJ").html("中评<br />(" + data.center + ")");
				$(".badPJ").html("差评<br />(" + data.bad + ")");
			}
		}
	});
}

	//评价 点击加载更多触发
function loadMore(){
	count++;
	loadPingJia();
}


//验证库存
function validateCount(id) {
	var params = {};
	params.id = id;
	var result;
	ajaxFunction("/" + PRJ_PATH + "/" + ZDesk_ROU + "/goodsManager/validateCount.action", params, false, function (data) {
		if (data.success) {
			result = data.goodsCount;
		}
	});
	return result;
}

//数量减
function subtract() {
	if (parseInt($("#count").val()) == 1) {
		return;
	}
	$("#count").val(parseInt($("#count").val()) - 1);
}
		
//数量加
function add() {
	$("#count").val(parseInt($("#count").val()) + 1);
}
		
//改变所选属性时，
function changeProp(v) {
	var arr = v.split(",");
	$(".wid").html(arr[0]);
	$(".goodsSalePrice").html(arr[1]);
	$(".goodsCount").html(arr[2]);
	$("#hId").val(arr[3]);
	$("#showImg")[0].src = arr[4];
	$(".goodsProp").html(arr[5]);
	// $(".goodsProp").html($("#goodsProp").html());
}

//非空判断
function BIsNullVal(value) {
	return typeof (value) == "undefined" || value == null || (typeof (value) == "string" && (value == "undefined" || value == "" || value == "null")) || (typeof (value) == "boolean" && value == false);
}

