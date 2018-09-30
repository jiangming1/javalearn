package com.stylefeng.guns.common.constant.factory;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.common.constant.state.Order;
import com.stylefeng.guns.core.support.HttpKit;
import com.stylefeng.guns.core.util.ToolUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * BootStrap Table默认的分页参数创建
 *
 * @author fengshuonan
 * @date 2017-04-05 22:25
 */
public class PageFactory<T> {

    private int limit;
    private int offset;
    private String sort;
    private String order;

    public PageFactory() {
    }

    public PageFactory(int limit, int offset, String order) {
        this.limit = limit;
        this.offset = offset;
        this.order = order;
    }

    public PageFactory(int limit, int offset, String sort, String order) {
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
        this.order = order;
    }

    public Page<T> defaultPage() {
        int limit;
        int offset;
        String sort;
        String order;

        HttpServletRequest request = HttpKit.getRequest();
        if (request.getParameter("limit") != null
                && request.getParameter("offset") != null
                /*&& request.getParameter("sort") != null*/
                && request.getParameter("order") != null) {
            //每页多少条数据
            limit = Integer.valueOf(request.getParameter("limit"));
            //每页的偏移量(本页当前有多少条)
            offset = Integer.valueOf(request.getParameter("offset"));
            //排序字段名称 可能为null
            sort = request.getParameter("sort");
            //asc或desc(升序或降序)
            order = request.getParameter("order");

        } else {
            limit = this.limit;
            offset = this.offset;
            order = this.order;
            sort = this.sort;
        }

        if (ToolUtil.isEmpty(sort)) {
            Page<T> page = new Page<>((offset / limit + 1), limit);
            page.setOpenSort(false);
            return page;
        } else {
            Page<T> page = new Page<>((offset / limit + 1), limit, sort);
            if (Order.ASC.getDes().equals(order)) {
                page.setAsc(true);
            } else {
                page.setAsc(false);
            }
            return page;
        }
    }
}
