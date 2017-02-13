//加载所有商品
	function loadGoods() {
		var params = {};
		
		if(!BIsNullVal($("#sou").val())){
			params.goodsTitle = $("#sou").val();
		}
		
		 params.flag = "detail"; 
		params.isMobile = "isMobile";
		params.rows=5;
		params.offset= parseInt((count - 1) * params.rows);
		ajaxFunction("/" + parent.PRJ_PATH + "/ZKM/goodsManager/selectMainGoods.action", params, false, function (data) {
			var str = "";
			if (data && data.data) {
				for (var i = 0; i < data.data.length; i++) {
					str += "<dl><dt><a href='javascript:void(0);' onclick=repost('"+data.data[i].id+"');> " +
							"<img class='lazy'  src='" + data.data[i].goodsImgs.split(",")[0] + "' /> </a>" + 
							"</dt><dd><a href='javascript:void(0);' onclick=repost('"+data.data[i].id+"');>"+
							"<h4>" + data.data[i].goodsTitle + "</h4><p class='promTags'></p><p class='price'>"+
							"<strong>&yen;" + data.data[i].goodsSalePrice + "</strong>"; 
									//如果没有市场价，不显示
							if(!BIsNullVal(data.data[i].goodsShowPrice) && data.data[i].goodsShowPrice != 0){
								str += "<del>&yen;" + data.data[i].goodsShowPrice + "</del>"; 
							}
							str +="</p></a>" +  
							//"<a class='cartBtn'  href='javascript:void(0);' onclick=location='demo.html' >+</a>" + 
							//"<div class='ins'><p></p><a href='#' class='goBuy'></a>" + 
							//"<a href='#'></a></div>"+
							"</dd></dl>";
							
				}
				$("#allGoods").append(str);
				if((parseInt(params.offset) + parseInt(params.rows)) >= data.total){
					$("#loadMore").css("display","none");
				}else{
					$("#loadMore").css("display","block");
				}
			}
		});	
	}

	//点击加载更多
	function loadMoreGoods(){
		count++;
		loadGoods();
	}
	
	//查询时
	function searchGoods(){
		count = 1;  // 重置分页计数；
		$("#allGoods").html('');
		loadGoods();
	} 
	
	//跳转至详情
	function repost(data){
	 	location="detail.html?groupId="+data;
	}

	function BIsNullVal(value) {  
	 	return typeof(value)=="undefined" || value==null ||  (typeof(value)=='string' &&(value=="undefined" || value=='' || value=='null' )) ||  (typeof(value)=='boolean' &&value==false  );
	};
