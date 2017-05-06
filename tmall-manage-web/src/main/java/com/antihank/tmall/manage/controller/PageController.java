package com.antihank.tmall.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by Antihank on 2017/5/4.
 */
@RequestMapping("/page")
@Controller
public class PageController implements Serializable {

    private static final long serialVersionUID = -5467466721813692054L;

    @RequestMapping(value = "/{pageName}", method = RequestMethod.GET)
    public String toPage(HttpServletRequest request, @PathVariable(value = "pageName") String pageName) {
        return pageName;
    }
}
