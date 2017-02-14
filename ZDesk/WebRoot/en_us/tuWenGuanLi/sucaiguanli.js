var pwin = window.parent;
var zituwengeshu=0;
var quUUID=0;
var jilu={};
var tupianjilu={};
var Img_Div;
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
function fanhui(){
history.go(-1);

}
function baocun(){
var reFillFn1 = getQueryString("closeFn");
var attr = document.getElementsByTagName('form') ; //获取当前formid
                fromId=attr[0].getAttribute("id");
if(fromId!=""){
tianjiazituwen_wanzheng();
}
               //var myDate = new Date();
               var s=fomateDate("YYYY-MM-dd hh：mm：ss",new Date());
               var arry=new Array();
               for(var i=0;i<50;i++){
               if(jilu["tableForm"+i+""]!=null&&jilu["tableForm"+i+""]!=""&&jilu["tableForm0"].isRelease=="发布"){
               jilu["tableForm"+i+""].releaseTime=s;
               jilu["tableForm"+i+""].releasePeople=getUserInfo().loginName;
               jilu["tableForm"+i+""].isRelease=jilu["tableForm0"].isRelease;
              //jilu["tableForm"+i+""].releasePeople="admin";
              jilu["tableForm"+i+""].Category=jilu["tableForm0"].Category;
               arry.push(jilu["tableForm"+i+""]);
               }
               if(jilu["tableForm"+i+""]!=null&&jilu["tableForm"+i+""]!=""&&jilu["tableForm0"].isRelease=="未发布"){
               jilu["tableForm"+i+""].isRelease=jilu["tableForm0"].isRelease;
               jilu["tableForm"+i+""].Category=jilu["tableForm0"].Category;
               arry.push(jilu["tableForm"+i+""]);
               }
               }
               var arry1=new Array();
               for(var k=0;k<50;k++){
               if(tupianjilu["Img_Div"+k+""]!=null&&tupianjilu["Img_Div"+k+""]!=""){
               tupianjilu["Img_Div"+k+""].uploadTime=s;
               tupianjilu["Img_Div"+k+""].uploadPeople=getUserInfo().loginName;
              //tupianjilu["Img_Div"+k+""].uploadPeople="admin";
              tupianjilu["Img_Div"+k+""].genusGroup="未分组";
               arry1.push(tupianjilu["Img_Div"+k+""]);
               }
               }
			    var params1={};
			    //var json={};
			    //json.imageName=imageName;
			    //json.imageUrl=imageUrl;
			    //json.uploadTime=fomateDate("YYYY-MM-dd hh:mm:ss",new Date());
			    params1.columnValues=arry;
			    params1.tableName="graphicInformationManager";
			    params1. primarykeyType="";
			    //使用增
			    //alert(JSON.stringify(params1));
			    commonInsertOrUpdate(params1,true,function(data){
			    if(data.success){
			    if(arry1!=""&&arry1!=null){
			    var params={};
			    params.columnValues=arry1;
			    params.tableName="pictureLibrary";
			    params. primarykeyType="";
			    //使用增
			    commonInsertOrUpdate(params,true,function(data){
			        if(data.success){
			         window.top.zinglabs.alert('添加多图文成功');
			         pwin[reFillFn1]();
			         pwin["doSearch1"]();
			        }
			    }); 
			    }else{
			     window.top.zinglabs.alert('您未添加图片,保存信息成功');
			     pwin[reFillFn1]();
			     pwin["doSearch1"]();
			    }
			    }
			    
			    }); 
			    



                
}
function baocun_dan(){
var reFillFn = getQueryString("closeFn");
var attr = document.getElementsByTagName('form') ; //获取当前formid
                fromId=attr[0].getAttribute("id");
if(fromId!=""){
tianjiazituwen_wanzheng();
}
               //var myDate = new Date();
               var s=fomateDate("YYYY-MM-dd hh:mm:ss",new Date());
               var arry=new Array();
               for(var i=0;i<50;i++){
               if(jilu["tableForm"+i+""]!=null&&jilu["tableForm"+i+""]!=""&&jilu["tableForm0"].isRelease=="发布"){
               jilu["tableForm"+i+""].releaseTime=s;
               jilu["tableForm"+i+""].releasePeople=getUserInfo().loginName;
              //jilu["tableForm"+i+""].releasePeople="admin";
              jilu["tableForm"+i+""].groupId="";
               arry.push(jilu["tableForm"+i+""]);
               }
               if(jilu["tableForm"+i+""]!=null&&jilu["tableForm"+i+""]!=""&&jilu["tableForm0"].isRelease=="未发布"){
               
               arry.push(jilu["tableForm"+i+""]);
               }
               }
               var arry1=new Array();
               for(var k=0;k<50;k++){
               if(tupianjilu["Img_Div"+k+""]!=null&&tupianjilu["Img_Div"+k+""]!=""){
               tupianjilu["Img_Div"+k+""].uploadTime=s;
              tupianjilu["Img_Div"+k+""].uploadPeople=getUserInfo().loginName;
              //tupianjilu["Img_Div"+k+""].uploadPeople="admin";
              tupianjilu["Img_Div"+k+""].genusGroup="未分组";
               arry1.push(tupianjilu["Img_Div"+k+""]);
               }
               }
			    var params1={};
			    //var json={};
			    //json.imageName=imageName;
			    //json.imageUrl=imageUrl;
			    //json.uploadTime=fomateDate("YYYY-MM-dd hh:mm:ss",new Date());
			    params1.columnValues=arry;
			    params1.tableName="graphicInformationManager";
			    params1.primarykeyType="";
			    //使用增
			    //alert(JSON.stringify(params1));
			    commonInsertOrUpdate(params1,true,function(data){
			    if(data.success){
			    if(arry1!=""&&arry1!=null){
			    var params={};
			    params.columnValues=arry1;
			    params.tableName="pictureLibrary";
			    params. primarykeyType="";
			    //使用增
			    commonInsertOrUpdate(params,true,function(data){
			        if(data.success){
			        window.top.zinglabs.alert('添加单图文成功');
			       pwin[reFillFn]();
			       pwin["doSearch1"]();
			        }
			    }); 
			    }else{
			   window.top.zinglabs.alert('您未添加图片,保存信息成功');
			    pwin[reFillFn]();
			    pwin["doSearch1"]();
			    }
			    
			    }
			    
			    }); 
			    
			    



                
}
function tianjiazituwenxinxi(){
var attr = document.getElementsByTagName('form') ; //获取当前formid
                fromId=attr[0].getAttribute("id");
if(fromId!=""){
tianjiazituwen_wanzheng();
}

zituwengeshu=zituwengeshu+1;
$("#td_add").append('<div id="Img_Div'+zituwengeshu+'" style="margin-bottom: 5px;width: 380px; height: 100px;  border:1px solid #F5F5F5; border-radius:12px;background-color: #F5F5F5;margin-left:1px;margin-right:0px  ">'+
								'<input type="button" id="xiugai'+zituwengeshu+'" onclick="xiugai('+zituwengeshu+'); " style=" width:190px;border:0;margin-left: 0px" class="btn" value="修改" />'+
								'<input type="button" id="shanchu'+zituwengeshu+'" onclick="shanchu('+zituwengeshu+'); " style=" width:190px;border:0;" class="btn" value="删除" />'+
								'<img id="img_Article_Img'+zituwengeshu+'" alt="等待您的上传" src="" style="width: 90px; height: 50px;margin-top:10px;padding-left:240px;border:1px solid #F5F5F5;background-color: #F5F5F5;visibility: hidden;margin-left:38px;margin-right:20px" />'+
								'<div style="width: 230px; white-space:nowrap;overflow:hidden;text-overflow:ellipsis;margin-top: -35px;margin-left:38px;" id="biaoti'+zituwengeshu+'"></div>'+
								
							'</div>');   
var attr1 = document.getElementsByTagName('form') ; //获取当前formid
attr1[0].setAttribute("id", "tableForm"+zituwengeshu+"");

clearForm();
}
function shanchu(a){
tianjiazituwen_wanzheng();
var my = document.getElementById("Img_Div"+a+"");
var shagngejiedian=my.previousSibling.id;
				var  tableid;
    if (my != null)
     my.parentNode.removeChild(my);
     if(tupianjilu.hasOwnProperty("Img_Div"+a+"")&&(tupianjilu["Img_Div"+a+""]!=null||tupianjilu["Img_Div"+a+""]!="")){
     delete tupianjilu["Img_Div"+a+""];
     }
     delete jilu["tableForm"+a+""];
       if(shagngejiedian=="button_tianjiazitu"){
			      var de=document.getElementById("button_tianjiazitu");
				  shagngejiedian=de.previousSibling.id;
				  tableid =shagngejiedian.substr(shagngejiedian.indexOf("Img_Div")+7, shagngejiedian.length); 
			      }else{
			      tableid =shagngejiedian.substr(shagngejiedian.indexOf("Img_Div")+7, shagngejiedian.length); 
			      }
     var attr1 = document.getElementsByTagName('form') ; //获取当前formid
				  attr1[0].setAttribute("id", "tableForm"+tableid+"");
				  var json=jilu["tableForm"+tableid+""];
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
					if(json.isRelease=="发布"){
						document.getElementById("isRelease1").checked=true;
					}else{
						document.getElementById("isRelease0").checked=true;
					}
					$("#img_Article_Img"+tableid+"").attr("src", json.coverPhysicalPath);
					if(json.textContent==""||json.textContent==undefined){
					UE.getEditor('editor').setContent("",false);
					}else{
					UE.getEditor('editor').setContent(json.textContent,false);
					}
}
function change(s){

document.getElementById("biaoti"+s+"").innerHTML=$("#title").val();
//$("#zhaiyao"+s+"").val($("#abstract").val());
}
function change1(s){

document.getElementById("zhaiyao"+s+"").innerHTML=$("#abstract").val();
//$("#zhaiyao"+s+"").val($("#abstract").val());
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
                var tuid=uuid[""+quUUID+""];
                quUUID=quUUID+1;
                var pid=uuid[""+quUUID+""];
                quUUID=quUUID+1;
                //alert(tuid+" "+pid);
                var attr = document.getElementsByTagName('form') ; //获取当前formid
                fromId=attr[0].getAttribute("id");
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
                Img_Div = fromId.substr(fromId.indexOf("tableForm")+9, fromId.length); 
                if(imageUrl==""&&tupianjilu["Img_Div"+Img_Div+""]!=null&&tupianjilu["Img_Div"+Img_Div+""].imageUrl!=""){
                params.columnValues.coverUrl=tupianjilu["Img_Div"+Img_Div+""].imageUrl;
                params.columnValues.coverPhysicalPath =tupianjilu["Img_Div"+Img_Div+""].imagePhysicalPath;
                params.columnValues.coverID= tupianjilu["Img_Div"+Img_Div+""].id;
                var pjson={};
			    pjson.id=tupianjilu["Img_Div"+Img_Div+""].id;
			    pjson.imageName=tupianjilu["Img_Div"+Img_Div+""].imageName;
			    pjson.uploadImageName=tupianjilu["Img_Div"+Img_Div+""].uploadImageName;
			    pjson.imageUrl=tupianjilu["Img_Div"+Img_Div+""].imageUrl;
			    pjson.imagePhysicalPath=tupianjilu["Img_Div"+Img_Div+""].imagePhysicalPath;
                }else if(imageUrl==""){
                 params.columnValues.coverUrl="";
                  params.columnValues.coverPhysicalPath ="";
                 params.columnValues.coverID="";
                }else{
                //alert(pid);
                
                params.columnValues.coverUrl = imageUrl;
                params.columnValues.coverPhysicalPath = coverPhysicalPath;
                params.columnValues.coverID= pid;
                }
                if(fromId.substr(fromId.indexOf("tableForm")+9, fromId.length)=='0'){
			     params.columnValues.id=parentId;
			    }else{
			    params.columnValues.id=tuid;
			    }
			    //params.columnValues.id=tuid;
			    if(fromId.substr(fromId.indexOf("tableForm")+9, fromId.length)=='0'){
			     params.columnValues.parentId=0;
			    }else{
			    params.columnValues.parentId=parentId;
			    }
			    params.columnValues.groupId=parentId;
			    delete params.columnValues.editorValue;
			    if(imageUrl!=""){
			    var pjson={};
			    pjson.id=pid;
			    pjson.imageName=imageName;
			    pjson.uploadImageName=uploadImageName;
			    pjson.imageUrl=imageUrl;
			    pjson.imagePhysicalPath=coverPhysicalPath;
			    }
			    Img_Div = fromId.substr(fromId.indexOf("tableForm")+9, fromId.length); 
			   tupianjilu["Img_Div"+Img_Div+""] =pjson;
			   //alert(JSON.stringify(tupianjilu));
			   jilu[fromId] =params.columnValues;
			    //params.tableName="graphicInformationManager";
			    //alert(JSON.stringify(jilu));
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
function xiugai(xiugaiId){
xiugaitupianId=xiugaiId;
var attr = document.getElementsByTagName('form') ; //获取当前formid
                fromId=attr[0].getAttribute("id");
if(fromId!=""){
tianjiazituwen_wanzheng();
}
   imageName="";
   imageUrl="";
   coverPhysicalPath="";
   uploadImageName="";
xiebiaodan();
}
function dayin(){
alert(JSON.stringify(tupianjilu)); 
alert(JSON.stringify(jilu)); 
}
function xiebiaodan(){
var attr1 = document.getElementsByTagName('form') ; //获取当前formid
attr1[0].setAttribute("id", "tableForm"+xiugaitupianId+"");
var json=jilu["tableForm"+xiugaitupianId+""];
//alert(JSON.stringify(json));
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
			if(json.isRelease=="发布"){
			document.getElementById("isRelease1").checked=true;
			}else{
			document.getElementById("isRelease0").checked=true;
			}
			//alert(data.data[0].textContent);
			//UE.getEditor('editor').setContent('1',true);
			if(json.textContent==undefined||json.textContent==""){
			UE.getEditor('editor').setContent(" ",false);
			}else{
			UE.getEditor('editor').setContent(json.textContent,false);
			}
			
}