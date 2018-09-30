package com.stylefeng.guns.core.util;

import cn.hutool.core.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;

@Component
public class MyFun {
    private static final Logger log = LoggerFactory.getLogger(MyFun.class);

    @Resource
    private ServletContext servletContext;

    public static String getResourceRootPath() {
        String rootPath;
        String path = MyFun.class.getClassLoader().getResource("application.yml").getPath();
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
        return rootPath;
    }

    public static String convertRegexStr(String regexStr) {
        if (regexStr != null) {
            regexStr = regexStr.replaceAll("(`)\\1", "\0");
            regexStr = regexStr.replaceAll("`", "\\\\");
            regexStr = regexStr.replaceAll("\\\0", "`");
            return regexStr;
        }
        return "";
    }

    public static void mkdir(String path) {
        if (!FileUtil.exist(path)) {
            FileUtil.mkdir(path);
        }
    }

    public boolean imgExists(String path) {
        path = getResourceRootPath() + path;
        if (FileUtil.exist(path) && FileUtil.isFile(path)) {
            return true;
        }
        return false;
    }

    public String getApplicationContextPath() {
        return servletContext.getContextPath() != null ? servletContext.getContextPath() : "";
    }

}
