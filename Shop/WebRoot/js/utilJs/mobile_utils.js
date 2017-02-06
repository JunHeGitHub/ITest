/**********************************************************/
//通用工具函数库                                                    //
// 说明                                                             //
// 1.此js里的函数要与业务完全无关,要能拿出来单独使用               //
// 2.所有函数都要有注释,参数含义,返回值的说明等,如可以最好标注作者 //
// 3.此js添加函数 要在文档里添加其相应的函数说明                   //
//                                                        //
/**********************************************************/


var  TYPE_GROUP='group';
var  TYPE_GROUP2='GROUP';
var  TYPE_CHAT='chat';
var  TYPE_IM='IM';
var  TYPE_ORG='ORG';
var  TYPE_WOOW='WOOW';
var  TYPE_KH_ORDER='KH_ORDER';


/**
 *
 * @param type
 * @param chatId
 * @param companyId
 * 跳转到指定聊天页面
 */
function MH_GoChat(type,chatId,companyId) {
    if(type==TYPE_GROUP2){
        window.location.href = "/SRender?jsLoader=chat%2FtalkLoader&tpl=chat%2Ftalk&type="+type+"&did=" + chatId  + "&_t=" + (new Date()).getTime();
    }else if(type==TYPE_ORG){
        window.location.href = "/SRender?jsLoader=chat%2FtalkLoader&tpl=chat%2Ftalk&type="+type+"&did=" + chatId + "&companyId=" + companyId + "&_t=" + (new Date()).getTime();
    }else if(type==TYPE_WOOW){
        window.location.href = "/SRender?jsLoader=chat%2FtalkLoader&tpl=chat%2Ftalk&type="+type+"&did=" + chatId + "&companyId=" + companyId + "&v=9999.9999";
    }
};


/**
 *
 * zinggrid 点击行数据触发聊天，缺类型根据情况补充
 * @param rowData 点击的行数据
 * @param type 类型
 *
 */
function goGroupChat(rowData,type) {
    if (!MH_U.Z_IsEmpty(rowData)){
        if(type==TYPE_GROUP2 && !MH_U.Z_IsEmpty(rowData.Id)){
            MH_GoChat(type,rowData.Id);
//                window.location.href = "/SRender?jsLoader=chat%2FtalkLoader&tpl=chat%2Ftalk&type=GROUP&did=" + rowData.Id  + "&_t=" + (new Date()).getTime();
        }else{
            MH_GoChat(type,rowData.chatId,rowData.companyId);
//                window.location.href = "/SRender?jsLoader=chat%2FtalkLoader&tpl=chat%2Ftalk&type=ORG&did=" + rowData.chatId + "&companyId=" + rowData.companyId + "&_t=" + (new Date()).getTime();
        }
    }
    event.stopPropagation();
}

//手机端禁用所有的前往

function disableEnter(){
    $(document).keydown(function(event){
        if(is_weixin()){
            var target = event.target;
            if(event.keyCode===13){
                var tag = target.tagName;
                if(tag == "INPUT"){
                    return false;
                }else{
                    return true;
                }
            }
        }

    });
}


// 解决ios 不支持position:fixed 属性 我 后退 菜单乱飞。
$(':input').focus(function(){
    $(".foot-bar").css("position","absolute");
});
$(':input').blur(function(){
    $(".foot-bar").css("position","fixed");
});



//日期时间格式化
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
 * 日期时间格式化函数，效率不错只需要一次正则替换
 *
 * @param fmt
 * @returns {*}
 *
 * EX1: (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
 * EX2: (new Date()).Format("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
 */
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
};

/**
 *
 * @param fmt
 * @returns {*}
 * 格式化为utc时间
 */
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
};

/**根据格式重建日期对象
 * demo Z_BuildDate(dateString,'yyyy-MM-dd hh:mi:ss');
 * */
function Z_BuildDate(dateString, formatter) {
    var today = new Date();
    if (!dateString || dateString == "") {
        return today;

    }
    if (!formatter || formatter == "") {
        formatter = "yyyy-MM-dd hh:mi:ss";
    }
    var yearMarker = formatter.replace(/[^y|Y]/g, '');
    var monthMarker = formatter.replace(/[^M]/g, '');
    var dayMarker = formatter.replace(/[^d]/g, '');
    var hourMarker = formatter.replace(/[^h]/g, '');
    var minutesMarker = formatter.replace(/[^mi]/g, '');
    var secondsMarker = formatter.replace(/[^ss]/g, '');
    var yearPosition = formatter.indexOf(yearMarker);
    var yearLength = yearMarker.length;
    var year = dateString.substring(yearPosition, yearPosition + yearLength) * 1;
    if (yearLength == 2) {
        if (year < 50) {
            year += 2000;
        }
        else {
            year += 1900;
        }
    }
    var monthPosition = formatter.indexOf(monthMarker);
    var month = dateString.substring(monthPosition, monthPosition + monthMarker.length) * 1 - 1;
    var dayPosition = formatter.indexOf(dayMarker);
    var day = dateString.substring(dayPosition, dayPosition + dayMarker.length) * 1;
    var hourPosition = formatter.indexOf(hourMarker);
    var hour = dateString.substring(hourPosition, hourPosition + hourMarker.length) * 1;
    var minutesPosition = formatter.indexOf(minutesMarker);
    var minutes = dateString.substring(minutesPosition, minutesPosition + minutesMarker.length) * 1;
    var secondsPosition = formatter.indexOf(secondsMarker);
    var seconds = dateString.substring(secondsPosition, secondsPosition + secondsMarker.length) * 1;
    return new Date(year, month, day, hour, minutes, seconds);
};

/**
 *  比较
 * @param date1   yyyy-MM-dd hh:mm:ss
 * @param data2   yyyy-MM-dd hh:mm:ss
 * @returns date1>date2 返回 true ，date1 <date2 返回false
 */
function compareDate(dateStr1,dateStr2,format){
    var date1=Z_BuildDate(dateStr1,format);
    var date2=Z_BuildDate(dateStr2,format);

    if(date1.getTime()>=date2.getTime()){
        return true;
    }else{
        return false;
    }


}
/**
 * 时间格式化为昨天、前天等短时间的函数 fmt日期格式
 * TODO 时区
 * */
function shortTime(timeStr,fmt) {
    var tt = Z_BuildDate(timeStr,fmt);
    var date = tt.getTime();
    var now = new Date();
    var days = parseInt((now.getTime() - date) / 86400000);
    var today = now.getDate();
    var year = tt.getFullYear();
    var mouth = tt.getMonth() + 1;
    var day = tt.getDate();

    //var time = tt.getHours() < 10 ? "0" + tt.getHours() : tt.getHours();
    //var min = tt.getMinutes() < 10 ? "0" + tt.getMinutes() : tt.getMinutes();

    var time = tt.getHours();
    time=time<10?'0'+time:time;
    var min = tt.getMinutes();
    min=min<10?'0'+min:min;

    var result, offset;
    offset = Math.abs(today - day);
    if (days < 3&&offset<3) {
        if (offset === 0) {
            var offsetMills = now.getTime() - date;
            if (offsetMills < 1000) {
                result = '刚刚';
            } else if (offsetMills < 60000) {
                var s = parseInt(offsetMills / 1000);
                result = s + '秒前';
            } else if (offsetMills < 3600000) {
                var m = parseInt(offsetMills / 60000);
                result = m + '分钟前';
            } else {
                var h = parseInt(offsetMills / 3600000);
                result = h + '小时前';
            }
            //result = "今天 " + time + ":" + min;
            // result = time + ":" + min;
        } else if (offset === 1) {
            result = "昨天 " + time + ":" + min;
        } else if (offset === 2) {
            result = "前天 " + time + ":" + min;
        }
    } else {
        result = mouth + "月" + day + "日";
    }
    return result;
};


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

var b3TipDialog="false";
function openB3TipDialog(msg,title,closable){
    if(b3TipDialog=="false"){
        b3TipDialog = new BootstrapDialog({
            title: title,
            animate: false,
            type: BootstrapDialog.TYPE_WARNING,
            closable: closable,
            message: msg
        });
    }else{
        b3TipDialog.setTitle(title);
        b3TipDialog.setMessage(msg);
        b3TipDialog.setClosable(closable);
    }
    b3TipDialog.open();
};

function closeB3TipDialog(){
    setTimeout(function(){
        b3TipDialog.close();
    },1500);

};


/**
 ajax 请求封装
 @param  url 请求路径
 @param  params 参数
 @param  sync  同步异步
 ·@param fn 回调方法
 **/

function ajaxFunction(url, params, sync, fn,showTip) {
    var rdata = false;
    $.ajax({
        async : sync,
        type : "post",
        url : url,
        dataType : "json",
        data : params,
        success : function(data) {
            try{
                rdata = data;
                if (fn && typeof(fn) == 'function') {
                    fn(data);
                }
            }catch(ex){
                if (fn && typeof(fn) == 'function') {
                    fn(ex);
                }
                console.info(ex.stack);
            }
        },
        beforeSend: function () {
            if(showTip == true){
                openB3TipDialog("正在加载，请稍后...","提示",false);
            }
        },
        complete: function () {
            if(showTip == true){
                closeB3TipDialog();
            }
        },
        error : function(r) {
            try{
                if (fn && typeof(fn) == 'function') {
                    fn('error');
                }
            }catch(ex){
                if (fn && typeof(fn) == 'function') {
                    fn(ex);
                }
            }
        }
    });
    return rdata;
}


/***
 表单序列化
 @param id 表单 id
 @return object

 修改了之前的方法  ps 防止通用增改删异常
 增加根据data-sType属性
 如果 data-sType="date"
 value 等于 "" 则不进行拼装
 2014年12月19日  zhiyuan.guo
 如果影响之前的代码 则对此函数进行调整

 **/
function zkm_getHtmlFormJsons(fromId){
    var params={};

    $('#'+fromId+' :input').each(function(index,obj) {
        var type = obj.type;
        var tag = obj.tagName.toLowerCase();
        var $obj=$(obj);
        var value=$obj.val();
        var key=$obj.attr('name');

        if ((type == 'text' || type == 'password' ||type == 'hidden' || tag == 'textarea'||tag == 'select')&&key!=undefined&&key!=''){

            var sType=$obj.attr('data-sType');
            if(sType=="date"){
                if(value!="")
                    params[key]=$obj.val();

            }else{
                params[key]=$obj.val();
            }

        }
        if(type == 'radio'){
            if($obj.is(':checked')==true){
                params[key]=$obj.val();

            }
        }
        if(type == 'checkbox'){
            if($obj.is(':checked')==true){

                if( key!=null &&  key!='undefined'){

                    params[key]= $obj.val();
                }
            }

        }

    });

    return params;
}
/***
 判断是否为空 兼容false情况
 **/
function Z_IsEmpty20(value) {
    return typeof(value)=="undefined" || value==null ||  (typeof(value)=='string' &&(value=="undefined" || value=='' )) ||  (typeof(value)=='boolean' &&value==false  );
}

function  Z_IsEmpty(value) {
    return typeof(value)=="undefined" || value === null || value === undefined || value=="undefined" ;
}




function procErr(data) {
    if(!Z_IsEmpty(data) && !Z_IsEmpty(data.error)){
        if (data.error=='-1000'){
            alert('请完善个人信息，开启功能');
//             window.location="/card/detail/cardPage.htm";
//             window.location="/authPage/authOne.htm";
            window.location="/SRender?jsLoader=forward%2FforwardLoader&actionUrl=undefined&forwardUrl=personalInformation%2Fupdate.html&v="+JV;
            //window.location="/card/update.htm?msg=isVisitor&v="+JV;
        }else if(data.error=='-1001'){
            alert('操作超时，请重试');
            window.location=myurl;
        }else if(data.error=='-2001'){
            alert('分配客服失败，请稍后重试');
            window.location=meurl;
        }else if(data.error=='-2002'){
            alert('客服全忙，请稍后重试');
            window.location=meurl;
        }else if(data.error=='-2003'){
            alert('应答失败，请重试');
            window.location="/NCard/IMKFService?ac=IMEnter&v="+JV;
        }else if(data.error=='-2004'){
            alert('客户已被其他客服接听');
            window.location="/NCard/IMKFService?ac=IMEnter&v="+JV;
        }else if(data.error=='-5001'){
            alert('退出前需要委托，与您相关的工作');
        }
    }
};


/***
 shared.js 依赖
 应该删掉

 **/

function getArgs(){
    passedArgs=new Array();
    var search = self.location.href;
    search = search.split('?');
    if(search.length>1){
        var argList = search[1];
        argList = argList.split('&');
        for(var i=0; i<argList.length; i++){
            var newArg = argList[i];
            newArg = argList[i].split('=');
            // alert(unescape(newArg[1])+" "+(decodeURIComponent(newArg[1]))+" "+decodeURIComponent(escape(unescape(newArg[1]))));
            passedArgs[unescape(newArg[0])] = unescape(newArg[1]);
        }
    }

}


/**
 *
 * @type {string}
 * 不是对话框的通用封装，因为BootstrapDialog 用着比较简单，可以根据
 * 场景简单封装，优先使用已有功能
 */
function openB3AjaxAlertDialog(msg,title,closable,callback,closeTime){
    var curBsDialog = BootstrapDialog.show({
        title: title,
        animate: false,
        message: msg,
        type: BootstrapDialog.TYPE_WARNING,
        closable: closable,
        closeByBackdrop: closable,
        closeByKeyboard: closable,
        buttons: [{
            label: '关闭',
            cssClass:'btn btn-default btn-block',
            action: function(dialogRef){
                dialogRef.close();
                if(callback != undefined){
                    callback();
                }
            }
        }]
    });
    if(closeTime!=undefined && !isNaN(closeTime)){
        this.closeCurBsDialog = function () {
            curBsDialog.close();
        }
        window.setTimeout("closeCurBsDialog()",closeTime);
    }
    return curBsDialog;
}

function openB3BlockUIDialog(msg,title){
    BootstrapDialog.show({
        title: title,
        animate: false,
        message: msg,
        type: BootstrapDialog.TYPE_WARNING,
        closable: false,
        closeByBackdrop: false,
        closeByKeyboard: false
    });
}

/**
 * 因ace属性影响弹出框按钮显示，需要在页面添加如下css：
 * .clear_btn_margin_buttom{
 *     margin-bottom: auto;
 * }
 * @param msg 提示信息
 * @param title 标题
 * @param confirmCallback 确认回掉
 * @param cancelCallback 取消回掉
 */
function openB3AjaxConfigDialog(msg,title,confirmCallback,cancelCallback){
    BootstrapDialog.show({
        title: title,
        animate: false,
        message: msg,
        type: BootstrapDialog.TYPE_WARNING,
        closable: false,
        closeByBackdrop: false,
        closeByKeyboard: false,
        buttons: [{
            label: '确定',
            cssClass: 'btn btn-primary pull-left pull-lien',
            action: function(dialogRef){
                if(confirmCallback != undefined){
                    var ret = confirmCallback(dialogRef);
                }
                if(false != ret){
                    dialogRef.close();
                }
            }
        },{
            label: '取消',
            cssClass: 'btn btn-primary pull-right pull-lien',
            action: function(dialogRef){
                if(cancelCallback != undefined){
                    var ret = cancelCallback(dialogRef);
                }
                if(false != ret){
                    dialogRef.close();
                }
            }
        }]
    });
}

function closeB3AjaxAlertDialog(){
    $.each(BootstrapDialog.dialogs, function(id, dialog){
        dialog.close();
    });
}


function goPage(page2go){
    var evt=getEvent();
    Z_StopEvent (evt);
    if(typeof page2go !='undefined' && page2go !='false' && page2go !='javascript:void(0);' ){
        window.location=page2go;
    }
}

function Z_StopEvent (evt) {
    var e=typeof(evt)!='undefined'?evt:window.event;
    if (window.event) {
        e.cancelBubble=true;
    }
    /*else {
        //e.preventDefault();

    }*/

    try{
        e.stopImmediatePropagation();
        e.stopPropagation();
    }catch(ex){}
}

function getEvent(event) {
    var ev = event || window.event;

    if (!ev) {
        var c = this.getEvent.caller;
        while (c) {
            ev = c.arguments[0];
            if (ev && (Event == ev.constructor || MouseEvent  == ev.constructor)) {
                break;
            }
            c = c.caller;
        }
    }
    return ev;
}


function Z_IsArray(o) {
    return o && (o instanceof Array) || o.constructor === Array;
};


function Z_IsNotEmtArray(o) {
    return Z_IsArray(o) && typeof(o.length) && o.length>0;
};
/**
 * 将跳转参数拼接到url中，如:xxx.action?name=zhangsan&age=12
 */
function addParamToUrl(jumpUrl, urlParam) {
    if ($.type(urlParam) == "object") {
        var jumpParm = "";
        for (var key in urlParam) {
            jumpParm +="&" + key + "=" +urlParam[key];
        }
        jumpUrl += "?" + jumpParm.substr(1,jumpParm.length);
    }
    return jumpUrl;
}
/**
 * 获取服务端跳转链接
 * @param param = [{
 * 						actionUrl:'xxx.action',
 * 						actionParam:{
 * 							name:'zhangsan'	,
 * 							age : '30'
 * 						},
 * 						forwardUrl:'yyy.html',
 * 						forwardParam:{
 * 							aaa:'bb'
 * 						}
 * 					},
 * 					{
 * 						actionUrl:'aaa.action',
 * 						forwardUrl:'bbb.html'
 * 					}]
 * actionUrl = "" 表示不请求tomcat服务器直接跳转
 * forwardUrl = "" 表示跳转“我”页面
 */
function getServerJumpUrl(param) {
    var url = "/SRender?jsLoader=" + encodeURIComponent("forward/forwardLoader");
    if($.type(param) == "array"){
        var actionUrlParam = "";
        var forwardUrlParam = "";
        for (var i = 0; i < param.length; i++) {
            actionUrlParam += addParamToUrl(param[i].actionUrl,param[i].actionParam) + ",";

            forwardUrlParam += addParamToUrl(param[i].forwardUrl,param[i].forwardParam) + ",";
        }
        url += "&actionUrl=" + encodeURIComponent(actionUrlParam.substr(0,actionUrlParam.length-1)) +
            "&forwardUrl=" + encodeURIComponent(forwardUrlParam.substr(0,forwardUrlParam.length-1));
        return url;
    }else{
        //alert("传入参数类型："+ $.type(param) + "错误，应该是array");
        return;
    }
}
/**
 * 服务端跳转函数
 * @param param = [{
 * 						actionUrl:'xxx.action',
 * 						actionParam:{
 * 							name:'zhangsan'	,
 * 							age : '30'
 * 						},
 * 						forwardUrl:'yyy.html',
 * 						forwardParam:{
 * 							aaa:'bb'
 * 						}
 * 					},
 * 					{
 * 						actionUrl:'aaa.action',
 * 						forwardUrl:'bbb.html'
 * 					}]
 * actionUrl = "" 表示不请求tomcat服务器直接跳转
 * forwardUrl = "" 表示跳转“我”页面
 */
function serverJump(param,otherParam){
    var url = getServerJumpUrl(param);
    if(!Z_IsEmpty(otherParam) && typeof(otherParam) == "object"){
        $.each(otherParam,function(name,val){
            url += "&"+name+"="+encodeURIComponent(val);
        });
    }
    window.location.href = url;
}

function clientRedirect(actionParam,urlParam){
    var url = "/SRender?jsLoader=" + encodeURIComponent("forward/forwardLoader");
    url += "&actionUrl=" + encodeURIComponent(actionParam) +
        "&forwardUrl=" + encodeURIComponent(urlParam);
    window.location.href = url;
}
/**
 * 将数据设置到form表单中
 * @param formId form表单id
 * @param data 需要设置的数据
 * @param fn 回掉函数
 */
function setVal2Form(formId,data, fn) {
    try{
        $('#' + formId + ' :input').each(function(index, inputObj) {
            var type = inputObj.type;
            var tag = inputObj.tagName.toLowerCase();
            // vTmp input name属性值
            var vTmp = "";
            if (!Z_IsEmpty(fn)) {
                vTmp = fn(inputObj.name);
            }
            vTmp = vTmp || data[inputObj.name];


            if (Z_IsEmpty(vTmp)) {
                vTmp = '';
            }
            if (type == 'text' || type == 'password' || type == 'hidden'
                || tag == 'textarea' || tag == 'select')
                $(inputObj).val(vTmp);
            else if (type == 'checkbox' || type == 'radio') {
                $(inputObj).attr('checked', vTmp == $(inputObj).val() ? true : false);
            }
        });
    }catch(e){
        alert(e.message);
    }
}
//前端页面底部"我的跳转"
function jumpToMePageFn(){
    window.location="/SRender?jsLoader=login%2FmeLoader&tpl=login%2Fmy&v=9999.9999";
}
//发现跳转
function foundPage(){
    window.location ="/SRender?jsLoader=tuWenGuanLi%2FfoundListLoader&"
        + "tpl=tuWenGuanLi%2FfoundList&companyId="+defaultfx+"&showFoot=null"
        + "&comefrom=found";
}
// 首页跳转
function mainPage(isGet){
    if(typeof(isGet)!='undefined' && isGet=='true'){
        return "/SRender?jsLoader=tuWenGuanLi%2FfoundListLoader&"
            + "tpl=tuWenGuanLi%2FfoundList&companyId="+defaultsy+"&showFoot=null&showHead=hide&bottomNavigationNum=4"
            + "&comefrom=main&mhMenuKey_=03"
    }else{
        window.location ="/SRender?jsLoader=tuWenGuanLi%2FfoundListLoader&"
            + "tpl=tuWenGuanLi%2FfoundList&companyId="+defaultsy+"&showFoot=null&showHead=hide&bottomNavigationNum=4"
            + "&comefrom=main&mhMenuKey_=03";
    }
}


function callBackHeadImg(data){
    // ?_t 每次读取最新图片。
    $("#showyeImg").attr('src',data[0]['relativePath']+"?_t=" + new Date().getTime());
}

//上传
function pcUploadImg(fn,imgType,userOptions){
    var defaults = {};
    var url = TUWEN_ROU + "/UploadFile/comm_upload.action";
    defaults.actionPath = url;
    defaults.limitCount = 1;
    defaults.isMultiple = false;
    defaults.reFillFn = fn;
    defaults.imgType = imgType;
    if(Z_IsEmpty20(userOptions)){
        userOptions = {};
    }
    var winWidth = userOptions.winWidth ? userOptions.winWidth : "100%";
    var winHeight =  userOptions.winHeight ? userOptions.winHeight : "'270px'";
    var winTitle = userOptions.winTitle ? userOptions.winTitle :"图片上传";

    var options = $.extend(true, {}, defaults, userOptions);
    var url = "/JsLib/dist/component/webUploader/index.html?params="+encodeURIComponent(JSON.stringify(options));
    var iframe = "<iframe frameborder='0' src=" + url + " width="+winWidth+" height="+winHeight+" />"
    openB3UploadDialog(iframe,winTitle);
}

// 加载UI
function openB3UploadDialog(msg,title){
    BootstrapDialog.show({
        title: title,
        animate: false,
        message: msg,
        type: BootstrapDialog.TYPE_WARNING,
        closable: true,
        closeByBackdrop: false,
        closeByKeyboard: false
    });
}





/*判断是否微信*/
function is_weixin(){
    var ua = navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i)=="micromessenger") {
        return true;
    } else {
        return false;
    }
};

function is_MQQBrowser(){
    var ua = navigator.userAgent.toLowerCase();
//    alert(ua);
    if(ua.match(/MQQBrowser/i)=="mqqbrowser" ) {
        return true;
    } else {
        return false;
    }
};

function is_mhappAndroid(){
    var ua = navigator.userAgent.toLowerCase();
    if(ua.match(/androidzcloud/i)=="androidzcloud") {
        return true;
    } else {
        return false;
    }
};

function is_mhapp(){
    return is_mhappAndroid();
};

function is_mhappios(){
    var ua = navigator.userAgent.toLowerCase();
    if(ua.match(/androidzcloud/i)=="ioszcloud") {
        return true;
    } else {
        return false;
    }
};


function isAndroid(){
    var ua = navigator.userAgent.toLowerCase();
    if(ua.indexOf('android')!=-1) {
        return true;
    } else {
        return false;
    }
};

function is_ios(){
    var ua = navigator.userAgent.toLowerCase();
//    alert(ua);
    if(ua.match(/iPhone/i)=="iphone" || ua.match(/iPad/i)=="ipad") {
        return true;
    } else {
        return false;
    }
}

function Z_DU(str){
    str = str.replace(/(2_3)(\w{4}|\w{2})/gi, function($0,$1,$2){
        return String.fromCharCode(parseInt($2,16));
    });
    return str;
}

function Z_EU(str){
    return str.replace(/([\u0000-\uFFFF])/g, function($0){
        try{
            var char16tmp=parseInt($0.charCodeAt().toString(16),16);
            if(char16tmp>=0x2002 && char16tmp<=0x200d){
                return "";
            }
        }catch(e){
            return "";
        }

        if($0.charCodeAt() <= 16){
            return '2_3000' + $0.charCodeAt().toString(16);
        }
        else if($0.charCodeAt() < 256) {
            return '2_300' + $0.charCodeAt().toString(16);
        }
        else {
            return '2_3' + $0.charCodeAt().toString(16);
        }
    });
} ;

//  修改名片
function goUpdateCardPage(){
    var param = [{
        forwardUrl:"personalInformation/update.html",
        actionUrl:"/ZingMH/ZDesk/MENHUWX/MHHelper/getDataForMHDict.action",
        forwardParam : {v:JV}
    }];
    serverJump(param);
}
//退出登录
function doLogout(){
    var url = "/ZingMH/ZDesk/MENHUWX/MHHelper/clearCookie.action";
    var params = {};
    ajaxFunction(url,params,true,function(data){
        if(!Z_IsEmpty20(data.retcode) && data.retcode == 0){
            window.location.href = "/SRender?jsLoader=login%2FqrLoginLoader&tpl=login%2FqrLogin";
        }else{
            openB3AjaxAlertDialog("退出失败！","提示",true);
        }
    });
}

//弹框 显示图片
function imgTip(path){
    BootstrapDialog.show({
        title : "帮助",
        animate : false,
        message : "<img src='"+path+"' width='100%'>",
        type : BootstrapDialog.TYPE_WARNING,
        closable : true,
        closeByBackdrop : false,
        closeByKeyboard : false
    });
}

/*
 *通用图片处理（人事档案头像和名片头像）
 * 参数1，参数2，参数3
 * imgType={hrImg,cardImg,bingImg}
 * cardUrl: 用户的cardUrl;
 * companyId: 人事档案头像需要该参数，当前用户所在的companyId
 */

function getImgSrc(imgType,cardUrl,companyId){

    var imgSrc = null;
    if(	"hrImg" == imgType){

        imgSrc = "/checkJpg/"+cardUrl.substr(0, 2)+"/"+companyId+cardUrl+"HR.jpg?_t=" + new Date().getTime();

    }else if("cardImg" == imgType){

        imgSrc = "/checkJpg/"+cardUrl+".jpg?_t="+ new Date().getTime();

    }else if("bingImg" == imgType){

        imgSrc = "/checkJpg/"+cardUrl.substr(0, 2)+"/" + cardUrl + companyId + "Bind.jpg?_t=" + new Date().getTime();
    }

    return imgSrc;

}

/*
 * 客户端日志输出方法 输出文件 服务器 /mnt/JSLOG.log下
 */
var isServLog=true;
function serverLogMH(str,logType) {
    if(!isServLog){return;}
    if(typeof(str)=='undefined' || str==null || str.length==0)return;
    logType=typeof(logType)=='undefined' || logType==null?"":logType;
    str=logType+":"+str;
    $.ajax({
        async : true,
        type : "post",
        url : "/NCard/NCardService?ac=JSLOG",
        data : {
            'content' : JV+" "+str,
            't' : (new Date()).getTime()
        },
        success : function(jsons) {
//            log.debug("save server log sucess");
        },
        error : function(r) {
//            log.debug('save server log error' + r);
        }
    });
};



/*
 * 检查script版本号
 * path 文件绝对路径
 */
function checkScriptVersion(path){
    try{
        var js = document.getElementsByTagName("script");
        for (var i = 0; i < js.length; i++) {
            var src = js[i].src;
            if(src != undefined && src != '' && src.length>0){
                var arraytemp = new Array();
                arraytemp = js[i].src.split('?');
                if(arraytemp.length > 1){
                    var paramTemp = arraytemp[1].split('=');
                    if(paramTemp[0] == 'v'){
                        if(paramTemp[1] != JV){
                            serverLogMH(path+" v != JV  "+arraytemp[0]+" v="+paramTemp[1]+" JV="+JV);
                        }
                    }else{
                        serverLogMH(path+" "+arraytemp[0]+" not have 'v='");
                    }
                }else{
                    serverLogMH(path+" "+arraytemp[0]+" not have '?v='");
                }
            }
        }
    }catch(e){
        serverLogMH("/JsLib/dist/js/mobile_utils.js "+e.message);
    }
}




/*点击下拉两级时的高度*/
var SEARCH_CONDITION_TWO_HEIGHT=500;
var SEARCH_GROUP_TWO_HEIGHT=300;


/*点击下拉一级时的高度*/

var SEARCH_CONDITION_ONE_HEIGHT=300;
var SEARCH_GROUP_ONE_HEIGHT=300;

/*搜索只有一级下拉*/
var SEARCH_TYPE_ONE='ONE';
/*搜索有两级下拉*/
var SEARCH_TYPE_TWO='TWO';

/**
 *
 * @param type 是否显示二级菜单
 * @param searchFn 点击查询按钮后
 *
 * 绑定dom标识 searchDiv searchCondition search-mhGroups
 * search-go search-back basic-search
 */
function MH_InitSearch(type,searchFn){
    $("#searchDiv").on('click',function(){
        if($('#searchCondition').css('display') == "none"){
            // $('#searchCondition').css('display','block');
            if(!Z_IsEmpty(type) && type==SEARCH_TYPE_ONE){
                /*一级下拉*/
                $('.search-mhGroups').hide();
                $('#searchCondition').slideDown(SEARCH_CONDITION_ONE_HEIGHT);
                $("#basic-search").focus();
            }else{
                $('#searchCondition').slideDown(SEARCH_CONDITION_TWO_HEIGHT);
                /*不是一级下拉*/
                $('.search-mhGroups').slideDown(SEARCH_GROUP_TWO_HEIGHT);
            }
        }else{
            //$('#searchCondition').css('display','none');
            $('#searchCondition').slideUp(SEARCH_CONDITION_TWO_HEIGHT);
            if(Z_IsEmpty(type) || type!=SEARCH_TYPE_ONE){
                /*不是一级下拉*/
                $('.search-mhGroups').slideUp(SEARCH_GROUP_TWO_HEIGHT);
            }
        }
    });


    if(Z_IsEmpty(type) || type!=SEARCH_TYPE_ONE){
        /*两级下拉，点击具体下拉项*/
        $(".searchItem").on("click",function(){
            var dataId = $(this).attr('dataId');
            $('#search-go').attr('dataId',dataId);
            $('.search-mhGroups').slideUp(SEARCH_GROUP_TWO_HEIGHT);
            $("#basic-search").focus();
        });

        /*两级下拉，文本框返回图标点击效果*/
        $("#search-back").on("click",function(){
            $('#search-go').attr('dataId','all');
            $('.search-mhGroups').slideDown(SEARCH_GROUP_TWO_HEIGHT);
        });
    }

    if(!Z_IsEmpty(searchFn) && typeof(searchFn)== 'function' ){
        $("#search-go").on("click",function(evt){
            Z_StopEvent (evt);
            searchFn();
        });
    }
}

/**
 * @param item  'card'等标识
 * 选择二级下拉
 * 原searchFriend()类似的场景
 *
 */
function MH_SearchItem(item){
    $('#searchCondition').slideDown(SEARCH_CONDITION_TWO_HEIGHT);
    $('#search-go').attr('dataId',item);
    $('.search-mhGroups').hide();
    $("#basic-search").focus();
}


/**
 *
 * @param var paramTmp={
 * inputObjId：使用的dom元素标识
 * itemRemoved：元素移除回调
 * itemAdded：元素添加回调
 * }
 * tag形式结果保存选择框，如orgList中的form-field-tags
 */
function Z_InitTagSelectInput (param) {

    var inputObjId=param.inputObjId;
    var tagSelectObj=$('#'+inputObjId);
    tagSelectObj.tagsinput({
        itemValue: 'id',
        readOnly: 'readOnly',
        itemText: 'text'
    });

    tagSelectObj.on('itemRemoved', function(event) {
        if(!Z_IsEmpty20(event.item.id) && typeof param.itemRemoved=='function'){
            param.itemRemoved(event.item);
        }
    });

    tagSelectObj.on('itemAdded', function(event) {
        if(!Z_IsEmpty20(event.item.id) && typeof param.itemAdded=='function'){
            param.itemAdded(event.item);
        }
    });
    return tagSelectObj;
};


/**
 *
 * @param tagSelectObj
 * @param valId
 * @param valtext
 * @constructor
 * 向tag input对象中添加一个元素
 */
function Z_TagSelectInputAdd (tagSelectObj,valId,valtext) {
    tagSelectObj.tagsinput('add', { id: valId, text: valtext});
};


/**
 *
 * @param tagSelectObj
 * @param valId
 * @param valtext
 * @constructor
 * 从tag input对象中移除一个元素
 */
function Z_TagSelectInputRemove (tagSelectObj,valId,valtext) {
    tagSelectObj.tagsinput('remove', { id: valId, text: valtext});
};

/**
 *
 * @param tagItem
 * @returns {null}
 * @constructor
 *
 * 获取选中的集合元素
 */
function Z_TagSelectInputVals(tagItem) {
    if(!Z_IsEmpty(tagItem) && tagItem!='false'){
        return tagItem.tagsinput('items');
    }else{
        return null;
    }
};



/**
 * es 通用操作函数，目前依赖b3环境，如果有必要调整为通用方式，移到common_utils中
 * 目前没支持query如果需要可以增加query支持。需要结合esForm.jst.ejs使用
 * 例子见：resInfo.html
 * */


/**
 * TODO 通用化，移动到工具类中
 * 简单处理了通用form编辑功能
 * 添加curGridObj参数 数据回填grid 有些需求列表是显示顺序号的 如果不进行grid reload,就会出现显示问题 传入则reload 不传则不处理
 *
 * CUR_DIALOG 添加纪录弹框对象，目的解决后端传回异常提醒时，
 * 只关闭正在处理中的提示框，表单窗口不关闭 否则用户输入的数据需要重新填写。
 *
 * 添加支持editFormJson.beforeSubmitFn 添加发起ajax之前的处理，为了解决个别需求在提交之前需要做相关处理已其他特殊判断等 可传入自定义的函数名
 * 添加支持editFormJson.afterSubmitFn 添加发起ajax请求之后的处理，为了解决个别需求在请求之后需要做相关处理等 可传入自定义的函数名
 * */
function esEditFormRow(paramTmp,editFormJson,curGridObj){
    var CUR_DIALOG=false;
    var editFormJST= JST['esForm'];

    var rowData={};
    var idName="";
    var formId="";
    var editUrl="";
    var gridVarName="";
    if(!Z_IsEmpty(paramTmp) && !Z_IsEmpty(paramTmp.rowData)){
        rowData=paramTmp.rowData;
        idName=paramTmp["idName"];
        formId=paramTmp["formId"];
        editUrl=paramTmp["editUrl"];
        gridVarName=paramTmp["gridVarName"];
    }else{
        /** TODO 增加统一入口，记录数据表示和错误码*/
        openB3AjaxAlertDialog("数据异常，请联系管理员","提示",false);
        return;
    }

    if(!Z_IsEmpty(rowData) && !Z_IsEmpty20(rowData[idName])){

        var rowIdVal=rowData[idName];

        var editVals= $.extend(true, {}, editFormJson);
        editVals.form.vals=rowData;

        BootstrapDialog.show({
            title: "修改",
            animate: false,
            message:editFormJST(editVals).replace(/\r|\n|\t/g, '') ,
            type: BootstrapDialog.TYPE_PRIMARY,
            closable: false,
            closeByBackdrop: false,
            closeByKeyboard: false,
            buttons: [{
                label: '取消',
                cssClass: 'btn btn-primary pull-right pull-lien',
                action: function(dialog){
                    dialog.close();
                }
            },{
                label: '确定',
                cssClass: 'btn btn-primary pull-left pull-lien',
                action: function(dialog){
                    if (!$("#"+formId).valid()){
                    	// 去掉数据异常提示，验证使用自带验证提示
                        //openB3AjaxAlertDialog("数据异常","提示",false);
                        return;
                    }

                    var editVal=_.object(_.map($("#"+formId).serializeArray(), function(item){return [item.name, item.value]; }));
                    editVal[idName]=rowIdVal;
                    /**
                     * 添加发起ajax之前的处理，为了解决个别需求在提交之前需要做相关处理已其他特殊判断等 可传入自定义的函数名
                     */
                    var beforeSubmitFn = editFormJson.beforeSubmitFn;
                    if(!Z_IsEmpty20(beforeSubmitFn)){
                        var beforeSubmitFnObj;
                        eval('beforeSubmitFnObj='+beforeSubmitFn);
                        var retData = beforeSubmitFnObj(editVal);
                        if(!Z_IsEmpty(retData)){
                            for(var key in retData){
                                editVal[key] = retData[key];
                            }
                        }
                    }
                    $.ajax({
                        dataType : "json",
                        url : editUrl,
                        type : "post",
                        data : editVal,
                        success: function (data) {
                            try {
                                //dialog.close();
                                //closeB3AjaxAlertDialog();
                                CUR_DIALOG.close();
                                MH_U.procZingMHErr(data);

                                if(!data.success){
                                    var errorMsg = data.msg;
                                    if(errorMsg==undefined){
                                        errorMsg = data.mgs;
                                    }
                                    openB3AjaxAlertDialog(errorMsg,"提示",false);
                                    return;
                                }
                                closeB3AjaxAlertDialog();
                                if(curGridObj!=undefined){
                                    curGridObj.reload();
                                }else{
                                    if( !Z_IsEmpty(gridVarName)){
                                        var gridObj;
                                        eval('gridObj='+gridVarName);
                                        gridObj.editOneRow(idName,rowData[idName],data.ret);
                                    }
                                }

                                /**
                                 * 添加发起ajax请求之后的处理，为了解决个别需求在请求之后需要做相关处理等 可传入自定义的函数名
                                 */
                                var afterSubmitFn = editFormJson.afterSubmitFn;
                                if(!Z_IsEmpty20(afterSubmitFn)){
                                    var afterSubmitFnObj;
                                    eval('afterSubmitFnObj='+afterSubmitFn);
                                    afterSubmitFnObj(data);
                                }
                            }
                            catch(e){

                            }
//                reloadGrid();
                        },
                        beforeSend: function () {
                            CUR_DIALOG = openB3AjaxAlertDialog("正在处理中，请稍后...","提示",false);
                        },
                        error:function(reData){
                            dialog.close();
                            closeB3AjaxAlertDialog();
                            CUR_DIALOG = openB3AjaxAlertDialog("操作失败","提示",false);
                        }
                    });
                }
            }]
        });
    }
    $("#"+formId).validate();
//    reloadGrid();
}





/**
 * TODO 通用化，移动到工具类中
 * 简单处理了通用form修改功能
 *
 * gridObj 数据回填grid
 *
 * CUR_DIALOG 添加纪录弹框对象，目的解决后端传回异常提醒时，
 * 只关闭正在处理中的提示框，表单窗口不关闭 否则用户输入的数据需要重新填写。
 *
 * 添加支持editFormJson.beforeSubmitFn 添加发起ajax之前的处理，为了解决个别需求在提交之前需要做相关处理已其他特殊判断等 可传入自定义的函数名
 * 添加支持editFormJson.afterSubmitFn 添加发起ajax请求之后的处理，为了解决个别需求在请求之后需要做相关处理等 可传入自定义的函数名
 * */
function esSaveFormRow(saveUrl,formId,editFormJson,gridObj){
    var CUR_DIALOG=false;
    var editFormJST= JST['esForm'];
    BootstrapDialog.show({
        title: "添加",
        animate: false,
        message:editFormJST(editFormJson).replace(/\r|\n|\t/g,''),
        type: BootstrapDialog.TYPE_PRIMARY,
        closable: false,
        closeByBackdrop: false,
        closeByKeyboard: false,
        buttons: [{
            label: '取消',
            cssClass: 'btn btn-primary pull-right pull-lien',
            action: function(dialog){
                dialog.close();
            }
        },{
            label: '确定',
            cssClass: 'btn btn-primary pull-left pull-lien',
            action: function(dialog){

                if (!$("#"+formId).valid()){
                	// 去掉数据异常提示，验证使用自带验证提示
                	//openB3AjaxAlertDialog("数据异常","提示",false);
                    return;
                }
                var saveVal=_.object(_.map($("#"+formId).serializeArray(), function(item){return [item.name, item.value]; }));

                /**
                 * 添加发起ajax之前的处理，为了解决个别需求在提交之前需要做相关处理已其他特殊判断等 可传入自定义的函数名
                 */
                var beforeSubmitFn = editFormJson.beforeSubmitFn;
                if(!Z_IsEmpty20(beforeSubmitFn)){
                    var beforeSubmitFnObj;
                    eval('beforeSubmitFnObj='+beforeSubmitFn);
                    var retData = beforeSubmitFnObj(saveVal);
                    if(!Z_IsEmpty(retData)){
                        for(var key in retData){
                            saveVal[key] = retData[key];
                        }
                    }
                }
                $.ajax({
                    dataType : "json",
                    url : saveUrl,
                    type : "post",
                    data : saveVal,
                    success: function (data) {
                        try{
                           // console.log(data);
                            //dialog.close();
                            //closeB3AjaxAlertDialog();
                            CUR_DIALOG.close();
                            MH_U.procZingMHErr(data);
                            if(!data.success){
                                var errorMsg = data.msg;
                                if(errorMsg==undefined){
                                    errorMsg = data.mgs;
                                }
                                openB3AjaxAlertDialog(errorMsg,"提示",false);
                                return;
                            }
                            closeB3AjaxAlertDialog();
                            gridObj.reload();
                            /**
                             * 添加发起ajax请求之后的处理，为了解决个别需求在请求之后需要做相关处理等 可传入自定义的函数名
                             */
                            var afterSubmitFn = editFormJson.afterSubmitFn;
                            if(!Z_IsEmpty20(afterSubmitFn)){
                                var afterSubmitFnObj;
                                eval('afterSubmitFnObj='+afterSubmitFn);
                                afterSubmitFnObj(data);
                            }

                        } catch(e){

                        }
                    },
                    beforeSend: function () {
                        CUR_DIALOG = openB3AjaxAlertDialog("正在处理中，请稍后...","提示",false);
                    },
                    error:function(reData){
                        dialog.close();
                        closeB3AjaxAlertDialog();
                        CUR_DIALOG = openB3AjaxAlertDialog("操作失败","提示",false);
                    }
                });
            }
        }]
    });
    $("#"+formId).validate();
}

/**
 * 删除角色
 * @param paramTmp
 *
 * 添加支持paramTmp.beforeSubmitFn 添加发起ajax之前的处理，为了解决个别需求在提交之前需要做相关处理已其他特殊判断等 可传入自定义的函数名
 * 添加支持paramTmp.afterSubmitFn 添加发起ajax请求之后的处理，为了解决个别需求在请求之后需要做相关处理等 可传入自定义的函数名
 */
function esDelFormRow(paramTmp){
    var rowData={};
    var idName="";
    var delUrl="";
    var gridVarName="";
    var delFieldName="";

    openB3AjaxConfigDialog('确认要删除吗?','删除',function(){
        if(!Z_IsEmpty(paramTmp) && !Z_IsEmpty(paramTmp.rowData) ){
            rowData=paramTmp.rowData;
            idName=paramTmp["idName"];
            delUrl=paramTmp["delUrl"];
            gridVarName=paramTmp["gridVarName"];

            if(Z_IsEmpty(idName) || Z_IsEmpty20(rowData[idName])){
                /** TODO 增加统一入口，记录数据表示和错误码*/
                openB3AjaxAlertDialog("数据异常，请联系管理员","提示",false);
                return;
            }

            var delData={};
            delData[paramTmp["idName"]]=rowData[idName];
            /**
             * 添加发起ajax之前的处理，为了解决个别需求在提交之前需要做相关处理已其他特殊判断等 可传入自定义的函数名
             */
            var beforeSubmitFn = paramTmp.beforeSubmitFn;
            if(!Z_IsEmpty20(beforeSubmitFn)){
                var beforeSubmitFnObj;
                eval('beforeSubmitFnObj='+beforeSubmitFn);
                var retData = beforeSubmitFnObj(delData);
                if(!Z_IsEmpty(retData)){
                    for(var key in retData){
                        delData[key] = retData[key];
                    }
                }
            }
            $.ajax({
                dataType : "json",
                url : delUrl,
                type : "post",
                data :delData,
                success: function (data) {
                    try{
                        closeB3AjaxAlertDialog();
                        var retCode=MH_U.procZingMHErr(data);
                        var isSubmitOk= false;

                        if(retCode=='0' && !Z_IsEmpty(gridVarName)){
                            isSubmitOk = true;
                            var gridObj;
                            eval('gridObj='+gridVarName);
                            // gridObj.delOneRow(idName,rowData[idName]);
                            gridObj.reload();
                            
                        }else{
                        	if(!Z_IsEmpty(data.msg) || !Z_IsEmpty(data.mgs)){
                                var errorMsg = data.msg;
                                if(Z_IsEmpty(errorMsg)){
                                    errorMsg=data.mgs;
                                }
                        		openB3AjaxAlertDialog(errorMsg,"提示",false);
                                if(data.success=="true" || data.success){
                                    isSubmitOk = true;
                                }
                        	}else{
                        		openB3AjaxAlertDialog("该角色下还有用户，您不能进行删除操作","提示",false);
                        	}
                        }
                        /**
                         * 添加发起ajax请求之后的处理，为了解决个别需求在请求之后需要做相关处理等 可传入自定义的函数名
                         */
                         if(isSubmitOk){
                             var afterSubmitFn = paramTmp.afterSubmitFn;
                             if(!Z_IsEmpty20(afterSubmitFn)){
                                 var afterSubmitFnObj;
                                 eval('afterSubmitFnObj='+afterSubmitFn);
                                 afterSubmitFnObj(data);
                             }
                         }

                    } catch(e){

                    }
//          reloadGrid();
                },
                beforeSend: function () {
                    openB3AjaxAlertDialog("正在处理中，请稍后...","提示",false);
                },
                error:function(reData){
                    closeB3AjaxAlertDialog();
                    openB3AjaxAlertDialog("操作失败","提示",false);
                }
            });

        }else{
            /** TODO 增加统一入口，记录数据表示和错误码*/
            openB3AjaxAlertDialog("数据异常，请联系管理员","提示",false);
            return;
        }
    },function(){
        return;
    });


}



/**
 *
 * init avalon
 *
 *
 * */

function initAvalon(){
    avalon.config({
        /**maxRepeatSize是用来配置ms-repeat循环绑定生成的代理节点所在的池子大小，
         目的是重复利用这些VM对象。默认是100。考虑手机内存情况，可以将此阀值调少点。*/
        lmaxRepeatSize: 20
        //,debug: false
        /*avalon拥有自己的AMD加载器，如果你想用requirejs或seajs，那你可以使用
         shim版本或禁用自带的加载器。已在代码中禁用*/
        //,loader: false



    });

}

/**
 * 将dom后代中具有属性data-rel="colorbox"的元素绑定到colorbox
 * 如bindColorbox(document);
 * 如bindColorbox(document.getElementById('');
 * @param dom
 */
function bindColorbox(dom) {
    var $overflow = '';
    var colorbox_params = {
        rel: 'colorbox',
        reposition:true,
        scalePhotos:true,
        scrolling:false,
        previous:'<i class="ace-icon fa fa-arrow-left"></i>',
        next:'<i class="ace-icon fa fa-arrow-right"></i>',
        close:'&times;',
        current:'{current} of {total}',
        maxWidth:'100%',
        maxHeight:'100%',
        onOpen:function(){
            $overflow = document.body.style.overflow;
            document.body.style.overflow = 'hidden';
        },
        onClosed:function(){
            document.body.style.overflow = $overflow;
        },
        onComplete:function(){
            $.colorbox.resize();
        }
    };

    var sel = $(dom).find('*[data-rel="colorbox"]');
    if (sel.length > 0) {
        sel.colorbox(colorbox_params);
    }
}

/**
 * 生成一个4位的随机码
 * @returns {string}
 */
function gen4Code() {
    return 'yxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
}

/**
 * 简单的json转对象方法
 * @param str
 */
function jsonReduction(str) {
    var objStr = str.replace(/'/g, "\"");
    return JSON.parse(objStr);
}

/**
 * 获取当前元素的坐标轴 X轴Y轴坐标通用方法
 * 目前:返回的是元素距离最左和最上的位置,如果需要更改可以重写.
 * @param element 传入 document.getElementById(id)
 * @returns {{}} 返回 X=position.offsetX,Y=position.offsetY
 */
function getPositionInDoc(element,dom_width) {
    //传入节点的宽度
    var dom_width = dom_width || 280;//eg需要控制节点是,但是算上页面左侧的padding
    //默认右侧padding间距
    var dom_padding_r = 20;
    var dom_padding_t = 5;
    //全局窗口宽度
    var win_width = window.innerWidth
    var position = {};
    position.offsetY = (function (e) {

        var distance = e.offsetTop;
        var parent = e.offsetParent;
        //迭代到终点......
        while (parent) {
            distance += parent.clientTop;
            distance += parent.offsetTop;
            parent = parent.offsetParent;
        }
        return distance+dom_padding_t;
    })(element);
    position.offsetX = (function (e) {
        var distance = e.offsetLeft;
        var parent = e.offsetParent;
        while (parent) {    
            
            distance += parent.clientLeft;
            distance += parent.offsetLeft;
            parent = parent.offsetParent;
        }
        if(win_width - distance <= dom_width){
            //计算最佳节点位置
            distance -= (dom_width-(win_width-distance)+dom_padding_r)
        }
        return distance;
    })(element);
    return position;
}

/**
 * 简单的对象深拷贝的方法
 * @param obj
 * @returns {*}
 */
function deepCopy(obj) {
    if (typeof obj === 'object') {
        var nObj = {};
        if(obj instanceof Array) {
            nObj = [];
        }
        for(var k in obj) {
            if(!obj.hasOwnProperty(k)) {
                continue;
            }
            var v = obj[k];
            if(typeof v === 'object') {
                nObj[k] = deepCopy(v);
            } else {
                nObj[k] = v;
            }
        }
        return nObj;
    } else {
        return undefined;
    }
}


/**
 * 获取dom的绝对定位,使用方法$(dom).offset();
 * @return {*} {top,left} or null
 */
$.fn.offsetAbs = function () {
    var getOffset = function (elem, doc, docElem, box) {
        try {
            box = elem.getBoundingClientRect();
        } catch(e) {}

        // Make sure we're not dealing with a disconnected DOM node
        if (!box || !jQuery.contains(docElem, elem)) {
            return box ? {
                top: box.top,
                left: box.left
            } : {
                top: 0,
                left: 0
            };
        }

        var body = doc.body,
            win = window,
            clientTop = docElem.clientTop || body.clientTop || 0,
            clientLeft = docElem.clientLeft || body.clientLeft || 0,
            scrollTop = win.pageYOffset || jQuery.support.boxModel && docElem.scrollTop || body.scrollTop,
            scrollLeft = win.pageXOffset || jQuery.support.boxModel && docElem.scrollLeft || body.scrollLeft,
            top = box.top + scrollTop - clientTop,
            left = box.left + scrollLeft - clientLeft;

        return {
            top: top,
            left: left
        };
    };

    var elem = this[0],
        doc = elem && elem.ownerDocument;

    if (!doc) {
        return null;
    }

    if (elem === doc.body) {
        return jQuery.offset.bodyOffset(elem);
    }

    return getOffset(elem, doc, doc.documentElement);
};


/**
 * 左侧菜单定位方法
 * 参数为菜单的key值
 */
function initMenuSelect(key){
	// 如果菜单 包含 “-” 说明该菜单为子菜单
	if(key.indexOf("-") > -1){
		var keyIndex = key.split("-");
		$(".noStyleFlag").removeClass("active");
		$(".noStyleFlag").removeClass("open");
		$("#m"+keyIndex[0]).addClass("active open");
		$("#m"+key).addClass("active");
	}else {
		$(".noStyleFlag").removeClass("active");
		$(".noStyleFlag").removeClass("open");
		$("#m"+key).addClass("active");			
	}
}

/**
 * 根据mapping重新组合数据的方法
 *
 * @param data 数据
 * @param mapping 处理规则, mapping结构如下:
 *   {
 *       newkey1: 'oldkey1',
 *       'newkey2': 'oldkey2',
 *       'newkey3.subkey': 'oldkey3.subkey2',
 *       'newkey4.subkey': 'oldkey4',
 *       'newkey5': {
 *           'key': 'oldkey5',
 *           'fn': function(val) {          //对原值进行处理
 *               //do something
 *               return newVal;
 *           }
 *       },
 *       'newkey6': function() {            //直接生成新值
 *          //dosomething
 *          return val;
 *       },
 *       'newkey7': {
 *          'key': 'oldkey7',
 *          'eq': { ...subMapping }         //oldkey7值为Array,里面的每一项指定subMapping规则
 *       }
 *   }
 *
 * @return object 新数据
 */
function dataRemap(data, mapping) {
    //js没有块作用域,只有方法作用域
    var newKey, oldKey, fn, eq, value, v, px, i, f;

    if (!data || typeof mapping !== 'object') {
        console.warn('dataRemap: data undefined or error format with mapping');
        return undefined;
    }
    outer: for (newKey in mapping) {
        if (!mapping.hasOwnProperty(newKey)) {
            continue;
        }
        v = mapping[newKey];
        if (!v || !(typeof v === 'string' || typeof v === 'function' || typeof v === 'object')) {
            console.warn('dataRemap: error mapping with key [' + newKey + ']');
            continue;
        }
        if (typeof v === 'object' && typeof v['key'] === 'undefined') {
            console.warn('dataRemap: error mapping with key [' + newKey + ']');
            continue;
        }
        oldKey = undefined;
        fn = undefined;
        eq = undefined;
        value = {};
        if (typeof v === 'function') {
            value = v();
        } else if (typeof v === 'string') {
            oldKey = v;
        } else {
            oldKey = v['key'];
            if (typeof v['fn'] === 'function') {
                fn = v['fn'];
            }
            if (typeof v['eq'] === 'object') {
                eq = v['eq'];
            }
        }
        //get value
        if (oldKey && oldKey.indexOf('.') !== -1) {
            px = oldKey.split('.');
            for (i = 0; i < px.length; i++) {
                if (i === 0) {
                    if (typeof data[px[0]] === 'undefined') {
                        continue outer;
                    }
                    value = data[px[0]];
                    if (px.length === 1) {
                        delete data[px[0]];
                    }
                } else {
                    if (typeof value[px[i]] === 'undefined') {
                        continue outer;
                    }
                    value = value[px[i]];
                    if (px.length === i + 1) {
                        delete value[px[i]];
                    }
                }
            }
        } else if(oldKey) {
            if (typeof data[oldKey] === 'undefined') {
                continue outer;
            }
            value = data[oldKey];
            delete data[oldKey];
        }
        //trans value
        if (fn) {
            try {
                value = fn(value);
            } catch(err) {
                console.error(err.message + '\n' + err.stack);
                break outer;
            }
        }
        if (eq) {
            if (value instanceof Array) {
                value.forEach(function(ele, ix, ry) {
                    ry[ix] = dataRemap(ele, eq);
                });
            } else {
                console.warn('key: ' + oldKey + ' value is not array');
            }
        }
        //set value
        if (newKey.indexOf('.') !== -1) {
            px = newKey.split('.');
            f = data;
            for (i = 0; i < px.length; i++) {
                if (px.length === i + 1) {
                    f[px[i]] = value;
                } else {
                    if (typeof f[px[i]] === 'undefined') {
                        f[px[i]] = {};
                    } else if (typeof f[px[i]] !== 'object') {
                        console.warn('dataRemap: overwrite happend on: ' + px[i] + ' of newKey: ' + newKey);
                        f[px[i]] = {};
                    }
                    f = f[px[i]];
                }
            }
        } else {
            data[newKey] = value;
        }
    }

    return data;
}

(function(){

    /**
     * Decimal adjustment of a number.
     *
     * @param {String}  type  The type of adjustment.
     * @param {Number}  value The number.
     * @param {Integer} exp   The exponent (the 10 logarithm of the adjustment base).
     * @returns {Number}      The adjusted value.
     */
    function decimalAdjust(type, value, exp) {
        // If the exp is undefined or zero...
        if (typeof exp === 'undefined' || +exp === 0) {
            return Math[type](value);
        }
        value = +value;
        exp = +exp;
        // If the value is not a number or the exp is not an integer...
        if (isNaN(value) || !(typeof exp === 'number' && exp % 1 === 0)) {
            return NaN;
        }
        // Shift
        value = value.toString().split('e');
        value = Math[type](+(value[0] + 'e' + (value[1] ? (+value[1] - exp) : -exp)));
        // Shift back
        value = value.toString().split('e');
        return +(value[0] + 'e' + (value[1] ? (+value[1] + exp) : exp));
    }

    // Decimal round
    if (!Math.round10) {
        Math.round10 = function(value, exp) {
            return decimalAdjust('round', value, exp);
        };
    }
    // Decimal floor
    if (!Math.floor10) {
        Math.floor10 = function(value, exp) {
            return decimalAdjust('floor', value, exp);
        };
    }
    // Decimal ceil
    if (!Math.ceil10) {
        Math.ceil10 = function(value, exp) {
            return decimalAdjust('ceil', value, exp);
        };
    }

})();

/**
 * 文件大小格式化显示的方法
 * @param size
 * @return {*} 格式化后的字符串
 */
function fileSizeFormat(size) {
    var sizeStr, temp;
    if (size < 0) {
        console.warn('fileSizeFormat: incorrect size ' + size);
        size = 0;
    }
    if (size < 1024) {
        sizeStr = size + 'B';
    }
    else if (size < 1024 * 1024) {
        temp = Math.round10(size / 1024, -2);
        sizeStr = temp + 'KB';
    }
    else if (size < 1024 * 1024 * 1024) {
        temp = Math.round10(size / (1024 * 1024), -2);
        sizeStr = temp + 'MB';
    }
    else {
        temp = Math.round10(size / (1024 * 1024 * 1024), -2);
        sizeStr = temp + 'GB';
    }
    return sizeStr;
}

// 部门文件柜
function departmentFile(id,name, e) {
    var evt = e || window.event;
    evt.stopPropagation();
    //window.location = "/SRender?jsLoader=fileManage%2FfileManageLoader&tpl=FileManage%2FfileManage&departmentId=" + data.id;
    window.location = "/SRender?jsLoader=explorer%2FexplorerLoader&tpl=explorer%2Fexplorer&departmentId=" + id + "&name=" + name;
    return false;
}
// 公司文件柜
function selfFile(cardUrl,name, e){
    var evt = e || window.event;
    evt.stopPropagation();
    // window.location = "/SRender?jsLoader=fileManage%2FfileManageLoader&tpl=FileManage%2FfileManage&cardUrl=" + data.cardUrl;
    window.location = "/SRender?jsLoader=explorer%2FexplorerLoader&tpl=explorer%2Fexplorer&cardUrl=" + cardUrl + "&name=" + name;
    return false;
}

/**
 *ES中根據 userUrl 对公司组织/boards进行数据分离,通用方法.
 */
function separateByCompanyId(data) {
    var arr = data;
    var map = {},
        dest = [];
    for (var i = 0; i < arr.length; i++) {
        var ai = arr[i];
        if (!map[ai.companyId]) {
            dest.push({
                companyId: ai.companyId,
                data: [ai]
            })
            map[ai.companyId] = ai;
        } else {
            for (var j = 0; j < dest.length; j++) {
                var dj = dest[j];
                if (dj.companyId == ai.companyId) {
                    dj.data.push(ai);
                    break;
                }
            }
        }
    }
    return dest;
};

/**
 * 返回一个路径的分级列表
 * 比如参数'/全部文件/文档/Java资料/',
 * 其结果为[
 *          {name:'全部文件', path: '/全部文件'},
 *          {name: '文档', path: '/全部文件/文档'},
 *          {name: 'Java资料', path: '/全部文件/文档/Java资料'}
 *        ]
 * @param path
 * @return {*}
 */
function pathWalker(path) {
    var deep = '/';
    var list = [];
    var px, i, p;
    if (path === '/') {
        return [{name:'', path: '/'}];
    }
    if (path.indexOf('/') === 0) {
        path = path.slice(1);
    }
    if (path.lastIndexOf('/') === path.length - 1) {
        path = path.slice(0, -1);
    }
    px = path.split('/');
    for (i = 0; i < px.length; i++) {
        p = {};
        p.name = px[i];
        deep += px[i];
        p.path = deep;
        list.push(p);
        deep += '/';
    }
    return list;
}


function fileDownloadCallBack(){
	$(document).triggerHandler("EVENT_DOWN_LOAD_FILE_OK");
}

String.prototype.startWith=function(str){     
	var reg=new RegExp("^"+str);     
	return reg.test(this);        
}  

String.prototype.endWith=function(str){     
	var reg=new RegExp(str+"$");     
	return reg.test(this);        
}

/**
 * 此方法是针对ES 通用树 common_tree 模仿该表数据类型递归数据数组转Tree树形结构的数据格式
 * 重构原来代码,取消递归
 * @param data tree数组
 * @param parentId 最上层节点
 * @returns {Array}
 */
function arrayToTreeJsonFuc (data,parentId){
    var result = [] , temp;
    for(var i in data){
        if(data[i].parentId==parentId){
            result.push(data[i]);
            temp = arrayToTreeJsonFuc(data,data[i].id);
            if(temp.length>0){
                data[i].children=temp;
            }
        }
    }
    return result;
}

