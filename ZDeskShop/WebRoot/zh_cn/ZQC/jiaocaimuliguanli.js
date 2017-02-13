
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
					pat.tableName = "T_TEACH_LIB_DIR";
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
		var rdata=[];
		var pat={};				   
		pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	pat.nameSpaceId = 'getTiaojianChaxun_jiaocaiku';
    	pat.TEACH_DIR_DESCRIPTION=$("#TEACH_DIR_DESCRIPTION").val();
    	commonSelectForComplex(pat, false,
    	function(data) {
     	if (data) {
		              rdata=data.rows;
		              var table=$('#zuoxipeixun').DataTable();
			   		  table.clear().draw();
			   		  table.rows.add(rdata).draw();	
					}
    }); 
      	//$("#TEACH_DIR_DESCRIPTION").val('');
     return rdata;			
	}
	function doUpdate(){
			if($("#id").val()==''||$("#id").val()==null){
			//alert('无id值，操作失败');
			zinglabs.i18n.alert('ZJGL_wuid');
			return;
			}
			if($("#TEACH_DIR_DESCRIPTIONU").val()==''||$("#TEACH_DIR_DESCRIPTIONU").val()==null||$("#TEACH_DIR_DESCRIPTIONU").val()==undefined){
		       //alert('请填写必填项');
		      zinglabs.i18n.alert('ZJGL_tianxiebitianxiang');
		       return;
			}
			var pat = zkm_getHtmlFormJsons('updateForm');
			pat.tableName = "T_TEACH_LIB_DIR";
			pat.primaryKey = "id";
			pat.primaryKeyValue = $("#id").val();
		    commonUpdate(pat,function(data){
		    var json=eval('('+data+')');
				     if(json.success){				        
				        //alert('修改成功');
				        zinglabs.i18n.alert('system_updateSuccess');
				          doSearch1();
				            $("#id").val('');
				          	$("#TEACH_DIR_DESCRIPTIONU").val('');
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
			            scroll2data("addForm");									
						$("#addFormDiv").slideDown(300);
																
			}
			function doInsert(){
			if($("#TEACH_DIR_DESCRIPTIONUA").val()==''||$("#TEACH_DIR_DESCRIPTIONUA").val()==null||$("#TEACH_DIR_DESCRIPTIONUA").val()==undefined){
		       //alert('请填写必填项');
		       zinglabs.i18n.alert('ZJGL_tianxiebitianxiang');
		       return;
			}var pat = zkm_getHtmlFormJsons("addForm");
				pat.tableName = "T_TEACH_LIB_DIR";
				//alert(JSON.stringify(pat));				
				commonInsert(pat, function(data) {
						var json = eval('(' + data + ')');
						if (json.success == true) {
							//alert('添加成功');
							zinglabs.i18n.alert('system_saveSuccess');
							doSearch1();
							$("#TEACH_DIR_DESCRIPTIONUA").val('');
					    													
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
                pat.tableName="T_TEACH_LIB_DIR";
                pat.primaryKey="id";
                pat.primaryKeyValue =selectData[i].id;
                commonDelete(pat,function(data){
                	var json=eval('('+data+')');
                	if(json.success){		            	 
		            	 		            	 
		            }
		            else{
		             	
		             	zinglabs.i18n.alert('system_deleteFailed');
		            }                	              
                });
			    
			    }
			    //alert('删除成功');
			    zinglabs.i18n.alert('system_deleteSuccess');
			     doSearch1(); 
			     $("#TEACH_DIR_DESCRIPTIONUA").val('');
			     $("#id").val('');
				 $("#TEACH_DIR_DESCRIPTIONU").val('');
			    });    				   				     
				
			      		    
			}				