package com.sup.supplierbiz.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j / SpringDoc OpenAPI 全局配置
 *
 * @author Gabe
 * @date 2026-10-19
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("供应链业务系统 API")
                        .version("1.0.0")
                        .description("Spring Boot 3 + Knife4j OpenAPI 3.0 文档"))
                // 声明一个名为 "Bearer Authentication" 的安全方案
                .schemaRequirement("Bearer Authentication",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("请输入登录后获得的 JWT token，不要加 Bearer 前缀，系统会自动补上"))
                // 全局生效：所有接口都需要这个安全方案
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"));
    }
}
