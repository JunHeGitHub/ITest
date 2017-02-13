var cardUrl='';//登录名变量
var row=0;//每页显示到第几条数据
var offset=0;//每页从第几条显示数据
var isData=0;//是否搜索到数据
var page=0;//当前第几页变量
var ZKM_ROU="ZKM";
$(document).ready(function() {
	//cardUrl=getQueryString("cardUrl");//登录名
	cardUrl="@cookie_cardUrl";
	$(window).scroll(function(){
		var h=$(document).scrollTop(); 
		if(h>200){
			$("#searchItem").slideUp(300);
		  $("#hideSearchItem").slideUp(300);
		}
	});
	searchButton();
	
	
});



 function getIdData(rows,offset){
	var searchItem=zkm_getHtmlFormJsons("searchDataForm");
    var rdata=[]; 
 	var pat = {};
 	pat.isPagination="false";
 	pat.rows=rows;
 	pat.offset=offset;
    pat.tableName="Workflow_Relation_Table";
    pat.beanId="commonSelectFilter";
    pat.primarykey="id";
    pat.nameSpace="com.zinglabs.apps.appCommonCurd.pojo.getList";
    pat.dbid="ZKM";
    pat.columnValues=cardUrl;
    pat.equal="cardUrl";
	pat.workflow_name=searchItem.workflow_name;
	pat.workflow_sort=searchItem.workflow_sort;
	pat.workflow_state=searchItem.workflow_state;
    var url="/"+PRJ_PATH+"/"+ZKM_ROU+"/"+"WorkflowSearch"+"/"+"MyWorkflowSearch.action";
    ajaxFunction(url, pat, false, function(data) {
		if (data && data.data.data&&data.data.data.length!=0) {
			rdata=data.data.data;
	  	    	if(rdata[0].total>10){
	  				var b= $("#loading");
	  	 			b.remove();
	  	 			$('#house-loadmore').append('<span id="loading">点击加载更多</span>');
	  	 			//$("#loading").show();
	  	 		}
		}
	});
	return rdata;
 }

function loadTable(data){
	for(var i=0;i<data.length;i++){
		var workflow_name="";
//		var woow_chat_id="woow"+data[i].woow_chat_id+"";
//        var woow_chat_id="woow"+data[i].woow_chat_id+"";
        var woow_chat_id=data[i].woow_chat_id+"";
        //20150611 判断此条工作流数据是否已正式发起 根据工作流状态进行跳转页面
        var path="";
        var _id=data[i].id;
        var _type=true;
        if(data[i].workflow_state!=null && data[i].workflow_state!=undefined && data[i].workflow_state!="待处理"){
			//path="/talk.htm?woowId="+woow_chat_id+"&v=9999.9999";
			path="/NCard/IMKFService?ac=talkEnter&_t="+(new Date()).getTime()+"&woowId="+woow_chat_id+"&v=9999.9999";
        }else{
        	_type=false;
        	path="/workflow/createWork.html?_id="+_id+"&_t="+(new Date()).getTime()+"&v=9999.9999";
        }
		if(data[i].workflow_name.length>10){
			workflow_name=data[i].workflow_name.substring(0,10)+"...";
		}else{
			workflow_name=data[i].workflow_name;
		}
		$("#info").append(
					"<a href='javascript:void(0);' onclick='redirectUrl(\""+path+"\","+_type+");' style='color:#484848;text-decoration:none;'>"
					+"<div>"
					+"<span>"+workflow_name+"</span>"
			       	+"<ul style='font-size:12px;margin:5px'>"
			       		+"<li>分类："+data[i].workflow_sort+"<span style='float: right;'>"+data[i].workflow_state+"</span></li>"
			       		+"<li>创建时间："+data[i].create_time+"</li>"
			       	+"</ul>"
			       	+"<hr/>"
			       	+"</div>"
			       	+"</a>"
			       	);
	}
}
function redirectUrl(url,_type){
	if(_type && _type!=null && _type!=undefined){
		window.location.href=url;
	}else{		
		alert("工作流未正式发起。\n请确认执行人信息！");			
		window.location.href=url;		
	}
}
//分页显示 当前页  总页数。。。
function flitGIlarts(data,row,offset){
	var totalpage=0;
	page=page+1;
	if(data!=""){
	var totalrow=data[0].total;
	}else{totalrow=0}
	if(totalrow==""||totalrow==null||totalrow==undefined){
		if(data[0]==""||data[0]==null||data[0]==undefined){
			totalrow=0;
		}else{
			totalrow=data[0].total;
		}
	}
	if(totalrow%(row-offset)>0){
 		totalpage=parseInt((totalrow/(row-offset))+1);
 	}else{
 		totalpage=totalrow/(row-offset);
 	}
 	$("#total").html(totalrow);
 	if(row>=totalrow){
		$("#row").html(totalrow);
		var b= $("#loading");
	  	b.remove();
	  	$('#house-loadmore').append('<span id="loading">没有更多加载项</span>');
	}else{
		$("#row").html(row);	
	}
}

function searchButton(){
page=0;//初始第一页
		$("#info").html("");
		var data=getIdData(10,0);//查询到的数据
		if(data&&data.length!=0){
			if(data.length>0){
				isData=1;
			}
			loadTable(data); //动态加载html标签和数据
			row=10;//每页显示到第几条数据
			offset=0;//每页从第几条显示数据
			flitGIlarts(data,row,offset);
		}else{
			$('#house-loadmore').append('<span id="loading">没有查询到数据！</span>');
		}
	$("#order-select").mousemove(function(){
		$("#order-select").css("opacity","0.7");
	});
	$("#order-select").mouseleave(function(){
		$("#order-select").css("opacity","1");
	});
	$("#hideSearchItem").mousemove(function(){
		$("#hideSearchItem").css("opacity","0.7");
	});
	$("#hideSearchItem").mouseleave(function(){
		$("#hideSearchItem").css("opacity","1");
	});
	
	$("#hideSearchItem").click(function(){
		$("#searchItem").slideToggle(300);
	});
	$("#order-select").click(function(){
		useLayMaskModels();
		$("#searchItem").slideToggle(300);
		$("#hideSearchItem").show(300);
	});
	
	$("#hideSearchItem").click(function(){
		page=0;//初始第一页
		$("#info").html("");
		var data=getIdData(10,0);//查询到的数据
		if(data.length>0){
			isData=1;
		}
		loadTable(data); //动态加载html标签和数据
		row=10;//每页显示到第几条数据
		offset=0;//每页从第几条显示数据
		flitGIlarts(data,row,offset);
	});
	$("#house-loadmore").click(function(){
		 if(isData!=0){
			 row=row+10;
			 offset=offset+10;
			 var data=getIdData(row,offset);
			 var length=data.length;
			 if(length!=0){
			 	loadTable(data);//动态加载html标签和数据
			 	flitGIlarts(data,row,offset);
		 	}
		 }
	});

}

//点击遮挡层时摧毁遮挡层
function clickUseLayMaskModels(obj_Mask){
	 var obj_Mask = $("#divMask");
	obj_Mask.remove();
	$("#hideSearchItem").slideUp(1);
	$("#searchItem").slideUp(300);
}

//遮挡层
function useLayMaskModels() {
    var obj_Mask = $("#divMask");
    if (obj_Mask.length != 0) {
        var state = obj_Mask.css("display");
        obj_Mask.show();
    }
    else {
        $(document.body).append("<div id='divMask' style='opacity:0;left:0px;top:300px;width:100%;height:100%;background-color:pink;z-index=-1;position:absolute;' onclick='clickUseLayMaskModels()'></div>");
    }
}