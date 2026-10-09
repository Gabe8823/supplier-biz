package com.sup.supplierbiz.config;

import com.sup.supplierbiz.common.interceptor.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置，注册 JWT 拦截器
 *
 * @author sup
 * @date 2026-10-09
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig  implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/login","/doc.html","/swagger-ui/**","/v3/api-docs/**","/swagger-resources/**","/webjars/**");
    }
}
