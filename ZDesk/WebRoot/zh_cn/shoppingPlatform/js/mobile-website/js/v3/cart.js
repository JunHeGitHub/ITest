/**
 * 列表页添加普通商品、积分商品到购物车
 * 
 * @param pmId
 * @param buyNum
 * @param isPoint(false:普通商品 true:积分商品)
 */
function addcart(pmId, buyNum, isPointProduct) {
	var now = new Date();
	$(".ins p").text("");
	var needpoint = 0;
	if(isPointProduct=='true'){
		needpoint = 1;
	}
	if(buyNum==0){
		buyNum=1;
	}
	/*commonStatistics('', 'buttonPosition', 'addcart', '');
	commonStatistics("","pmId",pmId,"");*/
	addcartStatistics(pmId);
	$.ajax({
		type : "POST",
		url : "../../../../buyproduct/" + pmId + "/" + buyNum + "?now=" + now
				+ "&needpoint=" + needpoint,
		data : "",
		dataType : "",
		success : function(backdata) {
			$(".ins p").text(backdata);
			if (backdata == '添加购物车成功!') {
				fAddCartNum(); // 加入成功后数量才变化
			}else if(backdata == "请登录"){
				window.location.href = "../../../../login";
			}
		}
	});

}