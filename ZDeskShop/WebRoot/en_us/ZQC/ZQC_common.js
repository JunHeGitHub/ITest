
/*得到评分指标树
*/
function getzhibiaoTree(){
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
//通过grade_index得到description
	function doSearch_description(grade_index){
		var pat={};
		pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	pat.nameSpaceId = 'getDescriptionByGrade_index';
    	pat.grade_index=grade_index;
    	commonSelectForComplex(pat, false,
    	function(data) {
     	rdata=data.rows;
    	
    });
    return rdata;				   		
	}
	//业务类型
	function doSearch_wulizu(){
		var pat={};				   
		pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	pat.nameSpaceId = 'getWulizu';
    	commonSelectForComplex(pat, false,
    	function(data) {
     	rdata=data.rows;
    	
    });
     return rdata;					   		
	}	
	//查询教材目录名----rdata[i].teach_dir_description
		function doSearch_jiaocaimuluming(){
			var rdata
			var pat={};				   
			pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
	    	pat.nameSpaceId = 'getJiaocaimuluming';
	    	commonSelectForComplex(pat, false,
		    	function(data) {
		     	rdata=data.rows;
		    });	
    	return rdata;			   		
	}
	//通过一个指标的值查到所有的节点
	function doSearchZhibiaoByDescription(description){
			var pat={};				   
			pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
	    	pat.nameSpaceId = 'getZhibiaoByDescription';
	    	pat.grade_name=description;
	    	commonSelectForComplex(pat, false,
	    	function(data) {
	     	rdata=data.rows;
	    });
	    return rdata;	
			
			}	
//将滚动条滚动到指定div位置 id为div的id
function scroll2data(id){
	$("html,body").animate({scrollTop: $("#"+id).offset().top}, 500); 
}
//将数据填充到对应表单中 id为form的id data为数据
function initForm(id,data){
	var cfs=$("#"+id+" input[name],textarea[name]");
	for(var i=0;i<cfs.length;i++){
		var e=cfs[i];					
		var v=data[e.name];
		if(v==undefined || v==null){
			v='';
		}
		$(e).val(v);						
	}
}
//刷新数据表格 id为table的id fn为函数名
function refresh(id,fn){
     var table=$('#'+id).DataTable();
     //清空筛选
     table.search("").draw();
     //清空数据
     table.clear().draw();
     //重新加载数据
     var data=fn; 
     table.rows.add(data).draw(true);			
}
//拼接筛选项 formName为form表单的id 返回sql字符串
function searchItem2sql(formName){
	var cfs = $('#'+formName+' :input');
	var searchItems="";
	var n=0;
	var bt="";//记录开始时间
	var et="";//记录结束时间
	for(var i=0;i<cfs.length;i++){
		var e=cfs[i];
		if(e.id!=undefined && e.value!=undefined){
			if(e.id.length>5){				
				if(e.id.substring(e.id.lastIndexOf("_")+1,e.id.length-1)=="DFT"){
					//判断开始时间是否填写
					if(n==0 && e.value=="" && $('input[name='+e.id+']').attr("isrequired")=="true"){
						//zinglabs.alert("请输入查询时间！");
						zinglabs.i18n.alert("ZQC_qingshurubiaoyouxinghaodeshijian");
						return false;
					}
					//记录开始时间
					if(n==0 && $('input[name='+e.id+']').attr("isrequired")=="true"){
						bt=e.value;
					}
					//判断结时间是否填写
					if(n==1 && e.value=="" && $('input[name='+e.id+']').attr("isrequired")=="true"){
						//zinglabs.alert("请输入结束时间！");
						zinglabs.i18n.alert("ZQC_qingshurubiaoyouxinghaodeshijian");
						return false;
					}
					//判断时间是否为指定天数
					if(n==1 && $('input[name='+e.id+']').attr("isrequired")=="true"){
						bt=new Date(Date.parse(bt.replace(/-/g,"/")));
						et=new Date(Date.parse(e.value.replace(/-/g,"/")));
						
						if(et.getTime()<bt.getTime()){
							//zinglabs.alert("结束时间不能大于开始时间！");
							zinglabs.i18n.alert("ZQC_jieshushijianbunengdayukaishihijian");
							return false;
						}
						if((U_DateDiffer(bt,et)/24)>30){
							//zinglabs.alert("查询时间不能大于30天！");
							zinglabs.i18n.alert("ZQC_chaxunshijanbunengdayusanshitian");
							return false;
						}
						//清空记录开始与结束的时间变量
						bt="";
						et="";
					}				
					
					if(n==0 && e.value!=""){
						searchItems+=" "+e.id.substring(0,e.id.length-5)+" >= '"+e.value+"' and ";
						n=1;
						continue;
					}else{						
						n=1;
					}
					
					if(n==1 && e.value!=""){
						searchItems+=" "+e.id.substring(0,e.id.length-5)+" <= '"+e.value+"' and ";
						n=0;
					}
														
				}else{
					if(e.value!=""){
						if(e.id.substring(0,e.id.length-2)=="record_length"){
							if(e.id.substring(e.id.lastIndexOf("_")+1,e.id.length)=="D"){
								searchItems+=" "+e.id.substring(0,e.id.length-2)+" >= '"+e.value+"' and ";
							}
							if(e.id.substring(e.id.lastIndexOf("_")+1,e.id.length)=="X"){
								searchItems+=" "+e.id.substring(0,e.id.length-2)+" <= '"+e.value+"' and ";
							}
						}else{
							searchItems+=" "+e.id+" like '%"+e.value+"%' and ";
						}
					}
				}
			}else{
				if(e.value!=""){
					if(e.id.substring(0,e.id.length-2)=="record_length"){
						if(e.id.substring(e.id.lastIndexOf("_")+1,e.id.length)=="D"){
							searchItems+=" "+e.id.substring(0,e.id.length-2)+" >= '"+e.value+"' and ";
						}
						if(e.id.substring(e.id.lastIndexOf("_")+1,e.id.length)=="X"){
							searchItems+=" "+e.id.substring(0,e.id.length-2)+" <= '"+e.value+"' and ";
						}
					}else{
						searchItems+=" "+e.id+" like '%"+e.value+"%' and ";
					}
				}
			}
		}
	}
	if(searchItems==""){
		searchItems="1=1";
	}else{
		searchItems=searchItems.substring(0,searchItems.length-5);
	}
	return searchItems;
}
//显示隐藏查询表单
function showSearchItems(id){
	$("#"+id).slideToggle(200);
}
//每页显示条数
function setPageNum(dataTableName,id){
	var table=$('#'+dataTableName).DataTable();
	var pageSize=$("#"+id).val();
	table.page.len(pageSize).draw();
	//alert(obj.value);
}
//全选 id为table的id cid为复选框的id
function toggleChecks(id,cid){
	var table=$('#'+id).DataTable();
	var tableTools = new $.fn.dataTable.TableTools(table);
       
    var ischeck=$("#"+cid).is(':checked');
    if(ischeck){
	    tableTools.fnSelectAll();
    }else{
	    tableTools.fnSelectNone();
    }
	if(id=="tableForUserData"){
		choseOK();
	}
}
//获取查询项中所有列名
function getColumns(tableName){
	var columnsString= "";
	var tableName=document.getElementById(tableName);
	var rows=tableName.rows;
	for(var i=0;i<rows.length;i++){
		var uu = rows[i].cells.length;
		for(var j = 0 ;j<uu;j++){
			if(j%2 != 0){
				var data = rows[i].cells[j].firstElementChild;
				var id = data.id;
				var value = data.value;
				
				if(value!=undefined && value!=null && value != ""){
					columnsString+=id+" like,";
				}
			} 		
		}
	}
	return columnsString.substring(0,columnsString.length-1);
}
var player2mp3 = function(containerID,mp3File) {
		//$("#"+containerID+" > object").remove();
		var content="<object width='100%' border='0' height='60' type='application/x-ms-wmp' classid='clsid:6BF52A52-394A-11D3-B153-00C04F79FAA6' id='wmpForIe'>"+
					"<param value='"+mp3File+"' name='url'>"+
					"<param value='1' name='rate'>"+
					"<param value='0' name='balance'>"+
					"<param value='0' name='currentPosition'>"+
					"<param value='100' name='playCount'>"+
					"<param value='-1' name='autoStart'>"+
					"<param value='' name='defaultFrame'>"+
					"<param value='100' name='volume'>"+
					"<param value='0' name='currentMarker'>"+
					"<param value='-1' name='invokeURLs'>"+
					"<param value='-1' name='stretchToFit'>"+
					"<param value='0' name='windowlessVideo'>"+
					"<param value='-1' name='enabled'>"+
					"<param value='Full' name='uiMode'>"+
					"<param value='-1' name='enableContextMenu'>"+
					"<param value='0' name='fullScreen'>"+
					"<param value='' name='SAMIStyle'>"+
					"<param value='' name='SAMILang'>"+
					"<param value='' name='SAMIFilename'>"+
					"<param value='' name='captioningID'>"+
					"<param value='0' name='enableErrorDialogs'>"+
					"<embed width='100%' height='60' enablecontextmenu='0' playcount='1' allowscan='1' showcontrols='1' autostart='1' type='video/x-ms-wmv' name='MediaPlayer' src='"+mp3File+"' id='wmpForOther'>"+
					"</object>";
		$("#"+containerID).html(content);
};

//加载业务类型
function load_business_type(id){
		var business_type=getDictListfoIndexData('ComBusinessType');
		$("#"+id).append("<option value=''></option>");
		if(business_type && business_type!=null && business_type!=undefined){
		for(var i=0;i<business_type.length;i++){
			if(business_type[i]!=null){
				$("#"+id).append("<option value="+business_type[i].name+">"+business_type[i].name+"</option>");				
			}
		}
		}
	}
//加载业务类型
function initYeWuLeiXing(id){
	var data=getDictListfoIndexData('ComBusinessType');	
    $("#"+id).append("<option value=''></option>");
    if(data && data!=null && data!=undefined){
	    $.each(data, function (n, d) {
	    	if(d!=null && d.name!=undefined && d.name!=""){
		    	$("#"+id).append("<option value="+d.name+">"+d.name+"</option>");    	
	    	}
	    });
    }
}

	//加载指标数据			
function initZhiBiao(id){
	var data=getzhibiaoTree();
    var zhibiao="";
    zhibiao='<option value="" ></option>';
    $.each(data, function (n, d) {
    	zhibiao+='<option value="' + d.grade_name + '" >' + d.grade_name + '</option>';
    });
    $("#"+id).append(zhibiao);
}

//加载数据状态
function initShuJuZhuangTai(source,id){
	$("#"+id).append("<option value=''></option>");
	if(source=="fenpei"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zero")+">"+zinglabs.zqc.getState("zero")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianweifenpei")+">"+zinglabs.zqc.getState("chujianweifenpei")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyifenpei")+">"+zinglabs.zqc.getState("chujianyifenpei")+"</option>");
	}
	if(source=="chujian"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianweifenpei")+">"+zinglabs.zqc.getState("chujianweifenpei")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyifenpei")+">"+zinglabs.zqc.getState("chujianyifenpei")+"</option>");
	}
	if(source=="fujian"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianweifenpei")+">"+zinglabs.zqc.getState("fujianweifenpei")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianyifenpei")+">"+zinglabs.zqc.getState("fujianyifenpei")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyifabu")+">"+zinglabs.zqc.getState("chujianyifabu")+"</option>");
	}
	if(source=="fushen"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsu")+">"+zinglabs.zqc.getState("zuoxishangsu")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsuchenggong")+">"+zinglabs.zqc.getState("zuoxishangsuchenggong")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsushibai")+">"+zinglabs.zqc.getState("zuoxishangsushibai")+"</option>");
	}
	if(source=="zhijianyuanfujian"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianyifenpei")+">"+zinglabs.zqc.getState("fujianyifenpei")+"</option>");
	}
	if(source=="wenbenpingfenchaxun"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyifabu")+">"+zinglabs.zqc.getState("chujianyifabu")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsu")+">"+zinglabs.zqc.getState("zuoxishangsu")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsuchenggong")+">"+zinglabs.zqc.getState("zuoxishangsuchenggong")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsushibai")+">"+zinglabs.zqc.getState("zuoxishangsushibai")+"</option>");
	}
	if(source=="wenbenfushenchaxun"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsuchenggong")+">"+zinglabs.zqc.getState("zuoxishangsuchenggong")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsushibai")+">"+zinglabs.zqc.getState("zuoxishangsushibai")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsu")+">"+zinglabs.zqc.getState("zuoxishangsu")+"</option>");
	}
	if(source=="yuyinpingfenchaxun"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyifabu")+">"+zinglabs.zqc.getState("chujianyifabu")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsu")+">"+zinglabs.zqc.getState("zuoxishangsu")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsuchenggong")+">"+zinglabs.zqc.getState("zuoxishangsuchenggong")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsushibai")+">"+zinglabs.zqc.getState("zuoxishangsushibai")+"</option>");
	}
	if(source=="wenbenmingxichaxun"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zero")+">"+zinglabs.zqc.getState("zero")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyifabu")+">"+zinglabs.zqc.getState("chujianyifabu")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianweifenpei")+">"+zinglabs.zqc.getState("chujianweifenpei")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyifenpei")+">"+zinglabs.zqc.getState("chujianyifenpei")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianweifenpei")+">"+zinglabs.zqc.getState("fujianweifenpei")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianyifenpei")+">"+zinglabs.zqc.getState("fujianyifenpei")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsu")+">"+zinglabs.zqc.getState("zuoxishangsu")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsuchenggong")+">"+zinglabs.zqc.getState("zuoxishangsuchenggong")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsushibai")+">"+zinglabs.zqc.getState("zuoxishangsushibai")+"</option>");
	}
	if(source=="yuyinmingxichaxun"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zero")+">"+zinglabs.zqc.getState("zero")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyifabu")+">"+zinglabs.zqc.getState("chujianyifabu")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianweifenpei")+">"+zinglabs.zqc.getState("chujianweifenpei")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyifenpei")+">"+zinglabs.zqc.getState("chujianyifenpei")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianweifenpei")+">"+zinglabs.zqc.getState("fujianweifenpei")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianyifenpei")+">"+zinglabs.zqc.getState("fujianyifenpei")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsu")+">"+zinglabs.zqc.getState("zuoxishangsu")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsuchenggong")+">"+zinglabs.zqc.getState("zuoxishangsuchenggong")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsushibai")+">"+zinglabs.zqc.getState("zuoxishangsushibai")+"</option>");
	}
}
//加载评分状态 
function initPingFenZhuangTai(source,id){
	$("#"+id).append("<option value=''></option>");
	if(source=="fenpei"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zero")+">"+zinglabs.zqc.getState("zero")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianweipingfen")+">"+zinglabs.zqc.getState("chujianweipingfen")+"</option>");
	}
	if(source=="chujian"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianweipingfen")+">"+zinglabs.zqc.getState("chujianweipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyipingfen")+">"+zinglabs.zqc.getState("chujianyipingfen")+"</option>");
	}
	if(source=="fujian"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyipingfen")+">"+zinglabs.zqc.getState("chujianyipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianweipingfen")+">"+zinglabs.zqc.getState("fujianweipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianyipingfen")+">"+zinglabs.zqc.getState("fujianyipingfen")+"</option>");
	}
	if(source=="fushen"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsu")+">"+zinglabs.zqc.getState("zuoxishangsu")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsuchenggong")+">"+zinglabs.zqc.getState("zuoxishangsuchenggong")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsushibai")+">"+zinglabs.zqc.getState("zuoxishangsushibai")+"</option>");
	}
	if(source=="zhijianyuanfujian"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyipingfen")+">"+zinglabs.zqc.getState("chujianyipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianweipingfen")+">"+zinglabs.zqc.getState("fujianweipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianyipingfen")+">"+zinglabs.zqc.getState("fujianyipingfen")+"</option>");
	}
	if(source=="wenbenmingxichaxun"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zero")+">"+zinglabs.zqc.getState("zero")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianweipingfen")+">"+zinglabs.zqc.getState("chujianweipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyipingfen")+">"+zinglabs.zqc.getState("chujianyipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianweipingfen")+">"+zinglabs.zqc.getState("fujianweipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianyipingfen")+">"+zinglabs.zqc.getState("fujianyipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsu")+">"+zinglabs.zqc.getState("zuoxishangsu")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsuchenggong")+">"+zinglabs.zqc.getState("zuoxishangsuchenggong")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsushibai")+">"+zinglabs.zqc.getState("zuoxishangsushibai")+"</option>");
	}
	if(source=="yuyinmingxichaxun"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zero")+">"+zinglabs.zqc.getState("zero")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianweipingfen")+">"+zinglabs.zqc.getState("chujianweipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyipingfen")+">"+zinglabs.zqc.getState("chujianyipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianweipingfen")+">"+zinglabs.zqc.getState("fujianweipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("fujianyipingfen")+">"+zinglabs.zqc.getState("fujianyipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsu")+">"+zinglabs.zqc.getState("zuoxishangsu")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsuchenggong")+">"+zinglabs.zqc.getState("zuoxishangsuchenggong")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsushibai")+">"+zinglabs.zqc.getState("zuoxishangsushibai")+"</option>");
	}
	if(source=="shujuchaxunwenbenpingfenchaxun"){
		$("#"+id).append("<option value="+zinglabs.zqc.getState("chujianyipingfen")+">"+zinglabs.zqc.getState("chujianyipingfen")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsu")+">"+zinglabs.zqc.getState("zuoxishangsu")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsuchenggong")+">"+zinglabs.zqc.getState("zuoxishangsuchenggong")+"</option>");
		$("#"+id).append("<option value="+zinglabs.zqc.getState("zuoxishangsushibai")+">"+zinglabs.zqc.getState("zuoxishangsushibai")+"</option>");
	}
	
}



	//判定是否选中数据，查看文本内容
		function lookContent(e){
			if(fnGetSelected().length==0){
				zinglabs.i18n.alert("zuoxichaxun_qingxuanzeyaochakandeshuju");
			}else{
				content(e);
			}
	}
	
	//判定是否选中数据，查看文本内容
		function lookContentMp3(e){
			if(fnGetSelected().length==0){
				zinglabs.i18n.alert("zuoxichaxun_qingxuanzeyaochakandeshuju");
			}else{
				content(e);
			}
	}
	
	//查看查看详情评分
	function content(e){
			var params={
		    title: '查看文本内容',
		     width:'900',
		      height:'600',
		    content: '<iframe src="" style="border: 0; width: 100%; height: 100%"  frameborder="0"/>'
		   
		    
		};
		zinglabs.dialog(params);	
		}
		


		//加载地区下拉			
function initQuartier(id){
	var data=getDictListfoIndexData('ComQuartier');
    var quartier="";
    quartier='<option value="" ></option>';
    if(data && data!=null && data!=undefined){
	    $.each(data, function (n, d) {
	    	quartier+='<option value="' + d.name + '" >' + d.name + '</option>';
	    });
    }
    $("#"+id).append(quartier);
}



	
		//加载客户满意度下拉			
function initClient_satisfy(id){
	var data=getDictListfoIndexData('CustomerSatisfaction');
    var client_satisfy="";
    client_satisfy='<option value="" ></option>';
    if(data && data!=null && data!=undefined){
	    $.each(data, function (n, d) {
	    	client_satisfy+='<option value="' + d.name + '" >' + d.name + '</option>';
	    });
    }
    $("#"+id).append(client_satisfy);
}



function chongzhi(id){
	  $("#"+id+" input,#"+id+" select[name]").val('');
}


	//系统当前时间
	function sys_date(){
		var myDate = new Date();
		var yyyy=myDate.getFullYear();    //获取完整的年份(4位,1970-????)
		var MM=(myDate.getMonth()+1)<10?"0"+(myDate.getMonth()+1):(myDate.getMonth()+1);        //获取当前月份(0-11,0代表1月)
		var dd=myDate.getDate()<10?"0"+myDate.getDate():myDate.getDate();      //获取当前日(1-31)
		var date=yyyy+"-"+MM+"-"+dd;
		var hh=myDate.getHours()<10?'0'+myDate.getHours():myDate.getHours();
		var mm=myDate.getMinutes()<10?'0'+myDate.getMinutes():myDate.getMinutes();
		var ss=myDate.getSeconds()<10?'0'+myDate.getSeconds():myDate.getSeconds();
		var time=hh+':'+mm+':'+ss;
		var sysdate=""+date+" "+time+"";
		return sysdate;
	}
	
	
	//下载文本
function xiaZaiWenBen(){
	var table = $("#tableForData").DataTable();
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();
	if (selectData.length == 0 || selectData == null || selectData == undefined) {
		//alert("请选中要下载的数据!");
		zinglabs.i18n.alert("zqc_share_qxzyxzdsj");
		return;
	}
	if(selectData.length > 1){
		//alert("只支持单个数据下载！");
		zinglabs.i18n.alert("zqc_share_zzcdgsjxz");
		return;
	}
	//alert("http://"+selectData[0].file_location+":9999/ZDesk/file/"+selectData[0].file_name,""+selectData[0].file_name+"");
	var file_location=$("#file_location").val();
	var file_name=$("#file_name").val();
	var file_url;
	var aa=file_name.substring(file_name.lastIndexOf("."),file_name.length);
	if(aa=='.mp3'){
		file_url="/usr/local/apache-tomcat-6.0.26/webapps/ZDesk/zh_cn/ZQC/hongshanguo.mp3";	
	}
	if(aa=='.html'){
		file_url="/usr/local/apache-tomcat-6.0.26/webapps/ZDesk/ZIM/record_data/"+file_name.substring(file_name.lastIndexOf("_")+1,file_name.lastIndexOf("_")+9)+"/"+file_name;	
	}
	
	commonDownload(file_url,""+file_name+"");
}