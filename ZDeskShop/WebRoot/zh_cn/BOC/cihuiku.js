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

var A_PROCESSDEFINITIONID = ""; //流程标识
var activitiParameterAndData; //流程配置和获取来的流程数据
var A_PROCESSINSTANCEID = ""; //流程实例ID
var A_TASKID = ""; //节点ID
var A_TASKNAME = ""; //节点名称(-获取待办)
var A_FORMCLASS = ""; //表单类
var JBSTate = ""; //经办审批状态
var FHSTate = ""; //复核审批状态
var winTopData = {}; //当前数据对象
var NowLoginName = ""; //当前登录用户
var UpDataObj = ""; //上传文件相关信息对象
var fhspOpinion; //复核审批状态
var reviewAdviceOpinion; //复核审批处理意见
var CheckAll = true; //表单验证
var CheckUploadExcel = false; //上传文件验证
var UserCheck=true;  //用户权限验证
var WinParent=window.parent;


/**刷新**/
function NowdoRepeat() {
   WinParent.doRepeat();   //父类刷新方法
    $("#allInterDiv").hide(); //隐藏表单
     WinParent.hideHight(); //隐藏iframe
    winTopData = ""; //清空当前数据对象
}

/*************************************************************************/

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

/*************************************************************************/

//显示待办相关信息
function OpenInter() {
    //根据留言ID查询留言对应相关内容
    for (var key in winTopData) {
        if (winTopData[key] == undefined || winTopData[key] == null) {
            winTopData[key] = "";
        }
        $("#" + key).val(winTopData[key]);
    }
    $("#reviewAdvice_Input").val(winTopData.reviewAdvice);
    CheckUploadExcel = false; //恢复上传验证原始状态
    $("#allInterDiv").slideDown();
    OtherSomeDiff(); //不同节点差异项
    fhSelOption(); //绑定复核意见下拉
}
/*************************************************************************/

//数据显示部分差异判别
function OtherSomeDiff() {
 	CheckUploadExcel = true; //上传文件验证默认为有效
    $("#worksTitle").css("color", "black");
    $("#Sub_btnDIV").show();
    $("#Add_btnDIV").hide();
    if (winTopData.nodeName == "词汇库经办") {
        $("#btn_down").show();
        $("#btn_upload").show();
        $("#checkUserInp").show();
        $("#checkUserBtn").show();
        $("#approveStateDIV").hide();
        $("#reviewAdvice_Select").hide();
        $("#reviewAdvice_Input").show();
        $("#showDIV").show();
        $("#btn_down").css({"margin-left":"550px","margin-top":" -145px"});
        $("#handAdvice").removeAttr("readOnly");
    } else if (winTopData.nodeName == "词汇库复核") {
   		$("#showDIV").hide();
   		$("#btn_down").css({"margin-left":"170px","margin-top":" -90px"});
		$("#checkUserInp").hide();
        $("#checkUserBtn").hide();
        $("#approveStateDIV").show();
        $("#btn_down").show();
        $("#reviewAdvice_Input").hide();
        $("#reviewAdvice_Select").show();
        $("#handAdvice").attr("readOnly", "readOnly");
    }
}

/*************************************************************************/

//绑定复核相关下拉(审批意见、审批状态)
function fhSelOption() {
    $("#reviewAdvice_Select").empty();
    $("#approveState").empty();
    $("#reviewAdvice_Select").append("<option value='------'>请选择</option>");
    $("#approveState").append("<option value='------'>请选择</option>");
    for (var i in fhspOpinion) {
        $("#approveState").append("<option value='" + fhspOpinion[i].code + "'>" + fhspOpinion[i].name + "</option>");
    }
    for (var i in reviewAdviceOpinion) {
        $("#reviewAdvice_Select").append("<option value='" + reviewAdviceOpinion[i].code + "'>" + reviewAdviceOpinion[i].name + "</option>");
    }
}
/*************************************************************************/

//新建词汇库审批按钮
function AddNewWordsCheck() {
   	fhSelOption(); //绑定复核意见
    winTopData == ""; //清空当前对象
    $("#showDIV").show();
    $("#btn_down").hide();
    $("#allInterDiv").slideDown();
    $("#Sub_btnDIV").hide();
    $("#Add_btnDIV").show();
    $("#btn_down").hide();
    $("#handAdvice").removeAttr("readOnly");
    $("#checkUserInp").show();
    $("#checkUserBtn").show();
    $("#approveStateDIV").hide();
    $("#appointedCheckUser_DIV").show();
    $("#approveState_DIV").hide();
    $("#reviewAdvice_Input").show();
    $("#reviewAdvice_Select").hide();

    $("#allInterDiv").find("input[type='text'],textarea").each(function() {
        $(this).val("");
    });
    alertInter("blur"); //加载词汇标题提示语句
}
/*************************************************************************/

//是否确定新建
function AddCheckIsOK(Req) {
    if (Req == "ok") {
        if (CheckAllFn('Sub')) {
            var pat = {};
            pat['fileName'] = UpDataObj.fileName; //原上传文件名
            pat['savePath'] = UpDataObj.savePath; //上传存放绝对路径
            pat['worksTitle'] = $("#worksTitle").val();
            pat['handAdvice'] = $("#handAdvice").val();
            pat['note'] = $("#note").val();
            pat['A_PROCESSDEFINITIONID'] = A_PROCESSDEFINITIONID;
            pat['A_FORMCLASS'] = "WorksBaseInsert";
            pat["disponseUser"] = NowLoginName;
            pat["A_ASSIGNEE"] = NowLoginName;
            pat["createUser"] = NowLoginName;
            pat["nodeName"] = "词汇库经办";
            pat["flowNode"] = "WorksBaseJB";
            pat["flowType"] = A_PROCESSDEFINITIONID;
            pat["approvalState"] = "新建";
            putLoginNameToParam(pat);
            doStartProcess(pat,
            function(data) {
                if (!data.success) {
                    zinglabs.i18n.alert("cihuikuguanli_shenpichuangjianshibai");
                } else {
                    NowdoRepeat();
                }
            });
        }
    } else if (Req == "no") {
        $("#allInterDiv").hide();
        WinParent.hideHight(); //隐藏iframe
    }
}

/*************************************************************************/

//表单验证
function CheckAllFn(State) {
    if (!CheckUploadExcel) {
        CheckAll = false;
        hideTooltip("fileName");
        showTooltip("fileName", "请上传有效文件");
    } else {
        CheckAll = checkTextMax(); //验证填写最大值
        hideTooltip("fileName");
    }
    if (State == 'Sub') {
        //复核审批意见下拉选择是否选择(可选情况下)
        if (!$("#approveState").is(":hidden")) {
            if ($("#approveState").val() == "------") {
                CheckAll = false;
                showTooltip("approveState", "请选择对应内容");
            } else {
                hideTooltip("approveState");
            }
            if ($("#approveState").find("option:selected").text() != "通过") {
               
                    if ($("#reviewAdvice_Select").val() == "------") {
                        CheckAll = false;
                        showTooltip("reviewAdvice_Select", "请选择对应内容");
                    } else {
                        hideTooltip("reviewAdvice_Select");
                }
            } else {
                hideTooltip("reviewAdvice");
            }
        }
    }
    //词汇标题是否为空
    if ($("#worksTitle").val() == "" || $("#worksTitle").val() == "默认为,有效上传文件名") {
        CheckAll = false;
        showTooltip("worksTitle", "词汇标题不可为空");
    } else {
        hideTooltip("worksTitle");
    }
    return CheckAll;
}

/*************************************************************************/

//填写内容最大值
function checkTextMax() {
    var mgsTooLog = "内容超出限制，请删减调整!";
    var BestRes = true;
    if ($("#note").val().length >= 70) {
        BestRes = false;
        showTooltip("note", mgsTooLog);
    } else {
        hideTooltip("note");
    }
    if ($("#handAdvice").val().length >= 70) {
        showTooltip("handAdvice", mgsTooLog);
        BestRes = false;
    } else {
        hideTooltip("handAdvice");
    }
    if ($("#worksTitle").val().length >= 30) {
        showTooltip("worksTitle_DIV", mgsTooLog);
        BestRes = false;
    } else {
        hideTooltip("worksTitle_DIV");
    }
    return BestRes;
}

/*************************************************************************/

//保存
function saveAndAudit(WhySave, WhyError) {
    if (CheckAllFn("Save")) {
		$.blockUI(maskContent);
        var pat = {};
        pat.appointedCheckUserID = $("#appointedCheckUserID").val();
        pat.appointedCheckUser = $("#appointedCheckUser").val();
        pat.handAdvice = $("#handAdvice").val(); //经办意见
        pat.reviewAdvice = $("#reviewAdvice_Select").find("option:selected").text(); //复核意见
        //未选择进行清除无效属性
        if(pat.reviewAdvice=="------"){
       		delete pat.reviewAdvice
        }
        pat.note = $("#note").val(); //备注信息
        if (UpDataObj != "") {
            pat['fileName'] = winTopData.fileName; //原上传文件名
            pat['savePath'] = winTopData.savePath; //上传存放绝对路径
        }
        pat['id'] = winTopData.id;
        var patUp = {};
        patUp.tableName = "WorksBaseMem";
        patUp.columnValues = pat;
        commonUpdate(patUp, true,
        function(data) {
        	sleep(1);
            if (data.success) {
                NowdoRepeat();
                $("#allInterDiv").hide();
            } else {
                if (WhySave == "OnlySave") {
                    zinglabs.i18n.alert("system_saveFailed");
                }
            }
            if (WhySave == "CanNotSubSave") {
                CanNotSubSaveResult(data.success, WhyError);
            }
		 $.unblockUI();
        });
    }
}

//提交失败保存结果提示语方法(异步请求策略)
function CanNotSubSaveResult(Result, WhyError) {
    var tishiyu = "但已成功保存";
    if (!Result) {
        tishiyu = "保存失败!";
    }
    zinglabs.i18n.alert("liuyanguanli_tijiaoshibaijinxingbaocun", {
        "${0}": WhyError,
        "${1}": tishiyu
    });
}
/*************************************************************************/

//获取待办弹窗
function addOrgByHQDB() {
    var url = "/ZDesk/zh_cn/BOC/huoqudaiban.html?PROCESSDEFINTION_ID=" + A_PROCESSDEFINITIONID + "&doRepeat=NowdoRepeat" + "&A_TASKNAME=" + A_TASKNAME + "&A_FORMCLASS=" + A_FORMCLASS;
    var nowhtmlurl = window.location.pathname;
    nowhtmlurl += window.location.search;
    var now = window.parent.$("iframe[src='" + nowhtmlurl + "']");
    now.attr("name", "needforme");
    window.top.huoqudaibanDialog(url, "获取待办", "1000", "360");
}

//留言审核提交
function submitAndAudit() {
    if (CheckAllFn("Sub")) {
		$.blockUI(maskContent);
        var pat = winTopData; //当前选中数据对象赋予pat
        var approvalState = $("#approveState").find("option:selected").text();
        pat["approvalState"] = approvalState;
        pat.note = $("#note").val(); //备注信息
        if (pat.nodeName == "词汇库经办") {
            if (UpDataObj != "") {
                pat['fileName'] = UpDataObj.fileName; //原上传文件名
                pat['savePath'] = UpDataObj.savePath; //上传存放绝对路径
            }
            pat["approvalState"] = "待审批";
            pat["flow_type"] = "2";
            pat["nodeName"] = "词汇库复核";
            pat["flowNode"] = "WorksBaseFH";
            var appointedCheckUserID = $("#appointedCheckUserID").val();
            if (appointedCheckUserID != undefined && appointedCheckUserID != "" && appointedCheckUserID != null) {
                pat.appointedCheckUserID = appointedCheckUserID;
                pat.appointedCheckUser = $("#appointedCheckUser").val();
                pat.disponseUser = appointedCheckUserID;
                pat["A_ASSIGNEE"] = appointedCheckUserID;
            } else {
                //没有指定处理人提交参数设置
                pat.disponseUser = "";
                pat["A_ASSIGNEE"] = "";
            }
            pat.handAdvice = $("#handAdvice").val(); //经办意见
        } else if (pat.nodeName == "词汇库复核") {
            if (approvalState == "退回") {
                pat.reviewAdvice = $("#reviewAdvice_Select").find("option:selected").text(); //复核意见
                pat.disponseUser = pat.createUser;
                pat.appointedCheckUserID = "";
                pat.appointedCheckUser = "";
                pat["A_ASSIGNEE"] = pat.createUser;
                pat["nodeName"] = "词汇库经办";
                pat["flowNode"] = "WorksBaseJB";
                pat["flow_type"] = "1";

            } else if (approvalState == "通过") {
                pat["flow_type"] = "-2";
                pat["nodeName"] = "完结";
                pat["flowNode"] = "Over";
                publishInsData(pat); //发布信息
                return;
            }
        }
		
        ActiSubmit(pat);
    }
}
/*************************************************************************/

//流程提交方法
function ActiSubmit(pat) {
    pat.A_FORMCLASS = "WorksBaseUpdate"; //表单处理类
    if (pat.a_id != "" && pat.a_id != null && pat.a_id != undefined) {
		$.blockUI(maskContent);
        pat.A_TASKID = pat.a_id; //流程唯一ID
        pat.A_PROCESSINSTANCEID = pat.flowId; //流程实例标识
        pat.A_PROCESSDEFINITIONID = pat.flowType; //流程标识
        doCompletTask(pat,
        function(data) {
            if (data.success) {
                NowdoRepeat();
            } else {
                saveAndAudit("CanNotSubSave", "流程失败");
            }
			$.unblockUI();
        });
    } else {
        zinglabs.i18n.alert("liuyanguanli_renwubianhaohuoqushibai");
    }
}

/*************************************************************************/

//数据发布,读取Excel数据并存入发布零时表
function publishInsData(ActPat) {
	$.blockUI(maskContent);
    var param = {};
    var pat = {};
    param["TableCells"] = "professionKey,revisionNote";
    param['FilePath'] = winTopData.savePath;
    param["batchMark"] = winTopData.flowId;
    pat.tableName = "WorksPublishMem";
    pat.primarykeyType = "uuid";
    var url = "/ZDesk/*/cihuikuProcess/readXlsAndInsert.action";
    ajaxFunction(url, param, true,
    function(data) {
        if (data.data) {
            pat.columnValues = data.data;
            commonInsertOrUpdate(pat, true,
            function(data) {
                if (data.success) {
                    zinglabs.alert("发布成功");
                    ActiSubmit(ActPat);
                } else {
                    saveAndAudit("CanNotSubSave", "失败,因发布数据存放失败");
                }
				$.unblockUI();
            });
        }
    });
}

/*************************************************************************/

//上传文件方法调用
function NowUploadFile() {
    var params = {};
    params.maxSize = 10; //10M,excel文件大小
    params.reFillFn = 'ResultData'; //回调函数
    //params.isShowDialog = true; //弹出上传窗体
    //params.limitCount = 1; //可同时上传文件大小
    params.isSimplePage=true;  //是否为简单上传形式
    params.isMultiple = false; //是否可以同时选中多个文件
  	params.frameWidth ="400px";
   	params.frameHeight="50px";
    useWebUploader("showDIV", params);
}
/*************************************************************************/

//上传文件后回调函数
function ResultData(data) {
    UpDataObj = data[0]; //为当前选中对象赋值
    var fileId = []; //上传文件存放表Id--目前传值为数组形式
    fileId.push(UpDataObj.id);
    //关闭上传dialog
    //uploadFileDig.close();--简单模式没有上传dialog
    //获取文件名称(整体)
    var fileName = data[0].fileName;
    $("#fileName").val(fileName);
    //分割为类型和名称
    var fileNameList = fileName.split(".");
    //清空流程词汇标题-加入提示内容(新建审核情况下)
    if (winTopData == "") {
        $("#worksTitle").val("");
        alertInter("blur");
    }
    //验证格式是否可执行操作(验证)
    if (fileNameList[1] != "xls") {
        CheckUploadExcel = false;
        hideTooltip("fileName");
        showTooltip("fileName", "此文件格式无法操作,请重新选择");
        //无效文件服务器端删除
        commonDeleteFile(fileId);
    } else {
        var param = {};
        param['FilePath'] = UpDataObj.savePath;
        param['TitleList'] = "专业关键词,修订备注";
        param['MaxLength'] = 14;
        var url = "/" + APP_PROJECT_NAME + "/" + APP_MODULES_NAME + "/cihuikuProcess" + "/" + "readXlsCheck.action";
        ajaxFunction(url, param, true,
        function(data) {
            if (!data.success) {
                //内容验证给予提示
                CheckUploadExcel = false;
                hideTooltip("fileName");
                showTooltip("fileName", data.mgs);
                //无效文件服务器端删除
                commonDeleteFile(fileId);
            } else {
                CheckUploadExcel = true;
                hideTooltip("fileName");
                //动态设置流程词汇标题为有效上传文件名(词汇标题为空的情况下)
                var worksTitle=$("#worksTitle").val();
                if (worksTitle== ""||worksTitle=="默认为,有效上传文件名") {
                    $("#worksTitle").val(fileNameList[0]);
                    $("#worksTitle").css("color", "black");
                }
            }
        });
    }
}
/*************************************************************************/

//下载方法调用
function NowDownLoad() {
    var fileName = winTopData.fileName; //原文件名称
    var savePath = winTopData.savePath; //存放文件绝对路径
    commonDownload(savePath, fileName,"ZKM");
}
/*************************************************************************/
//提示内容动态存除
function alertInter(State) {
    if ($("#worksTitle").val() == "默认为,有效上传文件名" && State == "focus") {
        $("#worksTitle").css("color", "black");
        $("#worksTitle").val("");
    } else if ($("#worksTitle").val() == "" && State == "blur") {
        $("#worksTitle").css("color", "#ccc");
        $("#worksTitle").val("默认为,有效上传文件名");
    }
}

/*************************************************************************/

/*树型文本获取*/
function userDialog(url, title, width, height) {
    var userDig = dialog({
        title: title,
        content: '<iframe src=' + url + ' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>',
        width: width,
        height: height
    });
    userDig.show();
    return userDig;
}
function addOrg() {
    var url = "/ZDesk/zh_cn/common/userMappingOrg.html?disponse=snode&showButton=y&reFillFn=getSelectNode&closeFn=closeDig&clearFn=clearDig&FilterBeanId=MessageQuanxianFilter&permissionName=留言复核标识";
    closeDialog = userDialog(url, "指定复核", "300", "400");
    //$("#checkUserBtn").attr("disabled","disabled");
}
//选择组织机构对应赋值
function getSelectNode(snode) {
    //判断选中是否为用户而非	机构
    if (snode[0].dataType == "ren") {
        //清空对应复核人val
        $("#appointedCheckUserID").val("");
        $("#appointedCheckUser").val("");
        //赋值
        $("#appointedCheckUserID").val(snode[0].id);
        $("#appointedCheckUser").val(snode[0].text);
        hideTooltip($("#appointedCheckUser").attr("id"));
        closeDig(); //关闭dialog
    } else {
        showTooltip($("#appointedCheckUser").attr("id"), "请正确选择处理人!");
    }

}

//关闭dialog
function closeDig() {
    window.closeDialog.close().remove();
    //$("#checkUserBtn").removeAttr("disabled");
}
//清空clearDig
function clearDig() {
    $("#appointedCheckUserID").val("");
    $("#appointedCheckUser").val("");
    closeDig(); //关闭dialog
}
/*************************************************************************/

$(function() {
    A_PROCESSDEFINITIONID = getQueryString("PROCESSDEFINTION_ID");
    A_TASKNAME = getQueryString("A_TASKNAME");
    A_FORMCLASS = getQueryString("A_FORMCLASS");
    NowLoginName = getUserInfo().loginName;
    
    //词汇库复核意见下拉和审批状态
    reviewAdviceOpinion = getDictListfoIndexData("WorksBase_fhAdvice"); //复核复核意见
    fhspOpinion = getDictListfoIndexData("WorksBase_fhspState"); //复核审批状态
    
    NowUploadFile(); //初始化简单上传主键
     
    var Type = getQueryString("TYPE"); //操作分类
    if(Type=="commonProcess"){
    	winTopData = window.top[A_PROCESSDEFINITIONID];
    	OpenInter(); //表单赋值
    	fhSelOption();    //绑定对应下拉
    }else if(Type=="new"){
   		AddNewWordsCheck(); //新建初始化
    }
});