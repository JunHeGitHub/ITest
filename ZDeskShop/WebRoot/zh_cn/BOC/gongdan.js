//关联工单查询action
var guanlianurl = "/" +PRJ_PATH + "/" + ZDesk_ROU +"/Bocgongdan/guanlian.action";
//关联工单数据
var guanliangongdandata = {} ;

var formlist = {
	"Fuwuyuyuedan" : "服务预约单",
	"Gonggongyewudan" : "公共业务单",
	"Jianyibiaoyangdan" : "建议表扬单",
	"Tousuchulidan" : "投诉处理单",
	"Wentizixundan" : "问题咨询单",
	"Yewuxiebandan" : "业务协查单",
	"Zhengyizhangkuandan" : "争议账款单"
};
var gongdanData_init = {} ;
//审批状态和工单状态
var spztAndgongdanState = {
	"0" : "待复核",
	"1" : "复核未通过",
	"2" : "内部处理中",
	"3" : "已完成",
	"4" : "无需回复"
};
//审批状态和流程流向
var spztAndflow_type = {
	"0" : "1",
	"1" : "3",
	"2" : "1",
	"3" : "2",
	"4" : "2"
};

/**
 * 初始指定表单数据
 * 1.表单
 * 2.审批状态
 * */
function setJsonToHtmlForm(data,orderForm){
	try{
		$('#'+orderForm+' :input').each(function(index,obj) {
			var $obj=$(obj);
			var key=$obj.attr('name'); 
			if(getDictListfoIndexData(key)!=undefined){
				initDicForm(key,orderForm) ;
			}
			if(data[key]!=undefined){
				$obj.val(data[key]); 
			}
		});
		
		if(data.a_name=="jingBan"){
			$("#userTree").show();
			$("#spzt").empty();
        	$("#spzt").append("<option selected value=''>----请选择----</option>"); 
        	$("#spzt").append("<option value='0'>提交复核</option>"); 
        	$("#spzt").append("<option value='3'>已完成</option>"); 
        	$("#spzt").append("<option value='4'>无需回复</option>"); 
		}
		if(data.a_name=="fuHe"){
			$("#spzt").empty();
        	$("#spzt").append("<option selected value=''>----请选择----</option>"); 
        	$("#spzt").append("<option value='1'>退回经办</option>"); 
        	$("#spzt").append("<option value='2'>发送工单</option>"); 
        	$("#spzt").append("<option value='3'>已完成</option>"); 
        	$("#spzt").append("<option value='4'>无需回复</option>"); 
        	$("#userTree").hide();
		}
		if(data.a_name=="huiFuZuoXi"){
			$("#spzt").empty();
        	$("#spzt").append("<option selected value=''>----请选择----</option>"); 
        	$("#spzt").append("<option value='3'>已完成</option>"); 
        	$("#spzt").append("<option value='4'>无需回复</option>"); 
		}
		
		$("#fuheMgs").val(data.fuheMgs==undefined?'':data.fuheMgs) ; 
		$("#huifuMgs").val(data.huifuMgs==undefined?'':data.huifuMgs) ; 
		$("#chuliMgs").val(data.chuliMgs==undefined?'':data.chuliMgs) ; 
		$("#tuihuiMgs").val(data.tuihuiMgs==undefined?'':data.tuihuiMgs) ; 
	}catch(e){
		window.console.log("setJsonToHtmlForm 异常.")
		window.console.log(e) ;
	}
}
/**
 * 字典下拉初始化:
 * 任意表单元素
 * */
function initDicForm(key,orderForm){
	var data = getDictListfoIndexData(key);
	var obj = $("#"+orderForm+" select[name="+key+"]") ;
	$("#"+orderForm+" select[name="+key+"]").empty(); 
	$("#"+orderForm+" select[name="+key+"]").append("<option value=''>"+"--------请选择-------"+"</option>"); 
	for(var i in data){
        $("#"+orderForm+" select[name="+key+"]").append("<option value='"+data[i].name+"'>"+data[i].name+"</option>"); 
    }
}
/**
 * 字典下拉初始化:
 * 客户信息和服务请求信息
 * */
function initBaseDic(){
	//证件类型
	initDic('paperType') ;
	//服务请求级别
	initDic('gongdanLevel') ;
	//服务请求级别
	initDic('channelName') ;
}
function initDic(selectid){
	var data = getDictListfoIndexData(selectid);
	$("#"+""+selectid+"").empty();
	$("#"+""+selectid+"").append("<option value=''>"+"--------请选择-------"+"</option>"); 
	for(var i in data){
        $("#"+""+selectid+"").append("<option value='"+data[i].name+"'>"+data[i].name+"</option>"); 
    }
}

function initgongdanleixing(){
	$("#spzt").empty();
    $("#spzt").append("<option selected value=''>----请选择----</option>"); 
    $("#spzt").append("<option value='0'>提交复核</option>"); 
    $("#spzt").append("<option value='3'>已完成</option>"); 
    $("#spzt").append("<option value='4'>无需回复</option>"); 
}
/**
 * 工单差异项区域初始化
 * */
function initgongdanType(data){
    if(data.gongdanType!=undefined&&data.gongdanType!=''){
		$("#spzt option[value='"+data.gongdanType+"'] ").attr("selected",true);
		$("#"+data.gongdanType).show() ;   
	}
	setJsonToHtmlForm(data,"orderForm"+data.gongdanType)
	validateByType("orderForm"+data.gongdanType);
}

//新建工单--工单入口方法
function xinjian(){
	var pat = {} ;
	var a = getUserInfo() ;
	pat['A_PROCESSDEFINITIONID'] =A_PROCESSDEFINITIONID;
	pat['A_FORMCLASS']= 'GongdanInsert';
	pat['A_ASSIGNEE']=getUserInfo().loginName;
	pat["chuangjianrenId"]= getUserInfo().loginName;
	pat["chuangjianrenName"]= getUserInfo().name;
	pat["departmentId"]=getUserInfo().departmentId;
	pat["departmentName"]=getUserInfo().departmentName;
	pat["companyId"]= getUserInfo().companyId;
	pat["companyName"]= getUserInfo().companyName;
	pat["flowNode"]="jingban";
	pat["gongdanState"]="新建";
	doStartProcess(pat,function(data){
		if(data.success){
			gongdanData_init = data.data ;
			setJsonToHtmlForm(gongdanData_init,'orderForm') ;
			//初始化工单类型
			initgongdanleixing();
		}
	});
	
    //更改提交按钮事件
	$("#tijiaoBtn1").attr('onclick','');
	$("#tijiaoBtn1").attr( 'onclick','tijiao()');
}

//提交（新建工单页面）
function tijiao(){
    submitgongdan(gongdanData_init) ;
    try{
    window.top.com_bootStarpTab_removeTab_def(thistabId) ;
    }catch(e){}
}
/*
 *保存 
 */
function baocun(){
	//工单区域表单验证
    var bool1= $("#orderForm").validate().form();
    var orderForm = {} ;
    orderForm = zkm_getHtmlFormJsons('orderForm');
	try{
    	bool3=  $("#orderForm"+orderForm.gongdanType).validate().form();
    }catch(e){
    	window.console.log(e) ;
		window.console.log("bool3 error") ;
    }
    var gongdanType = zkm_getHtmlFormJsons('orderForm'+orderForm.gongdanType);
    if(!bool1||!bool3){
    	return ;
    }
    orderForm.chuliMgs =$("#chuliMgs").val() ;
    orderForm.fuheMgs =$("#fuheMgs").val() ;
    orderForm.huifuMgs =$("#huifuMgs").val() ;
    if(orderForm["fuheTime"]==undefined||orderForm["fuheTime"]==""){
    	orderForm["fuheTime"]="0000-00-00 00:00" ;
    }
    if(orderForm["wanchengTime"]==undefined||orderForm["wanchengTime"]==""){
    	orderForm["wanchengTime"]="0000-00-00 00:00" ;
    }
     if(orderForm["manageTime"]==undefined||orderForm["manageTime"]==""){
    	orderForm["manageTime"]="0000-00-00 00:00" ;
    }
    var jsonform = $.extend({}, orderForm,gongdanType);
    var params = {} ;
    params.columnValues=jsonform;
    params.tableName='BOC_gongdan';
	commonUpdate(params,true,function(data){
		if(data.success){
			zinglabs.alert("保存成功") ;
			try{
				window.parent.doRepeat() ;
			}catch(e){}
		}else{
			zinglabs.alert("保存失败") ;
		}
	}) ;
}

//流程流向和工单状态是否为空验证
function validateFlowAndState(spxx,flow_type){
	if(spxx==undefined||flow_type==undefined||spxx==""||flow_type==""){
		return false ;
	}else{
		return true ;
	}
}
//表单提交
function submitgongdan(xdata){
	var data
	if(xdata==undefined||xdata==""){
		data = window.top[A_PROCESSDEFINITIONID] ;
	}else{
		data = xdata
	}
	var params = {} ;
	//流程参数
	params["A_TASKID"] = data["a_id"] ;
	params["A_PROCESSINSTANCEID"] =data["a_processInstanceId"];
	params["A_PROCESSDEFINITIONID"] =A_PROCESSDEFINITIONID;
	params['A_FORMCLASS']= 'GongdanUpdate';
	//用户参数
	try{
		params["departmentId"]=getUserInfo().departmentId;
		params["departmentName"]=getUserInfo().departmentName;
		params["companyId"]=getUserInfo().companyId;
		params["companyName"]=getUserInfo().companyName;
	}catch(e){
		window.console.log(e) ;
		window.console.log("获取参数异常 departmentId,departmentName,companyId,companyName") ;
	}
	//工单区域表单验证
    var bool1= $("#orderForm").validate().form();
    //审批区域表单验证
    var bool2= $("#spxx").validate().form();
    var orderForm =zkm_getHtmlFormJsons('orderForm');
    var spxxForm = zkm_getHtmlFormJsons('spxx');
    var bool3 = false ;
    try{
    	bool3=  $("#orderForm"+orderForm.gongdanType).validate().form();
    }catch(e){
    	window.console.log(e) ;
		window.console.log("bool3 error") ;
    }
    var gongdanType = zkm_getHtmlFormJsons('orderForm'+orderForm.gongdanType);
    params["gongdanState"]= spztAndgongdanState[spxxForm.spzt] ;
    params["flow_type"]= spztAndflow_type[spxxForm.spzt] ;
    //提交的逻辑验证   
    var bool4 = validateFlowAndState(spxxForm.spzt,params["flow_type"]) ;
    if(!bool1||!bool2||!bool3||!bool4){
    	return ;
    }
    var jsonform = $.extend({}, orderForm,spxxForm,params,gongdanType);
    //如果是退回经办，处理人设置创建人
    jsonform['A_ASSIGNEE']=$("#appUserId").val() ;
    if(spxxForm.spzt=="1"){
    	jsonform['A_ASSIGNEE']=data.chuangjianrenId;
    	jsonform['disponseUser']=data.chuangjianrenId;
    }
    //如果是提交复核,处理人是否填写
    if(spxxForm.spzt=="0"){
    	jsonform['disponseUser']= spxxForm.appUserName;
    }
    //如果当前数据为待复核数据,后台判断fuherenId不为空来生成fuheTime
    if(datatype=="fuHe"){
    	jsonform["fuherenId"] = window.top.getUserInfo().loginName;
    	jsonform['fuherenName']=window.top.getUserInfo().name;
    }
	doCompletTask(jsonform,function(data){
		if(data.success){
			if(xdata==undefined||xdata==""){
				try{
					window.parent.hideHight() ;
					window.parent.doRepeat() ;
				}catch(e){
				window.console.log(	"hideHight") ;
				}
			}else{
				try{
					window.console.log(	thistabId) ;
				
				window.top.com_bootStarpTab_removeTab_byId(thistabId,function(data){window.console.log("closetab") ;}) ;
				}catch(e){window.console.log("closetab  error") ;}
			}
		}
	}) ;
	
}

//加载表单验证和编辑权限
function initOrderFormValidate(){
	$("#orderForm").validate({ 	   
		//错误信息处理方法
		errorPlacement: function (error, element){
			//if(element.)
			element.addClass('error');                   
			var errorInfo=error.html();
			element.tooltip({trigger:"manual",
			title:errorInfo,
			placement:"left"
			}) ;
			element.tooltip("show");
		},
		//成功信息处理方法
		success: function ($e,dom) {
			var element=$(dom);
			element.tooltip('hide');
			element.removeClass('error');
		}
	});

	$("#spxx").validate({ 	   
		//错误信息处理方法
		errorPlacement: function (error, element){
			//if(element.)
			element.addClass('error');                   
			var errorInfo=error.html();
			element.tooltip({trigger:"manual",
			title:errorInfo,
			placement:"left"
			}) ;
			element.tooltip("show");
		},
		//成功信息处理方法
		success: function ($e,dom) {
			var element=$(dom);
			element.tooltip('hide');
			element.removeClass('error');
		}
	});
	//设置编辑权限
	permisson =window.top.GOLBAL_PARAM["userAllPermisson"].base;
	for(var i=0;i<permisson.length;i++){
        if(permisson[i].name=="工单回复坐席标识"){
            $("#huifuMgs").removeAttr("disabled");
        }
        if(permisson[i].name=="工单复核标识"){
            $("#fuheMgs").removeAttr("disabled");
        }
    }
}


//刷新
function refresh(){
	var table=$('#orderF').DataTable();
	table.clear().draw();
	table.rows.add(guanliandata).draw(true);
}
//附件弹窗
function showfj(){	
	$("#ifm>div").hide();
	var url = "/ZDesk_BOCdemo/gdxt/fjshowWindow.html";
	$("#fjshow iframe").attr("src", url);
	$("#fjshow").removeClass('hide').show();
}
//有工单类型的高度标识
var length1="" ;
function openSelect(type){	
	$("#Fuwuyuyuedan").hide() ;
	$("#Gonggongyewudan").hide() ;
	$("#Jianyibiaoyangdan").hide() ;
	$("#Tousuchulidan").hide() ;
	$("#Wentizixundan").hide() ;
	$("#Yewuxiebandan").hide() ;
	$("#Zhengyizhangkuandan").hide() ;
	$("#"+type).show() ;   
	gongdan_TYPE = "orderForm"+type;   
	validateByType(gongdan_TYPE);
	$('#'+gongdan_TYPE+' :input').each(function(index,obj) {
		var $obj=$(obj);
		var key=$obj.attr('name'); 
		if(getDictListfoIndexData(key)!=undefined){
			initDicForm(key,gongdan_TYPE) ;
		}
	});
	try{
		window.parent.changeHight() ;
	}catch(e){}
}

function validateByType(type){
    $("#"+type).validate({ 	   
    //错误信息处理方法
	errorPlacement: function (error, element){
		//if(element.)
		element.addClass('error');                   
		var errorInfo=error.html();
		element.tooltip({trigger:"manual",
			title:errorInfo,
			placement:"left"
		}) ;
		element.tooltip("show");
	},
	//成功信息处理方法
	success: function ($e,dom) {
		var element=$(dom);
		element.tooltip('hide');
		element.removeClass('error');
	}
});
}
var length2 = '' ;
//附件界面生成
function showfj(){
	//打开附件下载页面
	openSimpleUpload();
	$("#ifm>div").hide();
	var thisdataFJ =fujianSearch() ;
    $("#FJDiv").show();
    $('#FJtable').dataTable({
		'bPaginate':false,//分页器
		'bLengthChange': false,
		'bStateSave':true, //保留状态
		'bSort':false,//按列排序
		'bInfo':false,//页脚信息
		'bAutoWidth':true, //自动宽度
		'bAutoHeight':true, //自动高度
		'sPaginationType': 'bootstrap',//分页样式
		'dom': 'TRlrtip',
		'data':thisdataFJ,
		'columns': [
		    {"title":"附件名称" ,data: 'fileName',width:'30'},
			{"title":"附件大小" ,data: 'fileSize',width:'10'},
			{"title":"附件类型" ,data: 'fileType',width:'20'},
			{"title":"附件注释" ,data: 'fileText',width:'30'},
			{"title":"附件更新时间" ,data: 'uploadTime',width:'20'},
			{"title":"附件操作","sClass": "center","bSearchable": false,"bStorable": false,width:'10'}

		]
		,'columnDefs': [{
			"targets": -1,
			"defaultContent": "<button class='btn btn-success btn-mini btnDown'>下载</button><button class='btn btn-primary btn-mini btnEdit'>编辑</button><button class='btn btn-danger btn-mini btnDel'>删除</button>",
		      data:null
	         }
	    ],
		"oLanguage": {
  			"sSearch": "搜索:",
  			"sLengthMenu": "每页显示 _MENU_ 条记录",
 			"sZeroRecords": "Nothing found - 没有记录",
  			"sInfo": "显示第  _START_ 条到第  _END_ 条记录,一共  _TOTAL_ 条记录",
  			"sInfoEmpty": "显示0条记录",
  			"oPaginate": {
  				"sFirst": "首页",
   				"sPrevious": " 上一页 ",
   				"sNext":     " 下一页 ",
   				"sLast": "尾页"
   			}
  		}, 'tableTools': {
          	//single 单选 multi 多选  os 可以按ctrl 选择
           	// sRowSelect:'multi',
           	//回调 单选
           	fnRowSelected: function ( nodes ) {
            	var index=nodes[0]._DT_RowIndex;	
				var seldata= table.row(index).data();
				$("#fjname").val(seldata.Annex_Name);
				nowsel =seldata.Annex_ID;
            },
           //sRowSelect:'os',
            //sRowSelector:'td:first-child',
            aButtons:     [ /*{
            "sExtends":    "select_all",
            "sButtonText": "选择全部",
            "target":      "#select"
              } , 'select_none' */]
        	}
	});
	
	var table= $('#FJtable').DataTable();
	$('#FJtable tbody').on( 'click', '.btnDown', function (obj) {
		var data = table.row( $(this).parents('tr') ).data();
	    var path = data.savePath;
	    var name = data.fileName;
	    fujianDown(path,name) ;
	});
	$('#FJtable tbody').on( 'click', '.btnEdit', function (obj) {
	    fujianEdit() ;
//	    commonDownload(file,name);
	});
	$('#FJtable tbody').on( 'click', '.btnDel', function (obj) {
	    var data = table.row( $(this).parents('tr') ).data();
	    var id = data.id;
	    fujianDel(id) ;
	});
	try{
		window.parent.changeHight() ;
	}catch(e){}
}

//附件上传方法
function openSimpleUpload(){
	var params ={};
	params.maxSize=2;//10M
	params.relativeKey='relativePath';
	params.reFillFn='callbackUpload';
	params.isMultiple = '1';
	params.isSimplePage=true;
	params.limitCount='1' ;
	params.dataId= $("#gongdanId").val();
	params.frameHeight="34px";
	useWebUploader("fujian",params);
}
//附件是否存在
var isFujianExist = "" ;
//附件变量 
var fjData = {} ;
//回调
function callbackUpload(data){
	inTable('FJtable',fujianSearch()) ;
}
//转bt
function bytesToSize(bytes) {  
	if (bytes === 0) return '0 B';  
	var k = 1024;  
	sizes = ['B','KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];  
	i = Math.floor(Math.log(bytes) / Math.log(k));  
	var s = Number(bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[i] ;
	return  s;   
	//toPrecision(3) 后面保留一位小数，如1.0GB                                                                                                                  //return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];  
}
//提示已经有附件
function tishi(){
	zinglabs.alert("请删除原有附件,只能上传一个附件。") ;
}
//附件删除
function fujianDel(id){
	zinglabs.confirm("确认删除附件?",function(){
		var params = {};
		params.tableName='UploadSave';
		params.primarykey="id";
		params.columnValues={"id":id,"state":'0'};
		commonUpdate(params,true,function(data){});	 	
		//设置只能上传，已存在附件则不能继续上传，这么做为了删除后能继续选择附件。
		openSimpleUpload() ;
		//删除后，查询一次
		inTable('FJtable',fujianSearch()) ;
		isFujianExist="" ;
	});
}
//附件编辑
function fujianEdit(){
	var url = "gongdan_fujianbianji.html" ;
	fujianEditDialog = zinglabs.dialog({
		title : '编辑附件',
		background: '#FFFFFF',   // 遮罩颜色
 		duration: 300,    // 遮罩透明度渐变动画速度
 		background: '#BBFFFF', // 背景色
 		opacity: 0.1, // 透明度
 		lock: 'true',
		content : '<iframe src='
				+ url
				+ ' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>',
		width : '400',
		height : '200'
	});
	fujianEditDialog.show();
	return fujianEditDialog;
}
//关闭附件编辑框
function closefujianEditDialog(){
	fujianEditDialog.close() ;
	inTable('FJtable',fujianSearch()) ;
	window.focus();
	try{
		window.parent.changeHight() ;
	}catch(e){}
}
//附件下载
function fujianDown(path,name){
	commonDownload(path,name);
}
//附件查询
function fujianSearch(){
	var value =getnowid() ;
	if(value==""){return "";}	
	var fileData = searchFileToDownloadstate1("",value) ;
	if(fileData==undefined){
		return "" ;
	}
	try{fjData = fileData[0] ;}catch(e){}
	if(fileData!=undefined&&fileData.length>0){
		isFujianExist = "1" ;
		if(fileData[0].fileText==undefined){fileData[0].fileText="" ;}
		if(fileData[0].fileSize!=undefined){fileData[0].fileSize=bytesToSize(fileData[0].fileSize);}
		
//		inTable('FJtable',fileData) ;
	}else{
		isFujianExist = "0" ;
//		inTable('FJtable',"") ;
	}
	return fileData ;
}
//返回结果
function showjg(){
	$("#ifm>div").hide();
	$("#jieguoDiv").show();
	chaxunfanhuijieguo() ;
	try{
		var a =$('#gongdanType').val() ;
		window.parent.changeHight() ;
	}catch(e){}
}
var jieguoData = {} ;
function chaxunfanhuijieguo(){
	var value =getnowid() ;
	if(value==""){return ;}
	var params={} ;
	params.tableName='gd_ret_msg';
	var columnValues = {};
	columnValues.idcode = value ;
	params.columnValues = columnValues;
	params.equal='idcode' ;
	params.orderBy = "generateTime desc" ;
	commonSelect(params,false,function(data){
		if(data.data[0]!=undefined&&data.data[0].idcode!=undefined){
			jieguoData = data.data[0] ;
			$('#jieguoDiv :input').each(function(indexx,objj) {
				var $objj=$(objj);
				var key=$objj.attr('name'); 
				if(data.data[0][key]!=undefined){
					$objj.val(data.data[0][key]); 
				}
			});
		}
	});
}
var fuwuId=""
//点击相关服务请求
function  showfw(){
	$("#ifm>div").hide();
    $("#FWDiv").show();
    $('#orderF').dataTable({
		'bPaginate':false,//分页器
		'bLengthChange': false,
		'bStateSave':true, //保留状态
		'bSort':false,//按列排序
		'bInfo':false,//页脚信息
		'bAutoWidth':true, //自动宽度
		'sPaginationType': 'bootstrap',//分页样式
		'dom': 'TRlrtip',
		'data':guanliandata,
		'width':'900',
		'columns': [
			// {"title":"<input type='checkbox' id='selectall' onclick='toggleChecks(this);' ></input>",data: null,width:'30px', defaultContent: '', orderable: false},
		   {"title":"服务请求编号",data:"gongdanId", width:'14%'},
		   {"title":"工单状态" ,data: "gongdanState", width:'14%'},
           {"title":"客户来电时间" ,data: "customerCallTime", width:'15%'},
           {"title":"创建人" ,data: "chuangjianrenId", width:'15%'},
           {"title":"呼入号码" ,data: "callInNum", width:'14%'},
           {"title":"服务请求级别" ,data: "gongdanLevel", width:'13%'},
  //         ,
           {"title":"操作" ,data: null,"sClass": "center","bSearchable": false,"bStorable": false,width:'5'}
		]
		,
		columnDefs: [
			{
				"targets": -1,
				"defaultContent": "<button class='btn btn-danger btn-mini guanlianDel'>删除</button>",
				data:null
	        },{
	        	"targets": 0,
				"render": function ( data, type, row,   meta ) {
      				return  "<button class='btn btn-mini xiangxi'>"+data+"</button>";
				}
	        }
	    ],
	   "oLanguage": {
  			"sSearch": "搜索:",
  			"sLengthMenu": "每页显示 _MENU_ 条记录",
 			"sZeroRecords": "Nothing found - 没有记录",
  			"sInfo": "显示第  _START_ 条到第  _END_ 条记录,一共  _TOTAL_ 条记录",
  			"sInfoEmpty": "显示0条记录",
  			"oPaginate": {
  				"sFirst": "首页",
   				"sPrevious": " 上一页 ",
   				"sNext":     " 下一页 ",
   				"sLast": "尾页"
   			}
  		}, tableTools: {
          	//single 单选 multi 多选  os 可以按ctrl 选择
           	// sRowSelect:'multi',
           	//回调 单选
           	fnRowSelected: function ( nodes ) {
            },
            sRowSelect:'os',
            //sRowSelector:'td:first-child',
            aButtons:     [ /*{
            "sExtends":    "select_all",
            "sButtonText": "选择全部",
            "target":      "#select"
              } , 'select_none' */]
        	}
	});
	
	
	var table= $('#orderF').DataTable();
	$('#orderF tbody').on( 'click', '.guanlianDel', function (obj) {
		var data = table.row( $(this).parents('tr') ).data();
	    var guanlianId = data.gongdanId;
	    guanlianDel(guanlianId) ;
	});
	$('#orderF tbody').on( 'click', '.xiangxi', function (obj) {
      	 xiangxiDialog() ;
    });
    $('#chaolianjie').on( 'click', 'tr', function () {
        $(this).attr('class', '');
    });

	
	/**
	 * 辅助区域功能
	 * ---相关服务请求
	 * */
	chaxunguanlian();
	try{window.parent.changeHight() ;}catch(e){}
}
//删除关联关系
function guanlianDel(guanlianId){
	zinglabs.confirm("确认删除工单的关联关系?",function(){
		var value =getnowid() ;
		if(value==""){return ;}
       	var params = {};
		params.tableName='BOC_gongdan';
		params.primarykey="gongdanId";
		var column = {} ;
		column.gongdanId = value ;
		column.guanlianId = '0' ;
		params.columnValues=column;
		commonUpdate(params,true,function(data){});
		params.gongdanId = guanlianId ;
		commonUpdate(params,true,function(data){});
		glid="" ;
		inTable('orderF',"") ;
	});
}
var glid = ""
function guanlian(){
    var guanlianId = $("#guanlianId").val() ;
    if(guanlianId==""){
    	zinglabs.alert("请输入工单编号") ;
    	return ;
    }
    var gongdanId = $("#gongdanId").val() ;
    if(guanlianId==gongdanId){
    	zinglabs.alert("您输入的工单编号与当前工单的编号相同，请重新输入工单编号") ;
    	return ;
    }
    if(glid!=""){
    	zinglabs.alert("请先删除原有关联关系，只能关联一个工单。") ;
    	return ;
    }
    var params = {} ;
    params.gongdanId=gongdanId ;
    params.guanlianId=guanlianId ;
//    guanlian
	ajaxFunction(guanlianurl, params, true,function(data) {
		if(data.success){
			zinglabs.alert("关联成功") ;
			glid=guanlianId ;
			chaxunguanlian();
		}else{
			zinglabs.alert("关联失败") ;
		}
	});
}
var guanlianData = {} ;
//查询关联关系
function chaxunguanlian(){
	var value =getnowid() ;
	if(value==""){return ;}
	var params={} ;
	var columnValues = {};
	params.tableName='BOC_gongdan';
	params.equal='guanlianId' ;
	columnValues["guanlianId"] = value;
	params.columnValues = columnValues;
	commonSelect(params,true,function(data){
		if(data.data[0]!=undefined&&data.data[0].guanlianId!=undefined){
			glid=data.data[0].guanlianId ;
				guanlianData = data.data[0] ;
		}
		inTable('orderF',data.data) ;
	});
}
function getnowid(){
	var thisid="" ;
	$('#orderForm :input').each(function(index,obj) {
        var $obj=$(obj);
        var value=$obj.val();
       	var key=$obj.attr('name');
       	if(key=="gongdanId"){
       		if(value!=undefined&&value!=null&&value!=""){
       			thisid=value;
       		}
       	}
	});
	return thisid ;
}
// dialog窗口
function xiangxiDialog() {
	var typetitle = guanlianData.gongdanType ;
	var leixing="" ;
	if(typetitle!=undefined&&typetitle!=null&&typetitle!=""){
		try{
			leixing = formlist[typetitle] ;
		}catch(e){
		}
	}
	var title="详细信息" ;
	if(leixing!=undefined&&leixing!=""){
		title = leixing+":"+ title ;
	}
	var url = "gongdanxiangxi.html" ;
//	var artDialog = artDialog();
	var xiangxiDialog = zinglabs.dialog({
		background: '#FFFFFF',   // 遮罩颜色
 		duration: 300,    // 遮罩透明度渐变动画速度
 		background: '#BBFFFF', // 背景色
 		opacity: 0.1, // 透明度
 		lock: 'true',
		title : title,
		content : '<iframe src='
				+ url
				+ ' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>',
		width : '650',
		height : '460'
	});
	return xiangxiDialog;
}
//根据相应用户,设置对应字段操作限制
function checkuserdo(){
	//新建工单全部可以编辑
	if(user==""||user==null){
		$("#xgyj textarea").each(function(){
			$(this).attr("disabled",false);
		});
	//坐席组长有权利：填写审核意见和退回意见
	}else if(user=="zuoxizuzhang"){
		$("#xgyj textarea").eq(0).attr("disabled",false);
		$("#xgyj textarea").eq(1).attr("disabled",false);
		//经办有权利：填写处理意见
	}else if(user=="jingban"){
		$("#xgyj textarea").eq(2).attr("disabled",false);
	}
}

function getImgURL(node) {	
	var imgURL = "";	
    var file = null;
    if(node.files && node.files[0] ){
        file = node.files[0]; 
    }else if(node.files && node.files.item(0)){                    			
        file = node.files.item(0);   
    }
	//这种获取方式支持IE10  
	node.select();  
    imgURL = document.selection.createRange().text;   
	return imgURL;
}
/**
 * 群组树
 * */
function userDialog(url,title,width,height) {
 	var userDig = zinglabs.dialog({
		title : "请选择处理人",
		background: '#FFFFFF',   // 遮罩颜色
 		duration: 300,    // 遮罩透明度渐变动画速度
 		background: '#BBFFFF', // 背景色
 		opacity: 0.1, // 透明度
 		lock: 'true',
		content : '<iframe src=' + url
			+ ' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>',
		width :width,
		height : height
	});
	userDig.show();
	return userDig;
}
function addOrg() {
	var url = "/ZDesk/zh_cn/common/userMappingOrg.html?disponse=0&showButton=y&reFillFn=getSelectNode&closeFn=closeDig&clearFn=clearDig&FilterBeanId=MessageQuanxianFilter&permissionName=工单复核标识";
	closeDialog=userDialog(url,"选择复核人","300","400");
}
function getSelectNode(node){
	if(node[0].recordType =="org"){
		zinglabs.alert("请选择处理人");
		return ;
	}
    $("#appUserId").val("");
    $("#appUserName").val("");
    $("#appUserId").val(node[0].id); 
    $("#appUserName").val(node[0].text);
    closeDig();
}

//关闭dialog
function closeDig(){
	closeDialog.close();
	window.focus();
}
//清空clearDig
function clearDig(){
	$("#appUserId").val("");
    $("#appUserName").val("");
}
function inTable(id,data){
	var table=$('#'+id).DataTable();			     
	//清空筛选
	table.search("").draw();
	//清空数据
	table.clear().draw();
	//重新加载数据
	table.rows.add(data).draw(true);
}
/**
 * 字典
 */
function getDicfunction(){
	var businessTypeDic = getDictListfoIndexData("businessType");
	var channelNameDic = getDictListfoIndexData("channelName");
	var gongdanLevelDic = getDictListfoIndexData("gongdanLevel");
	var belongBankDic = getDictListfoIndexData("belongBank");
	var problemTypeDic = getDictListfoIndexData("problemType");
	var complainTypeDic = getDictListfoIndexData("complainType");
	var businessTypeDic = getDictListfoIndexData("businessType");
	var subscribeTypeDic = getDictListfoIndexData("subscribeType");
	
}
