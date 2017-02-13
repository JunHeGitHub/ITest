//EXCEL导出函数
function protExecl(){
	var table = $("#tableForData").DataTable();
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	if(selectData.length>0){
	var str='';
	var grade_str='';
	var shifouweipingfen=0;
	for(var i=0;i<selectData.length;i++){
		if(selectData[i].score_state==zinglabs.zqc.getState("zuoxishangsu")){
			shifouweipingfen++;
			break;
		}
		if(getId2()[i].id!=''&&getId2()[i].id!=null&&getId2()[i].id!=undefined
			&&selectData[i].file_name!=''&&selectData[i].file_name!=null&&selectData[i].file_name!=undefined
		){
			str+="'"+getId2()[i].id+"',";
			grade_str+="'"+selectData[i].file_name.substring(0,selectData[i].file_name.lastIndexOf("."))+"',";
		}
	}
	if(shifouweipingfen!=0){
		zinglabs.i18n.alert("ZQC_xuanzhongshujuzhonghanyoushangsuweiwanchengshuju");
		return;
	}
	var id='';0
	var grade_id='';
	//if(str!='('&&grade_id!='('){
		id=str.substring(0,str.lastIndexOf(','));
		grade_id=grade_str.substring(0,grade_str.lastIndexOf(','));
	//}
		var excelAttr={};
		excelAttr.fileName="复审评分详情"+shijianchuo()+".xls";
		excelAttr.fTitle="复审评分详情";
		excelAttr.attrNames="评分开始时间@服务开始时间@评分流水号@质检员@座席工号@总分@评分项|评分|评语@总评语";
		excelAttr.dbid="ZDesk";
		excelAttr.nameSpace='com.zinglabs.apps.mybaits_xml_def.ZQC.propEXCEL'
		excelAttr.beanId='ZQC_propExcel'
		excelAttr.id=id;
		excelAttr.grade_id=grade_id;
		excelAttr.pingFenTableName='T_GRADE_DETAIL_FUSHEN_NEW';
		excelAttr.pingYuTableName='fushen_remark';
		excelAttr.shangSuTableName='SA_AGENT_ARGUMENT';
		z_exportExcel(excelAttr,function(){
			
		});
	}else{
		zinglabs.i18n.alert("ZQC_qingxuanzhongyaodaochudeshuju");
	}
}

//根据多个文件名获取多个id
function getId2(){
	var table = $("#tableForData").DataTable();
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	if(selectData.length>0){
		var str='';
		for(var i=0;i<selectData.length;i++){
			if(selectData[i].file_name!=''&&selectData[i].file_name!=null&&selectData[i].file_name!=undefined){
				str+="'"+selectData[i].file_name+"',";
			}
		}
		var file_name='';
		file_name=str.substring(0,str.lastIndexOf(','));
		var rdata=[];
		var pat={};
		pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
		pat.nameSpaceId = 'getSU_QC_SOURCE_RECORD_DATA_id2';
	    pat.file_name=file_name;
	    commonSelectForComplex(pat, false,function(data){
		    if(data){
				rdata = data.rows;	
			}
	    }); 
	    return rdata;
		}
}


//根据文件名获取id
function getId(){
	$("#file_name").val();
	var rdata=[];
	var pat={};
	pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
	pat.nameSpaceId = 'getSU_QC_SOURCE_RECORD_DATA_id';
    pat.file_name=$("#file_name").val();
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
function pf(dt){
	var searchData="searchData";
	var params={
		title:"语音作业评分",
		url:"/"+window.top.PRJ_PATH+"/zh_cn/ZQC/RS_yuyinzuoyepingfen.html?data_source=fushen&id="+getId()[0].id+'&closeFn='+searchData+"&score_state="+getId()[0].score_state+"&grade_name="+getId()[0].grade_name+"&file_name="+getId()[0].file_name+"&file_location="+getId()[0].file_location,
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
    pat.score_state_parames="'"+zinglabs.zqc.getState("zuoxishangsu")+"','"+zinglabs.zqc.getState("zuoxishangsuchenggong")+"','"+zinglabs.zqc.getState("zuoxishangsushibai")+"'";
   	pat.file_name='%.mp3';
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



