/**
 * web-upload 工具类
 *
 * 约定：
 * 上传按钮的id = 图片隐藏域id + 'BtnId'
 * 图片预览框的id = 图片隐藏域id + 'PreId'
 *
 * @author fengshuonan
 */
(function () {

    var $WebUpload = function (pictureId) {
        this.pictureId = pictureId;
        this.uploadBtnId = pictureId + "BtnId";
        this.uploadPreId = pictureId + "PreId";
        this.uploadUrl = Feng.ctxPath + '/mgr/upload';
        this.fileSizeLimit = 100 * 1024 * 1024;
        this.picThumbWidth = 800;
        this.picThumbHeight = 800;
        this.imagePreviewWidth = 100;
        this.imagePreviewHeight = 100;
        this.uploadBarId = null;
    };

    $WebUpload.prototype = {
        /**
         * 初始化webUploader
         */
        init: function () {
            var uploader = this.create();
            this.bindEvent(uploader);
            return uploader;
        },

        /**
         * 创建webuploader对象
         */
        create: function () {
            var webUploader = WebUploader.create({
                auto: true,
                pick: {
                    id: '#' + this.uploadBtnId,
                    multiple: false,// 只上传一个
                },
                accept: {
                    title: 'Images',
                    extensions: 'gif,jpg,jpeg,bmp,png',
                    mimeTypes: 'image/gif,image/jpg,image/jpeg,image/bmp,image/png'
                },
                swf: Feng.ctxPath
                + '/static/css/plugins/webuploader/Uploader.swf',
                disableGlobalDnd: true,
                duplicate: true,
                server: this.uploadUrl,
                fileSingleSizeLimit: this.fileSizeLimit
            });

            return webUploader;
        },

        /**
         * 绑定事件
         */
        bindEvent: function (bindedObj) {
            var me = this;
            bindedObj.on('fileQueued', function (file) {
                //var $link = $('<div><img width="' + me.imagePreviewWidth + 'px" height="' + me.imagePreviewHeight + 'px"></div>');
                var $link = $('<a class="example-image-link" data-lightbox="example-1" data-title="Optional caption."><img class="example-image" width="' + me.imagePreviewWidth + 'px" height="' + me.imagePreviewHeight + 'px" alt="image-1"></a>');

                var $img = $link.find('img');

                $("#" + me.uploadPreId).html($link);

                // 生成缩略图
                bindedObj.makeThumb(file, function (error, src) {
                    if (error) {
                        $img.replaceWith('<span>不能预览</span>');
                        return;
                    }
                    $img.attr('src', src);
                    $link.attr('href',src);
                }, me.picThumbWidth, me.picThumbHeight);
            });

            // 文件上传过程中创建进度条实时显示。
            bindedObj.on('uploadProgress', function (file, percentage) {
                $("#" + me.uploadBarId).css("width", percentage * 100 + "%");
            });

            // 文件上传成功，给item添加成功class, 用样式标记上传成功。
            bindedObj.on('uploadSuccess', function (file, response) {
                Feng.success("上传成功");
                $("#" + me.pictureId).val(response);
            });

            // 文件上传失败，显示上传出错。
            bindedObj.on('uploadError', function (file) {
                Feng.error("上传失败");
            });

            // 其他错误
            bindedObj.on('error', function (type) {
                if ("Q_EXCEED_SIZE_LIMIT" == type) {
                    Feng.error("文件大小超出了限制");
                } else if ("Q_TYPE_DENIED" == type) {
                    Feng.error("文件类型不满足");
                } else if ("Q_EXCEED_NUM_LIMIT" == type) {
                    Feng.error("上传数量超过限制");
                } else if ("F_DUPLICATE" == type) {
                    Feng.error("图片选择重复");
                } else {
                    Feng.error("上传过程中出错");
                }
            });

            // 完成上传完了，成功或者失败
            bindedObj.on('uploadComplete', function (file) {
            });
        },

        /**
         * 设置图片上传的进度条的id
         */
        setUploadBarId: function (id) {
            this.uploadBarId = id;
        },

        /**
         * 设置图片上传的Url
         */
        setUploadUrl: function (url) {
            this.uploadUrl = Feng.ctxPath + url;
        },

        /**
         * 设置图片预览/缩略图宽高
         */
        setImageAttr: function (imagePreviewWidth, imagePreviewHeight, picThumbWidth, picThumbHeight) {
            this.imagePreviewWidth = imagePreviewWidth;
            this.imagePreviewHeight = imagePreviewHeight;
            this.picThumbWidth=picThumbWidth;
            this.picThumbHeight=picThumbHeight;
        }
    };

    window.$WebUpload = $WebUpload;

}());