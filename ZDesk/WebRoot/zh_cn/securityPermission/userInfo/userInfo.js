var isLoadData = false;
//禁用回退按键
disableBack();// 项目名称
var APP_PROJECT_NAME = window.top.PRJ_PATH || "ZDesk";
// 模块
var APP_MODULES_NAME = window.top.ZDesk_ROU || "ZDesk";

var ZKM_DBID="WXSDK";
// 遮罩属性
var maskContent = {
	css : {
		border : 'none',
		padding : '15px',
		backgroundColor : '#000',
		'-webkit-border-radius' : '10px',
		'-moz-border-radius' : '10px',
		opacity : .5,
		color : '#fff',
		font : '14px'
	},
	message : '<img src="../../../img/wait.gif" border="0" />  正在加载中，请稍候...'
};
var userInfoCurdStatus = "false";
var fullName = "suSecurityUser";
var rowSelTr = false;
var isDocker =false; //是否为docker模式，ture是。
var dockerCompanyId ="" ;//docker模式下的companyId
$(document).ready(function() {
			cleanSearchForm();
			//$.blockUI(maskContent);
			$('#userInfoGrid').DataTable({
						// 滚动条
						// scrollY: 500,
						scrollX: '100%',
						// 延迟加载
						deferRender : true,
						processing : false,
						// 开启服务端分页
						serverSide : true,
						// 服务器端分页ajax请求 可抽离出一个方法
						ajax : doSearch,
						pagingType : "full_numbers",
						pageLength : 10,
						bPaginate : true, // 翻页功能
						bLengthChange : true, // 改变每页显示数据数量
						bFilter : true, // 过滤功能
						bSort : true, // 排序功能
						bInfo : true,// 页脚信息
						bAutoWidth : false, // 自动宽度
						sPaginationType : "bootstrap",// 分页样式
						dom : 'TRlrtip',
						columns : [{
									title : "序号",
									data : "DT_RowNumber",
									defaultContent : '',
									orderable : false
								}, {
									"title" : "登录名",
									data : 'loginName',
									className : "my_class"
								}, {
									"title" : "姓名",
									data : 'name'
								}, {
									"title" : "密码",
									data : 'pwd'
								}, {
									"title" : "工号",
									data : 'job_number'
								}, {
									"title" : "分机号",
									data : 'phone_number'
								}, {
									"title" : "等级",
									data : 'agent_level'
								}, {
									"title" : "附加信息",
									data : 'agent_information'
								}, {
									"title" : "用户状态",
									data : 'startus'
								}, {
									"title" : "用户角色",
									data : 'roles'
								}, {
									"title" : "所属组织",
									data : 'orgs'
								},{
									"title" : "用户角色编号",
									data : 'roleIds',
									visible:false
								}, {
									"title" : "所属组织编号",
									data : 'orgIds',
									visible:false
								}, {
									"title" : "公司标识",
									data : 'companyId'
								}, {
									"title" : "公司名称",
									data : 'companyName'
								}, {
									"title" : "部门标识",
									data : 'departmentId'
								}, {
									"title" : "部门名称",
									data : 'departmentName'
								}, {
									"title" : "坐席号",
									data : 'seat_number'
								}, {
									"title" : "排序字段",
									data : 'viewIndex',
									visible : false
								},{
									"title" : "性别",
									data : 'alias3'
								},
								{
									"title" : "建立时间",
									data : 'createDate',
									visible : false
								}, {
									"title" : "最后登录时间",
									data : 'lastLoginDate',
									visible : false
								}, {
									"title" : "最后修改密码时间",
									data : 'lastModifyPassword',
									visible : false
								}],
						columnDefs : [{
									"searchable" : false,
									"orderable" : false,
									"targets" : 0
								}],

						oLanguage : {
							infoFiltered : function() {

							},
							"sLengthMenu" : "每页显示 _MENU_条",
							"sZeroRecords" : "没有找到符合条件的数据",
							"sProcessing" : "&lt;imgsrc=’./loading.gif’ /&gt;",
							"sInfo" : "当前第 _START_ - _END_ 条　共计 _TOTAL_ 条",
							"sInfoEmpty" : "没有记录",
							"sInfoFiltered" : "(从 _MAX_ 条记录中过滤)",
							"sSearch" : "搜索：",
							"oPaginate" : {
								"sFirst" : "首页",
								"sPrevious" : "前一页",
								"sNext" : "后一页",
								"sLast" : "尾页"
							}
						},
						tableTools : {
							fnRowSelected : function(nodes) {
								var index = nodes[0]._DT_RowIndex;
								rowSelTr = index;
								var data = table.row(index).data();
								openEdit(data);
							},
							sRowSelect : 'os',

							aButtons : []
						}
					});

			// 自定义列插件绑定
			// 绑定事件 on
			var table = $('#userInfoGrid').DataTable();
			initValidate();
			$.unblockUI();
			//excel导出

			$("#excelExportBtn").on("click",function(){
			var excelAttr=zkm_getHtmlFormJsons("userInfoSearchForm");
				excelAttr.nameSpace="com.zinglabs.apps.suPermission.excelUser";
				excelAttr.beanId = "suSecurityUserFilter";
				excelAttr.fileName="用户信息表.xls";
				excelAttr.fTitle="用户信息表";
				excelAttr.attrNames="编号@登录名@姓名@密码@工号@分机号@等级@附加信息@用户状态@用户角色@所属组织@公司标识@公司名称@部门标识@部门名称@坐席号@排序字段@建立时间@最后登录时间@最后修改密码时间";
				excelAttr.dbid="ZDesk";
				z_exportExcel(excelAttr,function(){});
			});
			//初始化是否需要验证用户存在于指定表配置标识
//			try{
//				var par ={};
//			    par.productionId="MH_ZDeskUserCompany";
//			  	var Pro=getproductionId(par);
//			   	IsCheckUserInMH=Pro[0].peizhiItemValue;
//			}catch(e){
//			logErr("Exception getproductionId need see log " + e.name + ": "
//				+ e.message);
//			}

	   /**
		*	后端判断是否为docker模式
		*	docker模式下影响添加用户
		*/
		try{
			var isDockerParam={} ;
			var isDockerUrl = "/" + APP_PROJECT_NAME + "/" + APP_MODULES_NAME+"/MH_ZDeskUserCompany" + "/" + "isDocker.action";
			var loginName = getUserInfo().loginName;
			isDockerParam.loginName = loginName;
			isDockerParam.idbid= ZKM_DBID ;
			ajaxFunction(isDockerUrl, isDockerParam, true,function(dataIsDocker){
           		if(dataIsDocker.success){
           			if(dataIsDocker.data.companyId!=undefined&&dataIsDocker.data.companyId!=null&&dataIsDocker.data.companyId!=""){
           				dockerCompanyId = dataIsDocker.data.companyId ;
           				isDocker=true ;
           				logErr("docker type is true:isDocker = true") ;
           			}
           		}
            });
       }catch(e){
       		logErr("Exception userInfo.js :exception for search isDocker , isDocker=false(default) !");
       }
           
});

/*******************************************************************************
 * @TODO 通用，增删改查
 */

function doAdd(fn) {
	var bool = $("#userInfoAddForm").validate().form();
	if (bool) {
		$.blockUI(maskContent);
		
		//判断是否为docker模式
		
		
		var params = {};
		var cValues = zkm_getHtmlFormJsons("userInfoAddForm");
        console.log(cValues);
		var roleIds = cValues.roleIds;
		var orgIds = cValues.orgIds;

		var date = fomateDate("yyyy-MM-dd hh:mm:ss", new Date());
		cValues.createDate = date;
		cValues.lastLoginDate = date;
		cValues.lastModifyPassword = date;
	
		//删除对象中属性
		delete cValues.roles;
		delete cValues.roleIds;
		delete cValues.orgs;
		delete cValues.orgIds;
		params.columnValues = cValues;
		params.tableName = fullName;
		// 添加用户
		commonInsertOrUpdate(params, true, function(data) {
					
					if(!data.success){
						showError(zinglabs.i18n.getText("system_saveFailed"));
						$.unblockUI();
						return;
					}
					//添加成功后，增加回调方法
					if (fn && typeof(fn) == 'function') {
						fn(data);
					}
					if (roleIds != "") {
						// 添加角色
						addRoles(cValues.loginName,roleIds)
					}
					if (orgIds != "") {
						// 添加组织机构
						addOrgs(cValues.loginName,orgIds)
					}

					// zinglabs.i18n.alert("system_saveSuccess");
					userInfoCurdStatus = "add";
					doCleanAddForm();
					$.unblockUI();
					refresh();
					showSuccess(zinglabs.i18n.getText("system_saveSuccess"));
					$("#userInfoAdd").hide();
				});
		// 添加成功 数据清理状态
		userInfoCurdStatus = false;
	} else {
		// 添加失败据需添加
		userInfoCurdStatus = "add";
	}
}


//docker模式下，新增用户
function  dockerDoAdd(){
	$.blockUI(maskContent);
	try{
	
		//验证用户是否重复
		var Check_MH_ZDeskUserCompany_url = "/" + APP_PROJECT_NAME + "/" + APP_MODULES_NAME+"/MH_ZDeskUserCompany" + "/" + "CheckUserIsHaveMH.action";
		//新增用户到MH_ZDeskUserCompany
		var Add_MH_ZDeskUserCompany_url = "/" + APP_PROJECT_NAME + "/" + APP_MODULES_NAME+"/MH_ZDeskUserCompany" + "/" + "AddUserOfMH.action";
		
		var LoginNameAdd =$("#loginName_add").val();
		var bool = $("#userInfoAddForm").validate().form(); //表单验证
		if (!bool) {
			$.unblockUI();
			return;
		}
		
		var param = {} ;
		param.loginName = LoginNameAdd ;
		param.companyId = dockerCompanyId ;
		param.idbid= ZKM_DBID ;
        ajaxFunction(Check_MH_ZDeskUserCompany_url, param, true,function(data){
         	if(data.success){
         		//新增用户到MH_ZDeskUserCompany
         		doAdd(function(data){
         			ajaxFunction(Add_MH_ZDeskUserCompany_url, param, true,function(dataAdd){
           				if(!dataAdd.success){
           					showError() ;
        					logErr("Exception CheckUserInMHDo need see log :insert MH_ZDeskUserCompany error");
           				};
					});
         		});
			}else{
         		zinglabs.alert("登录名已存在,请更改");
        	}
        $.unblockUI();
       	});
	}catch(e){
		$.unblockUI();
		logErr("Exception CheckUserInMHDo :"+ e.message);
	}
}

//添加角色
function addRoles(loginName,roleIds){
	var pat = {};
	var rolePat = new Array();
	var roleArray = roleIds.split(",");
	for (var i = 0; i < roleArray.length; i++) {
		rolePat.push({
			"loginName" : loginName,
			"roleId" : roleArray[i]
		});
	}
	pat.tableName = "suSecurityUserRole";
	pat.columnValues = rolePat;
	commonInsertOrUpdate(pat, true, function(data) {});
}
//添加组织
function addOrgs(loginName,orgIds){
	var pat = {};
	var orgPat = new Array();
	var orgArray = orgIds.split(",");
	for (var i = 0; i < orgArray.length; i++) {
		orgPat.push({
			"loginName" : loginName,
			"orgCode" : orgArray[i]
		});
	}
	pat.tableName = "suSecurityUserOrg";
	pat.columnValues = orgPat;
	commonInsertOrUpdate(pat, true, function(data) {});
}

function doEdit() {
	var bool = $("#userInfoEditForm").validate().form();
	if (bool) {
		$.blockUI(maskContent);
		var params = {};
		var cValues = zkm_getHtmlFormJsons("userInfoEditForm");
		//删除角色和组织
		doDeleteRoleAndOrg(cValues.loginName);
		// 主键的值
		params.columnValues = cValues;
		params.tableName = fullName;
		var orgIds = cValues.orgIds;
		var roleIds = cValues.roleIds;
		var orgs =  cValues.orgs;
		var roles =  cValues.roles;
		
		//删除对象中属性
		delete cValues.roles
		delete cValues.roleIds;
		delete cValues.orgs;
		delete cValues.orgIds;
		delete cValues.createDate;
		delete cValues.lastLoginDate;
		delete cValues.lastModifyPassword;
		
		// 如更改不是id 需定义 params.primarykey
		commonUpdate(params, true, function(data) {
			try {
				var tableTmp = $("#userInfoGrid").dataTable();
				if (data.success) {
					try {
						if (roleIds != "") {
							// 添加角色
							addRoles(cValues.loginName,roleIds)
						}
						if (orgIds != "") {
							// 添加组织机构
							addOrgs(cValues.loginName,orgIds)
						}
						refresh();
						showSuccess(zinglabs.i18n.getText("system_updateSuccess"));
						userInfoCurdStatus = "readOnly";
						setFormReadOnly('userInfoEditForm');
						$("#submitBtn").text('编辑');
						$("#userInfoEdit").hide();
					} catch (e) {
						showError("异常！");
						logErr("Exception need see log " + e.name+ ": " + e.message);
					}
				} else {
					logErr("Exception edit failed  " + data + " "+ ("Items" in data));
						showError(zinglabs.i18n.getText("system_updateFailed"));
					}
			} catch (ex) {
				showError("异常！");
				logErr("doEdit 2222 Excep " + ex.name + ": "+ ex.message);
			}
					
		});
		$.unblockUI();
	}
}

function doSearch(data, callback, settings) {
	if(isLoadData){
		$.blockUI(maskContent);
		//条件查询
		var searchForm = zkm_getHtmlFormJsons("userInfoSearchForm");
			searchForm.rows = settings._iDisplayLength;
			searchForm.offset = settings._iDisplayStart;
		if(searchForm.loginName.length>0 || searchForm.name.length>0 || searchForm.orgIds.length>0 || searchForm.roleIds.length>0){
			var url =  "/"+APP_PROJECT_NAME+"/"+APP_MODULES_NAME+"/UserInfo/searchUser.action";  
			ajaxFunction(url,searchForm,true,function(data){
				doSearchProcessing(data,callback,settings);
				$.unblockUI();
			});
			return;
		}
		
		//普通查询
		var params = {};
		params.tableName = fullName;
		params.rows = settings._iDisplayLength;
		params.offset = settings._iDisplayStart;
		
		commonSelect(params, true, function(data) {
			doSearchProcessing(data,callback,settings);
			$.unblockUI();
		});
	}
};
//处理查询
function doSearchProcessing(data,callback,settings){
	try {
		if (data && data.data) {
			var pdata = {};
			var size = data.data.length;
			var loginNameList = "";
			for (var i = 0; i < size; i++) {
				data.data[i].DT_RowNumber = i + 1;
				loginNameList = "'" + data.data[i].loginName + "',"+ loginNameList;
			}
			if (loginNameList != '') {
				// 角色处理
				var nameSpace = "com.zinglabs.apps.suPermission.getUserRoleByLoginName";
				var nameSpace1 = "com.zinglabs.apps.suPermission.getRoleNameById";
				var columns1 = "roleId";
				var columns2 = "name";
				var columns3 = "id";
				var roleList = processingData(loginNameList,nameSpace, nameSpace1, columns1, columns2, columns3);
				// 组织机构处理
				nameSpace = "com.zinglabs.apps.suPermission.getUserOrgByLoginName";
				nameSpace1 = "com.zinglabs.apps.suPermission.getOrgNameById";
				columns1 = "orgCode";
				columns2 = "text";
				columns3 = "id";
				var orgList = processingData(loginNameList,nameSpace, nameSpace1, columns1, columns2, columns3);
				// 为data.data加入 roles与orgs
				for (var i = 0; i < size; i++) {
					var loginName = data.data[i].loginName;
					if(roleList[loginName]==undefined){
						roleList[loginName] = {"ids":"","nameStr":""};
					}
					if(orgList[loginName]==undefined){
						orgList[loginName] = {"ids":"","nameStr":""};
					}
					data.data[i].roleIds = roleList[loginName].ids;
					data.data[i].orgIds = orgList[loginName].ids;
					data.data[i].roles = roleList[loginName].nameStr;
					data.data[i].orgs = orgList[loginName].nameStr;
				}
			}
			// 数据
			pdata.data = data.data;
			// 总条数
			pdata.recordsTotal = data.total || 0;
			// 过滤没有使用到总条数
			pdata.recordsFiltered = data.total || 0;
			// 代表第几次画页面 如pdata.draw 不能删 如删掉不会翻页
			pdata.draw = settings.iDraw;
			callback(pdata);
		}
	} catch (ex) {
		logErr("查询失败 failed " + ex.name + ": " + ex.message);
	}
}
// 处理数据
function processingData(loginNameList, nameSpace,nameSpace1,columns1,columns2, columns3) {
	// 处理
	loginNameList = loginNameList.substr(0, loginNameList.length - 1);
	var tempData = {};
	var loginNameArr = new Array();
	var idList = "";
	var pat = {};
	pat.url = "/" + APP_PROJECT_NAME + "/" + APP_MODULES_NAME
			+ "/appCommonsCurd" + "/" + "Search.action";
	pat.nameSpace = nameSpace;;
	pat.beanId = "suSecurityUserFilter";
	pat.loginNameList = loginNameList;
	commonCURD(pat, false, function(data) {
				var loginName = "";
				for (var i = 0; i < data.data.length; i++) {
					idList = "'" + data.data[i][columns1] + "'," + idList;
					if (loginName != data.data[i].loginName) {
						loginName = data.data[i].loginName;
						loginNameArr.push(loginName);
						tempData[loginName] = data.data[i][columns1] + ",";
					} else {
						tempData[loginName] = tempData[loginName]+ data.data[i][columns1] + ",";
					}
				}
				idList = idList.substr(0, idList.length - 1);
			});
	var rdata = {}
	var param = {};
	if (idList != '') {
		param.url = "/" + APP_PROJECT_NAME + "/" + APP_MODULES_NAME+ "/appCommonsCurd" + "/" + "Search.action";
		param.nameSpace = nameSpace1;
		param.beanId = "suSecurityUserFilter";
		param.idList = idList;
		commonCURD(param, false, function(data) {
					for (var i = 0; i < loginNameArr.length; i++) {
						var loginName = loginNameArr[i];
						var idList = tempData[loginName].substr(0,
								tempData[loginName].length - 1);
						var idStr = "";
						var nameStr = "";
						var idArr = idList.split(',');
						for (var j = 0; j < idArr.length; j++) {
							for (var k = 0; k < data.data.length; k++) {
								if (idArr[j] == data.data[k][columns3]) {
									idStr = data.data[k][columns3] +","+idStr;
									nameStr = data.data[k][columns2] + ","+ nameStr;
								}
							}
						}
						idStr = idStr.substr(0, idStr.length - 1);
						nameStr = nameStr.substr(0, nameStr.length - 1);
						rdata[loginName] = {"ids":idStr,"nameStr":nameStr};
					}
				});
	}
	return rdata;
}

function doDelete() {
	var tableTmp = $('#userInfoGrid').DataTable();
	var cell = tableTmp.rows(".active").data();
	if(cell.length == 0){
		showSuccess(zinglabs.i18n.getText("system_selectDataForDelete"))
		return;
	}
	zinglabs.confirm(zinglabs.i18n.getText("system_deleteConfirmation"), function() {
				var params = {};
				$.blockUI(maskContent);
				params.tableName = fullName;
				// 主键默认为id 如不为id 加上params.primarykey
				var ids = "";
				var loginNames = "";
				for (var i = 0; i < cell.length; i++) {
					var key = "id";
					if ("primarykey" in params) {
						key = params["primarykey"];
					}
					ids += cell[i][key] + ",";
					loginNames +=cell[i].loginName + ",";
				}
				params.columnValues = ids;
				commonDelete(params, true, function(data) {});
				loginNames = loginNames.substr(0, loginNames.length - 1);
				doDeleteRoleAndOrg(loginNames);
				tableTmp.row('.selected').remove().draw();
				$.unblockUI();
				showSuccess(zinglabs.i18n.getText("system_deleteSuccess"));
				$("#userInfoAdd,#userInfoEdit").hide();
				refresh();
			});
};
// 删除角色、组织
function doDeleteRoleAndOrg(loginNames) {
	//处理loginName
	var loginNameArr = loginNames.split(',');
	loginNames = "";
	for(var i in loginNameArr){
		loginNames += "'" + loginNameArr[i] + "',";
	}
	loginNames = loginNames.substr(0, loginNames.length - 1);
	// 删除角色
	var pat = {};
	pat.tableName = "suSecurityUserRole";
	pat.url = "/" + APP_PROJECT_NAME + "/" + APP_MODULES_NAME+ "/appCommonsCurd" + "/" + "Delete.action";;
	pat.loginNameList = loginNames;
	pat.nameSpace = "com.zinglabs.apps.suPermission.deleteOrgOrRoleByLoginName";
	pat.beanId = "suSecurityUserFilter";
	commonCURD(pat, true, function(data) {});

	// 删除组织
	pat.tableName = "suSecurityUserOrg";
	pat.nameSpace = "com.zinglabs.apps.suPermission.deleteOrgOrRoleByLoginName";
	commonCURD(pat,true, function(data) {});
	refresh();
}

/*******************************************************************************
 * 
 * @desc 指定panel赋值 和清空
 * @todo 只测试了主要类型 , checkbox、radio没测试
 * 
 * pVars.id panel id pVars.data 赋值数据 pVars.isClear 字符串 "true" 清空此panel
 * 
 * 
 * @TODO 通用，调试完成后分文件，不要加Velocity代码
 * 
 */
function setPanelVal(pVars) {
	$('#' + pVars.id + ' :input').each(function(index, obj) {
		var type = obj.type;
		var tag = obj.tagName.toLowerCase();
		if (("isClear" in pVars) && pVars.isClear == 'true') {
			if (type == 'text' || type == 'password' || tag == 'textarea'
					|| type == 'hidden')
				$(obj).val('');
			else if (type == 'checkbox' || type == 'radio')
				$(obj).attr('checked', false);
			else if (tag == 'select')
				$(obj).attr('selectedIndex', -1);
		} else {
			var vTmp = pVars.data[this.name];
			if (BIsNullVal(vTmp)) {
				vTmp = '';
			}
			if (type == 'text' || type == 'password' || type == 'hidden'
					|| tag == 'textarea')
				$(obj).val(vTmp);
			else if (type == 'checkbox' || type == 'radio') {
				$(obj).attr('checked', vTmp == '' ? false : vTmp);
			} else if (tag == 'select') {
				$(obj).attr('selectedIndex', vTmp == '' ? -1 : vTmp);
			}
		}
	});
}
// 每页显示条数
function setPageNum(obj) {
	var table = $('#userInfoGrid').DataTable();
	table.page.len(obj.value).draw();
}
// 刷新
function refresh() {
	$("#userInfoSearchForm")[0].reset();
	Search();
}

/*******************************************************************************
 * @TODO 通用，调试完成后分文件，不要加Velocity代码
 */
function BIsNullVal(value) {
	return typeof(value) == "undefined"
			|| value == null
			|| (typeof(value) == 'string' && (value == "undefined"
					|| value == '' || value == 'null'))
			|| (typeof(value) == 'boolean' && value == false);
};

/*******************************************************************************
 * 必须处理的严重错误，测试阶段可以alert 生产环境特殊标识写入日志 发现就要分析原因
 * 
 * @param err
 */
function logErr(errMsg) {
	log.debug(errMsg);
	// alert(errMsg);
	// PT().log.error(errMsg);
};

/*******************************************************************************
 * 嵌入ifram可以返回top parent 非嵌入 可以返回 window ...
 * 
 * @returns {Window}
 * @constructor
 */
function PT() {
	return window;
};
// 初始化验证组件
// fromid 表单ID
function initValidate() {
	$("#" + "userInfoAddForm").validate({
				rules : {
									loginName : {
						required : true,
						validateByStringNotHaveComma:true,
						validateInputType:true,
						validateStringIsExist:["登录名",fullName,"loginName","add"]
						
					},
					name:{
						required : true,
						validateInputType:true
					},
					pwd:{
						required : true,
						validateInputType:true
					},
					phone_number:{
						required : true,
						validateStringIsExist:["分机号",fullName,"phone_number","add"]
					},
					startus:{
						required : true,
						digits:true
					},
					viewIndex:{
						required : true,
						digits:true
					}
				},
				// 错误信息处理方法
				errorPlacement : function(error, element) {
					// 销毁之间的提示信息
					element.tooltip("destroy");
					element.addClass('error');
					var errorInfo = error.html();
					element.tooltip({
								title : errorInfo,
								placement : "right"
							});
					element.tooltip("show");
				},
				// 成功信息处理方法
				success : function($e, dom) {
					var element = $(dom);
					element.tooltip('hide');
					element.removeClass('error');
				},
				onfocusout:false,
				onkeyup:false
			});

	$("#" + "userInfoEditForm").validate({
				rules : {
					loginName : {
						required : true,
						validateInputType:true
					},
					name:{
						required : true,
						validateInputType:true
					},
					pwd:{
						required : true,
						validateInputType:true
					},
					phone_number:{
						required : true,
						validateStringIsExist:["分机号",fullName,"phone_number","edit","userInfoEditForm","loginName"]
					},
					startus:{
						required : true,
						digits:true
					},
					viewIndex:{
						required : true,
						digits:true
					}
				},
				// 错误信息处理方法
				errorPlacement : function(error, element) {
					// 销毁之间的提示信息
					element.tooltip("destroy");
					element.addClass('error');
					var errorInfo = error.html();
					element.tooltip({
								trigger : "submit",
								title : errorInfo,
								placement : "right"
							});
					element.tooltip("show");
				},
				// 成功信息处理方法
				success : function($e, dom) {
					var element = $(dom);
					element.tooltip('hide');
					element.removeClass('error');
				},
				onfocusout:false,
				onkeyup:false
			});

	$("#" + "userInfoSearchForm").validate({
				rules : {

	}			,
				// 错误信息处理方法
				errorPlacement : function(error, element) {
					// 销毁之间的提示信息
					element.tooltip("destroy");
					element.addClass('error');
					var errorInfo = error.html();
					element.tooltip({
								trigger : "submit",
								title : errorInfo,
								placement : "right"
							});
					element.tooltip("show");
				},
				// 成功信息处理方法
				success : function($e, dom) {
					var element = $(dom);
					element.tooltip('hide');
					element.removeClass('error');
				},
				onfocusout:false,
				onkeyup:false
			});

}

// 打开提交表单
function openAdd() {
	$("#userInfoEdit").hide();
	$("#userInfoAdd").show();
	doCleanAddForm();

}
function Search() {
	isLoadData = true;
	var tableTmp = $("#userInfoGrid").DataTable();
	tableTmp.draw();
}
// 打开提交表单
function openEdit(data) {
	$("#userInfoEdit").show();
	$("#userInfoAdd").hide();
	$("#submitBtn").text('编辑');
	userInfoCurdStatus = "readOnly";
	setFormReadOnly('userInfoEditForm');
	var pvars = {
		"id" : "userInfoEdit",
		"data" : data
	};
	setPanelVal(pvars);

}
// 提交方法
function doSubmit() {
	try {
		if (userInfoCurdStatus == "add") {
			//判断是否为isDocker模式进行用户新增验证
			if(isDocker==true){
				dockerDoAdd();
			}else{
				doAdd();
			};
		}else if(userInfoCurdStatus == "readOnly"){
			//解除只读
			setFormWirte("userInfoEditForm");
			userInfoCurdStatus="edit";
			$("#submitBtn").text('提交');
		}else if (userInfoCurdStatus == "edit") {
			doEdit();
		}
	} catch (e) {
		// 编辑、增加页面状态清理。一定要确保页面数据已经不再使用。
		zinglabs.alert("出现异常");
		userInfoCurdStatus = "false";
		logErr("Exception BtnAddSubmit need see log " + e.name + ": "
				+ e.message);
	}

}


// 显示错误信息
function showError(mgs) {
	zinglabs.alert(mgs);

}
// 显示成功信息
function showSuccess(mgs) {
	zinglabs.alert(mgs);
}
// 清空添加表单
function doCleanAddForm() {
	userInfoCurdStatus = "add";
	$("#submitBtn").text('提交');
	var pvars = {
		"id" : "userInfoAdd",
		"isClear" : "true"
	};
	setPanelVal(pvars);
}

function cleanSearchForm() {
	$("#userInfoSearchForm")[0].reset();
}

// 添加组织机构
var closeDialog;
var formName = "";
var formType = "";
function addOrEditOrg(type) {
	var url = "";
	formType = type;
	if(type == "search"){
		formName = "userInfoSearchForm";
		url = "/"+ window.top.PRJ_PATH+ "/zh_cn/common/zkmCommonTree.html?recordType=org&type=test&mode=many&showButton=y&reFillFn=getOrgCodeList";
	}else if(type == "add"){
		formName = "userInfoAdd";
		url = "/"+ window.top.PRJ_PATH+ "/zh_cn/common/zkmCommonTree.html?recordType=org&type=test&mode=many&showButton=y&reFillFn=getOrgCodeList";
	}else if(type == "edit"){
		formName = "userInfoEdit";
		var loginName = $("#userInfoEditForm #loginName_edit").val();
		var url = "/"+window.top.PRJ_PATH+"/zh_cn/common/zkmCommonTree.html?user="+ loginName + "&roleName=''&mode=many&recordType=org&type=test1&showButton=y&reFillFn=getOrgCodeList&closeFn=getOrgCodeList";
	}else{
		showError("type类型不正确!应为serch或add或edit");
		return;
	}
	closeDialog = orgDialog(url);
}
//为用户添加所属机构
function getOrgCodeList(orgCodeList,orgNameList) {
	for (var i = 0; i < orgNameList.length; i++) {
		if (orgNameList[i] == '组织机构树') {
			zinglabs.alert("不能选择(组织机构树)节点");
			return;
		}
	}
	$("#"+formName+" #orgIds").val(orgCodeList);
	$("#"+formName+" #orgs_"+formType).val(orgNameList);
	closeDig();
}
// 为用户添加角色
function addOrEditRole(type) {
	var url ="";
	formType = type;
	if(type == "search"){
		formName = "userInfoSearchForm";
		url = "/"+ window.top.PRJ_PATH+ "/zh_cn/securityPermission/userInfo/getRoleIdList.html?reFillFn=getRoleIdList";
	}else if(type == "add"){
		formName = "userInfoAdd";
		url = "/"+ window.top.PRJ_PATH+ "/zh_cn/securityPermission/userInfo/getRoleIdList.html?reFillFn=getRoleIdList";
	}else if(type == "edit"){
		formName = "userInfoEdit";
		var loginName = $("#userInfoEditForm #loginName_edit").val();
		url = "/"+window.top.PRJ_PATH+"/zh_cn/securityPermission/userInfo/getRoleIdList.html?loginName="+ loginName+'&reFillFn=getRoleIdList';
	}else{
		showError("type类型不正确!应为search或add或edit");
		return;
	}
	closeDialog = roleDialog(url);
}
/*
 * 获取角色id集合
 */
function getRoleIdList(roleIdList,roleNameList) {
	$("#"+formName+" #roleIds").val(roleIdList);
	$("#"+formName+" #roles_"+formType).val(roleNameList);
	closeDig();
}

// dialog窗口
function roleDialog(url) {
	var roleDig = dialog({
		title : '角色授权',
		content : '<iframe src='
				+ url
				+ ' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>',
		width : '740',
		height : '280'
	});
	roleDig.showModal();
	return roleDig;
}
function orgDialog(url) {
	var orgDig = dialog({
		title : '组织机构',
		content : '<iframe src='
				+ url
				+ ' style="border: 0; width: 100%; height: 100%"  frameborder="0"/>',
		width : '300',
		height : '280'
	});
	orgDig.showModal();
	return orgDig;
}
// 关闭dialog
function closeDig() {
	window.closeDialog.close().remove();
}
//设置表单只读
function setFormReadOnly(id){
	$("#"+id+" *").attr("disabled","disabled");
}
//解除只读
function setFormWirte(id){
	$("#"+id+" *").removeAttr("disabled");
}
