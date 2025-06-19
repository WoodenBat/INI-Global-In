package com.ini.security;

import com.ini.security.LoginSuccessHandler; // LoginSuccessHandler import 추가
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final LoginSuccessHandler loginSuccessHandler; // LoginSuccessHandler 주입

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/login", "/login/**", "/oauth2/**", "/finding-password", "/find-by-id",
								"/find-by-email", "/finding-password/by-id", "/finding-password/by-email",
								"/send-password-email", "/css/**", "/js/**", "/images/**", "/fonts/**",
								"/member/signup", "/member/checkId", "/member/checkEmail", "/member/checkNickname")
						.permitAll().anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").usernameParameter("username")
						.passwordParameter("password").successHandler(loginSuccessHandler) // 여기서 LoginSuccessHandler 지정
						.permitAll())
				.oauth2Login(oauth2 -> oauth2.loginPage("/login")
						.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
						.defaultSuccessUrl("/home", true));

		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}