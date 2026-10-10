package com.missyang.usercenter1.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author missyang
 */
@Data

public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -8904375038893742181L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

}
