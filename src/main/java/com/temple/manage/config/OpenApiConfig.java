package com.temple.manage.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * openapi配置
 * </p>
 *
 * @author messi
 * @package com.temple.manage.config
 * @description openapi配置
 * @date 2021-12-08 21:05
 * @verison V1.0.0
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .servers(servers())
                .info(info())
                .components(components())
                .externalDocs(externalDocumentation());
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes("Authorization", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
    }

    private List<Server> servers() {
        List<Server> list = new ArrayList<>();
        list.add(new Server().url("http://localhost/smanage").description("本地"));
        list.add(new Server().url("http://101.33.253.244/smanage").description("测试"));
        return list;
    }

    /**
     * 定义外部文档
     */
    private ExternalDocumentation externalDocumentation() {
        return new ExternalDocumentation()
                .description("外部文档")
                .url("https://github.com/damingerdai/hoteler");
    }

    /**
     *定义License
     */
    private License license() {
        return new License()
                .name("MIT")
                .url("https://opensource.org/licenses/MIT");
    }

    /**
     *定义Open Api的基本信息
     */
    private Info info() {
        return new Info()
                .title("Manage Open API")
                .description("Manage Open API")
                .version("v1.0.0")
                .license(license());
    }
}
