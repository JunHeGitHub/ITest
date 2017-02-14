/**
 * 添加和修改表单的开始日期和结束日期比较
 * 
 */

jQuery.validator.addMethod("compareByDates", function(value, element, params) {
			var startDate = $('#' + (element.form.attributes[0].nodeValue)
					+ " input[name=" + params + ']').val();
			// var endDate = $('#'+(element.form.attributes[0].nodeValue)+"
			// #"+params[1]).val();
			return new Date(Date.parse(startDate.replace("-", "/"))) <= new Date(Date
					.parse(value.replace("-", "/")));
		}, "结束日期必须大于开始日期!");
/*******************************************************************************
 * 验证日期格式 2012-01-01 hh:mm:ss
 */
jQuery.validator.addMethod("validateByDates", function(value, element, params) {
	var dateParam = /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1])) (?:(?:[0-2][0-3])|(?:[0-1][0-9])):[0-5][0-9]:[0-5][0-9]$/;
	return this.optional(element) || dateParam.test(value);
}, "日期格式有误,格式为2012-01-01 hh:mm:ss");
/*******************************************************************************
 * 验证Eamil格式
 */
jQuery.validator.addMethod("validateByEamil", function(value, element, params) {
	var emailParam = /^[_a-z\d\-\./]+@[_a-z\d\-]+(\.[_a-z\d\-]+)*(\.(info|biz|com|edu|gov|net|am|bz|cn|cx|hk|jp|tw|vc|vn))$/;
	return this.optional(element) || emailParam.test(value);
}, "邮箱格式有误！");
/*******************************************************************************
 * 查询表单的时间范围查询验证 不包含天数的限制
 ******************************************************************************/
jQuery.validator.addMethod("compareByDateSection", function(value, element,
		params) {
	var startDate = $('#' + (element.form.attributes[0].nodeValue)
			+ " input[name=" + params + 'timebegin]').val();
				var endDate = $('#' + (element.form.attributes[0].nodeValue)
			+ " input[name=" + params + 'timeend]').val();
	return new Date(Date.parse(startDate.replace("-", "/"))) <= new Date(Date
			.parse(endDate.replace("-", "/")));
}, "起始日期不能大于截止时期!");



/*******************************************************************************
 *查询表单的 日期比较和查询天数的限制
 ******************************************************************************/

jQuery.validator.addMethod("compareBySumDate",function(value,element,params){
    var aDate;
    var oDate1;
    var  oDate2;
    var  iDays;  
        try{
          
            var name=params.name;
      var  day=params.day;
            var begindata = $('#' + (element.form.attributes[0].nodeValue)
			+ " input[name=" + name + 'timebegin]').val();
            
	       begindata = begindata.replace(' ', "-");
	       begindata=begindata.replace(/:/g, "-");
	       var enddata=$('#' + (element.form.attributes[0].nodeValue)
			+ " input[name=" + name + 'timeend]').val();
	       enddata=enddata.replace(' ', "-");
	       enddata=enddata.replace(/:/g, "-");
	       aDate = begindata.split("-");   
	       oDate1 = new Date(aDate[0],aDate[1]-1,aDate[2],aDate[3],aDate[4],aDate[5]);   
	       aDate = enddata.split("-");   
	       oDate2 =new Date(aDate[0],aDate[1]-1,aDate[2],aDate[3],aDate[4],aDate[5]);    
	       iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 /24);    
  
   if(oDate1>oDate2){
	         alert("起始日期不能大于截止时期");
	       return false;
	       }   
          if(iDays<=day){
	       return  true;
	       } 
	  alert("只能统计"+day+"天内的");
        }catch(e){
           logErr("Exception BRegisterExtEvent need see log "+e.name + ": " + e.message);
        }
           return false;
	     
},""); 

/*******************************************************************************
 * 验证字符串中不带 ','
 */
jQuery.validator.addMethod("validateByStringNotHaveComma", function(value, element, param) {
	var  rep = /^[^,]*$/;
	return rep.test(value);   
}, $.validator.format("请确保输入的值没有逗号(',')"));

/*******************************************************************************
 * 验证字符串中是否存在
 * 使用时传一个数组[字段中文名,表名,字段英文名,类型(add或edit),formId(表单id,可选),formInputId(不加后缀的表单输入框id,可选)]
 */
jQuery.validator.addMethod("validateStringIsExist", function(value,element,param) {
	var flag = false;
	if(value != null && value != ""){
		var tableName = param[1];
		var key = param[2];
		var column = $.parseJSON('{"'+key+'":"'+value+'"}');
		
		var params = {};
			params.tableName=tableName;
			params.equal=key;
			params.columnValues=column;
		var type = param[3];
		commonSelect(params,false,function(data){
			if(type=="add"){
				if(data.total>0){
					flag = false;
				}else{
					flag = true;
				}
			}else if(type=="edit"){
				if(data.total==0){
					flag = true;
				}else{
					if(param.length==6){
						var formId = param[4];
						var formInputId = param[5];
						var v1 = $("#"+formId+" #"+formInputId+"_edit").val();
						var v2 = data.data[0][formInputId];
						if(v1 == v2){
							flag = true;
						}else{
							flag = false;
						}
					}
				}
			}
		});
	}
	return flag;   
}, $.validator.format("{0},已存在"));
//验证特殊字符
jQuery.validator.addMethod("validateInputType",function(value,element,param){
	     //只含有汉字、数字、字母、下划线不能以下划线开头 
		var regs=/^(?!_)[a-zA-Z0-9_\u4e00-\u9fa5]+$/;
		if(  regs.test(value)){
			return true;
		}else{
			return false;
		}
	
},"包含特殊字符");







