package com.antihank.tmall.manage.service.impl;

import com.antihank.tmall.manage.mapper.TestMapper;
import com.antihank.tmall.manage.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Antihank on 2017/4/27.
 */
@Service
public class TestServiceImpl implements TestService, Serializable {

    private static final long serialVersionUID = -8237713207994810126L;
    @Autowired
    private TestMapper mapper;

    @Override
    public String getCurrentDate() {
        return mapper.getCurrentDate();
    }
}
