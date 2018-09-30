/**
 * 初始化分类详情对话框
 */
var CategoryInfoDlg = {
    categoryInfoData: {}
};

/**
 * 清除数据
 */
CategoryInfoDlg.clearData = function () {
    this.categoryInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CategoryInfoDlg.set = function (key, val) {
    this.categoryInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
CategoryInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
CategoryInfoDlg.close = function () {
    parent.layer.close(window.parent.Category.layerIndex);
}

/**
 * 收集数据
 */
CategoryInfoDlg.collectData = function () {
    this.set('id').set('ancestorId').set('descendantId').set('descendantName');
}

/**
 * 提交添加
 */
CategoryInfoDlg.addSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/category/add", function (data) {
        if(data.code===500){
            Feng.error("添加失败! " + data.message + "!");
        }else{
            Feng.success("添加成功!");
        }
        window.parent.Category.table.refresh();
        CategoryInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.categoryInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
CategoryInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/category/update", function (data) {
        Feng.success("修改成功!");
        window.parent.Category.table.refresh();
        CategoryInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.categoryInfoData);
    ajax.start();
}

function onBodyDownCategory(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "categoryMenuContent" || $(
            event.target).parents("#categoryMenuContent").length > 0)) {
        CategoryInfoDlg.hideCategorySelectTree();
    }
}

CategoryInfoDlg.hideCategorySelectTree = function () {
    $("#categoryMenuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDownCategory);// mousedown当鼠标按下就可以触发，不用弹起
};

CategoryInfoDlg.showCategorySelectTree = function (currentPage) {
    var $left = ($("#ancestorName").offset().left) + "px";
    var $top = ($("#ancestorName").offset().top + 32) + "px";
    $("#categoryMenuContent").css({
        left: $left,
        top: $top
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDownCategory);
};

CategoryInfoDlg.onCheckCategory = function (e, treeId, treeNode) {
    $("#ancestorName").attr("value", categoryInstance.getCheckedValues(true));
    $("#ancestorId").attr("value", categoryInstance.getCheckedIds(true));
};

$(function () {
    var categoryZTree;
    var currentPageName = $("#currentPageName").val();
    switch (currentPageName) {
        case 'category_add':
        case 'category_edit':
            categoryZTree = new $ZTree("categoryTreeDemo", "/category/tree", 'useRadio');
            categoryZTree.bindOnCheck(CategoryInfoDlg.onCheckCategory);
            break;
    }

    categoryZTree.init();
    categoryInstance = categoryZTree;
});
