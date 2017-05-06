package com.antihank.tmall.manage.service;

import com.antihank.tmall.manage.pojo.BasePojo;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Antihank on 2017/5/1.
 */
public interface BaseService<T extends BasePojo> extends Serializable {

    T selectByPrimaryKey(Object o);

    List<T> selectAllAsList();

    List<T> selectAllAsList(T t);

    List<T> selectAllAsList(Integer page, Integer rows);

    List<T> selectAllAsList(Integer page, Integer rows, T t);

    PageInfo<T> selectAll(Integer page, Integer rows);

    PageInfo<T> selectAll(Integer page, Integer rows, Example example);

    int selectCount(T t);

    int insertSelective(T t);

    int updateByPrimaryKeySelective(T t);

    int deleteByPrimaryKey(Object o);

    int deleteByPrimaryKeys(Object[] os);
}
