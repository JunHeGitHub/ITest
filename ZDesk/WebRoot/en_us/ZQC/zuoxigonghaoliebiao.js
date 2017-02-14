
//查询文本作业数据
function searchData(){	
			var rdata
			var pat={};				   
			pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
	    	pat.nameSpaceId = 'getzuoxigonghao_search';
	    	pat.alias1='%'+$('#alias1').val()+'%';
	    	commonSelectForComplex(pat, false,
		    function(data){
	        if(data){
			var table=$('#tableForUserData').DataTable();	
			//清空筛选
		    table.search("").draw();
		    //清空数据
		    table.clear().draw();
		    //重新加载数据
		    var rdata=data.rows;
		    table.rows.add(rdata).draw(true);	
		}
		    });	
}
//加载质检员信息数据
function initUserData(){
	var utable=$('#tableForUserData').DataTable();	
	//清空筛选
	utable.search("").draw();
	//清空数据
	utable.clear().draw();
	//重新加载数据
	var data_all;
	var pat={};				   
		pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	pat.nameSpaceId = 'getZuoxigonghao';
    	commonSelectForComplex(pat, false,
    	function(data) {
     	data_all=data.rows;
    	//var select1 = $(".alias1"); 
    });		
	utable.rows.add(data_all).draw(true);
}

function choseOK(){
	var table=$('#tableForUserData').DataTable();	
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();	
	window.parent.ZQC_GOLBAL_PARAM["zuoxigonghaoData"]=selectData;
	
}