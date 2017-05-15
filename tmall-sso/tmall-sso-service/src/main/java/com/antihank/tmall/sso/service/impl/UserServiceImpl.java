package com.antihank.tmall.sso.service.impl;

import com.antihank.tmall.common.service.redis.RedisService;
import com.antihank.tmall.sso.mapper.UserMapper;
import com.antihank.tmall.sso.pojo.User;
import com.antihank.tmall.sso.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by Antihank on 2017/5/11.
 */
@Service
public class UserServiceImpl implements UserService, Serializable {

    private static final long serialVersionUID = -8794429313282815648L;
    @Autowired
    private UserMapper userMapper;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisService redisService;

    @Value("${TMALL_SSO_USER_CACHE_TIME}")
    private Integer TMALL_SSO_USER_CACHE_TIME;
    @Value("${TMALL_SSO_USER_CACHE_KEY_PREFIX}")
    private String TMALL_SSO_USER_CACHE_KEY_PREFIX;

    @Override
    public Boolean checkForExistence(String param, Integer type) {
        //判断type
        boolean b = false;
        User u = new User();
        switch (type) {
            case 1:
                //查询用户名
                u.setUsername(param);
                break;
            case 2:
                //查询电话号码
                u.setPhone(param);
                break;
            default:
                //查询邮箱
                u.setEmail(param);
                break;
        }

        int c = userMapper.selectCount(u);
        return c == 0;
    }

    @Override
    public String checkForLoginCache(String ticket) {
        //使用前缀+ticket生成key
        String key = TMALL_SSO_USER_CACHE_KEY_PREFIX + ticket;
        //查询Redis
        String value = redisService.get(key);

        if (StringUtils.isBlank(value)) {
            return value;
        }

        //假如用户存在，touch，表示活跃
        redisService.setex(key, TMALL_SSO_USER_CACHE_TIME, value);

        return value;
    }

    @Override
    public void saveUser(User user) {
        user.setCreated(new Date(System.currentTimeMillis()));
        user.setUpdated(user.getCreated());
        //加密密码
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        userMapper.insertSelective(user);
    }

    @Override
    public String login(String username, String password) {
        User param = new User();
        param.setUsername(username);
        param.setPassword(DigestUtils.md5Hex(password));

        User user = userMapper.selectOne(param);
        if (user == null) {
            return null;
        }
        //生成ticket
        String ticket = DigestUtils.md5Hex(username + System.currentTimeMillis());

        String key = TMALL_SSO_USER_CACHE_KEY_PREFIX + ticket;
        //写入redis
        try {
            redisService.setex(key, TMALL_SSO_USER_CACHE_TIME, objectMapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ticket;
    }

    @Override
    public void logout(String redisKey) {
        if (StringUtils.isBlank(redisKey)) {
            throw new RuntimeException("redisKey为空");
        }
        try {
            redisService.del(redisKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
