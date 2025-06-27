package com.ini.config;

import java.util.Locale;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LangInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		HttpSession session = request.getSession();
		String lang = (String) session.getAttribute("lang");

		if (lang == null) {
			lang = "ko";
			session.setAttribute("lang", lang);
		}

		Locale locale = switch (lang) {
		case "ja" -> Locale.JAPANESE;
		default -> Locale.KOREAN;
		};

		// 핵심: SessionLocaleResolver에 직접 넣어야 함
		session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);

		return true;
	}
}
