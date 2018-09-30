package com.stylefeng.guns.modular.cms.controller;

import com.stylefeng.guns.common.persistence.model.NewsPanel;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.modular.cms.dao.NewsPanelDao;
import com.stylefeng.guns.modular.cms.service.INewsPanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 新闻面板控制器
 *
 * @author zhoujin7
 * @Date 2018-02-07 20:59:36
 */
@Controller
@RequestMapping("/newsPanel")
public class NewsPanelController extends BaseController {

    private String PREFIX = "/cms/newsPanel/";

    @Autowired
    private INewsPanelService newsPanelService;

    @Resource
    private NewsPanelDao newsPanelDao;

    /**
     * 跳转到新闻面板首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "newsPanel.html";
    }

    /**
     * 跳转到添加新闻面板
     */
    @RequestMapping("/newsPanel_add")
    public String newsPanelAdd() {
        return PREFIX + "newsPanel_add.html";
    }

    /**
     * 跳转到修改新闻面板
     */
    @RequestMapping("/newsPanel_update/{newsPanelId}")
    public String newsPanelUpdate(@PathVariable Integer newsPanelId, Model model) {
        Map<String, Object> newsPanel = newsPanelDao.getNewsPanelById(newsPanelId);
        model.addAttribute("newsPanel", newsPanel);
        LogObjectHolder.me().set(newsPanel);
        return PREFIX + "newsPanel_edit.html";
    }

    /**
     * 获取新闻面板列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String name) {
        return newsPanelDao.getNewsPanel(name);
    }

    /**
     * 新增新闻面板
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(NewsPanel newsPanel) {
        newsPanelService.insert(newsPanel);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除新闻面板
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer newsPanelId) {
        newsPanelService.deleteById(newsPanelId);
        return SUCCESS_TIP;
    }

    /**
     * 修改新闻面板
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(NewsPanel newsPanel) {
        newsPanelService.updateById(newsPanel);
        return super.SUCCESS_TIP;
    }

    /**
     * 新闻面板详情
     */
    @RequestMapping(value = "/detail/{newsPanelId}")
    @ResponseBody
    public Object detail(@PathVariable("newsPanelId") Integer newsPanelId) {
        return newsPanelService.selectById(newsPanelId);
    }
}
