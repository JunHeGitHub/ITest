
/**
*
*
*
*/
//全局设置
	var goodCategory; // 全局分类Id


	//=======左侧树设置项=====
		var setting = {  
  			data: {
				simpleData: {
					enable: true,
					idKey:'id',
					piKey:'pid'
				},
				key:{name:"text"}
			},
			callback: {
				onClick: clickNode
			}
			
	    };  
	//=======================


	//=======================加载树=======
	function initTree(){
		var treeNodes,params="";  
		ajaxFunction("/" + parent.PRJ_PATH + "/" + 'ZKM' +"/Category/getTreeRoot.action", params, false, function(data) {
			if (data.success) { 
				treeNodes=data.data; 
				$.fn.zTree.init($("#treeDemo"), setting, treeNodes);
			}else{	
				zinglabs.alert(data.mgs);
			}
		});
	}


	/*
	* 清空全局变量存放的数据，每次draw dataTables时调用
	*/
	function clearData(){
		paramData = null;
		detailGoodsData = null;
	}


	//=======循环创建多个图片div========
	function initImg(){
		var imgList = "";
		   for(var i = 0; i<5;i++){
		    imgList += "<div  class='backImg' style='width:100px;height:100px;border:1px solid #DDDDDD;float:left;margin-left:5px;'>" + 
		                  "<img   style='width:100px;height:100px;'  id='image_"+i+"'   " + 
		                  "   onclick = 'upImage(this.id);'  />" +
		               "</div>";
		   }
		$("#imgs").html(imgList);	 
	}



	//======首页主商品显示
	function loadGoods(){
		goodsGrid = $('#goodsGrid').DataTable({
		         deferRender: true,
		         processing: false,
		         serverSide: true,
		         ajax: function (data, callback, settings) {
		               var params = searchForm();
		               
		               //条件查询
		               /*
		               if(!BIsNullVal($("#goodsCateGory").val())){
		               		params.goodsCateGory =  $("#goodsCateGory").val();
		               }else{
			               params.goodsCateGory = null;
		               }*/
		               params.flag="mainGoods";
					   params.rows=settings._iDisplayLength;
					   params.offset=settings._iDisplayStart; 
					   ajaxFunction("/" +parent.PRJ_PATH + "/" + 'ZKM' + "/goodsManager/selectMainGoods.action", params, false, function(data) {
							 if(data&&data.data){
							 		paramData = null  // 清空已存在的数据；
					                  var pdata={};
					                  pdata.data=data.data;
					                  pdata.recordsTotal=data.total;
					                  pdata.recordsFiltered=data.total;
					                  pdata.draw=settings.iDraw;
					                  callback(pdata);
					         }
						});
			 	 },
				pagingType: "full_numbers",
		        pageLength: $("#showNumbers").val(), //获取页面上的当前显示多少数据  
		        bPaginate: true, //翻页功能 
		        bLengthChange: false, //改变每页显示数据数量
		        bFilter: true, //过滤功能
		        bSort: true, //排序功能
		       //  bInfo: true,//页脚信息
		      //   bAutoWidth: true, //自动宽度
		        sPaginationType: "bootstrap",//分页样式
		        dom: 'TRlrtip',
		        scrollCollapse: true,
				sScrollX:"100%",
		        columns: [
		              {title:"表序号" ,data: 'id', visible: false,width:150},
		              {title:"组商品号" ,data: 'groupId', visible: false,width:150},
		              {title:"主商品号" ,data: 'goodsId', visible: false,width:150},
		              {title:"商品编号" ,data: 'goodsCode', visible: false,width:150},
		              {title:"商品名称" ,data: 'goodsName',width:150},
		              {title:"商品标题" ,data: 'goodsTitle',width:150},
		              {title:"库存" ,data: 'goodsCount',width:150},                                  
		              {title:"商品成本价" ,data: 'goodsCostPrice',width:150},
		              {title:"商品销售价" ,data: 'goodsSalePrice',width:150, "bSortable": true},
		              {title:"商品类型" ,data: 'goodsType',width:150},                                  
		              {title:"状态" ,data: 'goodsStatus',width:150},                                  
		              {title:"商品备注" ,data: 'goodsMark',width:150},     
		              {title:"创建时间" ,data: 'createDate',width:150},                                   
		              {title:"企业编号" ,data: 'companyId',  visible: false,width:150},
		              {title:"数据状态" ,data: 'dataStatus',  visible: false,width:150},
		              {title:"商品描述" ,data: 'goodsDesc',  visible: false,width:150},
		              {title:"商品图片" ,data: 'goodsImgs',  visible: false,width:150},
		              {title:"商品标签" ,data: 'goodsLabel',  visible: false,width:150},
		              {title:"商品分类" ,data: 'goodsCateGory',  visible: false,width:150},
		              {title:"显示价格" ,data: 'goodsShowPrice',  visible: false,width:150},
		              {title:"商品属性" ,data: 'goodsProp',  visible: false,width:150}
		               ],
		        oLanguage: {
		             infoFiltered:function(){
		             
		             },
		            "sLengthMenu": "每页显示 _MENU_条",
		            "sZeroRecords": "没有找到符合条件的数据",
		            "sProcessing": "&lt;imgsrc=’./loading.gif’ /&gt;",
		            "sInfo": "当前第 _START_ - _END_ 条　共计 _TOTAL_ 条",
		            "sInfoEmpty": "没有记录",
		            "sInfoFiltered": "(从 _MAX_ 条记录中过滤)",
		            "sSearch": "搜索：",
		            "oPaginate": {
		                "sFirst": "首页",
		                "sPrevious": "前一页",
		                "sNext": "后一页",
		                "sLast": "尾页"
		            }
		        }, tableTools: {
		        	//获取点击的行
		            fnRowSelected: function ( nodes ) {
		              var index=nodes[0]._DT_RowIndex;	
		               paramData = goodsGrid.row(index).data();
		               
		               $("#selectTitle").html(paramData.goodsName+"所有规格");
		               detailGoodsGrid.draw(true); 
		               
		            },
		             sRowSelect:'os',
		            aButtons: []
		        },"fnCreatedRow" : function(nRow,aData,iDataIndex){ // 格式化行
		        	$('td:eq(5)',nRow).html(aData.goodsType == "1"?"虚拟类":"实物类"); 
		        	$('td:eq(6)',nRow).html(aData.goodsStatus == "1"?"上架":"下架");
		        	//$('td:eq(8)',nRow).html(aData.createDate.split(" ")[0]);
				}
		    });
		   //  new $.fn.dataTable.FixedColumns( gt );  table.columns.adjust().draw(); 
	}


	//====查询表单配置
	function searchForm(){
		var params = {};
		
		params.goodsCateGory = $("#searchCateGory").val();
		params.goodsName = $("#searchName").val();
		params.goodsTitle = $("#searchTitle").val();
		params.goodsStatus = $("#searchStatus").val();
		params.goodsType = $("#searchType").val();
		
		return params;
	
	}
	
	//查询按钮；
	function tableSearch(){
		goodsGrid.draw(true);
	}

	//查询重置
	function chongzhi(id){
		  $("#"+id+" input,#"+id+" select[name]").val('');
	}

	//删除操作；
	function delGoods(gridData,grid){
		var params = {};
		console.log(gridData);
		if(BIsNullVal(gridData)){
			zinglabs.alert("请选择要删除的数据");
			return;
		}
		// console.log(gridData);
		params.flag="";
		if(gridData.id == gridData.goodsId){
			params.flag="main";
			//zinglabs.alert("该商品为主商品，删除前请确认该商品下没有其它商品！！！");
			//return;
		}
		
		zinglabs.confirm("您确定要删除该商品吗？",function(){
			
			params.groupId = gridData.groupId;
			params.goodsId = gridData.id;
			params.id = gridData.id;
			ajaxFunction("/" + parent.PRJ_PATH + "/" + 'ZKM' +"/goodsManager/updateOrDelGoodsInfo.action",params, false, function(data) {
					if (data.success) {
						 grid.row('.selected').remove().draw(false);
					}else{	
						zinglabs.alert(data.mgs);
					}
			}); 
		});
	}

	  //追加商品  
	 function appendGoods(){
	 	if(BIsNullVal(paramData)){
			zinglabs.alert("请选择要追加的主商品"); 
			return;
		}
	 
	 	paramValue = paramData;
	 	
	 	/*
	 	for(var i=0;i<5;i++){ 
	        $("#image_"+i)[0].src  = $("#image_"+i)[0].src;   
	    }*/
	 	
	 
	 	editor.execCommand('cleardoc');  //清空编辑器；
	    $('#insertGoods')[0].reset(); //表单清空；
	    		
	    $("#groupId").val(paramValue.groupId);
	    $("#goodsId").val(paramValue.goodsId);
	    $("#goodsCode").val(Math.uuidFast());
	    $("#goodsName").val(paramValue.goodsName);
	    $("#goodsCateGory").val(paramValue.goodsCateGory);
	    $("#goodsName").attr("disabled",true);   //禁用商品名称项；
	    
	     loadProp(paramValue.goodsCateGory); 
	    
	     paramStatus = "add";
	     
	     $("#addGoodsWindow").modal('show'); //关闭window;
	 
	 } 



//==================异步加载树节点====================================
	function ansyTree(node){ 
		var params={};
		params.id=node.id; 
		params.flag="all"; //参数自定义，只是一个标识，判断是否加载全部；
		ajaxFunction("/" + parent.PRJ_PATH + "/" + 'ZKM' +"/Category/getTreeRoot.action",params, false, function(data) {
				if (data.success) {
					treeNodes = data.data;
					// 查询不是空赋值；
					if(treeNodes != "" ){
						var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
						node = treeObj.addNodes(node, treeNodes);
						treeObj.expandAll(true); //展开点击节点下的所有节点
					}
				}else{	
					zinglabs.alert(data.mgs);
				}
		}); 
		/*
		params.goodsId = "B8C11FEA-EE75-4518-A695-7CEDE8EB54C9";
		
		ajaxFunction("/" + parent.PRJ_PATH + "/" + 'ZKM' +"/goodsManager/selectPropByGoodsId.action",params, false, function(data) {
			alert(data.success);
			alert(data.data[1].id);
			console.log(data.data);
			alert(data.data[1].goodsDesc);
			
		});*/
	}
//==========================



//=============点击树节点操作=============================================
	function clickNode(event, treeId, treeNode){
		//当前点击的节点没有子节点，加载一次
		if(treeNode.children == undefined){ 
			ansyTree(treeNode);
		}

		//默认商品分类为点击的节点分类；
		// $("#goodsCateGory").val(treeNode.text); 
		
			//var text = "";
			var id = "";
			while(treeNode != null){
				id += "'"+treeNode.id+"'";
				//text += treeNode.text; 
				// text = treeNode.text + text;
				treeNode =	treeNode.getParentNode();
				if(treeNode != null){
					id +=",";
					//text+="-";
					// text = "-"+ text;
				}
			}
			
		$("#goodsCateGory").val(id);
		$("#searchCateGory").val(id);
		
		 //动态生成表单；
		 loadProp(id);
		 
		 // goodCategory = id;
		 //根据分类id draw()goodsgrid
		 goodsGrid.draw(true);
		 
    }


	//====修改时通用赋值====
	function setPanelVal(pVars){
	    $('#'+pVars.id+' :input').each(function(index,obj) {
	        var type = obj.type;
	        var tag = obj.tagName.toLowerCase();
	      if(("isClear" in pVars) && pVars.isClear=='true'){
	            if (type == 'text' || type == 'password' || tag == 'textarea')
	                $(obj).val('');
	            else if (type == 'checkbox' || type == 'radio')
	                $(obj).attr('checked',false);
	            else if (tag == 'select')
	                $(obj).attr('selectedIndex',-1);
	        }else{
	            var vTmp=pVars.data[this.name];
	            if(BIsNullVal(vTmp)){
	                vTmp='';
	            }
	            if (type == 'text' || type == 'password' || tag == 'textarea')
	                $(obj).val(vTmp);
	            else if (type == 'checkbox' || type == 'radio'){
	                $(obj).attr('checked',vTmp==''?false:vTmp);
	            }
	            else if (tag == 'select'){
	            	//当下拉的时候设置为值，不设置下标。原方法是设置下标
	            	$(obj).val(vTmp);
	               //  $(obj).attr('selectedIndex',vTmp==''?-1:vTmp);
	            }
	        }
	    });
	}

	//===加载属性列表，参数为商品分类。goodsCateGory
    function loadProp(id){
    	var params = {};
    	params.categoryId = id;
    	params.flag =  "flag";
    	params.saleOrBuy = "1"; //区分为买家属性还是卖家属性
    	params.tableName = "tb_Prop"; //属性表 
    	ajaxFunction("/" + parent.PRJ_PATH + "/" + 'ZKM' +"/Category/getAllProp.action",params,false,  function(data) {
					// alert(111);
				if (data.success) {
					treeNodes = data.data;
					var str = ""; 
					if(treeNodes.length > 0){
						keyId = "";  
						for(var i = 0; i< treeNodes.length; i++){
							if(treeNodes[i].children != null && treeNodes[i].children != ""){
								// alert(111);
							      keyId += treeNodes[i].propName +",";  
							         str += "<div class='span12'  style='padding-top:5px;display:inline;float:left;'>" +
					                          "<span class='span5' style='padding-top: 8px;padding-bottom: 4px;' >"+treeNodes[i].propName+":</span>"; 
							        			
							      if(treeNodes[i].showType == "select"){
							      	
					                   str +="<select id='"+treeNodes[i].propName +"' name='"+treeNodes[i].propName+"' style='height:30px; float:left;width:200px;'>"+
					                         	"<option value=''>请选择</option>"; 
						                            for(var j = 0 ; j < treeNodes[i].children.length;j++){
						                              str += "<option value='"+treeNodes[i].children[j].propName+"'>"+treeNodes[i].children[j].propName+"</option>";
													}									                  
												//str +="</select><span style='color:red;display:none;' class='"+treeNodes[i].children[j].propName+"'>请选择"+treeNodes[i].children[j].propName+"</span>";
												str +="</select>";
												str +="<span style='color:red;display:none;' class="+treeNodes[i].propName+">请选择"+treeNodes[i].propName+"</span>"
							      }else if(treeNodes[i].showType == "text"){
							      		str +=" <input type='text' id='"+treeNodes[i].propName +"' name='"+ treeNodes[i].propName +"'  style='height:30px; float:left;width:200px;'  >"; 
							      		 str += "<span style='color:red;display:none;' class='"+treeNodes[i].propName+"'>请输入"+treeNodes[i].propName+"</span>"
							      }  
					                 
					              str += "</div>";  
							        
							}
						}
						 keyId = keyId.substring(0,keyId.length -1);
					}
						$("#prop").html(str);
				}else{	
				 // alert(111);
					zinglabs.alert(data.mgs);
				}
				// alert(keyId);
		});
    
    }


	//====================验证商品名称是否存在========================
	function checkGoodsName(){
		var params = {};
		 params.goodsName = $("#goodsName").val();
		 
		 params.goodsName =params.goodsName.replace(/^\s+|\s+$/g,""); //清除两边空格 
		 
		 if(BIsNullVal(params.goodsName)){
		 	$(".str").css("display","block");   
		 	$(".str").html("商品名称不能为空"); 
		 	$("#goodsName").val('');  
		 	return;
		 }
		ajaxFunction("/" + parent.PRJ_PATH + "/" + 'ZKM' +"/goodsManager/selectPropByGoodsId.action",params, false, function(data) {
		 		if(data.success){
		 			 $(".str").css("display","block");   
		 			 $(".str").html("该商品名称已存在"); 
		 		}else{
		 		
		 			$(".str").css("display","none");
		 			$(".str").html("");
		 			$("#goodsName").val(params.goodsName); 
		 		}	
		 	});
	}
	

	//=======非空判断； 
	function BIsNullVal(value) {  
	    return typeof(value)=="undefined" || value==null ||  (typeof(value)=='string' &&(value=="undefined" || value=='' || value=='null' )) ||  (typeof(value)=='boolean' &&value==false  );
	};


		
	//获取焦点时验证价格
	function cancelLast(num){
		if(num == "" || num == null){
			return "";		
		}else{
			return window.parseFloat(num); 
		}
	}
	
	
	//判读是否带有.00,如果不带则追加上；
	function addMin(num,cla){ 
		//$("."+cla).css('display','none'); 
		
		if(num == "" || num == null){
			return "";
		}else{
			//num = num.replace('/^\d+(\.\d{2})?$/',''); 
			//alert(num);
			if(num < 0){
				//$("."+cla).html("价格必须为正数");
				//$("."+cla).css('display','block');
				num = -(num);
			}
		 
			return window.parseFloat(num).toFixed(2); 
		}
	}
	
	//显示查询菜单
	function showSearchItems(id){
		$("#"+id).slideToggle(200);
	}

	//选择显示条数
	function setPageNum(number){
		// var pageSize=$("#showNumber").val();
		goodsGrid.page.len(number).draw();
		
	}


