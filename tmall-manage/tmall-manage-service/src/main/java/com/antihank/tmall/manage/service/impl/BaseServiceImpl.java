package com.antihank.tmall.manage.service.impl;

import com.antihank.tmall.manage.pojo.BasePojo;
import com.antihank.tmall.manage.service.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Antihank on 2017/5/1.
 */
public abstract class BaseServiceImpl<T extends BasePojo> implements BaseService<T> {

    private static final long serialVersionUID = -5354324783332081788L;

    @Autowired
    private Mapper<T> mapper;

    private Class<T> entityClass;

    @Override
    public List<T> selectAllAsList(T t) {
        return mapper.select(t);
    }

    public BaseServiceImpl() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        entityClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public List<T> selectAllAsList() {
        return mapper.selectAll();
    }

    @Override
    public List<T> selectAllAsList(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        return mapper.selectAll();
    }

    @Override
    public List<T> selectAllAsList(Integer page, Integer rows, T t) {
        return mapper.select(t);
    }

    @Override
    public int selectCount(T t) {
        return mapper.selectCount(t);
    }

    @Override
    public int deleteByPrimaryKeys(Object[] os) {
        Example e = new Example(entityClass);
        Example.Criteria c = e.createCriteria();
        c.andIn("id", Arrays.asList(os));
        return mapper.deleteByExample(e);
    }

    @Override
    public T selectByPrimaryKey(Object o) {
        return mapper.selectByPrimaryKey(o);
    }

    @Override
    public PageInfo<T> selectAll(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);

        List<T> list = mapper.selectAll();

        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<T> selectAll(Integer page, Integer rows, Example example) {
        PageHelper.startPage(page, rows);

        List<T> list = mapper.selectByExample(example);

        return new PageInfo<T>(list);
    }

    @Override
    public int insertSelective(T t) {
        t.setCreated(new Date(System.currentTimeMillis()));
        t.setUpdated(t.getCreated());
        return mapper.insertSelective(t);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        t.setUpdated(new Date(System.currentTimeMillis()));
        return mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public int deleteByPrimaryKey(Object o) {
        return mapper.deleteByPrimaryKey(o);
    }


}
