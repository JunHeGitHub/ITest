var pwin = window.parent;
var tupianjilu={};
var quUUID=0;
var zituwengeshu=0;
function getUUID(params, sync ,fn) {
	var url = "/" +PRJ_PATH+ "/" +"zkm"+"/CommonGenerateGUID" + "/" + "generateGUID.action?generateNumber=10";
	$.ajax({
		async : sync,
		type : "post",
		url : url,
		dataType : "json",
		data : {},
		success : function(data) {
			if (fn && typeof(fn) == 'function') {
				fn(data);
			}
		},
		error : function(r) {					
			if (fn && typeof(fn) == 'function') {
				fn('error');
			}
		}
	});
}
function xiebiaodan(json){
			$("#title").val(json.title); 
			$("#autor").val(json.autor); 
			if(json.coverToTextContent==1){
			document.getElementById("coverToTextContent").checked=true;
			}else{
			document.getElementById("coverToTextContent").checked=false;
			}
			$("#abstract").val(json.abstract);
			$("#Category").val(json.Category);
			$("#originalLink").val(json.originalLink); 
			if(json.isRelease=="已发布"){
			document.getElementById("isRelease1").checked=true;
			}else{
			document.getElementById("isRelease0").checked=true;
			}
			//alert(data.data[0].textContent);
			//UE.getEditor('editor').setContent('1',true);
			//UE.getEditor('editor').setContent(json.textContent,false);
			editor.addListener("ready", function () {        // editor准备好之后才可以使用        
			if(json.textContent==undefined||json.textContent==""){
			editor.setContent(" ",false);
			}else{
			editor.setContent(json.textContent,false);
			}
			
			
			});
}


/*function baocun(){
			if($("#title").val()==''||$("#abstract").val()==''){
			zinglabs.alert('标题和摘要为必填项');
			return;
			}
			var tupian=document.getElementById("img_Article_Img"); 
			var zhi=tupian.src;
 			var a=zhi.indexOf("jsp");
			if(a==-1){
			zinglabs.alert('请上传图片');
			return;
			}               
            var id;
			var params1={};
			var json={};
			json.imageName=imageName;
			json.imageUrl=imageUrl;
			json.uploadTime=fomateDate("YYYY-MM-dd hh:mm:ss",new Date());
			params1.columnValues=json;
			params1.tableName="pictureLibrary";
			commonInsertOrUpdate(params1,true,function(data){
			        id=data.idValues[0];
			        var tableForm=zkm_getHtmlFormJsons("tableForm");
			    	var params={};
			    	params.columnValues=tableForm;
			        var ch=document.getElementById("coverToTextContent");
                	if(ch.checked){
                		params.columnValues.coverToTextContent=1;
                	}else{
                		params.columnValues.coverToTextContent=0;
                	 }
			    	delete params.columnValues.zhaiyao;
			    	params.columnValues.textContent= params.columnValues.editorValue;
			    	params.columnValues.coverUrl= imageUrl;
			    	params.columnValues.coverID= id;
			    	delete params.columnValues.editorValue;
			    	params.tableName="graphicInformationManager";
			    	commonInsertOrUpdate(params,true,function(data){
			    		if(data.success){
			    		zinglabs.alert('添加成功');
			    		}
			    	}); 
			}); 
}*/


function tianjiazituwenxinxi_(){
			var attr = document.getElementsByTagName('form') ; //获取当前formid
			fromId=attr[0].getAttribute("id");
			if(fromId!=""){
			tianjiazituwen_wanzheng();
			}
			zituwengeshu=zituwengeshu+1;
			$("#button_tianjiazitu").before('<div id="Img_Div'+zituwengeshu+'" style="margin-bottom: 5px;width: 380px; height: 100px;  border:1px solid #F5F5F5; border-radius:12px;background-color: #F5F5F5;margin-left:1px;margin-right:0px  ">'+
								'<input type="button" id="xiugai'+zituwengeshu+'" onclick="xiugai(\''+zituwengeshu+'\'); " style=" width:190px;border:0;margin-left: 0px" class="btn" value="修改" />'+
								'<input type="button" id="shanchu'+zituwengeshu+'" onclick="shanchu(\''+zituwengeshu+'\'); " style=" width:190px;border:0;" class="btn" value="删除" />'+
								
								'<img id="img_Article_Img'+zituwengeshu+'" alt="等待您的上传" src="" style="width: 90px; height: 50px;margin-top:10px;padding-left:240px;border:1px solid #F5F5F5;background-color: #F5F5F5;visibility: hidden;margin-left:38px;margin-right:20px" />'+
								'<div style="width: 230px; white-space:nowrap;overflow:hidden;text-overflow:ellipsis;margin-top: -35px;margin-left:38px;" id="biaoti'+zituwengeshu+'"></div>'+
								'</div>');  
			var attr1 = document.getElementsByTagName('form') ; //获取当前formid
			attr1[0].setAttribute("id", "tableForm"+zituwengeshu+"");
			clearForm(); 
}

function clearForm(){
		   imageName="";
		   imageUrl="";
		   coverPhysicalPath="";
		   uploadImageName="";
		   $("#title").val('');
		   $("#autor").val('');
		   document.getElementById("coverToTextContent").checked=false;
		   //editor.addListener("ready", function () {        // editor准备好之后才可以使用        
					//editor.setContent('sssss',false);});
		   UE.getEditor('editor').setContent('',false);
		   $("#abstract").val('');
		   $("#originalLink").val('');
		   document.getElementById("isRelease1").checked=true;
		   document.getElementById("isRelease0").checked=false;
}


function change(s){
document.getElementById("biaoti"+s+"").innerHTML=$("#title").val();

}

function baocun(){
			   var t=0;
			   var reFillFn1 = getQueryString("closeFn");
			   var attr = document.getElementsByTagName('form') ; //获取当前formid
			   fromId=attr[0].getAttribute("id");
		       if(fromId!=""){
				tianjiazituwen_wanzheng();
			   }
			    var tp={};
               var allPic=[];
               tp.tableName="pictureLibrary";
               commonSelect(tp,false,function(data){
				 allPic=data.data;
				});
               var s=fomateDate("YYYY-MM-dd hh:mm:ss",new Date());
               var arry=new Array();
               var key1;
               var type="";
               for(var key in jsonshuju){
               if(jsonshuju[key].parentId=='0'&&jsonshuju[key].isRelease=="已发布"){
                    key1=key;
                    type=jsonshuju[key].Category;
	                jsonshuju[key].releaseTime=s;
	               jsonshuju[key].releasePeople=getUserInfo().loginName;
	               jsonshuju[key].singleOrDouble="多图文";
	               if(jsonshuju[key].originalLink==""||jsonshuju[key].originalLink==null){
	               jsonshuju[key].originalLink="/home/graphicText.html?id="+jsonshuju[key].id;
	               }
	                //jsonshuju[key].releasePeople="admin";
	                arry.push(jsonshuju[key]);
               }  
               }
               for(var key in jsonshuju){
               if(jsonshuju[key1].isRelease=="已发布"&&key1!=key){
                    t=1;
	                jsonshuju[key].releaseTime=s;
	                jsonshuju[key].Category=type;
	                jsonshuju[key].isRelease="已发布";
	                if(jsonshuju[key].originalLink==""||jsonshuju[key].originalLink==null){
	               jsonshuju[key].originalLink="/home/graphicText.html?id="+jsonshuju[key].id;
	               }
	                jsonshuju[key].releasePeople=getUserInfo().loginName;
	                //jsonshuju[key].releasePeople="admin";
	                arry.push(jsonshuju[key]);
               }  
               }
               if(t==0){
               if(jsonshuju[key1].originalLink==""||jsonshuju[key1].originalLink==null){
	               jsonshuju[key1].originalLink="/home/graphicText.html?id="+jsonshuju[key1].id;
	               }
               jsonshuju[key1].singleOrDouble="单图文";
               jsonshuju[key1].groupId="";
               } 
                var arry1=new Array();
                for(var key in tupianjilu){
               		tupianjilu[key].uploadTime=s;
                   tupianjilu[key].uploadPeople=getUserInfo().loginName;
                    //tupianjilu[key].uploadPeople="admin";
                    tupianjilu[key].genusGroup="未分组";
	                arry1.push(tupianjilu[key]);
               }
               
               
                for(var p=0;p<arry1.length;p++){
                  for(var y=0;y<allPic.length;y++){
                      if(arry1[p].imageName==allPic[y].imageName&&arry1[p].uploadImageName==allPic[y].uploadImageName){
                        for(var r=0;r<arry.length;r++){
                           if(arry1[p].id==arry[r].coverID){
                           arry[r].coverID=allPic[y].id;
                           }
                        }
                        arry1[p]="";
                      }
                  }
               }
                   var arry2=new Array();
               for(var w=0;w<arry1.length;w++){
               if(arry1[w]!=""){
               arry2.push(arry1[w]);
               }
               } 
			    var params1={};
			    params1.columnValues=arry;
			    params1.tableName="graphicInformationManager";
			    params1.primarykeyType="";
			    params1.primarykey="id";
			    //使用增
			    //alert(JSON.stringify(params1));
			    commonInsertOrUpdate(params1,true,function(data){
			    if(data.success){
			    if(arry2!=""&&arry2!=null){
			    var params={};
			    params.columnValues=arry2;
			    params.tableName="pictureLibrary";
			    params.primarykeyType="";
			    //使用增
			    commonInsertOrUpdate(params,true,function(data){
			        if(data.success){
			         window.top.zinglabs.alert('修改图文成功');
			         pwin[reFillFn1]();
			         pwin["doSearch1"]();
			        }
			    }); 
			    }else{
			     window.top.zinglabs.alert('修改信息成功');
			    pwin[reFillFn1]();
			    pwin["doSearch1"]();
			    }
			    }
			    
			    }); 
}

function xiugai(xiugaiId){
		xiugaitupianId=xiugaiId;
		var attr = document.getElementsByTagName('form') ; //获取当前formid
        fromId=attr[0].getAttribute("id");
		if(fromId!=""&&jsonshuju.hasOwnProperty(fromId) ){
			tianjiazituwen_wanzheng();
		}
		if(fromId!=""&&!jsonshuju.hasOwnProperty(fromId)){
			tianjiazituwen_wanzheng();
		}
   		imageName="";
   		imageUrl="";
   		coverPhysicalPath="";
   		uploadImageName="";
		xiebiaodan1();
}


function xiebiaodan1(){
		var attr1 = document.getElementsByTagName('form') ; //获取当前formid
		attr1[0].setAttribute("id", "tableForm"+xiugaitupianId+"");
		var json=jsonshuju["tableForm"+xiugaitupianId+""];
		$("#title").val(json.title); 
		$("#autor").val(json.autor); 
		if(json.coverToTextContent==1){
			document.getElementById("coverToTextContent").checked=true;
		}else{
			document.getElementById("coverToTextContent").checked=false;
		}
		$("#abstract").val(json.abstract);
		$("#Category").val(json.Category);
		$("#originalLink").val(json.originalLink); 
		if(json.isRelease=="已发布"){
			document.getElementById("isRelease1").checked=true;
		}else{
			document.getElementById("isRelease0").checked=true;
		}
		$("#img_Article_Img"+xiugaitupianId+"").attr("src", json.coverPhysicalPath); 
		if(json.textContent==undefined||json.textContent==""){
			UE.getEditor('editor').setContent(" ",false);
			}else{
			UE.getEditor('editor').setContent(json.textContent,false);
			}
}


function  tianjiazituwen_wanzheng(){

				if(quUUID==10){
				getUUID({}, true ,function(data){
				         var u=data.data;
		         for(var l=0;l<10;l++){
		          uuid[l]=u[l];
		         }
				});
				   quUUID=0;      
				}
				var attr = document.getElementsByTagName('form') ; //获取当前formid
                fromId=attr[0].getAttribute("id");
				if(fromId!=""&&jsonshuju.hasOwnProperty(fromId) ){
					 	var tuid=uuid[""+quUUID+""];
		                quUUID=quUUID+1;
		                var pid=uuid[""+quUUID+""];
		                quUUID=quUUID+1;
						var tableForm=zkm_getHtmlFormJsons(fromId);
					    var params={};
					    params.columnValues=tableForm;
					    var ch=document.getElementById("coverToTextContent");
		                if(ch.checked){
		                params.columnValues.coverToTextContent=1;
		                }else{
		                params.columnValues.coverToTextContent=0;
		                }
		                //params.columnValues.Category="";
		                params.columnValues.chainType="";
		                params.columnValues.textContent= params.columnValues.editorValue;
		                params.columnValues.id=jsonshuju[fromId].id;
		                params.columnValues.parentId=jsonshuju[fromId].parentId;
		                params.columnValues.groupId=jsonshuju[fromId].groupId;
		                Img_Div = fromId.substr(fromId.indexOf("tableForm")+9, fromId.length); 
		                if(coverPhysicalPath!=""&&jsonshuju[fromId].coverPhysicalPath!=coverPhysicalPath){
			                params.columnValues.coverUrl=imageUrl;
			                params.columnValues.coverPhysicalPath =coverPhysicalPath;
			                params.columnValues.coverID=pid;
			                var pjson={};
						    pjson.id=pid;
						    pjson.imageName=imageName;
						    pjson.uploadImageName=uploadImageName;
						    pjson.imageUrl=imageUrl;
						    pjson.imagePhysicalPath=coverPhysicalPath;
		                }else {
		                		 params.columnValues.coverUrl=jsonshuju[fromId].coverUrl;
		                  		 params.columnValues.coverPhysicalPath =jsonshuju[fromId].coverPhysicalPath;
		                 		 params.columnValues.coverID=jsonshuju[fromId].coverID;
		                }
					    delete params.columnValues.editorValue;
					    if(coverPhysicalPath!=""){
					    var pjson={};
						    pjson.id=pid;
						    pjson.imageName=imageName;
						    pjson.uploadImageName=uploadImageName;
						    pjson.imageUrl=imageUrl;
						    pjson.imagePhysicalPath=coverPhysicalPath;
						    tupianjilu["Img_Div"+Img_Div+""] =pjson;
					    }
					    jsonshuju[fromId] =params.columnValues;
				}
				if(!jsonshuju.hasOwnProperty(fromId) ){
		                var tuid=uuid[""+quUUID+""];
		                quUUID=quUUID+1;
		                var pid=uuid[""+quUUID+""];
		                quUUID=quUUID+1;
						var tableForm=zkm_getHtmlFormJsons(fromId);
					    var params={};
					    params.columnValues=tableForm;
					    var ch=document.getElementById("coverToTextContent");
		                if(ch.checked){
		                params.columnValues.coverToTextContent=1;
		                }else{
		                params.columnValues.coverToTextContent=0;
		                }
		                //params.columnValues.Category="";
		                params.columnValues.chainType="";
		                params.columnValues.textContent= params.columnValues.editorValue;
		                params.columnValues.id=tuid;
		                params.columnValues.parentId=parentId;
		                params.columnValues.groupId=groupId;
		                Img_Div = fromId.substr(fromId.indexOf("tableForm")+9, fromId.length); 
		                if(coverPhysicalPath!=""){
			                params.columnValues.coverUrl=imageUrl;
			                params.columnValues.coverPhysicalPath =coverPhysicalPath;
			                params.columnValues.coverID=pid;
			                var pjson={};
						    pjson.id=pid;
						    pjson.imageName=imageName;
						    pjson.uploadImageName=uploadImageName;
						    pjson.imageUrl=imageUrl;
						    pjson.imagePhysicalPath=coverPhysicalPath;
		                }else {
		                		 params.columnValues.coverUrl="";
		                  		 params.columnValues.coverPhysicalPath ="";
		                 		 params.columnValues.coverID="";
		                }
					    delete params.columnValues.editorValue;
					    if(coverPhysicalPath!=""){
					    var pjson={};
						    pjson.id=pid;
						    pjson.imageName=imageName;
						    pjson.uploadImageName=uploadImageName;
						    pjson.imageUrl=imageUrl;
						    pjson.imagePhysicalPath=coverPhysicalPath;
						    tupianjilu["Img_Div"+Img_Div+""] =pjson;
					    }
					    jsonshuju[fromId] =params.columnValues;
			}
}

function dayin(){
/*var attr = document.getElementsByTagName('form') ; //获取当前formid
                fromId=attr[0].getAttribute("id");
                Img_Div = fromId.substr(fromId.indexOf("tableForm")+9, fromId.length); */
alert(JSON.stringify(tupianjilu)); 
alert(JSON.stringify(jsonshuju)); 
/*var aa=document.getElementById("Img_Div62ACFECD-6F7A-4EF5-F37F-B669DBCB21B4");
alert(aa.previousSibling.id); */
}

function shanchu(a){
				tianjiazituwen_wanzheng();
				var my = document.getElementById("Img_Div"+a+"");
				var shagngejiedian=my.previousSibling.id;
				var shagngejiedian= $("#Img_Div"+a+"").prev().attr("id");
				var  tableid;
			    if (my != null)
			    my.parentNode.removeChild(my);
			    var params={};
       			var params1={};
	   			params1.tableName="graphicInformationManager";
	   //主键默认为id  如不为id 加上params.primarykey
	   			params1.primarykey="id";
	     		params1.columnValues=jsonshuju["tableForm"+a+""].id;
	     		commonDelete(params1,true,function(data){
	         		if(data.success){
	         		}
	    		}); 
			    if(tupianjilu.hasOwnProperty("Img_Div"+a+"")&&(tupianjilu["Img_Div"+a+""]!=null||tupianjilu["Img_Div"+a+""]!="")){
			      delete tupianjilu["Img_Div"+a+""];
			    }
			      delete jsonshuju["tableForm"+a+""];
			      if(shagngejiedian=="button_tianjiazitu"){
			      var de=document.getElementById("button_tianjiazitu");
				  shagngejiedian=de.previousSibling.id;
				  tableid =shagngejiedian.substr(shagngejiedian.indexOf("Img_Div")+7, shagngejiedian.length); 
			      }else{
			      tableid =shagngejiedian.substr(shagngejiedian.indexOf("Img_Div")+7, shagngejiedian.length); 
			      }
			      var attr1 = document.getElementsByTagName('form') ; //获取当前formid
				  attr1[0].setAttribute("id", "tableForm"+tableid+"");
				  var json=jsonshuju["tableForm"+tableid+""];
					$("#title").val(json.title); 
					$("#autor").val(json.autor); 
					if(json.coverToTextContent==1){
						document.getElementById("coverToTextContent").checked=true;
					}else{
						document.getElementById("coverToTextContent").checked=false;
					}
					$("#abstract").val(json.abstract);
					$("#Category").val(json.Category);
					$("#originalLink").val(json.originalLink); 
					if(json.isRelease=="已发布"){
						document.getElementById("isRelease1").checked=true;
					}else{
						document.getElementById("isRelease0").checked=true;
					}
					$("#img_Article_Img"+tableid+"").attr("src", json.coverPhysicalPath); 
					if(json.textContent==undefined||json.textContent==""){
					UE.getEditor('editor').setContent(" ",false);
					}else{
					UE.getEditor('editor').setContent(json.textContent,false);
					}
				  
}
