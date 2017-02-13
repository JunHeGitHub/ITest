//遮罩属性		
var maskContent = {
    css: {
        border: 'none',
        padding: '15px',
        backgroundColor: '#000',
        '-webkit-border-radius': '10px',
        '-moz-border-radius': '10px',
        opacity: .5,
        color: '#fff',
        font: '14px'
    },
    message: '<img src="../../img/wait.gif" border="0" />  正在加载中，请稍候...'
};
var liuyanhuifuliushuiCurdStatus = "false";

var YWLX = {
    sql: "SELECT `name` AS `name`,`name` AS code FROM ComboDic WHERE comboName='LYFlowYW'",
    dbid: "ZDesk"
};
var SF = {
    sql: "SELECT `name` AS `name`,`name` AS code FROM ComboDic WHERE comboName='BOCArea'",
    dbid: "ZDesk"
};
var HFFS = {
    sql: "SELECT `name` AS `name`,`name` AS code FROM ComboDic WHERE comboName='Returntype'",
    dbid: "ZDesk"
};

var xmlJsonMap = {};

var fullName = "false";
var selStr = "select ";
var insertStr = " insert into ";

fullName = "messagePorcessHis ";
insertStr += "messagePorcessHis  ( ";
xmlJsonMap["fullName"] = {};

xmlJsonMap["fullName"]["id"] = {};
xmlJsonMap["fullName"]["id"]["attrName"] = "id";
xmlJsonMap["fullName"]["id"]["dbType"] = "int";
xmlJsonMap["fullName"]["id"]["visible"] = "false";
xmlJsonMap["fullName"]["id"]["fullName"] = "messagePorcessHis ";
xmlJsonMap["fullName"]["id"]["timeFormat"] = "";
xmlJsonMap["fullName"]["id"]["fieldType"] = "1";
selStr += "id";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["approveState"] = {};
xmlJsonMap["fullName"]["approveState"]["attrName"] = "approveState";
xmlJsonMap["fullName"]["approveState"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["approveState"]["visible"] = "true";
xmlJsonMap["fullName"]["approveState"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["approveState"]["timeFormat"] = "";
xmlJsonMap["fullName"]["approveState"]["fieldType"] = "${attr.fieldType}";
selStr += "approveState";
insertStr += "approveState";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["disponseUser"] = {};
xmlJsonMap["fullName"]["disponseUser"]["attrName"] = "disponseUser";
xmlJsonMap["fullName"]["disponseUser"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["disponseUser"]["visible"] = "true";
xmlJsonMap["fullName"]["disponseUser"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["disponseUser"]["timeFormat"] = "";
xmlJsonMap["fullName"]["disponseUser"]["fieldType"] = "${attr.fieldType}";
selStr += "disponseUser";
insertStr += "disponseUser";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["disponseETime"] = {};
xmlJsonMap["fullName"]["disponseETime"]["attrName"] = "disponseETime";
xmlJsonMap["fullName"]["disponseETime"]["dbType"] = "DATETIME";
xmlJsonMap["fullName"]["disponseETime"]["visible"] = "true";
xmlJsonMap["fullName"]["disponseETime"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["disponseETime"]["timeFormat"] = "yyyy-MM-dd HH:mm:ss";
xmlJsonMap["fullName"]["disponseETime"]["fieldType"] = "${attr.fieldType}";
selStr += "disponseETime";
insertStr += "disponseETime";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["dataId"] = {};
xmlJsonMap["fullName"]["dataId"]["attrName"] = "dataId";
xmlJsonMap["fullName"]["dataId"]["dbType"] = "int";
xmlJsonMap["fullName"]["dataId"]["visible"] = "true";
xmlJsonMap["fullName"]["dataId"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["dataId"]["timeFormat"] = "";
xmlJsonMap["fullName"]["dataId"]["fieldType"] = "${attr.fieldType}";
selStr += "dataId";
insertStr += "dataId";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["YWLX"] = {};
xmlJsonMap["fullName"]["YWLX"]["attrName"] = "YWLX";
xmlJsonMap["fullName"]["YWLX"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["YWLX"]["visible"] = "true";
xmlJsonMap["fullName"]["YWLX"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["YWLX"]["timeFormat"] = "";
xmlJsonMap["fullName"]["YWLX"]["fieldType"] = "${attr.fieldType}";
selStr += "YWLX";
insertStr += "YWLX";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["XM"] = {};
xmlJsonMap["fullName"]["XM"]["attrName"] = "XM";
xmlJsonMap["fullName"]["XM"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["XM"]["visible"] = "true";
xmlJsonMap["fullName"]["XM"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["XM"]["timeFormat"] = "";
xmlJsonMap["fullName"]["XM"]["fieldType"] = "${attr.fieldType}";
selStr += "XM";
insertStr += "XM";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["SF"] = {};
xmlJsonMap["fullName"]["SF"]["attrName"] = "SF";
xmlJsonMap["fullName"]["SF"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["SF"]["visible"] = "true";
xmlJsonMap["fullName"]["SF"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["SF"]["timeFormat"] = "";
xmlJsonMap["fullName"]["SF"]["fieldType"] = "${attr.fieldType}";
selStr += "SF";
insertStr += "SF";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["HFFS"] = {};
xmlJsonMap["fullName"]["HFFS"]["attrName"] = "HFFS";
xmlJsonMap["fullName"]["HFFS"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["HFFS"]["visible"] = "true";
xmlJsonMap["fullName"]["HFFS"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["HFFS"]["timeFormat"] = "";
xmlJsonMap["fullName"]["HFFS"]["fieldType"] = "${attr.fieldType}";
selStr += "HFFS";
insertStr += "HFFS";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["LYLX"] = {};
xmlJsonMap["fullName"]["LYLX"]["attrName"] = "LYLX";
xmlJsonMap["fullName"]["LYLX"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["LYLX"]["visible"] = "true";
xmlJsonMap["fullName"]["LYLX"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["LYLX"]["timeFormat"] = "";
xmlJsonMap["fullName"]["LYLX"]["fieldType"] = "${attr.fieldType}";
selStr += "LYLX";
insertStr += "LYLX";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["generateTime"] = {};
xmlJsonMap["fullName"]["generateTime"]["attrName"] = "generateTime";
xmlJsonMap["fullName"]["generateTime"]["dbType"] = "DATETIME";
xmlJsonMap["fullName"]["generateTime"]["visible"] = "false";
xmlJsonMap["fullName"]["generateTime"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["generateTime"]["timeFormat"] = "yyyy-MM-dd HH:mm:ss";
xmlJsonMap["fullName"]["generateTime"]["fieldType"] = "${attr.fieldType}";
selStr += "generateTime";
insertStr += "generateTime";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["CW"] = {};
xmlJsonMap["fullName"]["CW"]["attrName"] = "CW";
xmlJsonMap["fullName"]["CW"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["CW"]["visible"] = "false";
xmlJsonMap["fullName"]["CW"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["CW"]["timeFormat"] = "";
xmlJsonMap["fullName"]["CW"]["fieldType"] = "${attr.fieldType}";
selStr += "CW";
insertStr += "CW";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["JSZH"] = {};
xmlJsonMap["fullName"]["JSZH"]["attrName"] = "JSZH";
xmlJsonMap["fullName"]["JSZH"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["JSZH"]["visible"] = "false";
xmlJsonMap["fullName"]["JSZH"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["JSZH"]["timeFormat"] = "";
xmlJsonMap["fullName"]["JSZH"]["fieldType"] = "${attr.fieldType}";
selStr += "JSZH";
insertStr += "JSZH";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["HF"] = {};
xmlJsonMap["fullName"]["HF"]["attrName"] = "HF";
xmlJsonMap["fullName"]["HF"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["HF"]["visible"] = "false";
xmlJsonMap["fullName"]["HF"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["HF"]["timeFormat"] = "";
xmlJsonMap["fullName"]["HF"]["fieldType"] = "${attr.fieldType}";
selStr += "HF";
insertStr += "HF";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["appointedCheckUser"] = {};
xmlJsonMap["fullName"]["appointedCheckUser"]["attrName"] = "appointedCheckUser";
xmlJsonMap["fullName"]["appointedCheckUser"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["appointedCheckUser"]["visible"] = "false";
xmlJsonMap["fullName"]["appointedCheckUser"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["appointedCheckUser"]["timeFormat"] = "";
xmlJsonMap["fullName"]["appointedCheckUser"]["fieldType"] = "${attr.fieldType}";
selStr += "appointedCheckUser";
insertStr += "appointedCheckUser";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["handAdvice"] = {};
xmlJsonMap["fullName"]["handAdvice"]["attrName"] = "handAdvice";
xmlJsonMap["fullName"]["handAdvice"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["handAdvice"]["visible"] = "false";
xmlJsonMap["fullName"]["handAdvice"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["handAdvice"]["timeFormat"] = "";
xmlJsonMap["fullName"]["handAdvice"]["fieldType"] = "${attr.fieldType}";
selStr += "handAdvice";
insertStr += "handAdvice";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["reviewAdvice"] = {};
xmlJsonMap["fullName"]["reviewAdvice"]["attrName"] = "reviewAdvice";
xmlJsonMap["fullName"]["reviewAdvice"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["reviewAdvice"]["visible"] = "false";
xmlJsonMap["fullName"]["reviewAdvice"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["reviewAdvice"]["timeFormat"] = "";
xmlJsonMap["fullName"]["reviewAdvice"]["fieldType"] = "${attr.fieldType}";
selStr += "reviewAdvice";
insertStr += "reviewAdvice";

selStr += ",";
// 还是有最后一个带,的情况最后一个canEdit==false时。doAdd中有特殊处理
xmlJsonMap["fullName"]["NR"] = {};
xmlJsonMap["fullName"]["NR"]["attrName"] = "NR";
xmlJsonMap["fullName"]["NR"]["dbType"] = "varchar";
xmlJsonMap["fullName"]["NR"]["visible"] = "false";
xmlJsonMap["fullName"]["NR"]["fullName"] = "messagePorcessHis";
xmlJsonMap["fullName"]["NR"]["timeFormat"] = "";
xmlJsonMap["fullName"]["NR"]["fieldType"] = "${attr.fieldType}";
selStr += "NR";
insertStr += "NR";

selStr += " from " + fullName + " where 1=1 ";

var filters = [];
var sqlCount = "";

var excelAttr = {};
excelAttr.fileName = "留言回复流水";
excelAttr.fTitle = "留言回复流水";
excelAttr.attrNames = "状态@处理人@处理时间@留言标识@业务类型@姓名@省份@回复方式@留言类型@客户留言时间@称谓@接收账号@回复内容@复核处理人@经办意见@复核意见@内容";
excelAttr.dbid = "ZDesk";
excelAttr.sql = "select approveState,disponseUser,disponseETime,dataId,YWLX,XM,SF,HFFS,LYLX,generateTime,CW,JSZH,HF,appointedCheckUser,handAdvice,reviewAdvice,NR FROM messagePorcessHis where 1=1" || selStr;
excelAttr["id"] = "liuyanhuifuliushuiSearch";
excelAttr["dtid"] = "liuyanhuifuliushuiGrid";

var NowCompanyId = ""; //当前用户所在公司ID 
$(document).ready(function() {
	$.blockUI(maskContent);
	//初始化加载文本编辑器和当前用户所在公司ID 
    if(initEditAndNowComId()){
    initData();

    $('#liuyanhuifuliushuiGrid').DataTable({
        //滚动条
        //scrollY: 500,
        scrollX: '100%',
        //延迟加载
        deferRender: true,
        processing: false,
        //开启服务端分页
        serverSide: true,
        //服务器端分页ajax请求 可抽离出一个方法
        ajax: doSearch,

        bPaginate: true,
        //翻页功能
        // sPaginationType: "bootstrap",//分页样式
        pagingType: "full_numbers",
        pageLength: 10,
        bLengthChange: true,
        //改变每页显示数据数量
        bFilter: true,
        //过滤功能
        bSort: true,
        //排序功能
        bInfo: true,
        //页脚信息
        bAutoWidth: false,
        //自动宽度
        // autoWidth: false,
        dom: 'ZTRlrtip',
        columns: [{
            title: "序号",
            data: "DT_RowNumber",
            defaultContent: '',
            width: '3%',
            orderable: false
        },
        {
            title: "数据标识",
            data: 'id',
            width: '6%',
            visible: false
        },
        {
            title: "状态",
            data: 'approveState',
            width: '6%',
            visible: true
        },
        {
            title: "当前处理人",
            data: 'disponseUser',
            width: '6%',
            visible: true
        },
        {
            title: "时间",
            data: 'disponseETime',
            width: '6%',
            visible: true
        },
        {
            title: "留言标识",
            data: 'dataId',
            width: '6%',
            visible: true
        },
        {
            title: "业务类型",
            data: 'YWLX',
            width: '6%',
            visible: true
        },
        {
            title: "姓名",
            data: 'XM',
            width: '6%',
            visible: true
        },
        {
            title: "省份",
            data: 'SF',
            width: '6%',
            visible: true
        },
        {
            title: "回复方式",
            data: 'HFFS',
            width: '6%',
            visible: true
        },
        {
            title: "留言类型",
            data: 'LYLX',
            width: '6%',
            visible: true
        },
        {
            title: "客户留言时间",
            data: 'generateTime',
            width: '6%',
            visible: false
        },
        {
            title: "称谓",
            data: 'CW',
            width: '6%',
            visible: false
        },
        {
            title: "接收账号",
            data: 'JSZH',
            width: '6%',
            visible: false
        },
        {
            title: "回复内容",
            data: 'HF',
            width: '6%',
            visible: false
        },
        {
            title: "复核处理人",
            data: 'appointedCheckUser',
            width: '6%',
            visible: false
        },
        {
            title: "经办意见",
            data: 'handAdvice',
            width: '6%',
            visible: false
        },
        {
            title: "复核意见",
            data: 'reviewAdvice',
            width: '6%',
            visible: false
        },
        {
            title: "内容",
            data: 'NR',
            width: '6%',
            visible: false
        }],
        columnDefs: [{
            "searchable": false,
            "orderable": false,
            "targets": 0
        }],

        oLanguage: {
            infoFiltered: function() {

},
            "sLengthMenu": "每页显示 _MENU_条",
            "sZeroRecords": "没有找到符合条件的数据",
            "sProcessing": "&lt;imgsrc=’./loading.gif’ /&gt;",
            "sInfo": "当前第 _START_ - _END_ 条　共计 _TOTAL_ 条",
            "sInfoEmpty": "没有记录",
            "sInfoFiltered": "(从 _MAX_ 条记录中过滤)",
            "sSearch": "搜索：",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "前一页",
                "sNext": "后一页",
                "sLast": "尾页"
            }
        },
        tableTools: {
            fnRowSelected: function(nodes) {
                var index = nodes[0]._DT_RowIndex;
                rowSelTr = index;
                var data = table.row(index).data();
                openEdit(data);
            },
            sRowSelect: 'multi',

            aButtons: []
        }
    });

    //自定义列插件绑定
    //绑定事件 on
    var table = $('#liuyanhuifuliushuiGrid').DataTable();

    //  动态按钮事件

    BRegisterExtEvent("excelExportBtn", "", "click", "[{ fn:Z_ServerExcel ,fp:[excelAttr,xmlJsonMap,filters]}]");

    initValidateForm();

    $.unblockUI();
 }
});

/***
 * @TODO
 * 通用，增删改查
 */

function doAdd() {
    var bool = $("#liuyanhuifuliushuiAddForm").validate().form();
    if (bool) {
        $.blockUI(maskContent);
        var params = {};
        var cValues = zkm_getHtmlFormJsons("liuyanhuifuliushuiAddForm");
        params.columnValues = cValues;
        params.tableName = fullName;
        //使用增
        commonInsertOrUpdate(params, true,
        function(data) {
            try {
                if (data) {
                    var tableTmp = $("#liuyanhuifuliushuiGrid").DataTable();
                    if (data.success) {
                        try {
                            params.columnValues.id = data.idValues[0];
                            tableTmp.row.add(params.columnValues).draw();
                            //TODO 此处国际化
                            showSuccess("添加成功!");
                        } catch(e) {
                            showError(e.message);
                            logErr("Exception need see log " + e.name + ": " + e.message);
                            liuyanhuifuliushuiCurdStatus = "add";
                        }
                        doCleanAddForm();
                        Search();
                    } else {
                        showError(data.mgs);
                        logErr("Exception add failed  " + data + " " + ("Items" in data));
                        liuyanhuifuliushuiCurdStatus = "add";
                    }
                }
            } catch(exx) {
                logErr("Exception need see log " + exx.name + ": " + exx.message);
                // doAddModalClean();
                showError(data.mgs);
                liuyanhuifuliushuiCurdStatus = "add";
            }
            $.unblockUI();
        });
        //添加成功 数据清理状态 
        liuyanhuifuliushuiCurdStatus = false;
    } else {
        //添加失败据需添加
        liuyanhuifuliushuiCurdStatus = "add";
    }
};

function doEdit() {
    var bool = $("#liuyanhuifuliushuiEditForm").validate().form();
    if (bool) {
        $.blockUI(maskContent);
        var params = {};
        var cValues = zkm_getHtmlFormJsons("liuyanhuifuliushuiEditForm");
        //主键的值
        params.columnValues = cValues;
        params.tableName = fullName;
        //如更改不是id 需定义 params.primarykey
        commonUpdate(params, true,
        function(data) {
            try {
                var tableTmp = $("#liuyanhuifuliushuiGrid").dataTable();
                //   var tableDataTmp=$(pVars.id+"Grid").DataTable();
                if (data.success) {
                    try {
                        //                           // var rowIndex=tableDataTmp.row('.selected').row().index();
                        //                            row indx在某些情况下不靠谱，调整为直接传选中行
                        // var rowSelTr=tableDataTmp.$('tr.selected');
                        tableTmp.fnUpdate(params.columnValues, rowSelTr);
                        showSuccess("修改成功!");
                    } catch(e) {
                        showError("异常！");
                        logErr("Exception need see log " + e.name + ": " + e.message);
                    }
                } else {
                    logErr("Exception edit failed  " + data + " " + ("Items" in data));
                    showError("修改失败");
                }
            } catch(ex) {
                showError("异常！");
                logErr("doEdit 2222 Excep " + ex.name + ": " + ex.message);
            }
            $.unblockUI();
        });
        return false;
    }
    return "edit";
};

var urlProc = "/ZDesk/ZDesk/ZDesk_Proc/DBProc.action";
function doSearch(data, callback, settings) {
    if (GOLBAL_PARAM["isSearch"]) {
        //验证
        var bool = $("#liuyanhuifuliushuiSearchForm").validate().form();
        if (!bool) {
            return;
        }

        var pVars = {};
        pVars["id"] = "#liuyanhuifuliushui";
        pVars["tableName"] = fullName;
        pVars["dbId"] = "ZDesk";
        pVars["url"] = "/ZDesk/ZDesk/XmlDBAction.do";
        pVars["selStr"] = selStr;

        var url = pVars.url;
        if (url.indexOf("XmlDBAction.do") != -1) {
            url = urlProc;
        }

        var swhere = sepllGenPageWherebyForm(pVars.id + "Search");
        var rows = settings._iDisplayLength;
        var offset = settings._iDisplayStart;
        var params = {};

        params["action"] = "LoadWinDataJson";
        params["dbId"] = pVars.dbId;
        params["tableName"] = pVars.tableName;
        params["encode"] = "true";

        //自定义sql多表查询时查询总条数
        sqlCount = "select count(0)  " + selStr.substring(selStr.indexOf("from"), selStr.length);
        params["sql"] = Z_EU(sqlCount + swhere + getFilterSql(filters) + 'and companyId='+NowCompanyId);
        //  params["pageId"]="true";
        //  params["pageSize"]="true";
        ajaxFunction(url, params, true,
        function(dataCount) {
            var len = dataCount.Items[0]['count(0)'];
            params["sql"] = Z_EU(pVars.selStr + swhere + getFilterSql(filters)+'and companyId='+NowCompanyId+' limit ' + offset + ',' + rows);
            $.ajax({
                async: true,
                type: "post",
                url: url,
                dataType: "json",
                data: params,
                success: function(data) {
                    var pdata = {};
                    var size = data.Items.length;
                    for (var i = 0; i < size; i++) {
                        data.Items[i].DT_RowNumber = i + 1;
                    }
                    // 数据
                    pdata.data = data.Items;
                    // 总条数
                    pdata.recordsTotal = len || 0;
                    // 过滤没有使用到总条数
                    pdata.recordsFiltered = len || 0;
                    // 代表第几次画页面 如pdata.draw 不能删 如删掉不会翻页
                    pdata.draw = settings.iDraw;
                    callback(pdata);
                },
                error: function(r) {
                    logErr("doEdit 11111 Excep " + r.name + ": " + r.message);
                    var pdata = {};
                    pdata.data = {};
                    // 总条数
                    pdata.recordsTotal = len || 0;
                    // 过滤没有使用到总条数
                    pdata.recordsFiltered = len || 0;
                    // 代表第几次画页面 如pdata.draw 不能删 如删掉不会翻页
                    pdata.draw = settings.iDraw;
                    callback(pdata);
                }
            });
        });
    }
};

function doDelete() {
    zinglabs.confirm("确认要删除此项？",
    function() {
        var params = {};
        $.blockUI(maskContent);
        params.tableName = fullName;
        //主键默认为id  如不为id 加上params.primarykey
        var tableTmp = $('#liuyanhuifuliushuiGrid').DataTable();
        var cell = tableTmp.rows(".active").data();
        var ids = "";
        for (var i = 0; i < cell.length; i++) {
            var key = "id";
            if ("primarykey" in params) {
                key = params["primarykey"];
            }
            ids += cell[i][key] + ",";
        }
        params.columnValues = ids;
        commonDelete(params, true,
        function(data) {
            if (data) {
                //                数据不是以Items组织的情况,可以做多个判断
                if (data.success) {
                    try {
                        tableTmp.row('.selected').remove().draw(false);
                        showSuccess("删除成功!");
                    } catch(e) {
                        logErr("Exception del need see log " + e.name + ": " + e.message);
                    }
                    openAdd();
                } else {
                    logErr("Exception del failed  " + data + " " + data.retcode);
                    showError("删除失败");
                }
            }
            $.unblockUI();
        });
    });
};

//每页显示条数
function setPageNum(obj) {
    var table = $('#liuyanhuifuliushuiGrid').DataTable();
    table.page.len(obj.value).draw();
}
//刷新
function refresh() {
    //$("#liuyanhuifuliushuiSearchForm")[0].reset();--(原)清空查询表单不合理
    //收起表单
    closeForm();
    Search();
}

/***
 * 必须处理的严重错误，测试阶段可以alert 生产环境特殊标识写入日志
 * 发现就要分析原因
 * @param err
 */
function logErr(errMsg) {
    log.debug(errMsg);
    //    alert(errMsg);
    //    PT().log.error(errMsg);
};

/***
 * 嵌入ifram可以返回top parent
 * 非嵌入 可以返回 window ...
 * @returns {Window}
 * @constructor
 */
function PT() {
    return window;
};
//初始化验证组件
//fromid 表单ID
function initValidateForm() {

    $("#" + "liuyanhuifuliushuiAddForm").validate({
        onfocusout: false,
        onkeyup: false,
        rules: {

},
        //错误信息处理方法
        errorPlacement: function(error, element) {
            //销毁之间的提示信息
            element.tooltip("destroy");
            element.addClass('error');
            var errorInfo = error.html();
            element.tooltip({
                trigger: "submit",
                title: errorInfo,
                placement: "right"
            });
            element.tooltip("show");
        },
        //成功信息处理方法
        success: function($e, dom) {
            var element = $(dom);
            element.tooltip('hide');
            element.removeClass('error');
        }
    });

    $("#" + "liuyanhuifuliushuiEditForm").validate({
        onfocusout: false,
        onkeyup: false,
        rules: {

},
        //错误信息处理方法
        errorPlacement: function(error, element) {
            //销毁之间的提示信息
            element.tooltip("destroy");
            element.addClass('error');
            var errorInfo = error.html();
            element.tooltip({
                trigger: "submit",
                title: errorInfo,
                placement: "right"
            });
            element.tooltip("show");
        },
        //成功信息处理方法
        success: function($e, dom) {
            var element = $(dom);
            element.tooltip('hide');
            element.removeClass('error');
        }
    });

    $("#" + "liuyanhuifuliushuiSearchForm").validate({
        onfocusout: false,
        onkeyup: false,
        rules: {

            disponseETimetimebegin: {
                required: true,

                validateByDates: "true"

            },
            disponseETimetimeend: {
                required: true,

                compareBySumDate: {
                    name: 'disponseETime',
                    day: '30'
                }

                ,
                validateByDates: "true"

            }
        },
        //错误信息处理方法
        errorPlacement: function(error, element) {
            //销毁之间的提示信息
            element.tooltip("destroy");
            element.addClass('error');
            var errorInfo = error.html();
            element.tooltip({
                trigger: "submit",
                title: errorInfo,
                placement: "right"
            });
            element.tooltip("show");
        },
        //成功信息处理方法
        success: function($e, dom) {
            var element = $(dom);
            element.tooltip('hide');
            element.removeClass('error');
        }
    });

}

function initData() {

    fillDictionaryData("#liuyanhuifuliushuiSearchForm select[name=YWLX]", "@sql_YWLX", "select", "YWLX", "");
    fillDictionaryData("#liuyanhuifuliushuiAddForm select[name=YWLX]", "@sql_YWLX", "select", "YWLX", "");
    fillDictionaryData("#liuyanhuifuliushuiEditForm select[name=YWLX]", "@sql_YWLX", "select", "YWLX", "");

    fillDictionaryData("#liuyanhuifuliushuiSearchForm select[name=SF]", "@sql_SF", "select", "SF", "");
    fillDictionaryData("#liuyanhuifuliushuiAddForm select[name=SF]", "@sql_SF", "select", "SF", "");
    fillDictionaryData("#liuyanhuifuliushuiEditForm select[name=SF]", "@sql_SF", "select", "SF", "");

    fillDictionaryData("#liuyanhuifuliushuiSearchForm select[name=HFFS]", "@sql_HFFS", "select", "HFFS", "");
    fillDictionaryData("#liuyanhuifuliushuiAddForm select[name=HFFS]", "@sql_HFFS", "select", "HFFS", "");
    fillDictionaryData("#liuyanhuifuliushuiEditForm select[name=HFFS]", "@sql_HFFS", "select", "HFFS", "");
}

//打开提交表单
function openAdd() {
    $("#liuyanhuifuliushuiEdit").hide();
    $("#liuyanhuifuliushuiAdd").show();
    doCleanAddForm();

}
function Search() {
    GOLBAL_PARAM["isSearch"] = true;

    var tableTmp = $("#liuyanhuifuliushuiGrid").DataTable();
    tableTmp.draw(true);
}
//打开提交表单
function openEdit(data) {
    $("#liuyanhuifuliushuiEdit").show();
    $("#liuyanhuifuliushuiAdd").hide();
	//因text类型字段默认为null,改数据默认为 "".
    if(data.HF=="null"){
    data.HF="";
    }
    liuyanhuifuliushuiCurdStatus = "edit";
    var pvars = {
        "id": "liuyanhuifuliushuiEdit",
        "data": data
    };
    setPanelVal(pvars);
    //判断回复方式继续不同内容展示形式
    try {
        if (data.HFFS == "短信回复") {
            $("#HFDX").show();
            $("#HFYX").hide();
            editor_a.setContent("");
            $("#HF").val(data.HF);
        } else {
            $("#HFYX").show();
            $("#HFDX").hide();
            editor_a.setContent(data.HF);
            $("#HF").val("");
        }
    } catch(e) {}
    //循环设置内容框为只读状态
    $("#liuyanhuifuliushuiEditForm input,textarea").each(function() {
        $(this).attr("readonly", "readonly");
    });

}
//提交方法
function doSubmit() {

    try {

        if (liuyanhuifuliushuiCurdStatus == "add") {
            doAdd();
        } else if (liuyanhuifuliushuiCurdStatus == "edit") {
            doEdit();
        }
    } catch(e) {
        //        编辑、增加页面状态清理。一定要确保页面数据已经不再使用。
        zinglabs.alert("出现异常");
        liuyanhuifuliushuiCurdStatus = "false";
        logErr("Exception BtnAddSubmit need see log " + e.name + ": " + e.message);
    }

}
//显示错误信息
function showError(mgs) {
    //$("#errorDiv").show();
    //  $("#successDiv").hide();
    //  $("#errorDiv").html(mgs);
    //  setTimeout(function(){
    //    $("#errorDiv").hide();
    // },3000);
    zinglabs.alert(mgs);

}
//显示成功信息
function showSuccess(mgs) {
    //  $("#errorDiv").hide();
    // $("#successDiv").show();
    //  $("#successDiv").html(mgs);
    //  setTimeout(function(){
    //     $("#successDiv").hide();
    //  },3000);
    zinglabs.alert(mgs);
}
//清空添加表单
function doCleanAddForm() {
    liuyanhuifuliushuiCurdStatus = "add";
    var pvars = {
        "id": "liuyanhuifuliushuiAdd",
        "isClear": "true"
    };
    setPanelVal(pvars);
}

function cleanSearchForm() {
    $("#liuyanhuifuliushuiSearchForm")[0].reset();
}

//收起表单
function closeForm() {
    $("#liuyanhuifuliushuiEdit").slideUp();
}

//初始化加载文本编辑器和当前用户所在公司ID 
function initEditAndNowComId() {
	var CheckComId=false; //公司信息
    //获取当前用户所在公司用于数据过滤
    editor_a = new baidu.editor.ui.Editor({
        toolbars: [['undo', 'redo', '|', 'bold', 'italic', 'underline', 'removeformat', 'formatmatch', 'autotypeset', '|', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', '|', '|', 'link', 'unlink']],
        //更多其他参数，请参考editor_config.js中的配置项  
        //focus时自动清空初始化时的内容
        autoClearinitialContent: true,
        wordCount: false,
        //关闭字数统计
        elementPathEnabled: false,
        //关闭elementPath
        //autoHeightEnabled: false,//编辑器自动变高失效
        initialFrameWidth: 690,
        //设置编辑器宽度
        initialFrameHeight: 200,
        //设置编辑器高度
        readonly: true //设置为只读状态
    });
    editor_a.render('editor'); //此处的参数值为编辑器的id值 	 
    //当前用户所在公司ID赋值
    try{
    	comId = window.top.zkm_app_getCompanyId();
	    if(comId!=""&&comId!=undefined&&comId!=null){
	   		NowCompanyId=comId;
	   		CheckComId=true;
		}else{
			zinglabs.alert("对不起,请核实公司信息再使用");
		}
    }catch(e){
		zinglabs.alert("对不起,请核实公司信息再使用");
    };
    return CheckComId;
};