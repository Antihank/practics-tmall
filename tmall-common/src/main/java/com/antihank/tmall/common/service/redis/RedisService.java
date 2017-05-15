package com.antihank.tmall.common.service.redis;

/**
 * Created by Antihank on 2017/5/9.
 */
public interface RedisService {
    // 设置
    String set(String key, String value);

    // 设置并同时设置过期时间
    String setex(String key, int seconds, String value);

    // 设置key过期
    Long expire(String key, int seconds);

    // 获取key值
    String get(String key);

    // 删除key
    Long del(String key);

    // 自增
    Long incr(String key);
}
