var APP_PROJECT_NAME=window.top.PRJ_PATH||"ZDesk";
//通用查询action路径
var COMMON_FIND_URL = "/" + APP_PROJECT_NAME + "/" + "ZKM/CommonsCurd" + "/" + "Find.action";
//通用Insert Action 路径
var COMMON_ADD_URl="/" + APP_PROJECT_NAME + "/" + "ZKM/CommonsCurd" + "/" + "Insert.action";
//通用Update Action路径
var COMMON_UPDATE_URl= "/" + APP_PROJECT_NAME + "/" + "ZKM/CommonsCurd" + "/" + "Update.action";
//通用delete Action 路径
var COMMON_DEL_URl= "/" + APP_PROJECT_NAME + "/" + "ZKM/CommonsCurd" + "/" + "Delete.action";
//通用 insertAction  路径 返回 id
var COMMON_Replace_URl="/" + APP_PROJECT_NAME + "/" + "ZKM/CommonsCurd" + "/" + "commonInsert.action";
//单表通用名称空间
var COMMON_CURD_NAMESPACE="com.zinglabs.apps.commons.pojo";
//获取自定义查询URL
//
var COMMON_ZDY_GEY_URL="/" + APP_PROJECT_NAME + "/" + "ZKM/CommonsCurd" + "/" + "FindForComplex.action?";
//获取自定义增删改URL
//
var COMMON_ZDY_GEY_URL_UPDATE="/" + APP_PROJECT_NAME + "/" + "ZKM/CommonsCurd" + "/" + "FindForComplex_update.action?";
/**
@param tableName 表名 String
@param  pat 参数  Object 例: {"primaryKey":"id","primaryKeyValue":"","username":"1","age":"1","hirdate":"1","dept":"1","job":"1","sal":"1","comm":"1","uuid":true}
*必添项:primaryKey uuid 其他为数据库对应的k-v
@param fn 回调函数  function
**/
function commonInsert(pat, fn) {
	var url = COMMON_ADD_URl;
	var commoninfo = {};
	commoninfo.tableName = pat.tableName;
	commoninfo.nameSpace = COMMON_CURD_NAMESPACE;
	commoninfo.nameSpaceId = "insert";
	var newPat = $.extend({}, pat, commoninfo);
	$.post(url, newPat, fn);
}
	
	
//通用查询 [id:表单id,tableName:表名,where:查询条件,limit:起始记录数,每页记录数,fn:回调函数]
function commonSelect(pat,sycn,fn) {
	var url = COMMON_FIND_URL;
	var commoninfo = assemblyParameters(pat);
	ajaxFunction(url, commoninfo, sycn, fn);
}




//拼装通用参数
function assemblyParameters(pat) {
	var commoninfo = {};
	if (pat.tableName == undefined || pat.tableName == null || pat.tableName == ""){
		alert("\u53c2\u6570\u96c6\u5fc5\u987b\u6709\u8868\u540d\u79f0");
		return false;
	}
	commoninfo.tableName = pat.tableName;
	commoninfo.nameSpace =COMMON_CURD_NAMESPACE;
	commoninfo.nameSpaceId = "getList";
	if (pat.where != undefined && pat.where != null && pat.where != "") {
		$.each(pat.where, function (k, v) {
			var value = v + "";
			eval("commoninfo." + k + "=" + "'" + value + "'");
		});
	}
	if (pat.orderBy != undefined && pat.orderBy != null && pat.orderBy != "") {
		commoninfo.orderBy = pat.orderBy;
	}
	if (pat.limit != undefined && pat.limit != null && pat.limit != "") {
		commoninfo.limit = pat.limit;
	}
	if (pat.start != undefined && pat.start != null && pat.start != "") {
		commoninfo.start = pat.start;
	}
	return commoninfo;
}



/**
  拼装URL get 请求
  @param  pat {} object
          
  
  **/

function getCommonsSelectUrl(pat) {
	var commoninfo = assemblyParameters(pat);
	var str = "";
	$.each(commoninfo, function (k, v) {
		str += k + "=" + v + "&";
	});
	var patStr = str.substr(0, str.length - 1);
	var url = COMMON_FIND_URL + patStr;
	return url;
}



/**
 @param

**/

function commonUpdate(pat, fn) {
	var url = COMMON_UPDATE_URl;
	var commoninfo = {};
	commoninfo.tableName = pat.tableName;
	commoninfo.nameSpace = COMMON_CURD_NAMESPACE;
	commoninfo.nameSpaceId = "update";
	var newPat = $.extend({}, pat, commoninfo);
	$.post(url, newPat, fn);
}





function commonDelete(pat, fn) {
	var url = COMMON_DEL_URl;
	var commoninfo = {};
	commoninfo.tableName = pat.tableName;
	commoninfo.nameSpace = COMMON_CURD_NAMESPACE;
	commoninfo.nameSpaceId = "del";
	var newPat = $.extend({}, pat, commoninfo);
	$.post(url, newPat, fn);
}

/**
自定义查询
**/
function commonSelectForComplex(pat,sync,fn) {
	var url =COMMON_ZDY_GEY_URL ;
	var commoninfo = checkParameters(pat);
	ajaxFunction(url, commoninfo, sync, fn);
}

/**
自定义增伤改
**/
function commonCRUDForComplex(pat,sync,fn) {
	var url =COMMON_ZDY_GEY_URL_UPDATE ;
	var commoninfo = checkParameters(pat);
	ajaxFunction(url, commoninfo, sync, fn);
}



//mybatis自定义配置文件查询
function checkParameters(pat) {
	if (pat.nameSpace == undefined || pat.nameSpace == null || pat.nameSpace == "") {
		alert("\u8bf7\u586b\u5199nameSpace");
		return false;
	}
	if (pat.nameSpaceId == undefined || pat.nameSpaceId == null || pat.nameSpaceId == "") {
		alert("\u8bf7\u586b\u5199nameSpaceId");
		return false;
	}
	return pat;
}

function getCommonsSelectUrlForComplex(pat) {
	var commoninfo = checkParameters(pat);
	var str = "";
	$.each(commoninfo, function (k, v) {
		str += k + "=" + v + "&";
	});
	var patStr = str.substr(0, str.length - 1);
	var url = COMMON_ZDY_GEY_URL + patStr;
	return url;
} 

/**
@param tableName 表名 String
@param  pat 参数  Object 例: {"primaryKey":"id","primaryKeyValue":"","username":"1","age":"1","hirdate":"1","dept":"1","job":"1","sal":"1","comm":"1","uuid":true}
*必添项:primaryKey uuid 其他为数据库对应的k-v
@param fn 回调函数  function
@return data.success true|false
@return data.mgs   操作成功 | 出现异常
@return data.data  主键的值  
**/
function commonInsertAndGetId(pat, fn) {
	var url = COMMON_Replace_URl;
	var commoninfo = {};
	commoninfo.tableName = pat.tableName;
	//commoninfo.nameSpace = COMMON_CURD_NAMESPACE;
	//commoninfo.nameSpaceId = "insertAutoId";
	var newPat = $.extend({}, pat, commoninfo);
	$.ajax({   
	            async : true,
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
					fn(r);
				}
				
	 });
}
/**
@param pat.tableName  String 表名
@param pat.where;  查询条件 where 后的条件  Object {name='guo',sex='boy'} 
@param pat.specialColumns  特殊条件  格式 key > 数据库字段(可不填 相当于数据库字段 条件 值),key1 =, key2 <
       pat.select;   格式  查询的列  例如  table.name, table2.name2,table.name3
       pat.leftjoin="";    例如  表名 on 表1.字段=表2字段
	   pat.rightjoin="";    同上
	   pat.outjoin="";      同上   
	   pat.innerjoin="";    同上 
       
@param fn 回调函数  function
@return data.success true|false
@return data.mgs   查询成功 | 出现异常
@return data.data  查询出的数据 
**/
function commonSearch(pat, sync ,fn) {
	var url ="/" + APP_PROJECT_NAME + "/" + "ZKM/CommonsCurd" + "/" + "commonSearch.action";;
	var commoninfo = {};
	var ColumnValuse=[];
	commoninfo.tableName = pat.tableName;
	var columns=pat.where;
	//特殊参数
	//字符串
	
	var specialColumns=pat.specialColumns;
	commoninfo.select=pat.select;
	commoninfo.leftjoin=pat.leftjoin;
	commoninfo.rightjoin=pat.rightjoin;
	commoninfo.outjoin=pat.outjoin;
	commoninfo.innerjoin=pat.innerjoin;
	//添加排序
	commoninfo.orderBy=pat.orderBy;
	//过滤掉特殊参数
	
	var specialColumn=pat.specialColumns;
	if(specialColumn){
		    specialColumn=specialColumn.split(',');
			for(var i=0;i<specialColumn.length;i++){
			      var tc=specialColumn[i]; 
			      //用空格
			      if(tc){
			          //去掉前后空格
			          tc=tc.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '');
				      tc=tc.split(" ");
				      if(tc.length>0){
						 var c=columns[tc[0]];
						 //TODO 判断 between and 
						 if(tc[2]!=undefined&&tc[2]!=null&tc[2]!=""){
						      specialColumn[i]=tc[2]+" "+tc[1];
						 }
						 if(c!=undefined&&c!=null&&c.length>0){
						         //通配符替换
						         if(tc[1]!=undefined&&tc[1]=="like"){
									    c="%"+c+"%";
								  }
								 ColumnValuse.push(c);
								 delete columns[tc[0]];
						  }else{
							   //去掉特殊参数
							    delete specialColumn[i];
						  }
					
				      }else{
				         alert("参数格式错误！");
				         return;
				      }
			      }
			
	     }
	}
	if(specialColumn&&specialColumn.length>0){
	  commoninfo.specialColumns=specialColumn.join(",");
	}
	if(ColumnValuse&&ColumnValuse.length>0){
	  commoninfo.teshucanshu=ColumnValuse.join(",");
	}
	
	commoninfo.columns=JSON.stringify(columns);
	

	$.ajax({   
	            async : sync,
				type : "post",
				url : url,
				dataType : "json",
				data : commoninfo,
				success : function(data) {
				   	if (fn && typeof(fn) == 'function') {
							fn(data);
				      }
				},
				error : function(r) {
					fn(r);
				}
				
	 });
}
//
$(function (){
   if(typeof JSON == 'undefined'){
       $('head').append($("<script type='text/javascript' src='../js/jsonMy.js'>"));
    }
});

