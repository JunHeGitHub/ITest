$(document).ready(function(){
		initShuJuZhuangTai("wenbenpingfenchaxun","data_state");
		initZhiBiao('grade_name');
		initPingFenZhuangTai("shujuchaxunwenbenpingfenchaxun","score_state");
		load_business_type("business_type");
		initPingFenZhuangTai("shujuchaxunwenbenpingfenchaxun","score_state");
		
	});
			function initForm(data){
				var cfs=$("#updateForm input[name],textarea[name]");
				for(var i=0;i<cfs.length;i++){
				var e=cfs[i];					
				var v=data[e.name];
				if(v==undefined || v==null){
						v='';
				}
				$(e).val(v);						
				}
		  }
			function scroll2data(id){
				$("html,body").animate({scrollTop: $("#"+id).offset().top}, 500); 
			}	
	
			//@@
			function refresh(){		 
		  	var table=$('#wenbeninfo').DataTable();
		     //清空筛选
		     table.search("").draw();		     
		     //清空数据
		     table.clear().draw();
		     //重新加载数据
		     var data=doSearch(); 
		     table.rows.add(data).draw(true);
		}
	//通用查询@@		
			function doSearch(){
					var rdata;
				   var pat = zkm_getHtmlFormJsons('searchDataForm');
					pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
					pat.primaryKey = "id";
					pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
					pat.nameSpaceId="SU_QC_SOURCE_RECORD_DATA_select";
					pat.is_mp3='txt';
					pat.ling='0';
					pat.chujianweifenpei=zinglabs.zqc.getState("chujianweifenpei");
					pat.fujianweifenpei=zinglabs.zqc.getState("fujianweifenpei");
					pat.chujianyifenpei=zinglabs.zqc.getState("chujianyifenpei");
					pat.fujianyifenpei=zinglabs.zqc.getState("fujianyifenpei");
			//		pat.where = appendSearchItem('searchItems');
					commonSelectForComplex(pat,false,function(data){			
					if(data.rows!= null){				
					  rdata=data.rows;				 	
			}			
		});
		return rdata;
	}
					
			//刷新
			function refresh(){
			     var table=$('#wenbeninfo').DataTable();
			     
			     //清空筛选
			     table.search("").draw();
			     //清空数据
			     table.clear().draw();
			     //重新加载数据
			     var data=doSearch(); 
			     table.rows.add(data).draw(true);			
			}
			//每页显示条数
			function setPageNum(obj){
			  var table=$('#wenbeninfo').DataTable();
			   if(obj.value!= undefined){
			  	table.page.len(obj.value).draw();
			  }
			  //alert(obj.value);
			}
			//全选
			 function toggleChecks(obj){
			 	var table=$('#wenbeninfo').DataTable();
			    var tableTools = new $.fn.dataTable.TableTools(table);
			       
			    var ischeck=$("#selectall").is(':checked');
			       if(ischeck){
			         tableTools.fnSelectAll();
			       }else{
			         tableTools.fnSelectNone();
			       }			      
			   }	
			   
			   
			
//按评分状态筛选
	function tableSearch(){
			$("#updateFormDiv").hide();
			//var begin=$("#begin_time_devoir_DFTB").val();//任务开始时间
			//var end=$("#begin_time_devoir_DFTE").val(); //任务结束时间
			var begin=$("#begin_time_DFTB").val();
			var end=$("#begin_time_DFTE").val(); 
			if(begin==''||begin==null){
				zinglabs.alert("请输入查询时间！");
				return;
			}else if(end==''||end==null){
				zinglabs.alert("请输入结束时间！");
				return;
			}else{
				if(timeCheck()){
					var info=doSearch();
					if(info!=''||info!=null){
					    var table=$('#wenbeninfo').DataTable();
					    table.clear().draw();
					    table.rows.add(info).draw();
				    }
			    }
		    }
	}
//开始时间和结束时间不能大于30天。
function timeCheck(){
	/*var bt=$("#begin_time_devoir_DFTB").val();
    var et=$("#begin_time_devoir_DFTE").val();
    bt=new Date(Date.parse(bt.replace(/-/g,"/")));
	et=new Date(Date.parse(et.replace(/-/g,"/")));
    if($("#begin_time_devoir_DFTB").val()==""||$("#begin_time_devoir_DFTE").val()==""){
    zinglabs.i18n.alert('ZQC_chaxunshijianbunengweikong');
    return false;
    }
    if(et.getTime()<bt.getTime()){
		zinglabs.i18n.alert('ZQC_jieshushijianbunengdayukaishihijian');
		return false;
	}
   if((U_DateDiffer(bt,et)/24)>30){
		zinglabs.i18n.alert("ZQC_chaxunshijanbunengdayusanshitian");
		return false;
	}else{
		return true;
	}*/
	var bt=$("#begin_time_DFTB").val();
    var et=$("#begin_time_DFTE").val();
    bt=new Date(Date.parse(bt.replace(/-/g,"/")));
	et=new Date(Date.parse(et.replace(/-/g,"/")));
    if($("#begin_time_DFTB").val()==""||$("#begin_time_DFTE").val()==""){
    zinglabs.i18n.alert('ZQC_chaxunshijianbunengweikong');
    return false;
    }
    if(et.getTime()<bt.getTime()){
		zinglabs.i18n.alert('ZQC_jieshushijianbunengdayukaishihijian');
		return false;
	}
   if((U_DateDiffer(bt,et)/24)>30){
		zinglabs.i18n.alert("ZQC_chaxunshijanbunengdayusanshitian");
		return false;
	}else{
		return true;
	}
	
}
			
				
	
						
//时间控件
function  checkTime1(){
	if($("#teach_set_timea").val()==''||$("#teach_set_timea").val()==null||$("#teach_set_timea").val()==undefined){
		CHECK_name =false ;
		showTooltip('time1','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('time1');
	}
}
function  checkTime2(){
	if($("#generate_timea").val()==''||$("#generate_timea").val()==null||$("#generate_timea").val()==undefined){
		CHECK_name =false ;
		showTooltip('time2','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('time2');
	}
}
function  checkTime3(){
	if($("#teach_set_timeu").val()==''||$("#teach_set_timeu").val()==null||$("#teach_set_timeu").val()==undefined){
		CHECK_name =false ;
		showTooltip('time3','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('time3');
	}
}
function  checkTime4(){
	if($("#generate_timeu").val()==''||$("#generate_timeu").val()==null||$("#generate_timeu").val()==undefined){
		CHECK_name =false ;
		showTooltip('time4','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('time4');
	}
}

//验证未通过样式
function showTooltip(id,title){
	$("#"+id).attr('title',title) ;
	$("#"+id).tooltip({
		placement:'left'
	}) ;
	$("#"+id).tooltip('show') ;
	$("#"+id).addClass('error') ;
	
}
//还原样式
function hideTooltip(id){
	$("#"+id).tooltip('destroy') ;
	$("#"+id).removeClass('error') ;
}

	

	//获取选中行
		function fnGetSelected(){
		    //注意DataTable大写 或者 $('#test').dataTable().api();	
		     var table=$('#wenbeninfo').DataTable();
		 //  var data =table.column().data();
		    //获取选中行 通过dataTable查找
		     var cell=table.rows(".active").data();
		    //通过table TableTools插件 查找
		     var tt = new $.fn.dataTable.TableTools(table);
		      var selectData=tt.fnGetSelectedData();
		   return selectData;
	}	
	
	
	//查看查看详情评分
	var close_dialog;
	function d(e){
			var file=$("#file_name").val();
			var grade_id=file.substring(0,file.lastIndexOf("."));
			var id=$("#wenbenid").val();
			var agent_user=$("#fenjihao").val();
			var grade_name=$("#pingfenzhibiao").val();
			var data_state=$("#shujuzhuangtai").val();
			var data=zkm_getHtmlFormJsons("updateForm");
			var file_location=$("#file_location").val();
			var tableSearch="tableSearch";
			var total=data.pfx_total;						//分数
			var owner=data.owner;								//质检员
			var file_name=data.file_name;						//文件名
			var generate_time_state_DFTB=data.generate_time_state;//评估开始时间
			var score_state=$("#updateData #score_state").val();
			var checkBtn=1;
			var params={
		    title: '查看文本评分',
		    width:'900',
		    height:'600',
		    content: '<iframe src='+e+'?grade_id='+grade_id+'&agent_user='+agent_user+'&closeFn='+tableSearch+'&file_location='+file_location+'&grade_name='+grade_name+'&checkBtn='+checkBtn+'&data_state='+data_state+'&score_state='+score_state+'&id='+id+'&total='+total+'&owner='+owner+'&file_name='+file_name+'&generate_time_state_DFTB='+generate_time_state_DFTB+'&score_state='+score_state+' style="border: 0; width:100%; height:100%"  frameborder="0"/>'
		   
		};
		close_dialog=zinglabs.dialog(params);	
		}
	//判定是否选中数据，查看各项评分
		function selectData(e){
			if(fnGetSelected().length>=2){
				zinglabs.i18n.alert("ZQC_ycznckytsj");
				return;
			}
			if(fnGetSelected().length==0){
				zinglabs.i18n.alert("zuoxichaxun_qingxuanzeyaochakandeshuju");
			}else{
				d(e);
			}
	}
	
	
	
	//查看文本内详情内容
function chaKanWenBenNeiRong(){
	if(fnGetSelected().length>=2){
		zinglabs.i18n.alert("ZQC_ycznckytsj");
		return;
	}
	if(fnGetSelected().length!=0){
		var file_location=$("#file_location").val();
		var file_name=$("#file_name").val();
		var file_address="http://"+file_location+":8111/ZDesk/ZIM/record_data/"+file_name.substring(file_name.lastIndexOf("_")+1,file_name.lastIndexOf("_")+9)+"/"+file_name;
		var params={
			title:"文本内容",
			width:"700",
			height:'500',
			content: '<iframe src='+file_address+' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>'
		};
		zinglabs.dialog(params);
	}else{
		zinglabs.i18n.alert("zuoxichaxun_qingxuanzeyaochakandeshuju");
	}
}
	//退回操作
	function  rollback_data_state(){
		//判断是否选中数据
		if(fnGetSelected().length==0){
				zinglabs.i18n.alert("ZQC_qingxuanzeyaotuihuideshuju");
		}else{
			var ss=0;
			for(var j=0;j<fnGetSelected().length;j++){
				if(fnGetSelected()[j].data_state==""+zinglabs.zqc.getState("zuoxishangsu")+""||fnGetSelected()[j].data_state==""+zinglabs.zqc.getState("zuoxishangsuchenggong")+""||fnGetSelected()[j].data_state==""+zinglabs.zqc.getState("zuoxishangsushibai")+""){
					ss+=1;
				}
			}
			//判断是否有上诉数据
			if(ss>0){
				zinglabs.i18n.alert("ZQC_tuihuishujucunzaishangsushuju");
			}else{
				var pat={};
				var id=$("#wenbenid").val();
				var rdata;
				var json;
				pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
				pat.primaryKey = "id";
				for(var i=0;i<fnGetSelected().length;i++){
					pat.data_state=""+zinglabs.zqc.getState("chujianyifenpei")+"";
					pat.score_state=""+zinglabs.zqc.getState("chujianweipingfen")+"";
					pat.primaryKeyValue = fnGetSelected()[i].id;
					pat.pfx_total=0;
					var file_name='';
					var grade_id='';
					file_name=fnGetSelected()[i].file_name;
					grade_id=file_name.substring(0,file_name.lastIndexOf(".html"));;
					commonUpdate(pat,function(data){
					 json=eval('('+data+')').success; 
						if(json){
							delete_chujian(grade_id);
							if(i==fnGetSelected().length){
								zinglabs.i18n.alert("ZQC_tuihuichenggong");	
								tableSearch();	
							}
					    }
					});
				}
			}
		}
	}
	
		//删除初检表数据
	function delete_chujian(id){
		var pat={};
		pat.tableName="T_GRADE_DETAIL_NEW";
		pat.primaryKey = "id";
		pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
		pat.nameSpaceId="T_GRADE_DETAIL_NEW_delete";
		pat.grade_id = id;
		commonCRUDForComplex(pat,function(data){
		});
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
	$("#tianJiaJiaoCaiDiv").slideDown(300);
	$("#btn_tianjiajiaocai").hide();
	$("#btn_tijiaojiaocai").show();
}
	
	//添加教材
function tianJiaJiaoCai(){
	var table = $("#wenbeninfo").DataTable();
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
		file_name=d.file_name.substring(0,d.file_name.indexOf(".html"));
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
		if(d.data_state!=zinglabs.zqc.getState("chujianyifabu")){
			yysj='cjwfb';
		}
	});
	if(yysj=='cjwfb'){
		//alert("数据中存在初检为评分数据!");
		zinglabs.i18n.alert("zqc_share_cjwpfsj");
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
		file_name=d.file_name.substring(0,d.file_name.indexOf(".html"));
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
	
	
	
	