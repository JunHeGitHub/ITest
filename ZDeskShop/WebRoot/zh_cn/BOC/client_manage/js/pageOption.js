jQuery.commonOption = (function() {
	var obj=new Object(); 
	// 遮罩属性
	obj.maskContent = {
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
		message : '<img src="../../img/wait.gif" border="0" />  正在加载中，请稍候...'
	};
	obj.pageOptionCurdStatus = "false";
	obj.tableName = "Z_Management";
	obj.rowSelTr = false;
	obj.pageParamSetting = {};
	obj.dataTable = false;
	obj.init = function(setting) {
		obj.pageParamSetting = setting;
		try {
			if (obj.BIsNullVal(obj.pageParamSetting.dataTableId)) {
				alert("datatable id未定义");
				return;
			}

			var dataTableInitParam = {
				// 滚动条
				// 延迟加载
				deferRender : true,
				processing : false,
				// 开启服务端分页
				serverSide : true,
				ajax : obj.doSearch,
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
				columnDefs : [ {
					"searchable" : false,
					"orderable" : false,
					"targets" : 0
				} ],

				oLanguage : {
					infoFiltered : function() {

					},
					"sLengthMenu" : "每页显示 _MENU_条",
					"sZeroRecords" : "没有找到符合条件的数据",
					"sProcessing" : "&lt;imgsrc=’../..img/wait.gif’ /&gt;",
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
						obj.rowSelTr = index;
						var data = obj.dataTable.api().row(index).data();
						obj.openEdit(data);
					},
					sRowSelect : 'multi',
					aButtons : []
				}
			};
			$.blockUI(obj.maskContent);
			if (!obj.BIsNullVal(obj.pageParamSetting.dataTableSetting)) {
				dataTableInitParam = $.extend({}, dataTableInitParam,obj.pageParamSetting.dataTableSetting);
			}
			obj.dataTable = $('#' + obj.pageParamSetting.dataTableId).dataTable(dataTableInitParam);
			// 自定义列插件绑定
			// 绑定事件 on
			if (!obj.BIsNullVal(obj.pageParamSetting.addFormId)) {
				obj.initValidate(obj.pageParamSetting.addFormId);
			}
			if (!obj.BIsNullVal(obj.pageParamSetting.editFormId)) {
				obj.initValidate(obj.pageParamSetting.editFormId);
			}
			$.unblockUI();
		} catch (e) {
			$.unblockUI();
			return 'init error.';
		}
		return obj;
	}
	/**
	 * ids:需要影藏的dom的id，以“,”分隔如:"keySubmit,keyDelete"
	 * type:调用此函数的功能，查询操作type=search，更新操作type=update，删除操作type=delete，增加操作type=add
	 */
	obj.hideDom = function(ids,type){
		if (obj.BIsNullVal(ids)) {
			return;
		}else{
			var idArray = ids.split(",");
			for(var i = 0;i<idArray.length;i++){
				$("#"+idArray[i]).hide();
			}
		}
	}
	/**
	 * ids:需要显示的dom的id，以“,”分隔如:"keySubmit,keyDelete"
	 * type:调用此函数的功能，查询操作type=search，更新操作type=update，删除操作type=delete，增加操作type=add
	 */
	obj.showDom = function(ids,type){
		if (obj.BIsNullVal(ids)) {
			return;
		}else{
			var idArray = ids.split(",");
			for(var i = 0;i<idArray.length;i++){
				$("#"+idArray[i]).show();
			}
		}
	}
	obj.showHideDom = function(showIds,hideIds){
		if (!obj.BIsNullVal(showIds)) {
			var idArray = showIds.split(",");
			for(var i = 0;i<idArray.length;i++){
				$("#"+idArray[i]).show();
			}
		}
		if (!obj.BIsNullVal(hideIds)) {
			var idArray = hideIds.split(",");
			for(var i = 0;i<idArray.length;i++){
				$("#"+idArray[i]).hide();
			}
		}
	}
	obj.doSearch = function(data, callback,settings) {
		if (obj.BIsNullVal(obj.pageParamSetting.serchFormId)) {
			alert("searchForm id为空！");
			return;
		}
		// 隐藏dom信息
		if (!obj.BIsNullVal(obj.pageParamSetting.searchHide)) {
			obj.hideDom(obj.pageParamSetting.searchHide,"search");
		}
		// 显示dom信息
		if(!obj.BIsNullVal(obj.pageParamSetting.searchShow)){
			obj.showDom(obj.pageParamSetting.searchShow,"search");
		}
		
		var params = spellSelectParams(obj.pageParamSetting.serchFormId);
		params.rows = settings._iDisplayLength;
		params.offset = settings._iDisplayStart;
		if(!obj.BIsNullVal(obj.pageParamSetting.searchCommon)){
			params = $.extend(true, params,obj.pageParamSetting.searchCommon);
		}
		$.blockUI(obj.maskContent);
		commonSelect(params, true, function(data) {
			try {
				if (data && data.data) {
					var pdata = {};
					var retDdata = data.data;
					if(typeof(obj.pageParamSetting.searchCommonCallback) == "function"){
						retDdata = obj.pageParamSetting.searchCommonCallback(data.data);
					}
					// 数据
					pdata.data = retDdata;
					// 总条数
					pdata.recordsTotal = data.total || 0;
					// 过滤没有使用到总条数
					pdata.recordsFiltered = data.total || 0;
					// 代表第几次画页面 如pdata.draw 不能删 如删掉不会翻页
					pdata.draw = settings.iDraw;
					callback(pdata);
				}
			} catch (ex) {
				obj.logErr("查询失败 failed " + ex.name + ": " + ex.message);
			}
			$.unblockUI();
		});
	};

	/***************************************************************************
	 * @TODO 通用，调试完成后分文件，不要加Velocity代码
	 */

	obj.doEdit = function() {
		if (obj.BIsNullVal(obj.pageParamSetting.editFormId)) {
			alert("editForm id 为空！");
			return;
		}
		var bool = $("#" + obj.pageParamSetting.editFormId).validate().form();
		if (bool) {
			$.blockUI(obj.maskContent);
			var params = {};
			var cValues = zkm_getHtmlFormJsons(obj.pageParamSetting.editFormId);
			// 主键的值
			params.columnValues = cValues;
			// cValues.msgReceiver="@cookie_zhi";
			params.tableName = obj.tableName;
			// 如更改不是id 需定义 params.primarykey
			commonUpdate(params, true, function(data) {
				try {
					if (data.success) {
						try {
							obj.dataTable.fnUpdate(params.columnValues, obj.rowSelTr);
							obj.showSuccess("修改成功!");
							if(!obj.BIsNullVal(obj.pageParamSetting.editShow)){
								obj.showDom(obj.pageParamSetting.editShow, "edit");
							}
							if(!obj.BIsNullVal(obj.pageParamSetting.editHide)){
								obj.hideDom(obj.pageParamSetting.editHide, "edit");
							}
						} catch (e) {
							showError("异常！");
							obj.logErr("Exception need see log " + e.name + ": " + e.message);
						}
					} else {
						obj.logErr("Exception edit failed  " + data + " " + ("Items" in data));
						showError("修改失败");
					}
				} catch (ex) {
					showError("异常！");
					obj.logErr("doEdit 2222 Excep " + ex.name + ": " + ex.message);
				}
				$.unblockUI();
			});
			return false;
		}
		return "edit";
	};

	/**
	 * 数据添加函数
	 */
	obj.doAdd = function() {
		if (obj.BIsNullVal(obj.pageParamSetting.addFormId)) {
			alert("addForm id 为空！");
			return;
		}
		var bool = $("#" + obj.pageParamSetting.addFormId).validate().form();

		if (bool) {
			$.blockUI(obj.maskContent);
			var cValues = zkm_getHtmlFormJsons(obj.pageParamSetting.addFormId);
			var params = {};
			// cValues.readFlag="@cookie_name";
			params.columnValues = cValues;
			params.tableName = obj.tableName;
			// 使用增
			commonInsertOrUpdate(params, true, function(data) {
				try {
					if (data) {
						pageOptionCurdStatus = "add";
						if (data.success) {
							params.columnValues.id = data.idValues[0];
							obj.dataTable.api().row.add(params.columnValues).draw();
							obj.showSuccess("添加成功!");
							if(!obj.BIsNullVal(obj.pageParamSetting.addShow)){
								obj.showDom(obj.pageParamSetting.addShow, "add");
							}
							if(!obj.BIsNullVal(obj.pageParamSetting.addHide)){
								obj.hideDom(obj.pageParamSetting.addHide, "add");
							}
							obj.doCleanAddForm();
						} else {
							showError(data.mgs);
							obj.logErr("Exception add failed  " + data + " " + ("Items" in data));
						}
					}
					$.unblockUI();
				} catch (exx) {
					obj.logErr("Exception need see log " + exx.name + ": " + exx.message);
					showError(data.mgs)
					pageOptionCurdStatus = "add";
					$.unblockUI();
				}
			});
		} else {
			// 添加失败据需添加
			pageOptionCurdStatus = "add";
		}
	};

	/***************************************************************************
	 * @TODO 通用，调试完成后分文件，不要加Velocity代码
	 */

	obj.doDelete = function() {
		zinglabs.confirm("确认要删除此项？", function() {
			var params = {};
			$.blockUI(obj.maskContent);
			// 主键默认为id 如不为id 加上params.primarykey
			var cell = obj.dataTable.api().rows(".active").data();
			var ids = "";
			var key = "id"
			if ("primarykey" in params) {
				key = params["primarykey"];
			}
			for (var i = 0; i < cell.length; i++) {
				ids += cell[i][key] + ",";
			}
			params.columnValues = ids;
			params = $.extend({},params,obj.pageParamSetting.deleteCommon);
			commonDelete(params, true, function(data) {
				try {
					if (data) {
						if (data.success) {
							obj.dataTable.api().row('.selected').remove().draw(true);
							if(obj.BIsNullVal(obj.pageParamSetting.deleteShow)){
								obj.showDom(obj.pageParamSetting.deleteShow, "delete");
							}
							if(obj.BIsNullVal(obj.pageParamSetting.deleteHide)){
								obj.hideDom(obj.pageParamSetting.deleteHide, "delete");
							}
							obj.showSuccess("删除成功!");
							
						} else {
							obj.logErr("Exception del failed  " + data + " " + data.retcode);
							obj.showError("删除失败");
						}
					}
				} catch (e) {
					obj.logErr("Exception del need see log " + e.name + ": " + e.message);
				}
				$.unblockUI();
			});
		});
	};

	/***************************************************************************
	 * @TODO 通用，调试完成后分文件，不要加Velocity代码
	 */

	obj.BCreateDelegate = function(dparam, method) {
		return function() {
			return method.apply(dparam, arguments);
		}
	}

	obj.BRegisterExtEvent = function(sid, tid, sEvent, fnStr, paramStr) {
		try {
			var sfn = eval(fnStr);
			for (var j = 0; j < sfn.length; j++) {
				var fnt = sfn[j];
				var fn = fnt.fn;
				var fp = fnt.fp;
				if (fn) {
					var parm = {
						'srcId' : sid,
						'srcEvent' : sEvent,
						'targetId' : tid,
						'fn' : fnt
					};
					// 合并参数，以parm为主
					parm = $.extend(fp, parm);
					if (typeof (fn) == 'function') {
						var callback = BCreateDelegate(parm, fn);
						$("#" + sid).bind(sEvent, parm, callback);

						log.debug(" register target : id - " + sid
								+ " event : " + sEvent);
					} else {
						log.error(" fn : " + fn + " is not a function");
					}
				} else {
					log.error(" fn is null");
				}
			}
		} catch (e) {
			obj.logErr("Exception BRegisterExtEvent need see log " + e.name + ": "
					+ e.message);
		}

	};

	/***************************************************************************
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
	obj.setPanelVal = function(pVars,fn) {
		$('#' + pVars.id + ' :input').each(
			function(index, inputObj) {
				var type = inputObj.type;
				var tag = inputObj.tagName.toLowerCase();
				if (("isClear" in pVars) && pVars.isClear == 'true') {
					if (type == 'text' || type == 'password'
							|| tag == 'textarea' || type == 'hidden')
						$(inputObj).val('');
					else if (type == 'checkbox' || type == 'radio')
						$(obj).attr('checked', false);
					else if (tag == 'select')
						$(obj).attr('selectedIndex', -1);
				} else {
					var vTmp = pVars.data[inputObj.name];
					if(!obj.BIsNullVal(fn)){
						vTmp = fn(vTmp);
					}

					if (obj.BIsNullVal(vTmp)) {
						vTmp = '';
					}
					if (type == 'text' || type == 'password'
							|| type == 'hidden' || tag == 'textarea'
							|| tag == 'select')
						$(inputObj).val(vTmp);
					else if (type == 'checkbox' || type == 'radio') {
						$(inputObj).attr('checked', vTmp == '' ? false : vTmp);
					} else if (tag == 'select') {
						$(inputObj).attr('selectedIndex',vTmp == '' ? -1 : vTmp);
					}
				}
			});
	}

	// 每页显示条数
	obj.setPageNum = function(obj) {
		obj.dataTable.page.len(obj.value).draw();
	}
	// 刷新
	obj.refresh = function() {
		$("#searchDataForm")[0].reset();
		history.go(0);
	}

	/***************************************************************************
	 * @TODO 通用，调试完成后分文件，不要加Velocity代码
	 */
	obj.BIsNullVal = function(value) {
		return typeof (value) == "undefined"|| value == null
				|| (typeof (value) == 'string' && (value == "undefined"
						|| value == '' || value == 'null'))
				|| (typeof (value) == 'boolean' && value == false);
	};
	// 触发datables ajax 方法
	obj.search = function() {
		obj.hideDom(obj.searchHide,"search");
		obj.dataTable.api().draw(true);
	};

	/***************************************************************************
	 * 必须处理的严重错误，测试阶段可以alert 生产环境特殊标识写入日志 发现就要分析原因
	 * 
	 * @param err
	 */
	obj.logErr = function(errMsg) {
		alert(errMsg);
		log.debug(errMsg);
	};

	/***************************************************************************
	 * 嵌入ifram可以返回top parent 非嵌入 可以返回 window ...
	 * 
	 * @returns {Window}
	 * @constructor
	 */
	obj.PT = function() {
		return window;
	};

	/**
	 * //初始化验证组件
	 * 
	 * @param fromid
	 *            表单ID
	 */
	obj.initValidate = function(fromid) {
		$("#" + fromid).validate({
			// 错误信息处理方法
			errorPlacement : function(error, element) {
				// if(element.)
				element.addClass('error');
				// 先销毁
				element.tooltip('destroy');
				var errorInfo = error.html();
				element.tooltip({
					trigger : "manual",
					title : errorInfo,
					placement : "left"
				});
				element.tooltip("show");
			},
			// 成功信息处理方法
			success : function($e, dom) {
				var element = $(dom);
				element.tooltip('hide');
				element.removeClass('error');
			}
		});
	}

	// 打开添加表单
	obj.openAdd = function(showIds,hideIds) {
		obj.showHideDom(showIds, hideIds);
		obj.pageOptionCurdStatus = "add";
		obj.doCleanAddForm();

	}

	// 打开修改表单
	obj.openEdit = function(data) {
		if(!obj.BIsNullVal(obj.pageParamSetting.openEditShow)){
			obj.showDom(obj.pageParamSetting.openEditShow, "openEdit");
		}
		if(!obj.BIsNullVal(obj.pageParamSetting.openEditHide)){
			obj.hideDom(obj.pageParamSetting.openEditHide, "openEdit");
		}
		obj.pageOptionCurdStatus = "edit";
		var pvars = {
			"id" : obj.pageParamSetting.editFormId,
			"data" : data
		};
		obj.setPanelVal(pvars,obj.pageParamSetting.editSetFn);
	}
	// 提交方法
	obj.doSubmit = function() {
		try {
			if (obj.pageOptionCurdStatus == "add") {
				obj.doAdd();
			} else if (obj.pageOptionCurdStatus == "edit") {
				obj.doEdit();
			}
		} catch (e) {
			// 编辑、增加页面状态清理。一定要确保页面数据已经不再使用。
			zinglabs.alert("出现异常");
			obj.pageOptionCurdStatus = "false";
			obj.logErr("Exception BtnAddSubmit need see log " + e.name + ": " + e.message);
		}
	}
	// 显示错误信息
	obj.showError = function(mgs) {
		// $("#errorDiv").show();
		// $("#successDiv").hide();
		// $("#errorDiv").html(mgs);
		// setTimeout(function(){
		// $("#errorDiv").hide();
		// },3000);
		zinglabs.alert(mgs);

	}
	// 显示成功信息
	obj.showSuccess = function(mgs) {
		// $("#errorDiv").hide();
		// $("#successDiv").show();
		// $("#successDiv").html(mgs);
		// setTimeout(function(){
		// $("#successDiv").hide();
		// },3000);
		zinglabs.alert(mgs);
	}
	// 清空添加表单
	obj.doCleanAddForm = function() {
		var pvars = {
			"id" : obj.pageParamSetting.addFormId,
			"isClear" : "true"
		};
		obj.setPanelVal(pvars);
	}

	obj.cleanSearchForm = function() {
		$("#searchDataForm")[0].reset();
	}
	return obj;
})();
