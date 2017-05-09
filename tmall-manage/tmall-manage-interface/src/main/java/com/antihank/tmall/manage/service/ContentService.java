package com.antihank.tmall.manage.service;

import com.antihank.tmall.manage.pojo.Content;
import com.github.pagehelper.PageInfo;

/**
 * Created by Antihank on 2017/5/8.
 */
public interface ContentService extends BaseService<Content> {
    PageInfo<Content> selectByCategoryId(Integer page, Integer rows, Long categoryId);

    /**
     * 为首页获取大广告的内容
     *
     * @return json格式的列表
     */
    String selectContentAsBigAdForPortal();
}
