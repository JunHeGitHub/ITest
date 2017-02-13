
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


var IsInit=true; //是否为初始化
var TableDataList; //选中列表数据
var NewTreenodeIdToUpd;  //节点赋值(改绑)
var NowSelDataOne;  //当前选中的一条数据
var Delurl="/"+window.top.PRJ_PATH+"/ZDesk/yuliaokuWTTreeProcess/delDataToTree.action"; //解绑绑路径
var DelOfNodeurl="/"+window.top.PRJ_PATH+"/ZDesk/yuliaokuWTTreeProcess/delDataToTreeOfDelNode.action"; //解绑路径(删除节点时)
var getDataListToTreeurl="/"+window.top.PRJ_PATH+"/ZDesk/yuliaokuWTTreeProcess/getDataListToTree.action";//获取绑定到树的对应数据
var updDataToTreeurl="/"+window.top.PRJ_PATH+"/ZDesk/yuliaokuWTTreeProcess/updDataToTree.action";//改绑绑路径


//分页查询

function FYdoSearch(data, callback, settings) {
    $.blockUI(maskContent);
    if(!IsInit){
    //分页参数
	 var rows = settings._iDisplayLength.toString(); //获取数据长度
	 var offset = settings._iDisplayStart.toString(); //开始获取数据位置
	 var params={};
       params.recordType=WTTreerecordType;
       params.nodeId=WTTreenodeId; 
       params.limit=offset+","+rows;
		ajaxFunction(getDataListToTreeurl, params, true,
                function(data) {
                var pdata = {};
	            //数据
	            pdata.data =[];
	            //总条数 
	            var total =0;
	           if(data.success){
                 pdata.data=data.data.AllData;
                 total=data.data.total;
                }
	            pdata.recordsTotal = total;
	            //过滤没有使用到总条数
	            pdata.recordsFiltered = total;
	            // 代表第几次画页面 如pdata.draw 不能删如删掉不会翻页 
	            pdata.draw = settings.iDraw;
	            callback(pdata);
	            $.unblockUI();
        });
	}else{
		IsInit=false;
		var pdata = {};
            //数据
            pdata.data =[];
            var total =0;
            //总条数
            pdata.recordsTotal = total;
            //过滤没有使用到总条数
            pdata.recordsFiltered = total;
            // 代表第几次画页面 如pdata.draw 不能删如删掉不会翻页 
            pdata.draw = settings.iDraw;
            callback(pdata);
            $.unblockUI();
	}
}

//动态移动滚动条位置(动画效果)
function scroll2data(id) {
        $("html,body").animate({scrollTop: $("#" + id).offset().top},700);
}

//数据详情
function OpenSel() {
    nowSelData =NowSelDataOne;
    $("#interShow").show();
    scroll2data("corpusDiv");
    //循环设置内容框为只读状态
    $("#interShow input,textarea").each(function() {
        $(this).attr("readonly", "readonly");
    });
    for (var key in nowSelData) {
        if (nowSelData[key] == undefined || nowSelData[key] == null) {
            nowSelData[key] = "";
        }
        $("#" + key).val(nowSelData[key]);
    }
  
  }

/*绑定数据解绑*/
function DelDataOfTree(){
 		$.blockUI(maskContent);
		var check=CheckSelDataOrNode("解绑","WTTree","treeDemo","OnlyTable");
		if(!check){
		$.unblockUI();
		return;
		}
   		var ids="";
        for(var i in TableDataList){
        	ids=TableDataList[i].id+";"+ids;
        	}
       var params={};
       params.DataIdsStr=ids;
       params.recordType=WTTreerecordType;
       params.nodeId=WTTreenodeId;
		ajaxFunction(Delurl, params,true,function(data) {
		    if (data.success) {
                zinglabs.alert("解绑成功");
                doRepeatBySon();
            } else {
                zinglabs.alert("解绑失败");
            }
            $.unblockUI();
        });
}

/*绑定数据解绑(删除节点时)*/
function DelDataOfTreeDelNode(Nodes){
	   $.blockUI(maskContent);
       var params={};
       params.NodeIdsStr=Nodes;
       params.recordType=WTTreerecordType;
		ajaxFunction(DelOfNodeurl, params, true,function(data) {
		    if (data.success) {
                doRepeatBySon();
            }
            $.unblockUI();
        });
}

/**绑定数据改绑**/
function UpdDataOfTree(){
		 $.blockUI(maskContent);
		if(NewTreenodeIdToUpd==""){
		alert("获取改绑新节点失败");
		$.unblockUI();
		return;
		}
		var ids="";
        for(var i in TableDataList){
        	ids=TableDataList[i].id+";"+ids;
        }	
	   var params={};
	       params.DataIdsStr=ids;
	       params.recordType=WTTreerecordType;
	       params.NewnodeId=NewTreenodeIdToUpd;
	       params.OldnodeId=WTTreenodeId;
			ajaxFunction(updDataToTreeurl, params, true,function(data) {
		    if (data.success) {
                zinglabs.alert("改绑成功");
                doRepeatBySon();
            } else {
                zinglabs.alert("改绑失败");
            }
            $.unblockUI();
        });
}


/**验证是否选中数据和节点 
* 参数说明： 
* 1、 Title：提示标题
* 2、TableName：表格Name
* 3、TreeName：树Name
* 4、CheckType：验证类型(All：所有，Node：树节点，Table:表格选择)
**/
function  CheckSelDataOrNode(Title,TableName,TreeName,CheckType){
		var CheckResult=true;
		if(CheckType=="CheckAll"||CheckType=="OnlyTable"){
		var table = $('#'+TableName).DataTable();
    	var tt = new $.fn.dataTable.TableTools(table);
   		TableDataList= tt.fnGetSelectedData();  
   		if(TableDataList.length==0){
   		CheckResult=false;
   		zinglabs.alert("请选择要"+Title+"的数据");
   			}
   		}
   		
   		if(CheckType=="CheckAll"||CheckType=="OnlyNode"){
   		var ztree = $.fn.zTree.getZTreeObj(TreeName);
		var SelData=ztree.getSelectedNodes();
		if(SelData[0]==undefined){
		CheckResult=false;
		zinglabs.alert("请选择要"+Title+"的节点");
			}
		}
		return CheckResult;
}


/**刷新**/
function doRepeatBySon() {
    nowSelData = "";
    var table = $("#WTTree").DataTable();
    table.draw();
    $("#interShow").hide();
}

/*获取对应节点绑定的数据*/
function FindTreeToData(){
 	doRepeatBySon();
}
var closeDialog;
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
//改绑数据弹窗
function showDig(){
//验证是否选择数据
var check=CheckSelDataOrNode("改绑","WTTree","treeDemo","OnlyTable");
		if(!check){
		return;
		}
 var url = "/ZDesk/zh_cn/BOC/yuliaokuWTTree.html?recordType="+WTTreerecordType;
    closeDialog = userDialog(url, "指定复核", "300", "400");
}

//关闭dialog
function closeDig(){
	closeDialog.close();
}

//收起表单
function closeForm() {
    $("#interShow").hide();
}
