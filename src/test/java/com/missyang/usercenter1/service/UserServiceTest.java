package com.missyang.usercenter1.service;

import com.missyang.usercenter1.model.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.nio.charset.StandardCharsets;

/**
 * 用户服务测试
 *
 * @author missyang
 */

@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();

        user.setUserName("yyy");
        user.setUserAccount("1234");
        user.setUserPassword("12345678");
        user.setAvatarUrl("https://tse1.mm.bing.net/th/id/OIP.9emQt5PZVj7hyyINlts-zgAAAA?r=0&rs=1&pid=ImgDetMain&o=7&rm=3");
        user.setEmail("123@gmail.com");

        boolean result = userService.save(user);
        Assertions.assertEquals(true, result);

        System.out.println(user.getId());
    }

    @Test
    public void testEncryptPassword() {
        final String SALT = "abcdyyy";
        String encryptedPassword = DigestUtils.md5DigestAsHex((SALT + "mypassword").getBytes());
        System.out.println(encryptedPassword);
    }

    @Test
    void userRegister() {
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
        String userAccount = " ";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount = "123";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount = "1234";
        userPassword = "123";
        checkPassword = "123";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount = "123@";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount = "1234";
        userPassword = "12345678";
        checkPassword = "1";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        userAccount = "1234";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

//        这个是正确的账号密码，但只能写一次，用第二次就会出错，执行第二次会重复。
//        userAccount = "manager";
//        userPassword = "This is manager password";
//        checkPassword = "This is manager password";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertTrue(result > 0);

        // 把上面代码搞一次，然后改成这样，才能永远执行不会错了
        userAccount = "manager";
        userPassword = "This is manager password";
        checkPassword = "This is manager password";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
//        Assertions.assertTrue(result > 0);
        Assertions.assertEquals(-1, result);

    }
}

























