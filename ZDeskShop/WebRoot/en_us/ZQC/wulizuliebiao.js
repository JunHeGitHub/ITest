
//查询文本作业数据
function searchData(){	
			var rdata
			var pat={};				   
			pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
	    	pat.nameSpaceId = 'getwulizu_search';
	    	pat.business_type='%'+$('#business_type').val()+'%';
	    	commonSelectForComplex(pat, false,
		    function(data){
		    //alert(JSON.stringify(data));
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
	var data=doSearch_wulizu(); 
	utable.rows.add(data).draw(true);
}

function choseOK(){
	var table=$('#tableForUserData').DataTable();	
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();	
	window.parent.ZQC_GOLBAL_PARAM["wulizuData"]=selectData;
	
}