package com.missyang.usercenter1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.missyang.usercenter1.mapper")
@SpringBootApplication
public class UserCenter1Application {

    public static void main(String[] args) {
        SpringApplication.run(UserCenter1Application.class, args);
    }

}
