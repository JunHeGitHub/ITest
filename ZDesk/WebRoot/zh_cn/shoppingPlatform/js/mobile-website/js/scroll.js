// scroll
(function(win){
    var touch = iTouch;
    if(!touch){return false;}

    var nextFrame = (function() {
        return window.requestAnimationFrame ||
            window.webkitRequestAnimationFrame ||
            window.mozRequestAnimationFrame ||
            window.oRequestAnimationFrame ||
            window.msRequestAnimationFrame ||
            function(callback) { return setTimeout(callback, 1); };
    })();

    var cancelFrame = (function () {
        return window.cancelRequestAnimationFrame ||
            window.webkitCancelAnimationFrame ||
            window.webkitCancelRequestAnimationFrame ||
            window.mozCancelRequestAnimationFrame ||
            window.oCancelRequestAnimationFrame ||
            window.msCancelRequestAnimationFrame ||
            clearTimeout;
    })();

     var PI = Math.PI;

    // todo: :(
    var timer = null;

    /** 扩展对象，仅适用在单层的扩展中
     * @param {Object} merge 来源对象
     * @param {Object} tar 扩展的目标对象
     * @param {Boolean} safe 是否进行安全的扩展，只扩展目标对象中已有的属性
     */
    function extend (merge, tar, safe) {
        var already;
        if (!safe) {
            already = function () { return true; };
        }
        else {
            already = function (obj, proper) {
                return obj.hasOwnProperty(proper);
            }
        }
        if (merge != null && tar != null) {
            var src, copy, name;
            for (name in merge) {
                if (merge.hasOwnProperty(name)) {
                    copy = merge[name];
                    if (tar === copy) {
                        continue;
                    }
                    //只覆盖已定义的属性？
                    if (copy !== undefined && already(tar, name)) {
                        tar[name] = copy;
                    }
                } //if-END
            } //for-END
            return tar;
        } //if-END
    }

    /** 使用原型链接来创建新对象
     * @param {Object} obj 要赋予原型的对象
     */
    function pro (obj) {
        var Func = function () { };
        Func.prototype = obj;
        return new Func();
    } //var pro - END


    //outer :外部容器
    // inner : 内部容器 改变transform
    // fix : 滑动后需要修正的单位值
    // inertia : 是否具有惯性
    function iScroll(param){
        var defP = {
            outer : false,
            inner : false,
            atTop  : false,
            spacing : false,
            atEnd : false,
            moving : false,
            dir : 'column',//'row'
            inertia : false,
            click : false
        };
        var newFun = pro(Fn);
        extend(param, defP, true);
        extend(defP, newFun);
        newFun.init();
        return newFun;
    }

    var Fn = {
        init:function(){
            var self = this;
            //检测是不是存在outer 和 element .如果没有则返回
            this.lastMoved = null;

            if(!this.outer || !this.inner){
                return false;
            }

            this.plant = null;
            var ua = navigator.userAgent.toLocaleLowerCase();
            if(ua.indexOf("iphone") > -1){
                this.plant = "iphone";
            }else if(ua.indexOf("android") > -1){
                this.plant = "android";
            }

            //为元素设置样式.
            this.setStyle();

            //加入touch事件
            touch({
                element : self.outer,
                start   : function(e){
                    self.start(e);
                },
                move    : function(e,dir,disX,disY,x,y){
                    self.move(e,dir,disX,disY,x,y);
                },
                end : function(){
                    self.end();
                },
                sliceEnd:function(v){
                    self.sliceEnd(v);
                },
                click : function(e){
                    self.click && self.click.apply(null,[e]);
                },
                prevent : 'all',
                multiple : false
            });
        },

        // sin .
        sin : function(start,end,time,callback,finish){
        var _start = new Date().getTime();
        var _end = _start + time;

        function step() {
            var _now = new Date().getTime();
            var _mid = Math.sin((_now-_start)/(_end - _start) * PI / 2) * (end - start) + start;
            callback && callback.apply(null,[_mid]);
            if (_now > _end ){
                cancelFrame(timer);
                timer = null;
                finish && finish.apply(null);
            }else{
                timer = nextFrame(step);
            }
        }

        if(!!timer){
            cancelFrame(timer);
            timer = null;
        }

        timer = nextFrame(step);
        },

            // 按照加速度去减速。
        slowdown : function(s,v,callback,finish){
        var _start = new Date().getTime();
        var _a = (v/v) * (-0.1);
        t = Math.abs(v/_a);
        function step(){
            var _now = new Date().getTime();
            var _deltaT = (_now - _start);
            var _mid = s + (2*v + _a*_deltaT)*_deltaT/2;
            callback && callback.apply(null,[_mid]);
            if (_now - _start > t ){
                cancelFrame(timer);
                timer = null;
                finish && finish.apply(null);
            }else{
                timer = nextFrame(step);
            }
        }

        if(!!timer){
            cancelFrame(timer);
            timer = null;
        }

        timer = nextFrame(step);
        },


        setStyle : function(){
            var inner = this.inner,
                outer = this.outer,
                prefix = this.prefix;

            outer.style['overflow'] = 'hidden';
            outer.style['position'] = outer.style['position'] ? outer.style['position'] : 'relative';

            inner.style['webkitTransformStyle'] = 'preserve-3d';

            if(this.dir === 'column' ){
                inner.style['webkitTransform'] = 'translateY(0)';
            }else if(this.dir === "row"){
                inner.style['webkitTransform'] = 'translateY(0)';
            }

        },

        start : function(e){
            this.touchstartY = parseFloat(this.inner.style['webkitTransform'].replace('translateY(',''));
            this.movedDis = 0;
        },

        move : function(e,dir,disX,disY,x,y){
            var _ih = this.inner.clientHeight;
            var _oh = this.outer.clientHeight;
            if(_ih < _oh){return false;}

            if(this.touchstartY == null ){
                this.start();
                this.movedDis = disY;
                return;
            }
            var _pos = disY - this.movedDis + this.touchstartY;
            this.move2Pos(_pos);
        },

        end : function(){
            this.movedDis = 0;
            this.touchstartY = null;
            this.lastMoved = null;

            var _ih = this.inner.clientHeight;
            var _oh = this.outer.clientHeight;
            // debugger;
            if(_ih < _oh){return false;}

            var self = this;
            var _buf = this.buffer;
            var _y = parseFloat(this.inner.style['webkitTransform'].replace('translateY(',''));

            var _mh = -_ih + _oh;
            if(_y > 0 ){
                self.sin(_y,0,200,function(_mid){
                    self.move2Pos(_mid);
                },function(){
                    self.move2Pos(0);
                    self.atTop && self.atTop.apply(null);
                });
            }else if(_y < _mh){
                self.sin(_y,_mh,200,function(_mid){
                    self.move2Pos(_mid);
                },function(){
                    self.move2Pos(_mh);
                    self.atEnd && self.atEnd.apply(null);
                });
            }
        },

        // 快速滑动
        // todo: 调试效果。
        // todo: 加入加速度换算。
        sliceEnd: function(v){
            var _ih = this.inner.clientHeight;
            var _oh = this.outer.clientHeight;
            if(_ih < _oh){return false;}

            var self = this;
            var _vy = v.y;
            var _absvy = Math.abs(v.y);
            var _y = parseFloat(this.inner.style['webkitTransform'].replace('translateY(',''));
        },

        move2Pos : function(_pos,hasBuffer){
            if(hasBuffer == undefined){hasBuffer = true;}
            var _buf =hasBuffer ? this.buffer : 0;
            if(_pos > _buf){
                _pos = _buf;
            }

            var _iw = this.inner.clientWidth;
            var _ow = this.outer.clientWidth;
            var _mh = -_iw + _ow - _buf;
            if(_pos < _mh){
                _pos = _mh;
            }

            this.moving && this.moving.apply(null,[Math.abs(_pos)]);
            this.inner.style['webkitTransform'] =  'translateY(' + _pos + 'px)'
        }
}

//--------view model----------->>
    win.iScroll =  iScroll;

})(window);