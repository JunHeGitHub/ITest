//加载数据
function initData() {
	/*
	var rdata;
	var pat = {};
	pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
	pat.where = {"is_mp3":"txt","owner":getUserInfo().loginName};
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
    pat.nameSpaceId = 'yuYinZuoYeChuJianPingFen_initData';
    pat.owner=getUserInfo().loginName;
    pat.data_state_parames="'"+zinglabs.zqc.getState("chujianyifenpei")+"'";
    pat.score_state_parames="'"+zinglabs.zqc.getState("chujianweipingfen")+"','"+zinglabs.zqc.getState("chujianyipingfen")+"'";
    commonSelectForComplex(pat, false,function(data){
	    if(data){
			rdata = data.rows;	
		}
    }); 
    return rdata;
}
//查询语音作业数据
function searchData(){	
	$("#updateFormDiv").slideUp(300);
	var rdata=[];
	var pat={};	
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    pat.nameSpaceId = 'yuYinZuoYeChuJianPingFen_search';
    pat.searchItems=searchItem2sql("searchDataForm");
    pat.owner=getUserInfo().loginName;
    pat.data_state_parames="'"+zinglabs.zqc.getState("chujianyifenpei")+"'";
    pat.score_state_parames="'"+zinglabs.zqc.getState("chujianweipingfen")+"','"+zinglabs.zqc.getState("chujianyipingfen")+"'";
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
//加载教材目录名
function initJiaoCaiMuLuMing(){
	var data=doSearch_jiaocaimuluming();
	
    var muluming="<option value='' selected ></option>";
    $.each(data, function (n, d) {
    	muluming+='<option value="' + d.teach_dir_description + '" >' + d.teach_dir_description + '</option>';
    });
    $("#teach_name").append(muluming);
}

var pfdialog;
function pf(index){
	var dt =new Array();
	var oTable = $('#tableForData').dataTable();   
  	var nNodes = oTable.fnGetNodes();
  	var searchData1="searchData";
  	for(var i=0; i<nNodes.length;i++){ 
    	dt.push(oTable.fnGetData(nNodes[i])); 
    }
	var params={
		title:"语音作业评分",
		onclose: function () {
	        	searchData();
    	},
		url:"/ZDesk/zh_cn/ZQC/yuyinzuoyepingfen.html?data_source=chujian&id="+dt[index].id+"&closeFn="+searchData1+"&score_state="+dt[index].score_state+"&grade_name="+dt[index].grade_name+"&file_name="+dt[index].file_name+"&file_location="+dt[index].file_location,
		width:"900"
	};
	pfdialog=zinglabs.dialog(params);
}
function showTianJiaJiaoCai(){
	$("#updateFormDiv").slideUp(300);	
	$("#tianJiaJiaoCaiDiv").slideDown(300);
	$("#btn_tianjiajiaocai").hide();
	$("#btn_tijiaojiaocai").show();
}
//判断教材语音是否已经存在
function panduanjiaocaiwenben(id){
	
	var parames={};
	var t="0";
	parames.tableName = "T_TEACH_DETAIL_NEW";
	parames.where = {"grade_id":id};
	commonSelect(parames, false, function (data) {
		if (data && data.rows != null && data.rows.length!=0) {
			t = "1";
		}
	});
	return t;
	
}
//添加教材
function tianJiaJiaoCai(){
	var table = $("#tableForData").DataTable();
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	if (selectData.length == 0 || selectData == null || selectData == undefined) {
		//alert("请选中一条数据!");
		zinglabs.i18n.alert("role_selectOneData");
		return;
	}
	if(selectData.length > 10){
		//alert("一次只能添加一条数据!");
		zinglabs.i18n.alert("zqc_share_yczntjytsj");
		return;
	}
	var muluming=$("#teach_name").val();
	if(muluming == undefined || muluming.length == 0){
		//alert("请选择教材目录名!");
		zinglabs.i18n.alert("zqc_share_qxzjcmlm");
		return;
	}
	var parames={};
	var file_name="";
	var yysj=0;
	$.each(selectData, function (n, d) {
		file_name=d.file_name.substring(0,d.file_name.indexOf(".mp3"));
		parames.grade_id=file_name;
		parames.import_user=getUserInfo().loginName;
		parames.phone_number=d.phone_number;
		parames.teach_set_time=fomateDate("YYYY-MM-dd hh:mm:ss",new Date());
		parames.file_name=d.file_name;
		parames.file_location=d.file_location;
		parames.teach_name=muluming;
		parames.remark=$("#remark").val();
		parames.file_location=d.file_location;
		if(panduanjiaocaiwenben(file_name) == "1"){
		//alert("该教材已存在，不可重复添加！");
		yysj+=1;
		}
		if(d.score_state==zinglabs.zqc.getState("fujianweipingfen")){
			yysj='fjwpf';
		}
		if(d.score_state==zinglabs.zqc.getState("chujianweipingfen")){
			yysj='cjwpf';
		}
	});
	if(yysj=='cjwpf'){
		//alert("数据中存在初检为评分数据!");
		zinglabs.i18n.alert("zqc_share_cjwpfsj");
		return;
	}
	if(yysj=='fjwpf'){
		//alert("数据中存在初检为评分数据!");
		zinglabs.i18n.alert("zqc_share_fjwpfsj");
		return;
	}
	if(yysj>0){
		//alert("数据中，有已存在或为评分！");
		zinglabs.i18n.alert("zqc_share_gjcycz");
		return;
	}else{
	var c=0;
	var b=0;
	$.each(selectData, function (n, d) {
		file_name=d.file_name.substring(0,d.file_name.indexOf(".mp3"));
		parames.grade_id=file_name;
		parames.import_user=getUserInfo().loginName;
		parames.phone_number=d.phone_number;
		parames.teach_set_time=fomateDate("YYYY-MM-dd hh:mm:ss",new Date());
		parames.file_name=d.file_name;
		parames.file_location=d.file_location;
		parames.teach_name=muluming;
		parames.remark=$("#remark").val();
		parames.file_location=d.file_location;
		parames.tableName = "T_TEACH_DETAIL_NEW";
		parames.primaryKey = "id";
		commonInsertAndGetId(parames,function(data){
		if(data.success){
			b+=1;
		}else{
			c+=1;
		}
		if(c==0 && b==selectData.length){
            //zinglabs.alert("添加成功！");
            zinglabs.i18n.alert("system_caozuochenggong");
		}
		if(c!=0 && b!=selectData.length){
		 	//alert(data.mgs);
		 	zinglabs.i18n.alert("system_caozuochenggong");
		}
	 });
	});
	}
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