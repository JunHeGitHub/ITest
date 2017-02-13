var ZQC_ROU="ZQC";
 //@@ 把评分指标的所有节点取出来
		function getpingfenzhibiaoAllNode(){
		var sum=0;
		//grade_index=getQueryString("id");
		//description=getQueryString("grade_name");				
		grade_index=241;
		var parentid=0;
		description='在线客服质量评估标准(2013年)';
		document.getElementById("grade_index_td").innerHTML=description;
			var jsons={};		
			var url="/"+window.top.PRJ_PATH+"/"+ZQC_ROU+"/ZKMCommonTree/getZhijianTreeNode.action";
			jsons.grade_index=grade_index;
			jsons.id='';
				ajaxFunction(url,jsons,false,function (data){
					if(data.success){
					var commonTreeParam=data.data;
					//alert(JSON.stringify(commonTreeParam));
					for(var i=0;i<commonTreeParam.length;i++){
					   if(commonTreeParam[i].pId==0){
					   var array = new Array(); //定义数组
					   $("#dongtaichuangjianTable").append("<table class='table table-bordered table-condensed' id='genjiedian_"+i+"'><caption>"+commonTreeParam[i].text+"</caption></table>");					   
					    for(var j=0;j<commonTreeParam.length;j++){
					    if(commonTreeParam[j].pId==commonTreeParam[i].id){
					    array.push(commonTreeParam[j]);
					    }
					   }
					   $("#genjiedian_"+i+"").append("<tr  id='tr_"+i+"'></tr>");
					   for(var k=0;k<array.length;k++){
					   $("#tr_"+i+"").append("<td style='text-align:center;' bgcolor='#FFFFCC'>"+array[k].text+"</td>");
					   }
					   $("#genjiedian_"+i+"").append("<tr  id='tr_tr_"+i+"'></tr>");
					   for(var p=0;p<array.length;p++){
					   $("#tr_tr_"+i+"").append("<td style='text-align:center;' bgcolor='#FFFFCC'>"+array[p].min_value+'~'+array[p].max_value+"</td>");
					   }
					   $("#genjiedian_"+i+"").append("<tr  id='tr_tr_tr_"+i+"'></tr>");
					   for(var r=0;r<array.length;r++){
					   var t=array[r].value_remark;
		               aa[i+'_'+r]=t;
		               var q=i+"_"+r;
					   $("#tr_tr_tr_"+i+"").append("<td bgcolor='#FFFFCC'><input type='text' id='ptf_00"+sum+"' class='input' name='ptf_00"+sum+"' style='padding: 0; margin: 0;  width: 100%' onclick=f('"+q+"') onfocus=f('"+q+"')></td>");
					   sum=sum+1;
					   }
					   }
					}			   
					}
				});			
			}
			function f(obj){
			$("#value_remark").val(aa[obj]);
			//alert(aa[obj]);
			
			}
	
			function doSearchZhibiaoByDescription(description){
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
			function doSearchIdByDescription(description){
			var pat={};				   
			pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
	    	pat.nameSpaceId = 'getIdByDescription';
	    	pat.grade_name=description;
	    	commonSelectForComplex(pat, false,
	    	function(data) {
	     	rdata=data.rows;
	    });
	    return rdata;	
			
			}
			function chujianbaocun(){
			  getCanshu['grade_remark']=$("#grade_remark").val();
			  var zong=0;
			  var fenshu={};
			  var cfs=$("#dongtaichuangjianTable input[name]");
				for(var i=0;i<cfs.length;i++){
				if(V_NumberCheck($("#pfx_00"+i).val())==false){
				 zinglabs.i18n.alert('ZJGL_tianxieshuzi');
				 return;
				}
				if($("#pfx_00"+i).val()==''){
				zinglabs.i18n.alert('ZQC_fenshubitian');
				 return;
				}
				fenshu['pfx_00'+i]=$("#pfx_00"+i).val();
				fenshuArray.push($("#pfx_00"+i).val());
				zong=zong+parseFloat($("#pfx_00"+i).val());
				}
				for(var j=cfs.length;j<10;j++){
				fenshu['pfx_00'+i]='';
				}
				fenshu['pfx_total']=zong;
				
				var newPat = $.extend({}, fenshu, getCanshu);
			   for(var i=0;i<cfs.length;i++){
				if(parseFloat(arraymin[i])>parseFloat($("#pfx_00"+i).val())||parseFloat($("#pfx_00"+i).val())>parseFloat(arraymax[i])){
				 zinglabs.i18n.alert('ZQC_shuzichaochufanwei');
				 return;
				}
				}
	            newPat.tableName = "T_GRADE_DETAIL_NEW";
				newPat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	        newPat.nameSpaceId = 'delPingfen';
				//alert(JSON.stringify(newPat));
				commonSelectForComplex(newPat, false,
		    	function(data) {
		    	}); 
				
				
				//先删再插
				
				newPat.tableName = "T_GRADE_DETAIL_NEW";
				newPat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	        newPat.nameSpaceId = 'insertPingfen';
				//alert(JSON.stringify(newPat));
				commonSelectForComplex(newPat, false,
		    	function(data) {
		    	            var pat={};
		     	       		pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
							pat.primaryKey = "id";
							pat.primaryKeyValue = newPat.id;
							pat.score_state=zinglabs.zqc.getState("chujianyipingfen");
							pat.pfx_total=newPat.pfx_total;
						    commonUpdate(pat,function(data){
						    var json=eval('('+data+')');
								     if(json.success){				        
								        zinglabs.i18n.alert('system_saveSuccess'); 
								          for(var i=0;i<cfs.length;i++){
												$("#pfx_00"+i).val('');
										  }
										  $("#grade_remark").val('');
										  $("#btn_chujianbaocun").addClass('hide');
										  $("#btn_fabu").removeClass('hide');
								      }else{
								       zinglabs.i18n.alert('system_saveFailed');
								       }
						     });     
		    	
		    	
		    	
		    	
		    	}); 
				
			}
			function fujianbaocun(){
			 getCanshu['fujian_remark']=$("#fujian_remark").val();
			 getCanshu['grade_remark']=$("#grade_remark").val();
			  var zong=0;
			  var fenshu={};
			  var cfs=$("#dongtaichuangjianTable input[name]");
				for(var i=0;i<cfs.length;i++){
				if(V_NumberCheck($("#pfx_00"+i).val())==false){
				 zinglabs.i18n.alert('ZJGL_tianxieshuzi');
				 return;
				}
				if($("#pfx_00"+i).val()==''){
				zinglabs.i18n.alert('ZQC_fenshubitian');
				 return;
				}
				fenshu['pfx_00'+i]=$("#pfx_00"+i).val();
				fenshuArray.push($("#pfx_00"+i).val());
				zong=zong+parseFloat($("#pfx_00"+i).val());
				}
				for(var j=cfs.length;j<10;j++){
				fenshu['pfx_00'+i]='';
				}
				fenshu['pfx_total']=zong;
				var newPat = $.extend({}, fenshu, getCanshu);
				 for(var i=0;i<cfs.length;i++){
				if(parseFloat(arraymin[i])>parseFloat($("#pfx_00"+i).val())||parseFloat($("#pfx_00"+i).val())>parseFloat(arraymax[i])){
				 zinglabs.i18n.alert('ZQC_shuzichaochufanwei');
				 return;
				}
				}
				newPat.tableName = "T_GRADE_DETAIL_FUJIAN_NEW";
				newPat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	        newPat.nameSpaceId = 'delPingfen_fj';
				//alert(JSON.stringify(newPat));
				commonSelectForComplex(newPat, false,
		    	function(data) {
		    	}); 
				
				
				
				
				
				newPat.tableName = "T_GRADE_DETAIL_FUJIAN_NEW";
				newPat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	        newPat.nameSpaceId = 'insertPingfen_fujian';
				//alert(JSON.stringify(newPat));
				commonSelectForComplex(newPat, false,
		    	function(data){
		    	            var pat={};
		     	       		pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
							pat.primaryKey = "id";
							pat.primaryKeyValue = newPat.id;
							pat.score_state=zinglabs.zqc.getState("fujianyipingfen");
							pat.data_state=zinglabs.zqc.getState("fujianyifenpei");
							pat.pfx_total=newPat.pfx_total;
						    commonUpdate(pat,function(data){
						    var json=eval('('+data+')');
								     if(json.success){				        
								        zinglabs.i18n.alert('system_saveSuccess'); 
								          for(var i=0;i<cfs.length;i++){
												$("#pfx_00"+i).val('');
										  }
										  $("#grade_remark").val('');
										  $("#fujian_remark").val('');
										  $("#btn_fujianbaocun").addClass('hide');
										  //$("#btn_fujianfabu").removeClass('hide');
										  document.getElementById('fujian_remark').readOnly=true;
								      }else{
								        zinglabs.i18n.alert('system_saveFailed');
								      }
						     });     
		    	}); 
			
			
			}
			function fushenbaocun(){
			getCanshu['fujian_remark']=$("#fujian_remark").val();
			 getCanshu['fushen_remark']=$("#fushen_remark").val();
			  var zong=0;
			  var fenshu={};
			  var cfs=$("#dongtaichuangjianTable input[name]");
				for(var i=0;i<cfs.length;i++){
				if(V_NumberCheck($("#pfx_00"+i).val())==false){
				 zinglabs.i18n.alert('ZJGL_tianxieshuzi');
				 return;
				}
				if($("#pfx_00"+i).val()==''){
				zinglabs.i18n.alert('ZQC_fenshubitian');
				 return;
				}
				fenshu['pfx_00'+i]=$("#pfx_00"+i).val();
				fenshuArray.push($("#pfx_00"+i).val());
				zong=zong+parseFloat($("#pfx_00"+i).val());
				}
				for(var j=cfs.length;j<10;j++){
				fenshu['pfx_00'+i]='';
				}
				fenshu['pfx_total']=zong;
				var newPat = $.extend({}, fenshu, getCanshu);
				for(var i=0;i<cfs.length;i++){
				if(parseFloat(arraymin[i])>parseFloat($("#pfx_00"+i).val())||parseFloat($("#pfx_00"+i).val())>parseFloat(arraymax[i])){
				 zinglabs.i18n.alert('ZQC_shuzichaochufanwei');
				 return;
				}
				}
				newPat.tableName = "T_GRADE_DETAIL_FUSHEN_NEW";
				newPat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
    	        newPat.nameSpaceId = 'insertPingfen_fushen';
				//alert(JSON.stringify(newPat));
				commonSelectForComplex(newPat, false,
		    	function(data) {
		    				var pat={};
		     	       		pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
							pat.primaryKey = "id";
							pat.primaryKeyValue = newPat.id;
							if(suoyou[0].pfx_total<newPat.pfx_total){
							pat.score_state=zinglabs.zqc.getState("shangsuchenggong");
							pat.data_state=zinglabs.zqc.getState("shangsuchenggong");
							}else{
							pat.score_state=zinglabs.zqc.getState("shangsushibai");
							pat.data_state=zinglabs.zqc.getState("shangsushibai");
							}
							pat.pfx_total=newPat.pfx_total;
						    commonUpdate(pat,function(data){
						    var json=eval('('+data+')');
								     if(json.success){				        
								        zinglabs.i18n.alert('system_saveSuccess'); 
								          for(var i=0;i<cfs.length;i++){
												$("#pfx_00"+i).val('');
										  }
										  $("#grade_remark").val('');
										  $("#fujian_remark").val('');
										  $("#fushen_remark").val('');
										  $("#btn_fushenbaocun").addClass('hide');
								      }else{
								        zinglabs.i18n.alert('system_saveFailed');
								      }
						     }); 
		    	}); 
			}
			
			
			function jisuanzongfen(){
			var zongshu=0;
			  var cfs=$("#dongtaichuangjianTable input[name]");
			  //alert(cfs.length);
				for(var i=0;i<cfs.length;i++){
				if(V_NumberCheck($("#pfx_00"+i).val())==false){
				//alert('填数字！');
				 zinglabs.i18n.alert('ZJGL_tianxieshuzi');
				 return;
				}
				if($("#pfx_00"+i).val()==''){
				 //alert('不能是空！');
				 zinglabs.i18n.alert('ZQC_fenshubitian');
				 return;
				}
				zongshu=zongshu+parseFloat($("#pfx_00"+i).val());
				}
			  $("#jisuanzongfen").removeClass('hide');
			  $("#jisuanzongfen").val(zongshu);
			}
			
			function fabu(){
			  var pat={};
		     	       		pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
							pat.primaryKey = "id";
							pat.primaryKeyValue = getCanshu.id;
							pat.score_state=zinglabs.zqc.getState("chujianyipingfen");
							pat.data_state=zinglabs.zqc.getState("chujianyifabu");
						    commonUpdate(pat,function(data){
						    var json=eval('('+data+')');
								     if(json.success){				        
								        //alert('修改成功');
								        zinglabs.i18n.alert('ZQC_fabuchenggong');
										  $("#btn_fabu").addClass('hide');
								      }else{
								        //alert('修改失败');
								         zinglabs.i18n.alert('ZQC_fabushibai');
								       }
						     }); 
			
			}
			function fabu_fujian(){
			var pat={};
		     	       		pat.tableName = "SU_QC_SOURCE_RECORD_DATA";
							pat.primaryKey = "id";
							pat.primaryKeyValue = getCanshu.id;
							pat.score_state=zinglabs.zqc.getState("fujianyifabu");
						    commonUpdate(pat,function(data){
						    var json=eval('('+data+')');
								     if(json.success){				        
								         //alert('修改成功');
								       zinglabs.i18n.alert('ZQC_fujianfabuchenggong');
										  $("#btn_fujianfabu").addClass('hide');
								      }else{
								        //alert('修改失败');
								         zinglabs.i18n.alert('ZQC_fujianfabushibai');
								       }
						     }); 
			
			}
			function getFenshuByGrade_id(grade_id){
			var pat={};				   
			pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
	    	pat.nameSpaceId = 'getFenshuByGrade_id';
	    	pat.grade_id=grade_id;
	    	commonSelectForComplex(pat, false,
	    	function(data) {
	     	rdata=data.rows;
	    	});
	    	return rdata;
			}
			function getFenshuByGrade_id_fj(grade_id){
			var pat={};				   
			pat.nameSpace = 'com.zinglabs.apps.mybaits_xml_def.ZQC';
	    	pat.nameSpaceId = 'getFenshuByGrade_id_fj';
	    	pat.grade_id=grade_id;
	    	commonSelectForComplex(pat, false,
	    	function(data) {
	     	rdata=data.rows;
	    	});
	    	return rdata;
			}
			function g(s,b,p){
			if($("#"+p+"").val()>b||$("#"+p+"").val()<s){
			zinglabs.i18n.alert('ZQC_shuzichaochufanwei');
			}
			if(V_NumberCheck($("#"+p+"").val())==false){
				 zinglabs.i18n.alert('ZJGL_tianxieshuzi');
				 return;
				
			}
			var cfs=$("#dongtaichuangjianTable input[name]");
			var sum=0;
				for(var i=0;i<cfs.length;i++){
				 if($("#pfx_00"+i).val()!=''&&$("#pfx_00"+i).val()!=null){
				 sum=sum+parseFloat($("#pfx_00"+i).val());
				 }
				}
			$("#jisuanzongfen").val(sum);
			
			}
function chaKanWenBenNeiRong(){
	var file_address=$("#file_address").val();
	
	var params={
		title:"文本内容",
		width:"700",
		content: '<iframe src='+file_address+' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>'
	};
	zinglabs.dialog(params);
}