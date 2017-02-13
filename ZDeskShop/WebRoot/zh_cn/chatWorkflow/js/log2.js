var isServLog=true;
function serverLogMH(str) {
    if(!isServLog){return;}
    if(typeof(str)=='undefined' || str==null || str.length==0)return;
    $.ajax({
        async : true,
        type : "post",
        url : "/NCard/NCardService?ac=JSLOG",
        data : {
            'content' : str,
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
