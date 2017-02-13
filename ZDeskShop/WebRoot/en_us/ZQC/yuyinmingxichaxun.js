$(document).ready(function(){
		initShuJuZhuangTai("wenbenmingxichaxun","data_state");
		initPingFenZhuangTai("wenbenmingxichaxun","score_state");
		initZhiBiao('grade_name');
		load_business_type("business_type");
		//加载客户满意度
		initClient_satisfy("client_satisfy");
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
		  	var table=$('#yuyintable').DataTable();
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
				   	pat = zkm_getHtmlFormJsons('searchDataForm');
					pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
					pat.primaryKey = "id";
					pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
					pat.nameSpaceId="SU_QC_SOURCE_RECORD_DATA_select_text";
					pat.is_mp3='mp3';
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
			     var table=$('#yuyintable').DataTable();
			     
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
			  var table=$('#yuyintable').DataTable();
			   if(obj.value!= undefined){
			  	table.page.len(obj.value).draw();
			  }
			  //alert(obj.value);
			}
			//全选
			 function toggleChecks(obj){
			 	var table=$('#yuyintable').DataTable();
			    var tableTools = new $.fn.dataTable.TableTools(table);
			       
			    var ischeck=$("#selectall").is(':checked');
			       if(ischeck){
			         tableTools.fnSelectAll();
			       }else{
			         tableTools.fnSelectNone();
			       }			      
			   }	
			   
			   
	
			
			
//筛选数据
function tableSearch(){
			$("#updateFormDiv").hide();	
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
				    var table=$('#yuyintable').DataTable();
				    table.clear().draw();
				    table.rows.add(info).draw();
			    }
			  //  $("#where input[name],textarea[name]").val("");
			}
	   }
}
				
//开始时间和结束时间不能大于30天。
function timeCheck(){
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

	
	
	
	
	function loadComboBox(){
		$("#score_state_com").append("<option value=''>--请选择--</option>");
		$("#score_state_com").append("<option value="+zinglabs.zqc.getState("zero")+">"+zinglabs.zqc.getState("zero")+"</option>");
		$("#score_state_com").append("<option value="+zinglabs.zqc.getState("chujianweipingfen")+">"+zinglabs.zqc.getState("chujianweipingfen")+"</option>");
		$("#score_state_com").append("<option value="+zinglabs.zqc.getState("chujianyifenpei")+">"+zinglabs.zqc.getState("chujianyifenpei")+"</option>");

	}
	
	
	//按钮判断如果是已上诉的数据按钮隐藏，不能上诉
	function checkBotton(){
		if($("#score_state").val()=='已上诉'){
			$("#btn_jisuan").hide();
		}
		if($("#score_state").val()!='已上诉'){
			$("#btn_jisuan").show();
		}
	}
	
	
	//获取选中行
		function fnGetSelected(){
		    //注意DataTable大写 或者 $('#test').dataTable().api();	
		     var table=$('#yuyintable').DataTable();
		 //  var data =table.column().data();
		    //获取选中行 通过dataTable查找
		     var cell=table.rows(".active").data();
		    //通过table TableTools插件 查找
		     var tt = new $.fn.dataTable.TableTools(table);
		      var selectData=tt.fnGetSelectedData();
		   return selectData;
	}	
	
	
	//查看语音内容
function chaKanYuYinNeiRong(){
	if(fnGetSelected().length>=2){
		zinglabs.i18n.alert("ZQC_ycznckytsj");
		return;
	}
	if(fnGetSelected().length!=0){
		var file_location=$("#file_location").val();
		var file_name=$("#file_name").val();
		var params={
			title:"语音内容",
			width:"700",
			height:'500',
			onclose: function () {
        	$("#iframe1").remove();
        	tableSearch();
    		},
			content: '<iframe src="lookyuyinneirong.html?file_name='+file_name+'" style="border: 0; width: 100%; height: 100%" id="iframe1"  frameborder="0"/>'
		};
		zinglabs.dialog(params);
	}else{
		zinglabs.i18n.alert("zuoxichaxun_qingxuanzeyaochakandeshuju");
	}
}
	