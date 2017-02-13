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

var dataGrid = {}; //datatable 初始化参数
var getActivitiTasksUrl = "/ZDesk/ZDesk/activityCommon/getUserTask.action"; //获取流程数据action路径
var A_PROCESSDEFINITIONID = ""; //流程标识
var activitiParameterAndData; //流程配置和获取来的流程数据
var A_PROCESSINSTANCEID = ""; //流程实例ID
var A_TASKID = ""; //节点ID
var A_TASKNAME = ""; //节点名称(-获取待办)
var A_FORMCLASS = ""; //表单类
var JBSTate = ""; //经办审批状态
var FHSTate = ""; //复核审批状态
var winTopData = "";
dataGrid = {
        'bPaginate': true,
        //分页器
        'bLengthChange': false,
        'bStateSave': true,
        //保留状态
        'bFilter': true,
        // 过滤功能
        // 开启服务端分页
        serverSide: true,
        //bPaginate: true, //翻页功能
        //bFilter: true, //过滤功能
        ajax: FYdoSearch,
        'bSort': true,//按列排序
        'bInfo': true, //页脚信息
        'bAutoWidth': false,//自动宽度
        'pagingType': "full_numbers",
         'sScrollX':'100%',//垂直滚动条,滚动区域
         "scrollY": '400px',
        //  "scrollCollapse": "true",
        'sPaginationType': 'bootstrap',
        //分页样式
        //pagingType : "full_numbers",
        'dom': 'TRlrtip',
        "oLanguage": {
                "sSearch": "搜索:",
                "sLengthMenu": "每页显示 _MENU_ 条记录",
                "sZeroRecords": "Nothing found - 没有记录",
                "sInfo": "显示第  _START_ -  _END_ 条,一共  _TOTAL_ 条",
                "sInfoEmpty": "显示0条记录",
                "scrollY": false,
                "scrollX": false,
                "scrollCollapse": false,
                "oPaginate": {
                        "sFirst": "首页",
                        "sPrevious": " 上一页 ",
                        "sNext": " 下一页 ",
                        "sLast": "尾页"
                }
        },
        tableTools: {
                //single 单选 multi 多选  os 可以按ctrl 选择
                // sRowSelect:'multi',
                //回调 单选
                fnRowSelected: function(nodes) {
                        var index = nodes[0]._DT_RowIndex;
                        var data = $('#activitibody').DataTable().row(index).data();
                        winTopData = data;
                        OpenInter();
                },
                sRowSelect: 'os',
                //sRowSelector:'td:first-child',
                aButtons: [
                /*{
      "sExtends":    "select_all",
      "sButtonText": "选择全部",
      "target":      "#select"
    } , 'select_none' */
                ]
        }
}

//初始化
function initTable() {
	$.blockUI(maskContent);
 		//验证用户做相应处理
        checkUser();
        //查询全部下拉(并存放对应模板内容),父级下拉id后期绑定用
        NeedParentVal("TemParentSel");
        doSearchAllTem();  //模板数据	
        
        if (window.top[A_PROCESSDEFINITIONID + "clumns"] == undefined) {
                var param = {};
                param.A_PROCESSDEFINITIONID = A_PROCESSDEFINITIONID; //流程标识
                putLoginNameToParam(param); //对象存入当前用户为处理人
                param.A_ASSIGNEE = loginName; //存入当前用户为处理人
                param.A_FORMCLASS = A_FORMCLASS; //表单类
                ajaxFunction(getActivitiTasksUrl, param, true,
                function(data) {
                        if (data.data) {
                                activitiParameterAndData = data.data;

                                //表单存入首页全局变量，方便减少没必要的请求
                                window.top[A_PROCESSDEFINITIONID + "clumns"] = activitiParameterAndData.clumns;

                                dataGrid.columns = eval(activitiParameterAndData.clumns); //表格表头填充
                                $('#activitibody').dataTable(dataGrid);
                                var table = $('#activitibody').DataTable();
                                //table.page.len('10').draw();	
                                $('#activitibody tbody').on('click', 'button',
                                function(obj) {
                                        var data = table.row($(this).parents('tr')).data();
                                });
                                var colvis = new $.fn.dataTable.ColVis(table, {
                                        buttonText: '自定义显示列',
                                        exclude: [0, -1]
                                });
                                $(colvis.button()).insertBefore('#cols');
                        }
                });
        } else {
                dataGrid.columns = eval(window.top[A_PROCESSDEFINITIONID + "clumns"]);
                $('#activitibody').dataTable(dataGrid);
        }
}

/**刷新**/
function NowdoRepeat() {
        var table = $('#activitibody').DataTable();
        table.draw(true);
        $("#allInterDiv").hide(); //隐藏表单
        winTopData = "";  //清空当前数据
}

//分页查询
function FYdoSearch(data, callback, settings) {
       $.blockUI(maskContent);
        var param = {};
        param.A_PROCESSDEFINITIONID = A_PROCESSDEFINITIONID;
        putLoginNameToParam(param);
        param.A_ASSIGNEE = loginName;
        param.A_FORMCLASS = A_FORMCLASS;
        param.A_BeginNum = settings._iDisplayStart;
        param.A_MaxNum = settings._iDisplayLength;
        ajaxFunction(getActivitiTasksUrl, param, true,
        function(Nowdata) {
                if (Nowdata.data) {
                        if (Nowdata.data.resultformdata != undefined) {
                                var pdata = {};
                                //数据
                                pdata.data = Nowdata.data.resultformdata;
                                var total = Nowdata.data.total;
                                //总条数
                                pdata.recordsTotal = total;
                                //过滤没有使用到总条数
                                pdata.recordsFiltered = total;
                                // 代表第几次画页面  如pdata.draw 不能删 如删掉不会翻页  
                                pdata.draw = settings.iDraw;
                                callback(pdata);
                        } else {
                                var Notdata = [];
                                var pdata = {};
                                //数据
                                pdata.data = Notdata;
                                var total = 0;
                                //总条数
                                pdata.recordsTotal = total;
                                //过滤没有使用到总条数
                                pdata.recordsFiltered = total;
                                // 代表第几次画页面  如pdata.draw 不能删 如删掉不会翻页  
                                pdata.draw = settings.iDraw;
                                callback(pdata);
                        }
                }
                 $.unblockUI();
        });	
        $("#allInterDiv").hide();
}

/**************/

/*************************************************************************/

//判断用户是经办人还是审核人
function checkUser() {
	try{
			var allPerm = window.top.GOLBAL_PARAM["userAllPermisson"].base; //当前用户标识
		    //循环判断是否有相应权限标识
		    for (var i = 0; i < allPerm.length; i++) {
		        if (allPerm[i].name == "留言经办标识") {
		            $("#lqBtn").show();
		        }
		    }
		    for (var i = 0; i < allPerm.length; i++) {
		        if (allPerm[i].name == "留言复核标识") {
		            $("#hqdbBtn").show();
		        }
		    }
		    //存放对应审批状态变量
		    JBSTate = getDictListfoIndexData("Message_jbspState");
		    FHSTate = getDictListfoIndexData("Message_fhspState");
   		 }catch(e){};
}

/*************************************************************************/

//显示待办留言相关信息
function OpenInter() {
 	try{
        //流程数据循环赋值
        for (var key in winTopData) {
                if (winTopData[key] == undefined || winTopData[key] == null) {
                        winTopData[key] = "";
                }
                $("#" + key).val(winTopData[key]);
        }
        //默认选中第一项下拉值
        $('#approveState').find("option:first").attr("selected", "selected");
        //数据显示部分差异判别
        OtherSomeDiff();
    	}catch(e){}
}

//动态移动滚动条位置(动画效果)
function scroll2data(id) {
        $("html,body").animate({scrollTop: $("#" + id).offset().top},700);
}

//数据显示部分差异判别
function OtherSomeDiff() {
		try{
        $("#allInterDiv").show();  //表单DIV
        scroll2data("allInterDiv"); //实现动态移动到打开表单页面位置
        
        //选中对应父级下拉,同时选中之前选中的模板类型
        var TemSelVal = winTopData["MBLX"]; //模板类型val
        if (winTopData.HFFS == "电子邮件回复") {
                $("#TemParentSel").val("MessageTemTypeEmail").change();
        } else if (winTopData.HFFS == "短信回复") {
                $("#TemParentSel").val("MessageTemTypePhone").change();
        }

        if (TemSelVal != "" && TemSelVal != undefined) {
                $("#TemSel").val(TemSelVal);
        }

        //根据当前流程节点进行判断生成对应操作选项.(如:用户有多重权限,这种方法不会冲突)
        $("#approveState").empty();
        $("#approveState").append("<option value='------'>请选择</option>");
        if (winTopData.nodeName == "留言经办") {
                for (var i in JBSTate) {
                        $("#approveState").append("<option value='" + JBSTate[i].code + "'>" + JBSTate[i].name + "</option>");
                }
                //对应可写内容设定
                $("#reviewAdvice").attr("readonly", "readonly");
                $("#handAdvice").removeAttr("readonly");
                $("#ZDcheck").show(); //处理人复核可选择。		
        } else if (winTopData.nodeName == "留言复核") {
                for (var i in FHSTate) {
                        $("#approveState").append("<option value='" + FHSTate[i].code + "'>" + FHSTate[i].name + "</option>");
                }
                //对应可写内容设定
                $("#handAdvice").attr("readonly", "readonly");
                $("#reviewAdvice").removeAttr("readonly");
                $("#ZDcheck").hide(); //处理人复核不可选择。
        }

        //对应显示回复内容
        if (!$("#HFYX").is(":hidden")) {
                editor_a.setContent(winTopData.HF);
                $("#HF").val("");
        } else {
                editor_a.setContent("");
                $("#HF").val(winTopData.HF);
        };
        }catch(e){};
}
/*************************************************************************/
//相关验证
function checks(SubOrSave) {
        var BestRes = true;
        BestRes = checkTextMax(); //验证输入内容超出设定值
        if (BestRes) {
                BestRes = checkDZYJ(); //邮箱手机格式验证
        }
        //下拉选择是否选择
        if (SubOrSave == "Sub") {
                $("select").not($("#TemSel")[0]).each(function() {
                        if ($(this).val() == "------"||$(this).val() ==undefined) {
                                BestRes = false;
                                showTooltip($(this).attr("id"), "请选择对应内容");
                        } else {
                                hideTooltip($(this).attr("id"));
                        }
                });
             //邮件标题是否为空
   			 if($("#Emailtitle").val().length<=0){
      			BestRes = false;
       			showTooltip($("#Emailtitle").attr("id"), "邮件标题不可为空");
            } else {
                hideTooltip($("#Emailtitle").attr("id"));
         	}
        }
        return BestRes;
}
//邮箱电话格式验证
function checkDZYJ() {
        var BestRes = true; //返回值标识
        var checkres = "allno"; //验证结果
        var JSZH = $("#JSZH").val();
        var sEmail = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        var sMobile = /^1[3|4|5|8][0-9]\d{4,8}$/;
        //同时验证邮箱或手机号是否正确
        if (sMobile.test(JSZH)) {
                checkres = "dxok"
        } else if (sEmail.test(JSZH)) {
                checkres = "yxok"
        }
		//如果邮箱内容框隐藏
        if ($("#HFYX").is(":hidden")) {
                if (checkres != "dxok") {
                        BestRes = false;
                        hideTooltip($("#JSZH").attr("id"));
                        showTooltip($("#JSZH").attr("id"), "请输入正确手机号");
                } else {
                        hideTooltip($("#JSZH").attr("id"));
                }
        } else {
                if (checkres != "yxok") {
                        BestRes = false;
                        hideTooltip($("#JSZH").attr("id"));
                        showTooltip($("#JSZH").attr("id"), "请输入正确邮箱");
                } else {
                        hideTooltip($("#JSZH").attr("id"));
                }
        }
        return BestRes;
}
//填写内容最大值
function checkTextMax() {
        var mgsTooLog = "内容过多，请删减调整";
        var BestRes = true;
        var HFInter = "";
        //内容的不同获取
        if ($("#HFYX").is(":hidden")) {
                HFInter = $("#HF").val();
        } else {
                HFInter = editor_a.getContent();
        }
		
        if (HFInter.length >= 3500) {
                BestRes = false;
                if ($("#HFYX").is(":hidden")) {
                        showTooltip($("#HFDX").attr("id"), mgsTooLog);
                } else {
                        showTooltip($("#HFYX").attr("id"), mgsTooLog);
                }
        } else {
                hideTooltip($("#HFYX").attr("id"));
                hideTooltip($("#HFDX").attr("id"));
        }
        if ($("#reviewAdvice").val().length >= 60) {
                BestRes = false;
                showTooltip($("#reviewAdvice").attr("id"), mgsTooLog);
        } else {
                hideTooltip($("#reviewAdvice").attr("id"));
        }
        if ($("#handAdvice").val().length >= 60) {
                showTooltip($("#handAdvice").attr("id"), mgsTooLog);
                BestRes = false;
        } else {
                hideTooltip($("#handAdvice").attr("id"));
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
/*************************************************************************/

/*************************************************************************/

/*************************************************************************/
//获取待办数据--按时间最早获取一条
function waitneed(checks) {
        $.blockUI(maskContent);
        if (checks == "lq") {
                var param = {};
                var pat = {};
                param['companyId'] = "000";
                var comId = companyId;
                if (comId != "" && comId != undefined && comId != null) {
                        param['companyId'] = comId;
                }
                var url = "/" + "ZDesk" + "/" + "ZDesk/messageProcess" + "/" + "randomData.action";
                ajaxFunction(url, param, true,
                function(data) {
                        if (data.success) {
                                if (data.data == undefined || data.data == "" || data.data == null) {
                                        zinglabs.alert("没有待处理的数据");
                                        return;
                                } else {
                                		//循环将留言数据存放在业务主表
                                        for (var key in data.data) {
                                                if (key == "id") {
                                                        pat.dataId = data.data.id;
                                                } else {
                                                        if (key != "ZT") {
                                                                pat[key] = data.data[key];
                                                        }
                                                }
                                        }
                                        //根据不同回复类型进行接收账号的统一赋值
                                        if (data.data.HFFS == "电子邮件回复") {
                                                pat.JSZH = data.data.DZYJ;
                                        } else if (data.data.HFFS == "短信回复") {
                                                pat.JSZH = data.data.SJ;
                                        }
                                        pat['A_PROCESSDEFINITIONID'] = A_PROCESSDEFINITIONID;
                                        pat['A_FORMCLASS'] = "MessageInsert";
                                        pat["disponseUser"] = loginName;
                                        pat["A_ASSIGNEE"] = loginName;
                                        pat["createUser"] = loginName;
                                        pat["A_UUID"] = true;
                                        pat["departmentName"] = departmentName;
                                        pat["departmentId"] = departmentId;
                                        pat["companyId"] = companyId;
                                        pat["companyName"] = companyName;
                                        pat["nodeName"] = "留言经办";
                                        pat["flowNode"] = "liuYanJB";
                                        pat["flowType"] = A_PROCESSDEFINITIONID;
                                        pat["approveState"] = "新建";
                                        putLoginNameToParam(pat);
                                        doStartProcess(pat,
                                        function(data) {
                                                if (!data.success) {
                                                        var patUpd = {};
                                                        patUpd.tableName = "Z_Messagebook";
                                                        patUpd.columnValues = {
                                                                "id": pat.dataId,
                                                                "ZT": "未处理"
                                                        };
                                                        commonUpdate(patUpd, true,
                                                        function(data) {});
                                                        zinglabs.alert("数据获取失败");
                                                } else {
                                                        NowdoRepeat();
                                                }
                                        });
                                }
                        } else {
                                zinglabs.alert("没有待处理数据");
                        }
                      $.unblockUI();
                });
        } else if (checks == "hqdb") {
                addOrgByHQDB();
                $.unblockUI();
        }
}

//判断是否选中数据进行操作提醒
function CheckIsDo(checks) {
		//代表没有选择数据
        if (winTopData == "") {
                waitneed(checks);
        } else {
                zinglabs.confirm("您正在编辑数据,成功执行此操作会清空未保存的数据!是否确定操作?",
                function() {
                        waitneed(checks);
                });
        }
}

//获取待办弹窗
function addOrgByHQDB() {
    var url = "/ZDesk/zh_cn/BOC/huoqudaiban.html?PROCESSDEFINTION_ID=" + A_PROCESSDEFINITIONID + "&doRepeat=NowdoRepeat" + "&A_TASKNAME=" + A_TASKNAME + "&A_FORMCLASS=" + A_FORMCLASS;
    var nowhtmlurl = window.location.pathname;
    nowhtmlurl += window.location.search;
    var now = window.parent.$("iframe[src='" + nowhtmlurl + "']");
    now.attr("name", "needforme");
    window.top.huoqudaibanDialog(url, "获取待办", "1000", "360");
}


var closeHQDBDig; 
function huoqudaibanDialog(url,title,width,height) {
 	var userDig = dialog({
				title : title,
				content : '<iframe src=' + url
						+ ' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>',
				width :width,
				height : height
			});
	closeHQDBDig =userDig.showModal();
}
//关闭dialog
function closeHQDBDigFn(){
	closeHQDBDig.close();
}

/*************************************************************************/
//保存
function saveAndAudit(WhySave, WhyError) {
	 $.blockUI(maskContent);
        if (checks("Save")) {
                var pat = {}
                pat["id"] = winTopData.id;
                pat["JSZH"] = $("#JSZH").val();
                pat["MBLX"] = $("#TemSel").find("option:selected").val();
                if ($("#HFYX").is(":hidden")) {
                        pat["HF"] = $("#HF").val();
                } else {
                        pat["HF"] = editor_a.getContent();
                }
                pat["HFFS"] = $("#TemParentSel").find("option:selected").text() + "回复";
                pat.handAdvice = $("#handAdvice").val();
                pat.appointedCheckUserID = $("#appointedCheckUserID").val();
                pat.appointedCheckUser = $("#appointedCheckUser").val();
                pat.reviewAdvice = $("#reviewAdvice").val();
                var patUp = {};
                patUp.tableName = "messagePorcess";
                patUp.columnValues = pat;
                commonUpdate(patUp, true,
                function(data) {
                        if (data.success) {
                                NowdoRepeat();
                                $("#allInterDiv").hide();
                        } else {
                                if (WhySave == "OnlySave") {
                                        zinglabs.alert("保存失败");
                                }
                        }
                        if (WhySave == "CanNotSubSave") {
                                CanNotSubSaveResult(data.success, WhyError);
                        }
                          $.unblockUI();
                });
        }else{
          $.unblockUI();
        }
}

/*************************************************************************/

//留言审核提交
function submitAndAudit() {
		$.blockUI(maskContent);
        if (checks("Sub")) {
                var pat = winTopData;  //当前选中数据对象赋予pat
                var SelState = $("#approveState").find("option:selected").text(); //审批状态
                pat.lastApproveState = pat.approveState;  
                pat.approveState = SelState;
                pat["JSZH"] = $("#JSZH").val();
                pat["MBLX"] = $("#TemSel").find("option:selected").val();
                if ($("#HFYX").is(":hidden")) {
                        pat["HF"] = $("#HF").val();
                } else {
                        pat["HF"] = editor_a.getContent();
                }
                pat["HFFS"] = $("#TemParentSel").find("option:selected").text() + "回复";
                if (SelState == "待审批") {
                        pat["nodeName"] = "留言复核";
                        pat["flowNode"] = "liuYanFH";
                        var appointedCheckUserID = $("#appointedCheckUserID").val();
                        if (appointedCheckUserID != undefined && appointedCheckUserID != "" && appointedCheckUserID != null) {
                                pat["flow_type"] = "2";
                                pat.appointedCheckUserID = appointedCheckUserID;
                                pat.appointedCheckUser = $("#appointedCheckUser").val();
                                pat.disponseUser = appointedCheckUserID;
                                pat["A_ASSIGNEE"] = appointedCheckUserID;
                        } else {
                                //没有指定处理人提交参数设置
                                //pat.disponseUser = "";--上一次用户需要记录
                                pat["A_ASSIGNEE"] = "";
                                pat["nodeName"] = "留言复核";
                                pat["flowNode"] = "liuYanFH";
                                pat["flow_type"] = "2";
                        }
                        pat.handAdvice = $("#handAdvice").val(); //经办意见

                } else if (SelState == "无需回复") {
                        pat["HF"] = "";
                        pat["nodeName"] = "完结";
                        pat["flowNode"] = "Over";
                        pat["flow_type"] = "-1";
                        pat.handAdvice = $("#handAdvice").val();

                } else if (SelState == "退回") {
                        pat.disponseUser = pat.createUser;
                        pat.appointedCheckUserID = "";
                        pat.appointedCheckUser = "";
                        pat["A_ASSIGNEE"] = pat.createUser;
                        pat["nodeName"] = "留言经办";
                        pat["flowNode"] = "liuYanJB";
                        pat["flow_type"] = "1";
                        pat.reviewAdvice = $("#reviewAdvice").val();

                } else if (SelState == "发送") {
                        pat["flow_type"] = "-2";
                        pat["nodeName"] = "完结";
                        pat["flowNode"] = "Over";
                        pat.reviewAdvice = $("#reviewAdvice").val();
                           zinglabs.confirm("是否确定提交流程并发送对应消息?",function(){
                           SendInter(pat); //回馈信息
                          });
                          $.unblockUI();
                          return;
                }
        ActiSubmit(pat);
       	}else{
          $.unblockUI();
        }
}

//提交失败保存结果提示语方法(异步请求策略)
function CanNotSubSaveResult(Result, WhyError) {
        var tishiyu = "但已成功保存";
        if (!Result) {
                tishiyu = "保存失败";
        }
        zinglabs.alert("提交"+WhyError+tishiyu);
}

//流程提交方法
function ActiSubmit(pat) {
	  $.blockUI(maskContent);	
        pat.A_FORMCLASS = "MessageUpdate"; //表单处理类
        if (pat.a_id != "" && pat.a_id != null && pat.a_id != undefined) {
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
                zinglabs.alert("流程任务编号获取失败");
                $.unblockUI();
        }
}

//发送相关信息
function SendInter(pat) {
 	 $.blockUI(maskContent);	
        var JSZH = $("#JSZH").val(); //接收账号
        if ($("#HFYX").is(":hidden")) {
                var HF = $("#HF").val();
                //发送短信接口方法
                sendShortMessage(JSZH, HF,
                function(data) {
                        if (data.success) {
                                zinglabs.alert("短信发送成功");
                                ActiSubmit(pat);
                        } else {
                                saveAndAudit("CanNotSubSave", "失败,因短信发送失败");
                        }
                });
        } else {
                var Emailtitle = $("#Emailtitle").val(); //邮件标题
                var HF = editor_a.getContent(); //邮件内容
                //发送邮件接口方法
                simpleSendMail(JSZH, Emailtitle, HF,
                function(data) {
                        if (data.success) {
                              zinglabs.alert("邮件发送成功");
                                ActiSubmit(pat);
                        } else {
                                saveAndAudit("CanNotSubSave", "失败,因邮件发送失败");
                        }
                });
        }
}

/*************************************************************************/

/*树型文本获取*/
function userDialog(url, title, width, height) {
        var userDig = zinglabs.dialog({
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
}
//选择组织机构对应赋值
function getSelectNode(snode) {
  //判断选中是否为用户而非	机构
    if(snode[0].dataType=="ren"){
    //清空对应复核人val
    $("#appointedCheckUserID").val("");
    $("#appointedCheckUser").val("");
    //赋值
    $("#appointedCheckUserID").val(snode[0].id);
    $("#appointedCheckUser").val(snode[0].text);
      hideTooltip($("#appointedCheckUser").attr("id"));
      closeDig();//关闭dialog
    }else{
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
/***模板分类数据***/
//显示对应模板数据
function ShowTemVal(SonSelId, ShowText) {
        var SelSonCode = $("#" + SonSelId).find("option:selected").val();
        var NeedTemSonData = {};
        //判断是否为没有选择下拉值，进行清空内容框
        if (SelSonCode == "------") {
                if ($("#HFYX").is(":hidden")) {
                        $("#" + ShowText).val("");
                } else {
                        editor_a.setContent("");
                }
                return;
        }
        //遍历缓存模板内容集合进行选中对应内容方便赋值
        for (var i in TemSonData) {
                if (TemSonData[i].code == SelSonCode) {
                        NeedTemSonData = TemSonData[i];
                }
        }
        //内容框赋值
        if ($("#HFYX").is(":hidden")) {
                $("#" + ShowText).val(NeedTemSonData.TemplateContent);
        } else {
                editor_a.setContent(NeedTemSonData.TemplateContent);
        }
}

//不同回复类型，对应模板
function CheckOtherTem(SelParentCode) {
        //手机回复
        if (SelParentCode == "MessageTemTypePhone") {
                $("#HFDX").show();
                $("#HFYX").hide();
                $("#emailTitle").hide();
        } else {
                //邮件回复
                if (SelParentCode == "MessageTemTypeEmail") {
                        $("#HFDX").hide();
                        $("#HFYX").show();
                        $("#emailTitle").show();
                }
        }
}