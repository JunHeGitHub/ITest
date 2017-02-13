	
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
		function doSearch(){
				   var pat={};
					pat.tableName = "SA_QC_GRADE_DICTINFO";
					pat.primaryKey = "id";		
					commonSelect(pat,false,function(data){					
					if(data.rows!= null){				
					  rdata=data.rows;				 	
			}			
		});
		return rdata;
	}
		function doSearch1(){
		$("#updateFormDiv").hide();
		var bt=$("#grade_index_time_s").val();
	    var et=$("#grade_index_time_e").val();
	    bt=new Date(Date.parse(bt.replace(/-/g,"/")));
		et=new Date(Date.parse(et.replace(/-/g,"/")));
	    if($("#grade_index_time_s").val()==""||$("#grade_index_time_e").val()==""){
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
    	pat.nameSpaceId = 'getpingfenzhibiaoliulan';
    	pat.grade_name=$("#grade_name").val();
    	pat.grade_index_desp=$("#grade_index_desp").val();
    	pat.grade_index_creator=$("#grade_index_creator").val();
    	pat.grade_index_time_s=$("#grade_index_time_s").val();
    	pat.grade_index_time_e=$("#grade_index_time_e").val();
    	commonSelectForComplex(pat, false,
    	function(data) {
     	if (data) {
		              rdata=data.rows;
		              var table=$('#zuoxipeixun').DataTable();
			   		  table.clear().draw();
			   		  table.rows.add(rdata).draw();	
					}
    }); 
      	//$("#grade_name").val('');
    	//$("#grade_index_desp").val('');
    	//$("#grade_index_creator").val('');
    	//$("#grade_index_time_s").val('');
    	//$("#grade_index_time_e").val('');
    		
    
     return rdata;			
	}
	
	function dochongzhi(){
		$("#grade_name").val('');
    	$("#grade_index_desp").val('');
    	$("#grade_index_creator").val('');
    	$("#grade_index_time_s").val('');
    	$("#grade_index_time_e").val('');
	}
	function doUpdate(){
			if($("#id").val()==''||$("#id").val()==null){
			//alert('无id值，操作失败');
			zinglabs.i18n.alert('ZJGL_wuid');
			return;
			}
			if($("#grade_nameu").val()==''||$("#grade_nameu").val()==null||$("#grade_nameu").val()==undefined||$("#grade_index_time_su").val()==''||$("#grade_index_time_su").val()==null||$("#grade_index_time_su").val()==undefined){
		       //alert('请填写必填项');
		      zinglabs.i18n.alert('ZJGL_tianxiebitianxiang');
		       return;
			}
			var pat = zkm_getHtmlFormJsons('updateForm');
			pat.tableName = "SA_QC_GRADE_DICTINFO";
			pat.primaryKey = "id";
			pat.primaryKeyValue = $("#id").val();
		    commonUpdate(pat,function(data){
		    var json=eval('('+data+')');
				     if(json.success){				        
				        //alert('修改成功');
				        	zinglabs.i18n.alert('system_updateSuccess');
				         	doSearch1();
				            $("#id").val('');
				          	$("#grade_nameu").val('');
					    	$("#grade_index_despu").val('');
					    	$("#grade_index_creatoru").val('');
					    	$("#grade_index_time_su").val('');
					    	
				      }else{
				        //alert('修改失败');
				        zinglabs.i18n.alert('system_updateFailed');
				       }
		     });
		      
			}
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
		function add(){	
						$("#updateFormDiv").slideUp(300);
			            scroll2data("addFormDiv");									
						$("#addFormDiv").removeClass('hide');
						var user=getUserInfo();
				       $("#grade_index_creatora").val(user.loginName);
																
			}
			function doInsert(){
			if($("#grade_namea").val()==''||$("#grade_namea").val()==null||$("#grade_namea").val()==undefined||$("#grade_index_time_a").val()==''||$("#grade_index_time_a").val()==null||$("#grade_index_time_a").val()==undefined){
		       //alert('请填写必填项');
		      zinglabs.i18n.alert('ZJGL_tianxiebitianxiang');
		       return;
			}var pat = zkm_getHtmlFormJsons("addForm");
				pat.tableName = "SA_QC_GRADE_DICTINFO";
				
				//alert(JSON.stringify(pat));				
				commonInsert(pat, function(data) {
						var json = eval('(' + data + ')');
						if (json.success == true) {
							//alert('添加成功');
							zinglabs.i18n.alert('system_saveSuccess');
							doSearch1();
							//doSearch1();
				          	$("#grade_namea").val('');
					    	$("#grade_index_despa").val('');
					    	//$("#grade_index_creatora").val('');
					    	$("#grade_index_time_a").val('');
							return;
						} else {
							//alert('添加失败');
							zinglabs.i18n.alert('system_saveFailed');
							return;
						}
					});
			}
			//通用删除@@
			function doDelete(){
				var table=$('#zuoxipeixun').DataTable();
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
                pat.tableName="SA_QC_GRADE_DICTINFO";
                pat.primaryKey="id";
                pat.primaryKeyValue =selectData[i].id;
                commonDelete(pat,function(data){
                	var json=eval('('+data+')');
                	if(json.success){		            	 
		            	refresh(); 		            	 
		            }
		            else{
		             	
		             	zinglabs.i18n.alert('system_deleteFailed');
		            }                	              
                });
			    
			    }
			    //alert('删除成功');
			    zinglabs.i18n.alert('system_deleteSuccess');
			     doSearch1(); 
			    			$("#id").val('');
				          	$("#grade_nameu").val('');
					    	$("#grade_index_despu").val('');
					    	$("#grade_index_creatoru").val('');
					    	$("#grade_index_time_su").val('');
					    	$("#grade_namea").val('');
					    	$("#grade_index_despa").val('');
					    	//$("#grade_index_creatora").val('');
					    	$("#grade_index_time_a").val('');
			    }); 	   				     
			}
function pf(index){
			
	var dt =new Array();
	var oTable = $('#zuoxipeixun').dataTable();   
  	var nNodes = oTable.fnGetNodes();
  	for(var i=0; i<nNodes.length;i++){ 
    	dt.push(oTable.fnGetData(nNodes[i])); 
    }
    var u="/"+window.top.PRJ_PATH+"/zh_cn/ZQC/pingfenzhibiaoguanli.html?id="+dt[index].id+"&grade_name="+dt[index].grade_name;
    
	var params={
		title:"评分指标管理",
		width:"900",
		height:"500",
		content: '<iframe src='+u+' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>'
		
	};
	zinglabs.dialog(params);
}			