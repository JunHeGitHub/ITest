/**
全局变量参数
使用说明
1.在GOLBAL_PARAM定义参数需在文档里写上参数作用及说明
2.命名要规范清晰 ，让人从变量名中看出它的含义
示例

//用户信息
GOLBAL_PARAM["userInfo"]={};


**/
var GOLBAL_PARAM={};

/**
 * 此变量用于获取对应程序语言的提示信息
 */
GOLBAL_PARAM["i18nPrompt"]={};

/**
*	字典项前端缓存变量.
*   格式 Map <key,list>  ,   key为comboName , list为对应字典数据。
*/
GOLBAL_PARAM["dictdata"]={};

GOLBAL_PARAM["zshd"]={};

/**
*	字典索引项.
*   格式 List
*/
GOLBAL_PARAM["dictIndex"]={} ;
 
/**
   *此用户所有权限
   *根据type 区分  格式   {' 权限类型' :[{ name：权限名称，等等 },{ name：权限名称，等等 }]  }
   *权限类型有 tab tab选项卡  menu 菜单   page 页面权限   base  基础权限   知识库权限 zkm
**/
GOLBAL_PARAM["userAllPermisson"]=false;

/**
系统语言
**/
var PROJECT_LANGUAGE="zh_cn";

/*
 * 质检模块 获取状态 方便国际化
 */
GOLBAL_PARAM["ZQC_STATE"]={};




//// 工程名称
var PRJ_PATH="ZDesk";

//通用
var ZDesk_ROU = "ZDesk";

var ITEMALLOCATION_PARAM={};




  
 
var  All_PARAM=null;
//有其他页面引用golbal_params但没jquery 
if(typeof $ !="undefined"){
	All_PARAM=getItemAllocationParam();
}


/**
 * 
 * @param 一个参数{ params.peizhiItem}
 * @return {}包含json对象的数组
 * 搜索到的数据为空，函数返回false。
 */
function getpeizhiItem(params){
	var o=All_PARAM;
	var res1 = jsonPath(o, "$..[?(@.peizhiItem =='"+params.peizhiItem+"')]");	
   	return res1;
}
/**
 * 
 * @param {}一个参数 params.bItem
 * @return {}包含json对象的数组
 * 搜索到的数据为空，函数返回false。
 */
function getbItem(params){
	var o=All_PARAM;
	var res2 = jsonPath(o, "$..[?(@.bItem =='"+params.bItem+"')]");
	return res2;
}

/**
 * 
 * @param {}一个参数params.productionId
 * @return {}包含json对象的数组
 * 搜索到的数据为空，函数返回false。
 */
function getproductionId(params){
	var o=All_PARAM;
	var res3 = jsonPath(o, "$..[?(@.productionId=='"+params.productionId+"')]");
	return res3;
}
/**
 * 
 * @param {}两个参数 params.peizhiItem,params.bItem
 * @return {}包含json对象的数组
 * 搜索到的数据为空，函数返回false。
 */
function getpeizhiItemAndbItem(params){
	var o=All_PARAM;
	var res4 = jsonPath(o, "$..[?(@.peizhiItem =='"+params.peizhiItem+"'),(@.bItem =='"+params.bItem+"')]");
	return res4;
}
/**
 * 
 * @param {}两个参数 params.productionId,params.bItem
 * @return {}包含json对象的数组
 * 搜索到的数据为空，函数返回false。
 */
function getproductionIdAndbItem(params){
	var o=All_PARAM;
	var res5 = jsonPath(o, "$..[?(@.productionId =='"+params.productionId+"'),(@.bItem =='"+params.bItem+"')]");
	return res5;
}
/**
 * 
 * @param {}两个参数 params.productionId,params.peizhiItem
 * @return {}包含json对象的数组
 * 搜索到的数据为空，函数返回false。
 */
function getproductionIdAndpeizhiItem(params){
	var o=All_PARAM;
	var res6 = jsonPath(o, "$..[?(@.peizhiItem =='"+params.peizhiItem+"'),(@.productionId =='"+params.productionId+"')]");
	return res6;
}

/**
 * 
 * @param {}三个参数  params.productionId,params.peizhiItem，params.bItem
 * @return {}json对象
 * 搜索到的数据为空，函数返回false。
 */
function getproductionIdAndpeizhiItemAndbItem(params){
	var o=getproductionIdAndbItem(params);
	var res7 = jsonPath(o, "$..[?(@.peizhiItem =='"+params.peizhiItem+"')]");
	 return res7;
}
/**
 * 固定productionId ==ZKM和bItem == inputshuju
 * @param {}一个参数 params.peizhiItem
 * @return {}json对象
 * 搜索到的数据为空，函数返回false。
 */
function gudingproductionIdAndbItem(params){
	var o=All_PARAM;
	var res8 = jsonPath(o, "$..[?(@.productionId =='ZKM'),(@.bItem =='inputshuju'),(@.peizhiItem =='"+params.peizhiItem+"')]");
	return res8;
}
/**
 * 一个，两个参数都可以传入，至少有一个不为空
 * @param {} params
 * @return {}返回json数组中的第一组数据
 * 搜索到的数据为空，函数返回false。
 */
function getjsonfirst(params) {
	var res1 = false;
	if (params.peizhiItem != "") {
		res1 = getpeizhiItem(params);
	} else if (params.bItem != "") {
		res1 = getbItem(params);
	} else if (params.productionId != "") {
		res1 = getproductionId(params);
	} else if (params.productionId != "" && params.bItem != "") {
		res1 = getproductionIdAndbItem(params);
	} else if (params.productionId != "" && params.peizhiItem != "") {
		res1 = getproductionIdAndpeizhiItem(params);
	} else if (params.bItem != "" && params.peizhiItem != "") {
		res1 = getpeizhiItemAndbItem(params);
	}
	if(typeof(res1)=="object")        {   
		
		var text = new Array();       
		for(var p in res1)          {   				
		if(typeof(res1[p])=="object")       
	        {          
		  text[0] = res1[0];}     			   
	         } 
	         return(text);
		}   
			
		 else {
		 	return res1;
		       }
		}

function setItemAllocationParam(params){
	var url = "/" + PRJ_PATH + "/" + "itemAllocation/itemAllocationAction" + "/" + "query.action";
	//var rdata;
	$.ajax({
		async :true,
		type : "post",
		url : url,
		data:params,
		dataType : "json",
		success : function(data) {
			if(data){				
			  var table=$('#propeiGrid').DataTable();
	     //清空筛选
	     table.search("").draw();
	     $(".search-query").val("");
	     //清空数据
	     table.clear().draw();	     
	     //重新加载数据
	     table.rows.add(data).draw(true);
	     $("#propeiAdd").hide();
        $("#propeiEdit").hide();
			}else{
				//rdata=data;
			}
		}
	});
	
 
}

function reloadItemAllocation(){
	var url = "/" + PRJ_PATH + "/" + "itemAllocation/itemAllocationAction" + "/" + "reload.action";
		//var rdata;
	$.ajax({
		async : true,
		type : "post",
		url : url,
		dataType : "json",
		success : function(data) {
	     if(data){
	     	All_PARAM=data;
			var table=$('#propeiGrid').DataTable();
		     //清空筛选
		     table.search("").draw();
		     $(".search-query").val("");
		     //清空数据
		     table.clear().draw();	     
		     //重新加载数据
		     table.rows.add(data).draw(true);
		     $("#propeiAdd").hide();
	        $("#propeiEdit").hide();
		}		
		else{
				//rdata=data;
			}
	}
	});
	
}
function getItemAllocationParam(params){
	var url = "/" + PRJ_PATH + "/" + "itemAllocation/itemAllocationAction" + "/" + "query.action";
	var rdata;
	$.ajax({
		async :false,
		type : "post",
		url : url,
		data:params,
		dataType : "json",
		success : function(data) {
			if(data){
				rdata=data;				
				//ITEMALLOCATION_PARAM=data;
			
			}else{
				rdata=data;
			}
		}
	});
	
  return rdata;
}
/*function itemAllocationToParam(param,bItem,productionId,peizhiItem){
	var length=param.length;
	if(bItem){
		param["bItem"][length]=bItem;
	}
	if(productionId){
		param["productionId"][length]=productionId;
	}
	if(peizhiItem){
		param["peizhiItem"][length]=peizhiItem;
	}
}*/
//浏览器信息
/*
 BrowserInfo 浏览器信息  BrowserInfo.msie=ture ie 浏览器 
                       BrowserInfo.chrome=true google 
 BrowserInfo.version 浏览器版本
 jquery 1.9 前提供的方法
*/
var BrowserInfo=null;

uaMatch =function( ua ) {
	ua = ua.toLowerCase();
	var match = /(chrome)[ \/]([\w.]+)/.exec( ua ) ||
		/(webkit)[ \/]([\w.]+)/.exec( ua ) ||
		/(opera)(?:.*version|)[ \/]([\w.]+)/.exec( ua ) ||
		/(msie) ([\w.]+)/.exec( ua ) ||
		ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec( ua ) ||
		[];
	return {
		browser: match[ 1 ] || "",
		version: match[ 2 ] || "0"
	};
}	   
if ( !BrowserInfo ) {
			matched = uaMatch( navigator.userAgent );
			browser = {};
			if ( matched.browser ) {
				browser[ matched.browser ] = true;
				browser.version = matched.version;
			}
			// Chrome is Webkit, but Webkit is also Safari.
			if ( browser.chrome ) {
				browser.webkit = true;
			} else if ( browser.webkit ) {
				browser.safari = true;
			}
		
			BrowserInfo = browser;
} 
 /***
    通过左侧树展开需要池化的页面
    规则 
    模块ID ：数量
    默认 是5
   IFRAME_CONFIG{
          模块1:5,
          模块2:6
   }
  TODO 可从数据一次载入该配置
 **/
var IFRAME_CONFIG={
    //
//    "5D907DF3-5C24-C99E-494D-638F4AED021F":5,
    "gongdan":5
 };