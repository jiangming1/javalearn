/**
 * 导航菜单管理初始化
 */
var Navmenu = {
    id: "NavmenuTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Navmenu.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '链接', field: 'link', visible: true, align: 'center', valign: 'middle'},
        {title: '父节点id', field: 'pId', visible: true, align: 'center', valign: 'middle'},
        {title: '父节点名称', field: 'parentName', visible: true, align: 'center', valign: 'middle'},
        {title: '权重', field: 'weight', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Navmenu.check = function () {
    var selected = $('#' + this.id).bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Navmenu.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加导航菜单
 */
Navmenu.openAddNavmenu = function () {
    var index = layer.open({
        type: 2,
        title: '添加导航菜单',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/navmenu/navmenu_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看导航菜单详情
 */
Navmenu.openNavmenuDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '导航菜单详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/navmenu/navmenu_update/' + Navmenu.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除导航菜单
 */
Navmenu.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/navmenu/delete", function (data) {
            Feng.success("删除成功!");
            Navmenu.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("descendantId", this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询导航菜单列表
 */
Navmenu.search = function () {
    var queryData = {};
    queryData['descendantName'] = $("#descendantName").val();
    Navmenu.table.refresh({query: queryData});
};

Navmenu.resetSearch = function () {
    $("#descendantName").val("");
    Navmenu.search();
}

$(function () {
    var defaultColunms = Navmenu.initColumn();
    var table = new BSTreeTable(Navmenu.id, "/navmenu/list", defaultColunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("pId");
    table.setExpandAll(true);
    table.init();
    Navmenu.table = table;
});
