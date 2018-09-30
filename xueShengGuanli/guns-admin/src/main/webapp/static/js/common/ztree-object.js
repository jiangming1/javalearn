/**
 * ztree插件的封装
 */
(function () {

    var $ZTree = function (id, url, useCheckboxOrRadio) {
        this.id = id;
        this.url = url;
        this.useCheckboxOrRadio = useCheckboxOrRadio;
        this.onClick = null;
        this.ondblclick = null;
        this.onCheck = null;
        this.settings = null;
        this.zNodes = null;
    };

    $ZTree.prototype = {
        /**
         * 初始化ztree的设置
         */
        initSetting: function () {
            var settings = {
                view: {
                    dblClickExpand: true,
                    selectedMulti: false
                },
                data: {simpleData: {enable: true}},
                callback: {
                    onClick: this.onClick,
                    onDblClick: this.ondblclick
                }
            };
            return settings;
        },
        checkBoxSetting: function () {
            var settings = {
                check: {
                    enable: true,//设置zTree的节点上是否显示checkbox框，默认值: false
                    chkboxType: {"Y": "", "N": ""}
                    // chkboxType :{ "Y" : "p", "N" : "s" } //Y:勾选（参数：p:影响父节点），N：不勾（参数s：影响子节点）[p 和 s 为参数]
                },
                data: {
                    simpleData: {
                        enable: true//使用简单模式
                    }
                },
                key: {
                    checked: "checked"//zTree 节点数据中保存check状态的属性名称。默认值："checked"
                },
                view: {
                    selectedMulti: true//允许选多个
                },
                callback: {
                    onCheck: this.onCheck
                }
            };
            return settings;
        },
        radioSetting: function () {
            var settings = {
                check: {
                    enable: true,//设置zTree的节点上是否显示radio框，默认值: false
                    chkStyle: "radio", radioType: "all" //整棵树当成一个分组
                },
                data: {
                    simpleData: {
                        enable: true//使用简单模式
                    }
                },
                view: {
                    selectedMulti: false//允许选多个
                },
                callback: {
                    onCheck: this.onCheck
                }
            };
            return settings;
        },

        /**
         * 手动设置ztree的设置
         */
        setSettings: function (val) {
            this.settings = val;
        },

        /**
         * 手动设置zNodes节点数据数组
         */
        setzNodes: function (val) {
            this.zNodes = val;
        },

        /**
         * 初始化ztree
         */
        init: function () {
            var zNodeSeting = null;
            if (this.settings != null) {
                zNodeSeting = this.settings;
            } else if (this.useCheckboxOrRadio === 'useCheckbox' || this.useCheckboxOrRadio === 'checkbox') {
                zNodeSeting = this.checkBoxSetting();
            } else if (this.useCheckboxOrRadio === 'useRadio' || this.useCheckboxOrRadio === 'radio') {
                zNodeSeting = this.radioSetting();
            }
            else {
                zNodeSeting = this.initSetting();
            }
            var zNodes;
            if (this.zNodes) {
                zNodes = this.zNodes;
            } else {
                zNodes = this.loadNodes();
            }
            $.fn.zTree.init($("#" + this.id), zNodeSeting, zNodes);
            var zTreeObj = $.fn.zTree.getZTreeObj(this.id);
            return zTreeObj;
        },

        /**
         * 绑定onclick事件
         */
        bindOnClick: function (func) {
            this.onClick = func;
        },
        /**
         * 绑定双击事件
         */
        bindOnDblClick: function (func) {
            this.ondblclick = func;
        },
        /**
         * 绑定选中事件
         */
        bindOnCheck: function (func) {
            this.onCheck = func;
        },

        /**
         * 加载节点
         */
        loadNodes: function () {
            var zNodes = null;
            var ajax = new $ax(Feng.ctxPath + this.url, function (data) {
                zNodes = data;
            }, function (data) {
                Feng.error("加载ztree信息失败!");
            });
            ajax.start();
            return zNodes;
        },

        /**
         * 获取选中的值
         */
        getSelectedVal: function () {
            var zTree = $.fn.zTree.getZTreeObj(this.id);
            var nodes = zTree.getSelectedNodes();
            return nodes[0].name;
        },
        /**
         * 获取单选框/复选框选中的值
         */
        getCheckedValues: function (containsRoot) {
            containsRoot = containsRoot || false;
            var zTree = $.fn.zTree.getZTreeObj(this.id);
            var nodes = zTree.getCheckedNodes(true);
            var checkedValues = [];
            for (var i = 0, len = nodes.length; len-- > 0; i++) {
                if (containsRoot) {
                    checkedValues.push(nodes[i].name);
                } else {
                    if ((nodes[i].name !== '顶级')) {
                        checkedValues.push(nodes[i].name);
                    }
                }
            }
            return checkedValues.toString();
        },
        /**
         * 获取单选框选中的Id/复选框选中的Ids
         */
        getCheckedIds: function (containsRoot) {
            containsRoot = containsRoot || false;
            var zTree = $.fn.zTree.getZTreeObj(this.id);
            var nodes = zTree.getCheckedNodes(true);
            var checkedIds = [];
            for (var i = 0, len = nodes.length; len-- > 0; i++) {
                if (containsRoot) {
                    checkedIds.push(nodes[i].id);
                } else {
                    if (nodes[i].id !== 0) {
                        checkedIds.push(nodes[i].id);
                    }
                }
            }
            return checkedIds.toString();
        }
    };

    window.$ZTree = $ZTree;

}());