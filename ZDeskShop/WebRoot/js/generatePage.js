/**
生成页面用的js
**/




function Z_equalsIgnoreCase(value1,value2) {
	if(typeof(value1)=="undefined"||typeof(value2)=="undefined"||value1==null || value2==null){
	return false;
	}
	return (value1.toLowerCase()==value2.toLowerCase());
};

function Z_IsArray(o) {
          return o && (o instanceof Array) || o.constructor === Array;
}
function Z_IsEmpty20(value) {
	return typeof(value)=="undefined" || value==null ||  (typeof(value)=='string' &&(value=="undefined" || value=='' || value=='null' )) ||  (typeof(value)=='boolean' &&value==false  );
};
function Z_IsEmpty(value) {
	return typeof(value)=="undefined" || value==null||value=="undefined" ;
};

function Z_IsEmptyValue(value) {
	return value=="undefined"||value==null || value=='' ;
};

//空函数，XML用于定义button
function Z_emptyFun(){
	
}

function BIsNullVal(value) {
    return typeof(value)=="undefined" || value==null ||  (typeof(value)=='string' &&(value=="undefined" || value=='' || value=='null' )) ||  (typeof(value)=='boolean' &&value==false  );
};

/**
获取 statistical的值

**/
function BGetStatisticalVal(attrName,confN){
    if(!BIsNullVal(statistical[attrName])&&!BIsNullVal(statistical[attrName][confN])){
       return statistical[attrName][confN];
    }else{
        return "";
    }

};
/***
 *
 * 获取xmlJsonMap 的字段fieldN 属性confN的值
 *
 * */
function BGetConfVal(tableN,fieldN,confN) {
    if(!BIsNullVal(xmlJsonMap[tableN][fieldN]) && !BIsNullVal(xmlJsonMap[tableN][fieldN][confN])){
        return xmlJsonMap[tableN][fieldN][confN];
    }else{
        return "";
    }
};

/***
 *
 * 判断xmlJsonMap 的字段fieldN是否包含属性confN
 *
 * @TODO
 * 通用，调试完成后分文件，不要加Velocity代码
 *
 */

 function BIsConfValNull(tableN,fieldN,confN) {
    if(!BIsNullVal(xmlJsonMap[tableN][fieldN]) && !BIsNullVal(xmlJsonMap[tableN][fieldN][confN])){
        return false;
    }else{
        return true;
    }
};


/***
 *
 * 获取xmlJsonMap 中满足条件的值
 *
 * @TODO
 * 通用，调试完成后分文件，不要加Velocity代码
 *
 * */
function BGetConditionConfVal(tableN,condition,conVal) {
    for (var fieldTmp in xmlJsonMap[tableN]) {
        if(condition in xmlJsonMap[tableN][fieldTmp] && xmlJsonMap[tableN][fieldTmp][condition]==conVal){
            return xmlJsonMap[tableN][fieldTmp];
        }
    }
    return null;
};

/***
 * @TODO  加密算法
 * 通用，调试完成后分文件，不要加Velocity代码
 */
function Z_EU(str){
    return str.replace(/([\u0000-\uFFFF])/g, function($0){
        if($0.charCodeAt() <= 16){
            return '2_3000' + $0.charCodeAt().toString(16);
        }
        else if($0.charCodeAt() < 256) {
            return '2_300' + $0.charCodeAt().toString(16);
        }
        else {
            return '2_3' + $0.charCodeAt().toString(16);
        }
    });
} ;

//事件注册函数
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
/***
报表页面统计方法


**/
function Z_JSONDataStatistical(datas) {
     var result={};
     //遍历结果集
     for(var i=0;i<datas.length;i++){
          //取到单条数据 遍历规则看哪个字段需要计算
          var data=datas[i];
          
          for(var key in data){
              //统计配置
             // var statisticalMap=statistical[key];
             for(var statisticalkey in statistical){
                 if(key==statistical[statisticalkey]["target"]){
                   var dataValue=data[key];
            
              //数据表格中该字段对应的值
            
               
             // if(statisticalMap!=null&&statisticalMap!=undefined){
                  //进行计算
	
                  var expType=window.BGetStatisticalVal(statisticalkey,"expType");
                  var expName=window.BGetStatisticalVal(statisticalkey,"expName");	
                  var expValue=window.BGetStatisticalVal(statisticalkey,"expValue");
                  var target=window.BGetStatisticalVal(statisticalkey,"target");
                  var sAttrName= window.BGetStatisticalVal(statisticalkey,"sAttrName");
                  var desc= window.BGetStatisticalVal(statisticalkey,"desc");
                  var title=BGetStatisticalVal (statisticalkey,"title");  
                   if(result[sAttrName]==undefined||result[sAttrName]==null){
                           result[sAttrName]={};
                           result[sAttrName]["expValue"]=expValue;
                           result[sAttrName]["expName"]=expName;
                           result[sAttrName]["expType"]=expType;
                           result[sAttrName]["target"]=target;
                           result[sAttrName]["desc"]=desc;
                           result[sAttrName]["title"]=title;
                     }
		            if(Z_equalsIgnoreCase(expType,'count')){
							if(Z_IsEmpty(result[sAttrName]['S_Count'])){
							    //字段值置0
								result[sAttrName]['S_Count']=0;				
							}
		//				<statistical attrNameT="StatuName" desc="接听人数" expName="EQ" expType="count" expValue="双方通话" sAttrName="accCount" target="StatuName" title="接听人数"/>
							if(runExpInFloat(expName,dataValue,expValue)){
								result[sAttrName]['S_Count']++;
							}
						//r.get($(exp).attr('attrNameT'));
					  }else if(Z_equalsIgnoreCase(expType,'sum')){
						if(Z_IsEmpty(result[sAttrName]['S_Sum'])){
							result[sAttrName]['S_Sum']=0.0;				
						}
					    if(runExpInFloat(expName,dataValue,expValue)){
						
							if(target != null && target != undefined&& target!=''){
								//log.debug(statisticalsMap[ee]['S_Sum']+"    "+parseFloat(vTmp));
								result[sAttrName]['S_Sum']=(result[sAttrName]['S_Sum']+parseFloat(data[target]));
						    }
					     }
					  }else if(Z_equalsIgnoreCase(expType,'avg')){
						if(Z_IsEmpty(result[sAttrName]['S_Sum'])){
							result[sAttrName]['S_Sum']=0.0;				
						}
						if(Z_IsEmpty(result[sAttrName]['S_Count'])){
							result[sAttrName]['S_Count']=0;				
						}
						if(runExpInFloat(expName,dataValue,expValue)){
							var vTmp=data[target];
							if(vTmp != null && vTmp != undefined&& vTmp!=''){
								result[sAttrName]['S_Sum']=(result[sAttrName]['S_Sum']+parseFloat(vTmp));
							}
							result[sAttrName]['S_Count']++;
						}
					   }else if(Z_equalsIgnoreCase(expType,'sql')){
					
					    alert("之后支持！");
					
					   }
					   
			     }
              }
          }
      

   
	}
	 //生成数据
		var ret=genExpRet(result);
		if(ret!=""){
		  $("#tongji").html("&nbsp;&nbsp;&nbsp;&nbsp;"+ret);
		}else{
		  $("#tongji").html("&nbsp;&nbsp;&nbsp;&nbsp;无统计项");
		}
		
}; 

/**

	通用方法
	生成统计数据

**/
function genExpRet(expMap) {
  	var ret="";
  	for (var e in expMap) {
		  var statisticalsMap=expMap[e];
		//for (var ee in statisticalsMap) {
			var exp=statisticalsMap['exp'];
			var expType=statisticalsMap['expType'];
			if(Z_equalsIgnoreCase(expType,'count')){
				if(Z_IsEmpty(statisticalsMap['S_Count'])){
					statisticalsMap['S_Count']=0;				
				}
				ret+='<b>'+statisticalsMap['title']+':'+statisticalsMap['S_Count']+' '+statisticalsMap['desc']+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </b> ';
			}else if(Z_equalsIgnoreCase(expType,'sum')){
				if(Z_IsEmpty(statisticalsMap['S_Sum'])){
					statisticalsMap['S_Sum']=0.0;				
				}
				ret+='<b>'+statisticalsMap['title']+':'+parseFloat(statisticalsMap['S_Sum']).toFixed(2)+' '+statisticalsMap['desc']+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>  ';
			}else if(Z_equalsIgnoreCase(expType,'avg')){
				if(Z_IsEmpty(statisticalsMap['S_Sum'])){
					statisticalsMap['S_Sum']=0.0;				
				}
				if(Z_IsEmpty(statisticalsMap['S_Count'])){
					statisticalsMap['S_Count']=0;				
				}
				
				if(statisticalsMap['S_Count']==0){
					ret+='<b>'+statisticalsMap['title']+':'+0+' '+statisticalsMap['desc']+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </b> ';
				}else{
					ret+='<b>'+statisticalsMap['title']+':'+parseFloat((statisticalsMap['S_Sum']/statisticalsMap['S_Count'])).toFixed(2)+' '+statisticalsMap['desc']+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </b> ';
				}
			}else if(Z_equalsIgnoreCase(expType,'sql')){
			     alert("sql 的 之后支持");
				//if(Z_IsEmpty(statisticalsMap[ee]['S_SQL'])){
				//	statisticalsMap['S_SQL']='';				
				//}
				//ret+='<b>'+$(exp).attr('title')+':'+statisticalsMap['S_SQL']+' '+statisticalsMap['desc']+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b>  ';
			}
		//}
	}
	ret+="";
	return ret;
  };





//filter 相关
/**
 * 通用方法
 * @param {} exp
 * @param {} value1 record value
 * @param {} value2 target 
 * @return {Boolean}
 */
	function runExpInFloat(exp, value1,value2) {
		var floatV1,floatV2;
		try {
			floatV1=parseFloat(value1);
			if(exp!='GTLT'){
				floatV2=parseFloat(value2);				
			}
			if(isNaN(floatV2)){
				floatV1=''+value1;
				floatV2=''+value2;
			}
		} catch (e) {
//			log.debug("runExpInFloat use str");
			floatV1=''+value1;
			floatV2=''+value2;
		}
		
		if(exp=='EQ'){
			if(floatV1==floatV2){
				return true;
			}else{
				return false;
			}
		}else if(exp=='notEQ'){
			if(floatV1==floatV2){
				return false;
			}else{
				return true;
			}
		}else if(exp=='GT'){
			if(floatV1>floatV2){
				return true;
			}else{
				return false;
			}
		}else if(exp=='GTEQ'){
			if(floatV1>=floatV2){
				return true;
			}else{
				return false;
			}
		}else if(exp=='LT'){
			if(floatV1<floatV2){
				return true;
			}else{
				return false;
			}
		}else if(exp=='LTEQ'){
			if(floatV1<=floatV2){
				return true;
			}else{
				return false;
			}	
		}else if(exp=='GTLT'){
			var value2Arr=[];
			value2Arr= value2.split("!Z_S");
			if(parseFloat(value1)<parseFloat(value2Arr[1]) && parseFloat(value1)>parseFloat(value2Arr[0])){
				return true;
			}else{
				return false;
			}
		}else if(exp=='regex'){
			var regex=value2;
			if(regex.test(value1)){
				return true;
			}else{
				return false;
			}
		}else if(exp=='EQ_VALUE_OR'){
			var value2ArrO=[];
			value2ArrO= value2.split("!Z_S");
			for(var ii=0;ii<value2ArrO.length;ii++){
				if(value1==value2ArrO[ii]){
					return true;
				}
			}
			return false;
		}
	};


//XML Filter 拼装
function  getFilterSql(filterParams){
		var restr="";
		if(filterParams!=null && filterParams.length>0){
			var connectorTmp=null;
			var filterTmp=null;
			var termRelation=null;
			var field=null;
			for(var i=0;i<filterParams.length;i++){
				filterParamTmp=filterParams[i];
				var be=filterParamTmp["begin"];
				var ed=filterParamTmp["end"];
				if(be!=undefined &&be!=null && be!=""){
					restr +=" " + be + " ";
				}
	//TODO 这里先不考虑Connector  里多个 filter 的状况  如果需要修改VM
	//			for(var j=0; j<filterParamTmp["attr"].length;j++){
	//			    filterTmp=filterParamTmp["attr"][j];
	//				restr +=" " + getOneTypefilterSql(filterTmp) + " ";
	//			}
	           	filterTmp=filterParamTmp["attr"];
				restr +=" " + getOneTypefilterSql(filterTmp) + " ";
			
				if(ed!=undefined &&ed!=null && ed!=""){
					restr +=" " + ed + " ";
				}
			}
		}
		return restr;
}
// 此方法 实现ConfXmlObj.java  getOneTypefilterSql 方法
// TODO 后续完善 else if()
function getOneTypefilterSql(xmlattrs){
		var type=xmlattrs["type"];
		if(type==null || type.length==0){
			type="default";
		}
		var  sql="";
		var  sqlRelation=xmlattrs["sqlRelation"];
		var  termRelation=xmlattrs["termRelation"];
		var  field=xmlattrs["field"];
		var  value=xmlattrs["value"];
		if(type=="defualt"){
			//TODO　这里的KEY　权限模块实现后需要修改
			if(value!= undefined && value!=null &&value!=""){
				sql+=" " + sqlRelation;
				sql+=" " + field + " " + termRelation + " '" + value + "' " ;
			}
			return sql;
		}else if(type=="sql"){
			//TODO　这里的KEY　权限模块实现后需要修改
			if(value!= undefined && value!=null &&value!=""){
				sql+=" " + value + " ";
			}
			return sql;
		}else{
		  alert("XML配置filter 中 没改type 处理方法");
		}
}




	
	
	
	
//EXCEL通用部分 	
	
//EXCEL 下载
function Z_excelTaskFileDownload_do(dateValue,param){
    if(dateValue==""||dateValue==null||dateValue==undefined){
      // zinglabs.alert("请选择日期！");
      alert("请选择日期！");
       return ;
    }
	var fileName=dateValue + ".xls";
	var winName=param["winName"];
	var path=param['downloadPath'];
	path=GOLBAL_PARAM[path]||"/ZDesk/reportExcelExpFolder";
	path=path+"/"+winName+"/"+fileName
	window.open(path);
}
//excel 下载
function Z_excelTaskFileDownload(param){
    //这里可以把param赋给某个全局变量去做
    param=param.data;  
    if (Z_IsEmpty(param) || param == null || Z_IsEmpty(param['winName'])
			|| param['winName'] == null || param['downloadPath'] == null ) {
		//param 空时先提示
		zinglabs.alert("缺少下载excel参数");
		//param = Z_LoadArguments(param,'winName');
		return ;
		
	}
	//这里ID先写死  我会在每个页面生成一个DIV
	$('#downloadExcelWindow').modal({
		        backdrop: 'static',
		        show:true,
		        keyboard: false
	});
	
}

/**Exlce 导出
   param 导出excel 的性
   页面  xml 转成json 的属性
**/
function Z_ServerExcel(pdata){
     //事件注册获取参数
     var p =pdata.data;
     var excelParams=p[0],attr2Json=p[1],filters=p[2];
     //这里验证只针对生成页面
	 var table= $("#"+excelParams.dtid).DataTable();
	if(table.data().length == 0){
			 zinglabs.alert("没有数据,不能进行excel导出操作！");		
			return;
	 }
 
	var where=sepllGenPageWherebyForm("#"+excelParams.id);	  
	           var pp={};
	            var filters=getFilterSql(filters);
			    pp.sql=excelParams.sql+where+filters;
			    pp.fileName=excelParams.fileName+Date.parse(new Date())+".xls";
			    pp.fTitle=excelParams.fTitle;
			    pp.attrNames=excelParams.attrNames;
			    pp.dbid=excelParams.dbid;
			   // params.sql="";
			    if(excelParams.sql==""||excelParams.sql==undefined||excelParams.sql==null){
			      //selStr 每个页面生成的查询sql
			      pp.sql=selStr+where+filters;
			    }
			    //统计 生成位置先写死
			    pp.statistical=$("#tongji").html();
			    //TODO  EXCEL导出方法修改
			    z_exportExcel(pp,function(data){
			          try{
			              if(data&&data.data){

			                }
			          }catch(ex){
			             logErr("查询失败 failed "+ex.name + ": " + ex.message);
			          }    
			    });
}



/***
 * 此函数支持单表和多表的条件拼接如果为多表则表示表名.字段名称
  拼装生成页面where 查询条件
  如果参数fullName不为空则拼接条件时为fullName.字段
  params
**/


function sepllGenPageWherebyForm($id,fullName) {
	var swhere = "";
	// var params=window.zkm_getHtmlFormJsons("baobiaoceshiSearchForm");
	$($id + " :input").each(function(index, obj) {
		var type = obj.type;
		var tag = obj.tagName.toLowerCase();

		var value = "";
		var needProc = false;
		var needSpell = true;

		if (type == 'text' || type == 'password' || tag == 'textarea') {
			value = $(obj).val();
			needProc = true;
		} else if (type == 'checkbox' || type == 'radio') {
			if ($(obj).attr('checked') == true) {
				value == "true";
			} else {
				value == "false";
			}
			needProc = true;
		} else if (tag == 'select') {
			// 这里是因为报表页面 是个时间粒度 select 项 遇到这项跳过
			if ($(obj).attr("id") == "timeGranularity") {
				needProc = false;
			} else {
				value = $(obj).val();
				needProc = true;
			}
		}

		if (needProc && !BIsNullVal(value)) {

			swhere += " and ";

			var nameTmp = $(obj).attr('name');
		 
		
			var compareTmp = " = ";
			if (nameTmp.indexOf("timebegin") != -1) {
	 
				nameTmp = nameTmp.substring(0, nameTmp.indexOf('timebegin'));
				compareTmp = " >= ";

			} else if (nameTmp.indexOf("timeend") != -1) {
				nameTmp = nameTmp.substring(0, nameTmp.indexOf('timeend'));
				compareTmp = " <= ";
			} else if (nameTmp.indexOf("multicombo") != -1) {
				nameTmp = nameTmp.substring(0, nameTmp.indexOf('multicombo'));
				var valueArr = value.split(",");
				var mstr = "(";
				for (var i = 0; i < valueArr.length; i++) {
						if(fullName!=undefined && fullName!='' && fullName!=null ){
						 		mstr += '`' + fullName+'`'+"."+'`'+nameTmp + '`' + compareTmp + " '"
							+ valueArr[i] + "' " + " or ";
						}else{
							mstr += '`' + nameTmp + '`' + compareTmp + " '"
							+ valueArr[i] + "' " + " or ";
						}
				
				}
				mstr = mstr.substring(0, mstr.lastIndexOf('or'));
				mstr += ")";
				swhere += mstr;
				needSpell = false;
			}
			if (needSpell) {
					if(fullName!=undefined && fullName!='' && fullName!=null ){
			
				swhere += fullName+"."+nameTmp + compareTmp;
			}else{
				swhere += nameTmp + compareTmp;
			}

				// 目前未支持 数字类型比较大小 所以暂时不需要判断是否加双引号。
				swhere += " '";
				// 特殊字符转义比如单引号和双引号
                value= escapeJavaScriptStyleString(value);
				swhere += value;
				swhere += "' ";
			}

		}
	});

	return swhere;
}



/***
 *
 * @desc 指定panel赋值 和清空
 * @todo 只测试了主要类型 , checkbox、radio没测试
 *
 * pVars.id panel id
 * pVars.data 赋值数据
 * pVars.isClear 字符串 "true" 清空此panel
 *
 *
 * @TODO
 * 通用，调试完成后分文件，不要加Velocity代码
 *
 */
function setPanelVal(pVars){
    $('#'+pVars.id+' :input').each(function(index,obj) {
        var type = obj.type;
        var tag = obj.tagName.toLowerCase();
        if(("isClear" in pVars) && pVars.isClear=='true'){
            if (type == 'text' || type == 'password' || type=='hidden' || tag == 'textarea')
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
            if (type == 'text' || type == 'password' || type=='hidden'  || tag == 'textarea')
                $(obj).val(vTmp);
            else if (type == 'checkbox' || type == 'radio'){
                $(obj).attr('checked',vTmp==''?false:vTmp);
//                this.checked = vTmp==''?false:vTmp;
            }
            else if (tag == 'select'){
            	$(obj).val(vTmp);
               // $(obj).attr('selectedIndex',vTmp==''?-1:vTmp);
            }
        }
    });
}


//创建行
/**
  
 这里先去出先要改变颜色的字段跟规则
 
 遍历规则  复核条件的 改变 颜色
 
 rulse:[{rule:[{target:"",gt:"",lt;""}],type:'',color:''},
 {rule:[{target:"",gt:"",lt;""}],type:'',color:''}]
**/
function DT_createRow(attrs,rows,dt_row){
   var rules=[];
   //规则的使用
    for(attr in attrs){
       var attrbute=attrs[attr];
       //个字段规则 这效率不高以后修改
       if(attrbute["color"]!=null&&attrbute["color"]!=""&&typeof(attrbute)=="object"){
           var index=attrbute["index"];
           var rulse=attrbute["color"];
           if(Z_IsEmptyValue(rulse)){
              continue;
           }
           rulse=rulse["rules"]||"";
           for(var i=0;i<rulse.length;i++){
               var rule=rulse[i];
               var bool=vRules(rule["rule"],rows,attr);
               if(bool){
                    //type 单元格变色 还是整行变色
                    var type=rule["type"];
                    //颜色
                    var cc=rule["color"];
                    if(type=="row"){
                            $(dt_row).css("background",cc);
                     }else if(type=="col"){
                            var indexNum=parseInt(index);
                            $('td', dt_row).eq(indexNum).css("background",cc);
                     }
               }
           }     
       }
     }
}
/**
  验证规则 
  ruleObj 验证规则
  data    验证数据
  attr    备用字段 target ='' 则用attr
  {rule:[{target:"",eq:"",gt:""},{target:"",eq:"",gt:''}]}
**/
function vRules(ruleObj,data,attr){
      var bool=true;
      if(Z_IsEmptyValue(ruleObj)){
        return false;
      }
      for(var i=0;i<ruleObj.length;i++){
         var rule=ruleObj[i];
         var attrName=rule.target;
         if(attrName==''){
           attrName=attr;
         }
         var value=data[attrName];
         if(value==undefined){
           return false;
         }
         //小于的值
         var lt=rule["lt"];
         //大于的值
         var gt=rule["gt"];
         //等于的值
         var eq=rule["eq"];
         //不等于的值
         var ne=rule["ne"];
         //小于等于的值
         var le=rule["le"];
         //大于等于
         var ge=rule["ge"];
         
         if(eq!=undefined){
            if(value==eq){
              continue;
            }else{
              return false;
            } 
         }else{
             if(!Z_IsEmptyValue(lt)){
                 if(parseInt(value)>=parseInt(lt))
                   return false;
             }
             if(!Z_IsEmptyValue(gt)){
                 if(parseInt(value)<=parseInt(gt))
                   return false;
             }
             if(!Z_IsEmptyValue(ne)){
                 if(value===ne)
                   return false;
             } 
             if(!Z_IsEmptyValue(le)){
                 if(parseInt(value)>parseInt(le))
                   return false;
             } 
             if(!Z_IsEmptyValue(ge)){
                 if(parseInt(value)<parseInt(ge))
                   return false;
             }  
             
         }        
      }
    return bool;
}


/**
 *
 * selector:被填充combo的选择表达式
 * comboType：下列列表载入标识  字典索引
 * comboParam：联动标识         联动索引
 *encode：是否为后端加密true为后端加密否则为传输时加密
 * @desc填充指定的combo
 * 
 * */

 function fillDictionaryData(selector,comboType ,stype,attrName,encode){
       var dictData="";
	    if(comboType.indexOf('@')==0){
	       var len=comboType.indexOf('_');
	       if(len>0){
	          var type=comboType.substring(1,len);
	          //参数
	          var cs=comboType.substring((len+1),comboType.length)
	          if(type=="sql"){
	             var ps=eval(cs);
	             var params={};
	                params["action"]="LoadWinDataJson";
				    params["dbId"]=ps.dbid;
				    params["tableName"]="";
				    params["encode"]="true";
				   if(encode!=undefined && encode=="true"){
				params["sql"] =  ps.sql;
				}else{
				params["sql"] = Z_EU(ps.sql) ;
				}
				    
				    $.ajax({
						        async :false,
						        type : "post",
						        url : "/ZDesk/ZDesk/ZDesk_Proc/DBProc.action",
						        dataType : "json",
						        data : params,
						        success : function(data) {
						               if(data&&data.retcode==0){
			                              dictData=data.Items; 
			                            }else{
			                              //记录日志
			                            }  
						        },
						        error : function(r){
						            alert("error");
						        }
				    });
 
	             
	          }else if(type=="cascade" && cs!="" && cs !=undefined  ){
	              dictData=getDicListfoComboNameByComboDic(cs)||{};
	          }
	       
	          else if(type=="fn"){
	               try{
	                    eval(cs(selector));  
	               }catch(e){
	                  alert("不存在"+cs+"方法！");
	               }
	               return ;
	          } 
	       }else{
	         //非正确规则格式
			 alert("非正确规则格式");
			 return ;
	       } 
	    }else{
	        var dictData= getDicListfoComboNameByComboDic(comboType)||{};
	    }
	    fillData(selector,dictData,stype,attrName);
	}
	
	/* 先把数据查询放在缓存中
 * 根据comboName 获取对应数据
 *  如果缓存里没有数据则重新加载数据
 * @param {} comboName @return {json}
 */
	function getDicListfoComboNameByComboDic(indexData) {
	var dictdata = '';
	var comData = '';
	if (indexData != null && indexData != undefined) {
		if (GOLBAL_PARAM["dictdata"].data == ''|| GOLBAL_PARAM["dictdata"].data == null|| GOLBAL_PARAM["dictdata"].data == undefined) {
			reloadDicData();
			dictdata = GOLBAL_PARAM["dictdata"].data;
		} else {
			dictdata = GOLBAL_PARAM["dictdata"].data;
		}
		comData = dictdata[indexData];
	} else {
		return '';
	}
	return comData;
}
//填充字典项方法
function fillData(selector,dictData,type,attrName){
   if(type=="select"){
        $(selector).empty();
		$(selector).append('<option value="">请选择</option>');
		$.each(dictData, function(index,element) {
		   $(selector).append('<option value="'+element.code+'">' + element.name + '</option>');			        
		}); 
   }else if(type=="radio"){
        $(selector).empty();
        $.each(dictData, function(index, element) {
	        $(selector).append(' <label class="checkbox-inline">' + '<input type="radio"   name="' + attrName + '"  value="' + element.code + '"> ' + element.name + ' </label> ');
	    });
   
   }else if(type=="checkbox"){
         $(selector).empty();
         $.each(dictData, function(index,element) {
			$(selector).append(' <label class="checkbox-inline">'+'<input type="checkbox"  id="'+attrName+'" name="'+attrName+'"  value="'+element.code+'"> '+element.name+' </label> ');
		 }); 
      
   }else if(type=="multicombo"){
     	$(selector).MultiCombo({data:dictData});
    } 
}
/**
 转义字符串特殊符号
**/	
function  escapeSql(str){
   str=str.replace(/^(\')|(\")$/g,"\"");
   return str;
}

//重置功能	
function doResetting(){
	zinglabs.confirm("确认要重置此项？",function(){
		var params={};
		$.blockUI(maskContent);
		params.loginName =getUserInfo().loginName ;
		var tableTmp=$('#kehuduanliuyanGrid').DataTable();
		var cell=tableTmp.rows(".active").data();
		var id="";
		for(var i=0;i<cell.length;i++){
			var key="id";
			id+=cell[0][key];
		}
		params["id"]=id;
		var url = "/" + window.top.PRJ_PATH + "/activitiManager/activityCommon/resettingMessage.action";
		ajaxFunction(url,params,true,function(data){
			if(data.success){
				showSuccess("重置成功!") ;
			}else{
			    showError("重置失败!");
			}
		});
	$.unblockUI();
	});
};

//新建工单

//特殊字符转 义
function escapeJavaScriptStyleString(str){
  var sz=str.length; 
  var out="";
   if (str == null || str.toString()==""){   return;   }
              for (var i = 0; i < sz; i++) {
               var ch = str.charAt(i);
              // handle unicode
                 if (ch > 0xfff) {
                    out+=(ch);
                } else if (ch > 0xff) {
                      out+=( ch);
              } else if (ch > 0x7f) {
                  out+( ch);
              } else if (ch < 32) {
                      switch (ch) {
                        case '\b' :
                            out+=('\\');
                              out+=('b');
                          break;
                       case '\n' :
                            out+=('\\');
                           out+=('n');
                            break;
                       case '\t' :
                            out+=('\\');
                            out+=('t');
                             break;
                        case '\f' :
                              out+=('\\');
                            out+=('f');
                            break;
                         case '\r' :
                          out+=('\\');
                             out+=('r');
                             break;
                        default :
                             if (ch > 0xf) {
                                 out+=( ch);
                             } else {
                                 out+=( ch);
                           }
                             break;
                    }
                 } else {
                    switch (ch) {
                         case '\'' :
                         if (true) {
                            out+=('\\');
                             }
                           out+=('\'');
                          break;
                         case '"' :
                           out+=('\\');
                            out+=('"');
                             break;
                         case '\\' :
                            out+=('\\');
                            out+=('\\');
                           break;
                       case '/' :
                            if (true) {
                                out+=('\\');
                            }
                        out+=('/');
                          break;
                         default :
                            out+=(ch);
                            break;
                    }
                 }
             }
             return out;
         }
