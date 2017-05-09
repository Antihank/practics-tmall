package com.antihank.tmall.manage.service.impl;

import com.antihank.tmall.manage.pojo.Content;
import com.antihank.tmall.manage.service.ContentService;
import com.antihank.tmall.manage.service.redis.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Antihank on 2017/5/8.
 */
@Service
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService {
    private static final long serialVersionUID = -351188303008872594L;

    @Value("${TMALL_PORTAL_BIG_AD_CATEGORY_ID}")
    private Integer TMALL_PORTAL_BIG_AD_CATEGORY_ID;
    @Value("${TMALL_PORTAL_BIG_AD_AMOUNT}")
    private Integer TMALL_PORTAL_BIG_AD_AMOUNT;
    @Value("${TMALL_PORTAL_BIG_AD_CACHE_KEY}")
    private String TMALL_PORTAL_BIG_AD_CACHE_KEY;
    @Value("${TMALL_PORTAL_BIG_AD_CACHE_SECOND}")
    private Integer TMALL_PORTAL_BIG_AD_CACHE_SECOND;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisService redisService;

    @Override
    public PageInfo<Content> selectByCategoryId(Integer page, Integer rows, Long categoryId) {

        Example e = new Example(Content.class);
        Example.Criteria c = e.createCriteria();
        c.andEqualTo("categoryId", categoryId);
        e.orderBy("updated").desc();
        return selectAll(page, rows, e);
    }

    @Override
    public String selectContentAsBigAdForPortal() {
        //查看是否有缓存
        try {
            String s = redisService.get(TMALL_PORTAL_BIG_AD_CACHE_KEY);
            if (StringUtils.isNotBlank(s)) {
                return s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取最新的6条 大广告 内容数据
        PageInfo<Content> info = selectByCategoryId(1, TMALL_PORTAL_BIG_AD_AMOUNT, TMALL_PORTAL_BIG_AD_CATEGORY_ID.longValue());
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (Content c : info.getList()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("alt", "");
            map.put("height", 240);
            map.put("heightB", 240);
            map.put("href", c.getUrl());
            map.put("src", c.getPic());
            map.put("srcB", c.getPic());
            map.put("width", 670);
            map.put("widthB", 550);
            list.add(map);
        }
        //转json
        String result = null;
        try {
            result = objectMapper.writeValueAsString(list);
            if (StringUtils.isNotBlank(result)) {
                redisService.setex(TMALL_PORTAL_BIG_AD_CACHE_KEY, TMALL_PORTAL_BIG_AD_CACHE_SECOND, result);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
