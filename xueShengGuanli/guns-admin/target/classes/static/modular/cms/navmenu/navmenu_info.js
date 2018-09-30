/**
 * 初始化导航菜单详情对话框
 */
var NavmenuInfoDlg = {
    navmenuInfoData : {}
};

/**
 * 清除数据
 */
NavmenuInfoDlg.clearData = function() {
    this.navmenuInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
NavmenuInfoDlg.set = function(key, val) {
    this.navmenuInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
NavmenuInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
NavmenuInfoDlg.close = function() {
    parent.layer.close(window.parent.Navmenu.layerIndex);
}

/**
 * 收集数据
 */
NavmenuInfoDlg.collectData = function() {
    this.set('id').set('ancestorId').set('descendantId').set('descendantName').set('descendantLink').set('descendantWeight');
}

/**
 * 提交添加
 */
NavmenuInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/navmenu/add", function(data){
        Feng.success("添加成功!");
        window.parent.Navmenu.table.refresh();
        NavmenuInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.navmenuInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
NavmenuInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/navmenu/update", function(data){
        Feng.success("修改成功!");
        window.parent.Navmenu.table.refresh();
        NavmenuInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.navmenuInfoData);
    ajax.start();
}

function onBodyDownNavmenu(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "navmenuMenuContent" || $(
            event.target).parents("#navmenuMenuContent").length > 0)) {
        NavmenuInfoDlg.hideNavmenuSelectTree();
    }
}

NavmenuInfoDlg.hideNavmenuSelectTree = function () {
    $("#navmenuMenuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDownNavmenu);// mousedown当鼠标按下就可以触发，不用弹起
};

NavmenuInfoDlg.showNavmenuSelectTree = function (currentPage) {
    var $left = ($("#ancestorName").offset().left) + "px";
    var $top = ($("#ancestorName").offset().top + 32) + "px";
    $("#navmenuMenuContent").css({
        left: $left,
        top: $top
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDownNavmenu);
};


NavmenuInfoDlg.onCheckNavmenu = function (e, treeId, treeNode) {
    $("#ancestorName").attr("value", navmenuInstance.getCheckedValues(true));
    $("#ancestorId").attr("value", navmenuInstance.getCheckedIds(true));
};

$(function() {
    var navmenuZTree;
    var currentPageName = $("#currentPageName").val();
    switch (currentPageName) {
        case 'navmenu_add':
        case 'navmenu_edit':
            navmenuZTree = new $ZTree("navmenuTreeDemo", "/navmenu/tree", 'useRadio');
            navmenuZTree.bindOnCheck(NavmenuInfoDlg.onCheckNavmenu);
            break;
    }

    navmenuZTree.init();
    navmenuInstance = navmenuZTree;
});
