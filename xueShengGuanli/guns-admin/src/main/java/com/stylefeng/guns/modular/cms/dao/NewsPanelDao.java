package com.stylefeng.guns.modular.cms.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.common.persistence.model.NewsPanel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 新闻面板表 Mapper 接口
 * </p>
 *
 * @author zhoujin7
 * @since 2018-02-07
 */
public interface NewsPanelDao extends BaseMapper<NewsPanel> {
    List<Map<String, Object>> getNewsPanel(@Param("name") String name);

    Map<String, Object> getNewsPanelById(@Param("id") Integer id);
}