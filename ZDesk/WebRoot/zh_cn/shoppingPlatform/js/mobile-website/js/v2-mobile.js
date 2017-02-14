/**
 * User: fengweixing
 * Date: 13-6-13
 * Time: 13:36
 */
//TODO: 所有click事件改为tap触屏事件

//页面加载完成隐藏地址栏
window.addEventListener('load', function () {

});

//返回顶部
$('.gotop').click(function(){
    window.scrollTo(0, 1);
});

//页面模块置顶
function modFixed(fixedEle){
    $(window).scroll(function(){
        var scrollTopValue = $(window).scrollTop();
        if(scrollTopValue>1){
            $(fixedEle).addClass('fixed');
        }else{
            $(fixedEle).removeClass('fixed');
        }
    });
}

//页面模块悬浮
function modFloat(floatEle){
    var eleH = $(floatEle).height(),
        offsetTopValue = $(floatEle).offset().top;
    $(window).scroll(function(){
        var scrollTopValue = $(window).scrollTop() + eleH;
        if(scrollTopValue>offsetTopValue){
            $(floatEle).removeClass('fixed');
        }else{
            $(floatEle).addClass('fixed');
        }
    });
}

//将弹出框放在页面可视区域合适位置
function fSetAlertTipPosition(element) {
    var offsetTopValue = $(window).scrollTop(), //获取距顶部滚动距离
        innerH = $(window).height(), //获取当前屏幕可视区域高度
        alertTipH = $(element).height();//获取弹出框高度
    //如可视区域高度小于弹出框高度，则从可视区域顶部开始显示弹出框
    if (innerH < alertTipH) {
        $(element).css('top', offsetTopValue);
    } else {
        //反之，则在可视区域中间显示弹出框
        var alertTipTopValue = (innerH - alertTipH) / 2 + offsetTopValue;
        $(element).css('top', alertTipTopValue);
    }

    $('.closeBtn').click(function(){
        $('.tipMask, .tip').hide();
    });
}

//获取长度为len的随机字符串
function getRandomString(len) {
    len = len || 32;
    var $chars = '0123456789'; // 默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1
    var maxPos = $chars.length;
    var pwd = '';
    for (i = 0; i < len; i++) {
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
    }
    return pwd;
}

//搜索自动提示
function sechSgst(){
    fInputFocusBlur();//input默认文字focus blur效果

    $('#keyword').focus(function(){
        $('.cateBtn').hide();
        $(this).parent().addClass('searchFormFocus');
        if($(this).parent().siblings().hasClass('legRegOk')){
            $('#suggest').addClass('suggestAfterLegReg');
        }
    });

    $('#keyword, #suggest').click(function(e){
        e.stopPropagation();
    });

/*    $('#keyword').blur(function(){
        $('.cateBtn').show();
        $(this).parent().removeClass('searchFormFocus');
    });*/

/*    $('#keyword').keyup(function(){
        //过滤空格
        var inputTxt = $(this).val().replace(/^\s\s*//*, '');
        var delayTime = 400;

        //延迟400ms显示搜索建议和执行查询
        setTimeout(function(){
            if(inputTxt){
                $('#suggest').show();
            }else{
                $('#suggest').hide();
            }
        }, delayTime);
    });*/

    $('body').click(function(){
        $('#suggest').hide();
        $('#keyword').parent().removeClass('searchFormFocus');
        $('.cateBtn').show();
    });
}

//input默认文字focus blur效果, 根据html中input的tip属性值来设置input value
function fInputFocusBlur(){
    var inputEle = $('input[tip]');
    for(var i =0;i<inputEle.length;i++){
        var nowInput = inputEle.eq(i);
        nowInput.val(nowInput.attr('tip')).css('color', '#999');
    }
    inputEle.click(function(){
        var _this = $(this)
            ,defVal=_this.attr('tip');
        _this.parent().addClass('cur');
        if (_this.val() == defVal) {
            _this.val('');
            _this.css('color','#333');
        }
    }).blur(function(){
            var _this = $(this)
                ,defVal=_this.attr('tip');
            _this.parent().removeClass('cur');
            if ((_this.val()).replace(/\s*/g,'') == '') {
                _this.val(defVal);
                _this.css('color','#999');
            }
        });
}

//添加到购物车
function buy_car(btnEle) {//添加到购物车
    $(btnEle).live('click', function () {//click要绑定到ajax出来的元素上，所以用live
        //如果是1mall或卖完的商品，则不显示加入购物车提示; 如果用户未登录时添加landingpage商品，不显示加购物车提示，直接跳转到登录页面
        if ($(this).closest('dl').hasClass('isMall') !== true && $(this).closest('dl').hasClass('soldOut') !== true && !$(this).hasClass('landingpage_nolog')) {
            $(this).next().show();
            $(this).closest('dl').siblings().find('.ins').hide();
        }
    });
    $('.ins .goBuy').live('click', function () {//click要绑定到ajax出来的元素上，所以用live
        $(this).parent().hide();
        return false;
    });
};

/*属性选择 attribute*/
var mAttr = {
    /*3种属性定义*/
    attrData: {
        mColor:false,
        mSize:false,
        mMtrl:false
    },
    idNoSlt: null,
    /*初始化*/
    init: function(){
        mAttr.checkDefault();
        mAttr.click('#mColor');
        mAttr.click('#mSize');
        mAttr.click('#mMtrl');
        mAttr.submit();
    },
    /*页面加载时检查是否有选中属性*/
    checkDefault: function(){
        if($('#mColor span').hasClass('checked')){
            mAttr.attrData.mColor = $('#mColor .checked').text();
            $('#choosecolor, #choosecolor2').text(mAttr.attrData.mColor);
        }
        if($('#mSize span').hasClass('checked')){
            mAttr.attrData.mSize = $('#mSize .checked').text();
            $('#choosesize, #choosesize2').text(mAttr.attrData.mSize);
        }
        if($('#mMtrl span').hasClass('checked')){
            mAttr.attrData.mMtrl = $('#mMtrl .checked').text();
            $('#choosemtrl, #choosemtrl2').text(mAttr.attrData.mMtrl);
        }
    },
    /*页面点击选择属性*/
    click: function(id){
        var thumbIndex;
        $('span', id).not('.out').click(function(){
            $(this).addClass('checked').siblings().removeClass('checked');
            if(id == '#mColor'){
                mAttr.attrData.mColor = $(this).text();
                $('#choosecolor, #choosecolor2').text(mAttr.attrData.mColor);
                thumbIndex = $('#mColor span').index($(this));
                mAttr.changeThumb(thumbIndex);
            }
            if(id == '#mSize'){
                mAttr.attrData.mSize = $(this).text();
                $('#choosesize, #choosesize2').text(mAttr.attrData.mSize);
            }
            if(id == '#mMtrl'){
                mAttr.attrData.mMtrl = $(this).text();
                $('#choosemtrl, #choosemtrl2').text(mAttr.attrData.mMtrl);
            }
        });
    },
    /*切换缩略图*/
    changeThumb: function(thumbIndex){
        $('.mProThumb img').eq(thumbIndex).show().siblings('img').hide();
    },
    /*返回编辑*/
    goBack: function(){
      $('.backToEdit').click(function(){
         var attrId = $(this).closest('.tip').attr('id');
         if(attrId == 'mtrlTip'){
             $('#mtrlTip').hide();
             $('#sizeTip').show();
             mAttr.tipClick('#sizeTip');
         }else if(attrId == 'sizeTip'){
             $('#sizeTip').hide();
             $('#colorTip').show();
             mAttr.tipClick('#colorTip');
         }
      });
    },
    /*如果页面属性未选择时点击加入购物车，则显示属性选择弹框*/
    tipShow: function(tipId){
        $(tipId).show();
        $(tipId).parent().siblings('.tipMask').show();
        fSetAlertTipPosition('.alertTip');
        mAttr.tipClick(tipId);
        mAttr.tipClose();
        mAttr.goBack();
    },
    /*在弹框中点击选择属性*/
    tipClick: function(tipId){
        $('li', tipId).not('.out').click(function(){
            $(this).addClass('checked').siblings().removeClass('checked');
            var valueIdx = $(this).closest('.tip').find('li').index($(this));
            if(tipId == '#colorTip'){
                mAttr.attrData.mColor = $(this).text();
                $('#choosecolor, #choosecolor2').text(mAttr.attrData.mColor);
                $('#mColor span').eq(valueIdx).addClass('checked').siblings().removeClass('checked');
                $(tipId).hide();
            }
            if(tipId == '#sizeTip'){
                mAttr.attrData.mSize = $(this).text();
                $('#choosesize, #choosesize2').text(mAttr.attrData.mSize);
                $('#mSize span').eq(valueIdx).addClass('checked').siblings().removeClass('checked');
                $(tipId).hide();
            }
            if(tipId == '#mtrlTip'){
                mAttr.attrData.mMtrl = $(this).text();
                $('#choosemtrl, #choosemtrl2').text(mAttr.attrData.mMtrl);
                $('#mMtrl span').eq(valueIdx).addClass('checked').siblings().removeClass('checked');
                $(tipId).hide();
            }
            $(tipId).parent().siblings('.tipMask').hide();
            mAttr.tipStep();
        });
    },
    /*弹窗顺序显示*/
    tipStep: function(){
        for(var j in mAttr.attrData){
            if(!mAttr.attrData[j]){
                idNoSlt = j.toLowerCase().replace(/^m{0,1}/gi, '') + 'Tip';
                mAttr.tipShow('#'+idNoSlt);
                break;
            }
        }
    },
    /*关闭弹窗*/
    tipClose: function(){
        $('.tip h4 b, .tip .closeBtn').click(function(){
           $('.tipMask, .tip').hide();
        });
    },
    /*点击 加入购物车 按钮*/
    submit: function(){
            $('#mAttrSbmtBtn').click(function(){
                mAttr.tipStep();
            });
    }
}

//右上角购物车数字变化
function fAddCartNum() {
    var carNum = parseInt($('.headVerTen .cart em').text(), 10);
    if ($('#buycount').length > 0) {
        var iptNum = parseInt($('.buy_num').val(), 10),
            finalNum = carNum + iptNum;
        $('.headVerTen .cart em').text(finalNum);
    } else {
        $('.headVerTen .cart em').text(carNum + 1);
    }
}

//检查页面输入框是否有默认值，如果没有则显示错误提示
function fChkCopnIpt(whichIpt) {
    var defaultIptVal = $(whichIpt).attr('value');
    console.log(defaultIptVal);
    if (defaultIptVal != '') {
        $(whichIpt).parents().next('.mobiEror').hide();
        $(whichIpt).parents('.w48').next('.mobiEror').hide();
        $(whichIpt).parents('.longIpt').siblings('.alignC').find('.greyBtn').removeClass('greyBtn').addClass('redBtn');
        $(whichIpt).parents('.verfyCod').siblings('.alignC').find('.greyBtn').removeClass('greyBtn').addClass('redBtn');
    }else{
        $(whichIpt).parents('.longIpt').siblings('.alignC').find('.redBtn').removeClass('redBtn').addClass('greyBtn');
    }

    //检测input是否有输入以及相应的效果
    $(whichIpt).blur(function () {
        var newIptVal = $(this).val().replace(/(^\s*)|(\s*$)/g, '');
        if (newIptVal == '') {
            $(this).parents().next('.mobiEror').show();
            $('.longIpt').siblings('.alignC').find('.redBtn').removeClass('redBtn').addClass('greyBtn');
            $('.verfyCod').next('.alignC').find('.redBtn').removeClass('redBtn').addClass('greyBtn');
        } else {
            $(this).parents().next('.mobiEror').hide();
            $('.longIpt').siblings('.alignC').find('.greyBtn').removeClass('greyBtn').addClass('redBtn');
            $('.verfyCod').next('.alignC').find('.greyBtn').removeClass('greyBtn').addClass('redBtn');
        }
    }).keyup(function () {
            $(this).triggerHandler('blur');
        });

    //focus input 显示‘清除’按钮
    $(whichIpt).focus(function(){
        $(this).parent().prev().show();
    });

    //点击‘清除’按钮，清空input
    $('.clearIpt').bind('click', function(){
        $(this).next().children().val('');
        $('.longIpt').siblings('.payNowBtn').addClass('greyBtn').removeClass('payNowBtn');
        $('.alignC .redBtn').removeClass('redBtn').addClass('greyBtn');
    });

    //页面加载完成光标自动focus到页面第一个input里文字的末尾
    $('.mobiIpt input').selectRange();
}

//页面加载完成光标自动focus到页面第一个input里文字的末尾
$.fn.selectRange = function(){
    var _this = this[0],
        length = _this.value.length;
    if (_this.setSelectionRange) {
        _this.focus();
        _this.setSelectionRange(length, length);
    } else if (_this.createTextRange) {
        var range = _this.createTextRange();
        range.collapse(true);
        range.moveEnd('character', length);
        range.moveStart('character', length);
        range.select();
    }
};

//手机注册设置密码页面 - click显示隐藏密码
function fShowPswd() {
    var togglePasswordField = document.getElementById('togglePswd');
    togglePasswordField.addEventListener('click', fTogglePswdClick, false);
}

function fTogglePswdClick() {
    var passwordField = document.getElementById('mobiPswd');
    if (passwordField.type == 'password') {
        $('#togglePswd').removeClass('checkbox2').addClass('checkbox');
        passwordField.type = 'text';
    } else {
        $('#togglePswd').removeClass('checkbox').addClass('checkbox2');
        passwordField.type = 'password';
    }
}

//手机号注册页面 - 重新获取验证码 按钮 倒计时功能
function fShowTime(totalSec) {
    var realSec = totalSec - 1;
    if (totalSec != 0) {
        fGetCopnTimer(realSec);
        window.setTimeout('fShowTime(' + realSec + ')', 1000);
        if(realSec == '0'){
            $('.timerOut').hide();
            $('#twice').removeClass('greyBtn').addClass('payNowBtn').children('em').show();
            $('#timer').addClass('none');
        }
    } else {
        return false;
    }
}
function fGetCopnTimer(sec) {
    $('#timer').html(sec);
}

//方便自动或手动执行倒计时
function fTimerOrNot(element){
    var timerValue = $('#timerValue').val();
    $('em', element).hide();
    $(element).children('.timerOut').show();
    fShowTime(timerValue); //倒计时功能
    $('#timer').removeClass('none');
    $('#twice').removeClass('payNowBtn').addClass('greyBtn');
}

//包裹中剩余商品显示隐藏 leftPackgeHide
function leftPackgeShowHide(){
    $('.packge').each(function(){
        var packgePro = $('.borderBotm', this),
            packgeProLen = packgePro.length;

        packgeLoop();
        /*默认只显示2个*/
        function packgeLoop(){
            for(var i = 2; i < packgeProLen; i++){
                packgePro.eq(i).hide();
            }
        }

        /*更多商品显示*/
        $('.pages_nextprev', this).click(function(){
            if($('p', this).hasClass('cur')){
                packgeLoop();
                $('p', this).removeClass('cur');
            }else{
                packgePro.show();
                $('p', this).addClass('cur');
            }
        });
    });
}

/*是否要发票*/
function getInvoices(){
    $('.modRadio').click(function(){
        if($('i', this).hasClass('fl')){
            $('i', this).attr('class', 'fr');
            $('ins', this).text('是');
            $('.modRadioCon').show();
            $('#invoicesNd, #defaultAddr').val('1');
        }else{
            $('i', this).attr('class', 'fl');
            $('ins', this).text('否');
            $('.modRadioCon').hide();
            $('#invoicesNd, #defaultAddr').val('0');
        }
    });

    $('.invoicesCon').click(function(){
        $('ul', this).show();
        $('ul', this).parent().siblings('.invoicesCon').find('ul').hide();
    });

    $('.invoicesCon li').click(function(){
       var liTxt = $(this).text();
        $(this).parents('.invoicesCon').find('p ins').text(liTxt);
        $(this).parent().siblings('p').removeClass('grey');
        setTimeout(function(){
            $('.invoicesCon ul').hide();
        }, 1);
    });
}

//价格选择
function selectPrice(ID){
    var classPrice = '.' + ID
        ,_this = $('.cashInput input');

    $('.cashInput input').selectRange();//页面加载完成光标自动focus到页面第一个input里文字的末尾
    $('a',classPrice).click(function(){
        var balanceValue = Number($('em', this).text());
        $('.cashNob').val(balanceValue);
        _this.selectRange();
    });
}

//radio变幻效果
function radio_bg(classradio) {
    var cid = '.' + classradio, label = cid + '>.radio', input = '#paymentMethod';
    $(label).click(function () {
        $(this).addClass('radio2').removeClass('radio').siblings('.radio2').addClass('radio').removeClass('radio2');
        $(input).val($(this).find('ins.none').text());
    });
}

//子分类导航
function helpOpen(ID){
    var _this = '.' +ID;
    $('.borderBotm',_this).not('.noClick').click(function(){
        if($(this).hasClass('cur')){
            $(this).removeClass('cur');
            $('.subCon', this).hide();
        }else{
            $(this).addClass('cur').siblings('.borderBotm',_this).removeClass('cur');
            $(this).find('.subCon').show();
            $(this).siblings('.borderBotm',_this).find('.subCon').hide();
        }
    });

    $('.subCon').click(function(e){
        e.stopPropagation();
    });
}

//下拉框分页
function turnPage(){
    var url=document.getElementById('selPage').options[document.getElementById('selPage').selectedIndex].value;
    window.location.href=url;
}

$(function(){
    sechSgst();//搜索自动提示

    $('#searchBtn').click(function(){
        $('.searchBar').show();
        return false;
    });

    /*n元n件页面点击 已选商品 显示隐藏效果*/
    $('#proChkedNum').parent().click(function(){
        $('#proChecked .in').show();
        return false;
    });

    //购物车checkbox模拟: 全选
    $('.checkedAllBtn').click(function(){
        var checkedBtnGroup = $(this).closest('.byCar').find('dl').not('.out'),
            numIpt = checkedBtnGroup.find('.checkedBtn').siblings('.numIpt'),
            numIptLen = numIpt.length,
            /*checkbox个数*/
//            allBtnNum = checkedBtnGroup.find('.checkedBtn').length,
            /*选中的checkbox个数*/
            checkedBtn = checkedBtnGroup.find('.checked').siblings('.numIpt'),
//            checkedLen = checkedBtn.length,
            /*总的已选中个数*/
//            allCheckedNum = Number($('.cur .proCheckedNum').text()),
            numIptSum = 0;
//            numIptSum2 = 0;

        for(var i=0; i<numIptLen; i++){
            numIptSum += Number(numIpt[i].value);
        }

        if($(this).hasClass('checked') == false){
            $(this).addClass('checked');
            checkedBtnGroup.find('.checkedBtn').addClass('checked');
            $('.cur .proCheckedNum').text(numIptSum);
            $(this).siblings('.proCheckedNum').text(numIptSum);
        }else{
            $(this).siblings('.proCheckedNum').text('0');
            $('.cur .proCheckedNum').text('0');
            $(this).removeClass('checked');
            $(this).closest('.byCar').find('dl').not('.out').find('.checkedBtn').not('.promotionBtn').removeClass('checked');
        }
        saveCheckedId();
    });
    //购物车checkbox模拟: 单选
    $('.checkedBtn').not('.checkedAllBtn').click(function(){
            /*获取shadowHead中的数量*/
        var checkedNum = Number($(this).closest('.byCar').find('.proCheckedNum').text()),
            checkbtnLen = Number($(this).closest('.byCar').find('dl').not('.out').find('.checkedBtn').length),
            checkedBtnLen = Number($(this).closest('.byCar').find('dl').not('.out').find('.checked').length),
            /*checkbox个数*/
            allBtnNum = $(this).closest('.byCar').find('dl').not('.out').find('.checkedBtn').length,
            _this = $(this),
            numIpt = _this.siblings('.numIpt')
            numIptValue = Number(numIpt.val()),
            /*总的已选中个数*/
            allCheckedNum = Number($('.cur .proCheckedNum').text());
        if($(this).hasClass('checked') == false){
            $(this).addClass('checked');
            /*如果input输入框数量大于1*/
            if(numIptValue>1){
                $('.cur .proCheckedNum').text(allCheckedNum + numIptValue);
                $(this).closest('.byCar').find('.proCheckedNum').text(checkedNum + numIptValue);
            /*如果input输入框数量=1*/
            }else{
                $('.cur .proCheckedNum').text(allCheckedNum + 1);
                $(this).closest('.byCar').find('.proCheckedNum').text(checkedNum + 1);
            }
            if(checkbtnLen == checkedBtnLen + 1){
                $(this).closest('.byCar').find('.checkedAllBtn').addClass('checked');
            }
        }else{
            $(this).removeClass('checked');
            /*如果input输入框数量大于1*/
            if(numIptValue>1){
                $('.cur .proCheckedNum').text(allCheckedNum - numIptValue);
                $(this).closest('.byCar').find('.proCheckedNum').text(checkedNum - numIptValue);
                if(checkedNum == numIptValue || (checkedNum + numIptValue) != allBtnNum){
                    $(this).closest('.byCar').find('.checkedAllBtn').removeClass('checked');
                }
            /*如果input输入框数量=1*/
            }else{
                $('.cur .proCheckedNum').text(allCheckedNum - 1);
                $(this).closest('.byCar').find('.proCheckedNum').text(checkedNum - 1);
                if(checkedNum == 1 || (checkedNum + 1) != allBtnNum){
                    $(this).closest('.byCar').find('.checkedAllBtn').removeClass('checked');
                }
            }
        }
        saveCheckedId();
    });

    $('.giftListRadio').click(function(){
        $(this).closest('.byCar').find('.proCheckedNum').text(1);
    });
});
