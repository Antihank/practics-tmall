package com.antihank.tmall.portal.controller;

import com.antihank.tmall.portal.utils.CookieUtils;
import com.antihank.tmall.sso.pojo.User;
import com.antihank.tmall.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Antihank on 2017/5/11.
 */
@Controller
@RequestMapping("user")
public class UserController implements Serializable {

    private static final long serialVersionUID = -582756751232452417L;

    @Autowired
    private UserService userService;

    @Value("${TMALL_PORTAL_USER_COOKIE_KEY}")
    private String TMALL_PORTAL_USER_COOKIE_KEY;
    @Value("${TMALL_PORTAL_USER_COOKIE_TIME}")
    private Integer TMALL_PORTAL_USER_COOKIE_TIME;

    /**
     * 注册
     *
     * @param user 参数对象
     * @return Map格式的json对象，包含status元素
     */
    @RequestMapping(value = "doRegister", method = RequestMethod.POST)
    public ResponseEntity register(User user) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("status", 500);

        try {
            userService.saveUser(user);
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(map);
    }

    /**
     * 通过用户名和密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @param request  请求对象
     * @param response 响应对象
     * @return
     */
    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public ResponseEntity login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("status", 500);

        try {
            //获取ticket
            String ticket = userService.login(username, password);
            if (ticket == null) {
                return ResponseEntity.status(404).body(null);
            }
            CookieUtils.setCookie(request, response, TMALL_PORTAL_USER_COOKIE_KEY, ticket, TMALL_PORTAL_USER_COOKIE_TIME, true);
            map.put("status", 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(map);
    }

    public String logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            String ticket = CookieUtils.getCookieValue(request, TMALL_PORTAL_USER_COOKIE_KEY, true);
            userService.logout(TMALL_PORTAL_USER_COOKIE_KEY + ticket);
            CookieUtils.setCookie(request, response, TMALL_PORTAL_USER_COOKIE_KEY, "", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:index.html";
    }


}
