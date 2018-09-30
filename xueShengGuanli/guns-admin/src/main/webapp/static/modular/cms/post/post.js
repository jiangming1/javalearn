/**
 * 文章管理管理初始化
 */
var Post = {
    id: "PostTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    currentPageName: null
};

/**
 * 初始化表格的列
 */
Post.initColumn = function () {
    return [
        {
            field: 'selectItem',
            checkbox: true,
        },
        /*        {field: 'selectItem', radio: true},*/
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '标题', field: 'title', visible: true, align: 'center', valign: 'middle'},
        {
            title: '内容',
            field: 'content',
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value) {
                return value.toString().replace(/<[^>]+>/g, "").replace(/&.+?;/g,"").substr(0, 30) + '...';
            }
        },
        {title: '作者', field: 'author', visible: true, align: 'center', valign: 'middle'},
        {
            title: '分类',
            field: 'categories',
            visible: true,
            align: 'center',
            valign: 'middle',
            formatter: function (value) {
                if (value[0]) {
                    if (typeof value[0] === 'string') {
                        return value.toString();
                    } else if (typeof value[0] === 'object') {
                        var categoryArr = [];
                        for (var i = value.length; i-- > 0;) {
                            categoryArr.push(value[i].category);
                        }
                        return categoryArr.toString();
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        },
        {
            title: '标签',
            field: 'tags',
            visible: false,
            align: 'center',
            valign: 'middle',
            formatter: function (value) {
                if (value[0]) {
                    if (typeof value[0] === 'string') {
                        return value.toString();
                    } else if (typeof value[0] === 'object') {
                        var tagArr = [];
                        for (var i = value.length; i-- > 0;) {
                            tagArr.push(value[i].tag);
                        }
                        return tagArr.toString();
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        },
        {title: '发表时间', field: 'createTime', visible: true, align: 'center', valign: 'middle',
            formatter: function (value) {
                return new Date(value).myDateFormat("yyyy-MM-dd hh:mm:ss");
            }},
        {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle',
            formatter: function (value) {
                return new Date(value).myDateFormat("yyyy-MM-dd hh:mm:ss");
            }},
        {title: '状态', field: 'status', visible: true, align: 'center', valign: 'middle'},
        {title: '权重', field: 'weight', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Post.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Post.seItem = selected;
        return true;
    }
};

/**
 * 点击添加文章管理
 */
Post.openAddPost = function () {
    var index = layer.open({
        type: 2,
        title: '添加文章管理',
        area: ['100%', '100%'], //宽高
        fix: true, //固定
        maxmin: false,
        scrollbar: false,
        content: Feng.ctxPath + '/post/post_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看文章管理详情
 */
Post.openPostDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '文章管理详情',
            area: ['100%', '100%'], //宽高
            fix: true, //固定
            maxmin: false, //关闭最大化最小化按钮
            scrollbar: false,
            content: Feng.ctxPath + '/post/post_update/' + Post.seItem[0].id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除文章管理
 */
Post.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/post/delete", function (data) {
            Feng.success("删除成功!");
            Post.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        var postIds = [];
        this.seItem.forEach(function (item) {
            postIds.push(item.id);
        });
        Feng.setContentType("application/json;charset=utf-8");
        Feng.sessionTimeoutRegistry();
        ajax.data = JSON.stringify(postIds);
        ajax.start();
    }
};

/**
 * 移动文章到回收站
 */
Post.trash = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/post/trash", function (data) {
            Feng.success("移动成功!");
            Post.table.refresh();
            var iframe = window.parent.frames['post_recycle_bin'];
            if (iframe) {
                iframe.Post.table.refresh();
            }
        }, function (data) {
            Feng.error("移动失败!" + data.responseJSON.message + "!");
        });
        var postIds = [];
        this.seItem.forEach(function (item) {
            postIds.push(item.id);
        });
        Feng.setContentType("application/json;charset=utf-8");
        Feng.sessionTimeoutRegistry();
        ajax.data = JSON.stringify(postIds);
        ajax.start();
    }
};

/**
 * 从回收站恢复文章
 */
Post.recoverPost = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/post/recover_post", function (data) {
            Feng.success("恢复成功!");
            Post.table.refresh();
            var iframe = window.parent.frames['post'];
            if (iframe) {
                iframe.Post.table.refresh();
            }
        }, function (data) {
            Feng.error("恢复失败!" + data.responseJSON.message + "!");
        });
        var postIds = [];
        this.seItem.forEach(function (item) {
            postIds.push(item.id);
        });
        Feng.setContentType("application/json;charset=utf-8");
        Feng.sessionTimeoutRegistry();
        ajax.data = JSON.stringify(postIds);
        ajax.start();
    }
};

Post.resetSearch = function () {
    $("#title").val("");
    if ($("#isAuthor").val()==='false') {
        $("#author").val("");
    }
    $("#category").attr("value", "");
    $("#tag").val("");
    if (Post.currentPageName !== 'post_recycle_bin') {
        $("#status").attr("value", "");
    }
    $("#beginTime").val("");
    $("#endTime").val("");
    Post.search();
}

/**
 * 查询文章管理列表
 */
Post.search = function () {
    function trim(str) {
        return str ? str.trim() : '';
    }

    var queryData = {};
    queryData['title'] = trim($("#title").val());
    queryData['author'] = trim($("#author").val());
    queryData['category'] = trim($("#category").val());
    queryData['tag'] = trim($("#tag").val());
    queryData['status'] = trim($("#status").val());
    queryData['beginTime'] = trim($("#beginTime").val());
    queryData['endTime'] = trim($("#endTime").val());
    Post.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Post.initColumn();
    var table = new BSTable(Post.id, "/post/list", defaultColunms, "application/json");
    table.setPaginationType("server");
    Post.table = table.init();
    Post.currentPageName = $("#currentPageName").val();

    if (Post.currentPageName === 'post_recycle_bin') {
        var queryData = {};
        queryData['status'] = '回收';
        table.refresh({query: queryData});
    }

    $('#PostTable').on('load-success.bs.table', function (e, data) {
        if (data.total && !data.rows.length) {
            $('#PostTable').bootstrapTable('prevPage').bootstrapTable('refresh');
            if (data.total == 0) {
                $('#PostTable').off('load-success.bs.table');
            }

        }
    });

    var currentPageName = $("#currentPageName").val();
    switch (currentPageName) {
        case 'post':
            window.name = 'post';
            break;
        case 'post_recycle_bin':
            window.name = 'post_recycle_bin';
            break;
    }

});
