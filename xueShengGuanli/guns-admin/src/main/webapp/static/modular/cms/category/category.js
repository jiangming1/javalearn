/**
 * 分类管理初始化
 */
var Category = {
    id: "CategoryTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Category.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '父节点id', field: 'pId', visible: true, align: 'center', valign: 'middle'},
        {title: '父节点名称', field: 'parentName', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Category.check = function () {
    var selected = $('#' + this.id).bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Category.seItem = selected[0];

        return true;
    }
};

/**
 * 点击添加分类
 */
Category.openAddCategory = function () {
    var index = layer.open({
        type: 2,
        title: '添加分类',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/category/category_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看分类详情
 */
Category.openCategoryDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '分类详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/category/category_update/' + Category.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除分类
 */
Category.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/category/delete", function (data) {
            Feng.success("删除成功!");
            Category.table.refresh();
            var iframe = window.parent.frames['post'];
            if (iframe) {
                iframe.Post.table.refresh();
            }
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("descendantId", this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询分类列表
 */
Category.search = function () {
    var queryData = {};
    queryData['descendantName'] = $("#descendantName").val();
    Category.table.refresh({query: queryData});
};

Category.resetSearch = function () {
    $("#descendantName").val("");
    Category.search();
}

$(function () {
    var defaultColunms = Category.initColumn();
    var table = new BSTreeTable(Category.id, "/category/list", defaultColunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("pId");
    table.setExpandAll(true);
    table.init();
    Category.table = table;
});
