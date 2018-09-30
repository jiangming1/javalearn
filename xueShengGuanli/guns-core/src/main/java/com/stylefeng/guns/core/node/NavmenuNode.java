package com.stylefeng.guns.core.node;

public class NavmenuNode {

    private Integer id;    //节点id

    private Integer pId;//父节点id

    private String name;//节点名称

    private String parentName; //父节点名称

    private String link;

    private Integer weight;

    private Boolean open;//是否打开节点

    private Boolean checked;//是否被选中

    public static NavmenuNode createParent() {
        NavmenuNode zTreeNode = new NavmenuNode();
        zTreeNode.setChecked(true);
        zTreeNode.setId(0);
        zTreeNode.setName("顶级");
        zTreeNode.setOpen(true);
        zTreeNode.setpId(0);
        return zTreeNode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getIsOpen() {
        return open;
    }

    public void setIsOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
