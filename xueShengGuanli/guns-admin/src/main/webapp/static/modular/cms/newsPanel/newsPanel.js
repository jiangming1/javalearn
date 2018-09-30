/**
 * 新闻面板管理初始化
 */
var NewsPanel = {
    id: "NewsPanelTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
NewsPanel.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主键id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '英文名称', field: 'englishName', visible: true, align: 'center', valign: 'middle'},
            {title: '分类', field: 'category', visible: true, align: 'center', valign: 'middle'},
            {title: '权重', field: 'weight', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
NewsPanel.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        NewsPanel.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加新闻面板
 */
NewsPanel.openAddNewsPanel = function () {
    var index = layer.open({
        type: 2,
        title: '添加新闻面板',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/newsPanel/newsPanel_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看新闻面板详情
 */
NewsPanel.openNewsPanelDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '新闻面板详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/newsPanel/newsPanel_update/' + NewsPanel.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除新闻面板
 */
NewsPanel.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/newsPanel/delete", function (data) {
            Feng.success("删除成功!");
            NewsPanel.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("newsPanelId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询新闻面板列表
 */
NewsPanel.search = function () {
    var queryData = {};
    queryData['name'] = $("#name").val();
    NewsPanel.table.refresh({query: queryData});
};

NewsPanel.resetSearch = function () {
    $("#name").val("");
    NewsPanel.search();
}

$(function () {
    var defaultColunms = NewsPanel.initColumn();
    var table = new BSTable(NewsPanel.id, "/newsPanel/list", defaultColunms);
    table.setPaginationType("client");
    NewsPanel.table = table.init();
});
