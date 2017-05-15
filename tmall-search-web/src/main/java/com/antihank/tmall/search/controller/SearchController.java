package com.antihank.tmall.search.controller;

import com.antihank.tmall.common.vo.DataGridResult;
import com.antihank.tmall.manage.pojo.Item;
import com.antihank.tmall.search.service.SearchService;
import com.antihank.tmall.search.vo.SolrItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by Antihank on 2017/5/15.
 */
@Controller
@RequestMapping("search")
public class SearchController implements Serializable {

    private static final long serialVersionUID = 6541071491480233084L;
    @Value("${TMALL_SEARCH_PAGE_SIZE}")
    private Integer TMAL_SEARCH_PAGE_SIZE;
    @Autowired
    private SearchService searchService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView search(@RequestParam(value = "q", required = false) String query
            , @RequestParam(value = "page", defaultValue = "1") Integer page) {
        //中文乱码
        /*if (StringUtils.isNotBlank(query)) {
            try {
                query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }*/

        //通过searchService查询
        DataGridResult<SolrItem> result = searchService.searchIndexForItemByQuery(query, page, TMAL_SEARCH_PAGE_SIZE);
        ModelAndView mv = new ModelAndView("search");

        //添加页面需要的同步参数
        mv.addObject("itemList", result.getRows());
        mv.addObject("query", query);
        mv.addObject("totalPages", (result.getTotal() + TMAL_SEARCH_PAGE_SIZE - 1) / TMAL_SEARCH_PAGE_SIZE);
        mv.addObject("page", page);

        return mv;
    }
}
