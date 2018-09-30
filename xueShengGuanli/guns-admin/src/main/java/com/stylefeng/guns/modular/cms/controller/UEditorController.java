package com.stylefeng.guns.modular.cms.controller;

import com.baidu.ueditor.ActionEnter;
import com.stylefeng.guns.core.util.MyFun;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Controller
public class UEditorController {
    private final static String staticPath = "static/";

    public UEditorController() {
    }

    @RequestMapping("/ueditor")
    public void exec(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        response.setHeader("Content-Type", "text/html");

        String rootPath = MyFun.getResourceRootPath();
        boolean isRunningAsJar = false;
        if (rootPath.contains(".jar")) {
            isRunningAsJar = true;
        }

        out.write(new ActionEnter(request, rootPath, staticPath, isRunningAsJar).exec());
    }

}
