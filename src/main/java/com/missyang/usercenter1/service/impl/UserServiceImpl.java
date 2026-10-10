package com.missyang.usercenter1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.missyang.usercenter1.common.ErrorCode;
import com.missyang.usercenter1.exception.BusinessException;
import com.missyang.usercenter1.model.domain.User;
import com.missyang.usercenter1.service.UserService;
import com.missyang.usercenter1.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.missyang.usercenter1.constant.UserConstant;

/**
 * 用户服务实体类
 *
 * @author missyang
*/

@Service
@Slf4j

public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    private static final String SALT = "abcdyyy";

    // 把他放到接口里

    /**
     * 用户注册
     *
     * @author missyang
     *
     * 1、不能为空
     * 2、账号长度不能小于 4
     * 3、密码长度不能小于 8
     * 4、账号不能含有特殊字符
     * 5、密码与校验码不同
     * 6、账户不能重复
     * 7、加密
     * 8、把用户保存一下
     *
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }

        if(userAccount.length() < 4) {
            return -1;
        }

        if(userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(validPattern);
        Matcher matcher = pattern.matcher(userAccount);
        if(matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能含有特殊字符");
        }

        if(!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已存在");
        }

        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 最后保存用户
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptedPassword);

        boolean saveResult = this.save(user);
        if(!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "内部异常，保存用户失败");
        }

        // 如果保存失败，这条代码会返回 null ，但是这个函数是小写 long，所以只能在前面写好，如果保存失败，返回 -1
        return user.getId();

    }

    /**
     * 用户登录
     *
     * @author missyang
     *
     * 1、不能为空
     * 2、账号长度不能小于 4
     * 3、密码长度不能小于 8
     * 4、账号不能含有特殊字符
     * 5、加密
     * 6、账户存在，以及账户和密码得配对
     * 8、把用户保存一下
     *
     */

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if(StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        if(userAccount.length() < 4) {
            return null;
        }

        if(userPassword.length() < 8) {
            return null;
        }

        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(validPattern);
        Matcher matcher = pattern.matcher(userAccount);
        if(matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能含有特殊字符");
        }

        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptedPassword);
        User user =  userMapper.selectOne(queryWrapper);
        if(user == null) {
            log.info("user not exists, or, userAccount can not match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不存在，或者，账号和密码不匹配");
        }

        // 用户信息脱敏，这个是要返回到前端的
        // 这是一个新的空对象，密码坚决不能传回去，更新时间和是否删除就不返回了
        User safetyUser = getSafetyUser(user);

        // 记录登录状态，给搞一个键值对
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    /**
     * 用户脱敏
     */
    @Override
    public User getSafetyUser(User originUser) {
        if(originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserName(originUser.getUserName());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setStatus(originUser.getStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setRole(originUser.getRole());

        return safetyUser;
    }

    /**
     * 注销登录状态
     * @author yyy
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return 1;
    }

}




