package com.ini.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ini.filter.XSSFilter;

@Configuration
public class FilterConfig {

	@Bean
    public FilterRegistrationBean<XSSFilter> xssFilter() {
        FilterRegistrationBean<XSSFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XSSFilter());
        registration.addUrlPatterns("/*");  // 전체 요청 대상
        registration.setOrder(1);           // 필터 실행 순서
        return registration;
    }
}
