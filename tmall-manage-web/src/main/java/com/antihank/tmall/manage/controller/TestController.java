package com.antihank.tmall.manage.controller;

import com.antihank.tmall.manage.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created by Antihank on 2017/4/27.
 */
@Controller
@RequestMapping("test")
public class TestController {

    @Resource
    private TestService service;

    @RequestMapping("date")
    @ResponseBody
    public String getDate() {
        return service.getCurrentDate();
    }

    @RequestMapping("index")
    public ModelAndView toIndex() {
        return new ModelAndView("index");
    }
}
