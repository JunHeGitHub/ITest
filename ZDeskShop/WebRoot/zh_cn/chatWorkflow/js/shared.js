var passedArgs=new Array();
var hasSelArea='false';
var USER_IE="MSIE";
var USER_IE_TYPE_IE="MSIE";
var USER_IE_TYPE_CHROME="KHTML";

var JV="9999.9999";
var errurl="/error.htm?v="+JV;
var meurl="/me/my.html?v="+JV;

var xmlDomVers = [
    "Microsoft.XmlDom",
    "MSXML2.DOMDocument",
    "MSXML2.DOMDocument.3.0",
    "MSXML2.DOMDocument.4.0",
    "MSXML2.DOMDocument.5.0"
//		"MSXML2.FreeThreadedDOMDocument"
//		,
//		"MSXML2.FreeThreadedDOMDocument.3.0",
//		"MSXML2.FreeThreadedDOMDocument.4.0",
//		"MSXML2.FreeThreadedDOMDocument.5.0"

];

if (navigator.appVersion.indexOf("MSIE") != -1) {
    USER_IE="MSIE";
    for (var i=xmlDomVers.length-1;i>=0;i--) {
        try {
            var xmlDom = new ActiveXObject(xmlDomVers[i]);
        } catch(e) {
            continue;
        }
        DOM_OBJ=xmlDomVers[i];
        break;
    }
} else if (navigator.appVersion.indexOf("KHTML") != -1) {
    USER_IE="KHTML";
}else{
    USER_IE="OTHER";
}

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


function loadXMLStr(str,sync,fn,pData) {
    if(USER_IE==USER_IE_TYPE_IE){
        var xmlObj = new ActiveXObject(DOM_OBJ);
        xmlObj.validateOnParse = false;
        xmlObj.resolveExternals = false;
        xmlObj.preserveWhiteSpace = false;

        if(fn){
            xmlObj.onreadystatechange=function(){if(xmlObj.readyState==4) fn(pData,xmlObj);};
        }
        if(sync){
            xmlObj.async = sync;
        }else{
            xmlObj.async = false;
        }
        if (str) {
            xmlObj.loadXML(str);
        }
        return xmlObj;
    }else {
        var ret;
        var xmlObj = new DOMParser();
        if(sync){
            xmlObj.async = sync;
        }else{
            xmlObj.async = false;
        }
        if(fn){
            xmlObj.onload=function(){fn(pData,xmlObj);};
        }
        if (str) {
            ret=xmlObj.parseFromString(str, "text/xml");
            return ret;
        }
        return xmlObj;
    }
};

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

function jqmUpListView(listT,nlStr,msgTemp,isAppend){
    listT.empty();
    if(!Z_IsEmpty(msgTemp)){
        if(Z_IsEmpty(isAppend) || isAppend=='true'){
            listT.append(msgTemp.sprintf(
                'c',
                new Date().prettyTime(),
                nlStr));
        }else if(isAppend=='false'){
            listT.prepend(msgTemp.sprintf(
                'c',
                new Date().prettyTime(),
                nlStr));
        }
//        scrollT.scrollTop($(document).height());
    }else{

        if(Z_IsEmpty(isAppend) || isAppend=='true'){
            listT.append(nlStr);
        }else if(isAppend=='false'){
            listT.prepend(nlStr);
        }

    }


    if (listT.jqmData("role") === "listview") {
        listT.listview("refresh");
    } else {
        listT.trigger("create");
    }

    //    $("#wrapper").scrollTop($(document).height());
};




function tipMsg(msg,tid){
    var tarId="loginTip";
    if(!Z_IsEmpty(tid)){
        tarId=tid;
    }
    var b=document.getElementById(tarId);
    b.innerHTML=msg;
}

//如果IndustryID是map 会包含 IndustryID 和 isFriend 参数 如：paramap["IndustryID"] paramap["isFriend"]等

function loadNextPage(ul,keyName,pageNum,listCB,content,focusArea,IndustryID,sType,hType,sOrder){
    if (!Z_IsEmpty20(keyName)) {
        keyName=Z_ClientStrEncode(keyName);
//            urlTmp= "/NCard/NCardService?ac=search&k=" + keyName + "&page=0";
//                ul.jqmData("lt-url", "/NCard/NCardService?ac=search&k=" + keyName );
//                $("#searchList").attr("data-lt-url","/NCard/NCardService?ac=search&k="+keyName+"&page=0");
    } else {
        keyName="";
    }
        
    var paramMap= {};
    paramMap["page"]=pageNum;
    paramMap["keyName"]=keyName;

    if(!Z_IsEmpty20(IndustryID)){
        if(typeof(IndustryID)=='string'){
            paramMap["IndustryID"]=IndustryID;
        }else if(typeof(IndustryID)=='object'){
            if(!Z_IsEmpty20(IndustryID.IndustryID)){
                paramMap["IndustryID"]=IndustryID.IndustryID;
            }

            if(!Z_IsEmpty20(IndustryID.isFriend)){
                paramMap["isFriend"]=IndustryID.isFriend;
            }

        }
    }
    
    if(!Z_IsEmpty20(focusArea)){
    	paramMap["focusArea"]=focusArea;
    }

    if(!Z_IsEmpty20(sType)){
        paramMap["sType"]=sType;
    }

    if(!Z_IsEmpty20(hType)){
        paramMap["hType"]=hType;
    }

    if(!Z_IsEmpty20(sOrder)){
        paramMap["sOrder"]=sOrder;
    }
    $.ajax({
        url: ul.jqmData("lt-url"),
        type: "POST",
        dataType: "json",
	    data: paramMap,
       // data: { "page": pageNum, "keyName":""+keyName+"","focusArea":""+focusArea+"","IndustryID":""+IndustryID+""},
        success: function (data) {
//                        if (data.flag == 1) {
//                ul.jqmData("lt-page", 1);
//                ul.jqmData("lt-pagesize", 10);
//                    ul.jqmData("lt-url", "/NCard/NCardService?ac=search&k=" + keyName + "&page=0");

            try{
                if(Z_IsEmpty20(data)){
                    $.mobile.loading("hide");
                    return;
                }

                procErr(data);

                var countAll=0;
                if(!Z_IsEmpty(data.countAll)){
                    countAll=data.countAll;
                    if(pageNum*10 >countAll){
                        pageNum=0;
                    }
                }

                jqmUpListView(ul,listCB(data));
                var tplTmp="<br>";
                ul.jqmData("data-lt-page", pageNum);

                if(!Z_IsEmpty(content)){
                    var pageJquery=$("#pageBef") ;
                    if(Z_IsEmpty(pageJquery) || pageJquery.attr('id')!='pageBef'){
//                var content = $("#industry-list .fav-list-content");
                        tplTmp +=' <div class="ui-block-a pag-left"><a href="#" data-role="button" id="pageBef" style="float: left"  >上一页</a></div>';
                        tplTmp +='<div class="ui-block-b pag-center" id="countAllDiv" >共'+countAll+'条记录</div>';
                        tplTmp +='<div class="ui-block-c pag-right"><a href="#" data-role="button"  id="pageNext"  style="float: right" >下一页</a></div>';
                        content.append( tplTmp);
                        content.trigger("create");
                    } else{
                        if(!Z_IsEmpty(data.countAll)){

                            $('#countAllDiv').empty();
                            $('#countAllDiv').append('共'+data.countAll+'条记录').trigger('create');
//                            $("#countAllDiv").text('共'+data.countAll+'条记录') ;
                        }
                    }
                }

            } catch(e){

            }

        },
        beforeSend: function () {
//            是否显示通过参数配置
//            $('<div class="page-tips">正在加载...</div>').appendTo(ul);
            // tips.html(options.language.loading);
            $.mobile.loading("show");
        },
        complete: function () {
//            var tips = ul.find(".page-tips");
//            if(!Z_IsEmpty(tips)){
//                tips.html('');
//            }
            //This.jqmRemoveData("lt-loader");
            //$('<div class="page-tips">' + languageTips + '</div>').appendTo(This);
//                        tips.html(languageTips);
            $.mobile.loading("hide");
        },
        error:function(reData){
//                var t=reData;
            // log.debug(reData+" swap down  failed ");
        }
    });
};

//ul,keyName,pageNum,listCB,content,focusArea,IndustryID,sType,hType,sOrder
//<ul id="searchList" data-role="listview" class="swap-list" data-lt-url="/NCard/NCardService?ac=loadhelpList"
// data-lt-recordcount="0" data-lt-pagesize="10" data-lt-page="0" data-lt-keyName="">


/***
 * @param param["listClass"]
 * @param param["isClean"] 是否删除列表中之前的数据
 * @param param["listCB"] 处理返回数据样式的callback
 * @param param["CB"] 程序运行后的callback
 *
 * @param param["servParam"] param to serv
 * @param param["servParam"]["pageNum"]
 */

function loadNextPageNoUi(param,servParam){
    showAlert();
    var listObj=$(param["listClass"]);
    var pageNum
    if(!Z_IsEmpty20(param["servParam"]["pageNum"])){
        pageNum = param["servParam"]["pageNum"];
    }else{
        param["servParam"]["pageNum"]=0;
        pageNum = 0;
    }

    $.ajax({
        url: listObj.attr("lt-url"),
        type: "POST",
        dataType: "json",
        data: param["servParam"],
        success: function (data) {
            try{
                if(Z_IsEmpty20(data)){
                    showAlertHide();
                    return;
                }

                procErr(data);

                var countAll=0;
                if(!Z_IsEmpty(data.countAll)){
                    countAll=data.countAll;
                    if(pageNum*10 >countAll){
                        pageNum=0;
                    }
                }

                if(param["isClean"]){
                    listObj.empty();
                }
                listObj.prepend(param["listCB"](data));
                listObj.attr("data-lt-page", pageNum);


                if(!Z_IsEmpty20(param["CB"])){
                    param["CB"]();
                }

            } catch(e){

            }
            showAlertHide();
        },
        beforeSend: function () {
//            是否显示通过参数配置
//            $('<div class="page-tips">正在加载...</div>').appendTo(ul);
            // tips.html(options.language.loading);
//            $.mobile.loading("show");
        },
        complete: function () {
//            var tips = ul.find(".page-tips");
//            if(!Z_IsEmpty(tips)){
//                tips.html('');
//            }
            //This.jqmRemoveData("lt-loader");
            //$('<div class="page-tips">' + languageTips + '</div>').appendTo(This);
//                        tips.html(languageTips);
//            $.mobile.loading("hide");
        },
        error:function(reData){
            showAlertHide();
//                var t=reData;
            // log.debug(reData+" swap down  failed ");
        }
    });
};


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

function getNick(aJID) {
    if (typeof(aJID) == 'undefined' || !aJID)
        return;
    var nick
    if (aJID.indexOf('@') != -1)
        nick = aJID.substring(0, aJID.indexOf('@'));
    else{
        nick=aJID;
    }
    return nick;
} ;

function cutResource(aJID) { // removes resource from a given jid
    if (typeof(aJID) == 'undefined' || !aJID)
        return;
    var retval = aJID;
    if (retval.indexOf("/") != -1)
        retval = retval.substring(0,retval.indexOf("/"));
    return retval;
};


function Z_MsgAddColor(msg,color) {
    if(color)
        msg = "<span style=\"color:" + color
            + ";\" class=msgnick>" + msg + "</span>&nbsp;";
    return msg;
};

var KVMapGL={};
function parseAndUpdate(status) {
    var sArr = status.split('_ZSP_');
    var needReBuild=false;
    var isMyAlert=false;
    for(var i=0; i<sArr.length; i++){
        if(!Z_IsEmpty20(sArr[i])){
            if(sArr[i]=='g'){
                KVMapGL['group']="g";
                var gLink=$("#btnMyUrl");
                gLink.before('<em class="favapply_num out_num">&nbsp;</em>');
                needReBuild=true;
            }else if(sArr[i]=='m'){
                KVMapGL['message']="m";
                var gLink=$("#btnMyUrl");
                gLink.before('<em class="favapply_num out_num">&nbsp;</em>');
                needReBuild=true;

                var msgH2Tmp=$("#msgH2");
                if(!Z_IsEmpty20(msgH2Tmp)){
                    msgH2Tmp.html('广播<span class="remind-num-msg">&nbsp;</span>');
                }

                var noReadATmp = $('#noReadA');
                if(!Z_IsEmpty20(noReadATmp)){
                    noReadATmp.append('<span class="remind-num-msg">&nbsp;</span>');
                }

//                var gLink=$("#btnMessageUrl");
//                gLink.before('<em class="favapply_num out_num">&nbsp;</em>');
//                needReBuild=true;
            }else if(sArr[i]=='h'){
                KVMapGL['helpInfo']="h";
                var gLink=$("#btnMyUrl");
                gLink.before('<em class="favapply_num out_num">&nbsp;</em>');

                var helpListBtnemTmp=$("#helpListBtnem");
                helpListBtnemTmp.html('&nbsp;');
//                <em id="helpListBtnem">&nbsp;</em>

                needReBuild=true;
            }else if(sArr[i].indexOf('f_')!=-1){
                KVMapGL['favCount']=sArr[i].substring(sArr[i].indexOf('f_')+2);
                if(!Z_IsEmpty20(KVMapGL['favCount']) && KVMapGL['favCount']!='0'){
                    var cardLink= $("#btnMyUrl");
                    cardLink.before('<em class="favapply_num out_num">&nbsp;</em>');
                    needReBuild=true;
                    isMyAlert=true;
                }
            }else if(sArr[i]=='c'){
                KVMapGL['newChat']="c";
                if(!isMyAlert){
                    var cardLink= $("#btnMyUrl");
                    cardLink.before('<em class="favapply_num out_num">&nbsp;</em>');
                    needReBuild=true;
                }
            }else if(sArr[i]=='qkf'){
                KVMapGL['qkf']="qkf";
                var cardLink= $("#btnKeFuUrl");
                cardLink.before('<em class="favapply_num out_num">&nbsp;</em>');

                try{
                    var QKefuPoint = $('#redPointQkefu');
                    $("#redPointQkefu").removeClass("red_point_no_alert_h");
                    $("#redPointQkefu").addClass("red_point_alert_h");
                }catch (e){}

            }else if(sArr[i]=='ca'){
                KVMapGL['ca']="ca";
                var cardLink= $("#btnKeFuUrl");
                cardLink.before('<em class="favapply_num out_num">&nbsp;</em>');

                try{
                    $("#redPointChat").removeClass("red_point_no_alert_h");
                    $("#redPointChat").addClass("red_point_alert_h");
                }catch (e){}
            }
        }
    }

//    if(needReBuild){
//        var toolBarTT=$("#mainToolBar");
//        toolBarTT.trigger('create');
//    }
};

function hideBtn(btnId) {
    $(btnId).closest('.ui-btn').hide();
};

function sCheckV(cid,cv) {
    if(cv=='true'){
        $("#"+cid).attr("checked",true);
//        $("#"+cid).attr("checked",true).val("refresh");
    }else {
        $("#"+cid).attr("checked",false);
//        $("#"+cid).attr("checked",false).val("refresh");
    }
};


function procErr(data) {
    if(!Z_IsEmpty(data) && !Z_IsEmpty(data.error)){
        if (data.error=='-1000'){
            alert('请完善个人信息，开启功能');
//             window.location="/card/detail/cardPage.htm";
//             window.location="/authPage/authOne.htm";
            window.location="/card/update.htm?msg=isVisitor&v="+JV;
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
        }
    }
};
//function addBeginLike(){
//    var _img=document.createElement("img");
//    _img.id="dt_like_list_begin";
//    _img.src="/css/like_list_icon.png";
//    $("#dt_like_list").append(_img);
//};
//
//function createSpanDom(userName){
//    var _span=document.createElement("span");
////    _a.id="dt_like_list_"+userId;
////    _a.className="dt_nick";
////    _a.ontouchstart=function(){};
////    _a.href="/timeline/"+userId;
//    _span.innerHTML="&nbsp;&nbsp;"+userName;
//    return _span;
//};
//
//function createSpanDom2(userName){
//    var _a=document.createElement("a");
////    _a.id="dt_like_list_"+userId;
//    _a.className="dt_nick";
////    _a.ontouchstart=function(){};
//    _a.href="#";
//    _a.innerHTML="&nbsp;&nbsp;"+userName;
//    return _a;
//};
//
//function showItem(id){
//    $('#'+id).css('display','block');
//};
//
//function _checkCss(css){
//    var _div = document.createElement("div");
//    if (css in _div.style ) return true;
//    css = css.replace(/^[a-z]/, function(val){return val.toUpperCase();});
//    var _vendors = ["O","Moz","Webkit"];
//    for(var i=0,len=_vendors.length;i<len;i++){
//        if(_vendors[i] + css in _div.style){return true;break;}
//    }
//    return false;
//}

//    function _opacity(id,p){
//        var _s =$('#'+id).style;
//        if("opacity"in _s){
//            _s.opacity=p;
//        }else if("MozOpacity"in _s){
//            _s.MozOpacity=p;
//        }else if("filter"in _s){
//            _s.filter="alpha(opacity="+p*100+")";
//        }
//    };


    function activityLike() {

        $("#dt_like_inner_div").html($("#dt_like_inner_div").html()+"&nbsp;&nbsp;test");
        $("#dt_like_count").html(parseInt($("#dt_like_count").html())+1);

//    var _item=createSpanDom('test');
//    if(!$("#dt_like_list_begin")){addBeginLike();}
//    if($("#dt_like_list_begin").nextSibling){
//        $("#dt_like_list").insertBefore(_item,$("dt_like_list_begin").nextSibling);
//    }else{
//        $("#dt_like_list").append(_item);
//    }
//     $("#dt_like_count").html(parseInt($("#dt_like_count").html())+1);
////    $("#dt_like_count").innerHTML=;
//    showItem("dt_like_top");
//    showItem("dt_like_main");
//    if(!_checkCss("transition")){return;}
//    _opacity("dt_like_list_begin",0);
//        _opacity("dt_like_icon",1);
//    setTimeout(function(){
//        with($("#dt_like_icon").style){
//            left="8px";
//            top="8px";
//            width="15px";
//            height="20px";
//        }
//        _._opacity("dt_like_icon",1);
//    },100);


}



function getIndustryName(IndustryID) {
    if(IndustryID=='1'){
        return 'IT&#183;互联网&#183;游戏';
    }
    else if(IndustryID=='5'){
        return '通信';
    }
    else if(IndustryID=='13'){
        return '金融业';
    }
    else if(IndustryID=='4'){
        return '电子&#183;微电子';
    }
    else if(IndustryID=='6'){
        return '广告&#183;会展&#183;公关';
    }
    else if(IndustryID=='7'){
        return '房地产开发&#183;建筑与工程';
    }
    else if(IndustryID=='8'){
        return '物业管理&#183;商业中心';
    }
    else if(IndustryID=='9'){
        return '家居&#183;室内设计&#183;装潢';
    }
    else if(IndustryID=='10'){
        return '中介服务';
    }
    else if(IndustryID=='11'){
        return '专业服务';
    }
    else if(IndustryID=='12'){
        return '检验&#183;检测&#183;认证';
    }
    else if(IndustryID=='14'){
        return '贸易&#183;进出口';
    }
    else if(IndustryID=='15'){
        return '媒体&#183;出版&#183;文化传播';
    }
    else if(IndustryID=='16'){
        return '印刷&#183;包装&#183;造纸';
    }
    else if(IndustryID=='17'){
        return '快速消费品';
    }
    else if(IndustryID=='18'){
        return '耐用消费品';
    }
    else if(IndustryID=='19'){
        return '玩具&#183;工艺品&#183;收藏品&#183;奢侈品';
    }
    else if(IndustryID=='20'){
        return '家电业';
    }
    else if(IndustryID=='21'){
        return '办公设备&#183;用品';
    }
    else if(IndustryID=='22'){
        return '旅游&#183;酒店&#183;餐饮服务';
    }
    else if(IndustryID=='23'){
        return '批发&#183;零售';
    }
    else if(IndustryID=='24'){
        return '交通&#183;运输&#183;物流';
    }
    else if(IndustryID=='25'){
        return '娱乐&#183;运动&#183;休闲';
    }
    else if(IndustryID=='26'){
        return '制药&#183;生物工程';
    }
    else if(IndustryID=='27'){
        return '医疗&#183;保健&#183;美容&#183;卫生服务';
    }
    else if(IndustryID=='28'){
        return '医疗设备&#183;器械';
    }
    else if(IndustryID=='29'){
        return '环保行业';
    }
    else if(IndustryID=='30'){
        return '石油&#183;化工&#183;矿产&#183;采掘&#183;冶炼&#183;原材料';
    }
    else if(IndustryID=='31'){
        return '能源';
    }
    else if(IndustryID=='32'){
        return '仪器&#183;仪表&#183;工业自动化&#183;电气';
    }
    else if(IndustryID=='33'){
        return '汽车&#183;摩托车';
    }
    else if(IndustryID=='34'){
        return '机械制造&#183;机电&#183;重工';
    }
    else if(IndustryID=='35'){
        return '原材料及加工';
    }
    else if(IndustryID=='36'){
        return '农&#183;林&#183;牧&#183;渔';
    }
    else if(IndustryID=='37'){
        return '航空&#183;航天研究与制造';
    }
    else if(IndustryID=='38'){
        return '船舶制造';
    }
    else if(IndustryID=='39'){
        return '教育&#183;培训&#183;科研&#183;院校';
    }
    else if(IndustryID=='40'){
        return '政府&#183;非营利机构';
    }
    else if(IndustryID=='0'){
        return '其他行业';
    }else return '&nbsp;';
};

function getAlumniName(alumni) {
if(alumni=='1'){
    alumni='学士';
}else if(alumni=='2'){
    alumni='硕士';
} else if(alumni=='3'){
    alumni='博士';
} else if(alumni=='4'){
    alumni='教职工';
} else return '&nbsp;';
    return alumni;
};
function getAreaName(focusArea) {
if(focusArea=='1'){
    focusArea='北京';
}
else if(focusArea=='5'){
    focusArea='天津';
}
else if(focusArea=='13'){
    focusArea='上海';
}
else if(focusArea=='4'){
    focusArea='重庆';
}
else if(focusArea=='6'){
    focusArea='黑龙江';
}
else if(focusArea=='7'){
    focusArea='辽宁';
}
else if(focusArea=='8'){
    focusArea='吉林';
}
else if(focusArea=='9'){
    focusArea='河北';
}
else if(focusArea=='10'){
    focusArea='山西';
}
else if(focusArea=='11'){
    focusArea='山东';
}
else if(focusArea=='12'){
    focusArea='江苏';
}
else if(focusArea=='14'){
    focusArea='河南';
}
else if(focusArea=='15'){
    focusArea='湖北';
}
else if(focusArea=='16'){
    focusArea='浙江';
}
else if(focusArea=='17'){
    focusArea='福建';
}
else if(focusArea=='18'){
    focusArea='广东';
}
else if(focusArea=='19'){
    focusArea='江西';
}
else if(focusArea=='20'){
    focusArea='湖南';
}
else if(focusArea=='21'){
    focusArea='贵州';
}
else if(focusArea=='22'){
    focusArea='广西';
}
else if(focusArea=='23'){
    focusArea='云南';
}
else if(focusArea=='24'){
    focusArea='四川';
}
else if(focusArea=='25'){
    focusArea='陕西';
}
else if(focusArea=='26'){
    focusArea='甘肃';
}
else if(focusArea=='27'){
    focusArea='宁夏';
}
else if(focusArea=='28'){
    focusArea='新疆';
}
else if(focusArea=='29'){
    focusArea='西藏';
}
else if(focusArea=='31'){
    focusArea='内蒙古';
}else if(focusArea=='37'){
    focusArea='青海';
}else if(focusArea=='33'){
    focusArea='海南';
}else if(focusArea=='34'){
    focusArea='台湾';
}else if(focusArea=='35'){
    focusArea='香港';
}else if(focusArea=='36'){
    focusArea='澳门';
}else if(focusArea=='32'){
    focusArea='安徽';
}else if(focusArea=='30'){
    focusArea='海外';
}else focusArea='&nbsp;';
    return focusArea;
};
function getAlumniName(alumni) {
if(alumni=='1'){
    alumni='学士';
}
else if(alumni=='2'){
    alumni='硕士';
}
else if(alumni=='3'){
    alumni='博士';
}
else if(alumni=='4'){
    alumni='教职工';
}
else alumni='&nbsp;';
    return alumni;
};


function getHTypeName(hTypeRet) {
    if(hTypeRet=='1'){
        hTypeRet='房产';
    }
    else if(hTypeRet=='2'){
        hTypeRet='跳蚤市场';
    }
    else if(hTypeRet=='3'){
        hTypeRet='求职招聘';
    }
    else if(hTypeRet=='4'){
        hTypeRet='生活';
    }
    else if(hTypeRet=='5'){
        hTypeRet='服务';
    }
    else if(hTypeRet=='6'){
        hTypeRet='婚庆摄影';
    }
    else if(hTypeRet=='7'){
        hTypeRet='旅游休闲';
    }
    else if(hTypeRet=='8'){
        hTypeRet='商务服务';
    }
    else if(hTypeRet=='9'){
        hTypeRet='教育培训';
    }
    else if(hTypeRet=='10'){
        hTypeRet='兼职';
    }
    else if(hTypeRet=='11'){
        hTypeRet='婚恋';
    }
    else if(hTypeRet=='12'){
        hTypeRet='医疗健康';
    }
    else if(hTypeRet=='13'){
        hTypeRet='票务';
    }
    else if(hTypeRet=='14'){
        hTypeRet='宠物';
    }
    else if(hTypeRet=='15'){
        hTypeRet='汽车';
    }
    else if(hTypeRet=='16'){
        hTypeRet='艺术';
    }
    else if(hTypeRet=='17'){
        hTypeRet='餐饮美食';
    }else if(hTypeRet=='18'){
        hTypeRet='求助';
    }
    else if(hTypeRet=='99'){
        hTypeRet='其它';
    }else hTypeRet='';

    return hTypeRet;
};



function fillCombo(comboId){
     if(!Z_IsEmpty(comboIdMap[comboId])){
         var comboObj=$("#"+comboId);
         if(!Z_IsEmpty(comboObj)){
             comboObj.append(comboIdMap[comboId]);
             comboObj.selectmenu("refresh");
         }
     }
};
var comboIdMap={};
comboIdMap["focusArea"]="";
comboIdMap["focusArea"]+="<option value=\"\">地域</option>";
comboIdMap["focusArea"]+="<option value=\"1\">北京</option>";
comboIdMap["focusArea"]+="<option value=\"5\">天津</option>";
comboIdMap["focusArea"]+="<option value=\"13\">上海</option>";
comboIdMap["focusArea"]+="<option value=\"4\">重庆</option>";
comboIdMap["focusArea"]+="<option value=\"6\">黑龙江</option>";
comboIdMap["focusArea"]+="<option value=\"7\">辽宁</option>";
comboIdMap["focusArea"]+="<option value=\"8\">吉林</option>";
comboIdMap["focusArea"]+="<option value=\"9\">河北</option>";
comboIdMap["focusArea"]+="<option value=\"10\">山西</option>";
comboIdMap["focusArea"]+="<option value=\"11\">山东</option>";
comboIdMap["focusArea"]+="<option value=\"12\">江苏</option>";
comboIdMap["focusArea"]+="<option value=\"14\">河南</option>";
comboIdMap["focusArea"]+="<option value=\"15\">湖北</option>";
comboIdMap["focusArea"]+="<option value=\"16\">浙江</option>";
comboIdMap["focusArea"]+="<option value=\"17\">福建</option>";
comboIdMap["focusArea"]+="<option value=\"18\">广东</option>";
comboIdMap["focusArea"]+="<option value=\"19\">江西</option>";
comboIdMap["focusArea"]+="<option value=\"20\">湖南</option>";
comboIdMap["focusArea"]+="<option value=\"21\">贵州</option>";
comboIdMap["focusArea"]+="<option value=\"22\">广西</option>";
comboIdMap["focusArea"]+="<option value=\"23\">云南</option>";
comboIdMap["focusArea"]+="<option value=\"24\">四川</option>";
comboIdMap["focusArea"]+="<option value=\"25\">陕西</option>";
comboIdMap["focusArea"]+="<option value=\"26\">甘肃</option>";
comboIdMap["focusArea"]+="<option value=\"27\">宁夏</option>";
comboIdMap["focusArea"]+="<option value=\"28\">新疆</option>";
comboIdMap["focusArea"]+="<option value=\"29\">西藏</option>";
comboIdMap["focusArea"]+="<option value=\"31\">内蒙古</option>";
comboIdMap["focusArea"]+="<option value=\"37\">青海</option>";
comboIdMap["focusArea"]+="<option value=\"33\">海南</option>";
comboIdMap["focusArea"]+="<option value=\"34\">台湾</option>";
comboIdMap["focusArea"]+="<option value=\"35\">香港</option>";
comboIdMap["focusArea"]+="<option value=\"36\">澳门</option>";
comboIdMap["focusArea"]+="<option value=\"32\">安徽</option>";
comboIdMap["focusArea"]+="<option value=\"30\">海外</option>";


comboIdMap["IndustryID"]="";
comboIdMap["IndustryID"]+="<option value=\"\">行业</option>";
comboIdMap["IndustryID"]+="<option value=\"1\">IT&#183;互联网&#183;游戏</option>";
comboIdMap["IndustryID"]+="<option value=\"5\">通信（设备&#183;运营&#183;增值服务）</option>";
comboIdMap["IndustryID"]+="<option value=\"13\">金融业（投资&#183;保险&#183;证券&#183;基金）</option>";
comboIdMap["IndustryID"]+="<option value=\"4\">电子&#183;微电子</option>";
comboIdMap["IndustryID"]+="<option value=\"6\">广告&#183;会展&#183;公关</option>";
comboIdMap["IndustryID"]+="<option value=\"7\">房地产开发&#183;建筑与工程</option>";
comboIdMap["IndustryID"]+="<option value=\"8\">物业管理&#183;商业中心</option>";
comboIdMap["IndustryID"]+="<option value=\"9\">家居&#183;室内设计&#183;装潢</option>";
comboIdMap["IndustryID"]+="<option value=\"10\">中介服务</option>";
comboIdMap["IndustryID"]+="<option value=\"11\">专业服务（咨询&#183;财会&#183;法律等）</option>";
comboIdMap["IndustryID"]+="<option value=\"12\">检验&#183;检测&#183;认证</option>";
comboIdMap["IndustryID"]+="<option value=\"14\">贸易&#183;进出口</option>";
comboIdMap["IndustryID"]+="<option value=\"15\">媒体&#183;出版&#183;文化传播</option>";
comboIdMap["IndustryID"]+="<option value=\"16\">印刷&#183;包装&#183;造纸</option>";
comboIdMap["IndustryID"]+="<option value=\"17\">快速消费品（食品&#183;饮料&#183;日化&#183;烟酒等）</option>";
comboIdMap["IndustryID"]+="<option value=\"18\">耐用消费品（服饰&#183;纺织&#183;皮革&#183;家具）</option>";
comboIdMap["IndustryID"]+="<option value=\"19\">玩具&#183;工艺品&#183;收藏品&#183;奢侈品</option>";
comboIdMap["IndustryID"]+="<option value=\"20\">家电业</option>";
comboIdMap["IndustryID"]+="<option value=\"21\">办公设备&#183;用品</option>";
comboIdMap["IndustryID"]+="<option value=\"22\">旅游&#183;酒店&#183;餐饮服务</option>";
comboIdMap["IndustryID"]+="<option value=\"23\">批发&#183;零售</option>";
comboIdMap["IndustryID"]+="<option value=\"24\">交通&#183;运输&#183;物流</option>";
comboIdMap["IndustryID"]+="<option value=\"25\">娱乐&#183;运动&#183;休闲</option>";
comboIdMap["IndustryID"]+="<option value=\"26\">制药&#183;生物工程</option>";
comboIdMap["IndustryID"]+="<option value=\"27\">医疗&#183;保健&#183;美容&#183;卫生服务</option>";
comboIdMap["IndustryID"]+="<option value=\"28\">医疗设备&#183;器械</option>";
comboIdMap["IndustryID"]+="<option value=\"29\">环保行业</option>";
comboIdMap["IndustryID"]+="<option value=\"30\">石油&#183;化工&#183;矿产&#183;采掘&#183;冶炼&#183;原材料</option>";
comboIdMap["IndustryID"]+="<option value=\"31\">能源（电力&#183;石油）&#183;水利</option>";
comboIdMap["IndustryID"]+="<option value=\"32\">仪器&#183;仪表&#183;工业自动化&#183;电气</option>";
comboIdMap["IndustryID"]+="<option value=\"33\">汽车&#183;摩托车（制造&#183;维护&#183;配件&#183;销售&#183;服务）</option>";
comboIdMap["IndustryID"]+="<option value=\"34\">机械制造&#183;机电&#183;重工</option>";
comboIdMap["IndustryID"]+="<option value=\"35\">原材料及加工（金属&#183;木材&#183;橡胶&#183;塑料&#183;玻璃&#183;陶瓷&#183;建材）</option>";
comboIdMap["IndustryID"]+="<option value=\"36\">农&#183;林&#183;牧&#183;渔</option>";
comboIdMap["IndustryID"]+="<option value=\"37\">航空&#183;航天研究与制造</option>";
comboIdMap["IndustryID"]+="<option value=\"38\">船舶制造</option>";
comboIdMap["IndustryID"]+="<option value=\"39\">教育&#183;培训&#183;科研&#183;院校</option>";
comboIdMap["IndustryID"]+="<option value=\"40\">政府&#183;非营利机构</option>";
comboIdMap["IndustryID"]+="<option value=\"0\">其他行业</option>";

comboIdMap["hType"]="";
comboIdMap["hType"]+="<option value=\"\">所有分类</option>";
comboIdMap["hType"]+="<option value=\"18\">求助</option>";
comboIdMap["hType"]+="<option value=\"1\">房产</option>";
comboIdMap["hType"]+="<option value=\"2\">跳蚤市场</option>";
comboIdMap["hType"]+="<option value=\"3\">求职招聘</option>";
comboIdMap["hType"]+="<option value=\"4\">生活</option>";
comboIdMap["hType"]+="<option value=\"5\">服务</option>";
comboIdMap["hType"]+="<option value=\"6\">婚庆</option>";
comboIdMap["hType"]+="<option value=\"7\">旅游休闲</option>";
comboIdMap["hType"]+="<option value=\"8\">商务服务</option>";
comboIdMap["hType"]+="<option value=\"9\">教育培训</option>";
comboIdMap["hType"]+="<option value=\"10\">兼职</option>";
comboIdMap["hType"]+="<option value=\"11\">婚恋</option>";
comboIdMap["hType"]+="<option value=\"12\">医疗健康</option>";
comboIdMap["hType"]+="<option value=\"13\">票务</option>";
comboIdMap["hType"]+="<option value=\"14\">宠物</option>";
comboIdMap["hType"]+="<option value=\"15\">汽车</option>";
comboIdMap["hType"]+="<option value=\"16\">艺术</option>";
comboIdMap["hType"]+="<option value=\"17\">餐饮美食</option>";
//18已经有了 注意
comboIdMap["hType"]+="<option value=\"99\">其它</option>";


comboIdMap["isFriend"]="";


//var focusAreaStr="";



//if(!is_weixin()){
//    location.href ="/zcloud";
//};


//$('#msga').trigger('click');





//ajax请求时调的函数
function showAlert(){
    var maskbox=document.createElement('div');
    var maskbox_con=document.createElement('div');
    var imgs=document.createElement('img');
    maskbox.id='maskbox';
    maskbox_con.className='maskbox_con';
    imgs.src='/images/loadings.gif';
    maskbox_con.appendChild(imgs);
    maskbox.appendChild(maskbox_con);
    document.body.appendChild(maskbox);
}
//ajax请求完成时调的函数
function showAlertHide(){
    var maskbox=document.getElementById('maskbox') ;
    document.body.removeChild(maskbox);
}
