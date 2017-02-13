 var treeNode_;
 var TWGL="TWGL";
 //@@ 树的展示
		function getItemTree(){
			    var parentid=0;
				$("#treeDemo").html('');
				var jsons={};		
				var url="/"+PRJ_PATH+"/"+TWGL+"/ZKMCommonTree/getCaidanTreeNode.action";
				jsons.id='';
				var isStart='';		
				ajaxFunction(url,jsons,false,function (data){
					if(data.success){
					var commonTreeParam=data.data;
				for(var i=0;i<commonTreeParam.length;i++){
				    if(commonTreeParam[i].pId==0){
				    	commonTreeParam[i].text=commonTreeParam[i].type;
				    }else{
				    	if(commonTreeParam[i].isStart==1){
				     	isStart='(已启用)';
				     	commonTreeParam[i].text=commonTreeParam[i].text+isStart;
				    	}else{
				    		commonTreeParam[i].text=commonTreeParam[i].text;
				    	}
				    }
			   }	   
					$.fn.zTree.init($("#treeDemo"), setting, commonTreeParam);		
					var treeObj = $.fn.zTree.getZTreeObj("treeDemo"); 
					treeObj.expandAll(true);								
					}
				});			
			}
			//@@刷新树
			function refresh_tree(){
				$("#treeDemo").html('');
				var jsons={};		
				var url="/"+PRJ_PATH+"/"+TWGL+"/ZKMCommonTree/getCaidanTreeNode.action";
				jsons.id='';
				ajaxFunction(url,jsons,false,function (data){
					if(data.success){
					var commonTreeParam=data.data;
				for(var i=0;i<commonTreeParam.length;i++){
				    if(commonTreeParam[i].pId==0){
				    	commonTreeParam[i].text=commonTreeParam[i].type;
				    }else{
					    if(commonTreeParam[i].isStart==1){
					     	isStart='(已启用)';
					    	commonTreeParam[i].text=commonTreeParam[i].text+isStart;
					    }else{
					    	commonTreeParam[i].text=commonTreeParam[i].text;
					    }
				    }
				    
			   }
					$.fn.zTree.init($("#treeDemo"), setting, commonTreeParam);
					var treeObj = $.fn.zTree.getZTreeObj("treeDemo"); 
					treeObj.expandAll(true); 										
					}
				});			
			}
			
			
function doUpdate(){
	if($("#id").val()==''||$("#id").val()==null){
		zinglabs.alert('无id值，操作失败');
		//zinglabs.i18n.alert('ZJGL_wuid'); 
		return;
	}
	if(updateOrAdd==0){
		if($("#name").val()==''||$("#name").val()==null||$("#name").val()==undefined){
			zinglabs.alert('请填写必填项');
			//zinglabs.i18n.alert('ZJGL_tianxiebitianxiang');
			return;
		}
		
		
		if(charVali($("#name").val()) > 10){
			zinglabs.alert("菜单名称过长");
			return;
		}
		
		
		
				/*var pat = zkm_getHtmlFormJsons('gexiangzhibiao');
				pat.tableName = "tree_weixincaidan";
				pat.primaryKey = "id";
				pat.primaryKeyValue = $("#id").val();
			    commonUpdate(pat,function(data){
			    var json=eval('('+data+')');
					     if(json.success){*/
		var params={};
		var pat = zkm_getHtmlFormJsons('gexiangzhibiao');
		pat.id=$("#id").val();
		var tt=$("#id").val();
		
		if($("#isStart").prop("checked") == true){
			pat.isStart = "1";
		}else{
			pat.isStart = "0";
		}
		params.columnValues=pat;
		params.tableName="tree_weixincaidan";
		//如更改不是id 需定义 params.primarykey
		
		
		commonUpdate(params,true,function(data){
		if(data.success){
		     //refresh_tree();	
			 zinglabs.alert('修改成功');
			 //zinglabs.i18n.alert('system_updateSuccess');
			 //$("#baocunshujuku").addClass('hide');
			$("#bianji").removeClass('hide');
			$("#name").val('');
				
			$("#url").val('');
			document.getElementById('isStart').checked=true;
			$("#gexiangzhibiaoDiv").hide();	
	    	$("#gexiangzhibiaoDiv_").hide();
	    	/*	            
	    	if(pat.isStart==0){
	    		 var jsons1={};
	    		 var url="/"+PRJ_PATH+"/"+TWGL+"/ZKMCommonTree/getCaidanTreeNode_ById.action";
				jsons1.id=$("#id").val();
				ajaxFunction(url,jsons1,false,function (data){
					if(data.data!=""&&data.data!=null){
						for(var i=0;i<data.data.length;i++){
							var params={};
							var pat = {};
							pat.id=data.data[i].id;
							pat.isStart="0";
							params.columnValues=pat;
							params.tableName="tree_weixincaidan";
								    //如更改不是id 需定义 params.primarykey
							commonUpdate(params,true,function(data){
								refresh_tree();
							});
						}
					}
				});
			}         */  
	    	$("#id").val('');
	    	refresh_tree();
			/*			
			var jsons={};
			jsons.type=treeNode_.type;
			var url="/"+PRJ_PATH+"/"+TWGL+"/ZKMCommonTree/getCaidanTreeNode_geshu.action";
			ajaxFunction(url,jsons,false,function (data){
				if(data.success){
					var commonTreeParam=data.data;
					var array = new Array(); //定义数组
					if(commonTreeParam.length>=5){
						var params3={};
			   			var pat = {};
					    pat.id=tt;
					    pat.isStart="0";
					    params3.columnValues=pat;
					    params3.tableName="tree_weixincaidan";
					    commonUpdate(params3,false,function(data){
					    	refresh_tree();
					   	    window.top.zinglabs.alert('根节点已超出启用个数');
					   	});
					
					}
				}
			});*/
			/*		
			var jsons={};
			jsons.type=treeNode_.type;
			var url="/"+PRJ_PATH+"/"+TWGL+"/ZKMCommonTree/getCaidanTreeNode_geshu_isStart.action";
			ajaxFunction(url,jsons,false,function (data){
				if(data.success){
					var commonTreeParam=data.data;
					var array = new Array(); //定义数组
					if(commonTreeParam.length>=5){
						var params3={};
			    		var pat = {};
					    pat.id=tt;
					    pat.isStart="1";
					    params3.columnValues=pat;
					    params3.tableName="tree_weixincaidan";
			   			commonUpdate(params3,false,function(data){
				   			refresh_tree();
				   			window.top.zinglabs.alert('不允许将根节点皆置为未开启模式');
				   		});
			
					}	
				}
			});*/
					/*for(var i=0;i<commonTreeParam.length;i++){
					jsons={};
					jsons.parentId=commonTreeParam[i].id;
				    url="/"+PRJ_PATH+"/"+TWGL+"/ZKMCommonTree/getCaidanTreeNode_geshu_child.action";
					ajaxFunction(url,jsons,false,function (data){
						if(data.success){
						var commonTreeParam=data.data;
					if(commonTreeParam.length>5){
					  b=0;
					  array.push(commonTreeParam.length);
					  //document.getElementById("tishi").innerHTML="子菜单过多";
					  //alert('子菜单过多');
					  return;
					}else{
					 b=0;
					array.push(commonTreeParam.length);
					  //document.getElementById("tishi").innerHTML="符合条件";
					  //alert('子菜单过多');
					  return;
					}	   
						}
					});		
					}
					}	
					else{
					b=0;document.getElementById("tishi").innerHTML="根菜单过多";
					//alert('根菜单过多');
					return;
					}   
					var gg=0;
					for( gg=0;gg<array.length;gg++){
					if(array[gg]>5){
					document.getElementById("tishi").innerHTML="子菜单过多";
					break;
					}
					}
					if(gg>=array.length){
					document.getElementById("tishi").innerHTML="...";
					}
						}
					});*/
			}else{
				zinglabs.alert('修改失败');
					        //zinglabs.i18n.alert('system_updateFailed');
			}
		});
	}
}
//通用修改@@----正式入库    先判断是更新操作还是添加操作
function doUpdate_ruku(){
	//更新
	if(updateOrAdd==0){
		if($("#name").val()==''||$("#name").val()==null||$("#name").val()==undefined){
		    zinglabs.alert('请填写必填项');
		    return;
		}
		
		
		if(charVali($("#name").val()) > 10){
			zinglabs.alert("菜单名称过长");
			return;
		}
		
		$("#name").val($("#name").val().toLowerCase());
		
		
		var params={};
		var pat = zkm_getHtmlFormJsons('gexiangzhibiao');
		pat.id=$("#id").val();
		params.columnValues=pat;
		params.tableName="tree_weixincaidan";
		//如更改不是id 需定义 params.primarykey
		commonUpdate(params,true,function(data){
			if(data.success){
				refresh_tree();	
				zinglabs.alert('修改成功');
				 //zinglabs.i18n.alert('system_updateSuccess');
				$("#baocunshujuku").addClass('hide');			           								
				$("#bianji").removeClass('hide');
				$("#name").val('');
				$("#id").val('');
				$("#url").val('');
				$("#isStart").val('');
				var cfs=$("#gexiangzhibiao input[name],textarea[name]");
				for(var i=0;i<cfs.length;i++){
					var e=cfs[i];	
					document.getElementById(e.name).readOnly=true;													
				}	
			}else{
				zinglabs.alert('修改失败');
			 }
		});
	}
	//添加节点
	if(updateOrAdd==1){
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var nodes = treeObj.getSelectedNodes();
		var newNode = {};
		if($("#name").val()==''||$("#name").val()==null||$("#name").val()==undefined){
		    zinglabs.alert('请填写必填项');
		    return;
		}
		
		
		if(charVali($("#name").val()) > 10){
			zinglabs.alert("菜单名称过长");
			return;
		}
		
		$("#name").val($("#name").val().toLowerCase());
		
		/*
		if($("#name").val().length > 4){ 
			
		}*/
		
		
		var params1={};
		var pat = zkm_getHtmlFormJsons("gexiangzhibiao");
		 delete pat.id;
		pat.parentId=""+nodes[0].id+"";
		pat.type=nodes[0].type;
		params1.columnValues=pat;
		params1.tableName="tree_weixincaidan";
			    //使用增
			    //alert(JSON.stringify(params1));
		commonInsertOrUpdate(params1,true,function(data){
				if(data.success){
					newNode.id=data.idValues[0];
					var temp=data.idValues[0];
					zinglabs.alert('添加节点成功');
						           //zinglabs.i18n.alert('system_saveSuccess');
					 refresh_tree();
					$("#baocunshujuku").addClass('hide');			           								
					$("#bianji").removeClass('hide');
					$("#name").val('');
					$("#id").val('');
					$("#url").val('');
					document.getElementById('isStart').checked=true;
					$("#gexiangzhibiaoDiv").hide();	
    		        $("#gexiangzhibiaoDiv_").hide();
					var jsons={};
					jsons.type=nodes[0].type;
				/*	var url="/"+PRJ_PATH+"/"+TWGL+"/ZKMCommonTree/getCaidanTreeNode_geshu.action";
					ajaxFunction(url,jsons,false,function (data){
						if(data.success){
							var commonTreeParam=data.data;
							var array = new Array(); //定义数组
							if(commonTreeParam.length>=5){
								var params3={};
				    			var pat = {};
				    			pat.id=temp;
				    			pat.isStart="0";
				    			params3.columnValues=pat;
				   	 			params3.tableName="tree_weixincaidan";
							    commonUpdate(params3,false,function(data){
							    	refresh_tree();
							    	window.top.zinglabs.alert('根节点已超出启用个数');
							    });
							}	
						}
					});*/
					
				/*	
				var jsons={};
				jsons.type=nodes[0].type;
				var url="/"+PRJ_PATH+"/"+TWGL+"/ZKMCommonTree/getCaidanTreeNode_geshu_isStart.action";
				ajaxFunction(url,jsons,false,function (data){
					if(data.success){
						var commonTreeParam=data.data;
						var array = new Array(); //定义数组
						if(commonTreeParam.length>=5){
							var params3={};
						    var pat = {};
						    pat.id=temp;
						    pat.isStart="1";
						    params3.columnValues=pat;
						    params3.tableName="tree_weixincaidan";
						    commonUpdate(params3,false,function(data){
						    	refresh_tree();
						    	window.top.zinglabs.alert('不允许将根节点皆置为未开启模式');
						    });
						}	
					}
				});*/
		}else{
			zinglabs.alert('添加节点失败');
		}
	  });
	}
	
	//添加根节点
	if(updateOrAdd==3){
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var newNode = {};
		if($("#name").val()==''||$("#name").val()==null||$("#name").val()==undefined){
		    zinglabs.alert('请填写必填项');
		    return;
		}
		
		if(charVali($("#name").val()) > 8){
			zinglabs.alert("菜单名称过长");
			return;
		}
		
		
		var params1={};
		var pat = zkm_getHtmlFormJsons("gexiangzhibiao");
		delete pat.id;
		pat.parentId="0";
		params1.columnValues=pat;
		params1.tableName="tree_weixincaidan";
		commonInsertOrUpdate(params1,true,function(data){
				if(data.success){
					newNode.id=data.idValues[0];
					zinglabs.alert('添加根节点成功');
					refresh_tree();
					$("#baocunshujuku").addClass('hide');			           								
					$("#bianji").removeClass('hide');
					$("#name").val('');
					$("#id").val('');
					$("#url").val('');
					document.getElementById('isStart').checked=true;
					$("#gexiangzhibiaoDiv").hide();	
    		        $("#gexiangzhibiaoDiv_").hide();
																			/*var jsons={};
			var url="/"+PRJ_PATH+"/ZKM/ZKMCommonTree/getCaidanTreeNode_geshu.action";
				ajaxFunction(url,jsons,false,function (data){
					if(data.success){
					var commonTreeParam=data.data;
					var array = new Array(); //定义数组
				if(commonTreeParam.length<=3){
				for(var i=0;i<commonTreeParam.length;i++){
				jsons={};
				jsons.parentid=commonTreeParam[i].id;
			    url="/"+PRJ_PATH+"/ZKM/ZKMCommonTree/getCaidanTreeNode_geshu_child.action";
				ajaxFunction(url,jsons,false,function (data){
					if(data.success){
					var commonTreeParam=data.data;
				if(commonTreeParam.length>5){
				  b=0;
				  array.push(commonTreeParam.length);
				  //document.getElementById("tishi").innerHTML="子菜单过多";
				  //alert('子菜单过多');
				  return;
				}else{
				b=0;array.push(commonTreeParam.length);
				  //document.getElementById("tishi").innerHTML="符合条件";
				  //alert('子菜单过多');
				  return;
				}	   
					}
				});		
				}
				}	
				else{
				b=0;document.getElementById("tishi").innerHTML="根菜单过多";
				//alert('根菜单过多');
				return;
				}   
				var gg=0;
				for( gg=0;gg<array.length;gg++){
				if(array[gg]>5){
				document.getElementById("tishi").innerHTML="子菜单过多";
				break;
				}
				
				}
				if(gg>=array.length){
				document.getElementById("tishi").innerHTML="符合条件";
				}
					}
				});*/
			}else{
					 zinglabs.alert('添加节点失败');
						            //zinglabs.i18n.alert('system_saveFailed');
			}
		});
		return;                  
	}
}

	function deljJiedian(){
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var nodes = treeObj.getSelectedNodes();
		if(nodes==''||nodes==null||nodes.length==0){
			 zinglabs.alert('选择数据');
			 	//zinglabs.i18n.alert('system_selectDataForDelete');
			 return;
		}
		
		// 默认根节点不可删除；
		var node = nodes[0].getParentNode();
		if(node == null){ 
			zinglabs.alert("默认根节点不可删除");
			return;
		}
		zinglabs.confirm("您确定要删除节点吗？",function(){
			//var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			//treeObj.removeNode(treeNode);
			//alert(JSON.stringify(treeNode_));
				onRemoved(treeNode_);
				
		});		     
			    
	}
			function removeNode(treeNode){
			 onRemoved(treeNode);
			
			}
			function onRemove(e,treeId,treeNode){
				 zinglabs.confirm("您确定要删除节点吗？",function(){
				onRemoved(e,treeId,treeNode);
				 });							
			}
			function onRemoved(treeNode){
			 	var url="/"+PRJ_PATH+"/"+TWGL+"/ZKMCommonTree/deleteTreeNode_caishu.action";
	      		/*var p={};
	       		p.id=treeNode.id;
	       		p.grade_index=grade_index;
	       		alert();
   				ajaxFunction(url,p, false, function(data) {
				if (data.success) {
						alert('删除成功');							
				}else{	
				zinglabs.alert(data.mgs);
				}
				});
				var pat = {};
                pat.tableName="dictinfo";
                pat.primaryKey="id";
                pat.primaryKeyValue =treeNode.id;
                 //alert(JSON.stringify(pat));
                commonDelete(pat,function(data){
                	var json=eval('('+data+')');
                	if(json.success){		            	 
		            	 alert('删除成功');		            	 
		            }
		            else{
		             	alert(json.mgs);
		            }                	              
                });*/
            var nodeParm={};
            var delParams=[];
            delParams=getdelParams(delParams,treeNode);
            var ids="";
            for(var i=0;i<delParams.length;i++){
                ids+=delParams[i]+","
            }
            nodeParm.ids=ids;
            ajaxFunction(url, nodeParm, false, function (data) {
                if (data.success) {
                      zinglabs.alert('删除成功！');
                      //zinglabs.i18n.alert('system_deleteSuccess');
                      refresh_tree();
                        $("#name").val('');
									$("#id").val('');
									$("#url").val('');
									//$("#isStart").val('');
									document.getElementById('isStart').checked=true;
									$("#gexiangzhibiaoDiv_").hide();
    		    $("#gexiangzhibiaoDiv").hide();
									var jsons={};
						jsons.type=treeNode.type;
				var url="/"+PRJ_PATH+"/"+TWGL+"/ZKMCommonTree/getCaidanTreeNode_geshu.action";
					ajaxFunction(url,jsons,false,function (data){
						if(data.success){
						var commonTreeParam=data.data;
						var array = new Array(); //定义数组
					if(commonTreeParam.length<=4){
					for(var i=0;i<commonTreeParam.length;i++){
					jsons={};
					jsons.parentId=""+commonTreeParam[i].id+"";
				    url="/"+PRJ_PATH+"/"+TWGL+"/ZKMCommonTree/getCaidanTreeNode_geshu_child.action";
					ajaxFunction(url,jsons,false,function (data){
						if(data.success){
						var commonTreeParam=data.data;
					if(commonTreeParam.length>5){
					  b=0;
					  array.push(commonTreeParam.length);
					  //document.getElementById("tishi").innerHTML="子菜单过多";
					  //alert('子菜单过多');
					  return;
					}else{
					 b=0;
					array.push(commonTreeParam.length);
					  //document.getElementById("tishi").innerHTML="符合条件";
					  //alert('子菜单过多');
					  return;
					}	   
						}
					});		
					}
					}	
					else{
					b=0;document.getElementById("tishi").innerHTML="根菜单过多";
					//alert('根菜单过多');
					return;
					}   
					var gg=0;
					for( gg=0;gg<array.length;gg++){
					if(array[gg]>5){
					document.getElementById("tishi").innerHTML="子菜单过多";
					break;
					}
					}
					if(gg>=array.length){
					document.getElementById("tishi").innerHTML="...";
					}
						}
					});
									
                } else {
                    //alert("删除时出现异常！");
                    zinglabs.i18n.alert('system_deleteFailed');
                }
            });
			  //alert(JSON.stringify(treeNode));
				
			}
			//@@delete
			function getdelParams(ids,node){
			ids.push(node.id);
			if (node.isParent){
					for(var obj in node.children){ 
						getdelParams(ids,node.children[obj]);
					}
			   }
			return ids;
			}
			function beforeEditName(treeId,treeNode){
			//return confirm("你确定要编辑该节点吗?");
			zinglabs.confirm("您确定要编辑改节点吗？",function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				treeObj.removeNode(treeNode);
				onRemoved(treeNode);
				 });		     
			     return false;
			}
			function beforeRename(treeId,treeNode,newName,isCancel){
			if(newName==''||newName==null){
			//zinglabs.alert("您好名称不能为空");
			zinglabs.i18n.alert('ZJGL_mingzibuweikong');
			return false;
			}
			return true;
			}
			function onRename(e,treeId,treeNode,isCancel){
			var pat={};
			pat.description=treeNode.text;
			pat.tableName = "dictinfo";
			pat.primaryKey = "id";
			pat.primaryKeyValue = treeNode.id;
		    commonUpdate(pat,function(data){
		    var json=eval('('+data+')');
				     if(json.success){				        
				        //alert('修改名字成功');
				        zinglabs.i18n.alert('system_saveSuccess');
				          refresh();
				      }else{
				        //alert('修改名字失败');
				        zinglabs.i18n.alert('system_updateFailed');
				       }
		     });
			}
			//@@单击右面显示各项指标的具体指
			function zTreeOnClick(event, treeId, treeNode) {
			treeNode_=treeNode;	
			updateOrAdd=0;		
			if(parseFloat(treeNode_["level"])!=0){
    		    $("#gexiangzhibiaoDiv").show();	
    		    $("#gexiangzhibiaoDiv_").hide();
    		    $("#baocunshujuku").addClass('hide');			           								
			$("#bianji").removeClass('hide');
			 /*var cfs=$("#gexiangzhibiao input[name],textarea[name]");
				for(var i=0;i<cfs.length;i++){
				var e=cfs[i];	
				document.getElementById(e.name).readOnly=true;													
				}*/
			$("#name").val(treeNode.name);
			$("#id").val(treeNode.id);
			$("#url").val(treeNode.u);
			if(parseFloat(treeNode.isStart)==1){
			document.getElementById('isStart').checked=true;
			}else{
			document.getElementById('isStart').checked=false;
			}	
    		}else{
    		    $("#gexiangzhibiaoDiv").hide();	
    		    $("#gexiangzhibiaoDiv_").hide();
    		}	
    		}
    		//@@添加节点
    		function doAdd(){
    		    $("#gexiangzhibiaoDiv").show();	
    		    $("#gexiangzhibiaoDiv_").hide();
    		    updateOrAdd=1;
    			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				var nodes = treeObj.getSelectedNodes();
			 	if(nodes!=''&&nodes!=null&&nodes.length==1){
			 	$("#name").val('');
				$("#id").val('');
				$("#url").val('');
				//$("#isStart").val('');
				document.getElementById('isStart').checked=true;
			 	$("#bianji").addClass('hide');			           								
				$("#baocunshujuku").removeClass('hide');
				/*var cfs=$("#gexiangzhibiao input[name],textarea[name]");
				for(var i=0;i<cfs.length;i++){
				var e=cfs[i];	
				document.getElementById(e.name).readOnly=false;													
				}*/
			 	}else{
			 	zinglabs.alert('请选择节点');
			 	$("#gexiangzhibiaoDiv").hide();	
    		    $("#gexiangzhibiaoDiv_").hide();
			 	//zinglabs.i18n.alert('tree_chooseNode');
			 	}
			 	if(parseFloat(treeNode_["level"])==2){
			 	$("#gexiangzhibiaoDiv").hide();	
    		    zinglabs.alert('不能再添加节点');
    		}	
    		    }
    		    //@@添加根节点
    		    function addGenjiedian(){
    		    $("#gexiangzhibiaoDiv_").show();
    		    $("#gexiangzhibiaoDiv").hide();
    		    $("#btn_jisuan_").addClass('hide');
    		    updateOrAdd=3;
			 	//$("#bianji").addClass('hide');			           								
				//$("#baocunshujuku").removeClass('hide');
				/*var cfs=$("#gexiangzhibiao input[name],textarea[name]");
				for(var i=0;i<cfs.length;i++){
				var e=cfs[i];	
				document.getElementById(e.name).readOnly=false;													
				}*/
    		    }
    		    function  checkDescription(){
				if($("#description").val()==''||$("#description").val()==null||$("#description").val()==undefined){
					CHECK_name =false ;
					showTooltip('description_DIV','此项不能为空！') ;
				}else{
					CHECK_name =true ;
					hideTooltip('description_DIV');
				}
			}
			function doUpdate_ruku_genjiedian(){
			/*var pat = zkm_getHtmlFormJsons("gexiangzhibiao_");
				pat.tableName = "tree_weixincaidan";
				pat.primaryKey = "id";	
				pat.uuid=false;
				pat.parentId=0;
				commonInsertAndGetId(pat,function(data){*/
				
				
				
					    var params1={};
			    var pat = zkm_getHtmlFormJsons("gexiangzhibiao_");
			    pat.parentId="0";
			    params1.columnValues=pat;
			    params1.tableName="tree_weixincaidan";
			    //使用增
			    //alert(JSON.stringify(params1));
			    commonInsertOrUpdate(params1,true,function(data){
				if(data.success){
				refresh_tree();
				}
				});
			}
			function zidingyicandan(){
			if(treeNode_!=null&&treeNode_!=""&&treeNode_.pId==null){
			var b=1;
			var jsons={};
						jsons.type=treeNode_.type;
				var url="/"+PRJ_PATH+"/"+TWGL+"/ZKMCommonTree/getCaidanTreeNode_geshu.action";
				var array = new Array(); //定义数组
					ajaxFunction(url,jsons,false,function (data){
						if(data.success){
						var commonTreeParam=data.data;
						
					if(commonTreeParam.length<=4){
					for(var i=0;i<commonTreeParam.length;i++){
					jsons={};
					jsons.parentId=""+commonTreeParam[i].id+"";
				    url="/"+PRJ_PATH+"/"+TWGL+"/ZKMCommonTree/getCaidanTreeNode_geshu_child.action";
					ajaxFunction(url,jsons,false,function (data){
						if(data.success){
						var commonTreeParam=data.data;
					if(commonTreeParam.length>5){
					  array.push(commonTreeParam.length);
					  return;
					}else{
					array.push(commonTreeParam.length);
					  return;
					}	   
						}
					});		
					}
					}	
					else{
					b=0;
					//document.getElementById("tishi").innerHTML="根菜单过多";
					zinglabs.alert('根菜单过多');
					return;
					}   
					var gg=0;
					for( gg=0;gg<array.length;gg++){
					if(array[gg]>5){
					b=0;
					//document.getElementById("tishi").innerHTML="子菜单过多";
					zinglabs.alert('子菜单过多');
					break;
					}
					}
					if(gg>=array.length){
					document.getElementById("tishi").innerHTML="...";
					}
						}
					});
				if(b!=0){
				var e="/"+PRJ_PATH+"/zh_cn/tuWenGuanLi/zidingyicaidan.html?type="+treeNode_.type;
				var params={
				title:"生成自定义菜单",
				width:"350",
				 height:'350',
				content: '<iframe src='+e+' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>'
				
			};
			zinglabs.dialog(params);	
				}
			}else{
			zinglabs.alert('请选择一种菜单类型');
			}
			
				
			
			}
			function  checkPercent(){
				if($("#percent").val()==''||$("#percent").val()==null||$("#percent").val()==undefined){
					CHECK_name =false ;
					showTooltip('percent_DIV','此项不能为空！');
				}else{
					CHECK_name =true ;
					hideTooltip('percent_DIV');
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
			
			//验证菜单名称长度
			function charVali(str){
				var len = 0;  
				  for (var i=0; i<str.length; i++) {  
				    if (str.charCodeAt(i)>127 || str.charCodeAt(i)==94) {  
				       len += 2;  
				     } else {  
				       len ++;  
				     }  
				   }  
				  return len;  
			}
			
          	