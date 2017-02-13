$(document).ready(function(){
		initShuJuZhuangTai("wenbenpingfenchaxun","data_state");
		initZhiBiao('grade_name');
		load_business_type("business_type");
		
	});


function initForm(data){
				var cfs=$("#updateData input[name],textarea[name]");
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
						//根据当前用户分机号
						pat.phone_number=getUserInfo().phone_number;
						//当前登录用户
						//pat.alias1=getUserInfo().loginName;
						pat.is_mp3='txt';
						pat.ling=zinglabs.zqc.getState("zero");
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
			  if(obj.value!=undefined){
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
			   
			   
	
			
			
//筛选数据
function tableSearch(){
		$("#updateFormDiv").slideUp(300);
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
	var zuoxishangsu_close;
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
			var score_state=$("#score_state").val();
			var tableSearch="tableSearch";
			var params={
		    title: '查看文本评分',
		    width:'900',
		    height:'600',
		    content: '<iframe src='+e+'?grade_id='+grade_id+'&closeFn='+tableSearch+'&agent_user='+agent_user+'&file_location='+file_location+'&closeFn='+tableSearch+'&grade_name='+grade_name+'&data_state='+data_state+'&score_state='+score_state+'&id='+id+'&total='+total+'&owner='+owner+'&file_name='+file_name+'&generate_time_state_DFTB='+generate_time_state_DFTB+'&score_state='+score_state+' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>'
		   
		    
		};
		zuoxishangsu_close=zuoxishangsu_close=zinglabs.dialog(params);
			
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
	
	

	
