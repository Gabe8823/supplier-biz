package com.sup.supplierbiz.util;

import cn.hutool.core.date.DateTime;
import com.sup.supplierbiz.config.JWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
//@RequiredArgsConstructor
public class JWTUtils {
    //private final JWTProperties jwtProperties;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;
    private SecretKey key;


    /**
     * 初始化
     */
    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成token
     * @param userId
     * @param username
     * @return
     */
    public String generateToken (Long userId,String username){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) //设置主题
                .claim("username",username) //声明
                .setIssuedAt(now)//签发时间
                .setExpiration(expireDate)//过期时间
                .signWith(key)//key签名
                .compact();//生成jwt字符串
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public Claims parseToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key) //同一把钥匙解析
                .build()
                .parseClaimsJws(token)//解析、验签、验过期
                .getBody();// 获得载荷
    }

    public boolean validateToken(String token){
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;//token过期
        } catch (Exception e){
            return false;
        }
    }
    public Long getUserId(String token){
        return Long.valueOf(parseToken(token).getSubject());
    }
    public String getUsername(String token){
        return parseToken(token).get("username",String.class);
    }
}
