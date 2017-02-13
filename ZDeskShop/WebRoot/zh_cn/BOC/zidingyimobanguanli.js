//遮罩属性		
var maskContent = {
    css: {
        border: 'none',
        padding: '5px',
        backgroundColor: '#000',
        '-webkit-border-radius': '5px',
        '-moz-border-radius': '5px',
        opacity: .5,
        color: '#fff',
        font: '14px'
    },
    message: '<img src="../../img/wait.gif" border="0" />  请稍候...'
};

var nowSelData = ""; //当前选中数据
var rdata = []; //加载数据(模板)
var Pardata = []; //加载数据(模板分类)
var CheckBest=false; //最终验证结果
var CT=false;  //验证是否为空
/*****************************************************/
//相关验证

//模板文本内容验证
function checkText() {
    var checkRes = false;
    var TextVal =$("#Temtext").val();
    if (TextVal== "" ||TextVal== null || TextVal== undefined) {
    	hideTooltip("Temtext_DIV");
        showTooltip("Temtext_DIV","请输入模板文本");
        checkRes = false;
    } else {
        hideTooltip("Temtext_DIV");
        checkRes = true;
    }
    return checkRes;
}


//下拉验证
function checksSel() {
    var checkResult = false;
    $("select").each(function() {
        if ($(this).val() == "------" || $(this).val() == "") {
            checkResult = false;
            showTooltip($(this).attr("id"), "\u8bf7\u9009\u62e9\u5bf9\u5e94\u5185\u5bb9");
        } else {
            checkResult = true;
            hideTooltip($(this).attr("id"));
        }
    });
    return checkResult;
}

//判断是提交还是修改,同时验证名称是否存在
function SubmitAddOrUpd_CheckIsHave(AddOrUpd){
CheckBest=false;
 $.blockUI(maskContent);
CT=checkText(); //验证是否为空
CS=checksSel(); //下拉验证
CM=checkTextMax(); //验证输入最大值
	if(CT&&CS&&CM){
		CheckBest=true;
	}
	CheckIsHave(AddOrUpd); //验证是否存在
}
//判断数据是否存在(之后调用对应提交方法)
function CheckIsHave(AddOrUpd) {
    //修改状态时判断是否名称异同
    var Val=$("#Temtext").val();
    if (nowSelData != "") {
        if (nowSelData.text == Val) {
        	if(CheckBest){
        		SubmitDoOr(AddOrUpd);
        	}else{
        		$.unblockUI();
        	}
            return;
        }
    }
    //没有输入不验证
    if(!CT){
   		$.unblockUI();
    	return;
    }
    var rdata = [];
    var pat = {};
    pat.tableName = "TemplateMem";
    pat.equal = "isParent,text";
    pat.columnValues = {
            "text": Val,
            "isParent": "1"
        };
    commonSelect(pat, true,
    function(data) {
        if (data && data.data) {
            rdata = data.data;
        } else {
            zinglabs.alert(data.mgs);
        }
	    if (rdata.length != 0) {
	    	hideTooltip("Temtext_DIV");
	    	showTooltip("Temtext_DIV","名称已存在,请重新输入");
	    } else {
	    	if(CheckBest){
	    	hideTooltip("Temtext_DIV");
	    	SubmitDoOr(AddOrUpd);
	    	}else{
	    	}
	    }
	$.unblockUI();
   });
}
//提交对应方法
function SubmitDoOr(AddOrUpd){
if(AddOrUpd=="Add"){
AddTem();
}else if(AddOrUpd=="Upd"){
updateTem();
	}
}

//填写内容最大值
function checkTextMax() {
    var BestRes = true;
    var HFInter = "";
    if ($("#HFYX").is(":hidden")) {
        HFInter = $("#TemplateContent").val();
    } else {
        HFInter = editor_a.getContent();
    }
    if (HFInter.length >= 3500) {
        BestRes = false;
        showTooltip($("#MBNR").attr("id"), "内容超出限制，请删减调整!");
    } else {
        hideTooltip($("#MBNR").attr("id"));
    }
     if($("#Temtext").val().length <=32){
      	hideTooltip($("#Temtext").attr("id"));
    } else {
    	BestRes = false;
        showTooltip($("#Temtext").attr("id"), "内容超出限制，请删减调整!");
    }
    return BestRes;
}

//表单验证 提示样式
function showTooltip(id, title) {
    $("#" + id).attr("title", title);
    $("#" + id).tooltip({
        placement: "top"
    });
    $("#" + id).tooltip("show");
    $("#" + id).addClass("error");
}

//还原样式
function hideTooltip(id) {
    $("#" + id).tooltip("destroy");
    $("#" + id).removeClass("error");
}
/*****************************************************/

//分页查询
function FYdoSearch(data, callback, settings) {
    $.blockUI(maskContent);
    Pardata = getDictListfoIndexData("MessageTemType");  //模板分类数据
    var pat = {};
    var paramt = {};
    pat.tableName = "TemplateMem";
    pat.equal = "isParent";
    //分页参数
    pat.rows = settings._iDisplayLength; //获取数据长度
    pat.offset = settings._iDisplayStart; //开始获取数据位置
    pat.columnValues = {
        "isParent": "1"
    }; //是否为模板类型
    commonSelect(pat, true,
    function(Nowdata) {
        if (Nowdata && Nowdata.data) {
            rdata = Nowdata.data;
            var pdata = {};
            //数据
            pdata.data = Nowdata.data;
            var total = Nowdata.total;
            //总条数
            pdata.recordsTotal = total;
            //过滤没有使用到总条数
            pdata.recordsFiltered = total;
            // 代表第几次画页面 如pdata.draw 不能删如删掉不会翻页 
            pdata.draw = settings.iDraw;
            callback(pdata);
        } else {
            zinglabs.alert(data.mgs);
        }
         $.unblockUI();
    });
    SelNeedVal(); //绑定下拉
}

/*****************************************************/

//新增模板
function AddTem() {
   	 $.blockUI(maskContent);
		 var CodeSe="";  //随机5位数，用于设置code
		for(var i=0;i<5;i++){
		CodeSe+=Math.floor(Math.random()*10);
		}
        var param ={};
        param.text=$("#Temtext").val();
        param.parentText = $("#parentCode").find("option:selected").text();
        param.parentCode = $("#parentCode").find("option:selected").val();
        param.code=CodeSe;
        if ($("#HFDX").is(":hidden")) {
            param.TemplateContent = editor_a.getContent();
        }else{
        	 param.TemplateContent = $("#TemplateContent").val();
        }
        var pat = {};
        pat.tableName = "TemplateMem";
        pat.primarykeyType = "uuid";
        pat.columnValues = param;
        commonInsertOrUpdate(pat, true,
        function(data) {
            if (data.success) {
                clearTem();
                zinglabs.alert("保存成功");
                doRepeatBySon();
                clearTem();
            } else {
                zinglabs.alert("保存失败");
            }
        $.unblockUI();   
        });
        $("#sandInter").slideDown();
}
/*****************************************************/

//更新模板
function updateTem() {
    if (nowSelData != "") {
          $.blockUI(maskContent);
            var param ={};
            param.text=$("#Temtext").val();
            param.parentText = $("#parentCode").find("option:selected").text();
            param.parentCode = $("#parentCode").find("option:selected").val();
            if ($("#HFDX").is(":hidden")) {
                param.TemplateContent = editor_a.getContent();
            }else{
        	 param.TemplateContent = $("#TemplateContent").val();
        	}
            param.id = nowSelData.id;
            var pat = {};
            pat.tableName = "TemplateMem";
            pat.columnValues = param;
            commonUpdate(pat, true,
            function(data) {
                if (data.success) {
                    zinglabs.alert("修改成功");
                    doRepeatBySon();
                    clearTem();
                } else {
                    zinglabs.alert("修改失败");
                }
             $.unblockUI(); 
           	 });
    } else {
        zinglabs.alert("请选择要修改的数据");
    }
}
/*****************************************************/

//删除模板
function deleteTem() {
    if (nowSelData != "") {
     	$.blockUI(maskContent);	
        var id = nowSelData.id;
        var patd = {};
        patd.tableName = "TemplateMem";
        patd.columnValues = id;
        commonDelete(patd, true,
        function(data) {
            if (data.success) {
                zinglabs.alert("删除成功");
                doRepeatBySon();
                clearTem();
            } else {
                zinglabs.alert("删除失败");
            }
         $.unblockUI(); 
        });
    } else {
        zinglabs.alert("请选择要删除的数据");
    }
}
/*****************************************************/

//新建模板按钮相应显藏
function XJMBBtn() {
    clearTem();
    $("#sandInter").slideDown();
    $("#btn_xinjianTJ").show();
    $("#btn_xiugaiMB").hide();
}
/*****************************************************/

//动态移动滚动条位置(动画效果)
function scroll2data(id) {
    $("html,body").animate({scrollTop: $("#" + id).offset().top},700);
}

//模板详情
function OpenSel(data) {
    nowSelData = data;
    for (var key in data) {
        if (data[key] == undefined || data[key] == null) {
            data[key] = "";
        }
        if(key=="text"){
        $("#Temtext").val(data.text);
        }
        $("#" + key).val(data[key]);
    }
    $("#sandInter").slideDown();
    $("#btn_xinjianTJ").hide();
    $("#btn_xiugaiMB").show();
    scroll2data("sandInter");
    $("#parentCode").change();

    //对应显示回复内容
    if ($("#HFDX").is(":hidden")) {
        editor_a.setContent(data.TemplateContent);
        $("#TemplateContent").val("");
    } else {
        editor_a.setContent("");
        $("#TemplateContent").val(data.TemplateContent);
    };
}
/*****************************************************/
/**刷新**/
function doRepeatBySon() {
    nowSelData = "";
    var table = $("#test").DataTable();
    table.draw();
    $("#sandInter").hide();
}
/*****************************************************/

//	清空数据
function clearTem() {
    $("#TemForm input,textarea").each(function() {
        $(this).val("");
    });
    nowSelData="";
    editor_a.setContent("");
    $("#parentCode").find("option:first").attr("selected","selected");
}

//下拉绑定值
function SelNeedVal() {
    var data = Pardata;
    $("#parentCode").empty();
    $("#parentCode").append("<option value='------'>请选择</option>");
    for (var i in data) {
        $("#parentCode").append("<option value='" + data[i].code + "'>" + data[i].name + "</option>");
    }
}

//不同模板分类显示不同的输入方式
function CheckShowOther(nowSel) {
    if ($(nowSel).val() == "MessageTemTypePhone") {
        $("#HFDX").show();
        $("#HFYX").hide();
    } else {
        $("#HFYX").show();
        $("#HFDX").hide();
    }
}