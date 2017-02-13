	
var aa={};
var sum=0;
$(document).ready(function(){
	sys_date();
	checkButton();
	loadDescription();
});

//判断上诉按钮隐藏显示
function checkButton(){
	var score_state=getQueryString("data_state");
	var checkBtn=getQueryString("checkBtn");
/*	if(score_state==zinglabs.zqc.getState("zuoxishangsuchenggong")||score_state==zinglabs.zqc.getState("zuoxishangsushibai")){
		$("#tuihui").hide();
	}else{
		$("#tuihui").show();
	}
*/	
	if(score_state==zinglabs.zqc.getState("zuoxishangsu")||score_state==zinglabs.zqc.getState("zuoxishangsuchenggong")||score_state==zinglabs.zqc.getState("zuoxishangsushibai")){
		$("#shangsuBtn").hide();
		document.getElementById('shangsuneirong').readOnly=true;
	}
	if(score_state==zinglabs.zqc.getState("zuoxishangsu")||checkBtn==1||score_state==zinglabs.zqc.getState("zuoxishangsuchenggong")||score_state==zinglabs.zqc.getState("zuoxishangsushibai")){
		document.getElementById('shangsuneirong').readOnly=true;
		$("#shangsuBtn").hide();
	}else{
		$("#shangsuBtn").show();
	}
	if(checkBtn=='jiaocai'){
		document.getElementById('shangsuneirong').readOnly=true;
		$("#shangsuBtn").hide();
		$("#chakanneirong").show();
	}
}	

	//获取grade_id函数
	function getGrade_id(){
		var grade_id;
		return grade_id=getQueryString("grade_id");
	}
		
	//根据grade_id获取各项初检评分信息
	function FujianScore(){
			var aa=getGrade_id();
			var grade_id=aa;
			
		 	var grade_id=grade_id;
			var rdata;
		   	var pat = zkm_getHtmlFormJsons('searchItems');
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
		//根据grade_id获取各项复检评分信息
	function lookScore(){
			
			var grade_id=getGrade_id();
			
		 	var grade_id=grade_id;
			var rdata;
		   	var pat = zkm_getHtmlFormJsons('searchItems');
			pat.tableName = "T_GRADE_DETAIL_FUSHEN_NEW";
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
		   	var pat = zkm_getHtmlFormJsons('searchItems');
			pat.tableName = "T_GRADE_DETAIL_FUSHEN_NEW";
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
	
		//根据grade_id获取各项复审评分信息
	function ShangSuScore(){
			var grade_id=getGrade_id()
		 	var grade_id=grade_id;
			var rdata;
		   	var pat = zkm_getHtmlFormJsons('searchItems');
			pat.tableName = "SA_AGENT_ARGUMENT";
			pat.primaryKey = "id";
			pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
			pat.nameSpaceId="SA_AGENT_ARGUMENT_select_text";
			pat.grade_id=grade_id;
	//		pat.where = appendSearchItem('searchItems');
			commonSelectForComplex(pat,false,function(data){			
			if(data.rows!= null){				
			  rdata=data.rows;				 	
			}			
		});
		return rdata;
	}
	


//查询各项分数
	function  loadDescription(){
		var data_state=getQueryString("data_state");
		var score_state=getQueryString("score_state");
		var chujian=lookScore();
		var fushen=FushenScore();
		var fujian=FujianScore();
		var shangsuyuanyin=ShangSuScore();
		var score;
		//(score_state=='座席上诉'){
		if(score_state==zinglabs.zqc.getState("zuoxishangsu")||data_state==zinglabs.zqc.getState("zuoxishangsu")||score_state==zinglabs.zqc.getState("zuoxishangsuchenggong")||score_state==zinglabs.zqc.getState("zuoxishangsushibai")){
			   if(shangsuyuanyin.length!=0){
				       $("#shangsuneirong").val(shangsuyuanyin[0].agent_user_remark);
				}
		}
		//(score_state=='上诉成功'||score_state=='上诉失败'){
		if(data_state==zinglabs.zqc.getState("zuoxishangsuchenggong")||data_state==zinglabs.zqc.getState("zuoxishangsushibai")){
			score=FushenScore();
		}
		//if(score_state=='初检已发布'||score_state=='初检已评分'||data_state=='初检已分配'){
		if(data_state==zinglabs.zqc.getState("chujianyifabu")||score_state==zinglabs.zqc.getState("chujianyifenpei")){
			score=lookScore();
		}
		//if(score_state=='复检已发布'||data_state=='复检已分配'||score_state=='复检已评分'){
		if(data_state==zinglabs.zqc.getState("fujianyifenpei")||score_state==zinglabs.zqc.getState("fujianyipingfen")){
			score=FujianScore();
		}
		if(score_state==zinglabs.zqc.getState("zuoxishangsu")||data_state==zinglabs.zqc.getState("zuoxishangsu")){
			score=lookScore();
		}
		if(FujianScore().length==0&&FushenScore().length==0){
			score=lookScore();
		}
		var num=0;
		if(score!=undefined){
			num=score.length;
		}
		if(num!=0){
			var grade_name=getQueryString("grade_name");
			if(grade_name==''||grade_name=="undefined"){
				 zinglabs.i18n.alert("gexiangpingfen_meiyouduiyingdepingfenzhibiao");
			}else{
			document.getElementById("grade_index_td").innerHTML="评分指标："+grade_name;
			var commonTreeParam=doSearchZhibiaoByDescription(grade_name);
			//控制每行四列的变量
			var row=0;
			//动态加载各项评分
			for(var i=0;i<commonTreeParam.length;i++){
								   if(commonTreeParam[i].PARENTID==0){
								   	
								   var arraymin = new Array(); //定义数组
								   var arraymax = new Array(); //定义数组
								   var array = new Array(); //定义数组
								   
								   $("#dongtaichuangjianTable").append("<table class='table table-bordered table-condensed' id='genjiedian_"+i+"'><caption>"+commonTreeParam[i].DESCRIPTION+"</caption></table>");
								   	 for(var j=0;j<commonTreeParam.length;j++){
								    if(commonTreeParam[j].PARENTID==commonTreeParam[i].ID){
								    array.push(commonTreeParam[j]);
								    }
								   }
								   $("#genjiedian_"+i+"").append("<tr  id='tr_"+i+"'></tr>");
								   $("#genjiedian_"+i+"").append("<tr  id='tr_tr_"+i+"'></tr>");
								   $("#genjiedian_"+i+"").append("<tr  id='tr_tr_tr_"+i+"'></tr>");
								   for(var k=0;k<array.length;k++){
								   if(k<4){
									   //各项评分
									   $("#tr_"+i+"").append("<td style='text-align:center;' >"+array[k].DESCRIPTION+"</td>");
									   //最大值最小值
									   arraymin.push(array[k].min_value);
									   arraymax.push(array[k].max_value);
									   $("#tr_tr_"+i+"").append("<td style='text-align:center;'>"+array[k].min_value+'~'+array[k].max_value+"</td>");
									   //分值
									   var t=array[k].value_remark;
						               aa[i+'_'+k]=t;
						               var q=i+"_"+k;
						               var p='pfx_00'+sum;
						               var aaaa=score[0][p];
									   $("#tr_tr_tr_"+i+"").append("<td ><input type='text' id='pfx_00"+sum+"' value='"+score[0][p]+"' class='input' name='pfx_00"+sum+"' style='padding: 0; margin: 0;  width: 100%; text-align:center;' readonly='readonly'  onchange=g("+array[k].min_value+","+array[k].max_value+",'"+p+"') onclick=f('"+q+"') onfocus=f('"+q+"')></td>");
								   
								   }
								   
								   if(k>=4){
								   		if(k%4==0){
								   			$("#genjiedian_"+i+"").append("<tr  id='tr_k"+k+"'></tr>");
									   		$("#genjiedian_"+i+"").append("<tr  id='tr_tr_k"+k+"'></tr>");
									   		$("#genjiedian_"+i+"").append("<tr  id='tr_tr_tr_k"+k+"'></tr>");
									   		row=k;
									   		
								   		}
								   		  //各项评分
									   $("#genjiedian_"+i+" #tr_k"+row+"").append("<td style='text-align:center;' >"+array[k].DESCRIPTION+"</td>");
									   //最大值最小值
									   arraymin.push(array[k].min_value);
									   arraymax.push(array[k].max_value);
									   $("#genjiedian_"+i+" #tr_tr_k"+row+"").append("<td style='text-align:center;'>"+array[k].min_value+'~'+array[k].max_value+"</td>");
									   //分值
									   var t=array[k].value_remark;
						               aa[i+'_'+k]=t;
						               var q=i+"_"+k;
						               var p='pfx_00'+sum;
									   $("#genjiedian_"+i+" #tr_tr_tr_k"+row+"").append("<td ><input type='text' id='pfx_00"+sum+"' value='"+score[0][p]+"' class='input' name='pfx_00"+sum+"' style='padding: 0; margin: 0;  width: 100%; text-align:center;' readonly='readonly'  onchange=g("+array[k].min_value+","+array[k].max_value+",'"+p+"') onclick=f('"+q+"') onfocus=f('"+q+"')></td>");
								   
								   }
								   sum=sum+1;
								   }
								   
								   
								   
								   
								   }
								}
				       // $("#grade_table_tr1").append("<td style='text-align: center;'>总分</td>");
				        //$("#grade_table_tr2").append("<td style='text-align: center;'>"+score[0].pfx_total+"</td>");
				        if(chujian.length!=0){
				       	 $("#grade_remark").val(chujian[0].grade_remark);
				        }
				        if(fushen.length!=0){
				        	$("#fushen_remark").val(fushen[0].grade_remark);
				        	$("#shangsuneirong").val(shangsuyuanyin[0].agent_user_remark);
				        }
				        if(fujian.length!=0){
				        	$("#fujian_remark").val(fujian[0].grade_remark);
				        }
				   }
			var commonTreeParam=doSearchZhibiaoByDescription(grade_name);
			for(var i=0;i<commonTreeParam.length;i++){
				var array = new Array();
				if(commonTreeParam[i].PARENTID==0){
				 for(var j=0;j<commonTreeParam.length;j++){
					    if(commonTreeParam[j].PARENTID==commonTreeParam[i].ID){
					    array.push(commonTreeParam[j]);
					    }
				 }
				
				}
				 $("#value_remark").val(array[0].value_remark);
				break;
			}
        }else{
        	window.close();
        	 zinglabs.i18n.alert("gexiangpingfen_meiyoupingfenshuju");
        }
       
	}					
		//上诉  更新SU_QC_SOURCE_RECORD_DATA  插入复审表数据
	function UpdateAndInsert(){
		
		var agent_user=getQueryString("agent_user");
		var grade_id=getGrade_id()
		var rdata;
		var agent_user_remark=$("#shangsuneirong").val();		
		var grade_remark=$("#grade_remark").val();
		var grade_remark_fj=$("#fujian_remark").val();	
	   	var pat = zkm_getHtmlFormJsons('searchItems');
		pat.tableName = "SA_AGENT_ARGUMENT";
		pat.primaryKey = "id";
		pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
		pat.nameSpaceId="SA_AGENT_ARGUMENT_insert";
		pat.grade_remark=grade_remark;						//初检评语
		pat.grade_remark_fj=grade_remark_fj;				//复检评语
		pat.score_state=zinglabs.zqc.getState("zuoxishangsu");							//状态
		pat.agent_user_dt=sys_date();								//上诉时间，配置文件传当前时间
		pat.agent_user_remark=agent_user_remark;			//上诉原因，（座席复审说明）
		pat.grade_id=grade_id;  							//grade_id
		pat.pfx_total=getQueryString("total");					//分数
		pat.owner=getQueryString("owner");						//质检员
		pat.update_time=getQueryString("generate_time_state_DFTB");	//评估开始时间
		pat.file_name=getQueryString("file_name");	//文件名
		pat.agent_user=agent_user;
		var chujian=lookScore();
		if(chujian.length!=0){
			pat.grade_remark=chujian[0].grade_remark;		
		}								
		if(select_shangsu(grade_id).length!=0){
			delete_shangsu(grade_id);
		}
		updateSU_QC_SOURCE_RECORD_DATA();//修改SU_QC_SOURCE_RECORD_DATA表状态
//		pat.where = appendSearchItem('searchItems');
		commonCRUDForComplex(pat,false,function(data){		//执行插入	
			var pwin = window.parent;
			var closeFn=getQueryString("closeFn");
			//window.parent.zuoxishangsu_close.close();
			
			zinglabs.i18n.alert2fn('zuoxishangsu_shangsuchenggong',{},function(){
										  	//关闭当前弹出窗口
										  	window.parent.zuoxishangsu_close.close();
										 	pwin[closeFn]();
										  });
			
		});
		}
	
	function updateSU_QC_SOURCE_RECORD_DATA(){
		var pat={};
		var id=getQueryString("id");
		var rdata;		
		pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
		pat.nameSpaceId="SU_QC_SOURCE_RECORD_DATA_update";
		pat.id=id;
		pat.data_state=zinglabs.zqc.getState("zuoxishangsu");
		pat.score_state=zinglabs.zqc.getState("zuoxishangsu");
//		pat.where = appendSearchItem('searchItems');
		commonCRUDForComplex(pat,false,function(data){			
		
		});
	}		
	
	//上诉按钮单击事件 上诉内容非空验证	
	function shangsuCheck(){
		var shangsuneirong=$("#shangsuneirong").val();
		if(shangsuneirong==''||shangsuneirong==null){
			zinglabs.i18n.alert("zuoxichaxun_shangsuyuanyin");
			return;
		}else{
			UpdateAndInsert();
		}
	}
	
	//删除上诉表数据
	function delete_shangsu(id){
		var pat={};
		var rdata;		
		pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
		pat.nameSpaceId="SA_AGENT_ARGUMENT_delete";
		pat.grade_id=id;
//		pat.where = appendSearchItem('searchItems');
		commonCRUDForComplex(pat,false,function(data){			
			rdata=data.rows;
		});
		return rdata;
	}	
	
	//根据grade_id查询上诉表数据
	function select_shangsu(id){
		var pat={};
		var rdata;		
		pat.nameSpace="com.zinglabs.apps.mybaits_xml_def.ZQC";
		pat.nameSpaceId="SA_AGENT_ARGUMENT_select_text";
		pat.grade_id=id;
//		pat.where = appendSearchItem('searchItems');
		commonSelectForComplex(pat,false,function(data){			
			 rdata=data.rows;	
		});
		return rdata;
	}
	
	//查看文本内详情内容
function chaKanWenBenNeiRong(){
		var file_location=getQueryString("file_location");
		var file_name=getQueryString("file_name");
		var folder=file_name.substring((file_name.lastIndexOf("_")+1),(file_name.lastIndexOf("_")+9));
		var file_url="/"+window.top.PRJ_PATH+"/ZIM/record_data/"+folder+"/"+file_name;
		var params={
			title:"文本内容",
			width:"700",
			height:'500',
			content: '<iframe src='+file_url+' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>'
		};
		zinglabs.dialog(params);
	
}