var ZQC_GOLBAL_PARAM={};
ZQC_GOLBAL_PARAM["zhiJianYuanData"]={};
//处理显示与隐藏
function showFenPei(){
	$("#updateFormDiv").slideUp(300);
	$("#tianJiaJiaoCaiDiv").slideUp(300);
	$("#btn_tijiaojiaocai").hide();
	$("#btn_tianjiajiaocai").show();
	$("#fenPeiDiv").slideDown(300);
	$("#btn_fenpei").hide();
	$("#btn_tijiaofenpei").show();
	scroll2data("fenPeiDiv");
}
function hideAll(){
	$("#fenPeiDiv").slideUp(300);
	$("#btn_tijiaofenpei").hide();
	$("#btn_fenpei").show();
	
}
//加载数据
function initData() {
	/*
	var rdata;
	var pat = {};
	pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
	pat.where = {"is_mp3":"txt","data_state":"初检已评分"};
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
    pat.nameSpaceId = 'yuYinZuoYeFuJianPingFen_initData';
    pat.data_state_parames="'"+zinglabs.zqc.getState("fujianyifenpei")+"','"+zinglabs.zqc.getState("fujianweifenpei")+"','"+zinglabs.zqc.getState("chujianyifabu")+"'";
    pat.score_state_parames="'"+zinglabs.zqc.getState("chujianyipingfen")+"','"+zinglabs.zqc.getState("fujianyipingfen")+"','"+zinglabs.zqc.getState("fujianweipingfen")+"'";
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
    pat.nameSpaceId = 'yuYinZuoYeFuJianPingFen_search';
    pat.searchItems=searchItem2sql("searchDataForm");
    pat.data_state_parames="'"+zinglabs.zqc.getState("fujianyifenpei")+"','"+zinglabs.zqc.getState("fujianweifenpei")+"','"+zinglabs.zqc.getState("chujianyifabu")+"'";
    pat.score_state_parames="'"+zinglabs.zqc.getState("chujianyipingfen")+"','"+zinglabs.zqc.getState("fujianyipingfen")+"','"+zinglabs.zqc.getState("fujianweipingfen")+"'";
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
     if(data==undefined){
     	data=[];
     } 
     table.rows.add(data).draw(true);

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
		url:"/ZDesk/zh_cn/ZQC/yuyinzuoyepingfen.html?data_source=fujian&id="+dt[index].id+"&closeFn="+searchData1+"&score_state="+dt[index].score_state+"&grade_name="+dt[index].grade_name+"&file_name="+dt[index].file_name+"&file_location="+dt[index].file_location,
		width:"900"
	};
	pfdialog=zinglabs.dialog(params);
}
//分配 查询并显示质检员信息列表
function doFenPei(){
	
	//进行批量分配的操作
	var table=$('#tableForData').DataTable();
	//语音作业数据
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	//质检员数据
	var uselectData = ZQC_GOLBAL_PARAM["zhiJianYuanData"];
	if (selectData == undefined || selectData == null || selectData.length == 0) {
		//zinglabs.confirm("未选中列表中的数据！<br/>是否需要程序自动按平均分配规则分配列表中所有语音数据给选中的质检员？",function(){
			
			
		//});
		//alert("请选中要分配的数据!");
		zinglabs.i18n.alert("zqc_share_qxzyfpdsj");
		return;
	}
	
		//判断任务开始时间和结束时间是否为空
	if (timeCheck("fenPeiDiv #begin_time_retest_DFTB", "fenPeiDiv #end_time_retest_DFTE")) {
	//判断是否选择了质检员
	if(uselectData.length == undefined || uselectData.length == 0){
		//alert("请选中要分配语音数据给哪些质检员!");
		zinglabs.i18n.alert("zqc_share_fpsjgzjy");
		return;
	}
	//判断所选择数据是否未经过分配
	if(selectData && selectData!=null && selectData!=undefined){
		var si="id in (";
		$.each(selectData, function (n, d) {
			si+="'"+d.id+"',";
		});
		si=si.substring(0,si.length-1)+") and data_state ='"+zinglabs.zqc.getState("fujianyifenpei")+"'";
		var pat={};	
		pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
	    pat.nameSpaceId = 'zqc_common_checkyuyindata';
	    pat.searchItems=si;
		commonSelectForComplex(pat, false,function(data){
		    if(data && data.rows.length!=0){
				//alert("所要分配的数据中存在已分配的数据！");
				zinglabs.i18n.alert("zqc_share_czyfpdsj_plfp");
				return;	
			}else{
				//执行数据状态更新操作
				zinglabs.confirm(zinglabs.i18n.getText("zqc_share_qdfpsxzsj"), function () {
					
					var dcon=selectData.length;
					var ucon=uselectData.length;
					var updateData={};
					var tc=0;
					//处理选中的数据			
					for(var i=0; i<dcon; i++){
						for(var j=0 ; j<Math.round(dcon/ucon) ; j++){
							if(selectData[tc]==undefined){
								break;
							}
							updateData[tc]=selectData[tc];
							updateData[tc].data_state=""+zinglabs.zqc.getState("fujianyifenpei")+"";
							updateData[tc].score_state=""+zinglabs.zqc.getState("fujianweipingfen")+"";
							if(i==ucon){
								updateData[tc].owner = uselectData[0].loginName;
							}else{
								updateData[tc].owner = uselectData[i].loginName;
							}
							updateData[tc].begin_time_retest = $("#fenPeiDiv #begin_time_retest_DFTB").val();
							updateData[tc].end_time_retest = $("#fenPeiDiv #end_time_retest_DFTE").val();
							updateData[tc].assign_role = getUserInfo().loginName;
							tc+=1;
						}
					}
					
					var b=0;
					var c=0;
					$.each(selectData, function (n, d) {
						var pat = d;
						pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
						pat.primaryKey = "id";
						pat.primaryKeyValue = d.id;
						commonUpdate(pat,function(data){
							var json=eval('('+data+')'); 
							c+=1;	      
							if(!json.success){
								b+=1;			    	
						    }
						    if(b==0 && c==selectData.length){
						    	//alert("分配成功！");
						    	zinglabs.i18n.alert("system_caozuochenggong");
						    	refresh("tableForData",initData());
						    }else if(b!=0 && c!=selectData.length){
						    	//alert("分配异常！");
						    	zinglabs.i18n.alert("system_caozuoshibai");
						    }
						});			
					});
					
				});
			}
	    });	
		
	}
	}
}
//取消分配
function quXiaoFenPei(){
	
	var table = $("#tableForData").DataTable();
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	if (selectData.length == 0 || selectData == null || selectData == undefined) {
		//alert("请选中要取消分配的数据!");
		zinglabs.i18n.alert("zqc_share_qxzyqxfpdsj");
		return;
	}
	//判断是否有操作权限
	if(selectData && selectData!=null && selectData!=undefined){
		var c =0;  
		$.each(selectData, function (n, d) {
			if(d.assign_role != getUserInfo().loginName){
				c+=1;
			}			
		});
		if(c != 0){
			//alert("所要取消分配的数据中存在无权操作的数据，只能取消分配人标识为当前登录人的数据！");
			zinglabs.i18n.alert("zqc_share_czwqczdsj_fprbs");
			return;
		}
	}
	//判断所选择数据是否为初检已分配
	if(selectData && selectData!=null && selectData!=undefined){
		var c =0;  
		$.each(selectData, function (n, d) {
			if(d.data_state!=""+zinglabs.zqc.getState("fujianyifenpei")+""){
				c+=1;
			}			
		});
		if(c != 0){
			//alert("所要取消分配的数据中存在语音状态初检已发布、初检已评分或复检未分配的数据！");
			zinglabs.i18n.alert("zqc_share_qxfpdsjbhf_fj");
			return;
		}
	}
	//执行数据状态更新操作
	zinglabs.confirm(zinglabs.i18n.getText("zqc_share_qdqxfpsxzsj"), function () {
		var b=0;
		var c=0;
		$.each(selectData, function (n, d) {
			var pat = d;
			pat.data_state=""+zinglabs.zqc.getState("fujianweifenpei")+"";
			pat.score_state=""+zinglabs.zqc.getState("fujianweipingfen")+"";
			pat.owner = " ";
			pat.grade_name = " ";
			pat.assign_role = " ";
			pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
			pat.primaryKey = "id";
			pat.primaryKeyValue = d.id;
			commonUpdate(pat,function(data){
				var json=eval('('+data+')'); 
				c+=1;	      
				if(!json.success){
					b+=1;			    	
			    }
			    if(b==0 && c==selectData.length){
			    	//alert("取消分配成功！");
			    	zinglabs.i18n.alert("system_caozuochenggong");
			    	refresh("tableForData",initData());
			    }else if(b!=0 && c!=selectData.length){
			    	//alert("取消分配异常！");
			    	zinglabs.i18n.alert("system_caozuoshibai");
			    }
			});			
		});
		
	});
	
}
//选择质检员
function choseZhiJianYuan(){
	var params={
		title:"质检员列表",
		content: '<iframe src="/ZDesk/zh_cn/ZQC/zhijianyuanliebiao.html" style="border: 0; width: 100%; height: 100%"  frameborder="0"/>',
		width:"900",
		height:"570",
		ok: function(){
			var data=ZQC_GOLBAL_PARAM["zhiJianYuanData"];
			if(data.length == undefined || data.length == 0 ){
				//alert("请选中需要分配的质检员信息!");
				zinglabs.i18n.alert("zqc_share_qxzxyfpdzjyxx");
				return false;
			}
			var us="";
			for(var i=0;i<data.length;i++){
				us+=data[i].loginName+",";
			}
			us=us.substring(0,us.length-1);
			$("#zhiJianYuanList").val(us);
		},
		okValue:" 确 定 ",
		cancel:function(){},
		cancelValue:" 关 闭 "
	};
	zinglabs.dialog(params);
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
function showTianJiaJiaoCai(){
	$("#updateFormDiv").slideUp(300);	
	$("#fenPeiDiv").slideUp(300);
	$("#tianJiaJiaoCaiDiv").slideDown(300);
	$("#btn_tianjiajiaocai").hide();
	$("#btn_tijiaojiaocai").show();
	$("#btn_tijiaofenpei").hide();
	$("#btn_fenpei").show();
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



//开始时间和结束时间不能大于30天。
function timeCheck(bid, eid) {
	var bt = $("#" + bid).val();
	var et = $("#" + eid).val();
	bt = new Date(Date.parse(bt.replace(/-/g, "/")));
	et = new Date(Date.parse(et.replace(/-/g, "/")));
	if ($("#" + bid).val() == "" || $("#" + eid).val() == "") {
		zinglabs.i18n.alert("ZQC_renwukaishishijianbunengweikong");
		return false;
	}
	if (et.getTime() < bt.getTime()) {
		zinglabs.i18n.alert("ZQC_jieshushijianbunengdayukaishihijian");
		return false;
	}
	if ((U_DateDiffer(bt, et) / 24) > 30) {
		zinglabs.i18n.alert("ZQC_renwujieshushijianbunengdayuqishishijian");
		return false;
	} else {
		return true;
	}
}