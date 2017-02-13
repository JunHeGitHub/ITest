/*
获取菜单
json数据格式
*/
function getMenu(t){
			var type = t;
			var jsons={};	
			var rData=[];
			var resultData={"length":0};
			var count=0;
			var url="/"+parent.PRJ_PATH+"/TuWenMsg/ZKMCommonTree/getCaidanTreeNode_geshu.action";
			jsons.type=type;
				ajaxFunction(url,jsons,false,function (data){
					if(data.success){
						var allData=eval('(' + data.data + ')');
						var menuData= allData.menuData;
						var subMenuData= allData.subMenuData;
						count=menuData.length;
						for(var i=0;i<menuData.length;i++){
							var q={};
							q.name=menuData[i]["name"];
							q.address=menuData[i]["u"];
							q.id=menuData[i]["id"];
							q.data=[];
							resultData[i]=q;
						}
						resultData.length=menuData.length;
						
						for(var k=0;k<subMenuData.length;k++){
						  	for(var p=0;p<count;p++){
						        if(subMenuData[k]["pId"]==resultData[p]["id"]){
						             var s={};
						             s.name=subMenuData[k]["name"];
						             s.address=subMenuData[k]["u"];
						             resultData[p]["data"].push(s);
						        }
							}
						}
					}
				});
			
			return resultData;	
}

//处理menu数据
function handleMenuData(type,topMenuData,bottomMenuData){
	var menuStr="";
	if(type!=undefined && type!="" && (type=="top" || type=="top_bottom")){
		if(topMenuData && topMenuData!=null && topMenuData!=undefined && topMenuData.length!=0){
			menuStr="<div id='topMenu3x5' ><ul>";
					
			if(topMenuData.length>3){
				menuStr="<div id='topMenu4x5' ><ul>";
			}
			for(var o = 0;o< topMenuData.length;o++){
				if(topMenuData[o].data && topMenuData[o].data!=null && topMenuData[o].data!=undefined && topMenuData[o].data.length==0){
					menuStr+="<li><a href='"+topMenuData[o].address+"' ><img class='img_front' src='images/front.png'><div class='menu_li'><img src='images/coin.png' width=10>&nbsp;"+topMenuData[o].name+"</div></a>";
				}else{
					menuStr+="<li><img class='img_front' src='images/front.png'><div class='menu_li'><img src='images/coin.png' width=10>&nbsp;"+topMenuData[o].name+"</div>";
				}
				if(o<topMenuData.length-1){
					menuStr+="<img class='line' src='images/line.png' width=1>";
				}
				if(topMenuData[o].data && topMenuData[o].data!=null && topMenuData[o].data!=undefined && topMenuData[o].data.length!=0){
					menuStr+="<span><img src='images/navbg"+topMenuData[o].data.length+"_top.png' width='100%'><div class='zicaidian'>";
					for(var s in topMenuData[o].data){
						menuStr+="<a href='"+topMenuData[o].data[s].address+"'>"+topMenuData[o].data[s].name+"</a>";	
					}
					menuStr+="</div></span>";
				}
				menuStr+="</li>";
			}
			menuStr+="</u></div>";
	
		}
	}
	if(type!=undefined && type!="" && (type=="bottom" || type=="top_bottom")){
		if(bottomMenuData && bottomMenuData!=null && bottomMenuData!=undefined && bottomMenuData.length!=0){
			
			if(bottomMenuData.length>3){
				menuStr+="<div id='bottomMenu4x5' ><ul>";
			}else{
				menuStr+="<div id='bottomMenu3x5' ><ul>";
			}
			for(var o = 0;o< bottomMenuData.length;o++){
				if(bottomMenuData[o].data && bottomMenuData[o].data!=null && bottomMenuData[o].data!=undefined && bottomMenuData[o].data.length==0){
					menuStr+="<li><a href='"+bottomMenuData[o].address+"' ><img class='img_front' src='images/front.png'><div class='menu_li'><img src='images/coin.png' width=10>&nbsp;"+bottomMenuData[o].name+"</div></a>";
				}else{
					menuStr+="<li><img class='img_front' src='images/front.png'><div class='menu_li'><img src='images/coin.png' width=10>&nbsp;"+bottomMenuData[o].name+"</div>";
				}
				if(o<bottomMenuData.length-1){
					menuStr+="<img class='line' src='images/line.png' width=1>";
				}			
				if(bottomMenuData[o].data && bottomMenuData[o].data!=null && bottomMenuData[o].data!=undefined && bottomMenuData[o].data.length!=0){
					menuStr+="<span><img src='images/navbg"+bottomMenuData[o].data.length+"_bottom.png' width='100%'><div class='zicaidian'>";
					for(var s in bottomMenuData[o].data){
						menuStr+="<a href='"+bottomMenuData[o].data[s].address+"'>"+bottomMenuData[o].data[s].name+"</a>";	
					}
					menuStr+="</div></span>";
				}
				menuStr+="</li>";
			}
			menuStr+="</u></div>";
			
	
		}
	}
	if(menuStr!=""){
		var cssTemp="";
		if(type!=undefined && type!="" && type=="top"){
			cssTemp="editAreaTop";
		}
		if(type!=undefined && type!="" && type=="bottom"){
			cssTemp="editAreaBottom";
		}
		if(type!=undefined && type!="" && type=="top_bottom"){
			cssTemp="editAreaTopAndBottom";
		}
		$("#editArea").removeClass("editArea");
		$("#editArea").addClass(cssTemp);
		var temp="<div id='zheceng' class='footer_front'><img src='images/front.png' width='100%' height='100%'></div>";
		$(temp).appendTo($("body"));	
		$("#zheceng").before(menuStr);
		$("<scri"+"pt>"+"</scr"+"ipt>").attr({src:'./js/menuControl.js',type:'text/javascript'}).appendTo($("body"));
	}
}
	/*
	 * 获取图文信息数据
	 */
	function initData(tuWenType){
		var params = {};
			params.tableName="graphicInformationManager";
			if(recordFirstTime==""){
				params.columnValues={"parentId":"0","isRelease":"已发布","Category":tuWenType};
				params.equal={"parentId":"parentId","isRelease":"isRelease","Category":"Category"};
			}else{
				params.columnValues={"parentId":"0","isRelease":"已发布","Category":tuWenType,"releaseTime":recordFirstTime};
				params.equal={"parentId":"parentId","isRelease":"isRelease","Category":"Category"};
				params .lessThan={"releaseTime":"releaseTime"};
			}
			
			params.offset=fNum;
			params.rows=loadNum;
			params.orderBy="releaseTime desc";
		
		commonSelect(params,false,function(data){
			if(data && data.success){
				recordData=data.data;
				handleJsonData(recordData);	
			}else{
				alert(data.mgs);
			}			
		});
	}
	/*
	 * 加载更多图文信息数据
	 */
	function loadMoreData(tuWenType){
		initData(tuWenType);	
	}
	/*
	 * 获取子图文信息数据
	 */
	function getZiTuWen(id){
		var params = {};
			params.tableName="graphicInformationManager";
			params.columnValues={"parentId":id};
			params.equal={"parentId":"parentId"};
		
		commonSelect(params,false,function(data){
			if(data && data.success){
				recordSubData=data.data;
			}
		});
	}
	/*
	 * 处理获取到的图文信息的json数据
	 */
	function handleJsonData(recordData){
		var tuWenMessage="";
			for(var o in recordData){
				fNum+=1;//判断再次加载时从第几条数据开始			
				var parentId = recordData[o].parentId;
				var groupId = recordData[o].groupId;
				if(recordFirstTime==""){
					recordFirstTime = recordData[o].releaseTime;
				}
		        if(parentId!=null && parentId!=undefined && groupId!=null && groupId!=undefined){
		        	//单图文
		        	if(parentId=="0" && groupId==""){
		        		tuWenMessage+="<div class='_content'><div class='time'>"+recordData[o].releaseTime+"</div>"+
		        			"<div class='_tuwen'><div class='_tuwenPanel'><a href='"+recordData[o].originalLink+"' >"+
		        			"<div class='_tuwenImg'><div class='_tuwenImgPanel'><img style='visibility: inherit;' src='"+recordData[o].coverUrl+"'></div>"+
		        			"<div class='_tuwenImgFooter'><p class='mesgTitleTitle left'>"+recordData[o].title+"</p><div class='clr'></div></div></div>"+
		        			"<div class='zhaiyao'><p>"+recordData[o].abstract+"</p><div class='clr'></div></div></a></div></div></div>";
		        	}else{
		        	//多图文
		        		tuWenMessage+="<div class='_content'><div class='time'>"+recordData[o].releaseTime+"</div>"+
		        			"<div class='_tuwen'><div class='_tuwenPanel'><a href='"+recordData[o].originalLink+"' >"+
		        			"<div class='_tuwenImg'><div class='_tuwenImgPanel'><img style='visibility: inherit;' src='"+recordData[o].coverUrl+"'></div>"+
		        			"<div class='_tuwenImgFooter'><p class='mesgTitleTitle left'>"+recordData[o].title+"</p><div class='clr'></div></div></div></a><div class='_tuwenContent'>";
		        		//获取子图文信息数据
		        		getZiTuWen(recordData[o].id);
		        		if(recordSubData!=null && recordSubData!=undefined && recordSubData.length!=0){
		        			for(var s = 0 ;s<recordSubData.length;s++){
		        				tuWenMessage+="<div class='clr'></div><a href='"+recordSubData[s].originalLink+"' ><div class='_tuwenMesg'>"+
		        				"<span class='_tuwenMesgDot'></span><div class='_tuwenMesgTitle left'>"+
		        				"<p class='left'>"+recordSubData[s].title+"</p><div class='clr'></div></div>"+
		        				"<div class='_tuwenMesgIcon right'><img src='"+recordSubData[s].coverUrl+"' /></div><div class='clr'></div></div></a>";
		        			}
		        		}
		        		tuWenMessage+="</div></div></div></div>";
		        	}	        
		        }   
		    }
	    $("#divForButton").before(tuWenMessage);
	}