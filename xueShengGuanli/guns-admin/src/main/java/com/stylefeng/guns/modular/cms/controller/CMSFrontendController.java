package com.stylefeng.guns.modular.cms.controller;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.common.constant.factory.PageFactory;
import com.stylefeng.guns.core.util.MyPage;
import com.stylefeng.guns.modular.cms.dao.BannerDao;
import com.stylefeng.guns.modular.cms.dao.NavmenuDao;
import com.stylefeng.guns.modular.cms.dao.NewsPanelDao;
import com.stylefeng.guns.modular.cms.dao.PostDao;
import com.stylefeng.guns.modular.cms.transfer.PostInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CMSFrontendController {

    private static String PREFIX = "/cms/cms-fronted/";

    @Resource
    PostDao postDao;

    @Autowired
    BannerDao bannerDao;

    @Resource
    NavmenuDao navmenuDao;

    @Resource
    NewsPanelDao newsPanelDao;

    @Autowired
    private Environment environment;

    /**
     * 网站前端首页
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {

        Page page = new PageFactory(8, 0, "desc").defaultPage();

        List<Map<String, Object>> newsPanelList = newsPanelDao.getNewsPanel(null);
        model.addAttribute("newsPanelList", newsPanelList);
        List<List<Map<String, Object>>> newsPanelArticleList = new ArrayList<>();
        for (Map<String, Object> newsPanelMap : newsPanelList) {
            newsPanelArticleList.add(postDao.getPostInfoByCategory(page, (String) newsPanelMap.get("name")));
        }
        model.addAttribute("newsPanelArticleList", newsPanelArticleList);

        List<Map<String, Object>> bannerList = bannerDao.getBanner(page, null);
        model.addAttribute("bannerList", bannerList);

        List<Map<String, Object>> navmenuList = navmenuDao.getNavmenu();
        model.addAttribute("navmenuList", navmenuList);

        model.addAttribute("siteName", environment.getProperty("cms.siteName"));

        model.addAttribute("hits", hits());

        return PREFIX + "index.html";
    }

    @RequestMapping(value = "/{category}/article/{currentPageNum}/{postId}", method = RequestMethod.GET)
    public String article(@PathVariable("category") String category, @PathVariable("currentPageNum") Integer currentPageNum, @PathVariable("postId") Integer postId, Model model, SitePreference sitePreference) {

        model.addAttribute("category", category);
        model.addAttribute("currentPageNum", currentPageNum);

        Map<String, Object> prevArticle = postDao.getPrevPostInfoByCategoryAndId(category, postId);
        model.addAttribute("prevArticle", prevArticle);
        Map<String, Object> nextArticle = postDao.getNextPostInfoByCategoryAndId(category, postId);
        model.addAttribute("nextArticle", nextArticle);

        boolean isMobile = sitePreference.isMobile() ? true : false;
        model.addAttribute("isMobile", isMobile);

        Map<String, Object> article = postDao.getPostInfoById(postId);
        if (isMobile) {
            String content = (String) article.get("content");
            content = content.replaceAll("(<img )", "$1 class='img-responsive'");
            article.put("content", content);
        }
        model.addAttribute("article", article);

        List<Map<String, Object>> navmenuList = navmenuDao.getNavmenu();
        model.addAttribute("navmenuList", navmenuList);

        model.addAttribute("siteName", environment.getProperty("cms.siteName"));

        model.addAttribute("hits", hits());

        return PREFIX + "article.html";
    }

    @RequestMapping(value = "/category/{category}/{currentPageNum}", method = RequestMethod.GET)
    public String articleList(@PathVariable("category") String category, @PathVariable("currentPageNum") Integer currentPageNum, Model model, SitePreference sitePreference) {
        int sizePerPage = 20;
        int offset = (currentPageNum - 1) * sizePerPage;
        int pagingBtnLimit = 5;

        Page page = new PageFactory(sizePerPage, offset, "desc").defaultPage();

        List<Map<String, Object>> articleList = postDao.getPostInfoByCategory(page, category);

        MyPage myPage = new MyPage(page.getTotal(), sizePerPage, currentPageNum, pagingBtnLimit);

        if (!myPage.isDoPagingSuccess()) {
            return "redirect:/global/error";
        }

        model.addAttribute("articleList", articleList);
        model.addAttribute("category", category);
        model.addAttribute("myPage", myPage);

        boolean isMobile = sitePreference.isMobile() ? true : false;
        model.addAttribute("isMobile", isMobile);

        List<Map<String, Object>> navmenuList = navmenuDao.getNavmenu();
        model.addAttribute("navmenuList", navmenuList);
        model.addAttribute("siteName", environment.getProperty("cms.siteName"));

        model.addAttribute("hits", hits());

        return PREFIX + "article_list.html";
    }

    /**
     * 按标题搜索文章
     */
    @RequestMapping(value = "/search/{content}/{currentPageNum}")
    public Object search(@PathVariable("content") String content, @PathVariable("currentPageNum") Integer currentPageNum, Model model, SitePreference sitePreference) {
        int sizePerPage = 20;
        int offset = (currentPageNum - 1) * sizePerPage;
        int pagingBtnLimit = 5;

        Page page = new PageFactory(sizePerPage, offset, "desc").defaultPage();

        PostInfo postInfo = new PostInfo();
        postInfo.setTitle(content);
        List<Map<String, Object>> articleList = postDao.searchPostInfo(page, postInfo);

        MyPage myPage = new MyPage(page.getTotal(), sizePerPage, currentPageNum, pagingBtnLimit);

        if (!myPage.isDoPagingSuccess()) {
            return "redirect:/global/error";
        }

        model.addAttribute("articleList", articleList);
        model.addAttribute("content", content);
        model.addAttribute("myPage", myPage);

        boolean isMobile = sitePreference.isMobile() ? true : false;
        model.addAttribute("isMobile", isMobile);

        List<Map<String, Object>> navmenuList = navmenuDao.getNavmenu();
        model.addAttribute("navmenuList", navmenuList);
        model.addAttribute("siteName", environment.getProperty("cms.siteName"));

        model.addAttribute("hits", hits());

        return PREFIX + "search_list.html";
    }

    private int hits() {
        String rootPath;
        String path = CMSFrontendController.class.getClassLoader().getResource("application.yml").getPath();
        File file = new File(path);
        if (file.getParentFile().isDirectory()) {
            rootPath = new File(path).getParent() + "/";
        } else {
            rootPath = new File(path).getParentFile().getParent() + "/";
            rootPath = rootPath.replace("file:", "");
        }
        try {
            File classpath = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!classpath.exists()) {
                classpath = new File("");
            }
            if (rootPath.contains(".jar")) {
                rootPath = classpath.getAbsolutePath() + "/";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        rootPath = rootPath + "static/";

        File hitsFile = new File(rootPath + "hits.txt");
        FileReader fileReader = new FileReader(hitsFile);
        FileWriter fileWriter = new FileWriter(hitsFile);
        int hits = Integer.parseInt(fileReader.readString()) + 1;
        fileWriter.write(hits + "");

        return hits;
    }
}
