var url = "/" + PRJ_PATH + "/" + ZDesk_ROU + "/OD/";
var orderGroupNumber1 = '';
var userAccount = "1";
$(document).ready(function() {
			orderGroupNumber1 = getUrlParam('orderGroupNumber');
			if(orderGroupNumber1==""||orderGroupNumber1==undefined){
				return;
			}
			userAccount=getUrlParam('userAccount');
			select();
			loaded();
		});
// init data
function select() {
	try {
		popShade(1);
		var data = {
			"orderId" : orderGroupNumber1,
			'buyersUser' : userAccount
		}
		ajaxFunction(url + "getOrderInPhones.action", data, true,
				function(list) {
					for (var i = 0; i < list.data.data.length; i++) {
						$("#buyersName").text(list.data.data[i]["buyersName"]);
						$("#buyersPhone").text(list.data.data[i]["buyersPhone"]);
						$("#orderNumber").text(list.data.data[i]["orderNumber"]);
						$("#orderState").text(list.data.data[i]["keyValue"]);
						$("#buyersAddress").text(list.data.data[i]["buyersAddress"]);
						$("#orderTime").text(list.data.data[i]["orderTime"]); 
						$("#orderPrice").text("￥"+list.data.data[i]["orderPrice"]);
						$("#buyersMessage").text(list.data.data[i]["buyersMessage"]);
						for(var j=0;j<(list.data.data[i]["subset"].length||0);j++){
							initOrderGoodsText(list.data.data[i]["subset"][j]);
						}
					}
					popShade(-1);
					upMyScroll();
				});
	} catch (e) {
		popShade(-1);
	}
}
function initOrderGoodsText(data){
	var str='<div class="goodsText divFlotLeft">'
					+'	<div class="goodsImg divFlotLeft">'
						+'	<img alt="图片暂无"'
							+'	src="'+data.goodsImgs.split(",")[0]+'">'
						+'</div>'
						+'<div class="goodsNamePrice divFlotLeft">'
						+'	<div class="goodsName overflowHidden divFlotLeft">'+data.goodsName+'</div>'
						+'	<div class="goodsPrice divFlotLeft colorFFD5702 ">'
						+'		￥<span id="goodsPrice-'+data.id+'">'+data.price+'</span>'
						+'	</div>'
						+'</div>'
					    +'	<div class="goodsPropNumber divFlotLeft">'
						+'	<div id="goodsProp-'+data.id+'" class="goodsProp text_overflow4 text_overflow divFlotLeft">'+data.goodsProp+
						'<br>'+data.goodsCotegory+'	</div>'
						+'	<div class="goodsNumber divFlotLeft">'
						+'		x<span id="goodsNumber-'+data.id+'">'+data.numbers+'</span>'
						+'	</div>'
					+'	</div>'
					+'</div>';
	$("#goodsDiv").append(str);
}