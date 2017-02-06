/**
ajax 请求封装
@param  url 请求路径
@param  params 参数  
@param  sync  同步异步
·@param fn 回调方法
**/

function ajaxFn(url, params, sync, fn) {
	var rdata = false;
	try{
		$.ajax({
					async : sync,
					type : "post",
					url : url,
					dataType : "json",
					data : params,
					success : function(data) {
						rdata = data;
						if (fn && typeof(fn) == 'function') {
							fn(data);
						}
					},
					error : function(r) {					
						if (fn && typeof(fn) == 'function') {
							fn('error');
						}
					}
				});
	}catch(ex){
	     if (fn && typeof(fn) == 'function') {
					fn(ex);
		  }
		
	}
	return rdata;
}


/**
 YYYY或yyyy 表示年
 MM 月
 dd 或DD 日
 hh或 HH 小时
 mm  分钟
 ss 或 SS 秒
 @param  formatStr  时间格式  例如 YYYY-MM-dd hh：mm：ss
 @param  date  国际标准时间
  
*/
function fomateDate(formatStr,date){
    var str = formatStr;   
	  str=str.replace(/yyyy|YYYY/,date.getFullYear());   
	  str=str.replace(/yy|YY/,(date.getYear() % 100)>9?(date.getYear() % 100).toString():'0' + (date.getYear() % 100));   
	  str=str.replace(/MM/,(date.getMonth()+1)>9?(date.getMonth()+1):'0' + (date.getMonth()+1));    
	  str=str.replace(/dd|DD/,date.getDate()>9?date.getDate().toString():'0' + date.getDate());    
	  str=str.replace(/hh|HH/,date.getHours()>9?date.getHours().toString():'0' + date.getHours());    
	  str=str.replace(/mm/,date.getMinutes()>9?date.getMinutes().toString():'0' + date.getMinutes());   
	  str=str.replace(/ss|SS/,date.getSeconds()>9?date.getSeconds().toString():'0' + date.getSeconds());   
      return str;
}



/**
  获取url 里参数值

**/
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


