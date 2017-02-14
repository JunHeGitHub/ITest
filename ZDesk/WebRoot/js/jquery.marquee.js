/**
 *  参数说明
 * 	data 跑马灯显示要显示的内容 格式JSON[{"key":"value"},{"key":"value"}],{"key":"value"},"普通文本"
 *    JSON格式下的参数
 *    content:'内容',conId:'内容id',href:'超链接',onClick:'点击事件',oneStyle:'设置单个内容的样式',target超链接打开窗口方式 
 *    content必填,conId不填时默认从con0开始 数字自增,href,onClick,oneStyle,target不填时为''
 *    
 *  direction 方向 left默认  right up down
 *	behavior 行为 scroll重复默认 alternate左右反复 slide到尽头停   不建议设置，使用默认即可
 *	scrollamount 速度 数字 越大越快
 *	mouseEvent 鼠标移入悬停事件  true false
 *  gap 设置多条文本的间隔距离 数字格式 默认100
 *  height;跑马灯高度  数字
 *  width;跑马宽度  数字
 *  bgColor;跑马灯背景颜色
 *  conClass;添加marquee子标签class样式
 *  marqueeClass;添加marqueen class样式
 */
;(function($) {
	try{
		if(!U_object_isNotNull(com_bootStarpTab_genTabIdNum)){
			com_bootStarpTab_genTabIdNum=0;
		}
	}catch(e){
		com_bootStarpTab_genTabIdNum=0;
	}
	$.fn.extend({
		/**
		 * 创建跑马灯 
		 * */
		"marquee":function(p){
			var settings={
				 mouseEvent:"true",//鼠标移入悬停事件
				 gap:100 //间距
			};
			p=$().extend(settings,p);
			
			var direction = p["direction"];//方向
			var behavior = p["behavior"];//行为
			var scrollamount = p["scrollamount"];//速度
			var mouseEvent=p["mouseEvent"];
			var gap = p["gap"];//多条内容间隔
			var height = p["height"];//跑马灯高度
			var width = p["width"];//跑马灯宽度
			var bgColor=p["bgColor"];//跑马灯背景颜色
			var conClass=p["conClass"];//marquee表现子标签样式
			var marqueeClass=p["marqueeClass"];//marquen标签样式，设置普通文本
			this.html("<marquee></marquee>");
			var marqueeStr =this["selector"]+" marquee";
			var $marquee = $(marqueeStr);
			this.appendContent(p);	
			if(direction=='up'||direction=='right'||direction=='down'||direction=='left'){
				$marquee.attr('direction',direction);
			}
			if(behavior=='scroll'||behavior=='alternate'||behavior=='slide'){
				$marquee.attr('behavior',behavior);
			}
			if(scrollamount!=''&&scrollamount!=undefined&&/^[0-9]+$/.test(scrollamount)){
				$marquee.attr("scrollamount",scrollamount);
			}
			
			if(mouseEvent=='true'||mouseEvent==true){
				$marquee.attr("onmouseover","this.stop()");
				$marquee.attr("onmouseout","this.start()");
			}
			
			if(/^[0-9]+$/.test(gap)){
				$(marqueeStr+" a").mouseenter(function(){
						$(marqueeStr+" a").css("cursor","pointer");
				});
			}
			if(/^[0-9]+$/.test(height)){
				$marquee.attr("height",height);
			}
			if(/^[0-9]+$/.test(width)){
				$marquee.attr("width",width);
			}
			
			if(/^#[a-fA-F\d]{6}$/.test(bgColor)||/^[a-z]+/.test(bgColor)){
				$marquee.attr("bgColor",bgColor);
			}
			if(U_object_isNotNull(marqueeClass)){
				$marquee.addClass(marqueeClass);
			}
			if(U_object_isNotNull(conClass)){
				$marquee.children().addClass(conClass);
			}
		},
		/**
		 * 根据id、index删除内容
		 */
		"deleteMarqueeData":function(p){
			var marqueeStr =this["selector"]+" marquee";
				//查找marqyee下的所有子节点，并把与index相等的数据删除
				if(p.index!=undefined&&p.index!=''){
					var n=$(marqueeStr).children("a:eq("+p.index+")").remove();
					n.next().remove();
					return;
				}
				//删除与id匹配的内容
				if(p.id!=undefined&&p.id!=''){
					var n=$(marqueeStr+" #"+p.id).remove();
					n.next().remove();
					return;
				}
				//没有子节点删除marquee跑马灯
				if($(marqueeStr).children().length==0){
					$(marqueeStr).remove();
					return;
				}
			}
		,
		/**
		 * 添加数据
		 */
		"appendContent":function(p){
			var settings={
				 gap:100 //间距
			};
			p=$().extend(settings,p);
			var data = p["data"];//JSON格式数据或普通文本
			var writeCon='';//写入marquee标签中的内容
			var marqueeStr =this["selector"]+" marquee";
			var $marquee = $(marqueeStr);
			var gap = p["gap"];
			//判断是否为json格式数据
			if(/^\{["'].*["']:["'].*["']\}$/.test(JSON.stringify(data))||/^\[(\{["'].*["']:["'].*["']\},?)+\]$/.test(JSON.stringify(data))){
				var conId;//内容id
				var oneStyle;//单个内容样式
				var href;//超链接地址
				var onClick;//点击事件
				var target;//超链接打开窗口方式
				if(data.length!=''&&data.length!=undefined){
					//多条JSON格式数据
					for(var i=0;i<data.length;i++){
						if(U_object_isNotNull(data[i].conId)){
							conId=data[i].conId;
						}else{
							conId="con"+com_bootStarpTab_genTabIdNum;
							com_bootStarpTab_genTabIdNum++;
						}
						if(U_object_isNotNull(data[i].href)){
							href=data[i].href;
						}else{
							href='javascript:void(0);';
						}
						if(U_object_isNotNull(data[i].onClick)){
							onClick=data[i].onClick;
						}else{
							onClick='javascript:void(0);';
						}
						if(U_object_isNotNull(data[i].oneStyle)){
							oneStyle=data[i].oneStyle;
						}else{
							oneStyle='';
						}
						if(U_object_isNotNull(data[i].target)){
							target=data[i].target;
						}else{
							target='_top';
						}
						writeCon="<a id='"+conId+"'>"+data[i].content+"</a><i style='display: inline;padding-left:"+p.gap+"px;'></i>";
						$marquee.append(writeCon);
						$(marqueeStr+" #"+conId).attr("href",href).attr("onClick",onClick).attr("style",oneStyle).attr("target",target);
					}
				}else{
						//一条JSON格式数据
						if(U_object_isNotNull(data["conId"])){
							conId=data["conId"];
						}else{
							conId="con"+com_bootStarpTab_genTabIdNum;
							com_bootStarpTab_genTabIdNum++;
						}
						if(U_object_isNotNull(data["href"])){
							href=+data["href"];
						}else{
							href='javascript:void(0);';
						}
						if(U_object_isNotNull(data["onClick"])){
							onClick=data["onClick"];
						}else{
							onClick='';
						}
						if(U_object_isNotNull(data["oneStyle"])){
							oneStyle=data["oneStyle"];
						}else{
							oneStyle='';
						}
						if(U_object_isNotNull(data["target"])){
							target=data["target"];
						}else{
							target='_top';
						}
						writeCon="<a id='"+conId+"'>"+data["content"]+"</a><i style='display: inline;padding-left:"+p.gap+"px;'></i>"
						$marquee.append(writeCon);
						$(marqueeStr+" #"+conId).attr("href",href).attr("onClick",onClick).attr("style",oneStyle).attr("target",target);
				}
			}else{
				//普通文本
				writeCon=data+"<i style='padding-left:"+gap+"'></i>";
				$marquee.append(writeCon);
			}
		},
		/**
		 * 修改数据，只修改文本内容
		 */
		"updataContent":function(p){
			var settings={
				 mouseEvent:"true",//鼠标移入悬停事件
				 gap:100 //间距
			};
			p=$().extend(settings,p);
			var marqueeStr =this["selector"]+" marquee";
			var $marquee = $(marqueeStr);
			var gap = p["gap"];
			//普通文本
			var marqueeStr =this["selector"]+" marquee";
				//查找marqyee下的所有子节点，并把与index相等的数据修改
				$(marqueeStr).children("a:eq("+p.index+")").html(p.content+"<i style='padding-left:"+gap+"'></i>");
				//修改与id匹配的内容
				$(marqueeStr+" #"+p.id).html(p.content+"<i style='padding-left:"+gap+"'></i>");
			}
	});
})(jQuery);