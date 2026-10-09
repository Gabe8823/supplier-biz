package com.sup.supplierbiz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置属性
 *
 * @author sup
 * @date 2026-10-09
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {
    private String secret;
    private Long expiration;
}
