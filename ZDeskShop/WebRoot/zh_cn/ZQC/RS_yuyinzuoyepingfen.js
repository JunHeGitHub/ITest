$(document).ready(function(){
	var file_name=getQueryString("file_name");
	var folder=file_name.substring((file_name.lastIndexOf("_")+1),(file_name.lastIndexOf("_")+9));
	var file_url="9.253.16.10/record_data/"+folder+"/"+file_name;
	player2mp3("player2mp3",file_url);
	//player2mp3("player2mp3","/"+window.top.PRJ_PATH+"/zh_cn/ZQC/hongshanguo.mp3");
});
 
//根据grade_id获取各项复检评分信息
function FujianScore(){
	 	var grade_id=getGrade_id();
		var rdata;   
	   	var pat =  {};
		pat.primaryKey = "id";
		pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
		pat.nameSpaceId="T_GRADE_DETAIL_FUJIAN_NEW_score";
		pat.grade_id=grade_id;
//		pat.where = appendSearchItem('searchItems');
		commonSelectForComplex(pat,false,function(data){		
		if(data.rows!= null){
		  rdata=data.rows;				 	
		}
	});
	return rdata;
}

//获取grade_id函数
function getGrade_id(){
	var grade_id=getQueryString("file_name").substring(0,getQueryString("file_name").lastIndexOf("."));;
	return grade_id;
}

//根据grade_id获取各项复检评分信息
function lookScore(){
		var grade_id=getGrade_id();
	 	var grade_id=grade_id;
		var rdata;
	   	var pat =  {};
		pat.primaryKey = "id";
		pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
		pat.nameSpaceId="T_GRADE_DETAIL_NEW_score";
		pat.grade_id=grade_id;
//		pat.where = appendSearchItem('searchItems');
		commonSelectForComplex(pat,false,function(data){			
		if(data.rows!= null){				
		  rdata=data.rows;				 	
		}			
	});
	return rdata;
}

//根据grade_id获取各项复审评分信息
function FushenScore(){
		var grade_id=getGrade_id()
	 	var grade_id=grade_id;
		var rdata;
	   	var pat = {};
		pat.primaryKey = "id";
		pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
		pat.nameSpaceId="T_GRADE_DETAIL_FUSHEN_NEW_score";
		pat.grade_id=grade_id;
//		pat.where = appendSearchItem('searchItems');
		commonSelectForComplex(pat,false,function(data){			
		if(data.rows!= null){				
		  rdata=data.rows;				 	
		}
	});
	return rdata;
	}




//根据grade_name查询评分指标
function doSearchZhibiaoByDescription(grade_name){
		var pat={};				   
		pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
	  	pat.nameSpaceId = 'getZhibiaoByDescription';
	  	pat.grade_name=description;
	  	commonSelectForComplex(pat, false,
	  	function(data) {
	   	rdata=data.rows;
	  });
	  return rdata;	
}


//根据grade_id查询dicinfo表
function doSearchZhibiaoByGrade_id(grade_id){
	var pat={};				   
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
   	pat.nameSpaceId = 'getZhibiaoByGrade_id';
   	pat.grade_index=grade_id;
   	commonSelectForComplex(pat, false,
   	function(data) {
    	rdata=data.rows;
   });
   return rdata;	
}
	
	
//根据grade_id查询上诉表数据
function doSearchShangSuYuanYinByGrade_id(grade_id){
	var pat={};				   
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
   	pat.nameSpaceId = 'getShangSuYuanYinByGrade_id';
   	pat.grade_id=grade_id;
   	commonSelectForComplex(pat, false,
   	function(data) {
    	rdata=data.rows;
   });
   return rdata;	
}

//根据grade_name查询SA_QC_GRADE_DICTINFO表
function doSearchIdByDescription(grade_name){
	var pat={};				   
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
  	pat.nameSpaceId = 'getIdByDescription';
  	pat.grade_name=grade_name;
  	commonSelectForComplex(pat, false,
  	function(data) {
   	rdata=data.rows;
  });
  return rdata;	
}
	
		

//根据文件名获取数据
function getId(){
	var rdata=[];
	var pat={};
	pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
	pat.nameSpaceId = 'getSU_QC_SOURCE_RECORD_DATA_id';
    pat.file_name=getQueryString("file_name");
    commonSelectForComplex(pat, false,function(data){
	    if(data){
			rdata = data.rows;	
		}
    }); 
    return rdata;
}

			
//计算总分			
function jisuanzongfen(){
	var zongshu=0;
	var is_involve=0;
	var selectCount=$("#pingfenzhiForm select[name]");
	for(var i=0;i<selectCount.length;i++){
		if(i<=9){
			if(!isNaN(parseFloat($("#pfx_00"+i).val()))){
				is_involve+=parseFloat($("#is_involve"+i).html());///权重
				zongshu=zongshu+parseFloat($("#pfx_00"+i).val());//分值
			}
		}
		if(i>9){
			if(!isNaN(parseFloat($("#pfx_0"+i).val()))){
				is_involve+=parseFloat($("#is_involve"+i).html());//权重
				zongshu=zongshu+parseFloat($("#pfx_0"+i).val());//分值
			}
		}
	}
	
  var sum=zongshu*100/is_involve+"";
  var sumScrore=sum.substring(0,sum.lastIndexOf(".")+3);
  if(is_involve==0&&zongshu==0){
		sumScrore=100;
	}
  $("#scoreSum").html("总分： "+sumScrore+"分");
  return sumScrore;
  //$("#jisuanzongfen").val(zongshu);
}




//数组获取最大值
function getMaxValue(option){
	var max=parseInt(option[0]);
 	for(var k=0;k<option.length;k++){
 		var num=parseInt(option[k]);
 		if(num>max){
 			max=num;
 		}
 	}
 	return max;
}

//加载时 是否已评语样式提示selectChuJianRemark
var setRemar
function loadPingYuCss(parameter){
	var grade_name=getQueryString("grade_name");//评分指标名
	var grade_index_id=doSearchZhibiaoByDescription(grade_name);
	var selectCount=$("#pingyuForm input[id]");
	if(parameter==''){
	for(var i=0;i<selectCount.length;i++){
			var is_null=selectRemark(grade_index_id[i].ID);
			if(is_null.length>0){
				if(is_null[0].remark_describe!=''&&is_null[0].remark_describe!="null"){
					$("#shifouyipingyu"+i).html("已评语");
					var Remark={"Remark":is_null[0].remark_describe};
					getRemark(Remark,i);//加载时为评语赋值
				}else{
					$("#shifouyipingyu"+i).html("未评语");
				}
			}
		}
	}else{
		for(var i=0;i<selectCount.length;i++){
			var is_null=selectChuJianRemark(grade_index_id[i].ID,parameter);
			if(is_null.length>0){
				if(is_null[0].remark_describe!=''&&is_null[0].remark_describe!="null"){
					$("#shifouyipingyu"+i).html("已评语");
					var Remark={"Remark":is_null[0].remark_describe};
					getRemark(Remark,i);//加载时为评语赋值
				}else{
					$("#shifouyipingyu"+i).html("未评语");
				}
			}
		}
	}
}


//是否已评语样式提示
function pingYuCss(){
	var selectCount=$("#pingyuForm input[id]");
	for(var i=0;i<selectCount.length;i++){
		if(remark[i]!=undefined){
			if(remark[i].Remark!=''&&remark[i].Remark!=null&&remark[i].Remark!=undefined){
				$("#shifouyipingyu"+i).html("已评语");
			}else{
				$("#shifouyipingyu"+i).html("未评语");
			}
		}
	}
}


//评语按钮函数
var grade_index_id='';//grade_index_id   ID的接受变量
var pydialog;
function remarkButton(i,ID){
	var setRemark=''
	var getRemark="getRemark";
	var aa=remark;
	if(remark.length>i){
		if(remark[i]!=undefined){
			setRemark=remark[i].Remark;
		}
	}else{
		if(selectRemark(ID)!=null&&selectRemark(ID)!=""&&selectRemark(ID)!=undefined){
			setRemark=selectRemark(ID)[0].remark_describe;
		}else{
			if(selectChuJianRemark(ID,'chujian')!=null&&selectChuJianRemark(ID,'chujian')!=""&&selectChuJianRemark(ID,'chujian')!=undefined){
			setRemark=selectChuJianRemark(ID,'chujian')[0].remark_describe;
			}
		}
	}
	var params={
		title:"初检评语",
		url:"/"+window.top.PRJ_PATH+"/zh_cn/ZQC/remarkPage.html?closeFn="+getRemark+"&i="+i+"&setRemark="+setRemark,
		width:"400"
	};
	pydialog=zinglabs.dialog(params);
}




//查询初检评语表数据
function selectChuJianRemark(grade_index_id,parameter){
	var pat={};
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
  	pat.nameSpaceId = "selectPingYu";
  	pat.grade_id=getGrade_id();
  	if(parameter!=''){
		if(parameter=='chujian'){
			pat.tableName='chujian_remark';
		}
		if(parameter=='fujian'){
			pat.tableName='fujian_remark';
		}
		if(parameter=='fushen'){
			pat.tableName='fushen_remark';
		}
	}
  	if(grade_index_id!=''){
  		pat.grade_index_id=grade_index_id;
  	}
  	commonSelectForComplex(pat, false,
  	function(data) {
   	rdata=data.rows;
  });
  return rdata;	
}

//查询初检，复检，复审评语表数据
function selectRemark(grade_index_id){
	var pat={};
	var data_source=getQueryString("data_source");//判断是初检，复检，复审
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
  	pat.nameSpaceId = "selectPingYu";
  	pat.grade_id=getGrade_id();
  	if(data_source!=''){
		if(data_source=='chujian'){
			pat.tableName='chujian_remark';
		}
		if(data_source=='fujian'){
			pat.tableName='fujian_remark';
		}
		if(data_source=='fushen'){
			pat.tableName='fushen_remark';
		}
	}
  	if(grade_index_id!=''){
  		pat.grade_index_id=grade_index_id;
  	}
  	commonSelectForComplex(pat, false,
  	function(data) {
   	rdata=data.rows;
  });
  return rdata;	
}
//获取评语
var remark=new Array();
function getRemark(Remark,i){
	 remark[i]=Remark;
	 pingYuCss();
}




function  getPat(){
	var pat={};
	pat.id=getQueryString("id");
	pat.grade_id=getGrade_id();
	pat.first_owner=getUserInfo().loginName;
	pat.pfx_total=jisuanzongfen();
	pat.file_name=getQueryString("file_name");//文件名
	pat.file_location=getQueryString("file_location");
	pat.grade_remark=$("#grade_remark").val();
	pat.grade_remark_fj=$("#fujian_remark").val();//复检描述
	pat.reowner=getUserInfo().loginName;	//复检质监员
	var selectCount=$("#pingfenzhiForm select[name]");
	for(var i=0;i<selectCount.length;i++){
		if(i<=9){
			if($("#pfx_00"+i).val()=='bushejicixiang'){
				pat["pfx_00"+i]=-1;
			}else
				pat["pfx_00"+i]=parseInt($("#pfx_00"+i).val());
			}
		}
		if(i>9){
			if($("#pfx_0"+i).val()=='bushejicixiang'){
				pat["pfx_0"+i]=-1;
			}else{
				pat["pfx_0"+i]=parseInt($("#pfx_0"+i).val());
			}
	}
	var grade_name=getQueryString("grade_name");
	pat.use_grade_index=doSearchZhibiaoByDescription(grade_name)[0].grade_index;
	pat.grade_index_id=doSearchZhibiaoByDescription(grade_name);
	return pat;
}


//str控制删除初检，复检，复审表,然后把数据插入对应表。。
function delete_insert_Data(str){
	var pat={};
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
	if(str=="chujian"){
		 pat.nameSpaceId = 'delPingfen';//删除初检表数据
	}
	if(str=="fujian"){
		 pat.nameSpaceId = 'delPingfen_fj';//删除附加表数据
	}
	if(str=="fushen"){
	 	pat.nameSpaceId = 'delPingfen_fs';//删除复审表数据
	}
	pat.grade_id=getGrade_id();;
	//alert(JSON.stringify(newPat));
	commonCRUDForComplex(pat, true,
   	function(data) {
   		if(data){
   			if(str=="chujian"){
   				chujianbaocun();
   			}
   			if(str=="fujian"){
   				fujianbaocun();
   			}
   			if(str=="fushen"){
   				fushenbaocun();
   			}
   		}
   	}); 
}


//初检保存
function chujianbaocun(){
	//初检 insertPingfen
	var pat={};
	pat=getPat();
	pat.tableName = "T_GRADE_DETAIL_FUSHEN_NEW";
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    pat.nameSpaceId = 'insertPingfen';
	//alert(JSON.stringify(newPat));
	commonCRUDForComplex(pat, true,
   	function(data) {
   		update_SU_QC_SOURCE_RECORD_DATA(pat.id,'chujianyipingfen');//修改数据表
	});
}

//修改数据表(分数) str控制 初检发布，或者初检评分
function update_SU_QC_SOURCE_RECORD_DATA(id,str){
	var pat={};
	pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
	pat.primaryKey = "id";
	pat.primaryKeyValue = id;
	if(str=='chujianyipingfen'){
		pat.score_state=zinglabs.zqc.getState("chujianyipingfen");
	}
	if(str=='chujianyifabu'){
		pat.data_state=zinglabs.zqc.getState("chujianyifabu");
		pat.score_state=zinglabs.zqc.getState("chujianyipingfen");
	}
	if(str=='fujianpingfen'){
		pat.score_state=zinglabs.zqc.getState("fujianyipingfen");
		pat.data_state=zinglabs.zqc.getState("fujianyifenpei");
		pat.begin_time_retest =sys_date();
		pat.end_time_retest =sys_date();
	}
	if(str=='fushenpingfen'){
		if(jisuanzongfen()>getId()[0].pfx_total){
			pat.score_state=zinglabs.zqc.getState("zuoxishangsuchenggong");
			pat.data_state=zinglabs.zqc.getState("zuoxishangsuchenggong");
		}else{
			pat.score_state=zinglabs.zqc.getState("zuoxishangsushibai");
			pat.data_state=zinglabs.zqc.getState("zuoxishangsushibai");
		}
	}
	pat.pfx_total=jisuanzongfen();
	   commonUpdate(pat,function(data){
	   var json=eval('('+data+')');
		     if(json.success){	
		     	  saveRemark(str);
		      }else{
		       zinglabs.i18n.alert('system_saveFailed');
		       }
	    });  
}
//复检保存
function fujianbaocun(){
	var pat={};
	pat=getPat();
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    pat.nameSpaceId = 'insertPingfen_fujian';
	//alert(JSON.stringify(newPat));
	commonCRUDForComplex(pat,true,
   	function(data) {
   		update_SU_QC_SOURCE_RECORD_DATA(pat.id,'fujianpingfen');//修改数据表
	});
}


//复审保存
function fushenbaocun(){
	//复审 fs insertPingfen_fushen
	var pat={};
	pat=getPat();
	pat.grade_remark=$("#fushen_remark").val();
	pat.tableName = "T_GRADE_DETAIL_FUSHEN_NEW";
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    pat.nameSpaceId = 'insertPingfen_fushen';
	//alert(JSON.stringify(newPat));
	commonCRUDForComplex(pat, true,
   	function(data) {
   		Update_shangsu();//修改数据表
	});
}


//发布
function fabu(){
	//初检 insertPingfen
	var pat={};
	pat=getPat();
	pat.tableName = "T_GRADE_DETAIL_FUSHEN_NEW";
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    pat.nameSpaceId = 'insertPingfen';
	//alert(JSON.stringify(newPat));
	commonCRUDForComplex(pat, true,
   	function(data) {
   		update_SU_QC_SOURCE_RECORD_DATA(pat.id,'chujianyifabu');//修改数据表
	});
}


//修改上诉表状态，评语，复审时间
function Update_shangsu(){
		var file_name=getQueryString("file_name");
		var grade_id=file_name.substring(0,file_name.lastIndexOf("."));
		var rdata=[];
		var pat={};
		pat=getPat();
		pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
		pat.nameSpaceId = 'xiugaishangsubiao';
		pat.grade_id=grade_id;
	    pat.file_name=file_name;
	    pat.update_time=sys_date();
		pat.admin_remark=$("#fushen_remark").val();
		pat.score_state=getId()[0].score_state;
		if(jisuanzongfen()>getId()[0].pfx_total){
			pat.score_state=zinglabs.zqc.getState("zuoxishangsuchenggong");
		}else{
			pat.score_state=zinglabs.zqc.getState("zuoxishangsushibai");
		}
	   commonCRUDForComplex(pat, true,function(data){
	    if(data){
	    	update_SU_QC_SOURCE_RECORD_DATA(pat.id,'fushenpingfen');
		}
	   });
}


//查询上诉原因和复审描述
function doSearchShangSuYuanYinByGrade_id(){
	var pat={};
	pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
  	pat.nameSpaceId = 'getShangSuYuanYinByGrade_id';
  	pat.grade_id=getGrade_id();
  	commonSelectForComplex(pat,false,
  	function(data) {
   	rdata=data.rows;
  });
  return rdata;	
}





//保存初检，复检，复审评语表 str判断初检是否发布。
function saveRemark(str){
	var num=0;
	var pat={};
	var data_source=getQueryString("data_source");//判断是初检，复检，复审
	var selectCount=$("#pingyuForm input[id]");
	if((selectRemark('')==''||selectRemark('')==null||selectRemark('')==undefined)){
		for(var i=0;i<selectCount.length;i++){
			if(data_source!=''){
				if(data_source=='chujian'){
					pat.tableName='chujian_remark';
				}
				if(data_source=='fujian'){
					pat.tableName='fujian_remark';
				}
				if(data_source=='fushen'){
					pat.tableName='fushen_remark';
				}
			}
			pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
		  	pat.nameSpaceId = 'insertPingYu';
		  	pat.grade_id=getGrade_id();
		  	pat.grade_index_id=getPat().grade_index_id[i].ID;
		  	pat.DESCRIPTION=getPat().grade_index_id[i].DESCRIPTION;
		  	if(i<=9){
	  		var aa=$("#pfx_00"+i).val()
	  		if($("#pfx_00"+i).val()=='bushejicixiang'){
	  			pat.grade_score=-1;
	  		}else{
	  			pat.grade_score=parseFloat($("#pfx_00"+i).val());
	  		}
	  	}
	  	if(i>9){
	  		if($("#pfx_0"+i).val()=='bushejicixiang'){
	  			pat.grade_score=-1;
	  		}else{
	  			pat.grade_score=parseFloat($("#pfx_0"+i).val());
	  		}
	  	}
		  	if(remark[i]==undefined){
		  		pat.remark_describe="";
		  	}else{
		  		pat.remark_describe=remark[i].Remark;
		  	}
		  	commonCRUDForComplex(pat, true,
		  	function(data) {
		  	num+=data.rows;
		  	if(num==selectCount.length){
		  		var pwin = window.parent;
			    var closeFn=getQueryString("closeFn");
		  		if(str!='chujianyifabu'&&str!='fushenpingfen'){
			  		  zinglabs.i18n.alert('system_saveSuccess'); 
					  document.getElementById('btn_fabu').disabled = false;
					  pwin[closeFn]();
				  }
				  if(str=='chujianyifabu'){
				   zinglabs.i18n.alert2fn('ZQC_fabuchenggong',{},function(){
					  	//关闭当前弹出窗口
					  	pwin.pfdialog.close().remove();
					  	pwin[closeFn]();
					  });
		  			}
		  			if(str=='fushenpingfen'||str=='fujianpingfen'){
		  				zinglabs.i18n.alert2fn('system_saveSuccess',{},function(){
					  	//关闭当前弹出窗口
					  	pwin.pfdialog.close().remove();
					  	pwin[closeFn]();
					  });
		  			}
	  			}
		  });
	  }
  }else{
  	updateRemark(str);//修改初检，复检，复审评语表
  }
}

//修改初检，复检，复审评语表
function  updateRemark(str){
	var num=0;
	var pat={};
	var data_source=getQueryString("data_source");//判断是初检，复检，复审
	var selectCount=$("#pingyuForm input[id]");
	for(var i=0;i<selectCount.length;i++){
		if(data_source!=''){
			if(data_source=='chujian'){
				pat.tableName='chujian_remark';
			}
			if(data_source=='fujian'){
				pat.tableName='fujian_remark';
			}
			if(data_source=='fushen'){
				pat.tableName='fushen_remark';
			}
		}
		pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
	  	pat.nameSpaceId = 'updatePingYu';
	  	pat.grade_id=getGrade_id();
	  	pat.grade_index_id=getPat().grade_index_id[i].ID;
	  	pat.DESCRIPTION=getPat().grade_index_id[i].DESCRIPTION;
	  	if(i<=9){
	  		var aa=$("#pfx_00"+i).val()
	  		if($("#pfx_00"+i).val()=='bushejicixiang'){
	  			pat.grade_score=-1;
	  		}else{
	  			pat.grade_score=parseFloat($("#pfx_00"+i).val());
	  		}
	  	}
	  	if(i>9){
	  		if($("#pfx_0"+i).val()=='bushejicixiang'){
	  			pat.grade_score=-1;
	  		}else{
	  			pat.grade_score=parseFloat($("#pfx_0"+i).val());
	  		}
	  	}
	  	if(remark[i]==undefined){
	  		pat.remark_describe="";
	  	}else{
	  		pat.remark_describe=remark[i].Remark;
	  	}
	  	commonCRUDForComplex(pat, true,
	  	function(data) {
	  	num+=data.rows;
	  	if(num==selectCount.length){
	  		var pwin = window.parent;
			var closeFn=getQueryString("closeFn");
	  		if(str!='chujianyifabu'&&str!='fushenpingfen'){
		  		 zinglabs.i18n.alert('system_saveSuccess'); 
				  document.getElementById('btn_fabu').disabled = false;
				  pwin[closeFn]();
			  }
			  if(str=='chujianyifabu'){
			   zinglabs.i18n.alert2fn('ZQC_fabuchenggong',{},function(){
				  	//关闭当前弹出窗口
				  	window.parent.pfdialog.close().remove();
				  	pwin[closeFn]();
				  });
	  			}
	  		if(str=='fushenpingfen'||str=='fujianpingfen'){
		  				zinglabs.i18n.alert2fn('system_saveSuccess',{},function(){
					  	//关闭当前弹出窗口
					  	pwin.pfdialog.close().remove();
					  	pwin[closeFn]();
					  });
		  			}
  			}
	  });
  }
}