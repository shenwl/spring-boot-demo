package com.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mvc;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(
                new AuthController(authenticationManager, userService)
        ).build();
    }

    @Test
    void returnNotLoginByDefault() throws Exception {
        mvc.perform(get("/auth")).andExpect(status().isOk()).andExpect(result -> {
            assertTrue(result.getResponse().getContentAsString().contains("401"));
        });
    }

    @Test
    void auth() {
    }

    @Test
    void logout() {
    }

    @Test
    void register() {
    }

    @Test
    void login() throws Exception {
        mvc.perform(get("/auth")).andExpect(status().isOk()).andExpect(result -> {
            assertTrue(result.getResponse().getContentAsString().contains("401"));
        });

        Map<String, String> usernamePassword = new HashMap<>();
        usernamePassword.put("username", "testUser");
        usernamePassword.put("password", "myPwd");

        Mockito.when(userService.loadUserByUsername("testUser")).thenReturn(new User("testUser", bCryptPasswordEncoder.encode("myPwd"), Collections.emptyList()));

        MvcResult response = mvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(usernamePassword)))
                .andExpect(status().isOk())
                .andExpect(ret -> assertTrue(ret.getResponse().getContentAsString().contains("200")))
                .andReturn();

        mvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(res -> {
                    Assertions.assertTrue(res.getResponse().getContentAsString().contains("401"));
                });

        HttpSession session = response.getRequest().getSession();
        Mockito.when(userService.getUserByUserName("testUser")).thenReturn(new com.entity.User(1, "testUser", bCryptPasswordEncoder.encode("myPwd")));

        mvc.perform(get("/auth").session((MockHttpSession) session)).andExpect(status().isOk())
                .andExpect(res -> {
                    Assertions.assertTrue(res.getResponse().getContentAsString().contains("200"));
                });
    }
}