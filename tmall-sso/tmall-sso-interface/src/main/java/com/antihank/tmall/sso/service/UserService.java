package com.antihank.tmall.sso.service;

import com.antihank.tmall.sso.pojo.User;

/**
 * Created by Antihank on 2017/5/11.
 */
public interface UserService {
    /**
     * 检查数据的可用性
     *
     * @param param 数据
     * @param type  数据类型，1代表用户名，2代表电话号码，3代表邮箱账号
     * @return
     */
    Boolean checkForExistence(String param, Integer type);

    /**
     * 根据ticket查询redis中对应的用户json格式字符串
     *
     * @param ticket 用户登录的标识符
     * @return 用户json格式字符串，需要支持跨域
     */
    String checkForLoginCache(String ticket);

    /**
     * 保存用户数据，注册
     *
     * @param user
     */
    void saveUser(User user);

    /**
     * 通过用户名和密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    String login(String username, String password);

    /**
     * 根据redisKey来删除redis缓存
     *
     * @param redisKey
     */
    void logout(String redisKey);
}
