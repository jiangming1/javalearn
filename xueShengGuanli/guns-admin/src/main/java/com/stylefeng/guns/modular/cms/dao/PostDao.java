package com.stylefeng.guns.modular.cms.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.common.persistence.model.Post;
import com.stylefeng.guns.modular.cms.transfer.PostInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章表 Mapper 接口
 * </p>
 *
 * @author zhoujin7
 * @since 2017-11-30
 */
public interface PostDao extends BaseMapper<Post> {

    List<Map<String, Object>> getPostInfo(@Param("page") Page page, @Param("p") PostInfo postInfo);

    Map<String, Object> getPostInfoById(@Param("postId") Integer postId);

    Map<String, Object> getPrevPostInfoByCategoryAndId(@Param("category") String category, @Param("postId") Integer postId);

    Map<String, Object> getNextPostInfoByCategoryAndId(@Param("category") String category, @Param("postId") Integer postId);

    List<Map<String, Object>> searchPostInfo(@Param("page") Page page, @Param("p") PostInfo postInfo);

    List<Map<String, Object>> getPostInfoByCategory(@Param("page") Page page, @Param("category") String category);

    int bindPostAndTaxonomy(@Param("taxonomyIds") List<Integer> taxonomyIds, @Param("postId") Integer postId);

    int unbindPostAndTaxonomy(@Param("taxonomyIds") List<Integer> taxonomyIds, @Param("postId") Integer postId);

    int insertPost(@Param("p") PostInfo postInfo);

    int deletePost(@Param("postId") Integer postId);

}