package com.antihank.tmall.search.test;

import com.antihank.tmall.manage.pojo.Item;
import com.antihank.tmall.manage.service.ItemService;
import com.antihank.tmall.search.service.SearchService;
import com.antihank.tmall.search.vo.SolrItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antihank on 2017/5/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/tmall-search-web-servlet.xml")
public class ImportSolrIndexTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private SearchService searchService;
    private List<SolrItem> solrList;

    @Before
    public void setUp() throws Exception {
        solrList = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            SolrItem item = new SolrItem();
            solrList.add(item);
        }
    }

    @Test
    public void importTest() throws Exception {
        //穷举分页获取所有item
        int page = 1;
        int rows = 500;

        do {
            //分页装入
            List<Item> list = itemService.selectAllAsList(page, rows);

            for (int i = 0; i < list.size(); i++) {
                Item item = list.get(i);
                SolrItem solrItem = solrList.get(i);
                solrItem.setId(item.getId());
                solrItem.setImage(item.getImage());
                solrItem.setPrice(item.getPrice());
                solrItem.setSellPoint(item.getSellPoint());
                solrItem.setStatus(item.getStatus());
                solrItem.setTitle(item.getTitle());
            }

            searchService.saveOrUpdateSolrItemList(solrList);
            page++;
            rows = list.size();
            System.out.println("导入第" + page + "页，导入了" + rows + "条。");
        } while (rows == 500);
    }
}
