package com.lwj.usercenterback.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lwj.usercenterback.common.ErrorCode;
import com.lwj.usercenterback.exception.BusinessException;
import com.lwj.usercenterback.model.domain.User;
import com.lwj.usercenterback.service.UserService;
import com.lwj.usercenterback.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Pattern;

import static com.lwj.usercenterback.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author lwj
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-03-22 21:28:03
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public long userRegister(String username, String password, String checkPassword) {
//        1. 非空
        if(StringUtils.isAnyBlank(username,password,checkPassword))
            throw  new BusinessException(ErrorCode.NULL_ERROR);
//        2. 账户长度 **不小于** 4 位
        if (username.length()<4)
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户长度不能小于四位");
//        3. 密码 **不小于** 8 位吧
        if(password.length()<8 || checkPassword.length() < 8)
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"密码不能小于8位");
//        5. 账户不包含特殊字符
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        boolean isSpec = Pattern.compile(regEx).matcher(username).find();
        if(isSpec)
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户名不能包含特殊字符");
//        4. 密码和校验密码相同
        if(!password.equals(checkPassword))
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不相等");
//        5. 账户不能重复
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        Long count = userMapper.selectCount(queryWrapper);
        if(count > 0)
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户名已经被注册");
//      6.对密码进行加密（密码千万不要直接以明文存储到数据库中）
        String encryptPassword = DigestUtils.md5DigestAsHex(password.getBytes());
//        向数据库插入用户数据
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptPassword);
        int insert = userMapper.insert(user);
        if(insert == 0){
            throw  new BusinessException(ErrorCode.SYSTEM_ERROR,"注册失败");
        }
        return user.getId();
    }

    @Override
    public User userLogin(String username, String password, HttpServletRequest request) {
//        1. 非空
        if(StringUtils.isAnyBlank(username,password))
            throw  new BusinessException(ErrorCode.NULL_ERROR);
//        2. 账户长度 **不小于** 4 位
        if (username.length()<4)
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户长度不能小于四位");
//        3. 密码就 **不小于** 8 位吧
        if(password.length()<8)
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"密码不小于8位");
//        4. 账户不包含特殊字符
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        boolean isSpec = Pattern.compile(regEx).matcher(username).find();
        if(isSpec)
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户名不能包含特殊字符");
        // 5.加密
        String encryptPassword = DigestUtils.md5DigestAsHex(password.getBytes());
//        6. 校验密码是否输入正确，要和数据库中的密文密码去对比
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username).eq(User::getPassword,encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null)
            throw  new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码错误");
//        7. 用户信息脱敏，隐藏敏感信息，防止数据库中的字段泄露
        User safetiedUser = safetyUser(user);
//        4. 我们要记录用户的登录态（session），将其存到服务器上（用后端 SpringBoot 框架封装的服务器 tomcat 去记录）cookie
        request.getSession().setAttribute(USER_LOGIN_STATE,safetiedUser);
//        5. 返回脱敏后的用户信息
        return safetiedUser;
    }

    @Override
    public User safetyUser(User oringinalUser){
        if(oringinalUser == null)
            return null;
        User safeUser =  new User();
        safeUser.setId(oringinalUser.getId());
        safeUser.setUsername(oringinalUser.getUsername());
        safeUser.setNickName(oringinalUser.getNickName());
        safeUser.setAvatarUrl(oringinalUser.getAvatarUrl());
        safeUser.setGender(oringinalUser.getGender());
        safeUser.setPhone(oringinalUser.getPhone());
        safeUser.setEmail(oringinalUser.getEmail());
        safeUser.setUserRow(oringinalUser.getUserRow());
        safeUser.setCreateTime(oringinalUser.getCreateTime());
        return safeUser;
    }

    @Override
    public int deleteUser(long id) {
        int i = userMapper.deleteById(id);
        return i;
    }
}




