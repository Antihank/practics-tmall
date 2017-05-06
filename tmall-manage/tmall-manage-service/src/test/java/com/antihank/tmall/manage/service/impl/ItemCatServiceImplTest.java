package com.antihank.tmall.manage.service.impl;

import com.antihank.tmall.manage.pojo.ItemCat;
import com.antihank.tmall.manage.service.ItemCatService;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * Created by Antihank on 2017/5/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/applicationContext-dao.xml", "classpath:spring/applicationContext-service.xml"})
public class ItemCatServiceImplTest {

    @Resource
    private ItemCatService service;

    @Test
    public void selectByPrimaryKey() throws Exception {
        assert service != null;
        ItemCat itemCat = service.selectByPrimaryKey(1L);
        assert itemCat != null;
    }

    @Test
    public void selectAll() throws Exception {
        PageInfo<ItemCat> page = service.selectAll(1, 10);
        assert page != null;
        int size = page.getSize();
        assert size != 0;
    }

    @Test
    public void selectAll1() throws Exception {
        Example example = new Example(ItemCat.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("name", "%图书%");
        PageInfo<ItemCat> page = service.selectAll(1, 10, example);
        assert page != null;
        int size = page.getSize();
        System.out.println(size);
    }

    @Test
    public void insertSelective() throws Exception {
        HashMap<Integer, String> map = new HashMap<>();


    }

    @Test
    public void updateByPrimaryKeySelective() throws Exception {

    }

    @Test
    public void deleteByPrimaryKey() throws Exception {

    }

}