var TemSonData = []; //模板子级数据
/****调用模板****/

var TemParentData = getDictListfoIndexData("MessageTemType");  //模板分类数据

						
//查询模板所有数据--(为了减少与服务器的交互)
function doSearchAllTem() {
    var rdata = [];
    var pat = {};
    pat.tableName = "TemplateMem";
 	pat.columnValues ={
  	 "isParent":"1"
  	 };
    commonSelect(pat, true,
    function(data) {
        if (data && data.data) {
           TemSonData= data.data;
            
        } else {
            TemDataIsNull = true;
        }
    });
}
//绑定父级下拉 
function NeedParentVal(SelId){
    $("#" + SelId).empty();
    $("#" + SelId).append("<option value='------'>请选择</option>");
    for (var i in TemParentData) {
        $("#" + SelId).append("<option value='" + TemParentData[i].code + "'>" + TemParentData[i].name + "</option>");
    }
}

//绑定模板下拉
function NeedVal(SelId, data) {
    $("#" + SelId).empty();
    $("#" + SelId).append("<option value='------'>请选择</option>");
    for (var i in data) {
        $("#" + SelId).append("<option value='" + data[i].code + "'>" + data[i].text + "</option>");
    }
}

//绑定对应子级下拉
function NeedSonValByCode(SelId, SonSelId) {
    var SelParentCode = $("#" + SelId).find("option:selected").val(); //ie8情况下直接.val().获取当前选中为数组形式
    var Sondata = [];
    for (var i in TemSonData) {
        if (TemSonData[i].parentCode == SelParentCode) {
            Sondata.push(TemSonData[i]);
        }
    }
    NeedVal(SonSelId, Sondata);
    CheckOtherTem(SelParentCode); //回调对应引用页面的差异操作方法
}