/**
 * 初始化新闻面板详情对话框
 */
var NewsPanelInfoDlg = {
    newsPanelInfoData: {}
};

/**
 * 清除数据
 */
NewsPanelInfoDlg.clearData = function () {
    this.newsPanelInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
NewsPanelInfoDlg.set = function (key, val) {
    this.newsPanelInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
NewsPanelInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
NewsPanelInfoDlg.close = function () {
    parent.layer.close(window.parent.NewsPanel.layerIndex);
}

/**
 * 收集数据
 */
NewsPanelInfoDlg.collectData = function () {
    this
        .set('id')
        .set('name')
        .set('englishName')
        .set('categoryId')
        .set('weight')
    ;
}

/**
 * 提交添加
 */
NewsPanelInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/newsPanel/add", function (data) {
        Feng.success("添加成功!");
        window.parent.NewsPanel.table.refresh();
        NewsPanelInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.newsPanelInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
NewsPanelInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/newsPanel/update", function (data) {
        Feng.success("修改成功!");
        window.parent.NewsPanel.table.refresh();
        NewsPanelInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.newsPanelInfoData);
    ajax.start();
}

function onBodyDownCategory(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "categoryMenuContent" || $(
            event.target).parents("#categoryMenuContent").length > 0)) {
        NewsPanelInfoDlg.hideCategorySelectTree();
    }
}

NewsPanelInfoDlg.hideCategorySelectTree = function () {
    $("#categoryMenuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDownCategory);// mousedown当鼠标按下就可以触发，不用弹起
};

NewsPanelInfoDlg.showCategorySelectTree = function (currentPage) {
    var $left = ($("#name").offset().left-25) + "px";
    var $top = ($("#categoryId").offset().top + 32) + "px";
    $("#categoryMenuContent").css({
        left: $left,
        top: $top
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDownCategory);
};

NewsPanelInfoDlg.onCheckCategory = function (e, treeId, treeNode) {
    $("#category").attr("value", categoryInstance.getCheckedValues(false));
    $("#categoryId").attr("value", categoryInstance.getCheckedIds(false));
};

$(function () {
    var categoryZTree;
    var currentPageName = $("#currentPageName").val();
    switch (currentPageName) {
        case 'newsPanel_add':
        case 'newsPanel_edit':
            categoryZTree = new $ZTree("categoryTreeDemo", "/category/tree", 'useRadio');
            categoryZTree.bindOnCheck(NewsPanelInfoDlg.onCheckCategory);
            break;
    }

    categoryZTree.init();
    categoryInstance = categoryZTree;
});
