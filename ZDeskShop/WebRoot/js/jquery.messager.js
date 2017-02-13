/**
	滑动消息提示框
	注：使用时设置 位置 样式 top:0上边 bottom:0下边  left:0左边  right:0右边      结合使用实现 上左 上右 下左 下右四个方向位置
	
	id 消息提示框id
	width 宽度
	height 高度
	title 标题
	text 消息内容
	closeTime 关闭时间 毫秒
	type 滑入动画效果类型   slide上下   fade淡入淡出  show从角落显示隐藏  leftOrRight左右
	speed 动画播放时间 毫秒
	className 自定义class样式  多个用空格分开 如：alert alert-info
	
*/
(function(jQuery) {
	//初始数字
	try{
		if(num!=undefined&&num!=''){
			num=50;
		}
	}catch(e){
		num=50;
	}
	this.timer1=null;
	//显示消息提示框方法
	this.show = function(p) {
		//初始化参数
		var settings = {
			width:200,
			height:150,
			title:'信息提示',
			closeTime:4000,
			speed:600
		};
		p = $().extend(settings,p);
		var id;
		if(p.id!=undefined&&p.id!=null){
			id=p.id;
		}else{
			p.id="message"+num;
			id = p.id;
		}
		//向body内写入提示框
		this.inits(p);
		//设置样式
		this.setClass(p);
		//动画效果类型
		switch (p.type) {
		case 'slide'://上下
			$("#"+id).slideDown(p.speed);
			break;
		case 'fade'://淡入
			$("#"+id).fadeIn(p.speed);
			break;
		case 'show'://从一角慢慢展开
			$("#"+id).show(p.speed);
			break;
		case 'leftOrRight'://左右
			$("#"+id).animate({width:"show",opacity:"show"},p.speed);	
			break;	
		default:
			$("#"+id).slideDown(p.speed);
			break;
		}
		var bottomHeight =  "-"+document.documentElement.scrollTop;
		$("#"+id).css("bottom", bottomHeight + "px");
		this.rmmessage(p);
		p={};
	};
	//写入提示框代码方法
	this.inits = function(p) {
		$("body").prepend('<div id='+p.id+' class="alert" style="z-index:'+num+'; width:'
		+p.width
		+'px; height:'
		+p.height
		+'px; position:absolute; display:none; bottom:0; right:0; overflow:hidden;"><b id="title">'
		+p.title+'</b><button id="message_close" type="button" class="close">&times;</button><hr style=" margin:0; padding:0;"><span id="contnet">'+p.text+'</span></div>');
		$("#message_close").click( function() {
			setTimeout('this.closeMsg('+JSON.stringify(p)+')', 1);
		});
		//鼠标移入停止关闭提示框，移出等待指定时间关闭提示框
		$("#"+p.id).hover( function() {
			clearTimeout(timer1);
			timer1 = null;
		}, function() {
			if (p.closeTime > 0)
				timer1 = setTimeout('this.closeMsg('+JSON.stringify(p)+')', p.closeTime);
		});
		//消息提示框随滚动条滚动，固定在指定位置
		$(window).scroll(
				function() {
					var bottomHeight =  "-"+document.documentElement.scrollTop;
					$("#"+id).css("bottom", bottomHeight + "px");
				});
		num++;
	};
	//判断是否手动关闭提示框方法 closeTime=0手动关闭
	this.rmmessage = function(p) {
		if (p.closeTime > 0) {
			timer1=setTimeout('this.closeMsg('+JSON.stringify(p)+');',p.closeTime);
		}
	};
	//关闭消息提示框方法
	this.closeMsg = function(p) {
		switch (p.type) {
		case 'slide'://左右
			$("#"+p.id).slideUp(p.speed);
			break;
		case 'fade'://淡出
			$("#"+p.id).fadeOut(p.speed);
			break;
		case 'show'://向角落
			$("#"+p.id).hide(p.speed);
			break;
		case 'leftOrRight'://左右
			$("#"+p.id).animate({width:"hide",opacity:"hide"},p.speed);
			break;	
		default:
			$("#"+p.id).slideUp(p.speed);
			break;
		};
		setTimeout('$("#'+p.id+'").remove()',p.speed);
	}
	//设置自定类样式名称 多个用空格隔开 如:alert alert-info
	this.setClass=function(p){
		$("#"+p.id).addClass(p.className);
	};
	jQuery.messager = this;
	return jQuery;
})(jQuery);