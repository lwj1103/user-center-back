package com.lwj.usercenterback.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lwj.usercenterback.common.BaseResponse;
import com.lwj.usercenterback.common.ErrorCode;
import com.lwj.usercenterback.common.ResultUtils;
import com.lwj.usercenterback.exception.BusinessException;
import com.lwj.usercenterback.model.domain.User;
import com.lwj.usercenterback.model.request.DeleteRequest;
import com.lwj.usercenterback.model.request.LoginRequest;
import com.lwj.usercenterback.model.request.RegisterRequest;
import com.lwj.usercenterback.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.Session;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.lwj.usercenterback.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody RegisterRequest request){
        if (request == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String username = request.getUsername();
        String password = request.getPassword();
        String checkPassword = request.getCheckPassword();
        if(StringUtils.isAnyBlank(username,password,checkPassword))
            throw new BusinessException(ErrorCode.NULL_ERROR,"有参数为空");
        long result = userService.userRegister(username, password, checkPassword);
        return ResultUtils.success(result);
    }
    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request){
        if (loginRequest == null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        if(StringUtils.isAnyBlank(username,password))
            throw new BusinessException(ErrorCode.NULL_ERROR,"有参数为空");
        User user = userService.userLogin(username, password, request);
        return ResultUtils.success(user);
    }
    @GetMapping("/current")
    public BaseResponse<User> currentUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(USER_LOGIN_STATE);
        if(user == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN,"未登录");
        Long id = user.getId();
        user = userService.getById(id);
        User safrUser = userService.safetyUser(user);
        return ResultUtils.success(safrUser);
    }
    @GetMapping("/search")
    public List<User> search(String username ,HttpServletRequest request){
        //权限验证,管理员才能操作
        if(!isAdmin(request))
            throw new BusinessException(ErrorCode.NO_AUTH);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(username!=null,User::getUsername,username);
        List<User> list = userService.list(queryWrapper);
        return list;
    }

    @PostMapping("/delete")
    public BaseResponse<Integer> delete(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
        //权限验证，管理员才能操作
        if(!isAdmin(request))
            throw new BusinessException(ErrorCode.NO_AUTH);
        Long id = deleteRequest.getId();
        if(id<0)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        int i = userService.deleteUser(id);
        return ResultUtils.success(i);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> update(@RequestBody User user,HttpServletRequest request){
        //权限验证，管理员才能操作
        if(!isAdmin(request))
            throw new BusinessException(ErrorCode.NO_AUTH);
        boolean b = userService.updateById(user);
        return ResultUtils.success(b);
    }



    private Boolean isAdmin(HttpServletRequest request){
        User user = (User)request.getSession().getAttribute(USER_LOGIN_STATE);
        if(user==null || user.getUserRow() != 1){
            return false;
        }
        return true;
    }
    @PostMapping("/logout")
    public void logout(HttpServletRequest request){
        request.getSession().setAttribute(USER_LOGIN_STATE,null);

    }
}
