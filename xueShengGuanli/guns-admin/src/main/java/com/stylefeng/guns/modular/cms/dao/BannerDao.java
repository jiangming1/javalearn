package com.stylefeng.guns.modular.cms.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.common.persistence.model.Banner;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 大图表 Mapper 接口
 * </p>
 *
 * @author zhoujin7
 * @since 2018-02-01
 */
public interface BannerDao extends BaseMapper<Banner> {
    List<Map<String, Object>> getBanner(@Param("page") Page page, @Param("title") String title);

    int insertBanner(@Param("b") Banner banner);
}