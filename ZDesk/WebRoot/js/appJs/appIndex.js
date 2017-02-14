$(document).ready(function(){
	$(window).bind("resize",function(){
		app_css_index_content_left_reheight();
	});
	$(window).resize();
});

/**
 * 当页面大小发生改变时触发此函数，
 * 以改变left与leftmain的高度
 * 此函数在页面初始化时与window的resize事件绑定
 * @param {} p
 */
function app_css_index_content_left_reheight(p){
	var re=U_reCssHeight({
		src:'app_css_index_content',
		dist:'tab-content',
		fix:'-40'
	});
	var re=U_reCssHeight({
		src:'app_css_index_content',
		distParent:'tab-content',
		distParentType:'css',
		dist:'active',
		fix:'-40'
	});
}