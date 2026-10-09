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


/**
 * JWT 工具类
 *
 * @author sup
 * @date 2026-10-09
 */
@Component
public class JWTUtils {
    /** JWT 签名密钥 */
    @Value("${jwt.secret}")
    private String secret;
    /** JWT 过期时间（毫秒） */
    @Value("${jwt.expiration}")
    private long expiration;
    /** HMAC 密钥对象，懒加载 */
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
                //设置主题
                .setSubject(String.valueOf(userId))
                //声明
                .claim("username",username)
                //签发时间
                .setIssuedAt(now)
                //过期时间
                .setExpiration(expireDate)
                //key签名
                .signWith(key)
                //生成jwt字符串
                .compact();
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public Claims parseToken(String token){
        return Jwts.parserBuilder()
                //同一把钥匙解析
                .setSigningKey(key)
                .build()
                //解析、验签、验过期
                .parseClaimsJws(token)
                // 获得载荷
                .getBody();
    }

    public boolean validateToken(String token){
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            //token过期
            return false;
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
