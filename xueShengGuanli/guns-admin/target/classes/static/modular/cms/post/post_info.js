/**
 * 初始化文章管理详情对话框
 */
var PostInfoDlg = {
    postInfoData: {}
};

/**
 * 清除数据
 */
PostInfoDlg.clearData = function () {
    this.postInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PostInfoDlg.set = function (key, val) {
    if (key === "content") {
        this.postInfoData["content"] = ue.getContent();
    } else if (key === "categoryIds" || key === "oldCategoryIds") {
        if ((typeof val == "undefined")) {
            if ($("#" + key).val()) {
                this.postInfoData[key] = $("#" + key).val().split(',');
            }
        } else {
            this.postInfoData[key] = val;
        }
    } else if (key === "coverImage") {
        if (typeof val == "undefined") {
            var coverImageValue = $("#" + key).val();
            if (coverImageValue === Feng.ctxPath + "/static/img/banner-upload.png") {
                this.postInfoData[key] = "";
            } else {
                this.postInfoData[key] = coverImageValue;
            }
        } else {
            this.postInfoData[key] = val;
        }
    } else {
        this.postInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    }
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PostInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
PostInfoDlg.close = function () {
    parent.layer.close(window.parent.Post.layerIndex);
}

/**
 * 收集数据
 */
PostInfoDlg.collectData = function () {
    this
        .set('id')
        .set('title')
        .set('content')
        .set('author')
        .set('authorId')
        .set('categories')
        .set('categoryIds')
        .set('oldCategoryIds')
        .set('tags')
        .set('status')
        .set('weight')
        .set('coverImage')
    ;
}

/**
 * 提交添加
 */
PostInfoDlg.addSubmit = function () {
    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/post/add", function (data) {
        Feng.success("添加成功!");
        window.parent.Post.table.refresh();
        PostInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });

    var data = JSON.stringify(this.postInfoData);
    Feng.setContentType("application/json;charset=utf-8");
    Feng.sessionTimeoutRegistry();
    ajax.setData(data);
    ajax.start();
}

function onBodyDownCategory(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "categoryMenuContent" || $(
        event.target).parents("#categoryMenuContent").length > 0)) {
        PostInfoDlg.hideCategorySelectTree();
    }
}

/**
 * 隐藏选择的树
 */
PostInfoDlg.hideCategorySelectTree = function () {
    $("#categoryMenuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDownCategory);// mousedown当鼠标按下就可以触发，不用弹起
};

function onBodyDownStatus(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "statusMenuContent" || $(
        event.target).parents("#statusMenuContent").length > 0)) {
        PostInfoDlg.hideStatusSelectTree();
    }
}

/**
 * 隐藏选择的树
 */
PostInfoDlg.hideStatusSelectTree = function () {
    $("#statusMenuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDownStatus);// mousedown当鼠标按下就可以触发，不用弹起
};

/**
 * 显示分类选择的树
 *
 * @returns
 */
PostInfoDlg.showCategorySelectTree = function (currentPage) {
    var $left = "";
    var $top = "";
    if (currentPage === 'post') {
        $left = ($("#category").offset().left - 26) + "px";
    } else {
        //$currentPageName === 'post_edit' 或者 'post_add'
        $left = ($("#author").offset().left) + "px";
        $top = ($("#container").offset().top - 16) + "px";
        if ($(window).width() < 761) {
            $top = ($("#tags").offset().top - 24) + "px";
        }
    }
    $("#categoryMenuContent").css({
        left: $left,
        top: $top
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDownCategory);
};

/**
 * 显示状态选择的树
 *
 * @returns
 */
PostInfoDlg.showStatusSelectTree = function ($currentPageName) {
    var statusObj = $("#status");
    var statusOffset = $("#status").offset();
    var $left = "";
    var $top = "";
    if ($currentPageName === 'post') {
        $left = (statusOffset.left) - 25 + "px";
    } else {
        //$currentPageName === 'post_edit' 或者 'post_add'
        $left = statusOffset.left + "px";
        $top = statusOffset.top + statusObj.outerHeight() + "px";
    }
    $("#statusMenuContent").css({
        left: $left,
        top: $top
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDownStatus);
};

/**
 * 提交修改
 */
PostInfoDlg.editSubmit = function () {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/post/update", function (data) {
        Feng.success("修改成功!");
        window.parent.Post.table.refresh();
        PostInfoDlg.close();
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });

    var data = JSON.stringify(this.postInfoData);
    Feng.setContentType("application/json;charset=utf-8");
    Feng.sessionTimeoutRegistry();
    ajax.setData(data);
    ajax.start();
}

PostInfoDlg.delCoverImage = function () {
    $("#coverImage").val("");
    $("#coverImagePreId").children("a:first-child").attr("href", Feng.ctxPath + "/static/img/banner-upload.png");
    $("#coverImagePreId").children("a:first-child").children("img:first-child").attr("src", Feng.ctxPath + "/static/img/banner-upload.png");
}

/**
 * 点击分类input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
PostInfoDlg.onCheckCategories = function (e, treeId, treeNode) {
    function trim(str) {
        return str ? str.trim() : '';
    }

    $("#categories").attr("value", trim(categoryInstance.getCheckedValues()));
    $("#categoryIds").attr("value", trim(categoryInstance.getCheckedIds()));
};

/**
 * 点击分类input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
PostInfoDlg.onCheckCategory = function (e, treeId, treeNode) {
    $("#category").attr("value", categoryInstance.getCheckedValues().trim());
};

/**
 * 点击状态input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
PostInfoDlg.onCheckStatus = function (e, treeId, treeNode) {
    $("#status").attr("value", statusInstance.getCheckedValues().trim());
};

$(function () {

    var categoryZTree;
    var currentPageName = $("#currentPageName").val();
    switch (currentPageName) {
        case 'post':
        case 'post_recycle_bin':
            categoryZTree = new $ZTree("categoryTreeDemo", "/category/tree", 'useRadio');
            categoryZTree.bindOnCheck(PostInfoDlg.onCheckCategory);
            break;
        case 'post_add':
            categoryZTree = new $ZTree("categoryTreeDemo", "/category/tree", 'useCheckbox');
            categoryZTree.bindOnCheck(PostInfoDlg.onCheckCategories);
            break;
        case 'post_edit' :
            var postId = $("#id").val();
            categoryZTree = new $ZTree("categoryTreeDemo", "/category/tree/" + postId, 'useCheckbox');
            categoryZTree.bindOnCheck(PostInfoDlg.onCheckCategories);
            break;
    }

    categoryZTree.init();
    categoryInstance = categoryZTree;

    var zNodes = [
        {id: 1, pId: 0, name: "发布", open: true, checked: false},
        {id: 2, pId: 0, name: "草稿", open: true, checked: false},
        /*{id: 3, pId: 0, name: "回收", open: true, checked: false}*/
    ];

    var currentStatus = $("#status").val();
    switch (currentStatus) {
        case '发布':
            zNodes[0].checked = true;
            zNodes[1].checked = false;
            /*zNodes[2].checked = false*/
            break;
        case '草稿':
            zNodes[0].checked = false;
            zNodes[1].checked = true;
            /*zNodes[2].checked = false*/
            break;
        /*       case '回收':
                   zNodes[0].checked = false;
                   zNodes[1].checked = false;
                   zNodes[2].checked = true
                   break;*/
    }

    var statusZTree = new $ZTree("statusTreeDemo", null, 'useRadio');
    statusZTree.bindOnCheck(PostInfoDlg.onCheckStatus);
    statusZTree.setzNodes(zNodes);
    statusZTree.init();
    statusInstance = statusZTree;

    // 初始化图片上传
    var imageUp = new $WebUpload("coverImage");
    imageUp.setUploadBarId("progressBar");
    imageUp.setImageAttr(400, 300, 800, 800);
    imageUp.setUploadUrl('/post/upload');
    imageUp.init();

});
