package com.antihank.tmall.manage.service;

import com.antihank.tmall.manage.pojo.Item;
import com.github.pagehelper.PageInfo;

/**
 * Created by Antihank on 2017/5/4.
 */
public interface ItemService extends BaseService<Item> {

    /**
     * 保存新物品
     *
     * @param item     物品
     * @param itemDesc 物品描述信息
     * @return
     */
    Long save(Item item, String itemDesc);

    /**
     * 根据标题分页查询物品
     *
     * @param title 物品标题
     * @param page  查询首页
     * @param rows  页面大小
     * @return
     */
    PageInfo<Item> selectAllByTitle(String title, Integer page, Integer rows);

    /**
     * 更新武平
     *
     * @param item
     * @param desc
     */
    void updateItem(Item item, String desc);

    /**
     * 根据id数组下架物品
     *
     * @param ids id数组
     */
    void instock(String[] ids);

    /**
     * 根据id数组上架物品
     *
     * @param ids id数组
     */
    void reshelf(String[] ids);

    /**
     * 根据id数组假删除
     *
     * @param ids id数组
     */
    void fakeDelete(String[] ids);
}
