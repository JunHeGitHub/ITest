//-------------------------拖拽------------------------------------------
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
	
	function beforeDrag(treeId, treeNodes) {
			for (var i=0,l=treeNodes.length; i<l; i++) {
				if (treeNodes[i].drag === false) {
					return false;
				}
			}
			return true;
		}
	
	
	
	
	// 拖拽前判断是否允许拖拽
	 function beforeDrop(treeId, treeNodes, targetNode, moveType) {
		 return targetNode ? targetNode.drop !== false : true;
	 } 
		// 拖拽操作结束时调用函数
	 function onDrop(event,treeId,treeNodes,targetNode,moveType){
	 		var params = {}; 
	 		var url ="";
	 		 params.id =  "'"+treeNodes[0].id+"'";
	 		if(treeNodes[0].propUseOrNot != undefined){
				params.categoryId = "'"+targetNode.id+"'";	
				url="updatePropNode.action";  	 	
	 		}else if(treeNodes[0].categroyUseOrNot != undefined){
	 			params.parentId = "'"+targetNode.id+"'";
	 			url="updateCateNode.action";
	 		}
	 		
	 		
	 		
	 		ajaxFunction("/" +'ZDesk' + "/" + 'ZKM' +"/Category/" +url ,params, false,  function(data) {
				if (data.success) {
					//  loadTree();
				}else{	
					alert(data.mgs); 
				}
			});
	 		
	 		
	    }
	//------------------------右键菜单------------------------------
	function showRMenu(type, x, y) {
		$("#rMenu ul").show();
		/*if (type=="root") {
				$("#m_del").hide();
				$("#m_check").hide();
				$("#m_unCheck").hide();
			} else {
				$("#m_del").show();
				$("#m_check").show();
				$("#m_unCheck").show();
			}*/
		rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

		$("body").bind("mousedown", onBodyMouseDown);
	}
	
	function onBodyMouseDown(event){
			if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
				rMenu.css({"visibility" : "hidden"});
			}
		}
	
	function hideRMenu() {
			if (rMenu) rMenu.css({"visibility": "hidden"});
			$("body").unbind("mousedown", onBodyMouseDown);
	}
	
	function zTreeOnRightClick(event, treeId, treeNode) {
		if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
				$.fn.zTree.getZTreeObj("treeDemo").cancelSelectedNode();
				showRMenu("root", event.clientX, event.clientY);
		} else if (treeNode && !treeNode.noR) {
			$.fn.zTree.getZTreeObj("treeDemo").selectNode(treeNode);
			showRMenu("node", event.clientX, event.clientY);
		}
		
		//  当前右键的treeNode
		rightClickNode = treeNode;
		
	}

	// 复制
	function copyNode(){
		hideRMenu();
		
		parseNodes = rightClickNode;
		
	}
	
	//粘贴
	function parseNode(){
		 hideRMenu(); 
		 
		var params={};
		
		params.primarykeyType = "uuid"; // 主键为 UUID
		if(parseNodes.categroyUseOrNot != undefined){
			var uuid = Math.uuidFast();
			
			var str ="[";	
			
			 str +="{\"id\":\""+uuid+"\",\"parentId\":\"" +rightClickNode.id+"\",\"nodeName\":\""+parseNodes.text+
			        "\",\"categroyUseOrNot\":\"1\",\"companyId\":\"1\"}";             
			 
				
			if(parseNodes.children != undefined){
				str += ",";
				for(var i = 0; i< parseNodes.children.length; i++){
					str +="{\"id\":\""+Math.uuidFast() +"\",\"parentId\":\"" +uuid+"\",\"nodeName\":\""+parseNodes.children[i].text+
			        "\",\"categroyUseOrNot\":\"1\",\"companyId\":\"1\"}";            
			 	
			 		
			 		if(parseNodes.children.length - 1 != i){
			 		 str +=",";
			 		}
			 
				}
			}
				str+="]";
				str =  JSON.parse(str); 
		    params.columnValues=str;
		    params.tableName=categoryTableName; 
		}else if(parseNodes.propUseOrNot != undefined){
		    params.tableName=propTableName; 
			var uuid = Math.uuidFast();
			
			var str ="[";	
			
			
			 str +="{\"id\":\""+uuid+"\",\"categoryId\":\"" +rightClickNode.id+"\",\"propName\":\""+parseNodes.text+
			        "\",\"requiredOrNot\":\"1\",\"propUseOrNot\":\"1\",\"createPerson\":\"张三\",\"createDate\":\"2015-03-10\",\"companyId\":\"1\"}";            
			 
				
			if(parseNodes.children != undefined){
				str += ",";
				for(var i = 0; i< parseNodes.children.length; i++){
					str +="{\"id\":\""+Math.uuidFast() +"\",\"categoryId\":\"" +uuid+"\",\"propName\":\""+parseNodes.children[i].text+
			        "\",\"requiredOrNot\":\"1\",\"propUseOrNot\":\"1\",\"createPerson\":\"张三\",\"createDate\":\"2015-03-10\",\"companyId\":\"1\"}";            
			 	
			 		
			 		if(parseNodes.children.length - 1 == i){
			 		 str +=",";
			 		}
				}
			}
				str+="]";
		    str =  JSON.parse(str); 
		    params.columnValues=str;
		}
		 commonInsertOrUpdate(params,true,function(data){
	        try{
	             if (data) {
	                 if(data.success){
	                    try{
	                       //动态追加新增的节点
						    loadTree();
	                    }catch(e){
	                       // alert("Exception need see log1 "+e.name + ": " + e.message);
	                    }
	                    //  $('#'+window).modal('hide');
	                  }else{
	                     //  alert("Exception add failed2  "+data +" "+("Items" in data)); 
	                   }
	              }
	         }catch(exx){
	        // cons ole.log(data);
	        //  	alert("Exception add failed3  "+data +" "+("Items" in data));
	         }
	    });
	}
	
	//禁用分类或属性；
	function disableNode(){
		
		zinglabs.confirm("您确定要删除节点吗？",function(){
			//  alert(rightClickNode.id);
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getCheckedNodes(true); 
		 // 	alert(nodes);
			if(nodes == null || nodes == ""){
				zinglabs.alert("请选择要禁用的节点");
				return;
			}
			
			var params={};
			var ids="";	
		
			for(var i = 0;i<nodes.length;i++){
				ids += "'"+nodes[i].id+"'"; 
				if(nodes.length -1 != i){
					ids +=",";
				}
			}
			params.id = ids;
			params.propUseOrNot = 0;
			params.categroyUseOrNot = 0
		    ajaxFunction("/" +'ZDesk' + "/" + 'ZKM' +"/Category/disableTreeNode.action",params, false,  function(data) {
				if (data.success) {
					loadTree();
				}else{	
					alert(data.mgs);
				}
			});
		});								 
	}
	
	//-----------------------------------------------------------------------
	
	function loadForm(treeNode){
		
		var nodeLevel = treeNode.level;
		
		var htmlStr="";
		for(var i = nodeLevel + 1; i  > 0 ; i--){  
			var str = "<br/><div class='span12'>"+
                      "<span class='span1' style='padding-top: 8px;padding-bottom: 4px;' >"+treeNode.text+"</span>"+
                      "<input type='text' style='height:30px; width:200px;' id='nodeName' name='nodeName' >"+
                      "</div>";
               htmlStr = str + htmlStr;        
                        
			treeNode = treeNode.getParentNode();
		}
		
		 $("#detail").html("<form id='updateForm'>" + htmlStr + "</form>" ); 
	}	
	
	