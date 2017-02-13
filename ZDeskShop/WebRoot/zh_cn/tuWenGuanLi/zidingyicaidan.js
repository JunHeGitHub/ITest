/*
获取菜单
json数据格式
*/
function getMenu(t){
			var type = t;
			var jsons={};	
			var rData=[];
			var resultData={"length":0};
			var count=0;
			var url="/"+PRJ_PATH+"/ZKM/ZKMCommonTree/getCaidanTreeNode_geshu.action";
			jsons.type=type;
				ajaxFunction(url,jsons,false,function (data){
					if(data.success){
						count=data.data.length;
						for(var i=0;i<data.data.length;i++){
							var q={};
							q.name=data.data[i]["name"];
							q.address=data.data[i]["u"];
							q.id=data.data[i]["id"];
							q.data=[];
							resultData[i]=q;
						}
						resultData.length=data.data.length;
					}
				});
			url="/"+PRJ_PATH+"/ZKM/ZKMCommonTree/getCaidanTreeNode_isStart1.action";
			jsons.type=type;
				ajaxFunction(url,jsons,false,function (data){
					if(data.success){
						for(var k=0;k<data.data.length;k++){
						  	for(var p=0;p<count;p++){
						        if(data.data[k]["pId"]==resultData[p]["id"]){
						             var s={};
						             s.name=data.data[k]["name"];
						             s.address=data.data[k]["u"];
						             resultData[p]["data"].push(s);
						        }
							}
						}
					}
				});	
			return resultData;	
}