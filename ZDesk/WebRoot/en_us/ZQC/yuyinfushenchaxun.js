$(document).ready(function(){
		initShuJuZhuangTai("wenbenfushenchaxun","score_state");
	})


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
					pat.tableName = "SA_AGENT_ARGUMENT";
					pat.primaryKey = "id";
					pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
					pat.nameSpaceId="SA_AGENT_ARGUMENT_select";
					//根据分机号查询
					if(agent_userNum().length!=0){
						pat.agent_user= agent_userNum()[0].phone_number;
					}
					pat.ling=zinglabs.zqc.getState("zero");
					pat.wenjian='%.mp3';
					
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
			   
			   
			   function lookInfo(){
				   var file=$("#file_name").val();
				   var page=file.substring(file.lastIndexOf("_")+1,file.lastIndexOf("_")+9);
				   var path=page+'/'+file;
				   	window.open("http://127.0.0.1/ZIM/ZQCRecord/"+path,"ZIMHistory","width=800,height=600,scrollbars=yes,resizable=yes");
			   }
			
			
			//按评条件筛选
				function tableSearch(){
					$("#updateFormDiv").slideUp(300);
						var begin=$("#agent_user_dt_DFTB").val();
					var end=$("#agent_user_dt_DFTE").val();
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
								  //  $("#where input[name],textarea[name]").val("");
							}
					   }
				}
				
//开始时间和结束时间不能大于30天。
function timeCheck(){
	var bt=$("#agent_user_dt_DFTB").val();
    var et=$("#agent_user_dt_DFTE").val();
    bt=new Date(Date.parse(bt.replace(/-/g,"/")));
	et=new Date(Date.parse(et.replace(/-/g,"/")));
    if($("#agent_user_dt_DFTB").val()==""||$("#agent_user_dt_DFTE").val()==""){
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
				//根据当前分机号号
				function agent_userNum() {
					var rdata;
					var pat = zkm_getHtmlFormJsons('searchItems');
					pat.tableName = "SA_AGENT_ARGUMENT";
					pat.primaryKey = "id";
					pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
					pat.nameSpaceId="SU_QC_SOURCE_RECORD_DATA_select_text";
					//根据分机号查询
					pat.alias1=getUserInfo().loginName;
					pat.is_mp3='mp3';
			//		pat.where = appendSearchItem('searchItems');
					commonSelectForComplex(pat,false,function(data){			
					if(data.rows!= null){				
					  rdata=data.rows;
			}			
		});
		return rdata;
	}


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

	
	
		//根据评分流水号查询grade_index
	function getGrade_index(){
		var rdata;
		var pat = zkm_getHtmlFormJsons('searchItems');
		pat.tableName = "T_GRADE_DETAIL_FUSHEN_NEW";
		pat.primaryKey = "id";
		pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
		pat.nameSpaceId="T_GRADE_DETAIL_NEW_score";
		pat.grade_id=$("#pingfenliushuihao").val();
		commonSelectForComplex(pat,false,function(data){			
		if(data.rows!= null){				
		  rdata=data.rows;
			}			
		});
		return rdata;
	}
	//获取评分指标
	function getGrade_name(){
		var rdata;
		var pat = zkm_getHtmlFormJsons('searchItems');
		pat.tableName = "T_GRADE_DETAIL_FUSHEN_NEW";
		pat.primaryKey = "id";
		pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
		pat.nameSpaceId="getGrade_name_select";
		if(getGrade_index().length!=0){
			pat.id=getGrade_index()[0].use_grade_index;
		}
		commonSelectForComplex(pat,false,function(data){			
		if(data.rows!= null){				
		  rdata=data.rows;
			}			
		});
		return rdata;
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
	function d(e){
			var file=$(".file_name").val();
			var grade_id=$(".grade_id").val();
			var score_state=$("#updateFormDiv #score_state").val();
			var checkBtn=1;
			if(getGrade_name().length!=0){
				var grade_name=getGrade_name()[0].grade_name;
			}
			
			var params={
		    title: '语音文本评分',
		    width:'900',
		    height:'600',
		    onclose: function () {
	        	$("#iframe1").remove();
	        	tableSearch();
    		},
		    content: '<iframe src='+e+'?grade_id='+grade_id+'&grade_name='+grade_name+'&score_state='+score_state+'&checkBtn='+checkBtn+' style="border: 0; width: 100%; height: 100%" id="iframe1"  frameborder="0"/>'
		   
		    
		};
		zinglabs.dialog(params);
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
	