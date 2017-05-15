package com.antihank.tmall.manage.controller;

import com.antihank.tmall.common.vo.DataGridResult;
import com.antihank.tmall.manage.pojo.Item;
import com.antihank.tmall.manage.service.ItemService;
import com.github.pagehelper.PageInfo;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Topic;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Antihank on 2017/5/4.
 */
@Controller
@RequestMapping("/item")
public class ItemController implements Serializable {

    private static final long serialVersionUID = 5350306013423840417L;
    @Autowired
    private ItemService itemService;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Topic itemTopicDestination;

    /**
     * 新增商品
     *
     * @param item 商品对象
     * @param desc 商品描述
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity add(Item item, @RequestParam(value = "desc", required = false) String desc) {
        try {
            Long id = itemService.save(item, desc);
            sendMQMessage("update",id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 根据title的条件查询列表
     *
     * @param title 查询条件
     * @param page  当前页
     * @param rows  页面大小
     * @return DataGrid格式的JSON
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<DataGridResult> list(@RequestParam(value = "title", required = false) String title, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "rows", defaultValue = "30") Integer rows) {

        PageInfo<Item> info = itemService.selectAllByTitle(title, page, rows);
        long total = info.getTotal();
        List<Item> list = info.getList();

        return ResponseEntity.status(list.size() == 0 ? HttpStatus.BAD_REQUEST : HttpStatus.OK).body(new DataGridResult(total, list));
    }

    /**
     * 更新物品以及其描述信息
     *
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResponseEntity update(Item item, @RequestParam("desc") String desc) {

        try {
            itemService.updateItem(item, desc);
            sendMQMessage("update", item.getId());
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * 根据物品id删除物品,物品id用,隔开
     *
     * @param ids 物品id
     * @return
     */
    @RequestMapping("delete")
    public ResponseEntity delete(@RequestParam("ids") String ids) {
        try {
            String[] arr = ids.split(",");
            itemService.fakeDelete(arr);
            for (String s : arr) {
                sendMQMessage("delete", Long.parseLong(s));
            }
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * 根据物品id下架物品,物品id用,隔开
     *
     * @param ids 物品id
     * @return
     */
    @RequestMapping("instock")
    public ResponseEntity instock(@RequestParam("ids") String ids) {
        try {
            String[] arr = ids.split(",");
            itemService.instock(arr);
            for (String s : arr) {
                sendMQMessage("delete", Long.parseLong(s));
            }
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * 根据物品id上架物品,物品id用,隔开
     *
     * @param ids 物品id
     * @return
     */
    @RequestMapping("reshelf")
    public ResponseEntity reshelf(@RequestParam("ids") String ids) {
        try {
            String[] arr = ids.split(",");
            itemService.reshelf(ids.split(","));
            for (String s : arr) {
                sendMQMessage("delete", Long.parseLong(s));
            }
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * 通用发送mq消息 --发送更新索引消息
     *
     * @param type   更新类型——delete:删除；update；更新或者新增
     * @param itemId 数据库记录id
     */
    private void sendMQMessage(final String type, final Long itemId) {
        jmsTemplate.send(itemTopicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ActiveMQMapMessage map = new ActiveMQMapMessage();
                map.setString("type", type);
                map.setLong("itemId", itemId);
                return map;
            }
        });
    }
}
