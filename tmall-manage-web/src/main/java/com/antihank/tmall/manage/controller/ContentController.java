package com.antihank.tmall.manage.controller;

import com.antihank.tmall.common.vo.DataGridResult;
import com.antihank.tmall.manage.pojo.Content;
import com.antihank.tmall.manage.service.ContentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

/**
 * Created by Antihank on 2017/5/8.
 */
@Controller
@RequestMapping("content")
public class ContentController implements Serializable {

    private static final long serialVersionUID = 4129588521249631639L;
    @Autowired
    private ContentService contentService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity list(@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "rows", defaultValue = "20") Integer rows) {

        PageInfo<Content> info = contentService.selectByCategoryId(page, rows, categoryId);
        DataGridResult<Content> result = new DataGridResult<Content>();
        result.setTotal(info.getTotal());
        result.setRows(info.getList());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity add(Content content) {
        try {
            contentService.insertSelective(content);
            return ResponseEntity.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResponseEntity update(Content content) {
        try {
            contentService.updateByPrimaryKeySelective(content);
            return ResponseEntity.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }


    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseEntity delete(@RequestParam("ids") String ids) {
        try {
            String[] arr = ids.split(",");
            Long[] longs = new Long[arr.length];
            for (int i = 0; i < arr.length; i++) {
                longs[i] = Long.parseLong(arr[i]);
            }
            contentService.deleteByPrimaryKeys(longs);
            return ResponseEntity.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }
}
