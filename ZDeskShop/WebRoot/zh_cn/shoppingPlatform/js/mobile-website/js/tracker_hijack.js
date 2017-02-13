var refer=document.referrer?document.referrer:""; 
if(refer!="")
    refer=encodeURIComponent(refer);
//alert(refer);
var pars=document.location.search;
//alert(pars);
//alert(pars.indexOf("?"));
//alert(pars.length>1);
var input=new Object();
if (pars && pars.indexOf("?")==0 && pars.length>1){
    //alert(pars);
    pars=pars.substr(1);
    //alert(pars);
    var list=pars.split("&");
    for(var n=0;n<list.length;n++){
        //alert(list[n]);
        var item=list[n].split("=");
        if(item.length==2){
            input[item[0]]=item[1];
        }
		
    }
}
//alert(input.tracker_u);
var tracker_u=input.tracker_u?input.tracker_u:"";
var tracker_type=input.tracker_type?input.tracker_type:"";
var tracker_pid=input.tracker_pid?input.tracker_pid:"";
var tracker_src=input.tracker_src?input.tracker_src:"";
//var adgroupKeywordID=input.adgroupKeywordID?input.adgroupKeywordID:"";
//trackerContainer.addParameter(new Parameter("adgroupKeywordID",adgroupKeywordID));
//alert(refer);
//if(refer.indexOf("yihaodian.com")!=-1){
//    refer="";
//}

if(refer!="" && ""==tracker_u){
    tracker_type="0";
}



if(jQuery.cookie("unionKey"))
{
	trackerContainer.addParameter(new Parameter("tracker_u",jQuery.cookie("unionKey")));
}
if (jQuery.cookie("adgroupKeywordID")){
	trackerContainer.addParameter(new Parameter("adgroupKeywordID", jQuery.cookie("adgroupKeywordID")));
}

trackerContainer.addParameter(new Parameter("tracker_src",tracker_src));


/**************************普通跟踪信息********************/
var info_refer=document.referrer?document.referrer:"";
if(info_refer!="")
    info_refer=encodeURIComponent(info_refer);
    
trackerContainer.addParameter(new Parameter("infoPreviousUrl",info_refer));//前一页地址
trackerContainer.addParameter(new Parameter("infoTrackerSrc",tracker_src));//页面来源
if(jQuery.cookie("yihaodian_uid"))
trackerContainer.addParameter(new Parameter("endUserId",jQuery.cookie("yihaodian_uid")));

var sendTrackerCookie="";
if(jQuery.cookie("msessionid"))
sendTrackerCookie="msessionid:"+jQuery.cookie("msessionid");
if(jQuery.cookie("uname"))
sendTrackerCookie+=",uname:"+jQuery.cookie("uname");
if(jQuery.cookie("unionKey"))
sendTrackerCookie+=",unionKey:"+jQuery.cookie("unionKey");
if(jQuery.cookie("unionType"))
sendTrackerCookie+=",unionType:"+jQuery.cookie("unionType");
if(jQuery.cookie("tracker"))
sendTrackerCookie+=",tracker:"+jQuery.cookie("tracker");
if(jQuery.cookie("LTINFO"))
sendTrackerCookie+=",LTINFO:"+jQuery.cookie("LTINFO");

trackerContainer.addParameter(new Parameter("cookie",sendTrackerCookie));///把cookie添加到trackerContainer中

/*trackerContainer.addStock("8486","1:34,56:45,67:56");
trackerContainer.addStock("4444","2:34,3:45,5:56");
trackerContainer.addCommonAttached("2","hello");*/



/**************************finish************************/
if(getQueryStringRegExp("fee")!=null){
trackerContainer.addParameter(new Parameter("fee",getQueryStringRegExp("fee")));
}

trackerContainer.addParameter(new Parameter("provinceId",jQuery.cookie("provinceId")));
trackerContainer.addParameter(new Parameter("cityId",jQuery.cookie("cityId")));
var tracker_params = new Array();
//对于记录广告位置的cookie,当下个跳转页面
function clearTrackPositionToCookie(name,value){	
		var date = new Date();
    	date.setTime(date.getTime() - 10000);
    	document.cookie = name+"="+value+";path=/;domain=."+no3wUrl+";expires=" + date.toGMTString();		
}



/**********************发送信息***************************/

function initHijack(){
	
	jQuery.ajax({ 
	            async:false, 
	            url: trackerContainer.toUrl(),  
	            type: 'GET',   
	            dataType: 'jsonp', 
	            jsonp: 'jsoncallback'
	        });

}
jQuery(document).ready(function(){
	if(getCookies("linkPosition")!=null){
		linkPosition=getCookies("linkPosition");
		trackerContainer.addParameter(new Parameter("linkPosition",linkPosition));
		clearTrackPositionToCookie("linkPosition",linkPosition);
		initHijack();
	}else if(getCookies("buttonPosition")!=null){
		buttonPosition=getCookies("buttonPosition");
		trackerContainer.addParameter(new Parameter("buttonPosition",buttonPosition));
		clearTrackPositionToCookie("buttonPosition",buttonPosition);
		initHijack();
	}else if(getCookies("buttonPosition")==null&&getCookies("linkPosition")==null){
		callLoadlinkCookie();
	}else{
	  initHijack();
	}


});




function callTracker(provinceId){
trackerContainer.addParameter(new Parameter("provinceId",provinceId));
trackerContainer.addParameter(new Parameter("cityId",jQuery.cookie("cityId")));
	jQuery.ajax({ 
            async:true, 
            url: trackerContainer.toUrl(),  
            type: 'GET',   
            dataType: 'jsonp', 
            jsonp: 'jsoncallback'
        });
}
