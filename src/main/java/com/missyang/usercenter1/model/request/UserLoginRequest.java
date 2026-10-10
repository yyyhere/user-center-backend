package com.missyang.usercenter1.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author missyang
 */
@Data

public class UserLoginRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -5533563159178208504L;

    private String userAccount;

    private String userPassword;

}
