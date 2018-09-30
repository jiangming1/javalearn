package com.stylefeng.guns.modular.cms.service;

import com.baomidou.mybatisplus.service.IService;
import com.stylefeng.guns.common.persistence.model.Post;
import com.stylefeng.guns.modular.cms.transfer.PostInfo;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author zhoujin7
 * @since 2017-12-02
 */
public interface IPostService extends IService<Post> {
    void modifyPostInfo(PostInfo postInfo);
}
