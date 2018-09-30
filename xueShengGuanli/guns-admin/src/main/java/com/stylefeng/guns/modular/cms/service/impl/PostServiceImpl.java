package com.stylefeng.guns.modular.cms.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.stylefeng.guns.common.persistence.dao.PostMapper;
import com.stylefeng.guns.common.persistence.model.Post;
import com.stylefeng.guns.modular.cms.service.IPostService;
import com.stylefeng.guns.modular.cms.transfer.PostInfo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author zhoujin7
 * @since 2017-12-02
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {
    public void modifyPostInfo(PostInfo postInfo) {
        String status = postInfo.getStatus();
        if (status != null) {
            switch (status) {
                case "发布":
                    status = "publish";
                    break;
                case "草稿":
                    status = "draft";
                    break;
                case "回收":
                    status = "trash";
                    break;
                default:
                    status = "";
                    break;
            }
            postInfo.setStatus(status);
        }
    }
}
