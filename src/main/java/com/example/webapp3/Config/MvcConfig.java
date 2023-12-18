package com.example.webapp3.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;
    @Value("${upload.ImgPath}")
    private String uploadImgPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**")
                .addResourceLocations("file:///" + uploadPath + "/");
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///" + uploadImgPath + "/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/something").setViewName("Main");
        registry.addViewController("/generate").setViewName("GenPassword");

        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/hello").setViewName("hello");
    }
}
