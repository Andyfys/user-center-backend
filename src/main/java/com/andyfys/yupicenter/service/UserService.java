package com.andyfys.yupicenter.service;

import com.andyfys.yupicenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author Andy
* @description 针对表【user】的数据库操作Service
* @createDate 2022-08-10 13:13:10
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户帐户
     * @param userPassword  用户密码
     * @param checkPassword 二次检查密码
     * @return long
     */
    long userRegister(String userAccount,String userPassword,String checkPassword,String planetCode);

    /**
     * 用户登录
     *
     * @param userAccount  用户帐户
     * @param userPassword 用户密码
     * @param request      请求
     * @return {@link User}
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getCurrentUser(HttpServletRequest request);

    Integer userLogout(HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param user 用户
     * @return {@link User}
     */
    User getSafetyUser(User user);
}
