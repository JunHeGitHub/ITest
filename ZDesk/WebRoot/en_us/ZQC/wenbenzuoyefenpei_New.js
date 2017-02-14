
function doSeach(){
    var params={}
    var rdata=[];
    params.tableName="SU_QC_SOURCE_RECORD_DATA";
    params.rows=10;
    params.offset=0;
    commonSelect(params,false,function(data){
                if(data&&data.data){
                	rdata=data.data;
                }
    });
	return rdata;
}		


function searchData() {
	var params={};
	params.columnValues=zkm_getHtmlFormJsons("searchDataForm");
    params.tableName="SU_QC_SOURCE_RECORD_DATA";
    params.moreThan={"begin_time_DFTB":"begin_time"};
    params.lessThan={"begin_time_DFTE":"begin_time"};
    params.rows=10;
    params.offset=0;
    commonSelect(params,false,function(data){
                if(data&&data.data){
                	var table = $("#tableForData").DataTable();	
					//清空筛选
					table.search("").draw();
				    //清空数据
					table.clear().draw();
				    //重新加载数据
					var rdata=data.data;
					table.rows.add(rdata).draw(true);
                }
    });
}


//
/**
   拼装参数
   需定义好规则 如时间段格式 之后通用此函数
**/
function commonFrom2Json(id){
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
   $('#'+id+' :input').each(function(index,obj) {
        var type = obj.type;
        var tag = obj.tagName.toLowerCase();
        var $obj=$(obj);
        if (type == 'text' || type == 'password' || tag == 'textarea'||tag == 'select'){
            var sType=$obj.attr('data-sType');
            if(sType=="equal"){
              equal[$obj.attr('name')]=$obj.attr('name');
            
            }else if(sType=="moreThan"){
               var  nameTmp=$obj.attr('name');
               nameTmp= nameTmp.substring(0,nameTmp.lastIndexOf('timebegin"'));
               moreThan[$obj.attr('name')]=nameTmp;
            }else if(sType=="lessThan"){
               var  nameTmp=$obj.attr('name');
               nameTmp= nameTmp.substring(0,nameTmp.lastIndexOf('timeend'));
               lessThan[$obj.attr('name')]=nameTmp;
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