var b=true;

function leftNavigation(btn_img,left,right){
	/*	$("#btn").hide();
	$("#"+left+"").mouseover(function(){
		if(b){
		$("#btn").show();
		}
	});
	$("#"+right+"").mouseover(function(){
		if(b){
		$("#btn").hide();
		}
	});
	*/

		
		//$("#"+left+"").appezndTo("<div>dssdf</div>");
		$("#"+right+"").addClass("app_css_index_content_main1");
		$("#"+left+"").addClass("app_css_index_content_left1");
		$("#btn").addClass("LeftBtn");
		$("#"+btn_img+"").addClass("btn_close");
		$("#"+left+"").animate({paddingLeft:290});
		
		$("#btn").css("cursor","pointer").click(function(){
		if(b){
			$("#"+left+"").animate({paddingLeft:0});
			$("#"+btn_img+"").removeClass("btn_close");
			$("#"+btn_img+"").addClass("btn_open");
			b=false;
		}else{
			$("#"+left+"").animate({paddingLeft:290});
			$("#"+btn_img+"").removeClass("btn_open");
			$("#"+btn_img+"").addClass("btn_close");
			b=true;
		}
	})
}