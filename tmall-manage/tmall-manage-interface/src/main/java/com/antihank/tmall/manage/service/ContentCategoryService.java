package com.antihank.tmall.manage.service;

import com.antihank.tmall.manage.pojo.ContentCategory;

import java.util.List;

/**
 * Created by Antihank on 2017/5/8.
 */
public interface ContentCategoryService extends BaseService<ContentCategory> {
    void delete(ContentCategory contentCategory);

    List<ContentCategory> selectByParentId(Long parentId);

    ContentCategory add(ContentCategory contentCategory);

}
