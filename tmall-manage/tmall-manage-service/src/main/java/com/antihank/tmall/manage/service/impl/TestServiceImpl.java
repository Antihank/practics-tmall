package com.antihank.tmall.manage.service.impl;

import com.antihank.tmall.manage.mapper.TestMapper;
import com.antihank.tmall.manage.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Antihank on 2017/4/27.
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper mapper;

    @Override
    public String getCurrentDate() {
        return mapper.getCurrentDate();
    }
}
