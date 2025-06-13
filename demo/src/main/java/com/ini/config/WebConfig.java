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
		// 예: http://localhost:8080/images/profile/파일명 으로 접근 가능
		registry.addResourceHandler("/images/profile/**")
		.addResourceLocations("file:///" + uploadPath + "/");
	}
}
