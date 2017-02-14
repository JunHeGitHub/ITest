window.onload = function(){
	$('#topMenu3x5 ul li').each(function(j){
		$('#topMenu3x5 ul li').eq(j).removeClass("on");
		$('#topMenu3x5 ul li span').eq(j).animate({top:-$('#topMenu3x5 ul li span').eq(j).height()},100);
	});

	$('#topMenu4x5 ul li').each(function(j){
		$('#topMenu4x5 ul li').eq(j).removeClass("on");
		$('#topMenu4x5 ul li span').eq(j).animate({top:-$('#topMenu4x5 ul li span').eq(j).height()},100);
	});

	$('#bottomMenu3x5 ul li').each(function(j){
		$('#bottomMenu3x5 ul li').eq(j).removeClass("on");
		$('#bottomMenu3x5 ul li span').eq(j).animate({bottom:-$('#bottomMenu3x5 ul li span').eq(j).height()},100);
	});

	$('#bottomMenu4x5 ul li').each(function(j){
		$('#bottomMenu4x5 ul li').eq(j).removeClass("on");
		$('#bottomMenu4x5 ul li span').eq(j).animate({bottom:-$('#bottomMenu4x5 ul li span').eq(j).height()},100);
	});
}

$('#topMenu3x5 ul li').each(function(i){
	$(this).click(function(){
		if($(this).attr("class")!="on"){
			$('#topMenu3x5 ul .on span').animate({top:-$('#topMenu3x5 ul .on span').height()},200);
			$('#topMenu3x5 ul .on').removeClass("on");
			$(this).addClass("on");
			$('#topMenu3x5 ul li span').eq(i).animate({top:45},200);
			$('.footer_front').show();
		}else{
			$('#topMenu3x5 ul li span').eq(i).animate({top:-$('#topMenu3x5 ul li span').eq(i).height()},200);
			$(this).removeClass("on");
			$('.footer_front').hide();
		}
	});
});

$('#topMenu4x5 ul li').each(function(i){
	$(this).click(function(){
		if($(this).attr("class")!="on"){
			$('#topMenu4x5 ul .on span').animate({top:-$('#topMenu4x5 ul .on span').height()},200);
			$('#topMenu4x5 ul .on').removeClass("on");
			$(this).addClass("on");
			$('#topMenu4x5 ul li span').eq(i).animate({top:45},200);
			$('.footer_front').show();
		}else{
			$('#topMenu4x5 ul li span').eq(i).animate({top:-$('#topMenu4x5 ul li span').eq(i).height()},200);
			$(this).removeClass("on");
			$('.footer_front').hide();
		}
	});
});

$('#bottomMenu3x5 ul li').each(function(i){
	$(this).click(function(){
		if($(this).attr("class")!="on"){
			$('#bottomMenu3x5 ul .on span').animate({bottom:-$('#bottomMenu3x5 ul .on span').height()},200);
			$('#bottomMenu3x5 ul .on').removeClass("on");
			$(this).addClass("on");
			$('#bottomMenu3x5 ul li span').eq(i).animate({bottom:35},200);
			$('.footer_front').show();
		}else{
			$('#bottomMenu3x5 ul li span').eq(i).animate({bottom:-$('#bottomMenu3x5 ul li span').eq(i).height()},200);
			$(this).removeClass("on");
			$('.footer_front').hide();
		}
	});
});

$('#bottomMenu4x5 ul li').each(function(i){
	$(this).click(function(){
		if($(this).attr("class")!="on"){
			$('#bottomMenu4x5 ul .on span').animate({bottom:-$('#bottomMenu4x5 ul .on span').height()},200);
			$('#bottomMenu4x5 ul .on').removeClass("on");
			$(this).addClass("on");
			$('#bottomMenu4x5 ul li span').eq(i).animate({bottom:35},200);
			$('.footer_front').show();
		}else{
			$('#bottomMenu4x5 ul li span').eq(i).animate({bottom:-$('#bottomMenu4x5 ul li span').eq(i).height()},200);
			$(this).removeClass("on");
			$('.footer_front').hide();
		}
	});
});

$('.footer_front').click(function(){
	$('#topMenu3x5 ul .on span').animate({top:-$('#topMenu3x5 ul .on span').height()},200);
	$('#topMenu3x5 ul .on').removeClass("on");

	$('#topMenu4x5 ul .on span').animate({top:-$('#topMenu4x5 ul .on span').height()},200);
	$('#topMenu4x5 ul .on').removeClass("on");

	$('#bottomMenu3x5 ul .on span').animate({bottom:-$('#bottomMenu3x5 ul .on span').height()},200);
	$('#bottomMenu3x5 ul .on').removeClass("on");

	$('#bottomMenu4x5 ul .on span').animate({bottom:-$('#bottomMenu4x5 ul .on span').height()},200);
	$('#bottomMenu4x5 ul .on').removeClass("on");
	
	$(this).hide();
});