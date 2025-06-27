package com.ini.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${file.upload-path}")
	private String uploadPath;

	@Autowired
	LangInterceptor langInterceptor;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 업로드 폴더 매핑 (기존 코드)
		String path = uploadPath.replace("\\", "/");
		if (!path.endsWith("/")) {
			path += "/";
		}
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + path);
	}

	// 기본 Locale 설정 (한국어)
	@Bean
	public LocaleResolver localeResolver() {
		return new SessionLocaleResolver(); // 세션 기반
	}

	// 필요 시 언어 변경을 위한 파라미터 설정 (예: ?lang=ja)
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");
		return interceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(langInterceptor).addPathPatterns("/**"); // 네가 만든 인터셉터
		registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/**"); // 이 줄 추가
	}

}
