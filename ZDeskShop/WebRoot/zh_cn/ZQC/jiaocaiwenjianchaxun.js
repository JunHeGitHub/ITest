var ZQC_GOLBAL_PARAM={};
ZQC_GOLBAL_PARAM["wulizuData"]={};
ZQC_GOLBAL_PARAM["zuoxigonghaoData"]={};
//@@查询教材目录名---写入select里面
		function doSearch_jiaocaimuluming(){
		var pat={};				   
		pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	pat.nameSpaceId = 'getJiaocaimuluming';
    	commonSelectForComplex(pat, false,
    	function(data) {
     	var data_all=data.rows;
    	var select1 = $(".teach_name"); 
      	select1.empty();//清空select下拉框
     	$("<option value='' select='selected'>"+''+"</option>").appendTo(select1)
    	for(var i=0;i<data_all.length;i++){
    	if(data_all[i]!='null'&&data_all[i]!=null&&data_all[i]!=undefined){
    	$("<option value='" + data_all[i].teach_dir_description + "'>" + data_all[i].teach_dir_description + "</option>").appendTo(select1)//动态添加Option子项
    	}
    	} 
    });				   		
	}	
			function doSearch_wulizu(){
		var pat={};				   
		pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	pat.nameSpaceId = 'getWulizu';
    	commonSelectForComplex(pat, false,
    	function(data) {
     	var data_all=data.rows;
    	var select1 = $(".alias2"); 
      	select1.empty();//清空select下拉框
     	$("<option value='' select='selected'>"+''+"</option>").appendTo(select1)
    	for(var i=0;i<data_all.length;i++){
    	if(data_all[i]!='null'&&data_all[i]!=null&&data_all[i]!=undefined){
    	        $("<option value='" + data_all[i].business_type + "'>" + data_all[i].business_type + "</option>").appendTo(select1)//动态添加Option子项
    	}
    	} 
    });				   		
	}	
	//@@查询坐席工号---写入select里面	
	function doSearch_zuoxigonghao(){
		var pat={};				   
		pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	pat.nameSpaceId = 'getZuoxigonghao';
    	commonSelectForComplex(pat, false,
    	function(data) {
     	var data_all=data.rows;
    	var select1 = $(".alias1"); 
      	select1.empty();//清空select下拉框
     	$("<option value='' select='selected'>"+''+"</option>").appendTo(select1)
    	for(var i=0;i<data_all.length;i++){
    	if(data_all[i]!='null'&&data_all[i]!=null&&data_all[i]!=undefined){
        $("<option value='" + data_all[i].alias1 + "'>" + data_all[i].alias1 + "</option>").appendTo(select1)//动态添加Option子项
        }
    	} 
    });				   		
	}
	
	//添加培训判断是否选中数据
	function addPeixun(){
		if(data_==null||data_==''){
			zinglabs.i18n.alert('ZJGL_xuanzeshuju');
			return;
		}
		 $("#updateFormDiv").slideUp(300);
		 $("#addFormDiv").slideUp(300);
		 $("#addPeixunDiv").slideDown(300);
		 scroll2data("addPeixunDiv");
		 var user=getUserInfo();
			$("#grade_ida_p").val(data_.grade_id);
			$("#file_namea_p").val(data_.file_name);
			$("#import_usera_p").val(user.loginName);
			$("#phone_numbera_p").val(user.phone_number);
			$("#file_locationa_p").val(data_.file_location);
			$("#remarka_p").val('');
	        $("#wulizuList").val('');
			$("#zuoxigonghaoList").val('');
		}
	
	
	//添加培训
	function doAddPeiXun(){
	if($("#grade_ida_p").val()==''||$("#grade_ida_p").val()==null||$("#grade_ida_p").val()==undefined||$("#generate_timea_p").val()==''
			||$("#generate_timea_p").val()==null||
			$("#generate_timea_p").val()==undefined||$("#teach_set_timea_p").val()==''||$("#teach_set_timea_p").val()==null||$("#teach_set_timea_p").val()==undefined){
		       //alert('请填写必填项');
		       zinglabs.i18n.alert('ZJGL_tianxiebitianxiang'); 
		       return;
			}
			
			if($("#remarka_p").val().length>=200){
			 zinglabs.i18n.alert('ZQC_zifuchaochang'); 
		       return;
			}
				var pat = zkm_getHtmlFormJsons("addPeixun");
				var pat2=zkm_getHtmlFormJsons("addPeixun");
				pat.tableName = "AGENT_TEACH_QUERY";
				pat.remark=$("#remarka_p").val();
				pat.alias1='';
				pat.alias2='';			
				commonInsert(pat, function(data) {
						var json = eval('(' + data + ')');
						if (json.success == true) {
							//alert('添加成功');
							zinglabs.i18n.alert('system_saveSuccess'); 
							var wulizuList = $("#wulizuList").val();
							var zuoxigonghaoList = $("#zuoxigonghaoList").val();
							if (wulizuList != undefined
									&& wulizuList != ''
									) {
								var pat3 = {};
								pat3.tableName = "AGENT_TEACH_QUERY_ZU_GONGHAO";
								pat3.grade_id =pat2.grade_id;
								var array = wulizuList.split(",");
								for (var i = 0; i < array.length; i++) {
									pat3.wulizu = array[i];
									commonInsert(pat3);
								}
							}
							if (zuoxigonghaoList != undefined
									&& zuoxigonghaoList != ''
									) {
								var pat4 = {};
								pat4.tableName = "AGENT_TEACH_QUERY_ZU_GONGHAO";
								pat4.grade_id =pat2.grade_id;
								var array = zuoxigonghaoList.split(",");
								for (var i = 0; i < array.length; i++) {
									pat4.zuoxigonghao = array[i];
									commonInsert(pat4);
								}
							}
							$("#grade_ida_p").val('');
					    	$("#import_usera_p").val('');
					    	$("#phone_numbera_p").val('');
					    	$("#teach_set_timea_p").val('');
					    	$("#generate_timea_p").val('');
					    	$("#file_namea_p").val('');
					    	$("#remarka_p").val('');
					    	$("#file_locationa_p").val('');  	
					    	$("#wulizuList").val('');
							$("#zuoxigonghaoList").val('');
						} else {
							//alert('添加失败');
							zinglabs.i18n.alert('system_saveFailed');
							return;
						}
					});
	
	}						
			//添加国际化提示信息函数
			function add(){	
			             $("#alias1a").val('');
			             $("#alias2a").val('');
			            $("#addFormDiv #import_usera").val(getUserInfo().loginName);
						$("#updateFormDiv").slideUp(300);
			            scroll2data("addFormDiv");									
						$("#addFormDiv").slideDown(300);
						$("#addPeixunDiv").slideUp(300);											
			}
			function initForm1(data){
				var cfs=$("#updateForm input[name],textarea[name],select[name]");
				for(var i=0;i<cfs.length;i++){
				var e=cfs[i];					
				var v=data[e.name];
				if(v==undefined || v==null){
						v='';
				}
				$(e).val(v);						
				}			 
		  }
			
			//通用修改@@
			function doUpdate(){
			if($("#id").val()==''||$("#id").val()==null){
			//alert('无id值，操作失败');
			zinglabs.i18n.alert('ZJGL_wuid');
			return;
			}
			var pat = zkm_getHtmlFormJsons('updateForm');
			pat.tableName = "T_TEACH_DETAIL_NEW";
			pat.primaryKey = "id";
			pat.primaryKeyValue = $("#id").val();
		    commonUpdate(pat,function(data){
		    var json=eval('('+data+')');
				     if(json.success){				        
				        //alert('修改成功');
				        zinglabs.i18n.alert('system_updateSuccess');
				          doRefresh();
				            $("#id").val('');
				          	$("#grade_idu").val('');
					    	$("#import_useru").val('');
					    	$("#phone_numberu").val('');
					    	$("#teach_set_timeu").val('');
					    	$("#remarku").val('');
					    	$("#file_nameu").val('');
					    	$("#file_locationu").val('');
					    	$("#teach_nameu").val('');
				      }else{
				        //alert('修改失败');
				         zinglabs.i18n.alert('system_updateFailed');
				       }
		     });
			}
			//@@
			function refresh(){		 
		  	var table=$('#zuoxipeixun').DataTable();
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
				   var pat={};
					pat.tableName = "T_TEACH_DETAIL_NEW";
					pat.primaryKey = "id";		
					commonSelect(pat,false,function(data){					
					if(data.rows!= null){				
					  rdata=data.rows;				 	
			}			
		});
		return rdata;
	}
	//@@条件查询
	function doSearch1(){
		$("#updateFormDiv").hide();
	    var bt=$("#teach_set_time_start").val();
	    var et=$("#teach_set_time_end").val();
	    bt=new Date(Date.parse(bt.replace(/-/g,"/")));
		et=new Date(Date.parse(et.replace(/-/g,"/")));
	    if($("#teach_set_time_start").val()==""||$("#teach_set_time_end").val()==""){
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
		}
	    var rdata=[];
		var pat={};				   
		pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	pat.nameSpaceId = 'getTiaojianChaxun_jiaocaimuluchaxun';
    	pat.grade_id=$("#grade_id").val();
    	pat.import_user=$("#import_user").val();
    	pat.phone_number=$("#phone_number").val();
    	pat.teach_set_time_start=$("#teach_set_time_start").val();
    	pat.teach_set_time_end=$("#teach_set_time_end").val();
    	pat.teach_name=$("#teach_name").val();
    	pat.remark=$("#remark").val();
    	pat.file_name=$("#file_name").val();
    	pat.file_location=$("#file_location").val();   	
    	commonSelectForComplex(pat, false,
    	function(data) {
     	if (data) {
		              rdata=data.rows;
		              var table=$('#zuoxipeixun').DataTable();
			   		  table.clear().draw();
			   		  table.rows.add(rdata).draw();	
					}
    }); 
      	//$("#grade_id").val('');
    	//$("#import_user").val('');
    	//$("#phone_number").val('');
    	//$("#teach_set_time_start").val('');
    	//$("#teach_set_time_end").val('');
    	//$("#teach_name").val('');
    	//$("#remark").val('');
    	//$("#file_name").val('');
    	//$("#file_location").val('');   	
    
     return rdata;			
	}
	function dochongzhi(){
		$("#grade_id").val('');
    	$("#import_user").val('');
    	$("#phone_number").val('');
    	$("#teach_set_time_start").val('');
    	$("#teach_set_time_end").val('');
    	$("#teach_name").val('');
    	$("#remark").val('');
    	$("#file_name").val('');
    	$("#file_location").val('');  
	}
	//拼接筛选项
	function appendSearchItem(formName){
		var pat = {};
		var temp = "{";
		var cfs=$("#"+formName+" input[name]");
		    for(var i=0;i<cfs.length;i++){
		        var e=cfs[i];
		        if(e.value != '' && e.value !=null && e.value != undefined){
		        	temp += "'"+e.name+"':'"+e.value+"',";
		        }
		    }
		temp = temp.substring(0,temp.length-1)+"}";
		if(temp.length != 1 && temp != ''){
			pat = eval("("+temp+")");
		}		
		return pat;
	}			
			//通用删除@@
			function doDelete(){
				var table=$('#zuoxipeixun').DataTable();
				//var cell=table.rows(".active").data();				
				var tt = new $.fn.dataTable.TableTools(table);
			    var selectData=tt.fnGetSelectedData();
			   
			    if(selectData.length==0 || selectData==null || selectData==undefined){
			    	//alert("请选中1条数据!");			  
			    	zinglabs.i18n.alert('system_selectDataForDelete');  	
			    	return;
			    }
			    zinglabs.confirm(zinglabs.i18n.getText("system_deleteConfirmation"),function (){
			    for(var i=0;i<selectData.length;i++){
			    var pat = {};
                pat.tableName="T_TEACH_DETAIL_NEW";
                pat.primaryKey="id";
                pat.primaryKeyValue =selectData[i].id;
                 //alert(JSON.stringify(pat));
                commonDelete(pat,function(data){
                	var json=eval('('+data+')');
                	if(json.success){		            	 
		            	 		            	 
		            }
		            else{
		             	alert(json.mgs);
		            }                	              
                });
			    
			    }
			    //alert('删除成功');
			    zinglabs.i18n.alert('system_deleteSuccess'); 
			     doRefresh();
			     $("#id").val('');
				          	$("#grade_idu").val('');
					    	$("#import_useru").val('');
					    	$("#phone_numberu").val('');
					    	$("#teach_set_timeu").val('');
					    	$("#remarku").val('');
					    	$("#file_nameu").val('');
					    	$("#file_locationu").val('');
					    	$("#teach_nameu").val(''); 
			    });    				   				     
				
			      		    
			}						
			//刷新
			function refresh(){
			     var table=$('#zuoxipeixun').DataTable();
			     
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
			  var table=$('#zuoxipeixun').DataTable();
			   if(obj.value!= undefined){
			  	table.page.len(obj.value).draw();
			  }
			  //alert(obj.value);
			}
			//全选
			 function toggleChecks(obj){
			 	var table=$('#zuoxipeixun').DataTable();
			    var tableTools = new $.fn.dataTable.TableTools(table);
			       
			    var ischeck=$("#selectall").is(':checked');
			       if(ischeck){
			         tableTools.fnSelectAll();
			       }else{
			         tableTools.fnSelectNone();
			       }			      
			   }			
			//通用添加@@
			function doInsert(){
			if($("#grade_ida").val()==''||$("#grade_ida").val()==null||$("#grade_ida").val()==undefined||$("#teach_set_timea").val()==''
			||$("#teach_set_timea").val()==null||
			$("#teach_set_timea").val()==undefined){
		       //alert('请填写必填项');
		       zinglabs.i18n.alert('ZJGL_tianxiebitianxiang'); 
		       return;
			}
				var pat = zkm_getHtmlFormJsons("addForm");
				pat.tableName = "T_TEACH_DETAIL_NEW";
				//alert(JSON.stringify(pat));				
				commonInsert(pat, function(data) {
						var json = eval('(' + data + ')');
						if (json.success == true) {
							//alert('添加成功');
							zinglabs.i18n.alert('system_saveSuccess'); 
							doRefresh();
							$("#grade_ida").val('');
					    	$("#import_usera").val('');
					    	$("#phone_numbera").val('');
					    	$("#teach_set_timea").val('');
					    	$("#teach_namea").val('');
					    	$("#remarka").val('');
					    	$("#file_namea").val('');
					    	$("#file_locationa").val('');  														
							return;
						} else {
							//alert('添加失败');
							zinglabs.i18n.alert('system_saveFailed');
							return;
						}
					});
			}
			
			
			
			//-----------------------------------
			function  checkText(){
	if($("#grade_ida").val()==''||$("#grade_ida").val()==null||$("#grade_ida").val()==undefined){
		CHECK_name =false ;
		showTooltip('text-Div-a','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('text-Div-a');
	}
}
function  checkText5(){
	if($("#grade_ida_p").val()==''||$("#grade_ida_p").val()==null||$("#grade_ida_p").val()==undefined){
		CHECK_name =false ;
		showTooltip('text-Div','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('text-Div');
	}
}
			function  checkText1(){
	if($("#grade_idu").val()==''||$("#grade_idu").val()==null||$("#grade_idu").val()==undefined){
		CHECK_name =false ;
		showTooltip('text-Div-u','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('text-Div-u');
	}
}
function  checkTime1(){
	if($("#teach_set_timeu").val()==''||$("#teach_set_timeu").val()==null||$("#teach_set_timeu").val()==undefined){
		CHECK_name =false ;
		showTooltip('time1','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('time1');
	}
}

function  checkTime3(){
	if($("#teach_set_timea").val()==''||$("#teach_set_timea").val()==null||$("#teach_set_timea").val()==undefined){
		CHECK_name =false ;
		showTooltip('time3','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('time3');
	}
}
function  checkTime5(){
	if($("#generate_timea_p").val()==''||$("#generate_timea_p").val()==null||$("#generate_timea_p").val()==undefined){
		CHECK_name =false ;
		showTooltip('time5','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('time5');
	}
}
function  checkTime6(){
	if($("#teach_set_timea_p").val()==''||$("#teach_set_timea_p").val()==null||$("#teach_set_timea_p").val()==undefined){
		CHECK_name =false ;
		showTooltip('time6','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('time6');
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
function choseWulizu(){
	var params={
		title:"物理组列表",
		url:"/"+window.top.PRJ_PATH+"/zh_cn/ZQC/wulizuliebiao.html",
		width:"900",
		height:"450",
		ok: function(){
			var data=ZQC_GOLBAL_PARAM["wulizuData"];
			if(data.length == undefined || data.length == 0 ){
				zinglabs.i18n.alert("system_selectDataForDelete");
				return false;
			}
			var us="";
			for(var i=0;i<data.length;i++){
				us+=data[i].business_type+",";
			}
			us=us.substring(0,us.length-1);
			$("#wulizuList").val(us);
			
		},
		okValue:" 确 定 ",
		cancel:function(){},
		cancelValue:" 关 闭 "
	};
	zinglabs.dialog(params);
}		
function chosezuoxigonghao(){
	var params={
		title:"坐席工号列表",
		url:"/"+window.top.PRJ_PATH+"/zh_cn/ZQC/zuoxigonghaoliebiao.html",
		width:"900",
		height:"450",
		ok: function(){
			var data=ZQC_GOLBAL_PARAM["zuoxigonghaoData"];
			if(data.length == undefined || data.length == 0 ){
				zinglabs.i18n.alert("system_selectDataForDelete");
				return false;
			}
			var us="";
			for(var i=0;i<data.length;i++){
				us+=data[i].alias1+",";
			}
			us=us.substring(0,us.length-1);
			$("#zuoxigonghaoList").val(us);
			
		},
		okValue:" 确 定 ",
		cancel:function(){},
		cancelValue:" 关 闭 "
	};
	zinglabs.dialog(params);
}	

//查看查看详情评分
	var zuoxishangsu_close;
	function d(){
			if(data_==null||data_==''){
			zinglabs.i18n.alert('ZJGL_xuanzeshuju');
			return;
			}
			var e;
			var file=$("#file_nameu").val();
			var is_mp3_html=file.substring(file.lastIndexOf("."),file.length);
			if(is_mp3_html=='.html'){
				e='theScore.html';
			}else if(is_mp3_html=='.mp3'){
				e='theScoreMP3.html';
			}else{
				alert("文件格式不匹配！");
			}
			if(getState(file).length==0){
				alert("数据错误！");
			}else{
				var score_state=getState(file)[0].score_state;
				var data_state=getState(file)[0].data_state;
				var grade_name=getState(file)[0].grade_name;
				var file_location=getState(file)[0].file_location;
			}
			var checkBtn='jiaocai';
			var grade_id=file.substring(0,file.lastIndexOf("."));
			var tableSearch="doRefresh";
			var params={
		    title: '查看文本评分',
		    width:'900',
		    height:'600',
		    content: '<iframe src='+e+'?grade_id='+grade_id+'&closeFn='+tableSearch+'&file_name='+file+'&data_state='+data_state+'&score_state='+score_state+'&grade_name='+grade_name+'&checkBtn='+checkBtn+'&file_location='+file_location+' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>'
		};
		zuoxishangsu_close=zinglabs.dialog(params);
			
		}
		
	//根据文件名查询各项评分
	function getState(file){
			var rdata;
			var pat = {};;
			pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
			pat.primaryKey = "id";
			pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
			pat.nameSpaceId="getSU_QC_SOURCE_RECORD_DATA_state";
	//		pat.where = appendSearchItem('searchItems');
			pat.file_name=file;
			commonSelectForComplex(pat,false,function(data){			
			if(data.rows!= null){				
			  rdata=data.rows;				 	
				}			
			});
			return rdata;
	}
		
	//@@条件查询
	function doRefresh(){
	    var bt=$("#teach_set_time_start").val();
	    var et=$("#teach_set_time_end").val();
	    bt=new Date(Date.parse(bt.replace(/-/g,"/")));
		et=new Date(Date.parse(et.replace(/-/g,"/")));
	    var rdata=[];
		var pat={};				   
		pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	pat.nameSpaceId = 'getTiaojianChaxun_jiaocaimuluchaxun';
    	pat.grade_id=$("#grade_id").val();
    	pat.import_user=$("#import_user").val();
    	pat.phone_number=$("#phone_number").val();
    	pat.teach_set_time_start=$("#teach_set_time_start").val();
    	pat.teach_set_time_end=$("#teach_set_time_end").val();
    	pat.teach_name=$("#teach_name").val();
    	pat.remark=$("#remark").val();
    	pat.file_name=$("#file_name").val();
    	pat.file_location=$("#file_location").val();   	
    	commonSelectForComplex(pat, false,
    	function(data) {
     	if (data) {
		              rdata=data.rows;
		              var table=$('#zuoxipeixun').DataTable();
			   		  table.clear().draw();
			   		  table.rows.add(rdata).draw();	
					}
    }); 
      	$("#grade_id").val('');
    	$("#import_user").val('');
    	$("#phone_number").val('');
    	//$("#teach_set_time_start").val('');
    	//$("#teach_set_time_end").val('');
    	$("#teach_name").val('');
    	$("#remark").val('');
    	$("#file_name").val('');
    	$("#file_location").val('');   
     return rdata;			
	}