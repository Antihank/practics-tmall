package com.antihank.tmall.portal.controller;

import com.antihank.tmall.manage.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;

/**
 * Created by Antihank on 2017/5/8.
 */
@Controller
public class IndexController implements Serializable {

    private static final long serialVersionUID = -6414839914518876795L;
    @Autowired
    private ContentService contentService;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        String result = contentService.selectContentAsBigAdForPortal();
        mv.addObject("bigAdData", result);
        return mv;
    }
}
