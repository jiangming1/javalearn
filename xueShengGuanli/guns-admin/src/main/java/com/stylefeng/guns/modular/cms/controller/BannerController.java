package com.stylefeng.guns.modular.cms.controller;

import cn.hutool.core.util.ImageUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.common.constant.factory.PageFactory;
import com.stylefeng.guns.common.exception.BizExceptionEnum;
import com.stylefeng.guns.common.exception.BussinessException;
import com.stylefeng.guns.common.persistence.model.Banner;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.shiro.ShiroKit;
import com.stylefeng.guns.core.util.MyFun;
import com.stylefeng.guns.modular.cms.dao.BannerDao;
import com.stylefeng.guns.modular.cms.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 轮播图控制器
 *
 * @author zhoujin7
 * @Date 2018-02-01 15:51:36
 */
@Controller
@RequestMapping("/banner")
public class BannerController extends BaseController {

    private String PREFIX = "/cms/banner/";

    @Autowired
    private IBannerService bannerService;
    @Autowired
    private BannerDao bannerDao;

    /**
     * 跳转到轮播图首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "banner.html";
    }

    /**
     * 跳转到添加轮播图
     */
    @RequestMapping("/banner_add")
    public String bannerAdd(Model model) {
        model.addAttribute("authorId", ShiroKit.getUser().getId());
        return PREFIX + "banner_add.html";
    }

    /**
     * 跳转到修改轮播图
     */
    @RequestMapping("/banner_update/{bannerId}")
    public String bannerUpdate(@PathVariable Integer bannerId, Model model) {
        Banner banner = bannerService.selectById(bannerId);
        model.addAttribute("banner", banner);
        LogObjectHolder.me().set(banner);
        return PREFIX + "banner_edit.html";
    }

    /**
     * 获取轮播图列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam Integer limit, @RequestParam Integer offset, @RequestParam String order, @RequestParam(required = false) String title) {
        Page page = new PageFactory(limit, offset, order).defaultPage();

        List<Map<String, Object>> result = bannerDao.getBanner(page, title);
        page.setRecords(result);
        return super.packForBT(page);
    }

    /**
     * 新增轮播图
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Banner banner) {
        bannerDao.insertBanner(banner);
        return super.SUCCESS_TIP;
    }

    /**
     * 上传图片(上传到项目的static/upload/cms/banner-image/)
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public String upload(@RequestPart("file") MultipartFile picture) {
        String rootPath = MyFun.getResourceRootPath();
        rootPath = rootPath + "static/upload/cms/banner-image/";
        MyFun.mkdir(rootPath);

        String pictureName = UUID.randomUUID().toString();
        String originPictureName = pictureName + "-origin.jpg";
        String pictureName1500x505 = pictureName + "-1500x505.jpg";
        File originImageFile = new File(rootPath + originPictureName);
        try {
            picture.transferTo(originImageFile);
        } catch (Exception e) {
            throw new BussinessException(BizExceptionEnum.UPLOAD_ERROR);
        }

        Image originImage = ImageUtil.read(originImageFile);
        Image scaledSrcImage = ImageUtil.scale(originImage, 1500, 505);
        File descImageFile = new File(rootPath + pictureName1500x505);
        ImageUtil.write(scaledSrcImage, descImageFile);

        return "static/upload/cms/banner-image/" + pictureName1500x505;
    }

    /**
     * 删除轮播图
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer bannerId) {
        bannerService.deleteById(bannerId);
        return SUCCESS_TIP;
    }

    /**
     * 修改轮播图
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Banner banner) {
        bannerService.updateById(banner);
        return super.SUCCESS_TIP;
    }

    /**
     * 轮播图详情
     */
    @RequestMapping(value = "/detail/{bannerId}")
    @ResponseBody
    public Object detail(@PathVariable("bannerId") Integer bannerId) {
        return bannerService.selectById(bannerId);
    }

}
