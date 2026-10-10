package com.missyang.usercenter1.service;

import com.missyang.usercenter1.model.domain.User;
import com.baomidou.mybatisplus.spring.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author missyang
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @author missyang
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    int userLogout(HttpServletRequest request);
}
