package com.stylefeng.guns.modular.cms.controller;

import com.stylefeng.guns.common.persistence.model.Navmenu;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.node.NavmenuNode;
import com.stylefeng.guns.modular.cms.dao.NavmenuDao;
import com.stylefeng.guns.modular.cms.service.INavmenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 导航菜单控制器
 *
 * @author zhoujin7
 * @Date 2018-02-06 11:43:45
 */
@Controller
@RequestMapping("/navmenu")
public class NavmenuController extends BaseController {

    @Resource
    private NavmenuDao navmenuDao;

    private String PREFIX = "/cms/navmenu/";

    @Autowired
    private INavmenuService navmenuService;

    /**
     * 跳转到导航菜单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "navmenu.html";
    }

    /**
     * 跳转到添加导航菜单
     */
    @RequestMapping("/navmenu_add")
    public String navmenuAdd() {
        return PREFIX + "navmenu_add.html";
    }

    /**
     * 跳转到修改导航菜单
     */
    @RequestMapping("/navmenu_update/{descendantId}")
    public String navmenuUpdate(@PathVariable Integer descendantId, Model model) {
        Navmenu navmenu = navmenuDao.selectById(descendantId);
        model.addAttribute("navmenu", navmenu);
        Map ancestorNavmenu = navmenuDao.findDirectAncestor(descendantId);
        model.addAttribute("ancestorNavmenu", ancestorNavmenu);
        LogObjectHolder.me().set(navmenu);
        return PREFIX + "navmenu_edit.html";
    }

    /**
     * 获取导航菜单的tree列表
     */
    @RequestMapping("/tree")
    @ResponseBody
    public List<NavmenuNode> tree() {
        List<NavmenuNode> tree = this.navmenuDao.tree(null);
        tree.add(NavmenuNode.createParent());
        return tree;
    }

    /**
     * 获取导航菜单列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String descendantName) {
        List<NavmenuNode> tree = this.navmenuDao.tree(descendantName);
        return tree;
    }

    /**
     * 新增导航菜单
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(@RequestParam Integer ancestorId, @RequestParam String descendantName, @RequestParam String descendantLink, @RequestParam Integer descendantWeight) {
        this.navmenuDao.addNode(ancestorId, descendantName, descendantLink, descendantWeight);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除导航菜单
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer descendantId) {
        this.navmenuDao.removeSubtree(descendantId);
        return SUCCESS_TIP;
    }

    /**
     * 修改导航菜单
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(@RequestParam Integer ancestorId, @RequestParam Integer descendantId, @RequestParam String descendantName, @RequestParam String descendantLink, @RequestParam Integer descendantWeight) {
        navmenuDao.moveNode(ancestorId, descendantId);
        Navmenu navmenu = new Navmenu();
        navmenu.setId(descendantId);
        navmenu.setName(descendantName);
        navmenu.setLink(descendantLink);
        navmenu.setWeight(descendantWeight);
        navmenuDao.updateById(navmenu);
        return super.SUCCESS_TIP;
    }

    /**
     * 导航菜单详情
     */
    @RequestMapping(value = "/detail/{descendantId}")
    @ResponseBody
    public Object detail(@PathVariable("descendantId") Integer descendantId) {
        return navmenuDao.selectById(descendantId);
    }
}
