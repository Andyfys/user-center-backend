package com.andyfys.yupicenter;

import java.util.Date;


import com.andyfys.yupicenter.model.domain.User;
import com.andyfys.yupicenter.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class YupiCenterApplicationTests {

    @Resource
    private UserService userService;


//    @Test
//    @DisplayName("测试mybatis-plus")
//    void contextLoads() {
//        User user = new User();
//        user.setUsername("andy");
//        user.setUserAccount("123");
//        user.setAvatarUrl("https://shouyou.3dmgame.com/newpage/images/logo.png");
//        user.setGender(0);
//        user.setUserPassword("111");
//        user.setPhone("123");
//        user.setEmail("456");
//        user.setPlanetCode("xxx");
//        boolean userSave = userService.save(user);
//        System.out.println("userSave = " + userSave);
//        assertTrue(userSave);
//    }
//
//    @Test
//    @DisplayName("测试注册功能")
//    void testRegister() {
//        //1.测试非空
//        String userAccount = "andy";
//        String userPassword = "";
//        String checkPassword = "12345678";
//        String planetCode = "12345";
//        long register = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
//        assertEquals(-1, register);
//        userAccount = "";
//        userPassword = "12345678";
//        register = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
//        assertEquals(-1, register);
//        userAccount = "andy";
//        userPassword = "12345678";
//        checkPassword = "";
//        register = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
//        assertEquals(-1, register);
//        //2.测试用户名和密码合理性
//        userAccount = "aaa";
//        userPassword = "12345678";
//        checkPassword = "12345678";
//        register = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
//        assertEquals(-1, register);
//        userAccount = "andy";
//        userPassword = "123456";
//        checkPassword = "12345678";
//        //3.测试特殊字符
//        register = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
//        assertEquals(-1, register);
//        userAccount = "an dy";
//        userPassword = "12345678";
//        checkPassword = "12345678";
//        register = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
//        assertEquals(-1, register);
//        //4.测试是否重复
//        userAccount = "1234";
//        userPassword = "12345678";
//        checkPassword = "12345678";
//        register = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
//        assertEquals(-1, register);
//        //5.测试两次密码不一致
//        userAccount = "andy";
//        userPassword = "123456789";
//        checkPassword = "12345678";
//        register = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
//        assertEquals(-1, register);
//        //6.测试整体功能
//        userAccount = "hahaha";
//        userPassword = "12345678";
//        checkPassword = "12345678";
//        register = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
//        assertEquals(-1, register);
//        //7.测试星球编号长度
//        userAccount = "bababa";
//        userPassword = "12345678";
//        checkPassword = "12345678";
//        planetCode = "123456";
//        register = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
//        assertEquals(-1, register);
//        //8.测试星球编号唯一性
//        userAccount = "ahahah";
//        userPassword = "12345678";
//        checkPassword = "12345678";
//        planetCode = "12345";
//        register = userService.userRegister(userAccount, userPassword, checkPassword,planetCode);
//        assertEquals(-1, register);
//    }
}
