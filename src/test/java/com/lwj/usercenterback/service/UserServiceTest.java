package com.lwj.usercenterback.service;

import com.lwj.usercenterback.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Test
    public void testRegister(){
        String username = "";
        String password = "123456";
        String checkPassword = "123456";
        long result = userService.userRegister(username, password, checkPassword);
        assertEquals(result,-1);
        username = "lwj";
        result = userService.userRegister(username, password, checkPassword);
        assertEquals(result,-1);
        username = "lwjbb";
        result = userService.userRegister(username, password, checkPassword);
        assertEquals(result,-1);
        username = "lwjbb*";
        password = "12345678";
        checkPassword="12345679";
        result = userService.userRegister(username, password, checkPassword);
        assertEquals(result,-1);
        result = userService.userRegister(username, password, checkPassword);
        assertEquals(result,-1);
        username = "lwjcc";
        checkPassword="12345678";
        result = userService.userRegister(username, password, checkPassword);
        assertEquals(result,1);
        result = userService.userRegister(username, password, checkPassword);
        assertEquals(result,-1);


    }
}