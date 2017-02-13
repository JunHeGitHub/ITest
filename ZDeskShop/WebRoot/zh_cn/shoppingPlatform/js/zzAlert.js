/**
 * css 文件 暂未分离 放置于 demo.css ************手写遮罩****************
 * 
 */
var zz = 0;
var zzStr = "<div class='zz'></div>";
// zzi 添加遮罩为+1 消除为-1 清除多次遮罩 加大负数值
function popShade(zzi) {
	zz -= -(zzi);
	$(".zz").fadeIn();
	if (zz <= 0) {
		zz = 0;
		$(".zz").removeClass("hi");
		$(".zz").fadeOut();
	}
}
var myScroll, pullUpEl, pullUpOffset;
function loaded() {
	myScroll = new IScroll('#wrapper', {
				Usetransition : false
				// 取消 onclinc 遮挡
				//preventDefault : false
			});
}
//滚动内容发生变动后调用
function upMyScroll(){
	$("#scroller").append("<div class='containerSon divFlotLeft' id='upMyScroll'>-</div>");
	myScroll.refresh();
	$("#upMyScroll").remove();
}

function zzBack() {
	window.history.back(-1);
}
// alert
function popoAlert(str, fn) {
	popShade(1);
	if ($("#alertzz").length <= 0) {
		var alertHtml = '	<div id="alertzz" class="alertzz">'
				+ '<div id="alertStr" class="alertStr" onclick="">' + str
				+ '</div>' + '	<div class="gogn"  >继续</div>'
				+ '<div class="alertBotton " onclick="closerAlert()">关闭</div>'
				+ '</div>';
		$(document.body).append(alertHtml);
	} else {
		$("#alertStr").html(str);

	}
	$(".gogn").click(function() {
				try {
					if ((fn)() != false) {
						// 取消绑定
						$(".gogn").unbind("click");
						closerAlert();
					} else {
					}
				} catch (e) {
					closerAlert();
					// 取消绑定
					$(".gogn").unbind("click");
				}
			});
	$("#alertzz").fadeIn();
}
function closerAlert() {
	popShade(-1);
	$(".gogn").unbind("click");
	$("#alertzz").hide();
}
/** ***********--手写遮罩--**************** */

function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); // 构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg); // 匹配目标参数
	if (r != null) {
		try {
			return decodeURIComponent(r[2]);
		} catch (e) {
			return r[2] || ''; // 返回参数值
		}
		/* return decodeURIComponent(decodeURI(r[2])); */
	}

}