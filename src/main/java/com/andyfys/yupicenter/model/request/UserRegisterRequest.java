package com.andyfys.yupicenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andyfys
 * @version 1.0
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = 2631030942563559502L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
