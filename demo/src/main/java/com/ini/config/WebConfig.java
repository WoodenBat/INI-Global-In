package com.ini.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 슬래시 통일
    	String path = uploadPath.replace("\\", "/");
    	if (!path.endsWith("/")) {
    	    path += "/";
    	}

        // file: 프로토콜  (file:///은 절대경로 앞에 쓰임)
    	registry.addResourceHandler("/uploads/**")
        		.addResourceLocations("file:" + path);
    }
}

