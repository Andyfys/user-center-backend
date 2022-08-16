package com.andyfys.yupicenter.service.impl;

import com.andyfys.yupicenter.common.ErrorCode;
import com.andyfys.yupicenter.exception.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.andyfys.yupicenter.model.domain.User;
import com.andyfys.yupicenter.service.UserService;
import com.andyfys.yupicenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.andyfys.yupicenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author Andy
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2022-08-10 13:13:10
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private UserMapper userMapper;
    /**
     * 盐值
     */
    private static final String SALT = "andyfys";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        //1. 检查非空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "数据存在空值");
        }
        //2. 校验账户以及密码的长度问题
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户长度过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度过短");
        }
        //校验星球编号长度
        if ( planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号过长");
        }
        //2.1 账户不得包含特殊字符
        String validValue = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%…… &*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validValue).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.3 密码和二次输入密码要相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.2 账户名不得重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        Long aLong = userMapper.selectCount(userQueryWrapper);
        if (aLong > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.2 星球账户不得重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        aLong = userMapper.selectCount(queryWrapper);
        if (aLong > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //3. 密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        //4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        //1. 检查非空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2. 校验账户以及密码的长度问题
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.1 账户不得包含特殊字符
        String validValue = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%…… &*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validValue).matcher(userAccount);
        if (matcher.find()) {
            log.info("user login failed, userAccount Cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //3. 密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        //4.数据库查询信息
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        userQueryWrapper.eq("userPassword", encryptPassword);
        //这里实际上是存在问题的，
        // 第一点，可能会把我们已经逻辑删除的数据给查出来，（使用mybatis-plus自带的功能进行处理）
        // 第二点，没有对查出来的数据进行判断 （ok）
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //5.信息脱敏
        User safetyUser = getSafetyUser(user);
        //5.保存用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /**
     * 获取当前用户
     *
     * @param request 请求
     * @return {@link User}
     */
    @Override
    public User getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        //这里会出现空的情况
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //从数据库实时查询数据，防止信息错乱
        Long id = currentUser.getId();
        //TODO 后续需要对用户权限等进行进一步校验
        User newUser = userMapper.selectById(id);
        return getSafetyUser(newUser);
    }

    /**
     * 用户注销
     *
     * @param request 请求
     * @return {@link Integer}
     */
    @Override
    public Integer userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 得到安全用户
     *
     * @param user 用户
     * @return {@link User}
     */
    @Override
    public User getSafetyUser(User user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User safeUser = new User();
        safeUser.setId(user.getId());
        safeUser.setUsername(user.getUsername());
        safeUser.setUserAccount(user.getUserAccount());
        safeUser.setAvatarUrl(user.getAvatarUrl());
        safeUser.setGender(user.getGender());
        safeUser.setPhone(user.getPhone());
        safeUser.setEmail(user.getEmail());
        safeUser.setUserRole(user.getUserRole());
        safeUser.setUserStatus(user.getUserStatus());
        safeUser.setCreateTime(user.getCreateTime());
        safeUser.setPlanetCode(user.getPlanetCode());
        return safeUser;
    }
}




