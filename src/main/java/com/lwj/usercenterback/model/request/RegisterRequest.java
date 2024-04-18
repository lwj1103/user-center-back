package com.lwj.usercenterback.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 */
@Data
public class RegisterRequest implements Serializable {
    private String username;
    private String password;
    private String checkPassword;

}
