package com.antihank.tmall.sso.controller;

import com.antihank.tmall.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

import static com.sun.corba.se.impl.util.RepositoryId.cache;

/**
 * Created by Antihank on 2017/5/11.
 */
@Controller
@RequestMapping("user")
public class UserController implements Serializable {

    private static final long serialVersionUID = 6195770813825880001L;
    @Autowired
    private UserService userService;

    /**
     * 检查数据的可用性
     *
     * @param param    数据
     * @param type     数据类型，1代表用户名，2代表电话号码，3代表邮箱账号
     * @param callback 回调函数名(可选 )
     * @return true可用，false不可用
     */
    @RequestMapping(value = "check/{param}/{type}", method = RequestMethod.GET)
    public ResponseEntity checkForExistence(@PathVariable("param") String param, @PathVariable("type") Integer type,
                                            @RequestParam(value = "callback", required = false) String callback) {
        //判断type是否可用
        if (type > 3 || type < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        String result = "false";
        //调用service
        try {
            Boolean b = userService.checkForExistence(param, type);
            result = b.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //jsonp
        if (StringUtils.isNotBlank(callback)) {
            result = callback + "(" + result + ");";
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 根据ticket查询redis中对应的用户json格式字符串
     *
     * @param ticket   用户登录的标识符
     * @param callback 回调函数名(可选 )
     * @return 用户json格式字符串，需要支持跨域
     */
    @RequestMapping(value = "/{ticket}", method = RequestMethod.GET)
    public ResponseEntity checkForLoginCache(@PathVariable("ticket") String ticket, @RequestParam(value = "callback", required = false) String callback) {
        //测空
        if (StringUtils.isBlank(ticket)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        //调用service
        String cache = null;
        try {
            cache = userService.checkForLoginCache(ticket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //jsonp
        if (StringUtils.isNotBlank(callback)) {
            cache = callback + "(" + cache + ");";
        }
        return ResponseEntity.status(HttpStatus.OK).body(cache);
    }
}
