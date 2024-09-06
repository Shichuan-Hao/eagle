package cn.byteswalk.eaglemqconsole.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: Shaun Hao
 * @CreateTime: 2024-09-06 11:11
 * @Description: 跨域
 * @Version: 1.0
 */
@Configuration
public class CorsConfig
        implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 是否发送 Cookie
                .allowCredentials(true)
                // 放开哪些原始域
                .allowedOrigins("*")
                .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"})
                .allowedHeaders("*")
                .exposedHeaders("*");
    }
}

