
//var basUrl = 'http://qinghua.zing400.com';
//var basIpUrl = 'http://58.83.224.43';
var basIpUrl = 'http://zinglabs.zing400.com';

var basUrl = basIpUrl;
//var wid='gh_b6927ed7cde9';
var wid='gh_36aed7b39995';

var myurl="/NCard/NCardService?ac=WXCP&pn=cardPage&authType=snsapi_base&wid="+wid;

//var basUrl = 'http://192.168.199.240:88';
var dataForWeixin = {
    //appId: "wxa7e5128426626ca5",
//    appId: "wx3f48c9946023a1fd",
    appId: "wx36e5868338747635",
//    appId: "wx3f48c9946023a1fd",
    MsgImg: "/css/weicard.jpg",
    TLImg: "",
    url: "",
    title: "",
    MstTitle:"",
    desc: "",
    fakeid: "",
    weibodesc: "",
    callback: function () { }
};
function SetupWeiXinShareInfo(url, title, desc, weibodesc, img,msgtit) {
    dataForWeixin.url = url;
    dataForWeixin.title = title;
    dataForWeixin.desc = desc;
    dataForWeixin.weibodesc = weibodesc;
    dataForWeixin.MstTitle = msgtit;
    if (img != '') {
        dataForWeixin.MsgImg = img;
    }

    if(isJsApiOk=="true"){
        wx.onMenuShareAppMessage({
            title: title,
            desc: desc,
            link: basUrl + dataForWeixin.url,
            imgUrl: basUrl + dataForWeixin.MsgImg,
            trigger: function (res) {
//                alert('用户点击发送给朋友');
            },
            success: function (res) {
//                alert('已分享');
            },
            cancel: function (res) {
//                alert('已取消');
            },
            fail: function (res) {
//                alert(JSON.stringify(res));
            }
        });

        wx.onMenuShareTimeline({
            title: title,
            link: basUrl + dataForWeixin.url,
            imgUrl: basUrl + dataForWeixin.MsgImg,
            trigger: function (res) {
//                alert('用户点击分享到朋友圈');
            },
            success: function (res) {
//                alert('已分享');
            },
            cancel: function (res) {
//                alert('已取消');
            },
            fail: function (res) {
//                alert(JSON.stringify(res));
            }
        });
    }

}
(function () {
    var onBridgeReady = function () {
        WeixinJSBridge.on('menu:share:appmessage', function (argv) {
            alert(basUrl + dataForWeixin.url);
            WeixinJSBridge.invoke('sendAppMessage', {
                "appid": dataForWeixin.appId,
                "img_url": basUrl + dataForWeixin.MsgImg,
                "img_width": "120",
                "img_height": "120",
                "link": basUrl + dataForWeixin.url,
                "desc": dataForWeixin.MstTitle,
                "title": dataForWeixin.title
            }, function (res) { //alert(res.err_msg);if (res.err_msg == "send_app_msg:ok") {
                (dataForWeixin.callback)();// }
            });
        });
        WeixinJSBridge.on('menu:share:timeline', function (argv) {
//            (dataForWeixin.callback)();
            WeixinJSBridge.invoke('shareTimeline', {
                "img_url": basUrl + dataForWeixin.MsgImg,
                "img_width": "120",
                "img_height": "120",
                "link": basUrl + dataForWeixin.url,
                "desc": dataForWeixin.MstTitle,
                "title": dataForWeixin.desc
            }, function (res) { (dataForWeixin.callback)(); });
        });
        WeixinJSBridge.on('menu:share:weibo', function (argv) {
            WeixinJSBridge.invoke('shareWeibo', {
                "content": dataForWeixin.weibodesc + basUrl + dataForWeixin.url,
                "url": basUrl + dataForWeixin.url
            }, function (res) { (dataForWeixin.callback)(); });
        });
        WeixinJSBridge.on('menu:share:facebook', function (argv) {
            WeixinJSBridge.invoke('shareFB', {
                "img_url": basUrl + dataForWeixin.MsgImg,
                "img_width": "120",
                "img_height": "120",
                "link": basUrl + dataForWeixin.url,
                "desc": dataForWeixin.desc,
                "title": dataForWeixin.title
            }, function (res) { });
        });


        window.setTimeout(function() {
            hideToobarWX();
        }, 4000);

        WeixinJSBridge.call('hideOptionMenu');
        WeixinJSBridge.call('showOptionMenu');

//        alert('showOptionMenu');

    };
    if (document.addEventListener) {
        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
    } else if (document.attachEvent) {
        document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
    }
})();

function hideToobarWX() {
    WeixinJSBridge.call('hideToolbar');
}

function back2WX() {
    WeixinJSBridge.invoke('closeWindow',{},function(res){
        //alert("back2WX");
    });
};

//jsApiList: [
//    'checkJsApi',
//    'chooseImage',
//    'uploadImage',
//    'startRecord',
//    'stopRecord',
//    'onVoiceRecordEnd',
//    'uploadVoice'
//]
var recordTime=0;
var isUpdateRecord="false";
var isJsApiOk="false";

function initJSSDK(appArr,callback) {
    $.ajax({
        async: true,
        url: '/WXINFO/WXSDK/weixin?ajaxAction=WX_AJAX_JSAPI_SIGN',
        type: "POST",
        dataType: "json",
        data: { "wid": wid, "url": Z_EU(window.location.href)},
        success: function (data) {
            if (!Z_IsEmpty20(data.nonceStr)) {
                wx.config({
                    debug: true,
                    appId: '' + dataForWeixin.appId + '',
                    timestamp: data.timestamp,
                    nonceStr: '' + data.nonceStr + '',
                    signature: '' + data.signature + '',
                    jsApiList: appArr
                });

                wx.ready(function () {
//                            alert("wx.ready ");
                    wx.checkJsApi({
                        jsApiList: appArr,
                        success: function (res) {
                            isJsApiOk = "true";
                            if(typeof callback != "undefined"){
                                callback();
                            }
                            serverLogMH("checkJsApi success  "+res.errMsg);
                        },
                        fail: function (res) {
                            serverLogMH("checkJsApi fail  "+res.errMsg);
                            if(typeof callback != "undefined"){
                                callback();
                            }
                        }
                    });
                });

                wx.error(function (res) {
                    serverLogMH("wx.error "+res.errMsg);
                    if(typeof callback != "undefined"){
                        callback();
                    }
                });

//                        wx.onVoiceRecordEnd({
//                            // 录音时间超过一分钟没有停止的时候会执行 complete 回调
//                            complete: function (res) {
//                                alert("onVoiceRecordEnd begin");
//                                isUpdateRecord="true";
//                                var localId = res.localId;
//                                stopuploadVoice();
//                                alert(localId);
//                            }
//                        });

            }
        },

        error: function (reData) {
//            alert("error " + reData);
        }
    });
};


//    上传结束后才改变录音状态
function stopuploadVoice() {
    $(".startRecord").html('开始录音');
    isUpdateRecord="false";
    recordTime=0;
};

var isUploadingImg="false";
var imgLocalIds;
function stopUploadImg() {
    isUploadingImg="false";
    imgLocalIds="false";
    if(recordPageIndex!='false'){
        layer.close(recordPageIndex);
        recordPageIndex="false";
    }
}
function uploadImgwx() {

}

function uploadImgwx(paramTmp) {
    serverLogMH("uploadImgwx start");
    isUploadingImg="true";
    wx.uploadImage({
        localId: imgLocalIds[0],
        isShowProgressTips: 1, // 默认为1，显示进度提示
        success: function (res) {
//            alert("sucess "+res.errMsg);
            var serverId = res.serverId;
            serverId=Z_EU(serverId);
//                            alert("uploadImage "+serverId);

//                            上传速度很快，不加状态防护。需要时按声音逻辑调整。
            $.ajax({
                async: false,
                url: '/WXINFO/WXSDK/weixin?ajaxAction=WX_AJAX_UPLOAD_MEDIA',
                type: "POST",
                dataType: "json",
                data: { "wid":wid,"serverId": serverId, "type":'Z_GROUP_IMG_MSG' ,"bId":paramTmp["bId"],"bType":paramTmp["bType"]},
                success: function (data) {
//                                    不加上传成功提示，会在页面中看到
                    stopUploadImg();
                    if(!Z_IsEmpty(data) && !Z_IsEmpty(data.retcode) && data.retcode==0 ){


                    }else{
                        alert("上传失败，请稍后重试");
                    }

                    if(recordPageIndex!='false'){
                        layer.close(recordPageIndex);
                        recordPageIndex="false";
                    }
                },

                error: function (reData) {
                    stopUploadImg();
                    if(recordPageIndex!='false'){
                        layer.close(recordPageIndex);
                        recordPageIndex="false";
                    }
                    alert("网络连接失败，请稍后重试");
                }
            });

            stopUploadImg();

        },
        fail: function (res) {
            stopUploadImg();s
            alert("上传图片失败 "+res.errMsg);
        }
    });


};


function startuploadImg(paramImg) {

    if(isUploadingImg=="true"){
        alert("正在上传图片请稍后。");
        return;
    }

    var paramTmp;
    if(Z_IsEmpty(paramImg)){
        paramTmp=getVoiceImgParam();
        if(paramTmp.bId=='false'){
            return;
        }
    }else{
        paramTmp=paramImg;
    }
    wx.chooseImage({
        success: function (res) {
            imgLocalIds = res.localIds;
//            alert(localIds[0]);
            if(typeof(imgLocalIds)!="undefined"){
                if(Z_IsNotEmtArray(imgLocalIds)){
                    window.setTimeout(function() {
                        try{
                            uploadImgwx(paramTmp);
                        }catch (e){
                            alert("上传失败，请稍后重试.");
                            stopUploadImg();
                        }

                    }, 1000);
                }
            }
        },
        fail: function (res) {
            stopUploadImg();
            alert("choose Img fail "+res.errMsg);
        }
    });
};



function startuploadImgBak(paramImg) {

    var paramTmp;
    if(Z_IsEmpty(paramImg)){
        paramTmp=getVoiceImgParam();
        if(paramTmp.bId=='false'){
            return;
        }
    }else{
        paramTmp=paramImg;
    }
    wx.chooseImage({
        success: function (res) {
            var localIds = res.localIds;
//            alert(localIds[0]);
            if(typeof(localIds)!="undefined"){
                if(Z_IsNotEmtArray(localIds)){
                    wx.uploadImage({
                        localId: localIds[0],
                        isShowProgressTips: 1, // 默认为1，显示进度提示
                        success: function (res) {
                            alert("sucess "+res.errMsg);
                            var serverId = res.serverId;
                            serverId=Z_EU(serverId);
//                            alert("uploadImage "+serverId);

//                            上传速度很快，不加状态防护。需要时按声音逻辑调整。
                            $.ajax({
                                async: true,
                                url: '/WXINFO/WXSDK/weixin?ajaxAction=WX_AJAX_UPLOAD_MEDIA',
                                type: "POST",
                                dataType: "json",
                                data: { "wid":wid,"serverId": serverId, "type":'Z_GROUP_IMG_MSG' ,"bId":paramTmp["bId"],"bType":paramTmp["bType"]},
                                success: function (data) {
//                                    不加上传成功提示，会在页面中看到
                                    if(!Z_IsEmpty(data) && !Z_IsEmpty(data.retcode) && data.retcode==0 ){


                                    }else{
                                        alert("上传失败，请稍后重试");
                                    }

                                    if(recordPageIndex!='false'){
                                        layer.close(recordPageIndex);
                                        recordPageIndex="false";
                                    }
                                },

                                error: function (reData) {
                                    if(recordPageIndex!='false'){
                                        layer.close(recordPageIndex);
                                        recordPageIndex="false";
                                    }
                                    alert("网络连接失败，请稍后重试");

                                }
                            });
                        },
                        fail: function (res) {
                            alert("uploadImage fail "+res.res.errMsg);
                        }
                    });
                }
            }
        }
    });
};



function startuploadVoice() {
    var paramTmp=getVoiceImgParam();
    if(paramTmp.bId=='false'){
//        alert("startuploadVoice return "+paramTmp.bId);
        return;
    }

    if(recordTime==0){
        $(".startRecord").html('停止录音');
        recordTime=(new Date()).getTime();
        wx.startRecord();
    }else{
        if(isUpdateRecord=='false'){
            isUpdateRecord='true';
            wx.stopRecord({
                success: function (res) {
                    var localId = res.localId;
                    wx.uploadVoice({
                        localId: localId,
                        isShowProgressTips: 1, // 默认为1，显示进度提示
                        success: function (res) {
                            var serverId = res.serverId;
                            serverId=Z_EU(serverId);

                            $.ajax({
                                async: true,
                                url: '/WXINFO/WXSDK/weixin?ajaxAction=WX_AJAX_UPLOAD_MEDIA',
                                type: "POST",
                                dataType: "json",
                                data: { "wid":wid,"serverId": serverId, "type":'Z_GROUP_VOICE_MSG' ,"bId":paramTmp["bId"],"bType":paramTmp["bType"]},
                                success: function (data) {
//                                    不加上传成功提示，会在页面中看到
                                    if(!Z_IsEmpty(data) && !Z_IsEmpty(data.retcode) && data.retcode==0 ){

                                    }else{
                                        alert("上传失败，请稍后重试");
                                    }

                                    stopuploadVoice();
                                    if(recordPageIndex!='false'){
                                        layer.close(recordPageIndex);
                                        recordPageIndex="false";
                                    }
                                },

                                error: function (reData) {
                                    stopuploadVoice();
                                    if(recordPageIndex!='false'){
                                        layer.close(recordPageIndex);
                                        recordPageIndex="false";
                                    }
                                    alert("网络连接失败，请稍后重试");
                                }
                            });


                        },
                        fail: function (res) {
//                            alert("uploadVoice fail");
                            alert("录音失败请稍后重试");
                            stopuploadVoice();
                        }
                    });
                },
                fail: function (res) {
//                            alert(res.errMsg);
                    stopuploadVoice();
                    if((new Date()).getTime()-recordTime>=60*1000){
                        alert("录音失败:声音长度超过1分钟");
                    }
                }
            });
        }else if((new Date()).getTime()-recordTime>=60*1000){
            stopuploadVoice();
            alert("录音失败:声音长度超过1分钟");
        }else{
            alert("正在上传录音请稍后。");
        }
    }
};

var recordPageIndex="false";

function startuploadImgVoice() {
//    isRecordPagePop="true";
    recordPageIndex = layer.open({
        type: 1, //1代表页面层
        content: '<div class="recordBtn startRecord">开始录音</div><div class="recordBtn uploadImg">上传图片</div><div class="recordBtn recordBtnClose">返回</div>',
        style: 'width:300px; height:200px; border:none;',
        shadeClose: false,
        success: function(olayer){
            var cla = 'getElementsByClassName';
            olayer[cla]('recordBtnClose')[0].onclick = function(){
                layer.close(recordPageIndex);
                recordPageIndex="false";
            }


            $('.startRecord').click(function () {
//                alert("startRecord click");
                if(isJsApiOk == "true"){
                    startuploadVoice();
                }

            }) ;

            $('.uploadImg').click(function () {
//                alert("uploadImg click");
                if(isJsApiOk == "true") {
                    startuploadImg();
                }
            }) ;


//            $(".startRecord").live("click", function () {
//                alert("startRecord click");
//                if(isJsApiOk == "true"){
//                    startuploadVoice();
//                }
//            });
//
//
//            $(".uploadImg").live("click", function () {
//                alert("uploadImg click");
//                if(isJsApiOk == "true") {
//                    startuploadImg();
//                }
//            });

        }
    });
};




