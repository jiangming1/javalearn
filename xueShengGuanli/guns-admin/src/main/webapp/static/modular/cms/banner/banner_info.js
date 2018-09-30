/**
 * 初始化轮播图详情对话框
 */
var BannerInfoDlg = {
    bannerInfoData: {}
};

/**
 * 清除数据
 */
BannerInfoDlg.clearData = function () {
    this.bannerInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BannerInfoDlg.set = function (key, val) {
    this.bannerInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BannerInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BannerInfoDlg.close = function () {
    parent.layer.close(window.parent.Banner.layerIndex);
}

/**
 * 收集数据
 */
BannerInfoDlg.collectData = function () {
    this
        .set('id')
        .set('authorId')
        .set('title')
        .set('image')
        .set('link')
        .set('weight')
    ;
}

/**
 * 提交添加
 */
BannerInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/banner/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Banner.table.refresh();
        BannerInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bannerInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
BannerInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/banner/update", function (data) {
        Feng.success("修改成功!");
        window.parent.Banner.table.refresh();
        BannerInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.bannerInfoData);
    ajax.start();
}

$(function () {
    var currentPageName = $("#currentPageName").val();
    if (currentPageName === 'banner_edit' || currentPageName === 'banner_add') {
        // 初始化图片上传
        var imageUp = new $WebUpload("image");
        imageUp.setUploadBarId("progressBar");
        imageUp.setImageAttr(300,100,1200,400);
        imageUp.setUploadUrl('/banner/upload');
        imageUp.init();
    }
});
