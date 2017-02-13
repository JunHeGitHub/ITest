
var Log4js;
/*debug info error none */
var LOG_LEVEL="debug";
/*chrome ie */
var Log4jsType="chrome";
if (Log4jsType=="chrome") {
    Log4js = {
        logger: null,
        debug : function() { if  (LOG_LEVEL === 'debug'){ Log4js._log.apply(Log4js._log, arguments); }},
        info  : function() { if ((LOG_LEVEL === 'info')  || (LOG_LEVEL === 'debug'))  { Log4js._log.apply(Log4js._log, arguments); }},
        error : function() {Log4js._log.apply(Log4js._log, arguments); },
        _log  : function() {
            if(LOG_LEVEL=='none'){
                return;
            }

            if (!Log4js.logger) {
                var console = window.console;
                //IE8 下会报错
                if (console && typeof console.log=="functon") {
                    if (console.log.apply) {
                        Log4js.logger = console.log;
                    } else if ((typeof console.log === "object") && Function.prototype.bind) {
                        Log4js.logger = Function.prototype.bind.call(console.log, console);
                    } else if ((typeof console.log === "object") && Function.prototype.call) {
                        Log4js.logger = function() {
                            Function.prototype.call.call(console.log, console, Array.prototype.slice.call(arguments));
                        };
                    }
                }
            }

            if (Log4js.logger) {
                Log4js.logger.apply(window.console, arguments);
            }

//            var logElement = document.getElementById(PushStream.LOG_OUTPUT_ELEMENT_ID);
//            if (logElement) {
//                var str = '';
//                for (var i = 0; i < arguments.length; i++) {
//                    str += arguments[i] + " ";
//                }
//                logElement.innerHTML += str + '\n';
//
//                var lines = logElement.innerHTML.split('\n');
//                if (lines.length > 100) {
//                    logElement.innerHTML = lines.slice(-100).join('\n');
//                }
//            }
        }
    };
}else{
    Log4js = {
        debug: function () {
            if (LOG_LEVEL === 'debug') {
                var args = Array.prototype.slice.call(arguments);
                log.debug(arr2Str(args));
            }
        },
        info: function () {
            if ((LOG_LEVEL === 'info') || (LOG_LEVEL === 'debug')) {
                var args = Array.prototype.slice.call(arguments);
                log.info(arr2Str(args));
//                Log4js._log.apply(Log4js._log, arguments);
            }
        },
        error: function () {
            if(LOG_LEVEL!='none'){
                var args = Array.prototype.slice.call(arguments);
                log.error(arr2Str(args));
            }
        }

    };
}

function arr2Str(arr) {
    var ret="";
    for(var i=0;i<arr.length;i++){
        ret +=" "+arr[i];
    }
    return ret;
}

var log = new function() {

    this.m_oLoger = null;
    this.useServerLog="false";

    this.info = function(str) {
        this.log(4,str);
    }

    this.debug = function(str) {
        if(typeof(passedArgs)!='undefined' &&  typeof(passedArgs["p1"])!='undefined' ){
            str=' ['+passedArgs["p1"]+'] '+str
        }
        this.log(3, str);
    };

    this.warn = function(str) {
        this.log(2, str);
    };

    this.error = function(str) {
        this.log(1, str);
    };

    this.log = function(lvl, str) {
        if(Log4jsType=='ie' && typeof(this.m_oLoger)!= "undefined"  && typeof(this.m_oLoger.Write)!= "undefined" ){

            if( this.m_oLoger == null )
                return;

            this.m_oLoger.Write(str);
        }else if(Log4jsType=='chrome'){
            Log4js.debug(str);
        }
    };

    if( Log4jsType=='ie'){
        try {
            var sHtml = '<OBJECT id="Loger4IE1" classid="clsid:7255E604-91AD-4D2F-95B4-8F28478100C8"' +
                'codebase="/ZinglabsD/Log4IE.dll#version=1,0,0,1" style="display:none"></OBJECT>';
            document.write(sHtml);
        }catch( e ) {
            // alert(e.name + " : " + e.message);
        }

        this.m_oLoger = document.getElementById("Loger4IE1");
        if( this.m_oLoger != null ) {
            var dt = new Date();
            try {
                this.m_oLoger.SetLogFileName("D:\\zinglabs\\ZDesk" + dt.getYear()+''+(dt.getMonth()+1)+''+dt.getDate() + ".log");
            }catch(e) {
                //alert(e.name + " : " + e.message);
            }
        }
    }


    /**
     * 日志内容要提供 座席/外线标识
     * 必须控制此日志范围 尽量少使用此日志
     */
    this.serverLog = function(str) {

        if(this.useServerLog=="false"){return;}

        if(typeof(str)=='undefined' || str==null || str.length==0)return;
        if(typeof(passedArgs)!='undefined' &&  typeof(passedArgs["p1"])!='undefined' ){
            str=' ['+passedArgs["p1"]+'] '+str
        }

        $.ajax({
            async : false,
            type : "post",
            url : AT_LINK,
            dataType : "json",
            data : {
                'content' : str,
                'action' : 'zimServerLog'
            },
            success : function(jsons) {
                log.debug("save server log sucess");
            },
            error : function(r) {
                log.debug('save server log error' + r);
            }
        });
    };

};
