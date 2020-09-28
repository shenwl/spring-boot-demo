package com.service;

import com.entity.User;
import com.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void save(String username, String password, String avatar) {
        userMapper.insertOne(username, bCryptPasswordEncoder.encode(password), avatar != null ? avatar : "");
    }

    public User getUserById(Integer id) {
        return userMapper.findUserById(id);
    }

    public User getUserByUserName(String username) {
        return userMapper.findUserByUsername(username);
    }


    public String getPassword(String username) {
        User user = userMapper.findUserByUsername(username);
        return user.getEncryptedPassword();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("未找到用户，请检查登录名或者密码");
        }
        return new org.springframework.security.core.userdetails.User(
                username,
                user.getEncryptedPassword(),
                Collections.emptyList()
        );
    }
}
