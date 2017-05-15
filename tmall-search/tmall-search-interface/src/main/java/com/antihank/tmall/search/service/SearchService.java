package com.antihank.tmall.search.service;

import com.antihank.tmall.common.vo.DataGridResult;
import com.antihank.tmall.search.vo.SolrItem;

import java.util.List;

/**
 * Created by Antihank on 2017/5/14.
 */
public interface SearchService {
    /**
     * 保存或者更新solr索引
     *
     * @param solrList
     */
    void saveOrUpdateSolrItemList(List<SolrItem> solrList);

    /**
     * 通过查询语句查询索引库
     *
     * @param query    查询语句
     * @param page     页码
     * @param pageSize 页大小
     * @return 标准页数据
     */
    DataGridResult<SolrItem> searchIndexForItemByQuery(String query, Integer page, Integer pageSize);

    /**
     * 保存或更新solr索引
     *
     * @param solrItem
     */
    void saveOrUpdateSolrItem(SolrItem solrItem);

    /**
     * 根据id删除solr索引
     *
     * @param itemId
     */
    void deleteSolrItemById(Long itemId);
}
