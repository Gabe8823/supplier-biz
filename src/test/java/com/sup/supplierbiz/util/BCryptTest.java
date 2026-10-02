package com.sup.supplierbiz.util;

import cn.hutool.crypto.digest.BCrypt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BCryptTest {
    @Test
    public void testBCrypt(){
        String password = "123456";
        System.out.println(BCrypt.hashpw(password));
    }

}
