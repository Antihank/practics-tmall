package com.antihank.tmall.manage.controller;

import com.antihank.tmall.manage.pojo.ItemCat;
import com.antihank.tmall.manage.service.ItemCatService;
import org.omg.CORBA.BAD_CONTEXT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Antihank on 2017/5/4.
 */
@Controller
@RequestMapping("/item/cat")
public class ItemCatController implements Serializable {
    private static final long serialVersionUID = 7068505639250114532L;

    @Autowired
    private ItemCatService itemCatService;

    /**
     * 根据父id获取商品类目lieb
     *
     * @param parentId 商品类目父id，默认为0
     * @return JSON格式的列表
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity list(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        ItemCat itemCat = new ItemCat();
        itemCat.setParentId(parentId);
        List<ItemCat> list = itemCatService.selectAllAsList(itemCat);
        return ResponseEntity.status(list.size() == 0 ? HttpStatus.BAD_REQUEST : HttpStatus.OK).body(list);
    }

    /**
     * 根据商品类目id查询
     *
     * @param itemCatId 商品类目id
     * @return JSON格式的对象
     */
    @RequestMapping(value = "/{itemCatId}", method = RequestMethod.GET)
    public ResponseEntity<ItemCat> queryItemCatById(@PathVariable("itemCatId") Long itemCatId) {
        ItemCat itemCat = itemCatService.selectByPrimaryKey(itemCatId);
        return ResponseEntity.status(itemCat == null ? HttpStatus.BAD_REQUEST : HttpStatus.OK).body(itemCat);
    }
}
