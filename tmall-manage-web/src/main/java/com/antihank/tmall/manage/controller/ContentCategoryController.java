package com.antihank.tmall.manage.controller;

import com.antihank.tmall.manage.pojo.ContentCategory;
import com.antihank.tmall.manage.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Antihank on 2017/5/8.
 */
@Controller
@RequestMapping("content/category")
public class ContentCategoryController implements Serializable {

    private static final long serialVersionUID = -846632994912100148L;
    @Autowired
    private ContentCategoryService contentCategoryService;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listByParent(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        List<ContentCategory> list = contentCategoryService.selectByParentId(parentId);
        return ResponseEntity.status(list.size() == 0 ? HttpStatus.NOT_FOUND : HttpStatus.OK).body(list);
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity add(ContentCategory contentCategory) {

        try {
            ContentCategory result = contentCategoryService.add(contentCategory);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResponseEntity update(ContentCategory contentCategory) {

        try {
            contentCategoryService.updateByPrimaryKeySelective(contentCategory);
            return ResponseEntity.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseEntity delete(ContentCategory contentCategory) {

        try {
            contentCategoryService.delete(contentCategory);
            return ResponseEntity.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }
}
