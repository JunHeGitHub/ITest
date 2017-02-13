	
	function closeDig(){
	window.closeDialog.close();
    }				
			//添加国际化提示信息函数
function add() {
closeDialog=add_();
	//window.location.href="/"+PRJ_PATH+"/zh_cn/ueditor/sucaiguanli.html";
}
function add_(){
var u="/"+PRJ_PATH+"/zh_cn/ueditor/sucaiguanli_new.html";
	var closeDialog1=dialog({
		title:"素材库添加",
		width:"900",
		height:"650",
		content: "<iframe src='/"+PRJ_PATH+"/zh_cn/tuWenGuanLi/sucaiguanli_new.html?closeFn=closeDig' style='border: 0; width: 100%; height: 100%'  frameborder='0'/>"
	});
	closeDialog1.show();
	return closeDialog1;
}
function add_dan() {
var u="/"+PRJ_PATH+"/zh_cn/ueditor/sucaiguanli_new_dan.html";
	closeDialog=dialog({
		title:"素材库添加",
		width:"900",
		height:"650",
		content: "<iframe src='/"+PRJ_PATH+"/zh_cn/tuWenGuanLi/sucaiguanli_new_dan.html?closeFn=closeDig' style='border: 0; width: 100%; height: 100%'  frameborder='0'/>"
	});
	closeDialog.show();
	return closeDialog;
	//window.location.href="/"+PRJ_PATH+"/zh_cn/ueditor/sucaiguanli.html";
}
function initForm1(data) {
	var cfs = $("#updateForm input[name],textarea[name],select[name]");
	for (var i = 0; i < cfs.length; i++) {
		var e = cfs[i];
		var v = data[e.name];
		if (v == undefined || v == null) {
			v = "";
		}
		$(e).val(v);
	}
}
			
			//通用修改@@
function doUpdate() {
	if ($("#grade_idu").val() == "" || $("#grade_idu").val() == null || $("#grade_idu").val() == undefined || $("#teach_set_timeu").val() == "" || $("#teach_set_timeu").val() == null || $("#teach_set_timeu").val() == undefined || $("#generate_timeu").val() == "" || $("#generate_timeu").val() == null || $("#generate_timeu").val() == undefined) {
		       //alert('请填写必填项');
		zinglabs.i18n.alert("ZJGL_tianxiebitianxiang");
		return;
	}
	if ($("#remarku").val().length >= 200) {
		zinglabs.i18n.alert("ZQC_zifuchaochang");
		return;
	}
	var pat = zkm_getHtmlFormJsons("updateForm");
	pat.tableName = "AGENT_TEACH_QUERY";
	pat.primaryKey = "id";
	pat.primaryKeyValue = $("#id").val();
	commonUpdate(pat, function (data) {
		var json = eval("(" + data + ")");
		if (json.success) {				        
				        //alert('修改成功');
			zinglabs.i18n.alert("system_updateSuccess");
			$("#grade_idu").val("");
			$("#id").val("");
			$("#import_useru").val("");
			$("#phone_numberu").val("");
			$("#teach_set_timeu").val("");
			$("#generate_timeu").val("");
			$("#file_nameu").val("");
			$("#file_locationu").val("");
			$("#remarku").val("");
			refresh();
		} else {
				        //alert('修改失败');
			zinglabs.i18n.alert("system_updateFailed");
		}
	});
}
			//@@
function refresh() {
	var table = $("#zuoxipeixun").DataTable();
		     //清空筛选
	table.search("").draw();		     
		     //清空数据
	table.clear().draw();
		     //重新加载数据
	var data = doSearch1();
	table.rows.add(data).draw(true);
}
	//通用查询@@		
function doSearch() {
	var rdata = [];
	var pat = {};
	pat.tableName = "AGENT_TEACH_QUERY";
	pat.primaryKey = "id";
	commonSelect(pat, false, function (data) {
		if (data.rows != null) {
			rdata = data.rows;
		}
	});
	var table = $("#zuoxipeixun").DataTable();
		     //清空筛选
	table.search("").draw();		     
		     //清空数据
	table.clear().draw();
		     //重新加载数据
		     //var data=doSearch(); 
	table.rows.add(rdata).draw(true);
}
	//@@条件查询
function doSearch1() {
	var rdata = [];
	var tableForm = spellSelectParams("find");
	
	
	//var params = {};
	tableForm.tableName = "graphicInformationManager";
	tableForm.isPagination=false;
                //传到后台的值 
	tableForm.columnValues.parentId = 0;
	tableForm.columnValues.Category=tableForm.columnValues.Category1;
	tableForm.like.Category="Category";
	delete tableForm.columnValues.Category1;
	delete tableForm.like.Category1;
	tableForm.equal.parentId="parentId";
	//tableForm.orderBy="dataTime desc";
                //where条件的对应关系
	commonSelect(tableForm, false, function (data) {
		if (data.data != null) {
			rdata = data.data;
		}
		//alert(JSON.stringify(data));
	});
	var table = $("#zuoxipeixun").DataTable();
		     //清空筛选
	table.search("").draw();		     
		     //清空数据
	table.clear().draw();
		     //重新加载数据
		     //var data=doSearch(); 
	table.rows.add(rdata).draw(true);
	
}
function chongzhi(){
$("#title").val('');
	$("#autor").val('');
	$("#Category").val('');
}
	//拼接筛选项
function appendSearchItem(formName) {
	var pat = {};
	var temp = "{";
	var cfs = $("#" + formName + " input[name]");
	for (var i = 0; i < cfs.length; i++) {
		var e = cfs[i];
		if (e.value != "" && e.value != null && e.value != undefined) {
			temp += "'" + e.name + "':'" + e.value + "',";
		}
	}
	temp = temp.substring(0, temp.length - 1) + "}";
	if (temp.length != 1 && temp != "") {
		pat = eval("(" + temp + ")");
	}
	return pat;
}			
			//通用删除@@
function doDelte(){
var table = $("#zuoxipeixun").DataTable();
	  var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	if(selectData.length<1){
	zinglabs.alert('请选择要删除的数据');return;
	}
zinglabs.confirm("确认要删除此项？",function(){
       var params={};
       var params1={};
	   params.tableName="graphicInformationManager";
	   params1.tableName="graphicInformationManager";
	   //主键默认为id  如不为id 加上params.primarykey
	   params1.primarykey="groupId";
	   var ids="";
	   for(var i=0;i<selectData.length;i++){
	          var key="id";
	         if("primarykey" in params){
	             key=params["primarykey"];
	         }
	         ids+=selectData[i][key]+",";
	     }
	     params.columnValues=ids;
	     commonDelete(params,true,function(data){
	         if(data.success){
	         doSearch1();
	         }
	    }); 
	    
	     params1.columnValues=ids;
	     commonDelete(params1,true,function(data){
	         if(data.success){
	         zinglabs.alert("删除成功");
	         }
	    }); 
 });
}
function  doFabu(){
	var table = $("#zuoxipeixun").DataTable();
	  var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	if(selectData.length<1){
	zinglabs.alert('请选择要删除的数据');
	return;
	}
	for(var i=0;i<selectData.length;i++){
	var json={};
	json.id=selectData[i].id;
	json.isRelease="已发布";
			var params={};
		    params.columnValues=json;
		    params.tableName="graphicInformationManager";
		    //如更改不是id 需定义 params.primarykey
		    commonUpdate(params,true,function(data){
		    if(data.success){
		    doSearch1();
		    }
		    });
	
	}
	 		 
		    zinglabs.alert("发布成功");

}
function doQuxiaofabu(){
	var table = $("#zuoxipeixun").DataTable();
	  var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	if(selectData.length<1){
	zinglabs.alert('请选择要删除的数据');
	return;
	}
	for(var i=0;i<selectData.length;i++){
	var json={};
	json.id=selectData[i].id;
	json.isRelease="未发布";
			var params={};
		    params.columnValues=json;
		    params.tableName="graphicInformationManager";
		    //如更改不是id 需定义 params.primarykey
		    commonUpdate(params,true,function(data){
		    if(data.success){
		    doSearch1();
		    }
		    });
	
	}
	 		 
		    zinglabs.alert("取消发布成功");

} 						
			//刷新
function refresh() {
	var table = $("#zuoxipeixun").DataTable();
			     
			     //清空筛选
	table.search("").draw();
			     //清空数据
	table.clear().draw();
			     //重新加载数据
	var data = doSearch();
	table.rows.add(data).draw(true);
}
			//每页显示条数
function setPageNum(obj) {
	var table = $("#zuoxipeixun").DataTable();
	if(obj.value!= undefined){
			  	table.page.len(obj.value).draw();
			  }
			  //alert(obj.value);
}
			//全选
function toggleChecks(obj) {
	var table = $("#zuoxipeixun").DataTable();
	var tableTools = new $.fn.dataTable.TableTools(table);
	var ischeck = $("#selectall").is(":checked");
	if (ischeck) {
		tableTools.fnSelectAll();
	} else {
		tableTools.fnSelectNone();
	}
}			
			//通用添加@@
function doInsert() {
	if ($("#grade_ida").val() == "" || $("#grade_ida").val() == null || $("#grade_ida").val() == undefined || $("#teach_set_timea").val() == "" || $("#teach_set_timea").val() == null || $("#teach_set_timea").val() == undefined || $("#generate_timea").val() == "" || $("#generate_timea").val() == null || $("#generate_timea").val() == undefined) {
		       //alert('请填写必填项');
		zinglabs.i18n.alert("ZJGL_tianxiebitianxiang");
		return;
	}
	var pat = zkm_getHtmlFormJsons("addForm");
	pat.tableName = "AGENT_TEACH_QUERY";
				//alert(JSON.stringify(pat));				
	commonInsert(pat, function (data) {
		var json = eval("(" + data + ")");
		if (json.success == true) {
							//alert('添加成功');
			zinglabs.i18n.alert("system_saveSuccess");
			doSearch1();
			return;
		} else {
							//alert('添加失败');
			zinglabs.i18n.alert("system_saveFailed");
			return;
		}
	});
}
			
			
			
			//-----------------------------------
function checkText() {
	if ($("#grade_ida").val() == "" || $("#grade_ida").val() == null || $("#grade_ida").val() == undefined) {
		CHECK_name = false;
		showTooltip("text-Div", "\u6b64\u9879\u4e0d\u80fd\u4e3a\u7a7a\uff01");
	} else {
		CHECK_name = true;
		hideTooltip("text-Div");
	}
}
function checkTime1() {
	if ($("#teach_set_timea").val() == "" || $("#teach_set_timea").val() == null || $("#teach_set_timea").val() == undefined) {
		CHECK_name = false;
		showTooltip("time1", "\u6b64\u9879\u4e0d\u80fd\u4e3a\u7a7a\uff01");
	} else {
		CHECK_name = true;
		hideTooltip("time1");
	}
}
function checkTime2() {
	if ($("#generate_timea").val() == "" || $("#generate_timea").val() == null || $("#generate_timea").val() == undefined) {
		CHECK_name = false;
		showTooltip("time2", "\u6b64\u9879\u4e0d\u80fd\u4e3a\u7a7a\uff01");
	} else {
		CHECK_name = true;
		hideTooltip("time2");
	}
}
function checkTime3() {
	if ($("#teach_set_timeu").val() == "" || $("#teach_set_timeu").val() == null || $("#teach_set_timeu").val() == undefined) {
		CHECK_name = false;
		showTooltip("time3", "\u6b64\u9879\u4e0d\u80fd\u4e3a\u7a7a\uff01");
	} else {
		CHECK_name = true;
		hideTooltip("time3");
	}
}
function checkTime4() {
	if ($("#generate_timeu").val() == "" || $("#generate_timeu").val() == null || $("#generate_timeu").val() == undefined) {
		CHECK_name = false;
		showTooltip("time4", "\u6b64\u9879\u4e0d\u80fd\u4e3a\u7a7a\uff01");
	} else {
		CHECK_name = true;
		hideTooltip("time4");
	}
}
//验证未通过样式
function showTooltip(id, title) {
	$("#" + id).attr("title", title);
	$("#" + id).tooltip({placement:"left"});
	$("#" + id).tooltip("show");
	$("#" + id).addClass("error");
}
//还原样式
function hideTooltip(id) {
	$("#" + id).tooltip("destroy");
	$("#" + id).removeClass("error");
}
/*function pf(index) {

	var dt = new Array();
	var oTable = $("#zuoxipeixun").dataTable();
	var nNodes = oTable.fnGetNodes();
	for (var i = 0; i < nNodes.length; i++) {
		dt.push(oTable.fnGetData(nNodes[i]));
	}
	var u="/"+PRJ_PATH+"/zh_cn/tuWenGuanLi/sucaiguanli_new_update.html?closeFn=closeDig&id="+dt[index].id;
		closeDialog=dialog({
		title:"素材库修改",
		width:"900",
		height:"650",
		content: "<iframe src="+u+" style='border: 0; width: 100%; height: 100%'  frameborder='0'/>"
	});
	closeDialog.show();
	return closeDialog;
	
}*/
function pf(id) {

	
	var u="/"+PRJ_PATH+"/zh_cn/tuWenGuanLi/sucaiguanli_new_update.html?closeFn=closeDig&id="+id;
		closeDialog=dialog({
		title:"素材库修改",
		width:"900",
		height:"650",
		content: "<iframe src="+u+" style='border: 0; width: 100%; height: 100%'  frameborder='0'/>"
	});
	closeDialog.show();
	return closeDialog;
	
}

