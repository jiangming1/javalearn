package com.stylefeng.guns.modular.cms.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.common.constant.factory.PageFactory;
import com.stylefeng.guns.common.exception.BizExceptionEnum;
import com.stylefeng.guns.common.exception.BussinessException;
import com.stylefeng.guns.common.persistence.model.Post;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.MyFun;
import com.stylefeng.guns.modular.cms.dao.NavmenuDao;
import com.stylefeng.guns.modular.cms.dao.PostDao;
import com.stylefeng.guns.modular.cms.service.IPostService;
import com.stylefeng.guns.modular.cms.transfer.PostInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 文章管理控制器
 *
 * @author zhoujin7
 * @Date 2017-12-01 14:58:13
 */
@Controller
@RequestMapping("/post")
@Transactional
public class PostController extends BaseController {

    private String PREFIX = "/cms/post/";
    @Autowired
    private IPostService postService;
    @Autowired
    private PostDao postDao;
    @Resource
    private NavmenuDao navmenuDao;

    /**
     * 跳转到文章管理首页
     */
    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("isAuthor", ShiroKit.isAuthor());
        model.addAttribute("author", ShiroKit.getUser().getName());
        return PREFIX + "post.html";
    }

    /**
     * 跳转到添加文章管理
     */
    @RequestMapping("/post_add")
    public String postAdd(Model model) {
        model.addAttribute("author", ShiroKit.getUser().getName());
        model.addAttribute("authorId", ShiroKit.getUser().getId());
        return PREFIX + "post_add.html";
    }

    /**
     * 跳转到修改文章管理
     */
    @RequestMapping("/post_update/{postId}")
    public String postUpdate(@PathVariable Integer postId, Model model) {
        Map<String, Object> post = postDao.getPostInfoById(postId);
        model.addAttribute("post", post);
        LogObjectHolder.me().set(post);
        return PREFIX + "post_edit.html";
    }

    /**
     * 获取文章管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestBody PostInfo postInfo) {

        Page page = new PageFactory(postInfo.getLimit(), postInfo.getOffset(), postInfo.getOrder()).defaultPage();

        postService.modifyPostInfo(postInfo);

        if (ShiroKit.isAuthor()) {
            postInfo.setAuthor(ShiroKit.getUser().getName());
        }

        Map<String, Object> postInfoMap = BeanUtil.beanToMap(postInfo, false, true);

        String[] postInfoProperties = {"title", "author", "category", "tag", "status", "beginTime", "endTime"};
        for (String property : postInfoProperties) {
            String propValue = (String) postInfoMap.get(property);
            if (propValue != null && propValue != "") {
                List<Map<String, Object>> result = postDao.searchPostInfo(page, postInfo);
                int total = postDao.selectCount(null);
                if (page.getTotal() > total) {
                    page.setTotal(total);
                }
                page.setRecords(result);
                return super.packForBT(page);
            }
        }

        List<Map<String, Object>> result = postDao.getPostInfo(page, postInfo);

        page.setRecords(result);
        return super.packForBT(page);
    }

    /**
     * 新增文章管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(@RequestBody PostInfo postInfo) {
        postService.modifyPostInfo(postInfo);

        postDao.insertPost(postInfo);

        List<Integer> categoryIds = postInfo.getCategoryIds();
        if (CollUtil.isNotEmpty(categoryIds)) {
            postDao.bindPostAndTaxonomy(categoryIds, postInfo.getId());
        }

        return super.SUCCESS_TIP;
    }

    /**
     * 跳转到回收站
     */
    @RequestMapping("/recycle_bin")
    public String recycleBin(Model model) {
        model.addAttribute("isAuthor", ShiroKit.isAuthor());
        model.addAttribute("author", ShiroKit.getUser().getName());
        return PREFIX + "post_recycle_bin.html";
    }

    /**
     * 将文章移动到回收站
     */
    @RequestMapping(value = "/trash")
    @ResponseBody
    public Object trash(@RequestBody List<Integer> postIds) {
        Post post = new Post();
        for (Integer postId : postIds) {
            post.setId(postId);
            post.setStatus("trash");
            postDao.updateById(post);
        }
        return SUCCESS_TIP;
    }

    /**
     * 将文章移出回收站
     */
    @RequestMapping(value = "/recover_post")
    @ResponseBody
    public Object recoverPost(@RequestBody List<Integer> postIds) {
        Post post = new Post();
        for (Integer postId : postIds) {
            post.setId(postId);
            post.setStatus("draft");
            postDao.updateById(post);
        }
        return SUCCESS_TIP;
    }

    /**
     * 删除文章管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestBody List<Integer> postIds) {
        for (Integer postId : postIds) {
            postDao.deletePost(postId);
        }
        return SUCCESS_TIP;
    }

    /**
     * 修改文章管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(@RequestBody PostInfo postInfo) {
        postService.modifyPostInfo(postInfo);
        List<Integer> categoryIds = postInfo.getCategoryIds();
        List<Integer> oldCategoryIds = postInfo.getOldCategoryIds();
        List<Integer> subtractNewOld = new ArrayList<>();

        if (categoryIds == null) {
            categoryIds = new ArrayList<>();
        }
        if (oldCategoryIds == null) {
            oldCategoryIds = new ArrayList<>();
        }

        for (Integer categoryId : categoryIds) {
            if (!oldCategoryIds.contains(categoryId)) {
                subtractNewOld.add(categoryId);
            }
        }

        List<Integer> subtractOldNew = new ArrayList<>();

        for (Integer oldCategoryId : oldCategoryIds) {
            if (!categoryIds.contains(oldCategoryId)) {
                subtractOldNew.add(oldCategoryId);
            }
        }

        if (CollUtil.isNotEmpty(subtractOldNew)) {
            postDao.unbindPostAndTaxonomy(subtractOldNew, postInfo.getId());
        }

        if (CollUtil.isNotEmpty(subtractNewOld)) {
            postDao.bindPostAndTaxonomy(subtractNewOld, postInfo.getId());
        }


        Post post = new Post();
        post.setId(postInfo.getId());
        post.setTitle(postInfo.getTitle());
        post.setStatus(postInfo.getStatus());
        post.setWeight(postInfo.getWeight());
        post.setContent(postInfo.getContent());
        post.setCoverImage(postInfo.getCoverImage());

        postDao.updateById(post);

        return super.SUCCESS_TIP;
    }

    /**
     * 获取文章内容
     */
    @RequestMapping(value = "/content/{postId}")
    @ResponseBody
    public Object content(@PathVariable("postId") Integer postId) {
        Map<String, Object> post = postDao.getPostInfoById(postId);
        String content = (String) post.get("content");
        return content;
    }

    /**
     * 文章管理详情
     */
    @RequestMapping(value = "/detail/{postId}")
    @ResponseBody
    public Object detail(@PathVariable("postId") Integer postId) {
        return postDao.selectById(postId);
    }


    /**
     * 上传图片(上传到项目的static/upload/cms/post-cover_image/)
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public String upload(@RequestPart("file") MultipartFile picture) {
        String rootPath = MyFun.getResourceRootPath();
        rootPath = rootPath + "static/upload/cms/post-cover_image/";
        MyFun.mkdir(rootPath);

        String pictureName = UUID.randomUUID().toString() + ".jpg";
        File originImageFile = new File(rootPath + pictureName);
        try {
            picture.transferTo(originImageFile);
        } catch (Exception e) {
            throw new BussinessException(BizExceptionEnum.UPLOAD_ERROR);
        }

        return "static/upload/cms/post-cover_image/" + pictureName;
    }

}
