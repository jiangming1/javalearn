/**
 * 轮播图管理初始化
 */
var Banner = {
    id: "BannerTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Banner.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '作者', field: 'author', visible: true, align: 'center', valign: 'middle'},
        {title: '标题', field: 'title', visible: true, align: 'center', valign: 'middle'},
        {
            title: '图片', field: 'image', visible: true, align: 'center', valign: 'middle',
            formatter: function (value) {
                return '<a class="example-image-link" href="' + value + '" data-lightbox="example-1"><img class="example-image" src="' + value + '"  width="100px" height="70.3125px" /></a>';
            }
        },
        {title: '链接', field: 'link', visible: true, align: 'center', valign: 'middle'},
        {
            title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle',
            formatter: function (value) {
                return new Date(value).myDateFormat("yyyy-MM-dd hh:mm:ss");
            }
        },
        {
            title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle',
            formatter: function (value) {
                return new Date(value).myDateFormat("yyyy-MM-dd hh:mm:ss");
            }
        },
        {title: '权重', field: 'weight', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Banner.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Banner.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加轮播图
 */
Banner.openAddBanner = function () {
    var index = layer.open({
        type: 2,
        title: '添加轮播图',
        area: ['100%', '100%'], //宽高
        fix: true, //不固定
        maxmin: false, //开启最大化最小化按钮
        content: Feng.ctxPath + '/banner/banner_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看轮播图详情
 */
Banner.openBannerDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '轮播图详情',
            area: ['100%', '100%'], //宽高
            fix: true, //不固定
            maxmin: false, //开启最大化最小化按钮
            content: Feng.ctxPath + '/banner/banner_update/' + Banner.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除轮播图
 */
Banner.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/banner/delete", function (data) {
            Feng.success("删除成功!");
            Banner.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("bannerId", this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询轮播图列表
 */
Banner.search = function () {
    var queryData = {};
    queryData['title'] = $("#title").val();
    Banner.table.refresh({query: queryData});
};

Banner.resetSearch = function () {
    $("#title").val("");
    Banner.search();
}

$(function () {
    var defaultColunms = Banner.initColumn();
    var table = new BSTable(Banner.id, "/banner/list", defaultColunms);
    table.setPaginationType("server");
    Banner.table = table.init();

});
