package com.stylefeng.guns.modular.cms.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.common.persistence.model.Taxonomy;
import com.stylefeng.guns.core.node.ZTreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分类表 Mapper 接口
 * </p>
 *
 * @author zhoujin7
 * @since 2017-12-02
 */
public interface CategoryDao extends BaseMapper<Taxonomy> {
    List<ZTreeNode> tree(@Param("postId") Integer postId, @Param("descendantName") String descendantName);

    void addNode(@Param("ancestorId") Integer ancestorId, @Param("descendantName") String descendantName);

    void moveNode(@Param("ancestorId") Integer ancestorId, @Param("descendantId") Integer descendantId);

    void removeSubtree(@Param("nodeId") Integer nodeId);

    Map<String, Object> findDirectAncestor(@Param("descendantId") Integer descendantId);

    int unbindPostAndTaxonomy(@Param("taxonomyId") Integer taxonomyId);

}