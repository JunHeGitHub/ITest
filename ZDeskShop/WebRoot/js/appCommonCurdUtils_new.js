/***
   通用增删改
**/

//项目名称
var APP_PROJECT_NAME=window.top.PRJ_PATH||"ZDesk";
//模块
var APP_MODULES_NAME=window.top.ZDesk_ROU||"ZDesk";
//通用查询action路径
var COMMON_FIND_URL = "/" + APP_PROJECT_NAME + "/" +APP_MODULES_NAME+ "/appCommonsCurd" + "/" + "Search.action";
//通用Insert Action 路径
var COMMON_ADD_URl="/" + APP_PROJECT_NAME + "/" +APP_MODULES_NAME+ "/appCommonsCurd" + "/" + "Insert.action";
//通用Update Action路径
var COMMON_UPDATE_URl= "/" + APP_PROJECT_NAME + "/" +APP_MODULES_NAME+"/appCommonsCurd" + "/" + "Update.action";
//通用delete Action 路径
var COMMON_DEL_URl= "/" + APP_PROJECT_NAME + "/" +APP_MODULES_NAME+"/appCommonsCurd" + "/" + "Delete.action";

//事务性修改 Action 路径
var COMMON_TRANSACTION_UPDATE_URL= "/" + APP_PROJECT_NAME + "/" +APP_MODULES_NAME+"/appCommonsCurd" + "/" + "transactionUpdate.action";

var COMMON_EXPORT_URL= "/" + APP_PROJECT_NAME + "/" +APP_MODULES_NAME+"/appCommonsCurd" + "/" + "exportExcel.action";
 
var DEFAULT_DBID="ZKM";
/***
 通用 增改 参数
**/

var InsertSetting={
     //表名
     "tableName":"",
     //可自定义
     "nameSpace":"com.zinglabs.apps.appCommonCurd.pojo.common_insert",
     //表单序列化 格式类型   批量格式 [{"name":"guo","age":"120"},{"name":"zhi","age":"1"}]
     //                  非批量格式 {"name":"guo","age":"120"} 
     "columnValues":{},
     //主键值  String  格式  primarykey:"id"
     "primarykey":"id",
     //自己 处理方法 需在 spring配置文件定义 bean 自定义xml文件 请将beanId改成 commonFreeFilter 
     "beanId":"commonInsertFilter",
     //uuid  或者不填  默认走李总统一id生成机制
     "primarykeyType":"integer",
	 //数据连接池id
	 "dbid":DEFAULT_DBID
};

/** 
@param 自定义参数 详见 InsertSetting 
@param sycn  同步异步
@param fn 回调函数  function(data)
                           data.mgs 后台提示信息
                           data.total 更改或插入总条数
                           data.success 异常返回 false  正常返回true
						   data.idValues  []  如果是insert 返回 插入id 数组形式
**/ 
function commonInsertOrUpdate(params, sync ,fn) {
	var url = COMMON_ADD_URl;
	//
	var columnValues=params.columnValues;
	var newPat = $.extend(true,{},InsertSetting, params);
	//判断params 是否是数组对象
	var type=$.type(columnValues);
	if(type=="array"){
	    newPat.columnValues=JSON.stringify(columnValues);
	}else if(type=="object"){
	   newPat.columnValues="["+JSON.stringify(columnValues)+"]";
	}else{
	   alert("通用insert 方法-参数格式错误! columnValues应json格式");
	   return ;
	}
	
	
	$.ajax({
		async : sync,
		type : "post",
		url : url,
		dataType : "json",
		data : newPat,
		success : function(data) {
			if (fn && typeof(fn) == 'function') {
				fn(data);
			}
		},
		error : function(r) {					
			if (fn && typeof(fn) == 'function') {
				fn('error');
			}
		}
	});
}
//查询参数默认值
/**
  注意 有or条件查询 请自定义查询语句
 **/
var selectSetting={
   
      // 说明 ：  查询字段 用逗号隔开 没有为*
      // 使用示例  如果要查name age 字段  select name,age from table
      //          columns:name,age
      columns:"*",
      // 说明 :  where 条件中 等于号的字段  
      // 使用示例  如果要查select name,age from table  where name='guo'
      //          equal[表单中的name]: 数据中字段名称 name
      // 或者       定义成字符   name,age的形式
      equal:{},
      // 说明 :  notequal 不等于条件 使用方法同上    TODO 还未实现
      notequal:{},
	  // 说明 :  where 条件是like的字段 用法同上     TODO 目前通配符只支持%  需用户手动输入 
	  like:{},
	  // 说明 :  where 条件是>=的字段 与上用法区别 不支持字符串输入   
	  moreThan:{},
	  // 说明:   where 条件<=的      用法同上 
	  lessThan:{},
	  // 说明:   表单转换的json 上面  equal  notequal like moreThan lessThan 是根据key 
	  //         到此参数中取值拼成sql
	  // 格式     {表单中name:输入值}
	  columnValues:{},
	  //表名
	  tableName:"",
	  //排序字段  说明: 排序字段
	               //用法示例 :  如果根据 id 和 name排序 
	               //                orderBy:id,name
	  orderBy:"",
	  //偏移量   说明: 单使用默认分页时 当前记录表示查询的起始条数
	  offset:0,
	  //显示条数 说明: 此条表示分页每页显示多少条记录
	  rows:10,
	  //主键     说明: 根据主键分页时用到 如果主键不是id字段 请修改此处值
	  primarykey:"id",
	  //当页最后一条记录的值  说明: 此处 如使用id分页 请当页最后一条（最大的id）赋给此参数
	  //TODO 未做测试
	  primaryKeyValue:"0",	
	  //nameSpace  两种用法  1.使用默认增删改是 如需根据整形id分页  则将
	                       //nameSpace 改成  com.zinglabs.apps.commons.pojo.getList_forId
	                       //2.如使用自定义查询sql语句 此处应改成mybaits中 SQL的映射路径
	  nameSpace:"com.zinglabs.apps.appCommonCurd.pojo.getList", 
	  //是否开启分页 说明 ： true 开启分页 false 不开启分页  
	  isPagination:"true",
	  //自己 处理方法 需在 spring配置文件定义 bean 自定义xml文件 
	  //如参数不需处理 请将beanId改成 commonFreeFilter 
	  "beanId":"commonSelectFilter",
	  //数据连接池id
	  "dbid":DEFAULT_DBID
};	
/**
  通用查询 参数   @params params 参数见上方 selectSetting
               @params sync   同步异步
               @params fn(data)     回调函数
                         data.success 成功返回true 失败返回 false
                         data.mgs     提示信息     如成功返回成功，失败返回异常
                         data.total   如果使用分页 则返回总条数
                         data.data    查询出的数据集合
**/	
function commonSelect(params,sync,fn) {
    
	var url = COMMON_FIND_URL; 
	//通用查询参数
	if(params.nameSpace!=excelSetting.nameSpace&&params.nameSpace!=undefined&&params.nameSpace!=""&&params.nameSpace!=null){
	
	
	   var newpat=params;
	
	}else{
	   var newpat =$.extend(true,{},selectSetting,params);
		//此处代码抽离
		if(newpat.equal!=undefined&&newpat.equal!=null&&$.type(newpat.equal)=="string"){
		    var equalArr=newpat.equal.split(","); 
		    var equalObj={};
		    for(var i=0;i<equalArr.length;i++){
		       equalObj[equalArr[i]]=equalArr[i];
		    }
		    equalObj=JSON.stringify(equalObj);
	        newpat.equal=equalObj;
		}else if(newpat.equal!=undefined&&newpat.equal!=null&&$.type(newpat.equal)=="object"){
		     var equalObj={};
		      equalObj=JSON.stringify(newpat.equal);
	          newpat.equal=equalObj;
		}
		if($.type(newpat.like)=="string"){
		    var equalArr=newpat.like.split(",");
		    var equalObj={};
		    for(var i=0;i<equalArr.length;i++){
		       equalObj[equalArr[i]]=equalArr[i];
		    }
		    equalObj=JSON.stringify(equalObj);
	        newpat.like=equalObj;
		}else if($.type(newpat.like)=="object"){
		     var equalObj={};
		     equalObj=JSON.stringify(newpat.like);
	         newpat.like=equalObj;
		}
		if($.type(newpat.moreThan)=="object"){
		     var temp=newpat.moreThan;
		     newpat.moreThan=JSON.stringify(temp);
		}
		if($.type(newpat.lessThan)=="object"){
		     var temp=newpat.lessThan;
		     newpat.lessThan=JSON.stringify(temp);
		}
	    var columnValues=newpat.columnValues||"";
	    if(columnValues!=""&&columnValues!="{}"){
	      	newpat.columnValues=JSON.stringify(columnValues);
	    }else{
			newpat.columnValues=columnValues;
		}
		
		
	
	}
	


	 
	 
	//验证相关参数

	$.ajax({
		async : sync,
		type : "post",
		url : url,
		dataType : "json",
		data : newpat,
		success : function(data) {
			if (fn && typeof(fn) == 'function') {
				fn(data);
			}
		},
		error : function(r) {					
			if (fn && typeof(fn) == 'function') {
				fn('error');
			}
		}
	});
}

/**
  通用更改参数

**/
var UpdateSetting={
      //表名
     "tableName":"",
     //可自定义
     "nameSpace":"com.zinglabs.apps.appCommonCurd.pojo.common_update",
     //表单序列化 格式类型 {"name":"guo","age":"120"},{"name":"zhi","age":"1"}
     "columnValues":{},
     //主键值  String  格式  primarykey:"id"
     "primarykey":"id",
     //自己 处理方法 需在 spring配置文件定义 bean
      "beanId":"commonUpdateFilter",
	  "dbid":DEFAULT_DBID
}
/**
 @param  params  json 里的值 UpdateSetting中定义的
 @param  sync 同步/异步  
 @param  fn(data)回调
                 data.mgs 提示消息
                 data.success true/false
                 data.total 总共执行sql的条数
**/

function commonUpdate(params,sync,fn) {
	var url = COMMON_UPDATE_URl;
	var newPat = $.extend(true,{},UpdateSetting , params);
	var columnValues=params.columnValues;
	var primarykey=newPat.primarykey;
	if($.type(columnValues)=="object"){
	   if(columnValues[primarykey]==undefined||columnValues[primarykey]==null||columnValues[primarykey]==""){
	      alert("通用update 主键值不能为空！");
	       return ;
	   }
	}else{
       alert("通用update方法columnValues参数不正确！");
       return;
	}
	newPat.columnValues=JSON.stringify(columnValues);
	
	$.ajax({
		async : sync,
		type : "post",
		url : url,
		dataType : "json",
		data : newPat,
		success : function(data) {
			if (fn && typeof(fn) == 'function') {
				fn(data);
			}
		},
		error : function(r) {					
			if (fn && typeof(fn) == 'function') {
				fn('error');
			}
		}
	});
}

/***
  通用删除全部参数

**/
var DeleteSetting={
        //表名
     "tableName":"",
     //可自定义
     "nameSpace":"com.zinglabs.apps.appCommonCurd.pojo.common_delete",
     //  格式   用逗号隔开 "1,2,3,4,5,6,7" 或 数组 
     "columnValues":"",
     //主键值  String  格式  primarykey:"id"
     "primarykey":"id",
     //自己 处理方法 需在 spring配置文件定义 bean
     "beanId":"commonDeleteFilter",
     "dbid":DEFAULT_DBID
}
/**
  通用删除

 @param  params  json 里的值 DeleteSetting中定义的
 @param  sync 同步/异步 
 @param  fn(data)回调
                 data.mgs 提示消息
                 data.success true/false
                 data.total 总共执行sql的条数
**/
function commonDelete(params,sync,fn) {
	var url = COMMON_DEL_URl;
	var newPat = $.extend(true,{},DeleteSetting, params);
	var columnValues=params.columnValues;
	var type=$.type(columnValues);
	if(type=="array"){
	  newPat.columnValues=columnValues.join(",");
	}else if(type=="string"||type=="number"){
	  newPat.columnValues=columnValues;
	}else{
	 alert("通用删除 columnValues 格式不正确");
	 return;
	}
	$.ajax({
		async : sync,
		type : "post",
		url : url,
		dataType : "json",
		data : newPat,
		success : function(data) {
			if (fn && typeof(fn) == 'function') {
				fn(data);
			}
		},
		error : function(r) {					
			if (fn && typeof(fn) == 'function') {
				fn('error');
			}
		}
	});
}
/***
  拼装通用查询参数
    @params fromId  表单id
    注意格式:  表单项需要加data-sType属性,用于区分查询条件  like/equal/moreThan/lessThan
                时间段表单项   起始时间以timebegin结尾    结束时间要以timeend 结尾
**/

function spellSelectParams(fromId){
   var params={};
   var lessThan={};
   var moreThan={};
   var like={};
   var equal={};
   var columnValues={};
   params.lessThan=lessThan;
   params.moreThan=moreThan;
   params.like=like;
   params.equal=equal;
   params.columnValues=columnValues;
   $('#'+fromId+' :input').each(function(index,obj) {
        var type = obj.type;
        var tag = obj.tagName.toLowerCase();
        var $obj=$(obj);
        var value=$obj.val();
        if (type == 'text' || type == 'password' ||type == 'hidden' || tag == 'textarea'||tag == 'select'){
            var sType=$obj.attr('data-sType');
            if(sType=="equal"){
              equal[$obj.attr('name')]=$obj.attr('name');
            }else if(sType=="moreThan"){
              //如果时间为空跳过
              if(value!=""){
                   var  nameTmp=$obj.attr('name');
	               nameTmp= nameTmp.substring(0,nameTmp.lastIndexOf('timebegin'));
	               moreThan[$obj.attr('name')]=nameTmp;
              } 
              
              
            }else if(sType=="lessThan"){
             //如果时间为空跳过
                if(value!=""){
                   var  nameTmp=$obj.attr('name');
	               nameTmp= nameTmp.substring(0,nameTmp.lastIndexOf('timeend'));
	               lessThan[$obj.attr('name')]=nameTmp;
                }    
            } else{
                 //默认为like
                 like[$obj.attr('name')]=$obj.attr('name');
            }
            columnValues[$obj.attr('name')]=$obj.val(); 
          }  
      //TODO checkbox   处理
    });
  
   return params;
}
/**
  update 参数
**/
var transactionUpdateSetting=$.extend(true,UpdateSetting,{
	    
	  "clazz":""
	
});

/**
事物性提交

主要用于领取流程 ，任务分配等功能、

**/
function transactionUpdate(params,sync,fn){
	var url = COMMON_TRANSACTION_UPDATE_URL;
	var newPat = $.extend(true,{},transactionUpdateSetting , params);
	var columnValues=params.columnValues;
	var primarykey=newPat.primarykey;
	if($.type(columnValues)=="object"){
	   if(columnValues[primarykey]==undefined||columnValues[primarykey]==null||columnValues[primarykey]==""){
	      alert("通用update 主键值不能为空！");
	       return ;
	   }
	}else{
       alert("通用update方法columnValues参数不正确！");
       return;
	}
	newPat.columnValues=JSON.stringify(columnValues);
	
	$.ajax({
		async : sync,
		type : "post",
		url : url,
		dataType : "json",
		data : newPat,
		success : function(data) {
			if (fn && typeof(fn) == 'function') {
				fn(data);
			}
		},
		error : function(r) {					
			if (fn && typeof(fn) == 'function') {
				fn('error');
			}
		}
	});
	
}

/**
继承selectSetting  
**/
var excelSetting=$.extend(true,{},selectSetting,{
	  //
	  isPagination:"false",
	  //文件名称
	  fileName:"",
	  //标题
	  fTitle:"",
	  //excel表头
	  attrNames:"",
	  //统计 将字符串导出
	  statistical:""
});
//通用导excel函数

function z_exportExcel(params,fn){
	var url = COMMON_EXPORT_URL; 
	//通用查询参数
	
	
	if(params.nameSpace!=excelSetting.nameSpace&&params.nameSpace!=undefined&&params.nameSpace!=""&&params.nameSpace!=null){
	
	
	   var newpat=params;
	
	}else{
		var newpat =$.extend(true,{},excelSetting,params);
		//此处代码抽离
		if($.type(newpat.equal)=="string"&&newpat.equal!=""){
		    var equalArr=newpat.equal.split(","); 
		    var equalObj={};
		    for(var i=0;i<equalArr.length;i++){
		       equalObj[equalArr[i]]=equalArr[i];
		    }
		    equalObj=JSON.stringify(equalObj);
	        newpat.equal=equalObj;
		}else if($.type(newpat.equal)=="object"&&newpat.equal.length>0){
		     var equalObj={};
		      equalObj=JSON.stringify(newpat.equal);
	          newpat.equal=equalObj;
		}
		if($.type(newpat.like)=="string"&&newpat.like!=""){
		    var equalArr=newpat.like.split(",");
		    var equalObj={};
		    for(var i=0;i<equalArr.length;i++){
		       equalObj[equalArr[i]]=equalArr[i];
		    }
		    equalObj=JSON.stringify(equalObj);
	        newpat.like=equalObj;
		}else if($.type(newpat.like)=="object"&&newpat.like.length>0){
		     var equalObj={};
		     equalObj=JSON.stringify(newpat.like);
	         newpat.like=equalObj;
		}
		if($.type(newpat.moreThan)=="object"&&newpat.moreThan.length>0){
		     var temp=newpat.moreThan;
		     newpat.moreThan=JSON.stringify(temp);
		}
		if($.type(newpat.lessThan)=="object"&&newpat.lessThan.length>0){
		     var temp=newpat.lessThan;
		     newpat.lessThan=JSON.stringify(temp);
		}
	    var columnValues=newpat.columnValues||"";
	    if(columnValues!=""&&columnValues!="{}"){
	      	newpat.columnValues=JSON.stringify(columnValues);
	    }else{
			newpat.columnValues=columnValues;
		}
	}
	//验证相关参数 escape

	excelDownload(newpat);

}
/**
  params  key-value 形式  
  	  isPagination:"false",
	  //文件名称
	  fileName:"",
	  //标题
	  fTitle:"",
	  //excel表头
	  attrNames:"",
	  //统计 将字符串导出
	  statistical:""
**/
function excelDownload(params){
		var $form=window.top.$("#excel_download_form");
	
		//$form.empty();
		var form=$form[0];
		//防止单页测试时报错
		if(form==undefined||form==null){
		   $("body").append('<form id="excel_download_form" target="new" method="post" onsubmit="return false;" action="#">   </form>');
		   $form=$("#excel_download_form");
		   form=$form[0];
		}
		$form.html('');
		setTimeout(function(){
		    form.setAttribute("action",COMMON_EXPORT_URL);
			//表单赋值
			for(var key in params){
				        value=params[key];
				        if(typeof value=="object"){
				          value=JSON.stringify(value);
				        }
				    	if(value!=undefined && value!=null){
						      var inputTag = document.createElement("input");  
						      inputTag.type="hidden";  
							  inputTag.name=key; 
							  inputTag.id=key; 
							  inputTag.value=value;
							  form.appendChild(inputTag);
						}	
				 }
		    form.submit();
		},100)
}
var freeSettring={
       nameSpace:"",
       "beanId":"commonSelectFilter",
	   //数据连接池id
	   "dbid":DEFAULT_DBID,
	   //其他 例如sql 参数
	   "otherParams":""
}
/**
  通用增改删自定义sql方法
  需传入nameSpcace id
  和参数
**/
function commonCURD(params,sync,fn){
    var newpat =$.extend(true,{},freeSettring,params);
    var url = newpat.url;
    if(url == undefined || url == ''){
		zinglabs.alert("没有url参数！");
		return;
	}
	var nameSpace = params.nameSpace;
	  if(nameSpace == undefined || nameSpace == ''){
		zinglabs.alert("没有nameSpace参数！");
		return;
	}
	var beanId = params.beanId;
	  if(beanId == undefined || beanId == ''){
		zinglabs.alert("没有beanId参数！");
		return;
	}
    url = newpat.url ;
	//验证相关参数
	$.ajax({
		async : sync,
		type : "post",
		url : url,
		dataType : "json",
		data : newpat,
		success : function(data) {
			if (fn && typeof(fn) == 'function') {
				fn(data);
			}
		},
		error : function(r) {					
			if (fn && typeof(fn) == 'function') {
				fn('error');
			}
		}
	});
}
$(function (){
   if(typeof JSON == 'undefined'){
       $('head').append($("<script type='text/javascript' src='json2.js'>"));
    }
});

