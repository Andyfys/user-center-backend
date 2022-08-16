package com.andyfys.yupicenter.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Andyfys
 * @version 1.0
 */
@Data
public class UserLoginRequest implements Serializable {


    private static final long serialVersionUID = -3015013868337507244L;
    private String userAccount;
    private String userPassword;

}
