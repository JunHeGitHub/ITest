
//根据文件名获取id
function getId(){
	$("#file_name").val();
	var rdata=[];
	var pat={};
	pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
	pat.nameSpaceId = 'getSU_QC_SOURCE_RECORD_DATA_id';
    pat.file_name=$("#file_name").val();;
    commonSelectForComplex(pat, false,function(data){
	    if(data){
			rdata = data.rows;	
		}
    }); 
    return rdata;
}


//加载数据
function initData() {
	/*
	var rdata;
	var pat = {};
	pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
	pat.where = {"is_mp3":"txt","data_state":"座席上诉"};
	commonSelect(pat, false, function (data) {
		if (data && data.rows != null) {
			rdata = data.rows;
		}
	});
	return rdata;
	*/
	var rdata=[];
	var pat={};	
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    pat.nameSpaceId = 'wenBenZuoYeFuShenPingFen_initData';
    pat.data_state_parames="'"+zinglabs.zqc.getState("zuoxishangsu")+"'";
    pat.score_state_parames="'"+zinglabs.zqc.getState("zuoxishangsu")+"'";
    commonSelectForComplex(pat, false,function(data){
	    if(data){
			rdata = data.rows;	
		}
    }); 
    return rdata;
}
//刷新
function refresh(){
     var table=$('#tableForData').DataTable();
     
     //清空筛选
     table.search("").draw();
     $("#shaixuan").val('');
     //清空数据
     table.clear().draw();
     //重新加载数据
     var data=searchData(); 
     table.rows.add(data).draw(true);

}

var pfdialog;
function pf(index){
	var dt =new Array();
	var oTable = $('#tableForData').dataTable();   
  	var nNodes = oTable.fnGetNodes(); 
  	var aa=getId();
  	var searchData="searchData";
	var params={
		title:"文本作业评分",
		url:"/ZDesk/zh_cn/ZQC/wenbenzuoyepingfen.html?data_source=fushen&id="+getId()[0].id+'&closeFn='+searchData+"&score_state="+getId()[0].score_state+"&grade_name="+getId()[0].grade_name+"&file_name="+getId()[0].file_name+"&file_location="+getId()[0].file_location,
		width:"900"
	};
	pfdialog=zinglabs.dialog(params);
}
//查询文本作业数据
function searchData(){	
	$("#updateFormDiv").slideUp(500);
	var rdata=[];
	var pat={};	
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    pat.nameSpaceId = 'wenBenZuoYeFuShenPingFen_search';
    pat.searchItems=searchItem2sql("searchDataForm");
    pat.data_state_parames="'"+zinglabs.zqc.getState("zuoxishangsu")+"'";
    pat.score_state_parames="'"+zinglabs.zqc.getState("zuoxishangsu")+"'";
    pat.file_name='%.html';
    commonSelectForComplex(pat, false,function(data){
	    if(data){
			var table=$('#tableForData').DataTable();	
			//清空筛选
		    table.search("").draw();
		    //清空数据
		    table.clear().draw();
		    //重新加载数据
		    var rdata=data.rows;
		    table.rows.add(rdata).draw(true);	
		}
    }); 
}