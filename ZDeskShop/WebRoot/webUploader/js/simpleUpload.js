(function( $ ){
    	// 当domReady的时候开始初始化
		var array = new Array();
		var dataId = params.dataId;
		if(dataId == undefined||dataId===''||dataId==null){
			dataId="";
		}
		var maxSize = params.maxSize;
		if(maxSize == undefined || maxSize===' '|| maxSize==null){
			maxSize=20*1024*1024;//20M
		}else{
			maxSize=maxSize*1024*1024;//maxSize M
		}
		var relativeKey = params.relativeKey;
		if(relativeKey == undefined || relativeKey==='' || relativeKey==null){
			relativeKey="relativePath";
		}
		var reFillFn = params.reFillFn;
		if(reFillFn == undefined || reFillFn==='' || reFillFn==null){
			reFillFn="";
		}
		var limitSize = params.limitSize;
		if(limitSize == undefined || limitSize==='' || limitSize==null){
			limitSize=200*1024*1024;
		}
		var limitCount = params.limitCount;
		if(limitCount == undefined || limitCount===' '|| limitCount==null){
			limitCount=300;
		}
		var allowType = params.allowType;
		if(allowType == undefined || allowType=='' || allowType==null){
			allowType='all';
		}
		var isMultiple = params.isMultiple;
		if(isMultiple == undefined || isMultiple === '' || isMultiple==null){
			isMultiple=true;
		}
		var serverPath = params.serverPath;
		if(serverPath == undefined || serverPath === '' || serverPath==null){
			serverPath='ZKM';
		}
		var maps = {"all":{title:"所有文件(*.*)",extensions:'*',mimeTypes:'*/*'},"images":{title:"图片文件(*.gif,*.jpg,*.jpeg,*.bmp,*.png)",extensions:'gif,jpg,jpeg,bmp,png',mimeTypes:'image/*'}}
    $(function() {
        var $wrap = $('#uploader'),
            // 文件容器
            $queue = $( '<ul class="filelist"></ul>' ) .appendTo( $wrap.find( '.queueList' ) ),

            // 状态栏，包括进度和控制按钮
            $statusBar = $wrap.find( '.statusBar' ),

            // 上传按钮
            $upload = $wrap.find( '.uploadBtn' ),

            // 没选择文件之前的内容。
            $placeHolder = $wrap.find( '.placeholder' ),

            $progress = $statusBar.find( '.progress' ).hide(),

            // 添加的文件数量
            fileCount = 0,

            // 添加的文件总大小
            fileSize = 0,

            // 优化retina, 在retina下这个值是2
            ratio = window.devicePixelRatio || 1,

            // 可能有pedding, ready, uploading, confirm, done.
            state = 'pedding',

            // 所有文件的进度信息，key为file id
            percentages = {},
            // 检测是否已经安装flash，检测flash的版本
            flashVersion = ( function() {
                var version;

                try {
                    version = navigator.plugins[ 'Shockwave Flash' ];
                    version = version.description;
                } catch ( ex ) {
                    try {
                        version = new ActiveXObject('ShockwaveFlash.ShockwaveFlash')
                                .GetVariable('$version');
                    } catch ( ex2 ) {
                        version = '0.0';
                    }
                }
                version = version.match( /\d+/g );
                return parseFloat( version[ 0 ] + '.' + version[ 1 ], 10 );
            } )(),

            supportTransition = (function(){
                var s = document.createElement('p').style,
                    r = 'transition' in s ||
                            'WebkitTransition' in s ||
                            'MozTransition' in s ||
                            'msTransition' in s ||
                            'OTransition' in s;
                s = null;
                return r;
            })(),

            // WebUploader实例
            uploader;

        if ( !WebUploader.Uploader.support('flash') && WebUploader.browser.ie ) {
            // flash 安装了但是版本过低。
            if (flashVersion) {
                (function(container) {
                    window['expressinstallcallback'] = function( state ) {
                        switch(state) {
                            case 'Download.Cancelled':
                               window.top. zinglabs.alert('您取消了更新！')
                                break;

                            case 'Download.Failed':
                               window.top. zinglabs.alert('安装失败')
                                break;

                            default:
                                window.top.zinglabs.alert('安装已成功，请刷新！');
                                break;
                        }
                        delete window['expressinstallcallback'];
                    };

                    var swf = 'js/expressInstall.swf';
                    // insert flash object
                    var html = '<object type="application/' +
                            'x-shockwave-flash" data="' +  swf + '" ';

                    if (WebUploader.browser.ie) {
                        html += 'classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" ';
                    }

                    html += 'width="100%" height="100%" style="outline:0">'  +
                        '<param name="movie" value="' + swf + '" />' +
                        '<param name="wmode" value="transparent" />' +
                        '<param name="allowscriptaccess" value="always" />' +
                    '</object>';

                    container.html(html);

                })($wrap);

            // 压根就没有安转。
            } else {
                $wrap.html('<a href="http://www.adobe.com/go/getflashplayer" target="_blank" border="0"><img alt="get flash player" src="http://www.adobe.com/macromedia/style_guide/images/160x41_Get_Flash_Player.jpg" /></a>');
            }

            return;
        } else if (!WebUploader.Uploader.support()) {
            window.top.zinglabs.alert( 'Web Uploader 不支持您的浏览器！');
            return;
        }

        // 实例化
        uploader = WebUploader.create({
            pick: {
                id: '#filePicker',
                label: '选择文件',
                multiple:isMultiple
            },
            swf: 'js/Uploader.swf',
            server: '/'+window.top.PRJ_PATH+'/'+serverPath+'/UploadFile/upload.action?dataId='+dataId+'&maxSize='+maxSize+'&relativeKey='+relativeKey,
            // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
            //文件总数量限制
            fileNumLimit: limitCount,
            //文件总大小限制
            fileSizeLimit: limitSize,    // 200 M
            //单文件大小限制
            fileSingleSizeLimit: maxSize,    // 20 M
            //上传文件限制
            accept:maps[allowType]?maps[allowType]:maps.all
        });
        // 添加“添加文件”的按钮，
        uploader.on('ready', function() {
            window.uploader = uploader;
        });

        // 当有文件添加进来时执行，负责view的创建
        function addFile( file ) {
            var $li = $( '<li id="' + file.id + '">' +
                    '<p class="title">' + file.name + '</p>' +
                    '</li>' ),
                $btns = $('<div class="file-panel"><span class="cancel">删除</span></div>').appendTo( $li ),
                $prgress = $li.find('p.progress span'),
                $info = $('<span class="error"></span>'),
				$msg = $('<span class="msg"></span>'),
                showError = function( code ) {
                    switch( code ) {
                        case 'exceed_size':
                            text = '文件大小超出';
                            break;

                        case 'interrupt':
                            text = '上传暂停';
                            break;

                        default:
                            text = '上传失败';
                            break;
                    }
                    $msg.html("");
                    $info.text( text ).appendTo( $li );
                };
            if ( file.getStatus() === 'invalid' ) {
                showError( file.statusText );
            } else {
                percentages[ file.id ] = [ file.size, 0 ];
                file.rotation = 0;
               // $msg.text("等待上传").appendTo( $li );
            }
            file.on('statuschange', function( cur, prev ) {
                // 成功
                if ( cur === 'error' || cur === 'invalid' ) {
                    showError( file.statusText );
                    percentages[ file.id ][ 1 ] = 1;
                } else if ( cur === 'interrupt' ) {
                    showError( 'interrupt' );
                } else if ( cur === 'queued' ) {
                    percentages[ file.id ][ 1 ] = 0;
                } else if ( cur === 'progress' ) {
                	$progress.show();
                    $msg.text("上传中...")
                }else if ( cur === 'complete' ) {
                   $msg.text("已上传")
                }
                $li.removeClass( 'state-' + prev ).addClass( 'state-' + cur );
            });

            $btns.on( 'click', 'span', function() {
                var index = $(this).index();
                switch ( index ) {
                    case 0:
                        uploader.removeFile( file );
                        return;
                }
            });
            
            $li.appendTo( $queue );
        }
		// 负责view的销毁
        function removeFile( file ) {
            var $li = $('#'+file.id);
            delete percentages[ file.id ];
            updateTotalProgress();
            $li.off().find('.file-panel').off().end().remove();
        }
        function updateTotalProgress() {
            var loaded = 0,
                total = 0,
                spans = $progress.children(),
                percent;

            $.each( percentages, function( k, v ) {
                total += v[ 0 ];
                loaded += v[ 0 ] * v[ 1 ];
            } );

            percent = total ? loaded / total : 0;

            spans.eq( 0 ).text( Math.round( percent * 100 ) + '%' );
            spans.eq( 1 ).css( 'width', Math.round( percent * 100 ) + '%' );
        }

        function setState( val ) {
            var file, stats;

            if ( val === state ) {
                return;
            }

            $upload.removeClass( 'state-' + state );
            $upload.addClass( 'state-' + val );
            state = val;

            switch ( state ) {
                case 'pedding':
                    $queue.hide();
                    uploader.refresh();
                    break;

                case 'ready':
                    $queue.show();
                    uploader.refresh();
                    break;

                case 'uploading':
                    $upload.text( '暂停上传' );
                    break;

                case 'paused':
                    $upload.text( '继续上传' );
                    break;

                case 'confirm':
                    $upload.text( '开始上传' );
                    stats = uploader.getStats();
                    if ( stats.successNum && !stats.uploadFailNum ) {
                        setState( 'finish' );
                        return;
                    }
                    break;
                case 'finish':
                	$progress.hide();
                    stats = uploader.getStats();
                    if ( stats.successNum ) {
                        //上传成功后的回调
				    	if(reFillFn!=undefined||reFillFn!=null||reFillFn!=''){
				    		pwin[reFillFn](array);
				    	}
				    	$queue.html('');
				    	window.top. zinglabs.alert( '上传完成' );
                    } else {
                        // 没有成功的图片，重设
                        state = 'done';
                        location.reload();
                    }
                    break;
            }
        }

        uploader.onUploadProgress = function( file, percentage ) {
            var $li = $('#'+file.id),
            $percent = $li.find('.progress span');
            $percent.css( 'width', percentage * 100 + '%' );
            percentages[ file.id ][ 1 ] = percentage;
            updateTotalProgress();
        };

        uploader.onFileQueued = function( file ) {
            fileCount++;
            fileSize += file.size;

            if ( fileCount === 1 ) {
                $placeHolder.addClass( 'element-invisible' );
                $statusBar.show();
            }

            addFile( file );
            setState( 'ready' );
            updateTotalProgress();
        };

        uploader.onFileDequeued = function( file ) {
            fileCount--;
            fileSize -= file.size;

            if ( !fileCount ) {
                setState( 'pedding' );
            }

            removeFile( file );
            updateTotalProgress();

        };

        uploader.on( 'all', function( type ) {
            var stats;
            switch( type ) {
                case 'uploadFinished':
                    setState( 'confirm' );
                    break;

                case 'startUpload':
                    setState( 'uploading' );
                    break;

                case 'stopUpload':
                    setState( 'paused' );
                    break;

            }
        });
        //从服务器获取返回数据
		uploader.on( 'uploadAccept', function(object,ret) {
			delete ret._raw;
			ret.fileName=ascii2native(ret.fileName);
			ret.newFileName=ascii2native(ret.newFileName);
			array.push(ret);
        });
        uploader.onError = function( code ) {
        	var msg ='';
        	switch(code){
        		case 'F_DUPLICATE':
        		msg='上传文件重复';
        		break;
        		
        		case 'F_EXCEED_SIZE':
        		msg='超出上传文件大小限制';
        		break;
        		
        		case 'Q_EXCEED_SIZE_LIMIT':
        		msg='请删除原有附件，超出上传文件总大小限制。';
        		break;
        		
        		case 'Q_EXCEED_NUM_LIMIT':
        		msg='超出上传文件个数限制';
        		break;
        		
        		case 'Q_TYPE_DENIED':
        		msg='不允许上传的文件类型';
        		break;
        		
        		default:
        		msg='上传文件出错('+msg+')'
        	}
            window.top.zinglabs.alert( 'Eroor: ' + msg);
        };

        $upload.on('click', function() {
            if ( $(this).hasClass( 'disabled' ) ) {
                return false;
            }
            if ( state === 'ready' ) {
                uploader.upload();
            } else if ( state === 'paused' ) {
                uploader.upload();
            } else if ( state === 'uploading' ) {
                uploader.stop();
            }
        });

        $upload.addClass( 'state-' + state );
        updateTotalProgress();
    });

})( jQuery );