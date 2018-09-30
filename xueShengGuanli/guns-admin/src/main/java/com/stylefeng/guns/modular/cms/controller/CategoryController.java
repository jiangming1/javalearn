package com.stylefeng.guns.modular.cms.controller;

import com.stylefeng.guns.common.persistence.model.Taxonomy;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.log.LogObjectHolder;
import com.stylefeng.guns.core.node.ZTreeNode;
import com.stylefeng.guns.modular.cms.dao.CategoryDao;
import com.stylefeng.guns.modular.cms.service.ITaxonomyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类控制器
 *
 * @author zhoujin7
 * @Date 2017-12-08 13:52:01
 */
@Controller
@RequestMapping("/category")
public class CategoryController extends BaseController {

    private String PREFIX = "/cms/category/";

    @Autowired
    private ITaxonomyService categoryService;

    @Autowired
    private CategoryDao categoryDao;

    /**
     * 根据文章id获取分类的tree列表
     */
    @RequestMapping("/tree/{postId}")
    @ResponseBody
    public List<ZTreeNode> tree(@PathVariable Integer postId) {
        List<ZTreeNode> tree = this.categoryDao.tree(postId, null);
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    /**
     * 获取分类的tree列表
     */
    @RequestMapping("/tree")
    @ResponseBody
    public List<ZTreeNode> tree() {
        List<ZTreeNode> tree = this.categoryDao.tree(null, null);
        tree.add(ZTreeNode.createParent());
        return tree;
    }

    /**
     * 跳转到分类首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "category.html";
    }

    /**
     * 跳转到添加分类
     */
    @RequestMapping("/category_add")
    public String categoryAdd() {
        return PREFIX + "category_add.html";
    }

    /**
     * 跳转到修改分类
     */
    @RequestMapping("/category_update/{descendantId}")
    public String categoryUpdate(@PathVariable Integer descendantId, Model model) {
        Taxonomy category = categoryService.selectById(descendantId);
        model.addAttribute("category", category);
        Map ancestorCategory = categoryDao.findDirectAncestor(descendantId);
        model.addAttribute("ancestorCategory", ancestorCategory);
        LogObjectHolder.me().set(category);
        return PREFIX + "category_edit.html";
    }

    /**
     * 获取分类列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String descendantName) {
        List<ZTreeNode> tree = this.categoryDao.tree(null, descendantName);
        return tree;
    }

    /**
     * 新增分类
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(@RequestParam Integer ancestorId, @RequestParam String descendantName) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", descendantName);
        if (categoryDao.selectByMap(map).size() == 0) {
            this.categoryDao.addNode(ancestorId, descendantName);
        } else {
            return new ErrorTip(500, "该分类名已存在!");
        }
        return super.SUCCESS_TIP;
    }

    /**
     * 删除分类
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer descendantId) {
        categoryDao.removeSubtree(descendantId);
        categoryDao.unbindPostAndTaxonomy(descendantId);
        return SUCCESS_TIP;
    }

    /**
     * 修改分类
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(@RequestParam Integer ancestorId, @RequestParam Integer descendantId, @RequestParam String descendantName) {
        categoryDao.moveNode(ancestorId, descendantId);
        Taxonomy category = new Taxonomy();
        category.setId(descendantId);
        category.setName(descendantName);
        categoryDao.updateById(category);
        return super.SUCCESS_TIP;
    }

    /**
     * 分类详情
     */
    @RequestMapping(value = "/detail/{descendantId}")
    @ResponseBody
    public Object detail(@PathVariable("descendantId") Integer descendantId) {
        return categoryService.selectById(descendantId);
    }
}
