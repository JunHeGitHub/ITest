/**
 * 项目通用js
 * 
 * 方法 要有 注释 参数说明 文档
 */

// 工程名称  引用global_params 中的全局变量
var PRJ_PATH = window.top.PRJ_PATH||"ZDesk";
//主服务器模块
var ZDesk_ROU = window.top.ZDesk_ROU||"ZDesk";

// 系统语言国际化模块---------------------------------------------------------------------开始

// 读取国际化提示并进行缓存操作 供在程序首次加载时调用
function getI18nPromptData() {
	// 请求地址
	var url = "/" + PRJ_PATH + "/" + ZDesk_ROU+"/i18nPrompt" + "/"+ "getI18nPromptData.action";
	var rData = {};
	ajaxFunction(url, "", false, function(data) {
				if (data && data.success) {
					rData = data.data;
					window.top.GOLBAL_PARAM["i18nPrompt"] = data.data;
				} else {
					alert('国际化提示读取数据失败!');
				}
			});
	return rData;
}
/*
 * 提示框需要加载样式文件和js文件 此处操作是在向页面中动态载入样式文件和js文件
 */
document.write("<script language=javascript src='/"+PRJ_PATH+"/js/artdialog/dialog-plus-min.js'></script>");
document.write("<link rel='stylesheet' href='/"+PRJ_PATH+"/js/artdialog/ui-dialog.css' type='text/css'></link>");
// 通用提示框封装
var zinglabs = new function() {
	var o = {};
	/*
	 * alert提示框
	 */
	o.alert = function(msg) {
		var d = dialog({
					fixed : true,
					width : 350,
					title : '提示',
					content : msg,
					okValue : '确定',
					ok : function() {
					}
				});
		d.showModal();
	}
	/*
	 * alert提示框 带回调
	 */
	o.alert2fn = function(msg,fn) {
		if (typeof fn != "function") {
			fn = function() {
				alert("It's not function!");
			}
		}
		var d = dialog({
					fixed : true,
					width : 350,
					title : '提示',
					content : msg,
					okValue : '确定',
					ok : fn
				});
		d.showModal();
	}
	/*
	 * confirm 确认提示框
	 */
	o.confirm = function(msg, fn) {
		if (typeof fn != "function") {
			fn = function() {
				alert("It's not function!");
			}
		}
		var d = dialog({
					fixed : true,
					width : 350,
					title : '提示',
					content : msg,
					okValue : '确定',
					ok : fn,
					cancelValue : '取消',
					cancel : function() {
					}
				});
		d.showModal();
	}
	/*
	 * confirm 确认提示框
	 */
	o.confirm2cancelFn = function(msg, fn, cancelFn) {
		if (typeof fn != "function") {
			fn = function() {
				alert("It's not function!");
			}
		}
		if (typeof cancelFn != "function") {
			cancelFn = function() {
				alert("It's not function!");
			}
		}
		var d = dialog({
					fixed : true,
					width : 350,
					title : '提示',
					content : msg,
					okValue : '确定',
					ok : fn,
					cancelValue : '取消',
					cancel : cancelFn
				});
		d.showModal();
	}
	
	//api 
	/**
	   文档地址 http://aui.github.io/artDialog/doc/index.html
	       //标题
			title: 'loading..',
			//ifream 地址
			url: './dialog-content.html',
			//打开事件
			onshow: function () {
				
			},
			//iframe 载入事件
			oniframeload: function () {
				
			},
			//关闭事件
			onclose: function () {
			}
	**/   
	/**  iframe 示例
	    	var dd= zinglabs.dialog({
			      title: '测试例子'
				  url: 'http://www.baidu.com',
				  width:1000,
				  height:800
			      
			 });
			 dd.close();
			 dd.show();
	**/
	o.dialog=function(param){
	   var d=dialog(param);
		d.showModal();	
	    return d;
	}
	// 国际化提示形式封装
	o.i18n = new function() {
		var o2 = {};
		// 获取国际话提示文本内容
		/**
		 * 国际化提示函数 使用方式： 1.页面引入appCommonUtil.js文件
		 * 2.在页面中想要弹出提示信息的地方使用zinglsbs.i18n.getText("saveOk"); saveOk=保存成功！
		 */
		o2.getText = function(key, pat) {
			var promptData = {};
			promptData = window.top.GOLBAL_PARAM["i18nPrompt"];
			if (pat != undefined && pat != null) {
				var promptMsg = "";
				if (promptData[key] != null && promptData[key] != undefined&& promptData[key].length != 0) {
					promptMsg = promptData[key];
					for (var o in pat) {
						promptMsg = promptMsg.replace(o, pat[o]);
					}
					return promptMsg;
				}

			} else {
				if (promptData[key] != null && promptData[key] != undefined&& promptData[key].length != 0) {
					return promptData[key];
				}
			}
			if (promptData["notKey"] != null&& promptData["notKey"] != undefined) {
				return promptData["notKey"];
			}
			return "未成功读取到国际化提示数据，请点击重载提示缓存按钮后重试！";
		}
		// 直接弹出提示
		o2.alert = function(key, pat) {
			var promptMsg = zinglabs.i18n.getText(key, pat);
			zinglabs.alert(promptMsg);
		}
		// 直接弹出提示 带回调
		o2.alert2fn = function(key, pat,fn) {
			if (typeof fn != "function") {
				fn = function() {
					alert("It's not function!");
				}
			}
			var promptMsg = zinglabs.i18n.getText(key, pat);
			zinglabs.alert2fn(promptMsg,fn);
		}
		return o2;
	}
	o.zqc =new function(){
		var o3 = {};
		// 获取质检模块中所涉及的状态
		/**
		 * 使用方式： 1.页面引入appCommonUtil.js文件 
		 * 	zinglabs.zqc.getState("chujianyifenpei");
		 */
		o3.getState = function(key) {
			var stateData = {};
			stateData = window.top.GOLBAL_PARAM["ZQC_STATE"];
			return stateData[key];
			
		}
		return o3;
	}

	return o;
}

// 重新加载缓存数据
function reloadI18nPromptCache() {
	// 请求提示数据 并进行数据缓存
	var url = "/" + PRJ_PATH + "/" + ZDesk_ROU+ "/i18nPrompt" + "/"+ "getI18nPromptData.action";
	var serverData = {};

	ajaxFunction(url, "", false, function(data) {
				if (data && data.success) {
					serverData = data.data;
					window.top.GOLBAL_PARAM["i18nPrompt"] = serverData;
					// alert("重载缓存成功！");
					zinglabs.alert("重载缓存成功！");
				} else {
					// alert("获取数据失败！");
					zinglabs.alert("获取数据失败！");
				}
			});
}
// 转换json对象中指定key的内容字符串为ascii字符码
function native2ascii(params, conversionKey) {
	if (params == null || params == undefined) {
		return params;
	}
	var conversionAscii = "";
	var conversionValue = params[conversionKey];
	for (var i = 0; i < conversionValue.length; i++) {
		var code = Number(conversionValue[i].charCodeAt(0));
		if (code > 127) {
			var charAscii = code.toString(16);
			charAscii = new String("0000").substring(charAscii.length, 4)
					+ charAscii;
			conversionAscii += "\\u" + charAscii;
		} else {
			conversionAscii += conversionValue[i];
		}
	}
	params[conversionKey] = conversionAscii;
	return params;
}
// 直接转换字符串为ascii字符码
function nativeToascii(str) {
	var conversionAscii = "";
	var conversionValue = str;
	for (var i = 0; i < conversionValue.length; i++) {
		var code = Number(conversionValue[i].charCodeAt(0));
		if (code > 127) {
			var charAscii = code.toString(16);
			charAscii = new String("0000").substring(charAscii.length, 4)+ charAscii;
			conversionAscii += "\\u" + charAscii;
		} else {
			conversionAscii += conversionValue[i];
		}
	}
	return conversionAscii;
}
// 直接将ascii字符码转换成文字
function ascii2native(str) {
	var conversionValue = str.split("\\u");
	var nativeValue = conversionValue[0];
	for (var i = 1; i < conversionValue.length; i++) {
		var code = conversionValue[i];
		nativeValue += String.fromCharCode(parseInt("0x" + code.substring(0, 4)));
		if (code.length > 4) {
			nativeValue += code.substring(4, code.length);
		}
	}
	return nativeValue;
}

// 系统语言国际化模块---------------------------------------------------------------------结束

// 系统权限模块--------------------------------------------------------------------------开始
/**
 * 查询所有角色 返回json数组 格式为[{},{},{},{},{}]
 */
function searchAllRole() {
	// 通用查询action路径
	var url = "/" + PRJ_PATH + "/" +ZDesk_ROU+"/CommonsCurd" + "/" + "Find.action";
	var params = {};
	params.tableName = "suSecurityRole";
	params.nameSpace = "com.zinglabs.apps.commons.pojo";
	params.nameSpaceId = "getList";
	var rdata = [];
	$.ajax({
				async : false,
				type : "post",
				url : url,
				dataType : "json",
				data : params,
				success : function(data) {
					if (data) {
						rdata = data.rows;
					}
				},
				error : function(r) {
				}
			});
	return rdata;
}

// 获取权限类型对应的信息
function getSuSecurityPermissionInfo(type, modelId) {
	// 通用查询action路径
	var url = "/" + PRJ_PATH + "/" + ZDesk_ROU+"/suSecurityPermission" + "/"+ "searchSuSecurityPermission.action";
	var stype = {
		typeName : type,
		"modleId" : modelId
	}
	var rdata = [];
	$.ajax({
				async : false,
				type : "post",
				url : url,
				dataType : "json",
				data : stype,
				success : function(data) {
					if (data) {
						rdata = data.data;
					}
				},
				error : function(r) {
					alert("error");
				}
			});
	return rdata;
}
// 根据用户名获取此用户全部系统权限
function getAllPermissionByLoginName(loginName) {
	var rdata = {};
	var params = {
		"loginName" : loginName
	};
	var url = "/" + PRJ_PATH + "/" + ZDesk_ROU + "/permissonMappingRole" + "/"+ "getAllPermisson.action";
	$.ajax({
				async : false,
				type : "post",
				url : url,
				dataType : "json",
				data : params,
				success : function(data) {
					if (data && data.success) {
						rdata = data.data;
					} else {
						zinglabs.alert(data.mgs);
					}
				},
				error : function(r) {
					alert("error");
				}
			});
	return rdata;
}
/**
 * 缓存当前用户全部权限
 */
function initUserPermission() {
	if (typeof GOLBAL_PARAM != undefined) {
		// 获取登陆名
		var loginName = getUserInfo().loginName;
		var permissions = getAllPermissionByLoginName(loginName);
		GOLBAL_PARAM["userAllPermisson"] = permissions;

	} else {
		alert("请引用golbal_params.js");
	}

}

// 验证该用户是否有权
function isHavePermission() {

}
// 获取用户名获取用户信息
function getUserInfoByLoginName(loginName) {
	var url = "/" + PRJ_PATH + "/" + ZDesk_ROU + "/UserInfo" + "/"+ "getUserByLoginName.action";
	var params = {
		"loginName" : loginName
	};
	var rdata = {};
	$.ajax({
				async : false,
				type : "post",
				url : url,
				dataType : "json",
				data : params,
				success : function(data) {
					if (data) {
						rdata = data;
					} else {
						zinglabs.alert(data.mgs);
					}
				},
				error : function(r) {
					alert("error");
				}
			});
	return rdata

}
// 获取当前系统用户角色
function getUserRole() {
	if (GOLBAL_PARAM && GOLBAL_PARAM["userInfo"] != null) {
		var userInfo = GOLBAL_PARAM["userInfo"];
	}
	return userInfo.roles.join(",");
}
// 获用取当前用户名称
function getUserInfo() {
	return window.top.GOLBAL_PARAM["userInfo"];
}
//获取当前用户权限
function getUserPermission(){
   return window.top.GOLBAL_PARAM["userAllPermisson"];
}
//隐藏
function getQueryString(name) {
	// 如果链接没有参数，或者链接中不存在我们要获取的参数，直接返回空
	if (location.href.indexOf("?") == -1
			|| location.href.indexOf(name + '=') == -1) {
		return '';
	}

	// 获取链接中参数部分
	var queryString = location.href.substring(location.href.indexOf("?") + 1);

	// 分离参数对 ?key=value&key2=value2
	var parameters = queryString.split("&");

	var pos, paraName, paraValue;
	for (var i = 0; i < parameters.length; i++) {
		// 获取等号位置
		pos = parameters[i].indexOf('=');
		if (pos == -1) {
			continue;
		}

		// 获取name 和 value
		paraName = parameters[i].substring(0, pos);
		paraValue = parameters[i].substring(pos + 1);

		// 如果查询的name等于当前name，就返回当前值，同时，将链接中的+号还原成空格
		if (paraName == name) {
			return unescape(paraValue.replace(/\+/g, " "));
		}
	}
	return '';
}
function hidenButton(){
		var modelId =getQueryString("modleId");	
		if(modelId==''){
		return;
		}
	 var list=getSuSecurityPermissionInfo("page",modelId);
	 var exitlist=window.top.getUserPermission().page;		 
	 if(exitlist==''||exitlist==null||exitlist==undefined){
	 for(var i=0;i<list.length;i++){
		$("#"+list[i].funId+"").hide();
	 }
	 return;
	 }else{	
	   for(var i=0;i<list.length;i++){   
	   for(var j=0;j<exitlist.length;j++){
	   	if(list[i].funId==exitlist[j].funId&&list[i].modleId==exitlist[j].modleId){
	      list[i].funId='';
	      break;   
	   	}
	   }	
		$("#"+list[i].funId+"").hide();
		} 
	 }			
	}
//
//$(document).ready(function(){
//	hidenButton(); 
      
//});

/**
  防止回退键返回 goHistory
***/

function banBackSpace(e){     
    var ev = e || window.event;//获取event对象     
    var obj = ev.target || ev.srcElement;//获取事件源     
      
    var t = obj.type || obj.getAttribute('type');//获取事件源类型    
      
    //获取作为判断条件的事件类型  
    var vReadOnly = obj.getAttribute('readonly');  
    var vEnabled = obj.getAttribute('enabled');  
    //处理null值情况  
    vReadOnly = (vReadOnly == null) ? false : vReadOnly;  
    vEnabled = (vEnabled == null) ? true : vEnabled;  
      
    //当敲Backspace键时，事件源类型为密码或单行、多行文本的，  
    //并且readonly属性为true或enabled属性为false的，则退格键失效  
    var flag1=(ev.keyCode == 8 && (t=="password" || t=="text" || t=="textarea")   
                && (vReadOnly==true || vEnabled!=true))?true:false;  
     
    //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效  
    var flag2=(ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea")  
                ?true:false;          
      
    //判断  
    if(flag2){  
        return false;  
    }  
    if(flag1){     
        return false;     
    }     
    
}
/*
 * 禁用浏览器点击退格按键(disableBack()) 返回上一页
 */
function disableBack(){
	//禁止后退键 作用于Firefox、Opera
	document.onkeypress = banBackSpace;
	//禁止后退键  作用于IE、Chrome
	document.onkeydown = banBackSpace;
};
disableBack();
// 系统权限模块-------------------------------------------------------------------结束

// 字典模块----------------------------------------------------------------------开始
/**
 * 加载字典项缓存
 * 
 * @param {}
 * @return {}
 */
function reloadDicData() {
	var url = "/" + PRJ_PATH + "/" +ZDesk_ROU+ "/Dicmaintenance" + "/"+ "reloadDicData.action";
	var params = {};
	var ddata = {};
	ajaxFunction(url, params, false, function(data) {
				ddata = data.data;
			});
	ddata = ddata;
	GOLBAL_PARAM["dictdata"].data = ddata;
}
/**
 * 全部字典数据
 * 
 */
function getAllDictValue() {
	var dictdata = ''
	if (GOLBAL_PARAM["dictdata"].data == ''|| GOLBAL_PARAM["dictdata"].data == null|| GOLBAL_PARAM["dictdata"].data == undefined) {
		reloadDicData();
		dictdata = GOLBAL_PARAM["dictdata"].data;
	} else {
		dictdata = GOLBAL_PARAM["dictdata"].data;
	}
	var alldictdata = {};
	var list = new Array();
	for (var key in dictdata) {
		alldictdata = dictdata[key];
		for (var i in alldictdata) {
			list.push(alldictdata[i]);
		}
	}
	return list;
}
/**
 * 根据code,获取name
 * 
 * @param {}
 *            code
 * @return {} value
 */
function getDictValueForCode(code) {
	var list = new Array();
	list = getAllDictValue();
	var name = '';
	for (var li in list) {
		if (list[li]['code'] == code) {
			name = list[li].name;
			break
		}
	}
	return name;
}
/**
 * 根据comboName 获取对应数据
 * 
 * @param {}
 *            comboName
 * @return {json}
 */
function getDictListfoIndexData(indexData) {
	var dictdata = '';
	var comData = '';
	if (indexData != null && indexData != undefined) {
		if (GOLBAL_PARAM["dictdata"].data == ''|| GOLBAL_PARAM["dictdata"].data == null|| GOLBAL_PARAM["dictdata"].data == undefined) {
			reloadDicData();
			dictdata = GOLBAL_PARAM["dictdata"].data;
		} else {
			dictdata = GOLBAL_PARAM["dictdata"].data;
		}
		comData = dictdata[indexData];
	} else {
		return '';
	}
	return comData;
}
/**
 * 加载字典索引数据到缓存中
 * 
 */

function onloadDictIndexData() {
	var url = "/" + PRJ_PATH + "/" +ZDesk_ROU+ "/Dicmaintenance" + "/"+ "selectDicIndex.action";
	var params = {};
	var ddata = {};
	ajaxFunction(url, params, false, function(data) {
				ddata = data.data;
			});
	ddata = ddata;
	GOLBAL_PARAM["dictIndex"].data = ddata;
}
/**
 * 获取字典索引数据
 * 
 */
function getDictIndexData() {
	if (GOLBAL_PARAM["dictIndex"].data != ""&& GOLBAL_PARAM["dictIndex"].data != null&& GOLBAL_PARAM["dictIndex"].data != undefined) {
		return GOLBAL_PARAM["dictIndex"].data;
	} else {
		onloadDictIndexData();
		return GOLBAL_PARAM["dictIndex"].data;
	}
}

// 字典模块--------------------------------------------------------------------------结束

// 系统启动加载模块 ------------------------------------------------------------------开始
/*******************************************************************************
 * 系统初始化
 */

function ZDesk_init() {
	// 解析url后参数
	getArgs();
	if (passedArgs["p1"] == undefined || passedArgs["p1"] == null|| passedArgs["p1"] == "") {
		window.location.href = "login.html";
		return;
	}

	initCache();

	// 权限部分

	initUserPermission();
	// 初始化头部
	init_head_content();
	// 初始化头部工具栏
	init_head_bar();
	// 初始化主体内容左侧
	init_content_left();
	// 初始化主体内容右侧
	init_content_right();
	// 初始化尾部工具栏
	init_footer_bar();
	// 初始化尾部内容
	init_footer_content();
}

// 缓存加载
function initCache() {
	// 系统配置
	
	// 国际化缓存
	GOLBAL_PARAM["i18nPrompt"] = getI18nPromptData();
	// TODO用户信息
	var loginName = passedArgs["p1"];
	GOLBAL_PARAM["userInfo"] = getUserInfoByLoginName(loginName);
	// TODO字典项缓存
	reloadDicData();
	//质检模块下的状态
	GOLBAL_PARAM["ZQC_STATE"] = getStateForZQC();

}

/**
 * 配置项加载
 */

// 系统加载模块 ------------------------------------------------------------------结束
// 系统推出
function logout() {
	//closeTheWindow();
	window.location.href="login.html"
}

// 无提示关闭当前窗口
function closeTheWindow() {
	if (!window.opener) {
		window.open("", "_self");
		window.close();
	} else {
		window.close();
	}

}

/**
 * 根据角色名取该角色下的所有用户名信息
 */
function getUserByRoleName(roleName){
	//获取角色id
	var url ="/" + PRJ_PATH +"/"+ZDesk_ROU+"/RoleCurd" + "/" + "getRoleByRoleName.action";
	var params = {};
	 params.name=roleName;
	 params.nameSpace ="com.zinglabs.apps.suPermission";
	 params.nameSpaceId = "getRoleByRoleName";
	 var roleId;
	 ajaxFunction(url,params,false,function(data){
	 		roleId=data.rows[0].id;
	 });
	 //根据角色id获取角色下用户集合{"loginName","admin"}
	 var rdata;
	 var array = new Array();
	 url = "/" + PRJ_PATH+ "/" + "ZKM/UserInfo" + "/" + "getLoginNameList.action";
	  params.roleId=roleId;
	  ajaxFunction(url,params,false,function(data){
	  		url= "/" + "ZDesk" + "/" + "ZKM/UserInfo" + "/" + "getUserByLoginName.action";
	  		for( var i in data.loginNameList){
	  			params.loginName=data.loginNameList[i].loginName;
	  			ajaxFunction(url,params,false,function(d){
	  				d.roles=d.roles.join(",");
	  				d.orgs=d.orgs.join(",");
	  				array.push(d);
	  			});
	  		}
	  });
	 //alert(JSON.stringify(array));
	  return array;
}

/*文件上传webUploader
 * 参数说明
 * id div的id
 * params:初始化上传所需的参数
 * 			 1、params.dataId 说明:文件与数据关联，若需为上传文件绑定数据，设置此参数，默认为''不绑定
 * 			 2、params.maxSize 说明:单个文件大小限制 默认为20M
 * 			 3、params.relativeKey 说明:去数据库根据relativeKey查询相对位置的值 对应DataItemAllocation中的peizhiItem字段 
 * 										 注:自定义相对路径时，需把productionId字段设置为fileUpload 参数类型为:String 默认为relativePath
 * 			 4、params.reFillFn 说明:上传文件成功后的回调方法，返回上传成功文件的信息，示例:doTest(data)  params.reFillFn="doTest"
 * 			 5、params.limitSize 说明:总文件的大小限制 参数类型为:number 默认为200M
 * 			 6、params.limitCount	说明:总文件的数量限制 参数类型为:number 默认为300
 * 			 7、params.allowType 说明:允许上传的文件类型 参数类型为:String,可选值(all、images) all为默认
 * 			 8、params.frameWidth 说明:设置iframe的宽度 参数类型为:number 默认为800
 * 			 9、params.frameHeight 说明:设置iframe的高度 参数类型为:number 默认为480
 * 		   10、params.frameStyle 说明:设置iframe的行内样式 参数类型为:行内style格式  默认为border:0 
 * 											  注:设置此参数时，若不想显示iframe边框需带上border:0
 * 		   11、params.isSimplePage 说明:是否是简单类型的文件上传 参数类型为:boolean 默认为false
 * 		   12、params.frameClass 说明:设置iframe的class样式 参数类型为:与class属性相同 多个用 " " 空格分隔
 * 		   13、params.isShowDialog 说明:设置是否显示为dialog 参数类型为:boolean 默认为false
 * 		   14、params.isMultiple 说明:设置是否可以选择多个文件 参数类型为:boolean 默认为true
 *		   15、params.serverPath 说明： 自定义请求路径  如:http://localhost:8080/ZDesk/ZKM/…  中的ZKM
 *		   参数类型为String 默认为ZKM

 */	
var uploadFileDig;
function useWebUploader(id,params){
		$("#"+id+" iframe").contents().find("body").empty();
		var frameWidth = params.frameWidth;
		if(frameWidth == undefined || frameWidth==='' || frameWidth==null){
			frameWidth=800;
		}
		var frameHeight = params.frameHeight;
		if(frameHeight == undefined || frameHeight==='' || frameHeight==null){
			frameHeight=480;
		}
		var frameStyle = params.frameStyle;
		if(frameStyle == undefined || frameStyle==='' || frameStyle==null){
			frameStyle='border:0';
		}
		var frameClass = params.frameClass;
		if(frameClass == undefined || frameClass==='' || frameClass==null){
			frameClass='';
		}
		var isShowDialog = params.isShowDialog;
		if(isShowDialog == undefined || isShowDialog==='' || isShowDialog==null){
			isShowDialog=false;
		}
		var page = '';
		var isSimplePage = params.isSimplePage;
		if(isSimplePage == undefined || isSimplePage==='' || isSimplePage==null){
			isSimplePage=false;
		}
		if(isSimplePage){
			page='simpleUpload.html';
		}else{
			page='index.html';
		}
		var url = '/'+PRJ_PATH+'/webUploader/'+page+'?params='+JSON.stringify(params);
		$("#"+id).children().remove();
		var $farme = $("<iframe frameborder='0'/>").appendTo("#"+id);
		if(isShowDialog){
			 uploadFileDig = window.top.dialog({
				title : '文件上传',
				content :$farme,
				onbeforeremove: function(){
					$farme.contents().find("body").empty();
					window.parent.focus();
				}
			});
			$farme.attr("src",url).attr("width",frameWidth).attr("height",frameHeight).attr("style",frameStyle).addClass(frameClass);
			uploadFileDig.showModal();
		}else{
			$farme.attr("src",url).attr("width",frameWidth).attr("height",frameHeight).attr("style",frameStyle).addClass(frameClass);
		}
}
/*根据dataId查询已上传的文件,并通过fn回调返回数据
 *dataId 数据Id
 *fn 回调函数
 *返回结果 数组	
 */
function searchFileToDownload(dataId,fn){
		if(dataId==undefined||dataId==''){
			zinglabs.alert("缺少必要参数dataId");
			return;
		}
		if(fn==undefined||fn==''){
			zinglabs.alert("缺少必要参数fn");
			return;
		}
		var url = '/'+PRJ_PATH+'/'+ZDesk_ROU+'/Commons/searchFileToDownload.action';
		var params = {};
		params.dataId=dataId;
		params.isPagination=false;
		ajaxFunction(url,params,true,function(data){
			if (fn && typeof(fn) == 'function') {
				fn(data.data);
			}
		});
}
//查询附件，根据业务id,附件的状态是未删除
function searchFileToDownloadstate1(id,dataId){
//		if(id==undefined||id==''){
//			zinglabs.alert("缺少必要参数id");
//			return;
//		}
		if(dataId==undefined||dataId==''){
			zinglabs.alert("缺少必要参数dataId");
			return;
		}
		var redata ;
		var params = {};
		params.tableName="FileDataMapper";
		params.columns="fileId";
		params.equal="dataId";
		params.columnValues={"dataId":dataId,"state":'1'};
		params.isPagination=false;
		commonSelect(params,false,function(data){
			if(!data.success||!(data.data.length>0)){
				return "" ;
			}
			for(var i in data.data){
				var fileId=data.data[i].fileId;
				var pat = {};
				pat.tableName="UploadSave";
				pat.equal="id,state";
				var eqParam = {} ;
				eqParam.id = fileId ;
				eqParam.state = "1" ;
				pat.columnValues=eqParam ;
				pat.isPagination=false;
				commonSelect(pat,false,function(rdata){
					if(!rdata.success||!(rdata.data.length>0)){
						return "" ;
					}
					for(var j in rdata.data){
						var file = rdata.data[j].savePath;
						var name = rdata.data[j].fileName;
						var state = rdata.data[j].state;
						var str ='';
//						if(state==1){
//							str = '<a href="javascript:void(0)" onclick="commonDownload(\''+file+'\',\''+name+'\')">'+name+'</a><br>';
//						}
//						$("#"+id).append(str);
						
					}
					redata=rdata.data ;
				});
			}
		});
		return redata ;
}
/*
 * 根据文件id获取文件信息
 * id 文件ID
 * fn 回调函数
 */
function commonGetFile(id,fn){
	if(id.length==0){
		zinglabs.alert('id不能为空');
	}
	 var params = {};
	 params.tableName='UploadSave';
	 params.primarykey="id";
	 params.equal = "id";
	 params.columnValues={"id":id},
	 commonSelect(params,true,function(data){
	 	fn(data.data[0]) ;
	 });
}
/*通用删除上传文件（假删除 设置state 0删除  1未删除）
 * ids 为文件id数组
 * */
function commonDeleteFile(ids){
	if(ids.length==0){
		console.error('文件id数组为空');
		return false;
	}
	try{
		for(var i in ids){
		     var id = ids[i];
			 var params = {};
				   params.tableName='UploadSave';
				   params.primarykey="id";
				   params.columnValues={"id":id,"state":0};
			commonUpdate(params,true,function(data){});	 	
		}
		return true;
	}catch(e){
		console.error(e);
		for(var i in ids){
		     var id = ids[i];
			 var params = {};
				   params.tableName='UploadSave';
				   params.primarykey="id";
				   params.columnValues={"id":id,"state":1};
			commonUpdate(params,true,function(data){});	 	
		}
		return false;
	};
}
/**
   需要解决安全问题  文件路径
   file      文件路径
   name  下载后文件名称
   forwardPath    转发路径    
**/
function commonDownload(file,name,forwardPath){
		if(forwardPath.length == 0){
			zinglabs.alert("请填写转发路径");
			return;
		}
		var url = '/'+PRJ_PATH+'/'+forwardPath+'/Commons/fileDownload.action';
		var iframe = window.top.$("#commonFileDownload");
		if(iframe.attr("src")==undefined){
			iframe=$('<iframe id="commonFileDownload" style="display:none"></iframe>').appendTo(window.top.$("body"));
		}
		if(file){
			iframe.attr("src",url+"?file="+nativeToascii(file)+"&name="+nativeToascii(name));
		}else{
			zinglabs.alert("文件路径不存在");
		}
}
//根据文件id下载文件
function commonDownloadByFileId(fileId,name,forwardPath){
	if(forwardPath.length == 0){
		zinglabs.alert("请填写转发路径");
		return;
	}
	//根据id获取文件
	commonGetFile(fileId,function(data){
		//下载文件
		commonDownload(data.savePath,name,forwardPath);
	});
}
/**
事件绑定方法
  列var param={
         "ok":{
           //绑定方法
           fn:"test",
           //绑定参数
           fp:"我是test方法hahaha",
           //绑定事件
           //event:"",
           //作用域
           scope:window
         },
         "ok2":{
           fn:"test2",
           fp:""
         }
       }
     
**/
 function bindEvent(param){
          //遍历 param
          for(var domObj in param){
             //绑定参数    domObj是 绑定对象
             var domTarget=param[domObj];
             
             var domParam={};
             if(typeof domTarget=="string"){
                domParam.fn=domTarget;
                domParam.fp="";
             }else{
               domParam=domTarget;
             }
             //默认点击事件
             var domEvent=domParam.event!=undefined?domParam.event:"click";
             //默认作用域
             var scope=domParam.scope!=undefined?domParam.scope:window
             //绑定事件方法
             $("#"+domObj).bind(domEvent,domParam,function(event){
                  var pdata=event.data;
                  var fp=pdata.fp;
                  var fn=pdata.fn;
                  scope[fn](fp);
             });
          } 
       }
 //初始化表单验证
 //依赖 WebRoot/js/jqueryValidate/jquery.validate.js
 function initValidate(id){
    		   $("#"+id).validate({ 	   
				      //错误信息处理方法
				      errorPlacement: function (error, element){
				       //if(element.)
                         element.addClass('error');                   
                         var errorInfo=error.html();
						 element.tooltip({trigger:"manual",
						                 title:errorInfo
						                 }) ;
						 element.tooltip("show");
                        },
                       //成功信息处理方法
	                    success: function ($e,dom) {
	                       var element=$(dom);
	                       element.tooltip('hide');
	                       //element.tooltip('destroy');
	                       element.removeClass('error');
	                       
	                     }
				});
 }
//初始化下拉列表
function initSelect(){
		$("select").each(function(index,element){
             var $element=$(element);
             var val=$element.attr("data-select");
             if(val!=undefined&&val!=""){
                  $element.append('<option value="">----</option>');
                   //获取字典数据
                  var dictData=window.top.getDictListfoIndexData(val)||{};
                  //下拉赋值
                  for(var i=0;i<dictData.length;i++){
                    $element.append('<option value="'+dictData[i].code+'">'+dictData[i].name+'</option>');
                  }
             }
	   });  
}
//获取质检状态
function getStateForZQC(){
	
	// 通用查询action路径
	var url = "/" + PRJ_PATH +"/"+ZDesk_ROU + "/CommonsCurd" + "/" + "Find.action";
	var params = {};
	params.tableName = "ZQC_STATE_MANAGER";
	params.nameSpace = "com.zinglabs.apps.commons.pojo";
	params.nameSpaceId = "getList";
	var rdata={};
	$.ajax({
				async : false,
				type : "post",
				url : url,
				dataType : "json",
				data : params,
				success : function(data) {
					if (data) {
						var tm = data.rows;
						for(var i=0;i<tm.length;i++){
							rdata[tm[i].stateKey]=tm[i].stateValue;
						}
					}
				},
				error : function(r) {
				}
			});
	return rdata;
}


//-----------------------树与数据相关操作--------------
/*
 * 绑定数据
 * 参数：
 * 			beanName  xml中定义的Filter名称(bean id)(必填)
 * 			nodeIdList 节点id集合(格式:'data1,...,dataN'的字符串)(必填)
 * 	   		dataIdList   数据id(格式:'data1,...,dataN'的字符串)(必填)
 */
function bundlingData(pat){
	var url ="/" + PRJ_PATH +"/"+ZDesk_ROU+ "/TreeDataMapper/doDataBinding.action";
	if(pat.beanName==""||pat.beanName==undefined||pat.beanName==null){
		zinglabs.alert("缺少必要参数:beanName");
		return false;
	}
	if(pat.nodeIdList==""||pat.nodeIdList==undefined||pat.nodeIdList==null){
		zinglabs.alert("缺少必要参数:nodeIdList");
		return false;
	}
	if(pat.dataIdList==""||pat.dataIdList==undefined||pat.dataIdList==null){
		zinglabs.alert("缺少必要参数:dataIdList");
		return false;
	}
	var rdata;
	ajaxFunction(url,pat,false,function(data){
		rdata = data.success
	});
	return rdata;
}
/*
 * 解绑数据
*			beanName  xml中定义的Filter名称(bean id)(必填)
 * 			nodeIdList 节点id集合(格式:'data1,...,dataN'的字符串)(必填)
 * 	   		dataIdList   数据id集合(格式:'data1,...,dataN'的字符串)(必填)
 */
function unbundlingData(pat){
	var url ="/" + PRJ_PATH +"/"+ZDesk_ROU+ "/TreeDataMapper/doDataUnbinding.action";
		if(pat.beanName==""||pat.beanName==undefined||pat.beanName==null){
		zinglabs.alert("缺少必要参数:beanName");
		return false;
	}
	if(pat.nodeIdList==undefined||pat.nodeIdList==null){
		zinglabs.alert("缺少必要参数:nodeIdList");
		return false;
	}
	if(pat.dataIdList==""||pat.dataIdList==undefined||pat.dataIdList==null){
		zinglabs.alert("缺少必要参数:dataIdList");
		return false;
	}
	//参数处理
	var array = pat.nodeIdList.split(',');
	var nodeIdList = '';
	var dataIdList='';
	for(var i in array){
		var nodeId=array[i];
		if(/^\".+\"$/.test(nodeId)){
			nodeIdList = nodeIdList+nodeId+',';
		}else{
			nodeIdList = nodeIdList+'"'+nodeId+'"'+',';
		}
	}
	nodeIdList=nodeIdList.substring(0,nodeIdList.length-1);
	
	array = pat.dataIdList.split(',');
	for(var i in array){
		var dataId=array[i];
		if(/^\".+\"$/.test(dataId)){
			dataIdList = dataIdList+dataId+',';
		}else{
			dataIdList = dataIdList+'"'+dataId+'"'+',';
		}
	}
	dataIdList=dataIdList.substring(0,dataIdList.length-1);
	pat.nodeIdList=nodeIdList;
	pat.dataIdList=dataIdList;
	var rdata;
	ajaxFunction(url,pat,false,function(data){
		rdata = data.success
	});
	return rdata;
}
/*
 * 查询树与数据关联数据
 * beanName  xml中定义的Filter名称(bean id)(必填),tableName 表名(必填),columnName 字段名(多个字段用','分隔),where 查询条件(格式:{key1:value1,...,keyN:valueN})
 */
function searchData(pat){
	var url ="/" + PRJ_PATH +"/"+ZDesk_ROU+"/TreeDataMapper/getDataList.action";
		if(pat.beanName==""||pat.beanName==undefined||pat.beanName==null){
		zinglabs.alert("缺少必要参数:beanName");
		return false;
	}
	if(pat.tableName==""||pat.tableName==undefined||pat.tableName==null){
		zinglabs.alert("缺少必要参数:tableName");
		return false;
	}
	if (pat.where != undefined && pat.where != null && pat.where != "") {
		$.each(pat.where, function (k, v) {
			var value = v + "";
				eval("pat." + k + "=" + "'" + value + "'");
		});
		delete pat.where;
	}
	var rdata={};
	ajaxFunction(url,pat,false,function(data){
		rdata=data.data;
	});
	return rdata;	
}

/**
 * 发送eMail
 * @param {} msgMailStr 正文
 * @param {} titleccStr　标题
 * @param {} cusMail	邮件发送地址
 */
function simpleSendMail(cusMail,titleccStr,msgMailStr,fn){
	var url ="/" + PRJ_PATH +"/"+ZDesk_ROU+"/mailAction/sendMail.action";
	var params = {
		skillType:'bocmail',
		test:'true',
 		titlecc:titleccStr,
 		cusMail:cusMail,
 		content:msgMailStr	
	}
	ajaxFunction(url, params, true, function(data){
		if(typeof fn == "function"){
			fn(data);
		}else{
			if (data && data.success) {
				zinglabs.alert("邮件发送成功！");
			}else{
				zinglabs.alert("邮件发送失败，请联系管理员！");
			}
		}
	});
}
/**
 * 重新加载邮件配置
 */
function initMailCfg(){
	var url ="/" + PRJ_PATH +"/"+ZDesk_ROU+"/mailAction/initMailCfg.action";
	
	ajaxFunction(url, {skillType:'bocmail'}, true, function(data){
		if (data && data.success) {
			zinglabs.alert("短信配置加载成功！");
		}
	});
}
/**
 * 发送短信
 * @param {} msgNO 手机号
 * @param {} msgStr　发送内容
 */
function sendShortMessage(msgNO,msgStr,fn){
	var url ="/" + PRJ_PATH +"/"+ZDesk_ROU+"/shortMessageAction/sendShortMessage.action";
	
	if(typeof(msgStr)=='undefined' || msgStr==null){
		zinglabs.alert("发送内容不能为空！");
	}
	if(typeof(msgNO)=='undefined' || msgNO==null){
		zinglabs.alert("手机号不能为空！");
	}
	/*if(ProjectType=="ZSH"){
		GOLBAL_PARAM["reportFashongduanxinCount"]+=1;
	}*/
	var params = {
		smsNumber : msgNO,
		smsContent : msgStr
	}
	
	ajaxFunction(url, params, true, function(data){
		if(typeof fn == "function"){
			fn(data);
		}else{
			if (data && data.success) {
				zinglabs.alert("短信已发送到短信网关！");
			}else{
				zinglabs.alert("短信发送失败，请联系管理员！");
			}
		}
	});
}
/**
 * 重新加载短信配置
 */
function initShortMessageCfg(){
	var url ="/" + PRJ_PATH +"/"+ZDesk_ROU+"/shortMessageAction/initCfg.action";
	
	ajaxFunction(url, {}, true, function(data){
		if (data && data.success) {
			zinglabs.alert("短信配置加载成功！");
		}
	});
}


/***
iframe 池实现
   接口说明
   获取一个iframe
  iframePool.getIframe(moduleId)
   释放一个iframe
  iframePool.releaseIframe(moduleId,iframeId)
***/
 var iframePool={};
 (function(){
     //已使用过的iframe状态
     var inUserMap={};
  //未使用的iframe池
  var notUseMap={};
  //获取一个iframe
     iframePool.getIframe=function(moduleId){
         if(!iframeIsInit(moduleId)){
            init(moduleId);
         }
         //如果池中数量>0 取出数据 改变已初始化过中状态
         if(notUseMap[moduleId].length>0){
           
          var iframeId=notUseMap[moduleId].pop();
          inUserMap[moduleId][iframeId]=true;
         // console.log("已加载完",notUseMap);
          return iframeId;
         
         }else{
            return false;
         }
    
     }
     //释放iframe
  iframePool.releaseIframe=function(moduleId,iframeId) {
     
      var iframeStat=false;
      //获取该链接状态
      if(inUserMap[moduleId]){
           iframeStat=inUserMap[moduleId][iframeId];
      }
   
      if(iframeStat&&notUseMap[moduleId]){
    //if(notUseMap[moduleId]&&notUseMap[moduleId]<getIfrNum(moduleId)){
       iframeStat=false;
       notUseMap[moduleId].push(iframeId);
       //console.log("释放窗口:",notUseMap); 
       return true;
     }else{
       // console.log("该窗口未被使用:"+moduleId);
        return false;
     }
   
    //return inUserMap[moduleId];
  }
  //初始化iframe 池
     function init(moduleId){
        //获取模块相对应的池中iframe的个数
        var num=getIfrNum(moduleId);
        
        var mpool=notUseMap[moduleId]=[];
        inUserMap[moduleId]={};
           
        for(var i=0;i<num;i++){
            mpool[i]=moduleId+"_"+i+"IFRPOOL";
        }
        return ;
     };
     //判断某类型的iframe是否初始化 
     function iframeIsInit(moduleId){
           if(notUseMap[moduleId]==undefined||notUseMap[moduleId]==null){
              return false;
           }
        return true;
     }
     //获取模块对应ifr的个数
     function getIfrNum(moduleId){
         var defaultNum=IFRAME_CONFIG.moduleId||5;
         return defaultNum;
     }
 })();
     /**
       在当前页面创建iframe  
       ifrId id=""
       fn回调 子页面的方法
       src 
       attrs  属性
     **/
 function createIframe(ifrId,src,fn,otherParams,attrs){
    var paramsStr="";
    //ie8 要引 json2.js 传参
    var type=$.type(otherParams);
    if(type==="object"){
        paramsStr=JSON.stringify(otherParams); 
    }else if(type==="array"){
       paramsStr=otherParams.toString();
    }else{
      paramsStr=otherParams;
    }
  
    //console.log(otherParams);
    //改动说明 这里由于子页面是异步加载 固iframe的onload方法可能在  js加载完前执行
    //所以这改成由 页面的函数回调触发 iframeInit 方法
    //ifrId iframe的ID ，fn 触发的方法  paramsStr 其他参数字符串
    //先从URL中获取吧
    src+="&ifrId="+ifrId+"&fn="+fn+"&paramsStr"+paramsStr;
   
    //var str="<iframe id='"+ifrId+"'  src='"+src+"' frameBorder='0'  style='height:98%' ></iframe>";
    var str="<iframe id='"+ifrId+"'  src='"+src+"' frameBorder='0' onload='iframeInit("+"\""+ifrId+"\",\""+fn+"\","+paramsStr+" )' style='height:98%' ></iframe>";
     return str;
   }
   
   /** reset 页面状态 
    otherParams
   
   **/
   function iframeInit(ifrId,fn,otherParams){
     //
   
     //子页面window对象
     //console.log("iframe 初始化:"+ifrId);
     var childPage= document.getElementById(ifrId).contentWindow;
     if(childPage&&typeof childPage[fn]=="function"){
        //console.log("调用子页面的"+fn+"方法");
        var tabId=ifrId+"_tid";
        try{
          childPage[fn](tabId,otherParams,ifrId);
        }catch(e){
        	  alert("iframeInit--"+e.message+"----ifrId:"+ifrId+"---fn:"+fn+"----otherParams:"+otherParams);
            console.log("iframeInit--"+e.message+"----ifrId:"+ifrId+"---fn:"+fn+"----otherParams:"+otherParams);
        }
      
     }else{
        //console.log("iframeInit()   iframe 不存在 或者 子页面"+fn+"方法不存在");
     }
   }
//页面中时清空所有表单值
 function clearAllFormValue(){
       $("form").each(function (index,element){         
          element.reset();
       });
 }  
 
 
/**
     全局遮罩
   
    依赖
       jquery.js
       jquery.blockUI.js
       
   **/
 // 遮罩属性定义
var global_maskContent = {
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
//打开全局遮罩
function openGlobalMask(){
      window.top.$.blockUI(global_maskContent);
}
//关闭遮罩
function closeGlobalMask(){
    window.top.$.unblockUI(global_maskContent);
}

/**
 * 工单入口(坐席端)
 * 
 * callInNum： 文本客户的呼入号。
 * customerCallTime ： 文本通话开始时间。
 * gongdanSource  ： 工单来源 ：文本客户（网页端未通过网银认证的客户）；网银客户；微信客户；留言客户；语音客户；
 * customerName  :　客户姓名
 * companyFullName :  “对方信息”企业全称信息
 * phoneNum  :“对方信息”手机号信息
 * paperType ：   证件类型(字典项数据)
 * 没有的字段就传空值。
 * */
function gongdanrukou(param){
	var url ="/" + PRJ_PATH +"/"+ZDesk_ROU+"/activityCommon/doStartByProcessDefinitionId.action";

	
	ajaxFunction(url,param, true, function(data){
		if (data && data.success) {	}
	});
}
