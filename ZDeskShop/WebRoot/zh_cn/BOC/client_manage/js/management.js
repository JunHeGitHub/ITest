//遮罩属性		
var maskContent = {
		css : {
			border : 'none',
			padding : '15px',
			backgroundColor : '#000',
			'-webkit-border-radius' : '10px',
			'-moz-border-radius' : '10px',
			opacity : .5,
			color : '#fff',
			font : '14px'
		},
		message : '<img src="../../img/wait.gif" border="0" />  正在加载中，请稍候...'
	};
var yijieshouxinxiCurdStatus="false";
var fullName="Z_Management";
var rowSelTr=false;
$(document).ready(function() {
           
   $.blockUI(maskContent);
   $('#yijieshouxinxiGrid').DataTable({
        //滚动条
        //scrollY: 500,
        //scrollX: false,
        //延迟加载
         deferRender: true,
         processing: false,  
         //开启服务端分页
         serverSide: true,
         //服务器端分页ajax请求 可抽离出一个方法
        ajax: doSearch,
		pagingType: "full_numbers",
        pageLength: 10,
        bPaginate: true, //翻页功能
        bLengthChange: true, //改变每页显示数据数量
        bFilter: true, //过滤功能
        bSort: true, //排序功能
        bInfo: true,//页脚信息
        bAutoWidth: false, //自动宽度
        sPaginationType: "bootstrap",//分页样式
        dom: 'TRlrtip',
        columns: [
              {title:"序号",data: "id", defaultContent: '', width:'3%',orderable: false },
              {title:"图片名称" ,data: 'name',width:'6%', visible: true},
              {title:"背景颜色" ,data: 'color',width:'6%', visible: false},
              {title:"图片地址" ,data: 'imgurl',width:'6%', visible: false},
              {title:"链接地址" ,data: 'url',width:'6%', visible: true},
              {title:"编号" ,data: 'weight',width:'6%', visible: true},
              {title:"是否启用" ,data: 'enable',width:'6%', visible: true},
              {title:"图片类型" ,data: 'type',width:'6%', visible: true}
             ],
        columnDefs: [ {
            "searchable": false,
            "orderable": false,
            "targets": 0
        } ],
        
        oLanguage: {
             infoFiltered:function(){
           		
             },
            "sLengthMenu": "每页显示 _MENU_条",
            "sZeroRecords": "没有找到符合条件的数据",
            "sProcessing": "&lt;imgsrc=’../..img/wait.gif’ /&gt;",
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
            fnRowSelected: function ( nodes ) {
                   var index=nodes[0]._DT_RowIndex;	
                   rowSelTr=index;
	               var data = table.row(index).data();
	               openEdit(data);
             },
            sRowSelect:'multi',
            
            aButtons: []
        }
    });
    
   
    
    //自定义列插件绑定
    //绑定事件 on
    var table= $('#yijieshouxinxiGrid').DataTable();
    initValidate("yijieshouxinxiAddForm");
    initValidate("yijieshouxinxiEditForm");
     
    $.unblockUI();

});


/**
 *
 * selector:被填充combo的选择表达式
 * comboType：下列列表载入标识  字典索引
 * comboParam：联动标识         联动索引
 *
 * @desc填充指定的combo
 *
 * */
 function fillCombo(selector,comboType,comboParam){
	    if(comboType.indexOf('@')==0){
	       var len=comboType.indexOf('_');
	       if(len>0){
	          var type=comboType.substring(1,len);
	          //参数
	          var cs=comboType.substring((len+1),comboType.length)
	          if(type=="sql"){
	          
	             var params={
	                  nameSpace:cs,
					  beanId:"commonSelectFilter",
					  //模板生成
					  dbid:"ZDesk",
	             };
	              commonSelect(params,true,function(data){
			          try{
			              if(data&&data.data){
			               //TODO 进行一次转换
                            dictData=data.data; 
                            $.each(dictData, function(index,element) {
						        $(selector).append('<option value="'+element.code+'">' + element.name + '</option>');
						        
						    }); 
			              }
			       }catch(ex){
			             logErr("查询失败 failed "+ex.name + ": " + ex.message);
			       }
			       
			       });  
	             
	          }else if(type=="static"){
	              //TODO velocity 生成
	               var data= [{'code':'123','name':'123'},{'code':'456','name':'456'},{'code':'789','name':'789'}];
	               $(selector).append('<option value=""></option>');
	               $.each(data, function(index,element) {
						        $(selector).append('<option value="'+element.code+'">' + element.name + '</option>');
						        
					}); 
	          
	          }
	         
                
	       
	       
	       }else{
	         //非正确规则格式
	       } 
	    }else{
	        var dictData=window.top.getDictListfoIndexData(comboType)||{};
	        $(selector).empty();
	        $.each(dictData, function(index,element) {
		        $(selector).append('<option value="'+element.code+'">' + element.name + '</option>');
		    });
		
	    }
}

function doSearch(data, callback, settings){
				//进来先把提交和删除按钮隐藏掉
				$("#keySubmit").hide();
				$("#keyDelete").hide();
                var params=spellSelectParams("searchDataForm");
                params.moreThan={"typebegin":"type"};
			    params.lessThan={"typeend":"type"};
			    params.columnValues.typebegin=3;
			    params.columnValues.typeend=4;
			    params.tableName="Z_Management";
			    params.rows=settings._iDisplayLength;
			    params.offset=settings._iDisplayStart;
			    $.blockUI(maskContent);
			    commonSelect(params,true,function(data){
			          try{
			              if(data&&data.data){
			                  var pdata={};
			                  var size=data.data.length;
			                  for(var i=0;i<size;i++){
			                  	if(data.data[i].enable==0){
			                  		data.data[i].enable="已禁用";
			                  	}else if(data.data[i].enable==1){
			                  		data.data[i].enable="已启用";
			                  	};
			                  	if(data.data[i].type==3){
			                  		data.data[i].type="小图";
			                  	}else if(data.data[i].type==4){
			                  		data.data[i].type="大图";
			                  	};
			                    data.data[i].DT_RowNumber=i+1;
			                     
			                    }
			                 
			                  //数据
			                  pdata.data=data.data;
			                 
			                  //总条数
			                  pdata.recordsTotal=data.total||0;
			                   //过滤没有使用到总条数
			                  pdata.recordsFiltered=data.total||0;
			                  // 代表第几次画页面  如pdata.draw 不能删 如删掉不会翻页  
			                  pdata.draw=settings.iDraw;
			                  
			                  callback(pdata);
			                 
			               }
			          }catch(ex){
			             logErr("查询失败 failed "+ex.name + ": " + ex.message);
			          }  
			          $.unblockUI();    
			    });
};


/***
 * @TODO
 * 通用，调试完成后分文件，不要加Velocity代码
 */

function doEdit(){
   
   var bool= $("#yijieshouxinxiEditForm").validate().form();
   if(bool){
            $.blockUI(maskContent);
            var params={};
		    var cValues=zkm_getHtmlFormJsons("yijieshouxinxiEditForm");
		    //主键的值
		    params.columnValues=cValues;
		    //cValues.msgReceiver="@cookie_zhi";
		    params.tableName=fullName;
		    //如更改不是id 需定义 params.primarykey
		    commonUpdate(params,true,function(data){
		       try{
		                    var tableTmp=$("#yijieshouxinxiGrid").dataTable();
		                    if(data.success){
		                        try{
		                            tableTmp.fnUpdate(params.columnValues, rowSelTr );
		                            showSuccess("修改成功!");
		                             $("#yijieshouxinxiEdit").hide();
		                        }catch(e){
		                             showError("异常！");
		                            logErr("Exception need see log "+e.name + ": " + e.message);
		                        }
		                    }else{
		                        logErr("Exception edit failed  "+data +" "+("Items" in data));
		                        showError("修改失败");
		                    }         
		          }catch(ex){
		                     showError("异常！");
		                    logErr("doEdit 2222 Excep "+ex.name + ": " + ex.message);
		           }
		           $.unblockUI();
		   
		   });
   
       return false;
   }   
  
   
   return "edit";

};


/***
 * @TODO
 * 通用，调试完成后分文件，不要加Velocity代码
 */

function doAdd(){
  
     var bool= $("#yijieshouxinxiAddForm").validate().form();
      
     if(bool){
               $.blockUI(maskContent);
	           var cValues=zkm_getHtmlFormJsons("yijieshouxinxiAddForm");
			    var params={};
			    //cValues.readFlag="@cookie_name";
			    params.columnValues=cValues;
			    params.tableName=fullName;
			    //使用增
			    commonInsertOrUpdate(params,true,function(data){
			        try{
	                    if (data) {
	                        var tableTmp=$("#yijieshouxinxiGrid").DataTable();
	                        if(data.success){
	                            try{
	                                params.columnValues.id=data.idValues[0];
	                                tableTmp.row.add(params.columnValues).draw();
	                                showSuccess("添加成功!");
	                                $("#yijieshouxinxiAdd").hide();
	                            }catch(e){
	                                 showError(e.message);
	                                logErr("Exception need see log "+e.name + ": " + e.message);
	                                yijieshouxinxiCurdStatus="add";
	                            }
	                            doCleanAddForm();
	                        }else{
	                            showError(data.mgs);
	                            logErr("Exception add failed  "+data +" "+("Items" in data));
	                            yijieshouxinxiCurdStatus="add";
	                        }
	                        
			               }
	
	                }catch(exx){
	                    logErr("Exception need see log "+exx.name + ": " + exx.message);
	                   // doAddModalClean();
	                    showError(data.mgs)
	                   yijieshouxinxiCurdStatus="add";
	                }  
			            $.unblockUI();
			    });   
			//添加成功 数据清理状态 
			yijieshouxinxiCurdStatus=false;
	     }else{
	       //添加失败据需添加
	         yijieshouxinxiCurdStatus="add";
	     }
};


/***
 * @TODO
 * 通用，调试完成后分文件，不要加Velocity代码
 */

function doDelete(){
    zinglabs.confirm("确认要删除此项？",function(){
      var params={};
       $.blockUI(maskContent);
	   params.tableName=fullName;
	   //主键默认为id  如不为id 加上params.primarykey
	   var tableTmp=$('#yijieshouxinxiGrid').DataTable();
	   var cell=tableTmp.rows(".active").data();
	   var ids="";
	   for(var i=0;i<cell.length;i++){
	          var key="id";
	         if("primarykey" in params){
	             key=params["primarykey"];
	         }
	         ids+=cell[i][key]+",";
	     }
	     params.columnValues=ids;
	     commonDelete(params,true,function(data){
	         if (data) {
	                    if(data.success){
	                        try{
	                            tableTmp.row('.selected').remove().draw(false);
	                            showSuccess("删除成功!");
	                            $("#yijieshouxinxiEdit").hide();
	                        }catch(e){
	                            logErr("Exception del need see log "+e.name + ": " + e.message);
	                        }
	                        
	                    }else{
	                        logErr("Exception del failed  "+data +" "+data.retcode);
	                        showError("删除失败");
	                    }
	                }
	           $.unblockUI();
	       
	    }); 
 });
};


/***
 * @TODO
 * 通用，调试完成后分文件，不要加Velocity代码
 */

function BCreateDelegate(dparam,method){
    return function() {
        return method.apply(dparam, arguments);
    }
};

function BRegisterExtEvent(sid,tid,sEvent,fnStr,paramStr){
    try{
        var sfn=eval(fnStr);
        for(var j=0;j<sfn.length;j++){
            var fnt=sfn[j];
            var fn=fnt.fn;
            var fp=fnt.fp;
            if(fn){
                var parm={'srcId':sid,'srcEvent':sEvent,'targetId':tid,'fn':fnt};
                //合并参数，以parm为主
                parm=$.extend(fp,parm);
                if(typeof(fn)=='function'){
                    var callback=BCreateDelegate(parm,fn);
                    $("#"+sid).bind(sEvent,parm,callback);

                    log.debug(" register target : id - " + sid + " event : " + sEvent);
                }else{
                    log.error(" fn : " + fn + " is not a function");
                }
            }else{
                log.error(" fn is null");
            }
        }
    }catch(e){
        logErr("Exception BRegisterExtEvent need see log "+e.name + ": " + e.message);
    }


};

/*******************************************************************************
 * 
 * @desc 指定panel赋值 和清空
 * @todo 只测试了主要类型 , checkbox、radio没测试
 * 
 * pVars.id panel id pVars.data 赋值数据 pVars.isClear 字符串 "true" 清空此panel
 * 
 * 
 * @TODO 通用，调试完成后分文件，不要加Velocity代码
 * 
 */
function setPanelVal(pVars) {
	$('#' + pVars.id + ' :input').each(function(index, obj) {
		var type = obj.type;
		var tag = obj.tagName.toLowerCase();
		if (("isClear" in pVars) && pVars.isClear == 'true') {
			if (type == 'text' || type == 'password' || tag == 'textarea'
					|| type == 'hidden')
				$(obj).val('');
			else if (type == 'checkbox' || type == 'radio')
				$(obj).attr('checked', false);
			else if (tag == 'select')
				$(obj).attr('selectedIndex', -1);
		} else {
			var vTmp = pVars.data[this.name];
			if(vTmp=="大图"){
				vTmp=4
			}else if(vTmp=="小图"){
				vTmp=3
			}else if(vTmp=="已启用"){
				vTmp=1
			}else if(vTmp=="已禁用"){
				vTmp=0
			}
			
			if (BIsNullVal(vTmp)) {
				vTmp = '';
			}
			if (type == 'text' || type == 'password' || type == 'hidden' || tag == 'textarea' || tag == 'select')
				$(obj).val(vTmp);
			else if (type == 'checkbox' || type == 'radio') {
				$(obj).attr('checked', vTmp == '' ? false : vTmp);
			} else if (tag == 'select') {
				$(obj).attr('selectedIndex', vTmp == '' ? -1 : vTmp);
			}
		}
	});
}



//每页显示条数
function setPageNum(obj){
    var table=$('#yijieshouxinxiGrid').DataTable();
    table.page.len(obj.value).draw();
}
//刷新
function refresh(){
   $("#searchDataForm")[0].reset();
   history.go(0);
}




/***
 * @TODO
 * 通用，调试完成后分文件，不要加Velocity代码
 */
function BIsNullVal(value) {
    return typeof(value)=="undefined" || value==null ||  (typeof(value)=='string' &&(value=="undefined" || value=='' || value=='null' )) ||  (typeof(value)=='boolean' &&value==false  );
};
// 触发datables ajax 方法
function search(){
   var tableTmp=$("#yijieshouxinxiGrid").DataTable();
   tableTmp.draw(true);
};






/***
 * 必须处理的严重错误，测试阶段可以alert 生产环境特殊标识写入日志
 * 发现就要分析原因
 * @param err
 */
function logErr(errMsg) {
    log.debug(errMsg);
//    alert(errMsg);
//    PT().log.error(errMsg);
};


/***
 * 嵌入ifram可以返回top parent
 * 非嵌入 可以返回 window ...
 * @returns {Window}
 * @constructor
 */
function PT(){
    return window;
};
//初始化验证组件
//fromid 表单ID
function initValidate(fromid){
  $("#"+fromid).validate({ 	   
				      //错误信息处理方法
				         errorPlacement: function (error, element){
				       //if(element.)
                          element.addClass('error'); 
                          //先销毁
                          element.tooltip('destroy');
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

//打开添加表单
function openAdd(){
  $("#yijieshouxinxiEdit").hide(); 
  $("#yijieshouxinxiAdd").show();
  $("#keySubmit").show();
  
  doCleanAddForm();
  
}

//打开修改表单
function openEdit(data){
   $("#yijieshouxinxiEdit").show();
   $("#yijieshouxinxiAdd").hide();
   $("#keySubmit").show();
   $("#keyDelete").show();
 
   yijieshouxinxiCurdStatus="edit";
   var pvars={"id":"yijieshouxinxiEdit","data":data};
   
   setPanelVal(pvars);
   
 
}
//提交方法
function doSubmit(){
   
 try{
          if(yijieshouxinxiCurdStatus=="add"){
              doAdd();
           
          }else if(yijieshouxinxiCurdStatus=="edit"){
              doEdit();
          }
        }catch(e){
            //        编辑、增加页面状态清理。一定要确保页面数据已经不再使用。
            zinglabs.alert("出现异常");
            yijieshouxinxiCurdStatus="false";
            logErr("Exception BtnAddSubmit need see log "+e.name + ": " + e.message);
        }
		
	 
}
//显示错误信息
function showError(mgs){
  //$("#errorDiv").show();
  //  $("#successDiv").hide();
  //  $("#errorDiv").html(mgs);
  //  setTimeout(function(){
  //    $("#errorDiv").hide();
  // },3000);
    zinglabs.alert(mgs);

}
//显示成功信息
function showSuccess(mgs){
  //  $("#errorDiv").hide();
  // $("#successDiv").show();
  //  $("#successDiv").html(mgs);
 //  setTimeout(function(){
  //     $("#successDiv").hide();
 //  },3000);
 zinglabs.alert(mgs);
}
//清空添加表单
function doCleanAddForm(){
   yijieshouxinxiCurdStatus="add";
   var pvars={"id":"yijieshouxinxiAdd","isClear":"true"};
   setPanelVal(pvars);
}


function cleanSearchForm(){
    $("#searchDataForm")[0].reset();
}
//打开文件上传
function openUpload(){
	var params ={};
	params.maxSize=10;//10M
	params.relativeKey='relativePath';
	params.reFillFn='success';
	params.isShowDialog = true;
	params.isMultiple=false;
	params.limitCount=1;
	useWebUploader("showDiv",params);
}

//上传成功回调方法
function success(data){
	var params={};
	//params.name=data[0].fileName;
	params.imgurl=data[0].relativeSavePath;
	if(yijieshouxinxiCurdStatus=="add"){
	$("#imgurl").val(params.imgurl);
	}else if(yijieshouxinxiCurdStatus=="edit"){
   	$("#imgurledit").val(params.imgurl);
   	}

}
