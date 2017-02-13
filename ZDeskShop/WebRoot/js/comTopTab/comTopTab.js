/**
 * TODO:还存在几个问题，：
 * 		1、目前没到达组件级包装的程度，组件级包装需要重构代码。
 * 
 * 		2、现在JS一载入时就启用了拖拽，应该由参数确定，但需要将功能进一步组件化才能实现。
 * 
 * 		3、初始化页面内手写的TAB标签时，可以进行show与shown事件绑定，但也需要进行组件化才能实现。否则代码耦合性太高
 * 
 * 		以上1、2、3应该进行jquery组件形式的封装，便于以$().topTab()的形式进行初始化与调用。
 * 		目前尽量以代码的形式进行TAB的添加，以便于事件的绑定。
 * 		
 * 		4、可以进一步扩展tab文字前放置一个图标或什么的。
 * 
 * 		5、将已经写好的DIV传给addTab进行显示(目前是传入DIV的内容，这个在实际使用中不太实用)
 * 
 * 	依赖：
 * 		基于bootstrap 的tab完成
 * 		bootstrap
 * 		jquery
 * 		jquery.mousewheel 鼠标滚轮插件
 * 		jquery.dragsort	
 * 
 * 	说明：
 * 		完成功能：
 * 			宽度自适应
 * 			标签超出长度时，出现左右箭头，用来移动标签
 * 			标签宽度可以参数定义
 * 			标签是否可以关闭可以参数定义
 * 			标签关闭模式分为隐藏与移除两种
 * 			标签可以拖拽摆放
 * 			标签多时（超长、超多）时可以使用滚轮左移，右移
 * 			被激活或添加标签显示即：
 * 				已经存在的标签，再次被触发时，若不在显示区域则定位显示该标签。
 * 				新添加标签在topTab末尾添加，添加后定位显示
 * 
 */

/**
 * 组件的初始事件及事件绑定
 */

var COM_TOP_TAB_JS_CACHER={
	
};

/**
 * 设置缓存
 * @param {} p
 * @param {} isCover
 */
function com_bootStarpTab_setCacher(p,isCover){
	if(!U_object_isNotNull(isCover)){
		isCover=true;
	}
	if(U_object_isNotNull(p)){
		var tabid=p["tabId"];
		if(U_object_isNotNull(tabid)){
			if(isCover){
				COM_TOP_TAB_JS_CACHER[tabid]=p;
			}else{
				var ctc=COM_TOP_TAB_JS_CACHER[tabid];
				if(!U_object_isNotNull(ctc)){
					COM_TOP_TAB_JS_CACHER[tabid]=p;
				}
			}
		}
	}
}
/**
 * 获取缓存
 * @param {} tabid
 * @return {}
 */
function com_bootStarpTab_getCacher(tabid){
	if(U_object_isNotNull(tabid)){
		return COM_TOP_TAB_JS_CACHER[tabid];
	}
}
/**
 * 清理缓存
 * @param {} tabid
 */
function com_bootStarpTab_cacherClear(tabid){
	if(U_object_isNotNull(tabid)){
		COM_TOP_TAB_JS_CACHER[tabid]=null;
		
	}
}

$(document).ready(function(){
	$(".com_css_topTab").each(function(i,el){
		//绑定页面中所的TAB的滚轮事件
		$(el).bind("mousewheel",function(event, delta){
			 var dir = delta > 0 ? 'up' : 'down',vel = Math.abs(delta);
			 var el=event.target;
			 var tabel=$(el).parents(".com_css_tomTab_tabBar")[0];
			 var cel="";
			 if(dir=="up"){
			 	cel=$(tabel).children(".com_css_topTab_leftButton").children("a")[0];
			 }
			 if(dir=='down'){
			 	cel=$(tabel).children(".com_css_topTab_rightButton").children("a")[0];
			 }
			 $(cel).click();
			 return false;
		});
		//计算初始ul宽度
		var initUlWidth=0;
		$(el).find("li").each(function(i,elc){
			var w=$(elc).css("width");
			initUlWidth+=parseInt(w);
		});
		initUlWidth+=10;
		$(el).children("ul").css("width",initUlWidth);
		//绑定拖拽功能
		$(el).children("ul").dragsort();
	});
	//绑定window resize事件
	$(window).bind("resize",function(){
		$(".com_css_topTab").each(function(i,el){
			var tabel=$(el).parents(".com_css_tomTab_tabBar")[0];
			com_bootStarpTab_check_leftAndRightShow(tabel);
		});
	});
});

/**
 * 验证是否打开移与右移
 * @param {} el 参数为事件触发点TAB子节点的class=.com_css_tomTab_tabBar
 */
function com_bootStarpTab_check_leftAndRightShow(el){
	var ul=$(el).find("ul")[0];
	var ulw=parseInt($(ul).css("width"));
	var elw=parseInt($(el).css("width"));
	if(ulw>=elw){
		$(el).children(".com_css_topTab_leftButton").show();
		$(el).children(".com_css_topTab_rightButton").show();
	}else{
		$(el).children(".com_css_topTab_leftButton").hide();
		$(el).children(".com_css_topTab_rightButton").hide();
	}
}

/**
 * 左移按钮事件
 * @param {} tabId
 */
function com_bootStarpTab_moveLeft(tabId){
	U_jquery_scrollLeft_moveTo({
		el:$("#" +tabId).parent()[0],
		mode:"++",
		pos:-80
	});
}

/**
 * 右移按钮事件
 * @param {} tabId
 */
function com_bootStarpTab_moveRight(tabId){
	U_jquery_scrollLeft_moveTo({
		el:$("#" +tabId).parent()[0],
		mode:"++",
		pos:80
	});
}
// 遮罩属性定义
var maskContent = {
		css : {
			border : 'none',
			padding : '15px',
			backgroundColor : '#000',
			'-webkit-border-radius' : '10px',
			'-moz-border-radius' : '10px',
			opacity : .5,
			color : '#fff',
			font : '14px'
		},
		message : '正在加载中，请稍候...'
	};

/**
 * 添加一个TAB
 * @param {} p 参数
 * @return {String} 返回执行结果
 */
function com_bootStarpTab_addTab(p){
	window.top.$.blockUI(maskContent);
	try{
		if(!U_object_isNotNull(com_bootStarpTab_genTabIdNum)){
			com_bootStarpTab_genTabIdNum=0;
		}
	}catch(e){
		com_bootStarpTab_genTabIdNum=0;
	}
	var settings={
		//容器类型[div iframe]
		contentType:'iframe',
		//contentType为iframe时src
		iframeSrc:'',
		//contentType为div时div的内容
		divContent:'',
		//生成的标签控件ID,若不指定ID，则按规则生成，规则：tabComId + genTabIdNum
		tabId:'',
		//生成的标签名称
		tabName:'',
		//标签控件的ID
		tabComId:'',
		//标签控件容器ID
		tabComContentId:'',
		//生成的标签是否可以被关闭
		hasClose:true,
		//关闭标签的关闭模式[remove hide] 移除与隐藏
		closeMode:'remove',
		//生成的标签是否即时激活
		hasActive:true,
		//对应bootstarp的show事件
		show:'',
		//对应bootstarp的shown事件
		shown:'',
		tabStyle:'width:80px',
		//在建立与移除标签时修正标签宽度防止串行
		fixWidth:44,
		//不需要设置，这个属性是用来记录生成的tab ico元素ID的
		icoElementId:'',
		//不需要设置，这个属性是用来记录生成的tab 文字元素ID的
		textElementId:'',
		//不需要设置，这个属性是用来记录生成的tab 关闭按钮元素ID的
		closeElementId:'',
		//关闭之后回调方法
		closeFn:'',
	    //创建之前调用 return true 或 false
	    beforeCreateFn:'',
		/*tab图标设置
		 * icoSettings:{
		 *  //图标的class
		 * 	icoCss:'',//必填
		 *  //显示图标区域的style设置
		 *  style:'',//非必填
		 *  //图标是否闪烁
		 *  flashing:false,//非必填
		 *  //图标闪烁使用的方法
		 *  flashingFunction:com_bootStarpTab_defaultFlashing,//非必填，方法可以自写
		 *  //闪烁时间设置
		 *  flashingInt:500//非必填
		 * }
		 */
		icoSettings:''
	};
	p=$().extend(settings,p);
	var tabName=p["tabName"];
	var tabComId=p["tabComId"];
	var tabComContentId=p["tabComContentId"];
	var contentType=p["contentType"];
	var iframeSrc=p["iframeSrc"];
	var divContent=p["divContent"];
	var beforeCreateFn=p["beforeCreateFn"];
	var closeFn=p["closeFn"];
	if(tabName!='' && tabComId !='' && contentType!=''){
		if(contentType!='iframe' && contentType!='div'){
			return "com_bootStarpTab_addTab param is error [contentType] is not in iframe or div ";
		}
		if(contentType=="iframe" && iframeSrc==''){
			return "com_bootStarpTab_addTab param is error [contentType iframeSrc] iframe must be have iframeSrc value ."
		}else if(contentType=="div" && divContent==''){
			return "com_bootStarpTab_addTab param is error [contentType divContent] DIV must be have divContent value ."
		}
		var tabCom=$("#"+ tabComId)[0];
		if(!U_object_isNotNull(tabCom)){
			return "com_bootStarpTab_addTab param is error [tabComId] is not find element";
		}
		//若不设定tabComContentId则默认为 tabComId + _content
		if(tabComContentId==''){
			tabComContentId=tabComId + "_content";
		}
		var tabComContent=$("#" +tabComContentId)[0];
		if(!U_object_isNotNull(tabComContent)){
			return "com_bootStarpTab_addTab param is error [tabComContentId] is not find element";
		}
		
		var tabId=p["tabId"];
		var hasActive=p["hasActive"];
		var hasClose=p["hasClose"];
		var closeMode=p["closeMode"];
		if(tabId==''){
			tabId=tabComId +"_" + (com_bootStarpTab_genTabIdNum++);
		}
		var tabContentId=tabId + "_content";
		
		p["tabId"]=tabId;
		p["tabContentId"]=tabContentId;
		//alert(tabId + "    " + tabContentId);
		
		var hasTabid=$("#" + tabId)[0];
		var hasTabContentid=$("#" + tabContentId)[0];
		//ID对应的元素已经存在则show否则，建立。
		var oldDisplay="";
		
		//创建tab前调用
		if(typeof beforeCreateFn=="function"){
		
		    var bool=beforeCreateFn(p);
		   
		   	
			//如果  beforeCreateFn 返回false 停止运行
			if(!bool){
			     window.top.$.unblockUI(maskContent);
			     return ;   
			}
		}
		if(U_object_isNotNull(hasTabid)){
			if(U_object_isNotNull(hasTabContentid)){
				var elParent=$(hasTabid).parent()[0].tagName;
				if(elParent=='UL'){
					$("#" + tabId + " a").tab("show");
				}else{
					var el=$(hasTabid).clone();
					//var elContent=$(hasTabContentid).clone();
					$(hasTabid).remove();
					//$(hasTabContentid).remove();
					el=$(tabCom).append(el);
					//$(tabComContent).append(elContent);
					$("#" + tabId + " a").tab("show");
					//重新计算并设置UL宽度，防止折行
					com_bootStarpTab_resizeTabWidth($("#" + tabId)[0]);
					//在hide时关闭按钮事件被移除，这里重新绑定
					var $closeBtn=$("#"+ tabId +"_closeButton");
					$closeBtn.data("tabId",tabId);
					$closeBtn.bind("click",function(){
					    var _tabId= $(this).data("tabId");
					    var _params=com_bootStarpTab_getCacher(_tabId);
					    var closeFn=_params["closeFn"]||"";
						com_bootStarpTab_removeTab_def(_params.tabId,_params.closeMode,closeFn);
					});
				}
			}else{
				//有标签，但没找到内容的先移除标签再重建
				$(hasTabid).remove();
				com_bootStarpTab_createTab(p);
			}
		}else{
			//新建TAB
			com_bootStarpTab_createTab(p);
		}
		//验证是否要打开左移与右移
		var tabel=$(tabCom).parents(".com_css_tomTab_tabBar")[0];
		com_bootStarpTab_check_leftAndRightShow(tabel);
		
		//移动滚动条至tab位置
		var scrollEl=$(tabel).children(".com_css_topTab")[0];
		var pos=$("#" + tabId).position().left;
		U_jquery_scrollLeft_moveTo({
			el:scrollEl,
			mode:"move",
			pos:pos
		});
		window.top.$.unblockUI(maskContent);
		return "com_bootStarpTab_addTab success.";
	}else{
		window.top.$.unblockUI(maskContent);
		return "com_bootStarpTab_addTab param is error [ tabName tabComId tabComContentId] is '' ."
	}
}

/**
 * 调整tab宽度，防止折行
 * @param {} el 参数为事件触发点TAB子节点的UL元素
 */
function com_bootStarpTab_resizeTabWidth(el){
	var fixWidth=20;
	var tabid=$(el).attr("id");
	//获取缓存的参数
	var tabp=com_bootStarpTab_getCacher(tabid);
	if(U_object_isNotNull(tabp)){
		fixWidth=tabp["fixWidth"];
	}
	var p=$(el).parent();
	var sw=$(el).find("span").css("width");
	var icon = $(el).find("#" + tabid +"_closeButton");
	var lw = parseInt(sw) + fixWidth;
	if(U_object_isNotNull(icon) && icon.css("width")){
		lw += parseInt(icon.css("width"));
	}
	var pw=p.css("width");
	pw=parseInt(pw) + parseInt(lw);
	p.css("width",pw);
	$(el).css("width",lw);
}
/**
 * 显示定位
 * @param {} el
 */
function com_bootStarpTab_positionFix(el){
	
}

/**
 * 设置TAB对象的显示图标
 */
function com_bootStarpTab_setIco(p){
	var settings={
		//标签的ID
		tabId:'',
		//标签显示图标设定的css名称
		icoCss:'',
		style:'width:16px;height:16px',
		//图标是否闪烁
		flashing:false,
		//图标闪烁使用的方法
		flashingFunction:com_bootStarpTab_defaultFlashing,
		flashingInt:500
	}
	p=$().extend(settings,p);
	var tabid=p["tabId"];
	if(U_object_isNotNull(tabid)){
		var tabp=com_bootStarpTab_getCacher(tabid);
		var icoid=tabp["icoElementId"];
		if(U_object_isNotNull(icoid)){
			var e=$("#" + icoid);
			var icss=p["icoCss"];
			var stl=p["style"];
			if(U_object_isNotNull(icss)){
				//$(e).attr("className",icss);
				//e[0].className=icss;
				document.getElementById(icoid).className=icss;
			}
			//$(e).attr("style",stl);
			e.style=stl;
			if(p["flashing"]){
				if(U_object_isNotNull(tabp["flashingObj"])){
					com_bootStarpTab_stopFlashing(tabid);
				}
				if(U_object_isNotNull(p["flashingFunction"]) && typeof(p["flashingFunction"])=="function"){
					var interval=window.setInterval(function(){
						p["flashingFunction"](tabid);
					},p["flashingInt"]);
					tabp["flashingObj"]=interval;
				}
			}
		}
		tabp["icoSettings"]=p;
		com_bootStarpTab_setCacher(tabp);
	}
}
/**
 * 标签图标闪烁使用的方法
 * @param {} p
 */
function com_bootStarpTab_defaultFlashing(tabid){
	 if(U_object_isNotNull(tabid)){
	 	var tabp=com_bootStarpTab_getCacher(tabid);
	 	if(U_object_isNotNull(tabp)){
	 		var icoid=tabp["icoElementId"];
		 	var icoset=tabp["icoSettings"];
		 	if(U_object_isNotNull(icoset)){
		 		if(icoset["flashFalg"] ==undefined || icoset["flashFalg"]==null || icoset["flashFalg"]==''){
			 		icoset["flashFalg"]=0;
			 	}
			 	if(icoset["flashFalg"]==0){
			 		icoset["flashFalg"]=1;
			 		//$("#" + icoid).attr("className","");
			 		document.getElementById(icoid).className='';
			 	}else{
			 		icoset["flashFalg"]=0;
			 		//$("#" + icoid).attr("className",icoset["icoCss"]);
			 		document.getElementById(icoid).className=icoset["icoCss"];
			 	}
			 	tabp["icoSettings"]=icoset;
			 	com_bootStarpTab_setCacher(tabp);
		 	}
	 	}
	 }
}
/**
 * 停止闪烁
 * @param {} tabid
 */
function com_bootStarpTab_stopFlashing(tabid){
	if(U_object_isNotNull(tabid)){
		var tabp=com_bootStarpTab_getCacher(tabid);
		var interval=tabp["flashingObj"];
		if(U_object_isNotNull(interval)){
			window.clearInterval(interval);
			tabp["flashingObj"]=null;
			
			var tabp=com_bootStarpTab_getCacher(tabid);
			var icoid=tabp["icoElementId"];
		 	var icoset=tabp["icoSettings"];
		 	icoset["flashFalg"]=0;
		 	document.getElementById(icoid).className=icoset["icoCss"];
		 	tabp["icoSettings"]=icoset;
			com_bootStarpTab_setCacher(tabp);
		}
	}
}

/**
 * 设置TAB对象的显示文字
 */
function com_bootStarpTab_setText(tabid,text){
	if(U_object_isNotNull(tabid) && U_object_isNotNull(text)){
		var tabp=com_bootStarpTab_getCacher(tabid);
		var textid=tabp["textElementId"];
		if(U_object_isNotNull(textid)){
			$("#" + textid).text(text);
		}
	}
}
/**
 * 设置TAB对象的标签长度
 */
function com_bootStarpTab_setWidth(param){
	
}

/**
 * 新建一个TAB页正规入口在com_bootStarpTab_addTab方法中，由该方法调用此为私有
 * @param {} p 参见com_bootStarpTab_addTab
 */
function com_bootStarpTab_createTab(p){
	//属性[tabId tabContentId] 在方法com_bootStarpTab_addTab 进行了初始设定
	var tabName=p["tabName"],tabComId=p["tabComId"],tabComContentId=p["tabComContentId"],contentType=p["contentType"],
		iframeSrc=p["iframeSrc"],divContent=p["divContent"],tabId=p["tabId"],tabContentId=p["tabContentId"],hasActive=p["hasActive"],
		hasClose=p["hasClose"],closeMode=p["closeMode"],reEl='';
	//若不设定tabComContentId则默认为 tabComId + _content
	if(tabComContentId==''){
		tabComContentId=tabComId + "_content";
	}
	
	var tabCom=$("#"+ tabComId)[0];
	var tabComContent=$("#" +tabComContentId)[0];
	var tabStyle=p["tabStyle"];
	if(!U_object_isNotNull(tabStyle) || tabStyle.indexOf("width")==-1 ){
		var tabSize = tabName.length * parseInt($("html").css("font-size").replace("px",""));
		tabStyle += "width:" + (tabSize + 20) + "px";
	}
	var tabTitle='<li id="'+ tabId +'"><a href="#' + tabContentId + '"  data-toggle="tab">';
	tabTitle+='<i id="'+ tabId +'_ico" class="" style="width:16px;height:16px;">&nbsp;&nbsp;&nbsp;&nbsp;</i>';
	tabTitle+='<span id="'+tabId+'_text" style="'+ tabStyle +'">' + tabName + '</span>';
	if(hasClose){
		tabTitle+='<i id="'+ tabId +'_closeButton" class="icon-remove">&nbsp;&nbsp;&nbsp;&nbsp;</i>';
	}
	tabTitle+="</a></li>";
	
	//设置参数
	p["icoElementId"]=tabId + "_ico";
	p["textElementId"]=tabId + "_text";
	p["closeElementId"]=tabId + "_closeButton";
	
	var tabContent='<div class="tab-pane com_css_topTab_content" id="'+ tabContentId +'">';
	if(contentType=='iframe'){
		tabContent+='<iframe src="'+ iframeSrc +'"  frameBorder="0"></iframe>';
	}else if(contentType=='div'){
		tabContent+='<div>'+ divContent +'</div>';
	}
	tabContent+="</div>";
	//写入元素
	reEl=$(tabCom).append(tabTitle);
	if(hasClose){
	    var $closeBtn=$("#"+ tabId +"_closeButton");
	    //将tabId存储起来
	    $closeBtn.data("tabId",tabId);
		$closeBtn.bind("click",function(){
		    var _tabId= $(this).data("tabId");
		    //alert(_tabId);
		    var _params=com_bootStarpTab_getCacher(_tabId);
		    var closeFn=_params["closeFn"]||"";
			com_bootStarpTab_removeTab_def(_params.tabId,_params.closeMode,closeFn);
		});
	}
	$(tabComContent).append(tabContent);
	
	//缓存参数
	com_bootStarpTab_setCacher(p);
	
	//设置tab图标,一定要在缓存参数后执行。
	if(U_object_isNotNull(p["icoSettings"])){
		p["icoSettings"]["tabId"]=tabId;
		com_bootStarpTab_setIco(p["icoSettings"]);
	}
	
	//注册事件
	var showfn=p["show"];
	if(showfn!='' && typeof(showfn)=='function'){
		$('#' + tabComId + ' a[href="#'+ tabContentId +'"]').on("show",showfn);
	}
	showfn=p["shown"];
	if(showfn!='' && typeof(showfn)=='function'){
		$('#' + tabComId + ' a[href="#'+ tabContentId +'"]').on("shown",showfn);
	}
	
	//是否立即激活
	if(hasActive){
		$('#' + tabComId + ' a[href="#'+ tabContentId +'"]').tab("show");
	}
	
	//重新计算并设置UL宽度，防止折行
	com_bootStarpTab_resizeTabWidth($("#" + tabId)[0]);
	return reEl;
}
/**
 * 移除或隐藏TAB
 * @param {} p 参数JSON
 * @return {String} 返回成功文字描述
 */
function com_bootStarpTab_removeTab(p){
	var settings={
		id:'',
		//关闭模式[remove hide] 移除与隐藏,与com_bootStarpTab_addTab对应
		mode:'remove',
		closeFn:''
	}
	p=$().extend(settings,p);
	var id=p["id"];
	var mode=p["mode"];
	var closeFn=p["closeFn"];

	if(id==''){
		return "com_bootStarpTab_removeTab param is error [id] is ''.";
	}
	//重新计算并设置UL宽度
	var fixWidth=20;
	var w=$("#" +id).css("width");
	var tabel=$("#" + id).parents(".com_css_tomTab_tabBar")[0];
	var ul=$(tabel).find("ul")[0];
	var ulw=$(ul).css("width");
	var tabCacheP=com_bootStarpTab_getCacher(id);
	var tabContentId=tabCacheP["tabContentId"];
	//alert(fixWidth);
	if(U_object_isNotNull(tabCacheP) && U_object_isNotNull(tabCacheP["fixWidth"])){
		fixWidth=tabCacheP["fixWidth"];
	}
	//alert(fixWidth);
	w=parseInt(ulw)-parseInt(w);
	$(ul).css("width",w);
	var oldAct=$(ul).children(".active")[0];
	var oldprv=parseInt($('#' + id).index());
	var $tab=$('#' + id);
	//进行移除操作
	if(mode=='remove'){
		$('#' + id).remove();
		$("#" + tabContentId + " iframe").contents().find("body").empty();  
		$('#' + tabContentId).remove();
		//清理缓存参数
		com_bootStarpTab_cacherClear(id);
	}else if(mode=='hide'){
		var cbtId=tabCacheP["closeElementId"];
		//解除click事件引用，再次显示时会再次绑定
		$("#" + cbtId).unbind("click");
		var el=$('#' + id).clone();
		var elContent=$('#'+tabContentId);
		//判断当前页签是否被激活
	    if(elContent.hasClass("active")){    
			el.removeClass("active");
			elContent.removeClass("active");
		}else{
		   //未被激活时找到激活的tab 阻止冒泡事件 防止标签窜页
          // var $activeTab= $('#' + id).nextAll().filter(".active");
           //阻止冒泡事件
           //event.stopPropagation();
          $tab.click(function(e) {
			    e.stopPropagation();
		  });

		}
		$tab.remove();
		com_bootStarpTab_removeTab_toHide(el,elContent);
		//$('#' + tabContentId).remove();		
	}
	
	//如存在关闭时回调函数进行调用
	if(typeof closeFn=="function"){
	    var tab_Id=id;
	   //console.log("tabId"+id);
	    closeFn(tabCacheP);
	}
	//验证是否要打开左移与右移
	com_bootStarpTab_check_leftAndRightShow(tabel);
	if(!U_object_isNotNull(oldAct) || oldAct.id==id){
		if(oldprv>0){
			var pos=parseInt(oldprv)-1;
			$(ul).find("li:eq(" + pos + ") a").tab("show");
		}else{
			var ali=$(ul).children("li")[0];
			if(U_object_isNotNull(ali)){
				$(ul).find("li:eq(0) a").tab("show");
			}
		}
	}
}

function com_bootStarpTab_removeTab_toHide(el,elContent){
	var hideDiv=$("#____topTab_to_hid_div")[0];
	if(!U_object_isNotNull(hideDiv)){
		$("body").append("<div id='____topTab_to_hid_div' style='display:none'></div>");
		hideDiv=$("#____topTab_to_hid_div");
	}
	$(hideDiv).append(el);
	//取消对正文内容的移动，正文内容可能会很大，这种DOM的操作有可能影响性能
	//$(hideDiv).append(elContent);
}

/**
 * 对关闭tab的再封装
 * @param {} id   关闭LI的ID
 * @param {} mode 关闭模式，隐藏还是移除
 * @param {} fn   关闭后回调  
 */
function com_bootStarpTab_removeTab_def(id,mode,closeFn){
    
	com_bootStarpTab_removeTab({
		id:id,
		mode:mode,
		"closeFn":closeFn
	});
}
/**
  跟据tabid 关闭选项卡
  id       tabId唯一标识  String
  closeFn  关闭后回调方法  function
**/
function com_bootStarpTab_removeTab_byId(id,closeFn){
    var params=com_bootStarpTab_getCacher(id);
    //console.log("params",params);
    var mode=params["closeMode"];
	com_bootStarpTab_removeTab({
		id:id,
		mode:mode,
		"closeFn":closeFn
	});
}
