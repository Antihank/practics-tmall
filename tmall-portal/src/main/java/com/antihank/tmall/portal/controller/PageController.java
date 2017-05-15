package com.antihank.tmall.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;

/**
 * Created by Antihank on 2017/5/11.
 */
@RequestMapping("page")
@Controller
public class PageController implements Serializable {

    private static final long serialVersionUID = 7787452800289064512L;

    @RequestMapping(value = "/{pageName}", method = RequestMethod.GET)
    public String toPage(@PathVariable("pageName") String pageName) {
        return pageName;
    }


}

