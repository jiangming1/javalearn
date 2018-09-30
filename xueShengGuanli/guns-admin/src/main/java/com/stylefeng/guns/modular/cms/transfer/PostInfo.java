package com.stylefeng.guns.modular.cms.transfer;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * PostInfo
 * 只用来绑定和接收application/json格式的输入参数
 * </p>
 *
 * @author zhoujin7
 * @since 2017-12-02
 */
public class PostInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 作者
     */
    private String author;
    /**
     * 作者id
     */
    private Integer authorId;
    /**
     * 分类
     */
    private String category;
    /**
     * 分类
     */
    private List<String> categories;
    /**
     * 分类Ids
     */
    private List<Integer> categoryIds;
    /**
     * 旧的分类Ids
     */
    private List<Integer> oldCategoryIds;
    /**
     * 标签
     */
    private String tag;
    /**
     * 标签
     */
    private List<String> tags;
    /**
     * 标签Ids
     */
    private List<Integer> tagIds;
    /**
     * 发表时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 状态
     */
    private String status;
    /**
     * 封面图片
     */
    private String coverImage;
    /**
     * 权重
     */
    private Integer weight;

    /**
     * 开始时间(按时间段查找)
     */
    private Date beginTime;
    /**
     * 结束时间(按时间段查找)
     */
    private Date endTime;

    private Integer limit;

    private Integer offset;

    private String sort;

    private String order;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<Integer> getOldCategoryIds() {
        return oldCategoryIds;
    }

    public void setOldCategoryIds(List<Integer> oldCategoryIds) {
        this.oldCategoryIds = oldCategoryIds;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "PostInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", authorId=" + authorId +
                ", category='" + category + '\'' +
                ", categories=" + categories +
                ", categoryIds=" + categoryIds +
                ", oldCategoryIds=" + oldCategoryIds +
                ", tag='" + tag + '\'' +
                ", tags=" + tags +
                ", tagIds=" + tagIds +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", status='" + status + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", weight=" + weight +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", limit=" + limit +
                ", offset=" + offset +
                ", sort='" + sort + '\'' +
                ", order='" + order + '\'' +
                '}';
    }
}
