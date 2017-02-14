// - 购物车弹窗
$(function () {
	var on = 0;
	$(".add_cart").live('click', function (event) {
		event.stopPropagation();
		event.preventDefault();
		$(document).trigger('click');
		$(".goods_rec_single").unbind();
		$(".item_box").unbind();

		//$(this).parents('.goods_rec_single').addClass("pop_on").find('.cart_pop').show();
		//$(this).parents('.item_box').addClass("pop_on").find('.cart_pop').show();
	});
	$(".cart_pop").live('click', function () {
		event.stopPropagation();
	});
	$(".continue_buy").live('click', function (event) {
		$('.cart_pop').hide().parents('.goods_rec_single').removeClass("pop_on");
		$('.cart_pop').hide().parents('.item_box').removeClass("pop_on");
	});

	$(document).on('click', function () {
		var cart_pop = $(".pop_on").find('.cart_pop');
		var goods_rec_single = cart_pop.parents('.goods_rec_single');
		var item_box = cart_pop.parents('.item_box');

		cart_pop.hide();
		goods_rec_single.removeClass("pop_on");
		item_box.removeClass("pop_on");
	});
});

// - 瀑布溜自适应
$(function () {

	$(".waterfall").each(function () {
		var divArr = $(this).find(".waterfall_el");
		var l = 0,
			r = 0,
			waterfall_left = $(this).find(".waterfall_left"),
			waterfall_right = $(this).find(".waterfall_right");

		for (var j = 0; j < divArr.length; j++) {
			if (j == 0) {
				waterfall_left.append(divArr[j]);
			} else {
				waterfall_right.append(divArr[j]);
			}
		}

		var i = 0;
		var locEl = function () {
			// console.log(l, r);
			if (l <= r) {

				var imgHeight = 0;

				var obj = new Image();
				obj.src = $(divArr[i]).find('img')[0].src;
				obj.onload = function () {
					imgHeight = obj.height;
					l += $(divArr[i]).height();
					waterfall_left.append(divArr[i]);
					i++;

					if (i < divArr.length) {
						locEl();
					}
				}
				obj.onerror = function () {
					imgHeight = obj.height;
					l += $(divArr[i]).height();
					waterfall_left.append(divArr[i]);
					i++;

					if (i < divArr.length) {
						locEl();
					}
				}

			} else {

				var imgHeight = 0;

				var obj = new Image();
				obj.src = $(divArr[i]).find('img')[0].src;
				obj.onload = function () {
					imgHeight = obj.height;
					r += $(divArr[i]).height();
					waterfall_right.append(divArr[i]);
					i++;

					if (i < divArr.length) {
						locEl();
					}
				}
				obj.onerror = function () {
					imgHeight = obj.height;
					r += $(divArr[i]).height();
					waterfall_right.append(divArr[i]);
					i++;

					if (i < divArr.length) {
						locEl();
					}
				}
			}
			$(divArr[i]).css('visibility', 'visible');

		};
		locEl();

	});

});

// - 加载更多是显示 loading img
$(function () {
	$(".load_more").live('click', function () {
		$(this).hide().next().show();
	});
});