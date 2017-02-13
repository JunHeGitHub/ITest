var ZQC_ROU="ZQC";
 var treeNode_;
 
 
 //@@ 树的展示FF
function getItemTree(){
	grade_index=getQueryString("id");
	var description=getQueryString("grade_name");				
	//grade_index=241;
	var parentid=0;
	//description='在线客服质量评估标准(2013年)';
	document.getElementById("grade_index").innerHTML="评分指标："+description;
	//$("#grade_index").innerHTML(description);
		$("#treeDemo").html('');
		var jsons={};		
		var url="/"+window.top.PRJ_PATH+"/"+ZQC_ROU+"/ZKMCommonTree/getZhijianTreeNode.action";
		jsons.grade_index=grade_index;
		jsons.id='';			
			ajaxFunction(url,jsons,false,function (data){
				if(data.success){
				var commonTreeParam=data.data;
			for(var i=0;i<commonTreeParam.length;i++){
				commonTreeParam[i].text=commonTreeParam[i].text+'('+commonTreeParam[i].percent+')';
		   }
				$.fn.zTree.init($("#treeDemo"), setting, commonTreeParam);	
				huoQuZongfen(commonTreeParam,'1');// 获取分值	
				huoQuQuanZhong(commonTreeParam,'1');//获取权重		
				doJisuanfenzhi();//计算分值	
				huoQuQuanZhong();//获取权重					
				}
			});
	}
	
	
//@@刷新树
function refresh_tree(){
		$("#treeDemo").html('');
		var jsons={};		
		var url="/"+window.top.PRJ_PATH+"/"+ZQC_ROU+"/ZKMCommonTree/getZhijianTreeNode.action";
		jsons.grade_index=grade_index;
		jsons.id='';		
			ajaxFunction(url,jsons,false,function (data){
				if(data.success){
				var commonTreeParam=data.data;
				for(var i=0;i<commonTreeParam.length;i++){
				commonTreeParam[i].text=commonTreeParam[i].text+'('+commonTreeParam[i].percent+')';
		   }				   
				$.fn.zTree.init($("#treeDemo"), setting, commonTreeParam);
				huoQuZongfen(commonTreeParam,'1');//获取分值		
				huoQuQuanZhong(commonTreeParam,'1');//获取权重	
				doJisuanfenzhi();//计算分值									
				}
			});						
}

			
			
//@@各项指标修改
function doUpdate(){
	if($("#id").val()==''||$("#id").val()==null){
	//alert('无id值，操作失败');
	zinglabs.i18n.alert('ZJGL_wuid'); 
	return;
	}
	$("#bianji").addClass('hide');			           								
	$("#baocunshujuku").removeClass('hide');
	var cfs=$("#gexiangzhibiao input[name],textarea[name]");
	$('#is_involve').prop("disabled",false);//移除checkbox只读样式
	for(var i=0;i<cfs.length;i++){
	var e=cfs[i];	
	document.getElementById(e.name).readOnly=false;													
	}
}


//@@计算权重
function doJisuanQuanZhong(){
		huoQuQuanZhong(select_id,'');
		var sum=0;
	 	for(var i=0;i<score.length;i++){
	 		if(weight[i]!=null&&weight[i]!=''&&weight[i]!=undefined){
	 			sum+=parseInt(weight[i][i]);
	 		}
	 	}
	   return sum;
}



//获取权重
var weight=new Array;	
function huoQuQuanZhong(id,str){
	if(id!=''&&id!=null&&id!=undefined){
		select_id=id;
		var val=$("#weight").val();
		var json={};
		if(str=='1'){
			for(var i=0;i<id.length;i++){
				json[id[i].id]=id[i].weight;
				weight[id[i].id]=json;
			}
		}else{
			var aa=json;
			if(json!=null){
					json[id.id]=val;
					weight[id.id]=json;
				}
		}
	}
}




//@@计算分数
var select_id={};
function doJisuanfenzhi(){
		huoQuZongfen(select_id,'');
		var aa=score;
		var sum=0;
	 	for(var i=0;i<score.length;i++){
	 		if(score[i]!=null&&score[i]!=''&&score[i]!=undefined){
	 			sum+=parseInt(score[i][i]);
	 		}
	 	}
	   $("#jisuanfenzhi").val(sum);
	   return sum;
}
//获取各项分值
var score=new Array;
function huoQuZongfen(id,str){
	if(id!=''&&id!=null&&id!=undefined){
		select_id=id;
		var val=$("#percent").val();
		var json={};
		if(str=='1'){
			for(var i=0;i<id.length;i++){
				json[id[i].id]=id[i].percent;
				score[id[i].id]=json;
			}
		}else{
				json[id.id]=val;
				score[id.id]=json;
		}
	}
}



//通用修改@@----正式入库    先判断是更新操作还是添加操作
function doUpdate_ruku(){
	//更新
	if(updateOrAdd==0){
	if($("#description").val()==''||$("#description").val()==null||$("#description").val()==undefined||$("#percent").val()==''
	||$("#percent").val()==null||
	$("#percent").val()==undefined){
	      //alert('请填写必填项');
	      zinglabs.i18n.alert('ZJGL_tianxiebitianxiang');
	      return;
	}
	if(!V_ZhengshuCheck($("#min_value").val())||
	  !V_ZhengshuCheck($("#max_value").val())||
	  !V_ZhengshuCheck($("#reference_value").val())||
	  !V_ZhengshuCheck($("#percent").val())){
	  //alert('请填写数字');
	  zinglabs.i18n.alert('ZJGL_tianxieshuzi');
	  return;
	}
	if(doJisuanfenzhi()<0||doJisuanfenzhi()>100){
		//alert("总分不能低于0分，或超过100！");
		zinglabs.i18n.alert('ZJGL_zongfenbunengchaoguolinghuoyibai');
		return;
	}
	var aa=doJisuanQuanZhong();
	if(doJisuanQuanZhong()<0||doJisuanQuanZhong()>100){
		zinglabs.i18n.alert('ZJGL_quanzhongbunengchaoguolinghuoyibai');
		return;
	}
	var pat = zkm_getHtmlFormJsons('gexiangzhibiao');
	pat.tableName = "dictinfo";
	pat.primaryKey = "id";
	pat.primaryKeyValue = $("#id").val();
   commonUpdate(pat,function(data){
   var json=eval('('+data+')');
	     if(json.success){
	        refresh_tree();	
	        //alert('修改成功');
	        zinglabs.i18n.alert('system_updateSuccess');
	        clearInput();//清空输入框
			var cfs=$("#gexiangzhibiao input[name],textarea[name]");
			for(var i=0;i<cfs.length;i++){
			var e=cfs[i];
			document.getElementById(e.name).readOnly=true;													
			}
			$('#is_involve').prop("disabled",true);//移除checkbox只读样式
			$('#is_involve').val(0);
	      }else{
	        //alert('修改失败');
	        zinglabs.i18n.alert('system_updateFailed');
	      }
    });
    }
    
    
    
    //添加节点
    if(updateOrAdd==1){
    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
 	var nodes = treeObj.getSelectedNodes();
 	var newNode = {};
    if($("#description").val()==''||$("#description").val()==null||$("#description").val()==undefined||$("#percent").val()==''
	||$("#percent").val()==null||
	$("#percent").val()==undefined){
      //alert('请填写必填项');
      zinglabs.i18n.alert('ZJGL_tianxiebitianxiang');
      return;
	}
	if(!V_ZhengshuCheck($("#min_value").val())||
	  !V_ZhengshuCheck($("#max_value").val())||
	  !V_ZhengshuCheck($("#percent").val())||
	  !V_ZhengshuCheck($("#reference_value").val())){
		  //alert('请填写数字');
		  zinglabs.i18n.alert('ZJGL_tianxieshuzi');
		  return;
	}
	var pat = zkm_getHtmlFormJsons("gexiangzhibiao");
	pat.tableName = "dictinfo";
	pat.primaryKey = "id";	
	pat.uuid=false;
	delete pat.id;
	delete pat.genjiedian;	
	pat.parentid=nodes[0].id;
	pat.grade_index=grade_index;
	commonInsertAndGetId(pat,function(data){
			           if(data.success){
			           newNode.id=data.data;
			           //alert('添加节点成功');
			           zinglabs.i18n.alert('system_saveSuccess');
			            clearInput();
						var cfs=$("#gexiangzhibiao input[name],textarea[name]");
						for(var i=0;i<cfs.length;i++){
						var e=cfs[i];	
						document.getElementById(e.name).readOnly=true;													
						}
			            }else{
			             //alert('添加节点失败');
			             zinglabs.i18n.alert('system_saveFailed');
			            }
			        });
			var nodess;						
			if (nodes[0].zAsync){
			newNode.text=pat.description+'('+pat.percent+')';
                     nodess = treeObj.addNodes(nodes[0],newNode, false); 
                     refresh_tree();
 		    return;                  
                     }
    }
    
    
    
    //添加根节点
    if(updateOrAdd==3){
    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
 	var newNode = {};
    if($("#description").val()==''||$("#description").val()==null||$("#description").val()==undefined||$("#percent").val()==''
	||$("#percent").val()==null||
	$("#percent").val()==undefined){
	      //alert('请填写必填项');
	      zinglabs.i18n.alert('ZJGL_tianxiebitianxiang');
	      return;
	}
	if(!V_ZhengshuCheck($("#min_value").val())||
	  !V_ZhengshuCheck($("#max_value").val())||
	  !V_ZhengshuCheck($("#percent").val())||
	  !V_ZhengshuCheck($("#reference_value").val())){
	  //alert('请填写数字');
	  zinglabs.i18n.alert('ZJGL_tianxieshuzi');
	  return;
	}
	var pat = zkm_getHtmlFormJsons("gexiangzhibiao");
	pat.tableName = "dictinfo";
	pat.primaryKey = "id";	
	pat.uuid=false;
	delete pat.id;
	delete pat.genjiedian;	
	pat.parentid=0;
	pat.grade_index=grade_index;
	commonInsertAndGetId(pat,function(data){
			           if(data.success){
			           newNode.id=data.data;
			           //alert('添加跟节点成功');
			           zinglabs.i18n.alert('system_saveSuccess');
			           refresh_tree();
			            clearInput();
						var cfs=$("#gexiangzhibiao input[name],textarea[name]");
						for(var i=0;i<cfs.length;i++){
						var e=cfs[i];	
						document.getElementById(e.name).readOnly=true;													
						}
			            }else{
			             //alert('添加节点失败');
			             zinglabs.i18n.alert('system_saveFailed');
			            }
			        });
			//var nodess;						
			//newNode.text=pat.description+'('+pat.percent+')';
                     //nodess = treeObj.addNodes(null,newNode, true); 
                     
 		    return;                  
    }
}






			function deljJiedian(){
			    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				var nodes = treeObj.getSelectedNodes();
			 	if(nodes==''||nodes==null||nodes.length==0){
			 	zinglabs.i18n.alert('system_selectDataForDelete');
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
 		var url="/"+window.top.PRJ_PATH+"/"+ZQC_ROU+"/ZKMCommonTree/deleteTreeNode_ZJ.action";
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
                   //alert('删除成功！');
                   zinglabs.i18n.alert('system_deleteSuccess');
                   refresh_tree();
                     clearInput();
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
		         refresh_tree();
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
		$("#baocunshujuku").addClass('hide');
		$("#bianji").removeClass('hide');
		$('#is_involve').prop("disabled",true);//checkbox只读样式		
		 var cfs=$("#gexiangzhibiao input[name],textarea[name]");
		for(var i=0;i<cfs.length;i++){
			var e=cfs[i];
			document.getElementById(e.name).readOnly=true;
		}
		$("#genjiedian").val('');
		$("#description").val(treeNode.description);            
		$("#id").val(treeNode.id);
		$("#min_value").val(treeNode.min_value);
		$("#max_value").val(treeNode.max_value);
		$("#reference_value").val(treeNode.reference_value);
		$("#weight").val(treeNode.weight);
		if(treeNode.reference2_value==null||treeNode.reference2_value==''||treeNode.reference2_value=='null'){
		$("#reference2_value").val('0');
		}else{
		$("#reference2_value").val(treeNode.reference2_value);
		}
		$("#percent").val(treeNode.percent);
		if(treeNode.value_remark==null||treeNode.value_remark=='null'||treeNode.value_remark==''){
		 $("#value_remark").val('');
		}else{
		$("#value_remark").val(treeNode.value_remark);
		}
		$("#is_involve").val(treeNode.is_involve);
	huoQuZongfen(treeNode,'');
}


    		
    		
    		
    		
    		
//@@添加节点
function doAdd(){
    updateOrAdd=1;
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var nodes = treeObj.getSelectedNodes();
 	if(nodes!=''&&nodes!=null&&nodes.length==1){
 	$("#genjiedian").val('所属上级评分项：'+nodes[0].description);
 	clearInput();
 	$("#bianji").addClass('hide');			           								
	$("#baocunshujuku").removeClass('hide');
	var cfs=$("#gexiangzhibiao input[name],textarea[name]");
	$('#is_involve').prop("disabled",false);//checkbox只读样式	
	for(var i=0;i<cfs.length;i++){
	var e=cfs[i];	
	document.getElementById(e.name).readOnly=false;													
	}
	document.getElementById('genjiedian').readOnly=true;
 	}else{
 	//alert('请选择节点');
 	zinglabs.i18n.alert('tree_chooseNode');
 	}
}


//@@添加根节点
function addGenjiedian(){
	updateOrAdd=3;
 	clearInput();
 	$("#bianji").addClass('hide');			           								
	$("#baocunshujuku").removeClass('hide');
	var cfs=$("#gexiangzhibiao input[name],textarea[name]");
	$('#is_involve').prop("disabled",false);//checkbox只读样式		
	for(var i=0;i<cfs.length;i++){
	var e=cfs[i];	
	document.getElementById(e.name).readOnly=false;													
	}
	document.getElementById('genjiedian').readOnly=true;
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
			$("#"+id).tooltip('show');
			$("#"+id).addClass('error');
		}
		//还原样式
		function hideTooltip(id){
			$("#"+id).tooltip('destroy') ;
			$("#"+id).removeClass('error') ;
		}
			
   function clearInput(){
			$("#baocunshujuku").addClass('hide');			           								
			$("#bianji").removeClass('hide');
			$("#description").val('');
			$("#id").val('');
			$("#min_value").val('');
			$("#max_value").val('');
			$("#reference_value").val('');
			$("#reference2_value").val('');
			$("#percent").val('');
			$("#value_remark").val('');
			$("#weight").val('');
}

       	