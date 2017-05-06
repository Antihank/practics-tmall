package com.antihank.tmall.manage.controller;

import com.antihank.tmall.manage.pojo.ItemDesc;
import com.antihank.tmall.manage.service.ItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * Created by Antihank on 2017/5/6.
 */
@Controller
@RequestMapping("/item/desc")
public class ItemDescController implements Serializable {

    private static final long serialVersionUID = 9171865294311398031L;
    @Autowired
    private ItemDescService itemDescService;

    /**
     * 根据商品id查询商品描述信息用于回显
     *
     * @param itemDescId
     * @return
     */
    @RequestMapping(value = "/{itemDescId}", method = RequestMethod.GET)
    public ResponseEntity<ItemDesc> queryItemDescById(@PathVariable("itemDescId") Long itemDescId) {
        ItemDesc itemDesc = itemDescService.selectByPrimaryKey(itemDescId);
        return ResponseEntity.status(itemDesc == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK).body(itemDesc);
    }

}
