package com.sup.supplierbiz.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JWTUtilsTest {
    @Autowired
    private JWTUtils jwtUtils;
    @Test
    public void testJwt(){
        String token = jwtUtils.generateToken(1L, "jack");
        System.out.println("生成的token:" + token);
        System.out.println("是否有效：" + jwtUtils.validateToken(token));
        System.out.println("用户id：" + jwtUtils.getUserId(token));
        System.out.println("用户名：" + jwtUtils.getUsername(token));
        System.out.println("篡改后是否有效：" + jwtUtils.validateToken(token + "x"));
    }

}
