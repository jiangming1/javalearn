package com.stylefeng.guns.modular.cms.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.common.persistence.model.Navmenu;
import com.stylefeng.guns.core.node.NavmenuNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 导航菜单表 Mapper 接口
 * </p>
 *
 * @author zhoujin7
 * @since 2018-02-06
 */
public interface NavmenuDao extends BaseMapper<Navmenu> {
    List<NavmenuNode> tree(@Param("descendantName") String descendantName);

    void addNode(@Param("ancestorId") Integer ancestorId, @Param("descendantName") String descendantName, @Param("descendantLink") String descendantLink, @Param("descendantWeight") Integer descendantWeight);

    void moveNode(@Param("ancestorId") Integer ancestorId, @Param("descendantId") Integer descendantId);

    void removeSubtree(@Param("nodeId") Integer nodeId);

    Map<String, Object> findDirectAncestor(@Param("descendantId") Integer descendantId);

    List<Map<String, Object>> findDirectDescendants(@Param("ancestorId") Integer ancestorId);

    List<Map<String, Object>> getNavmenu();
}