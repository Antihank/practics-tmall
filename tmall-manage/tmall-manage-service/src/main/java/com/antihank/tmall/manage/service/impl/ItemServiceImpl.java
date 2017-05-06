package com.antihank.tmall.manage.service.impl;

import com.antihank.tmall.manage.mapper.ItemDescMapper;
import com.antihank.tmall.manage.pojo.Item;
import com.antihank.tmall.manage.pojo.ItemDesc;
import com.antihank.tmall.manage.service.ItemService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Date;

/**
 * Created by Antihank on 2017/5/4.
 */
@Service
public class ItemServiceImpl extends BaseServiceImpl<Item> implements ItemService {
    private static final long serialVersionUID = 5439127468074786550L;

    @Autowired
    private ItemDescMapper itemDescMapper;

    @Override
    public Long save(Item item, String itemDesc) {
        //保存
        insertSelective(item);

        ItemDesc i = new ItemDesc();
        i.setCreated(new Date(System.currentTimeMillis()));
        i.setUpdated(i.getCreated());
        i.setItemId(item.getId());
        i.setItemDesc(itemDesc);
        itemDescMapper.insertSelective(i);

        return item.getId();
    }

    @Override
    public PageInfo<Item> selectAllByTitle(String title, Integer page, Integer rows) {
        if (title != null) {
            try {
                title = URLDecoder.decode(title, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Example e = new Example(Item.class);
            Example.Criteria c = e.createCriteria();
            c.andLike("title", "%" + title + "%");
            return selectAll(page, rows, e);
        } else {
            return selectAll(page, rows);
        }
    }

    @Override
    public void updateItem(Item item, String desc) {
        //选择性更新
        updateByPrimaryKeySelective(item);

        //更新 描述信息
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setUpdated(item.getUpdated());
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDescMapper.updateByPrimaryKey(itemDesc);
    }

    @Override
    public void instock(String[] ids) {
        for (String id : ids) {
            Item item = new Item();
            item.setId(Long.parseLong(id));
            item.setStatus(2);
            updateByPrimaryKeySelective(item);
        }
    }

    @Override
    public void reshelf(String[] ids) {
        for (String id : ids) {
            Item item = new Item();
            item.setId(Long.parseLong(id));
            item.setStatus(1);
            updateByPrimaryKeySelective(item);
        }
    }

    @Override
    public void fakeDelete(String[] ids) {
        for (String id : ids) {
            Item item = new Item();
            item.setId(Long.parseLong(id));
            item.setStatus(3);
            updateByPrimaryKeySelective(item);
        }
    }
}
