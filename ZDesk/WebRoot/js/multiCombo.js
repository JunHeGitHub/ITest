(function($) {
	$.fn.extend({
		"MultiCombo" : function(options) {
			var op = $.extend({
						width : "138px",
						height : "auto",
						data : []
					}, options);
			var $this = $(this);
			var $multiBtn = $(this).find("button");// 指向multBtn按钮id
			var $multiInput = $(this).find("input[type=text]");
			var $list = $('<div class="list hide" style="border: 1px solid #617775;border-top:0;  max-height:auto;	overflow: hidden;background: #fff;margin-left:120px; position:absolute;z-index: 99999 !important;"></div>').appendTo($this);
			$list.css({
						"width" : op.width,
						"height" : op.height
					});
			// 控制弹出页面的显示与隐藏
			$multiBtn.click(function(e) {
						$list.toggle();
						e.stopPropagation();
					});
			$(document).click(function() {
						$list.hide();
					});

			$list.filter("*").click(function(e) {
						e.stopPropagation();
					});
			// 加载默认数据
			$list.append('<ul style="padding:0;margin:0;"></ul>');
			var $ul = $list.find("ul");
			// 加载json数据
			if (op.data.length > 0) {
				var jsonData = op.data;
				for (var i = 0; i < jsonData.length; i++) {
					$ul
							.append('<li style="border-top: 1px solid #617775;font-size:12px;"><input style="margin:3px;" type="checkbox" value="'
									+ jsonData[i].code
									+ '" /><span>'
									+ jsonData[i].name + '</span></li>');
				}
			}
			// 点击其它复选框时，更新隐藏控件值,文本框的值
			$("input", $ul).click(function() {
						var kArr = new Array();
						$("input:checked", $ul).each(function(index) {
									kArr[index] = $(this).val();
								});
						$multiInput.val(kArr.join(","));
			});
		}
	});
})(jQuery);