package com.ini.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ini.member.service.MemberService;
import com.ini.member.vo.MemberDTO;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String userId = authentication.getName(); // username 값
        MemberDTO member = memberService.findMemberById(userId);

        if (member != null) {
            HttpSession session = request.getSession();
            session.setAttribute("nickname", member.getUser_nickname());
        }

        response.sendRedirect("/home"); // 로그인 성공 후 홈으로 이동
    }
}
