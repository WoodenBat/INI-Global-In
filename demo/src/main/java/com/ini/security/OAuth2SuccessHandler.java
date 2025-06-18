package com.ini.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		Object principal = authentication.getPrincipal();

		if (principal instanceof OAuth2User) {
			OAuth2User oauthUser = (OAuth2User) principal;

			// 세션에 유저 정보 저장 (필요시)
			request.getSession().setAttribute("user", oauthUser.getAttributes());

			// 로그인 성공 후 홈으로 리다이렉트
			response.sendRedirect("/");
		} else {
			// 예상치 못한 경우 처리 (보통 여기로 오지 않음)
			response.sendRedirect("/login?error");
		}
	}
}