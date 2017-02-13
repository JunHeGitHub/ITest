/*通用功能整理到这里*/


var passedArgs=new Array();

function getArgs(){
    search = self.location.href;
    search = search.split('?');
    if(search.length>1){
        argList = search[1];
        argList = argList.split('&');
        for(var i=0; i<argList.length; i++){
            newArg = argList[i];
            newArg = argList[i].split('=');
            // alert(unescape(newArg[1])+" "+(decodeURIComponent(newArg[1]))+" "+decodeURIComponent(escape(unescape(newArg[1]))));
            passedArgs[unescape(newArg[0])] = unescape(newArg[1]);
        }
    }
}

function Z_IsEmpty20(value) {
    return typeof(value)=="undefined" || value==null ||  (typeof(value)=='string' &&(value=="undefined" || value=='' )) ||  (typeof(value)=='boolean' &&value==false  )  ;
};

function Z_IsEmpty2(v) {
    return v === null || v === undefined || v == "undefined" || typeof(v) == "undefined";
};

// EX1: (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// EX2: (new Date()).Format("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
Date.prototype.format = function(fmt)
{
    var o = {
        "M+" : this.getMonth()+1,   //月份
        "d+" : this.getDate(),      //日
        "h+" : this.getHours(),     //小时
        "m+" : this.getMinutes(),   //分
        "s+" : this.getSeconds(),   //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S" : this.getMilliseconds() //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

Date.prototype.formatUTC = function(fmt)
{
    var o = {
        "M+" : this.getUTCMonth()+1,   //月份
        "d+" : this.getUTCDate(),      //日
        "h+" : this.getUTCHours(),     //小时
        "m+" : this.getUTCMinutes(),   //分
        "s+" : this.getUTCSeconds(),   //秒
        "q+" : Math.floor((this.getUTCMonth()+3)/3), //季度
        "S" : this.getUTCMilliseconds() //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getUTCFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}


function Z_ClientStrDecode(reqStr){
    if(typeof(reqStr)=="undefined" || reqStr==null ||  (typeof(reqStr)=='string' &&(reqStr=="undefined" || reqStr=='' )) ||  (typeof(reqStr)=='boolean' &&reqStr==false  )){
        return '';
    }
    return unescape(reqStr);
};

function Z_DecodeU2(str){
    str = str.replace(/(2_3)(\w{4}|\w{2})/gi, function($0,$1,$2){
        return String.fromCharCode(parseInt($2,16));
    });
    return str;
};

function Z_IsEmpty(value,allowBlank) {
    return typeof(value)=="undefined" || value === null || value === undefined || ((Z_IsArray(value) && !value.length))  || (!allowBlank ? value === '' : false) ||value=="undefined" ;
};

function Z_IsArray(o) {
    return o && (o instanceof Array) || o.constructor === Array;
};


function Z_IsNotEmtArray(o) {
    return Z_IsArray(o) && typeof(o.length) && o.length>0;
};

function Z_ClientStrEncode(reqStr){
    if(Z_IsEmpty20(reqStr)){
        return '';
    }
//	return encodeURIComponent(encodeURIComponent(reqStr));
    return escape(reqStr);
}

function is_weixin(){
    var ua = navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i)=="micromessenger") {
        return true;
    } else {
        return false;
    }
} ;


function is_weixn_iphone(){
    var ua = navigator.userAgent.toLowerCase();
//    alert(ua);
    if(ua.match(/MicroMessenger/i)=="micromessenger" && (ua.match(/iPhone/i)=="iphone" || ua.match(/iPad/i)=="ipad")  ) {
        return true;
    } else {
        return false;
    }
}


function isAndroid(){
    var ua = navigator.userAgent.toLowerCase();
    if(ua.indexOf('android')!=-1) {
        return true;
    } else {
        return false;
    }
} ;


