package com.school.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI配置
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("学校管理系统 API")
                        .description("学校管理系统接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Agnes AI")
                                .email("agent@sapiens.ai")));
    }
}
