
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
var WTTreeNewNodeId;  //改绑到新的节点
var getDataListNotToTreeurl="/"+window.top.PRJ_PATH+"/ZDesk/yuliaokuWTTreeProcess/getDataListNotToTree.action";//获取未绑定到树的对应数据
var companyId; //公司ID

//分页查询

function FYdoSearch(data, callback, settings) {
    $.blockUI(maskContent);
    if(true){
    IsInit=false;
    //分页参数
    var rows = settings._iDisplayLength.toString(); //获取数据长度
    var offset = settings._iDisplayStart.toString(); //开始获取数据位置
   var params={};
       params.companyId="01";
       if(NewTreerecordType=="yuliaokuWTTree-KHD"){
       params.clientQuestion="是";
       }
       if(NewTreerecordType=="yuliaokuWTTree-ZXD"){
       params.seatQuestion="是";
       }
       params.limit=offset+","+rows;
		ajaxFunction(getDataListNotToTreeurl, params, true,
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

/*绑定数据到树*/
function AddDataToTree(HaveDataIds){
		$.blockUI(maskContent);
		var check=CheckSelDataOrNode("绑定","WTTree","treeDemo","CheckAll");
		if(!check){
		$.unblockUI();
		return;
		}
		
   		var DataList=[]; //批量绑定数据集合
   		for(var i in TableDataList){
   		var param={};
   			param.dataId=TableDataList[i].id;
   			param.recordType=NewTreerecordType;
      		param.nodeId=NewTreenodeId;
   			DataList.push(param);
   		}
 		var pat = {};
        pat.tableName = "yuliaokuWTTree_detail";
        pat.primarykeyType = "uuid";
        pat.columnValues = DataList;
        commonInsertOrUpdate(pat, true,
        function(data) {
            if (data.success) {
                zinglabs.alert("绑定成功");
                doRepeatBySon();
            } else {
                zinglabs.alert("绑定失败");
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
   		return CheckResult;
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
}

/*获取对应节点未绑定的数据*/
function FindTreeToData(){
 	doRepeatBySon();
}
