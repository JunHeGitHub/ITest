//加载数据
function initData() {
	
	var rdata;
	var pat = {};
	pat.tableName = "ZQC_STATE_MANAGER";
	commonSelect(pat, false, function (data) {
		if (data && data.rows != null) {
			rdata = data.rows;
		}
	});
	return rdata;
	
}
function showAdd(){
	$("#updateFormDiv").slideUp(500);
	$("#addFormDiv").slideDown(300);
	$("#btn_tianjia").hide();
	$("#btn_tijiao").show();
	scroll2data("addFormDiv");
}