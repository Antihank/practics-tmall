package com.antihank.tmall.search.service.impl;

import com.antihank.tmall.common.vo.DataGridResult;
import com.antihank.tmall.search.service.SearchService;
import com.antihank.tmall.search.vo.SolrItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Antihank on 2017/5/14.
 */
@Service
public class SearchServiceImpl implements SearchService, Serializable {
    private static final long serialVersionUID = 4471880409108236280L;

    @Autowired
    private CloudSolrServer cloudSolrServer;


    @Override
    public void saveOrUpdateSolrItemList(List<SolrItem> solrList) {
        try {
            cloudSolrServer.addBeans(solrList);
            cloudSolrServer.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DataGridResult<SolrItem> searchIndexForItemByQuery(String query, Integer page, Integer pageSize) {
        //判断查询语句是否存在
        boolean isHighlight = StringUtils.isNoneBlank(query);

        if (StringUtils.isBlank(query)) {
            query = "*";
        }
        //设置查询对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询标题
        solrQuery.setQuery("title:" + query + " AND status:1");

        //分页
        solrQuery.setStart((page - 1) * pageSize);
        solrQuery.setRows(pageSize);

        //高亮
        //没有查询条件就不需要高亮
        solrQuery.setHighlight(isHighlight);
        solrQuery.addHighlightField("title");
        solrQuery.setHighlightSimplePre("<em>");
        solrQuery.setHighlightSimplePost("</em>");

        QueryResponse response = null;
        try {
            response = cloudSolrServer.query(solrQuery);

        } catch (SolrServerException e) {
            e.printStackTrace();
        }

        //获取需要的数据
        List<SolrItem> list = response.getBeans(SolrItem.class);

        long total = response.getResults().getNumFound();


        if (isHighlight && list != null && list.size() != 0) {
            //将高亮设置进去
            Map<String, Map<String, List<String>>> map = response.getHighlighting();
            for (SolrItem item : list) {
                Map<String, List<String>> field = map.get(item.getId().toString());
                if (field.size() == 0) {
                    continue;
                }
                String highlightingField = field.get("title").get(0);
                item.setTitle(highlightingField);
            }
        }
        DataGridResult<SolrItem> result = new DataGridResult<>();
        result.setRows(list);
        result.setTotal(total);
        return result;
    }

    @Override
    public void saveOrUpdateSolrItem(SolrItem solrItem) {
        try {
            cloudSolrServer.addBean(solrItem);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSolrItemById(Long itemId) {
        try {
            cloudSolrServer.deleteById(itemId.toString());
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
