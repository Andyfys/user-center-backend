package com.andyfys.yupicenter.controller;

import com.andyfys.yupicenter.common.BaseResponse;
import com.andyfys.yupicenter.common.ErrorCode;
import com.andyfys.yupicenter.exception.BusinessException;
import com.andyfys.yupicenter.model.domain.User;
import com.andyfys.yupicenter.model.request.UserLoginRequest;
import com.andyfys.yupicenter.model.request.UserRegisterRequest;
import com.andyfys.yupicenter.service.UserService;
import com.andyfys.yupicenter.common.utils.ResultUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.andyfys.yupicenter.constant.UserConstant.ADMIN_ROLE;
import static com.andyfys.yupicenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 * 以json格式返回数据
 *
 * @author Andyfys
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     *
     * @param userRegisterRequest 用户注册请求
     * @return {@link Long}
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //规范写法如下
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword,planetCode)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        long register = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(register);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录请求
     * @param request          请求
     * @return {@link User}
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //规范写法如下
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 获取当前用户
     *
     * @param request 请求
     * @return {@link User}
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User currentUser = userService.getCurrentUser(request);
        return ResultUtils.success(currentUser);
    }


    /**
     * 用户注销
     *
     * @param request 请求
     * @return {@link Integer}
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 搜索用户
     * 问题：查询字段过多(解决)
     *
     * @param username 用户名
     * @param request  请求
     * @return {@link List}<{@link User}>
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        //鉴权
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            //这里属于偷懒行为，正常还是要将这种逻辑写入到service中的
            userQueryWrapper.like("username", username);
        }
        //执行手动脱敏
        List<User> list = userService.list(userQueryWrapper);
        List<User> userList = list.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(userList);
    }

    /**
     * 删除用户
     *
     * @param id      id
     * @param request 请求
     * @return boolean
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        //鉴权
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }


    /**
     * 管理员鉴权
     *
     * @param request 请求
     * @return boolean
     */
    private boolean isAdmin(HttpServletRequest request) {
        //！！！管理员鉴权
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        //这里会出现空的情况
        User user = (User) userObj;
        // 用户权限 0 - 普通用户 1 - 管理员
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
