package com.antihank.tmall.common.service.redis.impl;

import com.antihank.tmall.common.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;

/**
 * Created by Antihank on 2017/5/9.
 */
public class SingleRedisServiceImpl implements RedisService, Serializable {

    private static final long serialVersionUID = -8963091171650866235L;
    /**
     * 注解表示该注入不是必须的，即使本类被构造，但是没有对象也不会报错。
     */
    @Autowired(required = false)
    private JedisPool jedisPool;

    @Override
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(key, value);
        //将连接返回连接池
        jedis.close();
        return result;
    }

    @Override
    public String setex(String key, int seconds, String value) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.setex(key, seconds, value);
        jedis.close();
        return result;
    }

    @Override
    public Long expire(String key, int seconds) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.expire(key, seconds);
        jedis.close();
        return result;
    }

    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    @Override
    public Long del(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.del(key);
        jedis.close();
        return result;
    }

    @Override
    public Long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.incr(key);
        jedis.close();
        return result;
    }

}
