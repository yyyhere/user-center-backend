package com.missyang.usercenter1.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.missyang.usercenter1.common.BaseResponse;
import com.missyang.usercenter1.common.ErrorCode;
import com.missyang.usercenter1.common.ResultUtils;
import com.missyang.usercenter1.constant.UserConstant;
import com.missyang.usercenter1.exception.BusinessException;
import com.missyang.usercenter1.model.domain.User;
import com.missyang.usercenter1.model.request.UserLoginRequest;
import com.missyang.usercenter1.model.request.UserRegisterRequest;
import com.missyang.usercenter1.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.missyang.usercenter1.constant.UserConstant.ADMIN_ROLE;
import static com.missyang.usercenter1.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author missyang
 */

@RestController // 你还记得 .json 文件吗，用这个可以生成返回的 .json 文件，在 http 测试文件里面用

@RequestMapping("/user")

public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if(userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没接收到注册请求和注册参数");
        }  // 小写的 long 可以写返回-1，但是大写的 Long 返回要写 null，跟前面讲也有一个对应

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        // return userService.userRegister(userAccount, userPassword, checkPassword);
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);

    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if(userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没接收到登录请求和登录参数");
        }  // 小写的 long 可以写返回-1，但是大写的 Long 返回要写 null，跟前面讲也有一个对应

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // return userService.userLogin(userAccount, userPassword, request);
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if(request == null) {
            return null;
        }  // 小写的 long 可以写返回-1，但是大写的 Long 返回要写 null，跟前面讲也有一个对应

        // return userService.userLogout(request);
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObject;
        if(currentUser == null) {   // 没登录过
            return null;
        }
        // 如果已经登录过了,去数据库取他的真实数据，就是除了账号密码以外的id，就是去数据库把他全部玩意都给取出来
        long userId = currentUser.getId();
        // TODO 后面要校验用户是否合法，就是被删除了没，就是删除那玩意被设置成 1 没
        User user = userService.getById(userId);  // 这个是 mybatis-plus 里面自带的函数，几天不写java，又忘记了。
        // 如果去查数据库查出来为空，其实我也不知道为啥查数据库是空，可能是人为从数据库里删除了一些东西
        // getSafetyUser(user) 这个函数是需要判断一下是否为空的
        // return userService.getSafetyUser(user);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String userName, HttpServletRequest request) {
        if(!isAdmin(request)) {
            // return new ArrayList<>();
            ArrayList<User> objects = new ArrayList<>();
            return ResultUtils.success(objects);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNoneBlank(userName)) {
            queryWrapper.like("userName", userName);
        }
        List<User> usersList = userService.list(queryWrapper);
        // return usersList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        List<User> list = usersList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if(!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "不是管理员，没有权限删除");
        }
        if(id <= 0) {
            return null;
        }

        // return userService.removeById(id);
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 是否为管理员
     */
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObject;
        if(user == null || user.getRole() != ADMIN_ROLE) {
            return false;  // 用户退出了登录状态，或者，是普通用户，返回空
        }
        return true;
    }
}
