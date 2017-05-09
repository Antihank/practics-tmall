package com.antihank.tmall.manage.service.impl;

import com.antihank.tmall.manage.mapper.ContentCategoryMapper;
import com.antihank.tmall.manage.pojo.ContentCategory;
import com.antihank.tmall.manage.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by Antihank on 2017/5/8.
 */
@Service
public class ContentCategoryServiceImpl extends BaseServiceImpl<ContentCategory> implements ContentCategoryService {
    private static final long serialVersionUID = -4253604413973090652L;

    @Autowired
    private ContentCategoryMapper contentCategoryMapper;

    @Override
    public void delete(ContentCategory contentCategory) {
        deleteByIdAsRecursion(contentCategory.getId());
    }

    private void deleteByIdAsRecursion(Long id) {
        ContentCategory cc = contentCategoryMapper.selectByPrimaryKey(id);
        if (cc.getIsParent()) {
            Example e = new Example(ContentCategory.class);
            Example.Criteria c = e.createCriteria();
            c.andEqualTo("parentId", cc.getId());
            List<ContentCategory> ccList = contentCategoryMapper.selectByExample(e);
            for (ContentCategory ccc : ccList) {
                deleteByIdAsRecursion(ccc.getId());
            }
            contentCategoryMapper.deleteByPrimaryKey(cc.getId());
        } else {
            deleteByPrimaryKey(cc.getId());
        }
    }

    @Override
    public List<ContentCategory> selectByParentId(Long parentId) {
        Example e = new Example(ContentCategory.class);
        Example.Criteria c = e.createCriteria();
        c.andEqualTo("parentId", parentId);
        return contentCategoryMapper.selectByExample(e);
    }

    @Override
    public ContentCategory add(ContentCategory contentCategory) {
        Example e = new Example(ContentCategory.class);
        Example.Criteria c = e.createCriteria();
        c.andEqualTo("parentId", contentCategory.getParentId());
        int count = contentCategoryMapper.selectCountByExample(e);

        //设置缺失属性
        contentCategory.setIsParent(false);
        contentCategory.setSortOrder(count + 1);
        contentCategory.setStatus(1);

        //插入
        insertSelective(contentCategory);

        //维护关系
        ContentCategory p = new ContentCategory();
        p.setId(contentCategory.getParentId());
        p.setIsParent(true);

        updateByPrimaryKeySelective(p);
        return contentCategory;
    }
}
