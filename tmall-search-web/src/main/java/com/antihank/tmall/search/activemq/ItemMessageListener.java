package com.antihank.tmall.search.activemq;

import com.antihank.tmall.manage.pojo.Item;
import com.antihank.tmall.manage.service.ItemService;
import com.antihank.tmall.search.service.SearchService;
import com.antihank.tmall.search.vo.SolrItem;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.Serializable;

/**
 * Created by Antihank on 2017/5/15.
 * 商品消息监听器，在商品数据被更改时更新对应的搜索引擎索引
 */
public class ItemMessageListener implements Serializable, MessageListener {
    private static final long serialVersionUID = -5346788464215877586L;

    @Autowired
    private ItemService itemService;

    @Autowired
    private SearchService searchService;

    @Override
    public void onMessage(Message message) {
        //判断Message是不是MapMessage
        try {
            if (message instanceof ActiveMQMapMessage) {
                ActiveMQMapMessage map = (ActiveMQMapMessage) message;
                String type = map.getString("type");
                Long itemId = map.getLong("itemId");
                switch (type) {
                    case "update":
                        Item item = itemService.selectByPrimaryKey(itemId);
                        SolrItem solrItem = new SolrItem();
                        solrItem.setId(item.getId());
                        solrItem.setImage(item.getImage());
                        solrItem.setPrice(item.getPrice());
                        solrItem.setSellPoint(item.getSellPoint());
                        solrItem.setStatus(item.getStatus());
                        solrItem.setTitle(item.getTitle());

                        searchService.saveOrUpdateSolrItem(solrItem);
                        break;
                    case "delete":
                        searchService.deleteSolrItemById(itemId);
                        break;
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
