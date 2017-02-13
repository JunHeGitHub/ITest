var data_source='';//判断是 初检 复检 复审
var aa={};
var sum=0;
$(document).ready(function(){
	checkButton();
	loadDescription();
	var file_name=getQueryString("file_name");
	var folder=file_name.substring((file_name.lastIndexOf("_")+1),(file_name.lastIndexOf("_")+9));
	var file_url="9.253.16.10/record_data/"+folder+"/"+file_name;
	player2mp3("player2mp3",file_url);
	//player2mp3("player2mp3","/"+window.top.PRJ_PATH+"/zh_cn/ZQC/hongshanguo.mp3");
});

//判断上诉按钮隐藏显示
function checkButton(){
	var score_state=getQueryString("score_state");
	var checkBtn=getQueryString("checkBtn");
/*	if(score_state==zinglabs.zqc.getState("zuoxishangsuchenggong")||score_state==zinglabs.zqc.getState("zuoxishangsushibai")){
		$("#tuihui").hide();
	}else{
		$("#tuihui").show();
	}
*/	
	if(checkBtn==1){
		$("#shangsuBtn").hide();
		document.getElementById('shangsuneirong').readOnly=true;
		$("#chakanneirong").hide();
	}
	if(score_state==zinglabs.zqc.getState("zuoxishangsu")||checkBtn==1||score_state==zinglabs.zqc.getState("zuoxishangsuchenggong")||score_state==zinglabs.zqc.getState("zuoxishangsushibai")){
		document.getElementById('shangsuneirong').readOnly=true;
		$("#shangsuBtn").hide();
		$("#chakanneirong").hide();
	}else{
		$("#shangsuBtn").show();
		$("#chakanneirong").hide();
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
		
	//根据grade_id获取各项复检评分信息
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
		//根据grade_id获取各项初检评分信息
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
		if(score_state==zinglabs.zqc.getState("zuoxishangsuchenggong")||score_state==zinglabs.zqc.getState("zuoxishangsushibai")){
			score=FushenScore();
			data_source='fushen';
		}
		//if(score_state=='初检已发布'||score_state=='初检已评分'||data_state=='初检已分配'){
		if(data_state==zinglabs.zqc.getState("chujianyifabu")||score_state==zinglabs.zqc.getState("chujianyifenpei")){
			score=lookScore();
			data_source='chujian';
		}
		//if(score_state=='复检已发布'||data_state=='复检已分配'||score_state=='复检已评分'){
		if(data_state==zinglabs.zqc.getState("fujianyifenpei")||score_state==zinglabs.zqc.getState("fujianyipingfen")){
			score=FujianScore();
			data_source='fujian';
		}
		if(score_state==zinglabs.zqc.getState("zuoxishangsu")||data_state==zinglabs.zqc.getState("zuoxishangsu")){
			score=lookScore();
			data_source='chujian';
		}
		if(FujianScore().length==0&&FushenScore().length==0){
			score=lookScore();
			data_source='chujian';
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
				var span_id='';
				 $("#pingfenxiang").append("<tr ><td style='height:30px'>"+commonTreeParam[i].DESCRIPTION+"&nbsp;&nbsp;&nbsp;权重:(<span id='is_involve"+i+"' name='is_involve"+i+"' >"+commonTreeParam[i].weight+"</span>%)</td></tr>");
		    	if(i<10){
		    		if(score[0]["pfx_00"+i]!=-1){
		    		 $("#pingfenzhi").append("<tr><td style='height:30px'><span id='score"+i+"' name='score"+i+"' >"+score[0]["pfx_00"+i]+"</span></td></tr>");
		    		}else{
		    		 $("#pingfenzhi").append("<tr><td style='height:30px'><span id='score"+i+"' name='score"+i+"' >不涉及此项</span></td></tr>");
		    		}
		    	}
		    	if(i>9){
		    		if(score[0]["pfx_00"+i]!=-1){
		    		 $("#pingfenzhi").append("<tr><td style='height:30px'><span id='score"+i+"' name='score"+i+"' >"+score[0]["pfx_0"+i]+"</span></td></tr>");
		    	}else{
		    		 $("#pingfenzhi").append("<tr><td style='height:30px'><span id='score"+i+"' name='score"+i+"' >不涉及此项</span></td></tr>");
		    	}
		    	}
		    	
				 var select=$("#pfx_00"+i).empty();//清空select下拉框
				 var option = commonTreeParam[i].reference2_value.split(","); //定义数组
				 $("#pingyu").append("<tr><td style='height:30px'><input id='pingYuButton"+i+"' onclick='remarkButton("+i+","+commonTreeParam[i].ID+")' type='button' class='span3 btn' value='查看此项评语' /><span style=' float: right; ' id='shifouyipingyu"+i+"'>未评语</span></td></tr>");
			}
				       // $("#grade_table_tr1").append("<td style='text-align: center;'>总分</td>");
				        //$("#grade_table_tr2").append("<td style='text-align: center;'>"+score[0].pfx_total+"</td>");
				        if(chujian.length!=0){
				       	 $("#grade_remark").val(chujian[0].grade_remark);
				        }
				        if(fushen.length!=0){
				        	$("#fushen_remark").val(fushen[0].grade_remark);
				        }
				        if(fujian.length!=0){
				        	$("#fujian_remark").val(fujian[0].grade_remark);
				        }
				   }
			
        }else{
        	window.close();
        	 zinglabs.i18n.alert("gexiangpingfen_meiyoupingfenshuju");
        }
        jisuanzongfen();
        loadPingYuCss();
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
		pat.agent_user_dt=sys_date();							//上诉时间，配置文件传当前时间
		pat.agent_user_remark=agent_user_remark;			//上诉原因，（座席复审说明）
		pat.grade_id=grade_id;  							//grade_id
		pat.pfx_total=getQueryString("total");					//分数
		pat.owner=getQueryString("owner");						//质检员
		pat.update_time=getQueryString("generate_time_state");	//评估开始时间
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
										  	$("#player2mp3").remove();
										  	pwin.zuoxishangsu_close.close();
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
		commonCRUDForComplex(pat,false,function(data){		//执行修改	
		
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
		commonCRUDForComplex(pat,false,function(data){			//执行删除
		
		});
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
	

	//计算总分			
function jisuanzongfen(){
	var zongshu=0;
	var is_involve=0;
	var selectCount=$("#pingfenzhiForm span[name]");
	for(var i=0;i<selectCount.length;i++){
		var aa=$("#score"+i).html();
		if(i<=9){
			if(!isNaN(parseFloat($("#score"+i).html()))){
				is_involve+=parseFloat($("#is_involve"+i).html());///权重
				zongshu=zongshu+parseFloat($("#score"+i).html());//分值
			}
		}
		if(i>9){
			if(!isNaN(parseFloat($("#score"+i).val()))){
				is_involve+=parseFloat($("#is_involve"+i).html());//权重
				zongshu=zongshu+parseFloat($("#score"+i).html());//分值
			}
		}
	}
  var sum=zongshu*100/is_involve+"";
  var sumScrore=sum.substring(0,sum.lastIndexOf(".")+3);
  $("#scoreSum").html("总分： "+sumScrore+"分");
  return sum;
  //$("#jisuanzongfen").val(zongshu);
}

//加载时 是否已评语样式提示
var setRemar
function loadPingYuCss(){
	var grade_name=getQueryString("grade_name");//评分指标名
	var grade_index_id=doSearchZhibiaoByDescription(grade_name);
	var selectCount=$("#pingyuForm input[id]");
	for(var i=0;i<selectCount.length;i++){
		var is_null=selectRemark(grade_index_id[i].ID);
		if(is_null.length>0){
			if(is_null[0].remark_describe!=''){
				$("#shifouyipingyu"+i).html("已评语");
			}else{
				$("#shifouyipingyu"+i).html("未评语");
				$("#pingYuButton"+i).attr('disabled','disabled');
			}
		}else{
			$("#shifouyipingyu"+i).html("未评语");
			$("#pingYuButton"+i).attr('disabled','disabled');
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

//获取评语
var remark=new Array();
function getRemark(Remark,i){
	 remark[i]=Remark;
	 pingYuCss();
}


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
		}
	}
	var params={
		title:"初检评语",
		url:"/"+window.top.PRJ_PATH+"/zh_cn/ZQC/remarkPage.html?closeFn="+getRemark+"&i="+i+"&setRemark="+setRemark,
		width:"400"
	};
	pydialog=zinglabs.dialog(params);
}


//查询初检，复检，复审评语表数据
function selectRemark(grade_index_id){
	var pat={};
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

	