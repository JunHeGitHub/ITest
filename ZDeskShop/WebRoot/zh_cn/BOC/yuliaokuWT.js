/*自定义变量*/
var DoType=""; //操作分类
var WTTreerecordType=""; //树的分类
var WTTreenodeId=""; //节点编号
/****************************/

var SP="sp"
		var child;
		var  treeNode_del;
		var recordType;
		var type;
		var params={};
		var commonTreeParam=[];
		var jsons=[];
		var jsons1=[];
		var nodeType;
		var parentId;
		var isSearch;
		var ids=[];
		var objs={};
		var rootValue =null;
		var curStatus = null;
		var changeNode = new Array();
		var asyncNode = new Array();
		var allasyncNode = new Array();
		var treeNodes=[];
		var treeObj;
		var setting = {
		data: {
			simpleData: {
				enable: true,
				idKey:'id',
				piKey:'pid'
			},
			key:{name:"text"}
		},
		async: {},
		callback:{
			beforeDrop:beforeDrop,
			onDrop:onDrop,
			beforeRemove:beforeRemove,//点击删除时触发，用来提示用户是否确定删除
			beforeEditName: beforeEditName,//点击编辑时触发，用来判断该节点是否能编辑
			beforeRename:beforeRename,//编辑结束时触发，用来验证输入的数据是否符合要求
			onRemove:onRemove,
			onRename:onRename,//编辑后触发，用于操作后台
			onClick:zTreeOnClick,//点击节点触发的事件
			removeNode:removeNode
			},
		check: {
			enable: false,
			chkStyle: "checkbox",
			chkboxType: { "Y": "", "N": "" }
		},
		view: {
		},
		edit: {
		drag: {
				isCopy: false,
				isMove: true,
				prev: dropPrev,
				inner: dropInner,
				next: dropNext,
				autoOpenTime: 1000
			},
		enable: true,
		editNameSelectAll:true,
		removeTitle:'删除',
		renameTitle:'重命名'
		}
		};
		$(document).ready(function(){
			load();
		});
		
		
		function dropPrev(treeId, nodes, targetNode){
		return dropRoot(treeId, nodes, targetNode);
		}
		function dropInner(treeId, nodes, targetNode){
		return dropRoot(treeId, nodes, targetNode);
		}
		function dropNext(treeId, nodes, targetNode){
		return dropRoot(treeId, nodes, targetNode);
		}
		function dropRoot(treeId, nodes, targetNode){
		if (!targetNode) {
			return false;
		}
		return true;
		}
		// 拖拽前判断是否允许拖拽
	 	function beforeDrop(treeId, treeNodes, targetNode, moveType) {
		 return targetNode ? targetNode.drop !== false : true;
		} 
		// 拖拽操作结束时调用函数
		function onDrop(event,treeId,treeNodes,targetNode,moveType){
	     if(treeNodes[0].pId==null||treeNodes[0].pId==''){  //拖拽到根节点的情况
		 var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		 var nodes = treeObj.getNodes();
		 treeNodes[0].pId='';
		 var p={};
		 p.id=treeNodes[0].id;
		 p.pId='';
		 ajaxFunction("/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/treeNodeClassificationUpdatePidIsNull.action", p, false, function(data) {
			if (data.success) {
				   var ids = [];
				   for(var i=0;i<nodes.length;i++){
					    ids.push(nodes[i].id);
				   }
				   var params = {};
				   params.ids = ids.join(";");
			ajaxFunction("/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/treeNodeClassificationRoot.action", params, false, function(data) {
			if (data.success) {}
		    });
			}
		});
			return;
		}
	    var treeNodeChildren = treeNodes[0].getParentNode().children;
		var ids = [];
		for(var i=0;i<treeNodeChildren.length;i++){
			ids.push(treeNodeChildren[i].id);
		}
		var params = {};
		var treeNode = treeNodes[0];
		//alert(JSON.stringify(treeNode));
		params.treeNode = JSON.stringify(treeNode);
		params.ids = ids.join(";");
		params.indexs = ids.join(";");
		ajaxFunction("/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/treeNodeClassification.action", params, false, function(data) {
			if (data.success) {}
		});
	    }	
	    
			///ZDesk/sp/ZKMCommonTree/getTreeNode.action	;
		//加载方法，左侧配置表
		function load(){
			var recordType=getQueryString("recordType");
			WTTreerecordType=getQueryString("recordType"); //树分类
			var type=getQueryString("type"); 
			DoType=getQueryString("DoType"); //操作分类
			if(recordType!=null && recordType!='' && recordType!=undefined){
				params.recordType=recordType;
			}
			if(type!=null && type!='' && type!=undefined){
				params.type=type;
			}
				getItemTree(params);
			}
		
		function getUrl(treeId, treeNode) {
		var param = '';
		if(typeof(treeNode) !=undefined && treeNode != null ){
			param = "?id="+treeNode.id+"&recordType="+recordType+"&isSynchronous=1";
			return "/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/getTreeNode.action"+param;
		}else{
			
			
			zinglabs.i18n.alert('tree_addNode');	
			return "/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/getTreeNode.action?recordType="+treeNodes.recordType;
		}
	    }
		function onAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus,errorThrown) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		zinglabs.i18n.alert('tree_yibushuxianyichange');
			
		zTree.updateNode(treeNode);
		}
		function checkParent(node) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		var parentNode = node.getParentNode();
		if(rootValue == null){
			node.highlight = true;
			zTree.updateNode(node);
			if(parentNode != null){
			checkParent(parentNode);
			}
		}else if (rootValue !=null && parentNode != null && node.id != rootValue.id) {
			node.highlight = true;
			zTree.updateNode(node);
			checkParent(parentNode);
		}
		}
		function onAsyncSuccess(event, treeId,treeNode, msg) {
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		if(curStatus!=null && curStatus=="expand"){
			asyncNodes(treeNode.children);
		}else  if(curStatus!=null && curStatus=="searchExpand"){
			asyncSearchNodes(treeNode.children);
		}
		if(isSearch){
			for ( var i = 0; i < asyncNode.length; i++) {
				checkParent(zTree.getNodesByParam("id", asyncNode[i].id, null)[0]);
			}
		} else {
			zTree.updateNode(treeNode);
		}
		}
		// 查询异步加载节点
		function asyncSearchNodes(nodes) {
		if (!nodes)
			return;
		curStatus="searchExpand";
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		for ( var i = 0, l = nodes.length; i < l; i++) {
			if (nodes[i].isParent && nodes[i].zAsync) {
				if (nodes[i].name.indexOf(lastName) != -1) {
					changeNode.push(nodes[i]);
				}
				asyncSearchNodes(nodes[i].children);
			} else {
				if (nodes[i].name.indexOf(lastName) != -1) {
					checkParent(nodes[i]);
				}
				//强行异步加载父节点的子节点
				zTree.reAsyncChildNodes(nodes[i], "refresh", true);
			  }
		}
	   }
		// 异步加载节点
		function asyncNodes(nodes) {
		if (!nodes)
			return;
		curStatus="expand";
		var zTree = $.fn.zTree.getZTreeObj("treeDemo");
		for ( var i = 0, l = nodes.length; i < l; i++) {
			//展开 / 折叠 指定的节点
			zTree.expandNode(nodes[i], true, false, false);
			if (nodes[i].isParent && nodes[i].zAsync) {
				asyncNodes(nodes[i].children);
			} else {
			//强行异步加载父节点的子节点
				zTree.reAsyncChildNodes(nodes[i], "refresh", true);
			}
		}
	}
		function getItemTree(jsons){
			commonTreeParam='';
			if(jsons!=null && jsons!=''){
			$("#treeDemo").html('');
			$('#text_').val('');
							$("#companyName_").val('');
							$("#departmentName_").val('');
							$("#leftClick_").val('');
							$("#rightClick_").val('');
							$("#leftParam_").val('');
							$("#rightParam_").val('');
			
			
			
			var url="/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/getTreeNode.action";
			jsons.id='';
			jsons.isSynchronous=0; //非异步
			jsons.beanName='';
			if(jsons.isSynchronous=="1"){
				setting.async.enable = true;
					//Ajax 获取数据的 URL 地址
				setting.async.url = getUrl;
					//用于对 Ajax 返回数据进行预处理的函数
				setting.async.dataFilter = ajaxDataFilter;
					//Ajax 请求提交的静态参数键值对
				setting.async.otherParam = ["roleName", getQueryString("roleName"),"beanName",'',"sortField",getQueryString("sortField")];
					//用于捕获异步加载正常结束的事件回调函数
				setting.callback.onAsyncSuccess = onAsyncSuccess;
					//用于捕获异步加载出现异常错误的事件回调函数
				setting.callback.onAsyncError = onAsyncError;
					//setting.callback.beforeAsync= zTreeBeforeAsync;
			}
				nodeType=jsons.recordType;
				treeNodes=jsons;
				ajaxFunction(url,jsons,false,function (data){
					if(data.success){
					commonTreeParam=data.data;
				   if(commonTreeParam==null||commonTreeParam==''){
				   $.fn.zTree.init($("#treeDemo"), setting,null);
				   //alert('无树结构'); 
				   $("#recordType").val(jsons.recordType);
				   $("#type_").val(jsons.type);
					recordType=$("#recordType").val();
					type=$("#type_").val();
					return;
				   }else{
					$.fn.zTree.init($("#treeDemo"), setting, commonTreeParam);					
					$("#recordType").val(jsons.recordType);
					$("#type_").val(jsons.type);
					recordType=$("#recordType").val();
					type=$("#type_").val();
					}
					}else{
					
					zinglabs.alert('没有数据，缺少必要参数！');	
					$("#recordType").val('');
					$("#type_").val('');
					}
				});
			}
			}
			function ajaxDataFilter(treeId, parentNode, responseData) {
		      	if (responseData) {
				if (isSearch) {
				asyncNode = new Array();
				var returnData = responseData.data;
				for ( var i = 0; i < returnData.length; i++) {
				if (returnData[i] != null
							&& returnData[i].name.indexOf(lastName) != -1) {
						allasyncNode.push(returnData[i]);
						asyncNode.push(returnData[i]);
					}
				}
			}
			return responseData.data;
			}
			return null;
			}
			function beforeRemove(treeId,treeNode){
			zinglabs.confirm("您确定要删除节点吗？",function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				//删除节点,同时解绑对应节点绑定的数据(包含父节点情况)
				var childNodes = treeObj.transformToArray(treeNode);
			     var Nodes ="";
			     for(i = 0; i < childNodes.length; i++) {
			          Nodes= childNodes[i].id+";"+Nodes;
			     	}
				  DelDataOfTreeDelNode(Nodes);
				  treeObj.removeNode(treeNode);
				 onRemoved(treeNode);
				 });
			     
			     return false;
			   
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
			 	var url="/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/deletezuzhijigouTreeNode.action";
	      		var p={};
	       		p.id=treeNode.id;
   				ajaxFunction(url, p, false, function(data) {
				if (data.success) {
						zinglabs.i18n.alert('system_deleteSuccess');
				
				
				}else{	
				zinglabs.alert(data.mgs);
				
				}
				});
			}
			function beforeEditName(treeId,treeNode){
			return confirm("你确定要编辑该节点吗?");
			}
			function beforeRename(treeId,treeNode,newName,isCancel){
			if(newName==''||newName==null){
			
			zinglabs.alert("您好名称不能为空");
			return false;
			}
			return true;
			}
			function onRename(e,treeId,treeNode,isCancel){
			var params={};
			params.id=treeNode.id;
			params.text=treeNode.text;
			var url="/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/updateTreeNodeName.action";
			ajaxFunction(url, params, false, function(data) {
						if (data.success) {
												zinglabs.i18n.alert('system_updateSuccess');
						
					
						}else{	
						zinglabs.alert(data.mgs);
					
						}
					});
			}
			//添加树
			function addConfigTree(){
			 	$('#myModal9 #nodeName1').val('');
				$('#myModal9 #recordType_').val('');
				$('#myModal9 #type').val('');
				$('#myModal9 #requestUrl').val('');
				$('#myModal9 #isSynchronous').val('');
			 	$("#myModal9").modal('show');
			}
			
			function addTree(){
		    var pat={};
			var url="/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/commonTreeSave.action";
			
			
			if($('#recordType_').val()==null||$('#recordType_').val()==''||$("#nodeName1").val()==''||$("#nodeName1").val()==null||$("#nodeName1").val()==undefined){
			checkName();
			checkNodeName();			
			//zinglabs.alert('分类类型不能为空');
			return;
			}
			pat.nodeName=$('#nodeName1').val();
			pat.recordType=$('#recordType_').val();
			pat.type=$('#type').val();
			pat.requestUrl=$('#requestUrl').val();
			pat.isSynchronous=$('#isSynchronous').val();
			ajaxFunction(url, pat, false, function(data) {
			if (data.success) {
								zinglabs.i18n.alert('system_saveSuccess');
				
							//zinglabs.alert('添加成功！');
				
				$("#ul li:not(':first')").remove();
				load();
				$("#treeDemo").html('');
				$("#myModal9").modal('hide');
			}else{
												zinglabs.i18n.alert('tree_recordTypeAndTypeSame');
				
				//zinglabs.alert('添加失败！您的recordType和type值已存在');
				
			}
			});
			}
			
			function updateNodeSubmit(){
			var url="/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/updateTreeNode.action";
		   	var param={};
		   		param.companyName=$('#myModal10 #companyName').val();
				param.departmentName=$('#myModal10 #departmentName').val();
				param.leftClick=$('#myModal10 #leftClick').val();
				param.leftParam=$('#myModal10 #leftParam').val();
				param.rightClick=$('#myModal10 #rightClick').val();
				param.rightParam=$('#myModal10 #rightParam').val();
				param.modleId=$('#myModal10 #modleId').val();
				param.nodeTypeUrl=$('#myModal10 #nodeTypeUrl').val();
				param.id=$('#myModal10 #id').val();
		   	ajaxFunction(url, param, false, function(data) {
					if (data.success) {
																	zinglabs.i18n.alert('system_updateSuccess');
					
						
				$("#myModal10").modal('hide');
					}else{	
						
						zinglabs.alert(data.mgs);
					}
				});
				$('#myModal10 #companyName').val('');
				$('#myModal10 #departmentName').val('');
				$('#myModal10 #modleId').val('');
				$('#myModal10 #nodeTypeUrl').val('');
				$('#myModal10 #leftClick').val('');
				$('#myModal10 #leftParam').val('');
				$('#myModal10 #rightClick').val('');
				$('#myModal10 #rightParam').val('');
				$('#myModal10 #id').val('');
			}
	      	//添加根节点
			function addRootNode(){
			var url="/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/addTreeNode.action";
			if(nodeType==undefined || nodeType==null || nodeType==''){
																					zinglabs.i18n.alert('tree_chooseRootType');
				
				
				return;
			}
		  	params.recordType=nodeType;
		  	params.createUser="ZSKadmin";
		  	params.text=$('#rootText').val();
		   	ajaxFunction(url, params, false, function(data) {
					if (data.success) {
					zinglabs.i18n.alert('system_saveSuccess');
						
						
					}else{	
					zinglabs.alert(data.mgs);
						
					}
				});
			}
			function zTreeOnClick(event, treeId, treeNode) {
	 	 		parentId=treeNode.id;
	   			jsons=treeNode;
	   			WTTreenodeId=treeNode.id; //选中节点赋值
	   			FindTreeToData();
   			}
          	//添加子节点
          	function addNode_(){
          	        $('#text_').val('');
					$("#companyName_").val('');
					$("#departmentName_").val('');
					$("#leftClick_").val('');
					$("#rightClick_").val('');
					$("#leftParam_").val('');
					$("#rightParam_").val('');
			//if(jsons==null || jsons==""){			
			//zinglabs.alert('请选择节点');
			//return;
			//}
			    try{
			    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				var nodes = treeObj.getSelectedNodes();
			    }catch(e){
			    zinglabs.i18n.alert('tree_chooseNode');
			    return;
			    }
				
			 	if(nodes!=''&&nodes!=null&&nodes.length==1){
			 	 child=0;
			 	$("#myModal15").modal('show');		 	
			 	}else if(nodes==''||nodes==null){
			 						zinglabs.i18n.alert('tree_chooseNode');
			 	
			 	}else{
			 	//zinglabs.alert('操作错误');
			 						zinglabs.i18n.alert('role_operationFailed');
			 	
			 	}
			 	 //zinglabs.confirm("你确定要添加子节点吗？",function (){      
				   //$("#myModal15").modal('show');
			    //});
          	}
          	function addNode1_(){
          			$('#text_').val('');
					$("#nodeTypeUrl_").val('');
					$("#modleId_").val('');
					$("#leftClick_").val('');
					$("#rightClick_").val('');
					$("#leftParam_").val('');
					$("#rightParam_").val('');         
          	if(recordType==''||recordType==null){        	
          	zinglabs.i18n.alert('tree_nochooseTree');
          	
          	return;
          	}         	
          	       child=1;
				   $("#myModal15").modal('show');			             	         	
          	}
          	function addNodeRootOrChild(){
          	if(child==0){
          	addNode();
          	}
          	if (child==1){
          	addNode1();
          	}
          	
          	}
			function addNode(){
				var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
				var nodes = treeObj.getSelectedNodes();
			 	if(nodes!=''&&nodes!=null&&nodes.length==1){						 	
			 	//var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			 	var newNode = {};
			 		var recordType=$("#recordType").val();
			 		var text=$("#text_").val();
		 		if(text==""||text==null){
		 		    
		 		              	checkText();
		 		    		 
		 		 	return;
		 		}			 		
			 		var url="/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/addTreeNode.action";
			 		var params={};
				  	params.recordType=recordType;
				  	params.createUser="ZSKadmin";
				  	params.text=text;
				  	params.parentId=nodes[0].id;
				  	params.nodeTypeUrl=$("#nodeTypeUrl_").val();
			  	params.modleId=$("#modleId_").val();
			  	params.leftClick=$("#leftClick_").val();
			  	params.rightClick=$("#rightClick_").val();
			  	params.leftParam=$("#leftParam_").val();
			  	params.rightParam=$("#rightParam_").val();
				   	ajaxFunction(url, params, false, function(data) {
							if (data.success) {
							//alert(JSON.stringify(data));
							newNode.id=data.data;
									 		              	zinglabs.i18n.alert('system_saveSuccess');

								child=-1;
								$('#text_').val('');
							$("#modleId_").val('');
							$("#nodeTypeUrl_").val('');
							$("#leftClick_").val('');
							$("#rightClick_").val('');
							$("#leftParam_").val('');
							$("#rightParam_").val('');
								$('#recordType').val(recordType);
							}else{	
								zinglabs.alert(data.mgs);								
							}
						});
						 var nodess;						
						if (nodes[0].zAsync){
						newNode.text=text;
                        nodess = treeObj.addNodes(nodes[0],newNode, false); 
                       
			 		$("#myModal15").modal('hide');
			 		return;                  
               }
               else{
              //nodes.reAsyncChildNodes(jsons, "refresh",'false');                   
			 		$("#myModal15").modal('hide');
			 		return;
              }
			}
			else{
			zinglabs.i18n.alert('tree_chooseNode');
			
			}
			}
		function addNode1(){
		
		 	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		 	var newNode = {};
		 		var recordType=$("#recordType").val();
		 		var text=$("#text_").val();
		 		if(text==""||text==null){
		 
		 					checkText();
		 				 		
		 		 return;
		 		}
		 		var url="/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/addTreeNode.action";
		 		var params={};
			  	params.recordType=recordType;
			  	params.createUser="ZSKadmin";
			  	params.text=text;
			  	params.parentId="";
			  	params.nodeTypeUrl=$("#nodeTypeUrl_").val();
			  	params.modleId=$("#modleId_").val();
			  	params.leftClick=$("#leftClick_").val();
			  	params.rightClick=$("#rightClick_").val();
			  	params.leftParam=$("#leftParam_").val();
			  	params.rightParam=$("#rightParam_").val();
			   	ajaxFunction(url, params, false, function(data) {
						if (data.success) {
						newNode.id=data.data;
							child=-1;
									 					zinglabs.i18n.alert('system_saveSuccess');
							
							$('#text_').val('');
							$("#nodeTypeUrl_").val('');
							$("#modleId_").val('');
							$("#leftClick_").val('');
							$("#rightClick_").val('');
							$("#leftParam_").val('');
							$("#rightParam_").val('');
							$('#recordType').val(recordType);
						}else{	
							zinglabs.alert(data.mgs);							
						}
					});
					newNode.text=text;
		 		newNode = treeObj.addNodes(null, newNode,true);
		 		//getItemTree(jsons1);
		 		//jsons=[];
		 		$("#myModal15").modal('hide');
		 		return;
		}
    	//删除节点
		function deleteNodes(){
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			if(treeObj==null || treeObj==""){
													 					zinglabs.i18n.alert('tree_chooseDelTree');
				
				
				return;
			}else{
	   		var nodes = treeObj.getSelectedNodes();
			}
	   		if(nodes.length<=0){
	   																 					zinglabs.i18n.alert('tree_chooseNode');
	   			
	   			return ;
	   		}
		var url="/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/deletezuzhijigouTreeNode.action";
		objs.nodeId=getdelParams(ids,jsons).join(',');
	   	ajaxFunction(url, objs, false, function(data) {
				if (data.success) {					
						   	zinglabs.i18n.alert('system_deleteSuccess');
						   							
				}else{					
									zinglabs.alert(data.mgs);					
				}
			});
		}
		//获取删除参数
		
		function getdelParams(ids,node){
			ids.push(node.id);
			if (node.isParent){
					for(var obj in node.children){ 
						getdelParams(ids,node.children[obj]);
					}
			   }
			return ids;
		}
		//修改节点
		function updateNode(){
				if(jsons==null || jsons==""){
										   	zinglabs.i18n.alert('tree_chooseUpdateNode');
					
					return;
				}
				var url="/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/selectTreeNodeById.action";
			var p={};
			p.id=jsons.id;
		   	ajaxFunction(url, p, false, function(data) {
					if (data.success) {
					jsons=data.data;
			$('#myModal10 #companyName').val(jsons[0].companyName);
			$('#myModal10 #departmentName').val(jsons[0].departmentName);
			$('#myModal10 #leftClick').val(jsons[0].leftClick);
			$('#myModal10 #leftParam').val(jsons[0].leftParam);
			$('#myModal10 #rightClick').val(jsons[0].rightClick);
			$('#myModal10 #rightParam').val(jsons[0].rightParam);
			$('#myModal10 #id').val(jsons[0].id);
			$('#myModal10 #modleId').val(jsons[0].modleId);
			$('#myModal10 #nodeTypeUrl').val(jsons[0].nodeTypeUrl);
			 $("#myModal10").modal('show');
			 jsons='';
					}else{	
					zinglabs.alert(data.mgs);
						
					}
				});
		}
		//删除树
		function deleteTree(){
			if(treeNodes!=null&&treeNodes!=''){
				//var a=confirm("您确定要删除树吗？");
				
				zinglabs.confirm("您确定要删除树吗？",function (){      
				    var params={};
		 		params.nodeId=treeNodes.nodeId;
				params.recordType=treeNodes.recordType;
				params.type=treeNodes.type;
				//alert(JSON.stringify(params));
				var url="/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/commonTreeDelete.action";
				var urls="/"+window.top.PRJ_PATH+"/"+SP+"/ZKMCommonTree/commonTreeDeleteByRecordType.action";
				ajaxFunction(url, params, false, function(data) {
					if (data.success) {
						ajaxFunction(urls, params, false, function(data) {
					if (data.success) {
																   	zinglabs.i18n.alert('system_deleteSuccess');
						
							
					}else{	
						zinglabs.alert(data.mgs);
						
					}
				});
					}else{	
						zinglabs.alert(data.mgs);
						
					}
				});
				
				$("#ul li:not(':first')").remove();
				
			    load();
			    $("#treeDemo").html('');
			    });
				
				
		 	
			}else{
			zinglabs.i18n.alert('tree_chooseTreeNode');
			
			}
			 
		}
		
		function zTreeBeforeAsync(treeId, treeNode) {
	        	treeNode.children=treeNode.data;
			    return true;
	   }
	   function showModel(){
	   
	   if(recordType==''||recordType==null){
	    zinglabs.i18n.alert('tree_chooseTreeNode');
	    return;
	   }
	   $("#myModal12").modal('show');
	   
	    $('#myModal12 #recordType12').val(recordType);
	   $('#myModal12 #type12').val(type);
	   }
	   function getAddress(){	
	  
	     var recordType12=$('#myModal12 #recordType12').val();
	     var type12=$('#myModal12 #type12').val();
	     if(recordType12==''||recordType12==null){
	       				zinglabs.i18n.alert('tree_chooseTreeNode');
	       						
	       
	       return;
	     }
	     var address="/"+window.top.PRJ_PATH+"/zkmCommonTree.html?recordType="+recordType12;
	     if($('#myModal12 #type12').val()!=''&&$('#myModal12 #type12').val()!=null){
	       address+="&type="+$('#myModal12 #type12').val();
	     }
	     if($('#myModal12 #showButton12').val()!=''&&$('#myModal12 #showButton12').val()!=null){
	       address+="&showButton="+$('#myModal12 #showButton12').val();
	     }
	     if($('#myModal12 #closeFn12').val()!=''&&$('#myModal12 #closeFn12').val()!=null){
	       address+="&closeFn="+$('#myModal12 #closeFn12').val();
	     }
	     if($('#myModal12 #reFillFn12').val()!=''&&$('#myModal12 #reFillFn12').val()!=null){
	       address+="&reFillFn="+$('#myModal12 #reFillFn12').val();
	     }
	     if($('#myModal12 #mode12').val()!=''&&$('#myModal12 #mode12').val()!=null){
	       address+="&mode="+$('#myModal12 #mode12').val();
	     }
	     if($('#myModal12 #pchm12').val()!=''&&$('#myModal12 #pchm12').val()!=null){
	       address+="&pchm="+$('#myModal12 #pchm12').val();
	     }
	     if($('#myModal12 #cchm12').val()!=''&&$('#myModal12 #cchm12').val()!=null){
	       address+="&cchm="+$('#myModal12 #cchm12').val();
	     }
	     $('#myModal12 #recordType12').val('');
	     $('#myModal12 #type12').val('');
	     $('#myModal12 #showButton12').val('');
	     $('#myModal12 #closeFn12').val('');
	     $('#myModal12 #reFillFn12').val('');
	     $('#myModal12 #mode12').val('');
	     $('#myModal12 #pchm12').val('');
	     $('#myModal12 #cchm12').val('');
	     $("#myModal12").modal('hide');
	     window.clipboardData.setData('text', address); 
	     //return address;
	     
	   }
	  
	   function checkName(){
	if($("#recordType_").val()==''||$("#recordType_").val()==null||$("#recordType_").val()==undefined){
		CHECK_name =false ;
		showTooltip('recordType_Div','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('recordType_Div');
	}
}
function  checkNodeName(){
	if($("#nodeName1").val()==''||$("#nodeName1").val()==null||$("#nodeName1").val()==undefined){
		CHECK_name =false ;
		showTooltip('nodeName1_Div','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('nodeName1_Div');
	}
}
function  checkText(){
	if($("#text_").val()==''||$("#text_").val()==null||$("#text_").val()==undefined){
		CHECK_name =false ;
		showTooltip('text-Div','此项不能为空！') ;
	}else{
		CHECK_name =true ;
		hideTooltip('text-Div');
	}
}
//验证未通过样式
function showTooltip(id,title){
	$("#"+id).attr('title',title) ;
	$("#"+id).tooltip({
		placement:'bottom'
	}) ;
	$("#"+id).tooltip('show') ;
	$("#"+id).addClass('error') ;
	
}
//还原样式
function hideTooltip(id){
	$("#"+id).tooltip('destroy') ;
	$("#"+id).removeClass('error') ;
}