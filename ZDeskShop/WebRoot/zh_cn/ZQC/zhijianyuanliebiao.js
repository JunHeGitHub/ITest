
//查询文本作业数据
function searchData(){	
	var rdata=[];
	var pat={};	
	var loginName=[];
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    pat.nameSpaceId = 'zhiJianYuanLieBiao_search';
    pat.searchItems=searchItem2sql("searchUserDataForm");
    if(getSuSecurityUserRole_loginName().length>0||getSuSecurityUserRole_loginName()!= undefinde){
	    for(var i=0;i<getSuSecurityUserRole_loginName().length;i++){
	    	loginName+="'"+getSuSecurityUserRole_loginName()[i].loginName+"'"+",";
	    }
	var loginName1 = loginName.substring(0,loginName.length - 1);
    pat.loginName="("+loginName1+")";
    }else{
    	 pat.loginName="('')";
    }
    
    commonSelectForComplex(pat, false,function(data){
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
//查询description为质检员，复检员，质检组长的id 
function getSuSecurityRole_id(){
	var rdata=[];
	var pat={};	
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    pat.nameSpaceId = 'getSuSecurityRole_id';
    pat.searchItems="('质检组长','复检员','质检员')";
    commonSelectForComplex(pat, false,function(data){
	    if(data){
			rdata=data.rows;
		}
    }); 
    return rdata;
}

//根据description为质检员，复检员，质检组长的id 查询SuSecurityUserRole的loginName
function getSuSecurityUserRole_loginName(){
	var rdata=[];
	var role_id=[];
	var pat={};	
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    pat.nameSpaceId = 'getSuSecurityUserRole_loginName';
    if(getSuSecurityRole_id().length>0||getSuSecurityRole_id()!=undefinde){
    	for(var i=0;i<getSuSecurityRole_id().length;i++){
    	role_id+="'"+getSuSecurityRole_id()[i].id+"'"+",";
    }
    var role_id1 = role_id.substring(0,role_id.length - 1);
  	pat.searchItems="("+role_id1+")";
    }else{
    	pat.searchItems="('')";
    }
   	
    commonSelectForComplex(pat, false,function(data){
	    if(data){
			rdata=data.rows;
		}
    }); 
    return rdata;
}


//加载质检员信息数据
function initUserData(){
	var utable=$('#tableForUserData').DataTable();	
	//清空筛选
	utable.search("").draw();
	//清空数据
	utable.clear().draw();
	//重新加载数据
	var data=getUserByRoleName("系统管理员"); 
	utable.rows.add(data).draw(true);
}

function choseOK(){
	var table=$('#tableForUserData').DataTable();	
	var tt = new $.fn.dataTable.TableTools(table);
	var selectData = tt.fnGetSelectedData();	
	window.parent.ZQC_GOLBAL_PARAM["zhiJianYuanData"]=selectData;
}