					
			//添加国际化提示信息函数
			function add(){	
			             $("#alias1a").val('');
			             $("#alias2a").val('');
						$("#updateFormDiv").addClass('hide');
			            scroll2data("addFormDiv");									
						$("#addFormDiv").removeClass('hide');
																
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
				if($("#grade_idu").val()==''||$("#grade_idu").val()==null||$("#grade_idu").val()==undefined||$("#teach_set_timeu").val()==''
			||$("#teach_set_timeu").val()==null||
			$("#teach_set_timeu").val()==undefined||$("#generate_timeu").val()==''||$("#generate_timeu").val()==null||$("#generate_timeu").val()==undefined){
		       //alert('请填写必填项');
		       zinglabs.i18n.alert('ZJGL_tianxiebitianxiang'); 
		       return;
			}
			if($("#remarku").val().length>=200){
			 zinglabs.i18n.alert('ZQC_zifuchaochang'); 
		       return;
			}
			var pat = zkm_getHtmlFormJsons('updateForm');
			pat.tableName = "AGENT_TEACH_QUERY";
			pat.primaryKey = "id";
			pat.primaryKeyValue = $("#id").val();
		    commonUpdate(pat,function(data){
		    var json=eval('('+data+')');
				     if(json.success){				        
				        //alert('修改成功');
				        zinglabs.i18n.alert('system_updateSuccess');
				        $("#grade_idu").val('');
				        $("#id").val('');
				    	$("#import_useru").val('');
				    	$("#phone_numberu").val('');
				    	$("#teach_set_timeu").val('');
				    	$("#generate_timeu").val('');
				    	$("#file_nameu").val('');
				    	$("#file_locationu").val('');
				    	$("#remarku").val('');
				          refresh();
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
					pat.tableName = "AGENT_TEACH_QUERY";
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
    	pat.nameSpaceId = 'getTiaojianChaxun';
    	//pat.grade_id=$("#grade_id").val();
    	pat.import_user=$("#import_user").val();
    	pat.phone_number=$("#phone_number").val();
    	pat.teach_set_time_start=$("#teach_set_time_start").val();
    	pat.teach_set_time_end=$("#teach_set_time_end").val();
    	pat.file_name=$("#file_name").val();
    	pat.generate_time_start=$("#generate_time_start").val();
    	pat.generate_time_end=$("#generate_time_end").val();
    	var user=getUserInfo();
    	pat.gonghao=user.job_number;
    	var s="";
    	var array = user.orgs;
    	for (var i = 0; i < array.length; i++) {
		s+=array[i]+',';
		}
		s=s.substring(0,s.lastIndexOf(","));
    	pat.zuzhi=s;
    	commonSelectForComplex(pat, false,
    	function(data) {
     	if (data) {
		              rdata=data.rows;
		              var table=$('#zuoxipeixun').DataTable();
			   		  table.clear().draw();
			   		  table.rows.add(rdata).draw();	
					}
    }); 
      	/*
      	$("#grade_id").val('');
    	$("#import_user").val('');
    	$("#phone_number").val('');
    	$("#teach_set_time_start").val('');
    	$("#teach_set_time_end").val('');
    	$("#file_name").val('');
    	$("#generate_time_start").val('');
    	$("#generate_time_end").val('');
    	$("#alias1").val('');   	
    */
     return rdata;			
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
			   
			    if(selectData.length==0 || selectData==null || selectData==undefined||selectData.length>1){
			    	//alert("请选中1条数据!");	
			    	zinglabs.i18n.alert('system_selectDataForDelete');		    	
			    	return;
			    }
			    zinglabs.confirm(zinglabs.i18n.getText("system_deleteConfirmation"),function (){      				   				     
				var pat = {};
                pat.tableName="SU_QC_SOURCE_RECORD_DATA";
                pat.primaryKey="id";
                pat.primaryKeyValue =selectData[0].id;
                 //alert(JSON.stringify(pat));
                commonDelete(pat,function(data){
                	var json=eval('('+data+')');
                	if(json.success){		            	 
		            	 doSearch1();		            	 
		            }
		            else{
		             	alert(json.mgs);
		            }                	              
                });
			    });
			      		    
			}						
			//刷新
			function refresh(){
			     var table=$('#zuoxipeixun').DataTable();
			     
			     //清空筛选
			   //  table.search("").draw();
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
			$("#teach_set_timea").val()==undefined||$("#generate_timea").val()==''||$("#generate_timea").val()==null||$("#generate_timea").val()==undefined){
		       //alert('请填写必填项');
		       zinglabs.i18n.alert('ZJGL_tianxiebitianxiang');
		       return;
			}
				var pat = zkm_getHtmlFormJsons("addForm");
				pat.tableName = "AGENT_TEACH_QUERY";
				//alert(JSON.stringify(pat));				
				commonInsert(pat, function(data) {
						var json = eval('(' + data + ')');
						if (json.success == true) {
							//alert('添加成功');
							zinglabs.i18n.alert('system_saveSuccess');
							doSearch1();														
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
		showTooltip('text-Div','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('text-Div');
	}
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
function pf(index){
			
	var dt =new Array();
	var oTable = $('#zuoxipeixun').dataTable();   
  	var nNodes = oTable.fnGetNodes();
  	for(var i=0; i<nNodes.length;i++){ 
    	dt.push(oTable.fnGetData(nNodes[i])); 
    }
    if(dt[index].file_name.indexOf(".html")!=-1){
    var file_name=dt[index].file_name;
	var folder=file_name.substring((file_name.lastIndexOf("_")+1),(file_name.lastIndexOf("_")+9));
	var u="/"+window.top.PRJ_PATH+"/ZIM/record_data/"+folder+"/"+file_name;
	var params={
		title:"查看内容",
		width:"950",
		url: "/"+window.top.PRJ_PATH+"/zh_cn/ZQC/wenbenzuoyepingfen.html?peixun=peixun&grade_id="+dt[index].grade_id+"&lujing="+u 
		
	};
    }
    if(dt[index].file_name.indexOf(".mp3")!=-1){
    var u="http://"+dt[index].file_location+"/"+window.top.PRJ_PATH+"/"+dt[index].file_name;
	var params={
		title:"查看内容",
		width:"950",
		url: "/"+window.top.PRJ_PATH+"/zh_cn/ZQC/yuyinzuoyepingfen.html?peixun=peixun&grade_id="+dt[index].grade_id+"&lujing="+u
		
	};
    }
	zinglabs.dialog(params);
}			