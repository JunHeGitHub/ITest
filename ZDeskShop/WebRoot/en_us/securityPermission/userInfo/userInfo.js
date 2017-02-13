var YONGHUXINXI_FIND = "/" + window.top.PRJ_PATH + "/" +window.top.ZDesk_ROU+"/UserInfo" + "/"
		+ "Find.action";
var ZKM = "ZKM";		
/**
 * 查询所有用户信息
 */
function getUserList() {
	var url = YONGHUXINXI_FIND;
	var pat = {};
	var rdata = [];
	ajaxFunction(url, pat, false, function(data) {
		rdata = assemblyUserData(data);
	});
	return rdata;
}

/**
 * 拼装数据
 */
function assemblyUserData(data) {
	var rdata = data.users;
	var roleList = data.roles;
	var orgList = data.orgs;
	for (var i = 0; i<rdata.length; i++) {
		var loginName = rdata[i].loginName;
		var roles = '';
		for(var j=0;j<roleList.length;j++){
			if(roleList[j].loginName==loginName){
				roles=roleList[j].roles;
				break;
			}
		}
		var orgs ='';
		for(var k=0;k<orgList.length;k++){
			if(orgList[k].loginName==loginName){
				orgs=orgList[k].orgs;
				break;
			}
		}
			rdata[i].roles = roles;
			rdata[i].orgs = orgs;
	}
	return rdata;
}
// 验证表单
function validationFrom(pat) {
	var flg = false;
	if (pat.loginName != 'undefined' && !U_object_isNotNull(pat.loginName)) {
		showTooltip("chkLoginName",zinglabs.i18n.getText("yonghuguanli_denglumingchengbunengweikong"));
		return flg;
	} else {
		hideTooltip("chkLoginName");
	}
	if (pat.name != 'undefined' && !U_object_isNotNull(pat.name)) {
		showTooltip("chkName",zinglabs.i18n.getText("yonghuguanli_yonghuxingmingbunengweikong"));
		return flg;
	} else {
			hideTooltip("chkName");
	}
	if (pat.phone_number != 'undefined'
			&& !U_object_isNotNull(pat.phone_number)) {
		showTooltip("chkPhone",zinglabs.i18n.getText("yonghuguanli_fenjihaobunengweikong"));
		return flg;
	} else {
		hideTooltip("chkPhone");
	}

	if (pat.pwd != 'undefined' && !U_object_isNotNull(pat.pwd)) {
		showTooltip("chkPwd",zinglabs.i18n.getText("yonghuguanli_mimabunegweikong"));
		return flg;
	} else {
		hideTooltip("chkPwd");
	}
	var data = getUserList();
	for (var i = 0; i < data.length; i++) {
		if (data[i].loginName == pat.loginName) {
			showTooltip("chkLoginName",zinglabs.i18n.getText("yonghuguanli_denglumingchengyicunzai"));
			return flg;
		}
		if (data[i].phone_number == pat.phone_number) {
			showTooltip("chkPhone",zinglabs.i18n.getText("yonghuguanli_fenjihaoyicunzai"));
			return flg;
		}
	}
	hideTooltip("chkLoginName,#chkName,#chkPhone,#chkPwd,#chkRpdw");
	flg = true;
	return flg;
}
// 查询数据
function doSearch() {
	var data = getUserList();
	return data;
}
// 条件查询
function tableSearch() {
	var table = $('#test').DataTable();
	var value = $("#shaixuan").val();
	table.search(value).draw(true);
}
/*
 * function tableSearch(){ var table=$('#test').DataTable();
 * table.columns().search("").draw(); var pat ={};
 * pat=zkm_getHtmlFormJsons("searchWorkOrderForm") var value ="";
 * if(pat.loginName!=''){ value = pat.loginName;
 * table.column(3).search(value).draw(); } if(pat.name!=''){ value = pat.name;
 * table.column(4).search(value).draw(); } if(pat.phone_number!=''){ value =
 * pat.phone_number; table.column(6).search(value).draw(); }
 * if(pat.agent_level!=''){ value = pat.agent_level;
 * table.column(7).search(value).draw(); } if(pat.roleName!=''){ value =
 * pat.roleName; table.column(11).search(value).draw(); }
 * if(pat.companyName!=''){ value = pat.companyName;
 * table.column(12).search(value).draw(); } addHide("#update"); }
 */

// 删除数据
function doDelete(selectData) {
	var str = '';
	var strLoginName = '';
	for (var i in selectData) {
		str = str +'"'+ selectData[i].id +'"'+ ',';
		strLoginName = strLoginName+ '"'+selectData[i].loginName +'"'+ ',';
	}
	var params  ={};
	params.idList=str.substr(0, str.length - 1);
	params.loginNameList=strLoginName.substr(0,strLoginName.length - 1);
	var url = "/"+window.top.PRJ_PATH+"/"+ZKM+"/UserInfo/deleteUserById.action";
	ajaxFunction(url,params, false);
	url = "/"+window.top.PRJ_PATH+"/"+ZKM+"/UserInfo/deleteUserRoleByLoginName.action";
	ajaxFunction(url,params, false);
	url = "/"+window.top.PRJ_PATH+"/"+ZKM+"/UserInfo/deleteUserOrgByLoginName.action";
	ajaxFunction(url,params, false);
	refresh();
	zinglabs.i18n.alert("system_deleteSuccess");
}

// 增加/修改数据
function doUpdateOrSubmit() {
	var pat = zkm_getHtmlFormJsons("updateForm");
	pat.tableName = "suSecurityUser";
	pat.primaryKey = "id";
	pat.primaryKeyValue = pat.id;
	delete pat.roles;
	delete pat.orgs;
	delete pat.roleNameList;
	delete pat.companyTextList;
	if (pat.primaryKeyValue != null && pat.primaryKeyValue != 'undefined'
			&& pat.primaryKeyValue != '') {
		if (updateFromData.loginName != pat.loginName) {
			var data = getUserList();
			if(pat.loginName!=''&&pat.loginName!=null&&pat.loginName!=undefined){
				for (var i = 0; i < data.length; i++) {
					if (data[i].loginName == pat.loginName) {
						zinglabs.alert(zinglabs.i18n.getText("yonghuguanli_denglumingchengyicunzai")+","+ zinglabs.i18n.getText("system_updateFailed"));
						return;
					}
				}
			}else{
				zinglabs.alert(zinglabs.i18n.getText("yonghuguanli_denglumingchengbunengweikong")+","+ zinglabs.i18n.getText("system_updateFailed"));
				return;
			}
		}
		if (updateFromData.phone_number != pat.phone_number) {
			var data = getUserList();
			if(pat.phone_number!=''&&pat.phone_number!=null&&pat.phone_number!=undefined){
				for (var i = 0; i < data.length; i++) {
					if (data[i].phone_number == pat.phone_number) {
						zinglabs.alert(zinglabs.i18n.getText("yonghuguanli_fenjihaoyicunzai")+","+ zinglabs.i18n.getText("system_updateFailed"));
						return;
					}
				}
			}else{
				zinglabs.alert(zinglabs.i18n.getText("yonghuguanli_mimabunegweikong")+","+ zinglabs.i18n.getText("system_updateFailed"));
				return;
			}
		}
		// 修改
		pat.where = pat.primaryKey.id;
		commonUpdate(pat, function(data) {
					if (data) {
						refresh();
						overloadingData();
						zinglabs.i18n.alert("system_updateSuccess");
						return;
					} else {
						zinglabs.i18n.alert("system_updateFailed");
						return;
					}
				});
	} else {
		// 增加
		if (validationFrom(pat)) {
			var date = fomateDate("yyyy-MM-dd hh:mm:ss", new Date());
			pat.createDate = date;
			pat.lastLoginDate = date;
			pat.lastModifyPassword = date;
			if (pat.viewIndex == ""||!V_NumberCheck(pat.viewIndex)) {
				delete pat.viewIndex;
			}
			if (pat.startus == "") {
				delete pat.startus;
			}
			commonInsert(pat, function(data) {
						var json = eval('(' + data + ')');
						if (json.success == true) {
							var loginName = $("#loginName").val();
							var roleIdList = $("#roleIdList").val();
							if (roleIdList != undefined && roleIdList != ''
									&& loginName != undefined
									&& loginName != '') {
								var pat = {};
								pat.tableName = "suSecurityUserRole";
								pat.loginName = loginName;
								var array = roleIdList.split(",");
								for (var i = 0; i < array.length; i++) {
									pat.roleId = array[i];
									commonInsert(pat);
								}
							}
							var companyCodeList = $("#companyCodeList").val();
							if (companyCodeList != undefined
									&& companyCodeList != ''
									&& loginName != undefined
									&& loginName != '') {
								var pat = {};
								pat.tableName = "suSecurityUserOrg";
								pat.primaryKey = "id";
								pat.uuid = 'true';
								pat.loginName = loginName;
								var array = companyCodeList.split(",");
								for (var i = 0; i < array.length; i++) {
									pat.orgCode = array[i];
									commonInsert(pat);
								}
							}
							refresh();
							overloadingData();
							zinglabs.i18n.alert("system_saveSuccess");
							return;
						} else {
							zinglabs.i18n.alert("system_saveFailed");
							return;
						}
					});
		}
	}
}
// 显示信息
function removeHide(data) {
	$(data).removeClass('hide');
}
// 隐藏信息
function addHide(data) {
	$(data).addClass('hide');
}
// 设置提示框
function setPrompts(id, content) {
	$(id).popover({
				placement : 'right',
				trigger : 'manual',
				content : content
			});
}
// 为用户添加修改角色
function addRoleForUser() {
		var loginName = $("#updateForm #loginName").val();
		if(loginName!=undefined&&loginName!=null&&loginName!=''){
			var url = "/"+window.top.PRJ_PATH+"/zh_cn/securityPermission/userInfo/delRoleForUser.html?loginName="+ loginName+'&reFillFn=roleReFillFn';
			closeDialog=roleDialog(url);		
			return;
		}else{
			zinglabs.i18n.alert("yonghuguanli_meiyouzhaodaodenglumingbunengxiugaijuese");
			return;
		}
		
}
function roleReFillFn(pat){
	closeDig();
	zinglabs.i18n.alert("yonghuguanli_yonghuyongyoujuesexiugaichenggong");
	var params = {};
	var roleIdList = pat.roleIdList.split(',');
	var str = '';
	params.tableName = "suSecurityRole";
	params.primaryKey = "id";
	for (var i in roleIdList) {
		params.primaryKeyValue = roleIdList[i];
		params.where = {
			"id" : params.primaryKeyValue
		};
		commonSelect(params,false,function(data){
			str = data.rows[0].name + "," + str;
		})
	}
	var roleNameList = str.substr(0, str.length - 1);
	$("#roles").val(roleNameList);
	overloadingData();
}
// 为用户添加角色
function addRole() {
	var url ="/"+window.top.PRJ_PATH+"/zh_cn/securityPermission/userInfo/getRoleIdList.html?reFillFn=getRoleIdList";
	closeDialog=roleDialog(url);
}
/*
 * 获取角色id集合
 */
function getRoleIdList(roleIdList) {
	var str = '';
	$("#roleIdList").val(roleIdList);
	var params = {};
	params.tableName = "suSecurityRole";
	params.primaryKey = "id";
	for (var i in roleIdList) {
		params.primaryKeyValue = roleIdList[i];
		params.where = {
			"id" : params.primaryKeyValue
		};
		commonSelect(params, false, function(data) {
					str = data.rows[0]['name'] + "," + str;
				});
	}
	var roleNameList = str.substr(0, str.length - 1);
	$("#roleNameList").val(roleNameList);
	closeDig();
}

// 为用户添加组织机构
function addOrgForUser() {
	var loginName = $("#updateForm #loginName").val();
		if(loginName!=undefined&&loginName!=null&&loginName!=''){
			var url = "/"+window.top.PRJ_PATH+"/zh_cn/common/zkmCommonTree.html?user="+ loginName + "&roleName=''&mode=many&recordType=org&type=test1&showButton=y&reFillFn=saveOrgForUser&closeFn=closeDig";
			closeDialog=orgDialog(url);		
			return;
		}else{
			zinglabs.i18n.alert("yonghuguanli_meiyouzhaodaodenglumingbunengxiugaizuzhi");
			return;
		}
}
/*
 * 保存组织
 */
function saveOrgForUser(nodes) {
	var loginName = $("#loginName").val();
	var orgCode;
	var param = {};
	param.tableName = 'suSecurityUserOrg';
	param.primaryKey = 'loginName';
	param.primaryKeyValue = loginName;
	commonDelete(param, function() {
		param.uuid = 'true';
		param.primaryKey = 'id';
		param.loginName = loginName;
		for (var i = 0; i < nodes.length; i++) {
			orgCode = nodes[i];
			param.orgCode = orgCode;
			commonInsert(param);
		}
		zinglabs.alert("用户所属组织,"+ zinglabs.i18n.getText("system_updateSuccess"));
		closeDig();
		overloadingData();
		var str = '';
		for (var i = 0; i < nodes.length; i++) {
			var params = {};
			params.tableName = "commonTree_detail";
			params.primaryKey = "id";
			params.primaryKeyValue = nodes[i];
			params.where = {
			"id" : params.primaryKeyValue
		};
		commonSelect(params, false, function(data) {
			str = data.rows[0].text + "," + str;
		});
		var companyTextList = str.substr(0, str.length - 1);
		$("#orgs").val(companyTextList);
	}		
	});
}
function addOrg() {
	var url ="/"+window.top.PRJ_PATH+"/zh_cn/common/zkmCommonTree.html?recordType=org&type=test&mode=many&showButton=y&reFillFn=getOrgCodeList&closeFn=closeDig";
	closeDialog=orgDialog(url);
}
function getOrgCodeList(orgCodeList) {
	var str = '';
	$("#companyCodeList").val(orgCodeList);
	for (var i = 0; i < orgCodeList.length; i++) {
		var params = {};
		params.tableName = "commonTree_detail";
		params.primaryKey = "id";
		params.primaryKeyValue = orgCodeList[i];
		params.where = {
			"id" : params.primaryKeyValue
		};
		commonSelect(params, false, function(data) {
			str = data.rows[0].text + "," + str;
		});
	}
	var companyTextList = str.substr(0, str.length - 1);
	$("#companyTextList").val(companyTextList);
	closeDig();
	overloadingData();
}

//dialog窗口
function roleDialog(url) {
	var roleDig = dialog({
				title : '角色授权',
				content : '<iframe src=' + url
						+ ' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>',
				width : '745',
				height : '400'
			});
	roleDig.show();
	return roleDig;
}
function orgDialog(url) {
	var orgDig = dialog({
				title : '组织机构',
				content : '<iframe src=' + url
						+ ' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>',
				width : '300',
				height : '400'
			});
	orgDig.show();
	return orgDig;
}
//关闭dialog
function closeDig(){
	window.closeDialog.close().remove();
}
/**
 * 重载数据
 */
function overloadingData(){
	var table=$('#test').DataTable();
     //清空筛选
     table.columns().search("").draw();
	 clearForm("searchWorkOrderForm");
     //清空数据
     table.clear().draw();
     //重新加载数据
     var data=doSearch(); 
     table.rows.add(data).draw(true);
}
/**
 * 滚定数据
 */
function scroll2data(id) {
	$("html,body").animate({
				scrollTop : $("#" + id).offset().top
			}, 1000);
}
/**
 * 收起数据
 */
function shou() {
	var anim =scroll2data("t");
}
//验证未通过样式
function showTooltip(id,title){
	$("#"+id).attr('title',title) ;
	$("#"+id).tooltip({
		placement:'right'
	}) ;
	$("#"+id).tooltip('show') ;
	$("#"+id).addClass('error') ;
}
//还原样式
function hideTooltip(id){
	$("#"+id).tooltip('destroy') ;
	$("#"+id).removeClass('error') ;
}
