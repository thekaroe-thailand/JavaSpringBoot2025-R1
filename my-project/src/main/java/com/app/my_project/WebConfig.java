package com.app.my_project;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.app.my_project.interceptor.JwtInterceptor;
import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static String secret;
    private final JwtInterceptor jwtInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    public static String getSecret() {
        if (secret == null) {
            Dotenv dotenv = Dotenv.configure()
                    .directory(System.getProperty("user.dir") + "/my-project")
                    .load();
            secret = dotenv.get("JWT_SECRET");
        }
        return secret;
    }
}