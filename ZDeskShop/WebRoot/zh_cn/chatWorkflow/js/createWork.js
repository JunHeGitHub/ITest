

function Z_IsEmpty(value,allowBlank) {
    return typeof(value)=="undefined" || value === null || value === undefined || ((Z_IsArray(value) && !value.length))  || (!allowBlank ? value === '' : false) ||value=="undefined" ;
};

function Z_IsArray(o) {
    return o && (o instanceof Array) || o.constructor === Array;
};


//获取系统时间  精确到秒
function getTime(){
  var d=new Date(),str="";
  str +=d.getFullYear()+"-"; //获取当前年份 
 
 if((""+d.getMonth()).length==2)
 str +=(d.getMonth()+1)+"-";
 else
 str +="0"+(d.getMonth()+1+"-");

  if((""+d.getDate()).length==2)
 str +=d.getDate()+" ";
 else
 str +="0"+d.getDate()+" ";
 
  if((""+d.getHours()).length==2)
 str +=d.getHours()+":";
 else
 str +="0"+d.getHours()+":";
 
 if((""+d.getMinutes()).length==2)
 str +=d.getMinutes()+":";
 else
 str +="0"+d.getMinutes()+":";
 
 if((""+d.getSeconds()).length==2)
 str +=d.getSeconds();
 else
 str +="0"+d.getSeconds();
 return str;
}

//选完分类、模块名称赋值
function fillCombo(comboId){
//searchCategory();
openCategoryDesc();
//openCategoryNameByCategoryId();
     if(!Z_IsEmpty(comboIdMap[comboId])){
         var comboObj=$("#"+comboId);
         if(!Z_IsEmpty(comboObj)){
             comboObj.append(comboIdMap[comboId]);
             //comboObj.selectmenu("refresh");
         }
     }
};
var comboIdMap={};
//显示分类的下拉列表
function searchCategory(){
$("#categoryDesc").hide();
$("#categoryDesc").val('');
	var rdata=[];
	var pat={};
	var columnValues={};
	pat.tableName="workflow_category";
	pat.columnValues=columnValues;	
    pat.columnValues.isDelete="已启用";
    pat.equal="isDelete";
	commonSelect(pat,true,function(data){
	comboIdMap["focusArea"]="<option value=\"0\">选择分类</option>";
		if(data&&data.data){	
			rdata=data.data;
			for(var i=0;i<rdata.length;i++){
				comboIdMap["focusArea"]+="<option value=\""+rdata[i].id+"\">"+rdata[i].categoryName+"</option>";
			}
		}
		$("#focusArea").find("option").remove();
		var comboObj=$("#"+"focusArea");
		comboObj.append(comboIdMap["focusArea"]);
       // comboObj.selectmenu("refresh");
	});
	return rdata;
}


//ajax请求时调的函数
function showAlert(){
    var maskbox=document.createElement('div');
    var maskbox_con=document.createElement('div');
    var imgs=document.createElement('img');
    maskbox.id='maskbox';
    maskbox_con.className='maskbox_con';
    imgs.src='/images/loadings.gif';
    maskbox_con.appendChild(imgs);
    maskbox.appendChild(maskbox_con);
    document.body.appendChild(maskbox);
}
//ajax请求完成时调的函数
function showAlertHide(){
    var maskbox=document.getElementById('maskbox') ;
    document.body.removeChild(maskbox);
}
