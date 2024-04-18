package com.lwj.usercenterback.service;

import com.lwj.usercenterback.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author lwj
* @description 针对表【user】的数据库操作Service
* @createDate 2024-03-22 21:28:03
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param username
     * @param password
     * @param checkPassword
     * @return
     */
    long userRegister(String username,String password,String checkPassword);

    /**
     * 用户登录
     * @param username
     * @param password
     * @param request
     * @return
     */
    User userLogin(String username, String password, HttpServletRequest request);

    /**
     * 脱敏
     * @param oringinalUser
     * @return
     */
    User safetyUser(User oringinalUser);

    int deleteUser(long id);
}
