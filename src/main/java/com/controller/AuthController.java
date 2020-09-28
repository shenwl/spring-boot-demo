package com.controller;

import com.entity.User;
import com.service.UserService;
import com.utils.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User loggedUser = userService.getUserByUserName(authentication == null ? null : authentication.getName());
        if (loggedUser == null) {
            return new HttpResponse(401, "未登录");
        }
        return new HttpResponse(200, loggedUser, "");
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedUser = userService.getUserByUserName(userName);
        if (loggedUser == null) {
            return new HttpResponse(401, "用户尚未登录");
        }
        SecurityContextHolder.clearContext();
        return new HttpResponse(200, "已注销");
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Object register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        String avatar = usernameAndPassword.get("avatar");

        if (username.length() < 1 || password.length() < 1) {
            return new HttpResponse(400, "用户名密码不能为空");
        }

        User user = userService.getUserByUserName(username);

        if (user != null) {
            return new HttpResponse(400, "用户已存在");
        }

        userService.save(username, password, avatar);
        return new HttpResponse(200, "注册成功");
    }

    @PostMapping("/auth/login")
    public HttpResponse login(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");

        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return new HttpResponse(400, "用户不存在");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, password, userDetails.getAuthorities()
        );

        try {
            authenticationManager.authenticate(token);
            // 保存用户信息
            SecurityContextHolder.getContext().setAuthentication(token);

            User loggedInUser = new User(1, "Mike");
            return new HttpResponse(200, loggedInUser, "Mike");
        } catch (BadCredentialsException e) {
            return new HttpResponse(400, "用户不存在");
        }
    }
}
